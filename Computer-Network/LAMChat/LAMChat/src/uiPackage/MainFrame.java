package uiPackage;

import java.awt.Color;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import dataStructure.ContentProvider;
import dataStructure.User;
import toolPackage.Encoder;
import toolPackage.Handler;
import toolPackage.IPtool;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.JProgressBar;

/**
 * * @author 作者 :GiantJun @date 创建时间：2019年6月12日 下午1:14:26 @version
 * 1.0 @parameter @return @throws
 */
public class MainFrame extends JFrame {

	// 这个主视窗中，是这个程序里第一个定义端口号的，其他组件都依据此端口号
	private static int port = 6001; // 本地程序端口
	private int port1 = 60001; // 连接方程序端口
	private ContentProvider contentProvider = null;
	private Handler myHandler = null;

	protected User myself = null; // 自己的信息
	protected String toUserName = null; // 聊天中指向的聊天对象
	// 用户列表的图形化序列list
	private DefaultListModel listmodel = new DefaultListModel();

	private HashMap<String, String> history = new HashMap();

	private JTextField textField_2;

	private JTextArea textArea;
	private JScrollPane scrollPane = null;
	private HashMap<String, JTextPane> textPaneMap = new HashMap<>();
	private JTextPane priviousTextPane = null;
	private JTextPane nullTextPane = null;
	public static JButton button_1;
	private JButton button = null;
	//用户列表
	private JList friendsList = null;
	
	private boolean isTextV = true; // 布尔变量保存“空用户表”显示文本框的可视化状态
	private JTextField textField_1;

	public JProgressBar progressBar = null;	//文件上傳進度條
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame(port+"",IPtool.getMyIP());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.constructor
	 */
	public MainFrame(String userName) {
		this.myself = new User(userName,IPtool.getMyIP(),IPtool.getMyMask(),port+"");
		chatInit(this,true,null);
		initialize();
		System.out.println("本地主机的子网掩码是：" + IPtool.getMyMask());
		System.out.println("本地的IP地址是：" + IPtool.getMyIP());
		System.out.println("程序端口是：" + port);

	}
	
