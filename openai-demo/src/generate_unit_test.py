import os
import shutil
import re
import subprocess
from openai import OpenAI

import utils

api_key = ""
base_url = ""


client: OpenAI = OpenAI(base_url=base_url, api_key=api_key)


def send_prompt(prompt):
    """
    发送单个请求给 ChatGPT 获取结果
    """
    response = client.chat.completions.create(
        temperature=0.2,
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """你是一个精通Kubernetes的专家,对Kubernetes有深入了解，熟悉工作原理和每个API和对象，同时你也是一名Java开发者，熟悉Java开发，能够帮我生成Java代码。请确保只输出Java代码，不要附加任何解释。""",
            },
            {
                "role": "system",
                "content": """
                Kubernetes 对象通常由MetaData,Spec和Status组成，MetaData包含对象的名称，标签和注释，Spec包含对象的配置信息，包括Pod的配置信息，Service的配置信息等等。
                """,

            },
            {
                "role": "user",
                "content": """我将提供类文件和它的依赖，你需要帮我生成对应的API调用和单元测试代码，格式为Java和JUnit, Junit版本v5，生成的实体类不支持链式的set。请只输出代码，不要附加任何额外信息。""",
            },
            {"role": "user", "content": prompt},
        ],
        stop=["\n\n"],
    )
    for choice in response.choices:
        print(choice.message.content)
    return response.choices[0].message.content.strip()


def send_prompt_in_batches(prompt, batch_size=5):
    """
    将请求分批发送，避免传输过长的内容。
    """
    # 将长文本按 batch_size 分割
    batched_prompts = [prompt[i:i + batch_size] for i in range(0, len(prompt), batch_size)]
    responses = []
    for batch_prompt in batched_prompts:
        response = send_prompt(batch_prompt)
        responses.append(response)
    return "\n".join(responses)


def extract_code(response):
    """
    提取 Java 代码块中的内容（假设是 Java 代码）
    """
    code_match = re.search(r"```java\n(.*?)```", response, re.DOTALL)
    if code_match:
        return code_match.group(1).strip()
    return response.strip()


def remove_comments(java_file_content):
    """
    去除 Java 代码中的单行和多行注释
    """
    # 去除单行注释
    java_file_content = re.sub(r'//.*', '', java_file_content)
    # 去除多行注释
    java_file_content = re.sub(r'/\*.*?\*/', '', java_file_content, flags=re.DOTALL)
    return java_file_content


def get_method_signatures(java_file_content):
    """
    提取所有 public 方法的签名
    """
    java_file_content = remove_comments(java_file_content)
    
    method_pattern = r'public\s+([a-zA-Z_][a-zA-Z0-9_<>]*)\s+([a-zA-Z_][a-zA-Z0-9_]*)\s*\(([^)]*)\)\s*\{'
    matches = re.findall(method_pattern, java_file_content)
    
    method_signatures = []
    for match in matches:
        return_type = match[0].strip()  # 返回类型
        method_name = match[1].strip()  # 方法名
        parameters = match[2].strip()  # 参数列表
        method_signatures.append(f"{return_type} {method_name}({parameters})")
    return method_signatures


dependency_cache = {}

def get_dependency_content(dependency):
    """
    从文件系统中读取依赖类的内容，避免重复读取
    """
    if dependency in dependency_cache:
        return dependency_cache[dependency]

    dependency_file = "./sdk/src/main/java/" + dependency.replace(".", "/") + ".java"
    with open(dependency_file, "r", encoding="utf-8") as f:
        dependency_content = f.read()

    # 缓存依赖内容
    dependency_cache[dependency] = dependency_content
    return dependency_content


def run_mvn_test(test_class):
    """
    运行 Maven 测试并返回测试结果。如果测试失败，返回错误信息。
    """
    print(f"测试类：{test_class}")
    try:
        # 使用 subprocess 直接设置工作目录并执行 mvn test
        result = subprocess.run(
            ["mvn", "test", f"-Dtest={test_class}"],   # 执行 mvn 命令
            capture_output=True,                       # 捕获标准输出和标准错误
            text=True,                                # 捕获文本输出
            check=True,                               # 如果命令返回非零退出码，抛出 CalledProcessError
            cwd="/home/hzhq/Workspace/MyProjects/DailyLearning/openai-demo/sdk"  # 设置工作目录为 sdk
        )

        # 如果命令执行成功，返回 True 和标准输出
        return True, result.stdout

    except subprocess.CalledProcessError as e:
        # 捕获错误并打印错误信息
        # print(f"测试失败，错误信息：{e.output}")
        # 返回 False 和标准错误流
        return False, e.output


