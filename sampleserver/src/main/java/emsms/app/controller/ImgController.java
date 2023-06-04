package emsms.app.controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import emsms.app.model.json.ImgJsonModel;
import emsms.propertiesfileaccsess.KeyFilePropertiesFileReader;

public class ImgController {
	public String getImgData(Map<String,Object> reqparam) {
		String ret = "";
		String imgNo = (String)reqparam.get(KeyFilePropertiesFileReader.getKeyValue("key.imgNo"));
		ImgJsonModel im = new ImgJsonModel(imgNo);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			ret = mapper.writeValueAsString(im); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public void testCall() {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("pass ... ObjectMapper");
	}
//	public static void main(String args[]) {
//		ImgJsonModel im = new ImgJsonModel("test.png");
////		TestModel im = new TestModel("Test", "Data");
////		try {
////			long l = Files.size(Paths.get("sampleserver/img/test.png"));
////		}catch(IOException e) {
////			System.out.println("Error");
////		}
//		
//		ObjectMapper obj = new ObjectMapper();
//		obj.enable(SerializationFeature.INDENT_OUTPUT);
//		try {
//			String a = obj.writeValueAsString(im);
//			System.out.println(a);
//		} catch(Exception e) {
//			System.out.println("Error");
//		}
//	}
}
