package cn.ffcs.zhsq.ypms.mscase.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.DeptHandle;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.DeptHandleMapper;
import cn.ffcs.zhsq.ypms.mscase.service.DeptHandleService;

/**
 * @Description: 延平民生派发单位处理过程表模块服务实现
 * @Author: zhangzhenhai
 * @Date: 05-03 14:34:50
 * @Copyright: 2018 福富软件
 */
@Service("deptHandleServiceImpl")
@Transactional
public class DeptHandleServiceImpl implements DeptHandleService {

	@Autowired
	private DeptHandleMapper deptHandleMapper; //注入延平民生派发单位处理过程表模块dao

	/**
	 * 新增数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 延平民生派发单位处理过程表id
	 */
	@Override
	public Long insert(DeptHandle bo) {
		deptHandleMapper.insert(bo);
		return bo.getRdhId();
	}

	/**
	 * 修改数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DeptHandle bo) {
		long result = deptHandleMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DeptHandle bo) {
		long result = deptHandleMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生派发单位处理过程表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeptHandle> list = deptHandleMapper.searchList(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = deptHandleMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 延平民生派发单位处理过程表id
	 * @return 延平民生派发单位处理过程表业务对象
	 */
	@Override
	public DeptHandle searchById(Long id) {
		DeptHandle bo = deptHandleMapper.searchById(id);
		formatBo(bo, "yyyy-MM-dd HH:mm:ss");
		return bo;
	}
	
	/**
	 * 实体对象格式化
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:30:55
	 * @param @param bo
	 * @param @param dateSdf
	 * @param @return    
	 * @return deptHandle
	 */
	public DeptHandle formatBo(DeptHandle bo,String dateSdf){
		
		//时间格式转换
		Date updateTime = bo.getUpdateTime();
		Date createTime = bo.getCreateTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String updateTimeStr = "";
		if(updateTime != null){
			updateTimeStr = sdf.format(updateTime);
		}
		
		String createTimeStr = "";
		if(createTime != null){
			createTimeStr = sdf.format(createTime);
		}
		
		bo.setUpdateTimeStr(updateTimeStr);
		bo.setCreateTimeStr(createTimeStr);
		return bo;
		
	}
	/**
	 * list格式化
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:31:05
	 * @param @param list
	 * @param @param dateSdf
	 * @param @return    
	 * @return List<deptHandle>
	 */
	public List<DeptHandle> formatListBo(List<DeptHandle> list,String dateSdf){
		
		//时间格式转换
		for (DeptHandle deptHandle : list) {
			formatBo(deptHandle, dateSdf);
		}
		return list;
		
	}
	
	/**
	 * 时间格式化(date===>String)
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:31:05
	 * @param @param Date
	 * @param @param dateSdf
	 * @param @return    
	 * @return String
	 */
	public String formatDateToString(Date paramDate,String dateSdf){
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String dateStr = "";
		if(paramDate != null){
			dateStr = sdf.format(paramDate);
		}
		return dateStr;
	}
	
	/**
	 * 时间格式化(String===>date)
	 * @author zhangzhenhai
	 * @date 2018-4-24 下午5:28:04
	 * @param @param dateSdf
	 * @param @param date
	 * @param @return    
	 * @return Date
	 */
	public Date formatStringDateTo(String date,String dateSdf) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	@Override
	public List<DeptHandle> searchListByParams(Map<String, Object> params_h) {
		List<DeptHandle> list = deptHandleMapper.searchListByParams(params_h);
		return list;
	}

	@Override
	public boolean updateByMore(DeptHandle deptHandle) {
		long result = deptHandleMapper.updateByMore(deptHandle);
		return result > 0;
	}
	
	

}