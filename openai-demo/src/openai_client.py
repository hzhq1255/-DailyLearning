from openai import OpenAI
import os
import glob

# api_key = os.getenv("OPENAI_API_KEY")
# base_url = os.getenv("OPENAI_API_BASE_URL")

client: OpenAI = OpenAI(
    base_url=base_url,
    api_key=api_key
)


completion = client.chat.completions.create(
    model="gpt-3.5-turbo",
    messages=[
        {"role": "system", "content": """
         你是一个精通Kubernetes的专家，对Kubernetes有深入了解，熟悉工作原理和每个API和对象,
         同时你也是一名JAVA开发者对其非常熟悉，能够用Java语言实现Kubernetes的API调用。
         """},
        {"role": "user", "content": """
         目前有我使用OpenAPI-Generator工具基于OpenAPI描述文件生成的SDK，
         我将提供对应的类以及依赖的类，你需要帮我完成生成对应的API调用和对应的资源对象的测试用例这个任务。
         你需要输出代码以及测试用例，且输出的代码格式为Java，测试用例格式为JUnit。
         """,},
    ]
)

for choice in completion.choices:
    print(choice.message.content)
