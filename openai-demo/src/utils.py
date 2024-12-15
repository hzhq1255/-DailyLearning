import os
import re

def parse_java_class(file_path, base_package):
    """解析 Java 文件，通过 import 语句提取类的依赖关系，且只包含当前项目包下的依赖"""
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # 提取所有 import 语句
    import_statements = re.findall(r'import\s+([a-zA-Z0-9_.]+);', content)

    # 只保留以 base_package 为前缀的依赖类
    import_statements = [imp for imp in import_statements if imp.startswith(base_package)]

    return {
        'class_name': get_class_name(content),
        'dependencies': import_statements
    }

def get_class_name(content):
    """从文件内容中提取类名（包括 class、interface 和 enum）"""
    # 正则表达式直接匹配 public class, public interface, public enum
    match = re.search(r'\bpublic\s+(class|interface|enum)\s+([A-Za-z_][A-Za-z0-9_<>]*)\b', content)
    return match.group(2) if match else None

def find_all_java_files(root_dir):
    """遍历目录，找到所有 Java 文件"""
    java_files = []
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith('.java'):
                java_files.append(os.path.join(root, file))
    return java_files

def get_dependencies(java_files, base_package):
    """提取每个 Java 文件的依赖关系（类和引用类），且只包含当前项目包下的依赖"""
    dependencies = {}
    for file in java_files:
        class_info = parse_java_class(file, base_package)
        dependencies[class_info['class_name']] = class_info['dependencies']
    return dependencies

def get_base_package(file_path):
    """从 Java 文件中提取包名"""
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    match = re.search(r'package\s+([a-zA-Z0-9_.]+);', content)
    return match.group(1) if match else None



java_files = find_all_java_files("./sdk")
dependencies = get_dependencies(java_files, "org.openapitools.client")

print('java_files:')
print(java_files)
print('dependencies:')
print(dependencies)
