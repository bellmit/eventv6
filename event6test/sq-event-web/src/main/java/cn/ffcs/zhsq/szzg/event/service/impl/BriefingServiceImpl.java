package cn.ffcs.zhsq.szzg.event.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.szzg.event.BriefingMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IBriefingService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.NumberFormat;

@Service(value="briefingService")
public class BriefingServiceImpl implements IBriefingService {

    @Autowired
    private BriefingMapper briefingMapper;

    
    @Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	

    @Override
    public List<Map<String, Object>> findById(Map<String, Object> params) {
        return briefingMapper.findById(params);
    }

    @Override
    public List<Map<String, Object>> getBriefingList(Map<String, Object> params) throws Exception {
    	Integer pageNo = 1;
    	if(CommonFunctions.isNotBlank(params, "page")) {
    		pageNo=Integer.parseInt(params.get("page").toString());
    		pageNo = pageNo < 1 ? 1 : pageNo;
    	}
        Integer pageSize =20;
        if(CommonFunctions.isNotBlank(params, "rows")) {
        	pageSize=Integer.parseInt(params.get("rows").toString());
        	pageSize = pageSize < 1 ? 1 : pageSize;
    	}
        RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
    	List<Map<String, Object>> briefingList = briefingMapper.getBriefingList(params, rowBounds);
    	for (int i = 0, j = briefingList.size(); i < j; i++) {
            Map<String, Object> map = briefingList.get(i);
            String datestr = DateUtils.formatDate(map.get("DATE_").toString(), "yyyy-MM");
            map.put("DATE_STR", datestr);
        }
    	return briefingList;
    }
    public EUDGPagination getList(Map<String, Object> params) throws Exception {
        if(CommonFunctions.isBlank(params,"isValid")){
            params.put("isValid",1);
        }
        
        Integer count = briefingMapper.getBriefingCount(params);
        List<Map<String, Object>> briefingList = null;
        if(count>0) {
        	briefingList = getBriefingList(params);
        }else {
        	briefingList = new ArrayList<Map<String,Object>>();
        }
        return new EUDGPagination(count,briefingList);
    }

    @Override
    public Integer update(Map<String, Object> params) {
        return briefingMapper.update(params);
    }

    @Override
    public Integer getBriefingCount(Map<String, Object> params) {
    	if(CommonFunctions.isBlank(params,"isValid")){
            params.put("isValid",1);
        }
        return briefingMapper.getBriefingCount(params);
    }

    @Override
    public List<Map<String, Object>> getEditMessageList(Map<String, Object> params) {
        return briefingMapper.getEditMessageList(params);
    }

    @Override
    public Integer deleteEditMessage(Integer bizId) {
        return briefingMapper.deleteEditMessage(bizId);
    }

    @Override
    public Integer addEditMessage(Map<String, Object> params) {
        return briefingMapper.addEditMessage(params);
    }
   
    /**
	     * 
	????????????01
	????????????02
	????????????03
	????????????04
	????????????05
	????????????06
	????????????08
	
	????????????07
     */
    @Override
    public Map<String, Object> queryEventCount(Map<String, Object> params) {
        Map<String, Object> res = new HashMap<String,Object>();
        List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();//????????????
        String thisMonth = params.get("thisMonth").toString();
        res.put("month",Integer.parseInt(thisMonth.substring(4)));
        res.put("year",thisMonth.substring(0,4));
        boolean isCity = false;
        if (params.get("infoOrgCode").toString().length()==2){
            res.put("gridLevel","???");
        }else {
            res.put("gridLevel","???");
            res.put("gridChildTitle","??????");
            isCity = true;
        }
        Map<String,Object> maMap = new HashMap<>();//??????map
  	  	Map<String,Object> msMap = new HashMap<>();//??????map
  	  	String[] maArr = "07,DM".split(",");//??????????????????
  	  	for (String p : maArr) {maMap.put(p, "1"); }
  	  
  	  	String[] msArr = "01,02,03,04,05,06,08,99".split(",");//??????????????????
  	  	for (String p : msArr) {msMap.put(p, "1"); }
  	  	
        List<BaseDataDict> typeBDD = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, 
        		params.get("infoOrgCode").toString());
        for (int i=0,l=typeBDD.size();i<l;i++) {
        	if(typeBDD.get(i).getDictGeneralCode() == null||
        			!(maMap.containsKey(typeBDD.get(i).getDictGeneralCode().substring(0, 2)) || 
        					msMap.containsKey(typeBDD.get(i).getDictGeneralCode().substring(0, 2)))) {
        		typeBDD.remove(i);i--;l--;
			}
		}
        BaseDataDict dm = new BaseDataDict();
        dm.setDictName("????????????");
        dm.setDictGeneralCode("DM");
        BaseDataDict ms99 = new BaseDataDict();
        ms99.setDictName("????????????");
        ms99.setDictGeneralCode("99");
		typeBDD.add(dm);
		typeBDD.add(ms99);
        //??????  ?????? ??????
        returnList =   briefingMapper.queryEventCountByType(params); //??????/???????????? ???????????????
        Map<String, Object> map1=  handleOverallSituation(returnList, typeBDD,maMap,msMap);
        res.putAll(map1);
        List<Map<String, Object>> listMapSmallType = briefingMapper.queryEventCountBySmallType(params); //?????? ?????????????????????
        params.put("orgCode", params.get("infoOrgCode").toString());
        params.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
        List<BaseDataDict> typeBDDAll = baseDictionaryService.findDataDictListByCodes(params);//?????????????????? ??????????????????-??????
        params.put("dictPcode", "B798");
        List<BaseDataDict> typeDM2 = baseDictionaryService.findDataDictListByCodes(params);
        for (BaseDataDict bd : typeDM2) {
			bd.setDictGeneralCode("DM"+bd.getDictGeneralCode());
			typeBDDAll.add(bd); 
		}
        maMap.put("total", res.get("peopleSafeEvent").toString());
        msMap.put("total", res.get("peopleLifeEvent").toString());
        Map<String, Object> map12 =  handleTypeAnalysis(listMapSmallType,typeBDDAll,maMap,msMap);
        res.putAll(map12);
        
        //????????????????????????????????????
        List<Map<String, Object>> gridAndCMC = briefingMapper.queryEventByGrid(params);
        //List<Map<String, Object>> volunteerAndDuty = briefingMapper.queryEventByVolunteer(params);
        //endTime = System.currentTimeMillis(); time +="<br/>queryEventByVolunteer:"+ (endTime - startTime);startTime = endTime;
        res.putAll(handleTable3Province(gridAndCMC,null,res.get("gridLevel").toString()));
        //???????????????
        //if ("???".equals(res.get("gridLevel"))){
            //?????????????????????
            List<Map<String, Object>> cityEventListMap = briefingMapper.queryEventByCity(params);
            List<Map<String, Object>> thisCity = null;
            if(isCity) {
            	params.put("thisCity", "isCity");
            	thisCity= briefingMapper.queryEventByCity(params);
            }
            res.putAll(handleCityEventListMap(cityEventListMap,thisCity));
            //?????????????????????????????????
            List<Map<String, Object>> list1Map = briefingMapper.queryEventByCMDGrid(params);
            List<Map<String, Object>> list2Map = briefingMapper.queryEventByCMDGridType(params);
            for (int i=0,l=list2Map.size();i<l;i++) {
            	if(list2Map.get(i).get("DICT_GENERAL_CODE") == null||
            			!(maMap.containsKey(list2Map.get(i).get("DICT_GENERAL_CODE").toString()) || msMap.containsKey(list2Map.get(i).get("DICT_GENERAL_CODE").toString()))) {
					list2Map.remove(i);i--;l--;
				}
			}
            res.putAll(handleCMDEventListMap(list1Map,list2Map,"cmd",typeBDD));
            //????????????????????????
            
