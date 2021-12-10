package cn.ffcs.zhsq.base.dao;

import cn.ffcs.common.Pagination;
import cn.ffcs.shequ.base.domain.db.CatalogType;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "orgConfigDaoImpl")
public class OrgConfigDaoImpl extends OracleDB{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private RowMapper<OrgExtraInfo> mapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
	
	/**
	 * 分页取组织描点配置
	 * */
	public Pagination getOrgConfigWithPagination(int pageNo, int pageSize) {
		Pagination pagination;
		try {			
			int totalCount = this.getOrgConfigCount();			
			int pages = totalCount/pageSize;
			if(totalCount%pageSize>0) pages++;
			int start = (pageNo-1)*pageSize;
			int end = pageNo*pageSize;
			String sql = "select * from t_dc_org_info t1";
			String querySql = "select * from (select r.*,rownum row_num from ("+sql+" order by t1.CREATE_TIME desc) r where rownum<="+end+") where row_num>"+start;
			List<OrgExtraInfo> list = (List<OrgExtraInfo>) this.queryForList(querySql, mapper);
			pagination = new Pagination(pageNo, pageSize, pages, totalCount, list);
		} catch (Exception e) {
			e.printStackTrace();
			pagination = new Pagination(pageNo, pageSize, 0, 0, new ArrayList<OrgExtraInfo>());
		}
		return pagination;
	}
	
	/**
	 * 组织描点数
	 * */
	public int getOrgConfigCount() {
		String countSql = "select count(*) from t_dc_org_info ";
		return this.queryForInt(countSql);		
	}

