package cn.ffcs.zhsq.times.service;/**
 * Created by Administrator on 2017/6/29.
 */


import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.timerManage.TimerManage;
import cn.ffcs.zhsq.mybatis.persistence.timerManage.TimerManageMapper;
import cn.ffcs.zhsq.time.ITimerManageService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-06-29 10:10
 **/
@Service(value = "timerManageServiceImpl")
public class TimerManageServiceImpl implements ITimerManageService {

    @Autowired
    private TimerManageMapper timerManageMapper;

    @Override
    public List<TimerManage> getTimerManageList(Map<String, Object> param) {
        return timerManageMapper.getTimerManageList(param);
    }

    @Override
    public EUDGPagination findTimerManagePagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        int count = timerManageMapper.findCountByCriteria(params);
        List<TimerManage> list = timerManageMapper.findPageListByCriteria(params, rowBounds);
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    @Override
    public int updateTimerManage(TimerManage timerManage){
        int i = timerManageMapper.updateByPrimaryKeySelective(timerManage);
        return i;
    }

    @Override
    public TimerManage findById(Long timerId){
        TimerManage timerManage = timerManageMapper.selectByPrimaryKey(timerId);
        return timerManage;
    }
}
