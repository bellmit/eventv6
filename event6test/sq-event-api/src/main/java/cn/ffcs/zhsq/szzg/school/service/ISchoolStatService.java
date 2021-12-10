package cn.ffcs.zhsq.szzg.school.service;


import java.util.Map;

import com.sun.tools.javac.util.List;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolStat;

/**
 * @Description: 学校人数统计表模块服务
 * @Author: os.wuzhj
 * @Date: 04-04 08:43:01
 * @Copyright: 2019 福富软件
 */
public interface ISchoolStatService {

	/**
	 * 新增数据
	 * @param bo 学校人数统计表业务对象
	 * @return 学校人数统计表id
	 */
	public Long insert(SchoolStat bo);

	/**
	 * 修改数据
	 * @param bo 学校人数统计表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(SchoolStat bo);

	/**
	 * 删除数据
	 * @param bo 学校人数统计表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(SchoolStat bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 学校人数统计表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 学校人数统计表id
	 * @return 学校人数统计表业务对象
	 */
	public SchoolStat searchById(Long id);
	
}