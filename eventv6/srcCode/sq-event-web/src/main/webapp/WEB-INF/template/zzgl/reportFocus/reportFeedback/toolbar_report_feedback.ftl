<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="reportFeedbackQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType}" />
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属区域：</li>
	                <li>
	                	<input id="infoOrgCode" name="regionCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" type="text" class="hide"/>
	                	<input id="gridName" type="text" class="inp1 InpDisable w150" />
	                </li>
					<li>业务类型：</li>
					<li>
						<input id="bizType" name="bizType" type="text" value="" class="queryParam hide"/>
						<input id="bizTypeName" type="text" class="inp1 InpDisable w150" />
					</li>

	            	<li>关键字：</li>
	                <li><input name="keyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="下达内容/反馈内容" defaultValue="下达内容/反馈内容" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>反馈编号：</span></label><input class="inp1 queryParam" type="text" id="fbUUId" name="fbUUId" style="width:248px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>业务标识：</span></label><input class="inp1 queryParam" type="text" id="bizSign" name="bizSign" style="width:248px;"></input></td>
	                                    	</tr>
											<tr>
	                                    		<td><label class="LabName width65px"><span>报告编号：</span></label><input class="inp1 queryParam" type="text" id="bizCode" name="bizCode" style="width:248px;"></input></td>
	                                    	</tr>
											<tr>
												<td><label class="LabName width65px"><span>下达状态：</span></label><input id="seStatus" name="seStatus" type="text" value="" class="queryParam hide"/><input id="seStatusName" type="text" class="inp1 queryParam"  style="width:248px;" /></td>
											</tr>
											<tr>
												<td><label class="LabName width65px"><span>接收状态：</span></label><input id="reStatus" name="reStatus" type="text" value="" class="queryParam hide"/><input id="reStatusName" type="text" class="inp1 queryParam"  style="width:248px;" /></td>
											</tr>
											<tr>
												<td><label class="LabName width65px"><span>反馈状态：</span></label><input id="fbStatus" name="fbStatus" type="text" value="" class="queryParam hide"/><input id="fbStatusName" type="text" class="inp1 queryParam"  style="width:248px;" /></td>
											</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>下达时间：</span></label>
													<input class="inp1 Wdate fl queryParam" type="text" id="beginSeTime" name="beginSeTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">
														至</span><input class="inp1 Wdate fl queryParam" type="text" id="endSeTime" name="endSeTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
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

		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, {
			OnCleared: function() {
				$("#infoOrgCode").val('');
			},
			ShowOptions: {
				EnableToolbar : true
			}
		});

		var bizTypeArray = null;
		<#if bizTypeMap??>
			var bizTypeObj = null;
		    bizTypeArray = [];
			<#list bizTypeMap?keys as bizType>
				bizTypeObj = {};
				bizTypeObj.name = "${bizTypeMap[bizType]}";
				bizTypeObj.value = "${bizType}";
				bizTypeArray.push(bizTypeObj);
			</#list>
		</#if>
		if(bizTypeArray != null && bizTypeArray.length > 1) {
			AnoleApi.initListComboBox("bizTypeName", "bizType", null, null, null, {
				DataSrc : bizTypeArray,
				ShowOptions : {
					EnableToolbar : true
				}
			});
		}

		AnoleApi.initListComboBox("seStatusName", "seStatus", null, null, null, {
			DataSrc : [{"name":"已下达", "value":"01"},{"name":"已退回", "value":"02"}],
			ShowOptions : {
				EnableToolbar : true
			}
		});

		AnoleApi.initListComboBox("reStatusName", "reStatus", null, null, null, {
			DataSrc : [{"name":"未接收", "value":"01"},{"name":"已接收", "value":"02"},{"name":"超时接收", "value":"03"}],
			ShowOptions : {
				EnableToolbar : true
			}
		});

		AnoleApi.initListComboBox("fbStatusName", "fbStatus", null, null, <#if listType?? && listType =='1'>['01']<#else>null</#if>, {
			DataSrc : [{"name":"未反馈", "value":"01"},{"name":"已反馈", "value":"02"},{"name":"超时反馈", "value":"03"}],
			ShowOptions : {
				EnableToolbar : true
			}
		});

		if($("#actionDiv").find("a").length) {
			$("#actionDiv").show();
		}
		/*else {
			$("#toolbarDiv").remove();
		}*/
	});
	
	function resetCondition() {//重置
		$('#reportFeedbackQueryForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#reportFeedbackQueryForm .queryParam").each(function() {
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

<@block name="operateFunction">
	<#include "/zzgl/reportFocus/reportFeedback/operate_report_feedback.ftl" />
</@block>