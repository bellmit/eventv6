package cn.ffcs.zhsq.moduleRela.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.moduleRela.ModuleRela;

/**
 * @Description:
 * @Author: ztc
 * @Date: 2018/5/1817:29
 */
public interface IModuleRelaService {
    /**
     * 批量插入关联记录
     * @param map
     * 必填参数
     *     moduleCodeLeft 		左方模块编码，001 扫黑除恶_线索管理；
     *     moduleIdLeft   		左方模块编号
     *     moduleCodeRight 		右方模块编码，001 扫黑除恶_黑恶团伙管理
     *     moduleIdRightList	右方模块编号，类型为List<Long> 
     * 非必填参数
     *     userInfo		类型为cn.ffcs.uam.bo.UserInfo
     *     creatorId	用户id
     *
     * @return
     * @throws Exception 
     */
    public boolean saveModuleRelaBatch(Map<String,Object> map) throws Exception;
    /**
     * 根据左方信息删除关联记录
     * @param moduleCodeLeft	左方模块编码：001 扫黑除恶_线索管理；
     * @param moduleIdLeft		左方模块编号id
     * @param params
     * 			delUserId		删除操作用户id
     * @return
     */
    public boolean deleteByLeft(String moduleCodeLeft, Long moduleIdLeft, Map<String,Object> params);
    
    /**
     * 根据关联记录id删除关联记录
     * @param relaId
     * @param delUserId
     * @return
     */
    public boolean deleteByRelaId(Long relaId, Long delUserId);
    
    /**
     * 依据左方信息获取关联信息
     * @param moduleCodeLeft	左方模块编码：001 扫黑除恶_线索管理；
     * @param moduleIdLeft		左方模块编号id
     * @param params
     * 			moduleCodeRight	右方模块编码，001 扫黑除恶_黑恶团伙管理
     * @return
     */
    public List<ModuleRela> findModuleRelaByLeft(String moduleCodeLeft, Long moduleIdLeft, Map<String, Object> params);
}
