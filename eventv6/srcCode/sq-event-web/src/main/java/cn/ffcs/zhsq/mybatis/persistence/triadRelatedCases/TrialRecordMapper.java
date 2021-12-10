package cn.ffcs.zhsq.mybatis.persistence.triadRelatedCases;

import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.CaseFilling;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.TrialRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 庭审记录表模块dao接口
 * @Author: chenshikai
 * @Date: 09-08 17:46:14
 * @Copyright: 2021 福富软件
 */
public interface TrialRecordMapper {


	/**
	 * 批量新增
	 * @param bo
	 * @return
	 */
	public long batchInsert(CaseFilling bo);


	/**
	 * 根据主表主键删除数据
	 * @param id
	 * @return
	 */
	public long deleteByUndCaseUuid(@Param("undCaseUuid") String id, @Param("updator") Long userId);


	public List<TrialRecord> searchListById(String undCaseUuid);



}