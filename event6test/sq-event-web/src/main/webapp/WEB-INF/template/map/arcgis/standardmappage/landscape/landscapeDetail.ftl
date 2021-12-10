<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>园林绿化-详情</title>
<#if Session.gmisDomain?exists>
	<#assign gmisDomain = Session.gmisDomain>
</#if>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body>
	<div class="con LyWatch" style="height:225px;overflow:auto;">
        	<ul>
                <li style="width:280px;">
                	<p><b><a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showDetail('${landscapeManage.name_!''}','${landscapeManage.gardenId?c}')">
                	${landscapeManage.name_!''}
                	</a></b></p>
                    <p>所在网格：<span class="FontDarkBlue"><#if landscapeManage.gridName??>${landscapeManage.gridName}</#if></span></p>
                    <p title="<#if landscapeManage.placeAddr?exists>${landscapeManage.placeAddr}</#if>">
                                        地址：
                    <span>
                    <#if landscapeManage.placeAddr??>
                    	<#if (landscapeManage.placeAddr)?length lt 35 >
                    		${landscapeManage.placeAddr}
                    	<#else>
                    		${landscapeManage.placeAddr[0..34]}..
                    	</#if>
                    </#if>
                    </span></p>
                    <p>类型：<span class="FontDarkBlue"><#if landscapeManage.typeStr??>${landscapeManage.typeStr}</#if></span></p>
                    <p>主要种植物品种：<span class="FontDarkBlue">
                    <#if landscapeManage.plants??>
                    	<#if (landscapeManage.plants)?length lt 35 >
                    		${landscapeManage.plants}
                    	<#else>
                    		${landscapeManage.plants[0..34]}..
                    	</#if>
                    </#if>
                    </span>
                    </p>
                </li>
            </ul>
        </div>
</body>
<script type="text/javascript">
function showDetail(title,id) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = "${gmisDomain!''}/gmis/landscapeManage/detail.jhtml?gardenId="+id+"&showClose=false";
	//window.parent.showDetailLigerUI(title,url,850,480)
	window.parent.showMaxJqueryWindow(title,url,600,400);	
}
</script>
</html>