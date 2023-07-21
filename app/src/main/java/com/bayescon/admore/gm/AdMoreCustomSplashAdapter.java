package com.bayescon.admore.gm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.advance.advancesdkdemo.advance.LogUtil;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomServiceConfig;
import com.bytedance.msdk.api.v2.ad.custom.splash.GMCustomSplashAdapter;
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash;
import com.mercury.sdk.core.model.ADClickJumpInf;
import com.mercury.sdk.core.splash.MercurySplashData;
import com.mercury.sdk.core.splash.MercurySplashRenderListener;
import com.mercury.sdk.core.splash.MercurySplashRequestListener;
import com.mercury.sdk.core.splash.SplashAD;
import com.mercury.sdk.util.ADError;

import java.util.Map;


public class AdMoreCustomSplashAdapter extends GMCustomSplashAdapter {
    String TAG = "[" + AdMoreCustomSplashAdapter.class.getSimpleName() + "] ";
    SplashAD splashAD;
    boolean hasSucc = false;

    private Context mContext;
    @Override
    public void load(Context context, GMAdSlotSplash gmAdSlotSplash, GMCustomServiceConfig gmCustomServiceConfig) {
        try {
            mContext = context;
            splashAD = new SplashAD(context, gmCustomServiceConfig.getADNNetworkSlotId());
            splashAD.setRequestListener(new MercurySplashRequestListener() {
                @Override
                public void onAdSuccess(MercurySplashData splashData) {
                    Log.i(TAG, "onAdSuccess");

                    if (isBidding()) {//bidding类型⼴告
                        double ecpm = splashAD.getEcpm(); //当⽆权限调⽤该接⼝时，SDK会返回-1
                        if (ecpm < 0) {
                            ecpm = 0;
                        }
                        Log.e(TAG, "ecpm:" + ecpm);
                        callLoadSuccess(ecpm); //bidding⼴告成功回调，回传竞价⼴告价格
                    } else {
                        //普通类型⼴告
                        callLoadSuccess();
                    }

                    if (splashData!=null){
                        //设置广告渲染事件监听
                        splashData.setRenderListener(new MercurySplashRenderListener() {
                            @Override
                            public void onSkip() {
                                Log.i(TAG, "onSkip");
                                callSplashAdDismiss();
                            }

                            @Override
                            public void onCountDown() {
                                Log.i(TAG, "onCountDown");
                                callSplashAdDismiss();
                            }

                            @Override
                            public void onRenderSuccess() {
                                Log.i(TAG, "onRenderSuccess");
                                callSplashAdShow();
                            }

                            @Override
                            public void onClicked(ADClickJumpInf clickInf) {
                                Log.i(TAG, "onClicked");
                                callSplashAdClicked();
                            }

                            @Override
                            public void onRenderFail(ADError adError) {
                                if (adError != null) {
                                    Log.i(TAG, "onNoAD errorCode = " + adError.code + " error msg " + adError.msg);
                                    callLoadFail(new GMCustomAdError(adError.code, adError.msg));
                                } else {
                                    callLoadFail(new GMCustomAdError(111, "no ad"));
                                }
                            }
                        });
                    }
                }

                @Override
                public void onMaterialCached() {
                    Log.i(TAG, "onMaterialCached");

                }

                @Override
                public void onAdFailed(ADError adError) {
//                    isLoadSuccess = false;
                    if (adError != null) {
                        Log.i(TAG, "onNoAD errorCode = " + adError.code + " error msg " + adError.msg);
                        callLoadFail(new GMCustomAdError(adError.code, adError.msg));
                    } else {
                        callLoadFail(new GMCustomAdError(111, "no ad"));
                    }
                }
            });

            splashAD.fetchAdOnly();

        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void showAd(ViewGroup viewGroup) {
        Log.i(TAG, "showAd");

        if (splashAD != null) {
            splashAD.showAd((Activity) mContext,viewGroup);
        }
    }

    @Override
    public void receiveBidResult(boolean win, double winnerPrice, int loseReason, Map<String, Object> extra) {
        super.receiveBidResult(win, winnerPrice, loseReason, extra);
        LogUtil.high(TAG + "advance 出价是否胜出：" + win + " ，胜出价格：" + winnerPrice + " 分（人民币）" + "， loseReason = " + loseReason + "， extra = " + extra);
    }


    //回调广告成功，此时需要
    private void callSucc() {
        try {
            double eCpm = 0;
            if (splashAD != null) {
                eCpm = splashAD.getEcpm();
            }
            LogUtil.high(TAG + "advance 出价：" + eCpm + " 分（人民币）");

            if (!hasSucc) {
                hasSucc = true;
                if (eCpm > 0) {
                    callLoadSuccess(eCpm);
                } else {
                    callLoadSuccess();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 是否是Bidding广告
     *
     * @return
     */
    public boolean isBidding() {
        return getBiddingType() == GMAdConstant.AD_TYPE_CLIENT_BIDING;
    }
}
