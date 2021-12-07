package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.AdvanceCustomizeAd;
import com.advance.BaseParallelAdapter;
import com.advance.advancesdkdemo.R;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;
import com.advance.supplier.gdt.GdtUtil;
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
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

import static com.advance.model.AdvanceError.ERROR_DATA_NULL;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyGdtNCAdapter implements NativeADUnifiedListener {

    private Activity activity;
    private SdkSupplier sdkSupplier;
    private AdvanceCustomizeAd customizeAd;
    private NativeUnifiedAD mAdManager;
    private ViewGroup adContainer;


    public MyGdtNCAdapter(Activity activity, AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, ViewGroup adContainer) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
        this.adContainer = adContainer;
    }

    public void loadAd() {
        try {
            GDTADManager.getInstance().initWith(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid));

            mAdManager = new NativeUnifiedAD(activity, sdkSupplier.adspotid, this);
            mAdManager.setMaxVideoDuration(60);
            mAdManager.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.NOConfirm);
            mAdManager.loadData(sdkSupplier.adCount);
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (customizeAd != null)
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
        }

    }

    @Override
    public void onADLoaded(List<NativeUnifiedADData> list) {
        if (list == null || list.isEmpty()) {
            //这里一定要调用customizeAd 的事件方法
            if (customizeAd != null)
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
        } else {
            //这里一定要调用customizeAd 的事件方法
            if (customizeAd != null)
                customizeAd.adapterDidSucceed(sdkSupplier);
            new GdtNativeAdData(list.get(0)).showAd();
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        int code = -1;
        String msg = "default onNoAD";
        if (adError != null) {
            code = adError.getErrorCode();
            msg = adError.getErrorMsg();
        }
        //这里一定要调用customizeAd 的事件方法
        if (null != customizeAd) {
            customizeAd.adapterDidFailed(AdvanceError.parseErr(code, msg));
        }
        LogUtil.AdvanceLog(code + msg);
    }


    private class GdtNativeAdData {

        private View adView;
        NativeUnifiedADData nativeUnifiedADData;

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

        public GdtNativeAdData(NativeUnifiedADData nativeUnifiedADData) {
            this.nativeUnifiedADData = nativeUnifiedADData;
            try {
                adView = LayoutInflater.from(activity).inflate(R.layout.gdt_nc_layout, null, false);
                initViews();
            } catch (Exception e) {
                e.printStackTrace();
                //这里一定要调用customizeAd 的事件方法
                if (customizeAd != null)
                    customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
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

        public void showAd() {
            try {
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
                        //这里一定要调用customizeAd 的事件方法
                        if (customizeAd != null)
                            customizeAd.adapterDidShow(sdkSupplier);
                    }

                    @Override
                    public void onADClicked() {
                        Log.d(TAG, "onADClicked: " + " clickUrl: " + nativeUnifiedADData.ext.get("clickUrl"));
                        //这里一定要调用customizeAd 的事件方法
                        if (customizeAd != null)
                            customizeAd.adapterDidClicked(sdkSupplier);

                    }

                    @Override
                    public void onADError(AdError adError) {
                        int code = -1;
                        String msg = "default onNoAD";
                        if (adError != null) {
                            code = adError.getErrorCode();
                            msg = adError.getErrorMsg();
                        }
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd) {
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(code, msg));
                        }
                        LogUtil.AdvanceLog(code + msg);
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
                //这里一定要调用customizeAd 的事件方法
                if (customizeAd != null)
                    customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
            }

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
