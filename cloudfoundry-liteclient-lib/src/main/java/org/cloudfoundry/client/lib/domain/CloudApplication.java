package org.cloudfoundry.client.lib.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cloudfoundry.client.ibmlib.OAuth2AccessToken;
import org.cloudfoundry.client.ibmlib.ResponseObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudApplication {
	public enum AppState {
		UPDATING, STARTED, STOPPED
	}

	public enum DebugMode {
		run,
		suspend
	}

	public enum EntityField {
		stack_guid,memory,
		space_guid,
		health_check_timeout,
		buildpack,
		service_bindings_url,
		events_url,
		detected_buildpack,
		command,
		name,
		staging_task_id,
		state,
		debug,
		environment_json,
		space_url,
		disk_quota,
		console,
		stack_url,
		package_state,
		version,
		production,
		routes_url,
		detected_buildpack_guid,
		instances
	}

	public enum MetadataFields {
		updated_at,
		guid,
		created_at,
		url
	}
	private JSONObject entity;
	private JSONObject metadata;
	private OAuth2AccessToken oauth2AccessToken;
	private int diskQuota;
	private int memory;
	private List<String> env;
	private List<String> services;
	private int instances;
	private Staging staging;
	private AppState state;
	//	private int runningInstances;
	private List<String> uris;

	public CloudApplication(OAuth2AccessToken oauth2AccessToken,JSONObject entity, JSONObject metadata) {
		this.oauth2AccessToken = oauth2AccessToken;
		this.entity = entity;
		this.metadata = metadata;

		//		System.out.println("---------------------");
		//		System.out.println(metadata.toString(3));
		//		System.out.println(entity.toString(3));

		// populate the object fields
		try {
			state = AppState.valueOf(entity.getString(EntityField.state.name()));
			//			runningInstances = entity.getInt(EntityField.instances.name());
			instances = entity.getInt(EntityField.instances.name());
			setEnv(getEnvFromJSON(entity.getJSONObject("environment_json")));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public List<String> getServices() {
		if (services==null) {
			services = new ArrayList<String>();
			String urlPath = "/v2/service_instances?inline-relations-depth=1&return_user_provided_service_instances=true";

			try {
				ResponseObject ro = ResponseObject.getResponsObject(urlPath, oauth2AccessToken);
				String appGuid = metadata.getString("guid");
				JSONArray resources = ro.getJSONArray("resources");
				for (int i = 0; i < resources.length();i++) {
					JSONObject entity = resources.getJSONObject(i).getJSONObject("entity");
					JSONArray service_bindings = entity.getJSONArray("service_bindings");
					for (int j = 0; j < service_bindings.length();j++) {
						JSONObject jo = service_bindings.getJSONObject(j).getJSONObject("entity");
						if (appGuid.equals(jo.getString("app_guid"))) {
							services.add(entity.getString("name"));
						}
					}
				}
			} 
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	//	private List<Map<String, Object>> getAllResources(String urlOffset) {
	//		List<Map<String, Object>> allResources = new ArrayList<Map<String, Object>>();
	//		try {
	//
	//			ResponseObject ro = ResponseObject.getResponsObject(urlOffset, oauth2AccessToken);
	//			Map<String, Object> respMap = ro.convertJsonToMap();
	//			List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
	//			if (newResources != null && newResources.size() > 0) {
	//				allResources.addAll(newResources);
	//			}
	//			String nextUrl = (String) respMap.get("next_url");
	//			while (nextUrl != null && nextUrl.length() > 0) {
	//				nextUrl = addPageOfResources(nextUrl, allResources);
	//			}
	//		}
	//		catch (Throwable t) {
	//			t.printStackTrace();
	//		}
	//		return allResources;
	//	}

	//	private String addPageOfResources(String nextUrl, List<Map<String, Object>> allResources) throws URISyntaxException, IOException {
	//		ResponseObject ro = ResponseObject.getResponsObject(nextUrl, oauth2AccessToken);
	//
	//		Map<String, Object> respMap = ro.convertJsonToMap();
	//		List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
	//		if (newResources != null && newResources.size() > 0) {
	//			allResources.addAll(newResources);
	//		}
	//		return (String) respMap.get("next_url");
	//	}

	//	private void fillInEmbeddedResource(Map<String, Object> resource, String... resourcePath) throws JSONException, IllegalStateException, IOException, URISyntaxException {
	//		if (resourcePath.length == 0) {
	//			return;
	//		}
	//		Map<String, Object> entity = (Map<String, Object>) resource.get("entity");
	//
	//		String headKey = resourcePath[0];
	//		String[] tailPath = Arrays.copyOfRange(resourcePath, 1, resourcePath.length);
	//
	//		if (!entity.containsKey(headKey)) {
	//			String pathUrl = entity.get(headKey + "_url").toString();
	//			ResponseObject ro = ResponseObject.getResponsObject(pathUrl, oauth2AccessToken);
	//			Object response = ro.convertJsonToMap();
	//			if (response instanceof Map) {
	//				Map<String, Object> responseMap = (Map<String, Object>) response;
	//				if (responseMap.containsKey("resources")) {
	//					response = responseMap.get("resources");
	//				}
	//			}
	//			entity.put(headKey, response);
	//		}
	//		Object embeddedResource = entity.get(headKey);
	//
	//		if (embeddedResource instanceof Map) {
	//			Map<String, Object> embeddedResourceMap = (Map<String, Object>) embeddedResource;
	//			//entity = (Map<String, Object>) embeddedResourceMap.get("entity");
	//			fillInEmbeddedResource(embeddedResourceMap, tailPath);
	//		} else if (embeddedResource instanceof List) {
	//			List<Object> embeddedResourcesList = (List<Object>) embeddedResource;
	//			for (Object r: embeddedResourcesList) {
	//				fillInEmbeddedResource((Map<String, Object>)r, tailPath);
	//			}
	//		} else {
	//			// no way to proceed
	//			return;
	//		}
	//	}

	//	private boolean hasEmbeddedResource(Map<String, Object> resource, String resourceKey) {
	//		Map<String, Object> entity = (Map<String, Object>) resource.get("entity");
	//		return entity.containsKey(resourceKey) || entity.containsKey(resourceKey + "_url");
	//	}

	public String getName() {
		try {
			return entity.getString(EntityField.name.name());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public DebugMode getDebug() {
		try {
			if (entity.isNull(EntityField.debug.name()))
				return DebugMode.suspend;
			else
				return DebugMode.valueOf(entity.getString(EntityField.debug.name()));
		} 
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setDebug(DebugMode debug) {
		try {
			entity.put(EntityField.debug.name(),debug.name());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getUris() {
		return uris;
	}

	public void setUris(List<String> uris) {
		this.uris = uris;
	}

	//	public Map<String, String> getEnvAsMap() {
	//		Map<String,String> envMap = new HashMap<String, String>();
	//		for (String nameAndValue : env) {
	//			String[] parts = nameAndValue.split("=");
	//			envMap.put(parts[0], parts.length == 2 ? parts[1] : null);
	//		}
	//		return envMap;
	//	}

	// TODO - this method fails with 404 on Bluemix
	public List<String> getEnv() {
		//{"system_env_json":{"VCAP_SERVICES":{}},"environment_json":{"env_var":"env_val"}}		
		if (env==null) {
			String urlOffset = "/v2/apps/"+getGuid().toString()+"/env";
			try {
				ResponseObject ro = ResponseObject.getResponsObject(urlOffset, oauth2AccessToken);
				JSONObject environment_json = ro.getJSONObject("environment_json");
				env = getEnvFromJSON(environment_json);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return env;
	}

	private static List<String> getEnvFromJSON(JSONObject jsonObject) {
		List<String> e = new ArrayList<String>();
		for (Object key : jsonObject.keySet()) {
			e.add(e.toString()+"="+jsonObject.getString(key.toString()));
		}
		return e;
	}

	//	public void setEnv(Map<String, String> env) {
	//		List<String> joined = new ArrayList<String>();
	//		for (Map.Entry<String, String> entry : env.entrySet()) {
	//			joined.add(entry.getKey() + '=' + entry.getValue());
	//		}
	//		this.env = joined;
	//	}

	public void setEnv(List<String> env) {
		for (String s : env) {
			if (!s.contains("=")) {
				throw new IllegalArgumentException("Environment setting without '=' is invalid: " + s);
			}
		}
		this.env = env;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getDiskQuota() {
		return diskQuota;
	}

	public void setDiskQuota(int diskQuota) {
		this.diskQuota = diskQuota;
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public Staging getStaging() {
		return staging;
	}

	public void setStaging(Staging staging) {
		this.staging = staging;
	}

	public AppState getState() {
		return this.state;
	}

	public void setState(AppState state) {
		this.state = state;
	}

	//	public int getRunningInstances() {
	//		return runningInstances;
	//	}
	//
	//	public void setRunningInstances(int runningInstances) {
	//		this.runningInstances = runningInstances;
	//	}

	@Override
	public String toString() {
		return "CloudApplication [staging=" + staging + ", instances="
				+ instances + ", name=" + getName() 
				+ ", memory=" + memory + ", diskQuota=" + diskQuota
				+ ", state=" + state + ", debug=" + getDebug() + ", uris=" + getUris() + ",services=" + services
				+ ", env=" + env + "]";
	}

	public UUID getGuid() {
		try {
			return UUID.fromString(metadata.getString("guid"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}


//"apps_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/apps",
//"organization_url": "/v2/organizations/432bfe49-173a-4ab9-9751-b5b4e13a8efa",
//"service_instances_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/service_instances",
//"auditors_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/auditors",
//"organization_guid": "432bfe49-173a-4ab9-9751-b5b4e13a8efa",
//"managers_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/managers",
//"app_events_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/app_events",
//"events_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/events",
//"developers_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/developers",
//"routes_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/routes",
//"name": "dev",
//"domains_url": "/v2/spaces/3007e522-64b4-470d-81bd-3bf681cd7a0d/domains"