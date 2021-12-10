package cn.ffcs.zhsq.warningScheme.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.intermediateData.publicDemand.service.PublicDemandService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import cn.ffcs.zhsq.warningScheme.service.IWarningSchemeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.system.publicUtil.StringUtils;

/**
 * @Description: 预警方案模块控制器
 * @Author: youwj
 * @Date: 05-28 15:32:03
 * @Copyright: 2019 福富软件
 */
@Controller("warningSchemeController")
@RequestMapping("/zhsq/warningScheme")
public class WarningSchemeController extends ZZBaseController {
	
	//预警方案服务
    @Autowired
    private IWarningSchemeService warningSchemeService;
    
    //字典服务
    @Autowired
	private IBaseDictionaryService dictionaryService;

	/**
	 * 跳转列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		return "/zzgl/warningScheme/list_warning_scheme.ftl";
	}
	
	/**
     * 获取列表记录
     * @param session
     * @param page		页码
     * @param rows		每页记录数
     * @param params
     * 			bizType	业务模块编码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listData",method = RequestMethod.POST)
    public EUDGPagination listData(HttpSession session,
                                   @RequestParam(value="page")int page,
                                   @RequestParam(value = "rows")int rows,
                                   @RequestParam(value = "bizType")String bizType,
                                   @RequestParam(value = "schemeName")String schemeName,
                                   @RequestParam Map<String,Object> params){
    	
        if(StringUtils.isBlank(bizType)){
        	params.put("bizType", "01");
        }else{
        	params.put("bizType", bizType);
        }
        
        if(null!=schemeName&&schemeName.length()>0){
        	params.put("schemeName", schemeName);
        }
        

        EUDGPagination schemePage = warningSchemeService.findSchemePagination(page, rows, params);

        return schemePage;
    }
    
    /**
     * 跳转新增/编辑预警信息页面
     * @param session
     * @param schemeId		预警信息id
     * @param map
     * @return
     */
    @RequestMapping("/toAddOrEdit")
    public String toAddOrEdit(HttpSession session, 
    		@RequestParam(value = "schemeId", required = false) Long schemeId,
    		ModelMap map){
    	
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	
        List<BaseDataDict> dataDictListByDictCode = dictionaryService.getDataDictTree("A001093271",userInfo.getOrgCode());

        Map<String,Long> codeMap=new HashMap<String,Long>();
		for (BaseDataDict var : dataDictListByDictCode) {
			codeMap.put(var.getDictGeneralCode(), var.getDictOrderby());
		}
        
    	if(null!=schemeId&&schemeId>0){//有传入Id,跳转编辑页面

    		SchemeMatch schemeMatch=warningSchemeService.findSchemeById(schemeId);
    		List<SchemeKeyword> schemeKeywordList=warningSchemeService.findSchemeKeywordBySchemeId(schemeId);
    		
    		map.addAttribute("schemeMatch", schemeMatch);
    		
    		List<Map<String, Object>> keywordList = new ArrayList<Map<String, Object>>();
            Map<String, Object> value = new HashMap<>();
            
            List<Map<String, Object>> keyidList = new ArrayList<Map<String, Object>>();
            Map<String, Object> value2 = new HashMap<>();
            
            for(int i=0,l=schemeKeywordList.size();i<l;i++){
            	value.put("value_"+schemeKeywordList.get(i).getCode(), schemeKeywordList.get(i).getKeyword());
            	value2.put("keyid_"+schemeKeywordList.get(i).getCode(), schemeKeywordList.get(i).getSchemeKeywordId());
            }

            keywordList.add(value);
            keyidList.add(value2);
    		map.addAttribute("keywordList", keywordList);
    		map.addAttribute("keyidList", keyidList);

    	}else{
    		map.addAttribute("scheme", new SchemeMatch());
    		map.addAttribute("keywordList", new ArrayList<Map<String, Object>>());
    	}

    	map.addAttribute("dicts", dataDictListByDictCode);
    	return "/zzgl/warningScheme/add_warning_scheme.ftl";
    	
    	
    }
    
