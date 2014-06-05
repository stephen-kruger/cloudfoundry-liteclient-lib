package org.cloudfoundry.client.compat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 
 * @author Stephen Kruger.
 * 
 */
public class ServiceResponse extends ResponseObject {
	public enum Fields {resources,next_url,prev_url,total_pages,total_results};
	
	public ServiceResponse(InputStream is) throws JSONException {
		super(is);
	}
	
//	public static ServiceResponse getServicesResponse(URL target,OAuth2AccessToken loginResponse) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
////		GET http://api.ng.bluemix.net/v2/apps
////			Headers:
////			accept          => application/json;charset=utf-8
////			authorization => bearer eyJhbGci�.   (the token type + your access token)
//		HttpGet request = new HttpGet();
//		request.setURI(new URL(target+"/v2/apps").toURI());
//		request.setHeader("accept", "application/json;charset=utf-8");
//		request.setHeader("authorization", "bearer "+loginResponse.getString(OAuth2AccessToken.Fields.access_token.name()));
//		
//		HttpClient client = HttpClientFactory.getThreadSafeClient()
//		HttpResponse response = client.execute(request);	
//		HttpEntity entity = response.getEntity();
//		if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
//			throw new ClientProtocolException(response.getStatusLine().getReasonPhrase());
//		}
//		return new ServiceResponse(entity.getContent());
//	}
	
	public List<CloudApplication> getApplications(OAuth2AccessToken oauth2AccessToken) throws JSONException {
		List<CloudApplication> applications = new ArrayList<CloudApplication>();
		JSONArray resources = getJSONArray(Fields.resources.name());
		for (int i = 0; i < resources.length(); i++) {
			applications.add(new CloudApplication(oauth2AccessToken,resources.getJSONObject(i).getJSONObject("entity"),resources.getJSONObject(i).getJSONObject("metadata")));
		}
		return applications;
	}
}
