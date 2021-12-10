<#include "/component/ComboBox.ftl" />
<#include "/component/AnoleDate.ftl">
<#include "/component/maxJqueryEasyUIWin.ftl" />

<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="searchForm">
		<div class="fl">
        	<ul>
        		<li>所属网格：</li>
        		<li>
        			<input type="text" id="orgCode" value="${orgCode!''}" style="display:none;"/>
        			<input type="text" id="gridId" value="<#if gridId??>${gridId?c}</#if>" style="display:none;"/>
        			<input type="text" id="gridName" value="${gridName!''}" class="inp1 InpDisable" style="width:150px;" />
        		</li>
        		<!--
              	<li>报送时间：</li>
            	<li>
            		<input id="date1" type="text" class="inp1" style="width: 185px;" value=""/>
					<input type="text" id="startRptDate" name="startRptDate" value="" style="display:none;"/>
					<input type="text" id="endRptDate" name="endRptDate" value="" style="display:none;"/>
				</li> -->
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            	
            </ul>
        </div>
        </form>
	</div>

	<div class="h_10" id="TenLineHeight1"></div>
	<div class="ToolBar">
		<div class="blind"></div><!-- 文字提示 -->
		<div class="tool fr" >
		<!-- 
			<@ffcs.right rightCode="export" parentCode="${system_privilege_action?default('')}">
    			<a id="export" href="#" class="NorToolBtn ExportBtn" onclick="_export();">导出</a>
    		</@ffcs.right>
    		-->
			<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
    			<a id="del" href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
    		</@ffcs.right>
    		<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a id="edit" href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a id="add" href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
		</div>
	</div>
			
</div>

<script type="text/javascript">
	$(function() {
		AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
			if (items && items.length > 0) {
				$("#orgCode").val(items[0].orgCode);
			}
		});
	});
	
	function buttonAccessByStatus(status) {
		$('.NorToolBtn').hide();
		var ary = [];
		if (status == "") {
			ary.push("add");
			ary.push("edit");
			ary.push("del");
		} else if (status == "0") {
			ary.push("add");
			ary.push("edit");
			ary.push("del");
		} else if (status == "1") {
			ary.push("add");
			ary.push("edit");
			ary.push("del");
		} else if (status == "2") {	// 删除
			ary.push("add");
			ary.push("edit");
			ary.push("del");
		}
		ary.push("export");
		$.each(ary, function(i, id) {
			$('#' + id).show();
		});
	}
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/add.jhtml'; 
		showMaxJqueryWindow("新增配置信息", url,600,400);
	}
	
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		var num = rows.length;
		if (num != 1) {
		    $.messager.alert('提示','请先选中数据再执行此操作!','warning');
			return;
		} else {
			var tcId = rows[0].tcId;
			var status = rows[0].status_;
			var url = '${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/edit.jhtml?tcId=' + tcId;
			showMaxJqueryWindow("编辑配置信息", url,600,400);
		}
	}
	
	function del() {
		var rows = $('#list').datagrid('getSelections');
		var num = rows.length;
		if (num != 1) {
		    $.messager.alert('提示','请先选中数据再执行此操作!','warning');
			return;
		} else {
		   var tcId = rows[0].tcId;
		   var url = '${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/del.jhtml?tcId=' + tcId;
		   $.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
				if (r) {
					modleopen();
					$.ajax({
						type: "POST",
						url: url,
						dataType: "json",
						success: function(data){
							if(data.flag == true) {
								$.messager.alert('提示', '删除成功！', 'info');
							}else {
								$.messager.alert('提示', '删除失败！', 'info');
							}
						    
							$("#list").datagrid('load');
							$('#list').datagrid('unselectAll');
							modleclose();
						},
						error:function(data) {
							$.messager.alert('错误','连接超时！','error');
							modleclose();
						}
					});
				}
			});
		}
	}
</script>