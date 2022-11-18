语音聊天室是火山引擎实时音视频提供的一个开源示例项目。本文介绍如何快速跑通该示例项目，体验语音聊天室效果。

## 应用使用说明

使用该工程文件构建应用后，即可使用构建的应用体验语音聊天室。
你和你的同事必须加入同一个房间，才能一同体验语音聊天室。
如果你已经安装过 火山引擎场景化 Demo 安装包，示例项目编译运行前请先卸载原有安装包，否则会提示安装失败。

## 前置条件

- Android Studio （推荐版本 [Chipmunk](https://developer.android.com/studio/releases)）
	

- [Gradle](https://gradle.org/releases/) （版本： gradle-7.4.2-all）
	

- Android 4.4+ 真机
	

- 有效的 [火山引擎开发者账号](https://console.volcengine.com/auth/login)
	

### 操作步骤

### **步骤 1：获取 AppID 和 AppKey**

在火山引擎控制台->[应用管理](https://console.volcengine.com/rtc/listRTC)页面创建应用或使用已创建应用获取 AppID 和 AppAppKey

### **步骤 2：获取 AccessKeyID 和 SecretAccessKey**

在火山引擎控制台-> [密钥管理](https://console.volcengine.com/iam/keymanage/)页面获取 **AccessKeyID 和 SecretAccessKey**

### 步骤 3：构建工程

1. 使用 Android Studio 打开该项目的`RTC_VoiceChat_Demo-main/Android/veRTC_Demo_Android` 文件夹
	

2. 填写 **LoginUrl。** 
	

进入 `scene-core/gradle.properties` 文件，填写 **LoginUrl**。

当前你可以使用 **`https://common.rtc.volcvideo.com/rtc_demo_special/login`** 作为测试服务器域名，仅提供跑通测试服务，无法保障正式需求。

<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_5267a0ed9024d308c687fb5e70c0c0a5" width="500px" >

3. **填写 APPID、APPKey、AccessKeyID、SecretAccessKey**
	

进入 `component/joinrtsparams` 目录下 `gradle.properties`文件，使用从火山引擎控制台获取的 **APPID、APPKey、AccessKeyID、SecretAccessKey 填写到该文件相应位置。** 
<br>

> ACCOUNT\_ID 和 VOD\_SPACE 为录制相关功能配置参数，此 Demo 无录制功能，填 `1` 即可。

<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_f4267cf5d85af43dafb3fcbf28eb360a" width="500px" >

### 步骤 4：编译运行

1. 将手机连接到电脑，并在开发者选项中打开调试功能。连接成功后，设备名称会出现在界面右上方。
	
	<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_3485f63885162b12708811a6c4f0cf4d" width="500px" >
	

<br>

2. 选择**Run** -> **Run**，开始编译。编译成功后你的 Android 设备上会出现新应用。部分手机会出现二次确认，请选择确认安装。
	

<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_b882b65e75cc7406c1d8f2e2beebbf0a" width="500px" >

运行开始界面如下：
<br>

<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_37abcf3600012d35d7b2e2b144fd35b0" width="200px" >
