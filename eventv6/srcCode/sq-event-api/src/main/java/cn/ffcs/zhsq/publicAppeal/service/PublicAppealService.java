package cn.ffcs.zhsq.publicAppeal.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: 公众诉求模块服务
 * @Author: zhongshm
 * @Date: 09-01 11:20:04
 * @Copyright: 2017 福富软件
 */
public interface PublicAppealService {


	public Long saveAndReport(PublicAppeal bo, UserInfo userInfo,Map<String,Object> params);

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public Long insert(PublicAppeal bo) throws IOException;

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public boolean update(PublicAppeal bo);

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean delete(PublicAppeal bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	public EUDGPagination searchList(int page, int rows, UserInfo userInfo, Map<String, Object> params);

	public PublicAppeal searchByEventId(Long id);

	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public PublicAppeal searchById(Long id);

}