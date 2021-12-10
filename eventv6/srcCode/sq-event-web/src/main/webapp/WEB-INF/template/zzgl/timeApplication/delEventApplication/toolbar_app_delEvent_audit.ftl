<@override name="extraParamTopTr">
	<tr>
		<td>
			<label class="LabName width65px"><span>复核状态：</span></label>
			<input id="timeAppCheckStatus" name="timeAppCheckStatus" type="text" value="" class="queryParam hide"/>
			<input id="timeAppCheckStatusName" type="text" class="inp1 InpDisable w150" />
		</td>
	</tr>
	<tr>
		<td><label class="LabName width65px"><span>办理人：</span></label><input class="inp1 w150 queryParam" type="text" id="applicantName" name="applicantName" ></input></td>
	</tr>
</@override>
<@override name="delEventAppGlobalVariables">
	var timeAppCheckStatusComboBox = null;
</@override>
<@override name="extraParamInit">
	timeAppCheckStatusComboBox = AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, ["3"], {
		DataSrc : [{"name":"待复核", "value":"3"},{"name":"已恢复", "value":"2"},{"name":"已删除", "value":"1"}],
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
</@override>
<@override name="function_resetCondition_after_reset">
	<!--为了重置后，字典名称能正常展示-->
	timeAppCheckStatusComboBox.setSelectedNodes(["3"]);
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/toolbar_app_delEvent.ftl" />