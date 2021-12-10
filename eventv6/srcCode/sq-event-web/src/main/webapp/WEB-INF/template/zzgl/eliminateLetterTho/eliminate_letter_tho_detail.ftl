<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>处理环节（流程）</title>
    <#include "/component/layuiCommon/common_view_files_1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/jx-sanshuyihan/pages/blue/css/handle-flow.css"/>
    <script type="text/javascript" charset="utf-8" src="${uiDomain!''}/js/jquery.form.js"></script>
    <#if isBigScreen == '1'>
        <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/jx-sanshuyihan/pages/blue/css/ssyh-xiangq.css" />
    </#if>
</head>

<style type="text/css">
    #close {
        float: right;
        height: 100%;
        line-height: 100%;
        text-align: center;
        font-size: 13px;
        background: #e9ecf0;
        border-top: 1px solid #cccccc;
        padding: 0px 14px;
        cursor: pointer;
        -webkit-transition: all 0.5s ease-in-out;
        -moz-transition: all 0.5s ease-in-out;
        -ms-transition: all 0.5s ease-in-out;
        -o-transition: all 0.5s ease-in-out;
        transition: all 0.35s ease-in-out;
        -wbkit-box-shadow: 1px -1px 3px rgba(204, 204, 204, 0.3);
        box-shadow: 1px -1px 3px rgba(204, 204, 204, 0.3);
        margin-right: 2px;
    }
</style>

