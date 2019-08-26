package com.bayescom.advancesdkdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.WeakHashMap;

public class NativeRecyclerViewActivity extends Activity
        implements AdvanceNativeListener {

    private static final String TAG = NativeRecyclerViewActivity.class.getSimpleName();
    private AQuery mAQuery;
    private AdvanceNative mAdManager;
    private List<AdvanceNativeAdData> mAds;
    private CustomAdapter mAdapter;
    private H mHandler = new H();

    private static final int ITEM_COUNT = 50;
    private static final int FIRST_AD_POSITION = 5;
    private static final int AD_DISTANCE = 10;
    private static final int MSG_REFRESH_LIST = 1;

    private static final int TYPE_DATA = 0;
    private static final int TYPE_GDT_AD = 1;
    private static final int TYPE_CSJ_AD = 2;
    private static final int TYPE_BAYES_AD = 3;
    //穿山甲下载监听器Map
    private Map<CustomHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_view);
        initView();
        mAdManager = new AdvanceNative(this, "122121", "121212");
        mAdManager.setAdListener(this);
        mAdManager.setGdtMaxVideoDuration(60);
        mAdManager.loadAd();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        List<NormalItem> list = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; ++i) {
            list.add(new NormalItem("No." + i + " Normal Data"));
        }
        mAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
        mAQuery = new AQuery(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAds != null) {
            for (AdvanceNativeAdData ad : mAds) {
                ad.resume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAds != null) {
            for (AdvanceNativeAdData ad : mAds) {
                ad.destroy();
            }
        }
        mAds = null;
    }

    @Override
    public void onAdShow() {
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailed() {

        Toast.makeText(this, "广告失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeAdData> list) {
        Toast.makeText(this, "广告数据加载成功", Toast.LENGTH_SHORT).show();
        mAds = list;
        mHandler.sendEmptyMessage(MSG_REFRESH_LIST);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomHolder> {

        private List<Object> mData;
        private Context mContext;
        private TreeSet mADSet = new TreeSet();

        public CustomAdapter(Context context, List list) {
            mContext = context;
            mData = list;
        }

        public void addAdToPosition(AdvanceNativeAdData advanceNativeAdData, int position) {
            if (position >= 0 && position < mData.size()) {
                mData.add(position, advanceNativeAdData);
                mADSet.add(position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mADSet.contains(position)) {
                AdvanceNativeAdData advanceNativeAdData = (AdvanceNativeAdData) mData.get(position);
                if (AdvanceConfig.SDK_TAG_GDT.equals(advanceNativeAdData.getSdkTag())) {
                    return TYPE_GDT_AD;
                } else if (AdvanceConfig.SDK_TAG_CSJ.equals(advanceNativeAdData.getSdkTag())) {
                    return TYPE_CSJ_AD;
                } else {
                    return TYPE_BAYES_AD;
                }

            } else {
                return TYPE_DATA;
            }
        }

        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_GDT_AD:
                    view = LayoutInflater.from(mContext).inflate(R.layout.gdt_item_ad_unified, null);
                    break;
                case TYPE_CSJ_AD:
                    view = LayoutInflater.from(mContext).inflate(R.layout.csj_item_ad_unified, null);
                    break;
                case TYPE_BAYES_AD:
                    view = LayoutInflater.from(mContext).inflate(R.layout.bayes_item_ad_unified, null);
                    break;
                case TYPE_DATA:
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_data, null);
                    break;

                default:
                    view = null;
            }
            return new CustomHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_GDT_AD:
                    initGdtAdItemView(position, holder);
                    break;
                case TYPE_CSJ_AD:
                    initCsjAdItemView(position, holder);
                    break;
                case TYPE_BAYES_AD:
                    initBayesAdItemView(position, holder);
                    break;
                case TYPE_DATA:
                    holder.title.setText(((NormalItem) mData.get(position))
                            .getTitle());
                    break;
            }
        }

        private void initGdtAdItemView(int position, final CustomHolder holder) {
            AdvanceNativeAdData advanceNativeAdData = (AdvanceNativeAdData) mData.get(position);
            //广点通原生广告数据对象
            final GdtNativeAdData ad = (GdtNativeAdData) advanceNativeAdData;
            AQuery gdtAQ = holder.gdtAQ;
            gdtAQ.id(R.id.img_logo).image(
                    TextUtils.isEmpty(ad.getIconUrl()) ? ad.getImgUrl() : ad.getIconUrl(), false, true);
            holder.name.setText(ad.getTitle());
            holder.desc.setText(ad.getDesc());
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(holder.download);
            // 视频广告
            if (ad.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                holder.poster.setVisibility(View.INVISIBLE);
                holder.mediaView.setVisibility(View.VISIBLE);
            } else {
                holder.poster.setVisibility(View.VISIBLE);
                holder.mediaView.setVisibility(View.INVISIBLE);
            }
            ad.bindAdToView(NativeRecyclerViewActivity.this, holder.container, null,
                    clickableViews);
            gdtAQ.id(R.id.img_poster).image(ad.getImgUrl(), false, true, 0, 0,
                    new BitmapAjaxCallback() {
                        @Override
                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                            if (iv.getVisibility() == View.VISIBLE) {
                                iv.setImageBitmap(bm);
                            }
                        }
                    });

            //设置广点通广告监听器(必须)
            setGdtAdListener(holder, ad);
            //更新广点通广告视图按钮文字
            updateGdtAdAction(holder.download, ad);
        }

        private void initCsjAdItemView(int position, final CustomHolder holder) {
            AdvanceNativeAdData advanceNativeAdData = (AdvanceNativeAdData) mData.get(position);
            //获取穿山甲原生广告数据对象
            final CsjNativeAdData ad = (CsjNativeAdData) advanceNativeAdData;
            AQuery csjAQ = holder.csjAQ;
            int patternType = ad.getImageMode();

            if (patternType == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
                holder.csjLargeImage.setVisibility(View.GONE);
                holder.csjVerticalImage.setVisibility(View.GONE);
                holder.csjVideoView.setVisibility(View.GONE);
                holder.csjSmallImage.setVisibility(View.VISIBLE);
                holder.csjImageGroupContainer.setVisibility(View.GONE);
                if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                    TTImage image = ad.getImageList().get(0);
                    if (image != null && image.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image_small).image(image.getImageUrl(), false, true);
                    }
                }
            } else if (patternType == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
                holder.csjLargeImage.setVisibility(View.VISIBLE);
                holder.csjVerticalImage.setVisibility(View.GONE);
                holder.csjVideoView.setVisibility(View.GONE);
                holder.csjSmallImage.setVisibility(View.GONE);
                holder.csjImageGroupContainer.setVisibility(View.GONE);
                if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                    TTImage image = ad.getImageList().get(0);
                    if (image != null && image.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image).image(image.getImageUrl(), false, true);
                    }
                }

            } else if (patternType == TTAdConstant.IMAGE_MODE_GROUP_IMG) {
                holder.csjLargeImage.setVisibility(View.GONE);
                holder.csjVerticalImage.setVisibility(View.GONE);
                holder.csjVideoView.setVisibility(View.GONE);
                holder.csjSmallImage.setVisibility(View.GONE);
                holder.csjImageGroupContainer.setVisibility(View.VISIBLE);

                if (ad.getImageList() != null && ad.getImageList().size() >= 3) {
                    TTImage image1 = ad.getImageList().get(0);
                    TTImage image2 = ad.getImageList().get(1);
                    TTImage image3 = ad.getImageList().get(2);
                    if (image1 != null && image1.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image1).image(image1.getImageUrl(), false, false);
                    }
                    if (image2 != null && image2.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image2).image(image2.getImageUrl(), false, false);
                    }
                    if (image3 != null && image3.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image3).image(image3.getImageUrl(), false, false);
                    }
                }


            } else if (patternType == TTAdConstant.IMAGE_MODE_VIDEO) {

                holder.csjLargeImage.setVisibility(View.GONE);
                holder.csjVerticalImage.setVisibility(View.GONE);
                holder.csjVideoView.setVisibility(View.VISIBLE);
                holder.csjSmallImage.setVisibility(View.GONE);
                holder.csjImageGroupContainer.setVisibility(View.GONE);
                //视频广告设置播放状态回调（可选）
                ad.setVideoAdListener(new TTFeedAd.VideoAdListener() {
                    @Override
                    public void onVideoLoad(TTFeedAd ad) {
                        Log.d("DEMO","Video Loaded");

                    }

                    @Override
                    public void onVideoError(int errorCode, int extraCode) {
                        Log.d("DEMO","Video Error");

                    }

                    @Override
                    public void onVideoAdStartPlay(TTFeedAd ad) {
                        Log.d("DEMO","Video Start");

                    }

                    @Override
                    public void onVideoAdPaused(TTFeedAd ad) {
                        Log.d("DEMO","Video Paused");

                    }

                    @Override
                    public void onVideoAdContinuePlay(TTFeedAd ad) {
                        Log.d("DEMO","Video ContinuePlay");

                    }
                });
                if (holder.csjVideoView != null) {
                    //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                    View video = ad.getAdView();
                    if (video != null) {
                        if (video.getParent() == null) {
                            holder.csjVideoView.removeAllViews();
                            holder.csjVideoView.addView(video);
                        }
                    }
                }

            } else if (patternType == TTAdConstant.IMAGE_MODE_VERTICAL_IMG) {
                if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
                    TTImage image = ad.getImageList().get(0);
                    if (image != null && image.isValid()) {
                        csjAQ.id(R.id.iv_listitem_image_vertical).image(image.getImageUrl(), false, true);
                    }
                }


            }
            //绑定广告数据
            holder.csjTitle.setText(ad.getTitle()); //title为广告的简单信息提示
            holder.csjDescription.setText(ad.getDescription()); //description为广告的较长的说明
            holder.csjSource.setText(ad.getSource() == null ? "广告来源" : ad.getSource());
            TTImage icon = ad.getIcon();
            if (icon != null && icon.isValid()) {
                csjAQ.id(R.id.iv_listitem_icon).image(icon.getImageUrl(), false, true);

            }
            Bitmap logoBitmap = ad.getAdLogo();
            if(logoBitmap!=null)
            {
                csjAQ.id(R.id.tv_listitem_ad_logo).image(logoBitmap);
            }
            List<View> clickViewList = new ArrayList<>();
            clickViewList.add(holder.csjContainer);
            //触发创意广告的view（点击下载或拨打电话）
            List<View> creativeViewList = new ArrayList<>();
            if (null != holder.csjCreativeButton) {
                creativeViewList.add(holder.csjCreativeButton);
            }
            //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
