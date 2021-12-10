<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件采集</title>
<link href="${rc.getContextPath()}/css/normal.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/css/add_people.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/css/jquery.mCustomScrollbar.css" rel="stylesheet"  type="text/css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/jquery-1.7.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery.form.js" ></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/function.js"></script>
<script>window.jQuery || document.write('<script src="${rc.getContextPath()}/js/minified/jquery-1.11.0.min.js"><\/script>')</script>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		<input type="hidden" id="gridCode" name="gridCode" value="<#if event.gridCode??>${event.gridCode}</#if>">
		<input type="hidden" id="type" name="type" value="<#if event.type??>${event.type}</#if>" />
		<input type="hidden" id="code" name="code" value="<#if event.code??>${event.code}</#if>" />
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		
		<div class="OpenWindow Width800 MaxHeight">
			<div class="MC_Top">
		    	<ul>
		        	<li><label class="LabName">事件分类：</label><input class="inp1 InpDisable" style="width:177px;" type="text" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" /></li>
		            <li><label class="LabName">所属网格：</label><input class="inp1 InpDisable" style="width:177px;" type="text" id="gridName" name="gridName" value="<#if event.gridName??>${event.gridName}</#if>" /></li>
		            <#if event.code??><li class="number">事件编号：<span>${event.code}</span></li></#if>
		        </ul>
		    </div>
		    <div id="detailInfo" border="false"><!--事件详情-->
		    </div>
		    	
		    
			<div class="BigTool">
	            <#if (isReport?? && isReport)>
		            <div class="BtnList" style="width:210px;">
						<a href="###" onclick="cancel();" class="BigNorToolBtn GreenBtn"><img src="${rc.getContextPath()}/images/cancel.png" />取消</a>
						<!--
				    	<a href="###"><img src="${rc.getContextPath()}/images/sys1_25.png" />越级上报</a>
				    	-->
				    	<a href="###" onclick="tableSubmit('saveEventAndReport');" class="BigNorToolBtn BlueBtn"><img src="${rc.getContextPath()}/images/submit.png" />提交</a>
		            </div>
		        <#else>
		        	<div class="BtnList" style="width:410px;">
						<a href="###" onclick="cancel();" class="BigNorToolBtn GreenBtn"><img src="${rc.getContextPath()}/images/cancel.png" />取消</a>
						<!--
				    	<a href="###"><img src="${rc.getContextPath()}/images/sys1_25.png" />越级上报</a>
				    	-->
				       	<a href="###" onclick="closed();" class="BigNorToolBtn BlueBtn"><img src="${rc.getContextPath()}/images/finish2.png" />结案</a>
				        <a href="###" onclick="tableSubmit('saveEvent',parent.startWorkFlow);" class="BigNorToolBtn BlueBtn"><img src="${rc.getContextPath()}/images/submit.png" />提交</a>
				    	<a href="###" onclick="tableSubmit('saveEvent');" class="BigNorToolBtn BlueBtn"><img src="${rc.getContextPath()}/images/sys1_25.png" />保存</a>
	            	</div>
	            </#if>
	        </div>	
		</div>
	</form>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">
	var type = "${event.type}";

	$(function(){
		var typeVal = $("#type").val();
		var eventId = $("#eventId").val();
		var urls="${rc.getContextPath()}/zhsq/event/eventDisposalController/selectEventType.jhtml?type="+typeVal+"&operator=create";
		if(eventId!=undefined && eventId!=null && eventId!=""){
			urls="${rc.getContextPath()}/zhsq/event/eventDisposalController/selectEventType.jhtml?type="+typeVal+"&operator=edit&eventId="+eventId;
		}
		$("#detailInfo").panel({
			height:'auto',
			overflow:'no',
			href: urls
		});
		
		//AnoleApi.initEventTypeComboBox("typeName", "type");
		AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, { 'OnChanged' : onTreeComboBoxChanged });
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(items != undefined && items != null && items.length > 0) {
				var grid = items[0];
				$("#gridCode").val(grid.orgCode);
			}
		});
		<@block name="patrolTypeInputScript"></@block>
	});
	
	function involedPeopleCallback(users) {
		if(users == ""){
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
			return;
		}
		var usersDiv = "";
		var userNames = "";
		var userArray = users.split("；");
		if(userArray != ""){
			$.each(userArray, function(i, n){
				var items = n.split("，");
				if(typeof(items[1])!="undefined" ){
					var userName = items[1];
					if(userName.length > 3){//名字显示前三个字
						userName = userName.substr(0, 3);
					}
					usersDiv += "<p title="+items[1]+"("+items[2]+")>"+userName+"<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople(\""+items[1]+"\",\""+items[2]+"\", $(this).parent());'/></p>";
					userNames += items[1] + "，";
				}
			});
			
			userNames = userNames.substr(0, userNames.length - 1);
			$("#involvedPeopleName").html(usersDiv);//用于页面显示
			$("#eventInvolvedPeople").val(users);//用于后台保存
			$("#involvedPersion").val(userNames);
		}else{
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
		}
	}
	
	function tableSubmit(m, callback, toClose){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			var msg = "添加";
			<#if event.eventId??>
				m = "editEvent";
				msg = "更新";
			</#if>
			<#if (isReport?? && isReport)>
				msg = "上报";
			</#if>
			if(toClose==undefined || toClose==null){
				toClose = "";
			}
			
			if(callback!=undefined && callback!=null && callback!=""){
				m += "AndStart";
			}
			
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);
	      	
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		if(data.eventId){
		  			if(callback!=undefined && callback!=null && callback!=""){
		  				//$.messager.alert('提示',data.result,'info');
		  				callback(data);
		  			}else{
		  				
		  				if(data.result){
		  					msg += "成功";
		  				}else{
		  					msg += "失败";
		  				}
		  				parent.reloadDataForSubPage(msg,data.type);
		  			}
				}else{
					modleclose();
					$.messager.alert('错误','连接超时，请重试！', 'error');
				}
			});
	  	}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function closed(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			modleopen();
			var eventId = $("#eventId").val();
			if(eventId!=undefined && eventId!=null && eventId!=""){
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
					data: 'eventId='+ eventId +'&toClose=1',
					dataType:"json",
					success: function(data){
						if(parent.startWorkFlow != undefined){
				   			parent.startWorkFlow(data);
				   		}else{
				   			$.messager.alert('错误','连接错误！','error');
				   		}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
			}else{
				tableSubmit('saveEvent',parent.startWorkFlow, "1");
			}
		}
	}
	
	function showMap() {
		var mapt = $("#mapt").val();
		var x = $("#x").val();
		var y = $("#y").val();
		var data;
		if(x!=''&&y!=''&&mapt!=''){
			data = 'x='+x+'&y='+y+'&mapt='+mapt;
		}else{
			data = 'mapt='+mapt+"&gridId="+$("#gridId").val();
		}
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfAnchorPoint.jhtml?' + data;
		showMaxJqueryWindow("地图标记", url);
	}
	
	function mapMarkerSelectorCallback(mapt,x,y){
		$("#mapt").val(mapt);
		$("#x").val(x);
		$("#y").val(y);
		$("#mapTab").html("已标注");
		$("#mapTab").addClass("local");
		closeMaxJqueryWindow();
	}
	
	function onTreeComboBoxChanged(val){
		this.type = val;
		var eventId = $("#eventId").val();
		var urls="${rc.getContextPath()}/zhsq/event/eventDisposalController/selectEventType.jhtml?type="+val+"&operator=create";
		if(eventId!=undefined && eventId!=null && eventId!=""){
			urls="${rc.getContextPath()}/zhsq/event/eventDisposalController/selectEventType.jhtml?type="+val+"&operator=edit&eventId="+eventId;
		}
		$("#detailInfo").panel({
			height:'auto',
			overflow:'no',
			href: urls
		});
	}
</script>
</html>