package cn.ffcs.shequ.utils.arcgisAop;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
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
public class ArcgisDataOfCampusAop extends ZZBaseController {
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

                List<PlaceInfo> placeInfoList = (List<PlaceInfo>) eudgPagination.getRows();
                if (ConstantValue.YANDU_FUNC_ORG_CODE.equals(defaultInfoOrgCode)) {//盐都楼宇执行隐匿功能
                    gridPath = cacheService.getBuildScopeSetting();
                    idYD = true;
                }

                for (PlaceInfo placeInfo : placeInfoList) {
                    String placePath = placeInfo.getGridPath();
                    String roomAddress = placeInfo.getRoomAddress();
                    if (idYD) {//盐都楼宇执行隐匿功能
                        if (StringUtils.isNotBlank(placePath)) {
                            placePath = placePath.replaceAll(gridPath, "");
                            roomAddress = placePath + roomAddress;
                            placeInfo.setRoomAddress(roomAddress);
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            System.out.print("出租屋列表过滤异常：" + throwable.getMessage());
        }

        return object;
    }
}
