<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>三会一课-新增/编辑</title>
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
<form id="meetingForm" name="meetingForm" action="" method="post">
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
    <input type="hidden" id="module" value="MEETINGS_AND_LESSON"/>
    <#--信息采集来源-->
    <input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
    <input type="hidden" id="collectSource" name="collectSource" value="${reportFocus.collectSource!}"/>

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
                        <label class="LabName"><span><label class="Asterik">*</label>会议时间：</span></label>
                        <input type="text" id="meetingTime" name="meetingTime" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:37%; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true,maxDate:'#F{$dp.$D(\'reportTime\')}', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${reportFocus.reportTimeStr!}" readonly="readonly"></input>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide"><span><label class="Asterik">*</label>会议类型：</span></label>
                        <input type="hidden" id="meetingType" name="meetingType" value="${reportFocus.meetingType!}" />
                        <input  id="meetingTypeName" type="text" style="width: 37%" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
                    </td>

                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span><label class="Asterik">*</label>所属区域：</span>
                        </label>
                        <input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
                        <input type="text" id="gridName" style="width: 64%" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">

                    </td>
                </tr>
                <tr>
                	<td class="LeftTd">
                        <label class="LabName"><span>报告方式：</span></label>
                        <div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
                        <input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide">
                            <span><label class="Asterik">*</label>党组织名称：</span>
                        </label>
                        <input  id="partyGroupName" name="partyGroupName" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.partyGroupName!}" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>
                                <#if reportFocus.collectSource?? && reportFocus.collectSource == '02'><label class="Asterik">*</label></#if>发生地址：
                            </span>
                        </label>
                        <input type="text" class="inp1 easyui-validatebox autoWidth"
                               data-options="<#if reportFocus.collectSource?? && reportFocus.collectSource == '02'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
                               name="occurred" id="occurred" value="${reportFocus.occurred!}" />
                    </td>
                    <td class="LeftTd hide">
                        <label class="LabName labelNameWide">
                            <span>
                                <#if reportFocus.collectSource?? && reportFocus.collectSource == '02'><label class="Asterik">*</label></#if>地理标注：
                            </span>
                        </label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span><label class="Asterik">*</label>应到会党员：</span>
                        </label>
                        <input type="text" style="width:72%; height:28px;"  class="inp1 easyui-numberbox singleCellInpClass" data-options="min:1,max:999,required:true" name="shouldAttriveNum" id="shouldAttriveNum" value="<#if reportFocus?? && reportFocus.shouldAttriveNum??>${reportFocus.shouldAttriveNum?c}<#else>0</#if>"/>（人）
                    </td>
                    <td class="LeftTd">
                        <label class="LabName labelNameWide">
                            <span><label class="Asterik">*</label>实到会党员：</span>
                        </label>
                        <input type="text" style="width:67%; height:28px;" class="inp1 easyui-numberbox singleCellInpClass" data-options="min:1,max:999,required:true" name="actualNumber" id="actualNumber" value="<#if reportFocus?? && reportFocus.actualNumber??>${reportFocus.actualNumber?c}<#else>0</#if>"  />（人）
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>
                                <#if reportFocus.collectSource?? && reportFocus.collectSource == '02'><label class="Asterik">*</label></#if>图片上传：
                            </span>
                        </label><div id="bigFileUploadDiv"></div>
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
            <a href="###" onclick="saveMeeting(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
            <a href="###" onclick="saveMeeting(0);" class="BigNorToolBtn SaveBtn">保存</a>
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
        
        $('#meetingForm .autoWidth').each(function() {
        	$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
        });
        
        init4Location('occurred', {
        	<#if reportFocus.collectSource?? && reportFocus.collectSource!='02'>
        		//来源是非网格员只需要选择到村社区层级
        		_limit_select_level : 5//选择到哪个层级
        	</#if>
        });
			
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
                attachmentData: {attachmentType:'MEETINGS_AND_LESSON'},
                module: 'meetingsAndLesson',
                individualOpt: {
                    isUploadHandlingPic: true
                }
            },
            reportId = $('#reportId').val();
        var bigViodeUploadOpt = {	
				useType: 'add',
				fileExt: '.mp4,.avi,.amr',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'MEETINGS_AND_LESSON'},
				module: 'meetingsAndLesson',
				individualOpt: {
					isUploadHandlingPic: true
				}
			};
        var attachFileUploadOpt = {
            useType: 'add',
            fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
            labelDict: [{'name':'处理前', 'value':'1'}],
            attachmentData: {attachmentType:'MEETINGS_AND_LESSON'},
            module: 'meetingsAndLesson',
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

        AnoleApi.initTreeComboBox("meetingTypeName", "meetingType", {
            "B210006001" : [${meetingTypeDictCodes!}]
        }, null, ["${reportFocus.meetingType!}"]);

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
                    <#if reportFocus.collectSource??>
                        //来源是网格的 选择到网格层级
                        <#if reportFocus.collectSource=='02'>
                            AllowSelectLevel:"6"
                        <#elseif reportFocus.collectSource == '01' || reportFocus.collectSource == '03' || reportFocus.collectSource == '04'>
                            //来源是村社区、乡镇街道的需要选择到村社区或者网格层级
                            AllowSelectLevel:"5,6"
                        <#elseif reportFocus.collectSource == '05' || reportFocus.collectSource == '06'>
                            //市职部门选择到市级
                            AllowSelectLevel:"3,4,5,6"
                        </#if>
                    <#else >
                        AllowSelectLevel:"6"
                    </#if>
                }
            }
        );

        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };

        enableScrollBar('content-d',options);
    });

    function saveMeeting(btnType) {
        var isValid =  $("#meetingForm").form('validate'),
            longitude = $('#x').val(),
            latitude = $('#y').val(),
            mapType = $('#mapt').val(),
            collectSource = $('#collectSource').val();
            
        isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
            && checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
        
        if(isValid) {
            //二级网格采集的 地理标注 处理前图片必填
            if(collectSource == '02'){
                if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
                    $.messager.alert('警告', "请先完成地理标注！", 'warning');
                    return;
                } else {
                	isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
                }
            }

            if(isValid){
                var shouldAttriveNum = parseInt($('#shouldAttriveNum').val(), 10);
                var actualNumber = parseInt($('#actualNumber').val(), 10);
                if(actualNumber > shouldAttriveNum){
                    $.messager.alert('提示', '应到会党员应大于等于实到会党员！', 'info');
                    return;
                }
            }
        }

        if(isValid) {
            modleopen();
            var isStart = btnType == 1;

            $("#meetingForm").attr("action", "${rc.getContextPath()}/zhsq/reportMeeting/saveReportFocus.jhtml");

            $('#isStart').val(isStart);

            $("#meetingForm").ajaxSubmit(function(data) {
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
            mapType = 'MEETINGS_AND_LESSON',
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
    
    $(window).resize(function() {
    	var winHeight = $(window).height(), winWidth = $(window).width();
    	
    	if(winHeight != _winHeight || winWidth != _winWidth) {
    		_winHeight = winHeight;
    		_winWidth = winWidth;
    		
    		$('#meetingForm .MC_con').height(winHeight - $('#meetingForm .BigTool').outerHeight());
    		$('#meetingForm .autoWidth').each(function() {
    			$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
    		});
    	}
    });
</script>

<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>