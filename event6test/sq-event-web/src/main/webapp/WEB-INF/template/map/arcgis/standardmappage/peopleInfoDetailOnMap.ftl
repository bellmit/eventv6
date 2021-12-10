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
                    	<#if residentDetail.picUrl??>
							<img src="${RESOURSE_SERVER_PATH}${residentDetail.picUrl}" />
						<#else>
							<img id="peopleImg"  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" />
						</#if>
                    	
                    </li>
                    <li style="width:250px;">
                    	<p style="padding-left:0;"><span style="cursor:pointer;" onclick="showPopDetail('${residentDetail.partyName}','${residentDetail.partyId?c}')"><#if residentDetail.partyName?exists>${residentDetail.partyName}</#if>&nbsp;</span>
                    	<#if residentDetail.genderCN?exists>
							${residentDetail.genderCN}
						</#if>&nbsp;
						<#if residentDetail.nationCN?exists>${residentDetail.nationCN}</#if>&nbsp;
						<#if residentDetail.birthday?exists><#if residentDetail.birthday?is_string>${residentDetail.birthday}<#else>${residentDetail.birthday?string("yyyy-MM-dd")}</#if>出生</#if></p>
						

						
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
                        	<span style="color:#000;line-height:24px;font-size:12px;">所属楼宇：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
		                    	${buildingName}
							</span><br/>
                        </#if>
                        
                        <div style=" <#if (residentDetail.identityCardHS?exists || residentDetail.identityCard?exists) && (residentDetail.mobilePhoneHS?exists || residentDetail.mobilePhone?exists)>height:36px;<#else>height:24px;</#if>">
	                        <#if residentDetail.identityCardHS?exists>
	                        	<p class="PeopleID fl">
	                        		${residentDetail.identityCardHS}
									<#elseif residentDetail.identityCard?exists>
										${residentDetail.identityCard}
	                        	</p>
	                        </#if>	
	                        <#if residentDetail.mobilePhoneHS?exists>
	                        	<p class="PeoplePhone fl" title="${residentDetail.mobilePhoneHS}">
	                        		${residentDetail.mobilePhoneHS}
	                        	</p>
							<#elseif residentDetail.mobilePhone?exists>
									<p class="PeoplePhone fl" title="${residentDetail.mobilePhone}">
										${residentDetail.mobilePhone}
									</p>
							</#if>
                        </div>

                        <#if tagFlag??>
							<#if (tagFlag?size>0)>
								<p class="PeopleTags" style="width:220px">
									<#list tagFlag as val>
										<a name="all_kinds_info" style="color:#0075a9;" href="javascript:showPeopleDetail('${val.aliasName}(${residentDetail.partyName})','${val.viewUrl}','${val.relCode}','${val.width}','${val.height}');" style="color:blue">${val.aliasName}</a>
                        			</#list> 
                        		</p>
                      	 	</#if>
						</#if> 
						
						<#if visitRecordList??>
                        	<#if (visitRecordList?size>0)>
	                        	<p class="VisitRecord">
	                        		<a id="visitRecord" name="visitRecord" style="color:#0075a9;" href="javascript:showVisitRecord('${residentDetail.partyId?c}');">走访记录(<#if visitRecordCount??>${visitRecordCount?c}</#if>)</a>
	                        	</p>
                        	</#if>
						</#if>

						<#if crowdDict?? && tagCodeList?? && showPolicyLawBut?? && showPolicyLawBut == "yes">
							<#if (crowdDict?size>0) && (tagCodeList?size>0)>
								<a id="policyLaw" name="policyLaw" style="color:#0075a9;background: #03A9F4;color: #fff;padding: 5px 20px;margin-top: 5px;display: inline-block;border-radius: 20px;" href="javascript:showPolicyLaw();">政策法规</a>
							</#if>
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

var tagKey = '';
<#if tag??>
	<#if (tag?size>0)>
		<#list tag as val>
			tagKey  += '${val[0]}'+',';
		</#list>
  	</#if>
</#if> 

function showPopDetail(title,partyId) {
	if (isCross != undefined) { // 跨域
		var url = "${POPULATION_URL}/cirs/viewResident.jhtml?menu=1&partyId="+partyId;
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+800+","+370+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		var population_url =  window.parent.document.getElementById("POPULATION_URL").value;
		var url = population_url+"/cirs/viewResident.jhtml?menu=1&partyId="+partyId;
	
		window.parent.showMaxJqueryWindow(title,url,800,370);
	}
}

function showPeopleDetail(title, url, b, w, h) {
	if(h==null || h=='null'|| h==''){
		h = 450;
	}
	if(w==null || w=='null' || w==''){
		w = 800;
	}
	// 综治标签
	var zzgridTags = ["P","N","O","K","L","Q","AR","SW","Z","M","AQ","AS","AT","SX","FA","IH","DE","TD","LA","WZ"];
	// gmis标签
	var gmisTags = ["VW","VC"];
	var isZZgridTag = $.inArray(b, zzgridTags) > -1 ? true : false;
	var isGmisTag = $.inArray(b, gmisTags) > -1 ? true : false;
	if(isZZgridTag){
		url = "${SQ_ZZGRID_URL}"+ url;
	}else if(isGmisTag){
		url = "${SQ_GMIS_URL}"+ url +"&parentHeight="+ $(window).height();
	}else{
		//人口标签
		url = "${POPULATION_URL}"+ url;
	}
    if(url.indexOf('/DFarmy/showItem')> -1){
        h = 450;//涉军人员高度调整
    }
	window.parent.showMaxJqueryWindow(title,url,w,h);
}

function showDetail(partyId,id,name) {
	var url = "${SQ_ZZGRID_URL}/zzgl/map/data/residentionfo/typeDetail/"+partyId+".jhtml?id="+id+"&gridId="+${gridId?c};
	addPersonLi(partyId,name,id,url);
}

function showVisitRecord(partyId) {
	var height = 500;
	var width = 1100;
	var title = "走访记录";
	var url = "${RS_DOMAIN}/visitRecord/visit.jhtml?serviceObject="+partyId+"&view=view";
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,width,height);
	}
}

