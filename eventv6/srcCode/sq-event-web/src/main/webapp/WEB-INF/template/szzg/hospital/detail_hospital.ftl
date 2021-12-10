<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>医院信息</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
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
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.typeName??>${hospitalBO.typeName}</#if></span>

                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属区域：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.orgName??>${hospitalBO.orgName}</#if></span>


                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>医院名称：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.hospitalName??>${hospitalBO.hospitalName}</#if></span>

                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>医院地址：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if hospitalBO.address??>${hospitalBO.address}</#if></span>

                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd">
                            <label class="LabName"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>法定代表人：</span></label>
                            <span class="Check_Radio FontDarkBlue">    <#if hospitalBO.legalRepresentative??>${hospitalBO.legalRepresentative}</#if></span>

                        </td>
                        <td><label class="LabName"><span>联系电话：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if hospitalBO.tel??>${hospitalBO.tel}</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>邮政编码：</span></label>
                            <span class="Check_Radio FontDarkBlue">        <#if hospitalBO.postcode??>${hospitalBO.postcode}</#if></span>

                        </td>
                        <td><label class="LabName"><span>官方网址：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.url??>${hospitalBO.url}</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>是否城镇职工医保定点单位：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if (hospitalBO.isUebmiFixedUnit?? && hospitalBO.isUebmiFixedUnit=='1')>是</#if> <#if (hospitalBO.isUebmiFixedUnit?? && hospitalBO.isUebmiFixedUnit!='1')>否</#if></span>

                        </td>
                        <td class="LeftTd"><label class="LabName"><span>是否新农合定点单位：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if (hospitalBO.isNcmsFixedUnit?? && hospitalBO.isNcmsFixedUnit=='1')>是</#if> <#if (hospitalBO.isNcmsFixedUnit?? && hospitalBO.isNcmsFixedUnit!='1')>否</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>占地面积：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.area??>${hospitalBO.area}&nbsp;(公顷)</#if></span>

                        </td>
                        <td><label class="LabName"><span>建筑面积：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if hospitalBO.constructionArea??>${hospitalBO.constructionArea}&nbsp;(平方米)</#if></span>

                        </td>
                    </tr>


                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>住院床位数：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if hospitalBO.hospitalBeds??>${hospitalBO.hospitalBeds}&nbsp;(床)</#if></span>

                        </td>
                        <td><label class="LabName"><span>员工总数：</span></label>

                            <span class="Check_Radio FontDarkBlue"> <#if hospitalBO.employeeNum??>${hospitalBO.employeeNum}&nbsp;(人)</#if></span>

                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>专业技术人员数量：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if hospitalBO.specialistNum??>${hospitalBO.specialistNum}&nbsp;(人)</#if></span>

                        </td>
                        <td><label class="LabName"><span>血库库存量：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if hospitalBO.bloodBankStock??>${hospitalBO.bloodBankStock}&nbsp;(单位)</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>医院简介：</span></label>
                            <p><span class="Check_Radio FontDarkBlue"
                                     style="width: 85%"> <#if  hospitalBO.hospitalSummary??>${hospitalBO.hospitalSummary}</#if></span></p>
                          </td>
                    </tr>


                </table>
            </form>
        </div>
    </div>
    <#if show=="1">
    <div class="BigTool">
        <div class="BtnList">
            <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">关闭</a>
        </div>
    </div>
    </#if>
</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">


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
        $("#content-d").mCustomScrollbar({theme:"minimal-dark"});
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