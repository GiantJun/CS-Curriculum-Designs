package clients;

import java.awt.EventQueue;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** * @author  ���� :GientJun 
* @date ����ʱ�䣺2018��12��2�� ����8:06:37 
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
	
	//���������¼
	public void saveHistory(String friendName,String history) throws SQLException {
		String pass = null;
		pass = this.getHistory(friendName);
		String upDate = "update "+userName+" set history="+"'"+pass+history+"'"+" where friendName="+"'"+friendName+"'";
		statement.execute(upDate);
		System.out.println("�����¼����ɹ���");
		System.out.println("�����¼����ɹ���");
	}
	
	//��ѯ�����¼,����userName��friendName�������¼
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
	
	//ɾ�������¼
		public void deleteHistory(String friendName) throws SQLException {
			String upDate = "update "+userName+" set history=null where friendName="+"'"+friendName+"'";
			statement.execute(upDate);
			System.out.println("�����¼ɾ���ɹ���");
		}
	
		

}
