package cn.ffcs.zhsq.mybatis.persistence.event;

import cn.ffcs.zhsq.mybatis.domain.event.EventRela;
import cn.ffcs.zhsq.mybatis.domain.event.EventRelaKey;

public interface EventRelaMapper {
    int deleteByPrimaryKey(EventRelaKey key);

    int insert(EventRela record);

    int insertSelective(EventRela record);

    EventRela selectByPrimaryKey(EventRelaKey key);

    int updateByPrimaryKeySelective(EventRela record);

    int updateByPrimaryKey(EventRela record);
}