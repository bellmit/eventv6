package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.event.EventProcess;

public interface EventProcessMapper {
    int deleteByPrimaryKey(Integer eventProcessId);

    int insert(EventProcess record);

    int insertSelective(EventProcess record);

    EventProcess selectByPrimaryKey(Integer eventProcessId);

    int updateByPrimaryKeySelective(EventProcess record);

    int updateByPrimaryKey(EventProcess record);
    
    /**
     * 依据条件查询记录
     * @param eventProcess
     * @return 结果按主键降序排列
     */
    public List<EventProcess> findEventProcessByCondition(EventProcess eventProcess);
}