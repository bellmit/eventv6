<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>

<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.w190{width:190px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="timeAppQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!}" />
		<input type="hidden" name="eventStatus" class="queryParam" value="99,00,01,02,03,04,06" />
		<input type="hidden" name="isCapApplicant" class="queryParam" value="true" />
		<input type="hidden" name="isJurisdictionQuery" class="queryParam" value="${isJurisdictionQuery!}" />
		<input type="hidden" name="isCreatedBySelf" class="queryParam" value="${isCreatedBySelf!}" />
		<input type="hidden" name="isEnableBuildScope" class="queryParam" value="${isEnableBuildScope!}" />
		<@block name="extraHiddenParam"></@block>
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属网格：</li>
                    <li>
                        <input type="text" id="infoOrgCode" name="infoOrgCode" class="hide queryParam" />
                        <input id="gridName" type="text" class="inp1 InpDisable w150" />
                    </li>
	                <li>采集时间：</li>
	                <li>
	                	<input class="inp1 hide queryParam" type="text" id="eventCreateTimeStart" name="eventCreateTimeStart" value="${eventCreateTimeStart!}"></input>
	                	<input class="inp1 hide queryParam" type="text" id="eventCreateTimeEnd" name="eventCreateTimeEnd" value="${eventCreateTimeEnd!}"></input>
	                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:195px;" value="${eventCreateTimeStart!}<#if eventCreateTimeStart?? && eventCreateTimeEnd??> ~ </#if>${eventCreateTimeEnd!}"/>
	                </li>
	                <li>关键字：</li>
	                <li><input name="eventKeyRemarkWord" type="text" class="inp1 keyBlank w190 queryParam" id="eventKeyWord" value="事件描述/标题/事发详址/补充描述" defaultValue="事件描述/标题/事发详址/补充描述" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<@block name="extraParamTopTr"></@block>
	                                    	<tr id="gridRegionTypeTr">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>网格类型：</span></label>
	                                    			<input type="text" id="gridRegionType" name="gridRegionType" class="hide queryParam"/>
	                                    			<input type="text" id="gridRegionTypeName" class="inp1 InpDisable w150" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事件分类：</span></label>
	                                    			<input type="text" id="type" name="eventType" class="hide queryParam"/>
	                                    			<input type="text" id="typeName" class="inp1 InpDisable w150" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 w150 queryParam" type="text" id="code" name="code" ></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span><@block name="createTimeQueryLableName">删除时间：</@block></span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart"></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd"></input>
	                                    			<input type="text" id="_timeAppCreateTimeDateRender" class="inp1 InpDisable" style="width:195px;" />
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
	        <div id="actionDiv" class="tool fr hide">
				<@actionCheck></@actionCheck>
			</div>
		        
	    </div>
	</form>
</div>

<script type="text/javascript">
	var createTimeDateRender = null, timeAppCreateTimeDateRender = null;
	<@block name="delEventAppGlobalVariables"></@block>
	
	$(function(){
        AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
            if(items && items.length > 0){
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
        
        <@block name="extraParamInit"></@block>
        
        AnoleApi.initTreeComboBox("gridRegionTypeName", "gridRegionType", "E040", null, null, {
    		ShowOptions: {
    			 EnableToolbar : true
    		}
    	});
    	
        AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, { 
        	ChooseType : "1" ,
        	EnabledSearch : true,
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        
        createTimeDateRender = $('#_createTimeDateRender').anoleDateRender({
        	BackfillType : "1",
        	ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
        	ShowOptions : {
        		TabItems : ["常用", "年", "季", "月", "清空"]
        	},
        	BackEvents : {
        		OnSelected : function(api) {
        			$("#eventCreateTimeStart").val(api.getStartDate());
        			$("#eventCreateTimeEnd").val(api.getEndDate());
        		},
        		OnCleared : function() {
        			$("#eventCreateTimeStart").val('');
        			$("#eventCreateTimeEnd").val('');
        		}
        	}
        }).anoleDateApi();
        
        timeAppCreateTimeDateRender = $('#_timeAppCreateTimeDateRender').anoleDateRender({
        	BackfillType : "1",
        	ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
        	ShowOptions : {
        		TabItems : ["常用", "年", "季", "月", "清空"]
        	},
        	BackEvents : {
        		OnSelected : function(api) {
        			$("#createTimeStart").val(api.getStartDate());
        			$("#createTimeEnd").val(api.getEndDate());
        		},
        		OnCleared : function() {
        			$("#createTimeStart").val('');
        			$("#createTimeEnd").val('');
        		}
        	}
        }).anoleDateApi();
		
        if($("#actionDiv").find("a").length) {
        	$("#actionDiv").show();
        } else {
        	$("#toolBarDiv").css({'height': '0', 'border-top': 'none'});
        }
        
        $("#toolBarDiv").show();
	});
    
    function audit() {
    	var applicationId = "";

        $("input[name='applicationId']:checked").each(function() {
            applicationId = $(this).val();
        });
        
		if(isBlankString(applicationId)) {
			$.messager.alert('提示', '请选中要复核的事件!', 'warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/timeApplication/toAudit.jhtml?applicationId=" + applicationId + "&listType=" + $('#listType').val();
			
		  	openJqueryWindowByParams({
		  		title: "复核事件信息",
		  		maxWidth: 480,
		  		maxHeight: 280,
		  		targetUrl: url
		  	});
		}
	}
	
	function recovery(operateName, serviceName) {
		var eventId = "", selectedItem = $('#list').datagrid('getSelected');
		operateName = operateName || '恢复';
		serviceName = serviceName || 'timeApplicationCallback4SCUMYPQInvalidEventService';
		
		if(selectedItem) {
			eventId = selectedItem.eventId;
		}
        
		if(isBlankString(eventId)) {
			$.messager.alert('提示', '请选中要' + operateName + '的事件！', 'warning');
		} else {
			$.messager.confirm('提示', '您确定' + operateName + '选中的事件吗？', function(r) {
				if(r) {
					var url = '${rc.getContextPath()}/zhsq/timeApplication/saveTimeApplication.jhtml',
						reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" method="post"></form>';
					
					modleopen();
					
					$("#jqueryToolbar").append($(reportForm));
					$("#_report4eventForm").append($('<input type="hidden" name="applicationId" value="' + selectedItem.applicationId + '" />'));
					$("#_report4eventForm").append($('<input type="hidden" name="checkId" value="' + selectedItem.checkId + '" />'));
					$("#_report4eventForm").append($('<input type="hidden" name="timeAppCheckStatus" value="4" />'));
					$("#_report4eventForm").append($('<input type="hidden" name="serviceName" value="' + serviceName + '" />'));
					
					$("#_report4eventForm").attr('action', url);
					
					$("#_report4eventForm").ajaxSubmit(function(data) {
						if(data.success == true) {
							$.messager.alert('提示', '事件' + operateName + '操作成功！', 'info');
						} else {
							$.messager.alert('错误', '该事件已' + operateName + '，无需重复操作！', 'error');
						}
						
						searchData(true);
						
						modleclose();
					});
					
					$("#_report4eventForm").remove();
				}
			});
		}
	}
	
	function selectItem(index, rec) {
    	var timeAppCheckStatus = rec.timeAppCheckStatus;
    	
    	if(timeAppCheckStatus && timeAppCheckStatus == '3') {
    		$("#audit").show();
    	} else {
    		$("#audit").hide();
    	}
    }
    
    function detail(eventId) {
		if(eventId == "") {
			$.messager.alert('警告','请选中要查看的事件!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&isIgnoreStatus=true&eventId=" + eventId;
			
		  	openJqueryWindowByParams({
		  		title: "查看事件信息",
		  		targetUrl: url
		  	});
		}
    }
	
	<@block name="function_resetCondition">
	function resetCondition() {//重置
		<@block name="function_resetCondition_body">
		createTimeDateRender.doClear();
		timeAppCreateTimeDateRender.doClear();
		
		$('#timeAppQueryForm')[0].reset();
		$('#eventKeyWord').addClass('keyBlank');
		
		<@block name="function_resetCondition_after_reset"></@block>
		
		searchData();
		</@block>
	}
	</@block>
	
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

	//删除事件数据详情报表

    function detailData(reportType) {
    	reportType = reportType || 1;
        var url = "${rc.getContextPath()}/zhsq/timeApplicationReportFordelEvent/toReportForDelEvent.jhtml?reportType=" + reportType;

        openJqueryWindowByParams({
            title: "<@block name="detailDataReportWindowTitle">事件删除统计</@block>",
			padding_top:0,
			padding_left:0,
			padding_bottom:0,
			padding_right:0,
            targetUrl: url
        });
    }
</script>