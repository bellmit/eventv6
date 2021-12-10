package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.DoubleRandomTaskService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTask;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTaskStatistics;
import cn.ffcs.zhsq.mybatis.persistence.event.DoubleRandomTaskMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;

/**
 * @Description: 双随机任务模块服务实现
 * @Author: dingyw
 * @Date: 11-07 10:15:17
 * @Copyright: 2017 福富软件
 */
@Service("doubleRandomTaskServiceImpl")
@Transactional
public class DoubleRandomTaskServiceImpl implements DoubleRandomTaskService {

	@Autowired
	private DoubleRandomTaskMapper doubleRandomTaskMapper; //注入双随机任务模块dao
	@Autowired
	private IBaseDictionaryService dictionaryService;
	/**
	 * 新增数据
	 * @param bo 双随机任务业务对象
	 * @return 双随机任务id
	 */
	@Override
	public Long insert(DoubleRandomTask bo) {
		doubleRandomTaskMapper.insert(bo);
		return bo.getId();
	}

	/**
	 * 修改数据
	 * @param bo 双随机任务业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DoubleRandomTask bo) {
		long result = doubleRandomTaskMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 双随机任务业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DoubleRandomTask bo) {
		long result = doubleRandomTaskMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 双随机任务分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DoubleRandomTask> list = doubleRandomTaskMapper.searchList(params, rowBounds);
		long count = doubleRandomTaskMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	@Override
	public EUDGPagination findEventPageList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventDisposal> list = doubleRandomTaskMapper.findEventPageList(params, rowBounds);
		if(list != null){
			String orgCode = (String)params.get("orgCode");
			formatData(list,orgCode);
		}
		long count = doubleRandomTaskMapper.countEventList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	@Cacheable(value = "baseCache")
	private List<BaseDataDict> getDictList(String dictCode,String orgCode){
		return dictionaryService.getDataDictListOfSinglestage(dictCode,orgCode);
	}
	/**
	 * 格式化输出事件数据
	 * @param eventList	事件列表
	 * @param orgCode	组织编码
	 */
	@Cacheable(value = "baseCache")
	private void formatData(List<EventDisposal> eventList, String orgCode) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			List<BaseDataDict> urgencyDegreeDict = getDictList(ConstantValue.URGENCY_DEGREE_PCODE, orgCode),
							   influenceDegreeDict = getDictList(ConstantValue.INFLUENCE_DEGREE_PCODE, orgCode),
							   sourceDict = getDictList(ConstantValue.SOURCE_PCODE, orgCode),
							   involvedNumDict = getDictList(ConstantValue.INVOLVED_NUM_PCODE, orgCode),
							   statusDict = getDictList(ConstantValue.STATUS_PCODE, orgCode),
							   subStatusDict = getDictList(ConstantValue.SUB_STATUS_PCODE, orgCode),
							   collectWayDict = getDictList(ConstantValue.COLLECT_WAY_PCODE, orgCode),
							   eventTypeDict = dictionaryService.findDataDictListByCodes(dictMap);
			
			String eventType = "",
				   urgencyDegree = "",
				   influenceDegree = "",
				   source = "",
				   involvedNum = "",
				   status = "",
				   subStatus = "",
				   collectWay = "";
			
