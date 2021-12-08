package com.nextweb.nwapplogger;

import android.app.Activity;

import java.util.Map;

/**
 * Created by nextweb on 2018. 1. 10..
 */

public abstract class INWAppLogger {
    public abstract void init(Activity act, Boolean isLog, String url);
    public abstract void setParameters(String type, String userId, String sessionId);
    public abstract void start(Activity act);
    public abstract void clear(Activity act);
    public abstract void resume(Activity act);
    public abstract void pause(Activity act);
    public abstract void sendLog(Activity act, String buildStr);
    public abstract void sendLog(Activity act, Map<String, String> params, String buildStr);

    public abstract void setUserId(String uid);
    public abstract void setSessionId(String ssid);
    public abstract void setPrintLog(boolean isPrint);
}
