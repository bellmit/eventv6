<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.summary{border-bottom:1px solid #d8d8d8; height:56px;}
	.summary ul li{float:left; font-size:12px; line-height:28px; padding:0 10px; margin-right:5px;}
</style>

<div id="jqueryToolbar" class="MainContent">
	<div class="summary">
		<div style="padding: 10px 10px 0 10px;"><b>业务配置基本信息</b></div>
		<ul>
	    	<li>所属地域：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.regionName??>${handlerWfCfg.regionName}</#if></span></li>
	    	<li>业务类型：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.bizTypeName??>${handlerWfCfg.bizTypeName}</#if></span></li>
	    	<li>配置信息：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.wfCfgName??>${handlerWfCfg.wfCfgName}</#if></span></li>
	    </ul>
	</div>
	<div class="ConSearch">
		<form id="wfProcCfgForm">
			<input type="hidden" id="wfcId" name="wfcId" value="<#if handlerWfCfg.wfcId??>${handlerWfCfg.wfcId?c}</#if>" class="queryParam" />
			
	        <div class="fl">
	        	<ul>
	                <li>所属地域：</li>
	                <li>
		                <input type="text" id="infoOrgCode" name="regionCode" value="" class="queryParam hide" />
		                <input type="text" id="gridName" name="gridName" value="" class="inp1 InpDisable w150"/>
		            </li>
		            <li>事件类别：</li>
		            <li>
		    			<input type="text" id="eventCodes" name="eventCodes" value="" class="queryParam hide" />
		    			<input type="text" id="eventCodesName" class="inp1 InpDisable w150" />
			    	</li>
			    	<li>流程环节：</li>
		            <li>
		    			<input type="text" id="taskCode" name="taskCode" value="" class="queryParam hide" />
		    			<input type="text" id="taskCodeName" class="inp1 InpDisable w150" />
			    	</li>
	            	<li style="position:relative; display:none;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:370px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
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
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBarDIV">
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
		
		<div class="tool fr" id="toolFrDIV">
			<a id="del" href="javascript:void(0)" onclick="del()" class="NorToolBtn DelBtn">删除</a>
			<a id="add" href="javascript:void(0)" onclick="edit()" class="NorToolBtn EditBtn">编辑</a>
			<a id="add" href="javascript:void(0)" onclick="add()" class="NorToolBtn AddBtn">新增</a>
		</div>
    </div>
	
</div>

<script type="text/javascript">
	var defaultWinOpt = {
		'maxWidth': 735,
		'maxHeight': 400
	};
	
	$(function(){
        AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, {
            Async : {
				enable : true,
				autoParam : [ "id=gridId" ],
				dataFilter : _filter,
				otherParam : {
					rootName: "行政区划",
					startGridId : "<#if startGridId??>${startGridId?c}</#if>"
				}
			}
        });
        
        AnoleApi.initTreeComboBox("eventCodesName", "eventCodes", "${eventTypePcode!}", null, null, {
        	RenderType: "01",
        	ShowOptions:{
				EnableToolbar : true,
				ChkboxType : {
					"Y": "s", 
					"N": "ps" 
				}
			}
        });
        
        fetchTaskCodes();
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/toAdd.jhtml?wfcId=' + $("#wfcId").val(),
			opt = {
				'title': "新增环节配置信息",
				'targetUrl': url
			};
	   
	   	openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
	}
	
	function edit() {
		var wfpcId = "";
		$("input[name='wfpcId']:checked").each(function() {
			wfpcId = $(this).val();
		});
		if(wfpcId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/toAdd.jhtml?wfcId='+ $("#wfcId").val() +'&wfpcId=' + wfpcId,
				opt = {
					'title': "编辑环节配置信息",
					'targetUrl': url
				};
		   
		   	openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
		}
	}
	
	function detail(wfpcId) {
		var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/toDetail.jhtml?wfpcId='+wfpcId,
			opt = {
				'title': "查看环节配置信息",
				'targetUrl': url
			};
		
		openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
	}
	
	function del(){
		var wfpcId = "";
		$("input[name='wfpcId']:checked").each(function() {
			wfpcId = $(this).val();
		});
		if(wfpcId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的环节编码配置吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/delProcCfg.jhtml',
						data: 'wfpcId='+wfpcId,
						dataType:"json",
						success: function(data) {
							modleclose();
		  		
  							if(data.success && data.success == true) {
								flashData(data.tipMsg, true);
							}
						},
						error:function(data){
							modleclose();
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	//获取环节编码
	function fetchTaskCodes() {
		var wfcId = $("#wfcId").val();
		
		if(wfcId) {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/fetchTaskCodes.jhtml',
				data: 'wfcId='+ wfcId,
				dataType:"json",
				success: function(data){
					if(data && data.length) {
						var taskCodeArray = [];
						
						for(var index in data) {
							var taskCodeObj = {};
							
							taskCodeObj.name = data[index].taskCodeName;
							taskCodeObj.value = data[index].taskCode;
							
							taskCodeArray.push(taskCodeObj);
						}
						
						AnoleApi.initListComboBox("taskCodeName", "taskCode", null, null, null, {
				        	DataSrc: taskCodeArray
				        });
					}
				},
				error:function(data){
					$.messager.alert('错误','流程环节获取失败！','error');
				}
			});
		}
	}
	
	function searchData() {
		var searchArray = new Array();
		
		$("#wfProcCfgForm .queryParam").each(function(index, param) {
			var name = $(param).attr("name"),
				value = $(param).val();
			
			if(isNotBlankString(value)) {
				searchArray[name] = value;
			}
		});
		
		doSearch(searchArray);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		flashData();
	}
	
	function resetCondition() {
		$('#wfProcCfgForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>