<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ImageView.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:213px;overflow:auto;">
		      <ul>
			      <li style="width:120px; height:145px;">
			      	<div id = "imgDiv" onclick='ffcs_viewImg("${fireResource.id!''}")'>
			      	<#if fireResource.photoPath??>
					    <img id="preview" src="${RESOURSE_SERVER_PATH}${fireResource.photoPath}"  style="width:120px; height:145px;margin-top: 5px;margin-bottom: 15px;"/>
					<#else>
					    <img id="preview"  src="${rc.getContextPath()}/images/notbuilding.gif" border=0 style="width:120px; height:145px;margin-top: 5px;margin-bottom: 15px;"/>
					</#if>
					<div>
	              </li>
		      	
		          <li style="width:180px;">
		               <p class="FontDarkBlue" style="font-size:14px;width:190px;">
		               	<#if fireResource.operateStatus!='1'>
		               		<img src="${rc.getContextPath()}/ui/images/yichang.png" title="异常"/>
		               	</#if>
			               <b style="cursor:default" title="<#if fireResource.name?exists>${fireResource.name}</#if>">
			               	<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showNewSourceDetail('<#if fireResource.name?exists>${fireResource.name}</#if>','<#if fireResource.id?exists>${fireResource.id}</#if>')">
								<#if fireResource.name?exists><#if fireResource.name?length gt 21 >${fireResource.name[0..21]}<#else>${fireResource.name}</#if></#if>&nbsp;
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
		               	<#if fireResource.address??>
			               	<span class="FontDarkBlue" title="${fireResource.address}">
			               		${fireResource.address}
			               	</span>
		               	<#elseif  fireResource.buildingName??>
		               		<span class="FontDarkBlue">
			               		${fireResource.buildingName}<#if fireResource.floor??>第${fireResource.floor}层</#if>
			               	</span>
			            <#else>
			            	<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <#if fireResource.catalog?? && fireResource.catalog!='3' && fireResource.catalog!='4' && fireResource.catalog!='5'>
		               <p>消防栓形式：
		               	<#if fireResource.fireTypeLbl??>
			               	<span class="FontDarkBlue" title="${fireResource.fireTypeLbl}">
			               		${fireResource.fireTypeLbl}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>管网形式：
		               	<#if fireResource.pipeTypeLbl??>
			               	<span class="FontDarkBlue" title="${fireResource.pipeTypeLbl}">
			               		${fireResource.pipeTypeLbl}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>口径：
		               	<#if fireResource.caliber??>
			               	<span class="FontDarkBlue" title="${fireResource.caliber}">
			               		${fireResource.caliber}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               </#if>
		               <p>运行状态：
		               	<#if fireResource.operateStatusLbl??>
			               	<span class="FontDarkBlue" title="${fireResource.operateStatusLbl}">
			               		${fireResource.operateStatusLbl}
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
$(function(){
	var fieldId  = "${fireResource.id!''}";
	var paths = "${RESOURSE_SERVER_PATH!''}${fireResource.photoPath!''}";
	parent.ImageViewApi.initImageView(fieldId,paths);
});

function ffcs_viewImg(fieldId){console.log(fieldId);
	parent.ffcs_viewImg(fieldId,0);
}

function showNewSourceDetail(title,id) {
	var catalog = ${fireResource.catalog};
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/fireGrid/fireResource/detail.jhtml?id="+id;
	
	if (catalog=='5') {
		url = sq_zzgrid_url+"/zzgl/fireGrid/firePool/map/detail.jhtml?id="+id;
	}
	
	window.parent.showMaxJqueryWindow(title,url,639,320);	
}
</script>

</html>
