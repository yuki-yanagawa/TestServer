package emsms;

import java.util.HashMap;
import java.util.Map;

public class TestConde {
	public static void main(String args[]) {
		Map<String,String> m = new HashMap<String, String>();
		m.put("key", "test1");
		Object o = (Object)m;
		
		m = (Map<String,String>)o;
		
		System.out.println(m.get("key"));
		
		String test = '"' + "sample" + '"';
		System.out.println(test);
		
		if(test.matches("^\".*")) {
			System.out.println("OK!!!!");
		}
		
		System.out.println(test.replaceAll("^\"|\"$", ""));
	}
}
