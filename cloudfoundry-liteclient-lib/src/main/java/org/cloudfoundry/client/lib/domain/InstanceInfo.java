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

//import org.cloudfoundry.client.lib.util.CloudUtil;

import java.util.Date;

import org.json.JSONObject;

public class InstanceInfo {
	private Date since;
	private int index;
	private InstanceState state;
	private String debugIp=null, instanceState;
	private int debugPort=-1;

	public InstanceInfo(JSONObject infoMap) {
		since = new Date(infoMap.getLong("since") * 1000);
		index = infoMap.getInt("index");
		instanceState = infoMap.getString("state");
		state = InstanceState.valueOfWithDefault(instanceState);
		if (!infoMap.isNull("debug_ip"))
			debugIp = infoMap.getString("debug_ip");
		if (!infoMap.isNull("debug_port"))
			debugPort = infoMap.getInt("debug_port");
	}

	public Date getSince() {
		return since;
	}

	public int getIndex() {
		return index;
	}

	public InstanceState getState() {
		return state;
	}

	public String getDebugIp() {
		return debugIp;
	}

	public int getDebugPort() {
		return debugPort;
	}
}
