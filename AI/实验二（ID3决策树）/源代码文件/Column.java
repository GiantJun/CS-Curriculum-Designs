package AILab3;

public class Column {
	private String name;
	private double value;
	
	public Column(String name, double value){
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public double getValue(){
		return value;
	}
}
