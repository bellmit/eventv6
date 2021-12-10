package cn.ffcs.zhsq.event.service;

import java.io.InputStream;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 事件导入记录相关接口 T_EVENT_IMPORT
 * @author zhangls
 *
 */
public interface IEventImport4FileService {
	/**
	 * 读取csv文件，批量导入数据
	 * @param fileInputStream	csv文件流
	 * @return
	 * 		successTotal	成功导入记录数
	 * 		total			需要导入记录数
	 * @throws Exception
	 */
	public Map<String, Object> recordImport(InputStream fileInputStream) throws Exception;
	
	/**
	 * 批量导入数据
	 * @param fileInputStream
	 * @param fileSuffix	导入文件后缀，支持csv、xls
	 * @return
	 * 		successTotal	成功导入记录数
	 * 		total			需要导入记录数
	 * @throws Exception
	 */
	public Map<String, Object> recordImport(InputStream fileInputStream, String fileSuffix) throws Exception;
	
	/**
	 * 启动线程将未归档的记录，归档处理
	 */
	public void startThread4Archive();
	
	/**
	 * 暂停线程
	 */
	public void endThread4Archive();
	
	/**
	 * 依据rowid更新记录
	 * @param eventMap
	 * 			eventId		事件id
	 * 			msgWrong	导入提示信息
	 * 			status		导入状态
	 * 			rowid		
	 * @return
	 */
	public boolean updateEventImportByRowid(Map<String, Object> eventMap);
	
	/**
	 * 依据rowid删除记录，真删除
	 * @param rowid
	 * @return
	 */
	public boolean delEventImportByRowid(String rowid);
	
	/**
	 * 获取中间表T_EVENT_IMPORT列信息
	 * @return
	 * 		列名为key
	 * 		COLUMN_NAME	列名
	 * 		DATA_TYPE	列类型，VARCHAR、CHAR、NUMBER
	 * 		DATA_LENGTH	列长度
	 * 		NULLABLE	是否可为空，Y可为空；N不可为空
	 * 		COMMENTS	列备注信息
	 */
	public Map<String, Map<String, Object>> findTableColInfo();
	
	/**
	 * 依据rowid获取记录
	 * @param rowid
	 * @return
	 * 		key：列名
	 * 		value:	COLUMN_NAME		列名
	 * 				COLUMN_VALUE	列值
	 * 				DATA_TYPE		列类型，VARCHAR、CHAR、NUMBER
	 * 				DATA_LENGTH		列长度
	 * 				NULLABLE		是否可为空，Y可为空；N不可为空
	 */
	public Map<String, Map<String, Object>>  findEventImportByRowid(String rowid);
	
	/**
	 * 分页获取导入的数据
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			keyWord	关键字，事件标题、内容、发生地址
	 * 			status	导入状态
	 * @return
	 */
	public EUDGPagination findEventImportPagination(int pageNo, int pageSize,
			Map<String, Object> params);
}
