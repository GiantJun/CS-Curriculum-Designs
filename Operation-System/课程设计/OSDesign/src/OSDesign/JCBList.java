package OSDesign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JCBList {
	// �洢���̵��б�
	private List<JCB> jcbList = new ArrayList<JCB>();
	private JTable table;

	public JCBList(JTable table) {
		this.table = table;
	}

	// ���б���Ӷ���
	public boolean add(JCB jcb) {
		return jcbList.add(jcb);
	}

	// �Ƴ������еĶ���
	public boolean remove(JCB jcb) {
		return jcbList.remove(jcb);
	}

	// ��ջ�����б��л�õ�һ�����󣬲������Ƴ����б�
	public JCB pop() {
		if (jcbList.size() > 0) {
			JCB result = jcbList.get(0);
			jcbList.remove(0);
			return result;
		}
		return null;
	}

	// ��ȡ���̶��еĴ�С
	public int size() {
		return jcbList.size();
	}

	// ���б�����ʱ����������
	public void sortByTime() {
		Collections.sort(jcbList);
	}

	// ��õ�һ��Ԫ�صĵ���ʱ��
	public Time getFirstTime() {
		return jcbList.get(0).getArrivalTime();
	}

	//����num�����ڱ�ʶ��ʾ�Ĳ�ͬ����������һ����4�У�һ����5��
	public void updateTable(int num) {

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		// ���������
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < num; j++) {
				table.setValueAt("", i, j);
			}
		}
		if (jcbList.size() == 0) {
			return;
		} else {
			if (num == 4) {
				for (int i = 0; i < jcbList.size(); i++) {
					JCB jcb = jcbList.get(i);
					if (i >= model.getRowCount()) {
						model.addRow(new Object[] { jcb.getName(), jcb.getArrivalTimeString(), jcb.getNeedTime() + "",
								jcb.getPriority() + "" });
					} else {
						table.setValueAt(jcb.getName(), i, 0);
						table.setValueAt(jcb.getArrivalTimeString(), i, 1);
						table.setValueAt(jcb.getNeedTime() + "", i, 2);
						table.setValueAt(jcb.getPriority() + "", i, 3);
					}
				}
			} else {
				for (int i = 0; i < jcbList.size(); i++) {
					JCB jcb = jcbList.get(i);
					if (i > model.getRowCount()) {
						model.addRow(new Object[] { jcb.getName(), jcb.getArrivalTimeString(), jcb.getNeedTime() + "",
								jcb.getPriority() + "", "δ֪" });
					} else {
						table.setValueAt(jcb.getName(), i, 0);
						table.setValueAt(jcb.getArrivalTimeString(), i, 1);
						table.setValueAt(jcb.getNeedTime() + "", i, 2);
						table.setValueAt(jcb.getPriority() + "", i, 3);
						table.setValueAt("δ֪", i, 4);
					}
				}
			}
		}
	}

	// ɾ���б��е�����Ԫ��
	public void removeALL() {
		jcbList.removeAll(jcbList);
	}

	// ��ӡ�б��еĽ��̵ĸ�����ֵ
	public void displayList() {
		System.out
				.println("jcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "jcbStatus");
		for (JCB jcb : jcbList) {
			jcb.display();
		}
	}
}
