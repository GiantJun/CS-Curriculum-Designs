package toolPackage;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/** * @author  作者 :GiantJun 
* @date 创建时间：2019年7月1日 上午9:48:39 
* @version 1.0 
* <p>Description: </p>   
* @throws 
*/
public class MySelector {
	private volatile boolean mark = false;
	private final Selector selector;
	
	public MySelector() throws IOException {
		this.selector = Selector.open();
	}
	
	public Set<SelectionKey> selectedKeys(){
		return selector.selectedKeys();
	}
	
	public synchronized SelectionKey reg(SelectableChannel channel,int op) throws ClosedChannelException{
		mark = true;
		selector.wakeup();
		SelectionKey result = channel.register(selector, op);
		mark = false;
		return result;
	}
	
	public int select() throws IOException{
		while(true) {
			if(mark == true)
				continue;
			int result = selector.select();
			return result;
		}
	}
}
