package com.advance.advancesdkdemo;

public class Constants {

    // 配置为Mercury渠道优先的广告用例
   public static class Mercury {
        public static final String splashAdspotId = "10000531";
        public static final String bannerAdspotId = "10003093";
        public static final String nativeExpressAdspotId = "10003096";
        public static final String rewardAdspotId = "10003102";
        public static final String interstitialAdspotId = "10000398";
        public static final String customNativeAdspotId = "10003122";


        //Mercury渠道目前不支持全屏视频
        public static final String fullScreenVideoAdspotId = "";
    }

    // 配置为穿山甲渠道优先的广告用例
    public static class Csj {
        //目前穿山甲后台没有使用模板广告选项。
//        public static final String splashAdspotId = "10003083";
        public static final String splashAdspotId = "10004193";
        //使用模板的穿山甲广告
        public static final String bannerAdspotId = "10003091";
        public static final String nativeExpressAdspotId = "10003094";
        public static final String rewardAdspotId = "10003100";
        public static final String interstitialAdspotId = "10003097";
        public static final String fullScreenVideoAdspotId = "10003103";
        public static final String customNativeAdspotId = "10003120";
    }

    // 配置为广点通渠道优先的广告用例
   public static class Gdt {
        public static final String splashAdspotId = "10003079";
        public static final String bannerAdspotId = "10003092";
        public static final String nativeExpressAdspotId = "10003095";
        public static final String rewardAdspotId = "10003101";
        public static final String interstitialAdspotId = "10003098";
        public static final String fullScreenVideoAdspotId = "10003104";
        public static final String customNativeAdspotId = "10003121";
    }
}
