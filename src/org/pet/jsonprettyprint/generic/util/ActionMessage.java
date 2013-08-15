package org.pet.jsonprettyprint.generic.util;

import java.util.Properties;

public class ActionMessage {
	private Properties props;

	private ActionMessage(String filename) {
		props = PropertiesUtil.loadFromClassPath(filename, ActionMessage.class);
	}

	public static final ActionMessage getInstance(String filename) {
		return new ActionMessage(filename);
	}

	public String getMessage(String key, String defaultMsg) {
		return props.getProperty(key, defaultMsg);
	}
}
