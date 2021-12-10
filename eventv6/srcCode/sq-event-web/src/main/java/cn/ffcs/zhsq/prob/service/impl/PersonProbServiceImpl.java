package cn.ffcs.zhsq.prob.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.base.export.ExpStruc;
import cn.ffcs.zhsq.base.export.IExporter;
import cn.ffcs.zhsq.mybatis.persistence.prob.PersonProbMapper;
import cn.ffcs.zhsq.prob.service.IPersonProbService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "personProbService")
public class PersonProbServiceImpl implements IPersonProbService,IExporter {

	@Autowired
	private PersonProbMapper personProbMapper;

	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Override
	public EUDGPagination searchList(int page, int rows,
			Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<Map<String, Object>> list = personProbMapper.findPageList4Person(params, rowBounds);
		int count = personProbMapper.findCount4Person(params);
		EUDGPagination pagination = new EUDGPagination(count,dateFormat(list));
		return pagination;
		
	}

	@Override
	public Map<String, Object> searchById(int id) {
		
		return personProbMapper.searchById(id);
	}
	
	public List<Map<String, Object>>  dateFormat(List<Map<String, Object>> list) {
		
		 //政治面貌
		 List<BaseDataDict> politics = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.POLITICS, null);
		 //违纪人员职级
		 List<BaseDataDict> professionType = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PROFESSION_TYPE, null);
		 //党纪处分
		 List<BaseDataDict> partyFlag = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PARTY_FLAG, null);
		
		 //政纪处分
		 List<BaseDataDict> disciplineyFlag = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DISCIPLINE_FLAG, null);
		 
		//组织处理类型
		 List<BaseDataDict> orgProcType = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ORG_PROC_TYPE, null);
		 
		//违纪违规资金类别
		 List<BaseDataDict> violationMoneyType = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.VIOLATION_MONEY_TYPE, null);
		 
		//扫黑除恶类类
		 List<BaseDataDict> violationType0 = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.VIOLATION_TYPE_0, null);
		//违规违纪类
		 List<BaseDataDict> violationType1 = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.VIOLATION_TYPE_1, null);
		//作风建设类类
		 List<BaseDataDict> violationType2 = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.VIOLATION_TYPE_2, null);
		
		 
		//四种形态
		 List<BaseDataDict> shape = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SHAPE, null);
		 if(list != null) {			
			 for(Map<String, Object> data : list) {
				 if(data.containsKey("violationDate")){
					 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					 String dateString = formatter.format(data.get("violationDate"));
					 data.remove("violationDate");
					 data.put("violationDate", dateString);
				 }
				 if(data.containsKey("politics")){
					 for(BaseDataDict dict : politics) {
						 if(data.get("politics").equals(dict.getDictGeneralCode())) {
							 data.remove("politics");
							 data.put("politics", dict.getDictName());
							 break;
						 }
					 }	
				 }
				 if(data.containsKey("professionType")){
					 for(BaseDataDict dict : professionType) {
						 if(data.get("professionType").equals(dict.getDictGeneralCode())) {
							 data.remove("professionType");
							 data.put("professionType", dict.getDictName());
							 break;
						 }
					 }	
				 }
				 if(data.containsKey("partyFlag")){
					 for(BaseDataDict dict : partyFlag) {
						 if(data.get("partyFlag").equals(dict.getDictGeneralCode())) {
							 data.remove("partyFlag");
							 data.put("partyFlag", dict.getDictName());
							 break;
						 }
					 }	
				 }
				 if(data.containsKey("disciplineyFlag")){
					 for(BaseDataDict dict : disciplineyFlag) {
						 if(data.get("disciplineyFlag").equals(dict.getDictGeneralCode())) {
							 data.remove("disciplineyFlag");
							 data.put("disciplineyFlag", dict.getDictName());
							 break;
						 }
					 }	 
				 }
				 if(data.containsKey("orgProcType")){
					 for(BaseDataDict dict : orgProcType) {
						 List<BaseDataDict> orgProcSonType = baseDictionaryService.getDataDictListOfSinglestage(dict.getDictCode(), null);
						 for (BaseDataDict sonDict : orgProcSonType) {
							 if(data.get("orgProcType").equals(sonDict.getDictGeneralCode())) {
								 data.remove("orgProcType");
								 data.put("orgProcType", dict.getDictName());
								 break;
							 }
						}	 
					 }	 
				 }
				 
				 if(data.containsKey("violationMoneyType")){
					 List<String> newList = new ArrayList<String>();
					 String[] violationMoneyTypeList = data.get("violationMoneyType").toString().split(",");
					 for (String v : violationMoneyTypeList) {
					     for(BaseDataDict dict : violationMoneyType) {
							 if(v.equals(dict.getDictGeneralCode())) {
								 newList.add(dict.getDictName());
								 break;
						      }
						 } 
					 }
					 data.remove("violationMoneyType");
					 
					 data.put("violationMoneyType", StringUtils.join(newList.toArray(),";"));
				 }
				 
				 if(data.containsKey("violationType")){
					 List<String> newViolationType0List = new ArrayList<String>();
					 List<String> newViolationType1List = new ArrayList<String>();
					 List<String> newViolationType2List = new ArrayList<String>();
					 String[] violationTypelList = data.get("violationType").toString().split(",");
					 for (String v : violationTypelList) {
					     for(BaseDataDict dict : violationType0) {
							 if(v.equals(dict.getDictGeneralCode())) {
								 newViolationType0List.add(dict.getDictName());
								 break;
						      }
						 } 
					     for(BaseDataDict dict : violationType1) {
							 if(v.equals(dict.getDictGeneralCode())) {
								 newViolationType1List.add(dict.getDictName());
								 break;
						      }
						 } 
					     for(BaseDataDict dict : violationType2) {
							 if(v.equals(dict.getDictGeneralCode())) {
								 newViolationType2List.add(dict.getDictName());
								 break;
						      }
						 } 
					 }
					 data.remove("violationType");
					 data.put("violationType0", StringUtils.join(newViolationType0List.toArray(),";"));
					 data.put("violationType1", StringUtils.join(newViolationType1List.toArray(),";"));
					 data.put("violationType2", StringUtils.join(newViolationType2List.toArray(),";"));
				 }
				 
				 if(data.containsKey("shape")){
					 for(BaseDataDict dict : shape) {
						 if(data.get("shape").equals(dict.getDictGeneralCode())) {
							 data.remove("shape");
							 data.put("shape", dict.getDictName());
							 break;
						 }
					 }	 
				 }
				 if(data.containsKey("blameFlag")){
					if ("1".equals(data.get("blameFlag"))) {
						data.remove("blameFlag");
						data.put("blameFlag", "是");
					}else {
						data.remove("blameFlag");
						data.put("blameFlag", "否");
					}
				 }
				 if(data.containsKey("transferJusticeFlag")){
						if ("1".equals(data.get("transferJusticeFlag"))) {
							data.remove("transferJusticeFlag");
							data.put("transferJusticeFlag", "是");
						}else {
							data.remove("transferJusticeFlag");
							data.put("transferJusticeFlag", "否");
						}
					 }
				 if(data.containsKey("sex")){
						data.remove("sex");
						if ("1".equals(data.get("sex"))) {
							data.put("sex", "男");
						}else {
							data.put("sex", "女");
						}
					 }
			 }
		  }
	     return list;
	}
	
	@Override
	public ExpStruc getExpStruc(String ctlType, UserInfo userInfo,
			Map<String, Object> pmMap) {
		ExpStruc expStruc = new ExpStruc(IExporter.EXP_LIST, "个人问题统计表");
	
		List<Map<String, Object>> dataList = personProbMapper.findPageList4Person(pmMap);
		dateFormat(dataList);
		
		//数据处理
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> item = dataList.get(i);
				if (item != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ID", i + 1);
					map.put("probTitle", item.get("probTitle"));
					map.put("name", item.get("name"));
					map.put("sex", item.get("sex"));
					map.put("politics", item.get("politics"));
					map.put("workUnit", item.get("workUnit"));
					
					map.put("profession", item.get("profession"));
					map.put("caseBrief", item.get("caseBrief"));
					map.put("professionType", item.get("professionType"));
					map.put("violationDate", item.get("violationDate"));
					map.put("partyFlag", item.get("partyFlag"));
					map.put("disciplineyFlag", item.get("disciplineyFlag"));
					map.put("orgProcType", item.get("orgProcType"));
					map.put("procResult", item.get("procResult"));
					map.put("blameFlag", item.get("blameFlag"));
					map.put("transferJusticeFlag", item.get("transferJusticeFlag"));
					map.put("amountInvolved", item.get("amountInvolved"));
					map.put("violationMoney", item.get("violationMoney"));
					map.put("violationMoneyType", item.get("violationMoneyType"));
					map.put("violationType1", item.get("violationType1"));
					map.put("violationType2", item.get("violationType2"));
					map.put("violationType0", item.get("violationType0"));
					map.put("shape", item.get("shape"));
					map.put("amountOfRecovery", item.get("amountOfRecovery"));

					list.add(map);
				}
			}
		}
		expStruc.setVals(list);
		//excel字段设置
		List<String[]> keys = new ArrayList<String[]>();
		keys.add(new String[]{"序号", "ID"});
		keys.add(new String[]{"问题标题", "probTitle"});
		keys.add(new String[]{"姓名", "name"});
		keys.add(new String[]{"性别","sex"});
		keys.add(new String[]{"政治面貌", "politics"});
		keys.add(new String[]{"单位", "workUnit"});
		keys.add(new String[]{"职务", "profession"});
		keys.add(new String[]{"简要案情", "caseBrief"});
		keys.add(new String[]{"违纪人员职级", "professionType"});
		keys.add(new String[]{"违纪违规时间", "violationDate"});
		keys.add(new String[]{"党纪处分", "partyFlag"});
		keys.add(new String[]{"政纪处分", "disciplineyFlag"});
		keys.add(new String[]{ "组织处理类型","orgProcType"});
		keys.add(new String[]{"组织处理内容", "procResult"});
		keys.add(new String[]{"是否问责", "blameFlag"});
		keys.add(new String[]{"是否移送司法", "transferJusticeFlag"});
		keys.add(new String[]{"涉案金额", "amountInvolved"});
		keys.add(new String[]{"违纪金额", "violationMoney"});
		keys.add(new String[]{"违纪违法资金类别", "violationMoneyType"});
		keys.add(new String[]{"违规违纪类别统计", "violationType1"});
		keys.add(new String[]{"作风建设类别统计", "violationType2"});
		keys.add(new String[]{"扫黑除恶类别统计", "violationType0"});		
		keys.add(new String[]{"四种形态", "shape"});
		keys.add(new String[]{"追缴资金（万元）", "amountOfRecovery"});
		expStruc.setKeys(keys);
		return expStruc;
	}

}
