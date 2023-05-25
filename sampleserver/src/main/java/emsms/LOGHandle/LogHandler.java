package emsms.LOGHandle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class LogHandler {
	public static LogHandler loghandler = new LogHandler();
	private int debuggLevelDef;
	private boolean stdoutDef;
	private FileOutputStream logFile;
	private LogHandler() {
		Properties prop = new Properties();
		String debuggPropPath = "conf/debuggs.properties";
		try {
			// Read Debugg Properties File
			FileInputStream fi = new FileInputStream(debuggPropPath);
			prop.load(fi);
			debuggLevelDef = Integer.parseInt(prop.getProperty("debugsLevel"));
			stdoutDef = Boolean.parseBoolean(prop.getProperty("stdout")); 
			String logfilePath = prop.getProperty("outputLogFilePath");
			
			// Creating OutPutFile
			logFile = new FileOutputStream(logfilePath,true);
			
		} catch (FileNotFoundException fs) {
			debuggLevelDef = 0;
			stdoutDef = false;
		} catch (IOException ie) {
			debuggLevelDef = 0;
			stdoutDef = false;
		}
	}
	public static LogHandler getInstance() {
		return loghandler;
	}
	public void error(String mess) {
		if (debuggLevelDef <= 0) {
			return;
		}
		try {
			logFile.write(createLogMessage(mess,"error").getBytes());
			logFile.flush();
		} catch(IOException e) {
			
		}
		if(stdoutDef) {
			System.out.println(mess);
		}
	}
	public void warn(String mess) {
		if (debuggLevelDef <= 1) {
			return;
		}
		try {
			logFile.write(createLogMessage(mess,"warn").getBytes());
			logFile.flush();
		} catch(IOException e) {
			
		}
		if(stdoutDef) {
			System.out.println(mess);
		}
	}
	public void info(String mess) {
		if (debuggLevelDef <= 2) {
			return;
		}
		try {
			logFile.write(createLogMessage(mess,"info").getBytes());
			logFile.flush();
		} catch(IOException e) {
			
		}
		if(stdoutDef) {
			System.out.println(mess);
		}
	}
	
	private String createLogMessage(String mess, String type) {
		String datetime = LocalDateTime.now().toString();
		switch(type) {
		case "error":
			mess = "[ErrorLog " + datetime + " ]" + mess;
			break;
		case "warn":
			mess = "[WarnLog " + datetime + " ]" + mess;
			break;
		case "info":
			mess = "[InfoLog " + datetime + " ]" + mess;
			break;
		}
		return mess + "\r\n";
	}
}
