package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mercury.sdk.core.config.AdConfigManager;
import com.mercury.sdk.core.config.AdPatternType;
import com.mercury.sdk.core.config.VideoOption;
import com.mercury.sdk.core.nativ.NativeAD;
import com.mercury.sdk.core.nativ.NativeADData;
import com.mercury.sdk.core.nativ.NativeADEventListener;
import com.mercury.sdk.core.nativ.NativeADListener;
import com.mercury.sdk.core.nativ.NativeADMediaListener;
import com.mercury.sdk.core.widget.MediaView;
import com.mercury.sdk.core.widget.NativeAdContainer;
import com.mercury.sdk.util.ADError;

import java.util.ArrayList;
import java.util.List;

public class MyMercuryNCAdapter implements NativeADListener {
    private Activity activity;
    private SdkSupplier sdkSupplier;
    private MyNativeCustomizeAd customizeAd;
    private NativeAD mAdManager;
    boolean threeImageHasExposure = false;

    public MyMercuryNCAdapter(Activity activity, MyNativeCustomizeAd customizeAd, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
    }

    public void loadAd() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);
            AdConfigManager.getInstance().setOaId(AdvanceConfig.getInstance().getOaid());
            mAdManager = new NativeAD(activity, sdkSupplier.adspotid, this);
            mAdManager.loadAD(sdkSupplier.adCount);
        } catch (Exception e) {
            e.printStackTrace();
            customizeAd.onFailed();
        }

    }

    @Override
    public void onADLoaded(List<NativeADData> list) {
        if (list == null || list.isEmpty()) {
            customizeAd.onFailed();
        } else {
            ArrayList<MyNativeCustomizeAdItem> advanceNativeAdDataList = new ArrayList<>();
            for (NativeADData adData : list) {
                MercuryNativeAdData advanceNativeAdData = new MercuryNativeAdData(activity, adData, customizeAd);
                advanceNativeAdDataList.add(advanceNativeAdData);
            }
            customizeAd.onLoaded(advanceNativeAdDataList);
        }
    }

    @Override
    public void onNoAD(ADError adError) {
        LogUtil.AdvanceLog("onNoAD error code: " + adError.code + ", error msg: " + adError.msg);
        customizeAd.onFailed();
    }


    class MercuryNativeAdData implements MyNativeCustomizeAdItem {

        private View adView;
        NativeADData nativeADData;
        Activity activity;
        MyNativeCustomizeAd myNativeCustomizeAd;

        ImageView mIcon;
        ImageView mDislike;
        Button mCreativeButton;
        TextView mTitle;
        TextView mDescription;
        TextView mSource;
        TextView mAdSource;

        ImageView mGroupImage1;
        ImageView mGroupImage2;
        ImageView mGroupImage3;
        LinearLayout mGroupContainer;

        private MediaView mMediaView;
        private ImageView mImagePoster;
        private NativeAdContainer nativeContainer;

        String TAG = "MercuryNativeAdData";

        public MercuryNativeAdData(Activity activity, NativeADData nativeADData, MyNativeCustomizeAd myNativeCustomizeAd) {
            this.activity = activity;
            this.nativeADData = nativeADData;
            this.myNativeCustomizeAd = myNativeCustomizeAd;

            try {
                adView = LayoutInflater.from(activity).inflate(R.layout.mer_nc_layout, null, false);
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
            mAdSource = adView.findViewById(R.id.tv_ad_source);

            mGroupContainer = adView.findViewById(R.id.native_3img);
            mGroupImage1 = adView.findViewById(R.id.img_1);
            mGroupImage2 = adView.findViewById(R.id.img_2);
            mGroupImage3 = adView.findViewById(R.id.img_3);

            mMediaView = adView.findViewById(R.id.media_view);
            mImagePoster = adView.findViewById(R.id.img_poster);
            nativeContainer = adView.findViewById(R.id.native_ad_container);
        }

        @Override
        public void showAd() {
            try {
                FrameLayout adContainer = myNativeCustomizeAd.getAdContainer();
                if (adContainer == null) {
                    Log.e(TAG, "需要先调用setAdContainer 设置广告位载体");
                    return;
                }
                adContainer.removeAllViews();

                renderAdUi(nativeADData);
                updateAdAction(nativeADData);

                List<View> clickableViews = new ArrayList<>();
                // 所有广告类型，注册点击事件
                clickableViews.add(mCreativeButton);

                if (nativeADData.getAdPatternType() == AdPatternType.NATIVE_VIDEO_2TEXT
                        || nativeADData.getAdPatternType() == AdPatternType.NATIVE_1VIDEO_1ICON_2TEXT) {

                    nativeADData.bindAdToView(activity, nativeContainer, clickableViews);

                    VideoOption videoOption = getVideoOption();
                    nativeADData.bindMediaView(mMediaView, videoOption, new NativeADMediaListener() {
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
                        public void onVideoCompleted() {
                            Log.d(TAG, "onVideoCompleted: ");
                        }

                        @Override
                        public void onVideoError(ADError error) {
                            Log.d(TAG, "onVideoError: ");
                        }


                    });
                } else if (nativeADData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_1ICON_2TEXT ||
                        nativeADData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT) {
                    // 双图双文、单图双文：注册mImagePoster的点击事件
                    clickableViews.add(mImagePoster);
                } else {
                    // 三小图广告：注册native_3img_ad_container的点击事件
                    clickableViews.add(mGroupContainer);
                }

                //核心事件去回调统一的show clicked 等方法
                nativeADData.setNativeAdEventListener(new NativeADEventListener() {
                    @Override
                    public void onADExposed() {
                        Log.d(TAG, "onADExposed: ");
                        if (customizeAd != null)
                            customizeAd.onShow(MercuryNativeAdData.this);
                    }

                    @Override
                    public void onADClicked() {
                        Log.d(TAG, "onADClicked: " + " clickUrl: " + nativeADData.ext.get("clickUrl"));
                        if (customizeAd != null)
                            customizeAd.onClicked(MercuryNativeAdData.this);
                    }

                    @Override
                    public void onADError(ADError error) {
                        Log.d(TAG, "onADError error code :" + error.code + "  error msg: " + error.msg);
                        if (customizeAd != null)
                            customizeAd.onFailed();
                    }


                });
                nativeADData.bindAdToView(activity, nativeContainer, clickableViews);

                adContainer.addView(adView);
            } catch (Exception e) {
                e.printStackTrace();
                if (customizeAd != null)
                    customizeAd.onFailed();
            }
        }

        @Override
        public String getSupplierId() {
            return AdvanceConfig.SDK_ID_MERCURY;
        }

        private VideoOption getVideoOption() {
            VideoOption videoOption;
            VideoOption.Builder builder = new VideoOption.Builder();

            builder.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS);
            builder.setAutoPlayMuted(true);
            videoOption = builder.build();
            return videoOption;
        }


        private void renderAdUi(final NativeADData ad) {
            //不包含source字段
            mSource.setVisibility(View.GONE);

            String source = ad.getADSource();
            if (!"".equals(source)) {
                mAdSource.setText(source);
            } else {
                mAdSource.setText("广告");
            }
            mAdSource.setVisibility(View.VISIBLE);

            mTitle.setText(ad.getTitle());
            mDescription.setText(ad.getDesc());

            // 这里需要在广告图片加载完成时,调用 onPicADExposure 进行曝光上报
            RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (ad.getAdPatternType() == AdPatternType.NATIVE_3IMAGE_2TEXT) {
                        if (!threeImageHasExposure) {
                            ad.onPicADExposure();
                            threeImageHasExposure = true;
                        }
                    } else {
                        ad.onPicADExposure();
                    }
                    return false;
                }
            };

            int patternType = ad.getAdPatternType();
            if (patternType == AdPatternType.NATIVE_1IMAGE_1ICON_2TEXT || patternType == AdPatternType.NATIVE_1IMAGE_2TEXT) {
                if (patternType == AdPatternType.NATIVE_1IMAGE_1ICON_2TEXT)
                    Glide.with(activity).load(ad.getIconUrl()).into(mIcon);
                Glide.with(activity).load(ad.getImgUrl()).listener(requestListener).into(mImagePoster);

                mImagePoster.setVisibility(View.VISIBLE);
                mGroupContainer.setVisibility(View.GONE);
                mMediaView.setVisibility(View.GONE);
            } else if (patternType == AdPatternType.NATIVE_1VIDEO_1ICON_2TEXT || patternType == AdPatternType.NATIVE_VIDEO_2TEXT) {
                if (patternType == AdPatternType.NATIVE_1VIDEO_1ICON_2TEXT)
                    Glide.with(activity).load(ad.getIconUrl()).into(mIcon);

                mMediaView.setVisibility(View.VISIBLE);
                mImagePoster.setVisibility(View.GONE);
                mGroupContainer.setVisibility(View.GONE);
            } else if (patternType == AdPatternType.NATIVE_3IMAGE_1ICON_2TEXT || patternType == AdPatternType.NATIVE_3IMAGE_2TEXT) {
                if (patternType == AdPatternType.NATIVE_3IMAGE_1ICON_2TEXT)
                    Glide.with(activity).load(ad.getIconUrl()).into(mIcon);

                Glide.with(activity).load(ad.getImgList().get(0)).listener(requestListener).into(mGroupImage1);
                Glide.with(activity).load(ad.getImgList().get(1)).listener(requestListener).into(mGroupImage2);
                Glide.with(activity).load(ad.getImgList().get(2)).listener(requestListener).into(mGroupImage3);

                mGroupContainer.setVisibility(View.VISIBLE);
                mMediaView.setVisibility(View.GONE);
                mImagePoster.setVisibility(View.GONE);
            }
            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.destroy();
                    if (customizeAd != null)
                        customizeAd.onClosed(MercuryNativeAdData.this);
                }
            });
        }

        void updateAdAction(NativeADData ad) {
            if (!ad.isAppAd()) {
                mCreativeButton.setText("浏览");
            } else {
                mCreativeButton.setText("下载");
            }

        }
    }
}
