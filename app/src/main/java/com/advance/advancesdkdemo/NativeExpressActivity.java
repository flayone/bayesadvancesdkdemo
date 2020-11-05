package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceNativeExpress;
import com.advance.AdvanceNativeExpressAdItem;
import com.advance.AdvanceNativeExpressListener;
import com.advance.csj.CsjNativeExpressAdItem;
import com.advance.model.AdvanceError;
import com.bytedance.sdk.openadsdk.TTAdDislike;

import java.util.List;

public class NativeExpressActivity extends AppCompatActivity implements AdvanceNativeExpressListener {
    private AdvanceNativeExpress advanceNativeExpress;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_express);
        container = findViewById(R.id.native_express_container);

        advanceNativeExpress = new AdvanceNativeExpress(this, ADManager.getInstance().getNativeExpressAdspotId());
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(this);
        advanceNativeExpress.loadStrategy();
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
        Log.d("DEMO", "LOADED");
        if (null == list || list.isEmpty()) {
            Log.d("DEMO", "NO AD RESULT");
        } else {
            AdvanceNativeExpressAdItem advanceNativeExpressAdItem = list.get(0);

            //穿山甲需要设置dislike逻辑，要在选中回调里移除广告
            if (advanceNativeExpressAdItem.getSdkId().equals(AdvanceConfig.SDK_ID_CSJ)) {
                CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
                csjNativeExpressAdItem.setDislikeCallback(NativeExpressActivity.this, new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int i, String s) {
                        container.removeAllViews();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onRefuse() {

                    }
                });
            }

            container.removeAllViews();
            container.setVisibility(View.VISIBLE);
            container.addView(advanceNativeExpressAdItem.getExpressAdView());
            //render以后才会进行广告渲染， 广告可见才会产生曝光，否则将无法产生收益。
            advanceNativeExpressAdItem.render();
        }
    }

    @Override
    public void onAdShow(View view) {
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

        Log.d("DEMO", "SHOW");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        Toast.makeText(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code, Toast.LENGTH_SHORT).show();

        Log.d("DEMO", "FAILED");

    }

    @Override
    public void onAdRenderFailed(View view) {

        Toast.makeText(this, "广告渲染失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdRenderSuccess(View view) {
        Toast.makeText(this, "广告渲染成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked(View view) {
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
        Log.d("DEMO", "CLICKED");

    }

    @Override
    public void onAdClose(View view) {

        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
        Log.d("DEMO", "CLOSED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
