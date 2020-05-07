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
 * * @author ���� :GiantJun @date ����ʱ�䣺2019��6��12�� ����1:14:26 @version
 * 1.0 @parameter @return @throws
 */
public class MainFrame extends JFrame {

	// ������Ӵ��У�������������һ������˿ںŵģ�������������ݴ˶˿ں�
	private static int port = 6001; // ���س���˿�
	private int port1 = 60001; // ���ӷ�����˿�
	private ContentProvider contentProvider = null;
	private Handler myHandler = null;

	protected User myself = null; // �Լ�����Ϣ
	protected String toUserName = null; // ������ָ����������
	// �û��б��ͼ�λ�����list
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
	//�û��б�
	private JList friendsList = null;
	
	private boolean isTextV = true; // �����������桰���û�����ʾ�ı���Ŀ��ӻ�״̬
	private JTextField textField_1;

	public JProgressBar progressBar = null;	//�ļ��ς��M�ȗl
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
		System.out.println("�������������������ǣ�" + IPtool.getMyMask());
		System.out.println("���ص�IP��ַ�ǣ�" + IPtool.getMyIP());
		System.out.println("����˿��ǣ�" + port);

	}
	
	public MainFrame(String userName,String ip) {
		this.myself = new User(userName,IPtool.getMyIP(),IPtool.getMyMask(),port+"");
		chatInit(this,false,ip);
		initialize();
		System.out.println("�������������������ǣ�" + IPtool.getMyMask());
		System.out.println("���ص�IP��ַ�ǣ�" + IPtool.getMyIP());
		System.out.println("����˿��ǣ�" + port);

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
		this.setTitle("JavaChat������P2P����  " + "(�û�����" + myself.name + "��");
		this.setBackground(new Color(65, 105, 225));
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		button = new JButton("\u53D1\u9001");
		button.setForeground(Color.WHITE);
		button.setFont(new Font("����", Font.PLAIN, 18));
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
							System.out.println("�޷�������Ϣ��");
							e1.printStackTrace();
						}
						showMassage(myself.name, toUserName, message, true);
					} else {
						showDialog("���͵���Ϣ����Ϊ�գ�");
					}
				} else {
					showDialog("����ѡ���������");
				}
			}
		});
		button.setBounds(652, 504, 113, 29);
		this.getContentPane().add(button);

		// ��ʼ�������б�
		friendsList = new JList(listmodel);
		// ����ѡ��Ϊֻ֧�ֵ�ѡ
		friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// ����JList�Ļ���Ԫ�񷽷�
		friendsList.setCellRenderer(new ImageCellRenderer(contentProvider.getUserMap()));
		// ���ú����б�ļ�����
		friendsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// ��if����ֻ��Ӧһ��ѡ���¼�
				if (arg0.getValueIsAdjusting()) {
					// ��ȡ���б�ѡ�е�ѡ������������ֻѡ�õ�һ��
					int[] indices = friendsList.getSelectedIndices();
					// ��ȡѡ�����ݵ�listModel
					ListModel<String> listModel = friendsList.getModel();
					// ���ѡ�еĵ�һ��
					toUserName = listModel.getElementAt(indices[0]);

					textField_2.setText("��������Ķ����ǣ�" + toUserName);
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
		button_1.setFont(new Font("����", Font.PLAIN, 18));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			// �����ļ�����
			public void mouseClicked(MouseEvent e) {
				if (toUserName == null) {
					showDialog("����ѡ��Ҫ�����ļ��Ķ���");
				} else {
					String address = showFileOpenDialog(button_1,false);
					if(address != null)
					try {
						myHandler.sendReady(contentProvider.getConnectionMap().get(toUserName), address);
					} catch (IOException e1) {
						System.out.println("�޷����ͷ����ļ����󣡣���");
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
		btnNewButton.setFont(new Font("����", Font.PLAIN, 18));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			// �O��Ĭ�J����·��
			public void mouseClicked(MouseEvent e) {

				String address = showFileOpenDialog(btnNewButton,true);
				myHandler.setSavePath(address);
			}
		});
		toolBar.add(btnNewButton);

		//�@ʾ�������쌦����@ʾ��
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(237, 1, 389, 35);
		textField_2.setFont(new Font("����", Font.PLAIN, 17));
		textField_2.setBackground(Color.white);
		this.getContentPane().add(textField_2);
		textField_2.setColumns(10);

		//�հ�����ӛ��@ʾ
		priviousTextPane = new JTextPane();
		nullTextPane = priviousTextPane;
		priviousTextPane.setFont(new Font("SimSun", Font.PLAIN, 15));
		priviousTextPane.setEditable(false);
		priviousTextPane.setBounds(236, 48, 529, 279);
		priviousTextPane.setVisible(true);
		this.getContentPane().add(priviousTextPane);

		JLabel label = new JLabel("\u597D\u53CB\u5217\u8868\uFF1A");
		label.setBackground(null);
		label.setFont(new Font("����", Font.PLAIN, 19));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 220, 35);
		this.getContentPane().add(label);
		JScrollPane textScrollPane = new JScrollPane();
		textScrollPane.setBounds(236, 369, 529, 124);
		// textArea.setBounds(227, 337, 468, 90);
		this.getContentPane().add(textScrollPane);

		// ���������
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 17));
		textArea.setColumns(20);
		textScrollPane.setViewportView(textArea);

		// û���û�����ʱ��ʾ�����û��б����ı���ʾ��
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setText("����ǰ�û��б�Ϊ�գ�");
		textField_1.setFont(new Font("����", Font.PLAIN, 17));
		textField_1.setBounds(0, 35, 220, 500);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setBackground(null);
		textField_1.setVisible(isTextV);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(507, 340, 224, 14);
		//�O���M�ȗl߅��
		progressBar.setBorderPainted(true);
		//�O���M�ȗl�������Сֵ
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		//�O�î�ǰ�M�ȗl��ֵ
		progressBar.setValue(0);
		//�L�u�ٷֱ��ı�
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar);
		
		JButton button_3 = new JButton("\u4E3B\u52A8\u8FDE\u63A5");
		button_3.setBounds(640, 9, 113, 27);
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			// �O��Ĭ�J����·��
			public void mouseClicked(MouseEvent e) {
				new ConnectFrame(myHandler,myself,port1,contentProvider);
			}
		});
		toolBar.add(btnNewButton);
		getContentPane().add(button_3);
		
