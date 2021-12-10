package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 江阴市(JYS) 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4JYSService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		preNodeCode		上一节点编码，当isReject为true时，传入
 * 
 * @author zhangls
 *
 */
@Service(value = "eventStatusDecisionMaking4JYSService")
public class EventStatusDecisionMaking4JYSServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private static final String STREET_12345_NODE_CODE = "task12";//镇街12345平台
	private static final String STREET_PATROL_NODE_CODE = "task13";//公安巡防APP
	//大丰区事件流程图新增节点（相对于江阴事件流程图）
	private static final String STREET_DEPARTMENT_NODE_CODE = "task14";//街道职能部门
	private static final String DISTRICT_PATROL_NODE_CODE = "task15";//县区职能部门

	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//分流
		statusMap.put(STREET_NODE_CODE+"-"+STREET_12345_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_NODE_CODE+"-"+STREET_PATROL_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);

		statusMap.put(STREET_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+DISTRICT_PATROL_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		//响水个性化流程实现
		//网格员上报到街道职能部门；街道职能部门上报到乡镇(街道)处理；乡镇街道下派给街道职能部门处理
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);

		return statusMap;
	}
	
	/**
	 * 更新事件状态，并新增中间记录
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @param handleDate
	 * @return
	 * @throws Exception
	 */
	protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
		String eventStatus = super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);
		
		if(eventId != null && eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				//结案时，清除结案时间；归档时，重新生成
				if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(event.getStatus()) && (event.getFinTime() != null || StringUtils.isBlank(event.getFinTimeStr()))) {
					EventDisposal eventTmp = new EventDisposal();
					eventTmp.setEventId(eventId);
					eventTmp.setFinTimeStr("");
					eventTmp.setFinTime(null);
					eventDisposalService.updateEventDisposalById(eventTmp);
				}
			}
		}
		
		return eventStatus;
	}
}
