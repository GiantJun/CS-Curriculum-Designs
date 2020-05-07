package TicTacToeGame;

import java.util.ArrayList;

public class Computer {
	//һ��Ϊ״̬����ı�ʶ���壬������
	private final int MAN;
	private final int COMPUTER;
	private int depth;	//������չ�������ȣ���࿴�����壩
	
	private final int INF = 10000;	//�趨Ϊ����� 
	
	public Computer(int Man,int Computer,int depth){
		this.MAN = Man;
		this.COMPUTER = Computer;
		this.depth = depth;
	}
	
	//���COMPUTER��������������0-8������λ��
	public int getResult(int[][] matrix){
		int max = -INF;	//�������
		int k;
		int pointX = 0,pointY = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();	//�洢�Ѿ����ʹ��Ĳ���Ч���
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
					//�����е��⣬˵��Ϊδ���ֵĲ���Ч��
					list.add(new Integer(i*3+j));
					matrix[i][j] = COMPUTER;	//���������ڴ˴�
					//���ж��Ƿ�Ϊ��ʤ��
					if(isWin(matrix)){
						matrix[i][j] =  0;	//�ָ�ԭ����Ϊԭ����״̬
						return i * 3 + j;
					}
					k = alphabeta(MAN,matrix,-INF,INF,1);	//�ӵ�һ�㿪ʼ���µݹ�
					matrix[i][j] =  0;	//�ָ�ԭ����Ϊԭ����״̬
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
	
	//ʵ����չ�������Ȳ�ʹ��alpha-beta��֦����ȡ״̬��ֵ
	public int alphabeta(int who,int[][] matrix,int alpha,int beta,int depth){
		//�Ѿ��ﵽ�����ȣ��򷵻ر�Ҷ�ӽڵ�Ĺ���ֵ
		if(depth == this.depth){
			return getScore(who,matrix);
		}
		
		int v;	//������ʱ�洢�ֲ������Сֵ
		ArrayList<Integer> list = new ArrayList<Integer>();	//�洢�Ѿ����ʹ��Ĳ���Ч���
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(matrix[i][j] == 0){
					//���ȼ���Ƿ��Ѿ����ʹ����λ�õĵ�Чλ��
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
					//�����е��⣬˵��Ϊδ���ֵĲ���Ч��
					list.add(new Integer(i*3+j));
					/******* ���¾�Ϊδ���ֵ���� *****/
					matrix[i][j] = who;
					//�ֵ���һ������£������Ǽ����Ҳ��������
					if(who == COMPUTER){
						v = alphabeta(MAN,matrix,alpha,beta,depth+1);
					}else{
						v = alphabeta(COMPUTER,matrix,alpha,beta,depth+1);
					}
					matrix[i][j] = 0;	//�ָ�Ϊ��ʼֵ
					if(who == COMPUTER){
						alpha = Math.max(alpha, v);
					}else{
						beta = Math.min(beta, v);
					}
					//���м�֧�������Ѿ�ʵ���������ײ��ڵ��ȫ������
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
	
	//�ж���������λ���Ƿ��Ч
	public boolean isEqual(int who,int x1,int y1,int x2,int y2,int[][] matrix){
		//ͬ�����ӵ�λ��������Ϊ����Ч
		if(x1==x2 && ((y1-y2)==1 || (y1-y2)==-1)){
			return false;
		}
		if(y1==y2 && ((x1-x2)==1 || (x1-x2)==-1)){
			return false;
		}
		//��������������任������
		//����4�жԳƷ�ʽ���ֱ��ǹ��ں���Գơ�����Գơ����Խ��߶Գơ����Խ��߶Գ�
		//��������һ�ֶԳƾ��ǵ�Ч��
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
		//�ж��Ƿ����ں���Գ�
		if(copy1[0][0]==copy2[0][2] && copy1[0][1]==copy2[0][1] && copy1[0][2]==copy2[0][0]	//��һ��
				&& copy1[1][0]==copy2[1][2] && copy1[1][1]==copy2[1][1] && copy1[1][2]==copy2[1][0]	//�ڶ���
				&& copy1[2][0]==copy2[2][2] && copy1[2][1]==copy2[2][1] && copy1[2][2]==copy2[2][0]){	//������
			return true;
		}
		//�ж��Ƿ���������Գ�
		if(copy1[0][0]==copy2[2][0] && copy1[1][0]==copy2[1][0] && copy1[2][0]==copy2[0][0]	//��һ��
				&& copy1[0][1]==copy2[2][1] && copy1[1][1]==copy2[1][1] && copy1[2][1]==copy2[0][1]	//�ڶ���
				&& copy1[0][2]==copy2[2][2] && copy1[1][2]==copy2[1][2] && copy1[2][2]==copy2[0][2]){	//������
			return true;
		}
		//�ж��Ƿ��������Խ��߶Գ�
		boolean flag = true;	//���������Խ��߶Գ�
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(copy1[i][j] != copy2[j][i]){
					//��������������˵������
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
		}
		if(flag == true)	return true;
		//�ж��Ƿ����ڷ��Խ��߶Գ�
		flag = true;	//�����Ƿ��Խ��߶Գ�
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(copy1[i][j] != copy2[2-j][2-i]){
					//��������������˵������
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
	
	
	//Ϊ���ۺ��������ڣ����пո����ϻ��������Ӻ��������������-�������������Ӻ�������ߵ�������
	public int getScore(int who,int[][] matrix){
		if(isWin(matrix)){
			if(who == MAN) return INF;
			else	return (-1) * INF;
		}
		
		int cResult = 0;
		int mResult = 0;
		int[][] tempC = new int[3][3];	//���COMPUTER���Ӻ�ľ���
		int[][] tempM = new int[3][3];	//���MAN���Ӻ�ľ���
		//�ڸ��Ƶľ����У���ԭ״̬�����еĿո�ȫ������COMPUTER������
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
		//����к���
		for(int i=0; i<3; i++){
			//�����
			if(tempC[i][0]==COMPUTER && tempC[i][1]==COMPUTER && tempC[i][2]==COMPUTER){
				cResult++;
			}
			if(tempM[i][0]==MAN && tempM[i][1]==MAN && tempM[i][2]==MAN){
				mResult++;
			}
			//�����
			if(tempC[0][i]==COMPUTER && tempC[1][i]==COMPUTER && tempC[2][i]==COMPUTER){
				cResult++;
			}
			if(tempM[0][i]==MAN && tempM[1][i]==MAN && tempM[2][i]==MAN){
				mResult++;
			}
		}
		//������Խ���
		if(tempC[0][0]==COMPUTER && tempC[1][1]==COMPUTER && tempC[2][2]==COMPUTER){
			cResult++;
		}
		if(tempM[0][0]==MAN && tempM[1][1]==MAN && tempM[2][2]==MAN){
			mResult++;
		}
		//��鷴�Խ���
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
			//��һ��
			if(matrix[i][0] == MAN && matrix[i][1] == MAN && matrix[i][2] == MAN){
				return true;
			}
			if(matrix[i][0] == COMPUTER && matrix[i][1] == COMPUTER && matrix[i][2] == COMPUTER){
				return true;
			}
			//��һ��
			if(matrix[0][i] == MAN && matrix[1][i] == MAN && matrix[2][i] == MAN){
				return true;
			}
			if(matrix[0][i] == COMPUTER && matrix[1][i] == COMPUTER && matrix[2][i] == COMPUTER){
				return true;
			}
		}
		//���Խ���һ��
		if(matrix[0][0] == MAN && matrix[1][1] == MAN && matrix[2][2] == MAN){
			return true;
		}
		if(matrix[0][0] == COMPUTER && matrix[1][1] == COMPUTER && matrix[2][2] == COMPUTER){
			return true;
		}
		//���Խ���һ��
		if(matrix[0][2] == MAN && matrix[1][1] == MAN && matrix[2][0] == MAN){
			return true;
		}
		if(matrix[0][2] == COMPUTER && matrix[1][1] == COMPUTER && matrix[2][0] == COMPUTER){
			return true;
		}
		return false;
	}
}
