var ncctools = function () {
};
ncctools.input = function(title, info, fnCallBack) {
	   var clickBtn = "";
	   var keyEvent = "";

		   clickBtn = "<input type=button id='btn_ncc_input' value=\"确定\" class=\"btn_ncc_input\"  style=margin-left:5px; />";
		   keyEvent = "onKeyDown=\"if(event.keyCode == 13){nccbar.exTransfer(hojo.byId('id_ncc_input').value)}\"";

	   var msgw,msgh,bordercolor;
	   msgw=247;
	   msgh=82;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById("nccbar").offsetWidth;
	   sHeight=document.documentElement.scrollHeight;	//document.getElementById("nccbar").offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','nccbody.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.3";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById("nccbar").appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","nccbody.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
       msgObj.style.left = "50%";
       msgObj.style.top = "50%";
       msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
       msgObj.style.marginLeft = "-135px" ;
       msgObj.style.marginTop = 0+document.documentElement.scrollTop+"px";
       msgObj.style.width = msgw + "px";
       msgObj.style.height =msgh + "px";
       msgObj.style.textAlign = "left";
       msgObj.style.lineHeight ="25px";
       msgObj.style.zIndex = "10001";
       // msgObj.style.paddingTop = "2px";
       // msgObj.style.paddingLeft = "18px";
       // msgObj.style.paddingRight = "4px";
       msgObj.innerHTML = "<div style='height:27px;background: #f3f3f3;overflow:hidden;padding-top:5px;padding-right:10px;'><div style='text-align:right'>" +
           					"<div style=\"float: left;padding-left: 5px;font-size: 14px;font-weight: 600;color: #1AA2D6;\">"+title+"</div>" +
       							"<a href=javascript:ncctools.close();>" +
       								"<img src='imgs/close_1.gif' onmouseover=\"this.src='imgs/close_2.gif'\" onmouseout=\"this.src='imgs/close_1.gif'\" />" +
       							"</a>" +
       						"</div></div>" +

       						"<div style='height:35px;padding-top: 10px;overflow:hidden;text-align:center'>" +
       							"<div id='nccbody.loading.message' style='float:left;color:#666666;padding-left:5px;'>" +
       								"<input id='id_ncc_input' value='"+info+"'   onfocus='if(value==\""+info+"\") {value=\"\"}'  onblur='if(value==\"\"){value=\""+info+"\"}' type='text' size='20' class='fl ncc_input_value' "+keyEvent+" />" + clickBtn + "</div>" +
       						"</div>";
       document.getElementById("nccbar").appendChild(msgObj);
       var msgDiv=$('#nccbar');
    	msgDiv.find('.btn_ncc_input').unbind('click').click(function () {
			   try {
                   var seatId = msgDiv.find('.ncc_input_value').val();
                   fnCallBack(seatId);
               } catch (e) {
               }
               ncctools.close();
           });
};

ncctools.close = function() {
	if(ncctools.notifyDialogInterval != "") {
		clearInterval(ncctools.notifyDialogInterval);
	}
	var bgObj = document.getElementById("nccbody.bgDiv");
	var msgObj = document.getElementById("nccbody.msgDiv");
	if(msgObj != null) {
		document.getElementById("nccbar").removeChild(msgObj);	
	}
	if(bgObj != null) {
		document.getElementById("nccbar").removeChild(bgObj);	
	}
};

ncctools.dateParse = function(date) {
    var year = date.getFullYear(); 
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    return year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day) + " " 
                + (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + (second > 9 ? second : "0" + second);
};

ncctools.notifyDialogStayRemain;
ncctools.notifyDialogInterval = "";
ncctools.index = 1;

ncctools.showLoading = function(destExten) {
	ncctools.notifyDialogStayRemain = 40000;
	ncctools.index = 1;
	ncctools.loading("", "nccbar");
	ncctools.notifyDialogInterval = setInterval(function(){
		ncctools.checkLoadingHide(destExten);
	}, 1000);
};

ncctools.checkLoadingHide = function(destExten) {
	var index = ncctools.index ++;
	var html = ("正在等待<span style='color:#3fb23f;font-weight:bold'>"+destExten+ "</span>接听，" + "请稍后<span style='font-weight:bold'>(00:" + (index<10?("0"+index):index) +")</span>");
	document.getElementById("nccbody.loading.message").innerHTML = html;
	if(ncctools.notifyDialogStayRemain <= 0){
		ncctools.hideNotify();
	}
	ncctools.notifyDialogStayRemain -= 1000;
};

