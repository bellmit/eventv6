<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title>
	<script type="text/javascript">
		var js_ctx =  "${rc.getContextPath()}";
		var _myServer = "${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
		var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
	</script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/default/easyui.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/3.8/init.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsMap.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsMeasure.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSlider.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsOverviewMap.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsLayerPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsFillQuery.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsTianDiTuLayer.js"></script> 

	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/arcgis_demo.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/arcgis_draw.js"></script>
	
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<script src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
	<tr>
		<td>
			<input id="showSubAreaFlag" type="checkbox" onclick="changePolygon(this);" checked="checked"/>
			<label for="showSubAreaFlag" style="font-size:14px;">显示子级网格区划图</label>
		</td>
		<td class="itemtit150">
			<input type="hidden" id="orgLocationInfoJSONString"  name="orgLocationInfoJSONString" />
			<input type="hidden" id="orgId"  name="orgId" />
		</td>
	</tr>
</table>
<div class="page-container" id="map" style="position: absolute; top:30px;bottom:0px;right:0px; left:0px; z-index: 1;">
</div>
<div id="jsSlider"></div>
<div class="MapTools">
	<ul>
		<li class="ThreeWei" onclick="threeWeiClick()"></li>
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
<div style="height:0px">
<iframe id="cross_domain_frame" name="cross_domain_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>
</body>

