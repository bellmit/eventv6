<!DOCTYPE html>
<html>
<head> 
	<title>空气质量</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
	
  	    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/yanping/public.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/yanping/wrap_right.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/yanping/jquery.bxslider.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/style/css/public.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/style/css/wrap_right.css"/>
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/js/yanping/jquery.SuperSlide.2.1.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/js/yanping/stopcar.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

	
</head>
<body onload="setInterval('getAllParkPointByIds(\'${uiDomain!''}\')',60000)">
		<div class="container clearfix"  style="height:372px !important" >
			
			
						<p class="wrap_header mt20 header_text fl">空气质量指数</p>
						<div class="header_count">
							<p >更新时间：<span id="aqi-chart-nr"></span></p>
						</div>
						<div class="wrap_slider">
							<div class="slider_l">
								<div class="slider_l_t"><img src="${rc.getContextPath()}/images/yanping/icon_kqzl.png"/></div><!--表盘色值#c1ff7f, #fff17f, #ff9452, #ff5252, #c4266d, #7d0038; 指针色值#7fafff -->
								<p class="slider_l_h">AQI<span>优</span></p>
								<p class="slider_l_c">延平区实时空气质量指数</p>
							</div>
							<div class="slider_r">
								<ul class="slider_r_t">
									<li class="active">AQI</li>
									<li>PM 2.5</li>
									<li>PM 10</li>
									<li>首要污染物</li>
								</ul>
								<ul class="slider_r_b">
									<li class="active">
										<ul class="slider_r_c">
											<li class="slider_bg1">
												<p><span id="aqi0"></span>μg/m³</p>
												<h5>南平市监测站</h5>
											</li>
											<li class="slider_bg2">
												<p><span id="aqi1"></span>μg/m³</p>
												<h5>南平铝业</h5>
											</li>
											<li class="slider_bg3">
												<p><span id="aqi2"></span>μg/m³</p>
												<h5>南平七中</h5>
											</li>
											<li class="slider_bg4">
												<p><span id="aqi3"></span>μg/m³</p>
												<h5>茫荡山</h5>
											</li>
										</ul>
									</li>
									<li>
										<ul class="slider_r_c">
											<li class="slider_bg2">
												<p><span id="pm250"></span>μg/m³</p>
												<h5>南平市监测站</h5>
											</li>
											<li class="slider_bg1">
												<p><span id="pm251"></span>μg/m³</p>
												<h5>南平铝业</h5>
											</li>
											<li class="slider_bg3">
												<p><span id="pm252">101</span>μg/m³</p>
												<h5>南平七中</h5>
											</li>
											<li class="slider_bg4">
												<p><span id="pm253">160</span>μg/m³</p>
												<h5>茫荡山</h5>
											</li>
										</ul>
									</li>
									<li>
										<ul class="slider_r_c">
											<li class="slider_bg1">
												<p><span id="pm100"></span>μg/m³</p>
												<h5>南平市监测站</h5>
											</li>
											<li class="slider_bg3">
												<p><span id="pm101"></span>μg/m³</p>
												<h5>南平铝业</h5>
											</li>
											<li class="slider_bg2">
												<p><span id="pm102"></span>μg/m³</p>
												<h5>南平七中</h5>
											</li>
											<li class="slider_bg4">
												<p><span id="pm103"></span>μg/m³</p>
												<h5>茫荡山</h5>
											</li>
										</ul>
									</li>
									<li>
										<ul class="slider_r_c">
											<li class="slider_bg5">
												<p><span id="main0" style="font-size:20px"></span></p>
												<h5>南平市监测站</h5>
											</li>
											<li class="slider_bg5">
												<p><span id="main1" style="font-size:20px"></span></p>
												<h5>南平铝业</h5>
											</li>
											<li class="slider_bg5">
												<p><span id="main2" style="font-size:20px"></span></p>
												<h5>南平七中</h5>
											</li>
											<li class="slider_bg5">
												<p><span id="main3" style="font-size:20px"></span></p>
												<h5>茫荡山</h5>
											</li>
										</ul>
									</li>
								</ul>
							
				</div>
			</div>
		</div>
	</body>
