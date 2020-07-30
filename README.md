# online-judge
一个评测机基于linux c, 客户端基于java web的在线评测系统
## 功能与模块
整个系统分为web系统和评测机两个部分，web系统负责用户访问评测系统的相关功能以及管理员对系统题目，用户等数据进行管理。该部分数据库使用MySQL，后端开发框架使用springboot，前端主要使用bootstrap完成。
评测机负责编译用户提交的代码，运行用户程序，限定并获取用户程序的资源消耗和结果判定。评测机由C语言开发完成，工作于linux环境，其代码在judgeServer目录中。关于两个部分的通信，将在后续做描述。
数据库定义文件为xx目录中的dbdefine.sql文件。

## 开发环境
```
OS Name:   Linux, Ubuntu 16.04
OS Version:   4.4.0-112-generic
Architecture:   amd64
Java Home:   jdk1.8.0_60/jre
JVM Version:   1.8.0_60-b27
JVM Vendor:   Oracle Corporation
开发工具:   intellij idea
```

## 评测机支持语言
1. __C__
2. __C++__
3. __Java__
4. __Python2.x__
5. __Python3.x__


### 工作环境配置
1. 运行环境要求Linux 64位系统, 内存1G以上, 可用磁盘空间5G以上.
2. 安装运行时环境, gcc, g++, Java, Python2,Python3, 并设置相关环境变量,评测机在编译的时候需要本地环境支持.
3. 安装MySQL数据库并导入数据库定义.
4. 将系统语言设置为英文, 在 __/etc/default/locale__ 中添加 __LANG=en.US__, 防止gcc标准错误中出现非ASCII字符导致web端显示乱码.

### 编译运行评测机
1. 环境准备：编译之前确保主机是linux系统，并安装有make和gcc.
2. 编译程序：下载judgeServer到任意目录，终端进入该目录，执行`make`,如果不出意外，执行之后将在目录中生成一个`main`可执行文件。
3. 配置参数：打开__oj.conf__文件, 默认配置文件如下, 需要指定的是评测机的端口port和测试样例文件所在的跟目录。其中java_policy与debug暂时未用到。
4. 运行程序：确保__oj.conf__与__main__在同一目录，执行`./main`启动评测机
```
#judge server系统配置文件
#请严格按照格式配置, 否则系统会无法读取配置文件

#是否开启调试模式
debug=1
#端口, 默认端口2345
port=2345
#java安全配置文件
java_policy=
#最大输出文件限制字节
outputFileSize=8192
#测试数据目录,路径不能包括空格
testPointDir=./testpoints/
#被禁用的系统调用
restrictedSyscalls=fork,   nanosleep,pause,reboot,   rmdir,rename,unlink,umount,chdir   connect,accept,sendto,recvfrom,sendmsg,recvmsg,shutdown,bind,listen,getsockname,getpeername,socketpair,setsockopt,getsockopt
```

## web server的运行
1. 将项目导入idea
2. 修改`application.properties`文件中的数据账户，评测机的访问地址与端口。
3. 直接在idea中运行项目或者导出jar包运行。
4. 在前面操作都正常的情况下，现在可以浏览器访问了。
5. 访问到的内容是空的，请自行添加题目和用户数据。

## 题目数据结构
题目的数据包括对问题的描述和测试数据两个部分，题目的添加可以直接在网页上操作完成。题目保存的时候，题目的文字描述信息，将保存到数据库，图片将保存到`application.properties`文件指定的路径。
题目的测试数据将以文件的形式保存在`application.properties`文件指定的路径中。题目的测试数据以如下形式存储, 在添加题目的时候系统会自动按词结构保存文件。
```
测试数据根目录:
    题目ID1:
        input:
            1.in
            2.in
            ...
        output:
            1.out
            2.out
            ...
    题目ID2：
        input:
            1.in
            2.in
            ...
        output:
            1.out
            2.out
            ...
```
**请设置评测机的测试数据目录为添加题目是测试数据保存的目录，否则评测机无法访问测试点数据，进而无法评测**

## 评测机与web系统的通信
两者的通信使用C/S模式基于套接字，评测机作为server端，web系统为client端。web系统发起评测任务时，按如下协议进行通信：
```
+--------------------+
|4字节整数 任务id     |
+--------------------+
|4字节整数 题目id     |
+--------------------+
|4字节整数 题目时间限制|
+--------------------+
|4字节整数 题目内存限制|
+--------------------+
|4字节整数 代码长度len |
+--------------------+
|20字节 语言类型      |
+--------------------+
|4字节整数 测试点数量  |
+--------------------+
|len字节的源代码数据   |
+--------------------+
```

评测机给web系统响应结的结果为json，结构如下：
```
{
    "submitID":1,
    "judgeTime":1595864815847,
    "compileRetVal":0,
    "compileResult":"",
    "timeConsume":21,
    "memConsume":35232,
    "finalResult":"Accepted",
    "message":"",
    "judgeDetails":[
        {"testPointID":1, "timeConsume":20, "memConsume":35232, "returnVal":0, "result":"Accepted"},
        {"testPointID":2, "timeConsume":16, "memConsume":35232, "returnVal":0, "result":"Accepted"}
    ]
}
```

## 备注
1. 项目最开始使用的jsp/servlet开发, 前期代码比较凌乱，后面用spring框架重构了一下。
2. 评测机开始为一个单c文件，结构不够清晰。重构将单个文件按不同功能分解到了不同文件，并使用makefile编译。
3. 在原来的基础上重新设计了通信协议，使得结构更加清晰简单，修复了一些问题，并对功能进行增强。


## 效果预览
![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/home.png)
> 图为首页.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/problem-list.png)
> 图为题目列表.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/submit-record.png)
> 图为提交记录.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/contest-overview.png)
> 图为比赛.

## 参考
1. [qdacm](https://qdacm.com/)
2. [hustoj](https://github.com/zhblue/hustoj)
