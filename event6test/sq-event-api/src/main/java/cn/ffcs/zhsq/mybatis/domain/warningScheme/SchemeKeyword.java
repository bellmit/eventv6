package cn.ffcs.zhsq.mybatis.domain.warningScheme;

import java.io.Serializable;

/**
 * @Description: 预警方案模块：方案匹配关键字
 * @Author: youwj
 * @Date: 05-28 15:32:01
 * @Copyright: 2019 福富软件
 */
public class SchemeKeyword implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long schemeKeywordId;//主键
	private Long schemeId;//方案外键
	private String code;//事件等级字典
	private String keyword;//等级对应关键字
	public Long getSchemeKeywordId() {
		return schemeKeywordId;
	}
	public void setSchemeKeywordId(Long schemeKeywordId) {
		this.schemeKeywordId = schemeKeywordId;
	}
	public Long getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	

}
