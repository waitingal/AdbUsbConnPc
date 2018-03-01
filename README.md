 1.PC端运行 adb forward tcp:XXXX tcp:8768 （本地端口转发）
 
 2.PC端使用adb 发送广播 启动或停止 Android Server
（adb shell am broadcast -a com.adb_usc_conn_pc.demo.SYTReceive.StartService/com.adb_usc_conn_pc.demo.SYTReceive.StopService）
    或Android端监听USB拨插广播来启动Android Server

 3.PC端 使用 127.0.0.1:xxxx 连接Android Server 建立Socket连接进行数据通信。
    
