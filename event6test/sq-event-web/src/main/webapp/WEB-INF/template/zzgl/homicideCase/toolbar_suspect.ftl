<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="homicideForm">
			<input type="hidden" id="bizType" name="bizType" class="queryParam" value="${bizType!}" />
			<input type="hidden" id="hashBizId" name="hashBizId" class="queryParam" value="<#if bizId??>${bizId}</#if>" />
			
	        <div class="fl">
	        	<input type="text" class="hide" /><!--防止回车后变成提交操作-->
	        	<ul>
	        		<li>姓名：</li>
	                <li><input type="text" id="name" name="name" class="inp1 queryParam" style="width:150px;" value="" onkeydown="_onkeydown();" /></li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="###" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
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
        	<#if isNotDetail?? && isNotDetail>
	    		<a href="###" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="###" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="###" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</#if>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	<#if isNotDetail?? && isNotDetail>
	function add() {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toAddSuspect.jhtml?bizType='+$("#bizType").val()+ '&hashBizId='+ $("#hashBizId").val();
		showMaxJqueryWindow("新增人员信息", url, fetchWinWidth(), undefined);
	}
	
	function edit() {
		var hashId = "";
		$("input[name='hashId']:checked").each(function() {
			hashId = $(this).val();
		});
		if(hashId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toAddSuspect.jhtml?bizType='+ $("#bizType").val()+ '&hashBizId='+ $("#hashBizId").val() +'&hashId='+ hashId;
			showMaxJqueryWindow("编辑人员信息", url, fetchWinWidth(), undefined);
		}
	}
	
	function del(){
		var hashId = "";
		$("input[name='hashId']:checked").each(function() {
			hashId = $(this).val();
		});
		if(hashId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的人员信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/delSuspect.jhtml',
						data: 'ipId='+hashId,
						dataType:"json",
						success: function(data) {
							if (data.result) {
								var msg = '人员信息删除成功！';
								modleclose();
								
								reloadDataForSubPage(msg);
							}
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}
	}
	</#if>
	
	function detail(ipId) {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toDetailSuspect.jhtml?ipId='+ipId;
		showMaxJqueryWindow("查看人员信息", url, fetchWinWidth(), undefined);
	}
	
	function searchData() {
		var searchArray = new Array();
		
		$(".queryParam").each(function() {
			var val = $(this).val();
			var key = $(this).attr("name");
			
			if(isNotBlankString(val)){
				searchArray[key] = val;
			}
		});
		
		doSearch(searchArray);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$('#homicideForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>