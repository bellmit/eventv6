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
	<form id="eventExtendQueryForm">
		<input type="hidden" class="queryParam" id="listType" name="listType" value="" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属网格：</li>
	                <li>
	                	<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
	                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable w150" />
	                </li>
	            	<li>事件分类：</li>
	                <li>
	                	<input id="eventType" name="eventType" type="text" value="" class="queryParam hide"/>
	                	<input id="eventTypeName" type="text" class="inp1 InpDisable w150" />
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
	                                    			<label class="LabName width65px"><span>排序字段：</span></label>
	                                    			<input type="text" id="orderByField" name="orderByField" class="queryParam hide" />
	                                    			<input type="text" id="orderByFieldInput" class="hide" />
	                                    			<input class="inp1" type="text" id="orderByFieldName" style="width:135px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr id="orderByFieldRadioTr" class="hide">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>排序方式：</span></label>
	                                    			<div class="Check_Radio" style="margin-right: 40px;"><input type="radio" name="orderByRadio" id="orderByRadioAsc" value="ASC" checked onclick="radionCheck('ASC')"></input><label for="orderByRadioAsc" style="cursor: pointer;">升序</label></div>
	                                    			<div class="Check_Radio"><input type="radio" name="orderByRadio" id="orderByRadioDesc" value="DESC" onclick="radionCheck('DESC')"></input><label for="orderByRadioDesc" style="cursor: pointer;">降序</label></div>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 queryParam" type="text" id="code" name="code" style="width:135px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事发时间：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
	                                    			<input type="text" id="_happenTimeDateRender" class="inp1 InpDisable dateRenderWidth" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>采集时间：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value=""></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd" value=""></input>
	                                    			<input type="text" id="_createTimeDateRender" class="inp1 InpDisable dateRenderWidth" value=""/>
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
	            	<li><a href="###" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
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
    var orderByFieldComboBox = null,
    	createTimeDateRender = null,
        happenTimeDateRender = null;

	$(function() {
		//构造链接中传来的参数，用于传入列表查询
		<#if extraParam??>
			<#list extraParam?keys as mapKey>
				var inputObj = $('#eventExtendQueryForm input[name="${mapKey}"]');
				if(inputObj.length) {
					inputObj.val('${extraParam[mapKey]}');
				} else {
					$("#eventExtendQueryForm").prepend('<input type="hidden" class="queryParam" name="${mapKey}" value="${extraParam[mapKey]}" />');
				}
			</#list>
		</#if>
		
		AnoleApi.initTreeComboBox("eventTypeName", "eventType", "A001093199", null, null, { 
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
        
        orderByFieldComboBox = AnoleApi.initListComboBox("orderByFieldName", "orderByFieldInput", null, function(value) {
        		$("#orderByFieldRadioTr").show();
        		
        		radionCheck();
        	}, null, {
        	DataSrc : [{"name":"采集时间", "value":"CREATE_TIME"},{"name":"事发时间", "value":"HAPPEN_TIME"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	},
        	OnCleared: function() {
        		$("#orderByField").val("");
        		$("#orderByFieldRadioTr").hide();
        	}
        });
        
        if($('#_createTimeDateRender').length > 0) {
        	createTimeDateRender = init4DateRender('_createTimeDateRender', 'createTimeStart', 'createTimeEnd');
        }
        if($('#_happenTimeDateRender').length > 0) {
        	happenTimeDateRender = init4DateRender('_happenTimeDateRender', 'happenTimeStart', 'happenTimeEnd');
        }
		
        if($("#actionDiv").find("a").length) {
        	$("#actionDiv").show();
        } else {
        	$("#toolBarDiv").css({'height': '0', 'border-top': 'none'});
        }
        
        $("#toolBarDiv").show();
	});
	
    function radionCheck(orderType) {
    	var orderField = $("#orderByFieldInput").val();
    	
    	if(isBlankStringTrim(orderType)) {
    		orderType = $('#orderByFieldRadioTr input[name="orderByRadio"]:checked').eq(0).val();
    	}
    	
    	$("#orderByField").val(orderField + " " + orderType);
    }
    
	function detail(eventId) {
		if(eventId) {
			var eventType = "all",
				listType = $("#listType").val(),
				url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventId=" + eventId + "&eventType=";
			
			if(listType == '2' || listType == '4') {
				eventType = "todo";
			}
			
			url += eventType;
			
			openJqueryWindowByParams({
				maxWidth: 1000,
				title: "查看事件信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选中查看的事件再执行此操作!','warning');
		}
	}
	
	function addTimeApplication(eventId, applicationType) {
		var url = "${rc.getContextPath()}/zhsq/eventExtend/toAddTimeApplication.jhtml?applicationType=" + applicationType + "&eventId=" + eventId;
		
		var title = "督查督办";
		
		if(applicationType && (applicationType == '2' || applicationType == '3')) {
			title = "申请延时";
		}
		
		openJqueryWindowByParams({
			maxWidth: 480,
			maxHeight: 280,
			title: title,
			targetUrl: url
		});
	}
	
	function resetCondition() {//重置
		clear4DateRender();
		$('#eventExtendQueryForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventExtendQueryForm .queryParam").each(function() {
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
	
	function flashData() {//待办事件办理后，回调
		reloadDataForSubPage();
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
		if(createTimeDateRender != null) {
			createTimeDateRender.doClear();
		}
		if(happenTimeDateRender != null) {
			happenTimeDateRender.doClear();
		}
	}
</script>