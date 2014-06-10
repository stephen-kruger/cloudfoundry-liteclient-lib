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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;
//import org.cloudfoundry.client.lib.rest.CloudControllerClient;
//import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;
import org.cloudfoundry.client.compat.OAuth2AccessToken;
import org.cloudfoundry.client.compat.ResponseObject;
import org.cloudfoundry.client.compat.util.Assert;
import org.cloudfoundry.client.compat.util.Utils;
//import org.cloudfoundry.client.ibmlib.util.Assert;
//import org.cloudfoundry.client.ibmlib.ResponseErrorHandler;
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
import org.cloudfoundry.client.lib.domain.CloudServicePlan;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudStack;
import org.cloudfoundry.client.lib.domain.CrashInfo;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.util.CloudEntityResourceMapper;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.json.JSONObject;

/**
 * A Java client to exercise the Cloud Foundry API.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 * @author Stephen Kruger
 */
public class CloudFoundryClient implements CloudFoundryOperations {
	public static final String METADATA="metadata";
	public static final String ENTITY="entity";
	private static Logger log = Logger.getAnonymousLogger();
	private static final String NYI = "NOT YET IMPLEMENTED";
	private static final String API_BASE = "/v2";

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

	public CloudInfo getCloudInfo() throws CloudFoundryException {
		if (info == null) {
			if (token==null) {
				log.info("Not logged in");
				token = new OAuth2AccessToken("",cloudControllerUrl.toString());
				JSONObject v2_info = ResponseObject.getResponsObject("/info", token);
				info = new CloudInfo(v2_info);
			}
			else {
				JSONObject v1_info = ResponseObject.getResponsObject("/info", token);
				JSONObject v2_info = ResponseObject.getResponsObject(API_BASE+"/info", token);
				info = new CloudInfo(v1_info, v2_info);
			}
		}
		return info;
	}

