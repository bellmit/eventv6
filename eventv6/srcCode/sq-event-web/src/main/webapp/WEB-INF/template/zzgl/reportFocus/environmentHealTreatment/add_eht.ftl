<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>环境卫生整治-新增/编辑</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
    <#include "/component/standard_common_files-1.1.ftl" />
    <script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <#include "/component/bigFileUpload.ftl" />
    <#include "/component/ComboBox.ftl" />

    <style type="text/css">
        .singleCellInpClass{width: 57%}
        .singleCellClass{width: 62%;}
        .labelNameWide{width: 132px;}
    </style>
</head>
<body>
<form id="ehtForm" name="ehtForm" action="" method="post">
    <input type="hidden" id="isStart" name="isStart" value="false" />
    <input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
    <input type="hidden" name="isSaveResMarkerInfo" value="true" />
    <input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
    <input type="hidden" name="reportUUID" value="${reportFocus.reportUUID!}" />
    <input type="hidden" name="reportType" value="${reportType!}" />
    <!--用于地图-->
    <input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
    <input type="hidden" id="name" name="name" value="" />
    <input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
    <input type="hidden" id="module" value="ENVIRONMENTAL_HEALTH_TREATMENT"/>
    <#--信息采集来源-->
    <input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
    <#--报告方式-->
    <input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />

    <div id="content-d" class="MC_con content light">
        <div id="eventWechatNorformDiv" class="NorForm">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="LeftTd">
                        <label class="LabName"><span>报告人姓名：</span></label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
                        <input type="hidden" name="reporterName" value="${reportFocus.reporterName!}" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide">
                            <span>报告人电话：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reporterTel!''}</div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>数据来源：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${dataSourceName!''}</div>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide">
                            <span>报告方式：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span><label class="Asterik">*</label>所属区域：</span>
                        </label>
                        <input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
                        <input type="text" id="gridName" style="width: 300px;" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox"
                               data-options="required:true,tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide">
                            <span>报告时间：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!''}</div>
                        <input type="hidden" id="reportTime" name="reportTime" value="${reportFocus.reportTimeStr!}"></input>
                    </td>
                </tr>
                <#if reportFocus.dataSource?? && (reportFocus.dataSource?contains('03') || reportFocus.dataSource?contains('04'))>
                    <tr>
                        <td class="LeftTd" colspan="1" id="discoveryChannelTr">
                            <label class="LabName"><span><label class="Asterik">*</label>发现渠道：</span></label>
                            <input type="hidden" id="discoveryChannel" name="discoveryChannel" value="${reportFocus.discoveryChannel!}" />
                            <input  id="discoveryChannelName" type="text" class="inp1 easyui-validatebox singleCellInpClass"
                                    style="width: 300px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
                        </td>
                    </tr>
                </#if>
                <tr>
                    <td class="LeftTd" colspan="1">
                        <label class="LabName">
                            <span><#if reportFocus.dataSource?? && reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>发生地址：</span>
                        </label>
                        <input type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;"
                               data-options="<#if reportFocus.dataSource?? && reportFocus.dataSource == '01'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
                               name="occurred" id="occurred" value="${reportFocus.occurred!}" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide"><span><label class="Asterik">*</label>问题类型：</span></label>
                        <input type="hidden" id="problemType" name="problemType" value="${reportFocus.problemType!}" />
                        <input  id="problemTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass"
                                style="width: 270px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" />
                    </td>
                    <td class="LeftTd hide">
                        <label class="LabName labelNameWide"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    </td>
                </tr>
                <#if reportFocus.dataSource == '02'>
                    <tr>
                        <td class="LeftTd">
                            <label class="LabName">
                                <span><label class="Asterik">*</label>反馈时限：</span>
                            </label>
                            <input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass"
                                   data-options="required:true,tipPosition:'bottom'" style="width:300px; cursor:pointer;"
                                   onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:true, isShowToday:false})"
                                   value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd" colspan="2">
                            <label class="LabName">
                                <span><label class="Asterik">*</label>登记内容：</span>
                            </label>
                            <div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
                            <textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;"
                                      data-options="required:true,tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
                        </td>
                    </tr>
                </#if>
                <tr id="problemDescTr" class="hide">
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span><label class="Asterik">*</label>问题描述：</span>
                        </label>
                        <textarea name="problemDesc" id="problemDesc" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;"
                                  data-options="tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${reportFocus.problemDesc!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>备注：</span>
                        </label>
                        <textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[2000]','characterCheck']">${reportFocus.remark!}</textarea>
                    </td>
                </tr>
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
    </div>

    <div class="BigTool">
        <div class="BtnList">
            <a href="###" onclick="saveEHT(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
            <a href="###" onclick="saveEHT(0);" class="BigNorToolBtn SaveBtn">保存</a>
            <a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
        </div>
    </div>
