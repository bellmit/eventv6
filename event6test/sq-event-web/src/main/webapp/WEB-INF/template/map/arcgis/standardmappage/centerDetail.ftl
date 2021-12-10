<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>概要信息-综治中心</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
<style>
body, html{overflow:auto;}
</style>
</head>
<body>
		<div class="con LyWatch" style="overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="width:100%;font-size:14px;">
			               <b style="cursor:default" title="<#if prvetionTeam.name?exists>${prvetionTeam.name}</#if>">
								<#if prvetionTeam.name?exists><#if prvetionTeam.name?length gt 31 >${prvetionTeam.name[0..31]}...<#else>${prvetionTeam.name}</#if></#if>&nbsp;
			               </b>
		               </p>
		               <p>所属区域：
		               	<#if gridNames??>
		               	<span class="FontDarkBlue" title="${gridNames}">
		               		${gridNames}
		               	</span>
		               	</#if>&nbsp;
		               	</p>
		               	<p>
		               	机构层级：
		               	<#if prvetionTeam.orgLevelStr??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.orgLevelStr}">
			               		${prvetionTeam.orgLevelStr}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>负责人：
		               	<#if prvetionTeam.manager??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.manager}">
			               		${prvetionTeam.manager}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>
		               </p>
		               <p>
		               	负责人电话：
		               	<#if prvetionTeam.managerTel??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.managerTel}">
			               		${prvetionTeam.managerTel}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		          </li>
		      </ul>
		      <div class="clear"></div>
		      
		</div>			
</body>
<script type="text/javascript">
var teamId = '${prvetionTeam.teamId!''}';
var teamHashId = '${prvetionTeam.hashId!''}';  
var title= '${prvetionTeam.name!''}';  
var bizType= '${prvetionTeam.bizType!''}';
function showControlsafetyRanksDetail() {
	var url ='${SQ_GMIS_URL}/gmis/center/detail.jhtml?showClose=1&bizType='+bizType+'&hashId='+teamHashId+'&teamId='+teamId;
	window.parent.showMaxJqueryWindow(title,url,710,300);		
}
</script>
</html>
