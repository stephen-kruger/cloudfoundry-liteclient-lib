/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.cloudfoundry.client.compat.OAuth2AccessToken;
//import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudRoute;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudStack;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.Staging;
//import org.cloudfoundry.client.ibmlib.OAuth2AccessToken;
//import org.cloudfoundry.client.ibmlib.ResponseErrorHandler;
import org.json.JSONException;

/**
 * The interface defining operations making up the Cloud Foundry Java client's API.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 */
public interface CloudFoundryOperations {

	/**
	 * Override the default REST response error handler with a custom error handler.
	 *
	 * @param errorHandler
	 */
//	void setResponseErrorHandler(ResponseErrorHandler errorHandler);

	/**
	 * Get the URL used for the cloud controller.
	 *
	 * @return the cloud controller URL
	 */
	URL getCloudControllerUrl();

	/**
	 * Get CloudInfo for the current cloud.
	 *
	 * @return CloudInfo object containing the cloud info
	 * @throws CloudFoundryException 
	 */
	CloudInfo getCloudInfo() throws CloudFoundryException;

	/**
	 * Get list of CloudSpaces for the current cloud.
	 *
	 * @return List of CloudSpace objects containing the space info
	 */
	List<CloudSpace> getSpaces();

	/**
	 * Get list of CloudOrganizations for the current cloud.
	 *
	 * @return List of CloudOrganizations objects containing the organization info
	 */
	List<CloudOrganization> getOrganizations();

	/**
	 * Register new user account with the provided credentials.
	 *
	 * @param email the email account
	 * @param password the password
	 */
	void register(String email, String password);

	/**
	 * Update the password for the logged in user.
	 *
	 * @param newPassword the new password
	 */
	void updatePassword(String newPassword);

	/**
	 * Update the password for the logged in user using
	 * the username/old_password provided in the credentials.
	 *
	 * @param credentials current credentials
	 * @param newPassword the new password
	 */
	void updatePassword(CloudCredentials credentials, String newPassword);

	/**
	 * Unregister and log out the currently logged in user
	 */
	void unregister();

	/**
	 * Login using the credentials already set for the client.
	 *
	 * @return authentication token
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 * @throws CloudFoundryException 
	 */
	OAuth2AccessToken login() throws CloudFoundryException;

	/**
	 * Logout closing the current session.
	 */
	void logout();

	/**
	 * Get all cloud applications.
	 *
	 * @return list of cloud applications
	 * @throws CloudFoundryException 
	 */
	List<CloudApplication> getApplications() throws CloudFoundryException;

	/**
	 * Get cloud application with the specified name.
	 *
	 * @param appName name of the app
	 * @return the cloud application
	 * @throws CloudFoundryException 
	 */
	CloudApplication getApplication(String appName) throws CloudFoundryException;

	/**
	 * Get application stats for the app with the specified name.
	 *
	 * @param appName name of the app
	 * @return the cloud application stats
	 * @throws CloudFoundryException 
	 */
	ApplicationStats getApplicationStats(String appName) throws CloudFoundryException;

	/**
	 * Create application.
	 *
	 * @param appName application name
	 * @param staging staging info
	 * @param memory memory to use in MB
	 * @param uris list of URIs for the app
	 * @param serviceNames list of service names to bind to app
	 * @throws CloudFoundryException 
	 */
	void createApplication(String appName, Staging staging, Integer memory, List<String> uris,
                           List<String> serviceNames) throws CloudFoundryException;

	/**
	 * Create application.
	 *
	 * @param appName      application name
	 * @param staging      staging info
	 * @param disk         disk quota to use in MB
	 * @param memory       memory to use in MB
	 * @param uris         list of URIs for the app
	 * @param serviceNames list of service names to bind to app
	 * @throws CloudFoundryException 
	 */
	public void createApplication(String appName, Staging staging, Integer disk, Integer memory, List<String> uris,
	                              List<String> serviceNames) throws CloudFoundryException;

	/**
	 * Create a service.
	 *
	 * @param service cloud service info
	 * @throws CloudFoundryException 
	 */
	void createService(CloudService service) throws CloudFoundryException;

	/**
	 * Create a user-provided service.
	 *
	 * @param service cloud service info
	 * @param credentials the user-provided service credentials
	 */
	void createUserProvidedService(CloudService service, Map<String, Object> credentials);

	/**
	 * Upload an application.
	 *
	 * @param appName application name
	 * @param file path to the application archive or folder
	 * @throws java.io.IOException
	 */
	void uploadApplication(String appName, String file) throws IOException;

