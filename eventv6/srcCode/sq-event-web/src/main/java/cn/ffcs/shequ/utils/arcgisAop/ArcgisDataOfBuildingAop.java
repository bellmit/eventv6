package cn.ffcs.shequ.utils.arcgisAop;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by WF on 2019/06/27.
 */
public class ArcgisDataOfBuildingAop extends ZZBaseController {
    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private CacheService cacheService;

    public Object doIntercept(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
            if (object != null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
                String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
                boolean idYD = false;//是否盐都
                String gridPath = "";
                EUDGPagination eudgPagination = (EUDGPagination) object;
                if (eudgPagination.getTotal() == 0) {
                    return object;
                }
                List<AreaBuildingInfo> buildingList = (List<AreaBuildingInfo>) eudgPagination.getRows();
                if (ConstantValue.YANDU_FUNC_ORG_CODE.equals(defaultInfoOrgCode)) {//盐都楼宇执行隐匿功能
                    gridPath = cacheService.getBuildScopeSetting();
                    idYD = true;
                }

                for (AreaBuildingInfo areaBuildingInfo : buildingList) {
                    String buildingAddress = areaBuildingInfo.getBuildingAddress();
                    if (!StringUtils.isBlank(areaBuildingInfo.getUseNature())) {
                        areaBuildingInfo.setUseNatureLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, areaBuildingInfo.getUseNature()));
                    }
                    if (idYD) {//盐都楼宇执行隐匿功能
                        if (StringUtils.isNotBlank(buildingAddress)) {
                            buildingAddress = buildingAddress.replaceAll(gridPath, "");
                            areaBuildingInfo.setBuildingAddress(buildingAddress);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            System.out.print("楼宇列表过滤异常：" + e.getMessage());
        }
        return object;
    }
}
