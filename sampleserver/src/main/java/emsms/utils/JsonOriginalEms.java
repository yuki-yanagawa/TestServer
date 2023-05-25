package emsms.utils;

import java.util.HashMap;
import java.util.Map;

public class JsonOriginalEms<K> {
	public String parse(Map<K, ?> map) {
		String retStr = "{";
		retStr += generateJsonStr(map,new StringBuffer());
		retStr = retStr + "}";
		return retStr;
	}
	private String generateJsonStr(Map<K, ?> map, StringBuffer sb) {
		for(Object k : (Object[])map.entrySet().stream().map(m -> m.getKey()).toArray()) {
			if(map.get(k) instanceof Map) {
				sb.append("'" + k + "'" + " : { ");
				generateJsonStr((Map<K, ?>)map.get(k),sb);
				sb.append(" } ");
			} else {
				sb.append("'" + k + "'" + " : " + "'" + map.get(k) + "'" + ",");
			}
		}
		return sb.toString().substring(0,sb.length() - 1);
	}
}
