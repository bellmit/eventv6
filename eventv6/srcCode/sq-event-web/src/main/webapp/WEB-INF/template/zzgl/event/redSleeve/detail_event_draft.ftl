<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件信息详情</title>
	<link href="${rc.getContextPath()}/css/normal.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/css/add_people.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/css/jquery.mCustomScrollbar.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
	<!-- custom scrollbar plugin -->
	<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/fbsource/jquery.fancybox.js?v=2.1.5"></script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
	
	<script>
		(function($){
			$(window).load(function(){
				
				$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
				$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
				
				$("#content-d").mCustomScrollbar({theme:"dark"});
			});
		})(jQuery);
	</script>
	
	<script type="text/javascript">
		var $model = "${model}";
		$(function(){
			getFirstImgs();
			getLastImgs();
			var h = 0;
			if ($(".BigTool").length != 0) {
				h = $(".BigTool").height();
			}
			setInterval(function(){
				var height = $(window).height();
				$(".MetterList .box").css("height", height- 20 - h);
			}, 3);
		});
		
		<!--处理前图片-->
		function getFirstImgs(){
			var url = '${rc.getContextPath()}/zhsq/att/getList.jhtml';
			var data ={'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':1};
			var firstImgDiv = $("#firstImgs");
			$.post(url,data,function(result){
				if (result.length>0) {
					$("#firstImgNum").html(result.length);
					for(var i= 0; i<result.length;i++) {
						var filePath = result[i].filePath;
						var fileName =result[i].fileName;
						var img = '<a class="fancybox" href="${downPath}'+filePath+'" data-fancybox-group="gallery" ><img class="pic" src="${downPath}'+filePath+'" /></a>';
						firstImgDiv.append(img);
					}
					firstImgDiv.find('.fancybox').fancybox();
					firstImgDiv.prev().bind("click", function() {
						firstImgDiv.find('.fancybox').eq(0).click();
					});
				}else{
					$("#firstImgCode").attr("style", "display:none");
					$("#firstImgCodeNone").removeAttr("style");
				}
			},"json");
		}
		
		<!--处理后图片-->
		function getLastImgs(){
			var url = '${rc.getContextPath()}/zhsq/att/getList.jhtml';
			var data ={'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':3};
			var lastImgDiv = $("#lastImgs");
			$.post(url,data,function(result){
				if (result.length>0) {
					$("#lastImgNum").html(result.length);
					for(var i= 0; i<result.length;i++) {
						var filePath = result[i].filePath;
						var fileName =result[i].fileName;
						var img = '<a class="fancybox" href="${downPath}'+filePath+'" data-fancybox-group="gallery" ><img class="pic" src="${downPath}'+filePath+'" /></a>';
						lastImgDiv.append(img);
					}
					lastImgDiv.find('.fancybox').fancybox();
					lastImgDiv.prev().bind("click", function() {
						lastImgDiv.find('.fancybox').eq(0).click();
					});
				}else{
					$("#lastImgCode").attr("style", "display:none");
					$("#lastImgCodeNone").removeAttr("style");
				}
			},"json");
		}
	</script>
</head>

