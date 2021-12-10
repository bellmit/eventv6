package cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho;

import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterTho;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函主表模块dao接口
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 福富软件
 */
public interface EliminateLetterThoMapper {

    /**
     * 新增数据
     *
     * @param bo 三书一函主表业务对象
     * @return 三书一函主表id
     */
    long insert(EliminateLetterTho bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函主表业务对象
     * @return 修改的记录数
     */
    long update(EliminateLetterTho bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函主表业务对象
     * @return 修改的记录数
     */
    long updateBlankByThoUuid(EliminateLetterTho bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函主表业务对象
     * @return 修改的记录数
     */
    long updateByThoUuid(EliminateLetterTho bo);

    /**
     * 删除数据
     *
     * @param bo 三书一函主表业务对象
     * @return 删除的记录数
     */
    long delete(EliminateLetterTho bo);

    /**
     * 查询数据（分页）
     *
     * @param params    查询参数
     * @param rowBounds 分页对象
     * @return 三书一函主表数据列表
     */
    List<EliminateLetterTho> searchList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询数据总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    long countList(Map<String, Object> params);

    /**
     * 查询待办数据（分页）
     *
     * @param params    查询参数
     * @param rowBounds 分页对象
     * @return 三书一函主表数据列表
     */
    List<EliminateLetterTho> searchWaitList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询待办数据（不分页）
     * @param params    查询参数
     * @return 三书一函主表数据列表
     */
    List<EliminateLetterTho> searchWaitList(Map<String, Object> params);

    /**
     * 查询待办数据总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    long countWaitList(Map<String, Object> params);

    /**
     * 查询辖区数据（分页）
     *
     * @param params    查询参数
     * @param rowBounds 分页对象
     * @return 三书一函主表数据列表
     */
    List<EliminateLetterTho> searchJurisdictionList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询辖区数据总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    long countJurisdictionList(Map<String, Object> params);

    /**
     * 查询归档数据（分页）
     *
     * @param params    查询参数
     * @param rowBounds 分页对象
     * @return 三书一函主表数据列表
     */
    List<EliminateLetterTho> searchArchiveList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询归档数据总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    long countArchiveList(Map<String, Object> params);

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函主表id
     * @return 三书一函主表业务对象
     */
    EliminateLetterTho searchById(Long id);

    /**
     * 根据业务id查询数据
     *
     * @param thoUuid 三书一函主表thoUuid
     * @return 三书一函主表业务对象
     */
    EliminateLetterTho searchByThoUuid(String thoUuid);



    /**
     * 统计大屏各种类型的主表总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    long statisticsMainCount(Map<String, Object> params);

    /**
     * 行业领域文书分布情况
     *
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getIndusDistribution(Map<String, Object> params);

    /**
     * 文书类型分布情况
     *
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getLetterTypeDistribution(Map<String, Object> params);

    /**
     * 部门排行情况
     *
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getDeptRankList(Map<String, Object> params);

    /**
     * 根据部门查找文书分布情况
     * 部门排行弹框使用
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getLetterTypeDistributionByDeptCode(Map<String, Object> params);

    /**
     * 根据专业编码查找文书地域分布情况
     * 部门排行弹框使用
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getAreaDistributionByProfessionCode(Map<String, Object> params);

    /**
     * 根据地域获取获取总数分布情况
     * 地图使用
     * @param params    查询参数
     * @return
     */
    List<Map<String, Object>> getTotalDistributionByRegionCode(Map<String, Object> params);

    /**
     * 统计大屏文书列表分页
     * @param params
     * @return
     */
    List<EliminateLetterTho> searchPage(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 统计大屏文书列表总数
     * @param params
     * @return
     */
    Long searchPageCount(Map<String, Object> params);
}