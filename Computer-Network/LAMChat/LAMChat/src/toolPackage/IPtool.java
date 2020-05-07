package toolPackage;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * * @author ���� :GiantJun @date ����ʱ�䣺2019��6��26�� ����10:28:18 @version
 * 1.0 @parameter @return @throws
 */
public class IPtool {// ������Ҫ���ڻ�ȡ����IP��ַ�������������롢

	
	public static void main(String argus[]) {
		System.out.println("�������������������ǣ�"+getMyMask());
		System.out.println("���ص�IP��ַ�ǣ�"+getMyIP());
		
		brocast();
	}
	
	public static void brocast() {
		
		//������������
		int[] mask_array = changeS2I(getMyMask());
		//���������
		int hostNum = getHostNum(mask_array);
		//����������Ч�ֽڳ���
		int maskLength = getMaskLength(mask_array);
		//��������IP��ַ����
		int[] host_array = changeS2I(getMyIP());
		//��������С����IP��ַ
		int[] ip_array = getLowIP(mask_array, host_array);
		//�������������IP��ַ
		int[] high_array = getMaxIP(ip_array, hostNum);
		
		while(ip_array[maskLength - 1] <= high_array[maskLength - 1]) {
//			����Ա�����IP������Ĳ���
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
			//�����λ�����
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
	
	/* ��ȡ���ص�IP��ַ */
	public static String getMyIP() {

		int[] host_array = new int[4]; // ����Ǳ���������IP��ַ�����ʾ
		InetAddress localHost = null;

		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* ��ȡ����IP�������ʾ */
		String temp = new String(localHost.getHostAddress());
		String[] s = temp.split("\\.");
		for (int i = 0; i < s.length; i++) {
			host_array[i] = new Integer(s[i]);
		}

		return host_array[0] + "." + host_array[1] + "." + host_array[2] + "." + host_array[3];
	}

	//����ת��IP��ַ�ַ���Ϊ��������
	public static int[] changeS2I(String ipString) {
		int[] result = new int[4];
		for(int i = 0; i < 4; i++) {
			result[i] = Integer.parseUnsignedInt(ipString.split("\\.")[i]);
		}
		return result;
	}
	
	/* ��ȡ���ص��������� */
	public static String getMyMask() {
		int[] mask_array = new int[4]; // �����������ʾ����ʱ�ķֶ�����
		int maskLength = 0; // ���������������λΪ1�ĸ���

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

		/* �ɹ���ȡ�����������Чλ�� */
		 maskLength = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		//maskLength = 23;

		/* ������ʾ���������룺255.XXXX.XXXX.XXXX */
		for (int i = 0; i < 4; i++) {
			mask_array[i] = 0;
		}
		int num1 = maskLength / 8; // ȷ����������ռ����Ч�ֽ���
		int num2 = maskLength % 8; // ȷ����������Ķ���λ��
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

	//��ñ����ε����IP��ַ
	public static int[] getMaxIP(int[] ip_array, int hostNum) {
		int[] high_array = new int[4];
		// ����������ε����IP��ַ
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

	//��ñ����ε���СIP��ַ
	public static int[] getLowIP(int[] mask_array, int[] host_array) {
		int[] ip_array = new int[4];
		/* ��ʼ����һ����ʼ������IP��ַ */
		for (int i = 0; i < 4; i++) {
			ip_array[i] = host_array[i] & mask_array[i];
		}
		return ip_array;
	}

	//��ñ������������ɵ����������
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
