<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <title>新增执纪问责</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
    <link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
    <script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>

    <style type="text/css">
        .comboxWidth{width: 150px;}
        .wideLabelName{width: 110px;}
        .measureUnit{margin-left: -10px; float: right; padding: 0; text-align: left;}
    </style>
</head>
<body>
<form id="submitForm" name="submitForm" action="${rc.getContextPath()}/zhsq/accountabilityEnforcement/saveProb.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
    <input type="hidden" id="probId" name="probId" value="<#if record.probId??>${record.probId?c}</#if>" />
    <input type="hidden" id="isStart" name="isStart" value="false" />
    <input type="hidden" id="isClose" name="isClose" value="false" />
    <!--是否保存处置信息-->
    <input type="hidden" id="isSaveHandleResult" name="isSaveHandleResult" value="false" />

    <div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
        <div class="NorForm NorForm2">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>问题标题：</span>
                        </label>
                        <input id="probTitle" name="probTitle" type="text"
                               class="inp1 inp2 easyui-validatebox"
                               style="width: 73%;"
                               value="${record.probTitle!''}"
                               data-options="required:true,validType:['maxLength[100]','characterCheck'],tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                    <#if record.probNo??>
                        <label class="LabName wideLabelName"><span>问题编号：</span></label><div class="Check_Radio">${record.probNo}</div>
                    <#else>
                        <label class="LabName"><span></span></label>
                    </#if>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>所属行政辖区：</span>
                        </label>
                        <input type="hidden" id="regionCode" name="regionCode" value="${record.regionCode!regionCode}" />
                        <input type="text" id="gridName" name="gridName" value="${record.regionName!gridName}" class="inp1 comboxWidth easyui-validatebox" data-options="required:true,validType:['maxLength[100]','characterCheck'],tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>对象类别：</span>
                        </label>
                        <input type="hidden" id="violationObjType" name="violationObjType" value="${record.violationObjType!''}" />
                        <input type="text" id="violationObjTypeStr" class="inp1 comboxWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>违纪违规时间：</span>
                        </label>
                        <input type="text" id="violationDateStr" value="${record.violationDateStr!}" name="violationDateStr" onclick="WdatePicker({dateFmt:'yyyy-MM',readOnly:true,isShowClear:false,isShowToday:false,maxDate:'${currentDate?string('yyyy-MM')}'})" class="inp1 InpDisable Wdate easyui-validatebox comboxWidth"  data-options="required:true,tipPosition:'bottom'" style="cursor:pointer;"  />
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>对象姓名：</span>
                        </label>
                        <input type="text" id="objName" name="objName" value="${record.objName!''}" class="inp1 comboxWidth easyui-validatebox"
                               data-options="validType:'maxLength[80]', tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>对象性别：</span>
                        </label>
                        <input type="hidden" id="objSex" name="objSex" value="${record.objSex!''}" />
                        <input type="text" id="objSexStr" class="inp1 comboxWidth easyui-validatebox" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>政治面貌：</span>
                        </label>
                        <input type="hidden" id="objPolitics" name="objPolitics" value="${record.objPolitics!''}" />
                        <input type="text" id="objPoliticsStr" class="inp1 comboxWidth easyui-validatebox" />
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>单位：</span>
                        </label>
                        <input type="text" id="objWorkUnit" name="objWorkUnit" value="${record.objWorkUnit!''}" class="inp1 comboxWidth easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>职务：</span>
                        </label>
                        <input type="text" id="objProfessionPerson" name="objProfessionPerson" value="${record.objProfession!''}" class="inp1 comboxWidth easyui-validatebox"
                               data-options="validType:'maxLength[30]', tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>违纪人员职级：</span>
                        </label>
                        <input type="hidden" id="objProfessionTypePerson" name="objProfessionTypePerson" value="${record.objProfessionTypePerson!''}" />
                        <input type="text" id="objProfessionTypePersonStr" class="inp1 comboxWidth easyui-validatebox"/>
                    </td>
                </tr>
                <tr class="violationUnit hide">
                    <td class="LeftTd">
                        <label class="LabName"><span>单位名称：</span></label>
                        <input type="text" id="objProfessionUnit" name="objProfessionUnit" value="${record.objProfession!''}" class="inp1 comboxWidth easyui-validatebox"
                               data-options="validType:'maxLength[30]', tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName"><span>单位级别：</span></label>
                        <input type="hidden" id="objProfessionTypeUnit" name="objProfessionTypeUnit" value="${record.objProfessionType!''}" />
                        <input type="text" id="objProfessionTypeUnitStr" name="objProfessionTypeUnitStr" class="inp1 comboxWidth easyui-validatebox" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>涉案金额：</span>
                        </label>
                        <input id="amountInvolved" name="amountInvolved" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               value="${record.amountInvolved!''}"
                               data-options="validType:'floatNumStr[12,2]',tipPosition:'bottom'" />
                        <label class="LabName measureUnit">(万元)</label>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>违纪违规金额：</span>
                        </label>
                        <input id="violationMoney" name="violationMoney" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               value="<#if record.violationMoney??>${record.violationMoney?c}</#if>"
                               data-options="validType:'floatNumStr[12,2]',tipPosition:'bottom'" />
                        <label class="LabName measureUnit">(万元)</label>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>违纪违规资金类别：</span>
                        </label>
                        <input type="hidden" id="violationMoneyType" name="violationMoneyType" value="${record.violationMoneyType!}">

                        <input id="violationMoneyTypeStr" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               data-options="tipPosition:'bottom'" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>问题线索来源：</span>
                        </label>
                        <input type="hidden" id="source" name="source" value="${record.source!''}"/>
                        <input type="text" id="sourceStr" class="inp1 comboxWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>问题上报部门：</span>
                        </label>
                        <input id="rptUnitName" name="rptUnitName" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               value="${record.rptUnitName!''}"
                               data-options="required:true,validType:['maxLength[500]','characterCheck'],tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>违规违纪违法类别：</span>
                        </label>
                        <input type="hidden" id="violationType" name="violationType" value="${record.violationType!''}" />
                        <input type="text" id="violationTypeStr" class="inp1 comboxWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>上报人：</span>
                        </label>
                        <input id="rptPersonName" name="rptPersonName" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               value="${record.rptPersonName!''}"
                               data-options="required:true,validType:['maxLength[300]','characterCheck'],tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>联系方式：</span>
                        </label>
                        <input id="rptPersonTel" name="rptPersonTel" type="text"
                               class="inp1 comboxWidth easyui-validatebox"
                               value="${record.rptPersonTel!''}"
                               data-options="tipPosition:'bottom',validType:'mobileorphone'" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>处置类型：</span>
                        </label>
                        <input type="hidden" id="procType" name="procType" value="${record.procType!''}" />
                        <input type="text" id="procTypeStr" class="inp1 comboxWidth easyui-validatebox" data-options="required: true, tipPosition:'bottom'"/>
                    </td>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>处置时限：</span>
                        </label>
                        <input type="text" id="procDeadlineStr" name="procDeadlineStr"  style="cursor: pointer;" value="${record.procDeadlineStr!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,isShowClear:false,isShowToday:false,maxDate:'${record.currentDate}'})" class="inp1 Wdate comboxWidth easyuiRequired" data-options="tipPosition:'bottom'" />
                    </td>
                </tr>
                <tr>
                    <td colspan="3" class="LeftTd" >
                        <label class="LabName">
                            <span>简要案情：</span>
                        </label>
                        <textarea id="caseBrief" name="caseBrief" value="${caseBrief!''}" class="easyui-validatebox" style="height: 160px; width: 88%;" data-options="required:true,validType:['maxLength[4000]','characterCheck'],tipPosition:'bottom'">${record.caseBrief!''}</textarea>
                    </td>
                </tr>
            </table>
            <table id="handleResultTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="hide">
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>是否问责：</span>
                        </label>
                        <span class="Check_Radio"><input class="cursorPoint" type="radio" id="blameFlag_1" name="blameFlag" value="1" /><label for="blameFlag_1" class="cursorPoint" >是&nbsp;</label></span>
                        <span class="Check_Radio"><input class="cursorPoint" type="radio" id="blameFlag_0" name="blameFlag" value="0"/><label for="blameFlag_0" class="cursorPoint" >否&nbsp;</label></span>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>问题处置状态：</span>
                        </label>
                        <input type="hidden" id="procStatus" name="procStatus" value="" />
                        <input type="text" id="procStatusName" class="inp1 comboxWidth easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName">
                            <span>四种形态：</span>
                        </label>
                        <input id="shape" name="shape" type="hidden" value="" />
                        <input id="shapeName" type="text" class="inp1 comboxWidth easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom'" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>追缴资金：</span>
                        </label>
                        <input type="text" id="amountOfRecovery" name="amountOfRecovery" class="inp1 comboxWidth easyui-validatebox" data-options="validType:'floatNumStr[12,2]',tipPosition:'bottom'" value="<#if record.amountOfRecovery??>${record.amountOfRecovery?c}</#if>" />
                        <label class="LabName measureUnit" style="margin-left: -50px;">(万元)</label>
                    </td>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span></span>
                        </label>
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd" colspan="3">
                        <label class="LabName">
                            <span>是否移送司法：</span>
                        </label>
                        <span class="Check_Radio"><input class="cursorPoint" type="radio" id="transferJusticeFlag_1" name="transferJusticeFlag" value="1" /><label for="transferJusticeFlag_1" class="cursorPoint" >是&nbsp;</label></span>
                        <span class="Check_Radio"><input class="cursorPoint" type="radio" id="transferJusticeFlag_0" name="transferJusticeFlag" value="0"/><label for="transferJusticeFlag_0" class="cursorPoint" >否&nbsp;</label></span>
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd">
                        <label class="LabName"><span>党纪处分：</span></label>
                        <input id="partyFlag" name="partyFlag" type="hidden" value="" />
                        <input id="partyFlagName" type="text" class="inp1 comboxWidth InpDisable easyui-validatebox" data-options="tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName"><span>党纪处分时间：</span></label>
                        <input type="text" id="partyPunishmentDateStr" name="partyPunishmentDateStr" class="inp1 Wdate easyui-validatebox comboxWidth" style="cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName"><span>党纪处分决定机关：</span></label>
                        <input type="text" id="partyPunishmentOrgan" name="partyPunishmentOrgan" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" value="" />
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd">
                        <label class="LabName"><span>政纪处分：</span></label>
                        <input id="disciplineFlag" name="disciplineFlag" type="hidden" value="" />
                        <input id="disciplineFlagName" type="text" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName"><span>政纪处分时间：</span></label>
                        <input type="text" id="disciplinePunishmentDateStr" name="disciplinePunishmentDateStr" class="inp1 Wdate easyui-validatebox comboxWidth" style="cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName"><span>政纪处分决定机关：</span></label>
                        <input type="text" id="disciplinePunishmentOrgan" name="disciplinePunishmentOrgan" class="inp1 easyui-validatebox comboxWidth" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" value="" />
                    </td>
                </tr>
                <tr class="violationPerson hide">
                    <td class="LeftTd">
                        <label class="LabName"><span>组织处理：</span></label>
                        <input id="orgProcType" name="orgProcType" type="hidden" value="" />
                        <input id="orgProcTypeName" type="text" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd">
                        <label class="LabName"><span>组织处理时间：</span></label>
                        <input type="text" id="orgProcDateStr" name="orgProcDateStr" class="inp1 Wdate easyui-validatebox comboxWidth" style="cursor:pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName wideLabelName"><span>组织处理决定机关：</span></label>
                        <input type="text" id="orgProcOrgan" name="orgProcOrgan" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" value="" />
                    </td>
                </tr>
                <tr class="violationUnit hide">
                    <td class="LeftTd">
                        <label class="LabName"><span>纪律处分：</span></label>
                        <input id="disciplinaryPunishment" name="disciplinaryPunishment" type="hidden" value="" />
                        <input id="disciplinaryPunishmentName" type="text" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom'" />
                    </td>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName"><span>问责处理：</span></label>
                        <input id="accountabilityDisposition" name="accountabilityDisposition" type="hidden" value="" />
                        <input id="accountabilityDispositionName" type="text" class="inp1 comboxWidth easyui-validatebox" data-options="tipPosition:'bottom'" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="3">
                        <label class="LabName"><span>处置结果简述：</span></label>
                        <textarea rows="3" style="height:160px; width: 88%;" id="procResult" name="procResult" class="area1 easyui-validatebox easyuiRequired" data-options="tipPosition:'bottom',validType:['maxLength[4000]','characterCheck']">${record.procResult!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" class="LeftTd">
                        <label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox easyuiRequired fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="2"><label class="LabName">
                        <span>问题附件：</span></label>
                        <div id="fileupload" class="ImgUpLoad"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="###" onclick="showAdvice(false, false);" class="BigNorToolBtn SaveBtn">保存</a>
            <a href="###" onclick="showAdvice(true, false);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
            <a href="###" onclick="showAdvice(true, true);" class="BigNorToolBtn BigJieAnBtn">结案</a>
            <a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
        </div>
    </div>
</form>
</body>


<script type="text/javascript">
    $(function() {
        var sourceArray = ('${record.source}').split(",");

        //问题线索来源,支持多选
        AnoleApi.initListComboBox("sourceStr", "source", "B590000", null, sourceArray, {RenderType:'01'});

        //违纪违规资金类别
        var moneyTypeArray = ('${record.violationMoneyType}').split(",");
        AnoleApi.initListComboBox("violationMoneyTypeStr", "violationMoneyType", "B590002", null, moneyTypeArray, {RenderType:'01'});

        //单位级别，违纪人员级别
        AnoleApi.initListComboBox("objProfessionTypePersonStr", "objProfessionTypePerson", "B590007", null,["${record.objProfessionTypePerson!}"]);
        AnoleApi.initListComboBox("objProfessionTypeUnitStr", "objProfessionTypeUnit", "B590008", null,["${record.objProfessionTypeUnit!}"]);

        //违规违纪违法类别
        var violationTypeArray = ('${record.violationType}').split(",");
        AnoleApi.initTreeComboBox("violationTypeStr", "violationType", "B590003", null,violationTypeArray,{
            RenderType:'11',
            FilterData:function(data) {
                if (data && data.length > 0) {
                    for ( var i = 0, len = data.length; i < len; i++) {
                        if (data[i].value && data[i].value.length == 2) {
                            data[i]["nocheck"] = true;
                        }
                    }
                }
                return data;
            }
        });

        //对象类别
        AnoleApi.initListComboBox("violationObjTypeStr", "violationObjType", "B590004", function(id,items){
            if('1'==id){
                //显示单位填写选项
                $("#content-d .violationPerson").hide();
                $("#content-d .violationUnit").show();
            }else if('2'==id){
                //显示个人填写选项
                $("#content-d .violationUnit").hide();
                $("#content-d .violationPerson").show();
            }
        },["${record.violationObjType!}"]);

        //问题处置状态
        AnoleApi.initListComboBox("procStatusName", "procStatus", "B590009", null,["${record.procStatus!}"]);

        //处置类型
        AnoleApi.initListComboBox("procTypeStr", "procType", "B590010", null,["${record.procType!}"]);

        //党纪处分
        AnoleApi.initTreeComboBox("partyFlagName", "partyFlag", "B590012001", null, ["${record.partyDisciplineFlag!}"]);

        //政纪处分
        AnoleApi.initTreeComboBox("disciplineFlagName", "disciplineFlag", "B590012002", null, ["${record.disciplineFlag!}"]);

        //组织处理
        AnoleApi.initTreeComboBox("orgProcTypeName", "orgProcType", "B590013", null,["${record.orgProcType!}"]);

        //四种形态
        AnoleApi.initListComboBox("shapeName", "shape", "B590014", null,["${record.shape!}"]);
        //对象性别
        AnoleApi.initListComboBox("objSexStr", "objSex", "B590005", null,["${record.objSex!'1'}"]);
        //政治面貌
        AnoleApi.initListComboBox("objPoliticsStr", "objPolitics", "B590006", null,["${record.objPolitics!}"]);
        //加载网格
        AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
            if (items && items.length > 0) {
                $("#regionCode").val(items[0].orgCode);
            }
        });

        //纪律处分
        AnoleApi.initTreeComboBox("disciplinaryPunishmentName", "disciplinaryPunishment", "B590015", null, ["${record.disciplinaryPunishment!}"]);
        //问责处理
        AnoleApi.initTreeComboBox("accountabilityDispositionName", "accountabilityDisposition", "B590016", null, ["${record.accountabilityDisposition!}"]);

        var radioList = [{'name':'问题采集', 'value':'1'},{'name':'问题处置', 'value':'2'}];
        //加载附件控件
        fileUpload({
            positionId:'fileupload', //附件列表DIV 的id值
            type:'eidt', //add edit detail 三种操作类型
            initType:'jsonp', //ajax,hidden 编辑表单时获取已上传附件列表
            imgDomain:'${IMG_URL!}',//图片域名 type为add或者edit时，生效
            context_path:'${SQ_FILE_URL}',
            ajaxData: {'attachmentType':'${attachmentType!}','eventSeq':'1,2'<#if record.probId??>,'bizId': '${record.probId?c}'</#if>},
            appCode:'accountEnforce',
            file_types:'*.jpg;*.gif;*.png;*.jpeg;*.amr;*.mp3;*.mp4;*.doc;*.docx;*.xls;',
            radio_list: radioList,
            showPattern: 'all'
        });

        $("#advice").width($(window).width() * 0.88);
        $("#blameFlag_${record.blameFlag!'0'}").attr("checked", true);
        $("#transferJusticeFlag_${record.transferJusticeFlag!'0'}").attr("checked", true);

        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        enableScrollBar('content-d',options);
    });

    function showAdvice(isStart, isClose) {
        if(isClose && isClose == true) {
            $("#handleResultTable .easyuiRequired").each(function() {
                $(this).validatebox({
                    required: true
                });
            });

            $("#handleResultTable").show();
        } else {
            $("#handleResultTable").hide();

            $("#handleResultTable .easyuiRequired").each(function() {
                $(this).validatebox({
                    required: false
                });
            });

            $('#advice').val("");
        }

        save(isStart, isClose);
    }

    //保存
    function save(isStart, isClose) {
        var isValid = $('#submitForm').form('validate');

        if(isValid) {
            var amountInvolved = $("#amountInvolved").val(),
                    violationMoney = $("#violationMoney").val();

            if(isBlankString(amountInvolved)) {
                amountInvolved = 0;
            }

            if(isBlankString(violationMoney)) {
                violationMoney = 0;
            }

            amountInvolved = parseFloat(amountInvolved);
            violationMoney = parseFloat(violationMoney);

            if(amountInvolved < violationMoney) {
                isValid = false;
                $.messager.alert('警告', '违纪违规金额不得超过涉案金额！', 'warning');
            }
        }

        if(isValid) {
            var attachments = "",
                    attachmentInpt = $('#submitForm input[name="attachmentId"]');

            $("#isStart").val(isStart);
            $("#isClose").val(isClose);
            $("#isSaveHandleResult").val(isClose);

            if(attachmentInpt && attachmentInpt.length>0) {
                attachmentInpt.each(function() {
                    attachments += "," + $(this).val();
                });
                attachments = attachments.substr(1);
            }

            $("#attachmentIds").val(attachments);

            modleopen(); //打开遮罩层
            $.ajax({
                type : 'POST',
                url : '${rc.getContextPath()}/zhsq/accountabilityEnforcement/saveProb.json',
                data : $('#submitForm').serializeArray(),
                dataType : 'json',
                success : function(data) {
                    if (data.result == false) {
                        $.messager.alert('错误', '保存失败！', 'error');
                    } else {
                        var probId = $("#probId").val() || data.probId,
                                isCurrent = probId && probId > 0,
                                tipMsg = data.tipMsg || '保存成功！';

                        if(isStart && !isClose) {
                            parent.searchData();
                            parent.detail(probId, "2");
                            if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
                                parent.closeBeforeMaxJqueryWindow();
                            }
                        } else {
                            if(isClose) {
                                tipMsg = "结案成功！";
                            }

                            parent.reloadDataForSubPage(tipMsg, isCurrent);
                        }
                    }
                },
                error : function(data) {
                    $.messager.alert('错误', '连接超时！', 'error');
                },
                complete : function() {
                    modleclose(); //关闭遮罩层
                }
            });
        }
    }

    //取消
    function cancel() {
        parent.closeMaxJqueryWindow();
    }

</script>

</html>
