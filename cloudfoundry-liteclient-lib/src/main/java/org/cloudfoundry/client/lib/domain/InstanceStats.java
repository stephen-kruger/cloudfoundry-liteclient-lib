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

//import static org.cloudfoundry.client.lib.util.CloudUtil.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cloudfoundry.client.ibmlib.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class InstanceStats {

	public static class Usage {

		private double cpu;
		private int disk;
		private double mem;
		private Date time;

		public Usage(JSONObject attributes) {
			this.time = ResponseObject.parseDate(attributes.getString("time"));
			this.cpu = attributes.getDouble("cpu");
			this.disk = attributes.getInt("disk");
			this.mem = attributes.getDouble("mem");
		}

		public double getCpu() {
			return cpu;
		}

		public int getDisk() {
			return disk;
		}

		public double getMem() {
			return mem;
		}

		public Date getTime() {
			return time;
		}
	}

	private int cores;
	private long diskQuota;
	private int fdsQuota;
	private String host;
	private String id;
	private long memQuota;
	private String name;
	private int port;
	private InstanceState state;
	private double uptime;
	private List<String> uris;
	private Usage usage;

	public InstanceStats(String id, JSONObject attributes) {
		this.id = id;
		String instanceState = attributes.getString("state");
		this.state = InstanceState.valueOfWithDefault(instanceState);
		if (attributes.has("stats")) {
			JSONObject stats = attributes.getJSONObject("stats");
			if (stats.has("cores"))
				this.cores = stats.getInt("cores");
			this.name = stats.getString("name");
			if (stats.has("usage")) {
				JSONObject usageValue = stats.getJSONObject("usage");
				this.usage = new Usage(usageValue);
			}
			this.diskQuota = stats.getLong("disk_quota");
			this.port = stats.getInt("port");
			this.memQuota = stats.getLong("mem_quota");
			if (stats.has("uris")) {
				List<String> statsValue = new ArrayList<String>();
				JSONArray uriarray = stats.getJSONArray("uris");
				for (int i = 0; i < uriarray.length();i++) {
					statsValue.add(uriarray.getString(i));
				}
			}
			this.fdsQuota = stats.getInt("fds_quota");
			this.host = stats.getString("host");
			this.uptime = stats.getDouble("uptime");
		}
	}

	public int getCores() {
		return cores;
	}

	public long getDiskQuota() {
		return diskQuota;
	}

	public int getFdsQuota() {
		return fdsQuota;
	}

	public String getHost() {
		return host;
	}

	public String getId() {
		return id;
	}

	public long getMemQuota() {
		return memQuota;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public InstanceState getState() {
		return state;
	}

	public double getUptime() {
		return uptime;
	}

	public List<String> getUris() {
		return uris;
	}

	public Usage getUsage() {
		return usage;
	}
}
