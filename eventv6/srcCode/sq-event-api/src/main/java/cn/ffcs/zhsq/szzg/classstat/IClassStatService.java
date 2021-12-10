package cn.ffcs.zhsq.szzg.classstat;

import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.szzg.classstat.ClassStat;

public interface IClassStatService {
	//按区域
	public List<ClassStat> findhjrkRegionListByOrgCode(String orgCode);
	
	//户籍人口  性别，年龄
	public List<ClassStat> findhjrkSexAgeListByOrgCode(String orgCode);

}
