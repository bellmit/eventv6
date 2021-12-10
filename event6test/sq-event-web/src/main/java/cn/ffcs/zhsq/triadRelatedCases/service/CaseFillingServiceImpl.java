package cn.ffcs.zhsq.triadRelatedCases.service;

import java.text.ParseException;
import java.util.*;

import cn.ffcs.common.utils.DateUtils;
import cn.ffcs.file.mybatis.domain.attachment.AttachmentByUUID;
import cn.ffcs.file.service.IAttachmentByUUIDService;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.CaseFilling;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.TrialRecord;
import cn.ffcs.zhsq.mybatis.persistence.triadRelatedCases.CaseFillingMapper;
import cn.ffcs.zhsq.mybatis.persistence.triadRelatedCases.TrialRecordMapper;
import cn.ffcs.zhsq.triadRelatedCases.ICaseFillingService;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;

import static cn.ffcs.zhsq.utils.CommonFunctions.isNotBlank;
import static cn.ffcs.zhsq.utils.CommonFunctions.mapListSort4Pinyin;


/**
 * @Description: 涉黑案件填报表模块服务实现
 * @Author: chenshikai
 * @Date: 09-08 17:46:05
 * @Copyright: 2021 福富软件
 */
@Service("caseFillingServiceImpl")
@Transactional
public class CaseFillingServiceImpl implements ICaseFillingService {

	@Autowired
	private CaseFillingMapper caseFillingMapper; //注入涉黑案件填报表模块dao
	@Autowired
	private TrialRecordMapper trialRecordMapper; //注入庭审信息表模块dao
	@Autowired
	private IAttachmentByUUIDService attachmentByUUIDService;//附件上传


	/**
	 * 新增数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 涉黑案件填报表id
	 */
	@Override
	public Long insert(CaseFilling bo) {
		//保存基本信息
		caseFillingMapper.insert(bo);

		return bo.getUndCaseId();
	}

	/**
	 * 修改数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(CaseFilling bo) {
		long result = caseFillingMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Long userId,List<String> undCaseUuidList) {
		long result = caseFillingMapper.delete(userId,undCaseUuidList);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 涉黑案件填报表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(LayuiPage page, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
		List<CaseFilling> list = caseFillingMapper.searchList(params, rowBounds);
//		formatListData(list,true);
		long count = caseFillingMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}


	/**
	 * 根据业务id查询数据
	 * @param id 涉黑案件填报表id
	 * @return 涉黑案件填报表业务对象
	 */
	@Override
	public List<TrialRecord> searchListById(String undCaseUuid) {
		List<TrialRecord> trialRecords = trialRecordMapper.searchListById(undCaseUuid);
		return trialRecords;
	}


	/**
	 * 根据业务id查询数据
	 * @param id 涉黑案件填报表id
	 * @return 涉黑案件填报表业务对象
	 */
	@Override
	public CaseFilling searchById(String undCaseUuid) {
		CaseFilling bo = caseFillingMapper.searchById(undCaseUuid);
		return bo;
	}


	/**
	 * 新增或者编辑涉黑案件
	 * @param bo
	 * @return
	 */
	@Override
	public boolean insertOrUpdate(CaseFilling bo) {
		long effectRow1;
		long effectRow2;
		List<AttachmentByUUID> attachmentByUUIDS = bo.getAttList();
		if(StringUtils.isBlank(bo.getUndCaseUuid())){
			effectRow1 = caseFillingMapper.insert(bo);
			effectRow2 = trialRecordMapper.batchInsert(bo);
			if(attachmentByUUIDS!=null){
				//保存附件
				for (AttachmentByUUID attachment:attachmentByUUIDS) {
					attachment.setBizUUID(bo.getUndCaseUuid());
				}
				attachmentByUUIDService.saveAttachment(bo.getAttList());
			}

		}else{
			effectRow1 = caseFillingMapper.update(bo);
			trialRecordMapper.deleteByUndCaseUuid(bo.getUndCaseUuid(),bo.getUpdator());
			effectRow2 = trialRecordMapper.batchInsert(bo);
			//更新附件(先删除后插)
			attachmentByUUIDService.deleteByBizId(bo.getUndCaseUuid(),"proseCution");//删除起诉意见书
			attachmentByUUIDService.deleteByBizId(bo.getUndCaseUuid(),"indictMent");//删除起诉书
			attachmentByUUIDService.deleteByBizId(bo.getUndCaseUuid(),"judgMent");//删除判决书
			if(attachmentByUUIDS!=null){
				//保存附件
				for (AttachmentByUUID attachment:attachmentByUUIDS) {
					attachment.setBizUUID(bo.getUndCaseUuid());
				}
				attachmentByUUIDService.saveAttachment(bo.getAttList());
			}
		}
		return effectRow1>0 && effectRow2>0;
	}

