<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑城市绿化分布数据</title>
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
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/green/saveOrUpdate.jhtml"  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">

                    <input type="hidden" id="id" name="id" value="${id}" />
                    <input type="hidden" id="markerOperation" name="markerOperation" value="${markerOperation}"/>
                    <input type="hidden" id="module" name="module" value="GREEN_MARK"/>

                    <input name="seqid" type="hidden" id="seqid" value="<#if greenBeltBO.seqid??>${greenBeltBO.seqid}</#if>">
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属区域：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenBeltBO.orgName??>${greenBeltBO.orgName}</#if></span>


                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>绿地名称：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.gName??>${greenBeltBO.gName}</#if></span>

                        </td>
                    </tr>


                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>城市绿地面积：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.gArea??>${greenBeltBO.gArea}&nbsp;(公顷)</#if></span>

                        </td>
                        <td><label class="LabName"><span>城市绿化率：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenBeltBO.gRate??>${greenBeltBO.gRate}&nbsp;(%)</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>占地面积：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenBeltBO.gCoverarea??>${greenBeltBO.gCoverarea}&nbsp;(公顷)</#if></span>

                        </td>
                        <td><label class="LabName"><span>绿化覆盖率：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if greenBeltBO.gCoverrate??>${greenBeltBO.gCoverrate}&nbsp;(%)</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>主要种植品种：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.plant??>${greenBeltBO.plant}</#if></span>

                        </td>
                        <td><label class="LabName"><span>种植日期：</span></label>
                            <span class="Check_Radio FontDarkBlue">   <#if greenBeltBO.plantdate??>${greenBeltBO.plantdate}</#if></span>

                        </td>
                    </tr>

                    <tr>

                        <td><label class="LabName"><span>绿地类型：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.gTypeName??>${greenBeltBO.gTypeName}</#if></span>

                        </td>

                        <td class="LeftTd"><label class="LabName"><span>是否达标：</span></label>
                            <span class="Check_Radio FontDarkBlue">    <#if (greenBeltBO.std?? && greenBeltBO.std=='1')>是</#if> <#if (greenBeltBO.std?? && greenBeltBO.std!='1')>否</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>道路长度：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenBeltBO.extattr1??>${greenBeltBO.extattr1}&nbsp;(米)</#if></span>

                        </td>
                        <td><label class="LabName"><span>道路宽度：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.extattr2??>${greenBeltBO.extattr2}&nbsp;(米)</#if></span>

                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>地址：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenBeltBO.location??>${greenBeltBO.location}</#if></span>
                            </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd">
                            <label class="LabName"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>备注：</span></label>
                            <span class="Check_Radio FontDarkBlue" style="width: 85%"> <#if greenBeltBO.remark??>${greenBeltBO.remark}</#if></span>
                           </td>
                    </tr>


                </table>
            </form>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">关闭</a>
        </div>
    </div>
</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">


    $("#content-d").mCustomScrollbar({theme:"minimal-dark"});

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
        //加载数据字典：类型
        AnoleApi.initTreeComboBox("gTypeName", null, 'S002002', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('gType').value = items[0].dictCode;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});




    });
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
        var gridId = ""
        var markerOperation = $('#markerOperation').val();
        var id = $('#id').val();
        alert(id)
        var mapType = 'GREEN_MARK';
        var isEdit = true;
        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }
    //    function getParameterJson() {
    //        var parameterJson={
    //            "id":$("#id").val(),
    //            "name":$("#name").val()
    //        }
    //        return parameterJson;
    //    }
    $(window).resize(function(){
        var winHeight = $(window).height();
        var winWidth = $(window).width();

        if(winHeight != _winHeight || winWidth != _winWidth) {
            location.reload();
        }
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