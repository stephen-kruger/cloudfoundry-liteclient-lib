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
import org.cloudfoundry.client.eventlib.listeners.CloudInfoListener;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudInfo;

public class EventTest extends TestCase {
	private static Logger log = Logger.getAnonymousLogger();

	String target;
	String user;
	String password;
	CloudFoundryEventClient cfc;
	Thread ticker;
	private boolean loggedIn = false;
	private CloudInfo cloudInfo;

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
		Runnable tickerThread = new Runnable() {

			@Override
			public void run() {
				while (true) {
					cfc.tick();
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}};
			ticker = new Thread(tickerThread);
			ticker.start();

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
				loggedIn  = true;
			}

			@Override
			public void loggedOut() {
				loggedIn = false;
				log.info("Logout notification received");
			}

			@Override
			public void cloudFoundryException(CloudFoundryException e) {
				log.severe(e.getMessage());
			}

		};
		cfc.addAuthenticationListener(al);
		cfc.login();
		int maxWait = 5;
		while ((loggedIn==false)&&(maxWait-- > 0)) {
			log.info("Waiting for login to process "+maxWait);
			sleepyMe(2000);
		}
		assertTrue("Login failed",maxWait>0);
		cfc.logout();
		maxWait = 5;
		while ((loggedIn==true)&&(maxWait-- > 0)) {
			log.fine("Waiting for logout to process "+maxWait);
			sleepyMe(2000);
		}
		assertTrue("Logout failed",maxWait>0);
		cfc.removeAuthenticationListener(al);
	}

	public void testApplicationList() {
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

	public void testCloudInfo() {
		CloudInfoListener cil = new CloudInfoListener() {

			@Override
			public void cloudInfo(CloudInfo ci) {
				cloudInfo = ci;				
			}

			@Override
			public void cloudFoundryException(CloudFoundryException e) {
				log.severe(e.getMessage());				
			}

		};
		cfc.addCloudInfoListener(cil);
		cfc.login();
		cfc.cloudInfo();
		int maxWait = 10;
		while ((cloudInfo==null)&&(maxWait-- > 0)) {
			log.info("Waiting for cloudInfo "+maxWait);
			sleepyMe(2000);
		}
		assertNotNull("get CloudInfo failed",cloudInfo);		
	}

	public void sleepyMe(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}