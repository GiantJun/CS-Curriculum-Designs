package AILab3;

import java.util.ArrayList;

//���ڴ洢�����£��������Ե��������������ɼ������
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
