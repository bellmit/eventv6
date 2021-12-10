<!DOCTYPE html>
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地图图层配置</title> 
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/AnoleDragSort.ftl" />
</head>
<body class="easyui-layout">
	<div region="center" title="<span class='easui-layout-title'>地图图层菜单列表</span>" border="false" style="overflow:hidden;">
		<table id="list"></table>
	</div>

<div id="jqueryToolbar">
	<div class="ConSearch">
		<div class="fl">
        	<ul>
                <li>关键字：</li>
                <li><input name="keywords" type="text" class="inp1" id="keywords" value="查询菜单名称、菜单显示名称、菜单编码" style="color:gray; width:280px;" onfocus="if(this.value=='查询菜单名称、菜单显示名称、菜单编码'){this.value='';}$(this).attr('style','width:280px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:280px;');this.value='查询菜单名称、菜单显示名称、菜单编码';}" onkeydown="_onkeydown();" /></li>
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" title="点击搜索" onclick="searchData()">搜索</a></li>
            	<li><a href="#" class="chongzhi" title="重置搜索条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
	</div>
	
	<div class="h_10 clear"></div>
	
	<div class="ToolBar" id="ToolBar">	
        <div class="tool fr">
			<a id="del" href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			<a id="edit" href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			<a id="add" href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			<a id="add" href="#" class="NorToolBtn OrderbyBtn" onclick="sort();">排序</a>
        </div>
    </div>
</div>
    
	<script type="text/javascript">
		var gdcPid = '${gdcPid?c}';
		var selectedIdStr = "";
		$(function(){
			loadDataList();
		});
	
	    function loadDataList(){
	    	$('#list').datagrid({
				nowrap: false,
				striped: true,
				fit: true,
				remoteSort:false,
				fitColumns: true,
				scrollbarSize :0,
				url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/listData.json',
				columns:[[
	                {field:'gdcId',title:'gdcId',width:40,hidden:true},
	                {field:'menuName',title:'图层菜单名称',width:200},
	                {field:'menuCode',title:'图层菜单编码',width:200},
	                {field:'parentMenuName',title:'所属上级菜单',width:200},
	                {field:'status',title:'状态',width:70,
	                	formatter:function(value,rowData,rowIndex){
							if(value=="001"){
								return "启用";
							}else if (value=="003"){
								return "禁用";
							}
						}
	                }/*,
	                {field:'sort',title:'排序',width:40}*/
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,
				singleSelect:true,
				queryParams:{gdcPid:gdcPid},//默认查询参数
				rownumbers:true,//显示行号的列
				onSelect:function(index,rec){
					selectedIdStr=rec.gdcId;
				},
				onDblClickRow:function(index,rec){
					showDetailRow(rec.dictId);
				}
			});
			//设置分页控件
		    var p = $('#list').datagrid('getPager');
			$(p).pagination({
				pageSize: 20,//每页显示的记录条数，默认为10
				pageList: [20,30,40,50],//可以设置每页记录条数的列表
				beforePageText: '第',//页数文本框前显示的汉字
				afterPageText: '页    共 {pages} 页',
				displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
				/*,
				onBeforeRefresh:function(){
					$(this).pagination('loading');
					alert('before refresh');
					$(this).pagination('loaded');
				}*/
			});
	    }
	    
    	//排序
		function sort() {
			DragSortApi.sort($('#list'), {
				vm : { id : "gdcId", title : "menuName", isAllData : true, rmIds : [gdcPid]},
				db : { code : "MENU_SORT" },
				event : {
					saveAfter : function() {
						$("#list").datagrid('load');
						parent.document.getElementById("left").contentWindow.reloadSelectTreeNode();
					}
				}
			});
		}
	    
	    function add(){
	    	var url = '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/toAddGisDataCfg.jhtml?gdcPid='+gdcPid;
	    	showMaxJqueryWindow("地图图层菜单新增", url,800,450);
	    }
	    
	    function edit() {
			var rows = $('#list').datagrid('getSelections');
		    if(rows.length!=1){
		        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
				return;
		    }
		    var id=rows[0].gdcId;
	    	var url = '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/toEditGisDataCfg.jhtml?gdcId='+id;
	    	showMaxJqueryWindow("地图图层菜单编辑", url,800,450);
	    }
	    	    
		function isCanDelete(id) {
			var flag = true;
			
			$.ajax({
				url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/isHaveChildren.json',
				type:'POST',
				timeout: 300000, 
				async: false,
				data:{gdcPid:id},
				error: function(){   
					$.messager.alert('友情提示','字典编码校验失败,详情请查看后台日志!','warning');
				 }, 
				 success:function(data){
				 	if(data.result){
				 		flag = false;
				 	}else {
				 		flag = true;
				 	}
				 }  
			});   
			
			return flag;
		}
		
		function del() {
			if(!isCanDelete(selectedIdStr)) {
				$.messager.alert('友情提示','该节点有子节点无法删除!','warning');
				return;
			}
			if(confirm("您确定删除这项吗？")){
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/delete.json',
					data: 'gdcId='+selectedIdStr,
					dataType:"json",
					success: function(data){
						if(data.result){
							$("#list").datagrid('load');
							parent.document.getElementById("left").contentWindow.reloadSelectTreeNode();
					       	$.messager.alert('提示', '删除成功！', 'info');
					    }else{
					    	$("#list").datagrid('load');
					    	$.messager.alert('提示', '删除失败！', 'info');
					    }
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		}
		
		function searchData() {
			var a = new Array();
			var keywords = $("#keywords").val();
			if(keywords=="查询菜单名称、菜单显示名称、菜单编码") keywords="";
			if(keywords!=null && keywords!="") a["keywords"]=keywords.trim();
			a["gdcPid"] = gdcPid;
			doSearch(a);
		}
		
		function doSearch(queryParams){
			$('#list').datagrid('clearSelections');
			$("#list").datagrid('options').queryParams=queryParams;
			$("#list").datagrid('load');
		}
		
		function resetCondition() {
			$("#keywords").val("查询菜单名称、菜单显示名称、菜单编码");
			searchData();
		}
		
		//回车查询
		function _onkeydown(){
			var keyCode = event.keyCode;
			if(keyCode == 13){
				searchData();
			}
		}
		
		//-- 供子页调用的重新载入数据方法
		function reloadDataForSubPage(reload, result){
			closeMaxJqueryWindow();
			if(result && reload!='') $.messager.alert('提示', result, 'info');
			if(reload){
				$("#list").datagrid('load');
			} 
			parent.document.getElementById("left").contentWindow.reloadSelectTreeNode();
		}
	</script>
</body>
</html>