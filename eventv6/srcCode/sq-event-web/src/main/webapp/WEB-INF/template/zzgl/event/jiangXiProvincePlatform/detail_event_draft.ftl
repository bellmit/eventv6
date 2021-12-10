<@override name="eventDetailPageTitle">
	江西省平台事件草稿详情页面
</@override>

<@override name="initExpandScript">
	$('#mapAsterik').show();
	$('#bigFileUploadAsterik').show();
</@override>

<@override name="attachmentCheck">
	var longitude = $('#x').val(),
		latitude = $('#y').val(),
		mapType = $('#mapt').val();
	
	if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
		$.messager.alert('警告', "请先完成地理标注！", 'warning');
		return;
	} else {
		if(!checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'))) {
			return;
		}
	}
</@override>

<@extends name="/zzgl/event/detail_event_draft.ftl" />