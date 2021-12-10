<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/openLayer/v3.13.1/css/ol.css">
    
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <script src="${rc.getContextPath()}/js/map/openLayer/v3.13.1/build/ol.js"></script>
    <script src="${rc.getContextPath()}/js/map/openLayer/ffcsPlugin/ffcsSlider.js"></script>
    
    <link rel="stylesheet" href="${ZZGRID_DOMAIN!''}/js/components/tip/css/tip.css" type="text/css"/>
	<script type="text/javascript" src="${ZZGRID_DOMAIN!''}/js/components/tip/jquery.anole.tip.js"></script>
	<title>olwfs加载页面</title>
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		.AlphaBack1 tr td{padding-top:2px;padding-right:2px;}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
		td{margin: 0;padding: 5px;list-style: none;}
		.ol-zoom{top : 3.5em}
		
		#gridDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:290px; display:none;}
		#gridDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#gridDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		
		var BUILDING_BIND_AID_DISPLAY = '${BUILDING_BIND_AID_DISPLAY!''}';
	</style>
</head>
<body style="width:100%;height:100%;border:none;">
	<input type="hidden" id="mapt" value="" disabled="true" />
	<input type="hidden" id="x" value="" readonly="true"/>
	<input type="hidden" id="y" value="" readonly="true"/>
	<input type="hidden" id="hs" value="" readonly="true"/>
	<input type="hidden" id="wid" value="" /> 
	<input type="hidden" id="showGridId" value="" /> 
	
	<div id="firstImgs" style="display: none;"></div>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          	<!--
	          	<td>
	          		<a href="#" onclick="relate()" class="NorToolBtn SmallSaveBtn">直接关联</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="editBoundary()" class="NorToolBtn EditBtn">轮廓重新编辑</a>
	    		</td>
	          	<td>
	          	 	<a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">关联编辑</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="choose('blindBuilding')" class="NorToolBtn EditBtn">轮廓绑定</a>
	    		</td>
	    		-->
	    		<td>
	          		<a href="#" onclick="saveBuildingData()" id="saveButton" class="NorToolBtn SmallSaveBtn" style="display:none;">保存</a>
	          	</td>
	    		<td>
	          	 	<a href="#" onclick="choose('drawPoint')" id="drawPoint" class="NorToolBtn SetCenterBtn">编辑中心点</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="choose('cancleDrawPoint')" id="cancleDrawPoint" class="NorToolBtn BackBtn" style="display:none;">取消中心点标注</a>
	    		</td>
	          	<td>
	          		<a href="#" onclick="choose('drawBuilding')" id="drawBuilding" class="NorToolBtn DrawBorderBtn">轮廓编辑</a>
	    		</td>
	          	<td>
	          		<a href="#" onclick="choose('cancleDrawOrEditBuilding')" id="cancleDrawOrEditBuilding" class="NorToolBtn BackBtn" style="display:none;">取消轮廓编辑</a>
	    		</td>
	          	<td>
	          		<a href="#" onclick="choose('delDrawData')" id="delDrawData" class="NorToolBtn DrawBorderBtn" style="display:none;">删除轮廓</a>
	    		</td>
	    		<!--
	          	<td>
	          		<a href="#" onclick="addModifyTool()" id="modifyButton" class="NorToolBtn EditBtn">轮廓编辑</a>
	    		</td>
	    		-->
	    		<#if BUILDING_BIND_AID_DISPLAY?? && BUILDING_BIND_AID_DISPLAY == '1'>
		          	<td>
		    	 		<div class="MapLevel fl" style="width:122px">
			    	 		<ul>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="gridDisplayChange();" /></li>
				                <li><a class="styleChangeA" onclick="gridDisplayChange();">辅助显示</a></li>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="gridDisplayChange();" /></li>
			            	</ul>
		       			 </div>
		    	 	</td>
	    	 	</#if>
	    	 	<td>
	    	 		<div id="tipMessage" style="color:yellow;font-size:13px"></div>
	 				<div class="blind" style="color:yellow;font-size:13px;float:right;"></div>
	    	 	</td>
	        </table>
	    </div>
		<div id="gridDisplayDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li>
	    			<span><input name="displayLevel0" type="checkbox" value="0" onclick="getArcgisDataOfGridsByLevel(0);" id="displayLevel0" /></span>所在网格轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayLevel2" type="checkbox" value="2" onclick="getAroundBuildingData();" id="displayLevel2" /></span>周边楼宇
	    		</li>
	    	</ul>
	    </div>
	</div>
	<div id="jsSlider"></div>
	<div class="MapTools">
		<ul>
			<li class="ThreeWei" onclick="changeMapClick()"></li>
	    </ul>
		<div id="mapStyleDiv" class="MapStyle" style="display:none;">
			<span class="current" onclick="switchMapType(this,1)">矢量图</span>
			<span onclick="switchMapType(this,2)">影像图</span>
		</div>
	</div>
	<div id="myMap" style="position:absolute; top:0px;bottom:0px;right:0px; left:0px; border: #ccc 1xp solid;z-index:-1">
		<div id="map" tabindex="0" class="page-container" style="position: absolute; width:100%; height:100%;"></div>
	</div>
