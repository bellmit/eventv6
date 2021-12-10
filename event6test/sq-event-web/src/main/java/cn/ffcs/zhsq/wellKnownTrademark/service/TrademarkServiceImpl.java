package cn.ffcs.zhsq.wellKnownTrademark.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark.Trademark;
import cn.ffcs.zhsq.mybatis.persistence.wellKnownTrademark.TrademarkMapper;
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
 * Created by 张天慈 on 2017/12/15.
 */
@Service("trademarkServiceImpl")
@Transactional
public class TrademarkServiceImpl implements TrademarkService {

	//注入商标信息模块
	@Autowired
	private TrademarkMapper trademarkMapper;

	//地图标注服务
	@Autowired
	private IResMarkerService resMarkerService;


	/**
	 *
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public long countList(Map<String, Object> params) {
		long count = trademarkMapper.countList(params);
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
	public List<Trademark> findTrademarkList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1)* rows,rows);
		List<Trademark> list = trademarkMapper.searchList(params,rowBounds);

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
		RowBounds rowBounds = new RowBounds((page-1)* rows,rows);
		List<Trademark> list = trademarkMapper.searchList(params,rowBounds);
		Long count = trademarkMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count,list);
		return pagination;
	}

	/**
	 *
	 * @param trademark
	 * @return
	 */
	@Override
	public boolean insert(HttpSession session,Trademark trademark) {

		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		trademark.setCreatorId(userInfo.getUserId()); //创建人Id
		trademark.setCreatorName(userInfo.getUserName()); //创建人姓名
		trademark.setUpdaterId(userInfo.getUserId()); //修改人Id
		trademark.setUpdaterName(userInfo.getUserName()); //修改人姓名


		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//创建时间
		if (trademark.getCreatTimeDate() == null) {
			Date createdDate = new Date(); // 创建时间
			try {
				createdDate = DateUtils.convertStringToDate(sdf.format(createdDate), DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {

				e.printStackTrace();
			}
			trademark.setCreatTimeDate(createdDate);// 创建时间
			trademark.setUpdateTimeDate(createdDate); //修改时间
		}


		Boolean flag = false;
		if (trademarkMapper.insert(trademark)>0){
			ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.WELL_KNOWN_TRADEMARK_MARKER_TYPE);
			resMarker.setResourcesId(trademark.getTrademarkId());
			resMarker.setX(trademark.getLongitude().toString());
			resMarker.setY(trademark.getLatitude().toString());
			resMarkerService.saveOrUpdateResMarker(resMarker);
			flag = true;
		}

		return flag;
	}

	@Override
	public boolean update(HttpSession session, Trademark trademark) {
		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		trademark.setUpdaterId(userInfo.getUserId()); //修改人Id
		trademark.setUpdaterName(userInfo.getUserName()); //修改人姓名

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//修改时间
		Date updatedDate = new Date();

		try {
			updatedDate = DateUtils.convertStringToDate(sdf.format(updatedDate), DateUtils.PATTERN_24TIME);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		trademark.setUpdateTimeDate(updatedDate);// 修改时间

		Boolean flag = false;
		if(trademarkMapper.update(trademark) > 0){
			ResMarker resMarker = new ResMarker();
			resMarker.setCatalog("02");
			resMarker.setMapType("5");
			resMarker.setMarkerType(ConstantValue.WELL_KNOWN_TRADEMARK_MARKER_TYPE);
			resMarker.setResourcesId(trademark.getTrademarkId());
			resMarker.setX(trademark.getLongitude().toString());
			resMarker.setY(trademark.getLatitude().toString());
			resMarkerService.saveOrUpdateResMarker(resMarker);
			flag = true;
		}
		return flag;
	}

	@Override
	public Trademark findById(Long id) {
		Trademark trademark = trademarkMapper.findById(id);
		return trademark;
	}

	@Override
	public boolean delete(Trademark trademark) {
		Boolean flag = false;
		int rows = trademarkMapper.delete(trademark);
		if(rows > 0){
			flag = true;
		}
		return flag;
	}
}
