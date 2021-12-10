<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格1员信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: #fff;">
	<div style="margin:2px 3px 0px 3px;">
	<#if userBO?exists>
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                    <li>
                    	<#if userBO.photo??>
							<img id="userImg" alt="" src="${RESOURSE_SERVER_PATH}${userBO.photo}" width="85" height="115" />
						<#else>
							<img id="userImg" alt="" src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" width="85" height="115" />
						</#if>
                    </li>
                    <li style="width:245px;">
                    	<p><span>${userBO.partyName!''}<#if userBO.positionName??>【${userBO.positionName!''}】</#if></span></p>
                        <p title="<#if userBO.gridPath??>${userBO.gridPath}</#if>"><#if userBO.gridPath??>${userBO.gridPath}</#if></p>
                        <p><code>联系电话：</code>${userBO.verifyMobile!''}</p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="ManagerContact">
            	<ul>
                	<li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="h_10"></div>
            <div class="ManagerSearch">
            	<div class="nav"> 
                    <ul>
                        <li class="current">轨迹查询</li>
                    </ul>
                    <div class="line"></div>
                </div>
                <div class="con">
                	<input type="hidden" id="imsi" name="imsi" value="${userBO.imsi!''}"/>
					<input type="hidden" id="userId" name="userId" value="${userBO.userId!''}"/>
                    <input type="hidden" id="userName" name="userName" value="${userName!''}"/>
					<input type="hidden" id="mobileTelephone" name="mobileTelephone" value="${userBO.mobileTelephone!''}"/>
                	<ul>
                    	<li><input type="text" id="start" onclick="WdatePicker({el:'start',startDate:'${startTime}',maxDate:'#F{$dp.$D(\'end\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:120px; height:26px; line-height:24px;" value="${startTime}"/></li>
                        <li>—</li>
                    	<li><input type="text" id="end" onclick="WdatePicker({el:'end',startDate:'${endTime}',minDate:'#F{$dp.$D(\'start\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:120px; height:26px; line-height:24px;" value="${endTime}"/></li>
                        <li><a href="javascript:void(0);" onclick="trajectoryQuery()" class="btn"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_4.png" />查询</a></li>
                    </ul>
                    <div class="clear"></div> 
                </div>
            </div>
        </div>
    <#else>
    <table cellpadding="0" cellspacing="0" border="0"  class="searchList-2">
    	<tr style="height: 185px"><td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty" >未查到相关数据！</td></tr>
    </table>
    </#if>
		
		
	</div>
	
</body>
<script type="text/javascript">
	var isCross;
	<#if isCross??>
		isCross = '${isCross}';
	</#if>
	var jsJiangYinFlag = "<#if jsJiangYinFlag??>${jsJiangYinFlag}<#else>false</#if>";

	//没有手机号码的时候，无法语音呼叫或发送短信，按钮变灰色
	var telephone = $("#mobileTelephone").val();
	if(telephone==null || telephone==""){
	 $("#callPhone").css("background-color","#5d5d5d");
	 $("#sendMsg").css("background-color","#5d5d5d");
	 $("#callPhone").attr("title","无电话号码");
	 $("#sendMsg").attr("title","无电话号码");
	}

	function sendMessage(userId,fixedTelephone){
		if(fixedTelephone==null || fixedTelephone==""){
			//alert("没有电话号码可以发送短信");
			return;
		}
		
		var url = "${SQ_ZZGRID_URL}/zzgl/map/data/situation/sendSMSPage.jhtml?userId="+userId+"&phone="+fixedTelephone;
		var title = "发送短信";
		
		if (isCross != undefined) { // 跨域
			url = url.replace(/\&/g,"%26");
			title = encodeURIComponent(encodeURIComponent(title));
			
			var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+475+","+200+")";
			$("#cross_domain_iframe").attr("src",urlDomain);
		} else {
			window.parent.showMaxJqueryWindow(title,url,475,200);
		}
	}
	
	function showCall(bCall, userName, userImg){
		if(userImg!=null && userImg!=""){
			userImg = "${RESOURSE_SERVER_PATH}" + userImg;
  	 	}
  	 	
		if (isCross != undefined) { // 跨域
			showCall_(bCall, userName, userImg);
		} else {
	  	 	showVoiceCall("${rc.getContextPath()}", window.parent.showCustomEasyWindow, bCall, userName, userImg);
		}
  	}
	 
	 var zhsq_url = "${SQ_ZHSQ_EVENT_URL}";
	 
	// 旧地图使用
	function showCall_(bCall, userName, userImg){
		if(bCall==null || bCall==""){
			return;
		 }
		 
		 if(userName==undefined || userName==null || userName=="null"){
			 userName = "";
		 }
		 if (userImg==undefined || userImg==null || userImg=='') { 
		  	 userImg = "${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png";
		 }
		 
		 if (zhsq_url.indexOf("http") == -1) {
		 	zhsq_url = "http://" + zhsq_url;
		 }
		 
		var url = zhsq_url + "/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall=" + bCall + "&userName="
		+ encodeURIComponent(encodeURIComponent(encodeURIComponent(encodeURIComponent(userName)))) + "&userImg=" + encodeURI(userImg) + "&"
		+ new Date().getTime();
		
		url = url.replace(/\&/g,"%26");
		
		var title = "语音盒呼叫";
		title = encodeURIComponent(encodeURIComponent(title));
		
		//var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+320+","+410+", 'no')";
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showCustomEasyWindow('"+title+"','"+url+"',"+314+","+410+", false, 'no')";
		
		$("#cross_domain_iframe").attr("src",urlDomain);
	}
	
