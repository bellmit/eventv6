<div class="danger-map" style="padding-left:365px; padding-top:6px;"> 
	<canvas id="c_341602002013001" style="position:absolute ; z-index:10 " width="400px" height="320px" ></canvas>
	<canvas id="c_341602002013002" style="position:absolute ; z-index:11"  width="400px" height="320px" ></canvas>
    <canvas id="c_341602002013003" style="position:absolute ; z-index:12"  width="400px" height="320px" ></canvas>
	<canvas id="c_341602002013004" style="position:absolute ; z-index:13"  width="400px" height="320px" ></canvas>
    <canvas id="c_341602002013005" style="position:absolute ; z-index:14"  width="400px" height="320px" ></canvas>
</div>
<script>
//富荣花园
var ps341602002013001 = [ 39, 92, 17, 100, 6, 104, 7, 119, 14, 123, 20, 126, 22, 129,
		56, 129, 71, 129, 80, 128, 102, 129, 115, 128, 151, 123, 157, 122, 170,
		124, 181, 124, 194, 122, 206, 119, 243, 122, 246, 123, 254, 123, 258,
		119, 258, 109, 254, 92, 248, 82, 241, 80, 217, 78, 164, 84, 133, 82,
		111, 84, 70, 89 ];
// 蚂蚱庙
var ps341602002013002 = [ 269, 38, 258, 56, 247, 79, 252, 88, 254, 99, 257, 107, 257,
		113, 263, 119, 275, 119, 290, 124, 308, 123, 329, 126, 342, 126, 361,
		127, 373, 123, 385, 122, 386, 111, 388, 87, 384, 81, 385, 64, 384, 48,
		374, 31, 363, 24, 350, 21, 334, 20, 312, 22, 293, 29 ];
// 马元
var ps341602002013003 = [ 15, 127, 15, 143, 19, 153, 27, 162, 29, 175, 176, 192, 181,
		180, 188, 172, 194, 170, 207, 167, 214, 169, 232, 171, 229, 150, 224,
		140, 191, 136, 184, 133, 171, 130, 162, 123, 143, 122, 129, 126, 108,
		126, 66, 126, 43, 127, 15, 126, 14, 129 ];
// 天润花园+职业技术学院
var ps341602002013004 = [ 112, 277, 166, 291, 184, 292, 227, 297, 251, 302, 272, 302,
		309, 264, 304, 248, 301, 225, 296, 214, 274, 210, 247, 205, 226, 202,
		200, 203, 172, 204, 162, 193, 138, 192, 97, 188, 35, 180, 35, 184, 44,
		210, 57, 224, 67, 242, 80, 250, 94, 263 ];
// 将门楼
var ps341602002013005 = [ 364, 184, 346, 202, 339, 219, 332, 244, 311, 263, 302, 237,
		296, 212, 240, 205, 200, 200, 178, 202, 174, 193, 180, 184, 189, 173,
		201, 172, 230, 170, 225, 142, 176, 132, 168, 122, 182, 124, 200, 122,
		230, 122, 250, 122, 261, 118, 278, 124, 294, 125, 348, 129, 365, 127,
		378, 124, 383, 124, 389, 127, 368, 180 ];

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
canV("c_341602002013001", ps341602002013001, "#314cee");
canV("c_341602002013002", ps341602002013002, "#314cee");
canV("c_341602002013003", ps341602002013003, "#314cee");
canV("c_341602002013004", ps341602002013004, "#314cee");
canV("c_341602002013005", ps341602002013005, "#314cee");

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
</script>