package cn.ffcs.zhsq.CoordinateInverseQuery.service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.persistence.CoordinateInverseQuery.CoordinateInverseQueryMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 通过坐标反向查询接口实现类
 *
 * @Author sulch
 * @Date 2017-02-09 17:24
 */
@Service(value = "coordinateInverseQueryServiceImpl")
public class CoordinateInverseQueryServiceImpl implements ICoordinateInverseQueryService {
    
	@Autowired
    private CoordinateInverseQueryMapper coordinateInverseQueryMapper;
    
    @Autowired
	private IFunConfigurationService funConfigurationService;
    
    @Autowired
	private IMixedGridInfoService mixedGridInfoService;
    
    @Autowired
    private UserManageOutService userManageOutService;

    @Override
    public List<CoordinateInverseQueryGridInfo> findGridInfo(Map<String, Object> params) {
        List<CoordinateInverseQueryGridInfo> list = new ArrayList<CoordinateInverseQueryGridInfo>();
        String x = (String) params.get("x");
        String y = (String) params.get("y");
        if(StringUtils.isNotBlank(x) && StringUtils.isNotBlank(y)){
            try {
            	params.put("mapt", params.get("mapt") == null ? 5 : Integer.valueOf(String.valueOf(params.get("mapt"))));
                list = coordinateInverseQueryMapper.findGridInfo(params);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(list != null && list.size() > 0){
            return list;
        }else{
            return null;
        }
    }

	@Override
	public List<UserBO> findUserInfoByRegionCodeAndPosition(String longitude, String latitude, String orgCode,
			Map<String, Object> params) throws Exception {
		List<UserBO> userInfoList=null;
		int mapt=5;	//默认地图类型二位地图	
		int gridLevel=3; //默认选择人的目标层级
		String positionCode=""; 
		String gridRegionCode="";
		
		//获取地图类型
		if(CommonFunctions.isNotBlank(params, "mapt")) {
			mapt=Integer.valueOf(params.get("mapt").toString());
		}
		
		//获取网格层级
		if(CommonFunctions.isNotBlank(params, "gridLevel")) {
			gridLevel=Integer.valueOf(params.get("gridLevel").toString());
		}
		
		//获取职位编码
		if(CommonFunctions.isNotBlank(params, "positionCode")) {
			positionCode=params.get("positionCode").toString();
		}else {//通过功能配置获取
			positionCode=funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.CONNTACT_USER_POSITION, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		}
		
		if(StringUtils.isBlank(positionCode)) {
			throw new Exception("未找到相应的职位编码");
		}
		
		//通过经纬度转出所属网格
		Map<String, Object> searchParams=new HashMap<String, Object>();
		searchParams.put("x", longitude);
		searchParams.put("y", latitude);
		searchParams.put("mapt", mapt);
		List<CoordinateInverseQueryGridInfo> gridInfo = this.findGridInfo(searchParams);
		
		Map<String,Object> regionCode=new HashMap<String,Object>();
		
		if(null !=gridInfo&&gridInfo.size()>0) {
			
			for (CoordinateInverseQueryGridInfo grid : gridInfo) {
				MixedGridInfo gridInfoById = mixedGridInfoService.findMixedGridInfoById(grid.getGridId(), true);
				if(null!=gridInfoById) {
					
					if(gridInfoById.getGridLevel()==gridLevel) {
						regionCode.put("destLevel", gridInfoById.getInfoOrgCode());
					}else if(gridInfoById.getGridLevel()>gridLevel){
						regionCode.put("otherPid", gridInfoById.getParentGridId());
						regionCode.put("otherLevel", gridInfoById.getGridLevel());
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(regionCode, "destLevel")) {
				gridRegionCode=regionCode.get("destLevel").toString();
			}else {
				
				if(CommonFunctions.isBlank(regionCode, "otherPid")) {
					
					return new ArrayList<UserBO>();
					
				}else {
					
					Long parentId = Long.valueOf(regionCode.get("otherPid").toString());
					Integer otherLevel = Integer.valueOf(regionCode.get("otherLevel").toString());
					for(;gridLevel<otherLevel;otherLevel--) {
						
						MixedGridInfo gridInfoByPid=mixedGridInfoService.findMixedGridInfoById(parentId,true);
						parentId=gridInfoByPid.getParentGridId();
						gridRegionCode= gridInfoByPid.getInfoOrgCode().toString();
						
					}
				}
				
				
			}
			
			if(StringUtils.isNotBlank(gridRegionCode)) {
				userInfoList = userManageOutService.getByRegionAndPositionCode(positionCode, gridRegionCode);
			}else {
				throw new Exception("未找到相应层级的所属网格");
			}
			
			
			
		}else {
			throw new Exception("所传入的经纬度未找到所属网格");
		}
		
		
		return userInfoList;
	}
}
