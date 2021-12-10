package cn.ffcs.zhsq.hotLine.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.hotLine.service.HotlineService;
import cn.ffcs.zhsq.mybatis.persistence.hotLine.HotlineMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import net.sf.json.JSONArray;

/**
 * @Description: T_HOTLINE模块服务实现
 * @Author: wuxq
 * @Date: 09-08 17:02:47
 * @Copyright: 2020 福富软件
 */
@Service("hotlineServiceImpl")
@Transactional
public class HotlineServiceImpl implements HotlineService {

	@Autowired
	private HotlineMapper hotlineMapper; // 注入T_HOTLINE模块dao
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	/**
	 * 新增数据
	 * 
	 * @param bo
	 *            T_HOTLINE业务对象
	 * @return 是否新增成功
	 */
	@Override
	public boolean insert(Map<String, Object> hotLine) {
		try {
			if (CommonFunctions.isNotBlank(hotLine, "callTime")) {

				hotLine.put("callTime",DateUtils.convertStringToDate(hotLine.get("callTime").toString(), 
						DateUtils.PATTERN_24TIME));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hotlineMapper.insert(hotLine)>0;
	}

	/**
	 * 修改数据
	 * 
	 * @param bo
	 *            T_HOTLINE业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(Map<String, Object> hotLine) {
		return hotlineMapper.update(hotLine) > 0;
	}

	/**
	 * 删除数据
	 * 
	 * @param bo
	 *            T_HOTLINE业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Map<String, Object> hotLine) {
		return hotlineMapper.delete(hotLine) > 0;
	}

	/**
	 * 查询数据（分页）
	 * 
	 * @param params
	 *            查询参数
	 * @return T_HOTLINE分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

		int count = hotlineMapper.countList(params);
		List<Map<String, Object>> hotLineList = new ArrayList<Map<String, Object>>();

		if (count > 0) {
			hotLineList = hotlineMapper.searchList(params, rowBounds);
			formatHotLineOut(hotLineList);
		}

		EUDGPagination eventVerifyPagination = new EUDGPagination(count, hotLineList);

		return eventVerifyPagination;
	}

	/**
	 * 根据业务id查询数据
	 * 
	 * @param id
	 *            T_HOTLINEid
	 * @return T_HOTLINE业务对象
	 */
	@Override
	public Map<String, Object> searchByCaseNo(String caseNo) {
		return hotlineMapper.searchByCaseNo(caseNo);
	}

	/**
	 * 格式化输出数据
	 * 
	 * @param hotLineList
	 */
	private void formatHotLineOut(List<Map<String, Object>> hotLineList) {
		if (hotLineList != null) {
			Date callTime = null;
			List<BaseDataDict> eventStatusDict = null, sourcesDict = null, appealTypeDict = null, ageDict = null,
					eventTypeDict = null;

			eventStatusDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.HOTLINE_EVENT_STATUS,
					null);
			sourcesDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.HOTLINE_SOURCES, null);
			appealTypeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.HOTLINE_APPEAL_TYPE,
					null);
			eventTypeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.HOTLINE_EVENT_TYPE, null);

			for (Map<String, Object> hotline : hotLineList) {
				callTime = null;
				if (CommonFunctions.isNotBlank(hotline, "callTime")) {
					callTime = (Date) hotline.get("callTime");
				}

				if (callTime != null) {
					hotline.put("callTimeStr", DateUtils.formatDate(callTime, DateUtils.PATTERN_24TIME));
				}

				DataDictHelper.setDictValueForField(hotline, "sources", "sourcesName", sourcesDict);
				DataDictHelper.setDictValueForField(hotline, "appealType", "appealTypeName", appealTypeDict);
				DataDictHelper.setDictValueForField(hotline, "age", "ageName", ageDict);
				DataDictHelper.setDictValueForField(hotline, "eventStatus", "eventStatusName", eventStatusDict);
				DataDictHelper.setDictValueForField(hotline, "underType", "eventTypeName", eventTypeDict);

			}
		}
	}

	@Override
	public boolean insertAll(Map<String, Object> hotLine) {
		try {
			if (CommonFunctions.isNotBlank(hotLine, "dataList")) {
				List<Map<String,Object>> dataList = JSONArray.fromObject(hotLine.get("dataList").toString());
				if (null != dataList && dataList.size()>0) {
					hotlineMapper.insertAll(dataList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}