<script type="text/javascript">
	var selectRowNum = 0;
	
	var iconLayer;//编辑中心点图层
	var centerLayer;//设置中心点图层
 	var buildingPolygonLayer;//楼宇轮廓图层
	var gridLayer; //所在网格图层
	
	var drawPointFlag = true;//是否编辑中心点操作标识
	var blindBuildingFlag = true;//是否绑定轮廓操作标识
	var showGridLayerFlag = false; //是否显示所在网格的轮廓标识
	var drawBuildingHSFlag = true;//绘制楼宇轮廓标识
	
	var drawBuilding;//绘制楼宇轮廓工具
	var drawPoint;//绘制中心点工具
	var selectClick;//选择楼宇轮廓工具
	var modifyTool;//编辑工具

	var tipMessage = ""; //提示信息;
	
	var saveCenterPointFlag = false; //是否保存中心点坐标
	var saveBuildingHSFlag = false   //是否保存楼宇轮廓信息
	
	var currentMapStyleObj;//当前地图对象
	var mapStyleDiv;//切换地图div
	var gridDisplayDiv;//辅助显示
	mapStyleDiv = document.getElementById("mapStyleDiv");
	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0];
	gridDisplayDiv = document.getElementById("gridDisplayDiv");
	
	/**
	 * openlayer方式加载地图
	 */
	ol.ProxyHost = '${rc.getContextPath()}/proxy.cgi?';
	var projection = ol.proj.get('EPSG:4326');
	var projectionExtent = projection.getExtent();
	var size = ol.extent.getWidth(projectionExtent) / 256;
	var resolutions = new Array(20);
	var matrixIds = new Array(20);
	for (var z = 0; z < 20; ++z) {
		resolutions[z] = (size / Math.pow(2, z))/2;
		matrixIds[z] = z;
	}
	//矢量底图1
	var jevc = new ol.layer.Tile({
		name:"baseVec",
		source: new ol.source.WMTS({
//			url: 'http://www.jjmap.gov.cn/dfc/services/ogc/wmts/JJVEC',
            url: 'http://10.45.67.228/dfc/services/ogc/wmts/JJVEC',//政务网
			layer: '0',
			matrixSet: 'EPSG:4326',
			format: 'image/png',
			projection: projection,
			tileGrid: new ol.tilegrid.WMTS({
				origin: ol.extent.getTopLeft(projectionExtent),
				resolutions: resolutions,
				matrixIds: matrixIds
			}),
			style: 'default',
			wrapX: true
		})
	});
	//矢量底图2
	var jcva = new ol.layer.Tile({
		name:"baseVec",
		source: new ol.source.WMTS({
//			url: 'http://www.jjmap.gov.cn/dfc/services/ogc/wmts/JJCVA',
            url: 'http://10.45.67.228/dfc/services/ogc/wmts/JJCVA',//政务网
			layer: '0',
			matrixSet: 'EPSG:4326',
			format: 'image/png',
			projection: projection,
			tileGrid: new ol.tilegrid.WMTS({
				origin: ol.extent.getTopLeft(projectionExtent),
				resolutions: resolutions,
				matrixIds: matrixIds
			}),
			style: 'default',
			wrapX: true
		})
	});
	
	//影像底图1
	var yxMapLayers1 = new ol.layer.Tile({
		name:"baseVec",
		preload: Infinity,
		visible: false,//默认先不显示
		source: new ol.source.WMTS({
			//本地开发环境测试使用
//			url: 'http://www.jjmap.gov.cn/dfc/services/ogc/wmts/JJIMG',
            url: 'http://10.45.67.228/dfc/services/ogc/wmts/JJIMG',//政务网
			layer: '0',
			matrixSet: 'EPSG:4326',
			format: 'image/png',
			projection: projection,
			tileGrid: new ol.tilegrid.WMTS({
				origin: ol.extent.getTopLeft(projectionExtent),
				resolutions: resolutions,
				matrixIds: matrixIds
			}),
			style: 'default',
			wrapX: true
		})
	});
	
	//影像底图2
	var yxMapLayers2 = new ol.layer.Tile({
		name:"baseVec",
		preload: Infinity,
		visible: false,//默认先不显示
		source: new ol.source.WMTS({
			//本地开发环境测试使用
//			url: 'http://www.jjmap.gov.cn/dfc/services/ogc/wmts/JJCIA',
            url: 'http://10.45.67.228/dfc/services/ogc/wmts/JJCIA',//政务网
			layer: '0',
			matrixSet: 'EPSG:4326',
			format: 'image/png',
			projection: projection,
			tileGrid: new ol.tilegrid.WMTS({
				origin: ol.extent.getTopLeft(projectionExtent),
				resolutions: resolutions,
				matrixIds: matrixIds
			}),
			style: 'default',
			wrapX: true
		})
	});
	
	//第三方轮wfs服务
	var vectorSource = new ol.source.Vector({
		format: new ol.format.GML2(),
		url: function(extent) {
			//var url;
			//本地开发环境测试使用
			//var url = 'http://222.77.98.122:8090/servicemanager/wfsproxy/1345?REQUEST=GetFeature&VERSION=1.0.0&SERVICE=WFS&TYPENAME=SGS:LINE_1692&BBOX=79,18,130,49&MaxFeatures=100&outputFormat=application/json&key=c1e7b8ef52c3db3f984c397893538f8e';
			//现网使用地址
			var url = 'http://10.45.67.228/services/ogc/wfs/QingXinSheQuFangWuShiTi?service=WFS&request=GetFeature&version=1.0.0&TYPENAME=SGS:REGION_1740&' +
              'BBOX=118.5660876,24.807199243000095,118.569817774,24.811835467&MaxFeature=100';
			url = '${rc.getContextPath()}/proxy.jsp?' + url;
			return url;
        	/**
          	return 'http://10.45.67.228/services/ogc/wfs/QingXinSheQuFangWuShiTi?service=WFS&request=GetFeature&version=1.0.0&TYPENAME=SGS:REGION_1740&' +
              'BBOX=118.5660876,24.807199243000095,118.569817774,24.811835467&MaxFeature=100';
              **/
		},
		strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({
			maxZoom: 20
		}))
	});
	
	var vector = new ol.layer.Vector({
		source: vectorSource,
		style: new ol.style.Style({
			stroke: new ol.style.Stroke({
	 			color: 'rgba(0, 0, 255, 1.0)',
				width: 1
			})
		})
	});

	var map = new ol.Map({
		logo:false,
		layers: [
			jevc,
			jcva,
			yxMapLayers1,
			yxMapLayers2,
			vector
		],
		target: 'map',
		controls: ol.control.defaults({
			attributionOptions: ({
				collapsible: false
			}),
			zoom : false //去除地图上放大缩小的加号和减号
			
		}),
		view: new ol.View({
			<#if mapCenterX?? && mapCenterY??>
				center: [${mapCenterX}, ${mapCenterY}],
	        <#else>
	        	center: [118.561844204, 24.761048980000098],
	        </#if>
			projection:ol.proj.get("EPSG:4326"),
			zoom: 14,
            minZoom: 10,
            maxZoom: 19
		})
	});
	$(function(){
		 $("#jsSlider").ffcsSlider({
		 	map:map,
		 	currentLevel: 14,
			minLevel: 10,
			maxLevel: 19
		 });
	});
	/* 放大
	var view = map.getView();
	var zoom = view.getZoom();
	view.setZoom(zoom + 1);
	
	//缩小
	var view = map.getView();
	var zoom = view.getZoom();
	view.setZoom(zoom - 1);
	 */
	
	/*添加缩放滑动控件
	map.addControl(new ol.control.ZoomSlider({
		duration : 150,
		maxResolution : 20,
		minResolution : 1
	}));*/
	//设置地图双击放大
	var doubleClickZoom = new ol.interaction.DoubleClickZoom({
		duration : -1,
		delta: 0.000000000001//放大层级
    });
    map.addInteraction(doubleClickZoom);
	
	//添加选中工具
	function addSelectClickTool(){
		/**
		 * 添加选中对象方法
		 */
		selectClick = new ol.interaction.Select({
			condition: ol.events.condition.click,
			style: new ol.style.Style({//选中后对象的样式
	            stroke: new ol.style.Stroke({
					color: 'red',
					width: 1
				}),
				fill: new ol.style.Fill({
					color: 'rgba(0, 0, 255, 0.4)'
				})
	         })
		});
		
		map.addInteraction(selectClick);
		
		selectClick.on('select', function(e) {
			var opt = e;
			selectFeature = opt.selected[0];
			if(opt.selected.length > 0 && drawPointFlag == true && blindBuildingFlag == true){
				var obj = opt.selected[0].getGeometry();
				var polygonArry = obj.getCoordinates();
				var hs = '';
				//获取轮廓的数据
				var sumX=0,sumY=0,sumLength=0;
				for(var i = 0; i < polygonArry.length; i++){
					var polygonoObj = polygonArry[i];
					for(var j = 0; j < polygonoObj.length; j++){
						var polygonoObj2 = polygonoObj[j];
						if(polygonoObj2.length >= 3 && typeof(polygonoObj2[2]) == 'Array'){
							sumLength += polygonoObj2.length;
							for(var k = 0; k < polygonoObj2.length;k++){
								if(hs != null && hs != ''){
									hs = hs + ",";
								}
								//if(k == 0){//取第一个点为中心点
								//	$("#x").val(polygonoObj2[k][0]);
								//	$("#y").val(polygonoObj2[k][1]);
								//}
								sumX +=  Number(polygonoObj2[k][0]);
								sumY +=  Number(polygonoObj2[k][1]);
								hs = hs + polygonoObj2[k][0] + ',' + polygonoObj2[k][1];
							}
						}
					}
				}
			
				if(hs == null || hs == ""){
					sumX=0,sumY=0,sumLength=0;
					for(var i = 0; i < polygonArry.length; i++){
						var polygonoObj = polygonArry[i];
						sumLength += polygonoObj.length;
						for(var k = 0; k < polygonoObj.length; k++){
						
							if(hs != null && hs != ''){
								hs = hs + ",";
							}
							//if(k == 0){//取第一个点为中心点
							//	$("#x").val(polygonoObj[k][0]);
							//	$("#y").val(polygonoObj[k][1]);
							//}
							sumX +=  Number(polygonoObj[k][0]);
							sumY +=  Number(polygonoObj[k][1]);
							hs = hs + polygonoObj[k][0] + ',' + polygonoObj[k][1];
						}
					}
				}
				//计算中心点坐标
				var x = sumX/sumLength;
				var y = sumY/sumLength;
				$("#x").val(x);
				$("#y").val(y);
				
				if(hs != null && hs != ''){
					$("#hs").val(hs);
				}
				var x = $("#x").val();
				var y = $("#y").val();
				var hs = $("#hs").val();
				var buildingId = $('#wid').val();
                saveCenterPointFlag = true;
				saveData('blindBuilding',buildingId, x, y, hs);
			}
		});
	}
	
	//移除选中工具
	function removeSelectClickTool(){
		if(typeof(selectClick) != 'undefined'){
			map.removeInteraction(selectClick);
			selectClick = "";
		}
	}
	
	/**
	 * 选择轮廓后进行绑定
	 */
	var selectFeature; 
	var deselectFeatureStyle = new ol.style.Style({//选中后对象的样式
        stroke: new ol.style.Stroke({
			color: 'rgba(0, 0, 255, 1.0)',
			width: 1
		}),
		fill: new ol.style.Fill({
			color: 'rgba(0, 0, 255, 0)'
		})
    });
	function setSelectFeatureStyle(){
		if(typeof(selectFeature) != 'undefined'){
			//selectFeature.setStyle(deselectFeatureStyle);
			//暂时先不用
		}
	}
	
	//添加标注中心点工具
	function addDrawPointTool(){
		var geometryFunction, maxPoints;
		
		var iconStyle = new ol.style.Style({
			size : [5, 5]
		});
		drawPoint = new ol.interaction.Draw({
			source: source,
			type: 'Point',
			style : iconStyle,
			geometryFunction: geometryFunction
		});
		map.addInteraction(drawPoint);
	
		/*获取当前鼠标点击位置的经纬度*/
		map.addEventListener("click",function(e){ 
			
			if(drawPointFlag == true && blindBuildingFlag == false){
				map.removeLayer(centerLayer);
				map.removeLayer(iconLayer);
				var x = e.coordinate[0];
				var y = e.coordinate[1];
				if(x != null && x != ''){
					$("#x").val(x);
				}
				if(y != null && y != ''){
					$("#y").val(y);
				}
				var buildingId = $('#wid').val();
				var x = $("#x").val();
				var y = $("#y").val();
				
				var iconStyle = new ol.style.Style({
					image: new ol.style.Icon(({
				    	src: '${rc.getContextPath()}/images/RedShinyPin.png'
				  	}))
				});
				
				var iconFeature = new ol.Feature({
				 	geometry: new ol.geom.Point([x, y])
				});
				
				iconFeature.setStyle(iconStyle);
				
				iconLayer = new ol.layer.Vector({
			    	source: new ol.source.Vector({
			        	features: [iconFeature]
			    	})
				});
				map.addLayer(iconLayer);
				saveCenterPointFlag = true;
				removeAllTools();
			}
		});
	}
	
	/**
	 * 清除标注中心点状态
	 */
	function reomveDrawPointTool(){
		map.removeInteraction(drawPoint);
		drawPoint = "";
	}
	
	//添加绘制楼宇轮廓工具
	function addDrawBuildingTool(){
		var value = 'Polygon';
		features = new ol.Collection();
		featureOverlay = new ol.layer.Vector({
	        source: new ol.source.Vector({features: features}),
	        style: new ol.style.Style({//画完多边形后的样式
	        	fill: new ol.style.Fill({
	        		color: 'rgba(171, 171, 171, 0.3)'//多边形画完后，里面那块区域的填充色，参数：三个是rgb色；最后一个是透明度，值越小，越透明。
				}),
				stroke: new ol.style.Stroke({
					color: 'red',//多边形画完后，边框线条的颜色
					width: 2     //多边形画完后，边框线条的粗细
				})
			})
      	});
      	map.addLayer(featureOverlay);
		
		drawBuilding = new ol.interaction.Draw({
			features: features,
			type: (value),
			style: new ol.style.Style({//画多边形时的样式
	        	fill: new ol.style.Fill({
	        		color: 'rgba(171, 171, 171, 0.3)'//画多边形时，里面那块区域的填充色，参数：三个是rgb色；最后一个是透明度，值越小，越透明。
				}),
				stroke: new ol.style.Stroke({
					color: 'red',//画多边形时，边框线条的颜色
					width: 2     //画多边形时，边框线条的粗细
				})
			})
		});
		//画完多边形后促发的事件
		drawBuilding.on('drawend', function(evt){
			var feature = evt.feature;
			var p = feature.getGeometry();
			var hs = '';
			var sumX=0,sumY=0,sumLength=0;
			
			for(var i=0; i< p.getCoordinates().length; i++){
				var hsArray = p.getCoordinates()[i];
				sumLength += p.getCoordinates()[i].length;
				for(var j=0; j < hsArray.length; j++){
					if(hs != null && hs != ''){
						hs = hs + ",";
					}
					hs += hsArray[j][0] + ',' + hsArray[j][1];
					sumX +=  Number(hsArray[j][0]);
					sumY +=  Number(hsArray[j][1]);
				}
			}
			//计算中心点坐标
			var x = sumX/sumLength;
			var y = sumY/sumLength;
			$("#x").val(x);
			$("#y").val(y);
			$("#hs").val(hs);
			saveCenterPointFlag = true;
			removeDrawBuildingTool();
		});
		map.addInteraction(drawBuilding);
	}
	
	
	/**
	 * 解除绘制楼宇轮廓状态
	 */
	function removeDrawBuildingTool(){
		map.removeInteraction(drawBuilding);
		drawBuilding = "";
	}
	
	//清除所有的工具
	function removeAllTools(){
		removeDrawBuildingTool();
		reomveDrawPointTool();
		removeSelectClickTool();
		removModifyTool();
	}
	
	function initButton(){
		$('#drawBuilding').show();
		$('#drawPoint').show();
		$('#saveButton').hide();
		$('#cancleDrawPoint').hide();
		$('#cancleDrawOrEditBuilding').hide();
	}
	
	function setMapCenterPointIcon(x, y, targetType){
		map.removeLayer(centerLayer);
		map.removeLayer(iconLayer);
		var iconFeature = new ol.Feature({
		 	geometry: new ol.geom.Point([x, y])
		});
		
		if(targetType != null && targetType != '' && typeof(targetType) != 'undefined' && targetType == 'build'){
			var iconStyle = new ol.style.Style({
				image : new ol.style.Icon(({
					    	src: '${rc.getContextPath()}/images/RedShinyPin.png'
					  	})),
				size : [1, 10]
			});
			iconFeature.setStyle(iconStyle);
		}else{
			var iconStyle = new ol.style.Style({
				image: new ol.style.Icon(({
			    	//src: '${rc.getContextPath()}/images/RedShinyPin.png'
			  	}))
			});
			iconFeature.setStyle(iconStyle);
		}
		
		
		centerLayer = new ol.layer.Vector({
	    	source: new ol.source.Vector({
	        	features: [iconFeature]
	    	})
		});
		map.addLayer(centerLayer);
	}
	
	//添加编辑工具
	function addModifyTool(){
		map.removeLayer(centerLayer);
		drawPointFlag = false;
		blindBuildingFlag = false;
		drawBuildingHSFlag = true;
		$('#saveButton').show();
		removeSelectClickTool();
		selectClick = new ol.interaction.Select({
			wrapX: false
		});
		
		map.addInteraction(selectClick);
	
		modifyTool = new ol.interaction.Modify({
			features: selectClick.getFeatures()
		});
		
		modifyTool.on('modifyend',function(evt){
			var feature = evt.features;
			var p = feature.getArray()[0].getGeometry();
			var hs = '';
			var sumX=0,sumY=0,sumLength=0;
			
			for(var i=0; i< p.getCoordinates().length; i++){
				var hsArray = p.getCoordinates()[i];
				sumLength += p.getCoordinates()[i].length;
				for(var j=0; j < hsArray.length; j++){
					if(hs != null && hs != ''){
						hs = hs + ",";
					}
					hs += hsArray[j][0] + ',' + hsArray[j][1];
					sumX +=  Number(hsArray[j][0]);
					sumY +=  Number(hsArray[j][1]);
				}
			}
			//计算中心点坐标
			var x = sumX/sumLength;
			var y = sumY/sumLength;
			$("#x").val(x);
			$("#y").val(y);
			$("#hs").val(hs);
			saveCenterPointFlag = true;
		});
		map.addInteraction(modifyTool);
	}
	
	//移除编辑工具
	function removModifyTool(){
		removeSelectClickTool();
		map.removeInteraction(modifyTool);
		modifyTool = '';
	}
	
	 
	
	
