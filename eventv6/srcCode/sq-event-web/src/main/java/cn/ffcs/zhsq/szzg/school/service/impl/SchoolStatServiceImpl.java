package cn.ffcs.zhsq.szzg.school.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolStat;
import cn.ffcs.zhsq.mybatis.persistence.szzg.school.SchoolStatMapper;
import cn.ffcs.zhsq.szzg.school.service.ISchoolStatService;

/**
 * @Description: 学校人数统计表模块服务实现
 * @Author: os.wuzhj
 * @Date: 04-04 08:43:01
 * @Copyright: 2019 福富软件
 */
@Service("schoolStatServiceImpl")
@Transactional
public class SchoolStatServiceImpl implements ISchoolStatService {

	@Autowired
	private SchoolStatMapper schoolStatMapper; //注入学校人数统计表模块dao


	/**
	 * 新增数据
	 * @param bo 学校人数统计表业务对象
	 * @return 学校人数统计表id
	 */
	@Override
	public Long insert(SchoolStat bo) {
		int exit = schoolStatMapper.exit(bo);
		if(exit > 0) {
			return 0L;
		}
		schoolStatMapper.insert(bo);
		return bo.getStatId();
	}

	/**
	 * 修改数据
	 * @param bo 学校人数统计表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(SchoolStat bo) {
		int exit = schoolStatMapper.exit(bo);
		if(exit > 0) {
			return false;
		}
		long result = schoolStatMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 学校人数统计表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(SchoolStat bo) {
		long result = schoolStatMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 学校人数统计表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<SchoolStat> list = null;
		long count = schoolStatMapper.countList(params);
		if(count > 0) {
			list = schoolStatMapper.searchList(params, rowBounds);
		}else {
			list = new ArrayList<SchoolStat>();
		}
		return  new EUDGPagination(count, list);
	}

	/**
	 * 根据业务id查询数据
	 * @param id 学校人数统计表id
	 * @return 学校人数统计表业务对象
	 */
	@Override
	public SchoolStat searchById(Long id) {
		SchoolStat bo = schoolStatMapper.searchById(id);
		return bo;
	}

}