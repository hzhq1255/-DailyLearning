import utils

java_files = utils.find_all_java_files("./sdk")
dependencies = utils.get_dependencies(java_files, "org.openapitools.client")

def filter_unit_test_files(java_files):
    unit_test_files = []
    for file in java_files:
        if "test" in file:
            unit_test_files.append(file)
    return unit_test_files

unit_test_files = filter_unit_test_files(java_files)


for file in unit_test_files:
    class_name = utils.get_class_name(file)
    prompt = f"补充完善该单元测试文件，文件名为 {file}，类名为 {class_name}，依赖关系为 {dependencies[class_name]}" 
    for dependency in dependencies[class_name]:
        prompt += f",依赖类为 {dependency}"
        # 读取文件
        dependency_file = "./sdk/src/main" + dependency.replace(".", "/")        
    