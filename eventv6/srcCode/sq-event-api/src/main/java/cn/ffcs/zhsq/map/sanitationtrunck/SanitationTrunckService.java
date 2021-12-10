package cn.ffcs.zhsq.map.sanitationtrunck;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck.SanitationTrunck;

/**
 * @Description: 环卫车基础信息表模块服务
 * @Author: dingyw
 * @Date: 01-02 14:08:17
 * @Copyright: 2018 福富软件
 */
public interface SanitationTrunckService {

	/**
	 * 新增数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 环卫车基础信息表id
	 */
	public void insert(SanitationTrunck bo);
	
	/**
	 * 批量新增数据
	 * @param list
	 */
	public void batchSave(List<SanitationTrunck> list);

	/**
	 * 修改数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(SanitationTrunck bo);

	/**
	 * 删除数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(SanitationTrunck bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 环卫车基础信息表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 环卫车基础信息表id
	 * @return 环卫车基础信息表业务对象
	 */
	public SanitationTrunck searchById(String id);
	
	public void sysSanitatinTrunckInfo(List<SanitationTrunck> list);

}