package cn.ffcs.zhsq.map.labelLocation.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.labelLocation.service.ILabelLocationService;
import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.BuildingLLInfo;
import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.ComponentsLLInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.labelLocation.LabelLocationMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标注地理位置组件服务接口实现类
 *
 * @Author sulch
 * @Date 2016-11-10 14:35
 */
@Service(value = "labelLocationServiceImpl")
public class LabelLocationServiceImpl implements ILabelLocationService {
    @Autowired
    private LabelLocationMapper labelLocationMapper;

    @Override
    public EUDGPagination findLabelLocationPagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        pageSize = pageSize < 1 ? 20 : pageSize;
        RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
        EUDGPagination pagination = null;
        String findType = (String)params.get("findType");
        if(StringUtils.isNotBlank(findType)){
            if("building".equals(findType)){
                List<BuildingLLInfo> list = new ArrayList<BuildingLLInfo>();
                try {
                    list = labelLocationMapper.findPageBuildingCriteria(params, rowBounds);
                    int count = labelLocationMapper.findCountBuildingCriteria(params);
                    pagination = new EUDGPagination(count, list);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if("components".equals(findType)){
                try {
                    List<ComponentsLLInfo> list = new ArrayList<ComponentsLLInfo>();
                    list = labelLocationMapper.findPageComponentsCriteria(params, rowBounds);
                    int count = labelLocationMapper.findCountComponentsCriteria(params);
                    pagination = new EUDGPagination(count, list);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return pagination;
    }

    @Override
    public BuildingLLInfo findBuildingLLInfo(Map<String, Object> params) {
        BuildingLLInfo buildingLLInfo = new BuildingLLInfo();
        buildingLLInfo = labelLocationMapper.findBuildingLLInfo(params);
        return buildingLLInfo;
    }
}
