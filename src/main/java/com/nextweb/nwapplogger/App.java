package com.nextweb.nwapplogger;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class App extends Application {

  private volatile static App sInstance;
  private RequestQueue mRequestQueue;
  private LruBitmapCache mLruBitmapCache;
  private ImageLoader mImageLoader;
  private static Context mContext;

  protected App() {}

  public static App getInstance() {
    if (sInstance == null) {
      synchronized(App.class) {
        if (sInstance == null) {
          sInstance = new App();
        }
      }
    }
    return sInstance;
  }

  private static void addRequest(@NonNull final Request<?> request) {
    getInstance().getVolleyRequestQueue().add(request);
  }

  public static void addRequest(@NonNull final Context context, @NonNull final Request<?> request, @NonNull final String tag) {
    mContext = context;
    request.setTag(tag);
    addRequest(request);
  }

  public static void cancelAllRequests(@NonNull final String tag) {
    getInstance().getVolleyRequestQueue().cancelAll(tag);
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @NonNull
  public RequestQueue getVolleyRequestQueue() {

    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(mContext, new OkHttp3Stack());
    }

    return mRequestQueue;
  }

  @NonNull
  public ImageLoader getVolleyImageLoader() {
    if (mImageLoader == null) {
      mImageLoader = new ImageLoader
          (
              getVolleyRequestQueue(),
              App.getInstance().getVolleyImageCache()
          );
    }

    return mImageLoader;
  }

  @NonNull
  private LruBitmapCache getVolleyImageCache() {
    if (mLruBitmapCache == null) {
      mLruBitmapCache = new LruBitmapCache(sInstance);
    }

    return mLruBitmapCache;
  }

}
