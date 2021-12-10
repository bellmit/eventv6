<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑医院信息</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/scripts/updown/swfupload/handlers.js"></script>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
</head>
<style>
    .inp2{width: 140px;}
</style>
<body>
<div>
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm NorForm2">
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/hospital/saveOrUpdate.jhtml"  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">



                    <input type="hidden" id="id" name="id" value="<#if id??>${id}</#if>"/>

                    <input type="hidden" id="seqId" name="seqId" value="<#if hospitalBO.seqId??>${hospitalBO.seqId}</#if>"/>
                    <input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
                    <input type="hidden" id="module" name="module" value="HOSPITAL_MARK"/>
                    <tr>

                        <td><label class="LabName"><span>医院类型：</span></label>
                            <input id="type" name="type" type="hidden" value="<#if hospitalBO.type??>${hospitalBO.type}</#if>"/>
                            <input id="typeName" name="typeName" value="<#if hospitalBO.typeName??>${hospitalBO.typeName}</#if>" data-options="required:true" type="text" class="inp1 InpDisable easyui-validatebox" style="width:160px;"
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属区域：</span></label>
                            <input type="hidden" id="orgCode" name="orgCode" value="<#if hospitalBO.orgCode??>${hospitalBO.orgCode}</#if>">
                            <input  class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="orgName" value="<#if hospitalBO.orgName??>${hospitalBO.orgName}</#if>"
                                   data-options="required:true" style="width: 160px" name="orgName"/>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>医院名称：</span></label>
                            <input name="hospitalName" id="hospitalName" maxLength="100" type="text" class="inp1 easyui-validatebox" value="<#if hospitalBO.hospitalName??>${hospitalBO.hospitalName}</#if>" style="width: 500px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"/></td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>医院地址：</span></label>
                            <input name="address" id="address" maxLength="100" type="text" class="inp1 easyui-validatebox" value="<#if hospitalBO.address??>${hospitalBO.address}</#if>" style="width: 500px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"/></td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd">
                            <label class="LabName"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>法定代表人：</span></label>
                            <input name="legalRepresentative" id="legalRepresentative" type="text" style="height: 30px;width: 160px;"  value="<#if hospitalBO.legalRepresentative??>${hospitalBO.legalRepresentative}</#if>" class="inp1 inp2 easyui-validatebox" maxlength="51" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']"/>
                        </td>
                        <td><label class="LabName"><span>联系电话：</span></label>
                            <input name="tel" id="tel" style="height: 30px;width: 160px;" type="text" class="inp1 inp2 easyui-validatebox" validType='mobileorphone' value="<#if hospitalBO.tel??>${hospitalBO.tel}</#if>" maxlength="51"  data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>邮政编码：</span></label>
                            <input name="postcode" id="postcode" type="text" style="height: 30px;width: 160px;" class="inp1 inp2 easyui-validatebox"  validType='zipCode' value="<#if hospitalBO.postcode??>${hospitalBO.postcode}</#if>" data-options="tipPosition:'bottom',max:999999,min:0"/>
                        </td>
                        <td><label class="LabName"><span>官方网址：</span></label>
                            <input name="url" id="url" style="height: 30px;width: 160px;" type="text" class="inp1 inp2 easyui-validatebox" value="<#if hospitalBO.url??>${hospitalBO.url}</#if>"  maxlength="51" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>是否城镇职工医保定点单位：：</span></label>
                            <div class="Check_Radio"><input type="checkBox" name="isUebmiFixedUnit" id="isUebmiFixedUnit"  <#if (hospitalBO.isUebmiFixedUnit?? && hospitalBO.isUebmiFixedUnit=='1')>checked</#if>  onclick="changeUebValue();" /><label for="isUebmiFixedUnit" style="cursor:pointer;">是</label></div>
                        </td>
                        <td class="LeftTd"><label class="LabName"><span>是否新农合定点单位：：</span></label>
                            <div class="Check_Radio"><input type="checkBox" name="isNcmsFixedUnit" id="isNcmsFixedUnit"  <#if (hospitalBO.isNcmsFixedUnit?? && hospitalBO.isNcmsFixedUnit=='1')>checked</#if>  onclick="changeNcmValue();" /><label for="isUebmiFixedUnit" style="cursor:pointer;">是</label></div>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>占地面积：</span></label>
                            <input name="area" id="area" type="text" style="height: 30px;width: 160px;" class="inp1 inp2 easyui-numberbox"  value="<#if hospitalBO.area??>${hospitalBO.area}</#if>" data-options="tipPosition:'bottom',precision:2,max:99999999,min:0"/>&nbsp;(公顷)
                        </td>
                        <td><label class="LabName"><span>建筑面积：</span></label>
                            <input name="constructionArea" id="constructionArea" style="height: 30px;width: 160px;" type="text" class="inp1 inp2 easyui-numberbox" value="<#if hospitalBO.constructionArea??>${hospitalBO.constructionArea}</#if>"  data-options="tipPosition:'bottom',precision:2,max:99999999,min:0"/>&nbsp;(平方米)
                        </td>
                    </tr>


                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>住院床位数：</span></label>
                            <input name="hospitalBeds" id="hospitalBeds" type="text" style="height: 30px;width: 160px;" class="inp1 inp2 easyui-numberbox" value="<#if hospitalBO.hospitalBeds??>${hospitalBO.hospitalBeds}</#if>" data-options="tipPosition:'bottom',max:99999999,min:0"/>&nbsp;(床)
                        </td>
                        <td><label class="LabName"><span>员工总数：</span></label>
                            <input name="employeeNum" id="employeeNum" style="height: 30px;width: 160px;" type="text" class="inp1 inp2 easyui-numberbox"  value="<#if hospitalBO.employeeNum??>${hospitalBO.employeeNum}</#if>" data-options="tipPosition:'bottom',max:99999999,min:0"/>&nbsp;(人)
                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>专业技术人员数量：</span></label>
                            <input name="specialistNum" id="specialistNum" type="text" style="height: 30px;width: 160px;" class="inp1 inp2 easyui-numberbox" value="<#if hospitalBO.specialistNum??>${hospitalBO.specialistNum}</#if>" data-options="tipPosition:'bottom',max:99999999,min:0"/>&nbsp;(人)
                        </td>
                        <td><label class="LabName"><span>血库库存量：</span></label>
                            <input name="bloodBankStock" id="bloodBankStock" style="height: 30px;width: 160px;" type="text" class="inp1 inp2 easyui-numberbox" value="<#if hospitalBO.bloodBankStock??>${hospitalBO.bloodBankStock}</#if>" data-options="tipPosition:'bottom',max:99999999,min:0"/>&nbsp;(单位)
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>医院简介：</span></label>
                            <textarea name="hospitalSummary" id="hospitalSummary" cols="45" rows="30" style="width: 500px;height: 80px;" class="area1 easyui-validatebox"  maxLength="201" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']"><#if hospitalBO.hospitalSummary??>${hospitalBO.hospitalSummary}</#if></textarea></td>
                    </tr>


                </table>
            </form>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="#" class="BigNorToolBtn JieAnBtn" onclick="javascript:checkSubmit();">保存</a>
            <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">取消</a>
        </div>
    </div>
