package clients;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/** * @author  ���� :GientJun 
* @date ����ʱ�䣺2018��12��3�� ����9:56:35 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class SendFileThread implements Runnable{

	private Socket socket = null;
	private String fileAdress = null;
	private String toUserName = null;
	public SendFileThread(Socket socket,String fileAdress,String toUserName) {
		this.socket = socket;
		this.fileAdress = fileAdress;
		this.toUserName = toUserName;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//��ָ�����ļ�
		File file = null;
	    try {
	        file = new File(fileAdress);
	    } catch (NullPointerException e) {
	        System.out.println("�����ַ����");
	        e.printStackTrace();
	    }
	    DataOutputStream dos = null;
	    FileInputStream fis = null;
	    BufferedWriter bw = null;
	    try {
	        if (file.exists()) {
	        	bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	            dos = new DataOutputStream(socket.getOutputStream());
	            fis = new FileInputStream(file);
	            
	            bw.write(HeadString.SendFile+HeadString.SomeBody+toUserName+HeadString.SomeBody+HeadString.SendFile+"\n");
	            bw.flush();
	            dos.writeUTF(file.getName());
	            dos.flush();

	            //�����ļ��������
	            System.out.println("�ļ������У�");
	            byte[] bytes = new byte[1024];
	            int length = 0;
	            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
	                dos.write(bytes, 0, length);
	                dos.flush();
	                bw.write(HeadString.ING+"\n");
	                bw.flush();
	            }
	            bw.write(HeadString.OK+"\n");
	            bw.flush();
	            System.out.println("�ļ��������!");

	        } else {
	            System.out.println("�޷����ļ����ļ������ڣ�");
	        }
	    } catch (IOException e) {
	        System.out.println("����д�ļ�����");
	        e.printStackTrace();
	    }
	}

	
}
