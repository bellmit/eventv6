package cn.ffcs.zhsq.reportFocus.reportFeedback.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportFeedback.ReportFeedbackMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportFeedback.ReportSendMapper;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 上报信息反馈
 * 				  相关涉及表：T_EVENT_REPORT_SEND、T_EVENT_REPORT_FEEDBACK 
 * @ClassName:   ReportFeedbackServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月4日 下午3:36:41
 */
@Service(value = "reportFeedbackService")
public class ReportFeedbackServiceImpl implements IReportFeedbackService {
	@Autowired
	private ReportSendMapper reportSendMapper;
	
	@Autowired
	private ReportFeedbackMapper reportFeedbackMapper;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;

	@Autowired
	private UserManageOutService userManageService;

	/**
	 * 南安入格事件:业务类型
	 */
	public static final Map<String,String> BIZTYPE_MAP = BizType.toMap();

	/**
	 * 南安入格事件:下达状态
	 */
	public static Map<String,String> REPORT_SEND_STATUS_MAP = ReportSendStatus.toMap();

	/**
	 * 南安入格事件:接收状态
	 */
	public static Map<String,String> REPORT_READ_STATUS_MAP = ReportReadStatus.toMap();

	/**
	 * 南安入格事件:反馈状态
	 */
	public static Map<String,String> REPORT_FEEDBACK_STATUS_MAP = ReportFeedbackStatus.toMap();

	/**
	 * 南安入格事件:补充信息类型
	 */
	public static Map<String,String> REPORT_SEND_EXT_TYPE_MAP = ReportSendExtType.toMap();


