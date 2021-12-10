<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:100px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="accountEnforceQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属行政辖区：</li>
	                <li>
	                	<input id="infoOrgCode" name="regionCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" type="text" class="hide"/>
	                	<input id="gridName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <li>问题标题：</li>
	                <li>
	                	<input id="probTitle" name="probTitle" type="text" class="inp1 queryParam InpDisable w150" onkeydown="_onkeydown();" />
	                </li>
	            	<li>问题线索来源：</li>
	                <li>
	                	<input id="source" name="source" type="text" value="" class="queryParam hide"/>
	                	<input id="sourceName" type="text" class="inp1 InpDisable w150" />
	                </li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:380px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>问题编号：</span></label><input class="inp1 queryParam" type="text" name="probNo" style="width:135px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>对象类别：</span></label>
	                                    			<input id="violationObjType" name="violationObjType" type="text" value="" class="queryParam hide"/>
	                                    			<input id="violationObjTypeName" type="text" class="inp1 InpDisable" style="width: 135px;" />
	                                    		</td>
	                                    	</tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>问题上报部门：</span></label>
													<input id="rptUnitName" name="rptUnitName" class="inp1 InpDisable queryParam" type="text"  style="width:135px;"/>
												</td>
                                            </tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>涉案金额(万元)：</span></label><input class="inp1 fl easyui-validatebox queryParam" data-options="tipPosition:'bottom',validType:'floatNum'" type="text" name="amountInvolvedStart" style="width:110px;"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 fl easyui-validatebox queryParam" data-options="tipPosition:'bottom',validType:'floatNum'" type="text" name="amountInvolvedEnd" style="width:110px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>填报时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" name="createdStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" name="createdEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>违纪违规时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" name="violationDateStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" name="violationDateEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
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
		<div class="ToolBar hide" id="toolBarDiv">
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
	$(function(){
		AnoleApi.initTreeComboBox("sourceName", "source", "B590000", null, null, { 
			ChooseType : "1",
			ShowOptions:{
        		EnableToolbar : true
        	}
		});
		
		AnoleApi.initTreeComboBox("violationObjTypeName", "violationObjType", "B590004", null, null, { 
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

        if($("#actionDiv").find("a").length) {
        	$("#actionDiv").show();
        } else {
        	$("#toolBarDiv").css({'height': '0', 'border-top': 'none'});
        }
        
        $("#toolBarDiv").show();
	});
    
	function add() {
		var url = "${rc.getContextPath()}/zhsq/accountabilityEnforcement/toAdd.jhtml";
		
		openJqueryWindowByParams({
			maxWidth: 1000,
			title: "新增问题信息",
			targetUrl: url
		});
	}
	
	function edit() {
		var probId = "";
		
		$("input[name='probId']:checked").each(function() {
			probId = $(this).val();
		});
		
		if(probId == "") {
			$.messager.alert('警告','请选中要编辑的问题再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/accountabilityEnforcement/toAdd.jhtml?probId=" + probId;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 1000,
		  		title: "编辑问题信息",
		  		targetUrl: url
		  	});
		}
	}
	
	function del() {
		var probId = "";
		
		$("input[name='probId']:checked").each(function() {
			probId = $(this).val();
		});
		
		if(probId == "") {
			$.messager.alert('警告','请选中要删除的问题再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的问题吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/accountabilityEnforcement/delProb.jhtml',
						data: 'probId='+probId,
						dataType:"json",
						success: function(data) {
							if (data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							} else {
                                $.messager.alert('错误',data.tipMsg,'error');
                                modleclose();
							}
						},
						error:function(data){
							$.messager.alert('错误','删除问题连接超时！','error');
							modleclose();
						}
					});
				}
			});
		}
	}
	
	function detail(probId, listType) {
		if(probId) {
			listType = listType || $("#listType").val();
			
			var url = "${rc.getContextPath()}/zhsq/accountabilityEnforcement/toDetail.jhtml?probId=" + probId + "&listType=" + listType;
			
			openJqueryWindowByParams({
				maxWidth: 1000,
				title: "查看问题信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选中查看的问题再执行此操作!','warning');
		}
	}
	
	function archive() {
		modleopen();
		
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/accountabilityEnforcement/endAccountEnDraft.jhtml',
			dataType:"json",
			success: function(data) {
				var recordTotal = data.recordTotal || 0, 
					successTotal = data.successTotal || 0;
				modleclose();
				
				$.messager.alert('提示','共处理数据' + recordTotal + '条，成功归档' + successTotal + '条！','info', function() {
					reloadDataForSubPage(data.tipMsg, true);
				});
			},
			error:function(data){
				$.messager.alert('错误','归档草稿问题连接超时！','error');
				modleclose();
			}
		});
	}
	
	function resetCondition() {//重置
		$('#accountEnforceQueryForm')[0].reset();
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#accountEnforceQueryForm .queryParam").each(function() {
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
</script>