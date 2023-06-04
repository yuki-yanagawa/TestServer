package emsms;

import java.lang.reflect.Method;

public class TestRoute {
	public static void main(String args[]) {
		try {
			Class<?> clazz = Class.forName("emsms.app.controller.ImgController");
			Object action = clazz.getConstructor().newInstance();
			Method[] ms = clazz.getMethods();
			for (Method m : ms) {
				if(m.getName().equals("testCall")) {
					m.invoke(action, null);
				}
			}
		}catch(Exception e) {
			System.out.println(e.getCause().getMessage());
		}
	}
}
