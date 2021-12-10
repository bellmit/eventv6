<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="timeAppQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!}" />
		<input type="hidden" name="isJurisdictionQuery" class="queryParam" value="${isJurisdictionQuery!}" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	                <li>关键字：</li>
	                <li><input name="eventKeyWord" type="text" class="inp1 keyBlank w150 queryParam" id="eventKeyWord" value="事件描述/标题/事发详址" defaultValue="事件描述/标题/事发详址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li>审核状态：</li>
	                <li>
	                	<input id="timeAppCheckStatus" name="timeAppCheckStatus" type="text" value="" class="queryParam hide"/>
	                	<input id="timeAppCheckStatusName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <#if applicationTypeMap??>
		                <input id="applicationType" name="applicationType" type="text" value="${applicationType!}" defaultValue="${applicationType!}" class="queryParam hide"/>
		                <#if (applicationTypeMap?size > 1)>
			                <li>申请类别：</li>
			                <li>
			                	
			                	<input id="applicationTypeName" type="text" class="inp1 InpDisable w150" />
			                </li>
		                </#if>
	                </#if>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 queryParam" type="text" id="code" name="code" style="width:135px;"></input></td>
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
        AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, null, {
        	DataSrc : [{"name":"待审核", "value":"3"},{"name":"已通过", "value":"1"},{"name":"未通过", "value":"2"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	}
        });
        
        if($("#applicationType").length > 0) {
        	var applicationTypeArray = null;
        	
        	<#if applicationTypeMap??>
        		var applicationTypeObj = null;
        		applicationTypeArray = [];
        		
        		<#list applicationTypeMap?keys as applicationType>
        			applicationTypeObj = {};
        			
        			applicationTypeObj.name = "${applicationTypeMap[applicationType]}";
        			applicationTypeObj.value = "${applicationType}";
        			
        			applicationTypeArray.push(applicationTypeObj);
        		</#list>
        	</#if>
        	
        	if(applicationTypeArray != null && applicationTypeArray.length > 1) {
		        AnoleApi.initListComboBox("applicationTypeName", "applicationType", null, null, null, {
		        	DataSrc : applicationTypeArray,
		        	IsTriggerDocument: false,
		        	OnCleared: function() {
		        		$('#applicationType').val($('#applicationType').attr('defaultValue'));
		        	},
		        	ShowOptions:{
		        		EnableToolbar : true
		        	}
		        });
	        }
        }
        
        if($("#actionDiv").find("a").length) {
        	$("#actionDiv").show();
        } else {
        	$("#toolBarDiv").css({'height': '0', 'border-top': 'none'});
        }
        
        $("#toolBarDiv").show();
	});
    
    function selectItem(index, rec) {
    	var timeAppCheckStatus = rec.timeAppCheckStatus;
    	
    	if(timeAppCheckStatus && timeAppCheckStatus == '3') {
    		$("#audit").show();
    	} else {
    		$("#audit").hide();
    	}
    }
    
    function detail(applicationId) {
		if(applicationId == "") {
			$.messager.alert('警告','请选中要审核的记录!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/timeApplication/toDetail.jhtml?applicationId=" + applicationId + "&listType=" + $("#listType").val();
			
		  	openJqueryWindowByParams({
		  		maxWidth: 600,
		  		maxHeight: 360,
		  		title: "信息审核",
		  		targetUrl: url
		  	});
		}
    }
	
	function resetCondition() {//重置
		$('#timeAppQueryForm')[0].reset();
		$('#eventKeyWord').addClass('keyBlank');
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#timeAppQueryForm .queryParam").each(function() {
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
		var eventKeyWord = $(obj).val();
		
		if(eventKeyWord == ''){
			$(obj).addClass('keyBlank');
			$(obj).val($(obj).attr("defaultValue"));
		}
	}
</script>