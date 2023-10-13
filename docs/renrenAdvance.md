人人视频使用Advance SDK文档说明
=====================

## 1.SDK更新记录

| SDK版本    | 更新内容                | 更新时间       |
|:---------|:--------------------|:-----------|
|v4.3.0_rr2 |1.激励视频增加isValid()方法，来判断视频是否有效 <br/>2.新增聚合自渲染信息广告位AdvanceRenderFeed，已支持聚合SDK：穿山甲、优量汇、倍业广告SDK |2023-09-20
| v4.3.0_rr1 |1.兼容使用包名定制版穿山甲SDK <br/>2.开屏分离改造 <br/>3.激励视频分离改造 | 2023-09-06 |


## 2.SDK引入

gradle引入代码：

```

    def advanceVersion = "4.3.0_rr2"
    //必须，Advance聚合SDK 引入。如果gradle引入异常，请参考根目录gradle配置（确保maven依赖在最前面）然后重试
    implementation "io.github.bayescom:advance-core:${advanceVersion}"
    //必须，mercury SDK 引入 包含了广告aar以及渠道处理逻辑
    implementation "io.github.bayescom:advance-mry:${advanceVersion}_402"
     //穿山甲特殊版本，不包含穿山甲aar
    implementation "io.github.bayescom:advance-csj-oppo:${advanceVersion}_5307_core"
    //(可选)广点通、百度、快手SDK 引入，包含了广告aar以及渠道处理逻辑
    implementation "io.github.bayescom:advance-gdt:${advanceVersion}_4541"
    implementation "io.github.bayescom:advance-bd:${advanceVersion}_9313"
    implementation "io.github.bayescom:advance-ks:${advanceVersion}_33511"
   
    
//    如果存在优量汇等aar包冲突报错，不想删除包的话可以参考使用以下_core无广告aar引用依赖配置。
//    为避免版本不同导致执行异常，推荐使用上方正常带广告aar的引入方式。
//    implementation "io.github.bayescom:advance-gdt:${advanceVersion}_4541_core"
//    implementation "io.github.bayescom:advance-bd:${advanceVersion}_9313_core"
//    implementation "io.github.bayescom:advance-ks:${advanceVersion}_33511_core"
```

## 3.SDK新改动内容说明


### 3.1 开屏广告位

开屏分离调用指引

- 新增接口api说明：

| 方法                                   | 含义                                      | 是否必须 |
|:-------------------------------------|:----------------------------------------|:-----|
| AdvanceSplash(String adspotId)       | 广告初始化方法， adspotId为聚合广告位id               | 必须   |
| setLoadAsync(boolean isLoadAsync)    | 是否在异步线程中执行广告初始化方法，默认false，如果需要请手动置为true | 可选   |
| setCustomSkipView(TextView skipView) | 设置自定义得跳过view，仅对倍业广告SDK生效                | 可选   |
| show(ViewGroup adContainer)          | 展示广告，adContainer为广告得承载父布局               | 必须   |

- 调用广告load示例

```java
         //sdk 初始化方法
         AdvanceSplash advanceSplash = new AdvanceSplash(adid);
         //设置广告事件监听
         advanceSplash.setAdListener(new AdvanceSplashListener() {
                    @Override
                    public void onAdLoaded() {
//        建议标记广告成功，后续调用show需要广告成功状态
                    }

                    @Override
                    public void jumpToMain() {
//                      广告失败、广告跳过、倒计时结束均会回调执行到此时间， 建议在此执行跳转首页操作。
                    }

                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdFailed(AdvanceError advanceError) {
// 建议标记广告失败，后续如果是失败状态，不需要调用show
                    }

                    @Override
                    public void onSdkSelected(String id) {

                    }
                });
//      可选：   异步加载
                advanceSplash.setLoadAsync(true);
//       必须：  仅加载广告
                advanceSplash.loadOnly();
```

- 调用广告show示例


```java
//可选：设置自定义跳过view
  advanceSplash.setCustomSkipView(skipView);
//可选：设置底部logo布局
  advanceSplash.setLogoLayout(R.layout.splash_logo_layout, getResources().getDimensionPixelSize(R.dimen.logo_layout_height));
// 必须：展示开屏广告
  advanceSplash.show(adContainer);
```

