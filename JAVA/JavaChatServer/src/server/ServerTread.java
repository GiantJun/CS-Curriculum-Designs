package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ServerTread implements Runnable {
	
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	private String userName = null;
	private Socket s = null;
	private Map<String,String> data = null;
	private ContentProvider cp = null;
	private MyServer myserver;
	
	public ServerTread(Socket s,ContentProvider cp,MyServer myserver) {
		this.s = s;
		this.cp = cp;
		this.myserver = myserver;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("�޷�����������");
			e.printStackTrace();
		}
	}
	
	//һֱ�ӿͻ��˶�ȡ���ݣ������ɿ������ݣ�����ָ���û�
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String line = null;
		while((line = readFromClient()) != null) {
			//���칦��
			 if(line.startsWith(HeadString.Chat) && line.endsWith(HeadString.Chat)) {
				 //ȥ��Э���ַ���
				 line = getContent(line);
				 //��ȡ�ַ����е�������Ϣ
				 data = getData(line);
				 System.out.println(data.get(HeadString.SomeBody));
				 sendMassage(data.get(HeadString.SomeBody),data.get(HeadString.Massage));
			 //��½��֤,����ɹ�����������б�
			 }else if(line.startsWith(HeadString.LogIn) && line.endsWith(HeadString.LogIn)) {
				 line = getContent(line);
				 data = getData(line);
				 System.out.println("��ʼ��֤"+data.get(HeadString.UserName)+"�����");
				 //����û��˳�����,��ֹ�߳�
				 if(!checkLogIn(s,data.get(HeadString.UserName),data.get(HeadString.PassWord))) {
					 System.out.println("��֤ʧ�ܣ�");
					 continue;
				 }else {
					 userName = data.get(HeadString.UserName);
					 myserver.clientsmap.put(userName, s);
					 System.out.println(userName+":��֤�ɹ���");
					 try {
						bw.write(cp.getFriends(userName)+"\n");
						bw.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("�������ݿ��ȡ����");
						e.printStackTrace();
					}
				 }
			 //�����ļ�����
			 }else if(line.startsWith(HeadString.SendFile) && line.endsWith(HeadString.SendFile)) {
				 line = getContent(line);
				 data = getData(line);
				 String toUser = data.get(HeadString.SomeBody);
				 Socket somebody = myserver.clientsmap.get(toUser);
				 if(somebody == null) {
					 System.out.println(toUser+"�������ߣ�");
					 continue;
				 }
				 DataInputStream dis = null;
				 DataOutputStream dos = null;
				 BufferedWriter bw2 = null;
				 System.out.println(userName+"----�ļ�----->"+toUser);
				//�����롢�����
			        try {
			            dis = new DataInputStream(s.getInputStream());
			            dos = new DataOutputStream(somebody.getOutputStream());
			            bw2 = new BufferedWriter(new OutputStreamWriter(somebody.getOutputStream()));
			        } catch (IOException e) {
			            System.out.println("�޷��������������");
			            e.printStackTrace();
			        }
			        
			        byte[] bytes = new byte[1024];
			        int length = 0;
			        
			        //��ʼ�����ļ�
			        System.out.println("����ת���ļ���");
			        try {
			        	//�����ļ�ת����ʾ��Ϣ
				        bw2.write(HeadString.SendFile+userName+"\n");
				        bw2.flush();
						while((length= dis.read(bytes,0,bytes.length))!=-1){
							System.out.print("��");
						  	dos.write(bytes, 0, length);
						    dos.flush();
						    String flag = br.readLine();
						    System.out.println(flag);
						    if(flag.equals(HeadString.OK))
						    	break;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("ת�����̴���");
						e1.printStackTrace();
					}
			        System.out.println("����"+userName+"���ļ�ת����"+toUser+"�ɹ���");
			 }else if(line.startsWith(HeadString.UserEdit) && line.endsWith(HeadString.UserEdit)){
				 //�ͻ��˷��ͺ����޸���Ϣ���������ݿ�
				 line = getContent(line);
				 System.out.println(userName+"��ʼ���������б�");
				 try {
					 if(line.startsWith(HeadString.Delete)) {
						 String friendName = line.substring(HeadString.Stringlength, line.length());
						 cp.deleteFriends(userName, friendName);
						 System.out.println(userName+"ɾ���˺���:"+friendName);
					 }else if(line.startsWith(HeadString.ADD)) {
						 String friendName = line.substring(HeadString.Stringlength, line.length());
						 cp.addFriends(userName, friendName);
						 System.out.println(userName+"����˺���:"+friendName);
					 }
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
					 System.out.println("�������ݿ��������");
						e.printStackTrace();
				}
			 }
		}
	}
	
	//������Ϣ��ָ������
	private void sendMassage(String toName,String massage) {
		Socket somebody = myserver.clientsmap.get(toName);
		if(somebody == null) {
			System.out.println(toName+"�������ˣ���");
			return ;
		}
		try {
			BufferedWriter tobw = new BufferedWriter(new OutputStreamWriter(somebody.getOutputStream()));
			tobw.write(massage+"\n");
			tobw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(toName+"�����Ӵ��󣡣�");
			e.printStackTrace();
		}
		System.out.println(userName+"------>"+toName+"��"+massage);
	}
	
	//��ȡ�û����͵���Ϣ��δ����
	private String readFromClient() {
		try {
			return (String)br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("�û�:"+userName+"�˳�����");
			//�����쳣��˵���Ͽ������ӣ��Ƴ�socket
			if(myserver.clientsmap.containsValue(s)) {
				myserver.clientsmap.removeByValue(s);
			}
		}
		return null;
	}
	
	public String getContent(String line) {
		return line.substring(HeadString.Stringlength,line.length()-HeadString.Stringlength);
	}
	
	//�ɵõ���ֱ���ַ���ת��Ϊ����
	public Map<String,String> getData(String line) {
		String[] strings = line.split(HeadString.splitChar);
		Map<String,String> map = new HashMap<>();
		for(String part :strings) {
			String temp = part.substring(0,HeadString.Stringlength);
			if(temp.equals(HeadString.Massage)) {
				map.put(HeadString.Massage, getContent(part));
			}else if(temp.equals(HeadString.PassWord)){
				map.put(HeadString.PassWord,getContent(part));
			}else if(temp.equals(HeadString.SomeBody)) {
				map.put(HeadString.SomeBody, getContent(part));
			}else if(temp.equals(HeadString.UserName)) {
				map.put(HeadString.UserName, getContent(part));
			}
			System.out.println(temp+" ������ͼ "+getContent(part));
		}
		return map;
	}

	//��½�û�����������Լ��飬��������򷵻ش�����
	public boolean checkLogIn(Socket s,String userName,String passWord) {
		if(!myserver.contentProvider.checkIn(userName, passWord)) {
			try {
				bw.write(ReturnCode.LogERROR+"\n");
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//�����쳣��˵���û��Ͽ������ӣ���ֹ�߳�
				System.out.println(userName+"��½ʧ��!!!");
				e.printStackTrace();
			}
			return false;
		}else {
			try {
				bw.write(ReturnCode.LogSUCCEED+"\n");
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//�����쳣��˵���û��Ͽ������ӣ���ֹ�߳�
				System.out.println(userName+"��½ʧ��!!!");
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
	private void SayHello() {
		try {
			bw.write("�ѽ���������������ӣ�\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ServerTread:�޷�д���������");
			e.printStackTrace();
		}
	}
}
