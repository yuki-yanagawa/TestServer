package emsms.propertiesfileaccsess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * conf\commn.properties read
 * Called contructor then properties file read
 *
 */
public class CommonPropertiesFileReader {
	private Properties prop = null;
	public CommonPropertiesFileReader() {
		prop = new Properties();
		String commnpropPath = "conf/common.properties";
		try {
			FileInputStream fs = new FileInputStream(new File(commnpropPath));
			prop.load(fs);
		} catch(FileNotFoundException fe) {
			
		} catch(IOException e) {
			
		}
	}
	public String getPropertiesValue(String key) {
		return prop.getProperty(key);
	}

}
