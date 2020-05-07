package TicTacToeGame;

import java.util.ArrayList;

public class Computer {
	//一下为状态矩阵的标识定义，必须与
	private final int MAN;
	private final int COMPUTER;
	private int depth;	//允许拓展的最大深度（最多看几步棋）
	
	private final int INF = 10000;	//设定为无穷大 
	
	public Computer(int Man,int Computer,int depth){
		this.MAN = Man;
		this.COMPUTER = Computer;
		this.depth = depth;
	}
	
	//获得COMPUTER的下棋结果，返回0-8的下棋位置
	public int getResult(int[][] matrix){
		int max = -INF;	//负无穷大
		int k;
		int pointX = 0,pointY = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();	//存储已经访问过的不等效落点
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(matrix[i][j] == 0){
					boolean isUsed = false;
					if(list.size() > 0){
						for(Integer integer : list){
							int tempX = integer /3;
							int tempY = integer % 3;
							if(isEqual(COMPUTER,i,j,tempX,tempY,matrix)){
								isUsed = true;
								break;
							}
						}
						if(isUsed)	continue;
					}
					//能运行到这，说明为未出现的不等效点
					list.add(new Integer(i*3+j));
					matrix[i][j] = COMPUTER;	//假设落子在此处
					//先判断是否为决胜棋
					if(isWin(matrix)){
						matrix[i][j] =  0;	//恢复原矩阵为原来的状态
						return i * 3 + j;
					}
					k = alphabeta(MAN,matrix,-INF,INF,1);	//从第一层开始往下递归
					matrix[i][j] =  0;	//恢复原矩阵为原来的状态
					if(k > max){
						max = k;
						pointX = i;
						pointY = j;
					}
				}
			}
		}
		return pointX * 3 + pointY;
	}
	
	//实现拓展至最大深度并使用alpha-beta剪枝法获取状态估值
	public int alphabeta(int who,int[][] matrix,int alpha,int beta,int depth){
		//已经达到最大深度，则返回本叶子节点的估价值
		if(depth == this.depth){
			return getScore(who,matrix);
		}
		
		int v;	//用于临时存储局部最大最小值
		ArrayList<Integer> list = new ArrayList<Integer>();	//存储已经访问过的不等效落点
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(matrix[i][j] == 0){
					//首先检查是否已经访问过这个位置的等效位置
					boolean isUsed = false;
					if(list.size() > 0){
						for(Integer integer : list){
							int tempX = integer /3;
							int tempY = integer % 3;
							if(isEqual(who,i,j,tempX,tempY,matrix)){
								isUsed = true;
								break;
							}
						}
						if(isUsed)	continue;
					}
					//能运行到这，说明为未出现的不等效点
					list.add(new Integer(i*3+j));
					/******* 以下均为未出现的情况 *****/
					matrix[i][j] = who;
					//轮到下一个玩家下，可能是计算机也可能是人
					if(who == COMPUTER){
						v = alphabeta(MAN,matrix,alpha,beta,depth+1);
					}else{
						v = alphabeta(COMPUTER,matrix,alpha,beta,depth+1);
					}
					matrix[i][j] = 0;	//恢复为初始值
					if(who == COMPUTER){
						alpha = Math.max(alpha, v);
					}else{
						beta = Math.min(beta, v);
					}
					//进行剪支操作，已经实现了最左侧底部节点的全部遍历
					if(beta <= alpha){
						if(who == COMPUTER){
							return alpha;
						}
						return beta;
					}
				}
			}
		}
		if(who == COMPUTER){
			return alpha;
		}
		return beta;
	}
	
	//判断两个落子位置是否等效
	public boolean isEqual(int who,int x1,int y1,int x2,int y2,int[][] matrix){
		//同层落子的位置相邻则为不等效
		if(x1==x2 && ((y1-y2)==1 || (y1-y2)==-1)){
			return false;
		}
		if(y1==y2 && ((x1-x2)==1 || (x1-x2)==-1)){
			return false;
		}
		//其他情况需对数组变换来分析
		//共有4中对称方式，分别是关于横轴对称、纵轴对称、正对角线对称、反对角线对称
		//满足任意一种对称就是等效的
		int[][] copy1 = new int[3][3];
		int[][] copy2 = new int[3][3];
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(i==x1 && j==y1){
					copy1[i][j] = who;
				}else if(i==x2 && j==y2){
					copy2[i][j] = who;
				}else{
					copy1[i][j] = matrix[i][j];
					copy2[i][j] = matrix[i][j];
				}
				
			}
		}
		//判断是否属于横轴对称
		if(copy1[0][0]==copy2[0][2] && copy1[0][1]==copy2[0][1] && copy1[0][2]==copy2[0][0]	//第一行
				&& copy1[1][0]==copy2[1][2] && copy1[1][1]==copy2[1][1] && copy1[1][2]==copy2[1][0]	//第二行
				&& copy1[2][0]==copy2[2][2] && copy1[2][1]==copy2[2][1] && copy1[2][2]==copy2[2][0]){	//第三行
			return true;
		}
		//判断是否属于纵轴对称
		if(copy1[0][0]==copy2[2][0] && copy1[1][0]==copy2[1][0] && copy1[2][0]==copy2[0][0]	//第一列
				&& copy1[0][1]==copy2[2][1] && copy1[1][1]==copy2[1][1] && copy1[2][1]==copy2[0][1]	//第二列
				&& copy1[0][2]==copy2[2][2] && copy1[1][2]==copy2[1][2] && copy1[2][2]==copy2[0][2]){	//第三列
			return true;
		}
		//判断是否属于正对角线对称
		boolean flag = true;	//假设是正对角线对称
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(copy1[i][j] != copy2[j][i]){
					//出现相异的情况，说明不是
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
		}
		if(flag == true)	return true;
		//判断是否属于反对角线对称
		flag = true;	//假设是反对角线对称
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(copy1[i][j] != copy2[2-j][2-i]){
					//出现相异的情况，说明不是
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
		}
		if(flag == true)	return true;
		return false;
	}
	
	
	//为估价函数，等于（所有空格填上机器人棋子后三点成线总数）-（填上人类棋子后三点成线的总数）
	public int getScore(int who,int[][] matrix){
		if(isWin(matrix)){
			if(who == MAN) return INF;
			else	return (-1) * INF;
		}
		
		int cResult = 0;
		int mResult = 0;
		int[][] tempC = new int[3][3];	//填充COMPUTER棋子后的矩阵
		int[][] tempM = new int[3][3];	//填充MAN棋子后的矩阵
		//在复制的矩阵中，将原状态矩阵中的空格全部填上COMPUTER的棋子
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(matrix[i][j] == 0){
					tempC[i][j] = COMPUTER;
					tempM[i][j] = MAN;
				}else{
					tempC[i][j] = matrix[i][j];
					tempM[i][j] = matrix[i][j];
				}
			}
		}
		//检查行和列
		for(int i=0; i<3; i++){
			//检查行
			if(tempC[i][0]==COMPUTER && tempC[i][1]==COMPUTER && tempC[i][2]==COMPUTER){
				cResult++;
			}
			if(tempM[i][0]==MAN && tempM[i][1]==MAN && tempM[i][2]==MAN){
				mResult++;
			}
			//检查列
			if(tempC[0][i]==COMPUTER && tempC[1][i]==COMPUTER && tempC[2][i]==COMPUTER){
				cResult++;
			}
			if(tempM[0][i]==MAN && tempM[1][i]==MAN && tempM[2][i]==MAN){
				mResult++;
			}
		}
		//检查正对角线
		if(tempC[0][0]==COMPUTER && tempC[1][1]==COMPUTER && tempC[2][2]==COMPUTER){
			cResult++;
		}
		if(tempM[0][0]==MAN && tempM[1][1]==MAN && tempM[2][2]==MAN){
			mResult++;
		}
		//检查反对角线
		if(tempC[0][2]==COMPUTER && tempC[1][1]==COMPUTER && tempC[2][0]==COMPUTER){
			cResult++;
		}
		if(tempM[0][2]==MAN && tempM[1][1]==MAN && tempM[2][0]==MAN){
			mResult++;
		}
		
		return cResult - mResult;
	}
	
	private boolean isWin(int[][] matrix) {
		for(int i=0; i<3; i++){
			//行一子
			if(matrix[i][0] == MAN && matrix[i][1] == MAN && matrix[i][2] == MAN){
				return true;
			}
			if(matrix[i][0] == COMPUTER && matrix[i][1] == COMPUTER && matrix[i][2] == COMPUTER){
				return true;
			}
			//列一字
			if(matrix[0][i] == MAN && matrix[1][i] == MAN && matrix[2][i] == MAN){
				return true;
			}
			if(matrix[0][i] == COMPUTER && matrix[1][i] == COMPUTER && matrix[2][i] == COMPUTER){
				return true;
			}
		}
		//正对角线一字
		if(matrix[0][0] == MAN && matrix[1][1] == MAN && matrix[2][2] == MAN){
			return true;
		}
		if(matrix[0][0] == COMPUTER && matrix[1][1] == COMPUTER && matrix[2][2] == COMPUTER){
			return true;
		}
		//反对角线一字
		if(matrix[0][2] == MAN && matrix[1][1] == MAN && matrix[2][0] == MAN){
			return true;
		}
		if(matrix[0][2] == COMPUTER && matrix[1][1] == COMPUTER && matrix[2][0] == COMPUTER){
			return true;
		}
		return false;
	}
}
