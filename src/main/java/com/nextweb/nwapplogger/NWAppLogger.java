package com.nextweb.nwapplogger;

import android.app.Activity;

import java.util.Map;

/**
 * @author nextweb
 * 
 */
public class NWAppLogger extends NWLogger {

	private static volatile NWAppLogger uniqueInstance;

	private NWAppLogger() {
		super();
	}

	public static NWAppLogger getInstance() {
		if (uniqueInstance == null) {
			Class var0 = NWAppLogger.class;
			synchronized(NWAppLogger.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new NWAppLogger();
				}
			}
		}

		return uniqueInstance;
	}

	@Override
	public void init(Activity act, Boolean isLog, String url) {
		super.init(act, isLog, url);
	}

	@Override
	public void setParameters(String type, String userId, String sessionId) {
		super.setParameters(type, userId, sessionId);
	}

	@Override
	public void start(Activity act) {
		super.start(act);
	}

	@Override
	public void clear(Activity act) {
		super.clear(act);
	}

	@Override
	public void resume(Activity act) {
		super.resume(act);
	}

	@Override
	public void pause(Activity act) {
		super.pause(act);
	}

	@Override
	public void sendLog(Activity act, String buildStr) {
		super.sendLog(act, buildStr);
	}

	@Override
	public void sendLog(Activity act, Map<String, String> params, String buildStr) {
		super.sendLog(act, params, buildStr);
	}

	@Override
	public void setUserId(String uid) {
		super.setUserId(uid);
	}

	@Override
	public void setSessionId(String ssid) {
		super.setSessionId(ssid);
	}

	@Override
	public void setPrintLog(boolean isPrint) {
		super.setPrintLog(isPrint);
	}
}