</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script type="text/javascript">

    $.extend($.fn.validatebox.defaults.rules, {

        zipCode : { validator : function(value) {
            var reg = /^[0-9]{6}$/;
            return reg.test(value);
        },

            message : "邮编必须为6位数字."

        },

    });

    function changeUebValue(){
        var std = $("#isUebmiFixedUnit").is(":checked");
        if(std){
            $("#isUebmiFixedUnit").val(1);
        }else{
            $("#isUebmiFixedUnit").removeAttr("checked");
            $("#isUebmiFixedUnit").val(0);
        }
    }

    function changeNcmValue(){
        var std = $("#isNcmsFixedUnit").is(":checked");
        if(std){
            $("#isNcmsFixedUnit").val(1);
        }else{
            $("#isNcmsFixedUnit").removeAttr("checked");
            $("#isNcmsFixedUnit").val(0);
        }
    }

    var _winHeight = 0;
    var _winWidth = 0;
    /*
      地图标注改造 2015-06-29，参考geo项目行政区划地图标注
      markerOperation:地图标注操作类型
                       0表示添加
                       1表示编辑
                       2表示查看
      isEdit:是否是编辑状态
  */

    function showMap(){
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
        var width = 480;
        var height = 360;
        var gridId = "";
        var markerOperation = $('#markerOperation').val();
        var id = $('#id').val();
        var mapType = 'HOSPITAL_MARK';
        var isEdit = true;

        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }

    $(window).resize(function(){
        var winHeight = $(window).height();
        var winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            location.reload();
        }
    });



    $(function () {

        //加载区域
        AnoleApi.initGridZtreeComboBox("orgName", null, function (gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value = items[0].orgCode;
            }
        }, {
            rootName: "行政区划",
            ChooseType: '1',
            isShowPoorIcon: "0",
            ShowOptions: { EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });
        //加载数据字典：医院类型
        AnoleApi.initTreeComboBox("typeName", null, 'S008001', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('type').value = items[0].dictCode;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});


        $("#content-d").mCustomScrollbar({theme:"minimal-dark"});

    });



    //重复验证，并提交
    function checkSubmit() {
        var isValid = $("#tableForm").form('validate');
        if($('#x').val()==null||$('#x').val()=="")
        {
            $.messager.alert('错误', '请标注地理位置！', 'error');
            isValid=false
        }
        if (isValid) {
            $("#tableForm").ajaxSubmit(function (data) {
                if (data.result) {
                    parent.reloadDataForSubPage(data.msg);
                } else {
                    modleclose();

                    if (data.msg) {
                        $.messager.alert('错误', data.msg, 'error');
                    } else {
                        $.messager.alert('错误', '操作失败！', 'error');
                    }
                }
            });
        }
    }

    //取消
    function cancel() {
        parent.closeMaxJqueryWindow()

    }

    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
    }




</script>
</html>