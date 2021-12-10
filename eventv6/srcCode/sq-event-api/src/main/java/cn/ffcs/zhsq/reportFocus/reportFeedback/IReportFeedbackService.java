package cn.ffcs.zhsq.reportFocus.reportFeedback;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 上报信息反馈
 * 				  相关涉及表：T_EVENT_REPORT_SEND、T_EVENT_REPORT_FEEDBACK
 * @ClassName:   IReportFeedbackService   
 * @author:      张联松(zhangls)
 * @date:        2020年11月4日 下午3:15:42
 */
public interface IReportFeedbackService {

	/**
	 * 新增和编辑反馈信息
	 * @param reportFeedback
     *     fbUUId:反馈信息UUID String 选填,有值为修改
     *     seUUId:下达信息UUID String 新增时必填，编辑不做修改
     *     seContent:下达内容 String 新增时必填，编辑为空不做修改
     *     seStatus:下达状态，01 已下达；02 已退回；String 选填，默认为已下达，编辑不做修改
     *     reDeadline:接收时限 String 新增时必填，编辑不做修改
     *     fbDeadline:反馈时限 String 新增时必填，编辑不做修改
     *     fbOrgId:反馈组织Id Long 新增时必填，编辑不做修改
     *     fbOrgName:反馈组织名称 String 选填，编辑不做修改
     *     fbUserId:反馈用户Id Long 新增时必填，编辑不做修改
     *     fbUserName:反馈用户姓名 String 选填，编辑不做修改
	 * @param userInfo 登录用户
	 * @return 非空为fbUUId表示新增或编辑成功,为空表示新增或编辑失败
	 */
	String saveOrUpdateReportFeedback(Map<String, Object> reportFeedback, UserInfo userInfo) throws Exception;

    /**
     * 根据bizSign,bizType,dataSign,dataSource来查询seuuid
     * @param params bizSign,bizType,dataSign,dataSource
     * @return seuuid
     */
    String findSeUUIdByParam(Map<String, Object> params) throws Exception;

	/**
	 * 新增和编辑下达信息和反馈信息
	 * @param reportMap
     *  #########################################################################################################
     *      seUUId:下达信息UUID String 可选，非空为编辑操作，否则为新增
     *      regionCode：所属区域 String 编辑为可选，新增为必填
     *      bizType：业务类型，01 事件；02 两违；03 房屋安全隐患；04 企业安全隐患；05 疫情防控；06 流域水质 String 编辑为可选，新增为必填
     *      bizSign：业务标识，bizType为01时，表示事件id；为02时，表示reportUUID；其他类推 String 编辑为可选，新增为必填
     *      dataSource：数据来源,01 南安12345平台 String 编辑为可选，新增为必填
     *      dataSign：数据标识 String 编辑为可选，新增为必填
     *      @see IReportFeedbackService#saveOrUpdateReportSend
     *      fbUUId:反馈信息UUID String 选填,有值为修改
     *      seUUId:下达信息UUID String 新增时必填(参数传进来需与上面的seUUId保持一致，也可不传由上面新增或编辑的seUUId代替)，编辑不做修改
     *      seContent:下达内容 String 新增时必填，编辑为空不做修改
     *      seStatus:下达状态，01 已下达；02 已退回；String 选填，默认为已下达，编辑不做修改
     *      reDeadline:接收时限 String 新增时必填，编辑不做修改
     *      fbDeadline:反馈时限 String 新增时必填，编辑不做修改
     *      fbOrgId:反馈组织Id Long 新增时必填，编辑不做修改
     *      fbOrgName:反馈组织名称 String 选填，编辑不做修改
     *      fbUserId:反馈用户Id Long 新增时必填，编辑不做修改
     *      fbUserName:反馈用户姓名 String 选填，编辑不做修改
     *      @see IReportFeedbackService#saveOrUpdateReportFeedback
     *  #########################################################################################################
	 * @param userInfo 登录用户
	 * @return 非空(seUUId和多个fbUUId用逗号分隔返回)表示新增或编辑成功,为空表示新增或编辑失败
	 * @throws Exception
	 */
    String saveOrUpdateReport(Map<String, Object> reportMap, UserInfo userInfo) throws Exception;

