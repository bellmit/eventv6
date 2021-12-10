<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>环境卫生整治详情</title>
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
    <#--家庭成员组件-->
    <#--<#include "/component/commonFiles-1.1.ftl" />-->
    <script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/components/partyMember/partyMembersSelector.js"></script>

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
                    <li><a href="##" divId="mainDiv" class="active">环境卫生问题处置信息</a></li>
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
                <input type="hidden" id="module" value="ENVIRONMENTAL_HEALTH_TREATMENT"/>
                <#--是否上传处理后图片-->
                <input type="hidden" id="isUploadHandledPic" value="${isUploadHandledPic!'false'}"/>
                <input type="hidden" id="isUploadHandlingPic" value="${isUploadHandlingPic!'false'}"/>
                <input type="hidden" id="picTypeName" value=""/>
                <input type="hidden" id="isUploadNewFile" value=""/>
                <ul>
                    <li>
                        <p>
                        <p style="margin-top: -4px;" id="resmarkerDiv"></p>&nbsp;<p id="occurredDiv" style="font-size: 16px;<#if !(reportFocus.occurred??)>color: #C0C0C0;</#if>">${reportFocus.occurred!'（暂无发生地址信息！）'}</p>
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
                            <label class="LabName labelNameWide">
                                <span>报告人电话：</span></label>
                            <div class="Check_Radio FontDarkBlue">${reportFocus.reporterTel!}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd">
                            <label class="LabName">
                                <span>数据来源：</span>
                            </label>
                            <div class="Check_Radio FontDarkBlue">${reportFocus.dataSourceName!''}</div>
                        </td>
                        <td class="LeftTd">
                            <label class="LabName labelNameWide">
                                <span>报告方式：</span>
                            </label>
                            <div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
                        </td>
                    </tr>
                    <#if reportFocus.discoveryChannel??>
                        <tr>
                            <td style="width: 40%;">
                                <label class="LabName">
                                    <span><label class="Asterik">*</label>发现渠道：</span>
                                </label>
                                <div class="Check_Radio FontDarkBlue">${reportFocus.discoveryChannelName!}</div>
                            </td>
                            <td>
                                <label class="LabName labelNameWide">
                                    <span><label class="Asterik">*</label>${reportFocus.discoveryStaffName!'发现部门'}：</span>
                                </label>
                                <div class="Check_Radio FontDarkBlue">${reportFocus.discoveryStaff!}</div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td style="width: 40%;">
                            <label class="LabName">
                                <span>报告时间：</span>
                            </label>
                            <div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!}</div>
                        </td>
                        <td>
                            <label class="LabName labelNameWide">
                                <span><label class="Asterik">*</label>问题类型：</span>
                            </label>
                            <div style="width: 330px;" class="Check_Radio FontDarkBlue">${reportFocus.problemTypeName!}</div>
                        </td>
                    </tr>
                    <#if reportFocus.dataSource?? && reportFocus.dataSource == '02'>
                        <tr>
                            <td colspan="2">
                                <label class="LabName">
                                    <span>反馈时限：</span>
                                </label>
                                <div class="Check_Radio FontDarkBlue">${reportFocus.feedbackTimeStr!}</div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <label class="LabName">
                                    <span><label class="Asterik">*</label>登记内容：</span>
                                </label>
                                <div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
                            </td>
                        </tr>
                    </#if>
                    <#if reportFocus.problemDesc??>
                        <tr>
                            <td colspan="2">
                                <label class="LabName">
                                    <span><label class="Asterik">*</label>问题描述：</span>
                                </label>
                                <div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.problemDesc!}</div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="2">
                            <label class="LabName"><span>备注：</span></label>
                            <div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
                        </td>
                    </tr>
                </table>

                <form id="ehdDetailForm" name="ehdDetailForm" action="" method="post" enctype="multipart/form-data">
                    <input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
                    <input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
                    <input type="hidden" id="reportType" name="reportType" value="${reportType!}" />
                    <input type="hidden" id="collectSource" name="collectSource" value="${reportFocus.collectSource!}" />

                    <div style="padding-top: 10px;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="LeftTd" colspan="2">
                                    <label class="LabName">
                                        <span><#if reportFocus.dataSource?? && reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>图片上传：</span>
                                    </label>
                                    <div id="bigFileUploadDiv"></div>
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
                module			: 'environmentHealTreatment',
                attachmentData	: {bizId: reportId, attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT', eventSeq: '1,2,3', isBindBizId: 'yes'},
                individualOpt 	: {
                    isUploadHandlingPic : true
                },
                //替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
                labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}],
                uploadSuccessCallback : function(file,response){
                    uploadback(file,response);
                },
                deleteCallback:function(obj){
                    deleteback(obj);
                }
            };
        var bigViodeUploadOpt = {
            useType			: 'view',
            fileExt			: '.mp4,.avi,.amr',
            module			: 'environmentHealTreatment',
            attachmentData	: {bizId: reportId, attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT', eventSeq: '1', isBindBizId: 'yes'},
            individualOpt 	: {
                isUploadHandlingPic : true
            },
            //替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
            labelDict		: [{'name':'处理前', 'value':'1'}],
            uploadSuccessCallback : function(file,response){
                uploadback(file,response);
            },
            deleteCallback:function(obj){
                deleteback(obj);
            }
        };

        var attachFileUploadOpt = {
            useType			: 'view',
            fileExt			: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            module			: 'environmentHealTreatment',
            attachmentData	: {bizId: reportId, attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT', eventSeq: '1', isBindBizId: 'yes'},
            individualOpt 	: {
                isUploadHandlingPic : true
            },
            //替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
            labelDict		: [{'name':'处理前', 'value':'1'}],
            uploadSuccessCallback : function(file,response){
                uploadback(file,response);
            },
            deleteCallback:function(obj){
                deleteback(obj);
            }
        };
        <#if isAble2Handle?? && isAble2Handle>
        var baseWorkOption = BaseWorkflowNodeHandle.initParam(),//获取默认的设置
            reportType = $('#reportType').val(),
            selectHandler = baseWorkOption.selectHandler;

        /*$('#reportType').attr('nodeId','-1');
        BaseWorkflowNodeHandle.checkRadio($('#reportType'));*/

        basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
        baseWorkRejectCallback = baseWorkOption.reject.rejectCallback;

        //人员选择回调、可选择人数限制
        $.extend(selectHandler, {
            callback4Confirm : _capDynamicContent
            <#if userSelectedLimit?? && userSelectedLimit>, userSelectedLimit : 1</#if>
        });

        BaseWorkflowNodeHandle.initParam({
            subTask: {
                subTaskUrl: '${rc.getContextPath()}/zhsq/reportEHT/subWorkflow4ReportFocus.jhtml?reportType=' + reportType + '&dataSource=' + $('#dataSource').val(),
                subTaskCallback: _subTaskOperate
            },
            reject: {
                rejectUrl: '${rc.getContextPath()}/zhsq/reportEHT/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
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
            fetchEPCFeedback();
        </#if>

        init4FetchRemind();

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

        var isSmallMedical = <#if reportFocus.collectSource?? && reportFocus.collectSource=='05'>true<#else>false</#if>;
        $('#mainDiv .autoRequired').each(function() {
            if(isSmallMedical) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });

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
            $("#ehdDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportEHT/startWorkflow4ReportFocus.jhtml");

            modleopen();

            $("#ehdDetailForm").ajaxSubmit(function(data) {
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
            isShowTextExtra = dynamicContentMap.isShowTextExtra || false,
            partyMemberList = data.partyMemberList||[],
            partyMember = data.partyMember||'',
            isShowDict = dynamicContentMap.isShowDict || false,
            isBeforeNode = dynamicContentMap.isBeforeNode || false,
            isShowAddress = dynamicContentMap.isShowAddress || false,
            isShowRegion = dynamicContentMap.isShowRegion || false,
            dynamicContentNextOrgNameLabel = '@nextOrgNames@',
            formTypeId = '${formTypeId!}',
            curNodeName = '${curNodeName!}',
            nextNodeName = $('#nodeName_').val();

        if($('#dynamicDict').length == 1) {
            $('#dynamicDict').val('');
        }

        if($('#dynamicContent').length == 1) {
            $('#dynamicContent').val('');
        }

        if($('#dynamicContentExtra').length == 1) {
            $('#dynamicContentExtra').val('');
        }

        if($('#dynamicAddress').length == 1) {
            $('#dynamicAddress').val('');
        }

        _handlerConstructor(data);

        if($('#adviceNote').length == 0) {
            $('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
        }

        $('#adviceNote').val(adviceNote);

        if(isShowDict) {
            if($('#dynamicContentDictTr').length == 0) {
                var dynamicDictJson = dynamicContentMap.dynamicDictJson || '',
                    dictPcode = dynamicContentMap.dictPcode || '';

                $('#remarkTr').before(
                    '<tr id="dynamicContentDictTr">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.dictLabelName + '：</span></label>' +
                    '<input type="hidden" id="dynamicDictId" value="" />' +
                    '<input type="hidden" id="dictLabelNameSpan" value="'+ dynamicContentMap.dictLabelName +'" />' +
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
                    },null,{
                        DefText : '请选择'
                    });
                } else if(dynamicDictJson) {
                    AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", null, function(dictValue, item) {
                        $('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
                        _capDynamicContent($('#dynamicContent').val(), dictValue);
                    },null, {
                        DataSrc: eval('(' + dynamicDictJson + ')'),
                        DefText : '请选择'
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
            $('#dynamicContentAreaTr').remove();

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
                $('#mainDiv').getNiceScroll().resize();
            }
        } else if($('#dynamicContentAreaTr').length == 1) {
            $('#dynamicContentAreaTr').hide();
            $('#dynamicContent').validatebox({
                required: false
            });
        }

        if(isShowTextExtra) {
            $('#dynamicContentAreaTrExtra').remove();

            if($('#dynamicContentAreaTrExtra').length == 0) {
                $('#remarkTr').before(
                    '<tr id="dynamicContentAreaTrExtra">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.textLabelNameExtra + '：</span></label>' +
                    '<textarea rows="3" style="height:50px;" id="dynamicContentExtra" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent(null,null,$(this).val());"></textarea>' +
                    '</td>' +
                    '</tr>'
                );

                var winWidth = $(window).width();
                $("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
                    $(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
                        .addClass("isSettledAutoWidth");
                });
                $('#mainDiv').getNiceScroll().resize();
            }
        } else if($('#dynamicContentAreaTrExtra').length == 1) {
            $('#dynamicContentAreaTrExtra').hide();
            $('#dynamicContentExtra').validatebox({
                required: false
            });
        }

        if(isShowRegion) {
            if($('#dynamicContentRegionTr').length == 0) {
                $('#remarkTr').before(
                    '<tr id="dynamicContentRegionTr">' +
                    '<td>' +
                    '<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.regionLabelName + '：</span></label>' +
                    '<input type="text" id="dynamicContentRegionName" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\'"/>' +
                    '<input type="hidden" id="dynamicContentRegionCode" name="regionCode" />' +
                    '<input type="hidden" id="dynamicContentRegionPath" />' +
                    '</td>' +
                    '</tr>'
                );

                //重新选择所属区域
                AnoleApi.initGridZtreeComboBox("dynamicContentRegionName", null,
                    function(gridId, items) {
                        if (items && items.length > 0) {
                            $('#dynamicContentRegionPath').val(items[0].gridPath);
                            $("#dynamicContentRegionCode").val(items[0].orgCode);
                            _capDynamicContent();

                            if(formTypeId =='360' && curNodeName == 'task6' && nextNodeName == 'task2') {
                                BaseWorkflowNodeHandle.checkRadio($('#_node_' + $('#nodeId').val()), {
                                    isClearNextUser: false,
                                    ajaxData: {
                                        isAlterBenchmark: true,
                                        benchmarkRegionCode: items[0].orgCode
                                    }
                                });
                            }
                        }
                    },
                    {
                        ChooseType:"1",
                        ShowOptions:{
                            AllowSelectLevel:"6"//选择到哪个层级
                        }
                    }
                );
            }

            $('#dynamicContentRegionName').validatebox({
                required: true
            });

            $('#dynamicContentRegionTr').show();
        }  else if($('#dynamicContentRegionTr').length == 1) {
            $('#dynamicContentRegionTr').hide();
            $('#dynamicContentRegionName').val('');
            $('#dynamicContentRegionName').validatebox({
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

        //判断是否上传处理后图片
        var isUploadHandledPic = data.isUploadHandledPic||false;
        var isUploadHandlingPic = data.isUploadHandlingPic||false;
        $('#isUploadHandledPic').val(isUploadHandledPic);
        $('#isUploadHandlingPic').val(isUploadHandlingPic);
        $('#picTypeName').val(data.picTypeName||'');

        //当前环节不展示家庭人员选择
        //当前环节为会签节点 且下一环节非 归档环节
        //进行办理意见的初始化回填
        //if(!isShowMemberChoose){
            _capDynamicContent();
        //}
        
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

        if($('#dynamicContentAreaTrExtra').length == 1 && $('#dynamicContentAreaTrExtra').is(':visible')) {
            $('#dynamicContentExtra').validatebox({
                required: true
            });
        }

        var curNodeName = $('#curNodeName').val();
        var nextNode = $('#nodeName_').val();

        //flowSaveForm流程办理页面先进行验证
        var isValid =  $("#flowSaveForm").form('validate');
        if(isValid){
            //判断是否上传处理后图片
            var isUploadHandledPic = $('#isUploadHandledPic').val()||false;
            var isUploadHandlingPic = $('#isUploadHandlingPic').val()||false;
            //处理后
            var typeName = $('#picTypeName').val();
            //个性化参数
            var optionObj = {
                isUseOption:true,
                msg:typeName
            };
            //校验附件文书是否上传成功校验结果，默认上传成功，当环节不需要强制上传附件时，可以正常提交工作流
            var isValid = true;
            //需要开启个性化参数，提示信息中附件名称根据传参确定
            //处理后
            if(isUploadHandledPic == true || isUploadHandledPic == 'true'){
                isValid = verifyAttIsUpload(typeName,3,isUploadHandledPic,optionObj);
            }
            //处理中
            if(isUploadHandlingPic == true || isUploadHandlingPic == 'true'){
                isValid = verifyAttIsUpload(typeName,2,isUploadHandlingPic,optionObj);
            }
            //由于部分附件上传需要根据动态字典值确定上传类型 不进行后台联动、判断在页面进行
            //整改到位办结归档的，处理中（已在后台处理）、此处增加处理后图片必填
            if(isValid){
                if(curNodeName == 'task2' && nextNode == 'end1'){
                    if($('#dictLabelNameSpan').val() == '核实结果' && $('#dynamicDictId').val() == '1'){
                        optionObj.msg = '处理后';
                        isValid = verifyAttIsUpload('处理后',3,true,optionObj);
                    }
                }else if((curNodeName == 'task4' || curNodeName == 'task5' || curNodeName == 'task6')
                    && nextNode == 'end1'){
                    optionObj.msg = '处理后';
                    isValid = verifyAttIsUpload('处理后',3,true,optionObj);
                }
            }
        }

        if(isValid){
            if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
                basWorkSubTaskCallback();
            }
        }
        /*if(isValid){
            this.savePPM();
        }*/
    }
    /*function savePPM() {
        var partyMember =  $("#partyMember").val()||$("#partyMemberContent").val();

        $.ajax({
            type: 'POST',
            url: '${rc.getContextPath()}/zhsq/reportPPM/saveReportFocus.json',
            data: {'reportId':$('#reportId').val(), 'reportUUID': $('#reportUUID').val(),'reportType':$('#reportType').val(),'partyMember':partyMember,},
            dataType: 'json',
            success: function(data) {
                modleopen();
                if(data.success && data.success == true) {
                    if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
                        basWorkSubTaskCallback();
                    }
                } else {
                    $.messager.alert('错误', data.tipMsg, 'error');
                }
            },
            error: function(data) {
                $.messager.alert('错误', '连接超时！', 'error');
            },
            complete : function() {
                modleclose(); //关闭遮罩层
            }
        });
    }*/

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

    function _capDynamicContent(dynamicContent, dictValue,dynamicContentExtra) {
        var adviceNote = $('#adviceNote').val(),
            advice = '',
            curNodeName = $('#curNodeName').val(),
            nextNodeName = $('#nodeName_').val(),
            formTypeId = $('#formTypeId').val();

        if(adviceNote) {
            var dynamicContentLabel = '@dynamicContent@',
                dynamicContentLabelExtra = '@dynamicContentExtra@',
                dynamicContentNextOrgNameLabel = '@nextOrgNames@',
                dynamicContentRegionPathLabel = '@nextRegionPath@';

            advice = adviceNote;

            if(advice.indexOf(dynamicContentLabel) >= 0 && dynamicContent) {
                advice = advice.replaceAll('@dynamicContent@', dynamicContent);

               //如果包含两个文本输入框 dynamicContent dynamicContentExtra 本次先替换掉消息模板中的 dynamicContent
                if(advice.indexOf('@dynamicContentExtra@') >= 0){
                    var dynamicContentExtra = $('#dynamicContentExtra').val();
                    if(isNotBlankStringTrim(dynamicContentExtra)){
                        advice = advice.replaceAll('@dynamicContentExtra@', dynamicContentExtra);
                    }
                }
            }
            if(advice.indexOf(dynamicContentLabelExtra) >= 0 && dynamicContentExtra) {
                advice = advice.replaceAll('@dynamicContentExtra@', dynamicContentExtra);

                //如果包含两个文本输入框 dynamicContent dynamicContentExtra 本次先替换掉消息模板中的 dynamicContentExtra
                if(advice.indexOf('@dynamicContent@') >= 0){
                    var dynamicContent = $('#dynamicContent').val();
                    if(isNotBlankStringTrim(dynamicContentExtra)){
                        advice = advice.replaceAll('@dynamicContent@', dynamicContent);
                    }
                }
            }

            if(advice.indexOf(dynamicContentNextOrgNameLabel) >= 0) {
                var nextOrgNames = $('#nextOrgNames').val();

                if(nextOrgNames) {
                    advice = advice.replaceAll(dynamicContentNextOrgNameLabel, nextOrgNames);
                }
            }

            if(advice.indexOf(dynamicContentRegionPathLabel) >= 0) {
                var nextRegionPath = $('#dynamicContentRegionPath').val();

                if(nextRegionPath) {
                    advice = advice.replaceAll(dynamicContentRegionPathLabel, nextRegionPath);
                }
            }

            if(advice.indexOf(dynamicContentLabel) >= 0
                || advice.indexOf(dynamicContentLabelExtra) >= 0
                || advice.indexOf(dynamicContentNextOrgNameLabel) >= 0
                || advice.indexOf(dynamicContentRegionPathLabel) >= 0) {
                advice = '';
            }
        }

        $('#advice').val(advice);
    }

    function _changeAdviceNote(textVal,onlyChangeMember) {
        var adviceNote = $('#adviceNote').val();
        var advice = $('#advice').val();
        if(adviceNote) {
            var dynamicContentLabel = '@memberName@';
            adviceNote = adviceNote.replaceAll(dynamicContentLabel, textVal);
            $('#adviceNote').val(adviceNote);

            //展示 dynamicContentAreaTr
            if($('#dynamicContentAreaTr').length == 1 && !$('#dynamicContentAreaTr').is(':visible')){
                $('#dynamicContentAreaTr').show();
                $('#dynamicContent').validatebox({
                    required: true
                });
                $('#mainDiv').getNiceScroll().resize();
            }
            if(isBlankStringTrim(advice) && onlyChangeMember){
                $('#advice').val(adviceNote);
            }
        }
    }

    function showWorkflowDetail() {//流程详情
        var instanceId = "${instanceId!}";
        if(instanceId) {
            modleopen();
            $("#workflowDetail").panel({
                height:'auto',
                width:'auto',
                overflow:'no',
                href: "${rc.getContextPath()}/zhsq/reportEHT/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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

                    modleclose();
                }
            });
        }
    }
    <#if feedbackCount?? && (feedbackCount > 0)>
    function fetchEPCFeedback() {
        var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
        $("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
        $("#feedbackListDiv > iframe").width($("#workflowDetail").width());
        $("#feedbackListDiv").height('auto');
    }
    </#if>
</script>

<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>