	public MainFrame(String userName,String ip) {
		this.myself = new User(userName,IPtool.getMyIP(),IPtool.getMyMask(),port+"");
		chatInit(this,false,ip);
		initialize();
		System.out.println("本地主机的子网掩码是：" + IPtool.getMyMask());
		System.out.println("本地的IP地址是：" + IPtool.getMyIP());
		System.out.println("程序端口是：" + port);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
		this.setVisible(true);
		this.setBounds(100, 100, 785, 581);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setTitle("JavaChat局域网P2P聊天  " + "(用户名：" + myself.name + "）");
		this.setBackground(new Color(65, 105, 225));
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		button = new JButton("\u53D1\u9001");
		button.setForeground(Color.WHITE);
		button.setFont(new Font("黑体", Font.PLAIN, 18));
		button.setBackground(new Color(1, 136, 251));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (toUserName != null) {
					String message = textArea.getText();
					if (!message.equals("") && message != null) {
						textArea.setText("");
						try {
							sendMassage(toUserName, message);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("无法发送信息！");
							e1.printStackTrace();
						}
						showMassage(myself.name, toUserName, message, true);
					} else {
						showDialog("发送的消息不能为空！");
					}
				} else {
					showDialog("请先选择聊天对象！");
				}
			}
		});
		button.setBounds(652, 504, 113, 29);
		this.getContentPane().add(button);

		// 初始化好友列表
		friendsList = new JList(listmodel);
		// 设置选择为只支持单选
		friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 设置JList的画单元格方法
		friendsList.setCellRenderer(new ImageCellRenderer(contentProvider.getUserMap()));
		// 设置好友列表的监听器
		friendsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// 加if条件只响应一次选择事件
				if (arg0.getValueIsAdjusting()) {
					// 获取所有被选中的选项索引，但是只选用第一个
					int[] indices = friendsList.getSelectedIndices();
					// 获取选项数据的listModel
					ListModel<String> listModel = friendsList.getModel();
					// 输出选中的第一项
					toUserName = listModel.getElementAt(indices[0]);

					textField_2.setText("正在聊天的对象是：" + toUserName);
					if (priviousTextPane != null) {
						priviousTextPane.setVisible(false);
					}
					priviousTextPane = textPaneMap.get(toUserName);
					priviousTextPane.setVisible(true);

					textArea.setText("");
					
					progressBar.setValue(0);
				}
			}

		});
		scrollPane = new JScrollPane(friendsList);
		scrollPane.setBounds(0, 35, 220, 500);
		scrollPane.setVisible(false);
		scrollPane.setBackground(Color.WHITE);
		this.getContentPane().add(scrollPane);
		this.getContentPane().add(scrollPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(new Color(240, 240, 240));
		toolBar.setBounds(237, 330, 256, 36);
		this.getContentPane().add(toolBar);

		button_1 = new JButton("\u53D1\u9001\u6587\u4EF6");
		button_1.setFont(new Font("宋体", Font.PLAIN, 18));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			// 发送文件操作
			public void mouseClicked(MouseEvent e) {
				if (toUserName == null) {
					showDialog("请先选择要发送文件的对象！");
				} else {
					String address = showFileOpenDialog(button_1,false);
					if(address != null)
					try {
						myHandler.sendReady(contentProvider.getConnectionMap().get(toUserName), address);
					} catch (IOException e1) {
						System.out.println("无法发送发送文件请求！！！");
					}
				}
				progressBar.setValue(0);
			}
		});
		toolBar.add(button_1);

		JButton button_2 = new JButton("");
		button_2.setEnabled(false);
		button_2.setVisible(false);
		toolBar.add(button_2);

		JButton btnNewButton = new JButton("\u8BBE\u7F6E\u4FDD\u5B58\u8DEF\u5F84");
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 18));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			// 設置默認保存路徑
			public void mouseClicked(MouseEvent e) {

				String address = showFileOpenDialog(btnNewButton,true);
				myHandler.setSavePath(address);
			}
		});
		toolBar.add(btnNewButton);

		//顯示正在聊天對象的顯示框
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(237, 1, 389, 35);
		textField_2.setFont(new Font("黑体", Font.PLAIN, 17));
		textField_2.setBackground(Color.white);
		this.getContentPane().add(textField_2);
		textField_2.setColumns(10);

		//空白聊天記錄顯示
		priviousTextPane = new JTextPane();
		nullTextPane = priviousTextPane;
		priviousTextPane.setFont(new Font("SimSun", Font.PLAIN, 15));
		priviousTextPane.setEditable(false);
		priviousTextPane.setBounds(236, 48, 529, 279);
		priviousTextPane.setVisible(true);
		this.getContentPane().add(priviousTextPane);

		JLabel label = new JLabel("\u597D\u53CB\u5217\u8868\uFF1A");
		label.setBackground(null);
		label.setFont(new Font("宋体", Font.PLAIN, 19));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 220, 35);
		this.getContentPane().add(label);
		JScrollPane textScrollPane = new JScrollPane();
		textScrollPane.setBounds(236, 369, 529, 124);
		// textArea.setBounds(227, 337, 468, 90);
		this.getContentPane().add(textScrollPane);

		// 聊天输入框
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 17));
		textArea.setColumns(20);
		textScrollPane.setViewportView(textArea);

		// 没用用户上线时显示“空用户列表”的文本显示框
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setText("（当前用户列表为空）");
		textField_1.setFont(new Font("宋体", Font.PLAIN, 17));
		textField_1.setBounds(0, 35, 220, 500);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setBackground(null);
		textField_1.setVisible(isTextV);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(507, 340, 224, 14);
		//設置進度條邊框
		progressBar.setBorderPainted(true);
		//設置進度條的最大最小值
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		//設置當前進度條的值
		progressBar.setValue(0);
		//繪製百分比文本
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar);
		
		JButton button_3 = new JButton("\u4E3B\u52A8\u8FDE\u63A5");
		button_3.setBounds(640, 9, 113, 27);
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			// 設置默認保存路徑
			public void mouseClicked(MouseEvent e) {
				new ConnectFrame(myHandler,myself,port1,contentProvider);
			}
		});
		toolBar.add(btnNewButton);
		getContentPane().add(button_3);
		
