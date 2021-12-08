package com.nextweb.nwapplogger;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

class AppInfoUtil {
	private static final AppInfoUtil instance = new AppInfoUtil();

	private AppInfoUtil() {
	}

	static AppInfoUtil getInstance() {
		return instance;
	}

	String getApplicationName(Context ctx) {
		int stringId = ctx.getApplicationInfo().labelRes;
		String appName = ctx.getString(stringId);

		if (appName != null) {
			return appName;
		} else {
			return "";
		}
	}

	String getActivityName(Context ctx) {

		try {
			String label = null;
			ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

			if (Constants.IS_DEBUG_MODE) {
				Log.d("topActivity", "CURRENT Activity ::"
						+ taskInfo.get(0).topActivity.getClassName());
			}
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			label = componentInfo.getClassName();

			if (label != null) {
				return label;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	String getAppRunCount(Context ctx) {

		SharedPreferences pref = ctx.getSharedPreferences("pref", ctx.MODE_PRIVATE);
		int nVal = pref.getInt("appRunCount", 0);

		return String.valueOf(nVal);
	}

	void addAppRunCount(Context ctx) {

		SharedPreferences pref = ctx.getSharedPreferences("pref", ctx.MODE_PRIVATE);
		int nVal = pref.getInt("appRunCount", 0);
		nVal += 1;

		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("appRunCount", nVal);
		editor.commit();
	}

	String getReferrer(Context ctx) {

		SharedPreferences pref = ctx.getSharedPreferences("pref", ctx.MODE_PRIVATE);
		String sVal = pref.getString("referrer", "");

		return sVal;
	}
}
