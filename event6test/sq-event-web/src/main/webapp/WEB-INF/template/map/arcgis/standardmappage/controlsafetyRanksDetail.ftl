<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安监队伍概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:225px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;width:100%;">
			               <b style="cursor:default" title="<#if prvetionTeam.name?exists>${prvetionTeam.name}</#if>">
			               	<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showControlsafetyRanksDetail()">
								<#if prvetionTeam.name?exists><#if prvetionTeam.name?length gt 21 >${prvetionTeam.name[0..21]}...<#else>${prvetionTeam.name}</#if></#if>&nbsp;
							</a>
			               </b>
		               </p>
		               <p>所属网格：
		               	<#if gridNames??>
		               	<span class="FontDarkBlue" title="${gridNames}">
		               		${gridNames}
		               	</span>
		               	</#if>&nbsp;
		               	</p>
		               	<#if prvetionTeam.bizType=='1'>
			               <p>组织类型：
			               	<#if prvetionTeam.teamTypeStr??>
				               	<span class="FontDarkBlue" title="${prvetionTeam.teamTypeStr}">
				               		${prvetionTeam.teamTypeStr}
				               	</span>
				            <#else>
				            	<span class="FontDarkBlue">
				               		未知
				               	</span>
			               	</#if>&nbsp;	
		               	<#elseif prvetionTeam.bizType=='0'>
			               	<p>机构类型：
			               	<#if prvetionTeam.teamTypeStr??>
				               	<span class="FontDarkBlue" title="${prvetionTeam.teamTypeStr}">
				               		${prvetionTeam.teamTypeStr}
				               	</span>
				            <#else>
				            	<span class="FontDarkBlue">
				               		未知
				               	</span>
			               	</#if>&nbsp;
		               	<#elseif prvetionTeam.bizType=='3'>
		               			<p>队伍类型：
			               	<#if prvetionTeam.teamTypeStr??>
				               	<span class="FontDarkBlue" title="${prvetionTeam.teamTypeStr}">
				               		${prvetionTeam.teamTypeStr}
				               	</span>
				            <#else>
				            	<span class="FontDarkBlue">
				               		未知
				               	</span>
			               	</#if>&nbsp;
		               	</#if>
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
		               
		               <#if prvetionTeam.bizType!='0'>
		               <p>负责人：
		               	<#if prvetionTeam.manager??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.manager}">
			               		${prvetionTeam.manager}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
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
		               </#if>
		          </li>
		      </ul>
		      <div class="clear"></div>
		      
		</div>			
<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
<script type="text/javascript">
var teamId = '${prvetionTeam.teamId!''}';
var teamHashId = '${prvetionTeam.hashId!''}';  
var title= '${prvetionTeam.name!''}';  
var bizType= '${prvetionTeam.bizType!''}';  
function showControlsafetyRanksDetail() {
	//var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	//var url = sq_zzgrid_url +'/zzgl/grid/newPrvetionTeam/new/detail.jhtml?showClose=1&teamId='+id+'&bizType='+bizType+'';
	var url ='${SQ_GMIS_URL}/gmis/prvetionTeam/detail.jhtml?showClose=1&bizType='+bizType+'&hashId='+teamHashId+'&teamId='+teamId;
	window.parent.showMaxJqueryWindow(title,url,610,280);	
}
</script>
</html>
