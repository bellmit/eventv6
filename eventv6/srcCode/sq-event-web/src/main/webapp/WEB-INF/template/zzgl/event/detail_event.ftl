<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><@block name="eventDetailPageTitle">事件信息详情</@block></title>
	<#include "/component/standard_common_files-1.1.ftl" />
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
    <!--插件如语音盒 使用js-->
    <script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<#--    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />-->
    <script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
    <@block name="eventDetailAdditionalQuote"></@block>
    
    <script>
		<#if eventType=="workStation">
        document.domain = "${domain}";
        var api = frameElement.api, W = api.opener, D = W.document;
		</#if>
		<#if source?? && source = 'workPlatform'>
        document.domain = "${domain}";
		<#elseif source?? && source = 'oldWorkPlatform'>
        //document.domain = "${domain}";
		<#else>
		</#if>
    </script>

    <style type="text/css">
        .DetailEdit .LabName{width:75px;}
        .DetailEdit .inpWidth{width: 89%;}
        #MetterBrief{
        	max-height:130px;
        }
    </style>

<#include "/component/ImageView.ftl" />
    <script type="text/javascript">
        var $model = "${model!}";
        var downPath = "${downPath}";
        var contextPath = "${rc.getContextPath()}";
        var _winHeight = 0;
        var _winWidth = 0;

        $(function(){
            var eventId = $("#eventId").val();
            _winHeight = $(window).height();
            _winWidth = $(window).width();

		<#if msgWrong??>
            $.messager.alert("错误", "${msgWrong}", "error");
		</#if>
		
		<@block name="slideInitFunction4EventDetail">
			getImages(eventId, '${EVENT_ATTACHMENT_TYPE!}');
		</@block>

		<#if instanceId??>
            $("#workflowDetail").panel({
                height:'auto',
                width:'auto',
                overflow:'no',
                href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=${instanceId}",
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
                    
                    <@block name="variableAdjust"></@block>
                    
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

			<#if isHandle?? && isHandle && handleEventPage??>
                //增添任务接收时间
                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/receiveTask.jhtml',
                    data: 'eventId='+eventId+'&instanceId=${instanceId}'+'&taskId=${taskId}',
                    dataType:"json",
                    success: function(data){
                    },
                    error:function(data){
                        $.messager.alert("错误", "任务接收失败！", "error");
                    }
                });
			</#if>
		</#if>

            //390为：为图片展示预留的宽度；
            var width = _winWidth - 390;
            var options = {
                axis : "yx",
                theme : "minimal-dark"
            };

            $("#contentDiv").width(width);

            enableScrollBar('MetterBrief',options);
            enableScrollBar('MetterMore',options);

            fetchEventExtraAttr();
            
            <@block name="additionalInitialization"></@block>
            /*地图控件初始化值判断*/
            <@block name="mapMarkerDiv"></@block>
        });

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
                                var iframeItemList = $("#" + li_id + "_div iframe"),
                                        iframeLen = iframeItemList.length;

                                if(iframeLen == 1) {
                                    var iframeItem = iframeItemList.eq(0),
                                            iframeLoaded = iframeItem.attr("_loadflag");

                                    if(isBlankStringTrim(iframeLoaded)) {
                                        iframeItem.attr("_loadflag", true);//用于防止重复加载
                                        iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
                                    }
                                }

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
        });

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

        function checkWorkFlow(){
            $.ajax({
                type: "POST",
                url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/checkWorkFlow.jhtml',
                data: 'taskId=${taskId}',
                dataType:"json",
                success: function(data){
                    if(data){
                    }else{
                        $("#workflowFrame").remove();
                        $.messager.alert("提示","该事件已经办理！","info");
                    }
                },
                error:function(data){
                    alert(data);
                }
            });
        }

		<#if instanceId??>
        function showWorkFlowPic() {
            var url = "${workflowCtx}/flowDetail.shtml?method=flowChart&instanceId=${instanceId}&taskId=${taskId}";
            try{
                parent.showMaxJqueryWindow("流程图窗口", url, null, null, null, "auto");
            }catch(e){
                showMaxJqueryWindow("流程图窗口", url, null, null, null, "auto");
            }
        }
		</#if>
    </script>
</head>

