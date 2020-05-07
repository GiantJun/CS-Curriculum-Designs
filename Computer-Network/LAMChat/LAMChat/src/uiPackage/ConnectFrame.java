package uiPackage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructure.User;
import dataStructure.ContentProvider;
import toolPackage.Handler;
import toolPackage.IPtool;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * * @author 作者 :GiantJun @date 创建时间：2019年7月2日 上午10:31:31 @version 1.0
 * <p>Description: </p> @throws
 */
public class ConnectFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblip;
	private JLabel lblNewLabel;
	private JLabel label;
	private JLabel label_1;
	private JTextField textField_1;

	private JFrame myself = null;
	public String result = null;

	/**
	 * Create the frame.
	 */
	public ConnectFrame(Handler handler, User user, int port,ContentProvider contentProvider) {
		myself = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 274);
		contentPane = new JPanel();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setEnabled(true);
		this.setTitle("主动连接局域网内IP地址输入");
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JButton button = new JButton("\u786E\u5B9A");
		button.setFont(new Font("宋体", Font.PLAIN, 17));
		button.setBounds(81, 166, 113, 27);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String ip1 = textField.getText();
				String ip2 = textField_1.getText();
				if (ip1 == null || ip2 == null) {
					JOptionPane.showConfirmDialog(contentPane, "请输入正确的IP地址，两个字段都必须在【0~255】的范围内！", "提示信息",
							JOptionPane.OK_OPTION);
				} else if (ip1.equals("") || ip2.equals("")) {
					JOptionPane.showConfirmDialog(contentPane, "请输入正确的IP地址，两个字段都必须在【0~255】的范围内！", "提示信息",
							JOptionPane.OK_OPTION);
				} else if (Integer.parseInt(ip1) > 255 || Integer.parseInt(ip1) < 0 || Integer.parseInt(ip2) > 255
						|| Integer.parseInt(ip2) < 0) {
					JOptionPane.showConfirmDialog(contentPane, "请输入正确的IP地址，两个字段都必须在【0~255】的范围内！", "提示信息",
							JOptionPane.OK_OPTION);
				} else {
					result = "192.168." + ip1 + "." + ip2;
					if(contentProvider.isConnectExist(result)){
						JOptionPane.showConfirmDialog(contentPane, "您想连接的IP地址已存在！", "提示信息",
								JOptionPane.OK_OPTION);
					}else if(result.equals(IPtool.getMyIP())) {
						JOptionPane.showConfirmDialog(contentPane, "您输入的是您本地的IP地址，请输入非本主机的IP地址！", "提示信息",
								JOptionPane.OK_OPTION);
					}else {
						JOptionPane.showConfirmDialog(contentPane, "连接的过程中可能需要消耗一定的时间，请稍等！", "提示信息",
								JOptionPane.OK_OPTION);
						myself.setVisible(false);
						myself.setEnabled(false);
						try {
							if(handler.initClient(user, result, port)) {
								JOptionPane.showConfirmDialog(contentPane, "连接"+result+"成功！", "提示信息",
										JOptionPane.OK_OPTION);
							}else {
								JOptionPane.showConfirmDialog(contentPane, "连接"+result+"失败！", "提示信息",
										JOptionPane.OK_OPTION);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				}
			}
		});
		contentPane.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.setFont(new Font("宋体", Font.PLAIN, 17));
		button_1.setBounds(217, 166, 113, 27);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				result = null;
				myself.setVisible(false);
				myself.setEnabled(false);
				;
			}
		});
		contentPane.add(button_1);

		textField = new JTextField();
		textField.setFont(new Font("宋体", Font.PLAIN, 17));
		textField.setBounds(155, 114, 67, 24);
		contentPane.add(textField);
		textField.setColumns(10);

		lblip = new JLabel("【提示】：请输入您想连接的本局域");
		lblip.setFont(new Font("宋体", Font.PLAIN, 17));
		lblip.setBounds(54, 36, 302, 18);
		contentPane.add(lblip);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 17));
		lblNewLabel.setText("\u7F51\u5185\u7684IP\u5730\u5740\uFF1A");
		lblNewLabel.setBounds(140, 67, 132, 24);
		contentPane.add(lblNewLabel);

		label = new JLabel("192 : 168 :");
		label.setFont(new Font("宋体", Font.PLAIN, 17));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(43, 114, 118, 24);
		contentPane.add(label);

		label_1 = new JLabel(":");
		label_1.setFont(new Font("宋体", Font.PLAIN, 17));
		label_1.setBounds(234, 117, 14, 18);
		contentPane.add(label_1);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("宋体", Font.PLAIN, 17));
		textField_1.setColumns(10);
		textField_1.setBounds(251, 114, 67, 24);
		contentPane.add(textField_1);
	}

}
