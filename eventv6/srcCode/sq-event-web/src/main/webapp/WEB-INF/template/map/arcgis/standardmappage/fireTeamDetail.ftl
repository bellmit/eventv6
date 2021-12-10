<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消防队信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ImageView.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:224px;overflow:auto;">
		      <ul>
		          <li style="width:120px; height:145px;">
			      	<div id = "imgDiv" onclick='ffcs_viewImg("${fireTeam.fireTeamId!''}")'>
			      	<#if fireTeam.photoPath??>
					    <img id="preview" src="${RESOURSE_SERVER_PATH}${fireTeam.photoPath}"  style="width:120px; height:145px;margin-top: 5px;margin-bottom: 15px;"/>
					<#else>
					    <img id="preview"  src="${rc.getContextPath()}/images/notbuilding.gif" border=0 style="width:120px; height:145px;margin-top: 5px;margin-bottom: 15px;"/>
					</#if>
					<div>
	              </li>
		          
		          <li style="width:220px;">
		               <p class="FontDarkBlue" style="font-size:14px;">
			               <b style="cursor:default" title="<#if fireTeam.name?exists>${fireTeam.name}</#if>">
			               	<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showDetail('<#if fireTeam.name?exists>${fireTeam.name}</#if>','<#if fireTeam.fireTeamId?exists>${fireTeam.fireTeamId}</#if>')">
								<#if fireTeam.name?exists><#if fireTeam.name?length gt 21 >${fireTeam.name[0..21]}...<#else>${fireTeam.name}</#if></#if>&nbsp;
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
		               <p>地址：
		               	<#if fireTeam.addr??>
			               	<span class="FontDarkBlue" title="${fireTeam.addr}">
			               		${fireTeam.addr}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <#if fireTeam.catalog?? && fireTeam.catalog!='3' && fireTeam.catalog!='4'>
		               <p>队伍编号：
		               	<#if fireTeam.teamNo??>
			               	<span class="FontDarkBlue" title="${fireTeam.teamNo}">
			               		${fireTeam.teamNo}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>形式类别：
		               	<#if fireTeam.catalogLbl??>
			               	<span class="FontDarkBlue" title="${fireTeam.catalogLbl}">
			               		${fireTeam.catalogLbl}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>联系人：
		               	<#if fireTeam.contact??>
			               	<span class="FontDarkBlue" title="${fireTeam.contact}">
			               		${fireTeam.contact}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               </#if>
		               <p>联系电话：
		               	<#if fireTeam.ph??>
			               	<span class="FontDarkBlue" title="${fireTeam.ph}">
			               		${fireTeam.ph}
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
$(function() {
	var fieldId  = "${fireTeam.fireTeamId!''}";
	var paths = "${RESOURSE_SERVER_PATH!''}${fireTeam.photoPath!''}";
	parent.ImageViewApi.initImageView(fieldId, paths);
});

function ffcs_viewImg(fieldId) {
	console.log(fieldId);
	parent.ffcs_viewImg(fieldId);
}

function showDetail(title,id) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/fireGrid/fireTeam/detail.jhtml?fireTeamId="+id;
	window.parent.showMaxJqueryWindow("详情", url, 700, 430);	
}
</script>

</html>
