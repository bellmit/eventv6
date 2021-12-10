<@override name="eventDetailPageTitle">
	南昌市大数据问题上报事件详情页面
</@override>
<@override name="detailExtend">
<tr>
    <td align="right">快捷标签：</td>
    <td colspan="5">
        <code><#if eventLabelName?? && eventLabelName!=''>${eventLabelName}<#else>无</#if></code>
    </td>
</tr>
</@override>
<@override name="attachmentCheck">
	if($('#selectedNodeValue').val()=='task8'){//结案环节需要上传处理后图片
    	var imgBefore = $("span[label-name='处理前']").length;
		var	imgAfter = $("span[label-name='处理后']").length;
		
		if(imgBefore == 0) {
			$.messager.alert('警告', "请先上传处理前图片！", 'info');
			return;
		}
		if(imgAfter == 0) {
			$.messager.alert('警告', "请先上传处理后图片！", 'info');
			return;
		}
    }
</@override>
<@extends name="/zzgl/event/detail_event.ftl" />