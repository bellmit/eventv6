<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.w100{width:100px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="inspectionForm">
	        <div class="fl">
	        	<ul>
	                <li>所属网格：</li>
	                <li>
		                <input id="infoOrgCode" name="gridCode" value="${infoOrgCode!}" class="hide queryParam" />
		                <input type="text" id="gridName" name="gridName" value="${gridName!}" class="inp1 InpDisable w150"/>
		            </li>
		            <li>检查时间：</li>
		            <li>
						<input type="text" style="cursor: pointer;" class="Wdate inp1 w100 fl queryParam" readonly name="startTime" id="startTimeStr" onclick="WdatePicker({readOnly:true})"/>
						<span class="Check_Radio" style="padding:0 5px;">至</span>
						<input  type="text" style="cursor: pointer;" class="Wdate inp1 w100 fl queryParam" readonly name="endStartTime" id="endStartTimeStr" onclick="WdatePicker({readOnly:true})"/>
				    </li>
				    <li>是否发现污染单位：</li>
	                <li>
	                	<input type="text" id="inspectResultFlag" name="inspectResultFlag" class="hide queryParam" />
	                	<input type="text" id="inspectResultFlagName" value="" class="inp1 w100" />
	                </li>
		            
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:285px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>检查类型：</span></label>
	                                    			<input type="text" id="inspectType" name="inspectType" class="hide queryParam" />
	                								<input type="text" id="inspectTypeName" value="" class="inp1 w150" />
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
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="toolBarDiv">
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
		<!--toolFrDiv代码格式不可换行，否则会影响布局-->
		<div class="tool fr" id="toolFrDiv"><@actionCheck></@actionCheck></div>
    </div>
	
</div>

<script type="text/javascript">
	var opt = {
		maxWidth: 735,
		maxHeight: 400
	};
	$(function(){
		if($('#toolFrDiv > a').length == 0){
			$("#toolBarDiv").remove();
		}
	
        AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
		
		AnoleApi.initTreeComboBox("inspectTypeName", "inspectType", "${EP_INSPECT_TYPE_PCODE!}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			},
			ChooseType : "1" 
		});
		
		AnoleApi.initListComboBox("inspectResultFlagName", "inspectResultFlag", null, null, [""], {
			ShowOptions : {
				EnableToolbar : true
			},
        	DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}]
        });
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/enterpriseCheck/toAdd.jhtml';
	   
	  	openJqueryWindowByParams($.extend({}, opt, {
	  		title: "新增环保企业巡查",
	  		targetUrl: url
	  	}));
	}
	
	function edit() {
		var birId = "";
		$("input[name='birId']:checked").each(function() {
			birId = $(this).val();
		});
		if(birId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/enterpriseCheck/toAdd.jhtml?birId=' + birId;
			
		  	openJqueryWindowByParams($.extend({}, opt, {
		  		title: "编辑环保企业巡查",
		  		targetUrl: url
		  	}));
		}
	}
	
	function report() {
		var selectedRow = $('#list').datagrid('getSelected');
		
		if(selectedRow == null) {
			$.messager.alert('警告','请选中要上报的数据再执行此操作!','warning');
		} else if(selectedRow.inspectResultFlag == '1') {
			var event = {
				"eventName" : '环保企业巡查',
				"happenTimeStr" : selectedRow.startTimeStr,
				"occurred" : selectedRow.inspectionAddr||'',
				"content" : "发现污染单位："+ selectedRow.inspectObjName +"。检查内容："+ selectedRow.content,
				"gridId" : selectedRow.gridId,
				"gridCode" : selectedRow.gridCode,
				"gridName" : selectedRow.gridName,
				"type" : selectedRow.inspectType,
				"typesForList" : selectedRow.inspectType,
				"contactUser" : selectedRow.directorOfInspectObj||'',
				"tel" : selectedRow.directorTelOfInspectObj||'',
				"advice" : selectedRow.inspectResult,
				"callBack" : 'parent.reportCallBack',
				"eventReportRecordInfo": {
					'bizId' : selectedRow.birId,
					'bizType' : 'ENVIRONMENTAL_PROTECTION_INSPECT'
				}
				
			};
			
			var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml';
			
		  	var reportWinId = openJqueryWindowByParams($.extend({}, opt, {
						  		maxWidth: 800,
						  		title: "上报环保企业巡查"
							  }));
		  	
		  	var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
							 '</form>';
			
			$("#jqueryToolbar").append($(reportForm));
			$("#_report4eventForm").append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));
			
			$("#_reportEventJson").val(JSON.stringify(event));
			$("#_report4eventForm").attr('action', url);
			
			$("#_report4eventForm").submit();
			
			$("#_report4eventForm").remove();
	  	} else {
	  		$.messager.alert('警告','该巡查记录不能上报!','warning');
	  	}
	}
	
	function reportCallBack(data) {
		if(data.result) {
			var selectedRow = $('#list').datagrid('getSelected');
			
			$.ajax({
				type: "POST",
				url: "${rc.getContextPath()}/zhsq/enterpriseCheck/saveEnterpriseCheck.jhtml",
				data: 'birId='+ selectedRow.birId+"&isDel=3",
				dataType:"json",
				success: function(data) {
					modleclose();
					
					if(data.success) {
	  					reloadDataForSubPage('上报成功！');
	  				} else {
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', '删除操作失败！', 'error');
	  					}
	  				}
				},
				error:function(data){
					modleclose();
					$.messager.alert('错误','连接超时！','error');
				}
			});
			
		} else {
			$.messager.alert('错误','上报失败！','error');
		}
	}
	
	function del(){
		var birId = "";
		$("input[name='birId']:checked").each(function() {
			birId = $(this).val();
		});
		if(birId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的环保企业巡查信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/enterpriseCheck/delEnterpriseCheck.jhtml',
						data: 'birId='+birId,
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.success) {
			  					reloadDataForSubPage(data.tipMsg, true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '删除操作失败！', 'error');
			  					}
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
	
	function detail(birId) {
		var url = '${rc.getContextPath()}/zhsq/enterpriseCheck/toDetail.jhtml?birId='+birId;
		
		opt.title = "查看环保企业巡查";
	   	opt.targetUrl = url;
	   
	  	openJqueryWindowByParams(opt);
	}
	
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = ["AddBtn", "EditBtn", "DelBtn", "ShangBaoBtn"];
			
			switch(selectedRow.isDel) {
				case '1': {
				}
				case '3': {
					showBtn.splice(1, 3); break;
				}
				case '2': {
					if(selectedRow.inspectResultFlag != '1') {
						showBtn.splice(-1, 1);
					}
					
					break;
				}
			}
			
			$("#toolFrDiv > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDiv ." + showBtn[index]).show();
			}
		}
	}
	
	function searchData(isCurrent) {
		var searchArray = new Array();
		
		$('.queryParam').each(function() {
			var queryObj = $(this),
				value = queryObj.val(),
				name = queryObj.attr("name");
			
			if(value) {
				searchArray[name] = value;
			}
			
		});
		
		doSearch(searchArray, isCurrent);
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
	
	function resetCondition() {
		$('#inspectionForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>