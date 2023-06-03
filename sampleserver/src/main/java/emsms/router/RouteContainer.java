package emsms.router;

import java.lang.reflect.Method;
import java.util.Map;

public class RouteContainer {
	public static RouteContainer routeContainer = new RouteContainer();
	private RouteContainer() {
		
	}
	public static RouteContainer getInstance() {
		if(routeContainer == null) {
			routeContainer = new RouteContainer();
		}
		return routeContainer;
	}
	
	public synchronized Object callFunction(String path, Object arglist) throws Exception{
		String[] pathList = path.split("\\.");
		String method = pathList[pathList.length - 1];
		path = path.substring(0,path.length() - method.length() - 1);
		
//		System.out.println(method);
//		System.out.println(path);
		
		Class<?> actionClass = Class.forName(path);
		Object action = actionClass.getConstructor().newInstance();
		
		Method[] methods = actionClass.getMethods();
		for(Method m : methods) {
			if(m.getName().equals(method)) {
				return m.invoke(action, (Map<String,Object>)arglist);
			}
		}
		return new String();
	}
}
