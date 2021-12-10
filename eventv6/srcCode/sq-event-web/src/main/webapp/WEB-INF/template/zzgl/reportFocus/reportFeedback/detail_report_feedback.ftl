<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>反馈信息详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />

		<style type="text/css">
			.singleCellClass{width: 68%;}
			.doubleCellClass{width: 88%}
			.labelNameWide{width: 132px;}
			.Check_Radio {
				float:left;
				margin-top:5px;
				*margin-top:9px;
				word-break:break-all;
			}
		</style>
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul id="topTitleUl" class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">反馈信息详情</a></li>
							<#if extTypeCount?? && (extTypeCount > 0)>
								<li><a href="##" divId="extRecordDiv">补充信息</a></li>
							</#if>
							<#if remindTypeCount?? && (remindTypeCount > 0)>
								<li><a href="##" divId="remindRecordDiv">催单记录</a></li>
							</#if>
						</ul>
					</div>
				</div>
				
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent" style="border-bottom: 1px solid #e5e5e5">
					<div class="fw-det-tog fw-det-tog-n" style="height: 83%;">
						<input type="hidden" id="fbUUId" name="fbUUId" value="<#if fbUUId??>${fbUUId}</#if>" />
						<input type="hidden" id="seUUId" name="seUUId" value="<#if seUUId??>${seUUId}</#if>" />

						<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<label class="LabName"><span>所属区域：</span></label><div class="Check_Radio FontDarkBlue" title="${gridPathName!}" style="width:240px;overflow:hidden; white-space:nowrap; text-overflow:ellipsis">${gridPathName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>业务类型：</span></label><div class="Check_Radio FontDarkBlue">${bizTypeStr!}</div>
								</td>
							</tr>
							<tr>
								<td style="width: 40%;">
									<label class="LabName"><span>报告编号：</span></label><div class="Check_Radio FontDarkBlue">
										<#if fromPage?? && fromPage == 'listPage'>
											<a href="###" title="查看${bizTypeStr!}详情" onclick="eventDetail('${bizSign!}')">${bizCode!}</a>
										<#else>
											${bizCode!}
										</#if>
									</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>数据来源：</span></label>
                                    <div class="Check_Radio FontDarkBlue singleCellClass">${dataSourceStr!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>数据标识：</span></label>
                                    <div class="Check_Radio FontDarkBlue">${dataSign!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>下达内容：</span></label>
                                    <div class="Check_Radio FontDarkBlue" title="${seContent!}" style="width:370px;overflow:hidden; white-space:nowrap; text-overflow:ellipsis">${seContent!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>下达时间：</span></label>
                                    <div class="Check_Radio FontDarkBlue">${seTime!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>下达状态：</span></label>
                                    <div class="Check_Radio FontDarkBlue">${seStatusStr!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>接收时间：</span></label>
                                    <div class="Check_Radio FontDarkBlue">${reTime!}</div>
								</td>
								<#if fbStatus?? && fbStatus != '01'> <#--未反馈-->
									<td>
										<label class="LabName labelNameWide"><span>反馈时间：</span></label>
										<div class="Check_Radio FontDarkBlue">${fbTime!}</div>
									</td>
								<#else>
									<td></td>
								</#if>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>接收时限：</span></label>
									<div class="Check_Radio FontDarkBlue">${reDeadline!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>反馈时限：</span></label>
									<div class="Check_Radio FontDarkBlue">${fbDeadline!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>接收状态：</span></label>
									<div class="Check_Radio FontDarkBlue">${reStatusStr!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>反馈状态：</span></label>
                                    <div class="Check_Radio FontDarkBlue">${fbStatusStr!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>反馈组织名称：</span></label>
									<div class="Check_Radio FontDarkBlue">${fbOrgName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>反馈用户姓名：</span></label>
									<div class="Check_Radio FontDarkBlue">${fbUserName!}</div>
								</td>
							</tr>
							<#if fbStatus?? && fbStatus != '01'> <#--非【未反馈】，则展示-->
                                <tr>
                                    <td colspan="2">
                                        <label class="LabName"><span>反馈内容：</span></label>
                                        <div class="Check_Radio FontDarkBlue doubleCellClass">${fbContent!}</div>
                                    </td>
                                </tr>
							</#if>
							<!--反馈处理-->
							<#if canDoFeedBack?? && canDoFeedBack == 'true'>
								<tr>
									<td colspan="2">
										<form id="reportFeedbackForm" name="reportFeedbackForm" action="" method="post">
											<input type="hidden" name="fbUUId" value="<#if fbUUId??>${fbUUId}</#if>" />
											<label class="LabName"><span id="remarkSpanHtml"><label class="Asterik">*</label>反馈内容：</span></label>
											<span id="contentSpan_1" class="FastReplyBtn">
												<textarea rows="3" style="height: 50px; width: 730px;resize: none" id="fbContent" name="fbContent"
														  class="area1 easyui-validatebox autoDoubleCell validatebox-text isSettledAutoWidth validatebox-invalid"
														  data-options="required:true,tipPosition:'bottom',validType:['maxLength[2000]','characterCheck']" title=""></textarea>
											</span>
										</form>
									</td>
								</tr>
							</#if>
						</table>
					</div>
					<!--反馈处理-->
					<#if canDoFeedBack?? && canDoFeedBack == 'true'>
						<div class="BigTool">
							<div class="BtnList" style="width: 314px;">
								<a href="###" onclick="doFeedback();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
								<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
							</div>
						</div>
					</#if>
				</div>
				<#if extTypeCount?? && (extTypeCount > 0)>
					<div id="extRecordDiv" class="fw-main tabContent" showType="${extType}">
					</div>
				</#if>
				<#if remindTypeCount?? && (remindTypeCount > 0)>
					<div id="remindRecordDiv" class="fw-main tabContent" showType="${remindType}">
					</div>
				</#if>
 			</div>
		</div>
	</body>
	
</html>
<script type="text/javascript">
	$(function () {
		var $winH = 0, $topH = 0, $btnH = 0;
		<#if extTypeCount?? && (extTypeCount > 0)
		   || remindTypeCount?? && (remindTypeCount > 0)>
			function showExtRecordList(divId,extType) {
				var url = "${rc.getContextPath()}/zhsq/reportFeedback/toExtRecordList.jhtml?seUUId=" + $('#seUUId').val() + "&extType=" +extType;
				$("#"+divId).append('<iframe iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
				$("#"+divId +" > iframe").width($("#mainDiv").width());
				$("#"+divId).height('auto');
			}
		$('#topTitleUl').on('click', 'li a', function() {
			$('#topTitleUl > li > a').removeClass('active');
			$(this).addClass('active');
			var divId = $(this).attr('divId');
			if(divId !='mainDiv' && !$('#' + divId).hasClass('recordsLoaded')) {
				$('#' + divId).addClass('recordsLoaded');
				showExtRecordList(divId,$('#' + divId).attr("showType"));
			}
			var iframeItemList = $("#" + divId + " iframe"),
				iframeLen = iframeItemList.length,
			    mainDivWidth = $(window).width() - 20 - 20;//左右边距20，样式container_fluid设置的
			var $winH = $(window).height(), $topH = $('#topTitleDiv').outerHeight(true), $btnH = $('#btnDiv').outerHeight(true);

			$('#' + divId).height($winH - $topH - $btnH);
			$('#' + divId).width(mainDivWidth + 10 + 10);

			if(iframeLen == 1) {
				var iframeItem = iframeItemList.eq(0),
					iframeLoaded = iframeItem.attr("_loadflag");
				if(isBlankStringTrim(iframeLoaded)) {
					iframeItem.attr("_loadflag", true);//用于防止重复加载
					iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
				}
			}
			$('#formDiv div.tabContent').hide();
			$('#' + divId).show();
		});
		</#if>
		$(window).on('load resize', function () {
			$winH = $(window).height();
			$topH = $('#topTitleDiv').outerHeight(true);
			$btnH = $('#btnDiv').outerHeight(true);

			$('#mainDiv').height($winH - $topH - $btnH);

			//滚动条初始化
			$("#mainDiv").niceScroll({
				cursorcolor:"rgba(0, 0, 0, 0.3)",
				cursoropacitymax:1,
				touchbehavior:false,
				cursorwidth:"4px",
				cursorborder:"0",
				cursorborderradius:"4px"
			});
		});
	});

	<#if fromPage?? && fromPage == 'listPage'>
	<#if canDoFeedBack?? && canDoFeedBack == 'true'>
	function doFeedback(){
		var validate = $("#reportFeedbackForm").form('validate');
		if(validate){
			modleopen();
			$("#reportFeedbackForm").attr("action", "${rc.getContextPath()}/zhsq/reportFeedback/doFeedback.jhtml");

			$("#reportFeedbackForm").ajaxSubmit(function(data) {
				modleclose();
				var result = data.success,
						msg = data.tipMsg;

				if(result && result == true) {
					msg = data.tipMsg || '操作成功！';
					parent.reloadDataForSubPage(msg);
				} else {
					msg = data.tipMsg || '操作失败！';
					$.messager.alert('错误', msg, 'error');
				}
			});
		}
	}
	function closeWin() {
		parent.closeMaxJqueryWindow();
	}
	</#if>
	function eventDetail(bizSign){
		if(bizSign) {
			var title = '查看${bizTypeStr!}详情';
			<#if bizType??>
			<#switch bizType>
			<#case "01">
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=" + bizSign;
			<#break>
			<#case "02">
			var url = "${rc.getContextPath()}/zhsq/reportTwoVioPre/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=1";
			<#break>
			<#case "03">
			var url = "${rc.getContextPath()}/zhsq/reportHHD/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=2";
			<#break>
			<#case "04">
			var url = "${rc.getContextPath()}/zhsq/reportEHD/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=3";
			<#break>
			<#case "05">
			var url = "${rc.getContextPath()}/zhsq/reportEPC/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=4";
			<#break>
			<#case "06">
			var url = "${rc.getContextPath()}/zhsq/reportWQ/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=5";
			<#break>
			<#case "08">
			var url = "${rc.getContextPath()}/zhsq/reportBusPro/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=10";
			<#break>
			<#case "10">
			var url = "${rc.getContextPath()}/zhsq/reportMarFac/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=13";
			<#break>
			<#case "11">
			var url = "${rc.getContextPath()}/zhsq/reportEHT/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=14";
			<#break>
			<#case "12">
			var url = "${rc.getContextPath()}/zhsq/reportTOT/toDetail.jhtml?reportUUID=" + bizSign + "&listType=5&reportType=15";
			<#break>
			<#default>
			$.messager.alert('警告','参数【${bizType!}】未定义!','warning');
			return;
			</#switch>
			</#if>
			parent.openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: title,
				targetUrl: url
			});
		}
	}
	</#if>
</script>