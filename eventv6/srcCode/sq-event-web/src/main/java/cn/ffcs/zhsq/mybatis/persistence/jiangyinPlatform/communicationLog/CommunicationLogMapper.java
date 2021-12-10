package cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.communicationLog;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;

/**
 * @Description: 通讯记录表模块dao接口
 * @Author: 林树轩
 * @Date: 03-11 08:59:39
 * @Copyright: 2020 福富软件
 */
public interface CommunicationLogMapper {
	
	/**
	 * 新增数据
	 * @param bo 通讯记录表业务对象
	 * @return 通讯记录表id
	 */
	public long insert(CommunicationLog bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 通讯记录表数据列表
	 */
	public List<CommunicationLog> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 通讯记录表数据总数
	 */
	public long countList(Map<String, Object> params);
	
}