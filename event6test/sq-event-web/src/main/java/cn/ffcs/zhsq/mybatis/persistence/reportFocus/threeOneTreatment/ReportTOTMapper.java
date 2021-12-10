package cn.ffcs.zhsq.mybatis.persistence.reportFocus.threeOneTreatment;

import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;

import java.util.Map;

public interface ReportTOTMapper extends ReportTwoViolationPreMapper{
    public void updateTaskTime(Map<String, Object> params);
}