	@Override
	public String saveOrUpdateReportFeedback(Map<String, Object> reportFeedback, UserInfo userInfo) throws Exception{
		Map<String,Object> param = new HashMap<>();
		this.checkReportFeedbackData(reportFeedback,param,userInfo);
		if(CommonFunctions.isNotBlank(param, "fbUUId")) {
			int rowUpdate = reportFeedbackMapper.updateMap(param);
			if (rowUpdate>0) {
				return param.get("fbUUId").toString();
			}else{
				return null;
			}
		}else{
			reportFeedbackMapper.insert(param);
			if (CommonFunctions.isNotBlank(param, "fbUUId")) {
				return param.get("fbUUId").toString();
			}
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
    public String saveOrUpdateReport(Map<String, Object> reportMap, UserInfo userInfo) throws Exception{
		String seUUId = this.saveOrUpdateReportSend(reportMap, userInfo);
		if(seUUId!=null){
			reportMap.put("seUUId",seUUId);
			String fbUUId = this.saveOrUpdateReportFeedback(reportMap, userInfo);
			if (fbUUId == null){
				throw new ZhsqEventException("下达信息新增失败！");
			}
		}
		return seUUId;
    }

	@Override
	public String findSeUUIdByParam(Map<String, Object> params) throws Exception {
		Map<String,Object> param = new HashMap<>();
		//业务类型
		if (CommonFunctions.isNotBlank(params, "bizType")) {
			String bizType = params.get("bizType").toString();
			if (CommonFunctions.isBlank(BIZTYPE_MAP,bizType)) {
				throw new ZhsqEventException("参数【业务类型bizType】"+ bizType +"不合法！");
			}
			param.put("bizType", bizType);
		}else{
			throw new ZhsqEventException("参数【业务类型bizType】不能为空！");
		}
		//业务标识
		if (CommonFunctions.isNotBlank(params, "bizSign")) {
			param.put("bizSign", params.get("bizSign"));
		}else{
			throw new ZhsqEventException("参数【业务标识bizSign】不能为空！");
		}
		//数据来源
		if (CommonFunctions.isNotBlank(params, "dataSource")) {
			param.put("dataSource", params.get("dataSource"));
		}else{
			throw new ZhsqEventException("参数【数据来源dataSource】不能为空！");
		}
		//数据标识
		if (CommonFunctions.isNotBlank(params, "dataSign")) {
			param.put("dataSign", params.get("dataSign"));
		}else{
			throw new ZhsqEventException("参数【数据标识dataSign】不能为空！");
		}
		return reportSendMapper.findSeUUIdByParam(param);
	}

    @Override
    public String saveOrUpdateReportSend(Map<String, Object> reportSend, UserInfo userInfo) throws Exception{
		Map<String,Object> param = new HashMap<>();
		this.checkReportSendData(reportSend,param,userInfo);
        if (CommonFunctions.isNotBlank(param, "seUUId")) {
			int rowUpdate = reportSendMapper.update(param);
			if (rowUpdate>0) {
				return param.get("seUUId").toString();
			}else{
				return null;
			}
        }else{
            reportSendMapper.insert(param);
			if (CommonFunctions.isNotBlank(param, "seUUId")) {
				return param.get("seUUId").toString();
			}
        }
        return null;
    }

	@Override
	public Map<String, Object> searchReportDataBySeUUId(String seUUid) {
		Map<String,Object> resultMap = new HashMap<>();
		Map<String, Object> reportSendMap = reportSendMapper.findByUUId(seUUid);
		if (!CollectionUtils.isEmpty(reportSendMap)) {
			resultMap.putAll(reportSendMap);
			this.formatResultMap(resultMap);
		}
		List<Map<String, Object>> reportFeedbackList = reportFeedbackMapper.findBySeUUId(seUUid);
		if (CollectionUtils.isEmpty(reportFeedbackList)) {
			reportFeedbackList = new ArrayList<>();
		}else{
			for (Map<String, Object> reportFeedback : reportFeedbackList) {
				this.formatResultMap(reportFeedback);
			}
		}
		resultMap.put("reportFeedbackArr",reportFeedbackList);
		return resultMap;
	}

	@Override
	public EUDGPagination findReportDataPage(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

		long count = reportSendMapper.countReportSendDataList(params);
		List<Map<String,Object>> list;
		if (count>0) {
			list = reportSendMapper.searchReportSendDataList(params, rowBounds);
            for (Map<String,Object> resultMap: list) {
                this.formatResultMap(resultMap);
            }
		}else{
			list = new ArrayList<>();
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	private void formatParamIn(Map<String, Object> params,Map<String,Object> param) throws Exception{
		if (CommonFunctions.isNotBlank(params,"fbUUId")) {
			param.put("fbUUId",params.get("fbUUId"));
		}
        if (CommonFunctions.isNotBlank(params,"fbUserId")) {
			param.put("fbUserId",params.get("fbUserId"));
        }
        if (CommonFunctions.isNotBlank(params,"fbOrgId")) {
			param.put("fbOrgId",params.get("fbOrgId"));
        }

		if (CommonFunctions.isNotBlank(params,"regionCode")) {
			param.put("regionCode",params.get("regionCode"));
		}
		if (CommonFunctions.isNotBlank(params,"bizType") && !param.containsKey("bizType")) {
			param.put("bizType",params.get("bizType"));
		}
		if (CommonFunctions.isNotBlank(params,"bizSign") && !param.containsKey("bizSign")) {
			param.put("bizSign",params.get("bizSign"));
		}
		if (CommonFunctions.isNotBlank(params,"bizCode")) {
			param.put("bizCode",params.get("bizCode"));
		}
		if (CommonFunctions.isNotBlank(params,"dataSource")) {
			param.put("dataSource",params.get("dataSource"));
		}
		if (CommonFunctions.isNotBlank(params,"dataSign")) {
			param.put("dataSign",params.get("dataSign"));
		}
		if (CommonFunctions.isNotBlank(params,"seStatus")) {
			param.put("seStatus",params.get("seStatus"));
		}
		if (CommonFunctions.isNotBlank(params,"reStatus")) {
			param.put("reStatus",params.get("reStatus"));
		}
		if (CommonFunctions.isNotBlank(params,"fbStatus")) {
			param.put("fbStatus",params.get("fbStatus"));
		}
		if (CommonFunctions.isNotBlank(params,"keyWord")) {
			param.put("keyWord",params.get("keyWord"));
		}
		//格式化时间参数
		if (CommonFunctions.isNotBlank(params,"beginSeTime")) {
			this.formatDate(params,"beginSeTime");
			param.put("beginSeTime",params.get("beginSeTime"));
		}
		if (CommonFunctions.isNotBlank(params,"endSeTime")) {
			this.formatDate(params,"endSeTime");
			param.put("endSeTime",params.get("endSeTime"));
		}
		if (CommonFunctions.isNotBlank(params,"beginReTime")) {
			this.formatDate(params,"beginReTime");
			param.put("beginReTime",params.get("beginReTime"));
		}
		if (CommonFunctions.isNotBlank(params,"endReTime")) {
			this.formatDate(params,"endReTime");
			param.put("endReTime",params.get("endReTime"));
		}
		if (CommonFunctions.isNotBlank(params,"beginFbTime")) {
			this.formatDate(params,"beginFbTime");
			param.put("beginFbTime",params.get("beginFbTime"));
		}
		if (CommonFunctions.isNotBlank(params,"endFbTime")) {
			this.formatDate(params,"endFbTime");
			param.put("endFbTime",params.get("endFbTime"));
		}
		if (CommonFunctions.isNotBlank(params,"beginReDeadline")) {
			this.formatDate(params,"beginReDeadline");
			param.put("beginReDeadline",params.get("beginReDeadline"));
		}
		if (CommonFunctions.isNotBlank(params,"endReDeadline")) {
			this.formatDate(params,"endReDeadline");
			param.put("endReDeadline",params.get("endReDeadline"));
		}
		if (CommonFunctions.isNotBlank(params,"beginFbDeadline")) {
			this.formatDate(params,"beginFbDeadline");
			param.put("beginFbDeadline",params.get("beginFbDeadline"));
		}
		if (CommonFunctions.isNotBlank(params,"endFbDeadline")) {
			this.formatDate(params,"endFbDeadline");
			param.put("endFbDeadline",params.get("endFbDeadline"));
		}
	}

	private void formatResultMap(Map<String, Object> result){
		if (CollectionUtils.isEmpty(result)) {
			return;
		}
		//反馈时限
		if (CommonFunctions.isNotBlank(result,"fbDeadline")) {
			if(result.get("fbDeadline") instanceof Timestamp){
				result.put("fbDeadline",DateUtils.formatDate((Date)result.get("fbDeadline"),DateUtils.PATTERN_24TIME));
			}
		}
		//反馈时间
		if (CommonFunctions.isNotBlank(result,"fbTime")) {
			if(result.get("fbTime") instanceof Timestamp){
				result.put("fbTime",DateUtils.formatDate((Date)result.get("fbTime"),DateUtils.PATTERN_24TIME));
			}
		}
		//接收时限
		if (CommonFunctions.isNotBlank(result,"reDeadline")) {
			if(result.get("reDeadline") instanceof Timestamp){
				result.put("reDeadline",DateUtils.formatDate((Date)result.get("reDeadline"),DateUtils.PATTERN_24TIME));
			}
		}
		//接收时间
		if (CommonFunctions.isNotBlank(result,"reTime")) {
			if(result.get("reTime") instanceof Timestamp){
				result.put("reTime",DateUtils.formatDate((Date)result.get("reTime"),DateUtils.PATTERN_24TIME));
			}
		}
		//下达时间
		if (CommonFunctions.isNotBlank(result,"seTime")) {
			if(result.get("seTime") instanceof Timestamp){
				result.put("seTime",DateUtils.formatDate((Date)result.get("seTime"),DateUtils.PATTERN_24TIME));
			}
		}
		//业务类型
		if (CommonFunctions.isNotBlank(result,"bizType")) {
			result.put("bizTypeStr",BIZTYPE_MAP.get(result.get("bizType")));
		}
		//数据来源
		if (CommonFunctions.isNotBlank(result,"dataSource")) {
			if ("01".equals(result.get("dataSource"))) {
				result.put("dataSourceStr","南安12345平台");
			}
		}
		//下达状态
		if (CommonFunctions.isNotBlank(result,"seStatus")) {
			result.put("seStatusStr",REPORT_SEND_STATUS_MAP.get(result.get("seStatus")));
		}
		//接收状态
		if (CommonFunctions.isNotBlank(result,"reStatus")) {
			result.put("reStatusStr",REPORT_READ_STATUS_MAP.get(result.get("reStatus")));
		}
		//反馈状态
		if (CommonFunctions.isNotBlank(result,"fbStatus")) {
			result.put("fbStatusStr",REPORT_FEEDBACK_STATUS_MAP.get(result.get("fbStatus")));
		}
	}

	@Override
	public EUDGPagination findReportFeedbackDataPage(int pageNo, int pageSize, Map<String, Object> params) {
		Map<String,Object> param = new HashMap();
		try {
			this.formatParamIn(params,param);
		} catch (Exception e) {
			e.printStackTrace();
			return new EUDGPagination();
		}
		long count = reportFeedbackMapper.countReportFeedbackDataList(param);
		List<Map<String,Object>> list=null;
		if(count>0){
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 20 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			list = reportFeedbackMapper.searchReportFeedbackDataList(param, rowBounds);
			//字典翻译 + 时间格式化
			for (Map<String,Object> reportFeedback : list) {
				this.formatResultMap(reportFeedback);
			}
		}else{
			list = new ArrayList<>();
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public List<Map<String, Object>> searchReportFeedbackList(String bizSign,String bizType, Map<String, Object> extraParam) {
		if (StringUtils.isEmpty(bizSign) || StringUtils.isEmpty(bizType)) {
			return new ArrayList<>();
		}
		Map<String, Object> param = new HashMap<>();
		param.put("bizSign",bizSign);
		param.put("bizType",bizType);
		if (!CollectionUtils.isEmpty(extraParam)) {
			try {
				this.formatParamIn(extraParam,param);
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		List<Map<String, Object>> reportFeedbackList = reportFeedbackMapper.searchReportFeedbackDataList(param);
		if (CollectionUtils.isEmpty(reportFeedbackList)) {
			reportFeedbackList= new ArrayList<>();
		}else{
			for (Map<String, Object> reportFeedback:reportFeedbackList) {
				this.formatResultMap(reportFeedback);
			}
		}
		return reportFeedbackList;
	}

	@Override
	public List<Map<String, Object>> searchReportSendExtList(String seUUId, String extType, Map<String, Object> extraParam) {
		if (StringUtils.isEmpty(seUUId) || StringUtils.isEmpty(seUUId)) {
			return new ArrayList<>();
		}
		Map<String, Object> param = new HashMap<>();
		param.put("seUUId",seUUId);
		param.put("extType",extType);
		if (!CollectionUtils.isEmpty(extraParam)) {
			try {
				this.formatParamIn(extraParam,param);
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		List<Map<String, Object>> reportSendExtList = reportSendMapper.searchReportSendExtDataList(param);
		if (CollectionUtils.isEmpty(reportSendExtList)) {
			reportSendExtList= new ArrayList<>();
		}else{
			for (Map<String, Object> extMap:reportSendExtList) {
				if (CommonFunctions.isNotBlank(extMap,"doDate")) {
					if(extMap.get("doDate") instanceof Timestamp){
						extMap.put("doDate",DateUtils.formatDate((Date)extMap.get("doDate"),DateUtils.PATTERN_24TIME));
					}
				}
			}
		}
		return reportSendExtList;
	}

	@Override
	public Long findReportSendExtCount(String seUUId, String extType, Map<String, Object> extraParam) {
		if (StringUtils.isEmpty(seUUId) || StringUtils.isEmpty(extType)) {
			return 0L;
		}
		Map<String,Object> param = new HashMap<>();
		if (!CollectionUtils.isEmpty(extraParam)) {
			param.putAll(extraParam);
		}
		param.put("seUUId",seUUId);
		param.put("extType",extType);
		return reportSendMapper.countReportSendExtDataList(param);
	}

	@Override
    public Map<String, Object> findReportFeedbackByfbUUId(String fbUUid) {
        Map<String, Object> reportFeedbackMap = reportFeedbackMapper.findByFbUUId(fbUUid);
        this.formatResultMap(reportFeedbackMap);
        return reportFeedbackMap;
    }

    @Override
	public boolean modifyReStatus(String fbUUId, UserInfo userInfo) throws Exception{
		if (userInfo == null) {
			throw new ZhsqEventException("签收用户不能为空");
		}
		Map<String, Object> reportFeedback = reportFeedbackMapper.findByFbUUId(fbUUId);
		if(CollectionUtils.isEmpty(reportFeedback)){
			throw new ZhsqEventException("反馈信息uuid【"+fbUUId+"】不存在");
		}else{
			if (userInfo.getUserId().equals(Long.valueOf(reportFeedback.get("fbUserId").toString()))
					&& userInfo.getOrgId().equals(Long.valueOf(reportFeedback.get("fbOrgId").toString()))) {
				if (ReportReadStatus.NOT_RECEIVED.getCode().equals(reportFeedback.get("reStatus"))) {
					if (CommonFunctions.isBlank(reportFeedback, "reDeadline")) {
						throw new ZhsqEventException("参数【接收时限】不存在");
					} else {
						this.formatDate(reportFeedback, "reDeadline");
					}
					Map<String, Object> param = new HashMap<>();
					param.put("fbUUId", fbUUId);
					param.put("updator", userInfo.getUserId());
					param.put("reTime", DateUtils.convertStringToDate(DateUtils.getNow(), DateUtils.PATTERN_24TIME));
					if (((Date) reportFeedback.get("reDeadline")).before((Date) param.get("reTime"))) {
						param.put("reStatus", ReportReadStatus.TIMEOUT_RECEIVED.getCode());
					} else {
						param.put("reStatus", ReportReadStatus.RECEIVED.getCode());
					}
					param.put("preReStatus", ReportReadStatus.NOT_RECEIVED.getCode());//设置修改条件为未签收的状态
					return reportFeedbackMapper.updateMap(param) > 0;
				}
			}
		}
		return false;
	}

	@Override
	public boolean modifyFbStatus(String fbUUId,String fbContent, UserInfo userInfo) throws Exception{
		if(StringUtils.isEmpty(fbUUId)){
			throw new ZhsqEventException("反馈信息fbUUId不能为空");
		}
		if(StringUtils.isEmpty(fbContent)){
			throw new ZhsqEventException("反馈内容fbContent不能为空");
		}
		if (userInfo == null) {
			throw new ZhsqEventException("反馈用户不能为空");
		}
		Map<String, Object> reportFeedback = reportFeedbackMapper.findByFbUUId(fbUUId);
		if(CollectionUtils.isEmpty(reportFeedback)) {
			throw new ZhsqEventException("反馈信息fbUUId【" + fbUUId + "】不存在");
		}else{
			if (userInfo.getUserId().equals(Long.valueOf(reportFeedback.get("fbUserId").toString()))
					&&  userInfo.getOrgId().equals(Long.valueOf(reportFeedback.get("fbOrgId").toString()))) {
                Object reStatus = reportFeedback.get("reStatus");
                if ((ReportReadStatus.RECEIVED.getCode().equals(reStatus) ||ReportReadStatus.TIMEOUT_RECEIVED.getCode().equals(reStatus))
                        && ReportFeedbackStatus.NOT_FEEDBACK.getCode().equals(reportFeedback.get("fbStatus"))) {
					if (CommonFunctions.isBlank(reportFeedback, "fbDeadline")) {
						throw new ZhsqEventException("参数【反馈时限】不存在");
					} else {
						this.formatDate(reportFeedback, "fbDeadline");
					}
					Map<String, Object> param = new HashMap<>();
					param.put("fbUUId", fbUUId);
					param.put("updator", userInfo.getUserId());
					param.put("fbContent", fbContent);
					param.put("fbTime", DateUtils.convertStringToDate(DateUtils.getNow(), DateUtils.PATTERN_24TIME));
					if (((Date) reportFeedback.get("fbDeadline")).before((Date) param.get("fbTime"))) {
						param.put("fbStatus", ReportFeedbackStatus.TIMEOUT_FEEDBACK.getCode());
					} else {
						param.put("fbStatus", ReportFeedbackStatus.FEEDBACK.getCode());
					}
					param.put("preFbStatus", ReportFeedbackStatus.NOT_FEEDBACK.getCode());//设置修改条件为未反馈的状态
					return reportFeedbackMapper.updateMap(param) > 0;
				}
			}else{
				throw new ZhsqEventException("非对应的反馈用户和反馈组织！");
			}
		}
		return false;
	}

    @Override
    public Long findReportFeedbackCount(String bizSign,String bizType, Map<String, Object> extraParam) {
        if (StringUtils.isEmpty(bizSign) || StringUtils.isEmpty(bizType)) {
            return 0L;
        }
	    Map<String,Object> param = new HashMap<>();
        if (!CollectionUtils.isEmpty(extraParam)) {
            param.putAll(extraParam);
        }
	    param.put("bizSign",bizSign);
	    param.put("bizType",bizType);
        return reportFeedbackMapper.countReportFeedbackDataList(param);
    }

    /**
	 * 校验信息反馈数据
	 * @param reportFeedback
	 * @throws Exception
	 */
    private void checkReportFeedbackData(Map<String, Object> reportFeedback,Map<String, Object> param,UserInfo userInfo) throws Exception{
		if (CollectionUtils.isEmpty(reportFeedback)) {
			throw new ZhsqEventException("信息反馈不能为空！");
		}
		if(userInfo==null){
			throw new ZhsqEventException("登录用户不能为空");
		}
		boolean isInsert = CommonFunctions.isBlank(reportFeedback, "fbUUId");
		if (isInsert) {
			if (CommonFunctions.isBlank(reportFeedback, "seUUId")) {
				throw new ZhsqEventException("参数【seUUId】不能为空！");
			}else{
				param.put("seUUId",reportFeedback.get("seUUId"));
			}
			//接收时限
			if (CommonFunctions.isNotBlank(reportFeedback, "reDeadline")) {
				this.formatDate(reportFeedback,"reDeadline");
				param.put("reDeadline",reportFeedback.get("reDeadline"));
			}else{
				throw new ZhsqEventException("参数【接收时限】不能为空！");
			}
			//反馈时限
			if (CommonFunctions.isNotBlank(reportFeedback, "fbDeadline")) {
				this.formatDate(reportFeedback,"fbDeadline");
				param.put("fbDeadline",reportFeedback.get("fbDeadline"));
			}else{
				throw new ZhsqEventException("参数【反馈时限】不能为空！");
			}
			param.put("seTime",DateUtils.convertStringToDate(DateUtils.getNow(),DateUtils.PATTERN_24TIME));//下达时间
			param.put("fbStatus",ReportFeedbackStatus.NOT_FEEDBACK.getCode());//反馈状态 新增默认为未反馈
			param.put("reStatus",ReportReadStatus.NOT_RECEIVED.getCode());////接收状态 新增默认为未接收
			param.put("seStatus",ReportSendStatus.ISSUED.getCode());//下达状态新增时默认为已下达
			//下达状态
			if (CommonFunctions.isNotBlank(reportFeedback, "seStatus")) {
				String seStatus = reportFeedback.get("seStatus").toString();
				if (CommonFunctions.isBlank(REPORT_SEND_STATUS_MAP,seStatus)) {
					throw new ZhsqEventException("参数【下达状态】"+ seStatus +"不合法！");
				}
				param.put("seStatus",seStatus);
			}
			//反馈组织
			Long fbOrgId = null;
			if (CommonFunctions.isNotBlank(reportFeedback, "fbOrgId")) {
				try {
					fbOrgId = Long.valueOf(reportFeedback.get("fbOrgId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new ZhsqEventException(e.getMessage());
				}
				param.put("fbOrgId",fbOrgId);
			}else{
				throw new ZhsqEventException("参数【反馈组织Id】不能为空！");
			}
			//反馈组织名称
			if (CommonFunctions.isNotBlank(reportFeedback, "fbOrgName")) {
				param.put("fbOrgName",reportFeedback.get("fbOrgName"));
			}else{
				OrgSocialInfoBO orgSocialInfoBO =orgSocialInfoOutService.findByOrgId(fbOrgId);
				if (orgSocialInfoBO != null) {
					param.put("fbOrgName",orgSocialInfoBO.getOrgName());
				}else{
					throw new ZhsqEventException("参数【反馈组织Id】不存在！");
				}
			}
			//反馈用户
			Long fbUserId = null;
			if (CommonFunctions.isNotBlank(reportFeedback, "fbUserId")) {
				try {
					fbUserId = Long.valueOf(reportFeedback.get("fbUserId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new ZhsqEventException(e.getMessage());
				}
				param.put("fbUserId",fbUserId);
			}else{
				throw new ZhsqEventException("参数【反馈用户Id】不能为空！");
			}
			//反馈用户姓名
			if (CommonFunctions.isNotBlank(reportFeedback, "fbUserName")) {
				param.put("fbUserName",reportFeedback.get("fbUserName"));
			}else{
				UserBO user = userManageService.getUserInfoByUserId(fbUserId);
				if (user != null) {
					param.put("fbUserName",user.getPartyName());
				}else{
					throw new ZhsqEventException("参数【反馈用户Id】不存在！");
				}
			}
			param.put("creator", userInfo.getUserId());
		}else{
			param.put("fbUUId",reportFeedback.get("fbUUId"));
		}
		//下达内容
		if (CommonFunctions.isNotBlank(reportFeedback, "seContent")) {
			param.put("seContent",reportFeedback.get("seContent"));
		}else{
			if (isInsert) {
				throw new ZhsqEventException("参数【下达内容】不能为空！");
			}else{
				param.put("noUpdate", "1");
			}
		}
		param.put("updator", userInfo.getUserId());
	}

	/**
	 * 格式化时间参数
	 * @param reportFeedback
	 * @param key
	 * @throws Exception
	 */
	private void formatDate(Map<String, Object> reportFeedback,String key) throws Exception{
		Object dateObj = reportFeedback.get(key);
		Date date = null;
		if(dateObj instanceof String) {
			try {
				date = DateUtils.convertStringToDate(dateObj.toString(), DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ZhsqEventException("参数[" + key + "]，不是如下时间格式：" + DateUtils.PATTERN_24TIME);
			}
		}else if(dateObj instanceof Date){
			date = (Date) dateObj;
		}
		reportFeedback.put(key,date);
	}

    /**
     * 校验信息下达数据
     * @param reportSend
     * @throws Exception
     */
    private void checkReportSendData(Map<String, Object> reportSend,Map<String, Object> param, UserInfo userInfo) throws Exception{
        if (CollectionUtils.isEmpty(reportSend)) {
            throw new ZhsqEventException("信息下达不能为空！");
        }
		if(userInfo==null){
			throw new ZhsqEventException("登录用户不能为空！");
		}
		boolean isUpdate = CommonFunctions.isNotBlank(reportSend, "seUUId");
		boolean shouldUpdate = false;
		if(isUpdate) {
			param.put("seUUId", reportSend.get("seUUId"));
		}else{
			param.put("creator", userInfo.getUserId());
		}
		//所属区域
		if (CommonFunctions.isNotBlank(reportSend, "regionCode")) {
			param.put("regionCode", reportSend.get("regionCode"));
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【所属区域regionCode】不能为空！");
			}
		}
		//业务类型
		if (CommonFunctions.isNotBlank(reportSend, "bizType")) {
			String bizType = reportSend.get("bizType").toString();
			if (CommonFunctions.isBlank(BIZTYPE_MAP,bizType)) {
				throw new ZhsqEventException("参数【业务类型bizType】"+ bizType +"不合法！");
			}
			param.put("bizType", bizType);
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【业务类型bizType】不能为空！");
			}
		}
		//业务标识
		if (CommonFunctions.isNotBlank(reportSend, "bizSign")) {
			param.put("bizSign", reportSend.get("bizSign"));
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【业务标识bizSign】不能为空！");
			}
		}
		//报告编号
		if (CommonFunctions.isNotBlank(reportSend, "bizCode")) {
			param.put("bizCode", reportSend.get("bizCode"));
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【报告编号bizCode】不能为空！");
			}
		}
		//数据来源
		if (CommonFunctions.isNotBlank(reportSend, "dataSource")) {
			param.put("dataSource", reportSend.get("dataSource"));
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【数据来源dataSource】不能为空！");
			}
		}
		//数据标识
		if (CommonFunctions.isNotBlank(reportSend, "dataSign")) {
			param.put("dataSign", reportSend.get("dataSign"));
			shouldUpdate = true;
		}else{
			if (!isUpdate) {
				throw new ZhsqEventException("参数【数据标识dataSign】不能为空！");
			}
		}
		param.put("updator", userInfo.getUserId());
		if(!shouldUpdate){
			param.put("noUpdate", "1");
		}
    }

    @Override
    public String saveReportSendExt(Map<String, Object> extMap, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<>();
        this.checkReportSendExt(extMap,param,userInfo);
        reportSendMapper.insertReportSendExt(param);
        if (CommonFunctions.isNotBlank(param, "extUUId")) {
            return param.get("extUUId").toString();
        }
        return null;
    }

	/**
	 * 校验补充信息
	 * @param extMap
	 * @throws Exception
	 */
	private void checkReportSendExt(Map<String, Object> extMap,Map<String, Object> param, UserInfo userInfo) throws Exception{
		if (CollectionUtils.isEmpty(extMap)) {
			throw new ZhsqEventException("补充信息不能为空！");
		}
		if(userInfo==null){
			throw new ZhsqEventException("登录用户不能为空！");
		}
		if (CommonFunctions.isNotBlank(extMap, "extType")) {
			String extType = extMap.get("extType").toString();
			if (CommonFunctions.isBlank(REPORT_SEND_EXT_TYPE_MAP,extType)) {
				throw new ZhsqEventException("参数【补充信息类型extType】"+ extType +"不合法！");
			}
			param.put("extType", extType);
		}else{
			throw new ZhsqEventException("参数【补充信息类型extType】不能为空！");
		}
		if (CommonFunctions.isNotBlank(extMap, "seUUId")) {
			String seUUId = extMap.get("seUUId").toString();
			if(CommonFunctions.isNotBlank(extMap, "checkValidOnBackEnd")
					&& "1".equals(extMap.get("checkValidOnBackEnd"))){
				Map<String, Object> reportSendMap = reportSendMapper.findByUUId(seUUId);
				if(CollectionUtils.isEmpty(reportSendMap)){
					throw new ZhsqEventException("未找到参数【下达信息seUUId】"+ seUUId);
				}
			}
			param.put("seUUId", seUUId);
		}else{
			throw new ZhsqEventException("参数【下达信息seUUId】不能为空！");
		}

		if (CommonFunctions.isNotBlank(extMap, "doPerson")) {
			param.put("doPerson", extMap.get("doPerson"));
		}else{
			throw new ZhsqEventException("参数【补充信息的处理人员或催单记录的办理人员doPerson】不能为空！");
		}

		if (CommonFunctions.isNotBlank(extMap, "doDate")) {
			this.formatDate(extMap,"doDate");
			param.put("doDate",extMap.get("doDate"));
		}else{
			throw new ZhsqEventException("参数【补充信息的处理时间或催单记录的催单时间doDate】不能为空！");
		}
		if (CommonFunctions.isNotBlank(extMap, "doContent")) {
			param.put("doContent", extMap.get("doContent"));
		}else{
			throw new ZhsqEventException("参数【补充信息的内容或催单记录的催单意见doContent】不能为空！");
		}
		param.put("creator", userInfo.getUserId());
		param.put("updator", userInfo.getUserId());
	}

}
