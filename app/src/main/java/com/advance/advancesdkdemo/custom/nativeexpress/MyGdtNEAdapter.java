package com.advance.advancesdkdemo.custom.nativeexpress;

import android.app.Activity;
import android.view.View;

import com.advance.AdvanceCustomizeAd;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

import static com.advance.model.AdvanceError.ERROR_DATA_NULL;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyGdtNEAdapter {
    private Activity activity;
    private AdvanceCustomizeAd customizeAd;
    private SdkSupplier sdkSupplier;
    private CustomExpressEventListener listener;
    private NativeExpressAD nativeExpressAd;

    public MyGdtNEAdapter(Activity activity, AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, CustomExpressEventListener listener) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
        this.listener = listener;
    }

    public void loadAd() {
        try {

            ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT);
            nativeExpressAd = new NativeExpressAD(activity, adSize, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, new NativeExpressAD.NativeExpressADListener() {
                @Override
                public void onADLoaded(List<NativeExpressADView> list) {
                    try {
                        if (list == null || list.isEmpty()) {

                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
                            }
                        } else {
                            List<CustomExpressAdItem> CustomNativeExpressAdItemList = new ArrayList<>();
                            for (NativeExpressADView nativeExpressADView : list) {
                                GdtCustomExpressAdItem customNativeExpressAdItem = new GdtCustomExpressAdItem(nativeExpressADView);
                                CustomNativeExpressAdItemList.add(customNativeExpressAdItem);
                            }

                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidSucceed(sdkSupplier);
                            }
                            if (listener != null) {
                                listener.onADLoaded(CustomNativeExpressAdItemList);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd)
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
                    }

                }

                @Override
                public void onRenderFail(NativeExpressADView nativeExpressADView) {

                    //这里一定要调用customizeAd 的事件方法
                    if (listener != null) {
                        listener.onADRenderFailed(nativeExpressADView);
                    }
                }

                @Override
                public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADExposure(NativeExpressADView nativeExpressADView) {

                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow(sdkSupplier);
                    }
                }

                @Override
                public void onADClicked(NativeExpressADView nativeExpressADView) {

                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked(sdkSupplier);
                    }
                }

                @Override
                public void onADClosed(NativeExpressADView nativeExpressADView) {
                    if (listener != null) {
                        listener.onADClosed(nativeExpressADView);
                    }
                }

                @Override
                public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

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
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(code,msg));
                    }
                    LogUtil.AdvanceLog(code + msg);
                }
            }); // 这里的Context必须为Activity
            nativeExpressAd.setMaxVideoDuration(60);
            nativeExpressAd.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.NOConfirm);
            nativeExpressAd.loadAD(sdkSupplier.adCount);
        } catch (
                Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd) {
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
            }
        }

    }


    class GdtCustomExpressAdItem implements CustomExpressAdItem {
        private NativeExpressADView nativeExpressADView;

        public GdtCustomExpressAdItem(NativeExpressADView nativeExpressADView) {
            this.nativeExpressADView = nativeExpressADView;
        }


        @Override
        public void destroy() {
            if (null != nativeExpressADView) {
                nativeExpressADView.destroy();
            }
        }

        @Override
        public void render() {
            if (nativeExpressADView != null) {
                nativeExpressADView.render();
            }
        }

        @Override
        public View getExpressAdView() {
            return nativeExpressADView;
        }
    }
}
