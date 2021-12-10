<@override name="eventDetailEditPageTitle">南安市事件待办可编辑详情页面</@override>

<@override name="eventDetailAdditionalQuote">
	<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
</@override>

<@override name="feedbackLi">
    <li id="07_li" class="hide">信息反馈列表</li>
</@override>
<@override name="feedbackConList">
    <div id="07_li_div" class="t_a_b_s hide">
        <div class="tabss">
            <div id="eventFeedbackListDiv"></div>
        </div>
    </div>
</@override>
<@override name="eventEvaParams">, 'bizType':'${bizType!}'</@override>
<@override name="feedbackContentConstructor">
    var feedbackCount = data.feedbackCount;
    if(feedbackCount > 0){
        $("#07_li").show();
        var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#eventId").val()+"&bizType=${bizType!}";
        $("#eventFeedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
        $("#eventFeedbackListDiv > iframe").width($("#workflowDetail").width());
        $("#eventFeedbackListDiv").height($("#workflowDetail").height());
    }
</@override>

<@override name="extraSetting4eventType">
	OnChanged : function(eventType, items) {
		if($('#_handleEventTable input[type=radio][name=nextNode][value=task8]').length > 0) {
			if(eventType == '22') {
				$('#_handleEventTable input[type=radio][name=nextNode]').parent().hide();
				$('#_handleEventTable input[type=radio][name=nextNode][value=task8]').parent().show();
				$('#_handleEventTable input[type=radio][name=nextNode][value=task8]').click();
			} else {
				$('#_handleEventTable input[type=radio][name=nextNode]').parent().show();
			}
		}

		changeDict(eventType,items);
		changeLabelDict(eventType);
		changeContactUserLabel(eventType);
	},
</@override>

<@override name="gridLabelName">
<input type="hidden" id="oldTypeVal" name="oldTypeVal" value="${event.type!}" />
所属区域
</@override>
<@override name="eventOccurredInput">
		<td colspan="1" class="LeftTd" id="buildingDiv" style="display: none">
			<label class="LabName">
				<span>楼栋名称：</span>
			</label>
			<input type="hidden" id="buildingId" name="buildingId" value="<#if event.buildingId??>${event.buildingId?c}</#if>" />
			<input type="hidden" id="buildingName" name="buildingName" value="${event.occurred!}" />
			<input type="hidden" id="buildingAddress" name="buildingAddress" value="${event.occurred!}" />
			<input type="text" id="building" class="comboselector" style="height: 28px; width: 405px;display: none" data-options="dType:'building', afterSelect:combo_afterSelect" query-params="orgCode=${event.gridCode!'-1'}" />
		</td>
		<td id="geographicalLabelingInput" style="display: none">
			<label class="LabName"><span>地理标注：</span></label>
			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
		</td>
	</tr>
	<tr>
		<td id="occurredDiv">
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
</@override>
<@override name="geographicalLabelingInput">
</@override>
<@override name="involvedNumInput">
	<td>
		<label class="LabName"><span>采集渠道：</span></label>
		<div id="collectWayNameLabel" class="Check_Radio FontDarkBlue singleCellClass">${event.collectWayName!}</div>
	</td>
	<td>
		<label class="LabName"><span>涉及人数：</span></label>
		<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
		<input type="text" class="inp1 easyui-validatebox selectWidth" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" />
	</td>
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

<@override name="initSupplement">
	var locationOption = {
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
		}
	};
	<#if event.resMarker??>
		$.extend(locationOption._addressMap, {
			_addressMapX	: '${event.resMarker.x!}',
			_addressMapY	: '${event.resMarker.y!}',
			_addressMapType	: '${event.resMarker.mapType!}'
		});
	</#if>

	$("#occurred").anoleAddressRender(locationOption);

	changeDict('${event.type}');
	changeContactUserLabel('${event.type}', true);

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
		}
	});-->

</@override>
<@override name="extraScriptFunction">
	//字典变更调用
	function changeDict(id,items){
		var oldTypeVal = $('#oldTypeVal').val();
		$('#oldTypeVal').val(id);
		$('#type').val(id);
		//森林防火事件-将事件详址空间变更为楼栋地址选择控件
		if(id === '23'){
			$('#buildingDiv').show();
			$('#occurredLabel').show();
			$('div.addressDiv').css({'display':'none'});

			//原值不为森林防火事件 切换到楼栋选择 清空原楼栋选择信息 清空事发详址内容
			if(oldTypeVal != '23'){
				var regionCode = $('#gridCode').val() || "${event.gridCode!'-1'}";

				//清空事发详址内容
				$('#occurredLabel').html('');
				$('#occurred').val('');

				reInitBuilding(regionCode);
			}

			//展示地图标注控件
			$('#geographicalLabelingInput').css({'display':'flex'});

		}else{
			$('#buildingDiv').hide();
			$('#occurredLabel').css({'display':'none'});
			$('#occurredLabel').val('');
			$('div.addressDiv').css({'display':'block'});

			//原控件为楼栋选择控件 切换事件分类时清空事发详址回填值
			if(oldTypeVal === '23'){
				//清空事发详址内容
				$('#occurredLabel').html('');
				$('#occurred').val('');
			}

			//展示地图标注控件
			$('#geographicalLabelingInput').css({'display':'none'});
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

<@extends name="/zzgl/event/detail_event_edit.ftl" />