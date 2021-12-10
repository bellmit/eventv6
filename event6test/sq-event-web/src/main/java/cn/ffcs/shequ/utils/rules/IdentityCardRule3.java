package cn.ffcs.shequ.utils.rules;

import cn.ffcs.shequ.security.rules.IRuleStrategy;

/**
 * 描述：身份证隐匿处理
 * Created by wuxq on 2020/12/11.
 */
public class IdentityCardRule3 implements IRuleStrategy {
    @Override
    public String operate(String txt) {
        if(txt!=null && txt.length()>1){
            String newtxt ="";
            if(txt.length()==18){
                newtxt = txt.replaceAll("(\\d{12})([a-zA-Z\\d]{6})", "$1******");
            }else if(txt.length()==15){
                newtxt= txt.substring(0,5)+"********";
            }
            return newtxt;
        }
        return txt;
    }
}

