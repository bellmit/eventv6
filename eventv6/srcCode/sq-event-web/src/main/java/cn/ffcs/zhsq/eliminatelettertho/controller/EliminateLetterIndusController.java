package cn.ffcs.zhsq.eliminatelettertho.controller;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterIndusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
 * @Description: 三书一函行业领域表模块控制器
 * @Author: liangbzh
 * @Date: 08-09 16:39:13
 * @Copyright: 2021 福富软件
 */ 
@Controller("eliminateLetterIndusController")
@RequestMapping("/zhsq/eliminateLetterIndus")
public class EliminateLetterIndusController extends ZZBaseController {

	@Autowired
	private IEliminateLetterIndusService eliminateLetterIndusService; //注入三书一函行业领域表模块服务

}