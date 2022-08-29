## **veRTC**

[火山引擎实时音视频 veRTC](https://www.volcengine.com/products/rtc) 可以提供全球互联网范围内高质量、低延时的实时音视频通信能力，通过调用 veRTC API，可以帮助开发者快速构建语音通话、视频通话、互动直播、转推直播等丰富场景功能。

## **veRTC** **场景化 Demo**

场景化 Demo（Solution Demo）是火山引擎实时音视频（veRTC）针对会议、教育、互娱等场景推出的最佳实践场景化解决方案。Demo 提供开源的客户端和服务端源码，并配套说明文档，帮助开发者快速搭建业务场景。

## **场景概述**

语音聊天室是指网络上虚拟的语音聊天房间，用户（房主）通过创建一个房间的方式，进行语音直播，房间设有麦位，麦位玩法丰富，房主可以通过麦位管理，邀请观众上麦、禁言正在连麦嘉宾等。

|<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_93af0ac8afeb46ed85f7bba1696a7057" width="500px" > |<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_08edb3c39d7cba6bf67595096bdca1c4" width="500px" > |<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_a9e807226400243d2ee00713c2b15e97" width="500px" > |<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_655efc17d4aa7e192255c9c7141d0063" width="500px" > |
|:-:|:-:|:-:|:-:|



## Demo 体验

Demo 下载地址参看[下载和体验场景化 Demo](https://www.volcengine.com/docs/6348/75707#%E4%B8%8B%E8%BD%BD%E5%92%8C%E4%BD%93%E9%AA%8C%E5%9C%BA%E6%99%AF%E5%8C%96-demo)。

## 文档资源

场景详细介绍与技术实现详情，请参看[语音聊天室](https://www.volcengine.com/docs/6348/117919)。

## **联系我们**

如果您遇到任何问题，填写[VolcengineRTC 反馈问卷](https://wenjuan.feishu.cn/m?t=sQrk90adbLwi-6ivu)，我们会竭力为您提供帮助

## 更多场景

- [一起看抖音](https://github.com/volcengine/RTC_WatchTogether_Demo)

一起看，即将线下跟朋友一起刷短视频、观影、看剧的场景搬到线上。通过创建一个线上实时互动的房间，在房间内加载视频内容，房间内的所有用户便可以享受同步观看视频的陪伴式体验。

|<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_e9137879ef3c121e0cadaefbeb9e70ee" width="500px" > |<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_b91667eec4a879d9f308eec864d0223e" width="500px" > |
|:-:|:-:|

- [语音沙龙](https://github.com/volcengine/RTC_Voice_Demo)

语音沙龙是指网络上虚拟的语音聊天房间，用户（房主）通过创建一个房间的方式，进行语音直播，但房间内无麦位的概念，上麦人数无限制。且语音沙龙中创建房间的人离开后，房主角色会移交给其他人。语音沙龙更适合社交类场景，弱化用户之间的差别。

- [视频互动](https://github.com/volcengine/RTC_VideoInteract_Demo)
	
视频互动是泛娱乐社交领域的一种常见场景。主播创建自己的直播间后，可以发起与其他主播进行连麦 PK，实现多个直播间之间的互动；同时主播也可以与直播间内的观众连麦，开启视频聊天室，与观众实时音视频互动。互动时，支持添加美颜特效，播放背景音乐。该方案中观众直接拉 RTC 流，观众看播延时更低，上下麦体验更平滑。


|<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_2a3e6ede58c5705dd04435663babdc76" height="400px" >|<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_b4201ce3943e5594ad9760fc2e2dad76" height="400px" >|
|:-:|:-:|

- [互动直播](https://github.com/volcengine/RTC_CDNLive_Demo)
	
互动直播与视频互动在场景功能上相同，均支持主播之间的跨房连麦以及主播和观众连麦，区别主要是实现方式不同，观众体验不同；互动直播方案中的观众从CDN拉流，视频互动方案中的观众直接拉 RTC 流，视频互动方案中观众看播延时更低，上下麦体验更平滑。

- [在线KTV](https://github.com/volcengine/RTC_KTV_Demo)
	

KTV 房间里，用户点歌，跟随音乐演唱歌曲并与其他用户进行音视频实时互动。


|<img src="https://lf6-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_d06709b4eaa5983d7a17698d6fbc8f34" height="400px" >|<img src="https://lf3-volc-editor.volccdn.com/obj/volcfe/sop-public/upload_e2061f1d984eaae663ffa5e3c4426986" height="400px" >|
|:-:|:-:|

- [在线课堂](https://github.com/volcengine/RTC_Meeting_Demo)
	

在线课堂是指一名老师进行在线讲课，成千上万个学生在线听课的教学场景，支持教师与学生、学生与学生之间的多种实时互动，支持老师开启集体发言、课堂录制、课堂监督、学生上台发言等功能。

- [视频会议](https://github.com/volcengine/RTC_Meeting_Demo)
	

视频会议是指一对一和多人纯语音或音视频通话。用户可以随时随地通过网络加入会议，进行实时互动。
