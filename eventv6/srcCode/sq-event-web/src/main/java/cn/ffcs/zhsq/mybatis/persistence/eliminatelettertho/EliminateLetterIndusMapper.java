package cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho;

import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterIndus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函行业领域表模块dao接口
 * @Author: liangbzh
 * @Date: 08-09 16:39:13
 * @Copyright: 2021 福富软件
 */
public interface EliminateLetterIndusMapper {

    /**
     * 新增数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 三书一函行业领域表id
     */
    long insert(EliminateLetterIndus bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 修改的记录数
     */
    long update(EliminateLetterIndus bo);

    /**
     * 将行业领域列表的记录设置为失效
     * @param bo 三书一函行业领域表业务对象
     * @return
     */
    long invalidByThoId(EliminateLetterIndus bo);

    /**
     * 删除数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 删除的记录数
     */
    long delete(EliminateLetterIndus bo);

    /**
     * 删除数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 删除的记录数
     */
    long deleteByThoId(EliminateLetterIndus bo);

    /**
     * 根据thoId查询
     * @param thoId
     * @return
     */
    List<EliminateLetterIndus> searchByThoId(Long thoId);

    /**
     * 查询数据（分页）
     *
     * @param params    查询参数
     * @return 三书一函行业领域表数据列表
     */
    List<EliminateLetterIndus> searchList(Map<String, Object> params);

    /**
     * 查询数据总数
     *
     * @param params 查询参数
     * @return 三书一函行业领域表数据总数
     */
    long countList(Map<String, Object> params);

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函行业领域表id
     * @return 三书一函行业领域表业务对象
     */
    EliminateLetterIndus searchById(Long id);

}