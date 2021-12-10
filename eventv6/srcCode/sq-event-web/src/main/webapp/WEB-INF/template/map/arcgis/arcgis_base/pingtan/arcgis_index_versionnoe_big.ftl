<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>矛盾纠纷</title>
<#include "/component/commonFiles-1.1.ftl" />
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/xdate.js" type="text/javascript"></script>
<link href="${uiDomain!''}/css/special_arcgis_pingtan/style2.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="disputeMainer">
  <div class="countrymain" id="countrymain">
  </div><!--end .countrymain-->
  <div class="pingtan" id="pingtan">
<!--     <h3>平潭整体风险指数<i class="dagico"></i></h3> -->
<!--     <p>自2014年1月1日至2016年8月31日，平潭共计发生矛盾纠纷案件<span class="szlan">18980</span>起</p> -->
<!--     <p>本月截止6号发生案件785起，相较上月同期<span class="szadd">增加7起</span>，呈增多趋势</p> -->
<!--     <p>本月截止6号发生案件类型最多的是<span class="szadd">城乡建设发展纠纷</span>，建议相关部门采取有效的预防干预措</p> -->
  </div>
  <div class="danger-date" id="danger-date"></div>
  <div class="danger-map" style="padding-left:30%; margin-top:1%;"> 
  <canvas id="c_351001" style="position:absolute ; z-index:10 " width="400px" height="275px"  ></canvas>
  <canvas id="c_351002" style="position:absolute ; z-index:11"   width="400px" height="275px"  ></canvas>
    <canvas id="c_351003" style="position:absolute ; z-index:12"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351004" style="position:absolute ; z-index:13"   width="400px" height="275px" ></canvas>
    <canvas id="c_351005" style="position:absolute ; z-index:14"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351006" style="position:absolute ; z-index:15"  width="400px" height="275px"  ></canvas>
    <canvas id="c_351007" style="position:absolute ; z-index:16"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351008" style="position:absolute ; z-index:17"  width="400px" height="275px"  ></canvas>
    <canvas id="c_351009" style="position:absolute ; z-index:18"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351010" style="position:absolute ; z-index:19"   width="400px" height="275px" ></canvas>
    <canvas id="c_351011" style="position:absolute ; z-index:20"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351012" style="position:absolute ; z-index:21"  width="400px" height="275px"  ></canvas>
    <canvas id="c_351013" style="position:absolute ; z-index:22"  width="400px" height="275px"  ></canvas>
  <canvas id="c_351014" style="position:absolute ; z-index:23"   width="400px" height="275px" ></canvas>
    <canvas id="c_351015" style="position:absolute ; z-index:24"   width="400px" height="375px" ></canvas>
 </div>
  <div class="clearfloat"></div>
  <div class="chartmain">
    <div class="chart-con">
      <h2>类型占比分析</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/top10Pie.jhtml"></iframe></div>
    </div><!--end .chart-con-->
    <div class="chart-con" style="width:596px; background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/big/dispute_bg4.png) no-repeat;">
      <h2>纠纷舆情趋势</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/trendLine.jhtml"></iframe></div>
    </div><!--end .chart-con-->
    <div class="chart-con">
      <h2>纠纷化解分析</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/resolutionRate.jhtml"></iframe></div>
    </div><!--end .chart-con-->
  </div><!--end .chartmain-->
</div><!--end .disputeMainer-->
</body>
</html>
<script type="text/javascript">
$(function(){
	getTopDispute();
});

