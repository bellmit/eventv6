package cn.ffcs.zhsq.szzg.greenManager.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenStdBO;
import cn.ffcs.system.publicUtil.EUDGPagination;


public interface GreenStdService {
	
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);
	
	/**
	 * 根据id查询
	 * @param seqid
	 * @return
	 */
	public GreenStdBO findGreenStdById(Long seqid);
	
	/**
	 * 更新
	 * @param greenStd
	 * @return
	 */
	public Boolean update(GreenStdBO greenStd);
	
	/**
	 * 批量删除
	 * @param seqid
	 * @return
	 */
	public Boolean del(Long seqid);
	
	/**
	 * 新增
	 * @param greenStd
	 * @return
	 */
	public Boolean insert(GreenStdBO greenStd);
	
	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	public List<GreenStdBO> findGreenStdByParams(Map<String, Object> params);

}
