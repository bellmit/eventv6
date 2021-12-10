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
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.MapBar{height:32px; position:absolute; z-index:10; top:0; left:0; width:100%; font-size:12px; line-height:32px; color:#fff;}
		.MapBar .con{height:32px; width:100%;}
		.MapBar td{height:32px;}
		.MapBar td span{color:#FF3;}
		.AlphaBack{background-color:rgba(0, 0, 0, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c000000',endColorstr='#8c000000');}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.btn1{width:60px; height:28px; line-height:26px; text-align:center;}
	</style>
</head>

<body>
<div class="MapBar">
	<div class="con AlphaBack">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="24" align="center"><input type="checkbox" name="checkbox" id="checkbox" style="margin-top:0px;" onclick="changePolygon(this);" checked="checked"></td>
            <td width="140"><span>显示子级网格区划图</span></td>
            <td width="40">名称：</td>
            <td width="120"><input type="text" class="inp1" id="orgName" name="orgName" value="<#if orgExtraInfo??><#if orgExtraInfo.orgName??>${orgExtraInfo.orgName}</#if></#if>"></td>
            <td width="40">经度：</td>
            <td width="120"><input type="text" class="inp1" id="x" name="longitude" value="<#if orgExtraInfo??><#if orgExtraInfo.longitude??>${orgExtraInfo.longitude?string('#.######')}</#if></#if>"></td>
            <td width="40">纬度：</td>
            <td width="120"><input type="text" class="inp1" id="y" name="latitude" value="<#if orgExtraInfo??><#if orgExtraInfo.latitude??>${orgExtraInfo.latitude?string('#.######')}</#if></#if>"></td>
            <td width="70"><input name="" type="button" value="保存" class="btn1" onclick="save()"></td>
            <td width="70"><input name="" type="button" value="编辑" class="btn1" onclick="edit()"></td>
            <td width="70"><input name="" type="button" value="重绘" class="btn1" onclick="redraw()"></td>
          </tr>
        </table>
    </div>
</div>
<!--
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
	<tr>
		<td>
			<input id="showSubAreaFlag" type="checkbox" onclick="changePolygon(this);" checked="checked"/>
			<label for="showSubAreaFlag" style="font-size:14px;">显示子级网格区划图</label>
		</td>
		<td class="itemtit150">
			<input id="savegridmapbtn" name="savegridmapbtn" type="button" onclick="save()" value="保存" class="btn_save" />
			<input id="editgridmapbtn" name="editgridmapbtn" type="button" onclick="edit()" value="编辑" />
			<input id="redrawgridmapbtn" name="redrawgridmapbtn" type="button" onclick="redraw()" value="重绘" />
			<input type="hidden" id="orgLocationInfoJSONString"  name="orgLocationInfoJSONString" />
			<input type="hidden" id="orgId"  name="orgId" />
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
	<tr class="item">
		<td class="itemtit">&nbsp;名称<font color='red'>*</font>：</td>
		<td class="border_b">&nbsp; <input type="text" class="w150" id="orgName" name="orgName" value="<#if orgExtraInfo??><#if orgExtraInfo.orgName??>${orgExtraInfo.orgName}</#if></#if>"/>
		
		<td class="itemtit">&nbsp;简介<font color='red'>*</font>：</td>
		<td class="border_b">&nbsp;<span>					
			<input type="text"  class="w150"  id="orgContent"  name="orgContent" value="<#if orgExtraInfo??><#if orgExtraInfo.orgContent??>${orgExtraInfo.orgContent}</#if></#if>"/>					
		</span>
		</td>
		<td class="itemtit">&nbsp;坐标范围<font color='red'>*</font>：</td>
		<td class="border_b">&nbsp;
			<select id="mapCoordinate" name="mapCoordinate" >
				<option value="5" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="5">selected</#if></#if></#if>	>5</option>
				<option value="6" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="6">selected</#if></#if></#if>	>6</option>
				<option value="7" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="7">selected</#if></#if></#if>	>7</option>
				<option value="8" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="8">selected</#if></#if></#if>	>8</option>
				<option value="9" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="9">selected</#if></#if></#if>	>9</option>
				<option value="10" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="10">selected</#if></#if></#if> >10</option>
				<option value="11" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="11">selected</#if></#if></#if> >11</option>
				<option value="12" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="12">selected</#if></#if></#if> >12</option>
				<option value="13" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="13">selected</#if></#if></#if> >13</option>
				<option value="14" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="14">selected</#if></#if></#if> >14</option>
				<option value="15" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="15">selected</#if></#if></#if> >15</option>
				<option value="16" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="16">selected</#if></#if></#if> >16</option>
				<option value="17" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="17">selected</#if></#if></#if> >17</option>
				<option value="18" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="18">selected</#if></#if></#if> >18</option>
				<option value="19" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="19">selected</#if></#if></#if> >19</option>
				<option value="20" <#if orgExtraInfo??><#if orgExtraInfo.mapCoordinate??><#if orgExtraInfo.mapCoordinate=="20">selected</#if></#if></#if> >20</option>					
			</select>
		</td>				
	</tr>
	
	<tr class="item">	
	<td class="itemtit">&nbsp;经度<font color='red'>*</font>：</td>
		<td class="border_b">&nbsp; <span> 
		 <input type="text"  class="w150" id="x" name="longitude" value="<#if orgExtraInfo??><#if orgExtraInfo.longitude??>${orgExtraInfo.longitude?string('#.######')}</#if></#if>"/> 
		</span>

		</td>
				
		<td class="itemtit">&nbsp;纬度<font color='red'>*</font>：</td>
		<td class="border_b">&nbsp;
			<input type="text"  class="w150" id="y" name="latitude" value="<#if orgExtraInfo??><#if orgExtraInfo.latitude??>${orgExtraInfo.latitude?string('#.######')}</#if></#if>"/>
		</td>	
		<td class="itemtit">&nbsp;</td>
		<td class="itemtit">&nbsp;
		</td>	
	</tr>
</table>
-->
<div class="page-container" id="map" style="position: absolute; top:40px;bottom:0px;right:0px; left:0px; z-index: 1;">
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
			var url = '${SQ_FIREGRID_URL}/firegrid/orgconfig/toArcgisCrossDomain.jhtml?'+ data;
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
            
            // 再添加第一个点
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
		var x = $('#x').val();
		var y = $('#y').val();
		if(x == 0 && y == 0) {
			x = currentArcgisConfigInfo.mapCenterX;
			y = currentArcgisConfigInfo.mapCenterY;
		}
		$('#x').val(x);
		$('#y').val(y);
		$('#mapt').val(currentArcgisConfigInfo.mapType);
		$("#map").ffcsMap.onClickGetPoint(x,y,'${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', pointGetCallBack);
		
		$("#map").ffcsMap.centerAt({
			x : parseFloat(x),          //中心点X坐标
			y : parseFloat(y),          //中心点y坐标
			wkid : 4326, //wkid 2437
			zoom : 12
		});
	}
	
	function pointGetCallBack(data) {
		var xys = data.toString().split(",");
		var x = xys[0];
		var y = xys[1];
		$('#x').val(x);
		$('#y').val(y);
		$('#mapt').val(arcgisMapConfigInfo.mapStartType);
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
	
	//信息校验
	function validate() {
	 	var orgName = $('#orgName').val();
	 	
	 	if(orgName=='') {
	 		$.messager.alert('友情提示','名称不能为空!','warning');
	 		return false;
	 	}
	 	
	 	var mapCoordinate = $('#mapCoordinate').val();
	 	if(mapCoordinate=='') {
	 		$.messager.alert('友情提示','坐标范围不能为空!','warning');
	 		return false;
	 	}
	 	var orgContent = $('#orgContent').val();
	 	if(orgContent=='') {
	 		$.messager.alert('友情提示','简介不能为空!','warning');
	 		return false;
	 	}
	 	var longitude = $('#x').val();
	 	if(longitude=='') {
	 		$.messager.alert('友情提示','经度不能为空!','warning');
	 		return false;
	 	}
	 	var latitude = $('#y').val();
	 	if(latitude=='') {
	 		$.messager.alert('友情提示','纬度不能为空!','warning');
	 		return false;
	 	}
	 	return true;
	}
	
	// 保存
	function save() {
		if (validate()) {
			orgLocationInfoJSONString = getOrgLocationInfoJSONString(); 
		  	//alert(orgLocationInfoJSONString);
		  	var orgId = '${orgId}';
		  	//经度
			var lng = $('#x').val();
		 	//纬度
		 	var lat = $('#y').val();
			//组织名称
			var orgName = $('#orgName').val();
			//组织简介
			var orgContent = $('#orgContent').val();
			//坐标范围
			var mapCoordinate = $('#mapCoordinate').val();
			
			var data = 'orgId='+orgId+'&longitude='+lng+'&latitude='+lat+'&orgName='+encodeURIComponent(orgName)+'&orgContent='+encodeURIComponent(orgContent)+'&mapCoordinate='+mapCoordinate+'&orgLocationInfoJSONString='+orgLocationInfoJSONString;
			var url = '${SQ_FIREGRID_URL}/firegrid/orgconfig/save.jhtml?'+ data;
			$('#cross_domain_frame').attr('src', url);
		}
	}
	
	// 编辑
	function edit() {
		var map = $.fn.ffcsMap.getMap();
	 	var polygonGraphicsLayer = map.getLayer("polygonGraphicsLayer");
	 	var isVisible = polygonGraphicsLayer.visible;
	 	
		if (isVisible) {
			markers = new Array(); // 清空
			
			$("#map").ffcsMap.edit(true, function(data){
				var dataObj = eval ("(" + data + ")");
				for (var i = 0; i < dataObj.coordinates[0].length; i++) {
					markers[i] = new esri.geometry.Point(dataObj.coordinates[0][i]);  
		        }  
			});
		}
	}
	
	// 重绘
	function redraw() {
		// 清除图形
		markers = new Array();
		var map = $.fn.ffcsMap.getMap();
	 	var polygonGraphicsLayer = map.getLayer("polygonGraphicsLayer");
	 	polygonGraphicsLayer.clear();
	 	map.graphics.clear();
	 	
	 	// 绘制
	 	$("#map").ffcsMap.draw('POLYGON', function(data){
	 		var dataObj = eval ("(" + data + ")");
	 		for (var i = 0; i < dataObj.coordinates[0].length; i++) {
				markers[i] = new esri.geometry.Point(dataObj.coordinates[0][i]);  
	        } 
	 	});
	}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