	/**
	 * Upload an application to cloud foundry.
	 * @param appName the application name
	 * @param file the application archive or folder
	 * @throws java.io.IOException
	 */
	void uploadApplication(String appName, File file) throws IOException;

	/**
	 * Upload an application to cloud foundry.
	 * @param appName the application name
	 * @param file the application archive
	 * @param callback a callback interface used to provide progress information or <tt>null</tt>
	 * @throws java.io.IOException
	 */
//	void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException;

	/**
	 * Upload an application to cloud foundry.
	 * @param appName the application name
	 * @param archive the application archive
	 * @throws java.io.IOException
	 */
//	void uploadApplication(String appName, ApplicationArchive archive) throws IOException;

	/**
	 * Upload an application to cloud foundry.
	 * @param appName the application name
	 * @param archive the application archive
	 * @param callback a callback interface used to provide progress information or <tt>null</tt>
	 * @throws java.io.IOException
	 */
//	void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException;

	/**
	 * Start application. May return starting info if the response obtained after the start request contains headers.
	 * If the response does not contain headers, null is returned instead.
	 *
	 * @param appName
	 *            name of application
	 * @return Starting info containing response headers, if headers are present in the response. If there are no headers, return null.
	 * @throws CloudFoundryException 
	 */
	StartingInfo startApplication(String appName) throws CloudFoundryException;

	/**
	 * Debug application.
	 *
	 * @param appName name of application
	 * @param mode debug mode info
	 */
	void debugApplication(String appName, CloudApplication.DebugMode mode);

	/**
	 * Stop application.
	 *
	 * @param appName name of application
	 * @throws CloudFoundryException 
	 */
	void stopApplication(String appName) throws CloudFoundryException;

	/**
	 * Restart application.
	 *
	 * @param appName name of application
	 * @throws CloudFoundryException 
	 */
	StartingInfo restartApplication(String appName) throws CloudFoundryException;

	/**
	 * Delete application.
	 *
	 * @param appName name of application
	 * @throws CloudFoundryException 
	 */
	void deleteApplication(String appName) throws CloudFoundryException;

	/**
	 * Delete all applications.
	 * @throws CloudFoundryException 
	 */
	void deleteAllApplications() throws CloudFoundryException;

	/**
	 * Delete all services.
	 * @throws CloudFoundryException 
	 */
	void deleteAllServices() throws CloudFoundryException;

	/**
	 * Update application disk quota.
	 *
	 * @param appName name of application
	 * @param disk new disk setting in MB
	 * @throws CloudFoundryException 
	 */
	void updateApplicationDiskQuota(String appName, int disk) throws CloudFoundryException;

	/**
	 * Update application memory.
	 *
	 * @param appName name of application
	 * @param memory new memory setting in MB
	 * @throws CloudFoundryException 
	 */
	void updateApplicationMemory(String appName, int memory) throws CloudFoundryException;

	/**
	 * Update application instances.
	 *
	 * @param appName name of application
	 * @param instances number of instances to use
	 * @throws CloudFoundryException 
	 */
	void updateApplicationInstances(String appName, int instances) throws CloudFoundryException;

	/**
	 * Update application services.
	 *
	 * @param appName name of appplication
	 * @param services list of services that should be bound to app
	 * @throws CloudFoundryException 
	 */
	void updateApplicationServices(String appName, List<String> services) throws CloudFoundryException;

	/**
	 * Update application staging information.
	 *
	 * @param appName name of appplication
	 * @param staging staging information for the app
	 * @throws CloudFoundryException 
	 */
	void updateApplicationStaging(String appName, Staging staging) throws CloudFoundryException;

	/**
	 * Update application URIs.
	 *
	 * @param appName name of application
	 * @param uris list of URIs the app should use
	 * @throws CloudFoundryException 
	 */
	void updateApplicationUris(String appName, List<String> uris) throws CloudFoundryException;

	/**
	 * Update application env using a map where the key specifies the name of the environment variable
	 * and the value the value of the environment variable..
	 *
	 * @param appName name of application
	 * @param env map of environment settings
	 * @throws CloudFoundryException 
	 */
	void updateApplicationEnv(String appName, Map<String, String> env) throws CloudFoundryException;

	/**
	 * Update application env using a list of strings each with one environment setting.
	 *
	 * @param appName name of application
	 * @param env list of environment settings
	 * @throws CloudFoundryException 
	 */
	void updateApplicationEnv(String appName, List<String> env) throws CloudFoundryException;