	/**
	 * 江西案件jsonp接口
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getJsnpData(Map<String, Object> params){
		List<Map<String,Object>> mapList = new ArrayList<>();
		String choseType = String.valueOf(params.get("type"));
		String year = String.valueOf(params.get("year"));
		if(StringUtils.isNotBlank(year)){
			Date beginTime = null;
			Date endTime = null;
			try {
				beginTime = DateUtils.convertStringToDate(year+"-01-01" +" 00:00:00", DateUtils.PATTERN_24TIME);
				String lastYear = String.valueOf((Integer.valueOf(year)+1));
				endTime = DateUtils.convertStringToDate(lastYear+"-01-01" +" 00:00:00", DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			params.put("beginTime",beginTime);//开始时间
			params.put("endTime",endTime);//结束时间
		}
		//涉黑案件类型--按地图撒点类型统计
		if(choseType.equals(ConstantValue.MAP_STATISTICS) && StringUtils.isNotBlank((String) params.get("orgCode"))){
			List<Map<String,Object>> regionList	= caseFillingMapper.countByRegionList((String) params.get("orgCode"));
			//加统计的上级的数据总数
			params.put("gridCode","36");
			Long sumTotal = caseFillingMapper.countTotal(params);//获取省级数据总数
			List<Map<String,Object>> mapLists = new ArrayList<>();
			for (Map<String,Object>region:regionList) {
				params.put("gridCode",region.get("ORG_CODE"));
				Long total = caseFillingMapper.countTotal(params);//获取地方总数
				region.put("TOTAL",total);
				mapLists.add(region);
			}
			//按地域数量排序
			mapListSort4Pinyin(mapLists,"TOTAL",2);
			//屏接数据
			Map<String,Object> sumTotalMap = new HashMap<>();
			sumTotalMap.put("sumTotal",sumTotal);
			sumTotalMap.put("mapLists",mapLists);
			mapList.add(sumTotalMap);
		}
		//涉黑案件类型--案件类型统计
		if(choseType.equals(ConstantValue.CASE_TYPE_STATISTICS)){
			mapList = caseFillingMapper.countBytypeList(params);
		}
		//涉黑案件类型--案件详情
		if(choseType.equals(ConstantValue.CASE_TYPE_DETAIL)){
			CaseFilling bo =this.searchById(params.get("undCaseUuid").toString());
			List<TrialRecord> trialRecords = this.searchListById(params.get("undCaseUuid").toString());
			bo.setTrialRecords(trialRecords);
			Map<String,Object> map = new HashMap<>();
			map.put("caseDetail",bo);
			mapList.add(map);
		}
		//涉黑案件类型--列表
		if(choseType.equals(ConstantValue.CASE_TYPE_LIST)){
			String page = params.get("page").toString();
			String rows = params.get("rows").toString();
			if (StringUtils.isBlank(page)){
				page="1";
			}
			if (StringUtils.isBlank(rows)){
				rows="20";
			}
			EUDGPagination eudgPagination  = getList(Integer.valueOf(page),Integer.valueOf(rows),params);
			Map<String,Object> map = new HashMap<>();
			map.put("caseList",eudgPagination);
			mapList.add(map);
		}
		return mapList;
	}

	private EUDGPagination getList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		String caseType = "";
		if(isNotBlank(params,"caseType")){
			caseType = params.get("caseType").toString();
		}
		//涉黑案件类型--在侦
		if(caseType.equals(ConstantValue.CASE_TYPE_ZZ)){
			List<CaseFilling> list = caseFillingMapper.searchzzList(params, rowBounds);
			long count = caseFillingMapper.countzzList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--在诉
		if(caseType.equals(ConstantValue.CASE_TYPE_ZS)){
			List<CaseFilling> list = caseFillingMapper.searchzsList(params, rowBounds);
			long count = caseFillingMapper.countzsList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--一审
		if(caseType.equals(ConstantValue.CASE_TYPE_YS)){
			List<CaseFilling> list = caseFillingMapper.searchysList(params, rowBounds);
			long count = caseFillingMapper.countysList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--二审
		if(caseType.equals(ConstantValue.CASE_TYPE_ES)){
			List<CaseFilling> list = caseFillingMapper.searchesList(params, rowBounds);
			long count = caseFillingMapper.countesList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--审结
		if(caseType.equals(ConstantValue.CASE_TYPE_SJ)){
			List<CaseFilling> list = caseFillingMapper.searchsjList(params, rowBounds);
			long count = caseFillingMapper.countsjList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--所有类型综合(在侦,在诉,一审,二审,审结)
		if(caseType.equals(ConstantValue.CASE_TYPE_ALL)){
			List<CaseFilling> list = caseFillingMapper.searchallList(params, rowBounds);
			long count = caseFillingMapper.countallList(params);
			EUDGPagination pagination = new EUDGPagination(count, list);
			return pagination;
		}
		//涉黑案件类型--督办
		if(caseType.equals(ConstantValue.CASE_TYPE_DB)){
			params.put("isSupervise","1");
		}
		//涉黑案件类型--默认查所有
		List<CaseFilling> list = caseFillingMapper.searchList(params, rowBounds);
		long count = caseFillingMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
}