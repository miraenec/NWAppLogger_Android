package com.nextweb.nwapplogger;

import android.os.Build;

/**
 * @author nextweb
 *
 */
class AndroidDevice {
	public static String BUILD_BOARD = Build.BOARD;
	public static String BUILD_BRAND = Build.BRAND;
	public static String BUILD_DEVICE = Build.DEVICE;
	public static String BUILD_DISPLAY = Build.DISPLAY;
	public static String BUILD_FINGERPRINT = Build.FINGERPRINT;
	public static String BUILD_HOST = Build.HOST;
	public static String BUILD_ID = Build.ID;
	public static String BUILD_MANUFACTURER = Build.MANUFACTURER;
	public static String BUILD_MODEL = Build.MODEL;
	public static String BUILD_PRODUCT = Build.PRODUCT;
	public static String BUILD_SERIAL = Build.SERIAL;
	public static String BUILD_TAGS = Build.TAGS;
	public static String BUILD_TIME = String.valueOf(Build.TIME);
	public static String BUILD_TYPE = Build.TYPE;
	public static String BUILD_USER = Build.USER;
	public static String BUILD_VERSION_RELEASE = Build.VERSION.RELEASE;
	public static String BUILD_VERSION_SDK_INT = String.valueOf(Build.VERSION.SDK_INT);
	public static String BUILD_VERSION_CODENAME = Build.VERSION.CODENAME;
	public static String BUILD_HARDWARE = Build.HARDWARE;
}
