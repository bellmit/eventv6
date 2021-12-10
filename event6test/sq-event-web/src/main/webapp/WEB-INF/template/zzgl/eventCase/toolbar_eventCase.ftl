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
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
		<input type="hidden" id="urgencyDegree" name="urgencyDegree" class="queryParam iconParam" />
		<input type="hidden" id="handleDateFlag" name="handleDateFlag" class="queryParam iconParam" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属网格：</li>
	                <li>
	                	<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
	                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable w150" />
	                </li>
	            	<li>案件分类：</li>
	                <li>
	                	<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable w150" />
	                </li>
	            	<li>关键字：</li>
	                <li><input name="keyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="案件描述/标题/案发详址" defaultValue="案件描述/标题/案发详址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>排序字段：</span></label>
	                                    			<input type="text" id="orderByField" name="orderByField" class="queryParam hide" />
	                                    			<input type="text" id="orderByFieldInput" class="hide" />
	                                    			<input class="inp1" type="text" id="orderByFieldName" style="width:135px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr id="orderByFieldRadioTr" class="hide">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>排序方式：</span></label>
	                                    			<div class="Check_Radio" style="margin-right: 40px;"><input type="radio" name="orderByRadio" id="orderByRadioAsc" value="ASC" checked onclick="radionCheck('ASC')"></input><label for="orderByRadioAsc" style="cursor: pointer;">升序</label></div>
	                                    			<div class="Check_Radio"><input type="radio" name="orderByRadio" id="orderByRadioDesc" value="DESC" onclick="radionCheck('DESC')"></input><label for="orderByRadioDesc" style="cursor: pointer;">降序</label></div>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>案件编号：</span></label><input class="inp1 queryParam" type="text" id="code" name="code" style="width:135px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>案发时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>采集时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="createTimeStart" name="createTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="createTimeEnd" name="createTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
	                                    	</tr>
	                                    </table>
	                                </div>
	                                <div class="BottomShadow"></div>
	                            </div>
	                        </div>
	                    </div>
	            	</li>
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
	        <div id="actionDiv" class="tool fr"><@actionCheck></@actionCheck></div>
	        <div id="iconSelectDiv" class="fl">
	        	<a href="###" class="icon_select" onclick="iconSelect(this);"><i class="ToolBarAll"></i>所有</a>
	        	<a href="###" onclick="iconSelect(this, 1);"><i class="ToolBarUrgency"></i>紧急</a>
	        	<a href="###" onclick="iconSelect(this, 2);"><i class="ToolBarOverDue"></i>超时</a>
	        </div>
	    </div>
	</form>
</div>

<script type="text/javascript">
    var orderByFieldComboBox = null;

	$(function(){
		AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, { 
			ChooseType : "1",
			ShowOptions:{
        		EnableToolbar : true
        	}
		});
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
        
        orderByFieldComboBox = AnoleApi.initListComboBox("orderByFieldName", "orderByFieldInput", null, function(value) {
        		$("#orderByFieldRadioTr").show();
        		
        		radionCheck();
        	}, null, {
        	DataSrc : [{"name":"采集时间", "value":"T1.CREATE_TIME"},{"name":"案发时间", "value":"T1.HAPPEN_TIME"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	},
        	OnCleared: function() {
        		$("#orderByField").val("");
        		$("#orderByFieldRadioTr").hide();
        	}
        });
	});
	
    function radionCheck(orderType) {
    	var orderField = $("#orderByFieldInput").val();
    	
    	if(isBlankStringTrim(orderType)) {
    		orderType = $('#orderByFieldRadioTr input[name="orderByRadio"]:checked').eq(0).val();
    	}
    	
    	$("#orderByField").val(orderField + " " + orderType);
    }
    
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
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
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
	
	function _onfocus(obj) {
		if($(obj).hasClass("keyBlank")){
			$(obj).val("");
			$(obj).removeClass('keyBlank')
		}
	}
	
	function _onblur(obj) {
		var keyWord = $(obj).val();
		
		if(keyWord == ''){
			$(obj).addClass('keyBlank');
			$(obj).val($(obj).attr("defaultValue"));
		}
	}
</script>