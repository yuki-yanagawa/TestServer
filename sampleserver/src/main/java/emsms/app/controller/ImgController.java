package emsms.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import emsms.app.model.json.ImgJsonModel;
import emsms.app.model.json.TestModel;

public class ImgController {
	public String getImgData(Map<String,Object> reqparam) {
		String ret = "";
		return ret;
	}
	public static void main(String args[]) {
		ImgJsonModel im = new ImgJsonModel("test.png");
//		TestModel im = new TestModel("Test", "Data");
//		try {
//			long l = Files.size(Paths.get("sampleserver/img/test.png"));
//		}catch(IOException e) {
//			System.out.println("Error");
//		}
		
		ObjectMapper obj = new ObjectMapper();
		obj.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			String a = obj.writeValueAsString(im);
			System.out.println(a);
		} catch(Exception e) {
			System.out.println("Error");
		}
	}
}
