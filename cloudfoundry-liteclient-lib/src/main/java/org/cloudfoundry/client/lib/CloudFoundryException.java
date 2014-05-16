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

import org.apache.http.HttpStatus;
//import org.springframework.web.client.HttpClientErrorException;

@SuppressWarnings("serial")
public class CloudFoundryException extends Exception {


	private String description;
	
	private int cloudFoundryErrorCode = -1;

	private int statusCode;

	public CloudFoundryException(int statusCode) {
		this(statusCode,"");
	}

	public CloudFoundryException(int statusCode, String statusText) {
		this(statusCode, statusText,0);
	}
	
	public CloudFoundryException(int statusCode, String statusText, int cloudFoundryErrorCode) {
		super(statusText);
		this.statusCode = statusCode;
		this.cloudFoundryErrorCode = cloudFoundryErrorCode;
	}

	/**
	 * Construct a new instance of {@code CloudFoundryException} based on a {@link HttpStatus}, status text and description.
	 * @param statusCode the status code
	 * @param statusText the status text
	 * @param description the description
	 */
	public CloudFoundryException(int statusCode, String statusText, String description) {
		this(statusCode, statusText);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns an additional error code that is specific to failures in Cloud Foundry requests or behaviour.
	 * @return Cloud Foundry error code, if available, or -1 if unknown.
	 */
	public int getCloudFoundryErrorCode() {
		return cloudFoundryErrorCode;
	}

	@Override
	public String toString() {
		if (description != null) {
			return super.toString() + " (" + description + ")";
		}
		return super.toString();
	}

	public int getStatusCode() {
		// TODO Auto-generated method stub
		return statusCode;
	}

}
