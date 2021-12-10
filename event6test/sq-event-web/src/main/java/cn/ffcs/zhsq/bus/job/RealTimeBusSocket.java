package cn.ffcs.zhsq.bus.job;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONObject;

import cn.ffcs.zhsq.bus.util.ByteUtils;
import cn.ffcs.zhsq.bus.websocket.pool.BusPool;

/**
 * 公交实施socket
 * 
 * @author zkongbai
 * 
 */
public class RealTimeBusSocket {
	private Charset charset = Charset.forName("gbk");
	
	public static void main(String[] args) throws Exception {
		new RealTimeBusSocket().init();
	}
	
	// 创建一个套接字通道，注意这里必须使用无参形式
	private Selector selector = null;
	private volatile boolean stop = false;

	public void init() throws Exception {
		selector = Selector.open();
		SocketChannel channel = SocketChannel.open();
		// 设置为非阻塞模式，这个方法必须在实际连接之前调用(所以open的时候不能提供服务器地址，否则会自动连接)
		channel.configureBlocking(false);
		if (channel.connect(new InetSocketAddress("218.6.54.78", 8821))) {
			channel.register(selector, SelectionKey.OP_READ);
			//System.out.println("准备发送数据..心跳包啦.....");
		} else {
			channel.register(selector, SelectionKey.OP_CONNECT);
			/*Timer timer = new Timer(true);
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					try {
						if (channel.connect(new InetSocketAddress("218.6.54.78", 8821))) {
							channel.register(selector, SelectionKey.OP_READ);
							doWrite(channel,"[heartBeat]");
						}else{
							channel.register(selector, SelectionKey.OP_CONNECT);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			},0,60*1000);*/
		}

		while (!stop) {
			selector.select(3000);
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			SelectionKey key = null;
			byte[] responseMsg = new byte[0];//业务响应消息(消息长度>1024的使用该数组)
			while (it.hasNext()) {
				key = it.next();
				it.remove();
				SocketChannel sc = (SocketChannel) key.channel();
				// OP_CONNECT 两种情况，链接成功或失败这个方法都会返回true
				if (key.isConnectable()) {
					//由于非阻塞模式，connect只管发起连接请求，finishConnect()方法会阻塞到链接结束并返回是否成功,另外还有一个isConnectionPending()返回的是是否处于正在连接状态(还在三次握手中)
					if (channel.finishConnect()) {
						sc.register(selector, SelectionKey.OP_READ);//处理完后必须吧OP_CONNECT关注去掉，改为关注OP_READ.key.interestOps(SelectionKey.OP_READ);
						System.out.println("开始发送注册消息.....");
						doWrite(channel,"[rigister]");//发送注册消息
					} else {
						// 链接失败，进程推出
						//System.exit(1);
					}
				}
				if (key.isReadable()) {
					// 读取服务端的响应
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					int readBytes = sc.read(buffer);
					//String content = "";
					if (readBytes > 0) {
						try {
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							int len = bytes.length;
							//content = ByteUtils.bytes2HexString(bytes);
							//System.out.println("接收服务器响应数据(转换为16进制)....\t\t"+content);
							if(bytes[0]==0x5B && bytes[len-1]==0x5D && bytes[1]==0x01){//链路消息响应
								chainResponse(bytes);
							}else if(bytes[0]==0x5B && bytes[len-1]==0x5D && bytes[1]==0x00){//业务响应消息,完整业务消息
								businessResponse(bytes);
							}else{//不完整的业务消息
								if(bytes[0]==0x5B && bytes[1]==0){
									responseMsg = bytes;
								}else{
									responseMsg = ByteUtils.concatByte(responseMsg, bytes);
									if(bytes[len-1]==93){//接收完成
										businessResponse(responseMsg);
										responseMsg = new byte[0];
									}
								}
							}
						} catch (Exception e) {
							//System.err.println("解析消息错误:\t\t"+e.getMessage());
						}
					} else if (readBytes < 0) {
						key.channel();
						sc.close();
					}
					key.interestOps(SelectionKey.OP_READ);
				}
			}
		}
	}
	
	/**
	 * 链路响应
	 * @param bytes
	 */
	private void chainResponse(byte[] bytes){
		if(bytes[3] == 0 && bytes[4]==3){//链路注册应答
			if(bytes[13]==0){
				System.err.println("注册失败了..........");
				stop = true;//关闭socket
			}else if(bytes[13]==1){
				System.out.println("恭喜....注册成功..........");
			}
		}
	}
	
	/**
	 * 业务数据响应
	 * @param bytes
	 */
	private void businessResponse(byte[] bytes){
		if(bytes[0]==0x5B && bytes[bytes.length-1]==0x5D && bytes[1]==0){//完整的业务消息(可能包含多条数据)
			
			String content = ByteUtils.bytes2HexString(bytes);
			//System.out.println("接收服务器响应数据(转换为16进制)....("+bytes.length+")\t\t"+content);
			String[] cArr = content.split(" 5b");
			int i = 0;
			byte[][] arr = new byte[cArr.length][];
			for(String c : cArr){
				if(c.startsWith(" 00")){//截取的数据
					c = "5b"+c;
				}
				arr[i] = ByteUtils.hexString2ByteArray(c);
				i++;
			}
			for(int j=0,len=arr.length;j<len;j++){
				bytes = arr[j];
				if(bytes[8]==1 && bytes[9]==0x11){//实时定位消息命令字
					byte[] packageHeader = Arrays.copyOfRange(bytes,2,33);//包头 总共长度31
					//content = ByteUtils.bytes2HexString(packageHeader);
					//System.out.println("包头数据....("+packageHeader.length+")\t\t"+content);
					byte[] data = Arrays.copyOfRange(bytes, 33, bytes.length-2);//包体
					data = transfor(data);//转义还原
					
					//content = ByteUtils.bytes2HexString(data);
					//System.out.println("包体数据....\t\t"+content);
					byte[] header = Arrays.copyOfRange(data,0,33);//消息头长度33
					//content = ByteUtils.bytes2HexString(header);
					//System.out.println("消息头数据....("+header.length+")\t\t"+content);
					byte[] body = Arrays.copyOfRange(data,33,data.length);//消息体
					//content = ByteUtils.bytes2HexString(body);
					//System.out.println("消息体数据....\t\t"+content);
					fixPositionInTime(packageHeader,header,body);
				}
			}
		}
	}
	
