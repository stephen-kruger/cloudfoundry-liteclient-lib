package com.ibm.bluemix;

import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.cloudfoundry.client.compat.OAuth2AccessToken;
import org.cloudfoundry.client.eventlib.CloudFoundryEventClient;
import org.cloudfoundry.client.eventlib.listeners.ApplicationListener;
import org.cloudfoundry.client.eventlib.listeners.AuthenticationListener;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;

public class EventTest extends TestCase {
	private static Logger log = Logger.getAnonymousLogger();

	String target;
	String user;
	String password;
	CloudFoundryEventClient cfc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Properties p = new Properties();
		p.load(new FileInputStream("src/test/resources/creds.properties"));
		user = p.getProperty("user");
		password = p.getProperty("passwd");
		target = p.getProperty("base");

		cfc = new CloudFoundryEventClient(new CloudCredentials(user,password), new URL(target));
		cfc.login();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		cfc.logout();
	}

	public void testLoginLogout() {
		AuthenticationListener al = new AuthenticationListener() {

			@Override
			public void loggedIn(OAuth2AccessToken token) {
				assertNotNull(token);
				log.info("Login notification received");

			}

			@Override
			public void loggedOut() {
			}

			@Override
			public void cloudFoundryException(CloudFoundryException e) {

			}

		};
		cfc.addAuthenticationListener(al);
		cfc.removeAuthenticationListener(al);
	}

	public void testApplicationList() {
		int count = 0;
		ApplicationListener al = new ApplicationListener() {

			@Override
			public void applications(List<CloudApplication> applications) {
				for (CloudApplication ca : applications) {
					log.info(ca.getName());
				}
			}

			@Override
			public void cloudFoundryException(CloudFoundryException e) {
				// TODO Auto-generated method stub

			}

		};
		cfc.addApplicationListener(al);


		try {
			Thread.sleep(25000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}


}