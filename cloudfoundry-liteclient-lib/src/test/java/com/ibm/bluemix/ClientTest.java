package com.ibm.bluemix;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.Staging;

public class ClientTest extends TestCase {

	String target = "https://api.ng.bluemix.net";
	String user = "xxxxxxx";
	String password = "xxxxxxxxxxxxxx";
	CloudFoundryClient cfc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cfc = new CloudFoundryClient(new CloudCredentials(user,password), new URL(target));
		cfc.login();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		cfc.logout();
	}

	public void testApps() {
		// create an app
		String appName = "JUnit Test App";
		Staging staging = new Staging();
		Integer memory = Integer.valueOf(1024);
		List<String> uris = new ArrayList<String>();
		List<String> serviceNames = new ArrayList<String>();
		cfc.createApplication(appName, staging, memory, uris, serviceNames);
		// list the app
		// delete the app
	}

	public void testConnectviaClient() throws Throwable {
		//		for (CloudOrganization c : cfc.getOrganizations()) {
		//			System.out.println(c.toString());
		//		}
		//
		//		List<CloudApplication> applications = cfc.getApplications();
		//		System.out.println("------here we go "+applications.size());
		//		for (CloudApplication s : applications) {
		//			System.out.println("Application:"+s.getName());
		//			//			System.out.println(s.getUris());
		//		}

		List<CloudService> services = cfc.getServices();
		for (CloudService s : services) {
			System.out.println("1:"+s.toString());
			System.out.println("2:"+cfc.getService(s.getName()));
		}
	}

	public void testConnect() throws Throwable {
		//		System.setProperty("socksProxyHost", "127.0.0.1");
		//        System.setProperty("socksProxyPort", "442");
		//		System.setProperty("http.proxyHost", "127.0.0.1");
		//		System.setProperty("http.proxyPort", "8118");
		//		System.setProperty("https.proxyHost", "127.0.0.1");
		//		System.setProperty("https.proxyPort", "8118");
		//		System.setProperty("http.proxySet", "true");
		//		System.setProperty("https.proxySet", "true");

		CloudCredentials credentials = new CloudCredentials(user, password);
		CloudFoundryClient client = new CloudFoundryClient(credentials, getTargetURL(target));
		client.login();
		System.out.printf("%nSpaces:%n");
		for (CloudSpace space : client.getSpaces()) {
			System.out.printf("  name=%s\t org=(%s)%n", space.getName(), space.getOrganization().getName());
		}

		//        System.out.printf("%nApplications:%n");
		//        for (CloudApplication application : client.getApplications()) {
		//            System.out.printf("APPLICATION:  %s %s %n", application.getName(), application.getState().name());
		//            System.out.printf("%nServices:%n");
		//            List<String>services = application.getServices();
		//            for (String sv : services) {
		//            	System.out.printf("  Service %s %n", sv);
		//            }
		//        }
		//
		System.out.printf("%nServices%n");
		for (CloudService service : client.getServices()) {
			System.out.printf("  %s\t(%s)%n", service.getName(), service.getLabel());
		}

	}

	private static URL getTargetURL(String target) {
		try {
			return URI.create(target).toURL();
		} 
		catch (MalformedURLException e) {
			throw new RuntimeException("The target URL is not valid: " + e.getMessage());
		}
	}
}