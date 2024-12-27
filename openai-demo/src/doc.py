import os
import re
import logging
from pathlib import Path
from time import sleep
from openai import OpenAI, APIStatusError
from config import OPENAI_API_KEY, OPENAI_API_BASE_URL

# 配置 OpenAI 客户端
client: OpenAI = OpenAI(base_url=f"{OPENAI_API_BASE_URL}/v1", api_key=OPENAI_API_KEY)

# 日志配置
logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)


def read_file(file_path: str) -> str:
    """读取文件内容"""
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            return f.read()
    except Exception as e:
        logger.error(f"读取文件 {file_path} 失败: {e}")
        raise


def write_file(file_path: str, content: str):
    """写入文件内容"""
    try:
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
    except Exception as e:
        logger.error(f"写入文件 {file_path} 失败: {e}")
        raise


def build_system_prompt() -> list:
    """构建系统提示"""
    example_file = "/workspace/Scratches/基线标准-示例.csv"
    rule_file = "/workspace/Scratches/检查方式.txt"
    docker_file = "/workspace/Scratches/docker - 19.03.15.md"

    example_content = read_file(example_file)
    rule_content = read_file(rule_file)
    docker_content = read_file(docker_file)

    return [
        {
            "role": "system",
            "content": """
            你是 Kubernetes 和 Linux 系统专家，擅长基线配置检查。
            根据我提供的基线标准示例和检查方式规则，生成符合格式的基线文档表格。
            输出格式严格为 CSV，列包括：基线标准、内容组件、检查项、检查值、检查实现方法、风险类型、检查项说明、处理建议、备注。
        """,
        },
        {
            "role": "system",
            "content": f"这是基线标准示例，文件内容如下：\n{example_content}",
        },
        {
            "role": "system",
            "content": f"这是检查方式规则，文件内容如下:\n{rule_content}",
        },
    ]


def build_user_prompt(doc_file: str) -> list:
    """构建用户提示"""
    doc_filename = Path(doc_file).stem
    base_name = doc_filename.split(" ")[0]
    doc_content = read_file(doc_file)

    return [
        {
            "role": "user",
            "content": f"""
            1. 基线标准内容固定为 {base_name}。
            2. 输出表格列包括：基线标准、组件、检查项、检查值、检查实现方法、风险类型、检查项说明、处理建议、备注。
            3. 所有内容使用双引号包裹。
            4. 仅输出 CSV 表格内容，无需解释。
            5. 这是基线文档，文件内容为:\n{doc_content}
            """,
        }
    ]


def chat_completion(system_prompt, user_prompt):
    """调用 OpenAI API 进行对话补全"""
    try:
        response = client.chat.completions.create(
            model="gpt-4o",
            messages=system_prompt + user_prompt,
            temperature=0.2,
            max_tokens=2048,
            # stop=["\n\n"],
        )
        content = "".join(choice.message.content for choice in response.choices)
        logger.info("Chat completion successful.")
        return extract_code_block(content)
    except APIStatusError as e:
        logger.error(f"调用 OpenAI API 失败: {e}")
        raise


def extract_code_block(content: str) -> str:
    """提取代码块内容"""
    code_blocks = re.findall(r"```(?:\w+\n)?(.*?)```", content, re.DOTALL)
    return "\n".join(code_blocks)


def list_doc_files(path: str) -> list:
    """列出指定目录中的文档文件"""
    return [
        os.path.join(path, file)
        for file in os.listdir(path)
        if os.path.isfile(os.path.join(path, file))
    ]


def main():
    doc_dir = "/workspace/Scratches/infra-2.1"
    output_dir = "/workspace/Scratches/infra-2.1-outputs"
    os.makedirs(output_dir, exist_ok=True)

    doc_files = list_doc_files(doc_dir)
    failed_doc_files = []

    for doc_file in doc_files:
        doc_filename = Path(doc_file).stem
        output_file = os.path.join(output_dir, f"{doc_filename}.csv")
        logger.info(f"开始生成 {doc_file} 文件的基线检查项表格")

        try:
            system_prompt = build_system_prompt()
            user_prompt = build_user_prompt(doc_file)
            response = chat_completion(system_prompt, user_prompt)

            logger.info(f"正在写入生成的表格文件 {output_file}")
            write_file(output_file, response)
        except Exception as e:
            logger.error(f"生成 {doc_file} 基线检查项表格失败: {e}")
            failed_doc_files.append(doc_file)
        else:
            logger.info(f"生成 {doc_file} 基线检查项表格完成")
        # break
        logger.info("等待 5s 后继续生成 防止过快触发openai限制")
        sleep(5)
    logger.info(f"生成失败的文件: {failed_doc_files}")
    if failed_doc_files:
        write_file("failed_files.txt", "\n".join(failed_doc_files))


if __name__ == "__main__":
    main()
