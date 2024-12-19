import os
import re

def get_class_name(content):
    """从文件内容中提取类名（包括 class、interface 和 enum）"""
    match = re.search(r'\bpublic\s+(class|interface|enum)\s+([A-Za-z_][A-Za-z0-9_<>]*)\b', content)
    return match.group(2) if match else None

def parse_java_class(file_path, base_package, all_classes_in_src):
    """解析 Java 文件，提取类的依赖关系（包括 import 和同包下的类）"""
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # 提取类名
    class_name = get_class_name(content)

    # 提取 import 语句中的依赖
    import_statements = re.findall(r'import\s+([a-zA-Z0-9_.]+);', content)
    # 只考虑当前包内的类
    import_statements = [imp for imp in import_statements if imp.startswith(base_package)]

    # 对于 test 类，依赖 src 包下所有同包的类
    dependencies = []
    if '/test/' in file_path:
        # 提取当前包的所有类（隐式依赖）
        package = get_base_package(file_path)
        dependencies = [cls for cls in all_classes_in_src if cls.startswith(package)]

    # 返回 import 语句的依赖和隐式依赖
    return {
        'class_name': class_name,
        'dependencies': list(set(import_statements + dependencies))  # 去重依赖
    }

def get_base_package(file_path):
    """从 Java 文件中提取包名"""
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    match = re.search(r'package\s+([a-zA-Z0-9_.]+);', content)
    return match.group(1) if match else None

def find_all_java_files(root_dir):
    """遍历目录，找到所有 Java 文件"""
    java_files = []
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith('.java'):
                java_files.append(os.path.join(root, file))
    return java_files

def get_all_classes_in_src(src_dir):
    """获取 src 目录下所有类的类名（包括包路径）"""
    all_classes = []
    for root, dirs, files in os.walk(src_dir):
        for file in files:
            if file.endswith('.java'):
                package = get_base_package(os.path.join(root, file))
                class_name = get_class_name(open(os.path.join(root, file), 'r').read())
                if package and class_name:
                    all_classes.append(f'{package}.{class_name}')
    return all_classes

def get_dependencies(src_dir, test_dir, base_package):
    """提取每个 Java 文件的依赖关系（类和引用类），并根据目录区分 src 和 test 类"""
    # 获取 src 目录下所有类
    all_classes_in_src = get_all_classes_in_src(src_dir)

    # 获取 test 目录下的所有 Java 文件
    test_java_files = find_all_java_files(test_dir)

    # 获取 src 目录下的所有 Java 文件
    src_java_files = find_all_java_files(src_dir)

    dependencies = {}

    # 解析 src 类的依赖
    for file in src_java_files:
        class_info = parse_java_class(file, base_package, all_classes_in_src)
        dependencies[class_info['class_name']] = class_info['dependencies']

    # 解析 test 类的依赖
    for file in test_java_files:
        class_info = parse_java_class(file, base_package, all_classes_in_src)
        dependencies[class_info['class_name']] = class_info['dependencies']

    return dependencies

dependencies = get_dependencies('./sdk/src/main/java', './sdk/src/test/java', "org.openapitools.client")

# print('dependencies:')
# print(dependencies)
# print(dependencies['PodStatusTest'])
