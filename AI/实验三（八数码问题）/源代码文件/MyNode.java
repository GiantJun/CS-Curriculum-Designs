
public class MyNode implements Comparable<MyNode> {

	private int[][] startData = null; // ��ʼ����
	private int[][] endData = null; // Ŀ������
	private int[][] data = null; // Ŀǰ������
	private int emptyX = 0; // �ո��������
	private int emptyY = 0; // �ո��������
	private int matrixSize = 0; // �������������������������������

	private MyNode parent = null; // ���ڵ�ĸ��׽ڵ㣨��Ҫ������ý�·����

	private int gValue = 0; // ��ɢ����ֵ����������ʼ�ڵ�ľ��루���ո��ƶ��Ĵ������൱��������ȣ�
	private int hValue = 0; // ��������ֵ��������Ŀ��ڵ�ľ���(����Ŀ��Ĳ������ָ���)
	private int f = 0; // A*����ֵ���� gֵ��hֵ�ĺ�

	public MyNode(int[][] startNode, int[][] endNode, int[][] data) {
		this.matrixSize = data.length;
		this.startData = startNode;
		this.endData = endNode;
		this.data = new int[this.matrixSize][this.matrixSize];
		// ����Ϊ�����˳���ȡ�ո�
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

	// ͼ�����㣬���ڸ��ƽڵ�
	public MyNode(MyNode node) {
		this(node.startData, node.endData, node.data);
	}

	// �����������ֵh
	public int getHVelue() {
		return this.hValue;
	}

	// ���hֵ
	public int getH() {
		return this.hValue;
	}

	// ���fֵ
	public int getF() {
		return this.f;
	}

	// ��ø��׽ڵ�
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
	
	// �����ǰ������
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

	// ����gֵ��hֵ��fֵ
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

	// �жϿո��ܹ�����������ƶ�
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

	// �ƶ��ո񣬷��ر任����¶���
	public MyNode emptyMove(Directions d) {
		// �ǳ��ؼ���һ�������򽫻ᵼ���޷�����ȫ�µĸ��ƶ��󣨼�֮�Ǹ��������������
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
