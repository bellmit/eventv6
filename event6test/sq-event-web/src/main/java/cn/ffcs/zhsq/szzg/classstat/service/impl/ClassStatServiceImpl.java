package cn.ffcs.zhsq.szzg.classstat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.zhsq.mybatis.domain.szzg.classstat.ClassStat;
import cn.ffcs.zhsq.mybatis.persistence.szzg.classStat.ClassStatMapper;
import cn.ffcs.zhsq.szzg.classstat.IClassStatService;

/**
 * @Description: 户籍人口模块服务实现
 * @Author: linzhu
 * @Date: 08-10 17:20:03
 * @Copyright: 2017 福富软件
 */
@Service("classStatServiceImpl")
@Transactional
public class ClassStatServiceImpl implements IClassStatService {

	@Autowired
	private ClassStatMapper classStatMapper; //注入户籍人口模块dao

	@Override
	public List<ClassStat> findhjrkRegionListByOrgCode(String orgCode) {
		return classStatMapper.findhjrkRegionListByOrgCode(orgCode);
	}

	@Override
	public List<ClassStat> findhjrkSexAgeListByOrgCode(String orgCode) {
		return classStatMapper.findhjrkSexAgeListByOrgCode(orgCode);
	}

}