package toolPackage;

import dataStructure.User;

/** * @author  作者 :GiantJun 
* @date 创建时间：2019年6月27日 下午11:17:32 
* @version 1.0 
* <p>Description: </p>  本类主要用于打包所需要的信息以便发送到对等方
* @throws 
*/
public class Encoder {
	
	public static String encodeMessage(String message,String myName) {
		return HeadString.Chat+myName+HeadString.splitChar+message;
	}
	
	//第一次发送个人信息
	public static String encodeIfo1(User myself) {
		return HeadString.SendUserIfo+myself.name+HeadString.splitChar+
				myself.ip+HeadString.splitChar+
				myself.ipMask+HeadString.splitChar+
				myself.port;
	}
	
	public static String encodeIfo2(User myself) {
		return HeadString.ReceiveUserIfo+myself.name+HeadString.splitChar+
				myself.ip+HeadString.splitChar+
				myself.ipMask+HeadString.splitChar+
				myself.port;
	}
	
	//发送文件类型信息并寻求确认
	public static String encodeFile(String fileName,long fileSize,String myName) {
		return HeadString.SendFile+myName+HeadString.splitChar+fileName+
				HeadString.splitChar+String.valueOf(fileSize);
	}
	
	//发出准备接收文件的信息
	public static String encodeReady(String myName) {
		return HeadString.ING+myName;
	}
	
	//发出文件接收成功的消息
	public static String encodeOK(String myName) {
		return HeadString.OK+myName;
	}
	
	public static String encodeNO(String myName) {
		return HeadString.NO+myName;
	}
}
