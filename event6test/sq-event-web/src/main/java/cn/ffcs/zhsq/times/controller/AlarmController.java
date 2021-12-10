package cn.ffcs.zhsq.times.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.doorsys.bo.equipment.AfsLog;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.alarm.IAlarmService;
import cn.ffcs.zhsq.mybatis.domain.alarm.AlarmLog;
import cn.ffcs.zhsq.mybatis.domain.alarm.FaceRecordInfo;
import cn.ffcs.zhsq.mybatis.domain.alarm.SnapRecordInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/zhsq/alarm")
public class AlarmController {

	@Autowired
	private IAlarmService alarmService;
	
	@RequestMapping(value = "/index")
	public String index(HttpSession session,
			HttpServletRequest request, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String data = request.getParameter("data");
		JSONObject jobj = JSONObject.fromObject(data);
		SnapRecordInfo o = (SnapRecordInfo)JSONObject.toBean(jobj, SnapRecordInfo.class);
		System.out.println(o.getEquName());
		return "/map/messager/index.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getData")
	public SnapRecordInfo getData(HttpSession session,
			HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
//		SnapRecordInfo snapRecordInfo = new SnapRecordInfo();
		SnapRecordInfo snapRecordInfo = alarmService.getAlarmDatas(beginTime, endTime);
		
//		SnapRecordInfo snapRecordInfo = new SnapRecordInfo();
//		snapRecordInfo.setStrName("捕获对象");
//		snapRecordInfo.setStrIP("抓拍地点");
//		snapRecordInfo.setEquName("人脸识别设备");
//		snapRecordInfo.setStSnapTime("2016-08-12 15:08:26");
//		FaceRecordInfo[] faceRecordInfo = new FaceRecordInfo[5];
//		FaceRecordInfo f = new FaceRecordInfo();
//		f.setStrName("姓名1");
//		f.setnAge(22);
//		f.setnGlassOn("是");
//		f.setnSimilarity(88);
//		f.setStSex("男");
//		faceRecordInfo[0] = f;
//		snapRecordInfo.setFaceRecordInfos(faceRecordInfo);
		
//		List<AlarmLog> afsLogs = new ArrayList<AlarmLog>();
//		AlarmLog afsLog = new AlarmLog();
//		afsLog.setStrName("aaa");
//		afsLog.setStSnapTime(new Date());
//		afsLog.setEqpName("设备名称");
//		afsLog.setnGlassOn("是");
//		afsLogs.add(afsLog);
//		AfsLog a = new AfsLog();
//		PropertyUtils.copyProperties(a, afsLog);
//		System.out.println(a.getnGlassOn());
		return snapRecordInfo;
	}
}
