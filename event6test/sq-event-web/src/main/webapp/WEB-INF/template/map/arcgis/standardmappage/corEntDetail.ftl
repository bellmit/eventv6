<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>企业-概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		/*width:120px; 限制宽度导致折行 @YangCQ */
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
		vertical-align:top;
	}
	.LyWatch span{width:60%;}
</style>

</head>
<body>
<div>
	<div id="content-dd" class="UnitInfo" style="border-bottom:none;">
        	<ul>
				<#if record.corName??>
	        		<li style="width:360px">
	        			<p class="FontDarkBlue" style="font-size:14px;font-weight:bold;cursor:default;width:100%;" title="${record.corName}">
		        				<#if (record.corName)?length lt 21 >
		                    		${record.corName}
		                    	<#else>
		                    		${record.corName[0..20]}
		                    	</#if>
	        			</p>
	        		</li>
				</#if>
				<#if record.corAddr??>
					<li style="width:360px;cursor:default;" title="${record.corAddr}">
						<p>
							<#if (record.corAddr)?length lt 25 >
	                    		${record.corAddr}
	                    	<#else>
	                    		${record.corAddr[0..24]}
	                    	</#if>
						</p>
					</li>
				</#if>
				<li style="width:150px;">注册资金：<span class="FontDarkBlue">${record.registeredCapital!''}</span></li>
				<li style="width:150px;">企业类型：<span class="FontDarkBlue">${record.categoryLabel!''}</span></li>
				<li style="width:336px;">法人：<span class="FontDarkBlue"><#if record.representativeName??>${record.representativeName}</#if></span></li>
				<li style="width:150px;">负责人：<span class="FontDarkBlue">${record.owner!''}</span></li>
				<li style="width:150px;">负责人电话：<span class="FontDarkBlue">${record.ownerTel!''}</span></li>
				<li style="width:330px;">经营范围：<span class="FontDarkBlue" style="width:80%;"><#if record.busScope??>${record.busScope}</#if></span></li>
			</ul>
        	
        	
           <div class="clear"></div>
        </div>
	</div>			
	   
</body>
</html>
