package cn.ffcs.zhsq.eliminatelettertho.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoReChgService;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterThoReChg;
import cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho.EliminateLetterThoReChgMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函接收与整改情况表模块服务实现
 * @Author: liangbzh
 * @Date: 08-09 16:41:05
 * @Copyright: 2021 福富软件
 */
@Service("eliminateLetterThoReChgServiceImpl")
@Transactional
public class EliminateLetterThoReChgServiceImpl implements IEliminateLetterThoReChgService {

    @Autowired
    private EliminateLetterThoReChgMapper eliminateLetterThoReChgMapper; //注入三书一函接收与整改情况表模块dao

    /**
     * 新增数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 三书一函接收与整改情况表id
     */
    @Override
    public Long insert(EliminateLetterThoReChg bo) throws Exception {
        eliminateLetterThoReChgMapper.insert(bo);
        return bo.getChgId();
    }

    /**
     * 修改数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 是否修改成功
     */
    @Override
    public boolean update(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.update(bo);
        return result > 0;
    }

    @Override
    public boolean updateBlankByThoId(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.updateBlankByThoId(bo);
        return result > 0;
    }

    @Override
    public boolean updateIssuingUnitFeedbackByThoId(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.updateIssuingUnitFeedbackByThoId(bo);
        return result > 0;
    }

    @Override
    public boolean updateIndustrySectorfeedbackByThoId(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.updateIndustrySectorfeedbackByThoId(bo);
        return result > 0;
    }

    /**
     * 删除数据
     *
     * @param bo 三书一函接收与整改情况表业务对象
     * @return 是否删除成功
     */
    @Override
    public boolean delete(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.delete(bo);
        return result > 0;
    }

    @Override
    public boolean deleteByThoId(EliminateLetterThoReChg bo) throws Exception {
        long result = eliminateLetterThoReChgMapper.deleteByThoId(bo);
        return result > 0;
    }

    @Override
    public List<Map<String,Object>> searchRegion(String regionCOde) {
        return eliminateLetterThoReChgMapper.searchRegion(regionCOde);
    }

    @Override
    public EliminateLetterThoReChg searchByThoId(Long thoId) {
        Assert.notNull(thoId, "缺少thoId");
        return eliminateLetterThoReChgMapper.searchByThoId(thoId);
    }

    /**
     * 查询数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函接收与整改情况表分页数据对象
     */
    @Override
    public EUDGPagination searchList(int page, int rows, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<EliminateLetterThoReChg> list = eliminateLetterThoReChgMapper.searchList(params, rowBounds);
        long count = eliminateLetterThoReChgMapper.countList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函接收与整改情况表id
     * @return 三书一函接收与整改情况表业务对象
     */
    @Override
    public EliminateLetterThoReChg searchById(Long id) throws Exception {
        EliminateLetterThoReChg bo = eliminateLetterThoReChgMapper.searchById(id);
        return bo;
    }

}