<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>扫黑除恶-线索管理 新增/编辑</title>
		<!-- 扫黑除恶样式 -->
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
		<#include "/component/commonFiles-1.1.ftl" />
		
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
		
		<!-- 扫黑除恶js -->
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/jquery.nicescroll.js" charset="utf-8"></script>
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/main-shce.js" charset="utf-8"></script>
		
	</head>
<#include "/component/TeamSelector.ftl" />
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh" style="padding-top:0px"><!-- 外框 -->
				
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent">
					<!-- 团伙信息 -->
					<div class="fw-det-tog fw-main-det">
						<div class="fw-det-tog-top">
							<h5><i></i>团伙信息</h5>
							<a href="###"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/></a>
						</div>
						<div class="fw-det-toggle">
							<ul class="fw-xw-from clearfix">
								<li class="xw-com2">
									<span class="fw-from1">所属区域：</span>
									<p class="from flex1">${bo.gridPath!}</p>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">团伙名称：</span>
									<p class="from flex1">${bo.gangName!}</p>
								</li>
				
								<li class="xw-com1">
									<span class="fw-from1">主要活动地带:</span>
									<p class="from flex1">${bo.activityZone!}</p>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">团伙涉黑情况:</span>
									<p class="from flex1">${bo.situation!}</p>
								</li>
								<li class="xw-com1">
									<span class="fw-from1">打击状况：</span>
									<p class="from flex1"><#if bo.hitStatus?? && bo.hitStatus=='1' >已扫除<#else>扫除中</#if></p>
								</li>
								
							</ul>
						</div>
					</div>
					<#if members?? && (members?size > 0)>
					<!-- 成员信息 -->
					<div class="fw-det-tog fw-main-det">
						<div class="fw-det-tog-top">
							<h5><i></i>成员信息</h5>
							<a href="###"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/></a>
						</div>
						<div id="memberInfoDiv" class="fw-det-toggle">
							<div class="repor-head">
								<ul id="memberUl" class="headlist flex1 clearfix" style="margin-left: 30px;">
									<#if members?? && (members?size > 0)>
										<#list members as item> 
											<li id="memberLi_${item_index}" contentId="memberInfoDiv_${item_index}" onclick="selectMemberInfo('memberInfoDiv_${item_index}')"  >
												<a class="hd-box <#if (item_index==0) > bd-on </#if>" title='${item.name}'></a>    
											</li>
										</#list>	
									</#if>
								</ul>
							</div>
								<#list members as item> 
								<div id="memberInfoDiv_${item_index}" class="report-pep xw-com1 <#if (item_index!=0) >hide</#if>" index="${item_index}">
									<ul class="fw-xw-from clearfix ">
										<li class="xw-com2">
											<span class="fw-from1">姓名：</span>
											<input type="text" class="from flex1" value="${item.name!}" readonly />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">年龄：</span>
											<input type="text" class="from flex1" value="${item.age!}" readonly />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">职业：</span>
											<input type="text" class="from flex1" value="${item.profession!}" readonly />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">身份证号：</span>
											<input type="text" class="from flex1" value="${item.idCard!}" readonly />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">是否骨干：</span>
											<input type="text" class="from flex1" value="<#if item.isSkeletonStaff?? && item.isSkeletonStaff=='1' >是<#else>否</#if> " readonly />
										</li>
										<li class="xw-com1">
											<span class="fw-from1">家庭住址：</span>
											<p class="from flex1">${item.homeAddr!}</p>
										</li>
									</ul>
								</div>
								</#list>
						</div>
						 <div class="mt20">
							<ul class="fw-xw-from clearfix" style="border-bottom: 1px dashed #ccc; padding-bottom: 20px;">
								<li class="xw-com1">
									<span class="fw-from1">备注：</span>
									<p class="from flex1">${bo.gangRemark!}</p>
								</li>
							</ul>
						 </div>
					</div>
					</#if>
					
				</div>
			
			</div>
		</div>
		
		<#include "/component/ComboBox.ftl" />
	</body>
	
	<script type="text/javascript">
		var basWorkSubTaskCallback = null;//存放原有的提交方法
			
		$(function () {
			
			$('.fw-det-tog-top > a').parent()
									.css('cursor', 'pointer')
									.on('click', function() {
										$(this).siblings('.fw-det-toggle').toggle(300);
									});
			$('.fw-tab-min').on('click', 'li a', function() {
				$('.fw-tab-min > li > a').removeClass('active');
				
				$(this).addClass('active');
				
				$('#formDiv div.tabContent').hide();
				$('#' + $(this).attr('divId')).show();
				
			});
				
			
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
		function selectMemberInfo(contentId) {
			var memberObj = $('#memberUl li[contentId='+ contentId +']');
			if(memberObj.length > 0) {//删除操作时，由于冒泡会触发一次父级的onclick事件
				var selectedObj = $('#memberUl a.bd-on'),
					selectedContentId = selectedObj.parent().attr('contentId');
				
				selectedObj.attr('title' , $('#' + selectedContentId + ' input[id=name]').val());
				
				$('#memberUl a.hd-box').removeClass('bd-on');
				memberObj.children('a.hd-box').addClass('bd-on');
				$('#memberInfoDiv div[id^=memberInfoDiv_]').hide();
				
				$('#memberUl li[contentId='+ contentId +']').show();
				$('#' + contentId).show();
			}
		}


	</script>
</html>