// 呼叫中心@zhangzhihua
/**
 * 代码框显示、隐藏函数
 */
function apiClick(id) {
	var o = document.getElementById(id);
	if (o.style.display == "") {
		o.style.display = "none";
	} else {
		o.style.display = "";
	}
}

/**
 * 判断是否安装agent,true:已安装;false:未安装
 */
function fCheckAgent(xo) {
	var xObject;
	var defaultObject = "AgentInsTallXControl.AgentInsTall";
	xObject = typeof(xo) == "undefined" || xo == "" ? defaultObject : xo;
	try {
		var a = new ActiveXObject(xObject);
	} catch (e) {
		return false;
	}
	return true;
}

/**
 * 启动UCAGENT
 */
function startAgentFun() {
	//return false;
	if (!fCheckAgent()) {
		//alert("您没有安装Agent,请退出本页面安装Agent");
		// 没有安装AGENT
		return;
	}
   // alert("启动");
	// var sDefault = "Ecc://";
	// 工号类型:(0-表示呼叫中心员工ID,1-表示外部系统员工ID),员工ID,,公司ID,公司名称
	// var str = sDefault + "0,3994,,1,测试";

	var rStaffId = "601";// 你们系统的工号;
	var rCompanyId = "232881";// 请做成配置，这个是我们系统的公司ID,正式上线后我们会根据具体企业改变，目前是测试的
	var rCompanyName = "厦门技术支持演示";// 请做成配置，这个是我们系统的公司名称,正式上线后我们会根据具体企业改变，目前是测试的
	var url="";
	url="Ecc://" + "1," + rStaffId + ",," + rCompanyId + ","
			+ rCompanyName;
			//alert(url);
			//return false;
			// alert(1211);
	window.location.href(url);
	// alert(1131);
    //window.open(url);
    // window.open(url,'呼叫中心','height=0,width=0');
	// window.location.href(str);
	window.event.returnValue = false;

// alert(111);
}

/**
 * OCX控件初始化
 */
function ocxInitFun() {
	var iReturn = null; // 在本方法的各段代码中公用。
	//alert(1);
	if(!oOCX|| !oOCX.IMRIOpenImr) return;
	iReturn = oOCX.IMRIOpenImr();

	if (iReturn == 3)
		window.alert("IMR版本未更新");
	if (iReturn == 4)
		window.alert("IMR_I.ocx版本未更新");
	if (iReturn != 0) {
		window.alert("连接IMR失败！" + iReturn);
	}
	if (iReturn == 0) {
	//	alert("初始化成功！");
		
	}
	return iReturn ;
}

/**
 * 呼出
 */
function callOutFun() {
	var caller = document.getElementById("callerNbr").value;
	if (caller == undefined) {
		caller = '';
	}
	oOCX.IMRISetValue(0, "SERVICE_SPACE_6", " DIALOUT;" + caller);
}

/**
 * 转接
 */
function callOtherFun() {
	oOCX.IMRISetValue(0, "SERVICE_SPACE_6", "CALLAWAY");

}

/**
 * 示忙函数
 */
function showBusyFun() {
	oOCX.IMRISetLocalState(1, 2);
}

/**
 * 示闲
 */
function showLeisureFun() {
	oOCX.IMRISetLocalState(1, 1);
}

/**
 * 静音
 */
function muteFun() {
	oOCX.IMRIMuteOnLink(1, 1);
}

/**
 * 取消静音
 */
function cancelMuteFun() {
	oOCX.IMRIMuteOnLink(1, 0);
}

/**
 * 设置动态参数
 */
function setParamValueFun() {
	var paramName = document.getElementById("paramName").value;
	var paramValue = document.getElementById("paramValue").value;
	var i = oOCX.IMRISetValue(0, paramName, paramValue);
	if (i == 0) {
		alert("设置成功！");
	} else {
		alert("设置失败！");
	}
}

/**
 * 获取动态参数值
 */
function getParamFun() {
	var paramName = document.getElementById("param").value;
	var str = oOCX.IMRIGetValue(1, paramName);
	alert(str);
}

/**
 * 更改话务台状态为未注册
 */
function terminalUnRegister() {
	if (oOCX.IMRIOpenImr() == 0) {
		try {
			return oOCX.IMRITermUnregister(1);
		} catch (ex) {
			return -1;
		}
	} else {
		return -1;
	}
}

/**
 * 签出
 */
function logOutFun() {
	var iReturn = null;
	if ((oOCX.IMRIGetLocalState(1) != oOCX.IMR_FREE)
			&& (oOCX.IMRIGetLocalState(1) != oOCX.IMR_LOCK)) {
		iReturn = terminalUnRegister();
		if (iReturn != 0) {
			if ((oOCX.IMRIGetLocalState(1) == oOCX.IMR_LINK)
					|| (oOCX.IMRIGetLocalState(1) == oOCX.IMR_CONSULTED_LINK)
					|| (oOCX.IMRIGetLocalState(1) == oOCX.IMR_CONSULTING_LINK)) {
				return -1;
			} else {
				return;
			}
		} else {
			alert("签出成功！");
			oOCX.IMRISetValue(0, "SERVICE_SPACE_6", "LOGOUT");
			return 0;
		}
	}
}