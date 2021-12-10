package cn.ffcs.zhsq.mybatis.domain.event;

import cn.ffcs.shequ.base.elasticsearch.bo.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:事件刷到Es实体
 * @Author: ztc
 * @Date: 2018/9/7 9:37
 */
public class EventDisposalEs implements Serializable{
    private static final long serialVersionUID = -5981431609183233665L;

    //事件字段T_EVENT
    private Long eventId;//事件ID  EVENT_ID
    private Long instanceId;//事件实例id
    private Long workFlowId;//事件工作流workFlowId
    private String eventName;//事件名称EVENT_NAME
    private String content;//事件描述  CONTENT_
    private String occurred;//事发详址  OCCURRED
    private String code;//事件编码  CODE_
    private Long gridId;//网格ID
    private String gridCode; //网格编码
    private String gridPath; //网格全路径
    private Date createTime;//创建时间  CREATE_TIME
    private Date happenTime;//发生时间 HAPPEN_TIME
    private Date handleDate;//办结期限 HANDLE_DATE
    private String contactUser;  //(联系人姓名、当事人姓名) CONTACT_USER
    private String tel;//联系电话  TELEPHONE
    private String type;//事件类型  TYPE_
    private String source;//目标来源  SOURCE 目击(01)、举报(02)
    private String influenceDegree;//影响范围  INFLUENCE_DEGREE
    private String urgencyDegree;//紧急程度 URGENCY_DEGREE 高(01)、中(02)、低(03)
    private String attrFlag;//ATTR_FLAG 附件标记，1：照片；2：语音；3：视频；
    private String eventStatus;//状态:受理(00)、上报未分流(01)、上报已分流(02)、结案待评(03),结束(04),挂起(05),已删除(06),反馈（07）
    private String collectWay;//采集渠道
    private String bizPlatform;//对接平台
    private String creatorOrgCode;//采集人所属组织
    private String bigText;//事件拼接大文本，标题 + 类型 + 地址 + 描述
    private String x;//经度
    private String y;//纬度


    //字典翻译字段
    private String typeCN;//事件类型名称 TYPE_NAME
    private String sourceCN;//目标来源名称 SOURCE_NAM
    private String influenceDegreeCN;//影响范围名称 INFLUENCE_DEGREE_NAME
    private String urgencyDegreeCN;//紧急程度名称 URGENCY_DEGREE_NAME
    private String attrFlagMCN;//ATTR_FLAG 附件标记，1：照片；2：语音；3：视频；
    private String eventStatusCN;//状态文本  STATUS_NAME
    private String collectWayCN;//采集渠道
    private String bizPlatformCN;//对接平台

    private String moduleType;


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOccurred() {
        return occurred;
    }

    public void setOccurred(String occurred) {
        this.occurred = occurred;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getGridId() {
        return gridId;
    }

    public void setGridId(Long gridId) {
        this.gridId = gridId;
    }

    public String getGridCode() {
        return gridCode;
    }

    public void setGridCode(String gridCode) {
        this.gridCode = gridCode;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getHandleDate() { return handleDate; }

    public void setHandleDate(Date handleDate) { this.handleDate = handleDate; }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInfluenceDegree() {
        return influenceDegree;
    }

    public void setInfluenceDegree(String influenceDegree) {
        this.influenceDegree = influenceDegree;
    }

    public String getUrgencyDegree() {
        return urgencyDegree;
    }

    public void setUrgencyDegree(String urgencyDegree) {
        this.urgencyDegree = urgencyDegree;
    }

    public String getAttrFlag() {
        return attrFlag;
    }

    public void setAttrFlag(String attrFlag) {
        this.attrFlag = attrFlag;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTypeCN() {
        return typeCN;
    }

    public void setTypeCN(String typeCN) {
        this.typeCN = typeCN;
    }

    public String getSourceCN() {
        return sourceCN;
    }

    public void setSourceCN(String sourceCN) {
        this.sourceCN = sourceCN;
    }

    public String getInfluenceDegreeCN() {
        return influenceDegreeCN;
    }

    public void setInfluenceDegreeCN(String influenceDegreeCN) {
        this.influenceDegreeCN = influenceDegreeCN;
    }

    public String getUrgencyDegreeCN() {
        return urgencyDegreeCN;
    }

    public void setUrgencyDegreeCN(String urgencyDegreeCN) {
        this.urgencyDegreeCN = urgencyDegreeCN;
    }

    public String getAttrFlagMCN() {
        return attrFlagMCN;
    }

    public void setAttrFlagMCN(String attrFlagMCN) {
        this.attrFlagMCN = attrFlagMCN;
    }

    public String getEventStatusCN() {
        return eventStatusCN;
    }

    public void setEventStatusCN(String eventStatusCN) {
        this.eventStatusCN = eventStatusCN;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getCollectWay() {
        return collectWay;
    }

    public void setCollectWay(String collectWay) {
        this.collectWay = collectWay;
    }

    public String getCollectWayCN() {
        return collectWayCN;
    }

    public void setCollectWayCN(String collectWayCN) {
        this.collectWayCN = collectWayCN;
    }

    public String getBizPlatform() {
        return bizPlatform;
    }

    public void setBizPlatform(String bizPlatform) {
        this.bizPlatform = bizPlatform;
    }

    public String getBizPlatformCN() {
        return bizPlatformCN;
    }

    public void setBizPlatformCN(String bizPlatformCN) {
        this.bizPlatformCN = bizPlatformCN;
    }

    public String getGridPath() {
        return gridPath;
    }

    public void setGridPath(String gridPath) {
        this.gridPath = gridPath;
    }

    public String getCreatorOrgCode() {
        return creatorOrgCode;
    }

    public void setCreatorOrgCode(String creatorOrgCode) {
        this.creatorOrgCode = creatorOrgCode;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(Long workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }
}
