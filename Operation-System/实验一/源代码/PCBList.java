package giantJun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import giantJun.Status;
import giantJun.PCB;;

public class PCBList {
	// �洢���̵��б�
	public List<PCB> pcbList = new ArrayList<PCB>();
	private JTable table;
	
	public PCBList(JTable table){
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

	// ������ҵ��С���Խ����б������������
	public void sortForSJF() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return arg0.getJobSize() - arg1.getJobSize();
			}
		});
	}

	// ���ݽ��̵���Ӧ�ȣ��Խ����б������������
	public void sortForHRN() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return (int) Math.floor(arg0.getHRN() - arg1.getHRN());
			}
		});
	}

	// ���ݽ��̵ĵ���ʱ�䣬�Խ����б������������
	public void sortForFCFS() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return arg0.getArrivalTime().compareTo(arg1.getArrivalTime());
			}
		});
	}

	// ���ݽ��̵����������Խ����б������������
	public void sortForPriority() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return arg0.getPriority() - arg1.getPriority();
			}
		});
	}

	public PCB get(int index){
		return pcbList.get(index);
	}
	
	public boolean isExist(PCB pcb){
		for(PCB temp : pcbList){
			if(temp.getName().equals(pcb.getName())){
				return true;
			}
		}
		return false;
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

	// ��ȡ���̶��еĴ�С
	public int size() {
		return pcbList.size();
	}
	
	//Ϊ��ģ�����һֱ��ת�����õģ���ȡ����һ�����̵ĵ���ʱ�䣬���������ת
	public Time getFirstTime() {
		return pcbList.get(0).getArrivalTime();
	}

	// ��ӡ�б��еĽ��̵ĸ�����ֵ
	public void displayList() {
		System.out.println("pcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "runedTime"
				+ " " + "pcbStatus" + " " + "wairTime" + " " + "HRN");
		for (PCB pcb : pcbList) {
			pcb.display();
		}
		System.out.println();
	}
	
	//ɾ���б��е�����Ԫ��
	public void removeALL(){
		pcbList.removeAll(pcbList);
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
			if (pcbList.size() == 0) {
				return;
			} else {
				if (num == 4) {
					for (int i = 0; i < pcbList.size(); i++) {
						PCB pcb = pcbList.get(i);
						if (i >= model.getRowCount()) {
							model.addRow(new Object[] { pcb.getName(), pcb.getArrivalTimeString(), pcb.getNeedTime() + "",
									pcb.getPriority() + "" });
						} else {
							table.setValueAt(pcb.getName(), i, 0);
							table.setValueAt(pcb.getArrivalTimeString(), i, 1);
							table.setValueAt(pcb.getNeedTime() + "", i, 2);
							table.setValueAt(pcb.getPriority() + "", i, 3);
						}
					}
				} else {
					for (int i = 0; i < pcbList.size(); i++) {
						PCB pcb = pcbList.get(i);
						if(pcb.getStatus().equals(Status.FINISH)){
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
						}else{
							if (i >= model.getRowCount()) {
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
		}
		
		
}
