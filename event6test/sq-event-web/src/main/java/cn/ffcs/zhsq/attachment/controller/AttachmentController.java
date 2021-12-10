package cn.ffcs.zhsq.attachment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.file.service.IFileTransferService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 附件
 * @author chenyu3
 *
 */
@Controller
@RequestMapping(value="/zhsq/att")
public class AttachmentController extends ZZBaseController{

	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IFileTransferService fileTransferService;
	
	@RequestMapping(value="/test/index")
	public String testIndex(HttpSession session, ModelMap map) {
		return "/zzgl/test/index.ftl";
	}
	
	@RequestMapping(value="/test/save")
	public String save(HttpSession session, ModelMap map,@RequestParam(value = "attachmentId") Long[] ids) {
		attachmentService.updateBizId(1l, "2", ids);
		return "/zzgl/test/index.ftl";
	}
	
	@RequestMapping(value="/test/imgShow")
	public String imgShow(HttpSession session, ModelMap map) {
		List<Attachment> list = attachmentService.findByBizId(1L,"2");
		map.addAttribute("list", list);
		return "/zzgl/test/imgshow.ftl";
	}
	
	/**
	 * 获取附件列表
	 * @param bizId				业务id
	 * @param attachmentType	业务类型
	 * @param eventSeq			1：处理前，2：处理中,3:处理后
	 * @param attachmentIds		额外的附件id，以英文逗号分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method= RequestMethod.POST)
	public List<Attachment> getList(@RequestParam(value = "bizId", required = false, defaultValue = "-1") Long bizId,
			@RequestParam(value = "attachmentType", required = false) String attachmentType,
			@RequestParam(value = "eventSeq", required = false) String eventSeq,
			@RequestParam(value = "attachmentIds", required = false) String attachmentIds) {
		
		List<Attachment> list = null;
		
		if(bizId != null && bizId > 0 && StringUtils.isNotBlank(attachmentType)) {
			list = attachmentService.findByBizId(bizId, attachmentType, eventSeq);
		}
		
		if(StringUtils.isNotBlank(attachmentIds)) {
			String[] attachmentIdArray = attachmentIds.split(",");
			Set<Long> attrIdSet = new HashSet<Long>();//用于排除附件重复添加
			Long attrId = -1L;
			Attachment attachment = null;
			
			if(list == null) {
				list = new ArrayList<Attachment>();
			} else {
				for(Attachment attr : list) {
					attrIdSet.add(attr.getAttachmentId());
				}
			}
			
			for(String attrIdStr : attachmentIdArray) {
				attrId = -1L;
				
				try {
					attrId = Long.valueOf(attrIdStr);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(attrId > 0 && !attrIdSet.contains(attrId)) {
					attrIdSet.add(attrId);
					attachment = attachmentService.findById(attrId);
					
					if(attachment != null) {
						list.add(attachment);
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 跨域请求附件列表
	 * @param bizId				业务id
	 * @param attachmentType	业务类型
	 * @param callback			jsonp回调
	 * @param eventSeq			1：处理前，2：处理中,3:处理后
	 * @param attachmentIds		额外的附件id，以英文逗号分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getListForJsonp")
	public String getListForJsonp(@RequestParam(value = "bizId", required = false, defaultValue="-1") Long bizId,
			@RequestParam(value = "attachmentType", required = false) String attachmentType,
			@RequestParam(value = "callback") String callback,
			@RequestParam(value = "eventSeq", required = false) String eventSeq,
			@RequestParam(value = "attachmentIds", required = false) String attachmentIds) {
		List<Attachment> list = this.getList(bizId, attachmentType, eventSeq, attachmentIds);
		StringBuffer json = new StringBuffer("");
		
		if(list != null && list.size() > 0) {
			for(Attachment att : list) {
				json.append(",{");
				json.append("\"attachmentId\":").append(att.getAttachmentId()).append(",");
				json.append("\"attachmentType\":\"").append(att.getAttachmentType()).append("\",");
				json.append("\"bizId\":").append(att.getBizId()).append(",");
				json.append("\"title\":\"").append(att.getTitle()).append("\",");
				json.append("\"filePath\":\"").append(att.getFilePath()).append("\",");
				json.append("\"fileName\":\"").append(att.getFileName()).append("\",");
				json.append("\"fileSize\":\"").append(att.getFileSize()).append("\",");
				json.append("\"imgWidth\":\"").append(att.getImgWidth()).append("\",");
				json.append("\"imgHeight\":\"").append(att.getImgHeight()).append("\",");
				json.append("\"eventSeq\":\"").append(att.getEventSeq()).append("\"");
				json.append("}");
			}
			json = new StringBuffer(json.substring(1));
		}
		
		return callback+"(["+json+"])";
	}
	
	@ResponseBody
	@RequestMapping(value="/slideShow", method= RequestMethod.POST)
	public Map<String, Object> slideShow(HttpSession session, @RequestParam(value = "bizId") Long bizId,@RequestParam(value = "attachmentType") String attachmentType) {
		List<Attachment> list = attachmentService.findByBizId(bizId,attachmentType);
		String filedomain = App.IMG.getDomain(session);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(list!=null && list.size()>0){
			resultMap.put("success", 1);
			resultMap.put("filedomain", filedomain);
			resultMap.put("list", list);
		}else{
			resultMap.put("success", 0);
		}
		
		return resultMap;
	}
	
	/**
	 * 视频播放跳转方法
	 * @param session
	 * @param path			视频/音频路径
	 * @param attachmentId	附件id
	 * @param videoType
	 * 			1	视频
	 * 			2	音频
	 * 			3	amr格式音频
	 * @param isRetry		jplayer播放失败时，改值设置为true
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toSeeVideo")
	public String toSeeVideo(HttpSession session,
			@RequestParam(value = "path",required=false) String path, 
			@RequestParam(value = "attachmentId",required=false) Long attachmentId,
			@RequestParam(value = "videoType",required=false,defaultValue="1") String videoType,
			@RequestParam(value = "isRetry",required=false, defaultValue="false") boolean isRetry, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		map.putAll(params);
		String downPath = App.IMG.getDomain(session),//支持多域名
				  msg="",
				  forwardPath = "/component/seeVideo_jp.ftl";;
		
		if(attachmentId != null && attachmentId > 0) {
			Attachment attr = attachmentService.findById(attachmentId);
			
			path = downPath + attr.getFilePath();
			
			map.addAttribute("attachmentId", attachmentId);
		} 
		
		if(StringUtils.isNotBlank(path)) {
			String fileSuffix = path.substring(path.lastIndexOf(".") + 1).trim();
			
			if(fileSuffix.equalsIgnoreCase("amr") || fileSuffix.equalsIgnoreCase("avi")) {
				try {
					path = downPath + fileTransferService.fileTransfer(path);
				} catch (Exception e) {
					msg = e.getMessage();
					e.printStackTrace();
				}
			}
		}
		
		map.addAttribute("path", path);
		map.addAttribute("videoType", videoType);
		map.addAttribute("msg", msg);
		
		if("3".equals(videoType)) {
			forwardPath = "/component/seeVideoAmr.ftl";
		} else if(isRetry) {
			forwardPath = "/component/seeVideo.ftl";
		}
		
		return forwardPath;
	}
	
}
