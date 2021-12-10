package cn.ffcs.zhsq.administrationPenalty.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.administrationPenalty.service.AdministrationPenaltyService;
import cn.ffcs.zhsq.mybatis.domain.administrationPenalty.AdministrationPenalty;
import cn.ffcs.zhsq.mybatis.persistence.administrationPenalty.AdministrationPenaltyMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/5.
 */
@Service("penaltyServiceImpl")
@Transactional
public class AdministrationPenaltyServiceImpl implements AdministrationPenaltyService {

	//注入行政处罚模块
	@Autowired
	private AdministrationPenaltyMapper penaltyMapper;

	/**
	 *
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public long countList(Map<String, Object> params) {
		long count = penaltyMapper.countList(params);
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
	public List<AdministrationPenalty> findPenaltyList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<AdministrationPenalty> list = penaltyMapper.searchList(params,rowBounds);
		return list;
	}

	/**
	 *
	 * @param page
	 * @param rows
	 * @param params 查询参数
	 * @return
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
		List<AdministrationPenalty> list = penaltyMapper.searchList(params,rowBounds);
		Long count = penaltyMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count,list);
		return pagination;
	}

	/**
	 *
	 * @param session
	 * @param penalty
	 * @return
	 */
	@Override
	public boolean insert(HttpSession session, AdministrationPenalty penalty) {

		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		penalty.setCreator(userInfo.getUserId());
		penalty.setCreatorName(userInfo.getUserName());
		penalty.setUpdater(userInfo.getUserId());
		penalty.setUpdaterName(userInfo.getUserName());

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//创建时间
		if(penalty.getCreatedTime() == null){
			Date createDate = new Date();
			try {
				createDate = DateUtils.convertStringToDate(sdf.format(createDate), DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {

				e.printStackTrace();
			}
			penalty.setCreatedTime(createDate);//创建时间
			penalty.setUpdatedTime(createDate);//修改时间
		}

		Boolean flag = false;
		if(penaltyMapper.insert(penalty) > 0){
			/*ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.ADMINISTRATIONPENALTY_MARKER_TYPE);
			resMarker.setResourcesId(penalty.getRegistrationId());
			resMarker.setX(penalty.getLongitude().toString());
			resMarker.setY(penalty.getLatitude().toString());
			resMarkerService.saveOrUpdateResMarker(resMarker);*/
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean update(HttpSession session, AdministrationPenalty penalty) {

		//获取登录用户的信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		penalty.setUpdater(userInfo.getUserId());
		penalty.setUpdaterName(userInfo.getUserName());

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//修改时间
		Date updatedDate = new Date();

		try {
			updatedDate = DateUtils.convertStringToDate(sdf.format(updatedDate), DateUtils.PATTERN_24TIME);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		penalty.setUpdatedTime(updatedDate);

		Boolean flag = false;
		if(penaltyMapper.update(penalty) > 0){
			/*ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.ADMINISTRATIONPENALTY_MARKER_TYPE);
			resMarker.setResourcesId(penalty.getRegistrationId());
			resMarker.setX(penalty.getLongitude().toString());
			resMarker.setY(penalty.getLatitude().toString());
			resMarkerService.saveOrUpdateResMarker(resMarker);*/
			flag = true;
		}

		return flag;
	}

	@Override
	public AdministrationPenalty findById(Long id) {
		AdministrationPenalty penalty = penaltyMapper.findById(id);

		return penalty;
	}

	@Override
	public boolean delete(HttpSession session, AdministrationPenalty penalty) {
		Boolean flag = false;
		//获取登录用户的信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		penalty.setUpdater(userInfo.getUserId());
		penalty.setUpdaterName(userInfo.getUserName());
		if(penaltyMapper.delete(penalty) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public List<AdministrationPenalty> findPenaltyByYM(Map<String,Object> param) {
		List<AdministrationPenalty> list = penaltyMapper.findPenaltyByYM(param);
		return list;
	}

	@Override
	public List<Map<String, Object>> findChart12Month(Map<String, Object> param) {
		return penaltyMapper.findChart12Month(param);
	}
}