function getTopDispute(){
 	layer.load(0);
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/topDispute.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
			getWholeInfo();
			if(data!=null&&data.length>0){
				for(var i=0;i<3;i++){
					var regionName = data[i].regionName;
// 					console.log(regionName.length);
					if(regionName.length>3) regionName = regionName.substring(0,3);
					var img = '';
					var cls = '';
					var color = '';
					if(data[i].riskNum<=3){
						cls = '3';
						color = 'green';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg8.png) no-repeat;';
					}else if(data[i].riskNum>3&&data[i].riskNum<=6){
						cls = '2';
						color = 'blue';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg7.png) no-repeat;';
					}else if(data[i].riskNum>6){
						cls = '1';
						color = 'red';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg6.png) no-repeat;';
					}
					fillColor(data[i].regionCode, color);
					$('#countrymain').append('<div class="country-con"><h2>'
							+regionName+'</h2><dl><dt><i class="danger'+cls+'" style="'+img+'">'
							+data[i].riskNum+'</i><br /><span class="'+color+'">风险指数</span></dt><dd>矛盾纠纷案件'
							+data[i].allNum+'起,'
							+data[i].allNumRateStr+'<br />化解率为'
							+data[i].mediationRateStr+'%，'+data[i].mediationNumRateStr+'<br />建议'
							+data[i].allNumAdvStr+'，'+data[i].mediationNumAdvStr+'。</dd></dl></div>');
				}
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
    		layer.closeAll('loading');
		}
	});
}

function getWholeInfo(){
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/wholeInfo.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
    		layer.closeAll('loading');
			if(data != null){
				var riskNum = data.riskNum;
				var text = '';
				var img = '';
				if(riskNum >= 0 && riskNum < 5){
					text = '低风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg5.png) no-repeat';
				}else if(riskNum >= 5 && riskNum < 8){
					text = '中等风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg9.png) no-repeat';
				}else if(riskNum >= 8 && riskNum < 11){
					text = '高风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg10.png) no-repeat';
				}
				$('#danger-date').css('background',img);
				$('#danger-date').html(text);
				var disputeTypeStr = '';
				if(data.monthMediationType!=null){
					disputeTypeStr = '<p>本月截止'+data.previousDate+'号发生案件类型最多的是<span class="szadd">'+data.monthMediationType.DISPUTE_TYPE_STR+'</span>，建议相关部门采取有效的预防干预措</p>';
				}
				var img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_danger'+riskNum+'.png) no-repeat;';
				$('#pingtan').append('<h3>'+data.regionName+'整体风险指数<i class="dagico" style="'+img+'"></i></h3><p>截止至'
						+data.previousMaxDate+'，'+data.regionName+'共计发生矛盾纠纷案件<span class="szlan">'
						+data.allNum+'</span>起</p><p>本月截止'+data.previousDate+'号发生案件'
						+data.thismonth2Lastday+'起，'+data.thismonth2LastdayMsg+'</p>'+disputeTypeStr);
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
    		layer.closeAll('loading');
		}
	});
}

function fillColor(regionCode, color) {
	try {
		var colors = {
			"green" : "#3ef03e",
			"blue" : "#10e7e0",
			"red" : "#ff6060"
		};
		var ps = eval('(ps' + regionCode + ')');
		canV("c_" + regionCode, ps, colors[color]);// red blue green
	} catch(e) {}
}

var ps351001 = [ 224, 184, 217, 177, 209, 171, 204, 169, 210, 168, 211, 163,
		208, 160, 204, 154, 210, 154, 215, 154, 219, 152, 219, 148, 223, 148,
		229, 148, 231, 149, 235, 149, 236, 154, 237, 157, 239, 162, 240, 167,
		241, 167, 241, 171, 236, 175, 230, 181, 224, 185, 224, 187 ];
var ps351002 = [ 172, 124, 170, 131, 169, 134, 168, 143, 171, 147, 170, 150,
		176, 142, 182, 135, 185, 127, 190, 125, 195, 126, 198, 127, 211, 132,
		218, 131, 225, 128, 226, 127, 223, 123, 221, 121, 221, 115, 218, 113,
		217, 107, 216, 102, 219, 98, 219, 94, 226, 87, 231, 84, 234, 83, 234,
		80, 232, 76, 224, 73, 218, 73, 215, 69, 210, 69, 209, 64, 207, 61, 204,
		59, 203, 69, 203, 72, 204, 78, 204, 83, 201, 91, 195, 97, 194, 104,
		197, 102, 193, 110, 189, 115, 182, 118, 178, 121, 176, 124, 173, 124 ];