def filter_errors(class_name,log):
    """
    过滤日志，保留 'ERROR' 及相关的符号和位置错误信息（与 'PodApiTest' 相关）。
    """

    cnt = 0
    is_error = False
    lines = []
    for line in log.split("\n"):
        if "[ERROR]" in line:
            if class_name in line:
                lines.append(line)
                is_error = True
                cnt += 1
            else:
                if cnt <=2 and is_error:
                    lines.append(line)
                else:
                    is_error = False
                    cnt = 0    

    return "\n".join(lines)

def generate_unit_test():
    # 获取所有测试文件
    java_files = utils.find_all_java_files("./sdk/src/test/java")

    # 获取依赖
    dependencies = utils.get_dependencies(
        "./sdk/src/main/java", "./sdk/src/test/java", "org.openapitools.client"
    )

    for file in java_files:
        if "Test" not in file:
            continue
        if os.path.exists(file + ".bak"):
            shutil.copy(file, file + ".bak")

        # 获取类名
        class_name = utils.get_class_name(open(file, "r").read())
        dependencies_str = ", ".join(dependencies[class_name])
        
        filename = file.split("/")[-1]
        if "api/" in file:
            prompt = f"补充完善该单元测试文件，文件名为 {filename}, 文件路径为 {file}，类名为 {class_name}。\n"
        else:
            prompt = f"补充完善该单元测试文件，文件名为 {filename}, 文件路径为 {file}，类名为 {class_name}，Model 类只需要完善他自身方法的单元测试,不需要依赖其他类。\n"
        # 将 Java 文件内容读取到提示语中
        with open(file, "r", encoding="utf-8") as f:
            prompt += f.read() + "\n"
        
        for dependency in dependencies[class_name]:
            dependency_content = get_dependency_content(dependency)
            dependency_content = remove_comments(dependency_content)
            method_signatures = get_method_signatures(dependency_content)
            method_signatures_str = "\n".join(method_signatures)
            # print("class_name:", class_name, "method_signatures:", method_signatures_str)
            if class_name in dependency:
                # 需要Mock的类
                prompt += f"需要进行单元测试的原始 {dependency} 文件: \n"
                prompt += f"{dependency_content}\n"
                continue
            else:
                pass
                # 需要Mock的方法
                prompt += f"可能依赖 {dependency} 中的方法签名: \n"
                prompt += f"{method_signatures_str}\n"
        # 将提示信息分批发送，避免内容过长
        full_response = send_prompt(prompt)

        # 提取生成的 Java 代码
        generated_code = extract_code(full_response)
        # 将生成的代码写入文件
        with open(file, "w", encoding="utf-8") as f:
            f.write(generated_code)            
        retries = 0
        max_retries = 3
        while retries < max_retries:
            # 运行 Maven 测试
            # 正则替换 src/test/java/ 为 ""
            filepath = re.sub(r"^.*src/test/java/", "", file)  # 去除前缀
            java_package_class = re.sub(r"\.java$", "", filepath)  # 去除文件扩展名
            java_package_class = java_package_class.replace("/", ".") 
            print("java_package_class:", java_package_class)
            _, test_output = run_mvn_test(filepath)
            # filter error from test_output
            test_output = filter_errors(class_name, test_output)
            err_prompt = f"生成代码: {generated_code}\n 当前测试失败，错误信息如下:\n{test_output}\n请修复上述错误，继续生成代码。\n"
            if test_output == "":
                print(f"测试通过，文件已生成: {file}")
                break
            else: 
                print(f"测试失败，错误信息：{test_output}")
            # 发送修复请求
            response = send_prompt(err_prompt)
            generated_code = extract_code(response)
            # 写入修复后的代码
            with open(file, "w", encoding="utf-8") as f:
                f.write(generated_code)
            
            retries += 1
            print(f"重试 {retries}/{max_retries} 次")

        if retries == max_retries:
            print("已达到最大重试次数，停止修复。")
        else:
            print(f"最终生成的代码: {file}")


        print(f"文件 {file} 处理完成！")




generate_unit_test()
