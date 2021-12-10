package cn.ffcs.zhsq.mybatis.persistence.szzg.event;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BriefingMapper {

    /**
     * 通过简报ID获取简报
     * @param params
     * @return
     */
    public List<Map<String, Object>> findById(Map<String, Object> params);

    /**
     * 获取简报列表
     * @param params
     * @return
     */
    public List<Map<String, Object>> getBriefingList(Map<String, Object> params, RowBounds bounds);

    /**
     * 修改简报
     * @param params
     * @return
     */
    public Integer update(Map<String, Object> params);

    /**
     * 获取简报总数
     * @param params
     * @return
     */
    public Integer getBriefingCount(Map<String, Object> params);

    /**
     * 获取简报编辑信息列表
     * @param params
     * @return
     */
    public List<Map<String, Object>> getEditMessageList(Map<String, Object> params);

    /**
     * 删除简报编辑信息
     * @param bizId
     * @return
     */
    public Integer deleteEditMessage(Integer bizId);

    /**
     * 添加简报编辑信息
     * @param params
     * @return
     */
    public Integer addEditMessage(Map<String, Object> params);

    /**
     * 获取民生民安事项数
     * @param params
     * @return
     */
    //public Map<String, Object> queryEventCount(Map<String, Object> params);

    /**
     * 根据事件大类分组获取事项数
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventCountByType(Map<String,Object> params);
    /**
     * 查询事项小类事件
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventCountBySmallType(Map<String,Object> params);

    /**
     * 查询网格员和综治中心的事件
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByGrid(Map<String,Object> params);

    /**
     * 查询平安志愿者和综治责任中心的事件
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByVolunteer(Map<String,Object> params);

    /**
     * 根据设区市取得事件信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByCity(Map<String,Object> params);

    /**
     * 根据综治中心(网格员、志愿者、责任中心)获取事件
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByCMDGrid(Map<String,Object> params);

    /**
     * 根据综治中心(网格员、志愿者、责任中心)和类别获取事件
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByCMDGridType(Map<String,Object> params);

    /**
     * 区县人均
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByCounty(Map<String,Object> params);

    /**
     * 区县逾期
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryEventByCountyOverdue(Map<String,Object> params);
    
    /**
     * 报表事件列表页总数
     * @param params
     * @return
     */
    public Long getEventListCount(Map<String,Object> params);
    
    /**
     * 报表事件列表页数据
     * @param params
     * @return
     */
    public List<Map<String,Object>> getEventListData(Map<String,Object> params,RowBounds rowBounds);

    /**
     * 报表事件列表页总数(表一表三办结)
     * @param params
     * @return
     */
    public Long getEventListCountByDisposal(Map<String,Object> params);

    /**
     * 报表事件列表页数据(表一表三办结)
     * @param params
     * @param rowBounds
     * @return
     */
    public List<Map<String,Object>> getEventListDataByDisposal(Map<String,Object> params,RowBounds rowBounds);
    /**
     * 报表事件列表页总数(表二待办)
     * @param params
     * @return
     */
    public Long getEventListCountByTimeDisposal(Map<String,Object> params);

    /**
     * 报表事件列表页数据(表二待办)
     * @param params
     * @param rowBounds
     * @return
     */
    public List<Map<String,Object>> getEventListDataByTimeDisposal(Map<String,Object> params,RowBounds rowBounds);
    /**
     * 	有效事项占比排名
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryCityValidRate(Map<String,Object> params);
}
