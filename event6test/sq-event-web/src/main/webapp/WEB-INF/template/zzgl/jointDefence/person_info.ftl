<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员信息</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
                	
<body style="background-color: #fff;" >
					<input type="hidden" id="userId" name="userId" value="${data.USER_ID!''}"/>
                    <input type="hidden" id="userName" name="userName" value="${data.PARTY_NAME!''}"/>
					<input type="hidden" id="mobileTelephone" name="mobileTelephone" value="${data.RESIDENT_MOBILE!''}"/>
	<div style="margin:2px 3px 0px 3px;">
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                    <li style="width:160px;">
                    	<p><span>${data.PARTY_NAME!''}</span><code>联防长</code></p>
                        <p title="<#if data.ORG_ENTITY_PATH??>${data.ORG_ENTITY_PATH}</#if>"><#if data.ORG_ENTITY_PATH??>${data.ORG_ENTITY_PATH}</#if></p>
                        <p><code>联系电话：</code>${data.RESIDENT_MOBILE!''}</p>
                    </li>
                    <div class="ManagerContact">
                        <ul>
						
						</ul>
					</div>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="ManagerContact">
            	<ul>
                	    <li id="callPhone" class="YellowBg" onclick="showCall('${data.RESIDENT_MOBILE!''}','${data.PARTY_NAME!''}','${data.photo!''}');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />语音盒呼叫</li>
						<li id="sendMsg" class="CyanBg" onclick="sendMessage('${data.USER_ID!''}','${data.RESIDENT_MOBILE!''}');"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_3.png" />发送短信</li>
					
                </ul>
                <div class="clear"></div>
            </div>
            <div class="h_10"></div>
         
        </div>
   		
		
	</div>
	
</body>
<script  type="text/javascript">

	var isCross; 

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
</script>
</html>
