package org.cloudfoundry.client.eventlib;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.client.compat.OAuth2AccessToken;
import org.cloudfoundry.client.eventlib.listeners.ApplicationListener;
import org.cloudfoundry.client.eventlib.listeners.AuthenticationListener;
import org.cloudfoundry.client.eventlib.listeners.CloudInfoListener;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;

public class CloudFoundryEventClient {

	private List<AuthenticationListener> authenticationListeners = new ArrayList<AuthenticationListener>();
	private List<CloudInfoListener> cloudInfoListeners = new ArrayList<CloudInfoListener>();
	private CloudFoundryClient cfc;
	private List<ApplicationListener> applicationListeners = new ArrayList<ApplicationListener>();
	private List<CloudApplication> applicationCache = new ArrayList<CloudApplication>();


	public CloudFoundryEventClient(CloudCredentials credentials, URL cloudControllerUrl) {
		cfc = new CloudFoundryClient(credentials, cloudControllerUrl);
	}

	public void addAuthenticationListener(AuthenticationListener al) {
		// don't add duplicates
		removeAuthenticationListener(al);
		authenticationListeners.add(al);
		// now send them the login token
		al.loggedIn(cfc.getToken());
	}

	public void removeAuthenticationListener(AuthenticationListener al) {
		authenticationListeners.remove(al);
	}

	public void addApplicationListener(ApplicationListener al) {
		// don't add duplicates
		removeApplicationListener(al);
		applicationListeners.add(al);
		try {
			if (applicationCache==null) {
				applicationCache = cfc.getApplications();
			}
			// now send them the latest applications
			al.applications(applicationCache);
		} 
		catch (CloudFoundryException cfe) {
			al.cloudFoundryException(cfe);
		}
	}

	public void removeApplicationListener(ApplicationListener al) {
		applicationListeners.remove(al);
	}

	public void addCloudInfoListener(CloudInfoListener cl) {
		// don't add duplicates
		removeCloudInfoListener(cl);
		cloudInfoListeners.add(cl);
		// now send them the latest CloudInfo object
		try {
			cl.cloudInfo(cfc.getCloudInfo());
		} 
		catch (CloudFoundryException cfe) {
			cl.cloudFoundryException(cfe);
		}
	}

	public void removeCloudInfoListener(CloudInfoListener cl) {
		authenticationListeners.remove(cl);
	}

	public void login() {
		try {
			OAuth2AccessToken token = cfc.login();
			for (AuthenticationListener al : authenticationListeners) {
				al.loggedIn(token);
			}
		}
		catch (CloudFoundryException cfe) {
			for (AuthenticationListener al : authenticationListeners) {
				al.cloudFoundryException(cfe);
				al.loggedOut();
			}	
		}
	}

	public void logout() {
		cfc.logout();for (AuthenticationListener al : authenticationListeners) {
			al.loggedOut();
		}	
	}
	
	/* 
	 * Calling this method clears all the locally cached objects, any subsequent calls will result in reloading
	 * of objects over the network.
	 */
	public void clearCache() {
		applicationCache.clear();
	}
}
