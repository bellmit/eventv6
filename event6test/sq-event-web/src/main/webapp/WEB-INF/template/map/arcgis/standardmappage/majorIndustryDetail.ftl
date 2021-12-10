<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重大产业</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body>
		<div class="con LyWatch" style="height:240px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;width:auto">
			               <b style="cursor:default" title="<#if industryinfo.industryName?exists>${industryinfo.industryName}</#if>">
			               	<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);">
								<#if industryinfo.industryName?exists><#if industryinfo.industryName?length gt 21 >${industryinfo.industryName[0..21]}...<#else>${industryinfo.industryName}</#if></#if>&nbsp;
							</a>
			               </b>
		               </p>
		               <p>产业名称：
		               	<#if industryinfo.industryName??>
		               	<span class="FontDarkBlue" title="${industryinfo.industryName}">
		               		${industryinfo.industryName}
		               	</span>
		               	</#if>&nbsp;
		               	</p>
		               <p>产业概况：
		               	<#if industryinfo.industrySurve??>
			               	<span class="FontDarkBlue" title="${policeInfo.industrySurve}">
			               		<#if industryinfo.industrySurve?length gt 500 >
			               			${industryinfo.industrySurve[0..500]}...
			               		<#else>
			               			${industryinfo.industrySurve}
			               		</#if>
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>		                              
		          </li>
		       
				  <li style="margin-bottom:1px;" class="clear">
                      <a style="margin-left:90px;" href="javascript:show('cyjj','产业简介')"><img src="${uiDomain!''}/images/cyjj.png"></a>
                      <a style="margin-left:30px;" href="javascript:showVedio()"><img src="${uiDomain!''}/images/spjk.png"></a>
                      <a style="margin-left:30px;" href="javascript:show('xmgl','项目管理')"><img src="${uiDomain!''}/images/xmgl.png"></a>
				  </li>
                  <li style="margin-top:0px;">
                      <a style="margin-left:97px;" href="javascript:show('cyjj','产业简介')">产业简介</a>
                      <a style="margin-left:38px;" href="javascript:showVedio()">视频监控</a>
                      <a style="margin-left:43px;" href="javascript:show('xmgl','项目管理')">项目管理</a>
				  </li>
		      </ul>
		      <div class="clear"></div>
		       
		</div>	
	
</body>
<script type="text/javascript">


    function show(type,title) {  
        var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/detail.jhtml?industryId=${industryinfo.industryId!}&type="+type;
         window.parent.showMaxJqueryWindow(title,url,600,360);
    }
    function showVedio(){
  	  window.parent.showGlobalPlayBox(${industryinfo.monitorSetting!});
    }

</script>

</html>
