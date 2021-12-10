package cn.ffcs.zhsq.eliminatelettertho.service.impl;

import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterIndusService;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterIndus;
import cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho.EliminateLetterIndusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 三书一函行业领域表模块服务实现
 * @Author: liangbzh
 * @Date: 08-09 16:39:13
 * @Copyright: 2021 福富软件
 */
@Service("eliminateLetterIndusServiceImpl")
@Transactional
public class EliminateLetterIndusServiceImpl implements IEliminateLetterIndusService {

    @Autowired
    private EliminateLetterIndusMapper eliminateLetterIndusMapper; //注入三书一函行业领域表模块dao

    /**
     * 新增数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 三书一函行业领域表id
     */
    @Override
    public Long insert(EliminateLetterIndus bo) throws Exception {
        eliminateLetterIndusMapper.insert(bo);
        return bo.getIndusId();
    }

    /**
     * 修改数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 是否修改成功
     */
    @Override
    public boolean update(EliminateLetterIndus bo) throws Exception {
        long result = eliminateLetterIndusMapper.update(bo);
        return result > 0;
    }

    /**
     * 删除数据
     *
     * @param bo 三书一函行业领域表业务对象
     * @return 是否删除成功
     */
    @Override
    public boolean delete(EliminateLetterIndus bo) throws Exception {
        long result = eliminateLetterIndusMapper.delete(bo);
        return result > 0;
    }

    @Override
    public boolean deleteByThoId(EliminateLetterIndus bo) throws Exception {
        long result = eliminateLetterIndusMapper.deleteByThoId(bo);
        return result > 0;
    }

    @Override
    public List<EliminateLetterIndus> searchByThoId(Long thoId) {
        Assert.notNull(thoId, "缺少thoId");
        return eliminateLetterIndusMapper.searchByThoId(thoId);
    }

    @Override
    public Long invalidByThoId(EliminateLetterIndus bo) {
        return eliminateLetterIndusMapper.invalidByThoId(bo);
    }

    /**
     * 查询数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函行业领域表分页数据对象
     */
    @Override
    public List<EliminateLetterIndus> searchList(Map<String, Object> params) throws Exception {
        List<EliminateLetterIndus> list = eliminateLetterIndusMapper.searchList(params);
        return list;
    }

    @Override
    public Map<String,List<EliminateLetterIndus>> getIndusMap(List<Long> thoIdList) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("mIdList",thoIdList);
        List<EliminateLetterIndus> list = this.searchList(params);
        Map<String,List<EliminateLetterIndus>> result = new HashMap<>(thoIdList.size());
        list.forEach(x->{
            String key = x.getThoId().toString();
            if(result.get(key)==null)
                result.put(key,new ArrayList<EliminateLetterIndus>());
            result.get(key).add(x);
        });
        return result;
    }

    @Override
    public Map<String,String> getIndusNameStr(List<Long> mIdList, Function<? super EliminateLetterIndus, ? extends String> field) throws Exception {
        Map<String,List<EliminateLetterIndus>> map = this.getIndusMap(mIdList);
        Map<String,String> ret = new HashMap<>(map.size());
        map.forEach((x,y)->{
            String v = y.stream().map(field).collect(Collectors.joining(","));
            v = "["+v+"]";
            ret.put(x,v);
        });
        return ret;
    }

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函行业领域表id
     * @return 三书一函行业领域表业务对象
     */
    @Override
    public EliminateLetterIndus searchById(Long id) throws Exception {
        EliminateLetterIndus bo = eliminateLetterIndusMapper.searchById(id);
        return bo;
    }

}