			for(EventDisposal event : eventList) {
				// 设置事件分类的值 大类+小类
				eventType = event.getType();
				urgencyDegree = event.getUrgencyDegree();
				influenceDegree = event.getInfluenceDegree();
				source = event.getSource();
				involvedNum = event.getInvolvedNum();
				status = event.getStatus();
				subStatus = event.getSubStatus();
				collectWay = event.getCollectWay();
				
				if(StringUtils.isNotBlank(eventType) && eventTypeDict != null) {
					StringBuffer eventClass = new StringBuffer("");
					String bigType = eventType, bigTypeName = "", bigDictCode = null;
					
					do {
						bigTypeName = "";
						
						for(BaseDataDict dataDict : eventTypeDict) {
							if((StringUtils.isNotBlank(bigDictCode) && !ConstantValue.BIG_TYPE_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
								||
								(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
								bigTypeName = dataDict.getDictName();
								bigDictCode = dataDict.getDictPcode();
								bigType = null;
								break;
							}
						}
						
						if(StringUtils.isNotBlank(bigTypeName)) {
							eventClass.insert(0, bigTypeName).insert(0, "-");
						}
					} while(StringUtils.isNotBlank(bigTypeName));
					
					if(eventClass.length() > 0) {
						event.setTypeName(eventClass.substring(eventClass.lastIndexOf("-") + 1));
						event.setEventClass(eventClass.substring(1));
					}
				}
				
				if (StringUtils.isNotBlank(urgencyDegree)) {// 紧急程度
					try {
						DataDictHelper.setDictValueForField(event, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				if (StringUtils.isNotBlank(influenceDegree)) {// 影响范围
					try {
						DataDictHelper.setDictValueForField(event, "influenceDegree", "influenceDegreeName", influenceDegreeDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				} else {
					event.setInfluenceDegreeName("");
				}
				if (StringUtils.isNotBlank(source)) {// 信息来源
					try {
						DataDictHelper.setDictValueForField(event, "source", "sourceName", sourceDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				// 涉及人数
				if (StringUtils.isNotBlank(involvedNum)) {
					try {
						DataDictHelper.setDictValueForField(event, "involvedNum", "involvedNumName", involvedNumDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				if (StringUtils.isNotBlank(status)) {// 事件状态
					try {
						DataDictHelper.setDictValueForField(event, "status", "statusName", statusDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				// 事件子状态
				if (StringUtils.isNotBlank(subStatus)) {
					try {
						DataDictHelper.setDictValueForField(event, "subStatus", "subStatusName", subStatusDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
					
					if(StringUtils.isNotBlank(event.getSubStatusName())) {
						if(ConstantValue.REJECT_SUB_STATUS.equals(subStatus)) {
							event.setStatusName(event.getSubStatusName());
						} else {
							event.setStatusName(event.getStatusName()+"-"+event.getSubStatusName());
						}
					}
				}
				if (StringUtils.isNotBlank(collectWay)) {// 采集渠道
					try {
						DataDictHelper.setDictValueForField(event, "collectWay", "collectWayName", collectWayDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 根据业务id查询数据
	 * @param id 双随机任务id
	 * @return 双随机任务业务对象
	 */
	@Override
	public DoubleRandomTask searchById(Long id) {
		DoubleRandomTask bo = doubleRandomTaskMapper.searchById(id);
		return bo;
	}

	@Override
	public List<DoubleRandomTaskStatistics> statisticsByCountry(Map<String, Object> params){
		return doubleRandomTaskMapper.statisticsByCountry(params);
	}

	@Override
	public List<EventDisposal> getRandomEvent(String orgCode,String startTime, String endTime,int size) {
		List<EventDisposal> list = doubleRandomTaskMapper.getRandomEvent(orgCode,startTime, endTime, size);
		if(list != null){
			formatData(list,orgCode);
		}
		return list;
	}

	@Override
	public EUDGPagination findDcEventPageList(int page, int rows,Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventDisposal> list = doubleRandomTaskMapper.findDcEventPageList(params, rowBounds);
		if(list != null){
			String orgCode = (String)params.get("orgCode");
			formatData(list,orgCode);
		}
		long count = doubleRandomTaskMapper.countDcEventList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public EUDGPagination findAssignEvent(int page, int rows,Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventDisposal> list = doubleRandomTaskMapper.getAssignEvent(params, rowBounds);
		if(list != null){
			String orgCode = (String)params.get("orgCode");
			formatData(list,orgCode);
		}
		long count = doubleRandomTaskMapper.countAssignEvent(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public long getCount(Map<String, Object> params) {
		long count = doubleRandomTaskMapper.countEventList(params);
		return count;
	}
	
	
}