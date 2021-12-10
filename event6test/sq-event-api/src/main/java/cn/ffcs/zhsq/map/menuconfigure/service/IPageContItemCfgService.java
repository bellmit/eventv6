package cn.ffcs.zhsq.map.menuconfigure.service;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageContItemCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageIndexCfg;

/**
 * 2014-10-09 liushi add 地图菜单配置服务接口
 * 
 * @author Administrator
 * 
 */
public interface IPageContItemCfgService {

	Long save(PageContItemCfg pageContItemCfg);

	Boolean savePageContItemCfgs(JSONArray jsonArray, String srcOrgCode,String srcPgIdxType, String orgCode, String pgIdxType, String displayStyle, String pgIdxId, String status);

	List<PageContItemCfg> findPageContItemCfg(PageContItemCfg pageContItemCfg);

	EUDGPagination findPageIndexCfgPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	PageIndexCfg findById(Map<String, Object> params);

	List<PageContItemCfg> findPageContItemCfgById(Map<String, Object> params);

	Long del(Long id);

	PageIndexCfg findByOrgCode(Map<String, Object> params);

}
