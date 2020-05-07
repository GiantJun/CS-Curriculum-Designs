package server;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月1日 下午8:23:41 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
/*
 * 用户表为：用户名(userName)  密码(passWord)
 * (users)	Huang   				123
 * 			Li						123
 * 			Liu						123
 * 
 * 联系人表：			friendName 	history
 * (Huang)		Li			null
 * 						Liu			null
 * 
 * 联系人表：			friendName 	history
 * (Li)			Huang		null
 * 						
 * 联系人表：			friendName 	history
 * (Liu)		Huang		null
 * */
public class ContentProvider {
	public final String url = "jdbc:mysql://localhost:3306/JavaChat?serverTimezone=GMT%2B8";
	private Statement statement = null;
	private ArrayList<String> userNames;
	
	public ContentProvider() throws ClassNotFoundException, SQLException {
		statement = getStatement(url);
		userNames = query("users","userName");
	}
	
	//仅限ContentProvider内部使用，数据库信息查找
	private ArrayList<String> query(String tableName,String column) throws SQLException{
		ArrayList<String> temp = new ArrayList<>();
		String query = "select * from "+tableName;
		ResultSet result = statement.executeQuery(query);
		while(result.next()) {
			String data = result.getString(column);
			temp.add(data);
		}
		return temp;
	}
	
	private Statement getStatement(String url) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, "root", "3117004568");
		return con.createStatement();
	}
	
	//登陆接口方法，返回登陆信息验证结果
	public boolean checkIn(String userName,String passWord) {
		try {
			String query = "select * from users";
			ResultSet result = statement.executeQuery(query);
			while(result.next()) {
				String name = result.getString("userName");
				String pw = result.getString("passWord");
				if(name.equals(userName) && pw.equals(passWord)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//获取所有好友的名字
	public String getFriends(String userName) throws SQLException {
		String temp = "";
		ArrayList<String> list = query(userName,"friendName");
		for(String name:list) {
			temp = temp + ","+name;
		}
		return temp.substring(1,temp.length());	
	}
	
	
	
	//关闭所有资源
	public void close() {
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//单方面删除好友
	public void deleteFriends(String userName,String friendName) throws SQLException {
		String delete = "delete from "+userName+" where friendName="+friendName;
		statement.execute(delete);
	}
	
	//添加好友
	public void addFriends(String userName,String friendName) throws SQLException {
		String add = "insert into "+userName+" values("+"'"+friendName+"'"+","+"null)";
		statement.execute(add);
	}
	
	
}