ncctools.checkTransferLoadingHide = function(destExten) {
	var index = ncctools.index ++;
	var html = ("转接中...<a href='#' onclick=\"javascript:phone.phone_cancelTransfer();\" style=\"margin-left:3px;cursor:pointer;font-weight:bold;color:red;text-decoration:underline;\">取消</a>");
	document.getElementById("nccbody.loading.message").innerHTML = html;
};

ncctools.hideNotify = function() {
	ncctools.close();
	clearInterval(ncctools.notifyDialogInterval);
};

ncctools.loading = function(message, parentId) {
	   var msgw,msgh,bordercolor;
	   msgw=247;
	   msgh=52;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById(parentId).offsetWidth;
	   sHeight=document.documentElement.scrollHeight;	//document.getElementById(parentId).offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','nccbody.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.3";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById(parentId).appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","nccbody.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
	   msgObj.style.left = "50%";
	   msgObj.style.top = "50%";
	   msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	   msgObj.style.marginLeft = "-135px" ;
	   msgObj.style.marginTop = -5+document.documentElement.scrollTop+"px";
	   msgObj.style.width = msgw + "px";
	   msgObj.style.height =msgh + "px";
	   msgObj.style.textAlign = "left";
	   msgObj.style.lineHeight ="25px";
	   msgObj.style.zIndex = "10001";
	   msgObj.style.paddingTop = "11px";
	   msgObj.style.paddingLeft = "12px";
	   msgObj.style.paddingRight = "10px";
	   msgObj.innerHTML = "<div style='height:20px;overflow:hidden;text-align:center'><img src='imgs/loading.gif' style='float:left;margin-top:5px;' /><div id='nccbody.loading.message' style='float:left;color:#666666;padding-left:5px;'>" + message + "</div><div style='clear:both;height:1px;overflow:hidden'>&nbsp;</div></div>";

	   document.getElementById(parentId).appendChild(msgObj);
};


ncctools.error = function(message) {
	var msgw,msgh,bordercolor;
	msgw=249;
    msgh=152;
    bordercolor="#c6c6c6";
   
    var sWidth,sHeight;
    sWidth=document.getElementById("nccbar").offsetWidth;
    sHeight=document.documentElement.scrollHeight;	//document.getElementById("nccbar").offsetHeight;
    var bgObj=document.createElement("div");
    bgObj.setAttribute('id','nccbody.bgDiv');
    bgObj.style.position="absolute";
    bgObj.style.top="0";
    bgObj.style.background="#f2f2f2";
    bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity="0.3";
    bgObj.style.left="0";
    bgObj.style.width=sWidth + "px";
    bgObj.style.height=sHeight + "px";
    bgObj.style.zIndex = "10000";
    document.getElementById("nccbar").appendChild(bgObj);

    var msgObj=document.createElement("div");
    msgObj.setAttribute("id","nccbody.msgDiv");
    msgObj.setAttribute("align","center");
    msgObj.style.background="white";
    msgObj.style.border="1px solid " + bordercolor;
    msgObj.style.position = "absolute";
	msgObj.style.left = "50%";
	msgObj.style.top = "50%";
    msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
    msgObj.style.marginLeft = "-135px" ;
    msgObj.style.marginTop = 0+document.documentElement.scrollTop+"px";
    msgObj.style.width = msgw + "px";
    msgObj.style.height =msgh + "px";
    msgObj.style.textAlign = "left";
    msgObj.style.lineHeight ="25px";
    msgObj.style.zIndex = "10001";
    msgObj.innerHTML = "<div style='height:27px;background: #f3f3f3;overflow:hidden;padding-top:10px;padding-right:10px;'><div style='float:right'><a href='javascript:ncctools.close();'><img src='imgs/close_1.gif' onmouseover=\"this.src='imgs/close_2.gif'\" onmouseout=\"this.src='imgs/close_1.gif'\" style='cursor: pointer;border:0px;' /></a></div></div>"+
        					"<div style='height:50px;overflow:hidden;margin-top:20px;padding-left:20px;'><img src='imgs/error.jpg' style='float:left;margin-top:4px;' /><div style='color:#666666;padding-left:5px;font-size:14px;padding-top:4px'>"+message+"</div></div>"+
        					"<div style='clear:both;height:1px;overflow:hidden'>&nbsp;</div>"+
        					"<div style='padding-left:180px;padding-top:10px;'><a href='javascript:ncctools.close();'><img src='imgs/confirm_1.jpg' onmouseover=\"this.src='imgs/confirm_2.jpg'\" onmouseout=\"this.src='imgs/confirm_1.jpg'\" style='cursor: pointer;border:0px;' /></a></div>";

    document.getElementById("nccbar").appendChild(msgObj);
}

