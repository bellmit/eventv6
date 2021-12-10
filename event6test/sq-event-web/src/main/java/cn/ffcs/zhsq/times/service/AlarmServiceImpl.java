package cn.ffcs.zhsq.times.service;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis2.databinding.types.UnsignedInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.doorsys.bo.equipment.Equipment;
import cn.ffcs.doorsys.service.equipment.IEquipmentService;
import cn.ffcs.zhsq.alarm.IAlarmService;
import cn.ffcs.zhsq.mybatis.domain.alarm.SnapRecordInfo;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AlarmList;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DeviceChannelInfo;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.FaceRecord;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetAlarmListsReq;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.InquireAlarmListCond;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SnapRecord;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.TimeRange;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "alarmServiceImpl")
public class AlarmServiceImpl implements IAlarmService{

	@Autowired
	protected IEquipmentService equipmentService;

	private SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public SnapRecordInfo getData(){
		WebServiceStub stub = null;
		
		WebServiceStub.GetAlarmListReq getAlarmListReq10 = new WebServiceStub.GetAlarmListReq();
		GetAlarmListsReq getAlarmListsReq = new GetAlarmListsReq();
//		getAlarmListsReq.setStInquireAlarmListCond(param);
		
		try {
			WebServiceStub.GetAlarmListRsp getAlarmListRsp = stub.getAlarmList(getAlarmListReq10);
			AlarmList AlarmList = getAlarmListRsp.getStAlarmList();
			
			SnapRecord snapRecord = AlarmList.getStSnapRecord();
			snapRecord.getStSnapDevice().getStrNote();
			snapRecord.getStSnapDevice().getStAddress().getStrIP();
			snapRecord.getStSnapTime();//抓拍时间
			
			snapRecord.getStHumanAttri().getStSex().getValue();
			snapRecord.getStHumanAttri().getNAge();
			snapRecord.getStHumanAttri().getNGlassOn();
			
			FaceRecord[] faceRecords = AlarmList.getStFaceRecord();
			for(FaceRecord faceRecord : faceRecords){
//				faceRecord.getStPicData().getOutputStream()
				
			}
			
//			String[] result = 
			return new SnapRecordInfo();
//			FaceRecord[] faceRecord = AlarmList.getStFaceRecord();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SnapRecordInfo getAlarmDatas(String beginTime, String endTime){
		Calendar now=Calendar.getInstance();
		String queryBeginTime=timeFormat.format(now.getTimeInMillis());
		
		now.add(Calendar.SECOND, 30);//轮训时间
		String queryEndTime=timeFormat.format(now.getTimeInMillis());
//		queryBeginTime ="2016-06-01 00:00:00";
//		queryEndTime="2016-07-21 00:00:00";
		InquireAlarmListCond stInquireAlarmListCond = new InquireAlarmListCond();
		/**查询获取人脸设备**/
		Map<String,Object> eqpParam = new HashMap<String, Object>();
		eqpParam.put("eqpType", "007");
		List<Equipment> eqpList = equipmentService.findList(eqpParam);
		if(eqpList.size()>0){
			DeviceChannelInfo[] devices =hkWebServiceHandler.parseDevice(eqpList);
			stInquireAlarmListCond.setStDeviceChanInfo(devices);
			/**设置人脸设备结束**/
			stInquireAlarmListCond.setUStartID(new UnsignedInt(ConstantValue.START));
			stInquireAlarmListCond.setUEndID(new UnsignedInt(ConstantValue.LIMIT));
			stInquireAlarmListCond.setUMinValue(new UnsignedInt(30));
			stInquireAlarmListCond.setUMaxValue(new UnsignedInt(100));
			stInquireAlarmListCond.setUMaxRecNum(new UnsignedInt(ConstantValue.LIMIT));
			stInquireAlarmListCond.setNInquireType(new UnsignedInt(0));
			TimeRange stSnapTimeRange = new TimeRange();
			stSnapTimeRange.setStStartTime(beginTime);
			stSnapTimeRange.setStEndTime(endTime);
			stInquireAlarmListCond.setStSnapTimeRange(stSnapTimeRange);
			stInquireAlarmListCond.setNAlarmType(new UnsignedInt(1));
			GetAlarmListsReq params = new GetAlarmListsReq();
			params.setStInquireAlarmListCond(stInquireAlarmListCond);
			SnapRecordInfo snapRecordInfo = hkWebServiceHandler.searchAfsLogs(params);
			return snapRecordInfo;
		}
		return null;
	}
}
