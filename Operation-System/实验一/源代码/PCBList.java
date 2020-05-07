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
	// 存储进程的列表
	public List<PCB> pcbList = new ArrayList<PCB>();
	private JTable table;
	
	public PCBList(JTable table){
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

	// 根据作业大小，对进程列表进行升序排序
	public void sortForSJF() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return arg0.getJobSize() - arg1.getJobSize();
			}
		});
	}

	// 根据进程的响应比，对进程列表进行升序排序
	public void sortForHRN() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return (int) Math.floor(arg0.getHRN() - arg1.getHRN());
			}
		});
	}

	// 根据进程的到达时间，对进程列表进行升序排序
	public void sortForFCFS() {
		Collections.sort(pcbList, new Comparator<PCB>() {
			@Override
			public int compare(PCB arg0, PCB arg1) {
				// TODO Auto-generated method stub
				return arg0.getArrivalTime().compareTo(arg1.getArrivalTime());
			}
		});
	}

	// 根据进程的优先数，对进程列表进行升序排序
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
	
	// 弹栈，从列表中获得第一个对象，并将其移除出列表
	public PCB pop() {
		if (pcbList.size() > 0) {
			PCB result = pcbList.get(0);
			pcbList.remove(0);
			return result;
		}
		return null;
	}

	// 获取进程队列的大小
	public int size() {
		return pcbList.size();
	}
	
	//为了模拟机器一直运转而设置的，获取任意一个进程的到达时间，允许机器空转
	public Time getFirstTime() {
		return pcbList.get(0).getArrivalTime();
	}

	// 打印列表中的进程的各属性值
	public void displayList() {
		System.out.println("pcbName" + " " + "priorityCode" + " " + "arrivalTime" + " " + "needTime" + " " + "runedTime"
				+ " " + "pcbStatus" + " " + "wairTime" + " " + "HRN");
		for (PCB pcb : pcbList) {
			pcb.display();
		}
		System.out.println();
	}
	
	//删除列表中的所有元素
	public void removeALL(){
		pcbList.removeAll(pcbList);
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
		}
		
		
}
