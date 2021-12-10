package cn.ffcs.zhsq.ypms.mscase.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.callInPerson.service.CallInPersonService;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.CaseHandler;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.DeptHandle;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.MsCase;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.ReceiveDept;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.CaseHandlerMapper;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.DeptHandleMapper;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.MsCaseMapper;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.ReceiveDeptMapper;
import cn.ffcs.zhsq.ypms.mscase.service.DeptHandleService;
import cn.ffcs.zhsq.ypms.mscase.service.MsCaseService;
import cn.ffcs.zhsq.ypms.mscase.service.ReceiveDeptService;

/**
 * @Description: 延平民生案件表模块服务实现
 * @Author: zhangzhenhai
 * @Date: 04-13 14:43:28
 * @Copyright: 2018 福富软件
 */
@Service("msCaseServiceImpl")
@Transactional
public class MsCaseServiceImpl implements MsCaseService {

	@Autowired
	private MsCaseMapper msCaseMapper; //注入延平民生案件表模块dao
	@Autowired
	private CaseHandlerMapper caseHandlerMapper; //注入延平民生案件操作员信息表模块dao
	@Autowired
	private ReceiveDeptMapper receiveDeptMapper; //注入延平民生派发单位表模块dao
	@Autowired
	private DeptHandleMapper deptHandleMapper; //注入延平民生派发单位表模块dao
	@Autowired
	private CallInPersonService callInPersonService; //注入延平民生派发单位表模块dao
	@Autowired
	private ReceiveDeptService receiveDeptService; //注入延平民生派发单位表模块dao
	@Autowired
	private DeptHandleService deptHandleService; //注入延平民生派发单位表模块dao

	/**
	 * 新增数据
	 * @param bo 延平民生案件表业务对象
	 * @return 延平民生案件表id
	 */
	@Override
	public Long insert(Map<String, Object> params_a) {
		//获取所有的参数
		String insertType = (String) params_a.get("insertType");
		MsCase bo = (MsCase) params_a.get("bo");
		DeptHandle dh = (DeptHandle) params_a.get("dh");
		UserInfo userInfo = (UserInfo) params_a.get("userInfo");//当前用户
		@SuppressWarnings("unchecked")
		List<ReceiveDept> recevieDeptList = (List<ReceiveDept>) params_a.get("recevieDeptList");//派发单位list
		
		//主叫人员信息入库
		CallInPerson callInPerson = new CallInPerson();
		callInPerson.setCpMobile(bo.getCallinNum());
		callInPerson.setCpName(bo.getCustomerName());
		callInPerson.setCpSex(bo.getCustomerGender());
		callInPerson.setCreater(userInfo.getUserId());
		Long id = callInPersonService.insertCheckMobile(callInPerson);
		
		//获取案件编号和对应的流水号
		Map<String, Object> result = getCaseNo();
		Long caseNoSerial = (Long) result.get("maxCaseNoSerial");
		String caseNo = (String) result.get("caseNo");
		bo.setCaseNo(caseNo);
		bo.setCaseNoSerial(caseNoSerial);
		
		//呼叫时间、创建人员设置，办理状态
		Date callinTime = formatStringDateTo(bo.getCallinTimeStr(),"yyyy-MM-dd HH:mm:ss");
		bo.setCallinTime(callinTime);
		bo.setCreateId(userInfo.getUserId());
		bo.setCreateOrgId(userInfo.getOrgId());
		bo.setCreateOrgCode(userInfo.getOrgCode());
		bo.setCreateTime(new Date());
		if("00".equals(insertType)){
			bo.setHandleStatus("00");//状态：（草稿）
		}else if("01".equals(insertType)){
			if("03".equals(bo.getHandleWay())){
				bo.setHandleStatus("01");//状态：（办理中）
			}else{
				bo.setHandleStatus("03");//状态：（归档）
			}
		}
		msCaseMapper.insert(bo);
		
		
		//拼装操作员操作信息入库
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userInfo", userInfo);
		params.put("handleType", "01");
		if("00".equals(insertType)){
			params.put("handleType", "00");//保存草稿
		}else if("01".equals(insertType)){
			if("03".equals(bo.getHandleWay())){
				params.put("handleType", "01");//派发
			}else{
				params.put("handleType", "08");//归档
			}
		}
		
		params.put("relaCaseId", bo.getCaseId());
		this.insertCaseHandler(params);
		
		//派发单位数据入库
		for (ReceiveDept receiveDept : recevieDeptList) {
			receiveDept.setCreateId(userInfo.getUserId());
			receiveDept.setCreateTime(new Date());
			if("00".equals(insertType)){
				receiveDept.setHandleStatus("00");//保存草稿
			}else if("01".equals(insertType)){
				receiveDept.setHandleStatus("01");//派发下去，受理中状态
			}
			
			receiveDept.setRelaCaseId(bo.getCaseId());
			receiveDeptMapper.insert(receiveDept);
		}
		//receiveDeptMapper.insertBatch(recevieDeptList);
		
		//指挥中心处理过程，处理结果入库
		String handleProcess = dh.getHandleProcess();
		if(!StringUtils.isEmpty(handleProcess)){
			dh.setRdName(userInfo.getOrgName());
			dh.setRdOrgCode(userInfo.getOrgCode());
			dh.setCreateId(userInfo.getUserId());
			dh.setCreateName(userInfo.getUserName());
			dh.setCreateTime(new Date());
			dh.setRelacaseId(bo.getCaseId());
			deptHandleMapper.insert(dh);
		}
		return bo.getCaseId();
	}

