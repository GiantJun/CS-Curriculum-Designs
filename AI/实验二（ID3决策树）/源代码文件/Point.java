package AILab3;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

//用于在广度优先遍历时，记录下每个节点的信息
public class Point {
	private String attributeName;
	private DefaultMutableTreeNode node;
	private ArrayList<Integer> rowList;
	private ArrayList<Integer> columnList;
	private int index;
	
	public Point(String name,int index,DefaultMutableTreeNode node,ArrayList<Integer> rowList,ArrayList<Integer> columnList){
		this.attributeName = name;
		this.index = index;
		this.node = node;
		this.rowList = rowList;
		this.columnList = columnList;
	}
	
	public DefaultMutableTreeNode getNode(){
		return node;
	}
	
	public ArrayList<Integer> getRowList(){
		return rowList;
	}
	
	public ArrayList<Integer> getColumnList(){
		return columnList;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getName(){
		return attributeName;
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return node;
	}
}
