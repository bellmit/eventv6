<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>低保户、贫困户、政策保障户概要</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ImageView.ftl" />

<style type="text/css">
	.p {widht:250px;}
</style>

</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body>
	<div class="con LyWatch" style="height:195px;overflow:auto;">
        	<ul>
                <li style="width:280px;">
                    <p title="<#if homeInfo.familyAddress?exists>${homeInfo.familyAddress}</#if>">
                    <span class="FontDarkBlue" style="font-size:14px;">
                    <b>
                    <#if homeInfo.familyAddress??>
                    	<#if (homeInfo.familyAddress)?length lt 18 >
                    		${homeInfo.familyAddress}
                    	<#else>
                    		${homeInfo.familyAddress[0..17]}..
                    	</#if>
                    </#if>
                    </span></b></p>
                   <p>户主姓名：<span class="FontDarkBlue" style="widht:250px;"><#if homeInfo.houseHoldName??>${homeInfo.houseHoldName}</#if></span></p>
                   <p>人口数：<span class="FontDarkBlue"><#if popTotal??>${popTotal?c}（人）</#if></span></p>
                   <p>致贫原因：
                   		<#if homeInfo.poorReasonMCN??>
		                   <span class="FontDarkBlue p" title="${homeInfo.poorReasonMCN}">
			                   		<#if (homeInfo.poorReasonMCN)?length lt 18 >
			                    		${homeInfo.poorReasonMCN}
			                    	<#else>
			                    		${homeInfo.poorReasonMCN[0..17]}..
			                    	</#if>
		                    </span>
	                   </#if>
                    </p>
                    <p>管理单位：
	                    <#if homeInfo.gridName??>
	                    	<span class="FontDarkBlue p" title="${homeInfo.gridName}">
		                    	<#if (homeInfo.gridName)?length lt 20 >
		                    		${homeInfo.gridName}
		                    	<#else>
		                    		${homeInfo.gridName[0..19]}..
		                    	</#if>
	                    	</span>
	                    </#if>
	                </p>
	                <p>帮扶责任人联系电话：
	                	<span class="FontDarkBlue">
							<#if homeHelpPhone??>
								${homeHelpPhone}
							</#if>
						</span>
					</p>
                </li>
            </ul>
        </div>
</body>
</html>