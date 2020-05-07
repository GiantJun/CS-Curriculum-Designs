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
	// ��ͷ��������
	private String[] columnNames = { "��������", "������ɫ", "�۾���ɫ", "�׻���" };
	// �������������
	private String[][] rowData = { { "��", "��", "��", "no" }, { "��", "��", "��", "yes" }, { "��", "��", "��", "no" },
			{ "��", "��", "��", "yes" }, { "��", "��", "��", "no" }, { "��", "��", "��", "yes" }, { "��", "��", "��", "no" },
			{ "��", "��", "��", "no" } };

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
		// ��ʼ��������������
		for (int i = 0; i < rowData.length; i++) {
			rowList.add(i);
		}
		for (int i = 0; i < rowData[0].length - 1; i++) {
			columnList.add(i);
		}
		ArrayList<Point> pointList = new ArrayList<Point>(); // ���ڹ�����ȱ����Ķ���

		// �������ĸ��ڵ�
		// ���ú����ĵõ����ε����������С��Ϣ��������columnNames������
		int index = getMinColumn(rowList, columnList);
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(columnNames[index]);
		Point rootPoint = new Point(columnNames[index], index, rootNode, rowList, columnList);
		pointList.add(rootPoint);

		// ʹ�ø��ڵ㴴�������
		JTree tree = new JTree(rootNode);
		// ��������ʾ���ڵ���
		tree.setShowsRootHandles(true);
		// �������ڵ�ɱ༭
		tree.setEditable(true);
		// ���ýڵ�ѡ�м�����
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("��ǰ��ѡ�еĽڵ�: " + e.getPath());
			}
		});
		scrollPane_1.setViewportView(tree);

		// ��ʼ���ɾ�����
		while (pointList.size() > 0) {
			// ��ȡ�ڵ���Ϣ
			Point point = pointList.get(0);
			pointList.remove(0);
			ArrayList<Integer> row = point.getRowList();
			ArrayList<Integer> column = point.getColumnList();
			int currentColumn = point.getIndex();
			DefaultMutableTreeNode node = point.getNode();
			/* ��չ�ڵ� */
			// �������ݼ�
			MyList2ColumnList list = new MyList2ColumnList(); // ���ڴ洢�����Ե��б�
			// �������������У����²�ͬ�������Լ�������
			for (int i : row) {
				String childAttributeName = rowData[i][currentColumn];
				// ���list���Ƿ��Ѿ�������������
				if (list.isExist(childAttributeName)) {
					// �������������
					ColumnList temp = list.getByName(childAttributeName);
					temp.addIndex(i);
				} else {
					// �������򴴽�������������,������list
					ColumnList temp = new ColumnList(childAttributeName);
					temp.addIndex(i);
					list.add(temp);
				}
			}

			// �����µ��������б�
			ArrayList<Integer> newColumn = new ArrayList<Integer>();
			for (int columnj : column) {
				if (columnj != currentColumn) {
					newColumn.add(columnj);
				}
			}
			// ���������Ե��������������ݼ����������ݼ�����С��Ϣ�����Ե����������������pointList��
			for (ColumnList c : list.getList()) {
				// ΪJTree����ӽڵ�
				if (c.getList().size() == 1 || isConsistency(c.getList())) {
					// ˵���Ѿ����ֵ���С�ĵ�λ��Ӧ�õó�yes��no�Ľ��ֵ
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(columnNames[currentColumn]+"("+c.getName()+")");
					node.add(newNode);
					int leafRow = c.getList().get(0);
					DefaultMutableTreeNode leafNode = new DefaultMutableTreeNode(
							rowData[leafRow][rowData[0].length - 1]);
					newNode.add(leafNode);
				} else {
					// Ӧ������չ������pointList��
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(columnNames[currentColumn]+"("+c.getName()+")");
					node.add(newNode);
					int newIndex = getMinColumn(c.getList(), newColumn);
					pointList.add(new Point(c.getName(), newIndex, newNode, c.getList(), newColumn));
				}
			}
		}

	}
	
	//�жϵ�ǰ�������ж�Ӧ��yes��no�Ƿ�ȫ��һ��
	private boolean isConsistency(ArrayList<Integer> row){
		String init = rowData[row.get(0)][rowData[0].length-1];
		for(int i : row){
			if(!init.equals(rowData[i][rowData[0].length-1])){
				return false;
			}
		}
		return true;
	}
	
	// ���㵥�ε����������Ե���Ϣ�أ���������С������column������
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
		// ѡ�����Ϣ����С������
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
		System.out.println("ѡ������Ϊ��" + columnNames[column.get(result)]);
		System.out.println();
		return column.get(result);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ID3�㷨���ɾ������������ߣ����ڿ� 3117004568��");
		frame.setBounds(100, 100, 837, 516);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 471, 469);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u6D4B\u8BD5\u6570\u636E\u96C6\u5217\u8868\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 18));
		label.setBounds(14, 13, 136, 32);
		panel.add(label);

		// ����һ�����ָ�� ���������� �� ��ͷ
		JTable table = new JTable(new DefaultTableModel(rowData, columnNames));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
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
		label_1.setFont(new Font("����", Font.PLAIN, 18));
		label_1.setBounds(14, 13, 136, 32);
		panel_1.add(label_1);
	}
}
