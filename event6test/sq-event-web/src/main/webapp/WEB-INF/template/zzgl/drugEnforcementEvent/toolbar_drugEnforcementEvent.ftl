<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width: 150px;}
</style>
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="drugEnforcementEventForm">
			<input type="hidden" class="queryParam" id="listType" name="listType" value="${listType!}" />
			<input type="hidden" class="queryParam" name="createDateStart" value="${createDateStart!}" />
			<input type="hidden" class="queryParam" name="createDateEnd" value="${createDateEnd!}" />
			
	        <div class="fl">
	        	<ul>
	        		<li>场所：</li>
	                <li>
	                	<input type="text" class="hide queryParam" id="infoOrgCode" name="infoOrgCode" value="${infoOrgCode!}" />
	                	<input type="text" class="hide" id="gridId" value="<#if startGridId??>${startGridId?c}</#if>" />
	                	<input type="text" class="inp1 InpDisable w150 queryParam" id="gridName" value="${startGridName!}" />
	                </li>
	                <li>姓名：</li>
	                <li><input type="text" id="addictName" name="addictName" class="inp1 w150 queryParam" onkeydown="_onkeydown();" /></li>
	                <li>公民身份号码：</li>
	                <li><input type="text" id="addictIdCard" name="addictIdCard" class="inp1 w150 queryParam" onkeydown="_onkeydown();" /></li>
	                
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:356px; top: 42px; left: -130px;">
	            			<div class="LeftShadow">
	                        	<div class="RightShadow">
	                            	<div class="list NorForm" style="position:relative;">
	                            		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	                            			<tr id="statusTr" class="hide">
	                            				<td>
	                            					<label class="LabName width65px"><span>当前状态：</span></label>
	                            					<input type="text" id="status" name="status" class="hide queryParam" value="${status!}" />
	                            					<input type="text" id="statusName" class="inp1" style="width:110px; *width:100px;" />
	                            				</td>
	                            			</tr>
	                            			<tr id="handleStatusTr" class="hide">
	                            				<td>
	                            					<label class="LabName width65px"><span>是否超时：</span></label>
	                            					<input type="text" id="handleStatus" name="handleStatus" class="hide queryParam" value="${handleStatus!}" />
	                            					<input type="text" id="handleStatusName" class="inp1" style="width:110px; *width:100px;" />
	                            				</td>
	                            			</tr>
	                            			<tr>
	                            				<td>
	                            					<label class="LabName width65px"><span>联系人员：</span></label><input type="text" id="contactUser" name="contactUser" class="inp1 queryParam" style="width:247px; *width:227px;" onkeydown="_onkeydown();" />
	                            				</td>
	                            			</tr>
	                            			<tr>
	                            				<td>
	                            					<label class="LabName width65px"><span>上报时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="reportDateStart" name="reportDateStart" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="reportDateEnd" name="reportDateEnd" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
	                            				</td>
	                            			</tr>
	                            		</table>
	                                </div>
	                            </div>
	                        </div>
	            		</div>
	            	</li>
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
        <div id="toolBtnDiv" class="tool fr">
        	<@actionCheck></@actionCheck>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	$(function() {
		var statusArray = [], listType = $("#listType").val(), 
			status = $("#status").val(), handleStatus = $("#handleStatus").val(),
			gridAsynOpt = null;
		
		<#if startGridId??>
			gridAsynOpt = {
				Async : {
					enable : true,
					autoParam : [ "id=gridId" ],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : ${startGridId?c}
					}
				}
			};
		</#if>
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items && items.length > 0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, gridAsynOpt);
		
		if(status == "" && (listType == "3" || listType == "4")) {
			$("#statusTr").show();
			
			<#if statusMap??>
				var statusObj = null;
				
				<#list statusMap?keys as status>
					<#if status != "001">
						statusObj = {};
						
						statusObj.name = "${statusMap[status]}";
						statusObj.value = "${status}";
						
						statusArray.push(statusObj);
					</#if>
				</#list>
			
	        </#if>
			
			AnoleApi.initListComboBox("statusName", "status", null, null, null, {
	        	DataSrc: statusArray,
	        	ShowOptions: {
	        		EnableToolbar: true
	        	}
	        });
        }
		
		if(handleStatus == "" && listType != '1') {
			$("#handleStatusTr").show();
			
			AnoleApi.initListComboBox("handleStatusName", "handleStatus", null, null, null, {
	        	DataSrc : [{"name":"是", "value":"02"},{"name":"否", "value":"01"}],
	        	ShowOptions: {
	        		EnableToolbar: true
	        	}
	        });
        }
				
		if(isBlankStringTrim($("#toolBtnDiv").html())) {
			$("#ToolBar").removeClass("ToolBar");
			$("#ToolBar").css("border-top", "1px solid #d8d8d8");
		}
	});
	
	function add() {
		var opt = {
			'maxHeight': 400,
			'maxWidth': 720
		};
		var url = '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toAdd.jhtml';
		
		opt.title = "新增禁毒事件";
		opt.targetUrl = url;
		
		openJqueryWindowByParams(opt);
	}
	
	function edit() {
		var drugEnforcementId = "";
		$("input[name='drugEnforcementId']:checked").each(function() {
			drugEnforcementId = $(this).val();
		});
		if(drugEnforcementId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var opt = {
				'maxHeight': 400,
				'maxWidth': 720
			};
			var url = '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toAdd.jhtml?drugEnforcementId=' + drugEnforcementId;
			
			opt.title = "编辑禁毒事件";
			opt.targetUrl = url;
			
			openJqueryWindowByParams(opt);
		}
	}
	
	function del() {
		var drugEnforcementId = "";
		$("input[name='drugEnforcementId']:checked").each(function() {
			drugEnforcementId = $(this).val();
		});
		if(drugEnforcementId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的禁毒事件吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/drugEnforcementEvent/delData.jhtml',
						data: 'drugEnforcementId='+drugEnforcementId,
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.success && data.success == true){
			  					reloadDataForSubPage(data.tipMsg, true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '删除失败！', 'error');
			  					}
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
	
	function detail(drugEnforcementId, listType) {
		var opt = {
			'maxWidth': 800
		};
		
		listType = listType || "${listType!}";
		
		var url = '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toDetail.jhtml?drugEnforcementId=' + drugEnforcementId + "&listType=" + listType;
		
		opt.title = "查看禁毒事件";
		opt.targetUrl = url;
		
		openJqueryWindowByParams(opt);
	}
	
	function fetchQueryParams() {
		var searchArray = new Array();
		
		$('#drugEnforcementEventForm .queryParam').each(function() {
			var queryObj = $(this),
				value = queryObj.val(),
				name = queryObj.attr("name");
			
			if(value) {
				searchArray[name] = value;
			}
			
		});
		
		return searchArray;
	}
	
	function searchData(isCurrent) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = fetchQueryParams();
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
	}
	
	function resetCondition() {
		$('#drugEnforcementEventForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
	function DivHide(){
		$("#ToolBar .blind").slideUp();//窗帘效果展开
	}
	
	function DivShow(msg){
		$("#ToolBar .blind").html(msg);
		$("#ToolBar .blind").slideDown();//窗帘效果展开
		setTimeout("this.DivHide()",800);
	}
	
</script>