<body onload="checkMetterHeight();">
<form id="flowSaveForm"	action="" method="post" enctype="multipart/form-data">
    <input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
    <input type="hidden" name="isDetail2Edit" value="<#if isDetail2Edit??>${isDetail2Edit}</#if>" />
    <!--防止以下属性被设置为默认值，故而需要携带-->
    <input type="hidden" name="happenTimeStr" value="${event.happenTimeStr!}" />
    <input type="hidden" name="urgencyDegree" value="${event.urgencyDegree!}" />
    <input type="hidden" name="eventInvolvedPeople" value="${event.eventInvolvedPeople!}" />

    <!--用于地图-->
    <input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" />
    <input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
    <input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">

    <div class="MetterList" style="margin:0 auto;">
        <div id="content-d" class="MC_con content light" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得事件简介能随着滚动条移动，隐藏横向滚动条-->
            <div id="metterContentDiv" class="MetterContent" style="margin:0 auto;">
                <div id="metterContentListShowDiv" class="title ListShow" style="background:none; padding-right: 0;">
                    <div id="contentDiv" class="fl" style="width:610px; height: 310px; position: relative;">

                        <div id="MetterBrief" style="border-bottom:1px dotted #cecece;">
						<@block name="dubanBackGround">
                            <div id="dubanIconDiv" class="dubanIcon hide"></div>
						</@block>
                            <ul>
                                <li style="word-break: break-all; width:97%; *width:94%;">
                                    <p><#if event.eventClass??>[${event.eventClass}]</#if> <span>${event.eventName!}</span></p>
                                    <p>
                                        于 <span>${event.happenTimeStr!}</span>
                                        在<@block name="geographicalLabelingInput"><span>${event.occurred!}<#if !(isDetail2Edit?? && isDetail2Edit)>
