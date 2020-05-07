package OSDesign;

public class JCB implements Comparable<JCB> {
	private String jcbName = null; // 控制块名
	private Time arrivalTime = null; // 到达时间
	private int needTime = 0; // 需要运行的时间
	private Status jcbStatus = Status.WAIT; // 进程所处的状态
	private int priorityCode = 100; // 优先级数

	public JCB(String jcbName, Time arrivalTime, int needTime, int priorityCode) {
		this.jcbName = jcbName;
		this.arrivalTime = arrivalTime;
		this.needTime = needTime;
		this.priorityCode = priorityCode;
	}

	// 获取JCB的名字
	public String getName() {
		return this.jcbName;
	}

	// 获取JCB的到达时间
	public Time getArrivalTime() {
		return new Time(this.arrivalTime.toString());
	}

	// 获取JCB需要运行的时间
	public int getNeedTime() {
		return this.needTime;
	}

	// 获取JCB的优先级数
	public int getPriority() {
		return this.priorityCode;
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

	// 显示PCB中包含的信息
	public void display() {
		System.out.println(jcbName + "\t" + priorityCode + "\t" + arrivalTime + "\t" + needTime + "\t" + jcbStatus);
	}

	// 依据优先级数排序
	@Override
	public int compareTo(JCB o) {
		// TODO Auto-generated method stub
		return this.arrivalTime.compareTo(o.getArrivalTime());
	}
}
