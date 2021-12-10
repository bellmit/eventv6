package cn.ffcs.zhsq.mybatis.persistence.timerManage;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.timerManage.TimerManage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TimerManageMapper extends MyBatisBaseMapper<TimerManage> {
    int deleteByPrimaryKey(BigDecimal timerId);

    int insert(TimerManage record);

    int insertSelective(TimerManage record);

    TimerManage selectByPrimaryKey(Long timerId);

    int updateByPrimaryKeySelective(TimerManage record);

    int updateByPrimaryKey(TimerManage record);

    List<TimerManage> getTimerManageList(Map<String, Object> param);
}