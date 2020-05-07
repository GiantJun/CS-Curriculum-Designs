package giantJun;

public class Time implements Comparable<Time> {
	private int hour;
	private int minue;

	public Time(String timeString) {
		String[] timeSplit = timeString.split(":");
		this.hour = Integer.parseInt(timeSplit[0]);
		this.minue = Integer.parseInt(timeSplit[1]);
	}

	// 根据传入的时间增加值，更新小时和分钟
	public void increase(int timeSlice) {
		if (minue + timeSlice >= 60) {
			hour += (minue + timeSlice) / 60;
			minue = (minue + timeSlice) % 60;
		} else {
			minue += timeSlice;
		}
	}

	// 打印存储的小时、分钟
	public String toString() {
		return hour + ":" + minue;
	}

	// 获取分钟数
	public int getMinue() {
		return this.minue;
	}

	// 获取小时数
	public int getHour() {
		return this.hour;
	}

	@Override
	public int compareTo(Time arg0) {
		// TODO Auto-generated method stub
		int m1 = this.hour * 60 + minue;
		int m2 = arg0.hour * 60 + arg0.getMinue();
		;
		return m1 - m2;
	}
}
