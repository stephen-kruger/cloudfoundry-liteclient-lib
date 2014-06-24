package org.cloudfoundry.client.eventlib.listeners;

import org.cloudfoundry.client.lib.CloudFoundryException;

public abstract class GenericListener {
	
	public abstract void cloudFoundryException(CloudFoundryException e);
}
