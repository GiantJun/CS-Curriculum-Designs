package toolPackage;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * * @author 作者 :GiantJun @date 创建时间：2019年6月26日 上午10:28:18 @version
 * 1.0 @parameter @return @throws
 */
public class IPtool {// 该类主要用于获取本地IP地址、本地子网掩码、

	
	public static void main(String argus[]) {
		System.out.println("本地主机的子网掩码是："+getMyMask());
		System.out.println("本地的IP地址是："+getMyIP());
		
		brocast();
	}
	
	public static void brocast() {
		
		//子网掩码数组
		int[] mask_array = changeS2I(getMyMask());
		//获得主机数
		int hostNum = getHostNum(mask_array);
		//子网掩码有效字节长度
		int maskLength = getMaskLength(mask_array);
		//本地主机IP地址数组
		int[] host_array = changeS2I(getMyIP());
		//本网段最小主机IP地址
		int[] ip_array = getLowIP(mask_array, host_array);
		//本网段最大主机IP地址
		int[] high_array = getMaxIP(ip_array, hostNum);
		
		while(ip_array[maskLength - 1] <= high_array[maskLength - 1]) {
//			避免对本机的IP做多余的操作
//			if(ip_array[3] == host_array[3] && ip_array[2] == host_array[2] 
//					&& ip_array[1] == host_array[1] && ip_array[0] == host_array[0]) {
//				ip_array[3]++;
//			}
//			else {
//				System.out.println(ip_array[0]+"."+ip_array[1]+"."+ip_array[2]+"."+ip_array[3]);
//				ip_array[3]++;
//			}
			
			System.out.println(ip_array[0]+"."+ip_array[1]+"."+ip_array[2]+"."+ip_array[3]);
			ip_array[3]++;
			//处理进位的情况
			if(ip_array[3] > 255) {
				ip_array[2] += 1;
				ip_array[3] = 0; 
			}	
			if(ip_array[2] > 255) {
				ip_array[1] += 1;
				ip_array[2] = 0; 
			}		
			if(ip_array[1] > 255) {
				ip_array[0] += 1;
				ip_array[1] = 0; 
			}
			
		}
	}
	
	public static int getMaskLength(int[] mask_array) {
		int maskLength = 0;
		for(int i = 0; i < 4; i++) {
			if(mask_array[i] < 255) {
				maskLength = i;
				break;
			}
		}
		return maskLength;
	}
	
	/* 获取本地的IP地址 */
	public static String getMyIP() {

		int[] host_array = new int[4]; // 这个是本地主机的IP地址数组表示
		InetAddress localHost = null;

		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* 获取本地IP的数组表示 */
		String temp = new String(localHost.getHostAddress());
		String[] s = temp.split("\\.");
		for (int i = 0; i < s.length; i++) {
			host_array[i] = new Integer(s[i]);
		}

		return host_array[0] + "." + host_array[1] + "." + host_array[2] + "." + host_array[3];
	}

	//用作转换IP地址字符串为整型数组
	public static int[] changeS2I(String ipString) {
		int[] result = new int[4];
		for(int i = 0; i < 4; i++) {
			result[i] = Integer.parseUnsignedInt(ipString.split("\\.")[i]);
		}
		return result;
	}
	
	/* 获取本地的子网掩码 */
	public static String getMyMask() {
		int[] mask_array = new int[4]; // 这个是用于显示子网时的分段数组
		int maskLength = 0; // 这个是子网掩码中位为1的个数

		InetAddress localHost = null;
		NetworkInterface networkInterface = null;

		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			networkInterface = NetworkInterface.getByInetAddress(localHost);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* 成功获取子网掩码的有效位数 */
		 maskLength = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		//maskLength = 23;

		/* 构造显示的子网掩码：255.XXXX.XXXX.XXXX */
		for (int i = 0; i < 4; i++) {
			mask_array[i] = 0;
		}
		int num1 = maskLength / 8; // 确定子网掩码占的有效字节数
		int num2 = maskLength % 8; // 确定子网掩码的多余位数
		for (int i = 0; i < num1; i++) {
			mask_array[i] = 255;
		}

		if (num1 < 4) {
			int sum = 0;
			for (int j = 0; j < num2; j++) {
				sum += Math.pow(2.0, 8.0 - j * 1.0 - 1);
			}
			mask_array[num1] = sum;
		}

		return mask_array[0] + "." + mask_array[1] + "." + mask_array[2] + "." + mask_array[3];
	}

	//获得本网段的最大IP地址
	public static int[] getMaxIP(int[] ip_array, int hostNum) {
		int[] high_array = new int[4];
		// 计算出该网段的最大IP地址
		for (int i = 0; i < 4; i++) {
			high_array[i] = ip_array[i];
		}
		high_array[3] += hostNum - 1;
		if (high_array[3] > 256) {
			high_array[2] += (high_array[3] / 256);
			high_array[3] = high_array[3] % 256;
		}
		if (high_array[2] > 256) {
			high_array[1] += (high_array[2] / 256);
			high_array[2] = high_array[2] % 256;
		}
		if (high_array[1] > 256) {
			high_array[0] += (high_array[1] / 256);
			high_array[1] = high_array[1] % 256;
		}
		return high_array;
	}

	//获得本网段的最小IP地址
	public static int[] getLowIP(int[] mask_array, int[] host_array) {
		int[] ip_array = new int[4];
		/* 初始化第一个开始操作的IP地址 */
		for (int i = 0; i < 4; i++) {
			ip_array[i] = host_array[i] & mask_array[i];
		}
		return ip_array;
	}

	//获得本网段所能容纳的最大主机数
	public static int getHostNum(int[] mask_array) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			if (mask_array[i] < 255) {
				result += Math.pow(256.0, 3.0 - i) * (256 - mask_array[i]);
				break;
			}
		}

		return result;
	}
}
