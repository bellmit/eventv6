package cn.ffcs.zhsq.base.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.ffcs.shequ.grid.page.Page;

/**
 * 基础数据库操作
 * 
 * @author guohh
 * 
 */
@Component
public abstract class OracleDB<T> {
	@Autowired
	private JdbcTemplate jdbcTemplateOracle;

	public List<Map<String, Object>> queryForList(String sql) {
		return this.jdbcTemplateOracle.queryForList(sql);
	}

	public int queryForInt(String sql) {
		return this.jdbcTemplateOracle.queryForObject(sql,Integer.class);
	}

	public long queryForLong(String sql) {
		return this.jdbcTemplateOracle.queryForObject(sql,Long.class);
	}
	
	public long queryForLong(String sql, Object... args) {
		return this.jdbcTemplateOracle.queryForObject(sql, args,Long.class);
	}

	public Object queryForObject(String sql, RowMapper rowMapper, Object... args) {
		return this.jdbcTemplateOracle.queryForObject(sql, args, rowMapper);
	}

	public List queryForList(String sql, RowMapper rowMapper, Object... args) {
		return this.jdbcTemplateOracle.query(sql, args, rowMapper);
	}

	public Map<String, Object> queryForMap(String sql, Object... args) {
		return this.jdbcTemplateOracle.queryForMap(sql, args);
	}

	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return this.jdbcTemplateOracle.queryForList(sql, args);
	}

	public int queryForInt(String sql, Object... args) {
		return this.jdbcTemplateOracle.queryForObject(sql, args,Integer.class);
	}

	public int update(String sql, Object... args) throws SQLException {
		return this.jdbcTemplateOracle.update(sql, args);
	}

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplateOracle;
	}

	public DataSource getDataSource() {
		return jdbcTemplateOracle.getDataSource();
	}

	public Connection getConnection() throws SQLException {
		return this.jdbcTemplateOracle.getDataSource().getConnection();
	}

	protected Long pageQueryForTotal(String sql, Object... param) {
		return this.getJdbcTemplate().queryForObject(sql, param,Long.class);
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            查询SQL
	 * @param page
	 *            分页参数
	 * @param param
	 *            查询参数
	 * @return
	 */
	protected List<T> pageQueryForList(String sql, Page page, RowMapper<?> mapper, Object... param) {
		StringBuffer pageSql = new StringBuffer();
		pageSql.append("select * from (select a.*,rownum row_num from (");
		pageSql.append(sql);
		pageSql.append(")a) b where b.row_num between ").append(page.getStart() + 1);
		pageSql.append(" and ").append(page.getLimit() + page.getStart());
		return this.queryForList(pageSql.toString(), mapper, param);
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
	
	/**
	 * 获取手工编码的下一个值，如201208001
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	protected String queryNextManualCode(String tableName, String columnName) {
		try {
			String sql = "select max(to_number(substr("+columnName+",3)))+1 nextCode from "+tableName+" where substr("+columnName+",3,6)=to_char(sysdate,'yyyymm')";
			Map<String, Object> result = this.queryForMap(sql, new Object[]{});
			return result.get("nextCode").toString();
		} catch(Exception e) {
			Date now = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			return (sdf.format(now)+"00001");
		}
	}
}
