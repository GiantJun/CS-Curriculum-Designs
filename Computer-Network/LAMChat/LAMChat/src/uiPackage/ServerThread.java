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
 * * @author 作者 :GiantJun @date 创建时间：2019年6月27日 下午3:25:11 @version
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
				//若一直为false说明select方法一直阻塞
				isBusy = false;
				selector.select();
				//设置线程忙状态
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

	// 处理方法，根据key的不同请求做出不同的响应
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
			//獲取接受到的第一個信息緩存，如果跟協議字符串匹配説明不是在發送文件
			message = handler.handleRead(key,frame.progressBar);

			// 以下根据前缀的不同进行数据处理
			if(message == null) {
				//解析出来的字符串为空，说明为文件数据流
				
				
			}
			else if(message.startsWith(HeadString.DISCONNECR)) {
				//连接断开
				String userName = contentProvider.getConnectionMap().getKeyfromValue((SocketChannel)key.channel());
				if(userName == null) {
					//说明内容提供器里面已经不含有该连接表项，连接已经断开
					return;
				}
				SocketChannel socketChannel = (SocketChannel)key.channel();
				//删除表中的一项
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
				System.out.println(userName+"断开连接\n");
				frame.removeFriendList(userName);
				
			}else if (message.startsWith(HeadString.SendUserIfo)) {
				// 第一次个人信息交换
				String content = getContent(message);
				String[] strings = content.split(HeadString.splitChar);
				User user = new User(strings[0], strings[1], strings[2],strings[3]);
				
				try {
					//将获取的信息存入用户表和连接表中
					contentProvider.getUserMap().put(user.name, user);
					contentProvider.getConnectionMap().put(user.name, (SocketChannel)key.channel());
				}catch(RuntimeException e) {
					//捕获异常，说明写入contentProvider的内容重复了
					return ;
				}
				//为聊天界面更新用户表
				frame.addFriendList(user.name);
				
				//发送自己的信息
				try {
					handler.sendMessage(Encoder.encodeIfo2(contentProvider.mySelf), (SocketChannel)key.channel());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("第二次发送个人信息出错");
				}
				System.out.println("获得到新用户"+user.name+"的信息");
			}
			else if(message.startsWith(HeadString.ReceiveUserIfo)) {
				String content = getContent(message);
				String[] strings = content.split(HeadString.splitChar);
				//四个字段分别对应：用户名、IP地址、子网掩码、端口号 
				User user = new User(strings[0], strings[1], strings[2],strings[3]);
				
				try {
					//将获取的信息存入用户表和连接表中
					contentProvider.getUserMap().put(user.name, user);
					contentProvider.getConnectionMap().put(user.name, (SocketChannel)key.channel());
				}catch(RuntimeException e) {
					//捕获异常，说明写入contentProvider的内容重复了
					return ;
				}
				
				//为聊天界面更新用户表
				frame.addFriendList(user.name);
				
				System.out.println("获得到对方"+user.name+"的信息");
			}
			else if(message.startsWith(HeadString.Chat)){
				//发来的是聊天内容
				String content = getContent(message);
				//两个字段分别对应：发送方名字、发送消息
				String[] strings = content.split(HeadString.splitChar);
				frame.showMassage(strings[0], frame.myself.name,strings[1], false);
				
			}else if(message.startsWith(HeadString.SendFile)) {
				//获得发送文件请求以及文件类型的后缀
				String content = getContent(message);
				//第一个字段是发送方名字，第二个字段是发送文件的文件名
				String[] strings = content.split(HeadString.splitChar);
				System.out.println("开始接收"+strings[1]+"文件");
				System.out.println("文件大小為"+Integer.parseInt(strings[2])/1024+" M");
				
				boolean isOK = frame.showNotice(strings[0]+"发来文件！\n"+"文件名为："+strings[1]+"\n文件大小："
				+Integer.parseInt(strings[2])/1024+" M\n"+"【是否确认接收？】");
				if(isOK) {
					try {
						handler.receiveReady(strings[1],strings[2],(SocketChannel)key.channel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("发送接收文件确认信息错误");
					}
					
				}else {
					try {
						handler.sayNo((SocketChannel)key.channel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("拒绝消息发送错误");
					}
				}
				
			}else if(message.startsWith(HeadString.ING)){
					//收到的是接受方允许发送文件
				try {
					handler.sendFile((SocketChannel)key.channel(),frame.progressBar);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("文件发送错误");
				}
			}else if(message.startsWith(HeadString.OK)) {
				//说明接受方成功接收到文件
				String content = getContent(message);
				System.out.println(content+"已确认完成文件接收");
				frame.showNotice(content+"成功收到了文件！");
				
			}else if(message.startsWith(HeadString.NO)) {
				String content = getContent(message);
				frame.showNotice(content+"拒绝了你的发送文件请求！");
			}
		}
	}

	
	
	private String getContent(String line) {
		return line.substring(HeadString.Stringlength);
	}
}