</form>

<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
</body>

<script type="text/javascript">
    var _winHeight = 0, _winWidth = 0;

    $(function() {
        _winHeight = $(window).height();
        _winWidth = $(window).width();

        //放置在组件初始化之前，以防止组件初始化后导致结构调整，从而影响宽度计算
        $('#ehtForm .autoWidth').each(function() {
            $(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
        });

        init4Location('occurred', {
            <#if reportFocus.dataSource?? && reportFocus.dataSource!='01'>
                //来源是非网格员只需要选择到村社区层级
                _limit_select_level : 5//选择到哪个层级
            </#if>
        });

        //加载网格
        AnoleApi.initGridZtreeComboBox("gridName", null,
            function(gridId, items) {
                if (items && items.length > 0) {
                    $("#regionCode").val(items[0].orgCode);
                }
            },
            {
                ChooseType:"1",
                ShowOptions:{
                    <#if reportFocus.dataSource??>
                        //来源是网格员巡查发现 01 选择到网格层级 其余的精确到村社区
                        <#if reportFocus.dataSource=='01' || reportFocus.dataSource=='' >
                            AllowSelectLevel:"6"//选择到哪个层级
                        <#else>
                            AllowSelectLevel:"5,6"//选择到哪个层级
                        </#if>
                    <#else>
                        AllowSelectLevel:"6"//选择到哪个层级
                    </#if>
                }
            }
        );

        <#if reportFocus.resMarker??>
        var resMarkerX = "${reportFocus.resMarker.x!}",
            resMarkerY = "${reportFocus.resMarker.y!}",
            resMarkerMapType = "${reportFocus.resMarker.mapType!}";

        if(resMarkerX && resMarkerY && resMarkerMapType) {
            callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
        }
        </#if>

        var bigFileUploadOpt = {
                useType: 'add',
                fileExt: '.jpg,.gif,.png,.jpeg,.webp',
                labelDict: [{'name':'处理前', 'value':'1'}],
                attachmentData: {attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT'},
                module: 'environmentHealTreatment',
                individualOpt: {
                    isUploadHandlingPic: true
                }
            },
            reportId = $('#reportId').val();

        var bigViodeUploadOpt = {
            useType: 'add',
            fileExt: '.mp4,.avi,.amr',
            showFileExt: '.mp4,.avi,.amr',
            labelDict: [{'name':'处理前', 'value':'1'}],
            attachmentData: {attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT'},
            module: 'environmentHealTreatment',
            individualOpt: {
                isUploadHandlingPic: true
            }
        };

        var attachFileUploadOpt = {
            useType: 'add',
            fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            labelDict: [{'name':'处理前', 'value':'1'}],
            attachmentData: {attachmentType:'ENVIRONMENTAL_HEALTH_TREATMENT'},
            module: 'environmentHealTreatment',
            individualOpt: {
                isUploadHandlingPic: true
            }
        };

        if(reportId) {
            $.extend(bigFileUploadOpt, {
                useType: 'edit'
            });
            $.extend(bigFileUploadOpt.attachmentData, {
                useType: 'edit',
                eventSeq: '1,2,3',
                bizId: reportId,
                isBindBizId: 'yes'
            });

            $.extend(bigViodeUploadOpt, {
                useType: 'edit'
            });
            $.extend(bigViodeUploadOpt.attachmentData, {
                useType: 'edit',
                eventSeq: '1',
                bizId: reportId,
                isBindBizId: 'yes'
            });

            $.extend(attachFileUploadOpt, {
                useType: 'edit'
            });
            $.extend(attachFileUploadOpt.attachmentData, {
                useType: 'edit',
                eventSeq: '1',
                bizId: reportId,
                isBindBizId: 'yes'
            });
        }

        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
        bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

        //发现渠道
        if($('#discoveryChannelName').length > 0) {
            AnoleApi.initTreeComboBox("discoveryChannelName", "discoveryChannel", "B210014004",
                function (dictValue, item) {

                    var discoveryStaffName = '';

                    if(dictValue == '1'){
                        discoveryStaffName = '发现部门';
                        $('#dataSource').val('03');
                    }else{
                        discoveryStaffName = '发现群众';
                        $('#dataSource').val('04');
                    }

                    if($('#discoveryStaffTr').length > 0){
                        $('#discoveryStaffTr').remove();
                    }

                    var appendHtml = '<td class="LeftTd" id="discoveryStaffTr">' +
                                        '<label class="LabName labelNameWide"><span><label class="Asterik">*</label>'+ discoveryStaffName +'：</span></label>' +
                                        '<input type="text" id="discoveryStaff" name="discoveryStaff" class="inp1 easyui-validatebox" value="${reportFocus.discoveryStaff!}"' +
                                            ' style="width:270px" data-options="tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']"/>' +
                                        '</td>';

                    $('#discoveryChannelTr').after(appendHtml);

                    $('#discoveryStaff').validatebox({
                        required: true
                    });
                },
                ["${reportFocus.discoveryChannel!}"],
                null
            );
        }

        var problemTypeArray = [], problemType = "${reportFocus.problemType!}";

        if(problemType) {
            problemTypeArray = problemType.split(",");
        }

        //问题类型
        if($('#problemTypeName').length > 0) {
            AnoleApi.initTreeComboBox("problemTypeName", "problemType", "B210014003",
                function (dictValue, item) {
                    var flag = isContainOther($('#problemType').val());

                    if(flag) {
                        $('#problemDescTr').show();
                    } else {
                        $('#problemDescTr').hide();
                        $('#problemDesc').val('');
                    }

                    $('#problemDesc').validatebox({
                        required: flag
                    });
                },
                problemTypeArray,
                {
                    RenderType : "01",
                    ShowOptions: {
                        EnableToolbar : true
                    }
                }
            );
        }

        //数据来源
        /*if($('#dataSourceName').length > 0) {
            AnoleApi.initTreeComboBox("dataSourceName", "dataSource", "B210011002", null, ["${reportFocus.dataSource!}"], {
                ShowOptions: {
                    EnableToolbar : true
                }
            });
        }*/

        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };

        enableScrollBar('content-d',options);
    });

    function saveEHT(btnType) {
        var isValid =  $("#ehtForm").form('validate'),
            longitude = $('#x').val(),
            latitude = $('#y').val(),
            mapType = $('#mapt').val(),
            dataSource = $('#dataSource').val();

        if(isValid && isNotBlankStringTrim(dataSource) && dataSource == '01'){
            isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
        }

        isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
            && checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');

        if(isValid) {
            modleopen();
            var isStart = btnType == 1;

            $("#ehtForm").attr("action", "${rc.getContextPath()}/zhsq/reportEHT/saveReportFocus.jhtml");

            $('#isStart').val(isStart);

            $("#ehtForm").ajaxSubmit(function(data) {
                modleclose();

                if(data.success && data.success == true) {
                    if(isStart) {
                        parent.searchData();
                        parent.detail(data.reportUUID, data.instanceId, '2');
                        if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
                            parent.closeBeforeMaxJqueryWindow();
                        }
                    } else {
                        parent.reloadDataForSubPage(data.tipMsg);
                    }
                } else {
                    $.messager.alert('错误', data.tipMsg, 'error');
                }
            });
        }
    }

    function showMap() {
        var callBackUrl = '${SQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
            width = 480, height = 360,
            gridId = "${defaultGridIdStr!'-1'}",
            markerOperation = $('#markerOperation').val(),
            id = $('#id').val(),
            mapType = 'ENVIRONMENTAL_HEALTH_TREATMENT',
            isEdit = true,
            parameterJson = {
                'id' : $('#id').val(),
                'name' : $('#name').val()
            };

        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
    }

    function closeWin() {
        parent.closeMaxJqueryWindow();
    }

    function isContainOther(dictValue) {
        var flag = false;

        if(isNotBlankStringTrim(dictValue)) {
            var dictArray = dictValue.split(','),
                DICT_OTHER = '99';

            for(var index in dictArray) {
                if(dictArray[index] === DICT_OTHER) {
                    flag = true; break;
                }
            }
        }

        return flag;
    }

    $(window).resize(function() {
        var winHeight = $(window).height(), winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            _winHeight = winHeight;
            _winWidth = winWidth;

            $('#ehtForm .MC_con').height(winHeight - $('#ehtForm .BigTool').outerHeight());
            $('#ehtForm .autoWidth').each(function() {
                $(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
            });
        }
    });

</script>

<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>