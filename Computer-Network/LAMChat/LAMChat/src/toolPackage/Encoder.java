package toolPackage;

import dataStructure.User;

/** * @author  ���� :GiantJun 
* @date ����ʱ�䣺2019��6��27�� ����11:17:32 
* @version 1.0 
* <p>Description: </p>  ������Ҫ���ڴ������Ҫ����Ϣ�Ա㷢�͵��Եȷ�
* @throws 
*/
public class Encoder {
	
	public static String encodeMessage(String message,String myName) {
		return HeadString.Chat+myName+HeadString.splitChar+message;
	}
	
	//��һ�η��͸�����Ϣ
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
	
	//�����ļ�������Ϣ��Ѱ��ȷ��
	public static String encodeFile(String fileName,long fileSize,String myName) {
		return HeadString.SendFile+myName+HeadString.splitChar+fileName+
				HeadString.splitChar+String.valueOf(fileSize);
	}
	
	//����׼�������ļ�����Ϣ
	public static String encodeReady(String myName) {
		return HeadString.ING+myName;
	}
	
	//�����ļ����ճɹ�����Ϣ
	public static String encodeOK(String myName) {
		return HeadString.OK+myName;
	}
	
	public static String encodeNO(String myName) {
		return HeadString.NO+myName;
	}
}