### 3.2 激励视频广告位


- 新增接口api说明：

| 方法                                  | 含义                        | 是否必须 |
|:------------------------------------|:--------------------------|:-----|
| AdvanceRewardVideo(String adspotId) | 广告初始化方法，不含activity信息， adspotId为聚合广告位id | 必须   |
| boolean isValid()|广告展示前调用，获取广告是否有效可展示 | 建议使用
| show(Activity activity)             | 展示广告，和不含activity信息得初始化方法配套使用                 |  必须    |

- 调用广告load示例

```java
//初始化广告
AdvanceRewardVideo advanceRewardVideo = new AdvanceRewardVideo(adid);
//监听广告事件
advanceRewardVideo.setAdListener(this);
//load广告
advanceRewardVideo.loadAd();
```

- 调用广告show示例

```java
//广告展现，注意要在广告成功以后调用展现
advanceRewardVideo.show(this);
```



### 3.3 自渲染信息流广告位

新增广告位，支持穿山甲、优量汇、倍业广告SDK的聚合，具体使用可以参考demo中示例代码。

####  广告位相关api方法

| 方法                                  | 含义                        | 是否必须 |
|:------------------------------------|:--------------------------|:-----|
| AdvanceRenderFeed(Context context, String adspotid) | 广告位初始化方法，**强烈建议Context传入Activity信息**，adspotId为聚合广告位id|必须
|setLoadListener(AdvanceRFLoadListener loadListener) |监听广告返回结果 |必须
|setRfMaterialProvider(AdvanceRFMaterialProvider provider)| 提供页面信息给广告SDK，用来绑定view及监听广告特殊事件，如下载和视频播放情况 ，一定要在调用广告展示前设置好必要信息，具体请参考demo中代码|必须
|setRenderEventListener(AdvanceRFEventListener renderEventListener)| 设置SDK渲染事件回调，主要是广告曝光、点击、关闭 等行为回调 |建议
|setCsjImgSize(int w, int h) | 设置期望图片大小，单位px，主要是设置给穿山甲使用，不设置将使用默认值 640*320 |可选
|loadStrategy()| |必须


####  调用示例说明

广告请求方法：

```

    private void loadAD() {

//测试广告位id，实际上线请使用自己的聚合广告位id
        String csjID = "10003120";
        String ylhID = "10003121";
        String mryID = "10003122";

        //广告初始化，传入聚合广告位id
        advanceRenderFeed = new AdvanceRenderFeed(this, ylhID);
        //设置期望图片大小，单位px，主要是设置给穿山甲使用，不设置将使用默认值 640*320
        advanceRenderFeed.setCsjImgSize(1080, 720);
        //设置广告请求回调
        advanceRenderFeed.setLoadListener(new AdvanceRFLoadListener() {
            @Override
            public void onADLoaded(AdvanceRFADData adData) {
                logAndToast(TAG + "onADLoaded  ");
                bindViewShow(adData);
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(TAG + "onAdFailed, advanceError = " + advanceError);
            }
        });
        //发起请求
        advanceRenderFeed.loadStrategy();
    }

    //绑定view信息给广告，并开始展示广告
    private void bindViewShow(AdvanceRFADData adData) {
        advRFRootView.setVisibility(View.VISIBLE);
        //进行广告图片、文字 及回调处理相关逻辑
        bindAdInf(adData);

        //必须，开始展现广告，一定要在 bindAdInf 之后调用！
        advanceRenderFeed.show();
    }

```

####  广告展示处理涉及类及方法说明


- **AdvanceRFADData** 广告返回信息集合，包含所有渲染需要得广告内容返回。

