package TicTacToeGame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

public class TicTacToeGame {

	private JFrame frame;

	// 状态矩阵中使用一下值做棋子标识，规定0为空位置
	private final int MAN = 1;
	private final int COMPUTER = 2;

	private ArrayList<JButton> chessList = new ArrayList<JButton>(); // 棋盘中存储的对象
	private int[][] matrix = new int[3][3];

	private Computer computer; // 计算机对象

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TicTacToeGame window = new TicTacToeGame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TicTacToeGame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("井字棋人机游戏(制作者：3117004568 黄钰竣)");
		frame.setBounds(100, 100, 727, 481);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton button_0 = new JButton();
		button_0.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_0, 0, 0);
			}
		});
		button_0.setBounds(280, 47, 117, 105);
		button_0.setEnabled(false);
		frame.getContentPane().add(button_0);
		chessList.add(button_0);

		JLabel label = new JLabel("\u68CB\u76D8\uFF1A");
		label.setFont(new Font("宋体", Font.BOLD, 18));
		label.setBounds(282, 16, 72, 18);
		frame.getContentPane().add(label);

		JLabel lblNewLabel = new JLabel("\u63A7\u5236\u9762\u677F");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 18));
		lblNewLabel.setBounds(44, 12, 105, 27);
		frame.getContentPane().add(lblNewLabel);

		JButton button_1 = new JButton();
		button_1.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_1.setBounds(416, 47, 117, 105);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_1, 0, 1);
			}
		});
		button_1.setEnabled(false);
		frame.getContentPane().add(button_1);
		chessList.add(button_1);

		JButton button_2 = new JButton();
		button_2.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_2.setBounds(550, 47, 117, 105);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_2, 0, 2);
			}
		});
		button_2.setEnabled(false);
		frame.getContentPane().add(button_2);
		chessList.add(button_2);

		JButton button_3 = new JButton();
		button_3.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_3.setBounds(279, 170, 117, 105);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_3, 1, 0);
			}
		});
		button_3.setEnabled(false);
		frame.getContentPane().add(button_3);
		chessList.add(button_3);

		JButton button_4 = new JButton();
		button_4.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_4.setBounds(417, 170, 117, 105);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_4, 1, 1);
			}
		});
		button_4.setEnabled(false);
		frame.getContentPane().add(button_4);
		chessList.add(button_4);

		JButton button_5 = new JButton();
		button_5.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_5.setBounds(550, 170, 117, 105);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_5, 1, 2);
			}
		});
		button_5.setEnabled(false);
		frame.getContentPane().add(button_5);
		chessList.add(button_5);

		JButton button_6 = new JButton();
		button_6.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_6.setBounds(279, 292, 117, 105);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_6, 2, 0);
			}
		});
		button_6.setEnabled(false);
		frame.getContentPane().add(button_6);
		chessList.add(button_6);

		JButton button_7 = new JButton();
		button_7.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_7.setBounds(419, 293, 117, 105);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_7, 2, 1);
			}
		});
		button_7.setEnabled(false);
		frame.getContentPane().add(button_7);
		chessList.add(button_7);

		JButton button_8 = new JButton();
		button_8.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 80));
		button_8.setBounds(551, 294, 117, 105);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealClick(button_8, 2, 2);
			}
		});
		button_8.setEnabled(false);
		frame.getContentPane().add(button_8);
		chessList.add(button_8);

		// 开始游戏按钮
		JButton button_start = new JButton("\u5F00\u59CB\u6E38\u620F");
		button_start.setBounds(32, 150, 133, 40);
		button_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (JButton jb : chessList) {
					jb.setEnabled(true);
				}
				computer = new Computer(MAN, COMPUTER, 2);
			}
		});
		frame.getContentPane().add(button_start);
		// 重新开始游戏按钮
		JButton button_restart = new JButton("\u91CD\u65B0\u5F00\u59CB");
		button_restart.setBounds(32, 215, 133, 40);
		button_restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (JButton jb : chessList) {
					jb.setText("");
					jb.setEnabled(true);
					jb.setBackground(null);
				}
				matrix = new int[3][3];
				computer = new Computer(MAN, COMPUTER, 2);
			}
		});
		frame.getContentPane().add(button_restart);
		// 退出游戏按钮
		JButton button_exit = new JButton("\u9000\u51FA\u6E38\u620F");
		button_exit.setBounds(32, 284, 133, 40);
		button_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				System.exit(0);
			}
		});
		frame.getContentPane().add(button_exit);

		JLabel lblNewLabel_1 = new JLabel("\u4EBA\u7C7B\u73A9\u5BB6\u7528\u68CB\uFF1A\u201C X \u201D");
		lblNewLabel_1.setBounds(32, 370, 170, 27);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblo = new JLabel("\u673A\u5668\u73A9\u5BB6\u7528\u68CB\uFF1A\u201C O \u201D");
		lblo.setBounds(32, 394, 170, 27);
		frame.getContentPane().add(lblo);
	}

	// 下棋的控制方法
	private void dealClick(JButton button, int row, int column) {
		//点击按钮的都是人，所以直接设置为人类的棋子
		button.setText("X");
		button.setBackground(Color.blue);
		matrix[row][column] = MAN;
		if(judge(row,column,MAN)==false){
			//接下来是机器人走的棋子
			int result = computer.getResult(matrix);
			JButton jb = chessList.get(result);
			jb.setText("O");
			jb.setBackground(Color.red);
			matrix[result/3][result%3] = COMPUTER;
			judge(result/3,result%3,COMPUTER);
		}
		
	}
	
	//判读棋局是否已经结束
	private boolean judge(int row,int column,int role){
		if(isNoWinner()){
			JOptionPane.showMessageDialog(frame, "你与机器人打成了平手！");
			return true;
		}else{
			if (isWin(row, column)) {
				if (role == MAN) {
					JOptionPane.showMessageDialog(frame, "恭喜你！你获胜了！");
					return true;
				} else {
					JOptionPane.showMessageDialog(frame, "很可惜！你失败了！");
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isWin(int row, int column) {
		int mark = matrix[row][column];
		return (matrix[row][0] == mark // 同一行
				&& matrix[row][1] == mark && matrix[row][2] == mark
				|| matrix[0][column] == mark // 同一列
						&& matrix[1][column] == mark && matrix[2][column] == mark
				|| row == column // 在正对角线上
						&& matrix[0][0] == mark && matrix[1][1] == mark && matrix[2][2] == mark
				|| row + column == 2 // 在反对角线上
						&& matrix[0][2] == mark && matrix[1][1] == mark && matrix[2][0] == mark);
	}
	
	private boolean isNoWinner(){
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(matrix[i][j] == 0)	return false;
			}
		}
		return true;
	}
}
