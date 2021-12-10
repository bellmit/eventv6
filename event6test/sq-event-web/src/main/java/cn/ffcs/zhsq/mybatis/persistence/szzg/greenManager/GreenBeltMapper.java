package cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenBeltBO;
import org.apache.ibatis.annotations.Param;


public interface GreenBeltMapper extends MyBatisBaseMapper<GreenBeltBO> {
	
	public List<GreenBeltBO> findGreenByParams(Map<String, Object> params);

	public int del(Long  seqid);
}
