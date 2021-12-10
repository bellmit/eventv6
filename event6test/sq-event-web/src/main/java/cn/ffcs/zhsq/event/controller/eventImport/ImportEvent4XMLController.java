package cn.ffcs.zhsq.event.controller.eventImport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * xml格式归档事件导入
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value = "/zhsq/event/importXml")
public class ImportEvent4XMLController extends ImportEventAbstractController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//表头字段转换
	private static final Map<String, String> EVENT_KEY_MAP = new HashMap<String, String>() {
		{
			put("dico", "infoOrgCode");
			put("itil", "eventName");
			put("evds", "content");
			put("evad", "occurred");
			put("acte", "happenTimeStr");
			put("cpna", "contactUser");
			put("cpdh", "tel");
			put("catm", "createTimeStr");
			put("note", "remark");
			put("evrt", "finTimeStr");
		}
	};
	
	/**
	 * 事件数据导入
	 * @param session
	 * @param importFile
	 * @param params
	 * 			_defaultInfoOrgCode	默认地域编码
	 * 			_defaultOrgName		默认组织名称
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/importData") 
	public String importData(HttpSession session,
			@RequestParam(value = "importFile") MultipartFile importFile,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		final MultipartFile file = importFile;
		final Map<String, Object> extraParams = params;
		 
		Thread thread = new Thread() {
			public void run() {
				List<Map<String, Object>> eventMapList = new ArrayList<Map<String, Object>>();
				int tipMsgType = 0;
				Long threadId = this.getId();
				
				try {
					eventMapList = changeXmlToMap(file, threadId, extraParams);
				} catch (JDOMException e) {
					tipMsgType = 1;
					e.printStackTrace();
				} catch (IOException e) {
					tipMsgType = 4;
					e.printStackTrace();
				}
				
				importResultMap.put(threadId+"_tipMsgType", tipMsgType);
				importResultMap.put(threadId+"_isActive", true);//用于区分线程变量是被清空了，还是未解析完成
				
				handleEvent(eventMapList, threadId);
			}
		};
		
		thread.start();
		
		//重定向，由于避免刷新页面后，事件重复导入
		return "redirect:/zhsq/event/importXml/toIndex.jhtml?threadId="+thread.getId();
	}
	
	/**
	 * 将xml文件转换为事件map
	 * @param importFile			导入文件
	 * @param threadId				线程id
	 * @param extraParams
	 * 			_defaultInfoOrgCode	默认地域编码
	 * 			_defaultOrgName		默认组织名称
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	private List<Map<String, Object>> changeXmlToMap(MultipartFile importFile, Long threadId, Map<String, Object> extraParams) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		List<Map<String, Object>> eventMapList = new ArrayList<Map<String, Object>>();
		
		document = builder.build(importFile.getInputStream());
		
		if(document != null) {
			Element rootElement = document.getRootElement();//<table>
			if(rootElement != null) {
				Map<String, Object> eventMap = null;
				
				Element rows = rootElement.getChild("rows");//<rows>
				if(rows != null) {
					List<Element> rowList = rows.getChildren(),	//<row>
								  itemList = null;
					Attribute itemAttr = null;
					
					if(rowList != null) {
						int total = rowList.size();
						//为了防止解析过慢，导致页面展示提前结束
						if(CommonFunctions.isBlank(importResultMap, threadId+"_total") && total > 1) {
							importResultMap.put(threadId+"_total", total);
						}
						
						for(Element row : rowList) {
							if(row != null) {
								itemList = row.getChildren();//<item>
								if(itemList != null) {
									eventMap = new HashMap<String, Object>();
									
									for(Element item : itemList) {
										itemAttr = item.getAttribute("name");
										
										if(itemAttr != null) {
											String attrValue = itemAttr.getValue().trim();
											
											if(EVENT_KEY_MAP.containsKey(attrValue)) {
												eventMap.put(EVENT_KEY_MAP.get(attrValue),  item.getTextTrim());
											}
										}
									}
									
									if(!eventMap.isEmpty()) {
										//数据调整
										alterEventMap(eventMap, extraParams);
										
										eventMapList.add(eventMap);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return eventMapList;
	}
	
	/**
	 * 个性处理导入数据
	 * @param eventMap
	 * @param extraParams
	 * 			_defaultInfoOrgCode	默认地域编码
	 * 			_defaultOrgName		默认组织名称
	 */
	private void alterEventMap(Map<String, Object> eventMap, Map<String, Object> extraParams) {
		if(eventMap != null && !eventMap.isEmpty()) {
			if(CommonFunctions.isNotBlank(eventMap, "happenTimeStr")) {
				String happenTimeStr = eventMap.get("happenTimeStr").toString();
				happenTimeStr = happenTimeStr.replace("T", " ").replace("Z", "");
				eventMap.put("happenTimeStr", happenTimeStr);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "createTimeStr")) {
				String createTimeStr = eventMap.get("createTimeStr").toString();
				createTimeStr = createTimeStr.replace("T", " ").replace("Z", "");
				eventMap.put("createTimeStr", createTimeStr);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "finTimeStr")) {
				String finTimeStr = eventMap.get("finTimeStr").toString();
				finTimeStr = finTimeStr.replace("T", " ").replace("Z", "");
				eventMap.put("finTimeStr", finTimeStr);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "infoOrgCode")) {
				String infoOrgCode = eventMap.get("infoOrgCode").toString();
				int len = 0;
				
				do {
					len = infoOrgCode.length();
					
					if(len > 6 && infoOrgCode.endsWith("000")) {
						infoOrgCode = infoOrgCode.substring(0, len - 3);
					} else if(len <= 6 && infoOrgCode.endsWith("00")) {
						infoOrgCode = infoOrgCode.substring(0, len - 2);
					} else {
						break;
					}
				} while(true);
				
				eventMap.put("infoOrgCode", infoOrgCode);
			} else {//未设置地域信息时，才启用默认设置
				if(CommonFunctions.isNotBlank(extraParams, "_defaultInfoOrgCode")) {
					eventMap.put("infoOrgCode", extraParams.get("_defaultInfoOrgCode"));
				}
				
				if(CommonFunctions.isBlank(eventMap, "orgId") && CommonFunctions.isNotBlank(extraParams, "_defaultOrgName")) {
					OrgSocialInfoBO orgInfo = findOrgByName(extraParams.get("_defaultOrgName").toString());
					if(orgInfo != null) {
						eventMap.put("orgId", orgInfo.getOrgId());
					}
				}
			}
		}
	}

	@Override
	public String exportFailEventContent(List<Map<String, Object>> eventMapList) {
        Map<String, String> eventKeyMap = new HashMap<String, String>() {
			{
				putAll(EVENT_KEY_MAP);
				put("msg", "msg");
			}
		};
		
        StringBuffer exportContent = new StringBuffer("");
        
        if(eventMapList != null && eventMapList.size() > 0) {
        	String keyObjStr = null;
        	
        	exportContent.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        	exportContent.append("<table name=\"CITY_EVENT\">\n");
			
        	exportContent.append("	<rows rownum=\"").append(eventMapList.size()).append("\">\n");
        	
			for(Map<String, Object> eventMap : eventMapList) {
				exportContent.append("		<row>\n");
				
				for(String key : eventKeyMap.keySet()) {
					keyObjStr = eventKeyMap.get(key).toString();
					
					if(CommonFunctions.isNotBlank(eventMap, keyObjStr)) {
						exportContent.append("			<item name=\"").append(key).append("\">")
									 .append("<![CDATA[").append(eventMap.get(keyObjStr)).append("]]>")
									 .append("</item>\n");
					}
				}
				
				exportContent.append("		</row>\n");
			}
    		
			exportContent.append("	</rows>\n");
        	exportContent.append("</table>\n");
        }
        
        return exportContent.toString();
	}

	@Override
	public String fetchIndexUrl() {
		return "/zzgl/event/eventImport/import_event_xml.ftl";
	}

	@Override
	public String fetchExportMsg() {
		return "请使用xml格式的文件(*.xml)";
	}

	@Override
	public String fetchExportName() {
		String exportName = "";
		
		try {
			exportName = URLEncoder.encode(EXPORT_EXCEL_NAME, "UTF-8") + ".xml";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return exportName;
	}
	
	/**
	 * 依据组织域名称获取组织信息
	 * @param orgName	组织名称
	 * @return
	 */
	private OrgSocialInfoBO findOrgByName(String orgName) {
		StringBuffer sql = new StringBuffer("");
		OrgSocialInfoBO orgInfo = null;
		
		sql.append(" SELECT T1.ORG_ID, T1.ORG_NAME, T1.ORG_CODE ")
		   .append(" FROM T_DC_ORG_SOCIAL_INFO T1 ")
		   .append(" WHERE T1.STATUS = '001' AND T1.ORG_NAME = ? ");
		
		List<OrgSocialInfoBO> orgEntityInfoList = jdbcTemplate.query(sql.toString(), new Object[]{orgName}, new RowMapper<OrgSocialInfoBO>() {
			@Override
			public OrgSocialInfoBO mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrgSocialInfoBO orgInfo = new OrgSocialInfoBO();
				
				orgInfo.setOrgId(rs.getLong("ORG_ID"));
				orgInfo.setOrgName(rs.getString("ORG_NAME"));
				orgInfo.setOrgCode(rs.getString("ORG_CODE"));
				
				return orgInfo;
			}
		});
		
		if(orgEntityInfoList != null && orgEntityInfoList.size() > 0) {
			orgInfo = orgEntityInfoList.get(0);
		}
		
		return orgInfo;
	}
}
