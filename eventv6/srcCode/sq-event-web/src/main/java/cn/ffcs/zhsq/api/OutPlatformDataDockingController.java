package cn.ffcs.zhsq.api;

import cn.ffcs.common.FileUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventDisposal;
import cn.ffcs.shequ.zzgl.service.event.IEventDisposalService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 张天慈 on 2018/1/19.
 * 12345平台，城市啄木鸟平台等外系统事件数据对接
 */
@Controller
@RequestMapping("/service/event")
public class OutPlatformDataDockingController extends ZZBaseController{
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IBaseDictionaryService dictionaryService;

	SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 手机号码验证
	 */
	private static final String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

	/**
	 * 外平台采集事件 12345平台，啄木鸟平台...
	 * @param session
	 * @param event  事件
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/outplatformSave",produces = "application/json;charset=UTF-8")
	public String outplatformSave(
			HttpSession session, HttpServletRequest request, EventDisposal event, ModelMap map)throws JSONException,IOException {

		//保存结果："保存成功！" : "保存失败！"
		JSONObject resultJson = new JSONObject();

		String jsonStr = null;
		try{
			jsonStr = FileUtils.readData(request.getInputStream(),request.getCharacterEncoding());
		}catch (IOException e){
			e.printStackTrace();
		}

		if(StringUtils.isBlank(jsonStr)){
			resultJson.put("status", 0);
			resultJson.put("desc", "数据格式有误,参数为空!");
			return resultJson.toString();
		}

		JSONObject jsonObject = JSONObject.fromObject(jsonStr);//--jsonObject判空！
		if(jsonObject == null){
			resultJson.put("status", 0);
			resultJson.put("desc", "参数为空");
		}else{
			String check = check(jsonObject);
			if(!check.equals("{}")){
				return check;
			}
		}

		String happenTimeStr = (jsonObject.get("happenTimeStr")).toString();
		Date happenTime = null;
		try {
			happenTime = sdf.parse(happenTimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		event.setType(jsonObject.get("type").toString());
		//event.setType(request.getParameter("type"));
		event.setHappenTime(happenTime);
		event.setReporter(jsonObject.get("reporter").toString());
		event.setUrgencyDegree(jsonObject.get("urgencyDegree").toString());
		event.setInfluenceDegree(jsonObject.get("influenceDegree").toString());
		event.setSource(jsonObject.get("source").toString());
		event.setTelephone(jsonObject.get("telephone").toString());
		event.setOccurred(jsonObject.get("occurred").toString());
		event.setContent(jsonObject.get("content").toString());
		event.setResult(jsonObject.get("result").toString());

		event.setCode(formate.format(new Date()));
		event.setCollectWay(ConstantValue.COLLECT_WAY_PC);//录入方式
		Date now = Calendar.getInstance().getTime();
		event.setUpdateDate(now);
		event.setCreateDate(now);
		event.setStatus(ConstantValue.EVENT_STATUS_RECEIVED);
		//获取事件级别
		Integer level = mixedGridInfoService.getGridLevelByOrgCodeOrGridId(null,event.getGridId());
		event.setLevel(level);
		event.setGridId(120458l);//测试用(用完删除)，默认event Grid_Id

		//任务单初始值
		//event.getTaskInfo().setEventId(event.getEventId());//
		event.getTaskInfo().setTaskCode(ConstantValue.TASK_CODE_COLLECT);
		event.getTaskInfo().setTaskName(ConstantValue.TASK_NAME_COLLECT);
		event.getTaskInfo().setTaskType("01");// 主办、从办
		event.getTaskInfo().setParentId(0L);
		event.getTaskInfo().setStatus("03");// 状态：已结案 状态：受理(00)、上报未分流(01)、上报已分流(02)、结案(03)、（结档归案）结束(04),挂起(05),已删除(06)*
		event.getTaskInfo().setIsRead("1");// 已读
		event.getTaskInfo().setReadDate(new Date());//接收时间
		event.getTaskInfo().setCreateDate(new Date());
		event.getTaskInfo().setGridId(event.getGridId());

		// 事件表(如果为0205,0207,0602,还要增加任务表和关联表)
		Long recordId = eventDisposalService.saveEventDisposalReturnId(event);//--！判断是否保存成功

		/*map.addAttribute("result", recordId > 0 ? "添加成功！" : "添加失败！");
		map.put("type", "save");*/