	/**
	 * 取所有组织描点
	 * */
	public List<OrgExtraInfo> getOrgConfigList() {
		String sql = "select * from t_dc_org_info t1";
		List<OrgExtraInfo> list = null;
		try {
			list = (List<OrgExtraInfo>) this.queryForList(sql, mapper);
		}catch(Exception e) {
			list = new ArrayList<OrgExtraInfo>();
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 获取所有的CatalogType
	 * */	
	public List<CatalogType> getCatalogTypeList() {
		String sql = "select * from t_dc_catalog_type t1 where t1.status='001'";
		RowMapper<CatalogType> catalogtypemapper = new BeanPropertyRowMapper<CatalogType>(CatalogType.class);
		List<CatalogType> list = null;
		try {
			list = (List<CatalogType>) this.queryForList(sql, catalogtypemapper);
		}catch(Exception e) {
			list = new ArrayList<CatalogType>();
			e.printStackTrace();
		}
		return list;
	}
	public List<OrgEntityInfo> getSubOrgEntityInfoList(Long parentOrgId) {
		String sql = "select * from t_dc_org_entity_info  where status=? and  parent_org_Id=?";
		RowMapper<OrgEntityInfo> orgEntityInfomapper = new BeanPropertyRowMapper<OrgEntityInfo>(OrgEntityInfo.class);
		List<OrgEntityInfo> list = null;
		try {
			list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfomapper,new Object[]{ConstantValue.STATUS_DEFAULT,parentOrgId});
		}catch(Exception e) {
			list = new ArrayList<OrgEntityInfo>();
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据组织类型ID及组织ID
	 * 取组织
	 * */
	public List<OrgEntityInfo> getOrgEntityInfoList(Long catalogId,Long orgId) {
		String sql = "select * from t_dc_org_entity_info " +
				" where status=? and catalog_id=? and location_code in (select distinct location_code from t_dc_org_entity_info where org_id=?)";
		RowMapper<OrgEntityInfo> orgEntityInfomapper = new BeanPropertyRowMapper<OrgEntityInfo>(OrgEntityInfo.class);
		List<OrgEntityInfo> list = null;
		try {
			list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfomapper,new Object[]{ConstantValue.STATUS_DEFAULT,catalogId,orgId});
		}catch(Exception e) {
			list = new ArrayList<OrgEntityInfo>();
			e.printStackTrace();
		}
		return list;		
	}
	
	
	/**
	 * 根据ORG
	 * 取中心点组织配置
	 * */
	public OrgExtraInfo getOrgExtraInfo(Long orgId) {
		String sql = "select * from t_dc_org_info where org_id=?"; 
		RowMapper<OrgExtraInfo> orgExtraInfomapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
		List<OrgExtraInfo> list = null;
		try {
			list = (List<OrgExtraInfo>)this.queryForList(sql, orgExtraInfomapper,new Object[]{orgId});
		}catch(Exception e) {			
			e.printStackTrace();
		}
		if(null != list && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	public int deleteOrgLocationInfo(Long orgId) {
		String sql = "delete from T_DC_ORG_LOCATION_INFO where org_id=?";
		return super.getJdbcTemplate().update(sql, new Object[]{orgId});		
	}
	
	public void insertOrgLocationInfo(final List<OrgLocationInfo> orgLocationInfoList) {
        String sql = "insert into T_DC_ORG_LOCATION_INFO(ORG_LOCATION_ID,ORG_ID,MAP_ORDER,MAP_ID,MAP_SHAPE,LONGITUDE,LATITUDE,CREATE_TIME,STATUS,STATUS_TIME) values(?,?,?,?,?,?,?,sysdate,?,sysdate)";
//		for(int i=0;i<orgLocationInfoList.size();i++) {
//			Long orglocationid = querySequenceNextValue("SEQ_ORG_LOCATION_INFO_ID");
//			OrgLocationInfo orgLocationInfo = orgLocationInfoList.get(i);
//			super.getJdbcTemplate().update(sql, new Object[]{orglocationid,
//					orgLocationInfo.getOrgId(),
//					orgLocationInfo.getMapOrder(),
//					new Long(1),
//					new Long(1),
//					orgLocationInfo.getLongitude(),
//					orgLocationInfo.getLatitude(),					
//					new Long(1)
//					});			
//		}
		
		super.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				OrgLocationInfo orgLocationInfo = orgLocationInfoList.get(i);
				Long orglocationid = querySequenceNextValue("SEQ_ORG_LOCATION_INFO_ID");
				ps.setLong(1, orglocationid);
				ps.setLong(2, orgLocationInfo.getOrgId());
				ps.setLong(3, orgLocationInfo.getMapOrder());
				ps.setLong(4, new Long(1));				
				ps.setString(5, orgLocationInfo.getMapShape()!=null && !"".equals(orgLocationInfo.getMapShape())?orgLocationInfo.getMapShape():"");
				ps.setDouble(6, orgLocationInfo.getLongitude());
				ps.setDouble(7, orgLocationInfo.getLatitude());				
				ps.setString(8, "1");
							
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return orgLocationInfoList.size();
			}
		});
	}
	
	/**
	 * 根据orgID取得中心点下的围点
	 * */
	public List<OrgLocationInfo> getOrgLocationInfoList(Long orgId) {
		String sql = "select * from T_DC_ORG_LOCATION_INFO t  where t.org_id=? order by map_order";
		RowMapper<OrgLocationInfo> orgLocationInfomapper = new BeanPropertyRowMapper<OrgLocationInfo>(OrgLocationInfo.class);
		List<OrgLocationInfo> list = null;
		try {
			list = (List<OrgLocationInfo>)this.queryForList(sql, orgLocationInfomapper,new Object[]{orgId});
		}catch(Exception e) {			
			e.printStackTrace();
		}		
		return list;	
	}
	
	/**
	 * 取子中心点位
	 * */
	public List<OrgExtraInfo> getSubOrgExtraInfoList(Long orgId) {
		String sql = "select * from t_dc_org_info where org_id in(select distinct org_id from t_dc_org_entity_info where parent_org_Id=? and status=?)";
		RowMapper<OrgExtraInfo> orgExtraInfoRowMapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
		return this.queryForList(sql, orgExtraInfoRowMapper, new Object[]{orgId, ConstantValue.STATUS_DEFAULT});
	}
	
	/**
	 * 更新
	 * */
	public boolean updateOrgExtraInfo(OrgExtraInfo gi) {
		StringBuffer sql = new StringBuffer();
		sql.append("update t_dc_org_info set ");
		sql.append("ORG_CONTENT=:orgContent,");
		sql.append("ORG_SCALE=:orgScale,");
		sql.append("TEL_AREA =:telArea,");
		sql.append("DUTY_DESC=:dutyDesc,");
		sql.append("ORG_NAME=:orgName,");
		sql.append("ORG_ADRESS=:orgAdress,");
		sql.append("CONTACT=:contact,");
		sql.append("CONTACT_TEL=:contactTel,");
		sql.append("CHARGE_PERSON=:chargePerson, ");
		
		sql.append("CHARGE_PERSON_TEL=:chargePersonTel, ");
		sql.append("REGIS_PERSON =:regisPerson, ");
		sql.append("REGIS_TIME =:regisTime, ");
		sql.append("STATUS =:status ,");
		sql.append("STATUS_TIME=:statusDate, ");
		sql.append("CREATE_TIME =:createTime, ");
		sql.append("CHARGE_PERSON_ADD=:chargePersonAdd, ");
		sql.append("DIR_NAME =:dirName, ");
		sql.append("RESIDENT_TOTAL=:residentTotal, ");
		
		sql.append("COM_SER_AREA =:comSerArea, ");
		sql.append("PRO_RIGHT =:proRight, ");
		sql.append("PRAC_RIGHT=:pracRight, ");
		sql.append("BORROW =:borrow, ");
		sql.append("RENT =:rent, ");
		sql.append("MAP_ID =:mapId, ");
		sql.append("MAP_COORDINATE =:mapCoordinate, ");
		sql.append("MAP_SHAPE =:mapShape, ");
		sql.append("LONGITUDE=:longitude, ");
		sql.append("LATITUDE =:latitude ");
		
		sql.append("where ORG_ID=:orgId");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.getDataSource());
		return template.update(sql.toString(), new BeanPropertySqlParameterSource(gi))!=-1?true:false;
	}

	/**
	 * 插入
	 * */
	public boolean insertOrgExtraInfo(OrgExtraInfo gi) {
		boolean rtn = false;	
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(this.getJdbcTemplate());
		jdbcInsert.withTableName("T_DC_ORG_INFO");		
		rtn = jdbcInsert.execute(new BeanPropertySqlParameterSource(gi)) != -1;
		return rtn;
	}
	
	/**
	 * 查询指定索引的下一个值
	 * 
	 * @param sequenceName
	 * @return
	 */
	protected long querySequenceNextValue(String sequenceName) {
		assert sequenceName != null && sequenceName.length() > 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(sequenceName).append(".NEXTVAL FROM DUAL");
		long val = this.queryForLong(sql.toString());
		return val;
	}

	public RowMapper<OrgExtraInfo> getMapper() {
		return mapper;
	}

	public void setMapper(RowMapper<OrgExtraInfo> mapper) {
		this.mapper = mapper;
	}
}
