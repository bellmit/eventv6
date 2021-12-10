<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/ui/css/normal.css"  />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	
	<!--新版附件-->
	<#--<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>-->
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
	<script>
		var base = '${rc.getContextPath()}';//项目的上下文，（工程名）
		var imgDomain = '${imgDomain}';//文件服务器域名
		var uiDomain = '${uiDomain}';//公共样式域名
		var skyDomain = '${skyDomain}';//网盘挂载IP【文档在线预览服务器IP：询问赛男团队，获取ip值】
		var componentsDomain = '${componentsDomain}';//公共组件工程域名
		var fileDomain = '${fileDomain}';//文件服务工程域名
	</script>
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/custom_msgClient.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js" type="text/javascript" ></script>
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
	
	<style>
		.NorForm td {
			border-bottom: 1px dotted rgb(255, 255, 255);
		}
		#_currentTaskHandlerDiv p{
			display: none;
		}
		.LabName {width: 100px;}
		.Asterik{
			color: red;
		}
	</style>
</head>
<body>
<div id="content-d" class="MC_con content light">
	<div name="tab" id="div0" class="NorForm" style="border:1px solid #c5d0dc;margin-top:10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>流程名称：</span></label>
					<span class="Check_Radio FontDarkBlue">${(bo.flowName)!}</span>
					<#if bo.lastSendId??>
						<button type="button" title="展示历史数据" style="margin-left: 23px" class="layui-btn layui-btn-danger layui-btn-xs layui-btn-radius" onclick="showLastSend(${bo.lastSendId})">历史流程</button>
					</#if>
				</td>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>协作类型：</span></label>
					<span class="Check_Radio FontDarkBlue">${(bo.sendTypeCN)!}</span>
				</td>
			</tr>

			<tr>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>发起日期：</span></label>
					<span class="Check_Radio FontDarkBlue">${(bo.publishDateStr)!}</span>
				</td>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>办理期限：</span></label>
					<span class="Check_Radio FontDarkBlue">${(bo.limitDateNum)!}(天)</span>
				</td>
			</tr>
			<tr>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>当事人姓名：</span></label>
					<span class="Check_Radio FontDarkBlue" style="width: 65%">${(bo.receiveName)!}</span>
				</td>
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>当事人地址：</span></label>
					<span class="Check_Radio FontDarkBlue" style="width: 70%">${(bo.receiveAddr)!}</span>
				</td>
			</tr>
			<#if bo.sendType == '003'>
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>原告：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 65%">${(bo.plaintiff)!}</span>
					</td>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>被告：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 70%">${(bo.defendant)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>案号：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 65%">${(bo.caseNo)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>案由：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 70%">${(bo.caseReason)!}</span>
					</td>
				</tr>
			</#if>
			<tr>
				<td>
					<label class="LabName"><span>详细说明：</span></label>
					<span class="Check_Radio FontDarkBlue" style="width: 70%">${(bo.expound)!}</span>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<label class="LabName"><span>附件：</span></label>
					<div id="attatch_div">

					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="h_20"></div>

	<#if showHandle=='y'>
		<!-- 处理环节处理内容 -->
		<#include "/zzgl/gdpersionsendflow/gd_persion_send_flow_handle.ftl" />
	<#else>
		<!-- 处理环节处理详情 -->
		<#include "/zzgl/gdpersionsendflow/gd_persion_send_flow_handle_view.ftl" />
	</#if>

	<#if instanceId?? && (instanceId > 0)>
		<div class="h_20"></div>
		<div class="ConList">
			<div class="nav" id="tab">
				<ul>
					<#if instanceId??>
						<li id="01_li" class="current">处理环节</li>
					</#if>
				</ul>
			</div>
			<div class="ListShow ListShow2">
				<#if instanceId??>
					<div id="01_li_div" class="t_a_b_s">
						<div id="workflowDetail" border="false"></div>
					</div>
				</#if>
			</div>
		</div>
		<div class="h_20"></div>
	</#if>
</div>
<div class="BigTool">
	<div class="BtnList">
		<#if showHandle=='y'>
			<#if nodeName != "task1">
				<a href="javascript:;" class="BigNorToolBtn RejectBtn" onClick="handleReject();">驳回</a>
			</#if>
			<a href="javascript:;" class="BigNorToolBtn SubmitBtn" onClick="handleSubmit();">提交</a>
		</#if> 
		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
	</div>
</div>
</body>

<script type="text/javascript">
	var id = '${(bo.sendId)!}'
	var isFormLast = '${(isFormLast)!}'
	if(isFormLast == 'yes'){
		$(".BigTool").hide()
	}
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}

	function initAttatch() {
		var attachmentData = {};
		$("#attatch_div").bigfileUpload({
			useType: 'view',//附件上传的使用类型，add,edit,view，（默认edit）;
			imgDomain : imgDomain,//图片服务器域名
			uiDomain : uiDomain,//公共样式域名
			skyDomain : skyDomain,//网盘挂载IP
			componentsDomain : componentsDomain,//公共组件域名
			fileDomain : fileDomain,//文件服务域名
			showTip : false,
			// fileExt : '.jpeg,.jpg,.png,.gif,.doc,.xls,',//允许上传的附件类型
			appcode:"zhsq_event",//文件所属的应用代码（默认值components）
			module:"gdPersionSendFlow",//文件所属的模块代码（默认值bigfile）
			attachmentData:{bizId:id,attachmentType:'GD_PERSION_FLOW_TYPE'},
			uploadSuccessCallback : function(file,response){
				console.log(file.id);
				console.log(response.attachmentId);
			}

		});

	}
	initAttatch()

	/**
	 * 展示历史数据
	 */
	function showLastSend(lastSendId) {
		var optin = {
			type: 2 //此处以iframe举例
			,title: '历史详情'
			,area: ['100%', '100%']
			,shade: 0
			,maxmin: true
			,content: "${rc.getContextPath()}/zhsq/gdPersionSendFlow/view.jhtml?isFormLast=yes&id=" + lastSendId
			,btn: ['关闭', '全部关闭']
			,btn2: function(){
				parent.layer.closeAll();
			}
			,success: function(layero){

			}
		};
		if(isFormLast == 'yes'){
			parent.layer.open(optin)
		}else{
			layer.open(optin)
		}
	}
