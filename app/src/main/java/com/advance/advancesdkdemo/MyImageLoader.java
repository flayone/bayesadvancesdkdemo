package com.advance.advancesdkdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alimm.tanx.ui.image.ILoader;
import com.alimm.tanx.ui.image.ImageConfig;
import com.alimm.tanx.ui.image.ImageConfig.ImageBitmapCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Glide自定义实现tanx 广告图片加载
 */
public class MyImageLoader implements ILoader {
    @Override
    public void load(ImageConfig config, final ImageBitmapCallback callback) {

        Glide.with(config.getContext())
                .asBitmap()
                .load(config.getUrl())
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (callback != null) {
                            callback.onFailure("图片加载失败");
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (callback != null) {
                            callback.onSuccess(resource);
                        }
                    }

                });
    }
}