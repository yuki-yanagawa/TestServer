package emsms.checkhandle;

import java.util.HashMap;
import java.util.Map;

import emsms.utils.JsonOriginalEms;

public class CheckHandle {
	public String main(Object obj) {
		Map<String,String> test = new HashMap<>();
		test.put("sampleCall", "OKTESTClear");
		JsonOriginalEms<String> j = new JsonOriginalEms<>();
		return j.parse(test);
	}
}
