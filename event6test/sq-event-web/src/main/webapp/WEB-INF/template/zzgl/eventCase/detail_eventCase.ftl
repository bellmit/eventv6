<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>案件信息详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<#include "/component/bigFileUpload.ftl" />

<style type="text/css">
	.DetailEdit .LabName{width:75px;}
	.DetailEdit .inpWidth{width: 89%;}
</style>
	
<#include "/component/ImageView.ftl" />

</head>

<body onload="checkMetterHeight();">
    <div class="MetterList" style="margin:0 auto;">
    	<div id="content-d" class="MC_con content light" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得案件简介能随着滚动条移动，隐藏横向滚动条-->
    		<form id="eventCaseForm" action="" method="post" enctype="multipart/form-data">
    			<input type="hidden" id="caseId" name="caseId" value="<#if eventCase.caseId??>${eventCase.caseId?c}</#if>" />
				<!--防止以下属性被设置为默认值，故而需要携带-->
				<input type="hidden" name="happenTimeStr" value="${eventCase.happenTimeStr!}" />
				<input type="hidden" name="urgencyDegree" value="${eventCase.urgencyDegree!}" />
				<input type="hidden" name="eventInvolvedPeople" value="${eventCase.eventInvolvedPeople!}" />
				
				<input type="hidden" id="isStart" name="isStart" value="" />
				<input type="hidden" id="isClose" name="isClose" value="" />
				
				<!--用于地图-->
				<input type="hidden" id="id" name="id" value="<#if eventCase.caseId??>${eventCase.caseId?c}</#if>" /> 
				<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
				<input type="hidden" id="gridId" name="gridId" value="<#if eventCase.gridId??>${eventCase.gridId?c}</#if>">
				<input type="hidden" id="module" value="${mapModuleCode!}" />
				
	        	<div class="MetterContent" style="margin:0 auto;">
		            <div class="title ListShow" style="background:none; padding-right: 0;">
		            	<div id="contentDiv" class="fl" style="width:610px; height: 310px; position: relative;">
		            		
		            		<div id="MetterBrief" style="border-bottom:1px dotted #cecece;">
		                    	<div id="dubanIconDiv" class="dubanIcon hide"></div>
			                	<ul>
			                    	<li style="word-break: break-all; width:97%; *width:94%;">
			                            <p><#if eventCase.eventClass??>[${eventCase.eventClass}]</#if> <span>${eventCase.eventName!}</span></p>
			                            <p>
			                            	于 <span>${eventCase.happenTimeStr!}</span> 
			                            	在 <span>${eventCase.occurred!} <#if resMarker??><#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/></span></#if>
			                            	<#if eventCase.content??>发生 <span>${eventCase.content!}</span></#if>。
			                            </p>
			                    	</li>
			                    </ul>
		                    </div>
		                    
	                    	<div id="MetterMore" class="ListShow ListShow2" style="word-break: break-all; border: none;">
		                    	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
				                	<tr>
				                		<td width="60px;" align="right" >信息来源：</td>
				                		<td><code>${eventCase.sourceName!}</code></td>
	            						<td width="13%" align="right" >采集渠道：</td>
	            						<td><code>${eventCase.collectWayName!}</code></td>
	            						<td width="13%" style="text-align:right;">紧急程度：</td>
	            						<td>
		            						<#if eventCase.urgencyDegree?? && eventCase.urgencyDegree!='01'>
												<code style="color:red">${eventCase.urgencyDegreeName!}</code>
											<#else> 
												<code>${eventCase.urgencyDegreeName!}</code>
											</#if>
				                		</td>
				                	</tr>
				                	<tr>
				                		<td align="right" >影响范围：</td>
				                		<td><code>${eventCase.influenceDegreeName!}</code></td>
	            						<td align="right">联系人员：</td>
	            						<td colspan="3">
	            							<code>
		            							${eventCase.contactUser!}
			            						<#if eventCase.tel??>
			            							(${eventCase.tel})
				                        		</#if>
			                        		</code>
				                		</td>
				                	</tr>
				                	<tr>
	                					<td align="right" >当前状态：</td>
	                					<td width="18%"><code>${eventCase.statusName!}</code></td>
		            					<td align="right" >案件编号：</td>
		            					<td><code>${eventCase.code!}</code></td>
		            					<#if eventCase.handleDateStr??>
		            						<td align="right" >办理时限：</td>
		            						<td><code>${eventCase.handleDateStr!}</code></td>
		            					<#else>
		            						<td></td>
		            						<td></td>
		            					</#if>
				                	</tr>
				                	<tr>
				                		<td align="right" >涉及人员：</td>
	        							<td colspan="5">
	        								<code><#if eventCase.involvedNumName??>(<b>${eventCase.involvedNumName}</b>)</#if>${eventCase.involvedPersonName!}</code>
				                		</td>
				                	</tr>
				                	<tr class="DotLine">
				                		<td align="right" >所属网格：</td>
	        							<td colspan="5">
	        								<code>${eventCase.gridPath!}</code>
				                		</td>
				                	</tr>
				                	
				                	<#if curTaskName??>
					                	<tr>
					                		<td align="right" >当前环节：</td>
					                		<td colspan="5">
	            								<code>${curTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code>
					                		</td>
					                	</tr>
				                	</#if>
				                </table>
			                </div>
			                
		                </div>
		                
		                
		                <div id="slider" class="fr" style="width:300px; height:180px; border-left:1px solid #cecece;">
	                		<ul></ul>
		                </div>
		            	<div class="clear"></div>
		            </div>
					
					<div id="adviceDiv" class="ListShow hide" style="padding: 0;">
						<div class="NorForm DetailEdit">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="LeftTd">
										<label class="LabName"><span style="text-align: left; color: #000; padding-left: 10px;">办理意见：</span></label><textarea rows="3" style="height:80px;" id="closeAdvice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div id="fileUploadDiv" class="ListShow hide" style="padding: 0;">
						<div class="NorForm DetailEdit">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" class="LeftTd">
										<label class="LabName"><span style="text-align: left; color: #000; padding-left: 10px;">图片上传：</span></label><div id="bigFileUploadDiv"></div>
									</td>
								</tr>
								<tr id="handleDateIntervalTr" class="hide">
									<td colspan="2" class="LeftTd">
										<label class="LabName"><span style="text-align: left; color: #000; padding-left: 10px;">处理时限：</span></label><input type="text" class="inp1 easyui-validatebox" data-options="validType:'numLength[2]', tipPosition:'bottom'" style="width: 10%;" id="handleDateInterval" name="handleDateInterval" value="" /><label class="LabName" style="float: none; color: #000; display: inline-block; width: 30px;">（天）</label>
									</td>
								</tr>
							</table>
						</div>
					</div>
					
	            </div>
            </form>
            
            <#if isCurHandler?? && isCurHandler>
				<div class="h_20"></div>
				<div style="padding: 0 20px 0 20px;">
					<#include "/zzgl/event/workflow/handle_node_base.ftl" />
				</div>
			</#if>
			
			<div id="workflowDetailDiv" class="hide" style="padding: 0 20px 0 20px;">
				<div class="h_20"></div>
				<div class="ConList">
					<div class="nav" id="tab">
						<ul>
							<li id="01_li" class="current">处理环节</li>
							<li id="02_li" class="hide">评价列表</li>
							<li id="03_li" class="dubanNav hide">督办记录</li>
						</ul>
					</div>
					<div class="ListShow ListShow2">
						<div id="01_li_div" class="t_a_b_s">
							<div id="workflowDetail" border="false"></div>
						</div>
						
						<div id="02_li_div" class="t_a_b_s hide">
							<div class="tabss">
								<div id="evaResultContent" class="appraise"></div>
							</div>
						</div>
						
						<div id="03_li_div" class="t_a_b_s hide">
							<div class="tabss">
								<div id="superviseResultContent" class="appraise"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
        </div>
    </div>
	                
	<#if instanceId??>
	<#elseif listType?? && listType==1>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="showAdvice(false);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
        		<a href="###" onclick="showAdvice(true);" class="BigNorToolBtn BigJieAnBtn">结案</a>
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    <#else>
    	<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    </#if>

<script type="text/javascript">
	var _winHeight = 0,
		_winWidth = 0,
		downPath = "${IMG_URL!}",//图片幻灯片展示使用
		contextPath = "${rc.getContextPath()}",
		basWorkSubTaskCallback = null;
	
	$(function(){
		var caseId = $("#caseId").val();
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		getImages(caseId, '${attachmentType!}');
		
		//390为：为图片展示预留的宽度；
		var width = _winWidth - 390;
		var options = {
			axis : "yx", 
			theme : "minimal-dark" 
		};
		
		$("#contentDiv").width(width);
		
		enableScrollBar('MetterBrief',options);
		enableScrollBar('MetterMore',options);
		
		showWorkflowDetail();
		
		<#if isCurHandler?? && isCurHandler>
			$("#fileUploadDiv").show();
			
			var baseWorkOption = BaseWorkflowNodeHandle.initParam();//获取默认的设置
			
			basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
			
			BaseWorkflowNodeHandle.initParam({
				subTask: {
					subTaskUrl: '${rc.getContextPath()}/zhsq/eventCase/subWorkflow4Case.jhtml',
					subTaskCallback: eventCaseSubTask
				},
				reject: {
					rejectUrl: '${rc.getContextPath()}/zhsq/eventCase/rejectWorkflow4Case.jhtml'
				},
				evaluate: {
					isShowEva: true
				},
				checkRadio: {
					radioCheckCallback: radioCheckCallback
				}
			});
			
			var bigFileUploadOpt = {
				useType			: 'edit',
				file_types		: '.jpg,.gif,.png,.jpeg',
				module			: 'zhsq_event_case',
				styleType		: 'list',
				attachmentData	: {bizId: caseId, attachmentType:'${attachmentType!}', eventSeq: '1,2,3', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				}
			};
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		</#if>
		
		var lis = $("#tab").find("li");
		lis.each(function() {
			$(this).bind("click", function() {
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
			});
		});
		
		$("#closeAdvice").width($(window).width() * 0.85);
		
		fetchEventExtraAttr();
	});
	
	function showWorkflowDetail() {
		var instanceId = "<#if instanceId??>${instanceId?c}</#if>";
		if(instanceId) {
			$("#workflowDetailDiv").show();
			
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
				}
			});
		}
	}
		
	function checkMetterHeight() {//需要在页面渲染完成后，执行，因为"MetterMore > table"的高度会因为折行效果发生变化
		var moreTableDefault = 210;
		var moreTableHeight = $("#MetterMore table").height();//由于添加的滚动条，因此table不再是MetterMore的直接下级
		var briefContentHeight = $("#MetterBrief ul > li").height();
		var briefHeight = 0;
		var dubanIcon = $(".dubanIcon").length;
		
		if(moreTableHeight > moreTableDefault) {
			moreTableHeight = moreTableDefault;
		}
		
		briefHeight = $("#contentDiv").height() - moreTableHeight - 1 - 1;//1为底部界限边距，共两条
		
		if(briefContentHeight < briefHeight && dubanIcon > 0) {//防止内容不足时，导致督办图标显示不完全
			$("#MetterBrief ul > li").height(briefHeight);
		}
		
		$("#MetterMore").height(moreTableHeight);
		$("#MetterBrief").height(briefHeight);
		
		//显示设置width样式，为了防止菜单页签切换时，宽度被修改
		$("#MetterBrief ul").width($("#MetterBrief").width());
		$("#MetterMore table").width($("#MetterMore").width());
	}
	
	function showAdvice(isClose) {
		$("#isStart").val(true);
		$("#isClose").val(isClose);
		
		if(isClose && isClose == true) {
			$('#closeAdvice').validatebox({
				required: true
			});
			
			$("#adviceDiv").show();
		} else {
			$("#adviceDiv").hide();
			
			$('#closeAdvice').validatebox({
				required: false
			});
			
			$('#closeAdvice').val("");
		}
		
		if($("#eventCaseForm").form('validate')) {
			startWorkflow(isClose);
		}
	}
	
	function startWorkflow(isClose) {
		var caseId = $("#caseId").val();
		
		if(caseId) {
			$("#eventCaseForm").attr("action", "${rc.getContextPath()}/zhsq/eventCase/startWorkflow4Case.jhtml");
	      	
	      	modleopen();
		  	$("#eventCaseForm").ajaxSubmit(function(data) {
		  		modleclose();
		  	
		  		if(data.success && data.success == true) {
		  			if(isClose) {
		  				parent.reloadDataForSubPage(data.tipMsg, true);
		  			} else {
		  				parent.searchData();
		  				parent.detail(caseId, "2");
		  				if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
		  					parent.closeBeforeMaxJqueryWindow();
		  				}
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
		
	function closeDetailWin() {
		parent.closeMaxJqueryWindow();
	}
	
	function flashData(msg) {//工作办理回调
		parent.reloadDataForSubPage(msg, true);
	}
		
	function showMap(){
		var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
			width = 480,
			height = 360,
			gridId = $("#gridId").val(),
			markerOperation = $('#markerOperation').val(),
			mapType = $("#module").val(),
			isEdit = false;
		
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
	}
	
	function showMix(fieldId, index) {//幻灯片点击事件
		ffcs_viewImg_win(fieldId, index);
	}

	function fetchEventExtraAttr() {
		$.ajax({
			type: "POST",
			url : '${rc.getContextPath()}/zhsq/eventCase/fetchEventExtraAttr.jhtml',
			data: {'caseId': $("#caseId").val()},
			dataType:"json",
			success: function(data){
				var evaResultList = data.evaResultList,
					superviseResultList = data.superviseResultList;
				
				if(evaResultList && evaResultList.length > 0) {
					var evaResultContent = "";
					$("#02_li").show();
					
					for(var index in evaResultList) {
						var evaItem = evaResultList[index];
						
						evaResultContent += '<div class="list">';
                        evaResultContent += '	<span>';
                        evaResultContent += '		<p>';
                        
                        if(evaItem.creatorName) {
                        	evaResultContent += '		<em class="FontDarkBlue">' + evaItem.creatorName + '</em>';
                        }
                        if(evaItem.createDateStr) {
                        	evaResultContent += '		于 ' + evaItem.createDateStr;
                        }
                        if(evaItem.evaLevelName) {
                        	evaResultContent += '		评价 <b class="FontRed">' + evaItem.evaLevelName + '</b>';
                        }
                        evaResultContent += '		</p>';
                        if(evaItem.evaContent) {
                        	evaResultContent += '	<p>' + evaItem.evaContent + '</p>';
                        }
                        evaResultContent += '	 </span>';
                        evaResultContent += '</div>';
					}
					
					$("#evaResultContent").html(evaResultContent);
				}
				
				if(superviseResultList && superviseResultList.length > 0) {
					var superviseResultContent = "";
					$("#03_li").show();
					$("#dubanIconDiv").show();
					
					for(var index in superviseResultList) {
						var supervise = superviseResultList[index];
						
						superviseResultContent += '<div class="list">';
                        superviseResultContent += '    <span>';
                        superviseResultContent += '        <p>';
                        if(supervise.remindUserName) {
                        	superviseResultContent += '			<em class="FontDarkBlue">' + supervise.remindUserName + '</em>';
                        }
                        if(supervise.remindDate) {
                        	superviseResultContent += '			于 ' + supervise.remindDateStr;
                        }
                        superviseResultContent += '				<b class="FontRed">督办</b>';
                        superviseResultContent += '			</p>';
                        if(supervise.remarks) {
                        	superviseResultContent += '			' + supervise.remarks;
                        }
                        superviseResultContent += '    </span>';
                        superviseResultContent += '</div>';
					}
					
					$("#superviseResultContent").html(superviseResultContent);
				}
			},
			error:function(data){
				$.messager.alert("错误", "获取案件额外信息失败！", "error");
			}
		});
	}
	
	function eventCaseSubTask() {//点击提交按钮调用方法
		if($("#handleDateIntervalTr").is(":visible")) {
			var isValid = $('#eventCaseForm').form('validate');
			
			if(isValid) {
				$("#eventCaseForm").attr("action", "${rc.getContextPath()}/zhsq/eventCase/saveEventCase.jhtml");
				
				$("#eventCaseForm").ajaxSubmit(function(data) {
				    var result = data.success,
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
		var curNodeName = "${curNodeName!}",
			CASE_CENTER_NODE = "task3",
			DEFAULT_INTERVAL = "2";
		
		if(CASE_CENTER_NODE == curNodeName) {
			$("#handleDateInterval").val(DEFAULT_INTERVAL);
			$("#handleDateIntervalTr").show();
		} else {
			$("#handleDateIntervalTr").hide();
			$("#handleDateInterval").val("");
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
</body>
</html>
