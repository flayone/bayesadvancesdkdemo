package com.advance.advancesdkdemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.huawei.hms.ads.HwAds;
import com.mercury.sdk.core.config.MercuryAD;

public class UserPrivacyDialog extends Dialog {
    public UserPrivacyDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.dialog_user_privacy);

        TextView cont = findViewById(R.id.tv_dup_content);
        cont.setText("请你务必审慎阅读、充分理解“服务协议和隐私政策”个条款，包括但不限于：为了向你提供内容等服务，我们需要收集你的设备信息、操作日志等个人信息。你可以在“设置”中查看、变更、删除个人信息并管理你的授权。你可以阅读《隐私政策》了解详细信息。如你同意，请点击“同意”开始接受我们的服务。");

        TextView y = findViewById(R.id.tv_dup_yes);
        TextView n = findViewById(R.id.tv_dup_no);

        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("preference", Context.MODE_PRIVATE).edit().putBoolean("agree_privacy",true).commit();
                /**SDK 初始化，！！！注意：要在用户同意APP隐私政策要求后再调用此初始化方法。*/
                //初始化聚合SDK
                AdvanceAD.initAD(context);
                //初始化HUAWEI Ads SDK，用于自定义SDK渠道
                HwAds.init(context);
                dismiss();
            }
        });

        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                System.exit(0);
            }
        });

        setCanceledOnTouchOutside(false);
        setCancelable(false);

    }
}
