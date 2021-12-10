<!DOCTYPE html>
<html>
<head>
	<title>12345事件列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/listSet.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
                <div class="fl">
                    <ul>
                        
                            <li>所属区域：</li>
                            <li>
                                <input type="hidden" name="zoneId" class="inp1" id="zoneId" />
                                <input class="inp1" type="text" id="zoneIdCN" name="zoneIdCN" style="width:135px;" />
                            </li>
                        
                        <li>搜索领域：</li>
                        <li>
                        	<input type="hidden" name="searchField" class="inp1" id="searchField" value="1"/>
                        	<input class="inp1" type="text" id="searchFieldCN" name="searchFieldCN" style="width:135px;" />
                        </li>
                        <li>关键词：</li>
                        <li><input type="text" name="keyWord" class="inp1" id="keyWord" value="" style="width:150px;" /></li>

                        <li style="position:relative;">
                            <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                            <div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                                <div class="LeftShadow">
                                    <div class="RightShadow">
                                        <div class="list NorForm" style="position:relative;">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            	<tr>
		                                    		<td>
                                                        <label class="LabName width65px"><span>事件时间：</span></label>
                                                        <input class="inp1 Wdate fl queryParam" type="text" id="startTime" name="startTime" value="${startTime!''}" readonly="readonly"
                                                               style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" ></input>
                                                        <span class="Check_Radio" style="padding:0 5px;">至</span>
                                                        <input class="inp1 Wdate fl queryParam" type="text" id="endTime" name="endTime" value="${endTime!''}" readonly="readonly"
                                                               style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" ></input>
                                                    </td>
		                                    	</tr>
                                                <tr>
                                                    <td><label class="LabName width65px"><span>诉求类型：</span></label>
														<input class="inp1" type="hidden" id="kindId" name="kindId" style="width:135px;" />
														<input class="inp1" type="text" id="kindIdCN" name="kindIdCN" style="width:145px;" />
													</td>
                                                </tr>
                                                <tr>
                                                    <td><label class="LabName width65px"><span>来源：</span></label>
                                                        <input class="inp1" type="hidden" id="comeFrom" name="comeFrom" style="width:135px;" />
                                                        <input class="inp1" type="text" id="comeFromCN" name="comeFromCN" style="width:145px;" />
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div class="BottomShadow"></div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">

		</div>
	</div>

