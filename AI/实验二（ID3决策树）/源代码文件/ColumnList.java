package AILab3;

import java.util.ArrayList;

//���ڱ��������£��䲻ͬ�����Ե�����ֵ������
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
