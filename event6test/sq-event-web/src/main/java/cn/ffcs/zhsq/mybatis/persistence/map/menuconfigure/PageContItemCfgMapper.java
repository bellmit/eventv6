package cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageContItemCfg;

public interface PageContItemCfgMapper extends MyBatisBaseMapper<PageContItemCfg>{
    int deleteByPrimaryKey(Integer contItemId);

    int insert(PageContItemCfg record);
    
    Integer getId();
    
   void delPageContItemCfgByOrgCode(Map<String, Object> param);

    int insertSelective(PageContItemCfg record);

    PageContItemCfg selectByPrimaryKey(Integer contItemId);

    int updateByPrimaryKeySelective(PageContItemCfg record);

    int updateByPrimaryKey(PageContItemCfg record);
    
    List<PageContItemCfg> findPageContItemCfg(Map<String, Object> param);
    
    List<PageContItemCfg> findPageContItemCfgById(Map<String, Object> param);
}