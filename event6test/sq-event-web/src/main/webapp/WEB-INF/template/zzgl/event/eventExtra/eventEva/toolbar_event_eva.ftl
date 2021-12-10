<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/orgtreeSet.js"></script>

<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="eventEvaQueryForm">
		<input id="evaType" name="evaType" type="hidden" class="queryParam" value="${evaType!}"/>
		
		<div class="ConSearch">
	        <div class="fl" id="conSearchFlDiv">
	        	<ul>
	        		<input id="infoOrgCode" name="eInfoOrgCode" type="text" class="hide queryParam" value=""/>
	                
	                <li class="eventCreateTimeLi">采集时间：</li>
	                <li class="eventCreateTimeLi">
	                	<input class="inp1 hide queryParam" type="text" id="createDayStart" name="createDayStart" value="${createDayStart!}"></input>
	                	<input class="inp1 hide queryParam" type="text" id="createDayEnd" name="createDayEnd" value="${createDayEnd!}"></input>
	                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:195px;" value="${createDayStart!}<#if createDayStart?? && createDayEnd??> ~ </#if>${createDayEnd!}"/>
	                </li>
	            	<li>关键字：</li>
	                <li><input name="keyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="事件描述/标题/事发详址" defaultValue="事件描述/标题/事发详址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事发时间：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value=""></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value=""></input>
	                                    			<input type="text" id="_happenTimeDateRender" class="inp1 InpDisable dateRenderWidth" value=""/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>评价等级：</span></label>
	                                    			<input type="text" id="evaLevel" name="evaLevel" class="hide queryParam" />
	                                    			<input type="text" class="inp1 w150" id="evaLevelName" />
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
    var orderByFieldComboBox = null,
    	gridTree = null;//网格树

	$(function(){
		//初始化网格树
    	gridTree = new OrgTreeSet({
			treStyle:"${treStyle!'top'}",
			layoutId:'easyuiLayoutBody',
			topSearchUl:'conSearchFlDiv',
			startGridName:"${gridName!''}",
			dataDomian:'${COMPONENTS_URL}',
			defaultOrgCode:'${defaultInfoOrgCode!}',
			onClickCB:function(rec){
				$('#infoOrgCode').val(rec.attributes.eOrgCode);
				if('left' == '${treStyle!}'){
					searchData();
				}
			},
			resetCB:function(){
				if('left' != '${treStyle!}'){
			    	infoOrgCode = '${infoOrgCode!}';
				}
			}
			<#if startGridId??>,startgridId : ${startGridId?c}</#if>
		});
		
		createTimeDateRender = $('#_createTimeDateRender').anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#createDayStart").val(api.getStartDate());
					$("#createDayEnd").val(api.getEndDate());
				},
				OnCleared : function() {
					$("#createDayStart").val('');
					$("#createDayEnd").val('');
				}
			}
		}).anoleDateApi();
		
		AnoleApi.initListComboBox("evaLevelName", "evaLevel", null, null, null, {
        	DataSrc : [{"name":"1星", "value":"1"},{"name":"2星", "value":"2"},{"name":"3星", "value":"3"},{"name":"4星", "value":"4"},{"name":"5星", "value":"5"}],
        	RenderType : "01",
        	ShowOptions:{
                EnableToolbar : true
            }
        });
	});
	
	function detail(eventId, evaType) {
		if(eventId) {
			evaType = evaType || $("#evaType").val();
			
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=history&eventId=" + eventId + "&evaType=" + $('#evaType').val();
			
			openJqueryWindowByParams({
				maximizable: true,
				maxWidth: 1000,
				title: "查看事件信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选中查看的事件再执行此操作!','warning');
		}
	}
	
	function selectItem(index, rec) {
    	if(isAble2Eva()) {
    		$("#evaEvent4Batch").show();
    	} else {
    		$("#evaEvent4Batch").hide();
    	}
    }
    
	function evaEvent4Batch() {//批量评价操作
		var selectedItems = $('#list').datagrid('getSelections');
		
		if(selectedItems.length == 0) {
			$.messager.alert('警告', '请选择需要进行批量评价操作的事件！', 'warning');
		} else if(selectedItems.length > 20) {
			$.messager.alert('警告', '已选中的事件不得超过20条！', 'warning');
		} else {
			if(!isAble2Eva()) {
				$.messager.alert('警告', '已选中的事件里包含了已评价的事件！', 'warning');
				return;
			}
			
			$.messager.confirm('提示', '您确定对已选中的事件进行批量评价操作？', function(r) {
				if(r) {
					var eventIdStr = '';
					
					for(var index in selectedItems) {
						eventIdStr += ',' + selectedItems[index].eventId;
					}
					
					if(eventIdStr) {
						var url = '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/toEvaluateEvent.jhtml',
							reportWinId = showMaxJqueryWindow("评价事件信息", '', 750, 300),
							reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post"></form>';
							
						
						$("#jqueryToolbar").append($(reportForm));
						$("#_report4eventForm").append($('<input type="hidden" id="_eventIdStr" name="eventIdStr" value="" />'));
						$("#_report4eventForm").append($('<input type="hidden" name="evaType" value="' + $("#evaType").val() + '" />'));
						$("#_report4eventForm").append($('<input type="hidden" name="isEvaContentRequired" value="false" />'));
						
						$("#_eventIdStr").val(eventIdStr.substr(1));
						$("#_report4eventForm").attr('action', url);
						
						$("#_report4eventForm").submit();
						$("#_report4eventForm").remove();
					}
				}
			});
		}
	}
	
	function isAble2Eva(selectedItems) {//是否可进行批量评价操作验证
    	var isAble2Eva = true;
    	
    	if(!selectedItems) {
    		selectedItems = $('#list').datagrid('getSelections');
    	}
    	
    	if(selectedItems.length > 0) {
    		var evaType = $('#evaType').val(),
    			evaLevel = null;
    		
    		for(var index in selectedItems) {
    			switch(evaType) {
		    		case '1': {
		    			evaLevel = selectedItems[index].streetEvaLevel;
		    			break;
		    		}
		    		case '2': {
		    			evaLevel = selectedItems[index].districtEvaLevel;
		    			break;
		    		}
		    	}
		    	
		    	if(evaLevel) {
		    		isAble2Eva = false;
		    		break;
		    	}
    		}
    	}
    	
    	return isAble2Eva;
    }
    
	function resetCondition() {//重置
		clear4DateRender();
		
		$('#eventEvaQueryForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		<#if 'left' != treStyle>
			gridTree.reset();
			$("#infoOrgCode").val('');
		</#if>
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventEvaQueryForm .queryParam").each(function() {
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