</script>
<!--流程展示JS-->
<script>
	$(function(){
		var _winHeight = $(window).height();
		var _winWidth = $(window).width();

		<#if instanceId??>
		$("#workflowDetail").panel({
			height:'auto',
			width:'auto',
			overflow:'no',
			href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=${instanceId}",
			onLoad:function(){//配合detail_workflow.ftl使用
				var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
				var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.4;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
				var taskListSize = $("#taskListSize").val();	//任务记录数
				var handleTaskNameWidth = 115;		//处理环节总宽度
				var handleLinkWidth = 21;			//办理环节宽度
				var handlePersonAndTimeWidth = 0;	//办理人/办理时间宽度
				var handleRemarkWidth = 0;			//办理意见宽度

				var remindSize = 0;					//催办记录数
				var remindPersonAndTimeWidth = 0;	//催办人和催办时间宽度
				var remindRemarkWidth = 0;			//催办意见宽度

				for(var index = 0; index < taskListSize; index++){
					remindSize = $("#remindListSize_"+index).val();//催办记录数
					remindPersonAndTimeWidth = 0;
					remindRemarkWidth = 0;

					handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();

					if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
						$("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
						handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
					}

					handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;

					$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度

					for(var index_ = 0; index_ < remindSize; index_++){
						remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
						remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
						$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
					}
				}

				adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
				var html = "<i class=\"TaskCurrent\"></i>当前环节" +
						"<i class=\"TaskHistory\"></i>历史环节"
				$("#iconDiv").html(html)
			}
		});
		</#if>
	});


</script>

</html>
