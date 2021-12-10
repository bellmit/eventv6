<@override name="eventMenuPageTitle">
	南安市事件采集页面
</@override>
<@override name="extraJs">
	<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
	
	<#if isUsePostMessage?? && isUsePostMessage=='true'>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<style>
	.mt-tbody {max-height: 4.5rem;}
		.mask-c-bottom {margin-top: .5rem;}
		.FromTo{margin-top:-0.05rem}
		.TimeControl{font-size:12px;}
		.mf-item>input {background-color: rgbargba(6, 31, 52, .8) ! important;color: white ! important}
		.ztree{background-color: rgbargba(6, 31, 52, .8) !important;}
		.ztree *{color:white!important;}
		.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
		.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
		.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
		.swiper-pagination {height: 0.12rem;bottom: .1rem !important;}
		.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
		.aj_items_t_yellow {text-align: left;}
		.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
		.mCSB_inside > .mCSB_container{margin-right:.1rem}
		body,html{width:100%;height:100%;}
		.select-table .select-td {
    		border-bottom: 1px solid #39BBF8;
    		border-right: 1px solid #39BBF8;
    		background-color: #F3F8FE;
		}
		#leftPanel>.ztree * {
    		padding: 0;
    		margin: 0;
    		font-size: 12px;
    		font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    		color: #000!important;
    		background-color: white !important;
		}
		
		#leftTree{
			background-color: white !important;
		}
		
		.inp1 {
    		background-color: rgba(6, 31, 52, .8) !important;
    		border-radius: 0.04rem ;
   			border: solid 1px #2e7bec;
    		color: #fff !important;
		}
		.area1 {
    		background-color: rgba(6, 31, 52, .8) !important;
    		border-radius: 0.04rem ;
   			border: solid 1px #2e7bec;
    		color: #fff !important;
		}
		
		.BigTool {
    		background: transparent;
    		margin-top: -2rem;
		}
		
		.LabName {
    		color: white;
    	}
    	
    	.panel .LabName {
    		color: #7c7c7c;
    	}
    	
    	.panel .inp1 {
    		background-color: white !important;
    		border-radius: 2px ;
   			border: 1px solid #e1e8f2;
    		color: #000 !important;
		}
    	
    	.ConSearch .inp1 {
    		background-color: white !important;
    		border-radius: 2px ;
   			border: 1px solid #e1e8f2;
    		color: #000 !important;
		}
		
		.ztree li a.curSelectedNode {
   	 		background-color: rgba(10, 55, 157, .8) !important;
    		border: 1px rgba(10, 55, 157, .8) solid !important;
		}
		
		.AddressControl .con {
    		background: white;
		}

	</style>
	</#if>
</@override>
<@override name="gridNameTd">
	<input type="hidden" id="oldTypeVal" name="oldTypeVal" value="${event.type!}" />
	<td>
		<label class="LabName">
			<span>所属区域：</span>
		</label>
		<input type="text" class="inp1 InpDisable easyui-validatebox" style="width:122px;" data-options="required:true" id="gridName" name="gridName" value="${event.gridName!}" />
	</td>
</@override>
<@override name="eventOccurredInput">
	<tr id="buildingDiv" style="display: none">
		<td colspan="2" class="LeftTd">
			<label class="LabName">
				<span>楼栋名称：</span>
			</label>
			<input type="hidden" id="buildingId" name="buildingId" value="<#if event.buildingId??>${event.buildingId?c}</#if>" />
			<input type="hidden" id="buildingName" name="buildingName" value="${event.occurred!}" />
			<input type="hidden" id="buildingAddress" name="buildingAddress" value="${event.occurred!}" />
			<input type="text" id="building" class="comboselector" style="height: 28px; width: 405px;display: none" data-options="dType:'building', afterSelect:combo_afterSelect" query-params="orgCode=${event.gridCode!'-1'}" />
		</td>
	</tr>
	<tr id="occurredDiv">
		<td colspan="2" class="LeftTd">
			<label class="LabName">
				<span>
					<label class="Asterik">*</label>事发详址：
				</span>
			</label>
			<input type="text" class="inp1 easyui-validatebox" style="width:405px;"
				   data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"
				   name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" />
			<div id="occurredLabel" style="display: none" class="Check_Radio FontDarkBlue singleCellClass">${event.occurred!}</div>
		</td>
	</tr>
</@override>
<@override name="geographicalLabelingInput">

</@override>
<@override name="involvedNumInput">
	<tr id="geographicalLabelingInput" style="display: none">
		<td colspan="2">
			<label class="LabName"><span>地理标注：</span></label>
			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
		</td>
	</tr>
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span>涉及人数：</span></label>
			<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
			<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" />
		</td>
	</tr>