var ps351003 = [ 136, 53, 139, 58, 138, 61, 133, 63, 129, 67, 128, 70, 125, 71,
		122, 73, 124, 79, 128, 85, 129, 93, 132, 97, 140, 98, 144, 100, 148,
		99, 150, 94, 151, 90, 152, 84, 149, 80, 148, 75, 148, 70, 152, 59, 161,
		55, 163, 52, 164, 47, 166, 46, 169, 41, 168, 36, 162, 32, 161, 32, 158,
		33, 155, 36, 150, 41, 153, 47, 149, 52, 145, 52, 142, 52 ];
var ps351004 = [ 206, 58, 210, 66, 215, 68, 219, 73, 226, 76, 233, 79, 232, 82,
		230, 85, 226, 88, 219, 92, 218, 96, 218, 101, 219, 111, 219, 116, 221,
		121, 226, 126, 229, 128, 235, 129, 239, 131, 243, 130, 249, 127, 256,
		124, 256, 120, 260, 119, 267, 120, 273, 123, 274, 124, 274, 129, 277,
		132, 283, 132, 284, 127, 285, 124, 288, 123, 290, 122, 290, 120, 293,
		116, 294, 112, 295, 107, 296, 100, 296, 99, 287, 103, 277, 103, 271,
		103, 266, 102, 260, 103, 259, 98, 259, 95, 254, 93, 251, 94, 249, 94,
		247, 97, 244, 97, 243, 95, 245, 91, 246, 86, 249, 81, 253, 77, 253, 73,
		250, 69, 244, 68, 238, 65, 235, 64, 231, 60, 228, 57, 225, 56, 222, 56,
		218, 57, 209, 58, 213, 59 ];
var ps351005 = [ 243, 170, 233, 173, 231, 179, 232, 181, 228, 184, 222, 189,
		218, 194, 217, 196, 217, 198, 220, 202, 223, 208, 225, 208, 227, 212,
		227, 217, 221, 223, 222, 225, 232, 220, 234, 219, 237, 211, 238, 211,
		239, 211, 241, 208, 243, 205, 249, 205, 255, 207, 258, 207, 258, 202,
		258, 201, 266, 198, 273, 196, 279, 202, 281, 199, 282, 209, 285, 205,
		288, 210, 293, 204, 294, 199, 292, 198, 289, 194, 289, 190, 290, 187,
		292, 185, 293, 184, 290, 181, 289, 181, 283, 181, 280, 182, 277, 181,
		270, 181, 265, 184, 265, 182, 263, 180, 263, 177, 258, 173, 259, 171,
		253, 173, 246, 172 ];
var ps351006 = [ 146, 160, 146, 165, 144, 168, 140, 169, 137, 170, 133, 176,
		135, 180, 135, 183, 135, 186, 135, 191, 134, 196, 135, 199, 138, 201,
		139, 202, 142, 198, 144, 195, 147, 189, 149, 187, 150, 186, 150, 186,
		157, 186, 161, 187, 162, 190, 161, 192, 155, 192, 155, 196, 153, 200,
		150, 203, 149, 207, 148, 213, 150, 220, 151, 227, 155, 228, 156, 229,
		162, 228, 161, 224, 160, 219, 160, 216, 163, 215, 165, 216, 166, 217,
		169, 218, 175, 218, 178, 218, 179, 216, 177, 209, 176, 204, 180, 204,
		184, 204, 185, 208, 185, 212, 193, 212, 192, 207, 198, 207, 201, 211,
		201, 213, 203, 217, 206, 221, 213, 221, 216, 225, 218, 225, 224, 223,
		229, 218, 229, 215, 225, 209, 218, 202, 218, 201, 211, 197, 210, 194,
		209, 186, 209, 183, 203, 179, 198, 174, 195, 169, 193, 162, 188, 157,
		186, 152, 180, 151, 175, 149, 180, 152, 180, 156, 185, 160, 181, 160,
		180, 157, 173, 156, 171, 157, 168, 159, 167, 161, 162, 162, 161, 162,
		160, 159, 157, 156, 150, 157 ];
