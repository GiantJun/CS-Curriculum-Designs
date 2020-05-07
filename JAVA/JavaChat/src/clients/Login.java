package clients;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月1日 下午11:12:17 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class Login {

	JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private static final int port = 6666;
	private static final String ip = "localhost";
	private static String headString = HeadString.LogIn;
	private boolean isConnected = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
			try{
				Login window = new Login();
				window.frame.setVisible(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("用户登陆");
		frame.setBounds(100, 100, 505, 345);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
		label.setBounds(78, 100, 72, 18);
		frame.getContentPane().add(label);
		
		textField = new JTextField();
		textField.setBounds(164, 97, 124, 24);
		frame.getContentPane().add(textField);
		textField.setColumns(1);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
		label_1.setBounds(78, 165, 55, 18);
		frame.getContentPane().add(label_1);
		
		JButton button = new JButton("\u767B\u9646");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName = textField.getText();
				String passWord = new String(passwordField.getPassword());
				Socket s = null;
				if(userName == null || passWord == null||userName.equals("")||passWord.equals("")) {
					showDialog("请正确输入用户名和密码");
				}else {
					try {
						s = new Socket();
						s.connect(new InetSocketAddress(ip,port),1000);
						isConnected = true;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("无法与服务器的通信！");
						//e1.printStackTrace();
						//如果警告弹窗中选择ok重新连接，如果选择cancle则取消连接
						if(showDialog("无法与服务器建立连接，请确认你的连接情况后按确认！")) {
							try {
								s = new Socket();
								s.connect(new InetSocketAddress(ip,port),1000);
								isConnected = true;
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								//e2.printStackTrace();
								System.out.println("再次连接失败");
								showDialog("再次连接失败!");
								isConnected = false;
							} 
						}
					}
					if(isConnected == true) {
						try {
							String a = null;
							if((a=sendLogin(s,userName,passWord)) != null) {
								Controler controler = new Controler(userName,s,a);
								frame.setVisible(false);
								MainFrame mainFrame = new MainFrame(controler);
							}else {
								showDialog("用户名或密码错误！");
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					}
				}
				
			}
		});
		button.setBounds(164, 214, 113, 27);
		frame.getContentPane().add(button);
		
		JLabel lblJavachat = new JLabel("JavaChat\u804A\u5929\u7CFB\u7EDF");
		lblJavachat.setBounds(134, 48, 184, 18);
		lblJavachat.setFont(new java.awt.Font("Dialog", 1, 20));
		frame.getContentPane().add(lblJavachat);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(164, 162, 124, 24);
		frame.getContentPane().add(passwordField);
		
		frame.setVisible(true);
	}
	
	//如果登陆成功，返回true，否则返回false
	public String sendLogin(Socket socket,String userName,String passWord) throws IOException {
		String temp = null;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw.write(HeadString.LogIn+wrapData(userName,passWord)+HeadString.LogIn+"\n");
		bw.flush();
		String line = br.readLine();
		if(line.startsWith(ReturnCode.LogERROR)) {
			System.out.println("登陆密码或用户名错误");
			return null;
		}
		else {
			System.out.println("登陆成功！");
			return br.readLine();
		}
		
	}
	
	private String wrapData(String userName,String passWord) {
		return HeadString.UserName+userName+HeadString.UserName+HeadString.splitChar+HeadString.PassWord+passWord+HeadString.PassWord;
	}
	
	//如果选择ok返回true，选择cancel返回false
	private boolean showDialog(String warning) {
		int result = JOptionPane.showConfirmDialog(frame,warning,
				"连接警告",JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION)	return true;
		return false;
	}
}
