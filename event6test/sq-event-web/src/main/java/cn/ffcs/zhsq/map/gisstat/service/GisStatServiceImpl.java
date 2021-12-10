package cn.ffcs.zhsq.map.gisstat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisHeatMapInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value="gisStatServiceImpl")
public class GisStatServiceImpl implements IGisStatService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private IFunConfigurationService configurationService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private ArcgisInfoMapper arcgisInfoMapper;

	@Override
	public List<Map<String, Object>> findRentRoom(Long gridId) {
		String currMonth = DateUtils.getToday("yyyy-MM");
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G.GRID_ID ID_, \n");
		sqlSB.append("       NVL(B.GRID_NUMS, 0) GRID_NUMS, \n");
		sqlSB.append("       NVL(B.UT_TC, 0) UT_TC, \n");
		sqlSB.append("       NVL(T.RS_TOTAL, 0) TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RENTAL T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.REGION_CODE \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		sqlSB.append("  LEFT JOIN T_STAT_BASE B \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = B.INFO_ORG_CODE \n");
		sqlSB.append("   AND B.AREA_CODE = 'COMMON' \n");
		sqlSB.append("   AND B.STAT_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND B.STAT_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
		args.add(currMonth);args.add(currMonth);
		args.add(currMonth);args.add(currMonth);args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if (gridInfo != null) {
			String exp = configurationService.turnCodeToValue("RENT_ROOM_BGCOLOR", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, gridInfo.getInfoOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if (StringUtils.isNotBlank(exp)) {
				// 出租屋平均数
				sqlSB.setLength(0);
				sqlSB.append("SELECT ROUND(SUM(NVL(T.RS_TOTAL, 0)) / COUNT(0)) RS_TOTAL \n");
				sqlSB.append("  FROM T_DC_GRID G \n");
				sqlSB.append("  LEFT JOIN T_BI_RENTAL T \n");
				sqlSB.append("    ON G.INFO_ORG_CODE = T.REGION_CODE \n");
				sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
				sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
				sqlSB.append(" WHERE G.STATUS = '001' \n");
				sqlSB.append("   AND G.PARENT_GRID_ID = (SELECT PARENT_GRID_ID FROM T_DC_GRID WHERE STATUS = '001' AND GRID_ID = ?) \n");
				logger.info("查询SQL语句：" + sqlSB.toString());
				int RENT_ROOM_AVG_NUM = queryForInt(sqlSB.toString(), new Object[] {currMonth,currMonth,gridId});
				
				exp = exp.replaceAll("＞", ">").replaceAll("＜", "<").replaceAll("＇", "'").replaceAll("＂", "\"");
				List<Variable> variables = new ArrayList<Variable>();
				variables.add(Variable.createVariable("出租屋数", 0));
				variables.add(Variable.createVariable("子网格数", 0));
				variables.add(Variable.createVariable("辖区单元数", 0));
				variables.add(Variable.createVariable("出租屋均数", 0));
				PreparedExpression pe = ExpressionEvaluator.preparedCompile(exp, variables);
				for (Map<String, Object> data : list) {
					pe.setArgument("出租屋数", Integer.valueOf(String.valueOf(data.get("TOTAL_"))));
					pe.setArgument("子网格数", Integer.valueOf(String.valueOf(data.get("GRID_NUMS"))));
					pe.setArgument("辖区单元数", Integer.valueOf(String.valueOf(data.get("UT_TC"))));
					pe.setArgument("出租屋均数", RENT_ROOM_AVG_NUM);
					Object o = pe.execute();
					data.put("COLOR_", o);
				}
			}
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getRealPeople(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G.GRID_ID ID_, SUM(NVL(T.TOTAL_, 0)) TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RS_CLASS_STAT T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE AND T.TYPE_ IN ('S', 'W', 'LB', 'OS') \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
		sqlSB.append(" GROUP BY G.GRID_ID \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getRealPeopleDetail(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT SUM(DECODE(T.TYPE_, 'S', NVL(T.TOTAL_, 0), 0)) S, \n");
		sqlSB.append("       SUM(DECODE(T.TYPE_, 'W', NVL(T.TOTAL_, 0), 0)) W, \n");
		sqlSB.append("       SUM(DECODE(T.TYPE_, 'LB', NVL(T.TOTAL_, 0), 0)) LB, \n");
		sqlSB.append("       SUM(DECODE(T.TYPE_, 'OS', NVL(T.TOTAL_, 0), 0)) OS \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RS_CLASS_STAT T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.GRID_ID = ? \n");
		sqlSB.append("   AND T.TYPE_ IN ('S', 'M', 'LB', 'OS') \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		Map<String, Object> infoMap = jdbcTemplate.queryForMap(sqlSB.toString(), args.toArray());
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, Object> info : infoMap.entrySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			if ("S".equals(info.getKey())) {
				map.put("TEXT_", "流动人口");
				map.put("TOTAL_", info.getValue());
			} else if ("W".equals(info.getKey())) {
				map.put("TEXT_", "台胞");
				map.put("TOTAL_", info.getValue());
			} else if ("LB".equals(info.getKey())) {
				map.put("TEXT_", "留守人员");
				map.put("TOTAL_", info.getValue());
			} else if ("OS".equals(info.getKey())) {
				map.put("TEXT_", "境外人员");
				map.put("TOTAL_", info.getValue());
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public Map<String, Object> getRentRoomRelatedInfo(Long gridId, Long parentGridId, String infoOrgCode) {
		String currMonth = DateUtils.getToday("yyyy-MM");
		Map<String, Object> rInfo = new HashMap<String, Object>();
		StringBuffer sqlSB = new StringBuffer();
		// 综治机构、机构成员
		sqlSB.append("SELECT COUNT(T3.TEAM_ID) TEAM_SUM, NVL(SUM(T3.MEMBER_SUM), 0) TEAM_MEMBER_SUM \n");
		sqlSB.append("  FROM (SELECT T1.TEAM_TYPE, \n");
		sqlSB.append("               T1.TEAM_ID, \n");
		sqlSB.append("               (SELECT COUNT(1) \n");
		sqlSB.append("                  FROM T_ZZ_TEAM_MEMBERS T2 \n");
		sqlSB.append("                 WHERE T1.TEAM_ID = T2.TEAM_ID \n");
		sqlSB.append("                   AND T2.STATUS = '001') MEMBER_SUM \n");
		sqlSB.append("          FROM T_ZZ_PRVETION_TEAM T1 \n");
		sqlSB.append("         WHERE T1.BIZ_TYPE = '0' \n");
		sqlSB.append("           AND T1.STATUS = '1' \n");
		sqlSB.append("           AND T1.REGION_CODE LIKE CONCAT(?, '%')) T3 \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		Map<String, Object> map = jdbcTemplate.queryForMap(sqlSB.toString(), new Object[] {infoOrgCode});
		rInfo.put("TEAM_SUM", map.get("TEAM_SUM"));
		rInfo.put("TEAM_MEMBER_SUM", map.get("TEAM_MEMBER_SUM"));
		// 群防群治组织、成员
		sqlSB.setLength(0);
		sqlSB.append("SELECT COUNT(T3.TEAM_ID) TEAM_SUM, NVL(SUM(T3.MEMBER_SUM), 0) TEAM_MEMBER_SUM \n");
		sqlSB.append("  FROM (SELECT T1.TEAM_TYPE, \n");
		sqlSB.append("               T1.TEAM_ID, \n");
		sqlSB.append("               (SELECT COUNT(1) \n");
		sqlSB.append("                  FROM T_ZZ_TEAM_MEMBERS T2 \n");
		sqlSB.append("                 WHERE T1.TEAM_ID = T2.TEAM_ID \n");
		sqlSB.append("                   AND T2.STATUS = '001') MEMBER_SUM \n");
		sqlSB.append("          FROM T_ZZ_PRVETION_TEAM T1 \n");
		sqlSB.append("         WHERE T1.BIZ_TYPE = '1' \n");
		sqlSB.append("           AND T1.STATUS = '1' \n");
		sqlSB.append("           AND T1.REGION_CODE LIKE CONCAT(?, '%')) T3 \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		map = jdbcTemplate.queryForMap(sqlSB.toString(), new Object[] {infoOrgCode});
		rInfo.put("ORG_TEAM_SUM", map.get("TEAM_SUM"));
		rInfo.put("ORG_TEAM_MEMBER_SUM", map.get("TEAM_MEMBER_SUM"));
		
		// 出租屋数
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RENTAL T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.REGION_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.GRID_ID = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		int RENT_ROOM_NUM = queryForInt(sqlSB.toString(), new Object[] {gridId,currMonth,currMonth});
		rInfo.put("RENT_ROOM_NUM", RENT_ROOM_NUM);
		
		// 出租屋平均数
		sqlSB.setLength(0);
		sqlSB.append("SELECT ROUND(SUM(NVL(T.RS_TOTAL, 0)) / COUNT(0)) RS_TOTAL \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RENTAL T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.REGION_CODE \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		int RENT_ROOM_AVG_NUM = queryForInt(sqlSB.toString(), new Object[] {currMonth,currMonth,parentGridId});
		rInfo.put("RENT_ROOM_AVG_NUM", RENT_ROOM_AVG_NUM);
		rInfo.put("IS_SHOW_RENT_ROOM", RENT_ROOM_NUM > RENT_ROOM_AVG_NUM ? "1" : "0");
		
		// 网格力量
		sqlSB.setLength(0);
		sqlSB.append("SELECT COUNT(*) RS_TOTAL \n");
		sqlSB.append("  FROM T_DC_GRID_ADMIN T \n");
		sqlSB.append("  LEFT JOIN T_DC_GRID G \n");
		sqlSB.append("    ON T.GRID_ID = G.GRID_ID \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND T.STATUS = '001' \n");
		sqlSB.append("   AND G.INFO_ORG_CODE LIKE ? || '%' \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("GRIDADMIN_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode}));
		
		// 党员 流动人口 志愿者
		sqlSB.setLength(0);
		sqlSB.append("SELECT SUM(DECODE(T.TYPE_, 'A', NVL(T.TOTAL_, 0), 0)) A, \n");
		sqlSB.append("       SUM(DECODE(T.TYPE_, 'S', NVL(T.TOTAL_, 0), 0)) S, \n");
		sqlSB.append("       SUM(DECODE(T.TYPE_, 'VO', NVL(T.TOTAL_, 0), 0)) VO \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RS_CLASS_STAT T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.GRID_ID = ? \n");
		sqlSB.append("   AND T.TYPE_ IN ('A', 'S', 'VO') \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		map = jdbcTemplate.queryForMap(sqlSB.toString(), new Object[] {gridId});
		rInfo.put("PARTY_NUM", map.get("A"));
		rInfo.put("VOLUNTEER_NUM", map.get("VO"));
		// 流动人口
		sqlSB.setLength(0);
		sqlSB.append("SELECT SUM(NVL(T.TOTAL_, 0)) RS_TOTAL \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RS_RENT_FLOW T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.GRID_ID = ? \n");
		sqlSB.append("   and length(T.region_code) = 2 \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		int LD_POPU_NUM = queryForInt(sqlSB.toString(), new Object[] {gridId});
		rInfo.put("LD_POPU_NUM", LD_POPU_NUM);
		
		// 流动人口平均数
		sqlSB.setLength(0);
		sqlSB.append("SELECT ROUND(SUM(NVL(T.TOTAL_, 0)) / COUNT(0)) RS_TOTAL \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN T_BI_RS_RENT_FLOW T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		int LD_POPU_AVG_NUM = queryForInt(sqlSB.toString(), new Object[] {parentGridId});
		rInfo.put("LD_POPU_AVG_NUM", LD_POPU_AVG_NUM);
		rInfo.put("IS_SHOW_LD_POPU", LD_POPU_NUM > LD_POPU_AVG_NUM ? "1" : "0");
		
		// 艾滋病
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL FROM T_BI_AIDS_PERSONS T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("AIDS_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		// 吸毒
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL FROM T_BI_DRUG T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("DRUG_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		// 社区矫正
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL FROM T_BI_CORRECT T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("CORRECT_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		// 刑满释放
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RELEASED_RS_TOTAL, 0) RS_TOTAL FROM T_BI_RELEASED T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("RELEASED_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		// 精神病
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL FROM T_BI_PSYCHIATRIC T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("PSYCHIATRIC_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		// 重点青少年
		sqlSB.setLength(0);
		sqlSB.append("SELECT NVL(T.RS_TOTAL, 0) RS_TOTAL FROM T_BI_YOUTH T WHERE T.REGION_CODE = ? \n");
		sqlSB.append("   AND T.STATICS_DATE >= TO_DATE(?, 'YYYY-MM') \n");
		sqlSB.append("   AND T.STATICS_DATE < ADD_MONTHS(TO_DATE(?, 'YYYY-MM'), 1) \n");
		logger.info("查询SQL语句：" + sqlSB.toString());
		rInfo.put("YOUTH_NUM", queryForInt(sqlSB.toString(), new Object[] {infoOrgCode,currMonth,currMonth}));
		
		
		return rInfo;
	}
	
	private int queryForInt(String sql, Object[] args) {
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, args);
			if (map.get("RS_TOTAL") == null) return 0;
			else return Integer.valueOf(String.valueOf(map.get("RS_TOTAL")));
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}
	
	@Override
	public List<Map<String, Object>> getMajorRelatedEvents(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G1.GRID_ID, (SELECT COUNT(1) FROM T_DC_GRID G \n");
		sqlSB.append(" LEFT JOIN T_ZZ_RELATED_EVENTS T ON G.INFO_ORG_CODE = T.GRID_CODE \n");
		sqlSB.append(" WHERE G.STATUS = '001' AND T.STATUS = '1' AND T.BIZ_TYPE = '3' AND T.GRID_CODE LIKE \n");
		sqlSB.append(" (SELECT G2.INFO_ORG_CODE FROM T_DC_GRID G2 WHERE G2.GRID_ID = G1.GRID_ID) || '%') BASENUM \n");
		sqlSB.append(" FROM T_DC_GRID G1 WHERE G1.STATUS = '001' AND G1.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getSchoolRelatedEvents(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G1.GRID_ID, G1.GRID_NAME, (SELECT count(1) FROM T_ZZ_RELATED_EVENTS T1 \n");
		sqlSB.append(" LEFT JOIN T_ZZ_PLA_INFO PLA ON T1.BIZ_ID = PLA.PLA_ID LEFT JOIN T_DC_GRID T2 \n");
		sqlSB.append(" ON PLA.GRID_ID = T2.GRID_ID WHERE T1.STATUS = '1' AND PLA.STATUS = '001' AND PLA.TYPE = '1' \n");
		sqlSB.append(" AND T2.STATUS = '001' AND T1.BIZ_TYPE = '2' AND T2.GRID_ID IN (SELECT T3.GRID_ID \n");
		sqlSB.append(" FROM T_DC_GRID T3 WHERE T3.STATUS = '001' START WITH T3.GRID_ID = G1.GRID_ID \n");
		sqlSB.append(" CONNECT BY PRIOR T3.GRID_ID = T3.PARENT_GRID_ID)) BASENUM FROM T_DC_GRID G1 \n");
		sqlSB.append(" WHERE G1.STATUS = '001' AND G1.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getRelatedRoadEvents(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G1.GRID_ID, G1.GRID_NAME, (SELECT COUNT(1) \n");
		sqlSB.append(" FROM T_ZZ_RELATED_EVENTS T1, T_ZZ_CARE_ROAD T3, T_DC_GRID T2 WHERE T1.BIZ_ID = T3.LOT_ID \n");
		sqlSB.append(" AND T3.GRID_ID = T2.GRID_ID AND T1.STATUS = '1' AND T3.STATUS = '1' AND T2.STATUS = '001' \n");
		sqlSB.append(" AND T2.GRID_ID IN (SELECT T4.GRID_ID FROM T_DC_GRID T4 WHERE T4.STATUS = '001' \n");
		sqlSB.append(" START WITH T4.GRID_ID = G1.GRID_ID CONNECT BY PRIOR T4.GRID_ID = T4.PARENT_GRID_ID)) BASENUM \n");
		sqlSB.append(" FROM T_DC_GRID G1 WHERE G1.STATUS = '001' AND G1.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getKepPop(Long gridId, String sYear, String pCode) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT G.GRID_ID ID_, NVL(SUM(T.SNUM), 0) TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID G \n");
		sqlSB.append("  LEFT JOIN "+ConstantValue.ZHSQ_SZZG_DB+"ZG_STATS_POP T \n");
		sqlSB.append("    ON G.INFO_ORG_CODE = T.ORG_CODE \n");
		sqlSB.append("   AND T.STATUS = '1' \n");
		sqlSB.append("   AND T.SYEAR = ? \n");
		sqlSB.append("  LEFT JOIN T_BAS_DATADICT D \n");
		sqlSB.append("    ON T.STYPE = D.DICT_CODE \n");
		sqlSB.append("   AND D.STATUS = '001' \n");
		sqlSB.append("   AND D.DICT_PCODE = ? \n");
		sqlSB.append(" WHERE G.STATUS = '001' \n");
		sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
		sqlSB.append(" GROUP BY G.GRID_ID \n");
		args.add(sYear);args.add(pCode);args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if (gridInfo != null) {
			String exp = configurationService.turnCodeToValue("KEY_POP_BGCOLOR", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, gridInfo.getInfoOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if (StringUtils.isNotBlank(exp)) {
				// 出租屋平均数
				sqlSB.setLength(0);
				sqlSB.append("SELECT ROUND(SUM(NVL(T.SNUM, 0)) / COUNT(0)) RS_TOTAL \n");
				sqlSB.append("  FROM "+ConstantValue.ZHSQ_SZZG_DB+"ZG_STATS_POP T \n");
				sqlSB.append("  LEFT JOIN T_DC_GRID G \n");
				sqlSB.append("    ON T.ORG_CODE = G.INFO_ORG_CODE \n");
				sqlSB.append("  JOIN T_BAS_DATADICT D \n");
				sqlSB.append("    ON T.STYPE = D.DICT_CODE \n");
				sqlSB.append(" WHERE D.STATUS = '001' \n");
				sqlSB.append("   AND T.STATUS = '1' \n");
				sqlSB.append("   AND T.SYEAR = ? \n");
				sqlSB.append("   AND D.DICT_PCODE = ? \n");
				sqlSB.append("   AND G.STATUS = '001' \n");
				sqlSB.append("   AND G.PARENT_GRID_ID = ? \n");
				logger.info("查询SQL语句：" + sqlSB.toString());
				int AVG_NUM = queryForInt(sqlSB.toString(), new Object[] { sYear, pCode, gridId });
				
				exp = exp.replaceAll("＞", ">").replaceAll("＜", "<").replaceAll("＇", "'").replaceAll("＂", "\"");
				List<Variable> variables = new ArrayList<Variable>();
				variables.add(Variable.createVariable("重点人口数", 0));
				variables.add(Variable.createVariable("重点人口均数", 0));
				PreparedExpression pe = ExpressionEvaluator.preparedCompile(exp, variables);
				for (Map<String, Object> data : list) {
					pe.setArgument("重点人口数", Integer.valueOf(String.valueOf(data.get("TOTAL_"))));
					pe.setArgument("重点人口均数", AVG_NUM);
					Object o = pe.execute();
					data.put("COLOR_", o);
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getPartyMember(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (SELECT COUNT(U.CI_PARTY_ID) \n");
		sqlSB.append("          FROM T_DC_GRID G \n");
		sqlSB.append("          LEFT JOIN T_DC_CI_RS_PARTY U \n");
		sqlSB.append("            ON G.INFO_ORG_CODE = U.ORG_CODE \n");
		sqlSB.append("         WHERE G.INFO_ORG_CODE LIKE A.INFO_ORG_CODE || '%' \n");
		sqlSB.append("           AND U.STATUS = '001' \n");
		sqlSB.append("           AND U.PARTY_TYPE = 'dangjian' \n");
		sqlSB.append("           AND G.STATUS = '001') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getStatCor(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (select count(t.cbi_id) \n");
		sqlSB.append("          from t_dc_grid g \n");
		sqlSB.append("          left join T_ZZ_COR_BASE_INFO t \n");
		sqlSB.append("            on g.grid_id = t.grid_id \n");
		sqlSB.append("         where g.status = '001' \n");
		sqlSB.append("           and t.status = '001' \n");
		sqlSB.append("           and g.Info_Org_Code LIKE A.INFO_ORG_CODE || '%') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getStatPartyOrg(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (SELECT count(1) \n");
		sqlSB.append("          FROM T_DC_PARTY_GROUP t \n");
		sqlSB.append("         INNER JOIN T_DC_GRID G \n");
		sqlSB.append("            ON T.ORG_CODE = G.INFO_ORG_CODE \n");
		sqlSB.append("           AND G.status = '001' \n");
		sqlSB.append("         WHERE t.STATUS = '001' \n");
		sqlSB.append("           AND instr(org_code, a.info_org_code) = 1 \n");
		sqlSB.append("           AND t.BIZ_TYPE = 0) TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<ArcgisHeatMapInfo> querySpectialPopulationData(String spectialPopulations, String infoOrgCode) {
		List<ArcgisHeatMapInfo> list = new ArrayList<ArcgisHeatMapInfo>();
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(spectialPopulations)) {
			params.put("bizTypes", spectialPopulations);
		}else{
			params.put("bizTypes", "''");
		}
		if(StringUtils.isNotBlank(infoOrgCode)){
			params.put("infoOrgCode", infoOrgCode);
		}
		list = arcgisInfoMapper.getSpectialPopulationHeatMapStat(params);
		return list;
	}

	@Override
	public List<ArcgisHeatMapInfo> queryPopulationData(String infoOrgCode) {
		List<ArcgisHeatMapInfo> list = new ArrayList<ArcgisHeatMapInfo>();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(infoOrgCode)){
			params.put("infoOrgCode", infoOrgCode);
		}
		list = arcgisInfoMapper.getPopulationHeatMapStat(params);
		return list;
	}

	@Override
	public List<Map<String, Object>> getUrbanCount(Long gridId, String urbanCode) {
		List<Object> args = new ArrayList<Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_,A.Info_Org_Code, \n");
		sqlSB.append("       (select COUNT(0) TOTAL_ \n");
		sqlSB.append("          from T_URBAN_OBJ T \n");
		sqlSB.append("          left join T_DC_GRID G \n");
		sqlSB.append("            on T.GRID_ID = G.GRID_ID \n");
		sqlSB.append("          LEFT JOIN T_ZY_RES_MARKER R \n");
		sqlSB.append("            ON T.OBJ_ID = R.RESOURCES_ID \n");
		sqlSB.append("           AND R.MARKER_TYPE = '020130' \n");
		sqlSB.append("         where T.STATUS = '1' \n");
		sqlSB.append("           AND G.STATUS = '001' \n");
		if (StringUtils.isNotBlank(urbanCode)) {
			urbanCode = "'" + urbanCode.replaceAll(",", "','").trim() + "'";
			sqlSB.append("                AND T.CLASS_CODE IN ("+urbanCode+") \n");
		}
		sqlSB.append("           AND R.MAP_TYPE = 5 \n");
		sqlSB.append("           AND R.X IS NOT NULL \n");
		sqlSB.append("           AND G.INFO_ORG_CODE LIKE A.INFO_ORG_CODE || '%') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		resultList=getList(list,urbanCode);
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getUrbanCountByCodes(String infoOrgCode, String urbanCode) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select T.CLASS_CODE CODE,COUNT(0) TOTAL_ \n");
		sqlSB.append("from T_URBAN_OBJ T \n");
		sqlSB.append("left join T_DC_GRID G \n");
		sqlSB.append("  on T.GRID_ID = G.GRID_ID \n");
		sqlSB.append("LEFT JOIN T_ZY_RES_MARKER R \n");
		sqlSB.append("  ON T.OBJ_ID = R.RESOURCES_ID \n");
		sqlSB.append(" AND R.MARKER_TYPE = '020130' \n");
		sqlSB.append("where T.STATUS = '1' \n");
		sqlSB.append(" AND G.STATUS = '001' \n");
		if (StringUtils.isNotBlank(urbanCode)) {
			urbanCode = "'" + urbanCode.replaceAll(",", "','").trim() + "'";
			sqlSB.append("                AND T.CLASS_CODE IN ("+urbanCode+") \n");
		}
		sqlSB.append(" AND R.MAP_TYPE = 5 \n");
		sqlSB.append(" AND R.X IS NOT NULL \n");
		sqlSB.append(" AND G.INFO_ORG_CODE LIKE ? || '%'\n");
		sqlSB.append("GROUP BY T.CLASS_CODE");
		args.add(infoOrgCode);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getRectify(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (select count(*) \n");
		sqlSB.append("          from T_ZZ_CORRECTIONAL_RECORD T1 \n");
		sqlSB.append("          LEFT OUTER JOIN T_DC_CI_RS_TOP T2 \n");
		sqlSB.append("            ON T1.CI_RS_ID = T2.CI_RS_ID \n");
		sqlSB.append("          LEFT JOIN T_DC_GRID G \n");
		sqlSB.append("            ON T1.GRID_ID = G.GRID_ID \n");
		sqlSB.append("         where T2.STATUS = '1' \n");
		sqlSB.append("           AND G.STATUS = '001' \n");
		sqlSB.append("           AND G.INFO_ORG_CODE LIKE A.INFO_ORG_CODE || '%' \n");
		sqlSB.append("           and T1.STATUS = '001') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getCamps(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (select count(*) \n");
		sqlSB.append("          from T_ZZ_RELEASED_RECORD T1 \n");
		sqlSB.append("          LEFT OUTER JOIN T_DC_CI_RS_TOP T2 \n");
		sqlSB.append("            ON T1.CI_RS_ID = T2.CI_RS_ID \n");
		sqlSB.append("          LEFT JOIN T_DC_GRID G \n");
		sqlSB.append("            ON T1.GRID_ID = G.GRID_ID \n");
		sqlSB.append("         where T2.STATUS = '1' \n");
		sqlSB.append("           AND G.STATUS = '001' \n");
		sqlSB.append("           AND G.INFO_ORG_CODE LIKE A.INFO_ORG_CODE || '%' \n");
		sqlSB.append("           and T1.STATUS = '001') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getJiangxiPetition(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (select count(*) \n");
		sqlSB.append("          from T_ZZ_PETITIONERS T1 \n");
		sqlSB.append("          LEFT OUTER JOIN T_DC_CI_RS_TOP T2 \n");
		sqlSB.append("            ON T1.CI_RS_ID = T2.CI_RS_ID \n");
		sqlSB.append("          LEFT JOIN T_DC_GRID G \n");
		sqlSB.append("            ON T1.REGION_CODE = G.INFO_ORG_CODE \n");
		sqlSB.append("         where T2.STATUS = '1' \n");
		sqlSB.append("           AND G.STATUS = '001' \n");
		sqlSB.append("           AND G.INFO_ORG_CODE LIKE A.INFO_ORG_CODE || '%' \n");
		sqlSB.append("           and T1.STATUS = '1') TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getGlobalEyes(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("SELECT A.GRID_ID ID_, \n");
		sqlSB.append("       (select COUNT(T1.MONITOR_ID) \n");
		sqlSB.append("          FROM CMS.CI_MONITOR T1 \n");
		sqlSB.append("         WHERE T1.ORG_CODE LIKE A.INFO_ORG_CODE || '%' \n");
		sqlSB.append("        ) TOTAL_ \n");
		sqlSB.append("  FROM T_DC_GRID A \n");
		sqlSB.append(" WHERE A.STATUS = '001' \n");
		sqlSB.append("   AND A.PARENT_GRID_ID = ? \n");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	public  List<Map<String, Object>>  getList(List<Map<String, Object>> list,String urbanCode){
		for(Map<String, Object> map:list){
			List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
			String infoOrgCode = (String) map.get("Info_Org_Code");
			Object total =  map.get("TOTAL_");
				List<Object> args = new ArrayList<Object>();
				StringBuffer sqlSB = new StringBuffer();
				sqlSB.append("select t.DICT_GENERAL_CODE class_code, \n");
				sqlSB.append("          t.DICT_REMARK img, \n");
				sqlSB.append(" 		    (select count(0)  from T_URBAN_OBJ t3 where t3.class_code = t.DICT_GENERAL_CODE and t3.info_org_code like ? || '%') total \n");
				sqlSB.append("         from t_bas_datadict t  \n");
				sqlSB.append("         where 1=1 \n");
				if (StringUtils.isNotBlank(urbanCode)) {
					sqlSB.append("                AND T.DICT_GENERAL_CODE IN ("+urbanCode+") \n");
				}
				sqlSB.append("          and t.dict_code like 'D005011%' \n");
				args.add(infoOrgCode);
				logger.info("查询SQL语句：" + sqlSB.toString());
				logger.info("查询SQL参数：" + args.toString());
			    l = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
				map.put("list",l);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getDisputeRectify(Long gridId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		/*sqlSB.append("SELECT G1.GRID_ID ID_, G1.GRID_NAME, (SELECT COUNT(1) CI_PARTY_ID \n");
		sqlSB.append(" FROM T_ZZ_DISPUTE_MEDIATION T1,  T_DC_GRID T2 WHERE \n");
		sqlSB.append(" T1.GRID_ID = T2.GRID_ID AND T2.STATUS = '001' \n");
		sqlSB.append(" AND T2.GRID_ID IN (SELECT T4.GRID_ID FROM T_DC_GRID T4 WHERE T4.STATUS = '001' \n");
		sqlSB.append(" START WITH T4.GRID_ID = G1.GRID_ID CONNECT BY PRIOR T4.GRID_ID = T4.PARENT_GRID_ID)) TOTAL_ \n");
		sqlSB.append(" FROM T_DC_GRID G1 WHERE G1.STATUS = '001' AND G1.PARENT_GRID_ID = ? \n");
		sqlSB.append(" ORDER BY G1.INFO_ORG_CODE \n");*/

		sqlSB.append("SELECT G1.GRID_ID ID_,G1.GRID_NAME,G1.INFO_ORG_CODE, COUNT(T1.MEDIATION_ID) TOTAL_ FROM T_DC_GRID G1 ");
		sqlSB.append(" INNER JOIN T_DC_GRID T2 ON T2.INFO_ORG_CODE LIKE G1.INFO_ORG_CODE || '%' AND T2.STATUS = '001'");
		sqlSB.append(" INNER JOIN T_ZZ_DISPUTE_MEDIATION T1 ON T1.GRID_ID = T2.GRID_ID");
		sqlSB.append(" WHERE G1.STATUS = '001'");
		sqlSB.append(" AND G1.PARENT_GRID_ID = ?");
		sqlSB.append(" GROUP BY G1.GRID_ID, G1.GRID_NAME, G1.INFO_ORG_CODE");
		sqlSB.append(" ORDER BY G1.INFO_ORG_CODE");
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

	@Override
	public List<Map<String, Object>> getDisputeRectify(Long gridId, String type) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSB = new StringBuffer();
		/*sqlSB.append("SELECT G1.GRID_ID ID_, G1.GRID_NAME, (SELECT COUNT(1) CI_PARTY_ID \n");
		sqlSB.append(" FROM T_ZZ_DISPUTE_MEDIATION T1,  T_DC_GRID T2 WHERE \n");
		sqlSB.append(" T1.GRID_ID = T2.GRID_ID AND T2.STATUS = '001' \n");
		sqlSB.append(" AND T2.GRID_ID IN (SELECT T4.GRID_ID FROM T_DC_GRID T4 WHERE T4.STATUS = '001' \n");
		sqlSB.append(" START WITH T4.GRID_ID = G1.GRID_ID CONNECT BY PRIOR T4.GRID_ID = T4.PARENT_GRID_ID)) TOTAL_ \n");
		if(StringUtils.isBlank(type)) {
			sqlSB.append(" FROM T_DC_GRID G1 WHERE G1.STATUS = '001' AND G1.PARENT_GRID_ID = ? \n");
		}else if(StringUtils.equals(type, "my")){
			sqlSB.append(" FROM T_DC_GRID G1 WHERE G1.STATUS = '001' AND G1.GRID_ID = ? \n");	
		}
		sqlSB.append(" ORDER BY G1.GRID_ID \n");*/

		sqlSB.append("SELECT G1.GRID_ID ID_,G1.GRID_NAME,G1.INFO_ORG_CODE, COUNT(T1.MEDIATION_ID) TOTAL_ FROM T_DC_GRID G1");
		sqlSB.append(" INNER JOIN T_DC_GRID T2 ON T2.INFO_ORG_CODE LIKE G1.INFO_ORG_CODE || '%' AND T2.STATUS = '001'");
		sqlSB.append(" INNER JOIN T_ZZ_DISPUTE_MEDIATION T1 ON T1.GRID_ID = T2.GRID_ID");
		sqlSB.append(" WHERE G1.STATUS = '001'");
		if(StringUtils.isBlank(type)) {
			sqlSB.append(" AND G1.PARENT_GRID_ID = ? \n");
		}else if(StringUtils.equals(type, "my")){
			sqlSB.append(" AND G1.GRID_ID = ? \n");
		}
		sqlSB.append(" GROUP BY G1.GRID_ID, G1.GRID_NAME, G1.INFO_ORG_CODE");
		sqlSB.append(" ORDER BY G1.INFO_ORG_CODE");
		
		args.add(gridId);
		logger.info("查询SQL语句：" + sqlSB.toString());
		logger.info("查询SQL参数：" + args.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSB.toString(), args.toArray());
		return list;
	}

}
