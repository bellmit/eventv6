package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.util.FieldMaskUtils;
import cn.ffcs.geo.geoStdAddress.service.IGeoStdAddressService;
import cn.ffcs.geo.mybatis.domain.geoStdAddress.GeoStdAddress;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.persistence.event.InvolvedPeopleMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(value="involvedPeopleServiceImpl")
public class InvolvedPeopleServiceImpl implements IInvolvedPeopleService{

	@Autowired
	private InvolvedPeopleMapper involvedPeopleMapper;
	
	@Autowired
	private IBaseDictionaryService dictionaryService;
	
	@Autowired
	private IGeoStdAddressService geoStdAddressService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Long insertInvolvedPeople(InvolvedPeople involvedPeople, Boolean isNotDuplicated) throws Exception {
		int result = 0;
		boolean condition = true;
		if(isNotDuplicated){
			condition = (involvedPeople != null && isParamLegal(involvedPeople) && isNotDuplicated(involvedPeople));
		}else{
			condition = (involvedPeople != null && isParamLegal(involvedPeople));
		}
		if(condition) {
			formatDataIn(involvedPeople);
			result = involvedPeopleMapper.insert(involvedPeople);
		}
		return result > 0 ? involvedPeople.getIpId() : -1L;
	}
	
	@Override
	public Long insertInvolvedPeople(InvolvedPeople involvedPeople) throws Exception {
		return insertInvolvedPeople(involvedPeople, true);
	}
	
	@Override
	public boolean updateInvolvedPeople(InvolvedPeople involvedPeople) throws Exception {
		int result = 0;
		
		if(involvedPeople != null && isNotDuplicated(involvedPeople)) {
			formatDataIn(involvedPeople);
			
			result = involvedPeopleMapper.update(involvedPeople);
		}
		
		return result > 0;
	}

	@Override
	public boolean deleteInvolvedPeopleById(Long ipId) {
		int result = 0;
		
		if(ipId != null && ipId > 0) {
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setIpId(ipId);
			result = involvedPeopleMapper.delete(involvedPeople);
		}
		
		return result > 0;
	}
	
	@Override
	public boolean deleteInvolvedPeopleByBiz(Long bizId, String bizType) {
		int result = 0;
		
		if(bizId != null && bizId > 0 && StringUtils.isNotBlank(bizType)) {
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setBizId(bizId);
			involvedPeople.setBizType(bizType);
			
			if(isParamLegal(involvedPeople)) {
				result = involvedPeopleMapper.delete(involvedPeople);
			}
		}
		
		return result > 0;
	}
	
	@Override
	public InvolvedPeople findInvolvedPeopleById(Long ipId) {
		InvolvedPeople involvedPeople = null;
		
		if(ipId != null && ipId > 0) {
			involvedPeople = involvedPeopleMapper.findById(ipId);
			
			if(involvedPeople != null) {
				List<InvolvedPeople> peopleList = new ArrayList<InvolvedPeople>();
				peopleList.add(involvedPeople);
				
				formatDataOut(peopleList,null);
				 FieldMaskUtils.translateConcealment("36", peopleList,null);
			}
			
		}
		
		return involvedPeople;
	}

	@Override
	public InvolvedPeople findInvolvedPeopleByIdAndOrgCode(Long ipId, String orgCode) {
		InvolvedPeople involvedPeople = null;

		if(ipId != null && ipId > 0) {
			involvedPeople = involvedPeopleMapper.findById(ipId);

			if(involvedPeople != null) {
				List<InvolvedPeople> peopleList = new ArrayList<InvolvedPeople>();
				peopleList.add(involvedPeople);

				formatDataOut(peopleList,orgCode);
				FieldMaskUtils.translateConcealment("36", peopleList,null);
			}

		}

		return involvedPeople;
	}

	@Override
	public List<InvolvedPeople> findInvolvedPeopleList(
			InvolvedPeople involvedPeople) {
		 List<InvolvedPeople> involvedPeopleList = null;
		 
		 if(involvedPeople != null) {
			 involvedPeopleList = involvedPeopleMapper.findInvolvedPeopleList(involvedPeople);
			 
			 formatDataOut(involvedPeopleList,null);
			 FieldMaskUtils.translateConcealment("36", involvedPeopleList,null);
		 }
		 
		 return involvedPeopleList;
	}

