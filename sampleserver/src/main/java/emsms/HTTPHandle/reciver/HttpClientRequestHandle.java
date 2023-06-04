package emsms.HTTPHandle.reciver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.URLDataSource;

import emsms.LOGHandle.LogHandler;
import emsms.router.RouteContainer;
import emsms.router.RoutesFileRead;

public class HttpClientRequestHandle extends Thread{
	private Socket clientSocket;
	private int recvSize;
	private final String htmlRegex = ".*\\.html$";
	private final String javascriptRegex = ".*\\.js$";
	private final String cssRegex = ".*\\.css$";
	private final String jsonFileRegex = ".*\\.json$";
	public HttpClientRequestHandle(Socket clientSocket, int recvSize) {
		this.clientSocket = clientSocket;
		this.recvSize = recvSize;
	}
	
	@Override
	public void run() {
		LogHandler logger = LogHandler.getInstance();
		try (InputStream is = this.clientSocket.getInputStream();
			OutputStream os = this.clientSocket.getOutputStream()){
			byte[] recvbyte = new byte[recvSize];
			is.read(recvbyte);
			String httpRequest = new String(recvbyte);
			os.write(httpRequestAnalizeAndCreateResponse(httpRequest).getBytes());
			os.flush();
			this.clientSocket.close();
		} catch(IOException e) {
			logger.error("client socket handle Error");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String httpRequestAnalizeAndCreateResponse(String httpRequest) {
		String retStr = "";
		LogHandler logger = LogHandler.getInstance();
		
		String[] httpRequestLine = httpRequest.split("\r\n");
		String httpRequestHeader = httpRequestLine[0];
		String[] httpRequestHeaderLine = httpRequestHeader.split(" ");
		if(httpRequestHeaderLine.length < 2) {
			logger.warn("[Client RequestHeaderError ...] ipAddr : " + this.clientSocket.getInetAddress() + "  port : " + this.clientSocket.getPort() + " requestHeader " + httpRequestHeader);
			return createResponseHeaderBad();
		}
		String requestMethod = httpRequestHeaderLine[0].trim();
		String requestData = httpRequestHeaderLine[1].trim();
		
		logger.info("[Client Connected ...] ipAddr : " + this.clientSocket.getInetAddress() + "  port : " + this.clientSocket.getPort() + " requestData " + requestData);
		
		String excludeHeaderHttpRequest = httpRequest.substring(httpRequestHeader.length());
		
		//This Map Key And Value Charcter is UpperCase
		Map<String,String> requestParameterMap = new HashMap<>();
		requestParameterMapConverter(httpRequestLine,requestParameterMap);
		
		if(requestData.equals("/") ) {
			requestData = "html/index.html";
			retStr = createResponseData(requestData,"html");
		} else if(requestData.matches(htmlRegex)) {
			requestData = "html" + requestData;
			retStr = createResponseData(requestData,"html");
		} else if(requestData.matches(javascriptRegex)) {
			requestData = "javascript" + requestData;
			retStr = createResponseData(requestData,"javascript");
		} else if(requestData.matches(cssRegex)) {
			requestData = "css" + requestData;
			retStr = createResponseData(requestData,"css");
		} else if(requestData.matches(jsonFileRegex)){
			requestData = "json" + requestData;
			retStr = createResponseData(requestData,"jsonFile");
		} else {
			Map<String,Object> routesFileMapAllList = RoutesFileRead.getInstance().getRoutesFileMap();
			Map<String,String> routesFileMap = (Map<String,String>)routesFileMapAllList.get(requestMethod);
			String actionClassPath = routesFileMap.get(requestData);
			if(actionClassPath == null || actionClassPath.trim().equals("")) {
				retStr = createResponseHeaderBad();
			} else {
				String requestPrameterData = "";
				Map<String,Object> reqMap = null;
				if(requestMethod.toUpperCase().equals("POST")) {
					int dataLength = 0;
					try {
						dataLength = requestParameterMap.get("CONTENT-LENGTH") != null ? Integer.parseInt(requestParameterMap.get("CONTENT-LENGTH")) : 0;
					} catch(NumberFormatException e) {
						dataLength = 0;
					}
					if(dataLength == 0) {
						try {
							retStr = createResponseDataForJson(actionCalssCallHandler(actionClassPath, new HashMap<String,Object>()),"json");
						} catch (Exception e){
							retStr = createResponseHeaderBad();
						}
					} else {
						requestPrameterData = excludeHeaderHttpRequest.trim().substring(excludeHeaderHttpRequest.trim().length() - dataLength);
						try {
							requestPrameterData = URLDecoder.decode(requestPrameterData, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							requestPrameterData = "";
						}
						if(requestPrameterData.trim().equals("")) {
							reqMap = new HashMap<>();
						} else {
							reqMap = createReqParamMap(requestPrameterData);
						}
					}
				}
				try {
					retStr = createResponseDataForJson(actionCalssCallHandler(actionClassPath, reqMap),"json");
				} catch (Exception e){
					retStr = createResponseHeaderBad();
				}
			}
		}
		return retStr;
		
	}
	
	private String createResponseData(String requestData, String type) {
		String responseStr = "";
		try(FileInputStream fi = new FileInputStream(requestData)){
			File fileData = new File(requestData);
			long filesize = Files.size(fileData.toPath());
			byte[] readsize = new byte[(int)filesize];
			fi.read(readsize);
			responseStr = createResponseHeaderSuccess(type,(int)filesize);
			responseStr = responseStr + new String(readsize) + "\r\n\r\n";
		} catch(IOException e) {
			responseStr = createResponseHeaderBad();
		}
		return responseStr;
	}
	
	private String createResponseDataForJson(String requestData, String type) {
		String responseStr = "";
		int dataSize = requestData.length();
		responseStr = createResponseHeaderSuccess(type,dataSize);
		responseStr = responseStr + requestData + "\r\n\r\n";
		return responseStr;
	}
	
	private String createResponseHeaderSuccess(String type, int fileSize) {
		String retStr = "";
		retStr = "HTTP/1.1 200 OK\r\n";
		retStr = retStr + "content-length : " + String.valueOf(fileSize) + "\r\n";
		switch(type) {
		case "html":
			retStr = retStr + "content-type: text/html; charset='utf-8'\r\n";
			break;
		case "css":
			retStr = retStr + "content-type: text/css; charset='utf-8'\r\n";
			break;
		case "javascript":
			retStr = retStr + "content-type: text/javascript; charset='utf-8'\r\n";
			break;
		case "jsonFile":
			retStr = retStr + "content-type: application/json; charset='utf-8'\r\n";
			break;
		case "json":
			retStr = retStr + "content-type: application/json; charset='utf-8'\r\n";
			break;
		}
		retStr = retStr + "server : sampleserver\r\n";
		retStr = retStr + "date : " + LocalDateTime.now().toString() + "\r\n";
		retStr = retStr + "connection : close\r\n";
		retStr = retStr + "\r\n";
		return retStr;
	}
	
	private String createResponseHeaderBad() {
		String retStr = "";
		retStr = "HTTP/1.1 404 BAD\r\n";
		retStr = retStr + "server : sampleserver\r\n";
		retStr = retStr + "date : " + LocalDateTime.now().toString() + "\r\n";
		retStr = retStr + "connection : close\r\n";
		retStr = retStr + "\r\n";
		return retStr;
	}
	
	private String actionCalssCallHandler(String actionClassPath, Object requestParameterData) throws Exception {
		return (String)RouteContainer.getInstance().callFunction(actionClassPath, requestParameterData);
	}
	
	private void requestParameterMapConverter(String[] requestParameterLine ,Map<String,String> requestparameterMap) {
		for(String requestParamter : requestParameterLine) {
			if(requestParamter.matches("\\s*(\\w|-)+\\s*:\\s*(\\w|-)+\\s*")) {
				String[] tmpLine = requestParamter.split(":");
				requestparameterMap.put(tmpLine[0].toUpperCase().trim(), tmpLine[1].toUpperCase().trim());
			}
		}
	}
	
	private Map<String, Object> createReqParamMap(String reqParamRawData) {
		//if(reqParamRawData.substring(0,1).equals("{") && reqParamRawData.substring(reqParamRawData.length()-1).equals("}")){
		if(reqParamRawData.matches("^\\{.*\\}")) {
			return createReqParamMapForJsonParam(reqParamRawData);
		} else {
			return createReqParamMapForSubmitParam(reqParamRawData);
		}
	}
	
	private Map<String, Object> createReqParamMapForJsonParam(String reqParamRawData) {
		Map<String, Object> retMap = new HashMap<String,Object>();
		reqParamRawData = reqParamRawData.replaceAll("\\{|\\}", "");
		String[] reqParamRawDataLine = reqParamRawData.split(",");
		try {
			for(String r : reqParamRawDataLine) {
				String key = r.split(":")[0].trim();
				String value = r.split(":")[1].trim();
				retMap.put(delimterStringQuataionType(key),delimterStringQuataionType(value));
			}
		} catch(Exception e) {
			retMap = new HashMap<String, Object>();
		}
		return retMap;
	}
	
	private Map<String, Object> createReqParamMapForSubmitParam(String reqParamRawData) {
		return new HashMap<String,Object>();
	}
	
	private String delimterStringQuataionType(String data) {
		if(data.matches("^\".*\"$")) {
			data = data.replaceAll("^\"|\"$", "");
		}
		return data;
	}

}
