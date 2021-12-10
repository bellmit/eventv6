package cn.ffcs.zhsq.jiangyinPlatform.communicationLog.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;

/**
 * @Description: 通讯记录表模块服务
 * @Author: 林树轩
 * @Date: 03-11 08:59:39
 * @Copyright: 2020 福富软件
 */
public interface ICommunicationLogService {

	/**
	 * 新增数据
	 * @param bo 通讯记录表业务对象
	 * @return 通讯记录表id
	 */
	public String insert(CommunicationLog bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯记录表分页数据对象
	 */
	public List<CommunicationLog> searchList(Map<String, Object> params);
	
}