	/**
	 * Get logs from the deployed application. The logs
	 * will be returned in a Map keyed by the path of the log file
	 * (logs/stderr.log, logs/stdout.log).
	 * @param appName name of the application
	 * @return a Map containing the logs. The logs will be returned with the path to the log file used as the key and
	 * the full content of the log file will be returned as a String value for the corresponding key.
	 * @throws CloudFoundryException 
	 * @deprecated Use {@link #streamLogs(String, ApplicationLogListener)} or {@link #streamRecentLogs(String, ApplicationLogListener)}
	 */
	Map<String, String> getLogs(String appName) throws CloudFoundryException;
	
	/**
	 * Stream application logs produced <em>after</em> this method is called.
	 * 
	 * This method has 'tail'-like behavior. Every time there is a new log entry,
	 * it notifies the listener.
	 * 
	 * @param appName the name of the application
	 * @param listener listener object to be notified
	 * @return token than can be used to cancel listening for logs
	 */
//	StreamingLogToken streamLogs(String appName, ApplicationLogListener listener);
	
	/**
	 * Stream recent log entries.
	 * 
	 * Stream logs that were recently produced for an app. Once recent log entries have been notified,
	 * the listener will not be notified any more.
	 * 
     * @param appName the name of the application
     * @param listener listener object to be notified
     * @return token than can be used to cancel listening for logs
	 */
//	StreamingLogToken streamRecentLogs(String appName, ApplicationLogListener listener);

	/**
	 * Get logs from most recent crash of the deployed application. The logs
	 * will be returned in a Map keyed by the path of the log file
	 * (logs/stderr.log, logs/stdout.log).
	 *
	 * @param appName name of the application
	 * @return a Map containing the logs. The logs will be returned with the path to the log file used as the key and
	 * the full content of the log file will be returned as a String value for the corresponding key.
	 * @throws CloudFoundryException 
	 */
	Map<String, String> getCrashLogs(String appName) throws CloudFoundryException;
	
	/**
	 * Get the staging log while an application is starting. A null
	 * value indicates that no further checks for staging logs should occur as
	 * staging logs are no longer available.
	 * 
	 * @param info
	 *            starting information containing staging log file URL. Obtained
	 *            after starting an application.
	 * @param offset
	 *            starting position from where content should be retrieved.
	 * @return portion of the staging log content starting from the offset. It
	 *         may contain multiple lines. Returns null if no further content is
	 *         available.
	 * @throws CloudFoundryException 
	 */
	String getStagingLogs(StartingInfo info, int offset) throws CloudFoundryException;


	/**
	 * Get the list of stacks available for staging applications.
	 *
	 * @return the list of available stacks
	 * @throws CloudFoundryException 
	 */
	List<CloudStack> getStacks() throws CloudFoundryException;

	/**
	 * Get a stack by name.
	 *
	 * @param name the name of the stack to get
	 * @return the stack, or null if not found
	 * @throws CloudFoundryException 
	 */
	CloudStack getStack(String name) throws CloudFoundryException;

	/**
	 * Get file from the deployed application.
	 *
	 * @param appName name of the application
	 * @param instanceIndex instance index
	 * @param filePath path to the file
	 * @return the contents of the file
	 * @throws CloudFoundryException 
	 */
	String getFile(String appName, int instanceIndex, String filePath) throws CloudFoundryException;

	/**
	 * Get a the content, starting at a specific position, of a file from the deployed application.
	 *
	 * @param appName name of the application
	 * @param instanceIndex instance index
	 * @param filePath path to the file
	 * @param startPosition the starting position of the file contents (inclusive)
	 * @return the contents of the file
	 * @throws CloudFoundryException 
	 */
	String getFile(String appName, int instanceIndex, String filePath, int startPosition) throws CloudFoundryException;

