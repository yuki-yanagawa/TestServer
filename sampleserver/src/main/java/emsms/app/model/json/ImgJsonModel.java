package emsms.app.model.json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImgJsonModel {
	private String name;
	private String dataBase64;
	
	public ImgJsonModel(String name) {
		this.name = name;
		createFieldData(this.name);
	}

	private void createFieldData (String name) {
		if(name.matches("^img/.*")) {
//			name = "sampleserver/" + name;
			//pass
		} else if(name.matches("^/img/.*")) {
//			name = "sampleserver" + name;
			name = name.substring(1);
		} else if(name.matches("^/.*")) {
//			name = "sampleserver/img" + name;
			name = "img" + name; 
		} else {
//			name = "sampleserver/img/" + name;
			name = "img/" + name;
		}
		try (FileInputStream fi = new FileInputStream(name)) {
			long fileSize = Files.size(Paths.get(name));
			byte[] data = new byte[(int)fileSize];
			fi.read(data);
			setImgData(Base64.getEncoder().encodeToString(data));
		} catch (FileNotFoundException fe) {
			
		} catch (IOException ie) {
			
		}
	}
	
	private void setImgData(String data) {
		this.dataBase64 = data;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return dataBase64;
	}
	
	
}
