package cn.ffcs.zhsq.emergencyResponse.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @author zhanggf
 * 应急预案
 */
@Controller
@RequestMapping(value="/zhsq/emergencyResponse")
public class EmergencyResponseController extends ZZBaseController{
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigureationService;
	
	private final String DEFAULT_TYPES_FOR_LIST = "0216,0217,0218";//列表默认查询的事件类型
	
    /**
     * 应急预案--首页
     * @param session
     * @param map
     * @param trigger 事件类型触发条件
     * @return
     */
    @RequestMapping(value="/listEmergencyResponse")
    public String index(HttpSession session, ModelMap map,
    		@RequestParam(value = "trigger", required=false) String trigger) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
    	
    	if(StringUtils.isNotBlank(trigger)){//获取列表可展示的事件类型
    		String typesForList = funConfigureationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
    		if(StringUtils.isBlank(typesForList)){
    			typesForList = DEFAULT_TYPES_FOR_LIST;
    		}
    		map.addAttribute("typesForList", typesForList);
    	}
    	
    	map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
    	
    	
    	
        return "/zzgl/emergencyResponse/list_emergencyResponse.ftl";
    }
    
    /**
     * 验证输入的密码与配置的密码是否匹配
     * @param session
     * @param map
     * @param password
     * @return 输入密码为空或者二者不匹配时，返回false；否则，返回true
     */
    @ResponseBody
    @RequestMapping(value="/checkPassword")
    public boolean checkPassword(HttpSession session, ModelMap map,
    		@RequestParam(value = "password", required=false) String password) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	
    	String emergencyResponsePassword = funConfigureationService.turnCodeToValue(ConstantValue.EMERGENCY_RESPONSE_PASSWORD, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
    	
    	return (StringUtils.isNotBlank(password) && emergencyResponsePassword.equals(password));
    }
    
    /**
     * 跳转至应急预案人员办理页面
     * @param session
     * @param map
     * @param important 应急预案等级
     * @param eventId 事件ID
     * @return
     */
    @RequestMapping(value="/toEmergencyResponseMsg")
    public String toyjya(HttpSession session, ModelMap map,
    		@RequestParam(value = "important") String important,
    		@RequestParam(value = "eventId") Long eventId) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	EventDisposal event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
    	String importantType = "";
    	String title = "";
		String gridCode = "";
		MixedGridInfo mixGridInfo = mixedGridInfoService.findMixedGridInfoById(event.getGridId(),false);
		if(mixGridInfo != null){
			gridCode = mixGridInfo.getGridCode();
			if(StringUtils.isNotBlank(gridCode) && gridCode.length()>9){//获取街道的网格编码
				gridCode = gridCode.substring(0, 9);
			}
		}
    	
    	if("1".equals(important)){
    		importantType="一";
    	}else if("2".equals(important)){
    		importantType="二";
    	}else if("3".equals(important)){
    		importantType="三";
    	}
    	
    	title += event.getTypeName()+"--"+importantType+"级应急响应";
    	
    	map.addAttribute("title", title);
    	map.addAttribute("eventId", eventId);
    	map.addAttribute("important", important);
    	map.addAttribute("importantType", importantType);
    	map.addAttribute("event", event);
		map.addAttribute("gridCode", gridCode);
    	
    	return "/zzgl/emergencyResponse/msg_emergencyResponse.ftl";
    }
    
    /**
     * 发送短信
     * @param session
     * @param map
     * @param importantType 应急预案等级中文名称
     * @param details 事件详情
     * @param telephoneAndDuty 需要发送短信的号码及对应的职责描述
     * @param isSendSms 是否发送短信，true不发送，其他未发送
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/sendMessage")
    public String sendMessage(HttpSession session, ModelMap map,
    		@RequestParam(value = "importantType") String importantType,
    		@RequestParam(value = "details") String details,
    		@RequestParam(value = "telephoneAndDuty") String telephoneAndDuty,
    		@RequestParam(value = "isSendSms", required=false) String isSendSms) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	String messageString = "";
    	
    	//获取应急预案短信模板
    	String emergencyResponseNote = funConfigureationService.turnCodeToValue(ConstantValue.EMERGENCY_RESPONSE_NOTE, ConstantValue.EMERGENCY_RESPONSE_NOTE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
    	
    	if(StringUtils.isNotBlank(emergencyResponseNote)){
    		if(emergencyResponseNote.contains("@content")){//替换事件内容
    			emergencyResponseNote = emergencyResponseNote.replace("@content", details).trim();
    		}
    		if(emergencyResponseNote.contains("@importantType")){//替换相应级别
    			emergencyResponseNote = emergencyResponseNote.replace("@importantType", importantType).trim();
    		}
	    	if(StringUtils.isNotBlank(telephoneAndDuty)){
	    		String[] telephoneAndDutyArray = telephoneAndDuty.split(";");
	    		for(int index = 0, len = telephoneAndDutyArray.length; index < len; index++){
	    			if(StringUtils.isNotBlank(telephoneAndDutyArray[index])){
	    				String[] telAndDuty = telephoneAndDutyArray[index].split(",");
	    				String telephone = telAndDuty[0];
	    				String duty = telAndDuty[1];
	    				
	    				if(StringUtils.isNotBlank(telephone) && StringUtils.isNotBlank(duty)){
	    					boolean isSendSmsFlag = Boolean.valueOf(isSendSms);
	    					
	    					if(emergencyResponseNote.contains("@duty")){//提供职责描述
	    						messageString = emergencyResponseNote.replace("@duty", duty);
	    					}
	    					
	    					if(!isSendSmsFlag && StringUtils.isNotBlank(messageString)){
	    						eventDisposalWorkflowService.sendSms(null, telephone, messageString, userInfo);
	    					}
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	return null;
    }
}
