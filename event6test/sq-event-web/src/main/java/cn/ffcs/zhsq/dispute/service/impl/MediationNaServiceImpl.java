package cn.ffcs.zhsq.dispute.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.shequ.utils.DesUtil;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.zhsq.mybatis.persistence.eventsub.EventSubMapper;
import cn.ffcs.zhsq.mybatis.persistence.szzg.event.EventAndReportJsonpMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.dispute.service.IMediationNaService;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationNa;
import cn.ffcs.zhsq.mybatis.persistence.dispute.MediationNaMapper;

/**
 * @Description: 南安矛盾纠纷模块服务实现
 * @Author: 黄文斌
 * @Date: 06-03 09:52:42
 * @Copyright: 2020 福富软件
 */
@Service("mediationNaServiceImpl")
@Transactional
public class MediationNaServiceImpl implements IMediationNaService {

	@Autowired
	private MediationNaMapper mediationNaMapper; // 注入南安矛盾纠纷模块dao
	@Autowired
	private CacheService cacheService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private EventSubMapper eventSubMapper; // 注入event_na模块dao
	@Autowired
	private EventAndReportJsonpMapper eventAndReportJsonpMapper;

	/**
	 * 新增数据
	 *
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	@Override
	public Long insert(MediationNa bo) {
		mediationNaMapper.insert(bo);
		return bo.getId();
	}

	/**
	 * 修改数据
	 *
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(MediationNa bo) {
		long result = mediationNaMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 *
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(MediationNa bo) {
		long result = mediationNaMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 *
	 * @param params 查询参数
	 * @return 南安矛盾纠纷分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		long count = mediationNaMapper.countList(params);
		List<MediationNa> list = new ArrayList<>();
		if (count != 0) {
			list = mediationNaMapper.searchList(params, rowBounds);
			for (int i = 0, size = list.size(); i < size; i++) {
				list.get(i).setFlowName(CommonFunctions.replaceScopePath(list.get(i).getFlowName(), cacheService));// 去除顶级域名
			}
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 *
	 * @param id 南安矛盾纠纷id
	 * @return 南安矛盾纠纷业务对象
	 */
	@Override
	public MediationNa searchById(Long id) {
		MediationNa bo = mediationNaMapper.searchById(id);
		return bo;
	}

	/**
	 * csk 批量新增、更新数据
	 *
	 * @param mediationNaList 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id map.get(key)
	 */
	@Override
	public Long insertAll(List<MediationNa> mediationNaList) {

		if (mediationNaList == null || mediationNaList.size() == 0) {
			return 0l;
		}
		int l = mediationNaList.size();
		List<Map<String, String>> lo = null;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String> bdd = new HashMap<String, String>();
		// 先删除子表中关联ID的数据(成功返回true，失败返回false)
		deletDockingtype(mediationNaList);

		// 往子表中插入新增/更新的数据(成功返回true，失败返回false)
		insterDockingtype(mediationNaList);

//		//List<BaseDataDict> statusStr = baseDictionaryService.getDataDictListOfSinglestage("A001093101",null);
		List<BaseDataDict> typeStr = baseDictionaryService.getDataDictListOfSinglestage("A001093100", null);
		for (BaseDataDict b : typeStr) {
			bdd.put(b.getDictName(), b.getDictGeneralCode());
		}

		if (l == 1) {
			params.put("locality", mediationNaList.get(0).getLocality());
		}
		lo = mediationNaMapper.getCodebyName(params);
		for (Map<String, String> m : lo) {
			params.put(m.get("GRIDNAME"), m.get("INFOORGCODE"));
		}
		for (int i = 0; i < l; i++) {
			if (StringUtils.isNotBlank(mediationNaList.get(i).getLocality())) {
				if (params.get(mediationNaList.get(i).getLocality().trim()) != null) {
					mediationNaList.get(i)
							.setInfoorgcode(params.get(mediationNaList.get(i).getLocality().trim()).toString());
				}
			}
			if (StringUtils.isNotBlank(mediationNaList.get(i).getDisputeType())) {
				mediationNaList.get(i).setType(bdd.get(mediationNaList.get(i).getDisputeType().trim()));
			}
		}
		mediationNaMapper.insertAll(mediationNaList);
		return 12l;
	}

