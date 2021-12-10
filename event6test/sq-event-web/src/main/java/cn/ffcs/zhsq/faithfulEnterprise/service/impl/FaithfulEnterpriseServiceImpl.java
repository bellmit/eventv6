package cn.ffcs.zhsq.faithfulEnterprise.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.zhsq.faithfulEnterprise.service.FaithfulEnterpriseService;
import cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise.FaithfulEnterprise;
import cn.ffcs.zhsq.mybatis.persistence.faithfulEnterprise.FaithfulEnterpriseMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/20.
 */
@Service("faithfulEnterpriseServiceImpl")
@Transactional
public class FaithfulEnterpriseServiceImpl implements FaithfulEnterpriseService {

	//注入守重企业模块dao
	@Autowired
	private FaithfulEnterpriseMapper enterpriseMapper;

	//注入地图标注服务
	@Autowired
	private IResMarkerService markerService;


	/**
	 * 企业总数
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public long countList(Map<String, Object> params) {
		Long count = enterpriseMapper.countList(params);
		return count;
	}

	/**
	 *
	 * @param page
	 * @param rows
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public List<FaithfulEnterprise> findEnterpriseList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<FaithfulEnterprise> list = enterpriseMapper.searchList(params,rowBounds);
		return list;
	}

	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<FaithfulEnterprise> list = enterpriseMapper.searchList(params,rowBounds);
		Long count = enterpriseMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count,list);
		return pagination;
	}

	@Override
	public boolean insert(HttpSession session, FaithfulEnterprise enterprise) {

		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		enterprise.setCreator(userInfo.getUserId()); //创建人Id
		enterprise.setCreatorName(userInfo.getUserName()); //创建人姓名
		enterprise.setUpdater(userInfo.getUserId()); //修改人Id
		enterprise.setUpdaterName(userInfo.getUserName()); //修改人姓名

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//创建时间
		if (enterprise.getCreatedTime() == null) {
			Date createdDate = new Date(); // 创建时间
			try {
				createdDate = DateUtils.convertStringToDate(sdf.format(createdDate), DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {

				e.printStackTrace();
			}
			enterprise.setCreatedTime(createdDate);// 创建时间
			enterprise.setUpdatedTime(createdDate); //修改时间
		}

		Boolean flag = false;
		if (enterpriseMapper.insert(enterprise)>0){
			ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.FAITHFUL_ENTERPRISE_MARKER_TYPE);
			resMarker.setResourcesId(enterprise.getEnterpriseId());
			resMarker.setX(enterprise.getLongitude().toString());
			resMarker.setY(enterprise.getLatitude().toString());
			markerService.saveOrUpdateResMarker(resMarker);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean update(HttpSession session, FaithfulEnterprise enterprise) {
		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		enterprise.setUpdater(userInfo.getUserId()); //修改人Id
		enterprise.setUpdaterName(userInfo.getUserName()); //修改人姓名

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//修改时间
		Date updatedDate = new Date();

		try {
			updatedDate = DateUtils.convertStringToDate(sdf.format(updatedDate), DateUtils.PATTERN_24TIME);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		enterprise.setUpdatedTime(updatedDate);// 修改时间

		Boolean flag = false;
		if(enterpriseMapper.update(enterprise) > 0){
			ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.FAITHFUL_ENTERPRISE_MARKER_TYPE);
			resMarker.setResourcesId(enterprise.getEnterpriseId());
			resMarker.setX(enterprise.getLongitude().toString());
			resMarker.setY(enterprise.getLatitude().toString());
			markerService.saveOrUpdateResMarker(resMarker);
			flag = true;
		}
		return flag;
	}

	@Override
	public FaithfulEnterprise findById(Long id) {
		FaithfulEnterprise enterprise = enterpriseMapper.findById(id);
		return enterprise;
	}

	@Override
	public boolean delete(FaithfulEnterprise enterprise) {
		Boolean flag = false;
		int rows = enterpriseMapper.delete(enterprise);
		if(rows > 0){
			flag = true;
		}
		return flag;
	}
}
