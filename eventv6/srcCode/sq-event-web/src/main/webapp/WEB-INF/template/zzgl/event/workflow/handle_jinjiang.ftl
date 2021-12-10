<@override name="handlePageTitle">
	晋江办理事件页面
</@override>

<@override name="extraCss">
	<style type="text/css">
		.DealProcess .LabName{margin-left: 20px; width:75px;}
	</style>
</@override>

<@override name="fileTypes">
	fileExt: '.jpg,.gif,.png,.jpeg,.webp,.zip,.7z,.txt,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.rar',
</@override>

<@override name="selectUserInitExtraParam">
	isShowUserPartyNameFuzzyQuery : true,
</@override>

<@override name="maxSingleFileSize">
	maxSingleFileSize: 50,
</@override>

<@extends name="/zzgl/event/workflow/handle_event_node.ftl"/>