package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatContConfigService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatConfig;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatContConfig;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value = "/zhsq/map/gisStatConfig")
public class GisStatConfigController extends ZZBaseController {

	@Autowired
	private IGisStatConfigService gisStatConfigService;

	@Autowired
	private IGisStatContConfigService gisStatContConfigService;

	@Autowired
	private IMenuConfigService menuConfigService;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;

	/**
	 * <p>
	 * 获取框选周边可配置地图图层
	 * </p>
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param statType
	 *            0：框选可配置 1：周边可配置
	 * @param bizType
	 *            ARCGIS_STANDARD_HOME：标准首页 ARCGIS_FIRECONTROL_HOME：消防首页
	 * @param regionCode
	 *            地域编码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getGisStatConfigsForJsonp")
	public String getGisStatConfigsForJsonp(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "statType", required = false) String statType,
			@RequestParam(value = "bizType", required = false) String bizType,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			String jsonpcallback) throws Exception {
		Map<String, List<GisStatContConfig>> dataObj = this.getGisStatConfigs(session, request, map, statType, bizType,
				regionCode);
		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
		if (dataObj != null) {
			for (Map.Entry<String, List<GisStatContConfig>> data : dataObj.entrySet()) {
				List<GisStatContConfig> list = data.getValue();
				if (list != null) {
					List<String> tempList = new ArrayList<String>();
					for (GisStatContConfig gisStatContConfig : list) {
						String gscStr = net.sf.json.JSONObject.fromObject(gisStatContConfig).toString();
						tempList.add(gscStr);
					}
					// String gscStrs = net.sf.json.JSONArray.fromObject(tempList).toString();
					dataMap.put(data.getKey(), tempList);
				}
			}
		}
		String valueAfter = net.sf.json.JSONObject.fromObject(dataMap).toString();
		String jsonStr = jsonpcallback + "(" + valueAfter + ")";
		return jsonStr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGisStatConfigs")
	public Map<String, List<GisStatContConfig>> getGisStatConfigs(HttpSession session, HttpServletRequest request,
			ModelMap map, @RequestParam(value = "statType", required = false) String statType,
			@RequestParam(value = "bizType", required = false) String bizType,
			@RequestParam(value = "regionCode", required = false) String regionCode) throws Exception {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		regionCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		OrgEntityInfoBO orgEntityInfoBO = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(regionCode);
		regionCode = orgEntityInfoBO.getOrgCode();

		Map<String, List<GisStatContConfig>> category = new LinkedHashMap<String, List<GisStatContConfig>>();

		List<GisStatContConfig> gisStatContConfigs = new ArrayList<GisStatContConfig>();

		GisStatConfig gisStatConfig = this.gisStatConfigService.getGisStatConfig(statType, bizType, regionCode);

		if (gisStatConfig != null) {
			// 获取分类
//			String catetories = gisStatConfig.getCategories();
			long statCfgId = gisStatConfig.getStatCfgId();
			long gdcId = 0L;
			GisDataCfg gisDataCfg = new GisDataCfg();
			
			String classification = gisStatConfig.getClassification();
			
			if ("1".equals(classification)) { // 分类
				gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, "未分类");

				if (gisStatContConfigs != null && gisStatContConfigs.size() > 0) {
					for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
						gdcId = gisStatContConfig.getLayCfgId();
						gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
						gisStatContConfig.setGisDataCfg(gisDataCfg);
					}
					
					category.put("未分类", gisStatContConfigs);
				}

				// 获取分类
				String catetories = gisStatConfig.getCategories();

				if (StringUtils.isNotBlank(catetories)) {
					String[] catetoriesArray = catetories.split(",");

					for (String objClass : catetoriesArray) {
						gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, objClass);

						for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
							gdcId = gisStatContConfig.getLayCfgId();
							gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
							gisStatContConfig.setGisDataCfg(gisDataCfg);
						}

						category.put(objClass, gisStatContConfigs);
					}
				}
			} else if ("0".equals(classification)) { // 不分类
				gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, null);

				for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
					gdcId = gisStatContConfig.getLayCfgId();
					gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
					gisStatContConfig.setGisDataCfg(gisDataCfg);
				}

				category.put("", gisStatContConfigs);
			}
		}

		return category;
	}

	/**
	 * <p>
	 * 框选周边可配置首页
	 * </p>
	 * 
	 * @param map
	 * @param session
	 * @param statType
	 *            0：框选可配置 1：周边可配置
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap map, HttpSession session, @RequestParam(value = "statType") String statType) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String regionCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		map.addAttribute("regionCode", regionCode);
		map.addAttribute("statType", statType);
		return "/map/gisstatconfig/list_gis_stat_config.ftl";
	}

	/**
	 * <p>
	 * 框选周边可配置分页
	 * </p>
	 * 
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @param regionCode
	 * @param statType
	 *            0:框选 1:周边
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "statType", required = false) String statType,
			@RequestParam(value = "status", required = false) String status) throws Exception {
		if (page <= 0) {
			page = 1;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionCode", regionCode);
		params.put("statType", statType);
		params.put("status", status);
		EUDGPagination pagination = new EUDGPagination();
		pagination = gisStatConfigService.findGisStatConfigsPagination(page, rows, params);
		return pagination;
	}

	/**
	 * <p>
	 * 框选周边可配置添加
	 * <p>
	 * 
	 * @param session
	 * @param statCfgId
	 *            主键id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	public String add(HttpSession session, @RequestParam(value = "statCfgId", required = false) Long statCfgId,
			@RequestParam(value = "statType", required = false) String statType, ModelMap map) throws Exception {
		String redirect = "/map/gisstatconfig/edit_gis_stat_config.ftl";

		GisStatConfig gisStatConfig = new GisStatConfig();

		if (statCfgId != null) {
			gisStatConfig = gisStatConfigService.getGisStatConfigById(statCfgId);
			redirect = "/map/gisstatconfig/edit_gis_stat_config.ftl";
		}

		map.addAttribute("statType", statType);
		map.addAttribute("gisStatConfig", gisStatConfig);

		return redirect;
	}

	/**
	 * 保存框选周边
	 * 
	 * @param session
	 * @param map
	 * @param gisStatConfig
	 *            框选周边配置
	 * @param gisStatContConfigs
	 *            框选周边可配置内容
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Map<String, Object> save(HttpSession session, ModelMap map,
			@ModelAttribute(value = "gisStatConfig") GisStatConfig gisStatConfig,
			@RequestParam(value = "gisStatContConfigs", required = false) String gisStatContConfigs) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		boolean result = false;
		long statCfgId = gisStatConfig.getStatCfgId();

		if (statCfgId > 0) { // --更新
			gisStatConfig.setUpdater(userInfo.getUserId());
			gisStatConfig.setUpdated(new Date());
			gisStatConfig.setUpdaterName(userInfo.getPartyName());

			result = gisStatConfigService.updateGisStatConfig(gisStatConfig);

			if (result) {
				gisStatContConfigService.deleteGisStatContConfigs(statCfgId);
				this.addGisStatConfigs(gisStatConfig, gisStatContConfigs);
			}
		} else { // --保存
			gisStatConfig.setCreator(userInfo.getUserId());
			gisStatConfig.setCreated(new Date());
			gisStatConfig.setCreatorName(userInfo.getPartyName());
			gisStatConfig.setStatus("1");
			statCfgId = gisStatConfigService.saveGisStatConfig(gisStatConfig);

			if (statCfgId > 0) {
				gisStatContConfigService.deleteGisStatContConfigs(statCfgId);
				this.addGisStatConfigs(gisStatConfig, gisStatContConfigs);
				result = true;
			}
		}

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("result", result);
		return results;
	}

	public void addGisStatConfigs(GisStatConfig gisStatConfig, String gisStatContConfigs) throws Exception {
		if (StringUtils.isNotBlank(gisStatContConfigs)) {
			JSONArray array;
			try {
				array = new JSONArray(gisStatContConfigs);

				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					GisStatContConfig gisStatContConfig = new GisStatContConfig();
					gisStatContConfig.setStatCfgId(gisStatConfig.getStatCfgId());
					gisStatContConfig.setStatObjName(obj.getString("statObjName"));
					gisStatContConfig.setObjClass(obj.getString("objClass"));
					gisStatContConfig.setLayCfgId(Long.valueOf(obj.getString("layerCfgId")));
					gisStatContConfig.setDisplayOrder(obj.getInt("displayOrder"));
					gisStatContConfig.setStatus("1");
					gisStatContConfigService.saveGisStatContConfig(gisStatContConfig);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "statCfgId") long statCfgId, @RequestParam(value = "status") String status,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		GisStatConfig gisStatConfig = new GisStatConfig();
		gisStatConfig.setStatCfgId(statCfgId);
		gisStatConfig.setCreator(userInfo.getUserId());
		gisStatConfig.setCreated(new Date());
		gisStatConfig.setCreatorName(userInfo.getPartyName());
		gisStatConfig.setStatus(status);

		boolean result = gisStatConfigService.deleteGisStatConfig(gisStatConfig);

		if (result && "0".equals(status)) {
			result = gisStatContConfigService.deleteGisStatContConfigs(statCfgId);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 框选周边可配置详情
	 * 
	 * @param session
	 * @param statCfgId
	 *            主键id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpSession session, @RequestParam(value = "statCfgId") Long statCfgId, ModelMap map)
			throws Exception {
		GisStatConfig gisStatConfig = gisStatConfigService.getGisStatConfigById(statCfgId);
		map.addAttribute("gisStatConfig", gisStatConfig);
		return "/map/gisstatconfig/detail_gis_stat_config.ftl";
	}

	/**
	 * 根据id获取分类
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param statCfgId
	 *            主键id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getGisStatConfigsById")
	public Map<String, List<GisStatContConfig>> getGisStatConfigsById(HttpSession session, HttpServletRequest request,
			ModelMap map, @RequestParam(value = "statCfgId", required = false) long statCfgId,
			@RequestParam(value = "classification", required = false) String classification) throws Exception {
		Map<String, List<GisStatContConfig>> category = new LinkedHashMap<String, List<GisStatContConfig>>();

		List<GisStatContConfig> gisStatContConfigs = new ArrayList<GisStatContConfig>();
		GisStatConfig gisStatConfig = null;

		if (statCfgId > 0) {
			gisStatConfig = gisStatConfigService.getGisStatConfigById(statCfgId);
		}

		long gdcId = 0L;
		GisDataCfg gisDataCfg = new GisDataCfg();

		if (gisStatConfig != null) {
			if (StringUtils.isBlank(classification)) {
				classification = gisStatConfig.getClassification();
			}

			if ("1".equals(classification)) { // 分类
				gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, "未分类");

				if (gisStatContConfigs != null && gisStatContConfigs.size() > 0) {
					for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
						gdcId = gisStatContConfig.getLayCfgId();
						gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
						gisStatContConfig.setGisDataCfg(gisDataCfg);
					}
					
					category.put("未分类", gisStatContConfigs);
				}

				// 获取分类
				String catetories = gisStatConfig.getCategories();

				if (StringUtils.isNotBlank(catetories)) {
					String[] catetoriesArray = catetories.split(",");

					for (String objClass : catetoriesArray) {
						gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, objClass);

						for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
							gdcId = gisStatContConfig.getLayCfgId();
							gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
							gisStatContConfig.setGisDataCfg(gisDataCfg);
						}

						category.put(objClass, gisStatContConfigs);
					}
				}
			} else if ("0".equals(classification)) { // 不分类
				gisStatContConfigs = gisStatContConfigService.getGisStatContConfigs(statCfgId, null);

				for (GisStatContConfig gisStatContConfig : gisStatContConfigs) {
					gdcId = gisStatContConfig.getLayCfgId();
					gisDataCfg = menuConfigService.getGisDataCfgById(gdcId);
					gisStatContConfig.setGisDataCfg(gisDataCfg);
				}

				category.put("", gisStatContConfigs);
			}
		}

		return category;
	}

	/**
	 * 判断配置是否已有分类
	 * 
	 * @param session
	 * @param statCfgId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/hasClassification")
	public Map hasClassification(HttpSession session,
			@RequestParam(value = "statCfgId", required = false) long statCfgId,
			@RequestParam(value = "categroy", required = false) String categroy) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String catetories = gisStatConfigService.getCatagories(statCfgId);

		if (StringUtils.isBlank(catetories)) {
			result.put("check", false);
		} else {
			if (catetories.indexOf(categroy) != -1) {
				result.put("check", true);
			} else {
				result.put("check", false);
			}
		}

		return result;
	}

	/**
	 * 判断同一个地域下是否存在相同的框选周边配置
	 * 
	 * @param session
	 * @param regionCode
	 * @param bizType
	 * @param statType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkBizType")
	public Map checkBizType(HttpSession session,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "bizType", required = false) String bizType,
			@RequestParam(value = "statType", required = false) String statType) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionCode", regionCode);
		params.put("bizType", bizType);
		params.put("statType", statType);

		Integer count = gisStatConfigService.repeatCountForBizType(params);

		if (count > 0) {
			result.put("check", false);
		} else {
			result.put("check", true);
		}

		return result;
	}
}
