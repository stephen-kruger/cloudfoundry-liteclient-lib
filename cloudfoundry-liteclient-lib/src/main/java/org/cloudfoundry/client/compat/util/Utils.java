package org.cloudfoundry.client.compat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONObject;

public class Utils {

	public static String safeGet(JSONObject jo, String key) {
		if (jo.has(key)) {
			if (!jo.isNull(key))
				return jo.getString(key);
			else
				return "";
		}
		else {
			// this will throw an exception, which is what we want
			return jo.getString(key);
		}
	}
	
	public static String safeGet(JSONObject jo, String key, String defaultValue) {
		if (jo.has(key)) {
			if (!jo.isNull(key))
				return jo.getString(key);
			else
				return defaultValue;
		}
		else {
			// this will throw an exception, which is what we want
			return defaultValue;
		}
	}

	public static String safeEncode(String s) {
		try {
			return URLEncoder.encode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	public static String safeDecode(String s) {
		try {
			return URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}
}
