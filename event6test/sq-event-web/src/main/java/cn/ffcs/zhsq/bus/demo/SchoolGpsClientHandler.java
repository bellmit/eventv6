package cn.ffcs.zhsq.bus.demo;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/****************
 * <p>Title: ClientHandler.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: FSTI</p>
 * @author Daniel Lee
 * @date 2009-12-23
 * @version 0.1
 */
public class SchoolGpsClientHandler extends IoHandlerAdapter{
	
	Charset chartset = Charset.forName("gbk");
	
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	public static byte[] concatByte(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		new SchoolGpsManager().connServer();
	}
	
	public byte[] getRigisterByte() throws NoSuchAlgorithmException{
		
		byte[] rigisterCode = {0x5B,0x01,0x00,0x02,0x00,56,54,51};
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] psw = md5.digest("webapiyp".getBytes(chartset));
		byte[] result = concatByte(rigisterCode,psw);
		byte judgeCode = 0;
		for(int i=1,len=result.length;i<len;i++){
			if(i != 1){
				judgeCode = (byte)(judgeCode^result[i]);//异或上一个值
			}else if(i==1){
				judgeCode = result[i];
			}
		}
		byte[] end = {judgeCode,0x5D};
		result = concatByte(result,end);
		return result;
	}
	final static Logger logger = LoggerFactory.getLogger(SchoolGpsClientHandler.class);
				
	private String user;
	private String pwd;
	private Map map = null ; 
	
	public SchoolGpsClientHandler(String userName,String password,Map m){
		this.user=userName;
		this.pwd=password;
		this.map = m;
	}
	
	public void exceptionCaught(IoSession session, Throwable t) throws Exception {
		logger.error(" 连接异常 "+t.getMessage());
		logger.error(" 连接关闭 \r\n");
		session.close();
	}

	/**
	 * 接收消息
	 */
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		ByteBuffer msg = (ByteBuffer) message;	
//		System.out.println(msg.toString()+"\r\n");		
		System.out.println("登录返回结果:\t"+message);
		String cmd = getSubBuff(msg);
		if (cmd.equals("001")){
			//001+chr(3) +代码+ chr(3)
			String login = getSubBuff(msg);
			if (login.equals("1")){
				logger.error(" 登录成功\r\n");
			}else{
				logger.error(" 登录失败\r\n");
			}
		}else if (cmd.equals("901")){
			showInfo(msg); //18965871020
		}else{
//			System.out.println(" 错误信息"+msg+"\r\n");
		}		
	}

	public void sessionCreated(IoSession session) throws Exception {
		logger.error(" 正在连接。。。\r\n");
		super.sessionCreated(session);
	}

	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		logger.error(" 已连上，正在登录\r\n");
		
		/*login.put("001".getBytes());
		login.put((byte)3);
		login.put(user.getBytes());
		login.put((byte)3);
		login.put(pwd.getBytes());
		login.put((byte)13);
		login.put((byte)10);*/
		String[] data = "5B 01 23 00 02 00 98 BD 92 00 00 03 5F 32 38 64 31 39 33 31 33 39 65 31 38 30 32 62 32 62 31 61 30 35 31 39 38 65 31 31 66 62 62 39 32 94 5D".split(" ");
		byte[] req = getRigisterByte();
		ByteBuffer login = ByteBuffer.allocate(req.length);
		
		login.put(req);
		login.flip();
		
		login.flip();
		logger.error("发送账号信息:"+login.toString());
		session.write(login);
	}

	public String getTimeStr(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	protected String getSubBuff(ByteBuffer msg){
		StringBuilder res = new StringBuilder();		
		while (msg.hasRemaining()){//是否还有数据
			byte b1 = msg.get();//
			if (b1 == 0x3){ //char(3) 分隔符
				break;
			}
			if (b1 == 0x13){//
				byte b2 = msg.get();
				if (b2 == 0x10){//
					break;
				}
				res.append((char)b1);
				b1 = b2;
			}
			res.append((char)b1);
		}
		return res.toString();
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	protected String getSubBuff1(ByteBuffer msg){
		StringBuilder res = new StringBuilder();		
		while (msg.hasRemaining()){//是否还有数据
			byte b1 = msg.get();//
			if (b1 == 0x3){ //char(3) 分隔符
				break;
			}
			if (b1 == 0x13){//
				byte b2 = msg.get();
				if (b2 == 0x10){//
					break;
				}
				res.append(b1);
				b1 = b2;
			}
			res.append(b1);
		}
		return res.toString();
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	protected java.nio.ByteBuffer getSubByte(ByteBuffer msg){
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(512);
		while (msg.hasRemaining()){
			byte b1 = msg.get();
			if (b1 == 0x3){ //char(3) 分隔符
				break;
			}			
			bb.put(b1);
		}
		bb.flip();
		return bb;
	}
	
	/**
	 * 方位角转换
	 * 方位的大小变化范围为0°～360°，北点为0°，东点为90°，南点为180°，西点为270°。
	 * 
	 * @return 东偏北12度
	 */
	private String getAzimuth(String str){	
		int azimuth = Integer.parseInt(str);
		int a=0;
		String b="";
		if(azimuth==0){
			return "在正北方向";
		}else if(azimuth<90){
			a=azimuth;b="东偏北";
		}else if(azimuth==90){
			return "在正东方向";
		}else if(azimuth<180){
			a=azimuth-90;b="东偏南";
		}else if(azimuth==180){
			return "在正南方向";
		}else if(azimuth<270){
			a=azimuth-180;b="西偏南";
		}else if(azimuth==270){
			return "在正西方向";	
		}else if(azimuth<360){
			a=azimuth-270;b="西偏北";
		}else if(azimuth==360){
			return "在正北方向";
		}else{
			return "";
		}		
		return b+a+"度";
	}
		
	/**
	 * 识别数据
	 * 
	 * @param info
	 */
	private void showInfo(ByteBuffer info){
		String uim = getSubBuff(info);//校车编号;手机号，前三位是中间件标识，可以忽略
		String mobile=uim.substring(3);
		String sGpsTime = getSubBuff(info);//定位时间，为格林威治时间		
		String sValid = getSubBuff(info);//定位标识:0-定位正常,1-定位异常
		String lng = getSubBuff(info);//经度
		String lat = getSubBuff(info); //纬度
		String altitude = getSubBuff(info); //海拔
		String speed =getSubBuff(info); //速度,速度为0为停车状态，速度不为0且定位时间超过5分钟则为离线
		String direction=getSubBuff(info); //方向；
		String status=getSubBuff1(info); //车辆状态,4个状态：电门关；设备在线；定位正常；停车时间
		String mileage=getSubBuff(info); //总里程		
		String statusText="";
		if(status.equals("0000")){
			statusText="电门关;设备在线;定位正常";
		}else if(status.equals("0001")){
			statusText="电门开;设备在线;定位正常";
		}else if(status.equals("0401")){
			statusText="电门开;超时停车报警;设备在线;定位正常";
		}else if(status.equals("0005")){
			statusText="电门开;设备在线;定位正常";//重车/油箱
		}else if(status.equals("04321")){
			statusText="电门开;疲劳驾驶报警;超时停车报警;设备在线;定位正常";
		}else if(status.equals("0400")){
			statusText="电门关;超时停车报警;设备在线;定位正常;";
		}else{
			statusText="未知状态["+status+"]";
		}
		SchoolBusGpsBO gps = new SchoolBusGpsBO();
		gps.setUpdatedTime(new Date());
		gps.setOnline(true);//默认在线
		gps.setDeviceNumber(uim);
		gps.setMobile(mobile);		
		gps.setPositionValid(sValid);
		gps.setLongitude(lng);
		gps.setLatitude(lat);
		gps.setAltitude(altitude);
		gps.setSpeed(speed);
		gps.setDirection(direction);
		gps.setDirectionText(getAzimuth(direction));
		gps.setStatus(status);
		gps.setStatusText(statusText);
		gps.setMileage(mileage);	
		Date sGpsDate=null;
		try{
//			sGpsDate=DateUtil.getDateTimefromStringByDefaultPatten(DateUtil.getLocateByGMT(sGpsTime));			
		}catch(Exception e){logger.error("无效的gps定位时间："+sGpsTime);sGpsDate=new Date();}
		gps.setGpsTime(sGpsDate);
//		if(Double.parseDouble(gps.getSpeed())>0){
			if((System.currentTimeMillis()-gps.getGpsTime().getTime())>600000){//超过5分钟没有更新gps的都判断为离线
				gps.setUpdatedTime(new Date());
				gps.setSpeed("0.0");
				gps.setOnline(false);
			}
//		}		
//		String data ="设备编号："+uim+",手机号："+mobile+",汇报时间："+gps.getUpdatedTime()+",定位时间："+gps.getGpsTime()+",定位标识："+sValid+",经度："+lng+",纬度："
//				+lat+",海拔："+altitude+",速度："+speed+",方向："+getAzimuth(direction)+",车辆状态："+status+",总里程："+mileage+",在线状态："+gps.isOnline()
//				+"\r\n";
//		if(gps.getUpdatedTime().compareTo(gps.getGpsTime())<0){
//			System.out.println(data);
//			System.out.print(gps.getUpdatedTime()+"->"+gps.getGpsTime());
//		}
		this.map.put(mobile, gps);		
	}
	
	
}
