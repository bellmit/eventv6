<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件信息详情</title>
	<link href="${rc.getContextPath()}/css/normal.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/css/add_people.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/css/jquery.mCustomScrollbar.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
	<!-- custom scrollbar plugin -->
	<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
	<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
	<script src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
	<#include "/component/ImageView.ftl" />
	<style type="text/css">
		.overtime{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default;}
		.overtime:hover{background:#e84c3d;}
	</style>
	
	<script>
		(function($){
			$(window).load(function(){
				$("#mainDiv").height($(window).height());
				$("#content-md").height($("#mainDiv").height() - $("#btnDiv").outerHeight());
				
				$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
				$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
				$("#content-d").mCustomScrollbar({theme:"dark"});
				$("#content-md").mCustomScrollbar({theme:"minimal-dark"});
			});
			<#if modeType?? && modeType == 'fuzhou'>
			if(parent.showResources) {
				parent.showResources('${event.resMarker.x!}', '${event.resMarker.y!}');
			}
			</#if>
		})(jQuery);
	</script>
	
	<script type="text/javascript">
		var $model = "${model}";
		$(function(){
			var eventName = "${event.eventName}";
			if(eventName.length > 10){
				eventName = eventName.substring(0,10) + '...';
			}
			$("#eventName").html(eventName);
			
			getImgs();
			//getFirstImgs();
			//getLastImgs();
			var h = 0;
			if ($(".BigTool").length != 0) {
				h = $(".BigTool").height();
			}
			setInterval(function(){
				var height = $(window).height();
				$(".MetterWatch").css("height", height- h);
			}, 3);
			
		});
		
// 		function ffcs_viewImg(fieldId,index){
// 			parent.ffcs_viewImg(fieldId,index);
// 		}
		
		var paths;
		// 轮播图片
		function getImgs(){
			var isUploadHandlingPic = <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>;
			var url = '${rc.getContextPath()}/zhsq/att/getList.jhtml';
			var dataEventSeq = "1,3";
			if(isUploadHandlingPic){
				dataEventSeq = "1,2,3";
			}
			var data = {'bizId':${event.eventId?c},'attachmentType':'${EVENT_ATTACHMENT_TYPE!}','eventSeq':dataEventSeq};
			
			$.post(url,data,function(result) {
                if(result.length > 0) {
                    var firstNum = 0, 
                    	  sliderImgDiv = $("#firstImgs ul"),
                    	  suffixStartIndex = -1,
                    	  imgSuffix = '',
                    	  imgStr = "png,jpg,gif,jpeg,webp",
                    	  audioStr = "amr,mp3",
                    	  videoStr = "mp4",
                    	  fileShowSrc = "",
                    	  width = 0,
                    	  height = 0,
                    	  imageLi = '',
                    	  arr = new Array(),
                    	  imgSrc = '',
                    	  fileTitleName = "",
                    	  eventSeq = '',
                    	  imgFileIndex = 0;
                    	  rootPath = '${rc.getContextPath()}',
                    	  downPath = '${downPath!}';

                    $(".MetterPic").show();

                    for (var i = 0, len = result.length; i < len; i++) {
                        imageLi = '';
                        fileTitleName = "";
                        width = result[i].imgWidth;
                        height = result[i].imgHeight;
                        imgSrc = downPath + result[i].filePath,
                        eventSeq = result[i].eventSeq;
                        suffixStartIndex = imgSrc.lastIndexOf('.');

                        if (suffixStartIndex >= 0) {
                            imgSuffix = imgSrc.substr(suffixStartIndex + 1).toLowerCase();
                        }

                        if (!fileTitleName) {
                            switch (eventSeq) {
                                case '1': {
                                    fileTitleName = "处理前";
                                    break;
                                }
                                case '2': {
                                    fileTitleName = "处理中";
                                    break;
                                }
                                case '3': {
                                    fileTitleName = "处理后";
                                    break;
                                }
                            }
                        }

                        if (audioStr.indexOf(imgSuffix) >= 0 || videoStr.indexOf(imgSuffix) >= 0) {
                            fileShowSrc = rootPath + '/scripts/updown/swfupload/images/thumbnail/audio.jpg';
                            var downloadUrl = rootPath + '/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId=' + result[i].attachmentId,
                                    titleName = '音频';

                            if (videoStr.indexOf(imgSuffix) >= 0) {
                                titleName = '视频';
                            }

                            fileTitleName += titleName;

                            imageLi = '<li><a style="cursor:pointer;" title="点击播放' + titleName + '" target="_blank" href="' + downloadUrl + '"><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,150,this)" alt="' + fileTitleName + '" src="' + fileShowSrc + '" /></a></li>';

                        } else if (imgStr.indexOf(imgSuffix) >= 0) {//只有图片才展示
                            var imageSrc = imgSrc;
                            
                            fileTitleName += '图片';
                            
                            if(imageSrc.indexOf('?') < 0) {
                            	imageSrc += '?t=' + Math.random();
                            }
					
                            imageLi = '<li><a style="cursor:pointer;" title="点击放大图片" onclick=ffcs_viewImg(\'playImg\',' + (imgFileIndex++)  + ')><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,150,this)" alt="' + fileTitleName + '" src="' + imageSrc + '" /></a></li>';
                        } else {
							var downloadUrl = rootPath + '/upFileServlet?method=down&attachmentId=' + result[i].attachmentId,
								  titleName = result[i].fileName || '';
							
							fileTitleName += '附件';
							fileShowSrc = rootPath + '/scripts/updown/swfupload/images/thumbnail/default.png';
							
							imageLi = '<li><a style="cursor:pointer;" title="点击下载 '+ titleName +'" target="_blank" href="'+ downloadUrl +'"><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,150,this)" alt="'+ fileTitleName +'" src="'+fileShowSrc+'" /></a></li>';
						}

                        if (imageLi) {
                        	if(imgStr.indexOf(imgSuffix) >= 0) {
	                            arr.push(imgSrc);
                            }
                            
                            sliderImgDiv.append(imageLi);
                        }
                    }

                    paths = arr;
                } else {
					$(".MetterPic").show();
					$(".MetterPic").html('<img src="${rc.getContextPath()}/images/zanwutupian.png"/>');
				}
				initNbspSlider();
			},"json");
		}
		
		$(document).ready(function() {
			var lis = $("#tab").find("li");
			lis.each(function() {
				$(this).bind("click", function() {
					if (typeof $(this).attr("id") != "undefined") {
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
					} else {
						showWorkFlowPic();
					}
				});
			});
			<#if instanceId??>
				var h = $("#formInfo").height();
				$("#02_li_div").height(h);
				$("#03_li_div").height(h);
			</#if>
		});
	
	</script>
	
	<script type="text/javascript">
		//初始化图片轮播
		function initNbspSlider(){
			$("#firstImgs").nbspSlider({
				widths:         "320px",        // 幻灯片宽度
				heights:        "150px",
				effect:	         "vertical",
				numBtnSty:       "square",
				speeds:          300,
				autoplay:       1,
				delays:         4000,
				preNexBtnShow:   0,
				altOpa:         0.5,            // ALT区块透明度
				altBgColor:     '#ccc',         // ALT区块背景颜色
				altHeight:      '20px',         // ALT区块高度
				altShow:         1,             // ALT区块是否显示(1为是0为否)
				altFontColor:    '#000',        // ALT区块内的字体颜色
				prevId: 		'prevBtn',      // 上一张幻灯片按钮ID
				nextId: 		'nextBtn'		// 下一张幻灯片按钮I
			});
		}
	</script>
<style type="text/css">

</style>
</head>

<body>
	<div id="mainDiv" class="zhoubiantongji">
	    <div class="content light" id="content-md" style="height: 190px;word-break: break-all;">
			<div class="MetterInfo"><#if superviseResultList?? && (superviseResultList?size>0)><div class="dubanIcon" style="top:0px;"></div></#if>
				<ul>
		        	<li>
		        		<#if event.urgencyDegree?? &&event.urgencyDegree!='01'>
		            		<b class="FontRed">[紧急]</b> 
		            	</#if>
		            	<#if event.urgencyDegree?? &&event.urgencyDegree=='02'>
		            		<b class="FontDarkBlue">[突发事件]</b> 
		            	</#if>
		        		<b id="eventName" title="${event.eventName}"></b>
		        	</li>
		            <li>于 <span class="FontDarkBlue">${event.happenTimeStr}</span>，在 <span class="FontDarkBlue"><#if event.occurred??>${event.occurred}</#if></span>
		            	<#if !modeType?? || modeType != 'fuzhou'>
			            	<#if event.resMarker.x?? && event.resMarker.y?? && event.resMarker.mapType?? && mapEngineType != '006'>
			            		<span class="resource" title="点击查看周边资源" onclick="searchZhouBian();">周边资源</span>
			            	</#if>
		            	</#if>
		            	 发生 <span class="FontDarkBlue">${event.content!}</span></li>
		            <li>目前状态：<span class="FontDarkBlue"><#if event.statusName??>${event.statusName}</#if></span></li>
		            <#if isHandle?? && handleTime?? && isHandle && stepRemainTime??><li>处理时限：<span class="FontDarkBlue">${handleTime}</span><#if (stepRemainTime?length>3) && (stepRemainTime?substring(0,2)=='超时')><span class="overtime" title="${stepRemainTime}">超时</span></#if></li></#if>
		            <#if event.handleDateStr?? && eventRemainTime??><li style="display:none;">事件处理时限：<span class="FontDarkBlue">${event.handleDateStr}</span><span>(${eventRemainTime})</span></li></#if>
		            <li style="display:none;"><#if curNodeTaskName??>当前环节：<span class="FontDarkBlue">${curNodeTaskName}</span></#if></li>
		            <li style="display:none;"><#if taskPersonStr??>当前办理人：<span class="FontDarkBlue">${taskPersonStr}</span></#if></li>
		        </ul>
		    </div>
		    <div class="MetterPic" style="display:none;">
		    	<p>事件处理照片：</p>
		        <div id="firstImgs">
					<ul>
		            </ul>
				</div>
		    </div>
		</div>
		
		<div id="btnDiv" class="btn">
			<input type="button" class="NorBtn" value="事件办理" onclick="showEventDetail();" />
			<input type="button" class="NorBtn <#if isShowSendMsg?? && isShowSendMsg><#else>hide</#if>" value="一键群发" onclick="sentMessage();"/>
		</div>
	</div>
<script type="text/javascript">
	<#if instanceId??>
		function onHandle() {
			var url = "${SQ_ZZGRID_URL}/zzgl/flowService/jumpPage.jhtml?instanceId=${instanceId}&workFlowId=${workFlowId!''}&formId=${event.eventId?c}&cachenum=" + Math.random()+"&eventType=event";
			//showCustomEasyWindow("办理窗口", 650, 350, url);
			showMaxJqueryWindow("办理窗口", url);
		}
		
		function showWorkFlowPic() {
			var url = "${workflowCtx}/flowDetail.shtml?method=flowChart&instanceId=${instanceId}&taskId=${taskId}";
			showMaxJqueryWindow("流程图窗口", url, null, null, null, "auto");
		}
	</#if>
	
	function startWorkFlow(toClose){
		modleopen();
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
			data: 'eventId=${event.eventId?c}'+'&toClose='+toClose,
			dataType:"json",
			success: function(data){
				parent.startWorkFlow(data);
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
					alert("已经是当前页的第一条");
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
					alert("已经是当前页的最后一条");
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
	function searchZhouBian() {
		var mapt=window.parent.currentArcgisConfigInfo.mapType;
		if(mapt != '') {
			var x=0;
			var y=0;
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/getEventGisDataByMapt.json',
				data:{eventId:'${event.eventId?c}', mapt:mapt},
				dataType:"json",
				async:false,
				success: function(data){
					if(data.x != undefined && data.x !=0) {
						x=data.x;
						y=data.y;
					}else {
						$.messager.alert('错误','无法获取地图点位！','error');
					}
				},
				error:function(data){
					$.messager.alert('错误','无法获取地图引擎！','error');
				}
			});
			window.parent.toSearchZhouBian(x,y);
		}
		
	}

	function ffcs_viewImg(fieldId, index){
		var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId + "&index="+index+"&paths="+paths+"&titles=";
		var name = "图片查看";
		
		window.open(url,name);
	}

	function openPostWindow(url, data, name){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target=name;
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow(name);
		});
