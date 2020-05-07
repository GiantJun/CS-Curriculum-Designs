package uiPackage;

import java.io.IOException;

import dataStructure.User;
import toolPackage.Handler;
import toolPackage.IPtool;

/**
 * * @author ���� :GiantJun @date ����ʱ�䣺2019��6��30�� ����3:57:01 @version 1.0
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
		// ������������
		int[] mask_array = IPtool.changeS2I(IPtool.getMyMask());
		// ���������
		int hostNum = IPtool.getHostNum(mask_array);
		// ����������Ч�ֽڳ���
		int maskLength = IPtool.getMaskLength(mask_array);
		// ��������IP��ַ����
		int[] host_array = IPtool.changeS2I(IPtool.getMyIP());
		// ��������С����IP��ַ
		int[] ip_array = IPtool.getLowIP(mask_array, host_array);
		// �������������IP��ַ
		int[] high_array = IPtool.getMaxIP(ip_array, hostNum);

		while (ip_array[maskLength - 1] <= high_array[maskLength - 1]) {
					 //����Ա�����IP������Ĳ���
					if (ip_array[3] == host_array[3] && ip_array[2] == host_array[2] && ip_array[1] == host_array[1]
							&& ip_array[0] == host_array[0]) {
						ip_array[3]++;
					} else {
						/***************** ���Ĳ��ַ����������� ********************/
						String ip = ip_array[0] + "." + ip_array[1] + "." + ip_array[2] + "." + ip_array[3];
						try {
							// �����Ƿ�ɹ��Ѿ���myHandler����
							myHandler.initClient(myself, ip, port);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /***************** ���Ĳ��ַ����������� ********************/
						ip_array[3]++;
					}

			
//			/***************** ���Ĳ��ַ����������󣨲��ų��Լ���IP�� ********************/
//			String ip = ip_array[0] + "." + ip_array[1] + "." + ip_array[2] + "." + ip_array[3];
//			try {
//				// �����Ƿ�ɹ��Ѿ���myHandler����
//				myHandler.initClient(myself, ip, port);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} /***************** ���Ĳ��ַ����������� �����ų��Լ���IP��********************/
//			ip_array[3]++;
			
			
			
			
			// �����λ�����
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
