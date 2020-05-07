package toolPackage;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

import javax.swing.JProgressBar;

import dataStructure.User;
import uiPackage.UpdateThread;

/**
 * * @author ���� :GiantJun @date ����ʱ�䣺2019��6��27�� ����9:51:33 @version
 * 1.0 @parameter @return @throws
 */
public class Handler {
	// ������Ĭ�ϴ�С
	private int bufferSize = 1024;
	// Ĭ���ַ���
	private String localCharset = "gb2312";
	// Ĭ����Ϣ����ѡ����
	public MySelector selector = null;
	
	// Ĭ�ϱ���·��
	private String savePath = "D:\\������P2P����ϵͳ";
	// ��д������
	private ByteBuffer sendBuffer = null;
	private ByteBuffer readBuffer = null;
	
	private String myName = null;
	private String fileName = "NoName.txt";
	private long fileSize = 0;
	public String sendPath = null;
	

	public Handler() {
		sendBuffer = ByteBuffer.allocate(bufferSize);
		readBuffer = ByteBuffer.allocate(bufferSize);
		sendBuffer.clear();
		readBuffer.clear();
	}

	public Handler(int bufferSize) {
		this(bufferSize, null);
	}

	public Handler(String localCharset) {
		this(-1, localCharset);
	}

	public Handler(int bufferSize, String localCharset ) {
		this.bufferSize = bufferSize > 0 ? bufferSize : this.bufferSize;
		this.localCharset = localCharset == null ? this.localCharset : localCharset;
		sendBuffer = ByteBuffer.allocate(bufferSize);
		readBuffer = ByteBuffer.allocate(bufferSize);
		sendBuffer.clear();
		readBuffer.clear();
	}

	public void setSavePath(String path) {
		System.out.println("���ļ�Ĭ�ϱ���·������Ϊ��"+path);
		this.savePath = path;
	}

	/************* �ͻ��˲��ֽӿ� ***************/
	// �����ͻ��ˣ������г�ʼ��
	public boolean initClient(User ifo,String ip,int port) throws IOException {
		
		//��ʼ���Լ����û�����
		myName = ifo.name;
		
		// ����socketChannel�����󶨶˿�
		SocketChannel clientChannel = SocketChannel.open();
		clientChannel.configureBlocking(false);
		// ������ʱѡ�����������Ҫ�ر�,���������������Selector��ͨ����Ψһ��Ӧ�����Կ��������ã���������
		Selector selectorT = Selector.open();
		clientChannel.register(selectorT, SelectionKey.OP_CONNECT);
		// ���ӷ����socket
		InetSocketAddress otherAddress = new InetSocketAddress(ip, port);
		clientChannel.connect(otherAddress);
		// ������������еĵ����Σ��ز����٣�
		try {
			selectorT.select();
			Set<SelectionKey> selectionKeys = selectorT.selectedKeys();
			for (SelectionKey key : selectionKeys) {
				if (key.isConnectable()) {
					SocketChannel client = (SocketChannel) key.channel();
					if (client.isConnectionPending()) {
						client.finishConnect();
						// ���͸�����Ϣ���Է�
						sendMessage(Encoder.encodeIfo1(ifo), client);
					}
				}
			}
			selectionKeys.clear();
			// �ر���ʱѡ����
			selectorT.close();
			// ���Ѿ��������ӵ�ͨ�����ɷ���˼�������������
			selector.reg(clientChannel, SelectionKey.OP_READ);
		} catch (IOException e) {
			//�����IP��ַ���Ӳ��ɹ���finishConnect�����ͻ��׳��쳣�������ﲶ��
			System.out.println(ip+"����ʧ��");
			return false;
		}
		return true;

	}

	//���Ͷ���Ϣ����Ϣ�����ΪString�����ܴ洢�ĳ���
	public void sendMessage(String message, SocketChannel socketChannel) throws IOException {
		
		sendBuffer.put(message.getBytes());
		sendBuffer.flip();
		if (socketChannel == null)
			System.out.println("����������");
		socketChannel.write(sendBuffer);
		sendBuffer.clear();

	}