//		//删除联系人按钮
//		JButton button_3 = new JButton("\u5220\u9664\u8BE5\u8054\u7CFB\u4EBA");
//		button_3.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//			}
//		});
//		button_3.setBounds(551, 1, 137, 27);
//		getContentPane().add(button_3);
//		this.setVisible(true);

	}

	private void chatInit(MainFrame frame,boolean isBroadcast,String ip) {

		// 创建底层传输控制器
		myHandler = new Handler();
		// 创建内容提供器
		contentProvider = new ContentProvider(myself);
		// 开启服务端和客户端
		try {
			myHandler.initServer(port);

			// 开启监听通道状态的线程
			ServerThread serverThread = new ServerThread(myHandler, contentProvider, frame);
			new Thread(serverThread).start();


			if(isBroadcast) {
				// 在本局域网内发送广播
				BroadcastThread broadcastThread = new BroadcastThread(myHandler,myself,port);
				new Thread(broadcastThread).start();
			}else {
				try {
					myHandler.initClient(myself, ip, port1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 选择要发送的文件
	private static String showFileOpenDialog(Component parent,boolean isSetPath) {
		JFileChooser fileChooser = new JFileChooser();
		File rFile = null;
		// 设置当前文件夹位置
		fileChooser.setCurrentDirectory(new File("."));
		// 设置文件选择模式，这里为仅文件可选或文件夾可選
		if(isSetPath) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}else {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// 添加可用的文件过滤器，第一个参数是描述，后面是需要过滤的文件扩展名
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
			// 设置默认文件过滤器
			fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
		}
		// 不可多选文件，一次只能传一个文件
		fileChooser.setMultiSelectionEnabled(false);
		
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			rFile = fileChooser.getSelectedFile();
			// File[] files = fileChooser.getSelectedFile();
		}
		return rFile.getAbsolutePath();
	}

	// 当用户列表变化是更新用户表
	public void addFriendList(String name) {
		history.put(name, "");
		listmodel.addElement(name);
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(236, 48, 529, 279);
		textPane.setVisible(true);
		textPane.setFont(new Font("SimSun", Font.PLAIN, 17));
		this.getContentPane().add(textPane);
		textPaneMap.put(name, textPane);
		if (isTextV) {
			isTextV = false;
			textField_1.setVisible(isTextV);
			scrollPane.setVisible(true);
		}
		showDialog("有一个新的伙伴上线了！！！");
	}

	// 当用户列表变化是更新用户表
	public void removeFriendList(String name) {
		history.remove(name);
		this.getContentPane().remove(textPaneMap.get(name));
		listmodel.removeElement(name);

		textPaneMap.get(name).setVisible(false);
		textPaneMap.remove(name);
		if (contentProvider.getUserMap().isEmpty()) {
			isTextV = true;
			textField_1.setVisible(isTextV);
			scrollPane.setVisible(false);
			toUserName = null;
		}
		textField_2.setText("");
		nullTextPane.setVisible(true);
		showDialog(name + "下线了！！！");
		
		progressBar.setValue(0);
	}

	// 将接收到的信息显示在聊天面板上
	public void showMassage(String fromUser, String toUser, String massage, boolean isUser) {

		if (isUser == true) {
			StyledDocument d = textPaneMap.get(toUser).getStyledDocument();
			SimpleAttributeSet attr = new SimpleAttributeSet();
			SimpleAttributeSet attr2 = new SimpleAttributeSet();

			StyleConstants.setForeground(attr, new Color(100, 149, 237));
			StyleConstants.setForeground(attr2, Color.black);
			try {
				d.insertString(d.getLength(), fromUser + " " + getTime() + "\n", attr);
				d.insertString(d.getLength(), massage + "\n", attr2);
				String temp = history.get(toUser) + fromUser + " " + getTime() + "\n" + massage + "\n";
				history.replace(toUser, temp);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			StyledDocument d = textPaneMap.get(fromUser).getStyledDocument();
			SimpleAttributeSet attr = new SimpleAttributeSet();
			SimpleAttributeSet attr2 = new SimpleAttributeSet();

			StyleConstants.setForeground(attr, new Color(124, 205, 124));
			StyleConstants.setForeground(attr2, Color.black);

			try {
				d.insertString(d.getLength(), fromUser + " " + getTime() + "\n", attr);
				d.insertString(d.getLength(), massage + "\n", attr2);
				String temp2 = history.get(fromUser) + fromUser + " " + getTime() + "\n" + massage + "\n";
				history.replace(fromUser, temp2);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//显示当前对话聊天记录窗，选中用户
			textField_2.setText("正在聊天的对象是：" + fromUser);
			if (priviousTextPane != null) {
				priviousTextPane.setVisible(false);
			}
			priviousTextPane = textPaneMap.get(fromUser);
			priviousTextPane.setVisible(true);
			//选中用户列表
			for(int i = 0; i < listmodel.size(); i++) {
				if(listmodel.getElementAt(i).equals(fromUser)) {
					if(!friendsList.isSelectedIndex(i)) {
						friendsList.setSelectedIndex(i);
					}
				}
			}
			toUserName = fromUser;
			textArea.setText("");
			
			progressBar.setValue(0);
		}
		
	}

	// 获取接受到信息的时间
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		String result = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) + " "
				+ cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		return result;
	}

	// 发送指定的信息到对等方
	public void sendMassage(String toUserName, String massage) throws IOException {
		myHandler.sendMessage(Encoder.encodeMessage(massage, myself.name),
				contentProvider.getConnectionMap().get(toUserName));
	}

	// 如果选择ok返回true，选择cancel返回false
	private boolean showDialog(String warning) {
		return JOptionPane.showConfirmDialog(this, warning, "提示信息", JOptionPane.OK_OPTION)==JOptionPane.OK_OPTION;
	}
	
	public boolean showNotice(String warning) {
		return showDialog(warning);
	}
}
