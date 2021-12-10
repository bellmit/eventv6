<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>事件新增-晋江</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
    <link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
<#include "/component/commonFiles-1.1.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
    <script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
    <script type="text/javascript" src="${(GEO_URL)}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>

    <style type="text/css">
        .LabName{height:30px;}
    </style>
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div>
    <form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
        <input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
        <input type="hidden" id="gridCode" name="gridCode" value="${event.gridCode!}">
        <input type="hidden" id="type" name="type" value="${event.type!}" />
        <input type="hidden" id="code" name="code" value="" />
        <input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
        <!--办理意见-->
        <input type="hidden" id="result" name="result" value="${event.result!}" />

        <!--用于地图-->
        <input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" />
        <input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
        <!--事件上报关联模块-->
        <input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
        <input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
        
        <!--外部传入的办理意见-->
        <input type="hidden" id="_outerAdvice" value="${advice!}" />
		
        <!--晋江涉事人员-->
        <input type="hidden" id="peopleListJson" name="peopleListJson" />
        <input type="hidden" id="isInvolvedPeopleAltered" name="isInvolvedPeopleAltered" value="true"/>
        <input type="hidden" name="committee" id="committee"/>
        <input type="hidden" name="nationality" id="nationality"/>
        <input type="hidden" name="resMarker.x" id="x"/>
        <input type="hidden" name="resMarker.y" id="y"/>
        <input type="hidden" name="resMarker.mapType" id="mapType"/>

        <div style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
            <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
                <div id="norFormDiv" class="NorForm" style="width:784px;">
                    <div class="fl" style="width:67%;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="LeftTd">
                                    <label class="LabName"><span>事件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd">
                                    <label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="${event.eventName!}" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd">
                                    <label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd">
                                    <label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label>
                                    <input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="${event.occurred!}" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="LeftTd" style="border-bottom:none;">
                                    <label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="width:405px; height:108px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" >${event.content!}</textarea>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="fr" style="width:33%;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td>
                                <#if event.code??>
                                    <label class="LabName"><span>事件编号：</span></label><div class="Check_Radio">${event.code}</div>
                                <#else>
                                    <label class="LabName"><span></span></label>
                                </#if>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label class="LabName"><span>影响范围：</span></label>
                                    <input type="hidden" id="influenceDegree" name="influenceDegree" value="${event.influenceDegree!}" />
                                    <input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="${event.influenceDegreeName!}" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label class="LabName"><span>信息来源：</span></label>
                                    <input type="hidden" id="source" name="source" value="${event.source!}" />
                                    <input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="${event.sourceName!}" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label class="LabName"><span>紧急程度：</span></label>
                                    <input type="hidden" id="urgencyDegree" name="urgencyDegree" value="${event.urgencyDegree!}" />
                                    <input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="${event.urgencyDegreeName!}" />
                                </td>
                            </tr>
                            <tr>
                                <td style="border-bottom:none;">
                                    <label class="LabName"><span>涉及人数：</span></label>
                                    <input  id="involvedNumInt" name="involvedNumInt" style="width: 40%;float: left;" class="inp1 easyui-validatebox" data-options="validType:'numLength[3]',required:true,tipPosition:'bottom'" value="${event.involvedNumInt!'0'}" />
                                    <label><span style="float: left;margin-top: 4px;margin-left: 7%;color: #7c7c7c;">（人）</span></label>
                                </td>
                            </tr>
                            <tr>
                                <td style="border-bottom:none;">
                                    <label class="LabName"><span>联系人员：</span></label><input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${event.contactUser!}" />
                                </td>
                            </tr>
                            <tr>
                                <td style="border-bottom:none;">
                                    <label class="LabName"><span>联系电话：</span></label><input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="${event.tel!}" />
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="clear" style="border-top:1px dotted #cecece;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr id="adviceTr" class="hide">
                                <td class="LeftTd">
                                    <label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="LeftTd">
                                    <label class="LabName"><span>涉及人员：</span></label>
                                    <input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="<#if event.eventInvolvedPeople??>${event.eventInvolvedPeople}</#if>" />
                                    <input type="hidden" name="involvedPersion" id="involvedPersion" value="<#if event.involvedPersion??>${event.involvedPersion}</#if>" />
                                    <div class="addinfo" style="width:667px;">
                                        <a href="javascript:void(0)" class="NorToolBtn AddBtn" style="background-color: #fec434;border-radius: 3px;margin-top: 3px;" onclick="addInvoledPeople();">新增</a>
                                    </div>
                                </td>
                            </tr>
                            <table id="involvedPeopleList">

                                    <tr>
                                        <td style="width: 78px;"></td>
                                        <td style="width: 26%;">
                                            <label class="LabName" style="text-align: left"><span><font color="red">*</font>姓名</span></label>
                                        </td>
                                        <td style="width: 23%;">
                                            <label class="LabName" style="text-align: left"><span><font color="red">*</font>证件类型</span></label>
                                        </td>
                                        <td style="width: 25%;">
                                            <label class="LabName" style="text-align: left"><span><font color="red">*</font>证件号码</span></label>
                                        </td>
                                        <td style="width: 21%;">
                                            <label class="LabName" style="text-align: left"><span>联系方式</span></label>
                                        </td>
                                    </tr>

                                    <#if involvedPeopleList?? && (involvedPeopleList?size >0)>
                                    <input id="numOfInvolvedPeople" type="hidden" value="${involvedPeopleList?size}"/>
                                    <#list involvedPeopleList as l >
                                        <tr>
                                            <td>
                                                <a href="javascript:void(0)" class="NorToolBtn DelBtn" style="background-color: #fec434;border-radius: 3px;white-space:nowrap;margin-left: 10px;margin-top: 4px;" onclick="delInvoledPeople(this);">删除</a>
                                            </td>
                                            <td style="width: 24%;">
                                                <input id="involvedPeopleName${l_index}" value="${l.name!''}" class="inp1" style="width: 100px;" onblur="getPerson(${l_index})"/>
                                            </td>
                                            <td style="width: 22%;">
                                                <input id="cardType${l_index}" type="hidden" class="inp1" />
                                                <input id="cardTypeName${l_index}" class="inp1 easyui-validatebox" style="width: 90px;"/>
                                            </td>
                                            <td style="width: 24%;">
                                                <input id="cardNumber${l_index}" value="${l.idCard!''}" class="inp1 " style="width: 140px;" onblur="getPerson(${l_index})"/>
                                            </td>
                                            <td style="width: 21%;">
                                                <input id="phoneNumber${l_index}" value="${l.tel!''}" class="inp1" style="width: 100px;"/>
                                            </td>
                                            <td>
                                                <input id="ciRsId${l_index}" value="${l.ciRsId!''}" type="hidden"/>
                                            </td>
                                        </tr>
                                    </#list>
                                </#if>
                            </table>
                            <tr>
                                <td class="LeftTd">
                                    <label class="LabName"><span>附件上传：</span></label><div class="ImgUpLoad" id="fileupload"></div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>

            <div class="BigTool">
                <div class="BtnList">
                <#if isShowSaveBtn?? && !isShowSaveBtn>
                <#else>
                    <a href="###" onclick="showAdvice('saveEvent');" class="BigNorToolBtn SaveBtn">保存</a>
                </#if>

                <#if (isReport?? && isReport)>
                    <a href="###" onclick="showAdvice('saveEventAndReport');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
                <#else>
                    <a href="###" onclick="showAdvice('saveEventAndStart', 'startWorkFlow');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
                </#if>

                <#if isShowCloseBtn?? && !isShowCloseBtn>
                <#else>
                    <a href="###" onclick="showAdvice(null, null, '1');" class="BigNorToolBtn BigJieAnBtn">结案</a>
                </#if>
                </div>
            </div>
        </div>
    </form>
