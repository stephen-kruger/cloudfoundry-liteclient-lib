package org.cloudfoundry.client.eventlib.listeners;

import org.cloudfoundry.client.compat.OAuth2AccessToken;

public abstract class AuthenticationListener extends GenericListener{

	/*
	 * The OAuth token associated with this logged in session, or null if not yet logged in
	 */
	public abstract void loggedIn(OAuth2AccessToken token);
	
	/*
	 * The exception associated with this logout, or null if the user requested logout.
	 */
	public abstract void loggedOut();
	
}
