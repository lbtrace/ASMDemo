# 使用 ASM 进行 AOP 编程的样例代码
包含以下例子
- 类中添加字段
- 类中删除字段
- 类中添加方法
- 类中删除方法
- 修改类中方法的实现
## 使用方法
1. 在项目的根目录命令行下执行如下命令编译插件
```
./gradlew plugin:assemble
```
2. 执行如下命令上传编译好的插件到 repo 目录下
```
./gradlew plugin:uploadArchives
```
3. 编译 App 主模块
```
./gradlew app:assembleDebug
```
4. 验证功能，在工程的 build 目录下查看字节码信息
```
....../ASM/app/build/intermediates/transforms/ASMTransform/debug/32/com/lbtrace/asm/
```
