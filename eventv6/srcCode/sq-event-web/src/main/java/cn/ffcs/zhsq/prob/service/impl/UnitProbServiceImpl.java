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

import com.ffcs.auth.platform.spring.util.Constant;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.base.export.ExpStruc;
import cn.ffcs.zhsq.base.export.IExporter;
import cn.ffcs.zhsq.mybatis.persistence.prob.UnitProbMapper;
import cn.ffcs.zhsq.prob.service.IUnitProbService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "unitProbService")
public class UnitProbServiceImpl implements IUnitProbService,IExporter{
	@Autowired
	private UnitProbMapper unitProbMapper;
    
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	
	@Override
	public EUDGPagination searchList(int page, int rows,
			Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<Map<String, Object>> list = unitProbMapper.findPageList4Unit(params,rowBounds);
		int count = unitProbMapper.findCount4Unit(params);
		EUDGPagination pagination = new EUDGPagination(count,dateFormat(list));
		return pagination;
	}
	public List<Map<String, Object>>  dateFormat(List<Map<String, Object>> list) {
		
		//单位级别
		 List<BaseDataDict> unitLevel = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.UNIT_LEVEL, null);
		 //纪律处分
		 List<BaseDataDict> disciplinaryPunishment = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DISCIPLINARY_PUNISHMENT, null);
		
		 //问题处置类型
		 List<BaseDataDict> procType = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PROC_TYPE, null);
		 
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
				 if(data.containsKey("professionType")){
					 for(BaseDataDict dict : unitLevel) {
						 if(data.get("professionType").equals(dict.getDictGeneralCode())) {
							 data.remove("professionType");
							 data.put("professionType", dict.getDictName());
							 break;
						 }
					 }	
				 }
				 if(data.containsKey("disciplinaryPunishment")){
					 for(BaseDataDict dict : disciplinaryPunishment) {
						 if(data.get("disciplinaryPunishment").equals(dict.getDictGeneralCode())) {
							 data.remove("disciplinaryPunishment");
							 data.put("disciplinaryPunishment", dict.getDictName());
							 break;
						 }
					 }	
				 }
				 if(data.containsKey("procType")){
					 for(BaseDataDict dict : procType) {
						 if(data.get("procType").equals(dict.getDictGeneralCode())) {
							 data.remove("procType");
							 data.put("procType", dict.getDictName());
							 break;
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
				}
		   }
	     return list;
	}
	@Override
	public Map<String, Object> searchById(int id) {
	
		return unitProbMapper.searchById(id);
	}
	
	
	@Override
	public ExpStruc getExpStruc(String ctlType, UserInfo userInfo,
			Map<String, Object> pmMap) {
		ExpStruc expStruc = new ExpStruc(IExporter.EXP_LIST, "单位问题统计表");
	
		List<Map<String, Object>> dataList = unitProbMapper.findPageList4Unit(pmMap);
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
  				    map.put("caseBrief", item.get("caseBrief"));
					map.put("professionType", item.get("professionType"));
					map.put("violationDate", item.get("violationDate"));
					map.put("disciplinaryPunishment", item.get("disciplinaryPunishment"));
					map.put("procType", item.get("procType"));
					map.put("blameFlag", item.get("blameFlag"));
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
		keys.add(new String[]{"简要案情", "caseBrief"});
		keys.add(new String[]{"单位级别", "professionType"});		
		keys.add(new String[]{"违纪违规时间", "violationDate"});
		keys.add(new String[]{"纪律处分", "disciplinaryPunishment"});
		keys.add(new String[]{"问责处理类型", "procType"});		
		keys.add(new String[]{"是否问责", "blameFlag"});
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
