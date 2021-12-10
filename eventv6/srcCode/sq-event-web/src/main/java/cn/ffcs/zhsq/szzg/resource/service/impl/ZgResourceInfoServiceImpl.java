package cn.ffcs.zhsq.szzg.resource.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceInfo;
import cn.ffcs.zhsq.mybatis.persistence.szzg.resource.ZgResourceInfoMapper;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description: zg_resource_info模块服务实现
 * @Author: huangwenbin
 * @Date: 09-16 10:01:50
 * @Copyright: 2017 福富软件
 */
@Service("zgResourceInfoServiceImpl")
@Transactional
public class ZgResourceInfoServiceImpl implements ZgResourceInfoService {

	@Autowired
	private ZgResourceInfoMapper zgResourceInfoMapper; //注入zg_resource_Info模块dao

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
	@Override
	public Map<String, Object> insert(Map<String, Object> params){
		try {
			if(params.get("resTypeCode") == null || params.get("resTypeCode").toString().length() == 0){
				params.put("msg", "资源类型编码为空/");
				params.put("status", "0");
				return params;
			}
	
			if(zgResourceInfoMapper.findByTypeCode(params) == 0	){//如果资源类型不存在
				params.put("status", "1");
				return params;
			}
			
			params = vaild(null,params,true);
			if(params.get("msg").toString().length() > 0){
				params.put("status", "0");
				return params;
			}
			ZgResourceInfo entity = new ZgResourceInfo();
		
			entity.setResTypeCode(params.get("resTypeCode").toString());
			entity.setOrgCode(params.get("orgCode").toString());
			entity.setResName(params.get("resName").toString());
			entity.setCreateUserId(Long.parseLong(params.get("createUserId").toString()));
			entity.setResTableId(Long.parseLong(params.get("resTableId").toString()));
			entity.setLng(Double.parseDouble(params.get("lng").toString()));
			entity.setLat(Double.parseDouble(params.get("lat").toString()));
			zgResourceInfoMapper.insert(entity);
			params.put("status", "1");
		} catch (Exception e) {
			params.put("status", "0");
			params.put("msg", e.getMessage());
			e.printStackTrace();
		}
		
		return params;
	}
	
	
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
	@Override
	public Map<String, Object> insert(ZgResourceInfo entity){
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			if(entity == null || entity.getResTypeCode() == null || entity.getResTypeCode().length() == 0){
				params.put("msg", "资源类型编码为空/");
				params.put("status", "0");
				return params;
			}
			params.put("resTypeCode", entity.getResTypeCode());
			if(zgResourceInfoMapper.findByTypeCode(params) == 0	){//如果资源类型不存在
				params.put("status", "1");
				return params;
			}
			params = vaild(entity,params,true);
			if(params.get("msg").toString().length() > 0){
				params.put("status", "0");
				return params;
			}
		
			zgResourceInfoMapper.insert(entity);
			params.put("status", "1");
		} catch (Exception e) {
			params.put("status", "0");
			params.put("msg", e.getMessage());
		}
		return params;
	}

	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName[可选] //资源名称
	 * @param lng[可选]//经度
	 * @param lat[可选]//纬度
	 * @param orgCode[可选]//信息域编码
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	@Override
	public Map<String, Object> update(Map<String, Object> params){
		ZgResourceInfo entity = new ZgResourceInfo();
		params = vaild(null,params,false);
		if(params.get("msg").toString().length() > 0){
			params.put("status", "0");
			return params;
		}
		try {
			entity.setResTypeCode(params.get("resTypeCode").toString());
			entity.setOrgCode(params.get("orgCode").toString());
			entity.setResName(params.get("resName").toString());
			entity.setUpdateUserId(Long.parseLong(params.get("updateUserId").toString()));
			entity.setResTableId(Long.parseLong(params.get("resTableId").toString()));
			entity.setLng(Double.parseDouble(params.get("lng").toString()));
			entity.setLat(Double.parseDouble(params.get("lat").toString()));
			zgResourceInfoMapper.update(entity);
			params.put("status", "1");
		} catch (Exception e) {
			params.put("status", "0");
			params.put("msg", e.getMessage());
		}
		return params;
	}

	private Map<String, Object> vaild(ZgResourceInfo entity,Map<String, Object> params,boolean isAdd){
		params.put("msg", "");
		if(entity != null){
			if(entity.getResTypeCode() == null || entity.getResTypeCode().length() == 0){params.put("msg", "资源类型编码为空/");}
			if(entity.getResTableId() == null){params.put("msg", params.get("msg").toString()+"资源相应表主键id为空/");}
			if(entity.getResName() == null|| entity.getResTypeCode().length() == 0){params.put("msg", params.get("msg").toString()+"资源名称为空/");}
			if(entity.getLng() == null){params.put("msg", params.get("msg").toString()+"经度为空/");}
			if(entity.getLat() == null){params.put("msg", params.get("msg").toString()+"纬度为空/");}
			if(entity.getOrgCode() == null|| entity.getOrgCode().length() == 0){params.put("msg", params.get("msg").toString()+"信息域编码为空/");}
			if(isAdd){
				if(entity.getCreateUserId() == null){params.put("msg", params.get("msg").toString()+"用户id为空/");}
			}else{
				if(entity.getUpdateUserId() == null){params.put("msg", params.get("msg").toString()+"用户id为空/");}
			}
		}else{
			if(params.get("resTypeCode") == null || params.get("resTypeCode").toString().length() == 0){params.put("msg", "资源类型编码为空/");}
			if(params.get("resTableId") == null|| params.get("resTableId").toString().length() == 0){params.put("msg", params.get("msg").toString()+"资源相应表主键id为空/");}
			if(params.get("resName") == null|| params.get("resName").toString().length() == 0){params.put("msg", params.get("msg").toString()+"资源名称为空/");}
			if(params.get("lng") == null|| params.get("lng").toString().length() == 0){params.put("msg", params.get("msg").toString()+"经度为空/");}
			if(params.get("lat") == null|| params.get("lat").toString().length() == 0){params.put("msg", params.get("msg").toString()+"纬度为空/");}
			if(params.get("orgCode") == null|| params.get("orgCode").toString().length() == 0){params.put("msg", params.get("msg").toString()+"信息域编码为空/");}
			if(isAdd){
				if(params.get("createUserId") == null|| params.get("createUserId").toString().length() == 0){params.put("msg", params.get("msg").toString()+"用户id为空/");}
			}else{
				if(params.get("updateUserId") == null|| params.get("updateUserId").toString().length() == 0){params.put("msg", params.get("msg").toString()+"用户id为空/");}
			}
		}
		return params;
	}
	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName[可选] //资源名称
	 * @param lng[可选]//经度
	 * @param lat[可选]//纬度
	 * @param orgCode[可选]//信息域编码
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	@Override
	public Map<String, Object> update(ZgResourceInfo entity){
		Map<String, Object> params = new HashMap<String, Object>();
		params = vaild(entity,params,false);
		if(params.get("msg").toString().length() > 0){
			params.put("status", "0");
			return params;
		}
		try {
			zgResourceInfoMapper.update(entity);
			params.put("status", "1");
		} catch (Exception e) {
			params.put("status", "0");
			params.put("msg", e.getMessage());
		}
		return params;
	}
	
	/**
	 * 删除数据(单条)
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param updateUserId(必须)//用户id
	 * 删除数据(批量)
	 * @param resTypeCode(必须) //资源类型编码
	 * @param key:resTableIds,value:"1,2,3" (必须) //资源相应表主键id 
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	@Override
	public Map<String, Object> delete(Map<String, Object> params){
		params.put("msg", "");
		if(params.get("resTypeCode") == null || params.get("resTypeCode").toString().length() == 0){params.put("msg", "资源类型编码为空/");}
		if((params.get("resTableId") == null  || params.get("resTableId").toString().length() == 0) &&
				(params.get("resTableIds") == null  || params.get("resTableIds").toString().length() == 0)){params.put("msg", params.get("msg").toString()+"资源相应表主键id为空/");}
		if(params.get("updateUserId") == null || params.get("updateUserId").toString().length() == 0){params.put("msg", params.get("msg").toString()+"用户id为空/");}
		if(params.get("msg").toString().length() > 0){
			return params;
		}
		if(params.get("resTableIds") != null && params.get("resTableIds").toString().length() > 0){
			if(!Pattern.matches("(\\d+,)*\\d+", params.get("resTableIds").toString())){
				params.put("status", "0");
				params.put("msg", "resTableIds格式有误");
				return params;
			}
			try {
				zgResourceInfoMapper.deleteByList(params);
				params.put("status", "1");
			} catch (Exception e) {
				params.put("status", "0");
				params.put("msg", e.getMessage());
			}
		}else{
			if(!Pattern.matches("\\d+", params.get("resTableId").toString())){
				params.put("status", "0");
				params.put("msg", "resTableId格式有误");
				return params;
			}
			try {
				ZgResourceInfo entity = new ZgResourceInfo();
				entity.setResTypeCode(params.get("resTypeCode").toString());
				entity.setResTableId(Long.parseLong(params.get("resTableId").toString()));
				entity.setUpdateUserId(Long.parseLong(params.get("updateUserId").toString()));
				zgResourceInfoMapper.deleteByBean(entity);
				params.put("status", "1");
			} catch (Exception e) {
				params.put("status", "0");
				params.put("msg", e.getMessage());
			}
		}
		return params;
	}

	/**
	 * 删除数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param updateUserId(必须)//用户id
	 * @return status	1:成功,0:失败
	 * 		   msg		反馈信息
	 */
	@Override
	public Map<String, Object> delete(ZgResourceInfo entity){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msg", "");
		if(entity.getResTypeCode() == null){params.put("msg", "资源类型编码为空/");}
		if(entity.getResTableId() == null){params.put("msg", params.get("msg").toString()+"资源相应表主键id为空/");}
		if(entity.getUpdateUserId() == null){params.put("msg", params.get("msg").toString()+"用户id为空/");}
		if(params.get("msg").toString().length() > 0){
			return params;
		}
		try {
			zgResourceInfoMapper.deleteByBean(entity);
			params.put("status", "1");
		} catch (Exception e) {
			params.put("status", "0");
			params.put("msg", e.getMessage());
		}
		return params;
	}
	
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return zg_resource_info分页数据对象
	 */
	public EUDGPagination findList(int page, int rows, Map<String, Object> params){
		RowBounds rowentityunds = new RowBounds((page - 1) * rows, rows);
		List<ZgResourceInfo> list = zgResourceInfoMapper.findPageListByCriteria(params, rowentityunds);
		long count = zgResourceInfoMapper.findCountByCriteria(params);
		return new EUDGPagination(count, list);
	}
	
	/**
	 * 根据业务id查询数据
	 * @param id zg_resource_infoid
	 * @return zg_resource_info业务对象
	 */
	public ZgResourceInfo findById(Long id){
		return zgResourceInfoMapper.findById(id);
	}
	
	/**
	 * 
	 */
	public List<ZgResourceInfo> findByParam(Map<String, Object> params){
		return zgResourceInfoMapper.findByParam(params);
	}
	
	public Map<String,Object> findResource(Map<String, Object> params){
		return zgResourceInfoMapper.findResource(params);
	}

	/**
	 * 新增数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param createUserId(必须)//用户id
	 */
	public int insertByList(List<Map<String, Object>> list){
		
		return 0;
	}

	/**
	 * 新增数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName(必须) //资源名称
	 * @param lng(必须)//经度
	 * @param lat(必须)//纬度
	 * @param orgCode(必须)//信息域编码
	 * @param createUserId(必须)//用户id
	 */
	public int insertByBeans(List<ZgResourceInfo> entity){
		return 0;
	}
	
	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName[可选] //资源名称
	 * @param lng[可选]//经度
	 * @param lat[可选]//纬度
	 * @param orgCode[可选]//信息域编码
	 * @param updateUserId(必须)//用户id
	 */
	public int updateByList(List<Map<String, Object>> params){
		return 0;
	}

	/**
	 * 修改数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param resName[可选] //资源名称
	 * @param lng[可选]//经度
	 * @param lat[可选]//纬度
	 * @param orgCode[可选]//信息域编码
	 * @param updateUserId(必须)//用户id
	 */
	public int updateByBeans(List<ZgResourceInfo> entity){
		return 0;
	}
	/**
	 * 删除数据
	 * @param resTypeCode(必须) //资源类型编码
	 * @param resTableId(必须) //资源相应表主键id
	 * @param updateUserId(必须)//用户id
	 */
	
	public int delete(List<ZgResourceInfo> entity){
		return 0;
	}
	
	
	/**
	 * 	部件查询周边
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String,Object>> findComponentByParam(Map<String, Object> params){
		return zgResourceInfoMapper.findComponentByParam(params);
	}

	@Override
	public EUDGPagination findListByParam(int page, int rows, Map<String, Object> params) {
		RowBounds rowentityunds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = zgResourceInfoMapper.findListByParam(params, rowentityunds);
		long count = zgResourceInfoMapper.findCountByParam(params);
		return new EUDGPagination(count, list);
	}

	/**
	 * 	部件查询周边
	 * @param params
	 * @return
	 */
	@Override
	public Map<String,Object> findComponentResource(Map<String, Object> params){
		return zgResourceInfoMapper.findComponentResource(params);
	}
}