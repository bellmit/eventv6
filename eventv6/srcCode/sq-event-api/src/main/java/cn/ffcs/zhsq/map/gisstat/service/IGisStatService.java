package cn.ffcs.zhsq.map.gisstat.service;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisHeatMapInfo;

import java.util.List;
import java.util.Map;

public interface IGisStatService {

	List<Map<String, Object>> findRentRoom(Long gridId);

	List<Map<String, Object>> getRealPeople(Long gridId);

	List<Map<String, Object>> getRealPeopleDetail(Long gridId);

	Map<String, Object> getRentRoomRelatedInfo(Long gridId, Long parentGridId, String infoOrgCode);
	
	List<Map<String, Object>> getMajorRelatedEvents(Long gridId);
	
	List<Map<String, Object>> getSchoolRelatedEvents(Long gridId);
	
	List<Map<String, Object>> getRelatedRoadEvents(Long gridId);
	
	List<Map<String, Object>> getKepPop(Long gridId, String sYear, String pCode);

	List<Map<String, Object>> getPartyMember(Long gridId);

	List<Map<String, Object>> getStatCor(Long gridId);
	
	List<Map<String, Object>> getStatPartyOrg(Long gridId);

	List<ArcgisHeatMapInfo> querySpectialPopulationData(String spectialPopulations, String infoOrgCode);

	List<ArcgisHeatMapInfo> queryPopulationData(String infoOrgCode);
	
	List<Map<String, Object>> getUrbanCount(Long gridId, String urbanCode);

	List<Map<String, Object>> getUrbanCountByCodes(String infoOrgCode, String urbanCode);

	List<Map<String, Object>> getRectify(Long gridId);

	List<Map<String, Object>> getCamps(Long gridId);

	List<Map<String, Object>> getJiangxiPetition(Long gridId);

	/**
	 * 获取全球眼的统计数据
	 * @param gridId
	 * @return
	 */
	List<Map<String, Object>> getGlobalEyes(Long gridId);
	
	
	
	/**
	 * 获取矛盾纠纷统计数据
	 * @param gridId
	 * @return
	 */
	List<Map<String, Object>> getDisputeRectify(Long gridId);
	/**
	 * 获取矛盾纠纷统计数据
	 * @param gridId ,type Id取子级，还是取自己 my 自己，null 为子级
	 * @return
	 */
	List<Map<String, Object>> getDisputeRectify(Long gridId,String type);

}
