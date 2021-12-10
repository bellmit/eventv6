<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>守重企业-详情</title>
<#if Session.gmisDomain?exists>
    <#assign gmisDomain = Session.gmisDomain>
</#if>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
<div class="con LyWatch" style="height:225px;">
    <ul>
        <li class="pic">
        <#if attachment??>
            <img src="${RESOURSE_SERVER_PATH}${attachment.filePath}" border=0 width="120px;" height="145px;" />
        </#if>
        </li>
        <li style="width:180px;">
            <p style="width: 160px;"><b>
            <#if enterprise.enterpriseName??>
                <#if (enterprise.enterpriseName)?length lt 15>
                ${enterprise.enterpriseName}
                <#else>
                ${enterprise.enterpriseName[0..10]}..
                </#if>
            </#if>
            </b></p>
            <p>所在网格：<span class="FontDarkBlue"><#if enterprise.gridName??>${enterprise.gridName}</#if></span></p>
            <p>企业地址：<span class="FontDarkBlue"  style="width: 200px;"><#if enterprise.enterpriseAddress??>${enterprise.enterpriseAddress}</#if></span></p>
            <p>评定时间：<span class="FontDarkBlue"><#if enterprise.evaluationTime??>${(enterprise.evaluationTime)?string('yyyy-MM-dd')}</#if></span></p>
        </li>
    </ul>
</div>

</body>
<script type="text/javascript">
    function showDetail(title,enterpriseId) {
        var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
        var url="${rc.getContextPath()}/event/zhsq/szzg/zgFaithfulEnterprise/detail.jhtml?id="+enterpriseId+"&showClose=false";
        //var url = "http://gd.fjsq.org:8301/event/zhsq/szzg/zgFaithfulEnterprise/detail.jhtml?id="+enterpriseId+"&showClose=false";

        window.parent.showMaxJqueryWindow(title,url,600,350);
    }
</script>
</html>