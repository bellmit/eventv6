<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重点场所管理信息-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="NorMapOpenDiv">	
		<div class="con LyWatch" style="width:340px;height:220px;overflow:auto;">
        	<ul>
                <li>
                	<p class="FontDarkBlue" style="font-size:14px;width:260px;"><b title="<#if zzSmallPlaces.placeName??>${zzSmallPlaces.placeName}</#if>">
                	<#if zzSmallPlaces.placeName??>
                		<#if (zzSmallPlaces.placeName)?length lt 15 >
                    		${zzSmallPlaces.placeName}
                    	<#else>
                    		${zzSmallPlaces.placeName[0..14]}..
                    	</#if>
                	<#else>&nbsp;</#if>
                	</b></p>
                    <p>所属网格：<span class="FontDarkBlue"><#if zzSmallPlaces.gridName??>${zzSmallPlaces.gridName}<#else>&nbsp;</#if></span></p> 
                    <p>场所类型：<span class="FontDarkBlue">
						<#if placeCataDD?? && zzSmallPlaces.placeCata??>
							<#list placeCataDD as placeCata1>
					           <#if placeCata1.dictGeneralCode==zzSmallPlaces.placeCata>
					              ${placeCata1.dictName!''}
					           </#if>
							</#list>
						<#else>&nbsp;
						</#if>
					&nbsp;</span></p>
                    <p>场所地址：<span class="FontDarkBlue" title="<#if zzSmallPlaces.placeAddr??>${zzSmallPlaces.placeAddr}</#if>">
                    <#if zzSmallPlaces.placeAddr??>
                    	<#if (zzSmallPlaces.placeAddr)?length lt 40 >
                    		${zzSmallPlaces.placeAddr}
                    	<#else>
                    		${zzSmallPlaces.placeAddr[0..39]}..
                    	</#if>
                    <#else>&nbsp;</#if>
                    </span></p>
                	<p>负责&nbsp;人：<span class="FontDarkBlue"><#if zzSmallPlaces.ceief??>${zzSmallPlaces.ceief}<#else>&nbsp;</#if></span></p>
                    <p>联系电话：<span class="FontDarkBlue"><#if zzSmallPlaces.tel??>${zzSmallPlaces.tel}<#else>&nbsp;</#if></span></p>
                    <p>备&nbsp;&nbsp;注：<span class="FontDarkBlue" title="<#if zzSmallPlaces.desc??>${zzSmallPlaces.desc}</#if>">
                    <#if zzSmallPlaces.desc??>
                    	<#if (zzSmallPlaces.desc)?length lt 40 >
                    		${zzSmallPlaces.desc}
                    	<#else>
                    		${zzSmallPlaces.desc[0..39]}..
                    	</#if>
                    <#else>&nbsp;</#if>
                    </span></p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
	</div>
</body>
</html>