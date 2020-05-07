package AILab3;

import java.util.ArrayList;

public class MyList2ColumnList {
private ArrayList<ColumnList> list = new ArrayList<ColumnList>();
	
	public void add(ColumnList a){
		list.add(a);
	}
	
	public boolean isExist(String name){
		for(ColumnList temp : list){
			if(temp.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public ColumnList getByName(String name){
		for(ColumnList temp: list){
			if(temp.getName().equals(name)){
				return temp;
			}
		}
		return null;
	}
	
	public ArrayList<ColumnList> getList(){
		return list;
	}
	
}
