package com.advance.advancesdkdemo.custom.nativeexpress;

import android.view.View;

public interface CustomExpressAdItem {
    void destroy();

    void render();

    View getExpressAdView();
}
