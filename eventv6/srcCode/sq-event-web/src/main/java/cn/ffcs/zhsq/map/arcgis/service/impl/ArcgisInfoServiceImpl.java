package cn.ffcs.zhsq.map.arcgis.service.impl;

import cn.ffcs.common.EUDGPagination;
import cn.ffcs.resident.service.CiRsRoomService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.WindowHelper;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisDataOfLocalMapper;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisInfoMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value="arcgisInfoService")
public class ArcgisInfoServiceImpl implements IArcgisInfoService {

	@Autowired
	private ArcgisInfoMapper arcgisInfoMapper;
	
	@Autowired
	private ArcgisDataOfLocalMapper arcgisDataOfLocalMapper;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private CiRsRoomService ciRsRoomServiceForRs;
	/**
	 * 2014-05-22 liushi add
	 * 根据infoOrgCode获取地图配置信息
	 * @param infoOrgCode
	 * @param engineType
	 * @return
	 */
	@Override
	public ArcgisMapConfigInfo findGisMapConfigInfoByOrgCode(String infoOrgCode,String engineType) {
		List<ArcgisMapConfigInfo> list = this.arcgisInfoMapper.findGisMapConfigInfoByOrgCode(infoOrgCode, engineType);
		ArcgisMapConfigInfo mapConfigInfo = new ArcgisMapConfigInfo();
		if(list.size() > 0) {
			mapConfigInfo = list.get(0);
		}
		return mapConfigInfo;
	}
	
