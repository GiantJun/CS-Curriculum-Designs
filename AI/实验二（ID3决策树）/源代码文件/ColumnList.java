package AILab3;

import java.util.ArrayList;

//用于保存属性下，其不同子属性的属性值和索引
public class ColumnList {
	private String name;
	private ArrayList<Integer> list;
	
	public ColumnList(String name){
		this.name = name;
		this.list = new ArrayList<Integer>();
	}
	
	public String getName(){
		return name;
	}
	
	public void addIndex(int index){
		list.add(index);
	}
	
	public ArrayList<Integer> getList(){
		return list;
	}
}
