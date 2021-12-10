package cn.ffcs.zhsq.mybatis.persistence.szzg.classStat;

import java.util.List;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.classstat.ClassStat;

public interface ClassStatMapper extends MyBatisBaseMapper<ClassStat>{
	//按区域
	public List<ClassStat> findhjrkRegionListByOrgCode(String orgCode);
	//户籍人口  性别，年龄
	public List<ClassStat> findhjrkSexAgeListByOrgCode(String orgCode);

}