	/**
	 * csk 调用接口
	 */
	@Override
	public String detail(Long id, String url) {
		// 调用远程接口

//		System.setProperty("http.proxyHost", "192.168.25.25");//本地测试使用
//		System.setProperty("http.proxyPort", "6666");
//
		// 调用远程接口
		String date = "";

		try {
			date = HttpUtil.sendGet(url, "id=" + id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// base64解密 再des解密
		String result = "";
		byte[] bytes = java.util.Base64.getMimeDecoder().decode(date);
		try {
			DesUtil desUtil = new DesUtil();
			byte[] b = desUtil.decrypt(bytes);
			result = unicodeToCn(new String(b, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Unicode转中文
	 */
	public static String unicodeToCn(String param) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(param);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			param = param.replace(matcher.group(1), ch + "");
		}
		return param;
	}

	// 近12个月趋势
	@Override
	public List<Map<String, Object>> findAcquisitionTrendData(Map<String, Object> params) {
		return mediationNaMapper.findAcquisitionTrendData(params);
	}

	// top5 案件类型
	@Override
	public List<Map<String, Object>> findHotEventData(Map<String, Object> params) {
		return mediationNaMapper.findHotEventData(params);
	}

	// 获取网格名
	@Override
	public String searchGrid(Long id) {
		MediationNa date = mediationNaMapper.searchGrid(id);
		String gridname = CommonFunctions.replaceScopePath(date.getFlowName(), cacheService);
		return gridname;
	}

	// 获取排查数/化解数/化解率
	@Override
	public Map<String, Object> searchNum(Map<String, Object> params) {
		Map<String, Object> data = new HashMap<>();
		// 排查数
		Long count = 0l;
		// 化解数
		Long Resolution_count = 0l;
		params.put("status", 0);
		count = mediationNaMapper.searchNum(params);
		params.put("status", 1000);
		Resolution_count = mediationNaMapper.searchNum(params);
		data.put("Resolution_rate", "0%");
		// 化解率
		if (count != 0l) {
			String Resolution_rate = String.format("%.2f",
					((Resolution_count.doubleValue() / count.doubleValue()) * 100)) + "%";
			data.put("Resolution_rate", Resolution_rate);
		}
		data.put("count", count);
		data.put("Resolution_count", Resolution_count);
		return data;
	}

	// 南安化解率排名

	@Override
	public List<Map<String, Object>> searchPaiming(Map<String, Object> params) {
		return mediationNaMapper.searchPaiming(params);
	}

	// 南安七种对接类型
	@Override
	public boolean insterDockingtype(List<MediationNa> mediationNaList) {
		boolean result = false;
		if (mediationNaList == null || mediationNaList.size() == 0) {
			return result;
		}
		List<Map<String, Object>> data = new LinkedList<>();
		Map<String, String> bdd = new HashMap<String, String>();
		List<BaseDataDict> typeStr = baseDictionaryService.getDataDictListOfSinglestage("A001093102", null);
		for (BaseDataDict b : typeStr) {
			bdd.put(b.getDictName(), b.getDictGeneralCode());
		}
		for (int i = 0, l = mediationNaList.size(); i < l; i++) {
			if (mediationNaList.get(i).getDockingType() != null && mediationNaList.get(i).getDockingType() != " ") {
				String[] strings = mediationNaList.get(i).getDockingType().split(",");
				int s = strings.length;
				for (int j = 0; j < s; j++) {
					Map<String, Object> params = new HashMap<String, Object>();
					if (bdd.get(strings[j]) != null) {
						params.put("TYPE_VAL", bdd.get(strings[j]));
					} else {
						params.put("TYPE_VAL", strings[j]);
					}
					params.put("BIZ_ID", mediationNaList.get(i).getId());
					params.put("TABLE_NAME", "T_ZZ_DISPUTE_MEDIATION_NA");
					params.put("TYPE_NAME", "DOCKING_TYPE");
					data.add(params);
				}
			}
		}
		return eventSubMapper.insertList(data);
	}

	// 南安七种对接类型子表删除操作
	@Override
	public boolean deletDockingtype(List<MediationNa> mediationNaList) {
		boolean result = false;
		if (mediationNaList == null || mediationNaList.size() == 0) {
			return result;
		}
		List<Map<String, Object>> daletData = new LinkedList<>();
		for (int i = 0, l = mediationNaList.size(); i < l; i++) {
			if (mediationNaList.get(i).getId() != null && mediationNaList.get(i).getId() != 0) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("BIZ_ID", mediationNaList.get(i).getId());
				daletData.add(data);
			}
		}
		return eventSubMapper.delTypeRela(daletData);
	}

	// 南安纠矛盾纠纷汇聚
	@Override
	public List<Map<String, Object>> DisputesConfluence(Map<String, Object> params) {
		return mediationNaMapper.DisputesConfluence(params);
	}

//矛盾纠纷撒点查询
	@Override
	public EUDGPagination DisputesDotsJsonp(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<MediationNa> list = mediationNaMapper.DisputesDotsJsonp(params, rowBounds);
		long count = mediationNaMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	// 矛盾纠纷状态数量统计
	@Override
	public List<Map<String, Object>> findStatusData(Map<String, Object> params) {
		return mediationNaMapper.findStatusData(params);
	}

	// 矛盾纠纷七对接类型数量统计
	@Override
	public List<Map<String, Object>> findStatusNum(Map<String, Object> params) {
		return eventSubMapper.findStatusNum(params);
	}

	// 状态总数数据
	@Override
	public Map<String, Object> getTypeNum(Map<String, Object> params) {
		return mediationNaMapper.findTypeNum(params);
	}

	// 趋势数据
	@Override
	public List<Map<String, Object>> getDataTime(Map<String, Object> params) {
		if (CommonFunctions.isBlank(params, "createTimeStart") || CommonFunctions.isBlank(params, "createTimeEnd")) {
			return new ArrayList<Map<String, Object>>();
		}
		params.put("startDate", params.get("createTimeStart").toString());
		params.put("endDate", params.get("createTimeEnd").toString());
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.startDay2EndDay(params);

		List<Map<String, Object>> dataList = mediationNaMapper.findDataTime(params);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (Map<String, Object> map : dataList) {// 先保存所有日期有值
			dataMap.put(map.get("trendDate").toString(), map.get("TOTAL_").toString());
		}
		String date_ = "";
		// 每个日期存入数量
		for (Map<String, Object> map : dayList) {
			date_ = map.get("DATE_").toString();
			map.put("trendDate", date_);
			map.put("TOTAL", dataMap.get(date_) != null ? dataMap.get(date_).toString() : "0");

		}
		return dayList;
	}

	/*
	 * public final static DateFormat dateFormat_Year = new
	 * SimpleDateFormat("yyyy"); public final static DateFormat dateFormat_Month =
	 * new SimpleDateFormat("yyyy-MM"); public final static DateFormat
	 * dateFormat_Day = new SimpleDateFormat("yyyy-MM-dd"); public final static
	 * Map<String, Map<String,Object>> TrendFormat = new HashMap<String,
	 * Map<String,Object>>() { { put("year", new HashMap<String, Object>() { {
	 * put("format", dateFormat_Year); put("trend", Calendar.YEAR); } });
	 * put("month", new HashMap<String, Object>() { { put("format",
	 * dateFormat_Month); put("trend", Calendar.MONTH); } }); put("day", new
	 * HashMap<String, Object>() { { put("format", dateFormat_Day); put("trend",
	 * Calendar.DATE); } }); } };
	 * 
	 *//***
		 * 获取两个时间段的年份/月份/天
		 * 
		 * @param startTime
		 * @param endTime
		 * @param trendType year month day
		 * @return
		 *//*
			 * public List<String> getTrendTime(String startTime, String endTime,String
			 * trendType) { List<String> res=new ArrayList<String>(); try {
			 * if(TrendFormat.containsKey(trendType)) {
			 * 
			 * Date start = dateFormat_Day.parse(startTime); Date end =
			 * dateFormat_Day.parse(endTime); Calendar tempStart = Calendar.getInstance();
			 * tempStart.setTime(start); Calendar tempEnd = Calendar.getInstance();
			 * tempEnd.setTime(end); DateFormat thisFormat=(DateFormat)
			 * TrendFormat.get(trendType).get("format"); Integer thisTrend=(Integer)
			 * TrendFormat.get(trendType).get("trend"); while
			 * (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) { String
			 * thisTemp=thisFormat.format(tempStart.getTime()); res.add(thisTemp);
			 * tempStart.add(thisTrend,1); } } } catch (Exception e) { e.printStackTrace();
			 * } return res; }
			 */

	/**
	 * 矛盾纠纷 区间每日汇总
	 */
	@Override
	public List<Map<String, Object>> fetchEventDay4Jsonp(Map<String, Object> params) {
		return mediationNaMapper.fetchEventDay(params);
	}

}
