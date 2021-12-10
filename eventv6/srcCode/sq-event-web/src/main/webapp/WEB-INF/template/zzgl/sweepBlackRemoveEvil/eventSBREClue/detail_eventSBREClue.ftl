<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>扫黑除恶-线索管理 新增/编辑</title>
		<!-- 扫黑除恶样式 -->
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<#include "/component/commonFiles-1.1.ftl" />
		
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		
		<!-- 扫黑除恶js -->
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/jquery.nicescroll.js" charset="utf-8"></script>
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/main-shce.js" charset="utf-8"></script>
		
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	
		
		<!--    add by wuzhj 新版附件上传 样式 start     -->
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
		
		<!--    add by wuzhj 新版附件上传 样式 end     -->
		
		
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">线索详情</a></li>
							<#if instanceId??>
							<li><a href="##" divId="taskDetailDiv">处理环节</a></li>
							</#if>
						</ul>
					</div>
				</div>
				
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent">
					<!-- 举报人信息 -->
					<div class="fw-det-tog fw-main-det">
						<div class="fw-det-tog-top">
							<h5><i></i>举报人信息</h5>
							<a href="###"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/></a>
						</div>
						<div class="fw-det-toggle">
							<ul class="fw-xw-from clearfix">
								<li class="xw-com1">
									<span class="fw-from1">实名举报：</span>
									<p class="from flex1">${clue.isRealNameName!}</p>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">举报人姓名：</span>
									<p class="from flex1"><#if informantInfo??>${informantInfo.name!}</#if></p>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">举报人联系方式：</span>
									<p class="from flex1"><#if informantInfo??>${informantInfo.tel!}</#if></p>
								</li>
							</ul>
						</div>
					</div>
					
					<!-- 被举报人信息 -->
					<div id="reportedInfoDiv" class="fw-det-tog fw-main-det">
						<div class="fw-det-tog-top">
							<h5><i></i>被举报人信息</h5>
							<a href="###"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/></a>
						</div>
						<div class="fw-det-toggle">
							<div class="repor-head">
								<ul id="reportedPersonUl" class="headlist flex1 clearfix" style="margin-left: 30px;">
									<li id="defaultReportedPersonLi" style="display: none;">
										<a class="hd-box"></a>
									</li>
								</ul>
							</div>
							
							<div id="defaultReportedContentDiv" class="report-pep xw-com1 mb20 hide" index="0">
								<input type="text" id="bizType" class="hide" value="10" />
								<ul class="fw-xw-from clearfix ">
									<li class="xw-com1">
										<span class="fw-from1">姓名：</span>
										<p id="name" class="from flex1"></p>
									</li>
									<li class="xw-com3">
										<span class="fw-from1">年龄：</span>
										<p id="age" class="from flex1"></p>
									</li>
									<li class="xw-com3">
										<span class="fw-from1">职业：</span>
										<p id="profession" class="from flex1"></p>
									</li>
									<li class="xw-com3">
										<span class="fw-from1">身份证号：</span>
										<p id="idCard" class="from flex1"></p>
									</li>
									<li class="xw-com1">
										<span class="fw-from1">家庭住址：</span>
										<p id="homeAddr" class="from flex1"></p>
									</li>
								</ul>
							</div>
							
						</div>
					</div>
					
					<!-- 线索基础信息 -->
					<div class="fw-det-tog fw-main-det">
						<div class="fw-det-tog-top">
							<h5><i></i>线索基础信息</h5>
							<a href="###"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/></a>
						</div>
						<div class="fw-det-toggle">
							<ul class="fw-xw-from clearfix">
								<li class="xw-com1">
									<span class="fw-from1">线索标题：</span>
									<p class="from flex1">${clue.clueTitle!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">发生区域：</span>
									<p class="from flex1">${clue.gridPath!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">线索来源：</span>
									<p class="from flex1"><span class="font-blue">${clue.clueSourceName!}</span></p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">重要程度：</span>
									<p class="from flex1"><span class="font-red">${clue.importantDegreeName!}</span></p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">是否加密线索：</span>
									<p class="from flex1">${clue.isEncryptName!'否'}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">举报时间：</span>
									<p class="from flex1">${clue.reportDateStr!}</p>
								</li>
								<#if clue.disposalMethod??>
								<li class="xw-com3">
									<span class="fw-from1">处置方式：</span>
									<p class="from flex1">${clue.disposalMethodName!}</p>
								</li>
								</#if>
							</ul>
							<ul id="disposalMethod_01" class="fw-xw-from clearfix hide">
								<li class="xw-com3">
									<span class="fw-from1">立案时间：</span>
									<p class="from flex1">${clue.registerDateStr!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">立案单位：</span>
									<p class="from flex1"><#if disposeUnitInfo??>${disposeUnitInfo.name!}</#if></p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">办案人：</span>
									<p class="from flex1">${clue.registerName!}</p>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">涉案罪名：</span>
									<p class="from flex1">${clue.suspectedChargesName!}</p> 
								</li>
							</ul>
							<ul id="disposalMethod_02" class="fw-xw-from clearfix hide">
								<li class="xw-com3">
									<span class="fw-from1">处罚时间：</span>
									<p class="from flex1">${clue.punishDateStr!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">处罚单位：</span>
									<p class="from flex1"><#if disposeUnitInfo??>${disposeUnitInfo.name!}</#if></p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">办案人：</span>
									<p class="from flex1">${clue.punishmenter!}</p>
								</li>
							</ul>
							<ul id="disposalMethod_03" class="fw-xw-from clearfix hide">
								<li class="xw-com3">
									<span class="fw-from1">移交时间：</span>
									<p class="from flex1">${clue.transferDateStr!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">移交单位：</span>
									<p class="from flex1"><#if disposeUnitInfo??>${disposeUnitInfo.name!}</#if></p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">办理人：</span>
									<p class="from flex1">${clue.transferName!}</p>
								</li>
							</ul>
							<ul id="disposalMethod_99" class="fw-xw-from clearfix hide">
								<li class="xw-com1">
									<span class="fw-from1">处理情况：</span>
									<p class="from flex1">${clue.disposalSituation!}</p>
								</li>
							</ul>
							<ul id="disposalMethod_" class="fw-xw-from clearfix hide">
								<li class="xw-com3">
									<span class="fw-from1">反馈人：</span>
									<p class="from flex1">${clue.feedbackName!}</p>
								</li>
								<li class="xw-com3">
									<span class="fw-from1">反馈时间：</span>
									<p class="from flex1">${clue.feedbackDateStr!}</p>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">举报人意见：</span>
									<p class="from flex1">${clue.informantAdvice!}</p>
								</li>
							</ul>
							<ul class="fw-xw-from clearfix">
                                <li class="xw-com1">
                                    <span class="fw-from1">涉及打击重点：</span>
                                    <p class="from flex1">${clue.involveAttackFocusName!}</p>
                                </li>
								<li class="xw-com1">
									<span class="fw-from1">线索简要内容：</span>
									<p class="from flex1">${clue.clueContent!}</p>
								</li>
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh"></i>涉及黑恶团伙：</span>
									<div class="darkteam flex1" type="text">
										<ul class="clearfix">
											<#if gangList?? && (gangList?size > 0)>
												<#list gangList as gang>
													<li class="darkteam-txt" title="${gang.gangName!}"><a onclick="showGangInfo(<#if gang.gangId??>${gang.gangId?c}</#if>);">${gang.gangName!}</a></li>
												</#list>
											</#if>
										</ul>
									</div>
								</li>
								<li class="xw-com1">
									<span id="href2"  class="fw-from1">线索附件：</span>
									<div class="ImgUpLoad" id="fileupload"></div>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">备注：</span>
									<p class="from flex1">${clue.clueRemark!}</p>
								</li>
							</ul>
						</div>
					</div>
					
					<form id="eventSBREClueForm" name="eventSBREClueForm" action="" method="post" enctype="multipart/form-data">
						<input type="hidden" id="clueId" name="clueId" value="<#if clue.clueId??>${clue.clueId?c}</#if>" />
						
						<!--处置反馈信息-->
						<div id="feedbackInfoDiv" class="fw-det-tog fw-det-tog-n mt20 hide">
							<div class="fw-det-tog-top">
								<h5><i style="background:#4f91ff;"></i>处置反馈信息</h5>
							</div>
							<div id="feedbackDiv" class="fw-chul">
								<ul class="fw-xw-from clearfix">
									<li class="xw-com1">
										<span class="fw-from1"><i class="spot-xh">*</i>处置方式：</span>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="disposalMethod_01" name="disposalMethod" value="01"  checked />
											<span class="radio-input"></span>立案
										</label>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="disposalMethod_02" name="disposalMethod" value="02" />
											<span class="radio-input"></span>行政处罚
										</label>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="disposalMethod_03" name="disposalMethod" value="03" />
											<span class="radio-input"></span>移交其他机关处理
										</label>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="disposalMethod_99" name="disposalMethod" value="99" />
											<span class="radio-input"></span>其他
										</label>
									</li>
								</ul>
								<ul id="disposalMethod_01_div" class="fw-xw-from clearfix">
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>立案时间：</span>
										<input type="text" name="registerDateStr" class="from flex1 bg-ico-day Wdate easyui-validatebox easyui_required" style="cursor: pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, maxDate:'${currentDate!}', dateFmt:'yyyy-MM-dd', isShowClear:false, isShowToday:false})" value="${currentDate!}" readonly="readonly" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>涉案罪名：</span>
										<input type="hidden" id="suspectedCharges" name="suspectedCharges" />
										<input type="text" id="suspectedChargesName" class="from flex1 bg-btm-arrow easyui-validatebox easyui_required" data-options="tipPosition:'bottom'" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>立案单位：</span>
										<input type="text" name="registerUnitName" class="from flex1 easyui-validatebox easyui_required" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1">办案人：</span>
										<input type="text" name="registerName" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" />
									</li>
								</ul>
								<ul id="disposalMethod_02_div" class="fw-xw-from clearfix hide">
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>处罚时间：</span>
										<input type="text" name="punishDateStr" class="from flex1 bg-ico-day Wdate easyui-validatebox easyui_required" style="cursor: pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, maxDate:'${currentDate!}', dateFmt:'yyyy-MM-dd', isShowClear:false, isShowToday:false})" value="${currentDate!}" readonly="readonly" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>处罚单位：</span>
										<input type="text" name="punishUnitName" class="from flex1 easyui-validatebox easyui_required" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1">办案人：</span>
										<input type="text" name="punishmenter" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" />
									</li>
								</ul>
								<ul id="disposalMethod_03_div" class="fw-xw-from clearfix hide">
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>移交时间：</span>
										<input type="text" name="transferDateStr" class="from flex1 bg-ico-day Wdate easyui-validatebox easyui_required" style="cursor: pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, maxDate:'${currentDate!}', dateFmt:'yyyy-MM-dd', isShowClear:false, isShowToday:false})" value="${currentDate!}" readonly="readonly" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1"><i class="spot-xh">*</i>移交单位：</span>
										<input type="text" name="transferUnitName" class="from flex1 easyui-validatebox easyui_required" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1">办理人：</span>
										<input type="text" name="transferName" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" />
									</li>
								</ul>
								<ul id="disposalMethod_99_div" class="fw-xw-from clearfix hide">
									<li class="xw-com1">
										<span class="fw-from1"><i class="spot-xh">*</i>处理情况：</span>
										<input type="text" name="disposalSituation" class="from flex1 easyui-validatebox easyui_required" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
									</li>
								</ul>
								<ul id="disposalMethod_div" class="fw-xw-from clearfix" style="padding-top: 12px; border-top:1px dotted #ccc;">
									<li class="xw-com3">
										<span class="fw-from1"><i class="realNameAsterisk spot-xh hide">*</i>反馈人：</span>
										<input type="text" name="feedbackName" class="from flex1 easyui-validatebox realNameRequired" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" />
									</li>
									<li class="xw-com3">
										<span class="fw-from1"><i class="realNameAsterisk spot-xh hide">*</i>反馈时间：</span>
										<input type="text" name="feedbackDateStr" class="from flex1 bg-ico-day Wdate easyui-validatebox" style="cursor: pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, maxDate:'${currentDate!}', dateFmt:'yyyy-MM-dd', isShowClear:false})" value="${currentDate!}" readonly="readonly" />
									</li>
									<li class="xw-com1">
										<span class="fw-from1"><i class="realNameAsterisk spot-xh hide">*</i>举报人意见：</span>
										<input type="text" name="informantAdvice" class="from flex1 easyui-validatebox realNameRequired" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
									</li>
								</ul>
							</div>
						</div>
						
					</form>
					
					<!--办理信息-->
					<#if isCurHandler?? && isCurHandler>
					<div class="fw-det-tog fw-det-tog-n mt20">
						<div class="fw-det-tog-top">
							<h5><i style="background:#4f91ff;"></i>线索处置</h5>
						</div>
						<div class="fw-chul">
							<#include "/zzgl/event/workflow/handle_node_base.ftl" />
						</div>
					</div>
					</#if>
					
				</div>
				
				<#if instanceId??>
				<div id="taskDetailDiv" class="fw-main tabContent ">
					<!-- 处理环节 -->
					<div id="workflowDetail" border="false"></div>
				</div>
				<#elseif listType?? && listType=='1'>
				<!--操作按钮-->
				<div id="btnDiv" class="btn-warp">
					<a class="btn-bon blue-btn" onclick="startWorkflow();">提交</a>
				</div>
				</#if>
			
			</div>
		</div>
		
		<#include "/component/ComboBox.ftl" />
	</body>
	
	<script type="text/javascript">
		var basWorkSubTaskCallback = null;//存放原有的提交方法
		
		var base = '${rc.getContextPath()}';
		var imgDomain = '${IMG_URL}';
		var uiDomain = '${uiDomain}';
		var componentsDomain = '${SQ_COMPONENTS_URL}';//公共组件工程域名
		var clueId;
		
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0;
			clueId = $("#clueId").val();
			
			$("#fileupload").bigfileUpload({
				useType: 'view',//附件上传的使用类型，add,edit,view，（默认edit）;
				chunkSize : 5,//切片的大小（默认5M）
				attachmentData:{bizId:clueId,attachmentType:'${attachmentType!}'},
				appcode:"event_SBRE_clue",//文件所属的应用代码（默认值components）
				module:"attachment",//文件所属的模块代码（默认值bigfile）
				imgDomain : imgDomain,//图片服务器域名
				uiDomain : uiDomain,//公共样式域名
				componentsDomain : componentsDomain,//公共组件域名
				isSaveDB : true,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
				isDelDbData:false,//是否删除数据库数据(默认true)
				isUseLabel: true,//是否使用附件标签(默认false)
				labelDict : [{'name':'处理前','value':'1'},{'name':'处理中','value':'2'},{'name':'处理后','value':'3'}],
				styleType:"box",//块状样式：box,列表样式：list,自定义样式：self
			});
			
			<#if reportedInfoList?? && (reportedInfoList?size>0)>
				showReportedPeople();
			</#if>
			
			<#if isCurHandler?? && isCurHandler>
				var baseWorkOption = BaseWorkflowNodeHandle.initParam();//获取默认的设置
				
				basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/eventSBREClue/subWorkflow4Clue.jhtml',
						subTaskCallback: eventClueSubTask
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/eventSBREClue/rejectWorkflow4Clue.jhtml'
					},
					evaluate: {
						isShowEva: false
					},
					checkRadio: {
						radioCheckCallback: radioCheckCallback
					},
					transactor: {
						handlerLabelName : '牵头单位',
						isShowAssistOrg : true,
						assistOrgLabelName : '协办单位'
					},
					selectHandler: {
						isShowOrgNameFuzzyQuery : true
					},
					selectOrgInfo: {
						isShowOrgNameFuzzyQuery : true
					}
				});
				
				swfOpt["type"] = 'edit'; 
				
				$('#feedbackDiv input[type=radio][name=disposalMethod]').on('click', function() {
					$('#feedbackDiv > ul[id^=disposalMethod_]').hide();
					
					$('#' + $(this).attr('id') + '_div').show();
					$('#disposalMethod_div').show();
					
					radioCheckCallback({
						nodeName: 'end1'
					});
				});
				
				var isRealName = "${clue.isRealName!'0'}" == '1';
				if(isRealName) {
					$("#disposalMethod_div .realNameAsterisk").show();
				} else {
					$("#disposalMethod_div .realNameAsterisk").hide();
				}
				
				$("#disposalMethod_div .realNameRequired").validatebox({
					required: isRealName
				});
				
				AnoleApi.initListComboBox("suspectedChargesName", "suspectedCharges", "B591004", null, null, {
					RenderType : "01"
				});
			</#if>
			
			<#if instanceId??>
				showWorkflowDetail();
			</#if>
			
			//标题点击内容显示与隐藏
			$('.fw-det-tog-top > a').parent()
									.css('cursor', 'pointer')
									.on('click', function() {
										$(this).siblings('.fw-det-toggle').toggle(300);
									});
			//顶部页签切换相应事件
			$('.fw-tab-min').on('click', 'li a', function() {
				$('.fw-tab-min > li > a').removeClass('active');
				
				$(this).addClass('active');
				
				$('#formDiv div.tabContent').hide();
				$('#' + $(this).attr('divId')).show();
				
			});
				
			fileUpload(swfOpt);
			
			//展示对应的反馈信息
			<#if clue.disposalMethod??>
				$('#disposalMethod_${clue.disposalMethod}').show();
				$('#disposalMethod_').show();
			</#if>
			
			$(window).on('load resize', function () {
				$winH = $(window).height();
				$topH = $('#topTitleDiv').outerHeight(true);
				$btnH = $('#btnDiv').outerHeight(true);
				
				$('#mainDiv').height($winH - $topH - $btnH);
				$('#taskDetailDiv').height($winH - $topH - $btnH);
            	
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
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function addReportedInfo() {//新增被举报人员
			var cloneContentObj = $('#defaultReportedContentDiv').clone(),
				clonePersonObj = $('#defaultReportedPersonLi').clone(),
				index = parseInt($('#defaultReportedContentDiv').attr('index'), 10) + 1,
				randomIndex = Math.random().toString().substr(2),
				personId = 'reportedPersonLi_' + index + '_' + randomIndex,
				contentId = 'reportedInfoDiv_' + index + '_' + randomIndex;
			
			cloneContentObj.attr('id', contentId);
			$('#defaultReportedContentDiv').attr('index', index);
			clonePersonObj.attr({'id': personId, 'contentId': contentId, 'onclick': 'selectReportedInfo("' + contentId + '")'});
			
			$('#defaultReportedPersonLi').before(clonePersonObj);
			$('#defaultReportedContentDiv').before(cloneContentObj);
			
			return contentId;
		}
		
		function selectReportedInfo(contentId) {
			var personObj = $('#reportedPersonUl li[contentId='+ contentId +']');
			
			if(personObj.length > 0) {
				var selectedObj = $('#reportedPersonUl a.bd-on'),
					selectedContentId = selectedObj.parent().attr('contentId');
				
				selectedObj.attr('title' , $('#' + selectedContentId + ' #name').html());
				
				$('#reportedPersonUl a.hd-box').removeClass('bd-on');
				personObj.children('a.hd-box').addClass('bd-on');
				$('#reportedInfoDiv div[id^=reportedInfoDiv_]').hide();
				
				$('#reportedPersonUl li[contentId='+ contentId +']').show();
				$('#' + contentId).show();
			}
		}
		
		function showReportedPeople() {//若被举报人存在，展示被举报人信息
			<#if reportedInfoList?? && (reportedInfoList?size>0)>
				var contentId = null,
					contentId4Add = [];
				
				<#list reportedInfoList as list>
					contentId = addReportedInfo();
					if(contentId) {
						var reportedPersonUl = $('#reportedPersonUl > li[contentId=' + contentId + ']');
						
						contentId4Add.push(contentId);
						
						//显示人员头像
						reportedPersonUl.show();
						//设置人员头像标题
						reportedPersonUl.children('a.hd-box').attr('title', '${list.name!}');
						
						$('#' + contentId + ' #name').html('${list.name!}');
						$('#' + contentId + ' #age').html('${list.age!}');
						$('#' + contentId + ' #profession').html('${list.profession!}');
						$('#' + contentId + ' #idCard').html('${list.idCard!}');
						$('#' + contentId + ' #homeAddr').html('${list.homeAddr!}');
					}
				</#list>
				
				if(contentId4Add.length > 0) {
					selectReportedInfo(contentId4Add[0]);
				}
			</#if>
		}
		
		function startWorkflow() {//启动流程
			var clueId = $("#clueId").val();
			
			if(clueId) {
				$("#eventSBREClueForm").attr("action", "${rc.getContextPath()}/zhsq/eventSBREClue/startWorkflow4Clue.jhtml");
				
				modleopen();
				
				$("#eventSBREClueForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						parent.searchData();
						parent.detail(clueId, "2");
						if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
							parent.closeBeforeMaxJqueryWindow();
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
	
		function eventClueSubTask() {//点击提交按钮调用方法
			if($("#feedbackInfoDiv").is(":visible")) {
				var isValid = $('#eventSBREClueForm').form('validate');
				
				if(isValid) {
					$("#eventSBREClueForm").attr("action", "${rc.getContextPath()}/zhsq/eventSBREClue/saveClue.jhtml");
					
					$("#eventSBREClueForm").ajaxSubmit(function(data) {
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
	
		function radioCheckCallback(option) {//下一环节选中回调方法
			var FEED_BACK_TASK_NAME = 'task4',//线索反馈
				isShowFeedback = FEED_BACK_TASK_NAME == '${curNodeName!}';
				
			if(isShowFeedback) {
				$('#feedbackInfoDiv').show();
			} else {
				$('#feedbackInfoDiv').hide();
			}
			
			$('#feedbackInfoDiv input.easyui_required').each(function() {
				$(this).validatebox({
					required: $(this).is(':visible')
				});
			});
		}
		
		function flashData(msg) {//工作办理回调
			parent.reloadDataForSubPage(msg, true);
		}
	
		function showWorkflowDetail() {//流程详情
			var instanceId = "<#if instanceId??>${instanceId?c}</#if>";
			if(instanceId) {
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
						
						$("#taskDetailDiv").hide();//为了防止当外框有滚动条时，可以在选择“线索详情”时看到“处理环节”的内容
					}
				});
			}
		}
		
		function showGangInfo(gangId) {
			if(gangId) {
				openJqueryWindowByParams({
					title			: "查看黑恶团伙信息",
					targetUrl		: '${rc.getContextPath()}/zhsq/eventSBREvilGang/detail.jhtml?gangId=' + gangId
				});
			}
		}
	</script>
</html>