<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>${moduleName}</title>
    <#include "/component/layuiCommon/common_form_files_1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
    <#include "/component/szcomponentsFiles-1.1.ftl" />
    <#if detailType !="">
        <!-- 扫黑除恶详情样式 大屏就添加 后台就移出 -->
        <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/jx-sanshuyihan/pages/blue/css/shce-xiangq.css" />
    </#if>
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
<form id="submitForm" style="width: 100%; height: 100%;">
    <div class="pl10 pr10 pt10 pb10 bs height-100 layui-form">
        <div class="height-100 bs">
            <p class="font-size-14 ml15 font-bold sdc-title pl10 mt15">
                涉黑案件详情
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
                                        <input type="text" placeholder="所属地区"  id="regionName"  name="gridCodeStr" autocomplete="off" class="layui-input" value="${bo.gridCodeStr}" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>涉案性质：</label>
                                    <div class="layui-input-block">
                                        <#if (bo.involvedNature)??>
                                            <#switch bo.involvedNature>
                                                <#case "01">
                                                    <input id="involvedNatureName" value="黑社会性质组织案件"  class="layui-input" type="text" readonly />
                                                    <#break>
                                                <#case "02">
                                                    <input id="involvedNatureName" value="恶势力犯罪集团案件"  class="layui-input" type="text" readonly />
                                                    <#break>
                                                <#case "03">
                                                    <input id="involvedNatureName" value="恶势力犯罪团伙案件"  class="layui-input" type="text" readonly />
                                                <#break>
                                                <#default>
                                            </#switch>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>案件名称：</label>
                                    <div class="layui-input-block">
                                        <input id="caseName" name="caseName" value="${bo.caseName}"  placeholder="请填写案件名称" class="layui-input" type="text" readonly />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>立案时间：</label>
                                    <div class="layui-input-block">
                                        <input  type="text" placeholder="请选择时间" class="layui-input" name="filingTime" id="filingTime"  readonly value="${bo.filingTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>侦查单位：</label>
                                    <div class="layui-input-block">
                                        <input id="investigationUnit" name="investigationUnit" value="${bo.investigationUnit}"  placeholder="请填写侦查单位" class="layui-input" type="text" readonly />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>查封扣押冻结财产数(万元)：</label>
                                    <div class="layui-input-block">
                                        <input id="freezeAssets" name="freezeAssets" value="${bo.freezeAssets}"  placeholder="请填写查封扣押冻结财产数(万元)" class="layui-input" type="text"  readonly/>
                                    </div>
                                </div>
                            </div>
                            </form>
                            <#if (bo.referralProsecutionTime)??>
                                <hr align=center color=#e6e6e6 class="hrtype">
                            <form id="submitForm2" class="clearfix">
                                <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>移送起诉时间：</label>
                                    <div class="layui-input-block">
                                        <input  type="text" placeholder="请选择时间" class="layui-input" name="referralProsecutionTime" id="referralProsecutionTime"  readonly value="${bo.referralProsecutionTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                                <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>公诉单位：</label>
                                    <div class="layui-input-block">
                                        <input id="publicProsecutionUnit" name="publicProsecutionUnit" value="${bo.publicProsecutionUnit}"  placeholder="请填写公诉单位" class="layui-input" type="text" readonly />
                                    </div>
                                </div>
                            </div>
                            </form>
                            </#if>

                            <#if (bo.complaintTime)??>
                                <hr align=center color=#e6e6e6 class="hrtype">
                            <form id="submitForm3" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>诉出时间：</label>
                                    <div class="layui-input-block">
                                        <input type="text" placeholder="请选择时间" class="layui-input" name="complaintTime" id="complaintTime"  readonly value="${bo.complaintTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>起诉人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="prosecutionsNumber" name="prosecutionsNumber" value="${bo.prosecutionsNumber}" placeholder="请填写起诉人数(人)" class="layui-input" type="text"  readonly/>
                                    </div>
                                </div>
                            </div>
                            </form>
                            </#if>
                            <#if (bo.trialRecords[0].judicialUnit)??>
                            <hr align=center color=#e6e6e6 class="hrtype">
                            <form id="submitForm4" class="clearfix">
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审单位：</label>
                                    <div class="layui-input-block">
                                        <input name="trialRecords[0].trialLevel" value="1" hidden>
                                        <input id="trialLevel1" name="trialRecords[0].judicialUnit" value="${bo.trialRecords[0].judicialUnit}"  placeholder="请填写起诉人数(人)" class="layui-input" type="text"  readonly/>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审判决时间：</label>
                                    <div class="layui-input-block">
                                        <input  type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[0].judgmentTime" id="judgmentTime1"  readonly value="${bo.trialRecords[0].judgmentTime?string('yyyy-MM-dd')}">
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>一审状态：</label>
                                    <div class="layui-input-block">
                                        <#if (bo.trialRecords[0].trialStatus)??>
                                            <#switch bo.trialRecords[0].trialStatus>
                                                <#case "01">
                                                    <input id="trialStatus1Name" value="生效"   class="layui-input" type="text" readonly/>                                                    <#break>
                                                <#case "02">
                                                <input id="trialStatus1Name"  value="上诉（抗诉）" class="layui-input" type="text" readonly/>                                                    <#break>
                                                <#default>
                                            </#switch>
                                        </#if>

                                    </div>
                                </div>
                            </div>
                                <#if bo.trialRecords[0].trialStatus=="01">
                                    <div style="display: none" id="trialLevel2" class="layui-col-xs4 pl15 pr15">
                                        <div class="layui-form-item">
                                            <label class="layui-form-label"><i>*</i>二审单位：</label>
                                            <div class="layui-input-block">
                                                <input name="trialRecords[1].trialLevel" value="2" hidden>
                                                <input id="judicialUnit2" name="trialRecords[1].judicialUnit"  placeholder="请填写二审单位" class="layui-input" type="text" readonly />
                                            </div>
                                        </div>
                                    </div>
                                    <div style="display: none" id="trialLevel2Time" class="layui-col-xs4 pl15 pr15">
                                        <div class="layui-form-item">
                                            <label class="layui-form-label"><i>*</i>二审判决时间：</label>
                                            <div class="layui-input-block">
                                                <input type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[1].judgmentTime" id="judgmentTime2"  readonly >
                                            </div>
                                        </div>
                                    </div>
                            <#else >
                                <div id="trialLevel2" class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>二审单位：</label>
                                        <div class="layui-input-block">
                                            <input name="trialRecords[1].trialLevel" value="2" hidden>
                                            <input id="judicialUnit2" name="trialRecords[1].judicialUnit" value="${bo.trialRecords[1].judicialUnit}" placeholder="请填写二审单位" class="layui-input" type="text" readonly />
                                        </div>
                                    </div>
                                </div>
                                <div id="trialLevel2Time" class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>二审判决时间：</label>
                                        <div class="layui-input-block">
                                            <input  type="text" placeholder="请选择时间" class="layui-input" name="trialRecords[1].judgmentTime" id="judgmentTime2"  readonly value="${bo.trialRecords[1].judgmentTime?string('yyyy-MM-dd')}">
                                        </div>
                                    </div>
                                </div>
                            </#if>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>判决人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="judgementsNumber" name="judgementsNumber" value="${bo.judgementsNumber}" placeholder="请填写判决人数(人)" class="layui-input" type="text" readonly />
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs4 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><i>*</i>重刑人数(人)：</label>
                                    <div class="layui-input-block">
                                        <input id="otherSentencesNumber" name="otherSentencesNumber" value="${bo.otherSentencesNumber}"  placeholder="请填写其他重刑人数(人)" class="layui-input" type="text"  readonly/>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs2 pl15 pr15 ">
                                <div class="layui-form-item">
                                    <label class="layui-form-label indusTip"><i>*</i>案件是否办结：</label>
                                    <div class="layui-input-block flex flex-ac indus">
                                        <#if (bo.isFinishde)??>
                                            <#if bo.isFinishde==1>
                                                <input name="isFinishde" title="是" value="1" type="radio" lay-skin="primary" checked="checked"><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon"></i><div>是</div></div>
                                                <input name="isFinishde" title="否" value="0" type="radio" lay-skin="primary"><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><div>否</div></div>
                                            <#elseif bo.isFinishde==0>
                                                <input name="isFinishde" title="是" value="1" type="radio" lay-skin="primary"><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><div>是</div></div>
                                                <input name="isFinishde" title="否" value="0" type="radio" lay-skin="primary" checked="checked"><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon"></i><div>否</div></div>
                                            </#if>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-col-xs2 pl15 pr15 ">
                                <div class="layui-form-item">
                                    <label class="layui-form-label indusTip"><i>*</i>财产是否处置到位：</label>
                                    <div class="layui-input-block flex flex-ac indus">
                                        <#if (bo.isDisposed)??>
                                            <#if bo.isDisposed==1>
                                                <input name="isDisposed" title="是" value="1" type="radio" lay-skin="primary" checked="checked"><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon"></i><div>是</div></div>
                                                <input name="isDisposed" title="否" value="0" type="radio" lay-skin="primary"><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><div>否</div></div>
                                            <#elseif bo.isDisposed==0>
                                                <input name="isDisposed" title="是" value="1" type="radio" lay-skin="primary"><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><div>是</div></div>
                                                <input name="isDisposed" title="否" value="0" type="radio" lay-skin="primary" checked="checked"><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon"></i><div>否</div></div>
                                            </#if>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            </#if>
                            <div id="proseCutionDis" class="layui-col-xs12 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">起诉意见书：</label>
                                    <div id="proseCution" class="parent_div"></div>
                                </div>
                            </div>
                            <div id="indictMentDis" class="layui-col-xs12 pl15 pr15">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">起诉书：</label>
                                    <div id="indictMent" class="parent_div"></div>
                                </div>
                            </div>
                            <div id="judgMentDis" class="layui-col-xs12 pl15 pr15">
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
                <button type="button" class="layui-btn layui-btn-normal"  onclick="closeBtn();">关闭</button>
            </div>
        </div>
    </div>
</form>
</body>

</html>

<script type="text/javascript" charset="utf-8">

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

    }

    //保存
    function save(type) {
        //必填项验证
        var flag = LayuiComp.valdationCom.verification($('#submitForm'));
        if(!flag){
            layer.msg("必填项为空,请填写必填项");
            return;
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
        var saveData = $('#submitForm').serialize();
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
            parent.layer.closeAll();
    }


    //初始化附件上传组件
    function bigUpload(id,bizType) {
        var bigUploadID ="#"+id;
        var bizType=bizType;
        bigUploads = $(bigUploadID).bigfileUpload({
            useType: 'view',//附件上传的使用类型，add,edit,view，（默认edit）;
            componentsDomain : componentsDomain,//公共组件域名
            skyDomain:skyDomain,
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
            initFileElemsCallback : function(fileShowObjArr,opt){
                if (fileShowObjArr[0]==undefined){
                    var displayType = bigUploadID+'Dis';
                    $(displayType).css("display","none");
                }
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