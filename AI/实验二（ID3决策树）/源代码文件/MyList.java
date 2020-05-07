package AILab3;

import java.util.ArrayList;

//用于存储属性下，各子属性的正反例个数，可计算分量
public class MyList {
	private ArrayList<Attribute> list = new ArrayList<Attribute>();
	
	public void add(Attribute a){
		list.add(a);
	}
	
	public boolean isExist(String name){
		for(Attribute temp : list){
			if(temp.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public Attribute getByName(String name){
		for(Attribute temp: list){
			if(temp.getName().equals(name)){
				return temp;
			}
		}
		return null;
	}
	
	public double sumEntropy(){
		double result = 0.0;
		for(Attribute temp : list){
			result += temp.getEntropy();
		}
		return result;
	}
}