	/**
	 * 2014-06-04 liushi add
	 * 保存网格轮廓信息
	 * 1、若数据库中已经存在该条数据那么进行修改操作
	 * 2、若数据库中不存在该条数据那么进行插入操作
	 * @param arcgisInfo
	 * @return
	 */
	@Override
	public boolean saveArcgisDrawAreaOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid){
//		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGridForIsExist(arcgisInfoOfGrid.getWid(),arcgisInfoOfGrid.getMapt());
		int row=0;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		boolean flag = false;
		if(arcgisInfoOfGrid != null){
			//bean转为map 开始
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(arcgisInfoOfGrid.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (int i = 0; i< propertyDescriptors.length; i++) {
					PropertyDescriptor descriptor = propertyDescriptors[i];
					String propertyName = descriptor.getName();
					if (!propertyName.equals("class")) {
						Method readMethod = descriptor.getReadMethod();
						Object result = readMethod.invoke(arcgisInfoOfGrid, new Object[0]);
						if (result != null) {
							params.put(propertyName, result);
						} else {
							params.put(propertyName, "");
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			//bean转为map 结束
		}
//		if(arcgisInfoOfGridList.size()>0) {//表示库表中已经存在对应的数据，需要进行修改操作
			//为了防止hs字段过长，改为用存储过程更新MAP_BUS_GRID_GIS：先更新其他字段再更新空间字段，空间字段需要用number的类型，用逗号隔开，sql分隔麻烦
			try {
				this.arcgisInfoMapper.updateArcgisDataOfGridMap(params);//这个存储过程即可以更新也可以新增
				row = Integer.parseInt(params.get("pRows").toString());//存储过程的返回值
				if(row>0){
					params2.put("wid",arcgisInfoOfGrid.getWid());
					params2.put("mapt",arcgisInfoOfGrid.getMapt());
					this.arcgisInfoMapper.updateArcgisDataOfGridSDO(params2);//更新空间字段
					if(params2.get("pRows") != null){
						System.out.println("更新网格空间字段结果："+Integer.parseInt(params2.get("pRows").toString()));//存储过程的返回值
					}else{
						System.out.println("更新网格空间字段结果：false");
					}

				}
			}catch (Exception e){
				e.printStackTrace();
			}

//		}else{//数据库中没有对应的数据，需要进行插入操作
//			row = this.arcgisInfoMapper.insertArcgisDataOfGrid(arcgisInfoOfGrid);
//		}
		if(row == 1) {
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}
	@Override
	public boolean deleteArcgisDrawAreaOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid){
		int row=0;
		boolean flag = false;
		row = this.arcgisInfoMapper.deleteArcgisDataOfGrid(arcgisInfoOfGrid);
		flag = row==1?true:false;
		return flag;
	}
	
	@Override
	public boolean updateBuildAddressXY(ArcgisInfoOfBuild arcgisInfoOfBuild) {
		List<ArcgisInfo> arcgisInfos = this.arcgisInfoMapper.getArcgisDataOfBuildForIsExist(arcgisInfoOfBuild.getWid(),arcgisInfoOfBuild.getMapt());
		int row = 0;
		boolean flag = false;
		if(arcgisInfos.size()>0){
			row = this.arcgisInfoMapper.updateBuildAddressXY(arcgisInfoOfBuild);
		}else{
			row = this.arcgisInfoMapper.updateBuildAddressXY(arcgisInfoOfBuild);
		}
		flag = row==1?true:false;

		//更新楼宇表相关冗余字段
		if(arcgisInfoOfBuild.getMapt() != null){
			AreaBuildingInfo areaBuildingInfo = new AreaBuildingInfo();
			if(arcgisInfoOfBuild.getWid() != null){
				areaBuildingInfo.setBuildingId(arcgisInfoOfBuild.getWid());
			}
			boolean hsFlag = false;//轮廓绘制标识
			boolean centerFlag = false;//中心点标注标识
			if(StringUtils.isNotBlank(arcgisInfoOfBuild.getHs())){
				hsFlag = true;
			}
			if(arcgisInfoOfBuild.getX() != null && arcgisInfoOfBuild.getY() != null){
				centerFlag = true;
			}
			if(arcgisInfoOfBuild.getMapt() == 5){//新地图二维
				areaBuildingInfo.setTdNew(hsFlag?"001":"002");
				areaBuildingInfo.setTdNewMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 30) {//新地图三维
				areaBuildingInfo.setThdNew(hsFlag?"001":"002");
				areaBuildingInfo.setThdNewMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 1 || arcgisInfoOfBuild.getMapt() == 2) {//旧地图二维
				areaBuildingInfo.setTdold(hsFlag?"001":"002");
				areaBuildingInfo.setTdoldMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 20 || arcgisInfoOfBuild.getMapt() == 4) {//旧地图三维
				areaBuildingInfo.setThdold(hsFlag?"001":"002");
				areaBuildingInfo.setThdoldMarker(centerFlag?"1":"0");
			}
			try {
				areaBuildingInfoService.updateAreaBuildingInfo(areaBuildingInfo);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return flag;
	}

	@Override
	public boolean saveArcgisDrawAreaOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild) {
		List<ArcgisInfo> arcgisInfos = this.arcgisInfoMapper.getArcgisDataOfBuildForIsExist(arcgisInfoOfBuild.getWid(),arcgisInfoOfBuild.getMapt());
		int row = 0;
		boolean flag = false;
		if(arcgisInfos.size()>0){
			row = this.arcgisInfoMapper.updateArcgisDataOfBuild(arcgisInfoOfBuild);
		}else{
			row = this.arcgisInfoMapper.insertArcgisDataOfBuild(arcgisInfoOfBuild);
		}
		flag = row==1?true:false;

		//更新楼宇表相关冗余字段
		if(arcgisInfoOfBuild.getMapt() != null){
			AreaBuildingInfo areaBuildingInfo = new AreaBuildingInfo();
			if(arcgisInfoOfBuild.getWid() != null){
				areaBuildingInfo.setBuildingId(arcgisInfoOfBuild.getWid());
			}
			boolean hsFlag = false;//轮廓绘制标识
			boolean centerFlag = false;//中心点标注标识
			if(StringUtils.isNotBlank(arcgisInfoOfBuild.getHs())){
				hsFlag = true;
			}
			if(arcgisInfoOfBuild.getX() != null && arcgisInfoOfBuild.getY() != null){
				centerFlag = true;
			}
			if(arcgisInfoOfBuild.getMapt() == 5){//新地图二维
				areaBuildingInfo.setTdNew(hsFlag?"001":"002");
				areaBuildingInfo.setTdNewMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 30) {//新地图三维
				areaBuildingInfo.setThdNew(hsFlag?"001":"002");
				areaBuildingInfo.setThdNewMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 1 || arcgisInfoOfBuild.getMapt() == 2) {//旧地图二维
				areaBuildingInfo.setTdold(hsFlag?"001":"002");
				areaBuildingInfo.setTdoldMarker(centerFlag?"1":"0");
			}else if(arcgisInfoOfBuild.getMapt() == 20 || arcgisInfoOfBuild.getMapt() == 4) {//旧地图三维
				areaBuildingInfo.setThdold(hsFlag?"001":"002");
				areaBuildingInfo.setThdoldMarker(centerFlag?"1":"0");
			}
			areaBuildingInfo.setUpdateUser(arcgisInfoOfBuild.getUpdateUser());
			try {
				areaBuildingInfoService.updateAreaBuildingInfo(areaBuildingInfo);
				//南安需求，改标注的时候要同步人员，不知道写在哪里，就写在这里判断好了
				areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfo(arcgisInfoOfBuild.getWid());
				if (areaBuildingInfo.getOrgCode().startsWith("350583")) {
					ciRsRoomServiceForRs.updateBuildIsLocal(arcgisInfoOfBuild.getWid(), "1");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return flag;
	}
	@Override
	public boolean deleteArcgisDrawAreaOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild) {
		List<ArcgisInfo> arcgisInfos = this.arcgisInfoMapper.getArcgisDataOfBuildForIsExist(arcgisInfoOfBuild.getWid(),arcgisInfoOfBuild.getMapt());
		int row = 0;
		boolean flag = false;
		if(arcgisInfos.size()>0){
			row = this.arcgisInfoMapper.deleteArcgisDataOfBuild(arcgisInfoOfBuild);
		}else{
			row = 1;
		}
		flag = row==1?true:false;
		//更新楼宇表相关冗余字段
		if(arcgisInfoOfBuild.getMapt() != null){
			AreaBuildingInfo areaBuildingInfo = new AreaBuildingInfo();
			if(arcgisInfoOfBuild.getWid() != null){
				areaBuildingInfo.setBuildingId(arcgisInfoOfBuild.getWid());
			}
			if(arcgisInfoOfBuild.getMapt() == 5){//新地图二维
				areaBuildingInfo.setTdNew("002");
				areaBuildingInfo.setThdNewMarker("0");
			}else if(arcgisInfoOfBuild.getMapt() == 30) {//新地图三维
				areaBuildingInfo.setThdNew("002");
				areaBuildingInfo.setThdNewMarker("0");
			}else if(arcgisInfoOfBuild.getMapt() == 1 || arcgisInfoOfBuild.getMapt() == 2) {//旧地图二维
				areaBuildingInfo.setTdold("002");
				areaBuildingInfo.setTdoldMarker("0");
			}else if(arcgisInfoOfBuild.getMapt() == 20 || arcgisInfoOfBuild.getMapt() == 4) {//旧地图三维
				areaBuildingInfo.setThdold("002");
				areaBuildingInfo.setThdoldMarker("0");
			}
			try {
				areaBuildingInfoService.updateAreaBuildingInfo(areaBuildingInfo);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 保存片区网格轮廓信息
	 * 1、若数据库中已经存在该条数据那么进行修改操作
	 * 2、若数据库中不存在该条数据那么进行插入操作
	 * @param arcgisInfoOfSegmentGrid
	 * @return
	 */
	@Override
	public boolean saveArcgisDrawAreaOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid){
		List<ArcgisInfoOfSegmentGrid> arcgisInfoOfSegmentGridList = this.arcgisInfoMapper.getArcgisDataOfSegmentGridForIsExist(arcgisInfoOfSegmentGrid.getWid(),arcgisInfoOfSegmentGrid.getMapt());
		int row=0;
		boolean flag = false;
		if(arcgisInfoOfSegmentGridList.size()>0) {//表示库表中已经存在对应的数据，需要进行修改操作
			row = this.arcgisInfoMapper.updateArcgisDataOfSegmentGrid(arcgisInfoOfSegmentGrid);
		}else{//数据库中没有对应的数据，需要进行插入操作
			row = this.arcgisInfoMapper.insertArcgisDataOfSegmentGrid(arcgisInfoOfSegmentGrid);
		}
		if(row == 1) {
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}
	
	@Override
	public boolean deleteArcgisDrawAreaOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid) {
		int row=0;
		boolean flag = false;
		row = this.arcgisInfoMapper.deleteArcgisDataOfSegmentGrid(arcgisInfoOfSegmentGrid);
		flag = row==1?true:false;
		return flag;
	}
	
	/**
	 * 2014-06-09 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 获取其父节点以及父节点下面子节点的数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGrids(Long gridId,Integer mapt){
		Long parentGridId = this.arcgisInfoMapper.getParentGridId(gridId);
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDrawDataOfGrids(parentGridId,mapt);
		return arcgisInfoOfGridList;
	}
	/**
	 * 2014-11-03 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 根据showType分别查询父级、自身、本级、下级的网格轮廓数据
	 * showType：0 代表自身轮廓数据查询  1代表父级轮廓数据查询  2代表本级（自己除外）轮廓数据查询 3、代表下级轮廓数据查询
	 * @param gridId
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGridsByLevel(Long gridId,Integer mapt,String showType){
		
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
		if("0".equals(showType)){//自身轮廓数据查询
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDrawDataOfSelfGrids(gridId,mapt);
		}else if("1".equals(showType)){//父级轮廓数据查询
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDrawDataOfParentGrids(gridId,mapt);
		}else if("2".equals(showType)){//本级（自己除外）轮廓数据查询
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDrawDataOfSameLevelGrids(gridId,mapt);
		}else if("3".equals(showType)){//下级轮廓数据查询
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDrawDataOfLowerGrids(gridId,mapt);
		}
		return arcgisInfoOfGridList;
	}
	/**
	 * 2014-06-10 zhanh add
	 * 获取楼栋父节点下所有数据
	 */
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuilds(Long gridId,
			Integer mapt) {
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getArcgisDrawDataOfBuilds(gridId,mapt);
		return arcgisInfoOfBuildList;
	}
	
	/**
	 * 2015-07-08 liushi add
	 * 根据网格id获取其下属的所有楼宇id
	 * @param gridId
	 * @return
	 */
	public String getArcgisDataIdsOfBuilds(Long gridId){
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getArcgisDrawDataIdsOfBuilds(gridId);
		String result = "";
		if(arcgisInfoOfBuildList != null) {
			for(ArcgisInfoOfBuild arcgisInfoOfBuild:arcgisInfoOfBuildList) {
				result += ("".equals(result))? String.valueOf(arcgisInfoOfBuild.getWid()) : ","+String.valueOf(arcgisInfoOfBuild.getWid());
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuildsPoints(Long gridId,
			Integer mapt) {
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getArcgisDataOfBuildsPoints(gridId,mapt);
		return arcgisInfoOfBuildList;
	}
	@Override
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuildsByBuildingId(
			Long buildingId, Integer mapt) {
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getDataOfBuildsByBuildingId(buildingId,mapt);
		return arcgisInfoOfBuildList;
	}
	@Override
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuildsByGridId(Long gridId, Integer mapt) {
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getArcgisDrawDataOfBuildsByGridId(gridId,mapt);
		return arcgisInfoOfBuildList;
	}
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(String gridCode,
			Long gridLevel, Integer mapt) {
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGrids(gridCode,gridLevel,mapt);
		return arcgisInfoOfGridList;
	}
	@Override
	public String getArcgisDataIdsOfGrids(Long gridId,Long gridLevel) {
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataIdsOfGrids(gridId,gridLevel);
		String result = "";
		if(arcgisInfoOfGridList != null) {
			for(ArcgisInfoOfGrid arcgisInfoOfGrid:arcgisInfoOfGridList) {
				result += ("".equals(result))? String.valueOf(arcgisInfoOfGrid.getWid()) : ","+String.valueOf(arcgisInfoOfGrid.getWid());
			}
		}
		return result;
	}
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(Long gridId,
			Long gridLevel, Integer mapt) {
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGridsByGridId(gridId,gridLevel,mapt);
		return arcgisInfoOfGridList;
	}
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByIds(String ids, Integer mapt) {
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGridsListByIds(ids,mapt);
		return arcgisInfoOfGridList;
	}
	@Override
	public List<ArcgisInfoOfHlhx> getArcgisDataOfHlhxListByIds(String ids, Integer mapt) {
		List<ArcgisInfoOfHlhx> arcgisInfoOfHlhxList = this.arcgisInfoMapper.getArcgisDataOfHlhxListByIds(ids,mapt);
		return arcgisInfoOfHlhxList;
	}
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsByLevels(String gridCode,
			String gridLevels, Integer mapt) {
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGridsByLevels(gridCode,gridLevels,mapt);
		return arcgisInfoOfGridList;
	}
	/**
	 * 2014-06-13 liushi add
	 * 获取单条的网格轮廓数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrid(Long gridId, Integer mapt){
		return this.arcgisInfoMapper.getArcgisDataOfGrid(gridId, mapt);
	}

	/**
	 * 2014-06-13 liushi add
	 * 获取单挑的楼宇轮廓数据
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuild(Long buildingId, Integer mapt){
		return this.arcgisInfoMapper.getArcgisDataOfBuild(buildingId, mapt);
	}
	

	@Override
	public List<ArcgisInfoOfSegmentGrid> getArcgisDataOfSegmentGrid(Long segmentGridId, Long parentGridId, Integer mapt){
		return this.arcgisInfoMapper.getArcgisDataOfSegmentGrid(segmentGridId, parentGridId, mapt);
	}
	
	/**
	 * 2014-06-26 liushi 
	 * 获取市政设施的相关定位信息
	 * @param resourcesId
	 * @param markerType
	 * @param catalog
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfMarkerType(Long resourcesId,
			String markerType, String catalog, Integer mapt) {
		return this.arcgisInfoMapper.getArcgisDataOfMarkerType(resourcesId, markerType,catalog,mapt);
	}
	/**
	 * 2014-07-17 liushi
	 * 根据市政设施的id获取所在网格的地图定位信息
	 * @param resourcesId
	 * @param markerType
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfGridByResId(Long resourcesId,
			String markerType, Integer mapt) {
		return this.arcgisInfoMapper.getArcgisDataOfGridByResId(resourcesId, markerType,mapt);
	}
	@Override
	public boolean saveArcgisDataOfMarkerType(ArcgisInfoOfPublic arcgisInfoOfPublic) {
		List<ArcgisInfoOfPublic> arcgisInfoOfPublics = this.arcgisInfoMapper.getArcgisDataOfMarkerType(arcgisInfoOfPublic.getWid(), arcgisInfoOfPublic.getMarkerType(),arcgisInfoOfPublic.getCatalog(),arcgisInfoOfPublic.getMapt());
		int row = 0;
		boolean flag = false;
		if(arcgisInfoOfPublics.size()>0){
			row = this.arcgisInfoMapper.updateArcgisDataOfMarkerType(arcgisInfoOfPublic);
		}else {
			row = this.arcgisInfoMapper.insertArcgisDataOfMarkerType(arcgisInfoOfPublic);
		}
		flag = row==1?true:false;
		return flag;
	}
	/**
	 * 2014-08-06 liushi
	 * 获取arcgis地图配置信息
	 * 1、获取地图配置信息
	 * 2、获取对应的地图服务信息
	 * 3、获取对应的地图标尺信息
	 * @param gridCode
	 * @return
	 */
	@Override
	public List<ArcgisConfigInfo> findArcgisConfigInfoByGridCode(String gridCode, String engineName){
		if (StringUtils.isBlank(engineName)) {
			engineName = ConstantValue.MAP_ENGINE_NAME;
		}
		List<ArcgisConfigInfo> list = this.arcgisInfoMapper.findArcgisConfigInfoByGridCode(gridCode, engineName);
		List<ArcgisConfigInfo> results = new ArrayList<ArcgisConfigInfo>();
		Integer mapTypeCode = 0;
		String orgCode = "0";
		if(list.size() > 0) {
			List<ArcgisConfigInfo> arcgisConfigInfoDefaults = new ArrayList<ArcgisConfigInfo>();
			List<ArcgisConfigInfo> arcgisConfigInfoParents = new ArrayList<ArcgisConfigInfo>();
//			orgCode = gridCode;
			for(ArcgisConfigInfo arcgisConfigInfo : list) {
				if(arcgisConfigInfo.getGridCode() != null && arcgisConfigInfo.getGridCode().equals(gridCode)) {
					List<ArcgisServiceInfo> arcgisServiceInfos = this.arcgisInfoMapper.findArcgisServiceInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					List<ArcgisScalenInfo> arcgisScalenInfos = this.arcgisInfoMapper.findArcgisScalenInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					arcgisConfigInfo.setArcgisServiceInfos(arcgisServiceInfos);
					arcgisConfigInfo.setArcgisScalenInfos(arcgisScalenInfos);
					results.add(arcgisConfigInfo);
				} else if(arcgisConfigInfo.getGridCode() == null){
					List<ArcgisServiceInfo> arcgisServiceInfos = this.arcgisInfoMapper.findArcgisServiceInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					List<ArcgisScalenInfo> arcgisScalenInfos = this.arcgisInfoMapper.findArcgisScalenInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					arcgisConfigInfo.setArcgisServiceInfos(arcgisServiceInfos);
					arcgisConfigInfo.setArcgisScalenInfos(arcgisScalenInfos);
					arcgisConfigInfoDefaults.add(arcgisConfigInfo);
				}else if(orgCode != null && (orgCode.length()<=arcgisConfigInfo.getGridCode().length())){
					if(orgCode.length()<arcgisConfigInfo.getGridCode().length()){
						orgCode = arcgisConfigInfo.getGridCode();
						arcgisConfigInfoParents.clear();
					}
					List<ArcgisServiceInfo> arcgisServiceInfos = this.arcgisInfoMapper.findArcgisServiceInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					List<ArcgisScalenInfo> arcgisScalenInfos = this.arcgisInfoMapper.findArcgisScalenInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
					arcgisConfigInfo.setArcgisServiceInfos(arcgisServiceInfos);
					arcgisConfigInfo.setArcgisScalenInfos(arcgisScalenInfos);
					arcgisConfigInfoParents.add(arcgisConfigInfo);
				}

			}
			if(results == null || results.size()<=0){
				results = arcgisConfigInfoParents;
				if(results == null || results.size()<=0){
					results = arcgisConfigInfoDefaults;
				}
//				for(ArcgisConfigInfo arcgisConfigInfo : list) {
//					if("0".equals(orgCode) 
//							|| (orgCode == null && arcgisConfigInfo.getGridCode() == null ) 
//							|| (orgCode != null && orgCode.equals(arcgisConfigInfo.getGridCode()))) {
//						orgCode = arcgisConfigInfo.getGridCode();
//						if(mapTypeCode != arcgisConfigInfo.getMapTypeCode()) {
//							List<ArcgisServiceInfo> arcgisServiceInfos = this.arcgisInfoMapper.findArcgisServiceInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
//							List<ArcgisScalenInfo> arcgisScalenInfos = this.arcgisInfoMapper.findArcgisScalenInfoByConfigId(arcgisConfigInfo.getArcgisConfigInfoId());
//							arcgisConfigInfo.setArcgisServiceInfos(arcgisServiceInfos);
//							arcgisConfigInfo.setArcgisScalenInfos(arcgisScalenInfos);
//							results.add(arcgisConfigInfo);
//							mapTypeCode = arcgisConfigInfo.getMapTypeCode();
//						}
//					}
//				}
			}
		}
		return results;
	}
	
	/**
	 * 2014-08-11 liushi add
	 * 获取网格员轨迹
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryList(String imsi,
			String locateTimeBegin, String locateTimeEnd,
			String mobileTelephone, Long userId,Integer mapt) {
		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoMapper.getGridAdminTrajectoryList(imsi,locateTimeBegin,locateTimeEnd,mobileTelephone,userId,mapt);
		return list;
	}

	@Override
	public List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryListByUserName(String userName, String locateTimeBegin, String locateTimeEnd, String mobileTelephone, Long userId, Integer mapt) {
		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoMapper.getGridAdminTrajectoryListByUserName(userName, locateTimeBegin, locateTimeEnd, mobileTelephone, userId, mapt);
		return list;
	}

	/**
	 * 队员轨迹数据查询
	 * @param memberId
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param userId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfTrajectory> getTeamMemberTrajectoryList(String memberId,String imsi,
																	   String locateTimeBegin, String locateTimeEnd,
																	   Long userId,Integer mapt) {
		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoMapper.getTeamMemberTrajectoryList(memberId, imsi, locateTimeBegin, locateTimeEnd, userId, mapt);
		return list;
	}

	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByRegisValue(String regisValue, Integer mapType) {
		List<String> orgCodeList = new ArrayList<String>();
		List<ArcgisInfoOfGrid> arcgisInfoOfGrids = new ArrayList<ArcgisInfoOfGrid>();
		List<MixedGridInfo> mixedGridInfos = new ArrayList<MixedGridInfo>();
		if(StringUtils.isNotBlank(regisValue)){
			orgCodeList = orgEntityInfoOutService.getCodeListByRegisValue(regisValue);
			if(orgCodeList != null && orgCodeList.size()>0){
				mixedGridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCodeListForEvent(orgCodeList);
			}
			if(mixedGridInfos != null && mixedGridInfos.size()>0){
				for(MixedGridInfo mixedGridInfo:mixedGridInfos){
					String mapts = "0";
					if(mixedGridInfo!=null && mixedGridInfo.getMapType()!=null && mixedGridInfo.getMapType().equals("005")) {
						if(mapType == null || "".equals(mapType)) {
							mapts = "5,30";
						}else {
							if(mapType.intValue() == 2){
								mapts = "5";
							}else if(mapType.intValue() == 3){
								mapts = "30";
							}
						}
					}else if(mapType!=null && mixedGridInfo.getMapType()!=null && mixedGridInfo.getMapType().equals("004")){
						if(mapType == null || "".equals(mapType)) {
							mapts = "1,2,4,20";
						}else {
							if(mapType.intValue() == 2){
								mapts = "1,2";
							}else if(mapType.intValue() == 3){
								mapts = "4,20";
							}
						}
					}
					List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
					arcgisInfoOfGridList = arcgisInfoMapper.getArcgisDataOfGridForOuter(mapts, mixedGridInfo.getGridId());
					if(arcgisInfoOfGridList != null && arcgisInfoOfGridList.size()>0){
						arcgisInfoOfGrids.addAll(arcgisInfoOfGridList);
					}
				}
			}
			return arcgisInfoOfGrids;
		}else{
			return null;
		}


	}

	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByOrgCode(String orgCode, Integer mapType) {
		OrgEntityInfoBO orgEntityInfoBO = new OrgEntityInfoBO();
		List<ArcgisInfoOfGrid> arcgisInfoOfGrids = new ArrayList<ArcgisInfoOfGrid>();
		List<MixedGridInfo> mixedGridInfos = new ArrayList<MixedGridInfo>();
		if(StringUtils.isNotBlank(orgCode)){
			orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(orgCode);
			if(orgEntityInfoBO != null && StringUtils.isNotBlank(orgEntityInfoBO.getOrgCode())){
				mixedGridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(orgEntityInfoBO.getOrgCode());
			}
			if(mixedGridInfos != null && mixedGridInfos.size()>0){
				for(MixedGridInfo mixedGridInfo:mixedGridInfos){
					String mapts = "0";
					if(mixedGridInfo!=null && mixedGridInfo.getMapType()!=null && mixedGridInfo.getMapType().equals("005")) {
						if(mapType == null || "".equals(mapType)) {
							mapts = "5,30";
						}else {
							if(mapType.intValue() == 2){
								mapts = "5";
							}else if(mapType.intValue() == 3){
								mapts = "30";
							}
						}
					}else if(mapType!=null && mixedGridInfo.getMapType()!=null && mixedGridInfo.getMapType().equals("004")){
						if(mapType == null || "".equals(mapType)) {
							mapts = "1,2,4,20";
						}else {
							if(mapType.intValue() == 2){
								mapts = "1,2";
							}else if(mapType.intValue() == 3){
								mapts = "4,20";
							}
						}
					}
					List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
					arcgisInfoOfGridList = arcgisInfoMapper.getArcgisDataOfGridForOuter(mapts, mixedGridInfo.getGridId());
					if(arcgisInfoOfGridList != null && arcgisInfoOfGridList.size()>0){
						arcgisInfoOfGrids.addAll(arcgisInfoOfGridList);
					}
				}
			}
			return arcgisInfoOfGrids;
		}else{
			return null;
		}
	}

	/**
	 * 2015-03-06 huangmw add
	 * 护路护线队员轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfTrajectory> getCareRoadMemberTrajectoryList(String memberId,String imsi,
			String locateTimeBegin, String locateTimeEnd,
			String mobileTelephone, Long userId,Integer mapt,String bizType) {
		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoMapper.getCareRoadMemberTrajectoryList(memberId, imsi,locateTimeBegin,locateTimeEnd,mobileTelephone,userId,mapt,bizType);
		return list;
	}

	/**
	 * 2015-01-22 liushi add 保存（含有新增跟修改的操作）资源轮廓数据
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	@Override
	public boolean saveArcgisDrawAreaOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic) {
		List<ArcgisInfoOfPublic> arcgisInfoOfPublicList = this.arcgisDataOfLocalMapper.getArcgisDataOfResourcesSelfById(arcgisInfoOfPublic.getWid(), arcgisInfoOfPublic.getMapt(), arcgisInfoOfPublic.getMarkerType());
		int row=0;
		boolean flag = false;
		if(arcgisInfoOfPublicList.size()>0) {//表示库表中已经存在对应的数据，需要进行修改操作
			row = this.arcgisInfoMapper.updateArcgisDataOfResources(arcgisInfoOfPublic);
		}else{//数据库中没有对应的数据，需要进行插入操作
			row = this.arcgisInfoMapper.insertArcgisDataOfResources(arcgisInfoOfPublic);
		}
		if(row == 1) {
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 2015-01-22 liushi add 逻辑删除资源轮廓数据
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	@Override
	public boolean deleteArcgisDrawAreaOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic) {
		int row=0;
		boolean flag = false;
		row = this.arcgisInfoMapper.deleteArcgisDataOfResources(arcgisInfoOfPublic);
		flag = row==1?true:false;
		return flag;
	}

	/**
	 * 2015-02-10 liushi add 根据传递的gridId 和mapt（非正常的mapt ）
	 * @param gridId
	 * @param mapt 2 二维地图 ，3 三维地图  ，null传回两条当前玩个使用的所有地图类型数据
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridForOuter(Long gridId,
			String mapt) {
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String mapts = "0";
		if(gridInfo!=null && gridInfo.getMapType()!=null && gridInfo.getMapType().equals("005")) {
			if(mapt == null || "".equals(mapt)) {
				mapts = "5,30";
			}else {
				if("2".equals(mapt)){
					mapts = "5";
				}else if("3".equals(mapt)){
					mapts = "30";
				}
			}
		}else if(gridInfo!=null && gridInfo.getMapType()!=null && gridInfo.getMapType().equals("004")){
			if(mapt == null || "".equals(mapt)) {
				mapts = "1,2,4,20";
			}else {
				if("2".equals(mapt)){
					mapts = "1,2";
				}else if("3".equals(mapt)){
					mapts = "4,20";
				}
			}
		}else if(gridInfo!=null && gridInfo.getMapType()!=null && gridInfo.getMapType().equals("007")) {
			mapts = "5";
		}
		List<ArcgisInfoOfGrid> list = this.arcgisInfoMapper.getArcgisDataOfGridForOuter(mapts, gridId);
		return list;
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfBuildBinding(Long id) {
		return this.arcgisDataOfLocalMapper.getArcgisDataOfBuildBinding(id);
	}

	@Override
	public int updateArcgisDataOfBuildBinding(Long id, String ids, String names) {
		return this.arcgisDataOfLocalMapper.updateArcgisDataOfBuildBinding(id, ids, names);
	}

	@Override
	public String getMaptByMapEngineTypeAndModule(String mapEngineType,
			String module, String infoOrgCode) {
		// 数据库存储的模块地图类型值
		String mapt = null;
		// 地图配置的是新地图(Arcgis地图引擎)还是旧地图(Gis地图引擎)
		if (StringUtils.isNotBlank(mapEngineType)) {
			// 根据模块查询地图配置的是几维地图（二维还是三维）
			String mapDimension = null;
			if (StringUtils.isNotBlank(module)) {
				mapDimension = this.funConfigurationService.turnCodeToValue(IArcgisInfoService.MAP_TYPE_CODE, module,
						IFunConfigurationService.CFG_TYPE_FACT_VAL, infoOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
				// 如果是新地图引擎
				if (mapEngineType.equals(IArcgisInfoService.NEW_MAP_ENGINE)) {
					// 有配置值
					if (StringUtils.isNotBlank(mapDimension)) {
						if (mapDimension.equals(IArcgisInfoService.TWO_DIMENSION)) { // 二维
							mapt = IArcgisInfoService.TWO_DIMENSION_MAPT_OF_NEWMAP; // 二维mapt为5
						} else if (mapDimension.equals(IArcgisInfoService.THREE_DIMENSION)) { // 三维
							mapt = IArcgisInfoService.THREE_DIMENSION_MAPT_OF_NEWMAP; // 三维mapt为30
						}
					}
				} else if (mapEngineType.equals(IArcgisInfoService.OLD_MAP_ENGINE)) { // 旧地图引擎
					// 有配置值
					if (StringUtils.isNotBlank(mapDimension)) {
						if (mapDimension.equals(IArcgisInfoService.TWO_DIMENSION)) { // 二维
							mapt = IArcgisInfoService.TWO_DIMENSION_MAPT_OF_OLDMAP;// 二维mapt为2
						} else if (mapDimension.equals(IArcgisInfoService.THREE_DIMENSION)) {// 三维
							mapt = IArcgisInfoService.THREE_DIMENSION_MAPT_OF_OLDMAP;// 三维mapt为20
						}
					}
				}
			}
		}
		return mapt;
	}

	@Override
	public String getMapTypeByModule(String modularCode, String infoOrgCode) {
		String mapType = "";
		if (modularCode != null && !"".equals(modularCode) && modularCode.length() > 1) {
			mapType = this.funConfigurationService.turnCodeToValue(IArcgisInfoService.MAP_TYPE_CODE, modularCode,
					IFunConfigurationService.CFG_TYPE_FACT_VAL, infoOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		}
		return mapType;
	}

	@Override
	public String getBizIds(String infoOrgCode, String type) {
		PreparedStatement pstm = null;
		Connection connection = null;
		ResultSet rs = null;
		StringBuffer sqlSB = new StringBuffer();
		try {
			DataSource ds = jdbcTemplate.getDataSource();
			connection = ds.getConnection();
			sqlSB.setLength(0);
			if ("grid".equals(type)) {
				sqlSB.append("SELECT G.GRID_ID ID_ \n");
				sqlSB.append("  FROM T_DC_GRID G \n");
				sqlSB.append(" WHERE G.STATUS = '001' \n");
				sqlSB.append("   AND G.INFO_ORG_CODE LIKE ? || '%' \n");
			} else if ("build".equals(type)) {
				sqlSB.append("SELECT T1.BUILDING_ID ID_ \n");
				sqlSB.append("  FROM T_DC_AREA_BUILDING_INFO T1 \n");
				sqlSB.append("  LEFT JOIN T_DC_GRID T2 \n");
				sqlSB.append("    ON T1.GRID_ID = T2.GRID_ID \n");
				sqlSB.append("   AND T2.STATUS = '001' \n");
				sqlSB.append(" WHERE T1.STATUS = '001' \n");
				sqlSB.append("   AND T2.INFO_ORG_CODE LIKE ? || '%' \n");
			}
			if (sqlSB.length() > 0) {
				StringBuffer ids = new StringBuffer();
				pstm = connection.prepareStatement(sqlSB.toString());
				pstm.setString(1, infoOrgCode);
				rs = pstm.executeQuery();
				while (rs.next()) {
					ids.append(rs.getString("ID_") + ",");
				}
				return ids.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstm != null) pstm.close();
				if (connection != null) connection.close();
			} catch (Exception e) {
				
			}
		}
		return "";
	}

	/**
	 * 2014-06-13 liushi add
	 * 获取单条的网格轮廓数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@Override
	public ArcgisInfoOfGrid getArcgisDataOfLastParentgrid(Long gridId, Integer mapt){
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
		arcgisInfoOfGridList =  this.arcgisInfoMapper.getArcgisDataOfParentgrids(gridId, mapt);
		ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
		//语句已按网格层级降序排序，第一条就是最接近且有网格轮廓数据的
		if(arcgisInfoOfGridList != null && arcgisInfoOfGridList.size()>0){
			arcgisInfoOfGrid = arcgisInfoOfGridList.get(0);
		}
		return arcgisInfoOfGrid;
	}

	@Override
	public List<ArcgisInfoOfTrajectory> getTrajectoryListByParams(Map<String, Object> params) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		List<ArcgisInfoOfTrajectory> list = new ArrayList<ArcgisInfoOfTrajectory>();
		List<ArcgisInfoOfDayTraj> dayTrajList = new ArrayList<ArcgisInfoOfDayTraj>();
		try {
			String startDateStr = (String) params.get("locateTimeBegin");
			String endDateStr = (String) params.get("locateTimeEnd");
			String currentDateStr = sdf.format(new Date());
			//System.out.println("userName："+params.get("userName").toString());
			if(StringUtils.isNotBlank(startDateStr)){
				//取值时间超出当前日期，就去取表T_DAY_TRAJ_STAT的数据
				if(DateUtils.compareMinDate(startDateStr, DateUtils.PATTERN_DATE, currentDateStr, DateUtils.PATTERN_DATE)){
					//获取历史数据
					params.put("startDateStr", DateUtils.formatDate(startDateStr,DateUtils.PATTERN_DATE));
					params.put("endDateStr", DateUtils.formatDate(endDateStr,DateUtils.PATTERN_DATE));
					dayTrajList = this.arcgisInfoMapper.getDayTrajListByParams(params);
					if(dayTrajList != null && dayTrajList.size()>0){
						for(int i=0;i<dayTrajList.size();i++){
							if(StringUtils.isNotBlank(dayTrajList.get(i).getGpsArr()) &&
									StringUtils.isNotBlank(dayTrajList.get(i).getGpsTimeArr())) {
								String[] gpsArr = dayTrajList.get(i).getGpsArr().split(";");
								String[] gpsTimeArr = dayTrajList.get(i).getGpsTimeArr().split(";");
								for (int j = 0; j < gpsArr.length; j++) {
									//如果统计时间不是开始时间那天的话就全部取出来，如果是开始时间那天的话就去判断轨迹时间是否大于开始时间
									if (StringUtils.isNotBlank(dayTrajList.get(i).getStatisticDateStr())
											&& StringUtils.isNotBlank(startDateStr) && StringUtils.isNotBlank(gpsTimeArr[j])) {
										if (!(DateUtils.formatDate(dayTrajList.get(i).getStatisticDateStr(), DateUtils.PATTERN_DATE).equals(DateUtils.formatDate(startDateStr, DateUtils.PATTERN_DATE)))
												|| DateUtils.compareMinDate(startDateStr, DateUtils.PATTERN_24TIME_Hm, gpsTimeArr[j], DateUtils.PATTERN_24TIME_Hm)) {
											String[] gps = gpsArr[j].split(",");
											ArcgisInfoOfTrajectory trajectory = new ArcgisInfoOfTrajectory();
											trajectory.setWid(dayTrajList.get(i).getWid());
											trajectory.setX(Double.parseDouble(gps[0]));
											trajectory.setY(Double.parseDouble(gps[1]));
											trajectory.setLocateTime(gpsTimeArr[j]);
											list.add(trajectory);
										}
									}

								}
							}
						}
					}
				}
				//如果结束时间是在当天的话，就需要去查询bus_locate表
				//条件先去掉，因为脚本没有跑完
				//if(StringUtils.isBlank(endDateStr) || (StringUtils.isNotBlank(endDateStr) && DateUtils.formatDate(endDateStr,DateUtils.PATTERN_DATE).equals(DateUtils.formatDate(currentDateStr, DateUtils.PATTERN_DATE)))){
				if(StringUtils.isBlank(startDateStr)){
					SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					startDateStr = sdfs.format(new Date());
				}
				params.put("locateTimeBegin", startDateStr);

				List<ArcgisInfoOfTrajectory> currentDaylist = new ArrayList<ArcgisInfoOfTrajectory>();
				if(params.get("userName") != null && params.get("jsJiangYinFlag") != null
						&& params.get("jsJiangYinFlag").toString().equals("true")){
					currentDaylist = this.arcgisInfoMapper.getTrajectoryListByUserName(params);
				}else if(params.get("userId") != null) {
					currentDaylist = this.arcgisInfoMapper.getTrajectoryListByUserId(params);
				}
				if(currentDaylist != null && currentDaylist.size()>0){
					list.addAll(currentDaylist);
				}
				//}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据参数获取轨迹信息
	 * @param params
	 * imsi imsi号
	 * userId 用户ID
	 * userName 用户名
	 * locateTimeBegin 轨迹开始时间
	 * locateTimeEnd 	轨迹结束时间
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfTrajectory> getDayTrajectoryListByParams(Map<String, Object> params) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		List<ArcgisInfoOfTrajectory> list = new ArrayList<ArcgisInfoOfTrajectory>();
		List<ArcgisInfoOfDayTraj> dayTrajList = new ArrayList<ArcgisInfoOfDayTraj>();
		try {
			String trackDate = (String) params.get("trackDate");
			String startDateStr = (String) params.get("locateTimeBegin");
			String endDateStr = (String) params.get("locateTimeEnd");
			String currentDateStr = sdf.format(new Date());
			//System.out.println("userName："+params.get("userName").toString());
			if(StringUtils.isNotBlank(trackDate)){
				if (trackDate.equals(currentDateStr)) {
					// 如果时间是在当天的话，就需要去查询bus_locate表
					List<ArcgisInfoOfTrajectory> currentDaylist = new ArrayList<ArcgisInfoOfTrajectory>();
					if(params.get("userName") != null && params.get("jsJiangYinFlag") != null
							&& params.get("jsJiangYinFlag").toString().equals("true")){
						currentDaylist = this.arcgisInfoMapper.getTrajectoryListByUserName(params);
					}else if(params.get("userId") != null) {
						currentDaylist = this.arcgisInfoMapper.getTrajectoryListByUserId(params);
					}
					if(currentDaylist != null && currentDaylist.size()>0){
						list.addAll(currentDaylist);
					}
				} else {// 取值时间不等于当前日期，就去取表T_DAY_TRAJ_STAT的数据
					// 获取历史数据
					params.put("startDateStr", startDateStr + ":00");
					params.put("endDateStr", endDateStr + ":00");
					dayTrajList = this.arcgisInfoMapper.getDayTrajListForOri(params);
					list.addAll(this.convertData(dayTrajList));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<ArcgisInfoOfGrid> getArcgisDataOfChildrenGridsByParentId(Long parentGridId, Integer mapt) {
		MixedGridInfo gridInfo = this.mixedGridInfoService.findMixedGridInfoById(parentGridId, false);
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = null;
		if (gridInfo != null && gridInfo.getGridLevel() == 6) {
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfGridsById(parentGridId, mapt);
		} else {
			arcgisInfoOfGridList = this.arcgisInfoMapper.getArcgisDataOfChildrenGridsByParentId(parentGridId, mapt);
		}
		return arcgisInfoOfGridList;
	}

	@Override
	public List<ArcgisInfoOfUser> getUserLastLocateByParams(Map<String, Object> params) {
		List<ArcgisInfoOfUser> list = new ArrayList<ArcgisInfoOfUser>();
		try {
			Integer mapt = (Integer) params.get("mapt");
			if(mapt == null){
				params.put("mapt", 5);
			}
			list = this.arcgisInfoMapper.getUserLastLocateByParams(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ArcgisInfoOfBuild> getArcgisDatasOfBuilds(String buildingIds, Integer mapt) {
		List<ArcgisInfoOfBuild> arcgisInfoOfBuildList = this.arcgisInfoMapper.getArcgisDatasOfBuilds(buildingIds,mapt);
		return arcgisInfoOfBuildList;
	}

	@Override
	public List<ArcgisInfoOfPoint> getArcgisDataOfPoint(Long wid, Integer mapt) {
		List<ArcgisInfoOfPoint> list= this.arcgisInfoMapper.getArcgisDataOfPoint(wid, mapt,ConstantValue.POINT_INFO_TYPE);
		return list;
	}
	
	/**
	 * 2015-01-22 liushi add 保存（含有新增跟修改的操作）资源轮廓数据
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	@Override
	public boolean saveArcgisDrawPoint(ArcgisInfoOfPoint arcgisInfoOfPoint) {
		List<ArcgisInfo> arcgisInfos = this.arcgisInfoMapper.IsExistArcgisData(arcgisInfoOfPoint.getWid(),arcgisInfoOfPoint.getMapt(),ConstantValue.POINT_INFO_TYPE);
		int row=0;
		boolean flag = false;
		if(arcgisInfos.size()>0) {//表示库表中已经存在对应的数据，需要进行修改操作
			row = this.arcgisInfoMapper.updateArcgisDataInType(arcgisInfoOfPoint);
		}else{//数据库中没有对应的数据，需要进行插入操作
			row = this.arcgisInfoMapper.insertArcgisDataInType(arcgisInfoOfPoint);
		}
		if(row == 1) {
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}

	
	@Override
	public boolean deleteArcgisDrawAreaOfPoint(ArcgisInfoOfPoint arcgisInfoOfPoint) {
		List<ArcgisInfo> arcgisInfos = this.arcgisInfoMapper.IsExistArcgisData(arcgisInfoOfPoint.getWid(),arcgisInfoOfPoint.getMapt(),ConstantValue.POINT_INFO_TYPE);
		int row = 0;
		boolean flag = false;
		if(arcgisInfos.size()>0){
			row = this.arcgisInfoMapper.deleteArcgisDataInType(arcgisInfoOfPoint);
		}else{
			row = 1;
		}
		flag = row==1?true:false;
		return flag;
	}

	@Override
	public List<ArcgisInfoOfPoint> getArcgisDrawDataOfPointsByCode(String infoOrgCode, Integer mapt) {
		List<ArcgisInfoOfPoint> arcgisInfoOfPointList = this.arcgisInfoMapper.getArcgisDrawDataOfPointsByGridId(infoOrgCode,mapt);
		return arcgisInfoOfPointList;
	}

	@Override
	public List<Map<String, Object>> getEventChildData(Map<String, Object> params) {
		Object obj=params.get("gridId");
		if(obj==null) return null;
		String date=DateUtils.getToday("yyyy-MM");
		Long gridId=Long.parseLong(obj.toString());
		params.put("date", date);
		return this.arcgisInfoMapper.getEventChildData(params);
	}
	
	private List<ArcgisInfoOfTrajectory> convertData(List<ArcgisInfoOfDayTraj> dayTrajList) {
		List<ArcgisInfoOfTrajectory> convertData = new ArrayList<ArcgisInfoOfTrajectory>();
		if (dayTrajList != null && dayTrajList.size() > 0) {
			ArcgisInfoOfDayTraj dayTraj = dayTrajList.get(0);
			if (StringUtils.isNotBlank(dayTraj.getGpsArr())) {
				Long userId = dayTraj.getWid();
				String[] gpsArrs = dayTraj.getGpsArr().split(";");
				String[] gpsTimeArrs = dayTraj.getGpsTimeArr().split(";");
				String[] inGridArrs = dayTraj.getInGridArr().split(";");
				
				for (int i = 1; i < gpsArrs.length; i++) {
					ArcgisInfoOfTrajectory trajectory = new ArcgisInfoOfTrajectory();
					trajectory.setWid(userId);
					trajectory.setLocateTime(gpsTimeArrs[i]);
					trajectory.setIsInGrid(inGridArrs[i]);
					String[] gpsArr = gpsArrs[i].split(",");
					if (gpsArr.length > 1) {
						trajectory.setX(Double.parseDouble(gpsArr[0]));
						trajectory.setY(Double.parseDouble(gpsArr[1]));
					}
					convertData.add(trajectory);
				}
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Collections.sort(convertData, new Comparator<ArcgisInfoOfTrajectory>() {
			@Override
			public int compare(ArcgisInfoOfTrajectory o1, ArcgisInfoOfTrajectory o2) {
				try {
                    Date dt1 = format.parse(o1.getLocateTime());
                    Date dt2 = format.parse(o2.getLocateTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;//小的放前面
                    }else {
                        return -1;
                    } 
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
			}
		});
		return convertData;
	}

	@Override
	public boolean saveRandomBuild(List<Long> gridIds) {
		Double x=0d,y=0d;
		boolean result=false;
		for(Long gridId:gridIds){
			List<AreaBuildingInfo> listBuild=areaBuildingInfoService.findAreaBuildInfoByGridId(gridId, null);
			List<ArcgisInfoOfGrid> gridInfoList=getArcgisDataOfGrid(gridId, 5);
			Double MaxX=0d,MinX=0d,MaxY=0d,MinY=0d;
			if(gridInfoList!=null && gridInfoList.size()>0){
				String hs=gridInfoList.get(0).getHs();
				String[] hsChar = hs.split(",");
				List<Double> xList=new ArrayList<>();
				List<Double> yList=new ArrayList<>();
				for(int i=0;i<hsChar.length;i++){
					if(i%2==0){
						xList.add(Double.parseDouble(hsChar[i]));
					}else{
						yList.add(Double.parseDouble(hsChar[i]));
					}
				}
				MaxX=Collections.max(xList);
				MinX=Collections.min(xList);
				MaxY=Collections.max(yList);
				MinY=Collections.min(yList);
			}
			for(AreaBuildingInfo build:listBuild){
				if(StringUtils.isNotEmpty(build.getX())){
					x=MinX+(Math.random()*(MaxX-MinX));
					y=MinY+(Math.random()*(MaxY-MinY));
					ArcgisInfoOfBuild arcgisInfoOfBuild=new ArcgisInfoOfBuild();
					arcgisInfoOfBuild.setWid(build.getBuildingId());
					arcgisInfoOfBuild.setX(x);
					arcgisInfoOfBuild.setY(y);
					arcgisInfoOfBuild.setMapt(5);
					saveArcgisDrawAreaOfBuild(arcgisInfoOfBuild);
					result=true;
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> saveGridOutLine(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String x = WindowHelper.getPmStr(params, "x");
		String y = WindowHelper.getPmStr(params, "y");
		String infoOrgCode = WindowHelper.getPmStr(params, "infoOrgCode");
		String hs = WindowHelper.getPmStr(params, "hs");
		String areaColor = WindowHelper.getPmStr(params, "areaColor");
		String nameColor = WindowHelper.getPmStr(params, "nameColor");
		String lineColor = WindowHelper.getPmStr(params, "lineColor");
		String lineWidth = WindowHelper.getPmStr(params, "lineWidth");
		String colorNum = WindowHelper.getPmStr(params, "colorNum");
		String mapCenterLevel = WindowHelper.getPmStr(params, "mapCenterLevel");
		if("".equals(infoOrgCode)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "组织域[infoOrgCode]不能为空");
	        return resultMap;
	    }
		if("".equals(x) ) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "中心点[x]不能为空");
	        return resultMap;
	    }
		if("".equals(y) ) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "中心点[y]不能为空");
	        return resultMap;
	    }
		if("".equals(hs)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "轮廓信息[hs]不能为空");
	        return resultMap;
	    }
		if("".equals(areaColor)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "区域颜色[areaColor]不能为空");
	        return resultMap;
	    }
		if("".equals(nameColor)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "区域颜色[nameColor]不能为空");
	        return resultMap;
	    }
		if("".equals(lineColor)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "边界线颜色[lineColor]不能为空");
	        return resultMap;
	    }
		if("".equals(lineWidth)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "边界线宽度[lineWidth]不能为空");
	        return resultMap;
	    }
		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		if (gridInfo == null || "".equals(gridInfo.getGridId())) {
			resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "组织域错误，未找到相对于的网格！");
	        return resultMap;
		}
		if(StringUtils.isBlank(mapCenterLevel)){
			mapCenterLevel = gridInfo.getGridLevel().toString();
		}
		
		ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
		arcgisInfoOfGrid.setWid(gridInfo.getGridId());
		arcgisInfoOfGrid.setMapt(5);
		arcgisInfoOfGrid.setX(Double.valueOf(x));
		arcgisInfoOfGrid.setY(Double.valueOf(y));
		arcgisInfoOfGrid.setHs(hs);
		arcgisInfoOfGrid.setAreaColor(areaColor);
		arcgisInfoOfGrid.setNameColor(nameColor);
		arcgisInfoOfGrid.setLineColor(lineColor);
		arcgisInfoOfGrid.setLineWidth(Integer.valueOf(lineWidth));
		//区域颜色透明参数
		arcgisInfoOfGrid.setColorNum(Float.valueOf(colorNum));
		//当前网格层级
		arcgisInfoOfGrid.setMapCenterLevel(Integer.valueOf(mapCenterLevel));
		boolean flag = false;
		try {
			flag = this.saveArcgisDrawAreaOfGrid(arcgisInfoOfGrid);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result",false);
	        resultMap.put("status", ConstantValue.STATUS_FAIL);
	        resultMap.put("desc", "请求失败：" + e.getMessage());
	        return resultMap;
		}
		
		resultMap.put("result",flag);
        resultMap.put("status", ConstantValue.STATUS_SUCCESS);
        resultMap.put("desc", "请求成功");
        return resultMap;
	}
	@Override
	public Map<String, Object> apiGetGridOutLine(Map<String, Object> params){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String infoOrgCode = WindowHelper.getPmStr(params, "infoOrgCode");
		if("".equals(infoOrgCode)) {
		    resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "组织域[infoOrgCode]不能为空");
	        return resultMap;
	    }
		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		if (gridInfo == null || "".equals(gridInfo.getGridId())) {
			resultMap.put("status", ConstantValue.STATUS_FAIL);
		    resultMap.put("desc", "组织域错误，未找到相对于的网格！");
	        return resultMap;
		}
		List<ArcgisInfoOfGrid> list=null;
		try {
			list=this.getArcgisDataOfGrid(gridInfo.getGridId(), 5);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result",list);
	        resultMap.put("status", ConstantValue.STATUS_FAIL);
	        resultMap.put("desc", "请求失败：" + e.getMessage());
	        return resultMap;
		}
		
		resultMap.put("result",list);
        resultMap.put("status", ConstantValue.STATUS_SUCCESS);
        resultMap.put("desc", "请求成功");
        return resultMap;
	}
	
	
	@Override
	public EUDGPagination findAreaGridAdmin(int pageNo, int pageSize,Map<String, Object> params) {
		 pageNo = pageNo < 1 ? 1 : pageNo;
	        pageSize = pageSize < 1 ? 10 : pageSize;
	        RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
	        int count = this.arcgisInfoMapper.findAreaGridAdminCount(params);
            List<Map<String,Object>> list = this.arcgisInfoMapper.findAreaGridAdminList(params, rowBounds);
		return new EUDGPagination(count, list);
	}

	@Override
	public List<ArcgisInfo> getArcgisCommonHs(Long wid, String type, Integer mapt) {
		List<ArcgisInfo> list= arcgisInfoMapper.getArcgisCommonHs(wid,type,mapt);
		return list;
	}

	@Override
	public List<Map<String, Object>> checkUsersHasTrajectory(List<CheckUsersHasTrajectoryParam> param) {
		List<Map<String, Object>>  list=new LinkedList<>();
		for (CheckUsersHasTrajectoryParam item : param) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("userId",item.getId());
			map.put("locateTimeBegin",item.getLocateTimeBegin());
			map.put("locateTimeEnd",item.getLocateTimeEnd());
			    // 查询bus_locate表
				Long count=arcgisInfoMapper.checkUsersHasTrajectoryByLocate(item);
				if (count>0){
					map.put("hasTrajectory","1");
				}else {
					map.put("hasTrajectory","0");
				}
			list.add(map);
		}
		return list;
	}
}
