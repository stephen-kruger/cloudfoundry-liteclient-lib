package org.cloudfoundry.client.lib.domain;

import org.cloudfoundry.client.compat.util.Utils;
import org.json.JSONObject;


public class CloudExtra extends JSONObject {
	
	public CloudExtra(String s) {
//		   "extra": "{\"displayName\":\"Push\",\"providerDisplayName\":\"IBM\",\"smallImageUrl\":\"https://mbaasbroker.ng.bluemix.net/images/push/push_small.png?version=1\",\"mediumImageUrl\":\"https://mbaasbroker.ng.bluemix.net/images/push/push_medium.png?version=1\",\"imageUrl\":\"https://mbaasbroker.ng.bluemix.net/images/push/push_large.png?version=1\",\"featuredImageUrl\":\"https://mbaasbroker.ng.bluemix.net/images/push/push_featured.png?version=1\",\"documentationUrl\":\"https://www.ng.bluemix.net/docs/Services/Push/Push.html\",\"instructionsUrl\":\"https://www.ng.bluemix.net/docs/Services/Push/Push.html\"}",
		super(s);
	}
	
	public String getDisplayName() {
		return Utils.safeGet(this, "displayName","");
	}
	
	public String getProviderDisplayName() {
		return Utils.safeGet(this, "providerDisplayName","");
	}
	
	public String getSmallImageUrl() {
		return Utils.safeGet(this, "smallImageUrl","");
	}
	
	public String getMediumImageUrl() {
		return Utils.safeGet(this, "mediumImageUrl","");
	}
	
	public String getImageUrl() {
		return Utils.safeGet(this, "imageUrl","");
	}
}
