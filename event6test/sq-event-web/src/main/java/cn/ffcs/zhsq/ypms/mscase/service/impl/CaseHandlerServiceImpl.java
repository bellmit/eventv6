package cn.ffcs.zhsq.ypms.mscase.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.CaseHandler;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.CaseHandlerMapper;
import cn.ffcs.zhsq.ypms.mscase.service.CaseHandlerService;

/**
 * @Description: 延平民生案件操作员信息表模块服务实现
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:42
 * @Copyright: 2018 福富软件
 */
@Service("caseHandlerServiceImpl")
@Transactional
public class CaseHandlerServiceImpl implements CaseHandlerService {

	@Autowired
	private CaseHandlerMapper caseHandlerMapper; //注入延平民生案件操作员信息表模块dao

	/**
	 * 新增数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 延平民生案件操作员信息表id
	 */
	@Override
	public Long insert(CaseHandler bo) {
		caseHandlerMapper.insert(bo);
		return bo.getChId();
	}

	/**
	 * 修改数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(CaseHandler bo) {
		long result = caseHandlerMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(CaseHandler bo) {
		long result = caseHandlerMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生案件操作员信息表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<CaseHandler> list = caseHandlerMapper.searchList(params, rowBounds);
		formatListBo(list, "yyyy-MM-dd HH:mm:ss");
		long count = caseHandlerMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 延平民生案件操作员信息表id
	 * @return 延平民生案件操作员信息表业务对象
	 */
	@Override
	public CaseHandler searchById(Long id) {
		CaseHandler bo = caseHandlerMapper.searchById(id);
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
	 * @return CaseHandler
	 */
	public CaseHandler formatBo(CaseHandler bo,String dateSdf){
		
		//时间格式转换
		Date handleTime = bo.getHandleTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String handleTimeStr = "";
		if(handleTime != null){
			handleTimeStr = sdf.format(handleTime);
		}
		
		bo.setHandleTimeStr(handleTimeStr);
		return bo;
		
	}
	/**
	 * list格式化
	 * @author zhangzhenhai
	 * @date 2018-4-19 下午2:31:05
	 * @param @param list
	 * @param @param dateSdf
	 * @param @return    
	 * @return List<CaseHandler>
	 */
	public List<CaseHandler> formatListBo(List<CaseHandler> list,String dateSdf){
		
		//时间格式转换
		for (CaseHandler caseHandler : list) {
			formatBo(caseHandler, dateSdf);
		}
		return list;
		
	}


}