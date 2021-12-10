package cn.ffcs.zhsq.oaUserOrgan.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.mybatis.domain.userOrganTree.UserOrganTree;

public interface OaUserOrganService {

	/**
	 * 根据组织机构id获取其子节点的值
	 * @param id
	 * @return
	 */
	public List<UserOrganTree> getUserOrgansByPid(Long id);
	
	/**获取当前组织和下级部门
	 * @param id
	 * @return
	 */
	public List<UserOrganTree> getCurOrgAndzbm(Long id);
	
	/**
	 * @param id
	 * @param uo
	 * @return
	 */
	public List<UserOrganTree> getUserOrgansByPid(@Param("id") Long id , @Param("uo") String uo);
	
	public Long getUserOrgansCountByPid(@Param("id") Long id , @Param("uo") String uo);
	
	/**
	 * 根据组织机构id获取其子节点的数量
	 * @param id
	 * @return
	 */
	public Long getUserOrgansCountByPid(Long id);
	
	/**
	 * 获取含有用户的下级组织
	 * @param id
	 * @return
	 */
	public List<Long> getChildOrgWithUser(@Param("id") Long id);
	
	/**
	 * 获取排班关联的组织/人员
	 * @param paramMap{orgCode:String,scheduleId:Long,delFlag:String}
	 * @return
	 */
	public List<UserOrganTree> getSuchedulePersons(Map<String,Object> paramMap);
	
	/**
	 * 查找部门(组织)/人员
	 * @param paramMap
	 * 		  {pId(Long):所属上级节点的id,uoos(Array):['b':部门,'o':组织,'u':人员],UOO(String):数组uoos中的值}
	 * @return
	 */
	 public List<UserOrganTree> getUserAndBMByParam(Map<String,Object> paramMap);
	 
 	/**
	 * 查找部门(组织)/人员(包含人员职位)
	 * @param paramMap
	 * @return
	 */
	 public List<UserOrganTree> getUserIncPsitionAndBMByParam(Map<String,Object> paramMap);
	 
	 /**
	  * 获取当前所有的父层级(最顶层为当前用户所在的区域)
	  * @param paramMap
	  * @return
	  */
	 public List<UserOrganTree> getAllParentLevelList(Map<String,Object> paramMap);
	 
	 /**
	  * 通过userid获取人员信息
	  * @param orgId
	  * @param userId
	  * @return
	  */
	 public UserOrganTree getPersonInfoByOrgIdUserId(Long orgId,Long userId);
	 
	 /**
	 * 获取用户的组织层级结构
	 * @param userIds 用户列表
	 * @param orgId 顶级组织(可以为当前组织)
	 * @return 形如 120628,100970  的列表数据<br />  (即 安泰街道,安泰街道管理员[假如当前登录为鼓楼区组织人员])
	 */
	 public List<String> getPathByUserIds(Long orgId,Long[] userIds);
	 
	 public List<UserOrganTree> getInfoByIds(List<Long> idsList,String uoo);
	 
	 /**
	  * 根据id列表获取区域数据列表
	  * @param idsList
	  * @return
	  */
	 public List<UserOrganTree> getEntityInfoByIds(List<Long> idsList);
	 
	/**
	  * 获取网格员与网格树信息
	 * @param paramMap pid:父网格id,duty:网格员职务,personGridLevel:网格员等级(6为网格下面的网格员,非乡镇/村等下面的网格员)
	 * @return
	 */
	public List<UserOrganTree> getGridPersonByPid(Map<String,Object> paramMap);
	/**
	 * 获取网格树信息(组织)
	 * @param paramMap
	 * @return
	 */
	public List<UserOrganTree> queryGrid(Map<String,Object> paramMap,Integer page,Integer rows);
	public Long queryGridCount(Map<String,Object> paramMap);
	/**
	 * 获取网格树信息(网格员)
	 * @param paramMap
	 * @return
	 */
	public List<UserOrganTree> queryGridPerson(Map<String,Object> paramMap,Integer page,Integer rows);
	public Long queryGridPersonCount(Map<String,Object> paramMap);
}
