<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />


</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: #fff;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="miId" value="${miId!''}" />
	<#if residentDetail?exists>
		<div class="con PeopleWatch">
        	<div class="PeopleInfo" style="height:100%;"> 
                <ul>
                    <li class="pic">
                    	<#if residentDetail.photoUrl??>
							<img src="${RESOURSE_SERVER_PATH}${residentDetail.photoUrl}" />
						<#else>
							<img id="peopleImg"  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" />
						</#if>
                    	
                    </li>
                    <li style="width:250px;">
                    	<#--<p style="padding-left:0;"><span style="cursor:pointer;" onclick="showPopDetail('${residentDetail.name}','${residentDetail.ciRsId?c}')"><#if residentDetail.name?exists>${residentDetail.name}</#if>&nbsp;</span>-->
                        <p style="padding-left:0;"><span><#if residentDetail.name?exists>${residentDetail.name}</#if>&nbsp;</span>
                    	<#if residentDetail.genderCN?exists>
							${residentDetail.genderCN}
						</#if>&nbsp;
						<#if residentDetail.ethnicCN?exists>${residentDetail.ethnicCN}</#if>&nbsp;
							<#if residentDetail.birthday?exists><#if residentDetail.birthday?is_string>${residentDetail.birthday}<#else>${residentDetail.birthday?string("yyyy-MM-dd")}</#if>出生</#if></p>
						
						
 						<#if gridNames?exists>
                        	<span style="color:#000;line-height:24px;font-size:12px;">网格：</span>
                    	
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
	                    		${gridNames}
							</span><br/>
                        </#if>                       	
						
                        <#if residentDetail.residence?exists>
                        	<span style="color:#000;line-height:24px;font-size:12px;">户籍地址：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;" title="${residentDetail.residence}">
								<#if (residentDetail.residence)?length lt 24 >
		                    		${residentDetail.residence}
		                    	<#else>
		                    		${residentDetail.residence[0..23]}..
		                    	</#if>
							</span><br/>
                        </#if>	
                        <#if buildingName?exists>
                        	<span style="color:#000;line-height:24px;font-size:12px;">现居住地址：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
		                    	${buildingName}
							</span><br/>
                        </#if>
                        
                        <div style="height:24px;">
	                        <#if residentDetail.identityCard?exists>
	                        	<p class="PeopleID fl">
	                        		${residentDetail.identityCard}
	                        	</p>
	                        </#if>	
	                        <#if residentDetail.residentMobile?exists>
	                        	<p class="PeoplePhone fl" title="${residentDetail.residentMobile}">
	                        		${residentDetail.residentMobile}
	                        	</p>
	                        </#if>
                        </div>
                        
						<p class="PeopleTags" style="width:220px">
							<a name="all_kinds_info" style="color:#0075a9;" href="javascript:showDetail('${residentDetail.ciRsId?c}','吸毒(${residentDetail.name})');" style="color:blue">吸毒人员详情</a>
						</p>

                        <p style="width:220px">
                            吸毒人员现状：<#if drugRecord?? && drugRecord.dtypeLabel??>${drugRecord.dtypeLabel!''}</#if>
                        </p>
						
						<#if visitRecordCount??>
							<p class="VisitRecord">
								<a id="visitRecord" name="visitRecord" style="color:#0075a9;" href="javascript:showVisitRecord('${residentDetail.ciRsId?c}');">走访记录(${visitRecordCount?c})</a>
							</p>
						</#if>
						
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
		
		
    <#else>
    <table cellpadding="0" cellspacing="0" border="0"  class="searchList-2">
    	<tr style="height: 185px"><td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty" >未查到相关数据！</td></tr>
    </table>
    </#if>
	
</body>
<script type="text/javascript">


var isCross;
<#if isCross??>
	isCross = '${isCross}';
</#if>


function showPopDetail(title,ciRsId) {
	if (isCross != undefined) { // 跨域
		var url = "${POPULATION_URL}/cirs/viewResident.jhtml?menu=1&ciRsId="+ciRsId;
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+800+","+370+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var population_url =  window.parent.document.getElementById("POPULATION_URL").value;
		var url = population_url+"/cirs/viewResident.jhtml?menu=1&ciRsId="+ciRsId;
	
		window.parent.showMaxJqueryWindow(title,url,800,370);
	}
}

function showDetail(ciRsId,id,name) {
	var url = "${SQ_ZZGRID_URL}/zzgl/map/data/residentionfo/typeDetail/"+ciRsId+".jhtml?id="+id+"&gridId="+${gridId?c};
	addPersonLi(ciRsId,name,url);
}

function showVisitRecord(ciRsId) {
	var height = 500;
	var width = 900;
	var title = "走访记录";
	var url = "${SQ_ZZGRID_URL}/zzgl/crowd/visitRecord/showVisitMsg.jhtml?ciRsId="+ciRsId;
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,null,null);
	}
}


function showEmergencyWay(){
	var height = 410;
	var width = 890;
	var title = "应急预案";
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/showEmergencyWay.jhtml?catalogId=40000012&subClassPcode=B177";
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,width,height);
	}
}

var LINXIA_FLAG = "<#if LINXIA_FLAG??>${LINXIA_FLAG!''}</#if>";
function addPersonLi(rsId,liName,url) {
	var src = "";
	var height = 230;
	var width = 635;
	var title = liName;
	url = '${SQ_ZZGRID_URL}/zzgl/crowd/drug2/detail2.jhtml?ciRsId='+rsId;
	width = null;
	height = null;
	src = url;

	// 隐藏div
	$("#person_div div").each(function(){
		$(this).css("display", "none");
	});
	
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,width,height);
	}
}
function showBuild(buildingId){
	var url = "${SQ_ZZGRID_URL}/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+buildingId;
	var title = "楼宇详细信息";
	var width = 948;
	var height = 405;
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,width,height);
	}
}
</script>
</html>
