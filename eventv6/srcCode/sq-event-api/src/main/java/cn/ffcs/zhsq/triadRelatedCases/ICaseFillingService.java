package cn.ffcs.zhsq.triadRelatedCases;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.CaseFilling;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.TrialRecord;

import java.util.List;
import java.util.Map;


/**
 * @Description: 涉黑案件填报表模块服务
 * @Author: chenshikai
 * @Date: 09-08 17:46:05
 * @Copyright: 2021 福富软件
 */
public interface ICaseFillingService {

	/**
	 * 新增数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 涉黑案件填报表id
	 */
	public Long insert(CaseFilling bo);

	/**
	 * 修改数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(CaseFilling bo);

	/**
	 * 删除数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(Long userId,List<String> undCaseUuidList);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 涉黑案件填报表分页数据对象
	 */
	public EUDGPagination searchList(LayuiPage page, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 涉黑案件填报表id
	 * @return 涉黑案件填报表业务对象
	 */
	public CaseFilling searchById(String id);

	/**
	 * 根据业务id查询数据
	 * @param id 涉黑案件填报表id
	 * @return 庭审信息对象
	 */
	public List<TrialRecord> searchListById(String id);


	/**
	 * 新增或者编辑涉黑案件
	 * @param bo
	 * @return
	 */
	public boolean insertOrUpdate(CaseFilling bo);



	/**
	 * 江西案件jsonp接口
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getJsnpData(Map<String, Object> params);
}