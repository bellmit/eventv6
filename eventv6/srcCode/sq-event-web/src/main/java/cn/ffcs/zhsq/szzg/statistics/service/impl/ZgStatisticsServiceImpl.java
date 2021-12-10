package cn.ffcs.zhsq.szzg.statistics.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.domain.szzg.statistics.ZgStatistics;
import cn.ffcs.zhsq.mybatis.persistence.nanChang3D.NanChang3DMapper;
import cn.ffcs.zhsq.mybatis.persistence.szzg.statistics.ZgStatisticsMapper;
import cn.ffcs.zhsq.szzg.statistics.service.IZgStatisticsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value="zgStatisticsServiceImpl")
public class ZgStatisticsServiceImpl implements IZgStatisticsService {

	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private ZgStatisticsMapper zgStatisticsMapper;
	@Autowired
	private NanChang3DMapper nanChang3DMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplateOracle;

	@Override
	public List<ZgStatistics> findByParam(Map<String, Object> param) {
		return zgStatisticsMapper.findByParam(param);
	}

	@Override
	public int insertByList(List<ZgStatistics> list) {
		return zgStatisticsMapper.insertByList(list);
	}

	@Override
	public int updateByList(List<ZgStatistics> list) {
		return zgStatisticsMapper.updateByList(list);
	}
	