ncctools.showSucc = function(message) {
	ncctools.notifyDialogStayRemain = 500;
	ncctools.success(message);
	ncctools.notifyDialogInterval = setInterval("ncctools.checkHide()", 500);
};

ncctools.checkHide = function() {
	if(ncctools.notifyDialogStayRemain <= 0){
		ncctools.hideNotify();
	}
	ncctools.notifyDialogStayRemain -= 500;
};

ncctools.success = function(message) {
	   var msgw,msgh,bordercolor;
	   msgw=172;
	   msgh=56;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById("nccbar").offsetWidth;
	   sHeight=document.documentElement.scrollHeight;	//document.getElementById("nccbar").offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','nccbody.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.3";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById("nccbar").appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","nccbody.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
	   msgObj.style.left = "50%";
	   msgObj.style.top = "50%";
	   msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	   msgObj.style.marginLeft = "-105px" ;
	   msgObj.style.marginTop = 0+document.documentElement.scrollTop+"px";
	   msgObj.style.width = msgw + "px";
	   msgObj.style.height =msgh + "px";
	   msgObj.style.textAlign = "left";
	   msgObj.style.lineHeight ="25px";
	   msgObj.style.zIndex = "10001";
	   msgObj.style.paddingTop = "18px";
	   msgObj.style.paddingLeft = "35px";
	   msgObj.innerHTML = "<div style='height:30px;overflow:hidden'><img src='imgs/success.jpg' style='float:left;margin-top:4px;' /><div style='float:left;color:#666666;padding-left:5px;font-size:15px;padding-top:4px'>" + message + "</div><div class='clear1'>&nbsp;</div></div>";

	   document.getElementById("nccbar").appendChild(msgObj);
};

ncctools.nccbar_showTranster = function(destExten) {
	var msgw,msgh,bordercolor;
	msgw=247;
	msgh=52;
	bordercolor="#c6c6c6";

	var sWidth,sHeight;
	sWidth=document.getElementById("nccbar").offsetWidth;
	sHeight=document.documentElement.scrollHeight;	//document.getElementById("nccbar").offsetHeight;
	var bgObj=document.createElement("div");
	bgObj.setAttribute('id','nccbody.bgDiv');
	bgObj.style.position="absolute";
	bgObj.style.top="0";
	bgObj.style.background="#f2f2f2";
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity="0.3";
	bgObj.style.left="0";
	bgObj.style.width=sWidth + "px";
	bgObj.style.height=sHeight + "px";
	bgObj.style.zIndex = "10000";
	document.getElementById("nccbar").appendChild(bgObj);

	var msgObj=document.createElement("div");
	msgObj.setAttribute("id","nccbody.msgDiv");
	msgObj.setAttribute("align","center");
	msgObj.style.background="white";
	msgObj.style.border="1px solid " + bordercolor;
	msgObj.style.position = "absolute";
	msgObj.style.left = "50%";
	msgObj.style.top = "50%";
	msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	msgObj.style.marginLeft = "-135px" ;
	msgObj.style.marginTop = 0+document.documentElement.scrollTop+"px";
	msgObj.style.width = msgw + "px";
	msgObj.style.height =msgh + "px";
	msgObj.style.textAlign = "left";
	msgObj.style.lineHeight ="25px";
	msgObj.style.zIndex = "10001";
	msgObj.style.paddingTop = "11px";
	msgObj.style.paddingLeft = "12px";
	msgObj.style.paddingRight = "10px";
	msgObj.innerHTML = "<div style='height:20px;overflow:hidden;text-align:center'><img src='imgs/loading.gif' style='float:left;margin-top:5px;' />" +
		"<div id='nccbody.loading.message' style='float:left;color:#666666;padding-left:5px;'>" +
		+ "</div><div style='clear:both;height:1px;overflow:hidden'>&nbsp;</div></div>";
	document.getElementById("nccbar").appendChild(msgObj);
	ncctools.checkTransferLoadingHide(destExten);
}


