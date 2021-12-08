package com.nextweb.nwapplogger;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

  private LruBitmapCache(final int maxSize) {
    super(maxSize);
  }

  public LruBitmapCache(@NonNull final Context ctx) {
    this(getCacheSize(ctx));
  }

  private static int getCacheSize(@NonNull final Context context) {
    final DisplayMetrics displayMetrics = context.getResources().
        getDisplayMetrics();
    final int screenWidth = displayMetrics.widthPixels;
    final int screenHeight = displayMetrics.heightPixels;
    // 4 bytes per pixel
    final int screenBytes = screenWidth * screenHeight * 4;

    return screenBytes * 3;
  }

  @Override
  protected int sizeOf(String key, Bitmap value) {
    return value.getRowBytes() * value.getHeight();
  }

  @Override
  public Bitmap getBitmap(String url) {
    return get(url);
  }

  @Override
  public void putBitmap(String url, Bitmap bitmap) {
    put(url, bitmap);
  }
}
