package org.cloudfoundry.client.lib;

import org.cloudfoundry.client.ibmlib.OAuth2AccessToken;

public class CloudCredentials {

	private String email, password, clientId, clientSecret, proxyUser;
	private OAuth2AccessToken token;
	
	/**
	 * Create credentials using email and password.
	 *
	 * @param email email to authenticate with
	 * @param password the password
	 */
	public CloudCredentials(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Create credentials using email, password, and client ID.
	 *
	 * @param email email to authenticate with
	 * @param password the password
	 * @param clientId the client ID to use for authorization
	 */
	public CloudCredentials(String email, String password, String clientId) {
		this.email = email;
		this.password = password;
		this.clientId = clientId;
	}

	/**
	 * Create credentials using email, password and client ID.
	 * @param email email to authenticate with
	 * @param password the password
	 * @param clientId the client ID to use for authorization
	 * @param clientSecret the secret for the given client
	 */
	public CloudCredentials(String email, String password, String clientId, String clientSecret) {
		this.email = email;
		this.password = password;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	/**
	 * Create credentials using a token.
	 *
	 * @param token token to use for authorization
	 */
	public CloudCredentials(OAuth2AccessToken token) {
		this.token = token;
	}

	/**
	 * Create credentials using a token.
	 *
	 * @param token token to use for authorization
	 * @param clientId the client ID to use for authorization
	 */
	public CloudCredentials(OAuth2AccessToken token, String clientId) {
		this.token = token;
		this.clientId = clientId;
	}

	/**
	 * Create credentials using a token.
	 *
	 * @param token token to use for authorization
	 * @param clientId the client ID to use for authorization
	 * @param clientSecret the password for the specified client
	 */
	public CloudCredentials(OAuth2AccessToken token, String clientId, String clientSecret) {
		this.token = token;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	/**
	 * Create proxy credentials.
	 *
	 * @param cloudCredentials credentials to use
	 * @param proxyForUser user to be proxied
	 */
	public CloudCredentials(CloudCredentials cloudCredentials, String proxyForUser) {
		this.email = cloudCredentials.getEmail();
		this.password = cloudCredentials.getPassword();
		this.clientId = cloudCredentials.getClientId();
		this.token = cloudCredentials.getToken();
		this.proxyUser = proxyForUser;
	}

	/**
	 * Get the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get the token.
	 *
	 * @return the token
	 */
	public OAuth2AccessToken getToken() {
		return token;
	}

	/**
	 * Get the client ID.
	 *
	 * @return the client ID
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Get the client secret
	 *
	 * @return the client secret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Get the proxy user.
	 *
	 * @return the proxy user
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * Is this a proxied set of credentials?
	 *
	 * @return whether a proxy user is set
	 */
	public boolean isProxyUserSet()  {
		return proxyUser != null;
	}

	/**
	 * Run commands as a different user.  The authenticated user must be
	 * privileged to run as this user.

	 * @param user the user to proxy for
	 * @return credentials for the proxied user
	 */
	public CloudCredentials proxyForUser(String user) {
		return new CloudCredentials(this, user);
	}
}
