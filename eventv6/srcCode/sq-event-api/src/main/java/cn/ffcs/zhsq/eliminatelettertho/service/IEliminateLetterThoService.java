package cn.ffcs.zhsq.eliminatelettertho.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterTho;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函主表模块服务
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 福富软件
 */
public interface IEliminateLetterThoService {

    /**
     * 保存草稿
     * @param bo
     * @param userInfo
     * @return
     * @throws Exception
     */
    Long saveRaft(EliminateLetterTho bo,  UserInfo userInfo) throws Exception;

    /**
     * 保存并启动工作流
     * @param bo
     * @param userInfo
     * @return
     * @throws Exception
     */
    Long startWorkFlow(EliminateLetterTho bo,  UserInfo userInfo) throws Exception;

    /**
     * 提交工作流
     * @param bo
     * @param userInfo
     * @return
     * @throws Exception
     */
    Long commitWorkFlow(EliminateLetterTho bo,  UserInfo userInfo, String type) throws Exception;

    /**
     * 驳回
     * @param bo
     * @param userInfo
     * @return
     * @throws Exception
     */
    Long reject(EliminateLetterTho bo,  UserInfo userInfo, String type) throws Exception;

    /**
     * 更新
     * @param bo
     * @throws Exception
     */
    void update(EliminateLetterTho bo) throws Exception;

    /**
     * 逻辑删除
     * @param bo
     * @param userInfo
     * @throws Exception
     */
    void delete(EliminateLetterTho bo, UserInfo userInfo) throws Exception;

    /**
     * 逻辑删除
     * @param thos
     * @param userInfo
     * @throws Exception
     */
    void deleteBatch(List<EliminateLetterTho> thos, UserInfo userInfo) throws Exception;

    /**
     * 通过thoUuid查询
     * @param bo
     * @return
     */
    EliminateLetterTho searchByThoUuid(EliminateLetterTho bo) throws Exception;

    /**
     * 获取详情页面数据
     * @param map
     * @param bo
     * @param userInfo
     * @throws Exception
     */
    void detail(ModelMap map, EliminateLetterTho bo, UserInfo userInfo) throws Exception;

    /**
     * 查询列表
     * @param page
     * @param params
     * @return
     * @throws Exception
     */
    EUDGPagination searchList(LayuiPage page, Map<String, Object> params) throws Exception;

    /**
     * 查询待办数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    EUDGPagination searchWaitList(LayuiPage page, Map<String, Object> params) throws Exception;

    /**
     * 查询待办数据（不分页）
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    List<EliminateLetterTho> searchWaitListAll(Map<String, Object> params) throws Exception;

    /**
     * 查询辖区数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    EUDGPagination searchJurisdictionList(LayuiPage page, Map<String, Object> params) throws Exception;

    /**
     * 查询归档数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    EUDGPagination searchArchiveList(LayuiPage page, Map<String, Object> params) throws Exception;

    /**
     * 查询导出列表
     *
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    List<EliminateLetterTho> searchExportList(LayuiPage page, Map<String, Object> params) throws Exception;

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函主表id
     * @return 三书一函主表业务对象
     */
    EliminateLetterTho searchById(Long id) throws Exception;

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函主表id
     * @return 三书一函主表业务对象
     */
    Map<String, Object> handle(EliminateLetterTho bo,UserInfo userInfo) throws Exception;

    /**
     * 统计大屏各种类型的主表总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    Map<String, Object> statisticsMainCount(Map<String, Object> params) throws Exception;

    /**
     * 行业领域文书分布情况
     *
     * @param params    查询参数
     * @return
     */
    Map<String, Object> getIndusDistribution(Map<String, Object> params) throws Exception;

    /**
     * 文书类型分布情况
     *
     * @param params    查询参数
     * @return
     */
    Map<String, Object> getLetterTypeDistribution(Map<String, Object> params) throws Exception;

    /**
     * 部门排行集合
     *
     * @param params    查询参数
     * @return
     */
    Map<String, Object> getDeptRankList(Map<String, Object> params) throws Exception;

    /**
     * 根据部门查找文书分布情况
     * 部门排行弹框使用
     * @param params    查询参数
     * @return
     */
    Map<String, Object> getLetterTypeDistributionByDeptCode(Map<String, Object> params) throws Exception;

    /**
     * 根据专业编码查找文书地域分布情况(部门排行弹框使用)
     * @param params
     * @return 文书地域分布情况
     * @throws Exception
     */
    Map<String, Object> getAreaDistributionByProfessionCode(Map<String, Object> params) throws Exception;

    /**
     * 根据地域获取获取总数分布情况
     * 地图使用
     * @param params    查询参数
     * @return
     */
    Map<String, Object> getTotalDistributionByRegionCode(Map<String, Object> params) throws Exception;

    /**
     * 三书一函大屏接口
     * 文书列表
     * @param params
     * @return
     * @throws Exception
     */
    Map<String, Object> searchPage(Map<String, Object> params) throws Exception;

}