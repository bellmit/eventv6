<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>致贫返贫监测-新增/编辑</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
    <#include "/component/standard_common_files-1.1.ftl" />
    <script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <#include "/component/bigFileUpload.ftl" />
    <#include "/component/ComboBox.ftl" />

    <#--人口选择器-->
    <script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
    <script type="text/javascript" src="${COMPONENTS_URL}/js/rs/residentSelector.js"></script>

    <style type="text/css">
        .singleCellInpClass{width: 57%}
        .singleCellClass{width: 62%;}
        .labelNameWide{width: 132px;}
    </style>
</head>
<body>
<form id="ppmForm" name="ppmForm" action="" method="post">
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
    <input type="hidden" id="module" value="POVERTY_PRE_MONITOR"/>
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
                            <span>报告时间：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!''}</div>
                        <input type="hidden" id="reportTime" name="reportTime" value="${reportFocus.reportTimeStr!}"></input>
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
                        <label class="LabName labelNameWide"><span>报告方式：</span></label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <input type="hidden" id="partyId" name="partyId" value="<#if reportFocus.partyId??>${reportFocus.partyId?c}</#if>" />
                        <input type="hidden" id="personName" name="personName" value="${reportFocus.personName!}" />
                        <label class="LabName">
                            <span><label class="Asterik">*</label>姓名：</span>
                        </label>
                        <input  id="resident" type="text" class="inp1 easyui-validatebox singleCellInpClass"
                                style="cursor: pointer;width: 310px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']"
                                value="${reportFocus.personName!}"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide"><span>证件号码：</span></label>
                        <input type="hidden" id="cardNumber" name="cardNumber" value="${reportFocus.cardNumber!}" />
                        <div id="cardNumberName" class="Check_Radio FontDarkBlue">${reportFocus.cardNumber!}</div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="1">
                        <label class="LabName">
                            <span><#if reportFocus.dataSource?? && reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>发生地址：</span>
                        </label>
                        <input type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;"
                               data-options="<#if reportFocus.dataSource?? && reportFocus.dataSource == '01'>required: true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
                               name="occurred" id="occurred" value="${reportFocus.occurred!}" />
                    </td>
                    <td class="LeftTd" id="povBackReasonTd">
                        <label class="LabName labelNameWide"><span><label class="Asterik">*</label>致贫返贫原因：</span></label>
                        <input type="hidden" id="povBackReason" name="povBackReason" value="${reportFocus.povBackReason!}" />
                        <input  id="povBackReasonName" type="text" class="inp1 easyui-validatebox singleCellInpClass"
                                style="width: 270px;" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
                    </td>
                    <td class="LeftTd hide">
                        <label class="LabName labelNameWide"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    </td>
                </tr>
                <tr id="ppmRiskTr" style="display: none">
                    <td class="LeftTd" id="ppmRiskTd">
                        <label class="LabName"><span style="display: block;width: 85px;"><label class="Asterik">*</label>存在致贫返贫风险：</span></label>
                        <input type="hidden" id="ppmRisk" name="ppmRisk" value="${reportFocus.ppmRisk!}" />
                        <input  id="ppmRiskName" type="text" class="inp1 easyui-validatebox singleCellInpClass"
                                style="width: 300px;" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
                    </td>
                </tr>
                <tr id="povBackDescTr" class="hide">
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span><label class="Asterik">*</label>致返贫说明：</span>
                        </label>
                        <textarea name="povBackDesc" id="povBackDesc" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']">${reportFocus.povBackDesc!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>备注：</span>
                        </label>
                        <textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div>
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
            <a href="###" onclick="savePPM(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
            <a href="###" onclick="savePPM(0);" class="BigNorToolBtn SaveBtn">保存</a>
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
        var dataSource = '${reportFocus.dataSource!'01'}';

        //放置在组件初始化之前，以防止组件初始化后导致结构调整，从而影响宽度计算
        $('#ppmForm .autoWidth').each(function() {
            $(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
        });

        init4Location('occurred', {
            <#if reportFocus.dataSource?? && reportFocus.dataSource!='01'>
                //来源是非网格员只需要选择到村社区层级
                _limit_select_level : 5//选择到哪个层级
            </#if>
        });

        $('#resident').residentSelector({
            panelHeight : 300,
            panelWidth 	: 550,
            dataDomain 	: '${RS_DOMAIN!}',
            type 		: 'v6resident',
            relCode		: 'T',//户籍人口
            srchType	: '002',//模糊查找
            value		: {
                value	: "<#if reportFocus.partyId??>${reportFocus.partyId?c}</#if>",
                text	: "${reportFocus.personName!}"
            },
            onClickRow 	: function(index, row) {
                $("#resident").val(row.partyName);
                $('#partyId').val(row.partyId);
                $('#personName').val(row.partyName);
                $('#cardNumber').val(row.identityCard);
                $('#cardNumberName').html(row.identityCard);
            },
            afterClear	: function(personName, partyId) {
                $('#partyId').val('');
                $('#personName').val('');
                $("#cardNumber").val('');
                $('#cardNumberName').html('');
            }
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
                attachmentData: {attachmentType:'POVERTY_PRE_MONITOR'},
                module: 'povertyPreMonitor',
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
            attachmentData: {attachmentType:'POVERTY_PRE_MONITOR'},
            module: 'povertyPreMonitor',
            individualOpt: {
                isUploadHandlingPic: true
            }
        };

        var attachFileUploadOpt = {
            useType: 'add',
            fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            showFileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            labelDict: [{'name':'处理前', 'value':'1'}],
            attachmentData: {attachmentType:'POVERTY_PRE_MONITOR'},
            module: 'povertyPreMonitor',
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

        //致贫返贫原因
        //网格员巡查发现、泉州市级领导发现的 新增时 致贫返贫原因非必填
        if(dataSource == '01'){
            $('#povBackReasonTd').hide();
            $('#povBackReasonTr').hide();
        }else{
            if($('#povBackReasonName').length > 0) {
                AnoleApi.initTreeComboBox("povBackReasonName", "povBackReason", "B210011003",
                    function (dictValue, item) {
                        var flag = isContainOther($('#povBackReason').val());

                        if(flag) {
                            $('#povBackDescTr').show();
                        } else {
                            $('#povBackDescTr').hide();
                            $('#povBackDesc').val('');
                        }

                        $('#povBackDesc').validatebox({
                            required: flag
                        });
                    },
                    ["${reportFocus.povBackReason!}"],
                    null
                );
            }
            $('#povBackReasonName').validatebox({required:true});
        }

        //二级网格01、南安市处级以下挂钩帮扶责任人05、第一副网格长的上报06
        if(dataSource == '01' || dataSource == '05' || dataSource == '06'){
            $('#ppmRiskTr').show();
            AnoleApi.initTreeComboBox("ppmRiskName", "ppmRisk", "B210011004",
                null,
                ["${reportFocus.ppmRisk!}"],
                null
            );
            $('#ppmRiskName').validatebox({required:true});
        }else{
            $('#ppmRiskTr').hide();
            $('#ppmRiskName').validatebox({required:false});
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

    function savePPM(btnType) {
        var isValid =  $("#ppmForm").form('validate'),
            longitude = $('#x').val(),
            latitude = $('#y').val(),
            mapType = $('#mapt').val();
        
        isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
            && checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
            && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');

        if(isValid) {
            modleopen();
            var isStart = btnType == 1;

            $("#ppmForm").attr("action", "${rc.getContextPath()}/zhsq/reportPPM/saveReportFocus.jhtml");

            $('#isStart').val(isStart);

            $("#ppmForm").ajaxSubmit(function(data) {
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
            mapType = 'EPIDEMIC_PRE_CONTROL',
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

            $('#ppmForm .MC_con').height(winHeight - $('#ppmForm .BigTool').outerHeight());
            $('#ppmForm .autoWidth').each(function() {
                $(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
            });
        }
    });

</script>

<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>