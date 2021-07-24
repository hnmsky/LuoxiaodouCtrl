# luoxiaodou_ctrl 萝小逗机器人控制App

## 简介

这是手机控制App， 机器人里的App请参考luoxiaodou app。

这个App是参考了机器人里面的测试App。



## 配置

默认会连接HiveMQ broker。 

如果用自己的broker，需要修改 MainFragment.kt 中的host 和topic。

```kotlin
    val host = "broker.hivemq.com"
    val port = 1883
    val topic = "luoxiaodou_1"
```



## 控制

目前只有left， right， forward， backward， reset， off几个命令有效。