	public List<CloudSpace> getSpaces() {
		List<CloudSpace> spaces = new ArrayList<CloudSpace>();
		String urlPath = API_BASE+"/spaces?inline-relations-depth=1";
		try {
			List<JSONObject> jspaces = ResponseObject.getResources(urlPath, token);
			for (JSONObject jspace : jspaces) {
				JSONObject entity = jspace.getJSONObject(ENTITY);
				Meta meta = new Meta(jspace.getJSONObject(METADATA));
				JSONObject orgEntity = entity.getJSONObject("organization");
				CloudOrganization org = new CloudOrganization(orgEntity.getJSONObject(METADATA),
						orgEntity.getJSONObject(ENTITY));
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
		String urlPath = API_BASE+"/organizations?inline-relations-depth=0";
		List<CloudOrganization> orgs = new ArrayList<CloudOrganization>();
		try {
			List<JSONObject>entities = ResponseObject.getResources(urlPath, token);

			for (JSONObject entity : entities) {
				orgs.add(new CloudOrganization(entity.getJSONObject(METADATA),
						entity.getJSONObject(ENTITY)));
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return orgs;
	}



	public void register(String email, String password) {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public void updatePassword(String newPassword) {
		updatePassword(credentials, newPassword);
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		ResponseObject.changePassword(token, credentials.getPassword(), newPassword);
		CloudCredentials newCloudCredentials = new CloudCredentials(credentials.getEmail(), newPassword);
		if (this.credentials.getProxyUser() != null) {
			this.credentials = newCloudCredentials.proxyForUser(this.credentials.getProxyUser());
		} else {
			this.credentials = newCloudCredentials;
		}
	}

	public void unregister() {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public OAuth2AccessToken login() throws CloudFoundryException {
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

	public List<CloudApplication> getApplications() throws CloudFoundryException {
		if (applications==null) {
			applications = new ArrayList<CloudApplication>();
			List<JSONObject> ja = ResponseObject.getResources(API_BASE+"/apps?inline-relations-depth=1", token);
			for (JSONObject resource : ja) {
				CloudApplication app = new CloudApplication(token,resource.getJSONObject(ENTITY),resource.getJSONObject(METADATA));
				applications.add(app);
			}
			for (CloudApplication app : applications) {
				app.setUris(findApplicationUris(app.getMeta().getGuid()));
			}
		}
		return applications;
	}

	private List<String> findApplicationUris(UUID appGuid) {
		String urlPath = API_BASE+"/apps/"+appGuid.toString()+"/routes?inline-relations-depth=1";
		List<String> uris =  new ArrayList<String>();

		try {
			List<JSONObject> ja = ResponseObject.getResources(urlPath, token);

			for (JSONObject entity : ja) {
				String host = entity.getJSONObject(ENTITY).getString("host");
				String domain = entity.getJSONObject(ENTITY).getJSONObject("domain").getJSONObject(ENTITY).getString("name");
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

	public CloudApplication getApplication(UUID appGuid) throws CloudFoundryException {
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
				for (Object instanceId : JsonUtil.keys(iinfo)) {
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

	private JSONObject getInstanceInfoForApp(UUID appId, String path) throws CloudFoundryException {
		String urlOffset = API_BASE+"/apps/"+appId.toString()+"/" + path;
		return ResponseObject.getResponsObject(urlOffset, token);
	}

	public void createApplication(String appName, Staging staging, Integer memory, List<String> uris, List<String> serviceNames) throws CloudFoundryException {
		createApplication(appName, staging, memory, 1204, uris, serviceNames);
	}

	public void createApplication(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, List<String> serviceNames) throws CloudFoundryException {
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

		String appResp = postForObject(API_BASE+"/apps", appRequest);
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

	private void addUris(List<String> uris, UUID appGuid) throws CloudFoundryException {
		Map<String, UUID> domains = getDomainGuids();
		for (String uri : uris) {
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			UUID domainGuid = domains.get(uriInfo.get("domainName"));
			bindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	private UUID getRouteGuid(String host, UUID domainGuid) {
		String urlPath = API_BASE+"/routes?inline-relations-depth=0&q=host:"+host;

		UUID routeGuid = null;
		try {
			List<JSONObject>ja = ResponseObject.getResources(urlPath, token);
			for (JSONObject resource : ja) {
				JSONObject entity = resource.getJSONObject(ENTITY);
				JSONObject metadata = resource.getJSONObject(METADATA);
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

	private void bindRoute(String host, UUID domainGuid, UUID appGuid) throws CloudFoundryException {
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid == null) {
			routeGuid = doAddRoute(host, domainGuid);
		}
		String bindPath = API_BASE+"/apps/"+appGuid+"/routes/"+routeGuid;
		HashMap<String, Object> bindRequest = new HashMap<String, Object>();
		putForObject(bindPath, bindRequest);
	}

	private UUID doAddRoute(String host, UUID domainGuid) throws CloudFoundryException {
		assertSpaceProvided("add route");

		HashMap<String, Object> routeRequest = new HashMap<String, Object>();
		routeRequest.put("host", host);
		routeRequest.put("domain_guid", domainGuid);
		routeRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		String routeResp = postForObject(API_BASE+"/routes", routeRequest);
		try {
			Map<String, Object> routeEntity = JsonUtil.convertJsonToMap(routeResp);
			return CloudEntityResourceMapper.getMeta(routeEntity).getGuid();
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	private String putForObject(String urlOffset, HashMap<String, Object> routeRequest) {
		try {
			return ResponseObject.putResponsObject(urlOffset, token, null, routeRequest).toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, String> headForObject(String urlOffset, Map<String, String> headers, int expect) throws CloudFoundryException {
		return ResponseObject.headResponsObject(urlOffset, token, headers,expect);
	}

	private void deleteForObject(String urlOffset) throws CloudFoundryException {
		ResponseObject.deleteResponsObject(urlOffset, token);		
	}

	private String postForObject(String urlOffset, HashMap<String, Object> routeRequest) throws CloudFoundryException {
		return ResponseObject.postResponsObject(urlOffset, token, null, routeRequest).toString();
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
		String urlPath = API_BASE+"";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		String domainPath = urlPath + "/domains?inline-relations-depth=1";

		Map<String, UUID> domains = new HashMap<String, UUID>();
		try {
			List<JSONObject>ja = ResponseObject.getResources(domainPath, token);
			for (JSONObject resource: ja) {
				JSONObject entity = resource.getJSONObject(ENTITY);
				JSONObject metadata = resource.getJSONObject(METADATA);
				domains.put(entity.getString("name"),
						new Meta(metadata).getGuid());
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return domains;
	}

	private void addStagingToRequest(Staging staging, HashMap<String, Object> appRequest) throws CloudFoundryException {
		if (staging.getBuildpackUrl() != null) {
			appRequest.put("buildpack", staging.getBuildpackUrl());
		}
		if (staging.getCommand() != null) {
			appRequest.put("command", staging.getCommand());
		}
		if (staging.getStack() != null) {
			// getStack(staging.getStack()).getMeta().getGuid().toString()
				appRequest.put("stack_guid", staging.getStack().getMeta().getGuid());
		}
		if (staging.getHealthCheckTimeout() != null) {
			appRequest.put("health_check_timeout", staging.getHealthCheckTimeout().toString());
		}
	}

	public void createService(CloudService service) throws CloudFoundryException {
		assertSpaceProvided("create service");
		Assert.notNull(service, "Service must not be null");
		Assert.notNull(service.getName(), "Service name must not be null");
		Assert.notNull(service.getLabel(), "Service label must not be null");
		Assert.notNull(service.getPlan(), "Service plan must not be null");

		CloudServicePlan cloudServicePlan = findPlanForService(service);

		HashMap<String, Object> serviceRequest = new HashMap<String, Object>();
		serviceRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		serviceRequest.put("name", service.getName());
		serviceRequest.put("service_plan_guid", cloudServicePlan.getMeta().getGuid());
		postForObject(API_BASE+"/service_instances", serviceRequest);
	}

	private List<CloudServiceOffering> getServiceOfferings(String label) throws CloudFoundryException {
		Assert.notNull(label, "Service label must not be null");
		List<JSONObject> resourceList = ResponseObject.getResources(API_BASE+"/services?inline-relations-depth=1", token);
		List<CloudServiceOffering> results = new ArrayList<CloudServiceOffering>();
		for (JSONObject resource : resourceList) {
			CloudServiceOffering cloudServiceOffering = new CloudServiceOffering(resource.getJSONObject(METADATA),resource.getJSONObject(ENTITY));
			if (cloudServiceOffering.getLabel() != null && label.equals(cloudServiceOffering.getLabel())) {
				results.add(cloudServiceOffering);
			}
		}
		return results;
	}

	private CloudServicePlan findPlanForService(CloudService service) throws CloudFoundryException {
		List<CloudServiceOffering> offerings = getServiceOfferings(service.getLabel());
		for (CloudServiceOffering offering : offerings) {
			if (service.getVersion() == null || service.getVersion().equals(offering.getVersion())) {
				for (CloudServicePlan plan : offering.getCloudServicePlans()) {
					if (service.getPlan() != null && service.getPlan().equals(plan.getName())) {
						return plan;
					}
				}
			}
		}
		throw new IllegalArgumentException("Service plan " + service.getPlan() + " not found");
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
				ResponseObject ro = ResponseObject.putResponsObject(API_BASE+"/apps/"+app.getMeta().getGuid()+"?stage_async=true", token, null, appRequest);

				// now update cached services object with new values
				//				CloudApplication newApp = new CloudApplication(token,ro.getJSONObject(ENTITY),ro.getJSONObject(METADATA));
				//				updateCachedApps(newApp);
				// force reload of apps
				applications = null;

				// Return a starting info, even with a null staging log value, as a non-null starting info
				// indicates that the response entity did have headers. The API contract is to return starting info
				// if there are headers in the response, null otherwise.
				JSONObject headers = ro.getJSONObject("headers");
				if (headers.has("x-app-staging-log")) {
					return new StartingInfo(Utils.safeDecode(headers.getString("x-app-staging-log")));
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
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public void stopApplication(String appName) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		if (app.getState() != CloudApplication.AppState.STOPPED) {
			HashMap<String, Object> appRequest = new HashMap<String, Object>();
			appRequest.put("state", CloudApplication.AppState.STOPPED);
			String urlOffset = API_BASE+"/apps/"+app.getMeta().getGuid().toString();
			try {
				ResponseObject ro = ResponseObject.putResponsObject(urlOffset, token, null, appRequest);
				// now update cached services object with new values
				new CloudApplication(token,ro.getJSONObject(ENTITY),ro.getJSONObject(METADATA));
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

	public void deleteApplication(String appName) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		doDeleteApplication(appId);
	}

	private void doDeleteApplication(UUID appId) throws CloudFoundryException {
		deleteForObject(API_BASE+"/apps/"+appId.toString()+"?recursive=true");
	}

	public void deleteAllApplications() throws CloudFoundryException {
		List<CloudApplication> cloudApps = getApplications();
		for (CloudApplication cloudApp : cloudApps) {
			deleteApplication(cloudApp.getName());
		}
	}

	public void deleteAllServices() throws CloudFoundryException {
		List<CloudService> cloudServices = getServices();
		for (CloudService cloudService : cloudServices) {
			doDeleteService(cloudService);
		}
	}

	private void doDeleteService(CloudService cloudService) throws CloudFoundryException {
		List<UUID> appIds = getAppsBoundToService(cloudService);
		if (appIds.size() > 0) {
			for (UUID appId : appIds) {
				doUnbindService(appId, cloudService.getMeta().getGuid());
			}
		}
		deleteForObject(API_BASE+"/service_instances/"+cloudService.getMeta().getGuid().toString());
	}

	private List<UUID> getAppsBoundToService(CloudService cloudService) throws CloudFoundryException {
		List<UUID> appGuids = new ArrayList<UUID>();
		String urlPath = API_BASE+"";
		//		if (sessionSpace != null) {
		//			urlVars.put("space", sessionSpace.getMeta().getGuid());
		//			urlPath = urlPath + "/spaces/{space}";
		//		}
		urlPath = urlPath + "/service_instances?q="+Utils.safeEncode("name:" + cloudService.getName());
		List<JSONObject> resourceList = ResponseObject.getResources(urlPath, token);
		for (JSONObject resource : resourceList) {
			List<JSONObject> service_bindings = ResponseObject.getResources(resource.getJSONObject(ENTITY).getString("service_bindings_url"), token);
			for (JSONObject sresource : service_bindings) {
				JSONObject entity = sresource.getJSONObject(ENTITY);
				if (entity.has("app_guid"))
					appGuids.add(UUID.fromString(entity.getString("app_guid")));
			}
		}
		return appGuids;
	}

	private UUID getAppId(String appName) throws CloudFoundryException {
		// no idea why the original code does all this pain. I will just scroll through my local list
		//		Map<String, Object> resource = findApplicationResource(appName, false);
		//		UUID guid = null;
		//		if (resource != null) {
		//			Map<String, Object> appMeta = (Map<String, Object>) resource.get(METADATA);
		//			guid = UUID.fromString(String.valueOf(appMeta.get("guid")));
		//		}
		//		return guid;
		for (CloudApplication app : getApplications()) {
			if (app.getName().equals(appName))
				return app.getMeta().getGuid();
		}
		return null;
	}

	public void updateApplicationDiskQuota(String appName, int disk) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("disk_quota", disk);
		String result = putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
		log.info(result);
	}

	public void updateApplicationMemory(String appName, int memory) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("memory", memory);
		String result = putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
		log.info(result);
	}

	public void updateApplicationInstances(String appName, int instances) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("instances", instances);
		String result = putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
		log.info(result);
	}

	public void updateApplicationServices(String appName, List<String> services) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		List<UUID> addServices = new ArrayList<UUID>();
		List<UUID> deleteServices = new ArrayList<UUID>();
		// services to add
		for (String serviceName : services) {
			if (!app.getServices().contains(serviceName)) {
				CloudService cloudService = getService(serviceName);
				if (cloudService != null) {
					addServices.add(cloudService.getMeta().getGuid());
				}
				else {
					throw new CloudFoundryException(HttpStatus.SC_NOT_FOUND, "Service with name " + serviceName +
							" not found in current space " + sessionSpace.getName());
				}
			}
		}
		// services to delete
		for (String serviceName : app.getServices()) {
			if (!services.contains(serviceName)) {
				CloudService cloudService = getService(serviceName);
				if (cloudService != null) {
					deleteServices.add(cloudService.getMeta().getGuid());
				}
			}
		}
		for (UUID serviceId : addServices) {
			doBindService(app.getMeta().getGuid(), serviceId);
		}
		for (UUID serviceId : deleteServices) {
			doUnbindService(app.getMeta().getGuid(), serviceId);
		}
	}

	private void doBindService(UUID appId, UUID serviceId) throws CloudFoundryException {
		HashMap<String, Object> serviceRequest = new HashMap<String, Object>();
		serviceRequest.put("service_instance_guid", serviceId);
		serviceRequest.put("app_guid", appId);
		postForObject(API_BASE+"/service_bindings", serviceRequest);
	}

	private void doUnbindService(UUID appId, UUID serviceId) throws CloudFoundryException {
		UUID serviceBindingId = getServiceBindingId(appId, serviceId);
		deleteForObject(API_BASE+"/service_bindings/"+serviceBindingId.toString());
	}

	private UUID getServiceBindingId(UUID appId, UUID serviceId ) throws CloudFoundryException {
		List<JSONObject> resourceList = ResponseObject.getResources(API_BASE+"/apps/"+appId.toString()+"/service_bindings", token);
		UUID serviceBindingId = null;
		for (JSONObject resource : resourceList) {
			JSONObject bindingMeta = resource.getJSONObject(METADATA);
			JSONObject bindingEntity = resource.getJSONObject(ENTITY);
			String serviceInstanceGuid = bindingEntity.getString("service_instance_guid");
			if (serviceInstanceGuid != null && serviceInstanceGuid.equals(serviceId.toString())) {
				String bindingGuid = (String) bindingMeta.get("guid");
				serviceBindingId = UUID.fromString(bindingGuid);
				break;
			}
		}
		return serviceBindingId;
	}

	public void updateApplicationStaging(String appName, Staging staging) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		addStagingToRequest(staging, appRequest);
		putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
	}

	public void updateApplicationUris(String appName, List<String> uris) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		List<String> newUris = new ArrayList<String>(uris);
		newUris.removeAll(app.getUris());
		List<String> removeUris = app.getUris();
		removeUris.removeAll(uris);
		removeUris(removeUris, app.getMeta().getGuid());
		addUris(newUris, app.getMeta().getGuid());
	}

	private void removeUris(List<String> uris, UUID appGuid) throws CloudFoundryException {
		Map<String, UUID> domains = getDomainGuids();
		for (String uri : uris) {
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			UUID domainGuid = domains.get(uriInfo.get("domainName"));
			unbindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	private void unbindRoute(String host, UUID domainGuid, UUID appGuid) throws CloudFoundryException {
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid != null) {
			String bindPath = API_BASE+"/apps/"+appGuid+"/routes/"+routeGuid.toString();
			deleteForObject(bindPath);
		}
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("environment_json", env);
		putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
	}

	public void updateApplicationEnv(String appName, List<String> env) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("environment_json", env);
		putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
	}

	public Map<String, String> getLogs(String appName) throws CloudFoundryException {
		String urlPath = API_BASE+"/apps/"+getAppId(appName)+"/instances/0/files";
		String instance = String.valueOf(0);
		return doGetLogs(urlPath, appName, instance);
	}

	public StreamingLogToken streamLogs(String appName, ApplicationLogListener listener) throws CloudFoundryException {	        
		UUID appId = getAppId(appName);
		CloudInfo cloudInfo = getCloudInfo();
		log.info("Streamlogs :"+cloudInfo.getLoggregatorEndpoint()+" "+appId);
		//	        String mode = recent ? "dump" : "tail";
		//	        URI loggregatorUri = loggregatorUriTemplate.expand(cloudInfo.getLoggregatorEndpoint(), mode, appId);
		//	        try {
		//	            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		//	            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();
		//	            Session session = container.connectToServer(new LoggregatorEndpoint(listener), config, loggregatorUri);
		//	            return new StreamingLogTokenImpl(session);
		//	        } 
		//	        catch (DeploymentException e) {
		//	            throw new CloudOperationException(e);
		//	        } 
		//	        catch (IOException e) {
		//	            throw new CloudOperationException(e);
		//	        }
		return null;
	}

	//    public StreamingLogToken streamRecentLogs(String appName, ApplicationLogListener listener) {
	//        return null;//cc.streamRecentLogs(appName, listener);
	//    }

	public Map<String, String> getCrashLogs(String appName) throws CloudFoundryException {
		String filePath = "";// TODO - where am I supposed to get this value?
		int index = 0;// TODO - where am I supposed to get this value?
		String urlPath = getFileUrlPath(index,filePath);
		CrashesInfo crashes = getCrashes(appName);
		if (crashes.getCrashes().isEmpty()) {
			return Collections.emptyMap();
		}
		TreeMap<Date, String> crashInstances = new TreeMap<Date, String>();
		for (CrashInfo crash : crashes.getCrashes()) {
			crashInstances.put(crash.getSince(), crash.getInstance());
		}
		String instance = crashInstances.get(crashInstances.lastKey());
		return doGetLogs(urlPath, appName, instance);
	}

	protected Map<String, String> doGetLogs(String urlPath, String appName, String instance) throws CloudFoundryException {
		Object appId = getFileAppId(appName);
		String logFiles = doGetFile(urlPath, appId, instance, "logs", -1, -1);
		String[] lines = logFiles.split("\n");
		List<String> fileNames = new ArrayList<String>();
		for (String line : lines) {
			String[] parts = line.split("\\s");
			if (parts.length > 0 && parts[0] != null) {
				fileNames.add(parts[0]);
			}
		}
		Map<String, String> logs = new HashMap<String, String>(fileNames.size());
		for(String fileName : fileNames) {
			String logFile = "logs" + "/" + fileName;
			logs.put(logFile, doGetFile(urlPath, appId, instance, logFile, -1, -1));
		}
		return logs;
	}

	public String getStagingLogs(StartingInfo info, int offset) throws CloudFoundryException {
		String stagingFile = info.getStagingFile();
		if (stagingFile != null) {
			try {
				HashMap<String, Object> logsRequest = new HashMap<String, Object>();
				logsRequest.put("offset", offset);
				String ro = ResponseObject.getResponsObjectAsString(stagingFile + "&tail&tail_offset="+offset, token);
				return ro;
			} catch (CloudFoundryException e) {
				if (e.getStatusCode()==HttpStatus.SC_NOT_FOUND) {
					// Content is no longer available
					return null;
				} else {
					throw e;
				}
			} 
			catch (Throwable e) {
				// Likely read timeout, the directory server won't serve 
				// the content again
				log.severe("Caught exception while fetching staging logs. Aborting. Caught:" + e.getMessage());
			} 
		}
		return null;
	}

	protected String getFileUrlPath(int instanceIndex, String filePath) {
		return API_BASE+"/apps/{appId}/instances/"+instanceIndex+"/files/"+filePath;
	}

	protected String doGetFile(String urlPath, Object app, int instanceIndex, String filePath, int startPosition, int endPosition) throws CloudFoundryException {
		return doGetFile(urlPath, app, String.valueOf(instanceIndex), filePath, startPosition, endPosition);
	}

	protected String doGetFile(String urlPath, Object app, String instance, String filePath, int startPosition, int endPosition) throws CloudFoundryException {
		Assert.isTrue(startPosition >= -1, "Invalid start position value: " + startPosition);
		Assert.isTrue(endPosition >= -1, "Invalid end position value: " + endPosition);
		Assert.isTrue(startPosition < 0 || endPosition < 0 || endPosition >= startPosition,
				"The end position (" + endPosition + ") can't be less than the start position (" + startPosition + ")");

		int start, end;
		if (startPosition == -1 && endPosition == -1) {
			start = 0;
			end = -1;
		} else {
			start = startPosition;
			end = endPosition;
		}

		final String range =
				"bytes=" + (start == -1 ? "" : start) + "-" + (end == -1 ? "" : end);
		return doGetFileByRange(urlPath, app, instance, filePath, start, end, range);
	}

	private String doGetFileByRange(String urlPath, Object app, String instance, String filePath, int start, int end, String range) throws CloudFoundryException {

		boolean supportsRanges;
		Map<String,String>headers = new HashMap<String,String>();
		headers.put("Range", "bytes=0-");
		try {
			headForObject(urlPath+"/"+filePath,headers,HttpStatus.SC_OK);
			supportsRanges = true;
		}
		catch (CloudFoundryException e) {
			supportsRanges = false;
		}


		headers = new HashMap<String,String>();
		if (supportsRanges) {
			headers.put("Range", range);
		}

		try {
			String response = ResponseObject.getResponsObjectAsString(urlPath+"/"+filePath, token, headers);

			boolean partialFile = false;
			//				if (response.getInt("status")!=HttpStatus.SC_PARTIAL_CONTENT) {
			partialFile = true;
			//				}
			if (!partialFile && response != null) {
				if (start == -1) {
					return response.toString().substring(response.length() - end);
				} else {
					if (start >= response.length()) {
						if (response.length() == 0) {
							return "";
						}
						throw new CloudFoundryException(HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE,
								"The starting position " + start + " is past the end of the file content.");
					}
					if (end != -1) {
						if (end >= response.length()) {
							end = response.length() - 1;
						}
						return response.toString().substring(start, end + 1);
					} else {
						return response.toString().substring(start);
					}
				}
			}
			return response.toString();
		}
		catch (CloudFoundryException cfe) {
			//cfe.printStackTrace();
			return "";
		}
	}

	protected Object getFileAppId(String appName) throws CloudFoundryException {
		return getAppId(appName);
	}

	public String getFile(String appName, int instanceIndex, String filePath) throws CloudFoundryException {
		return getFile(appName, instanceIndex, filePath, 0, -1);
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition) throws CloudFoundryException {
		Assert.isTrue(startPosition >= 0,
				startPosition + " is not a valid value for start position, it should be 0 or greater.");
		String urlPath = getFileUrlPath(instanceIndex,filePath);
		Object appId = getFileAppId(appName);
		return doGetFile(urlPath, appId, instanceIndex, filePath, startPosition, -1);
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition) throws CloudFoundryException {
		Assert.isTrue(startPosition >= 0,
				startPosition + " is not a valid value for start position, it should be 0 or greater.");
		Assert.isTrue(endPosition > startPosition,
				endPosition + " is not a valid value for end position, it should be greater than startPosition " +
						"which is " + startPosition + ".");
		Object appId = getFileAppId(appName);
		return doGetFile(appName, appId, instanceIndex, filePath, startPosition, endPosition - 1);
	}

	public String getFileTail(String appName, int instanceIndex, String filePath, int length) throws CloudFoundryException {
		Assert.isTrue(length > 0, length + " is not a valid value for length, it should be 1 or greater.");
		return getFile(appName, instanceIndex, filePath, -1, length);
	}

	// list services, un/provision services, modify instance

	public List<CloudService> getServices() {
		if (services==null) {
			services = new ArrayList<CloudService>();
			//		Map<String, Object> urlVars = new HashMap<String, Object>();
			String urlPath = API_BASE+"";
			//		if (sessionSpace != null) {
			//			urlVars.put("space", sessionSpace.getMeta().getGuid());
			//			urlPath = urlPath + "/spaces/{space}";
			//		}
			urlPath = urlPath + "/service_instances?inline-relations-depth=1&return_user_provided_service_instances=true";
			try {
				List<JSONObject> ja = ResponseObject.getResources(urlPath, token);
				for (JSONObject resource : ja) {
					JSONObject entity = resource.getJSONObject(ENTITY);
					JSONObject metadata = resource.getJSONObject(METADATA);
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
		//			String urlPath = API_BASE+"/service_instances?q="+"name:" + URLEncoder.encode(serviceName,"UTF-9")+"&return_user_provided_service_instances=true";
		//			JSONArray resources = ResponseObject.getResources(urlPath, token);
		//			for (int i=0; i < resources.length();i++) {
		//				CloudService service = new CloudService(new Meta(resources.getJSONObject(0).getJSONObject(METADATA)),
		//						resources.getJSONObject(0).getJSONObject(ENTITY),
		//						token);
		//				if (serviceName.equals(service.getName()))
		//					return service;
		//			}
		//			//			return new CloudService(new Meta(metadata),entity)
		//		}
		//		catch (Throwable t) {
		//			t.printStackTrace();
		//		}

		return null;
	}

	public void deleteService(String serviceName) throws CloudFoundryException {
		CloudService cloudService = getService(serviceName);
		doDeleteService(cloudService);
	}

	public List<CloudServiceOffering> getServiceOfferings() throws CloudFoundryException {
		String urlOffset = API_BASE+"/services?inline-relations-depth=1";
		List<JSONObject> resourceList = ResponseObject.getResources(urlOffset, token);
		List<CloudServiceOffering> serviceOfferings = new ArrayList<CloudServiceOffering>();

		for (JSONObject resource : resourceList) {
			//			System.out.println("meta");
			//			System.out.println(resource.getJSONObject(METADATA).toString(3));
			//			System.out.println("entity");
			//			System.out.println(resource.getJSONObject(ENTITY).toString(3));
			CloudServiceOffering serviceOffering = new CloudServiceOffering(resource.getJSONObject(METADATA), resource.getJSONObject(ENTITY));
			serviceOfferings.add(serviceOffering);
		}
		return serviceOfferings;
	}

	public void bindService(String appName, String serviceName) throws CloudFoundryException {
		CloudService cloudService = getService(serviceName);
		UUID appId = getAppId(appName);
		doBindService(appId, cloudService.getMeta().getGuid());
	}

	public void unbindService(String appName, String serviceName) throws CloudFoundryException {
		CloudService cloudService = getService(serviceName);
		UUID appId = getAppId(appName);
		doUnbindService(appId, cloudService.getMeta().getGuid());
	}

	public InstancesInfo getApplicationInstances(String appName) throws CloudFoundryException {
		CloudApplication app = getApplication(appName);
		return getApplicationInstances(app);
	}

	public InstancesInfo getApplicationInstances(CloudApplication app) throws CloudFoundryException {
		if (app.getState().equals(CloudApplication.AppState.STARTED)) {
			return doGetApplicationInstances(app.getMeta().getGuid());
		}
		return null;
	}

	private InstancesInfo doGetApplicationInstances(UUID appId) throws CloudFoundryException {
		try {
			List<JSONObject> instanceList = new ArrayList<JSONObject>();
			JSONObject respMap = getInstanceInfoForApp(appId, "instances");
			//			List<String> keys = new ArrayList<String>(respMap.keySet());
			for (Object instanceId : JsonUtil.keys(respMap)) {
				Integer index;
				try {
					index = Integer.valueOf(instanceId.toString());
				} catch (NumberFormatException e) {
					index = -1;
				}
				JSONObject instanceMap = respMap.getJSONObject(instanceId.toString());
				instanceMap.put("index", index);
				instanceList.add(instanceMap);
			}
			return new InstancesInfo(instanceList);
		} 
		catch (CloudFoundryException e) {
			if (e.getStatusCode()==HttpStatus.SC_BAD_REQUEST) {
				return null;
			} else {
				throw e;
			}

		}
	}
	public CrashesInfo getCrashes(String appName) {
		log.severe(NYI);
		return null;//cc.getCrashes(appName);
	}

	public List<CloudStack> getStacks() throws CloudFoundryException {
		String urlOffset = API_BASE+"/stacks";
		List<JSONObject> resourceList = ResponseObject.getResources(urlOffset, token);
		List<CloudStack> stacks = new ArrayList<CloudStack>();
		for (JSONObject resource : resourceList) {
			stacks.add(new CloudStack(resource.getJSONObject(METADATA),resource.getJSONObject(ENTITY)));
		}
		return stacks;
	}

	public CloudStack getStack(String name) throws CloudFoundryException {
		String urlOffset = API_BASE+"/stacks?q="+Utils.safeEncode("name:" + name);
		List<JSONObject> resourceList = ResponseObject.getResources(urlOffset, token);
		if (resourceList.size()>0) {
			JSONObject resource = resourceList.get(0);
			return new CloudStack(resource.getJSONObject(METADATA),resource.getJSONObject(ENTITY));
		}
		return null;
	}

	public void rename(String appName, String newName) throws CloudFoundryException {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("name", newName);
		putForObject(API_BASE+"/apps/"+appId.toString(), appRequest);
	}



	public List<CloudDomain> getPrivateDomains() throws CloudFoundryException {
		return doGetDomains(API_BASE+"/private_domains");
	}

	public List<CloudDomain> getSharedDomains() throws CloudFoundryException {
		return doGetDomains(API_BASE+"/shared_domains");
	}

	public List<CloudDomain> doGetDomains(CloudOrganization org) throws CloudFoundryException {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = API_BASE+"";
		if (org != null) {
			urlVars.put("org", org.getMeta().getGuid());
			urlPath = urlPath + "/organizations/{org}";
		}
		urlPath = urlPath + "/domains";
		return doGetDomains(urlPath, urlVars);
	}

	private List<CloudDomain> doGetDomains(String urlPath) throws CloudFoundryException {
		return doGetDomains(urlPath, null);
	}

	private List<CloudDomain> doGetDomains(String urlPath, Map<String, Object> urlVars) throws CloudFoundryException {
		List<JSONObject> domainResources = ResponseObject.getResources(urlPath, token);
		List<CloudDomain> domains = new ArrayList<CloudDomain>();
		for (JSONObject resource : domainResources) {
			domains.add(new CloudDomain(new Meta(resource.getJSONObject(METADATA)),
					resource.getJSONObject(ENTITY).getString("name"),
					null));
		}
		return domains;
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
		String urlPath = API_BASE+"/organizations/"+org.getMeta().getGuid().toString()+"/domains";
		try {
			List<JSONObject> ja = ResponseObject.getResources(urlPath, token);
			for (JSONObject resource : ja) {
				JSONObject meta = resource.getJSONObject(METADATA);
				JSONObject entity = resource.getJSONObject(ENTITY);
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

	public void addDomain(String domainName) throws CloudFoundryException {
		if (sessionSpace==null)
			sessionSpace = getSpaces().get(0);
		assertSpaceProvided("add domain");
		UUID domainGuid = getDomainGuid(domainName, false);
		if (domainGuid == null) {
			doCreateDomain(domainName);
		}
	}

	private UUID doCreateDomain(String domainName) throws CloudFoundryException {
		String urlPath = API_BASE+"/private_domains";
		HashMap<String, Object> domainRequest = new HashMap<String, Object>();
		domainRequest.put("owning_organization_guid", sessionSpace.getOrganization().getMeta().getGuid());
		domainRequest.put("name", domainName);
		domainRequest.put("wildcard", true);
		String resp = postForObject(urlPath, domainRequest);
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

	public void deleteDomain(String domainName) throws CloudFoundryException {
		assertSpaceProvided("delete domain");
		UUID domainGuid = getDomainGuid(domainName, true);
		List<CloudRoute> routes = getRoutes(domainName);
		if (routes.size() > 0) {
			throw new IllegalStateException("Unable to remove domain that is in use --" +
					" it has " + routes.size() + " routes.");
		}
		doDeleteDomain(domainGuid);
	}

	private void doDeleteDomain(UUID domainGuid) throws CloudFoundryException {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = API_BASE+"/private_domains/"+domainGuid.toString();
		urlVars.put("domain", domainGuid);
		deleteForObject(urlPath);
	}

	public void removeDomain(String domainName) {
		log.severe(NYI);
		//		cc.removeDomain(domainName);
	}

	public List<CloudRoute> getRoutes(String domainName) {
		UUID domainGuid = getDomainGuid(domainName, true);
		String urlPath = API_BASE+"/routes?inline-relations-depth=1";
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
		log.severe(NYI+" "+urlPath+" "+domainGuid);
		return routes;
	}

	private UUID getDomainGuid(String domainName, boolean required) {
		String urlPath = API_BASE+"/domains?inline-relations-depth=1&q=name:"+Utils.safeEncode(domainName);
		UUID domainGuid = null;
		try {
			List<JSONObject> ja = ResponseObject.getResources(urlPath, token);
			if (ja.size()>0) {
				return new Meta(ja.get(0).getJSONObject(METADATA)).getGuid();
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

	public void addRoute(String host, String domainName) throws CloudFoundryException {
		assertSpaceProvided("add route for domain");
		UUID domainGuid = getDomainGuid(domainName, true);
		doAddRoute(host, domainGuid);
	}

	public void deleteRoute(String host, String domainName) throws CloudFoundryException {
		assertSpaceProvided("delete route for domain");
		UUID domainGuid = getDomainGuid(domainName, true);
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid == null) {
			throw new IllegalArgumentException("Host '" + host + "' not found for domain '" + domainName + "'.");
		}
		doDeleteRoute(routeGuid);
	}

	private void doDeleteRoute(UUID routeGuid) throws CloudFoundryException {
		String urlPath = API_BASE+"/routes/"+routeGuid.toString();
		deleteForObject(urlPath);
	}

	//	public void registerRestLogListener(RestLogCallback callBack) {
	//		cc.registerRestLogListener(callBack);
	//	}
	//
	//	public void unRegisterRestLogListener(RestLogCallback callBack) {
	//		cc.unRegisterRestLogListener(callBack);
	//	}

}