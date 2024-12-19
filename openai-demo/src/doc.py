from openai import OpenAI
from config import OPENAI_API_KEY
from config import OPENAI_API_BASE_URL


client: OpenAI = OpenAI(base_url=OPENAI_API_BASE_URL + "/v1/chat/completions", api_key=OPENAI_API_KEY)


print("上传检查方式文档")
check_study_file = client.files.create(file=open("/workspace/Scratches/检查方式.txt", "rb"), purpose="fine-tune")

print("上传基线标准文档")
baseline_table_file = client.files.create(file=open("/workspace/Scratches/基线标准-示例.xlsx", "rb"), purpose="fine-tune")

print("上传原始基线文档")
orginal_baseline_file = client.files.create(file=open("/workspace/Scratches/infra-1.3/容器运行时_docker_19.03.15.md", "rb"), purpose="assistant")

print("创建基线检查机器人")
my_assistant = client.beta.assistants.create(
    model="gpt-3.5-turbo-1106",
    instructions="""
    你对Kubernetes相关组件有深入了解，理解linux系统和内核的配置，我将提供对应检查规范和示例文档。
    """,
    name="Baseline Assistant",
    tools=[{"type": "retrieval"}]
)

print("创建对话")
my_thread = client.beta.threads.create()
my_thread_message = client.beta.threads.messages.create(
  thread_id=my_thread.id,
  role="assistant",
  content="目前提供的是对应的检查方式的说明文档。",
  file_ids=[check_study_file.id]
)


print("提供对应的基线标准整理表格")
my_thread_message = client.beta.threads.messages.create(
  thread_id=my_thread.id,
  role="assistant",
  content="目前提供的是对应的基线标准整理表格的示例文件。",
  file_ids=[baseline_table_file.id]
)

print("提供对应的原始基线文档")
my_thread_message = client.beta.threads.messages.create(
  thread_id=my_thread.id,
  role="user",
  content="现在你是一个了解基线检查方式并且熟悉对应基线标准整理表格规范的机器人，使用你已经了解知识，根据对应的提供的基线文档，转换成对应的基线标准整理表格",
  file_ids=[orginal_baseline_file.id]
)


print("运行对话")
my_run = client.beta.threads.runs.create(
  thread_id=my_thread.id,
  assistant_id=my_assistant.id,
)

print("继续对话")
keep_retrieving_run = client.beta.threads.runs.retrieve(
    thread_id=my_thread.id,
    run_id=my_run.id
)

print("获取所有消息")
all_messages = client.beta.threads.messages.list(
    thread_id=my_thread.id
)

print(f"User: {my_thread_message.content[0].text.value}")
print(f"Assistant: {all_messages.data[0].content[0].text.value}")
