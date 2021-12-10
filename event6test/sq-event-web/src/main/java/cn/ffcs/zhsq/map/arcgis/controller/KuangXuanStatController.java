package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 2014-05-16 liushi add arcgis地图加载控制器
 * 
 * @author liushi
 * 
 */
@Controller
@RequestMapping(value = "/zhsq/map/kuangxuan/kuangxuanStat")
public class KuangXuanStatController extends ZZBaseController {

	@Resource(name = "kuangXuanStat")
	private IKuangXuanStatService kuangXuanStatService;

	@Autowired
	private IMenuConfigService menuConfigService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@RequestMapping(value = "/toKuangXuanPage")
	public String toZhouBianPage(HttpSession session, ModelMap map,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "geoString", required = false) String geoString,
			@RequestParam(value = "kuangxuanType", required = false) String kuangxuanType,
			@RequestParam(value = "homePageType", required = false) String homePageType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "gridId", required = false) String gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (elementsCollectionStr != null && !"".equals(elementsCollectionStr)) {
			map.put("elementsCollectionStr", elementsCollectionStr);
		} else {
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByKXServiceName(infoOrgCode, homePageType,
					kuangxuanType);
			if (gisDataCfg != null) {
				transGisDataCfgUrl(gisDataCfg, session);
				map.put("elementsCollectionStr", gisDataCfg.getElementsCollectionStr());
			} else {
				map.put("elementsCollectionStr", "");
			}

		}
		map.put("mapType", mapType);
		map.put("geoString", geoString);
		map.put("kuangxuanType", kuangxuanType);
		if (StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = userInfo.getInfoOrgCodeStr();
		}
		map.put("infoOrgCode", infoOrgCode);
		map.put("gridId", gridId);

		if ("kuangXuanStatOfXfs".equals(kuangxuanType)) {// 消防栓
			map.put("resTypeCode", "02010601");
		} else if ("kuangXuanStatOfWellLid".equals(kuangxuanType)) {// 井盖
			map.put("resTypeCode", "020107");
		} else if ("kuangXuanStatOfWaterCompany".equals(kuangxuanType)) {// 自来水公司
			map.put("markType", "0604");
		} else if ("kuangXuanStatOfWaterSource".equals(kuangxuanType)) {// 天然水源
			map.put("markType", "0603");
		} else if ("kuangXuanStatOfStreetlight".equals(kuangxuanType)) {// 路灯
			map.put("resTypeCode", "02010501");
		} else if ("kuangXuanStatOfGongShui".equals(kuangxuanType)) {// 供水
			map.put("resTypeCode", "020201");
		} else if ("kuangXuanStatOfGongRe".equals(kuangxuanType)) {// 供热
			map.put("resTypeCode", "020202");
		} else if ("kuangXuanStatOfRanQi".equals(kuangxuanType)) {// 燃气
			map.put("resTypeCode", "020203");
		} else if ("kuangXuanStatOfBusStation".equals(kuangxuanType)) {// 公交站
			map.put("resTypeCode", "02020401");
		} else if ("kuangXuanStatOfToilet".equals(kuangxuanType)) {// 公共厕所
			map.put("resTypeCode", "02010301");
		} else if ("kuangXuanStatOfPolice".equals(kuangxuanType)) {// 派出所
			map.put("resTypeCode", "020304");
		} else if ("kuangXuanStatOfFireHydrant".equals(kuangxuanType)) {// 新消防栓
			map.put("markType", "0601");
		}else if ("kuangXuanStatOfActiveFireTeamService".equals(kuangxuanType)) {// 现役消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		} else if ("kuangXuanStatOfObligationFireTeamService".equals(kuangxuanType)) {// 义务消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_YW);
		} else if ("kuangXuanStatOfVolunteerFireTeamService".equals(kuangxuanType)) {// 志愿消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_ZY);
		} else if ("kuangXuanStatOfFirePoolService".equals(kuangxuanType)) {// 消防水池
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_POOL);
		}

		return kuangXuanStatService.getKuangXuanPagePath(kuangxuanType);
	}

	@ResponseBody
	@RequestMapping(value = "/queryKuangXuanList", method = RequestMethod.POST)
	public EUDGPagination queryKuangXuanList(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "geoString", required = false) String geoString,
			@RequestParam(value = "kuangxuanType", required = false) String kuangxuanType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mapType", mapType);
		params.put("geoString", geoString);
		params.put("kuangxuanType", kuangxuanType);
		params.put("infoOrgCode", infoOrgCode);
		if ("kuangXuanStatOfRentRoom".equals(kuangxuanType)) {// 出租屋查询条件
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		} else if ("kuangXuanStatOfNonPublicOrg".equals(kuangxuanType)) {// 新经济组织查询条件
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			String gridName = (String) request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
		} else if ("kuangXuanStatOfOrganization".equals(kuangxuanType)) {// 新社会查询条件
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			String gridName = (String) request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
		} else if ("kuangXuanStatOfCorBase".equals(kuangxuanType)) { // 企业
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		} else if ("kuangXuanStatOfZdcs".equals(kuangxuanType)) {// 重点场所
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		} else if ("kuangXuanStatOfTotalPeople".equals(kuangxuanType)) {// 总人口
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfFloatPeople".equals(kuangxuanType)) {// 流动人口
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfXf".equals(kuangxuanType)) {// 信访
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfXds".equals(kuangxuanType)) {// 吸毒
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfJzs".equals(kuangxuanType)) {// 矫正
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfXss".equals(kuangxuanType)) {// 刑释解教
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfSfs".equals(kuangxuanType)) {// 上访
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfXjs".equals(kuangxuanType)) {// 邪教
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfXjs".equals(kuangxuanType)) {// 邪教
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfZjsbs".equals(kuangxuanType)) {// 重精神病
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfWxcys".equals(kuangxuanType)) {// 危险品
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfCzrys".equals(kuangxuanType)) {// 残障
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfSys".equals(kuangxuanType)) {// 失业
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfDbrys".equals(kuangxuanType)) {// 低保
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfDys".equals(kuangxuanType)) {// 党员
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfDys".equals(kuangxuanType)) {// 党员
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("kuangXuanStatOfTxs".equals(kuangxuanType)) {// 退休
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfJjyls".equals(kuangxuanType)) {// 居家养老
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("kuangXuanStatOfXfs".equals(kuangxuanType)) {// 消防栓
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfWellLid".equals(kuangxuanType)) {// 井盖
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfStreetlight".equals(kuangxuanType)) {// 路灯
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfGongShui".equals(kuangxuanType)) {// 供水
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfGongRe".equals(kuangxuanType)) {// 供热
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfRanQi".equals(kuangxuanType)) {// 燃气
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfBusStation".equals(kuangxuanType)) {// 公交站
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfToilet".equals(kuangxuanType)) {// 公共厕所
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfPolice".equals(kuangxuanType)) {// 派出所
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("resName", name.trim());
			}
		} else if ("kuangXuanStatOfFireHydrant".equals(kuangxuanType)) {// 新消防栓
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("kuangXuanStatOfActiveFireTeamService".equals(kuangxuanType)) {// 现役消防队
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		} else if ("kuangXuanStatOfObligationFireTeamService".equals(kuangxuanType)) {// 义务消防队
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		} else if ("kuangXuanStatOfVolunteerFireTeamService".equals(kuangxuanType)) {// 志愿消防队
			String name = (String) request.getParameter("name");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		} else if ("kuangXuanStatOfFirePoolService".equals(kuangxuanType)) {// 消防水池
			String name = (String) request.getParameter("name");
			String address = (String) request.getParameter("address");
			String operateStatus = (String) request.getParameter("operateStatus");
			params.put("name", name.trim());
			params.put("address", address.trim());
			params.put("operateStatus", operateStatus);
		}
		return kuangXuanStatService.statOfKuangXuanList(pageNo, pageSize, params);
	}

	public void transGisDataCfgUrl(GisDataCfg obj, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if (obj != null) {
			String menuListUrl = obj.getMenuListUrl();
			String menuSummaryUrl = obj.getMenuSummaryUrl();
			String menuDetailUrl = obj.getMenuDetailUrl();
			String elementsCollectionStr = obj.getElementsCollectionStr();
			String callBack = obj.getCallBack();
			if (menuListUrl != null && !"".equals(menuListUrl)) {
				String menuListUrlDomain = menuListUrl.split("/")[0];
				String actMenuListUrlDomain = CommonFunctions.getDomain(session, menuListUrlDomain);
				
				if (StringUtils.isBlank(actMenuListUrlDomain)) {
					actMenuListUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN", menuListUrlDomain,
							IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}
				
				menuListUrlDomain = "\\" + menuListUrlDomain;
				menuListUrl = menuListUrl.replaceFirst(menuListUrlDomain, actMenuListUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
				callBack = callBack.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
			}
			if (menuSummaryUrl != null && !"".equals(menuSummaryUrl)) {
				String menuSummaryUrlDomain = menuSummaryUrl.split("/")[0];
				String actMenuSummaryUrlDomain = CommonFunctions.getDomain(session, menuSummaryUrlDomain);
				
				if (StringUtils.isBlank(actMenuSummaryUrlDomain)) {
					actMenuSummaryUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
							menuSummaryUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}
				
				menuSummaryUrlDomain = "\\" + menuSummaryUrlDomain;
				menuSummaryUrl = menuSummaryUrl.replaceFirst(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuSummaryUrlDomain,
						actMenuSummaryUrlDomain);
				callBack = callBack.replaceAll(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
			}
			if (menuDetailUrl != null && !"".equals(menuDetailUrl)) {
				String menuDetailUrlDomain = menuDetailUrl.split("/")[0];
				String actMenuDetailUrlDomain = CommonFunctions.getDomain(session, menuDetailUrlDomain);
				
				if (StringUtils.isBlank(actMenuDetailUrlDomain)) {
					actMenuDetailUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
							menuDetailUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}
				
				menuDetailUrlDomain = "\\" + menuDetailUrlDomain;
				menuDetailUrl = menuDetailUrl.replaceFirst(menuDetailUrlDomain, actMenuDetailUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuDetailUrlDomain,
						actMenuDetailUrlDomain);
				callBack = callBack.replaceAll(menuDetailUrlDomain, actMenuDetailUrlDomain);
			}
			obj.setMenuListUrl(menuListUrl);
			obj.setMenuSummaryUrl(menuSummaryUrl);
			obj.setMenuDetailUrl(menuDetailUrl);
			obj.setElementsCollectionStr(elementsCollectionStr);
			obj.setCallBack(callBack);
		}
	}

}
