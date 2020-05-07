package databaseDesign;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;

public class DatabaseDesign {

	private JFrame frame;
	private DBManager dbManager;

	private static final String quaryEnrollment = "select * from enrollment"; // 查询录取计划
	private static final String quaryStudent = "select * from student"; // 查询学生表
	private static final String quaryEnrolledStudent = "select * from collagetaken"; // 查询录取学生信息
	private static final String quarySumary1 = "select * from totaltaken"; // 查询全校总体录取情况
	
	private static final String quarySpecailtyEnrollSumary = "select * from subjectviewdetail"; // 查询各专业录取情况统计信息
	private static final String quaryUnEnrolledStudent = "select * from returnstudentview"; // 查询被退档的学生信息
	private static final String quaryDirectEnrolledStudent = "select * from enrolldirectview"; // 查询未调剂直接录取的学生信息
	private static final String quaryDirectUnEnrolledStudent = "select * from returndirectview"; // 查询直接退档学生信息
	private static final String quaryNotDirectUnEnrolledStudent = "select * from returnbyrelieveview"; // 查询调剂时被退档的学生信息
	private static final String quaryNotDirectEnrolledStudent = "select * from enrollbyrelieveview"; // 查询调剂时被录取的学生信息
	private JTextField textField;
	private JTextField textField_1;
	
	private ArrayList<Vector> rsList = new ArrayList<Vector>();
	
	//留作输出的文件的查询结果集合
	private ResultSet enrollment;
	private ResultSet student;
	private ResultSet enrolledStudent;
	private ResultSet specailtyEnrollSumary;
	private ResultSet directEnrolledStudent;
	private ResultSet directUnEnrolledStudent;
	private ResultSet notDirectUnEnrolledStudent;
	private ResultSet notDirectEnrolledStudent;
	private ResultSet unEnrolledStudent;
	
	//作用不大的，只有一行数据
	private ResultSet Sumary1;
	