    /**
     * 跳转详情页面
     * @param schemeId	预警id
     * @param map
     * @return
     */
    @RequestMapping("/toDetail")
	public String toDetail(HttpSession session, Long schemeId, ModelMap map) {
    	
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		Map<String, Object> schemeDetailMap = new HashMap<String, Object>();
		StringBuffer msgWrong = new StringBuffer("");

        List<BaseDataDict> dataDictListByDictCode = dictionaryService.getDataDictTree("A001093271",userInfo.getOrgCode());
        Map<String,Long> codeMap=new HashMap<String,Long>();
		for (BaseDataDict var : dataDictListByDictCode) {
			codeMap.put(var.getDictGeneralCode(), var.getDictOrderby());
		}
		
		if (schemeId > 0) {

        	
        	try {
        		SchemeMatch schemeMatch=warningSchemeService.findSchemeById(schemeId);
        		List<SchemeKeyword> schemeKeywordList=warningSchemeService.findSchemeKeywordBySchemeId(schemeId);
        		
        		map.addAttribute("schemeMatch", schemeMatch);
        		
        		List<Map<String, Object>> keywordList = new ArrayList<Map<String, Object>>();
                Map<String, Object> value = new HashMap<>();
                for(int i=0,l=schemeKeywordList.size();i<l;i++){
                	value.put("value_"+schemeKeywordList.get(i).getCode(), schemeKeywordList.get(i).getKeyword());
                }
                keywordList.add(value);
        		map.addAttribute("keywordList", keywordList);
			} catch (Exception e) {
				msgWrong.append(e.getMessage());
				e.printStackTrace();
			}
        }
        
       
        
        if(msgWrong.length() > 0) {
        	map.addAttribute("msgWrong", msgWrong.toString());
        	schemeDetailMap.put("schemeMatch", new SchemeMatch());
    		schemeDetailMap.put("keywordList", new ArrayList<Map<String, Object>>());
        } else {
        	map.addAllAttributes(schemeDetailMap);
        }
    	map.addAttribute("dicts", dataDictListByDictCode);

        return "/zzgl/warningScheme/detail_warning_scheme.ftl";
    }
    
