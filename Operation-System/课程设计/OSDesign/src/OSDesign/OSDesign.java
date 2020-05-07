package OSDesign;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class OSDesign {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	private int jobNum = 6; // Ԥ��������������ҵ����Ĭ��Ϊ6
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;
	private JTable table_4;

	private boolean isStop = false; // �ж��Ƿ���ֹ
	private boolean isPause = false; // �ж��Ƿ���ͣ

	private JPanel panel_8; // ��ʾ��ǰʱ��
	private JPanel panel_3; // ��ʾ�������н���
	private JPanel panel_4; // ��ʾ�������ҵ
	private JPanel panel_6; // ��ʾ�ڴ�����ҵ
	private JPanel panel_7; // ��ʾ�������ҵ
	private JLabel stopLabel; // ʱ�������ʾ״̬

	private JLabel timeHour; // ʱ����ʾ��Сʱ
	private JLabel timeMinute; // ʱ����ʾ�ķ���

	private int MAXPROCESS = 2; // �������������ģ����������
	private int TIMESLICE = 1; // ʱ��Ƭ��С
	private int DELAY = 100; // ָ���������е�ʱ����ʱ
	private JCBList storeList; // �洢���У����ڴ洢�������ҵ
	private JCBList jcbWList; // ������
	private PCBList pcbWList; // �������У����������������ҵ
	private PCBList pcbFList; // ��ҵ��ɶ���

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OSDesign window = new OSDesign();
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
	public OSDesign() {
		initialize();
	}

	public void runSystem() {
		storeList.sortByTime(); // ��������а�����ʱ�����������������ٱȽ�����Ԫ��
		int num = storeList.size();
		JCB firstJob = storeList.pop();
		Time clock = firstJob.getArrivalTime();
		pcbWList.add(new PCB(firstJob));

		while (pcbFList.size() < num && !isStop) {
			if (isStop) {
				break;
			}
			// ģ����ҵ����ʱ�䵽��
			for (int i = 0; i < storeList.size(); i++) {
				if (clock.compareTo(storeList.getFirstTime()) == 0) {
					jcbWList.add(storeList.pop());
				} else {
					// �������õıȽ�
					break;
				}
			}

			if (pcbWList.size() < MAXPROCESS) {
				// ������ҵ���ȣ����������ȷ�������㷨
				jcbWList.sortByTime();
				while (pcbWList.size() < MAXPROCESS) {
					// ���������л�ȡԪ����ӵ����������У��������������������
					if (jcbWList.size() > 0) {
						pcbWList.add(new PCB(jcbWList.pop()));
					} else {
						break;
					}
				}
			}

			if (pcbWList.size() > 0) {
				// ���н��̵��ȣ����ÿ���ռ�����ȼ�����
				pcbWList.sortByPriority();
				// ���и��½�����Ϣ
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						changeTables(pcbWList.getFirst());
						pcbWList.updateTable();
						setClockOutput(clock);
					}
				}).start();
				try {
					Thread.sleep(DELAY);
				} catch (Exception e) {
					System.exit(0);// �˳�����
				}
				/****************************** ��ͣ���� *******************/
				while (isPause) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						System.exit(0);// �˳�����
					}
				}
				/****************** ������÷�����ֹ���� **********************/
				if (isStop) {
					break;
				}
				PCB pcb = pcbWList.pop();
				// �������е�ʱ���ͣ��1�룬���ڹ۲�
				// ���н���
				if (pcb.run(TIMESLICE) == Status.WAIT) {
					// ������һ��ʱ��Ƭ����ҵ��δ���
					pcbWList.add(pcb);
				} else {
					// ��ҵ�����
					pcb.setFinishedTime(clock);
					pcbFList.add(pcb);
				}
			}

			System.out.println("In " + clock.toString());
			System.out.println("�洢���У�");
			storeList.displayList();
			System.out.println("�����У�");
			jcbWList.displayList();
			System.out.println("�������У�");
			pcbWList.displayList();
			System.out.println("��ɶ��У�");
			pcbFList.displayList();
			System.out.println();

			clock.increase(TIMESLICE);
		}

		// ���и��½�����Ϣ
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				changeTables(null);
			}
		}).start();
	}

	// �������������б�
	private void changeTables(PCB pcb) {
		if (pcb == null) {
			for (int i = 0; i < 5; i++) {
				table_1.setValueAt("", 0, i);
			}
		} else {
			// ��������ִ����ҵ�б�
			table_1.setValueAt(pcb.getName(), 0, 0);
			table_1.setValueAt(pcb.getArrivalTimeString() + "", 0, 1);
			table_1.setValueAt(pcb.getRunedTime() + "", 0, 2);
			table_1.setValueAt(pcb.getJobSize() + "", 0, 3);
			table_1.setValueAt(pcb.getPriority() + "", 0, 4);
		}
		// �����ڴ�����������ҵ���������б�
		pcbWList.updateTable();
		pcbFList.updateTable();
		jcbWList.updateTable(5);
		// //���½���
		// panel_3.validate();
		// panel_4.validate();
		// panel_6.validate();
		// panel_7.validate();
	}

	public void setClockOutput(Time time) {
		int hour = time.getHour();
		int minute = time.getMinue();
		System.out.println("********* " + hour + " ********** " + minute + " *********");
		if (hour == 0) {
			timeHour.setText("00");
		} else {
			if (hour < 10) {
				timeHour.setText("0" + hour);
			} else {
				timeHour.setText("" + hour);
			}
		}
		if (minute == 0) {
			timeMinute.setText("00");
		} else {
			if (minute < 10) {
				timeMinute.setText("0" + minute);
			} else {
				timeMinute.setText("" + minute);
			}
		}
		panel_8.validate();
	}

	// ����ƽ����תʱ��
	private double calculate1() {
		double result = 0.0;
		for (PCB pcb : pcbFList.pcbList) {
			result += pcb.getFinishedTime().compareTo(pcb.getArrivalTime());
		}
		result = result / pcbFList.size();
		return result;
	}

	// ����ƽ����Ȩ��תʱ��
	private double calculate2() {
		double result = 0.0;
		for (PCB pcb : pcbFList.pcbList) {
			result += pcb.getFinishedTime().compareTo(pcb.getArrivalTime()) / pcb.getNeedTime();
		}
		result = result / pcbFList.size();
		return result;
	}

	private void initStoreList() {
		pcbFList.removeALL();
		pcbWList.removeALL();
		storeList.removeALL();
		jcbWList.removeALL();
		storeList.add(new JCB("JOB1", new Time("10:00"), 40, 5));
		storeList.add(new JCB("JOB2", new Time("10:20"), 30, 3));
		storeList.add(new JCB("JOB3", new Time("10:30"), 50, 4));
		storeList.add(new JCB("JOB4", new Time("10:50"), 20, 6));
		storeList.add(new JCB("JOB5", new Time("10:00"), 30, 3));
		storeList.add(new JCB("JOB6", new Time("10:10"), 10, 2));
		// ���½���
		storeList.updateTable(4);
		jcbWList.updateTable(5);
		pcbFList.updateTable();
		pcbWList.updateTable();
		// �ػ����������С���
		for (int i = 0; i < 5; i++) {
			table_1.setValueAt("", 0, i);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("����������ϵͳ��������:���ڿ� 3117004568��");
		frame.setBounds(100, 100, 1047, 734);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBackground(Color.WHITE);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel.setBounds(0, 0, 406, 134);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u63A7\u5236\u9762\u677F\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 17));
		label.setBounds(14, 13, 92, 18);
		panel.add(label);

		JLabel label_1 = new JLabel("\u4F5C\u4E1A\u8C03\u5EA6\uFF1A\u5148\u6765\u5148\u670D\u52A1");
		label_1.setBounds(14, 44, 165, 18);
		panel.add(label_1);

		JLabel label_2 = new JLabel("\u8FDB\u7A0B\u8C03\u5EA6\uFF1A\u53EF\u62A2\u5360\u4F18\u5148\u7EA7");
		label_2.setBounds(14, 75, 179, 18);
		panel.add(label_2);

		// ����ͣ���򡰼�������ť
		JButton button_4 = new JButton("\u6682\u505C");
		button_4.setEnabled(false);
		button_4.setBounds(299, 94, 92, 27);
		button_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isPause) {
					button_4.setText("��ͣ");
					isPause = false;
					timeHour.setForeground(Color.BLUE);
					timeMinute.setForeground(Color.BLUE);
					stopLabel.setForeground(Color.BLUE);
					stopLabel.setText("(������)");
					panel_8.validate();
				} else {
					button_4.setText("����");
					isPause = true;
					timeHour.setForeground(Color.RED);
					timeMinute.setForeground(Color.RED);
					stopLabel.setText("(����ͣ)");
					stopLabel.setForeground(Color.RED);
				}
			}
		});
		panel.add(button_4);

		panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBounds(410, 456, 608, 190);
		frame.getContentPane().add(panel_7);
		
		JLabel resultLabel = new JLabel("");
		resultLabel.setForeground(Color.RED);
		resultLabel.setFont(new Font("����", Font.PLAIN, 17));
		resultLabel.setBounds(145, 12, 411, 18);
		panel_7.add(resultLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 367, 406, 279);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel label_5 = new JLabel("\u9884\u8F93\u5165\u4F5C\u4E1A\u5217\u8868\uFF1A");
		label_5.setFont(new Font("����", Font.PLAIN, 17));
		label_5.setBounds(14, 11, 154, 18);
		panel_1.add(label_5);

		// ��ͷ��������
		Object[] columnNames = { "��ҵ����", "����ʱ��", "������ʱ��", "���ȼ���", "���ʱ��" };
		Object[] storeTitle = { "��ҵ����", "����ʱ��", "������ʱ��", "���ȼ���" };
		// �������������
		Object[][] rowData = { { "JOB1", "10:00", 40, 5 }, { "JOB2", "10:20", 30, 3 }, { "JOB3", "10:30", 50, 4 },
				{ "JOB4", "10:50", 20, 6 } };
		// ����һ�����ָ�� ���������� �� ��ͷ

		table = new JTable(new DefaultTableModel(rowData, storeTitle));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
		table.setPreferredScrollableViewportSize(new Dimension(400, 300));
		table.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(14, 33, 387, 233);
		panel_1.add(scrollPane);
		scrollPane.setWheelScrollingEnabled(true);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_2.setBounds(0, 220, 406, 134);
		frame.getContentPane().add(panel_2);

		JLabel label_3 = new JLabel("\u6D4B\u8BD5\u8F93\u5165\u9762\u677F\uFF1A");
		label_3.setFont(new Font("����", Font.PLAIN, 17));
		label_3.setBounds(14, 13, 150, 18);
		panel_2.add(label_3);

		JLabel label_4 = new JLabel("\u8FDB\u5165\u65F6\u95F4\uFF1A");
		label_4.setBounds(14, 44, 76, 18);
		panel_2.add(label_4);

		JLabel label_6 = new JLabel("\u4F18\u5148\u6570\uFF1A");
		label_6.setBounds(14, 78, 63, 18);
		panel_2.add(label_6);

		JLabel label_7 = new JLabel("\u9700\u8FD0\u884C\u65F6\u95F4\uFF1A");
		label_7.setBounds(193, 44, 99, 18);
		panel_2.add(label_7);

		textField = new JTextField();
		textField.setBounds(90, 42, 86, 24);
		panel_2.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(287, 41, 86, 24);
		panel_2.add(textField_1);

		panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBounds(410, 316, 608, 141);
		frame.getContentPane().add(panel_6);
		
		// ��Ҫѡ�����Ŀ
		String[] listData = new String[] { "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

		JComboBox comboBox = new JComboBox(listData);
		comboBox.setBounds(90, 75, 53, 24);
		panel_2.add(comboBox);

		// �����롱�Զ�����̰�ť
		JButton button = new JButton("\u6DFB\u52A0");
		button.setBounds(178, 93, 86, 30);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String inTime = textField.getText();
				String runTime = textField_1.getText();
				String priority = (String) comboBox.getSelectedItem();
				int runMinute;
				// ���������
				if (inTime.equals("") || runTime.equals("") || priority.equals("")) {
					JOptionPane.showMessageDialog(frame, "��������ҵ��������Ϣ��");
				} else {
					try {
						int hour, minute;
						String[] temp = inTime.split(":");
						hour = Integer.parseInt(temp[0]);
						minute = Integer.parseInt(temp[0]);
						if (temp.length != 2) {
							JOptionPane.showMessageDialog(frame, "�������硰10:00����ʽ�ĵ���ʱ�䣨ע�⡰:��ΪӢ������ġ�:������");
							return;
						}
						if (hour >= 24 || minute >= 60) {
							JOptionPane.showMessageDialog(frame, "��������ȷ��Χ�ڵ�������ʱ�䣡");
							return;
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(frame, "�������硰10:00����ʽ�ĵ���ʱ�䣨ע�⡰:��ΪӢ������ġ�:������");
						return;
					}

					try {
						runMinute = Integer.parseInt(runTime);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(frame, "��������ȷ��������ʱ�䣨�磺1��10�ȣ���");
						return;
					}
					// ����Ϸ�
					jobNum++;
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (jobNum > model.getRowCount()) {
						model.addRow(new Object[] { "JOB" + jobNum, inTime, runTime, priority });
					}
					// ���������ѡ���
					textField_1.setText("");
					textField.setText("");
					comboBox.setSelectedIndex(0);
					// ����ҵ�������
					storeList.add(new JCB("JOB" + jobNum, new Time(inTime), runMinute, Integer.parseInt(priority)));
				}
			}
		});
		panel_2.add(button);

		// ��������ť
		JButton button_2 = new JButton("\u6E05\u7A7A");
		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// ���������ѡ���
				textField_1.setText("");
				textField.setText("");
				comboBox.setSelectedIndex(0);
			}
		});
		button_2.setBounds(287, 93, 86, 30);
		panel_2.add(button_2);

		// ��ʼ���а�ť
		JButton button_1 = new JButton("\u5F00\u59CB");
		button_1.setBounds(193, 54, 92, 27);
		button_1.addActionListener(new ActionListener() {
			// ��ʼ����
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DecimalFormat df =new DecimalFormat("#0.00");
				button_1.setEnabled(false);
				button_4.setEnabled(true);
				stopLabel.setForeground(Color.BLUE);
				stopLabel.setText("(������)");
				isStop = false;
				timeHour.setText("00");
				timeMinute.setText("00");
				timeHour.setForeground(Color.BLUE);
				timeMinute.setForeground(Color.BLUE);
				button.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						runSystem();
						resultLabel.setText(
								" ƽ����תʱ�䣺" + df.format(calculate1()) + "   ƽ����Ȩ��תʱ�䣺" + df.format(calculate2()));
						JOptionPane.showMessageDialog(frame, "ִ�н�����");
						button_4.setEnabled(false);
						stopLabel.setText("��������");
					}
				}).start();
			}

		});
		panel.add(button_1);

		// ���ð�ť
		JButton button_3 = new JButton("\u91CD\u7F6E");
		button_3.setBounds(299, 54, 92, 27);
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				initStoreList();
				button_1.setEnabled(true);
				stopLabel.setForeground(Color.BLUE);
				stopLabel.setText("");
				isStop = true;
				button_4.setEnabled(false);
				timeHour.setText("00");
				timeMinute.setText("00");
				timeHour.setForeground(Color.BLUE);
				timeMinute.setForeground(Color.BLUE);
				button.setEnabled(true);

				isPause = false;
				button_4.setText("��ͣ");
				jobNum = storeList.size();
				resultLabel.setText("");
			}
		});
		panel.add(button_3);

		panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(410, 0, 605, 103);
		frame.getContentPane().add(panel_3);

		JLabel label_8 = new JLabel("\u5F53\u524D\u6B63\u5728\u6267\u884C\u7684\u4F5C\u4E1A\uFF1A");
		label_8.setFont(new Font("����", Font.PLAIN, 17));
		label_8.setBounds(14, 11, 177, 18);
		panel_3.add(label_8);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		panel_5.setBounds(14, 34, 580, 66);
		panel_3.add(panel_5);

		// ��ͷ��������
		Object[] title = { "��ҵ����", "�ύʱ��", "������ʱ��", "����ʱ��", "���ȼ���" };
		Object[][] currentRow = { { "", "", "", "", "" } };
		table_1 = new JTable(currentRow, title);
		table_1.setBounds(0, 0, 566, 48);
		table_1.setRowHeight(30);
		table_1.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
		table_1.setPreferredScrollableViewportSize(new Dimension(400, 300));
		table_1.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		// �� ��ͷ ��ӵ�����������ʹ����ͨ���м�������ӱ��ʱ����ͷ �� ���� ��Ҫ�ֿ���ӣ�
		panel_5.add(table_1.getTableHeader(), BorderLayout.NORTH);
		// �� ������� ��ӵ���������
		panel_5.add(table_1, BorderLayout.CENTER);

		panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(410, 109, 608, 204);
		frame.getContentPane().add(panel_4);

		JLabel label_9 = new JLabel("\u5916\u5B58\u5C31\u7EEA\u961F\u5217\uFF1A");
		label_9.setFont(new Font("����", Font.PLAIN, 17));
		label_9.setBounds(14, 11, 154, 18);
		panel_4.add(label_9);

		JScrollPane scrollPane_1 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setWheelScrollingEnabled(true);
		scrollPane_1.setBounds(14, 42, 594, 156);
		panel_4.add(scrollPane_1);

		Object[][] outSideData = { { "", "", "", "", "" }, { "", "", "", "", "" }, { "", "", "", "", "" },
				{ "", "", "", "", "" } };
		table_2 = new JTable(new DefaultTableModel(outSideData, columnNames));
		table_2.setRowHeight(30);
		table_2.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
		table_2.setPreferredScrollableViewportSize(new Dimension(400, 300));
		table_2.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_1.setViewportView(table_2);

		JLabel label_10 = new JLabel("\u5185\u5B58\u5C31\u7EEA\u961F\u5217\uFF1A");
		label_10.setFont(new Font("����", Font.PLAIN, 17));
		label_10.setBounds(14, 11, 119, 18);
		panel_6.add(label_10);

		JScrollPane scrollPane_2 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setWheelScrollingEnabled(true);
		scrollPane_2.setBounds(14, 42, 594, 167);
		panel_6.add(scrollPane_2);

		Object[][] inSideData = { { "", "", "", "", "" }, { "", "", "", "", "" } };
		table_3 = new JTable(new DefaultTableModel(inSideData, columnNames));
		table_3.setRowHeight(30);
		table_3.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
		table_3.setPreferredScrollableViewportSize(new Dimension(400, 300));
		table_3.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_2.setViewportView(table_3);

		JLabel label_11 = new JLabel("\u5B8C\u6210\u4F5C\u4E1A\u961F\u5217\uFF1A");
		label_11.setFont(new Font("����", Font.PLAIN, 17));
		label_11.setBounds(14, 11, 154, 18);
		panel_7.add(label_11);

		JScrollPane scrollPane_3 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_3.setWheelScrollingEnabled(true);
		scrollPane_3.setBounds(14, 42, 594, 135);
		panel_7.add(scrollPane_3);

		Object[][] finishedData = { { "", "", "", "", "" }, { "", "", "", "", "" }, { "", "", "", "", "" },
				{ "", "", "", "", "" } };
		table_4 = new JTable(new DefaultTableModel(finishedData, columnNames));
		table_4.setRowHeight(30);
		table_4.getTableHeader().setFont(new Font("����", Font.PLAIN, 17));
		table_4.getTableHeader().setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_3.setViewportView(table_4);

		panel_8 = new JPanel();
		panel_8.setBounds(0, 140, 406, 77);
		frame.getContentPane().add(panel_8);
		panel_8.setLayout(null);

		JLabel label_12 = new JLabel("\u5F53\u524D\u65F6\u95F4\u663E\u793A\u9762\u677F\uFF1A");
		label_12.setFont(new Font("����", Font.PLAIN, 17));
		label_12.setBounds(14, 13, 161, 18);
		panel_8.add(label_12);

		timeHour = new JLabel("00");
		timeHour.setForeground(Color.BLUE);
		timeHour.setFont(new Font("����", Font.PLAIN, 30));
		timeHour.setHorizontalAlignment(SwingConstants.CENTER);
		timeHour.setBounds(186, 13, 60, 51);
		panel_8.add(timeHour);

		timeMinute = new JLabel("00");
		timeMinute.setForeground(Color.BLUE);
		timeMinute.setHorizontalAlignment(SwingConstants.CENTER);
		timeMinute.setFont(new Font("����", Font.PLAIN, 30));
		timeMinute.setBounds(270, 13, 60, 51);
		panel_8.add(timeMinute);

		JLabel label_14 = new JLabel(":");
		label_14.setHorizontalAlignment(SwingConstants.CENTER);
		label_14.setFont(new Font("����", Font.PLAIN, 30));
		label_14.setBounds(234, 13, 38, 51);
		panel_8.add(label_14);

		stopLabel = new JLabel("");
		stopLabel.setFont(new Font("����", Font.PLAIN, 17));
		stopLabel.setForeground(Color.RED);
		stopLabel.setBounds(330, 30, 76, 18);
		panel_8.add(stopLabel);

		storeList = new JCBList(table); // �洢���У����ڴ洢�������ҵ
		jcbWList = new JCBList(table_2); // ������
		pcbWList = new PCBList(table_3); // �������У����������������ҵ
		
		
		pcbFList = new PCBList(table_4); // ��ҵ��ɶ���
		
		// �洢�б��ʼ��
		initStoreList();
	}
}
