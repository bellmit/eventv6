package cn.ffcs.zhsq.mybatis.persistence.courtsynergism;

import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 法院协同办公模块dao接口
 * @Author: zhangch
 * @Date: 05-20 11:01:56
 * @Copyright: 2020 福富软件
 */
public interface CourtSynergismMapper {
	
	/**
	 * 新增数据
	 * @param bo 法院协同办公业务对象
	 * @return 法院协同办公id
	 */
	public long insert(CourtSynergism bo);
	
	/**
	 * 修改数据
	 * @param bo 法院协同办公业务对象
	 * @return 修改的记录数
	 */
	public long update(CourtSynergism bo);
	
	/**
	 * 删除数据
	 * @param bo 法院协同办公业务对象
	 * @return 删除的记录数
	 */
	public long delete(CourtSynergism bo);

	/**
	 * 根据业务id查询数据
	 * @param id 法院协同办公id
	 * @return 法院协同办公业务对象
	 */
	public CourtSynergism searchById(Long id);

    List<CourtSynergism> searchList4MyHandle(CourtSynergism bo, RowBounds rowBounds);

	long count4MyHandle(CourtSynergism bo);

	List<CourtSynergism> searchList4MyWait(CourtSynergism bo, RowBounds rowBounds);

	long countList4MyWait(CourtSynergism bo);

	List<CourtSynergism> searchList4MyCreate(CourtSynergism bo, RowBounds rowBounds);

	long countList4MyCreate(CourtSynergism bo);
}