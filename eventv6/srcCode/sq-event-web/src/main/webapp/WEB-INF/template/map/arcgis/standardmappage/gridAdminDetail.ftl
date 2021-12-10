<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <title>网格员信息</title>
<script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: transparent;">
<div id="gridAdminDetaiDiv" style="overflow-x: hidden;max-height: 340px;">
	<div style="margin:2px 3px 5px 3px;">
	<#if gridAdmin?exists>
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                    <li>
                    	<#if gridAdmin.photo??>
							<img id="userImg" alt="" src="${RESOURSE_SERVER_PATH}${gridAdmin.photo}" width="85" height="115" />
						<#else>
							<img id="userImg" alt="" src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" width="85" height="115" />
						</#if>
                    </li>
                    <li style="width:245px;">
                   	  <#if  isUserAnychat=="true" && gridAdmin.enabledAnyChat &&  gridAdmin.enabledAnyChat==1 >
                  		  <div style="float: right;" id="anychat">
                  		  <#if gridAdmin.isOnline=='0'>
                  		    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/list-phone.png" ></img>
	                    	<img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/list-video.png" ></img>
                  		 	 <#else>
                  		     <img id="phone" style="cursor:pointer;" src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/list-phone1.png" ></img>
	                    	 <img id="vedio" style="cursor:pointer;" src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/list-video1.png"" ></img>
                  		  </#if>
                    	</div>
                       </#if>
                    	<p><code>${gridAdmin.dutyLabel!''}</code><span>${gridAdmin.partyName!''}</span>
                    	</p>
                    	<p></p>
						<!--<p title="<#if gridAdmin.gridPath??>${gridAdmin.gridPath}</#if>"><#if gridAdmin.gridPath??><#if gridAdmin.gridPath?length gt 12 >${gridAdmin.gridPath[0..11]}...<#else>${gridAdmin.gridPath}</#if></#if></p>-->
                        <p title="<#if gridAdmin.gridPath??>${gridAdmin.gridPath}</#if>"><#if gridAdmin.gridPath??>${gridAdmin.gridPath}</#if></p>
                        <p><code>在线状态：</code><#if gridAdmin.isOnline == '1'>在线<#else>离线</#if><code>&nbsp;&nbsp;联系电话：</code>${gridAdmin.mobileTelephone!''}</p>
						<#if isDaFengFlag?? && isDaFengFlag=='true'>
							<p><code>管辖内：</code>${gridAdmin.regNum!''}户； ${gridAdmin.total!''}人； ${gridAdmin.merNum!''}商户</p>
						<#else>
							<p><code>当日巡查次数：</code><#if patrolNums??>${patrolNums}<#else>0</#if>&nbsp;&nbsp;<code>有效巡查时间：</code><#if patrolHours??>${patrolHours}<#else>0</#if>小时</p>
							<p><code>服务对象：</code>户总数:${gridAdmin.regNum!''}；人总数:${gridAdmin.total!''}</p>
						</#if>
                        <#if gridAdminWithWorkFlag?? && gridAdminWithWorkFlag=='true'>
							<p><a href="###" onclick="showWorkLog()" style="color:#0075a9;">工作日志</a></p>
						</#if>
                    </li>
                    <div class="ManagerContact">
                        <ul>
						<#if jsJiangYinFlag?? && jsJiangYinFlag=='true' && gridAdmin.duty?? && gridAdmin.duty=='006' && gridAdmin.remark??>
							<li id="mmp" class="BlueBg" style="height:26px;padding: 0px 10px" onclick="showVideo();"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_5.png" />查看视频</li>
						</#if>
						</ul>
					</div>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="ManagerContact">
            	<ul>
                	<li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
					<#if isUserYyhhj?? && isUserYyhhj=="true">
                        <li id="callPhone" class="YellowBg" onclick="showCall('${gridAdmin.mobileTelephone!''}','${gridAdmin.partyName!''}','${gridAdmin.photo!''}');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />语音盒呼叫</li>
					</#if>
					<#if jsJiangYinFlag?? && jsJiangYinFlag=="true">
                        <li id="callPhone2" class="YellowBg" onclick="showCall('${gridAdmin.mobileTelephone!''}','${gridAdmin.partyName!''}','${gridAdmin.photo!''}','makeCallPhone');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />软电话呼叫</li>
					</#if>
					<#if isUserFsdx?? && isUserFsdx=="true">
                		<li id="sendMsg" class="CyanBg" onclick="sendMessage('${gridAdmin.userId!''}','${gridAdmin.mobileTelephone!''}');"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_3.png" />发送短信</li>
					</#if>
                	<#if isUserMmp?? && isUserMmp=='true'>
                		<li id="mmp" class="BlueBg" onclick="mmp();"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_5.png" />视频呼叫</li> 
                	</#if>
                    <li class="PrinkBg" onclick="showWorkDetail();"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />工作详情</li>
                   <#if isUseMediasoup?? && isUseMediasoup=='true'>
                    <li class="BlueBg" id="mediasoup" style="display: none;" >视频对讲</li>
                   </#if>
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
                	<input type="hidden" id="imsi" name="imsi" value="${gridAdmin.imsi!''}"/>
					<input type="hidden" id="userId" name="userId" value="${gridAdmin.userId!''}"/>
                    <input type="hidden" id="userName" name="userName" value="${userName!''}"/>
					<input type="hidden" id="mobileTelephone" name="mobileTelephone" value="${gridAdmin.mobileTelephone!''}"/>
                	<ul>
                    	<li >
							<input type="text" id="trackDate1" class="inp1" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'%y-%M-%d',onpicked:function(){trackDate2.click();} })" style="width:74px; height:26px; line-height:24px;">
						</li>
	                    <li >
	                    	<input type="text" id="trackDate2" class="Wdate inp1" onclick="WdatePicker({dateFmt:'HH:mm',onpicked:function(){trackDate3.click();} })" style="width:65px; height:26px; line-height:24px;">
	                    </li>
	                    <li>—</li>
	                    <li >
	                    	<input type="text" id="trackDate3" class="Wdate inp1" onclick="WdatePicker({dateFmt:'HH:mm',minDate:'#F{$dp.$D(\'trackDate2\')}'})" style="width:65px; height:26px; line-height:24px;">
	                    </li>
                        <li><a href="javascript:void(0);" onclick="trajectoryQuery()" class="btn"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_4.png" />查询</a></li>
                    </ul>
                    <ul>
	                	<li class="LC2" style="width: 125px">
	                		<input name="" type="button" value="查询上午"  class="NorBtn" onclick="serchAMTrajectory()" style="margin-top: 10px;"/>
	                	</li>
	                	<li class="LC2"  style="width: 125px">
	                		<input name="" type="button" value="查询下午"  class="NorBtn" onclick="serchPMTrajectory()" style="margin-top: 10px;"/>
	                	</li>
	                </ul>
                    <div class="clear"></div> 
                </div>
        </div>
    <#else>
    <table cellpadding="0" cellspacing="0" border="0"  class="searchList-2">
    	<tr style="height: 185px"><td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty" >未查到相关数据！</td></tr>
    </table>
    </#if>
	</div>
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
	 $("#callPhone2").css("background-color","#5d5d5d").attr("title","无电话号码");
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
	
	function showCall(bCall, userName, userImg,softPhone){
		if(userImg!=null && userImg!=""){
			userImg = "${RESOURSE_SERVER_PATH}" + userImg;
  	 	}
  	 	
		if (isCross != undefined) { // 跨域
			showCall_(bCall, userName, userImg);
		} else {
	  	 	showVoiceCall("${rc.getContextPath()}", window.parent.showCustomEasyWindow, bCall, userName, userImg,softPhone);
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
	$.blockUI({message: "加载中，请稍后..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    		
	var trackDate1 = $("#trackDate1").val();
	var trackDate2 = $("#trackDate2").val();
	var trackDate3 = $("#trackDate3").val();
	if (trackDate1 == "") {
        $("#trackDate1").focus();
        return;
	}
	if (trackDate2 == "") {
        $("#trackDate2").focus();
        return;
	}
	if (trackDate3 == "") {
        $("#trackDate3").focus();
        return;
	}
	var start = trackDate1 + " " + trackDate2;
	var end = trackDate1 + " " + trackDate3;
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
	
	var params = "?trackDate="+trackDate1+"&locateTimeBegin="+start+"&locateTimeEnd="+end+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&userName="+userName+"&jsJiangYinFlag="+jsJiangYinFlag+"&t="+Math.random();
	
	if (isCross != undefined) { // 跨域
		var url="${SQ_ZZGRID_URL}/zzgl/map/data/trajectory/trajectoryList.jhtml"+params; 
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"look_admin_trace('"+url+"','queryadmintracelayer','admin_trace1.png','"+start+"','"+end+"')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfGridAdmin.jhtml"+params;
		window.parent.getTrajectoryOfGridAdmin(url,start,end,null, function() {
			$.unblockUI();
		});
	}
}
function dynamicTrajectoryQuery(){
	$.blockUI({message: "加载中，请稍后..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    
	var date = new Date();
    var y = date.getFullYear();
    var m = (date.getMonth() + 1) < 10 ? ("0" + (date.getMonth() + 1)) : (date.getMonth() + 1);
    var d = date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate();
    var h = date.getHours() < 10 ? ('0' + date.getHours()) : date.getHours()
    var f = date.getMinutes() < 10 ? ('0' + date.getMinutes()) : date.getMinutes()
    var s = date.getSeconds() < 10 ? ('0' + date.getseconds()) : date.getSeconds()
    var formatdate = y+'-'+m+'-'+d + " " + h + ":" + f + ":" + s;

    var start = y+'-'+m+'-'+d + " " + (h-1) + ":" + f + ":" + s;
	var end = y+'-'+m+'-'+d + " " + h + ":" + f + ":" + s;
	var imsi = $("#imsi").val();
	var mobileTelephone = $("#mobileTelephone").val();
	var userId = $("#userId").val();
    //2017-12-14 改造：不在通过IMSI号去查询轨迹，而是通过USER_ID去查询
//	if(imsi=="" && !jsJiangYinFlag){//江阴不判断是否绑定IMSI号
//		alert("未绑定IMSI号");
//		return false;
//	}
    var trackDate1 = $("#trackDate1").val();
    if (trackDate1 == "") {
        $("#trackDate1").focus();
        return;
    }
    var userName = $("#userName").val();

	var params = "?trackDate="+trackDate1+"&locateTimeBegin="+start+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&userName="+userName+"&jsJiangYinFlag="+jsJiangYinFlag+"&t="+Math.random();
	
	if (isCross != undefined) { // 跨域
		var url="${SQ_ZZGRID_URL}/zzgl/map/data/trajectory/trajectoryList.jhtml"+params; 
		url = url.replace(/\&/g,"%26");
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"subssgjj('"+url+"','queryadmintracelayer','admin_trace1.png')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfGridAdmin.jhtml"+params;
		window.parent.getDynamicTrajectoryOfGridAdmin(url,start,end, function() {
			$.unblockUI();
		}); 
	}
}

//视频呼叫网格员
function mmp(){
	var userName='${gridAdmin.userName!}';
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
		var userName = "${gridAdmin.userName!}";
		var partyName = "${gridAdmin.partyName!''}";
		var xcyNO = "${gridAdmin.partyName!''}";
        var gridAdminId = "${gridAdmin.gridAdminId!''}";
		var src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/xcyGridAdminVideo.jhtml?gridAdminId="+gridAdminId;
		window.parent.showMaxJqueryWindow(partyName + "执法仪视频", src, 720, 540, false);
	}

	$(function(){
		var myDate = new Date();
		var month = myDate.getMonth() + 1;
		month = month > 9 ? month : "0" + month;
		var dayNum = myDate.getDate();
		dayNum = dayNum > 9 ? dayNum : "0" + dayNum;
		$("#trackDate1").val("${queryDay!''}");
		$("#trackDate2").val("${startTime!''}");
		$("#trackDate3").val("${endTime!''}");

		var play = '<#if play??>${play}</#if>';//是否立即播放轨迹
		if(typeof play != 'undefined' && play == "true"){
            trajectoryQuery();
		}
        // $("#gridAdminDetaiDiv").css({
        //     maxHeight: 300
        // });

        // $("#gridAdminDetaiDiv").mCustomScrollbar({theme:"minimal-dark"});
	});
	
	function serchAMTrajectory(){
		$("#trackDate2").val("07:00");
		$("#trackDate3").val("12:00");
		trajectoryQuery();
	}
	
	function serchPMTrajectory(){
		$("#trackDate2").val("12:00");
		$("#trackDate3").val("21:00");
		trajectoryQuery();
	}
	
	//工作日志
	function showWorkLog(){
        var userId = $("#userId").val();
        var src = "${OA_DOMAIN!''}/web/log/myLog.mhtml?userId="+userId;
        window.parent.showMaxJqueryWindow("工作日志", src, 920, 440, false, true);
	}

    //工作详情
    function showWorkDetail(){
        var src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminWorkDetail.jhtml?gridAdminUserId=${gridAdmin.userId!''}&gridOrgCode=${gridAdmin.infoOrgCode!''}&gridAdminId=${gridAdmin.gridAdminId!''}&orgId=${orgId!''}";
        window.parent.showMaxJqueryWindow("工作详情", src, 1320, 580, false, true);
    }
</script>
<script>
var isOnAnychat=parent.isInitAnycat==0?false:true,isUserAnychat='${isUserAnychat}';
if(isOnAnychat&&isUserAnychat=='true'){
	
	$('#phone').click(function(){
		parent.transBuffer_userId('${gridAdmin.userId!}','${gridAdmin.partyName!}',2);
	});
	$('#vedio').click(function(){
		parent.transBuffer_userId('${gridAdmin.userId!}','${gridAdmin.partyName!}',1);
	});
}
</script>
<!-- Mediasoup视频消息组件${gridAdmin.userId !=userId!"30020928"} -->	
<#if isUseMediasoup??  && isUseMediasoup!="false"  && isUseMediasoup!="0" &&  gridAdmin.userId !=userId!"" > 
<script src="${uiDomain!''}/web-assets/common/js/mpush4mediasoup_detail.js "></script>
<script>
       mpush.init({
	      components_domain:'${COMPONENTS_URL}',//组件工程域名
	      uiDomain:'${uiDomain!''}',//ui公共样式域名
	      c_userId:"${gridAdmin.userId!''}",//接收用户id
          c_partyName:"${gridAdmin.partyName!''}",//接收用户姓名
	      detailBtbId:'mediasoup',//页面视频点击按钮按钮id，如效果图1的视频对讲按钮
	      isUseMediasoup:'${isUseMediasoup!'1'}'
	    });
</script>
</#if>
</html>
