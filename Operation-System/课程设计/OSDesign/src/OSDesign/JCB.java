package OSDesign;

public class JCB implements Comparable<JCB> {
	private String jcbName = null; // ���ƿ���
	private Time arrivalTime = null; // ����ʱ��
	private int needTime = 0; // ��Ҫ���е�ʱ��
	private Status jcbStatus = Status.WAIT; // ����������״̬
	private int priorityCode = 100; // ���ȼ���

	public JCB(String jcbName, Time arrivalTime, int needTime, int priorityCode) {
		this.jcbName = jcbName;
		this.arrivalTime = arrivalTime;
		this.needTime = needTime;
		this.priorityCode = priorityCode;
	}

	// ��ȡJCB������
	public String getName() {
		return this.jcbName;
	}

	// ��ȡJCB�ĵ���ʱ��
	public Time getArrivalTime() {
		return new Time(this.arrivalTime.toString());
	}

	// ��ȡJCB��Ҫ���е�ʱ��
	public int getNeedTime() {
		return this.needTime;
	}

	// ��ȡJCB�����ȼ���
	public int getPriority() {
		return this.priorityCode;
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

	// ��ʾPCB�а�������Ϣ
	public void display() {
		System.out.println(jcbName + "\t" + priorityCode + "\t" + arrivalTime + "\t" + needTime + "\t" + jcbStatus);
	}

	// �������ȼ�������
	@Override
	public int compareTo(JCB o) {
		// TODO Auto-generated method stub
		return this.arrivalTime.compareTo(o.getArrivalTime());
	}
}
