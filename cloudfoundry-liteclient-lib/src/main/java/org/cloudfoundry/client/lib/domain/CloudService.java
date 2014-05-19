/*
 * Copyright 2009-2012 the original author or authors.
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

package org.cloudfoundry.client.lib.domain;

import org.cloudfoundry.client.compat.OAuth2AccessToken;
import org.cloudfoundry.client.compat.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Class representing an instance of a service created for a space.
 *
 * @author Thomas Risberg
 */
public class CloudService extends CloudEntity {

	private String version;
	private String provider;

	private String label;
	private String plan;

	public CloudService() {
		super();
	}

	public CloudService(Meta meta, String name) {
		super(meta, name);
	}

	public CloudService(Meta meta, JSONObject entity, OAuth2AccessToken token) throws JSONException {
		super(meta, entity.getString("name"));
		if ((entity.has("gateway_data")&&(!entity.isNull("gateway_data")))) {
			if (entity.getJSONObject("gateway_data").has("plan")) {
				setVersion(entity.getJSONObject("gateway_data").getString("version"));
				setPlan(entity.getJSONObject("gateway_data").getString("plan"));
			}
		}

		// fill in the details
		try {
			if (entity.has("service_plan")) {
				JSONObject servicePlanEntity = entity.getJSONObject("service_plan").getJSONObject("entity");
				ResponseObject ro = ResponseObject.getResponsObject(servicePlanEntity.getString("service_url"), token);
				setLabel(ro.getJSONObject("entity").getString("label"));
				if (!ro.getJSONObject("entity").isNull("provider"))
					setProvider(ro.getJSONObject("entity").getString("provider"));
				setPlan(servicePlanEntity.getString("name"));
			}

		} 
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public boolean isUserProvided() {
		return plan == null && provider == null && version == null;
	}

	public String getVersion() {
		return version;
	}

	public String getLabel() {
		return label;
	}

	public String getProvider() {
		return provider;
	}

	public String getPlan() {
		return plan;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}
}
