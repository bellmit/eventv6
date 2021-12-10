<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>${moduleName}</title>
    <#include "/component/layuiCommon/common_form_files_1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
    <#include "/component/szcomponentsFiles-1.1.ftl" />
<style>
    .hrtype{
        height: 0px;
        margin: 18px 0;
        border-bottom: 1px solid #00968852;
        clear: both;
        background: #eee;
        width: 98%;
        margin-left: 1%;
    }
</style>

</head>

<body class="ovh">
<div  style="width: 100%; height: 100%;">
    <div class="pl10 pr10 pt10 pb10 bs height-100 layui-form">
        <div class="height-100 bs">
            <p class="font-size-14 ml15 font-bold sdc-title pl10 mt15">
                ${moduleName}
                <span class="layui-btn layui-btn-normal layui-btn-25 details-back fr mt2 mr10 fr-mt2" id="close" onclick="closeBtn();">关闭</span>
            </p>
            <div class="pl15 pr15 bs scroll" style="outline: none; padding-bottom: 50px; height: calc(100% - 150px); width: 100%;">
                <div class="layui-row sn-info lay-form-col-br mt15 mb10">
                    <div class="layui-col-xs12">
                        <p class="font-size-14 font-bold pl20 mb15">基本信息</p>
                        <div class="ml25 mr25">
                            <form id="submitFormbase" class="clearfix">
                            <input id="undCaseUuid" name="undCaseUuid" value="${bo.undCaseUuid}" type="hidden">
                            <input id="undCaseId" name="undCaseId" value="${bo.undCaseId}" type="hidden">
                            <input id="fillStatus"  name="fillStatus" value="00" type="hidden">
                            </form>
                            <form id="submitForm1" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>所属地区：</label>
                                    <div class="layui-input-block">
                                        <input type="hidden" id="gridId" />
                                        <input id="regionCode" name="gridCode" type="hidden" value="${bo.gridCode!}">
                                        <input type="text" placeholder="所属地区"  id="regionName"  lay-verify-custom="required:true" name="gridCodeStr" autocomplete="off" class="layui-input" value="${bo.gridCodeStr!}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>涉案性质：</label>
                                    <div class="layui-input-block">
                                        <input id="involvedNature" name="involvedNature"  lay-verify-custom="required:true" lay-verify-element="letterTypeName" type="hidden"/>
                                        <input id="involvedNatureName"  lay-verify-custom="required:true" class="layui-input" type="text" />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>案件名称：</label>
                                    <div class="layui-input-block">
                                        <input id="caseName" name="caseName" value="${bo.caseName}" lay-verify-custom="required:true, validType:['maxLength[80]']" placeholder="请填写案件名称" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>立案时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="filingTime" id="filingTime" lay-verify-custom="required:true" readonly value="${bo.filingTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>侦查单位：</label>
                                    <div class="layui-input-block">
                                        <input id="investigationUnit" name="investigationUnit" value="${bo.investigationUnit}" lay-verify-custom="required:true, validType:['maxLength[40]']" placeholder="请填写侦查单位" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>查封扣押冻结财产数(万元)：</label>
                                    <div class="layui-input-block">
                                        <input id="freezeAssets" name="freezeAssets" value="${bo.freezeAssets}" lay-verify-custom="required:true, number:[12, 2],validType:['minValue[0]']" placeholder="请填写查封扣押冻结财产数(万元)" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            </form>
                            <hr align=center color=#e6e6e6 class="hrtype">
                            <form id="submitForm2" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>移送起诉时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="referralProsecutionTime" id="referralProsecutionTime" lay-verify-custom="required:true" readonly value="${bo.referralProsecutionTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>公诉单位：</label>
                                    <div class="layui-input-block">
                                        <input id="publicProsecutionUnit" name="publicProsecutionUnit" value="${bo.publicProsecutionUnit}" lay-verify-custom="required:true, validType:['maxLength[40]']" placeholder="请填写公诉单位" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            </form >
                            <hr align=center color=#e6e6e6 class="hrtype">
                            <form id="submitForm3" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>诉出时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="complaintTime" id="complaintTime" lay-verify-custom="required:true" readonly value="${bo.complaintTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>起诉人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="prosecutionsNumber" name="prosecutionsNumber" value="${bo.prosecutionsNumber}" lay-verify-custom="required:true,  number:[5],validType:['minValue[0]']" placeholder="请填写起诉人数(人)" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            </form>
                            <hr align=center color=#e6e6e6 class="hrtype">
                            <form class="layui-form" id="submitForm4" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审单位：</label>
                                    <div class="layui-input-block">
                                        <input name="trialRecords[0].trialLevel" value="1" hidden>
                                        <input id="trialLevel1" name="trialRecords[0].judicialUnit" value="${bo.trialRecords[0].judicialUnit}" lay-verify-custom="required:true,validType:['maxLength[40]']" placeholder="请填写一审单位" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审判决时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[0].judgmentTime" id="judgmentTime1" lay-verify-custom="required:true" readonly value="${bo.trialRecords[0].judgmentTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审状态：</label>
                                    <div class="layui-input-block">
                                        <input id="trialStatus1" name="trialRecords[0].trialStatus"  lay-verify-custom="required:true" lay-verify-element="letterTypeName" type="hidden"/>
                                        <input id="trialStatus1Name"  lay-verify-custom="required:true" class="layui-input" type="text" />

                                    </div>
                                </div>
                            </div>
                                <#if bo.trialRecords[0].trialStatus=="01">
                                    <div style="display: none" id="trialLevel2" class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>二审单位：</label>
                                    <div class="layui-input-block">
                                        <input name="trialRecords[1].trialLevel" value="2" hidden>
                                        <input id="judicialUnit2" name="trialRecords[1].judicialUnit"  placeholder="请填写二审单位" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                                    <div style="display: none" id="trialLevel2Time" class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>二审判决时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[1].judgmentTime" id="judgmentTime2"  readonly >
                                    </div>
                                </div>
                            </div>
                                <#else >
                                 <div id="trialLevel2" class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>二审单位：</label>
                                    <div class="layui-input-block">
                                        <input name="trialRecords[1].trialLevel" value="2" hidden>
                                        <input id="judicialUnit2" name="trialRecords[1].judicialUnit" value="${bo.trialRecords[1].judicialUnit}" lay-verify-custom="required:true, validType:['maxLength[40]']" placeholder="请填写二审单位" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                                 <div id="trialLevel2Time" class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>二审判决时间：</label>
                                    <div class="layui-input-block">
                                        <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[1].judgmentTime" id="judgmentTime2" lay-verify-custom="required:true" readonly value="${bo.trialRecords[1].judgmentTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            </#if>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>判决人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="judgementsNumber" name="judgementsNumber" value="${bo.judgementsNumber}" lay-verify-custom="required:true,  number:[5],validType:['minValue[0]']" placeholder="请填写判决人数(人)" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>重刑人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="otherSentencesNumber" name="otherSentencesNumber" value="${bo.otherSentencesNumber}" lay-verify-custom="required:true,  number:[5],validType:['minValue[0]']" placeholder="请填写其他重刑人数(人)" class="layui-input" type="text"  />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs2 pl15 pr15 ">
                                <div class="layui-form-item">
                                    <label class="layui-form-label indusTip"><i>*</i>案件是否办结：</label>
                                    <div  class="layui-input-block flex flex-ac indus">
                                       <#if (bo.isFinishde)??>
                                           <#if bo.isFinishde==1>
                                               <input name="isFinishde" title="是" value="1" type="radio" lay-skin="primary" checked="checked" lay-filter="isFinishde">
                                               <input name="isFinishde" title="否" value="0" type="radio" lay-skin="primary" lay-filter="isFinishde">
                                           <#elseif bo.isFinishde==0>
                                               <input name="isFinishde" title="是" value="1" type="radio" lay-skin="primary" lay-filter="isFinishde">
                                               <input name="isFinishde" title="否" value="0" type="radio" lay-skin="primary" checked="checked" lay-filter="isFinishde">
                                           </#if>
                                        <#else >
                                            <input name="isFinishde"  title="是" value="1" type="radio" lay-skin="primary"  lay-filter="isFinishde">
                                            <input name="isFinishde" title="否" value="0" type="radio" lay-skin="primary"  lay-filter="isFinishde">
                                       </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs2 pl15 pr15 ">
                                <div class="layui-form-item">
                                    <label class="layui-form-label indusTip"><i>*</i>财产是否处置到位：</label>
                                    <div  class="layui-input-block flex flex-ac indus">
                                        <#if (bo.isDisposed)??>
                                            <#if bo.isDisposed==1>
                                                <input name="isDisposed" title="是" value="1" type="radio" lay-skin="primary" checked="checked" lay-filter="isDisposed">
                                                <input name="isDisposed" title="否" value="0" type="radio" lay-skin="primary" lay-filter="isDisposed">
                                            <#elseif bo.isDisposed==0>
                                                <input name="isDisposed" title="是" value="1" type="radio" lay-skin="primary" lay-filter="isDisposed">
                                                <input name="isDisposed" title="否" value="0" type="radio" lay-skin="primary" checked="checked" lay-filter="isDisposed">
                                            </#if>
                                        <#else >
                                            <input name="isDisposed" title="是" value="1" type="radio" lay-skin="primary" lay-filter="isDisposed">
                                            <input name="isDisposed" title="否" value="0" type="radio" lay-skin="primary" lay-filter="isDisposed">
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs12 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">起诉意见书：</label>
                                    <div id="proseCution" class="parent_div"></div>
                                </div>
                            </div>
                            <div class="layui-col-xs12 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">起诉书：</label>
                                    <div id="indictMent" class="parent_div"></div>
                                </div>
                            </div>
                            <div class="layui-col-xs12 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">判决书(只要上传终审判决书)：</label>
                                    <div id="judgMent" class="parent_div"></div>
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="sn-btn text-align-c">
                    <button type="button" class="layui-btn layui-btn-normal"  onclick="save(1);">保存草稿</button>
                    <button type="button" class="layui-btn layui-btn-normal"  onclick="save(2);">提交</button>
            </div>
        </div>
    </div>
</div>
</body>

</html>

<script type="text/javascript" charset="utf-8">
    var isFinishdeValue="";
    var isDisposedValue="";
    var init = function() {
        // 滚动条美化
        $('.scroll').niceScroll({
            cursorcolor: "#000", //滚动条的颜色
            cursoropacitymax: 0.12, //滚动条的透明度，从0-1
            cursorwidth: "4px", //滚动条的宽度  单位默认px
            cursorborderradius: "2px", //滚动条两头的圆角
            autohidemode: false, //是否隐藏滚动条  true的时候默认不显示滚动条，当鼠标经过的时候显示滚动条
            zindex: 0, //给滚动条设置z-index值
        })
        layui.use(['form', 'table', 'laydate', 'laytpl', 'element', 'layer'], function () {
            var form = layui.form,
                laydate = layui.laydate;
            //日期范围
            laydate.render({
                elem: '#filingTime',
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                // type: 'datetime',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });
            laydate.render({
                elem: '#referralProsecutionTime',
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                // type: 'datetime',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });
            laydate.render({
                elem: '#complaintTime',
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                // type: 'datetime',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });
            laydate.render({
                elem: '#judgmentTime1',
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                // type: 'datetime',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });
            laydate.render({
                elem: '#judgmentTime2',
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                // type: 'datetime',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });
            $(window).on('click', function () {
                $('.scroll').getNiceScroll().resize();
            })
            // 案件是否办结点击事件
            form.on('radio(isFinishde)', function(data){
                var value = data.elem.defaultValue;
                if(value != isFinishdeValue){
                    $('.t-body input').each(function (i, input) {
                        $(input).attr('data-check', "false");
                    })
                    $(this).attr('data-check', "true");
                    $(this).prop('checked', true);
                    isFinishdeValue = data.elem.defaultValue;
                    layui.form.render();
                } else{
                    $('.t-body input').each(function (i,input) {
                        $(input).attr('data-check', "false");
                    })
                    $(this).prop('checked', false);
                    isFinishdeValue = "";
                    layui.form.render();
                }
            });
            //财产是否处置到位
            form.on('radio(isDisposed)', function(data){
                var value = data.elem.defaultValue;
                if(value != isDisposedValue){
                    $('.t-body input').each(function (i, input) {
                        $(input).attr('data-check', "false");
                    })
                    $(this).attr('data-check', "true");
                    $(this).prop('checked', true);
                    isDisposedValue = data.elem.defaultValue;
                    layui.form.render();
                } else{
                    $('.t-body input').each(function (i,input) {
                        $(input).attr('data-check', "false");
                    })
                    $(this).prop('checked', false);
                    isDisposedValue = "";
                    layui.form.render();
                }
            });
        })
        <#-- 涉案性质类型 -->
        AnoleApi.initListComboBox("involvedNatureName", "involvedNature", '${involvedNatureDict}', null, ['${bo.involvedNature!''}'], {
            ShowOptions : {
                EnableToolbar : true
            }
        });
        //所属地域
        particleSize(${regionCode!''});
        AnoleApi.initGridZtreeComboBox("regionName", "gridId", function(gridId, items) {
            if(items != undefined && items != null && items.length > 0) {
                var grid = items[0];
                $("#regionCode").val(grid.orgCode);
                particleSize(grid.orgCode);
            }
        },{
            OnCleared:function(){
                $("#regionCode").val("");
            },
            ShowOptions : {
                EnableToolbar : true
            }});
    function particleSize(infoOrgCode) {
        infoOrgCode = infoOrgCode + "";
        var l = infoOrgCode.length;
        var s = 5;
        // 2:省 4：市 6：区 9：街道 12：社区 15：网格
        switch(l) {
            case 2:
                s = 5;break;
            case 4:
                s = 5;break;
            case 6:
                s = 4;break;
            case 9:
                s = 3;break;
            case 12:
                s = 2;break;
            case 15:
                s = 1;break;
        }
        $(".Check_Radio").removeClass("FontDarkBlue").show().eq(0).addClass("FontDarkBlue").children().attr("checked", "checked");
        $(".Check_Radio:gt(" + (s - 1) + ")").hide();
    }


        <#-- 一审状态 -->
        AnoleApi.initListComboBox("trialStatus1Name", "trialStatus1", '${trialStatusDict}', null, ['${bo.trialRecords[0].trialStatus!''}'], {
            OnSelected:function(value){
                //生效
                if (value=="01"){
                    $('#trialLevel2').css("display","none");
                    $('#trialLevel2Time').css("display","none");
                    $('#judicialUnit2').attr("lay-verify-custom","");
                    $('#judgmentTime2').attr("lay-verify-custom","");

                    $('#judicialUnit2').val("");
                    $('#judgmentTime2').val("");
                }else {
                    $('#trialLevel2').css("display","block");
                    $('#trialLevel2Time').css("display","block");
                    $('#judicialUnit2').attr("lay-verify-custom","required:true, validType:['maxLength[40]']");
                    $('#judgmentTime2').attr("lay-verify-custom","required:true");
                }
            },
            OnCleared:function(){
                $('#trialLevel2').css("display","block");
                $('#trialLevel2Time').css("display","block");
                $('#judicialUnit2').attr("lay-verify-custom","required:true, validType:['maxLength[40]']");
                $('#judgmentTime2').attr("lay-verify-custom","required:true");},
            ShowOptions : {
                EnableToolbar : true
            }
        });
    }
//判断1~3区块form表单中是否有填值
    function checkForm(formId) {
        var form = document.getElementById(formId);
        var tagElements = form.getElementsByTagName('input');
        var status = true;
        for (var j = 0; j < tagElements.length; j++){
            var content =  tagElements[j].value;
            if (content!="" && content!="请选择"){
                status=false;
                break ;
            }
        }
        return status;
    }
    //判断4区块是否有值
    function checkForm4(formId) {
        var status = true;
        var saveData4 = $(formId).serialize();//区块4数据
        saveData4 = decodeURIComponent(saveData4 ,true);//中文乱码
        saveData4.split('&').forEach(function (item) {  //js的forEach()方法
            item = item.split('=');
            var name = item[0],
                val = item[1];
            if (name=='trialRecords[0].trialLevel' || name=='trialRecords[1].trialLevel'){
                val="";
            }
            if (val!=""){
                status=false;
            }
        });
        return status;
    }
    //判断4区块radio是否有选中
    function ischeck(formId) {
        var status = false;
        var isFinishde = false;
        var isDisposed = false;
        var saveData4 = $(formId).serialize();//区块4数据
        saveData4 = decodeURIComponent(saveData4 ,true);//中文乱码
        saveData4.split('&').forEach(function (item) {  //js的forEach()方法
            item = item.split('=');
            var name = item[0];
            if (name=="isFinishde"){
                isFinishde=true;
            }
            if (name=="isDisposed"){
                isDisposed=true;
            }
        });
        status = isFinishde && isDisposed
        return status;
    }
    //保存
    function save(type) {
        //各区块如果有填报则必填项必填完整,如果区块没填则不用
        var isAllnull1 = checkForm('submitForm1');
        var isAllnull2 = checkForm('submitForm2');
        var isAllnull3 = checkForm('submitForm3');
        var isAllnull4 = checkForm4('#submitForm4');
        var isCheck = ischeck('#submitForm4');
        //第一区块为基本信息必填
        if (isAllnull1){
            layer.msg("第一区块内容不可为空!");
            return;
        }else {
            var flag1 = LayuiComp.valdationCom.verification($('#submitForm1'));
            if(!flag1){
                layer.msg("第一区块必填项需填完整!");
                return;
            }
        }
        //第二区块校验
        if (!isAllnull2){
            var flag2 = LayuiComp.valdationCom.verification($('#submitForm2'));
            if(!flag2){
                layer.msg("第二区块必填项需填完整!");
                return;
            }
        }
        //第三区块校验
        if (!isAllnull3){
            var flag3 = LayuiComp.valdationCom.verification($('#submitForm3'));
            if(!flag3){
                layer.msg("第三区块必填项需填完整!");
                return;
            }
        }
        //第四区块校验
        if (!isAllnull4){
            var flag4 = LayuiComp.valdationCom.verification($('#submitForm4'));
            if(!flag4){
                layer.msg("第四区块必填项需填完整!");
                return;
            }
            if (!isCheck){
                layer.msg("第四区块必填项需填完整!");
                return;
            }
        }
        var tips = '确定要保存草稿吗?';
        if (type==1){
            //保存草稿
            $('#fillStatus').val("00");
        }
        if(type==2){
            //提交
            tips='确定要提交吗?';
            $('#fillStatus').val("01");
        }
        //各表单数据
        var saveDatabase = $('#submitFormbase').serialize();//基础数据
        var saveData1 = $('#submitForm1').serialize();//区块1数据
        var saveData2 = $('#submitForm2').serialize();//区块2数据
        var saveData3 = $('#submitForm3').serialize();//区块3数据
        var saveData4 = $('#submitForm4').serialize();//区块4数据
        var saveData = saveDatabase +"&"+ saveData1 +"&"+ saveData2 +"&"+ saveData3 +"&"+ saveData4;
        layer.confirm(tips,{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {
            var submitLoad = layer.load(1);//打开遮罩
            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/caseFilling/save.json',
                data: saveData,
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        parent.layer.closeAll();
                        parent.layer.msg("提交成功", {skin: 'layui-layer-hui'});
                        reloadParentTable();
                    } else {
                        layer.msg(response.message, {skin: 'layui-layer-hui'});
                    }
                },
                error: function(response) {
                    layer.msg("连接超时", {skin: 'layui-layer-hui'});
                },
                complete : function() {
                    layer.close(submitLoad);
                }
            });

        })
    }
    var reloadParentTable = function() {
        parent.layui.table.reload('data1', {page: {curr: $(".layui-laypage-em").next().html()}});
        parent.layer.closeAll();
    }
    var closeBtn = function() {
        layer.confirm("尚未保存信息,确定要退出当前窗口吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index){
            parent.layer.closeAll();
        })

    }
    //初始化附件上传组件
    function bigUpload(id,bizType) {
        var bigUploadID ="#"+id;
        var bizType=bizType;
        bigUploads = $(bigUploadID).bigfileUpload({
            useType: 'edit',//附件上传的使用类型，add,edit,view，（默认edit）;
            componentsDomain : componentsDomain,//公共组件域名
            useDomainCache:true,
            fileExt : '.pdf,.doc,.docx',//允许上传的附件类型
            appcode:"event",//文件所属的应用代码（默认值components）
            module:"triadRelatedCases",//文件所属的模块代码（默认值bigfile）
            attachmentData:{bizId:'${bo.undCaseUuid}',attachmentType:bizType},
            isDelDbData:false,
            isDelDiscData:false,
            isSaveDB:false,
            useUUIDTable:"yes",
            styleType :'list',
            maxSingleFileSize : 20,
            uploadSuccessCallback : function(file,response){

            },
            deleteCallback:function(obj){

            },
            finishEleRander:function(){

            }
        });
    }
</script>

<script type="text/javascript" charset="utf-8">

    $(function() {
        init();
        //初始化附件上传
        bigUpload('proseCution','proseCution');
        bigUpload('indictMent','indictMent');
        bigUpload('judgMent','judgMent');
    })

</script>