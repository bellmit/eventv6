package cn.ffcs.zhsq.eventExpand.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 事件分类标签表相关服务
 * @author youwj
 * 2020/07/22
 */
public interface IEventLabelService {
	
	
	/**
	 * 新增或更新操作
	 * （labelId不存在则执行新增操作否则执行更新操作）
	 * @param 
	 * 		eventLabel 		事件标签表信息
	 * 
	 * 		userInfo		操作用户信息（必传）
	 * 2019/11/12
	 * @retuen 成功返回新增条数，失败返回-1 
	 */
	public Integer saveOrUpdate(Map<String,Object> eventLabel,UserInfo userInfo);
	
	/**
	 * 删除操作
	 * （eventId不存在则执行新增操作否则执行更新操作）
	 * @param 
	 * 		eventLabel	 	事件标签表信息
	 * 		必传参数
	 * 			labelId		事件标签表Id
	 * 
	 * 		userInfo		操作用户信息（必传）
	 * 2019/11/12
	 * @retuen 成功返回删除条数，失败返回-1 
	 */
	public Integer deleteEventLabel(Map<String,Object> eventLabel,UserInfo userInfo);
	
	/**
	 * 获取列表不分页
	 * @param
	 * 必传参数 
	 * 		eventLabel 	事件标签表信息
	 * 2019/11/12
	 */
	public List<Map<String,Object>> searchListByParam(Map<String,Object> eventLabel);
	
	/**
	 * 获取列表分页
	 * @param
	 * 必传参数 
	 * 		eventLabel 	事件标签表信息
	 * 		rowBounds	分页信息
	 * 2019/11/12
	 */
	public EUDGPagination searchList(Map<String,Object> eventLabel);
	
	/**
	 * 获取列表总数
	 * @param
	 * 必传参数 
	 * 		eventLabel 	事件标签表信息
	 * 2019/11/12
	 */
	public Long countList(Map<String,Object> eventLabel);
	
	/**
	 * 获取详情
	 * @param
	 * 必传参数 
	 * 		labelId 	事件标签表信息Id
	 * 2019/11/12
	 */
	public Map<String,Object> searchById(Long labelId);
	
	
	
	
}
