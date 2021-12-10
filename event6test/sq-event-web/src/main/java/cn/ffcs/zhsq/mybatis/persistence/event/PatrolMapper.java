package cn.ffcs.zhsq.mybatis.persistence.event;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.Patrol;

public interface PatrolMapper extends MyBatisBaseMapper<Patrol> {
    int deleteByPrimaryKey(Long patrolId);

    int insert(Patrol record);

    int insertSelective(Patrol record);

    Patrol selectByPrimaryKey(Long patrolId);

    int updateByPrimaryKeySelective(Patrol record);

    int updateByPrimaryKey(Patrol record);
    
    int deleteByIdForLogic(Patrol record);
    
    public Long getPotarlSequence();  
}