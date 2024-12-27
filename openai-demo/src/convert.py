import os
import pandas as pd

def convert_csv_to_xlsx(input_dir, output_dir, error_log="convert_failed_files.txt"):
    """
    将指定目录下的所有 CSV 文件转换为 XLSX 文件，并记录转换失败的文件。
    
    Args:
        input_dir (str): 包含 CSV 文件的目录路径。
        output_dir (str): 输出 XLSX 文件的目录路径。
        error_log (str): 保存转换失败文件路径的日志文件名。
    """
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)  # 如果输出目录不存在，则创建

    failed_files = []  # 用于存储失败的文件路径

    for file_name in os.listdir(input_dir):
        if file_name.endswith(".csv"):  # 仅处理 CSV 文件
            csv_path = os.path.join(input_dir, file_name)
            xlsx_path = os.path.join(output_dir, file_name.replace(".csv", ".xlsx"))

            try:
                # 读取 CSV 文件
                df = pd.read_csv(csv_path)
                # 写入到 XLSX 文件
                df.to_excel(xlsx_path, index=False, engine="openpyxl")
                print(f"成功将 {file_name} 转换为 {xlsx_path}")
            except Exception as e:
                print(f"处理 {file_name} 时出错: {e}")
                failed_files.append(csv_path)

    # 将失败的文件路径写入日志
    if failed_files:
        with open(error_log, "w", encoding="utf-8") as f:
            f.writelines(f"{file}\n" for file in failed_files)
        print(f"转换失败的文件已记录到 {error_log}")
    else:
        print("所有文件均已成功转换！")

# 示例调用
input_directory = "/workspace/Scratches/infra-2.1-outputs"  # 替换为实际的 CSV 文件目录路径
output_directory = "/workspace/Scratches/infra-2.1-tables"  # 替换为实际的输出目录路径
convert_csv_to_xlsx(input_directory, output_directory)
