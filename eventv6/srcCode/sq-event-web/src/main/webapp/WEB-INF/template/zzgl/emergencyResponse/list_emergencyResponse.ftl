<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>紧急预案</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${rc.getContextPath()}/css/emergency_response.css" rel="stylesheet" type="text/css" />

</head>
<body class="easyui-layout">
<div id="emergencyResponseToolbar" class="MainContent hide">
	<#include "/zzgl/emergencyResponse/toolbar_emergencyResponse.ftl" />
</div>      

<div id="centerContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table style="width:100%" id="list"></table>
</div>
	
<div id="hidePasswordWinodw" class="hide">
    <div class="NorWindow">
        <div class="title">启动预案</div>
        <div class="con">
        	<div class="password">
            	<p>应急预案启动密码</p>
                <p><input name="jjyapassword" type="password" class="inp3" id="jjyapassword" onkeydown="enterPress()"/></p>
                <p id="passwordBtn"><input name="" type="button" value="启动" class="btn1" onclick="checkPassword()"/></p>
                <p id="passwordCheck" class="hide"><img src="${rc.getContextPath()}/images/emergencyResponse/loading.gif" style="margin-right:10px;" />密码校验中，请稍后</p>
                <p id="passwordMsg" class="hide"><code class="FontRed">密码输入错误！</code></p>
            </div>
        </div>
    </div>
</div>

<div id="hideRespondWinodw" class="hide">
	<div id="ms" class="mask"></div>
    <div class="NorWindow">
        <div class="title"><a href="#" class="fr" onclick="hideWindow()"></a>启动预案</div>
        <div class="con">
            <div class="yjyaBtns">
                <ul>
                    <li><a href="javascript:tojjya(1)"><img src="${rc.getContextPath()}/images/emergencyResponse/yjya_26.png" /><br /><font color="#ff0000">一级响应</font></a></li>
                    <li><a href="javascript:tojjya(2)"><img src="${rc.getContextPath()}/images/emergencyResponse/yjya_28.png" /><br /><font color="#ff7e00">二级响应</font></a></li>
                    <li><a href="javascript:tojjya(3)"><img src="${rc.getContextPath()}/images/emergencyResponse/yjya_30.png" /><br /><font color="#f9cd00">三级响应</font></a></li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
	var startGridId = "${startGridId?c}";
	var typesForList = "<#if typesForList??>${typesForList}</#if>";
	
	$(function(){
		$("#hidePasswordWinodw").show();
	});

	function loadDataList(){
		$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//
				{field:'type',title:'type', align:'center',hidden:true},//
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
					formatter: function(value,rec,rowIndex){
						if(value==null){
                			value="";
                		}
                		if(value.length>=20){
							value = value.substring(0,20);
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>';
						return f;
					},
					styler: function(value,rec,index){
						return "text-align:left";
                    }
				},
				{field:'happenTimeStr',title:'发生时间', align:'center',width:fixWidth(0.2),sortable:true},
                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
                {field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{gridId:startGridId,type:$("#type").val(),urgencyDegrees:["04"],eventType:"all", eventStatus:"01,02", typesForList:typesForList},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			}
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
	}

	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
		if(!eventId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&cachenum=" + Math.random();
	    	showMaxJqueryWindow("查看事件信息", url, 900, 400);
		}
	}
	
	function flashData(){//详情办理后回调方法
		closeMaxJqueryWindow();
		searchData();
	}
	
	function tojjya(level){
		var rows =$('#list').datagrid('getSelections');
		var eventId = rows[0].eventId;
		var type = rows[0].type;
		var url = '${rc.getContextPath()}/zhsq/emergencyResponse/toEmergencyResponseMsg.jhtml?important='+level+'&eventId='+eventId+'&type='+type;
		hideWindow();
		showMaxJqueryWindow("应急响应", url);
	}
	
	function hideWindow(){
		$("#hidePasswordWinodw").hide();
		$("#hideRespondWinodw").hide();
	}
	
	function enterPress(){ 
		if(event.keyCode == 13){ 
			checkPassword();
		} 
	} 
	
	function checkPassword(){
		$("#passwordBtn").hide();
		$("#passwordMsg").hide();
		$("#passwordCheck").show();
		
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/emergencyResponse/checkPassword.jhtml',
			data: 'password=' + $("#jjyapassword").val(),
			dataType:"json",
			success: function(data){
				if(data){
					$("#hidePasswordWinodw").hide();
					$("#emergencyResponseToolbar").show();
					loadDataList();
				}else{
					$("#jjyapassword").val("");//清空错误的密码
					$("#jjyapassword").focus();
					$("#passwordCheck").hide();
					$("#passwordBtn").show();
					$("#passwordMsg").show();
				}
				
			},
			error:function(data){
				$.messager.alert('错误','连接错误！','error');
			}
    	});
	}

</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />//语音盒呼叫使用

</body>
</html>