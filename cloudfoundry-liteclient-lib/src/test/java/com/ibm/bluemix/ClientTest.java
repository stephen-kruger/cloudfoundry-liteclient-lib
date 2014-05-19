package com.ibm.bluemix;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudInfo.Limits;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.json.JSONException;

public class ClientTest extends TestCase {
	private static Logger log = Logger.getAnonymousLogger();

	String target;
	String user;
	String password;
	CloudFoundryClient cfc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Properties p = new Properties();
		p.load(new FileInputStream("src/test/resources/creds.properties"));
		user = p.getProperty("user");
		password = p.getProperty("passwd");
		target = p.getProperty("base");
		
		cfc = new CloudFoundryClient(new CloudCredentials(user,password), new URL(target));
		cfc.login();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		cfc.logout();
	}

	public void testSpaces() {
		List<CloudSpace> spaces = cfc.getSpaces();
		assertTrue("No spaces found",spaces.size()>0);
		log.info("Found "+spaces.size()+" spaces");
		for (CloudSpace space : spaces) {
			log.info("Space 1:"+space.toString());
		}
	}
	
	public void testDomains() {
//		List<CloudDomain> domains = cfc.getDomains();
//		assertTrue("No domains found",domains.size()>0);
//		log.info("Found "+domains.size()+" domains");
//		for (CloudDomain domain : domains) {
//			log.info("Domain 1:"+domain.toString());
//		}
		
		// test create
		cfc.addDomain("xxx");
	}
	
	public void testOrganisations() {
		List<CloudOrganization> orgs = cfc.getOrganizations();
		assertTrue("No organisations found",orgs.size()>0);
		log.info("Found "+orgs.size()+" domains");
		for (CloudOrganization org : orgs) {
			log.info("Organization 1:"+org.toString());
			// now check the domains
			List<CloudDomain> domains = cfc.getDomainsForOrg(org);
			log.info(" Domains:"+domains.size());
		}
		
	}
	
	public void testCloudInfo() throws JSONException, IllegalStateException, IOException, URISyntaxException {
		log.info("==========================");
		CloudInfo cloudInfo = cfc.getCloudInfo();
		Limits limits = cloudInfo.getLimits();
		log.info("MaxTotalMemory:"+limits.getMaxTotalMemory());
		log.info("MaxApps:"+limits.getMaxApps());
		log.info("MaxServices:"+limits.getMaxServices());
		log.info("MaxUrisPerApp:"+limits.getMaxUrisPerApp());

	}
	
	public void testApps() throws CloudFoundryException {	
//INFO: App 1:CloudApplication [staging=Staging [command=null buildpack=null stack=lucid64 healthCheckTimeout=null], instances=1, name=app1, memory=512, diskQuota=1024, state=STARTED, debug=null, uris=[bb.ng.bluemix.net],services=[service 1.1, service 1.2], env=[]]
//INFO: App 1:CloudApplication [staging=Staging [command=null buildpack=null stack=lucid64 healthCheckTimeout=null], instances=1, name=app1, memory=512, diskQuota=1024, state=STARTED, debug=suspend, uris=[bb.ng.bluemix.net],services=null, env=[]]
				
		List<CloudApplication> apps = cfc.getApplications();
		assertTrue("No apps found",apps.size()>0);
		log.info("Found "+apps.size()+" apps");
		for (CloudApplication app : apps) {
			log.info("App 1:"+app.toString());
			String fwk = (String) app.getStaging().getStack();//.get("model");
			assertNotNull(app.getStaging());
			assertNotNull(cfc.getApplicationStats(app.getName()));
			cfc.restartApplication(app.getName());
		}
		
		// create an app
//		String appName = "JUnit Test App";
//		Staging staging = new Staging();
//		Integer memory = Integer.valueOf(1024);
//		List<String> uris = new ArrayList<String>();
//		List<String> serviceNames = new ArrayList<String>();
//		cfc.createApplication(appName, staging, memory, uris, serviceNames);
		// list the app
		// delete the app
	}

	public void testServices() throws Throwable {
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
//			>>>mongodb
//			>>>BLUAcceleration
//			>>>TimeSeriesDatabase
//			>>>InternetOfThings
			assertNotNull("Service name was null",s.getName());
			assertNotNull("Service label was null",s.getLabel());
			System.out.println(">>>"+s.getProvider());
//			assertNotNull("Provider was null",s.getProvider());
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