<!DOCTYPE html>
<html style="overflow:hidden">
	<head>
	    <meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	    <title>事件列表详情——南安</title>
	    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/JiangYinPlatform/css/mask.css"/>
	    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/normal.css" />
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css" />
    	<link rel="stylesheet" href="${uiDomain}/web-assets/common/css/reset.css">
    	<link rel="stylesheet" href="${uiDomain}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
	    
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/css/xxbh-mask.css">
	    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
		<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/paging/paging.js"></script>
		<link rel="stylesheet" href="${uiDomain!''}/js/paging/paging.css">
		<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
		
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	
		<!--<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>-->
		<script type="text/javascript" src="${rc.getContextPath()}/js/workflow/easyui-utils.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
		<#include "/component/bigFileUpload.ftl" />
		
		
		
		<script type="text/javascript">
			var winW, scale,rem;
			$(window).on('load resize', function () {
				fullPage();
				function fullPage() {//将页面等比缩放
					winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>
					if (winW < 1000) {
						winW = 1000;
					}
					var whdef =  100 / 1920;
					rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
					$('html').css('font-size', rem + "px");
					line();
					$('.mt-tbody').getNiceScroll().resize();
				}
			});
		</script>
		<style>
		body,html{background:rgba(0,0,0,0)}
		.mt-tbody {max-height: 4.5rem;}
		.mask-c-bottom {margin-top: .5rem;}
		.FromTo{margin-top:-0.05rem}
		.TimeControl{font-size:12px;}
		.mf-item>input {background-color: rgba(29, 32, 136, .1) ! important;color: white ! important}
		.ztree{background-color: rgba(7, 44, 86, .9) !important;}
		.ztree *{color:white!important;}
		.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
		.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
		.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
		.swiper-pagination {height: 0.12rem;bottom: .1rem !important;}
		.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
		.aj_items_t_yellow {text-align: left;}
		.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
		.mCSB_inside > .mCSB_container{margin-right:.1rem}
		body,html{width:100%;height:100%;}
		.select-table .select-td {
    		border-bottom: 1px solid #39BBF8;
    		border-right: 1px solid #39BBF8;
    		background-color: #F3F8FE;
		}
		#leftPanel>.ztree * {
    		margin: 0;
    		font-size: 12px;
    		font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    		color: #000!important;
    		background-color: white !important;
		}
		
		#leftTree{
			background-color: white !important;
		}
		
		.BtnList{
			margin-left:3rem;
		}
		
		.layout-panel-center{
			height:3.2rem;
		}	
		
		.panel-body-noborder{
			height:3.2rem;
		}	
		
		.layout-panel-south{
			height:0.6rem;
		}
		
		#workflowBtn{
			margin-left:4rem;
		}
		
		.bigFile-upload-box .zt-pxt>span{
			font-size:12px;
			margin-top:.06rem;
			color:white;
		}
		
		.list-file-name{
			color: #999;
		}
		
		.layui-layer-btn{
			font-size:12px;
		}
		
		.window-shadow{
			height:100px;
		}
		
		.upload-div2{
			background: #fafafa;
		}
		
		.filebox{
			background: none !important;
		}
		
		.bigFile-upload-box .cl-tb{
			color:white;
		}
		
		.list-file-name{
			color:white;
		}
		
		.bigFile-upload-box{
			height:2.4rem;
		}
		
		.window-body{
			height:1rem;
		}
		
		.window-shadow{
			display:none ! important;
		}
		
		.div-text{
	        position: absolute;
	        top:0;
	        left:0;
	        width: 56px;
	        height: 10px;
	        background: rgba(0,0,0,0.5);
	        padding: 10px;
	        box-sizing: border-box;
	        border-radius: 2px;
	        z-index: 20;
    	}
    	.div-text>p{
        	font-size: 12px;
        	color: #fff;
        	margin-top: -.11rem;
    	}
    	.mdt-item {
		    margin-top: .09rem;
		}

		.span-grid-admin{
			text-decoration:underline;
		}
		
		.mcb-r-btn {
	    	bottom: .05rem;
	    }
	    
	    .mbr20 {
		    margin-bottom: 0rem;
		}
		.mlr30 {
		    margin-left: 0rem;
		}
		.mrr30 {
		    margin-right: 0rem;
		}
		.tab-div {
		    font-size: 0;   
		}
		.tab-div>a.active {
		    background-image: linear-gradient( 
		0deg
		 , rgba(0, 168, 255, 0.8) 0%, rgba(0, 168, 255, 1) 50%, rgba(0, 168, 255, 0.8) 100%);
		    background-blend-mode: normal, normal;
		}
		.tab-div>a {
		    width: 1.2rem;
		    display: inline-block;
		    height: 0.3rem;
		    line-height: 0.30rem;
		    background-image: linear-gradient( 
		0deg
		 , rgba(0, 168, 255, 0.5) 0%, rgba(0, 168, 255, 0.29) 50%, rgba(0, 168, 255, 0.08) 100%);
		    font-size: .15rem;
		    font-weight: normal;
		    font-stretch: normal;
		    letter-spacing: 1px;
		    color: #feffff;
		    text-align: center;
		    transform: -webkit-skewX(-15deg);
		    transform: skewX( 
		-15deg
		 );
		    cursor: pointer;
		}
		.hide{
		 display: none;
		}
		.show {
		    display: block !important;
		}
		.dubanIcon{
		  top:20%;
		}
		</style>
	</head>
	<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
	<body style="background-color: transparent;">
		<div class="event-mask" id="eventAttr" style="width: 10rem;height: 6rem;margin-top:-3rem;left:53%;z-index:55;display:none">
            <div class="mask-main-top" style="height:0rem;">
                <i class="close" id="eventAddClose"></i>
            </div>
            <div class="mask-main-bottom" style="height:100%;">
                <iframe id="mask-eventAttr" frameborder="0" width="100%" height="100%" style="overflow:hidden"></iframe>
            </div>
        </div>
		<div class="container-fluid">
			<div class="wrap_container">
				<div class="mask-title flex flex-jc mtr30">
					<p>事件详情</p>
				</div>
				<div class="mask-content mask-content1" style="display: block;">
					<div class="mc1-content" style="display: block;">
						<div class="mc-text flex" style="display:none">
							<div class="mct-item clearfix bs">
								<div class="clearfix fl mlr10">
									<p>满意率：</p>
									<p class="variableList" id="satisfiedRate"></p>
								</div>
							</div>
							<div class="mct-item mct-item1 clearfix flex1 bs mlr15" id="loadOvertimeListDiv" onclick="loadOvertimeList()">
								<p>超时数：</p>
								<p>共<span class="variableList" id="overtimeEventCount"></span>条</p>
							</div>
							<div class="mct-item mct-item1 clearfix flex1 bs mlr15" id="loadSuperviseListDiv" onclick="loadSuperviseList()">
								<p>督办数：</p>
								<p>共<span class="variableList" id="superviseEventCount"></span>条</p>
							</div>
						</div>
						<div class="mask-filter clearfix">
							<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
							<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart"></input>
	                		<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd"></input>
							<div class="mf-item mf-item2 clearfix fl">
								<p>所属网格：</p>
								<input class="inp1 InpDisable maet-ti-select" style="color:white;width:100px; background-color: rgba(29, 32, 136, .1);box-shadow: inset 0 0 .08rem rgba(0, 176, 255, .4);border: solid .01rem rgba(0, 176, 255, 0.5);" id="gridName" name="gridName" type="text"/></p>
							</div>
							<div class="mf-item mf-item3 clearfix fl">
								<p>日期：</p>
								<input type="text" id="_createTimeDateRender" class="inp1 InpDisable maet-ti-select" style="color:white;width:2.8rem;font-size:.14rem" value="${createTimeStart!}<#if createTimeStart?? && createTimeEnd??> ~ </#if>${createTimeEnd!}"/></p>
							</div>
							<div class="mf-item clearfix fl">
								<p>关键字：</p>
								<input type="text" id="keyRemarkWord" placeholder="事件描述/标题/事发详址/补充描述" class="bs" style="width: 4.0rem">
							</div>
							<div class="mf-btn clar fr">
								<a onclick="searchEventList()" href="javascript:void(0);" class="mfb-search fl clearfix">
									<i></i>
									<p>查询</p>
								</a>
								<a onclick="resetEventList()" href="javascript:void(0);" class="mfb-reset fr clearfix">
									<i></i>
									<p>重置</p>
								</a>
							</div>
						</div>
						<div class="mask-table">
							<div class="mask-table-content" style="display: block;">
								<div class="mt-thead bs">
									<table>
										<colgroup>
											<col style="width: auto;">
											<col style="width: 1.2rem;">
											<col style="width: 2rem;">
											<col style="width: 3rem;">
											<col style="width: 2.4rem;">
											<col style="width: 1.2rem;">
											<col style="width: 1.2rem;">
										</colgroup>
										<thead>
											<tr>
												<th>事件标题</th>
												<th>事发时间</th>
												<th>办结期限</th>
												<th>事件分类</th>
												<th>所属网格</th>
												<th>当前状态</th>
												<th>采集时间</th>
											</tr>
										</thead>
									</table>
								</div>
						
								<div class="mt-tbody bs">
									<table>
										<colgroup>
											<col style="width: auto;">
											<col style="width: 1.2rem;">
											<col style="width: 2rem;">
											<col style="width: 3rem;">
											<col style="width: 2.4rem;">
											<col style="width: 1.2rem;">
											<col style="width: 1.2rem;">
										</colgroup>
										<tbody id="eventListBodyContent" class="variableTable">
											
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="mask-c-bottom clearfix">
							<div class="fr mcb-pagination clearfix" id="pageDiv">
								<div id="eventTypePage"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="mask-content mask-content2">
					<a href="javascript:void(0);" class="mc2-back" id="mc2-back">返回</a>
					<div class="mc-box flex">
						<div class="mcb-left bs">
							<div class="mc-title bs">
								<p>事件属性</p>
							</div>
							<div class="mtr20 mask-device">
								<div class="swiper-container md-swiper-container">
									<div class="swiper-wrapper variable" id="attrDiv">
									
									</div>
									<!-- Add Pagination -->
									<div class="swiper-pagination"></div>
								</div>
								
							</div>
							<div class="mdt-box mtr20">
								<div class="md-text mcbl-bg bs">
									<div class="angle top-left"></div>
									<div class="angle top-right"></div>
									<div class="angle bottom-left"></div>
									<div class="angle bottom-right"></div>
									<div class="mdt-item flex flex-ac">
										<div>
											<p class="mdt-icon">紧急程度:</p>
										</div>
										<!-- 一般添加general，正常添加normal，紧急添加urgent -->
										<p class="general" id="urgenceDegreeStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">影响范围:</p>
										</div>
										<p class="white flex1 variable" id="influenceStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">涉及人员:</p>
										</div>
										<p class="white flex1 variable" id="involvePeopleStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">联系人员:</p>
										</div>
										<p class="white flex1 variable" id="contactor"></p>
									</div>
								</div>
								<div class="md-text md-text1 mcbl-bg bs">
									<div class="angle top-left"></div>
									<div class="angle top-right"></div>
									<div class="angle bottom-left"></div>
									<div class="angle bottom-right"></div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">信息来源:</p>
										</div>
										<p class="white flex1 variable" id="sourceStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">采集渠道:</p>
										</div>
										<p class="white flex1 variable" id="collectStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">所属网格:</p>
										</div>
										<p class="white flex1 variable" id="gridPathStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">事件编号:</p>
										</div>
										<p class="white flex1 variable" id="eventNumStr"></p>
									</div>
								</div>
							</div>
						</div>
						<div class="mcb-right flex1 mlr10">
							<div class="mcb-r-top" style="height:2.1rem">
								<div class="mc-title bs flex flex-jb">
									<p class="variable" id="eventNameStr" style="max-width: 8rem;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"></p>
									<div class="mcb-rtc-time clearfix">
										<p>事发时间:</p>
										<p class="variable" id="cerateTimeStr"></p>
									</div>
								</div>
								<div class="mcb-rt-content" style="height:1.8rem">
									<div class="mcb-rtc-title flex flex-ac flex-jb" style="height:0.5rem">
										<p class="mcb-rtc-name variable" id="eventContentStr" style="max-height: 0.5rem;"></p>
									</div>
									<div class="clearfix mcb-rtc-item flex">
										<p style="width: 0.4rem;">地点:</p>
										<p class="variable" id="occurdStr"></p>
									</div>
									<div class="clearfix mcb-rtc-item flex">
										<p>当前环节:</p>
										<p class="variable" id="curTask"></p>
										<p class="mlr20">当前状态:</p>
										<p class="variable" id="curStatus"></p>
									</div>
								</div>
								<a href="javascript:void(0);" id="db-btn" class="mcb-r-btn db-btn">办理</a>
								<a href="javascript:void(0);" id="sjhj-btn" class="mcb-r-btn sjhj-btn" style="display: block;">历史环节</a>
							</div>
						    <div id="dubanIconDiv" class="dubanIcon hide"></div>
							<div class="mcb-r-bottom mcb-r-bottom1 bs" style="height:4.2rem">
								 <div class="tab-div mlr30 mrr30 mbr20" id="tabdiv" style="padding-top:.1rem">
					                <a class="active"><b>流程详情</b></a>   
					                <a class="mlr20" id="db_bt" style="display: none;"><b>督办记录</b></a>
					             </div>
					             <div id="tabdivSub" >
					             	<div style="display: none" class="show"><!--流程详情  --> 
						             	<div class="mc-title bs flex flex-ac flex-jb">
											<!-- <p>流程详情</p> -->
											<div class="det-links-des">
												<ul class="flex layer_aj_bt_n flex-ac flex-je">
													<li class="flex flex-ac">
														<div class="aj_ks aj_ks_blue">
															<div class="aj_ks1"></div>
															<div class="aj_ks2"></div>
														</div>
														<p>当前环节</p>
													</li>
													<li class="flex flex-ac" style="margin-right:0.5rem">
														<div class="aj_ks aj_ks_gray">
															<div class="aj_ks1"></div>
															<div class="aj_ks2"></div>
														</div>
														<p>历史环节</p>
													</li>
													<li class="flex flex-ac">
							        					<div class="aj_ks aj_ks_gray">
															<div class="aj_ks1" style="background-color: #FFB90F"></div>
															<div class="aj_ks2"></div>
								        				</div>
							        					<p>驳回环节</p>
							       					 </li>
												</ul>
											</div>
										</div>
										<div class="mcb-rb-content mcb-rb-content1" style="height:3.2rem">
											<div class="layer_aj_bt_b bs flex flex-ac">
												<p class="layer_aj_bt_b1">办理环节</p>
												<p class="layer_aj_bt_b2">办理人/办理时间</p>
												<p class="layer_aj_bt_b3">办理意见</p>
											</div>
											<div class="layer_aj_bt_s">
												<div class="layer_aj_bt_line"></div>
												<ul class="layer_aj_bt_items variable" id="flowDetail">
													
												</ul>
											</div>
										</div>
					             	</div>
					             	<div id="remindListContentDiv" style="display: none"><!--督办记录  -->
					             	</div>
					             </div>
							</div>
							<div class="mcb-r-bottom mcb-r-bottom2 bs" style="display: block;height:4.2rem">
								<div class="mc-title bs flex flex-ac flex-jb">
									<p>办理</p>
								</div>
								<!-- 待办事件办理 -->
    <div class="mae-container mae-event-handle" id="mae-event-handle" style="display:block;margin-left:-4.5rem;height:52%;width:12rem">
        <input type="hidden" name="formId" id="formId"/>
		<input type="hidden" name="instanceId" id="instanceId"/>
		<input type="hidden" name="nextNode" id="nextNode"/>
		<input type="hidden" name="nextNodeCode" id="nextNodeCode"/>
		<input type="hidden" name="nextNodeId" id="nextNodeId"/>
		<input type="hidden" name="curTaskId" id="curTaskId"/>
		<input type="hidden" name="deploymentId" id="deploymentId"/>
		<input type="hidden" name="curNodeName" id="curNodeName"/>
		<input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
		
        <div class="mael-content">
            <div id="handlebox" class="mael-c-top">
                <div class="mael-ct-item clearfix">
                    <p><span>*</span>下一环节:</p>
                    <div class="mael-cti-right fr">
                        <div id="nextTaskNode" class="mael-crir-link clearfix" style="margin-top: 0.07rem;"></div>
                    </div>
                </div>
                
                <div id="evaLev" class="mael-ct-item clearfix">
                <input type="hidden" name="evaLevel" id="evaLevel"/>
                    <p><span>*</span>评价等级:</p>
                    <div class="mael-cti-right fr">
                        <div class="mael-crir-link clearfix">
                            <a onclick="evaClick('01')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: 0rem;"><i></i></div>
                                <p>不满意</p>
                            </a>
                            <a onclick="evaClick('03')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: 0rem;"><i></i></div>
                                <p>一般</p>
                            </a>
                            <a id="defaultSelect" onclick="evaClick('02')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: 0rem;"><i></i></div>
                                <p>满意</p>
                            </a>
                        </div>
                    </div>
                </div>

                <div id="handlePerson" class="mael-ct-item clearfix">
                    <p><span>*</span>办理人员:</p>
                    <div class="mael-cti-right fr">
                        <a style="display:none" id="selectOrgButton" onclick="_selectHandler()" href="javascript:void(0);" class="mael-ctir-personnel">
                            <p>选择办理人员</p>
                        </a>
                        <input type="hidden" name="nextStaffId" id="userIds"/>
				        <input type="hidden" name="curOrgIds" id="curOrgIds"/>
				        <input type="hidden" id="curOrgNames" />
				        <div class="FontDarkBlue fl DealMan Check_Radio" id="userName_div"><b name="userNames" id="userNames" style="color:white;font-size:12px"></b></div>
		    	        <div class="FontDarkBlue fl DealMan"><b name="htmlUserNames" id="htmlUserNames"></b></div>
				        <input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
                    </div>
                </div>

                <div id="intervalTr" class="mael-ct-item clearfix" style="display:none">
                    <p>办理时限:</p>
                    <div class="mael-cti-right fr">
                        <input type="text" id="eventHandleInterval" name="eventHandleInterval" class="from mael-ctir-text easyui-numberbox" data-options="tipPosition:'bottom',max:7,min:1" style="width: 210px; height: 28px;" value="${eventDefaultHandleInterval!'7'}" />
						<label class="LabName" style="float: none; display: inline-block; margin-left: 0; width: 25px;color:white">(天)</label>
                    </div>
                </div>
                
                <div class="mael-ct-item clearfix">
                    <p><span>*</span>办理意见:</p>
                    <div class="mael-cti-right fr">
                        <textarea id="adviceText" class="mael-ctir-text easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" placeholder="请输入您的意见"></textarea>
                    </div>
                </div>
                
                <div class="mael-ct-item clearfix" id="attrPartDiv">
                    <p>附件上传:</p>
                    <div class="mael-cti-right fr" >
                        <div id="bigFileUploadDiv" style="max-height:2.5rem;overflow:auto"></div>
                    </div>
                </div>

                <div class="mael-ct-item clearfix" style="height:0.5rem">
                    <div id="workflowBtn" style="height:0.3rem;position:relative"><div>
                </div>

            </div>
            
            
        </div>
        
        
        
       <#include "/zzgl/event/workflow/select_user.ftl" />

        
    </div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>
		
		var u = navigator.userAgent, app = navigator.appVersion;   
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
			
			$(function(){
				if("${eventId}"){
					initSupervise("${eventId}",null);
				}else{
					initEventList(null,null);
				}
			});
			
			$('#superviseRemark').validatebox({
				required:true,validType:['maxLength[250]','characterCheck'],tipPosition:'bottom'
			})
			$('#smsContent').validatebox({
				required:true,validType:['maxLength[500]','characterCheck'],tipPosition:'top'
			})
			
			$('.close').click(function(){
				$('#mask-eventAttr').attr('src','');
				$('#eventAttr').hide();
			});
			
			//初始化时间控件
			var createTimeDateRender = $('#_createTimeDateRender').anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#createTimeStart").val(api.getStartDate());
					$("#createTimeEnd").val(api.getEndDate());
				},
				OnCleared : function() {
					$("#createTimeStart").val('');
					$("#createTimeEnd").val('');
				}
			}
			}).anoleDateApi();
			
			
			
			// 滚动条优化
			$('.mt-tbody,.mdt-box,.mcb-rb-content1,#eventContentStr').niceScroll({
				cursorcolor: '#00a0e9',
				cursoropacitymax: '.4',
				cursorwidth: ".05rem",
				cursorborderradius: ".05rem",
				cursorborder: 'none',
				autohidemode: false,
			})
			
			//顶部页签点击事件
			$('.mct-item1').click(function(){
				$(this).toggleClass('active').siblings('.mct-item1').removeClass('active');
			})
		
			//发送短信按钮是否选中
			$('#sendmsg').click(function(){
				if($('#sendMessageButton').hasClass('active')){
					$('#sendMessageButton').removeClass('active');
					$('#otherMobileNums').val('');
					$('#smsContent').val('');
					$('#otherMobileNumsDiv').css("visibility","hidden");
					$('#smsContentDiv').css("visibility","hidden");
				}else{
					var isValid =  $("#superviseRemarkForm").form("validate");
					if(isValid){
						$('#sendMessageButton').addClass('active');
						$('#otherMobileNumsDiv').css("visibility","visible");
						$('#smsContentDiv').css("visibility","visible");
					}
				}
			});
			
			// 历史环节的线
			function line(){
				$('.layer_aj_bt_line').height($('.layer_aj_bt_items').height()+38);
			}
			// 内容的切换
			$('.mt-tbody>table tbody tr td+td').click(function(){
				$('.mask-content1').hide().siblings('.mask-content2').show();
				$('.mt-tbody,.mdt-box').getNiceScroll().resize();
			})
			
			// 环节选择
            $('.mael-crir-link').on('click', '.mael-crirl-item', function () {
                $(this).addClass('active').siblings().removeClass('active');
            });
			
			//返回键点击事件
			$('.mc2-back').click(function(){
				$('.mask-content2').hide().siblings('.mask-content1').show();
				$('.mt-tbody,.mdt-box,.mcb-rb-content1').getNiceScroll().resize();
				$('.variable').html('');
				$('#superviseRemark').val('');
				$('#otherMobileNums').val('');
				destroySwiper();
			})
			
			
			// 事件属性的swiper的初始化
			var mdSwiper=null;
			function eventSwiper(attrflag){
				if(mdSwiper!=null){
					mdSwiper.destroy(false);
				}
				if(attrflag>1){
				
				mdSwiper = new Swiper('.md-swiper-container', {
					loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
					pagination: {
						el: '.swiper-pagination',
						clickable: true
					},
				});
				
				mdSwiper.autoplay.start();
				}
			}
			// 注销swiperd方法
			function destroySwiper(){
				//mdSwiper.destroy(false);
			}
			// 事件环节和督办的切换
			$('#sjhj-btn').click(function(){
				$(this).hide().siblings('#db-btn').show();
				$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
				line();
				$('.mcb-rb-content1').getNiceScroll().resize();
			})
			$('#db-btn').click(function(){
				$(this).hide().siblings('#sjhj-btn').show();
				$('.mcb-r-bottom2').show().siblings('.mcb-r-bottom1').hide();
				$('.mcb-rb-content1').getNiceScroll().resize();
				showHandleDetail(curEventId,instanceId);
			})
			
			
			window.addEventListener("message",function(e){  
				eval(e.data);
			},false);
			
			var curEventId;
			var taskId;
			var instanceId;
			
			var imgList=[];//图片集合用于展示使用
			var imgTitleList=[];//图片标题集合用于展示使用
			
			function initSupervise(eventId,type){
				
				modleopen();
		 		$(".mask-content2").addClass('db-mc').show().siblings('.mask-content1').hide();
		 		$('#sendMessageButton').removeClass('active');
				$('#otherMobileNumsDiv').css("visibility","hidden");
				$('#smsContentDiv').css("visibility","hidden");
			 	//获取事件详情
			 	$('.layer_aj_bt_line').height(0);//清空历史环节线
			 	
			 	
			 	if(type=='list'){
		 			$(".mc2-back").css('display','unset');
		 			//列表入口需要判断是否显示办理按钮（默认显示历史环节页面）
		 			$('#sjhj-btn').hide().siblings('#db-btn').show();
					$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回列表页面（相当于点击左上角的返回）
					$('#cancelSupervise').click(function(){
						$('#mc2-back').click();
					});
					
					
		 			
		 					
				}else{//督办入口进入，默认也显示历史环节页面
					
					$('#sjhj-btn').hide().siblings('#db-btn').show();
					$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回大屏页面（相当于点击右上角的×）
					$('#cancelSupervise').click(function(){
						closeIframe();
						if("${messageUrl!''}"){
							window.parent.postMessage('eventDetailClose()',"${messageUrl!''}");
						}else{
							window.parent.postMessage('eventDetailClose()','${GIS_DOMAIN!""}/gis/jiangYinPlatform/index.jhtml');
						}
					});
					
				}
				
				//点击提交则执行督办提交方法addRemind
				$('#submitSupervise').click(function(){
					addRemind(type);
				});
			 	
			 	$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getEventInfo.json',
					data: {"eventId":eventId},
					dataType:"json",
					success: function(data){
						console.log(data);
						var event=data.event;
						
						//设置全局变量
						curEventId=event.eventId;
						taskId=data.process[0].TASK_ID;
						instanceId=data.instanceId;
						
						 
						$('#urgenceDegreeStr').removeClass();
						var urgenceDegree=event.urgencyDegree;
						if(urgenceDegree=='03'){
							$('#urgenceDegreeStr').addClass('urgent');
						}else if(urgenceDegree=='02'){
							$('#urgenceDegreeStr').addClass('normal');
						}else{
							$('#urgenceDegreeStr').addClass('general');
						}
						$('#urgenceDegreeStr').addClass('variable')
						$('#urgenceDegreeStr').html(event.urgencyDegreeName);
						$('#influenceStr').html(event.influenceDegreeName);
						$('#involvePeopleStr').html(event.involvedNumName);
						$('#contactor').html(event.contactUser+'('+event.tel+')');
						$('#sourceStr').html(event.sourceName);
						$('#collectStr').html(event.collectWayName);
						$('#gridPathStr').html(event.gridPath);
						$('#eventNumStr').html(event.code);
						$('#cerateTimeStr').html(event.happenTimeStr);
						$('#eventContentStr').html(event.content);
						$('#occurdStr').html(event.occurred);
						$('#curStatus').html(event.statusName);
						if(event.eventClass){
							$('#eventNameStr').html('['+event.eventClass+']'+event.eventName);
							$('#eventNameStr').attr("title",'['+event.eventClass+']'+event.eventName);
						}else{
							$('#eventNameStr').html(event.eventName);
							$('#eventNameStr').attr("title",event.eventName);
						}
						
						if(data.process[0].IS_CURRENT_TASK){
							$('#curTask').html(formatStr(data.process[0].TASK_NAME));
						}else{
							$('#curTask').html('结案');
						}
						
		 				//如果不是归档状态且是当前办理人则显示办理按钮
		 				var isCurrentUser=data.isCurrentUser;
		 				
		 				if(event.status=='04'||(!isCurrentUser)){//已结案或者不是当前办理人，不显示办理按钮
		 					$('#sjhj-btn').hide().siblings('#db-btn').show();
							$('#db-btn').hide();
							$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
							$('.mcb-rb-content1').getNiceScroll().resize();
		 				}
		 				
						//填充环节信息
						var flow=data.process;
						var flowStr="";
						
						//设置办理人组织层级颜色
						var handleColorArr={
							"1":"",
							"2":"",
							"3":"#e65e7d",
							"4":"#e68f66",
							"5":"#e6dd63",
							"6":"#00d1fe",
							"7":"#00d1fe",
						}
						
						var imgTitleArr={
							"1":"处理前",
							"2":"处理中",
							"3":"处理后"
						}
						
						for(var i=0,j=flow.length;i<j;i++){
							var d=flow[i];
							//如果是当前环节
							if(d.IS_CURRENT_TASK){
								flowStr+='<li class="flex flex-ac">'+
								'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>'+
								'<div class="aj_ks aj_ks_blue">'+
								'<div class="aj_ks1"></div>'+
								'<div class="aj_ks2"></div>'+
						        '</div>'+
						        '<div>'+
						        '<div class="flex flex-ac">';
								<#if canContactGridAdmin??>
									if(d.HANDLE_PERSON_USER_ID){
										var _userIds = d.HANDLE_PERSON_USER_ID.split(';')
										var _userNames = d.HANDLE_PERSON.split(';')
										var _userOrgIds = d.HANDLE_PERSON_ORG_ID.split(';')
										var _lis = '';
										if(Array.isArray(_userIds) && _userIds.length>0){
											for (var k = 0,l=_userIds.length; k <l ; k++) {
												if(_userIds[k]){
													var _onclickStr = ' onclick="showGridAdminDetail('+_userIds[k]+','+_userOrgIds[k]+')" ';
													_lis += '<span style="color:'+handleColorArr[d.ORG_LEVEL]+';CURSOR: pointer; text-underline-position:auto;" onmouseover="this.className=\'span-grid-admin\'" onmouseout="this.className=\'\'" ' + _onclickStr +'>'+_userNames[k]+'<img src="${uiDomain!}/web-assets/_big-screen/nanAn/images/zdry/icon-list-t03b.png" /></span><br/>';
												}
											}
										}
										if(_lis){
											flowStr+='<p class="aj_items_t aj_items_t_yellow" >' + _lis +'</p></div>';
										}
									}
								<#else>
									flowStr+='<p class="aj_items_t aj_items_t_yellow">'+d.HANDLE_PERSON+'</p></div>';
								</#if>

						        if(d.subAndReceivedTaskList!=null && d.subAndReceivedTaskList.length>0){
						        
						        	for(var m=0,n=d.subAndReceivedTaskList.length;m<n;m++){
						        		var t=d.subAndReceivedTaskList[m];
						        		
						        		flowStr+='<div class="flex flex-ac mtr10">'+
						        		'<div class="aj_items_t2 flex">'+
						        		'<p class="aj_items_t aj_items_t_yellow" style="margin-left:0rem;text-align: left"><span style="color:'+handleColorArr[d.ORG_LEVEL]+';">'+t.TRANSACTOR_NAME+'('+t.ORG_NAME+')'+'</span><br>';
						        		if(m==0){
						        			flowStr+='<span>接受时间</span>:'+t.RECEIVE_TIME;
						        		}else{
						        			flowStr+='<span>处理时间</span>:'+t.END_TIME;
						        		}
						        		
						        		if(t.REMARKS){
						        			flowStr+='<p class="aj_items_t aj_items_t_result">'+t.REMARKS+'</p>'
						        		}
						        	
						        	}
						        
						        }
							
						        flowStr+='</div>'+
						        '</li>';
							}else{
							
								flowStr+='<li class="flex flex-ac">'+
								'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>';
								
								if(d.OPERATE_TYPE==2){//驳回环节
									flowStr+='<div class="aj_ks aj_ks_gray">'+
										'<div class="aj_ks1" style="background-color: #FFB90F"></div>'+
										'<div class="aj_ks2"></div>'+
										'</div>';
								}else{
								
									flowStr+='<div class="aj_ks aj_ks_gray">'+
									'<div class="aj_ks1"></div>'+
									'<div class="aj_ks2"></div>'+
									'</div>';
								
								}
								
								flowStr+='<div class="aj_items_t2 flex">'+
								'<p class="aj_items_t aj_items_t_green bs"><span style="color:'+handleColorArr[d.ORG_LEVEL]+';">'+formatStr(d.TRANSACTOR_NAME)+'('+d.ORG_NAME+')'+'</span><span> 耗时 '+d.INTER_TIME+'</span><br><span>办结时间</span>:'+d.END_TIME+'</p>'+
								'</div>';
								if(d.REMARKS){
									flowStr+='<p class="aj_items_t aj_items_t_result">'+d.REMARKS+'</p>';
								}
								flowStr+='</li>';
							
							}
						
						}
						
						$('#flowDetail').html(flowStr);
						fetchRemindInfo();
						
						line();
						
						if(type=='list'){
		 					$(".mc2-back").css('display','unset');
						}else{//督办入口进入，默认显示督办页面
						}
						
						
						// 滚动条优化
						$('.mcb-rb-content1').getNiceScroll().resize();
						
						
						//填充附件轮播信息
						var attrStr="";
						imgList=[];
						imgTitleList=[];
						var imgs=data.imgs;
						
						var sounds=data.sounds;
						var videos=data.videos;
						var attrflag=0;
						if(imgs.length>0){
							for(var i=0,j=imgs.length;i<j;i++){
								var d=imgs[i];
								imgList.push("${IMG_URL}"+d.filePath);
								imgTitleList.push(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前");
								attrStr+='<div class="swiper-slide">';
								attrStr+='<div class="div-text"><p>'+(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前")+'</p></div>';
								attrStr+='<img style="cursor:pointer;z-index:10" onclick="showImg('+i+')" src="${IMG_URL}'+d.filePath+'" >';
								attrStr+='</div>';
								attrflag+=1;
							}
						}
						if(sounds.length>0){
							for(var i=0,j=sounds.length;i<j;i++){
								var d=sounds[i];
								attrStr+='<div class="swiper-slide">'+
								'<img  style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						if(videos.length>0){
							for(var i=0,j=videos.length;i<j;i++){
								var d=videos[i];
								attrStr+='<div class="swiper-slide">'+
								'<img style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						
						if(attrStr==''){
							attrStr+='<div class="swiper-slide">'+
							'<img src="${uiDomain!''}/images/nopic.jpg">'+
							'</div>';
						}
				
						
						$('#attrDiv').html(attrStr);
						
						eventSwiper(attrflag);//初始化轮播
						fetchRemindInfo();
						
					},
					complete:function(){
						modleclose();
					}
				});
				
			}
			
			var begintime;
			var endtime;
			
			function initTime(){
        		begintime= "${(createTimeStart)!}";
        		endtime="${(createTimeEnd)!}";
        	}
        	
        	Date.prototype.format = function(format) {
    			var o = {
        		"M+" : this.getMonth() + 1, // month
        		"d+" : this.getDate(), // day
        		"h+" : this.getHours(), // hour
        		"m+" : this.getMinutes(), // minute
        		"s+" : this.getSeconds(), // second
        		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        		"S" : this.getMilliseconds()
    			// millisecond
    			}
    			if (/(y+)/.test(format))
        			format = format.replace(RegExp.$1, (this.getFullYear() + "")
                		.substr(4 - RegExp.$1.length));
    				for ( var k in o)
        				if (new RegExp("(" + k + ")").test(format))
            				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    			: ("00" + o[k]).substr(("" + o[k]).length));
    			return format;
			}
			
			var curPage=1;
			var pageSize=10;
			var totalPage=1;
			
			var infoOrgCode;
			var defaultOrgCode;
			var urgencyDegree;
			var defaultGridName;
			var status;
			var startGridId;
			var eventStatus='00,01,02,03,04';//大屏上的列表暂时都只显示未办结的事件，排除已归档的事件
			var ztreeApi=null;
			
			function initEventList(urgencyDegreeStr,infoOrgCodeStr){
				modleopen();
				
				if(urgencyDegreeStr){
					urgencyDegree='0'+urgencyDegreeStr;
				}
				
				if(infoOrgCodeStr){
					infoOrgCode=infoOrgCodeStr;
					//赋予默认查询初始网格编码
					defaultOrgCode=infoOrgCodeStr;
				}else{
					defaultOrgCode="${infoOrgCode}";
					infoOrgCode="${infoOrgCode}";
				}
				
				
				$.ajax({
					type: "POST",
					async: false,
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getGridInfo.json',
					data: {"infoOrgCode":defaultOrgCode},
					dataType:"json",
					success: function(data){
						console.log(data);
						startGridId=data.gridId;
						defaultGridName=data.gridName;
						$('#gridName').val(defaultGridName);
						
						//初始化网格树

						if(!(ztreeApi)){
						ztreeApi=AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
								if(items!=undefined && items!=null && items.length>0){
									var grid = items[0];
									infoOrgCode=grid.orgCode;
								} 
						}, {
						OnCleared: function() {
							infoOrgCode=defaultOrgCode;
						},
						ShowOptions: {
							EnableToolbar : true
						},
						
						startGridId : startGridId
						});
						
						$("#combobox_INPUTgridName_ul_0").parent().mCustomScrollbar();
						}
			
						
						
						
					}
				});
				
				
				
				
				
				initTime();
				$('#createTimeStart').val(begintime);
				$('#createTimeEnd').val(endtime);
				$('#_createTimeDateRender').val(begintime+' ~ '+endtime);
				
				$(".mask-content1").show().siblings('.mask-content2').hide();
				
				getQueryParams();
				queryParams.infoOrgCode=defaultOrgCode;
				
				if(urgencyDegree){
					queryParams.urgencyDegree=urgencyDegree;
				}
				
				//获取事件列表页信息
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getEventListPageData.json',
					data: queryParams,
					dataType:"json",
					success: function(data){
						console.log(data);
						var list=data.eventList.rows;
						
						if(list == null || list.length == 0){
							return;
						}
						totalPage=Math.ceil(data.eventList.total/pageSize);
						var str="";
						
						for(var i=0,j=list.length;i<j;i++){
						
							var d=list[i];
							str+='<tr style="cursor:pointer" onclick="initSupervise('+d.eventId+',\''+'list'+'\')">'+
							'<td title="'+d.eventName+'">'+d.eventName+'</td>'+
							'<td>'+new Date(d.happenTime).format('yyyy-MM-dd')+'</td>'+
							'<td>'+d.handleDateStr+'</td>'+
							'<td>'+formatStr(d.eventClass)+'</td>'+
							'<td title="'+d.gridPath+'">'+d.gridPath+'</td>'+
							'<td>'+d.statusName+'</td>'+
							'<td>'+new Date(d.createTime).format('yyyy-MM-dd')+'</td>'+
							'</tr>';
							
						
						}
						
						//填充满意度
						if(data.allEvaEvent==0){
							$('#satisfiedRate').html('暂无归档评价事件');
						}else{
							$('#satisfiedRate').html(formatDouble(data.satisfyEvaEvent/data.allEvaEvent))
						}
						
						//填充超时事件数
						$('#overtimeEventCount').html(data.findOvertimeEventCount);
						
						//填充督办事件数
						$('#superviseEventCount').html(data.findSuperviseEventCount);
						
						$('#eventListBodyContent').html(str);
						
						
						//设置分页
						if(data.eventList.total<pageSize){
			    			$("#pageDiv").hide();
						}else{
			   				$("#pageDiv").show();
							$("#eventTypePage").remove();
							$("#pageDiv").html('<div id="eventTypePage"></div>');
							$("#eventTypePage").createPage({
								pageNum: (Math.floor((data.eventList.total-1)/pageSize)+1),
								current: curPage,
								backfun: function(e) {
									curPage=e.current;
									searchEventList(1);
								}
							});
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
			
			}
			
			//重置按钮
			function resetEventList(){
				$('#loadOvertimeListDiv').removeClass('active');
				$('#loadSuperviseListDiv').removeClass('active');
				//delete(queryParams.handleDateFlag);
				//delete(queryParams.superviseMark);
				$('#keyRemarkWord').val('');
				infoOrgCode=defaultOrgCode;
				$('#gridName').val(defaultGridName);
				curPage=1;
				$('.variableList').html('');
				$('.variableTable').html('');
				initEventList();
			}
			
			var queryParams={};
			function getQueryParams(){
			
				queryParams.eventType="all";
				queryParams.isEntryMatter=true;
				queryParams.page=curPage;
				queryParams.rows=pageSize;
				queryParams.createTimeStart=$('#createTimeStart').val();
				queryParams.createTimeEnd=$('#createTimeEnd').val();
				queryParams.keyRemarkWord=$('#keyRemarkWord').val();
				var type = "${(type)!}";
				if(type != null && type != ''){
					queryParams.type = type;
				}
				var urgencyDegree = "${(urgencyDegree)!}";//紧急程度
				if(urgencyDegree != null && urgencyDegree != ''){
					queryParams.urgencyDegree = urgencyDegree;
				}
				queryParams.status=eventStatus; // xx,xx,xx
				var statusForOut = "${(status)!}";
				if(statusForOut != null && statusForOut != ''){
					queryParams.status = statusForOut;
				}
				
				
			}
			
			
			//查询按钮
			function searchEventList(type){
				modleopen();
			
				if(!type){
					curPage=1;
				}
			
				$('.variableList').html('');
				$('.variableTable').html('');
				
				getQueryParams();
			
				queryParams.infoOrgCode=infoOrgCode;
				
				if(urgencyDegree){
					queryParams.urgencyDegree=urgencyDegree;
				}
				
				//获取事件列表页信息
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getEventListPageData.json',
					data: queryParams,
					dataType:"json",
					success: function(data){
						var list=data.eventList.rows;
						
						if(list == null || list.length == 0){
							return;
						}
						
						totalPage=Math.ceil(data.eventList.total/pageSize);
						
						var str="";
						
						for(var i=0,j=list.length;i<j;i++){
						
							var d=list[i];
							str+='<tr style="cursor:pointer" onclick="initSupervise('+d.eventId+',\''+'list'+'\')">'+
							'<td title="'+d.eventName+'">'+d.eventName+'</td>'+
							'<td>'+new Date(d.happenTime).format('yyyy-MM-dd')+'</td>'+
							'<td>'+d.handleDateStr+'</td>'+
							'<td>'+formatStr(d.eventClass)+'</td>'+
							'<td title="'+d.gridPath+'">'+d.gridPath+'</td>'+
							'<td>'+d.statusName+'</td>'+
							'<td>'+new Date(d.createTime).format('yyyy-MM-dd')+'</td>'+
							'</tr>';
							
						
						}
						
						//填充满意度
						if(data.allEvaEvent==0){
							$('#satisfiedRate').html('暂无归档评价事件');
						}else{
							$('#satisfiedRate').html(formatDouble(data.satisfyEvaEvent/data.allEvaEvent))
						}
						
						//填充超时事件数
						$('#overtimeEventCount').html(data.findOvertimeEventCount);
						
						//填充督办事件数
						$('#superviseEventCount').html(data.findSuperviseEventCount);
						
						$('#eventListBodyContent').html(str);
						
						//设置分页
						if(data.eventList.total<pageSize){
			    			$("#pageDiv").hide();
						}else{
			   				$("#pageDiv").show();
							$("#eventTypePage").remove();
							$("#pageDiv").html('<div id="eventTypePage"></div>');
							$("#eventTypePage").createPage({
								pageNum: (Math.floor((data.eventList.total-1)/pageSize)+1),
								current: curPage,
								backfun: function(e) {
									curPage=e.current;
									searchEventList(1);
								}
							});
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
			
			}
			
			
			function closeIframe(){
				$('.variable').html('');
				$('.variableList').html('');
				$('#superviseRemark').val('');
				$('#otherMobileNums').val('');
				$('#smsContent').val('');
				$('.variableTable').html('');
				$(".mask-content2").removeClass('db-mc');
				$(".mc2-back").css('display','none');
				
				destroySwiper();
			}
			
			function formatStr(str){
				if(str == null ||str == 'null' || str == undefined ){
					return '';
				}else{
					return str;				
				}
			}
			
			//督办提交操作
			function addRemind(type){
			
				var isValid =  $("#superviseRemarkForm").form("validate");
				if(isValid){//验证督办消息是否为空
					if($('#sendMessageButton').hasClass('active')){
						isValid =  $("#smsContentForm").form("validate");
					}
				}
				
				if(isValid){
				
				modleopen();
				//执行督办提交
				
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.json',
					data: {"eventId":curEventId,"taskId":taskId,"instanceId":instanceId,"remarks":$('#superviseRemark').val(),"otherMobileNums":$('#otherMobileNums').val(),"smsContent":$('#smsContent').val()},
					dataType:"json",
					success: function(data){
					
						if(data.result){
						
							closeIframe();
				
							//如果入口是list页面，则执行完返回list页面，并刷新页面
							if(type!=null&&type=='list'){
								initEventList();
								window.parent.postMessage('addRemindCallBack('+2+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
							}else{//如果入口是督办按钮或其他，则返回中控大屏并刷新对应模块
								window.parent.postMessage('addRemindCallBack('+1+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
							}
						
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
				
				}
			
				
			}
			
			function showImg(i){
				$('#eventAttr').show();
				$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?drag=true&background=none&overflow=none&fieldId=playImg&index='+i+'&titles='+imgTitleList.toString()+'&paths='+imgList.toString());
				//window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+imgList.toString());
			}
		
			function showVideo(id){//eventSeq
				$('#eventAttr').show();
				$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?isBigScreen=1&videoType=2&attachmentId='+id);
				//window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
			}
			
			//每隔三位加个逗号
			function formatInteger(nStr){
		
	 			nStr += '';  
        		x = nStr.split('.');  
        		x1 = x[0];  
        		x2 = x.length > 1 ? '.' + x[1] : '';  
        		var rgx = /(\d+)(\d{3})/;  
        		while (rgx.test(x1)) {  
            		x1 = x1.replace(rgx, '$1' + ',' + '$2');  
        		}  
        		return x1 + x2;  
	
			}
	
			//计算百分数保留2位小数并加上百分号
			function formatDouble(double){
	
				var str=Number(double*100).toFixed(2);
    			str+="%";
    			return str;
	
			}
			//两个条件不会共存，调用时先删除另外一个key
			var handleDateFlag='3';
			var superviseMark='1';
			
			function loadOvertimeList(){
				delete(queryParams.superviseMark);
				
				if(!$('#loadOvertimeListDiv').hasClass('active')){
					queryParams.handleDateFlag=handleDateFlag;
				}else{
					delete(queryParams.handleDateFlag);
				}
				
				searchEventList();
			}
			
			function loadSuperviseList(){
				delete(queryParams.handleDateFlag);
				
				if(!$('#loadSuperviseListDiv').hasClass('active')){
					queryParams.superviseMark=superviseMark;
				}else{
					delete(queryParams.superviseMark);
				}
				
				searchEventList();
			}
			
		var loadFlag=0;	
			//办理相关start
		function showHandleDetail(eventid,insid){
		
		if("${eventId}"&&loadFlag>0){
			return '';
		}else{
			loadFlag+=1;
		}
		
        modleopen(); 
        
        // 清空人员选择器
		$('#userTable').datagrid('loadData', { total : 0, rows : [] });
		$('#selUserTable').datagrid('loadData', { total : 0, rows : [] });
		// $("#promoter").attr("checked",false);
		$("#fast_user_div").hide();
        
        //初始化附件上传
        var bigFileUploadOpt = {
	   		useType: 'edit',
	   		<@block name="fileUploadInitOpt_fileTypes">
	   			fileExt: '.jpg,.gif,.png,.jpeg,.webp',
	   		</@block>
	   		attachmentData: {'bizId':eventid,'attachmentType':'ZHSQ_EVENT','eventSeq':'1,2,3', 'isBindBizId': 'yes'},
	   		module: 'event',
	   		styleType: 'list',
	   		individualOpt : {
	   			isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
	   		}
	   };
	   
	   $('#bigFileUploadDiv').html('');
	   bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
	   
	   $('#bigFileUploadDiv').delay(500).promise().done(defaultRadioCheck);//延时加载设置默认选中项(延时0.5秒)，为了能在图片上传控件加载完成后，才调用
	   
	   
	   /*$('#fileDiv').niceScroll({
			cursorcolor: "#2458a7",
			cursoropacitymax: 0.8,
			cursorwidth: ".05rem",
			cursorborderradius: ".05rem",
			cursorborder: 'none',
			autohidemode: true,
		});*/
		
        //获取工作流详情
        
        $.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getWorkflowHandleInfo.json',
		dataType : "json",
		data: {eventId:eventid,instanceId:insid}, 
		success: function (data) {
		    
		    $('#formId').val(eventid);
		    $('#instanceId').val(insid);
		    $('#curTaskId').val(data.taskId);
		    $('#deploymentId').val(data.deploymentId);
		    $('#curNodeName').val(data.curNode.nodeName);
		    
		    //设置下一环节
		    var strNextNodes='';
		    var nextNodes=data.taskNodes;
		    for(var i=0,j=nextNodes.length;i<j;i++){
		 	curTaskName=data.curNode.nodeName;
		    strNextNodes+='<a onclick="radioCheckOperate('+eventid+',\''+data.curNode.nodeName+'\',\''+nextNodes[i].nodeName+'\',\''+nextNodes[i].transitionCode+'\','+nextNodes[i].nodeId+','+insid+')" href="javascript:void(0);" class="mael-crirl-item clearfix">';
		    strNextNodes+='<div class="fl" style="margin-top: 0rem"><i></i></div>'+
		    '<p>'+nextNodes[i].nodeNameZH+'</p></a>';
		    
		    }
		    
		    
		    $('#nextTaskNode').html(strNextNodes);
		    
		    $(".mael-crirl-item")[0].click();//默认第一个选中
		    $('#ascrail2000').css('display','block');
		    
		    //设置按钮
		    var opList=data.operateLists;
		    if(opList.length==2){//有驳回按钮
		        var btnstr='<div class="mael-c-bottom clearfix" style="position: absolute;bottom: 0;">';
		        btnstr+='<a onclick="subTask()" href="javascript:void(0);" class="fl sumbit"><p>提交</p></a>'+
		        '<a id="rejectButton" onclick="rejectTask()" href="javascript:void(0);" class="fl mlr30"><p>驳回</p></a>';
		        
		        $('#workflowBtn').html(btnstr);
		        
		    }else{
		        var btnstr='<div class="maed-btn-box clearfix clearfix" style="position: absolute;bottom: 0;margin-left: 1rem;">';
		        btnstr+='<a onclick="subTask()" href="javascript:void(0);" class="fl sumbit"><p>提交</p></a>';
		        
		        $('#workflowBtn').html(btnstr);
		    }
		    
		    $('#backdetail').on('click', function () {
                $('#mae-event-detail').addClass('mae-container-on');
                $('#ascrail2000').css('display','none');
                $('#mae-event-handle').removeClass('mae-container-on');
                showEventInfo(eventid,bizplatform,status,pointReFlag);
            });
            
            // 滚动条优化
			$('#mae-event-handle').niceScroll({
				cursorcolor: '#00a0e9',
				cursoropacitymax: '.4',
				cursorwidth: ".05rem",
				cursorborderradius: ".05rem",
				cursorborder: 'none',
				autohidemode: false,
			})
		    
		    modleclose(); 
		    
		    
		}

	    })
        
     }

	 <#if canContactGridAdmin??>
     function showGridAdminDetail(userId,orgId){
		 modleopen();
		 $.ajax({
			 type: "POST",
			 async:false,
			 url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/findInfoOrgCodesByOrgIds.json',
			 data: {"orgId":orgId},
			 dataType:"json",
			 success: function (data) {
			 	if(data && data.length>0) {
					modleopen();
					$.ajax({
						type: "post",
						url: "${SQ_ZZGRID_URL}/zzgl/grid/gridAdmin/findGridAdminListByUserIdJsonp.json?jsoncallback=?",
						data: {
							'userId': userId,
							'infoOrgCode': data[0].ORG_CODE
						},
						dataType: "jsonp",//数据类型为jsonp
						success: function (data) {
                            if(Array.isArray(data.result) && data.result.length>0){
                                var userInfo = data.result[0];
                                var paramsData = {
                                    labelType: 'grider',
                                    wid: userInfo.gridAdminId,
                                    name: userInfo.partyName
                                };
                                var urlDomain = "${GIS_DOMAIN!}/gis/base/arcgis/commonCrossDomain.jhtml?callBackFunction=" + "summaryCallBack(" + encodeURIComponent(JSON.stringify(paramsData).replace(/"/g, "'")) + ")";
                                $("#cross_domain_iframe").attr("src", urlDomain);
                            }else{
                                $.messager.alert('提示','网格员管理模块无有效网格员，请确认网格员是否调整!','info');
                            }
						},
						error: function (e) {
							console.log(e);
						},
						complete:function(){
							modleclose();
						}
					});
				}
			 },
			 complete:function(){
				 modleclose();
			 }
		 });
	 }
	 </#if>
     
     var g_EventNodeCode=null;
     var curTaskName='';
     
     function defaultRadioCheck(){}//附件上传回掉函数，不定义会报错
     
     
        //下一办理环节选中操作
		function radioCheckOperate(formId_,curnodeName_,nodeName_,nodeCode_,nodeId_,insid) {
		
		    $('#userIds').val('');
			$('#curOrgIds').val('');
			$('#curOrgNames').val('');
			$('#userNames').html('');
			$('#htmlUserNames').html('');
			$('#htmlUserIds').html('');
			
			$('#nextNode').val(nodeName_);
			$('#nextNodeCode').val(nodeCode_);
			$('#nextNodeId').val(nodeId_);
			
			if('null'==nodeCode_){
                nodeCode_='';
            }
			
			// 通过ajax请求判断是否隐藏人员选择器，如果是上报越级，隐藏人员选择器，并将值传入userIds和userNames
			$.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/keyelement/keyElementController/getNodeInfoForEvent.jhtml",
				data:{
					formId : formId_,
					formType : '300',
					curnodeName: curnodeName_,
					nodeName: nodeName_,
					nodeCode: nodeCode_,
					nodeId: nodeId_,
					instanceId: insid
				},
				dataType:"json",
				success:function(data) {
					
					g_EventNodeCode = data.eventNodeCode;
					
					if (data.msg && data.msg != "") {
						$.messager.alert("警告", data.msg, "warning");
					} else {
					
					    if(!data.isSelectOrg&&!data.isSelectUser){//不用选择人环节
					    
					        $('#handlePerson').hide();
					        $('#evaLev').hide();
					        if(curnodeName_=='task9'){//评价环节 
					        
					        	$('#evaLev').show();
					        	$('#defaultSelect').click();
					        	$('#attrPartDiv').hide();//评价环节不展示附件上传按钮
					        
					        }else{
					        	$('#attrPartDiv').show();
					        }
					    
					    }else{
					    	$('#evaLev').hide();
					    	$('#handlePerson').show();
							//选择人员的情况
							if(data.isSelectUser){
						    	$('#selectOrgButton').hide();
						    	if(isAndroid){
								//if(navigator.userAgent.match("ua_ffcs")){
									$('#userName_div').css("margin-top","-0.1rem");
									$('#nextTaskNode').css("margin-top","0.04rem");
								}else{
									$('#userName_div').css("margin-top","-0.01rem");
								}
						    	$('#userIds').val(data.userIds);
			                	$('#curOrgIds').val(data.orgIds);
			                	$('#userNames').html(data.userNames);
			                	$('#htmlUserIds').html(data.userIds);
	
							}else{
						    	$('#selectOrgButton').show();
								$('#userName_div').css("margin-top","3px");
							}
						
						
					    }
					    
					    
						if(data.isShowInterval) {
							$("#intervalTr").show();
							$(".validatebox-text").css({"background-color":"rgba(0, 156, 255, .2)","border": "solid .01rem #009cff","color":"white"});
						}else{
							$("#intervalTr").hide();
						}
							
					}
				}
			});
		}
		
		
		
	//打开人员选择窗口
	function _selectHandler() {
		$('#userSelectorBtnList').width(260);
		var obj = {
			nextNodeId				: $('#nextNodeId').val(),
			nextUserIdElId 			: 'userIds',
			nextUserNameElId 	: 'userNames',
			nextOrgIdElId 			: 'curOrgIds',
			nextOrgNameElId 	: 'curOrgNames',
			nextNodeType 			: 'task',
			formId					: $("#formId").val(),
			formType				: '300',
			instanceId				: $("#instanceId").val()
		};
		
		selectUserByObj(obj);
		if(isAndroid){
		//if(navigator.userAgent.match("ua_ffcs")){
			$('.window-shadow').css('display','none');			
			$('.window').css('zoom','0.76');			
			$('.window').css('margin-left','1.5rem');			
			$('.window').css('margin-top','0.5rem');			
		}else{
			$('.window-shadow').css('display','none');			
		}
		
		$('.layout-panel-south .panel-body-noborder').css('height','0.5rem');
		
	}
	
	function evaClick(lev){
	    $('#evaLevel').val(lev);
	}
	
	
	function subTask(){
	    if($('#handlePerson').is(':hidden')){
            if(!$('#adviceText').val()){
                $.messager.alert("警告", "请填写办理意见", "info");
                return;
            }
      
        }else{
        
            if(!$('#userIds').val()){
                $.messager.alert("警告", "请选择办理人员", "info");
                return;
            }else{
                if(!$('#adviceText').val()){
                    $.messager.alert("警告", "请填写办理意见", "info");
                    return;
                }
            }
        
        }
        
        if($('#nextNode').val()=='task8'){//结案环节需要上传处理后图片
        	//var imgBefore = $("span[label-name='处理前']").length;
			var	imgAfter = $("span[label-name='处理后']").length;
			
			if(imgAfter == 0) {
				$.messager.alert('警告', "请先上传处理后图片！", 'warning');
				return;
			}
        }
        
	    modleopen();
	    
	    $.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml",
				data:{
					advice : $('#adviceText').val(),
					evaContent : $('#adviceText').val(),
					nextNode : $('#nextNode').val(),
					nextStaffId: $('#userIds').val(),
					instanceId:$('#instanceId').val(),
					deploymentId:$('#deploymentId').val(),
					curOrgIds:$('#curOrgIds').val(),
					formId:$('#formId').val(),
					taskId:$('#curTaskId').val(),
					evaLevel:$('#evaLevel').val(),
					curNodeName:$('#curNodeName').val(),
					eventHandleInterval:$('#eventHandleInterval').val()
				},
				dataType:"json",
				success:function(data) {
				    
				    modleclose();
				    closeIframe();
				    if("${messageUrl!''}"){
						window.parent.postMessage('eventDetailClose()',"${messageUrl!''}");
						window.parent.postMessage('flashTodoList()',"${messageUrl!''}");
				    }else{
						window.parent.postMessage('eventDetailClose()','${GIS_DOMAIN!""}/gis/jiangYinPlatform/index.jhtml');
						window.parent.postMessage('flashTodoList()','${GIS_DOMAIN!""}/gis/jiangYinPlatform/index.jhtml');
				    }
				    
				}
				
		});
				
	}
	
	
	function rejectTask(){
	
	    
            if(!$('#adviceText').val()){
                $.messager.alert("警告", "请填写驳回意见", "info");
                return;
            }
      
        
	
	
	    $.messager.confirm('驳回提示', '确定要驳回吗？流程将返回上一操作步骤！', function(r) {
	    
			if (r) {
			    modleopen();
				$.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/rejectInstance.jhtml",
				data:{
					advice : $('#adviceText').val(),
					instanceId:$('#instanceId').val(),
					taskId:$('#curTaskId').val()
				},
				dataType:"json",
				success:function(data) {
				    
				    modleclose();
				    closeIframe();
				    if("${messageUrl!''}"){
						window.parent.postMessage('eventDetailClose()',"${messageUrl!''}");
						window.parent.postMessage('flashTodoList()',"${messageUrl!''}");
					}else{
						window.parent.postMessage('eventDetailClose()','${GIS_DOMAIN!""}/gis/jiangYinPlatform/index.jhtml');
						window.parent.postMessage('flashTodoList()','${GIS_DOMAIN!""}/gis/jiangYinPlatform/index.jhtml');
					}
				}
				
		        });
			}
		});
	
	
	}
			//办理相关end
			
	$(function(){
        $('#tabdiv').on('click','a',function(){
            $(this).addClass('active').siblings().removeClass('active');
             $('#tabdivSub>div').eq($(this).index()).addClass('show').siblings().removeClass('show');
          var showIndex = $(this).index()
            if(showIndex==1){
            	$('.nicescroll-cursors').hide();
            }else{
            	$('.nicescroll-cursors').show();
            	 $('.h-x').niceScroll().resize();
            }    
        });
	});		
	//督办记录获取初始化

	
	//获取督办记录
	function fetchRemindInfo() {   
        $.ajax({
            type: "POST",
            url : '/zhsq_event/zhsq/workflow4Base/findUrgeOrRemindList.jhtml',
            data: {'instanceId': instanceId, 'category': '2'},
            dataType:"json",
            success: function(data){
                var superviseResultList = data.remindMapList;

                if(superviseResultList && superviseResultList.length > 0) {
                    var superviseResultContent = "", supervise = null;
                    $("#dubanIconDiv").show();
                    $("#db_bt").show();
                    

                    for(var index in superviseResultList) {
                        supervise = superviseResultList[index];
                        superviseResultContent += '<p class="aj_items_t aj_items_t_green bs" style="max-width:12rem;margin-top:.1rem">';
                        if(supervise.remindUserName) {
                            superviseResultContent += '	  <span style="color:#00d1fe;">' + supervise.remindUserName + '</span>';
                        }
                        if(supervise.remindDateStr) {
                            superviseResultContent += '	  <span>于 ' + supervise.remindDateStr+ '</span>';
                        }
                        superviseResultContent += '<span style="color: #ffd34c">  督办</span>';
                        superviseResultContent += '<br>';
                        if(supervise.remarks) {
                            superviseResultContent += '			<span>' + supervise.remarks+ '</span>';
                        }
                        superviseResultContent += '</p>';
                    } 
                    
                    $("#remindListContentDiv").html(superviseResultContent);
                }else{
                	 $("#dubanIconDiv").hide();
                	 $("#db_bt").hide();
                }
                
            },
            error:function(data){
                $.messager.alert("错误", "获取督办信息失败！", "error");
            }
        });
    }
			
		</script>
	</body>
</html>
