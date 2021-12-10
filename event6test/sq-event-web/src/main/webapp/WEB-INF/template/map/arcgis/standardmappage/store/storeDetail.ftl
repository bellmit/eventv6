<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>门店-详情</title>
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
                <li style="width:160px;">
                	<p><b><a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showDetail('${storeInfo.storeName!''}','${storeInfo.storeId?c}')">
                	<#if storeInfo.storeName??>
                    	<#if (storeInfo.storeName)?length lt 11 >
                    		${storeInfo.storeName}
                    	<#else>
                    		${storeInfo.storeName[0..10]}..
                    	</#if>
                    </#if>
                	</a></b></p>
                    <p title="<#if storeInfo.storeAddr?exists>${storeInfo.storeAddr}</#if>">
                    <span>
                    <#if storeInfo.storeAddr??>
                    	<#if (storeInfo.storeAddr)?length lt 35 >
                    		${storeInfo.storeAddr}
                    	<#else>
                    		${storeInfo.storeAddr[0..34]}..
                    	</#if>
                    </#if>
                    </span></p>
                    <p>所在网格：<span class="FontDarkBlue"><#if storeInfo.gridName??>${storeInfo.gridName}</#if></span></p>
                    <p>所在楼宇：<span class="FontDarkBlue"><#if storeInfo.buildingName??>${storeInfo.buildingName}</#if></span></p>
                    <p>店主：<span class="FontDarkBlue">
                    <#if storeInfo.shopkeeper??>${storeInfo.shopkeeper}</#if>
                    	（<#if storeInfo.shopkeeperTel??>${storeInfo.shopkeeperTel}<#else>无</#if>）
                    </span></p>
                </li>
            </ul>
        </div>
	
</body>
<script type="text/javascript">
function showDetail(title,storeId) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = "${gmisDomain!''}/gmis/storeManage/detail.jhtml?storeId="+storeId+"&showClose=false";
	//window.parent.showDetailLigerUI(title,url,850,480)
	window.parent.showMaxJqueryWindow(title,url,600,400);	
}
</script>
</html>