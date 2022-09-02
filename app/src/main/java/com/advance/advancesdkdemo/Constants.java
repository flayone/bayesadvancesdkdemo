package com.advance.advancesdkdemo;

public class Constants {

    //应用id（仅测试用，不可用来上线正式使用！）
    public static final String APP_ID = "100171";

    //示例代码位id（仅测试用，不可用来上线正式使用！），具体展现的是哪个广告, 由后台策略来决定。实际使用请使用自己的广告位id，一定不要使用这里的测试id上线发布，否则无收益！
    public static class TestIds {
        //        //以下测试id默认配置了测试用bidding广告，不产生收益
        public static final String adMoreSplashAdspotId = "10006504"; //admore开屏
        public static final String adMoreNativeAdspotId_1 = "10006505"; //admore信息流1
        public static final String adMoreNativeAdspotId_2 = "10006526"; //admore信息流2
        public static final String adMoreNativeAdspotId_3 = "10006527"; //admore信息流3
        public static final String adMoreRewardAdspotId = "10006506"; //admore激励视频

    }


    public static final String SP_AGREE_PRIVACY = "agree_privacy";
    public static final String SP_NAME = "preference";
}
