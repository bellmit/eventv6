<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

</head>
<body style="background-color: #fff;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<#if residentDetail?exists>
		<div class="con PeopleWatch" >
        	<div class="PeopleInfo" style="overflow:auto;"> 
                <ul>
                    <li>
                    	<#if residentDetail.I_PHOTO_URL??>
							<img src="${RESOURSE_SERVER_PATH}${residentDetail.I_PHOTO_URL}" border=0 width="105px;" height="140px;" />
						<#else>
							<img id="peopleImg"  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" border=0 style="width: 105px; height: 140px" />
						</#if>
                    	
                    </li>
                    <li style="width:250px;">
                    	<p style="padding-left:0;"><span style="cursor:pointer;" onclick="showPopDetail('${residentDetail.I_NAME}','${residentDetail.CI_RS_ID}')"><#if residentDetail.I_NAME?exists>${residentDetail.I_NAME}</#if>&nbsp;</span>
                    	<#if residentDetail.I_GENDER?exists>
							<#if residentDetail.I_GENDER="F">
								女
							<#else>
								男
							</#if>
						</#if>&nbsp;
						<#if residentDetail.I_ETHNIC?exists>${residentDetail.I_ETHNIC}</#if>&nbsp;
						<#if residentDetail.BIRTHDAY_STR?exists>${residentDetail.BIRTHDAY_STR}出生</#if></p>
						
						
 						<#if gridNames?exists>
                        	<span style="color:#000;line-height:24px;font-size:12px;">网格：</span>
                    	
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;" title="${gridNames}">
							<#if (gridNames)?length lt 24 >
	                    		${gridNames}
	                    	<#else>
	                    		${gridNames[0..23]}..
	                    	</#if>
							</span><br/>
                        </#if>                       	
                        
						
                        <#if residentDetail.I_RESIDENCE_ADDR?exists>
                        	<span style="color:#000;line-height:24px;font-size:12px;">地址：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
								<#if (residentDetail.I_RESIDENCE_ADDR)?length lt 24 >
		                    		${residentDetail.I_RESIDENCE_ADDR}
		                    	<#else>
		                    		${residentDetail.I_RESIDENCE_ADDR[0..24]}..
		                    	</#if>
							</span>
                        </#if>	
                        
                        
                        <#if residentDetail.I_IDENTITY_CARD?exists>
                        	<p class="PeopleID">
                        		${residentDetail.I_IDENTITY_CARD}
                        	</p>
                        </#if>	
                        
                        
                        <#if residentDetail.RESIDENT_MOBILE?exists>
                        	<p class="PeoplePhone">
                        		${residentDetail.RESIDENT_MOBILE}
                        	</p>
                        </#if>	
                        
                        
                        <#if tag??>
							<#if (tag?size>0)>
								<p class="PeopleTags" style="width:220px">
									<#list tag as val>
										<a name="all_kinds_info" style="color:#0075a9;" href="javascript:showDetail('${residentDetail.CI_RS_ID?c}','${val[0]}','${val[1]}(${residentDetail.I_NAME})');" style="color:blue">${val[1]}</a>
                        			</#list> 
                        		</p>
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
/*
$(document).ready(function(){
	var imgPath = $("#peopleImg").attr("src");
	var root = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	$("#peopleImg").attr("src",root+imgPath);
});
*/

function showPopDetail(title,ciRsId) {
	var population_url =  window.parent.document.getElementById("POPULATION_URL").value;
	var url = population_url+"/cirs/viewResident.jhtml?menu=1&ciRsId="+ciRsId;
	window.parent.showMaxJqueryWindow(title,url,800,370);
}

function showDetail(ciRsId,id,name) {
		if (!window.parent.isClicking) {
			var url = "${SQ_ZZGRID_URL}/zzgl/map/data/residentionfo/typeDetail/"+ciRsId+".jhtml?id="+id+"&gridId="+${gridId?c};
			addPersonLi(ciRsId,name,id,url);
		}
}

function addPersonLi(rsId,liName,code,url) {
			var src = "";
			var height = 230;
			var width = 635;
			var title = liName;
			if (code == "1") {// 党员信息 0101
				src = url;
				height = 202;
			} else if (code == "11") {// 吸毒信息 0201
				src = url;
				height = 241;
			} else if (code == "16") {// 台胞信息
				src = url;
				height = 160;
			} else if (code == "14") {// 刑释解教信息 0202
				src = url;
				height = 273;
			} else if (code == "13") {// 矫正信息 0203
				src = url;
				height = 220;
			} else if (code == "12") {// 邪教信息 0204
				src = url;
				height = 203;
			} else if (code == "10") {// 上访信息 0205
				src = url;
				height = 117;
			} else if (code == "9") {// 危险品从业信息 0206
				src = url;
				height = 125;
			} else if (code == "8") {// 精神障碍患者信息 0207
				src = url;
				height = 200;
				width = 935;
			} else if (code == "7") {// 残障信息 0301
				src = url;
				height = 242;
			} else if (code == "6") {// 低保信息 0302
				src = url;
				height = 195;
			} else if (code == "0303") {// 居家养老信息 0303
				src = url;
				height = 250;
				width = 635;
			}else if (code == "3") {// 居家养老信息 0303
				src = url;
				height = 118;
			} else if (code == "0304") {// 志愿者信息 0304
				src = "";
			}  else if (code == "15") {// 志愿者信息 0304
				src = url;
				height = 260;
				width = 690;
			}else if (code == "2") {// 退休信息 0401
				src = url;
				height = 163;
			} else if (code == "5") {// 失业信息 0402
				src = url;
				height = 156;
			} else if (code == "4") {// 服兵役信息 1001
				src = url;
				height = 155;
			} else if (liName == "走访记录") {// 走访记录
				src = "${SQ_ZZGRID_URL}/gis.shtml?method=getPersonnelVisitsRecord&id=" + rsId + "&codes=" + code;
				height = 250;
			}

			// 隐藏div
			$("#person_div div").each(function(){
				$(this).css("display", "none");
			});
			
			window.parent.showMaxJqueryWindow(title,url,width,height);
}

</script>
</html>
