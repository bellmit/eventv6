package cn.ffcs.zhsq.warningScheme.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.mybatis.persistence.warningScheme.SchemeKeywordMapper;
import cn.ffcs.zhsq.mybatis.persistence.warningScheme.SchemeMatchMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.warningScheme.service.IWarningSchemeService;

/**
 * @Description: 预警方案服务实现类
 * @Author: youwj
 * @Date: 05-28 15:32:03
 * @Copyright: 2019 福富软件
 */
@Service("warningSchemeServiceImpl")
@Transactional
public class WarningSchemeServiceImpl implements IWarningSchemeService {
	
	@Autowired
    private SchemeMatchMapper schemeMatchMapper;
	
	@Autowired
	private SchemeKeywordMapper schemeKeywordMapper;
	
	@Autowired
	private IEventDisposalService eventDisposalService;

	@Override
	public EUDGPagination findSchemePagination(int page, int rows, Map<String, Object> params) {

        Long count = findSchemeCount(params);
        List<SchemeMatch> schemeMatchList = new ArrayList<>();

        if (count > 0) {
        	
        	page = page < 1 ? 1 : page;
        	rows = rows < 1 ? 20 : rows;
            RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
            
            schemeMatchList = schemeMatchMapper.searchList(params, rowBounds);
           /* List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> value = new HashMap<>();
            for(int i=0,l=schemeMatchList.size();i<l;i++){
            	value.put("value_"+i, schemeMatchList.get(i).getRemark());
            }
            list.add(value);*/
        }

        EUDGPagination schemeMatchPagination = new EUDGPagination(count, schemeMatchList);

        return schemeMatchPagination;
		
	}
	
	/**
     * 统计预警方案数量
     * @param params
     * @return
     */
    private Long findSchemeCount(Map<String, Object> params) {
    	
		Long count = schemeMatchMapper.countList(params);
		
		return count;
	}

	@Override
	public SchemeMatch findSchemeById(Long schemeId) {
		return schemeMatchMapper.searchById(schemeId);
	}

	@Override
	public List<SchemeKeyword> findSchemeKeywordBySchemeId(Long schemeId) {
		return schemeKeywordMapper.searchBySchemeId(schemeId);
	}

	@Override
	public Long saveScheme(SchemeMatch schemeMatch) {
		schemeMatchMapper.insert(schemeMatch);
		return schemeMatch.getSchemeId();
	}

	@Override
	public void saveSchemeKeyword(List<SchemeKeyword> list) {
		
		schemeKeywordMapper.insertByList(list);
	}

	@Override
	public Long saveSchemeKeyword(SchemeKeyword schemeKeyword) {
		schemeKeywordMapper.insert(schemeKeyword);
		return schemeKeyword.getSchemeKeywordId();
	}

	@Override
	public Long updateScheme(SchemeMatch schemeMatch) {
		return schemeMatchMapper.update(schemeMatch);
	}

	@Override
	public List<String> findAllCode() {
		return schemeKeywordMapper.findAllCode();
	}

	@Override
	public Long updateSchemeKeyword(SchemeKeyword schemeKeyword) {
		return schemeKeywordMapper.update(schemeKeyword);
	}

	@Override
	public Long deleteSchemeById(Long schemeId) {
		return schemeMatchMapper.deleteByUpdateStatus(schemeId);
	}

	@Override
	public List<SchemeMatch> findSchemeEffect() {
		return schemeMatchMapper.findSchemeEffect();
	}

	@Override
	public Map<String,Object> fetchKeyWord(List<String> keyword, Long eventId) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("keyword", keyword);
		params.put("eventId", eventId);
		
		return schemeKeywordMapper.fetchKeyword(params);
	}

	@Override
	public EventDisposal findEventById(Long eventId) {
            EventDisposal event = null;
		
		if(eventId != null && eventId > 0) {
			
			event = eventDisposalService.findEventByIdSimple(eventId);
		}
		
		return event;
	}

	@Override
	public void updateSchemeKeyword(List<SchemeKeyword> list) {
		
		schemeKeywordMapper.updateByList(list);
		
	}

	

}