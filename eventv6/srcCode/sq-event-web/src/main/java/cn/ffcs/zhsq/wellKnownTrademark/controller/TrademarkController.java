package cn.ffcs.zhsq.wellKnownTrademark.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.ImageUtils;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark.Trademark;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.wellKnownTrademark.service.TrademarkService;

/**
 * Created by 张天慈 on 2017/12/15.
 */
@Controller("trademarkController")
@RequestMapping("/zhsq/szzg/trademark")
public class TrademarkController extends ZZBaseController{

	//注入商标信息服务模块
	@Autowired
	private TrademarkService trademarkService;

	//注入文件上传服务
	@Autowired
	FileUploadService fileUploadService;

	//文件上传路径
	private static final String FILE_UPLOAD_EXTERIOR = "trademark_img";
	private final String sPath = "/upload/UploadPhoto/";

	/**
	 * 数据列表页面
	 */
	@RequestMapping(value = "/index")
	public Object index(HttpSession session, ModelMap map, HttpServletRequest request){

		//获取当前登录用户的信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		//获取默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));

		//BD工程域的域名
		map.addAttribute("IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));
		map.put("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "szzg/wellKnownTrademark/list_trademark.ftl";
	}

	/**
	 * 分页显示数据
	 */
	@ResponseBody
	@RequestMapping(value = "/listData",method = RequestMethod.POST)
	public Object listData(HttpSession session, Trademark trademark, ModelMap map, int page, int rows){

		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;

		Map<String,Object> params = new HashedMap();
		params.put("gridCode",trademark.getGridCode());
		params.put("unitName",trademark.getUnitName());
		params.put("brand",trademark.getBrand());
		params.put("thatTimeDate",trademark.getThatTimeDate());
		params.put("scope",trademark.getScope());

		EUDGPagination pagination = trademarkService.searchList(page,rows,params);

		return pagination;

	}

	/**
	 * 新增商标信息
	 */
	@RequestMapping("add")
	public Object addTrademark(HttpSession session,ModelMap map){

		Trademark trademark = new Trademark();


		//获取默认网格信息
		Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
		trademark.setGridId((Long)defaultGridInfo.get(KEY_START_GRID_ID));
		trademark.setGridCode((String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		trademark.setGridName((String)defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("trademark",trademark);
		//标注地理位置
		map.addAttribute("SQ_ZHSQ_EVENT_URL",App.EVENT.getDomain(session)); //Event项目路径
		map.addAttribute("module",ConstantValue.WELL_KNOWN_TRADEMARK_MARKER_TYPE); //模块，自定义常量
		map.addAttribute("markerOperation",ConstantValue.ADD_MARKER); //添加地图标注


		return "szzg/wellKnownTrademark/add_trademark.ftl";
	}



	/**
	 * 修改商标信息
	 */
	@RequestMapping("/edit")
	public Object update(ModelMap map,HttpSession session,Long id){
		Trademark trademark = trademarkService.findById(id);
		map.addAttribute("trademark",trademark);

		//图片展示
		map.put("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));

		//标注地理位置
		map.addAttribute("SQ_ZHSQ_EVENT_URL",App.EVENT.getDomain(session)); //GMIS项目路径
		map.addAttribute("module",ConstantValue.WELL_KNOWN_TRADEMARK_MARKER_TYPE); //模块，自定义常量
		map.addAttribute("markerOperation",ConstantValue.EDIT_MARKER); //添加标注操作
		return "szzg/wellKnownTrademark/add_trademark.ftl";
	}

	/**
	 * 保存新增或修改的商标信息
	 */
	@ResponseBody
	@RequestMapping(value = "/save",produces = {"text/plain;charset=UTF-8"})
	public Object saveOrUpdate(HttpServletRequest request,HttpSession session,Trademark trademark){

		//数据是否保存成功，success OR fail
		String result = "fail";
		Map<String,Object> resultMap = new HashMap();


		try {
			//@RequestParam(value = "upPhoto", required = false) MultipartFile upPhoto,
			MultipartFile upPhoto=null;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			// 设置编码
			commonsMultipartResolver.setDefaultEncoding("utf-8");
			// 判断是否有文件上传
			if (commonsMultipartResolver.isMultipart(request)) {
				//有文件上传
				//将request变成多部分request
				MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
				//获取multiRequest 中所有的文件名
				Iterator<String> iter=multiRequest.getFileNames();
				while (iter.hasNext()) {
					upPhoto = multiRequest.getFile(iter.next().toString());//(String) iter.next()
				}
			}
			if (upPhoto != null && !upPhoto.isEmpty() && upPhoto.getSize() != 0) {
				//byte[] multipartFileBytes = upPhoto.getBytes();
				// 上传文件先放入临时文件夹
				File localFile =getTempFile(request, upPhoto);
				String filePath = fileUploadService.uploadSingleFile(localFile.getName(),
						getBytes(localFile.getAbsolutePath()), ConstantValue.RESOURSE_DOMAIN_KEY,
						FILE_UPLOAD_EXTERIOR);
				localFile.delete();//删除临时文件
				trademark.setTrademarkImg(filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}



		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//认定时间
		Date thatTimeDate = null;
		if(request.getParameter("thatTime") != ""){
			try {
				thatTimeDate = sdf.parse(request.getParameter("thatTime"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}


		//获取商标信息
		trademark.setUnitName(request.getParameter("unitName"));
		trademark.setThatTimeDate(thatTimeDate);
		trademark.setBrand(request.getParameter("brand"));
		trademark.setScope(request.getParameter("scope"));
		trademark.setUnitAddress(request.getParameter("unitAddress"));
		trademark.setLatitude(request.getParameter("latitude"));
		trademark.setLongitude(request.getParameter("longitude"));
		trademark.setRegionCode(request.getParameter("gridCode"));

		//判断商标信息是否存在
		if(trademark.getTrademarkId() == null){
			//商标信息不存在，新增商标信息
			Boolean flag = trademarkService.insert(session,trademark);
			if(flag == true){
				result = "success";
			}
		}else{//存在时，修改设备信息
			Boolean flag = trademarkService.update(session,trademark);
			if(flag == true){
				result = "success";
			}
		}
		resultMap.put("result",result);
		return result+"";
	}


	/**
	 * 删除商标信息
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(Trademark trademark,HttpSession session){
		Map<String,Object> resultMap = new HashMap();
		String result = "fail";

		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		trademark.setUpdaterId(userInfo.getUserId()); //删除人Id
		trademark.setUpdaterName(userInfo.getUserName()); //删除人姓名

		boolean flag = trademarkService.delete(trademark);
		if(flag == true){
			result = "success";
		}
		resultMap.put("result",result);
		return resultMap;
	}

	/**
	 * 商标详情页面
	 */
	@RequestMapping("/detail")
	public Object detail(HttpSession session,Long id,ModelMap map,String showClose){
		//图片展示
		map.addAttribute("RESOURSE_SERVER_PATH",App.IMG.getDomain(session));

		//event调用，隐藏关闭按钮
		map.addAttribute("showClose",showClose);

		if( id != null){
			Trademark trademark = trademarkService.findById(id);
			map.addAttribute("trademark",trademark);
		}

		return "szzg/wellKnownTrademark/detail_trademark.ftl";
	}

	/**
	 * 生成临时文件
	 * @param request
	 * @param upPhoto
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private File getTempFile(HttpServletRequest request,MultipartFile upPhoto) throws IllegalStateException, IOException{
		String fileName = upPhoto.getOriginalFilename();
		String imgUploadPath = request.getSession().getServletContext().getRealPath("/") + sPath;
		String suffix = fileName.substring(fileName.indexOf("."), fileName.length());
		String newFileName = System.currentTimeMillis() + suffix;
		String localFileName = imgUploadPath + newFileName;
		File localFile = new File(localFileName);
		upPhoto.transferTo(localFile);
		ImageUtils.scale2(localFileName, localFileName, 117, 160, true);
		return localFile;
	}


	/**
	 * 获得指定文件的byte数组
	 * @param filePath
	 * @return
	 */
	public static byte[] getBytes(String filePath){
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}


}
