package cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho;

import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterThoReChg;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函接收与整改情况表模块dao接口
 * @Author: liangbzh
 * @Date: 08-09 16:41:05
 * @Copyright: 2021 福富软件
 */
public interface EliminateLetterThoReChgMapper {

    /**
     * 新增数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 三书一函接收与整改情况表id
     */
    long insert(EliminateLetterThoReChg bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 修改的记录数
     */
    long update(EliminateLetterThoReChg bo);

    /**
     * 修改数据
     * @param bo
     * @return
     * @throws Exception
     */
    long updateIssuingUnitFeedbackByThoId(EliminateLetterThoReChg bo) throws Exception;

    long updateIndustrySectorfeedbackByThoId(EliminateLetterThoReChg bo) throws Exception;

    /**
     * 修改数据
     * @param bo 三书一函接收与整改情况表业务对象
     * @return
     */
    long updateBlankByThoId(EliminateLetterThoReChg bo);

    /**
     * 修改数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 修改的记录数
     */
    long updateByThoId(EliminateLetterThoReChg bo);

    /**
     * 删除数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 删除的记录数
     */
    long delete(EliminateLetterThoReChg bo);

    /**
     * 删除数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 删除的记录数
     */
    long deleteByThoId(EliminateLetterThoReChg bo);

    /**
     * 根据regionCOde查询
     * @param regionCOde
     * @return
     */
    List<Map<String,Object>> searchRegion(String regionCOde);

    /**
     * 根据thoId查询
     * @param thoId
     * @return
     */
    EliminateLetterThoReChg searchByThoId(Long thoId);

    /**
     * 查询数据（分页）
     *
     * @param params    查询参数
     * @param rowBounds 分页对象
     * @return 三书一函接收与整改情况表数据列表
     */
    List<EliminateLetterThoReChg> searchList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询数据总数
     *
     * @param params 查询参数
     * @return 三书一函接收与整改情况表数据总数
     */
    long countList(Map<String, Object> params);

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函接收与整改情况表id
     * @return 三书一函接收与整改情况表业务对象
     */
    EliminateLetterThoReChg searchById(Long id);

}