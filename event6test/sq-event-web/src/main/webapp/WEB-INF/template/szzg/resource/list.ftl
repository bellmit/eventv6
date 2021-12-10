<!DOCTYPE html>
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源子类操作</title> 
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</head>
<body class="easyui-layout">
	<div region="center" title="<span class='easui-layout-title'>资源菜单列表</span>" border="false" style="overflow:hidden;">
		<table id="list"></table>
	</div>

<div id="jqueryToolbar">
	<div class="ConSearch">
		<div class="fl">
        	<ul>
                <li>名称：</li>
                <li><input name="name" type="text" class="inp1" id="name" placeholder="资源名称/资源编码" style="color:gray; width:280px;" onfocus="if(this.value=='1'){this.value='';}$(this).attr('style','width:280px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:280px;');}" onkeydown="_onkeydown();" /></li>
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
			<a id="del" href="#" class="NorToolBtn DelBtn" onclick="isCanDelete();">删除</a>
			<a  href="#" class="NorToolBtn ShangBaoBtn" onclick="move();">移动节点</a>
			<a  href="#" class="NorToolBtn linkBtn" onclick="details();">详情</a>
			<a id="edit" href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			<a id="add" href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
        </div>
    </div>
</div>
    
	<script type="text/javascript">
		var id = '${id!0}';
		var selectedIdStr = "",leftWin;
		
		$(function(){
			leftWin = parent.document.getElementById("left").contentWindow;
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
				url:'${rc.getContextPath()}/zhsq/szzg/zgResourceController/findList.json',
				columns:[[
	                {field:'resTypeId',title:'resTypeId',width:10,hidden:true},
	                {field:'resTypeName',title:'资源名称',width:150},
	                {field:'typeCode',title:'资源编码',width:150},
	                {field:'icon',title:'资源图片名称',width:150},
	                {field:'menuName',title:'专题图层名称',width:150},
	                {field:'path',title:'资源路径',width:600,formatter:function(value,row,index){
						var path = leftWin.getPath(row.resTypeId);
						return "<div title='"+path+"' style='overflow:hidden;height:40px;'>"+path+"</div>";
					}}
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,
				singleSelect:true,
				queryParams:{id:id},//默认查询参数
				rownumbers:true,//显示行号的列
				onLoadError: function () {//数据加载异常
					$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
				},
				onLoadSuccess: function(data){//事件标题内容左对齐
					if(data.total == 0) {
						$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					}
				}
			});
			//设置分页控件
		    var p = $('#list').datagrid('getPager');
			$(p).pagination({
				pageSize: 10,//每页显示的记录条数，默认为10
				pageList: [10,20,40,50],//可以设置每页记录条数的列表
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
	    
	    function add(){
	    	var url = '${rc.getContextPath()}/zhsq/szzg/zgResourceController/toAdd.jhtml?pid='+id;
	    	showMaxJqueryWindow("资源菜单新增", url,400,450);
	    }
	    
	    function edit() {
			var rows = $('#list').datagrid('getSelections');
		    if(rows.length==0){
		        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
				return;
		    }
	    	var url = '${rc.getContextPath()}/zhsq/szzg/zgResourceController/toAdd.jhtml?id='+rows[0].resTypeId;
	    	showMaxJqueryWindow("资源菜单编辑", url,400,450);
	    }
	    
		function move() {
			var rows = $('#list').datagrid('getSelections');
		    if(rows.length==0){
		        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
				return;
		    }
	    	var url = '${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=left&id=move_'+rows[0].resTypeId;
	    	showMaxJqueryWindow("资源菜单列表", url,200,350);
	    }
		function showMoveMsg(){
			$.messager.alert('提示',"节点不能移动到子节点或自己节点上");
		}
	    function moveNode(parentId,name) {
			var children = $('#list').datagrid('getSelections')[0].resTypeId;
			if(parentId == children){
				$.messager.alert('提示','不能选择俩个相同节点！','warning');
				return;
			}
			if(confirm("您确定移动到'"+name+"'节点吗？")){
				$.ajax({
				url:'${rc.getContextPath()}/zhsq/szzg/zgResourceController/moveNode.json',
				type:'POST',
				timeout: 300000, 
				async: false,
				data:{id:children,pid:parentId},
				error: function(){   
					$.messager.alert('友情提示','移动节点失败!','warning');
				 }, 
				 success:function(data){
					modleclose();
					DivShow(data.tipMsg);
					treeReload();
					searchData();
				}  
				});
			}
	    }
		
		function details() {
			var rows = $('#list').datagrid('getSelections');
		    if(rows.length==0){
		        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
				return;
		    }
	    	var url = '${rc.getContextPath()}/zhsq/szzg/zgResourceController/detail.jhtml?id='+rows[0].resTypeId;
	    	showMaxJqueryWindow("资源菜单详情", url,300,350);
	    }
	    	    
		function isCanDelete(id) {
			var rows = $('#list').datagrid('getSelections');
		    if(rows.length==0){
		        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
				return;
		    }
			$.ajax({
				url:'${rc.getContextPath()}/zhsq/szzg/zgResourceController/isHaveChildren.json',
				type:'POST',
				timeout: 300000, 
				async: false,
				data:{id:rows[0].resTypeId},
				error: function(){   
					$.messager.alert('友情提示','删除校验失败!','warning');
				 }, 
				 success:function(data){
					if(confirm("您确定删除这项"+(data>0?'及其所有子项':'')+'吗？')){
						del(rows[0].resTypeId);
					}
				}  
			});   			
		}
		
		function del(id) {
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/szzg/zgResourceController/delete.json',
				data: {id:id},
				dataType:"json",
				success: function(data){
					modleclose();
					DivShow(data.tipMsg);
					treeReload();
					searchData();
				},
				error:function(data){
					$.messager.alert('错误','连接超时！','error');
				}
			});
		}
		
		function treeReload(){
			parent.document.getElementById("left").src = "${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=left&id="+id;
		}
		
		function searchData() {
			var a = new Array();
			var name = $("#name").val();
			if(name!=null && name!=""){
				 a["name"]=name;
			}
			a["id"] = id;
			doSearch(a);
		}
		
		function doSearch(queryParams){
			$('#list').datagrid('clearSelections');
			$("#list").datagrid('options').queryParams=queryParams;
			$("#list").datagrid('load');
		}
		
		function resetCondition() {
			$("#name").val("");
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
		function reloadDataForSubPage( result){
			closeMaxJqueryWindow();
			DivShow(result.tipMsg);
			searchData();
			treeReload();
		}
	</script>
</body>
</html>