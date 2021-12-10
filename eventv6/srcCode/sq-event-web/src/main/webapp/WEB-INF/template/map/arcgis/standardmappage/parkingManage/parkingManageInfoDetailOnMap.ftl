<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>两车管理概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:190px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;width:270px;">
		               	<b style="cursor:default;" title="<#if parkingManage.onRoad?exists>${parkingManage.onRoad!''}</#if>">
		               		<#if parkingManage.onRoad?exists>
		               			<#if parkingManage.onRoad?length gt 21 >
		               				${parkingManage.onRoad[0..20]}...
		               			<#else>
		               				${parkingManage.onRoad}
		               			</#if>
		               		</#if>&nbsp;
		               	</b>
		               </p>
		               <p>停放地点 ：
		               	<span class="FontDarkBlue">${parkingManage.placeAddr!''}</span>
		               </p>
		               <p>类别：<span class="FontDarkBlue">
               				<#if parkingManage.type??>
							  	<#if parkingManage.type=="01">
							  		代管点
								</#if>
								<#if parkingManage.type=="02">
									非代管点
								</#if>
								<#if parkingManage.type=="03">
									物业管理
								</#if>
								<#if parkingManage.type=="04">
									单位自管
								</#if>
							</#if>
	               			</span>
		               </p>
		               <p>管理员：<span class="FontDarkBlue">${parkingManage.manager!''}</span></p>
		               <p>联系电话：<span class="FontDarkBlue">${parkingManage.managerTelephone!''}</span></p>
		               <p>管理单位：<span class="FontDarkBlue">${parkingManage.manageUnit!''}</span></p>
		               <p>联系单位电话：<span class="FontDarkBlue">${parkingManage.unitTelephone!''}</span></p>
		          </li>
		      </ul>
		      <div class="clear"></div>
		</div>			
</body>
</html>
