<#include "/component/ComboBox.ftl" />
<style type="text/css">
.width65px{width:65px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
                <li>所属网格：</li>
                <li><input id="orgCode" type="hidden" value="${orgCode}"/>
                <input value="${orgName}" id="orgName" type="text" class="inp1 InpDisable" style="width:100px;"/></li>
                <li>年份:</li>
                <li>
                <input type="text" class="inp1 Wdate timeClass" id="syear"  value="${currentYear}"
					onClick="WdatePicker({readOnly:true,isShowOK:false, isShowToday:false,dateFmt:'yyyy'});"  style='width:100px'>
				</li>
            	<li>类型：</li>
                <li><input id="stype" type="hidden" />
                <input name="stypeStr" id="stypeStr" type="text" class="inp1"  style='width:120px'/></li>
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
        <div class="tool fr">
    			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	
	$(function(){
		AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
				if (items && items.length > 0) {
					document.getElementById('orgCode').value=items[0].orgCode;
				}
			}, { 
				ChooseType : '1',
				isShowPoorIcon: "0",
				ShowOptions : {EnableToolbar : false}
			});
		
       AnoleApi.initTreeComboBox("stypeStr", 'stype','${pcode}',function(gridId, items){
				if(isNotBlankParam(items) && items.length>0){
					document.getElementById('stype').value=items[0].dictCode;
				} 
			},null,{ChooseType:'1',ShowOptions : {EnableToolbar : true},
				OnCleared:function(){
					document.getElementById('stype').value='';
				}});

	});
	
	function add() {
		var opt = {
	    	title: "新增${pName}类型统计",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/toAdd.jhtml?pcode=${pcode}',
	    	width: 400,
	    	maxHeight: 300
	    };
		openJqueryWindowByParams(opt);
	}
	function detail(id) {
		var opt = {
	    	title: "${pName}类型统计详情",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/detail.jhtml?id='+id,
	    	width: 400,
	    	maxHeight: 300
	    };
		openJqueryWindowByParams(opt);
	}
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if(rows.length == 0) {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		var opt = {
	    	title: "编辑${pName}类型统计",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/toAdd.jhtml?pcode=${pcode}&id='+rows[0].seqId,
	    	width: 400,
	    	maxHeight: 300
	    };
		openJqueryWindowByParams(opt);
	}
	
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if(rows.length == 0) {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/delete.jhtml',
					data: {'id':rows[0].seqId},
					dataType:"json",
					success: function(data){
						modleclose();
						DivShow(data.tipMsg);
						searchData();
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	function searchData() {
		var params ={pcode:'${pcode}'};
		var syear = document.getElementById('syear').value ;
		if(syear.length>0){
			params['syear']= syear;
		}
		params['orgCode']= document.getElementById('orgCode').value;
		
		var stype = document.getElementById('stype').value ;
		if(stype.length > 0){
			params['stype']= stype;
		}
		list.datagrid('clearSelections');
		list.datagrid('options').queryParams=params;
		list.datagrid('load');
	}
	function resetCondition() {
		document.getElementById('stype').value = '';
		document.getElementById('stypeStr').value = '';
		document.getElementById('syear').value = '';
		document.getElementById('orgCode').value = '${orgCode}';
		document.getElementById('orgName').value = '${orgName}';
	}
	

	
</script>