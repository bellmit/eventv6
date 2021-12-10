package cn.ffcs.zhsq.times.service;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.UnsignedInt;
import org.apache.axis2.databinding.types.UnsignedShort;

import cn.ffcs.doorsys.bo.equipment.AfsLog;
import cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub;
import cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.InitSystemResponse;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub;
import cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.*;
import cn.ffcs.zhsq.utils.ConstantValue;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			WebServiceStub webServiceStub = new WebServiceStub(
					"http:172.117.253.194:8000/WebService");
			GetDevicesReq dreq = new GetDevicesReq();
			GetDevicesRsp drsp = null;
			drsp = webServiceStub.getDevices(dreq);
			Device[] devs = drsp.getStDevice();
			int num = 50;
//			DeviceChannelInfo[] devList = new DeviceChannelInfo[num];
//			for (int i = 0; i < 1; i++) {
//				DeviceChannelInfo dev = new DeviceChannelInfo();
//				dev.setNChannel(0);
//				dev.setStDevName("");
//				dev.setNGroupID(0);
//				dev.setStSnapDevice(devs[i + 25].getStAddress());
//				devList[i] = dev;
//			}

			DeviceChannelInfo[] devList = new DeviceChannelInfo[1];

			DeviceChannelInfo dev1 = new DeviceChannelInfo();
			dev1.setNChannel(0);
			dev1.setStDevName("");
			dev1.setNGroupID(0);
			Address address1 = new Address();
			address1.setNPort(new UnsignedShort(8000));
			address1.setStrIP("172.17.119.69");
			dev1.setStSnapDevice(address1);
			devList[0] = dev1;

			InquireAlarmListCond stInquireAlarmListCond = new InquireAlarmListCond();
			stInquireAlarmListCond.setStDeviceChanInfo(devList);
			stInquireAlarmListCond.setUStartID(new UnsignedInt(0));
			stInquireAlarmListCond.setUEndID(new UnsignedInt(20));
			stInquireAlarmListCond.setUMinValue(new UnsignedInt(30));
			stInquireAlarmListCond.setUMaxValue(new UnsignedInt(100));
			stInquireAlarmListCond.setUMaxRecNum(new UnsignedInt(100));
			TimeRange stSnapTimeRange = new TimeRange();
			stSnapTimeRange.setStStartTime("2016-06-13 00:00:00");
			stSnapTimeRange.setStEndTime("2016-07-20 00:00:00");
			stInquireAlarmListCond.setStSnapTimeRange(stSnapTimeRange);

			stInquireAlarmListCond.setNInquireType(new UnsignedInt(0));
			stInquireAlarmListCond.setNAlarmType(new UnsignedInt(1));
			GetAlarmListsReq params = new GetAlarmListsReq();
			params.setStInquireAlarmListCond(stInquireAlarmListCond);

			GetAlarmListsRsp rsp = null;
			try {
				rsp = webServiceStub.getAlarmLists(params);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			System.out.println("nErrorCode：" + rsp.getNErrorCode());

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
}