    /**
     * 保存新增/编辑预警信息
     * @param session
     * @param schemeId	预警方案id
     * @param schemeMatch
     * @param schemeKeyword
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdatScheme")
    public Map<String,Object> saveOrUpdatScheme(HttpSession session,
    		HttpServletRequest request, Long schemeId,
                                       SchemeMatch schemeMatch,
                                       String updateTimeStr,
                                       @RequestParam Map<String,Object> param,
                                       ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        boolean result = false;
        ResultObj resultObj = null;
        Map<String,Object> resultMap = new HashMap<>();
        Long saveSchemeId =0L;
        
        List<BaseDataDict> dataDictListByDictCode = dictionaryService.getDataDictTree("A001093271",userInfo.getOrgCode());

        if (schemeId != null && schemeId > 0) {//传入Id，执行修改
        	
        	//先获取当前更新时间判断此次更新是否是最新操作
        	SchemeMatch match=warningSchemeService.findSchemeById(schemeId);
        	try {
				if(match.getUpdateTime().getTime()==DateUtils.convertStringToDate(updateTimeStr, "yyyy/MM/dd HH:mm:ss").getTime()){
					//先更新主表信息
					schemeMatch.setUpdateTime(new Date());
					schemeMatch.setUpdator(userInfo.getUserId());
					warningSchemeService.updateScheme(schemeMatch);
					
					//再更新子表的信息
					//预留做批量操作
					List<SchemeKeyword> oldList=new ArrayList<SchemeKeyword>();
					List<SchemeKeyword> newList=new ArrayList<SchemeKeyword>();
					
					List<String> findAllCode = warningSchemeService.findAllCode();
					Map<String,String> codeMap=new HashMap<String,String>();
					for (String string : findAllCode) {
						codeMap.put(string, string);
					}
					
				    for (BaseDataDict dic : dataDictListByDictCode) {
						
						SchemeKeyword schemeKeyword=new SchemeKeyword();
						schemeKeyword.setSchemeId(schemeId);
						String thisKeyword=request.getParameter("code_"+dic.getDictGeneralCode());
						Long thisKeywordId=Long.valueOf(request.getParameter("keyid_"+dic.getDictGeneralCode()));
						schemeKeyword.setKeyword(thisKeyword);
						schemeKeyword.setSchemeKeywordId(thisKeywordId);
						schemeKeyword.setCode(dic.getDictGeneralCode());
						
						//判断该dict_general_code在库中是否存在 存在则更新，否则新增
						if(null!=codeMap.get(dic.getDictGeneralCode())){
							oldList.add(schemeKeyword);
							//warningSchemeService.updateSchemeKeyword(schemeKeyword);
						}else{
							newList.add(schemeKeyword);
							//warningSchemeService.saveSchemeKeyword(schemeKeyword);
						}
					}
				    
				    if(null!=oldList&&oldList.size()>0){
				    	warningSchemeService.updateSchemeKeyword(oldList);
					}
				    
				    if(null!=newList&&newList.size()>0){
				    	warningSchemeService.saveSchemeKeyword(newList);
				    }
					
				}else{
					throw new Exception("当前更新操作不是最新的操作，请确认后继续更新");
				}
				result = true;
                resultObj = Msg.ADD.getResult(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultObj = Msg.ADD.getResult(e);
			}
           
        } else {//没传Id执行新增
            try {
            	//先执行主表的新增
            	//补全除了name,remark,bizType之外的字段
            	schemeMatch.setIsValid("1");//设置记录状态
            	schemeMatch.setStatus("0");//设置业务状态
            	schemeMatch.setCreator(userInfo.getUserId());
            	schemeMatch.setCreateTime(new Date());
            	schemeMatch.setUpdator(userInfo.getUserId());
            	schemeMatch.setUpdateTime(new Date());
            	
            	saveSchemeId = warningSchemeService.saveScheme(schemeMatch);
            	
            	//在执行字表的新增
            	List<SchemeKeyword> list=new ArrayList<SchemeKeyword>();
            	
            	for (BaseDataDict dic : dataDictListByDictCode) {
					
            		SchemeKeyword schemeKeyword=new SchemeKeyword();
            		schemeKeyword.setSchemeId(saveSchemeId);
            		String thisKeyword=request.getParameter("code_"+dic.getDictGeneralCode());
            		schemeKeyword.setKeyword(thisKeyword);
            		schemeKeyword.setCode(dic.getDictGeneralCode());
            		list.add(schemeKeyword);
            		//warningSchemeService.saveSchemeKeyword(schemeKeyword);
				}
            	
            	warningSchemeService.saveSchemeKeyword(list);
            	
            	result = saveSchemeId > 0;
                resultObj = Msg.ADD.getResult(result);
            } catch (Exception e) {
                e.printStackTrace();
                resultObj = Msg.ADD.getResult(e);
            }

        }

        resultMap.put("result",result);
        resultMap.put("tipMsg",resultObj.getTipMsg());
        resultMap.put("schemeId",saveSchemeId);

        return resultMap;
    }
    
    /**
     * 删除预警信息
     * @param session
     * @param schemeId	预警方案id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deletaScheme")
    public Map<String,Object> deletaScheme(HttpSession session,
    		HttpServletRequest request, SchemeMatch schemeMatch,
                                       @RequestParam Map<String,Object> param,
                                       ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        boolean result = false;
        ResultObj resultObj = null;
        Map<String,Object> resultMap = new HashMap<>();
        
        try {
        	schemeMatch.setIsValid("0");
        	schemeMatch.setUpdator(userInfo.getUserId());
        	schemeMatch.setUpdateTime(new Date());
        	Long updateScheme = warningSchemeService.updateScheme(schemeMatch);
        	
        	result = updateScheme>0;
            resultObj = Msg.ADD.getResult(result);
            
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = Msg.ADD.getResult(e);
		}
        
       

        resultMap.put("result",result);
        resultMap.put("tipMsg",resultObj.getTipMsg());
        resultMap.put("schemeId",schemeMatch.getSchemeId());

        return resultMap;
    }
    
    
    /**
     * 预警生效失效
     * @param session
     * @param schemeId	预警方案id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/SchemeEffect")
    public Map<String,Object> SchemeEffect(HttpSession session,
    		HttpServletRequest request, SchemeMatch schemeMatch,
                                       @RequestParam Map<String,Object> param,
                                       ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        boolean result = false;
        ResultObj resultObj = null;
        Map<String,Object> resultMap = new HashMap<>();
        
        try {
        		
			schemeMatch.setUpdator(userInfo.getUserId());
			schemeMatch.setUpdateTime(new Date());
			Long updateScheme = warningSchemeService.updateScheme(schemeMatch);

			result = updateScheme > 0;
			resultObj = Msg.ADD.getResult(result);
        	
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = Msg.ADD.getResult(e);
		}
        
       

        resultMap.put("result",result);
        resultMap.put("tipMsg",resultObj.getTipMsg());
        resultMap.put("schemeId",schemeMatch.getSchemeId());

        return resultMap;
    }
    
    /**
     * 判断是否已经存在生效的预警方案
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findSchemeEffect")
	public Map<String, Object> findSchemeEffect(HttpSession session, @RequestParam Map<String, Object> param,
			ModelMap map) {
		boolean result = false;
		String resultStr="0";
		Map<String, Object> resultMap = new HashMap<>();

		List<SchemeMatch> list = warningSchemeService.findSchemeEffect();
		if (null != list && list.size() > 0) {
			result=true;
			resultStr="1";
		}

		resultMap.put("result", result);
		resultMap.put("resultStr", resultStr);

        return resultMap;
    }
    
    

}