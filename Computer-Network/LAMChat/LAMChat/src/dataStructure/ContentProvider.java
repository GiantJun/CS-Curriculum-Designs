package dataStructure;

import java.nio.channels.SocketChannel;

/** * @author  作者 :GiantJun 
* @date 创建时间：2019年6月28日 下午3:39:22 
* @version 1.0 
* <p>Description: </p>   
* @throws 
*/
public class ContentProvider {
	
	//连接在线表
	private OnlineMap<String,SocketChannel> connectionMap = null;
	private OnlineMap<String,User> userMap = null;
	//自己的个人信息
	public User mySelf = null;
	
	public ContentProvider(User user) {
		mySelf = user;
		connectionMap = new OnlineMap<>();
		userMap = new OnlineMap<>();
	}
		
	public synchronized OnlineMap<String, User> getUserMap() {
		return userMap;
	}
	
	public synchronized OnlineMap<String, SocketChannel> getConnectionMap() {
		return connectionMap;
	}

	public synchronized boolean isConnectExist(String ip) {
		
		for(String key:userMap.getKeySet()) {
			if(userMap.get(key).ip.equals(ip)) {
				return true;
			}
		}
		return false;
	}
}
