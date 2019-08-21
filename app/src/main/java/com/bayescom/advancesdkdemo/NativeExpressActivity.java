package com.bayescom.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bayesadvance.AdvanceConfig;
import com.bayesadvance.AdvanceNativeExpress;
import com.bayesadvance.AdvanceNativeExpressAdItem;
import com.bayesadvance.AdvanceNativeExpressListener;
import com.bayesadvance.csj.CsjNativeExpressAdItem;
import com.bayesadvance.gdt.GdtNativeAdExpressAdItem;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class NativeExpressActivity extends AppCompatActivity implements AdvanceNativeExpressListener {
    private AdvanceNativeExpress advanceNativeExpress;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_express);
        container = findViewById(R.id.native_express_container);
        advanceNativeExpress = new AdvanceNativeExpress(this, "121212", "121212");
        advanceNativeExpress.setExpressViewAcceptedSize(600,300)
                .setCsjImageAcceptedSize(640,320)
                .setGdtAutoHeight(true)
                .setGdtFullWidth(true);
        advanceNativeExpress.setAdListener(this);
        advanceNativeExpress.loadAd();
    }

    @Override
    public void onAdShow() {

        Log.d("DEMO", "SHOW");
    }

    @Override
    public void onAdFailed() {
        Log.d("DEMO", "FAILED");

    }

    @Override
    public void onAdClicked() {
        Log.d("DEMO", "CLICKED");

    }

    @Override
    public void onAdClose(View view) {

        Log.d("DEMO", "CLOSED");
    }

    @Override
    public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
        Log.d("DEMO", "LOADED");
        if (null == list && list.isEmpty()) {
            return;
        } else {
            for(AdvanceNativeExpressAdItem advanceNativeExpressAdItem :list) {

                if (advanceNativeExpressAdItem.getSdkTag().equals(AdvanceConfig.SDK_TAG_GDT)) {
                    GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem = (GdtNativeAdExpressAdItem) advanceNativeExpressAdItem;
                    if(gdtNativeAdExpressAdItem.getBoundData().getAdPatternType()==AdPatternType.NATIVE_VIDEO)
                    {
                        renderGdtExpressAd(gdtNativeAdExpressAdItem);
                        break;
                    }
                } else if (advanceNativeExpressAdItem.getSdkTag().equals(AdvanceConfig.SDK_TAG_CSJ)) {
                    CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem)  advanceNativeExpressAdItem;
                    if(csjNativeExpressAdItem.getImageMode()==TTAdConstant.IMAGE_MODE_VIDEO)
                    {
                        renderCsjExpressAd(csjNativeExpressAdItem);
                        break;

                    }
                }
            }

        }

    }

    public void renderGdtExpressAd(GdtNativeAdExpressAdItem gdtNativeAdExpressAdItem) {
        container.removeAllViews();
        container.setVisibility(View.VISIBLE);
        if (gdtNativeAdExpressAdItem.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            gdtNativeAdExpressAdItem.setMediaListener(new NativeExpressMediaListener() {
                @Override
                public void onVideoInit(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoLoading(NativeExpressADView nativeExpressADView) {

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
        // 广告可见才会产生曝光，否则将无法产生收益。
        container.addView(gdtNativeAdExpressAdItem.getNativeExpressADView());
        gdtNativeAdExpressAdItem.render();


    }

    public void renderCsjExpressAd(CsjNativeExpressAdItem csjNativeExpressAdItem) {
        csjNativeExpressAdItem.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d("DEMO","CLICKED");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d("DEMO","SHOW");

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.d("DEMO","RENDER FAILED");
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                container.removeAllViews();
                container.setVisibility(View.VISIBLE);
                container.addView(view);
            }
        });
        csjNativeExpressAdItem.setDislikeCallback(this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int i, String s) {
                container.setVisibility(View.GONE);
            }
            @Override
            public void onCancel() {

            }
        });
        if(csjNativeExpressAdItem.getInteractionType()==TTAdConstant.INTERACTION_TYPE_DOWNLOAD)
        {
            csjNativeExpressAdItem.setDownloadListener(new TTAppDownloadListener() {
                @Override
                public void onIdle() {

                }

                @Override
                public void onDownloadActive(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadPaused(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadFailed(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadFinished(long l, String s, String s1) {

                }

                @Override
                public void onInstalled(String s, String s1) {

                }
            });
        }
        csjNativeExpressAdItem.render();

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
