package emsms.router;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RoutesFileRead {
	private static RoutesFileRead routesFileRead = new RoutesFileRead();
	private Map<String,Object> routeFileMap = new HashMap<>(); 
	
	private RoutesFileRead() {
		createFileMap();
	}
	
	public static RoutesFileRead getInstance() {
		return routesFileRead;
	}
	
	private void createFileMap() {
		File routesFile = new File("conf/routes");
		try (FileInputStream fi = new FileInputStream(routesFile)){
//			System.out.println(Files.size(routesFile.toPath()));
			byte[] fileread = new byte[(int)Files.size(routesFile.toPath())];
			fi.read(fileread);
//			System.out.println(new String(fileread));
			String routesconf = new String(fileread);
			String[] routesconfLine = routesconf.split("\r\n");
			
			Map<String,Object> refmap = new HashMap<>();
			for(String r : routesconfLine) {
				String[] rLine = r.split("\\s+");
//				System.out.println(rLine[0]);
//				System.out.println(rLine[1]);
//				System.out.println(rLine[2]);
				refmap.put(rLine[1].trim(),rLine[2].trim());
				routeFileMap.put(rLine[0].trim(), refmap);
			}
		} catch(IOException e) {
			
		}
	}
	
	public Map<String,Object> getRoutesFileMap() {
		return routeFileMap;
	}
}