	/**
	 * 修改数据
	 * @param bo 延平民生案件表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(Map<String, Object> params_a) {
		MsCase bo = (MsCase) params_a.get("bo");
		DeptHandle dh = (DeptHandle) params_a.get("dh");
		UserInfo userInfo = (UserInfo) params_a.get("userInfo");//当前用户
		String updateType = (String) params_a.get("updateType");//更新类型
		String menuType = (String) params_a.get("menuType");//更新类型
		List<ReceiveDept> recevieDeptList = (List<ReceiveDept>) params_a.get("recevieDeptList");//派发单位list
		
		//
		Map<String, Object> params_h = new HashMap<String, Object>();
		params_h.put("userInfo", userInfo);
		params_h.put("relaCaseId", bo.getCaseId());
		
		Map<String, Object> params_d = new HashMap<String, Object>();
		params_d.put("deptOrgCode", userInfo.getOrgCode());
		params_d.put("relaCaseId", bo.getCaseId());
		params_d.put("ldType", "0");
		
		//根据更新类型：中心重新派发，反馈，回访，归档，插入不同的操作员操作类型，并且更新派发单位表(先删后增)
		if ("01".equals(updateType)) {//中心重新派发
			//更新派发单位表(先删后增)
			Long count = receiveDeptMapper.deleteByRelaCaseId(bo.getCaseId());
			for (ReceiveDept receiveDept : recevieDeptList) {
				receiveDept.setCreateId(userInfo.getUserId());
				receiveDept.setCreateTime(new Date());
				receiveDept.setHandleStatus("01");//派发下去，受理中状态
				receiveDept.setRelaCaseId(bo.getCaseId());
				receiveDeptMapper.insert(receiveDept);
			}
			
			//插入操作员操作信息
			params_h.put("handleType", "06");
			insertCaseHandler(params_h);
			//案件状态
			bo.setHandleStatus("01");
			
		}else if ("02".equals(updateType)) {//委办局处理反馈
			//更新派发单位信息
			ReceiveDept receiveDept = receiveDeptMapper.searchByParams(params_d);
			if(receiveDept != null){
				receiveDept.setHandleStatus("02");
				Long updateCount = receiveDeptMapper.update(receiveDept);
			
				//插入派发单位处理过程处理结果记录
				//(第一次反馈时插入，第二次修改后 反馈时修改原来的记录)
				Map<String, Object> params_f = new HashMap<String, Object>();
				params_f.put("rdId", receiveDept.getRdId());
				List<DeptHandle> list = deptHandleMapper.searchList(params_f, new RowBounds());
				int listSize = list.size();
				dh.setCurrStatus("01");
				if(listSize == 0){
					dh.setRdId(receiveDept.getRdId());
					dh.setRdName(receiveDept.getLdName());
					dh.setRdOrgCode(receiveDept.getOrgCode());
					dh.setCreateId(userInfo.getUserId());
					dh.setCreateName(userInfo.getUserName());
					dh.setCreateTime(new Date());
					dh.setRelacaseId(bo.getCaseId());
					deptHandleMapper.insert(dh);
				}else{
					dh.setUpdateId(userInfo.getUserId());
					dh.setUpdateTime(new Date());
					dh.setRdhId(list.get(0).getRdhId());
					deptHandleMapper.update(dh);
				}
			}	
			
			//插入操作员操作信息
			params_h.put("handleType", "02");
			insertCaseHandler(params_h);
			
			//案件状态(判断所有委办局都办理完就设置为：回访中)
			Map<String, Object> params_c = new HashMap<String, Object>();
			params_c.put("relaCaseId", bo.getCaseId());
			Long count = receiveDeptMapper.countUnfinishDept(params_c);//联动队伍未办理的数量
			if (count == 0L){
				bo.setHandleStatus("02");
			}
			
			
			
		}else if ("03".equals(updateType)) {//归档
			
			if ("untreated".equals(menuType)){
			//插入派发单位处理过程处理结果记录
				String handleProcess = dh.getHandleProcess();
				if(!StringUtils.isEmpty(handleProcess)){
					dh.setRdName(userInfo.getOrgName());
					dh.setRdOrgCode(userInfo.getOrgCode());
					dh.setCreateId(userInfo.getUserId());
					dh.setCreateName(userInfo.getUserName());
					dh.setCreateTime(new Date());
					dh.setRelacaseId(bo.getCaseId());
					deptHandleMapper.insert(dh);
				}
			//修改将所有的派发单位的办理状态为中心代办理状态
				Map<String, Object> params_f = new HashMap<String, Object>();
				params_f.put("relaCaseId", bo.getCaseId());
				params_f.put("ldType", "0");
				
				String statusListStr = "01,03";//派发单位的处理状态
				params_f.put("statusList",Arrays.asList(statusListStr.split(",")));
				
				List<ReceiveDept> tempDeptlist = receiveDeptMapper.searchListByParams(params_f);
				for (ReceiveDept receiveDept : tempDeptlist) {
					receiveDept.setHandleStatus("04");
					Long updateCount = receiveDeptMapper.update(receiveDept);
				}
				
			}
			
			//插入操作员操作信息
			params_h.put("handleType", "08");
			insertCaseHandler(params_h);
			//案件状态(待归档)
			bo.setHandleStatus("03");
		}
//		else if ("06".equals(updateType)) {//被驳回后再派发
//			Long count = receiveDeptMapper.deleteByRelaCaseId(bo.getCaseId());
//			for (ReceiveDept receiveDept : recevieDeptList) {
//				receiveDept.setCreateId(userInfo.getUserId());
//				receiveDept.setCreateTime(new Date());
//				receiveDept.setHandleStatus("01");//派发下去，受理中状态
//				receiveDept.setRelaCaseId(bo.getCaseId());
//				receiveDeptMapper.insert(receiveDept);
//			}
//			
//			//插入操作员操作信息
//			params_h.put("handleType", "06");
//			insertCaseHandler(params_h);
//			//案件状态
//			bo.setHandleStatus("01");
//			
//		}
		
		
		
		//更新案件基础信息表
		long result = msCaseMapper.update(bo);
		
		
		return result > 0;
	}
	
	
	@Override
	public boolean updateSimple(MsCase bo) {
		long result = msCaseMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 延平民生案件表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(MsCase bo) {
		long result = msCaseMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生案件表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<MsCase> list = msCaseMapper.searchList(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = msCaseMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	
	@Override
	public EUDGPagination searchListForWait(int page, int rows,Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<MsCase> list = msCaseMapper.searchListForWait(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = msCaseMapper.countListForWait(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	

	@Override
	public EUDGPagination searchListForHanlde(int page, int rows,Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<MsCase> list = msCaseMapper.searchListForHanlde(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = msCaseMapper.countListForHanlde(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public EUDGPagination searchListForUntreated(int page, int rows,
			Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<MsCase> list = msCaseMapper.searchListForUntreated(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = msCaseMapper.countListForUntreated(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生案件表id
	 * @return 延平民生案件表业务对象
	 */
	@Override
	public MsCase searchById(Long id) {
		MsCase bo = msCaseMapper.searchById(id);
		formatBo(bo, "yyyy-MM-dd HH:mm:ss");
		return bo;
	}

