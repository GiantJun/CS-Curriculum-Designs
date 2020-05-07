package uiPackage;

import javax.swing.JProgressBar;

/** * @author  作者 :GiantJun 
* @date 创建时间：2019年7月1日 下午7:11:31 
* @version 1.0 
* <p>Description: </p>   
* @throws 
*/
public class UpdateThread implements Runnable{
	
	private JProgressBar progress;
	private int value = 0;
	
	public UpdateThread(JProgressBar progress) {
		this.progress = progress;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
				if(value >= 100) {
					progress.setValue(value);
					break;
				}
				progress.setValue(value);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}
