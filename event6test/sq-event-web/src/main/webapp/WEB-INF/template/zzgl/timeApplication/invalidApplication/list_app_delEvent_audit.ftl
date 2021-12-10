<@override name="createTimeQueryLableName">无效时间：</@override>
<@override name="createTimeStrFieldTitle">无效时间</@override>
<@override name="reasonFieldTitle">无效原因</@override>
<@override name="extraParamInit">
	timeAppCheckStatusComboBox = AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, ["3"], {
		DataSrc : [{"name":"待复核", "value":"3"},{"name":"已恢复", "value":"2"},{"name":"已无效", "value":"1"}],
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
	
	$('#gridRegionTypeTr').hide();
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/list_app_delEvent_audit.ftl" />