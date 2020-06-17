package com.advance.advancesdkdemo.custom.nativeexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.NativeExpressRecyclerViewActivity;
import com.advance.advancesdkdemo.R;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomNativeExpressListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private AdvanceCustomizeAd customizeAd;
    private List<NativeExpressRecyclerViewActivity.NormalItem> mNormalDataList = new ArrayList<>();
    private CustomAdapter mAdapter;
    private CustomExpressEventListener listener;

    private HashMap<View, Integer> mAdViewPositionMap = new HashMap<>();
    private List<CustomExpressAdItem> mAdItemList;

    public static final int MAX_ITEMS = 50;
    public static int FIRST_AD_POSITION = 1; // 第一条广告的位置
    public static int ITEMS_PER_AD = 10;     // 每间隔10个条目插入一条广告

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_express_recycler_view);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initList();
        loadExpressAD();
    }


    private void loadExpressAD() {
        //统一核心事件回调
        listener = new CustomExpressEventListener() {
            @Override
            public void onADLoaded(List<CustomExpressAdItem> list) {
                mAdItemList = list;
                for (int i = 0; i < mAdItemList.size(); i++) {
                    int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
                    if (position <= mNormalDataList.size()) {
                        CustomExpressAdItem item = mAdItemList.get(i);
                        mAdViewPositionMap.put(item.getExpressAdView(), position); // 把每个广告在列表中位置记录下来
                        mAdapter.addADItemToPosition(position, mAdItemList.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onADRenderFailed(View view) {
                if (mAdapter != null) {
                    int removedPosition = mAdViewPositionMap.get(view);
                    mAdapter.removeADView(removedPosition);
                }
                mAdViewPositionMap.remove(view);
            }

            @Override
            public void onADClosed(View view) {
                if (mAdapter != null) {
                    int removedPosition = mAdViewPositionMap.get(view);
                    mAdapter.removeADView(removedPosition);
                }
                mAdViewPositionMap.remove(view);
            }
        };
        customizeAd = new AdvanceCustomizeAd(this, ADManager.getInstance().getNativeExpressAdspotId());
        //设置渠道的结果监听
        customizeAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调
            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        new MyCsjNEAdapter(CustomNativeExpressListActivity.this, customizeAd, selectedSupplier, listener).loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        new MyGdtNEAdapter(CustomNativeExpressListActivity.this, customizeAd, selectedSupplier, listener).loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        new MyMercuryNEAdapter(CustomNativeExpressListActivity.this, customizeAd, selectedSupplier, listener).loadAd();
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        customizeAd.selectSdkSupplier();
                }
            }
        });
        //推荐：设置是否采用策略缓存
        customizeAd.enableStrategyCache(true);
        //必须：设置打底SDK参数，SdkSupplier（"对应渠道平台申请的广告位id", 渠道平台id标识）
        customizeAd.setDefaultSdkSupplier(new SdkSupplier("10002678", AdvanceSupplierID.MERCURY));
        //必须：请求策略
        customizeAd.loadStrategy();
    }


    private void initList() {
        for (int i = 0; i < MAX_ITEMS; ++i) {
            mNormalDataList.add(new NativeExpressRecyclerViewActivity.NormalItem("No." + i + " Normal Data"));
        }
        mAdapter = new CustomAdapter(mNormalDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用完了每一个NativeExpressADView之后都要释放掉资源。
        if (mAdItemList != null) {
            for (CustomExpressAdItem item : mAdItemList) {
                item.destroy();
            }
        }
    }


    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        static final int TYPE_DATA = 0;
        static final int TYPE_AD = 1;
        private List<Object> mData;

        public CustomAdapter(List list) {
            mData = list;
        }

        // 把返回的NativeExpressADView添加到数据集里面去
        public void addADItemToPosition(int position, CustomExpressAdItem adItem) {
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
            return mData.get(position) instanceof CustomExpressAdItem ? TYPE_AD : TYPE_DATA;
        }

        @Override
        public void onBindViewHolder(final CustomAdapter.CustomViewHolder customViewHolder, final int position) {
            int type = getItemViewType(position);
            if (TYPE_AD == type) {
                final CustomExpressAdItem advanceNativeExpressAdItem = (CustomExpressAdItem) mData.get(position);

                View adView = advanceNativeExpressAdItem.getExpressAdView();
                if (customViewHolder.container.getChildCount() > 0
                        && customViewHolder.container.getChildAt(0) == adView) {
                    return;
                }
                if (adView == null) {
                    return;
                }

                if (customViewHolder.container.getChildCount() > 0) {
                    customViewHolder.container.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                mAdViewPositionMap.put(adView, position); // 广告在列表中的位置是可以被更新的

                //将广告添加到布局
                customViewHolder.container.addView(adView);
                //渲染广告
                advanceNativeExpressAdItem.render();

            } else {
                customViewHolder.title.setText(((NativeExpressRecyclerViewActivity.NormalItem) mData.get(position)).getTitle());
            }
        }

        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            int layoutId = (viewType == TYPE_AD) ? R.layout.item_express_ad : R.layout.item_data;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, null);
            CustomAdapter.CustomViewHolder viewHolder = new CustomAdapter.CustomViewHolder(view);
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
