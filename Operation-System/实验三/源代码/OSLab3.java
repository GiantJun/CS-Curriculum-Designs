package OSLab3;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class OSLab3 {

	private final int SAMLLISTSIZE = 5; // �����ֵ���С����
	private final int FF = 1000; // �״���Ӧ�㷨��ʶ
	private final int NF = 2000; // ѭ���״���Ӧ�㷨��ʶ
	private final int BF = 3000; // �����Ӧ�㷨��ʶ
	private final int WF = 4000; // ���Ӧ�㷨��ʶ

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTable table;
	private JLabel noteLabel;	//��ʾ��������Ϣ

	private MyNode header; // ͷָ��
	private int mode = FF; // ��ʶ��ѡ�ķ����㷨
	private MyNode NFNode = null; // ѭ���״���Ӧר��ָ��

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OSLab3 window = new OSLab3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OSLab3() {
		initialize();
	}

	// ����ָ�����ֵ���ҵ
	public boolean recycle(String name) {
		// �ȶ�λ��ָ���Ľڵ�
		MyNode node = header;
		boolean isRecycled=false;
		while (node != null) {
			if (node.getData().getName().equals(name)) {
				break;
			}
			node = node.next;
		}
		// ���޷��ҵ�����˵�������ڸýڵ�
		if (node == null) {
			noteLabel.setText("���з��������в�����"+name+"�ڵ�");
			return false;
		}
		// ���ҵ��Ľڵ�Ϊ���з������򷵻�false
		if (node.getData().getStatus().equals(Status.EMPTY)) {
			noteLabel.setText(name+"����һ��ռ�÷���������Ҫ����");
			return false;
		}
		// ���ҵ�ָ�����ֵĽڵ㣬�ȼ�¼�¸ýڵ�ĸ��ڵ���ӽڵ�
		MyNode myParent = node.parent;
		MyNode myNext = node.next;
		String message = "���յķ����У�"+name;
		// �ж�ǰ��Ľڵ��Ƿ���Ժϲ�
		if(myParent!=null){
			if (myParent.getData().getStatus().equals(Status.EMPTY)) {
				// ˵��ǰһ���������Ժϲ�����ϲ�
				StoreArea area = node.deleteNode(); // ɾ���˸ýڵ�
				myParent.getData().merge(area);
				node = myParent; // �������ϲ���һ������
				message = message +"��"+myParent.getData().getName();
				isRecycled=true;
			}
		}
		if(myNext!=null){
			if (myNext.getData().getStatus().equals(Status.EMPTY)) {
				// ˵����һ���������Ժϲ�
				StoreArea area1 = myNext.deleteNode(); // ɾ�����ӽڵ�
				node.getData().merge(area1);
				node.getData().setStatus(Status.EMPTY);
				message = message + "��"+area1.getName();
				isRecycled=true;
			}
		}
		if(!isRecycled){
			node.getData().setStatus(Status.EMPTY);
		}
		noteLabel.setText(message);
		return true;
	}

	public boolean distribute(String newName, int size) {
		MyNode node = header;
		MyNode myParent;
		if (mode == FF) {
			// ʹ���״���Ӧ�㷨
			while (node != null) {
				if (node.getData().getStatus().equals(Status.EMPTY)) {
					// ������δ��ռ�ã����Է���
					StoreArea area = node.getData().splitArea(newName, size);
					if (area != null) {
						// ˵���÷�������ϸ�֣������ɹ�
						myParent = node.parent;
						if (myParent == null) {
							// ˵��Ϊͷ�ڵ�
							MyNode newNode = new MyNode(area);
							newNode.insert(node);
							header = newNode;
							noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
							return true;
						} else {
							// ˵������ͷ�ڵ�
							MyNode newNode = new MyNode(area);
							myParent.insert(newNode);
							noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
							return true;
						}
					}
				}
				node = node.next;
			}
		} else if (mode == NF) {
			// ʹ�������Ӧ�㷨
			if (NFNode == null) {
				NFNode = header;
			}
			// ���µ�ǰָ��ָ��Ķ��󣬷�ֹ��������ѭ��
			MyNode label = NFNode;
			// ���ಿ�ִ������״���Ӧ�㷨����,ֻ�ǽ�node��ΪNFNode
			while (true) {
				if (NFNode.getData().getStatus().equals(Status.EMPTY)) {
					// ������δ��ռ�ã����Է���
					StoreArea area = NFNode.getData().splitArea(newName, size);
					if (area != null) {
						// ˵���÷�������ϸ�֣������ɹ�
						myParent = NFNode.parent;
						if (myParent == null) {
							// ˵��Ϊͷ�ڵ�
							MyNode newNode = new MyNode(area);
							newNode.insert(NFNode);
							header = newNode;
							noteLabel.setText(newName+" �ɹ��� " +NFNode.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
							// �����㷨˼�룬��ָ��ָ����һ��������
							NFNode = NFNode.next;
							return true;
						} else {
							// ˵������ͷ�ڵ�
							MyNode newNode = new MyNode(area);
							myParent.insert(newNode);
							noteLabel.setText(newName+" �ɹ��� " +NFNode.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
							// �����㷨˼�룬��ָ��ָ����һ��������
							NFNode = NFNode.next;
							return true;
						}
					}
				}
				NFNode = NFNode.next;
				if (NFNode == null) {
					// ˵������������β
					NFNode = header;
				}
				if (NFNode == label) {
					// ˵���Ѿ������������һ�Σ����Ҳ������ʷ���
					noteLabel.setText(" ���з����������޺��ʵķ����ɹ�"+newName+"����ʹ��");
					return false;
				}
			}
		} else if (mode == BF) {
			// ���������Ӧ�㷨
			// ���ȱ�����������Ҫ����������С�Ŀ��з���
			MyNode temp = header;
			while (temp != null) {
				// �жϸýڵ��Ƿ�����Ҫ�󣬼���С������Ϊ���п�
				if (temp.getData().isFit(size)) {
					if (!temp.getData().isBiger(node.getData())) {
						// ���Ѽ�¼�ڵ������µ�ǰ�ڵ�
						node = temp;
					}
				}
				temp = temp.next;
			}
			// �ٴ�ȷ����С�鲻��ռ�ú󣬿�ʼ����
			if (node.getData().getStatus().equals(Status.EMPTY)) {
				StoreArea area = node.getData().splitArea(newName, size);
				if (area != null) {
					// ˵���÷�������ϸ�֣������ɹ�
					myParent = node.parent;
					if (myParent == null) {
						// ˵��Ϊͷ�ڵ�
						MyNode newNode = new MyNode(area);
						newNode.insert(node);
						header = newNode;
						noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
						return true;
					} else {
						// ˵������ͷ�ڵ�
						MyNode newNode = new MyNode(area);
						myParent.insert(newNode);
						noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
						return true;
					}
				}
			}

		} else if (mode == WF) {
			// �������Ӧ�㷨��������ǰ�����ƣ�ֻ���ж�������ͬ
			// ���ȱ�����������Ҫ�����������Ŀ��з���
			MyNode temp = header;
			while (temp != null) {
				// �жϸýڵ��Ƿ�����Ҫ�󣬼���С������Ϊ���п�
				if (temp.getData().isFit(size) && temp.getData().getStatus().equals(Status.EMPTY)) {
					//����һ���ڵ�node��Ϊ���з��������ҵ���һ�������Һ��ʵķ�������ֵ��node
					if(node.getData().getStatus().equals(Status.FULL)){
						node=temp;
						continue;
					}
					if (temp.getData().isBiger(node.getData())) {
						// ���Ѽ�¼�ڵ������µ�ǰ�ڵ�
						node = temp;
					}
				}
				temp = temp.next;
			}
			// �ٴ�ȷ����С�鲻��ռ�ú󣬿�ʼ����
			if (node.getData().getStatus().equals(Status.EMPTY)) {
				StoreArea area = node.getData().splitArea(newName, size);
				if (area != null) {
					// ˵���÷�������ϸ�֣������ɹ�
					myParent = node.parent;
					if (myParent == null) {
						// ˵��Ϊͷ�ڵ�
						MyNode newNode = new MyNode(area);
						newNode.insert(node);
						header = newNode;
						noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
						return true;
					} else {
						// ˵������ͷ�ڵ�
						MyNode newNode = new MyNode(area);
						myParent.insert(newNode);
						noteLabel.setText(newName+" �ɹ��� " +node.getData().getName()+" �зֵ��˴�СΪ"+size+"���ڴ�ռ�");
						return true;
					}
				}
			}
		}

		return false;
	}

	// ���½���
	public void updateView() {
		MyNode node = header;
		int k = 0;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		StoreArea area = null;
		// ���������
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < 4; j++) {
				table.setValueAt("", i, j);
			}
		}
		while (node != null) {
			area = node.getData();
			String statusString;
			if (area.getStatus().equals(Status.EMPTY)) {
				statusString = "����";
			} else {
				statusString = "ռ��";
			}
			if (k >= model.getRowCount()) {
				model.addRow(new Object[] { area.getName(), area.getSize(), area.getStartIndex(), statusString });
			} else {
				model.setValueAt(area.getName(), k, 0);
				model.setValueAt(area.getSize(), k, 1);
				model.setValueAt(area.getStartIndex(), k, 2);
				model.setValueAt(statusString, k, 3);
			}
			node = node.next;
			k++;
		}
	}

	// ��ʼ����������
	public void initMyNode() {
		header = new MyNode(new StoreArea("Area1", 50, 0, SAMLLISTSIZE));
		// ���������ͽڵ�
		MyNode node2 = new MyNode(new StoreArea("Area2", 50, 50, SAMLLISTSIZE));
		MyNode node3 = new MyNode(new StoreArea("Area3", 30, 100, SAMLLISTSIZE));
		MyNode node4 = new MyNode(new StoreArea("Area4", 60, 130, SAMLLISTSIZE));
		MyNode node5 = new MyNode(new StoreArea("Area5", 20, 190, SAMLLISTSIZE));
		MyNode node6 = new MyNode(new StoreArea("Area6", 40, 210, SAMLLISTSIZE));
		// ���ýڵ�1��3��4Ϊ��ռ��
		header.getData().setStatus(Status.FULL);
		node3.getData().setStatus(Status.FULL);
		node4.getData().setStatus(Status.FULL);
		node6.getData().setStatus(Status.FULL);
		// ���ýڵ������
		header.insert(node2);
		node2.insert(node3);
		node3.insert(node4);
		node4.insert(node5);
		node5.insert(node6);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 653, 546);
		frame.setTitle("����ϵͳʵ��3���洢�����������ߣ����ڿ���");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 634, 112);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u63A7\u5236\u9762\u677F");
		label.setFont(new Font("����", Font.PLAIN, 18));
		label.setBounds(14, 13, 72, 18);
		panel.add(label);

		// �״���Ӧ��ť
		JRadioButton radioButton = new JRadioButton("\u9996\u6B21\u9002\u5E94\u7B97\u6CD5");
		radioButton.setSelected(true);
		radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = FF;
			}
		});
		radioButton.setBounds(121, 42, 119, 27);
		panel.add(radioButton);

		// ѭ���״���Ӧ�㷨
		JRadioButton radioButton_1 = new JRadioButton("\u5FAA\u73AF\u9996\u6B21\u9002\u5E94\u7B97\u6CD5");
		radioButton_1.setBounds(246, 42, 149, 27);
		radioButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = NF;
			}
		});
		panel.add(radioButton_1);

		// �����Ӧ�㷨��ť
		JRadioButton radioButton_2 = new JRadioButton("\u6700\u4F73\u9002\u5E94\u7B97\u6CD5");
		radioButton_2.setBounds(118, 75, 119, 27);
		radioButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = BF;
			}
		});
		panel.add(radioButton_2);

		// �ʵ���㷨��ť
		JRadioButton radioButton_3 = new JRadioButton("\u6700\u574F\u9002\u5E94\u7B97\u6CD5");
		radioButton_3.setBounds(246, 74, 119, 27);
		radioButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = WF;
			}
		});
		panel.add(radioButton_3);

		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(radioButton);
		btnGroup.add(radioButton_1);
		btnGroup.add(radioButton_2);
		btnGroup.add(radioButton_3);

		JLabel label_1 = new JLabel("\u52A8\u6001\u5206\u533A\u5206\u914D\u7B97\u6CD5\uFF1A");
		label_1.setBounds(118, 15, 154, 18);
		panel.add(label_1);

		JButton button_2 = new JButton("\u91CD\u7F6E\u7A0B\u5E8F");
		button_2.setBounds(439, 42, 113, 27);
		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				mode = FF;
				radioButton.setSelected(true);
				initMyNode();
				updateView();
			}
		});
		panel.add(button_2);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(0, 112, 238, 349);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel label_2 = new JLabel("\u8F93\u5165\u9762\u677F");
		label_2.setFont(new Font("����", Font.PLAIN, 18));
		label_2.setBounds(14, 13, 72, 18);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel("\u5206\u914D\u5185\u5B58");
		label_3.setBounds(68, 46, 73, 18);
		panel_1.add(label_3);

		textField = new JTextField();
		textField.setBounds(88, 73, 86, 24);
		panel_1.add(textField);
		textField.setColumns(10);

		JLabel label_4 = new JLabel("\u65B0\u5206\u533A\u540D\uFF1A");
		label_4.setBounds(14, 76, 76, 18);
		panel_1.add(label_4);

		JLabel label_5 = new JLabel("\u4F5C\u4E1A\u5927\u5C0F\uFF1A");
		label_5.setBounds(14, 113, 86, 18);
		panel_1.add(label_5);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(88, 110, 86, 24);
		panel_1.add(textField_1);

		// ���䰴ť
		JButton button = new JButton("\u5206\u914D");
		button.setBounds(72, 152, 86, 27);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String newName = textField.getText(); // ��ȡ�·�������
				if (newName.equals("") || textField_1.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "��������������Ϣ��");
				} else {
					int newSize;
					try {// ��ȡ�·�����С
						newSize = Integer.parseInt(textField_1.getText());
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame, "��������ȷ��ҵ��С����ҵ��СӦΪ���֣�");
						return;
					}
					// ����Ϊ��ȡ��������
					if (distribute(newName, newSize)) {
						// ˵������ɹ�
						updateView();
						JOptionPane.showMessageDialog(frame, "����ɹ���");
					} else {
						noteLabel.setText("�ڴ���з��������в����ҵ�����"+newName+"����ʹ�õĿ��з���");
						JOptionPane.showMessageDialog(frame, "û���ҵ����ʵĿ��з�����");
					}
				}
				textField.setText("");
				textField_1.setText("");
			}
		});
		panel_1.add(button);

		JLabel label_6 = new JLabel("\u56DE\u6536\u5206\u533A");
		label_6.setBounds(68, 204, 71, 18);
		panel_1.add(label_6);

		JLabel label_7 = new JLabel("\u5206\u533A\u540D\uFF1A");
		label_7.setBounds(14, 235, 60, 18);
		panel_1.add(label_7);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(88, 235, 86, 24);
		panel_1.add(textField_2);

		// ���հ�ť
		JButton button_1 = new JButton("\u56DE\u6536");
		button_1.setBounds(72, 272, 86, 27);
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String deleteName = textField_2.getText();
				if (recycle(deleteName)) {
					updateView();
				} else {
					JOptionPane.showMessageDialog(frame, "�������б��а�������Ϊ��ռ�á��ķ�������");
				}
				textField_2.setText("");
			}
		});
		panel_1.add(button_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(239, 112, 395, 349);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel label_8 = new JLabel("\u5185\u5B58\u5206\u914D\u60C5\u51B5\u9762\u677F");
		label_8.setFont(new Font("����", Font.PLAIN, 18));
		label_8.setBounds(14, 13, 154, 18);
		panel_2.add(label_8);

		// ��ͷ��������
		Object[] columnNames = { "������", "������С", "����ʼַ", "״̬" };
		Object[][] rowData = { { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" },
				{ "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" } };

		table = new JTable(new DefaultTableModel(rowData, columnNames));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));

		JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(14, 45, 381, 291);
		panel_2.add(scrollPane);
		
		noteLabel = new JLabel("");
		noteLabel.setForeground(Color.BLUE);
		noteLabel.setFont(new Font("����", Font.PLAIN, 18));
		noteLabel.setBounds(75, 468, 546, 31);
		frame.getContentPane().add(noteLabel);
		
		JLabel lblNewLabel = new JLabel("\u4FE1\u606F\uFF1A");
		lblNewLabel.setBounds(10, 470, 51, 18);
		frame.getContentPane().add(lblNewLabel);

		initMyNode();
		updateView();
	}
}