| 方法                                  | 含义                        |
|:------------------------------------|:--------------------------|
|String getTitle() |获取广告标题文字内容
|String getDesc() |获取广告描述文字
|String getIconUrl()|获取小图标链接地址，可能为空
|String getSourceText() |获取广告来源说明，仅穿山甲会返回此信息，其他默认为空
|String getVideoImageUrl()|获取视频定帧图，穿山甲、倍业SDK会返回此信息，其他默认为空
|List<String> getImgList()|获取图片链接地址列表，当列表中为多个时，代表为多图广告
|boolean isDownloadAD | 是否为app下载类广告，下载类广告注意处理六要素展示相关内容
|int getECPM() |获取广告出价，未获取到价格的话，返回结果为0
|AdvanceRFDownloadElement getDownloadElement() | 获取下载六要素信息
|AdvanceSdkSupplier getSdkSupplier | 获取当前执行的SDK渠道信息

- **AdvanceRFMaterialProvider** 素材渲染管理器，开发者接入端，用来传递的必要信息，比如素材view、回调监听等信息，用来和渲染事件进行联动。具体参数及含义请参考下方说明：

```
public class AdvanceRFMaterialProvider{
    //根布局view，所有自渲染布局必需要被根布局包裹，必需
    public AdvRFRootView rootView;
    //视频view，必需
    public AdvRFVideoView videoView;
    //广告logo标识，必需
    public AdvRFLogoView logoView;
    //关闭按钮view，必需
    public View disLikeView;
    //点击view，必需
    public ArrayList<View> clickViews = new ArrayList<>();
    //创意按钮view，穿山甲、优量汇会用到，可选
    public ArrayList<View> creativeViews = new ArrayList<>();
    //下载监听器，仅穿山甲、优量汇生效，可选
    public AdvanceRFDownloadListener downloadListener;
    //视频播放监听器，可选
    public AdvanceRFVideoEventListener videoEventListener;
    //视频设置选项，对优量汇、倍业广告SDK生效，可选
    public AdvanceRFVideoOption videoOption;
 }
```

- **AdvanceRFDownloadElement** 下载六要素，包含下载目标APP相关信息获取方法，注意处理时部分字段可能为空


| 方法                                  | 含义                        |
|:------------------------------------|:--------------------------|
|String getAppName() |获取应用名称
|getAppVersion()| 获取应用版本号
|getAppDeveloper() | 获取开发者公司名称
|String getPrivacyUrl() |获取隐私协议
|String getPermissionUrl() | 获取权限列表网页地址，可通过webView加载此链接，和getPermissionList方法含义相同，优先取此内容
|getPermissionList(BYAbsCallBack<ArrayList<AdvDownloadPermissionModel>> callBack) | 获取权限列表网页地址，通过列表自渲染展示结果，和getPermissionUrl含义相同，当getPermissionUrl无法获取到信息时使用此信息。注意在异步回调里处理好点击跳转逻辑
|String getFunctionDescUrl() | 获取产品功能说明网页地址 ，可通过webView加载此链接，可能为空，穿山甲暂不支持返回该字段。和getFunctionDescText含义相同，优先取此内容
|String getFunctionDescText()|获取产品功能说明文字，可能为空，穿山甲、优量汇暂不支持返回该字段。和getFunctionDescUrl含义相同，当getFunctionDescUrl方法返回空内容时，使用此字段获取说明信息。
| long getPkgSize() |获取目标APP文件大小，单位byte ，可能为空

- 完整展示广告代码示例，具体实现细节请参考demo中代码：

