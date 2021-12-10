package cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure;

import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageIndexCfg;

public interface PageIndexCfgMapper extends MyBatisBaseMapper<PageIndexCfg> {
    int deleteByPrimaryKey(Integer pgIdxCfgId);

    int insert(PageIndexCfg record);
    
    void delPageIndexCfg(Map<String, Object> param);
    
    int delPageIndexCfgById(Long id);

    int insertSelective(PageIndexCfg record);

    PageIndexCfg selectByPrimaryKey(Integer pgIdxCfgId);

    int updateByPrimaryKeySelective(PageIndexCfg record);

    int updateByPrimaryKey(PageIndexCfg record);
    
    PageIndexCfg findById(Map<String, Object> param);
    
    PageIndexCfg findByOrgCode(Map<String, Object> param);
}