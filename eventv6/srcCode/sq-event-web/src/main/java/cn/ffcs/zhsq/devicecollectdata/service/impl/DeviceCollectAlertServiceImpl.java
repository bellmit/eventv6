package cn.ffcs.zhsq.devicecollectdata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectAlertService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceCollectAlertMapper;

/**
 * @Description: 设备采集告警数据模块服务实现
 * @Author: husp
 * @Date: 10-13 09:04:54
 * @Copyright: 2017 福富软件
 */
@Service("deviceCollectAlertServiceImpl")
@Transactional
public class DeviceCollectAlertServiceImpl implements DeviceCollectAlertService {

	@Autowired
	private DeviceCollectAlertMapper deviceCollectAlertMapper; //注入设备采集告警数据模块dao

	/**
	 * 新增数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 设备采集告警数据id
	 */
	@Override
	public Long insert(DeviceCollectAlert bo) {
		deviceCollectAlertMapper.insert(bo);
		return bo.getCollectAlertId();
	}

	/**
	 * 修改数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DeviceCollectAlert bo) {
		long result = deviceCollectAlertMapper.update(bo);
		return result > 0;
	}
	/**
	 * 保存或修改数据，存在修改，不存在保存
	 * @param runDataList 设备采集数据指标项编码和值集合
	 * @param bo 设备采集数据业务对象
	 * @param resultMap 返回信息
	 * @return 是否修改成功
	 */
	@SuppressWarnings("unchecked")
	public boolean saveOrUpdate(List<Map<String,Object>> runDataList,DeviceCollectAlert bo,Map<String, Object> resultMap){
		boolean flag=true;
		int i = 0;
		try{
			RowBounds rowBounds = new RowBounds(0, 20);
			DeviceCollectAlert bo1=null;
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("deviceId", bo.getDeviceServiceId());
			params.put("bizType", bo.getBizType());
			List<DeviceCollectAlert> list = deviceCollectAlertMapper.searchList(params, rowBounds);
			if(list!=null && list.size()>0){
				DeviceCollectAlert bo2=list.get(0);
				
				bo1=new DeviceCollectAlert();
				bo1.setDeviceServiceId(bo.getDeviceServiceId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				try{
					bo1.setCollectTime(sdf.parse(bo.getCollTime()));
				}catch(Exception e){
					resultMap.put("status", 1);
					resultMap.put("desc", "采集时间collTime异常，正确格式：2017-01-01 01:01:01");
					return false;
				}
				if(bo2.getCollectAlertJsonStr()!=null && !"".equals(bo2.getCollectAlertJsonStr())){
					JSONArray jsonArray = JSONArray.fromObject(bo2.getCollectAlertJsonStr());  
					List<Map<String,Object>> listJson = (List<Map<String,Object>>)jsonArray;  
			        if(listJson!=null && listJson.size()>0){
			        	String collectAlertJsonStr="";
			        	List<Map<String,Object>> slist=new ArrayList<Map<String,Object>>();
			        	for (int j = 0; j < listJson.size(); j++) {  
			        		boolean sflag=true;
			        		Map<String,Object> lobj=listJson.get(i);  
			        		String skey ="";
				            for(Entry<String,Object> entry1 : lobj.entrySet()){  
				            	skey=entry1.getKey();  
				            }  
				            for (i = 0; i < runDataList.size(); i++) {  
					            Map<String,Object> obj=runDataList.get(i);  
					            String skey1 ="";
					            for(Entry<String,Object> entry : obj.entrySet()){  
					            	skey1=entry.getKey();  
					            }  
					            if(skey.equals(skey1)){
					            	sflag=false;
					            	slist.add(obj);
					            	break;
					            }
					        }  
				            if(sflag){
				            	slist.add(lobj);
				            }
			        	}
			        	
			        	if(slist!=null && slist.size()>0){
			        		JSONArray json = JSONArray.fromObject(slist); 
			        		collectAlertJsonStr = json.toString();//把json转换为String 
			        		bo1.setCollectAlertJsonStr(collectAlertJsonStr);
							deviceCollectAlertMapper.update(bo1);
			        	}
			        }else{
			        	bo1.setCollectAlertJsonStr(bo.getCollectAlertJsonStr());
						bo1.setBizType(bo.getBizType());
						bo1.setStatus("1");
						deviceCollectAlertMapper.insert(bo1);
			        }
				}else{
					bo1.setCollectAlertJsonStr(bo.getCollectAlertJsonStr());
					bo1.setBizType(bo.getBizType());
					bo1.setStatus("1");
					deviceCollectAlertMapper.insert(bo1);
				}
			}else{
				bo1=new DeviceCollectAlert();
				bo1.setDeviceServiceId(bo.getDeviceServiceId());
				bo1.setCollectAlertJsonStr(bo.getCollectAlertJsonStr());
				bo1.setBizType(bo.getBizType());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				try{
					bo1.setCollectTime(sdf.parse(bo.getCollTime()));
				}catch(Exception e){
					resultMap.put("status", 1);
					resultMap.put("desc", "采集时间collTime异常，正确格式：2017-01-01 01:01:01");
					return false;
				}
				bo1.setStatus("1");
				deviceCollectAlertMapper.insert(bo1);
			}
		}catch(Exception e){
			Map<String,Object> obj=runDataList.get(i);  
			String errinfo="调用异常";
			if(obj!=null){
				for(Entry<String,Object> entry : obj.entrySet()){  
	                String skey = entry.getKey();  
	                errinfo=errinfo+":"+skey+"异常";
				}   
			}
			resultMap.put("status", 1);
			resultMap.put("desc", errinfo);
			return false;
		}
		
		return flag;
	}
	/**
	 * 删除数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DeviceCollectAlert bo) {
		long result = deviceCollectAlertMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备采集告警数据分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeviceCollectAlert> list = deviceCollectAlertMapper.searchList(params, rowBounds);
		formatOutData(list);
		long count = deviceCollectAlertMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 设备采集告警数据id
	 * @return 设备采集告警数据业务对象
	 */
	@Override
	public DeviceCollectAlert searchById(Long id) {
		DeviceCollectAlert bo = deviceCollectAlertMapper.searchById(id);
		return bo;
	}

	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<DeviceCollectAlert> findList( Map<String, Object> params) {		
		List<DeviceCollectAlert> list = deviceCollectAlertMapper.searchList(params);
		formatOutData(list);
		return list;
	}
	
	
	/**
	 * 格式化输出参数
	 * @param twoStations
     */
	private void formatOutData(List<DeviceCollectAlert> list){
	
		for(DeviceCollectAlert deviceCollectAlert : list){
			if(null != deviceCollectAlert.getCollectTime()){
				deviceCollectAlert.setCollTime(DateUtils.formatDate(deviceCollectAlert.getCollectTime(), DateUtils.PATTERN_24TIME));
			}
			
		}
	}
	/**
	 * 根据设备ID获取告警数
	 * @param deviceId
	 * @return
	 */
	public Integer getCountByDeviceId(Long deviceId) {	
		return deviceCollectAlertMapper.getCountByDeviceId(deviceId);
	}

	@Override
	public List<DeviceCollectAlert> findListByDeviceId(Long deviceId) {
		List<DeviceCollectAlert> list = deviceCollectAlertMapper.findListByDeviceId(deviceId);
		formatOutData(list);
		return list;
	}
	
	
	public Long countList(Map<String, Object> params){
		return deviceCollectAlertMapper.countList(params);
	}
}