	@Override
	public MsCase searchByParams(Map<String, Object> params_a) {
		MsCase bo = msCaseMapper.searchByParams(params_a);
		formatBo(bo, "yyyy-MM-dd HH:mm:ss");
		return bo;
	}
	
	/**
	 * 实体对象格式化
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:30:55
	 * @param @param bo
	 * @param @param dateSdf
	 * @param @return    
	 * @return msCase
	 */
	public MsCase formatBo(MsCase bo,String dateSdf){
		
		//时间格式转换
		Date callinTime = bo.getCallinTime();
		Date createTime = bo.getCreateTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String callinTimeStr = "";
		if(callinTime != null){
			callinTimeStr = sdf.format(callinTime);
		}
		
		String createTimeStr = "";
		if(createTime != null){
			createTimeStr = sdf.format(createTime);
		}
		
		bo.setCallinTimeStr(callinTimeStr);
		bo.setCreateTimeStr(createTimeStr);
		return bo;
		
	}
	/**
	 * list格式化
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:31:05
	 * @param @param list
	 * @param @param dateSdf
	 * @param @return    
	 * @return List<msCase>
	 */
	public List<MsCase> formatListBo(List<MsCase> list,String dateSdf){
		
		//时间格式转换
		for (MsCase msCase : list) {
			formatBo(msCase, dateSdf);
		}
		return list;
		
	}
	
