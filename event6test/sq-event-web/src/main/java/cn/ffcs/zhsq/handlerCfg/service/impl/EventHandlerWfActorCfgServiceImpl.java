package cn.ffcs.zhsq.handlerCfg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfActorCfgService;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;
import cn.ffcs.zhsq.mybatis.persistence.handlerCfg.EventHandlerWfActorCfgMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;

/**
 * 环节人员配置接口
 * T_BIZ_REGION_ACTOR_CFG
 * 
 * @author zhangls
 *
 */
@Service(value="eventHandlerWfActorCfgService")
public class EventHandlerWfActorCfgServiceImpl implements
		IEventHandlerWfActorCfgService {

	@Autowired
	private EventHandlerWfActorCfgMapper eventHandlerWfActorCfgMapper;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public Long saveHandlerWfActorCfg(
			EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws Exception {
		Long racId = -1L;
		
		if(eventHandlerWfActorCfgPO != null) {
			int result = 0;
			
			if(isRecordValid(eventHandlerWfActorCfgPO) && isRecordNotDuplicated(eventHandlerWfActorCfgPO)) {
				result = eventHandlerWfActorCfgMapper.insert(eventHandlerWfActorCfgPO);
			}
			
			if(result > 0) {
				racId = eventHandlerWfActorCfgPO.getRacId();
			}
		}
		
		return racId;
	}

	@Override
	public int saveHandlerWfActorCfg(Long wfpcId, String transactorIds, String transactorOrgIds, String transactorType) throws Exception {
		int result = 0;
		
		if(StringUtils.isNotBlank(transactorIds) && StringUtils.isNotBlank(transactorOrgIds)) {
			String[] transactorIdArray = transactorIds.split(","),
					 transactorOrgIdArray = transactorOrgIds.split(",");
			
			if(transactorIdArray.length != transactorOrgIdArray.length) {
				throw new IllegalArgumentException("参数[transactorIds]与[transactorOrgIds]未能一一对应！");
			} else {
				Long transactorId = -1L, transactorOrgId = -1L, racId = -1L;
				EventHandlerWfActorCfgPO actorCfg = null;
				List<Long> affectedRacIdList = new ArrayList<Long>();
				
				for(int index = 0, len = transactorIdArray.length; index < len; index++) {
					try {
						transactorId = Long.valueOf(transactorIdArray[index]);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					try {
						transactorOrgId = Long.valueOf(transactorOrgIdArray[index]);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					if(transactorId != null && transactorId > 0 && transactorOrgId != null && transactorOrgId > 0) {
						actorCfg = new EventHandlerWfActorCfgPO();
						racId = -1L;
						
						actorCfg.setTransactorId(transactorId);
						actorCfg.setTransactorOrgId(transactorOrgId);
						actorCfg.setTransactorType(transactorType);
						actorCfg.setWfpcId(wfpcId);
						
						try {
							racId = this.saveHandlerWfActorCfg(actorCfg);
						} catch (ZhsqEventException e) {//已存在的人员配置
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("wfpcId", wfpcId);
							params.put("transactorId", transactorId);
							params.put("transactorOrgId", transactorOrgId);
							params.put("transactorType", transactorType);
							
							List<EventHandlerWfActorCfgPO> wfActorCfgList = this.findHandlerWfActorCfgList(params);
							if(wfActorCfgList != null) {
								for(EventHandlerWfActorCfgPO wfActorCfg : wfActorCfgList) {
									affectedRacIdList.add(wfActorCfg.getRacId());
								}
							}
							
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(racId > 0) {//新增成功的人员配置
							affectedRacIdList.add(racId);
							result++;
						}
					}
				}
				
				delete4Idle(wfpcId, affectedRacIdList);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean updateHandlerWfActorCfgById(
			EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws Exception {
		boolean flag = false;
		
		if(eventHandlerWfActorCfgPO != null) {
			int result = 0;
			
			if(isRecordNotDuplicated(eventHandlerWfActorCfgPO)) {
				result = eventHandlerWfActorCfgMapper.update(eventHandlerWfActorCfgPO);
			}
			
			flag = result > 0;
		}
		
		return flag;
	}

	@Override
	public boolean delHandlerWfActorCfgById(Long racId) {
		boolean flag = false;
		
		if(racId != null && racId > 0) {
			flag = eventHandlerWfActorCfgMapper.delete(racId) > 0; 
		}
		
		return flag;
	}
	
	@Override
	public int delHandlerWfActorCfgByWfpcId(Long wfpcId) {
		int result = 0;
		
		if(wfpcId != null && wfpcId > 0) {
			result = eventHandlerWfActorCfgMapper.deleteByWfpcId(wfpcId, null);
		}
		
		return result;
	}

	@Override
	public int delHandlerWfActorCfgByWfcId(Long wfcId) {
		int result = 0;
		
		if(wfcId != null && wfcId > 0) {
			result = eventHandlerWfActorCfgMapper.deleteByWfcId(wfcId);
		}
		
		return result;
	}
	
	@Override
	public EUDGPagination findHandlerWfActorCfgPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = eventHandlerWfActorCfgMapper.findCountByCriteria(params);
		List<EventHandlerWfActorCfgPO> wfActorCfgList = eventHandlerWfActorCfgMapper.findPageListByCriteria(params, rowBounds);
		
		formatDataOut(wfActorCfgList);
		
		EUDGPagination wfActorCfgPagination = new EUDGPagination(count, wfActorCfgList);
		
		return wfActorCfgPagination;
	}
	
	@Override
	public List<EventHandlerWfActorCfgPO> findHandlerWfActorCfgList(Map<String, Object> params) {
		List<EventHandlerWfActorCfgPO> wfActorCfgList = eventHandlerWfActorCfgMapper.findPageListByCriteria(params);
		
		formatDataOut(wfActorCfgList);
		
		return wfActorCfgList;
	}
	
	/**
	 * 删除无用的人员配置
	 * @param wfpcId			流程环节配置id
	 * @param affectedRacIdList	受影响的人员配置记录/需要保留的人员配置记录
	 * @return
	 */
	private int delete4Idle(Long wfpcId, List<Long> affectedRacIdList) {
		int result = 0;
		
		if(wfpcId != null && wfpcId > 0) {
			result = eventHandlerWfActorCfgMapper.deleteByWfpcId(wfpcId, affectedRacIdList);
		}
		
		return result;
	}
	/**
	 * 判断属性是否有效
	 * @param eventHandlerWfActorCfgPO
	 * @return 属性均有效，返回true；有缺少属性，或者属性无效，返回false
	 * @throws Exception
	 */
	private boolean isRecordValid(EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws Exception {
		StringBuffer msgWrong = new StringBuffer("");
		boolean flag = false;
		
		if(eventHandlerWfActorCfgPO != null) {
			String transactorType = eventHandlerWfActorCfgPO.getTransactorType();
			Long wfpcId = eventHandlerWfActorCfgPO.getWfpcId(),
				 transactorId = eventHandlerWfActorCfgPO.getTransactorId(),
				 transactorOrgId = eventHandlerWfActorCfgPO.getTransactorOrgId();
			
			if(StringUtils.isBlank(transactorType)) {
				msgWrong.append("缺少参数[transactorType]！");
			}
			if(wfpcId == null || wfpcId < 0) {
				msgWrong.append("缺少参数[wfpcId]！");
			}
			if(transactorId == null || transactorId < 0) {
				msgWrong.append("缺少参数[transactorId]！");
			}
			if(transactorOrgId == null || transactorOrgId < 0) {
				msgWrong.append("缺少参数[transactorOrgId]！");
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new IllegalArgumentException(msgWrong.toString());
		} else {
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 判断记录是否没有重复
	 * @param eventHandlerWfActorCfgPO
	 * @return 记录重复，返回false；否则返回true
	 * @throws ZhsqEventException
	 */
	private boolean isRecordNotDuplicated(EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws ZhsqEventException {
		boolean isNotDuplicated = true;
		
		if(eventHandlerWfActorCfgPO != null) {
			String transactorType = eventHandlerWfActorCfgPO.getTransactorType();
			Long wfpcId = eventHandlerWfActorCfgPO.getWfpcId(),
				 transactorId = eventHandlerWfActorCfgPO.getTransactorId(),
				 transactorOrgId = eventHandlerWfActorCfgPO.getTransactorOrgId();
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuffer exceptionMsg = new StringBuffer("");
			
			if(StringUtils.isNotBlank(transactorType)) {
				params.put("transactorType", transactorType);
				exceptionMsg.append("[transactorType]为").append(transactorType).append(";");
			}
			if(wfpcId != null && wfpcId > 0) {
				params.put("wfpcId", wfpcId);
				exceptionMsg.append("[wfpcId]为").append(wfpcId).append(";");
			}
			if(transactorId != null && transactorId > 0) {
				params.put("transactorId", transactorId);
				exceptionMsg.append("[transactorId]为").append(transactorId).append(";");
			}
			if(transactorOrgId != null && transactorOrgId > 0) {
				params.put("transactorOrgId", transactorOrgId);
				exceptionMsg.append("[transactorOrgId]为").append(transactorOrgId).append(";");
			}
			
			isNotDuplicated = eventHandlerWfActorCfgMapper.findCount4Duplicate(params) == 0;
			
			if(!isNotDuplicated) {
				throw new ZhsqEventException("如下属性组合的记录已存在：" + exceptionMsg);
			}
		}
		
		return isNotDuplicated;
	}
	
	/**
	 * 格式化输出数据
	 * @param wfActorList
	 */
	private void formatDataOut(List<EventHandlerWfActorCfgPO> wfActorList) {
		if(wfActorList != null && wfActorList.size() > 0) {
			for(EventHandlerWfActorCfgPO wfActor : wfActorList) {
				formatDataOut(wfActor);
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param wfActor
	 */
	private void formatDataOut(EventHandlerWfActorCfgPO  wfActor) {
		if(wfActor != null) {
			String transactorType = wfActor.getTransactorType();
			Long transactorId = wfActor.getTransactorId(),
				 transactorOrgId = wfActor.getTransactorOrgId();
			
			if(EventHandlerWfActorCfgPO.TRANSACTOR_TYPE.PERSON.getCode().equals(transactorType)) {
				if(transactorId != null && transactorId > 0) {
					UserBO user = userManageService.getUserInfoByUserId(transactorId);
					if(user != null) {
						wfActor.setTransactorName(user.getPartyName());
					}
				}
				
				if(transactorOrgId != null && transactorOrgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(transactorOrgId);
					if(orgInfo != null) {
						wfActor.setTransactorOrgName(orgInfo.getOrgName());
					}
				}
			}
		}
	}
}
