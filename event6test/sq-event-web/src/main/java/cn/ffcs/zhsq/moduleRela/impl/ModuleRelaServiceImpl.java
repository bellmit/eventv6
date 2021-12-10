package cn.ffcs.zhsq.moduleRela.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.moduleRela.service.IModuleRelaService;
import cn.ffcs.zhsq.mybatis.domain.moduleRela.ModuleRela;
import cn.ffcs.zhsq.mybatis.persistence.moduleRela.ModuleRelaMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: ztc
 * @Date: 2018/5/19 14:00
 */
@Service("moduleRelaServiceImpl")
@Transactional
public class ModuleRelaServiceImpl implements IModuleRelaService {
    @Autowired
    private ModuleRelaMapper moduleRelaMapper;

    @Override
    public boolean saveModuleRelaBatch(Map<String, Object> map) throws Exception {
        String moduleCodeLeft = "",//左方模块编码，001 扫黑除恶_线索管理；
        	   moduleCodeRight = "";//右方模块编码，001 扫黑除恶_黑恶团伙管理
        Long moduleIdLeft = -1L,//左方模块编号
        	 creatorId = -1L;
        List<ModuleRela> moduleRelaList = new ArrayList<>();
        StringBuffer msgWrong = new StringBuffer("");
        boolean flag = false;

        if (CommonFunctions.isNotBlank(map, "moduleCodeLeft")) {
            moduleCodeLeft = map.get("moduleCodeLeft").toString();
        } else {
        	msgWrong.append("参数moduleCodeLeft不能为空！");
        }
        if (CommonFunctions.isNotBlank(map, "moduleIdLeft")) {
        	try {
        		moduleIdLeft = Long.valueOf(map.get("moduleIdLeft").toString());
        	} catch(NumberFormatException e) {
        		msgWrong.append("参数moduleIdLeft[").append(map.get("moduleIdLeft")).append("]不能转换为有效的Long型数据！");
        	}
        	
        	if(moduleIdLeft < 0) {
        		msgWrong.append("参数moduleIdLeft[").append(map.get("moduleIdLeft")).append("]必须大于0！");
        	}
        } else {
        	msgWrong.append("参数moduleIdLeft不能为空！");
        }
        if (CommonFunctions.isNotBlank(map, "moduleCodeRight")) {
            moduleCodeRight = map.get("moduleCodeRight").toString();
        } else {
        	msgWrong.append("参数moduleCodeRight不能为空！");
        }
        if (CommonFunctions.isNotBlank(map, "userInfo")) {
            UserInfo userInfo = (UserInfo) map.get("userInfo");
            creatorId = userInfo.getUserId();
        } else if(CommonFunctions.isNotBlank(map, "creatorId")) {
        	try {
        		creatorId = Long.valueOf(map.get("creatorId").toString());
        	} catch(NumberFormatException e) {
        		e.printStackTrace();
        	}
        }

        if (CommonFunctions.isNotBlank(map, "moduleIdRightList")) {
        	List<Long> moduleIdRightList = (List<Long>) map.get("moduleIdRightList"),
        			   moduleIdRightValidList = new ArrayList<Long>();
        	
            for(Long moduleRightId : moduleIdRightList) {
            	if(moduleRightId > 0) {
            		moduleIdRightValidList.add(moduleRightId);
            	}
            }
            
            if(moduleIdRightValidList.size() > 0) {
            	ModuleRela moduleRela = null;
            	
            	for(Long moduleRightId : moduleIdRightValidList) {
            		moduleRela = new ModuleRela();
            		
            		moduleRela.setModuleCodeLeft(moduleCodeLeft);
            		moduleRela.setModuleIdLeft(moduleIdLeft);
            		moduleRela.setModuleCodeRight(moduleCodeRight);
            		moduleRela.setModuleIdRight(moduleRightId);
            		
            		if(creatorId > 0) {
            			moduleRela.setCreatorId(creatorId);
            		}
            		
            		moduleRelaList.add(moduleRela);
            	}
            } else {
            	msgWrong.append("参数moduleIdRightList[").append(StringUtils.join(moduleIdRightList, ",")).append("]中没有有效的数值！");
            }
            
        }

        if (moduleRelaList.size() > 0) {
            //将原有的关联记录状态置为：
            this.deleteByLeft(moduleCodeLeft, moduleIdLeft, map);
            flag = moduleRelaMapper.insertBatch(moduleRelaList) > 0;
        } else if(msgWrong.length() > 0) {
        	throw new Exception(msgWrong.toString());
        }
        
        return flag;
    }

    @Override
    public boolean deleteByLeft(String moduleCodeLeft, Long moduleIdLeft, Map<String,Object> params) {
    	boolean flag = false;
    	
    	if(StringUtils.isNotBlank(moduleCodeLeft) && moduleIdLeft != null) {
    		Map<String, Object> delMap = new HashMap<String, Object>();
    		delMap.put("moduleCodeLeft", moduleCodeLeft);
    		delMap.put("moduleIdLeft", moduleIdLeft);
    		
    		if(params != null && !params.isEmpty()) {
    			delMap.putAll(params);
    		}
    		
    		flag = moduleRelaMapper.deleteByLeft(params) > 0;
    	}
    	
    	return flag;
    }
    
    @Override
    public boolean deleteByRelaId(Long relaId, Long delUserId) {
    	boolean flag = false;
    	
    	if(relaId != null && relaId > 0) {
    		flag = moduleRelaMapper.delete(relaId, delUserId) > 0;
    	}
    	
    	return flag;
    }
    
    @Override
    public List<ModuleRela> findModuleRelaByLeft(String moduleCodeLeft, Long moduleIdLeft, Map<String, Object> params) {
    	List<ModuleRela> moduleRelaList = null;
    	
    	if(StringUtils.isNotBlank(moduleCodeLeft) && moduleIdLeft != null && moduleIdLeft > 0) {
    		Map<String, Object> queryParam = new HashMap<String, Object>();
    		
    		queryParam.put("moduleCodeLeft", moduleCodeLeft);
    		queryParam.put("moduleIdLeft", moduleIdLeft);
    		
    		if(params != null && !params.isEmpty()) {
    			queryParam.putAll(params);
    		}
    		
    		moduleRelaList = moduleRelaMapper.findModuleRelaByLeft(queryParam);
    	}
    	
    	return moduleRelaList;
    }
}
