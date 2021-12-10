<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>三会一课详情</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
    <#include "/component/standard_common_files-1.1.ftl" />
    <script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
    <#include "/component/bigFileUpload.ftl" />
    <#include "/component/ComboBox.ftl" />
    <script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>

    <style type="text/css">
        .singleCellClass{width: 68%;}
        .doubleCellClass{width: 88%}
        .labelNameWide{width: 132px;}
        .ToolBar {
            border-top:0px;
            border-bottom:0px;
            background:#fff;
            padding: 6px 0px;/*解决表格跳动*/
            color:rgba(82,148,232,.8);
            height: 38px;
            zoom:1;
        }
        .Check_Radio {
            float:left;
            margin-top:5px;
            *margin-top:9px;
            word-break:break-all;
        }
        .datagrid-mask-msg {
            box-sizing:content-box
        }
        /*添加额外样式，解决由于受其他样式影响，详情页地图初始化后按钮底部阴影和换行*/
        div.ConSearch{
            box-shadow: 0 0 black
        }
        div.MainContent{
            box-sizing: content-box
        }
    </style>
</head>

<body>
<div class="container_fluid">
    <!-- 顶部标题 -->
    <div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
        <div id="topTitleDiv" class="fw-toptitle">
            <div class="fw-tab">
                <ul id="topTitleUl" class="fw-tab-min clearfix">
                    <li><a href="##" divId="mainDiv" class="active">三会一课信息</a></li>
                    <#if instanceId??>
                        <li><a href="##" divId="taskDetailDiv">处理环节</a></li>
                    </#if>
                    <#if feedbackCount?? && (feedbackCount > 0)>
                        <li><a href="##" divId="feedbackListDiv">信息反馈列表</a></li>
                    </#if>
                </ul>
            </div>
        </div>

        <!-- 主体内容 -->
        <div id="mainDiv" class="fw-main tabContent" style="border-bottom: 1px solid #e5e5e5">
            <div class="fw-det-tog fw-det-tog-n">
                <!--用于地图-->
                <input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
                <input type="hidden" id="markerOperation" name="markerOperation" value="3"/>
                <input type="hidden" id="module" value="MEETINGS_AND_LESSON"/>
                <ul>
                    <li>
                        <p>
                        	<p style="margin-top: -4px;" id="resmarkerDiv"></p>&nbsp;<p id="occurredDiv" style="font-size: 16px;">${reportFocus.occurred!}</p>
                        </p>
                    </li>
                </ul>

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                	<tr>
                		<td>
                			<label class="LabName"><span>报告编号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
                		</td>
                		<td>
                			<label class="LabName labelNameWide"><span><label class="Asterik">*</label>所属区域：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.regionPath!}</div>
                		</td>
                	</tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>报告人姓名：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
                        </td>
                        <td>
                            <label class="LabName labelNameWide"><span>报告时间：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!}</div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 40%;">
                            <label class="LabName"><span><label class="Asterik">*</label>会议时间：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.meetingTimeStr!}</div>
                        </td>
                        <td>
                            <label class="LabName labelNameWide"><span>报告方式：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span><label class="Asterik">*</label>会议类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.meetingTypeName!}</div>
                        </td>
                        <td>
                            <label class="LabName labelNameWide"><span><label class="Asterik">*</label>党组织名称：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.partyGroupName!}</div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 40%;">
                            <label class="LabName"><span><label class="Asterik">*</label>应到会党员：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.shouldAttriveNum!}人</div>
                        </td>
                        <td>
                            <label class="LabName labelNameWide"><span><label class="Asterik">*</label>实到会党员：</span></label><div class="Check_Radio FontDarkBlue singleCellClass"><#if reportFocus.actualNumber??>${reportFocus.actualNumber}</#if>人</div>
                        </td>
                    </tr>
                    <tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
                    	<td colspan="2">
                    		<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
                    	</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
                        </td>
                    </tr>
                </table>

                <form id="tmolDetailForm" name="tmolDetailForm" action="" method="post" enctype="multipart/form-data">
                    <input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
                    <input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
                    <input type="hidden" id="reportType" name="reportType" value="${reportType!}" />

                    <div style="padding-top: 10px;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="LeftTd" colspan="2">
                                    <label class="LabName"><span><#if reportFocus.collectSource?? && reportFocus.collectSource == '02'><label class="Asterik">*</label></#if>图片上传：</span></label><div id="bigFileUploadDiv"></div>
                                </td>
                            </tr>
                            <tr>
								<td class="LeftTd" colspan="2">
									<label class="LabName"><span>视频上传：</span></label><div id="bigVideoUploadDiv"></div>
								</td>
							</tr>
                            <tr>
                                <td class="LeftTd" colspan="2">
                                    <label class="LabName"><span>附件上传：</span></label><div id="attachFileUploadDiv"></div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>

            <!--办理信息-->
            <#if isAble2Handle?? && isAble2Handle>
                <div class="fw-det-tog fw-det-tog-n mt20">
                    <div class="fw-det-tog-top">
                        <h5><i style="background:#4f91ff;"></i>处置流程</h5>
                    </div>
                    <div class="fw-chul">
                        <#include "/zzgl/event/workflow/handle_node_base.ftl" />
                    </div>
                </div>
            </#if>

        </div>

        <#if instanceId??>
            <div id="taskDetailDiv" class="fw-main tabContent" style="padding-top: 3px;">
                <!-- 处理环节 -->
                <div id="workflowDetail" border="false"></div>
            </div>
        <#elseif listType?? && listType=='1'>
            <!--操作按钮-->
            <div id="btnDiv" class="btn-warp">
                <a class="btn-bon blue-btn" onclick="startWorkflow();">提交</a>
            </div>
        </#if>
        <#if feedbackCount?? && (feedbackCount > 0)>
            <div id="feedbackListDiv" class="fw-main tabContent"></div>
        </#if>
    </div>
