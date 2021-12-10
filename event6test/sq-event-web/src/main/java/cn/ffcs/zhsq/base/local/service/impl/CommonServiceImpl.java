/**
 * 
 */
package cn.ffcs.zhsq.base.local.service.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.ffcs.shequ.statistics.domain.pojo.StatisticsInfo;
import cn.ffcs.zhsq.base.local.service.ICommonService;

/**
 * 通用服务实现类
 * @author guohh
 *
 */
//-- modify by huangwenbin 2019-9-16 无用接口先注释 ，commonService applicationContext-dubbo.xml和其他重名
@Service(value="common2Service")
public class CommonServiceImpl implements ICommonService {

	@Override
	public void filterGridStatInfo(List<StatisticsInfo> orgStatInfoList, String orgCode) {
		//-- modify by guohh 2014.1.2 海沧街道过滤空房间数
		//if(orgStatInfoList!=null && (orgCode.startsWith("350205001") || orgCode.startsWith("35020501"))) {
			int emptyIndex = -1;
			for(int i=0; i<orgStatInfoList.size(); i++) {
				StatisticsInfo statInfo = orgStatInfoList.get(i);
				if(statInfo.getStatTypeName().contains("空房间")) {
					emptyIndex = i;
					break;
				}
			}
			if(emptyIndex>0) orgStatInfoList.remove(emptyIndex);
		//}
	}

}
