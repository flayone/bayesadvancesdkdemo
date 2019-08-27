package com.bayescom.advancesdkdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bayesadvance.AdvanceConfig;
import com.bayesadvance.AdvanceNative;
import com.bayesadvance.AdvanceNativeAdData;
import com.bayesadvance.AdvanceNativeListener;
import com.bayesadvance.bayes.BayesNativeAdData;
import com.bayesadvance.csj.CsjNativeAdData;
import com.bayesadvance.gdt.GdtNativeAdData;
import com.bayescom.sdk.BayesVideoView;
import com.bytedance.sdk.openadsdk.DownloadStatusController;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class NativeActivity extends Activity implements AdvanceNativeListener {

    private AQuery mAQuery;
    private AdvanceNativeAdData advanceNativeAdData;
    private H mHandler = new H();
    private static final int MSG_INIT_AD = 0;
    private static final int MSG_VIDEO_START = 1;
    private FrameLayout advanceNativeAdContainer;
    private static final String TAG = NativeActivity.class.getSimpleName();
    private TTAppDownloadListener ttAppDownloadListener;


    private AdvanceNative advanceNative;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        advanceNativeAdContainer = (FrameLayout) findViewById(R.id.advance_native_ad_container);
        advanceNative = new AdvanceNative(this, Constants.mediaId,Constants.nativeAdspotId);
        advanceNative.setAdListener(this);
        advanceNative.loadAd();

    }


    private void initAd(final AdvanceNativeAdData advanceNativeAdData) {
        mAQuery = new AQuery(findViewById(R.id.advance_native_ad_container));
        if (advanceNativeAdData.getSdkTag().equals(AdvanceConfig.SDK_TAG_GDT)) {
            final GdtNativeAdData ad = (GdtNativeAdData) advanceNativeAdData;
            renderGdtAdUi(ad);
        }
        if (advanceNativeAdData.getSdkTag().equals(AdvanceConfig.SDK_TAG_CSJ)) {
            final CsjNativeAdData ad = (CsjNativeAdData) advanceNativeAdData;
            renderCsjAdUi(ad);

        }
        if (advanceNativeAdData.getSdkTag().equals(AdvanceConfig.SDK_TAG_BAYES)) {
            final BayesNativeAdData ad = (BayesNativeAdData) advanceNativeAdData;
            renderBayesAdUi(ad);

        }
    }

    @Nullable
    public static VideoOption getVideoOption() {
        VideoOption.Builder builder = new VideoOption.Builder();
        builder.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI);
        builder.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS);
        builder.setAutoPlayMuted(true);
        return builder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (advanceNativeAdData != null) {
            // 必须要在Actiivty.onResume()时通知到广告数据，以便重置广告恢复状态
            advanceNativeAdData.resume();
            }
    }

    private void renderGdtAdUi(final GdtNativeAdData ad) {
        int patternType = ad.getAdPatternType();
        View adView = LayoutInflater.from(this).inflate(R.layout.gdt_item_ad_unified, advanceNativeAdContainer, true);
        final Button mDownloadButton = adView.findViewById(R.id.btn_download);
        ImageView mImagePoster = adView.findViewById(R.id.img_poster);
        NativeAdContainer mNativeContainer = adView.findViewById(R.id.native_ad_container);

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(mDownloadButton);
        if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT
                || patternType == AdPatternType.NATIVE_VIDEO) {
            mAQuery.id(R.id.img_logo).image(ad.getImgUrl(), false, true);
            mAQuery.id(R.id.img_poster).image(ad.getImgUrl(), false, true, 0, 0,
                    new BitmapAjaxCallback() {
                        @Override
                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                            if (iv.getVisibility() == View.VISIBLE) {
                                iv.setImageBitmap(bm);
                            }
                        }
                    });
            mAQuery.id(R.id.text_title).text(ad.getTitle());
            mAQuery.id(R.id.text_desc).text(ad.getDesc());
            clickableViews.add(mImagePoster);
            clickableViews.add(adView.findViewById(R.id.img_logo));

        } else if (patternType == AdPatternType.NATIVE_3IMAGE) {
            mAQuery.id(R.id.img_1).image(ad.getImgUrls().get(0), false, true);
            mAQuery.id(R.id.img_2).image(ad.getImgUrls().get(1), false, true);
            mAQuery.id(R.id.img_3).image(ad.getImgUrls().get(2), false, true);
            mAQuery.id(R.id.native_3img_title).text(ad.getTitle());
            mAQuery.id(R.id.native_3img_desc).text(ad.getDesc());
            clickableViews.add(adView.findViewById(R.id.img_1));
            clickableViews.add(adView.findViewById(R.id.img_2));
            clickableViews.add(adView.findViewById(R.id.img_3));
        } else if (patternType == AdPatternType.NATIVE_1IMAGE_2TEXT) {
            mAQuery.id(R.id.img_logo).image(ad.getImgUrl(), false, true);
            mAQuery.id(R.id.img_poster).clear();
            mAQuery.id(R.id.text_title).text(ad.getTitle());
            mAQuery.id(R.id.text_desc).text(ad.getDesc());
            clickableViews.add(mImagePoster);
            clickableViews.add(adView.findViewById(R.id.img_logo));
        }

        ad.bindAdToView(this, mNativeContainer, null, clickableViews);
        ad.setNativeAdEventListener(new NativeADEventListener() {
            @Override
            public void onADExposed() {
                Log.d(TAG, "onADExposed: ");
            }

            @Override
            public void onADClicked() {
                Log.d(TAG, "onADClicked: ");
            }

            @Override
            public void onADError(AdError error) {
                Log.d(TAG, "onADError error code :" + error.getErrorCode()
                        + "  error msg: " + error.getErrorMsg());
            }

            @Override
            public void onADStatusChanged() {
                Log.d(TAG, "onADStatusChanged: ");
                updateGdtAdAction(mDownloadButton, ad);
            }
        });

        if (ad.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            MediaView mMediaView = mNativeContainer.findViewById(R.id.gdt_media_view);
            VideoOption videoOption = getVideoOption();
            ad.bindMediaView(mMediaView, videoOption, new NativeADMediaListener() {
                @Override
                public void onVideoInit() {
                    Log.d(TAG, "onVideoInit: ");
                }

                @Override
                public void onVideoLoading() {
                    Log.d(TAG, "onVideoLoading: ");
                }

                @Override
                public void onVideoReady() {
                    Log.d(TAG, "onVideoReady: ");
                }

                @Override
                public void onVideoLoaded(int videoDuration) {
                    Log.d(TAG, "onVideoLoaded: ");

                }

                @Override
                public void onVideoStop() {

                }

                @Override
                public void onVideoClicked() {

                }

                @Override
                public void onVideoStart() {
                    Log.d(TAG, "onVideoStart: ");
                }

                @Override
                public void onVideoPause() {
                    Log.d(TAG, "onVideoPause: ");
                }

                @Override
                public void onVideoResume() {
                    Log.d(TAG, "onVideoResume: ");
                }

                @Override
                public void onVideoCompleted() {
                    Log.d(TAG, "onVideoCompleted: ");
                }

                @Override
                public void onVideoError(AdError error) {
                    Log.d(TAG, "onVideoError: ");
                }
            });
            mImagePoster.setVisibility(View.GONE);
            mMediaView.setVisibility(View.VISIBLE);
        }

        updateGdtAdAction(mDownloadButton, ad);
    }

    private void renderCsjAdUi(CsjNativeAdData ad) {
        int patternType = ad.getImageMode();
        LayoutInflater.from(this).inflate(R.layout.csj_item_ad_unified,advanceNativeAdContainer,true);
        ImageView mSmallImage = advanceNativeAdContainer.findViewById(R.id.iv_listitem_image_small);
        ImageView mLargeImage = advanceNativeAdContainer.findViewById(R.id.iv_listitem_image);
        ViewGroup mImageGroupLayout = advanceNativeAdContainer.findViewById(R.id.layout_image_group);
        ImageView mGroupImage1= advanceNativeAdContainer.findViewById(R.id.iv_listitem_image1);
        ImageView mGroupImage2 = advanceNativeAdContainer.findViewById(R.id.iv_listitem_image2);
        ImageView mGroupImage3 = advanceNativeAdContainer.findViewById(R.id.iv_listitem_image3);
        ImageView mVerticalImage = advanceNativeAdContainer.findViewById(R.id.iv_listitem_image_vertical);
        FrameLayout mVideoView = advanceNativeAdContainer.findViewById(R.id.iv_listitem_video);

        if (patternType == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
            mSmallImage.setVisibility(View.VISIBLE);
            mLargeImage.setVisibility(View.GONE);
            mImageGroupLayout.setVisibility(View.GONE);
            mVerticalImage.setVisibility(View.GONE);
            mVideoView.setVisibility(View.GONE);

            if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                TTImage image = ad.getImageList().get(0);
                if (image != null && image.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image_small).image(image.getImageUrl(), false, true);
                }
            }
        } else if (patternType == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
            mSmallImage.setVisibility(View.GONE);
            mLargeImage.setVisibility(View.VISIBLE);
            mImageGroupLayout.setVisibility(View.GONE);
            mVerticalImage.setVisibility(View.GONE);
            mVideoView.setVisibility(View.GONE);

            if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                TTImage image = ad.getImageList().get(0);
                if (image != null && image.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image).image(image.getImageUrl(), false, true);
                }
            }

        } else if (patternType == TTAdConstant.IMAGE_MODE_GROUP_IMG) {

            mSmallImage.setVisibility(View.GONE);
            mLargeImage.setVisibility(View.GONE);
            mImageGroupLayout.setVisibility(View.VISIBLE);
            mVerticalImage.setVisibility(View.GONE);
            mVideoView.setVisibility(View.GONE);
            if (ad.getImageList() != null && ad.getImageList().size() >= 3) {
                TTImage image1 = ad.getImageList().get(0);
                TTImage image2 = ad.getImageList().get(1);
                TTImage image3 = ad.getImageList().get(2);
                if (image1 != null && image1.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image1).image(image1.getImageUrl(), false, false);
                }
                if (image2 != null && image2.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image2).image(image2.getImageUrl(), false, false);
                }
                if (image3 != null && image3.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image3).image(image3.getImageUrl(), false, false);
                }
            }


        } else if (patternType == TTAdConstant.IMAGE_MODE_VIDEO) {
            mSmallImage.setVisibility(View.GONE);
            mLargeImage.setVisibility(View.GONE);
            mImageGroupLayout.setVisibility(View.GONE);
            mVerticalImage.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
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
            });
            if (mVideoView != null) {
                //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                View video = ad.getAdView();
                if (video != null) {
                    if (video.getParent() == null) {
                        mVideoView.removeAllViews();
                        mVideoView.addView(video);
                    }
                }
            }

        } else if (patternType == TTAdConstant.IMAGE_MODE_VERTICAL_IMG) {
            mSmallImage.setVisibility(View.GONE);
            mLargeImage.setVisibility(View.GONE);
            mImageGroupLayout.setVisibility(View.GONE);
            mVerticalImage.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                TTImage image = ad.getImageList().get(0);
                if (image != null && image.isValid()) {
                    mAQuery.id(R.id.iv_listitem_image_vertical).image(image.getImageUrl(), false, true);
                }
            }
        }
        TextView mTitle = (TextView) advanceNativeAdContainer.findViewById(R.id.tv_listitem_ad_title);
        TextView mSource = (TextView) advanceNativeAdContainer.findViewById(R.id.tv_listitem_ad_source);
        TextView mDescription = (TextView) advanceNativeAdContainer.findViewById(R.id.tv_listitem_ad_desc);
        ImageView mIcon = (ImageView) advanceNativeAdContainer.findViewById(R.id.iv_listitem_icon);
        Button mCreativeButton = (Button) advanceNativeAdContainer.findViewById(R.id.btn_listitem_creative);
