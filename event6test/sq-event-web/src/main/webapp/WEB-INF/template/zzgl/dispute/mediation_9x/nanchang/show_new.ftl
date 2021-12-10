<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>detail01-详情页</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/dispute/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/detail.css"/>
		<script type="text/javascript">
			var base = "${rc.getContextPath()}",imgDomain = '${imgDomain}',fileDomain = '${fileDomain}';
		</script>
	</head>
	<style>
		i.spot-xh {display: inline-block;color: #f54952;padding-right: 5px;/* vertical-align: middle; */}
		.spot-xhs{padding-left: 10px}
	</style>
	<body class="xiu-body">
		<div class="container-detail flex">
			<div class="det-nav">
				<ul class="det-nav-wrap">
					<li>
						<a href="#1" class="active flex flex-ac flex-jc"><i class="det-dis-none"></i>纠纷基本信息</a><!--要使用图标i时，把det-dis-none类去掉-->
					</li>
					<li>
						<a href="#2" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>主要当事人信息</a>
					</li>
					<li>
						<a href="#3" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>化解信息</a>
					</li>
					<li>
						<a href="#4" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>考评信息</a>
					</li>
				</ul>
			</div>
			<input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
        	<input type="hidden" id="gridId" name="gridId" value="<#if disputeMediation.gridId??>${disputeMediation.gridId}</#if>">
			<div class="det-wrapper flex1">
				<div class="fw-det-tog-top mlr20" id="1">
					<h5><i></i>基本信息</h5>
					<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_down.gif"> </a>
				</div>
				<div class="fw-det-toggle">
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt" style="margin-left:5%"><i class="spot-xh">*</i>事件名称:</p>
							<p><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">所属区域:</p>
							<p><#if disputeMediation.gridPath??>${disputeMediation.gridPath}</#if></p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs">发生日期:</p>
							<p><#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" style="margin-left:5%">受理日期:</p>
							<p><#if disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">调解时限:</p>
							<p><span><#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if></span></p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt"><i class="spot-xh">*</i>发生地址:</p>
							<p><#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt" style="margin-left:5%"><i class="spot-xh">*</i>事件类别:</p>
							<p><#if disputeType_9x??>
	                  	 		 	<#list disputeType_9x as l>
										<#if disputeMediation.disputeType2??>
											<#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
										</#if>
									</#list>
	                 		 	</#if>
	                 	    </p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt"><i class="spot-xh">*</i>事件规模:</p>
							<p>
								<#if disputeScaleDC??>
		                  	 		 <#list disputeScaleDC as l>
										<#if disputeMediation.disputeScale??>
											<#if (l.dictGeneralCode?string==disputeMediation.disputeScale?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
							</p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">涉及人数:</p>
							<p><#if disputeMediation.involveNum??>${disputeMediation.involveNum}（人）</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" style="margin-left:5%">涉及金额:</p>
							<p><#if disputeMediation.involvedAmount??>${disputeMediation.involvedAmount?string('0.00')}（元）</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt"><i class="spot-xh">*</i>影响范围:</p>
							<p><#if scopeInfluence??>${scopeInfluence}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">事件性质:</p>
							<p><#if eventNature??>${eventNature}</#if></p>
						</li>
					</ul>
					
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" style="margin-left:5%">风险类型:</p>
							<p>
								<#if FXLXDM??>
		                  	 		 <#list FXLXDM as l>
										<#if disputeMediation.riskCode??>
											<#if (l.dictGeneralCode?string==disputeMediation.riskCode?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">风险等级:</p>
							<p>
								<#if FXDJDM??>
		                  	 		 <#list FXDJDM as l>
										<#if disputeMediation.riskGrade??>
											<#if (l.dictGeneralCode?string==disputeMediation.riskGrade?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt"><i class="spot-xh">*</i>涉及单位:</p>
							<p><#if disputeMediation.involvedOrgName??>${disputeMediation.involvedOrgName}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com1">
							<p class="det-con-tt" style="margin-left:1.5%;">措施手段_详细情况:</p>
							<p><#if disputeMediation.workOrderDetail??>${disputeMediation.workOrderDetail}</#if></p>
						</li>
						<li class="xw-com1">
							<p class="det-con-tt" style="margin-left:1.5%;">工作记载_详细情况:</p>
							<p><#if disputeMediation.measureDetail??>${disputeMediation.measureDetail}</#if></p>
						</li>
						
						<li class="xw-com1">
							<p class="det-con-tt" style="margin-left:1.5%"><i class="spot-xh">*</i>事件简述:</p>
							<p><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></p>
						</li>
							<li class="xw-com1">
								<p class="det-con-tt spot-xhs" style="margin-left:1.5%;margin-top: 1.5%;">附件详情:</p>
								<div class="xw-com1 pic-upload mt10">
									<div id="attas" class="flex mt15 flex-wrap" >
										<#if attList?size &gt; 0>
											<#list attList as att>
							    				<#if att.title == 'image'>
												<div class="pic pic_content" id="pic_${att_index}">
													<span title="点击预览该图片" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
														<img title="${att.fileName}" style="width:56px;height:36px;" src="${imgDomain}${att.filePath}" />
														<p>${att.fileName}</p>
													</span>
													<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
												</div>
							    				<#elseif att.title == 'excel'>
							    				<div class="pic pic_content" id="pic_${att_index}">
													<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
														<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_excel.png" />
														<p>${att.fileName}</p>
													</span>
													<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
												</div>
												<#elseif att.title == 'word'>
							    				<div class="pic pic_content" id="pic_${att_index}">
													<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
														<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_word.png" />
														<p>${att.fileName}</p>
													</span>
													<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
												</div>
												<#elseif att.title == 'ppt'>
							    				<div class="pic pic_content" id="pic_${att_index}">
													<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
														<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_ppt.png" />
														<p>${att.fileName}</p>
													</span>
													<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
												</div>
												<#else>
							    				<div class="pic pic_content" id="pic_${att_index}">
													<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
														<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_word.png" />
														<p>${att.fileName}</p>
													</span>
													<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
												</div>
												</#if>
						    				</#list>
				    					<#else>
				    						<p class="det-con-tt" style="color:#666666;margin-left:-0.5%;margin-top:-0.2%;font-size: 12px;">暂无附件</p>
										</#if>
									</div>
								</div>
							</li> 
					</ul>
				</div>
				
				<div class="det-section" style="margin-top: 10px;">
					<div class="fw-det-tog-top" id="2">
						<h5><i></i>主要当事人信息</h5>
						<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
					</div>
					<div class="fw-det-toggle">
						<div class="det-box mt10" id="main_people" style="margin-left:45%;color: #f08750;font-size:18px;">
						</div>
					</div>
				</div>
				<div class="det-section">	
					<div class="fw-det-tog-top" id="3">
						<h5><i></i>化解信息</h5>
						<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
					</div>
					<div class="fw-det-toggle">
						<ul class="det-con2 clearfix">
							<li class="xw-com3">
								<p class="det-con-tt">化解方式：</p>
								<p>
									<#if mediationTypeDC_9x??>
			                  	 		 <#list mediationTypeDC_9x as l>
											<#if disputeMediation.mediationType??>
												<#if (l.dictGeneralCode?string==disputeMediation.mediationType?string)>${l.dictName}</#if>
											</#if>
										 </#list>
		                 		 	</#if>
                 		 		</p>
							</li>
							<li class="xw-com3">
								<p class="det-con-tt">化解日期：</p>
								<p><#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if></p>
							</li>
							<li class="xw-com3 fr">
								<p class="det-con-tt">化解责任人姓名：</p>
								<p><#if disputeMediation.mediator??>${disputeMediation.mediator}</#if></p>
							</li>
						</ul>
						<ul class="det-con2 clearfix">
							<li class="xw-com3">
								<p class="det-con-tt">化解责任人联系方式：</p>
								<p><#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if></p>
							</li>
							<li class="xw-com3">
								<p class="det-con-tt">化解组织：</p>
								<p><#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if></p>
							</li>
							<li class="xw-com3 fr">
								<p class="det-con-tt">化解是否成功：</p>
								<p><#if disputeMediation.isSuccess??><#if disputeMediation.isSuccess=="1">成功<#else>失败</#if></#if></p>
							</li>
							<li class="xw-com3">
								<p class="det-con-tt">化解情况：</p>
								<p><#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if></p>
							</li>
						</ul>
					</div>
				</div>
					<div class="det-section">	
						<div class="fw-det-tog-top" id="4">
							<h5><i></i>考评信息</h5>
							<a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
						</div>
						<div class="fw-det-toggle">
							<ul class="det-con2 clearfix">
								<li class="xw-com1">
									<p class="det-con-tt">考评日期：</p>
									<p><#if disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if></p>
								</li>
								<li class="xw-com1">
									<p class="det-con-tt">考评意见：</p>
									<p><#if disputeMediation.evaOpn??>${disputeMediation.evaOpn}</#if></p>
								</li>
							</ul>
						</div>
					
						<div class="det-btn-wrap mt25 mb10">
							<a href="javascript:void(0);" class="det-btn det-bg-gray" onclick="javascript:cancl();">关闭</a>
						</div>
				</div>
			</div>
		</div><!--container-fluid-->
		<script src="${rc.getContextPath()}/dispute/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${rc.getContextPath()}/dispute/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
		<script>
			var swiper;
			$(window).on('load resize', function(){
				setTimeout(function(){
					$('.det-ann-pic').width($('.det-section').width() * 0.3);
					swiper = new Swiper('.swiper-container', {
						allowTouchMove: false,
						navigation: {
							nextEl: '.swiper-button-next',
							prevEl: '.swiper-button-prev',
						},
					});
				}, 200);
			});
			$(function() {
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

				$('.det-nav-wrap').on('click', 'li a', function(){
					$(this).addClass('active').parent().siblings().children().removeClass('active');
				});
				//附件关闭按钮
				$('.pic_content').each(function(index) {
					$(this).hover(function() {
						$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
					}, function() {
						$('.pic_content').eq(index).find('.off_btn').addClass('dn');
					});
				});
		    });
		</script>
		<script src="${rc.getContextPath()}/dispute/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
		<script>
            $(function () {
            	//获取人员信息
            	<#if involvedPeoples??>
	        		<#list involvedPeoples as l>
	                    detailMainPeople("${l.ipId!''}");
	        		</#list>
        		</#if>
            	
                $(".fw-container-detail,.det-wrapper, .layer_aj_bt_s").niceScroll({
                    cursorcolor:"rgba(0, 0, 0, 0.3)",
                    cursoropacitymax:1,
                    touchbehavior:false,
                    cursorwidth:"4px",
                    cursorborder:"0",
                    cursorborderradius:"4px",
                //  autohidemode: false //隐藏式滚动条
                });
            });
            
            function detailMainPeople(ipId){
                if(ipId!=null&&ipId!=""){
                	$.ajax({
        			 	type: "POST",
        			 	url: '${rc.getContextPath()}/zhsq/involvedPeople/detail.jhtml?ipId='+ipId+'',
        			 	dataType:'json',
        			 	success: function(data){
		        			 		var main_people_html = '<div style="border:1px #e5e5e5 solid;margin-top:5px"><ul class="det-con2 clearfix" style="margin-top:15px">'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>姓名:</p>'+
																	'<p>'+data.involvedPeople.name+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>性别:</p>'+
																	'<p>'+data.involvedPeople.sexName+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>证件类型:</p>'+
																	'<p>'+data.involvedPeople.cardTypeName+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>证件号码:</p>'+
																	'<p>'+data.involvedPeople.idCard+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>民族:</p>'+
																	'<p>'+data.involvedPeople.nationName+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>人员类别:</p>'+
																	'<p>'+data.involvedPeople.peopleTypeName+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>学历:</p>'+
																	'<p>'+data.involvedPeople.eduName+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr">'+
																	'<p class="det-con-tt"><i class="spot-xh">*</i>居住详址:</p>'+
																	'<p>'+data.involvedPeople.homeAddr+'</p>'+
																'</li>'+
														'</ul></div>';
									$('#main_people').before(main_people_html);
        			 		}
        			 	});
                }
            }
            
            function viewOrDownLoad(fileUrl,type) {
            	if (type && type != 'img') {
            		fileUrl = base + '/file/downloadFile.jhtml?path='+fileUrl;
            	}
            	window.open(fileUrl);
            }
            
            function cancl(){
		        parent.closeMaxJqueryWindow();
		    }
            
        	//文件下载
        	function downLoadFile(filePath,fileName) {
        		var attaId ='';
        		//fileName = stripscript(fileName);
        		$('#attFileUrl').val(filePath);
        		$('#attName').val(fileName);
        		$('form#attForm').submit();
        	}
        </script>
	</body>
</html>
