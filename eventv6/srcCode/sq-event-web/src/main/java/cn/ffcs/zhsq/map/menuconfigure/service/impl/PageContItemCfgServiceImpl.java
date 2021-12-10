package cn.ffcs.zhsq.map.menuconfigure.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.map.menuconfigure.service.IPageContItemCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageContItemCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageIndexCfg;
import cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure.MenuConfigMapper;
import cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure.PageContItemCfgMapper;
import cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure.PageIndexCfgMapper;

@Service(value = "pageContItemCfgServiceImpl")
public class PageContItemCfgServiceImpl implements IPageContItemCfgService {
	@Autowired
	private MenuConfigMapper menuConfigMapper;
	@Autowired
	private PageContItemCfgMapper pageContItemCfgMapper;
	@Autowired
	private PageIndexCfgMapper pageIndexCfgMapper;
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	@Override
	public Long del(Long id) {
		int result = pageIndexCfgMapper.delPageIndexCfgById(id);
//		int result = pageContItemCfgMapper.insert(pageContItemCfg);
		return Long.valueOf(result);
	}
	
	@Override
	public Long save(PageContItemCfg pageContItemCfg) {
		int result = pageContItemCfgMapper.insert(pageContItemCfg);
		return Long.valueOf(result);
	}

	@Override
	public PageIndexCfg findById(Map<String, Object> params){
		PageIndexCfg pageIndexCfg = pageIndexCfgMapper.findById(params);
		format(params, pageIndexCfg);
		return pageIndexCfg;
	}

	@Override
	public PageIndexCfg findByOrgCode(Map<String, Object> params){
		PageIndexCfg pageIndexCfg = pageIndexCfgMapper.findByOrgCode(params);
		format(params, pageIndexCfg);
		return pageIndexCfg;
	}

	@Override
	public List<PageContItemCfg> findPageContItemCfgById(Map<String, Object> params){
		List<PageContItemCfg> pageContItemCfg = new ArrayList<PageContItemCfg>();
		if(null == params.get("pgIdxId") && null != params.get("pgIdxType") && null != params.get("orgCode")){
//			PageIndexCfg pageIndexCfg = pageIndexCfgMapper.findByOrgCode(params);
//			format(params, pageIndexCfg);
//			if(null != pageIndexCfg){
//				Map<String, Object> param = new HashMap<String, Object>();
//				param.put("pgIdxId", pageIndexCfg.getPgIdxCfgId());
//				pageContItemCfg = pageContItemCfgMapper.findPageContItemCfgById(param);
//			}
		}else{
			pageContItemCfg = pageContItemCfgMapper.findPageContItemCfgById(params);
		}
		return pageContItemCfg;
	}
	
	@Override
	public EUDGPagination findPageIndexCfgPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1 ? 1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		int count = pageIndexCfgMapper.findCountByCriteria(params);
		List<PageIndexCfg> list = pageIndexCfgMapper.findPageListByCriteria(params, rowBounds);
		for(PageIndexCfg pageIndexCfg : list){
			format(params, pageIndexCfg);
		}
//		List<BaseDataDict> baseDataDicts = dictionaryService.getDataDictListOfSinglestage("B559", params.get("infoOrgCode").toString());
//		for(PageIndexCfg pageIndexCfg : list){
//			for(BaseDataDict baseDataDict : baseDataDicts){
//				if(baseDataDict.getDictGeneralCode().equals(pageIndexCfg.getPgIdxType())){
//					pageIndexCfg.setPgIdxTypeName(baseDataDict.getDictName());
//				}
//			}
//		}
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	private void format(Map<String, Object> params,PageIndexCfg pageIndexCfg){
		if(null == pageIndexCfg)
			return;
		List<BaseDataDict> baseDataDicts = dictionaryService.getDataDictListOfSinglestage("B559", params.get("orgCode").toString());
		for(BaseDataDict baseDataDict : baseDataDicts){
			if(baseDataDict.getDictGeneralCode().equals(pageIndexCfg.getPgIdxType())){
				pageIndexCfg.setPgIdxTypeName(baseDataDict.getDictName());
			}
		}
		if(StringUtils.isNotBlank(pageIndexCfg.getRegionCode())){
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(pageIndexCfg.getRegionCode());
			if(null != gridInfo)
				pageIndexCfg.setGridName(gridInfo.getGridName());
		}
	}
	
