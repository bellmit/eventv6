<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件调度</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-common.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/scim/styles/gridListStyle.css" />
<script src="${rc.getContextPath()}/theme/scim/scripts/tablestyle.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/MyDatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/scim/gis/query/css/nav_hover.css"/>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/scim/styles/pop.css" />
<script type="text/javascript" src="${rc.getContextPath()}/theme/scim/gis/gisdt/callCenter.js"></script>

<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerForm.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDateEditor.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerCheckBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerButton.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerRadio.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerSpinner.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerTextBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js"></script>
<style>
.btn_handle{ background:url(${rc.getContextPath()}/theme/scim/images/scim/btn_handle.png) no-repeat; width:56px; height:48px; display:block; text-indent:-99999px;}
textarea{
	width:95%;
	border:1px solid #dadada;
}
</style>
</head>
<body>
<div id="chartdiv2" style="display: none;">
</div>
	<input type="hidden" id="eventId" name="eventId" value="${event.eventId?c}"/>
	<input type="hidden" id="typeName" name="typeName" value="${event.typeName}"/>
	<input type="hidden" id="level" name="level" value="<#if event.level?exists>${event.level}</#if>"/>
	<input type="hidden" id="orgCode" name="orgCode"/>
	<#if taskInfo??>
	  <input type="hidden" id="taskInfo.taskInfoId" name="taskInfo.taskInfoId" value="<#if taskInfo??><#if taskInfo.taskInfoId??>${taskInfo.taskInfoId?c}</#if></#if>"/>
	</#if>
	<input type="hidden" id="userIds" name="userIds"/>
	<input type="hidden" id="distributaryUserIds" name="distributaryUserIds"/>
