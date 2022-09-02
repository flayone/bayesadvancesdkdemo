package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.advance.advancesdkdemo.advance.AdvanceAD;
import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.util.NormalItem;
import com.bayescom.admore.core.AMError;
import com.bayescom.admore.nativ.AdMoreNativeExpress;
import com.bayescom.admore.nativ.AdMoreNativeExpressListener;

import java.util.ArrayList;
import java.util.List;

public class AdMoreNativeActivity extends AppCompatActivity {

    public static final int MAX_ITEMS = 80;
    public static int FIRST_AD_POSITION = 1; // 第一条广告的位置
    public static int ITEMS_PER_AD = 15;     // 每间隔多少个条目插入一条广告
    //需要加在的广告id信息，这里添加了三条，注意！！！id一定不要重复
    private final String[] adIds = new String[]{Constants.TestIds.adMoreNativeAdspotId_1, Constants.TestIds.adMoreNativeAdspotId_2, Constants.TestIds.adMoreNativeAdspotId_3};

    private RecyclerView mRecyclerView;
    private final List<NormalItem> mNormalDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_express_recycler_view);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //一定要加上该配置，防止item复用导致广告重复
        mRecyclerView.setItemViewCacheSize(500);

        initData();
    }

    private void initData() {
        for (int i = 0; i < MAX_ITEMS; ++i) {
            mNormalDataList.add(new NormalItem("No." + i + " Normal Data"));
        }
        //添加广告的数目，这里定义了3条广告。
        for (int i = 0; i < adIds.length; i++) {
            int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
            if (position <= mNormalDataList.size()) {
                //将广告的NormalItem定义为默认的空标题item。
                //也可以不同item使用不同的广告位id作为广告标识，这样方便区分不同item的广告数据表现。
                final NormalItem itemAD = new NormalItem("");
                //获取到广告id，进行初始化操作
                itemAD.adMoreNativeExpress = new AdMoreNativeExpress(this, adIds[i], new AdMoreNativeExpressListener() {
                    @Override
                    public void onClose() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onClose");
                    }

                    @Override
                    public void onRenderSuccess() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onRenderSuccess");

                    }

                    @Override
                    public void onRenderFailed() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onRenderFailed");


                    }

                    @Override
                    public void onSuccess() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onSuccess");

                    }

                    @Override
                    public void onShow() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onShow");


                    }

                    @Override
                    public void onClick() {
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, " onClick");


                    }

                    @Override
                    public void onFailed(AMError amError) {
                        String eMsg = "";
                        if (amError != null) {
                            eMsg = amError.toString();
                        }
                        AdvanceAD.logAndToast(AdMoreNativeActivity.this, "onFailed,eMsg = " + eMsg);

                    }
                });
                mNormalDataList.add(position, itemAD);
            }
        }

        //列表adapter创建，传入要渲染的数据信息
        CustomAdapter mAdapter = new CustomAdapter(this, mNormalDataList);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * RecyclerView的Adapter
     */
    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        static final int TYPE_DATA = 0;
        static final int TYPE_AD = 1;
        private final List<NormalItem> mData;
        Activity mActivity;

        public CustomAdapter(Activity activity, List<NormalItem> list) {
            mActivity = activity;
            mData = list;
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
            //核心步骤：根据是否包含标题内容，来判断是否为广告item
            return TextUtils.isEmpty(mData.get(position).title) ? TYPE_AD : TYPE_DATA;
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, final int position) {
            int type = getItemViewType(position);
            if (TYPE_AD == type) {
                //广告核心步骤：设置广告展示用布局
                mData.get(position).adMoreNativeExpress.setAdContainer(customViewHolder.container);
                //开始加载广告
                mData.get(position).adMoreNativeExpress.loadAndShow();

            } else {
                customViewHolder.title.setText(mData.get(position).getTitle());
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            int layoutId = (viewType == TYPE_AD) ? R.layout.item_express_ad : R.layout.item_data;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            return new CustomViewHolder(view);
        }

        static class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView test;
            public ViewGroup container; // 广告承载布局

            public CustomViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                container = view.findViewById(R.id.express_ad_container);
                test = view.findViewById(R.id.tv_ad);
            }
        }
    }


}