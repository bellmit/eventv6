<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>

<style type="text/css">
	.width65px{width:75px;}
	.selectWidth{width: 135px;}
	.dateRenderWidth{width: 195px;}
	.keyBlank{color:gray;}
	.advancedSel{width:138px; height:28px;}
	.advancedInp{width: 135px;}
	/*图标选中凹陷效果只有在ie9及其以上才有效果*/
	.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
</style>
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="eventTaskForm">
			<input type="hidden" id="startInfoOrgCode" value="${infoOrgCode!}" />
			<input type="hidden" id="eventType" name="eventType" value="all" class="queryParam" />
			<input type="text" id="orderByField" name="orderByField" class="queryParam hide" value=""/>
			<@block name="extraFormAttributes"></@block>
			
	        <div class="fl">
	        	<ul>
	        		<li><@block name="labelName4QueryGrid">所属网格：</@block></li>
	                <li>
	                	<input id="gridLevel" type="text" class="hide" value="${startGridLevel!}" />
	                	<input type="text" id="infoOrgCode" name="eInfoOrgCode" class="queryParam hide"/>
	                	<input type="text" id="gridId" name="gridId" class="queryParam hide"/>
	                	<input type="text" id="gridName" name="gridName" class="inp1 InpDisable" style="width: 150px;" />
	                </li>
	                <li>采集时间：</li>
	                <li>
	                	<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!}"></input>
	                	<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd" value="${createTimeEnd!}"></input>
	                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable dateRenderWidth" value="${createTimeStart!}<#if createTimeStart?? && createTimeEnd??> ~ </#if>${createTimeEnd!}"/>
	                </li>
	            	<li>关键字：</li>
	                <li><input type="text" id="keyWord" name="keyWord" class="inp1 keyBlank queryParam" style="width:150px;" value="事件描述/标题/事发详址" defaultValue="事件描述/标题/事发详址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:372px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="*overflow-x:hidden; position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事件分类：</span></label>
	                                    			<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                                    			<input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:135px;"/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>事件编号：</span></label><input type="text" id="code" name="code" class="inp1 advancedInp queryParam" ></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事件状态：</span></label>
	                                    			<select id="status" class="queryParam advancedSel easyui-combobox" style="height: 28px;" data-options="editable: false, panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
	                                    				<#if statusDC??>
	                                    					<option value="">不限</option>
	                                    					<#list statusDC as l>
	                                    						<#if l.dictGeneralCode != "05" && l.dictGeneralCode != "06" && l.dictGeneralCode != "99">
	                                    							<option value="${l.dictGeneralCode}">${l.dictName}</option>
	                                    						</#if>
	                                    					</#list>
	                                    				</#if>
	                                    			</select>
	                                    		</td>
	                                    	</tr>
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
	                                    			<label class="LabName width65px"><span>办结期限：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="handleDateDayStart" name="handleDateDayStart" value="${handleDateDayStart!''}"></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="handleDateDayEnd" name="handleDateDayEnd" value="${handleDateDayEnd!''}"></input>
	                                    			<input type="text" id="_handleDateRender" class="inp1 InpDisable dateRenderWidth" value="${handleDateDayStart!}<#if handleDateDayStart?? && handleDateDayEnd??> ~ </#if>${handleDateDayEnd!}"/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>结案时间：</span></label>
	                                    			<input class="inp1 hide queryParam" type="text" id="finTimeStart" name="finTimeStart" value=""></input>
	                                    			<input class="inp1 hide queryParam" type="text" id="finTimeEnd" name="finTimeEnd" value=""></input>
	                                    			<input type="text" id="_finTimeDateRender" class="inp1 InpDisable dateRenderWidth" value=""/>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>采集人员：</span></label><input class="inp1 advancedInp queryParam" type="text" id="contactUser" name="contactUser"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>采集渠道：</span></label>
							                    	<input type="text" id="collectWays" name="collectWay" class="hide queryParam"/>
	                                    			<input type="text" id="collectWaysName" class="inp1 selectWidth" />
							                    </td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>信息来源：</span></label>
							                    	<input type="text" id="sources" name="source" class="hide queryParam"/>
	                                    			<input type="text" id="sourcesName" class="inp1 selectWidth" />
							                    </td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
							                    	<label class="LabName width65px"><span>影响范围：</span></label>
							                        <input type="text" id="influenceDegrees" name="influenceDegree" class="hide queryParam"/>
	                                    			<input type="text" id="influenceDegreesName" class="inp1 selectWidth" />
							                    </td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>紧急程度：</span></label>
							                        <input type="text" id="urgencyDegrees" name="urgencyDegree" class="hide queryParam"/>
	                                    			<input type="text" id="urgencyDegreesName" class="inp1 selectWidth" />
							                    </td>
	                                    	</tr>
	                                    	<tr id="attrFlagTr">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>附件类型：</span></label>
	                                    			<input type="text" id="attrFlags" name="attrFlag" class="hide queryParam"/>
	                                    			<input type="text" id="attrFlagsName" class="inp1 selectWidth" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办理人员：</span></label>
	                                    			
	                                    			<input type="text" id="curUserIds" name="curUserIds" class="queryParam hide"/>
	                                    			<input type="text" id="curUserOrgIds" name="curUserOrgIds" class="queryParam hide"/>
	                                    			<input type="text" id="curUserOrgNames" class="hide"/>
	                                    			
	                                    			<input type="text" id="curUserNames" class="inp1 InpDisable advancedInp" style="cursor: pointer;" onclick="_selectUserAll();" readonly />
	                                    		</td>
	                                    	</tr>
	                                    	<@block name="curOrgIdsTr">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办理单位：</span></label>
	                                    			<input id="curOrgIds" name="curOrgIds" value="" class="queryParam hide"/>
	                                    			<input type="text" class="inp1 InpDisable" style="width: 135px;" name="curOrgNames" id="curOrgNames" value=""/>
	                                    		</td>
	                                    	</tr>
	                                    	</@block>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办理情况：</span></label>
	                                    			<input type="text" id="taskStatus" name="taskStatus" class="hide queryParam"/>
	                                    			<input type="text" id="taskStatusName" class="inp1 selectWidth" />
	                                    		</td>
	                                    	</tr>
	                                    	
	                                    	<@block name="extendCondition"></@block>
	                                    </table>
	                                </div>
	                                
	                                <div class="list NorForm">
		                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td style="*padding-bottom: 5px;">
	                                    			<a href="###" class="BigNorToolBtn BigJieAnBtn" style="float: right;" onclick="conditionSearch()">确定</a>
							                    </td>
							                    <td>
							                    	<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeAdvanceSearch()">关闭</a>
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
	            	<li><a href="###" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
	            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
        </form>
        
        <div class="clear"></div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
    		
        <div class="tool fr">
        </div>
    </div>
	
