package org.cloudfoundry.client.ibmlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Stephen Kruger.
 * 
 */
public class ResponseObject extends JSONObject {
	private static Logger log = Logger.getAnonymousLogger();
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	protected ResponseObject() {

	}

	protected ResponseObject(String jsonStr) throws JSONException {
		super(jsonStr);
	}

	public ResponseObject(InputStream is) throws JSONException {
		super(streamToString(is));
	}

	private static String streamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		String result =  s.hasNext() ? s.next() : "";
		try {
			s.close();
			is.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected static URI getTargetAction(URL target,String rest) throws MalformedURLException, URISyntaxException {
		return new URL(target.toString()+"/"+rest).toURI();
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> convertJsonToMap() throws JSONException {
		Iterator keys = this.keys();
		Map<String, Object> result = new HashMap<String, Object>();
		String key;
		while (keys.hasNext()) {
			key = keys.next().toString();
			result.put(key, getString(key));
		}
		return result;
	}

	public static void deleteResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken) throws CloudFoundryException {
		HttpDelete request = new HttpDelete();

		try {
			request.setURI(new URL(oauth2AccessToken.getString(OAuth2AccessToken.Fields.target.name())+urlOffset).toURI());
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "URI Error",e.getMessage());
		}

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		request.setHeader("Authorization", "bearer "+oauth2AccessToken.getString(OAuth2AccessToken.Fields.access_token.name()));
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);	
//			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				throw new CloudFoundryException(response.getStatusLine().getStatusCode(), "Client Error",response.getStatusLine().getReasonPhrase());
			}
			//ResponseObject ro = new ResponseObject(entity.getContent());
		}
		catch (IOException ioe) {
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "IOException",ioe.getMessage());
		}
		
	}
	/*
	 * This method uses the existing oauth token to authenticate the request.
	 */
	public static ResponseObject getResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken) throws CloudFoundryException {
		HttpGet request = new HttpGet();

		try {
			request.setURI(new URL(oauth2AccessToken.getString(OAuth2AccessToken.Fields.target.name())+urlOffset).toURI());
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "URI Error",e.getMessage());
		}

		log.info(request.getURI().toString());
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		request.setHeader("Authorization", "bearer "+oauth2AccessToken.getString(OAuth2AccessToken.Fields.access_token.name()));
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);	
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				throw new CloudFoundryException(response.getStatusLine().getStatusCode(), "Client Error",response.getStatusLine().getReasonPhrase());
			}
			ResponseObject ro = new ResponseObject(entity.getContent());
			return ro;
		}
		catch (IOException ioe) {
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "IOException",ioe.getMessage());
		}
	}

	public static String getResponsObjectAsString(String urlOffset, OAuth2AccessToken oauth2AccessToken) throws CloudFoundryException {
		HttpGet request = new HttpGet();

		try {
			request.setURI(new URL(oauth2AccessToken.getString(OAuth2AccessToken.Fields.target.name())+urlOffset).toURI());
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "URI Error",e.getMessage());
		}

		log.info(request.getURI().toString());
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		request.setHeader("Authorization", "bearer "+oauth2AccessToken.getString(OAuth2AccessToken.Fields.access_token.name()));
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);	
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				throw new CloudFoundryException(response.getStatusLine().getStatusCode(), "Client Error",response.getStatusLine().getReasonPhrase());
			}
			return streamToString(entity.getContent());
		}
		catch (IOException ioe) {
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "IOException",ioe.getMessage());
		}
	}

	/*
	 * This method has no authentication, and is used for the intitial login request. The uri is the full uri, not just the offset.
	 */
	public static JSONObject postResponsObject(URI uri, Map<String, String> headers, StringEntity body) throws ClientProtocolException, IOException, IllegalStateException, JSONException {
		HttpPost request = new HttpPost();
		request.setURI(uri); 
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		for (String key : headers.keySet()) {
			request.setHeader(key, headers.get(key));			
		}
		request.setEntity(body);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);	
		HttpEntity entity = response.getEntity();
		if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
			throw new ClientProtocolException(response.getStatusLine().getReasonPhrase());
		}
		return new ResponseObject(entity.getContent());
	}

	public static ResponseObject postResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken, Map<String, String> headers, Map<String,Object> body) throws CloudFoundryException {
		return postResponsObject(urlOffset, oauth2AccessToken, headers, getStringEntity(body));
	}

	public static ResponseObject putResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken, Map<String, String> headers, Map<String,Object> body) throws ClientProtocolException, IOException, URISyntaxException, JSONException {
		return putResponsObject(urlOffset, oauth2AccessToken, headers, getStringEntity(body));
	}

	public static ResponseObject putResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken, Map<String, String> headers, StringEntity body) throws ClientProtocolException, IOException, URISyntaxException, JSONException {
		HttpPut request = new HttpPut();
		request.setURI(new URL(oauth2AccessToken.getString(OAuth2AccessToken.Fields.target.name())+urlOffset).toURI()); 
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		request.setHeader("Authorization", "bearer "+oauth2AccessToken.getString(OAuth2AccessToken.Fields.access_token.name()));

		if (headers!=null) {
			for (String key : headers.keySet()) {
				request.setHeader(key, headers.get(key));			
			}
		}

		request.setEntity(body);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);	
		HttpEntity entity = response.getEntity();
		int returnCode = response.getStatusLine().getStatusCode();
		if (returnCode!=HttpStatus.SC_CREATED) {
			throw new ClientProtocolException(response.getStatusLine().getReasonPhrase());
		}
		// copy the headers into the response object in case caller needs them, some silly
		// api contract thought this is a good way to communicate information.
		Header[] rheaders = response.getAllHeaders();
		JSONObject reponseHeaders = new JSONObject();
		for (int i = 0; i < rheaders.length; i++) {
			reponseHeaders.put(rheaders[i].getName(), rheaders[i].getValue());
		}
		ResponseObject ro = new ResponseObject(entity.getContent());
		ro.put("headers", reponseHeaders);
		return ro;
	}

	/*
	 * This method performs an authenticated POST request using the existing ouath token.
	 */
	public static ResponseObject postResponsObject(String urlOffset, OAuth2AccessToken oauth2AccessToken, Map<String, String> headers, StringEntity body) throws CloudFoundryException {
		HttpPost request = new HttpPost();
		try {
			request.setURI(new URL(oauth2AccessToken.getString(OAuth2AccessToken.Fields.target.name())+urlOffset).toURI());
		} 
		catch (Throwable e) {
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "URI Error",e.getMessage());
		} 
		log.info("Posting to "+request.getURI());
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Accept", "application/json;charset=utf-8");
		request.setHeader("Authorization", "bearer "+oauth2AccessToken.getString(OAuth2AccessToken.Fields.access_token.name()));
		if (headers!=null) {
			for (String key : headers.keySet()) {
				request.setHeader(key, headers.get(key));			
			}
		}
		request.setEntity(body);
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);	
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				throw new ClientProtocolException(response.getStatusLine().getReasonPhrase());
			}
			return new ResponseObject(entity.getContent());
		}
		catch (IOException ioe) {
			throw new CloudFoundryException(HttpStatus.SC_BAD_REQUEST, "IOException",ioe.getMessage());
		}
	}

	public static JSONArray getResources(String urlOffset, OAuth2AccessToken oauth2AccessToken) throws CloudFoundryException {
		ResponseObject ro = getResponsObject(urlOffset,oauth2AccessToken);
		JSONArray result = ro.getJSONArray("resources");
		while (!ro.isNull("next_url")) {
			JSONArray next = getResources(ro.getString("next_url"),oauth2AccessToken);
			for (int i = 0; i < next.length();i++) {
				result.put(next.get(i));
			}
		}
		return result;
	}

	public static Date parseDate(Object dateString) {
		if (dateString != null) {
			try {
				// if the time zone part of the dateString contains a colon (e.g. 2013-09-19T21:56:36+00:00)
				// then remove it before parsing
				String isoDateString = dateString.toString().replaceFirst(":(?=[0-9]{2}$)", "");
				return dateFormatter.parse(isoDateString);
			} catch (Exception ignore) {}
		}
		return null;
	}

	private static StringEntity getStringEntity(Map<String, Object> body) throws JSONException {
		try {
			return new StringEntity(JsonUtil.convertMapToJson(body).toString());
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void changePassword(OAuth2AccessToken token, String oldPassword, String newPassword) {
		// TODO - not my high priority right now
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(AUTHORIZATION_HEADER_KEY, token.getTokenType() + " " + token.getValue());
//		HttpEntity info = new HttpEntity(headers);
//		ResponseEntity<String> response = restTemplate.exchange(authorizationUrl + "/userinfo", HttpMethod.GET, info, String.class);
//		Map<String, Object> responseMap = JsonUtil.convertJsonToMap(response.getBody());
//		String userId = (String) responseMap.get("user_id");
//		Map<String, Object> body = new HashMap<String, Object>();
//		body.put("schemas", new String[] {"urn:scim:schemas:core:1.0"});
//		body.put("password", newPassword);
//		body.put("oldPassword", oldPassword);
//		HttpEntity<Map> httpEntity = new HttpEntity<Map>(body, headers);
//		restTemplate.put(authorizationUrl + "/User/{id}/password", httpEntity, userId);
		
	}

}