<#--                                            <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>-->
                                            <div id="map" style="display:inline"></div>
                                        </#if></span></@block>
									<#if event.content??>发生 <span>${event.content!}</span></#if>
                                    </p>
                                </li>
                            </ul>
                        </div>

                        <div id="MetterMore" class="ListShow ListShow2" style="word-break: break-all; border: none;">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
                            	<@block name="matterMoreTableContent">
                            	<tr>
                                    <td width="75px;" align="right" >信息来源：</td>
                                    <td><code>${event.sourceName!}</code></td>
                                    <td width="13%" align="right" >采集渠道：</td>
                                    <td><code>${event.collectWayName!}</code></td>
                                    <@block name="urgencyDegreeNameTd">
                                    <td width="13%" style="text-align:right;">紧急程度：</td>
                                    <td>
									<#if event.urgencyDegree?? && event.urgencyDegree!='01'>
                                        <code style="color:red">${event.urgencyDegreeName!}</code>
									<#else>
                                        <code>${event.urgencyDegreeName!}</code>
									</#if>
                                    </td>
                                    </@block>
                                </tr>
                                <tr>
                                    <td align="right" >影响范围：</td>
                                    <td><code>${event.influenceDegreeName!}</code></td>
                                    <@block name="contactUserTd">
                                    <td align="right" ><@block name="contactUserLabelSpan">联系人员：</@block></td>
                                    <td colspan="3">
                                        <code>
										${event.contactUser!}
										<#if event.tel??>
											<#if (isOnVoiceCall?? && isOnVoiceCall) && (eventType?? && (eventType=='my' || eventType=='todo'))>
                                                <a href="###" style="text-decoration:none;" onclick="showVoiceCall('${rc.getContextPath()}', window.parent.showCustomEasyWindow, '${event.tel}','<#if event.contactUser??>${event.contactUser}</#if>')">(${event.tel}<img title="语音呼叫" src="${uiDomain!''}/images/cloundcall.png">)</a>
											<#else>
                                                (${event.tel})
											</#if>
										</#if>
                                        </code>
                                    </td>
                                    </@block>
                                </tr>
                                <@block name="singleLineExtraInfoTr"></@block>
                                <tr>
                                    <td align="right" >当前状态：</td>
                                    <td width="18%"><code>${event.statusName!}</code></td>
                                    <td align="right" >事件编号：</td>
                                    <td colspan="3"><code>${event.code!}</code></td>
                                </tr>
                                <@block name="detailExtend"></@block>
                                <tr>
                                    <td align="right" >涉及人员：</td>
                                    <td colspan="5">
                                        <code><#if event.involvedNumName??>(<b>${event.involvedNumName}</b>)</#if>${event.involvedPersion!}</code>
                                    </td>
                                </tr>
                                <@block name="gridPathTr">
                                <tr class="DotLine">
                                    <td align="right" >所属网格：</td>
                                    <td colspan="5">
                                        <code>${event.gridPath!}</code>
                                    </td>
                                </tr>
                                </@block>
                                
                                <#if curNodeTaskName??>
                                <@block name="curNodeTaskNameTr">
                                <tr>
                                    <td align="right" >当前环节：</td>
                                    <td colspan="5">
                                        <code>${curNodeTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code>
                                    </td>
                                </tr>
                                </@block>
                                </#if>
                                <#if isHandle?? && handleTime?? && isHandle && stepRemainTime??>
                                	<#assign stepHandle = true>
                                <#else>
                                	<#assign stepHandle = false>
                                </#if>
                                
                                <#if event.handleDateStr?? && eventRemainTime??>
                                	<#assign eventHandle = true>
                                <#else>
                                	<#assign eventHandle = false>
                                </#if>
                                
                                <#if stepHandle || eventHandle>
                                <tr>
                                    <td align="right" >处理时限：</td>
                                    <td colspan="5">
										<#if stepHandle>
                                            (环节)<code><b <#if (stepRemainTime?length>3) && (stepRemainTime?substring(0,2)=='超时')>class="FontRed"</#if>>${stepRemainTime}</b></code>
										</#if>
										<#if eventHandle>
                                            (事件)<code><b <#if event.handleDateFlag?? && event.handleDateFlag=='3'>class="FontRed"</#if>>${eventRemainTime}</b></code>
										</#if>
                                    </td>
                                </tr>
                                </#if>
                            	</@block>
                            </table>
                        </div>

                    </div>


                    <div id="slider" class="fr" style="width:300px; height:180px; border-left:1px solid #cecece;">
                        <ul></ul>
                    </div>
                    <div class="clear"></div>
                </div>

			<#if bizDetailUrl??>
                <div class="WebNotice" <#if showNotice??&&showNotice='0'>style="display:none;"</#if>>
                    <p>此事件关联了${eventReportBizTypeName!}业务单信息，<a href="###" onclick="showDetail()">点击查看</a></p>
                </div>
			</#if>

			<#if isDetail2Edit?? && isDetail2Edit>
                <div class="h_20"></div>
                <div class="title ListShow" style="padding: 0;">
                    <div class="NorForm DetailEdit">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td colspan="2" class="LeftTd">
                                    <label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 inpWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="<#if event.eventName??>${event.eventName}</#if>" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd">
                                    <label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label><input type="text" class="inp1 inpWidth easyui-validatebox" style="width: 77%;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" />
										<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd" style="border-bottom:none;">
                                    <label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 inpWidth easyui-validatebox" style="height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" ><#if event.content??>${event.content}</#if></textarea>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
			</#if>
			
			<@block name="additionalHandlePageBefore"></@block>

			<#if isHandle?? && isHandle && handleEventPage??>
                <div class="h_20"></div>
				<#include "/zzgl/event/workflow/${handleEventPage}" />
			</#if>
			
			<@block name="additionalEventPage"></@block>

			<#if instanceId??>
                <div id="showimageNa"  class="h_20" style="text-align: right;">
<#--				<a id="showImageType" target="_blank" href="${$COMPONENTS_DOMAIN}/zhsq/showImage/showImageType.jhtml?bizId=<#if event.eventId??>${event.eventId?c}</#if>&attachmentType=ZHSQ_EVENT" style="height: 20px;font-size:14px;position: relative;top: 10px;color: red;cursor: pointer;display:none;">事件照片对比</a></div>-->
                </div>
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                        	<@block name="conListLi">
                        		<#if instanceId??>
                        			<li id="01_li" class="current">处理环节</li>
                        			<!--
                        				<li>流程图</li>
                        			-->
                        		</#if>
                        		<li id="02_li" class="hide">评价列表</li>
                        		<li id="03_li" class="hide">督办记录</li>
                        		<li id="04_li" class="hide">时限申请</li>
                        		<li id="05_li" class="hide">关联历史事件</li>
                        		<li id="06_li" class="hide">相似事件</li>
                                <@block name="feedbackLi"></@block>
                        	</@block>
                        </ul>
                    </div>
                    
                    <div class="ListShow ListShow2">
                    	<@block name="conListLiContent">
                    		<#if instanceId??>
                    			<div id="01_li_div" class="t_a_b_s">
                    				<div id="workflowDetail" border="false"></div>
                    			</div>
                    		</#if>
                    		
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
                    		
                    		<div id="04_li_div" class="t_a_b_s hide">
                    			<div class="tabss">
                    				<div id="timeApplicationContent" class="appraise"></div>
                    			</div>
                    		</div>
                    		
                    		<div id="05_li_div" class="t_a_b_s hide">
                    			<div class="tabss">
                    				<div id="eventAncestorListDiv"></div>
                    			</div>
                    		</div>
                    		
                    		<div id="06_li_div" class="t_a_b_s hide">
                    			<div class="tabss">
                    				<div id="eventDuplicationListDiv"></div>
                    			</div>
                    		</div>
                            <@block name="feedbackConList"></@block>
                    	</@block>
                    </div>
                    
                </div>
                <div class="h_20"></div>
			</#if>
            </div>
        </div>
    </div>
</form>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">

    function flashData() {
        parent.winType = '0';

        try{
            parent.flashData(null, true);
        }catch(e){
		<#if eventType??&&eventType=="workStation">
            api.close();
		</#if>
        }
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
        parent.parent.top.topDialog.openDialog("查看详情", 400, 900, url)
	<#elseif source?? && source = 'oldWorkPlatform'>
        url += "&source=${source}";
        //parent.showEventContent(url,'查看详情');
        showMaxJqueryWindow("查看详情", url,880,400, false, 'no');
	<#else>
		try {
			parent.showMaxJqueryWindow("查看详情", url, fetchWinWidth({'padding_left': 0, 'padding_right': 0}), fetchWinHeight({'padding_top': 0, 'padding_bottom': 0}), false, 'no');
		} catch(e) {
			showMaxJqueryWindow("查看详情", url, fetchWinWidth({'padding_left': 0, 'padding_right': 0}), fetchWinHeight({'padding_top': 0, 'padding_bottom': 0}), false, 'no');
		}
	</#if>

    }

    var mapMarker= new MapMarker({
        el:"map",//div挂载点
        context:"${GIS_DOMAIN}",//gis域名
        data:{ //业务数据
            id : "${event.eventId!}",//业务标识
            markerType :'0301',//模块类型
            markerOperation :2,//地图操作类型 0和1为添加修改标注，2为查看标注
            gridId : $("#gridId").val(),//网格标识，用于打开地图初始的默认位置
            <#if event.resMarker??>
            	<#if event.resMarker.x?? && event.resMarker.y??>
            		initPosType:1,
            		initPosVal:{x:'${event.resMarker.x!0}',y:'${event.resMarker.y!0}',z:'${event.resMarker.z!0}'},
            	</#if>
            </#if>
            width:480,//弹框宽度，可以不传，默认480px
            height:360,//弹框高度，可以不传，默认360px
            showName:""
        },
        done:function (data) {//弹框确认回调，已回填了xyz到页面元素

        }
    });

    /*   function showMap(){
           var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
        var width = 480;
        var height = 360;
        var gridId = $("#gridId").val();
        var markerOperation = $('#markerOperation').val();
        var id = $('#eventId').val();
        var mapType = 'EVENT_V1';
        var isEdit = false;

	<#if isDetail2Edit?? && isDetail2Edit>
        isEdit = true;
	</#if>

        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }*/

    function showMix(fieldId, index){
	<#if !(isOpenInNewWindows?? && isOpenInNewWindows)>
        ffcs_viewImg_win(fieldId, index);
	<#else>
        ffcs_viewImg(fieldId, index);
	</#if>
    }

    function openPostWindow(url, data, titles){
        var tempForm = document.createElement("form");
        tempForm.id="tempForm1";
        tempForm.method="post";
        tempForm.action=url;
        tempForm.target="图片查看";
        var hideInput = document.createElement("input");
        hideInput.type="hidden";
        hideInput.name= "paths";
        hideInput.value= data;
        tempForm.appendChild(hideInput);
        var hideInput = document.createElement("input");
        hideInput.type="hidden";
        hideInput.name= "titles";
        hideInput.value= titles;
        tempForm.appendChild(hideInput);
        tempForm.submit(function(){
            openWindow("图片查看");
        });
// 		tempForm.attachEvent("onsubmit",function(){
// 			openWindow(name);
// 		});
        document.body.appendChild(tempForm);
//		tempForm.fireEvent("onsubmit");
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

    function openWindow(name){
        window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
    }
    
    <@block name="fetchEventExtraAttrFunction">
    function fetchEventExtraAttr() {
        $.ajax({
            type: "POST",
            url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/fetchEventExtraAttr.jhtml',
            data: {'eventId': $("#eventId").val()<@block name="eventEvaParams"></@block>},
            dataType:"json",
            success: function(data){

                var evaResultList = data.evaResultList,
                        superviseResultList = data.superviseResultList,
                        timeAppList = data.timeAppList,
                        ancestorCount = data.ancestorCount,
                        eventDuplicationCount = data.eventDuplicationCount;
                <@block name="evaResultContentConstruct">
                if(evaResultList && evaResultList.length > 0) {
                    var evaResultContent = "",
                            evaItem = null;
                    $("#02_li").show();

                    for(var index in evaResultList) {
                        evaItem = evaResultList[index];

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
                </@block>

                if(superviseResultList && superviseResultList.length > 0) {
                    var superviseResultContent = "",
                            supervise = null;
                    $("#03_li").show();
                    $("#dubanIconDiv").show();

				    var colormap={};
                    colormap["3"]='#0A0A0A';
                    colormap["1"]='#EEC900';
                    colormap["2"]='#e60012';
                    var supervisionType = "";//督办类型

                    for(var index in superviseResultList) {
                        supervise = superviseResultList[index];

                        superviseResultContent += '<div class="list">';
                        superviseResultContent += '    <span>';
                        superviseResultContent += '        <p>';
                        if(supervise.remindUserName) {
                            superviseResultContent += '			<em class="FontDarkBlue">' + supervise.remindUserName + '</em>';
                        }
                        if(supervise.remindDateStr) {
                            superviseResultContent += '			于 ' + supervise.remindDateStr;
                        }
                        
                        supervisionType = supervise.supervisionType;//改为实际值
                        if(supervisionType != null){
                            $("#dubanIconDiv").css('background','url(${rc.getContextPath()}/images/dubanbig'+ supervisionType +'.png');
                        }
                        superviseResultContent += '				<b style="color:'+colormap[supervisionType]+'">督办</b>';
					
					
                        superviseResultContent += '			</p>';
                        if(supervise.remarks) {
                            superviseResultContent += '			' + supervise.remarks;
                        }
                        superviseResultContent += '    </span>';
                        superviseResultContent += '</div>';
                    }

                    $("#superviseResultContent").html(superviseResultContent);
                }
                
                <@block name="timeAppContentConstructor">
                if(timeAppList && timeAppList.length > 0) {
                    var timeAppContent = "";

                    $("#04_li").show();

                    for(var index in timeAppList) {
                        timeApp = timeAppList[index];
                        
                        timeAppContent += timeAppConstructor(timeApp);
                    }

                    $("#timeApplicationContent").html(timeAppContent);
                }
                </@block>

                if(ancestorCount > 0) {
                    $("#05_li").show();

                    var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toListEventAncestor.jhtml?eventId=" + $("#eventId").val();

                    $("#eventAncestorListDiv").append('<iframe id="eventAncesorIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');

                    $("#eventAncestorListDiv > iframe").width($("#workflowDetail").width());
                    $("#eventAncestorListDiv").height($("#workflowDetail").height());
                }
                
                if(eventDuplicationCount > 0) {
                	$("#06_li").show();

                    var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toListEventDuplication.jhtml?leadEventId=" + $("#eventId").val();

                    $("#eventDuplicationListDiv").append('<iframe id="eventDuplicationIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');

                    $("#eventDuplicationListDiv > iframe").width($("#workflowDetail").width());
                    $("#eventDuplicationListDiv").height($("#workflowDetail").height());
                }
                <@block name="feedbackContentConstructor"></@block>
            },
            error:function(data){
                $.messager.alert("错误", "获取事件额外信息失败！", "error");
            }
        });
    }
    </@block>
    
    function timeAppConstructor(timeApp) {
    	var timeAppContent = "";
    	
    	if(timeApp) {
    		var checkAdvice = timeApp.checkAdvice || '',
    			  auditorName = timeApp.auditorName || '',
    			  reason = timeApp.reason || '';
	
	        timeAppContent += '<div class="list">';
	        timeAppContent += '    <span>';
	        timeAppContent += '        <p>';
	        timeAppContent += '				该事件于 <em class="FontDarkBlue">' + timeApp.createTimeStr + '</em>';
	        timeAppContent += '   			增添时限 <b class="FontRed">' + timeApp.intervalName + '</b><b>【' + timeApp.applicationTypeName + '】</b>';
	        timeAppContent += '        </p>';
	
	        if(reason) {
	            timeAppContent += 		   '<b>申请原因：</b>' + reason;
	        }
	
	        timeAppContent += '        <p>';
	        timeAppContent += '				<em class="FontDarkBlue">' + auditorName + '(' + timeApp.auditorOrgName + ')' + '</em>';
	        timeAppContent += '				<b class="FontRed">'+ timeApp.timeAppCheckStatusName +'</b>';
	        timeAppContent += '        </p>';
	
	        if(checkAdvice) {
	            timeAppContent += 		   '<b>审核意见：</b>' + checkAdvice;
	        }
	
	        timeAppContent += '    </span>';
	        timeAppContent += '</div>';
        }
        
        return timeAppContent;
    }
    
    <@block name="extraDetailFunction"></@block>
    
    $(window).resize(function(){
        var winHeight = $(window).height();
        var winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            location.reload();
        }
    });
    <@block name="showImageTypeBlock"></@block>

</script>

<@block name="extraFtlInclude"></@block>

</body>
</html>