<div id="chartdiv">
  <table border="0" cellspacing="1" cellpadding="0" class="tb_cont" align="center">
      <tr>
        <td class="sj_title_sty">事件分类</td>
        <td class="sj_cot_sty"> 	<#if event.eventClass??>${event.eventClass}</#if></td>
      <#if event.type=='0105'>
        <td class="sj_title_sty">效能投诉点</td>
        <td class="sj_cot_sty"><#if event.gridName??>${event.gridName}</#if>&nbsp;&nbsp;
        </td>
        <td rowspan="4" class="sj_img_sty" valign="top" align="center">
        	<#if attachmentList?? && (attachmentList?size>0)>
        		<#list attachmentList as item>
        			<input type="hidden" id="efficiency_${item_index}" value="${RESOURSE_SERVER_PATH}/${item.filePath}"/>
        		</#list>
        		<img id="efficiencyImg" src="${RESOURSE_SERVER_PATH}/${firstImg}" name="0" title="第1/${attachmentList?size}张图片" width="138" height="140" onclick="changePic('efficiencyImg','efficiency_', this.name, '${attachmentList?size - 1}', '1')" style="cursor:hand"/>
        	<#else>
				<img src="${rc.getContextPath()}/images/notbuilding.gif" width="138" height="140"/>
			</#if>
			<!--
        	<#if event.firstImg??>
				<img src="${RESOURSE_SERVER_PATH}/${event.firstImg}" id="firstImg" width="138" height="140"/>
			<#else>
				<img src="${rc.getContextPath()}/images/notbuilding.gif" width="138" height="140"/>
			</#if>
			-->
           <!-- div style="margin-top:50px;">无事件处理前照片</div> -->
        </td>
      </tr>
      <tr>
      	<td class="sj_title_sty">反馈人员</td>
        <td class="sj_cot1_sty">
           <#if event.reporter??>${event.reporter}</#if>
        </td>
        <td class="sj_title_sty">反馈电话</td>
        <td class="sj_cot1_sty">
				<#if event.telephone??>${event.telephone}</#if>			
          &nbsp;
          <label id="registerName" style="display:none;"><#if event.reporter??>${event.reporter}</#if>	</label>
          <label id="registerPhone" style="display:none;"><#if event.reporteTel??>${event.reporteTel}</#if>	</label>
          
          <!-- 根据权限点的开关显示隐藏拨号功能 -->
          <!--
           <a href="javascript:callPhone();" > <img border="0" src="${rc.getContextPath()}/theme/scim/gis/img/sj_p.png" style="vertical-align: middle" width="20px" height="20px" title="电话"/></a>
           -->
           <#if event.reporteTel??>
				<a href="javascript:callPhone();" > 
		    		<img src="${rc.getContextPath()}/theme/scim/gis/img/sj_p.png" style="vertical-align: middle;border:none" width="24" height="24" 
		    		title="电话号码：<#if event.reporteTel??>${event.reporteTel}</#if>" >
		   	    </a>  
		   </#if>
        </td>
      </tr>
      <tr>
      <#else>
      <td class="sj_title_sty">采集人员</td>
        <td class="sj_cot_sty">
        <#if event.createUserName??>${event.createUserName}</#if>&nbsp;&nbsp;
        </td>
        <td rowspan="4" class="sj_img_sty" valign="top" align="center">
        	<#if attachmentList?? && (attachmentList?size>0)>
        		<#list attachmentList as item>
        			<input type="hidden" id="attachment_${item_index}" value="${RESOURSE_SERVER_PATH}/${item.filePath}"/>
        		</#list>
        		<img id="attachmentImg" src="${RESOURSE_SERVER_PATH}/${firstImg}" name="0" title="第1/${attachmentList?size}张图片" width="138" height="140" onclick="changePic('attachmentImg','attachment_', this.name, '${attachmentList?size - 1}', '1')" style="cursor:hand"/>
        	<#else>
				<img src="${rc.getContextPath()}/images/notbuilding.gif" width="138" height="140"/>
			</#if>
			<!--
        	<#if event.firstImg??>
				<img src="${RESOURSE_SERVER_PATH}/${event.firstImg}" id="firstImg" width="138" height="140"/>
			<#else>
				<img src="${rc.getContextPath()}/images/notbuilding.gif" width="138" height="140"/>
			</#if>
			-->
           <!-- div style="margin-top:50px;">无事件处理前照片</div> -->
        </td>
      </tr>
      <tr>
      	<td class="sj_title_sty">所属网格</td>
        <td class="sj_cot1_sty">
          	<#if event.gridName??>${event.gridName}</#if>
        </td>
        <td class="sj_title_sty">紧急程度</td>
        <td class="sj_cot1_sty">
				<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>
          &nbsp;
        </td>
      </tr>
      <tr>
      	<td class="sj_title_sty">所属楼宇</td>
        <td class="sj_cot_sty">
        	<#if event.buildingName??>
	        	${event.buildingName}
	        	
        	</#if>
        	<img title="周边资源" onclick="javascript:window.parent.popSurroundingResLayer('${event.eventId?c}');" src="${rc.getContextPath()}/images/presources.png" width="18" height="18" style="cursor:hand;"/>
        	<!--<img title="周边资源" onclick="javascript:window.parent.popSurroundingResLayer('${event.eventId?c}');" src="${rc.getContextPath()}/images/presources.png" width="18" height="18" style="cursor:hand;"/>
        		
			<img title="全球眼" onclick="javascript:window.parent.popGlobalEyeLayer();" src="${rc.getContextPath()}/images/webcam.png" width="18" height="18" style="cursor:hand;"/>&nbsp;&nbsp;&nbsp;
			-->
        </td>
        <td class="sj_title_sty">事发详址</td>
        <td class="sj_cot1_sty">
        	<span class="sj_red">
     			<#if event.occurred??>
					<#if event.occurred?length gt 12>
						${event.occurred[0..12]}
					<#else>
						${event.occurred}
					</#if>
				</#if>
        	</span>
        </td>
      </tr>
      <tr>
      </#if>
      	<td class="sj_title_sty">发生时间</td>
        <td class="sj_cot_sty"><#if event.happenTimeStr??>${event.happenTimeStr}</#if></td>
        <td class="sj_title_sty">处理时限</td>
        <td class="sj_cot1_sty">
        	<span class="sj_red">
     			<#if event.remainTime??>
					${event.remainTime}
			   </#if>
        	</span>
        </td>
      </tr>
      <#if (event.type='0203' && !isLock)>
      <tr>
    	<td class="sj_title_sty">涉及金额</td>
		<td colspan="4" bgcolor="#ffffff" style="padding-left:4px;">
			${event.involvedMoney?if_exists} 元
		</td>
	  </tr>
	  </#if><#--    矛盾纠纷 0203 end -->
      <tr>
        <td class="sj_title_sty">事件描述</td>
        <td colspan="4" bgcolor="#ffffff" style="padding-left:4px;" title="<#if event.content??>${event.content}</#if>">
            <#if event.content??>
               ${event.content}
            </#if>
		</td>
      </tr>
      <tr>
      	<td class="sj_title_sty">
      		<#if taskInfo??>
				<#if taskInfo.status == '00' && taskInfo.userName??>
					办理人
				<#elseif taskInfo.status == '00' && taskInfo.orgName??> 
					办理机构
				<#elseif taskInfo.status == '01' && taskUserNames??>
					办理人 
				</#if>
			</#if>
		</td>
      	<td class="sj_cot_sty">
      	 	<#if taskInfo??>
				<#if taskInfo.status == '00' && taskInfo.userName??>
					${taskInfo.userName}
				<#elseif taskInfo.status == '00' && taskInfo.orgName??> 
					${taskInfo.orgName}
				<#elseif taskInfo.status == '01' && taskUserNames??>
					${taskUserNames} 
				</#if>
			</#if>
      	</td>
      	<td class="sj_title_sty">当前状态</td>
      	<td colspan="2" class="sj_cot1_sty">
      		<span class="sj_red">
      			<#if event.statusName??>${event.statusName}</#if>
			</span>
      	</td>
      </tr>
      	<#if taskFlag?? && taskFlag == 'true'>
      	<tr>
	        <td class="sj_title_sty">事件处理</td>
	        <td colspan="4" class="sj_cot_sty">
		          <table border="0" cellspacing="0" cellpadding="0" class="tb_cont1">
		            <tr>
		              <td width="51%" rowspan="2">
		              	<textarea id="results" name="results" cols="20" rows="3"></textarea>
		              </td>
		              <td width="280" class="sj_blue">
			              <#if  event.status != '03'>
			              	<input id="endCase" name="caseAction" type="radio" value="0" />结案&nbsp;&nbsp;&nbsp;
			              </#if>
			                <#if  event.status != '00' && event.status != '03'>
			              	<input id="distributeCase" name="caseAction" type="radio" value="1" />分流到部门&nbsp;&nbsp;&nbsp;<br/>
			               </#if>
			                <#if  event.status != '00' && event.status != '03'>
			              	<input id="distributeCase2" name="caseAction" type="radio" value="3" />分流到人&nbsp;&nbsp;&nbsp;
			               </#if>
			               <#if  event.status != '03'>
			              	<input id="reportCase" name="caseAction" type="radio" value="2" />上报
			             </#if>
		              </td>
		              <td width="5" align="center">&nbsp;
		             </td>
		              <td width="80" rowspan="2" align="center"><a href="javascript:doAction();" class="btn_handle">办理</a></td>
		            </tr>
		            <tr>
		              <td bgcolor="#FFFFFF"><span class="sj_box1" id="result" style="display:none;"></span></td>
		              <td width="5" align="center">&nbsp;</td>
		            </tr>
				</table>
	        </td>
	      </tr>
       </#if>
       <#if taskInfo??>
          <#if taskInfo.results??>
		   <tr>
        	<td class="sj_title_sty">
        		办理结果
        	</td>
        	<td colspan="4" class="sj_cot_sty">
        		${taskInfo.results }
        	</td>
		</tr>
        </#if>
     </#if>
    </table>
