<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>智慧万宁-城市运行中心</title>
		<script type="text/javascript">
			var uiDomain = "${uiDomain!''}";
			var js_ctx =  "${SQ_EVENT_URL}";
		</script>
		<!--引入 重置默认样式 statics/zhxc -->
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/wanning/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/wanning/css/operating-center.css"/>
		<!-- 3D地图文件 -->
		<script src="http://115.236.87.26:9999/wllhy/DDEarth20190327.js"></script>
		<script src="http://115.236.87.26:9999/wllhy/DDEarthPlus20190401.js"></script>
		<!--引入 重置默认样式 statics/basic -->
		<script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${rc.getContextPath()}/map/ddearth/3DBiz.js"></script>
	</head>
	<body>
		<div class="container-fluid">
			<!--地图层（预留）start-->
			<div id="glCanvas" class="container-map" style="background:none;">
			</div>
			<!--地图层（预留）end-->
			
			<!--页面内容 start-->
			<div class="container-con">
				<!--logo-->
				<div class="oper-logo">
					<img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-oper-logo.png" />
				</div>
				
				<!--nav-->
				<div class="oper-nav clearfix">
					<a href="javascript:;" class="oper-nav-item oper-nav-active">
						<b>总体概况</b>
						<i></i>
					</a>
					<a href="javascript:;" class="oper-nav-item">
						<b>治安态势</b>
						<i></i>
					</a>
					<a href="javascript:;" class="oper-nav-item">
						<b>智能布控</b>
						<i></i>
					</a>
					<a href="javascript:;" class="oper-nav-item">
						<b>区域安防</b>
						<i></i>
					</a>
					<a href="javascript:;" class="oper-nav-item">
						<b>重大活动</b>
						<i></i>
					</a>
				</div>
				
				<!--tabs-->
				<div class="oper-tabs">
					<a href="javascript:;" class="oper-tabs-item oper-tabs-active">
						<div class="oper-tabs-icon">
							<i><img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-tabs-jb.png" /></i>
						</div>
						<p>基本信息</p>
					</a>
					<a href="javascript:;" class="oper-tabs-item">
						<div class="oper-tabs-icon">
							<i><img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-tabs-rk.png" /></i>
						</div>
						<p>人口信息</p>
					</a>
					<a href="javascript:;" class="oper-tabs-item">
						<div class="oper-tabs-icon">
							<i><img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-tabs-cl.png" /></i>
						</div>
						<p>车辆信息</p>
					</a>
					<a href="javascript:;" class="oper-tabs-item">
						<div class="oper-tabs-icon">
							<i><img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-tabs-sd.png" /></i>
						</div>
						<p>水、电、天然气</p>
					</a>
					<a href="javascript:;" class="oper-tabs-item">
						<div class="oper-tabs-icon">
							<i><img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-tabs-shlj.png" /></i>
						</div>
						<p>生活垃圾</p>
					</a>
				</div>
				
				<!--数据展示-->
				<div class="oper-data">
					<p class="oper-data-hd">万利隆花园</p>
					<div class="oper-data-map">
						<img src="${uiDomain!''}/web-assets/_big-screen/wanning/images/operating-center/icon-oper-data-map.png">
					</div>
					<div class="oper-data-con">
						<div class="oper-data-text">
							<b>楼盘名</b>
							<em>万利隆花园</em>
						</div>
						<div class="oper-data-text">
							<b>楼盘名</b>
							<em>万利隆花园</em>
						</div>
						<div class="oper-data-text">
							<b>城区</b>
							<em>万城镇城北新区</em>
						</div>
						<div class="oper-data-text">
							<b>占地面积</b>
							<em>92亩</em>
						</div>
						<div class="oper-data-text">
							<b>建筑面积</b>
							<em>13.3万平方米</em>
						</div>
						<div class="oper-data-text">
							<b>开发商</b>
							<em>万宁万利隆投资有限公司</em>
						</div>
						<div class="oper-data-text">
							<b>物业公司</b>
							<em>万宁万利隆投资有限公司</em>
						</div>
						<div class="oper-data-text">
							<p>简介</p>
							<p>位于万宁市万城镇城北新区，小区南临环市二路，东临纵一路，西临人民公园，坐拥十万平米的苍翠绿意，东山岭、轻轨铁路万宁站尽在咫尺，地理位置得天独厚。</p>
							<p>小区设施齐全，配套游泳池、篮球场、会所、幼儿园等运动娱乐设施，星级物业管理公司提供最完善的周到管理服务。</p>
						</div>
					</div>
				</div>
				
			</div>
			<!--页面内容 end-->
		</div><!--container-fluid-->
	</body>
</html>
