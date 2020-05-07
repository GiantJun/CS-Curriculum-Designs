
public class MyNode implements Comparable<MyNode> {

	private int[][] startData = null; // 起始数组
	private int[][] endData = null; // 目标数组
	private int[][] data = null; // 目前的数组
	private int emptyX = 0; // 空格的行坐标
	private int emptyY = 0; // 空格的列坐标
	private int matrixSize = 0; // 数组的行数和列数（行数等于列数）

	private MyNode parent = null; // 本节点的父亲节点（主要用作获得解路径）

	private int gValue = 0; // 耗散函数值，采用与起始节点的距离（即空格移动的次数，相当于树的深度）
	private int hValue = 0; // 启发函数值，采用与目标节点的距离(即与目标的差异数字个数)
	private int f = 0; // A*函数值，即 g值与h值的和

	public MyNode(int[][] startNode, int[][] endNode, int[][] data) {
		this.matrixSize = data.length;
		this.startData = startNode;
		this.endData = endNode;
		this.data = new int[this.matrixSize][this.matrixSize];
		// 以下为深拷贝，顺便获取空格
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {
				if (data[i][j] == 0) {
					emptyX = i;
					emptyY = j;
				}
				this.data[i][j] = data[i][j];
			}

		}
		calculate();
	}

	// 图个方便，用于复制节点
	public MyNode(MyNode node) {
		this(node.startData, node.endData, node.data);
	}

	// 获得启发函数值h
	public int getHVelue() {
		return this.hValue;
	}

	// 获得h值
	public int getH() {
		return this.hValue;
	}

	// 获得f值
	public int getF() {
		return this.f;
	}

	// 获得父亲节点
	public MyNode getParent() {
		return this.parent;
	}

	public boolean isEqual(MyNode node) {
		for(int i=0; i<this.matrixSize; i++) {
			for(int j=0; j<this.matrixSize; j++)
				if(this.data[i][j] != node.data[i][j]) {
					return false;
				}
		}
		return true;
	}
	
	// 输出当前的数组
	public void printNode() {
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				if (data[i][j] == 0) {
					System.out.print(" " + "  ");
				} else {
					System.out.print(data[i][j] + "  ");
				}
			}
			System.out.println();
		}
	}

	public void setStartNode(int[][] startData) {
		this.startData = startData;
	}

	public void setEndNode(int[][] endData) {
		this.endData = endData;
	}

	// 计算g值、h值和f值
	private void calculate() {
		int hResult = 0;
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				if (data[i][j] != endData[i][j]) {
					hResult++;
				}
			}
		}
		hValue = hResult;
		if (parent != null) {
			gValue = parent.gValue + 1;
		}
		f = hValue + gValue;
	}

	// 判断空格能够向这个方向移动
	public boolean isMovable(Directions d) {
		switch (d) {
		case UP:
			if (emptyX - 1 < 0) {
				return false;
			}
			break;
		case DOWN:
			if (emptyX + 1 >= matrixSize) {
				return false;
			}
			break;
		case LEFT:
			if (emptyY - 1 < 0) {
				return false;
			}
			break;
		case RIGHT:
			if (emptyY + 1 >= matrixSize) {
				return false;
			}
			break;
		}
		return true;
	}

	// 移动空格，返回变换后的新对象
	public MyNode emptyMove(Directions d) {
		// 非常关键的一步，否则将会导致无法生成全新的复制对象（即之是复制数组的索引）
		MyNode result = new MyNode(this.startData, this.endData, this.data);
		switch (d) {
		case UP:
			result.data[result.emptyX][result.emptyY] = result.data[result.emptyX - 1][result.emptyY];
			result.data[result.emptyX - 1][result.emptyY] = 0;
			--(result.emptyX);
			break;
		case DOWN:
			result.data[result.emptyX][result.emptyY] = result.data[result.emptyX + 1][result.emptyY];
			result.data[result.emptyX + 1][result.emptyY] = 0;
			++(result.emptyX);
			break;
		case LEFT:
			result.data[result.emptyX][result.emptyY] = result.data[result.emptyX][result.emptyY - 1];
			result.data[result.emptyX][result.emptyY - 1] = 0;
			--(result.emptyY);
			break;
		case RIGHT:
			result.data[result.emptyX][result.emptyY] = result.data[result.emptyX][result.emptyY + 1];
			result.data[result.emptyX][result.emptyY + 1] = 0;
			++(result.emptyY);
			break;
		}
		result.calculate();
		result.parent = this;
		return result;
	}

	@Override
	public int compareTo(MyNode arg0) {
		// TODO Auto-generated method stub
		return this.f - arg0.f;
	}

}
