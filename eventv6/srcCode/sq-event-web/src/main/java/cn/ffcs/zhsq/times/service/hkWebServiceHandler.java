package cn.ffcs.zhsq.times.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.UnsignedShort;

import cn.ffcs.doorsys.bo.equipment.AfsLog;
import cn.ffcs.doorsys.bo.equipment.Equipment;
import cn.ffcs.zhsq.mybatis.domain.alarm.AlarmLog;
import cn.ffcs.zhsq.mybatis.domain.alarm.FaceRecordInfo;
import cn.ffcs.zhsq.mybatis.domain.alarm.SnapRecordInfo;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DeviceChannelInfo;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.*;

/**
 * 海康WebService处理类
 * @author 
 *
 */
public class hkWebServiceHandler {
	
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	/*********************人脸识别 begin*****************************/
	private static WebServiceStub webServiceStub=null;
	private static WebServiceStub getWebServiceStub(){
		if(webServiceStub==null){
			try {
				webServiceStub = new WebServiceStub("http://172.117.253.194:8000/WebService");
			} catch (AxisFault e) {
				e.printStackTrace();
			} 
		}
		return webServiceStub;
	}
	
	public static DeviceChannelInfo[] parseDevice(List<Equipment> equipmentList){
		DeviceChannelInfo[] devList = new DeviceChannelInfo[equipmentList.size()]; 
		int i=0;
		for (Equipment equipment : equipmentList) {
			if(equipment.getEqpPort() != null){
				DeviceChannelInfo dev = new DeviceChannelInfo();
				dev.setNChannel(0);
				dev.setStDevName("");
				dev.setNGroupID(0);
				Address address = new Address();
				address.setNPort(new UnsignedShort(equipment.getEqpPort()));
				address.setStrIP(equipment.getEqpIp());
				dev.setStSnapDevice(address);
				devList[i]=dev;	
				i++;
			}
		}
//		
//		DeviceChannelInfo dev1 = new DeviceChannelInfo();
//		dev1.setNChannel(0);
//		dev1.setStDevName("");
//		dev1.setNGroupID(0);
//		Address address1 = new Address();
//		address1.setNPort(new UnsignedShort(8000));
//		address1.setStrIP("172.17.119.69");
//		dev1.setStSnapDevice(address1);
//		devList[0]=dev1;	
		return devList;
	}
	
