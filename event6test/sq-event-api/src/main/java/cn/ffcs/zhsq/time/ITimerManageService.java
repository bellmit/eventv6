package cn.ffcs.zhsq.time;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.timerManage.TimerManage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface ITimerManageService {

    List<TimerManage> getTimerManageList(Map<String, Object> param);

    EUDGPagination findTimerManagePagination(int pageNo, int pageSize, Map<String, Object> params);

    int updateTimerManage(TimerManage timerManage);

    TimerManage findById(Long timerId);
}
