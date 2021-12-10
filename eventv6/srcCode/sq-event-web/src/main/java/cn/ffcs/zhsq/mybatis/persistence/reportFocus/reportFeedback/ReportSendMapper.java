package cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportFeedback;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Description: 接收下派信息
 * 				 相关涉及表 T_EVENT_REPORT_SEND
 * @ClassName:   ReportSendMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年11月4日 下午3:44:12
 */
public interface ReportSendMapper extends MyBatisBaseMapper<Map<String, Object>> {

    /**
     * 根据seUUId查询下派信息
     * @param seUUId
     * @return
     */
    Map<String, Object> findByUUId(@Param(value = "seUUId") String seUUId);

    /**
     * 查询数据总数
     * @param params 查询参数
     * @return 下达信息数据总数
     */
    long countReportSendDataList(Map<String, Object> params);

    /**
     * 查询数据（分页）
     * @param params 查询参数
     * @param rowBounds 分页对象
     * @return 下达信息数据列表
     */
    List<Map<String, Object>> searchReportSendDataList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 根据bizSign,bizType,dataSign,dataSource来查询seuuid
     * @param params bizSign,bizType,dataSign,dataSource
     * @return seuuid
     */
    String findSeUUIdByParam(Map<String, Object> params);

    /**
     * 新增一条补充信息或催单记录
     * @param extMap
     * @return
     */
    int insertReportSendExt(Map<String, Object> extMap);

    /**
     * 查询补充信息无分页列表数据
     * @param params 查询参数
     * @return 补充信息无分页列表数据
     */
    List<Map<String, Object>> searchReportSendExtDataList(Map<String, Object> params);

    /**
     * 查询补充信息总数
     * @param params 查询参数
     * @return 补充信息数据总数
     */
    long countReportSendExtDataList(Map<String, Object> params);
}
