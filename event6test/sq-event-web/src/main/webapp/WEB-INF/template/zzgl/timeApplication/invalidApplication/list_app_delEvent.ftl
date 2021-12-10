<@override name="createTimeQueryLableName">无效时间：</@override>
<@override name="createTimeStrFieldTitle">无效时间</@override>
<@override name="reasonFieldTitle">无效原因</@override>
<@override name="extraParamTopTr">
	<tr>
		<td>
			<label class="LabName width65px"><span>采集渠道：</span></label>
			<input type="text" id="eventCollectWay" name="eventCollectWay" class="hide queryParam"/>
			<input type="text" id="eventCollectWayName" class="inp1 w150" />
		</td>
	</tr>
	<tr>
		<td>
			<label class="LabName width65px"><span>信息来源：</span></label>
			<input type="text" id="eventSource" name="eventSource" class="hide queryParam"/>
			<input type="text" id="eventSourceName" class="inp1 w150" />
		</td>
	</tr>
</@override>
<@override name="extraParamInit">
	$('#gridRegionTypeTr').hide();
	
	AnoleApi.initTreeComboBox("eventCollectWayName", "eventCollectWay", "A001093096", null, null, {
		RenderType : "01",
		ShowOptions: {
			EnableToolbar : true
		}	
	});
	
	AnoleApi.initTreeComboBox("eventSourceName", "eventSource", "A001093222", null, null, {
		RenderType : "01",
		ShowOptions: {
			EnableToolbar : true
		}
	});
	
	$('#timeAppQueryForm input[name="isCapApplicant"]').val(false);
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/list_app_delEvent.ftl" />