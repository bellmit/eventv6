<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>联防长列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/jointDefence/toolbar_jointDefence.ftl" />
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
    $(function(){
    	loadDataList();
    });
	
    function loadDataList(){
		var queryParams = queryData();
		
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'caseId',
			url:'${rc.getContextPath()}/zhsq/jointDefence/listData.json',
			columns:[[
				{field:'caseId',checkbox:true,width:40,hidden:'true'},
				{field:'PARTY_NAME',title:'姓名', align:'left',width:fixWidth(0.12),sortable:true,
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        var f = '<a href="###" title='+ rec.PARTY_NAME +' onclick=show("'+ rec.IDENTITY_CARD+ '","'+rec.USER_ID+'","'+rec.USER_REGION_CODE+'")>'+value+'</a>&nbsp;';
                        return f;
                    }
                },
				{field:'ORG_ENTITY_PATH',title:'所属联防组', align:'center',width:fixWidth(0.4),sortable:true},
                {field:'I_GENDER',title:'性别', align:'center',width:fixWidth(0.1),sortable:true,
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        if(value=='M') return "男";
                        if(value=='F') return "女";
                        return "";
                    }
                },
				{field:'VERIFY_MOBILE',title:'联系方式', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'IDENTITY_CARD',title:'身份证号码', align:'center',width:fixWidth(0.16),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec){
				detail(rec.caseId);
			},
			onLoadError: function () {//数据加载异常
        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
    		},
			onLoadSuccess: function(data){//案件标题内容左对齐
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				} else {
					var panel = $(this).datagrid('getPanel'),
						tr = panel.find('.datagrid-view2 tr.datagrid-row'),//获取展示的列表行
		            	trLen = 0,
		            	trHeight = 0,
		            	dataViewHeight = 0,
		            	trIndexMax = 0;
		            
		            if(tr) {
		            	trLen = tr.length;
		            	trHeight = tr.eq(0).height();
		            } 
		            
	             	dataViewHeight = $('.datagrid-view').height();//获取列表的高度(包括列表标题)
	             	trIndexMax = parseInt(dataViewHeight / trHeight, 10) - 1;//扣除一行，表行索引从0开始
	             	
		            tr.each(function(i){
		            	var operateNotice = $(this).find('.OperateNotice');
		            	
		                $(this).mouseover(function() {  
		                	var trOffset = $(this).offset(),
		                		scrollTop = $(".datagrid-body").scrollTop(),
		                		arrowHeight = $(this).find('.arrow').eq(0).outerHeight(true);
		                	
		                	if(i==(trLen - 1) && i != 0 && (scrollTop > 0 || trIndexMax == i)){//有滚动条或者无滚动条时能展示的最大行数的最后一行
		                		$(this).find('.arrow').css({'top': '28px', 'background': 'url(${rc.getContextPath()}/images/arrow2.png)'});
		                		
		                		operateNotice.css('top', trHeight * i - scrollTop + arrowHeight + 2 + "px");//operateNotice距离list的高度，增添2个像素是为了更贴合行
		                	} else {
		                		operateNotice.css('top', trHeight * (i + 2) - scrollTop - 2 + "px");//operateNotice距离list的高度，扣除2个像素是为了更贴合行底
		                	}
		                	
		                	operateNotice.css('left', trOffset.left + "px").show();
		                }); 
		                
		                $(this).mouseout(function(){  
		                	operateNotice.hide();
		                }); 
		            }); 
				}
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

    function show(id, userId, userRegionCode){
        var url = "${rc.getContextPath()}/zhsq/jointDefence/toDetail.jhtml?id=" + id + "&userId=" + userId+"&userRegionCode="+userRegionCode;
        openJqueryWindowByParams({
            title: "查看信息",
            targetUrl: url
        });
	}


	function clickFormatter(value, rec, rowIndex) {
		var title = "",
			caseName = rec.caseName,
			listType = $("#listType").val(),
			urgencyDegreeTitle = "";
		
		if(caseName) {
			if(listType == 4) {//我发起的
				title = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="cuiban" onclick="urgeEventCase(' + rec.caseId + ', 0)">催办</li></ul><div class="arrow"></div></div></div>';
			} else if(listType == 5 && rec.status != '04') {//辖区所有
				title = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="urgeEventCase(' + rec.caseId + ', 1)">督办</li></ul><div class="arrow"></div></div></div>';
			}
			
			if(rec.urgencyDegree && rec.urgencyDegree != '01') {
				urgencyDegreeTitle = '<i title="'+ rec.urgencyDegreeName +'" class="ToolBarUrgencyCase" style="margin: -5px 5px 0 0; background-size: 18px; width: 18px; height: 18px;"></i>';
			}
				
			if(listType == 2) {//待办
				var remindTitle = "";
				
				if(rec.remindMark == '1') {
					remindTitle = '<i title="催办" class="ToolBarRemind" style="margin: 0 5px 0 0;"></i>';
				}
				
				caseName = remindTitle + caseName;
			}
			
			if(listType == 2 || listType == 5) {
				if(rec.superviseMark == '1') {
					caseName = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 5px 0 0; width:28px; height:28px;">' + caseName;
				}
			}
			
			title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.caseId + '\')">'+ urgencyDegreeTitle + caseName +'</a>';
		}
		
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		
		return value;
	}
	
	function urgeEventCase(caseId, operateType) {
		var url = "${rc.getContextPath()}/zhsq/eventCase/toAddUrgeOrRemind.jhtml?caseId=" + caseId + "&operateType=" + operateType,
			title = "催办信息";
		
		if(operateType == 1) {
			title = "督办信息";
		}
		
		openJqueryWindowByParams({
			maxWidth: 480,
			maxHeight: 180,
			title: title,
			targetUrl: url
		});
	}
	
	function urgeEventCaseCallback(data) {
		if(data.result && data.result == true) {
			var msg = "催办成功！";
			
			if(data.operateType == 1) {
				msg = "督办成功！";
			}
			
			reloadDataForSubPage(msg, true);
		}
	}
	
	function BaseWorkflow4UrgeHandle_initParam() {//督办、催办初始化方法
		var initObj = {
			addUrge: {
	    		addUrgeUrl: '${rc.getContextPath()}/zhsq/eventCase/addUrgeOrRemind.jhtml',
	    		addUrgeCallback: urgeEventCaseCallback
	    	}
    	};
    	
    	return initObj;
	}
</script>
</body>
</html>