package OSDesign;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import OSDesign.Time;

public class PCBList {
	// 存储进程的列表
	ArrayList<PCB> pcbList = new ArrayList<PCB>();
	private JTable table;

	public PCBList(JTable table) {
		this.table = table;
	}

	// 向列表添加对象
	public boolean add(PCB pcb) {
		return pcbList.add(pcb);
	}

	// 移除队列中的对象
	public boolean remove(PCB pcb) {
		return pcbList.remove(pcb);
	}

	// 弹栈，从列表中获得第一个对象，并将其移除出列表
	public PCB pop() {
		if (pcbList.size() > 0) {
			PCB result = pcbList.get(0);
			pcbList.remove(0);
			return result;
		}
		return null;
	}

	// 获取第一个元素（为不安全的使用方法）
	public PCB getFirst() {
		if(pcbList.size()==0){
			return null;
		}
		return pcbList.get(0);
	}

	// 获取进程队列的大小
	public int size() {
		return pcbList.size();
	}

	// 对列表进行按优先级升序排序
	public void sortByPriority() {
		Collections.sort(pcbList);
	}

	public void updateTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		// 清除所有行
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < 5; j++) {
				table.setValueAt("", i, j);
			}
		}
		if (pcbList.size() == 0) {
			return;
		} else {
			for (int i = 0; i < pcbList.size(); i++) {
				PCB pcb = pcbList.get(i);
				// 判断进程是否完成已输出结束时间
				if (pcb.getStatus().equals(Status.FINISH)) {
					Time finishTime = pcb.getFinishedTime();
					String minute = finishTime.getMinue()+"",hour=finishTime.getHour()+"";
					if(finishTime.getMinue()<10){
						minute = "0"+finishTime.getMinue();
					}
					if(finishTime.getHour()<10){
						hour = "0"+finishTime.getHour();
					}
					String finishTimeString = hour+" : "+minute; 
					if (i >= model.getRowCount()) {
						model.addRow(new Object[] { pcb.getName(), pcb.getArrivalTimeString(), pcb.getNeedTime() + "",
								pcb.getPriority() + "", finishTimeString });
					} else {
						table.setValueAt(pcb.getName(), i, 0);
						table.setValueAt(pcb.getArrivalTimeString(), i, 1);
						table.setValueAt(pcb.getNeedTime() + "", i, 2);
						table.setValueAt(pcb.getPriority() + "", i, 3);
						table.setValueAt(finishTimeString, i, 4);
					}
				} else {
					if (i > model.getRowCount()) {
						model.addRow(new Object[] { pcb.getName(), pcb.getArrivalTimeString(), pcb.getNeedTime() + "",
								pcb.getPriority() + "", "未知" });
					} else {
						table.setValueAt(pcb.getName(), i, 0);
						table.setValueAt(pcb.getArrivalTimeString(), i, 1);
						table.setValueAt(pcb.getNeedTime() + "", i, 2);
						table.setValueAt(pcb.getPriority() + "", i, 3);
						table.setValueAt("未知", i, 4);
					}
				}

			}
		}
	}

	// 删除列表中的所有元素
	public void removeALL() {
		pcbList.removeAll(pcbList);
	}

	// 打印列表中的进程的各属性值
	public void displayList() {
		System.out.println("pcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "runedTime"
				+ " " + "pcbStatus" + " " + "finishTime");
		for (PCB pcb : pcbList) {
			pcb.display();
		}
	}
}
