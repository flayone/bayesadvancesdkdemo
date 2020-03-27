package com.advance.advancesdkdemo;

public class ADManager {
    private static ADManager instance;

    private String mediaId;
    private String splashAdspotId;
    private String bannerAdspotId;
    private String nativeExpressAdspotId;
    private String rewardAdspotId;
    private String interstitialAdspotId;
    private String fullScreenVideoAdspotId;

    public static synchronized ADManager getInstance() {
        if (instance == null) {
            instance = new ADManager();
        }
        return instance;
    }


    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getSplashAdspotId() {
        return splashAdspotId;
    }

    public void setSplashAdspotId(String splashAdspotId) {
        this.splashAdspotId = splashAdspotId;
    }

    public String getBannerAdspotId() {
        return bannerAdspotId;
    }

    public void setBannerAdspotId(String bannerAdspotId) {
        this.bannerAdspotId = bannerAdspotId;
    }

    public String getNativeExpressAdspotId() {
        return nativeExpressAdspotId;
    }

    public void setNativeExpressAdspotId(String nativeExpressAdspotId) {
        this.nativeExpressAdspotId = nativeExpressAdspotId;
    }

    public String getRewardAdspotId() {
        return rewardAdspotId;
    }

    public void setRewardAdspotId(String rewardAdspotId) {
        this.rewardAdspotId = rewardAdspotId;
    }

    public String getInterstitialAdspotId() {
        return interstitialAdspotId;
    }

    public void setInterstitialAdspotId(String interstitialAdspotId) {
        this.interstitialAdspotId = interstitialAdspotId;
    }

    public String getFullScreenVideoAdspotId() {
        return fullScreenVideoAdspotId;
    }

    public void setFullScreenVideoAdspotId(String fullScreenVideoAdspotId) {
        this.fullScreenVideoAdspotId = fullScreenVideoAdspotId;
    }
}