</div>

<script type="text/javascript">
	<!--人员选择使用参数-->
	var g_EventNodeCode = {},
		createTimeDateRender = null,
		happenTimeDateRender = null,
		handleDateRender = null,
		finTimeDateRender = null;
	
	$(function(){
		if($('#_createTimeDateRender').length > 0) {
			createTimeDateRender = init4DateRender('_createTimeDateRender', 'createTimeStart', 'createTimeEnd');
		}
		if($('#_happenTimeDateRender').length > 0) {
			happenTimeDateRender = init4DateRender('_happenTimeDateRender', 'happenTimeStart', 'happenTimeEnd');
		}
		if($('#_handleDateRender').length > 0) {
			handleDateRender = init4DateRender('_handleDateRender', 'handleDateDayStart', 'handleDateDayEnd');
		}
		if($('#_finTimeDateRender').length > 0) {
			finTimeDateRender = init4DateRender('_finTimeDateRender', 'finTimeStart', 'finTimeEnd');
		}
		
		var gridOpt = {
			OnCleared: function() {
				$("#infoOrgCode").val('');
			},
			ShowOptions: {
				EnableToolbar : true
			}
		};
		
		<@block name="extraGridOption"></@block>
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.eOrgCode);
				$('#gridLevel').val(grid.gridLevel);
			} 
		}, gridOpt);
		
		AnoleApi.initTreeComboBox("collectWaysName", "collectWays", "A001093096", null, null, {
			RenderType : "01",
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initTreeComboBox("sourcesName", "sources", "A001093222", null, null, {
			RenderType : "01",
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initTreeComboBox("influenceDegreesName", "influenceDegrees", "A001093094", null, null, {
			RenderType : "01",
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initTreeComboBox("urgencyDegreesName", "urgencyDegrees", "A001093271", null, null, {
			RenderType : "01",
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initTreeComboBox("attrFlagsName", "attrFlags", "B063", null, null, {
			RenderType : "01",
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initListComboBox("taskStatusName", "taskStatus", null, null, null, {
			DataSrc : [{"name":"未结束", "value":"2"}, {"name":"已结束", "value":"1"}],
			ShowOptions:{
				EnableToolbar : true
			}
		});
		
		AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, {
			ChooseType : "1",
			EnabledSearch : true,
			ShowOptions: {
				EnableToolbar : true
			}
		});
		
		if($('#curOrgIds').length > 0) {
			AnoleApi.initFuncOrgZtreeComboBox("curOrgNames", "curOrgIds", null, {
				Async : {
					enable		: true,
					autoParam	: [ "id=orgId", "professionCode" ],
					dataFilter	: _filter,
					otherParam	: {
						"rootOrgId"	: '<#if orgRootId??>${orgRootId?c}</#if>',
						"type"		: -1
					}
				},
				OnCleared : function() {
					$("#curOrgIds").val("");
				},
				ShowOptions : {
					EnableToolbar : true
				}
			});
		}
		
		<@block name="extendConditionInit"></@block>
		
		$(document).off("click");//移除原有的点击事件
	});
	
	function _selectUserAll() {//办理人员选择条件
		g_EventNodeCode.nodeCode = "${nodeCode!}";
		
		selectUserByObj({
			nextUserIdElId 		: 'curUserIds',
			nextUserNameElId 	: 'curUserNames',
			nextOrgIdElId 		: 'curUserOrgIds',
			nextOrgNameElId 	: 'curUserOrgNames',
			nextNodeId 			: '0',
			nextNodeType 		: 'task',
			userSelectedLimit 	: 1
		});
	}
	
	function detail(eventId, instanceId){
		if(eventId) {
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&instanceId="+instanceId+"&eventId="+eventId+"&cachenum=" + Math.random();
			
			<@block name="eventDetailExtraParam"></@block>
			
			showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
		} else {
			$.messager.alert('警告','请选择一条事件！','warning');
		}
	}
	
	function resetCondition() {//重置
		createTimeDateRender.doClear();
		
		$('#eventTaskForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		$(".advancedSel").each(function() {
			$(this).combobox('setValue', "")
				   .combobox('setText', '不限 ');//为了使得能够显示"不限"，其后添加了一个空格
		});
		
		$("#iconDiv > a").eq(0).click();
	}
	
	function conditionSearch(){//点击查询按钮
		var iconDiv = $("#iconDiv > a[class='icon_select']");
		if(iconDiv.length > 0) {
			iconDiv.eq(0).click();
		} else {
			searchData();
		}
	}
	
	function searchData(queryArray){//查询
		var searchArray = queryData(queryArray);
		
		doSearch(searchArray);
	}
    
    function doSearch(queryParams){
    	closeAdvanceSearch()
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	function queryData(queryArray) {
        var searchArray = {};
        
        if(isNotBlankParam(queryArray)) {
			searchArray = queryArray;
		}

        $("#eventTaskForm .queryParam").each(function() {
			var val = $(this).val();
			var key = $(this).attr("name");
			
			if($(this).hasClass("advancedSel")) {
				val = $(this).combobox('getValues').toString();
				key = $(this).attr('id');
			} else if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		if(isBlankString(searchArray.eInfoOrgCode)) {
			searchArray["eInfoOrgCode"] = $("#startInfoOrgCode").val();
		}
		
		if(isBlankString(searchArray.status) || isNotBlankString(searchArray.taskStatus)) {
			var eventStatus = null,
				  taskStatus = searchArray.taskStatus;
			
			switch(taskStatus) {
				case "1" : {
					eventStatus = "04"; break;
				}
				case "2" : {
					eventStatus = comboboxOptionVal("status", "04"); break;
				}
			}
			
			if(eventStatus) {
				searchArray["status"] = eventStatus;
			}
		}
		
        return searchArray;
    }
	
	function closeAdvanceSearch() {//隐藏高级查询
		$(".AdvanceSearch").fadeOut();
	}
	
	<@block name="function_iconSearchData">
	function iconSearchData(index, obj) {//点击搜索图标
		<@block name="function_iconSearchData_body">
		var searchArray = new Array();
		
		iconSelect(obj);//选中点击的图标
		switch(index) {
			case 0: {//点击所有图标
				break;
			}
			case 1: {//点击紧急图标
				var urgencyDegree = "02,03,04";
				
				if(urgencyDegree) {
					searchArray["urgencyDegree"] = urgencyDegree;
				}
				
				break;
			}
			case 2: {//点击将到期图标
				searchArray["handleDateFlag"] = "2";
				break;
			}
			case 3: {//点击已过期图标
				searchArray["handleDateFlag"] = "3";
				break;
			}
			case 4: {//点击正常图标
				searchArray["handleDateFlag"] = "1";
				break;
			}
		}
		
		searchData(searchArray);
		</@block>
	}
	</@block>
	
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
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			conditionSearch();
		}
	}
	
	function comboboxSelect(record, id, exceptValue){//选中下列框选项
		var exceptAttr = [];
		
		if(exceptValue==undefined || exceptValue==null){
			exceptValue = "";
			exceptAttr[0] = "";
		}else if(typeof exceptValue == 'string'){
			exceptAttr = exceptValue.split(',');
		}
		
		if(exceptValue.indexOf(record.value) != -1){
			$('#'+id).combobox("setValue", record.value);
		}else{
			for(var index = 0, len = exceptAttr.length; index < len; index++){
				$('#'+id).combobox("unselect", exceptAttr[index]);
			}
		}
	}
	
	function comboboxUnselect(record, id){//取消选中下列框选项
		var values = $('#'+id).combobox("getValues");
		if(values.length == 0){
			$('#'+id).combobox("setValue", "");
		}
	}
	
	//获取指定select的option的值 selectId指定的select的id， exceptValue需要排除的值
	//返回值为：各值以英文逗号相连
	function comboboxOptionVal(selectId, exceptValue) {
		var selectOpts = $("#"+selectId).combobox("getData"),
			opt = null,
			optVal = "",
			selectVal = "";
		
		for(var index = 0, len = selectOpts.length; index < len; index++) {
			opt = selectOpts[index];
			optVal = opt.value;
			
			if(optVal && (!exceptValue || exceptValue && optVal != exceptValue)) {
				selectVal += "," + optVal;
			}
		}
		
		if(selectVal) {
			selectVal = selectVal.substring(1);
		}
		
		return selectVal;
	}
	
    function iconSelect(obj){//为选择的图标增添凹陷效果
    	if(isNotBlankParam(obj)){
    		iconUnSelect();
    		$(obj).addClass('icon_select');
    	}
    }
    
    function iconUnSelect(){//去除图片的凹陷效果
    	$("#iconDiv > a[class='icon_select']").each(function(){
			$(this).removeClass('icon_select');
		});
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
    	if(handleDateRender != null) {
    		handleDateRender.doClear();
    	}
    	if(finTimeDateRender != null) {
    		finTimeDateRender.doClear();
    	}
    }
</script>