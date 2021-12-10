/**
 * 
 */
package cn.ffcs.zhsq.base.local.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import cn.ffcs.log.api.bo.Pagination;
import cn.ffcs.uam.bo.UserInfo;

/**
 * 网格日志服务接口
 * @author guohh
 *
 */
public interface IGridLogService {

	/**
	 * 保存日志
	 * @param session session
	 * @param request request
	 * @param action 操作
	 * @param modelCode 模块编码
	 * @param tableName 表名
	 * @param recordId 记录Id
	 * @param valueBefore 修改前的值
	 * @param valueAfter 修改后的值
	 * @param operaId 批次号
	 * @return
	 */
	public boolean saveLog(HttpServletRequest request, UserInfo userInfo, String action, String modelCode, String tableName, Long recordId,
			String valueBefore, String valueAfter, String operaId);
	
	public boolean saveLog(HttpServletRequest request, UserInfo userInfo, String action, String modelCode, String tableName, Long recordId,
			String recordName, String valueBefore, String valueAfter, String operaId);
	
	/**
	 * 保存日志2
	 * @param request
	 * @param logModule
	 * @param action
	 * @param recordId
	 * @param valueBefore
	 * @param valueAfter
	 * @return
	 */
	public boolean savelog2(HttpServletRequest request, LogModule logModule, ActionType action, 
			Long recordId, String recordName, String valueBefore, String valueAfter);
	

	/**
	 * 保存日志3
	 * @param request
	 * @param logModule
	 * @param action
	 * @param recordId
	 * @param valueBefore
	 * @param valueAfter
	 * @return
	 */
	public boolean savelog3(HttpServletRequest request, LogModule logModule, ActionType action, 
			Long recordId, String recordName, Object valueBefore, Object valueAfter); 
	
	/**
	 * 查询日志
	 * @param criteria
	 * @param page
	 * @return
	 */
	public Pagination dataChangeLogQuery(HashMap<String,Object> criteria, Pagination page);
	
	
	/**
	 * 操作类型
	 */
	public enum ActionType{
		insert("I"), 
		update("U"), 
		delete("D"),
		export("EXP"),
		view("V"),
		imported("IMP"),
		query("Q");
		
		ActionType(String value){
			this.value = value;
		}
		private String value;
		public String getValue(){
			return this.value;
		}
	}
	
}
