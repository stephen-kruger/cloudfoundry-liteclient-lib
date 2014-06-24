package org.cloudfoundry.client.eventlib.listeners;

import org.cloudfoundry.client.lib.domain.CloudInfo;

public abstract class CloudInfoListener extends GenericListener {

	/*
	 * The OAuth token associated with this logged in session, or null if not yet logged in
	 */
	public abstract void cloudInfo(CloudInfo cloudInfo);
	
}
