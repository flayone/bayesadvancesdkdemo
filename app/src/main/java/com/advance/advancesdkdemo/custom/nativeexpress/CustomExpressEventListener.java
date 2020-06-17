package com.advance.advancesdkdemo.custom.nativeexpress;

import android.view.View;

import java.util.List;

public interface CustomExpressEventListener {
    void onADLoaded(List<CustomExpressAdItem> list);

    void onADRenderFailed(View view);

    void onADClosed(View view);
}
