import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] originalData = { { 2, 8, 3 }, { 1, 6, 4 }, { 7, 0, 5 } };
		int[][] endData = { { 1, 2, 3 }, { 8, 0, 4 }, { 7, 6, 5 } };

		MyNode node = new MyNode(originalData, endData, originalData);
//		MyNode node1 = new MyNode(originalData, originalData, originalData);
//		
//
//		ArrayList<MyNode> list = new ArrayList<MyNode>();
//		list.add(node1);
//		list.add(node);
//		for(MyNode n : list) {
//			System.out.println(n.getF());
//		}
//		Collections.sort(list);
//		System.out.println();
//		for(MyNode n : list) {
//			System.out.println(n.getF());
//		}
		run(node);
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
	}

	public static void run(MyNode node) {
		ArrayList<MyNode> openList = new ArrayList<MyNode>();
		ArrayList<MyNode> closeList = new ArrayList<MyNode>();
		ArrayList<MyNode> resultList = new ArrayList<MyNode>();
		
		//一直运行，直至变换成目标节点
		while (node.getHVelue() != 0) {
			MyNode temp;
			if (node.isMovable(Directions.DOWN)) {
				temp = node.emptyMove(Directions.DOWN);
				if(!isComtain(openList,closeList,temp)) {
					openList.add(temp);
				}
			}
			if (node.isMovable(Directions.UP)) {
				temp = node.emptyMove(Directions.UP);
				if(!isComtain(openList,closeList,temp)) {
					openList.add(temp);
				}
			}
			if (node.isMovable(Directions.LEFT)) {
				temp = node.emptyMove(Directions.LEFT);
				if(!isComtain(openList,closeList,temp)) {
					openList.add(temp);
				}
			}
			if (node.isMovable(Directions.RIGHT)) {
				temp = node.emptyMove(Directions.RIGHT);
				if(!isComtain(openList,closeList,temp)) {
					openList.add(temp);
				}
			}
			//完成本节点的一次拓展，存入close表中
			closeList.add(node);
			//对open
			Collections.sort(openList);
			node = openList.get(0);
			openList.remove(0);
		}
		
		while(node.getParent() != null) {
			resultList.add(node);
			node = node.getParent();
		}
		resultList.add(node);
		System.out.println("----开始输出变换路径----");
		for(int i = resultList.size()-1; i>-1; i--) {
			resultList.get(i).printNode();
			System.out.println();
		}
		System.out.println("----结束输出变换路径----");
	}
	
	public static boolean isComtain(ArrayList<MyNode> openList,ArrayList<MyNode> closeList,MyNode node) {
//		if(openList.size() == 0 && closeList.size() == 0) {
//			return false;
//		}
		for(MyNode n : openList) {
			if(n.isEqual(node)) {
				return true;
			}
		}
		for(MyNode n : closeList) {
			if(n.isEqual(node)) {
				return true;
			}
		}
		return false;
	}
}
