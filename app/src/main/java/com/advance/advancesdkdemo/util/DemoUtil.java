package com.advance.advancesdkdemo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.MyApplication;
import com.advance.advancesdkdemo.custom.DemoWebActivity;
import com.advance.core.srender.AdvanceRFDownloadElement;
import com.advance.supplier.csj.AdvanceCsjManager;
import com.advance.utils.LogUtil;
import com.bayes.sdk.basic.itf.BYBaseCallBack;
import com.bayes.sdk.basic.util.BYThreadUtil;
import com.bayes.sdk.basic.util.BYToast;
import com.bayes.sdk.basic.util.BYUtil;
import com.bykv.vk.openvk.TTVfConfig;
import com.bykv.vk.openvk.TTVfConstant;
import com.bykv.vk.openvk.TTVfSdk;

import java.util.ArrayList;

public class DemoUtil {
    public static String TAG = "DemoUtil ";

    public static void logAndToast(String msg) {
        Log.d(TAG, msg);
        BYThreadUtil.switchMainThread(new BYBaseCallBack() {
            @Override
            public void call() {
                BYToast.showToast(TAG + msg);
            }
        });
    }

    public static void addTextLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    public static void openInWeb(String webUrl, String webText, ArrayList<AdvanceRFDownloadElement.AdvDownloadPermissionModel> pList) {
        Intent intent = new Intent();
        int pSzie = 0;
        if (pList != null) {
            pSzie = pList.size();
        }
        LogUtil.devDebug("webUrl = " + webUrl + ", webText = " + webText + "pList.size = " + pSzie);
        intent.putExtra("webUrl", webUrl);
        intent.putExtra("webText", webText);
        intent.putExtra("pList", pList);
        intent.setClass(MyApplication.getInstance(), DemoWebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);

    }


    public static void initCsjOP(Context context) {

        boolean supportMP = AdvanceConfig.getInstance().getSupportMultiProcess();
        int[] directDownloadNetworkType = AdvanceConfig.getInstance().getCsjDirectDownloadNetworkType();

        String csjappid = "5404211"; // demo appID
//        String csjappid = "5001121"; //自渲染 appid
        TTVfConfig.Builder ttBuilder = new TTVfConfig.Builder().appId(csjappid)
                .debug(BYUtil.isDebug()) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .appName(AdvanceConfig.getInstance().getAppName());
        try { //避免部分配置被突然移除，导致初始化异常
            ttBuilder //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                    .titleBarTheme(TTVfConstant.TITLE_BAR_THEME_LIGHT)
                    .allowShowNotify(true) //是否允许sdk展示通知栏提示
                    // .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                    .directDownloadNetworkType(directDownloadNetworkType) //允许直接下载的网络状态集合
                    .supportMultiProcess(supportMP) //是否支持多进程，true支持
//                    .customController(ttCustomController)
            ;
            //                    .asyncInit(true) //如果是主线程使用异步
        } catch (Throwable e) {
            e.printStackTrace();
        }
        final TTVfConfig config = ttBuilder.build();


        //模拟APP先进行穿山甲初始化
        if (!ADManager.getInstance().hasInit) {
            TTVfSdk.init(context, config);
            TTVfSdk.start(new TTVfSdk.Callback() {
                @Override
                public void success() {
                    LogUtil.d("CsjUtil out csj init success");
                    AdvanceCsjManager.outerInitSuccess();
                }

                @Override
                public void fail(int i, String s) {
                    LogUtil.d("CsjUtil out csj init failed");
                    AdvanceCsjManager.outerInitFailed(i, s);
                }
            });
            ADManager.getInstance().hasInit = true;

            AdvanceCsjManager.outerInitCalled();
        }
//        后面正常执行advance广告加载流程
    }
}
