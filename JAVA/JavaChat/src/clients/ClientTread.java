package clients;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientTread implements Runnable{
	private Socket s = null;
	private BufferedReader br = null;
	private MainFrame frame;
	private String userName;
	public ClientTread(Socket s,MainFrame frame) {
		this.s = s;
		this.frame = frame;
		this.userName = frame.userName;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("clientTread :无法打开输入流");
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				if(line.startsWith(HeadString.SendFile)) {
					String fromName = line.substring(HeadString.Stringlength,line.length());
					byte[] bytes = new byte[1024];
			        int length = 0;
			        DataInputStream dis = null;
			        File file = null;
			        FileOutputStream fos = null;
			        //打开输入流
			        try {
			            dis = new DataInputStream(s.getInputStream());
			        } catch (IOException e) {
			            System.out.println("无法打开输入输出流！");
			            e.printStackTrace();
			        }
			        try {
			            //打开文件，准备接收数据
			            File directory = new File("E:\\JavaChat");//保存路径
			            String fileName = dis.readUTF();
			            if(!directory.exists()) {
			                directory.mkdirs();
			            }
			            file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);

			            //打开文件输入流
			            fos = new FileOutputStream(file);
			            //开始接收文件
			            System.out.println("正在接收文件：");
			            frame.showMassage(frame.toUserName, userName,"正在接收文件。。。。", false);
			            while((length= dis.read(bytes,0,bytes.length))!=-1){
			                fos.write(bytes,0,length);
			                fos.flush();
			            }
			            System.out.println("来自client1的文件接收成功！");
			            frame.showMassage(frame.toUserName,userName, "文件接收成功！\n文件保存在："+file.getAbsolutePath(), false);
			        } catch (IOException e) {
			            System.out.println("文件名读取错误或无法新建文件");
			            e.printStackTrace();
			        }
				}else {
					frame.showMassage(frame.toUserName, userName,line, false);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("clientTread :已断开连接！");
		}
	}
}