//			tempForm.attachEvent("onsubmit",function(){
//				openWindow(name);
//			});
		document.body.appendChild(tempForm);
		//tempForm.fireEvent("onsubmit");
		tempForm.submit();
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}
	
	function showEventDetail() {
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId=${instanceId!}&eventId=${event.eventId?c}&cachenum=" + Math.random();
		
		parent.showMaxJqueryWindow("事件信息", url, 960, 470, true);
	}
	
	function sentMessage() {
		var msgContent = '${event.content!}',
			successLen = 0,
			failLen = 0,
			sendTypeArrayLen = 0,
			userIds = [],
			phoneNums = [],
			userIdArray = [],
			phoneNumArray = [],
			sendTypeArray = [];
		
		if(parent.getMsgs) {
			var msgs = parent.getMsgs();
			userIdArray = msgs.msgUserIds || [];
			phoneNumArray = msgs.msgTels || [];
		}
		
		if(userIdArray.length > 0) {
			$(userIdArray).each(function(index, userId) {
				if(userId) {
					userIds.push(userId);
				}
			});
			
			if(userIds.length > 0) {
				sendTypeArray.push("0");
				sendTypeArray.push("2");
			}
		}
		if(phoneNumArray.length > 0) {
			$(phoneNumArray).each(function(index, phoneNum) {
				if(phoneNum && isMobile(phoneNum)) {//手机号码才发送短信
					phoneNums.push(phoneNum);
				}
			});
			
			if(phoneNums.length > 0) {
				sendTypeArray.push("1");
			}
		}
		if(msgContent.length > 500) {//防止消息内容过长
			msgContent = msgContent.substring(0, 500);
		}
		
		sendTypeArrayLen = sendTypeArray.length
		
		if(sendTypeArrayLen > 0) {
			modleopen();
		} else {
			$.messager.alert('警告', '请选择消息接收对象！', 'warning');
		}
		
		for(var index = 0; index < sendTypeArrayLen; index++) {
			var sendType = sendTypeArray[index];
			
			switch(sendType) {
				case "0": {//发送站内消息
					$.ajax({
						type : 'POST',
						dataType : "jsonp",
						data: {'msgContent': encodeURIComponent(msgContent), 'userIds': userIds.toString(), 'moduleCode': '99', 'bizType': '${bizType!"00"}'},
						url : "${ANOLE_COMPONENT_URL}/system/uam/newmessage/addMsg4Jsonp.json?jsoncallback=?&t="+Math.random(),
						success: function(data) {
							if(data.result == "1") {
								successLen++;
							} else {
								failLen++;
								
								if(data.msg && false) {
									$.messager.alert('错误', '站内消息发送失败：'+data.msg, 'error');
								}
							}
							
							if((successLen + failLen) == sendTypeArrayLen) {
								modleclose();
								
								if(successLen > 0) {//一种方式成功即发送成功
									$.messager.alert('提示', '消息发送成功!', 'info');
								} else {
									$.messager.alert('提示', '消息发送失败!', 'info');
								}
							}
						},
						error : function() {
							failLen++;
						}
					});
					
					break;
				}
				case "1": {//发送短信
					$.ajax({
						type : 'POST',
						dataType : "json",
						data: {'msgContent': msgContent, 'phoneNums': phoneNums.toString(), 'sendType': 1},
						url : "${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/sendSms.json?t="+Math.random(),
						success: function(data) {
							if(data.result == true) {
								successLen++;
							} else {
								failLen++;
								
								if(data.msg && false) {
									$.messager.alert('错误', '短信发送失败：' + data.msg, 'error');
								}
							}
							
							if((successLen + failLen) == sendTypeArrayLen) {
								modleclose();
								
								if(successLen > 0) {
									$.messager.alert('提示', '消息发送成功!', 'info');
								} else {
									$.messager.alert('提示', '消息发送失败!', 'info');
								}
							}
						},
						error : function() {
							failLen++;
						}
					});
					
					break;
				}
				case "2": {//推送消息到手机端
					$.ajax({
						type : 'POST',
						dataType : "json",
						data: {'msgContent': msgContent, 'userIds': userIds.toString(), 'msgUrl': 'cn.ffcs.wisdom.sqxxh.module.eventflow.activity.EventMgrDetail&instanceId=${instanceId!}&eventId=${event.eventId?c}'},
						url : "${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/pushMsg2Mobile.json?t="+Math.random(),
						success: function(data) {
							data = new Function("return (" + data + ")")();
							
							if(data.result == true) {
								successLen++;
							} else {
								failLen++;
								
								if(data.msg && false) {
									$.messager.alert('错误', '手机消息推送失败：' + data.msg, 'error');
								}
							}
							
							if((successLen + failLen) == sendTypeArrayLen) {
								modleclose();
								
								if(successLen > 0) {
									$.messager.alert('提示', '消息发送成功!', 'info');
								} else {
									$.messager.alert('提示', '消息发送失败!', 'info');
								}
							}
						},
						error : function() {
							failLen++;
						}
					});
				}
				
			}
			
		}
	}
</script>
</body>
</html>