		//status 0 保存成功，1 保存失败;desc 保存成功，保存失败
		resultJson.put("status",recordId > 0 ? "1" : "0");
		resultJson.put("desc",recordId > 0 ? "保存成功" : "保存失败");//0--1
		System.out.println(resultJson);
		return resultJson.toString();
	}

	//新增外平台事件时判断关键字段是否为空,字段长度是否超出限制,相关字段格式是否正确
	private String check(JSONObject jsonObject)throws JSONException{

		//记录操作结果
		JSONObject resultObject = new JSONObject();
		//参数有无异常判断标志
		Boolean flag = true;
		//记录参数具体错误信息
		JSONObject recordMsg = new JSONObject();

		//--！类型验证，字典验证类型是否存在！验证所有字段长度，所有参数验证报错信息一次返回
		if(jsonObject.get("type") == null){
			flag = false;
			recordMsg.put("type","[type]事件类型不能为空");
		}else {
			String type = jsonObject.get("type").toString();
			byte[] typeLength = type.getBytes();
			if(typeLength.length > 10){
				flag = false;
				recordMsg.put("type","[type]事件类型长度超出限制");
			}

			//验证字典中事件类型是否存在
			Map<String,Object> params = new HashMap();
			params.put("dictPcode",ConstantValue.BIG_TYPE_PCODE);
			//List<BaseDataDict> dataList = this.dictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE,null);
			//根据父节点dictPcode获取所有子节点
			List<BaseDataDict> dataList = this.dictionaryService.findDataDictListByCodes(params);
			for(BaseDataDict baseDataDict : dataList){
				flag = true;
				//若字典中存在该事件类型,结束循环
				if(type.equals(baseDataDict.getDictGeneralCode())){
					break;
				}else{
					flag = false;
				}
				//System.out.println(baseDataDict.getDictGeneralCode()+"--");
			}
			//字典当中不存在事件类型
			if(flag == false){
				recordMsg.put("NoDictType","[type]事件类型字典业务编码不存在");
			}

		}

		//判断时间是否为空
		if(jsonObject.get("happenTimeStr") == null ){
			flag = false;
			recordMsg.put("happenTimeStr","[happenTimeStr]发生时间不能为空");
		}

		//时间不为空，判断时间格式 yyyy-MM-dd HH:mm:ss
		if(jsonObject.get("happenTimeStr") != null){
			String happenTimeStr = (jsonObject.get("happenTimeStr")).toString();

			try{
				Date date = sdf.parse(happenTimeStr);
			}catch (ParseException e){
				flag = false;
				recordMsg.put("happenTimeStr","[happenTimeStr]发生时间格式错误");
				e.printStackTrace();
			}
		}

		if(jsonObject.get("reporter") == null){
			flag = false;
			recordMsg.put("reporter","[reporter]反馈人员不能为空");
		}else {
			byte[] reporter = jsonObject.get("reporter").toString().getBytes();
			if(reporter.length > 24 ){
				flag = false;
				recordMsg.put("reporter","[reporter]反馈人员长度超出限制");
			}
		}

		if(jsonObject.get("urgencyDegree") == null){
			flag = false;
			recordMsg.put("urgencyDegree","[urgencyDegree]紧急程度不能为空");
		}else {
			byte[] urgencyDegree = jsonObject.get("urgencyDegree").toString().getBytes();
			if(urgencyDegree.length > 2){
				flag = false;
				recordMsg.put("urgencyDegree","[urgencyDegree]紧急程度长度超出限制");
			}
		}

		if(jsonObject.get("influenceDegree") == null){
			flag = false;
			recordMsg.put("influenceDegree","[influenceDegree]影响范围不能为空");
		}else {
			byte[] influenceDegree = jsonObject.get("influenceDegree").toString().getBytes();
			if(influenceDegree.length > 2){
				flag = false;
				recordMsg.put("influenceDegree","[influenceDegree]影响范围长度超出限制");
			}
		}

		if(jsonObject.get("source") == null){
			flag = false;
			recordMsg.put("source","[source]信息来源不能为空");
		}else {
			byte[] source = jsonObject.get("source").toString().getBytes();
			if(source.length > 2){
				flag = false;
				recordMsg.put("source","[source]信息来源长度超出限制");
			}
		}

		if(jsonObject.get("telephone") == null){
			flag = false;
			recordMsg.put("telephone","[telephone]联系电话不能为空");
		}

		if(jsonObject.get("telephone") != null){
			String telephone = jsonObject.get("telephone").toString();
			if(!telephone.matches(PHONE_NUMBER_REG)){
				flag = false;
				recordMsg.put("telephone","[telephone]联系电话格式错误");
			}
		}

		if(jsonObject.get("occurred") == null){
			flag = false;
			recordMsg.put("occurred","[occurred]事发祥址不能为空");
		}else {
			byte[] occurred = jsonObject.get("occurred").toString().getBytes();
			if(occurred.length > 128){
				flag = false;
				recordMsg.put("occurred","[occurred]事发祥址长度超出限制");
			}
		}

		if(jsonObject.get("content") == null){
			flag = false;
			recordMsg.put("content","[content]事件描述不能为空");
		}else {
			byte[] content = jsonObject.get("content").toString().getBytes();
			if(content.length > 2048){
				flag = false;
				recordMsg.put("content","[content]事件描述长度超出限制");
			}
		}

		if(jsonObject.get("result") == null){
			flag = false;
			recordMsg.put("result","[result]处理结果不能为空");
		}else {
			byte[] result = jsonObject.get("result").toString().getBytes();
			if(result.length > 256){
				flag = false;
				recordMsg.put("result","[result]处理结果长度超出限制");
			}
		}

		if(!flag){
			resultObject.put("status",0);
			resultObject.put("desc","操作失败");
			resultObject.put("data",recordMsg.toString());
		}

		//System.out.println(resultObject);

		return resultObject.toString();
	}

}


