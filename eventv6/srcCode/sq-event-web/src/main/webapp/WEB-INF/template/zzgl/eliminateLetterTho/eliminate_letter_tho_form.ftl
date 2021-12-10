<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>添加文书</title>
    <#include "/component/layuiCommon/common_form_files_1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
    <script type="text/javascript" charset="utf-8" src="${uiDomain!''}/js/jquery.form.js"></script>
    <script type="text/javascript" charset="utf-8" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" charset="utf-8" src="${uiDomain}/web-assets/plugins/layui-ext/verification/v1.0.0/js/layui-verification.js"></script>
    <script src="${uiDomain!''}/app-assets/extend/wap-person-select/js/jquery.ffcs.personselect.js?v=1.0.010"></script>
    <script src="${uiDomain!''}/app-assets/extend/wap-person-select/js/custom_msgClient.js"></script>
</head>

<body class="ovh">
    <form id="thoForm" style="width: 100%; height: 100%;">
        <div class="pl10 pr10 pt10 pb10 bs height-100 layui-form">
            <div class="height-100 bs">
                <p class="font-size-14 ml15 font-bold sdc-title pl10 mt15" id="closeP">
                    ${title}
                    <span class="layui-btn layui-btn-normal layui-btn-25 details-back fr mt2 mr10 fr-mt2" id="close" onclick="closeBtn();">关闭</span>
                </p>
                <div class="pl15 pr15 bs scroll" style="outline: none; padding-bottom: 50px; height: calc(100% - 150px); width: 100%;">
                    <div class="layui-row sn-info lay-form-col-br mt15 mb10">

                        <div class="layui-col-xs12">

                            <p class="font-size-14 font-bold pl20 mb15">基本信息（制发单位填写）</p>

                            <div class="ml25 mr25">
                                <input id="thoUuid" name="thoUuid" value="${thoUuid}" type="hidden">
                                <input id="thtNo" name="thtNo" value="${thtNo}" type="hidden">

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>“三书一函”编码：</label>
                                        <div class="layui-input-block">
                                            <input value="${thtNo}" disabled placeholder="无需填写，系统自动分配" class="layui-input" disabled type="text">
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>文书类型：</label>
                                        <div class="layui-input-block">
                                            <input id="letterType" name="letterType" value="${letterType}" lay-verify-custom="required:true" lay-verify-element="letterTypeName" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                            <input id="letterTypeName" name="letterTypeName" value="${letterTypeName}" lay-verify-custom="required:true" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>发出时间：</label>
                                        <div class="layui-input-block">
                                            <input id="fbDateStr" name="fbDateStr" value="${fbDateStr!''}" lay-verify-custom="required:true" type="text" class="layui-input" placeholder="发出时间" autocomplete="off" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item my-layui-form-item">
                                        <label class="layui-form-label"><i>*</i>制发单位：</label>
                                        <div class="layui-col-xs12 flex">
                                            <div class="layui-input-inline">
                                                <input id="fbDepartCode" name="fbDepartCode" value="${fbDepartCode}" lay-verify-custom="required:true" lay-verify-element="fbDepartName" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                                <input id="fbDepartName" name="fbDepartName" value="${fbDepartName}" lay-verify-custom="required:true" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")}/>
                                            </div>
                                            <div class="flex1">
                                                <input id="fbDepartNameDet" name="fbDepartNameDet" value="${fbDepartNameDet!}" lay-verify-custom="required:true, validType:['maxLength[50]']" placeholder="单位名称" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")} />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>接收单位：</label>
                                        <div class="layui-input-block">
                                            <input id="reUserName" name="reChgs.reUserName" value="${reUserName!}" lay-verify-custom="required:true" class="layui-input" type="text" autocomplete="off" ${(bizStatus != "99")?string("disabled","")} />
                                            <input id="reUser" name="reChgs.reUser" value='${reUser!}' lay-verify-custom="required:true" class="layui-input" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                            <input id="reUserId" name="reChgs.reUserId" value="${reUserId!}" lay-verify-custom="required:true" class="layui-input" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                            <input id="reDepartCode" name="reChgs.reDepartCode" value="${reDepartCode!}" lay-verify-custom="required:true" class="layui-input" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                            <input id="reDepartNameDet" name="reChgs.reDepartNameDet" value="${reDepartNameDet!}" lay-verify-custom="required:true" class="layui-input" type="hidden" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>文号：</label>
                                        <div class="layui-input-block">
                                            <input id="letterNo" name="letterNo" value="${letterNo!}" lay-verify-custom="required:true, validType:['maxLength[50]']" placeholder="请填写文号" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>案件名称：</label>
                                        <div class="layui-input-block">
                                            <input id="caseName" name="caseName" value="${caseName}" lay-verify-custom="required:true, validType:['maxLength[200]']" placeholder="请填写案件名称" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs4 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>案件编码：</label>
                                        <div class="layui-input-block">
                                            <input id="caseNo" name="caseNo" value="${caseNo}" lay-verify-custom="required:true, validType:['maxLength[50]']" placeholder="请填写案件编码" class="layui-input" type="text" ${(bizStatus != "99")?string("disabled","")} />
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs12 pl15 pr15 indus">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label indusTip"><i>*</i>行业、领域名称：</label>
                                        <div class="layui-input-block flex flex-ac indus">
                                            <#list indus as item>
                                                <#if item.dictName != '其他'>
                                                    <#assign exists = 0>
                                                    <#list checkedIndus as checkedItem>
                                                        <#if item.dictGeneralCode == checkedItem.industrialCode>
                                                            <#assign exists = 1>
                                                            <#break>
                                                        </#if>
                                                    </#list>
                                                    <#if exists == 1>
                                                        <input name="induss[${item_index}].industrialCode" title="${item.dictName}" value="${item.dictGeneralCode}" type="checkbox" lay-skin="primary" lay-filter="industrialCode" checked ${(bizStatus != "99")?string("disabled","")}>
                                                    <#else>
                                                        <input name="induss[${item_index}].industrialCode" title="${item.dictName}" value="${item.dictGeneralCode}" type="checkbox" lay-skin="primary" lay-filter="industrialCode" ${(bizStatus != "99")?string("disabled","")}>
                                                    </#if>
                                                </#if>
                                            </#list>
                                        </div>
                                    </div>
                                </div>

                                <#assign existOther = 0>
                                <div class="layui-col-xs12 pl15 pr15 indus">
                                    <div class="layui-col-xs1">
                                        <div class="layui-form-item">
                                            <div class="layui-input-block flex flex-ac" class="other-indus">
                                                <#list indus as item>
                                                    <#if item.dictName == '其他'>
                                                        <#assign exists = 0>
                                                        <#list checkedIndus as checkedItem>
                                                            <#if item.dictGeneralCode == checkedItem.industrialCode>
                                                                <#assign exists = 1>
                                                                <#assign existOther = 1>
                                                                <#break>
                                                            </#if>
                                                        </#list>
                                                        <#if exists == 1>
                                                            <input name="induss[${item_index}].industrialCode" title="${item.dictName}" value="${item.dictGeneralCode}" type="checkbox" lay-skin="primary" lay-filter="industrialCode" checked ${(bizStatus != "99")?string("disabled","")}>
                                                        <#else>
                                                            <input name="induss[${item_index}].industrialCode" title="${item.dictName}" value="${item.dictGeneralCode}" type="checkbox" lay-skin="primary" lay-filter="industrialCode" ${(bizStatus != "99")?string("disabled","")}>
                                                        </#if>
                                                    </#if>
                                                </#list>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="layui-col-xs11">
                                        <div class="layui-input-block posi-rela">
                                            <textarea id="industrialComment" name="industrialComment" lay-verify-custom="validType:['maxLength[2000]']" class="layui-textarea lt-min-height lt-min-height1" placeholder="补充说明，选择“其他”时填写" ${( existOther != 1 || bizStatus != "99")?string("disabled","")}>${industrialComment}</textarea>
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-col-xs12 pl15 pr15">
                                    <div class="layui-form-item">
                                        <label class="layui-form-label"><i>*</i>文书内容：</label>
                                        <div class="layui-input-block posi-rela">
                                            <textarea id="letterContentClob" name="letterContentClob" target-id="letterContentClobRemark" maxlength="2000" onkeyup="textareaRemark(this, 2000);" lay-verify-custom="required:true,validType:['maxLength[2000]']" class="layui-textarea lt-min-height lt-min-height1" placeholder="请填写文书内容" ${(bizStatus != "99")?string("disabled","")}>${letterContentClob}</textarea>
                                            <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="letterContentClobRemark">0 / 2000字</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <#-- 草稿(bizStatus:99)、行业部门(professionalType:SSYH_HYBM_ZY)、扫黑办(professionalType:shbzy) -->
                        <#if bizStatus != "99">

                            <#-- 制发单位派发给行业部门 -->
                            <#if bizStatus == "01" || bizStatus == "02" || bizStatus == "03" || bizStatus == "03" || bizStatus == "04">

                                <div class="layui-col-xs12 mt60">

                                    <p class="font-size-14 font-bold pl20 mb15">接收情况（接收单位填写）</p>

                                    <div class="ml25 mr25">
                                        <div class="layui-col-xs12 pl15 pr15">
                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i>*</i>接收时间：</label>
                                                <div class="layui-input-block">
                                                    <input id="reDateStr" name="reChgs.reDateStr" value="${reDateStr!''}" lay-verify-custom="required:true" type="text" class="layui-input" placeholder="接收时间" autocomplete="off" ${(bizStatus != "01")?string("disabled","")} />
                                                </div>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15">
                                            <label class="layui-form-label"><i>*</i>回复详情：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="reType" name="reChgs.reType" value="${reType}" lay-verify-custom="required:true" lay-verify-element="reTypeName" type="hidden" ${(bizStatus != "01")?string("disabled","")}/>
                                                            <input id="reTypeName" name="reChgs.reTypeName" value="${reTypeName}" lay-verify-custom="required:true" class="layui-input" type="text" ${(bizStatus != "01")?string("disabled","")}/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-input-block posi-rela layui-form-item">
                                                        <textarea id="reDetail" name="reChgs.reDetail" target-id="reTypeRemark" lay-verify-custom="required:true,validType:['maxLength[1000]']" maxlength="1000" onkeyup="textareaRemark(this, 1000);" class="layui-textarea lt-min-height lt-min-height1" placeholder="回复详情" ${(bizStatus != "01")?string("disabled","")}>${reDetail}</textarea>
                                                        <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="reTypeRemark">0 / 1000字</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15">
                                            <label class="layui-form-label"><i>*</i>是否提出异议：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="reDissentAgree" name="reChgs.reDissentAgree" value="${reDissentAgree}" lay-verify-custom="required:true" lay-verify-element="reDissentAgreeName" type="hidden" ${(bizStatus != "01")?string("disabled","")}/>
                                                            <input id="reDissentAgreeName" name="reChgs.reDissentAgreeName" value="${reDissentAgreeName}" lay-verify-custom="required:true" class="layui-input" type="text" ${(bizStatus != "01")?string("disabled","")} />
                                                        </div>
                                                    </div>
                                                </div>
                                                <#if bizStatus == "01" || (reDissentDetail != null && reDissentDetail?length gt 0)>
                                                    <div class="layui-col-xs12 ${(reDissentDetail != null && reDissentDetail?length gt 0)?string('', 'hide')}">
                                                        <div class="layui-input-block posi-rela layui-form-item">
                                                            <textarea id="reDissentDetail" name="reChgs.reDissentDetail" target-id="reDissentDetailRemark" onkeyup="textareaRemark(this, 1000);" maxlength="1000" lay-verify-custom="validType:['maxLength[1000]']" class="layui-textarea lt-min-height lt-min-height1" ${(bizStatus == "01" && indusChgDetail?length == 0)?string('placeholder="异议详情"','')} ${(bizStatus != "01")?string("disabled","")}>${reDissentDetail}</textarea>
                                                            <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="reDissentDetailRemark">0 / 1000字</p>
                                                        </div>
                                                    </div>
                                                </#if>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15 hide" id="chgTypeDiv">
                                            <label class="layui-form-label"><i>*</i>是否开展整改：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="chgType" name="reChgs.chgType" value="${chgType}" type="hidden" ${(bizStatus != "01")?string("disabled","")}/>
                                                            <input id="chgTypeName" name="reChgs.chgTypeName" value="${chgTypeName}"  class="layui-input" type="text" ${(bizStatus != "01")?string("disabled","")}/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <#if bizStatus == "01" || (chgDetail != null && chgDetail?length gt 0)>
                                                    <div class="layui-col-xs12">
                                                        <div class="layui-input-block posi-rela layui-form-item">
                                                            <textarea id="chgDetail" name="reChgs.chgDetail" target-id="chgDetailRemark" onkeyup="textareaRemark(this, 1000);" maxlength="1000" lay-verify-custom="validType:['maxLength[1000]']" class="layui-textarea lt-min-height lt-min-height1" ${(bizStatus != "01" && chgDetail?length == 0)?string('placeholder="整改详情"','')} ${(bizStatus != "01")?string("disabled","")}>${chgDetail}</textarea>
                                                            <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="chgDetailRemark">0 / 1000字</p>
                                                        </div>
                                                    </div>
                                                </#if>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15 hide" id="indusChgAgreeDiv">
                                            <label class="layui-form-label"><i>*</i>是否开展行业治理：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="indusChgAgree" name="reChgs.indusChgAgree" value="${indusChgAgree}" type="hidden" ${(bizStatus != "01")?string("disabled","")}/>
                                                            <input id="indusChgAgreeName" name="reChgs.indusChgAgreeName" value="${indusChgAgreeName}" class="layui-input" type="text" ${(bizStatus != "01")?string("disabled","")} />
                                                        </div>
                                                    </div>
                                                </div>
                                                <#if bizStatus == "01" || (indusChgDetail != null && indusChgDetail?length gt 0)>
                                                    <div class="layui-col-xs12">
                                                        <div class="layui-input-block posi-rela layui-form-item">
                                                            <textarea id="indusChgDetail" name="reChgs.indusChgDetail" target-id="indusChgDetailRemark" onkeyup="textareaRemark(this, 1000);" maxlength="1000" lay-verify-custom="validType:['maxLength[1000]']" class="layui-textarea lt-min-height lt-min-height1" ${(bizStatus != "01" && indusChgDetail?length == 0)?string('placeholder="开展行业治理情况"','')} ${(bizStatus != "01")?string("disabled","")}>${indusChgDetail}</textarea>
                                                            <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="indusChgDetailRemark">0 / 1000字</p>
                                                        </div>
                                                    </div>
                                                </#if>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15 hide" id="longActionAgreeDiv">
                                            <label class="layui-form-label"><i>*</i>是否建立长效机制：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="longActionAgree" name="reChgs.longActionAgree" value="${longActionAgree}" type="hidden" ${(bizStatus != "01")?string("disabled","")}/>
                                                            <input id="longActionAgreeName" name="reChgs.longActionAgreeName"  value="${longActionAgreeName}" class="layui-input" type="text" ${(bizStatus != "01")?string("disabled","")}/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <#if bizStatus == "01" || (longActionDetail != null && longActionDetail?length gt 0)>
                                                    <div class="layui-col-xs12">
                                                        <div class="layui-input-block posi-rela layui-form-item">
                                                            <textarea id="longActionDetail" name="reChgs.longActionDetail" target-id="longActionDetailRemark" onkeyup="textareaRemark(this, 1000);" maxlength="1000" lay-verify-custom="validType:['maxLength[1000]']" class="layui-textarea lt-min-height lt-min-height1" ${(bizStatus != "01" && longActionDetail?length == 0)?string('placeholder="建立长效机制情况"','')} ${(bizStatus != "01")?string("disabled","")}>${longActionDetail}</textarea>
                                                            <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="longActionDetailRemark">0 / 1000字</p>
                                                        </div>
                                                    </div>
                                                </#if>
                                            </div>
                                        </div>

                                        <div class="layui-col-xs12 pl15 pr15 hide" id="othClobDiv">
                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i>*</i>其他需要说明的情况：</label>
                                                <div class="layui-input-block posi-rela layui-form-item">
                                                    <textarea id="othClob" name="reChgs.othClob" target-id="othClobRemark" lay-verify-custom="validType:['maxLength[2000]']" maxlength="2000" onkeyup="textareaRemark(this, 2000);" class="layui-textarea lt-min-height lt-min-height1" placeholder="没有请填写“无”" ${(bizStatus != "01")?string("disabled","")}>${othClob}</textarea>
                                                    <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="othClobRemark">0 / 2000字</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>

                            </#if>

                            <#-- 行业部门完成表单，提交给制发单位-->
                            <#if (bizStatus == "01" && (dissentAgree?? || dissentDetail ??)) || bizStatus == "02" || bizStatus == "03" || bizStatus == "04" >

                                <div class="layui-col-xs12 mt60">

                                    <p class="font-size-14 font-bold pl20 mb15">异议处理情况（制发单位填写）</p>

                                    <div class="ml25 mr25">

                                        <div class="layui-col-xs12 pl15 pr15">
                                            <label class="layui-form-label">是否同意异议：</label>
                                            <div>
                                                <div class="layui-col-xs12">
                                                    <div class="layui-form-item">
                                                        <div class="layui-input-block">
                                                            <input id="dissentAgree" name="reChgs.dissentAgree" value="${dissentAgree}" lay-verify-custom="required:false" lay-verify-element="dissentAgreeName" type="hidden" ${(bizStatus != "02")?string("disabled","")}/>
                                                            <input id="dissentAgreeName" name="reChgs.dissentAgreeName" value="${dissentAgreeName}" lay-verify-custom="required:false" class="layui-input" type="text" ${(bizStatus != "02")?string("disabled","")}/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-col-xs12" id="dissentAgreeTxt">
                                                    <div class="layui-input-block posi-rela layui-form-item">
                                                        <textarea id="dissentDetail" name="reChgs.dissentDetail" target-id="dissentDetailRemark" onkeyup="textareaRemark(this, 1000);" maxlength="1000" lay-verify-custom="validType:['maxLength[1000]']" class="layui-textarea lt-min-height lt-min-height1" ${(bizStatus == "02")?string('placeholder="详情信息·"','')} ${(bizStatus != "02")?string("disabled","")}>${dissentDetail}</textarea>
                                                        <p class="textarea-text posi-abso font-size-12 text-lh1 cor-ccd0d9" id="dissentDetailRemark">0 / 1000字</p>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>

                                    </div>

                                </div>

                            </#if>

                            <#if bizStatus == "02" || bizStatus == "03" >
                                <#if nextPersonOrgid??>
                                    <div class="layui-col-xs12 mt60" id="selectNextPersonDiv">
                                        <p class="font-size-14 font-bold pl20 mb15">下一环节办理人</p>
                                        <div class="ml25 mr25">
                                            <div class="layui-col-xs12 pl15 pr15" id="selectNextPerson"></div>
                                        </div>
                                    </div>
                                </#if>
                            </#if>

                        </#if>
                    </div>
                </div>
                <div class="sn-btn text-align-c">
                    <#if bizStatus == "99">
                        <button type="button" class="layui-btn layui-btn-normal" id="save" onclick="saveRaftBtn();">保存草稿</button>
                        <button type="button" class="layui-btn layui-btn-normal" id="startWorkFlow" onclick="startWorkFlowBtn();">提交</button>
                    </#if>
                    <#if hasRejectBtn == true>
                        <button type="button" class="layui-btn layui-btn-normal" id="reject" onclick="rejectBtn(this);"
                            <#if bizStatus == "02">
                                unit-type="SSYH_ZFDW_ZY"
                            </#if>
                            <#if bizStatus == "03">
                                unit-type="shbzy"
                            </#if>
                            <#if bizStatus == "04">
                                unit-type="shbzy"
                            </#if>
                        >驳回</button>
                    </#if>
                    <#if bizStatus != "99" && bizStatus != "07" && bizStatus != "09" && bizStatus != "10">
                        <button type="button" class="layui-btn layui-btn-normal" onclick="commitWorkFlowBtn(this)"
                            <#if bizStatus == "01">
                                unit-type="SSYH_HYBM_ZY"
                            </#if>
                            <#if bizStatus == "02">
                                unit-type="SSYH_ZFDW_ZY"
                            </#if>
                            <#if bizStatus == "03">
                                unit-type="shbzy"
                            </#if>
                            <#if bizStatus == "04">
                                unit-type="shbzy"
                            </#if>
                        >${commitBtnName}</button>
                    </#if>
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

        layui.use(['form', 'table', 'laydate', 'laytpl', 'element', 'layer'], function () {

            var form = layui.form,
                table = layui.table,
                laydate = layui.laydate,
                laytpl = layui.laytpl,
                element = layui.element,
                layer = layui.layer;

            <#if bizStatus == "99">
                laydate.render({
                    elem: '#fbDateStr',
                    format: 'yyyy-MM-dd HH:mm:ss',
                    theme: 'rq1',
                    type: 'datetime',
                    btns: ['clear', 'confirm'],
                    trigger: 'click',
                });
            </#if>

            <#if bizStatus == "01">
                laydate.render({
                    elem: '#reDateStr',
                    format: 'yyyy-MM-dd HH:mm:ss',
                    theme: 'rq1',
                    type: 'datetime',
                    btns: ['clear', 'confirm'],
                    trigger: 'click',
                });
            </#if>

            initTextarea();

            $(window).on('click', function () {
                $('.scroll').getNiceScroll().resize();
            })

            form.on('checkbox(industrialCode)', function(data){
                if($(this).attr('title') == '其他') {
                    if(data.elem.checked) {
                        $('#industrialComment').removeAttr('disabled');
                    } else {
                        $('#industrialComment').attr('disabled', true).val('').html('');
                    }
                }
            });

            var $indusCheckboxs = $('.indus').find(':input[name$="industrialCode"]');
            $indusCheckboxs.each(function(index) {
                var title = $(this).attr('title');
                var $span = $('.indus').find('span:contains(\''+ title +'\')');
                if($span.length > 0) {
                    $span.parent().bind('click', function() {
                        if($span.parent().hasClass('layui-form-checked')) {
                            $(this).prop('checked', true);
                            if($span.text() == '其他') {
                                $('#industrialComment').removeAttr('disabled');
                            }
                        } else {
                            $(this).prop('checked', false);
                            if($span.text() == '其他') {
                                $('#industrialComment').attr('disabled', true).val('');
                            }
                        }
                    })
                }
            })

        })

        //草稿才加载
        <#if bizStatus == "99">

            <#-- 文书类型 -->
            AnoleApi.initListComboBox("letterTypeName", "letterType", '${letterTypeCode}', null, null, {
                ShowOptions : {
                    EnableToolbar : true
                }
            });

            AnoleApi.initOrgZtreeComboBox("fbDepartName", "fbDepartCode", function (regionCode, items){
                if(items!=undefined && items!=null && items.length>0){
                    var org = items[0];
                    if(org.orgCode!='-1'){
                        $("#fbDepartCode").val(org.orgCode);
                    }
                }
            },{
                ShowOptions: {
                    EnableToolbar: true
                }
            });

            <#-- 接收单位 -->
            $('#reUserName').wapSelect(
                '${orgId}',
                function(res){
                    if(res && res.length > 0) {
                        $('#reDepartCode').val(res[0].orgCode);
                        $('#reDepartNameDet').val(res[0].orgName);
                        $('#reUserId').val(res[0].userId);
                        $('#reUserName').val(res[0].userName);
                        $('#reUser').val(JSON.stringify(res));
                    }
                },
                {
                    multi: false,
                    layer:{
                        area:['500px','400px']
                    },
                    context:'${$COMPONENTS_DOMAIN}',
                    postParam:{
                        orgType:'0',
                        initValue: '${reUser}'
                    }
                }
            );
        </#if>

        <#if bizStatus == "01">

            if($('#reType').length > 0) {
                AnoleApi.initListComboBox("reTypeName", "reType", null, null, ["${reType}"], {
                    ShowOptions : {
                        EnableToolbar : true
                    },
                    DataSrc: [{"name":"按期回复", "value":"1"},{"name":"未按期回复", "value":"2"}]
                });
            }

            if($('#reDissentAgree').length > 0) {

                AnoleApi.initListComboBox(
                    "reDissentAgreeName",
                    "reDissentAgree",
                    null,
                    function(val, items){

                        if(val == '1') {

                            $('#reDissentDetail').removeAttr('disabled');
                            if($('#reDissentAgree').val() == '') {
                                $('#reDissentDetail').val('');
                            }

                            $('#reDissentDetail').parent().parent().removeClass('hide');

                            $('#chgTypeDiv').addClass('hide');
                            if($('#chgType').val() == '') {
                                $('#chgDetail').val('');
                            }

                            $('#indusChgAgreeDiv').addClass('hide');
                            if($('#indusChgAgree').val() == '') {
                                $('#indusChgDetail').val('');
                            }

                            $('#longActionAgreeDiv').addClass('hide');
                            if($('#longActionAgree').val() == '') {
                                $('#longActionDetail').val('');
                            }

                            $('#othClobDiv').addClass('hide');
                            $('#othClob').val('');

                        } else {

                            $('#reDissentDetail').attr('disabled', 'disabled').val('');

                            $('#reDissentDetail').parent().parent().addClass('hide');

                            if($('#chgTypeDiv').length > 0) {

                                $('#chgTypeDiv').removeClass('hide');

                                AnoleApi.initListComboBox(
                                    "chgTypeName",
                                    "chgType",
                                    "${chgTypesCode}",
                                    function(val, items){
                                        if(val != '4') {
                                            $('#chgDetail').removeAttr('disabled');
                                        } else {
                                            $('#chgDetail').attr('disabled', 'disabled').val('');
                                        }
                                    },
                                    ["${chgType}"],
                                    {
                                        ShowOptions: {
                                            EnableToolbar: true
                                        }
                                    }
                                );

                            }

                            if($('#indusChgAgreeDiv').length > 0) {

                                $('#indusChgAgreeDiv').removeClass('hide');

                                AnoleApi.initListComboBox(
                                    "indusChgAgreeName",
                                    "indusChgAgree",
                                    null,
                                    function(val, items){
                                        if(val == '1') {
                                            $('#indusChgDetail').removeAttr('disabled');
                                        } else {
                                            $('#indusChgDetail').attr('disabled', 'disabled').val('');
                                        }
                                    },
                                    ["${indusChgAgree}"],
                                    {
                                        ShowOptions: {
                                            EnableToolbar: true
                                        },
                                        DataSrc: [{"name": "是", "value": "1"}, {"name": "否", "value": "2"}]
                                    }
                                );

                            }

                            if($('#longActionAgreeDiv').length > 0) {

                                $('#longActionAgreeDiv').removeClass('hide');

                                AnoleApi.initListComboBox(
                                    "longActionAgreeName",
                                    "longActionAgree",
                                    null,
                                    function(val, items){
                                        if(val == '1') {
                                            $('#longActionDetail').removeAttr('disabled');
                                        } else {
                                            $('#longActionDetail').attr('disabled', 'disabled').val('');
                                        }
                                    },
                                    ["${longActionAgree}"],
                                    {
                                        ShowOptions: {
                                            EnableToolbar: true
                                        },
                                        DataSrc: [{"name": "是", "value": "1"}, {"name": "否", "value": "2"}]
                                    }
                                );

                            }

                            if($('#othClobDiv').length > 0) {

                                $('#othClobDiv').removeClass('hide');

                            }

                        }
                    },
                    ["${reDissentAgree}"],
                    {
                        ShowOptions: {
                            EnableToolbar: true
                        },
                        DataSrc: [{"name": "是", "value": "1"}, {"name": "否", "value": "2"}]
                    }
                );

            }

        </#if>

        <#if bizStatus == "02">

            <#if reDissentAgree == "2">
                $('#chgTypeDiv').removeClass('hide');
                $('#indusChgAgreeDiv').removeClass('hide');
                $('#longActionAgreeDiv').removeClass('hide');
                $('#othClobDiv').removeClass('hide');
            </#if>

            if($('#dissentAgree').length > 0) {

                AnoleApi.initListComboBox(
                    "dissentAgreeName",
                    "dissentAgree",
                    null,
                    function(val, items){

                    },
                    null,
                    {
                        ShowOptions: {
                            EnableToolbar: true
                        },
                        DataSrc: [{"name": "是", "value": "2"}, {"name": "否", "value": "1"}]
                    }
                );

            }

            initSelectNextPerson();

        </#if>

        <#if bizStatus == "03">

            <#if reDissentAgree == "2">
                $('#chgTypeDiv').removeClass('hide');
                $('#indusChgAgreeDiv').removeClass('hide');
                $('#longActionAgreeDiv').removeClass('hide');
                $('#othClobDiv').removeClass('hide');
            </#if>

            initSelectNextPerson();

        </#if>

        <#if bizStatus == "04">

            <#if reDissentAgree == "2">
                $('#chgTypeDiv').removeClass('hide');
                $('#indusChgAgreeDiv').removeClass('hide');
                $('#longActionAgreeDiv').removeClass('hide');
                $('#othClobDiv').removeClass('hide');
            </#if>
        </#if>

        initTextarea();

    }

    var initTextarea = function() {

        // textarea随着内容撑开高度
        $('textarea').each(function () {
            this.setAttribute('style', 'height:' + (this.scrollHeight) + 'px;overflow-y:hidden;');
        }).on('input propertychange', function () {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
            $('.scroll').getNiceScroll().resize();
        });

        $('textarea').each(function () {
            var targetId = $(this).attr('target-id');
            if(targetId && $('#' + targetId).length > 0) {
                var text = $(this).val();
                var len = LayuiComp.valdationCom.rule.checkStrLength(text);
                var maxLength = $(this).attr('maxlength');
                $('#' + targetId).html(len + ' / ' + maxLength + '字');
            }
        })

    }

    var initSelectNextPerson = function() {
        $('#selectNextPerson').html(
            '<div class="layui-form-item">'+
                '<label class="layui-form-label"><i>*</i>${nextPersonTypeName}：</label>'+
                '<div class="layui-input-block">' +
                    '<input id="nextReUserName" name="nextReUserName" class="layui-input" type="text" readonly/>' +
                    '<input id="nextUserStr" name="nextUserStr" class="layui-input" type="hidden" value=\'\'/>' +
                '</div>' +
            '</div>'
        );
        $('#nextReUserName').wapSelect(
            '${nextPersonOrgid}',
            function(res){
                if(res && res.length > 0) {
                    $('#nextReUserName').val(res[0].userName);
                    $('#nextUserStr').val(JSON.stringify(res));
                }
            },
            {
                multi: false,
                layer:{
                    area:['500px','400px']
                },
                context:'${$COMPONENTS_DOMAIN}'
            }
        );
    }

    var getFormData = function(isReject) {

        var serializeArray = $('#thoForm').serializeArray();
        var paramArray = serializeArray;

        <#if bizStatus == "99">

            if(!LayuiComp.valdationCom.verification($('#thoForm'))){
                return null;
            }

            var $industrialCodes = $('input[type=checkbox][name$="industrialCode"]:checked');

            if($industrialCodes.length == 0) {
                layer.tips('请勾选行业、领域名称', '.indusTip',{tips: 1});
                return null;
            }

            $industrialCodes = $('input[type=checkbox][name$="industrialCode"][title="其他"]:checked');

            if($industrialCodes.length == 1 && $('#industrialComment').val().trim().length == 0) {
                layer.tips('请填写补充说明', '#industrialComment',{tips: 1});
                return null;
            }

            paramArray = [];
            for(var i=0, index = 0; i<serializeArray.length; i++) {
                var item = serializeArray[i];
                if(item.name.indexOf('industrialCode') < 0) {
                    paramArray.push({name: item.name, value: item.value});
                } else {
                    paramArray.push({name: 'induss['+ index +'].industrialCode', value: item.value});
                    index++;
                }
            }

        </#if>

        <#if bizStatus == "01">

            if(!LayuiComp.valdationCom.verification($('#thoForm'))){
                return null;
            }

            if($('#reDissentAgree').val() == 1) {

                if($('#reDissentDetail').val() == '') {
                    layer.tips('请填写异议详情', '#reDissentDetail',{tips: 1});
                    return null;
                }

            } else {

                if($('#chgType').val() == '') {
                    layer.tips('是否开展整改', '#chgTypeName',{tips: 1});
                    return null;
                }

                if($('#chgType').val() != '4' && $('#chgDetail').val() == '') {
                    layer.tips('请填写整改说明', '#chgDetail',{tips: 1});
                    return null;
                }

                if($('#indusChgAgree').val() == '') {
                    layer.tips('是否开展行业治理', '#indusChgAgreeName',{tips: 1});
                    return null;
                }

                if($('#indusChgAgree').val() == '1' && $('#indusChgDetail').val() == '') {
                    layer.tips('请填写行业治理情况', '#indusChgDetail',{tips: 1});
                    return null;
                }

                if($('#longActionAgree').val() == '') {
                    layer.tips('是否建立长效机制', '#longActionAgreeName',{tips: 1});
                    return null;
                }

                if($('#longActionAgree').val() == '1' && $('#longActionDetail').val() == '') {
                    layer.tips('请填写建立长效机制情况', '#longActionDetail',{tips: 1});
                    return null;
                }

                if($('#othClob').val() == '') {
                    layer.tips('请填写其他需要说明的情况', '#othClob',{tips: 1});
                    return null;
                }

            }

        </#if>

        <#if bizStatus == "02">

            if(!LayuiComp.valdationCom.verification($('#thoForm'))){
                return null;
            }

            if($('#dissentAgree').length > 0) {
                if($('#dissentAgree').val() == '1' && $('#dissentAgreeName').val() == '') {
                    layer.tips('请填写异议', '#dissentAgreeName',{tips: 1});
                    return null;
                }
            }

        </#if>

        <#if bizStatus == "03"></#if>

        <#if bizStatus == "04"></#if>

        if(!isReject) {
            if($('#nextUserStr').length > 0) {
                if($('#nextUserStr').val() == '') {
                    layer.tips('请填写选择${nextPersonTypeName}', '#nextReUserName',{tips: 1});
                    return null;
                }
            }
        }

        return paramArray;

    }

    var startWorkFlowBtn = function() {

        var data = getFormData(false);

        if(data == null) return;

        layer.confirm("确定要提交吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {

            var submitLoad = layer.load(1);//打开遮罩

            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/startWorkFlow.json',
                data: data,
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        if ('${onlyView}' == 'onlyView'){
                            gmMsgClient.sent(parent,'db7px3',1);
                        }

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

    var saveRaftBtn = function() {

        var data = getFormData(false);

        if(data == null) return;

        layer.confirm("确定要保存草稿吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {

            var submitLoad = layer.load(1);//打开遮罩

            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/saveRaft.json',
                data: data,
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        if ('${onlyView}' == 'onlyView'){
                            gmMsgClient.sent(parent,'db7px3',1);
                        }
                        parent.layer.closeAll();
                        parent.layer.msg("保存草稿成功", {skin: 'layui-layer-hui'});
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

        });

    }

    var rejectBtn = function(_this) {

        var type = $(_this).attr('unit-type');

        var data = getFormData(true);

        if(data == null) return;

        layer.confirm("确定要驳回吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {

            var submitLoad = layer.load(1);//打开遮罩

            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/reject/'+ type +'.json',
                data: data,
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        if ('${onlyView}' == 'onlyView'){
                            gmMsgClient.sent(parent,'db7px3',1);
                        }

                        parent.layer.closeAll();
                        parent.layer.msg("提交成功", {skin: 'layui-layer-hui'});
                        reloadParentTable();
                    } else {
                        layer.msg(response.message, {skin: 'layui-layer-hui'});
                    }
                },
                error: function(response) {
                    layer.msg(response.message, {skin: 'layui-layer-hui'});
                },
                complete : function() {
                    layer.close(submitLoad);
                }
            });

        });

    }

    var commitWorkFlowBtn = function(_this) {

        var type = $(_this).attr('unit-type');

        var data = getFormData(false);

        if(data == null) return;

        layer.confirm("确定要${commitBtnName}吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {

            var submitLoad = layer.load(1);//打开遮罩

            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/commitWorkFlow/'+ type +'.json',
                data: data,
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        if ('${onlyView}' == 'onlyView'){
                            gmMsgClient.sent(parent,'db7px3',1);
                        }

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

    var textareaRemark = function(_this, length) {
        var text = $(_this).val();
        var len = LayuiComp.valdationCom.rule.checkStrLength(text);
        $(_this).parent().find('p').html(len + ' / ' + length + '字');
    }

    var reloadParentTable = function() {
        parent.layui.table.reload('data1', {page: {curr: $(".layui-laypage-em").next().html()}});
        parent.layer.closeAll();
    }

    var closeBtn = function() {
        parent.layer.closeAll();
    }

</script>

<script type="text/javascript" charset="utf-8">

    $(function() {

        init();

        if ('${onlyView}' == 'onlyView'){
           $("#closeP").hide()
        }
    })

</script>