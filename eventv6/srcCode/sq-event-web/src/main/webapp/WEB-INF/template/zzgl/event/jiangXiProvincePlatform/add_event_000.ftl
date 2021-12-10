<@override name="eventAddPageTitle">
	江西省平台事件新增页面
</@override>

<@override name="sourceDictExtraOption">
	,Disabled : true
</@override>

<@override name="initExpandScript">
	$('#mapAsterik').show();
	$('#bigFileUploadAsterik').show();
</@override>

<@override name="attachmentCheck">
	if(isValid) {
		var longitude = $('#x').val(),
			latitude = $('#y').val(),
			mapType = $('#mapt').val();
		
		if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
			$.messager.alert('警告', "请先完成地理标注！", 'warning');
			return;
		} else {
			isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
		}
	}
</@override>

<@extends name="/zzgl/event/add_event_000.ftl" />