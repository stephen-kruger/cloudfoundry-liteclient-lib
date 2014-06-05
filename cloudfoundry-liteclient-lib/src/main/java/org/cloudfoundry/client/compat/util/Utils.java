package org.cloudfoundry.client.compat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Utils {
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
