package AILab3;

public class Attribute {
	String name = null;
	int yesValue=0;
	int noValue=0;
	
	public Attribute(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void increaseYesValue(){
		this.yesValue++;
	}
	
	public void increaseNoValue(){
		this.noValue++;
	}
	
	public double getEntropy(){
		if(yesValue==0 || noValue==0){
			return 0;
		}
		return yesValue*Math.log10(Double.valueOf(yesValue)/Double.valueOf(yesValue+noValue)) + 
				noValue*Math.log10(Double.valueOf(noValue)/Double.valueOf(yesValue+noValue));
	}
}