```

    //展示广告信息
    private void bindAdInf(AdvanceRFADData adData) {
        //建议，监听广告渲染事件回调
        advanceRenderFeed.setRenderEventListener(new AdvanceRFEventListener() {
            @Override
            public void onAdShow(AdvanceRFADData adData) {
                logAndToast(TAG + "onAdShow");

            }

            @Override
            public void onAdClicked(AdvanceRFADData adData) {
                logAndToast(TAG + "onAdClicked");
            }

            @Override
            public void onAdClose(AdvanceRFADData adData) {
                logAndToast(TAG + "onAdClose");

            }

            @Override
            public void onAdErr(AdvanceRFADData adData, AdvanceError advanceError) {
                logAndToast(TAG + "onAdErr" + " ,advanceError = " + advanceError);

            }
        });

        AdvanceRFMaterialProvider materialProvider = new AdvanceRFMaterialProvider();
        //必须，绑定视图给SDK渲染使用
        materialProvider.rootView = advRFRootView;
        materialProvider.videoView = advRFVideoView;
        materialProvider.logoView = advRFLogoView;
        //必须，添加可响应点击事件的view
        materialProvider.clickViews.add(advRFVideoView);
        materialProvider.clickViews.add(mDescription);
        materialProvider.clickViews.add(mCreativeButton);
        materialProvider.clickViews.add(mImagePoster);
        materialProvider.clickViews.add(mTitle);
        materialProvider.clickViews.add(mIcon);
        //必须，关闭按钮
        materialProvider.disLikeView = mDislike;
        //可选，设置下载监听，仅穿山甲支持
        materialProvider.downloadListener = new AdvanceRFDownloadListener() {
            @Override
            public void onIdle(AdvanceRFADData data) {

            }

            @Override
            public void onDownloadStatusUpdate(AdvanceRFADData data, AdvanceRFDownloadInf downloadInf) {
                int status = downloadInf.downloadStatus;
                String showText = "";
                switch (status) {
                    case AdvanceRFConstant.AD_DOWNLOAD_STATUS_PAUSED:
                    case AdvanceRFConstant.AD_DOWNLOAD_STATUS_ACTIVE:
                        showText = downloadInf.getDownloadPercent() + "%";
                        break;
                    case AdvanceRFConstant.AD_DOWNLOAD_STATUS_FINISHED:
                        showText = "点击安装";
                        break;
                    case AdvanceRFConstant.AD_DOWNLOAD_STATUS_FAILED:
                        showText = "重新下载";
                        break;
                }
                mCreativeButton.setText(showText);
            }

            @Override
            public void onInstalled(AdvanceRFADData data, String appName) {
                mCreativeButton.setText("点击打开");

            }
        };

        if (adData.isVideo()) {
            //可选，设置视频播放选项，仅对优量汇、mercury生效
            AdvanceRFVideoOption videoOption = new AdvanceRFVideoOption();
            videoOption.isMute = true;
            videoOption.autoPlayNetStatus = AdvanceRFConstant.VIDEO_AUTO_PLAY_ALWAYS;
            materialProvider.videoOption = videoOption;

            //可选，设置视频播放监听，全部生效
            materialProvider.videoEventListener = new AdvanceRFVideoEventListener() {
                @Override
                public void onReady(AdvanceRFADData data) {
                    logAndToast(TAG + "onReady");


                }

                @Override
                public void onPlayStart(AdvanceRFADData data) {
                    logAndToast(TAG + "onPlayStart");

                }

                @Override
                public void onPlaying(AdvanceRFADData data, long current, long duration) {
                    logAndToast(TAG + "onPlaying");

                }

                @Override
                public void onPause(AdvanceRFADData data) {
                    logAndToast(TAG + "onPause");

                }

                @Override
                public void onResume(AdvanceRFADData data) {
                    logAndToast(TAG + "onResume");

                }

                @Override
                public void onComplete(AdvanceRFADData data) {
                    logAndToast(TAG + "onComplete");

                }

                @Override
                public void onError(AdvanceRFADData data, AdvanceError error) {
                    logAndToast(TAG + "onError ,err = " + error);

                }
            };
        }
        //必须，传递必要信息给SDK用于渲染
        advanceRenderFeed.setRfMaterialProvider(materialProvider);

// --------------- 以下为APP侧广告view渲染相关逻辑 -----------------------

        //通用展示部分，可能为空
        mTitle.setText(adData.getTitle());
        mDescription.setText(adData.getDesc());

        //设置广告icon图标，可能无返回
        if (!TextUtils.isEmpty(adData.getIconUrl())) {
            Glide.with(this).load(adData.getIconUrl()).into(mIcon);
        }
        //广告来源信息，仅穿山甲会返回
        if (!TextUtils.isEmpty(adData.getSourceText())) {
            mSource.setText(adData.getSourceText());
        }
        //视频类广告
        if (adData.isVideo()) {
            advRFVideoView.setVisibility(View.VISIBLE);
            mImagePoster.setVisibility(View.GONE);
            mGroupContainer.setVisibility(View.GONE);

        } else {//图片类广告
            //隐藏视频视图
            advRFVideoView.setVisibility(View.GONE);

            List<String> imgList = adData.getImgList();
            int picSize = imgList.size();
            LogUtil.devDebug("picSize = " + picSize);
            if (picSize > 1) {//多图
                mImagePoster.setVisibility(View.GONE);
                mGroupContainer.setVisibility(View.VISIBLE);

                String imgUrl1 = imgList.get(0);
                Glide.with(this).load(imgUrl1).into(mGroupImage1);

                String imgUrl2 = imgList.get(1);
                Glide.with(this).load(imgUrl2).into(mGroupImage2);

                if (picSize > 2) {
                    String imgUrl3 = imgList.get(2);
                    Glide.with(this).load(imgUrl3).into(mGroupImage3);
                }
            } else if (picSize > 0) {//单图
                mImagePoster.setVisibility(View.VISIBLE);
                mGroupContainer.setVisibility(View.GONE);

                String imgUrl = imgList.get(0);
                Glide.with(this).load(imgUrl).into(mImagePoster);
            }
        }

        if (adData.isDownloadAD()) {
            mCreativeButton.setText("立即下载");
            // 六要素 相关内容
            AdvanceRFDownloadElement downloadElement = adData.getDownloadElement();
            if (downloadElement != null) {
                appRoot.setVisibility(View.VISIBLE);

                mAppName.setText("应用名：" + downloadElement.getAppName());
                mAppVersion.setText("版本号：" + downloadElement.getAppVersion());
                mAppDeveloper.setText("开发者：" + downloadElement.getAppDeveloper());

                String privacy = downloadElement.getPrivacyUrl();
                if (TextUtils.isEmpty(privacy)) {
                    mAppPrivacy.setVisibility(View.GONE);
                } else {
                    mAppPrivacy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DemoUtil.openInWeb(privacy, "", null);
                        }
                    });
                }


                // 因为部分adn为异步返回信息，需要在回调里进行
                String pUrl = downloadElement.getPermissionUrl();
                downloadElement.getPermissionList(new BYAbsCallBack<ArrayList<AdvanceRFDownloadElement.AdvDownloadPermissionModel>>() {
                    @Override
                    public void invoke(ArrayList<AdvanceRFDownloadElement.AdvDownloadPermissionModel> pList) {
                        //都为空的话不展示，权限信息
                        if (TextUtils.isEmpty(pUrl) && pList.size() == 0) {
                            mAppPermission.setVisibility(View.GONE);
                        } else {
                            mAppPermission.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //优先看是否为url，然后再看是否有权限列表信息
                                    if (!TextUtils.isEmpty(pUrl)) {
                                        DemoUtil.openInWeb(pUrl, "", null);
                                    } else if (pList.size() > 0) {
                                        DemoUtil.openInWeb("", "", pList);
                                    }
                                }
                            });
                        }
                    }
                });


                String fUrl = downloadElement.getFunctionDescUrl();
                String fText = downloadElement.getFunctionDescText();
                //都为空的话不展示，介绍说明
                if (TextUtils.isEmpty(fUrl) && TextUtils.isEmpty(fText)) {
                    mAppFunction.setVisibility(View.GONE);
                } else {
                    mAppFunction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!TextUtils.isEmpty(fUrl)) {
                                DemoUtil.openInWeb(fUrl, "", null);
                            } else if (!TextUtils.isEmpty(fText)) {
                                DemoUtil.openInWeb("", fText, null);
                            }
                        }
                    });
                }

            }
        } else {
            mCreativeButton.setText("查看详情");
        }
    }
```


注意事项：<br/>1.广告初始化时，**强烈建议Context传入Activity信息**   <br/>2.在广告成功后，调用展示方法前，必须要通过**setRfMaterialProvider(AdvanceRFMaterialProvider provider)**方法传入必要参数信息，否则影响广告展示效果和曝光计费。<br/>3.注意下载类广告需要进行六要素信息外显，请参考demo实现对应功能。


