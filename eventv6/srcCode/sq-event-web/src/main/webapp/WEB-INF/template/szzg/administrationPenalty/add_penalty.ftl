<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <title>新增行政处罚信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
    <style>
        .PhotoEdit{width:96%; height:24px; line-height:24px; text-align:center; position:absolute;
            z-index:999;
            bottom:0px;
            left:7px;
            background:#000; filter:alpha(opacity=60); opacity:0.6; color:#fff; display:none; cursor:pointer;}
    </style>
</head>
<body>
<form id="submitForm" name="submitForm" action="${rc.getContextPath()}/zhsq/szzg/zgPenaltyController/save.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" id="penaltyId" name="penaltyId" value="${(penalty.penaltyId)!}" />
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
        <div class="NorForm NorForm2">
            <table>
                <tr>
                    <td class="LeftTd" style="width: 270px;">
                        <label class="LabName">
                            <span>所在网格：</span>
                        </label>
                        <input type="hidden" id="gridCode" name="gridCode" value="${penalty.gridCode!''}" />
                        <input type="text" id="gridName" name="gridName" value="${penalty.gridName!''}" class="inp1 inp2 easyui-validatebox" style="width: 200px;" />
                    </td>
                    <td class="LeftTd" style="width: 300px;">
                        <label class="LabName">
                            <span>工商注册号：</span>
                        </label>
                        <input type="text" id="registrationId" name="registrationId" class="inp1 inp2 easyui-validatebox" value="${penalty.registrationId!''}" style="width: 230px;" data-options="validType:['maxLength[64]','characterCheck']"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 270px;">
                        <label class="LabName">
                            <span>企业名称：</span>
                        </label>
                        <input type="text" id="enterpriseName" name="enterpriseName" class="inp1 inp2 easyui-validatebox" value="${penalty.enterpriseName!''}" style="width: 200px;" data-options="required:true,validType:['maxLength[128]','characterCheck']"/>
                    </td>
                    <td class="LeftTd" style="width: 300px;">
                        <label class="LabName">
                            <span>决定文书号：</span>
                        </label>
                        <input type="text" id="decisionInstrument" name="decisionInstrument" class="inp1 inp2 easyui-validatebox" value="${penalty.decisionInstrument!''}" style="width: 230px;" data-options="validType:['maxLength[64]','characterCheck']"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 270px;">
                        <label class="LabName">
                            <span>处罚日期：</span>
                        </label>
                        <input type="text" class="Wdate inp1"  id="penaltyDate" name="penaltyDate" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-M-d', readOnly:true, alwaysUseStartDate:true})"
                               value="<#if penalty.penaltyDate??>${penalty.penaltyDate?string('yyyy-M-d')}</#if>" style="width: 200px;"/>
                    </td>
                    <td class="LeftTd" style="width: 300px;">
                        <label class="LabName">
                            <span>处罚依据：</span>
                        </label>
                        <input type="text" id="penaltyBases" name="penaltyBases" class="inp1 inp2 easyui-validatebox" value="${penalty.penaltyBases}" style="width: 230px;"
                        data-options="validType:['maxLength[256]']"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" colspan="2">
                        <label class="LabName">
                            <span>处罚结论：</span>
                        </label>
                        <textarea id="penaltyConclusion" class="inp1 inp2 easyui-validatebox" name="penaltyConclusion" style="width: 535px;" rows="3" wrap="soft" data-options="validType:['maxLength[256]','characterCheck']">${penalty.penaltyConclusion!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd" style="width: 700px">
                        <label class="LabName">
                            <span>处罚事由：</span>
                        </label>
                        <textarea id="penaltyCause" class="easyui-validatebox" name="penaltyCause" style="width:535px;" rows="6" wrap="soft" data-options="validType:['maxLength[3000]','characterCheck']">${penalty.penaltyCause!''}</textarea>
                    </td>
                </tr>
                <tr>
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
            modleopen(); //打开遮罩层
            $.ajax({
                type : 'POST',
                url : '${rc.getContextPath()}/zhsq/szzg/zgPenaltyController/save.json',
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

</script>
</body>
</html>
