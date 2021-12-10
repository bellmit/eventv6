package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 事件额外信息处理接口
 * @ClassName:   EventDisposal4ExtraMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年2月27日 下午4:48:22
 */
public interface EventDisposal4ExtraMapper {
	/**
	 * 获取需要星级评价的事件数量
	 * @param param
	 * @return
	 */
	public int findCount4JurisdictionEventEva(Map<String, Object> param);
	
	/**
	 * 获取需要星级评价的事件
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPageList4JurisdictionEventEva(Map<String, Object> param, RowBounds bounds);
	
}