	/**
	 * 时间格式化(date===>String)
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:31:05
	 * @param @param Date
	 * @param @param dateSdf
	 * @param @return    
	 * @return String
	 */
	public String formatDateToString(Date paramDate,String dateSdf){
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String dateStr = "";
		if(paramDate != null){
			dateStr = sdf.format(paramDate);
		}
		return dateStr;
	}
	
	/**
	 * 时间格式化(String===>date)
	 * @author zhangzhenhai
	 * @date 2018-4-24 下午5:28:04
	 * @param @param dateSdf
	 * @param @param date
	 * @param @return    
	 * @return Date
	 */
	public Date formatStringDateTo(String date,String dateSdf) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	/**
	 * 生成案件号
	 * @author zhangzhenhai
	 * @date 2018-4-23 下午4:39:13
	 * @param @return    
	 * @return String
	 */
	public Map<String, Object> getCaseNo(){
		Map<String, Object> result = new HashMap<String, Object>();
		
		//获取流水号
		Map<String, Object> params = new HashMap<String, Object>();
		String currDate = formatDateToString(new Date(), "yyyy-MM-dd");
		params.put("currDate", currDate);
		Long maxCaseNoSerial = getMaxCaseNoSerial(params);
		String caseNoSerialStr=(new DecimalFormat("000")).format(maxCaseNoSerial+1);
		
		//编号头部
		String caseNoStr = "YP"+formatDateToString(new Date(), "yyyyMMdd");
		
		String caseNo = caseNoStr + caseNoSerialStr;
		result.put("caseNo", caseNo);
		result.put("maxCaseNoSerial", maxCaseNoSerial+1);
		return result;
	}
	
	/**
	 * 获取当天最大的流水号
	 * @author zhangzhenhai
	 * @date 2018-4-23 下午5:20:25
	 * @param @param params
	 * @param @return    
	 * @return Long
	 */
	public Long getMaxCaseNoSerial(Map<String, Object> params) {
		Long maxCaseNoSerial = msCaseMapper.getMaxCaseNoSerial(params);
		if(maxCaseNoSerial == null){
			maxCaseNoSerial = 0L;
		}
		return maxCaseNoSerial;
	}
	
