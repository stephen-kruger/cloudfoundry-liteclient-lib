package org.cloudfoundry.client.eventlib.listeners;

import java.util.List;

import org.cloudfoundry.client.lib.domain.CloudApplication;

public abstract class ApplicationListener extends GenericListener {

	/*
	 * The OAuth token associated with this logged in session, or null if not yet logged in
	 */
	public abstract void applications(List<CloudApplication> applications);
	
}
