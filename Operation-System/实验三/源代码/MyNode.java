package OSLab3;

public class MyNode {
	public MyNode parent=null;
	private StoreArea data;
	public MyNode next=null;
	
	public MyNode(StoreArea data){
		this.data = data;
	}
	
	//��ȡ��ǰ�ڵ������
	public StoreArea getData(){
		return data;
	}
	
	//�����½ڵ㵽���ڵ����
	public void insert(MyNode node){
		node.next = this.next;
		if(this.next!=null){
			this.next.parent = node;
		}
		this.next = node;
		node.parent = this;
	}
	
	//ɾ�����ڵ㣬�����ر��ڵ������
	public StoreArea deleteNode(){
		parent.next = next;
		if(next==null){
			return data;
		}
		next.parent = parent;
		return data;
	}
	
}