	/**
	 * 插入操作员操作信息
	 * @author zhangzhenhai
	 * @date 2018-4-23 下午5:20:25
	 * @param @param Map<String, Object> params :handleContent(操作意见)；handleType(操作类型);relaCaseId(关联案件);userInfo(当前用户)
	 * @return Long
	 */
	@Override
	public Long insertCaseHandler(Map<String, Object> params) {
		String handleContent = (String) params.get("handleContent");
		String handleType = (String) params.get("handleType");
		Long relaCaseId = (Long) params.get("relaCaseId");
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		
		CaseHandler caseHandler = new CaseHandler();
		
		caseHandler.setHandlerId(userInfo.getUserId());
		caseHandler.setHandlerName(userInfo.getUserName());
		caseHandler.setHandlerOrgId(userInfo.getOrgId());
		caseHandler.setHandlerOrgCode(userInfo.getOrgCode());
		caseHandler.setHandlerTel(userInfo.getVerifyMobile());
		caseHandler.setHandleTime(new Date());
		
		caseHandler.setHandleType(handleType);
		caseHandler.setRelaCaseId(relaCaseId);
		caseHandler.setHandleContent(handleContent);
		
		caseHandlerMapper.insert(caseHandler);
		
		return caseHandler.getChId();
	}

	@Override
	public boolean isArchiver(MsCase bo) {
		Map<String, Object> params_d = new HashMap<String, Object>();
		params_d.put("relaCaseId", bo.getCaseId());
		params_d.put("ldType", "0");
		params_d.put("handleStatus", "01");
		List<ReceiveDept> list = receiveDeptMapper.searchListByParams(params_d);
		
		if(list.size() > 0){
			return false;
		}else{
			return true;
		}
		
	}

	@Override
	public boolean deptReject(Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		CaseHandler caseHandler = (CaseHandler) params.get("caseHandler");
		
		//设置操作员操作类型
		caseHandler.setHandleType("03");//驳回
		
		//更新联动单位的办理状态
		Map<String, Object> params_d = new HashMap<String, Object>();
		params_d.put("deptOrgCode", userInfo.getOrgCode());
		params_d.put("relaCaseId", caseHandler.getRelaCaseId());
		params_d.put("ldType", "0");
		
		ReceiveDept receiveDept = receiveDeptService.searchByParams(params_d);
		if( receiveDept != null){
			receiveDept.setHandleStatus("03");
			receiveDeptService.update(receiveDept);
		}
		
		
		//更新案件办理状态
		Map<String, Object> params_a = new HashMap<String, Object>();
		params_a.put("caseId", caseHandler.getRelaCaseId());
		MsCase msCase = this.searchByParams(params_a);
		msCase.setHandleStatus("04");
		boolean resultUpdate = this.updateSimple(msCase);
		
		return true;
	}

	@Override
	public boolean centerRegresses(Map<String, Object> params) {
		HttpServletRequest request = (HttpServletRequest) params.get("request");
		CaseHandler caseHandler = (CaseHandler) params.get("caseHandler");
		
		
		//回退的派发单位主键id数组
		String rdIdStr = request.getParameter("rdIdStr");
		List<Long> rdIdList = new ArrayList<Long>();
		if (!StringUtils.isEmpty(rdIdStr)) {
			List<String> rdIdStrList = Arrays.asList(rdIdStr.split(","));
			for (String rdId : rdIdStrList) {
				rdIdList.add(Long.parseLong(rdId));
			}
		}
		
		//设置操作员操作类型
		caseHandler.setHandleType("05");//回退
		
		//更新联动单位的办理状态
		Map<String, Object> params_e = new HashMap<String, Object>();
		params_e.put("rdIdList", rdIdList);
		List<ReceiveDept> receiveDeptlist = receiveDeptService.searchListByParams(params_e);
		for (ReceiveDept receiveDept : receiveDeptlist) {
			receiveDept.setHandleStatus("01");
			receiveDeptService.update(receiveDept);
		}
		
		//修改处理过程表的当前状态为退回状态
		DeptHandle deptHandle = new DeptHandle();
		Long rdId = rdIdList.get(0);
		deptHandle.setRdId(rdId);
		deptHandle.setRelacaseId(caseHandler.getRelaCaseId());
		deptHandle.setCurrStatus("02");//回退状态
		boolean updateResult = deptHandleService.updateByMore(deptHandle);
		
		//更新案件办理状态
		Map<String, Object> params_a = new HashMap<String, Object>();
		params_a.put("caseId", caseHandler.getRelaCaseId());
		MsCase msCase = this.searchByParams(params_a);
		msCase.setHandleStatus("01");
		boolean resultUpdate = this.updateSimple(msCase);
		
		return true;
	}

	

	
	
	
}