	/**
	 * 查询标题
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findTitle(Map<String,Object> param){
		return zgStatisticsMapper.findTitle(param);
	}
	
	public int deleteByParam(Map<String,Object> param){
		return zgStatisticsMapper.deleteByParam(param);
	}
	/**
	 * 根据参数查询是否存在
	 * @param stype
	 * @param smonth
	 * @param syear
	 * @return
	 */
	public int findCount(Map<String,Object> param){
		return zgStatisticsMapper.findCount(param);
	}
	/**
	 * 查询树形
	 * @param param
	 * @return
	 */
	public List<ZgStatistics> findTreeTable(Map<String,Object> param){
		return zgStatisticsMapper.findTreeTable(param);
	}
	/**
	 * 查询 12个月
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findChart12Month(Map<String,Object> param){
		return zgStatisticsMapper.findChart12Month(param);
	}

	@Override
	public List<Map<String, Object>> findGDPYearAndMonth(Map<String, Object> param) {
		return zgStatisticsMapper.findGDPYearAndMonth(param);
	}
	/**
	 * 查询 当年季度 或 季度每年趋势
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGDPYearOrMonth(Map<String,Object> param){
		return zgStatisticsMapper.findGDPYearOrMonth(param);
	}
	
	/**
	 * 区域 各年份
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGDPYears(Map<String,Object> param){
		return zgStatisticsMapper.findGDPYears(param);
	}
	
	@Override
	public List<Map<String, Object>> findStatisticsDate(Map<String, Object> param) {
		return zgStatisticsMapper.findStatisticsDate(param);
	}
	@Override
	public List<Map<String, Object>> findGeneralDate(Map<String, Object> param) {
		return zgStatisticsMapper.findGeneralDate(param);
	}
	/**
	 * 林地统计
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGeneralYear(Map<String,Object> param){
		return zgStatisticsMapper.findGeneralYear(param);
	}
	/**
	 * 林地对比数据
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGeneralByParam(Map<String,Object> param){
		if(param.get("orgCode") == null){//年,类型所有区域对比
			return zgStatisticsMapper.findGeneralTypeYear(param);
		}//某区域历史数据
		return zgStatisticsMapper.findGeneralOrgCode(param);
	}

	/**
	 * 获取事件热力图
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String,Object>  findEventHeatData(Map<String, Object> param) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = sdf.parse(param.get("beginTime").toString());//开始日期
		long beginL = beginDate.getTime();//开始日期时间戳
		long endL = sdf.parse(param.get("endTime").toString()).getTime();//结束日期时间戳
		int day = (int) ((endL-beginL)/(1000*3600*24));
		param.put("success", false);
		if(day <4) {//相隔日期小于4天
			param.put("msg", 1);
			return param;
		}
		int month = Integer.parseInt(param.get("month").toString());
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		c.add(Calendar.MONTH, month);
		if((endL - c.getTimeInMillis()) >0) {//相隔日期大于 month 个月
			param.put("msg", 2);
			return param;
		}
		c.setTime(beginDate);
		int dayCha = day/4;
		String[] dayArr = new String[5];
		
		dayArr[0] = sdf.format(c.getTime());
		for(int i=1;i<5;i++){//设置4个区间时间段
			c.add(Calendar.DATE, (i==4?(day%4):0)+dayCha);
			dayArr[i] =sdf.format(c.getTime());
		}
		StringBuffer sql =new StringBuffer();
		sql.append(" SELECT  m.x ||':' || m.y   FROM t_event t join t_dc_grid g on t.grid_id=g.grid_id ");
		sql.append(" join t_zy_res_marker m on t.event_id = m.resources_id  where g.status='001' and g.info_org_CODE LIKE  ? ||'%' ");
		sql.append("     AND t.STATUS in ( '00','01','02','03','04') and m.marker_type = '0301'  AND t.event_id BETWEEN ? and ? ");
		//sql.append(" ? and ? ");
		boolean hasEventType = CommonFunctions.isNotBlank(param, "eventType");
		if (hasEventType) {
			sql.append("   AND t.type_ LIKE ? ||'%' ");
		}
		jdbcTemplateOracle.setFetchSize(10000);
		Map<String, Object> idMap = new HashMap<String, Object>();
		for(int i=0,l=dayArr.length-1;i<l;i++){//为了前台直接使用4个范围内的数组
			param.put("beginTime", dayArr[i]+" 00:00:00");
			if(i+1 < l){
				param.put("endTime", dayArr[i+1]+" 00:00:00");
			}else{
				param.put("endTime", dayArr[i+1]+" 23:59:59");
			}
			long now = System.currentTimeMillis();
			idMap = zgStatisticsMapper.findEventIdByDate(param);
			param.put("idTime"+i, (System.currentTimeMillis() - now));
			if(idMap == null || idMap.get("MIN_ID")==null) {
				param.put("list"+(i+1),new ArrayList<String>());
				continue;
			}
			//+idMap.get("MIN_ID").toString()+" and " +idMap.get("MAX_ID").toString()
			Object[] paramObj = new Object[]{param.get("orgCode").toString(),idMap.get("MIN_ID").toString(),idMap.get("MAX_ID").toString()};
			if(hasEventType){
				paramObj = new Object[]{param.get("orgCode").toString(),idMap.get("MIN_ID").toString(),idMap.get("MAX_ID").toString(),param.get("eventType").toString()};
			}
			param.put("list"+(i+1), jdbcTemplateOracle.queryForList(sql.toString(),paramObj, String.class));
			if(param.get("MIN_ID") == null) {//存最小主键id
				param.put("MIN_ID", idMap.get("MIN_ID").toString());
			}
			if(idMap.get("MAX_ID") != null) {//循环覆盖最大主键id
				param.put("MAX_ID", idMap.get("MAX_ID").toString());
			}
			param.put("allTime"+i, (System.currentTimeMillis() - now));
		}
		param.put("dayArr", dayArr);
		param.put("success", true);
		return param;
	}
	
	
	/**
	 * 获取事件热力图(没有四个区间)
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String,Object>  findEventHeatMap(Map<String, Object> param) throws Exception {
		StringBuffer sql =new StringBuffer();

		
		sql.append(" SELECT  m.x ||':' || m.y as xy    FROM t_event t join t_dc_grid g on t.grid_id=g.grid_id ");
		sql.append(" join t_zy_res_marker m on t.event_id = m.resources_id  where g.status='001' and g.info_org_CODE LIKE  ? ||'%' ");
		sql.append("     AND t.STATUS in ( '00','01','02','03','04') and m.marker_type = '0301'   and m.map_type='5'   AND t.event_id BETWEEN ? and ? ");

		jdbcTemplateOracle.setFetchSize(100000);
		Map<String, Object> idMap = new HashMap<String, Object>();

		idMap = nanChang3DMapper.findEventIdByDate(param);
		
		
		if(idMap!=null && idMap!=null) {
			param.put("list", jdbcTemplateOracle.queryForList(sql.toString(),new Object[]{param.get("infoOrgCode").toString(),
					idMap.get("MIN_ID").toString(),idMap.get("MAX_ID").toString()}, String.class));
		}
		

		return param;
	}
	
	
	
	@Override
	public List<Map<String, Object>> findEventTop5(Map<String, Object> param) { 
		//param.put("substrNum", zgStatisticsMapper.findEventDatadictLength(null));
		return zgStatisticsMapper.findEventTop5(param);
	}
	public EUDGPagination findEventList(int pageNo, int pageSize, Map<String, Object> param) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds bounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		param.put("conditions", "conditions");//根据条件查询
		int count = zgStatisticsMapper.findEventKXCount(param);
		if(count>0) {
			list = formatDataList(zgStatisticsMapper.findEventList(param,bounds),param);
		}
		return new EUDGPagination(count, list);
	}
	private List<Map<String, Object>> formatDataList(List<Map<String, Object>> list,
			Map<String, Object> param) {
		//Long s = System.currentTimeMillis(); 
		param.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
		List<BaseDataDict> eventTypeDict = dictionaryService.findDataDictListByCodes(param);
		//Long e = System.currentTimeMillis();
		Map<String, BaseDataDict> dictGeneralCode = new HashMap<String, BaseDataDict>();
		Map<String, BaseDataDict> dictCode = new HashMap<String, BaseDataDict>();
		//e = System.currentTimeMillis();
		for (BaseDataDict b : eventTypeDict) {
			dictGeneralCode.put(b.getDictGeneralCode(), b);
			dictCode.put(b.getDictCode(), b);
		}
		//e = System.currentTimeMillis();
		for (Map<String,Object> m : list) {
			StringBuffer eventClass = new StringBuffer("");
			Object type_ = m.get("TYPE_");
			if(type_ == null){
				continue;
			}
			String type = type_.toString();
			BaseDataDict bdd = dictGeneralCode.get(type);
			if(bdd == null){
				continue;
			}
			do {
				eventClass.insert(0, "-"+bdd.getDictName());
				bdd = dictCode.get(bdd.getDictPcode());
				type = bdd.getDictCode();
				bdd = dictCode.get(type);
			} while (StringUtils.isNotBlank(type) && !ConstantValue.BIG_TYPE_PCODE.equals(type));
			
			if(eventClass.length() > 0) {
				m.put("TYPE_", eventClass.substring(1));
			}
		}
		//e = System.currentTimeMillis();
		return list;
	}

	/**
	 * 框选总数
	 */
	public int findEventCount(Map<String,Object> param){
		jdbcTemplateOracle.execute("{call PROC_EVENT_HEATMAP('"+param.get("orgCode").toString()+"','"+
				param.get("minID").toString()+"','"+param.get("maxID").toString()+"','"+
						param.get("points").toString()+"',"+param.get("dateNo").toString()+")}");
		return zgStatisticsMapper.findEventKXCount(param);
	}

