package org.cloudfoundry.client.lib.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class CloudStack  extends CloudEntity {
	private String description;

	public CloudStack(JSONObject metadata, JSONObject entity) throws JSONException {
		this(new Meta(metadata),entity.getString("name"),entity.getString("description"));
	}
	
	public CloudStack(Meta meta, String name, String description) {
		setMeta(meta);
		setName(name);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