	/**
	 * Get a range of content of a file from the deployed application. The range begins at the specified startPosition
	 * and extends to the character at endPosition - 1.
	 *
	 * @param appName name of the application
	 * @param instanceIndex instance index
	 * @param filePath path to the file
	 * @param startPosition the starting position of the file contents (inclusive)
	 * @param endPosition the ending position of the file contents (exclusive)
	 * @return the contents of the file
	 * @throws CloudFoundryException 
	 */
	String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition) throws CloudFoundryException;

	/**
	 * Get a the last bytes, with length as specified, of content of a file from the deployed application.
	 *
	 * @param appName name of the application
	 * @param instanceIndex instance index
	 * @param filePath path to the file
	 * @param length the length of the file contents to retrieve
	 * @return the contents of the file
	 * @throws CloudFoundryException 
	 */
	String getFileTail(String appName, int instanceIndex, String filePath, int length) throws CloudFoundryException;

	/**
	 * Get list of cloud services.
	 *
	 * @return list of cloud services
	 */
	List<CloudService> getServices();

	/**
	 * Get cloud service.
	 *
	 * @param service name of service
	 * @return the cloud service info
	 */
	CloudService getService(String service);

	/**
	 * Delete cloud service.
	 *
	 * @param service name of service
	 * @throws CloudFoundryException 
	 */
	void deleteService(String service) throws CloudFoundryException;

	/**
	 * Get all service offerings.
	 *
	 * @return list of service offerings
	 * @throws CloudFoundryException 
	 */
	List<CloudServiceOffering> getServiceOfferings() throws CloudFoundryException;

	/**
	 * Associate (provision) a service with an application.
	 *
	 * @param appName the application name
	 * @param serviceName the service name
	 * @throws CloudFoundryException 
	 */
	void bindService(String appName, String serviceName) throws CloudFoundryException;

	/**
	 * Un-associate (unprovision) a service from an application.
	 * @param appName the application name
	 * @param serviceName the service name
	 * @throws CloudFoundryException 
	 */
	void unbindService(String appName, String serviceName) throws CloudFoundryException;

	/**
	 * Get application instances info for application.
	 *
	 * @param appName name of application.
	 * @return instances info
	 * @throws CloudFoundryException 
	 */
	InstancesInfo getApplicationInstances(String appName) throws CloudFoundryException;

	/**
	 * Get application instances info for application.
	 *
	 * @param app the application.
	 * @return instances info
	 * @throws CloudFoundryException 
	 */
	InstancesInfo getApplicationInstances(CloudApplication app) throws CloudFoundryException;

	/**
	 * Get crashes info for application.
	 * @param appName name of application
	 * @return crashes info
	 */
	CrashesInfo getCrashes(String appName);

	/**
	 * Rename an application.
	 *
	 * @param appName the current name
	 * @param newName the new name
	 * @throws CloudFoundryException 
	 */
	void rename(String appName, String newName) throws CloudFoundryException;

	/**
	 * Get list of all domain registered for the current organization.
	 *
	 * @return list of domains
	 */
	List<CloudDomain> getDomainsForOrg();

	/**
	 * Get list of all private domains.
	 *
	 * @return list of private domains
	 * @throws CloudFoundryException 
	 */
	List<CloudDomain> getPrivateDomains() throws CloudFoundryException;

	/**
	 * Get list of all shared domains.
	 *
	 * @return list of shared domains
	 * @throws CloudFoundryException 
	 */
	List<CloudDomain> getSharedDomains() throws CloudFoundryException;

	/**
	 * Get list of all domain shared and private domains.
	 *
	 * @return list of domains
	 */
	List<CloudDomain> getDomains();

	/**
	 * Add a private domain in the current organization.
	 *
	 * @param domainName the domain to add
	 * @throws CloudFoundryException 
	 */
	void addDomain(String domainName) throws CloudFoundryException;

	/**
	 * Delete a private domain in the current organization.
	 *
	 * @param domainName the domain to remove
	 * @deprecated alias for {@link #deleteDomain}
	 */
	void removeDomain(String domainName);

	/**
	 * Delete a private domain in the current organization.
	 *
	 * @param domainName the domain to delete
	 * @throws CloudFoundryException 
	 */
	void deleteDomain(String domainName) throws CloudFoundryException;

	/**
	 * Get the info for all routes for a domain.
	 *
	 * @param domainName the domain the routes belong to
	 * @return list of routes
	 */
	List<CloudRoute> getRoutes(String domainName);

	/**
	 * Register a new route to the a domain.
	 *
	 * @param host the host of the route to register
	 * @param domainName the domain of the route to register
	 * @throws CloudFoundryException 
	 */
	void addRoute(String host, String domainName) throws CloudFoundryException;

	/**
	 * Delete a registered route from the space of the current session.
	 *
	 * @param host the host of the route to delete
	 * @param domainName the domain of the route to delete
	 * @throws CloudFoundryException 
	 */
	void deleteRoute(String host, String domainName) throws CloudFoundryException;

	/**
	 * Register a new RestLogCallback
	 *
	 * @param callBack the callback to be registered
	 */
//	void registerRestLogListener(RestLogCallback callBack);

	/**
	 * Un-register a RestLogCallback
	 *
	 * @param callBack the callback to be un-registered
	 */
//	void unRegisterRestLogListener(RestLogCallback callBack);
}
