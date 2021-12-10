package cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenStdBO;

import java.util.List;
import java.util.Map;


public interface GreenStdMapper extends MyBatisBaseMapper<GreenStdBO> {
	
	public int del(Long seqid);
	
	public List<GreenStdBO> findGreenStdByParams(Map<String, Object> params);

}