            List<Map<String, Object>> gridManlist1Map = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> gridManlist2Map = new ArrayList<Map<String,Object>>();
            gridManlist1Map.addAll(list1Map);
            gridManlist2Map.addAll(list2Map);
            res.putAll(handleCMDEventListMap(gridManlist1Map,gridManlist2Map,"gridMan",typeBDD));
            //????????????????????????            
            List<Map<String, Object>> volunteerlist1Map = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> volunteerlist2Map = new ArrayList<Map<String,Object>>();
            volunteerlist1Map.addAll(list1Map);
            volunteerlist2Map.addAll(list2Map);
            res.putAll(handleCMDEventListMap(volunteerlist1Map,volunteerlist2Map,"volunteer",typeBDD));
            //?????????????????????????????????
            List<Map<String, Object>> dutyCenterlist1Map = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> dutyCenterlist2Map = new ArrayList<Map<String,Object>>();
            dutyCenterlist1Map.addAll(list1Map);
            dutyCenterlist2Map.addAll(list2Map);
            res.putAll(handleCMDEventListMap(dutyCenterlist1Map,dutyCenterlist2Map,"dutyCenter",typeBDD));
       // }
        //??????????????????
        List<Map<String, Object>> countyEventListMap = briefingMapper.queryEventByCounty(params);
        res.putAll(handleCountyEventListMap(countyEventListMap));
        //????????????
        List<Map<String, Object>> countyOverdueEventListMap = briefingMapper.queryEventByCountyOverdue(params);
        res.putAll(handleCountyOverdueEvent(countyOverdueEventListMap));
        res.put("cityValidList", briefingMapper.queryCityValidRate(params));
        return res;
    }
    
    public Map<String,Object> handleCMDEventListMap(List<Map<String,Object>> gridList,List<Map<String,Object>> typeList,
    		String flag, List<BaseDataDict> typeBDD){
    	 List<Map<String, Object>> cmdEventListMap = new ArrayList<Map<String,Object>>();
         List<Map<String, Object>> cmdEventList2Map = new ArrayList<Map<String,Object>>();
        NumberFormat nf = NumberFormat.getInstance();
        String reportKey = flag+"ReportCounts";
        String allKey = flag+"AllCounts";
        String disposalKey = flag+"DisposalCounts";
        Map<String,Object> dataMap = new HashMap<>();
        Integer cmdTotalEvent =0 ;
        Integer allCmdTotalEvent =0 ;
        Integer cmdTotalDisposal=0 ;
        Integer cmd2TotalEvent=0;
        //?????????????????????
        for (int i = 0,l=gridList.size(); i < l; i++) {
        	 Map<String, Object> eventMap = new HashMap<String, Object>();
            cmdTotalEvent+= Integer.parseInt(gridList.get(i).get(reportKey).toString());
            cmdTotalDisposal+= Integer.parseInt(gridList.get(i).get(disposalKey).toString());
            allCmdTotalEvent+= Integer.parseInt(gridList.get(i).get(allKey).toString());
            
            eventMap.put("INFO_ORG_CODE",gridList.get(i).get("INFO_ORG_CODE").toString());
            eventMap.put("gridName",gridList.get(i).get("gridName").toString());
            eventMap.put("reportCounts",gridList.get(i).get(reportKey).toString());
            eventMap.put("allCounts",gridList.get(i).get(allKey).toString());
            eventMap.put("disposalCounts",gridList.get(i).get(disposalKey).toString());
            cmdEventListMap.add(eventMap);
        }
        
        Map<String,Object> typeMap = new HashMap<>();
        //????????????
        for (int i = 0,l=typeList.size();i < l; i++) {
        	if(typeList.get(i).get("DICT_GENERAL_CODE")==null) {
        		typeList.remove(i);
        		l -=1;
        		i--;
        		continue;
        	}
        	Map<String, Object> eventMap = new HashMap<String, Object>();
        	typeMap.put(typeList.get(i).get("DICT_GENERAL_CODE").toString(),"1");
        	cmd2TotalEvent+= Integer.parseInt(typeList.get(i).get(reportKey).toString());
        	
        	eventMap.put("DICT_GENERAL_CODE",typeList.get(i).get("DICT_GENERAL_CODE"));
        	eventMap.put("reportCounts",typeList.get(i).get(reportKey));
        	eventMap.put("allCounts",typeList.get(i).get(allKey));
        	eventMap.put("disposalCounts",typeList.get(i).get(disposalKey));
        	cmdEventList2Map.add(eventMap);
        }
        Collections.sort(cmdEventListMap, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Integer name1 = Integer.valueOf(o1.get("reportCounts").toString());
				Integer name2 = Integer.valueOf(o2.get("reportCounts").toString());
				int i= name2.compareTo(name1);
				if( i == 0) {
					return Integer.valueOf(o1.get("INFO_ORG_CODE").toString()) - Integer.valueOf(o2.get("INFO_ORG_CODE").toString());
				}
				return i;		
			}
		});
        Collections.sort(cmdEventList2Map, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Integer name1 = Integer.valueOf(o1.get("reportCounts").toString());
				Integer name2 = Integer.valueOf(o2.get("reportCounts").toString());
				int i= name2.compareTo(name1);
				if( i == 0) {
					return 0;
				}
				return i;
			}
		});
        
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        //???????????????
        for (int i = 0,l=cmdEventListMap.size(); i < l; i++) {
            Map<String, Object> eventMap = cmdEventListMap.get(i);
            eventMap.put("perCapita",formatPercent2(eventMap.get("reportCounts"),eventMap.get("allCounts"),nf));
            eventMap.put("rate",eventMap.get("perCapita")+"%");
            list.add(eventMap);
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                 if (Double.parseDouble(o1.get("perCapita").toString())>Double.parseDouble(o2.get("perCapita").toString())){
                     return -1;
                 }else if ("0".equals(o2.get("perCapita").toString())) {
                     return 0;
                 }else {
                     return 1;
                 }
            }
        });
        
        for (BaseDataDict b : typeBDD) {
			if(typeMap.get(b.getDictGeneralCode()) == null) {
				Map<String, Object> eventMap = new HashMap<String, Object>();
				eventMap.put("DICT_GENERAL_CODE", b.getDictGeneralCode());
				eventMap.put("reportCounts", "0");
				eventMap.put("disposalCounts", "0");
				cmdEventList2Map.add(eventMap);
			}
			typeMap.put(b.getDictGeneralCode(),b.getDictName());
		}
        //???????????????
        for (int i = 0,l= cmdEventList2Map.size(); i < l ; i++) {
            Map<String, Object> eventMap = cmdEventList2Map.get(i);
            eventMap.put("eventType",typeMap.get(eventMap.get("DICT_GENERAL_CODE").toString()).toString());
            eventMap.put("rate",formatPercent(eventMap.get("reportCounts"),cmd2TotalEvent,nf));
        }
        if ("cmd".equals(flag)){
            dataMap.put("cmdTotalEvent",cmdTotalEvent);
            dataMap.put("allCmdTotalEvent",allCmdTotalEvent);
            dataMap.put("cmdTotalDisposal",cmdTotalDisposal);
            dataMap.put("cmdList1",list);//?????????????????????
            dataMap.put("cmdList11",cmdEventListMap);//loadImg6
            dataMap.put("cmdList2",cmdEventList2Map);//????????????????????????????????????????????????????????????
        }else if ("gridMan".equals(flag)){
            dataMap.put("gridManTotalEvent",cmdTotalEvent);
            dataMap.put("allGridManTotalEvent",allCmdTotalEvent);
            dataMap.put("gridManTotalDisposal",cmdTotalDisposal);
            dataMap.put("gridManList1",list);
            dataMap.put("gridManList11",cmdEventListMap);//loadImg8 
            dataMap.put("gridManList2",cmdEventList2Map);
        }else if ("volunteer".equals(flag)){
            dataMap.put("volunteerTotalEvent",cmdTotalEvent);
            dataMap.put("allVolunteerTotalEvent",allCmdTotalEvent);
            dataMap.put("volunteerTotalDisposal",cmdTotalDisposal);
            dataMap.put("volunteerList1",list);
            dataMap.put("volunteerList11",cmdEventListMap);
            dataMap.put("volunteerList2",cmdEventList2Map);
        }else if ("dutyCenter".equals(flag)){
            dataMap.put("dutyCenterTotalEvent",cmdTotalEvent);
            dataMap.put("allDutyCenterTotalEvent",allCmdTotalEvent);
            dataMap.put("dutyCenterTotalDisposal",cmdTotalDisposal);
            dataMap.put("dutyCenterList1",list);
            dataMap.put("dutyCenterList11",cmdEventListMap);
            dataMap.put("dutyCenterList2",cmdEventList2Map);
        }
        return dataMap;
    }
    
    //???????????????????????????????????? ?????????????????????
    public  Map<String,Object> getTypeNameByCode(Map<String,BaseDataDict> typeCode,Map<Long,BaseDataDict> typePid,String code){
    	Map<String,Object> dataMap = new HashMap<>();
    	BaseDataDict bdd = typeCode.get(code);
    	dataMap.put("smallType", bdd.getDictName());
    	String bigTypeName = "";
    	Long pid = bdd.getDictPid();
    	while(typePid.get(pid) != null) {
    		BaseDataDict bdd1 = typePid.get(pid);
    		bigTypeName = typePid.get(bdd1.getDictId()).getDictName();
    		pid = typePid.get(bdd1.getDictId()).getDictPid();
    	}
    	dataMap.put("bigType", bigTypeName);
    	return dataMap;
    }
    //????????????????????????????????????
    private Map<String,Object> handleTypeAnalysis(List<Map<String, Object>> listMapSmallType,
			List<BaseDataDict> typeBDD, Map<String, Object> maMap, Map<String, Object> msMap) {
    	Map<String,Object> dataMap = new HashMap<>();
    	NumberFormat nf = NumberFormat.getInstance();
    	Map<String,BaseDataDict> typeCode = new HashMap<>();
    	Map<Long,BaseDataDict> typeId = new HashMap<>();
    	Map<Long,String> typePid = new HashMap<>();
    	for (BaseDataDict b : typeBDD) {
			typeCode.put(b.getDictGeneralCode(), b);
			typeId.put(b.getDictId(), b);
			if(typePid.get(b.getDictPid()) == null) {
				typePid.put(b.getDictPid(),"1");
			}
		}
    	if(typeCode.containsKey("BIG_TYPE")) {
    		typeId.remove(typeCode.get("BIG_TYPE").getDictId());
        	typePid.remove(typeCode.get("BIG_TYPE").getDictPid());
        	typeCode.remove("BIG_TYPE");
    	}
    	
    	 List<Map<String,Object>> pSafeListMapSmallType = new ArrayList<Map<String,Object>>();
         List<Map<String,Object>> pLifeListMapSmallType = new ArrayList<Map<String,Object>>();
         Object peopleSafeEvent = maMap.get("total");
         Object peopleLifeEvent = msMap.get("total");
         int showLength = 10;
         int maIndex = 0;
         int msIndex = 0;
         String code = null;
         for (Map<String, Object> map : listMapSmallType) {
        	 if(maIndex>=showLength  && msIndex>=showLength) {
        		 break;
        	 }
        	 Object reportCounts = map.get("reportCounts");
			if(map.get("EVENT_TYPE")!=null && typeCode.get(map.get("EVENT_TYPE").toString())!=null && 
					!"0".equals(map.get("allCounts").toString())) {
				code = map.get("EVENT_TYPE").toString();
				
				if(maMap.get(code.substring(0, 2))!=null && maIndex <showLength) {//????????????
					dataMap.put(code, "1");
					map.put("number",++maIndex);
		            map.put("rate",formatPercent(reportCounts,peopleSafeEvent,nf));
		            map.put("disposalRate",formatPercent(map.get("disposalCounts"),reportCounts,nf));
		            map.put("overdueRate",formatPercent(map.get("overdue"),reportCounts,nf));
		            map.putAll(getTypeNameByCode(typeCode,typeId,code));
					pSafeListMapSmallType.add(map);
				}else if(msMap.get(code.substring(0, 2))!=null &&  msIndex<showLength) {//????????????
					dataMap.put(code, "1");
					map.put("number",++msIndex);
		            map.put("rate",formatPercent(reportCounts,peopleLifeEvent,nf));
		            map.put("disposalRate",formatPercent(map.get("disposalCounts"),reportCounts,nf));
		            map.put("overdueRate",formatPercent(map.get("overdue"),reportCounts,nf));
		            map.putAll(getTypeNameByCode(typeCode,typeId,code));
					pLifeListMapSmallType.add(map);
				}
			}
		} 
       //?????????10??? ???????????? ???????????????????????????
         for (BaseDataDict b : typeBDD) {
 			 if(maIndex>=showLength && msIndex>=showLength){//??????10??? ????????????
 				 break;
 			 }//????????????10??????0
 			if(maIndex<10 && (maMap.get(b.getDictGeneralCode().substring(0, 2))!=null) 
 				&& dataMap.get(b.getDictGeneralCode()) == null //sql??????????????????????????????
 				&& typePid.get(b.getDictId()) == null){	//??????????????????typePid???????????????????????????
 				Map<String, Object> map = getInitMap2(b.getDictGeneralCode(),++maIndex);
 				map.putAll(getTypeNameByCode(typeCode,typeId,b.getDictGeneralCode()));
 				pSafeListMapSmallType.add(map);	
 			}else if(msIndex<10 && (msMap.get(b.getDictGeneralCode().substring(0, 2))!=null) 
 	 				&& dataMap.get(b.getDictGeneralCode()) == null 
 	 				&& typePid.get(b.getDictId()) == null){	
 				Map<String, Object> map = getInitMap2(b.getDictGeneralCode(),++msIndex);
 				map.putAll(getTypeNameByCode(typeCode,typeId,b.getDictGeneralCode()));
 				pLifeListMapSmallType.add(map);	
 	 		}
 		}
        dataMap.clear();
        dataMap.put("tableData1",pSafeListMapSmallType);
        dataMap.put("tableData2",pLifeListMapSmallType);
		return dataMap;
	}
    //?????? ?????? ??????
    public Map<String,Object> getInitMap2(String code,int num){
		Map<String, Object> d = new HashMap<String,Object>();
		d.put("number", num);d.put("DICT_GENERAL_CODE", code);d.put("reportCounts", 0);d.put("allCounts", 0);d.put("disposalCounts", 0);d.put("overdue", 0);
  		 d.put("rate", "0%");d.put("disposalRate", "0%"); d.put("overdueRate", "0%");d.put("peopleSafeTypeDisposalRate", "0%");
  		return d;
	}
	
    //???????????? ???????????? ????????????
	private Map<String,Object> handleOverallSituation(List<Map<String, Object>> returnList,List<BaseDataDict> typeBDD,
			Map<String, Object> maMap,Map<String, Object> msMap) {
		NumberFormat nf = NumberFormat.getInstance();
	  Map<String,Object> dataMap = new HashMap<>();
	  Map<String,Map<String,Object>> maDataMap = new HashMap<>();//??????map
  	  Map<String,Map<String,Object>> msDataMap = new HashMap<>();//??????map
	  Integer thisRTotal = 0;//?????????
	  Integer thisDCounts = 0;
	  Integer lastRTotal = 0;
	  Integer thisATotal = 0;//????????????
	  
	  Integer thisReportTotal = 0;//??????
	  Integer thisAllTotal = 0;//???????????????
	  Integer thisDisposalCounts = 0;
	  Integer lastReportTotal = 0;
	  
	  Integer thisReportTotalMS = 0;//??????
	  Integer thisDisposalCountsMS = 0;
	  Integer thisAllTotalMS = 0;//???????????????
	  Integer lastReportTotalMS = 0;
	  
	  Integer thisReportTotalMA = 0;//??????
	  Integer thisAllTotalMA = 0;//???????????????
	  Integer thisDisposalCountsMA = 0;
	  Integer lastReportTotalMA = 0;
	  String code = null;
	  Integer lastAllCounts = 0;	//????????????
	  Integer lastDisposalCounts = 0;//???????????????
	  for (Map<String, Object> map : returnList) {//??????????????????????????????
		  lastRTotal= Integer.parseInt(map.get("lastReportTotal").toString());	//???????????????????????? ?????????
		  thisRTotal = Integer.parseInt(map.get("thisReportTotal").toString());	//???????????????????????? ?????????
		  thisDCounts = Integer.parseInt(map.get("thisDisposalCounts").toString());	//?????????????????? ?????? ?????????
		  thisATotal = Integer.parseInt(map.get("thisAllCounts").toString());	//?????????????????? ??????
		  lastAllCounts += Integer.parseInt(map.get("lastAllCounts").toString());	//????????????
		  lastDisposalCounts += Integer.parseInt(map.get("lastDisposalCounts").toString());	//???????????????
		  if(map.get("DICT_GENERAL_CODE")!=null) {
				code = map.get("DICT_GENERAL_CODE").toString();
				if(maMap.get(code)!=null	) {//????????????
					 thisReportTotalMA += thisRTotal;	//????????????
					 thisDisposalCountsMA += thisDCounts;	//???????????????
					 thisAllTotalMA += thisATotal;	//?????????
					 lastReportTotalMA += lastRTotal;	//???????????????
					 map.put("peopleSafeTypeRGRate",RGPercent(thisRTotal,lastRTotal,nf));
					 map.put("allPeopleSafeTypeTotal",thisATotal);
			        // map.put("peopleSafeTypeRate",formatPercent(eventMap.get("thisReportTotal"),peopleSafeEvent,nf));
			         map.put("peopleSafeTypeDisposalRate",formatPercent(thisDCounts,thisRTotal,nf));
			         maDataMap.put(code, map);
				}else if(msMap.get(code)!=null){	//????????????
					 thisReportTotalMS += thisRTotal;
					 thisDisposalCountsMS += thisDCounts;
					 lastReportTotalMS += lastRTotal;
					 thisAllTotalMS += thisATotal;	//?????????
					 map.put("peopleLifeTypeRGRate",RGPercent(thisRTotal,lastRTotal,nf));
					 map.put("allPeopleLifeTypeTotal",thisATotal);
//						map.put("peopleLifeTypeRate",formatPercent(eventMap.get("thisReportTotal"),peopleLifeEvent,nf));
					 map.put("peopleLifeTypeDisposalRate",formatPercent(thisDCounts,thisRTotal,nf));
					 msDataMap.put(code, map);
				}
			}
			
		}
	  lastReportTotal = lastReportTotalMS+lastReportTotalMA;//????????????+??????
	  thisReportTotal = thisReportTotalMS + thisReportTotalMA;//????????????+??????
	  thisAllTotal = thisAllTotalMS + thisAllTotalMA;//????????????+??????
	  thisDisposalCounts = thisDisposalCountsMA + thisDisposalCountsMS;//????????????  ??????+??????
	  //??????
      dataMap.put("totalEvent",thisReportTotal);
      dataMap.put("allTotalEvent",thisAllTotal);
      dataMap.put("lastReportTotal",lastReportTotal);
      dataMap.put("totalDisposal",thisDisposalCounts);
      String thisDisposalRate = formatPercent2(thisDisposalCounts,thisReportTotal,nf);//???????????????
      dataMap.put("totalDisposalRate",thisDisposalRate+"%");
      String thisValidRate = formatPercent2(thisReportTotal,thisAllTotal,nf);//???????????????
      dataMap.put("thisValidRate",thisValidRate+"%");//??????????????????
      String lastValidRate = formatPercent2(lastReportTotal,lastAllCounts,nf);//???????????????
      Double validRate =Double.valueOf(thisValidRate) - Double.valueOf(lastValidRate);
      //??????????????????
      dataMap.put("compareLastRate",thisValidRate.equals(lastValidRate)?"??????":((validRate>0?"??????":"??????")+nf.format(validRate).replace("-", "")+"%"));
      //???????????????
      String lastDisposalRate = formatPercent2(lastDisposalCounts,lastReportTotal,nf);
      
      validRate =Double.valueOf(thisDisposalRate) - Double.valueOf(lastDisposalRate);
      dataMap.put("compareLastDisposalRate",thisDisposalRate.equals(lastDisposalRate)?"??????":((validRate>0?"??????":"??????")+nf.format(validRate).replace("-", "")+"%"));
      
      //??????
      dataMap.put("allPeopleLifeEvent",thisAllTotalMS);//?????????
      dataMap.put("peopleLifeEvent",thisReportTotalMS);//???????????????
      dataMap.put("lastPeopleLifeEvent",lastReportTotalMS);//?????????
      dataMap.put("peopleLifeDisposal",thisDisposalCountsMS);//???????????????
      dataMap.put("peopleLifeDisposalRate",formatPercent(thisDisposalCountsMS,thisReportTotalMS,nf));//???????????????
      dataMap.put("peopleLifeRGRate",RGPercent(thisReportTotalMS,lastReportTotalMS,nf));//??????
      dataMap.put("peopleLifeRate",formatPercent(thisReportTotalMS,thisReportTotal,nf));

      //??????
      dataMap.put("allPeopleSafeEvent",thisAllTotalMA);
      dataMap.put("peopleSafeEvent",thisReportTotalMA);
      dataMap.put("peopleSafeDisposal",thisDisposalCountsMA);
      dataMap.put("lastPeopleSafeEvent",lastReportTotalMA);
      dataMap.put("peopleSafeDisposalRate",formatPercent(thisDisposalCountsMA,thisReportTotalMA,nf));
      dataMap.put("peopleSafeRGRate",RGPercent(thisReportTotalMA,lastReportTotalMA,nf));
      dataMap.put("peopleSafeRate",formatPercent(thisReportTotalMA,thisReportTotal,nf));
      List<Map<String,Object>> peopleSafeListMap = new ArrayList<Map<String,Object>>();
      List<Map<String,Object>> peopleLifeListMap = new ArrayList<Map<String,Object>>();
      for (BaseDataDict b : typeBDD) {
    	code = b.getDictGeneralCode();
      	if(maMap.get(code)!=null) {//?????????????????????
      		if(maDataMap.get(code)!=null) {
      			maDataMap.get(code).put("eventType",b.getDictName());
      			maDataMap.get(code).put("peopleSafeTypeRate",formatPercent(maDataMap.get(code).get("thisReportTotal"),thisReportTotalMA,nf));
      			peopleSafeListMap.add( maDataMap.get(code));
      		}else {
      			peopleSafeListMap.add(getInitMap(code,b.getDictName()));
      		}
      	}else if(msMap.get(code)!=null) {//?????????????????????
      		if(msDataMap.get(code )!=null) {
      			msDataMap.get(code).put("eventType",b.getDictName());
      			msDataMap.get(code).put("peopleLifeTypeRate",formatPercent(msDataMap.get(code).get("thisReportTotal"),thisReportTotalMS,nf));
      			peopleLifeListMap.add( msDataMap.get(code ));
      		}else {
      			peopleLifeListMap.add(getInitMap(code,b.getDictName()));
      		}
      	}
      	
      }
      dataMap.put("peopleSafeListMap",peopleSafeListMap);
      dataMap.put("peopleLifeListMap",peopleLifeListMap);
		return dataMap;
	}
	
	public Map<String,Object> getInitMap(String code, String dictName){
		Map<String, Object> d = new HashMap<String,Object>();
		d.put("eventType", dictName);
		d.put("allPeopleSafeTypeTotal", 0);
		d.put("allPeopleLifeTypeTotal", 0);
  		d.put("DICT_GENERAL_CODE", code);d.put("lastReportTotal", 0);d.put("thisReportTotal", 0);d.put("thisDisposalCounts", 0);
  		d.put("peopleLifeTypeRGRate", "??????");d.put("peopleLifeTypeRate", "0%");d.put("peopleLifeTypeDisposalRate", "0%");
  		d.put("peopleSafeTypeRGRate", "??????");d.put("peopleSafeTypeRate", "0%");d.put("peopleSafeTypeDisposalRate", "0%");
  		return d;
	}
    
    
    public Map<String,Object> handleTable3Province(List<Map<String,Object>> gridAndCMC,List<Map<String,Object>> volunteerAndDuty,String gridLevel){
        int v1,v2,v3,v4 ;
        String str ="</w:t></w:r></w:p><w:p><w:pPr><w:widowControl w:val=\"off\"/><w:ind w:left=\"-200\" w:left-chars=\"-100\" w:right=\"-92\" w:right-chars=\"-46\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"????????????\" w:h-ansi=\"????????????\" w:hint=\"default\"/><w:sz w:val=\"21\"/><w:sz-cs w:val=\"21\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"????????????\" w:h-ansi=\"????????????\" w:hint=\"fareast\"/><w:b/><w:b-cs/><w:sz w:val=\"21\"/><w:sz-cs w:val=\"21\"/></w:rPr><w:t>";
        if ("???".equals(gridLevel)) {//??????????????????
        	gridAndCMC.get(7).put("channel", "???????????????"+str+"??????????????????");
           v1=0;
           v2=8;
           v3=6;
           v4=7;
       }else {
    	   gridAndCMC.get(7).put("channel", "?????????"+str+"??????????????????");
            v1=1;
            v2=7;
            v3=5;
            v4=6;
        }
        //'??????????????????',0,'??????????????????',1,'??????????????????',2,'??????????????????',3,'??????????????????',4,'?????????',5,'???????????????',6,'??????????????????',7
        Integer reportTotal = 0;
        Integer reportCounts = 0;//?????????????????? ?????????
        Integer disposalCounts = 0;
        Integer overdue = 0;
        Integer allCount = 0;
        Map<String,Object> CMDTotalMap = new HashMap<String,Object>();
        NumberFormat nf = NumberFormat.getInstance();
        Map<String,Object> dataMap = new HashMap<>();
        List<Map<String,Object>> tableData3 = new ArrayList<Map<String,Object>>();
       
       //???????????????????????????
        for (int i = v1; i <=4 ; i++) {
          //??????????????????
            reportCounts += Integer.parseInt(gridAndCMC.get(i).get("reportCounts").toString());
            //??????????????????
            disposalCounts += Integer.parseInt(gridAndCMC.get(i).get("disposalCounts").toString());
            //??????????????????
            overdue += Integer.parseInt(gridAndCMC.get(i).get("overdue").toString());
            //???????????????
            allCount += Integer.parseInt(gridAndCMC.get(i).get("allCounts").toString());
        }
        //????????????
        CMDTotalMap.put("reportCounts",reportCounts);
        CMDTotalMap.put("disposalCounts",disposalCounts);
        CMDTotalMap.put("overdue",overdue);
        CMDTotalMap.put("allCounts",allCount);
        
        Map<String, Object> gridManMap = gridAndCMC.get(5);//?????????
        
        reportTotal=reportCounts+Integer.parseInt(gridAndCMC.get(6).get("reportCounts").toString())//???????????????
        +Integer.parseInt(gridAndCMC.get(7).get("reportCounts").toString())//??????????????????
        +Integer.parseInt(gridManMap.get("reportCounts").toString()); //?????????

        //?????????
        gridManMap.put("rate",formatPercent(gridManMap.get("reportCounts"),gridManMap.get("allCounts"),nf));
        gridManMap.put("disposalRate",formatPercent(gridManMap.get("disposalCounts"),gridManMap.get("reportCounts"),nf));
        gridManMap.put("overdueRate",formatPercent(gridManMap.get("overdue"),gridManMap.get("reportCounts"),nf));
        tableData3.add(gridManMap);
        //????????????
        CMDTotalMap.put("rate",formatPercent(CMDTotalMap.get("reportCounts"),CMDTotalMap.get("allCounts"),nf));
        CMDTotalMap.put("disposalRate",formatPercent(CMDTotalMap.get("disposalCounts"),CMDTotalMap.get("reportCounts"),nf));
        CMDTotalMap.put("overdueRate",formatPercent(CMDTotalMap.get("overdue"),CMDTotalMap.get("reportCounts"),nf));
        CMDTotalMap.put("channel","????????????");
        tableData3.add(CMDTotalMap);
        //??????
        //Object CMDReportCounts = CMDTotalMap.get("reportCounts");
        for (int i = v1+1; i <=4 ; i++) {
            Map<String, Object> eventMap = gridAndCMC.get(i);
            eventMap.put("rate",formatPercent(eventMap.get("reportCounts"),eventMap.get("allCounts"),nf));
            eventMap.put("disposalRate",formatPercent(eventMap.get("disposalCounts"),eventMap.get("reportCounts"),nf));
            eventMap.put("overdueRate",formatPercent(eventMap.get("overdue"),eventMap.get("reportCounts"),nf));       
            tableData3.add(eventMap);
        }
        int volunteerAndDutySize = gridAndCMC.size();
        for (int i = 6; i <volunteerAndDutySize ; i++) {
            Map<String, Object> eventMap = gridAndCMC.get(i);
            eventMap.put("rate",formatPercent(eventMap.get("reportCounts"),eventMap.get("allCounts"),nf));
            eventMap.put("disposalRate",formatPercent(eventMap.get("disposalCounts"),eventMap.get("reportCounts"),nf));
            eventMap.put("overdueRate",formatPercent(eventMap.get("overdue"),eventMap.get("reportCounts"),nf));
            tableData3.add(eventMap);
        }
        int j= 1;
        for (int i = 0; i < v2; i++) {
            if (i==1){
                tableData3.get(i).put("addFontBold","<w:b/><w:b-cs/>");
                tableData3.get(i).put("number",j++);
                tableData3.get(i).put("typeSpan","<w:vmerge w:val=\"restart\" />");
            }else if (i==0||i==v3||i==v4){
                tableData3.get(i).put("addFontBold","<w:b/><w:b-cs/>");
                tableData3.get(i).put("number",j++);
                tableData3.get(i).put("typeSpan","");
            }else {
                tableData3.get(i).put("number",j);
                tableData3.get(i).put("typeSpan","<w:vmerge />");
            }

        }

        dataMap.put("tableData3",tableData3);
        return dataMap;
    }
   
    public Map<String,Object> handleCityEventListMap(List<Map<String,Object>> cityEventListMap, List<Map<String, Object>> thisCity){
        NumberFormat nf = NumberFormat.getInstance();
        Map<String,Object> dataMap = new HashMap<>();
        Integer allCityTotalEvent =0 ;
        Integer cityTotalEvent =0 ;
        Integer cityTotalDisposal=0 ;

        int cityEventListMapSize = cityEventListMap.size();
        for (int i = 0; i < cityEventListMapSize; i++) {
            Map<String, Object> eventMap = cityEventListMap.get(i);
            allCityTotalEvent+= Integer.parseInt(eventMap.get("allCounts").toString());
            cityTotalEvent+= Integer.parseInt(eventMap.get("reportCounts").toString());
            cityTotalDisposal+= Integer.parseInt(eventMap.get("disposalCounts").toString());
        }
        List<Map<String,Object>> cityList = new ArrayList<Map<String,Object>>();
        //???????????????
        for (int i = 0; i < cityEventListMapSize ; i++) {
            Map<String, Object> eventMap = cityEventListMap.get(i);
            eventMap.put("perCapita",formatPercent2(eventMap.get("reportCounts"),eventMap.get("allCounts"),nf));
            eventMap.put("rate",eventMap.get("perCapita")+"%");
            cityList.add(eventMap);
        }
        Collections.sort(cityList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                 if (Double.parseDouble(o1.get("perCapita").toString())>Double.parseDouble(o2.get("perCapita").toString())){
                     return -1;
                 }else if ("0".equals(o2.get("perCapita").toString())) {
                     return 0;
                 }else {
                     return 1;
                 }
            }
        });
        dataMap.put("allCityTotalEvent",allCityTotalEvent);
        dataMap.put("cityTotalEvent",cityTotalEvent);
        dataMap.put("cityTotalDisposal",cityTotalDisposal);
        if(thisCity != null && thisCity.size()>0) {
        	cityEventListMap.add(thisCity.get(0));
        }
        dataMap.put("cityList2",cityEventListMap);
        dataMap.put("cityList1",cityList);
        return dataMap;
    }
    
    public Map<String,Object> handleCountyEventListMap(List<Map<String,Object>> countyEventListMap){
        NumberFormat nf = NumberFormat.getInstance();
        Map<String,Object> dataMap = new HashMap<>();

        int countyEventListMapSize = countyEventListMap.size();

        //??????
        for (int i = 0; i < countyEventListMapSize; i++) {
            Map<String, Object> eventType1 = countyEventListMap.get(i);
            countyEventListMap.get(i).put("perCapita",formatDivision(eventType1.get("constantPeople"),eventType1.get("reportCounts"),nf));

        }

        Collections.sort(countyEventListMap, new Comparator<Map<String, Object>>() {
        	@Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        		Double d1 = Double.parseDouble(o1.get("perCapita").toString());
        		Double d2 = Double.parseDouble(o2.get("perCapita").toString());
        		return d1.compareTo(d2);
            }
        });
 
        dataMap.put("tableData4",countyEventListMap);
        return dataMap;
    }
    
    public Map<String,Object> handleCountyOverdueEvent(List<Map<String,Object>> countyOverdueEventListMap){
        NumberFormat nf = NumberFormat.getInstance();
        Map<String,Object> dataMap = new HashMap<>();
        int dueCountyCityNumber = 0;
        List<Map<String,Object>> dueCountyCityList=new ArrayList<>();
        int size = countyOverdueEventListMap.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> eventMap = countyOverdueEventListMap.get(i);
            countyOverdueEventListMap.get(i).put("rate",formatPercent(eventMap.get("overdueTotal"),eventMap.get("reportCounts"),nf));
            if ("0".equals(eventMap.get("overdueTotal").toString())){
                dueCountyCityNumber++;
                dueCountyCityList.add(eventMap);
            }
        }
        Collections.sort(countyOverdueEventListMap, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String o1str = o1.get("rate").toString();
                String o1Double = o1str.substring(0,o1str.length()-1);
                String o2str = o2.get("rate").toString();
                String o2Double = o2str.substring(0,o2str.length()-1);

                if (Double.parseDouble(o1Double)>Double.parseDouble(o2Double)){
//                    if ((Double.parseDouble(o2.get("perCapita").toString())-b)<0.0001){
//                        return -1;
//                    }
                    return -1;
                }else if (Double.parseDouble(o1Double)<Double.parseDouble(o2Double)) {
//                    if ((Double.parseDouble(o1.get("perCapita").toString())-b)<0.0001){
//                        return 1;
//                    }
                    return 1;
                }else {
                    return 0;
                }
            }
        });
        dataMap.put("dueCountyCityNumber",dueCountyCityNumber);
        dataMap.put("dueCountyCityList",dueCountyCityList);

        dataMap.put("tableData6",countyOverdueEventListMap);
        return dataMap;
    }


    public static String formatPercent(Object value, Object denominator, NumberFormat nf){
        return formatPercent2(value,denominator,nf)+"%";
    }
    public static String formatPercent2(Object value, Object denominator, NumberFormat nf){
        return ((value==null || "0".equals(value.toString())||denominator==null || "0".equals(denominator.toString()))?"0"
                :(nf.format(Math.round(Double.parseDouble(value.toString()) /Double.parseDouble(denominator.toString()) * 10000) / 100.00).toString().replaceAll(",","")));
    }
    public static String RGPercent(Object value, Object denominator, NumberFormat nf){
        if (value==null){
            value = "0";
        }
        String RGString =  (denominator==null || "0".equals(denominator.toString()))?"??????"
                :(nf.format(Math.round((Double.parseDouble(value.toString())-Double.parseDouble(denominator.toString()))/Double.parseDouble(denominator.toString()) * 10000) / 100.00)+"%");
        boolean status = RGString.contains("-");
        if (status){
            return RGString.replace("-","??????");
        }else {
//            if (RGString.contains("??????")){
//                return RGString;
//            }
            return ("??????"+RGString);
        }
    }
    public static String formatDivision(Object value, Object denominator, NumberFormat nf){
        return ((value==null || "0".equals(value.toString())||denominator==null || "0".equals(denominator.toString()))?"0"
                :(nf.format(Math.round(Double.parseDouble(value.toString()) /Double.parseDouble(denominator.toString()) * 1000000) / 100.00)).toString().replaceAll(",",""));
    }

	@Override
	public EUDGPagination getEventListData(Map<String, Object> params, UserInfo userInfo) {
		Integer page=1;
		Integer rows=20;
		if(CommonFunctions.isNotBlank(params, "page")) {
			page=Integer.valueOf(params.get("page").toString());
		}
		if(CommonFunctions.isNotBlank(params, "rows")) {
			rows=Integer.valueOf(params.get("rows").toString());
		}
		page=page>0?page:1;
		rows=rows>0?rows:20;
		
		EUDGPagination eventPagination=new EUDGPagination();
		
		try {
			this.formatParamIn(params,userInfo);
			RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
			Long count = 0L;
			List<Map<String, Object>> eventList = null;
			
			count = briefingMapper.getEventListCount(params);
			
			if(count > 0) {
				eventList = briefingMapper.getEventListData(params, rowBounds);
			} else {
				eventList = new ArrayList<Map<String, Object>>();
			}
			
			formatMapDataOut(eventList, params);
			
			eventPagination = new EUDGPagination(count, eventList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventPagination;
	}
    @Override
    public EUDGPagination getEventListDataByDisposal(Map<String, Object> params, UserInfo userInfo) {
        Integer page=1;
        Integer rows=20;
        if(CommonFunctions.isNotBlank(params, "page")) {
            page=Integer.valueOf(params.get("page").toString());
        }
        if(CommonFunctions.isNotBlank(params, "rows")) {
            rows=Integer.valueOf(params.get("rows").toString());
        }
        page=page>0?page:1;
        rows=rows>0?rows:20;

        EUDGPagination eventPagination=new EUDGPagination();

        try {
            this.formatParamIn(params,userInfo);
            RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
            Long count = 0L;
            List<Map<String, Object>> eventList = null;

            count = briefingMapper.getEventListCountByDisposal(params);

            if(count > 0) {
                eventList = briefingMapper.getEventListDataByDisposal(params, rowBounds);
            } else {
                eventList = new ArrayList<Map<String, Object>>();
            }

            formatMapDataOut(eventList, params);

            eventPagination = new EUDGPagination(count, eventList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventPagination;
    }
    @Override
    public EUDGPagination getEventListDataByTimeDisposal(Map<String, Object> params, UserInfo userInfo) {
        Integer page=1;
        Integer rows=20;
        if(CommonFunctions.isNotBlank(params, "page")) {
            page=Integer.valueOf(params.get("page").toString());
        }
        if(CommonFunctions.isNotBlank(params, "rows")) {
            rows=Integer.valueOf(params.get("rows").toString());
        }
        page=page>0?page:1;
        rows=rows>0?rows:20;

        EUDGPagination eventPagination=new EUDGPagination();

        try {
            this.formatParamIn(params,userInfo);
            RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
            Long count = 0L;
            List<Map<String, Object>> eventList = null;

            count = briefingMapper.getEventListCountByTimeDisposal(params);

            if(count > 0) {
                eventList = briefingMapper.getEventListDataByTimeDisposal(params, rowBounds);
            } else {
                eventList = new ArrayList<Map<String, Object>>();
            }

            formatMapDataOut(eventList, params);

            eventPagination = new EUDGPagination(count, eventList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventPagination;
    }
    private void formatMapDataOut(List<Map<String, Object>> eventList, Map<String, Object> params) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			String userOrgCode = null;
			List<BaseDataDict> influenceDegreeDictList = null,
							   urgencyDegreeDictList = null,
							   statusDictList = null,
							   subStatusDictList = null,
							   eventTypeDict = null,
							   evaLevelDictList = null,
							   sourceDictList = null,
							   patrolTypeDictList=null,
							   collectWayDictList = null; 
			List<Node> workflowNodeList = null;
			boolean isCapCurNodeInfo = false,
					      isCapCurHandlerName = false;
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
				userOrgCode = params.get("orgCode").toString();
			}
			
			
			dictMap.put("orgCode", userOrgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			influenceDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, userOrgCode);
			urgencyDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, userOrgCode);
			statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
			subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, userOrgCode);
			eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userOrgCode);
			collectWayDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, userOrgCode);
			
			if(CommonFunctions.isNotBlank(params, "isCapCurNodeInfo")) {
				isCapCurNodeInfo = Boolean.valueOf(params.get("isCapCurNodeInfo").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "isCapCurHandlerName")) {
				isCapCurHandlerName = Boolean.valueOf(params.get("isCapCurHandlerName").toString());
			}
			
			if(isCapCurNodeInfo) {
				Long workflowId = null;//?????????queryNodes?????????????????????
				
				workflowNodeList = eventDisposalWorkflowService.queryNodes(workflowId);
			}
			
			for(Map<String, Object> eventMap : eventList) {
				userOrgCode = null; 
				
				// ???????????????????????????
				if(CommonFunctions.isNotBlank(eventMap, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventMap.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						eventMap.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						eventMap.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				if(CommonFunctions.isNotBlank(eventMap, "curNodeName")) {
					if(workflowNodeList != null) {
						Long workflowId = -1L;
						String curNodeName = eventMap.get("curNodeName").toString();
						
						if(CommonFunctions.isNotBlank(eventMap, "workflowId")) {
							workflowId = Long.valueOf(eventMap.get("workflowId").toString());
						}
						
						//????????????????????????????????????????????????????????????????????????????????????
						if(workflowId > 0) {
							for(Node workflowNode : workflowNodeList) {
								if(workflowId.equals(workflowNode.getWorkFlowId()) && curNodeName.equals(workflowNode.getNodeName())) {
									eventMap.put("curNodeNameZH", workflowNode.getNodeNameZH());
									break;
								}
							}
						}
					}
					
					//?????????????????????????????????????????????????????????????????????????????????????????????????????????
					if(isCapCurHandlerName) {
						Long instanceId = null;
						Map<String, Object> curDataMap = null;
						StringBuffer curHandlerName = new StringBuffer("");
						String[] userNameArray = null, orgNameArray = null;
						
						if(CommonFunctions.isNotBlank(eventMap, "instanceId")) {
							instanceId = Long.valueOf(eventMap.get("instanceId").toString());
						}
						
						if(instanceId != null && instanceId > 0) {
							curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_USERNAME_ALL")) {
								userNameArray = curDataMap.get("WF_USERNAME_ALL").toString().split(",");
							}
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_ORGNAME_ALL")) {
								orgNameArray = curDataMap.get("WF_ORGNAME_ALL").toString().split(",");
							}
						}
						
						if(userNameArray != null && orgNameArray != null) {
							for(int index = 0, userNameLen = userNameArray.length, orgNameLen = orgNameArray.length; index < userNameLen; index++) {
								curHandlerName.append(userNameArray[index]);
								
								if(orgNameLen > index) {
									curHandlerName.append("(").append(orgNameArray[index]).append(");");
								}
							}
						} else if(orgNameArray != null) {
							for(String orgName : orgNameArray) {
								curHandlerName.append("(").append(orgName).append(");");
							}
						}
						
						if(curHandlerName.length() > 0) {
							eventMap.put("curHandlerName", curHandlerName.toString());
						}
					}
				}
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "influenceDegree", "influenceDegreeName", influenceDegreeDictList);
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDictList);
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "status", "statusName", statusDictList);
				
				// ???????????????
				DataDictHelper.setDictValueForField(eventMap, "subStatus", "subStatusName", subStatusDictList);
				
				// ??????????????????
				DataDictHelper.setDictValueForField(eventMap, "patrolType", "patrolTypeName", patrolTypeDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "statusName") && CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
					String subStatus = eventMap.get("subStatus").toString(),
						   subStatusName = eventMap.get("subStatusName").toString();
					
					if(ConstantValue.REJECT_SUB_STATUS.equals(subStatus)) {
						eventMap.put("statusName", subStatusName);
					} else if(CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
						eventMap.put("statusName", eventMap.get("statusName") + "-" + subStatusName);
					}
				}
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "evaLevel", "evaLevelName", evaLevelDictList);
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "source", "sourceName", sourceDictList);
				
				// ????????????
				DataDictHelper.setDictValueForField(eventMap, "collectWay", "collectWayName", collectWayDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
					eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "handleDate")) {
					eventMap.put("handleDateStr", DateUtils.formatDate((Date)eventMap.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "finTime")) {
					eventMap.put("finTimeStr", DateUtils.formatDate((Date)eventMap.get("finTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
					eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "remindMark") && CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "3");
				} else if(CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "2");
				} else if(CommonFunctions.isNotBlank(eventMap, "remindMark")) {
					eventMap.put("remindStatus", "1");
				}
				if(CommonFunctions.isNotBlank(eventMap, "wfAttentionStatus")) {
					eventMap.put("isAttention", "1".equals(eventMap.get("wfAttentionStatus").toString()));
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	private void formatParamIn(Map<String, Object> params,UserInfo userInfo) throws ZhsqEventException {
		if(params != null && !params.isEmpty()) {
			//????????????????????????
			StringBuffer dynamicSql4Event = new StringBuffer("");
			List<String> statusList = null;
			StringBuffer msgWrong = new StringBuffer("");
			
			if(msgWrong.length() > 0) {
				throw new ZhsqEventException(msgWrong.toString());
			}
			
			
			
			if(CommonFunctions.isNotBlank(params, "statusList")) {
				Object statusListObj = params.get("statusList");
				if(statusListObj instanceof List) {
					statusList = (List<String>) params.get("statusList");
				} else if(statusListObj instanceof String) {
					statusList = Arrays.asList(statusListObj.toString().split(","));
				}
			} else if(CommonFunctions.isNotBlank(params, "statusArray")) {
				Object statusArray = params.get("statusArray");
				if(statusArray instanceof String[]) {
					statusList = Arrays.asList((String[]) params.get("statusArray"));
				}
			} else if(CommonFunctions.isNotBlank(params, "status")) {
				statusList = Arrays.asList(params.get("status").toString().split(","));
			}
			
			if(statusList!=null) {
				params.put("statusList", statusList);
			}
			
			if(CommonFunctions.isNotBlank(params, "collectWay")) {
				String collectWay = params.get("collectWay").toString();
				if(collectWay.contains(",")) {
					params.put("collectWayArray", collectWay.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "subStatus")) {
				String subStatus = params.get("subStatus").toString();
				if(subStatus.contains(",")) {
					params.put("subStatusArray", subStatus.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "influenceDegree")) {
				String influenceDegree = params.get("influenceDegree").toString();
				if(influenceDegree.contains(",")) {
					params.put("influenceDegreeArray", influenceDegree.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				String urgencyDegree = params.get("urgencyDegree").toString();
				if(urgencyDegree.contains(",")) {
					params.put("urgencyDegreeArray", urgencyDegree.split(","));
				}
			}

            if(CommonFunctions.isNotBlank(params, "orgLevel")) {
                String orgLevel = params.get("orgLevel").toString();
                if(orgLevel.contains(",")) {
                    params.put("orgLevelArray", orgLevel.split(","));
                }
            }


            if(CommonFunctions.isNotBlank(params, "hoOrgLevel")) {
                String hoOrgLevel = params.get("hoOrgLevel").toString();
                if(hoOrgLevel.contains(",")) {
                    params.put("hoOrgLevelArray", hoOrgLevel.split(","));
                }
            }

			if(CommonFunctions.isNotBlank(params, "source")) {
				String source = params.get("source").toString();
				if(source.contains(",")) {
					params.put("sourceArray", source.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "attrFlag")) {
				String attrFlag = params.get("attrFlag").toString();
				
				if(!attrFlag.contains(",%")) {
					attrFlag = attrFlag.replaceAll(",", ",%");
				}
				
				params.put("attrFlag", attrFlag);
			}
			if(CommonFunctions.isNotBlank(params, "evaLevelList")) {
				Object evaLevelListObj = params.get("evaLevelList");
				if(evaLevelListObj instanceof String) {
					params.put("evaLevelList", Arrays.asList(evaLevelListObj.toString().split(","))); 
				}
			}


			if(CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String bizPlatform = params.get("bizPlatform").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizPlatformArray", bizPlatform.split(","));
					params.remove("bizPlatform");
				}
			}
			if(CommonFunctions.isNotBlank(params, "gridId") && CommonFunctions.isBlank(params, "infoOrgCode")) {
				Long gridId = null;
				
				try {
					gridId = Long.valueOf(params.get("gridId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(gridId != null && gridId > 0) {
					MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
					if(gridInfo != null) {
						params.put("infoOrgCode", gridInfo.getInfoOrgCode());
					}
				}
			}
			
			
			
			if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")) {
				String eventAttrTrigger = params.get("eventAttrTrigger").toString(),
					   eventAttrName = "",
					   orgCode = "";
				
				if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
					orgCode = params.get("eventAttrOrgCode").toString();
				} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
					orgCode = params.get("orgCode").toString();
				}
				
				eventAttrName = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				
				if(StringUtils.isNotBlank(eventAttrName)){
					String[] eventAttrNameArray = eventAttrName.split(",");
					String attrValue = "";
					
					for(String attrName : eventAttrNameArray){
						attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
						
						if(StringUtils.isNotBlank(attrValue)){
							dynamicSql4Event.append(" AND T1.").append(attrName).append(" IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
						} else {
							attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
							
							if(StringUtils.isNotBlank(attrValue)) {
								dynamicSql4Event.append(" AND T1.").append(attrName).append(" NOT IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
							}
						}
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "type")) {
				String type = params.get("type").toString();
				
				if(type.contains(",")) {
					params.put("types", params.get("type"));
					params.remove("type");
				}
			}
			if(CommonFunctions.isNotBlank(params, "types") || CommonFunctions.isNotBlank(params, "trigger")) {
				boolean isRemoveTypes = false;
				String types = "";
				
				if(CommonFunctions.isNotBlank(params, "isRemoveTypes")) {//isRemoveTypes???true????????????types???????????????????????????isRemoveTypes???false????????????types????????????????????????
					isRemoveTypes = Boolean.valueOf(params.get("isRemoveTypes").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "types")) {
					types = params.get("types").toString().trim();
				} else if(CommonFunctions.isNotBlank(params, "trigger")) {
					String trigger = params.get("trigger").toString(),
						   orgCode = "";
					
					if(CommonFunctions.isNotBlank(params, "orgCode")) {
						orgCode = params.get("orgCode").toString();
					}
					
					types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					
					if(StringUtils.isBlank(types)) {
						isRemoveTypes = true;
						types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					}
				}
				
				if(StringUtils.isNotBlank(types)) {
					String[] typesArray = types.split(",");
					
					if(typesArray.length > 0) {
						if(isRemoveTypes) {
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" AND T1.TYPE_ NOT LIKE '").append(_type).append("%'");
								}
							}
						} else {
							dynamicSql4Event.append(" AND ( 1 != 1 ");
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" OR T1.TYPE_ LIKE '" + _type + "%'");
								}
							}
							dynamicSql4Event.append(") ");
						}
					}
				}
			}

			if(dynamicSql4Event.length() > 0) {
				params.put("dynamicSql4Event", dynamicSql4Event.toString());
			}
		}
	}
	
}