var ps351007 = [ 152, 60, 150, 64, 149, 70, 151, 76, 151, 80, 153, 84, 154, 87,
		157, 86, 161, 85, 164, 83, 167, 87, 168, 91, 174, 92, 173, 92, 173, 90,
		173, 84, 174, 83, 178, 83, 180, 84, 182, 85, 185, 83, 185, 79, 187, 71,
		187, 68, 187, 65, 187, 61, 187, 56, 188, 52, 195, 52, 199, 52, 198, 49,
		197, 46, 191, 43, 187, 40, 184, 40, 178, 43, 173, 44, 170, 43, 170, 43,
		166, 47, 165, 49, 159, 53, 156, 56, 154, 59 ];
var ps351008 = [ 184, 207, 181, 207, 178, 207, 180, 210, 182, 214, 180, 218,
		177, 217, 168, 217, 167, 216, 163, 216, 162, 221, 164, 226, 166, 229,
		166, 232, 162, 233, 156, 234, 155, 237, 158, 239, 164, 239, 166, 237,
		172, 239, 180, 242, 182, 246, 191, 248, 194, 248, 197, 251, 201, 248,
		204, 253, 207, 259, 212, 265, 213, 268, 218, 268, 223, 266, 227, 266,
		230, 268, 231, 268, 231, 264, 232, 262, 234, 260, 234, 260, 235, 255,
		237, 255, 237, 254, 232, 252, 226, 245, 232, 250, 224, 248, 215, 250,
		213, 247, 216, 243, 216, 241, 214, 238, 214, 234, 214, 229, 217, 226,
		217, 226, 214, 222, 207, 219, 204, 218, 201, 214, 200, 213, 199, 208,
		197, 207, 194, 207, 188, 209, 186, 214 ];
var ps351009 = [ 188, 14, 181, 18, 178, 18, 174, 20, 173, 23, 171, 27, 170, 29,
		169, 34, 170, 40, 174, 44, 177, 42, 184, 41, 188, 41, 191, 41, 195, 36,
		195, 33, 199, 27, 200, 26, 203, 20, 203, 18, 205, 15, 204, 13, 201, 12,
		196, 11, 192, 11 ];
var ps351010 = [ 15, 44, 11, 31, 5, 36, 3, 44, 9, 49, 16, 49, 24, 54, 29, 54,
		32, 47, 36, 45, 39, 45, 42, 40, 48, 37, 48, 30, 56, 25, 57, 21, 58, 10,
		59, 9, 61, 5, 55, 3, 52, 3, 48, 3, 44, 4, 43, 9, 39, 12, 36, 16, 33,
		18, 27, 22, 23, 25, 23, 29, 15, 31 ];
var ps351011 = [ 85, 10, 81, 11, 79, 20, 82, 25, 86, 24, 94, 22, 96, 22, 97,
		26, 94, 31, 97, 35, 103, 39, 106, 43, 111, 41, 117, 43, 119, 41, 130,
		38, 136, 42, 141, 38, 145, 33, 143, 28, 141, 22, 141, 15, 139, 13, 134,
		14, 129, 20, 126, 20, 121, 24, 116, 28, 111, 26, 109, 20, 106, 19, 104,
		19, 102, 17, 99, 12, 95, 10, 92, 5, 90, 4, 87, 6, 84, 9 ];
var ps351012 = [ 174, 91, 174, 99, 170, 102, 167, 107, 166, 112, 165, 119, 168,
		121, 173, 122, 182, 117, 188, 114, 191, 108, 191, 103, 193, 99, 197,
		95, 199, 92, 201, 90, 202, 87, 202, 84, 200, 77, 200, 72, 202, 65, 203,
		61, 203, 59, 203, 56, 203, 53, 202, 50, 197, 51, 194, 52, 191, 52, 188,
		55, 188, 61, 188, 67, 189, 72, 189, 76, 189, 80, 184, 85, 182, 85, 177,
		82, 177, 82, 177, 84 ];
var ps351013 = [ 267, 71, 265, 81, 269, 86, 275, 86, 279, 84, 283, 81, 289, 81,
		294, 87, 300, 87, 302, 88, 314, 79, 314, 81, 307, 75, 306, 71, 308, 68,
		303, 65, 303, 64, 300, 65, 295, 69, 292, 69, 288, 66, 283, 63, 280, 64,
		280, 68, 272, 72 ];
