package com.aarete.pi.metadata.constant;

public class MetaDataConstants {

	private MetaDataConstants() {
	}

	public static final String USER_CONFIG_TABLE = "PI-UserConfigMetaData";

	static public final String PERSONA_CONFIG_TABLE = "PI-PersonaConfigUI";

	static public final String PAGE_CONFIG_TABLE = "PI-PageConfigUI";
	
	static public final String KEY_USER_ID = "userId";

	static public final String PAYLOAD_NAME = "name";

	static public final String PAYLOAD_ROLES = "roles";

	static public final String PAYLOAD_PREFERRED_USERNAME = "preferred_username";
	
	//## Logging related constants
	public static final String LOGGING_MDC_USER ="user";
	public static final String LOGGING_MDC_USER_ID ="user-id";
	public static final String LOGGING_MDC_FUNCTIONALITY_HEAD ="functionality-head";
	public static final String LOGGING_MDC_FUNCTIONALITY_SUB_HEAD ="functionality-subhead";
}
