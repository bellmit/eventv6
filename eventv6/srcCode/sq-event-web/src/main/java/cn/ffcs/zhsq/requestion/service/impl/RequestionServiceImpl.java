package cn.ffcs.zhsq.requestion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.workflow.spring.RequestionService;
import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;
import cn.ffcs.zhsq.mybatis.persistence.requestion.RequestionMapper;
import cn.ffcs.zhsq.requestion.service.IReqLinkService;
import cn.ffcs.zhsq.requestion.service.IRequestionService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 诉求表模块服务实现
 * @Author: caiby
 * @Date: 03-12 08:45:59
 * @Copyright: 2018 福富软件
 */
@Service("requestionServiceImpl")
@Transactional
public class RequestionServiceImpl implements IRequestionService {

	@Autowired
	private RequestionMapper requestionMapper; //注入诉求表模块dao
	@Autowired
	private RequestionService workflowService;
	@Autowired
	private IBaseDictionaryService dictionaryService;
	
	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	@Override
	public Long insert(Requestion bo) {
		requestionMapper.insert(bo);
		return bo.getReqId();
	}
	
	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	@Override
	public Map<String, Object> insert(Requestion bo,Map<String, Object> params) {
		Long reqId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			bo.setCreatTime(new Date());
			requestionMapper.insert(bo);
			reqId = bo.getReqId();
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "表单新增失败");
			System.out.println("-----表单新增失败------");
			e.printStackTrace();
		}
		if(reqId!=null){
			try {
				params.put("formId", reqId);
				resultMap = workflowService.startRequestionFlow(params);
				resultMap.put("reqId", reqId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				requestionMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}

	/**
	 * 修改数据
	 * @param bo 诉求表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(Requestion bo) {
		long result = requestionMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 诉求表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Requestion bo) {
		long result = requestionMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Requestion> list = requestionMapper.searchList(params, rowBounds);
		long count = requestionMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchDBList(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countDBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Requestion> list = requestionMapper.searchJBList(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countJBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Requestion> list = requestionMapper.searchAllList(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countAllList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	@Override
	public Requestion searchById(Long id,String formTypeId) {
		Requestion bo = requestionMapper.searchById(id,formTypeId);
		if(bo!=null){
			List<Requestion> list = new ArrayList<Requestion>();
			list.add(bo);
			formate(list, "");
		}
		return bo;
	}
	
	public Requestion searchByKeyId(Long keyId){
		
		Requestion bo = null;
		List<Requestion> tempList = requestionMapper.searchByKeyId(keyId);
		if(!tempList.isEmpty()){
			bo = tempList.get(0);
			List<Requestion> list = new ArrayList<Requestion>();
			list.add(bo);
			formate(list, "");
		}
		return bo;
	}
	
	private void formate(List<Requestion> requestionList, String orgCode) {
		List<BaseDataDict> typeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.CASE_TYPE_CODE, orgCode);
		if(requestionList!=null && requestionList.size()>0){
			for(Requestion re : requestionList){
				if(null==re.getType()) continue;
				for(BaseDataDict dataDict : typeList){
					if(re.getType().equals(dataDict.getDictGeneralCode())){
						re.setTypeStr(dataDict.getDictName());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchDBList_Main(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchDBList_Main(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countDBList_Main(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchDBList_Task(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchDBList_Task(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countDBList_Task(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchJBList_Task(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchJBList_Task(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countJBList_Task(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchAllList_Main(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchAllList_Main(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countAllList_Main(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	@Override
	public EUDGPagination searchList_Main(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); 
		List<Requestion> list = requestionMapper.searchList_Main(params, rowBounds);
		formate(list, params.get("orgCode")==null?"":params.get("orgCode").toString());
		long count = requestionMapper.countRequest(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * PC端根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	@Override
	public Requestion searchBy_id(Long id) {
		
		Requestion bo = requestionMapper.searchBy_id(id);
		if(bo!=null){
			List<Requestion> list = new ArrayList<Requestion>();
			list.add(bo);
			formate(list, "");
		}
		return bo;
	}
	
	/**
	 * 消息提醒代办数量
	 */
	public long countDB(Long orgId){
		
		long count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		String formTypeId = workflowService.getFormTypeId("requestion");
		String formTypeId_ = workflowService.getFormTypeId("linkageunit");
		
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
		params.put("dbOrgId", orgId.toString()); 
		
		count = requestionMapper.countDB(params);
		
		return count;
	}
}