var ps351014 = [ 185, 132, 176, 143, 174, 143, 169, 149, 174, 148, 177, 149,
		180, 151, 183, 151, 189, 152, 189, 156, 189, 161, 192, 166, 195, 169,
		198, 176, 202, 180, 204, 184, 207, 184, 209, 186, 211, 192, 212, 195,
		212, 197, 217, 198, 218, 193, 225, 187, 224, 184, 216, 180, 214, 179,
		216, 174, 206, 174, 203, 171, 207, 168, 210, 168, 211, 166, 209, 162,
		205, 160, 205, 157, 211, 156, 215, 156, 216, 153, 220, 150, 226, 150,
		229, 150, 237, 150, 236, 146, 236, 140, 239, 133, 235, 129, 231, 128,
		227, 128, 219, 132, 213, 131, 207, 133, 202, 130, 195, 126, 190, 126,
		190, 126, 189, 126, 184, 133, 184, 135, 181, 139 ];
var ps351015 = [ 111, 235, 103, 238, 108, 243, 102, 243, 100, 249, 104, 254,
		100, 258, 90, 261, 84, 272, 89, 278, 90, 280, 90, 286, 92, 287, 94,
		294, 94, 298, 96, 301, 101, 308, 105, 316, 109, 318, 114, 321, 118,
		321, 122, 329, 128, 325, 137, 317, 143, 312, 149, 312, 149, 312, 155,
		311, 160, 313, 166, 312, 169, 311, 172, 310, 170, 307, 165, 307, 161,
		307, 161, 304, 163, 296, 166, 291, 161, 292, 157, 296, 153, 304, 146,
		305, 139, 308, 127, 308, 119, 307, 116, 298, 116, 293, 116, 286, 118,
		283, 116, 282, 110, 282, 106, 282, 106, 280, 109, 277, 112, 277, 113,
		271, 106, 266, 107, 260, 108, 257, 115, 256, 120, 255, 124, 258, 125,
		263, 130, 262, 131, 253, 131, 250, 133, 246, 132, 244, 127, 244, 126,
		241, 128, 237, 128, 234, 125, 233, 122, 234, 116, 235 ];

// canV("canvas1",ps2);
var canvasImage = new Image();
// 赋值图片地址
/*canvasImage.src = "${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dipute_map.png";*/
function canV(canvasId, ps, color) {
	var canvas = document.getElementById(canvasId);

	if (canvas.getContext) {
		// 获取对应的CanvasRenderingContext2D对象(画笔)
		var ctx = canvas.getContext("2d");

		ctx.beginPath();
		// 设置线条颜色为蓝色
		ctx.strokeStyle = "#33ffff"; // 设置路径起点坐标
		ctx.fillStyle = color;
		ctx.moveTo(ps[0], ps[1]);
		var pslen = ps.length;
		ctx.globalAlpha = 0.9;// 透明度
		for ( var i = 2; i < pslen; i = i + 2)

		{

			ctx.lineTo(ps[i], ps[i + 1]);
		}

		// 先关闭绘制路径。注意，此时将会使用直线连接当前端点和起始端点。
		ctx.stroke();
		ctx.fill();

	}

	// 必须图片加载完后在进行绘图
	// canvasImage.onload = function() {
	// drawimage(this);
	// };

}
canV("c_351001", ps351001, "#314cee");
canV("c_351002", ps351002, "#314cee");
canV("c_351003", ps351003, "#314cee");
canV("c_351004", ps351004, "#314cee");
canV("c_351005", ps351005, "#314cee");
canV("c_351006", ps351006, "#314cee");
canV("c_351007", ps351007, "#314cee");
canV("c_351008", ps351008, "#314cee");
canV("c_351009", ps351009, "#314cee");
canV("c_351010", ps351010, "#314cee");
canV("c_351011", ps351011, "#314cee");
canV("c_351012", ps351012, "#314cee");
canV("c_351013", ps351013, "#314cee");
canV("c_351014", ps351014, "#314cee");
canV("c_351015", ps351015, "#314cee");
</script>
