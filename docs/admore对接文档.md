# AdMore 使用说明-Android

## 1.简介

AdMore是一款可以实现优量汇、快手、穿山甲、tanx、mercury进行header bidding竞价逻辑的广告bidding插件，使媒体接入bidding更加简单高效。


**AdMore插件基于[Advance聚合SDK](http://www.bayescom.com/docsify/docs/#/advance/sdk/android/index)
运行，使用AdMore之前请先确保已正确[引入Advance](http://www.bayescom.com/docsify/docs/#/advance/sdk/android/start)**

AdMore广告位支持情况

|支持广告类型|  开屏 |  信息流 | 激励视频 |
|:------------- |:---------------|  :---------------|  :---------------|   
| AdMore |✅ |✅ |✅ | 

## 2.SDK引入

gradle引入：

```
    def advanceVersion = "4.1.0"
    //必须，Advance聚合SDK 引入
    implementation "io.github.flayone:advance-core:${advanceVersion}"
    //必须，admore插件 引入
    implementation "io.github.flayone:admore:${advanceVersion}_4"
    
    ……………… 其余ADN adapter引入，具体请参考Advance SDK引入部分
```

### 初始化及准备工作

AdMore无需初始化和额外准备内容，因为需要集成AdvanceSDK，请确保按照[Advance文档](http://www.bayescom.com/docsify/docs/#/advance/sdk/android/index)
正确完成**Advance初始化**及**隐私选项控制等**相关配置内容。 具体可参考demo中配置

## 3.加载广告

**广告加载主要分为三步**： <br/>1.初始化广告类，一般是通过`new AdMoreXXX(context,"广告位id",adEventListener)`初始化广告类。
<br/>2.设置广告配置信息，通过`AdMoreXXX`来获取对应广告位置的`AdvanceXXX`实例，进行个性化配置调整。
<br/>3.拉取和展示广告，`AdMoreXXX`所有位置请求广告方法固定，分别是:

- `loadAndShow`代表加载广告并展示，广告成功返回后会自动执行广告展示方法。

- `loadOnly`仅加载广告，广告成功返回后，用户可以自由选择展示时机（注意：广告有展示有效期限制，收到广告后应尽早展示）。

- `show` 展示广告方法，前提是已返回了广告，展示才会生效。

特别提醒：同一时刻加载得广告，广告位id必须不可重复！（主要是信息流位置）

### 3.1 开屏广告（AdMoreSplash）

加载、展示广告核心方法，详细请参考demo中代码：

```java
        //初始化广告处理类
        AdMoreSplash splash=new AdMoreSplash(this,Constants.TestIds.adMoreSplashAdspotId,adContainer,adMoreSplashListener);
                //注意！！：如果开屏页是fragment或者dialog盖在主页上的实现，这里需要置为true。不设置时默认值为false，代表开屏和首页为两个不同的activity
//        splash.getAdvanceSplash().setShowInSingleActivity(true);
                //请求并展示开屏广告。
                splash.loadAndShow();
```

* 开屏事件回调 **AdMoreSplashListener**

| 方法名|    方法介绍|  
|:------------- |:---------------|  
|onSuccess()    | 成功获取广告信息回调
|onShow()    | 广告成功展示回调
|onFailed(AMError amError)  |  广告加载失败回调
|onClick()|  广告被点击回调
|onAdSkip()|  用户跳过广告回调
|onAdTimeOver()|  倒计时结束回调
|jumpToMain()| 跳转到首页方法，直接在此回调方法中进行跳转首页行为

### 3.2信息流广告(AdMoreNativeExpress)

**特别注意：广告位id不可重复使用，否则可能影响广告正常展示，特别是用于信息流位置时，一定要用不同的广告位id来加载不同position位置的广告。**

请求广告核心方法，详细请参考demo中代码：

```java
AdMoreNativeExpress adMoreNativeExpress=new AdMoreNativeExpress(this,Constants.TestIds.adMoreNativeAdspotId,AdMoreNativeExpressListener);
        adMoreNativeExpress.setAdContainer(container);//设置广告承载布局
        adMoreNativeExpress.loadAndShow();
```

* 事件回调 **AdMoreNativeExpressListener**

| 方法名|    方法介绍|  
|:------------- |:---------------|  
|onSuccess()    | 成功获取广告信息回调
|onShow()    | 广告成功展示回调
|onFailed(AMError amError)  |  广告加载失败回调
|onClick()|  广告被点击回调
|onRenderSuccess()|    广告渲染成功
|onRenderFailed()|    广告渲染失败
|onClose()|    用户关闭广告回调

### 3.3激励视频(AdMoreReward)

加载广告核心方法，详细请参考demo中代码：

```java
//初始化广告
AdMoreReward reward=new AdMoreReward(this,Constants.TestIds.adMoreRewardAdspotId,AdMoreRewardListener);
//必须：请求策略并请求广告
        reward.loadAndShow();
```

* 事件回调 **AdMoreRewardListener**

| 方法名|    方法介绍|  
|:------------- |:---------------|  
|onSuccess()    | 成功获取广告信息回调
|onShow()    | 广告成功展示回调
|onFailed(AMError amError)  |  广告加载失败回调
|onClick()|  广告被点击回调
|onVideoCached()    |视频缓冲成功回调
|onVideoComplete() | 视频播放完成回调
|onVideoSkip() | 视频播放跳过回调
|onAdClicked()|    广告被点击回调
|onAdClose()|    用户关闭广告回调
|onAdReward()|    激励发放回调

* 基础激励视频实现类 **AdvanceRewardVideoItem**（已废弃）

|方法名|    方法介绍|  
|:------------- |:---------------|  
|String getSdkId() | 获取当前加载的SDK渠道，sdkID对应 **1.4 SDK渠道平台信息**中的SDK ID
|showAd() | 展示当前激励视频广告

## 4.异常处理

### 4.1 debug日志排查

如果集成完成，需要调试sdk请务必在**SDK初始化配置时打开debug模式**, 或者直接在需要debug的地方调用：

	AdvanceConfig.getInstance().setDebug(true)

然后在` Android Studio`  中的 ` Logcat` 下搜索 **advance** 关键字，即可查看SDK中所有的打印日志。

<font size=2>注： 部分手机需要在开发者选项打开日志打印开关，部分华为手机需要系统设置中开启日志打印，如果无法打印请先确认手机系统设置是否正常。</font>

### 4.2 SDK错误码

|错误码      | 错误详情 | 排查方向
|:------------- |:---------------|  :---------------|  
|9901| 数据为空 |广告返回的数据为空，一般为渠道广告无填充。
|9902 | 加载某个SDK渠道时发生异常| 查看logcat下日志输出，寻找具体原因
|9903 |展示某个SDK渠道时发生异常| 查看logcat下日志输出，寻找具体原因
|9904|渲染某个SDK渠道时发生异常| 查看logcat下日志输出，寻找具体原因
|9905|当前策略中无有效的SDK渠道，或渠道信息为空 | 检查聚合后台中对应广告位的渠道设置，有可能是配错渠道或者没有配置
|9906| 策略调度时发生异常 | 查看logcat下日志输出，及时反馈我们帮助排查
|9907|未获取到策略 |首先检查网络是否正常，若网络正常请检查聚合后台广告位状态是否正常，以及广告位配置是否正确，若全部配置正常，依然报此问题，请联系我们
|9908|某个SDK渠道启动异常| 查看logcat下日志输出，及时反馈我们帮助排查
|9909|穿山甲渠道SDK超时且不再加载优先级更低的渠道 | 加载超时，检查网络是否正常。仅开屏中会出现，代表开屏广告位中配置了setCsjTimeOutQuit(true)，低频可忽略
|9910 | 穿山甲渠道SDK加载超时 | 加载超时，检查网络是否正常
|9911 | 百度SDK加载失败 | 查看logcat下日志输出，寻找具体原因
|9912 | 快手SDK加载失败，广告位id类型转换异常 | 一般是广告位转换出错，检查后台配置的快手渠道下广告位id是否为纯数字，非纯数字会引起该异常。
|9913|无activity异常 | 一般是广告所在页面生命周期不正常，为页面消失状态，此时无法执行广告的加载逻辑
|9914| 快手SDK初始化异常| 检查快手id配置是否正常
|9915 | 渲染失败 | 一般是网络较差导致，检查网络无误后多试几次
|9916 | tanx执行失败 | 具体失败原因请查看对应时间点的logcat输出日志，了解更多
|9920 |达到策略并行层设置得超时时间，且无广告返回 | 检查超时时间设置是否过短，和广告位id配置是否正确。
|9921 | 添加广告布局失败 |检查传递得广告父布局是否正常
|99_XXX | 某个SDK渠道返回的错误，其中XXX为该SDK渠道返回的具体错误码 | 翻阅各个平台下的错误码说明查找具体原因： <br/>穿山甲：[错误码说明，点击跳转](https://www.pangle.cn/union/media/union/download/detail?id=4&docId=5de8d9b925b16b00113af0ed&osType=android)<br/>广点通：[错误码说明，点击跳转](https://developers.adnet.qq.com/doc/android/union/union_debug#sdk%20%E9%94%99%E8%AF%AF%E7%A0%81)<br/>百度：[接入文档，点击跳转；错误码详见17.2](https://union.baidu.com/miniappblog/2020/06/16/AndroidSDK/) <br/>快手：无在线文档，[点击下载离线文档，查看详细错误信息](https://static.yximgs.com/udata/pkg/KS-Android-KSAdSDk/kssdk-ad-3.3.5.3-publishRelease-6d3b66a-167.zip)

### 4.3 其他异常处理

Q: 项目构建时出现 “unexpected element < queries> found in < manifest>”异常?

A:
queries是Android11新引入的manifest基础成分，低版本的gradle无法识别，[点此查看详细说明](https://stackoverflow.com/questions/62969917/how-do-i-fix-unexpected-element-queries-found-in-manifest)。Google发布了以下插件包更新来支持此特性，建议升级Android
Gradle Plugin 到以下版本：

- 3.3.3
- 3.4.3
- 3.5.4
- 3.6.4
- 4.0.1

---

Q: 请求不到广告怎么办？

A: 首先检查广告位的ID配置是否正确，是否按照文档要求请求广告，然后根据SDK返回的异常code和msg，翻阅上面 ** SDK错误码**来分析错误原因。

---

## 5 测试验收

一般广告可以正常展示、点击、跳转即代表集成成功。

开发对接期间广告位状态为测试状态，发版放量前请告知我们，以便安排正式广告填充。
