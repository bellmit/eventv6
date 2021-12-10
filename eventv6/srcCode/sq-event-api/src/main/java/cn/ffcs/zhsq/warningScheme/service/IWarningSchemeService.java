package cn.ffcs.zhsq.warningScheme.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;

/**
 * @Description: 预警方案模块服务
 * @Author: youwj
 * @Date: 05-28 11:20:04
 * @Copyright: 2019 福富软件
 */
public interface IWarningSchemeService {

	/**
     * 获取列表记录
     * @param session
     * @param page		页码
     * @param rows		每页记录数
     * @param params
     * 			bizType	业务模块编码
     * @return
     */
	EUDGPagination findSchemePagination(int page, int rows, Map<String, Object> params);

	/**
     * 根据id获取预警方案信息
     * @param schemeId
     */
	SchemeMatch findSchemeById(Long schemeId);

	/**
     * 根据scheme_id获取预警方案的等级信息
     * @param schemeId
     */
	List<SchemeKeyword> findSchemeKeywordBySchemeId(Long schemeId);

	/**
     * 新增
     * @param SchemeMatch
     */
	Long saveScheme(SchemeMatch schemeMatch);

	
	/**
     * 根据keyword集合批量新增
     * @param SchemeMatch
     */
	void saveSchemeKeyword(List<SchemeKeyword> list);
	
	/**
	 * 根据keyword集合批量修改
	 * @param SchemeMatch
	 */
	void updateSchemeKeyword(List<SchemeKeyword> list);

	Long saveSchemeKeyword(SchemeKeyword schemeKeyword);

	/**
     * 更新
     * @param SchemeMatch
     */
	Long updateScheme(SchemeMatch schemeMatch);

	/**
     * 获取所有的code
     */
	List<String> findAllCode();

	Long updateSchemeKeyword(SchemeKeyword schemeKeyword);


	/**
     * 删除（假删）
     * @param SchemeId
     */
	Long deleteSchemeById(Long schemeId);

	/**
     * 获取生效的记录
     */
	List<SchemeMatch> findSchemeEffect();

	/**
     * 判断事件是否匹配关键字
     */
	Map<String,Object> fetchKeyWord(List<String> keyword, Long eventId);

	/**
     * 查询事件
     */
	EventDisposal findEventById(Long eventId);

	
	
}
