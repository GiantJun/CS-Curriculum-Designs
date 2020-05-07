package uiPackage;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * * @author 作者 :GiantJun @date 创建时间：2019年6月12日 下午1:12:46 @version
 * 1.0 @parameter @return @throws
 */
public class LogIn {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public LogIn() {
		initialize();
	}

	private JTextField textField;
	private JTextField textField_2;
	
	private JRadioButton radioButton = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("用户登陆");
		frame.setBounds(100, 100, 501, 337);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
		label.setFont(new Font("宋体", Font.PLAIN, 17));
		label.setBounds(71, 109, 72, 18);
		frame.getContentPane().add(label);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setBackground(new Color(255, 255, 255));
		textField.setBounds(157, 103, 190, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(1);

		JLabel lblJavachat = new JLabel("\u5C40\u57DF\u7F51P2P\u804A\u5929\u7CFB\u7EDF");
		lblJavachat.setHorizontalAlignment(SwingConstants.CENTER);
		lblJavachat.setBounds(115, 26, 253, 49);
		lblJavachat.setFont(new Font("Dialog", Font.BOLD, 24));
		frame.getContentPane().add(lblJavachat);

		frame.setVisible(true);

		JButton button = new JButton("\u767B\u9646");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName = textField.getText();
				if (userName == null || userName.equals("") ) {
					showDialog("请正确输入用户名！)");
				} else {
					System.out.println("你的用户名是：" + userName);
					
					if(radioButton.isSelected()) {
						showDialog("正在向所在局域网发送广播，这会消耗较长时间，请稍等");
						frame.setVisible(false);
						new MainFrame(userName);
					}else {
						String connectIP = textField_2.getText();
						if(connectIP == null || connectIP.equals("")) {
							showDialog("請輸入正確的指定連接IP地址！");
						}else {
							frame.setVisible(false);
							new MainFrame(userName,connectIP);
						}
					}
				}
			}
		});
		button.setBounds(163, 235, 138, 42);
		frame.getContentPane().add(button);
		
		JLabel lblip = new JLabel("\u6307\u5B9A\u9023\u63A5IP\uFF1A");
		lblip.setFont(new Font("宋体", Font.PLAIN, 17));
		lblip.setBounds(54, 148, 103, 33);
		frame.getContentPane().add(lblip);
		
		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setText("（不可輸入）");
		textField_2.setBackground(new Color(238, 238, 238));
		textField_2.setColumns(1);
		textField_2.setEditable(false);
		textField_2.setBounds(157, 149, 190, 33);
		frame.getContentPane().add(textField_2);
		
		//廣播單選按鈕
		radioButton = new JRadioButton("\u5EE3\u64AD");
		radioButton.setBounds(157, 199, 72, 27);
		radioButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				if(radioButton.isSelected()) {
					textField_2.setText("\uFF08\u4E0D\u53EF\u8F38\u5165\uFF09");
					textField_2.setBackground(new Color(238, 238, 238));
					textField_2.setHorizontalAlignment(JTextField.CENTER);
					textField_2.setEditable(false);
				}else {
					textField_2.setText("");
					textField_2.setBackground(new Color(255, 255, 255));
					textField_2.setHorizontalAlignment(JTextField.LEFT);
					textField_2.setEditable(true);
				}
			}
			
		});
		
		JRadioButton radioButton_1 = new JRadioButton("\u6307\u5B9A\u9023\u63A5");
		radioButton_1.setBounds(230, 200, 95, 27);
		
		
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(radioButton);
		btnGroup.add(radioButton_1);
		radioButton.setSelected(true);
		
		frame.getContentPane().add(radioButton);
		frame.getContentPane().add(radioButton_1);
		
//		textField_1 = new JTextField();
//		textField_1.setBounds(157, 172, 190, 33);
//		frame.getContentPane().add(textField_1);
//		textField_1.setColumns(1);
//		
//		label_1 = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
//		label_1.setBounds(71, 179, 72, 18);
//		frame.getContentPane().add(label_1);

	}

	// 如果选择ok返回true，选择cancel返回false
	private boolean showDialog(String warning) {
		int result = JOptionPane.showConfirmDialog(frame, warning, "连接通知", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
			return true;
		return false;
	}
}
