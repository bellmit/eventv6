<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="eventDetailPageTitle">事件草稿详情-晋江</@block></title>
	
	<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/add.css" />
	
	<!-- 命案防控-->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
	
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	<#include "/component/bigFileUpload.ftl" />
	
	<style type="text/css">
		.WindowLeftMenu .list {margin-left: 0; width: 150px;}
		.WindowLeftMenu .list dd a {text-decoration : none;}
		#involvedPeopleList td{border-bottom: none;}
		.fw-main ul li{
			clear: unset;
		}
		.leftTdWidth{
			width:50%;
		}
	</style>
</head>

<body>
	<div class="OpenWindow">
		<!--left menu begin-->
		<div class="WindowLeftMenu fl hide" id="leftMenuDiv" style="padding:0;">
			<div class="list">
				<dl>
					<dd id="leftMenuDD">
						<p class="current finish" labelTypeName="eventBasicInfo"><a href="#eventBasicInfo">基本信息</a></p>
						<#if careRoads??>
							<p labelTypeName="careRoads"><a href="#careRoads">涉及线路案(事)件</a></p>
						</#if>
						<#if majorRelatedEvents??>
							<p labelTypeName="majorRelatedEvents"><a href="#majorRelatedEvents">重特大案(事)件</a></p>
						</#if>
						<#if homicideCase??>
							<p labelTypeName="homicideCase"><a href="#homicideCase">命案防控</a></p>
						</#if>
						<#if disputeMediation??>
							<p labelTypeName="disputeMediation"><a href="#disputeMediation">矛盾纠纷排查化解</a></p>
						</#if>
						<#if schoolRelatedEvents??>
							<p labelTypeName="schoolRelatedEvents"><a href="#schoolRelatedEvents">涉及师生(事)件</a></p>
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
							<div id="content-d">
								<div id="eventLabelContentIncludeDiv">
									<!--基本信息-->
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_basic.ftl" />
									<!--涉及线路案(事)件-->
									<#if careRoads??>
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_careRoads.ftl" />
									</#if>
									<!--重特大案(事)件-->
									<#if majorRelatedEvents??>
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_majorRelatedEvents.ftl" />
									</#if>
									<!--命案防控-->
									<#if homicideCase??>
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_homicideCase.ftl" />
									</#if>
									<!--矛盾纠纷排查化解-->
									<#if disputeMediation??>
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_disputeMediation.ftl" />
									</#if>
									<!--涉及师生(事)件-->
									<#if schoolRelatedEvents??>
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_schoolRelatedEvents.ftl" />
									</#if>
								</div>
							</div><!--content-d end-->
							
							<!--操作按钮 开始-->
							<div id="btnListDiv" class="BigTool">
								<div class="BtnList">
									<a href="###" class="BigNorToolBtn BigShangBaoBtn" onclick="startWorkFlow(0);">提交</a>
									<!--
									<a href="###" class="BigNorToolBtn BigJieAnBtn" onclick="startWorkFlow(1);">结案</a>
									-->
									<a href="###" class="BigNorToolBtn CancelBtn" onclick="cancel();">关闭</a>
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
        
		var contentDivHeight = _winHeight - $('#btnListDiv').outerHeight(true),
			  leftMenuDivWidth = 0;
        
        leftMenuDivWidth=initLabelLeft(leftMenuDivWidth);
        
		$('#leftMenuDiv').height(_winHeight);
		$('#rightContentDiv').height(contentDivHeight)
										.width(_winWidth - leftMenuDivWidth)
										.css('left', leftMenuDivWidth);
		$('#content-d').height(contentDivHeight);
		
		//为滚动条滚动添加动画效果
		$('#leftMenuDD > p > a').on('click', function() {
			$("#content-d").animate({scrollTop: $(this.hash)[0].offsetTop}, 400);
		});
		
        //页面滚动条设置
		$("#content-d").niceScroll({
			cursorcolor:"rgba(0, 0, 0, 0.3)",
			cursoropacitymax:1,
			touchbehavior:false,
			cursorwidth:"4px",
			cursorborder:"0",
			cursorborderradius:"4px"
		});
		
		initItem4Basic();
		
		<#if homicideCase??>
			initItem4HomicideCase();
		</#if>
		<#if disputeMediation??>
			initItem4DisputeMediation();
		</#if>
	});
	
	function initItem4Basic() {
        var bigFileUploadOpt = {
        	useType: 'view',
        	fileExt: '.jpg,.gif,.png,.jpeg,.webp,.zip,.7z,.txt,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.rar',
        	attachmentData: {bizId: $('#eventId').val(), attachmentType:'${EVENT_ATTACHMENT_TYPE!}', eventSeq: '1,2,3'},
        	individualOpt : {
        		isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
        	}
        };
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        
        $("#advice").width($('#content').outerWidth(true));
	}
	
	function startWorkFlow(toClose) {
		var isValid =  $("#tableForm").form('validate');
		
		if(isValid) {
			modleopen();
			
			$('#toClose').val(toClose);
			
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml");

            $("#tableForm").ajaxSubmit(function(data) {
            	if(data){
            		data.eventDetailUrlExtraParam='&isCapEventLabelInfo=true';
                    data.eventDetailHeight=$(window).height();
                    data.eventDetailWidth=$(window).width();
            	}
            	parent.startWorkFlow(data, '0');
            });
    	}
	}
    
    function flashData() {
		parent.closeMaxJqueryWindow();
		parent.flashData();
	}
    
    function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function showDetail(){
    <#if isDomain??>
        var isDomain = "${isDomain}";
    </#if>
        var url;
    <#if bizDetailUrl??>
        url = '${bizDetailUrl}';
        if(isDomain){
            url += "&isDomain=" + isDomain;
        }
    </#if>
    <#if source?? && source = 'workPlatform'>
        url += "&source=${source}";
        parent.parent.top.topDialog.openDialog("查看事件信息", 400, 900, url)
    <#elseif source?? && source = 'oldWorkPlatform'>
        url += "&source=${source}";
        showMaxJqueryWindow("查看详情", url,880,400,'no');
    <#else>
        parent.showMaxJqueryWindow("查看详情", url,900,400,'no');
    </#if>

    }

    function showMap(){
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
        var width = 480;
        var height = 360;
        var gridId = $("#gridId").val();
        var markerOperation = $('#markerOperation').val();
        var id = $('#eventId').val();
        var mapType = 'EVENT_V1';
        var isEdit = false;
        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }
    
	$(window).resize(function(){
        var winHeight = $(window).height();
        var winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            location.reload();
        }
    });
    
</script>
<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_event_js.ftl" />
<#if homicideCase??>
	<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_homicideCase_js.ftl" />
</#if>
<#if disputeMediation??>
	<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_disputeMediation_js.ftl" />
</#if>

</html>