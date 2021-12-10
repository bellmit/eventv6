package cn.ffcs.zhsq.bus.demo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.PooledByteBufferAllocator;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;


public class SchoolGpsManager {
	
	public static void main(String[] args) {
		new SchoolGpsManager().connServer();
	}

	private String serverHost="218.6.54.78";
	private String serverPort="8821";
	private String userName="webapi_yp";
	private String password="webapiyp";
	private int max_cpu = Runtime.getRuntime().availableProcessors() + 1;
	private IoConnector connector = new SocketConnector( max_cpu>3?3:max_cpu, Executors.newCachedThreadPool());
	private ConnectFuture connFuture = null;
	
	/**
	 * 缓存车辆gps数据
	 */
	private ConcurrentHashMap<String, SchoolBusGpsBO> gpsData = new ConcurrentHashMap<String,SchoolBusGpsBO>();
	/**
	 * 增量数据
	 */
	private ConcurrentHashMap<String, SchoolBusGpsBO> gpsIncrData = new ConcurrentHashMap<String,SchoolBusGpsBO>();

	/**
	 * 连接socket服务器
	 */
	public void connServer(){
		//设置buffer 
		ByteBuffer.setUseDirectBuffers(false);
	    ByteBuffer.setAllocator(new PooledByteBufferAllocator());		   
	    SocketConnectorConfig cfg = new SocketConnectorConfig();
	    cfg.setConnectTimeout(30);//s
	    //进行消息过滤和格式转换
//	    cfg.getFilterChain().addLast( "logger", new LoggingFilter());//打印info级socket日志
	    SocketAddress addr = new InetSocketAddress(serverHost,Integer.parseInt(serverPort));	
	    //建立一个I/O通道
	    connFuture = connector.connect(addr, new SchoolGpsClientHandler(this.userName,this.password,gpsData), cfg);	    
	}
	
	/**
	 * 定期检测连接状态
	 */
	public void timerEvent(){
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask(){
			public void run(){
//				System.out.println("定时检查socket连接情况.....");
//				printGpsData();
				try{
					if (connFuture == null || !connFuture.isConnected()
							|| connFuture.getSession()==null 
							|| connFuture.getSession().isClosing() 
							|| !connFuture.getSession().isConnected()){
						connServer();
					}else{
						updateGpsStatus();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}},0, 60000);//每分种检测一次
	}

	/**
	 * 定时更新gps离线状态
	 */
	public void updateGpsStatus(){
		SchoolBusGpsBO bo;
		long time = System.currentTimeMillis();
		for (String key : gpsData.keySet()) {
			bo = gpsData.get(key);
			if(bo.isOnline()){//只需要检测在线车辆
//				if(Double.parseDouble(bo.getSpeed())>0){
					if((time-bo.getUpdatedTime().getTime())>600000){//超过10分钟没有更新gps都判断为离线
						bo.setOnline(false);
						bo.setSpeed("0.0");
						bo.setUpdatedTime(new Date());
						gpsData.put(bo.getMobile(), bo);
//						System.out.println("更新车辆状态为离线："+bo.getMobile());
					}
//				}
			}
		}
	}
	
	public void printGpsData(){
		System.out.println("size-->"+gpsData.size());
		for (String key : gpsData.keySet()) {
			SchoolBusGpsBO bo = gpsData.get(key);
			System.out.println(bo.getMobile()+","+bo.getUpdatedTime()+","+bo.getGpsTime()+","+bo.getLongitude()+","+bo.getLatitude()+","+bo.getStatus()+","+bo.getPositionValid());
		}
	}
	
	public ConcurrentHashMap<String, SchoolBusGpsBO> getGpsData() {
		return gpsData;
	}

	public void setGpsData(ConcurrentHashMap<String, SchoolBusGpsBO> gpsData) {
		this.gpsData = gpsData;
	}
	
}
