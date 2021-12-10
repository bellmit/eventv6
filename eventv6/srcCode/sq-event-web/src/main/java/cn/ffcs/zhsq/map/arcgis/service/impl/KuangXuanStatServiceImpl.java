package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value="kuangXuanStat")
public class KuangXuanStatServiceImpl extends ApplicationObjectSupport implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 2014-11-10 liushi 
	 * 框选统计集合器，通过传递的spring注入的bean name获取对象，然后调用方法
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String kuangxuanType = (String)params.get("kuangxuanType");
		IKuangXuanStatService kuangXuanStatService = (IKuangXuanStatService)this.getApplicationContext().getBean(kuangxuanType);
		int resultCount = kuangXuanStatService.statOfKuangXuan(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		String kuangxuanType = (String)params.get("kuangxuanType");
		IKuangXuanStatService kuangXuanStatService = (IKuangXuanStatService)this.getApplicationContext().getBean(kuangxuanType);
		return kuangXuanStatService.statOfKuangXuanList(pageNo, pageSize, params);
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		IKuangXuanStatService kuangXuanStatService = (IKuangXuanStatService) this.getApplicationContext().getBean(kuangXuanType);
		return kuangXuanStatService.getKuangXuanPagePath(kuangXuanType);
	}
}
