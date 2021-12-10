package cn.ffcs.zhsq.eliminatelettertho.service;

import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterIndus;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Description: 三书一函行业领域表模块服务
 * @Author: liangbzh
 * @Date: 08-09 16:39:13
 * @Copyright: 2021 福富软件
 */
public interface IEliminateLetterIndusService {

    Long insert(EliminateLetterIndus bo) throws Exception;

    boolean update(EliminateLetterIndus bo) throws Exception;

    boolean delete(EliminateLetterIndus bo) throws Exception;

    boolean deleteByThoId(EliminateLetterIndus bo) throws Exception;

    List<EliminateLetterIndus> searchByThoId(Long thoId);

    Long invalidByThoId(EliminateLetterIndus bo);

    /**
     * 查询数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函行业领域表分页数据对象
     */
    List<EliminateLetterIndus> searchList(Map<String, Object> params) throws Exception;

    /**
     *
     * @param thoIdList
     * @return
     * @throws Exception
     */
    Map<String,List<EliminateLetterIndus>> getIndusMap(List<Long> thoIdList) throws Exception;


    Map<String,String> getIndusNameStr(List<Long> mIdList, Function<? super EliminateLetterIndus, ? extends String> field) throws Exception;

    EliminateLetterIndus searchById(Long id) throws Exception;

}