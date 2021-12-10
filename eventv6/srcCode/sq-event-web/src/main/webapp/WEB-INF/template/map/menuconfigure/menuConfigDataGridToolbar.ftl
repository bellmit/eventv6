<#include "/component/ComboBox.ftl" />
<style type="text/css">
.width65px{width:65px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
                <li>所属网格：</li>
                <li><input id="regionCode" type="hidden" value="<#if !admin>${gridCode!''}</#if>"/><input id="gridId" type="hidden" value="${gridId!''}"/><input id="gridName" name="gridName" type="text" value="<#if !admin>${gridName!''}</#if>" class="inp1 InpDisable" style="width:150px;"/></li>
            	<li>地图类型：</li>
                <li>
                <input type="text" id="pgIdxType" name="pgIdxType" value="" style="display:none;"/>
                <input type="text" id="pgIdxTypeName" name="pgIdxTypeName" value="" class="inp1 InpDisable" style="width:150px;" />
                </li>
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
    	<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
		</script>
        <div class="tool fr">
        <a href="#" class="NorToolBtn AddBtn" style="background-color: #298124;" onclick="copy();">复制</a>
        <a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
        <a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
        <a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			<@ffcs.right rightCode="copy" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" style="background-color: #298124;" onclick="copy();">复制</a>
			</@ffcs.right>
    		<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
    			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			</@ffcs.right>
			<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	var queryValue = "";
	var lotUrl = "${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/listCareRoads.jhtml?lotName=";
	
	$(function(){

		AnoleApi.initListComboBox("pgIdxTypeName", "pgIdxType", "B559", null, null);

		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#regionCode").val(grid.orgCode);
			} 
		}<#if admin>,{
			ShowOptions:{
				EnableToolbar : true
			},
			OnCleared : function(){
				$("#regionCode").val('');
			}
		}</#if>);
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/menuConfig.jhtml?r=${admin?c}';
		showMaxJqueryWindow("新增", url);
	}
	
	function copy() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要复制的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/menuConfig/copy.jhtml?pgIdxCfgId='+idStr;
		showMaxJqueryWindow("复制", url);
	}
	
	function edit() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/menuConfig.jhtml?pgIdxCfgId='+idStr;
		showMaxJqueryWindow("编辑", url);
	}
	
	function del() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/del.jhtml',
					data: 'idStr='+idStr,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '删除成功！', 'info');
						$('#list').datagrid('clearSelections');	//清除掉列表选中记录
						$("#list").datagrid('load');
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	
	function resetCondition() {
		$("#gridName").val("<#if !admin>${gridName!''}</#if>");
		$("#startGridId").val("<#if !admin>${gridId!''}</#if>");
		$("#regionCode").val("<#if !admin>${gridCode!''}</#if>");
		$("#pgIdxType").val("");
		$("#pgIdxTypeName").val("");
		searchData(1);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData(1);
		}
	}
	
</script>