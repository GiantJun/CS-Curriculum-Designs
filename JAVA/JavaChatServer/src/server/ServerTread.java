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
			System.out.println("无法打开输入流！");
			e.printStackTrace();
		}
	}
	
	//一直从客户端读取数据，解析成可用数据，发给指定用户
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String line = null;
		while((line = readFromClient()) != null) {
			//聊天功能
			 if(line.startsWith(HeadString.Chat) && line.endsWith(HeadString.Chat)) {
				 //去掉协议字符串
				 line = getContent(line);
				 //获取字符串中的有用信息
				 data = getData(line);
				 System.out.println(data.get(HeadString.SomeBody));
				 sendMassage(data.get(HeadString.SomeBody),data.get(HeadString.Massage));
			 //登陆验证,如果成功，传输好友列表
			 }else if(line.startsWith(HeadString.LogIn) && line.endsWith(HeadString.LogIn)) {
				 line = getContent(line);
				 data = getData(line);
				 System.out.println("开始验证"+data.get(HeadString.UserName)+"的身份");
				 //如果用户退出程序,终止线程
				 if(!checkLogIn(s,data.get(HeadString.UserName),data.get(HeadString.PassWord))) {
					 System.out.println("验证失败！");
					 continue;
				 }else {
					 userName = data.get(HeadString.UserName);
					 myserver.clientsmap.put(userName, s);
					 System.out.println(userName+":验证成功！");
					 try {
						bw.write(cp.getFriends(userName)+"\n");
						bw.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("数据数据库读取出错！");
						e.printStackTrace();
					}
				 }
			 //发送文件功能
			 }else if(line.startsWith(HeadString.SendFile) && line.endsWith(HeadString.SendFile)) {
				 line = getContent(line);
				 data = getData(line);
				 String toUser = data.get(HeadString.SomeBody);
				 Socket somebody = myserver.clientsmap.get(toUser);
				 if(somebody == null) {
					 System.out.println(toUser+"：已下线！");
					 continue;
				 }
				 DataInputStream dis = null;
				 DataOutputStream dos = null;
				 BufferedWriter bw2 = null;
				 System.out.println(userName+"----文件----->"+toUser);
				//打开输入、输出流
			        try {
			            dis = new DataInputStream(s.getInputStream());
			            dos = new DataOutputStream(somebody.getOutputStream());
			            bw2 = new BufferedWriter(new OutputStreamWriter(somebody.getOutputStream()));
			        } catch (IOException e) {
			            System.out.println("无法打开输入输出流！");
			            e.printStackTrace();
			        }
			        
			        byte[] bytes = new byte[1024];
			        int length = 0;
			        
			        //开始接收文件
			        System.out.println("正在转发文件：");
			        try {
			        	//发送文件转发提示信息
				        bw2.write(HeadString.SendFile+userName+"\n");
				        bw2.flush();
						while((length= dis.read(bytes,0,bytes.length))!=-1){
							System.out.print("。");
						  	dos.write(bytes, 0, length);
						    dos.flush();
						    String flag = br.readLine();
						    System.out.println(flag);
						    if(flag.equals(HeadString.OK))
						    	break;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("转发过程错误！");
						e1.printStackTrace();
					}
			        System.out.println("来自"+userName+"的文件转发到"+toUser+"成功！");
			 }else if(line.startsWith(HeadString.UserEdit) && line.endsWith(HeadString.UserEdit)){
				 //客户端发送好友修改信息，操作数据库
				 line = getContent(line);
				 System.out.println(userName+"开始操作好友列表");
				 try {
					 if(line.startsWith(HeadString.Delete)) {
						 String friendName = line.substring(HeadString.Stringlength, line.length());
						 cp.deleteFriends(userName, friendName);
						 System.out.println(userName+"删除了好友:"+friendName);
					 }else if(line.startsWith(HeadString.ADD)) {
						 String friendName = line.substring(HeadString.Stringlength, line.length());
						 cp.addFriends(userName, friendName);
						 System.out.println(userName+"添加了好友:"+friendName);
					 }
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
					 System.out.println("数据数据库操作出错！");
						e.printStackTrace();
				}
			 }
		}
	}
	
	//发送信息到指定对象
	private void sendMassage(String toName,String massage) {
		Socket somebody = myserver.clientsmap.get(toName);
		if(somebody == null) {
			System.out.println(toName+"：下线了！！");
			return ;
		}
		try {
			BufferedWriter tobw = new BufferedWriter(new OutputStreamWriter(somebody.getOutputStream()));
			tobw.write(massage+"\n");
			tobw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(toName+"：连接错误！！");
			e.printStackTrace();
		}
		System.out.println(userName+"------>"+toName+"："+massage);
	}
	
	//获取用户发送的信息（未处理）
	private String readFromClient() {
		try {
			return (String)br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("用户:"+userName+"退出程序！");
			//发生异常则说明断开了连接，移除socket
			if(myserver.clientsmap.containsValue(s)) {
				myserver.clientsmap.removeByValue(s);
			}
		}
		return null;
	}
	
	public String getContent(String line) {
		return line.substring(HeadString.Stringlength,line.length()-HeadString.Stringlength);
	}
	
	//由得到的直接字符串转化为数据
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
			System.out.println(temp+" 数据入图 "+getContent(part));
		}
		return map;
	}

	//登陆用户名与密码配对检验，若不配对则返回错误码
	public boolean checkLogIn(Socket s,String userName,String passWord) {
		if(!myserver.contentProvider.checkIn(userName, passWord)) {
			try {
				bw.write(ReturnCode.LogERROR+"\n");
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//发生异常则说明用户断开了连接，终止线程
				System.out.println(userName+"登陆失败!!!");
				e.printStackTrace();
			}
			return false;
		}else {
			try {
				bw.write(ReturnCode.LogSUCCEED+"\n");
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//发生异常则说明用户断开了连接，终止线程
				System.out.println(userName+"登陆失败!!!");
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
	private void SayHello() {
		try {
			bw.write("已建立与服务器的连接！\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ServerTread:无法写入输出流！");
			e.printStackTrace();
		}
	}
}