<body class="xiu-body">

    <div class="container-linkflow">

    <div class="link-flow-nav">
        <ul class="link-flow-nav-list clearfix">
            <li class="list active-flow">文书详情</li>
            <li class="list <#if instanceId == -99>hide</#if>" >处理流程</li>
        </ul>
        <input type="button" value="&nbsp;关&nbsp;闭&nbsp;" id="close">
    </div>

    <!--处理环节（流程）主体区域-->
    <div class="link-flow-warp">
        <!--处理环节（流程）主体-->
        <div class="link-flow-box">
            <div class="link-flow-dchot h-o">
                <div id="content-d" class="printing-warp pb30">
                    <div class="printing-btn" id="_topDiv">
                        <!--原来的关闭按钮-->
                    </div>
                    <div class="printing-title">
                        <h1 class="p-title">扫黑除恶专项斗争“三书一函”情况填报表</h1>
                    </div>
                    <p class="font-size-15 cor-f00 ml10 mt20">注意：此表格中，数据项名称底色为灰色并标记红色星号的项为必填项</p>
                    <div class="table-wrap-ext1 ml10 mt10 mr10">
                        <table class="table-ext1 width-p100">
                            <colgroup>
                                <col width="5%">
                                <col width="8%">
                                <col width="10%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="10%">
                                <col width="10%">
                                <col width="10%">
                                <col width="10%">
                                <col>
                            </colgroup>
                            <tbody>
                            <tr>
                                <td colspan="14">
                                    <p class="line-height-40 font-size-18 cor-000">（一）基本信息</p>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">1</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">“三书一函“编码：</p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${thtNo}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-f00 ml10">(模板中不需填写，导入时会自动分配)</p>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="14">
                                    <p class="line-height-40 font-size-18 cor-000">
                                        （二）制发情况
                                        <sapn class="cor-f00">（制发单位填写）</sapn>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">1</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>文书类型：</p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${letterTypeName}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333"></p>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">2</p>
                                </td>
                                <td colspan="2" rowspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>制发单位：
                                    </p>
                                </td>
                                <td colspan="11">
                                    <p class="font-size-15 cor-f00 ml15">(如制发单位为中央和国家机关，请在省（区市）位置选择“全国”)</p>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${createProvince}</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 ml15">省（区、市）</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${createCity}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">市（地、州、盟）</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${createCounty}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">县（市、区、旗）</p>
                                </td>
                                <td colspan="1" class="bg-f5">
                                    <p class="font-size-15 cor-333 ml10">${fbDepartNameDet}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">(单位名称)</p>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">3</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>发出时间：
                                    </p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${fbDateYear}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">年</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${fbDateMonth}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">月</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${fbDateDate}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">日</p>
                                </td>
                                <td colspan="5" rowspan="2">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">4</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>文号：
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${letterNo}</p>
                                </td>
                            </tr>

                            <tr class="height-60">
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">5</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>案件名称（100字以内）：
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${caseName}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-f00 ml15">例：XX、XX等X人涉嫌XX罪案</p>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">6</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>案件编码：
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${caseNo}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-f00 ml15">
                                        注：指发出检察建议书、司法建议书、公安提示函的案件在本平台中的23位案件编码（监察建议书可不填写）
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">7</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 font-bold text-align-r mr10">行业、领域</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r mr10">
                                        <span class="cor-f00">* </span>行业、领域名称</br>（可多选）：
                                    </p>
                                </td>
                                <td colspan="11" class="pt5 pb5">
                                    <#list indus as item>
                                        <#if item.index == 0 || (item.index + 1) % 4 == 0>
                                            <ul class="radio-ext1-wrap">
                                        </#if>
                                        <li>
                                            <div class="checkbox-wrap" style="width: 250px;">
                                                <input class="checkbox-ext1" type="checkbox"
                                                       name="color-input-red" ${(item.isChecked)?string('checked', '')}>
                                                <label>
                                                    <span class="checkbox-ext1-s"></span>
                                                    ${item.name}
                                                </label>
                                            </div>
                                        </li>
                                        <#if item.index != 0 && ((item.index + 1) % 4 == 0 || (item.index + 1) == indus?size)>
                                            </ul>
                                        </#if>
                                    </#list>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 font-bold text-align-r mr10 pt10 pb10">
                                        补充说明
                                        <br>
                                        （选择“其他”时填写；1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333" readonly id="test">${industrialComment}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">8</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 font-bold text-align-r mr10">
                                        <p class="font-size-15 cor-333 font-bold text-align-r mr10">
                                            <span class="cor-f00">* </span>
                                            文书内容
                                            <br>
                                            （3000字以内）：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${letterContentClob}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="14">
                                    <p class="line-height-40 font-size-18 cor-000">
                                        （三）接收情况
                                        <sapn class="cor-f00">（接收单位填写）</sapn>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">1</p>
                                </td>
                                <td colspan="2" rowspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>接收单位：
                                    </p>
                                </td>
                                <td colspan="11">
                                    <p class="font-size-15 cor-f00 ml15">(如接收单位为中央和国家机关，请在省（区、市）位置选择“全国”)</p>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${reProvince}</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 ml15">省（区、市）</p>
                                </td>
                                <td colspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${reCity}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">市（地、州、盟）</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr10">${reCounty}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">县（市、区、旗）</p>
                                </td>
                                <td colspan="1" class="bg-f5">
                                    <p class="font-size-15 cor-333 ml10">${reDepartNameDet}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml10">(单位名称)</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">2</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r">
                                        <span class="cor-f00">* </span>接收时间：
                                    </p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${reDateYear}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">年</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${reDateMonth}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">月</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 text-align-r mr15">${reDateDate}</p>
                                </td>
                                <td colspan="1">
                                    <p class="font-size-15 cor-333 ml15">日</p>
                                </td>
                                <td colspan="5" rowspan="2">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">3</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">回复情况</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>回复情况：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${reType}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r mr10">
                                        <span class="cor-f00">* </span>
                                        回复详情
                                        <br>
                                        （1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${reDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">4</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">是否提出异议</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>
                                            是否提出异议：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${reDissentAgree}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r pt15 pb15 mr10">
                                        <span class="cor-f00">* </span>
                                        异议详情
                                        <br>
                                        （选择“是”需填写；1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${reDissentDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="14">
                                    <p class="line-height-40 font-size-18 cor-000">
                                        （四）异议处理情况
                                        <sapn class="cor-f00">（制发单位填写）</sapn>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">1</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">是否同意异议</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>
                                            是否同意异议：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${dissentAgree}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r pt20 pb20 mr10">
                                        详细信息
                                        <br>
                                        (1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${dissentDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="14">
                                    <p class="line-height-40 font-size-18 cor-000">
                                        （五）整改落实情况
                                        <sapn class="cor-f00">（接收单位填写）</sapn>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">1</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">是否开展整改</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>
                                            是否开展整改：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${chgType}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r pt20 pb20 mr10">
                                        整改详情
                                        <br>
                                        （未整改时不需填写；1000字以内）
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${chgDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">2</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">是否开展行业治理</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>
                                            是否开展行业治理：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${indusChgAgree}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r pt20 pb20 mr10">
                                        开展行业治理情况 （选择“是”需填 写；1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${indusChgDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-c">3</p>
                                </td>
                                <td rowspan="2">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">是否建立长效机制</p>
                                </td>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333">
                                        <p class="font-size-15 cor-333 font-bold text-align-r">
                                            <span class="cor-f00">* </span>
                                            是否建立长效机制：
                                        </p>
                                    </p>
                                </td>
                                <td colspan="6">
                                    <p class="font-size-15 cor-333 ml15">${longActionAgree}</p>
                                </td>
                                <td colspan="5">
                                    <p class="font-size-15 cor-333 ml15"></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="bg-f5">
                                    <p class="font-size-15 cor-333 font-bold text-align-r pt20 pb20 mr10">
                                        建立长效机制情况 （选择“是”需填 写；1000字以内）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${longActionDetail}</textarea>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <p class="font-size-15 cor-333 text-align-c">4</p>
                                </td>
                                <td colspan="2" class="bg-f5">
                                    <p class="font-size-15 cor-333 text-align-r mr5 font-bold">
                                        <span class="cor-f00">* </span>
                                        其他需要说明的情况
                                        </br>
                                        （2000字以内；</br>没有请填写“无”）：
                                    </p>
                                </td>
                                <td colspan="11" class="pl15 pt10 pb10 pr10">
                                    <textarea class="font-size-15 cor-333 h-o" readonly>${othClob}</textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <p class="font-size-15 cor-333 font-bold mt15">备注：</p>
                        <p class="font-size-15 cor-333 font-bold">1. 该表格由制发单位填写后及时录入，同级扫黑办应协助制发单位督促接收单位填写其余内容并及时更新。</p>
                        <p class="font-size-15 cor-333 font-bold">2. 同一工作流程的所有信息需全部填写方能成功提交。</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="link-flow-box">
            <div class="link-flow-legend">
                <ul class="legend-list clearfix">
                    <li class="leg">
                        <div class="leg-spot aj-ks aj-ks-yellow">
                            <div class="aj-ks1"></div>
                            <div class="aj-ks2"></div>
                            <div class="aj-ks3"></div>
                        </div>
                        <span class="p">当前环节</span>
                    </li>
                    <li class="leg">
                        <div class="leg-spot aj-ks aj-ks-gray">
                            <div class="aj-ks1"></div>
                            <div class="aj-ks2"></div>
                        </div>
                        <span class="p">历史环节</span>
                    </li>
                </ul>
            </div>
            <div class="link-flow-titlebar clearfix">
                <div class="wbar w1">办理环节</div>
                <div class="wbar w2">办理人/办理时间</div>
            </div>

            <!--流程图-->
            <div class="link-flow-main">
                <div class="height-p100">
                    <div class="flow-line"></div>
                    <div class="link-flow-cont h-o">
                        <ul class="link-flowbox-items">
                            <#if allLinks??>
                                <#assign index = 0>
                                <#list allLinks as l>
                                    <#if l.TASK_CODE == 'end' >
                                        <li>
                                            <h5 class="flow-grid-txt c-yellow flow-w">${l.TASK_NAME}</h5>
                                            <div class="flow-w">
                                                <div class="leg-spot aj-ks aj-ks-yellow">
                                                    <div class="aj-ks1"></div>
                                                    <div class="aj-ks2"></div>
                                                    <div class="aj-ks3"></div>
                                                </div>
                                            </div>
                                            <div class="flow-w"></div>
                                        </li>
                                        <#assign index = 1>
                                    <#elseif index == 0>
                                        <li>
                                            <h5 class="flow-grid-txt c-yellow flow-w">${l.ORG_NAME!}<i class="icon-cs-down"></i></h5>
                                            <div class="flow-w">
                                                <div class="leg-spot aj-ks aj-ks-yellow">
                                                    <div class="aj-ks1"></div>
                                                    <div class="aj-ks2"></div>
                                                    <div class="aj-ks3"></div>
                                                </div>
                                            </div>
                                            <div class="flow-w">
                                                <div class="onflow-lit">
                                                    <div class="flow-w banl-items">
                                                        <p class="flow-d-text">
                                                            <strong class="txt-sys0">${l.TRANSACTOR_NAME!}</strong>
                                                            <#if l.INTER_TIME??>
                                                                <span class="txt-span c1-sblue">耗时</span>
                                                                <strong class="txt-sys0">${l.INTER_TIME!}</strong>
                                                                <br>
                                                            </#if>
                                                            <#if l.END_TIME??>
                                                        <p class="txt-sys1">办结时间：<span>${l.END_TIME}</span></p>
                                                        </#if>
                                                        </p>
                                                    </div>
                                                    <#if l.REMARKS??>
                                                        <div class="flow-w banl-items bl-bob">
                                                            <p class="aj_items_t1 aj_items_t_blue">${l.REMARKS}</p>
                                                        </div>
                                                    </#if>
                                                </div>
                                            </div>
                                        </li>
                                        <#assign index = 1>
                                    <#else>
                                        <li>
                                            <h5 class="flow-w flow-grid-txt">${l.ORG_NAME!}<i class="icon-cs-down"></i>
                                            </h5>
                                            <div class="flow-w">
                                                <div class="leg-spot aj-ks aj-ks-gray">
                                                    <div class="aj-ks1"></div>
                                                    <div class="aj-ks2"></div>
                                                </div>
                                            </div>
                                            <div class="flow-w">
                                                <div class="onflow-lit">
                                                    <div class="flow-w banl-items">
                                                        <p class="flow-d-text">
                                                            <strong class="txt-sys0">${l.TRANSACTOR_NAME!}</strong>
                                                            <#if l.INTER_TIME??>
                                                                <span class="txt-span c1-sblue">耗时</span>
                                                                <strong class="txt-sys0">${l.INTER_TIME!}</strong>
                                                                <br>
                                                            </#if>
                                                        <#if l.END_TIME??>
                                                            <p class="txt-sys1">办结时间：<span>${l.END_TIME}</span></p>
                                                        </#if>
                                                        </p>
                                                    </div>
                                                    <#if l.REMARKS??>
                                                        <div class="flow-w banl-items bl-bob">
                                                            <p class="aj_items_t1 aj_items_t_blue">${l.REMARKS}</p>
                                                        </div>
                                                    </#if>
                                                </div>
                                            </div>
                                        </li>
                                    </#if>

                                </#list>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>

<script type="text/javascript" charset="utf-8">

    $(function () {

        init();

    });

</script>

<script type="text/javascript" charset="utf-8">

    var init = function() {

        layui.use(['form', 'table', 'laydate', 'laytpl', 'element', 'layer'], function () {

            var form = layui.form,
                table = layui.table,
                laydate = layui.laydate,
                laytpl = layui.laytpl,
                element = layui.element,
                layer = layui.layer;

            $('textarea').each(function () {
                this.setAttribute('style', 'width: 100%; overflow-y:auto; resize: none;');
                let scrollHeight = $(this)[0].scrollHeight;
                $(this).height(scrollHeight);
                // this.setAttribute('style', 'min-height:250px; width: 100%; overflow-y:auto; resize: none;');
            });

            rebuildScroll('textarea',{cursorwidth:0});

        });

        rebuildScroll('.link-flow-dchot.h-o');

        var $conLinkflowH, $linkFlowNavH, $linkFlowWarpH, $flowLegendH, $flowTitlebarH;
        $(window).on('load resize', function () {
            //计算主区域高度
            $conLinkflowH = $('.container-linkflow').outerHeight(true);
            $linkFlowNavH = $('.link-flow-nav').outerHeight(true);
            $('.link-flow-warp').height($conLinkflowH - $linkFlowNavH);
            //计算轴高度
            $linkFlowWarpH = $('.link-flow-warp').outerHeight(true);
            $flowLegendH = $('.link-flow-legend').outerHeight(true);
            $flowTitlebarH = $('.link-flow-titlebar').outerHeight(true);
            $('.link-flow-main').height($linkFlowWarpH - $flowLegendH - $flowTitlebarH);
        });

        //导航切换
        var switchBar;
        $('.link-flow-warp .link-flow-box').eq(0).show();
        $('.link-flow-nav-list').on('click', 'li', function () {
            $(this).addClass('active-flow').siblings().removeClass('active-flow');
            switchBar = $(this).index();
            $('.link-flow-warp .link-flow-box').eq(switchBar).show().siblings().hide();
            $(".h-o").niceScroll().resize();
        });

        //关闭按钮
        $('#close').bind('click', function () {
            parent.layer.closeAll();
        })

    }

    //滚动条美化
    var rebuildScroll = function(ele,opts) {
        opts = opts||{};
        var defaultOpts = {
            cursorcolor: "#a6a6a6",
            cursoropacitymax: 1,
            touchbehavior: false,
            cursorwidth: "6px",
            cursorborder: "0",
            cursorborderradius: "3px",
            autohidemode: true
        };
        opts = $.extend(true,defaultOpts,opts);
        $(ele).niceScroll(opts);
    }

</script>