	/**
	 * 招商引资-延平
	 * 字典：查询所有行业分类
	 */
	public List<String> findAttractInvestmentSort(Map<String,Object> param){
		return zgStatisticsMapper.findAttractInvestmentSort(param);
	}
	
	/**
	 * 招商引资-延平
	 * 柱形图：签约及开工项目情况
	 */
	public Map<String,Object> findAttractInvestmentBarData(Map<String,Object> param){
		return zgStatisticsMapper.findAttractInvestmentBarData(param);
	}
	
	/**
	 * 招商引资-延平
	 * 饼图:查询该年份的行业签约或开工占比
	 */
	public List<Map<String,Object>> findAttractInvestmentByYearAndStatus(Map<String,Object> param){
		return zgStatisticsMapper.findAttractInvestmentByYearAndStatus(param);
	}
	/**
	 * 删除旧框选数据
	 * @param param
	 */
	@Override
	public void delLastKXData(Map<String, Object> param) {
		zgStatisticsMapper.delLastKXData(param);
		
	}

	@Override
	public List<Map<String, Object>> findGridCountByToday(Map<String, Object> param) {
		List<Map<String, Object>> gridList = zgStatisticsMapper.findGridByParentCode(param);
		List<Map<String, Object>> countList = zgStatisticsMapper.findGridCountByToday(param);
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		Map<String,Object> e = new HashMap<>();
		for (Map<String, Object> c : countList) {
			for (int i=0,l= gridList.size();i<l;i++) {
				if(gridList.get(i).get("INFO_ORG_CODE").toString().equals(c.get("INFO_ORG_CODE").toString())) {
					gridList.get(i).put("COUNT", c.get("C").toString());
					list.add(gridList.get(i));
					e.put(i+"", true);
				}
			}
		}
		for (int i=0,l= gridList.size();i<l;i++) {
			if(e.get(i+"") ==null)
			list.add(gridList.get(i));
		}
		return list;
	}
	
	
}
