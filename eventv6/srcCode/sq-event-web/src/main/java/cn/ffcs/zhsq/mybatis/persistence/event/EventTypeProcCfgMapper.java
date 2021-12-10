package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.EventTypeProcCfgPO;

public interface EventTypeProcCfgMapper extends MyBatisBaseMapper<EventTypeProcCfgPO> {

	public List<EventTypeProcCfgPO> findPO(Map<String, Object> param);

	public int deletePO(@Param(value = "regionCode") String regionCode, @Param(value = "type") String type);

}