</@override>
<@override name="bigFileUploadInitOption">
	$.extend(bigFileUploadOpt, {
		finishEleRander: function() {
			var eventType ="${event.type!}";
			
			if(eventType) {
				changeLabelDict(eventType);
			}
		}
	});
	
	var bigViodeUploadOpt = $.extend({}, bigFileUploadOpt, {
		fileExt: '.mp4,.avi,.amr',
		labelDict:[{'name':'处理前', 'value':'1'}]
	}),
	videoUploadHtml = '<tr>' +
				      	'<td class="LeftTd">' +
				      		'<label class="LabName"><span>视频上传：</span></label><div id="bigVideoUploadDiv"></div>' +
				      	'</td>' +
					  '</tr>';
	
	$('#bigFileUploadTr').after(videoUploadHtml);
	
	bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
</@override>
<@override name="gridTreeInitMethod">
	<#if rootGridId?? && (rootGridId > 0)>
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#gridCode").val(grid.orgCode);
			}
		}, {
			Async : {
				enable : true,
				autoParam : [ "id=gridId" ],
				dataFilter : _filter,
				otherParam : {
					"startGridId" : ${rootGridId?c}
				}
			},
			ChooseType:"1",
			ShowOptions:{
					AllowSelectLevel:"5,6"
				}
		});
	<#else>
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#gridCode").val(grid.orgCode);
			}
		},{
			ChooseType:"1",
			ShowOptions:{
					AllowSelectLevel:"5,6"
				}
		});
	</#if>
</@override>
<@override name="initExpandScript">

	var locationOption ={
				_source : 'XIEJING',
				_show_level : 6,
				_startAddress :"${event.occurred!}",
				_select_scope : 0,
				_isSearchCode : 1,
				_customAddressIsNull : false,
				_addressMap : {
					_addressMapShow : true,
					_addressMapIsEdit : true
				},
				_startDivisionCode : "${START_DIVISION_CODE!}",
				BackEvents : {
					OnSelected : function(api) {
						var isLocated = api.addressData._addressMap._addressMapIsEdit || false,
							latitude = '', longitude = '', mapType = '5',
							showName = "标注地理位置";

						$("#occurred").val(api.getAddress());

						if(isLocated == true) {
							latitude = api.addressData._addressMap._addressMapX;
							longitude = api.addressData._addressMap._addressMapY;
							mapType = api.addressData._addressMap._addressMapType;
						}

						if(latitude && longitude) {
							showName = "修改地理位置";
						} else {
							latitude = '';
							longitude = '';
							mapType = '';
						}

						$('#x').val(latitude);
						$('#y').val(longitude);
						$('#mapt').val(mapType);
						$("#mapTab2").html(showName);
					},
				OnCleared : function(api) {
					//清空按钮触发的事件
					$("#occurred").val('');
					$('#x').val('');
					$('#y').val('');
					$('#mapt').val('');
					$("#mapTab2").html('标注地理位置');
				}
			},
				_limit_select_level : 4
	};

	<#if event.resMarker??>
		$.extend(locationOption._addressMap, {
			_addressMapX	: '${event.resMarker.x!}',
			_addressMapY	: '${event.resMarker.y!}',
			_addressMapType	: '${event.resMarker.mapType!}'
		});
	</#if>

	$("#occurred").anoleAddressRender(locationOption);

	<#--$("#occurred").anoleAddressRender({
		_source : 'XIEJING',
		_show_level : 6,
		_startAddress :"${event.occurred!}",
		_select_scope : 0,
		_isSearchCode : 1,
		_customAddressIsNull : false,
		_addressMap : {
			_addressMapShow : false,
		},
		_startDivisionCode : "${START_DIVISION_CODE!}",
		BackEvents : {
			OnSelected : function(api) {
				$("#occurred").val(api.getAddress());
			},
			OnCleared : function(api) {
				//清空按钮触发的事件
				$("#occurred").val('');
			}
		},
		_limit_select_level : 4
	});-->
	
	<#if isUsePostMessage?? && isUsePostMessage=='true'>
	$('.WdateDiv').css('background-color','rgba(7, 44, 86, .9) !important');
	$('#wholePageDiv').css({'background-color':'transparent','margin-top': '1rem'});
	$('#contactUser').css('width','220px');
	$('#tel').css('width','220px');
	$('#content').css('width','410px');
	$('html').css('overflow','hidden');
	</#if>
	//事件描述高度适配
	$('#content').css({'height':'112px'});
</@override>
<@override name="addEventMenuTableSubmitCrossOverHandler">
	var isUsePostMessage = '${isUsePostMessage!}';
	var messageUrl = '${messageUrl!}';
	if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
		window.parent.postMessage('${callBack!}(' + JSON.stringify(data) + ')', messageUrl);
	} else {
		<@super></@super>
	}
