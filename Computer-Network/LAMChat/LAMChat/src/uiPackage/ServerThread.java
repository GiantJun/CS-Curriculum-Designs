package uiPackage;

import java.io.IOException;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

import dataStructure.ContentProvider;
import dataStructure.User;
import toolPackage.Encoder;
import toolPackage.Handler;
import toolPackage.HeadString;
import toolPackage.MySelector;


/**
 * * @author ���� :GiantJun @date ����ʱ�䣺2019��6��27�� ����3:25:11 @version
 * 1.0 @parameter @return @throws
 */
public class ServerThread implements Runnable {
	private Handler handler = null;
	private MySelector selector = null;
	private ContentProvider contentProvider = null;
	protected MainFrame frame;
	
	public boolean isBusy = false;
	public boolean flag = true;
	

	public ServerThread(Handler handler, ContentProvider contentProvider,MainFrame frame) {
		this.handler = handler;
		this.selector = handler.selector;
		this.contentProvider = contentProvider;
		this.frame = frame;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			try {
				//��һֱΪfalse˵��select����һֱ����
				isBusy = false;
				selector.select();
				//�����߳�æ״̬
				isBusy = true;
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (SelectionKey key : selectionKeys) {
					handle(key);
				}
				selectionKeys.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

		}
	}

	// ������������key�Ĳ�ͬ����������ͬ����Ӧ
	private void handle(SelectionKey key) {
		String message = "";
		if (key.isAcceptable()) {
			try {
				handler.handleAccept(key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (key.isReadable()) {
			//�@ȡ���ܵ��ĵ�һ����Ϣ���棬������f�h�ַ���ƥ���h�������ڰl���ļ�
			message = handler.handleRead(key,frame.progressBar);

			// ���¸���ǰ׺�Ĳ�ͬ�������ݴ���
			if(message == null) {
				//�����������ַ���Ϊ�գ�˵��Ϊ�ļ�������
				
				
			}
			else if(message.startsWith(HeadString.DISCONNECR)) {
				//���ӶϿ�
				String userName = contentProvider.getConnectionMap().getKeyfromValue((SocketChannel)key.channel());
				if(userName == null) {
					//˵�������ṩ�������Ѿ������и����ӱ�������Ѿ��Ͽ�
					return;
				}
				SocketChannel socketChannel = (SocketChannel)key.channel();
				//ɾ�����е�һ��
				contentProvider.getUserMap().remove(userName);
				contentProvider.getConnectionMap().removeByValue((SocketChannel)key.channel());
				
				try {
					socketChannel.shutdownOutput();
					socketChannel.shutdownInput();
					socketChannel.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(userName+"�Ͽ�����\n");
				frame.removeFriendList(userName);
				
			}else if (message.startsWith(HeadString.SendUserIfo)) {
				// ��һ�θ�����Ϣ����
				String content = getContent(message);
				String[] strings = content.split(HeadString.splitChar);
				User user = new User(strings[0], strings[1], strings[2],strings[3]);
				
				try {
					//����ȡ����Ϣ�����û�������ӱ���
					contentProvider.getUserMap().put(user.name, user);
					contentProvider.getConnectionMap().put(user.name, (SocketChannel)key.channel());
				}catch(RuntimeException e) {
					//�����쳣��˵��д��contentProvider�������ظ���
					return ;
				}
				//Ϊ�����������û���
				frame.addFriendList(user.name);
				
				//�����Լ�����Ϣ
				try {
					handler.sendMessage(Encoder.encodeIfo2(contentProvider.mySelf), (SocketChannel)key.channel());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("�ڶ��η��͸�����Ϣ����");
				}
				System.out.println("��õ����û�"+user.name+"����Ϣ");
			}
			else if(message.startsWith(HeadString.ReceiveUserIfo)) {
				String content = getContent(message);
				String[] strings = content.split(HeadString.splitChar);
				//�ĸ��ֶηֱ��Ӧ���û�����IP��ַ���������롢�˿ں� 
				User user = new User(strings[0], strings[1], strings[2],strings[3]);
				
				try {
					//����ȡ����Ϣ�����û�������ӱ���
					contentProvider.getUserMap().put(user.name, user);
					contentProvider.getConnectionMap().put(user.name, (SocketChannel)key.channel());
				}catch(RuntimeException e) {
					//�����쳣��˵��д��contentProvider�������ظ���
					return ;
				}
				
				//Ϊ�����������û���
				frame.addFriendList(user.name);
				
				System.out.println("��õ��Է�"+user.name+"����Ϣ");
			}
			else if(message.startsWith(HeadString.Chat)){
				//����������������
				String content = getContent(message);
				//�����ֶηֱ��Ӧ�����ͷ����֡�������Ϣ
				String[] strings = content.split(HeadString.splitChar);
				frame.showMassage(strings[0], frame.myself.name,strings[1], false);
				
			}else if(message.startsWith(HeadString.SendFile)) {
				//��÷����ļ������Լ��ļ����͵ĺ�׺
				String content = getContent(message);
				//��һ���ֶ��Ƿ��ͷ����֣��ڶ����ֶ��Ƿ����ļ����ļ���
				String[] strings = content.split(HeadString.splitChar);
				System.out.println("��ʼ����"+strings[1]+"�ļ�");
				System.out.println("�ļ���С��"+Integer.parseInt(strings[2])/1024+" M");
				
				boolean isOK = frame.showNotice(strings[0]+"�����ļ���\n"+"�ļ���Ϊ��"+strings[1]+"\n�ļ���С��"
				+Integer.parseInt(strings[2])/1024+" M\n"+"���Ƿ�ȷ�Ͻ��գ���");
				if(isOK) {
					try {
						handler.receiveReady(strings[1],strings[2],(SocketChannel)key.channel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("���ͽ����ļ�ȷ����Ϣ����");
					}
					
				}else {
					try {
						handler.sayNo((SocketChannel)key.channel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("�ܾ���Ϣ���ʹ���");
					}
				}
				
			}else if(message.startsWith(HeadString.ING)){
					//�յ����ǽ��ܷ��������ļ�
				try {
					handler.sendFile((SocketChannel)key.channel(),frame.progressBar);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("�ļ����ʹ���");
				}
			}else if(message.startsWith(HeadString.OK)) {
				//˵�����ܷ��ɹ����յ��ļ�
				String content = getContent(message);
				System.out.println(content+"��ȷ������ļ�����");
				frame.showNotice(content+"�ɹ��յ����ļ���");
				
			}else if(message.startsWith(HeadString.NO)) {
				String content = getContent(message);
				frame.showNotice(content+"�ܾ�����ķ����ļ�����");
			}
		}
	}

	
	
	private String getContent(String line) {
		return line.substring(HeadString.Stringlength);
	}
}
