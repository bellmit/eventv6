<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新疆纪委扶贫问题详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	

<style type="text/css">
	.adviceSpan{text-align: left; color: #000; padding-left: 10px;}
	.cursorPoint{cursor: pointer}
	.measureUnit{margin-left: -10px; float: right; padding: 0; text-align: left;}
	#problemForm .LabName{float: none;}
	.wideLabName{width: 110px;}
	.ListShow table code{color: #36c;}
	.NorForm td{padding-left: 0;}
</style>
	
<#include "/component/ImageView.ftl" />
<#include "/component/ComboBox.ftl" />

</head>

<body onload="checkMetterHeight();">
    <div class="MetterList" style="margin:0 auto;">
    	<div id="content-d" class="MC_con content light" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得案件简介能随着滚动条移动，隐藏横向滚动条-->
    		<form id="problemForm" action="" method="post" enctype="multipart/form-data">
    			<input type="hidden" id="probId" name="probId" value="<#if probMap.probId??>${probMap.probId?c}</#if>" />
				
				<input type="hidden" id="isStart" name="isStart" value="" />
				<input type="hidden" id="isClose" name="isClose" value="" />
				<!--是否保存处置信息-->
				<input type="hidden" id="isSaveHandleResult" name="isSaveHandleResult" value="false" />
				
	        	<div class="MetterContent" style="margin:0 auto;">
		            <div class="title ListShow" style="background:none; padding-right: 0;">
		            	<div id="contentDiv" class="fl" style="width:610px; height: 310px; position: relative;">
		            		
		            		<div id="MetterBrief" style="border-bottom:1px dotted #cecece;">
		                    	<div id="dubanIconDiv" class="dubanIcon hide"></div>
			                	<ul>
			                    	<li style="word-break: break-all; width:97%; *width:94%;">
			                    		<p><span>${probMap.probTitle!}</span></p>
			                            <p>
			                            	<span>${probMap.rptPersonName!}<#if probMap.rptPersonTel??>(${probMap.rptPersonTel})</#if></span> 于 <span>${probMap.createdStr!}</span> 
			                            	上报问题。问题内容如下： <span>${probMap.caseBrief!}</span>。
			                            </p>
			                    	</li>
			                    </ul>
		                    </div>
		                    
	                    	<div id="MetterMore" class="ListShow ListShow2" style="word-break: break-all; border: none;">
		                    	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
				                	<tr>
				                		<td width="115px;" align="right"><label class="LabName"><span>问题编号：</span></label></td>
				                		<td><code>${probMap.probNo!}</code></td>
	                					<td align="right" ><label class="LabName"><span>涉案金额：</span></label></td>
	            						<td>
	            							<code>
		            							<#if probMap.amountInvolved??>${probMap.amountInvolved?c}(万元)<#else>&nbsp;</#if>
			                        		</code>
				                		</td>
	            						<td align="right" ><label class="LabName wideLabName"><span>违纪违规时间：</span></label></td>
	            						<td>
	            							<code>${probMap.violationDateStr!'&nbsp;'}</code>
				                		</td>
				                	</tr>
				                	<tr>
				                		<td align="right" ><label class="LabName"><span>对象类别：</span></label></td>
				                		<td><code>${probMap.violationObjTypeName!'&nbsp;'}</code></td>
				                		<td align="right" ><label class="LabName"><span>违纪违规金额：</span></label></td>
	            						<td>
	            							<code>
		            							<#if probMap.violationMoney??>${probMap.violationMoney?c}(万元)<#else>&nbsp;</#if>
			                        		</code>
				                		</td>
				                		<td align="right"><label class="LabName"><span>处置时限：</span></label></td>
				                		<td><code>${probMap.procDeadlineStr!'&nbsp;'}</code></td>
				                	</tr>
				                	<#if probMap.violationObjType?? && probMap.violationObjType == '2'>
				                	<tr>
				                		<td align="right"><label class="LabName"><span>对象姓名：</span></label></td>
				                		<td><code>${probMap.objName!'&nbsp;'}</code></td>
				                		<td align="right"><label class="LabName"><span>对象性别：</span></label></td>
				                		<td><code>${probMap.objSexName!'&nbsp;'}</code></td>
				                		<td align="right"><label class="LabName"><span>政治面貌：</span></label></td>
				                		<td><code>${probMap.objPoliticsName!'&nbsp;'}</code></td>
				                	</tr>
				                	<tr>
				                		<td align="right"><label class="LabName"><span>单位：</span></label></td>
				                		<td><code>${probMap.objWorkUnit!'&nbsp;'}</code></td>
				                		<td align="right"><label class="LabName"><span>职务：</span></label></td>
				                		<td><code>${probMap.objProfession!'&nbsp;'}</code></td>
				                		<td align="right"><label class="LabName"><span>违纪人员职级：</span></label></td>
				                		<td><code>${probMap.objProfessionTypeNamePerson!'&nbsp;'}</code></td>
				                	</tr>
				                	<#elseif probMap.violationObjType?? && probMap.violationObjType == '1'>
				                	<tr>
				                		<td align="right"><label class="LabName"><span>单位名称：</span></label></td>
				                		<td><code>${probMap.objProfession!'&nbsp;'}</code></td>
				                		<td align="right"><label class="LabName"><span>单位级别：</span></label></td>
				                		<td colspan="3"><code>${probMap.objProfessionTypeNameUnit!'&nbsp;'}</code></td>
				                	</tr>
				                	</#if>
				                	<tr>
				                		<td align="right" ><label class="LabName wideLabName"><span>违纪违规资金类别：</span></label></td>
	                					<td colspan="5"><code>${probMap.violationMoneyTypeName!'&nbsp;'}</code></td>
				                	</tr>
				                	<tr>
	                					<td align="right" ><label class="LabName wideLabName"><span>违规违纪违法类别：</span></label></td>
		            					<td colspan="5"><code>${probMap.violationTypeName!'&nbsp;'}</code></td>
				                	</tr>
				                	<tr>
	                					<td align="right" ><label class="LabName"><span>问题线索来源：</span></label></td>
	            						<td colspan="5"><code>${probMap.sourceName!'&nbsp;'}</code></td>
				                	</tr>
				                	<tr>
	                					<td align="right" ><label class="LabName"><span>问题上报部门：</span></label></td>
	            						<td colspan="5"><code>${probMap.rptUnitName!'&nbsp;'}</code></td>
				                	</tr>
				                	<tr class="DotLine">
				                		<td align="right" ><label class="LabName"><span>所属行政辖区：</span></label></td>
	        							<td colspan="5">
	        								<code>${probMap.regionPath!'&nbsp;'}</code>
				                		</td>
				                	</tr>
				                	
				                	<#if curTaskName??>
					                	<tr>
					                		<td align="right" ><label class="LabName"><span>当前环节：</span></label></td>
					                		<td colspan="5">
	            								<code>${curTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code>
					                		</td>
					                	</tr>
				                	</#if>
				                </table>
			                </div>
			                
		                </div>
		                
		                <div id="slider" class="fr" style="width:300px; height:180px; border-left:1px solid #cecece;">
	                		<ul></ul>
		                </div>
		            	<div class="clear"></div>
		            </div>
					
					<#if instanceId??>
					<div id="handleResultDetailDiv" class="ListShow ListShow2" style="word-break: break-all;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px; padding-bottom: 5px;">
							<tr>
								<td width="125px;" align="right" ><label class="LabName"><span>问题处置状态：</span></label></td>
								<td><code>${probMap.procStatusName!'&nbsp;'}</code></td>
								<td align="right"><label class="LabName"><span>是否问责：</span></label></td>
								<td colspan="3"><code>${probMap.blameFlagName!}</code></td>
							</tr>
							<tr>
								<td align="right"><label class="LabName"><span>四种形态：</span></label></td>
								<td><code>${probMap.shapeName!'&nbsp;'}</code></td>
								<td align="right"><label class="LabName"><span>追缴资金：</span></label></td>
								<td><code><#if probMap.amountOfRecovery??>${probMap.amountOfRecovery?c}(万元)<#else>&nbsp;</#if></code></td>
							</tr>
							<#if probMap.violationObjType??>
								<#if probMap.violationObjType == '2'>
									<tr>
										<td align="right"><label class="LabName"><span>是否移送司法：</span></label></td>
										<td colspan="5"><code>${probMap.transferJusticeFlagName!}</code></td>
									</tr>
									<tr>
										<td align="right" ><label class="LabName"><span>组织处理类型：</span></label></td>
										<td><code>${probMap.orgProcTypeName!'&nbsp;'}</code></td>
										<td align="right" ><label class="LabName"><span>组织处理时间：</span></label></td>
										<td><code>${probMap.orgProcDateStr!'&nbsp;'}</code></td>
										<td align="right" ><label class="LabName wideLabName"><span>组织处理决定机关：</span></label></td>
										<td><code>${probMap.orgProcOrgan!'&nbsp;'}</code></td>
									</tr>
									<tr>
										<td align="right"><label class="LabName"><span>党纪处分类型：</span></label></td>
										<td><code>${probMap.partyFlagName!'&nbsp;'}</code></td>
										<td align="right"><label class="LabName"><span>党纪处分时间：</span></label></td>
										<td><code>${probMap.partyPunishmentDateStr!'&nbsp;'}</code></td>
										<td align="right"><label class="LabName wideLabName"><span>党纪处分决定机关：</span></label></td>
										<td><code>${probMap.partyPunishmentOrgan!'&nbsp;'}</code></td>
									</tr>
									<tr>
										<td align="right"><label class="LabName"><span>政纪处分类型：</span></label></td>
										<td><code>${probMap.disciplineFlagName!'&nbsp;'}</code></td>
										<td align="right"><label class="LabName"><span>政纪处分时间：</span></label></td>
										<td><code>${probMap.disciplinePunishmentDateStr!'&nbsp;'}</code></td>
										<td align="right"><label class="LabName wideLabName"><span>政纪处分决定机关：</span></label></td>
										<td><code>${probMap.disciplinePunishmentOrgan!'&nbsp;'}</code></td>
									</tr>
								<#elseif probMap.violationObjType == '1'>
									<tr>
										<td align="right"><label class="LabName"><span>纪律处分：</span></label></td>
										<td><code>${probMap.disciplinaryPunishmentName!'&nbsp;'}</code></td>
										<td align="right"><label class="LabName"><span>问责处理：</span></label></td>
										<td colspan="3"><code>${probMap.accountabilityDispositionName!'&nbsp;'}</code></td>
									</tr>
								</#if>
							</#if>
							<tr>
								<td align="right" ><label class="LabName"><span>处置结果简述：</span></label></td>
								<td colspan="5"><code>${probMap.procResult!}</code></td>
							</tr>
						</table>
					</div>
					</#if>
					<#if (isCurHandler?? && isCurHandler) || !(instanceId??)>
					<div id="handleResultDiv" class="ListShow hide" style="padding: 0;">
						<div class="NorForm DetailEdit">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td align="right"><div style="width: 125px;"><label class="LabName"><span>问题处置状态：</span></label></div></td>
				                	<td>
				                		<input type="hidden" id="procStatus" name="procStatus" value="" />
				                		<input type="text" id="procStatusName" class="inp1 InpDisable easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom'" style="width: 135px;" />
				                	</td>
									<td align="right"><label class="LabName"><span>是否问责：</span></label></td>
									<td colspan="3">
										<span class="Check_Radio"><input class="cursorPoint" type="radio" id="blameFlag_1" name="blameFlag" value="1" /><label for="blameFlag_1" class="cursorPoint" >是&nbsp;</label></span>
										<span class="Check_Radio"><input class="cursorPoint" type="radio" id="blameFlag_0" name="blameFlag" value="0"/><label for="blameFlag_0" class="cursorPoint" >否&nbsp;</label></span>
									</td>
								</tr>
								<tr>
									<td align="right"><label class="LabName"><span>追缴资金：</span></label></td>
									<td>
										<input type="text" id="amountOfRecovery" name="amountOfRecovery" class="inp1 easyui-validatebox" data-options="validType:'floatNumStr[12,2]',tipPosition:'bottom'" style="height: 30px; width: 138px;" value="<#if probMap.amountOfRecovery??>${probMap.amountOfRecovery?c}</#if>" />
										<label class="LabName measureUnit" style="float: none; display: inline-block; margin-left: 0;">(万元)</label>
									</td>
									<td align="right" ><label class="LabName"><span>四种形态：</span></label></td>
									<td colspan="3">
										<input id="shape" name="shape" type="hidden" value="" />
										<input id="shapeName" type="text" class="inp1 InpDisable easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom'" style="width: 135px;" />
									</td>
								</tr>
								<#if probMap.violationObjType??>
									<#if probMap.violationObjType == '2'>
									<tr>
										<td align="right"><label class="LabName"><span>是否移送司法：</span></label></td>
										<td colspan="5">
											<span class="Check_Radio"><input class="cursorPoint" type="radio" id="transferJusticeFlag_1" name="transferJusticeFlag" value="1" /><label for="transferJusticeFlag_1" class="cursorPoint" >是&nbsp;</label></span>
											<span class="Check_Radio"><input class="cursorPoint"  type="radio" id="transferJusticeFlag_0" name="transferJusticeFlag" value="0"/><label for="transferJusticeFlag_0" class="cursorPoint" >否&nbsp;</label></span>
										</td>
									</tr>
									<tr>
										<td align="right"><label class="LabName"><span>党纪处分类型：</span></label></td>
										<td>
											<input id="partyFlag" name="partyFlag" type="hidden" value="" />
											<input id="partyFlagName" type="text" class="inp1 InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" style="width: 135px;" />
										</td>
										<td align="right"><label class="LabName"><span>党纪处分时间：</span></label></td>
										<td>
											<input type="text" id="partyPunishmentDateStr" name="partyPunishmentDateStr" class="inp1 Wdate easyui-validatebox" style="width:135px; cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
										</td>
										<td align="right"><label class="LabName wideLabName"><span>党纪处分决定机关：</span></label></td>
										<td>
											<input type="text" id="partyPunishmentOrgan" name="partyPunishmentOrgan" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" style="width: 93%;" value="" />
										</td>
									</tr>
									<tr>
										<td align="right"><label class="LabName"><span>政纪处分类型：</span></label></td>
										<td>
											<input id="disciplineFlag" name="disciplineFlag" type="hidden" value="" />
											<input id="disciplineFlagName" type="text" class="inp1 InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" style="width: 135px;" />
										</td>
										<td align="right"><label class="LabName"><span>政纪处分时间：</span></label></td>
										<td>
											<input type="text" id="disciplinePunishmentDateStr" name="disciplinePunishmentDateStr" class="inp1 Wdate easyui-validatebox" style="width:135px; cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
										</td>
										<td align="right"><label class="LabName wideLabName"><span>政纪处分决定机关：</span></label></td>
										<td>
											<input type="text" id="disciplinePunishmentOrgan" name="disciplinePunishmentOrgan" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" style="width: 93%;" value="" />
										</td>
									</tr>
									<tr>
										<td align="right" ><label class="LabName"><span>组织处理类型：</span></label></td>
										<td>
											<input id="orgProcType" name="orgProcType" type="hidden" value="" />
											<input id="orgProcTypeName" type="text" class="inp1 InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" style="width: 135px;" />
										</td>
										<td align="right"><label class="LabName"><span>组织处理时间：</span></label></td>
										<td>
											<input type="text" id="orgProcDateStr" name="orgProcDateStr" class="inp1 Wdate easyui-validatebox" style="width:135px; cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
										</td>
										<td align="right"><label class="LabName wideLabName"><span>组织处理决定机关：</span></label></td>
										<td>
											<input type="text" id="orgProcOrgan" name="orgProcOrgan" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" style="width: 93%;" value="" />
										</td>
									</tr>
									<#elseif probMap.violationObjType == '1'>
									<tr>
										<td align="right"><label class="LabName"><span>纪律处分：</span></label></td>
										<td>
											<input id="disciplinaryPunishment" name="disciplinaryPunishment" type="hidden" value="" />
											<input id="disciplinaryPunishmentName" type="text" class="inp1 InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" style="width: 135px;" />
										</td>
										<td align="right"><label class="LabName"><span>问责处理：</span></label></td>
										<td colspan="3">
											<input id="accountabilityDisposition" name="accountabilityDisposition" type="hidden" value="" />
											<input id="accountabilityDispositionName" type="text" class="inp1 InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" style="width: 135px;" />
										</td>
									</tr>
									</#if>
								</#if>
								<tr>
									<td align="right" ><label class="LabName"><span>处置结果简述：</span></label></td>
									<td colspan="5">
										<textarea rows="3" style="height:160px;" id="procResult" name="procResult" class="area1 easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom',validType:['maxLength[4000]','characterCheck']">${probMap.procResult!}</textarea>
									</td>
								</tr>
								<#if !(instanceId??) && (listType?? && listType==1)>
								<tr>
									<td align="right" ><label class="LabName"><span>办理意见：</span></label></td>
									<td class="LeftTd" colspan="5">
										<textarea rows="3" style="height:60px;" id="closeAdvice" name="advice" class="area1 easyui-validatebox fast-reply easyuiRequired" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
									</td>
								</tr>
								</#if>
							</table>
						</div>
					</div>
					</#if>
		                   	
					<div id="fileUploadDiv" class="ListShow" style="padding: 0;">
						<div class="NorForm DetailEdit">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="125px;" align="right"><label class="LabName"><span>图片上传：</span></label></td>
									<td><div class="ImgUpLoad" id="fileupload"></div></td>
								</tr>
							</table>
						</div>
					</div>
					
	            </div>
            </form>
            
            <#if isCurHandler?? && isCurHandler>
				<div class="h_20"></div>
				<div style="padding: 0 20px 0 20px;">
					<#include "/zzgl/event/workflow/handle_node_base.ftl" />
				</div>
			</#if>
			
			<div id="workflowDetailDiv" class="hide" style="padding: 0 20px 0 20px;">
				<div class="h_20"></div>
				<div class="ConList">
					<div class="nav" id="tab">
						<ul>
							<li id="01_li" class="current">处理环节</li>
						</ul>
					</div>
					<div class="ListShow ListShow2">
						<div id="01_li_div" class="t_a_b_s">
							<div id="workflowDetail" border="false"></div>
						</div>
					</div>
				</div>
			</div>
			
        </div>
    </div>
	                
	<#if instanceId??>
	<#elseif listType?? && listType==1>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="showAdvice(false);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
        		<a href="###" onclick="showAdvice(true);" class="BigNorToolBtn BigJieAnBtn">结案</a>
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    <#else>
    	<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    </#if>

<script type="text/javascript">
	var _winHeight = 0,
		_winWidth = 0,
		downPath = "${IMG_URL!}",//图片幻灯片展示使用
		contextPath = "${rc.getContextPath()}",
		basWorkSubTaskCallback = null,
		partyDisciplineFlagBox = null,
		orgProcTypeBox = null,
		shapeBox = null,
		procStatusBox = null;
	
	$(function() {
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		var probId = $("#probId").val(),
			width = _winWidth - 390,//390为：为图片展示预留的宽度；
			options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        },
	        swfOpt = {
	        	positionId:'fileupload',//附件列表DIV的id值',
	        	type:'detail',//add edit detail
	        	initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
	        	imgDomain:'${IMG_URL!}',//图片域名 type为add或者edit时，生效
	        	context_path:'${SQ_FILE_URL}',
	        	ajaxData: {'bizId': probId,'attachmentType':'${attachmentType!}','eventSeq':'1,2'},
	        	showPattern: 'list'
	        };
        
        $("#contentDiv").width(width);
        
	    enableScrollBar('MetterBrief',options);
        enableScrollBar('MetterMore',options);
        
        getImages(probId, '${attachmentType!}', {
        	"1": "问题采集",
        	"2": "问题处置"
        });//获取右侧幻灯片
        
        showWorkflowDetail();
        
        <#if !(instanceId??) || (isCurHandler?? && isCurHandler)>
        	$("#blameFlag_${probMap.blameFlag!'0'}").attr("checked", true);
        	$("#transferJusticeFlag_${probMap.transferJusticeFlag!'0'}").attr("checked", true);
        	
        	<#if isCurHandler?? && isCurHandler>
        	var baseWorkOption = BaseWorkflowNodeHandle.initParam();//获取默认的设置
        	
        	basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
        	
        	BaseWorkflowNodeHandle.initParam({
        		subTask: {
        			subTaskUrl: '${rc.getContextPath()}/zhsq/accountabilityEnforcement/subWorkflow4AccountEn.jhtml',
        			subTaskCallback: probSubTask
        		},
        		reject: {
        			rejectUrl: '${rc.getContextPath()}/zhsq/accountabilityEnforcement/rejectWorkflow4AccountEn.jhtml'
        		},
        		evaluate: {
        			isShowEva: false
        		},
        		checkRadio: {
        			radioCheckCallback: radioCheckCallback
        		}
        	});
        	
        	$.extend(swfOpt, {
        		type			: 'edit',
        		file_types		: '*.jpg;*.gif;*.png;*.jpeg;*.amr;*.mp3;*.mp4;*.doc;*.docx;*.xls;',
        		radio_list		: [{'name':'问题处置', 'value':'2'}],
        		radio_list_all	: [{'name':'问题采集', 'value':'1'},{'name':'问题处置', 'value':'2'}],
        		appCode			: 'account_enforcement',
        		initAttrParam	: {'attachmentType':'${attachmentType!}', 'bizId': probId},
        		showPattern		: 'all'
        	});
        	</#if>
        	
        	procStatusBox = AnoleApi.initListComboBox("procStatusName", "procStatus", "B590009", null,["${probMap.procStatus!}"]);
        	
        	shapeBox = AnoleApi.initTreeComboBox("shapeName", "shape", "B590014", null, ["${probMap.shape!}"]);
        	
        	<#if probMap.violationObjType??>
        		<#if probMap.violationObjType == '1'>
        			AnoleApi.initTreeComboBox("disciplinaryPunishmentName", "disciplinaryPunishment", "B590015", null, ["${probMap.disciplinaryPunishment!}"]);
        			AnoleApi.initTreeComboBox("accountabilityDispositionName", "accountabilityDisposition", "B590016", null, ["${probMap.accountabilityDisposition!}"]);
        		<#elseif probMap.violationObjType == '2'>
        			partyDisciplineFlagBox = AnoleApi.initTreeComboBox("partyFlagName", "partyFlag", "B590012001", null, ["${probMap.partyDisciplineFlag!}"]);
        			orgProcTypeBox = AnoleApi.initTreeComboBox("orgProcTypeName", "orgProcType", "B590013", null, ["${probMap.orgProcType!}"]);
        			AnoleApi.initTreeComboBox("disciplineFlagName", "disciplineFlag", "B590012002", null, ["${probMap.disciplineFlag!}"]);
        		</#if>
        	</#if>
        </#if>
        
		fileUpload(swfOpt);
		
        var lis = $("#tab").find("li");
		lis.each(function() {
			$(this).bind("click", function() {
				lis.each(function() {
					$(this).removeClass("current");
				});
				$(this).addClass("current");
				var li_id = $(this).attr("id");
				$(".t_a_b_s").each(function() {
					var obj = $(this);
					if (obj.attr("id") == li_id + "_div") {
						$(this).removeClass("hide");
					} else {
						$(this).addClass("hide");
					}
				});
			});
		});
		
		$("#procResult").width($(window).width() * 0.79);
		$("#closeAdvice").width($(window).width() * 0.79);
	});
	
	function handleResultInit() {//数据还原
		$("#blameFlag_${probMap.blameFlag!'0'}").attr("checked", true);
		$("#transferJusticeFlag_${probMap.transferJusticeFlag!'0'}").attr("checked", true);
		$("#amountOfRecovery").val("<#if probMap.amountOfRecovery??>${probMap.amountOfRecovery?c}</#if>");
		$("#procResult").val("${probMap.procResult!}");
		
		partyDisciplineFlagBox.setSelectedNodes(["${probMap.partyDisciplineFlag}"]);
		orgProcTypeBox.setSelectedNodes(["${probMap.orgProcType!}"]);
		shapeBox.setSelectedNodes(["${probMap.shape!}"]);
	}
	
	function probSubTask() {
		if($("#handleResultDiv").is(":visible")) {
			var isValid = $('#problemForm').form('validate');
			
			if(isValid) {
				$("#problemForm").attr("action", '${rc.getContextPath()}/zhsq/accountabilityEnforcement/saveProb.jhtml');
				
				$("#problemForm").ajaxSubmit(function(data) {
				    var result = data.result,
				    	msg = data.tipMsg;
				    
				    if(result && result == true) {
				    	if(basWorkSubTaskCallback && typeof basWorkSubTaskCallback === 'function') {
				    		basWorkSubTaskCallback();
				    	} else {
					    	msg = msg || '操作成功！';
					    	parent.reloadDataForSubPage(msg, true);
				    	}
				    } else {
				    	msg = msg || '操作失败！';
				    	$.messager.alert('错误', msg, 'error');
				    }
				});
			}
		} else if(basWorkSubTaskCallback && typeof basWorkSubTaskCallback === 'function') {
			basWorkSubTaskCallback();
		}
	}
	
	function radioCheckCallback(option) {
		var curNodeName = "${curNodeName!}",
			nextNodeName = option.nodeName,
			leaderNodeName = "task11",
			endNodeName = "end1",
			isRequired = true;
		
		if(nextNodeName == leaderNodeName || (curNodeName != leaderNodeName && nextNodeName == endNodeName)) {
			$("#handleResultDetailDiv").hide();
			$("#handleResultDiv").show();
		} else {
			isRequired = false;
			$("#handleResultDiv").hide();
			$("#handleResultDetailDiv").show();
			
			handleResultInit();
		}
		
		$("#isSaveHandleResult").val(isRequired);
		
		$('#handleResultDiv .easyuiRequired').each(function() {
			$(this).validatebox({
				required: isRequired
			});
		});
	}
	
	function showWorkflowDetail() {
		var instanceId = "<#if instanceId??>${instanceId?c}</#if>";
		if(instanceId) {
			$("#workflowDetailDiv").show();
			
			$("#workflowDetail").panel({
				height:'auto',
				width:'auto',
				overflow:'no',
				href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=" + instanceId,
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
				}
			});
		}
	}
		
	function checkMetterHeight() {//需要在页面渲染完成后，执行，因为"MetterMore > table"的高度会因为折行效果发生变化
		var moreTableDefault = 210;
		var moreTableHeight = $("#MetterMore table").height();//由于添加的滚动条，因此table不再是MetterMore的直接下级
		var briefContentHeight = $("#MetterBrief ul > li").height();
		var briefHeight = 0;
		var dubanIcon = $(".dubanIcon").length;
		
		if(moreTableHeight > moreTableDefault) {
			moreTableHeight = moreTableDefault;
		}
		
		briefHeight = $("#contentDiv").height() - moreTableHeight - 1 - 1;//1为底部界限边距，共两条
		
		if(briefContentHeight < briefHeight && dubanIcon > 0) {//防止内容不足时，导致督办图标显示不完全
			$("#MetterBrief ul > li").height(briefHeight);
		}
		
		$("#MetterMore").height(moreTableHeight);
		$("#MetterBrief").height(briefHeight);
		
		//显示设置width样式，为了防止菜单页签切换时，宽度被修改
		$("#MetterBrief ul").width($("#MetterBrief").width());
		$("#MetterMore table").width($("#MetterMore").width());
	}
	
	function showAdvice(isClose) {
		$("#isStart").val(true);
		$("#isClose").val(isClose);
		$("#isSaveHandleResult").val(isClose);
		
		if(isClose && isClose == true) {
			radioCheckCallback({
				'nodeName' : 'end1'
			});
		}
		
		if($("#problemForm").form('validate')) {
			startWorkflow(isClose);
		}
	}
	
	function showAdvice_(isClose) {
		$("#isStart").val(true);
		$("#isClose").val(isClose);
		$("#isSaveHandleResult").val(isClose);
		
		if(isClose && isClose == true) {
			$('#closeAdvice').validatebox({
				required: true
			});
			
			$("#closeAdviceDiv").show();
		} else {
			$("#closeAdviceDiv").hide();
			
			$('#closeAdvice').validatebox({
				required: false
			});
			
			$('#closeAdvice').val("");
		}
		
		if($("#problemForm").form('validate')) {
			startWorkflow(isClose);
		}
	}
	
	function startWorkflow(isClose) {
		var probId = $("#probId").val();
		
		if(probId) {
			$("#problemForm").attr("action", "${rc.getContextPath()}/zhsq/accountabilityEnforcement/startWorkflow4AccountEn.jhtml");
	      	
	      	modleopen();
		  	$("#problemForm").ajaxSubmit(function(data) {
		  		modleclose();
		  	
		  		if(data.success && data.success == true) {
		  			if(isClose) {
		  				parent.reloadDataForSubPage(data.tipMsg, true);
		  			} else {
		  				parent.searchData();
		  				parent.detail(probId, "2");
		  				if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
		  					parent.closeBeforeMaxJqueryWindow();
		  				}
		  			}
		  		} else {
		  			if(data.tipMsg) {
		  				$.messager.alert('错误', data.tipMsg, 'error');
		  			} else {
		  				$.messager.alert('错误', '操作失败！', 'error');
		  			}
		  		}
		  	});
		}
	}
		
	function closeDetailWin() {
		parent.closeMaxJqueryWindow();
	}
	
	function flashData(msg) {//工作办理回调
		parent.reloadDataForSubPage(msg, true);
	}
	
	function showMix(fieldId, index) {//幻灯片点击事件
		ffcs_viewImg_win(fieldId, index);
	}
	
	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
</script>
</body>
</html>