//            creativeViewList.add(adContainer);
            //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
            ad.registerViewForInteraction(holder.csjContainer, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
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
                    ad.setActivityForDownloadApp(NativeRecyclerViewActivity.this);
                    holder.csjCreativeButton.setVisibility(View.VISIBLE);
                    //绑定下载状态监听器
                    bindCsjDownloadListener( holder.csjCreativeButton,holder, ad);
                    //绑定下载状态控制器（测试用途)
//                    bindCsjDownLoadStatusController(holder.csjStopButton,holder.csjRemoveButton, ad);
                    break;
                case TTAdConstant.INTERACTION_TYPE_DIAL:
                    holder.csjCreativeButton.setVisibility(View.VISIBLE);
                     holder.csjCreativeButton.setText("立即拨打");
                    break;
                case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
                case TTAdConstant.INTERACTION_TYPE_BROWSER:
//                    adCreativeButton.setVisibility(View.GONE);
                   holder.csjCreativeButton.setVisibility(View.VISIBLE);
                   holder.csjCreativeButton.setText("查看详情");
                    break;
                default:
                   holder.csjCreativeButton.setVisibility(View.GONE);
            }



        }

        private void initBayesAdItemView(int position, final CustomHolder holder) {
            AdvanceNativeAdData advanceNativeAdData = (AdvanceNativeAdData) mData.get(position);
            //获取倍业原生广告数据对象
            final BayesNativeAdData ad = (BayesNativeAdData) advanceNativeAdData;
            AQuery bayesAQ = holder.bayesAQ;
            //需要先绑定广告容器视图和可点击View,一般为按钮，需要在容器内部，不可以是广告容器本身
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(holder.bayesDownloadButton);
            ad.bindView(holder.bayesContainer,clickableViews);
            holder.bayesTitle.setText(ad.getTitle());
            holder.bayesDescription.setText(ad.getDescription());
            String adSource= ad.getAdSource();
            if(TextUtils.isEmpty(adSource))
            {
                holder.bayesSource.setText("广告");

            }else{
                holder.bayesSource.setText(ad.getAdSource());
            }
            bayesAQ.id(R.id.img_logo).image(ad.getIcon(),false,true);
            if(ad.isAppAd())
            {
                holder.bayesDownloadButton.setText("下载");
            }else
            {
                holder.bayesDownloadButton.setText("浏览");
            }
            if(ad.getIsVideo())
            {
                holder.bayesVideoViewContainer.setVisibility(View.VISIBLE);
                holder.bayesImageGroupContainer.setVisibility(View.GONE);
                holder.bayesImageLarge.setVisibility(View.GONE);
                BayesVideoView videoView = ad.getBayesVideoView();
                if(videoView!=null)
                {
                    if(holder.bayesVideoViewContainer !=null&&videoView.getParent()==null)
                    {
                        holder.bayesVideoViewContainer.removeAllViews();
                        holder.bayesVideoViewContainer.addView(videoView);
                        ad.muteVideo();
                        ad.playVideo();
                    }else
                    {
                        ad.resumeVideo();

                    }

                }

            }else if(ad.getImageList()!=null&&ad.getImageList().size()==1)
            {
                //单图大图
                holder.bayesVideoViewContainer.setVisibility(View.GONE);
                holder.bayesImageGroupContainer.setVisibility(View.GONE);
                holder.bayesImageLarge.setVisibility(View.VISIBLE);
                bayesAQ.id(R.id.img_poster).image(ad.getImageList().get(0),false,true);

            }else if(ad.getImageList()!=null&&ad.getImageList().size()>=3)
            {
                //小图组图
                holder.bayesVideoViewContainer.setVisibility(View.GONE);
                holder.bayesImageGroupContainer.setVisibility(View.VISIBLE);
                holder.bayesImageLarge.setVisibility(View.GONE);
                bayesAQ.id(R.id.img_1).image(ad.getImageList().get(0),false,true);
                bayesAQ.id(R.id.img_2).image(ad.getImageList().get(1),false,true);
                bayesAQ.id(R.id.img_3).image(ad.getImageList().get(2),false,true);
            }
            //发送展示上报
            ad.reportAdShow();




        }
        private void bindCsjDownloadListener(final Button mCreativeButton,
                                               final CustomHolder holder, CsjNativeAdData ad) {
           TTAppDownloadListener  ttAppDownloadListener = new TTAppDownloadListener() {
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
                    return mTTAppDownloadListenerMap.get(holder) == this;
                }
            };
            //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
            ad.setDownloadListener(ttAppDownloadListener); // 注册下载监听器
            mTTAppDownloadListenerMap.put(holder, ttAppDownloadListener);
        }

        private void setGdtAdListener(final CustomHolder holder, final GdtNativeAdData ad) {
            ad.setNativeAdEventListener(new NativeADEventListener() {
                @Override
                public void onADExposed() {
                    Log.d(TAG, "onADExposed: " + ad.getTitle());
                }

                @Override
                public void onADClicked() {
                    Log.d(TAG, "onADClicked: " + ad.getTitle());
                }

                @Override
                public void onADError(AdError error) {
                    Log.d(TAG, "onADError error code :" + error.getErrorCode()
                            + "  error msg: " + error.getErrorMsg());
                }

                @Override
                public void onADStatusChanged() {
                    updateGdtAdAction(holder.download, ad);
                }
            });
            // 视频广告
            if (ad.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                ad.bindMediaView(holder.mediaView, null, new NativeADMediaListener() {
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

                    @Override
                    public void onVideoStop() {

                    }

                    @Override
                    public void onVideoClicked() {

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public MediaView mediaView;
        public RelativeLayout adInfoContainer;
        public TextView name;
        public TextView desc;
        public ImageView logo;
        public ImageView poster;
        public Button download;
        public NativeAdContainer container;
        public AQuery gdtAQ;
        //穿山甲广告数据
        public ImageView csjSmallImage;
        public ImageView csjLargeImage;
        public ImageView csjGroupImage1;
        public ImageView csjGroupImage2;
        public ImageView csjGroupImage3;
        public ImageView csjVerticalImage;
        public FrameLayout csjVideoView;
        public TextView csjTitle;
        public TextView csjSource;
        public TextView csjDescription;
        public ImageView csjIcon;
        public Button csjCreativeButton;
        public ImageView csjLogo;
        public Button csjStopButton;
        public Button csjRemoveButton;
        public ViewGroup csjImageGroupContainer;
        public ViewGroup csjContainer;
        public AQuery csjAQ;
        //倍业广告数据
        public TextView bayesTitle;
        public TextView bayesDescription;
        public TextView bayesSource;
        public ImageView bayesIcon;
        public ImageView bayesImageLarge;
        public ImageView bayesImage1;
        public ImageView bayesImage2;
        public ImageView bayesImage3;
        public Button bayesDownloadButton;
        public FrameLayout bayesVideoViewContainer;
        public ViewGroup bayesImageGroupContainer;
        public ViewGroup bayesContainer;
        public AQuery bayesAQ;


        public CustomHolder(View itemView, int adType) {
            super(itemView);
            switch (adType) {
                case TYPE_GDT_AD:
                    mediaView = itemView.findViewById(R.id.gdt_media_view);
                    adInfoContainer = itemView.findViewById(R.id.ad_info_container);
                    logo = itemView.findViewById(R.id.img_logo);
                    poster = itemView.findViewById(R.id.img_poster);
                    name = itemView.findViewById(R.id.text_title);
                    desc = itemView.findViewById(R.id.text_desc);
                    download = itemView.findViewById(R.id.btn_download);
                    container = itemView.findViewById(R.id.native_ad_container);
                    gdtAQ = new AQuery(itemView);
                    break;
                case TYPE_CSJ_AD:
                    csjLargeImage = itemView.findViewById(R.id.iv_listitem_image);
                    csjSmallImage = itemView.findViewById(R.id.iv_listitem_image_small);
                    csjVerticalImage = itemView.findViewById(R.id.iv_listitem_image_vertical);
                    csjGroupImage1 =itemView.findViewById(R.id.iv_listitem_image1);
                    csjGroupImage2 =itemView.findViewById(R.id.iv_listitem_image2);
                    csjGroupImage3 =itemView.findViewById(R.id.iv_listitem_image3);
                    csjVideoView = itemView.findViewById(R.id.iv_listitem_video);
                    csjTitle = itemView.findViewById(R.id.tv_listitem_ad_title);
                    csjSource = itemView.findViewById(R.id.tv_listitem_ad_source);
                    csjDescription =itemView.findViewById(R.id.tv_listitem_ad_desc);
                    csjIcon = itemView.findViewById(R.id.iv_listitem_icon);
                    csjCreativeButton = itemView.findViewById(R.id.btn_listitem_creative);
                    csjLogo = itemView.findViewById(R.id.tv_listitem_ad_logo);
                    csjStopButton =itemView.findViewById(R.id.btn_listitem_stop);
                    csjRemoveButton =itemView.findViewById(R.id.btn_listitem_remove);
                    csjImageGroupContainer =itemView.findViewById(R.id.layout_image_group);
                    csjContainer =itemView.findViewById(R.id.native_ad_container);
                    csjAQ = new AQuery(itemView);
                    break;
                case TYPE_BAYES_AD:
                    bayesTitle = itemView.findViewById(R.id.text_title);
                    bayesDescription = itemView.findViewById(R.id.text_desc);
                    bayesSource = itemView.findViewById(R.id.text_adsource);
                    bayesImageLarge = itemView.findViewById(R.id.img_poster);
                    bayesImage1 = itemView.findViewById(R.id.img_1);
                    bayesImage2 = itemView.findViewById(R.id.img_2);
                    bayesImage3 = itemView.findViewById(R.id.img_3);
                    bayesIcon   = itemView.findViewById(R.id.img_logo);
                    bayesVideoViewContainer = itemView.findViewById(R.id.bayes_media_view);
                    bayesImageGroupContainer = itemView.findViewById(R.id.native_3img_ad_container);
                    bayesDownloadButton = itemView.findViewById(R.id.btn_download);
                    bayesContainer =itemView.findViewById(R.id.native_ad_container);
                    bayesAQ = new AQuery(itemView);
                    break;
                case TYPE_DATA:
                    title = itemView.findViewById(R.id.title);
                    break;

            }
        }
    }

    class NormalItem {
        private String mTitle;

        public NormalItem(String title) {
            this.mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

    }

    private class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_LIST:
                    if (mAds != null && mAds.size() > 0 && mAdapter != null) {
                        for (int i = 0; i < mAds.size(); i++) {
                            mAdapter.addAdToPosition(mAds.get(i), i * AD_DISTANCE + FIRST_AD_POSITION);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
            }
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
}
