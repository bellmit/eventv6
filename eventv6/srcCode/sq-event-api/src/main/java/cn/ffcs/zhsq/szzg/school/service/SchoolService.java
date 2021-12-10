package cn.ffcs.zhsq.szzg.school.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolBo;
import cn.ffcs.system.publicUtil.EUDGPagination;


public interface SchoolService {

    /**
     * 查询学校标注
     * @param params
     * @return
     */
    public List<SchoolBo> findSchoolMark(Map<String, Object> params);

    /**
     * 分页查询
     * @param params
     * @param page
     * @param rows
     * @return
     */
    public EUDGPagination findPageListByCriteria(Map<String, Object> params, Integer page, Integer rows);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public SchoolBo findSchoolById(Long id);

    /**
     * 更新
     * @param schoolBo
     * @return
     */
    public Boolean save(SchoolBo schoolBo);

    /**
     * 批量删除
     * @param seqid
     * @return
     */
    public Boolean delete(Long seqid);

    /**
     * 查询树形
     * @param param
     * @return
     */
    public List<SchoolBo> findTreeTable(Map<String,Object> param);

    /**
     * 新增
     * @param schoolBo
     * @return
     */
    public Boolean insert(SchoolBo schoolBo);

    /**
     * 获取标注
     * @param params
     * @return
     */
    public List<SchoolBo> findSchoolPoint(Map<String, Object> params);

    /**
     * 获取柱状图数据
     * @return
     */
    public Map<String, Object> getSchoolCharts(Map<String, Object> params);



}
