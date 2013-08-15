package org.pet.jsonprettyprint.generic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class Description	: 
 * Created By			: Khairul Ikhwan
 * Created Date			: Jul 5, 2013
 * Current Version		: 1.0
 * Latest Changes By	: 
 * Latest Changes Date	: 
 */
public class PropertiesUtil {

	public static final Properties loadFromClassPath(String path, Class<?> clazz) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = clazz.getResourceAsStream(path);
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
	
}
