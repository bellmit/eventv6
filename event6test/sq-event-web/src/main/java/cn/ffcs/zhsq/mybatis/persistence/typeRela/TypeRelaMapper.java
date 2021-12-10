package cn.ffcs.zhsq.mybatis.persistence.typeRela;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/22.
 * 类别关联
 */
public interface TypeRelaMapper extends MyBatisBaseMapper<Map<String,Object>>{

	/**
	 * 根据业务ID，业务类型删除类别关联记录
	 * */
	public Boolean delTypeRela(Map<String,Object> param);

}
