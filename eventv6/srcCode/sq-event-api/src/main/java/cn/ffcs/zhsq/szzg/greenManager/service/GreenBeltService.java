package cn.ffcs.zhsq.szzg.greenManager.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenBeltBO;
import cn.ffcs.system.publicUtil.EUDGPagination;


public interface GreenBeltService {
	
	/**
	 * 条件查询
	 * @param params
	 * @return
	 */
	public List<GreenBeltBO> findGreenByParams(Map<String, Object> params);
	
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);
	
	/**
	 * 根据Id查询
	 * @param seqid
	 * @return
	 */
	public GreenBeltBO findGreenBeltById(Long seqid);
	
	/**
	 * 更新
	 * @param greenBelt
	 * @return
	 */
	public Boolean update(GreenBeltBO greenBelt);
	
	/**
	 * 单条删除
	 * @param seqid
	 * @return
	 */
	public Boolean del(Long seqid);
	
	/**
	 * 新增
	 * @param greenBelt
	 * @return
	 */
	public Boolean insert(GreenBeltBO greenBelt);
}