<script type="text/javascript">
	$(document).ready(function() {
			var aqi_map = ${aqi_map};
			var kq_list =${kq_list}; // [{"seqid":7052589,"stationName":"省外办","statioid":"1290A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"102","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"33","so224":"22","no2":"33","no224":"34","pm10":"128","pm1024":"138","pm25":"76","pm2524":"59","co":"0.997","co24":"1.045","o3":"130","o38":"70","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"421.290644","y":"8870.378206","longitude":"","dimensions":"","opdate":null},{"seqid":7052590,"stationName":"省林业公司","statioid":"1291A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"98","state":"2","stateName":"良","airclass":"","className":"二级","so2":"30","so224":"41","no2":"20","no224":"36","pm10":"107","pm1024":"160","pm25":"73","pm2524":"72","co":"0.603","co24":"0.945","o3":"131","o38":"76","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"-3264.512044","y":"9715.449895","longitude":"","dimensions":"","opdate":null},{"seqid":7052591,"stationName":"林科所","statioid":"1292A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"127","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"45","so224":"21","no2":"22","no224":"33","pm10":"195","pm1024":"136","pm25":"96","pm2524":"72","co":"1.256","co24":"1.165","o3":"166","o38":"81","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"11121.302094","y":"12577.775403","longitude":"","dimensions":"","opdate":null},{"seqid":7052592,"stationName":"京东镇政府","statioid":"1293A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"92","state":"2","stateName":"良","airclass":"","className":"二级","so2":"43","so224":"42","no2":"14","no224":"25","pm10":"126","pm1024":"114","pm25":"68","pm2524":"73","co":"0.887","co24":"1.001","o3":"162","o38":"76","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"6406.144862","y":"13138.531715","longitude":"","dimensions":"","opdate":null},{"seqid":7052593,"stationName":"建工学校","statioid":"1294A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"103","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"37","so224":"28","no2":"24","no224":"59","pm10":"126","pm1024":"162","pm25":"77","pm2524":"62","co":"0.924","co24":"0.997","o3":"134","o38":"47","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"-1171.957097","y":"12199.266689","longitude":"","dimensions":"","opdate":null},{"seqid":7052594,"stationName":"象湖","statioid":"1295A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"128","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"41","so224":"21","no2":"49","no224":"68","pm10":"205","pm1024":"216","pm25":"63","pm2524":"75","co":"2.077","co24":"1.564","o3":"99","o38":"36","mainFomite":"颗粒物(PM10)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"8389.513083","y":"9149.538857","longitude":"","dimensions":"","opdate":null},{"seqid":7052595,"stationName":"武术学校","statioid":"1296A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"68","state":"2","stateName":"良","airclass":"","className":"二级","so2":"10","so224":"5","no2":"8","no224":"9","pm10":"61","pm1024":"82","pm25":"49","pm2524":"36","co":"0.541","co24":"0.57","o3":"79","o38":"34","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"-3768.449974","y":"7513.036264","longitude":"","dimensions":"","opdate":null},{"seqid":7052596,"stationName":"石化","statioid":"1297A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"142","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"43","so224":"26","no2":"45","no224":"53","pm10":"177","pm1024":"186","pm25":"108","pm2524":"107","co":"1.397","co24":"1.574","o3":"160","o38":"62","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"4574.951533","y":"12817.032258","longitude":"","dimensions":"","opdate":null},{"seqid":7052597,"stationName":"省站","statioid":"1298A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"90","state":"2","stateName":"良","airclass":"","className":"二级","so2":"46","so224":"42","no2":"21","no224":"31","pm10":"129","pm1024":"120","pm25":"65","pm2524":"61","co":"1.063","co24":"0.887","o3":"149","o38":"74","mainFomite":"颗粒物(PM10)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"7699.246515","y":"9972.466179","longitude":"","dimensions":"","opdate":null}];
			var nowtime= '${nowtime}';
			KQ_layer3(aqi_map, kq_list, nowtime);
			
				});
	
	
	function KQ_layer3(aqi_map, kq_list, nowtime){
	
	var str = nowtime ;
	
	$("#aqi-chart-nr").html(str);
	
	
	for ( var i = 0; i < kq_list.length; i++) {
	
	   var aqi = "#aqi"+i;
	   var pm25 = "#pm25"+i;
	   var pm10 = "#pm10"+i;
	   var main = "#main"+i;
	  
	  
	 if(kq_list[i].aqi ==null){
	 
	 $(aqi).html("无");
	 
	 }else{
	 
	  $(aqi).html(kq_list[i].aqi);
	  
	  }
	 
	 
	 if(kq_list[i].pm25 ==null){
	 
	  $(pm25).html("无");
	  
	  } else {
	  
	  
	 $(pm25).html(kq_list[i].pm25);
	 
	 }
	  
	  
	 if(kq_list[i].pm10 ==null){
	 
	 $(pm10).html("无");
	 
	 }else{
	 
	  $(pm10).html(kq_list[i].pm10);
	 
	 }
	  
	 if(kq_list[i].mainFomite ==null){
	 
	 $(main).html("无");
	 
	 }else{
	 
	
	 
	  $(main).html(kq_list[i].mainFomite);
        
       }
       }
	
}
</script>

<script src="${uiDomain!''}/js/yanping/index.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="${uiDomain!''}/js/style/js/index.js"></script>
</html>
