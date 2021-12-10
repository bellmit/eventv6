package cn.ffcs.zhsq.crowd.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.TaskInfo;
import cn.ffcs.zhsq.crowd.service.IVisitRecordService;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.crowd.VisitRecordMapper;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.mybatis.persistence.crowd.EventVisitRelaMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value="visitRecordServiceImpl")
public class VisitRecordServiceImpl implements IVisitRecordService{
	
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private VisitRecordMapper visitRecordMapper;
	
	@Autowired
	private EventDisposalMapper eventDisposalMapper;
	
	@Autowired
	private EventVisitRelaMapper eventVisitRelaMapper;
	
	@Override
	public VisitRecord findVisitRecordById(Long prId) {
		VisitRecord record = visitRecordMapper.findById(prId);
		if(record!=null){
			record.setVisitedTypeStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_TYPE, record.getVisitedType()));
			record.setVisitFormStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_FORM, record.getVisitForm()));
			record.setVisitEffectStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_EFFECT, record.getVisitEffect()));
			record.setRecentStateStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_RECENT_STATE, record.getRecentState()));
		}
		return record;
	}

	@Override
	public EUDGPagination findVisitRecordPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		int count = visitRecordMapper.findCountByCriteria(params);
		List<VisitRecord> list = visitRecordMapper.findPageListByCriteria(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public boolean deleteVisitRecorddById(Long userId, List<Long> recordIdList) {
		
		try {
			for(Long id:recordIdList){
				//visitRecordMapper.delete(userId, id);
				visitRecordMapper.delete(id);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean isVisitRecordExists(Long ciRsId) {
		return false;
	}

	@Override
	public Long saveVisitRecordReturnId(VisitRecord record) {
		int row = visitRecordMapper.insert(record);
		return row > 0 ? record.getVisitId() : -1L;
	}

	@Override
	public boolean updateVisitRecord(VisitRecord record) {
		int row = visitRecordMapper.update(record);
		return row>0?true:false;
	}

	@Override
	public VisitRecord findVisitRecordByEventId(Long eventId) {
		VisitRecord visitRecord = visitRecordMapper.findVisitRecordByEventId(eventId);
		if(visitRecord!=null){
			visitRecord.setVisitedTypeStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_TYPE, visitRecord.getVisitedType()));
			visitRecord.setVisitFormStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_FORM, visitRecord.getVisitForm()));
			visitRecord.setVisitEffectStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_EFFECT, visitRecord.getVisitEffect()));
			visitRecord.setRecentStateStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_RECENT_STATE, visitRecord.getRecentState()));
		}
		return visitRecord;
	}

	@Override
	public Long saveAndReport(VisitRecord record, EventDisposal event,TaskInfo taskInfo,
			UserInfo userInfo) {
		
		//写入事件
		eventDisposalMapper.insert(event);
		
		//写入任务清单表
		taskInfo.setEventId(event.getEventId());
		
		//写入重点人员表
		int row = visitRecordMapper.insert(record);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", event.getEventId());
		params.put("visitId", record.getVisitId());
		int result = eventVisitRelaMapper.visitRelaInsert(params);
		
		return result>0?1L:0L;
	}
	
	@Override
	public Long saveAndReport(VisitRecord record, EventDisposal event,ArrayList<TaskInfo> taskInfoList,
			UserInfo userInfo) {
		
		//写入事件
		eventDisposalMapper.insert(event);
		
		//写入重点人员表
		int row = visitRecordMapper.insert(record);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", event.getEventId());
		params.put("visitId", record.getVisitId());
		int result = eventVisitRelaMapper.visitRelaInsert(params);
		
		return result>0?1L:0L;
	}

	@Override
	public VisitRecord findByCiRsId(Long ciRsId) {
		VisitRecord visitRecord = visitRecordMapper.findByCiRsId(ciRsId);
		if(visitRecord!=null){
			visitRecord.setVisitedTypeStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_TYPE, visitRecord.getVisitedType()));
			visitRecord.setVisitFormStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_FORM, visitRecord.getVisitForm()));
			visitRecord.setVisitEffectStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_EFFECT, visitRecord.getVisitEffect()));
			visitRecord.setRecentStateStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_RECENT_STATE, visitRecord.getRecentState()));
		}
		return visitRecord;
	}

	@Override
	public List<VisitRecord> findVisitRecordList(Long ciRsId, String visitType) {
		List<VisitRecord> recordList = visitRecordMapper.findVisitRecordList(ciRsId, visitType);
		if(recordList!=null && recordList.size()>0) {
			for(VisitRecord record : recordList) {
				record.setVisitedTypeStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_TYPE, record.getVisitedType()));
				record.setVisitFormStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_FORM, record.getVisitForm()));
				record.setVisitEffectStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_VISIT_EFFECT, record.getVisitEffect()));
				record.setRecentStateStr(dictionaryService.getTableColumnText(ConstantValue.TABLE_VISIT_RECORD, ConstantValue.COLUMN_VISIT_RECORD_RECENT_STATE, record.getRecentState()));
			}
		}
		return recordList;
	}
}
