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

	private final int SAMLLISTSIZE = 5; // 允许划分的最小分区
	private final int FF = 1000; // 首次适应算法标识
	private final int NF = 2000; // 循环首次适应算法标识
	private final int BF = 3000; // 最佳适应算法标识
	private final int WF = 4000; // 最坏适应算法标识

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTable table;
	private JLabel noteLabel;	//显示分配结果信息

	private MyNode header; // 头指针
	private int mode = FF; // 标识所选的分配算法
	private MyNode NFNode = null; // 循环首次适应专属指针

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

	// 回收指定名字的作业
	public boolean recycle(String name) {
		// 先定位到指定的节点
		MyNode node = header;
		boolean isRecycled=false;
		while (node != null) {
			if (node.getData().getName().equals(name)) {
				break;
			}
			node = node.next;
		}
		// 若无法找到，则说明不存在该节点
		if (node == null) {
			noteLabel.setText("空闲分区链表中不存在"+name+"节点");
			return false;
		}
		// 若找到的节点为空闲分区，则返回false
		if (node.getData().getStatus().equals(Status.EMPTY)) {
			noteLabel.setText(name+"不是一个占用分区，不需要回收");
			return false;
		}
		// 已找到指定名字的节点，先记录下该节点的父节点和子节点
		MyNode myParent = node.parent;
		MyNode myNext = node.next;
		String message = "回收的分区有："+name;
		// 判断前面的节点是否可以合并
		if(myParent!=null){
			if (myParent.getData().getStatus().equals(Status.EMPTY)) {
				// 说明前一个分区可以合并，则合并
				StoreArea area = node.deleteNode(); // 删除了该节点
				myParent.getData().merge(area);
				node = myParent; // 方便后面合并下一个分区
				message = message +"、"+myParent.getData().getName();
				isRecycled=true;
			}
		}
		if(myNext!=null){
			if (myNext.getData().getStatus().equals(Status.EMPTY)) {
				// 说明后一个分区可以合并
				StoreArea area1 = myNext.deleteNode(); // 删除了子节点
				node.getData().merge(area1);
				node.getData().setStatus(Status.EMPTY);
				message = message + "、"+area1.getName();
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
			// 使用首次适应算法
			while (node != null) {
				if (node.getData().getStatus().equals(Status.EMPTY)) {
					// 若分区未被占用，则尝试分区
					StoreArea area = node.getData().splitArea(newName, size);
					if (area != null) {
						// 说明该分区可再细分，分区成功
						myParent = node.parent;
						if (myParent == null) {
							// 说明为头节点
							MyNode newNode = new MyNode(area);
							newNode.insert(node);
							header = newNode;
							noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
							return true;
						} else {
							// 说明不是头节点
							MyNode newNode = new MyNode(area);
							myParent.insert(newNode);
							noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
							return true;
						}
					}
				}
				node = node.next;
			}
		} else if (mode == NF) {
			// 使用最佳适应算法
			if (NFNode == null) {
				NFNode = header;
			}
			// 记下当前指针指向的对象，防止在陷入死循环
			MyNode label = NFNode;
			// 其余部分代码与首次适应算法相似,只是将node改为NFNode
			while (true) {
				if (NFNode.getData().getStatus().equals(Status.EMPTY)) {
					// 若分区未被占用，则尝试分区
					StoreArea area = NFNode.getData().splitArea(newName, size);
					if (area != null) {
						// 说明该分区可再细分，分区成功
						myParent = NFNode.parent;
						if (myParent == null) {
							// 说明为头节点
							MyNode newNode = new MyNode(area);
							newNode.insert(NFNode);
							header = newNode;
							noteLabel.setText(newName+" 成功从 " +NFNode.getData().getName()+" 中分得了大小为"+size+"的内存空间");
							// 按照算法思想，将指针指向下一个空闲区
							NFNode = NFNode.next;
							return true;
						} else {
							// 说明不是头节点
							MyNode newNode = new MyNode(area);
							myParent.insert(newNode);
							noteLabel.setText(newName+" 成功从 " +NFNode.getData().getName()+" 中分得了大小为"+size+"的内存空间");
							// 按照算法思想，将指针指向下一个空闲区
							NFNode = NFNode.next;
							return true;
						}
					}
				}
				NFNode = NFNode.next;
				if (NFNode == null) {
					// 说明遍历到链表尾
					NFNode = header;
				}
				if (NFNode == label) {
					// 说明已经将链表遍历了一次，仍找不到合适分区
					noteLabel.setText(" 空闲分区链表中无合适的分区可供"+newName+"分配使用");
					return false;
				}
			}
		} else if (mode == BF) {
			// 采用最佳适应算法
			// 首先遍历记下满足要求，且又是最小的空闲分区
			MyNode temp = header;
			while (temp != null) {
				// 判断该节点是否满足要求，即大小合适且为空闲块
				if (temp.getData().isFit(size)) {
					if (!temp.getData().isBiger(node.getData())) {
						// 比已记录节点大，则记下当前节点
						node = temp;
					}
				}
				temp = temp.next;
			}
			// 再次确保最小块不被占用后，开始分配
			if (node.getData().getStatus().equals(Status.EMPTY)) {
				StoreArea area = node.getData().splitArea(newName, size);
				if (area != null) {
					// 说明该分区可再细分，分区成功
					myParent = node.parent;
					if (myParent == null) {
						// 说明为头节点
						MyNode newNode = new MyNode(area);
						newNode.insert(node);
						header = newNode;
						noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
						return true;
					} else {
						// 说明不是头节点
						MyNode newNode = new MyNode(area);
						myParent.insert(newNode);
						noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
						return true;
					}
				}
			}

		} else if (mode == WF) {
			// 采用最坏适应算法，代码与前面相似，只是判断条件不同
			// 首先遍历记下满足要求，且又是最大的空闲分区
			MyNode temp = header;
			while (temp != null) {
				// 判断该节点是否满足要求，即大小合适且为空闲块
				if (temp.getData().isFit(size) && temp.getData().getStatus().equals(Status.EMPTY)) {
					//若第一个节点node不为空闲分区，则找到第一个空闲且合适的分区，赋值给node
					if(node.getData().getStatus().equals(Status.FULL)){
						node=temp;
						continue;
					}
					if (temp.getData().isBiger(node.getData())) {
						// 比已记录节点大，则记下当前节点
						node = temp;
					}
				}
				temp = temp.next;
			}
			// 再次确保最小块不被占用后，开始分配
			if (node.getData().getStatus().equals(Status.EMPTY)) {
				StoreArea area = node.getData().splitArea(newName, size);
				if (area != null) {
					// 说明该分区可再细分，分区成功
					myParent = node.parent;
					if (myParent == null) {
						// 说明为头节点
						MyNode newNode = new MyNode(area);
						newNode.insert(node);
						header = newNode;
						noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
						return true;
					} else {
						// 说明不是头节点
						MyNode newNode = new MyNode(area);
						myParent.insert(newNode);
						noteLabel.setText(newName+" 成功从 " +node.getData().getName()+" 中分得了大小为"+size+"的内存空间");
						return true;
					}
				}
			}
		}

		return false;
	}

	// 更新界面
	public void updateView() {
		MyNode node = header;
		int k = 0;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		StoreArea area = null;
		// 清除所有行
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < 4; j++) {
				table.setValueAt("", i, j);
			}
		}
		while (node != null) {
			area = node.getData();
			String statusString;
			if (area.getStatus().equals(Status.EMPTY)) {
				statusString = "空闲";
			} else {
				statusString = "占用";
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

	// 初始化分区链表
	public void initMyNode() {
		header = new MyNode(new StoreArea("Area1", 50, 0, SAMLLISTSIZE));
		// 创建分区和节点
		MyNode node2 = new MyNode(new StoreArea("Area2", 50, 50, SAMLLISTSIZE));
		MyNode node3 = new MyNode(new StoreArea("Area3", 30, 100, SAMLLISTSIZE));
		MyNode node4 = new MyNode(new StoreArea("Area4", 60, 130, SAMLLISTSIZE));
		MyNode node5 = new MyNode(new StoreArea("Area5", 20, 190, SAMLLISTSIZE));
		MyNode node6 = new MyNode(new StoreArea("Area6", 40, 210, SAMLLISTSIZE));
		// 设置节点1、3、4为被占用
		header.getData().setStatus(Status.FULL);
		node3.getData().setStatus(Status.FULL);
		node4.getData().setStatus(Status.FULL);
		node6.getData().setStatus(Status.FULL);
		// 设置节点的连接
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
		frame.setTitle("操作系统实验3（存储管理）（制作者：黄钰竣）");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 634, 112);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u63A7\u5236\u9762\u677F");
		label.setFont(new Font("黑体", Font.PLAIN, 18));
		label.setBounds(14, 13, 72, 18);
		panel.add(label);

		// 首次适应按钮
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

		// 循环首次适应算法
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

		// 最佳适应算法按钮
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

		// 最坏实行算法按钮
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
		label_2.setFont(new Font("黑体", Font.PLAIN, 18));
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

		// 分配按钮
		JButton button = new JButton("\u5206\u914D");
		button.setBounds(72, 152, 86, 27);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String newName = textField.getText(); // 获取新分区名字
				if (newName.equals("") || textField_1.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "请输入完整的信息！");
				} else {
					int newSize;
					try {// 获取新分区大小
						newSize = Integer.parseInt(textField_1.getText());
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame, "请输入正确作业大小，作业大小应为数字！");
						return;
					}
					// 以下为获取数字正常
					if (distribute(newName, newSize)) {
						// 说明分配成功
						updateView();
						JOptionPane.showMessageDialog(frame, "分配成功！");
					} else {
						noteLabel.setText("内存空闲分区链表中不能找到合适"+newName+"分配使用的空闲分区");
						JOptionPane.showMessageDialog(frame, "没有找到合适的空闲分区！");
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

		// 回收按钮
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
					JOptionPane.showMessageDialog(frame, "请输入列表中包含，且为“占用”的分区名！");
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
		label_8.setFont(new Font("黑体", Font.PLAIN, 18));
		label_8.setBounds(14, 13, 154, 18);
		panel_2.add(label_8);

		// 表头（列名）
		Object[] columnNames = { "分区名", "分区大小", "分区始址", "状态" };
		Object[][] rowData = { { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" },
				{ "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" } };

		table = new JTable(new DefaultTableModel(rowData, columnNames));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("黑体", Font.PLAIN, 17));

		JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(14, 45, 381, 291);
		panel_2.add(scrollPane);
		
		noteLabel = new JLabel("");
		noteLabel.setForeground(Color.BLUE);
		noteLabel.setFont(new Font("黑体", Font.PLAIN, 18));
		noteLabel.setBounds(75, 468, 546, 31);
		frame.getContentPane().add(noteLabel);
		
		JLabel lblNewLabel = new JLabel("\u4FE1\u606F\uFF1A");
		lblNewLabel.setBounds(10, 470, 51, 18);
		frame.getContentPane().add(lblNewLabel);

		initMyNode();
		updateView();
	}
}