</div>
<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>

<script type="text/javascript">
    $(function() {

        //当前用户缺少信息权时直接关闭页面
        var startDivisionCode = "${START_DIVISION_CODE!}";
        if ('-1' == startDivisionCode) {
            if(parent && typeof parent.msgPage === 'function') {
                parent.msgPage('当前用户缺少信息权，请联系管理员配置！');
            } else {
                $.messager.alert('提示','当前用户缺少信息权，请联系管理员配置！','INFO');
                return;
            }

        }

        var options = {
        	axis : "yx",
        	theme : "minimal-dark"
        },
        isUploadHandlingPic = <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>,
        radioList = [{'name':'处理前', 'value':'1'},{'name':'处理后', 'value':'3'}],
        eventSeq = "1,3",
        swfOpt = null;

        enableScrollBar('content-d',options);

        $("#norFormDiv").width($(window).width());
        $("#advice").width($(window).width() * 0.85);

        if(isUploadHandlingPic){
            eventSeq = "1,2,3";
            radioList = [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}];
        }
        
        swfOpt = {
            positionId:'fileupload',//附件列表DIV的id值',
            type:'add',//add edit detail
            initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
            imgDomain:'${imgDownPath!}',//图片域名 type为add或者edit时，生效
            context_path:'${SQ_FILE_URL}',
            ajaxData: {'eventSeq':1},//未处理
            appCode:'zhsq_event',
            file_types:'*.jpg;*.gif;*.png;*.jpeg;*.webp;*.zip;*.7z;*.txt;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.pdf;*.rar;',
            radio_list: radioList,
            showPattern: 'list',
            file_size_limit: '50 MB'
        };
        
        <#if event.eventId?? || attachmentIds??>
        	swfOpt["type"] = 'edit';
        	swfOpt["ajaxData"] = {'attachmentType':'${EVENT_ATTACHMENT_TYPE!}','eventSeq':eventSeq};
        	
        	<#if event.eventId??>
        		swfOpt["ajaxData"].bizId = '${event.eventId?c}';
        	</#if>
        	
        	<#if attachmentIds??>
        		swfOpt["ajaxData"].attachmentIds = "${attachmentIds!}";
        	</#if>
        </#if>

        swfUpload1 = fileUpload(swfOpt);

        var typesDictCode = "${typesDictCode!}";
        if(isNotBlankString(typesDictCode) && typesDictCode!="null") {
            AnoleApi.initTreeComboBox("typeName", "type", {
                "${bigTypePcode!}" : [${typesDictCode!}]
            }, null, null, {//0 展示指定的字典；1 去除指定的字典；
                FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>"
            });
        } else {
            AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}");
        }


        AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "${influenceDegreePcode}", null, [<#if event.influenceDegree??>"${event.influenceDegree}"<#else>"01"</#if>]);
        AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "${urgencyDegreePcode}", null, [<#if event.urgencyDegree??>"${event.urgencyDegree}"<#else>"01"</#if>]);
        AnoleApi.initListComboBox("sourceName", "source", "${sourcePcode}", null, [<#if event.source??>"${event.source}"<#else>"01"</#if>]);

        /*初始化涉及人员表单*/
        <#if involvedPeopleList?? && (involvedPeopleList?size > 0)>
        	var numOfInvolvedPeople = $("#numOfInvolvedPeople").val();
        	for (var i = 0;i < numOfInvolvedPeople;i++) {
        		//初始化数据字典，(证件类型数据字典)
        		AnoleApi.initListComboBox("cardTypeName"+ i, "cardType"+ i, "D030001",null,null);
        		
        		$("#involvedPeopleName" + i).validatebox({
        			required:true,
        			validType: ['maxLength[20]'],
        			tipPosition: 'bottom'
        		});
        		
        		$("#cardTypeName" + i).validatebox({
        			required:true,
        			validType: ['maxLength[100]'],
        			tipPosition: 'bottom'
        		});
        		
        		$("#cardNumber" + i).validatebox({
        			required:true,
        			validType: ['maxLength[50]'],
        			tipPosition: 'bottom'
        		});
        		
        		$("#phoneNumber" + i).validatebox({
        			validType:'mobileorphone',
        			tipPosition: 'bottom'
        		});
        	}
        </#if>

        //初始化事发详址控件
        $("#occurred").anoleAddressRender({
            _source : 'XIEJING',//必传参数，数据来源
            _select_scope : 0,
            _show_level : 6,//显示到哪个层级
            _startAddress :"${event.occurred!}",
            _startDivisionCode : "${START_DIVISION_CODE!}", //默认选中网格，非必传参数


            BackEvents : {
                OnSelected : function(api) {
                    $("#nationality").val(api.addressData._startCountryCode);
                    $("#occurred").val(api.getAddress());
                    $("#x").val(api.getAddressMapX());
                    $("#y").val(api.getAddressMapY());
                    $("#mapType").val(${event.resMarker.mapType!'5'});
                    //获取infoOrgCode，地址库控件选取到网格后自动回填所属网格编码，并且置空默认网格id，保存事件时后台会根据gridCode自动转换出gridId
                    $("#gridCode").val(api.getInfoOrgCode());
                    $("#gridId").val('');

                    if(api.getAddressCode().length >= 12){
                        var committee = api.addressData._province + api.addressData._county + api.addressData._street + api.addressData._community
                        if(committee.endsWith("社区")){
                            committee += "居委会";
                        }else if(committee.endsWith("村")){
                            committee += "委会";
                        }
                        $("#committee").val(committee);
                    }else{
                        $("#committee").val("");
                    }
                },
                OnCleared : function(api) {
                    //清空按钮触发的事件
                    $("#occurred").val('');
                    $("#committee").val("");
                }
            }
        });


    });

    function showAdvice(m, callback, toClose) {//展示办理意见
        if(toClose && toClose == '1') {
            $('#advice').validatebox({
                required: true
            });

            $("#adviceTr").show();

            closed();
        } else {
            $("#adviceTr").hide();

            $('#advice').validatebox({
                required: false
            });

            $('#advice').val($('#_outerAdvice').val());

            tableSubmit(m, callback, toClose);
        }
    }

    function tableSubmit(m, callback, toClose) {
        var isValid =  $("#tableForm").form('validate'),
                advice = $("#advice").val(),
                isAdviceVisible = $("#adviceDiv").is(":visible");

        if(isValid) {
            var type = $("#type").val();

            if(isBlankParam(toClose)){
                toClose = "";
            } else if(toClose == "1") {
                $("#result").val($("#advice").val());
            }

            //保存涉及人员
            involvedPeopleList();

            $("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);

            modleopen();
            $("#tableForm").ajaxSubmit(function(data) {
                if(data.eventId) {
                    var iframeUrl = "${iframeUrl!}",
                            outerCallBack = "${callBack!}";

                    if(callback) {//为了保证提交、上报、结案能正常操作
                        eval(callback)(data);
                    } else if(iframeUrl && outerCallBack){//跨域回调
                        if(iframeUrl.indexOf('?') != -1) {
                            iframeUrl += "&";
                        } else {
                            iframeUrl += "?";
                        }

                        data.isCrossDomain = true;
                        iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
                        $("#crossOverIframe").attr("src", iframeUrl);
                    } else if(outerCallBack) {//本域回调
                        eval(outerCallBack)(data);
                    } else {//自身调用回调
                        var msg = "添加";
                    <#if event.eventId??>
                        m = "editEvent";
                        msg = "更新";
                    </#if>
                    <#if (isReport?? && isReport)>
                        msg = "上报";
                    </#if>

                        if(data.result){
                            msg += "成功";
                        }else{
                            msg += "失败";
                        }

                        flashData(msg, data.type);
                    }
                } else {
                    modleclose();
                    $.messager.alert('错误', '保存事件失败，请重试！', 'error');
                }
            });
        }
    }

    function closed() {//事件结案
        var isValid =  $("#tableForm").form('validate');
        if(isValid){
            var eventId = $("#eventId").val();

            if(eventId && eventId != "") {
                modleopen();
                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
                    data: 'eventId='+ eventId +'&toClose=1&advice='+$("#advice").val(),
                    dataType:"json",
                    success: function(data) {
                        if(startWorkFlow != undefined) {
                            startWorkFlow(data);
                        } else {
                            $.messager.alert('错误','事件结案失败！','error');
                        }
                    },
                    error:function(data){
                        $.messager.alert('错误','事件结案，连接错误！','error');
                    }
                });
            } else {
                tableSubmit('saveEventAndStart', 'startWorkFlow', "1");
            }
        }
    }

    function startWorkFlow(data) {//启动流程
        var formId = data.formId,
                new_workFlowId = data.workflowId,
                wftypeId = data.wftypeId,
                orgCode = data.orgCode,
                orgType = data.orgType,
                toClose = data.toClose,
                advice = data.advice;

        //启动流程
        $.ajax({
            //type: "POST",
            url : '${rc.getContextPath()}/zhsq/workflow/workflowController/startFlow.jhtml',
            data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose, 'advice': advice},
            dataType:"json",
            success: function(data) {
                modleclose();
                if(data.result){
                    var instanceId = data.instanceId;
                    if(isNotBlankString(instanceId)) {
                        var iframeUrl = "${iframeUrl!}",
                                outerCallBack = "${callBack!}";

                        if(iframeUrl && outerCallBack) {//跨域回调
                            if(iframeUrl.indexOf('?') != -1) {
                                iframeUrl += "&";
                            } else {
                                iframeUrl += "?";
                            }

                            data.isCrossDomain = true;
                            data.eventId = formId;

                            iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
                            $("#crossOverIframe").attr("src", iframeUrl);
                        } else {
                            var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&eventId="+formId+"&cachenum=" + Math.random();
                            if(outerCallBack) {
                                if(toClose == '0') {
                                    window.location = url;
                                }
                                eval(outerCallBack)(data);
                            } else {
                                if(toClose == '0') {
                                    showMaxJqueryWindow("事件办理", url, null, null, null, null, flashData);
                                } else if(toClose == '1') {
                                    try{
                                        closeMaxJqueryWindow();//新增弹出窗口的关闭方法
                                    }catch(e){}
                                    $.messager.alert('','事件结案成功！','info',function() {
                                        flashData();
                                    });
                                }
                            }
                        }
                    }
                } else {
                    var msg = data.msgWrong || "事件启动失败！";

                    try {
                        closeMaxJqueryWindow();//新增弹出窗口的关闭方法
                    } catch(e) {
                    }

                    $.messager.alert('错误',msg,'error');
                }
            },
            error:function(data) {
                $.messager.alert('错误','事件流程未启动成功！','error');
            }
        });
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

    function flashData(msg) {
        $("#tableForm").attr("action",'${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson={isReport:false}');
        if(isNotBlankString(msg)) {
            $.messager.alert('', msg, 'info',function(){
                $("#tableForm").submit();
            });
        } else {
            $("#tableForm").submit();
        }
    }

    <#if involvedPeopleList??>
    var flag = "${involvedPeopleList?size + 1}";
    <#else >
    var flag = 1;
    </#if>
    function addInvoledPeople(){
        var str = "";
        str = '<tr id="involvedPeopleTr_'+ flag +'">' +
                '<td>' +
                    '<a href="javascript:void(0)" class="NorToolBtn DelBtn" style="background-color: #fec434;border-radius: 3px;white-space:nowrap;margin-left: 10px;margin-top: 4px;" onclick="delInvoledPeople(this);">删除</a>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="involvedPeopleName' + flag + '" class="inp1" style="width: 100px;" onblur="getPerson(' + flag + ')"/>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="cardType' + flag + '" type="hidden" class="inp1"/>' +
                    '<input id="cardTypeName' + flag + '" class="inp1" style="width: 90px;" />' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="cardNumber' + flag + '" class="inp1" style="width: 140px;" onblur="getPerson(' + flag + ')"/>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="phoneNumber' + flag + '" class="inp1" style="width: 100px;"/>' +
                '</td>' +
                '<td>' +
                    '<input id="ciRsId' + flag + '" type="hidden"/>' +
                '</td>' +
                '</tr>';
        $("#involvedPeopleList").append(str);

        $("#involvedPeopleName" + (flag)).validatebox({
            required:true,
            validType: ['maxLength[20]'],
            tipPosition: 'bottom'
        });
        $("#cardTypeName" + (flag)).validatebox({
            required:true,
            tipPosition: 'bottom'
        });

        $("#cardNumber" + (flag)).validatebox({
            required:true,
            validType: ['idcard'],
            tipPosition: 'bottom'
        });

        $("#phoneNumber" + (flag)).validatebox({
            validType: 'mobileorphone',
            tipPosition: 'bottom'
        });

        //加载数据字典，(证件类型数据字典)
        AnoleApi.initListComboBox("cardTypeName"+flag, "cardType"+flag, "D030001",function(value,item,inobj) {
            var num = inobj.settings.TargetId.replace(/[^0-9]/ig,"");//inobj当前对象，inobj.settings.TargetId当前对象id值
            getPerson(num);

        },["001"]);

        flag++;
    }

    function delInvoledPeople(obj){
        $(obj).parent().parent().remove();
    }

    //判断涉及人员姓名身份证号是否存在
    function getPerson(num){
        //人员姓名
        var name = $("#involvedPeopleName"+num).val();
        //证件号码
        var cardNumber = $("#cardNumber"+num).val();

        //证件类型证件类型为身份证时验证证件号码类型
        var cardType = $("#cardType"+num).val();
        var options = {required:true, tipPosition: 'bottom',validType:['maxLength[50]']};
        if (cardType == '001') {
            options.validType = ['idcard'];
        }
        $("#cardNumber" + (num)).validatebox(options);

        //姓名和证件号码同时不为空时获取人员信息
        if (name.length == 0 || cardNumber.length == 0 ) {
            return;
        }
        getNewPerson(num);
    }

    function getNewPerson(num){
        var name = $("#involvedPeopleName"+num).val();
        var cardNumber = $("#cardNumber"+num).val();
        var cardType = $("#cardType"+num).val();


        if (name.length == 0) {
            $.messager.alert('提示','姓名不能为空');
            return;
        }
        if (cardNumber.length == 0) {
            $.messager.alert('提示','证件号码不能为空');
            return;
        }
        if (cardType.length == 0) {
            $.messager.alert('提示','证件类型不能为空');
            return;
        }

        //判断涉事人员是否存在，存在的话提示并清除本条记录
        //涉事人员信息
        if (!involvedPeopleList()) {
            involvedPeopleList();
        } else {
            $("#involvedPeopleName"+(num)).val('') ;
            $("#cardTypeName"+(num)).val('') ;
            $("#cardNumber"+(num)).val('') ;
            $("#phoneNumber"+(num)).val('') ;
            $("#ciRsId"+(num)).val('') ;
            return;
        }

        modleopen();
        $.ajax({
            type:"POST",
            url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/findPerson.json',
            data:{name:name,cardNumber:cardNumber,cardType:cardType},
            dataType:"json",
            success:function (data) {
                modleclose();
                if(data.entity==null){
                    //查询不到人员信息，置空联系方式（）
                    $("#phoneNumber"+(num)).val() == "" ;
                    return;
                }
                //人员信息回填
                $("#cardType"+(num)).val(data.entity.certType) ;
                if ($("#phoneNumber"+(num)).val() == '') {
                    $("#phoneNumber"+(num)).val(data.entity.mobilePhone) ;
                }
                $("#ciRsId"+(num)).val(data.entity.partyId) ;
            },
            error:function(data){
                $.messager.alert('提示','连接超时！');
                //modleclose();
            }

        });
    }

    /*涉及人员数据列表*/
    function involvedPeopleList() {
        var peopleList = [];
        $("#involvedPeopleList").find("tr").each(function () {
            var tdArr = $(this).children();

            var name = tdArr.eq(1).find('input').val(),//姓名
                    cardType = tdArr.eq(2).find('input').val(),//证件类型
                    idCard = tdArr.eq(3).find('input').val(),//证件号码
                    tel = tdArr.eq(4).find('input').val();//联系方式
                    ciRsId = tdArr.eq(5).find('input').val();//联系方式

            if (name != undefined) {
                var obj = new Object();
                obj.name = name;
                obj.cardType = cardType;
                obj.idCard = idCard;
                obj.tel = tel;
                obj.ciRsId = ciRsId;
                peopleList.push(obj);
            }

        });
        //判断涉及人员信息是否重复添加
        var peopleExitence = false;
        for(var i = 0,len = peopleList.length;i < len;i++){
            for(var j = i+1; j < len;j++){
                if (peopleList[i].idCard == peopleList[j].idCard) {
                    $.messager.alert('提示','证件号码为['+peopleList[i].idCard+']姓名为['+peopleList[i].name+']的人员信息已经添加，请勿重复添加！');
                    peopleExitence = true;
                }
            }
        }
        var peopleListJson = JSON.stringify(peopleList);
        $("#peopleListJson").val(peopleListJson);
        return peopleExitence;
    }
</script>
</html>