</@override>
<@override name="addEventMenuStartWorkflowCrossOverHandler">
	var isUsePostMessage = '${isUsePostMessage!}';
	var messageUrl = '${messageUrl!}';
	if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
		window.parent.postMessage('${callBack!}(' + JSON.stringify(data) + ')', messageUrl);
	} else {
		<@super></@super>
	}
</@override>
<@override name="extraSetting4eventType">
	OnChanged : function(eventType, items) {
		changeDict(eventType,items);
		changeLabelDict(eventType);
		changeContactUserLabel(eventType);
	},
</@override>
<@override name="attachmentCheck">
	if(isValid) {
		isValid = checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv');
	}
	
	if(isValid) {
		var labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict,
			typeNameObj = {},
			option = {},
			eventType = $('#type').val();
		
		if(labelDict) {
			for(var index in labelDict) {
				typeNameObj[labelDict[index].value] = labelDict[index].name;
			}
			
			option.typeNameObj = typeNameObj;
		}
		
		if(eventType === '22') {
			isValid = checkPicture(0, $('#bigFileUploadDiv div[file-status="complete"]'), null, option)
					&& checkPicture(1, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
		} else {
			isValid = checkPicture(toClose, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
		}
	}

	var occurred = $('#occurred').val();
	var type = $('#type').val();

	if(isBlankStringTrim(occurred) && isNotBlankStringTrim(type) && type == '23'){
		isValid = false;
		$.messager.alert('警告','事发详址不能为空，请选择楼栋名称！','warning');
	}
</@override>
<@override name="extraScriptFunction">
	//字典变更调用
	function changeDict(id,items){
		var oldTypeVal = $('#oldTypeVal').val();

		//字典赋值
		$('#oldTypeVal').val(id);
		$('#type').val(id);

		//森林防火事件-将事件详址空间变更为楼栋地址选择控件
		if(id === '23'){
			$('#buildingDiv').show();
			$('#occurredLabel').show();

			//原值不为森林防火事件 切换到楼栋选择 清空原楼栋选择信息 清空事发详址内容
			if(oldTypeVal != '23'){
				var regionCode = $('#gridCode').val() || "${event.gridCode!'-1'}";

				//清空事发详址内容
				$('#occurredLabel').html('');
				$('#occurred').val('');

				reInitBuilding(regionCode);
			}

			$('div.addressDiv').css({'display':'none'});
			//展示地图标注控件
			$('#geographicalLabelingInput').css({'display':'contents'});
		}else{
			$('#buildingDiv').hide();
			$('#occurredLabel').css({'display':'none'});
			$('#occurredLabel').val('');
			$('div.addressDiv').css({'display':'block'});
			//展示地图标注控件
			$('#geographicalLabelingInput').css({'display':'none'});

			//原控件为楼栋选择控件 切换事件分类时清空事发详址回填值
			if(oldTypeVal === '23'){
				//清空事发详址内容
				$('#occurredLabel').html('');
				$('#occurred').val('');
			}
		}
	}
	//楼栋组件回调方法
	function combo_afterSelect(data, target) {
		var buildingId = '', buildingName = '', buildingAddress = '',
			isLocated = '', latitude = '', longitude = '', mapType = '';

			if(data) {
				if(data == '') {//清空选择值
					var regionCode = $('#gridCode').val() || "${event.gridCode!'-1'}";

					reInitBuilding(regionCode);
				} else {
					buildingId = data.buildingId;
					buildingName = data.buildingName;
					buildingAddress = data.buildingAddress;
					isLocated = data.tdNewMarker || '';

					if(isLocated == '1') {
					latitude = data.x || '';
					longitude = data.y || '';
					mapType = '5';
			}
		}
	}

			$('#buildingId').val(data.buildingId);
			$('#buildingName').val(data.buildingName);
			$('#buildingAddress').val(data.buildingAddress);
			$('#occurred').val(buildingAddress);
			$('#occurredLabel').html(buildingAddress);

			$('#x').val(latitude);
			$('#y').val(longitude);
			$('#mapt').val(mapType);
	}

	function reInitBuilding(regionCode) {
		var options = $("#building").comboselector("options"),
			queryParams = options["query-params"];

			regionCode = regionCode || '-1';

		queryParams = queryParams || {};
		queryParams.orgCode = regionCode;

		$('#building').comboselector("clear");//会回调combo_afterSelect
		$('#building').comboselector('iniQueryParams', queryParams);
	}
</@override>
<@override name="extraFtlInclude">
	<#include "/zzgl/event/nananshi/check_attachment.ftl" />
</@override>

<@extends name="/zzgl/event/add_event_menu.ftl" />