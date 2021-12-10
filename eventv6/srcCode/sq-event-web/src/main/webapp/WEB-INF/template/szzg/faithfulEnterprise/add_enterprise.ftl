<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <title>新增守重企业信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<!--用于地图-->
<input type="hidden" id="id" name="id" value="${enterprise.enterpriseId!''}" />
<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>"/>
<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId}</#if>"/>
<form id="submitForm" name="submitForm" action="${rc.getContextPath()}/gmis/faithfulEnterprise/save.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" id="enterpriseId" name="enterpriseId" value="${(enterprise.enterpriseId)!}" />
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
        <div class="NorForm NorForm2">
            <table>
                <tr>
                    <td class="LeftTd" style="width: 310px;">
                        <label class="LabName">
                            <span>所在网格：</span>
                        </label>
                        <input type="hidden" id="gridCode" name="gridCode" value="${enterprise.gridCode!''}" />
                        <input type="text" id="gridName" name="gridName" value="${enterprise.gridName!''}" class="inp1 inp2 easyui-validatebox" style="width: 180px;" />
                    </td>
                    <td class="LeftTd" style="width: 310px;">
                        <label class="LabName">
                            <span>企业名称：</span>
                        </label>
                        <input type="text" id="enterpriseName" name="enterpriseName" class="inp1 inp2 easyui-validatebox" value="${enterprise.enterpriseName!''}" style="width: 180px;" data-options="required:true,validType:['maxLength[128]','characterCheck']"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 310px;">
                        <label class="LabName">
                            <span>工商注册号：</span>
                        </label>
                        <input type="text" id="registrationId" name="registrationId" class="inp1 inp2 easyui-validatebox" value="${enterprise.registrationId!''}" style="width: 180px;" data-options="validType:['maxLength[64]','characterCheck']"/>
                    </td>
                    <td class="LeftTd" style="width: 200px;">
                        <label class="LabName">
                            <span>评定时间：</span>
                        </label>
                        <input type="text" class="Wdate inp1"  id="evaluationTimeStr" name="evaluationTimeStr" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})"
                               value="<#if enterprise.evaluationTime??>${enterprise.evaluationTime?string('yyyy-MM-dd')}</#if>" style="width: 180px;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd" >
                        <label class="LabName">
                            <span>企业地址：</span>
                        </label>
                        <input id="enterpriseAddress" name="enterpriseAddress" type="text" style="width: 300px;"
                               class="inp1 easyui-validatebox"
                               value="${enterprise.enterpriseAddress}"
                               data-options="required:true,validType:['maxLength[100]','characterCheck'],tipPosition:'bottom'"/>
                        <input type="hidden" id="latitude" name="latitude" value="${enterprise.latitude!''}" /> <#--纬度-->
                        <input type="hidden" id="longitude" name="longitude" value="${enterprise.longitude!''}"/><#--经度-->
                    <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="width: 600px;">
                        <label class="LabName">
                            <span>评定级别：</span>
                        </label>
                        <textarea id="evaluationLevel" name="evaluationLevel"  class="inp1 easyui-validatebox" style="width: 450px;" rows="3" wrap="soft" data-options="validType:['maxLength[64]','characterCheck']">${enterprise.evaluationLevel!''}</textarea>
                    </td>
                </tr>

            </table>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
            <a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
        </div>
    </div>
</form>
<script type="text/javascript">

    $("#content-d").mCustomScrollbar({theme: "minimal-dark"});

    $(function(){
        $("#localImag").hover(function(){
            $(this).find(".PhotoEdit").slideDown(200);
        }, function(){
            $(this).find(".PhotoEdit").slideUp(200);
        });
    });

    $(function() {

        //加载网格
        AnoleApi.initGridZtreeComboBox("gridName",  "gridCode", function(gridId, items) {
            if (items && items.length > 0) {
                $("#gridCode").val(items[0].orgCode);
            }
        });
    });





    //保存
    function save() {
        var isValid = $('#submitForm').form('validate');
        if (isValid) {
            document.getElementById('longitude').value = document.getElementById('x').value;
            document.getElementById('latitude').value = document.getElementById('y').value;
            modleopen(); //打开遮罩层
            $.ajax({
                type : 'POST',
                url : '${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/save.json',
                data : $('#submitForm').serializeArray(),
                dataType : 'json',
                success : function(data) {
                    if (data.result == 'fail') {
                        $.messager.alert('错误', '保存失败！', 'error');
                    } else {
                        $.messager.alert('提示', '保存成功！', 'info', function() {
                            parent.closeMaxJqueryWindow();
                        });
                        parent.searchData();
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
    //地图标注
    function showMap(){
        var callBackUrl = "${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml";
        //var callBackUrl = "http://gd.fjsq.org:8301/event/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml";
        var width = 480;
        var height = 360;
        var gridId = $("#gridId").val();
        var markerOperation = $('#markerOperation').val();
        var id = $('#id').val();
        var mapType = $('#module').val();
        var isEdit = true;
        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }
</script>

</body>
</html>
