package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.advance.AdvanceAD;
import com.bayescom.admore.core.AMError;
import com.bayescom.admore.reward.AdMoreReward;
import com.bayescom.admore.reward.AdMoreRewardListener;

public class AdMoreRewardActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);

        AdMoreReward reward = new AdMoreReward(this, Constants.TestIds.adMoreRewardAdspotId, new AdMoreRewardListener() {
            @Override
            public void onVideoCached() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onVideoCached");

            }

            @Override
            public void onVideoComplete() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onVideoComplete");

            }

            @Override
            public void onVideoSkip() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onVideoSkip");

            }

            @Override
            public void onAdClose() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onAdClose");

            }

            @Override
            public void onAdReward() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onAdReward");

            }

            @Override
            public void onSuccess() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onSuccess");

            }

            @Override
            public void onShow() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onShow");

            }

            @Override
            public void onClick() {
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, " onClick");

            }

            @Override
            public void onFailed(AMError amError) {
                String eMsg = "";
                if (amError != null) {
                    eMsg = amError.toString();
                }
                AdvanceAD.logAndToast(AdMoreRewardActivity.this, "onFailed,eMsg = " + eMsg);
            }
        });
        reward.loadAndShow();
    }
}
