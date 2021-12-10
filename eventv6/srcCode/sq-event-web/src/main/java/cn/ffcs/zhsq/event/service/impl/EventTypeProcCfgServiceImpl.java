package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.IEventTypeProcCfg;
import cn.ffcs.zhsq.mybatis.domain.event.EventTypeProcCfgPO;
import cn.ffcs.zhsq.mybatis.domain.event.EventTypeProcCfgVO;
import cn.ffcs.zhsq.mybatis.persistence.event.EventTypeProcCfgMapper;

@Service(value="eventTypeProcCfgServiceImpl")
public class EventTypeProcCfgServiceImpl implements IEventTypeProcCfg {

	@Autowired
	private EventTypeProcCfgMapper eventTypeProcCfgMapper;
	
	@Autowired
	private IBaseDictionaryService dictionaryService;

	private List<EventTypeProcCfgPO> transPOs(EventTypeProcCfgVO cfgVO) {
		List<EventTypeProcCfgPO> pos = new ArrayList<EventTypeProcCfgPO>();
		if (cfgVO != null && cfgVO.getEventCloseSpecs() != null && cfgVO.getEventCloseSpecs().size() > 0) {
			for (int i = 0; i < cfgVO.getEventCloseSpecs().size(); i++) {
				EventTypeProcCfgPO po = new EventTypeProcCfgPO();
				po.setRegionCode(cfgVO.getRegionCode());
				po.setType(cfgVO.getType());
				po.setEtpcId(cfgVO.getEtpcIds().get(i));
				po.setCollectEventSpec(cfgVO.getCollectEventSpecs().get(i));
				po.setTimeLimitType(cfgVO.getTimeLimitTypes().get(i));
				po.setTimeLimitVal(cfgVO.getTimeLimitVals().get(i));
				po.setEventCloseSpec(cfgVO.getEventCloseSpecs().get(i));
				pos.add(po);
			}
		}
		return pos;
	}
	
	private EventTypeProcCfgVO transVO(List<EventTypeProcCfgPO> pos) {
		if (pos != null && pos.size() > 0) {
			EventTypeProcCfgVO vo = new EventTypeProcCfgVO();
			List<Long> etpcIds = new ArrayList<Long>();
			List<String> collectEventSpecs = new ArrayList<String>();
			List<String> timeLimitTypes = new ArrayList<String>();
			List<Integer> timeLimitVals = new ArrayList<Integer>();
			List<String> eventCloseSpecs = new ArrayList<String>();
			for (int i = 0; i < pos.size(); i++) {
				EventTypeProcCfgPO po = pos.get(i);
				if (i == 0) {
					vo.setRegionCode(po.getRegionCode());
					vo.setType(po.getType());
					vo.setRegionName(po.getRegionName());
				}
				etpcIds.add(po.getEtpcId());
				collectEventSpecs.add(po.getCollectEventSpec());
				timeLimitTypes.add(po.getTimeLimitType());
				timeLimitVals.add(po.getTimeLimitVal());
				eventCloseSpecs.add(po.getEventCloseSpec());
			}
			vo.setEtpcIds(etpcIds);
			vo.setCollectEventSpecs(collectEventSpecs);
			vo.setTimeLimitTypes(timeLimitTypes);
			vo.setTimeLimitVals(timeLimitVals);
			vo.setEventCloseSpecs(eventCloseSpecs);
			return vo;
		}
		return null;
	}

	@Override
	public boolean save(EventTypeProcCfgVO cfgVO) {
		List<EventTypeProcCfgPO> pos = this.transPOs(cfgVO);
		for (EventTypeProcCfgPO eventTypeProcCfgPO : pos) {
			this.eventTypeProcCfgMapper.insert(eventTypeProcCfgPO);
		}
		return true;
	}

	@Override
	public boolean update(EventTypeProcCfgVO cfgVO) {
		this.delete(cfgVO.getRegionCode(), cfgVO.getType());
		List<EventTypeProcCfgPO> pos = this.transPOs(cfgVO);
		for (EventTypeProcCfgPO eventTypeProcCfgPO : pos) {
			this.eventTypeProcCfgMapper.insert(eventTypeProcCfgPO);
		}
		return true;
	}

	@Override
	public EventTypeProcCfgVO findVO(String regionCode, String type, String userOrgCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionCode", regionCode);
		params.put("type", type);
		params.put("schType", "eq");
		List<EventTypeProcCfgPO> pos = this.eventTypeProcCfgMapper.findPO(params);
		return this.formatOut(this.transVO(pos), userOrgCode);
	}

	@Override
	public EUDGPagination findListPagination(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		params.put("schEventType", "like");
		int count = eventTypeProcCfgMapper.findCountByCriteria(params);
		List<EventTypeProcCfgPO> list = eventTypeProcCfgMapper.findPageListByCriteria(params, rowBounds);
		List<EventTypeProcCfgVO> rows = new ArrayList<EventTypeProcCfgVO>(list.size());
		for (int i = 0; i < list.size(); i++) {
			EventTypeProcCfgPO po = list.get(i);
			EventTypeProcCfgVO vo = new EventTypeProcCfgVO();
			vo.setRegionCode(po.getRegionCode());
			vo.setType(po.getType());
			vo.setRegionName(po.getRegionName());
			rows.add(vo);
		}
		formatOut(rows, String.valueOf(params.get("userOrgCode")));
		EUDGPagination eudgPagination = new EUDGPagination(count, rows);
		return eudgPagination;
	}

	@Override
	public boolean delete(String regionCode, String type) {
		this.eventTypeProcCfgMapper.deletePO(regionCode, type);
		return true;
	}
	
	private void formatOut(List<EventTypeProcCfgVO> vos, String orgCode) {
		if (vos != null && vos.size() > 0) {
			List<BaseDataDict> eventDicts = dictionaryService.getDataDictTree("A001093199", orgCode);
			for (EventTypeProcCfgVO vo : vos) {
				for (BaseDataDict dict : eventDicts) {
					if (dict.getDictGeneralCode().equals(vo.getType())) {
						vo.setTypeName(dict.getDictName());
						break;
					}
				}
			}
		}
	}
	
	private EventTypeProcCfgVO formatOut(EventTypeProcCfgVO vo, String orgCode) {
		if (vo != null) {
			List<BaseDataDict> eventDicts = dictionaryService.getDataDictTree("A001093199", orgCode);
			for (BaseDataDict dict : eventDicts) {
				if (dict.getDictGeneralCode().equals(vo.getType())) {
					vo.setTypeName(dict.getDictName());
					break;
				}
			}
		}
		return vo;
	}

}