var current = "<#if currentDate??>${currentDate}</#if>";
current = Date.parse(current.replace(/-/g,"/"));
var currentDate = new Date(current); // 当前时间

var currentYear = currentDate.getFullYear();//当前年份
var currentMonth = currentDate.getMonth() + 1;// 当前月份

var currentDate2 = new Date(currentYear, currentMonth, 0);
var lastDate = currentDate2.getDate(); // 当前月最后一天

var dateLine = new Date();
dateLine.setFullYear(currentYear);
dateLine.setMonth(currentMonth - 1);
dateLine.setDate(lastDate);
dateLine.setHours(23);
dateLine.setMinutes(59);
dateLine.setSeconds(59);

// 获取三个月前的时间
function get3MonthBefor(currentDate){  
   var resultDate,year,month,date,hms; 
   year = currentDate.getFullYear();
   month = currentDate.getMonth()+1;
   date = 1;
   switch(month)
   {
		case 1:
		case 2:
			month += 10;
			year--;
			break;
		default:
		    month -= 2;
		 	break;
   }
   
   month = (month < 10) ? ('0' + month) : month;
   resultDate = year + '/' + month +'/' +date;
   return new Date(resultDate);
}

function trajectoryQuery(){
	var start = $("#start").val();
	var end = $("#end").val();
	var imsi = $("#imsi").val();
	var mobileTelephone = $("#mobileTelephone").val();
	var userId = $("#userId").val();
	//2017-12-14 改造：不在通过IMSI号去查询轨迹，而是通过USER_ID去查询
//	if(imsi=="" && !jsJiangYinFlag){//江阴不判断是否绑定IMSI号
//		alert("未绑定IMSI号");
//		return;
//	}
	
	var tempDate = get3MonthBefor(currentDate);
	
	if (start > end) {
		alert("开始时间需要小于结束时间");
		return;
	}
	
	var start2 = Date.parse(start.replace(/-/g,"/"));
	var startDate = new Date(start2); // 开始时间
	
	var end2 = Date.parse(end.replace(/-/g,"/"));
	var endDate = new Date(end2); // 结束时间

	if (startDate.getTime() < tempDate.getTime()
			|| startDate.getTime() > dateLine.getTime()
			|| endDate.getTime() > dateLine.getTime()) {
		alert("只能查询三个月内（从" + (tempDate.getMonth() + 1) + "月1号开始）的轨迹数据");
		return;
	}
	var userName = $("#userName").val();
	
	var params = "?locateTimeBegin="+start+"&locateTimeEnd="+end+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&userName="+userName+"&jsJiangYinFlag="+jsJiangYinFlag+"&t="+Math.random();
	
	if (isCross != undefined) { // 跨域
		var url="${SQ_ZZGRID_URL}/zzgl/map/data/trajectory/trajectoryList.jhtml"+params; 
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"look_admin_trace('"+url+"','queryadmintracelayer','admin_trace1.png','"+start+"','"+end+"')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfGridAdmin.jhtml"+params;
		window.parent.getTrajectoryOfGridAdmin(url,start,end);
	}
}
function dynamicTrajectoryQuery(){
    var startDate = new Date(new Date().getTime() - 1 * 60 * 60 * 1000);
	var start = startDate.getFullYear() + "-" + (startDate.getMonth()+1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes();

    var endDate = new Date();
	var end = endDate.getFullYear() + "-" + (endDate.getMonth()+1) + "-" + endDate.getDate() + " " + endDate.getHours() + ":" + endDate.getMinutes();
	var imsi = $("#imsi").val();
	var mobileTelephone = $("#mobileTelephone").val();
	var userId = $("#userId").val();
    var userName = $("#userName").val();

	var params = "?locateTimeBegin="+start+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&userName="+userName+"&jsJiangYinFlag="+jsJiangYinFlag+"&t="+Math.random();
	
	if (isCross != undefined) { // 跨域
		var url="${SQ_ZZGRID_URL}/zzgl/map/data/trajectory/trajectoryList.jhtml"+params; 
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"subssgjj('"+url+"','queryadmintracelayer','admin_trace1.png')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfGridAdmin.jhtml"+params;
		window.parent.getDynamicTrajectoryOfGridAdmin(url,start,end); 
	}
}

//视频呼叫网格员
function mmp(){
	var userName='${userBO.userName!}';
	//if(userName!='huan')
	//	userName="test3";  
		
	var talkid=userName+"@mmp";//视频对应的网格员Id
	window.parent.showMmpSelector();  
	
	if(window.parent.checkActiveX())
		window.parent.StartVideoTalk(talkid);
}


	/**
	 * 巡查管理员有执法视频
	 */
	function showVideo(){
		var userName = "${userBO.userName!}";
		var partyName = "${userBO.partyName!''}";
		var xcyNO = "${userBO.partyName!''}";
        var userBOId = "${userBO.gridAdminId!''}";
		var src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/xcyGridAdminVideo.jhtml?gridAdminId="+gridAdminId;
		window.parent.showMaxJqueryWindow(partyName + "执法仪视频", src, 720, 540, false);
	}

	$(function(){
		var play = '<#if play??>${play}</#if>';//是否立即播放轨迹
		if(typeof play != 'undefined' && play == "true"){
            trajectoryQuery();
		}
	})
</script>
</html>
