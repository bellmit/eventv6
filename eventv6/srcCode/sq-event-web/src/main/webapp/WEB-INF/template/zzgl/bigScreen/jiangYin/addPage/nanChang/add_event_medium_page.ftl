<@override name="eventMenuPageTitle">
	事件采集页面
</@override>
<@override name="extraJs">
	
	<#if isUsePostMessage?? && isUsePostMessage=='true'>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
		<style>
		.mt-tbody {max-height: 4.5rem;}
			.mask-c-bottom {margin-top: .5rem;}
			.FromTo{margin-top:-0.05rem}
			.TimeControl{font-size:12px;}
			.mf-item>input {background-color: rgbargba(6, 31, 52, .8) ! important;color: white ! important}
			.ztree{background-color: rgbargba(6, 31, 52, .8) !important;}
			.ztree *{color:white!important;}
			.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
			.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
			.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
			.swiper-pagination {height: 0.12rem;bottom: .1rem !important;}
			.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
			.aj_items_t_yellow {text-align: left;}
			.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
			.mCSB_inside > .mCSB_container{margin-right:.1rem}
			body,html{width:100%;height:100%;}
			.select-table .select-td {
	    		border-bottom: 1px solid #39BBF8;
	    		border-right: 1px solid #39BBF8;
	    		background-color: #F3F8FE;
			}
			#leftPanel>.ztree * {
	    		padding: 0;
	    		margin: 0;
	    		font-size: 12px;
	    		font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
	    		color: #000!important;
	    		background-color: white !important;
			}
			
			#leftTree{
				background-color: white !important;
			}
			
			.inp1 {
	    		background-color: rgba(6, 31, 52, .8) !important;
	    		border-radius: 0.04rem ;
	   			border: solid 1px #2e7bec;
	    		color: #fff !important;
			}
			.area1 {
	    		background-color: rgba(6, 31, 52, .8) !important;
	    		border-radius: 0.04rem ;
	   			border: solid 1px #2e7bec;
	    		color: #fff !important;
			}
			
			.BigTool {
	    		background: transparent;
	    		margin-top: -2rem;
			}
			
			.LabName {
	    		color: white;
	    	}
	    	
	    	.panel .LabName {
	    		color: #7c7c7c;
	    	}
	    	
	    	.panel .inp1 {
	    		background-color: white !important;
	    		border-radius: 2px ;
	   			border: 1px solid #e1e8f2;
	    		color: #000 !important;
			}
	    	
	    	.ConSearch .inp1 {
	    		background-color: white !important;
	    		border-radius: 2px ;
	   			border: 1px solid #e1e8f2;
	    		color: #000 !important;
			}
			
			.ztree li a.curSelectedNode {
	   	 		background-color: rgba(10, 55, 157, .8) !important;
	    		border: 1px rgba(10, 55, 157, .8) solid !important;
			}
			
			.AddressControl .con {
	    		background: white;
			}
			
			.upload-text {
				color:white;
			}
			
			.file-text {
				color:white;
			}
	
		</style>
	</#if>
</@override>

<@override name="initExpandScript">
	<#if isUsePostMessage?? && isUsePostMessage=='true'>
	$('.WdateDiv').css('background-color','rgba(7, 44, 86, .9) !important');
	$('#wholePageDiv').css({'background-color':'transparent','margin-top': '1rem'});
	</#if>
</@override>

<@override name="addEventMenuTableSubmitCrossOverHandler">
	var isUsePostMessage = '${isUsePostMessage!}';
	var messageUrl = '${messageUrl!}';
	if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
		window.parent.postMessage('${callBack!}(' + JSON.stringify(data) + ')', messageUrl);
	} else {
		<@super></@super>
	}
</@override>
<@override name="addEventMenuStartWorkflowCrossOverHandler">
	var isUsePostMessage = '${isUsePostMessage!}';
	var messageUrl = '${messageUrl!}';
	if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
		window.parent.postMessage('${callBack!}(' + JSON.stringify(data) + ')', messageUrl);
	} else {
		<@super></@super>
	}
</@override>

<@extends name="/zzgl/event/add_event_menu.ftl" />