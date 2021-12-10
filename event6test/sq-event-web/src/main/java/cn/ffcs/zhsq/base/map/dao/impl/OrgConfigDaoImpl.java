package cn.ffcs.zhsq.base.map.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.base.domain.db.CatalogType;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;
import cn.ffcs.zhsq.base.dao.OracleDB;
import cn.ffcs.zhsq.base.map.dao.IOrgConfigDao;

@Service(value = "firegridOrgConfigDaoImpl")
public class OrgConfigDaoImpl extends OracleDB implements IOrgConfigDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 获取组织详情
	 * @param orgId
	 * @return
	 */
	public OrgEntityInfo queryEntityByPK(long orgId) {
		assert orgId != Long.MIN_VALUE;
		String sql = "select * from T_XF_ORG_ENTITY_INFO where ORG_ID =?";
		RowMapper<OrgEntityInfo> mapper = new BeanPropertyRowMapper<OrgEntityInfo>(
				OrgEntityInfo.class);
		return (OrgEntityInfo) this.queryForObject(sql, mapper, orgId);
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
	
	/**
	 * 获取组织下的所有子节点
	 * @param parentOrgId
	 * @return
	 */
	public List<OrgEntityInfo> getSubOrgEntityInfoList(Long parentOrgId) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO " +
				" where parent_org_Id=? order by org_id";
		RowMapper<OrgEntityInfo> orgEntityInfomapper = new BeanPropertyRowMapper<OrgEntityInfo>(OrgEntityInfo.class);
		List<OrgEntityInfo> list = null;
		try {
			list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper,new Object[]{parentOrgId});
		}catch(Exception e) {
			list = new ArrayList<OrgEntityInfo>();
			e.printStackTrace();
		}
		return list;
	}
	public List<OrgEntityInfo> getSubOrgEntityInfoList(Long catalogId,Long parentOrgId) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO " +
				" where catalog_id=? and parent_org_Id=? order by org_id";
		
		List<OrgEntityInfo> list = null;
		try {
			list = (List<OrgEntityInfo>) this.getJdbcTemplate().query(sql,new Object[]{catalogId,parentOrgId},orgEntityInfoMapper);
		}catch(Exception e) {
			list = new ArrayList<OrgEntityInfo>();
			e.printStackTrace();
		}
		return list;
	}
	
	public int getSubOrgCount(Long catalogId,Long parentOrgId) {
		String sql = "select count(*) from T_XF_ORG_ENTITY_INFO " +
		" where catalog_id=? and parent_org_Id=? ";

		return  this.getJdbcTemplate().queryForObject(sql,new Object[]{catalogId,parentOrgId},Integer.class);

	}
	
	public OrgEntityInfo getOrgEntityInfo(Long orgId) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO " +
		" where org_Id=?";
		List<OrgEntityInfo> list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper, new Object[]{orgId});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	public OrgEntityInfo getOrgEntityInfoByCatalogIdandParentOrgId(int catalogId,Long parentOrgId) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO t where t.catalog_id=? and t.parent_org_id=?";
			List<OrgEntityInfo> list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper, new Object[]{catalogId,parentOrgId});
			if(list!=null && list.size()>0) {
				return list.get(0);
			}
		return null;
	}
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandLocationCode(int catalogId,String locationCode) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO t where t.catalog_id=? and t.org_code=?";
		List<OrgEntityInfo> list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper, new Object[]{catalogId,locationCode});		
		return list;
	}
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandOrgCode(int catalogId,String orgCode) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO t where t.catalog_id=? and t.org_code=?";
		List<OrgEntityInfo> list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper, new Object[]{catalogId,orgCode});		
		return list;
	}
	
	public OrgEntityInfo getOrgEntityInfo(Long catalogId,String locationCode) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO where catalog_id=? and location_code=?";
			List<OrgEntityInfo> list = (List<OrgEntityInfo>)this.queryForList(sql, orgEntityInfoMapper, new Object[]{catalogId,locationCode});
			if(list!=null && list.size()>0) {
				return list.get(0);
			}
		return null;
	}
	/**
	 * 删 除
	 * */
	public int deleteOrgEntityInfo(Long orgId) {
		String sql = "delete from T_XF_ORG_ENTITY_INFO where org_id=?";
		int result = this.getJdbcTemplate().update(sql, new Object[]{orgId});
		return result;
	}
	
	/**根据父结点ID获取最大的orgCode*/
	public Long getMaxOrgCodeByParentOrgId(Long catalogId,Long parentOrgId) {
		String sql = "select MAX(ascii(SUBSTR(SUBSTR(org_code,-2,2),-2,1))||ascii(SUBSTR(SUBSTR(org_code,-2,2),-1))) from T_XF_ORG_ENTITY_INFO where catalog_id=? and parent_org_id = ?";
		return this.getJdbcTemplate().queryForObject(sql,new Object[]{catalogId,parentOrgId},Long.class);
	}
	/**
	 * 根据组织类型ID及组织ID
	 * 取组织
	 * */
	public List<OrgEntityInfo> getOrgEntityInfoList(Long catalogId, Long parentOrgId) {
		String sql = "select * from T_XF_ORG_ENTITY_INFO " +
				" where catalog_id=? and parent_org_id=? order by org_id";		
		List<OrgEntityInfo> list = null;
		try {
			list = (List<OrgEntityInfo>)this.queryForList(sql,orgEntityInfoMapper,new Object[]{catalogId,parentOrgId});
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
		String sql = "select * from T_XF_ORG_EXTRA_INFO where org_id=?"; 
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
	public OrgExtraInfo getOrgExtraInfo(Long orgId,String mapType) {
		String sql = "select * from T_XF_ORG_EXTRA_INFO where org_id=? and map_type=?"; 
		RowMapper<OrgExtraInfo> orgExtraInfomapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
		List<OrgExtraInfo> list = null;
		try {
			list = (List<OrgExtraInfo>)this.queryForList(sql, orgExtraInfomapper,new Object[]{orgId,mapType});
		}catch(Exception e) {			
			e.printStackTrace();
		}
		if(null != list && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 删除组织对应的点位配置信息
	 * @param orgId
	 * @return
	 */
	public int deleteOrgLocationInfo(Long orgId) {
		String sql = "delete from T_XF_ORG_LOCATION_INFO where org_id=?";
		return super.getJdbcTemplate().update(sql, new Object[]{orgId});		
	}
	
	/**
	 * 保存描点配置
	 * @param orgLocationInfoList
	 */
	public void insertOrgLocationInfo(final List<OrgLocationInfo> orgLocationInfoList) {
		String sql = "insert into T_XF_ORG_LOCATION_INFO(ORG_LOCATION_ID,ORG_ID,MAP_ORDER,MAP_ID,MAP_SHAPE,LONGITUDE,LATITUDE,CREATE_TIME,STATUS,STATUS_TIME) values(?,?,?,?,?,?,?,sysdate,?,sysdate)";
		super.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				OrgLocationInfo orgLocationInfo = orgLocationInfoList.get(i);
				Long orglocationid = querySequenceNextValue("SEQ_XFORGLOCATION_ID");
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
	public boolean batchUpdateOrgLocationInfo(final List<OrgLocationInfo> orgLocationInfoList) {
		String sql = "insert into T_XF_ORG_LOCATION_INFO(ORG_LOCATION_ID,ORG_ID,MAP_ORDER,MAP_ID,MAP_SHAPE,LONGITUDE,LATITUDE,CREATE_TIME,STATUS,STATUS_TIME) values(?,?,?,?,?,?,?,sysdate,?,sysdate)";
		boolean result = true;
		try {
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
		}catch(Exception e) {
			result = false;
		}
		return result;
	}
	/**
	 * 根据orgID取得中心点下的围点
	 * */
	public List<OrgLocationInfo> getOrgLocationInfoList(Long orgId) {
		String sql = "select * from T_XF_ORG_LOCATION_INFO t  where t.org_id=? order by map_order";
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
	 * 取子中心点位 中心点类型为google地图
	 * */
	public List<OrgExtraInfo> getSubOrgExtraInfoList(Long orgId) {
		String sql = "select * from T_XF_ORG_EXTRA_INFO where  org_id in(select distinct org_id from T_XF_ORG_ENTITY_INFO where parent_org_Id=?)";
		RowMapper<OrgExtraInfo> orgExtraInfoRowMapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
		return this.queryForList(sql, orgExtraInfoRowMapper, new Object[]{orgId});
	}
	
	public List<OrgExtraInfo> getSubOrgExtraInfoList(Long orgId,String mapType) {
		String sql = "select * from T_XF_ORG_EXTRA_INFO where map_type=? and org_id in(select distinct org_id from T_XF_ORG_ENTITY_INFO where parent_org_Id=?)";
		RowMapper<OrgExtraInfo> orgExtraInfoRowMapper = new BeanPropertyRowMapper<OrgExtraInfo>(OrgExtraInfo.class);
		return this.queryForList(sql, orgExtraInfoRowMapper, new Object[]{mapType,orgId});
	}
	/**
	 * 更新
	 * */
	public boolean updateOrgExtraInfo(OrgExtraInfo gi) {
		StringBuffer sql = new StringBuffer();
		sql.append("update T_XF_ORG_EXTRA_INFO set ");
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
		sql.append("STATUS_DATE=:statusDate, ");
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
		sql.append("LATITUDE =:latitude, ");
		
		sql.append("MAP_TYPE =:mapType, ");
		sql.append("MAP_URL =:mapUrl ");
		
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
		jdbcInsert.withTableName("T_XF_ORG_EXTRA_INFO");
		rtn = jdbcInsert.execute(new BeanPropertySqlParameterSource(gi)) != -1;
		return rtn;
	}
	
	public int getOrgEntityMaxChiefLevel(long catalogId) {
		String sql = "select max(chief_level) from T_XF_ORG_ENTITY_INFO where catalog_id=?"; 
		return getJdbcTemplate().queryForObject(sql, new Object[]{catalogId},Integer.class);
	}
	/**组织层级类型*/
	public List<JSONObject> getAllOrgLayerTypeList() {
		String sql = "select * from t_dc_data_dictionary_info where table_id = 'T_XF_ORG_ENTITY_INFO' and column_id='ORG_LAYER_TYPE'";
		return getJdbcTemplate().query(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int n) throws SQLException {
				JSONObject obj = new JSONObject();
				//t.column_value,t.column_value_remark
				obj.put("id", rs.getString("column_value"));
				obj.put("text", rs.getString("column_value_remark"));
				return obj;
			}
			
		});
	}
	
	private RowMapper<OrgEntityInfo> orgEntityInfoMapper = new RowMapper(){
		@Override
		public Object mapRow(ResultSet rs, int i)
				throws SQLException {
			//t.org_id,t.org_name,t.parent_org_id,t.org_code,t.catalog_id,t.org_type,t.chief_level,t.is_test,t.operate_user,t.location_code
			//t.gov_level,t.create_time,t.status,t.status_time,t.org_layer_type,t.icon
			OrgEntityInfo orgEntityInfo = new OrgEntityInfo();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			orgEntityInfo.setOrgId(rs.getLong("org_id"));
			orgEntityInfo.setOrgName(rs.getString("org_name"));
			orgEntityInfo.setParentOrgId(rs.getLong("parent_org_id"));
			orgEntityInfo.setOrgCode(rs.getString("org_code"));
			orgEntityInfo.setCatalogId(rs.getLong("catalog_id"));
			orgEntityInfo.setOrgType(rs.getString("org_type"));
			orgEntityInfo.setChiefLevel(rs.getString("chief_level"));
			orgEntityInfo.setIsTest(rs.getString("is_test"));
			//orgEntityInfo.setOperateUser(rs.getLong("operate_user"));
			orgEntityInfo.setLocationCode(rs.getString("location_code"));
			orgEntityInfo.setGovLevel(rs.getString("gov_level"));
			//orgEntityInfo.setCreateTime(rs.getString("create_time"));
			//orgEntityInfo.setStatusTime(rs.getString("status_time"));
			//orgEntityInfo.setOrgLayerType(rs.getString("org_layer_type"));
			String orgLayerType = rs.getString("org_layer_type");
			//orgEntityInfo.setIcon(rs.getString("icon"));
			
			
			return orgEntityInfo;
		}
		
	};
	
	@Override
	public Long getParentOrgId(Long subOrgId, String expectLayerType) {
		try {
			String sql = "select org_id from T_XF_ORG_ENTITY_INFO where org_layer_type=? start with org_id=? connect by prior parent_org_id=org_id";
			return this.queryForLong(sql, new Object[]{expectLayerType, subOrgId});
		} catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
	}

}