	public static SnapRecordInfo searchAfsLogs(GetAlarmListsReq params){
			WebServiceStub webServiceStub = getWebServiceStub();
//				
//			InquireAlarmListCond  stInquireAlarmListCond = new InquireAlarmListCond();
//			stInquireAlarmListCond.setStDeviceChanInfo(parseDevice(equipmentList));	
//			stInquireAlarmListCond.setUStartID(new UnsignedInt(0));
//			stInquireAlarmListCond.setUEndID(new UnsignedInt(20));
//			stInquireAlarmListCond.setUMaxRecNum(new UnsignedInt(1000));
//			stInquireAlarmListCond.setUMinValue(new UnsignedInt(30));
//			stInquireAlarmListCond.setUMaxValue(new UnsignedInt(100));
//			TimeRange stSnapTimeRange = new TimeRange();
////			stSnapTimeRange.setStStartTime("2016-06-13 00:00:00");
////			stSnapTimeRange.setStEndTime("2016-07-20 00:00:00");
//			stSnapTimeRange.setStStartTime(beginTime);
//			stSnapTimeRange.setStEndTime(endTime);
//			
//			stInquireAlarmListCond.setStSnapTimeRange(stSnapTimeRange);
//			
//			stInquireAlarmListCond.setNInquireType(new UnsignedInt(0));
//			stInquireAlarmListCond.setNAlarmType(new UnsignedInt(1));
//			GetAlarmListsReq params = new GetAlarmListsReq();
//			params.setStInquireAlarmListCond(stInquireAlarmListCond);
			GetAlarmListsRsp rsp=null;
			try {
				rsp = webServiceStub.getAlarmLists(params);
				return getAsfLogList(rsp);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			System.out.println("nErrorCode：" + rsp.getNErrorCode());
			return null;
	}
	
	/**
	 * 将 GetAlarmListsRsp 转成  List<AlarmLog>对象
	 * @param rsp
	 * @return
	 */
	public static SnapRecordInfo getAsfLogList(GetAlarmListsRsp rsp){
		if(rsp.getNErrorCode()!=1)	return null;
		SnapRecordInfo snapRecordInfo = new SnapRecordInfo();
		List<AfsLog> afsLogList = new ArrayList<AfsLog>();
		AlarmList[] alarmList =  rsp.getStAlarmList();
		if(rsp == null || alarmList == null) return null;
		for (AlarmList alarm : alarmList) {
			AfsLog afsLog = new AfsLog();
			afsLog.setuId(alarm.getUID().longValue());
			SnapRecord snapRecord =alarm.getStSnapRecord();
			try {
				afsLog.setStSnapTime(timeFormat.parse(snapRecord.getStSnapTime()));
			} catch (ParseException e) {
				afsLog.setStSnapTime(new Date());
			}
			DataHandler dh= snapRecord.getStPicData();
			try {
				afsLog.setStPicData(new byte[dh.getInputStream().available()]);
			} catch (IOException e) {
				afsLog.setStPicData(null);
			}
			afsLog.setSzStorIP(snapRecord.getSzStorIP());
			afsLog.setnStorPort(snapRecord.getNStorPort());
			afsLog.setSzBkgPath(snapRecord.getSzBkgPath());
			afsLog.setSzCapPath(snapRecord.getSzCapPath());
			afsLog.setbModelStatus(snapRecord.getBModelStatus());
//			afsLog.set
//			afsLog.setBizId(snapRecord.getStSnapDevice().getUID().toString());
			afsLogList.add(afsLog);

			snapRecordInfo.setStSnapTime(snapRecord.getStSnapTime());//抓拍时间
			snapRecordInfo.setStrIP(snapRecord.getStSnapDevice().getStAddress().getStrIP());//抓拍地点
			snapRecordInfo.setStrName(snapRecord.getStHumanAttri().getStrName());//捕获对象
			
			
			//人脸信息
			FaceRecord[] faceRecords = alarm.getStFaceRecord();
			FaceRecordInfo[] faceRecordInfos = new FaceRecordInfo[faceRecords.length];
			for(int i=0; i<faceRecords.length;i++){
				faceRecordInfos[i].setStrName(faceRecords[i].getStHumanAttri().getStrName());//姓名
				String stSex = faceRecords[i].getStHumanAttri().getStSex().getValue();//性别
				if(stSex.equals("0")){//无限制
					faceRecordInfos[i].setStSex("无限制");
				}else if(stSex.equals("1")){//男性
					faceRecordInfos[i].setStSex("男性");
				}else if(stSex.equals("2")){//女性
					faceRecordInfos[i].setStSex("女性");
				}else if(stSex.equals("3")){//未知
					faceRecordInfos[i].setStSex("未知");
				}
				faceRecordInfos[i].setnAge(faceRecords[i].getStHumanAttri().getNAge());//年龄
				int nGlassOn = faceRecords[i].getStHumanAttri().getNGlassOn();//是否戴眼镜
				if(nGlassOn == 0){
					faceRecordInfos[i].setnGlassOn("未知");
				}else if(nGlassOn == 1){
					faceRecordInfos[i].setnGlassOn("否");
				}else if(nGlassOn == 2){
					faceRecordInfos[i].setnGlassOn("是");
				}else if(nGlassOn == 2){
					faceRecordInfos[i].setnGlassOn("未知");
				}
				faceRecordInfos[i].setnSimilarity(faceRecords[i].getNSimilarity());//相似度
			}
			snapRecordInfo.setFaceRecordInfos(faceRecordInfos);
		}
		return snapRecordInfo;
	}
	
	/*********************人脸识别 end*****************************/
	

}
