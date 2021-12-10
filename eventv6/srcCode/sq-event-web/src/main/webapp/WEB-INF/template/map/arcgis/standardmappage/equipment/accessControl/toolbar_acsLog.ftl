<style type="text/css">
	.w150{width:150px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="acsLogForm">
			<input id="bizId" name="bizId" type="hidden" class="queryParam" value="${bizId!}" />
			
	        <div class="fl">
	        	<ul>
	        		<li>住户名：</li>
	                <li><input id="rsName" name="rsName" type="text" class="inp1 w150 queryParam" onkeydown="_onkeydown();" /></li>
	                <li>房号：</li>
	                <li><input id="roomNo" name="roomNo" type="text" class="inp1 w150 queryParam" onkeydown="_onkeydown();" /></li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
</div>

<script type="text/javascript">
	function searchData() {
		var searchArray = new Array(),
			queryVal = "";
		
		$("#acsLogForm .queryParam").each(function() {
			queryVal = $(this).val();
			
			if(queryVal) {
				searchArray[$(this).attr("name")] = queryVal;
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
		$('#acsLogForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>