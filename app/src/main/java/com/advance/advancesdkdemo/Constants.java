package com.advance.advancesdkdemo;

public class Constants {

    //示例代码位id，具体展现的是哪个广告, 由后台策略来决定。实际使用请使用自己的广告位id，一定不要使用这里的id上线发布，否则无收益！
    public static class TestIds {
        //示例id默认配置为穿山甲广告
        public static final String splashAdspotId = "10003083"; //开屏
        public static final String bannerAdspotId = "10003091"; //banner
        public static final String nativeExpressAdspotId = "10003094"; //信息流
        public static final String rewardAdspotId = "10003100"; //激励视频
        public static final String interstitialAdspotId = "10003097"; //插屏
        public static final String fullScreenVideoAdspotId = "10003103"; //全屏视频
        public static final String customNativeAdspotId = "10003120"; //原生自渲染
    }
}
