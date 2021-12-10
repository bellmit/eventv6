<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	.dateRenderWidth{width: 195px;}
</style>
<div id="jqueryToolbar">
	<form id="eventMsgQueryForm">
		<input type="hidden" name="moduleCode" class="queryParam" value="${moduleCode!}" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	            	<li>消息内容：</li>
	                <li><input name="msgContent" type="text" class="inp1 keyBlank w150 queryParam" id="msgContent" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>发送时间：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
	                                    			<input type="text" id="_happenTimeDateRender" class="inp1 InpDisable dateRenderWidth" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
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
	        <div id="actionDiv" class="tool fr"><@actionCheck></@actionCheck></div>
	    </div>
	</form>
</div>

<script type="text/javascript">
	var happenTimeDateRender = null;
	
	$(function() {
		if($('#_happenTimeDateRender').length > 0) {
			happenTimeDateRender = init4DateRender('_happenTimeDateRender', 'happenTimeStart', 'happenTimeEnd');
		}
	});
	
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
	
	function resetCondition() {//重置
		clear4DateRender();
		
		$('#eventMsgQueryForm')[0].reset();
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventMsgQueryForm .queryParam").each(function() {
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
	
	function init4DateRender(renderId, startTimeId, endTimeId) {
		var dateRender = null;
		
		dateRender = $('#' + renderId).anoleDateRender({
			BackfillType : "1",
			ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#" + startTimeId).val(api.getStartDate());
					$("#" + endTimeId).val(api.getEndDate());
				},
				OnCleared : function() {
					$("#" + startTimeId).val('');
					$("#" + endTimeId).val('');
				}
			}
		}).anoleDateApi();
		
		return dateRender;
	}
	
	function clear4DateRender() {
		if(happenTimeDateRender != null) {
			happenTimeDateRender.doClear();
		}
	}
</script>