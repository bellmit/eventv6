package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTask;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTaskStatistics;

/**
 * @Description: 海南万宁中控舱督办信息sql查询
 * @Author: youwj
 * @Date: 5-17 10:15:16
 * @Copyright: 2019 福富软件
 */
public interface EventSuperviseRespMapper {
	
	//根据督办信息Id与被督办人Id查找消息IdmsgId
	public Map<String,Object> findMsgInfo(Map<String, Object> params);
	
	

}