package cn.ffcs.zhsq.mybatis.persistence.dispute;


import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationRes;

public interface DisputeMediationResMapper {
    int deleteByPrimaryKey(Long mediationResId);
    
    DisputeMediationRes selectByMediationId(Long mediationId);

    int insert(DisputeMediationRes record);

    int insertSelective(DisputeMediationRes record);

    DisputeMediationRes selectByPrimaryKey(Long mediationResId);

    int updateByPrimaryKeySelective(DisputeMediationRes record);

    int updateByPrimaryKey(DisputeMediationRes record);
}