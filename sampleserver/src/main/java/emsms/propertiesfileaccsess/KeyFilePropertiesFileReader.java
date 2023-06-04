package emsms.propertiesfileaccsess;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class KeyFilePropertiesFileReader {
	private static Properties keyProp = null;
	private static KeyFilePropertiesFileReader kf = new KeyFilePropertiesFileReader();
	
	private KeyFilePropertiesFileReader() {
		keyProp = new Properties();
		String keyPropPath = "conf/key.properties";
		try {
			FileInputStream fs = new FileInputStream(new File(keyPropPath));
			keyProp.load(fs);
		} catch(Exception e) {
			
		}
	}
	
	public static String getKeyValue(String key) {
		return keyProp.getProperty(key);
	}

}
