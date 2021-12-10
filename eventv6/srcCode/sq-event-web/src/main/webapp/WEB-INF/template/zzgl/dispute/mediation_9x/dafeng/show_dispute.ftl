<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>detail01-详情页</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/dispute/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/detail.css"/>
		<script src="${rc.getContextPath()}/dispute/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${rc.getContextPath()}/dispute/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
		<!--新版附件-->
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
		<script>
			var base = '${rc.getContextPath()}';
			var imgDomain = '${imgDomain}';
			var uiDomain = '${uiDomain}';
			var skyDomain = '${skyDomain}';
			var componentsDomain = '${COMPONENTS_DOMAIN}';
		</script>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
		<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<style>
		i.spot-xh {display: inline-block;color: #f54952;padding-right: 5px;/* vertical-align: middle; */}
		.spot-xhs{padding-left: 10px}
		.spot-xhl{padding-left: 20px}
		.det-con2 > li.fr {float: none!important;}
		.parent_div {
			width: 92%;
		}
		.panel-tool .panel-tool-max{display: unset!important;}
		.baseDispute{min-width: 96px;}
		.det-nav-wrap > li > a:hover, .det-nav-wrap > li > .active {background-color: #5294e8!important;}
    	.fw-det-tog-top > h5 > i {background-color:#5294e8!important;}
    	.det-btn-wrap>.det-bg-gray {background-color:#5294e8!important;color: rgb(255, 255, 255)!important}
		.p-right{text-align: right}
		.det-con-tt {min-width: 100px;padding-right: 10px;text-align: right;}
    
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
							<p class="det-con-tt spot-xhs"><i class="spot-xh">*</i>事件名称：</p>
							<p><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">所属区域：</p>
							<p ><#if disputeMediation.gridPath??>${disputeMediation.gridPath}</#if></p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs  "><i class="spot-xh">*</i>发生日期：</p>
							<p><#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">受理日期：</p>
							<p ><#if disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs"><i class="spot-xh">*</i>化解时限：</p>
							<p ><#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if></p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs "><i class="spot-xh">*</i>发生地址：</p>
							<p ><#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" ><i class="spot-xh">*</i>事件类别：</p>
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
							<p class="det-con-tt spot-xhs"><i class="spot-xh">*</i>事件规模：</p>
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
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs  "><i class="spot-xh">*</i>涉及人数：</p>
							<p ><#if disputeMediation.involveNum??>${disputeMediation.involveNum}（人）</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">涉及金额：</p>
							<p ><#if disputeMediation.involvedAmount??>${disputeMediation.involvedAmount?string('0.00')}（元）</#if></p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs"><i class="spot-xh">*</i>影响范围：</p>
							<p><#if scopeInfluence??>${scopeInfluence}</#if></p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs">事件性质：</p>
							<p ><#if eventNature??>${eventNature}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" >风险类型：</p>
							<p >
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
							<p class="det-con-tt spot-xhs">风险等级：</p>
							<p >
								<#if FXDJDM??>
		                  	 		 <#list FXDJDM as l>
										<#if disputeMediation.riskGrade??>
											<#if (l.dictGeneralCode?string==disputeMediation.riskGrade?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs  ">案情评估：</p>
							<p >
								<#if caseAssessment??>
		                  	 		 <#list caseAssessment as l>
										<#if disputeMediation.caseAssessment??>
											<#if (l.dictGeneralCode?string==disputeMediation.caseAssessment?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs">激烈程度：</p>
							<p >
								<#if intenseDegree??>
		                  	 		 <#list intenseDegree as l>
										<#if disputeMediation.intenseDegree??>
											<#if (l.dictGeneralCode?string==disputeMediation.intenseDegree?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" >纠纷等级：</p>
							<p >
								<#if disputeLevel??>
		                  	 		 <#list disputeLevel as l>
										<#if disputeMediation.disputeLevel??>
											<#if (l.dictGeneralCode?string==disputeMediation.disputeLevel?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3 fr">
							<p class="det-con-tt spot-xhs  ">预警等级：</p>
							<p >
								<#if warningLevel??>
		                  	 		 <#list warningLevel as l>
										<#if disputeMediation.warningLevel??>
											<#if (l.dictGeneralCode?string==disputeMediation.warningLevel?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs" ><i class="spot-xh">*</i>矛盾纠纷类型：</p>
							<p>
								<#if disputeType3??>
		                  	 		 <#list disputeType3 as l>
										<#if disputeMediation.disputeType3??>
											<#if (l.dictGeneralCode?string==disputeMediation.disputeType3?string)>${l.dictName}</#if>
										</#if>
									</#list>
                 		 		</#if>
               		 		</p>
						</li>
						<li class="xw-com3">
							<p class="det-con-tt spot-xhs"><i class="spot-xh">*</i>涉及单位：</p>
							<p ><#if disputeMediation.involvedOrgName??>${disputeMediation.involvedOrgName}</#if></p>
						</li>
					</ul>
					<ul class="det-con2 clearfix">
						<li class="xw-com1">
							<p class="det-con-tt spot-xhs" >措施手段：</p>
							<p ><#if disputeMediation.workOrderDetail??>${disputeMediation.workOrderDetail}</#if></p>
						</li>
						<li class="xw-com1">
							<p class="det-con-tt spot-xhs">工作记载：</p>
							<p ><#if disputeMediation.measureDetail??>${disputeMediation.measureDetail}</#if></p>
						</li>
						<li class="xw-com1">
							<p class="det-con-tt spot-xhs" ><i class="spot-xh">*</i>事件简述：</p>
							<p ><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></p>
						</li>
							<li class="xw-com1">
								<p class="det-con-tt spot-xhs" ><i class="spot-xh">*</i>附件详情：</p>
								<div id="bigupload_1" class="parent_div"></div>
							</li> 
					</ul>
				</div>
				
				<div class="det-section" >
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
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解方式：</p>
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
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>最后调解日期：</p>
								<p><#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if></p>
							</li>
							<li class="xw-com3 fr">
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解责任人姓名：</p>
								<p><#if disputeMediation.mediator??>${disputeMediation.mediator}</#if></p>
							</li>
						</ul>
						<ul class="det-con2 clearfix">
							<li class="xw-com3">
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解责任人电话：</p>
								<p><#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if></p>
							</li>
							<li class="xw-com3">
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解组织：</p>
								<p><#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if></p>
							</li>
							<li class="xw-com3 fr">
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解是否成功：</p>
								<p><#if disputeMediation.isSuccess??><#if disputeMediation.isSuccess=="1">是<#else>否</#if></#if></p>
							</li>
							<li class="xw-com3" style="width:100%">
								<p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh">*</i>化解情况：</p>
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
									<p class="det-con-tt spot-xhs">考评日期：</p>
									<p><#if disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if></p>
								</li>
								<li class="xw-com1">
									<p class="det-con-tt spot-xhs">考评意见：</p>
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
				//新版附件
				uploadFile();
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
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>姓名：</p>'+
																	'<p>'+data.involvedPeople.name+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt spot-xhs p-right" style="width: 81px"><i class="spot-xh">*</i>性别：</p>'+
																	'<p>'+data.involvedPeople.sexName+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>证件类型：</p>'+
																	'<p>'+data.involvedPeople.cardTypeName+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>证件号码：</p>'+
																	'<p>'+data.involvedPeople.idCard+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt spot-xhs p-right" style="width: 81px"><i class="spot-xh">*</i>民族：</p>'+
																	'<p>'+data.involvedPeople.nationName+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>人员类别：</p>'+
																	'<p>'+data.involvedPeople.peopleTypeName+'</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">&nbsp;</i>学历：</p>'+
																	'<p>';
																	if(data.involvedPeople.eduName){
																		main_people_html+=data.involvedPeople.eduName;
																	}
																main_people_html +='</p>'+
																'</li>'+
																'<li class="xw-com3">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>当事人类型：</p>';
																	if(data.involvedPeople.involvedPeopleType == '1'){
																		main_people_html += '<p>甲方</p>';
																	}else if(data.involvedPeople.involvedPeopleType == '2'){
																		main_people_html +=	'<p>乙方</p>';
																	}
																	main_people_html += '</li>'+
																'<li class="xw-com3 fr" style="float: none;">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>居住详址：</p>'+
																	'<p>'+data.involvedPeople.homeAddr+'</p>'+
																'</li>'+
																'<li class="xw-com3 fr" style="float: none;">'+
																	'<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">*</i>联系电话：</p>'+
																	'<p>'+(data.involvedPeople.tel==null?"暂无":data.involvedPeople.tel)+'</p>'+
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

			//新版附件上传
			function uploadFile() {

				var bigupload = $("#bigupload_1").bigfileUpload({
					useType: 'view',////附件上传的使用类型，edit,view，（默认edit）;$(f
					chunkSize : 5,//切片的大小（默认5M）
					fileNumLimit : 10,//最大上传的文件数量（默认9）
					maxSingleFileSize:100,//单个文件最大值（默认300）,单位M
					fileExt : '.doc,.docx,.ppt,.pptx,.txt,.xls,.xlsx,.pdf,.jpg,.text,.png,',//支持上传的文件后缀名(默认开放：.bmp,.pdf,.jpg,.text,.png,.gif,.doc,.xls,.docx,.xlsx,.ppt,.pptx,.mp3,.wav,.MIDI,.m4a,.WMA,.wma,.mp4,)
					//initFileArr : attarr_1,////初始化的附件对象数组(默认为{})
					attachmentData:{bizId:'${disputeMediation.mediationId!}',attachmentType:'dispute_attachment_type'},
					appcode:"testAPP",//文件所属的应用代码（默认值components）
					module:"testModule",//文件所属的模块代码（默认值bigfile）
					imgDomain : imgDomain,//图片服务器域名
					componentsDomain : componentsDomain,//图片服务器域名
					//serverUrl : 'http://zzh.v6.aishequ.org:9611/partFile/uploadPartFile.jhtml',
					isSaveDB : false,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
					isUseLabel: false,//是否开启附件便签功能
					isDelDbData : false,
					isDelDiscData : false,
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
