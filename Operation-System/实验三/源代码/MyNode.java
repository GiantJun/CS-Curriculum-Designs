package OSLab3;

public class MyNode {
	public MyNode parent=null;
	private StoreArea data;
	public MyNode next=null;
	
	public MyNode(StoreArea data){
		this.data = data;
	}
	
	//获取当前节点的数据
	public StoreArea getData(){
		return data;
	}
	
	//插入新节点到本节点后面
	public void insert(MyNode node){
		node.next = this.next;
		if(this.next!=null){
			this.next.parent = node;
		}
		this.next = node;
		node.parent = this;
	}
	
	//删除本节点，并返回本节点的数据
	public StoreArea deleteNode(){
		parent.next = next;
		if(next==null){
			return data;
		}
		next.parent = parent;
		return data;
	}
	
}