	/**
	 * 新增和编辑下达信息
	 * @param reportSend
     *      seUUId:下达信息UUID String 可选，非空为编辑操作，否则为新增
     *      regionCode：所属区域 String 编辑为可选，新增为必填
     *      bizType：业务类型，01 事件；02 两违；03 房屋安全隐患；04 企业安全隐患；05 疫情防控；06 流域水质 String 编辑为可选，新增为必填
     *      bizSign：业务标识，bizType为01时，表示事件id；为02时，表示reportUUID；其他类推 String 编辑为可选，新增为必填
     *      dataSource：数据来源 String 编辑为可选，新增为必填
     *      dataSign：数据标识 String 编辑为可选，新增为必填
	 * @param userInfo 登录用户
	 * @return 非空为seUUId表示新增或编辑成功,为空表示新增或编辑失败
	 * @throws Exception
	 */
    String saveOrUpdateReportSend(Map<String, Object> reportSend, UserInfo userInfo) throws Exception;

	/**
	 * 根据seUUid查询下达信息和反馈信息
	 * @param seUUid
	 * @return
	 */
	Map<String,Object> searchReportDataBySeUUId(String seUUid);

	/**
	 * 下达信息分页列表加载
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return 公交线路信息表数据列表
	 */
	EUDGPagination findReportDataPage(int pageNo, int pageSize, Map<String, Object> params);


	/**
	 * 反馈信息分页列表加载
	 * @param pageNo
	 * @param pageSize
	 * @param params
     *
	 * @return
	 */
	EUDGPagination findReportFeedbackDataPage(int pageNo, int pageSize, Map<String, Object> params);


    /**
     * 根据事件或两违的id和业务类型获取反馈信息列表
     * @param bizSign 事件或两违等的id
     * @param bizType 业务类型，01 事件；02 两违；03 房屋安全隐患；04 企业安全隐患；05 疫情防控；06 流域水质
     * @param extraParam 额外参数
     * @return 反馈信息无分页列表数据
     */
	List<Map<String,Object>> searchReportFeedbackList(String bizSign, String bizType, Map<String, Object> extraParam);

	/**
	 * 根据fbUUId获取反馈信息
	 * @param fbUUid
	 * @return
	 */
	Map<String,Object> findReportFeedbackByfbUUId(String fbUUid);


    /**
     * 签收状态设置
     * @param fbUUid 反馈信息uuid
     * @param userInfo 登录用户
     * @return
     * @throws Exception
     */
	boolean modifyReStatus(String fbUUid, UserInfo userInfo) throws Exception;

    /**
     * 反馈状态设置
     * @param fbUUId 反馈信息uuid
     * @param fbContent 反馈内容
     * @param userInfo 登录用户
     * @return
     * @throws Exception
     */
	boolean modifyFbStatus(String fbUUId, String fbContent, UserInfo userInfo) throws Exception;

    /**
     * 查询事件关联的反馈信息数量
     * @param bizSign 事件或两违等的id
     * @param bizType 业务类型，01 事件；02 两违；03 房屋安全隐患；04 企业安全隐患；05 疫情防控；06 流域水质
     * @param extraParam 额外参数
     * @return
     */
    Long findReportFeedbackCount(String bizSign, String bizType, Map<String, Object> extraParam);

    /**
     * 新增一条补充信息或催单记录
     * @param extMap
     *      extUUId：主键
     * 		seUUId：下达信息UUID，非空
     * 		extType：补充信息类型：1，补充信息；2，催单记录，非空
     * 		doPerson：补充信息的处理人员，催单记录的办理人员，非空
     * 		doDate：补充信息的处理时间，催单记录的催单时间，非空
     * 		doContent：补充信息的内容，催单记录的催单意见，非空
     * 	    checkValidOnBackEnd：值为1后台对seUUId有效性校验；其他或不传仅非空校验
     * @param userInfo 登录用户
     * @return 非空为extUUID表示新增成功,为空表示新增失败
     * @throws Exception
     */
    String saveReportSendExt(Map<String, Object> extMap, UserInfo userInfo) throws Exception;

    /**
     * 根据seUUid和补充信息类型获取补充信息列表数据
     * @param seUUId
     * @param extType 补充信息类型，1 补充信息；2 催单信息
     * @param extraParam 额外参数
     * @return 补充信息无分页列表数据
     */
    List<Map<String,Object>> searchReportSendExtList(String seUUId, String extType, Map<String, Object> extraParam);

