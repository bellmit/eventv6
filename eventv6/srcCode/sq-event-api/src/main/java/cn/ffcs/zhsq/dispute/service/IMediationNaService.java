package cn.ffcs.zhsq.dispute.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationNa;

import java.util.List;
import java.util.Map;

/**
 * @Description: 南安矛盾纠纷模块服务
 * @Author: 黄文斌
 * @Date: 06-03 09:05:22
 * @Copyright: 2020 福富软件
 */
public interface IMediationNaService {

	/**
	 * 新增数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	public Long insert(MediationNa bo);

	/**
	 * 修改数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 是否修改成功
	 */
	public boolean update(MediationNa bo);

	/**
	 * 删除数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(MediationNa bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 南安矛盾纠纷分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 南安矛盾纠纷id
	 * @return 南安矛盾纠纷业务对象
	 */
	public MediationNa searchById(Long id);

	/**
	 * csk
	 * 批量新增、更新
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	public Long insertAll(List<MediationNa> mediationNaList);

	/**
	 * csk
	 * 调用接口
	 */
	public String detail(Long id,String url);

	/**
	 * csk
	 * 返回map
	 * @param 南安矛盾纠纷业务地点
	 * @return 近12个月趋势
	 */

	public List<Map<String,Object>> findAcquisitionTrendData(Map<String, Object> params);


	/**
	 *	 案件类型名TOP5
	 * @param map
	 *
	 *
	 * @return
	 */

	public List<Map<String,Object>> findHotEventData(Map<String, Object> params);

	/**
	 * 根据业务id查询数据
	 * @param id 南安矛盾纠纷id
	 * @return 网格名
	 */
	public String searchGrid(Long id);



	/**
	 * 根据业务id查询数据
	 * @param 地域编码
	 * @return 排查数，化解数
	 */
	public Map<String, Object> searchNum(Map<String, Object> params);

	/**
	 *	 案件化解率排名
	 * @param
	 *
	 * @return
	 */

	public List<Map<String,Object>> searchPaiming(Map<String, Object> params);




	//南安七种对接类型
	public boolean insterDockingtype(List<MediationNa> mediationNaList);
	//南安七种对接类型删除
	public boolean deletDockingtype(List<MediationNa> mediationNaList);

	/**
	 *	 南安纠矛盾纠纷汇聚
	 * @param
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> DisputesConfluence(Map<String, Object> params);

	/**
	 *	 案件化解率排名
	 * @param
	 *
	 * @return
	 */


	//矛盾纠纷撒点查询

	public EUDGPagination DisputesDotsJsonp(int page, int rows, Map<String, Object> params);
   //矛盾纠纷状态类型数量统计
    public List<Map<String, Object>> findStatusData(Map<String, Object> params);
    //南安七对接类型数量统计
	public List<Map<String,Object>> findStatusNum(Map<String, Object> params);
	//南安化解状态数量总数
	public Map<String, Object>getTypeNum(Map<String, Object> params);
	//南安获取趋势数据 时间段的年份/月份/天
	public List<Map<String, Object>> getDataTime(Map<String, Object> params);
	//矛盾纠纷 区间每日汇总
	public List<Map<String, Object>> fetchEventDay4Jsonp(Map<String, Object> params);
}