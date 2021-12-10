<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:225px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="width:100%;">
			               <b style="cursor:default" title="<#if prvetionTeam.name?exists>${prvetionTeam.name}</#if>">
			               	<a class="FontDarkBlue" href="javascript:void(0);" onclick="showControlsafetyRanksDetail('<#if prvetionTeam.name?exists>${prvetionTeam.name}</#if>','<#if prvetionTeam.teamId?exists>${prvetionTeam.teamId}</#if>','${prvetionTeam.bizType!''}')">
								<#if prvetionTeam.name?exists><#if prvetionTeam.name?length gt 11 >${prvetionTeam.name[0..11]}<#else>${prvetionTeam.name}</#if></#if>
							</a>
			               </b>
		               </p>
		               <p>所属网格：
		               	<span class="FontDarkBlue" title="${gridNames}">
		               		<#if gridNames??>${gridNames}</#if>
		               	</span>
		               	</p>
		               	<p>组织类型：
		               	<#if prvetionTeam.teamTypeStr??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.teamTypeStr}">
			               		${prvetionTeam.teamTypeStr}
			               	</span>
			            <#else>
			            	<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>	
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
		               <p>成员人数：
		               		<span class="FontDarkBlue" title="查看成员">
			               		<a style="text-decoration:underline;" href="javascript:void(0);" onclick="showMember();">${prvetionTeam.staffNum!''}</a> 人
			               	</span>
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
		               	负责人电话：
		               	<#if prvetionTeam.managerTel??>
			               	<span class="FontDarkBlue" title="${prvetionTeam.managerTel}">
			               		${prvetionTeam.managerTel}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>
		               </p>
		          </li>
		      </ul>
		      <div class="clear"></div>
		</div>			
<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
<script type="text/javascript">

var teamId = '${prvetionTeam.teamId!''}';
var teamHashId = '${prvetionTeam.hashId!''}';

function showControlsafetyRanksDetail(title,id,bizType) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url +'/zzgl/grid/newPrvetionTeam/new/detail.jhtml?showClose=1&teamId='+id+'&bizType='+bizType+'';
	window.parent.showMaxJqueryWindow(title,url,610,280);	
}
function showMember(){
	var url = '${GMIS_PATH}/gmis/teamMembers/listMember.jhtml?teamHashId='+teamHashId;
// 	var url = 'http://gd.fjsq.org:8880/sq-gmis-web/gmis/teamMembers/listMember.jhtml?teamId='+teamId;
	window.parent.showMaxJqueryWindow("成员列表",url,600, 380);
}
</script>
</html>
