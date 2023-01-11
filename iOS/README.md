语音聊天室是火山引擎实时音视频提供的一个开源示例项目。本文介绍如何快速跑通该示例项目，体验语音聊天室效果。

## 应用使用说明

使用该工程文件构建应用后，即可使用构建的应用体验语音聊天室。
你和你的同事必须加入同一个房间，才能一同体验语音聊天室。

## 前置条件

- [Xcode](https://developer.apple.com/download/all/?q=Xcode) 14.0+
	

- iOS 12.0+ 真机
	

- 有效的 [AppleID](http://appleid.apple.com/)
	

- 有效的 [火山引擎开发者账号](https://console.volcengine.com/auth/login)
	

- [CocoaPods](https://guides.cocoapods.org/using/getting-started.html#getting-started) 1.10.0+
	

## 操作步骤

### **步骤 1：获取 AppID 和 AppKey**

在火山引擎控制台->[应用管理](https://console.volcengine.com/rtc/listRTC)页面创建应用或使用已创建应用获取 **AppID** 和 **AppAppKey**

### **步骤 2：获取 AccessKeyID 和 SecretAccessKey**

在火山引擎控制台-> [密钥管理](https://console.volcengine.com/iam/keymanage/)页面获取 **AccessKeyID** 和 **SecretAccessKey**

### 步骤 3：构建工程

1. 打开终端窗口，进入 `RTC_VoiceChat_Demo/iOS/veRTC_Demo_iOS` 根目录<br> 
	<img src="https://portal.volccdn.com/obj/volcfe/cloud-universal-doc/upload_ab6885a787371079c8bd5a4e554de58a.png" width="500px" >
2. 执行 `pod install` 命令构建工程<br>
    <img src="https://portal.volccdn.com/obj/volcfe/cloud-universal-doc/upload_826318a7b078054b6161b878585f97ad.png" width="500px" >	
3. 进入 `RTC_VoiceChat_Demo/iOS/veRTC_Demo_iOS` 根目录，使用 Xcode 打开 `veRTC_Demo.xcworkspace` <br>
	<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_b479ae8a464cf80f054ca9f9ac6bc2bc" width="500px" >
4. 在 Xcode 中打开 `Pods/Development Pods/Core/BuildConfig.h` 文件<br>
    <img src="https://portal.volccdn.com/obj/volcfe/cloud-universal-doc/upload_0548899b170c606d3fe53adb7c670d4a.jpeg" width="500px" >	
5. 填写 **HeadUrl**<br>
    当前你可以使用 **https://common.rtc.volcvideo.com/rtc_demo_special** 作为测试服务器域名，仅提供跑通测试服务，无法保障正式需求。<br>
    <img src="https://portal.volccdn.com/obj/volcfe/cloud-universal-doc/upload_bfd188ba8820fd30621ba0b3d4ae57b2.jpeg" width="500px" >

6. **填写 APPID、APPKey、AccessKeyID 和 SecretAccessKey** <br>
	使用在火山引擎控制台获取的 **APPID、APPKey、AccessKeyID 和 SecretAccessKey** 填写到 `BuildConfig.h`文件的对应位置。 <br>
    <img src="https://portal.volccdn.com/obj/volcfe/cloud-universal-doc/upload_560b76c0194a7c3b056964a3fefb69d4.png" width="500px" >

### **步骤 4：配置开发者证书**

1. 将手机连接到电脑，在 `iOS Device` 选项中勾选您的 iOS 设备 <br>
	<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_c90035ea219af29f94677c0cd3fc0c12" width="500px" >
2. 登录 Apple ID。<br>
	2.1 选择 Xcode 页面左上角 **Xcode** > **Preferences**，或通过快捷键 **Command** + **,**  打开 Preferences。<br>
	2.2 选择 **Accounts**，点击左下部 **+**，选择 Apple ID 进行账号登录。<br>
		<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_fe1ce72f17db7eeae67a0952fb31c2df" width="500px" >

3. 配置开发者证书。 <br>
	3.1 单击 Xcode 左侧导航栏中的 `VeRTC_Demo` 项目，单击 `TARGETS` 下的 `VeRTC_Demo` 项目，选择 **Signing & Capabilities** > **Automatically manage signing** 自动生成证书 <br>
		<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_008590ac8d8df73204cb963ce0fdf094" width="500px" >

	3.2 在 **Team** 中选择 Personal Team。<br>
		<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_c09156899e72fcb2e947852e97398172" width="500px" >

	3.3 **修改 Bundle Identifier。** <br>
	默认的 `vertc.veRTCDemo.ios` 已被注册， 将其修改为其他 Bundle ID，格式为 `vertc.xxx`。 <br>
		<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_d80337370b95efceaa7188d583dc3b59" width="500px" >

### **步骤 5：编译运行**

选择 **Product** > **Run**， 开始编译。编译成功后你的 iOS 设备上会出现新应用。若为免费苹果账号，需先在`设置->通用-> VPN与设备管理 -> 描述文件与设备管理`中信任开发者 APP。

<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_9d1fad791b3616428a3011837f148581" width="500px" >

## 运行开始界面

<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_e083a5b625c5cacb661d606d50f33dc0" width="200px" >

