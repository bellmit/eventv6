/**
 * 
 */
package cn.ffcs.zhsq.base.local.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.log.api.bo.DataLog;
import cn.ffcs.log.api.bo.Pagination;
import cn.ffcs.log.api.service.LogDataService;
import cn.ffcs.log.api.service.LogService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.base.local.service.IGridLogService;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.CookieUtils;
import net.sf.json.JSONObject;

/**
 * 综治网格日志服务实现类
 * @author guohh
 *
 */
@Service(value="gridLogServiceImpl")
public class GridLogServiceImpl implements IGridLogService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogService logService;
	@Autowired
	private LogDataService logDataService;

	@Override
	public boolean saveLog(HttpServletRequest request, UserInfo userInfo, String action, String modelCode, String tableName, Long recordId,
			String memoInfo, String valueBefore, String valueAfter, String operaId) {
		try {
			DataLog dataLog = new DataLog();
			dataLog.setChangeStatus(ConstantValue.CHANGE_LOG_STATUS_DEFAULT);
			dataLog.setAction(action);
			dataLog.setMemoInfo(memoInfo);
			dataLog.setAppCode(ConstantValue.CHANGE_LOG_APP_CODE);
			dataLog.setChangeTime(Calendar.getInstance().getTime());
			dataLog.setChangeValeAfter(valueAfter);
			dataLog.setChangeValeBefore(valueBefore);
			dataLog.setModelCode(modelCode);
			dataLog.setOperateId(operaId);
			dataLog.setRecordId(String.valueOf(recordId));
			dataLog.setTableName(tableName);
			dataLog.setOrgCode(userInfo.getOrgCode());
			dataLog.setUserId(userInfo.getUserId());
			dataLog.setUserName(userInfo.getUserName());
			logger.info("记录日志开始-----------------------------");
			if(request!=null) {
				dataLog.setOperateIp(CommonFunctions.getRemoteIp(request));
				if (CookieUtils.getCookie(request, ConstantValue.UAM_COOKIE_TOKEN_KEY) != null) {
					String token = CookieUtils.getCookie(request, ConstantValue.UAM_COOKIE_TOKEN_KEY).getValue();
					if (token == null || "".equals(token)) {
						logger.info("cookies有认证信息存在---------------------->但是token解析失败");
					} else {
						try {
							dataLog.setOrgCode(userInfo.getOrgCode());
							dataLog.setUserId(userInfo.getUserId());
							dataLog.setToken(token);
							dataLog.setUserName(userInfo.getUserName());
						} catch (Exception e) {
							logger.info("记录日志，统一认证Cookie参数传入有误");
						}
					}
	    		} else {
					logger.info("记录日志，统一认证Cookie中没有认证信息存在---------------------->");
		    	}
			}
			logService.saveDataChangeLog(dataLog);
			logger.info("记录日志结束-----------------------------");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean saveLog(HttpServletRequest request, UserInfo userInfo, String action, String modelCode, String tableName, Long recordId,
			String valueBefore, String valueAfter, String operaId) {
		try {
			DataLog dataLog = new DataLog();
			dataLog.setChangeStatus(ConstantValue.CHANGE_LOG_STATUS_DEFAULT);
			dataLog.setAction(action);
			dataLog.setMemoInfo(tableName+"--"+recordId);
			dataLog.setAppCode(ConstantValue.CHANGE_LOG_APP_CODE);
			dataLog.setChangeTime(Calendar.getInstance().getTime());
			dataLog.setChangeValeAfter(valueAfter);
			dataLog.setChangeValeBefore(valueBefore);
			dataLog.setModelCode(modelCode);
			dataLog.setOperateId(operaId);
			dataLog.setRecordId(String.valueOf(recordId));
			dataLog.setTableName(tableName);
			dataLog.setOrgCode(userInfo.getOrgCode());
			dataLog.setUserId(userInfo.getUserId());
			dataLog.setUserName(userInfo.getUserName());
			logger.info("记录日志开始-----------------------------");
			if(request!=null) {
				dataLog.setOperateIp(CommonFunctions.getRemoteIp(request));
				if (CookieUtils.getCookie(request, ConstantValue.UAM_COOKIE_TOKEN_KEY) != null) {
					String token = CookieUtils.getCookie(request, ConstantValue.UAM_COOKIE_TOKEN_KEY).getValue();
					if (token == null || "".equals(token)) {
						logger.info("cookies有认证信息存在---------------------->但是token解析失败");
					} else {
						try {
							dataLog.setOrgCode(userInfo.getOrgCode());
							dataLog.setUserId(userInfo.getUserId());
							dataLog.setToken(token);
							dataLog.setUserName(userInfo.getUserName());
						} catch (Exception e) {
							logger.info("记录日志，统一认证Cookie参数传入有误");
						}
					}
	    		} else {
					logger.info("记录日志，统一认证Cookie中没有认证信息存在---------------------->");
		    	}
			}
			logService.saveDataChangeLog(dataLog);
			logger.info("记录日志结束-----------------------------");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 保存日志2
	 * @param request
	 * @param logModule
	 * @param action
	 * @param recordId
	 * @param valueBefore
	 * @param valueAfter
	 * @return
	 */
	@Override
	public boolean savelog2(HttpServletRequest request, LogModule logModule, ActionType action, 
			Long recordId, String memoInfo, String valueBefore, String valueAfter){
		String operaId = UUID.randomUUID().toString().replaceAll("-", "");
		UserInfo userInfo = null;
		HttpSession session = request.getSession();
		if(session!=null) userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		return this.saveLog(request, userInfo, action.getValue(), logModule.getModuleCode(), logModule.getTableName(), 
				recordId, memoInfo, valueBefore, valueAfter, operaId);
	}
	
	/**
	 * 保存日志3
	 * @param request
	 * @param logModule
	 * @param action
	 * @param recordId
	 * @param valueBefore
	 * @param valueAfter
	 * @return
	 */
	@Override
	public boolean savelog3(HttpServletRequest request, LogModule logModule,
			ActionType action, Long recordId, String memoInfo, Object valueBefore,
			Object valueAfter) {
		
		String operaId = UUID.randomUUID().toString().replaceAll("-", "");
		UserInfo userInfo = null;
		HttpSession session = request.getSession();
		String valueBeforeStr = JSONObject.fromObject(valueBefore).toString();
		String valueAfterStr = JSONObject.fromObject(valueAfter).toString();
		if(session!=null) userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		return this.saveLog(request, userInfo, action.getValue(), logModule.getModuleCode(), logModule.getTableName(), 
				recordId, memoInfo, valueBeforeStr, valueAfterStr, operaId);
	}
	
	/**
	 * 查询日志
	 * @param criteria
	 * @param page
	 * @return
	 */
	public Pagination dataChangeLogQuery(HashMap<String,Object> criteria, Pagination page){
		criteria.put("changeStatus", ConstantValue.CHANGE_LOG_STATUS_DEFAULT);
		criteria.put("appCode", ConstantValue.CHANGE_LOG_APP_CODE);
		if(criteria.containsKey("modelCode") && !criteria.containsKey("tableName")){
			String modelCode = criteria.get("modelCode")+"";
			LogModule module = LogModule.getLogModuleByCode(modelCode);
			if(module!=null)  criteria.put("tableName", module.getTableName());
		}
		return logDataService.queryUserOperatePage(criteria, page);
	}


}