//		//ɾ����ϵ�˰�ť
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

		// �����ײ㴫�������
		myHandler = new Handler();
		// ���������ṩ��
		contentProvider = new ContentProvider(myself);
		// ��������˺Ϳͻ���
		try {
			myHandler.initServer(port);

			// ��������ͨ��״̬���߳�
			ServerThread serverThread = new ServerThread(myHandler, contentProvider, frame);
			new Thread(serverThread).start();


			if(isBroadcast) {
				// �ڱ��������ڷ��͹㲥
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

	// ѡ��Ҫ���͵��ļ�
	private static String showFileOpenDialog(Component parent,boolean isSetPath) {
		JFileChooser fileChooser = new JFileChooser();
		File rFile = null;
		// ���õ�ǰ�ļ���λ��
		fileChooser.setCurrentDirectory(new File("."));
		// �����ļ�ѡ��ģʽ������Ϊ���ļ���ѡ���ļ��A���x
		if(isSetPath) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}else {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// ��ӿ��õ��ļ�����������һ����������������������Ҫ���˵��ļ���չ��
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
			// ����Ĭ���ļ�������
			fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
		}
		// ���ɶ�ѡ�ļ���һ��ֻ�ܴ�һ���ļ�
		fileChooser.setMultiSelectionEnabled(false);
		
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			rFile = fileChooser.getSelectedFile();
			// File[] files = fileChooser.getSelectedFile();
		}
		return rFile.getAbsolutePath();
	}

	// ���û��б�仯�Ǹ����û���
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
		showDialog("��һ���µĻ�������ˣ�����");
	}

	// ���û��б�仯�Ǹ����û���
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
		showDialog(name + "�����ˣ�����");
		
		progressBar.setValue(0);
	}

	// �����յ�����Ϣ��ʾ�����������
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
			//��ʾ��ǰ�Ի������¼����ѡ���û�
			textField_2.setText("��������Ķ����ǣ�" + fromUser);
			if (priviousTextPane != null) {
				priviousTextPane.setVisible(false);
			}
			priviousTextPane = textPaneMap.get(fromUser);
			priviousTextPane.setVisible(true);
			//ѡ���û��б�
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

	// ��ȡ���ܵ���Ϣ��ʱ��
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		String result = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) + " "
				+ cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		return result;
	}

	// ����ָ������Ϣ���Եȷ�
	public void sendMassage(String toUserName, String massage) throws IOException {
		myHandler.sendMessage(Encoder.encodeMessage(massage, myself.name),
				contentProvider.getConnectionMap().get(toUserName));
	}

	// ���ѡ��ok����true��ѡ��cancel����false
	private boolean showDialog(String warning) {
		return JOptionPane.showConfirmDialog(this, warning, "��ʾ��Ϣ", JOptionPane.OK_OPTION)==JOptionPane.OK_OPTION;
	}
	
	public boolean showNotice(String warning) {
		return showDialog(warning);
	}
}
