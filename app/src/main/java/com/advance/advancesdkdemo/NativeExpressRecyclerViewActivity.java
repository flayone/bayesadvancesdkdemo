package com.advance.advancesdkdemo;

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

import com.advance.AdvanceConfig;
import com.advance.AdvanceNativeExpress;
import com.advance.AdvanceNativeExpressAdItem;
import com.advance.AdvanceNativeExpressListener;
import com.advance.mercury.MercuryNativeExpressAdItem;
import com.advance.csj.CsjNativeExpressAdItem;
import com.advance.gdt.GdtNativeAdExpressAdItem;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.mercury.sdk.util.ADError;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
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
    private CustomAdapter mAdapter;
    private List<NormalItem> mNormalDataList = new ArrayList<>();
    private AdvanceNativeExpress mADManager;
    private List<AdvanceNativeExpressAdItem> mAdItemList;
    private HashMap<View, Integer> mAdViewPositionMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_native_express_recycler_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        mADManager = new AdvanceNativeExpress(this, ADManager.getInstance().getNativeExpressAdspotId());
        //可选：设置定制化参数
        mADManager.setExpressViewAcceptedSize(600, 250)
                .setGdtFullWidth(true)
                .setGdtAutoHeight(true)
                .setCsjImageAcceptedSize(640, 320);
        //推荐：核心事件监听回调
        mADManager.setAdListener(this);
        //推荐：设置是否采用策略缓存
        mADManager.enableStrategyCache(true);
        //必须：设置打底SDK参数，SdkSupplier（"对应渠道平台申请的广告位id", 渠道平台id标识）
        mADManager.setDefaultSdkSupplier(new SdkSupplier("10002678", AdvanceSupplierID.MERCURY));
        mADManager.loadAd();
    }
    //AdvanceSDK回调接口

    @Override
    public void onAdShow(View view) {
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {
        Toast.makeText(this, "广告失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdRenderFailed(View view) {
        Toast.makeText(this, "广告渲染失败", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onADRenderFail: " + view.toString());
        if (mAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(view);
            mAdapter.removeADView(removedPosition);
        }
        mAdViewPositionMap.remove(view);
    }

    @Override
    public void onAdRenderSuccess(View view) {

    }

    @Override
    public void onAdClicked(View view) {

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
        mAdViewPositionMap.remove(view);

    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        Toast.makeText(this, "广告加载完成", Toast.LENGTH_SHORT).show();
        mAdItemList = list;
        for (int i = 0; i < mAdItemList.size(); i++) {
            int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
            if (position <= mNormalDataList.size()) {
                AdvanceNativeExpressAdItem item = mAdItemList.get(i);
                if (AdvanceConfig.SDK_ID_GDT.equals(item.getSdkId())) {
                    GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem = (GdtNativeAdExpressAdItem) item;
                    if (gdtNativeAdExpressAdItem.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        gdtNativeAdExpressAdItem.setMediaListener(new NativeExpressMediaListener() {
                            @Override
                            public void onVideoInit(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoLoading(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoCached(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {

                            }

                            @Override
                            public void onVideoStart(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoPause(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoComplete(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

                            }

                            @Override
                            public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

                            }
                        });
                    }
                }
                if (AdvanceConfig.SDK_ID_MERCURY.equals((item.getSdkId()))) {
                    MercuryNativeExpressAdItem mercuryNativeExpressAdItem = (MercuryNativeExpressAdItem) item;
                    if (mercuryNativeExpressAdItem.getAdPatternType() == com.mercury.sdk.core.config.AdPatternType.NATIVE_VIDEO_2TEXT ||
                            mercuryNativeExpressAdItem.getAdPatternType() == com.mercury.sdk.core.config.AdPatternType.NATIVE_1VIDEO_1ICON_2TEXT) {
                        mercuryNativeExpressAdItem.setMediaListener(new com.mercury.sdk.core.nativ.NativeExpressMediaListener() {
                            @Override
                            public void onVideoInit(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoLoading(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoReady(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView, long l) {

                            }

                            @Override
                            public void onVideoStart(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoPause(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoComplete(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView) {

                            }

                            @Override
                            public void onVideoError(com.mercury.sdk.core.nativ.NativeExpressADView nativeExpressADView, ADError adError) {

                            }
                        });
                    }

                } else if (AdvanceConfig.SDK_ID_CSJ.equals(item.getSdkId())) {

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
                            Toast.makeText(NativeExpressRecyclerViewActivity.this, s, Toast.LENGTH_SHORT).show();
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

    public static class NormalItem {
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

                View adView = advanceNativeExpressAdItem.getExpressAdView();
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
                mAdViewPositionMap.put(adView, position); // 广告在列表中的位置是可以被更新的

                if (AdvanceConfig.SDK_ID_GDT.equals(advanceNativeExpressAdItem.getSdkId())) {
                    //广点通adview渲染方式
                    GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem = (GdtNativeAdExpressAdItem) advanceNativeExpressAdItem;
                    gdtNativeAdExpressAdItem.render(); // 调用render方法后sdk才会开始展示广告
                } else if (AdvanceConfig.SDK_ID_MERCURY.equals(advanceNativeExpressAdItem.getSdkId())) {
                    MercuryNativeExpressAdItem mercuryNativeExpressAdItem = (MercuryNativeExpressAdItem) advanceNativeExpressAdItem;
                    mercuryNativeExpressAdItem.render();// 调用render方法后sdk才会开始展示广告
                } else if (AdvanceConfig.SDK_ID_CSJ.equals(advanceNativeExpressAdItem.getSdkId())) {
                    //穿山甲渲染方式
                    CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
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


}
