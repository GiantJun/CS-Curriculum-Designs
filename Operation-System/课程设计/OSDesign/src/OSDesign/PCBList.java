package OSDesign;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import OSDesign.Time;

public class PCBList {
	// �洢���̵��б�
	ArrayList<PCB> pcbList = new ArrayList<PCB>();
	private JTable table;

	public PCBList(JTable table) {
		this.table = table;
	}

	// ���б���Ӷ���
	public boolean add(PCB pcb) {
		return pcbList.add(pcb);
	}

	// �Ƴ������еĶ���
	public boolean remove(PCB pcb) {
		return pcbList.remove(pcb);
	}

	// ��ջ�����б��л�õ�һ�����󣬲������Ƴ����б�
	public PCB pop() {
		if (pcbList.size() > 0) {
			PCB result = pcbList.get(0);
			pcbList.remove(0);
			return result;
		}
		return null;
	}

	// ��ȡ��һ��Ԫ�أ�Ϊ����ȫ��ʹ�÷�����
	public PCB getFirst() {
		if(pcbList.size()==0){
			return null;
		}
		return pcbList.get(0);
	}

	// ��ȡ���̶��еĴ�С
	public int size() {
		return pcbList.size();
	}

	// ���б���а����ȼ���������
	public void sortByPriority() {
		Collections.sort(pcbList);
	}

	public void updateTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		// ���������
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
				// �жϽ����Ƿ�������������ʱ��
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
								pcb.getPriority() + "", "δ֪" });
					} else {
						table.setValueAt(pcb.getName(), i, 0);
						table.setValueAt(pcb.getArrivalTimeString(), i, 1);
						table.setValueAt(pcb.getNeedTime() + "", i, 2);
						table.setValueAt(pcb.getPriority() + "", i, 3);
						table.setValueAt("δ֪", i, 4);
					}
				}

			}
		}
	}

	// ɾ���б��е�����Ԫ��
	public void removeALL() {
		pcbList.removeAll(pcbList);
	}

	// ��ӡ�б��еĽ��̵ĸ�����ֵ
	public void displayList() {
		System.out.println("pcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "runedTime"
				+ " " + "pcbStatus" + " " + "finishTime");
		for (PCB pcb : pcbList) {
			pcb.display();
		}
	}
}
