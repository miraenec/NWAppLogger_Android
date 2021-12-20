package com.nextweb.nwapplogger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;

import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

/**
 * @author nextweb
 *
 */
class DeviceInfoUtil {

	private static final DeviceInfoUtil instance = new DeviceInfoUtil();

	private DeviceInfoUtil() {
	}

	static DeviceInfoUtil getInstance() {
		return instance;
	}

	String getResolution(Activity act) {
		Display display = act.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		return width + "x" + height;
	}

	void getUniqueADID(final Activity act) {

		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(act);
					if (!advertisingIdInfo.isLimitAdTrackingEnabled()) {
						NWLogger.setVid(advertisingIdInfo.getId());
					}
				} catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
					e.printStackTrace();
				}
			}
		});
	}

	String getUniqueDeviceId(Activity act) {
		try {
			TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
			if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				return Constants.UnknownDevice;
			}

			String tmDevice = "" + tm.getDeviceId();

			try {
				String tmSerial = "" + tm.getSimSerialNumber();
				String androidId = android.provider.Settings.Secure.getString(act.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
				UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
				String uuid = deviceUuid.toString();

				// Log.d("getUniqueDeviceId", uuid);

				return uuid;
			} catch (NullPointerException ex) {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				digest.reset();

				return getHex(digest.digest(tmDevice.getBytes("UTF-8")));
			}
		} catch (Exception ex) {
			return Constants.UnknownDevice;
		}
	}

	boolean isNotUnknownDevice(Activity act) {
		String deviceId = getUniqueDeviceId(act);

		if (!(Constants.UnknownDevice.equals(deviceId) && "null".equalsIgnoreCase(deviceId) && deviceId == null)) {
			return true;
		} else {
			return false;
		}
	}

	String getApiKeyFromManifest(Context context) {
		String apiKey = "";

		try {
			if (context != null) {
				String packageName = context.getPackageName();
				ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
				Bundle bundle = ai.metaData;

				if (bundle != null) {
					apiKey = bundle.getString(Constants.XtractorKey);
				}
			}
		} catch (Exception e) {

			if (Constants.IS_DEBUG_MODE)
				Log.e("ApiKeyFromManifest", "Caught non-fatal exception while retrieving apiKey: " + e);
		}

		return apiKey;
	}

	String getMacAddress(Context context) {
		String macAddress = "";
		boolean bIsWifiOff = false;

		WifiManager wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (!wfManager.isWifiEnabled()) {
			// wfManager.setWifiEnabled(true);
			bIsWifiOff = true;
		}

		WifiInfo wfInfo = wfManager.getConnectionInfo();
		macAddress = wfInfo.getMacAddress();

		if (bIsWifiOff) {
			// wfManager.setWifiEnabled(false);
			bIsWifiOff = false;
		}

		return macAddress;
	}
	
	private String getHex(byte[] raw) {
		final StringBuilder hex = new StringBuilder(2 * raw.length);

		for (byte b : raw) {
			hex.append(Constants.HEXES.charAt((b & 0xF0) >> 4)).append(Constants.HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	String getCountry(Context context) {
		Locale systemLocale = context.getResources().getConfiguration().locale;
		String strCountry = systemLocale.getCountry();

		return strCountry;
	}

	String getLanguage(Context context) {
		Locale systemLocale = context.getResources().getConfiguration().locale;
		String strLanguage = systemLocale.getLanguage();

		return strLanguage;
	}

	String getCarrier(Context context) {
		TelephonyManager telephonyManager =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
		String operatorName = telephonyManager.getNetworkOperatorName();

		return operatorName;
	}
}
