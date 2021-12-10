<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>填写化解信息</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/detail.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/main-shce.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/form-add.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/ui/js/function.js"></script>
    <#include "/component/standard_common_files-1.1.ftl" />
    <link href="${GEO_URL}/js/components/geoAddressPlugin/css/address.css" rel="stylesheet" />
    <script type="text/javascript" src="${(GEO_URL)}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
    <style>
        .textbox.combo>input{width:90%!important;}
        .textbox.numberbox.spinner>input{width:90%!important;line-height:30px;}
        .textbox.numberbox>input{width:100%!important;line-height:30px;}
        .textbox-addon.textbox-addon-right>a{margin-top:5px!important;}
        .det-nav-wrap > li > a:hover, .det-nav-wrap > li > .active {background-color: #5294e8!important;}
        .fw-det-tog-top > h5 > i {background-color:#5294e8!important;}
        .btn-warp .btn-bon {background-color:#5294e8!important;;color: rgb(255, 255, 255)!important}
        .det-wrapper {left: 0px}
        .fw-from1 {min-width: 96px}
        .form-warp-sh{padding-top: 0px}
        textarea{
            resize:none;
            background: #fff;
            padding-left: 10px;
            border: 1px solid #ccc;
            padding-top: 6px;
        }
        .validatebox-invalid{
            border-color: #ccc;
        }
    </style>
</head>
<body class="xiu-body">
<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/disputeMediation/resolveSave.jhtml"  method="post">
    <input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
    <input type="hidden" id="mediationResId" name="mediationResId" value="<#if disputeMediation.mediationResId??>${disputeMediation.mediationResId}</#if>">
    <div class="container-detail flex">
        <div class="det-wrapper flex1">
            <div class="container_fluid">
                <div class="form-warp-sh "><!-- 外框 -->
                    <div class="fw-main">
                        <!--化解信息-->
                        <div class="fw-det-tog mt10">
                            <div class="fw-det-toggle">
                                <ul class="fw-xw-from clearfix">
                                    <li class="xw-com3">
                                        <span class="fw-from1">化解方式：</span>
                                        <input type="hidden" id="mediationType" name="mediationType" value="<#if disputeMediation.mediationType??>${disputeMediation.mediationType}</#if>">
                                        <input name="mediationTypeStr" id="mediationTypeStr" class="from flex1" type="text" placeholder="请选择">
                                    </li>
                                    <li class="xw-com3">
                                        <span class="fw-from1">最后调解日期：</span>
                                        <input id="mediationDateStr" name="mediationDateStr" type="text" class="from flex1 pr30" readonly="readonly" onclick="WdatePicker({el:'mediationDateStr',dateFmt:'yyyy-MM-dd'})" data-options="required:true,tipPosition:'bottom'"  value="<#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if>"/>
                                        <a href="javascript:void(0);" onclick="WdatePicker({el:'mediationDateStr',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'happenTimeStr\')}'})" class="time-switch flex flex-ac flex-jc">
                                            <img src="${rc.getContextPath()}/style/images/icon_time.png"/>
                                        </a>
                                    </li>
                                    <li class="xw-com3">
                                        <span class="fw-from1">化解责任人姓名：</span>
                                        <input name="mediator" id="mediator" class="from flex1 easyui-validatebox" type="text" value="<#if disputeMediation.mediator??>${disputeMediation.mediator}</#if>"  data-options="required:true,validType:['maxLength[15]','characterCheck']">
                                    </li>
                                    <li class="xw-com3">
                                        <span class="fw-from1">化解责任人电话：</span>
                                        <input name="mediationTel" id="mediationTel" class="from flex1 easyui-validatebox" type="text" value="<#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if>" maxlength="11" data-options="required:true,validType:['maxLength[11]','characterCheck']"/>
                                    </li>
                                     <li class="xw-com3">
                                        <span class="fw-from1">化解组织：</span>
                                        <input name="mediationOrgName" id="mediationOrgName" class="from flex1 easyui-validatebox" type="text" value="<#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>"  data-options="required:true,validType:['maxLength[50]','characterCheck']"/>
                                    </li>
                                    <li class="xw-com3">
                                        <span class="fw-from1">化解是否成功：</span>
                                        <input type="hidden" id="isSuccess" name="isSuccess"  value="<#if disputeMediation.isSuccess??>${disputeMediation.isSuccess}</#if>">
                                        <input name="isSuccessStr" id="isSuccessStr" class="from flex1" type="text" placeholder="请选择">
                                    </li>
                                    <li class="xw-com1">
                                        <span class="fw-from1">化解情况：</span>
                                        <textarea name="mediationResult" id="mediationResult" class="from flex1 easyui-validatebox pl10" style="height: 50px;" type="text" value="<#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if>" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1000]','characterCheck']" placeholder="请输入化解情况">${disputeMediation.mediationResult}</textarea>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="BtnList">
                    <a class="BigNorToolBtn SaveBtn" onclick="javascript:save();">保存</a>
                    <a class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">取消</a>
                </div>
            </div><!--container-fluid-->
        </div>
    </div><!--container-fluid-->
</form>
<#include "/component/FieldCfg.ftl" />
<#include "/component/ComboBox.ftl">
<#include "/zzgl/dispute/mediation_9x/map/maxJqueryEasyUIWin.ftl" />
<script src="${rc.getContextPath()}/dispute/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
<script>
    $(function () {
        layer.load(0);// 加载遮罩层

        AnoleApi.initTreeComboBox("mediationTypeStr", "mediationType", "B417", null<#if disputeMediation.mediationType??>, ["${disputeMediation.mediationType}"]</#if>);

        //化解是否成功
        AnoleApi.initListComboBox("isSuccessStr", "isSuccess", null, null, ["${disputeMediation.isSuccess!'0'}"], {
            RenderType : "00",
            DataSrc : [{"name":"是", "value":"1"},{"name":"否", "value":"0"}]
        });

        var $winH, $topH, $btnH;
        $(window).on('load resize', function () {
            $winH = $(window).height();
            $topH = $('.fw-toptitle').height();
            $btnH = $('.btn-warp').height();
            $('.fw-main').height($winH - $topH - $btnH - 76);
            $(".fw-main").niceScroll({
                cursorcolor:"rgba(0, 0, 0, 0.3)",
                cursoropacitymax:1,
                touchbehavior:false,
                cursorwidth:"4px",
                cursorborder:"0",
                cursorborderradius:"4px"
            });
        });

        //做电话正则:如果说输入的电话号码不正确则给出提示,正确则不显示提示(添加，修改)
        $.extend($.fn.validatebox.defaults.rules, {
            telNum:{ //既验证手机号，又验证座机号
                validator: function(value, param){
                    return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
                },
                message: '请输入正确的电话号码'
            }
        });
        layer.closeAll()
    });

    function save(){
        var isValid =  $("#tableForm").form('validate');
        if($("#mediationType").val() == ''){
            $.messager.alert('提示', '化解方式不能为空', 'info', function() {});
            return;
        }
        if($("#mediationDateStr").val() == ''){
            $.messager.alert('提示', '最后调解日期不能为空', 'info', function() {});
            return;
        }
        if($("#mediationTel").val() != "" && $("#mediationTel").val().length != 11){
            $.messager.alert('提示', '化解责任人电话格式不正确', 'info', function() {});
            return;
        }
        if(isValid){
            modleopen();
            var flag = 0;
            $("#tableForm").ajaxSubmit(function(data) {
                if(data.success){
                    modleclose();
                    layer.msg("化解信息填写成功");
                    setTimeout(function () {
                        parent.subTaskCallBack();
                    },700)
                }else {
                    modleclose();
                    $.messager.alert('错误', '保存失败', 'error');
                }
            });
        }
    }

    function cancl(){
        parent.closeMaxJqueryWindow();
    }

    //化解信息验证
    function valid (){
        if($("#mediationType").val() == ''){
            $.messager.alert('提示', '化解方式不能为空', 'info', function() {});
            return false;
        }
        if($("#mediationDateStr").val() == ''){
                $.messager.alert('提示', '最后调解日期不能为空', 'info', function() {});
                return false;
        }
        if($("#mediator").val()== ''){
            $.messager.alert('提示', '化解责任人姓名不能为空', 'info', function() {});
            return false;
        }
        if($("#mediationTel").val()== ''){
            $.messager.alert('提示', '化解责任人电话不能为空', 'info', function() {});
            return false;
        }
        if($("#mediationTel").val().length != 11){
            $.messager.alert('提示', '化解责任人电话格式不正确', 'info', function() {});
            return false;
        }
        if($("#mediationOrgName").val()== ''){
            $.messager.alert('提示', '化解组织不能为空', 'info', function() {});
            return false;
        }
        if($("#isSuccessStr").val()== '请选择'){
            $.messager.alert('提示', '化解是否成功不能为空', 'info', function() {});
            return false;
        }
        if($("#mediationResult").val() == ''){
            $.messager.alert('提示', '化解情况不能为空', 'info', function() {});
            return false;
        }else{
            return true;
        }
    }
</script>
</body>
</html>