	@Override
	public List<InvolvedPeople> findInvolvedPeopleListByBiz(Long bizId, String bizType) {
		List<InvolvedPeople> involvedPeopleList = null;
		
		if(bizId != null && bizId > 0 && StringUtils.isNotBlank(bizType)) {
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setBizId(bizId);
			involvedPeople.setBizType(bizType);
			
			if(isParamLegal(involvedPeople)) {
				involvedPeopleList = involvedPeopleMapper.findInvolvedPeopleList(involvedPeople);
				formatDataOut(involvedPeopleList,null);
				 FieldMaskUtils.translateConcealment("36", involvedPeopleList,null);
			}
		}
		
		return involvedPeopleList;
	}

	@Override
	public List<InvolvedPeople> findInvolvedPeopleListByBizAndExtPar(Long bizId, String bizType, Map params) {
		List<InvolvedPeople> involvedPeopleList = null;

		if(bizId != null && bizId > 0 && StringUtils.isNotBlank(bizType)) {
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setBizId(bizId);
			involvedPeople.setBizType(bizType);

			if(isParamLegal(involvedPeople)) {
				involvedPeopleList = involvedPeopleMapper.findInvolvedPeopleList(involvedPeople);
				formatDataOutByExtPar(involvedPeopleList,null);
				 FieldMaskUtils.translateConcealment("36", involvedPeopleList,null);
			}
		}

		return involvedPeopleList;
	}

	@Override
	public Long findInvolvedPeopleCount(InvolvedPeople involvedPeople) {
		Long count = 0L;
		
		if(involvedPeople != null) {
			count = involvedPeopleMapper.findInvolvedPeopleCount(involvedPeople);
		}
		
		return count;
	}
	
	/**
	 * 判断人员是否重复输入
	 * 判重条件：bizType, bizId, cardType, idCard
	 * 四个条件均不为空时，才进行判重
	 * @param involvedPeople
	 * @return 输入重复返回false，否则返回true
	 * @throws Exception
	 */
	private boolean isNotDuplicated(InvolvedPeople involvedPeople) throws Exception {
		boolean isNotDuplicated = true;
		
		if(involvedPeople != null) {
			Long ipId = involvedPeople.getIpId();
			Long bizId = involvedPeople.getBizId();
			String bizType = involvedPeople.getBizType(),
				   cardType = involvedPeople.getCardType(),
				   idCard = involvedPeople.getIdCard();
			
			if(bizId != null && StringUtils.isNotBlank(bizType) 
					&& StringUtils.isNotBlank(cardType) && StringUtils.isNotBlank(idCard)) {
				String cardTypeBefore = "",
					   idCardBefore = "";//由于判重操作需要cardType和idCard都不为空，因此排除了将cardType或者idCard置空的操作
				InvolvedPeople peopleBefore = this.findInvolvedPeopleById(ipId);
				
				if(peopleBefore != null) {//编辑操作
					cardTypeBefore = peopleBefore.getCardType();
					idCardBefore = peopleBefore.getIdCard();
				}
				
				//证件类型和证件号码至少有一个不同时，才进行判重处理
				if(!cardType.equals(cardTypeBefore) || !idCard.equals(idCardBefore)) {
					InvolvedPeople peopleTmp = new InvolvedPeople();
					peopleTmp.setBizId(bizId);
					peopleTmp.setBizType(bizType);
					peopleTmp.setCardType(cardType);
					peopleTmp.setIdCard(idCard);
					
					//由于已经扣除了当前证件类型和证件号码，因此只要统计量大于0，就可说是重复了
					isNotDuplicated = this.findInvolvedPeopleCount(peopleTmp) == 0;
				}
				
				if(!isNotDuplicated) {
					StringBuffer msg = new StringBuffer("");
					
					msg.append("证件类型[")
					   .append(dictionaryService.changeCodeToName(ConstantValue.PRISONERS_DOC_TYPE, cardType, null))
					   .append("] 证件号码[")
					   .append(idCard)
					   .append("] 的人员信息已存在！");
					
					logger.error(msg.toString());
					throw new ZhsqEventException(msg.toString());
				}
			}
		}
		
		return isNotDuplicated;
	}
	
