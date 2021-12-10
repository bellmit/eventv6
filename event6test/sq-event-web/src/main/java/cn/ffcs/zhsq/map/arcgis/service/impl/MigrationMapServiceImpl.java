package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.map.arcgis.service.IMigrationMapService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.SpringContextUtil;

@Service(value="migrationMapService")
public class MigrationMapServiceImpl implements IMigrationMapService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Map<String, Object>> findMigrationData(Map<String, Object> params) {
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("xingYunDB");
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String type = (String) params.get("type");
		String date = (String) params.get("date");
		String flowCode = (String) params.get("flowCode");
		String isOutType = (String) params.get("isOutType");
		if (!"0".equals(isOutType)) {// 省外
			//flowCode = "590";
		}
		try {
			conn = dataSource.getConnection();
			List<Object> args = new ArrayList<Object>();
			StringBuffer sqlSB = new StringBuffer(512);
			StringBuffer comSB = new StringBuffer(512);
			sqlSB.append("select * from ( select row_.*, rownum rownum_ from ( ");
			sqlSB.append("SELECT T.*,  \n");
			sqlSB.append("       C2.CITY_NAME FROM_CITY_NAME, C1.CITY_NAME TO_CITY_NAME \n");
			if ("0".equals(isOutType)) {// 省内
				comSB.append("  FROM V_ZZ_POPULATIONMIGRATION_FJ T \n");
			} else {
				comSB.append("  FROM V_ZZ_POPULATIONMIGRATION T \n");
			}
			comSB.append("  LEFT JOIN ZZ_CITYCONFIG C1 \n");
			comSB.append("    ON T.AREA_ID_GET = C1.ID \n");
			comSB.append("  LEFT JOIN ZZ_CITYCONFIG C2 \n");
			comSB.append("    ON T.AREA_ID_OUT = C2.ID \n");
			comSB.append(" WHERE T.DATA_ID = ? \n");
			args.add(date);
			comSB.append("   AND T.MOVE_TYPE = ? \n");
			args.add(type);
			if ("0".equals(type)) {
				comSB.append("   AND T.AREA_ID_GET = ? \n");
				if ("0".equals(isOutType)) {// 省内
					comSB.append("   AND T.AREA_ID_OUT LIKE '59%' \n");
				} else {
					comSB.append("   AND T.AREA_ID_OUT NOT LIKE '59%' \n");
				}
			} else {
				comSB.append("   AND T.AREA_ID_OUT = ? \n");
				if ("0".equals(isOutType)) {// 省内
					comSB.append("   AND T.AREA_ID_GET LIKE '59%' \n");
				} else {
					comSB.append("   AND T.AREA_ID_GET NOT LIKE '59%' \n");
				}
			}
			args.add(flowCode);
			
			sqlSB.append(comSB);
			sqlSB.append(" ORDER BY TO_NUMBER(T.P_CNT) DESC \n");
			sqlSB.append(" ) row_  where rownum <= 10) where rownum_ > 0");
			
			StringBuffer countSB = new StringBuffer(512);
			countSB.append("SELECT SUM(T.P_CNT) TOTAL  \n");
			countSB.append(comSB);
			logger.info("总量语句：" + countSB.toString());
			logger.info(args.toString());
			pstm = conn.prepareStatement(countSB.toString());
			for (int i = 1; i <= args.size(); i++) {
				pstm.setObject(i, args.get(i - 1));
			}
			rs = pstm.executeQuery();
			int total = 0;
			if (rs.next()) {
				total = rs.getInt("TOTAL");
			}
			rs.close();
			pstm.close();
			
			logger.info("列表语句：" + sqlSB.toString());
			logger.info(args.toString());
			pstm = conn.prepareStatement(sqlSB.toString());
			for (int i = 1; i <= args.size(); i++) {
				pstm.setObject(i, args.get(i - 1));
			}
			rs = pstm.executeQuery();
			List<Map<String, Object>> list = CommonFunctions.rsToList(rs);
			for (Map<String, Object> map : list) {
				int cnt = Integer.parseInt(map.get("P_CNT").toString());
				String ratio = String.format("%.2f", (float) cnt / (float) total * 100.0);
				map.put("ratio", ratio + "%");
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstm != null) pstm.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