<script type="text/javascript">

	var categorys = ${(categorys)!};

	$(function() {
	    
        AnoleApi.initListComboBox("zoneIdCN", "zoneId", null, null, ["-1"], {
            DataSrc: [
                {"name": "所有区域", "value": "-1"},
                {"name": "南平市直", "value": "34647"},
                {"name": "延平区", "value": "34665"},
                {"name": "邵武市", "value": "34672"},
                {"name": "武夷山市", "value": "34686"},
                {"name": "建瓯市", "value": "34693"},
                {"name": "建阳区", "value": "34742"},
                {"name": "浦城县", "value": "34714"},
                {"name": "光泽县", "value": "34721"},
                {"name": "松溪县", "value": "34728"},
                {"name": "政和县", "value": "34735"},
                {"name": "顺昌县", "value": "34707"}
            ],
            ShowOptions : {
                EnableToolbar : false
            }
        });
       

		AnoleApi.initListComboBox("searchFieldCN", "searchField", null, null, ["1"], {
			DataSrc: [
				{"name":"标题", "value":"1"},
				{"name":"内容", "value":"2"},
				{"name":"标题与内容", "value":"3"},
				{"name":"事件地点", "value":"4"},
				{"name":"诉求人", "value":"5"}],
			ShowOptions : {
	 			EnableToolbar : true
	   		},
	   		OnCleared: function() {
        		$("#searchField").val("1");
        	}
		});
		
        AnoleApi.initListComboBox("kindIdCN", "kindId", null, null, ["-1"], {
            DataSrc: [{"name":"不限", "value":"-1"},
				{"name":"投诉", "value":"1"},
				{"name":"建议", "value":"2"},
				{"name":"咨询", "value":"3"},
				{"name":"求助", "value":"5"}],
            ShowOptions : {
                EnableToolbar : false
            }
        });

        AnoleApi.initListComboBox("comeFromCN", "comeFrom", null, null, ["-1"], {
            DataSrc: [{"name":"不限", "value":"-1"},
				{"name":"网站", "value":"0"},
				{"name":"电话", "value":"1"},
				{"name":"短信", "value":"2"},
				{"name":"邮件", "value":"3"},
				{"name":"传真", "value":"4"},
				{"name":"市长信箱", "value":"5"},
				{"name":"其他", "value":"6"},
				{"name":"录音", "value":"7"},
				{"name":"QQ", "value":"8"},
				{"name":"APP", "value":"9"}],
            ShowOptions : {
                EnableToolbar : false
            }
        });

		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/event/bianminPlatformDataController/listData.jhtml',
			columns: [[
				{field:'callId', title:'诉求编号', align:'center', width:80,
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        var f = '<a href="javascript: detail(\''+ rec.callId+ '\')" title='+ rec.title +' >'+value+'</a>&nbsp;';
                        return f;
                    }
                },
				{field:'catalogId', title:'诉求类别', align:'center', width:60,
                	formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        if(categorys==null||categorys=='')return value;
                        var f = categorys[value];
                        return f;
                    }	
				},
				{field:'kindName', title:'诉求类型', align:'center', width:100},
				{field:'source', title:'来源', align:'center', width:60},
                {field:'title', title:'诉求标题', align:'center', width:220,
                	 formatter:function(value,rec,rowIndex){
                         if(value==null)return "";
                         var f = '<a href="javascript: detail(\''+ rec.callId+ '\')" title='+ rec.title +' >'+value+'</a>&nbsp;';
                         return f;
                     }	
                },
				{field:'createTime', title:'诉求时间', align:'center', width:100},
				{field:'callerName', title:'姓名', align:'center', width:80},
				{field:'callerCellphone', title:'手机号', align:'center', width:80},
                {field:'handlerStatus', title:'诉求状态', align:'center', width:60,
                    formatter:function(value,rec,rowIndex){
                        var f;
                        if(value == '0'){
                            f = '未批转';
                        }else if(value == '1'){
                            f = '已批转';
                        }else if(value == '2'){
                            f = '正在办理';
                        }else if(value == '3'){
                            f = '重新办理';
                        }else if(value == '4'){
                            f = '处理完毕';
                        }else if(value == '5'){
                            f = '过期未处理';
                        }else if(value == '6'){
                            f = '逾期办理';
                        }else if(value == '7'){
                            f = '逾期办理中';
                        }
                        return f;
                    }
                }
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
            queryParams: getQueryParams(),
            onLoadSuccess: function(data) {
                if(data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
                //listSuccess(data); //暂无数据提示
            },
            onLoadError: function() {
                listError();
            },
            onSelect:function(index,rec){
            }
		});
	}
	
	//详情
	function detail(callId) {
		var url = "${rc.getContextPath()}/zhsq/event/bianminPlatformDataController/detail.jhtml?callId=" + callId;
		showMaxJqueryWindow('详情', url, 600, 500);
	}

	function getQueryParams(){
        var a = new Array();
        
        var zoneId = $('#zoneId').val();
        if(zoneId!=null && zoneId!="") a["zoneId"]=zoneId;
        
        var searchField = $('#searchField').val();
        var keyWord = $('#keyWord').val();
        var kindId = $('#kindId').val();
        var comeFrom = $('#comeFrom').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        if(searchField!=null && searchField!="") a["searchField"]=searchField;
        if(keyWord!=null && keyWord!="") a["keyWord"]=keyWord;
        if(kindId!=null && kindId!="") a["kindId"]=kindId;
        if(comeFrom!=null && comeFrom!="") a["comeFrom"]=comeFrom;
        if(startTime!=null && startTime!="") a["startTime"]=startTime;
        if(endTime!=null && endTime!="") a["endTime"]=endTime;
        return a;
    }

    function searchData() {
        doSearch(getQueryParams());
    }

    function doSearch(queryParams){
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
</script>
</body>
</html>