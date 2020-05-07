package AILab3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;

public class ID3 {

	private JFrame frame;
	private JScrollPane scrollPane_1;
	// 表头（列名）
	private String[] columnNames = { "动物种类", "身体颜色", "眼睛颜色", "白化体" };
	// 表格所有行数据
	private String[][] rowData = { { "兔", "棕", "黑", "no" }, { "兔", "白", "红", "yes" }, { "兔", "灰", "红", "no" },
			{ "兔", "白", "红", "yes" }, { "象", "白", "黑", "no" }, { "象", "白", "红", "yes" }, { "象", "灰", "红", "no" },
			{ "象", "灰", "黑", "no" } };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ID3 window = new ID3();
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
	public ID3() {
		initialize();
	}

	private void createTree() {
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		ArrayList<Integer> columnList = new ArrayList<Integer>();
		// 初始化两个索引队列
		for (int i = 0; i < rowData.length; i++) {
			rowList.add(i);
		}
		for (int i = 0; i < rowData[0].length - 1; i++) {
			columnList.add(i);
		}
		ArrayList<Point> pointList = new ArrayList<Point>(); // 用于广度优先遍历的队列

		// 生成树的根节点
		// 调用函数的得到本次迭代计算的最小信息熵属性在columnNames的索引
		int index = getMinColumn(rowList, columnList);
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(columnNames[index]);
		Point rootPoint = new Point(columnNames[index], index, rootNode, rowList, columnList);
		pointList.add(rootPoint);

		// 使用根节点创建树组件
		JTree tree = new JTree(rootNode);
		// 设置树显示根节点句柄
		tree.setShowsRootHandles(true);
		// 设置树节点可编辑
		tree.setEditable(true);
		// 设置节点选中监听器
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("当前被选中的节点: " + e.getPath());
			}
		});
		scrollPane_1.setViewportView(tree);

		// 开始生成决策树
		while (pointList.size() > 0) {
			// 获取节点信息
			Point point = pointList.get(0);
			pointList.remove(0);
			ArrayList<Integer> row = point.getRowList();
			ArrayList<Integer> column = point.getColumnList();
			int currentColumn = point.getIndex();
			DefaultMutableTreeNode node = point.getNode();
			/* 扩展节点 */
			// 划分数据集
			MyList2ColumnList list = new MyList2ColumnList(); // 用于存储子属性的列表
			// 遍历所有数据行，记下不同的子属性及其索引
			for (int i : row) {
				String childAttributeName = rowData[i][currentColumn];
				// 检查list中是否已经包含该子属性
				if (list.isExist(childAttributeName)) {
					// 存在则记下索引
					ColumnList temp = list.getByName(childAttributeName);
					temp.addIndex(i);
				} else {
					// 不存在则创建，并记下索引,并加入list
					ColumnList temp = new ColumnList(childAttributeName);
					temp.addIndex(i);
					list.add(temp);
				}
			}

			// 生成新的列索引列表
			ArrayList<Integer> newColumn = new ArrayList<Integer>();
			for (int columnj : column) {
				if (columnj != currentColumn) {
					newColumn.add(columnj);
				}
			}
			// 根据子属性的索引划分子数据集，计算数据集的最小信息熵属性的索引，并将其加入pointList中
			for (ColumnList c : list.getList()) {
				// 为JTree添加子节点
				if (c.getList().size() == 1 || isConsistency(c.getList())) {
					// 说明已经划分到最小的单位，应该得出yes或no的结果值
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(columnNames[currentColumn]+"("+c.getName()+")");
					node.add(newNode);
					int leafRow = c.getList().get(0);
					DefaultMutableTreeNode leafNode = new DefaultMutableTreeNode(
							rowData[leafRow][rowData[0].length - 1]);
					newNode.add(leafNode);
				} else {
					// 应继续拓展，存入pointList中
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(columnNames[currentColumn]+"("+c.getName()+")");
					node.add(newNode);
					int newIndex = getMinColumn(c.getList(), newColumn);
					pointList.add(new Point(c.getName(), newIndex, newNode, c.getList(), newColumn));
				}
			}
		}

	}
	
	//判断当前子属性列对应的yes或no是否全部一致
	private boolean isConsistency(ArrayList<Integer> row){
		String init = rowData[row.get(0)][rowData[0].length-1];
		for(int i : row){
			if(!init.equals(rowData[i][rowData[0].length-1])){
				return false;
			}
		}
		return true;
	}
	
	// 计算单次迭代各个属性的信息熵，并返回最小属性在column的索引
	private int getMinColumn(ArrayList<Integer> row, ArrayList<Integer> column) {
		ArrayList<Column> columnList = new ArrayList<Column>();
		for (int i : column) {
			MyList list = new MyList();
			for (int j : row) {
				String temp = rowData[j][i];
				if (list.isExist(temp)) {
					if (rowData[j][rowData[0].length - 1].equals("yes")) {
						list.getByName(temp).increaseYesValue();
					} else {
						list.getByName(temp).increaseNoValue();
					}
				} else {
					Attribute a = new Attribute(temp);
					if (rowData[j][rowData[0].length - 1].equals("yes")) {
						a.increaseYesValue();
					} else {
						a.increaseNoValue();
					}
					list.add(a);
				}
			}
			columnList.add(new Column(columnNames[i], -list.sumEntropy() / rowData.length));
		}
		// 选择出信息熵最小的属性
		double min = 10000;
		int result = 0;
		for (int k=0; k < columnList.size(); k++) {
			Column a = columnList.get(k);
			if (a.getValue() < min) {
				min = a.getValue();
				result = k;
			}
			System.out.println(a.getName() + " : " + a.getValue());
		}
		System.out.println("选择属性为：" + columnNames[column.get(result)]);
		System.out.println();
		return column.get(result);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ID3算法生成决策树（制作者：黄钰竣 3117004568）");
		frame.setBounds(100, 100, 837, 516);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 471, 469);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u6D4B\u8BD5\u6570\u636E\u96C6\u5217\u8868\uFF1A");
		label.setFont(new Font("黑体", Font.PLAIN, 18));
		label.setBounds(14, 13, 136, 32);
		panel.add(label);

		// 创建一个表格，指定 所有行数据 和 表头
		JTable table = new JTable(new DefaultTableModel(rowData, columnNames));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("黑体", Font.PLAIN, 17));
		table.setPreferredScrollableViewportSize(new Dimension(400, 300));
		table.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(14, 58, 443, 398);
		panel.add(scrollPane);

		JButton button = new JButton("\u5F00\u59CB\u751F\u6210\u51B3\u7B56\u6811");
		button.setBounds(295, 13, 151, 32);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				createTree();
			}
		});
		panel.add(button);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(468, 0, 351, 469);
		panel_1.setLayout(null);
		frame.getContentPane().add(panel_1);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 50, 337, 419);
		panel_1.add(scrollPane_1);

		JLabel label_1 = new JLabel("\u751F\u6210\u7684\u51B3\u7B56\u6811\uFF1A");
		label_1.setFont(new Font("黑体", Font.PLAIN, 18));
		label_1.setBounds(14, 13, 136, 32);
		panel_1.add(label_1);
	}
}
