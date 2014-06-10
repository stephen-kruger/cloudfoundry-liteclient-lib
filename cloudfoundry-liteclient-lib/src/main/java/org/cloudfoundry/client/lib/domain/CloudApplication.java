package org.cloudfoundry.client.lib.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.compat.OAuth2AccessToken;
import org.cloudfoundry.client.compat.ResponseObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudApplication extends CloudEntity {
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
	private OAuth2AccessToken oauth2AccessToken;
	private int diskQuota;
	private int memory;
	private List<String> env;
	private List<String> services;
	private int instances;
	private Staging staging;
	private AppState state;
	private List<String> uris;
	private Integer runningInstances;
	private DebugMode debug;

	public CloudApplication(OAuth2AccessToken oauth2AccessToken,JSONObject entity, JSONObject metadata) throws JSONException {
		this.oauth2AccessToken = oauth2AccessToken;
		this.entity = entity;
		setMeta(new Meta(metadata));

		// populate the object fields
		try {
			state = AppState.valueOf(entity.getString(EntityField.state.name()));
			//			runningInstances = entity.getInt(EntityField.instances.name());
			instances = entity.getInt(EntityField.instances.name());
			setEnv(getEnvFromJSON(entity.getJSONObject("environment_json")));
			//---------inherited
			if (entity.has("runningInstances")) {
				runningInstances = entity.getInt("runningInstances");
			}

			if (entity.has("memory")) {
				memory = entity.getInt("memory");
			}
			if (entity.has("disk_quota")) {
				diskQuota = entity.getInt("disk_quota");
			}
			//			env = (List<String>) attributes.get("env");

			if (entity.isNull(EntityField.debug.name()))
				setDebug(null);
			else
				setDebug(DebugMode.valueOf(entity.getString(EntityField.debug.name())));

			String command = null;
			if (entity.has("command")) {
				if (entity.isNull("comand"))
					command = null;
				else
					command = entity.getString("command");
			}
			String buildpackUrl = null;
			if (entity.has("buildpack")) {
				if (entity.isNull("comand"))
					buildpackUrl = null;
				else
					buildpackUrl = entity.getString("buildpack");
			}
			if (entity.has("stack")) {
				CloudStack stack = new CloudStack(entity.getJSONObject("stack").getJSONObject("metadata"),entity.getJSONObject("stack").getJSONObject("entity"));
				System.out.println(entity.getJSONObject("stack").toString());
				setStaging(new Staging(command, buildpackUrl,stack,null));
			}
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
				String appGuid = getMeta().getGuid().toString();
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

	public String getName() {
		try {
			return entity.getString(EntityField.name.name());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public DebugMode getDebug() {
		return debug;
	}

	public void setDebug(DebugMode debug) {
		this.debug = debug;
	}

	public List<String> getUris() {
		return uris;
	}

	public void setUris(List<String> uris) {
		this.uris = uris;
	}

	public List<String> getEnv() {
		return env;
	}
	
	public Map<String,String> getEnvAsMap() {
		Map<String,String> envMap = new HashMap<String, String>();
		for (String nameAndValue : getEnv()) {
			String[] parts = nameAndValue.split("=");
			envMap.put(parts[0], parts.length == 2 ? parts[1] : null);
		}
		return envMap;
	}

	private static List<String> getEnvFromJSON(JSONObject jsonObject) throws JSONException {
		List<String> e = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
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
				+ ", state=" + state + ", debug=" + getDebug() + ", uris=" + getUris() + ",services=" + getServices()
				+ ", env=" + env + "]";
	}

	public int getRunningInstances() {
		return runningInstances;
	}

	public void setRunningInstances(int runningInstances) {
		this.runningInstances = runningInstances;
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