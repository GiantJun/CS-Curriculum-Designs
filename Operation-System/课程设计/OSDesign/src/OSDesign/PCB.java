package OSDesign;

public class PCB implements Comparable<PCB> {
	private String pcbName = ""; // 控制块名
	private int priorityCode = 100; // 优先级数
	private Time arrivalTime = new Time("23:59"); // 到达时间
	private int needTime = 0; // 需要运行的时间
	private int runedTime = 0; // 已经运行的时间
	private Time finishedTime = new Time("23:59"); // 完成时间
	private Status pcbStatus = Status.WAIT; // 进程所处的状态

	public PCB(JCB jcb) {
		this.pcbName = jcb.getName();
		this.priorityCode = jcb.getPriority();
		this.arrivalTime = jcb.getArrivalTime();
		this.needTime = jcb.getNeedTime();
	}

	// 运行该进程
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
	// 获取该PCB的优先级数
	public int getPriority() {
		return this.priorityCode;
	}

	// 获取该PCB的名字
	public String getName() {
		return this.pcbName;
	}

	// 获取该PCB仍需运行的时间
	public int getJobSize() {
		return needTime - runedTime;
	}

	// 获取该PCB的到达时间
	public Time getArrivalTime() {
		return new Time(this.arrivalTime.toString());
	}

	// 获取该PCB的到达时间
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

	// 获取PCB已经运行的时间
	public int getRunedTime() {
		return runedTime;
	}

	//获取进程需要运行的时间
	public int getNeedTime() {
		// TODO Auto-generated method stub
		return this.needTime;
	}
	
	//获取进程的状态
	public Status getStatus(){
		return pcbStatus;
	}
	
	// 设置进程优先级
	public void setPriorityCode(int timeSlice) {
		priorityCode -= timeSlice;
	}

	// 显示PCB中包含的信息
	public void display() {
		System.out.println(pcbName + "\t" + priorityCode + "\t" + arrivalTime + "\t" + needTime + "\t" + runedTime
				+ "\t" + pcbStatus + "\t" + finishedTime.toString());
	}

	// 进程执行完毕，模拟释放进程资源
	public void killProcess() {
		this.pcbStatus = Status.KILLED;
	}

	@Override
	public int compareTo(PCB o) {
		// TODO Auto-generated method stub
		return this.priorityCode - o.getPriority();
	}


}
