package cn.ffcs.zhsq.typeRela.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.typeRela.TypeRelaMapper;
import cn.ffcs.zhsq.typeRela.service.ITypeRelaService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/22.
 */
@Service("typeRelaService")
@Transactional
public class TypeRelaServiceImpl implements ITypeRelaService {
    @Autowired
    private TypeRelaMapper typeRelaMapper;

    @Autowired
    private IBaseDictionaryService dictionaryService;

    private String TYPERELA_BIZ_TYPE = "00",//业务类型：00执纪问责问题
            SOURCE_PCODE = "B590000",//问题线索来源字典编码
            VIOLATIONMONEYTYPE_PCODE = "B590002",//违规违纪资金类别字典编码
            VIOLATIONTYPE_PCODE = "B590003";//违规违纪违法类别

    @Override
    public Long insertTypeRela(Map<String, Object> param, UserInfo userInfo) {

        Map<String, Object> map = new HashMap();

        //业务类型
        if (CommonFunctions.isNotBlank(param, "bizType")) {
            map.put("bizType", param.get("bizType"));
        } else {
            throw new IllegalArgumentException("缺少参数 bizType ！");
        }
        //业务ID
        if (CommonFunctions.isNotBlank(param, "bizId")) {
            map.put("bizId", param.get("bizId"));
        } else {
            throw new IllegalArgumentException("缺少参数 bizId !");
        }

        //类别名称:问题来源
        if (CommonFunctions.isNotBlank(param, "typeValue")) {
            //问题来源可能有多个
            String[] typeValueArr = (param.get("typeValue").toString()).split(",");

            //父级字典编码
            if(CommonFunctions.isNotBlank(param,"typeCode")){
                map.put("typeCode", param.get("typeCode"));
            } else {
                throw new IllegalArgumentException("缺少参数 typeCode ！");
            }

            if(CommonFunctions.isNotBlank(param,"bizColumn")){
                map.put("bizColumn", param.get("bizColumn"));
            } else {
                throw new IllegalArgumentException("缺少参数 bizColumn ！");
            }

            for (int i = 0, len = typeValueArr.length; i < len; i++) {
                //类别对应的主表字段
                map.put("typeVal", typeValueArr[i]);
                typeRelaMapper.insert(map);
            }

        } else {
            throw new IllegalArgumentException("缺少参数 typeValue ！");
        }
        //类别值
        return null;
    }

    @Override
    public Boolean delTypeRela(Map<String, Object> param) {
        Map<String, Object> map = new HashMap();
        //删除的关联数据类型为：00执纪问责问题
        if (CommonFunctions.isNotBlank(param, "bizType")) {
            map.put("bizType", param.get("bizType"));
        } else {
            throw new IllegalArgumentException("缺少参数 bizType ！");
        }

        if (CommonFunctions.isNotBlank(param, "bizId")) {
            map.put("bizId", param.get("bizId"));
        } else {
            throw new IllegalArgumentException("缺少参数 bizId ！");
        }

        boolean result = typeRelaMapper.delTypeRela(map);

        return result;
    }

}
