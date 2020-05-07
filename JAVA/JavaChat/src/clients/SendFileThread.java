package clients;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月3日 上午9:56:35 
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
		//打开指定的文件
		File file = null;
	    try {
	        file = new File(fileAdress);
	    } catch (NullPointerException e) {
	        System.out.println("输入地址错误！");
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

	            //传输文件具体操作
	            System.out.println("文件传输中！");
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
	            System.out.println("文件传输完成!");

	        } else {
	            System.out.println("无法打开文件或文件不存在！");
	        }
	    } catch (IOException e) {
	        System.out.println("读或写文件出错！");
	        e.printStackTrace();
	    }
	}

	
}
