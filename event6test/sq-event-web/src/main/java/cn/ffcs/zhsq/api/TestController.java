package cn.ffcs.zhsq.api;/**
 * Created by Administrator on 2017/8/19.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhongshm
 * @create 2017-08-19 15:07
 **/


@Controller
@RequestMapping("/service")
public class TestController {

    @ResponseBody
    @RequestMapping(value="/test")
    public String test(@RequestParam(value="name",defaultValue="success") String name){
        name = "1" + name;


        return name;
    }
}
