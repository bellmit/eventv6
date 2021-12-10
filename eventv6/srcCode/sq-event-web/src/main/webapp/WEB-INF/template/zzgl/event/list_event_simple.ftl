<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
	<script type="text/javascript" src="${uiDomain}/web-assets/extend/js/excelExportBaseOnPage.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/orgtreeSet.js"></script>
	<style type="text/css">
		.width65px{width:105px;}
		.selectWidth{width: 159px;}
		.w150{width:130px;}
		.keyBlank{color:gray;}
		.dateRenderWidth{width: 195px;}
		/*图标选中凹陷效果只有在ie9及其以上才有效果*/
		.icon_select{width:100px;background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
		.icon_unselect{width:100px;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	</style>
</head>

<body class="easyui-layout" id="layoutArea">
	<div class="MainContent">
		<div id="jqueryToolbar">
			<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value="${infoOrgCode!''}"/>
			<form id="eventCaseQueryForm">
				<input id="listType" name="listType" type="text" class="hide queryParam" value="${listType!''}"/>
				
				<div class="ConSearch">
			        
			        <div class="fl" id="topSearchUl">
			        	<ul>
			            	<li class="eventCreateTimeLi">采集时间：</li>
			                <li class="eventCreateTimeLi">
			                	<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!}"></input>
			                	<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd" value="${createTimeEnd!}"></input>
			                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:195px;" value="${createTimeStart!}<#if createTimeStart?? && createTimeEnd??> ~ </#if>${createTimeEnd!}"/>
			                </li>
			                <li>关键字：</li>
		                    <li><input name="keyWord" style="width:165px" type="text" class="inp1 queryParam" id="keyWord" placeholder="事件详情/标题/发生详址" value="<#if keyWord??>${keyWord}</#if>" /></li>
			        		<li style="position:relative;">
			            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
			            		<div id="_advanceSearchDiv" class="AdvanceSearch DropDownList hide" style="width:410px; top: 42px; left: -130px;">
			                        <div class="LeftShadow">
			                            <div class="RightShadow">
			                                <div class="list NorForm" style="position:relative;">
			                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                                            <tr id="eventTypeTr">
			                                    		<td>
			                                    			<label class="LabName width65px"><span>事件分类：</span></label>
			                                    			<input id="type" name="type" type="text" value="" class="queryParam hide"/>
			                                    			<input id="typeName" name="typeName" type="text" class="inp1 selectWidth"/>
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
                                            				<label class="LabName width65px"><span>办结期限：</span></label>
                                            				<input class="inp1 hide queryParam" type="text" id="handleDateDayStart" name="handleDateDayStart" value="${handleDateDayStart!}"></input>
                                            				<input class="inp1 hide queryParam" type="text" id="handleDateDayEnd" name="handleDateDayEnd" value="${handleDateDayEnd!}"></input>
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
		                                                <td>
		                                                    <label class="LabName width65px"><span>采集渠道：</span></label>
		                                                    <input type="text" id="collectWays" name="collectWay" class="hide queryParam"/>
		                                                    <input type="text" id="collectWaysName" class="inp1 selectWidth" />
		                                                </td>
		                                            </tr>
				                                    <tr>
				                                        <td>
				                                            <label class="LabName width65px"><span>信息来源：</span></label>
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
		                                                <td>
		                                                    <label class="LabName width65px"><span>紧急程度：</span></label>
		                                                    <input type="text" id="urgencyDegrees" name="urgencyDegree" class="hide queryParam"/>
		                                                    <input type="text" id="urgencyDegreesName" class="inp1 selectWidth" />
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
			        <div id="toolFrDIV" class="tool fr"><@actionCheck></@actionCheck>
			          <!--<a href="#" id="delete" class="NorToolBtn DelBtn" onclick="del();">删除</a>
		              <a href="#" id="edit" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
		              <a href="#" id="add" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
			        </div>
			    </div>
			</form>
		</div>
	</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>
<script>

	var startInfoOrgCode = '${infoOrgCode!}';
	var orgtreeset;
	var treStyle="${treStyle!'top'}";
	var listType="${listType!''}";
	var defaultShowWindowHeight=560;
	var createTimeDateRender = null,
        happenTimeDateRender = null,
        handleDateRender = null,
        finTimeDateRender = null;
	
	
	$(function(){
		
		orgtreeset = new OrgTreeSet({
			treStyle:"${treStyle!'top'}",
			layoutId:'layoutArea',
			topSearchUl:'topSearchUl',
			startGridName:'${gridName}',
			dataDomian:'${COMPONENTS_URL}',
			defaultOrgCode:startInfoOrgCode,
			startgridId : '${startGridId}',
			onClickCB:function(rec){
				$('#infoOrgCode').val(rec.attributes.orgCode);
				if('left'=='${treStyle}'){
					searchData();
				}
			},
			resetCB:function(){
				if('left'!='${treStyle}'){
			    	$('#infoOrgCode').val("${infoOrgCode}");
				}
			}
		});
		
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
		
		
		<#if statuss??>
    		var statusArr = "${statuss}".split(',');
    		if($('#statuss').val()!=null) {
    			$('#statuss').combobox('setValues', statusArr);
    		}
    	</#if>
    	
       	AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, {
            ChooseType : "1" ,
            EnabledSearch : true,
            ShowOptions: {
                EnableToolbar : true
            }
        });

        AnoleApi.initTreeComboBox("collectWaysName", "collectWays", "A001093096", null, null, {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });

        AnoleApi.initTreeComboBox("sourcesName", "sources", "A001093222", null, null, {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        
        AnoleApi.initTreeComboBox("influenceDegreesName", "influenceDegrees", "A001093094", null, null, {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        
        AnoleApi.initTreeComboBox("urgencyDegreesName", "urgencyDegrees", "A001093271", null, null, {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });

	});
	

	function resetCondition() {//重置
		clear4DateRender();
		
		$('#eventCaseQueryForm')[0].reset();
		
		var queryParams=queryData();
		$("#list").datagrid('options').queryParams = queryParams;
		
		<#if 'left'!=treStyle>
			orgtreeset.reset();
			$("#infoOrgCode").val(startInfoOrgCode);
		</#if>
		
		<#if 'todo'==listType>
			$("#infoOrgCode").val('');
		</#if>

		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventCaseQueryForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		searchArray["infoOrgCode"]=$("#infoOrgCode").val();
		
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
    
	//按钮控制
	function authority(selectedRow) {
		
	}
	
	
	
	/*****************************************业务方法**********************************************/
	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
        if(!eventId){
            $.messager.alert('提示','请选择一条记录！','info');
        }else{
            var url = "${rc.getContextPath()}/zhsq/szzg/eventController/toSimpleEventDetail.jhtml?instanceId="+instanceId+"&eventId="+eventId+"&cachenum=" + Math.random();
                
            showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
        }
    }

    
    $(function(){
			
    	loadDataList();
    	
    });
    
    function loadDataList(){
    
    	var queryParams = queryData();
    
		$('#list').datagrid({
        	idField: 'teamId',
            width:600,
			height:300,
			nowrap: true,
			remoteSort:false,
			rownumbers:true,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			pagination:true,
			pageSize: 20,
			toolbar:'#jqueryToolbar',
			url:'${rc.getContextPath()}/zhsq/szzg/eventController/getSimpleListData.jhtml?listType=5',
			queryParams:queryParams,
            columns: [[
                
                {field:'eventId',align:'center',checkbox:true,hidden:true},//
                {field:'infoOrgCode',align:'center',checkbox:true,hidden:true},//
                {field:'gridId',align:'center',checkbox:true,hidden:true},//
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="showDetailRow(\'' + rec.eventId + '\',\''+rec.instanceId+'\')")>'+value+'</a>';
						return f;
					}	
				},
				{field:'happenTimeStr',title:'事发时间',halign:'left',align:'left',width:fixWidth(0.15),formatter: titleFormatter},//
				{field:'handleDateStr',title:'办结期限', halign:'left',align:'left',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
				{field:'eventClass',title:'事件分类', halign:'left',align:'left',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
				{field:'gridPath',title:'所属网格', halign:'left',align:'left',width:fixWidth(0.2),sortable:true,formatter: titleFormatter},
				{field:'statusName',title:'当前状态', halign:'left',align:'left',width:fixWidth(0.1),sortable:true,formatter: titleFormatter}

            ]],
            onLoadError: function () {//数据加载异常
        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
    		},
    		onSelect: function(index,rec) {
				authority(rec);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
             
        });
        
        //设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
        
    }
    
    function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	
    function titleFormatterGrid(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ rowData.gridPath +'" >'+ value +'</span>';
		}
		
		return title;
	}
	

	
	

</script>
</body>
</html>