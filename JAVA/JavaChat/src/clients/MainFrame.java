package clients;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.CardLayout;
import javax.swing.JTextArea;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月2日 下午9:13:34 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class MainFrame extends JFrame{

//	private JFrame frame;
	protected String userName;
	protected String toUserName = null;
	private String[] friends;
	private Socket socket;
	private JTextField textField_2;
	private BufferedWriter bw= null;
	private Controler controler = null;
	
	private JTextArea textArea;
	private JScrollPane scrollPane=null;
	private JPanel jContentPane = null;
	private JLabel priviousLabel = null;
	private HashMap<String,JTextPane> textPaneMap = new HashMap<>();
	private JTextPane priviousTextPane = null;
	public static JButton button_1;
	private JButton button  = null;
	
	private Thread listenTread = null;
	
	private HashMap<String,String> history;
	
	private static final int LoginMode = 666;
	private static final int ChatMode = 555;
	private static final int EditMode = 444;
	
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainFrame window = new MainFrame();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public MainFrame(Controler controler) {
		this.controler = controler;
		this.userName = controler.userName;
		this.socket = controler.socket;
		this.friends = controler.friends.split(",");
		history = new HashMap<>();
		for(String name:friends) {
			history.put(name, "");
		}
		
		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("MainFrame:无法打开输入流");
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
		this.setBounds(100, 100, 713, 504);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setTitle("JavaChat聊天 : "+userName);
		this.setBackground(new Color(65,105,225));
		this.setResizable(false);
		
		button = new JButton("\u53D1\u9001");
		button.setForeground(Color.WHITE);
		button.setFont(new Font("黑体", Font.PLAIN, 17));
		button.setBackground(new Color(1, 136, 251));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toUserName != null) {
					String massage =textArea.getText();
					textArea.setText("");
					try {
						sendMassage(toUserName,massage,ChatMode);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("无法发送信息！");
					e1.printStackTrace();
					}
					showMassage(userName,toUserName,massage,true);
				}else {
					showDialog("请先选择聊天对象！");
				}
			}
		});
		button.setBounds(582, 429, 113, 27);
		this.getContentPane().add(button);
		
		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		for(int i=0 ;i < friends.length; i++) {
			JLabel label = new JLabel();
			label.setFont(new Font("SimSun", Font.PLAIN, 17));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setText(friends[i]);
			label.setVisible(true);
			
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			textPane.setBounds(227, 29, 461, 278);
			textPane.setVisible(false);
			textPaneMap.put(label.getText(), textPane);
			
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					textField_2.setText(label.getText());
					if(priviousLabel != null) {
						priviousLabel.setBackground(null);
					}
					priviousLabel = label;
					
					label.setBackground(Color.BLACK);
	
					priviousTextPane.setVisible(false);
					priviousTextPane = textPaneMap.get(label.getText());
					priviousTextPane.setVisible(true);	
					
					toUserName = label.getText();
					textArea.setText("");
				}
			});
			
			panel.add(label);
			this.getContentPane().add(textPane);
		}
		
