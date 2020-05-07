package server;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
public class MyServer {
	private static final int port = 6666;
	
	public static ClientsMap<String,Socket> clientsmap = new ClientsMap<>();
	public static ContentProvider contentProvider ;
	
	public MyServer() throws ClassNotFoundException, SQLException {
		clientsmap = new ClientsMap<>();
		contentProvider = new ContentProvider();
	}
	
	public static void main(String[] args) {
		MyServer myserver = null;
		try {
			myserver = new MyServer();
		} catch (ClassNotFoundException | SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println("无法连接到数据库");
		}
		
		System.out.println("服务器已启动！");
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				Socket s = serverSocket.accept();
				 System.out.println("有用户发来登陆验证请求");
				new Thread(new ServerTread(s,contentProvider,myserver)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("无法创建ServerSocket实例");
			e.printStackTrace();
		}
		
		//关闭资源
		contentProvider.close();
	}
	
}
