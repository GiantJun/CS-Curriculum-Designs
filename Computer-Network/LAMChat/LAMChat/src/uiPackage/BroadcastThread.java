package uiPackage;

import java.io.IOException;

import dataStructure.User;
import toolPackage.Handler;
import toolPackage.IPtool;

/**
 * * @author 作者 :GiantJun @date 创建时间：2019年6月30日 下午3:57:01 @version 1.0
 * <p>Description: </p> @throws
 */
public class BroadcastThread implements Runnable {

	private Handler myHandler = null;
	private User myself = null;
	private int port = 60000;
	
	public BroadcastThread(Handler handler, User myself,int port) {
		this.myHandler = handler;
		this.myself = myself;
		this.port = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 子网掩码数组
		int[] mask_array = IPtool.changeS2I(IPtool.getMyMask());
		// 获得主机数
		int hostNum = IPtool.getHostNum(mask_array);
		// 子网掩码有效字节长度
		int maskLength = IPtool.getMaskLength(mask_array);
		// 本地主机IP地址数组
		int[] host_array = IPtool.changeS2I(IPtool.getMyIP());
		// 本网段最小主机IP地址
		int[] ip_array = IPtool.getLowIP(mask_array, host_array);
		// 本网段最大主机IP地址
		int[] high_array = IPtool.getMaxIP(ip_array, hostNum);

		while (ip_array[maskLength - 1] <= high_array[maskLength - 1]) {
					 //避免对本机的IP做多余的操作
					if (ip_array[3] == host_array[3] && ip_array[2] == host_array[2] && ip_array[1] == host_array[1]
							&& ip_array[0] == host_array[0]) {
						ip_array[3]++;
					} else {
						/***************** 核心部分发送连接请求 ********************/
						String ip = ip_array[0] + "." + ip_array[1] + "." + ip_array[2] + "." + ip_array[3];
						try {
							// 连接是否成功已经由myHandler处理
							myHandler.initClient(myself, ip, port);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /***************** 核心部分发送连接请求 ********************/
						ip_array[3]++;
					}

			
//			/***************** 核心部分发送连接请求（不排除自己的IP） ********************/
//			String ip = ip_array[0] + "." + ip_array[1] + "." + ip_array[2] + "." + ip_array[3];
//			try {
//				// 连接是否成功已经由myHandler处理
//				myHandler.initClient(myself, ip, port);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} /***************** 核心部分发送连接请求 （不排除自己的IP）********************/
//			ip_array[3]++;
			
			
			
			
			// 处理进位的情况
			if (ip_array[3] > 255) {
				ip_array[2] += 1;
				ip_array[3] = 0;
			}
			if (ip_array[2] > 255) {
				ip_array[1] += 1;
				ip_array[2] = 0;
			}
			if (ip_array[1] > 255) {
				ip_array[0] += 1;
				ip_array[1] = 0;
			}

		}
	}

}
