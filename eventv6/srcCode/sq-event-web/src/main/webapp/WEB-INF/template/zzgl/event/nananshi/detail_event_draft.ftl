<@override name="eventDetailPageTitle">南安市草稿事件页面</@override>
<@override name="eventExtraJs">
	<#include "/zzgl/event/nananshi/check_attachment.ftl" />
</@override>

<@override name="gridPathTd">
	<td class="LeftTd">
		<label class="LabName">
			<span>所属区域：</span>
		</label><div class="Check_Radio FontDarkBlue" style="width:62%"><#if event.gridPath??>${event.gridPath}</#if></div>
	</td>
</@override>
<@override name="geographicalLabelingInput">
	<tr class="DotLine">
		<td colspan="3" class="LeftTd"><label class="LabName"><span>地理位置：</span></label>
			<div id="resmarkerDiv" style="display: none">
				<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
			</div>
			<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		</td>
	</tr>
</@override>

<@override name="bigFileUploadInitOption">
	$.extend(bigFileUploadOpt, {
		labelDict: capLabelDict("${event.type!}")
	});
	
	changeContactUserLabel("${event.type!}");
	
	var bigViodeUploadOpt = $.extend({}, bigFileUploadOpt, {
		fileExt: '.mp4,.avi,.amr'
	}),
	videoUploadHtml = '<tr>' +
				      	'<td colspan="3" class="LeftTd">' +
				      		'<label class="LabName"><span>视频上传：</span></label><div id="bigVideoUploadDiv"></div>' +
				      	'</td>' +
					  '</tr>';
	
	$('#bigFileUploadTr').after(videoUploadHtml);
	
	bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
</@override>

<@override name="attachmentCheck">
	var labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict,
		typeNameObj = {},
		option = {};
	
	if(labelDict) {
		for(var index in labelDict) {
			typeNameObj[labelDict[index].value] = labelDict[index].name;
		}
		
		option.typeNameObj = typeNameObj;
	}
	
	if(!checkPicture(toClose, $('#bigFileUploadDiv div[file-status="complete"]'), null, option)) {
		return false;
	}
</@override>

<@override name="mapMarkerDiv">
	capMarkerData();
</@override>
<script type="text/javascript">
	function capMarkerData() {

		var markerOperation = $("#markerOperation").val(); // 地图操作类型
		var id = $("#id").val();
		var module = $("#module").val(); // 模块
		if (typeof module == "undefined" || module == "") {
			module = 'EVENT_V1';
		}

		var showName = "标注地理位置";

		$.ajax({
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?markerOperation='+markerOperation+'&id='+id+'&module='+module+'&t='+Math.random(),
			type: 'POST',
			timeout: 3000,
			dataType:"json",
			async: false,
			error: function(data){
				$.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning');
			},
			success: function(data) {
				if(data && data.x != "" && data.x != null){
					$('#resmarkerDiv').css('display','block');
				}else{
					$('#resmarkerDiv').css('display','none');
				}
			}
		});

		if(markerOperation == 3) {
			$("#mapTab2").html("");//为了展示可视部分
			$("#mapTab2").parent().addClass("mapTab3")
					.css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
					.attr("title", "查看地理位置");
		} else {
			$("#mapTab2").html(showName);
		}
	}
</script>

<@extends name="/zzgl/event/detail_event_draft.ftl" />