function showPolicyLaw() {
	var height = 450;
	var width = 950;
	var title = "政策法规";
	var url = "${SQ_EVENT_URL}/zhsq/map/arcgis/arcgisdataofpoplocal/showPolicyLaw.jhtml?tagKey="+tagKey+"&gridId="+${gridId?c};
	if (isCross != undefined) { // 跨域
		url = url.replace(/\&/g,"%26");
		title = encodeURIComponent(encodeURIComponent(title));
		
		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
		$("#cross_domain_iframe").attr("src",urlDomain);
	} else {
		window.parent.showMaxJqueryWindow(title,url,width,height);
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
function addPersonLi(partyId,liName,code,url) {
	var src = "";
	var height = 230;
	var width = 635;
	var title = liName;
	if (code == "1") {// 党员信息 0101
        url = "${POPULATION_URL}/party/showItem.jhtml?operate=view&partyId="+partyId;
		width = 900;
		height = 300;
	} else if (code == "11") {// 吸毒信息 0201
        src = url;
		width = 720;
		height = 400;
	} else if (code == "16") {// 台胞信息
		src = url;
		height = 167;
	} else if (code == "14") {// 刑释解教信息 0202
		src = url;
	} else if (code == "13") {// 矫正信息 0203
		src = url;
		height = 227;
	} else if (code == "12") {// 邪教信息 0204
		src = url;
		height = 210;
	} else if (code == "10") {// 上访信息 0205
		src = url;
		height = 164;
		width = 450;
	} else if (code == "9") {// 危险品从业信息 0206
		src = url;
		height = 172;
		width = 550;
	} else if (code == "8") {// 精神障碍患者信息 0207
		height = 240;
		src = url;
	} else if (code == "7") {// 残障信息 0301
        url = "${POPULATION_URL}/deformity/showItem.jhtml?itemName=baseinfo&operate=view&partyId="+partyId;
        height = 300;
        width = 900;
	} else if (code == "6") {// 低保信息 0302
        url = "${POPULATION_URL}/poor/showItem.jhtml?itemName=baseinfo&operate=view&partyId="+partyId;
        height = 300;
        width = 900;
	} else if (code == "0303") {
		src = url;
		height = 250;
		width = 635;
	}else if (code == "3") {// 居家养老信息 0303
        src = "${POPULATION_URL}/hbcare/showItem.jhtml?itemName=baseinfo&operate=view&partyId="+partyId;
		height = 137;
        width = 450;
	} else if (code == "0304") {// 志愿者信息 0304
        url = "${POPULATION_URL}/volunteer/showItem.jhtml?operate=view&partyId="+partyId;
        width = 900;
        height = 300;
	}  else if (code == "15") {// 志愿者信息 0304
        url = "${POPULATION_URL}/volunteer/showItem.jhtml?operate=view&partyId="+partyId;
        width = 900;
        height = 300;
	}else if (code == "2") {// 退休信息 0401
        url = "${POPULATION_URL}/unemployed/showItem.jhtml?itemName=employ&operate=view&partyId="+partyId;
        width = 900;
		src = url;
        height = 300;
	} else if (code == "5") {// 失业信息 0402
        url = "${POPULATION_URL}/unemployed/showItem.jhtml?itemName=employ&operate=view&partyId="+partyId;
        width = 900;
		src = url;
        height = 300;
	} else if (code == "4") {// 服兵役信息 1001
        url = "${POPULATION_URL}/army/showItem.jhtml?operate=view&partyId="+partyId;
        width = 900;
		src = url;
		height = 162;
	} else if (code == "18") {
		url = "${SQ_ZZGRID_URL}/zzgl/crowd/petitioner/detail.jhtml?ismap=2&miId=" + $("#miId").val();
		width = 900;
		height = 370;
	} else if (code == "21") {
		url = "${SQ_ZZGRID_URL}/resident/crowd/index/"+partyId+".jhtml?crowdType=AS&isDetail=detail";
		width = 660;
		height = 400;
	} else if (code == "19") { //重点青少年

	}else if (code == "22") {// 涉羟人员
        url = "${SQ_ZZGRID_URL}/resident/crowd/index/"+partyId+".jhtml?crowdType=IH&isDetail=detail";
        width = 1000;
        height = 400;
    }else if (code == "23") {// 2次以上行政拘留人员
        url = "${SQ_ZZGRID_URL}/zzgl/crowd/detention/form.jhtml?partyId="+partyId+"&isDetail=isDetail";
        width = 1000;
        height = 400;
    }else if (code == "24") {// 重点信访事项的挑头人
        url = "${SQ_ZZGRID_URL}/resident/crowd/index/"+partyId+".jhtml?crowdType=Q&isDetail=detail";
        width = 1000;
        height = 400;
    }else if (code == "25") {// 独居人
        url = "${SQ_ZZGRID_URL}/resident/crowd/index/"+partyId+".jhtml?crowdType=LA&isDetail=detail";
        width = 1000;
        height = 300;
    }else if (code == "26") {// 危重人
        url = "${SQ_ZZGRID_URL}/resident/crowd/index/"+partyId+".jhtml?crowdType=WZ&isDetail=detail";
        width = 1000;
        height = 370;
    }else if (code == "27") {// 暖心对象
        url = "${POPULATION_URL}/warmHeart/showItem.jhtml?partyId="+partyId+"&operate=view";
        width = 1000;
        height = 260;
    }

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
	//window.parent.showBuildDetail(buildingId, "楼宇详细信息");
	var url = "${SQ_ZZGRID_URL}/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+buildingId;
	var title = "楼宇详细信息";
	var width = 1200;
	var height = 555;
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
