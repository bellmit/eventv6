package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;

import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/05/18.
 */
public interface IEventSBREClueService extends IEventSBREClueWorkflowService {

    /**
     * 新增线索
     * @param eventSBREClue
     * @param params
     * 			attachmentIds	附件id，以英文逗号分隔
     * 			isAlterGangRela	是否修改黑恶团伙关联关系；true为是；默认为false
     *          gangIds 		黑恶团伙ids，以英文逗号分隔
     * @param userInfo
     * @return 线索主键
     * @throws Exception
     */
    public Long saveClue(EventSBREClue eventSBREClue, Map<String,Object> params, UserInfo userInfo) throws Exception;

    /**
     * 根据线索id删除线索
     * @param clueId	线索id
     * @param userId	删除者id
     * @return
     */
    public Boolean deleteByClueId(Long clueId,Long userId);
    /**
     * 根据线索id更新问题
     * @param eventSBREClue
     * @param params
     * 			attachmentIds	附件id，以英文逗号分隔
     * 			isAlterGangRela	是否修改黑恶团伙关联关系；true为是；默认为false
     *          gangIds 		黑恶团伙ids，以英文逗号分隔
     * @param userInfo
     * @return
     * @throws Exception
     */
    public Boolean updateClue(EventSBREClue eventSBREClue,Map<String,Object> params,UserInfo userInfo) throws Exception;
    /**
     * 依据线索id查找线索记录
     * @param clueId	线索id
     * @param params	额外信息
     * 			userOrgCode			组织编码
     * 			isCheckEncrypt		验证指定人员是否可见加密信息；true为验证，false为不验证；默认为false
     * 			encryptUserId		查看加密信息的用户id，isCheckEncrypt为true时，该属性必填
     * 			encryptOrgId		查看加密信息的组织id，isCheckEncrypt为true时，该属性必填
     * 			isCapGangInfo		是否获取黑恶团伙信息，true为获取；默认为false
     * 			isCapInformantInfo	是否获取举报人信息，true为获取；默认为false
     * 			isCapReportedInfo	是否获取被举报人信息，true为获取；默认为false
     * 			isCapDisposeUnit	是否获取综治单位信息，true为获取；默认为false
     * 			
     * @return
     * 			gangList			黑恶团伙信息，类型为List<EventSBREvilGang>，isCapGangInfo为true时获取
     * 			informantInfo		举报人信息，类型为InvolvedPeople，isCapInformantInfo为true时获取
     * 			reportedInfoList	被举报人信息，类型为List<InvolvedPeople>，isCapReportedInfo为true时获取
     * 			disposeUnitInfo		综治单位信息，类型为cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople，isCapDisposeUnit为true时获取
     * @throws Exception 
     */
    public Map<String,Object> findByClueId(Long clueId, Map<String, Object> params) throws Exception;
    
    /**
     * 确认用户是否有查看指定线索的权限
     * @param clueId	线索id
     * @param userInfo	用户信息
     * 			userId	用户id
     * 			orgId	用户组织id
     * @return
     * @throws Exception
     */
    public boolean checkAuthority(Long clueId, UserInfo userInfo) throws Exception;
    /**
     * 
     * @param clue			线索信息
     * @param userInfo		用户信息
     * 			userId		用户id
     * 			orgId		用户组织id
     * @return
     * @throws Exception
     */
    public boolean checkAuthority(EventSBREClue clue, UserInfo userInfo) throws Exception;
    
    /**
     * 查询线索信息（分页）
     * @param params 查询参数
     * 			listType			列表类型
     * 				1   草稿
     *          	2   待办
     *          	3   经办
     *          	4   辖区所有
     * 			creatorId			线索创建者id
     * 			curUserId			当前办理人员id，数据类型String
     * 			curOrgId			当前办理组织id，数据类型String
     * 			handledUserId		经办人员id，数据类型Long
     * 			handledOrgId		经办组织id，数据类型Long
     * 			isCopyClue			是否拷贝线索，true为拷贝；false不是；默认为false
     * 			infoOrgCode			发生地域编码
     * 			regionCode			所属地域编码
     * 			startInfoOrgCode	默认发生地域编码，infoOrgCode为空，listType为4，isCopyClue为false时，该属性会替换infoOrgCode
     * 			startRegionCode		默认所属地域编码，regionCode为空，listType为4，isCopyClue为false时，该属性会替换regionCode
     * 			involvedPeopleName	依据举报人/被举报人姓名模糊查询线索信息
     * 			involvedPeopleName4Accurate	依据举报人/被举报人姓名精确查询线索信息，需要使用属性involvedPeopleBizType
     * 			involvedPeopleBizType		09 举报人；10 被举报人
     * @return 线索分页数据对象
     */
    public EUDGPagination findCluePagination(int page, int rows, Map<String,Object> params,UserInfo userInfo);
    
    /**
     * 统计线索记录
     * @param params
     * 			involvedPeopleName4Accurate	用于精确查找的举报人员/被举报人员姓名
     * 			involvedPeopleBizType		人员业务类型，09 举报人员；10 被举报人员
     * @param userInfo						用户信息
     * @return
     */
    public int findClueCount(Map<String,Object> params, UserInfo userInfo);
    /**
     * 获取线索辖区所有列表 不分页
     * @param params
     * @return
     */
    public List<Map<String, Object>> findEventSBREClueList(Map<String, Object> params, UserInfo userInfo);
}
