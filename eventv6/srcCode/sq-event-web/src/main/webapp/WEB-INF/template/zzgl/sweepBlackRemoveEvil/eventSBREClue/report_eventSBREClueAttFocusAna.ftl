<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <title>打击重点统计分析报表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/listSet.ftl" />
<#include "/component/AnoleDate.ftl">
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
    <style type="text/css">
        .inp1 {width:150px;}
        table {
            border-collapse: collapse;
        }
        th{
            background-color: #EEEEEE;
            border: solid;
            border-width: 0 1px 1px 0;
            border-color: #ccc;
            border-style: solid;
            height: 38px;
            font-size: 14px;
        }
        tr td{
            height: 38px;
            font-size: 13px;
            text-align: center;
            border: solid;
            border-color: #ccc;
            border-width: 0 1px 1px 0;
        }
    </style>
</head>
<body >
<table style="width: 100%;height: 100%;">
    <div>
        <thead>
            <tr>
                <th name="attachFocusName" style="width: 40%">涉及打击重点</th>
                <th name="count" style="width: 30%">线索量</th>
                <th name="percent" style="width: 30%">占总线索比重</th>
            </tr>
        </thead>
    </div>

    <div>
        <tbody>
        <#if dataDictList?? && (dataDictList?size > 0)>
            <#list dataDictList as dict>
                <#if dict.isLeaf == "1">
                    <tr>
                        <td>${dict.dictName!}</td>
                        <td>
                            <#if dataMap?? && (dataMap?size > 0)>
                                <#list dataMap as data>
                                    <#if data.typeVal == dict.dictGeneralCode>
                                        <a style="cursor: pointer" onclick="getJurisdictionList('${(data.typeVal)!}')">
                                        ${data.count!}
                                        </a>
                                    </#if>
                                </#list>
                            </#if>
                        </td>
                        <td>
                            <#if dataMap?? && (dataMap?size > 0)>
                                <#list dataMap as data>
                                    <#if data.typeVal == dict.dictGeneralCode>
                                    ${data.percent!}
                                    </#if>
                                </#list>
                            </#if>
                        </td>
                    </tr>
                <#else >
                    <tr>
                        <td>
                            <table id="${dict.dictCode}" style="width: 100%">
                                <tr>
                                    <td id="${dict.dictId}"  style="width: 50%">
                                    ${dict.dictName!}
                                    </td>
                                </tr>
                                <#if childDataDictList?? && (childDataDictList?size > 0)>
                                    <#list childDataDictList as childDict>
                                        <#if childDict.dictPcode == dict.dictCode>
                                            <tr>
                                                <td style="width: 50%">
                                                ${childDict.dictName!}
                                                </td>
                                            </tr>
                                        </#if>
                                    </#list>
                                </#if>
                            </table>
                        </td>
                        <td>
                            <table style="width: 100%">
                                <#if childDataDictList?? && (childDataDictList?size > 0)>
                                    <#list childDataDictList as childDict>
                                        <#if childDict.dictPcode == dict.dictCode>
                                            <tr>
                                                <td style="width: 50%">
                                                    <#if dataMap?? && (dataMap?size > 0)>
                                                        <#list dataMap as data>
                                                            <#if data.typeVal == childDict.dictGeneralCode>
                                                                <a style="cursor: pointer" onclick="getJurisdictionList('${(data.typeVal)!}')">
                                                                ${data.count!}
                                                                </a>
                                                            </#if>
                                                        </#list>
                                                    </#if>
                                                </td>
                                            </tr>
                                        </#if>
                                    </#list>
                                </#if>
                            </table>
                        </td>
                        <td>
                            <table style="width: 100%">
                                <#if childDataDictList?? && (childDataDictList?size > 0)>
                                    <#list childDataDictList as childDict>
                                        <#if childDict.dictPcode == dict.dictCode>
                                            <tr>
                                                <td style="width: 50%">
                                                    <#if dataMap?? && (dataMap?size > 0)>
                                                        <#list dataMap as data>
                                                            <#if data.typeVal == childDict.dictGeneralCode>
                                                            ${data.percent!}
                                                            </#if>
                                                        </#list>
                                                    </#if>
                                                </td>
                                            </tr>
                                        </#if>
                                    </#list>
                                </#if>
                            </table>
                        </td>
                    </tr>
                </#if>
            </#list>
        </#if>
        </tbody>
    </div>
</table>
</body>
<script type="text/javascript">
    $(function() {
        /*动态改变有子项列的高度*/
        <#if dataDictList?? && (dataDictList?size > 0)>
            <#list dataDictList as dict>
                <#if dict.isLeaf == "0">
                    /*table行数*/
                    var trNum = $("#${dict.dictCode}").find("tr").length;
                    $("#${dict.dictId}").attr("rowspan",trNum);
                </#if>
            </#list>
        </#if>
    });
    //辖区所有列表
    function getJurisdictionList(typeVal) {

        if(isNotBlankString(typeVal)){
            var url = '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=4&typeVal='+typeVal;
            openJqueryWindowByParams({
                title			: "辖区线索信息",
                targetUrl		: url
            });
        }

    }
</script>
</html>
