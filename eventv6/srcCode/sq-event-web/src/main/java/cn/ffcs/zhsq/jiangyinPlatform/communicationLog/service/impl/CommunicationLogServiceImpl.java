package cn.ffcs.zhsq.jiangyinPlatform.communicationLog.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.zhsq.jiangyinPlatform.communicationLog.service.ICommunicationLogService;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;
import cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.communicationLog.CommunicationLogMapper;

/**
 * @Description: 通讯记录表模块服务实现
 * @Author: 林树轩
 * @Date: 03-11 08:59:39
 * @Copyright: 2020 福富软件
 */
@Service("communicationLogServiceImpl")
@Transactional
public class CommunicationLogServiceImpl implements ICommunicationLogService {

	@Autowired
	private CommunicationLogMapper communicationLogMapper; //注入通讯记录表模块dao

	/**
	 * 新增数据
	 * @param bo 通讯记录表业务对象
	 * @return 通讯记录表id
	 */
	@Override
	public String insert(CommunicationLog bo) {
		communicationLogMapper.insert(bo);
		return bo.getLogUuid();
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯记录表分页数据对象
	 */
	@Override
	public List<CommunicationLog> searchList(Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds(0, 20);
		List<CommunicationLog> list = communicationLogMapper.searchList(params, rowBounds);
		return list;
	}

}