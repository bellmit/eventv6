package cn.ffcs.shequ.securityEx;

import cn.ffcs.shequ.security.RuleItem;
import cn.ffcs.shequ.security.data.InterceptingData;

import java.util.List;

/**
 * 描述：
 * Created by zengy on 2018/1/22.
 */
public class PaginationData implements InterceptingData {
    @Override
    public List getData(RuleItem ruleItem, Object... data) {
        for(Object obj :data){
            if(obj instanceof cn.ffcs.resident.bo.Pagination) {
                cn.ffcs.resident.bo.Pagination d = (cn.ffcs.resident.bo.Pagination) obj;
                return d.getList();
            }
        }
        return null;
    }
}
