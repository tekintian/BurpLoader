# burploader burpsuite渗透测试加载工具

声明: 

**本项目来源网络,仅做学习测试研究使用! This project is only for learning and communication. For commercial use, please buy genuine software! **



支持 JDK11+ 或者 JDK20+

最新版本的burpsuite 2023版本默认使用的是JDK 20.0.2版本



## build

1. 先mvn clean 清理项目;
2. 运行 run cn.tekin.burploaderkeygen.KeygenForm.main() 看到主窗口后表明项目成功编译, 
3. 接着执行 mvn package 即可打包成功.jar文件, 
如果出现 "程序包 jdk.internal.org.objectweb.asm 不可见 " 异常,则再次执行package 即可

注意 打包后会有2个jar文件需要使用 xxx-jar-with-dependencies.jar 这个带有清单的jar才能运行


## vm启动参数
vmoptions 
--add-opens 这个参数是jdk大于17时必须增加的,否则无法加载jar中的Loader
~~~vmoptions
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.Opcodes=ALL-UNNAMED
-javaagent:BurpLoaderKeygen.jar
-noverify
~~~

