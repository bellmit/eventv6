package cn.ffcs.zhsq.reportFocus;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 两违防治业务扩展信息操作接口
 * @ClassName:   IReportFocusExtendService   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午3:09:46
 */
public interface IReportFocusExtendService {
	/**
	 * 保存/更新扩展信息
	 * @param reportExtend		两违信息
	 * @param userInfo			操作用户
	 * @return	操作成功返回uuid，否则返回null
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public String saveReportExtendInfo(Map<String, Object> reportExtend, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据reportUUID上报扩展信息
	 * @param reportUUID	上报uuid
	 * @param userInfo		操作用户
	 * @param params		额外信息
	 * 			isWithReportFocus	是否获取重点关注上报信息，默认为true
	 * @return
	 */
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 统计上报扩展信息数量
	 * @param params	查询参数
	 * @return
	 * @throws Exception 
	 */
	public int findCount4ReportExtend(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页获取上报扩展信息记录
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params	查询参数
	 * @return
	 * @throws Exception 
	 */
	public EUDGPagination findPagination4ReportExtend(int pageNo, int pageSize, Map<String, Object> params) throws Exception;

	/**
	 * 不分页获取上报扩展信息记录
	 * @param params	查询参数
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findList4ReportFocus(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取编号配置业务编码
	 * @return
	 */
	public String capNumberCfgBizCode();
	
	/**
	 * 格式化重点上报主表入参
	 * @param params
	 * 			regionCode	地域编码
	 * @throws Exception
	 */
	public void formatParamIn4Report(Map<String, Object> params) throws Exception;
	
	/**
	 * 短信/消息模板个性调整
	 * @param smsContent	短信/消息模板内容
	 * @param params		
	 * @param userInfo		操作用户信息
	 * @return 个性适配之后的短信/消息模板内容
	 * @throws Exception
	 */
	public String capSmsContent(String smsContent, Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 积分记录(OA)
	 * @param recordType	记录类型，1 提交操作；2 督办操作；
	 * @param userInfo		操作用户信息
	 * @param params		额外参数
	 * 必填参数
	 * 			instanceId	流程实例id
	 * 			proInstance	流程实例，类型：cn.ffcs.workflow.om.ProInstance
	 * 			reportType	报告类型
	 * @return
	 * @throws Exception
	 */
	public boolean recordPoint(int recordType, UserInfo userInfo, Map<String, Object> params) throws Exception;
}
