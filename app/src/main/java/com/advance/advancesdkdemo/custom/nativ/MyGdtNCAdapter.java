package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.R;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class MyGdtNCAdapter implements NativeADUnifiedListener {

    private Activity activity;
    private SdkSupplier sdkSupplier;
    private MyNativeCustomizeAd customizeAd;
    private NativeUnifiedAD mAdManager;

    public MyGdtNCAdapter(Activity activity, MyNativeCustomizeAd customizeAd, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;

    }

    public void loadAd() {
        try {
            mAdManager = new NativeUnifiedAD(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, this);
            mAdManager.setMaxVideoDuration(60);
            mAdManager.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.NOConfirm);
            mAdManager.loadData(sdkSupplier.adCount);
        } catch (Exception e) {
            e.printStackTrace();
            customizeAd.onFailed();
        }

    }

    @Override
    public void onADLoaded(List<NativeUnifiedADData> list) {
        if (list == null || list.isEmpty()) {
            customizeAd.onFailed();
        } else {
            ArrayList<MyNativeCustomizeAdItem> advanceNativeAdDataList = new ArrayList<>();
            for (NativeUnifiedADData nativeUnifiedADData : list) {
                GdtNativeAdData advanceNativeAdData = new GdtNativeAdData(activity, nativeUnifiedADData, customizeAd);
                advanceNativeAdDataList.add(advanceNativeAdData);
            }
            customizeAd.onLoaded(advanceNativeAdDataList);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        LogUtil.AdvanceLog(adError.getErrorCode() + adError.getErrorMsg());
        customizeAd.onFailed();
    }


    private class GdtNativeAdData implements MyNativeCustomizeAdItem {

        private View adView;
        NativeUnifiedADData nativeUnifiedADData;
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

        private MediaView mMediaView;
        private ImageView mImagePoster;
        private NativeAdContainer nativeContainer;

        String TAG = "GdtNativeAdData";

        public GdtNativeAdData(Activity activity, NativeUnifiedADData nativeUnifiedADData, MyNativeCustomizeAd myNativeCustomizeAd) {
            this.activity = activity;
            this.nativeUnifiedADData = nativeUnifiedADData;
            this.myNativeCustomizeAd = myNativeCustomizeAd;

            try {
                adView = LayoutInflater.from(activity).inflate(R.layout.gdt_nc_layout, null, false);
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

            mGroupContainer = adView.findViewById(R.id.native_3img);
            mGroupImage1 = adView.findViewById(R.id.img_1);
            mGroupImage2 = adView.findViewById(R.id.img_2);
            mGroupImage3 = adView.findViewById(R.id.img_3);

            mMediaView = adView.findViewById(R.id.gdt_media_view);
            mImagePoster = adView.findViewById(R.id.img_poster);
            nativeContainer = adView.findViewById(R.id.native_ad_container);
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

                renderAdUi(nativeUnifiedADData);
                updateAdAction(nativeUnifiedADData);

                List<View> clickableViews = new ArrayList<>();
                // 所有广告类型，注册点击事件
                clickableViews.add(mCreativeButton);

                if (nativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {

                    nativeUnifiedADData.bindAdToView(activity, nativeContainer, null, clickableViews);

                    VideoOption videoOption = getVideoOption();
                    nativeUnifiedADData.bindMediaView(mMediaView, videoOption, new NativeADMediaListener() {
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
                            Log.d(TAG, "onVideoReady");
                        }

                        @Override
                        public void onVideoLoaded(int videoDuration) {
                            Log.d(TAG, "onVideoLoaded: ");
                        }

                        @Override
                        public void onVideoStart() {
                            Log.d(TAG, "onVideoStart");
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

                        @Override
                        public void onVideoStop() {
                            Log.d(TAG, "onVideoStop");
                        }

                        @Override
                        public void onVideoClicked() {
                            Log.d(TAG, "onVideoClicked");
                        }
                    });
                } else if (nativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT ||
                        nativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT) {
                    // 双图双文、单图双文：注册mImagePoster的点击事件
                    clickableViews.add(mImagePoster);
                    nativeUnifiedADData.bindAdToView(activity, nativeContainer, null, clickableViews);
                } else {
                    // 三小图广告：注册native_3img_ad_container的点击事件
                    clickableViews.add(mGroupContainer);
                    nativeUnifiedADData.bindAdToView(activity, nativeContainer, null, clickableViews);
                }
                nativeUnifiedADData.setNativeAdEventListener(new NativeADEventListener() {
                    @Override
                    public void onADExposed() {
                        Log.d(TAG, "onADExposed: ");
                        if (customizeAd != null)
                            customizeAd.onShow(GdtNativeAdData.this);
                    }

                    @Override
                    public void onADClicked() {
                        Log.d(TAG, "onADClicked: " + " clickUrl: " + nativeUnifiedADData.ext.get("clickUrl"));
                        if (customizeAd != null)
                            customizeAd.onClicked(GdtNativeAdData.this);

                    }

                    @Override
                    public void onADError(AdError error) {
                        Log.d(TAG, "onADError error code :" + error.getErrorCode() + "  error msg: " + error.getErrorMsg());
                        if (customizeAd != null)
                            customizeAd.onFailed();
                    }

                    @Override
                    public void onADStatusChanged() {
                        Log.d(TAG, "onADStatusChanged: ");
                        updateAdAction(nativeUnifiedADData);
                    }
                });
                adContainer.addView(adView);
            } catch (Exception e) {
                e.printStackTrace();
                if (customizeAd != null)
                    customizeAd.onFailed();
            }

        }

        @Override
        public String getSDKTag() {
            return AdvanceConfig.SDK_TAG_GDT;
        }

        private VideoOption getVideoOption() {
            VideoOption videoOption;
            VideoOption.Builder builder = new VideoOption.Builder();

            builder.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS);
            builder.setAutoPlayMuted(true);
            builder.setDetailPageMuted(false);
            builder.setNeedCoverImage(true);
            builder.setNeedProgressBar(true);
            builder.setEnableDetailPage(true);
            builder.setEnableUserControl(false);
            videoOption = builder.build();
            return videoOption;
        }


        private void renderAdUi(final NativeUnifiedADData ad) {
            //广点通不包含source字段
            mSource.setVisibility(View.GONE);

            mTitle.setText(ad.getTitle());
            mDescription.setText(ad.getDesc());

            int patternType = ad.getAdPatternType();
            if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT || patternType == AdPatternType.NATIVE_1IMAGE_2TEXT) {
                if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT) {
                    Glide.with(activity).load(ad.getIconUrl()).into(mIcon);
                }
                Glide.with(activity).load(ad.getImgUrl()).into(mImagePoster);
                mImagePoster.setVisibility(View.VISIBLE);
                mGroupContainer.setVisibility(View.GONE);
                mMediaView.setVisibility(View.GONE);
            } else if (patternType == AdPatternType.NATIVE_VIDEO) {
                Glide.with(activity).load(ad.getIconUrl()).into(mIcon);

                mImagePoster.setVisibility(View.GONE);
                mGroupContainer.setVisibility(View.GONE);
                mMediaView.setVisibility(View.VISIBLE);
            } else if (patternType == AdPatternType.NATIVE_3IMAGE) {
                Glide.with(activity).load(ad.getIconUrl()).into(mIcon);

                Glide.with(activity).load(ad.getImgList().get(0)).into(mGroupImage1);
                Glide.with(activity).load(ad.getImgList().get(1)).into(mGroupImage2);
                Glide.with(activity).load(ad.getImgList().get(2)).into(mGroupImage3);
                mGroupContainer.setVisibility(View.VISIBLE);
                mMediaView.setVisibility(View.GONE);
                mImagePoster.setVisibility(View.GONE);
            }
            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.destroy();
                    nativeContainer.removeAllViews();
                    if (customizeAd != null)
                        customizeAd.onClosed(GdtNativeAdData.this);
                }
            });
        }

        void updateAdAction(NativeUnifiedADData ad) {
            if (!ad.isAppAd()) {
                mCreativeButton.setText("浏览");
                return;
            }
            switch (ad.getAppStatus()) {
                case 0:
                    mCreativeButton.setText("下载");
                    break;
                case 1:
                    mCreativeButton.setText("启动");
                    break;
                case 2:
                    mCreativeButton.setText("更新");
                    break;
                case 4:
                    mCreativeButton.setText(ad.getProgress() + "%");
                    break;
                case 8:
                    mCreativeButton.setText("安装");
                    break;
                case 16:
                    mCreativeButton.setText("下载失败，重新下载");
                    break;
                default:
                    mCreativeButton.setText("浏览");
                    break;
            }
        }
    }

}