<script type="text/javascript">
	 //经度
     var longitude = '<#if orgExtraInfo??><#if orgExtraInfo.longitude??>${orgExtraInfo.longitude?string('#.######')}</#if></#if>';         
     if(longitude == '') {
		longitude = 118.01333427429199;		
	 }
     //纬度
	 var latitude = '<#if orgExtraInfo??><#if orgExtraInfo.latitude??>${orgExtraInfo.latitude?string('#.######')}</#if></#if>';
	 if(latitude == '') {
		latitude = 24.472384796690427;					
	 }
	
	 // 地图缩放级别
	 var flag = '<#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??>${orgExtraInfo.mapCoordinate}</#if></#if>';
	 
	 if(flag == '') {		
		flag = 13;
	 }else {
	 	flag = parseInt(flag);
	 }

	 var subPolygonColors = ["#fc7272","#f7e689","#abe17f","#7fe1bd","#7fc4e1","#cb7fe1","#ee95b9","#da95ee"];
	 // 子中心点字符串
	 var subOrgLocationInfoJSONString = '<#if orgExtraInfo??><#if orgExtraInfo.subOrgExtraInfoStr??>${orgExtraInfo.subOrgExtraInfoStr}</#if></#if>';
	 var subOrgLocationInfoArray = jQuery.parseJSON(subOrgLocationInfoJSONString);
	 
	 var markers = new Array();
	 
     var polygon = '';
     var subPolygon = new Array();
     
     // 描点JSON数据
     var orgLocationInfoJSONString = '<#if orgExtraInfo??><#if orgExtraInfo.orgLocationInfoJSONString??>${orgExtraInfo.orgLocationInfoJSONString}</#if></#if>';
     var orgLocationInfoJSONArray;
	 if(orgLocationInfoJSONString=='') {
	 	orgLocationInfoJSONString = '[]';
	 }
	 orgLocationInfoJSONArray = jQuery.parseJSON(orgLocationInfoJSONString);
	 
	 // 渲染初始化
	 function init() {
	 	showSubpolygon();
		showPolygon();
	 
	 	var pointLayer = new esri.layers.GraphicsLayer();
	 	var imgUrl = '${rc.getContextPath()}/js/map/arcgis/library/style/images/location_point.png';
	 	var map = $.fn.ffcsMap.getMap();
	 	
	 	//显示子中心点标注
		 if(subOrgLocationInfoArray!=null && subOrgLocationInfoArray.length>0) {
		 	for(var i=0;i<subOrgLocationInfoArray.length;i++) {
		 		if(subOrgLocationInfoArray[i].latitude==null || subOrgLocationInfoArray[i].latitude=="" || subOrgLocationInfoArray[i].latitude<=0) continue;
		 		if(subOrgLocationInfoArray[i].longitude==null || subOrgLocationInfoArray[i].longitude=="" || subOrgLocationInfoArray[i].longitude<=0) continue;
		     	
		     	var orgId = subOrgLocationInfoArray[i].orgId;
				var orgName = subOrgLocationInfoArray[i].orgName;
				var mapPoint = new esri.geometry.Point(subOrgLocationInfoArray[i].longitude, subOrgLocationInfoArray[i].latitude);
		     	
		     	// 图片标注
		     	var symbol = new esri.symbols.PictureMarkerSymbol(imgUrl, 32, 32);
		     	var graphic = new esri.Graphic(mapPoint, symbol);
		     	graphic.setAttributes( {"orgId":orgId});
		     	
		     	// 文字标注
		     	var font = new esri.symbol.Font("12px", esri.symbol.Font.STYLE_NORMAL, esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
		     	var textSymbol = new esri.symbol.TextSymbol(orgName, font, new dojo.Color([204, 102, 51]));
		     	textSymbol.yoffset = -25;
				var textgraphic = new esri.Graphic(mapPoint, textSymbol);
				
				// 添加到地图上
				pointLayer.add(graphic);
				pointLayer.add(textgraphic);
				markers.push(mapPoint);
		     }
		 }
         
         // 绑定点事件
 		pointLayer.on("click", function(evt) {
 			var _graphic = evt.graphic;
 			var _orgId = _graphic.attributes.orgId;
 			
 			// 传递_orgId到firegrid供页面调用
 			var data = 'orgId='+_orgId;
			var url = '${SQ_FIREGRID_URL}/firegrid/orgconfig/toArcgisCrossDomainDisplay.jhtml?'+ data;
			$('#cross_domain_frame').attr('src', url);
		});
         
		map.addLayer(pointLayer);
	 }
	 
	 function showSubpolygon() {
	 	var map = $.fn.ffcsMap.getMap();
	 	var subPolygonGraphicsLayer = new esri.layers.GraphicsLayer({id:'subPolygonGraphicsLayer'});
	 		
	 	if(''!=subOrgLocationInfoJSONString) {		 		
	 		for(var i=0;i<subOrgLocationInfoArray.length;i++) {
	 			var subpoints = [];		 			
	 			if(subOrgLocationInfoArray[i].subOrgLocationInfo.length>0) {
	 				for(var j=0;j<subOrgLocationInfoArray[i].subOrgLocationInfo.length;j++) {
	 					var point = new esri.geometry.Point(subOrgLocationInfoArray[i].subOrgLocationInfo[j].longitude, subOrgLocationInfoArray[i].subOrgLocationInfo[j].latitude);
		 				subpoints.push(point);		 				
		 			}
		 			//再加第一个结点
		 			//subpoints.push(new google.maps.LatLng(subOrgLocationInfoArray[i].subOrgLocationInfo[0].latitude,subOrgLocationInfoArray[i].subOrgLocationInfo[0].longitude));
	 				var randNum=i%subPolygonColors.length;
	 				
	 				// 范围
	 				var subGPolygon = new esri.geometry.Polygon();
	 				subGPolygon.addRing(subpoints);
	 				
	 				var symbol = new esri.symbols.SimpleFillSymbol();
	 				// 设置颜色
	 				var rgbcolor = new dojo.Color(subPolygonColors[randNum]).toRgb();
	 				symbol.setColor(new dojo.Color([rgbcolor[0], rgbcolor[1], rgbcolor[2], 0.35]));
					var graphic = new esri.Graphic(subGPolygon, symbol);
					
					subPolygonGraphicsLayer.add(graphic);
					map.addLayer(subPolygonGraphicsLayer);
					subPolygon.push(subGPolygon);
	 			}
	 		}		 				 		
	 	}		 	
	 }
	 
	 function showPolygon() {
	 	var map = $.fn.ffcsMap.getMap();
	 	
	 	if(subPolygon != null && subPolygon.length>0) {
        	subPolygon = new Array();
        }
        //添加描点区域                
        var triangleCoords = [];                
	    //主网格区域点
	    var points = new Array();
	    if(orgLocationInfoJSONArray != null && orgLocationInfoJSONArray.length>0) {
	    	for(var i=0;i<orgLocationInfoJSONArray.length;i++) {
	    		var point = new esri.geometry.Point(orgLocationInfoJSONArray[i].longitude, orgLocationInfoJSONArray[i].latitude);
            	points.push(point);
            }
            
            points.push(new esri.geometry.Point(orgLocationInfoJSONArray[0].longitude,orgLocationInfoJSONArray[0].latitude));	
	    }
        
        // 范围
		var polygon = new esri.geometry.Polygon();
		polygon.addRing(points);
		
		var symbol = new esri.symbols.SimpleFillSymbol();
		var graphic = new esri.Graphic(polygon, symbol);
		
		var polygonGraphicsLayer = new esri.layers.GraphicsLayer({id:'polygonGraphicsLayer'});
		polygonGraphicsLayer.hide();
		polygonGraphicsLayer.add(graphic);
		map.addLayer(polygonGraphicsLayer);
	 }
	 
	 function changePolygon(obj) {
	 	var map = $.fn.ffcsMap.getMap();
	 	
	 	var subPolygonGraphicsLayer = map.getLayer("subPolygonGraphicsLayer");
	 	var polygonGraphicsLayer = map.getLayer("polygonGraphicsLayer");
	 	
	 	if(obj.checked==true) { 		//显示子级网格区域图	
	 		if(''!=polygon) {
	 			//polygon.setMap(null);
	 			//polygon = '';
	 		}
	 				 		
	 		if (subPolygonGraphicsLayer != null) {
		 		subPolygonGraphicsLayer.show();
	 		}

			if (polygonGraphicsLayer != null) {
		 		polygonGraphicsLayer.hide();
			}
	 	}else {						   //不显示子级网格区域图
            if (subPolygonGraphicsLayer != null) {
		 		subPolygonGraphicsLayer.hide();
	 		}
	 		
	 		if (polygonGraphicsLayer != null) {
		 		polygonGraphicsLayer.show();
			}
	 	}
	 }
</script>

<script>
	var flagPoint = false;
	jQuery(document).ready(function() {
		getArcgisInfo();
		init();
		setMapArcgisPoint();
	});
	 function threeWeiClick(){
	 	var mapStyleDiv = document.getElementById("mapStyleDiv");
	 	if(mapStyleDiv.style.display == 'none') {
	 		mapStyleDiv.style.display = 'block';
	 	}else {
	 		mapStyleDiv.style.display = 'none';
	 	}
	 }
	var currentMapStyleObj;
	//获取arcgis地图路径的配置信息
	function getArcgisInfo(){
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t='+Math.random(),
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			    arcgisConfigInfos=eval(data.arcgisConfigInfos);
			    var htmlStr = "";
			    for(var i=0; i<arcgisConfigInfos.length; i++){
			    	if(i==0){
			    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	}else (
			    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	)
			    }
			    var mapStyleDiv = document.getElementById("mapStyleDiv");
			    mapStyleDiv.innerHTML = htmlStr;
			    $("#mapStyleDiv").width(60*arcgisConfigInfos.length+8)
			    
			    if(htmlStr!=""){
			    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
			    }
			    
			    if(arcgisConfigInfos.length > 0) {
			    	loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap");
			    }
			 }
		 });
	}
	
	function switchArcgisByNumber(obj,n) {
		if(currentMapStyleObj != obj){
			currentMapStyleObj.className = "";
			obj.className = "current";
			currentMapStyleObj = obj;
			switchArcgis(arcgisConfigInfos[n]);
		}
	}
	//2014-05-26 liushi add获取加载地图的信息并且初始化地图信息对象
	function getMapArcgisInfo(){
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapArcgisInfo.json',
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			    var mapConfigInfo=eval(data.mapConfigInfo);
			    arcgisMapConfigInfo.Init(mapConfigInfo);
			    var mapt = $('#mapt').val();
			    if(mapt != null && mapt !='0') {//表示地图默认显示为2维
			    	if(mapt == '5') {
			    		currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
			    	}
			    }else {
			    	 if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
			    		currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
			   		 }else if(arcgisMapConfigInfo.mapStartType == "30") {//表示地图默认显示为2维
				    	currentArcgisMapInfo.Init3D(arcgisMapConfigInfo);
				    }
			    }
			    loadArcgisMap("map","jsSlider","vec");
			    
			 }
		 });
	}
	function setMapArcgisPoint() {
		$("#map").ffcsMap.centerAt({
			x : parseFloat(longitude),          //中心点X坐标
			y : parseFloat(latitude),          //中心点y坐标
			wkid : 4326, //wkid 2437
			zoom : flag
		});
	}
	
	function getOrgLocationInfoJSONString() {
	 	var jsonstr = '[]';
	 	if(markers != null && markers.length>0) {
	 		jsonstr = '[';
	 		for(var i=0;i<markers.length;i++) {
	 			var lat = markers[i].getLatitude();
				var lng = markers[i].getLongitude();
				var str = '';
				if(i+1<markers.length) {
					str += '{latitude:'+lat+',longitude:'+lng+',mapOrder:'+i+'},';
				}else {
					str += '{latitude:'+lat+',longitude:'+lng+',mapOrder:'+i+'}';
				}
				jsonstr += str;
	 		}
	 		jsonstr += ']';
	 	}     	
	 	return jsonstr;
	}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
