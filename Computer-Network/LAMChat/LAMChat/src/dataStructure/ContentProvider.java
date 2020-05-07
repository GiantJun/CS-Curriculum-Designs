package dataStructure;

import java.nio.channels.SocketChannel;

/** * @author  ���� :GiantJun 
* @date ����ʱ�䣺2019��6��28�� ����3:39:22 
* @version 1.0 
* <p>Description: </p>   
* @throws 
*/
public class ContentProvider {
	
	//�������߱�
	private OnlineMap<String,SocketChannel> connectionMap = null;
	private OnlineMap<String,User> userMap = null;
	//�Լ��ĸ�����Ϣ
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
