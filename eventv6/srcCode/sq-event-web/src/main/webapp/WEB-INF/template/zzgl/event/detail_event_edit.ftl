<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><@block name="eventDetailEditPageTitle">事件待办可编辑详情页面</@block></title>
	<#include "/component/standard_common_files-1.1.ftl" />
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
    
    <@block name="eventDetailAdditionalQuote"></@block>

    <style type="text/css">
        .DetailEdit .LabName{width:75px;}
        .DetailEdit .inpWidth{width: 89%;}
        .selectWidth {width: 200px;}
    </style>
</head>

<body>
<form id="flowSaveForm"	action="" method="post" enctype="multipart/form-data">
    <input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
    <input type="hidden" name="isHandleEdit" value="true" />

    <!--用于地图-->
    <input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" />
    <input type="hidden" id="markerOperation" name="markerOperation" value="1"/>

    <div class="MetterList">
        <div id="content-d" class="MC_con content light"><!--使得事件简介能随着滚动条移动，隐藏横向滚动条-->
            <div id="metterContentDiv" class="MetterContent" >
                <div id="metterContentListShowDiv" class="title ListShow" style="background:none;">
                    <div id="contentDiv" class="NorForm fl" style="width: 100%; height: 310px;">
                    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                    		<tr>
                    			<td>
                    				<label class="LabName"><span><label class="Asterik">*</label>事件分类：</span></label>
                    				<input type="hidden" id="type" name="type" value="<#if event.type??>${event.type}</#if>" />
                    				<input type="text" class="inp1 InpDisable easyui-validatebox selectWidth" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
                    			</td>
                    			<td>
                    				<label class="LabName"><span><@block name="gridLabelName">所属网格</@block>：</span></label>
                    				<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
                    				<input type="hidden" id="gridCode" name="gridCode" value="<#if event.gridCode??>${event.gridCode}</#if>">
                    				<input type="text" class="inp1 InpDisable easyui-validatebox selectWidth" data-options="required:true" id="gridName" name="gridName" value="<#if event.gridName??>${event.gridName}</#if>" />
                    			</td>
                    		</tr>
                    		<tr>
                    			<td>
                    				<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="<#if event.eventName??>${event.eventName}</#if>" />
                    			</td>
                    			<td>
                    				<label class="LabName"><span>影响范围：</span></label>
                    				<input type="hidden" id="influenceDegree" name="influenceDegree" value="<#if event.influenceDegree??>${event.influenceDegree}</#if>" />
                    				<input type="text" class="inp1 easyui-validatebox selectWidth" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="<#if event.influenceDegreeName??>${event.influenceDegreeName}</#if>" />	
                    			</td>
                    		</tr>
                    		<tr>
                    			<td>
                    				<label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox selectWidth" style="cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
                    			</td>
                    			<td>
                    				<label class="LabName"><span>信息来源：</span></label>
                    				<input type="hidden" id="source" name="source" value="<#if event.source??>${event.source}</#if>" />
                    				<input type="text" class="inp1 easyui-validatebox selectWidth" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="<#if event.sourceName??>${event.sourceName}</#if>" />
                    			</td>
                    		</tr>

                    		<tr>
								<@block name="eventOccurredInput">
                    			<td>
                    				<label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" />
                    			</td>
								</@block>
                    			<td>
                    				<label class="LabName"><span>紧急程度：</span></label>
                    				<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="<#if event.urgencyDegree??>${event.urgencyDegree}</#if>" />
                    				<input type="text" class="inp1 easyui-validatebox selectWidth" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>" />
                    			</td>
                    		</tr>
                    		<tr>
								<@block name="geographicalLabelingInput">
                    			<td>
                    				<label class="LabName"><span>地理标注：</span></label>
                    				<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    			</td>
								</@block>
								<@block name="involvedNumInput">
									<td>
										<label class="LabName"><span>涉及人数：</span></label>
										<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
										<input type="text" class="inp1 easyui-validatebox selectWidth" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" />
									</td>
								</@block>
                    		</tr>
                    		<tr>
                    			<td colspan="2">
                    				<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="width:86%; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" ><#if event.content??>${event.content}</#if></textarea>
                    			</td>
                    		</tr>
                    		<tr>
                    			<td class="LeftTd" colspan="2">
                    				<label class="LabName"><span>涉及人员：</span></label>
                    				<input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="<#if event.eventInvolvedPeople??>${event.eventInvolvedPeople}</#if>" />
                    				<input type="hidden" name="involvedPersion" id="involvedPersion" value="<#if event.involvedPersion??>${event.involvedPersion}</#if>" />
						        	<div class="addinfo" style="width:667px;">
						            	<code class="fl FontDarkBlue" onclick="showInvoledPeopleSelector();"><a href="###">点击添加人员</a></code>
						            	<div id="involvedPeopleName">
						            		<#if involvedPeopleList?? >
						            			<#list involvedPeopleList as l>
										    		<p title="<#if l.name??>${l.name}</#if>(<#if l.idCard??>${l.idCard}</#if>)"><#if l.name??>${l.name}</#if><img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople("<#if l.name??>${l.name}</#if>","<#if l.idCard??>${l.idCard}</#if>", $(this).parent());'/></p>
										    	</#list>
						            		</#if>
						            	</div>
						            </div>
                    			</td>
                    		</tr>
                    		<tr>
                    			<td>
                    				<label class="LabName"><span id="contactUserLabelSpan">联系人员：</span></label>
                    				<input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="<#if event.contactUser??>${event.contactUser}</#if>" />
                    			</td>
                    			<td>
                    				<label class="LabName"><span id="contactTelLabelSpan">联系电话：</span></label>
                    				<input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="<#if event.tel??>${event.tel}</#if>" />
                    			</td>
                    		</tr>
                    	</table>
                    </div>

                    <div class="clear"></div>
                </div>

			<#if bizDetailUrl??>
                <div class="WebNotice" <#if showNotice??&&showNotice='0'>style="display:none;"</#if>>
                    <p>此事件关联了${eventReportBizTypeName!}业务单信息，<a href="###" onclick="showDetail()">点击查看</a></p>
                </div>
			</#if>
			
			<@block name="additionalHandlePageBefore"></@block>

			<#if isHandle?? && isHandle && handleEventPage??>
                <div class="h_20"></div>
				<#include "/zzgl/event/workflow/${handleEventPage}" />
			</#if>
			
			<@block name="additionalEventPage"></@block>

			<#if instanceId??>
				<div class="h_20"></div>
				
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                        	<@block name="conListLi">
                        		<#if instanceId??>
                        			<li id="01_li" class="current">处理环节</li>
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
<script type="text/javascript">
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
		
		_itemInit();
		
		//由于受涉及人员involvedPeopleSelector.ftl影响，会扣除涉及人员的按钮高度，由此需要重新设置高度
		$('#flowSaveForm .MC_con').height($(window).height());
		
        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        
        enableScrollBar('contentDiv',options);
        fetchEventExtraAttr();
        
        <@block name="additionalInitialization"></@block>
        
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
                }
            });
        });
    });

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

    function showMap(){
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
        var width = 480;
        var height = 360;
        var gridId = $("#gridId").val();
        var markerOperation = $('#markerOperation').val();
        var id = $('#eventId').val();
        var mapType = 'EVENT_V1';
        var isEdit = true;

        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }
    
    function _itemInit() {
    	var typesDictCode = "${typesDictCode!}";
		if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			AnoleApi.initTreeComboBox("typeName", "type", { 
				"A001093199" : [${typesDictCode!}] 
			}, <@block name="callBackSelected4eventType">null</@block>, [<#if event.type??>"${event.type}"</#if>], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
			});
		} else {
			AnoleApi.initTreeComboBox("typeName", "type", "A001093199", <@block name="callBackSelected4eventType">null</@block>, [<#if event.type??>"${event.type}"</#if>], {
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
			});
		}
		
		<@block name="gridTreeInitMethod">
		<#if rootGridId?? && (rootGridId > 0)>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#gridCode").val(grid.orgCode);
				}
			}, {
				Async : {
					enable : true,
					autoParam : [ "id=gridId" ],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : ${rootGridId?c}
					}
				}
			});
		<#else>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#gridCode").val(grid.orgCode);
				}
			});
		</#if>
		</@block>
		
		AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "A001093094", null, [<#if event.influenceDegree??>"${event.influenceDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "A001093271", null, [<#if event.urgencyDegree??>"${event.urgencyDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("involvedNumName", "involvedNum", "A001093270", null, [<#if event.involvedNum??>"${event.involvedNum}"<#else>"00"</#if>]);
		
		var sourceDictCode = "${sourceDictCode!}";
		if(sourceDictCode!=null && sourceDictCode!="null" && sourceDictCode!="") {
			AnoleApi.initTreeComboBox("sourceName", "source", { 
				"A001093222" : [${sourceDictCode!}] 
			}, null, [<@block name="defaultEventSourceValue"><#if event.source??>"${event.source}"<#else>"01"</#if></@block>], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveSource?? && isRemoveSource>1<#else>0</#if>"
			});
		} else {
			AnoleApi.initListComboBox("sourceName", "source", "A001093222", null, [<@block name="defaultEventSourceValue"><#if event.source??>"${event.source}"<#else>"01"</#if></@block>]);
		}
		
		<@block name="initSupplement"></@block>
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
    
    function involedPeopleCallback(users) {
		if(users == ""){
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
			return;
		}
		var usersDiv = "";
		var userNames = "";
		var userArray = users.split("；");
		if(userArray != ""){
			$.each(userArray, function(i, n){
				var items = n.split("，");
				if(typeof(items[1])!="undefined" ){
					var userName = items[1];
					if(userName.length > 3){//名字显示前三个字
						userName = userName.substr(0, 3);
					}
					usersDiv += "<p title="+items[1]+"("+items[2]+")>"+userName+"<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople(\""+items[1]+"\",\""+items[2]+"\", $(this).parent());'/></p>";
					userNames += items[1] + "，";
				}
			});
			
			userNames = userNames.substr(0, userNames.length - 1);
			$("#involvedPeopleName").html(usersDiv);//用于页面显示
			$("#eventInvolvedPeople").val(users);//用于后台保存
			$("#involvedPersion").val(userNames);
		}else{
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
		}
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
	<@block name="extraScriptFunction"></@block>
</script>

<#include "/component/ComboBox.ftl" />
<#include "/component/involvedPeopleSelector.ftl">

<@block name="extraFtlInclude"></@block>

</body>
</html>
