<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼宇-详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ImageView.ftl" />
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body>
	<div class="con LyWatch" style="height:250px;overflow:auto;">
        	<ul>
            	<li class="pic">
            		<a href="#">
	            		<div id = "imgDiv" onclick='ffcs_viewImg("${record.buildingId?c}")'>
					      	<#if record.buildingExteriorFigure??>
							    <img src="${RESOURSE_SERVER_PATH}${record.buildingExteriorFigure}" border=0 width="120px;" height="145px;" />
							</#if>
						<div>
					</a>
            	</li>
                <li style="width:178px;">
                	<p><b><a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showBuildDetail('${record.buildingName!''}','${record.buildingId?c}')">${record.buildingName!''}</a></b></p>
                    <#if record.standardAddress?exists>
                    	<p title="<#if record.standardAddress?exists>${record.standardAddress}</#if>">
	                    <span>
	                    <#if record.standardAddress??>
	                    	<#if (record.standardAddress)?length lt 35 >
	                    		${record.standardAddress}
	                    	<#else>
	                    		${record.standardAddress[0..34]}..
	                    	</#if>
	                    </#if>
	                    </span></p>
	                <#else>
	                	<p title="<#if record.buildingAddress?exists>${record.buildingAddress}</#if>">
	                    <span>
	                    <#if record.buildingAddress??>
	                    	<#if (record.buildingAddress)?length lt 35 >
	                    		${record.buildingAddress}
	                    	<#else>
	                    		${record.buildingAddress[0..34]}..
	                    	</#if>
	                    </#if>
	                    </span></p>
                    </#if>
                    <p>楼层总数：<span class="FontDarkBlue"><#if record.buildingFloor??>${record.buildingFloor?c}</#if></span></p>
                    <p>建筑年份：<span class="FontDarkBlue"><#if record.buildingYear??>${record.buildingYear?c}</#if></span></p>
                    <p>使用性质：<span class="FontDarkBlue">
                    <#if record.useNatureLabel??>
                    	<#if (record.useNatureLabel)?length lt 11 >
                    		${record.useNatureLabel}
                    	<#else>
                    		${record.useNatureLabel[0..10]}..
                    	</#if>
                    </#if>
                    </span></p>
                </li>
            </ul>
            <div class="clear"></div>
            <div style="padding:0 10px 10px 10px; line-height:24px;">
				<p>管理单位：
                    <#if record.managementCompany??>
                    	<span class="FontDarkBlue" title="${record.managementCompany}">
                    	<#if (record.managementCompany)?length lt 30 >
                    		${record.managementCompany}
                    	<#else>
                    		${record.managementCompany[0..29]}..
                    	</#if>
                    	</span>
                    </#if>
                    </p>
                <p>联系电话：<span class="FontDarkBlue">
								<#if record.adminPhone??>
									<#list record.adminPhone?split(";;") as val>
										${val}<br/>
									</#list>
									<#else>
								</#if>
								</span>
								</p>
            </div>
        </div>
	
	
</body>
<script type="text/javascript">
var fieldId  = "${record.buildingId?c}";
var paths = "${RESOURSE_SERVER_PATH!''}${record.buildingExteriorFigure!''}";

function ffcs_viewImg(fieldId){
	var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId;
	var name = "图片查看";
	openPostWindow(url, paths, name);
// 	window.open("${rc.getContextPath()}/zhsq/showImage/indexOfPaths.jhtml?paths="+ paths +"&fieldId="+ fieldId,"查看图片",'');
}

var isCross;
<#if isCross??>
	isCross = '${isCross}';
</#if>

function showBuildDetail(title,buildingId) {
	if (isCross != undefined) { // 跨域
		var url = "${SQ_ZZGRID_URL}/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+buildingId;
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+948+","+405+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
		var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+buildingId;
		var height = $(window.parent).height() * 0.9;
		window.parent.showMaxJqueryWindow(title,url,1200,height);
	}
}

function openPostWindow(url, data, name){
	var tempForm = document.createElement("form");
	tempForm.id="tempForm1";
	tempForm.method="post";
	tempForm.action=url;
	tempForm.target=name;
	var hideInput = document.createElement("input");
	hideInput.type="hidden";
	hideInput.name= "paths";
	hideInput.value= data;
	tempForm.appendChild(hideInput);
	tempForm.submit(function(){
		openWindow(name);
	});
//		tempForm.attachEvent("onsubmit",function(){
//			openWindow(name);
//		});
	document.body.appendChild(tempForm);
	tempForm.fireEvent("onsubmit");
	tempForm.submit();
	document.body.removeChild(tempForm);
}

function openWindow(name){
	window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
}
</script>
</html>