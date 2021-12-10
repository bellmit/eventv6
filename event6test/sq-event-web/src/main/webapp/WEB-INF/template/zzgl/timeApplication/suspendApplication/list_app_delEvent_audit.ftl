<@override name="createTimeQueryLableName">挂起时间：</@override>
<@override name="createTimeStrFieldTitle">挂起时间</@override>
<@override name="reasonFieldTitle">挂起原因</@override>
<@override name="extraParamInit">
	timeAppCheckStatusComboBox = AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, ["3"], {
		DataSrc : [{"name":"待复核", "value":"3"},{"name":"已恢复", "value":"2"},{"name":"已挂起", "value":"1"}],
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/list_app_delEvent_audit.ftl" />