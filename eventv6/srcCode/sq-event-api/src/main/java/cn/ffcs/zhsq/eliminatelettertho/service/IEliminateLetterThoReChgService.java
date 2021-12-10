package cn.ffcs.zhsq.eliminatelettertho.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterThoReChg;

import java.util.List;
import java.util.Map;

/**
 * @Description: 三书一函接收与整改情况表模块服务
 * @Author: liangbzh
 * @Date: 08-09 16:41:05
 * @Copyright: 2021 福富软件
 */
public interface IEliminateLetterThoReChgService {

    Long insert(EliminateLetterThoReChg bo) throws Exception;

    boolean update(EliminateLetterThoReChg bo) throws Exception;

    boolean updateBlankByThoId(EliminateLetterThoReChg bo) throws Exception;

    boolean updateIssuingUnitFeedbackByThoId(EliminateLetterThoReChg bo) throws Exception;

    boolean updateIndustrySectorfeedbackByThoId(EliminateLetterThoReChg bo) throws Exception;

    boolean delete(EliminateLetterThoReChg bo) throws Exception;

    boolean deleteByThoId(EliminateLetterThoReChg bo) throws Exception;

    List<Map<String,Object>> searchRegion(String regionCOde);

    EliminateLetterThoReChg searchByThoId(Long thoId);

    EUDGPagination searchList(int page, int rows, Map<String, Object> params) throws Exception;

    EliminateLetterThoReChg searchById(Long id) throws Exception;

}