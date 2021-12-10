<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
</style>
<div id="jqueryToolbar">
	<form id="msgCCCfgForm">
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>流程图名称：</li>
	                <li>
	                	<input name="workflowNameFuzzy" type="text" class="inp1 w150 queryParam" id="wfWorkflowNameFuzzy" value="" onkeydown="_onkeydown();" />
	                </li>
	            	<li>目标节点：</li>
	                <li><input name="wfEndNodeName" type="text" class="inp1 w150 queryParam" id="wfEndNodeName" value="" onkeydown="_onkeydown();" /></li>
	            	<li>配置状态：</li>
	                <li>
	                	<input id="cfgStatus" name="cfgStatus" type="text" value="" class="queryParam hide"/>
	                	<input id="cfgStatusName" type="text" class="inp1 InpDisable w150" />
	                </li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>组织层级：</span></label>
	                                    			<input id="orgChiefLevel" name="orgChiefLevel" type="text" value="" class="queryParam hide"/>
	                                    			<input id="orgChiefLevelName" type="text" class="inp1 w150"/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>配送类型：</span></label>
	                                    			<input id="ccType" name="ccType" type="text" value="" class="queryParam hide"/>
	                                    			<input id="ccTypeName" type="text" class="inp1 w150"/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>配置类型：</span></label>
	                                    			<input id="cfgType" name="cfgType" type="text" value="" class="queryParam hide"/>
	                                    			<input id="cfgTypeName" type="text" class="inp1 w150"/>
	                                    		</td>
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
	        <div id="actionDiv" class="tool fr hide"><@actionCheck></@actionCheck></div>
	    </div>
	</form>
</div>

<script type="text/javascript">
	$(function() {
		AnoleApi.initListComboBox("cfgStatusName", "cfgStatus", null, null, null, {
			DataSrc : [{"name":"启用", "value":"1"}, {"name":"停用", "value":"0"}],
			ShowOptions: {
                EnableToolbar : true
            }
		});
		
		AnoleApi.initTreeComboBox("orgChiefLevelName", "orgChiefLevel", "E008", null, null, {
			ChooseType : "1" ,
			EnabledSearch : true,
			ShowOptions: {
				EnableToolbar : true
			}
		});
		
		AnoleApi.initListComboBox("cfgTypeName", "cfgType", null, null, ["${cfgInfo.cfgType!}"], {
			DataSrc : [{"name":"用户", "value":"1"},{"name":"组织", "value":"2"},{"name":"角色", "value":"3"},{"name":"职位", "value":"4"}],
			ShowOptions: {
				EnableToolbar : true
			}
		});
		
		AnoleApi.initListComboBox("ccTypeName", "ccType", null, null, ["${cfgInfo.ccType!}"], {
			DataSrc : [{"name":"主送", "value":"3"}, {"name":"分送", "value":"1"},{"name":"选送", "value":"2"}],
			ShowOptions: {
				EnableToolbar : true
			}
		});
		
		if($("#actionDiv").find("a").length) {
			$("#actionDiv").show();
		} else {
			$("#toolbarDiv").remove();
		}
	});
    
	function add() {
		var url = "${rc.getContextPath()}/zhsq/reportMsgCCCfg/toAdd.jhtml";
		
		openJqueryWindowByParams({
			maxWidth: 900,
			maximizable: true,
			title: "新增人员配置信息",
			targetUrl: url
		});
	}
	
	function edit() {
		var cfgUUID = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
			cfgUUID = selectItem.cfgUUID;
		}
		
		if(cfgUUID == "") {
			$.messager.alert('警告','请选中要编辑的记录再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/reportMsgCCCfg/toAdd.jhtml?cfgUUID=" + cfgUUID;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 900,
		  		maximizable: true,
		  		title: "编辑人员配置信息",
		  		targetUrl: url
		  	});
		}
	}
	
	function detail(cfgUUID) {
		if(cfgUUID) {
			var url = "${rc.getContextPath()}/zhsq/reportMsgCCCfg/toDetail.jhtml?cfgUUID=" + cfgUUID;
			
			openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: "查看人员配置信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选择需要查看的记录!','warning');
		}
	}
	
	function del() {
		var cfgUUID = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
			cfgUUID = selectItem.cfgUUID;
		}
		
		if(cfgUUID == "") {
			$.messager.alert('警告','请选中要删除的记录再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/reportMsgCCCfg/delCfgInfo.jhtml',
						data:{'cfgUUID': cfgUUID},
						dataType:"json",
						success: function(data) {
							if (data.success && data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							}
						},
						error:function(data){
							$.messager.alert('错误','删除人员配置信息失败！','error');
						}
					});
				}
			});
		}
	}
	
	function resetCondition() {//重置
		$('#msgCCCfgForm')[0].reset();
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#msgCCCfgForm .queryParam").each(function() {
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