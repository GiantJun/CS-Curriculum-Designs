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
 * * @author 作者 :GiantJun @date 创建时间：2019年6月27日 上午9:51:33 @version
 * 1.0 @parameter @return @throws
 */
public class Handler {
	// 缓冲器默认大小
	private int bufferSize = 1024;
	// 默认字符集
	private String localCharset = "gb2312";
	// 默认信息接收选择器
	public MySelector selector = null;
	
	// 默认保存路径
	private String savePath = "D:\\局域网P2P聊天系统";
	// 读写缓冲器
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
		System.out.println("将文件默认保存路径设置为："+path);
		this.savePath = path;
	}

	/************* 客户端部分接口 ***************/
	// 创建客户端，并进行初始化
	public boolean initClient(User ifo,String ip,int port) throws IOException {
		
		//初始化自己的用户名字
		myName = ifo.name;
		
		// 创建socketChannel，并绑定端口
		SocketChannel clientChannel = SocketChannel.open();
		clientChannel.configureBlocking(false);
		// 创建临时选择器，最后需要关闭,（！！！！这里的Selector与通道是唯一对应的所以可以这样用！！！！）
		Selector selectorT = Selector.open();
		clientChannel.register(selectorT, SelectionKey.OP_CONNECT);
		// 连接服务端socket
		InetSocketAddress otherAddress = new InetSocketAddress(ip, port);
		clientChannel.connect(otherAddress);
		// 完成三次握手中的第三次，必不可少！
		try {
			selectorT.select();
			Set<SelectionKey> selectionKeys = selectorT.selectedKeys();
			for (SelectionKey key : selectionKeys) {
				if (key.isConnectable()) {
					SocketChannel client = (SocketChannel) key.channel();
					if (client.isConnectionPending()) {
						client.finishConnect();
						// 传送个人信息给对方
						sendMessage(Encoder.encodeIfo1(ifo), client);
					}
				}
			}
			selectionKeys.clear();
			// 关闭临时选择器
			selectorT.close();
			// 将已经建立连接的通道交由服务端监听，处理请求
			selector.reg(clientChannel, SelectionKey.OP_READ);
		} catch (IOException e) {
			//如果该IP地址连接不成功，finishConnect方法就会抛出异常，在这里捕获
			System.out.println(ip+"连接失败");
			return false;
		}
		return true;

	}

	//发送短消息，消息长度最长为String类型能存储的长度
	public void sendMessage(String message, SocketChannel socketChannel) throws IOException {
		
		sendBuffer.put(message.getBytes());
		sendBuffer.flip();
		if (socketChannel == null)
			System.out.println("出错！！！！");
		socketChannel.write(sendBuffer);
		sendBuffer.clear();

	}

	public void sendChat(String message, SocketChannel channel) {
		try {
			sendMessage(Encoder.encodeMessage(message,myName),channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("无法发送聊天内容");
		}
	}
	/************* 服务端部分接口 ***************/
	// 开启服务器，并做相关初始化
	public void initServer(int port) throws IOException {
		// 创建serverSocketChannel ，监听端口
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 连接地址
		InetSocketAddress localAddress = new InetSocketAddress(IPtool.getMyIP(), port);
		serverChannel.socket().bind(localAddress);
		// 设置非阻塞模式
		serverChannel.configureBlocking(false);
		// 注册选择器
		try {
			selector = new MySelector();
			selector.reg(serverChannel, SelectionKey.OP_ACCEPT);
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("注册选择器出错！");
		}
		System.out.println("服务器开始工作！");
	}
	
	//服务端处理连接请求
	public void handleAccept(SelectionKey selectionKey) throws IOException {
		// 获取套接字通道
		SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
		// 设置非阻塞
		socketChannel.configureBlocking(false);
		// 注册套接字通道
		selector.reg(socketChannel, SelectionKey.OP_READ);
		System.out.println("有客户端上线！");
	}

	//处理读取事件，分文件读取和字符流读取
	public String handleRead(SelectionKey selectionKey,JProgressBar progress)   {
		// 获取套接字通道
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		String receiveStr = "";
		
		try {
			if (socketChannel.read(readBuffer) > 0) {
				// 可以读取内容，则将通道改为读取状态
				readBuffer.flip();
				// 按照编码读取数据
				receiveStr = new String(readBuffer.array(), 0, readBuffer.limit(), Charset.forName(localCharset));
				//System.out.println("读取到："+receiveStr);
				// 不清空缓存，进行数据流判断
				if(isData(receiveStr)) {
					//不符合所有的前缀，为文件数据流，保存为文件
					saveFile(socketChannel,progress);
					receiveStr = null;
					sendMessage(Encoder.encodeOK(myName),socketChannel);
				}
			}
		} catch (IOException e) {
			// 说明读到流的末尾，通道已经断开
			receiveStr = HeadString.DISCONNECR;
			readBuffer.clear();
			selectionKey.interestOps(SelectionKey.OP_READ);
			return receiveStr;
		}
		
		readBuffer.clear();
		// 注册selector 继续读取数据
		try {
			selector.reg(socketChannel, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectionKey.interestOps(SelectionKey.OP_READ);
		return receiveStr;
	}
	
	//发送准备工作，向接受方发送文件信息并请求发送允许
	public void sendReady(SocketChannel channel, String path) throws IOException {
		sendPath = path;
		File f = new File(path);
		String fileName = f.getName();
		long fileSize = f.length();
		// 第一次发送文件发送请求，包含文件的名字
		try {
			sendMessage(Encoder.encodeFile(fileName,fileSize,myName), channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("无法发送文件信息");
		}
		System.out.println("成功发送文件名称"+fileName);
	}
	
	//接收文件准备工作,向对方发送发送允许，修改文件名
	public void receiveReady(String content,String fileSize,SocketChannel channel) throws IOException {
		fileName = content;
		this.fileSize = Long.parseLong(fileSize);
		sendMessage(Encoder.encodeReady(myName),channel);
	}
	
	public void sayNo(SocketChannel channel) throws IOException {
		sendMessage(Encoder.encodeNO(myName),channel);
	}
	
	//将收到的文件流保存到本地默认path，保存文件，最后将fileName修改回默认值
	private void saveFile(SocketChannel channel,JProgressBar progress) throws FileNotFoundException, IOException {
		int i = 1;
		File directory = new File(savePath);// 保存路径
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(directory.getAbsolutePath()+"\\"+fileName);
		
		// 开始接收文件
		System.out.println("开始接收文件");
		FileChannel fileChannel = new FileOutputStream(file).getChannel();
		//使用之前读取的未清空字节缓冲区
		fileChannel.write(readBuffer);
		readBuffer.clear();
		int num = 0;
		int value = 0;
		
		//開闢進度條更新綫程，每0.1秒更新一次
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
			System.out.println("\n文件接收完成！");
			updateThread.setValue(100);
		}
		fileName = "UnName.txt";
		
	}
	
	//发送文件，最后将文件路径设置为空，代表该路径失效
	public void sendFile(SocketChannel channel,JProgressBar progressBar) throws IOException {
		File f = new File(sendPath);
		long fileSize = f.length();
		FileChannel fileChannel = new FileInputStream(f).getChannel();
		int num = 0;
		int i = 1;
		//開闢進度條更新綫程，每0.1秒更新一次
		UpdateThread updateThread = new UpdateThread(progressBar);
		new Thread(updateThread).start();
		System.out.println("开始发送文件");
		while ((num = fileChannel.read(sendBuffer)) > 0) {
			System.out.print("。");
			sendBuffer.flip();
			channel.write(sendBuffer);
			sendBuffer.clear();
			updateThread.setValue((int)(bufferSize/fileSize)*(i++));
		}
		if (num == -1) {
			updateThread.setValue(100);
			fileChannel.close();
			sendBuffer.clear();
			System.out.println("\n文件发送完成");
		}
		sendPath = null;
	}
	//判断数据是不是数据流
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
