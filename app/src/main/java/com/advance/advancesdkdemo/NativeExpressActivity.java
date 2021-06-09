package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceNativeExpress;
import com.advance.AdvanceNativeExpressAdItem;
import com.advance.AdvanceNativeExpressListener;
import com.advance.model.AdvanceError;
import com.advance.supplier.csj.CsjNativeExpressAdItem;
import com.advance.utils.LogUtil;
import com.bytedance.sdk.openadsdk.TTAdDislike;

import java.util.List;

public class NativeExpressActivity extends AppCompatActivity implements AdvanceNativeExpressListener {
    private AdvanceNativeExpress advanceNativeExpress;
    private FrameLayout container;
    private boolean isGdtExpress2 = false;
    AdvanceNativeExpressAdItem advanceNativeExpressAdItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_express);
        container = findViewById(R.id.native_express_container);

        //初始化
        advanceNativeExpress = new AdvanceNativeExpress(this, Constants.TestIds.nativeExpressAdspotId);
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(this);
        advanceNativeExpress.loadStrategy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (advanceNativeExpress != null) {
            advanceNativeExpress.destroy();
        }
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        DemoUtil.logAndToast(this, "广告加载成功");

        if (null == list || list.isEmpty()) {
            Log.d("DEMO", "NO AD RESULT");
        } else {
            advanceNativeExpressAdItem = list.get(0);

            //穿山甲需要设置dislike逻辑，要在选中回调里移除广告
            if (AdvanceConfig.SDK_ID_CSJ.equals(advanceNativeExpressAdItem.getSdkId())) {
                CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
                csjNativeExpressAdItem.setDislikeCallback(NativeExpressActivity.this, new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onSelected(int i, String s, boolean enforce) {
                        if (container != null)
                            container.removeAllViews();
                    }

                    @Override
                    public void onCancel() {

                    }

//                    @Override
//                    public void onRefuse() {
//
//                    }
                });
            }

            //从实际执行结果中获取是否是广点通模板2.0类型广告
            isGdtExpress2 = AdvanceConfig.SDK_ID_GDT.equals(advanceNativeExpressAdItem.getSdkId()) && advanceNativeExpress.isGdtExpress2();

            //广点通模板2.0不可以在这里可以直接添加视图，否则无法展示，应该在onAdRenderSuccess中添加视图
            if (!isGdtExpress2) {
                container.removeAllViews();
                container.setVisibility(View.VISIBLE);
                container.addView(advanceNativeExpressAdItem.getExpressAdView());
            }
            //render以后才会进行广告渲染， 广告可见才会产生曝光，否则将无法产生收益。
            advanceNativeExpressAdItem.render();
        }
    }

    @Override
    public void onAdRenderSuccess(View view) {
        DemoUtil.logAndToast(this, "广告渲染成功");

        //需要对回调进行主线程切换，防止回调在非主线程导致崩溃
        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
        if (isMainThread) {
            renderGdt2();
        } else {
            //如果是非主线程，需要强制切换到主线程来进行初始化
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LogUtil.AdvanceLog("force to main thread run render");
                    renderGdt2();
                }
            });
        }
    }

    private void renderGdt2(){
        //广点通模板2.0 需要在RenderSuccess以后再加载视图
        if (advanceNativeExpressAdItem != null && advanceNativeExpressAdItem.getSdkId().equals(AdvanceConfig.SDK_ID_GDT) && isGdtExpress2) {
            container.removeAllViews();
            container.setVisibility(View.VISIBLE);

            // 广告可见才会产生曝光，否则将无法产生收益。
            container.addView(advanceNativeExpressAdItem.getExpressAdView());
        }
    }

    @Override
    public void onAdClose(View view) {
        //移除布局
        container.removeAllViews();
        DemoUtil.logAndToast(this, "广告关闭");
    }

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
    }

    @Override
    public void onAdRenderFailed(View view) {
        DemoUtil.logAndToast(this, "广告渲染失败");
    }

    @Override
    public void onAdClicked(View view) {
        DemoUtil.logAndToast(this, "广告点击");
    }



}