	//临时数据查询集合，为根据用户需要查询
	private ResultSet tempResult;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseDesign window = new DatabaseDesign();
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
	public DatabaseDesign() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		// 首先初始化数据库管理类
		dbManager = new DBManager();
		// 初始化界面
		frame = new JFrame();
		frame.setTitle("平行志愿按学校录取（SQL版）简易界面");
		frame.setResizable(false);
		frame.setBounds(100, 100, 942, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel bigPanel_1 = new JPanel();
		bigPanel_1.setBounds(0, 34, 936, 559);
		frame.getContentPane().add(bigPanel_1);
		bigPanel_1.setLayout(null);

		JPanel bigPanel_2 = new JPanel();
		bigPanel_2.setBounds(0, 34, 936, 559);
		bigPanel_2.setLayout(null);
		bigPanel_2.setVisible(true);	//应为false
		bigPanel_2.setEnabled(true);	//应为false
		frame.getContentPane().add(bigPanel_2);
		
		// 需要选择的条目
		String[] listData = new String[] { "软件工程","网络工程","信息安全","材料类","材料成型及控制工程",
						"能源与动力工程","微电子科学与工程","环境科学与工程类","安全工程","外国语言文学类",
						"日语","电子科学与技术","光电信息科学与工程","制药工程","生物工程","工商管理类",
						"会计学","电子商务","信息管理与信息系统","房地产开发与管理","土地资源管理","数学类",
						"应用统计学","法学","社会工作","金融学类","经济学","国际经济与贸易","工业设计",
						"数字媒体技术","建筑学","城乡规划","风景园林"};

		JComboBox comboBox = new JComboBox(listData);
		comboBox.setBounds(333, 33, 180, 24);
		bigPanel_2.add(comboBox);
		
		JPanel searchPanel_1 = new JPanel();
		searchPanel_1.setBounds(14, 63, 908, 47);
		bigPanel_2.add(searchPanel_1);
		searchPanel_1.setLayout(new GridLayout(1,1));
		
		JButton searchButton_1 = new JButton("\u67E5\u8BE2");
		searchButton_1.setBounds(527, 32, 113, 27);
		searchButton_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String spacieltyName = (String)comboBox.getSelectedItem();
				String sqlString = "{call searchsubjectbyname("+spacieltyName+")}" ;
				String[] searchTitle = new String[]{"最高排位","最高分","最低排位","最低分","平均分","专业编号"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"随便");
					searchPanel_1.removeAll();
					searchPanel_1.add(scrollpane);
					searchPanel_1.validate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("查询失败");
					e.printStackTrace();
				}
			}
		});
		bigPanel_2.add(searchButton_1);
		
		JLabel label = new JLabel("\u6309\u4E13\u4E1A\u540D\u67E5\u8BE2\u8BE5\u4E13\u4E1A\u5F55\u53D6\u60C5\u51B5\uFF08\u6309\u4E13\u4E1A\u53F7\u67E5\u8BE2\u56E0\u4E0E\u8FD9\u4E2A\u76F8\u4F3C\uFF0C\u5C31\u4E0D\u5C55\u793A\u5982\u4F55\u8C03\u7528\u4E86\uFF09");
		label.setFont(new Font("黑体", Font.PLAIN, 20));
		label.setBounds(14, 0, 778, 27);
		bigPanel_2.add(label);
		
		JLabel specialtyName = new JLabel("\u4E13\u4E1A\u540D");
		specialtyName.setFont(new Font("黑体", Font.PLAIN, 15));
		specialtyName.setBounds(261, 32, 58, 27);
		bigPanel_2.add(specialtyName);
		
		JPanel searchPanel_2 = new JPanel();
		searchPanel_2.setBounds(14, 173, 908, 123);
		bigPanel_2.add(searchPanel_2);
		searchPanel_2.setLayout(new GridLayout(1, 1));
		
		JLabel specialtyName_1 = new JLabel("\u4E13\u4E1A\u540D");
		specialtyName_1.setFont(new Font("黑体", Font.PLAIN, 15));
		specialtyName_1.setBounds(261, 141, 58, 27);
		bigPanel_2.add(specialtyName_1);
		
		JComboBox comboBox_1 = new JComboBox(listData);
		comboBox_1.setBounds(333, 142, 180, 24);
		bigPanel_2.add(comboBox_1);
		
		JButton searchButton_2 = new JButton("\u67E5\u8BE2");
		searchButton_2.setBounds(527, 141, 113, 27);
		searchButton_2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String name2 = (String)comboBox_1.getSelectedItem();
				String sqlString = "{call searchsubject("+name2+")}" ;
				String[] searchTitle = new String[]{"学生名","分数","排位","生源地","科目类型","录取专业号","录取专业代码","录取专业名","年限"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"随便");
					searchPanel_2.removeAll();
					searchPanel_2.add(scrollpane);
					searchPanel_2.validate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("查询失败");
					e.printStackTrace();
				}
			}
		});
		bigPanel_2.add(searchButton_2);
		
		JLabel label_2 = new JLabel("\u67E5\u8BE2\u5404\u4E13\u4E1A\u5F55\u53D6\u5B66\u751F\u4FE1\u606F");
		label_2.setFont(new Font("黑体", Font.PLAIN, 20));
		label_2.setBounds(14, 109, 778, 27);
		bigPanel_2.add(label_2);
		
		JLabel label_1 = new JLabel("\u67E5\u8BE2\u5168\u7701\u6392\u540D\u524D\u767E\u5206\u4E4B\u591A\u5C11\u7684\u5B66\u751F\u4EBA\u6570");
		label_1.setFont(new Font("黑体", Font.PLAIN, 20));
		label_1.setBounds(14, 295, 778, 27);
		bigPanel_2.add(label_1);
		
		textField = new JTextField();
		textField.setBounds(157, 320, 86, 24);
		bigPanel_2.add(textField);
		textField.setColumns(10);
		
		JLabel label_3 = new JLabel("\u8F93\u5165\u767E\u5206\u6BD4\u6570\u5B57");
		label_3.setFont(new Font("黑体", Font.PLAIN, 15));
		label_3.setBounds(44, 319, 113, 27);
		bigPanel_2.add(label_3);
		
		JLabel label_5 = new JLabel("");
		label_5.setFont(new Font("黑体", Font.PLAIN, 15));
		label_5.setBounds(493, 320, 90, 27);
		bigPanel_2.add(label_5);
		
		JButton button_2 = new JButton("\u67E5\u8BE2");
		button_2.setBounds(261, 319, 113, 27);
		button_2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int percent=100;
				try{
					percent = Integer.parseInt(textField.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(frame, "请输入正确的百分数（仅数字无百分号）！");
					return;
				}
				ResultSet result = dbManager.query("{call percentstudentnum("+percent+")}");
				try{
					result.next();
				label_5.setText(result.getInt(1)+"");
				label_5.validate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("查询失败");
					e1.printStackTrace();
				}
			}
		});
		bigPanel_2.add(button_2);
		
		JLabel label_4 = new JLabel("\u7ED3\u679C\u4EBA\u6570\u4E3A\uFF1A");
		label_4.setFont(new Font("黑体", Font.PLAIN, 15));
		label_4.setBounds(402, 320, 90, 27);
		bigPanel_2.add(label_4);
		
		JLabel label_6 = new JLabel("\u67E5\u8BE2\u5168\u7701\u6392\u540D\u524D\u767E\u5206\u4E4B\u591A\u5C11\u7684\u5B66\u751F\u540D\u5355");
		label_6.setFont(new Font("黑体", Font.PLAIN, 20));
		label_6.setBounds(14, 345, 778, 27);
		bigPanel_2.add(label_6);
		
		JLabel label_7 = new JLabel("\u8F93\u5165\u767E\u5206\u6BD4\u6570\u5B57");
		label_7.setFont(new Font("黑体", Font.PLAIN, 15));
		label_7.setBounds(44, 369, 113, 27);
		bigPanel_2.add(label_7);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(157, 370, 86, 24);
		bigPanel_2.add(textField_1);
		
		JPanel searchPanel_3 = new JPanel();
		searchPanel_3.setBounds(14, 400, 908, 146);
		bigPanel_2.add(searchPanel_3);
		searchPanel_3.setLayout(new GridLayout(1, 1));
		
		JButton button_3 = new JButton("\u67E5\u8BE2");
		button_3.setBounds(261, 369, 113, 27);
		button_3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int percent=100;
				try{
					percent = Integer.parseInt(textField_1.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(frame, "请输入正确的百分数（仅数字无百分号）！");
					return;
				}
				String sqlString = "{call percentstudentsubject("+percent+")}" ;
				String[] searchTitle = new String[]{"学生名","分数","排位","生源地","科目类型","专业号","专业代码","专业名","学制"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"随便");
					searchPanel_3.removeAll();
					searchPanel_3.add(scrollpane);
					searchPanel_3.validate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("查询失败");
					e1.printStackTrace();
				}
			}
		});
		bigPanel_2.add(button_3);
		
		JButton button = new JButton("\u57FA\u672C\u8868\u53CA\u89C6\u56FE\u60C5\u51B5");
		button.setFont(new Font("黑体", Font.PLAIN, 20));
		button.setBounds(0, -2, 198, 29);
		button.setEnabled(false);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("\u67E5\u8BE2\u5F55\u53D6\u60C5\u51B5");
		button_1.setFont(new Font("黑体", Font.PLAIN, 20));
		button_1.setBounds(196, -2, 177, 29);
		frame.getContentPane().add(button_1);
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				bigPanel_1.setVisible(true);
				bigPanel_1.setEnabled(true);
				bigPanel_2.setVisible(false);
				bigPanel_2.setEnabled(false);
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		
		button_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				bigPanel_1.setVisible(false);
				bigPanel_1.setEnabled(false);
				bigPanel_2.setVisible(true);
				bigPanel_2.setEnabled(true);
				button.setEnabled(true);
				button_1.setEnabled(false);
			}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(14, 34, 908, 413);
		bigPanel_1.add(tabbedPane);

		JLabel label1 = new JLabel("\u57FA\u672C\u8868\u53CA\u89C6\u56FE\u4FE1\u606F\uFF1A");
		label1.setFont(new Font("宋体", Font.PLAIN, 18));
		label1.setBounds(14, 0, 201, 29);
		bigPanel_1.add(label1);

		JLabel label_11 = new JLabel("\u5F55\u53D6\u7684\u7EDF\u8BA1\u4FE1\u606F\uFF1A");
		label_11.setFont(new Font("宋体", Font.PLAIN, 18));
		label_11.setBounds(14, 445, 201, 29);
		bigPanel_1.add(label_11);
		
		JButton button_4 = new JButton("\u5BFC\u51FA\u5F53\u524D\u663E\u793A\u8868\u683C");
		button_4.setBounds(760, 3, 162, 27);
		button_4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String savePath = showFileSaveDialog();
				int index = tabbedPane.getSelectedIndex();
				Vector<Object> resultVector  = rsList.get(index);
				System.out.println("选中的页面序号是："+index);
				System.out.println("保存路径为："+savePath);
				if(savePath!=null){
					try {
						saveExcelFile(resultVector,savePath);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("文件写入异常！");
						e.printStackTrace();
					}
				}
			}
		});
		bigPanel_1.add(button_4);

		/************************** 第一部分的“基本表” **************************************/
		/********** 基本表的第一块页面（录取计划） ********/
		// 定义表头
		String[] enrollTitle = new String[] { "专业号", "专业代码", "专业名", "科目类型", "年限", "录取人数" };
		// 创建第 1 个选项卡（选项卡只包含 标题）
		try {
			tabbedPane.addTab("录取计划信息", createQuaryPanel(quaryEnrollment, enrollTitle,enrollment,"录取计划信息"));
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e2.printStackTrace();
		}

		/********** 基本表的第二块页面（学生信息） ********/
		// 定义表头
		String[] studentTitle = new String[] { "学生名", "分数", "志愿1", "志愿2", "志愿3", "志愿4", "志愿5", "志愿6", "是否服从调剂", "全省排位",
				"生源地", "科目类型" };
		// 创建第 1 个选项卡（选项卡只包含 标题）
		try {
			tabbedPane.addTab("考生志愿信息", createQuaryPanel(quaryStudent, studentTitle,student,"考生志愿信息"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}

		/********** 基本表的第三个页面（录取结果） ********/
		String[] enrolledStudentTitle = new String[] { "学生名", "分数", "全省排位", "生源地", "科目类型", "专业号", "专业代码", "专业名称",
				"学制" };
		try {
			tabbedPane.addTab("被录取考生所有信息", createQuaryPanel(quaryEnrolledStudent, enrolledStudentTitle,enrolledStudent,"被录取考生所有信息"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第四个页面（各专业录取情况统计信息） ********/
		String[] specialtySumaryTitle = new String[] { "专业编号", "专业代码", "专业名称", "最高排位", "最高分", "最低排位", "最低分", "平均分" };
		try {
			tabbedPane.addTab("各专业录取情况统计", createQuaryPanel(quarySpecailtyEnrollSumary, specialtySumaryTitle,specailtyEnrollSumary,"各专业录取情况统计"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第五个页面（被退档学生信息） ********/
		try {
			tabbedPane.addTab("被退档学生", createQuaryPanel(quaryUnEnrolledStudent, studentTitle,unEnrolledStudent,"被退档学生"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第六个页面（未调剂直接录取学生信息） ********/
		try {
			tabbedPane.addTab("未调剂直接录取学生", createQuaryPanel(quaryDirectEnrolledStudent, studentTitle,directEnrolledStudent,"未调剂直接录取学生"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第七个页面（直接退档学生信息） ********/
		try {
			tabbedPane.addTab("直接退档学生", createQuaryPanel(quaryDirectUnEnrolledStudent, studentTitle,directUnEnrolledStudent,"直接退档学生"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第八个页面（调剂时退挡学生信息） ********/
		try {
			tabbedPane.addTab("调剂时退挡学生", createQuaryPanel(quaryNotDirectUnEnrolledStudent, studentTitle,notDirectUnEnrolledStudent,"调剂时退挡学生"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}
		/********** 基本表的第九个页面（调剂时被录取学生信息） ********/
		try {
			tabbedPane.addTab("调剂时被录取学生", createQuaryPanel(quaryNotDirectEnrolledStudent, studentTitle,notDirectEnrolledStudent,"调剂时被录取学生"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e1.printStackTrace();
		}

		/************************** 第一面的“录取统计信息” **************************************/
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(14, 473, 908, 73);
		bigPanel_1.add(tabbedPane_1);
		/************** 录取统计信息的第一个页面(总体录取情况) **************/
		String[] sumTitle1 = new String[] { "最高排位", "最高分", "最低排位", "最低分", "平均分" };
		// 创建第 3 个选项卡（选项卡只包含 标题）
		try {
			tabbedPane_1.addTab("被录取考生所有信息", createQuaryPanel(quarySumary1, sumTitle1,Sumary1,"被录取考生所有信息"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("查询失败");
			e.printStackTrace();
		}
	}
	
	private JScrollPane createQuaryPanel(String sqlString, String[] title,ResultSet set,String fileName) throws SQLException {
		// 将title转换为vector类型
		Vector<String> titleVector = new Vector<String>();
		for (String temp : title) {
			titleVector.add(temp);
		}
		// 获取查询结果vector
		Vector<Object> vector = new Vector<Object>();
		ResultSet rs = dbManager.query(sqlString);
		set = rs;
		Vector<Object> storeVector = new Vector<Object>();
		storeVector.add(title);
		storeVector.add(rs);
		storeVector.add(fileName);
		rsList.add(storeVector);
		int columnSize = rs.getMetaData().getColumnCount();
		// 为表格输入数据
		while (rs.next()) {
			Vector<String> row = new Vector<String>();
			for (int i = 1; i <= columnSize; i++) {
				row.add(rs.getString(i));
			}
			vector.add(row);
		}
		// 创建显示表格
		JTable table = new JTable(new DefaultTableModel(vector, titleVector));
		table.setRowHeight(25);
		// 创建表格，加入到滑动面板中
		JScrollPane scrollPane = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setViewportView(table);
		return scrollPane;
	}
	
	private String showFileSaveDialog() {
		String result = null;
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // 设置打开文件选择框后默认输入的文件名
        //fileChooser.setSelectedFile();

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int code = fileChooser.showSaveDialog(frame);

        if (code == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"保存", 则获取选择的保存路径
            File file = fileChooser.getSelectedFile();
            result = file.getAbsolutePath();
        }
        return result;
    }
	
	private void saveExcelFile(Vector vector,String path) throws SQLException{
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		
		//获取标题数据和数据集合
		String[] title = (String[]) vector.get(0);
		ResultSet saveSet = (ResultSet) vector.get(1);
		String fileName = (String) vector.get(2);
		for(int i=0; i<title.length; i++){
			cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		//开始存入数据
		int j=0;
		saveSet.beforeFirst();
		while(saveSet.next()){
			row = sheet.createRow((int) j + 1);
			for(int i=1; i<=title.length;i++){
				row.createCell((short) i-1).setCellValue((String)saveSet.getString(i));
			}
			j++;
		}
		//开始写文件
		// 第六步，将文件存到指定位置
		try
		{
			FileOutputStream fout = new FileOutputStream(path+"/"+fileName+".xls");
			wb.write(fout);
			fout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
