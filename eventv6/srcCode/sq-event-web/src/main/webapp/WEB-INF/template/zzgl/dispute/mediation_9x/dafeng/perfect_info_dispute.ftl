
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>新增事件</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/detail.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/form-add.css"/>
		<#--<link href="${rc.getContextPath()}/ui/workorder/css/picUpload.css" rel="stylesheet" />-->
		<script type="text/javascript" src="${rc.getContextPath()}/ui/js/function.js"></script>
		<script type="text/javascript">
			var base = "${rc.getContextPath()}";
			var fileDomain = '${fileDomain}';
			var imgDomain = '${imgDomain}';
			var uiDomain = '${uiDomain}';
			var skyDomain = '${skyDomain}';
			var componentsDomain = '${COMPONENTS_DOMAIN}';
		</script>
		<#include "/component/commonFiles-1.1.ftl" />
		<link href="${GEO_URL}/js/components/geoAddressPlugin/css/address.css" rel="stylesheet" />
		<script type="text/javascript" src="${(GEO_URL)}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<!--新版附件-->
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>

		<style>
	    	.pic_content:hover .off_btn{display:block;}
	    	.textbox.combo>input{width:90%!important;}
	    	.textbox.numberbox.spinner>input{width:90%!important;line-height:30px;}
	    	.textbox.numberbox>input{width:100%!important;line-height:30px;}
	    	.textbox-addon.textbox-addon-right>a{margin-top:5px!important;}
    		.l-btn.l-btn-small {color: #444!important;background:-webkit-linear-gradient(top,#ffffff 0,#d8d8d8 100%)!important;}
			.parent_div {width: 92%;border: solid 1px #c7c7c7;overflow: hidden;}
			.det-nav-wrap > li > a:hover, .det-nav-wrap > li > .active {background-color: #5294e8!important;}
	    	.fw-det-tog-top > h5 > i {background-color:#5294e8!important;}
	    	.btn-warp .btn-bon {background-color:#5294e8!important;;color: rgb(255, 255, 255)!important}}
	    </style>
	</head>
	<body class="xiu-body">
	   <#if attList?? && attList?size gt 0>
            <input type="hidden" id="global_index" value="${attList?size}">
            <#else>
            <input type="hidden" id="global_index" value="0">
       </#if>
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/disputeMediation/save.jhtml?isPerfect=${isPerfect!''}"  method="post">
		<input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
        <input type="hidden" id="mediationResId" name="mediationResId" value="<#if disputeMediation.mediationResId??>${disputeMediation.mediationResId}</#if>">
        <input type="hidden" id="gridId" name="gridId" value="<#if disputeMediation.gridId??>${disputeMediation.gridId}</#if>">
        <input type="hidden" id="gridCode" name="gridCode" value="">
        <input type="hidden" id="id" name="id" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>"/>
        <input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>"/>
        <input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
        <input type="hidden" id="mediationCode" name="mediationCode" value="<#if disputeMediation.mediationCode??>${disputeMediation.mediationCode}</#if>">
        <input type="hidden" id="disputeStatus" name="disputeStatus" value="<#if disputeMediation.disputeStatus??>${disputeMediation.disputeStatus}</#if>">
        <input type="hidden" id="status" name="status" value="<#if disputeMediation.status??>${disputeMediation.status}</#if>">
        <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
        <input type="hidden" id="eventSeq" name="eventSeq" value="" />
        <input type="hidden" id="isPerfect" name="isPerfect" value="${isPerfect!''}" />
		<div class="container-detail flex">
			<div class="det-nav">
				<div class="det-nav-pons">
					<ul class="det-nav-wrap det-nav-wrap2">
						<li>
							<a href="#1" class="active flex flex-ac flex-jc" style="text-decoration:none;"><i class="det-dis-none"></i>纠纷基本信息</a><!--要使用图标i时，把det-dis-none类去掉-->
						</li>
						<li>
							<a href="#2" class="flex flex-ac flex-jc" style="text-decoration:none;"><i class="det-dis-none"></i>主要当事人信息</a>
						</li>
						<li>
							<a href="#3" class="flex flex-ac flex-jc" style="text-decoration:none;"><i class="det-dis-none"></i>化解信息</a>
						</li>
						<li>
							<a href="#4" class="flex flex-ac flex-jc" style="text-decoration:none;"><i class="det-dis-none"></i>考评信息</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="det-wrapper flex1">
				<div class="container_fluid">
					<div class="form-warp-sh "><!-- 外框 -->
						<div class="fw-toptitle">
							<h6 class="note-s">带<span>*</span>为必填项</h6>
						</div>
						<div class="fw-main">
							<!--基础信息-->
							<div class="fw-det-tog " id="1">
								<div class="fw-det-tog-top" >
									<h5><i></i>基础信息</h5>
									<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"/> </a>
								</div>
								<div class="fw-det-toggle">
									<ul class="fw-xw-from clearfix">
										<li class="xw-com3" style="width:885px;">
											<span class="fw-from1"><i class="spot-xh">*</i>事件名称：</span>
											<input name="disputeEventName" id="disputeEventName" maxLength="100" type="text" class="inp1 easyui-validatebox from flex1 bg-btm-arrow1"  value="<#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" placeholder="请输入事件标题"/>
										</li>
										<li class="xw-com1">
											<span class="fw-from1"><i class="spot-xh">*</i>事件简述：</span>
											<input name="disputeCondition" id="disputeCondition" class="inp1 easyui-validatebox from flex1 bg-btm-arrow1" type="text" value="<#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if>" data-options="tipPosition:'bottom',required:true, validType:['maxLength[1000]','characterCheck']" placeholder="请输入事件描述"/>
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>事发时间：</span>
											<input id="happenTimeStr" name="happenTimeStr" type="text" class="inp1 inp2 Wdate easyui-validatebox from flex1 bg-btm-arrow1 pr30" style="width:100%" readonly="readonly" data-options="required:true"  data-options="required:true,tipPosition:'bottom'"  value="<#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if>"/>
											<a href="javascript:void(0);" onclick="WdatePicker({el:'happenTimeStr',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="time-switch flex flex-ac flex-jc">
												<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
											</a>
										</li>
										<li class="xw-com3">
											<!--必填框未填写添加unfilled类，添加属性placeholder内容未"您还未输入地区所属网格"-->
											<span class="fw-from1">所属区域：</span>
											<input name="gridName" id="gridName" type="text" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow1" readonly value="<#if disputeMediation.gridName??>${disputeMediation.gridName}</#if>" data-options="required:true,tipPosition:'bottom'"/>
										</li>
										
										<li class="xw-com3">
											<span class="fw-from1">受理日期：</span>
											<input id="acceptedDateStr" name="acceptedDateStr" type="text" readonly="readonly" class="inp1 inp2 Wdate  from flex1 bg-btm-arrow2"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'happenTimeStr\')}', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if>"/>
											<a href="javascript:void(0);" onclick="WdatePicker({el:'acceptedDateStr',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'happenTimeStr\')}'})" class="time-switch flex flex-ac flex-jc">
												<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
											</a>
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>化解时限：</span>
											<input id="mediationDeadlineStr" name="mediationDeadlineStr" type="text" readonly="readonly" class="Wdate inp1 inp2 easyui-validatebox from flex1 bg-btm-arrow2" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'acceptedDateStr\')}',readOnly:true, acceptedDateStr:true})" value="<#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if>"/>
											<a href="javascript:void(0);" onclick="WdatePicker({el:'mediationDeadlineStr',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'acceptedDateStr\')}'})" class="time-switch flex flex-ac flex-jc">
												<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
											</a>
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>事发地址：</span>
											<input type="hidden" id="happenAddrCode" name="happenAddrCode" value="<#if disputeMediation.happenAddrCode??>${disputeMediation.happenAddrCode}</#if>" >
 											<input  name="happenAddr" id="happenAddr" class="inp1 easyui-validatebox from flex1 bg-btm-arrow1 pr30" type="text" style="width:260px; height:28px;" value="<#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if>"  placeholder="请输入选择事件发生地址" data-options="required:true" >
											 <!-- <a href="javascript:void(0);" class=" time-switch address-switch flex flex-ac flex-jc">
											</a> -->
										</li>


										<li class="xw-com3">
											<span class="fw-from1">地理标注：</span>
											 <a href="javascript:void(0);" class="geographical-position flex flex-ac">
												<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
											</a>
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>事件类别：</span>
											<input type="hidden" id="disputeType2" name="disputeType2" value="<#if disputeMediation.disputeType2??>${disputeMediation.disputeType2}</#if>" >
											<input name="disputeType2Str" id="disputeType2Str" class="inp1 easyui-validatebox from flex1 bg-btm-arrow2" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']"  placeholder="请选择">
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>事件规模：</span>
											<input type="hidden" id="disputeScale" name="disputeScale" value="<#if disputeMediation.disputeScale??>${disputeMediation.disputeScale}</#if>" >
											<input name="disputeScaleStr" id="disputeScaleStr" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请选择">
										</li>
										
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>影响范围：</span>
											 <input id="scopeInfluenece" name="scopeInfluenece" type="hidden" value="<#if disputeMediation.scopeInfluenece??>${disputeMediation.scopeInfluenece}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" >
											<input name="scopeInflueneceName" id="scopeInflueneceName" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入影响范围">
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>事件性质：</span>
											<input id="eventNature" name="eventNature" type="hidden"  value="<#if disputeMediation.eventNature??>${disputeMediation.eventNature}</#if>" data-options="tipPosition:'bottom'">
											<input name="eventNatureName" id="eventNatureName" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入影响范围">
										</li>
										<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>涉及人数(人)：</span>
											<input id="involveNum" name="involveNum" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow1" type="text" value="<#if disputeMediation.involveNum??>${disputeMediation.involveNum}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck'],events: {focus:daysOnBlur}"/>
										</li>
										<li class="xw-com3">
											<span class="fw-from1">涉及金额(元)：</span>
											<input name="involvedAmount" id="involvedAmount" class="from flex1 bg-btm-arrow2" type="text" value="<#if disputeMediation.involvedAmount??>${disputeMediation.involvedAmount?string('0.00')}</#if>" data-options="events: {focus: daysOnBlur}"/>
										</li>
										<li class="xw-com3">
											<span class="fw-from1">风险类型：</span>
											<input id="riskCode" name="riskCode" type="hidden"  value="<#if disputeMediation.riskCode??>${disputeMediation.riskCode}</#if>" data-options="tipPosition:'bottom'">
											<input name="riskCodeName" id="riskCodeName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入影响范围"> 
											<!-- <input name="riskCode" id="riskCode" class="from flex1 bg-btm-arrow1" type="text" value="<#if disputeMediation.riskCode??>${disputeMediation.riskCode}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入风险类型"> -->
										</li> 
									 	<li class="xw-com3">
											<span class="fw-from1">风险等级：</span>
											<input id="riskGrade" name="riskGrade" type="hidden"  value="<#if disputeMediation.riskGrade??>${disputeMediation.riskGrade}</#if>" data-options="tipPosition:'bottom'">
											<input name="riskGradeName" id="riskGradeName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入影响范围">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">案情评估：</span>
											<input id="caseAssessment" name="caseAssessment" type="hidden"  value="<#if disputeMediation.caseAssessment??>${disputeMediation.caseAssessment}</#if>" data-options="tipPosition:'bottom'">
											<input name="caseAssessmentName" id="caseAssessmentName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入案情评估">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">激烈程度：</span>
											<input id="intenseDegree" name="intenseDegree" type="hidden"  value="<#if disputeMediation.intenseDegree??>${disputeMediation.intenseDegree}</#if>" data-options="tipPosition:'bottom'">
											<input name="intenseDegreeName" id="intenseDegreeName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入激烈程度">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">纠纷等级：</span>
											<input id="disputeLevel" name="disputeLevel" type="hidden"  value="<#if disputeMediation.disputeLevel??>${disputeMediation.disputeLevel}</#if>" data-options="tipPosition:'bottom'">
											<input name="disputeLevelName" id="disputeLevelName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入纠纷等级">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">预警等级：</span>
											<input id="warningLevel" name="warningLevel" type="hidden"  value="<#if disputeMediation.warningLevel??>${disputeMediation.warningLevel}</#if>" data-options="tipPosition:'bottom'">
											<input name="warningLevelName" id="warningLevelName" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入预警等级">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">矛盾纠纷类型：</span>
											<input id="disputeType3" name="disputeType3" type="hidden"  value="<#if disputeMediation.disputeType3??>${disputeMediation.disputeType3}</#if>" data-options="tipPosition:'bottom'">
											<input name="disputeType3Name" id="disputeType3Name" class="from flex1 bg-btm-arrow1" type="text" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入矛盾纠纷类型">
										</li> 
										<li class="xw-com3">
											<span class="fw-from1">涉及单位：</span>
											<input name="involvedOrgName" id="involvedOrgName" class="inp1 easyui-validatebox from flex1 bg-btm-arrow1" type="text" value="<#if disputeMediation.involvedOrgName??>${disputeMediation.involvedOrgName}</#if>" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" placeholder="请输入涉及单位"/>
										</li>
										<li class="xw-com1">
											<span class="fw-from1">措施手段：</span>
											<textarea class="from flex1 bg-btm-arrow1" style="height: 50px;" name="workOrderDetail" id="workOrderDetail" type="text" value="<#if disputeMediation.workOrderDetail??>${disputeMediation.workOrderDetail}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="请输入措施手段详细情况">${disputeMediation.workOrderDetail}</textarea>
										</li>
										
										<li class="xw-com1">
											<span class="fw-from1">工作记载：</span>
											<textarea class="from flex1 bg-btm-arrow1" style="height: 50px;" type="text" name="measureDetail" id="measureDetail"  value="<#if disputeMediation.measureDetail??>${disputeMediation.measureDetail}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']" placeholder="工作记载详细情况">${disputeMediation.measureDetail}</textarea>
										</li>
										
										<li class="xw-com1">
											<span class="fw-from1" style="margin-top:3%;">附件上传：</span>
											<div id="bigupload_1" class="parent_div"></div>
										</li>
										
									</ul>
								</div>
							</div>
							<!--主要当事人信息-->
							<div class="fw-det-tog mt10" id="2">
								<div class="fw-det-tog-top" >
									<h5><i></i>主要当事人信息</h5>
									<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"/> </a>
								</div>
								<div class="fw-det-toggle">
									
									<div id="main_people" style="margin-top:10px;"><span onclick="addMainPeople()"><span class="btn_add" style="margin-left:45%;color: #f08750;font-size:12px;cursor: pointer;">+添加主要当事人信息</span></span></div>
									
								</div>
							</div>
							
							<!--化解信息-->
							<div class="fw-det-tog mt10" id="3">
								<div class="fw-det-tog-top" >
									<h5><i></i>化解信息</h5>
									<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"/> </a>
								</div>
								<div class="fw-det-toggle">
									<ul class="fw-xw-from clearfix">
										<li class="xw-com3">
											<span class="fw-from1">化解方式：</span>
											<input type="hidden" id="mediationType" name="mediationType" value="<#if disputeMediation.mediationType??>${disputeMediation.mediationType}</#if>">
											<input name="mediationTypeStr" id="mediationTypeStr" class="from flex1 bg-btm-arrow2" type="text" placeholder="请选择">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">最后调解日期：</span>
											<input id="mediationDateStr" name="mediationDateStr" type="text" class="from flex1 bg-btm-arrow1 pr30" readonly="readonly" data-options="required:true" onclick="WdatePicker({el:'mediationDateStr',dateFmt:'yyyy-MM-dd'})" data-options="required:true,tipPosition:'bottom'"  value="<#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if>"/>
											<!-- <input class="from flex1 bg-btm-arrow1 pr30" type="text" placeholder="请选择"> -->
											<a href="javascript:void(0);" onclick="WdatePicker({el:'mediationDateStr',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'happenTimeStr\')}'})" class="time-switch flex flex-ac flex-jc">
												<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
											</a>
										</li>
										<li class="xw-com3">
											<span class="fw-from1">化解责任人姓名：</span>
											<input name="mediator" id="mediator" class="from flex1 bg-btm-arrow1" type="text" value="<#if disputeMediation.mediator??>${disputeMediation.mediator}</#if>"  data-options="validType:['maxLength[15]','characterCheck']">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">化解责任人身份证：</span>
											<input name="hjCertNumber" id="hjCertNumber" class="inp1 from flex1 bg-btm-arrow1 easyui-validatebox" type="text" value="<#if disputeMediation.hjCertNumber??>${disputeMediation.hjCertNumber}</#if>"  data-options="validType:['idcard']">
										</li>
										<li class="xw-com3">
											<span class="fw-from1">化解组织：</span>
											<input name="mediationOrgName" id="mediationOrgName" class="from flex1 bg-btm-arrow1 easyui-validatebox" type="text" value="<#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>"  data-options="validType:['maxLength[50]','characterCheck']"/>
										</li> 
										<li class="xw-com3">
											<span class="fw-from1">化解是否成功：</span>
											 <input type="hidden" id="isSuccess" name="isSuccess"  value="<#if disputeMediation.isSuccess??>${disputeMediation.isSuccess}</#if>">
											 <input name="isSuccessStr" id="isSuccessStr" class="from flex1 bg-btm-arrow2" type="text" placeholder="请选择">
										</li>
										<li class="xw-com3">
											<span class="fw-from1" style="margin-left:-6.7px!important;">化解责任人联系方式：</span>
											<input name="mediationTel" id="mediationTel" class="inp1 from flex1 bg-btm-arrow1 easyui-numberbox" type="text" value="<#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if>" maxlength="11" data-options="validType:['maxLength[11]','characterCheck']"/>
										</li>
										<li class="xw-com1">
											<span class="fw-from1">化解情况：</span>
											<textarea name="mediationResult" id="mediationResult" class="from flex1 bg-btm-arrow1 easyui-validatebox" style="height: 50px;" type="text" value="<#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if>" data-options="tipPosition:'bottom',validType:['maxLength[1000]','characterCheck']" placeholder="请输入化解情况">${disputeMediation.mediationResult}</textarea>
										</li>
										
									</ul>
								</div>
							</div>
							
							<!--考评信息-->
							<div class="fw-det-tog mt10" id="4">
								<div class="fw-det-tog-top" >
									<h5><i></i>考评信息</h5>
									<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"/> </a>
								</div>
								<div class="fw-det-toggle">
									<ul class="fw-xw-from clearfix">
										<li class="xw-com3">
											<span class="fw-from1">考评日期：</span>
											<input id="evaDateStr" name="evaDateStr" type="text" readonly="readonly" class="inp1 inp2 Wdate from flex1 bg-btm-arrow2"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if>"/>
											<a href="javascript:void(0);" onclick="WdatePicker({el:'evaDateStr',dateFmt:'yyyy-MM-dd'})" class="time-switch flex flex-ac flex-jc">
												<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
											</a>
										</li>
										<li class="xw-com1">
											<span class="fw-from1">考评意见：</span>
											<textarea name="evaOpn" id="evaOpn" class="from flex1 bg-btm-arrow1 easyui-validatebox" style="height: 50px;" type="text" value="<#if disputeMediation.evaOpn??>${disputeMediation.evaOpn}</#if>" data-options="tipPosition:'bottom',validType:['maxLength[1000]','characterCheck']" placeholder="请输入考评意见">${disputeMediation.evaOpn}</textarea>
										</li>
									</ul>
								</div>
							</div>
								
						</div>
					</div>
					<div class="btn-warp">
						<a class="btn-bon" style="background: #f54952;" onclick="javascript:tableSubmit();">保存</a>
						<a class="btn-bon btn-cancel" style="background: #fff;" onclick="javascript:cancl();">取消</a>
					</div>
				</div><!--container-fluid-->
			</div>
		</div><!--container-fluid-->
		</form>
		<#include "/component/FieldCfg.ftl" />
		<#include "/component/ComboBox.ftl">
		<#include "/zzgl/dispute/mediation_9x/map/maxJqueryEasyUIWin.ftl" />
		<#include "/component/residentSelector.ftl" />
		<script type="text/javascript" src="${rc.getContextPath()}/js/module/resident/residentSelector.js"></script>
		<script src="${rc.getContextPath()}/dispute/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script> 
		<#--<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown-1.1/swfupload/swfupload.js"></script>-->
		<#--<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown-1.1/swfupload/handlers.js"></script>-->
		
		<#--<script src="${rc.getContextPath()}/ui/workorder/js/ajaxupload.js" type="text/javascript" charset="utf-8"></script>-->
		<#--<script src="${rc.getContextPath()}/ui/workorder/js/xinj.workorder.common.js?v_=201806072033" type="text/javascript" charset="utf-8"></script>-->
		<#--<script src="${rc.getContextPath()}/ui/workorder/js/xinj.workorder.create.js?v_=201806072034" type="text/javascript" charset="utf-8"></script>-->
		<script>
			var index = 0;
            $(function () {
                //事发详址
                addressObj = $("#happenAddr").anoleAddressRender({
                _source : 'XIEJING',//必传参数，数据来源
                _startDivisionCode : "${orgCode}", //默认选中网格，非必传参数
                //_startDivisionCode : "350102001001001", //默认选中网格，非必传参数
                // _startAddress : "福建省晋江市永嘉北路六里1号",
//                _startDivisionCode : "CN", //默认选中网格，非必传参数
                //_addressFormate: "省-市-县-镇",//户籍地可以用到
                _start_level : 0, //从哪个层级开始显示，非必传参数，如果_startDivisionCode参数有传的话，_start_level这个参数设置的层级应该大于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置0-3
                //_select_scope : 0,		 //选择范围（配置了该参数后需要传参数_startDivisionCode的值。0：辖区，当前及以下；1：建设范围，读取功能配置；2：所有，需要读取mysql的数据源；3：自定义选择范围，需要和参数配合使用。）
                _show_level : 6,  //显示到哪个层级，7--网格，非必传参数，如果_startDivisionCode参数有传的话，_show_level这个参数设置的层级应该小于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置3-6
                _addressMap : {//编辑页面可以传这个参数，非必传参数
                    _addressMapShow : false//是否显示地图标注功能
//                    _addressMapIsEdit : true,
//                    _addressMapX : 118.54727185058594,
//                    _addressMapY : 24.785271514892578,
//                    _addressMapType : 5
                },
                BackEvents : {
                    OnSelected : function(api) {
                        $("#happenAddr").val(api.getAddress());
                        $("#happenAddrCode").val(api.getInfoOrgCode());
                    },
                    OnCleared : function(api) {
                        //清空按钮触发的事件
                        $("#happenAddrCode").val('');
                        $("#happenAddr").val('');
                    },
                    OnCompleted : function(api) {
                        console.log(api.addressData);
                        console.log("地址的所属网格:"+api.addressData._infoOrgCode);
                    }
                }
            });



            	<#if involvedPeoples??>
	        		<#list involvedPeoples as l>
	        		   detailMainPeople("${l.ipId!''}")
	        		</#list>
        		</#if>
        		getMapMarkerData();
        		//showMap()
//         	addMainPeople();

	            layer.load(0);// 加载遮罩层
	            <#if displayTaiwangFlag??&&displayTaiwangFlag=='true'>
	            	var moduleCode = "dispute_mediation_taiwan";
	        	<#else>
	            	var moduleCode = "dispute_mediation";
	        	</#if>
                  $.excuteFieldCfg({
                    moduleCode: moduleCode,// 必传，模块编码
                    infoOrgCode: ""// 可选，不传取默认登录信息域编码
                }, function(isSuccess, msg) {// 回调函数，isSuccess：true成功/false失败
                    if(isSuccess != true) {
                        $.messager.alert('错误', msg, 'error');
                    }
                    layer.closeAll('loading'); // 关闭加载遮罩层
                });
                  
                //格式化涉及金额
                $("#involvedAmount").numberbox({
                    groupSeparator:',',
                    precision:2,
                    min:0,
                    groupSeparator:','
                });
                $("#involveNum").numberspinner({
                    min:1,
                    max:999,
                    editable: true
                }); 
                $('.textbox.numberbox.spinner').css({'width':$('#scopeInflueneceName').width()+50,'height':'30px'});
                $('.textbox.numberbox').css({'width':$('#scopeInflueneceName').width()+50,'height':'30px'});

                AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
                    if(items!=undefined && items!=null && items.length>0){
                        var grid = items[0];
                        $("#gridCode").val(grid.orgCode);
                    }
                });


                AnoleApi.initListComboBox("disputeScaleStr", "disputeScale", "${DISPUTE_SCALE_CODE}", null<#if disputeMediation.disputeScale??>, ["${disputeMediation.disputeScale}"]</#if>);
                AnoleApi.initTreeComboBox("disputeType2Str", "disputeType2", "${disputeTypeDict!''}", null<#if disputeMediation.disputeType2??>, ["${disputeMediation.disputeType2}"]</#if>);
                AnoleApi.initTreeComboBox("mediationTypeStr", "mediationType", "B417", null<#if disputeMediation.mediationType??>, ["${disputeMediation.mediationType}"]</#if>);
                AnoleApi.initListComboBox("scopeInflueneceName", "scopeInfluenece", "${SCOPE_INFLUNECE}", null<#if disputeMediation.scopeInfluenece??>, ["${disputeMediation.scopeInfluenece}"]</#if>);
                AnoleApi.initListComboBox("eventNatureName", "eventNature", "${EVENT_NATURE}", null<#if disputeMediation.eventNature??>, ["${disputeMediation.eventNature}"]</#if>);
                AnoleApi.initListComboBox("riskCodeName", "riskCode", "LM90112", null, ["${disputeMediation.riskCode}"]);
                AnoleApi.initListComboBox("riskGradeName", "riskGrade", "LM10017", null, ["${disputeMediation.riskGrade}"]);

                AnoleApi.initListComboBox("caseAssessmentName", "caseAssessment", "DM94696", null, ["${disputeMediation.caseAssessment}"]);
                AnoleApi.initListComboBox("intenseDegreeName", "intenseDegree", "DM94697", null, ["${disputeMediation.intenseDegree}"]);
                AnoleApi.initListComboBox("disputeLevelName", "disputeLevel", "DM94698", null, ["${disputeMediation.disputeLevel}"]);
                AnoleApi.initListComboBox("warningLevelName", "warningLevel", "DM94699", null, ["${disputeMediation.warningLevel}"]);
                AnoleApi.initListComboBox("disputeType3Name", "disputeType3", "DM01588", null, ["${disputeMediation.disputeType3}"]);

                //化解是否成功
       	     	AnoleApi.initListComboBox("isSuccessStr", "isSuccess", null, null, ["${disputeMediation.isSuccess!'0'}"], {
	       	        RenderType : "00",
	       	        DataSrc : [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
	       	        ShowOptions : {
	       	            EnableToolbar : true
	       	        }
           		});
                $('.headlist').on('click', 'li a', function(){
                    $(this).addClass('bd-on').parent().siblings().children().removeClass('bd-on');
                });
            	$('.det-nav-wrap').on('click', 'li a', function(){
					$(this).addClass('active').parent().siblings().children().removeClass('active');
				});
                var src;
                $('.fw-det-tog-top').on('click', function(){
                    $(this).siblings('.fw-det-toggle').toggle(300);
                    src = $(this).find('img').attr('src');
                    if(src == '${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif'){
                        $(this).find('img').attr("src","${rc.getContextPath()}/style/images/icon_fw_detail_down.gif");
                    }else{
                        $(this).find('img').attr("src","${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif");
                    }
                });
                var $winH, $topH, $btnH;
                $(window).on('load resize', function () {
                    $winH = $(window).height();
                    $topH = $('.fw-toptitle').height();
                    $btnH = $('.btn-warp').height();
                    $('.fw-main').height($winH - $topH - $btnH - 76);
                    $(".fw-main").niceScroll({
                        cursorcolor:"rgba(0, 0, 0, 0.3)",
                        cursoropacitymax:1,
                        touchbehavior:false,
                        cursorwidth:"4px",
                        cursorborder:"0",
                        cursorborderradius:"4px"
                    });
                });

				uploadFile();
				//做电话正则:如果说输入的电话号码不正确则给出提示,正确则不显示提示(添加，修改)
                $.extend($.fn.validatebox.defaults.rules, {
                    telNum:{ //既验证手机号，又验证座机号
                        validator: function(value, param){
                            return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
                        },
                        message: '请输入正确的电话号码'
                    }
                });
            });
            //是否发送站内信
            $('.send-letter').click(function(){
            	$(this).find('.select-box').toggleClass('send-box-active');
            })
            //地址选择框的显示与隐藏
            $('.address-switch').click(function(){
            	$('.address-box').show();
            })
            $('.address-pic-box img').click(function(){
            	$('.address-box').hide();
            })
            
            var cardTypeApi;
            var nationApi;
            var eduApi;
            var involvedPeopleType;
            function addMainPeople(){
 		        var main_people_html = '<div id="sel_'+index+'" style="border:1px #e5e5e5 solid;margin-top:5px">'+
		        						'<ul class="fw-xw-from clearfix" style="margin-top:15px">'+
								        '<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>姓名：</span>'+
											'<input type="text" id="i_name'+index+'" name="involvedPeople['+index+'].i_name" class="inp1 easyui-validatebox residentSelector from flex1 bg-btm-arrow2" value="${involvedPeople.name!''}" data-options="required:true" resident-Option="{panelWidth:470,panelHeight:210,orgCode:${orgCode},width:115,callback:nameSelected,textField:\'partyName\'}"/>'+
				                            '<input name="involvedPeople['+index+'].name" id="name'+index+'" type="hidden" value="${involvedPeople.name!''}"/>'+
										'</li>'+
								        '<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>性别：</span>'+
											 '<select id="sex'+index+'" name="involvedPeople['+index+'].sex" data-options="required:true" class="inp1  InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2">'+
								               '<#if disputeMediation.sex??>'+
					                               '<option value=""></option>'+
					                               '<option value="M" <#if ("M"==involvedPeople.sex)>selected="selected"</#if>>男</option>'+
					                               '<option value="F" <#if ("F"==involvedPeople.sex)>selected="selected"</#if>> 女</option>'+
											   '<#else>'+
								                   '<option selected="selected" value=""></option>'+
								                   '<option value="M">男</option>'+
								                   '<option value="F">女</option>'+
											   '</#if>'+
				                            '</select>'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>证件类型：</span>'+
											'<input type="hidden" id="cardType'+index+'" name="involvedPeople['+index+'].cardType" value="${involvedPeople.cardType!''}">'+
											'<input name="involvedPeople['+index+'].cardTypeStr" id="cardTypeStr'+index+'" type="text"  data-options="required:true,tipPosition:\'bottom\'" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2"/>'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>证件号码：</span>'+
											'<input name="involvedPeople['+index+'].idCard" id="idCard'+index+'" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[100]\',\'characterCheck\']" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow1" type="text" value="${involvedPeople.idCard!''}" placeholder="请输入证件号码">'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>民族：</span>'+
											'<input name="involvedPeople['+index+'].nation" id="nation'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${involvedPeople.nation!''}" />'+
											'<input name="involvedPeople['+index+'].nationStr" id="nationStr'+index+'" style="width:110px;" type="text" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2" />'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>人员类别：</span>'+
											'<input name="involvedPeople['+index+'].peopleType" id="peopleType'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${involvedPeople.peopleType!''}"/>'+
											' <input name="involvedPeople['+index+'].peopleTypeStr" id="peopleTypeStr'+index+'" type="text" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2" />'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1">学历：</span>'+
											'<input name="involvedPeople['+index+'].edu" id="edu'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${involvedPeople.edu!''}" />'+
				                            '<input name="involvedPeople['+index+'].eduName" id="eduName'+index+'" style="width:110px;" type="text" data-options="" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2" />'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>当事人类型：</span>'+
											'<input name="involvedPeople['+index+'].involvedPeopleType" id="involvedPeopleType'+index+'" type="hidden" value="${involvedPeople.involvedPeopleType!''}" />'+
				                            '<input name="involvedPeople['+index+'].involvedPeopleTypeStr" id="involvedPeopleTypeStr'+index+'" style="width:110px;" type="text" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow2" />'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1"><i class="spot-xh">*</i>居住详址：</span>'+
											'<input name="involvedPeople['+index+'].homeAddr" id="homeAddr'+index+'" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox validatebox-text validatebox-invalid from flex1 bg-btm-arrow1" type="text" value="${involvedPeople.homeAddr!''}" placeholder="请输入居住地址">'+
										'</li>'+
										'<li class="xw-com3">'+
											'<span class="fw-from1">联系电话：</span>'+
											'<input name="involvedPeople['+index+'].tel" id="tel'+index+'" data-options="required:false,prompt:\'请输入正确的手机号码\' , validType:\'telNum\'"  class="inp1 inp2 InpDisable easyui-validatebox validatebox-text  from flex1 bg-btm-arrow1" type="text" value="${involvedPeople.tel!''}" placeholder="请输入联系电话">'+
										'</li>'+
										'<li class="xw-com1">'+
											'<a href="#" class="BigNorToolBtn RejectBtn qwe" style="margin-left: 92%;background-color: #ccc;" onclick="javascript:deltd('+index+');">删除</a>'+
										'</li>'+
								'</ul></div>';
 		        var data = null;
		        $('#main_people').before(main_people_html);
		        $.parser.parse($('#sel_'+index));
		        //$('#i_name'+index_).css('display','block');
		        /* 主要当事人信息数据字典 */
	            cardTypeApi = AnoleApi.initListComboBox("cardTypeStr"+index, "cardType"+index, "D030001", null<#if involvedPeople.cardType??>, ["${involvedPeople.cardType!''}"]</#if>);
	            AnoleApi.initTreeComboBox("peopleTypeStr"+index, "peopleType"+index, "B416", null<#if involvedPeople.peopleType??>, ["${involvedPeople.peopleType!''}"]</#if>);
	            nationApi = AnoleApi.initListComboBox("nationStr"+index, "nation"+index, "D177003", null<#if involvedPeople.nation??>, ["${involvedPeople.nation!''}"]</#if>);
	            eduApi = AnoleApi.initListComboBox("eduName"+index, "edu"+index, "B064", null, ["${involvedPeople.edu!''}"]);
	            //当事人类型代码
			    involvedPeopleType = AnoleApi.initListComboBox("involvedPeopleTypeStr"+index, "involvedPeopleType"+index, "B230", null, ["${involvedPeople.involvedPeopleType!''}"]);
		        $('#i_name'+index).initResidentSelector();
		        $('#i_name'+index).nextAll('span').first().css({'width':$('#scopeInflueneceName').width()+50,'height':'30px'});
		        
		        index+=1;
		    };
		    function nameSelected(el,data){//姓名选择
		    	var index_= el.attr('id').replace('i_name','');
		    	$('#i_name'+index_).val(data.partyName);
		        $('#name'+index_).val(data.partyName);
		        $('#idCard'+index_).val(data.identityCard);
		        $('#homeAddr'+index_).val(data.residence);
		        if(data.gender == '1'){
		        	$('#sex'+index_).val('F');
		      	}else{
		      		$('#sex'+index_).val('M');
		      	}
		        nationApi.setSelectedNodes([data.nation]);
		        eduApi.setSelectedNodes([data.educationLevel]);
		        cardTypeApi.setSelectedNodes([data.certType]);
		        
		        $.parser.parse($('#sel_'+index_));
		    }
		    
		    function deltd(index_){
		    	$('#sel_'+index_).remove();
		    }
		    function closeInvPeople(index){
		        $(obj).parents('table:first').prev().remove();
		        $(obj).parents('table:first').remove();
		    }

		    
		    function tableSubmit(){
		        var isValid =  $("#tableForm").form('validate');
		        var a = $(".qwe").length
	            if( a ==0 ){
	            	$.messager.alert('提示', '添加主要当事人信息', 'info', function() {});
	            	return;
	            }
				if($("#mediationTel").val() !='' && $("#mediationTel").val().length != 11){
					$.messager.alert('提示', '化解责任人联系方式格式不正确', 'info', function() {});
	            	return;
				}
				for(var i=0;i<=index;i++){
					var telVal = $('#tel'+i).val()==undefined?"":$('#tel'+i).val();
					if(telVal !='' && !telVal.startWith('1')){
						$.messager.alert('提示', '联系电话格式不正确', 'info', function() {});
		            	return;
					}
					if(telVal !=undefined && telVal !='' && telVal.length != 11 ){
						$.messager.alert('提示', '联系电话格式不正确', 'info', function() {});
		            	return;
					}
				} 
		        if("3"==$("#disputeStatus").val()){
	                if(a < 2){
	                	$.messager.alert('提示', '结案时当事人至少两人', 'info', function() {});
		            	return;
	                }
	                if(!valid()){
	                	return;
	                };
	            }
		        if(isValid){
		            modleopen();
		            $("#status").val("1");
		            var flag = 0;
		            $("#tableForm").ajaxSubmit(function(data) {
		            	
		                if(data.success){
	                    	parent.reloadDataForSubPage(data.tipMsg);
		                }else {
		                    modleclose();
		                    $.messager.alert('错误', '结案失败', 'error');
		                }
		            });
		
		        }
		
		
		    }

		    function showResidentSelector(){
		        showInvoledPeopleSelector();
		    }
		
		    function cancl(){
		        parent.closeMaxJqueryWindow();
		    }

			function showMap(){
		
				var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
				var width = 480;
				var height = 360;
				var gridId = $("#gridId").val();
				var markerOperation = $('#markerOperation').val();
				var id = $('#eventId').val();
				var mapType = 'EVENT_V1';
				var isEdit = true;
				var parameterJson = getParameterJson();
				showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
			}
		    function getParameterJson() {
				var parameterJson={
					"id":$("#id").val(),
					"name":$("#name").val()
				}
				return parameterJson;
			}
		    
		    function detailMainPeople(ipId){
                if(ipId!=null&&ipId!=""){
                	$.ajax({
        			 	type: "POST",
        			 	url: '${rc.getContextPath()}/zhsq/involvedPeople/edit.jhtml?ipId='+ipId+'',
        			 	dataType:'json',
        			 	success: function(data){
							    $('#main_people').before(main_people_html (index,data));
							    //$.parser.parse($('#sel_'+index));
							    /* 主要当事人信息数据字典 */
							    //当事人类型代码
							    var involvedPeopleType = data.involvedPeople.involvedPeopleType || '1';
							    AnoleApi.initListComboBox("involvedPeopleTypeStr"+index, "involvedPeopleType"+index, "B230", null, [involvedPeopleType]);
							    cardTypeApi = AnoleApi.initListComboBox("cardTypeStr"+index, "cardType"+index, "D030001", null, [data.involvedPeople.cardType]);
							    AnoleApi.initTreeComboBox("peopleTypeStr"+index, "peopleType"+index, "B416", null, [data.involvedPeople.peopleType]);
							    nationApi = AnoleApi.initListComboBox("nationStr"+index, "nation"+index, "D177003", null, [data.involvedPeople.nation]);
							    eduApi = AnoleApi.initListComboBox("eduName"+index, "edu"+index, "D060001", null, [data.involvedPeople.edu]);
							    $("#i_name"+index).val(data.involvedPeople.name);
							    $("#name"+index).val(data.involvedPeople.name);
							    $('#i_name'+index).initResidentSelector();
							    $('#i_name'+index).nextAll('span').first().css({'width':$('#scopeInflueneceName').width()+50,'height':'30px'});
							    $("#idCard"+index).val(data.involvedPeople.idCard);
							    $("#homeAddr"+index).val(data.involvedPeople.homeAddr);
							    $("#tel"+index).val(data.involvedPeople.tel);
							    index+=1;
        			 		}
        			 	});
                }
            }

		    function main_people_html (index,data){
		    		var main_people_html = '<div id="sel_'+index+'" style="border:1px #e5e5e5 solid;margin-top:5px">'+
						'<ul class="fw-xw-from clearfix" style="margin-top:15px">'+
					        '<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>姓名：</span>'+
								'<input type="text" id="i_name'+index+'" name="involvedPeople['+index+'].i_name" class="residentSelector from flex1 bg-btm-arrow2" value="${data.involvedPeople.name!''}" data-options="required:true" style="height:28px;" resident-Option="{panelWidth:470,panelHeight:210,orgCode:${orgCode},width:115,callback:nameSelected,textField:\'partyName\'}"/>'+
		                        '<input name="involvedPeople['+index+'].name" id="name'+index+'" type="hidden" value="${data.involvedPeople.name!''}"/>'+
							'</li>'+
					        '<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>性别：</span>'+
							 	 '<select id="sex'+index+'" name="involvedPeople['+index+'].sex" class="from flex1 bg-btm-arrow2">';
					               if( data.involvedPeople.sex != ""){
					            	   main_people_html +=  '<option value="M" <#if ("M"==data.involvedPeople.sex)>selected="selected"</#if>>男</option>'+
		                               '<option value="F" <#if ("F"==data.involvedPeople.sex)>selected="selected"</#if>> 女</option>';
					               }else{
					            	   main_people_html +=   '<option selected="selected" value=""></option>'+
					                   '<option value="M">男</option>'+
					                   '<option value="F">女</option>';
					               }
		                    var html = main_people_html + '</select>'+ 
		                    
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>证件类型：</span>'+
								'<input type="hidden" id="cardType'+index+'" name="involvedPeople['+index+'].cardType">'+
								'<input name="involvedPeople['+index+'].cardTypeStr" id="cardTypeStr'+index+'" type="text" value="${data.involvedPeople.cardType!''}" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow1" readonly value="" />'+
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>证件号码：</span>'+
								'<input name="involvedPeople['+index+'].idCard" id="idCard'+index+'" class="from flex1 bg-btm-arrow1" type="text" value="${data.involvedPeople.idCard!''}" placeholder="请输入证件号码">'+
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>民族：</span>'+
								'<input name="involvedPeople['+index+'].nation" id="nation'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${data.involvedPeople.nation!''}" />'+
								'<input name="involvedPeople['+index+'].nationStr" id="nationStr'+index+'" style="width:110px;" type="text" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow2" data-options="required:true" readonly value="" class="inp1" />'+
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>人员类别：</span>'+
								'<input name="involvedPeople['+index+'].peopleType" id="peopleType'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${data.involvedPeople.peopleType!''}"/>'+
								'<input name="involvedPeople['+index+'].peopleTypeStr" id="peopleTypeStr'+index+'" type="text" value="${data.involvedPeople.peopleType!''}" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow2" readonly value="" class="inp1" data-options="required:true" />'+
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1">学历：</span>'+
								'<input name="involvedPeople['+index+'].edu" id="edu'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${data.involvedPeople.edu!''}" />'+
		                        '<input name="involvedPeople['+index+'].eduName" id="eduName'+index+'" style="width:110px;" type="text" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow2" data-options="" readonly value="" class="inp1" />'+
							'</li>'+
							
							
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>当事人类型：</span>'+
								'<input name="involvedPeople['+index+'].involvedPeopleType" id="involvedPeopleType'+index+'" type="hidden" class="inp1 inp2 easyui-validatebox" value="${data.involvedPeople.involvedPeopleType!''}" />'+
		                        '<input name="involvedPeople['+index+'].involvedPeopleTypeStr" id="involvedPeopleTypeStr'+index+'" style="width:110px;" type="text" class="inp1 inp2 InpDisable easyui-validatebox from flex1 bg-btm-arrow2" data-options="required:true" readonly value="" class="inp1" />'+
							'</li>'+
						
							
							'<li class="xw-com3">'+
								'<span class="fw-from1"><i class="spot-xh">*</i>居住详址：</span>'+
								'<input name="involvedPeople['+index+'].homeAddr" id="homeAddr'+index+'" class="from flex1 bg-btm-arrow1" type="text" value="${data.involvedPeople.homeAddr!''}" placeholder="请输入居住地址">'+
							'</li>'+
							'<li class="xw-com3">'+
								'<span class="fw-from1">联系电话：</span>'+
								'<input name="involvedPeople['+index+'].tel" id="tel'+index+'"  class="from flex1 bg-btm-arrow1 easyui-numberbox" oninput="value=value.replace(\/[^\\d]\/g,\'\')" maxlength="11" data-options="required:false,prompt:\'请输入正确的手机号码\' , validType:\'telNum\'" type="text" value="${involvedPeople.tel!''}" placeholder="请输入联系电话">'+
							'</li>'+
							
							'<li class="xw-com1">'+
								'<a href="#" class="BigNorToolBtn RejectBtn qwe" style="margin-left: 92%;background-color: #ccc;" onclick="javascript:deltd('+index+');">删除</a>'+
							'</li>'+
				'</ul></div>';
				
				return html;
		    }
		    
			function getMapMarkerData(){
				var id = $("#id").val(); // 业务id
				var module = $("#module").val(); // 模块
				var markerOperation = $("#markerOperation").val(); // 地图操作类型
				//var markerOperation = 1; // 地图操作类型
				var showName = "标注地理位置";
				$(".mapTab2").addClass("mapTab2");
				$.ajax({   
					 url: '${rc.getContextPath()}/zhsq/map/gis/getMapMarkerData.json?id='+id+'&module='+module+'&t='+Math.random(),
					 type: 'POST',
					 timeout: 3000,
					 dataType:"json",
					 async: false,
					 error: function(data){
					 	$.messager.alert('友情提示','地址库信息获取出现异常!','warning'); 
					 },
					 success: function(data){
					 	if (data != null) {
						 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
								if (data.x != "" && data.x != null) {
									showName = "修改地理位置";
								} else {
									showName = "标注地理位置";
								}
							} else if (markerOperation == 2) { // 查看标注
		                        if (data.x != "" && data.x != null) {
		                            showName = "查看地理位置";
		                        } else {
		                            showName = "未标注地理位置";
		                        }
							}

						 	if (data.x != "" && data.x != null) {
						 		$("#x").val(data.x);
						 	} else {
						 		$("#x").val("");
						 	}
						 	
						 	if (data.y != "" && data.y != null) {
						 		$("#y").val(data.y);
						 	} else {
						 		$("#y").val("");
						 	}
						 
					 		if (data.mapt != "" && data.mapt != null) {
						 		$("#mapt").val(data.mapt);
						 	}
		                    try{
		                        callbackMap(data.x,data.y);
		                    }catch(e){

		                    }
					 	}else{
		                    showName = "未标注地理位置";
						}
						$("#mapTab2").html(showName);
					 }
				});
			}
			
			//化解信息验证
			function valid (){
				if($("#mediationTypeStr").val() == '请选择'){
					$.messager.alert('提示', '化解方式不能为空', 'info', function() {});
	            	return false;
				}
			/*	if($("#mediationDateStr").val() == ''){
					$.messager.alert('提示', '化解日期不能为空', 'info', function() {});
					return false;
				}*/
				if($("#mediator").val()== ''){
					$.messager.alert('提示', '化解责任人姓名不能为空', 'info', function() {});
					return false;
				}
				/* if($("#hjCertNumber").val()== ''){
					$.messager.alert('提示', '化解责任人身份证不能为空', 'info', function() {});
					return false;
				}*/
				if($("#mediationOrgName").val()== ''){
					$.messager.alert('提示', '化解组织不能为空', 'info', function() {});
					return false;
				} 
				if($("#isSuccessStr").val()== '请选择'){
					$.messager.alert('提示', '化解是否成功不能为空', 'info', function() {});
	            	return false;
				}
				if($("#mediationTel").val()== ''){
					$.messager.alert('提示', '化解责任人联系方式不能为空', 'info', function() {});
	            	return false;
				}
				if($("#mediationTel").val().length != 11){
					$.messager.alert('提示', '化解责任人联系方式格式不正确', 'info', function() {});
	            	return false;
				}
				if($("#mediationResult").val() == ''){
					$.messager.alert('提示', '化解情况不能为空', 'info', function() {});
	            	return false;
				}else{
					return true;
				} 
			}
			
			function daysOnBlur() {
			   this.select();//这个取到的是旧值，不是修改后的值
			}

			//新版附件上传
			function uploadFile() {
				var bigupload = $("#bigupload_1").bigfileUpload({
					useType: 'edit',////附件上传的使用类型，edit,view，（默认edit）;$(f
					chunkSize : 5,//切片的大小（默认5M）
					fileNumLimit : 10,//最大上传的文件数量（默认9）
					maxSingleFileSize:100,//单个文件最大值（默认300）,单位M
					fileExt : '.doc,.docx,.ppt,.pptx,.txt,.xls,.xlsx,.pdf,.jpg,.text,.png,',//支持上传的文件后缀名(默认开放：.bmp,.pdf,.jpg,.text,.png,.gif,.doc,.xls,.docx,.xlsx,.ppt,.pptx,.mp3,.wav,.MIDI,.m4a,.WMA,.wma,.mp4,)
					//initFileArr : attarr_1,////初始化的附件对象数组(默认为{})
					attachmentData:{bizId:'${disputeMediation.mediationId}',attachmentType:'dispute_attachment_type'},
					appcode:"testAPP",//文件所属的应用代码（默认值components）
					module:"testModule",//文件所属的模块代码（默认值bigfile）
					imgDomain : imgDomain,//图片服务器域名
					uiDomain: uiDomain,
					componentsDomain : componentsDomain,//图片服务器域名
					//serverUrl : 'http://zzh.v6.aishequ.org:9611/partFile/uploadPartFile.jhtml',
					isSaveDB : false,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
					isUseLabel: false,//是否开启附件便签功能
					isDelDbData : false,
					labelDict : [
						{'name':'处理前','value':'1'},
						{'name':'处理中','value':'2'},
						{'name':'处理后','value':'3'}],
					isUseSetText: false,//是否开启附件设置回调功能
					setCallback:function(obj){
						console.log(obj);
						$("."+obj.attr("span-class")).html("设为主图");
						obj.find("."+obj.attr("span-class")).html("主图");
					}
				});
			}

		</script>
	</body>
</html>
