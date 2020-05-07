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
			System.out.println("clientTread :�޷���������");
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
			        //��������
			        try {
			            dis = new DataInputStream(s.getInputStream());
			        } catch (IOException e) {
			            System.out.println("�޷��������������");
			            e.printStackTrace();
			        }
			        try {
			            //���ļ���׼����������
			            File directory = new File("E:\\JavaChat");//����·��
			            String fileName = dis.readUTF();
			            if(!directory.exists()) {
			                directory.mkdirs();
			            }
			            file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);

			            //���ļ�������
			            fos = new FileOutputStream(file);
			            //��ʼ�����ļ�
			            System.out.println("���ڽ����ļ���");
			            frame.showMassage(frame.toUserName, userName,"���ڽ����ļ���������", false);
			            while((length= dis.read(bytes,0,bytes.length))!=-1){
			                fos.write(bytes,0,length);
			                fos.flush();
			            }
			            System.out.println("����client1���ļ����ճɹ���");
			            frame.showMassage(frame.toUserName,userName, "�ļ����ճɹ���\n�ļ������ڣ�"+file.getAbsolutePath(), false);
			        } catch (IOException e) {
			            System.out.println("�ļ�����ȡ������޷��½��ļ�");
			            e.printStackTrace();
			        }
				}else {
					frame.showMassage(frame.toUserName, userName,line, false);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("clientTread :�ѶϿ����ӣ�");
		}
	}
}
