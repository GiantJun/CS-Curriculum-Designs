package databaseDesign;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DBManager {
	private SQLServerDataSource ds=null;
	private Connection con;
	private Statement sta;
	private ResultSet rs;
	
	private Connection getConnection(){
		/****1433�����Լ���SQLserver�˿ں�(Ĭ����1433)*********/
		/**************DatabaseName��Ҫ���ӵ����ݿ�����*********/
		if(ds==null){
			ds = new SQLServerDataSource(); 
			ds.setUser("JavaManager");  
	        ds.setPassword("sqlstudy");  
	        ds.setServerName("localhost");  
	        ds.setPortNumber(1433);
	        ds.setDatabaseName("ZhiYuanLuQuXiTong");  
		}
        try {
			con = ds.getConnection();
			System.out.println("���ӳɹ�");
			sta = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("����ʧ��");
			e.printStackTrace();
		}
		return con;
	}
	
	/*public DBManager() {
		List<Connection> list = new ArrayList<Connection>();
		for (int i = 0; i < 5; i++) {
			list.add(this.getConnection());
		}
		this.con = list.get(0);
	}*/
	public int update(String sql){
		int row = -1;
		con = getConnection();
		try {
			row = sta.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			this.close();
		}
		return row;
	}
	
	public ResultSet query(String sql){
		con = getConnection();
		try {
			rs = sta.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public void close(){
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (sta != null) {
				sta.close();
				sta = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