	@Override
	public List<PageContItemCfg> findPageContItemCfg(PageContItemCfg pageContItemCfg) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pgIdxType", pageContItemCfg.getPgIdxType());
		param.put("orgCode", pageContItemCfg.getOrgCode());
		List<PageContItemCfg> pageContItemCfgs = pageContItemCfgMapper.findPageContItemCfg(param);
		return pageContItemCfgs;
	}

	@Override
	public Boolean savePageContItemCfgs(JSONArray jsonArray, String srcOrgCode,String srcPgIdxType,
			String orgCode, String pgIdxType, String displayStyle, String pgIdxId, String status) {
		Map<String, Object> srcParam = new HashMap<String, Object>();
		srcParam.put("orgCode", srcOrgCode);
		srcParam.put("pgIdxType", srcPgIdxType);
		srcParam.put("pgIdxId", pgIdxId);
//		param.put("status", status);
		List<PageContItemCfg> source = pageContItemCfgMapper.findPageContItemCfg(srcParam);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);
		param.put("pgIdxType", pgIdxType);
		param.put("pgIdxId", pgIdxId);
		pageIndexCfgMapper.delPageIndexCfg(param);
		pageContItemCfgMapper.delPageContItemCfgByOrgCode(param);
		//新增类型
		PageIndexCfg pageIndexCfg = new PageIndexCfg();
		pageIndexCfg.setPgIdxType(pgIdxType);
		pageIndexCfg.setRegionCode(orgCode);
		pageIndexCfg.setDisplayStyle(displayStyle);
		pageIndexCfg.setStatus(status);
		pageIndexCfgMapper.insert(pageIndexCfg);
		Integer pgIdxCfgId = pageIndexCfg.getPgIdxCfgId();
//		System.out.println(pageIndexCfg.getPgIdxCfgId());
		//新建一个根节点
		PageContItemCfg root = new PageContItemCfg();
		Integer autoidR = pageContItemCfgMapper.getId();
		root.setContItemId(autoidR);
		root.setName("根节点");
		root.setpContItemId(-1);
		root.setStatus("1");
		pageContItemCfgMapper.insert(root);
		Map<Long, Object> map = new HashMap<Long, Object>();
		map.put(-1L, autoidR);
		
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer autoid = pageContItemCfgMapper.getId();
			Integer id = (Integer)jsonObject.get("id");
			String name = ((String)jsonObject.get("name"));
			Integer displayOrder = ((Integer)jsonObject.get("index"));
			Integer layCfgId = id;
			for(int j=0; j<source.size(); j++){
				if(source.get(j).getContItemId().equals(id)){
					layCfgId = source.get(j).getLayCfgId();
				}
			}
			Integer pId = -1;
			if(!"null".equals(jsonObject.get("pId").toString())){
				pId = (Integer)jsonObject.get("pId");
			}
			map.put(Long.valueOf(id), autoid);
			PageContItemCfg pageContItemCfg = new PageContItemCfg();
			pageContItemCfg.setContItemId(autoid);
			pageContItemCfg.setName(name);
			pageContItemCfg.setDisplayOrder(displayOrder);
			pageContItemCfg.setStatus("1");
			pageContItemCfg.setLayCfgId(layCfgId);
			pageContItemCfg.setPgIdxId(pgIdxCfgId);
			Object k = map.get(Long.valueOf(pId));
			if(k == null){
				pageContItemCfg.setpContItemId((Integer)map.get(-1L));
			}else{
				pageContItemCfg.setpContItemId((Integer)k);
			}
			pageContItemCfgMapper.insert(pageContItemCfg);
		}
		
//		List<PageContItemCfg> source = null;
		if(null != source && source.size() > 0){
			for(int j=0; j<jsonArray.size(); j++){
				JSONObject jsonObject = jsonArray.getJSONObject(j);
				String name = (String)jsonObject.get("name");
				Integer id = (Integer)jsonObject.get("id");
				for(int i=0; i<source.size(); i++){
					if(source.get(i).getContItemId().equals(id)){
						if(source.get(i).getName().equals(name)){
							
						}
						break;
					}
				}
				
//				if(jsonArray.get(j))
//				if(menuListOrg.get(j).getPrivilegeId().intValue()==prividgeId){
//					if(menuListOrg.get(j).getMenuIcon()!=null && !menuListOrg.get(j).getMenuIcon().equals("")){
//						if(!menuListOrg.get(j).getMenuIcon().equals(icoUrl)){
//						icoUrl = menuListOrg.get(j).getMenuIcon();
//						}
//					}
//					break;
//				}	
			}
			
			
		}else{
			
			
//			for(PageContItemCfg item : pageContItemCfgs){
////				item.getContItemId()
//				if()
//				PageContItemCfg pageContItemCfg = new PageContItemCfg();
//				pageContItemCfg.setName(item.getName());
//				
//			}
//			pageContItemCfgMapper.i
			
		}
		
		return null;
	}
	
//	private void insertItemRecursion(JSONArray jsonArray){
//		for(int i=0; i<jsonArray.size(); i++){
//			JSONObject jsonObject = jsonArray.getJSONObject(i);
//			String tId = ((String)jsonObject.get("tId"));
//			Object parentTId = jsonObject.get("parentTId");
//			if()
//		}
//	}
}
