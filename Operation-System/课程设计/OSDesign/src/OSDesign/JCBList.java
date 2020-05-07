package OSDesign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JCBList {
	// 存储进程的列表
	private List<JCB> jcbList = new ArrayList<JCB>();
	private JTable table;

	public JCBList(JTable table) {
		this.table = table;
	}

	// 向列表添加对象
	public boolean add(JCB jcb) {
		return jcbList.add(jcb);
	}

	// 移除队列中的对象
	public boolean remove(JCB jcb) {
		return jcbList.remove(jcb);
	}

	// 弹栈，从列表中获得第一个对象，并将其移除出列表
	public JCB pop() {
		if (jcbList.size() > 0) {
			JCB result = jcbList.get(0);
			jcbList.remove(0);
			return result;
		}
		return null;
	}

	// 获取进程队列的大小
	public int size() {
		return jcbList.size();
	}

	// 对列表按到达时间升序排序
	public void sortByTime() {
		Collections.sort(jcbList);
	}

	// 获得第一个元素的到达时间
	public Time getFirstTime() {
		return jcbList.get(0).getArrivalTime();
	}

	//参数num是用于标识显示的不同表，共有两种一种是4列，一种是5列
	public void updateTable(int num) {

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		// 清除所有行
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
								jcb.getPriority() + "", "未知" });
					} else {
						table.setValueAt(jcb.getName(), i, 0);
						table.setValueAt(jcb.getArrivalTimeString(), i, 1);
						table.setValueAt(jcb.getNeedTime() + "", i, 2);
						table.setValueAt(jcb.getPriority() + "", i, 3);
						table.setValueAt("未知", i, 4);
					}
				}
			}
		}
	}

	// 删除列表中的所有元素
	public void removeALL() {
		jcbList.removeAll(jcbList);
	}

	// 打印列表中的进程的各属性值
	public void displayList() {
		System.out
				.println("jcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "jcbStatus");
		for (JCB jcb : jcbList) {
			jcb.display();
		}
	}
}