<body>
    <div class="MetterList">
    	<div id="content-d" class="box content light" style="height:433px;">
        	<div class="MetterContent">
            	<div class="title">
                	<div class="PreNext">
                    	<ul>
                        	<li title="上一条" onclick="prevEvent('${event.eventId?c}');" style="line-height:0; border-right:1px solid #77a70f;"><img src="${rc.getContextPath()}/images/pre2.png" /></li>
                        	<li title="下一条" onclick="nextEvent('${event.eventId?c}');" style="line-height:0; border-left:1px solid #b1d563;"><img src="${rc.getContextPath()}/images/next2.png" /></li>
                        </ul>
                    </div>
                	<ul>
                    	<#if event.urgencyDegree?? &&event.urgencyDegree=='02'>
                    		<li class="level">紧<br />急</li>
                    	</#if>
                    	<li style="padding:10px 0;">
                            <p>[${event.eventClass}] <span>${event.eventName}</span></p>
                            <p>于 <span>${event.happenTimeStr}</span> 在 <span><#if event.occurred??>${event.occurred}</#if></span> 发生 <span>${event.content}</span>。</p>
                            <p>目前状态：<span><#if event.statusName??>${event.statusName}</#if></span><#if handleTime??><span>&nbsp;&nbsp;处理时限：${handleTime}</span></#if></p>
                    	</li>
                    </ul>
                    <div class="clear"></div>
                </div>
                <div class="h_20"></div>
                <div class="ConList">
                    <div class="ListShow ListShow2" style="border-top:1px solid #c5d0dc;">
                        <div id="01_li_div" class="t_a_b_s">
                        	<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr class="DotLine">
                              	<td align="right">事件编号：</td>
                                <td><code><#if event.code??>${event.code}</#if></code></td>
                                <td align="right">网格名称：</td>
                                <td><code><#if event.gridName??>${event.gridName}</#if></code></td>
                                <td align="right">填报人员：</td>
                                <td><code><#if event.contactUser??>${event.contactUser}</#if></code>(<code><#if event.tel??>${event.tel}</#if></code>)</td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">信息来源：</td>
                                <td><code><#if event.sourceName??>${event.sourceName}</#if></code></td>
                                <td align="right">采集渠道：</td>
                                <td><code><#if event.collectWayName??>${event.collectWayName}</#if></code></td>
                                <td align="right">影响范围：</td>
                                <td><code><#if event.influenceDegreeName??>${event.influenceDegreeName}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">紧急程度：</td>
                                <td><code><#if event.urgencyDegree?? &&event.urgencyDegree=='02'>
									<span style="color:red"><#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if></span>
									<#else> <#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>
									</#if></code></td>
								<td align="right">巡防时间：</td>
                                <td><code><#if event.happenTimeStr??>${event.happenTimeStr}</#if></code>～<code><#if event.endTimeStr??>${event.endTimeStr}</#if></code></td>
                                <td align="right">巡防人数：</td>
                                <td><code><#if event.involvedNum??>${event.involvedNumName}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">巡防区域：</td>
                                <td colspan="5">&nbsp;<code style="line-height:22px;"><#if event.occurred??>${event.occurred}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">巡防情况：</td>
                                <td colspan="5">&nbsp;<code style="line-height:22px;"><#if event.content??>${event.content}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">处置情况：</td>
                                <td colspan="5">&nbsp;<code style="line-height:22px;"><#if event.result??>${event.result}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                              	<td align="right" width="80">带队负责：</td>
                                <td colspan="5"><code style="line-height:22px;"><#if event.involvedPersion??>${event.involvedPersion}</#if></code></td>
                              </tr>
                              <tr class="DotLine">
                                <td align="right">处理前图片：</td>
                                <td>
                                	<code id="firstImgCode" class="SeePic">
                                		<a href="###">共 <span id="firstImgNum">0</span> 张图片，点击浏览</a>
                                		<div id="firstImgs" style="display: none;"></div>
                                	</code>
                                	<code id="firstImgCodeNone" class="SeePic" style="display:none;">
		                        		<span>无</span>
		                        	</code>
                                </td>
                                <td align="right">处理后图片：</td>
                                <td>
                                	<code id="lastImgCode" class="SeePic">
                                		<a href="###">共 <span id="lastImgNum">0</span> 张图片，点击浏览</a>
                                	</code>
                                	<div id="lastImgs" style="display: none;"></div>
                                	<code id="lastImgCodeNone" class="SeePic" style="display:none;">
		                        		<span>无</span>
		                        	</code>
                                </td>
                                <td align="right">&nbsp;</td>
                                <td>&nbsp;</td>
                              </tr>
                            </table>
                      </div>
                    </div>
                </div>
            </div>
        </div>
	
		<div class="BigTool">
        	<div class="BtnList" style="width:200px;">
        		<a href="###" class="BigNorToolBtn BlueBtn" onclick="startWorkFlow(1);"><img src="${rc.getContextPath()}/images/finish2.png" />结案</a>
	        	<a href="###" class="BigNorToolBtn BlueBtn" onclick="startWorkFlow(0);"><img src="${rc.getContextPath()}/images/sys1_25.png" />提交</a>
            </div>
        </div>
    </div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">
	function startWorkFlow(toClose){
		modleopen();
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
			data: 'eventId=${event.eventId?c}'+'&toClose='+toClose,
			dataType:"json",
			success: function(data){
				parent.startWorkFlow(data, '0');
			},
			error:function(data){
				$.messager.alert('错误','连接错误！','error');
			}
    	});
	}
	
	function flashData() {
		parent.closeMaxJqueryWindow();
		parent.flashData();
	}
	
	function prevEvent(eventId) {
		if (typeof parent.prevColumn != "undefined") {
			try {
				var url = parent.prevColumn(eventId);
				if (url == "") {
					$.messager.alert("提示","已经是当前页的第一条","info");
				} else {
					$.blockUI({
						message : "加载中...",
						css : {
							width : '180px',
							height : '50px',
							lineHeight : '50px',
							top : $(window).height() / 2 - 25,
							left : $(window).width() / 2 - 90,
							background : 'url(${rc.getContextPath()}/css/loading.gif) no-repeat',
							textIndent : '0'
						},
						overlayCSS : {
							backgroundColor : '#fff'
						}
					});
					window.location.href = url;
				}
			} catch (e) {
				alert(e.message);
			}
		}
	}
	
	function nextEvent(eventId) {
		if (typeof parent.nextColumn != "undefined") {
			try {
				var url = parent.nextColumn(eventId);
				if (url == "") {
					$.messager.alert("提示","已经是当前页的最后一条","info");
				} else {
					$.blockUI({
						message : "加载中...",
						css : {
							width : '180px',
							height : '50px',
							lineHeight : '50px',
							top : $(window).height() / 2 - 25,
							left : $(window).width() / 2 - 90,
							background : 'url(${rc.getContextPath()}/css/loading.gif) no-repeat',
							textIndent : '0'
						},
						overlayCSS : {
							backgroundColor : '#fff'
						}
					});
					window.location.href = url;
				}
			} catch (e) {
				$.messager.alert("错误",e.message,"error");
			}
		}
	}
</script>
</body>
</html>
