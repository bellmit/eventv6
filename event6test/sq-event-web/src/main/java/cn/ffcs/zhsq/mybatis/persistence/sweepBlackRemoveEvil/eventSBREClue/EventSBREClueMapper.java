package cn.ffcs.zhsq.mybatis.persistence.sweepBlackRemoveEvil.eventSBREClue;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/15.
 */
public interface EventSBREClueMapper extends MyBatisBaseMapper<EventSBREClue>{

    /**
     * 根据线索id删除线索
     * @param clueId	线索id
     * @param delUserId	删除操作用户id
     * @return
     */
    public int deleteByClueId(@Param(value="clueId")Long clueId, @Param(value="delUserId")Long delUserId);

    /**
     * 待办线索统计
     * @param params
     * 			curUserId	办理人id
     * 			curOrgId	办理人所属组织id
     * */
    public int findCount4Todo(Map<String,Object> params);
    /**
     * 待办列表记录
     * @param param 参数
     * 			curUserId	办理人id
     * 			curOrgId	办理人所属组织id
     * @param bounds 分页信息
     * @return
     */
    public List<EventSBREClue> findPageList4Todo(Map<String, Object> param, RowBounds bounds);
    /**
     * 经办记录统计
     * @param param
     * 			handledUserId	经办人员id
     * 			handledOrgId	经办人员所属组织id
     * @return
     */
    public int findCount4Handled(Map<String, Object> param);

    /**
     * 经办列表记录
     * @param param 参数
     * 			handledUserId	经办人员id
     * 			handledOrgId	经办人员所属组织id
     * @param bounds 分页信息
     * @return
     */
    public List<EventSBREClue> findPageList4Handled(Map<String, Object> param, RowBounds bounds);
    /**
     * 我发起的记录统计
     * @param param
     * 			initiatorId 	发起人员id 
     * 			initiatorOrgId 	发起人员所属组织id
     * @return
     */
    public int findCount4Initiator(Map<String, Object> param);

    /**
     * 我发起的列表记录
     * @param param 参数
     * 			initiatorId 	发起人员id 
     * 			initiatorOrgId 	发起人员所属组织id
     * @param bounds 分页信息
     * @return
     */
    public List<EventSBREClue> findPageList4Initiator(Map<String, Object> param, RowBounds bounds);
    
    /**
     * 辖区所有记录统计
     * @param param
     * 			infoOrgCode	地域编码
     * @return
     */
    public int findCount4Jurisdiction(Map<String, Object> param);

    /**
     * 辖区所有列表记录
     * @param param 参数
     * 			infoOrgCode	地域编码
     * @param bounds 分页信息
     * @return
     */
    public List<EventSBREClue> findPageList4Jurisdiction(Map<String, Object> param, RowBounds bounds);
	
    /**
	 * 根据条件搜索不分页
	 * @param params
	 * @return
	 */
	public List<EventSBREClue> findEventSBREClueList(Map<String, Object> params);
    
}
