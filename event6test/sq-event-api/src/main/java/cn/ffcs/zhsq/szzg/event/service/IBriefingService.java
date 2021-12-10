package cn.ffcs.zhsq.szzg.event.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;

import java.util.List;
import java.util.Map;



public interface IBriefingService {

    //通过简报ID查找简报
    public List<Map<String, Object>> findById(Map<String, Object> params);
    public List<Map<String, Object>> getBriefingList(Map<String, Object> params)throws Exception;

    //获取简报列表
    public EUDGPagination getList(Map<String, Object> params)throws Exception;

    //更新简报信息
    public Integer update(Map<String, Object> params);

    //获取简报列表总数
    public Integer getBriefingCount(Map<String, Object> params);

    //获取简报数据
    public Map<String, Object> queryEventCount(Map<String, Object> params);

    //获取简报编辑信息列表
    public List<Map<String, Object>> getEditMessageList(Map<String, Object> params);

    //通过简报ID删除简报编辑信息
    public Integer deleteEditMessage(Integer bizId);

    //新增简报编辑信息
    public Integer addEditMessage(Map<String, Object> params);
    
    //获取报表事件列表数据
	public EUDGPagination getEventListData(Map<String, Object> params, UserInfo userInfo);
    //表一表三列表办结数据
    public EUDGPagination getEventListDataByDisposal(Map<String, Object> params, UserInfo userInfo);
    //表二列表待办数据
    public EUDGPagination getEventListDataByTimeDisposal(Map<String, Object> params, UserInfo userInfo);
}
