package cn.ffcs.zhsq.idssControl.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.doorsys.bo.control.ControlApplyInfo;
import cn.ffcs.doorsys.bo.control.ControlTargetRecord;
import cn.ffcs.doorsys.bo.control.ControlTargetRef;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.idssControl.service.IIdssControlService;
import cn.ffcs.zhsq.mybatis.persistence.idssControl.IdssControlMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value = "idssControlServiceImpl")
public class IdssControlServiceImpl implements IIdssControlService {
	
	@Autowired
	private IdssControlMapper idssControlMapper;
	
	@Override
	public EUDGPagination findIdssControlApplyInfo(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1 ? 1 : pageNo;
		pageSize = pageSize<1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		if(CommonFunctions.isNotBlank(params, "controlBeginTimeStr")) {
			Date controlBeginTime = null;
			try {
				controlBeginTime = DateUtils.convertStringToDate(params.get("controlBeginTimeStr").toString(), DateUtils.PATTERN_DATE);
			} catch (ParseException e) {
				params.remove("controlBeginTimeStr");
				e.printStackTrace();
			}
			
			if(controlBeginTime != null) {
				params.put("controlBeginTime", controlBeginTime);
			}
		}
		if(CommonFunctions.isNotBlank(params, "controlEndTimeStr")) {
			Date controlEndTime = null;
			try {
				controlEndTime = DateUtils.convertStringToDate(params.get("controlEndTimeStr").toString(), DateUtils.PATTERN_DATE);
			} catch (ParseException e) {
				params.remove("controlEndTimeStr");
				e.printStackTrace();
			}
			
			if(controlEndTime != null) {
				params.put("controlEndTime", controlEndTime);
			}
		}
		if(CommonFunctions.isNotBlank(params, "controlStatusList")) {
			params.put("controlStatusList", params.get("controlStatusList").toString().split(","));
		}
		
		List<ControlApplyInfo> applyInfofList = idssControlMapper.findApplyInfoByCriteria(params, rowBounds);
		int count = idssControlMapper.findApplyInfoCount(params);
		
		EUDGPagination applyInfoPagination = new EUDGPagination(count, applyInfofList);
		
		return applyInfoPagination;
	}
	
	@Override
	public EUDGPagination findIdssControlTargetRef(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1 ? 1 : pageNo;
		pageSize = pageSize<1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		if(CommonFunctions.isNotBlank(params, "controlBeginTimeStr")) {
			Date controlBeginTime = null;
			try {
				controlBeginTime = DateUtils.convertStringToDate(params.get("controlBeginTimeStr").toString(), DateUtils.PATTERN_DATE);
			} catch (ParseException e) {
				params.remove("controlBeginTimeStr");
				e.printStackTrace();
			}
			
			if(controlBeginTime != null) {
				params.put("controlBeginTime", controlBeginTime);
			}
		}
		if(CommonFunctions.isNotBlank(params, "controlEndTimeStr")) {
			Date controlEndTime = null;
			try {
				controlEndTime = DateUtils.convertStringToDate(params.get("controlEndTimeStr").toString(), DateUtils.PATTERN_DATE);
			} catch (ParseException e) {
				params.remove("controlEndTimeStr");
				e.printStackTrace();
			}
			
			if(controlEndTime != null) {
				params.put("controlEndTime", controlEndTime);
			}
		}
		
		List<ControlTargetRef> targetRefList = idssControlMapper.findTargetRefByCriteria(params, rowBounds);
		int count = idssControlMapper.findTargetRefCount(params);
		
		EUDGPagination targetRefPagination = new EUDGPagination(count, targetRefList);
		
		return targetRefPagination;
	}

	@Override
	public List<ControlTargetRecord>  findIdssControlTargetRecord(Map<String, Object> params) {
		return idssControlMapper.findTargeRecordList(params);
	}
	
	@Override
	public ControlTargetRecord findIdssControlTargetRecordById(Long recordId) {
		return idssControlMapper.findTargeRecordById(recordId);
	}

}
