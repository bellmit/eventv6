let earth = null;

let mapConfig = {
	centerX : 110.39161455408801,
	centerY : 18.8119663645267,
	centerZ : 500,
	EPSG : "4326"
};

$(function() {
	
	initPage();
	
	init3DMap();
});

function init3DMap() {
	let cfg = {
		dom : document.getElementById('glCanvas'),
		key : 'wSryzKApfH8y',
		EngineIP : 'http://115.236.87.26:9047',
		ResourcesIP : 'http://115.236.87.26:9999/wllhy',
		hideBGTile : true,
		hideSky : true
	};
	earth = new Earth(cfg);
	earth.open();
	earth.on('EarthFinish', function() {
		earth.Api.Camera.setLookDistance(50, 2000);
		earth.confg.ClickColor = 'ffb075';
		var point1 = new earth.Api.Type.Point(mapConfig.centerX, mapConfig.centerY, mapConfig.centerZ, mapConfig.EPSG);
		earth.Api.Light.add('Point', point1, null, {
			color : '#ffffff',
			distance : 9000,
			intensity : 2.6,
			decay : 0.5
		});
		earth.Api.Light.updateDirLight({
			visible : true,
			intensity : 0.7
		});
		var point = new earth.Api.Type.Point(110.39161455408801, 18.8119663645267, 80, mapConfig.EPSG);
		point.changeEPSGType('3857');
		earth.controls.ViewControl.dirLight.target.position.copy(point);
		
		addMarkerLayer("buildingLayer");
		
		point = new earth.Api.Type.Point(110.39062997774141, 18.81104387349039, 1, mapConfig.EPSG);
		addMarkerPointForCHE("buildingLayer", point);
		
		// 设置3D气泡
		earth.Api.Pop.setPop("buildingLayer", function(result) {
			var html = '<div class="dev-card-line" style="display:block;z-index:1;background:url(' + uiDomain + '/web-assets/_big-screen/wanning/images/operating-center/icon-dev-card-jy.png) no-repeat"></div><div class="dev-card" style="display:block;">\
				<canvas class="devbg1"></canvas>\
				<div class="dev-card-con">\
					<div class="dev-card-hd">\
						<p>警员</p>\
						<i></i>\
					</div>\
					<div class="dev-card-bd">\
						<span class="dev-card-pic">\
							<img src="' + uiDomain + '/web-assets/_big-screen/wanning/images/operating-center/icon-dev-card-pic1.png" />\
						</span>\
						<ul class="dev-card-items">\
							<li>\
								<p>编号：</p>\
								<b>9527</b>\
							</li>\
							<li>\
								<p>姓名：</p>\
								<b>周星星</b>\
							</li>\
							<li>\
								<p>呼号：</p>\
								<b>008</b>\
							</li>\
							<li>\
								<p>装备：</p>\
								<b>要你命三千</b>\
							</li>\
						</ul>\
					</div>\
					<div class="dev-card-ft">\
						<a href="javasript:;" class="dev-card-btn">任务下派</a>\
						<a href="javasript:;" class="dev-card-btn ">视频连线</a>\
						<a href="javasript:;" class="dev-card-btn">语音通话</a>\
					</div>\
				</div>\
			</div>';
			$(result).html(html);
			setTimeout(operDev, 10);
			// operDev();
			// result.innerHTML = html;
			// result.appendChild($(document).append(html));
		});
	});
	
	
}

function initPage() {
	
	$(window).on('load resize', function() {
		fullPage();
		setTimeout(fullPage, 10);// 二次执行页面缩放，解决全屏浏览时滚动条问题
	});
	
	// nav切换
	$('.oper-nav').on('click', '.oper-nav-item', function() {
		$(this).addClass('oper-nav-active').siblings().removeClass('oper-nav-active');
	});
	
	// tabs切换
	$('.oper-tabs').on('click', '.oper-tabs-item', function() {
		$(this).addClass('oper-tabs-active').siblings().removeClass('oper-tabs-active');
	});

}
//创建可点击3D图层
function addMarkerLayer(layerName) {
	var markerLayer = new earth.Api.Layers.ActiveLayer({
		objType : layerName
	}, earth);
	var lc = earth.getControl('LayerControl');
	lc.addLayer(markerLayer);
}
// 添加CHE模型标注
function addMarkerPointForCHE(layerName, point, cfg) {
	// 这几个属性是必须有的
	var monitorCfg = {
		data : {
			angle : 0,//模型角度
			id : '1',
			name : '游客中心正门枪立杆枪机',
			fbx : './images/JT/CHE.FBX',
			repeat_rate : "1,1,1",// 模型大小
			pause_time : 0,
			fbx_speed : 0.01,
			color : '#ff0000'
		},
		jsType : "Fbx",// 类型
		shapes : [ point ]
	};
	if (cfg) monitorCfg = $.extend(true, monitorCfg, cfg);
	let fbx = new earth.Api.Models.Fbx(monitorCfg, earth);
	// let obj = new earth.Api.Models.Obj(monitorCfg, earth);
	var layer = earth.Api.Layers.getLayer(layerName);
	layer.add(fbx);
}

var winW, scale, winH;
function fullPage() {// 将页面等比缩放
	winW = $(window).width();
	winH = $(window).height();
	if (winW < 1000) {
		winW = 1000;
	}
	var whdef = 100 / 1920;
	var rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
	$('html').css('font-size', rem + "px");
	scale = winW / 1920;
	$('.container-fluid').css({
		'width' : 1920 * scale,
		'height' : winH,
		'overflow' : 'hidden'
	});
	if ((!!window.ActiveXObject || "ActiveXObject" in window)) {
		$('.container-fluid').css({
			'width' : 1920 * scale,
			'height' : winH - 1,
			'overflow' : 'hidden'
		});
	}
}

var borCor, devbg1, devW, devH;
function operDev() {
	devbg1 = document.querySelector('.dev-card canvas'),
	devW = document.querySelector('.dev-card').clientWidth,
	devH = document.querySelector('.dev-card').clientHeight;

	borCor = '#2fd1ff';

	devbg1.width = devW;
	devbg1.height = devH;

	var ctx = devbg1.getContext('2d');
	ctx.beginPath();
	// 填充背景
	ctx.fillStyle = 'rgba(0, 24, 43, .7)';

	// 绘制边框
	ctx.moveTo(10, 15);
	ctx.lineTo(160 * scale, 15);
	ctx.lineTo(175 * scale, 30);
	ctx.lineTo(devW - 30, 30);
	ctx.lineTo(devW - 15, 45);
	ctx.lineTo(devW - 15, devH - 15);
	ctx.lineTo(25, devH - 15);
	ctx.lineTo(10, devH - 30);
	ctx.lineTo(10, 45);
	ctx.lineTo(5, 40);
	ctx.lineTo(5, 15);
	ctx.lineTo(10, 15);
	// 绘制边框颜色
	ctx.strokeStyle = borCor;
	ctx.lineWidth = '2';
	ctx.stroke();
	// 阴影
	ctx.shadowBlur = 8;
	ctx.shadowColor = borCor;

	ctx.fill();
}


