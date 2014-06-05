package org.cloudfoundry.client.compat;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.cloudfoundry.client.compat.util.Assert;
import org.cloudfoundry.client.compat.util.Utils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Stephen Kruger.
 * 
 */
public class OAuth2AccessToken extends ResponseObject {
	private static Logger log = Logger.getAnonymousLogger();
	public enum Fields {access_token, expires_in,scope, jti, target};

	public OAuth2AccessToken(InputStream is, String targetUrl) throws JSONException {
		super(is);
		put(Fields.target.name(), targetUrl);
	}

	public OAuth2AccessToken(JSONObject jo, String targetUrl) throws JSONException {
		super(jo.toString());
		Assert.notNull(targetUrl, "Target url is not set");
		put(Fields.target.name(), targetUrl);
	}

	public OAuth2AccessToken(String token, String targetUrl) throws JSONException {
		super();
		Assert.notNull(token, "Authentication token is not set");
		put(Fields.access_token.name(),token);
		Assert.notNull(targetUrl, "Target url is not set");
		put(Fields.target.name(), targetUrl);
	}

	public static OAuth2AccessToken getLoginResponse(String targetUrl, CloudInfo info, CloudCredentials credentials) throws CloudFoundryException {
		log.info("Requesting authentication token from "+targetUrl+"/oauth/token"+" with targetUrl="+targetUrl);	
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Authorization", "Basic Y2Y6");
		try {
			StringEntity body = new StringEntity("grant_type=password&username="+Utils.safeEncode(credentials.getEmail())+"&password="+Utils.safeEncode(credentials.getPassword()));
			JSONObject ro = ResponseObject.postResponsObject(new URL(info.getAuthorizationEndpoint()+"/oauth/token").toURI(),headers,body);
			return new OAuth2AccessToken(ro, targetUrl);
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST,t.getMessage());
		}
	}

	public String getValue() {
		try {
			return getString(Fields.access_token.name());
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