</div>
<#include "/zzgl_event/customEasyWin.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">

	$("#reportCase").click(function(){
		var reportType = 1;//一级上报
		var eventId = ${event.eventId?c};
		$.ajax({
				url: "${rc.getContextPath()}/zzgl/event/outPlatform/getParentOrg.jhtml?eventId="+eventId + "&reportType=" + reportType,
				success: function(jsonObject){
					var obj = eval(jsonObject);
					var title = "";
					var value = "";
					
					if (obj != null) {
						if (obj.orgName == null || obj.orgName == "") {
							title = "上级机构不存在！";
						} else {
							title = obj.orgName;
						}
						value = formatString(title, 10);
						$('#orgCode').attr('value',obj.orgCode);
						//显示当前操作的提示信息
					} else {
						 title = "上级机构不存在";
						 value = title;
					}
					$("#result").parent().attr("title", title);
					$("#result").text(value);
					$("#result").show();
				}
			});
	 });
	 
	 $("#distributeCase").click(function(){
	 	$("#result").text("");
		//分流到部门
		var level = '<#if event.level??>${event.level}</#if>';
		
		var eventId = ${event.eventId?c};
		var url = '${rc.getContextPath()}/zzgl/event/outPlatform/showDistributaryPage.jhtml?eventId='+eventId + "&type=gis";
		showCustomEasyWindow2("分流到部门",630,300,10,10,url);
	 });
	 
	 $("#distributeCase2").click(function(){
	 	$("#result").text("");
		//分流到人
		var level = '<#if event.level??>${event.level}</#if>';
		
		var eventId = ${event.eventId?c};
		var url = '${rc.getContextPath()}/zzgl/event/outPlatform/showSecondShuntPage.jhtml?eventId='+eventId + "&type=gis";
		//showMaxJqueryWindow("分流到人", url);
		//var url = '${rc.getContextPath()}/zzgl/event/outPlatform/showDistributaryPage.jhtml?eventId='+eventId + "&type=gis";
		showCustomEasyWindow2("分流到人",630,300,10,10,url);
	 });
	 
     function doAction() {
		var caseAction = $(":radio[name=caseAction]:checked").val();
		if (caseAction == null || caseAction == "") {
			alert("请至少选择一种处理类型！");
			return;
		}
		if (caseAction == "0") {
			endCase();
		} else if (caseAction == "1") {//分流到部门
			distributeCase();
		} else if (caseAction == "3") {//分流到人
			distributeCase2();
		} else if (caseAction == "2") {
			var reportFlag = 0;
			reportCase(reportFlag);//
		}
	}
	$("#endCase").click(function(){
	 $("#result").text("");
	});
    function endCase(){
	   $.messager.confirm('提示',"确认要对该事件进行结案处理?",function(r){
		if(r){
			var results = $('#results').val();
			if(results == null || results == ''){
				$.messager.alert('错误','请填写处理结果！','error');
				return;
			}
			var eventId = ${event.eventId?c};
			var typeName = '${event.typeName}';
			var taskInfoId = '<#if taskInfo??><#if taskInfo.taskInfoId??>${taskInfo.taskInfoId?c}</#if></#if>';
			var level = '<#if event.level??>${event.level}</#if>';
			var url = "${rc.getContextPath()}/zzgl/event/outPlatform//endCase.jhtml?eventId="+eventId + "&typeName="+typeName + "&taskInfoId="+taskInfoId + "&level=" + level + "&results="+encodeURIComponent(encodeURIComponent(results));
			showCustomEasyWindow2("发送短信",580,350,10,10,url);
		}
	  });
   }

	function reportCallBack(userIds,userNames){
	    $('#userIds').attr('value',userIds);
		var textValue = formatString(userNames,10);
		$("#result").parent().attr("title", userNames);
		$("#result").text(textValue);
		$("#result").show();
		closeCustomEasyWin();
	}

	function reportCase(reportFlag){
		$.messager.confirm('提示',"确认上报吗?",function(r){
				if(r){
					var results = $('#results').val();
					if(results == null || results == ''){
						$.messager.alert('错误','请填写处理结果！','error');
						return;
					}
					var orgCode = $('#orgCode').val();
					if(orgCode == null || orgCode == ''){
					  $.messager.alert('错误','上级机构不存在！','error');
					  return;
					}
					var isReport = '1';//一级上报
					var eventId = ${event.eventId?c};
					var typeName = '${event.typeName}';
					var taskInfoId = '<#if taskInfo??><#if taskInfo.taskInfoId??>${taskInfo.taskInfoId?c}</#if></#if>';
					var level = '<#if event.level??>${event.level}</#if>';
					var userIds = $('#userIds').val();
					var url = "${rc.getContextPath()}/zzgl/event/outPlatform/reportCase.jhtml?eventId="+eventId + 
					          "&typeName="+typeName + "&taskInfoId="+taskInfoId + "&level=" + level + 
					          "&results="+encodeURIComponent(encodeURIComponent(results)) + "&isReport=" + isReport  + "&userIds=" + userIds;
					showCustomEasyWindow2("发送短信",580,350,10,10,url);
				}
			});
	}

	/**
	 * 超过10个字符，加...
	 */
	function formatString(str, len) {
		if (len == null) {
			len = 10;
		}
		if (str == null || str == "") {
			return "";
		}
		if (str.length > len) {
			return str.substring(0, len) + "...";
		} else {
			return str;
		}
	}
	
	function reloadPage(msg){
		if(msg!=null && msg!=''){
			$.messager.alert('提示',msg,'info');
		}
		closeCustomEasyWin();
		window.location.reload();
	}
	
	//--分流到部门回调函数
	function distributeCaseCallBack(orgCode,orgName){
		 $('#orgCode').attr('value',orgCode);
		var textValue = formatString(orgName,10);
		$("#result").parent().attr("title", orgName);
		$("#result").text(textValue);
		$("#result").show();
		closeCustomEasyWin();
	}
	//--分流到人回调函数
	function secondShuntCaseCallBack(userIds,userNames){
		$('#distributaryUserIds').attr('value',userIds);
		var textValue = formatString(userNames,10);
		$("#result").parent().attr("title", userNames);
		$("#result").text(textValue);
		$("#result").show();
		closeCustomEasyWin();
	}
	
	function reportCallBack(userIds,userNames){
	    $('#userIds').attr('value',userIds);
		var textValue = formatString(userNames,10);
		$("#result").parent().attr("title", userNames);
		$("#result").text(textValue);
		$("#result").show();
		closeCustomEasyWin();
	}
	
	//--分流到部门
	function distributeCase(){
		var orgCode = $('#orgCode').val();
		if(orgCode==null){
			$.messager.alert('错误','请选择分流对象','error');
			return;
		}
		$.messager.confirm('提示信息','确定分流吗？',function(r){
			if(r){
				var results = $('#results').val();
				if(results == null || results == ''){
					$.messager.alert('错误','请填写处理结果！','error');
					return;
				}
				var eventId = ${event.eventId?c};
				var typeName = '${event.typeName}';
				var taskInfoId = '<#if taskInfo??><#if taskInfo.taskInfoId??>${taskInfo.taskInfoId?c}</#if></#if>';
				var level = '<#if event.level??>${event.level}</#if>';
				var url = "${rc.getContextPath()}/zzgl/event/outPlatform/distributeCase.jhtml?eventId="+eventId + 
					          "&typeName="+typeName + "&taskInfoId="+taskInfoId + "&level=" + level + 
					          "&results="+encodeURIComponent(encodeURIComponent(results)) + "&orgCode="+orgCode;
				showCustomEasyWindow2("发送短信",580,350,10,10,url);
			}
		});
	}
	//--分流到人
	function distributeCase2(){
		var distributaryUserIds = $('#distributaryUserIds').val();
		if(orgCode==null){
			$.messager.alert('错误','请选择分流对象','error');
			return;
		}
		$.messager.confirm('提示信息','确定分流吗？',function(r){
			if(r){
				var results = $('#results').val();
				if(results == null || results == ''){
					$.messager.alert('错误','请填写处理结果！','error');
					return;
				}
				var eventId = ${event.eventId?c};
				var typeName = '${event.typeName}';
				var taskInfoId = '<#if taskInfo??><#if taskInfo.taskInfoId??>${taskInfo.taskInfoId?c}</#if></#if>';
				var level = '<#if event.level??>${event.level}</#if>';
				var url = "${rc.getContextPath()}/zzgl/event/outPlatform/secondShuntCase.jhtml?eventId="+eventId + 
					          "&typeName="+typeName + "&taskInfoId="+taskInfoId + "&level=" + level + 
					          "&results="+encodeURIComponent(encodeURIComponent(results)) + "&userIds="+distributaryUserIds;
				//alert(distributaryUserIds);					          
				showCustomEasyWindow2("发送短信",580,350,10,10,url);
			}
		});
	}

	<!--语音盒-->
	function callPhone(){
		var phoneNum = $.trim($("#registerPhone").text());		
		var reporterName =$.trim($("#registerName").text());
		if (phoneNum==null || phoneNum==""){
			alert("电话不存在！");
			return;
		}else {
			phoneNum = phoneNum.replace(/[ \-]/g,"");//去除电话号码中包含的"-"和空格
			var pattern = /^\d*$/;
			if(phoneNum!="" && pattern.test(phoneNum)){//排除只包含空格和"-"的电话号码
				callSpeech(phoneNum, reporterName, "/theme/scim/gis/query/img/untitled.bmp");
				//window.makeCall(phoneNum, reporterName, null);
			}else{
				alert("电话："+phoneNum+"\n\n电话格式有误！");
				return;
			}
		}
	}
	
	var wingrid="";
	function callSpeech(phoneNum, reporterName, pictureUrl){
			//var url = "${rc.getContextPath()}/zzgl/event/requiredEvent/emp.jhtml?bCall=" + phoneNum + "&userName="
			var url = "${rc.getContextPath()}/voiceInterface/emp/go.jhtml?bCall=" + phoneNum + "&userName="
					+ encodeURIComponent(encodeURIComponent(reporterName)) + "&userImg=" + encodeURIComponent(encodeURIComponent(pictureUrl));
			wingrid = $.ligerDialog.open({ 
				title:"语音盒呼叫",
				url:url,
				height:180,
				width:800,
				showMax:false,
				showToggle:false,
				showMin:false,
				isResize:false,
				slide:false,
				isDrag:true,
				isunmask:true,
				isMax:false,
				isClosed:false,
				name:"speech",
				buttons:null
			});
			return wingrid;
    }
	
	/**
	*图片循环展示
	*imgId：放置图片的控件ID
	*imgInputId：图片提供方ID
	*index：当前图片位置，从0开始
	*size：图片总张数，从0开始计算
	*increase：增量值
	*/
	function changePic(imgId, imgInputId, index, size, increase){
		var index_ = parseInt(index) + parseInt(increase);
		var imgPath = "";
		var total = parseInt(size) + 1;
		if(index_ > size){
			index_ = 0;
		}
		
		if(index_ < 0){
			index_ = size;
		}
		imgPath = $("#"+imgInputId+index_).val();
		$("#"+imgId).attr("src", imgPath);
		$("#"+imgId).attr("name", index_);
		$("#"+imgId).attr("title", "第"+parseInt(index_+1)+"/"+total+"张图片");
	}
	
	//function showCall() {
	//	window.parent.makeCall($.trim($("#registerPhone").text()), $.trim($("#registerName").text()), "");
	//}
</script>
</body>
</html>
