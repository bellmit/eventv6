package cn.ffcs.zhsq.map.sanitationtrunck.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.sanitationtrunck.SanitationTrunckService;
import cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck.SanitationTrunck;

@Controller
@RequestMapping(value="/zhsq/map/sanitationtrunck")
public class SanitationTrunckController extends ZZBaseController {

	@Value("${syn_sanitationtrunck}")
	private String synUrl;
	
	@Autowired
	private SanitationTrunckService sanitationTrunckService;
	
	@RequestMapping("synData")
	public @ResponseBody Object synData(){
		try {
			
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			JSONObject json = HttpUtil.invokeOutInterface(synUrl, "POST", "", headers, null);
			
			int returnCode = json.getInt("returnCode");
			if(returnCode == 0){
				String result = json.getString("result");
				JSONArray array = JSONArray.fromObject(result);
				if(!array.isEmpty() && array.size() > 0){
					@SuppressWarnings("unchecked")
					List<SanitationTrunck> list = JsonHelper.getObjectList(array.toString(), SanitationTrunck.class);
					sanitationTrunckService.sysSanitatinTrunckInfo(list);
				}
			}else{
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
		return "success";
	}
	
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "unitCode", required=true) String unitCode,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "isCross", required=false) String isCross) {
		map.addAttribute("isCross", isCross);
//		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
//		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
//		map.put("gridId", gridId);
		map.put("unitCode", unitCode);
		map.put("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/sanitationtrunck/sanitationtrunck_list.html";
	}
	
	@RequestMapping(value="/pageList")
	public @ResponseBody Object pageList(HttpServletRequest request,
			HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "carcode", required = false) String carcode,
			@RequestParam(value = "carnumber", required = false) String carnumber) {
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			
			if(StringUtils.isNotEmpty(carcode)){
				params.put("carcode", carcode);
			}
			
			if(StringUtils.isNotEmpty(carnumber)){
				params.put("carnumber", carnumber);
			}
			
			if(StringUtils.isNotEmpty(id)){
				params.put("id", id);
			}
			
			params.put("validity", "0");//有效数据
			
			//unitCode={huanwei:环卫车辆,zhatu:渣土车,zhifa:执法车}
			String unitCode = request.getParameter("unitCode");
			if(StringUtils.isNotEmpty(unitCode)){
				params.put("unitCode", unitCode);
			} else {
				params.put("unitCode", "huanwei");//环卫车辆类型
			}
			
			EUDGPagination pagination = sanitationTrunckService.searchList(page, rows, params);
			
			return pagination;
		} catch (Exception e) {
			e.printStackTrace();
			return new EUDGPagination();
		}
	}
	
	@RequestMapping(value="/detail")
	public String detail(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "id", required=true) String id,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "isCross", required=false) String isCross) {
		map.addAttribute("isCross", isCross);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.put("gridId", gridId);
		map.put("elementsCollectionStr", elementsCollectionStr);
		SanitationTrunck data = sanitationTrunckService.searchById(id);
		map.put("data", data);
		return "/map/arcgis/standardmappage/sanitationtrunck/sanitationtrunck_detail.html";
	}
	
	@RequestMapping("/trunckDetailData")
	@ResponseBody
	public Object trunckDetailData(HttpServletRequest request,
								@RequestParam("id")String id){
		SanitationTrunck data = sanitationTrunckService.searchById(id);
		return data;
	}
}
