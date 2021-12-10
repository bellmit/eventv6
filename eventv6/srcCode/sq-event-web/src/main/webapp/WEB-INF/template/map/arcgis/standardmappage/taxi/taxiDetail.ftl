<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租车信息</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: #fff;">
	<div style="margin:2px 3px 0px 3px;">
	<#if data?exists>
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                <!--
                    <li>
						<img id="userImg" alt="" src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" width="85" height="115" />
                    </li>
                -->
                    <li style="width:100%;">
                    	<p><span>${data.carNo!''}</span><code>${data.buslineName!''}</code></p>
                        <p style="width:100%;"><code>车辆类型：</code>${data.carKindName!'暂无'}<code style="margin-left:10px;">登记日期：</code>${data.carDjrq!'暂无'}</p>
                        <p style="width:100%;"><code>登记证号：</code>${data.carDjzh!'暂无'}<code style="margin-left:10px;">购置证号：</code>${data.carGzzh!'暂无'}</p>
                        <p style="width:100%;"><code>发动机号：</code>${data.carFdjh!'暂无'}<code style="margin-left:10px;">发证日期：</code>${data.carFzrq!'暂无'}</p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
            <!--
            <div class="ManagerContact">
            	<ul>
                	<li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                	<li id="callPhone" class="YellowBg" onclick="showCall('','','');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />语音盒呼叫</li>
                	<li id="sendMsg" class="CyanBg" onclick="sendMessage('','');"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_3.png" />发送短信</li>
            		<li id="mmp" class="BlueBg" onclick="mmp();"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_5.png" />视频呼叫</li>
                </ul>
                <div class="clear"></div>
            </div>
            --> 
            <div class="h_10"></div>
            <div class="ManagerSearch">
            	<div class="nav"> 
                    <ul>
                        <li class="current">轨迹查询</li>
                        <li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                    </ul>
                    <div class="line"></div>
                </div>
                <div class="con">
                	<ul>
                    	<li><input type="text" id="start" onclick="WdatePicker({el:'start',startDate:'${startTime}',maxDate:'#F{$dp.$D(\'end\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:130px; height:26px; line-height:24px;" value="${startTime}"/></li>
                        <li>—</li>
                    	<li><input type="text" id="end" onclick="WdatePicker({el:'end',startDate:'${endTime}',minDate:'#F{$dp.$D(\'start\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:130px; height:26px; line-height:24px;" value="${endTime}"/></li>
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
	$('#MaxJqueryWindow_0',window.parent.document).css('height','200px');
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
	
var current = $("#end").val();
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
	
	var params = "?carId=${data.carId!c}&stime="+start+"&etime="+end+"&t="+Math.random();
	
	if (isCross != undefined) { // 跨域
		var url = "${rc.getContextPath()}/zhsq/map/taix/historyTrail.jhtml"+params;
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"look_admin_trace('"+url+"','taixLayer','admin_trace1.png','"+start+"','"+end+"')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/taix/historyTrail.jhtml"+params;
		window.parent.getTrajectoryOfTaxi(url,start,end);
	}
	parent.dialogs[0].window('collapse');
	//$('#MaxJqueryWindow_0',window.parent.document).css('display','none');
	//$('#MaxJqueryWindow_0',window.parent.document).prev('div').find('a').first().attr('class','panel-tool-collapse panel-tool-expand');
}
function dynamicTrajectoryQuery(){
	var start = '${startTime}';
	var end = '${endTime}';

	var params = "?devId=${data.devId!''}"
	
	if (isCross != undefined) { // 跨域
		var url = "${rc.getContextPath()}/zhsq/map/taix/daymicTrail.jhtml"+params;
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"subssgjj('"+url+"','taixLayer','admin_trace1.png')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/taix/daymicTrail.jhtml"+params;
		window.parent.getDynamicTrajectoryOfTaxi(url,start,end); 
	}
}

//视频呼叫网格员
function mmp(){
	var userName='';
		
	var talkid=userName+"@mmp";//视频对应的网格员Id
	window.parent.showMmpSelector();  
	
	if(window.parent.checkActiveX())
		window.parent.StartVideoTalk(talkid);
}


</script>
</html>
