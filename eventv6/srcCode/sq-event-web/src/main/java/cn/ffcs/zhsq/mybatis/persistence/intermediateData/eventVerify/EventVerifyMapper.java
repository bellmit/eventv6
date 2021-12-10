package cn.ffcs.zhsq.mybatis.persistence.intermediateData.eventVerify;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * @Description: 事件信息审核模块
 * @Author: zhangls
 * @Date: 08-21 09:05:05
 * @Copyright: 2017 福富软件
 */
public interface EventVerifyMapper  extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据主键删除事件审核记录
	 * @param id		事件审核记录主键
	 * @param delUserId	删除操作用户
	 * @return
	 */
	public int delete(@Param(value="id") Long id, @Param(value="delUserId") Long delUserId);
	
	/**
	 * 依据相关条件查询事件审核记录
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventVerifyByParam(Map<String, Object> params);
}