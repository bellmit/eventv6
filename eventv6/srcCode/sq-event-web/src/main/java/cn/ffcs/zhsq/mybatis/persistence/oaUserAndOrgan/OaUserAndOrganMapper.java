package cn.ffcs.zhsq.mybatis.persistence.oaUserAndOrgan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.userOrganTree.UserOrganTree;

public interface OaUserAndOrganMapper {

	@Select("SELECT id,name,pid,uoo as userOrOrgan FROM V_USER_AND_ORGAN WHERE PID = #{id} ")
	public List<UserOrganTree> getUserOrgansByPid(@Param("id") Long id);
	
	@Select("SELECT id,name,pid,uoo as userOrOrgan FROM V_USER_AND_ORGAN WHERE PID = #{id} and UOO = #{uo}")
	public List<UserOrganTree> getUserOrgansByPid2(@Param("id") Long id , @Param("uo") String uo);
	
	@Select("SELECT count(*) FROM V_USER_AND_ORGAN WHERE PID = #{id} ")
	public Long getUserOrgansCountByPid(@Param("id") Long id);
	
	@Select("SELECT count(*) FROM V_USER_AND_ORGAN WHERE PID = #{id} and UOO = #{uo} ")
	public Long getUserOrgansCountByPid2(@Param("id") Long id , @Param("uo") String uo);
	
	/**
	 * 获取当前组织和下级部门
	 * @param id
	 * @return
	 */
	@Select("select id,name,pid,org_code orgCode from v_user_and_bm t where 1=1 start with t.id = #{id} connect by prior t.id = t.pid and uoo = 'b'")
	public List<UserOrganTree> getCurOrgAndzbm(@Param("id") Long id);
	
	/**
	 * 获取含有用户的下级组织
	 * @param id
	 * @return
	 */
	@Select("select t.id from V_USER_AND_ORGAN t where  uoo='o' and connect_by_isleaf = 0 start with t.pid = #{id} connect by prior t.id = t.pid")
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
	 
	 public UserOrganTree getPersonInfoByOrgIdUserId(@Param("orgId")Long orgId, @Param("userId")Long userId);
	 
	 /**
	  * 获取用户的组织层级结构
	 * @param userIds 用户列表
	 * @param orgId 顶级组织(可以为当前组织)
	 * @return 形如 /100970/120628  的列表数据<br />  (即 安泰街道管理员/安泰街道[假如当前登录为鼓楼区组织人员])
	 */
	public List<String> getPathByUserIds(@Param("userIds")Long[] userIds,@Param("orgId")Long orgId);
	
	public List<UserOrganTree> getInfoByIds(@Param("idsList")List<Long> idsList,@Param("uoo")String uoo);
	
	/**
	  * 根据id列表获取区域数据列表
	  * @param idsList
	  * @return
	  */
	 public List<UserOrganTree> getEntityInfoByIds(@Param("idsList")List<Long> idsList);
	 
	 
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
	public List<UserOrganTree> queryGrid(Map<String,Object> paramMap,RowBounds rowBounds);
	public List<UserOrganTree> queryGrid(Map<String,Object> paramMap);
	public Long queryGridCount(Map<String,Object> paramMap);
	/**
	 * 获取网格树信息(网格员)
	 * @param paramMap
	 * @return
	 */
	public List<UserOrganTree> queryGridPerson(Map<String,Object> paramMap,RowBounds rowBounds);
	public List<UserOrganTree> queryGridPerson(Map<String,Object> paramMap);
	public Long queryGridPersonCount(Map<String,Object> paramMap);
	
}
