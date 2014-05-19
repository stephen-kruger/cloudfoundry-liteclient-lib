/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib;
/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.StartingInfo;
//import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudApplication.DebugMode;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.lib.domain.CloudEntity.Meta;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudRoute;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudStack;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.util.CloudEntityResourceMapper;
import org.cloudfoundry.client.lib.util.JsonUtil;
//import org.cloudfoundry.client.lib.rest.CloudControllerClient;
//import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;
import org.cloudfoundry.client.ibmlib.OAuth2AccessToken;
import org.cloudfoundry.client.ibmlib.ResponseObject;
//import org.cloudfoundry.client.ibmlib.util.Assert;
//import org.cloudfoundry.client.ibmlib.ResponseErrorHandler;
import org.cloudfoundry.client.ibmlib.util.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Java client to exercise the Cloud Foundry API.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 */
public class CloudFoundryClient implements CloudFoundryOperations {
	private static Logger log = Logger.getAnonymousLogger();
	private static final String NYI = "NOT YET IMPLEMENTED";
	//	private CloudControllerClient cc;

	private CloudInfo info;
	private CloudCredentials credentials;
	private URL cloudControllerUrl;
	private OAuth2AccessToken token;
	private CloudSpace sessionSpace;
	private List<CloudApplication> applications;
	private List<CloudService> services;

	/**
	 * Construct client for anonymous user. Useful only to get to the '/info' endpoint.
	 */

	//	public CloudFoundryClient(URL cloudControllerUrl) {
	////		this(null, cloudControllerUrl, null, (HttpProxyConfiguration) null, false);
	//	}
	//
	//	public CloudFoundryClient(URL cloudControllerUrl, boolean trustSelfSignedCerts) {
	////		this(null, cloudControllerUrl, null, (HttpProxyConfiguration) null, trustSelfSignedCerts);
	//	}

	//	public CloudFoundryClient(URL cloudControllerUrl, HttpProxyConfiguration httpProxyConfiguration) {
	//		this(null, cloudControllerUrl, null, httpProxyConfiguration, false);
	//	}

	//	public CloudFoundryClient(URL cloudControllerUrl, HttpProxyConfiguration httpProxyConfiguration,
	//	                          boolean trustSelfSignedCerts) {
	//		this(null, cloudControllerUrl, null, httpProxyConfiguration, trustSelfSignedCerts);
	//	}

