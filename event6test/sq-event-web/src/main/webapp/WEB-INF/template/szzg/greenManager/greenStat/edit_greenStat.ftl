<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>编辑绿化指标</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
</head>
<style>
    .inp2 {
        width: 140px;
    }
</style>
<body>
<div>
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm NorForm2">
            <form id="tableForm" name="tableForm"
                  action="${rc.getContextPath()}/zhsq/szzg/greenindicators/saveOrUpdate.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <input name="seqid" id="seqid" value="<#if statsGreen.seqid??>${statsGreen.seqid}</#if>"
                           type="hidden">
                    <input type="hidden" id="id" name="id" value="${id}"/>
                    <input type="hidden" id="markerOperation" name="markerOperation" value="${markerOperation}"/>
                    <input type="hidden" id="module" name="module" value="GREEN_MARK1"/>


                    <tr>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属年份：</span></label>
                            <input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text"
                                   id="syear" value="<#if statsGreen.syear??>${statsGreen.syear}</#if>"
                                   data-options="required:true" name="syear" readonly="true"/>

                        </td>


                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属区域：</span></label>
                        <#if statsGreen.orgName??>${statsGreen.orgName}</#if>

                        </td>


                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>绿化覆盖面积：</span></label>
                            <input name="gCoverarea" id="gCoverarea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.gCoverarea??>${statsGreen.gCoverarea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(公顷)
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>绿化覆盖率：</span></label>
                            <input name="gCoverrate" id="gCoverrate" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.gCoverrate??>${statsGreen.gCoverrate}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:100,min:0"/>&nbsp;(%)
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>建成区面积：</span></label>
                            <input name="builtupArea" id="builtupArea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.builtupArea??>${statsGreen.builtupArea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(平方公里)
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>建成区人口：</span></label>
                            <input name="popu" id="popu" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.popu??>${statsGreen.popu}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:4,required:true,max:99999999,min:0"/>&nbsp;(万人)
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>公园绿地面积：</span></label>
                            <input name="gParkarea" id="gParkarea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.gParkarea??>${statsGreen.gParkarea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(公顷)
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>人均公园绿地面积：</span></label>
                            <input name="gPerparkarea" id="gPerparkarea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.gPerparkarea??>${statsGreen.gPerparkarea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(平方米)
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>森林面积：</span></label>
                            <input name="forestArea" id="forestArea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.forestArea??>${statsGreen.forestArea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(公顷)
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>森林覆盖率：</span></label>
                            <input name="forestCoverarea" id="forestCoverarea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.forestCoverarea??>${statsGreen.forestCoverarea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:100,min:0"/>&nbsp;(%)
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>区域面积：</span></label>
                            <input name="regionalArea" id="regionalArea" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.regionalArea??>${statsGreen.regionalArea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(平方公里)
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>城市绿化面积：</span></label>
                            <input name="gArea" id="gArea" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.gArea??>${statsGreen.gArea}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:99999999,min:0"/>&nbsp;(公顷)
                        </td>



                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd">
                            <label class="LabName"><span>地理标注：</span></label>
                        <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                        </td>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>城市绿化率：</span></label>
                            <input name="gRate" id="gRate" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   value="<#if statsGreen.seqid??>${statsGreen.seqid}</#if>"
                                   style="width: 140px;height:30px"
                                   data-options="tipPosition:'bottom',precision:2,required:true,max:100,min:0"/>&nbsp;(%)
                        </td>

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

<script type="text/javascript">


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
            ShowOptions: {EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });

        //加载数据字典：文化程度
        AnoleApi.initTreeComboBox("typeName", null, 'S001002', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('type').value = items[0].dictCode;
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

    function showMap() {
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
        var width = 480;
        var height = 360;
        var gridId = ""
        var markerOperation = $('#markerOperation').val();
        var id = $('#id').val();

        var mapType = 'GREEN_MARK1';
        var isEdit = true;
        showMapWindow(gridId, callBackUrl, width, height, isEdit, mapType);
    }

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