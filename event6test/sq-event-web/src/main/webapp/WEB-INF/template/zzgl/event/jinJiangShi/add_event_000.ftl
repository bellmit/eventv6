<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="eventAddPageTitle">事件新增/编辑-晋江</@block></title>

	<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/add.css" />
	
	<!-- 命案防控-->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#--组织单位选择初始化框-->
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>

	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
	<script type="text/javascript" src="${(GEO_URL)}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
	<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	
	<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
	<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
	
	<#include "/component/bigFileUpload.ftl" />
	<#include "/component/ComboBox.ftl" />
	
	<style type="text/css">
		.WindowLeftMenu .list {margin-left: 0; width: 150px;}
		.WindowLeftMenu .list dd a {text-decoration : none;}
		.tagList {
            display: block;
            overflow: hidden;
        }

        .tagList li {
            float: left;
            width: 100px;
            border-radius: 3px;
            padding: 8px 10px;
            margin: 5px 10px;
            cursor: pointer;
            text-align: center;
            font-size: 13px;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .tagList li.normalItem {
            background-color: #eff2f4;
            border: solid 1px #ddd;
        }

        .tagList li.activeItem {
            background-color: #4395E1;
            border: solid 1px #79c0df;
            color: #fff;
        }
        .icon-add-w{
    		color:#f08750;
    		display:inline-block;
    		width:13px;
    		height:13px;
    		margin-right:5px;
    		vertical-align:-2px;
    		background:#2980B9 url('${uiDomain!''}/css/person/img/icon-add-w.png') top center no-repeat;
   		}
   		
   		input[type='checkbox'].toggle {
    		display: inline-block;
    		-webkit-appearance: none;
    		-moz-appearance: none;
    		appearance: none;
    		width: 55px;
    		height: 28px;
    		background-color: #CCCCCC;
    		position: relative;
    		-moz-border-radius: 30px;
    		-webkit-border-radius: 30px;
    		border-radius: 30px;
    		@inlcude box-shadow(none): ;
    		-moz-transition: all 0.2s ease-in-out;
    		-o-transition: all 0.2s ease-in-out;
    		-webkit-transition: all 0.2s ease-in-out;
    		transition: all 0.2s ease-in-out;
		}
    	input[type='checkbox']:checked.toggle {
   			-moz-box-shadow: inset 0 0 0 15px #158EC6;
    		-webkit-box-shadow: inset 0 0 0 15px #158EC6;
    		box-shadow: inset 0 0 0 15px #158EC6;
		}
		
        .fw-main ul li{
        	clear: unset;
        }
        
        .leftTdWidth{
        	width:52%;
        }
	</style>
</head>

<!--菜单新增操作时，选择事件类型后，会导致外框出现横纵向滚动条，草稿新增/编辑操作不会，具体缘由不明，使用 overflow:hidden可修复该问题-->
<body style="overflow: hidden;">
	<div class="OpenWindow">
		<!--left menu begin-->
		<div class="WindowLeftMenu fl hide" id="leftMenuDiv" style="padding:0;">
			<div class="list">
				<dl>
					<dd id="leftMenuDD">
						<p class="current finish"><a href="#eventLabel_eventBasicInfo">基本信息</a></p>
						<#if careRoads??>
							<p id="eventLabel_careRoadsp" labelTypeName="careRoads" ><a href="#eventLabel_careRoads">涉及线路案(事)件</a></p>
						</#if>
						<#if majorRelatedEvents??>
							<p id="eventLabel_majorRelatedEventsp" labelTypeName="majorRelatedEvents"><a href="#eventLabel_majorRelatedEvents">重特大案(事)件</a></p>
						</#if>
						<#if homicideCase??>
							<p id="eventLabel_homicideCasep" labelTypeName="homicideCase" ><a href="#eventLabel_homicideCase">命案防控</a></p>
						</#if>
						<#if disputeMediation??>
							<p id="eventLabel_disputeMediationp" labelTypeName="disputeMediation" ><a href="#eventLabel_disputeMediation">矛盾纠纷排查化解</a></p>
						</#if>
						<#if schoolRelatedEvents??>
							<p id="eventLabel_schoolRelatedEventsp" labelTypeName="schoolRelatedEvents" ><a href="#eventLabel_schoolRelatedEvents">涉及师生(事)件</a></p>
						</#if>
					</dd>
				</dl>
			</div>
		</div>
		<!--left menu end-->
		
		<div class="MC_con content light" style="overflow-x:hidden">
			<!--NorForm begin-->
			<div class="con BaseInfo NorForm" id="norFormDiv">
				<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
					<!--right content begin-->
						<div class="WindowRihgtCon fl"  id="rightContentDiv" style="position: fixed;">
							<!--事件类别  开始-->
							<div id="eventLabelTypeNameDiv">
								<div class="title FontDarkBlue">事件类别</div>
								<div class="has-more">
									<div class="basic-infor">
										<div class="tagList">
											<li class="normalItem"><span labelTypeName="careRoads" triggerId="isSaveCareRoads">涉及线路案(事)件</span></li>
											<li class="normalItem"><span labelTypeName="majorRelatedEvents" triggerId="isSaveMajorRelatedEvents">重特大案(事)件</span></li>
											<li class="normalItem"><span labelTypeName="homicideCase" triggerId="isSaveHomicideCase">命案防控</span></li>
											<li class="normalItem"><span labelTypeName="disputeMediation" triggerId="isSaveDisputeMediation">矛盾纠纷排查化解</span></li>
											<li class="normalItem"><span labelTypeName="schoolRelatedEvents" triggerId="isSaveSchoolRelatedEvents">涉及师生(事)件</span></li>
										</div>
									</div>
								</div>
							</div>
							<!--事件类别  结束-->

							<div id="content-d">
								<div id="eventLabelContentIncludeDiv">
									<!--基本信息-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_basic.ftl" />
									<!--涉及线路案(事)件-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_careRoads.ftl" />
									<!--重特大案(事)件-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_majorRelatedEvents.ftl" />
									<!--命案防控-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_homicideCase.ftl" />
									<!--矛盾纠纷排查化解-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_disputeMediation.ftl" />
									<!--涉及师生(事)件-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_schoolRelatedEvents.ftl" />
								</div>
							</div><!--content-d end-->
							
							<!--操作按钮 开始-->
							<div id="btnListDiv" class="BigTool">
								<div class="BtnList">
									<@block name="btnListContent">
									<#if (isReport?? && isReport)>
										<a href="###" onclick="showAdvice('saveEventAndStart', 'parent.startWorkFlow');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
										<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
									<#else>
										<#if isShowSaveBtn?? && !isShowSaveBtn>
										<#else>
											<a href="###" onclick="showAdvice('saveEvent');" class="BigNorToolBtn SaveBtn">保存</a>
										</#if>
										
										<#if !(instanceId?? && (instanceId > 0))>
											<a href="###" onclick="showAdvice('saveEventAndStart', 'parent.startWorkFlow');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
											<!--
											<a href="###" onclick="showAdvice(null, 'parent.startWorkFlow', '1');" class="BigNorToolBtn BigJieAnBtn">结案</a>
											-->
										</#if>
										
										<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
									</#if>
									</@block>
								</div>
							</div>
							<!--操作按钮 结束-->
							
						</div>
						<!--right content end-->
				</form>
				
			</div>
			<!--NorForm end-->
							
		</div>
		
	</div>
	
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	
</body>

<script type="text/javascript">
	var _winHeight = 0, _winWidth = 0;
	
	$(function() {
		//当前用户缺少信息权时直接关闭页面
		var startDivisionCode = "${START_DIVISION_CODE!}";
		if ('-1' == startDivisionCode) {
			if(parent && typeof parent.msgPage === 'function') {
				parent.msgPage('当前用户缺少信息权，请联系管理员配置！');
			} else {
				$.messager.alert('提示','当前用户缺少信息权，请联系管理员配置！','INFO');
				return;
			}
		}

		_winHeight = $(window).height();
		_winWidth = $(window).width();

		$('#leftMenuDiv').height(_winHeight);
		$('#rightContentDiv').height(_winHeight - $('#eventLabelTypeNameDiv').outerHeight(true) - $('#btnListDiv').outerHeight(true))
										.width(_winWidth);
		
		//左方菜单点击相应事件，为滚动条滚动添加动画效果
		$('#leftMenuDD > p > a').on('click', function() {
			$(this).parent().addClass("current finish").siblings().removeClass("current finish");
			$("#content-d").animate({scrollTop: $(this.hash)[0].offsetTop - $('#eventLabelTypeNameDiv').height()}, 400);
		});
		
		//事件类别点击相应事件
		$('#eventLabelTypeNameDiv div.tagList > li').on('click', function() {
			var tagListSpan = $(this).find('span').eq(0),
				  eventLabelType = tagListSpan.attr('labelTypeName'),
				  eventLabelId = "eventLabel_" + eventLabelType,
				  eventLabelpId = eventLabelId + 'p',
				  triggerId = tagListSpan.attr('triggerId'),
				  isTriggerOn = false,
				  leftMenuDivWidth = 0,
				  isItem2Hide = $(this).hasClass('activeItem');
			
			if(isItem2Hide) {
				$(this).removeClass('activeItem');
				$('#' + eventLabelpId).hide();
				
				$('#eventLabelContentIncludeDiv div.' + eventLabelType).hide();
				
				$('#leftMenuDD > p > a').eq(0).click();
			} else {
				$(this).addClass('activeItem');
				
				if($('#' + eventLabelpId).length == 0) {
					$('#leftMenuDD').append('<p id="' + eventLabelpId + '"><a href="#' + eventLabelId + '">' + tagListSpan.html() + '</a></p>');
					
					$('#' + eventLabelpId + ' > a').on('click', function() {
						$(this).parent().addClass("current finish").siblings().removeClass("current finish");
						$("#content-d").animate({scrollTop: $(this.hash)[0].offsetTop - $('#eventLabelTypeNameDiv').height()}, 400);
					});
				} else {
					$('#' + eventLabelpId).show();
				}
				
				$('#eventLabelContentIncludeDiv div.' + eventLabelType).show();
				$('#leftMenuDiv').show();
				
				isTriggerOn = true;
				
				$('#' + eventLabelpId + ' > a').eq(0).click();//手动触发点击事件
			}

			var requestParamObj = null;
			
			if(isItem2Hide) {
				requestParamObj = $('#eventLabelContentIncludeDiv div.' + eventLabelType + ' .requestParam');
			} else {
				requestParamObj = $('#eventLabelContentIncludeDiv div.' + eventLabelType + ' .requestParam:visible');
			}
			requestParamObj.each(function(i){
				if($(this).hasClass('comboselector')){
					$(this).comboselector({
						required: !isItem2Hide,
						tipPosition:'bottom'
					});
				}else{
					$(this).validatebox({
						required: !isItem2Hide,
						tipPosition:'bottom'
					});
				}
			});
				
			$('#' + triggerId).val(isTriggerOn);
        
			if($('#leftMenuDD > p:visible').length > 1) {
				$('#leftMenuDiv').show();
				leftMenuDivWidth = $('#leftMenuDiv').outerWidth(true);
			} else {
				$('#leftMenuDiv').hide();
			}
			
			$('#rightContentDiv').width(_winWidth - leftMenuDivWidth).css('left', leftMenuDivWidth);
			
			$("#eventLabelContentIncludeDiv .autoCellWidth:visible").each(function() {
				//4像素为偏移量，可能会随着样式变更等进行调整
				$(this).width($('#contactUser').parent().parent().find('td').eq(0).outerWidth(true) + $('#contactUser').outerWidth(true) + 4);
			});
			
			//防止事件类别折行导致按钮被挤下去
			$('#content-d').height(_winHeight - $('#eventLabelTypeNameDiv').outerHeight(true) - $('#btnListDiv').outerHeight(true));

		});
		
		if($('#leftMenuDD > p').length > 1) {
			$('#leftMenuDiv').show();
			
			$('#leftMenuDD > p').each(function() {
				var eventLabelType = $(this).attr('labelTypeName');
				
				if(isNotBlankStringTrim(eventLabelType)) {
					$('#eventLabelTypeNameDiv div.tagList > li > span[labelTypeName="' + eventLabelType + '"]').parent().click();
				}
			});
			
			$('#rightContentDiv').width(_winWidth - $('#leftMenuDiv').outerWidth(true)).css('left', $('#leftMenuDiv').outerWidth(true));

			//初始化结束之后让左侧回到事件基本信息处
			$('#leftMenuDD > p > a').eq(0).click();
		} else {
			$("#eventLabelContentIncludeDiv .autoCellWidth:visible").not(".isSettledAutoWidth").each(function() {
				//4像素为偏移量，可能会随着样式变更等进行调整
				$(this).width($('#contactUser').parent().parent().find('td').eq(0).outerWidth(true) + $('#contactUser').outerWidth(true) + 4)
						 .addClass("isSettledAutoWidth");
			});
		}
		
		$('#content-d').height(_winHeight - $('#eventLabelTypeNameDiv').outerHeight(true) - $('#btnListDiv').outerHeight(true));

		//页面滚动条
		$("#content-d").niceScroll({
			cursorcolor:"rgba(0, 0, 0, 0.3)",
			cursoropacitymax:1,
			touchbehavior:false,
			cursorwidth:"4px",
			cursorborder:"0",
			cursorborderradius:"4px"
		});

		initItem4Basic();
		initItem4CareRoads();
		initItem4MajorRelatedEvents();
		initItem4DisputeMediation();
		initItem4SchoolRelatedEvents();
		initItem4HomicideCase();
	});
	
	/*******************************************careRoads_begin*******************************************************/
	
/*	function initItem4CareRoads(){
	
        AnoleApi.initListComboBox("careRoads_natureName", "careRoads_nature", "B040", null, [<#if careRoads?? && careRoads.nature??>"${careRoads.nature}"<#else>"1"</#if>]);
	
	}*/
	
	/*******************************************careRoads_end*******************************************************/
	
	/*******************************************schoolRelatedEvents_beigin*******************************************************/
	
	/*function initItem4SchoolRelatedEvents(){
	
        AnoleApi.initListComboBox("schoolRelatedEvents_natureName", "schoolRelatedEvents_nature", "B040", null, [<#if schoolRelatedEvents?? && schoolRelatedEvents.nature??>"${schoolRelatedEvents.nature}"<#else>"1"</#if>]);
        
		//聚焦输入框           
		 $("#schoolRelatedEvents_ecapeNum").next("span").children().eq(1).focus(function(){
				$(this).select();
		 });
		//聚焦输入框           
		 $("#schoolRelatedEvents_arrestedNum").next("span").children().eq(1).focus(function(){
				$(this).select();
		 });
		//聚焦输入框           
		 $("#schoolRelatedEvents_crimeNum").next("span").children().eq(1).focus(function(){
				$(this).select();
		 });
		
	}*/
	
	/*******************************************schoolRelatedEvents_end*******************************************************/
	
	/*******************************************涉及案件共用js方法_begin*******************************************************/
	
	function changeDetectionValue(typeName){
		var isDetection = $("#"+typeName+"_isDetectionCheck").is(":checked");
		if(isDetection){
			$("#"+typeName+"_isDetection").val(1);
		}else{
			$("#"+typeName+"_isDetectionCheck").removeAttr("checked");
			$("#"+typeName+"_isDetection").val(0);
		}
	}
	
	function countCrimeNum(typeName){
		var ecapeNum = $("#"+typeName+"_ecapeNum").val();
		var arrestedNum = $("#"+typeName+"_arrestedNum").val();
		var crimeNum = 0;
		
		if(ecapeNum == ''){
			ecapeNum = 0;
		}
		
		if(arrestedNum == ''){
			arrestedNum = 0;
		}
		
		crimeNum = parseInt(ecapeNum, 10) + parseInt(arrestedNum, 10);
		$("#"+typeName+"_crimeNum").val(crimeNum);
		$("#"+typeName+"_crimeNum").numberspinner('setValue', crimeNum);
	}
	
	/*******************************************涉及案件共用js方法_end*******************************************************/
	
	/*******************************************disputeMediation_begin*******************************************************/
	
	/*function initItem4DisputeMediation(){
	
		AnoleApi.initTreeComboBox("disputeMediation_mediationTypeStr", "disputeMediation_mediationType", "B417", null<#if disputeMediation?? && disputeMediation.mediationType??>, ["${disputeMediation.mediationType}"]</#if>);
		AnoleApi.initTreeComboBox("disputeMediation_disputeScaleStr", "disputeMediation_disputeScale", "B036", null<#if disputeMediation?? && disputeMediation.disputeScale??>, ["${disputeMediation.disputeScale}"]</#if>);
		
		AnoleApi.initListComboBox("disputeMediation_isSuccessStr", "disputeMediation_isSuccess", null, null,["<#if disputeMediation?? && disputeMediation.isSuccess??>${disputeMediation.isSuccess!''}</#if>"],{
			RenderType : "00",
			DataSrc : [{"name":"是","value":"1"},{"name":"否","value":"0"}]
		});
	}*/
	
	function addRecOrg(){
		var url = "${SQ_ZZGRID_URL}/zzgl/legal/base/form.jhtml?view=1";
        showMaxJqueryWindow("新增单位组织", url, 900, 400);
	}
	
	function cancleOrg(result,legalId,legalName){
		$("#mediationOrgName").textbox("setValue", legalName);
		closeMaxJqueryWindow();
		$.messager.alert('提示','新增单位组织信息已保存成功！','info');
	}	
	
	/*******************************************disputeMediation_end*******************************************************/
	
	function initItem4MajorRelatedEvents() {
		AnoleApi.initListComboBox("majorRelatedEvents_eventTypeName", "majorRelatedEvents_eventType", "B401000", null, ["<#if majorRelatedEvents??>${majorRelatedEvents.eventType!}</#if>"]);
        AnoleApi.initListComboBox("majorRelatedEvents_eventLevelName", "majorRelatedEvents_eventLevel", "B401001", null, ["<#if majorRelatedEvents??>${majorRelatedEvents.eventLevel!}</#if>"]);
	}
	
	function showAdvice(m, callback, toClose) {//展示办理意见
        if(toClose && toClose == '1') {
            $('#advice').validatebox({
                required: true
            });

            $("#adviceTr").show();

            closed(callback);
        } else {
            $("#adviceTr").hide();

            $('#advice').validatebox({
                required: false
            });

            $('#advice').val($('#_outerAdvice').val());

            tableSubmit(m, callback, toClose);
        }
    }
    
    function tableSubmit(m, callback, toClose) {
    
    	if($("#disputeMediation_isDetection").val()=='1'){ 
			
			if($("input[name='disputeMediation.mediationOrgName']").val()=='' || $("#disputeMediation_mediationOrgName").val().length==0){
				$.messager.alert('提示','化解组织不可为空！','info');return;
			}
		}
    	
        var isValid =  $("#tableForm").form('validate'),
                advice = $("#advice").val(),
                isAdviceVisible = $("#adviceDiv").is(":visible");
        
        if(isValid) {
        	isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
        }
        
        if(isValid) {
            var type = $("#type").val(),
            	  eventId = $('#eventId').val();

            if(isBlankParam(toClose)){
                toClose = "";
            } else if(toClose == "1") {
                $("#result").val($("#advice").val());
            }
            
            if(isNotBlankStringTrim(eventId)) {
            	m = m.replace('saveEvent', 'editEvent');
            }
            
            //保存涉及人员
            involvedPeopleList();
            
            //各个子模块的相关人员如果有涉及那么需要必填
            
            //构建命案防控人员信息
            var isSaveHomicideCase = $('#isSaveHomicideCase').val();
            if(isSaveHomicideCase == 'true') {
            	if(!fetchHomicidePersonInfo()){
            		$.messager.alert('提示','命案犯罪嫌疑人与命案受害人不能为空！','info');return;
            	}
            }
			//构建涉及线路案事件主要嫌疑人信息
			var isSaveCareRoads = $('#isSaveCareRoads').val();
			if(isSaveCareRoads == 'true') {
				if(!fetchCareRoadsPersonInfo()){
					$.messager.alert('提示','涉及线路案事件主要嫌疑犯不能为空！','info');return;
				}
			}
			//构建矛盾纠纷当事人信息
			var isSaveDisputeMediation = $('#isSaveDisputeMediation').val();
			if(isSaveDisputeMediation == 'true') {
				if(!fetchDisputeMediationPartyInfo()){
					$.messager.alert('提示','矛盾纠纷当事人与化解责任人不能为空！','info');return;
				}
			}
			//构建涉及师生安全案事件主要嫌疑人信息
			var isSaveSchoolRelatedEvents = $('#isSaveSchoolRelatedEvents').val();
			if(isSaveSchoolRelatedEvents == 'true') {
				if(!fetchSchoolRelatedEventsPersonInfo()){
					$.messager.alert('提示','涉及师生安全案事件主要嫌疑犯不能为空！','info');return;
				}
			}

            $("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);

            modleopen();
            $("#tableForm").ajaxSubmit(function(data) {
                if(data.eventId && data.eventId > 0) {
                    var iframeUrl = "${iframeUrl!}",
                            outerCallBack = "${callBack!}";
                            
                    data.eventDetailUrlExtraParam='&isCapEventLabelInfo=true';
                    data.eventDetailHeight=$(window).height();
                    data.eventDetailWidth=$(window).width();
                    
                    if(callback) {//为了保证提交、上报、结案能正常操作
                        eval(callback)(data);
                    } else if(iframeUrl && outerCallBack){//跨域回调
                        if(iframeUrl.indexOf('?') != -1) {
                            iframeUrl += "&";
                        } else {
                            iframeUrl += "?";
                        }

                        data.isCrossDomain = true;
                        iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
                        $("#crossOverIframe").attr("src", iframeUrl);
                    } else if(outerCallBack) {//本域回调
                        eval(outerCallBack)(data);
                    } else {//自身调用回调
                        var msg = "添加";
                        
                        if(isNotBlankStringTrim(eventId)) {
                        	msg = "更新";
                        }
                        
                    <#if (isReport?? && isReport)>
                        msg = "上报";
                    </#if>

                        if(data.result){
                            msg += "成功";
                        }else{
                            msg += "失败";
                        }

                        flashData(msg, data.type);
                    }
                } else {
                    modleclose();
                    $.messager.alert('错误', '保存事件失败，请重试！', 'error');
                }
            });
        }
    }

    function closed(callback) {//事件结案
        var isValid =  $("#tableForm").form('validate');
        
        if(isValid) {
        	isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
        }
        
        if(isValid){
            var eventId = $("#eventId").val();

            if(eventId && eventId != "") {
                modleopen();
                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
                    data: {'eventId' : eventId, 'toClose' : 1, 'advice' : $("#advice").val()},
                    dataType:"json",
                    success: function(data) {
                    	if(callback) {//为了保证提交、上报、结案能正常操作
                    		eval(callback)(data);
                    	} else if(typeof startWorkFlow === 'function') {
                            startWorkFlow(data);
                        } else {
                            $.messager.alert('错误','事件结案失败！','error');
                        }
                    },
                    error:function(data){
                        $.messager.alert('错误','事件结案，连接错误！','error');
                    }
                });
            } else {
                tableSubmit('saveEventAndStart', callback, "1");
            }
        }
    }

    function startWorkFlow(data) {//启动流程
        var formId = data.formId,
                new_workFlowId = data.workflowId,
                wftypeId = data.wftypeId,
                orgCode = data.orgCode,
                orgType = data.orgType,
                toClose = data.toClose,
                advice = data.advice;
        
        if(formId == null || formId < 0) {
        	$.messager.alert('错误', '缺少有效的事件信息！', 'error');
        	return;
        }

        //启动流程
        $.ajax({
            //type: "POST",
            url : '${rc.getContextPath()}/zhsq/workflow/workflowController/startFlow.jhtml',
            data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose, 'advice': advice},
            dataType:"json",
            success: function(data) {
                modleclose();
                if(data.result){
                    var instanceId = data.instanceId;
                    if(isNotBlankString(instanceId)) {
                        var iframeUrl = "${iframeUrl!}",
                                outerCallBack = "${callBack!}";

                        if(iframeUrl && outerCallBack) {//跨域回调
                            if(iframeUrl.indexOf('?') != -1) {
                                iframeUrl += "&";
                            } else {
                                iframeUrl += "?";
                            }

                            data.isCrossDomain = true;
                            data.eventId = formId;

                            iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
                            $("#crossOverIframe").attr("src", iframeUrl);
                        } else {
                        	var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&eventId="+formId+"&cachenum=" + Math.random();
                        	if(outerCallBack) {
                        		if(toClose == '0') {
                        			window.location = url;
                        		}
                        		eval(outerCallBack)(data);
                        	} else {
                        		if(toClose == '0') {
                        			var option = {
                        				title : '事件办理',
                        				targetUrl : url,
                        				resizable : false,
                        				draggable : false,
                        				isAutoWidth : false,
                        				isAutoHeight : false,
                        				onBeforeClose : flashData
                        			};
                        			openJqueryWindowByParams(option);
                        		} else if(toClose == '1') {
                        			try {
                        				closeMaxJqueryWindow();//新增弹出窗口的关闭方法
                        			} catch(e) {}
                        			$.messager.alert('','事件结案成功！','info',function() {
                        				flashData();
                        			});
                        		}
                        	}
                        }
                    }
                } else {
                    var msg = data.msgWrong || "事件启动失败！";

                    try {
                        closeMaxJqueryWindow();//新增弹出窗口的关闭方法
                    } catch(e) {
                    }

                    $.messager.alert('错误',msg,'error');
                }
            },
            error:function(data) {
                $.messager.alert('错误','事件流程未启动成功！','error');
            }
        });
    }
    
    <@block name="function_flashData">
    function flashData(msg, type) {
    	<@block name="function_flashData_body">
        parent.reloadDataForSubPage(msg, type);
        </@block>
    }
    </@block>

    function cancel(){
		var closeCallBack = "${iframeCloseCallBack!}",
			iframeUrl = "${iframeUrl!}";
		
		if(iframeUrl && closeCallBack){
			if(iframeUrl.indexOf('?') != -1){
				iframeUrl += "&";
			}else{
				iframeUrl += "?";
			}
			
			iframeUrl += "callBack=" + closeCallBack;
			$("#crossOverIframe").attr("src", iframeUrl);
		}else{
			parent.closeMaxJqueryWindow();
		}
	}
	
	$(window).resize(function(){
        var winHeight = $(window).height();
        var winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            location.reload();
        }
    });
    
</script>
<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_event_js.ftl" />
<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_homicideCase_js.ftl" />
<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_careRoads_js.ftl" />
<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_disputeMediation_js.ftl" />
<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_schoolRelatedEvents_js.ftl" />
</html>