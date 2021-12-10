package cn.ffcs.zhsq.oaUserOrgan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.userOrganTree.UserOrganTree;
import cn.ffcs.zhsq.mybatis.persistence.oaUserAndOrgan.OaUserAndOrganMapper;
import cn.ffcs.zhsq.oaUserOrgan.service.OaUserOrganService;

@Service("oaUserOrganService")
public class OaUserOrganServiceImpl implements OaUserOrganService{

	@Autowired
	private OaUserAndOrganMapper oaUserAndOrganMapper;
	
	/* (non-Javadoc)
	 * @see cn.ffcs.oa.service.OaUserOrganService#getUserOrgansByPid(java.lang.Long)
	 */
	@Override
	public List<UserOrganTree> getUserOrgansByPid(Long id){
		return oaUserAndOrganMapper.getUserOrgansByPid(id);
	}
	
	/* (non-Javadoc)
	 * @see cn.ffcs.oa.service.OaUserOrganService#getUserOrgansCountByPid(java.lang.Long)
	 */
	@Override
	public Long getUserOrgansCountByPid(Long id){
		return oaUserAndOrganMapper.getUserOrgansCountByPid(id);
	}

	@Override
	public List<Long> getChildOrgWithUser(Long id) {
		return oaUserAndOrganMapper.getChildOrgWithUser(id);
	}

	@Override
	public List<UserOrganTree> getUserOrgansByPid(Long id, String uo) {
		return oaUserAndOrganMapper.getUserOrgansByPid2(id, uo);
	}

	@Override
	public Long getUserOrgansCountByPid(Long id, String uo) {
		return oaUserAndOrganMapper.getUserOrgansCountByPid2(id, uo);
	}

	@Override
	public List<UserOrganTree> getSuchedulePersons(Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.getSuchedulePersons(paramMap);
	}

	@Override
	public List<UserOrganTree> getUserAndBMByParam(Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.getUserAndBMByParam(paramMap);
	}

	@Override
	public List<UserOrganTree> getUserIncPsitionAndBMByParam(
			Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.getUserIncPsitionAndBMByParam(paramMap);
	}

	@Override
	public UserOrganTree getPersonInfoByOrgIdUserId(Long orgId, Long userId) {
		return oaUserAndOrganMapper.getPersonInfoByOrgIdUserId(orgId, userId);
	}
	
	@Override
	public List<UserOrganTree> getCurOrgAndzbm(Long id) {
		return oaUserAndOrganMapper.getCurOrgAndzbm(id);
	}
	
	@Override
	public List<UserOrganTree> getAllParentLevelList(
			Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.getAllParentLevelList(paramMap);
	}

	@Override
	public List<String> getPathByUserIds(Long orgId, Long[] userIds) {
		List<String> list = oaUserAndOrganMapper.getPathByUserIds(userIds, orgId);
		List<String> result = new ArrayList<String>();
		if(list != null && !list.isEmpty()){
			String[] temp = {};
			String resultR = "";
			for(String s : list){
				resultR = "";
				temp = s.split("/");
				
				for(int i=temp.length-1;i>=0;i--){
					if(StringUtils.isNotBlank(temp[i])){
						resultR += temp[i]+",";
					}
				}
				if(!"".equals(resultR)){
					resultR = resultR.substring(0, resultR.length()-1);
					result.add(resultR);
				}
			}
		}
		return result;
	}
	
	@Override
	public List<UserOrganTree> getInfoByIds(List<Long> idsList,String uoo) {
		return oaUserAndOrganMapper.getInfoByIds(idsList,uoo);
	}

	@Override
	public List<UserOrganTree> getEntityInfoByIds(List<Long> idsList) {
		return oaUserAndOrganMapper.getEntityInfoByIds(idsList);
	}

	@Override
	public List<UserOrganTree> getGridPersonByPid(Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.getGridPersonByPid(paramMap);
	}

	@Override
	public List<UserOrganTree> queryGrid(Map<String, Object> paramMap,Integer page,Integer rows) {
		if(page != null && rows != null){
			if(page<1) page = 1;
			if(rows<1) rows = 20;
			RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
			return oaUserAndOrganMapper.queryGrid(paramMap, rowBounds);
		}
		return oaUserAndOrganMapper.queryGrid(paramMap);
	}

	@Override
	public Long queryGridCount(Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.queryGridCount(paramMap);
	}

	@Override
	public List<UserOrganTree> queryGridPerson(Map<String, Object> paramMap,Integer page,Integer rows) {
		if(page != null && rows != null){
			if(page<1) page = 1;
			if(rows<1) rows = 20;
			RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
			return oaUserAndOrganMapper.queryGridPerson(paramMap, rowBounds);
		}
		return oaUserAndOrganMapper.queryGridPerson(paramMap);
	}

	@Override
	public Long queryGridPersonCount(Map<String, Object> paramMap) {
		return oaUserAndOrganMapper.queryGridPersonCount(paramMap);
	}
	
	
}
