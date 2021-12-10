package cn.ffcs.zhsq.mybatis.domain.event;

import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.shequ.bo.BaseEntity;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 事件
 */
public class EventDisposal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5981431609183333605L;

    //事件字段T_EVENT
    private Long eventId;//事件ID  EVENT_ID

    private String code;//事件编码  CODE_

    private String type;//事件类型  TYPE_

    private String typeName;//事件类型名称 TYPE_NAME

    private String eventClass;//事件分类

    private String bigType;//事件类型大类

    private String content;//事件描述  CONTENT_

    private String occurred;//事发详址  OCCURRED

    private Date handleDate;//处理时限  HANDLE_DATE

    private String handleDateStr;//处理时限文本

    private Date happenTime;//发生时间 HAPPEN_TIME

    private String happenTimeStr;//发生时间文本

    private String tel;//联系电话  TELEPHONE

    private String contactUser;  //(联系人姓名、当事人姓名) CONTACT_USER
    
    private String informant;//举报人员
    
    private String informantTel;//举报人员电话
    
    private String isAdopted;//是否采取
    
    private String gridCode; //网格编码

    private Long gridId;//网格ID

    private String gridName;//网格名称

    private String gridPath;//网格名称全路径

    private String source;//目标来源  SOURCE 目击(01)、举报(02)

    private String sourceName;//目标来源名称 SOURCE_NAME

    private String collectWay;//采集渠道 COLLECT_WAY 01:手机录入;1:PC录入;2:呼叫中心录入

    private String collectWayName;//采集渠道名称

    private String involvedNum;//涉及人数 INVOLVED_NUM

    private Long involvedNumInt;//涉及人数(目前供晋江手动输入涉案人数用) INVOLVED_NUM_INT

    private Float involvedMoney;//涉及金额 INVOLVED_MONEY

    private String involvedNumName;//涉及人数文本

    private String involvedPersion;//涉及人员姓名 INVOLVED_PERSION

    private String influenceDegree;//影响范围  INFLUENCE_DEGREE

    private String influenceDegreeName;//影响范围名称 INFLUENCE_DEGREE_NAME

    private String urgencyDegree;//紧急程度 URGENCY_DEGREE 高(01)、中(02)、低(03)

    private String urgencyDegreeName;//紧急程度名称 URGENCY_DEGREE_NAME

    private String status;//状态:受理(00)、上报未分流(01)、上报已分流(02)、结案待评(03),结束(04),挂起(05),已删除(06),反馈（07）

    private String statusName;//状态文本  STATUS_NAME

    private String subStatus;//子状态：处理中(01)、待核实(02)

    private String subStatusName;//子状态文本
    private Long updatorId;//更新人 UPDATOR_ID
    private Date updateTime;//更新时间  UPDATE_TIME

    private Long creatorId;//创建人  CREATOR_ID

    private String creatorName;//创建人姓名 CREATOR_NAME

    private Date createTime;//创建时间  CREATE_TIME

    private String createTimeStr;//创建时间文本

    private String handleDateStatus;//时限状态  HANDLE_DATE_STATUS

    private Date finTime;//结案时间  FIN_TIME

    private String finTimeStr;//结案时间文本

    private String bizPlatform;//对接平台 BIZ_PLATFORM

    private String startWorkFlow;//事件是否启动流程   0未启动，1启动

    private String infoOrgCode;//信息域

    private String startClosedTime;
    private String endClosedTime;

    private String instanceId;
    private String workFlowId;
    private String taskId;
    private String taskName;
    private String taskStatusName;//流程当前状态 1 申请中； 2 已结束； 其他 未启动
    private Boolean isAttention; //是否关注
    private String remindStatus;//督办状态 0：有督办 空：没有 1：催办；2：督办；3：催办和督办
    private String userNames;//工作流：待办用户名
    private String userIds;//工作流：待办用户ID
    private String remarks;//督办意见
    private String supervisionType;//督办类型,3：督办-普通-黑牌；1：督办-黄牌；2：督办-红牌。字典：A001093091；默认为 0
    private String remindUserName;//督办人
    private String remindedUserName;//被督办人
    private String remindDate;//督办时间
    private String operate; //回退状态 0:开始流程 1：审核通过 2：回退 3：下一步 4：暂存
    private String remark;//备注
    private String result;//处理结果
    private Date endTime;//结束发生时间
    private String endTimeStr;//结束发生时间文本
    private String handleStatus;//超时状态
    private String wfCreate;//WF_CREATE_ 任务到达事件

    private String attrFlag;//ATTR_FLAG 附件标记，1：照片；2：语音；3：视频；
    private String handleDateFlag;//处理时限标识 1：正常；2：将到期；3：已过期

    private String eventFlag;

    private String exchangeFlag;//1：已对接 0：未对接
    
    private List<Attachment> attList = null;//附件列表

    private Map<String, Object> eventExtend = null;
    
    /**
     * 未过期 ： 00
     */
    public static String HANDLE_STATUS_TO = "00";//未过期
    /**
     * 将过期 ： 01
     */
    public static String HANDLE_STATUS_IN = "01";
    /**
     * 已过期 ： 02
     */
    public static String HANDLE_STATUS_OVER = "02";//过期
    /**
     * 涉案线路
     */

    private String eventName;
    private String eventPeopleName;
    private String idCard;
    private String cardType;
    private String isDetection;
    private Long fugitiveAmount;
    private Long arrestedAmount;
    private String detectedDesc;
    private String dJudgeResult;//双随机事件评价结果
    private Long dId;//双随机事件id
    private String dBeginTime;//双随机事件复核截止时间
    private String dEndTime;//双随机事件复核截止时间
    private String dTaskType;//双随机任务类型


    /**
     * 涉事人员，以；分隔人员，以，分隔人员中的姓名和身份证
     */
    private String eventInvolvedPeople;

    /*******************重点人员管理*****************/
    VisitRecord visitRecord = null;
    /*******************重点人员关联表***************/
    //EventVisitRela eventVisitRela = new EventVisitRela();
    /*******************出租户管理表***************/
    EventRent eventRent = null;
    /*******************消防安全表***************/
    EventSurvey eventSurvey = new EventSurvey();
    /*******************标注信息表***************/
    ResMarker resMarker = new ResMarker();
    /*******************事件涉及人员表***************/

    /*******************事件上报关联模块***************/
    EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();

    //涉案线路
    private String eventNature; //案件性质
    private IncidentInvolved incidentInvolved;
    private InvolvedPeople involvedPeople;


    private String happenTimeStart;//发生时间开始
    private String happenTimeEnd;//发生时间结束

    private String createTimeStart;//创建时间开始文本
    private String createTimeEnd;//创建时间结束文本

    private String updateTimeStart;//更新时间开始
    private String updateTimeEnd;//更新时间结束

    private String handleDateStart;//处理时限开始
    private String handleDateEnd;//处理时限结束

    private Integer handleDateInterval;//处理时限时间间隔 相对采集时间

    //工作流
    private String wfStatus;//工作流流程状态

    private String taskReceivedStaus;//任务接收状态 0：未接收；1：已接收

    private String attachmentIds;//事件相关的附件id，以英文逗号相连

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getEventNature() {
        return eventNature;
    }

    public void setEventNature(String eventNature) {
        this.eventNature = eventNature;
    }

    public IncidentInvolved getIncidentInvolved() {
        return incidentInvolved;
    }

    public void setIncidentInvolved(IncidentInvolved incidentInvolved) {
        this.incidentInvolved = incidentInvolved;
    }

    public InvolvedPeople getInvolvedPeople() {
        return involvedPeople;
    }

    public void setInvolvedPeople(InvolvedPeople involvedPeople) {
        this.involvedPeople = involvedPeople;
    }

    public String getCollectWayName() {
        return collectWayName;
    }


    public void setCollectWayName(String collectWayName) {
        this.collectWayName = collectWayName;
    }

    public String getBigType() {
        return bigType;
    }


    public void setBigType(String bigType) {
        this.bigType = bigType;
    }


    public String getEventClass() {
        return eventClass;
    }


    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }


    public String getInvolvedNumName() {
        return involvedNumName;
    }


    public void setInvolvedNumName(String involvedNumName) {
        this.involvedNumName = involvedNumName;
    }

    public void setHappenTimeStr(String happenTimeStr) {
        this.happenTimeStr = happenTimeStr;
    }

    public String getHappenTimeStr() {
        return happenTimeStr;
    }

    public Long getEventId() {
        return eventId;
    }


    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getTypeName() {
        return typeName;
    }


    public void setTypeName(String typeName) {
        this.typeName = typeName;
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


    public Date getHandleDate() {
        return handleDate;
    }


    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }


    public String getHandleDateStr() {
        return handleDateStr;
    }


    public void setHandleDateStr(String handleDateStr) {
        this.handleDateStr = handleDateStr;
    }


    public Date getHappenTime() {
        return happenTime;
    }


    public void setHappenTime(Date happenTime) {

        this.happenTime = happenTime;

    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getInformant() {
		return informant;
	}

	public void setInformant(String informant) {
		this.informant = informant;
	}

	public String getInformantTel() {
		return informantTel;
	}

	public void setInformantTel(String informantTel) {
		this.informantTel = informantTel;
	}
	
	public String getIsAdopted() {
		return isAdopted;
	}

	public void setIsAdopted(String isAdopted) {
		this.isAdopted = isAdopted;
	}

	public String getGridCode() {
        return gridCode;
    }


    public void setGridCode(String gridCode) {
        this.gridCode = gridCode;
    }


    public Long getGridId() {
        return gridId;
    }


    public void setGridId(Long gridId) {
        this.gridId = gridId;
    }


    public String getGridName() {
        return gridName;
    }


    public void setGridName(String gridName) {
        this.gridName = gridName;
    }


    public String getSource() {
        return source;
    }


    public void setSource(String source) {
        this.source = source;
    }


    public String getSourceName() {
        return sourceName;
    }


    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }


    public String getCollectWay() {
        return collectWay;
    }


    public void setCollectWay(String collectWay) {
        this.collectWay = collectWay;
    }


    public String getInvolvedNum() {
        return involvedNum;
    }


    public void setInvolvedNum(String involvedNum) {
        this.involvedNum = involvedNum;
    }

    public Long getInvolvedNumInt() {
        return involvedNumInt;
    }

    public void setInvolvedNumInt(Long involvedNumInt) {
        this.involvedNumInt = involvedNumInt;
    }

    public String getInfluenceDegree() {
        return influenceDegree;
    }


    public void setInfluenceDegree(String influenceDegree) {
        this.influenceDegree = influenceDegree;
    }


    public String getInfluenceDegreeName() {
        return influenceDegreeName;
    }


    public void setInfluenceDegreeName(String influenceDegreeName) {
        this.influenceDegreeName = influenceDegreeName;
    }

    public String getUrgencyDegree() {
        return urgencyDegree;
    }


    public void setUrgencyDegree(String urgencyDegree) {
        this.urgencyDegree = urgencyDegree;
    }


    public String getUrgencyDegreeName() {
        return urgencyDegreeName;
    }


    public void setUrgencyDegreeName(String urgencyDegreeName) {
        this.urgencyDegreeName = urgencyDegreeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getStatusName() {
        return statusName;
    }


    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getHandleDateStatus() {
        return handleDateStatus;
    }


    public void setHandleDateStatus(String handleDateStatus) {
        this.handleDateStatus = handleDateStatus;
    }

    public String getEventInvolvedPeople() {
        return eventInvolvedPeople;
    }

    public void setEventInvolvedPeople(String eventInvolvedPeople) {
        this.eventInvolvedPeople = eventInvolvedPeople;
    }

    public VisitRecord getVisitRecord() {
        return visitRecord;
    }

    public void setVisitRecord(VisitRecord visitRecord) {
        this.visitRecord = visitRecord;
    }

	/*public EventVisitRela getEventVisitRela() {
        return eventVisitRela;
	}

	public void setEventVisitRela(EventVisitRela eventVisitRela) {
		this.eventVisitRela = eventVisitRela;
	}*/

    public EventRent getEventRent() {
        return eventRent;
    }

    public void setEventRent(EventRent eventRent) {
        this.eventRent = eventRent;
    }

    public EventSurvey getEventSurvey() {
        return eventSurvey;
    }

    public void setEventSurvey(EventSurvey eventSurvey) {
        this.eventSurvey = eventSurvey;
    }

    /**
     * 获取剩余的时间数值
     *
     * @return
     */
    public String getRemainTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";
        long startMinute = new Date().getTime() / 1000;
        long endMinute = 0;
        if (handleDateStr == null || "".equals(handleDateStr)) {
            return "不限时";
        }
        try {
            endMinute = format.parse(handleDateStr).getTime() / 1000 ;
        } catch (ParseException e) {
            e.printStackTrace();
            return "不限时";
        }
        if (finTime != null) {
            //已经结案
            startMinute = finTime.getTime() / 1000 ;
        }
        if (endMinute - startMinute < 0) {
            if (finTime != null) {
                result += "延后";
            } else {
                result += "超时";
            }
        } else {
            if (finTime != null) {
                result += "提前";
            } else {
                result += "剩余";
            }
        }
        return result + this.formatSecond((int) Math.abs((endMinute - startMinute)));
    }

    public String getRemainTime(String timeStr) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";
        long startMinute = new Date().getTime() / 1000 ;
        long endMinute = 0;
        if (timeStr == null || "".equals(timeStr)) {
            return "不限时";
        }
        try {
            endMinute = format.parse(timeStr).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return "不限时";
        }
        if (endMinute - startMinute < 0) {
            result += "超时";
        } else {
            result += "剩余";
        }
        return result + this.formatSecond((int) Math.abs((endMinute - startMinute)));
    }

    /**
     * 格式化分钟，如“2000”转化为“1天9小时20分钟”
     *
     * @param minutes
     * @return
     */
    public String formatMinute(int minutes) {
        if (minutes <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int m = minutes % 60;
        if (m != 0) {
            sb.append(m).append("分钟");
        }
        int hours = minutes / 60;
        if (hours == 0) {
            return sb.toString();
        }
        int h = hours % 24;
        if (h != 0) {
            sb.insert(0, h + "小时");
        }
        int days = hours / 24;
        if (days == 0) {
            return sb.toString();
        }
        sb.insert(0, days + "天");
        return sb.toString();
    }
    
    
    /**
     * 格式化秒钟
     *
     * @param second
     * @return
     */
    public String formatSecond(int second) {
        if (second <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        
        int s = second % 60;
        if (s != 0) {
            sb.append(s).append("秒");
        }
        
        int seconds = second / 60;
        if (seconds == 0) {
            return sb.toString();
        }
        
        int m = seconds % 60;
        if (m != 0) {
            sb.insert(0, m + "分钟");
        }
        int hours = seconds / 60;
        if (hours == 0) {
            return sb.toString();
        }
        int h = hours % 24;
        if (h != 0) {
            sb.insert(0, h + "小时");
        }
        int days = hours / 24;
        if (days == 0) {
            return sb.toString();
        }
        sb.insert(0, days + "天");
        return sb.toString();
    }
    

    public ResMarker getResMarker() {
        return resMarker;
    }

    public void setResMarker(ResMarker resMarker) {
        this.resMarker = resMarker;
    }

    public EventReportRecordInfo getEventReportRecordInfo() {
        return eventReportRecordInfo;
    }

    public void setEventReportRecordInfo(EventReportRecordInfo eventReportRecordInfo) {
        this.eventReportRecordInfo = eventReportRecordInfo;
    }

    public String getStartWorkFlow() {
        return startWorkFlow;
    }

    public void setStartWorkFlow(String startWorkFlow) {
        this.startWorkFlow = startWorkFlow;
    }

    public String getStartClosedTime() {
        return startClosedTime;
    }

    public void setStartClosedTime(String startClosedTime) {
        this.startClosedTime = startClosedTime;
    }

    public String getEndClosedTime() {
        return endClosedTime;
    }

    public void setEndClosedTime(String endClosedTime) {
        this.endClosedTime = endClosedTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPeopleName() {
        return eventPeopleName;
    }

    public void setEventPeopleName(String eventPeopleName) {
        this.eventPeopleName = eventPeopleName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIsDetection() {
        return isDetection;
    }

    public void setIsDetection(String isDetection) {
        this.isDetection = isDetection;
    }

    public Long getFugitiveAmount() {
        return fugitiveAmount;
    }

    public void setFugitiveAmount(Long fugitiveAmount) {
        this.fugitiveAmount = fugitiveAmount;
    }

    public Long getArrestedAmount() {
        return arrestedAmount;
    }

    public void setArrestedAmount(Long arrestedAmount) {
        this.arrestedAmount = arrestedAmount;
    }

    public String getDetectedDesc() {
        return detectedDesc;
    }

    public void setDetectedDesc(String detectedDesc) {
        this.detectedDesc = detectedDesc;
    }

    public Float getInvolvedMoney() {
        return involvedMoney;
    }

    public void setInvolvedMoney(Float involvedMoney) {
        this.involvedMoney = involvedMoney;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getInvolvedPersion() {
        return involvedPersion;
    }

    public void setInvolvedPersion(String involvedPersion) {
        this.involvedPersion = involvedPersion;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getFinTime() {
        return finTime;
    }

    public void setFinTime(Date finTime) {
        this.finTime = finTime;
    }

    public String getFinTimeStr() {
        return finTimeStr;
    }

    public void setFinTimeStr(String finTimeStr) {
        this.finTimeStr = finTimeStr;
    }

    public String getBizPlatform() {
        return bizPlatform;
    }

    public void setBizPlatform(String bizPlatform) {
        this.bizPlatform = bizPlatform;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getHappenTimeStart() {
        return happenTimeStart;
    }

    public void setHappenTimeStart(String happenTimeStart) {
        this.happenTimeStart = happenTimeStart;
    }

    public String getHappenTimeEnd() {
        return happenTimeEnd;
    }

    public void setHappenTimeEnd(String happenTimeEnd) {
        this.happenTimeEnd = happenTimeEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getUpdateTimeStart() {
        return updateTimeStart;
    }

    public void setUpdateTimeStart(String updateTimeStart) {
        this.updateTimeStart = updateTimeStart;
    }

    public String getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(String updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    public String getHandleDateStart() {
        return handleDateStart;
    }

    public void setHandleDateStart(String handleDateStart) {
        this.handleDateStart = handleDateStart;
    }

    public String getHandleDateEnd() {
        return handleDateEnd;
    }

    public void setHandleDateEnd(String handleDateEnd) {
        this.handleDateEnd = handleDateEnd;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public Boolean getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(Boolean isAttention) {
        this.isAttention = isAttention;
    }

    public String getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(String remindStatus) {
        this.remindStatus = remindStatus;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemindUserName() {
        return remindUserName;
    }

    public void setRemindUserName(String remindUserName) {
        this.remindUserName = remindUserName;
    }

    public String getRemindedUserName() {
        return remindedUserName;
    }

    public void setRemindedUserName(String remindedUserName) {
        this.remindedUserName = remindedUserName;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getInfoOrgCode() {
        return infoOrgCode;
    }

    public void setInfoOrgCode(String infoOrgCode) {
        this.infoOrgCode = infoOrgCode;
    }

    public String getWfCreate() {
        return wfCreate;
    }

    public void setWfCreate(String wfCreate) {
        this.wfCreate = wfCreate;
    }

    public String getAttrFlag() {
        return attrFlag;
    }

    public void setAttrFlag(String attrFlag) {
        this.attrFlag = attrFlag;
    }

    public String getHandleDateFlag() {
        return handleDateFlag;
    }

    public void setHandleDateFlag(String handleDateFlag) {
        this.handleDateFlag = handleDateFlag;
    }

    public Integer getHandleDateInterval() {
        return handleDateInterval;
    }

    public void setHandleDateInterval(Integer handleDateInterval) {
        this.handleDateInterval = handleDateInterval;
    }

    public String getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(String eventFlag) {
        this.eventFlag = eventFlag;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getSubStatusName() {
        return subStatusName;
    }

    public void setSubStatusName(String subStatusName) {
        this.subStatusName = subStatusName;
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag;
    }

    public String getTaskReceivedStaus() {
        return taskReceivedStaus;
    }

    public void setTaskReceivedStaus(String taskReceivedStaus) {
        this.taskReceivedStaus = taskReceivedStaus;
    }

    public String getGridPath() {
        return gridPath;
    }

    public void setGridPath(String gridPath) {
        this.gridPath = gridPath;
    }

    public String getdJudgeResult() {
        return dJudgeResult;
    }

    public void setdJudgeResult(String dJudgeResult) {
        this.dJudgeResult = dJudgeResult;
    }

    public Long getdId() {
        return dId;
    }

    public void setdId(Long dId) {
        this.dId = dId;
    }

    public String getdBeginTime() {
        return dBeginTime;
    }

    public void setdBeginTime(String dBeginTime) {
        this.dBeginTime = dBeginTime;
    }

    public String getdEndTime() {
        return dEndTime;
    }

    public void setdEndTime(String dEndTime) {
        this.dEndTime = dEndTime;
    }

    public String getdTaskType() {
        return dTaskType;
    }

    public void setdTaskType(String dTaskType) {
        this.dTaskType = dTaskType;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

	public List<Attachment> getAttList() {
		return attList;
	}

	public void setAttList(List<Attachment> attList) {
		this.attList = attList;
	}

    public Map<String, Object> getEventExtend() {
		return eventExtend;
	}

	public void setEventExtend(Map<String, Object> eventExtend) {
		this.eventExtend = eventExtend;
	}

	public String getSupervisionType() {
        return supervisionType;
    }

    public void setSupervisionType(String supervisionType) {
        this.supervisionType = supervisionType;
    }


}
