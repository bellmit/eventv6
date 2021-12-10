<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>消防安全管理员信息</title>
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
		<div class="con LyWatch" style="height:240px;overflow:auto;">
        	<ul>
        		<li style="width:300px"><span class="FontDarkBlue" style="font-size:14px;font-weight:bold"><#if safetyPersonManage.name??>${safetyPersonManage.name}</#if></span></li>
				<#if safetyPersonManage.corPlaceName??><li style="width:300px"><span>${safetyPersonManage.corPlaceName}&nbsp;</span></li></#if>
				
				<li style="width:135px;">职务：<span class="FontDarkBlue">
                    		<#if safetyPersonManage.personType??>
                        		<#if safetyPersonManage.personType == '0'>
                        			安全负责人
                        		<#elseif safetyPersonManage.personType == '1'>
                        			安全管理员
                        		<#elseif safetyPersonManage.personType == '2'>
                        			特种作业人员
                        		<#elseif safetyPersonManage.personType == '3'>
                        			仓库管理员
                        		<#elseif safetyPersonManage.personType == '4'>
                        			食堂操作员
                        		<#elseif safetyPersonManage.personType == '5'>
                        			消控室管理员
								</#if>
							</#if>
					   </span></li>
				<li style="width:135px;">所在网格：<span class="FontDarkBlue"><#if safetyPersonManage.gridName??>${safetyPersonManage.gridName}</#if></span></li>
				<li style="width:135px;">联系电话：<span class="FontDarkBlue"><#if safetyPersonManage.residentMobile??>${safetyPersonManage.residentMobile}</#if></span></li>
				<li style="width:155px;">内部考核是否合格：
               		     <span class="FontDarkBlue">	
               		     	<#if safetyPersonManage.internalAssessment??>
                			<#if safetyPersonManage.internalAssessment == '0'>
                				不合格
                			<#elseif safetyPersonManage.internalAssessment == '1'>
                				合格
                			</#if>
						</#if>
						</span></li>
				<li style="width:135px;">资格证书：
					<span class="FontDarkBlue">
						<#if safetyPersonManage.isCert??>
                			<#if safetyPersonManage.isCert == '0'>
                				无
                			<#elseif safetyPersonManage.isCert == '1'>
                				有
                			</#if>
						</#if>
					</span>
				</li>
				<li style="width:135px;">是否有内部培训：
					<span class="FontDarkBlue">
						<#if safetyPersonManage.internalTraining??>
                			<#if safetyPersonManage.internalTraining == '0'>
                				无
                			<#elseif safetyPersonManage.internalTraining == '1'>
                				有
                			</#if>
						</#if>
					</span>
				</li>
				<li style="width:235px;">身份证号：
					<span class="FontDarkBlue">
						<#if safetyPersonManage.identityCard??>${safetyPersonManage.identityCard}</#if>
					</span>
				</li>
			</ul>
           <div class="clear"></div>
        </div>
	</div>			
</body>
</html>
