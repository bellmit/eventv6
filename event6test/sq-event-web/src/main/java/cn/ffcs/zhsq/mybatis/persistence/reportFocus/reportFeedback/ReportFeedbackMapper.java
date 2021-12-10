package cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportFeedback;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Description: 上报信息反馈
 * 				  相关涉及表 T_EVENT_REPORT_FEEDBACK
 * @ClassName:   ReportFeedbackMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年11月4日 下午3:41:12
 */
public interface ReportFeedbackMapper extends MyBatisBaseMapper<Map<String, Object>> {

    /**
     * 更新下达信息反馈记录
     * @param param
     * @return
     */
    public int updateMap(Map<String, Object> param);

    /**
     *  根据fbUUID逻辑删除信息反馈记录
     * @param param
     * @return
     */
    long delete(Map<String, Object> param);

    /**
     * 依据seuuid获取反馈信息列表
     * @param seuuid
     * @return 反馈信息无分页列表数据
     */
    List<Map<String, Object>> findBySeUUId(String seuuid);

    /**
     * 依据fbUUid获取反馈信息
     * @param fbUUid
     * @return
     */
    Map<String, Object> findByFbUUId(String fbUUid);

    /**
     * 查询反馈数据总数
     * @param params 查询参数
     * @return 反馈信息数据总数
     */
    long countReportFeedbackDataList(Map<String, Object> params);


    /**
     * 查询反馈数据（分页）
     * @param params 查询参数
     * @param rowBounds 分页对象
     * @return 反馈信息数据列表
     */
    List<Map<String, Object>> searchReportFeedbackDataList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询反馈数据不分页
     * @param params 查询参数
     * @return 反馈信息数据列表
     */
    List<Map<String, Object>> searchReportFeedbackDataList(Map<String, Object> params);
}
