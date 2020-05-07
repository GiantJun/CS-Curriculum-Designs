package clients;

import java.awt.EventQueue;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月2日 下午8:06:37 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class Controler {
	public final String url = "jdbc:mysql://localhost:3306/JavaChat?serverTimezone=GMT%2B8";
	
	public String userName = null;
	public Socket socket = null;
	public String friends = null;
	private Statement statement = null;
	
	public Controler(String userName,Socket socket,String friends) throws ClassNotFoundException, SQLException {
		this.userName = userName;
		this.socket = socket;
		statement = getStatement();
		this.friends = friends;
	}
	private  Statement getStatement() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, "root", "3117004568");
		return con.createStatement();
	}
	
	//保存聊天记录
	public void saveHistory(String friendName,String history) throws SQLException {
		String pass = null;
		pass = this.getHistory(friendName);
		String upDate = "update "+userName+" set history="+"'"+pass+history+"'"+" where friendName="+"'"+friendName+"'";
		statement.execute(upDate);
		System.out.println("聊天记录保存成功！");
		System.out.println("聊天记录保存成功！");
	}
	
	//查询聊天记录,返回userName和friendName的聊天记录
	public String getHistory(String friendName) throws SQLException {
		String query = "select * from "+userName;
		String temp = "";
		ResultSet result = statement.executeQuery(query);		
		while(result.next()) {
			if(result.getString("friendName").equals(friendName)) {
				temp = temp + result.getString("history");
				break;
			}
		}
		return temp;
	}
	
	//删除聊天记录
		public void deleteHistory(String friendName) throws SQLException {
			String upDate = "update "+userName+" set history=null where friendName="+"'"+friendName+"'";
			statement.execute(upDate);
			System.out.println("聊天记录删除成功！");
		}
	
		

}
