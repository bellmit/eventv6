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
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
		
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
		<form id="eventSBREClueForm" name="eventSBREClueForm" action="" method="post" enctype="multipart/form-data">
			<input type="hidden" id="clueId" name="clueId" value="<#if clue.clueId??>${clue.clueId?c}</#if>" />
			<!-- 涉及人员 -->
			<input type="hidden" id="involvedPeopleArr" name="involvedPeopleArr" />
			<!-- 附件 -->
			<input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
			<!-- 黑恶团伙id -->
			<input type="hidden" id="gangIds" name="gangIds" value="" />
			<!-- 更新黑恶团伙关联信息 -->
			<input type="hidden" name="isAlterGangRela" value="true" />
			
			<div class="container_fluid">
				<div class="form-warp-sh"><!-- 外框 -->
					<!-- 顶部标题 -->
					<div id="topTitleDiv" class="fw-toptitle">
						<h6 class="note-s">带<span>*</span>为必填项</h6>
						<div class="fw-tab"><!-- 切换标签 -->
							<ul class="tab-hk clearfix">
								<li class="active"><i class="order order1"></i><a href="#informantInfoDiv">举报人信息</a></li>
								<li><i class="order order2"></i><a href="#reportedInfoDiv">被举报人信息</a></li>
								<li><i class="order order3"></i><a href="#clueBasicInfoDiv">线索基础信息</a></li>
								<li class="barline"></li>
							</ul>
						</div>
					</div>
					
					<!-- 主体内容 -->
					<div id="mainDiv" class="fw-main">
						<!-- 举报人信息 -->
						<div id="informantInfoDiv">
							<input type="text" id="bizType" class="hide" value="09" />
							<ul id="informantInfoUl" class="fw-xw-from clearfix" style="border-bottom: 1px dashed #ccc; padding-bottom: 20px;">
								<li id="isRealNameLi" class="xw-com1">
									<#if (instanceId?? && instanceId > 0)>
										<span class="fw-from1">实名举报：</span>
										<p class="from flex1">${clue.isRealNameName!}</p>
									<#else>
										<span class="fw-from1"><i class="spot-xh">*</i>实名举报：</span>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="isRealName_1" name="isRealName" value="1" />
											<span class="radio-input"></span>是
										</label>
										<label class="fw-radio-box">
											<input type="radio" class="fw-radio" id="isRealName_0" name="isRealName" value="0" checked />
											<span class="radio-input"></span>否
										</label>
									</#if>
								</li>
								<li class="xw-com2">
									<span class="fw-from1"><i class="spot-xh realNameAsterisk hide">*</i>举报人姓名：</span>
									<input type="text" id="name" value="<#if informantInfo??>${informantInfo.name!}</#if>" class="from flex1 realNameRequired easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" onblur="findReportedClueCount(this, '09');" />
								</li>
								<li class="xw-com2">
									<span class="fw-from1"><i class="spot-xh realNameAsterisk hide">*</i>举报人联系方式：</span>
									<input type="text" id="tel" value="<#if informantInfo??>${informantInfo.tel!}</#if>" class="from flex1 realNameRequired easyui-validatebox" data-options="tipPosition:'bottom',validType:'mobileorphone'" />
								</li>
								<li id="09_clueLi" class="xw-com1" style="display: none;">
									<span class="fw-from1" style="cursor: pointer;" onclick="showReportedClue('09');"><span id="09_involvedPeopleName" class="font-blue"></span>共计举报线索<span id="09_clueTotal" class="font-red">0</span>条</span>
								</li>
							</ul>
						</div>
						
						<!-- 被举报人信息 -->
						<div id="reportedInfoDiv" class="mt30 b-repor">
							<div class="repor-head">
								<span class="fw-from1"><i class="spot-xh"></i>被举报人：</span>
								<ul id="reportedPersonUl" class="headlist flex1 clearfix">
									<li id="addReportedPersonLi">
										<a class="hd-add" title="点击新增" onclick="addReportedInfo();"></a>
									</li>
									<li id="defaultReportedPersonLi" style="display: none;">
										<a class="hd-box-spot"></a>
										<a class="hd-box bd-on"></a>
									</li>
									
								</ul>
							</div>
							
							<div id="defaultReportedContentDiv" class="report-pep xw-com1 hide" index="0">
								<input type="text" id="bizType" class="hide" value="10" />
								<ul class="fw-xw-from clearfix ">
									<li class="xw-com2">
										<span class="fw-from1"><i class="spot-xh">*</i>姓名：</span>
										<input type="text" id="name" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" onblur="findReportedClueCount(this, '10');"/>
									</li>
									<li class="xw-com2">
										<span class="fw-from1">年龄：</span>
										<input type="text" id="age" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:'numLength[3]'" />
									</li>
									<li class="xw-com2">
										<span class="fw-from1">职业：</span>
										<input type="text" id="profession" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" />
									</li>
									<li class="xw-com2">
										<span class="fw-from1">身份证号：</span>
										<input type="text" id="idCard" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:'idcard'" />
									</li>
									<li class="xw-com1">
										<span class="fw-from1">家庭住址：</span>
										<input type="text" id="homeAddr" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[256]','characterCheck']" />
									</li>
								</ul>
							</div>
							
							<div>
								<ul>
									<li id="10_clueLi" class="xw-com1" style="display: none;">
										<span class="fw-from1" style="cursor: pointer;" onclick="showReportedClue('10');"><span id="10_involvedPeopleName" class="font-blue"></span>共计被举报线索<span id="10_clueTotal" class="font-red">0</span>条</span>
									</li>
								</ul>
							</div>
						</div>
						
						<!-- 线索基础信息 -->
						<div id="clueBasicInfoDiv" class="mt30">
							<ul class="fw-xw-from clearfix" style="border-top: 1px dashed #ccc; padding:20px 0 0 0;" >
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh">*</i>线索标题：</span>
									<input type="text" name="clueTitle" class="from flex1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${clue.clueTitle!}" />
								</li>
								<li class="xw-com3">
									<span class="fw-from1"><i class="spot-xh">*</i>发生区域：</span>
									<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${clue.infoOrgCode!}" />
									<input type="text" id="gridName" class="from flex1 bg-btm-arrow easyui-validatebox" data-options="required:true,tipPosition:'bottom'" value="${clue.gridName!}" />
								</li>
								<li class="xw-com3">
									<span class="fw-from1"><i class="spot-xh">*</i>线索来源：</span>
									<input type="hidden" id="clueSource" name="clueSource" value="${clue.clueSource!''}" />
									<input type="text" id="clueSourceName" class="from flex1 bg-btm-arrow easyui-validatebox" data-options="required:true,tipPosition:'bottom'" />
								</li>
								<li class="xw-com3">
									<span class="fw-from1"><i class="spot-xh">*</i>重要程度：</span>
									<input type="hidden" id="importantDegree" name="importantDegree" value="${clue.importantDegree!''}"/>
									<input type="text" id="importantDegreeName" class="from flex1 bg-btm-arrow easyui-validatebox" data-options="required:true,tipPosition:'bottom'" />
								</li>
								<li class="xw-com3">
									<span class="fw-from1"><i class="spot-xh">*</i>加密线索：</span>
									<label class="fw-radio-box">
										<input type="radio" class="fw-radio" id="isEncrypt_1" name="isEncrypt" value="1" />
										<span class="radio-input"></span>是
									</label>
									<label class="fw-radio-box">
										<input type="radio" class="fw-radio" id="isEncrypt_0" name="isEncrypt" value="0" checked />
										<span class="radio-input"></span>否
									</label>
								</li>
								<li class="xw-com3">
									<span class="fw-from1"><i class="spot-xh"></i>举报时间：</span>
									<input type="text" name="reportDateStr" class="from flex1 bg-ico-day Wdate" style="cursor: pointer;" onclick="WdatePicker({readOnly:true, maxDate:'${maxReportDate!}', dateFmt:'yyyy-MM-dd', isShowToday:false})" value="${clue.reportDateStr!}" readonly="readonly" />
								</li>
                                <li class="xw-com3">
                                    <span class="fw-from1"><i class="spot-xh">*</i>涉及打击重点：</span>
                                    <input type="hidden" id="involveAttackFocus" name="involveAttackFocus" value="${clue.involveAttackFocus!''}"/>
                                    <input type="text" id="involveAttackFocusName" class="from flex1 bg-btm-arrow easyui-validatebox"  data-options="required:true,tipPosition:'bottom'" />
                                </li>
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh"></i>涉及黑恶团伙：</span>
									
									<div class="darkteam flex1" type="text">
										<div class="btn-warp ta-l" style="margin: 0;">
											<a class="btn-bon blue-btn" onclick="showGangWin();">选择黑恶团伙</a>
										</div>
										<ul id="gangInfoUl" class="clearfix">
										</ul>
									</div>
								</li>
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh">*</i>线索简要内容：</span>
									<textarea name="clueContent" class="textfrom flex1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2000]','characterCheck']" >${clue.clueContent!}</textarea>
								</li>
								<li class="xw-com1">
									<span id="href2" class="fw-from1">线索附件：</span>
									<div class="ImgUpLoad" id="fileupload"></div>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">备注：</span>
									<textarea name="clueRemark" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[2000]','characterCheck']" >${clue.clueRemark!}</textarea>
								</li>
							</ul>
						</div>
						
					</div>
					
					<!-- 操作按钮 -->
					<div id="btnDiv" class="btn-warp">
						<a class="btn-bon green-btn" onclick="saveEventSBREClue(false);">保存</a>
						<#if !(instanceId?? && instanceId > 0)>
						<a class="btn-bon blue-btn" onclick="saveEventSBREClue(true);">提交</a>
						</#if>
					</div>
				</div>
			</div>
		</form>
	</body>
	
	<script type="text/javascript">
	
		var base = '${rc.getContextPath()}';
		var imgDomain = '${IMG_URL}';
		var uiDomain = '${uiDomain}';
		var componentsDomain = '${SQ_COMPONENTS_URL}';//公共组件工程域名
		var clueId;
		
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,removeCode = "${removeCode!}";
			clueId = $("#clueId").val();
			var swf={
				useType: 'add',//附件上传的使用类型，add,edit,view，（默认edit）;
				chunkSize : 5,//切片的大小（默认5M）
				fileNumLimit : 50,//最大上传的文件数量（默认9）
				maxSingleFileSize:50,//单个文件最大值（默认300）,单位M
				fileExt : '.jpg,.gif,.png,.jpeg,.zip,.doc,.docx,.amr,.mp3,.mp4',//支持上传的文件后缀名(默认值请查看参数说明）
				appcode:"event_SBRE_clue",//文件所属的应用代码（默认值components）
				module:"attachment",//文件所属的模块代码（默认值bigfile）
				imgDomain : imgDomain,//图片服务器域名
				uiDomain : uiDomain,//公共样式域名
				componentsDomain : componentsDomain,//公共组件域名
				isSaveDB : true,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
				isDelDbData:true,//是否删除数据库数据(默认true)
				isUseLabel: true,//是否使用附件标签(默认false)
				labelDict : [{'name':'处理前','value':'1'},{'name':'处理中','value':'2'},{'name':'处理后','value':'3'}],
				styleType:"box",//块状样式：box,列表样式：list,自定义样式：self
			};
			
			if(clueId){
				swf['useType']='edit';
				swf['attachmentData']={bizId:clueId,attachmentType:'${attachmentType!}'};
			}
				
			$("#fileupload").bigfileUpload(swf);
			
		
			AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				}
			});
			
			AnoleApi.initListComboBox("clueSourceName", "clueSource", {"B591001": removeCode.split(",")},null,["${clue.clueSource!''}"], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "1"
			});
			AnoleApi.initListComboBox("importantDegreeName", "importantDegree", "B591002",null,["${clue.importantDegree!''}"]);
			/*涉及打击重点*/
            var involveAttackFocusArr = ("${clue.involveAttackFocus!}").split(",");
			AnoleApi.initTreeComboBox("involveAttackFocusName", "involveAttackFocus", "B591006",null,involveAttackFocusArr,{
			    RenderType:'11',
				FilterData:function (data) {
					if (data && data.length > 0) {
					    for (var i = 0,len = data.length;i < len;i++) {
					        /*04,05,07包含二级选项，将父级设置为不可选*/
					        if (data[i].value && (data[i].value == '04' || data[i].value == '05' ||data[i].value == '07')) {
					            data[i]["nocheck"] = true;
							}
						}
					}
					return data;
                }
			});

			<#if reportedInfoList?? && (reportedInfoList?size>0)>
                showReportedPeople();//若被举报人存在，展示被举报人信息
			<#else >
                addReportedInfo();//默认展示一个被举报人信息新增模块
			</#if>

			<#if clue.isEncrypt??>
				$('#isEncrypt_${clue.isEncrypt}').attr('checked', true);
			</#if>
			
			<#if gangList?? && (gangList?size > 0)>
				var gangArray = [],
					gangObj = {};
					
				<#list gangList as gang>
					gangObj = {};
					gangObj.gangId = '<#if gang.gangId>${gang.gangId?c}</#if>';
					gangObj.gangName = '${gang.gangName!}';
					
					gangArray.push(gangObj);
				</#list>
				
				checkEvilGangs(gangArray);
			</#if>
			
			<#if (instanceId?? && instanceId > 0)>
				realNameCheck('${clue.isRealName!}');
			<#else>
				$('#isRealNameLi input[type=radio][name=isRealName]').on('click', function() {
					realNameCheck($(this).val());
				});
				
				$("#isRealName_${clue.isRealName!'0'}").click();
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
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function realNameCheck(isRealNameVal) {//实名制核查
			var isRealName = isRealNameVal == '1';
			
			if(isRealName) {
				$('#informantInfoUl .realNameAsterisk').show();
			} else {
				$('#informantInfoUl .realNameAsterisk').hide();
			}
			
			$('#informantInfoUl .realNameRequired').validatebox({
				required: isRealName
			});
		}
		
		function findReportedClueCount(obj, bizType) {//依据举报人/被举报人姓名查找线索数量
			var involvedPeopleName4Accurate = $(obj).val();
			
			$("#mainDiv li[id $= _clueLi").hide();
			
			if(isNotBlankString(involvedPeopleName4Accurate)) {
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/eventSBREClue/findReportedClueCount.jhtml',
					data: {'listType': 4, 'involvedPeopleName4Accurate': involvedPeopleName4Accurate, 'involvedPeopleBizType': bizType},
					dataType:"json",
					success: function(data) {
						var total = data.total || 0;
						
						if(total > 0) {
							$('#' + bizType + '_clueLi').show();
							$('#' + bizType + '_clueTotal').html(total);
							$('#' + bizType + '_involvedPeopleName').html(involvedPeopleName4Accurate);
						}
					},
					error:function(data) {
						$.messager.alert('错误','获取线索数量失败！','error');
					}
				});
			}
		}
		
		function showReportedClue(bizType) {
			var involvedPeopleName4Accurate = $('#' + bizType + '_involvedPeopleName').html();
			
			if(isNotBlankString(involvedPeopleName4Accurate) && isNotBlankString(bizType)) {
				var url = "${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=4&isCopyClue=true&involvedPeopleName4Accurate=" + involvedPeopleName4Accurate + "&involvedPeopleBizType=" + bizType;
				 
	            openJqueryWindowByParams({
	                title			: "已上报线索信息",
	                targetUrl		: url
	            });
            }
		}
		
		function addReportedInfo(isCheck) {
			var index = parseInt($('#defaultReportedContentDiv').attr('index'), 10) + 1,
				isValid = index == 1,//添加首个时，不验证，否则不能加载出
				contentId = '';
			
			if(index > 1) {
				if(isCheck == true) {
					isValid = $("#eventSBREClueForm").form('validate');
				} else {
					isValid = true;
				}
			}
			
			if(isValid) {
				var cloneContentObj = $('#defaultReportedContentDiv').clone(),
					clonePersonObj = $('#defaultReportedPersonLi').clone(),
					index = parseInt($('#defaultReportedContentDiv').attr('index'), 10) + 1,
					randomIndex = Math.random().toString().substr(2),
					personId = 'reportedPersonLi_' + index + '_' + randomIndex;
				
				contentId = 'reportedInfoDiv_' + index + '_' + randomIndex;
				
				cloneContentObj.attr('id', contentId);
				$('#defaultReportedContentDiv').attr('index', index);
				clonePersonObj.attr({'id': personId, 'contentId': contentId, 'onclick': 'selectReportedInfo("' + contentId + '")'});
				
				$('#reportedPersonUl').append(clonePersonObj);
				$('#reportedInfoDiv').append(cloneContentObj);
				$.parser.parse(cloneContentObj);//重新渲染模块，从而使得验证生效  
				
				$('#' + contentId + ' input[id=name]').validatebox({
					required: true
				});
				
				$('#' + personId + ' > a.hd-box-spot').attr('onclick', 'delReportedInfo("'+ contentId +'", "'+ personId +'")');
				
				selectReportedInfo(contentId);
			}
			
			return contentId;
		}
		
		function selectReportedInfo(contentId) {
			var personObj = $('#reportedPersonUl li[contentId='+ contentId +']');
			
			if(personObj.length > 0) {//删除操作时，由于冒泡会触发一次父级的onclick事件
				var selectedObj = $('#reportedPersonUl a.bd-on'),
					selectedContentId = selectedObj.parent().attr('contentId');
				
				selectedObj.attr('title' , $('#' + selectedContentId + ' input[id=name]').val());
				
				$('#reportedPersonUl a.hd-box').removeClass('bd-on');
				personObj.children('a.hd-box').addClass('bd-on');
				$('#reportedInfoDiv div[id^=reportedInfoDiv_]').hide();
				
				$('#reportedPersonUl li[contentId='+ contentId +']').show();
				$('#' + contentId).show();
			}
		}
		
		function delReportedInfo(contentId, personId) {
			var index = parseInt($('#defaultReportedContentDiv').attr('index'), 10) - 1;
			
			if(personId) {
				$('#' + personId).remove();
			} else {
				$('#reportedPersonUl li[contentId='+ contentId +']').remove();
			}
			
			$('#' + contentId).remove();
			
			if(index <= 0) {//保证至少有一个被举报人
				index = 1;
				addReportedInfo();
			} else {//默认选中首个
				selectReportedInfo($('#reportedInfoDiv div[id^=reportedInfoDiv_]').eq(0).attr('id'));
			}
			
			$('#defaultReportedContentDiv').attr('index', index);
		}
		
		function fetchReportedInfo() {
			var reportedObjArray = [],
				reportedObj = {},
				inpVal = "";
			
			$('#reportedInfoDiv div[id^=reportedInfoDiv_]').each(function() {
				reportedObj = {};
				
				$(this).find('input[type=text]').each(function() {
					inpVal = $(this).val();
					
					if(inpVal) {
						reportedObj[$(this).attr('id')] = inpVal;
					}
				});
				
				reportedObjArray.push(reportedObj);
			});
			
			//构建举报人员信息
			reportedObj = {};
			
			$('#informantInfoDiv input[type=text]').each(function() {
				inpVal = $(this).val();
				
				if(inpVal) {
					reportedObj[$(this).attr('id')] = inpVal;
				}
			});
			
			reportedObjArray.push(reportedObj);
			
			if(reportedObjArray.length > 0) {
			    $("#involvedPeopleArr").val(JSON.stringify(reportedObjArray));
			}
		}
		
		function showReportedPeople() {//若被举报人存在，展示被举报人信息
			<#if reportedInfoList?? && (reportedInfoList?size>0)>
				var contentId = null,
					contentId4Add = [];
				
				<#list reportedInfoList as list>
					contentId = addReportedInfo(false);
					if(contentId) {
						contentId4Add.push(contentId);
						
						$('#' + contentId + ' #name').val('${list.name!}');
						$('#' + contentId + ' #age').val('${list.age!}');
						$('#' + contentId + ' #profession').val('${list.profession!}');
						$('#' + contentId + ' #idCard').val('${list.idCard!}');
						$('#' + contentId + ' #homeAddr').val('${list.homeAddr!}');
					}
				</#list>
				
				if(contentId4Add.length > 0) {
					selectReportedInfo(contentId4Add[0]);
				}
			</#if>
		}
		
		function saveEventSBREClue(isStart) {//isStart:true 保存线索并提交启动流程;false:仅保存线索
			var isValid = $("#eventSBREClueForm").form('validate');
			
			$("#eventSBREClueForm").attr("action", "${rc.getContextPath()}/zhsq/eventSBREClue/saveClue.jhtml");
			
			if (isValid) {
				//涉及人员
				fetchReportedInfo();
				
				//附件
				var attachments = "",
					attachmentInpt = $('#eventSBREClueForm input[name="attachmentId"]');
					
				if(attachmentInpt && attachmentInpt.length>0) {
					attachmentInpt.each(function() {
						attachments += "," + $(this).val();
					});
					attachments = attachments.substr(1);
				}
				
				$("#attachmentIds").val(attachments);
				
				modleopen();
				
				$("#eventSBREClueForm").ajaxSubmit(function (data) {
					modleclose();
					
					if (data.result && data.result == true) {
						if(isStart && isStart == true) {
							if(data.clueId) {//为了保障后续的流程启动
								$("#clueId").val(data.clueId);
							}
							
							startEventSBREClue();
						} else {
							parent.reloadDataForSubPage(data.tipMsg, true);
						}
					} else {
						$.messager.alert('提示',data.tipMsg,'info');
					}
				});
			}
		}

        function startEventSBREClue() {//启动流程
            var isValid = $("#eventSBREClueForm").form('validate');
            
            $("#eventSBREClueForm").attr("action", "${rc.getContextPath()}/zhsq/eventSBREClue/startWorkflow4Clue.jhtml");

            if (isValid) {
            	modleopen();

                $("#eventSBREClueForm").ajaxSubmit(function (data) {
                	modleclose();

                	if (data.success == true) {//提交
                		parent.searchData();//刷新列表，防止重复提交
                		parent.detail(data.clueId, "2");//弹出待办详情页面
                		if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
                			parent.closeBeforeMaxJqueryWindow();//关闭新增/编辑窗口
                		}
                	} else {
                		 $.messager.alert('提示',data.tipMsg,'info');
                	}
                });
            }
        }
        
        function showGangWin() {
        	var selectedGangIds = "",
        		url = '${rc.getContextPath()}/zhsq/eventSBREvilGang/check.jhtml';
        	
        	$('#gangInfoUl > li').each(function() {
        		selectedGangIds += ',' + $(this).attr('id');
        	});
        	
        	if(selectedGangIds.length > 0) {
        		url += '?ids=' + selectedGangIds.substr(1);
        	}
        	
        	openJqueryWindowByParams({
                title			: "选择黑恶团伙",
                targetUrl		: url
            });
        }
        
        function checkEvilGangs(selectedRows) {//黑恶团伙选中回调方法
        	$('#gangInfoUl').html('');//清空原有后，重新生成黑恶团伙信息
        	
        	if(selectedRows.length > 0) {
        		var selectedRow = null,
        			selectedGangId = null,
        			selectedGangName = null;
        			gangIds = ''; 
        		
        		for(var index = 0, len = selectedRows.length; index < len; index++) {
        			selectedRow = selectedRows[index];
        			selectedGangId = selectedRow.gangId;
        			selectedGangName = selectedRow.gangName;
        			
        			if(selectedGangId && selectedGangName) {
	        			gangIds += ',' + selectedGangId;
	        			if($('#gangInfoUl > li[id="'+ selectedGangId +'"]').length == 0) {
	        				$('#gangInfoUl').append('<li id="'+ selectedGangId +'" class="darkteam-txt" title="'+ selectedGangName +'"><a onclick="showGangInfo('+ selectedGangId +');">'+ selectedGangName +'</a></li>');
	        			}
        			}
        		}
        		
        		if(gangIds.length > 0) {
        			$('#gangIds').val(gangIds.substr(1));
        		}
        	}
        	
        	closeMaxJqueryWindow();
        }
        
        function showGangInfo(gangId) {
        	if(gangId) {
	        	openJqueryWindowByParams({
	                title			: "查看黑恶团伙信息",
	                targetUrl		: '${rc.getContextPath()}/zhsq/eventSBREvilGang/detail.jhtml?gangId=' + gangId
	            });
            }
        }
        
        function copyClueInfo(clueId) {//线索复制回调方法
        	closeMaxJqueryWindow();
        	
        	window.location = "${rc.getContextPath()}/zhsq/eventSBREClue/toAdd.jhtml?copyClueId=" + clueId + '&clueId=' + $('#clueId').val();
        }
	</script>

</html>