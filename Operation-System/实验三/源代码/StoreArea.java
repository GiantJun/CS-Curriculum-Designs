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
	
	//获取分区名
	public String getName(){
		return name;
	}
	
	//获取分区大小
	public int getSize(){
		return storeSize;
	}
	
	//获取分区的始址
	public int getStartIndex(){
		return startIndex;
	}
	
	//获取分区的状态
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status s){
		status = s;
	}
	
	//分配内存,在空闲分区的低位划分一个要求大小的分区作为返回值，修正划分后本分区的参数
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
	
	//用于判断是否满足分配大小要求
	public boolean isFit(int size){
		if(storeSize - size < smallestSize){
			return false;
		}
		return true;
	}
	
	//用于比较两个分区的空闲空间大小
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
