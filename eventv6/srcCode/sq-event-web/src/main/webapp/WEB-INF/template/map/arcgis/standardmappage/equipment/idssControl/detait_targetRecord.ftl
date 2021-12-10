<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>立体防控-设备-概要信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
body{ padding:0; margin:0; font-family:Verdana, Geneva, sans-serif, "微软雅黑", "冬青黑体简体中文 W3"; font-size:12px; color:#333; line-height:1.5}
h3,p,dl,dd,dt{ margin:0; padding:0;}
.floatclear{ clear:both;}
.relateMainer{ padding:10px;}
.ralate-list{ border-bottom:1px solid #ddd; padding:10px 0;}
.ralate-list p{ padding:2px 0;}
.c-time{ color:#2d3dbf;}
.c-sfz{ color:#ec4a25;}
.c-gide{ color:#f5911d;}
.c-hs{ color:#888;}
.personlist{ border-collapse:collapse; font-size:12px;}
.personlist td{ padding:2px;}
.btn-family{ border:none;background:#f5911d; padding:6px 8px; cursor:pointer; color:#fff; font-size:12px;}
.ralate-list h3{ font-weight:normal; font-size:14px;}
.gide-list{ margin-top:10px;}
.gide-list dt{ float:left; margin-right:10px;}
.gide-list dd p{ padding:0;}

.implistMainer .ftit{ font-size:14px; font-weight:normal; color:#888; margin-left:10px;}
.implist{ margin-top:10px;}
.ycbox{ background:#fe9900; width:50px; height:50px; border-radius:6px; color:#fff; position:relative; text-align:center; font-weight:bold; font-size:24px;}
.ycbox .yc-tit{ position:absolute; left:0; bottom:0; background:#ff6501; font-size:12px; width:100%; border-radius:0px 0 6px 6px; font-weight:normal;}
.tag{margin-right:10px;float:left;cursor:pointer;width:30px;height:30px;background:#bf3630;}
</style>
<script type="text/javascript">

$(window).load(function(){
	$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
	$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
// 	$("#content-d").mCustomScrollbar({theme:"dark"});
	$(".cn").mCustomScrollbar({theme:"minimal-dark"});
});


function family(ciRsId){
	var title = "家庭成员";
	var width = 720;
	var height = 400;
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/showFamily.jhtml?ciRsId="+ciRsId;
	window.parent.showMaxJqueryWindow(title,url,width,height);
}

function showDetail(ciRsId,id,name){
	var url = "${SQ_ZZGRID_URL}/zzgl/map/data/residentionfo/typeDetail/"+ciRsId+".jhtml?id="+id+"&gridId="+${gridId?c};
	addPersonLi(ciRsId,name,id,url);
}

function addPersonLi(rsId,liName,code,url) {
	var src = "";
	var height = 230;
	var width = 635;
	var title = liName;
	if (code == "1") {// 党员信息 0101
		url = "${POPULATION_URL}/party/viewBaseAndActivity.jhtml?ciRsId="+rsId;;
		width = 950;
		height = 400;
	} else if (code == "11") {// 吸毒信息 0201
		var ENABLE_GB = '${ENABLE_GB!}';
		
		if(ENABLE_GB && ENABLE_GB == '1'){
			url = '${SQ_ZZGRID_URL}/zzgl/crowd/drug/toDetail.jhtml?ciRsId='+rsId;
			width = 720;
			height = 400;
		} else {
			height = 335;
		}
		
		src = url;
	} else if (code == "16") {// 台胞信息
		src = url;
		height = 167;
	} else if (code == "14") {// 刑释解教信息 0202
		src = url;
		height = 319;
	} else if (code == "13") {// 矫正信息 0203
		var ENABLE_GB = '<#if ENABLE_GB??>${ENABLE_GB!''}</#if>'
		if(ENABLE_GB != null && ENABLE_GB != '' && ENABLE_GB == '1'){
			url = "${SQ_ZZGRID_URL}/zzgl/crowd/correctional/detail.jhtml?standard=standard&ismap=2&ciRsId=" + rsId;
			width = 620;
			height = 400;
		}else{
			src = url;
			height = 227;
		}
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
		var ENABLE_GB = '${ENABLE_GB!}';
		if(ENABLE_GB && ENABLE_GB == '1'){
			url = '${SQ_ZZGRID_URL}/zzgl/crowd/mentalIllnessRecord/toDetail.jhtml?ciRsId='+rsId;
			width = 750;
			height = 400;
		} else {
			height = 240;
		}
		src = url;
		height = 240;
	} else if (code == "7") {// 残障信息 0301
		src = url;
		height = 417;
	} else if (code == "6") {// 低保信息 0302
		src = url;
		height = 202;
	} else if (code == "0303") {
		src = url;
		height = 250;
		width = 635;
	}else if (code == "3") {// 居家养老信息 0303
		src = url;
		height = 137;
		width = 450;
	} else if (code == "0304") {// 志愿者信息 0304
		src = "";
	}  else if (code == "15") {// 志愿者信息 0304
		src = url;
		height = 337;
	}else if (code == "2") {// 退休信息 0401
		src = url;
		height = 170;
	} else if (code == "5") {// 失业信息 0402
		src = url;
		height = 202;
	} else if (code == "4") {// 服兵役信息 1001
		src = url;
		height = 162;
		width = 500;
	} else if (liName == "走访记录") {// 走访记录
		src = "${SQ_ZZGRID_URL}/gis.shtml?method=getPersonnelVisitsRecord&id=" + rsId + "&codes=" + code;
		height = 250;
	} else if (code == "18") {
		url = "${SQ_ZZGRID_URL}/zzgl/crowd/petitioner/detail.jhtml?ismap=2&miId=" + $("#miId").val();
		width = 900;
		height = 370;
	} else if (code == "21") {
		url = "${SQ_ZZGRID_URL}/zzgl/crowd/aids/detail.jhtml?ciRsId=" + rsId;
		width = 660;
		height = 400;
	} else if (code == "19") { //重点青少年
		var ENABLE_GB = '<#if ENABLE_GB??>${ENABLE_GB!''}</#if>'
		if(ENABLE_GB != null && ENABLE_GB != '' && ENABLE_GB == '1'){
			url = "${SQ_ZZGRID_URL}/zzgl/crowd/youth/detail.jhtml?ismap=2&standard=standard&id=" + rsId;
			width = 720;
			height = 400;
		}
	}

	// 隐藏div
	$("#person_div div").each(function(){
		$(this).css("display", "none");
	});

	window.parent.showMaxJqueryWindow(title,url,width,height);
}

</script>
</head>
<body class="cn">
	<div class="relateMainer">
	  <div class="ralate-list">
	    <p>时间：<span class="c-time">${controlTargetRecord.eventTime?datetime}</span></p>
	    <p>设备：
	    	<#if controlTargetRecord.eventBussinessType == '1'>
       			门禁设备
       		<#elseif controlTargetRecord.eventBussinessType == '2'>
       			访客机设备
       		<#elseif controlTargetRecord.eventBussinessType == '3'>
       			卡口设备
       		<#elseif controlTargetRecord.eventBussinessType == '4'>
       			车闸设备
       		<#elseif controlTargetRecord.eventBussinessType == '5'>
       			人脸设备
       		</#if>- ${controlTargetRecord.eqpName!''}</p>
	    <p>对象：
	    	<#if controlTargetRecord.controlTargetType == '001'>
       			身份证
       		<#elseif controlTargetRecord.controlTargetType == '002'>
       			车牌
       		<#elseif controlTargetRecord.controlTargetType == '003'>
       			手机号码
       		</#if>- <span class="c-sfz">${controlTargetRecord.controlTargetObject!''}</span>
	    </p>
	  </div>
	  
	  <#if ciRsTop??>
	  <div class="ralate-list">
	    <h3>关联人员</h3>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="personlist">
	      <tr>
	        <td rowspan="4"><img src="<#if ciRsTop.photoUrl??>${RESOURSE_SERVER_PATH}${ciRsTop.photoUrl!''}</#if>" width="54" height="63" /></td>
	        <td>${ciRsTop.name!''}&nbsp;&nbsp;&nbsp;${ciRsTop.genderCN!''}&nbsp;&nbsp;&nbsp;${(ciRsTop.birthday?string("yyyy-MM-dd"))!''}出生</td>
	        <td rowspan="4"><button class="btn-family" onclick="family(${ciRsTop.ciRsId?c})">家庭成员</button></td>
	      </tr>
	      <tr>
	        <td>网格：${ciRsTop.gridName!''}</td>
	      </tr>
	      <tr>
	        <td>所属楼宇：红谷滩世纪花园社区</td>
	      </tr>
	      <tr>
	        <td>身份证：<span class="c-sfz">${ciRsTop.identityCard!''}</span></td>
	      </tr>
	      <tr>
	        <td colspan="3">
	        	<#if tag??>
					<#if (tag?size>0)>
						<p class="PeopleTags" style="width:220px">
							<#list tag as val>
							<#if val[0] == '11'>
								<div class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/吸毒人员.png" width="30" height="30" />
	                     		</div>
							</#if>
                     		<#if val[0] == '14'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/刑释解教人员.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '7'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/残障.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '8'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/重精神病.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '13'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/矫正人员.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '9'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/危险品从业人员.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '6'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/低保.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '2'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/退休.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		<#if val[0] == '5'>
                     			<div style="background:#871dca;" class="tag" onclick="showDetail('${ciRsTop.ciRsId?c}','${val[0]}','${val[1]}(${ciRsTop.name})');">
	                     		<img title="${val[1]}" src="${uiDomain}/images/map/gisv0/special_config/images/失业.png" width="30" height="30" />
	                     		</div>
                     		</#if>
                     		</#list>
                     	</p>
                   	 </#if>
				</#if>
	        </td>
	      </tr>
	    </table>
	  </div>
	  </#if>
	  
	  <!-- 网格力量 -->
	  <#if gridAdmins??&&(gridAdmins?size>0) >
	  <div class="ralate-list" style="border:none;">
	    <h3>网格力量&nbsp;&nbsp;<span class="c-gide">(设备所处网格的网格人员)</span></h3>
	    <#list gridAdmins as l>
	    <dl class="gide-list">
	      <dt><img id="preview" <#if l.photo??> src="${RESOURSE_SERVER_PATH}${l.photo}" <#else> src="${rc.getContextPath()}/images/untitled.png"</#if> width="35" height="44" /></dt>
	      <dd>
	        <p>${l.partyName!''}</p>
	        <p><span class="c-hs">工作手机：${l.mobileTelephone!''}</span></p>
	      </dd>
	    </dl>
	    <div class="floatclear"></div>
	    </#list>
	  </div>
	  </#if>
	</div>
</body>
</html>