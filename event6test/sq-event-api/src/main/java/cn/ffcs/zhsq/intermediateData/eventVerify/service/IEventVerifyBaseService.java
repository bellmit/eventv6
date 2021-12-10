package cn.ffcs.zhsq.intermediateData.eventVerify.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

public interface IEventVerifyBaseService {
	/**
	 * 新增微信事件
	 * @param eventVerify
	 * 				Long 	bizId 			对接平台记录id
	 * 				String	bizPlatform		对接平台，字典编码为B563
	 * 				String 	eventName 		事件标题
	 * 				String 	occurred 		事发地址
	 * 				String 	content 		事件描述
	 * 				java.util.Date 	happenTime 事发时间
	 * 				String  happenTimeStr	事发时间文本
	 * 				String 	infoOrgCode 	所属地域编码
	 * 				String 	status 			状态，字典编码为B195
	 * 				String 	contactUser 	联系人员
	 * 				String 	tel 			联系电话
	 * 				String 	remark 			办理意见
	 * 				String 	longitude 		经度
	 * 				String 	latitude 		纬度
	 * 				String	markerType		地图地位类型，为空时，不存储地位信息
	 * 				String	eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * 				Map<String, Object> dataJsonMap	事件扩展属性
	 * 					Integer isShow2Public		是否公开事件；1 公开；0 不公开
	 * 					String	eventBizPlatform	事件对接平台编码，为空时设置为与属性bizPlatform保持一致
	 * 
	 * 				String userOrgCode		组织编码
	 * 				String verifyType		审核类型，EventCase 案件审核；
	 * @return 新增成功返回eventVerifyId，否则返回-1
	 * @throws Exception 
	 */
	public Long saveEventVerify(Map<String, Object> eventVerify) throws Exception;

	/**
	 * 依据主键id修改事件审核记录
	 * @param eventVerify
	 * 			String eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * 			Boolean isForce2Update			是否强制更新，true为是；默认为false
	 * 			String verifyType				审核类型，EventCase 案件审核；
	 * @param userInfo		更新操作用户
	 * @return 更新成功返回true，否则返回false
	 * @throws Exception 
	 */
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception;

	/**
	 * 依据主键id删除微信事件
	 * @param eventVerifyId	事件审核记录主键
	 * @param delUserId		删除操作用户id
	 * @param eventVerify
	 * 			eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * @return
	 */
	public boolean deleteEventVerifyById(Long eventVerifyId, Long delUserId, Map<String, Object> eventVerify);

	/**
	 * 依据主键id查找事件审核记录
	 * @param eventVerifyId
	 * @param eventVerify
	 * 			userOrgCode	组织编码，不为空时，转换字典相关属性
	 * 			eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * @return
	 */
	public Map<String, Object> findEventVerifyById(Long eventVerifyId, Map<String, Object> eventVerify);
	
	/**
	 * 依据相关条件查询事件审核记录
	 * @param params
	 * 			userOrgCode		组织编码，不为空时，转换字典相关属性
	 * 			eventId			事件id
	 * 			bizPlatformList	对接平台编码，类型为List<String>，优先于bizPlatform使用
	 * 			bizPlatformForSearch	事件审核对接平台，多个值使用英文逗号分隔
	 *                                  优先于初始化生成的平台来源搜索条件使用
	 *                                  并且会比照默认初始化的来源平台取交集进行列表展示
	 *          isCapConfigureParam     是否开启使用功能配置中的来源平台搜索查询条件
	 * 			verifyType		审核类型，String类型，EventCase 案件审核；
	 * @return
	 */
	public List<Map<String, Object>> findEventVerifyByParam(Map<String, Object> params);
	
	/**
	 * 获取事件审核记录数量
	 * @param params
	 * 			isJurisdictionQuery		是否进行辖区内查询，如果为true则查询infoOrgCode辖区内数据，默认为false
	 * 			isDefaultJurisdiction	是否使用默认地域查询，如果为true则在infoOrgCode为空时，会设置默认地域编码；默认为true
	 * 			infoOrgCode				地域编码
	 * 			userOrgCode				组织编码
	 * 			eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * 			statusList				状态，类型为List<String>，该属性优先于status
	 * 			status					状态
	 * 			bizPlatformList			对接平台编码，类型为List<String>，优先于bizPlatform使用
	 * 			bizPlatform				对接平台，多个值使用英文逗号分隔
	 * 			verifyType				审核类型，String类型，EventCase 案件审核；
	 * 			
	 * 			isCapConfigureParam		是否使用配置参数，true为是，默认为false
	 * @return
	 */
	public int findEventVerifyCount(Map<String, Object> params);
	
	/**
	 * 分页获取事件审核记录
	 * @param pageNo	页码
	 * @param pageSize	每页显示记录数
	 * @param params
	 * 			isJurisdictionQuery		是否进行辖区内查询，如果为true则查询infoOrgCode辖区内数据
	 * 			isDefaultJurisdiction	是否使用默认地域查询，如果为true则在infoOrgCode为空时，会设置默认地域编码；默认为true
	 * 			infoOrgCode				地域编码
	 * 			userOrgCode				组织编码
	 * 			eventVerifyServiceName	指定事件审核事件类，默认为eventVerifyService
	 * 			statusList				状态，类型为List<String>，该属性优先于status
	 * 			status					状态
	 * 			verifyType				审核类型，String类型，EventCase 案件审核；
	 * 			
	 * 			isCapConfigureParam		是否使用配置参数，true为是，默认为false
	 * @return
	 */
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 获取构造事件页面使用相关属性
	 * @param eventVerify
	 * 			eventVerifyId		审核记录id
	 * 			userOrgCode			组织编码
	 * 			isCapAttachmentId	是否获取相关附件id信息，true为获取，默认为false
	 * @return
	 * 		outerAttachmentIds	附件id，使用英文逗号连接，当isCapAttachmentId为true时，获取该属性
	 * 		isShowCloseBtn		true则展示结案按钮；false则不展示
	 * 		isShowSaveBtn		true则展示保存按钮；false则不展示
	 * 		isReport			true则提交按钮进行上报操作；false则提交按钮进行启动操作
	 * 		verifyType			审核类型，String类型，EventCase 案件审核；
	 */
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify);
}
