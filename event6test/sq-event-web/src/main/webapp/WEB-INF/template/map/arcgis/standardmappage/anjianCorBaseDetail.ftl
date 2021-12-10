<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>安监企业-概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		width:120px;
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
	}
</style>

</head>
<body>
	<div class="NorMapOpenDiv">
		<div class="con LyWatch" style="height:220px;overflow:auto;">
        	<ul>
        		<li style="width:300px"><span class="FontDarkBlue" style="font-size:14px;font-weight:bold"><#if record.corName??>${record.corName}</#if></span></li>
				<#if record.corAddr??><li style="width:300px"><span>${record.corAddr}&nbsp;</span></li></#if>
				
				<li style="width:135px;">企业类型：<span class="FontDarkBlue">
                    		<#if corTypeDC??>
								<#list corTypeDC as l>
								     <#if record.corType??>
										<#if (l.COLUMN_VALUE==record.corType)>${l.COLUMN_VALUE_REMARK}</#if>
								    </#if>
								</#list>
							 </#if>
						   </span></li>
				<li style="width:135px;">法人代表：<span class="FontDarkBlue"><#if record.representativeName??>${record.representativeName}</#if></span></li>
				<li style="width:135px;">联系电话：<span class="FontDarkBlue"><#if record.telephone??>${record.telephone}</#if>&nbsp;</span></li>
				<li style="width:135px;">行业分类：
               		     <span class="FontDarkBlue">		
               				<#if categoryDC??>
								<#list categoryDC as l>
									<#if record.category??>
										<#if (l.COLUMN_VALUE==record.category)>${l.COLUMN_VALUE_REMARK}&nbsp;</#if>
									</#if>
								</#list>
							 </#if>
						</span></li>
				<li style="width:300px;">经营范围：<span class="FontDarkBlue"><#if record.busScope??>${record.busScope}</#if>&nbsp;</span></li>
			</ul>
        	
        	
           <div class="clear"></div>
        </div>
	</div>			
	   
</body>
</html>
