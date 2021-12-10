package cn.ffcs.zhsq.eliminatelettertho.controller;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoReChgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
 * @Description: 三书一函接收与整改情况表模块控制器
 * @Author: liangbzh
 * @Date: 08-09 16:41:05
 * @Copyright: 2021 福富软件
 */ 
@Controller("eliminateLetterThoReChgController")
@RequestMapping("/zhsq/eliminateLetterThoReChg")
public class EliminateLetterThoReChgController extends ZZBaseController {

	@Autowired
	private IEliminateLetterThoReChgService eliminateLetterThoReChgService; //注入三书一函接收与整改情况表模块服务

}