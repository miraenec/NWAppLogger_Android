//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.nextweb.nwapplogger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.nextweb.nwapplogger.LogBuilders.EventLogBuilder;

import java.util.HashMap;
import java.util.Map;

public class NWLogger extends INWAppLogger {

    private Context mContext;
    private static String HTTP_URL = "";
    private static String xtUid = "";
    private static String xtVid = "";
    private static String xtDid = "";
    private static String ssId = "";
    public static boolean isDebug = false;
    private static final DbOpenHelper mDbOpenHelper = DbOpenHelper.getInstance();
    private static final NetworkUtil networkUtil = NetworkUtil.getInstance();
    private static final DeviceInfoUtil deviceInfoUtil = DeviceInfoUtil.getInstance();
    private static final AppInfoUtil appInfoUtil = AppInfoUtil.getInstance();

    private InstallReferrerClient referrerClient;

    public interface OnGoogleReferrerDataCallback {
        void onGoogleReferrerData(boolean result);
    }
    private OnGoogleReferrerDataCallback mCallback;
    private Handler mHandler;
    private Runnable mRunnable;

    protected NWLogger() {
    }

    private void initDefaultInfo(Activity act) {
        this.mContext = act;

        if (xtDid == null || "".equalsIgnoreCase(xtDid)) {
            xtDid = deviceInfoUtil.getUniqueDeviceId(act);
        }
    }

