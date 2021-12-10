package cn.ffcs.zhsq.intermediateData.clue.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface IEventClueService {

//	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	Long report(Map<String, Object> params, UserInfo userInfo) throws Exception;

	/**
	 *
	 * @param id
	 * @param userInfo
	 * @return
	 * @throws Exception
     */
//	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public Long report(Long id, String eventType, UserInfo userInfo) throws Exception;

	/**
	 *
	 * @param id
	 * @param userInfo
	 * @return
	 * @throws Exception
     */
	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public Boolean reject(Long id, UserInfo userInfo) throws Exception;

}