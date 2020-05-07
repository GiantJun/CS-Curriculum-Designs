package clients;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/** * @author  作者 :GientJun 
* @date 创建时间：2018年12月4日 下午4:04:36 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class HistoryWindow extends JFrame{

//	private JFrame frame;
	private String history = "还没有聊天记录！";
	private String friendName = null;
	private Controler controler = null;
	private JTextArea textArea = null;
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					HistoryWindow window = new HistoryWindow();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public HistoryWindow(String history,String friendName,Controler controler) {
		if(!history.equals(null) && !history.equals("")) {
			this.history = history;
		}
		this.controler = controler;
		this.friendName = friendName;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
		this.getContentPane().setFont(new Font("Adobe Garamond Pro Bold", Font.PLAIN, 17));
		this.setBounds(100, 100, 509, 444);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("聊天记录查看窗口");
		this.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(0, 0, 356, 397);
		textArea.setFont(new Font("宋体", Font.PLAIN, 20));
		this.getContentPane().add(textArea);
		textArea.setText(history);
		
		JLabel label = new JLabel("\u804A\u5929\u8BB0\u5F55\u7F16\u8F91");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 16));
		label.setBounds(370, 13, 107, 18);
		this.getContentPane().add(label);
		
		JButton button = new JButton("\u6E05\u7A7A\u8BB0\u5F55");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					controler.deleteHistory(friendName);
					textArea.setText("");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("清空聊天记录错误！");
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(370, 56, 113, 27);
		this.getContentPane().add(button);
		
		this.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  
		        }
		      public void windowClosed(WindowEvent e) {
		    	  
		        }
		      });
	}
}
