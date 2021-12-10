package cn.ffcs.zhsq.base.export;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExpStruc implements Serializable {

	private static final long serialVersionUID = -2928370082245439673L;
	private String expType;
	// 文件名称
	private String fileName;
	// 列表参数
	private List<String[]> keys;// String[]：长度为2；第一位：表头名称，第二位：数据源key；
	private List<Map<String, Object>> vals;
	// 模板参数
	private String tempPath;
	private Map<String, Object> tempData;

	/**
	 * 构造导出数据结构
	 * @param expType：导出类型
	 * @param fileName：导出文件名称
	 */
	public ExpStruc(String expType, String fileName) {
		this.expType = expType;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String[]> getKeys() {
		return keys;
	}

	/**
	 * @param keys String[]：长度为2；第一位：表头名称，第二位：数据源key；
	 */
	public void setKeys(List<String[]> keys) {
		this.keys = keys;
	}

	public List<Map<String, Object>> getVals() {
		return vals;
	}

	public void setVals(List<Map<String, Object>> vals) {
		this.vals = vals;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public Map<String, Object> getTempData() {
		return tempData;
	}

	public void setTempData(Map<String, Object> tempData) {
		this.tempData = tempData;
	}

	public String getExpType() {
		return expType;
	}

}
