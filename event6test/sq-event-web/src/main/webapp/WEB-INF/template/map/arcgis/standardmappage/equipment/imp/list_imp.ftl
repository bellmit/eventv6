<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/scrollpagination.js" type="text/javascript"></script>
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

var page = 0;
$(function() {
	var buildingId= "${buildingId!''}";
 	layer.load(0);
    $('#content').scrollPagination({
        'contentPage': '${rc.getContextPath()}/zhsq/map/arcgis/equ/imp/listData.json',
        'contentData': {buildingId : buildingId, rows : 10, page : function(){return page} },
        'scrollTarget': $(window),
        'heightOffset': 10,
        'beforeLoad': function() {
			page = page + 1;
		},
        'afterLoad': function(elementsLoaded) {
    		layer.closeAll('loading');
            $(elementsLoaded).fadeInWithDelay();
        }
    });

    $.fn.fadeInWithDelay = function() {
        var delay = 0;
        return this.each(function() {
            $(this).delay(delay).animate({
                opacity: 1
            },
            200);
            delay += 100;
        });
    };
});
// jQuery ScrollPagination

function load(pageNo, pageSize){
	var postData = 'page='+pageNo+'&rows='+pageSize;
	$.ajax({
		type: "POST",
		url:'${rc.getContextPath()}/zhsq/map/arcgis/equ//imp/listData.json?t='+Math.random(),
		data: postData,
		dataType:"json",
		success: function(data){
			console.log(data);
		}
	});
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
<body>
<div class="implistMainer">
  <h3><span class="ftit">异常出入次数统计时间范围为近一个月</span></h3>
  <div id="content" class="implist">
<!--     <table width="100%" border="0" cellspacing="0" cellpadding="0"> -->
<!--       <tr> -->
<!--         <td rowspan="3"><img src="${uiDomain}/images/map/gisv0/special_img/zd_img1.jpg" width="54" height="63" /></td> -->
<!--         <td>王连盛&nbsp;&nbsp;&nbsp;男&nbsp;&nbsp;&nbsp;1978-12-10出生</td> -->
<!--         <td rowspan="3"><div class="ycbox">9<div class="yc-tit">异常出入</div></div></td> -->
<!--       </tr> -->
<!--       <tr> -->
<!--         <td>所属楼宇：红谷滩世纪花园社区</td> -->
<!--       </tr> -->
<!--       <tr> -->
<!--         <td>身份证：<span class="c-sfz">350898909098767876</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="${uiDomain}/images/map/gisv0/special_img/zd_icon1.png" width="15" height="15" /></td> -->
<!--       </tr> -->
<!--     </table> -->
  </div>
</div>
</body>
</html>