//        Button mStopButton = (Button) advanceNativeAdContainer.findViewById(R.id.btn_listitem_stop);
//        Button mRemoveButton = (Button) advanceNativeAdContainer.findViewById(R.id.btn_listitem_remove);
        //绑定广告数据
        mTitle.setText(ad.getTitle()); //title为广告的简单信息提示
        mDescription.setText(ad.getDescription()); //description为广告的较长的说明
        mSource.setText(TextUtils.isEmpty(ad.getSource()) ? "广告" : ad.getSource());
        TTImage icon = ad.getIcon();
        if (icon != null && icon.isValid()) {
            mAQuery.id(mIcon).image(icon.getImageUrl(), false, true);

        }
        Bitmap logoBitMap = ad.getAdLogo();
        if(logoBitMap!=null)
        {
            mAQuery.id(R.id.tv_listitem_ad_logo).image(logoBitMap);
        }
        //设置dislike弹窗，这里展示自定义的dialog
//        bindDislikeCustom(adViewHolder.mDislike, ad);

        //可以被点击的view, 也可以把convertView放进来意味item可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(advanceNativeAdContainer);
        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        if (null != mCreativeButton) {
            creativeViewList.add(mCreativeButton);
        }
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        creativeViewList.add(advanceNativeAdContainer);
        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        ad.registerViewForInteraction(advanceNativeAdContainer, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                }
            }
        });

        switch (ad.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                ad.setActivityForDownloadApp(this);
                mCreativeButton.setVisibility(View.VISIBLE);
                bindCsjDownloadListener(mCreativeButton, ad);
                //绑定下载状态控制器
                bindCsjDownLoadStatusController( ad);
                break;
            case TTAdConstant.INTERACTION_TYPE_DIAL:
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("立即拨打");
                break;
            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
            case TTAdConstant.INTERACTION_TYPE_BROWSER:
//                    adCreativeButton.setVisibility(View.GONE);
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("查看详情");
                break;
            default:
                mCreativeButton.setVisibility(View.GONE);
        }

    }


    private void bindCsjDownLoadStatusController(final CsjNativeAdData ad) {
//        final DownloadStatusController controller = ad.getDownloadStatusController();
//        if (mStopButton != null) {
//            mStopButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (controller != null) {
//                        controller.changeDownloadStatus();
//                        Log.d(TAG, "改变下载状态");
//                    }
//                }
//            });
//        }

//        mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (controller != null) {
//                    controller.cancelDownload();
//                    Log.d(TAG, "取消下载");
//                }
//            }
//        });
    }

    private void bindCsjDownloadListener(final Button mCreativeButton, CsjNativeAdData ad) {
         ttAppDownloadListener = new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                if (!isValid()) {
                    return;
                }
                mCreativeButton.setText("开始下载");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (totalBytes <= 0L) {
                    mCreativeButton.setText("下载中:0%");
                } else {
                    mCreativeButton.setText("下载中:" + (currBytes * 100 / totalBytes)+"%");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (totalBytes <= 0L) {
                    mCreativeButton.setText("下载暂停:0%");
                } else {
                    mCreativeButton.setText("下载暂停:" + (currBytes * 100 / totalBytes)+"%");
                }
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (mCreativeButton != null) {
                    mCreativeButton.setText("重新下载");
                }
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (mCreativeButton != null) {
                    mCreativeButton.setText("点击打开");
                }
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (mCreativeButton != null) {
                    mCreativeButton.setText("点击安装");
                }
            }

            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
            private boolean isValid() {
                return ttAppDownloadListener == this;
            }
        };
        //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
        ad.setDownloadListener(ttAppDownloadListener); // 注册下载监听器