	public void sendChat(String message, SocketChannel channel) {
		try {
			sendMessage(Encoder.encodeMessage(message,myName),channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�޷�������������");
		}
	}
	/************* ����˲��ֽӿ� ***************/
	// ������������������س�ʼ��
	public void initServer(int port) throws IOException {
		// ����serverSocketChannel �������˿�
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// ���ӵ�ַ
		InetSocketAddress localAddress = new InetSocketAddress(IPtool.getMyIP(), port);
		serverChannel.socket().bind(localAddress);
		// ���÷�����ģʽ
		serverChannel.configureBlocking(false);
		// ע��ѡ����
		try {
			selector = new MySelector();
			selector.reg(serverChannel, SelectionKey.OP_ACCEPT);
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("ע��ѡ��������");
		}
		System.out.println("��������ʼ������");
	}
	
	//����˴�����������
	public void handleAccept(SelectionKey selectionKey) throws IOException {
		// ��ȡ�׽���ͨ��
		SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
		// ���÷�����
		socketChannel.configureBlocking(false);
		// ע���׽���ͨ��
		selector.reg(socketChannel, SelectionKey.OP_READ);
		System.out.println("�пͻ������ߣ�");
	}

	//�����ȡ�¼������ļ���ȡ���ַ�����ȡ
	public String handleRead(SelectionKey selectionKey,JProgressBar progress)   {
		// ��ȡ�׽���ͨ��
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		String receiveStr = "";
		
		try {
			if (socketChannel.read(readBuffer) > 0) {
				// ���Զ�ȡ���ݣ���ͨ����Ϊ��ȡ״̬
				readBuffer.flip();
				// ���ձ����ȡ����
				receiveStr = new String(readBuffer.array(), 0, readBuffer.limit(), Charset.forName(localCharset));
				//System.out.println("��ȡ����"+receiveStr);
				// ����ջ��棬�����������ж�
				if(isData(receiveStr)) {
					//���������е�ǰ׺��Ϊ�ļ�������������Ϊ�ļ�
					saveFile(socketChannel,progress);
					receiveStr = null;
					sendMessage(Encoder.encodeOK(myName),socketChannel);
				}
			}
		} catch (IOException e) {
			// ˵����������ĩβ��ͨ���Ѿ��Ͽ�
			receiveStr = HeadString.DISCONNECR;
			readBuffer.clear();
			selectionKey.interestOps(SelectionKey.OP_READ);
			return receiveStr;
		}
		
		readBuffer.clear();
		// ע��selector ������ȡ����
		try {
			selector.reg(socketChannel, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectionKey.interestOps(SelectionKey.OP_READ);
		return receiveStr;
	}
	
	//����׼������������ܷ������ļ���Ϣ������������
	public void sendReady(SocketChannel channel, String path) throws IOException {
		sendPath = path;
		File f = new File(path);
		String fileName = f.getName();
		long fileSize = f.length();
		// ��һ�η����ļ��������󣬰����ļ�������
		try {
			sendMessage(Encoder.encodeFile(fileName,fileSize,myName), channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�޷������ļ���Ϣ");
		}
		System.out.println("�ɹ������ļ�����"+fileName);
	}
	
	//�����ļ�׼������,��Է����ͷ��������޸��ļ���
	public void receiveReady(String content,String fileSize,SocketChannel channel) throws IOException {
		fileName = content;
		this.fileSize = Long.parseLong(fileSize);
		sendMessage(Encoder.encodeReady(myName),channel);
	}
	
	public void sayNo(SocketChannel channel) throws IOException {
		sendMessage(Encoder.encodeNO(myName),channel);
	}
	
	//���յ����ļ������浽����Ĭ��path�������ļ������fileName�޸Ļ�Ĭ��ֵ
	private void saveFile(SocketChannel channel,JProgressBar progress) throws FileNotFoundException, IOException {
		int i = 1;
		File directory = new File(savePath);// ����·��
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(directory.getAbsolutePath()+"\\"+fileName);
		
		// ��ʼ�����ļ�
		System.out.println("��ʼ�����ļ�");
		FileChannel fileChannel = new FileOutputStream(file).getChannel();
		//ʹ��֮ǰ��ȡ��δ����ֽڻ�����
		fileChannel.write(readBuffer);
		readBuffer.clear();
		int num = 0;
		int value = 0;
		
		//�_�V�M�ȗl���¾Q�̣�ÿ0.1�����һ��
		UpdateThread updateThread = new UpdateThread(progress);
		new Thread(updateThread).start();
		
		while ((num = channel.read(readBuffer)) > 0) {
			readBuffer.flip();
			fileChannel.write(readBuffer);
			readBuffer.clear();
			value = (int) (bufferSize*(i++)*100/fileSize);
			updateThread.setValue(value);

		}
		if (num == 0) {
			fileChannel.close();
			readBuffer.clear();
			System.out.println("\n�ļ�������ɣ�");
			updateThread.setValue(100);
		}
		fileName = "UnName.txt";
		
	}
	
	//�����ļ�������ļ�·������Ϊ�գ������·��ʧЧ
	public void sendFile(SocketChannel channel,JProgressBar progressBar) throws IOException {
		File f = new File(sendPath);
		long fileSize = f.length();
		FileChannel fileChannel = new FileInputStream(f).getChannel();
		int num = 0;
		int i = 1;
		//�_�V�M�ȗl���¾Q�̣�ÿ0.1�����һ��
		UpdateThread updateThread = new UpdateThread(progressBar);
		new Thread(updateThread).start();
		System.out.println("��ʼ�����ļ�");
		while ((num = fileChannel.read(sendBuffer)) > 0) {
			System.out.print("��");
			sendBuffer.flip();
			channel.write(sendBuffer);
			sendBuffer.clear();
			updateThread.setValue((int)(bufferSize/fileSize)*(i++));
		}
		if (num == -1) {
			updateThread.setValue(100);
			fileChannel.close();
			sendBuffer.clear();
			System.out.println("\n�ļ��������");
		}
		sendPath = null;
	}
	//�ж������ǲ���������
	private boolean isData(String message) {
		String temp = message.substring(0, HeadString.Stringlength);
		switch(temp) {
		case HeadString.Chat:
		case HeadString.ING:
		case HeadString.OK:
		case HeadString.ReceiveUserIfo:
		case HeadString.SendUserIfo:
		case HeadString.SendFile:
		case HeadString.DISCONNECR:
		case HeadString.NO:
			return false;
		default: return true;
		}
	}
}