    private void tryToConnectReferrer(Activity act) {
        // 리퍼러 연결
        referrerClient = InstallReferrerClient.newBuilder(act).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {

                mHandler.removeCallbacks(mRunnable);

                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:

                        /**
                         *
                         * 구글플레이 앱과 연결이 성공했을 때에 구글 인스톨 리퍼러 데이터를 얻어 오기 위한 작업을 수행합니다.
                         *
                         * */
                        try {
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            SharedPreferences pref = mContext.getSharedPreferences("pref", mContext.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("referrer", referrerUrl);
                            editor.commit();

                            referrerClient.endConnection();

                            if (mCallback != null) {
                                mCallback.onGoogleReferrerData(true);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection could'nt be established
                        if (mCallback != null) {
                            mCallback.onGoogleReferrerData(false);
                        }
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Google Play by calling the startConnection() method.
            }
        });
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onGoogleReferrerData(false);
                }
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void sendTempLogs(String protocol) {
        Cursor cursor = mDbOpenHelper.selectAll();
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String urlStr = cursor.getString(1);
                String updateDt = cursor.getString(2);
                mDbOpenHelper.delete(id);
                if ("HTTPS".equals(protocol)) {
                    networkUtil.sendHttps((Context)null, urlStr, mDbOpenHelper);
                } else {
                    networkUtil.sendHttp(urlStr, mDbOpenHelper);
                }
            }
        }

    }

    private void sendAppStartLog(Activity act) {
        this.sendLog(act, (new EventLogBuilder()).setEvent("APP_START", "onCreate").build());
    }

    private void sendAppEndLog(Activity act) {
        this.sendLog(act, (new EventLogBuilder()).setEvent("APP_FINISH", "onDestroy").build());
    }

    private void sendAppOnPause(Activity act) {
        this.sendLog(act, (new EventLogBuilder()).setEvent("APP_PAUSE", "onPause").build());
    }

    private void sendAppOnResume(Activity act) {
        this.sendLog(act, (new EventLogBuilder()).setEvent("APP_RESUME", "onResume").build());
    }

    private void send(Activity act, String params, String protocol) {

        mContext = act;

        if (deviceInfoUtil.isNotUnknownDevice(act) && params != null) {
            String urlStr = HTTP_URL + "?" + "ct" + "=" + System.currentTimeMillis() + "&st" + "=" + System.currentTimeMillis() + params;
            if ("HTTPS".equals(protocol)) {
                networkUtil.sendHttps(act, urlStr, mDbOpenHelper);
            } else {
                networkUtil.sendHttp(urlStr, mDbOpenHelper);
            }
        }
    }

    private String getDefaultParams(Activity act) {

        if (mContext == null) {
            mContext = act;
        }

        Map<String, String> bindMap = new HashMap();
        bindMap.put("osType", "android");

        bindMap.put("appName", appInfoUtil.getApplicationName(mContext));
        bindMap.put("activityName", appInfoUtil.getActivityName(mContext));
        bindMap.put("packageName", mContext.getPackageName());

        if (act != null) {
            bindMap.put("resolution", deviceInfoUtil.getResolution(act));
            bindMap.put("className", act.getLocalClassName());
        }
        bindMap.put("xtVid", xtVid);
        bindMap.put("xtDid", xtDid);
        bindMap.put("xtUid", xtUid);
        bindMap.put("ssId", ssId);
        bindMap.put("macAddress", deviceInfoUtil.getMacAddress(mContext));
        bindMap.put("appRunCount", appInfoUtil.getAppRunCount(mContext));
        bindMap.put("referrer", appInfoUtil.getReferrer(mContext));
        bindMap.put("buildBoard", AndroidDevice.BUILD_BOARD);
        bindMap.put("buildBrand", AndroidDevice.BUILD_BRAND);
        bindMap.put("buildDevice", AndroidDevice.BUILD_DEVICE);
        bindMap.put("buildDisplay", AndroidDevice.BUILD_DISPLAY);
        bindMap.put("buildFingerprint", AndroidDevice.BUILD_FINGERPRINT);
        bindMap.put("buildHost", AndroidDevice.BUILD_HOST);
        bindMap.put("buildId", AndroidDevice.BUILD_ID);
        bindMap.put("buildManufacturer", AndroidDevice.BUILD_MANUFACTURER);
        bindMap.put("buildModel", AndroidDevice.BUILD_MODEL);
        bindMap.put("buildProduct", AndroidDevice.BUILD_PRODUCT);
        bindMap.put("buildSerial", AndroidDevice.BUILD_SERIAL);
        bindMap.put("buildTags", AndroidDevice.BUILD_TAGS);
        bindMap.put("buildTime", AndroidDevice.BUILD_TIME);
        bindMap.put("buildType", AndroidDevice.BUILD_TYPE);
        bindMap.put("buildUser", AndroidDevice.BUILD_USER);
        bindMap.put("buildVersionRelease", AndroidDevice.BUILD_VERSION_RELEASE);
        bindMap.put("buildVersionSdkInt", AndroidDevice.BUILD_VERSION_SDK_INT);
        bindMap.put("buildVersionCodename", AndroidDevice.BUILD_VERSION_CODENAME);
        bindMap.put("buildHardware", AndroidDevice.BUILD_HARDWARE);
        bindMap.put("countryCd", deviceInfoUtil.getCountry(mContext));
        bindMap.put("language", deviceInfoUtil.getLanguage(mContext));
        bindMap.put("carrier", deviceInfoUtil.getCarrier(mContext));

        return LogBuilders.build(bindMap);
    }

    private Map<String, String> getDefaultMaps(Activity act) {
        Map<String, String> bindMap = new HashMap();
        bindMap.put("osType", "android");

        bindMap.put("appName", appInfoUtil.getApplicationName(mContext));
        bindMap.put("activityName", appInfoUtil.getActivityName(mContext));
        bindMap.put("packageName", mContext.getPackageName());

        if (act != null) {

            bindMap.put("resolution", deviceInfoUtil.getResolution(act));
            bindMap.put("className", act.getLocalClassName());
        }
        bindMap.put("xtVid", xtVid);
        bindMap.put("xtDid", xtDid);
        bindMap.put("xtUid", xtUid);
        bindMap.put("ssId", ssId);
        bindMap.put("macAddress", deviceInfoUtil.getMacAddress(mContext));
        bindMap.put("appRunCount", appInfoUtil.getAppRunCount(mContext));
        bindMap.put("referrer", appInfoUtil.getReferrer(mContext));
        bindMap.put("buildBoard", AndroidDevice.BUILD_BOARD);
        bindMap.put("buildBrand", AndroidDevice.BUILD_BRAND);
        bindMap.put("buildDevice", AndroidDevice.BUILD_DEVICE);
        bindMap.put("buildDisplay", AndroidDevice.BUILD_DISPLAY);
        bindMap.put("buildFingerprint", AndroidDevice.BUILD_FINGERPRINT);
        bindMap.put("buildHost", AndroidDevice.BUILD_HOST);
        bindMap.put("buildId", AndroidDevice.BUILD_ID);
        bindMap.put("buildManufacturer", AndroidDevice.BUILD_MANUFACTURER);
        bindMap.put("buildModel", AndroidDevice.BUILD_MODEL);
        bindMap.put("buildProduct", AndroidDevice.BUILD_PRODUCT);
        bindMap.put("buildSerial", AndroidDevice.BUILD_SERIAL);
        bindMap.put("buildTags", AndroidDevice.BUILD_TAGS);
        bindMap.put("buildTime", AndroidDevice.BUILD_TIME);
        bindMap.put("buildType", AndroidDevice.BUILD_TYPE);
        bindMap.put("buildUser", AndroidDevice.BUILD_USER);
        bindMap.put("buildVersionRelease", AndroidDevice.BUILD_VERSION_RELEASE);
        bindMap.put("buildVersionSdkInt", AndroidDevice.BUILD_VERSION_SDK_INT);
        bindMap.put("buildVersionCodename", AndroidDevice.BUILD_VERSION_CODENAME);
        bindMap.put("buildHardware", AndroidDevice.BUILD_HARDWARE);
        bindMap.put("countryCd", deviceInfoUtil.getCountry(mContext));
        bindMap.put("language", deviceInfoUtil.getLanguage(mContext));
        bindMap.put("carrier", deviceInfoUtil.getCarrier(mContext));

        return bindMap;
    }

    public void init(final Activity act, final Boolean isDebug, final String url) {
        if (act != null) {

            this.isDebug = isDebug;
            this.HTTP_URL = url;

            try {
                if (act.getClass().getName().contains("OnGoogleReferrerDataCallback")) {
                    mCallback = (OnGoogleReferrerDataCallback) act;
                }
            } catch (Error e) {

            }

            // 앱 실행 횟수
            appInfoUtil.addAppRunCount(act);

            this.initDefaultInfo(act);

            // 리퍼러 정보 가져오기
            tryToConnectReferrer(act);

            if (mDbOpenHelper != null && mDbOpenHelper.open(act) != null) {
                this.sendTempLogs("HTTP");
            }
        }
    }

    public void setParameters(String type, String userId, String sessionId) {
        if ("0".equals(type)) {
            this.isDebug = true;
        } else {
            this.isDebug = false;
        }

        xtUid = userId;
        ssId = sessionId;
    }

    public void start(Activity act) {

        this.initDefaultInfo(act);
        this.sendAppStartLog(act);
    }

    public void clear(Activity act) {
        this.initDefaultInfo(act);
        this.sendAppEndLog(act);
        mDbOpenHelper.close();
    }

    public void resume(final Activity act) {
        this.initDefaultInfo(act);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                NWLogger.this.sendAppOnResume(act);
            }
        }, 1000L);
    }

    public void pause(Activity act) {
        this.initDefaultInfo(act);
        this.sendAppOnPause(act);
    }

    public void sendLog(Activity act, String buildStr) {
        this.send(act, this.getDefaultParams(act) + buildStr, "HTTP");
    }

    public void sendLog(Activity act, Map<String, String > params, String buildStr) {
        this.send(act, this.getDefaultParams(act) + LogBuilders.build(params) + buildStr, "HTTP");
    }

    public void setUserId(String uid) {
        xtUid = uid;
    }

    public void setSessionId(String ssid) {
        ssId = ssid;
    }

    public static void setVid(String vid) {
        xtVid = vid;
    }

    public void setPrintLog(boolean isPrint) {
        isDebug = isPrint;
    }
}
