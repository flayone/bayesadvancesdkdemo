package com.bayescom.advancesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bayesadvance.AdvanceConfig;
import com.bayesadvance.AdvanceNativeExpress;
import com.bayesadvance.AdvanceNativeExpressAdItem;
import com.bayesadvance.AdvanceNativeExpressListener;
import com.bayesadvance.csj.CsjNativeExpressAdItem;
import com.bayesadvance.gdt.GdtNativeAdExpressAdItem;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 在消息流中接入原生模板广告的示例
 */

public class NativeExpressRecyclerViewActivity extends Activity implements
        AdvanceNativeExpressListener {

    private static final String TAG = NativeExpressRecyclerViewActivity.class.getSimpleName();
    public static final int MAX_ITEMS = 50;
    public static int FIRST_AD_POSITION = 1; // 第一条广告的位置
    public static int ITEMS_PER_AD = 10;     // 每间隔10个条目插入一条广告

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private CustomAdapter mAdapter;
    private List<NormalItem> mNormalDataList = new ArrayList<NormalItem>();
    private AdvanceNativeExpress mADManager;
    private List<AdvanceNativeExpressAdItem> mAdItemList;
    private HashMap<View, Integer> mAdViewPositionMap = new HashMap<View, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_native_express_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 使用完了每一个NativeExpressADView之后都要释放掉资源。
        if (mAdItemList != null) {
            for (AdvanceNativeExpressAdItem item : mAdItemList) {
                item.destroy();
            }
        }
    }

    private void initData() {
        for (int i = 0; i < MAX_ITEMS; ++i) {
            mNormalDataList.add(new NormalItem("No." + i + " Normal Data"));
        }
        mAdapter = new CustomAdapter(mNormalDataList);
        mRecyclerView.setAdapter(mAdapter);
        initAdvanceNativeExpressAD();
    }

    /**
     *
     */
    private void initAdvanceNativeExpressAD() {
        mADManager = new AdvanceNativeExpress(this, "1212121", "12121");
        mADManager.setExpressViewAcceptedSize(600, 250)
                .setGdtFullWidth(true)
                .setGdtAutoHeight(true)
                .setCsjImageAcceptedSize(640, 320)
                .setAdListener(this);
        mADManager.loadAd();
    }
    //AdvanceSDK回调接口

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
    public void onAdClose(View view) {
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onADClosed: " + view.toString());
        if (mAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(view);
            mAdapter.removeADView(removedPosition);
        }

    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        Toast.makeText(this, "广告加载完成", Toast.LENGTH_SHORT).show();
        mAdItemList = list;
        for (int i = 0; i < mAdItemList.size(); i++) {
            int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
            if (position < mNormalDataList.size()) {
                AdvanceNativeExpressAdItem item = mAdItemList.get(i);
                if (AdvanceConfig.SDK_TAG_GDT.equals(item.getSdkTag())) {
                    GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem = (GdtNativeAdExpressAdItem) item;
                    if (gdtNativeAdExpressAdItem.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        gdtNativeAdExpressAdItem.setMediaListener(mediaListener);
                    }
                } else if (AdvanceConfig.SDK_TAG_CSJ.equals(item.getSdkTag())) {

                    CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) item;
                    //设置穿山甲广告交互监听器(必须)
                    csjNativeExpressAdItem.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int i) {

                        }

                        @Override
                        public void onAdShow(View view, int i) {

                        }

                        @Override
                        public void onRenderFail(View view, String s, int i) {

                        }

                        @Override
                        public void onRenderSuccess(View view, float v, float v1) {

                        }
                    });
                    //设置穿山甲SDK关闭监听器
                    csjNativeExpressAdItem.setDislikeCallback(this, new TTAdDislike.DislikeInteractionCallback() {
                        @Override
                        public void onSelected(int i, String s) {
                            Toast.makeText(NativeExpressRecyclerViewActivity.this,s,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                mAdViewPositionMap.put(item.getExpressAdView(), position); // 把每个广告在列表中位置记录下来
                mAdapter.addADItemToPosition(position, mAdItemList.get(i));
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    public class NormalItem {
        private String title;

        public NormalItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    /**
     * RecyclerView的Adapter
     */
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        static final int TYPE_DATA = 0;
        static final int TYPE_AD = 1;
        private List<Object> mData;

        public CustomAdapter(List list) {
            mData = list;
        }

        // 把返回的NativeExpressADView添加到数据集里面去
        public void addADItemToPosition(int position, AdvanceNativeExpressAdItem adItem) {
            if (position >= 0 && position < mData.size() && adItem != null) {
                mData.add(position, adItem);
            }
        }

        // 移除NativeExpressADView的时候是一条一条移除的
        public void removeADView(int position) {
            mData.remove(position);
            mAdapter.notifyItemRemoved(position); // position为adView在当前列表中的位置
            mAdapter.notifyItemRangeChanged(0, mData.size() - 1);
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mData.get(position) instanceof AdvanceNativeExpressAdItem ? TYPE_AD : TYPE_DATA;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
            int type = getItemViewType(position);
            if (TYPE_AD == type) {
                final AdvanceNativeExpressAdItem advanceNativeExpressAdItem = (AdvanceNativeExpressAdItem) mData.get(position);
                mAdViewPositionMap.put(advanceNativeExpressAdItem.getExpressAdView(), position); // 广告在列表中的位置是可以被更新的


                if (AdvanceConfig.SDK_TAG_GDT.equals(advanceNativeExpressAdItem.getSdkTag())) {
                    //广点通adview渲染方式
                    GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem = (GdtNativeAdExpressAdItem) advanceNativeExpressAdItem;
                    NativeExpressADView adView = gdtNativeAdExpressAdItem.getNativeExpressADView();
                    if (customViewHolder.container.getChildCount() > 0
                            && customViewHolder.container.getChildAt(0) == adView) {
                        return;
                    }

                    if (customViewHolder.container.getChildCount() > 0) {
                        customViewHolder.container.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }

                    customViewHolder.container.addView(adView);
                    adView.render(); // 调用render方法后sdk才会开始展示广告
                } else if (AdvanceConfig.SDK_TAG_CSJ.equals(advanceNativeExpressAdItem.getSdkTag())) {
                    //穿山甲渲染方式
                    CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
                    View adView = csjNativeExpressAdItem.getExpressAdView();
                    if (customViewHolder.container.getChildCount() > 0
                            && customViewHolder.container.getChildAt(0) == adView) {
                        return;
                    }

                    if (customViewHolder.container.getChildCount() > 0) {
                        customViewHolder.container.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }

                    customViewHolder.container.addView(adView);

                    csjNativeExpressAdItem.render(); //调用穿山甲render渲染方法

                }
            } else {
                customViewHolder.title.setText(((NormalItem) mData.get(position)).getTitle());
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            int layoutId = (viewType == TYPE_AD) ? R.layout.item_express_ad : R.layout.item_data;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ViewGroup container;

            public CustomViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                container = (ViewGroup) view.findViewById(R.id.express_ad_container);
            }
        }
    }

    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ")
                        .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }

    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition());
            return videoBuilder.toString();
        }
        return null;
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoInit: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoStart: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPause: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoComplete: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };
}
