from openai import OpenAI

from config import OPENAI_API_KEY
from config import OPENAI_API_BASE_URL

# api_key = os.getenv("OPENAI_API_KEY")
# base_url = os.getenv("OPENAI_API_BASE_URL")

print(OPENAI_API_KEY)
print(OPENAI_API_BASE_URL)

client: OpenAI = OpenAI(
    base_url=OPENAI_API_BASE_URL+"/v1",
    api_key=OPENAI_API_KEY
)

completion = client.chat.completions.create(
    model="gpt-3.5-turbo",
    messages=[
        {"role": "system", "content": """
        你是熟悉python的专家
         """},
        {"role": "user", "content": """
        实现一个函数，输入一个字符串，返回该字符串的反转
         """,},
    ]
)

for choice in completion.choices:
    print(choice.message.content)
