package cn.ffcs.zhsq.event.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.event.EventTypeProcCfgVO;

public interface IEventTypeProcCfg {

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean save(EventTypeProcCfgVO cfgVO);

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean update(EventTypeProcCfgVO cfgVO);

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean delete(String regionCode, String type);

	public EventTypeProcCfgVO findVO(String regionCode, String type, String userOrgCode);

	public EUDGPagination findListPagination(int pageNo, int pageSize, Map<String, Object> params);

}