</div>
</body>

<script type="text/javascript">
    var basWorkSubTaskCallback = null,//存放原有的提交回调方法
        baseWorkRejectCallback = null;//存放原有的驳回方法

    $(function () {
        var $winH = 0, $topH = 0, $btnH = 0,
            reportId = $("#reportId").val(),
            bigFileUploadOpt = {
                useType			: 'view',
				fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
                module			: 'meetingsAndLesson',
                attachmentData	: {bizId: reportId, attachmentType:'MEETINGS_AND_LESSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
                individualOpt 	: {
                    isUploadHandlingPic : true
                },
                //替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
                labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}]
            };
        var bigViodeUploadOpt = {
				useType			: 'view',
				fileExt			: '.mp4,.avi,.amr',
				module			: 'meetingsAndLesson',
				attachmentData	: {bizId: reportId, attachmentType:'MEETINGS_AND_LESSON', eventSeq: '1', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				},
				//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
				labelDict		: [{'name':'处理前', 'value':'1'}]
			};
        var attachFileUploadOpt = {
            useType			: 'view',
            fileExt			: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            module			: 'meetingsAndLesson',
            attachmentData	: {bizId: reportId, attachmentType:'MEETINGS_AND_LESSON', eventSeq: '1', isBindBizId: 'yes'},
            individualOpt 	: {
                isUploadHandlingPic : true
            },
            //替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
            labelDict		: [{'name':'处理前', 'value':'1'}]
        };

        <#if isAble2Handle?? && isAble2Handle>
        var baseWorkOption = BaseWorkflowNodeHandle.initParam(),//获取默认的设置
            reportType = $('#reportType').val(),
            selectHandler = baseWorkOption.selectHandler;

        basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
        baseWorkRejectCallback = baseWorkOption.reject.rejectCallback;

        $.extend(selectHandler, {
            userSelectedLimit : 1
        });

        BaseWorkflowNodeHandle.initParam({
            subTask: {
                subTaskUrl: '${rc.getContextPath()}/zhsq/reportMeeting/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
                subTaskCallback: _subTaskOperate
            },
            reject: {
                rejectUrl: '${rc.getContextPath()}/zhsq/reportMeeting/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
                rejectCallback: _rejectOperate
            },
            evaluate: {
                isShowEva: false
            },
            initDefaultValueSet: initDefaultValueSet4Base,
            checkRadio: {
                radioCheckCallback: radioCheckCallback
            },
            selectHandler : selectHandler
        });

        bigFileUploadOpt["useType"] = 'edit';
        bigViodeUploadOpt["useType"] = 'edit';
        attachFileUploadOpt["useType"] = 'edit';
        </#if>

        <#if feedbackCount?? && (feedbackCount > 0)>
        fetchMeetingFeedback();
        </#if>
        
        //顶部页签切换相应事件
        $('#topTitleUl').on('click', 'li a', function() {
            $('#topTitleUl > li > a').removeClass('active');

            $(this).addClass('active');

            var divId = $(this).attr('divId'),
            	iframeItemList = $("#" + divId + " iframe"),
            	iframeLen = iframeItemList.length,
            	mainDivWidth = $(window).width() - 20 - 20;//左右边距20，样式container_fluid设置的
            var $winH = $(window).height(), $topH = $('#topTitleDiv').outerHeight(true), $btnH = $('#btnDiv').outerHeight(true);
            
            $('#' + divId).height($winH - $topH - $btnH);
            $('#' + divId).width(mainDivWidth + 10 + 10);
            
            if(divId == 'taskDetailDiv' && !$('#taskDetailDiv').hasClass('flowDetailLoaded')) {
            	$('#taskDetailDiv').addClass('flowDetailLoaded');
            	
            	showWorkflowDetail();
            	
            	$('#workflowDetail').width(mainDivWidth);
            }
			
            if(iframeLen == 1) {
                var iframeItem = iframeItemList.eq(0),
                    iframeLoaded = iframeItem.attr("_loadflag");
                if(isBlankStringTrim(iframeLoaded)) {
                    iframeItem.attr("_loadflag", true);//用于防止重复加载
                    iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
                }
            }
            
            $('#formDiv div.tabContent').hide();
            $('#' + divId).show();
        });

        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
        bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

        $(window).on('load resize', function () {
            $winH = $(window).height();
            $topH = $('#topTitleDiv').outerHeight(true);
            $btnH = $('#btnDiv').outerHeight(true);

            $('#mainDiv').height($winH - $topH - $btnH);
            $('#taskDetailDiv').height($winH - $topH - $btnH);

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

        /*页面打开时判断地图标注是否存在*/
        capcapMarkerData();
        
        $('#topTitleUl  > li > a').eq(0).click();
        
        <#if msgWrong??>
        $.messager.alert('错误', '${msgWrong!}', 'error');
        </#if>
    });

    function startWorkflow() {//启动流程
        var reportId = $("#reportId").val();

        if(reportId) {
            $("#tmolDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMeeting/startWorkflow4ReportFocus.jhtml");

            modleopen();

            $("#tmolDetailForm").ajaxSubmit(function(data) {
                modleclose();

                if(data.success && data.success == true) {
                    parent.searchData();
                    parent.detail($('#reportUUID').val(), data.instanceId, '2');
                    if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
                        parent.closeBeforeMaxJqueryWindow();
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

    function radioCheckCallback(data) {//下一环节选中回调方法
        var adviceNote = data.adviceNote || '',
            dynamicContentMap = data.dynamicContentMap || {},
            isShowText = dynamicContentMap.isShowText || false,
            isShowDict = dynamicContentMap.isShowDict || false,
            isShowAddress = dynamicContentMap.isShowAddress || false;
        $('#advice').val('');
        if('end1'!=data.nodeName){
            $('#remarkSpanHtml').html('<label class="Asterik">*</label>退回原因：');
            $('#contentSpan_1').show(); // contentSpan_1
        }else{
            $('#remarkSpanHtml').html('');
            $('#contentSpan_1').hide();//contentSpan_1    remarkTr
        }

        if($('#dynamicDict').length == 1) {
            $('#dynamicDict').val('');
        }

        if($('#dynamicContent').length == 1) {
            $('#dynamicContent').val('');
        }

        if($('#dynamicAddress').length == 1) {
            $('#dynamicAddress').val('');
        }

        if(!isShowText && !isShowDict && !isShowAddress) {
            $('#advice').val(adviceNote);
        }
        
        _handlerConstructor(data);

        if(isShowDict || isShowText || isShowAddress) {
            if($('#adviceNote').length == 0) {
                $('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
            }

            $('#adviceNote').val(adviceNote);
        }

        if(isShowDict) {
            if($('#dynamicContentDictTr').length == 0) {
                var dynamicDictJson = dynamicContentMap.dynamicDictJson || '',
                    dictPcode = dynamicContentMap.dictPcode || '';

                $('#remarkTr').before(
                    '<tr id="dynamicContentDictTr">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.dictLabelName + '：</span></label>' +
                    '<input type="hidden" id="dynamicDictId" value="" />' +
                    '<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:\'bottom\'" id="dynamicDict" value="" />' +
                    '</td>' +
                    '</tr>'
                );

                if(dictPcode) {
                    AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", dictPcode, function(dictValue, item) {
                        var dictName = '';

                        if(item && item.length > 0) {
                            dictName = item[0].name;
                        }
                        _capDynamicContent(dictName, dictValue);
                    });
                } else if(dynamicDictJson) {
                    AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", null, function(dictValue, item) {
                        $('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
                        _capDynamicContent($('#dynamicContent').val(), dictValue);
                    }, null, {
                        DataSrc: eval('(' + dynamicDictJson + ')')
                    });
                }
            }

            $('#dynamicDict').validatebox({
                required: true
            });

            $('#dynamicContentDictTr').show();
        } else if($('#dynamicContentDictTr').length == 1) {
            $('#dynamicContentDictTr').hide();
            $('#dynamicDict').validatebox({
                required: false
            });
        }

        if(isShowText) {
            var curNodeName = '${curNodeName!}';

            if($('#dynamicContentAreaTr').length == 0) {
                $('#remarkTr').before(
                    '<tr id="dynamicContentAreaTr">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：</span></label>' +
                    '<textarea rows="3" style="height:50px;" id="dynamicContent" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' +
                    '</td>' +
                    '</tr>'
                );

                var winWidth = $(window).width();
                $("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
                    $(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
                        .addClass("isSettledAutoWidth");
                });
            }

            if(curNodeName == 'task4') {
                $('#dynamicContentAreaTr').hide();
            } else {
                $('#dynamicContent').validatebox({
                    required: true
                });

                $('#dynamicContentAreaTr').show();
            }

        } else if($('#dynamicContentAreaTr').length == 1) {
            $('#dynamicContentAreaTr').hide();
            $('#dynamicContent').validatebox({
                required: false
            });
        }

        if(isShowAddress) {
            if($('#dynamicContentAddressTr').length == 0) {
                var startDivisionCode = dynamicContentMap.startDivisionCode || '';

                $('#remarkTr').before(
                    '<tr id="dynamicContentAddressTr">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.addressLabelName + '：</span></label>' +
                    '<input type="text" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1500]\',\'characterCheck\']" name="occurred" id="dynamicAddress" value="" />' +
                    '<input type="hidden" name="regionCode" id="dynamicRegionCode"/>' +
                    '</td>' +
                    '</tr>'
                );

                $("#dynamicAddress").anoleAddressRender({
                    _source : 'XIEJING',//必传参数，数据来源
                    _select_scope : 0,
                    _show_level : 6,//显示到哪个层级
                    _context_show_level : 1,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
                    _startAddress :"",
                    _startDivisionCode : startDivisionCode, //默认选中网格，非必传参数
                    _customAddressIsNull : false,
                    _addressMap : {//编辑页面可以传这个参数，非必传参数
                        _addressMapShow : false,//是否显示地图标注功能
                        _addressMapIsEdit : true
                    },
                    BackEvents : {
                        OnSelected : function(api) {
                            $("#dynamicAddress").val(api.getAddress());
                            $("#dynamicRegionCode").val(api.getInfoOrgCode());
                            _capDynamicContent(api.getAddress());
                        },
                        OnCleared : function(api) {
                            //清空按钮触发的事件
                            $("#dynamicAddress").val('');
                        }
                    }
                });
            }

            $('#dynamicAddress').validatebox({
                required: true
            });

            $('#dynamicContentAddressTr').show();

        } else if($('#dynamicContentAddressTr').length == 1) {
            $('#dynamicContentAddressTr').hide();
            $('#dynamicAddress').validatebox({
                required: false
            });
        }

        if('end1'!=data.nodeName){
            $('#advice').val('');
        }else{
            $('#advice').val('0');
        }
        _itemSizeAdjust();
    }

    function _subTaskOperate() {
        if($('#dynamicContentDictTr').length == 1 && $('#dynamicContentDictTr').is(':visible')) {
            $('#dynamicDict').validatebox({
                required: true
            });
        }

        if($('#dynamicContentAreaTr').length == 1 && $('#dynamicContentAreaTr').is(':visible')) {
            $('#dynamicContent').validatebox({
                required: true
            });
        }

        if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
            basWorkSubTaskCallback();
        }
    }

    function _rejectOperate() {
        if($('#dynamicContentDictTr').length == 1) {
            $('#dynamicDict').validatebox({
                required: false
            });
        }

        if($('#dynamicContentAreaTr').length == 1) {
            $('#dynamicContent').validatebox({
                required: false
            });
        }

        if(baseWorkRejectCallback != null && typeof baseWorkRejectCallback === 'function') {
            baseWorkRejectCallback();
        }
    }

    function _checkSelectUser() {
        var selectUserIds = "", selectOrgIds = "";
        var userCheckbox = $("input[name='htmlSelectUserCheckbox'][type='checkbox']:checked");
        var allCheckBoxLen = $("input[name='htmlSelectUserCheckbox'][type='checkbox']").length;
        var checkedBoxLen = userCheckbox.length;

        if(userCheckbox.length > 0) {
            userCheckbox.each(function() {
                selectUserIds += "," + $(this).attr("userid");
                selectOrgIds += "," + $(this).attr("orgid");
            });

            if(selectUserIds.length > 0) {
                selectUserIds = selectUserIds.substr(1);
            }

            if(selectOrgIds.length > 0) {
                selectOrgIds = selectOrgIds.substr(1);
            }
        }

        $("#htmlSelectUserCheckAll").attr('checked',allCheckBoxLen == checkedBoxLen);

        $('#selectOrgIds').val(selectOrgIds);
        $('#selectUserIds').val(selectUserIds);
    }

    function _checkAllSelectUser() {
        var isCheckAll = $("#htmlSelectUserCheckAll").attr("checked");
        var userCheckbox = $("input[name='htmlSelectUserCheckbox'][type='checkbox']");

        for(var index = 0, len = userCheckbox.length; index < len; index++){
            userCheckbox[index].checked = isCheckAll;
        }

        _checkSelectUser();
    }

    function _capDynamicContent(dynamicContent, dictValue) {
        var adviceNote = $('#adviceNote').val(),
            advice = '',
            curNodeName = '${curNodeName!}';

        if(curNodeName == 'task4') {
            if(dictValue == '0') {
                $('#dynamicContentAreaTr').hide();
                $('#dynamicContent').validatebox({
                    required: false
                });
            } else {
                $('#dynamicContent').validatebox({
                    required: true
                });
                $('#dynamicContentAreaTr').show();
            }
        }

        if(adviceNote) {
            var dynamicContentLabel = '@dynamicContent@';

            if(adviceNote.indexOf(dynamicContentLabel) >= 0) {
                if(dynamicContent) {
                    advice = adviceNote.replaceAll('@dynamicContent@', dynamicContent);
                }
            } else {
                advice = adviceNote;
            }
        }

        $('#advice').val(advice);
    }

    function showWorkflowDetail() {//流程详情
        var instanceId = "${instanceId!}";
        if(instanceId) {
        	modleopen();
        	$("#workflowDetail").panel({
        		height:'auto',
        		width:'auto',
        		overflow:'no',
        		href: "${rc.getContextPath()}/zhsq/reportMeeting/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
        			
        			for(var index = 0; index < taskListSize; index++) {
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
        				
        				for(var index_ = 0; index_ < remindSize; index_++) {
        					remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
        					remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
        					$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
        				}
        			}
        			
        			adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
        			
        			modleclose();
        		}
        	});
        }
    }
    <#if feedbackCount?? && (feedbackCount > 0)>
    function fetchMeetingFeedback() {
        var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
        $("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
        $("#feedbackListDiv > iframe").width($("#workflowDetail").width());
        $("#feedbackListDiv").height('auto');
    }
    </#if>
</script>

<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>