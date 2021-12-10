package cn.ffcs.zhsq.szzg.resource.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: zg_resource_info模块服务
 * @Author: huangwenbin
 * @Date: 09-16 10:02:32
 * @Copyright: 2017 福富软件
 */
public interface ZgResourceInfoService {

	
	/**
	 * 新增数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param createUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> insert(Map<String, Object> params);
	
	/**
	 * 新增数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param createUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> insert(ZgResourceInfo bo);
	
	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> update(Map<String, Object> params);

	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> update(ZgResourceInfo bo);
	
	/**
	 * 删除数据(单条)
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param updateUserId(必须)//用户id
	 *
	 * @param 删除数据(批量)
	 * @param resTypeCode(必须) //资源类型编码
	 * @param key:resTableIds,value:"1,2,3" (必须) //资源相应表主键id 
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> delete(Map<String, Object> params);

	/**
	 * 删除数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	public Map<String, Object> delete(ZgResourceInfo bo);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return zg_resource_info分页数据对象
	 */
	public EUDGPagination findList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id zg_resource_infoid
	 * @return zg_resource_info业务对象
	 */
	public ZgResourceInfo findById(Long id);
	
	/**
	 * 
	 */
	public List<ZgResourceInfo> findByParam(Map<String, Object> params);

	public Map<String,Object> findResource(Map<String, Object> params);
	
	public Map<String,Object> findComponentResource(Map<String, Object> params);
	
	
	/**
	 *  部件查询周边
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findComponentByParam(Map<String, Object> params);

	/**
	 *  城市综管查询周边
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	public EUDGPagination findListByParam(int page, int rows, Map<String, Object> params);


}