    /**
     * 查询补充信息数量
     * @param seUUId
     * @param extType 补充信息类型，1 补充信息；2 催单信息
     * @param extraParam 额外参数
     * @return
     */
    Long findReportSendExtCount(String seUUId, String extType, Map<String, Object> extraParam);

    /**
	 * 业务类型枚举值
	 */
	public enum BizType {
		NORMAL_EVENT("01","其他事件"),
		TWO_VIO_PRE_EVENT("02","两违防治"),
        HOUSE_HIDDEN_DANGER("03","房屋安全整治"),
        ENTERPRISE_HIDDEN_DANGER("04","企业安全生产"),
        EPIDEMIC_PREVENTION_CONTROL("05","疫情防控"),
        BASIN_WATER_QUALITY("06","流域水质异常"),
        FOREST_FIRE_PREVENTION("07","森林防灭火"),
        BUSINESS_PROBLEM("08","营商问题"),
        PETITION_PERSON("09","信访人员稳控"),
        MARTYRS_FACILITY("10","烈士纪念设施"),
        ENVIRONMENT_HEAL_TREATMENT("11","环境卫生问题处置"),
        THREE_ONE_TREATMENT("12","三合一整治");

		private String code;
		private String name;

        BizType(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static Map<String,String> toMap(){
            Map<String,String> map = new HashMap<>();
            BizType[] bizTypes = BizType.values();
            for (BizType bizType:bizTypes) {
                map.put(bizType.getCode(),bizType.getName());
            }
            return map;
        }
	}

    /**
     * 下达状态枚举值
     */
	public enum ReportSendStatus{
        ISSUED("01","已下达"),
	    RETURNED("02","已退回");
        private String code;
        private String name;

        ReportSendStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
        public static Map<String,String> toMap(){
            Map<String,String> map = new HashMap<>();
            ReportSendStatus[] reportSendStatuses = ReportSendStatus.values();
            for (ReportSendStatus reportSendStatus:reportSendStatuses) {
                map.put(reportSendStatus.getCode(),reportSendStatus.getName());
            }
            return map;
        }
    }

    /**
     * 接收状态枚举值
     */
    public enum ReportReadStatus{
        NOT_RECEIVED("01","未接收"),
        RECEIVED("02","已接收"),
        TIMEOUT_RECEIVED("03","超时接收");
        private String code;
        private String name;

        ReportReadStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
        public static Map<String,String> toMap(){
            Map<String,String> map = new HashMap<>();
            ReportReadStatus[] reportReadStatuses = ReportReadStatus.values();
            for (ReportReadStatus reportReadStatus:reportReadStatuses) {
                map.put(reportReadStatus.getCode(),reportReadStatus.getName());
            }
            return map;
        }
    }

    /**
     * 反馈状态枚举值
     */
    public enum ReportFeedbackStatus{
        NOT_FEEDBACK("01","未反馈"),
        FEEDBACK("02","已反馈"),
        TIMEOUT_FEEDBACK("03","超时反馈");
        private String code;
        private String name;

        ReportFeedbackStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
        public static Map<String,String> toMap(){
            Map<String,String> map = new HashMap<>();
            ReportFeedbackStatus[] reportFeedbackStatuses = ReportFeedbackStatus.values();
            for (ReportFeedbackStatus reportFeedbackStatus:reportFeedbackStatuses) {
                map.put(reportFeedbackStatus.getCode(),reportFeedbackStatus.getName());
            }
            return map;
        }
    }

    /**
     * 补充信息枚举值
     */
    public enum ReportSendExtType{
        EXT_TYPE("1","补充信息"),
        REMIND_TYPE("2","催单信息");
        private String code;
        private String name;

        ReportSendExtType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
        public static Map<String,String> toMap(){
            Map<String,String> map = new HashMap<>();
            ReportSendExtType[] reportSendExtTypes = ReportSendExtType.values();
            for (ReportSendExtType reportSendExtType:reportSendExtTypes) {
                map.put(reportSendExtType.getCode(),reportSendExtType.getName());
            }
            return map;
        }
    }
}
