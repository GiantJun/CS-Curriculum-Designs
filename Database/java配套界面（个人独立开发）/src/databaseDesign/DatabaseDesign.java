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

	private static final String quaryEnrollment = "select * from enrollment"; // ��ѯ¼ȡ�ƻ�
	private static final String quaryStudent = "select * from student"; // ��ѯѧ����
	private static final String quaryEnrolledStudent = "select * from collagetaken"; // ��ѯ¼ȡѧ����Ϣ
	private static final String quarySumary1 = "select * from totaltaken"; // ��ѯȫУ����¼ȡ���
	
	private static final String quarySpecailtyEnrollSumary = "select * from subjectviewdetail"; // ��ѯ��רҵ¼ȡ���ͳ����Ϣ
	private static final String quaryUnEnrolledStudent = "select * from returnstudentview"; // ��ѯ���˵���ѧ����Ϣ
	private static final String quaryDirectEnrolledStudent = "select * from enrolldirectview"; // ��ѯδ����ֱ��¼ȡ��ѧ����Ϣ
	private static final String quaryDirectUnEnrolledStudent = "select * from returndirectview"; // ��ѯֱ���˵�ѧ����Ϣ
	private static final String quaryNotDirectUnEnrolledStudent = "select * from returnbyrelieveview"; // ��ѯ����ʱ���˵���ѧ����Ϣ
	private static final String quaryNotDirectEnrolledStudent = "select * from enrollbyrelieveview"; // ��ѯ����ʱ��¼ȡ��ѧ����Ϣ
	private JTextField textField;
	private JTextField textField_1;
	
	private ArrayList<Vector> rsList = new ArrayList<Vector>();
	
	//����������ļ��Ĳ�ѯ�������
	private ResultSet enrollment;
	private ResultSet student;
	private ResultSet enrolledStudent;
	private ResultSet specailtyEnrollSumary;
	private ResultSet directEnrolledStudent;
	private ResultSet directUnEnrolledStudent;
	private ResultSet notDirectUnEnrolledStudent;
	private ResultSet notDirectEnrolledStudent;
	private ResultSet unEnrolledStudent;
	
	//���ò���ģ�ֻ��һ������
	private ResultSet Sumary1;
	
	//��ʱ���ݲ�ѯ���ϣ�Ϊ�����û���Ҫ��ѯ
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
		// ���ȳ�ʼ�����ݿ������
		dbManager = new DBManager();
		// ��ʼ������
		frame = new JFrame();
		frame.setTitle("ƽ��־Ը��ѧУ¼ȡ��SQL�棩���׽���");
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
		bigPanel_2.setVisible(true);	//ӦΪfalse
		bigPanel_2.setEnabled(true);	//ӦΪfalse
		frame.getContentPane().add(bigPanel_2);
		
		// ��Ҫѡ�����Ŀ
		String[] listData = new String[] { "�������","���繤��","��Ϣ��ȫ","������","���ϳ��ͼ����ƹ���",
						"��Դ�붯������","΢���ӿ�ѧ�빤��","������ѧ�빤����","��ȫ����","���������ѧ��",
						"����","���ӿ�ѧ�뼼��","�����Ϣ��ѧ�빤��","��ҩ����","���﹤��","���̹�����",
						"���ѧ","��������","��Ϣ��������Ϣϵͳ","���ز����������","������Դ����","��ѧ��",
						"Ӧ��ͳ��ѧ","��ѧ","��Ṥ��","����ѧ��","����ѧ","���ʾ�����ó��","��ҵ���",
						"����ý�弼��","����ѧ","����滮","�羰԰��"};

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
				String[] searchTitle = new String[]{"�����λ","��߷�","�����λ","��ͷ�","ƽ����","רҵ���"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"���");
					searchPanel_1.removeAll();
					searchPanel_1.add(scrollpane);
					searchPanel_1.validate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("��ѯʧ��");
					e.printStackTrace();
				}
			}
		});
		bigPanel_2.add(searchButton_1);
		
		JLabel label = new JLabel("\u6309\u4E13\u4E1A\u540D\u67E5\u8BE2\u8BE5\u4E13\u4E1A\u5F55\u53D6\u60C5\u51B5\uFF08\u6309\u4E13\u4E1A\u53F7\u67E5\u8BE2\u56E0\u4E0E\u8FD9\u4E2A\u76F8\u4F3C\uFF0C\u5C31\u4E0D\u5C55\u793A\u5982\u4F55\u8C03\u7528\u4E86\uFF09");
		label.setFont(new Font("����", Font.PLAIN, 20));
		label.setBounds(14, 0, 778, 27);
		bigPanel_2.add(label);
		
		JLabel specialtyName = new JLabel("\u4E13\u4E1A\u540D");
		specialtyName.setFont(new Font("����", Font.PLAIN, 15));
		specialtyName.setBounds(261, 32, 58, 27);
		bigPanel_2.add(specialtyName);
		
		JPanel searchPanel_2 = new JPanel();
		searchPanel_2.setBounds(14, 173, 908, 123);
		bigPanel_2.add(searchPanel_2);
		searchPanel_2.setLayout(new GridLayout(1, 1));
		
		JLabel specialtyName_1 = new JLabel("\u4E13\u4E1A\u540D");
		specialtyName_1.setFont(new Font("����", Font.PLAIN, 15));
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
				String[] searchTitle = new String[]{"ѧ����","����","��λ","��Դ��","��Ŀ����","¼ȡרҵ��","¼ȡרҵ����","¼ȡרҵ��","����"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"���");
					searchPanel_2.removeAll();
					searchPanel_2.add(scrollpane);
					searchPanel_2.validate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("��ѯʧ��");
					e.printStackTrace();
				}
			}
		});
		bigPanel_2.add(searchButton_2);
		
		JLabel label_2 = new JLabel("\u67E5\u8BE2\u5404\u4E13\u4E1A\u5F55\u53D6\u5B66\u751F\u4FE1\u606F");
		label_2.setFont(new Font("����", Font.PLAIN, 20));
		label_2.setBounds(14, 109, 778, 27);
		bigPanel_2.add(label_2);
		
		JLabel label_1 = new JLabel("\u67E5\u8BE2\u5168\u7701\u6392\u540D\u524D\u767E\u5206\u4E4B\u591A\u5C11\u7684\u5B66\u751F\u4EBA\u6570");
		label_1.setFont(new Font("����", Font.PLAIN, 20));
		label_1.setBounds(14, 295, 778, 27);
		bigPanel_2.add(label_1);
		
		textField = new JTextField();
		textField.setBounds(157, 320, 86, 24);
		bigPanel_2.add(textField);
		textField.setColumns(10);
		
		JLabel label_3 = new JLabel("\u8F93\u5165\u767E\u5206\u6BD4\u6570\u5B57");
		label_3.setFont(new Font("����", Font.PLAIN, 15));
		label_3.setBounds(44, 319, 113, 27);
		bigPanel_2.add(label_3);
		
		JLabel label_5 = new JLabel("");
		label_5.setFont(new Font("����", Font.PLAIN, 15));
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
					JOptionPane.showMessageDialog(frame, "��������ȷ�İٷ������������ްٷֺţ���");
					return;
				}
				ResultSet result = dbManager.query("{call percentstudentnum("+percent+")}");
				try{
					result.next();
				label_5.setText(result.getInt(1)+"");
				label_5.validate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("��ѯʧ��");
					e1.printStackTrace();
				}
			}
		});
		bigPanel_2.add(button_2);
		
		JLabel label_4 = new JLabel("\u7ED3\u679C\u4EBA\u6570\u4E3A\uFF1A");
		label_4.setFont(new Font("����", Font.PLAIN, 15));
		label_4.setBounds(402, 320, 90, 27);
		bigPanel_2.add(label_4);
		
		JLabel label_6 = new JLabel("\u67E5\u8BE2\u5168\u7701\u6392\u540D\u524D\u767E\u5206\u4E4B\u591A\u5C11\u7684\u5B66\u751F\u540D\u5355");
		label_6.setFont(new Font("����", Font.PLAIN, 20));
		label_6.setBounds(14, 345, 778, 27);
		bigPanel_2.add(label_6);
		
		JLabel label_7 = new JLabel("\u8F93\u5165\u767E\u5206\u6BD4\u6570\u5B57");
		label_7.setFont(new Font("����", Font.PLAIN, 15));
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
					JOptionPane.showMessageDialog(frame, "��������ȷ�İٷ������������ްٷֺţ���");
					return;
				}
				String sqlString = "{call percentstudentsubject("+percent+")}" ;
				String[] searchTitle = new String[]{"ѧ����","����","��λ","��Դ��","��Ŀ����","רҵ��","רҵ����","רҵ��","ѧ��"};
				try {
					JScrollPane scrollpane = createQuaryPanel(sqlString, searchTitle,tempResult,"���");
					searchPanel_3.removeAll();
					searchPanel_3.add(scrollpane);
					searchPanel_3.validate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("��ѯʧ��");
					e1.printStackTrace();
				}
			}
		});
		bigPanel_2.add(button_3);
		
		JButton button = new JButton("\u57FA\u672C\u8868\u53CA\u89C6\u56FE\u60C5\u51B5");
		button.setFont(new Font("����", Font.PLAIN, 20));
		button.setBounds(0, -2, 198, 29);
		button.setEnabled(false);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("\u67E5\u8BE2\u5F55\u53D6\u60C5\u51B5");
		button_1.setFont(new Font("����", Font.PLAIN, 20));
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
		label1.setFont(new Font("����", Font.PLAIN, 18));
		label1.setBounds(14, 0, 201, 29);
		bigPanel_1.add(label1);

		JLabel label_11 = new JLabel("\u5F55\u53D6\u7684\u7EDF\u8BA1\u4FE1\u606F\uFF1A");
		label_11.setFont(new Font("����", Font.PLAIN, 18));
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
				System.out.println("ѡ�е�ҳ������ǣ�"+index);
				System.out.println("����·��Ϊ��"+savePath);
				if(savePath!=null){
					try {
						saveExcelFile(resultVector,savePath);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("�ļ�д���쳣��");
						e.printStackTrace();
					}
				}
			}
		});
		bigPanel_1.add(button_4);

		/************************** ��һ���ֵġ������� **************************************/
		/********** ������ĵ�һ��ҳ�棨¼ȡ�ƻ��� ********/
		// �����ͷ
		String[] enrollTitle = new String[] { "רҵ��", "רҵ����", "רҵ��", "��Ŀ����", "����", "¼ȡ����" };
		// ������ 1 ��ѡ���ѡ�ֻ���� ���⣩
		try {
			tabbedPane.addTab("¼ȡ�ƻ���Ϣ", createQuaryPanel(quaryEnrollment, enrollTitle,enrollment,"¼ȡ�ƻ���Ϣ"));
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e2.printStackTrace();
		}

		/********** ������ĵڶ���ҳ�棨ѧ����Ϣ�� ********/
		// �����ͷ
		String[] studentTitle = new String[] { "ѧ����", "����", "־Ը1", "־Ը2", "־Ը3", "־Ը4", "־Ը5", "־Ը6", "�Ƿ���ӵ���", "ȫʡ��λ",
				"��Դ��", "��Ŀ����" };
		// ������ 1 ��ѡ���ѡ�ֻ���� ���⣩
		try {
			tabbedPane.addTab("����־Ը��Ϣ", createQuaryPanel(quaryStudent, studentTitle,student,"����־Ը��Ϣ"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}

		/********** ������ĵ�����ҳ�棨¼ȡ����� ********/
		String[] enrolledStudentTitle = new String[] { "ѧ����", "����", "ȫʡ��λ", "��Դ��", "��Ŀ����", "רҵ��", "רҵ����", "רҵ����",
				"ѧ��" };
		try {
			tabbedPane.addTab("��¼ȡ����������Ϣ", createQuaryPanel(quaryEnrolledStudent, enrolledStudentTitle,enrolledStudent,"��¼ȡ����������Ϣ"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵ��ĸ�ҳ�棨��רҵ¼ȡ���ͳ����Ϣ�� ********/
		String[] specialtySumaryTitle = new String[] { "רҵ���", "רҵ����", "רҵ����", "�����λ", "��߷�", "�����λ", "��ͷ�", "ƽ����" };
		try {
			tabbedPane.addTab("��רҵ¼ȡ���ͳ��", createQuaryPanel(quarySpecailtyEnrollSumary, specialtySumaryTitle,specailtyEnrollSumary,"��רҵ¼ȡ���ͳ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵ����ҳ�棨���˵�ѧ����Ϣ�� ********/
		try {
			tabbedPane.addTab("���˵�ѧ��", createQuaryPanel(quaryUnEnrolledStudent, studentTitle,unEnrolledStudent,"���˵�ѧ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵ�����ҳ�棨δ����ֱ��¼ȡѧ����Ϣ�� ********/
		try {
			tabbedPane.addTab("δ����ֱ��¼ȡѧ��", createQuaryPanel(quaryDirectEnrolledStudent, studentTitle,directEnrolledStudent,"δ����ֱ��¼ȡѧ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵ��߸�ҳ�棨ֱ���˵�ѧ����Ϣ�� ********/
		try {
			tabbedPane.addTab("ֱ���˵�ѧ��", createQuaryPanel(quaryDirectUnEnrolledStudent, studentTitle,directUnEnrolledStudent,"ֱ���˵�ѧ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵڰ˸�ҳ�棨����ʱ�˵�ѧ����Ϣ�� ********/
		try {
			tabbedPane.addTab("����ʱ�˵�ѧ��", createQuaryPanel(quaryNotDirectUnEnrolledStudent, studentTitle,notDirectUnEnrolledStudent,"����ʱ�˵�ѧ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}
		/********** ������ĵھŸ�ҳ�棨����ʱ��¼ȡѧ����Ϣ�� ********/
		try {
			tabbedPane.addTab("����ʱ��¼ȡѧ��", createQuaryPanel(quaryNotDirectEnrolledStudent, studentTitle,notDirectEnrolledStudent,"����ʱ��¼ȡѧ��"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e1.printStackTrace();
		}

		/************************** ��һ��ġ�¼ȡͳ����Ϣ�� **************************************/
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(14, 473, 908, 73);
		bigPanel_1.add(tabbedPane_1);
		/************** ¼ȡͳ����Ϣ�ĵ�һ��ҳ��(����¼ȡ���) **************/
		String[] sumTitle1 = new String[] { "�����λ", "��߷�", "�����λ", "��ͷ�", "ƽ����" };
		// ������ 3 ��ѡ���ѡ�ֻ���� ���⣩
		try {
			tabbedPane_1.addTab("��¼ȡ����������Ϣ", createQuaryPanel(quarySumary1, sumTitle1,Sumary1,"��¼ȡ����������Ϣ"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("��ѯʧ��");
			e.printStackTrace();
		}
	}
	
	private JScrollPane createQuaryPanel(String sqlString, String[] title,ResultSet set,String fileName) throws SQLException {
		// ��titleת��Ϊvector����
		Vector<String> titleVector = new Vector<String>();
		for (String temp : title) {
			titleVector.add(temp);
		}
		// ��ȡ��ѯ���vector
		Vector<Object> vector = new Vector<Object>();
		ResultSet rs = dbManager.query(sqlString);
		set = rs;
		Vector<Object> storeVector = new Vector<Object>();
		storeVector.add(title);
		storeVector.add(rs);
		storeVector.add(fileName);
		rsList.add(storeVector);
		int columnSize = rs.getMetaData().getColumnCount();
		// Ϊ�����������
		while (rs.next()) {
			Vector<String> row = new Vector<String>();
			for (int i = 1; i <= columnSize; i++) {
				row.add(rs.getString(i));
			}
			vector.add(row);
		}
		// ������ʾ���
		JTable table = new JTable(new DefaultTableModel(vector, titleVector));
		table.setRowHeight(25);
		// ������񣬼��뵽���������
		JScrollPane scrollPane = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setViewportView(table);
		return scrollPane;
	}
	
	private String showFileSaveDialog() {
		String result = null;
        // ����һ��Ĭ�ϵ��ļ�ѡȡ��
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // ���ô��ļ�ѡ����Ĭ��������ļ���
        //fileChooser.setSelectedFile();

        // ���ļ�ѡ����߳̽�������, ֱ��ѡ��򱻹رգ�
        int code = fileChooser.showSaveDialog(frame);

        if (code == JFileChooser.APPROVE_OPTION) {
            // ��������"����", ���ȡѡ��ı���·��
            File file = fileChooser.getSelectedFile();
            result = file.getAbsolutePath();
        }
        return result;
    }
	
	private void saveExcelFile(Vector vector,String path) throws SQLException{
		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
		HSSFWorkbook wb = new HSSFWorkbook();
		// �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		HSSFSheet sheet = wb.createSheet("sheet");
		// ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		HSSFRow row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ
		
		HSSFCell cell = null;
		
		//��ȡ�������ݺ����ݼ���
		String[] title = (String[]) vector.get(0);
		ResultSet saveSet = (ResultSet) vector.get(1);
		String fileName = (String) vector.get(2);
		for(int i=0; i<title.length; i++){
			cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		//��ʼ��������
		int j=0;
		saveSet.beforeFirst();
		while(saveSet.next()){
			row = sheet.createRow((int) j + 1);
			for(int i=1; i<=title.length;i++){
				row.createCell((short) i-1).setCellValue((String)saveSet.getString(i));
			}
			j++;
		}
		//��ʼд�ļ�
		// �����������ļ��浽ָ��λ��
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