</script>

</body>
<script type="text/javascript">
	

	/**
	 * 开始标注中心点，添加标注状态
	 */
	var source = new ol.source.Vector({wrapX: false});
	
	function drawCenterPoint() {
		var buildingId = $('#wid').val();
		if(buildingId != null && buildingId != ''){
			drawPointFlag = true;
			addDrawPointTool();
		}else{
			DivShow('请选择要标注的楼宇!');
			$('#drawBuilding').show();
			$('#drawPoint').show();
			$('#saveButton').hide();
			$('#cancleDrawPoint').hide();
			$('#cancleDrawOrEditBuilding').hide();
		}
	}

	/**
	 * 保存数据
	 */
	function saveData(source, buildingId, x, y, hs){
		$("#tipMessage").html("");
		$('#saveButton').hide();
		var data = 'wid='+buildingId+'&x='+x+'&y='+y+'&hs='+hs+'&mapt=5&saveCenterPointFlag='+saveCenterPointFlag;
		saveCenterPointFlag = false;
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/updateMapDataOfBuildOfBinding.json?'+data;
		if(buildingId != null && buildingId !=''){
			if (source == 'blindBuilding'){
				tipMessage = "确定要进行此绑定操作吗？";
			}else if(source == 'saveButton'){
				tipMessage = "保存楼宇轮廓信息？";
			}
			if(confirm(""+tipMessage+"")){
                if(drawBuildingHSFlag || blindBuildingFlag){
                    var markerImgId = "markerImg_"+selectRowNum;
                    window.parent.document.getElementById(markerImgId).innerHTML = '<img src="${rc.getContextPath()}/images/tj_wg_80.png" class="FontDarkBlue" style="float:left" title="已绑定">';
                }
				$.ajax({   
					url: url,
					type: 'POST',
					timeout: 3000, 
					dataType:"json",
					async: false,
					error: function(data){
						$.messager.alert('提示','操作报错!','warning');
					},
					success: function(data){
						$('#drawPoint').show();
						$('#cancleDrawPoint').hide();
						$('#drawBuilding').show();
						$('#cancleDrawOrEditBuilding').hide();
					
						if(data.flag == true) {
							DivShow('操作成功!'); 
							//$.messager.alert('提示','操作成功!','info');
						}else {
							DivShow('操作失败!'); 
							//$.messager.alert('提示','操作失败!','info');
						}
						$("#hs").val("");
						
						/** 绑定或绘制楼宇轮廓后不再去刷新列表
						if(drawPointFlag){
							
						}else{
							var pageNum = parent.document.getElementById("pagination-num").textContent;
	        				var pageSize = parent.document.getElementById("pageSize").value;
							parent.loadMessage(pageNum,pageSize);
						}
						**/
						getBuildingHSById();
						//setMapCenterXY();
						setMapCenterPointIcon(x, y, 'build');
						removeAllTools();
						addSelectClickTool();
					}
				});
				if(drawPointFlag == true && blindBuildingFlag == false){
					drawPointFlag = false;
				} else {
					drawPointFlag = true;
				}
			} else {
				
		    }
		}else{
			DivShow("请选择要绑定的楼宇轮廓!");
		}
	}

	/**
	 * 选择功能（绑定楼宇、标注中心点）
	 */
	function choose(type){
		if(type != null && type != ''){
			removeAllTools();
			if(type == "drawPoint"){
				$('#saveButton').show();
				drawPointFlag = true;
				blindBuildingFlag = false;
				drawBuildingHSFlag = false;
				$('#drawPoint').hide();
				$('#drawBuilding').hide();
				$('#cancleDrawPoint').show();
				drawCenterPoint();
			}
			if(type == 'drawBuilding'){
				$('#saveButton').show();
				$('#drawPoint').hide();
				$('#drawBuilding').hide();
				$('#cancleDrawOrEditBuilding').show();
				var hs = parent.document.getElementById("selectBuildingHS").value;
				if(hs != null && hs != '' && typeof(hs) != 'undefined'){
					addModifyTool();//编辑轮廓
				}else{//绘制轮廓
					drawPointFlag = false;
					blindBuildingFlag = false;
					drawBuildingHSFlag = true;
					drawBuildingHS();
				}
			}
			if(type == "cancleDrawPoint"){
				$('#drawBuilding').show();
				$('#drawPoint').show();
				$('#saveButton').hide();
				$('#cancleDrawPoint').hide();
				drawPointFlag = false;
				removeAllTools();
				setMapCenterXY();
			}
			if(type == "cancleDrawOrEditBuilding"){
				$('#drawBuilding').show();
				$('#drawPoint').show();
				$('#saveButton').hide();
				$('#cancleDrawPoint').hide();
				$('#cancleDrawOrEditBuilding').hide();
				removeAllTools();
				getBuildingHSById();
			}
			if(type == "delDrawData"){
				$('#drawBuilding').show();
				$('#drawPoint').show();
				$('#saveButton').hide();
				$('#cancleDrawPoint').hide();
				$('#cancleDrawOrEditBuilding').hide();
				delBuildingHSData();
				removeAllTools();
				map.removeLayer(iconLayer);
				map.removeLayer(gridLayer);
			}
			
			 /**********
			 else if(type == "blindBuilding"){
				$.messager.alert('提示','请在地图上选择要绑定的楼宇轮廓!','info');
				drawPointFlag = false;
				blindBuildingFlag = true;
				removeAllTools();
			}
			***********/
		}
	}
	
	/**
	 * 根据网格id获取中心点坐标
	 */
	function setMapCenterXY(){
		clearPointIcon();
		var url = "";
		var wid = $('#gridId').val();
		var targetType = window.parent.document.getElementById('targetType').value;
		if(targetType != null && targetType != '' && typeof(targetType) != 'undefined' && targetType == 'build'){
			wid = $('#wid').val();
		}else {
			wid = window.parent.document.getElementById('gridId').value;
			targetType = 'grid';
		}
		var param = "?mapt=5&wid="+wid+"&targetType="+targetType;
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json";
		url = url+param;
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	alert('网格地图信息获取出现异常，请联系维护人员处理！'); 
			 },
			 success: function(data){
			 	var obj = data.result;
			 	if(obj != null) {
			 		$("#x").val(obj.x);
					$("#y").val(obj.y);
					var mapCenterLevel = 13;
					if(obj.mapCenterLevel != null){
						mapCenterLevel = obj.mapCenterLevel;
					}
					
					setMapCenterPointIcon(obj.x, obj.y, targetType)
					
					map.setView(new ol.View({
						center: [obj.x,obj.y],
						projection:ol.proj.get("EPSG:4326"),
						zoom: mapCenterLevel,
                        minZoom: 10,
                        maxZoom: 19
					}));

			 	}
			 }
		 });
	}
	
	/**
	 * 清除中心点标注图标
	 */
	function clearPointIcon(){
		map.removeLayer(iconLayer);
	}
	/**
	 * 在地图上画出多边形区域
	 */
	function getBuildingPolygon(){
		//清除绘制楼宇轮廓后留下的图层
		clearDrawBuildingPolygon();
		removeBuildingPolygonLayer();
		var hs = parent.document.getElementById("selectBuildingHS").value;
		
		if(hs != null && hs != '' && typeof(hs) != 'undefined'){
			$('#delDrawData').show();
		}else{
			$('#delDrawData').hide();
		}
		
		var pointList = [];
		var polygon_points = hs.split(",");
		var geojsonObject;
		for(var i=0;i<polygon_points.length;i++){
			var pointX, pointY;
			if(i%2==0){
				pointX = polygon_points[i];
				pointY = polygon_points[i+1];
				pointList.push([pointX,pointY]);
			}
			i++;
		}
		
		var geojsonObject = {
	    	'type': 'FeatureCollection',
	    	'crs': {
		      	'type': 'name',
		      	'properties': {
		        	'name': 'EPSG:3857'
		      	}
	    	},
	    	'features': [{
	      		'type': 'Feature',
	      		'geometry': {
	        		'type': 'Polygon',
	        		'coordinates': [pointList]
	      		}
	    	}]
	  	};
	  	
	  	var source = new ol.source.Vector({
			features: (new ol.format.GeoJSON()).readFeatures(geojsonObject)
		});
		
		var styles = [
			new ol.style.Style({
				stroke: new ol.style.Stroke({
					color: 'red',
					width: 1
				}),
				fill: new ol.style.Fill({
					color: 'rgba(0, 0, 255, 0.1)'
				})
			})
		];
		
		buildingPolygonLayer = new ol.layer.Vector({
			source: source,
			style: styles
		});
		
		map.addLayer(buildingPolygonLayer);
	}
	
	/**
	 * 清楚楼宇的轮廓图层
	 */
	function removeBuildingPolygonLayer(){
		map.removeLayer(buildingPolygonLayer);
	}
	

	//辅助显示
	
	function gridDisplayChange(){
	 	if(gridDisplayDiv.style.display == 'none') {
	 		gridDisplayDiv.style.display = 'block';
	 	}else {
	 		gridDisplayDiv.style.display = 'none';
	 	}
	 }
 
 	//显示所在网格轮廓
	function getArcgisDataOfGridsByLevel(level){
		if(window.document.getElementById("displayLevel"+level).checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt=5&showType=0&gridId='+window.document.getElementById("showGridId").value;
		 	$.ajax({
		 		url: url,
				type: 'POST',
				dataType:"json",
				error: function(data){
					$.messager.alert('提示','操作报错!','warning');
				},
				success: function(data){
					if(data.length > 0) {
						showGridLayerFlag = true;
						removeGridLayer();//先清除其他的网格轮廓
						getResourcePolygon(data[0].hs, 'gridLayer');
					}else{
						removeGridLayer();
					}
				}
		 	});
		 	
		}else{
			removeGridLayer();
		}
	}
	 
	/**
	 * 清除网格轮廓
	 */
	function removeGridLayer(){
		map.removeLayer(gridLayer);
	}
 
	/**
	 * 在地图上画出多边形区域
	 */
	function getResourcePolygon(hs,showType){
		var pointList = [];
		var polygon_points = hs.split(",");
		var geojsonObject;
		for(var i=0;i<polygon_points.length;i++){
			var pointX, pointY;
			if(i%2==0){
				pointX = polygon_points[i];
				pointY = polygon_points[i+1];
				pointList.push([pointX,pointY]);
			}
			i++;
		}
		  
		var geojsonObject = {
	    	'type': 'FeatureCollection',
	    	'crs': {
		      	'type': 'name',
		      	'properties': {
		        	'name': 'EPSG:3857'
		      	}
	    	},
	    	'features': [{
	      		'type': 'Feature',
	      		'geometry': {
	        		'type': 'Polygon',
	        		'coordinates': [pointList]
	      		}
	    	}]
	  	};
	  	
	  	var source = new ol.source.Vector({
			features: (new ol.format.GeoJSON()).readFeatures(geojsonObject)
		});
		
		var styles = [
			new ol.style.Style({
				stroke: new ol.style.Stroke({
					color: 'red',
					width: 1
				}),
				fill: new ol.style.Fill({
					color: 'rgba(0, 0, 255, 0.1)'
				})
			})
		];
		if(showType == "gridLayer"){
			gridLayer = new ol.layer.Vector({
				source: source,
				style: styles
			});	
			map.addLayer(gridLayer);
		}
	}
	
	//绘制楼宇轮廓
	var featureOverlay;
	var features;
	function drawBuildingHS(){
		var buildingId = $('#wid').val();
		if(buildingId != null && buildingId != ''){
			//清除绘制的轮廓
			clearDrawBuildingPolygon();
			addDrawBuildingTool();
		}else{	
			$('#drawBuilding').show();
			$('#drawPoint').show();
			$('#saveButton').hide();
			$('#cancleDrawPoint').hide();
			$('#cancleDrawOrEditBuilding').hide();
			DivShow('请选择要绘制轮廓的楼宇!');
		}
	}
	
	
	function clearDrawBuildingPolygon(){
		//清除绘制的轮廓
		features = "";
		map.removeInteraction(drawBuilding);
		map.removeLayer(featureOverlay);
	}
	
	
	
	//保存绘制的数据
	function saveBuildingData(){
		var buildingId = $('#wid').val();
		var hs = $("#hs").val();
		var x = $("#x").val();
		var y = $("#y").val();

		drawBuildingHSFlag = false;
		blindBuildingFlag = true;
		saveData('saveButton',buildingId, x, y, hs);
	}
	
	function DivHide(){
		$(".blind").hide();//窗帘效果展开
	}
	function DivShow(msg){
		$(".blind").html(msg);
		$(".blind").show();//窗帘效果展开
		setTimeout("this.DivHide()",2000);
	}
	
	/**
	 * 根据楼宇id获取楼宇轮廓
	 */ 
	function getBuildingHSById(){
		$('#saveButton').hide();
		$("#tipMessage").html("");
		getAroundBuildingData();
		var url = "";
		var buildingId = $('#wid').val();
		var param = "?wid="+buildingId+"&mapt=5";
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";
		url = url+param;
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	alert('获取楼宇轮廓信息出现异常，请联系维护人员处理！'); 
			 },
			 success: function(data){
			 	if(data.list.length>0) {
			 		//$('#modifyButton').show();
			 		window.parent.document.getElementById("selectBuildingHS").value = data.list[0].hs;
			 	} else {
			 		//$('#modifyButton').hide();
			 		tipMessage = "未画楼宇轮廓！";
					window.document.getElementById("tipMessage").innerHTML = tipMessage;
			 	}
			 	//画楼宇轮廓
		 		getBuildingPolygon();
			 }
		 });
	}
	
	/**
	 * 根据楼宇id删除楼宇轮廓和中心点
	 */ 
	function delBuildingHSData(){
		$('#saveButton').hide();
		$("#tipMessage").html("");
		getAroundBuildingData();
		var url = "";
		var buildingId = $('#wid').val();
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
    	var data = "targetType=build&wid="+ buildingId +"&mapt=5";
		if(!confirm('确定要删除?')){
    		return false;
    	}
    	var type = 1;
    	var layerName="";
		layerName = "buildLayer";
		type = 1
    	
    	
    	$.ajax({
		   type: "POST",
		   url: url,
		   data: data,
		   dataType:"json",
		   success: function(data){
				DivShow('删除成功！'); 
				$('#delDrawData').hide();
				var markerImgId = "markerImg_"+selectRowNum;
				window.parent.document.getElementById(markerImgId).innerHTML = "";
				parent.document.getElementById("selectBuildingHS").value = "";
				//$("#markerImg_" + selectRowNum).html('<img class="FontDarkBlue" src="${rc.getContextPath()}/images/tj_wg_80.png" style="float:left;border-width:0;" title="已标注">');
			    //清除绘制楼宇轮廓后留下的图层
				clearDrawBuildingPolygon();
				removeBuildingPolygonLayer();
        		addSelectClickTool();
		   },
		   error:function(msg){
    			DivShow('删除失败！');
		   }
		});
	}
	
	
	//周边楼宇
	function getAroundBuildingData() {
		var buildingId = $('#wid').val();

		if(typeof buildingId != 'undefined' && buildingId != null && buildingId != ''){
            if(window.document.getElementById("displayLevel2").checked == true) {
                map.removeLayer(arroudBuildingsViewLayer);
                map.removeLayer(arroudBuildingsPolygonLayerLayer);
                var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfBuilds.json?mapt=5&buildingId='+buildingId;
                $.ajax({
                    url: url,
                    type: 'POST',
                    timeout: 3000,
                    dataType:"json",
                    async: false,
                    error: function(data){
                        alert('获取周边楼宇轮廓信息出现异常，请联系维护人员处理！');
                    },
                    success: function(data){
                        if(data.length>0) {
                            setPointsView(data);
                        }
                    }
                });
            }else{
                map.removeLayer(arroudBuildingsViewLayer);
                map.removeLayer(arroudBuildingsPolygonLayerLayer);
            }
		}else{
            alert('请先选择一个楼宇！');
		}

	}
	
	var highlightStyleCache = {};
	var arroundBuildingFeatureOverlay = new ol.layer.Vector({
		source: new ol.source.Vector(),
		map: map,
		style: function(feature, resolution) {
			var text = resolution < 5000 ? feature.get('name') : '';
			if (!highlightStyleCache[text]) {
				highlightStyleCache[text] = new ol.style.Style({
					stroke: new ol.style.Stroke({
						color: '#f00',
						width: 1
					}),
					fill: new ol.style.Fill({
						color: 'rgba(255,0,0,0.1)'
					}),
					text: new ol.style.Text({
						font: '12px Calibri,sans-serif',
						text: text,
						fill: new ol.style.Fill({
							color: '#f00'
						})
					})
				});
			}
        	return highlightStyleCache[text];
		}
	});
	
	var highlight;
	var displayFeatureInfo = function(pixel) {

		var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
			return feature;
		});

		if (feature !== highlight) {
			if (highlight) {
				arroundBuildingFeatureOverlay.getSource().removeFeature(highlight);
			}
			if (feature) {
				arroundBuildingFeatureOverlay.getSource().addFeature(feature);
			}
			highlight = feature;
		}
	};
	
	var arroudBuildingsViewLayer;//周边楼宇中心点图层
	var arroudBuildingsPolygonLayerLayer;//周边楼宇轮廓图层
	function setPointsView(datas){
		//周边楼宇中心点定位图标
		var imageurl="${uiDomain!''}/images/map/gisv0/map_config/unselected/build/build_locate_point.png";
		if(datas != null && datas != '' && typeof(datas) != 'undefined' && datas.length > 0){
			var centerPointsList = [];
			var featuresObjList = [];
			
			var centerPointsFeatures = [];
			for(var i = 0; i < datas.length; i++){
				//获取中心点坐标
				var centerPointX = datas[i].x;
				var centerPointY = datas[i].y;
				
				var iconFeature = new ol.Feature({
				 	geometry: new ol.geom.Point([centerPointX,centerPointY])
				});
				//周边楼宇中心点样式
				var iconStyle = new ol.style.Style({
					image : new ol.style.Icon(({
						    	src: '${rc.getContextPath()}/images/map/openlayers/build_locate_point.png'
						  	})),
					size : [1, 10]
				});
				iconFeature.setStyle(iconStyle);
				centerPointsFeatures.push(iconFeature);
				
				var hs = datas[i].hs;
				var pointList = [];
				var polygon_points = hs.split(",");
				var geojsonObject;
				for(var j = 0;j < polygon_points.length; j++){
					var pointX, pointY;
					if(j%2==0){
						pointX = polygon_points[j];
						pointY = polygon_points[j+1];
						pointList.push([pointX,pointY]);
					}
					j++;
				}
				var featureObj = {
					'type': 'Feature',
		      		'geometry': {
		        		'type': 'Polygon',
		        		'coordinates': [pointList]
		      		},
		      		"properties": {
		      			"name": datas[i].buildingName
		      		}
				}
				featuresObjList.push(featureObj);
			}
			
			//地图添加周边楼宇中心点图层
			arroudBuildingsViewLayer = new ol.layer.Vector({
		    	source: new ol.source.Vector({
		        	features: centerPointsFeatures
		    	})
			});
			map.addLayer(arroudBuildingsViewLayer);
			
			
			var geojsonObject = {
		    	'type': 'FeatureCollection',
		    	'crs': {
			      	'type': 'name',
			      	'properties': {
			        	'name': 'EPSG:3857'
			      	}
		    	},
		    	'features': featuresObjList
		  	};
		  	 
		  	
		  	var source = new ol.source.Vector({
				features: (new ol.format.GeoJSON()).readFeatures(geojsonObject)
			});
			
			var styles = [
				new ol.style.Style({
					stroke: new ol.style.Stroke({
						color: 'red',
						width: 1
					})
				})
			];
			
			arroudBuildingsPolygonLayerLayer = new ol.layer.Vector({
				source: source,
				style: styles
			});
			
			map.addLayer(arroudBuildingsPolygonLayerLayer);
			
			map.on('pointermove', function(evt) {
				if (evt.dragging) {
					return;
				}
				var pixel = map.getEventPixel(evt.originalEvent);
				displayFeatureInfo(pixel);
			});
		}
	} 
	
	//地图切换
	function changeMapClick(){
	 	if(mapStyleDiv.style.display == 'none') {
	 		mapStyleDiv.style.display = 'block';
	 	}else {
	 		mapStyleDiv.style.display = 'none';
	 	}
	 	
	    $("#mapStyleDiv").width(60*2+8);
	}
	
	
	function switchMapType(obj,type){
		mapStyleDiv.style.display = 'none';
		if(currentMapStyleObj != obj){
			currentMapStyleObj.className = "";
			obj.className = "current";
			currentMapStyleObj = obj;
		}
		if(type=="1"){
	    	jevc.setVisible(true);
	    	jcva.setVisible(true);
	    	yxMapLayers1.setVisible(false);
	    	yxMapLayers2.setVisible(false);
	    }else if(type=="2"){
	    	jevc.setVisible(false);
	    	jcva.setVisible(false);
	    	yxMapLayers1.setVisible(true);
	    	yxMapLayers2.setVisible(true);
	    }
	}
	
	$('body').initAnoleTip({
		Top : '1px',
		Right : '10px',
		RenderType : 0,
		Url : "${ZZGRID_DOMAIN!''}/zzgl/systemPrompts/getSystemPromptsInfoForJsonp.jhtml"
	});
	

</script>
</html>