//		panel.add(new JLabel("haha"));
//		panel.add(new JLabel("haha"));
//		panel.add(new JLabel("haha"));
		panel.setPreferredSize(new Dimension(29, friends.length*75));
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 29, 224, 427);
		scrollPane.setViewportView(panel); 
		scrollPane.setVisible(true);
		scrollPane.setBackground(Color.WHITE);
		this.getContentPane().add(scrollPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(new Color(240, 240, 240));
		toolBar.setBounds(227, 307, 338, 27);
		this.getContentPane().add(toolBar);
		
		button_1 = new JButton("\u53D1\u9001\u6587\u4EF6");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String address = showFileOpenDialog(button_1);
				System.out.println(address);
				new Thread(new SendFileThread(socket,address,toUserName)).start();
			}
		});
		toolBar.add(button_1);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(227, 0, 323, 27);
		textField_2.setFont(new Font("黑体", Font.PLAIN, 17));
		textField_2.setBackground(Color.white);
		this.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		priviousTextPane = new JTextPane();
		priviousTextPane.setEditable(false);
		priviousTextPane.setBounds(227, 29, 461, 278);
		priviousTextPane.setVisible(true);
		this.getContentPane().add(priviousTextPane);
		
		JButton button_2 = new JButton("\u67E5\u770B\u804A\u5929\u8BB0\u5F55");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String his = null;
				try {
					his = controler.getHistory(toUserName);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("无法从数据库获取聊天记录！");
					e1.printStackTrace();
				}
				if( his == null||his.equals("null")) {
					his = "";
				}
				new HistoryWindow(history.get(toUserName)+his,toUserName,controler);
			}
		});
		button_2.setHorizontalAlignment(SwingConstants.LEFT);
		button_2.setBounds(565, 307, 123, 27);
		this.getContentPane().add(button_2);
		
		JLabel label = new JLabel("\u597D\u53CB\u5217\u8868\uFF1A");
		label.setBackground(null);
		label.setFont(new Font("宋体", Font.PLAIN, 17));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 224, 27);
		this.getContentPane().add(label);
		
		textArea = new JTextArea();
		textArea.setBounds(227, 337, 468, 90);
		this.getContentPane().add(textArea);
		
		JButton button_3 = new JButton("\u5220\u9664\u8BE5\u8054\u7CFB\u4EBA");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		button_3.setBounds(551, 1, 137, 27);
		getContentPane().add(button_3);
		this.setVisible(true);
		listenTread = new Thread(new ClientTread(socket,this));
		listenTread.start();
		
		this.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  for(String name:friends) {
					try {
						controler.saveHistory(name, history.get(name));
						System.out.println(history.get(name));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println("数据库保存聊天记录错误");
					}
		    	  }
		        }
		      public void windowClosed(WindowEvent e) {
		    	  for(String name:friends) {
					try {
						controler.saveHistory(name, history.get(name));
						System.out.println(history.get(name));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println("数据库保存聊天记录错误");
					}
		    	  }
		        }
		      });
	}
	
	//选择要发送的文件
	private static String showFileOpenDialog(Component parent) {
		JFileChooser fileChooser = new JFileChooser();
		File rFile = null;
		//设置当前文件夹位置
		fileChooser.setCurrentDirectory(new File("."));
		//设置文件选择模式，这里为仅文件可选
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//不可多选文件，一次只能传一个文件
		fileChooser.setMultiSelectionEnabled(false);
		//添加可用的文件过滤器，第一个参数是描述，后面是需要过滤的文件扩展名
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
		//设置默认文件过滤器
		fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
		int result = fileChooser.showOpenDialog(button_1);
		if(result == JFileChooser.APPROVE_OPTION) {
			rFile = fileChooser.getSelectedFile();
			//File[] files = fileChooser.getSelectedFile();
		}
		return rFile.getAbsolutePath();
	}
	
	//将接收到的信息显示在聊天面板上
	public void showMassage(String fromUser,String toUser,String massage,boolean isUser) {
		
    	if(isUser == true) {
    		StyledDocument d = textPaneMap.get(toUser).getStyledDocument();
    		SimpleAttributeSet attr = new SimpleAttributeSet();
    		SimpleAttributeSet attr2 = new SimpleAttributeSet();
    		
        	StyleConstants.setForeground(attr, new Color(100,149,237));
        	StyleConstants.setForeground(attr2, Color.black);
    	try {
			d.insertString(d.getLength(),fromUser+" "+getTime()+"\n",attr);
			d.insertString(d.getLength(), massage+"\n", attr2);
			String temp = history.get(toUser)+fromUser+" "+getTime()+"\n"+massage+"\n";
			history.replace(toUser, temp);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	}else {
    		StyledDocument d = textPaneMap.get(fromUser).getStyledDocument();
    		SimpleAttributeSet attr = new SimpleAttributeSet();
    		SimpleAttributeSet attr2 = new SimpleAttributeSet();
    		
        	StyleConstants.setForeground(attr, new Color(124,205,124));
        	StyleConstants.setForeground(attr2, Color.black);
        	
        	try {
    			d.insertString(d.getLength(),fromUser+" "+getTime()+"\n",attr);
    			d.insertString(d.getLength(), massage+"\n", attr2);
    			String temp2 = history.get(fromUser)+fromUser+" "+getTime()+"\n"+massage+"\n";
    			history.replace(fromUser, temp2);
    		} catch (BadLocationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
	}
	
	//获取接受到信息的时间
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		String result = cal.get(Calendar.YEAR)+"/"+ cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DATE)+" "+
							cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
		return result;
	}
	//发送指定的信息到服务器
	public void sendMassage(String toUserName,String massage,int mode) throws IOException {
		System.out.println("发送的有用信息为："+toUserName+" "+massage);
		switch(mode) {
		case ChatMode:bw.write(HeadString.Chat+HeadString.SomeBody+toUserName+HeadString.SomeBody+HeadString.splitChar+HeadString.Massage+massage+HeadString.Massage+HeadString.Chat+"\n");
					 bw.flush();
			break;
//		case LoginMode:bw.write(HeadString.LogIn
//				+HeadString.UserName+toUserName+HeadString.UserName
//				+HeadString.splitChar
//				+HeadString.PassWord+massage+HeadString.PassWord
//				+HeadString.LogIn
//				+"\n");
//			break;
		case EditMode:
			break;
		default:System.out.println("发送模式选择错误！");
		}
	}
	//如果选择ok返回true，选择cancel返回false
	private void showDialog(String warning) {
			int result = JOptionPane.showConfirmDialog(button ,warning,
					"提示信息",JOptionPane.OK_OPTION);
		}
}
