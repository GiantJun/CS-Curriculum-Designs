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

/** * @author  ���� :GientJun 
* @date ����ʱ�䣺2018��12��1�� ����8:23:41 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
/*
 * �û���Ϊ���û���(userName)  ����(passWord)
 * (users)	Huang   				123
 * 			Li						123
 * 			Liu						123
 * 
 * ��ϵ�˱�			friendName 	history
 * (Huang)		Li			null
 * 						Liu			null
 * 
 * ��ϵ�˱�			friendName 	history
 * (Li)			Huang		null
 * 						
 * ��ϵ�˱�			friendName 	history
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
	
	//����ContentProvider�ڲ�ʹ�ã����ݿ���Ϣ����
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
	
	//��½�ӿڷ��������ص�½��Ϣ��֤���
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
	//��ȡ���к��ѵ�����
	public String getFriends(String userName) throws SQLException {
		String temp = "";
		ArrayList<String> list = query(userName,"friendName");
		for(String name:list) {
			temp = temp + ","+name;
		}
		return temp.substring(1,temp.length());	
	}
	
	
	
	//�ر�������Դ
	public void close() {
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//������ɾ������
	public void deleteFriends(String userName,String friendName) throws SQLException {
		String delete = "delete from "+userName+" where friendName="+friendName;
		statement.execute(delete);
	}
	
	//��Ӻ���
	public void addFriends(String userName,String friendName) throws SQLException {
		String add = "insert into "+userName+" values("+"'"+friendName+"'"+","+"null)";
		statement.execute(add);
	}
	
	
}