	/**
	 * 检测参数有效性
	 * @param involvedPeople
	 * @throws Exception
	 */
	private boolean isParamLegal(InvolvedPeople involvedPeople) {
		boolean isValid = true;
		
		if(involvedPeople != null) {
			Long bizId = involvedPeople.getBizId();
			String bizType = involvedPeople.getBizType();
			StringBuffer msgWrong = new StringBuffer("");
			
			if(bizId == null || bizId < 0) {
				msgWrong.append("缺少参数[bizId]，请检查！");
			} else if(StringUtils.isBlank(bizType)) {
				msgWrong.append("缺少参数[bizType]，请检查！");
			}
			
			if(msgWrong.length() > 0) {
				isValid = false;
				throw new IllegalArgumentException(msgWrong.toString());
			}
		}
		
		return isValid;
	}
	
	/**
	 * 格式化输入数据
	 * @param involvedPeople
	 */
	private void formatDataIn(InvolvedPeople involvedPeople) {
		if(involvedPeople != null) {
			String birthdayStr = involvedPeople.getBirthdayStr();
			
			if(StringUtils.isNotBlank(birthdayStr)) {
				Date birthday = null;
				
				try {
					birthday = DateUtils.convertStringToDate(birthdayStr, DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(birthday != null) {
					involvedPeople.setBirthday(birthday);
				}
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param list
	 */
	private void formatDataOut(List<InvolvedPeople> list,String orgCode) {
		if(list != null && list.size() > 0) {
			String RS_CARD_TYPE_DICT_CODE = "D030001";//人口证件类型字典编码
			
			String isMentalDisease = "", isMinors = "", isTeenager = "", isSkeletonStaff = "";
			Map<String, Object> decisionMap = new HashMap<String, Object>() {
				{
					put("0", "否");put("1", "是");
				}
			};
			Date birthday = null;
			
			for(InvolvedPeople people : list) {
				//性别字典转换
				people.setSexName(dictionaryService.changeCodeToName(ConstantValue.GENDER_PCODE, people.getSex(), orgCode));

				//人员类别字典转换
				//people.setPeopleTypeName(dictionaryService.changeCodeToName(ConstantValue.PEOPLETYPE_PCODE, people.getPeopleType(), orgCode));
				if("01".equals(people.getPeopleType().substring(0, 2))){
					people.setPeopleTypeName(dictionaryService.changeCodeToName("B416000", people.getPeopleType(), orgCode));
				}else{
					people.setPeopleTypeName(dictionaryService.changeCodeToName("B416001", people.getPeopleType(), orgCode));
				}
				//证件类型字典转换
					//事件涉及人员使用人口证件类型进行转换，目前只有晋江事件有展示证件类型，其他事件证件类型默认为001(身份证)
					//后续需要将二者统一，目前为临时处理方法 20191120
					if(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType().equals(people.getBizType()) || InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType().equals(people.getBizType())) {
						people.setCardTypeName(dictionaryService.changeCodeToName(RS_CARD_TYPE_DICT_CODE, people.getCardType(), orgCode));
					} else {
						people.setCardTypeName(dictionaryService.changeCodeToName(ConstantValue.CARD_TYPE_PCODE, people.getCardType(), orgCode));
					}
				
				//职业类别字典转换
					people.setProfessionTypeName(dictionaryService.changeCodeToName(ConstantValue.PROFESSION_TYPE_PCODE, people.getProfessionType(), orgCode));
				
				//国籍字典转换
					people.setNationalityName(dictionaryService.changeCodeToName(ConstantValue.NATIONALITY_PCODE, people.getNationality(), orgCode));
				
				//民族字典转换
					people.setNationName(dictionaryService.changeCodeToName(ConstantValue.NATION_RS_PCODE, people.getNation(), orgCode));
				//婚姻状况字典转换
					people.setMarriageName(dictionaryService.changeCodeToName(ConstantValue.MARRIAGE_PCODE, people.getMarriage(), orgCode));
				
				//政治面貌字典转换
					people.setPoliticsName(dictionaryService.changeCodeToName(ConstantValue.POLITICS_PCODE, people.getPolitics(), orgCode));
				//学历字典转换
					people.setEduName(dictionaryService.changeCodeToName(ConstantValue.EDUCATION_PCODE, people.getEdu(), orgCode));
				//宗教信仰字典转换
					people.setReligionName(dictionaryService.changeCodeToName(ConstantValue.RELIGION_PCODE, people.getReligion(), orgCode));
				
				//转换是与否的属性
				isMentalDisease = people.getIsMentalDisease();
				isMinors = people.getIsMinors();
				isTeenager = people.getIsTeenager();
				isSkeletonStaff = people.getIsSkeletonStaff();
				
				if(StringUtils.isNotBlank(isMentalDisease) && CommonFunctions.isNotBlank(decisionMap, isMentalDisease)) {
					people.setMentalDisease(decisionMap.get(isMentalDisease).toString());
				}
				
				if(StringUtils.isNotBlank(isMinors) && CommonFunctions.isNotBlank(decisionMap, isMinors)) {
					people.setMinors(decisionMap.get(isMinors).toString());
				}
				
				if(StringUtils.isNotBlank(isTeenager) && CommonFunctions.isNotBlank(decisionMap, isTeenager)) {
					people.setTeenager(decisionMap.get(isTeenager).toString());
				}
				
				if(StringUtils.isNotBlank(isSkeletonStaff) && CommonFunctions.isNotBlank(decisionMap, isSkeletonStaff)) {
					people.setSkeletonStaff(decisionMap.get(isSkeletonStaff).toString());
				}
				
				//转换时间格式的属性
				birthday = people.getBirthday();
				if(birthday != null) {
					String birthdayStr = DateUtils.formatDate(birthday, DateUtils.PATTERN_DATE);
					if(StringUtils.isNotBlank(birthdayStr)) {
						people.setBirthdayStr(birthdayStr);
					}
				}
				
				//转换籍贯地址路径
				people.setBirthPlaceName(changeGeoCode2Addr(people.getBirthPlace()));
				//转换户籍地地址路径
				people.setReOrgCodeName(changeGeoCode2Addr(people.getReOrgCode()));
				//转换现住地地址路径
				people.setLiOrgCodeName(changeGeoCode2Addr(people.getLiOrgCode()));
				
				people.setHashBizId(HashIdManager.encrypt(people.getBizId()));
				people.setHashId(HashIdManager.encrypt(people.getIpId()));
				//people.setIpId(0l);
				//people.setBizId(0L);
			}
		}
	}

	/**
	 * 格式化输出数据
	 * 证件类型字典和人口证件类型字典保持一致
	 * @param list
	 */
	private void formatDataOutByExtPar(List<InvolvedPeople> list,String orgCode) {
		if(list != null && list.size() > 0) {
			String RS_CARD_TYPE_DICT_CODE = "D030001";//人口证件类型字典编码
			String isMentalDisease = "", isMinors = "", isTeenager = "", isSkeletonStaff = "";
			Map<String, Object> decisionMap = new HashMap<String, Object>() {
				{
					put("0", "否");put("1", "是");
				}
			};
			Date birthday = null;

			for(InvolvedPeople people : list) {
				//性别字典转换
				people.setSexName(dictionaryService.changeCodeToName(ConstantValue.GENDER_PCODE, people.getSex(), orgCode));

				//人员类别字典转换
				people.setPeopleTypeName(dictionaryService.changeCodeToName(ConstantValue.PEOPLETYPE_PCODE, people.getPeopleType(), orgCode));
				//证件类型字典转换
					//事件涉及人员使用人口证件类型进行转换，目前只有晋江事件有展示证件类型，其他事件证件类型默认为001(身份证)
					//后续需要将二者统一，目前为临时处理方法 20191120
					//晋江事件统一库涉及人口相关的目前暂时也使用这种办法
				if(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType().equals(people.getBizType())) {
					people.setCardTypeName(dictionaryService.changeCodeToName(RS_CARD_TYPE_DICT_CODE, people.getCardType(), orgCode));
				} else {
					people.setCardTypeName(dictionaryService.changeCodeToName(ConstantValue.CARD_TYPE_PCODE, people.getCardType(), orgCode));
				}
				//职业类别字典转换
				people.setProfessionTypeName(dictionaryService.changeCodeToName(ConstantValue.PROFESSION_TYPE_PCODE, people.getProfessionType(), orgCode));
			
			//国籍字典转换
				people.setNationalityName(dictionaryService.changeCodeToName(ConstantValue.NATIONALITY_PCODE, people.getNationality(), orgCode));
			
			//民族字典转换
				people.setNationName(dictionaryService.changeCodeToName(ConstantValue.NATION_RS_PCODE, people.getNation(), orgCode));
			//婚姻状况字典转换
				people.setMarriageName(dictionaryService.changeCodeToName(ConstantValue.MARRIAGE_PCODE, people.getMarriage(), orgCode));
			
			//政治面貌字典转换
				people.setPoliticsName(dictionaryService.changeCodeToName(ConstantValue.POLITICS_PCODE, people.getPolitics(), orgCode));
			//学历字典转换
				people.setEduName(dictionaryService.changeCodeToName(ConstantValue.EDUCATION_PCODE, people.getEdu(), orgCode));
			//宗教信仰字典转换
				people.setReligionName(dictionaryService.changeCodeToName(ConstantValue.RELIGION_PCODE, people.getReligion(), orgCode));
				//转换是与否的属性
				isMentalDisease = people.getIsMentalDisease();
				isMinors = people.getIsMinors();
				isTeenager = people.getIsTeenager();
				isSkeletonStaff = people.getIsSkeletonStaff();

				if(StringUtils.isNotBlank(isMentalDisease) && CommonFunctions.isNotBlank(decisionMap, isMentalDisease)) {
					people.setMentalDisease(decisionMap.get(isMentalDisease).toString());
				}

				if(StringUtils.isNotBlank(isMinors) && CommonFunctions.isNotBlank(decisionMap, isMinors)) {
					people.setMinors(decisionMap.get(isMinors).toString());
				}

				if(StringUtils.isNotBlank(isTeenager) && CommonFunctions.isNotBlank(decisionMap, isTeenager)) {
					people.setTeenager(decisionMap.get(isTeenager).toString());
				}

				if(StringUtils.isNotBlank(isSkeletonStaff) && CommonFunctions.isNotBlank(decisionMap, isSkeletonStaff)) {
					people.setSkeletonStaff(decisionMap.get(isSkeletonStaff).toString());
				}

				//转换时间格式的属性
				birthday = people.getBirthday();
				if(birthday != null) {
					String birthdayStr = DateUtils.formatDate(birthday, DateUtils.PATTERN_DATE);
					if(StringUtils.isNotBlank(birthdayStr)) {
						people.setBirthdayStr(birthdayStr);
					}
				}

				//转换籍贯地址路径
				people.setBirthPlaceName(changeGeoCode2Addr(people.getBirthPlace()));
				//转换户籍地地址路径
				people.setReOrgCodeName(changeGeoCode2Addr(people.getReOrgCode()));
				//转换现住地地址路径
				people.setLiOrgCodeName(changeGeoCode2Addr(people.getLiOrgCode()));
			}
		}
	}
	
	/**
	 * 将地址库编码转换为地址路径
	 * @param geoCode
	 * @return 转换成功返回地址路径，转换失败返回空字符串
	 */
	private String changeGeoCode2Addr(String geoCode) {
		String geoAddrPath = "";
		
		if(StringUtils.isNotBlank(geoCode)) {
			Map<String, Object> geoMap = new HashMap<String, Object>();
			List<String> divisionCodes = new ArrayList<String>();
			divisionCodes.add(geoCode);
			
			geoMap.put("bizType", "1");
			geoMap.put("divisionCodes", divisionCodes);
			
			List<GeoStdAddress> geoList = geoStdAddressService.findGeoStdAddressList(geoMap);
			
			if(geoList != null && geoList.size() == 1) {
				geoAddrPath = geoList.get(0).getAddressPathName();
			} else {
				logger.error("地址库转换["+geoCode+"] 失败！");
			}
		}
		
		return geoAddrPath;
	}

	@Override
	public long batchDelete(Map<String, Object> params) {
		return involvedPeopleMapper.batchDelete(params);
	}

	@Override
	public long batchInsert(List<InvolvedPeople> vList) {
		return involvedPeopleMapper.batchInsert(vList);
	}

	@Override
	public long batchUpdate(List<InvolvedPeople> vList) {
		return involvedPeopleMapper.batchUpdate(vList);
	}

	@Override
	public long batchSaveOrUpdate(Long bizId,List<InvolvedPeople> involvedPeopleList) {

		long result = -1;
		if(null != involvedPeopleList && involvedPeopleList.size() > 0){

			for(InvolvedPeople people:involvedPeopleList){
				if(null == people.getBizId()){
					if(null != bizId && bizId > 0){
						people.setBizId(bizId);
					}else {
						throw new IllegalArgumentException("参数【bizId】有误，请检查！");
					}
				}
				//出生日期处理
				formatDataIn(people);
			}
			result = involvedPeopleMapper.batchSaveOrUpdate(involvedPeopleList);
		}

		return result;
	}
	
}
