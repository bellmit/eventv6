package cn.ffcs.zhsq.requestion.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.workflow.spring.RequestionService;
import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;
import cn.ffcs.zhsq.mybatis.persistence.requestion.ReqLinkMapper;
import cn.ffcs.zhsq.requestion.service.IReqLinkService;
import cn.ffcs.zhsq.requestion.service.IRequestionService;

/**
 * @Description: 诉求联动单位模块服务实现
 * @Author: caiby
 * @Date: 03-12 10:09:52
 * @Copyright: 2018 福富软件
 */
@Service("reqLinkServiceImpl")
@Transactional
public class ReqLinkServiceImpl implements IReqLinkService {

	@Autowired
	private ReqLinkMapper reqLinkMapper; //注入诉求联动单位模块dao
	@Autowired
	private RequestionService workflowService;
	@Autowired
	private IRequestionService requestionService; //注入诉求表模块服务
	
	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	@Override
	public Map<String, Object> insert(ReqLink bo,Map<String, Object> params) {
		Long rluId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			reqLinkMapper.insert(bo);
			rluId = bo.getRluId();
		} catch (Exception e) {
			resultMap.put("message", "子表单新增失败");
			System.out.println("-----表单新增失败------");
			e.printStackTrace();
		}
		if(rluId!=null){
			try {
				params.put("formId", rluId);
				resultMap = workflowService.startRequestionFlow(params);
			} catch (Exception e) {
				resultMap.put("message", "子工作流启动失败");
				System.out.println("-----工作流启动失败------");
				reqLinkMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 新增数据
	 * @param bo 诉求联动单位业务对象
	 * @return 诉求联动单位id
	 */
	@Override
	public Long insert(ReqLink bo) {
		reqLinkMapper.insert(bo);
		return bo.getRluId();
	}

	/**
	 * 修改数据
	 * @param bo 诉求联动单位业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(ReqLink bo) {
		long result = reqLinkMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 诉求联动单位业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(ReqLink bo) {
		long result = reqLinkMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求联动单位分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<ReqLink> list = reqLinkMapper.searchList(params, rowBounds);
		long count = reqLinkMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	public List<ReqLink> searchList(Map<String, Object> params){
		
		List<ReqLink> list = reqLinkMapper.searchList(params);
		return list;
	}
	/**
	 * 根据业务id查询数据
	 * @param id 诉求联动单位id
	 * @return 诉求联动单位业务对象
	 */
	@Override
	public ReqLink searchById(Long id) {
		ReqLink bo = reqLinkMapper.searchById(id);
		return bo;
	}
	
	public List<Map<String,Object>> queryWFtaskId(Map<String,Object> params){
		
		return reqLinkMapper.queryWFtaskId(params);
	}

	public List<ReqLink> showBackList(Map<String, Object> params){
		
		List<ReqLink> list = reqLinkMapper.showBackList(params);
		return list;
	}
}