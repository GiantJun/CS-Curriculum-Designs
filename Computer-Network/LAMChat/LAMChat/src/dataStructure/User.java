package dataStructure;
/** * @author  作者 :GiantJun 
* @date 创建时间：2019年6月26日 上午10:25:49 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class User {

	public String name = null;
	public String ip = null;
	public String ipMask = null;
	public String port = null;
	
	public User(String name,String ip,String ipMask,String port) {
		this.name = name;
		this.ip = ip;
		this.ipMask = ipMask;
		this.port = String.valueOf(port);
	}
}
