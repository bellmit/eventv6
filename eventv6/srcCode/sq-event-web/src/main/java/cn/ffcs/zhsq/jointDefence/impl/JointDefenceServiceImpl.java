package cn.ffcs.zhsq.jointDefence.impl;

import cn.ffcs.shequ.utils.IdCardUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.jointDefence.service.IJointDefenceService;
import cn.ffcs.zhsq.mybatis.persistence.jointDefence.JointDefenceMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 案件 江西省罗坊镇
 * @author zhangls
 *
 */
@Service(value = "jointDefenceService")
public class JointDefenceServiceImpl implements IJointDefenceService {
	@Autowired
	private JointDefenceMapper jointDefenceMapper;

	@Autowired
	private IFunConfigurationService funConfigurationService;

	/**
	 * 联防长
	 */
	@Override
	public EUDGPagination findPagination(int pageNo, int pageSize, Map<String, Object> params) {
		//if(params.get("positionCode") == null || params.get("positionCode").toString().equals("")) return null;
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> jointDefenceList = new ArrayList<Map<String, Object>>();
		int count = jointDefenceMapper.findCount(params);
		if(count > 0) {
			jointDefenceList = jointDefenceMapper.findPageList(params, rowBounds);
            format(jointDefenceList);
		}
		return new EUDGPagination(count, jointDefenceList);
	}

	private void format(List<Map<String, Object>> jointDefenceList){
		if(jointDefenceList != null && jointDefenceList.size() > 0){
			for(Map<String, Object> jointDefence : jointDefenceList){
				if(null != jointDefence.get("IDENTITY_CARD")){
					String idCard = jointDefence.get("IDENTITY_CARD").toString();
                    String genderByIdCard = IdCardUtils.getGenderByIdCard(idCard);
                    jointDefence.put("I_GENDER", genderByIdCard);
//                    String birthByIdCard = IdCardUtil.getBirthByIdCard(idCard);
//                    jointDefence.put("", birthByIdCard);
                }
			}
		}
	}

	@Override
	public List<Map<String, Object>> findList(Map<String, Object> params) {
		String positionId = funConfigurationService.changeCodeToValue("JOIN_DEFENCE_CFG","POSITION_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		params.put("positionId", positionId);
		return jointDefenceMapper.findPageList(params);
	}

	@Override
	public EUDGPagination findEventPagination(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = new ArrayList<>();
		int count = jointDefenceMapper.findEventCount(params);
		if(count > 0) {
			list = jointDefenceMapper.findEventPageList(params, rowBounds);
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public EUDGPagination findPagination(int pageNo, int pageSize, Map<String, Object> params, UserInfo userInfo) {
		return null;
	}
	
	/**
	 * 根据orgcode查询整个完整路径
	 */
	@Override
	public Map<String,Object> findGridPathByOrgCode(Map<String,Object> param){
		return jointDefenceMapper.findGridPathByOrgCode(param);
	}
	/**
	 * 查询联防组数量、联防长数量、总人口数量、已上报事件（即采集事件量）、受理事件
	 */
	@Override
	public Map<String,Object> findStatByOrgCode(Map<String,Object> param){
		return jointDefenceMapper.findStatByOrgCode(param);
	}
	
	/**
	 * 展示联防组
	 */
	@Override
	public EUDGPagination findJointTeam(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String positionId = funConfigurationService.changeCodeToValue("JOIN_DEFENCE_CFG","POSITION_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		params.put("positionId", positionId);
		int count = jointDefenceMapper.findJointTeamCount(params);
		List<Map<String, Object>>	list = jointDefenceMapper.findJointTeamList(params, rowBounds);
		return new EUDGPagination(count, list);
	}
	/**
	 * 展示联防组
	 */
	@Override
	public EUDGPagination findJointPerson(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String positionId = funConfigurationService.changeCodeToValue("JOIN_DEFENCE_CFG","POSITION_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		params.put("positionId", positionId);
		int count = jointDefenceMapper.findJointPersonCount(params);
		List<Map<String, Object>>	list = jointDefenceMapper.findJointPersonList(params, rowBounds);
		return new EUDGPagination(count, list);
	}
	@Override
	public List<Map<String, Object>> findJointPersonList(Map<String, Object> param){
		String positionId = funConfigurationService.changeCodeToValue("JOIN_DEFENCE_CFG","POSITION_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		param.put("positionId", positionId);
		return jointDefenceMapper.findJointPersonList(param);
	}
}
