package cn.ffcs.zhsq.map.thresholdcolorcfg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;
import cn.ffcs.zhsq.mybatis.persistence.map.thresholdcolorcfg.ThresholdColorCfgMapper;

/**
 * 2014-10-09 liushi add
 * 地图统计热力图显示样式区间设置服务实现
 * @author Administrator
 *
 */
@Service(value="thresholdColorCfgServiceImpl")
public class ThresholdColorCfgServiceImpl implements IThresholdColorCfgService{
	@Autowired
	private ThresholdColorCfgMapper thresholdColorCfgMapper;
	
	@Override
	public EUDGPagination findThresholdColorCfgPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<ThresholdColorCfg> list = this.thresholdColorCfgMapper.findPageThresholdColorCfgCriteria(params, rowBounds);
		int count = this.thresholdColorCfgMapper.findCountThresholdColorCfgCriteria(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public ThresholdColorCfg findThresholdColorCfgById(Long tcId) {
		
		return this.thresholdColorCfgMapper.findThresholdColorCfgById(tcId);
	}

	/**
	 * 2015-09-23 liushi add
	 * 保存（包括新增和删除）
	 * @param thresholdColorCfg
	 * @return
	 */
	@Override
	public boolean saveThresholdColorCfg(ThresholdColorCfg thresholdColorCfg) {
		Integer count = 0;
		if(thresholdColorCfg.getTcId() == null) {//进行插入操作
			count = this.thresholdColorCfgMapper.insertThresholdColorCfg(thresholdColorCfg);
		}else {
			count = this.thresholdColorCfgMapper.updateThresholdColorCfg(thresholdColorCfg);
		}
		return (count == 0)? false:true;
	}

	@Override
	public boolean deleteThresholdColorCfg(Long tcId) {
		Integer count = this.thresholdColorCfgMapper.deleteThresholdColorCfg(tcId);
		return (count == 0)? false:true;
	}

	@Override
	public List<Map<String,Object>> getThresholdColorCfgList(
			Map<String, Object> param) {
		String orgCode = "";
		List<ThresholdColorCfg> resultList = new ArrayList<ThresholdColorCfg>();
		List<ThresholdColorCfg> list = this.thresholdColorCfgMapper.getThresholdColorCfgList(param);
		if(list != null && list.size()>0) {
			if(list.get(list.size()-1).getOrgCode() != null && !"".equals(list.get(list.size()-1).getOrgCode())) {
				for(int i=0; i<list.size(); i++) {
					ThresholdColorCfg obj = list.get(i);
					if(obj.getOrgCode() != null && !"".equals(obj.getOrgCode()) && "".equals(orgCode)) {
						orgCode = obj.getOrgCode();
						resultList.add(obj);
					}else if(obj.getOrgCode() != null && !"".equals(obj.getOrgCode()) && orgCode.equals(obj.getOrgCode())) {
						resultList.add(obj);
					}else if(obj.getOrgCode() != null && !"".equals(obj.getOrgCode()) && !orgCode.equals(obj.getOrgCode())){
						break;
					}
				}
			}else {
				resultList = list;
			}
		}
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(ThresholdColorCfg obj:resultList) {
			String jsonStr = obj.getParamCfgStr();
			try {
				JSONObject a = new JSONObject(jsonStr);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("colorVal", (String)a.get("colorVal"));
				map.put("minValue", Float.parseFloat((String)a.get("minValue")));
				map.put("maxValue", Float.parseFloat((String)a.get("maxValue")));
				map.put("colorNum", Float.parseFloat((String)a.get("colorNum")));
				listMap.add(map);
			} catch (JSONException e) {
				listMap = null;
				e.printStackTrace();
			}
		}
		return listMap;
	} 
	
	
}
