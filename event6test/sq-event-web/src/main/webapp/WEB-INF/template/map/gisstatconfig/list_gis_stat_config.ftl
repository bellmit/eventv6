<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>框选周边可配置首页</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<style type="text/css">
</style>

</head>
	<body class="easyui-layout">
		<input type="hidden" id="statType" name="statType" value="${statType!''}">
		<input type="hidden" id="regionCode" name="regionCode" value="${regionCode!''}">
	
		<div id="" region="center" border="false">
			<table id="list"></table>
		</div>
	
		<div id="jqueryToolbar">
			<div class="ConSearch">
				<div class="fl">
		        	<ul>
		                <li>所属网格：</li>
		        		<li><input type="hidden" id="gridId" name="gridId" value=""><input name="gridName" id="gridName" type="text" class="inp1 InpDisable" style="width:150px;"/></li>
		                <li>状态：</li>
		                <li>
		                	<select id="status" class="sel1" style="width:80px">
		                		<option value="">--全部--</option>
		                		<option value="1" selected>启用</option>
		                		<option value="2">禁用</option>
		                	</select>
		                </li>
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
		    	<div class="blind"></div>
		    	<script type="text/javascript">
					function HideDiv(){
						$(".blind").slideUp();//窗帘效果展开
					}
					function ShowDiv(msg){
						$(".blind").html(msg);
						$(".blind").slideDown();//窗帘效果展开
						setTimeout("this.HideDiv()",3000);
					}
				</script>	
		        <div class="tool fr">	
					<a href="#" id="forbidden" class="NorToolBtn DelBtn" onclick="del('2');">禁用</a>
					<a href="#" id="use" class="NorToolBtn DelBtn" onclick="del('1');">启用</a>
					<a href="#" id="del" class="NorToolBtn DelBtn" onclick="del('0');">删除</a>
					<a href="#" id="edit" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
					<a href="#" id="add" class="NorToolBtn AddBtn" onclick="add();">新增</a>
		        </div>
		    </div>
		</div>
	
		<script type="text/javascript">
			var statType; 
			var regionCode; 
		
			$(function(){
				statType = $("#statType").val();
				regionCode = $("#regionCode").val();
			
				loadDataList();
				
				AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(regionCode, items) {
					if(items!=undefined && items!=null && items.length>0){
						var grid = items[0];
						$("#regionCode").val(grid.gridCode);
					} 
				});
			});
			
			function loadDataList() {
				$('#list').datagrid({
					width:600,
					height:300,
					nowrap: true,
					rownumbers:true,
					striped: true,
					fit: true,
					idField:'statCfgId',
					fitColumns: true,
					scrollbarSize :0,
					singleSelect: true,
					remoteSort:false,
					url:'${rc.getContextPath()}/zhsq/map/gisStatConfig/listData.json',
					columns:[[  
		                {field:'statCfgId',checkbox:true,width:40,hidden:'true'}, 
		                {field:'gridName',title:'所属网格', align:'left',width:$(this).width() * 0.5, formatter:function(value, rec, index){
							var f = '<a href="###" onclick="showRow('+rec.statCfgId+')"><span title="'+ rec.gridName +'" style="color:blue; text-decoration:underline;">'+value+'</span></a>&nbsp;';
							return f;
						}},
		                {field:'bizType',title:'地图首页类型', align:'center',width:$(this).width() * 0.4, formatter:function(value, rec, index){
							if(value==null) {
		                		value = "";
		                	}
		                	
		                	var f = '<a href="###" onclick="showRow('+rec.statCfgId+')"><span title="'+ rec.bizType +'" style="color:blue; text-decoration:underline;">'+value+'</span></a>&nbsp;';
							return f;
						}},
					    {field:'status',title:'状态', align:'center',width:$(this).width() * 0.1, formatter:function(value, rec, index){
			                    if(value == null){
									value = "";
								}
								
								if(value == "2"){
									var f = '<div style="cursor:pointer">禁用</div>';
									return f;
								} else if (value == "1") {
									var f = '<div style="cursor:pointer">启用</div>';
									return f;
								}
					    	}
					    }				
					]],
					toolbar:'#jqueryToolbar',
					pagination:true,
					pageSize: 20,
					queryParams:{statType:statType,status:1,regionCode:regionCode},
					onClickRow : function(rowIndex, rowData) {
						switchButtonVisiable(rowData.status);
					},
					onLoadSuccess:function(data){
					    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
						if(data.total==0){					
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
			
			function resetCondition(){
				$("#regionCode").val("");
				$("#gridName").val("");
				$("#status option").eq(1).attr("selected",true);
				searchData();
			}
			
			function searchData() {
				var a = new Array();
				a["statType"]=statType;
				var regionCode = $("#regionCode").val();
				if(regionCode!=null && regionCode!="") a["regionCode"]=regionCode;
				var status = $('#status option:selected').val(); 
				if(status!=null && status!="") a["status"]=status;
				doSearch(a);
			}
			
			function doSearch(queryParams){
				$('#list').datagrid('clearSelections');
				$("#list").datagrid('options').queryParams=queryParams;
				$("#list").datagrid('load');
			}
			
			function add() {
				var url = "${rc.getContextPath()}/zhsq/map/gisStatConfig/add.jhtml?statType="+statType;
				showMaxJqueryWindow("新增框选配置信息", url, 750, 400);
			}
		
			function edit() {
				var rows = $('#list').datagrid('getSelections');
			    if(rows.length!=1){
			        $.messager.alert('提示','请选择一条操作执行此操作!','warning');
					return;
			    }
			    var id=rows[0].statCfgId;
				var url = '${rc.getContextPath()}/zhsq/map/gisStatConfig/add.jhtml?statCfgId='+id+'&statType='+statType;
				var title = '';
				if (statType == '0') {
					title = '编辑框选配置信息';
				} else if (statType == '1') {
					title = '编辑周边配置信息';
				}
				showMaxJqueryWindow(title, url, 750, 400);
			}
			
			function del(status) {
				var msg = "删除";
		
				if (status == "2") {
					msg = "禁用";
				} else if (status == "1") {
					msg = "启用";
				}
			
				var row = $('#list').datagrid('getSelected');
		
				if (row == null || row == '') {
					$.messager.alert('提示','请选中要'+msg+'的数据再执行此操作!','warning');
					return;
				}
				var statCfgId = row.statCfgId;
				
				$.messager.confirm('提示', '你确定'+msg+'选中的数据吗？', function(r){
					if (r){
						$.ajax({
							type: "POST",
							url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/delete.jhtml',
							data: 'statCfgId='+statCfgId+'&status='+status,
							dataType:"json",
							success: function(data){
								ShowDiv('成功'+msg+'记录！');
								$("#list").datagrid('load');
							},
							error:function(data){
								$.messager.alert('错误','连接超时！','error');
							}
						});
					}
				});
			}
			
			function showRow(id) {
				var title = '';
			
				if (statType == '0') {
					title = '查看框选配置信息';
				} else if (statType == '1') {
					title = '查看周边配置信息';
				}
				
				var url = '${rc.getContextPath()}/zhsq/map/gisStatConfig/detail.jhtml?statCfgId='+id;
				showMaxJqueryWindow(title, url, 750, 400);
			}
			
			//-- 供子页调用的重新载入数据方法
			function reloadDataForSubPage(result){
				closeMaxJqueryWindow();
				ShowDiv(result);
				searchData();
			}
			
			// 根据选中的街路巷里状态来显示启用与停用按钮
			function switchButtonVisiable(status) {
				if (status == "1") { // 启用
					$("#forbidden").show();
					$("#use").hide();
				} else if (status == "2") { // 停用
					$("#forbidden").hide();
					$("#use").show();
				}
			}
		</script>
	</body>
</html>