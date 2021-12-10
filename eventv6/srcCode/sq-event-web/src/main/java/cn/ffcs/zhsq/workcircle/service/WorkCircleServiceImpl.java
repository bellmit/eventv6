package cn.ffcs.zhsq.workcircle.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.workcircle.WorkCircle;
import cn.ffcs.zhsq.mybatis.persistence.workcircle.WorkCircleMapper;

/**
 * @Description: 工作圈表模块服务实现
 * @Author: zhengyi
 * @Date: 2019-09-09
 * @Copyright: 2019 福富软件
 */
@Service("workCircleServiceImpl")
@Transactional
public class WorkCircleServiceImpl implements IWorkCircleService {

	@Autowired
	private WorkCircleMapper workCircleMapper; // 注入工作圈表模块dao
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;	
	/* 生成工作圈记录的方式 */
	private final static String DEFAULT_CREATE_WAY = "WHEN_UPDATE";
	
	
	private String getValueFromMap(Map<String, Object> params, String key) {
		if (params != null && params.get(key) != null) {
			return params.get(key).toString();
		}
		return null;
	}
	
	private WorkCircle setWorkCircleInfo(EventDisposal event, UserInfo u, Map<String, Object> params) {
		WorkCircle workCircle = new WorkCircle();
		workCircle.setWcType("1");// 工作圈类型：1 事件 2 后期扩展
		workCircle.setUpdateBy(u.getUserId());
		workCircle.setNextUserId(getValueFromMap(params, "nextUserId"));
		workCircle.setNextUserName(getValueFromMap(params, "nextUserName"));
		workCircle.setNextDeptName(getValueFromMap(params, "nextDeptName"));
		workCircle.setNextDeptCode(getValueFromMap(params, "nextDeptCode"));
		workCircle.setWorkflowName(getValueFromMap(params, "workflowName"));		
		
		
		// 事件相关信息
		if (event != null && event.getEventId() != null  && event.getEventId() > 0) {
			EventDisposal record = eventDisposalService.findEventById(event.getEventId(), u.getOrgCode());
			workCircle.setEventId(event.getEventId());
			
			try {
				if (StringUtils.isEmpty(event.getEventName())) {
					workCircle.setEventTitle(record.getEventName());
				} else {
					workCircle.setEventTitle(event.getEventName());
				}
				if (StringUtils.isEmpty(event.getContent())) {
					workCircle.setEventContent(record.getContent());
				} else {
					workCircle.setEventContent(event.getContent());
				}
				if (event.getHappenTime() == null) {
					workCircle.setEventTime(record.getHappenTime());
				} else {
					workCircle.setEventTime(event.getHappenTime());
				}
				if (StringUtils.isEmpty(event.getOccurred())) {
					workCircle.setEventAddr(record.getOccurred());
				} else {
					workCircle.setEventAddr(event.getOccurred());
				}
				if (StringUtils.isEmpty(event.getRemark())) {
					workCircle.setRemark(record.getRemark());
				} else {
					workCircle.setRemark(event.getRemark());
				}
				if (StringUtils.isEmpty(event.getStatus())) {
					workCircle.setOpType(record.getStatus());
				} else {
					workCircle.setOpType(event.getStatus());
				}
				if (StringUtils.isEmpty(event.getStatusName())) {
					workCircle.setOpTypeName(record.getStatusName());
				} else {
					workCircle.setOpTypeName(event.getStatusName());
				}
				if (StringUtils.isEmpty(event.getContactUser())) {
					workCircle.setOpUserName(record.getContactUser());
				} else {
					workCircle.setOpUserName(event.getContactUser());
				}
				if (event.getCreatorId() == null) {
					workCircle.setCreateBy(record.getCreatorId());	
				} else {
					workCircle.setCreateBy(event.getCreatorId());	
				}
				if (StringUtils.isEmpty(event.getGridCode())) {
					workCircle.setOpDeptCode(record.getGridCode());
				} else {
					workCircle.setOpDeptCode(event.getGridCode());
				}
				if (StringUtils.isEmpty(event.getGridName())) {
					workCircle.setOpDeptName(record.getGridName());
				} else {
					workCircle.setOpDeptName(event.getGridName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return workCircle;
	}
	
	/**
	 * 	新增或保存
	 * @param {
	 *		event {
	 *			eventId, 事件ID	***必填
	 *			eventName, 事件标题
	 *			happenTime, 发生时间Date
	 *			happenTimeStr, 发生时间String 
	 *			occurred, 事发地址
	 *			remark, 备注
	 *			eventContent 事件内容
	 *		}
	 *		, workCircle	***当 params-createWay 值为 WHEN_INSERT 时，必填  
	 *		, params {
	 *			createWay;// 工作圈产生方式, WHEN_UPDATE-适用于原工作圈逻辑，WHEN_INSERT-适用于松溪城管新工作圈逻辑
	 *			opType; // 操作类型 1 上报 2 受理 3 结案 4 上报并结案
	 *			nextUserId; // 下一办理人
	 *			nextUserName; // 下一办理人名称
	 *			nextDeptCode; // 下一办理部门
	 *			nextDeptName; // 下一办理部门名称
	 *			workflowName; // 流程名
	 *		}	
	 * }
	 */
	public boolean saveOrUpdate(EventDisposal event, WorkCircle workCircle, UserInfo u, Map<String, Object> params) {
		try {
			if (DEFAULT_CREATE_WAY.equals(params.get("createWay"))) {
				return insert(workCircle) > 0;
			} else {
				WorkCircle bo = setWorkCircleInfo(event, u, params);
				WorkCircle temp = workCircleMapper.searchByEventId(event.getEventId());
				if (temp != null) {
					bo.setWcId(temp.getWcId());
					return update(bo);
				} else {
					return insert(bo) > 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 新增数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 工作圈表id
	 */
	public Long insert(WorkCircle bo) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if ("1".equals(bo.getOpType())) {
				params.put("opType", bo.getOpType());
				params.put("eventId", bo.getEventId());
				params.put("nextDeptCode", bo.getNextDeptCode());
			} else {
				params.put("opType", bo.getOpType());
				params.put("eventId", bo.getEventId());
				params.put("opDeptCode", bo.getOpDeptCode());
			}
			List<WorkCircle> list = workCircleMapper.searchListNotPage(params);
			if (list.size() == 0) {
				bo.setCreateBy(bo.getOpUserId());
				workCircleMapper.insert(bo);
				return bo.getWcId();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return 0l;
	}

	/**
	 * 修改数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(WorkCircle bo) {
		long result = workCircleMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(WorkCircle bo) {
		long result = workCircleMapper.delete(bo);
		return result > 0;
	}

	
	/**
	 * 	查询数据（分页）
	 * @param params 查询参数
	 * @return 工作圈表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		try {
			String startTime = params.get("startTime").toString();
			if (!StringUtils.isEmpty(startTime)) {
				params.put("eventStartTime", DateUtils.convertStringToDate(startTime, "yyyy-MM-dd"));
			}
			String endTime = params.get("endTime").toString();
			if (!StringUtils.isEmpty(endTime)) {
				
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd"));
		        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		        String afterEndDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				
				params.put("eventEndTime", DateUtils.convertStringToDate(afterEndDate, "yyyy-MM-dd"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		long count = workCircleMapper.countList(params);
		List<WorkCircle> list = workCircleMapper.searchList(params, rowBounds);
		for (WorkCircle workCircle : list) {
			List<Attachment> listAtta = attachmentService.findByBizId(workCircle.getEventId(), "ZHSQ_EVENT");// 获取事件所有附件
			List<Attachment> listAtta2 = new ArrayList<Attachment>();
			int i = 0;
			for (Attachment attachment : listAtta) {
				String filename = attachment.getFileName();
				String fileType = filename.substring(filename.length() - 3).toUpperCase();
				if ("JPG".equals(fileType) || "GIF".equals(fileType) || "PNG".equals(fileType)
						|| "JPEG".equals(fileType)) {
					listAtta2.add(attachment);
					i++;
					if (i == 9) {
						break;
					}
				}
			}
			workCircle.setListAtta(listAtta2);
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * 
	 * @param id 工作圈表id
	 * @return 工作圈表业务对象
	 */
	public WorkCircle searchById(Long id) {
		WorkCircle bo = workCircleMapper.searchById(id);
		return bo;
	}

	public WorkCircle searchByDept(WorkCircle bo) {
		WorkCircle tem = workCircleMapper.searchByDept(bo);
		Integer timeoutDept = 0;
		String workflowName = funConfigurationService.changeCodeToValue("WORKFLOW_ATTRIBUTE", "workflowName",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, bo.getOpDeptCode(),
				IFunConfigurationService.DIRECTION_UP_FUZZY);
		if (StringUtils.isEmpty(workflowName)) {
			bo.setWorkflowName(workflowName);
		} else {
			bo.setWorkflowName("");
		}

		timeoutDept = workCircleMapper.searchTimeout(bo);
		tem.setTimeoutDept(timeoutDept);
		return tem;
	}

	public WorkCircle searchByUserId(WorkCircle bo) {
		WorkCircle returnbo = new WorkCircle();
		WorkCircle tem = workCircleMapper.searchByUserMonth(bo);
		if (tem != null) {
			returnbo.setAcceptanceMonth(tem.getAcceptanceMonth());
			returnbo.setHandleMonth(tem.getHandleMonth());
		}
		tem = workCircleMapper.searchByUserYear(bo);
		if (tem != null) {
			returnbo.setAcceptanceYear(tem.getAcceptanceYear());
			returnbo.setHandleYear(tem.getHandleYear());
		}
		return returnbo;
	}


}