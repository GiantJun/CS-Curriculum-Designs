package OSDesign;

public class PCB implements Comparable<PCB> {
	private String pcbName = ""; // ���ƿ���
	private int priorityCode = 100; // ���ȼ���
	private Time arrivalTime = new Time("23:59"); // ����ʱ��
	private int needTime = 0; // ��Ҫ���е�ʱ��
	private int runedTime = 0; // �Ѿ����е�ʱ��
	private Time finishedTime = new Time("23:59"); // ���ʱ��
	private Status pcbStatus = Status.WAIT; // ����������״̬

	public PCB(JCB jcb) {
		this.pcbName = jcb.getName();
		this.priorityCode = jcb.getPriority();
		this.arrivalTime = jcb.getArrivalTime();
		this.needTime = jcb.getNeedTime();
	}

	// ���иý���
	public Status run(int timeSlice) {
		runedTime += timeSlice;
		if (runedTime >= needTime) {
			this.pcbStatus = Status.FINISH;
			return Status.FINISH;
		} else {
			this.pcbStatus = Status.WAIT;
			return Status.WAIT;
		}
	}

	public void setFinishedTime(Time finishedTime) {
		this.finishedTime = new Time(finishedTime.toString());
	}

	public Time getFinishedTime(){
		return finishedTime;
	}
	// ��ȡ��PCB�����ȼ���
	public int getPriority() {
		return this.priorityCode;
	}

	// ��ȡ��PCB������
	public String getName() {
		return this.pcbName;
	}

	// ��ȡ��PCB�������е�ʱ��
	public int getJobSize() {
		return needTime - runedTime;
	}

	// ��ȡ��PCB�ĵ���ʱ��
	public Time getArrivalTime() {
		return new Time(this.arrivalTime.toString());
	}

	// ��ȡ��PCB�ĵ���ʱ��
	public String getArrivalTimeString() {
		String minute, hour;
		if (arrivalTime.getHour() == 0) {
			hour = "00";
		} else {
			hour = arrivalTime.getHour() + "";
		}
		if (arrivalTime.getMinue() == 0) {
			minute = "00";
		} else {
			minute = arrivalTime.getMinue() + "";
		}
		return hour + ":" + minute;
	}

	// ��ȡPCB�Ѿ����е�ʱ��
	public int getRunedTime() {
		return runedTime;
	}

	//��ȡ������Ҫ���е�ʱ��
	public int getNeedTime() {
		// TODO Auto-generated method stub
		return this.needTime;
	}
	
	//��ȡ���̵�״̬
	public Status getStatus(){
		return pcbStatus;
	}
	
	// ���ý������ȼ�
	public void setPriorityCode(int timeSlice) {
		priorityCode -= timeSlice;
	}

	// ��ʾPCB�а�������Ϣ
	public void display() {
		System.out.println(pcbName + "\t" + priorityCode + "\t" + arrivalTime + "\t" + needTime + "\t" + runedTime
				+ "\t" + pcbStatus + "\t" + finishedTime.toString());
	}

	// ����ִ����ϣ�ģ���ͷŽ�����Դ
	public void killProcess() {
		this.pcbStatus = Status.KILLED;
	}

	@Override
	public int compareTo(PCB o) {
		// TODO Auto-generated method stub
		return this.priorityCode - o.getPriority();
	}


}
