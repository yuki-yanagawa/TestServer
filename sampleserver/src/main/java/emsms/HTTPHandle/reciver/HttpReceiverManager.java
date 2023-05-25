package emsms.HTTPHandle.reciver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import emsms.LOGHandle.LogHandler;
import emsms.propertiesfileaccsess.CommonPropertiesFileReader;

public class HttpReceiverManager {
	public static void main(String[] args) {
		LogHandler loghandler = LogHandler.getInstance();
		CommonPropertiesFileReader prop = new CommonPropertiesFileReader();
		boolean ipAddrUseFlg = Boolean.parseBoolean(prop.getPropertiesValue("ipAddrUse"));
		int portNo = 12345;
		int recvBufSize = 1024;
		try {
			portNo = Integer.parseInt(prop.getPropertiesValue("portNo"));
			recvBufSize = Integer.parseInt(prop.getPropertiesValue("recvBufferSize"));
		} catch(NumberFormatException e) {
			loghandler.error("Properties File BAD Setting -> port No!!!");
		}
		try(ServerSocket svr = new ServerSocket()) {
			if(ipAddrUseFlg) {
				 svr.bind(new InetSocketAddress(InetAddress.getLocalHost(), portNo));
			} else {
				svr.bind(new InetSocketAddress("localhost", portNo));
			}
			while(true) {
				Socket clientSocket = svr.accept();
				HttpClientRequestHandle httpClientRequestHandle = new HttpClientRequestHandle(clientSocket, recvBufSize);
				httpClientRequestHandle.start();
			}
		} catch (IOException e) {
			
		}
	}

}
