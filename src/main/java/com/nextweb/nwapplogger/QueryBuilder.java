package com.nextweb.nwapplogger;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author nextweb
 * 
 */
class QueryBuilder {

	public static final QueryBuilder instance = new QueryBuilder();

	private QueryBuilder() {
	}

	public static QueryBuilder getInstance() {
		return instance;
	}

	String getURLQuery(Map<String, String> bindMap) {
		if (bindMap != null && bindMap.size() > 0) {
			return getURLQuery(makeValuePairs(bindMap));
		} else {
			return "";
		}
	}

	private List<BasicNameValuePair> makeValuePairs(Map<String, String> bindMap) {
		if (bindMap != null && bindMap.size() > 0) {
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

			Iterator<String> iter = bindMap.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				nameValuePairs.add(new BasicNameValuePair(key, bindMap.get(key)));
			}

			return nameValuePairs;
		} else {
			return null;
		}
	}

	private String getURLQuery(List<BasicNameValuePair> params) {
		if (params != null && params.size() > 0) {

			StringBuilder stringBuilder = new StringBuilder();
			// boolean first = true;

			for (BasicNameValuePair pair : params) {
				// if (first)
				// first = false;
				// else
				stringBuilder.append("&");

				try {
					stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
					stringBuilder.append("=");
					stringBuilder.append(URLEncoder.encode(pair.getValue() == null ? "" : pair.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			return stringBuilder.toString();
		} else {
			return "";
		}
	}
}