	/**
	 * 转义还原
	 * @param arr
	 */
	private byte[] transfor(byte[] arr){
		ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length);
		byte bi = 0;
		for(int i=0,len=arr.length;i<len;i++){
			bi = arr[i];
			if(bi == 0x5A){
				if(arr[i+1] == 0x01){
					byteBuffer.put((byte)0x5B);
				}else if(arr[i+1] == 0x02){
					byteBuffer.put((byte)0x5A);
				}
				i++;//跳过下一个字节
			}else if(bi == 0x5E){
				if(arr[i+1] == 0x01){
					byteBuffer.put((byte)0x5D);
				}else if(arr[i+1] == 0x02){
					byteBuffer.put((byte)0x5E);
				}
				i++;//跳过下一个字节
			}else{
				byteBuffer.put(bi);
			}
		}
		byteBuffer.flip();//反转成为只读
		//byte[] result = new byte[byteBuffer.remaining()];
		return byteBuffer.array();
	}
	/**
	 * 实时定位消息
	 * @param packageHeader 包头数据
	 * @param msgHeader 消息头数据
	 * @param msgBody 消息体数据
	 */
	private void fixPositionInTime(byte[] packageHeader,byte[] msgHeader,byte[] msgBody){
		if(packageHeader[26]==0x01)
			System.err.println("当前消息是加密消息....密钥见后四位....");
		byte[] devIdArr = Arrays.copyOfRange(msgHeader,0,21);//设备id,不足用0补
		String devId = new String(devIdArr).trim().replaceFirst("000", "5500");
		//String content = ByteUtils.bytes2HexString(msgBody);
		//System.out.println("消息体数据....("+msgBody.length+")\t\t"+content);
		int lng = ByteUtils.bytes2Int(msgBody, 12);//经度
		int lat = ByteUtils.bytes2Int(msgBody, 8); //纬度
		short dir = ByteUtils.bytes2Short(msgBody,20);//方向
		byte sx = msgBody[36];//0：上行；1：下行
		String time = ByteUtils.bcd2StrTime(Arrays.copyOfRange(msgBody,22,28));
		if(time != null && lat>20*1000*1000 && lng>90*1000*1000){//有效数据
			//System.out.println("设备id:"+devId+"\t\tlat纬度:"+lat+"\t\tlng经度:"+lng+"\t\t定位时间:"+time+"\t方向:"+dir+"\t上下行:"+sx);
			JSONObject json = new JSONObject();
			json.put("devId", devId);
			json.put("lat", lat);
			json.put("lng", lng);
			json.put("time", time);
			json.put("dir", dir);
			json.put("sx", sx);
			String jsonStr = json.toString();
			BusPool.send2All(jsonStr);
		}
	}

	private void doWrite(SocketChannel sc, String data) throws Exception {
		byte[] req = null;
		if("[rigister]".equals(data)){
			req = getRigisterByte();
			String res = ByteUtils.bytes2HexString(req);
			System.out.println("注册发送的16进制数据:\t"+res);
			//System.out.println("注册发送的16进制数据(去除空格):\t"+res.replaceAll(" ", ""));
		}else{
			req = data.getBytes(charset);
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
		byteBuffer.put(req);
		byteBuffer.flip();
		sc.write(byteBuffer);
		if (!byteBuffer.hasRemaining()) {
			System.out.println("开始写数据啦.....");
		}
	}
	
	/**
	 * 获取注册链路消息byte数组
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] getRigisterByte(){
		byte[] rigisterCode = {0x5B,0x01,0x23};//0x5B:开始标识;0x01:链路管理包;0x23:版本号;
		short command = 0x02;//命令字链路注册消息
		rigisterCode = ByteUtils.concatByte(rigisterCode,ByteUtils.toHH(command));
		int clientId = 863;
		byte[] userId = ByteUtils.toHH(clientId);//发送方ID
		//byte[] userId = ByteUtils.getDWord("863".getBytes(charset));//发送方ID
		rigisterCode = ByteUtils.concatByte(rigisterCode, userId);
		//System.out.println("消息头封装完成!消息头:\t\t"+ByteUtils.bytes2HexString(rigisterCode));
		rigisterCode = ByteUtils.concatByte(rigisterCode, userId);//“客户端ID”即传入用户ID
		byte[] psw = ByteUtils.md5ToHex("webapiyp").getBytes(charset);//注册码即传入密码的MD5值
		rigisterCode = ByteUtils.concatByte(rigisterCode, psw);
		byte judgeCode = ByteUtils.getSocketJudgeCode(rigisterCode);
		byte[] end = new byte[]{judgeCode,0x5D};//校验码 以及 结束标识符
		rigisterCode = ByteUtils.concatByte(rigisterCode, end);
		return rigisterCode;
	}
	
}