//        mTTAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }

    private void renderBayesAdUi(BayesNativeAdData ad) {
        LayoutInflater.from(this).inflate(R.layout.bayes_item_ad_unified, advanceNativeAdContainer, true);
        //首先需要绑定广告容器
        Button button = advanceNativeAdContainer.findViewById(R.id.btn_download);
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(button);
        ad.bindView(advanceNativeAdContainer,clickableViews);
        mAQuery.id(R.id.img_logo).image(ad.getIcon());
        mAQuery.id(R.id.text_title).text(ad.getTitle());
        mAQuery.id(R.id.text_desc).text(ad.getDescription());
        if (ad.getIsVideo()) {
            ad.muteVideo();
            BayesVideoView videoView = ad.getBayesVideoView();
            FrameLayout bayesMediaView = advanceNativeAdContainer.findViewById(R.id.bayes_media_view);
            bayesMediaView.removeAllViews();
            bayesMediaView.addView(videoView);
            mAQuery.id(R.id.img_poster).visibility(View.GONE);
            ad.playVideo();

        }
        if (!ad.getIsVideo() && ad.getImageList() != null && ad.getImageList().size() == 1) {
            //单图
            mAQuery.id(R.id.img_poster).image(ad.getImageList().get(0));

        } else if (ad.getImageList() != null && ad.getImageList().size() >= 3) {
            //组图
            mAQuery.id(R.id.img_1).image(ad.getImageList().get(0));
            mAQuery.id(R.id.img_2).image(ad.getImageList().get(1));
            mAQuery.id(R.id.img_3).image(ad.getImageList().get(2));
        }
        if (ad.isAppAd()) {
            button.setText("下载");
        } else {
            button.setText("浏览");
        }
        mAQuery.id(R.id.text_adsource).text("广告");
        //上报展示曝光,请在真实曝光的时候上报
        ad.reportAdShow();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (advanceNativeAdData != null) {
            // 必须要在Actiivty.destroy()时通知到广告数据，以便释放内存
            advanceNativeAdData.destroy();
        }
    }

    public static void updateGdtAdAction(Button button, GdtNativeAdData ad) {
        if (!ad.isAppAd()) {
            button.setText("浏览");
            return;
        }
        switch (ad.getAppStatus()) {
            case 0:
                button.setText("下载");
                break;
            case 1:
                button.setText("启动");
                break;
            case 2:
                button.setText("更新");
                break;
            case 4:
                button.setText(ad.getProgress() + "%");
                break;
            case 8:
                button.setText("安装");
                break;
            case 16:
                button.setText("下载失败，重新下载");
                break;
            default:
                button.setText("浏览");
                break;
        }
    }


    @Override
    public void onAdShow() {
        Log.d("DEMO", "Ad Show");
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {
        Log.d("DEMO", "Ad Failed");
        Toast.makeText(this, "广告失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d("DEMO", "Ad Clicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeAdData> list) {
        if (list != null && list.size() > 0) {
            Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
            //发送给主线程
            Message msg = Message.obtain();
            msg.what = MSG_INIT_AD;
            advanceNativeAdData = list.get(0);
            msg.obj = advanceNativeAdData;
            mHandler.sendMessage(msg);

        }
    }

    private class H extends Handler {
        public H() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT_AD:
                    AdvanceNativeAdData ad = (AdvanceNativeAdData) msg.obj;
                    initAd(ad);
                    break;
                case MSG_VIDEO_START:
                    break;
            }
        }
    }
}