	/**
	 * Construct client without a default org and space.
	 */

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl) {
		//		this(credentials, cloudControllerUrl, null, (HttpProxyConfiguration) null, false);
		Assert.notNull(credentials, "Credentials were not set");
		this.credentials = credentials;
		this.token = credentials.getToken();
		Assert.notNull(credentials, "Cloud controller cannot be null");
		this.cloudControllerUrl = cloudControllerUrl;
	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl,
	//	                          boolean trustSelfSignedCerts) {
	////		this(credentials, cloudControllerUrl, null, (HttpProxyConfiguration) null, trustSelfSignedCerts);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl,
	//	                          HttpProxyConfiguration httpProxyConfiguration) {
	//		this(credentials, cloudControllerUrl, null, httpProxyConfiguration, false);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl,
	//	                          HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) {
	//		this(credentials, cloudControllerUrl, null, httpProxyConfiguration, trustSelfSignedCerts);
	//	}

	/**
	 * Construct a client with a default CloudSpace.
	 */

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace) {
	////		this(credentials, cloudControllerUrl, sessionSpace, null, false);
	//    }
	//
	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace,
	//	                          boolean trustSelfSignedCerts) {
	////		this(credentials, cloudControllerUrl, sessionSpace, null, trustSelfSignedCerts);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace,
	//	                          HttpProxyConfiguration httpProxyConfiguration) {
	//		this(credentials, cloudControllerUrl, sessionSpace, httpProxyConfiguration, false);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace,
	//	                          HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) {
	//		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
	//		CloudControllerClientFactory cloudControllerClientFactory =
	//				new CloudControllerClientFactory(httpProxyConfiguration, trustSelfSignedCerts);
	//		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, sessionSpace);
	//	}

	/**
	 * Construct a client with a default space name and org name.
	 */

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName) {
	////		this(credentials, cloudControllerUrl, orgName, spaceName, null, false);
	//	}
	//
	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	//	                          boolean trustSelfSignedCerts) {
	////		this(credentials, cloudControllerUrl, orgName, spaceName, null, trustSelfSignedCerts);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	//							  HttpProxyConfiguration httpProxyConfiguration) {
	//		this(credentials, cloudControllerUrl, orgName, spaceName, httpProxyConfiguration, false);
	//	}

	//	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	//	                          HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) {
	//		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
	//		CloudControllerClientFactory cloudControllerClientFactory =
	//				new CloudControllerClientFactory(httpProxyConfiguration, trustSelfSignedCerts);
	//		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, orgName, spaceName);
	//	}

	//	public void setResponseErrorHandler(ResponseErrorHandler errorHandler) {
	//		cc.setResponseErrorHandler(errorHandler);
	//	}

	//	private CloudSpace validateSpaceAndOrg(String spaceName, String orgName) {
	//		List<CloudSpace> spaces = getSpaces();
	//
	//		for (CloudSpace space : spaces) {
	//			if (space.getName().equals(spaceName)) {
	//				CloudOrganization org = space.getOrganization();
	//				if (orgName == null || org.getName().equals(orgName)) {
	//					return space;
	//				}
	//			}
	//		}
	//
	//		throw new IllegalArgumentException("No matching organization and space found for org: " + orgName + " space: " + spaceName);
	//	}

	public URL getCloudControllerUrl() {
		return cloudControllerUrl;//cc.getCloudControllerUrl();
	}

	public CloudInfo getCloudInfo() {
		if (info == null) {
			try {
				if (token==null) {
					log.info("Not logged in");
					token = new OAuth2AccessToken("",cloudControllerUrl.toString());
					JSONObject v2_info = ResponseObject.getResponsObject("/info", token);
					info = new CloudInfo(v2_info);
				}
				else {
					JSONObject v1_info = ResponseObject.getResponsObject("/info", token);
					JSONObject v2_info = ResponseObject.getResponsObject("/v2/info", token);
					info = new CloudInfo(v1_info, v2_info);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return info;
	}

	public List<CloudSpace> getSpaces() {
		List<CloudSpace> spaces = new ArrayList<CloudSpace>();
		String urlPath = "/v2/spaces?inline-relations-depth=1";
		try {
			JSONArray ja = ResponseObject.getResources(urlPath, token);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject entity = ja.getJSONObject(i).getJSONObject("entity");
				Meta meta = new Meta(ja.getJSONObject(i).getJSONObject("metadata"));
				JSONObject orgEntity = entity.getJSONObject("organization");
				CloudOrganization org = new CloudOrganization(orgEntity.getJSONObject("metadata"),
						orgEntity.getJSONObject("entity"));
				CloudSpace space = new CloudSpace(meta,entity.getString("name"),org);
				spaces.add(space);
			}

		} 
		catch (Throwable e) {
			e.printStackTrace();
		} 
		return spaces;
	}

	public List<CloudOrganization> getOrganizations() {
		String urlPath = "/v2/organizations?inline-relations-depth=0";
		List<CloudOrganization> orgs = new ArrayList<CloudOrganization>();
		try {
			JSONArray entities = ResponseObject.getResources(urlPath, token);

			for (int i =0; i < entities.length(); i++) {
				orgs.add(new CloudOrganization(entities.getJSONObject(i).getJSONObject("metadata"),
						entities.getJSONObject(i).getJSONObject("entity")));
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return orgs;
	}



	public void register(String email, String password) {
		//cc.register(email, password);
		log.severe(NYI);
	}

	public void updatePassword(String newPassword) {
		//cc.updatePassword(newPassword);
		log.severe(NYI);
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		//cc.updatePassword(credentials, newPassword);
		log.severe(NYI);
	}

	public void unregister() {
		//cc.unregister();
		log.severe(NYI);
	}

	public OAuth2AccessToken login() throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		token=OAuth2AccessToken.getLoginResponse(cloudControllerUrl.toString(),getCloudInfo(), credentials);//cc.login();

		// force regeneration of CloudInfo so it contains additional neded info
		info = null;
		getCloudInfo();
		//sessionSpace = validateSpaceAndOrg(spaceName, orgName);
		return token;
	}

	public void logout() {
		token = null;
	}

	public List<CloudApplication> getApplications() {
		if (applications==null) {
			applications = new ArrayList<CloudApplication>();
			try {
				JSONArray ja = ResponseObject.getResources("/v2/apps?inline-relations-depth=1", token);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject resource = ja.getJSONObject(i);
					CloudApplication app = new CloudApplication(token,resource.getJSONObject("entity"),resource.getJSONObject("metadata"));
					applications.add(app);
				}
				//			System.out.println(appResponse.toString(3));
				for (CloudApplication app : applications) {
					app.setUris(findApplicationUris(app.getMeta().getGuid()));
				}
			} 
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return applications;
	}

	private List<String> findApplicationUris(UUID appGuid) {
		String urlPath = "/v2/apps/"+appGuid.toString()+"/routes?inline-relations-depth=1";
		List<String> uris =  new ArrayList<String>();

		try {
			JSONArray ja = ResponseObject.getResources(urlPath, token);

			for (int i = 0; i < ja.length();i++) {
				JSONObject entity = ja.getJSONObject(i);
				String host = entity.getJSONObject("entity").getString("host");
				String domain = entity.getJSONObject("entity").getJSONObject("domain").getJSONObject("entity").getString("name");
				uris.add(host+"."+domain);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		return uris;
	}

	public CloudApplication getApplication(String appName) throws CloudFoundryException {
		List<CloudApplication> apps = getApplications();
		for (CloudApplication app : apps) {
			if (app.getName().equals(appName)) {
				return app;
			}
		}
		throw new CloudFoundryException(HttpStatus.SC_NOT_FOUND, "Not Found","Application not found");
	}

	public CloudApplication getApplication(UUID appGuid) {
		for (CloudApplication app : getApplications()) {
			if (app.getMeta().getGuid().equals(appGuid))
				return app;
		}
		return null;
	}

	public ApplicationStats getApplicationStats(String appName) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		return doGetApplicationStats(app.getMeta().getGuid(), app.getState());
	}

	private ApplicationStats doGetApplicationStats(UUID appId, CloudApplication.AppState appState) {
		List<InstanceStats> instanceList = new ArrayList<InstanceStats>();
		if (appState.equals(CloudApplication.AppState.STARTED)) {
			try {			
				JSONObject iinfo = getInstanceInfoForApp(appId, "stats");
				for (Object instanceId : iinfo.keySet()) {
					InstanceStats instanceStats = new InstanceStats(instanceId.toString(), iinfo.getJSONObject(instanceId.toString()));
					instanceList.add(instanceStats);
				}			

			}
			catch (Throwable t) {
				t.printStackTrace();
			}

		}
		return new ApplicationStats(instanceList);
	}

	private JSONObject getInstanceInfoForApp(UUID appId, String path) throws JSONException, IllegalStateException, IOException, URISyntaxException {
		String urlOffset = "/v2/apps/"+appId.toString()+"/" + path;
		return ResponseObject.getResponsObject(urlOffset, token);
	}

	public void createApplication(String appName, Staging staging, Integer memory, List<String> uris, List<String> serviceNames) {
		createApplication(appName, staging, memory, 1204, uris, serviceNames);
	}

	public void createApplication(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, List<String> serviceNames) {
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		if (sessionSpace!=null) {
			appRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		}
		appRequest.put("name", appName);
		appRequest.put("memory", memory.toString());
		if (disk != null) {
			appRequest.put("disk_quota", disk);
		}
		appRequest.put("instances", "1");
		addStagingToRequest(staging, appRequest);
		appRequest.put("state", CloudApplication.AppState.STOPPED.name());

		String appResp = postForObject(getUrl("/v2/apps"), appRequest, String.class);
		try {
			Map<String, Object> appEntity = JsonUtil.convertJsonToMap(appResp);
			UUID newAppGuid = CloudEntityResourceMapper.getMeta(appEntity).getGuid();

			if (serviceNames != null && serviceNames.size() > 0) {
				updateApplicationServices(appName, serviceNames);
			}

			if (uris != null && uris.size() > 0) {
				addUris(uris, newAppGuid);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void addUris(List<String> uris, UUID appGuid) {
		Map<String, UUID> domains = getDomainGuids();
		for (String uri : uris) {
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			UUID domainGuid = domains.get(uriInfo.get("domainName"));
			bindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	private UUID getRouteGuid(String host, UUID domainGuid) {
		String urlPath = "/v2/routes?inline-relations-depth=0&q=host:"+host;

		UUID routeGuid = null;
		try {
			JSONArray ja = ResponseObject.getResources(urlPath, token);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject entity = ja.getJSONObject(i).getJSONObject("entity");
				JSONObject metadata = ja.getJSONObject(i).getJSONObject("metadata");
				UUID routeSpace = UUID.fromString(entity.getString("space_guid"));
				UUID routeDomain = UUID.fromString(entity.getString("domain_guid"));
				if (sessionSpace.getMeta().getGuid().equals(routeSpace) &&
						domainGuid.equals(routeDomain)) {
					routeGuid = new Meta(metadata).getGuid();
				}
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return routeGuid;
	}

	//	private List<Map<String, Object>> getAllResources(String urlPath, Map<String, Object> urlVars) {
	//		List<Map<String, Object>> allResources = new ArrayList<Map<String, Object>>();
	//		try {
	//			JSONArray resources = ResponseObject.getResources(urlPath, token);
	//		} catch (Throwable e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		// TODO - figure out how these guys convert a rich json object to a Map
	//		return allResources;
	//	}

	//	private String getForObject(String url, Class<String> class1,
	//			Map<String, Object> urlVars) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

	private void bindRoute(String host, UUID domainGuid, UUID appGuid) {
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid == null) {
			routeGuid = doAddRoute(host, domainGuid);
		}
		String bindPath = "/v2/apps/"+appGuid+"/routes/"+routeGuid;
		HashMap<String, Object> bindRequest = new HashMap<String, Object>();
		putForObject(bindPath, bindRequest, String.class);
	}

	private UUID doAddRoute(String host, UUID domainGuid) {
		assertSpaceProvided("add route");

		HashMap<String, Object> routeRequest = new HashMap<String, Object>();
		routeRequest.put("host", host);
		routeRequest.put("domain_guid", domainGuid);
		routeRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		String routeResp = postForObject(getUrl("/v2/routes"), routeRequest, String.class);
		try {
			Map<String, Object> routeEntity = JsonUtil.convertJsonToMap(routeResp);
			return CloudEntityResourceMapper.getMeta(routeEntity).getGuid();
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	private String putForObject(String urlOffset, HashMap<String, Object> routeRequest, Class<String> class1) {
		try {
			return ResponseObject.putResponsObject(urlOffset, token, null, routeRequest).toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private String postForObject(String urlOffset, HashMap<String, Object> routeRequest, Class<String> class1) {
		try {
			return ResponseObject.postResponsObject(urlOffset, token, null, routeRequest).toString();
		} 
		catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getUrl(String string) {
		return string;
	}

	private void assertSpaceProvided(String operation) {
		Assert.notNull(sessionSpace, "Unable to " + operation + " without specifying organization and space to use.");
	}

	protected void extractUriInfo(Map<String, UUID> domains, String uri, Map<String, String> uriInfo) {
		URI newUri = URI.create(uri);
		String authority = newUri.getScheme() != null ? newUri.getAuthority(): newUri.getPath();
		for (String domain : domains.keySet()) {
			if (authority != null && authority.endsWith(domain)) {
				String previousDomain = uriInfo.get("domainName");
				if (previousDomain == null || domain.length() > previousDomain.length()) {
					//Favor most specific subdomains
					uriInfo.put("domainName", domain);
					if (domain.length() < authority.length()) {
						uriInfo.put("host", authority.substring(0, authority.indexOf(domain) - 1));
					} else if (domain.length() == authority.length()) {
						uriInfo.put("host", "");
					}
				}
			}
		}
		if (uriInfo.get("domainName") == null) {
			throw new IllegalArgumentException("Domain not found for URI " + uri);
		}
		if (uriInfo.get("host") == null) {
			throw new IllegalArgumentException("Invalid URI " + uri +
					" -- host not specified for domain " + uriInfo.get("domainName"));
		}
	}

	private Map<String, UUID> getDomainGuids() {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		String domainPath = urlPath + "/domains?inline-relations-depth=1";

		Map<String, UUID> domains = new HashMap<String, UUID>();
		try {
			JSONArray ja = ResponseObject.getResources(domainPath, token);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject entity = ja.getJSONObject(i).getJSONObject("entity");
				JSONObject metadata = ja.getJSONObject(i).getJSONObject("metadata");
				domains.put(entity.getString("name"),
						new Meta(metadata).getGuid());
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return domains;
	}

	private void addStagingToRequest(Staging staging, HashMap<String, Object> appRequest) {
		if (staging.getBuildpackUrl() != null) {
			appRequest.put("buildpack", staging.getBuildpackUrl());
		}
		if (staging.getCommand() != null) {
			appRequest.put("command", staging.getCommand());
		}
		if (staging.getStack() != null) {
			appRequest.put("stack_guid", getStack(staging.getStack()).getMeta().getGuid().toString());
		}
		if (staging.getHealthCheckTimeout() != null) {
			appRequest.put("health_check_timeout", staging.getHealthCheckTimeout().toString());
		}
	}

	public void createService(CloudService service) {
		log.severe(NYI);
		//cc.createService(service);
	}

	public void createUserProvidedService(CloudService service, Map<String, Object> credentials) {
		log.severe(NYI);
		//cc.createUserProvidedService(service, credentials);
	}

	public void uploadApplication(String appName, String file) throws IOException {
		log.severe(NYI);
		//cc.uploadApplication(appName, new File(file), null);
	}

	public void uploadApplication(String appName, File file) throws IOException {
		log.severe(NYI);
		//cc.uploadApplication(appName, file, null);
	}

	//	public void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException {
	//cc.uploadApplication(appName, file, callback);
	//	}

	//	public void uploadApplication(String appName, ApplicationArchive archive) throws IOException {
	//		cc.uploadApplication(appName, archive, null);
	//	}

	//	public void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException {
	//		cc.uploadApplication(appName, archive, callback);
	//	}

	public StartingInfo startApplication(String appName) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		if (app.getState() != CloudApplication.AppState.STARTED) {
			log.info("Starting application "+appName);
			HashMap<String, Object> appRequest = new HashMap<String, Object>();
			appRequest.put("state", CloudApplication.AppState.STARTED);
			try {
				ResponseObject ro = ResponseObject.putResponsObject("/v2/apps/"+app.getMeta().getGuid()+"?stage_async=true", token, null, appRequest);

				// now update cached services object with new values
				//				CloudApplication newApp = new CloudApplication(token,ro.getJSONObject("entity"),ro.getJSONObject("metadata"));
				//				updateCachedApps(newApp);
				// force reload of apps
				applications = null;

				// Return a starting info, even with a null staging log value, as a non-null starting info
				// indicates that the response entity did have headers. The API contract is to return starting info
				// if there are headers in the response, null otherwise.
				JSONObject headers = ro.getJSONObject("headers");
				if (headers.has("x-app-staging-log")) {
					return new StartingInfo(URLDecoder.decode(headers.getString("x-app-staging-log"), "UTF-8"));
				}
				else {
					return new StartingInfo(null);
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			// Return the starting info even if decoding failed or staging file is null
			//				return new StartingInfo(stagingFile);
			return null;
			//			}
		}
		log.info("App was already started");
		return null;
	}

	public void debugApplication(String appName, DebugMode mode) {
		log.severe(NYI);
		//cc.debugApplication(appName, mode);
	}

	public void stopApplication(String appName) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		if (app.getState() != CloudApplication.AppState.STOPPED) {
			HashMap<String, Object> appRequest = new HashMap<String, Object>();
			appRequest.put("state", CloudApplication.AppState.STOPPED);
			String urlOffset = "/v2/apps/"+app.getMeta().getGuid().toString();
			try {
				ResponseObject ro = ResponseObject.putResponsObject(urlOffset, token, null, appRequest);
				// now update cached services object with new values
				new CloudApplication(token,ro.getJSONObject("entity"),ro.getJSONObject("metadata"));
				// force reload of apps
				applications = null;
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		else {
			log.info("Application already stopped :"+appName);
		}
	}

	public StartingInfo restartApplication(String appName) throws CloudFoundryException {
		stopApplication(appName);
		return startApplication(appName);
	}

	public void deleteApplication(String appName) {
		log.severe(NYI);
		//cc.deleteApplication(appName);
	}

	public void deleteAllApplications() {
		log.severe(NYI);
		//cc.deleteAllApplications();
	}

	public void deleteAllServices() {
		log.severe(NYI);
		//cc.deleteAllServices();
	}

	public void updateApplicationDiskQuota(String appName, int disk) {
		log.severe(NYI);
		//cc.updateApplicationDiskQuota(appName, disk);
	}

	public void updateApplicationMemory(String appName, int memory) {
		log.severe(NYI);
		//cc.updateApplicationMemory(appName, memory);
	}

	public void updateApplicationInstances(String appName, int instances) {
		log.severe(NYI);
		//cc.updateApplicationInstances(appName, instances);
	}

	public void updateApplicationServices(String appName, List<String> services) {
		log.severe(NYI);
		//cc.updateApplicationServices(appName, services);
	}

	public void updateApplicationStaging(String appName, Staging staging) {
		log.severe(NYI);
		//cc.updateApplicationStaging(appName, staging);
	}

	public void updateApplicationUris(String appName, List<String> uris) {
		log.severe(NYI);
		//cc.updateApplicationUris(appName, uris);
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) {
		log.severe(NYI);
		//cc.updateApplicationEnv(appName, env);
	}

	public void updateApplicationEnv(String appName, List<String> env) {
		log.severe(NYI);
		//cc.updateApplicationEnv(appName, env);
	}

	/**
	 * @deprecated use {@link #streamLogs(String, ApplicationLogListener)} or {@link #streamRecentLogs(String, ApplicationLogListener)}
	 */
	public Map<String, String> getLogs(String appName) {
		log.severe(NYI);
		return null;//cc.getLogs(appName);
	}

	//	public StreamingLogToken streamLogs(String appName, ApplicationLogListener listener) {
	//	    return null;//cc.streamLogs(appName, listener);
	//	}

	//    public StreamingLogToken streamRecentLogs(String appName, ApplicationLogListener listener) {
	//        return null;//cc.streamRecentLogs(appName, listener);
	//    }

	public Map<String, String> getCrashLogs(String appName) {
		log.severe(NYI);
		return null;//cc.getCrashLogs(appName);
	}

	public String getStagingLogs(StartingInfo info, int offset) {
		log.severe(NYI);
		return null;//cc.getStagingLogs(info, offset);
	}

	public String getFile(String appName, int instanceIndex, String filePath) {
		log.severe(NYI);
		return null;//cc.getFile(appName, instanceIndex, filePath, 0, -1);
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition) {
		Assert.isTrue(startPosition >= 0,
				startPosition + " is not a valid value for start position, it should be 0 or greater.");
		log.severe(NYI);
		return null;//cc.getFile(appName, instanceIndex, filePath, startPosition, -1);
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition) {
		Assert.isTrue(startPosition >= 0,
				startPosition + " is not a valid value for start position, it should be 0 or greater.");
		Assert.isTrue(endPosition > startPosition,
				endPosition + " is not a valid value for end position, it should be greater than startPosition " +
						"which is " + startPosition + ".");
		log.severe(NYI);
		return null;//cc.getFile(appName, instanceIndex, filePath, startPosition, endPosition - 1);
	}

	public String getFileTail(String appName, int instanceIndex, String filePath, int length) {
		Assert.isTrue(length > 0, length + " is not a valid value for length, it should be 1 or greater.");
		log.severe(NYI);
		return null;//cc.getFile(appName, instanceIndex, filePath, -1, length);
	}

	// list services, un/provision services, modify instance

	public List<CloudService> getServices() {
		if (services==null) {
			services = new ArrayList<CloudService>();
			//		Map<String, Object> urlVars = new HashMap<String, Object>();
			String urlPath = "/v2";
			//		if (sessionSpace != null) {
			//			urlVars.put("space", sessionSpace.getMeta().getGuid());
			//			urlPath = urlPath + "/spaces/{space}";
			//		}
			urlPath = urlPath + "/service_instances?inline-relations-depth=1&return_user_provided_service_instances=true";
			try {
				JSONArray ja = ResponseObject.getResources(urlPath, token);
				for (int i = 0; i < ja.length(); i++) {
					//System.out.println(ja.toString(3));
					JSONObject entity = ja.getJSONObject(i).getJSONObject("entity");
					JSONObject metadata = ja.getJSONObject(i).getJSONObject("metadata");
					services.add(new CloudService(new Meta(metadata),entity, token));
				}
			} 
			catch (Throwable e) {
				e.printStackTrace();
			} 
			//		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
			//		for (Map<String, Object> resource : resourceList) {
			//			if (hasEmbeddedResource(resource, "service_plan")) {
			//				fillInEmbeddedResource(resource, "service_plan", "service");
			//			}
			//			services.add(resourceMapper.mapResource(resource, CloudService.class));
			//		}
		}
		return services;
	}

	public CloudService getService(String serviceName) {
		for (CloudService service : getServices()) {
			if (service.getName().equals(serviceName))
				return service;
		}
		//		if (sessionSpace != null) {
		//		urlVars.put("space", sessionSpace.getMeta().getGuid());
		//		urlPath = urlPath + "/spaces/{space}";
		//	}

		//		try {
		//			String urlPath = "/v2/service_instances?q="+"name:" + URLEncoder.encode(serviceName,"UTF-9")+"&return_user_provided_service_instances=true";
		//			JSONArray resources = ResponseObject.getResources(urlPath, token);
		//			for (int i=0; i < resources.length();i++) {
		//				CloudService service = new CloudService(new Meta(resources.getJSONObject(0).getJSONObject("metadata")),
		//						resources.getJSONObject(0).getJSONObject("entity"),
		//						token);
		//				if (serviceName.equals(service.getName()))
		//					return service;
		//			}
		//			//			System.out.println(resources.toString());
		//			//			return new CloudService(new Meta(metadata),entity)
		//		}
		//		catch (Throwable t) {
		//			t.printStackTrace();
		//		}

		return null;
	}

	public void deleteService(String serviceName) {
		log.severe(NYI);
	}

	public List<CloudServiceOffering> getServiceOfferings() {
		log.severe(NYI);
		return null;//cc.getServiceOfferings();
	}

	public void bindService(String appName, String serviceName) {
		log.severe(NYI);
		//		cc.bindService(appName, serviceName);
	}

	public void unbindService(String appName, String serviceName) {
		log.severe(NYI);
		//cc.unbindService(appName, serviceName);
	}

	public InstancesInfo getApplicationInstances(String appName) {
		log.severe(NYI);
		return null;//cc.getApplicationInstances(appName);
	}

	public InstancesInfo getApplicationInstances(CloudApplication app) {
		log.severe(NYI);
		return null;//cc.getApplicationInstances(app);
	}

	public CrashesInfo getCrashes(String appName) {
		log.severe(NYI);
		return null;//cc.getCrashes(appName);
	}

	public List<CloudStack> getStacks() {
		log.severe(NYI);
		return null;//cc.getStacks();
	}

	public CloudStack getStack(String name) {
		log.severe(NYI);
		return null;//cc.getStack(name);
	}

	public void rename(String appName, String newName) {
		log.severe(NYI);
		//		cc.rename(appName, newName);
	}



	public List<CloudDomain> getPrivateDomains() {
		log.severe(NYI);
		return null;//cc.getPrivateDomains();
	}

	public List<CloudDomain> getSharedDomains() {
		log.severe(NYI);
		return null;//cc.getSharedDomains();
	}

	public List<CloudDomain> getDomains() {
		List<CloudOrganization> orgs = getOrganizations();
		List<CloudDomain> domains = new ArrayList<CloudDomain>();
		for (CloudOrganization org : orgs) {
			List<CloudDomain> orgd = getDomainsForOrg(org);
			domains.addAll(orgd);
		}
		return domains;
	}

	public List<CloudDomain> getDomainsForOrg(CloudOrganization org) {
		List<CloudDomain> domains = new ArrayList<CloudDomain>();
		String urlPath = "/v2/organizations/"+org.getMeta().getGuid().toString()+"/domains";
		try {
			JSONArray ja = ResponseObject.getResources(urlPath, token);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject meta = ja.getJSONObject(i).getJSONObject("metadata");
				JSONObject entity = ja.getJSONObject(i).getJSONObject("entity");
				domains.add(new CloudDomain(new Meta(meta),entity.getString("name"),org));
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return domains;
	}

	public List<CloudDomain> getDomainsForOrg() {
		assertSpaceProvided("access organization domains");
		return getDomainsForOrg(sessionSpace.getOrganization());
	}

	public void addDomain(String domainName) {
		if (sessionSpace==null)
			sessionSpace = getSpaces().get(0);
		assertSpaceProvided("add domain");
		UUID domainGuid = getDomainGuid(domainName, false);
		if (domainGuid == null) {
			doCreateDomain(domainName);
		}
	}

	private UUID doCreateDomain(String domainName) {
		String urlPath = "/v2/private_domains";
		HashMap<String, Object> domainRequest = new HashMap<String, Object>();
		domainRequest.put("owning_organization_guid", sessionSpace.getOrganization().getMeta().getGuid());
		domainRequest.put("name", domainName);
		domainRequest.put("wildcard", true);
		String resp = postForObject(getUrl(urlPath), domainRequest, String.class);
		log.info(resp);
		try {
			//		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
			//		return resourceMapper.getGuidOfResource(respMap);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public void deleteDomain(String domainName) {
		log.severe(NYI);
		//		cc.deleteDomain(domainName);
	}

	public void removeDomain(String domainName) {
		log.severe(NYI);
		//		cc.removeDomain(domainName);
	}

	public List<CloudRoute> getRoutes(String domainName) {
		UUID domainGuid = getDomainGuid(domainName, true);
		String urlPath = "/v2/routes?inline-relations-depth=1";
		List<CloudRoute> routes = new ArrayList<CloudRoute>();
		// tbd


		//		for (Map<String, Object> route : allRoutes) {
		////			TODO: move space_guid to path once implemented (see above):
		//			UUID space = CloudEntityResourceMapper.getEntityAttribute(route, "space_guid", UUID.class);
		//			UUID domain = CloudEntityResourceMapper.getEntityAttribute(route, "domain_guid", UUID.class);
		//			if (sessionSpace.getMeta().getGuid().equals(space) && domainGuid.equals(domain)) {
		//				//routes.add(CloudEntityResourceMapper.getEntityAttribute(route, "host", String.class));
		//				routes.add(resourceMapper.mapResource(route, CloudRoute.class));
		//			}
		//		}
		log.severe(NYI);
		return routes;
	}

	private UUID getDomainGuid(String domainName, boolean required) {
		String urlPath = "/v2/domains?inline-relations-depth=1&q=name:"+URLEncoder.encode(domainName);
		UUID domainGuid = null;
		try {
			JSONArray ja = ResponseObject.getResources(urlPath, token);
			if (ja.length()>0) {
				return new Meta(ja.getJSONObject(0).getJSONObject("metadata")).getGuid();
			}
			// if we got here, no domains set
			if (required) {
				throw new IllegalArgumentException("Domain '" + domainName + "' not found.");
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return domainGuid;
	}

	public void addRoute(String host, String domainName) {
		//		cc.addRoute(host, domainName);
		log.severe(NYI);
	}

	public void deleteRoute(String host, String domainName) {
		//		cc.deleteRoute(host, domainName);
		log.severe(NYI);
	}

	//	public void registerRestLogListener(RestLogCallback callBack) {
	//		cc.registerRestLogListener(callBack);
	//	}
	//
	//	public void unRegisterRestLogListener(RestLogCallback callBack) {
	//		cc.unRegisterRestLogListener(callBack);
	//	}

}