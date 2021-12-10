package cn.ffcs.zhsq.mybatis.persistence.moduleRela;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.moduleRela.ModuleRela;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: ztc
 * @Date: 2018/5/1816:10
 */
public interface ModuleRelaMapper extends MyBatisBaseMapper<ModuleRela>{
    /**
     * 批量插入关联记录
     * @param list	关联记录id
     * @return
     */
    public int insertBatch(List<ModuleRela> list);
    /**
     * 根据左方信息删除关联记录
     * @param map
     *        moduleIdLeft		左方模块编号id
     *        moduleCodeLeft	左方模块编码：001 扫黑除恶_线索管理；
     *        delUserId			删除操作用户id
     * @return
     */
    public int deleteByLeft(Map<String,Object> map);
    
    /**
     * 依据关联记录id删除关联记录
     * @param relaId	关联记录id
     * @param delUserId	删除用户id
     * @return
     */
    public int delete(@Param(value="relaId") Long relaId, @Param(value="delUserId") Long delUserId);
    
    /**
     * 无分页获取关联记录
     * @param params
     * 			moduleIdLeft	左方模块编号id
     * 			moduleCodeLeft	左方模块编码：001 扫黑除恶_线索管理；
     * @return
     */
    public List<ModuleRela> findModuleRelaByLeft(Map<String, Object> params);
}
