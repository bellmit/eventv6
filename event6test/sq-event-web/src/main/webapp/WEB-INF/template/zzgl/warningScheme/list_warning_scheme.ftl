<!DOCTYPE html>
<html>
<head>
	<title>预警方案列表</title>
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
                        <li>方案名：</li>
                        <li><input type="text" id="schemeName" name="schemeName" class="inp1" value="" style="width:150px;" /></li>
                        <li style="display:none">业务模块：</li>
                        <li style="display:none"><input id="bizType" name="bizType" type="text" class="queryParam hide"/></li>
                        <li style="display:none"><input id="bizTypeName" type="text" class="inp1 InpDisable w150" /></li>
                    </ul>
                   
                </div>
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询" onclick="doSearch()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
		    <div class="blind"></div><!-- 文字提示 -->
			<div class="tool fr">
			<@actionCheck></@actionCheck>
               
			</div>
		</div>
		
	</div>
</body>
<script type="text/javascript">

	$(function() {

		loadList(); //加载列表
		
		//方案应用模块
        AnoleApi.initTreeComboBox("bizTypeName", "bizType", "A001093098", null, null, {
            ChooseType : "1",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        
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
			idField:'schemeId',
			url: '${rc.getContextPath()}/zhsq/warningScheme/listData.jhtml',
			queryParams: {bizType:$("#bizType").val(),schemeName:$("#schemeName").val()},
			columns: [[
			    {field:'schemeId',checkbox:true,width:40,hidden:'true'},
				{field:'schemeName', title:'方案名', align:'center', width:90,
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        var f = '<a href="###" title='+ rec.schemeName +' onclick=detail('+ rec.schemeId+ ')>'+value+'</a>&nbsp;';
                        return f;
                    }
                },
				{field:'createTime', title:'创建时间', align:'center', width:120,formatter:function(value){
                        return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                 }},
				{field:'status', title:'生效状态', align:'center', width:100,formatter:function(value){
                        if(value=='1'){
                            return '生效';
                        }else{
                            return '失效';
                        }
                        
                 }}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
            onLoadSuccess: function(data) {
                listSuccess(data); //暂无数据提示
            },
            onLoadError: function() {
                listError();
            }
		});
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/warningScheme/toAddOrEdit.jhtml';
		showMaxJqueryWindow('方案设定', url, null, null);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一个方案!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/warningScheme/toAddOrEdit.jhtml?schemeId=' + rows[0].schemeId;
			showMaxJqueryWindow('方案编辑', url, null, null);
		}
	}


	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/warningScheme/toDetail.jhtml?schemeId=" + id;
		showMaxJqueryWindow('详情', url, null, null);
	}

	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一个方案!', 'warning');
		} else {
			$.messager.confirm('提示', '确认删除已选方案？删除后方案将无法恢复，请谨慎操作，若是暂时停用方案，请选择方案失效', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/warningScheme/deletaScheme.jhtml',
						data: {
							schemeId: rows[0].schemeId
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == false) {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info');
								doSearch();
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				}
			});
		}
	}
	
	//失效
	function uneffect(){
	
	    var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一个方案!', 'warning');
		} else {
			
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/warningScheme/SchemeEffect.jhtml',
						data: {
							schemeId: rows[0].schemeId,
							status:'0'
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == false) {
								$.messager.alert('错误', '操作失败！', 'error');
							} else {
								$.messager.alert('提示', '方案已失效！', 'info');
								doSearch(true);
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				
		}
	
	}
	
	//生效
	function effect(){
	
	    var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一个方案!', 'warning');
		} else {
			
					modleopen(); //打开遮罩层
					//先判断是否有已经有生效的方案
					
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/warningScheme/findSchemeEffect.jhtml',
						dataType: 'json',
						success: function(data) {
						    
							if (data.result&&data.result==true) {
								$.messager.alert('提示', '系统已存在生效方案，若需调整，请先将生效方案失效后再操作', 'info');
							} else {
							
							    $.ajax({
						            type: 'POST',
            						url: '${rc.getContextPath()}/zhsq/warningScheme/SchemeEffect.jhtml',
	            					data: {
	            						schemeId: rows[0].schemeId,
            							status:'1'
            						},
	            					dataType: 'json',
	            					success: function(data) {
	            						if (data.result == false) {
	            							$.messager.alert('错误', '操作失败！', 'error');
	            						} else {
		            						$.messager.alert('提示', '方案已生效！', 'info');
		            						doSearch(true);
		            					}
		            				},
		            				error: function(data) {
		            					$.messager.alert('错误', '连接超时！', 'error');
		            				},
	
		            			});
								
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				
		}
	
	}
	
	
	function reloadDataForSubPage(msg, isCurrent) {
        try{
            closeMaxJqueryWindow();
        } catch(e) {}
        
        if(msg) {
            DivShow(msg);
        }

        doSearch(isCurrent);
    }
    
    function returnSubPage(){
         try{
            closeMaxJqueryWindow();
        } catch(e) {}
        
    }
    
    function doSearch(isCurrent){
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = {bizType:$("#bizType").val(),schemeName:$("#schemeName").val()};

        if(isCurrent && isCurrent == true) {
            $("#list").datagrid('reload');
        } else {
            $("#list").datagrid('load');
        }
    }
    
    function DivShow(msg) {
        $(".blind").html(msg);
        $(".blind").slideDown();//窗帘效果展开
        setTimeout("this.DivHide()",800);
    }
    
    function DivHide() {
        $(".blind").slideUp();//窗帘效果展开
    }
    
    function resetCondition(){
        $("#schemeName").val('');
        doSearch();
    }
</script>
</html>
