<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车闸列表</title>
<#include "/component/commonFiles-1.1.ftl" />

<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
<#include "/component/ImageView.ftl" />
<style>

</style>
</head>
<body>
	<div id="firstImgs">
		<ul>
			<li><a onclick="ffcs_viewImg_win('aaa')"><img class="pic" onload="AutoResizeImage(300,300,this)" src="${imgUrl!''}" alt="" /></a></li>
	    </ul>
	</div>
</body>
<script type="text/javascript">

$(function(){
	initNbspSlider();
// 	ImageViewApi.initImageView('aaa','${imgUrl!''}');
});

function ffcs_viewImg_win(fieldId){
	var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId + "&paths=${imgUrl!''}";
	var name = "图片查看";
	window.open(url,name);
}

//初始化图片轮播
function initNbspSlider(){
	$("#firstImgs").nbspSlider({
		widths:         "280px",        // 幻灯片宽度
		heights:        "260px",
		effect:	         "vertical",
		numBtnSty:       "square",
		speeds:          300,
		autoplay:       1,
		delays:         4000,
		preNexBtnShow:   0/*,
		altOpa:         0.5,            // ALT区块透明度
		altBgColor:     '#ccc',         // ALT区块背景颜色
		altHeight:      '20px',         // ALT区块高度
		altShow:         1,             // ALT区块是否显示(1为是0为否)
		altFontColor:    '#000',        // ALT区块内的字体颜色
		prevId: 		'prevBtn',      // 上一张幻灯片按钮ID
		nextId: 		'nextBtn'		// 下一张幻灯片按钮I
		*/
	});
}

function AutoResizeImage(maxWidth, maxHeight, objImg) {
	var img = new Image();
	img.src = objImg.src;
	var hRatio;
	var wRatio;
	var Ratio = 1;
	var w = img.width;
	var h = img.height;
	wRatio = maxWidth / w;
	hRatio = maxHeight / h;
	if (maxWidth == 0 && maxHeight == 0) {
		Ratio = 1;
	} else if (maxWidth == 0) {//
		if (hRatio < 1)
			Ratio = hRatio;
	} else if (maxHeight == 0) {
		if (wRatio < 1)
			Ratio = wRatio;
	} else if (wRatio < 1 || hRatio < 1) {
		Ratio = (wRatio <= hRatio ? wRatio : hRatio);
	}
	if (Ratio < 1) {
		w = w * Ratio;
		h = h * Ratio;
	}
	objImg.height = h;
	objImg.width = w;
}
</script>
</html>