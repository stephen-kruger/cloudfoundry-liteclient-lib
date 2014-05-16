package org.cloudfoundry.client.ibmlib.util;


public class Assert {

	public static void notNull(Object object, String message) {
		if (object==null)
			throw new RuntimeException(message);		
	}

	public static void isTrue(boolean bool, String message) {
		if (!bool)
			throw new RuntimeException(message);		
	}

	public static void state(boolean b, String message) {
		if (!b)
			throw new RuntimeException(message);	
	}

}
