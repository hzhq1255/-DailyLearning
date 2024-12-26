from logging import log
import os
from pathlib import Path
import re
from turtle import width
from urllib import response
from venv import logger
from openai import OpenAI, chat, APIStatusError, APIError
from config import OPENAI_API_KEY
from config import OPENAI_API_BASE_URL


client: OpenAI = OpenAI(base_url=OPENAI_API_BASE_URL + "/v1", api_key=OPENAI_API_KEY)

print("当前 OpenAI API KEY:", OPENAI_API_KEY)
print("当前 OpenAI API BASE URL:", OPENAI_API_BASE_URL)

# read file


def build_system_prompt():
    example_file = "/workspace/Scratches/基线标准示例.csv"
    example_file_content = ""
    with open(example_file, "r", encoding="utf-8") as f:
        example_file_content = f.read()
    rule_file = "/workspace/Scratches/检查方式.txt"
    rule_file_content = ""
    with open(rule_file, "r", encoding="utf-8") as f:
        rule_file_content = f.read()
    # docker_file = "/workspace/Scratches/docker - 19.03.15.md"
    # docker_file_content = ""
    # with open(docker_file, "r", encoding="utf-8") as f:
    #    docker_file_content = f.read()
    # logpilot_file = "/workspace/Scratches/logpilot - 0.9.7-filebeat-hc.8-20241009.md"
    # logpilot_file_content = ""
    # with open(logpilot_file, "r", encoding="utf-8") as f:
    #    logpilot_file_content = f.read()
    print(f"exmaple_content\n{example_file_content}")
    return [
        {
            "role": "system",
            "content": """
        你对Kubernetes相关组件有深入了解，理解linux系统和内核的配置，我有一个基线检查的需求，检查组件的配置如kubernetes的etcd的配置等。
        但是你需要按照我提供的格式进行学习，我会提供对应文件，第一份是基线标准示例的表格，第二份为检查基线表格中检查方式规则，第三份对应整理过的基线文档。
        你需要根据，检查基线的表格示例，完成基线文档的表格,使用检查方式规则进行说明，渲染对应的Monitor和Checker资源，输出格式为csv。
         """,
        },
        {
            "role": "system",
            "content": f"这是基线标准示例，格式为csv,文件内容为:\n{example_file_content}",
        },
        {
            "role": "system",
            "content": f"这是基线表格中检查方式规则，文件内容为:\n{rule_file_content}",
        },
        # {
        #     "role": "system",
        #     "content": f"这是基线标准表格示例中对应的两份文档，docker 的:\n{docker_file_content}\n"
        # }
    ]


def build_user_prompt(doc_file):
    doc_file_content = ""
    with open(doc_file, "r", encoding="utf-8") as f:
        doc_file_content = f.read()
    return [
        {
            "role": "user",
            "content": f"""
            现在你已经学会了基线标准示例和基线表格中检查方式规则，接下来我将提供一个基线文档，你需要帮我完成基线文档的表格，
            1. 这是基线文档，文件内容为:\n{doc_file_content}。
            2. 检查项、检查值、检查方式是需要使用双引号包裹，仅输出对应的csv文件内容，无需任何解释或注释。
            """,
        }
    ]


def chat_completion(system_prompt, user_prompt):
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=system_prompt + user_prompt,
        temperature=0,
        max_tokens=2048,
        stop=["\n\n"],
        # stream=True,
    )
    content = ""
    # for chunk in response:
    #     content = chunk.choices[0].message.content
    for choice in response.choices:
        content += choice.message.content
    print("------当前输出内容------")
    print(content)
    return extract_code_block(content)


def extract_code_block(content):
    code_blocks = re.findall(r"```(?:\w+\n)?(.*?)```", content, re.DOTALL)
    return "\n".join(code_blocks)


def list_doc_files(path):
    files = []
    for file in os.listdir(path):
        if os.path.isfile(os.path.join(path, file)):
            files.append(file)
    return files


if __name__ == "__main__":
    doc_dir = "/workspace/Scratches/infra-2.1"
    output_dir = "/workspace/Scratches/infra-2.1-outputs"
    doc_files = list_doc_files(doc_dir)
    failed_doc_files = []
    for doc_file in doc_files:
        doc_filename = Path(doc_file).stem
        system_prompt = build_system_prompt()
        user_prompt = build_user_prompt(doc_file=f"{doc_dir}/{doc_file}")
        output_file = f"{output_dir}/{doc_filename}.csv"
        print(f"开始生成 {doc_file} 文件的基线检查项表格", )
        try:
            response = chat_completion(system_prompt, user_prompt)
            with open(output_file, "w", encoding="utf-8") as f:
                f.write(response)
        except APIStatusError as e:
            print(f"生成 {doc_file} 基线检查项表格失败, 错误信息{e}", )
            failed_doc_files.append(doc_file)
        else:
            print(f"生成 {doc_file} 基线检查项表格完成")
        break
    print(f"生成失败的文件{failed_doc_files}")
    with open("failed_files.txt", "w", encoding="utf-8") as f:
        f.writelines(failed_doc_files)
    # system_prompt = build_system_prompt()
    # doc_file = "/workspace/Scratches/infra-2.1/docker - 19.03.15.md"
    # user_prompt = build_user_prompt(doc_file=doc_file)
    # response = chat_completion(system_prompt, user_prompt)
    # output_file = "/workspace/Scratches/infra-2.1-outputs/output.csv"
    # with open(output_file, "w", encoding="utf-8") as f:
    #     f.write(response)
