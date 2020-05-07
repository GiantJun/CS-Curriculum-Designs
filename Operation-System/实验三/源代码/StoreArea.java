package OSLab3;


public class StoreArea {
	private String name;
	private int storeSize;
	private Status status = Status.EMPTY;
	private int smallestSize;
	private int startIndex;
	
	public StoreArea(String name,int storeSize,int startIndex,int smallestSize){
		this.name = name;
		this.storeSize = storeSize;
		this.startIndex = startIndex;
		this.smallestSize = smallestSize;
	}
	
	//��ȡ������
	public String getName(){
		return name;
	}
	
	//��ȡ������С
	public int getSize(){
		return storeSize;
	}
	
	//��ȡ������ʼַ
	public int getStartIndex(){
		return startIndex;
	}
	
	//��ȡ������״̬
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status s){
		status = s;
	}
	
	//�����ڴ�,�ڿ��з����ĵ�λ����һ��Ҫ���С�ķ�����Ϊ����ֵ���������ֺ󱾷����Ĳ���
	public StoreArea splitArea(String newName,int size){
		if(storeSize - size < smallestSize ){
			return null;
		}
		StoreArea result = new StoreArea(newName,size,startIndex,this.smallestSize);
		result.setStatus(Status.FULL);
		this.storeSize -= size;
		startIndex += size;
		return result;
	}
	
	//�����ж��Ƿ���������СҪ��
	public boolean isFit(int size){
		if(storeSize - size < smallestSize){
			return false;
		}
		return true;
	}
	
	//���ڱȽ����������Ŀ��пռ��С
	public boolean isBiger(StoreArea area){
		if(storeSize>area.getSize()){
			return true;
		}
		return false;
	}
	
	public void merge(StoreArea area){
		this.storeSize += area.storeSize;
	}
}
