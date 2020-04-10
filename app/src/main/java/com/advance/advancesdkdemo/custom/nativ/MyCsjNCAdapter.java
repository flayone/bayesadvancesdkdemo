package com.advance.advancesdkdemo.custom.nativ;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.R;
import com.advance.model.SdkSupplier;
import com.advance.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCsjNCAdapter implements TTAdNative.FeedAdListener {

    private Activity activity;
    private SdkSupplier sdkSupplier;
    private MyNativeCustomizeAd customizeAd;
    private TTAdNative mAdManager;

    public MyCsjNCAdapter(Activity activity, MyNativeCustomizeAd customizeAd, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
    }

    public void loadAd() {
        //初始化方法，这里也可以自己去创建初始化方法
        TTAdSdk.init(activity.getApplicationContext(),
                new TTAdConfig.Builder()
                        .appId(sdkSupplier.mediaid)
                        .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName(AdvanceConfig.getInstance().getAppName())
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_LIGHT)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .debug(AdvanceConfig.getInstance().getDebug()) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_4G, TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                        .supportMultiProcess(true) //是否支持多进程，true支持
                        .build());


        final TTAdManager ttAdManager = TTAdSdk.getAdManager();
        if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
            ttAdManager.requestPermissionIfNecessary(activity);
        }
        TTAdNative ttAdNative = ttAdManager.createAdNative(activity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        ttAdManager.requestPermissionIfNecessary(activity);
        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                // 必选参数 设置您的CodeId
                .setCodeId(sdkSupplier.adspotid)
                // 必选参数 设置广告图片的最大尺寸及期望的图片宽高比，单位Px
                .setImageAcceptedSize(640, 320)
                // 可选参数 设置是否支持deeplink
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .build();

        ttAdNative.loadFeedAd(adSlot, this);
    }

    @Override
    public void onError(int code, String message) {
        LogUtil.AdvanceLog("onNoAD error code: " + code + ", error msg: " + message);
        customizeAd.onFailed();
    }


    @Override
    public void onFeedAdLoad(List<TTFeedAd> ads) {
        if (ads == null || ads.isEmpty()) {
            customizeAd.onFailed();
        } else {
            ArrayList<MyNativeCustomizeAdItem> advanceNativeAdDataList = new ArrayList<>();
            for (TTFeedAd adData : ads) {
                CsjAdData advanceNativeAdData = new CsjAdData(activity, adData, customizeAd);
                advanceNativeAdDataList.add(advanceNativeAdData);
            }
            customizeAd.onLoaded(advanceNativeAdDataList);
        }
    }

    class CsjAdData implements MyNativeCustomizeAdItem {
        private View adView;
        TTFeedAd ad;
        Activity activity;
        MyNativeCustomizeAd myNativeCustomizeAd;

        ImageView mIcon;
        ImageView mDislike;
        Button mCreativeButton;
        TextView mTitle;
        TextView mDescription;
        TextView mSource;

        ImageView mGroupImage1;
        ImageView mGroupImage2;
        ImageView mGroupImage3;
        LinearLayout mGroupContainer;

        private FrameLayout videoLayout;
        private ImageView mImagePoster;
        private ImageView mSmallImagePoster;
        private LinearLayout nativeContainer;
        private Map<CsjAdData, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

        String TAG = "CsjAdData";

        public CsjAdData(Activity activity, TTFeedAd adData, MyNativeCustomizeAd customizeAd) {
            this.activity = activity;
            this.ad = adData;
            this.myNativeCustomizeAd = customizeAd;

            try {
                //小图布局不太一样
                if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
                    adView = LayoutInflater.from(activity).inflate(R.layout.csj_nc_layout_small, null, false);
                } else {
                    adView = LayoutInflater.from(activity).inflate(R.layout.csj_nc_layout, null, false);
                }
                initViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void initViews() {
            mIcon = adView.findViewById(R.id.iv_nc_icon);
            mDislike = adView.findViewById(R.id.iv_nc_dislike);
            mCreativeButton = adView.findViewById(R.id.btn_nc_creative);
            mTitle = adView.findViewById(R.id.tv_nc_ad_title);
            mDescription = adView.findViewById(R.id.tv_nc_ad_desc);
            mSource = adView.findViewById(R.id.tv_nc_ad_source);


            nativeContainer = adView.findViewById(R.id.native_ad_container);

            if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
                mSmallImagePoster = adView.findViewById(R.id.iv_nc_small_image);
            } else {
                mGroupContainer = adView.findViewById(R.id.native_3img);
                mGroupImage1 = adView.findViewById(R.id.img_1);
                mGroupImage2 = adView.findViewById(R.id.img_2);
                mGroupImage3 = adView.findViewById(R.id.img_3);

                videoLayout = adView.findViewById(R.id.iv_nc_video);
                mImagePoster = adView.findViewById(R.id.img_poster);
            }

        }

        @Override
        public void showAd() {
            try {
                FrameLayout adContainer = myNativeCustomizeAd.getAdContainer();
                if (adContainer == null) {
                    Log.e("GdtNativeAdData", "需要先调用setAdContainer 设置广告位载体");
                    return;
                }
                adContainer.removeAllViews();
                //设置dislike弹窗，这里展示自定义的dialog
                bindDislikeCustom(ad);

                bindData(ad);
                adContainer.addView(adView);

            } catch (Exception e) {
                e.printStackTrace();
                if (customizeAd != null)
                    customizeAd.onFailed();
            }
        }

        private void bindData(TTFeedAd ad) {
            //可以被点击的view, 也可以把adView放进来意味整个布局可被点击
            List<View> clickViewList = new ArrayList<>();
            clickViewList.add(adView);
            //触发创意广告的view（点击下载或拨打电话）
            List<View> creativeViewList = new ArrayList<>();
            creativeViewList.add(mCreativeButton);
            //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
//            creativeViewList.add(convertView);
            //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
            ad.registerViewForInteraction((ViewGroup) adView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    if (customizeAd != null)
                        customizeAd.onClicked(CsjAdData.this);
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
                    if (customizeAd != null)
                        customizeAd.onClicked(CsjAdData.this);
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    if (customizeAd != null)
                        customizeAd.onShow(CsjAdData.this);
                }
            });
            mTitle.setText(ad.getTitle()); //title为广告的简单信息提示
            mDescription.setText(ad.getDescription()); //description为广告的较长的说明
            mSource.setText(ad.getSource() == null ? "广告来源" : ad.getSource());
            TTImage icon = ad.getIcon();
            if (icon != null && icon.isValid()) {
                Glide.with(activity).load(icon.getImageUrl()).into(mIcon);
            }

            switch (ad.getImageMode()) {
                case TTAdConstant.IMAGE_MODE_SMALL_IMG:
                    if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                        TTImage image = ad.getImageList().get(0);
                        if (image != null && image.isValid()) {
                            Glide.with(activity).load(image.getImageUrl()).into(mSmallImagePoster);
                        }
                    }

                    break;
                case TTAdConstant.IMAGE_MODE_LARGE_IMG:
                case TTAdConstant.IMAGE_MODE_VERTICAL_IMG:
                    if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                        TTImage image = ad.getImageList().get(0);
                        if (image != null && image.isValid()) {
                            Glide.with(activity).load(image.getImageUrl()).into(mImagePoster);
                        }
                    }

                    mImagePoster.setVisibility(View.VISIBLE);
                    videoLayout.setVisibility(View.GONE);
                    mGroupContainer.setVisibility(View.GONE);
                    break;
                case TTAdConstant.IMAGE_MODE_VIDEO:

                    videoLayout.setVisibility(View.VISIBLE);
                    mGroupContainer.setVisibility(View.GONE);
                    mImagePoster.setVisibility(View.GONE);
                    //视频广告设置播放状态回调（可选）
                    ad.setVideoAdListener(new TTFeedAd.VideoAdListener() {
                        @Override
                        public void onVideoLoad(TTFeedAd ad) {

                        }

                        @Override
                        public void onVideoError(int errorCode, int extraCode) {

                        }

                        @Override
                        public void onVideoAdStartPlay(TTFeedAd ad) {

                        }

                        @Override
                        public void onVideoAdPaused(TTFeedAd ad) {

                        }

                        @Override
                        public void onVideoAdContinuePlay(TTFeedAd ad) {

                        }

                        @Override
                        public void onProgressUpdate(long current, long duration) {
                            Log.e("VideoAdListener", "===onProgressUpdate current:" + current + " duration:" + duration);
                        }

                        @Override
                        public void onVideoAdComplete(TTFeedAd ad) {
                            Log.e("VideoAdListener", "===onVideoAdComplete");
                        }
                    });
                    break;
                case TTAdConstant.IMAGE_MODE_GROUP_IMG:
                    if (ad.getImageList() != null && ad.getImageList().size() >= 3) {
                        TTImage image1 = ad.getImageList().get(0);
                        TTImage image2 = ad.getImageList().get(1);
                        TTImage image3 = ad.getImageList().get(2);
                        if (image1 != null && image1.isValid()) {
                            Glide.with(activity).load(image1.getImageUrl()).into(mGroupImage1);
                        }
                        if (image2 != null && image2.isValid()) {
                            Glide.with(activity).load(image2.getImageUrl()).into(mGroupImage2);
                        }
                        if (image3 != null && image3.isValid()) {
                            Glide.with(activity).load(image3.getImageUrl()).into(mGroupImage3);
                        }

                        mGroupContainer.setVisibility(View.VISIBLE);
                        videoLayout.setVisibility(View.GONE);
                        mImagePoster.setVisibility(View.GONE);
                    }
                    break;
            }

            switch (ad.getInteractionType()) {
                case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                    //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                    ad.setActivityForDownloadApp(activity);
                    mCreativeButton.setVisibility(View.VISIBLE);
                    bindDownloadListener(mCreativeButton, ad);
                    break;
                case TTAdConstant.INTERACTION_TYPE_DIAL:
                    mCreativeButton.setVisibility(View.VISIBLE);
                    mCreativeButton.setText("立即拨打");
                    break;
                case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
                case TTAdConstant.INTERACTION_TYPE_BROWSER:
                    mCreativeButton.setVisibility(View.VISIBLE);
                    mCreativeButton.setText("查看详情");
                    break;
                default://                    交互类型异常
                    mCreativeButton.setVisibility(View.GONE);
            }


        }

        private void bindDislikeCustom(TTFeedAd ad) {
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(activity, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    if (customizeAd != null)
                        customizeAd.onClosed(CsjAdData.this);
                }
            });
            final TTAdDislike ttAdDislike = ad.getDislikeDialog(dislikeDialog);

            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //展示dislike可以自行调用dialog
                    dislikeDialog.show();

                    //也可以使用接口来展示
                    //ttAdDislike.showDislikeDialog();
                }
            });
        }

        private void bindDownloadListener(final Button adCreativeButton, TTFeedAd ad) {
            TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
                @Override
                public void onIdle() {
                    if (!isValid()) {
                        return;
                    }
                    adCreativeButton.setText("开始下载");

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                    if (!isValid()) {
                        return;
                    }
                    if (totalBytes <= 0L) {
                        adCreativeButton.setText("0%");
                    } else {
                        adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
                    }

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                    if (!isValid()) {
                        return;
                    }
                    if (totalBytes <= 0L) {
                        adCreativeButton.setText("0%");
                    } else {
                        adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
                    }

                }

                @Override
                public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                    if (!isValid()) {
                        return;
                    }
                    adCreativeButton.setText("重新下载");

                }

                @Override
                public void onInstalled(String fileName, String appName) {
                    if (!isValid()) {
                        return;
                    }
                    adCreativeButton.setText("点击打开");

                }

                @Override
                public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                    if (!isValid()) {
                        return;
                    }
                    adCreativeButton.setText("点击安装");

                }

                @SuppressWarnings("BooleanMethodIsAlwaysInverted")
                private boolean isValid() {
                    return mTTAppDownloadListenerMap.get(CsjAdData.this) == this;
                }
            };
            //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
            ad.setDownloadListener(downloadListener); // 注册下载监听器
            mTTAppDownloadListenerMap.put(CsjAdData.this, downloadListener);
        }


        @Override
        public String getSDKTag() {
            return AdvanceConfig.SDK_TAG_CSJ;
        }
    }
}
