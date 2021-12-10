<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	/*图标选中凹陷效果只有在ie9及其以上才有效果*/
	.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
</style>
<div id="jqueryToolbar">
	<form id="eventCaseQueryForm">
		<input type="hidden" id="urgencyDegree" name="urgencyDegree" class="queryParam iconParam" />
		<input type="hidden" id="handleDateFlag" name="handleDateFlag" class="queryParam iconParam" />
		<input type="hidden" id="positionId" name="positionId" class="queryParam iconParam" value="${positionId!''}"/>
		<input type="hidden" id="positionCode" name="positionCode" class="queryParam iconParam" value="${positionCode!''}"/>

		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属区域：</li>
	                <li>
	                	<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value="${infoOrgCode!''}" />
	                	<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
	                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable w150"  value="${gridName!}">
	                </li>
	            	<li>姓名：</li>
	                <li><input name="name" type="text" class="inp1 keyBlank w150 queryParam" style="width:100px;" id="" value="" onkeydown="_onkeydown();" /></li>
	            	<li>身份证号码：</li>
	                <li><input name="idCard" type="text" class="inp1 keyBlank w150 queryParam" id="" value="" onkeydown="_onkeydown();" /></li>
	            	<li>联系方式：</li>
	                <li><input name="tel" type="text" class="inp1 keyBlank w150 queryParam" style="width:100px;" id="" value="" onkeydown="_onkeydown();" /></li>

	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
	        <div class="clear"></div>‍
	        
		</div>
		<div class="h_10 clear"></div>
		<div class="ToolBar" id="toolbarDiv">
	    	<div class="blind"></div><!-- 文字提示 -->
	    	<script type="text/javascript">
				function DivHide() {
					$(".blind").slideUp();//窗帘效果展开
				}
				function DivShow(msg) {
					$(".blind").html(msg);
					$(".blind").slideDown();//窗帘效果展开
					setTimeout("this.DivHide()",800);
				}
			</script>
	        <div id="actionDiv" class="tool fr">
				<@actionCheck></@actionCheck>
<#--				<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
<#--				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>-->
<#--				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
			</div>
	    </div>
	</form>
</div>

<script type="text/javascript">
    var orderByFieldComboBox = null;
	var startInfoOrgCode = '${infoOrgCode!}';
	var orgtreeset;
	$(function(){
		
		AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});

	});

	function add() {
		var url = "${rc.getContextPath()}/zhsq/eventCase/toAdd.jhtml";
		
		openJqueryWindowByParams({
			maxWidth: 800,
			title: "新增案件信息",
			targetUrl: url
		});
	}
	
	function edit() {
		var caseId = "";
		
		$("input[name='caseId']:checked").each(function() {
			caseId = $(this).val();
		});
		
		if(caseId == "") {
			$.messager.alert('警告','请选中要编辑的案件再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/eventCase/toAdd.jhtml?caseId=" + caseId;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 800,
		  		title: "编辑案件信息",
		  		targetUrl: url
		  	});
		}
	}
	
	function del() {
		var caseId = "";
		
		$("input[name='caseId']:checked").each(function() {
			caseId = $(this).val();
		});
		
		if(caseId == "") {
			$.messager.alert('警告','请选中要删除的案件再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的案件吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/eventCase/delEventCase.jhtml',
						data: 'caseId='+caseId,
						dataType:"json",
						success: function(data) {
							if (data.success && data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							}
						},
						error:function(data){
							$.messager.alert('错误','删除案件连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	function detail(caseId, listType) {
		if(caseId) {
			listType = listType || $("#listType").val();
			
			var url = "${rc.getContextPath()}/zhsq/eventCase/toDetail.jhtml?caseId=" + caseId + "&listType=" + listType;
			
			openJqueryWindowByParams({
				maxWidth: 1000,
				title: "查看案件信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选中查看的案件再执行此操作!','warning');
		}
	}
	
	function iconSelect(obj, iconType) {//快速查询相应事件
		$("#iconSelectDiv > a").removeClass("icon_select");
		$(obj).addClass("icon_select");
		
		$("#eventCaseQueryForm .iconParam").each(function() {
			$(this).val("");
		});
				
		switch(iconType) {
			case 1: {
				$("#urgencyDegree").val("02,03,04");
				break;
			}
			case 2: {
				$("#handleDateFlag").val('03');
				break;
			}
		}
		
		searchData();
	}
	
	function resetCondition() {//重置
		$('#eventCaseQueryForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		if($("#iconSelectDiv > a").length) {
			iconSelect($("#iconSelectDiv > a").eq(0));
		}
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventCaseQueryForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		return searchArray;
	}
	
    function doSearch(queryParams, isCurrent){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
	}
	
	function reloadDataForSubPage(msg, isCurrent) {
		try{
			closeMaxJqueryWindow();
		} catch(e) {}
		
		if(msg) {
			DivShow(msg);
		}
		
		searchData(isCurrent);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}

</script>