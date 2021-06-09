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

import com.advance.AdvanceConfig;
import com.advance.AdvanceNativeExpress;
import com.advance.AdvanceNativeExpressAdItem;
import com.advance.AdvanceNativeExpressListener;
import com.advance.model.AdvanceError;
import com.advance.supplier.csj.CsjNativeExpressAdItem;
import com.advance.supplier.gdt.GdtEventListener2;
import com.advance.supplier.gdt.GdtNativeAdExpressAdItem;
import com.bytedance.sdk.openadsdk.TTAdDislike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private AdvanceNativeExpress advanceNativeExpress;
    private List<AdvanceNativeExpressAdItem> mAdItemList;
    private HashMap<View, Integer> mAdViewPositionMap = new HashMap<>();

    String selectedSupplierID;//当前聚合选中的SDK渠道标识
    boolean isGdtExpress2 = false; //是否为广点通模板2.0广告
    Iterator<AdvanceNativeExpressAdItem> iterator; //广点通模板2.0处理使用的迭代器

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
     * 初始化信息流广告，加载SDK策略
     */
    private void initAdvanceNativeExpressAD() {
        //初始化
        advanceNativeExpress = new AdvanceNativeExpress(this, Constants.TestIds.nativeExpressAdspotId);
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(this);
        advanceNativeExpress.loadStrategy();
    }
    //AdvanceSDK回调接口

    @Override
    public void onAdShow(View view) {
        DemoUtil.logAndToast(this, "广告展示");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "onSdkSelected = " + id);

        selectedSupplierID = id;
    }

    @Override
    public void onAdRenderFailed(View view) {
        DemoUtil.logAndToast(this, "广告渲染失败" + view.toString());

        //广点通模板信息流2.0不在这里处理，参考processGDT2Data方法中处理
        if (isGdtExpress2) {
            return;
        }
        if (mAdViewPositionMap != null) {
            int removedPosition = mAdViewPositionMap.get(view);
            if (mAdapter != null) {
                mAdapter.removeADView(removedPosition);
            }
            mAdViewPositionMap.remove(view);
        }
    }

    @Override
    public void onAdRenderSuccess(View view) {
        DemoUtil.logAndToast(this, "广告渲染成功" + view.toString());

    }

    @Override
    public void onAdClicked(View view) {
        DemoUtil.logAndToast(this, "广告点击" + view.toString());
    }

    @Override
    public void onAdClose(View view) {
        DemoUtil.logAndToast(this, "广告关闭" + view.toString());

        //广点通模板信息流2.0不在这里处理，参考processGDT2Data方法中处理
        if (isGdtExpress2) {
            return;
        }
        if (mAdViewPositionMap != null) {
            int removedPosition = mAdViewPositionMap.get(view);
            if (mAdapter != null) {
                mAdapter.removeADView(removedPosition);
            }
            mAdViewPositionMap.remove(view);
        }
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        DemoUtil.logAndToast(this, "广告加载成功");

        mAdItemList = list;
        //从实际执行结果中获取是否是广点通模板2.0类型广告
        isGdtExpress2 = AdvanceConfig.SDK_ID_GDT.equals(selectedSupplierID) && advanceNativeExpress.isGdtExpress2();
        Log.i(TAG, "isGdtExpress2: " + isGdtExpress2);
        mAdapter.setGdtExpress2(isGdtExpress2);

        if (isGdtExpress2) {
            iterator = list.iterator();
            processGDT2Data(0);
        } else {
            for (int i = 0; i < mAdItemList.size(); i++) {
                int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
                if (position <= mNormalDataList.size()) {
                    AdvanceNativeExpressAdItem item = mAdItemList.get(i);

                    mAdViewPositionMap.put(item.getExpressAdView(), position); // 把每个广告在列表中位置记录下来
                    mAdapter.addADItemToPosition(position, item);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 因为广点通模板2.0 的广告是渲染成功后，才有广告的 View，进而添加到UI中显示。
     * 如果多条广告同时开始渲染，渲染成功的回调顺序是不确定的，有可能第 2 条先渲染成功，然后第 1 条才渲染成功，
     * 这样导致的结果可能就是列表被滚动到第 2 条广告第位置，但其实用户并没有滑动。
     * 所以这里采用一条一条的渲染广告的方式，当前广告渲染成功或失败后再去渲染下一条广告。
     */
    private void processGDT2Data(final int i) {
        if (iterator.hasNext()) {
            final AdvanceNativeExpressAdItem data = iterator.next();

            final int position = FIRST_AD_POSITION + ITEMS_PER_AD * i + 1;
            if (position < mNormalDataList.size()) {
                final GdtNativeAdExpressAdItem gdtItem = (GdtNativeAdExpressAdItem) data;
                gdtItem.setAdEventListener2(new GdtEventListener2() {

                    @Override
                    public void onRenderSuccess(View nativeExpressADView) {
                        mAdViewPositionMap.put(nativeExpressADView, position);
                        mAdapter.addADItemToPosition(position, data);
                        mAdapter.notifyItemInserted(position);
                        // 当前广告渲染成功，开始渲染下一条广告
                        processGDT2Data(i + 1);
                    }

                    @Override
                    public void onRenderFail(View nativeExpressADView) {
                        processGDT2Data(i + 1);

                    }

                    @Override
                    public void onAdClosed(View nativeExpressADView) {
                        gdtItem.destroy();
                        Log.i(TAG, "onAdClosed, position:" + position);
                        if (mAdapter != null) {
                            int position = mAdViewPositionMap.get(nativeExpressADView);
                            mAdapter.removeADView(position);
                        }
                    }
                });
                gdtItem.render();
            }
        }
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
        private boolean gdtExpress2 = false;

        public CustomAdapter(List list) {
            mData = list;
        }

        public void setGdtExpress2(boolean gdtExpress2) {
            this.gdtExpress2 = gdtExpress2;
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

                if (adView != null) {
                    if (adView.getParent() != null)
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    customViewHolder.container.addView(adView);
                    mAdViewPositionMap.put(adView, position); // 广告在列表中的位置是可以被更新的
                }

                //穿山甲需要设置dislike逻辑，否则无法关闭广告
                if (AdvanceConfig.SDK_ID_CSJ.equals(advanceNativeExpressAdItem.getSdkId())) {
                    CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
                    csjNativeExpressAdItem.setDislikeCallback(NativeExpressRecyclerViewActivity.this, new TTAdDislike.DislikeInteractionCallback() {
                        @Override
                        public void onShow() {

                        }

                        @Override
                        public void onSelected(int i, String s, boolean enforce) {
                            customViewHolder.container.removeAllViews();
                        }

                        @Override
                        public void onCancel() {

                        }

//                        @Override
//                        public void onRefuse() {
//
//                        }
                    });
                }

                //广点通2.0需执行单独的渲染逻辑
                if (!gdtExpress2) {
                    //一定要进行render 否则无法成功展示广告
                    advanceNativeExpressAdItem.render();
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
                title = view.findViewById(R.id.title);
                container = view.findViewById(R.id.express_ad_container);
            }
        }
    }


}
