package com.nextweb.nwapplogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class NetworkUtil {

	private static final NetworkUtil instance = new NetworkUtil();

	private NetworkUtil() {
	}

	static NetworkUtil getInstance() {
		return instance;
	}

	void sendHttp(final String urlStr, final DbOpenHelper mDbOpenHelper) {
		try {
			final URL url = new URL(urlStr);
			url.openConnection();

			new Thread(new Runnable() {
				@SuppressLint("LongLogTag")
				public void run() {
					try {
						url.openStream();

						if (Constants.IS_DEBUG_MODE)
							Log.i("sendHttp send", "(" + urlStr.length() + " bytes)" + urlStr);
					} catch (IOException ex) {
						// ex.printStackTrace();

						if (Constants.IS_DEBUG_MODE)
							Log.e("openConnection thread error", " : " + ex.toString() + " (" + urlStr.length() + " bytes)" + urlStr);
					}
				}
			}).start();
		} catch (Exception ex) {
			// ex.printStackTrace();

			if (Constants.IS_DEBUG_MODE)
				Log.e("sendHttp error", " : " + ex.toString() + " (" + urlStr.length() + " bytes)" + urlStr);
		}
	}

	@SuppressLint("LongLogTag")
	void sendHttps(final Context context, final String urlStr, final DbOpenHelper mDbOpenHelper) {
		try {
			trustAllHosts();

//			for (String ddd : params.keySet()) {
//				Log.d("", ddd);
//			}

			final Uri uri = Uri.parse(urlStr);

			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");

			String query = uri.getQuery();
			final Map<String, String> params = getQueryMap(query);
			Set<String> keys = params.keySet();
			for (String key : keys)
			{
				System.out.println("Name=" + key);
				System.out.println("Value=" + params.get(key));
				if (Constants.IS_DEBUG_MODE) {
					Log.d("NWAppUsageLogger", key + " " + params.get(key));
				}
			}

			final String id = uri.getQueryParameter("id");

			Response.Listener<JSONObject> listner = new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {

					if (Constants.IS_DEBUG_MODE) {
						Log.d("NetworkUtil", "Response : " + response);
					}
				}
			};

			Response.ErrorListener errorListner = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

					if (Constants.IS_DEBUG_MODE) {
						Log.i("NetworkUtil", "error " + error.getMessage());
					}

				}
			};

			String url = "";
			String[] urls = urlStr.split("\\?");
			if (urls.length > 1) {
				url = urls[0];
			}

			InputStreamRequest jsonRequest = new InputStreamRequest(Request.Method.POST,
					url, headers, params, errorListner, listner);

			App.addRequest(context, jsonRequest, id);

		} catch (Exception e) {

			if (Constants.IS_DEBUG_MODE)
				Log.e("sendHttps IOException error"," error : " + e.toString() + "(" + urlStr.length() + " bytes)" + urlStr);
		}
	}

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params)
		{
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	boolean isNetworkConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
		boolean blte_4g = false;

		if (lte_4g != null)
			blte_4g = lte_4g.isConnected();

		if (mobile != null) {
			if (mobile.isConnected() || wifi.isConnected() || blte_4g)
				return true;
		} else {
			if (wifi.isConnected() || blte_4g)
				return true;
		}

		return false;
	}

	@SuppressLint("LongLogTag")
	private void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain,
					String authType)
					throws java.security.cert.CertificateException {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain,
					String authType)
					throws java.security.cert.CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			if (Constants.IS_DEBUG_MODE) {
				Log.e("trustAllHosts Exception error", e.getMessage());
			}
		}
	}

	private JSONObject getJsonStringFromMap(Map<String, String> map) {

		JSONObject json = new JSONObject();
		try {
			json.put("title", "test");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;

	}
}
