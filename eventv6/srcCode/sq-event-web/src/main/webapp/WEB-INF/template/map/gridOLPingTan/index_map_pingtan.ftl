<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title> 
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		.AlphaBack1 tr td{padding-top:3px;padding-right:2px;}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
		
		#gridDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:550px; display:none;}
		#gridDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#gridDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		
		#changeStyleDiv{width:160px; position:absolute; z-index:10000; top:32px; left:390px; display:none;}
		#changeStyleDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#changeStyleDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		.styleChangeA:hover{text-decoration : none}
		
	</style>
	
	<#include "/map/mapabc/relyJs.ftl"/>
	
    <link rel="stylesheet" href="${rc.getContextPath()}/js/cxcolor/css/jquery.cxcolor.css">
    <script type="text/javascript" src="${rc.getContextPath()}/js/cxcolor/js/jquery.cxcolor.js" ></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/map/map.api.js"></script>
	
	<link rel="stylesheet" href="${ZZGRID_DOMAIN!''}/js/components/tip/css/tip.css" type="text/css"/>
	<script type="text/javascript" src="${ZZGRID_DOMAIN!''}/js/components/tip/jquery.anole.tip.js"></script>
</head>
<body style="width:100%;height:100%;border:none;" >
	<input type="hidden" name="gridId" id="gridId" value="" />
	<input type="hidden" name="gridCode" id="gridCode" value="" />
	<input type="hidden" name="orgCode" id="orgCode" value="" />
	
	<input type="hidden" name="mapt" id="mapt" value="5" />
	<input type="hidden" readonly="true" name="x" id="x" value="" />
	<input type="hidden" readonly="true" name="y" id="y" value="" />
	<input type="hidden" readonly="true" name="wid" id="wid" value="" />
	<input type="hidden" readonly="true" name="hs" id="hs" value=""/>
	
	<input type="hidden" readonly="true" name="ssqy" id="ssqy" value="" />
	<input type="hidden" readonly="true" name="qyfj" id="qyfj" value="" />
	<input type="hidden" readonly="true" name="qycj" id="qycj" value="" />
	<input type="hidden" readonly="true" name="qymc" id="qymc" value="" />
	<input type="hidden" readonly="true" name="selectRowNum" id="selectRowNum" value="" />
	
 	<div>
		<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
		</div>
	</div>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr style="float:left">
				    <td>
		            	<a href="#" onclick="saveGridData()" id="saveGridData" class="NorToolBtn SmallSaveBtn">确定</a> &nbsp;
		            </td>
		            <td>
	          	 		<a href="#" onclick="drawPoint()" id="drawPoint" class="NorToolBtn SetCenterBtn">编辑中心点</a>
		          	</td>
		          	<td>
		          		<a href="#" onclick="cancleDrawPoint()" id="cancleDrawPoint" class="NorToolBtn BackBtn" style="display:none;">取消中心点标注</a>
		    		</td>
		          	<td>
		          		<a href="#" onclick="drawGrid()" id="drawGrid" class="NorToolBtn DrawBorderBtn">轮廓编辑</a>
		    		</td>
		          	<td>
		          		<a href="#" onclick="cancleDrawOrEditGrid()" id="cancleDrawOrEditGrid" class="NorToolBtn BackBtn" style="display:none;">取消轮廓编辑</a>
		    		</td>
		          	<td>
		          		<a href="#" onclick="delGridData()" id="delDrawData" class="NorToolBtn DelBtn" style="display:none;">删除轮廓</a>
		    		</td>
		    		<td>
		    	 		<div class="MapLevel fl" id="settingDiv" style="width:130px">
			    	 		<ul>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="styleChangeClick();" /></li>
				                <li><a class="styleChangeA" onclick="styleChangeClick();">设置</a></li>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="styleChangeClick();" /></li>
			            	</ul>
		       			 </div>
		    	 	</td>
		    	 	<td>
		    	 		<div class="MapLevel fl" id="gridDisplaySettingDiv" style="width:130px">
			    	 		<ul>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="gridDisplayChange();" /></li>
				                <li><a class="styleChangeA" onclick="gridDisplayChange();">辅助显示</a></li>
				            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="gridDisplayChange();" /></li>
			            	</ul>
		       			 </div>
		    	 	</td>
		            <td>
		    	 		<div id="tipMessage" style="color:yellow;font-size:13px"></div>
		 				<div class="blind" style="color:yellow;font-size:13px;float:right;"></div>
		    	 	</td>
		         </tr>
		    </table>    	
		</div>
		<div id="changeStyleDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li onclick="styleChangeClick();" style="text-align:right;">
	    			关闭
	    		</li>
      			<li>
      				默认显示层级:
      				<select id="mapCenterLevel" class="inp1" style="width:40px;height:18px;color:#000;padding:0;" onchange="setDefaultMapLevel();"></select>
				</li>
	    		<li>
	    			名称颜色：<input type="text" id="nameColor" value="" readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    		<li>
	    			轮廓颜色：<input type="text" id="lineColor" value=""  readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    		<li>
	    			填充颜色：<input type="text" id="areaColor" value=""  readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    	</ul>
	    </div>
		<div id="gridDisplayDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li>
	    			<span><input name="displayLevel1" type="checkbox" value="1" onclick="getArcgisDataOfGridsByLevel(1);" id="displayLevel1" /></span>上级轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayLevel2" type="checkbox" value="2" onclick="getArcgisDataOfGridsByLevel(2);" id="displayLevel2" /></span>本级轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayLevel3" type="checkbox" value="3" onclick="getArcgisDataOfGridsByLevel(3);" id="displayLevel3" /></span>下级轮廓
	    		</li>
	    	</ul>
	    </div>
	</div>
	<div class="MapTools" style="top:261px;z-index:2;">
		<div id="mapStyleDiv" class="MapStyle" style="display:none">
			<span class="current">矢量图</span>
			<span>影像图</span>
		</div>
	</div>

</body>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/customEasyWin.ftl" />
<script type="text/javascript">
var test1="tets";
var map = null;
var ds;//数据连接源
var conncallback;//连接源回调
var OPERATION_CODE = "QUERY";//判断是新增还是修改
var isEditOrAdd = "ADD";//判断是新增还是修改
var editOrAddFlag = false;//判断是新增还是修改
var mapApi2;
var markers = null;
var fid = "";//网格轮廓数据表fid
var gridDS = null;
var saveCenterPointFlag = false;
function _init() {
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	window.onresize=function(){
		$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
	};
	$(document).mouseup(function() {
		$(this).unbind("mousemove");
	});
}

$(function(){
    layer.load(0);
 	_init();
	var eventDomain = "${rc.getContextPath()}";
	var zzgridDomain = "${SQ_ZZGRID_URL}";
	//验证用户标识，成功后回调
	//SPGIS.VerifyKey("2da73f2f-6485-479b-9e0c-da85b49d370c");
	MMApi.LoadMap(eventDomain, zzgridDomain, "${SPGIS_IP!''}", "SpGisMap", function(mapApi) {
		mapApi.Init("map0", $("#gridId").val(), $("#orgCode").val());
		map = MMGlobal.mapObj;
        map.showSicMapOfName("pingtanimage");

        markers = new SPGIS.Layer.Markers("markers");

        layer.closeAll('loading');
	});
 	
});


function editCustomidByFid(ds, fid, customid, callBack) {
	if (fid != "" && ds != null && ds.isSucceed() && customid != "") {
		var param = new SPGIS.Data.UpdateParameter("fid=='" + fid + "'");
		param.setField("customid", customid);
		var edit = new SPGIS.Data.EditData(ds, param);
		edit.submit(function(d) {
			Lemon.MSG.MsgPanel.Debug(d);
			if (typeof callBack == "function") {
				callBack.call(this);
			}
		});
	} else {
		if (typeof callBack == "function") {
			callBack.call(this);
		}
	}
}

//保存并推送网格id和坐标信息
function saveGridData() {
	
	var wid = $('#wid').val();
	
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
		//取消绘制中心点图层
		if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
			drawPointClick.deactivate();
		}
		
		//取消编辑轮廓
		if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
			modifyFeature.deactivate();//取消编辑轮廓
		}
	
		var x = $('#x').val();
		var y = $('#y').val();
		var hs = $('#hs').val();
		var mapt = $('#mapt').val();
		
		//本地保存标注信息
		saveDateToFFCS(x, y, hs, mapt, wid);
		/*
		editCustomidByFid(gridDS, fid, wid, function() {
			//推送标注信息给星云
			//saveDateToSP(x, y, mapt, wid);
			//本地保存标注信息
			saveDateToFFCS(x, y, hs, mapt, wid);
		});*/
	}else{
		DivShow('请选择要标注的网格!');
	}
}

//本地保存
function saveDateToFFCS(x, y, hs, mapt, wid){
	var nameColor = $("#nameColor").val();
	var areaColor = $("#areaColor").val();
	var lineColor = $("#lineColor").val();
	
	var mapCenterLevel = $("#mapCenterLevel").val();
	var data = 'wid='+ wid +'&x='+ x +'&y='+ y +'&mapt='+ mapt + '&saveCenterPointFlag=' + saveCenterPointFlag +'&nameColor='+nameColor+'&areaColor='+areaColor+'&lineColor='+lineColor +'&mapCenterLevel=' + mapCenterLevel +'&hs=' + hs ;
	var url = '${rc.getContextPath()}/zhsq/map/gridOL/gridOLController/updateMapDataOfGridOfBinding.json?';
	if(wid != null && wid !=''){
		$.ajax({   
			url: url,
			type: 'POST',
	   		data: data,
			dataType:"json",
			error: function(data){
				$.messager.alert('提示','操作报错!','warning');
			},
			success: function(data){
				if(data.flag == true) {
					DivShow('操作成功!');
					clearAllLayer();
					//列表对应记录添加定位图标
					var selectRowNum = $("#selectRowNum").val();
					var markerImgId = "markerImg_"+selectRowNum;
                    window.parent.document.getElementById(markerImgId).innerHTML = '<img src="${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png" class="FontDarkBlue" style="float:left" title="已绑定">';
                
					getGisDataOfGrid(wid);
				}else {
					DivShow('操作失败!');
				}
				
			}
		});
	}else{
		alert("请选择要标注的网格!");
	}
}

/**
 * 清除图层
 */
function clearAllLayer(){
	$("#cancleDrawPoint").hide();
	$("#cancleDrawOrEditGrid").hide();
	//取消绘制的网格图层
	if(drawGridlayer != null && drawGridlayer != '' && typeof(drawGridlayer) != 'undefined'){
		drawGridlayer.removeAllFeatures();
		map.removeLayer(drawGridlayer);//取消轮廓绘制
	}
	
	//取消绘制中心点图层
	if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
		drawPointClick.deactivate();
	}
	
	//取消编辑轮廓
	if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
		modifyFeature.deactivate();//取消编辑轮廓
	}
	
	//取消中心点定位图层
	if(markers != null && markers != '' && typeof(markers) != 'undefined'){
		markers.clearMarkers();
		map.removeLayer(markers);
	}
	
	//清除网格图层
	if(gridLayer != null && gridLayer != '' && typeof(gridLayer) != 'undefined'){
		gridLayer.removeFeatures(gridFeature);
		map.removeLayer(gridLayer);
	}
	if(gridCenterLayer != null && gridCenterLayer != '' && typeof(gridCenterLayer) != 'undefined'){
		gridCenterLayer.removeFeatures(gridCenterFeature);
		map.removeLayer(gridCenterLayer);
	}
}

//推送信息给星云
function saveDateToSP(x, y, mapt, wid){
	delSPGISDate(wid);//先删除后新增
}


//查询星云数据
function querySPGISDate(wid){
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "QUERY";
		//属性查询参数对象，传入的是字段名称和要查询的值
	 	var param = new SPGIS.Data.AttributeParameter("customid==" + wid.toString());
		//数据查询功能对象，传入的参数是数据源、查询参数
		var query = new SPGIS.Data.QueryData(ds,param); 
		//执行查询
		query.submit(submitSucceed);
	}
}

//新增星云数据
function addSPGISDate(){
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	var wid = $('#wid').val();
	var ssqy = $('#ssqy').val();//所属区域
	var qyfj = $('#qyfj').val();//区域父级
	var qycj = $('#qycj').val();//区域层级
	var qymc = $('#qymc').val();//区域名称
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "ADD";
		//从数据源中获取当前数据源的元数据，可以查看数据源的字段结构		
		var prop=ds.getMetaData();
		prop.customid = wid;
		//构造地图上的点对象
	 	var p1 = new SPGIS.Geometry.Point(x, y);   
		var para=new SPGIS.Data.InsertParameter();
		para.add(p1, prop, ssqy, qyfj, qycj, qymc);
		var add = new SPGIS.Data.EditData(ds,para); 
		add.submit(submitSucceed);
	}
}

//修改星云数据
function editSPGISDate(){
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	var wid = $('#wid').val();
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "EDIT";
		var param=new SPGIS.Data.UpdateParameter("customid=="+ wid.toString());
		param.setField("customid",wid);
		param.setField("geometry",wid);
		var edit = new SPGIS.Data.EditData(ds,param); 
		edit.submit(submitSucceed);
	}
}

//删除星云数据
function delSPGISDate(wid){
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "DEL";
		//删除要素参数对象
		var param = new SPGIS.Data.DeleteParameter("customid=="+ wid.toString());
		var edit = new SPGIS.Data.EditData(ds,param); 
		edit.submit(submitSucceed);
	}
}


function submitSucceed(d){
	if(OPERATION_CODE != null && OPERATION_CODE != '' && typeof(OPERATION_CODE) != 'undefined'){
		if(OPERATION_CODE == "QUERY"){
			if(d.chains.length != 0){
				markers.clearMarkers();
				var datas = d.chains;
				for(var i = 0;i < datas.length;i++){
					var lonIn = datas[i].geometry.coordinates[0];
					var latIn = datas[i].geometry.coordinates[1];
					var markerLonLat = new SPGIS.Mapping.LonLat(lonIn,latIn);
					var size = new SPGIS.Mapping.Size(21,25);
					var offset = new SPGIS.Mapping.Pixel(-(size.w/2), -size.h);
					var icon = new SPGIS.Theme.Icon('${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png',size,offset);
					feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);  
			 	    feature.data.icon = icon;
			 	    feature.data.id = i;
			 	    var marker = feature.createMarker();
			 	    markers.addMarker(marker);	
			 	    if(i == 0){
			 	    	//设置地图中心点
			 	    	var lonlat = new SPGIS.Mapping.LonLat(lonIn, latIn);
						map.setCenter(lonlat);
			 	    }		
				}
				map.addLayer(markers);
			}else{
				markers.clearMarkers();
				DivShow('该网格未标注!');
			}
		}else if(OPERATION_CODE == "ADD"){
			//alert("新增成功");
		}else if(OPERATION_CODE == "EDIT"){
			//alert("修改成功");
		}else if(OPERATION_CODE == "DEL"){
			addSPGISDate();
		}
		
	}
	
}

/**
 * 地图中心点定位
 */
function setMapcenter(isQuery, gridId, infoOrgCode){
	if(isQuery != null && isQuery != '' && typeof(isQuery) != 'undefined' && isQuery == true){
		querySPGISDate(gridId);
	}else{
		if(gridId != null && gridId != '' && typeof(gridId) != 'undefined'){
			var mapCenterX = $("#x").val();
			var mapCenterY = $("#y").val();
			markers.clearMarkers();
			var markerLonLat = new SPGIS.Mapping.LonLat(mapCenterX,mapCenterY);
			var size = new SPGIS.Mapping.Size(21,25);
			var offset = new SPGIS.Mapping.Pixel(-(size.w/2), -size.h);
			var icon = new SPGIS.Theme.Icon('${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png',size,offset);
			var feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);  
	 	    feature.data.icon = icon;
	 	    feature.data.id = i;
	 	    var marker = feature.createMarker();
	 	    markers.addMarker(marker);	
 	    	//设置地图中心点
 	    	var lonlat = new SPGIS.Mapping.LonLat(mapCenterX, mapCenterY);
			map.setCenter(lonlat);
			var mapCenterLevel = $("#mapCenterLevel").val();
			map.zoomTo(mapCenterLevel);

			map.addLayer(markers);
			
		}
	}
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
 * 网格数据初始化
 */
function initGridMapData(){
	$("#cancleDrawOrEditGrid").hide();
	$("#cancleDrawPoint").hide();
	$("#drawPoint").show();
	$("#drawGrid").show();
	
	var wid = $("#wid").val();
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
		gridDisplayDiv.style.display = 'none';//隐藏辅助显示功能
		window.document.getElementById("displayLevel1").checked = false;
		window.document.getElementById("displayLevel2").checked = false;
		window.document.getElementById("displayLevel3").checked = false;
		removeDisplayGridLayer();
		//获取网格数据
		getGisDataOfGrid(wid);
	}
}

//获取网格轮廓数据
function getGisDataOfGrid(gridId){
	mapt = 5;
	var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt=5&showType=0&gridId='+gridId;
	$.ajax({
		url: url,
		type: 'POST',
		dataType:"json",
		async: false,
		error: function(data){
			$.messager.alert('警告','操作报错！','warning');
		},
		success: function(data){
			if(data.length > 0) {
				showGridLayerFlag = true;
				//removeGridLayer();//先清除其他的网格轮廓
				if(data[0].x != null){
					$("#x").val(data[0].x);
				}else{
					$("#x").val("");
				}
				if(data[0].y != null){
					$("#y").val(data[0].y);
				}else{
					$("#y").val("");
				}

                //设置“默认显示层级”选项
                document.getElementById("mapCenterLevel").innerHTML='';
                for(var i = 0;i<=10;i++){
                    var option = "<option";
                    if(data[0] != null && data[0].mapCenterLevel != null && data[0].mapCenterLevel==i){
                        option+=' selected="selected" ';
                    }
                    option+=" value=\'"+i+"\'>"+i+"</option>";
                    $("#mapCenterLevel").append(option);
                }
				if(data[0].hs != null){
					$("#hs").val(data[0].hs);
					$("#delDrawData").show();
					$("#drawGrid").show();
					$("#drawPoint").show();
					$("#gridDisplayDiv").css("left", "550px");
					//展示网格轮廓
					nameColorObj.color(data[0].nameColor);
					areaColorObj.color(data[0].areaColor);
					lineColorObj.color(data[0].lineColor);
					
					showGridPolygon(data[0]);
				}else{
					$("#hs").val("");
					$("#delDrawData").hide();
					$("#gridDisplayDiv").css("left", "550px");
				}
				
				//设置地图中心点
				setMapcenter(false, gridId);
			}else{
				//设置“默认显示层级”选项
                document.getElementById("mapCenterLevel").innerHTML='';
                for(var i = 0;i<=10;i++){
                    var option = "<option";
                    option+=" value=\'"+i+"\'>"+i+"</option>";
                    $("#mapCenterLevel").append(option);
                }
				$("#hs").val("");
				$("#delDrawData").hide();
				$("#gridDisplayDiv").css("left", "290px");
				if(gridLayer != null && gridLayer != '' && typeof(gridLayer) != 'undefined'){
					gridLayer.removeFeatures(gridFeature);
					map.removeLayer(gridLayer);
				}
				if(gridCenterLayer != null && gridCenterLayer != '' && typeof(gridCenterLayer) != 'undefined'){
					gridCenterLayer.removeFeatures(gridCenterFeature);
					map.removeLayer(gridCenterLayer);
				}
			}
		}
	});
}

/**
 * 展示网格轮廓
 */
var gridLayer;
var gridFeature;
var gridCenterLayer;
var gridCenterFeature;
function showGridPolygon(data){
	if(gridLayer != null && gridLayer != '' && typeof(gridLayer) != 'undefined'){
		gridLayer.removeFeatures(gridFeature);
		map.removeLayer(gridLayer);
	}
	if(gridCenterLayer != null && gridCenterLayer != '' && typeof(gridCenterLayer) != 'undefined'){
		gridCenterLayer.removeFeatures(gridCenterFeature);
		map.removeLayer(gridCenterLayer);
	}
	
	gridLayer = new SPGIS.Layer.Vector("vector");
	var pointList = [];
	var polygon_points = data.hs.split(",");
	var geojsonObject;
	if(polygon_points != null && polygon_points != '' && typeof(polygon_points) != 'undefined'){
		for(var i=0;i<polygon_points.length;i++){
			if(i%2==0){
				var point = new SPGIS.Geometry.Point(polygon_points[i],polygon_points[i+1]);
				pointList.push(point);
			}
			i++;
		}
	}
	
	var style_green = {
        strokeOpacity:1,
        strokeColor: data.lineColor, //轮廓颜色
        strokeWidth: 2,
        strokeLinecap:"round",
        pointRadius: 4,
        pointerEvents: "visiblePainted" ,
        fillOpacity: 0.5,  //透明度
        fillColor : data.areaColor,  //填充颜色
        graphicOpacity:0.1
    };
	
	var geoRing = new SPGIS.Geometry.LinearRing(pointList);
	var geoPolygon = new SPGIS.Geometry.Polygon(geoRing);
	gridFeature = new SPGIS.Feature(geoPolygon);
	
	gridFeature.style=style_green;
	
	gridLayer.addFeatures(gridFeature);
	
	map.addLayer(gridLayer);
	
	//添加网格中心点注释
	var centerPointOptions={  
	    label: data.gridName,  
	    fontColor: data.nameColor,  
	    fontFamily:"sans-serif",  
	    fontWeight:"bold",  
	    fontSize:12  
    };
	var centerPointStyle = new SPGIS.Theme.Style(centerPointOptions);
	gridCenterLayer = new SPGIS.Layer.Vector("layer",{
		styleMap:new SPGIS.Mapping.StyleMap(centerPointStyle)
	});
	map.addLayer(gridCenterLayer);
	var point = new SPGIS.Geometry.Point(data.x, data.y);
	gridCenterFeature = new SPGIS.Feature(point);
	gridCenterLayer.addFeatures(gridCenterFeature); 
}

/**
 * 标注中心点功能
 */
var drawPointClick;
function drawPoint(){
 	$("#changeStyleDiv").hide();
 	
 	$("#gridDisplayDiv").hide();
 	
	var wid = $("#wid").val();
	if(wid != null && wid != "" && typeof(wid) != 'undefined'){
		saveCenterPointFlag = true;
		//cancleDrawOrEditGrid();
		$("#cancleDrawOrEditGrid").hide();
		if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
			modifyFeature.deactivate();//取消编辑轮廓
		}
		
		markers.clearMarkers();
		$("#cancleDrawPoint").show();
		$("#drawPoint").hide();
		$("#drawGrid").show();
		$("#gridDisplayDiv").css("left", "505px");
		//添加标注功能
		drawPointClick = SPGIS.Control.ClickControl(function(e) {
			markers.clearMarkers();
			//在鼠标点击的地方，获取到坐标，并添加标注图标
			var lonlat = map.getLonLatFromPixel(e.xy).toShortString();
	    	var lon = lonlat.split(",")[0] * 1.0;
	    	var lat = lonlat.split(",")[1] * 1.0;
	    	var size = new SPGIS.Mapping.Size(21,25);
	    	var offset = new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h);
	    	var icon = new SPGIS.Theme.Icon(MMGlobal.ContextPath + '/js/map/spgis/lib/img/marker-gold.png', size, offset);
			var markerLonLat = new SPGIS.Mapping.LonLat(lon, lat);
			var feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);
			feature.closeBox = false;
			feature.data.overflow = "hidden";
			feature.data.icon = icon;
			var marker = feature.createMarker();
			markers.addMarker(marker);
			map.addLayer(markers);
			//获取坐标
			if (lon != null && lat != null) {
				$("#x").val(lon);
				$("#y").val(lat);
				$("#mapt").val(MMGlobal.MapType);
			}
			saveCenterPointFlag = true;
		});
		
		map.addControl(drawPointClick);
		drawPointClick.activate();
	}else{
		chooseGridTipMessage();
	}
}

/**
 * 取消标注中心点功能
 */
function cancleDrawPoint(){
	$("#changeStyleDiv").hide();
 	
 	$("#gridDisplayDiv").hide();
 	
	saveCenterPointFlag = false;
	$("#cancleDrawPoint").hide();
	$("#drawPoint").show();
	initGridMapData();
	if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
		drawPointClick.deactivate();
	}
	
}

/**
 * 绘制/编辑网格轮廓
 */
function drawGrid(){
	$("#changeStyleDiv").hide();
 	
 	$("#gridDisplayDiv").hide();
 	
	//cancleDrawPoint();
	var wid = $("#wid").val();
	if(wid != null && wid != "" && typeof(wid) != 'undefined'){
		$("#cancleDrawPoint").hide();
		if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
			drawPointClick.deactivate();
		}
		
		$("#cancleDrawOrEditGrid").show();
		$("#drawGrid").hide();
		$("#drawPoint").show();
		$("#gridDisplayDiv").css("left", "495px");
		
		var hs = document.getElementById("hs").value;
		if(hs != null && hs != '' && typeof(hs) != 'undefined'){
			addModifyTool(hs);//编辑轮廓
		}else{//绘制轮廓
			drawPointFlag = false;
			blindGridFlag = false;
			drawGridHSFlag = true;
			drawGridHS();
		}
	}else{
		chooseGridTipMessage();
	}
}

/**
 * 添加绘制网格轮廓工具
 */
var drawGridTool;
var drawGridlayer;
function drawGridHS(){
	drawGridlayer = new SPGIS.Layer.Vector("vectorlayer");
	map.addLayer(drawGridlayer);
	drawGridTool = map.createAction();
	drawGridTool.createPolygon(drawGridlayer,function(e){
		var str = "";
		var rings = e.feature.geometry.components;
		var ringPoints = rings[0].components;
		var hs = ''; 
		var sumX=0,sumY=0,sumLength=0;
		sumLength = ringPoints.length;
		for(var j = 0;j < ringPoints.length;j++){
			if(hs != null && hs != ''){
				hs = hs + ",";
			}
			hs += ringPoints[j].x + ',' + ringPoints[j].y;
			sumX +=  Number(ringPoints[j].x);
			sumY +=  Number(ringPoints[j].y);
		}
		//计算中心点坐标
		var x = sumX/sumLength;
		var y = sumY/sumLength;
		if(!saveCenterPointFlag || $("#x").val() == "" || $("#x").val() == null){
			$("#x").val(x);
			$("#y").val(y);
		}
		$("#hs").val(hs);
	});
}

/**
 * 编辑网格轮廓
 */
var modifyFeature;
function addModifyTool(){
	//获取编辑后的数据
	if(gridLayer != null && gridLayer != '' && typeof(gridLayer) != 'undefined'){
		modifyFeature = new SPGIS.Control.ModifyFeature(gridLayer.layer);
		map.addControl(modifyFeature);
		modifyFeature.activate();
		
		modifyFeature.onModificationEnd = function(e){
			var data = e.geometry.components[0].components;
			var hs = "";
			for(var i = 0;i < data.length;i++){
				if(hs != null && hs != ''){
					hs = hs + ",";
				}
				hs += data[i].x + ',' + data[i].y;
			}
			$("#hs").val(hs);
			//saveCenterPointFlag = false;
		} 
	}
	
}

/**
 * 取消网格轮廓绘制/编辑
 */
function cancleDrawOrEditGrid(){
	$("#changeStyleDiv").hide();
 	
 	$("#gridDisplayDiv").hide();

	$("#cancleDrawOrEditGrid").hide();
	$("#drawGrid").show();
	initGridMapData();

	var hs = document.getElementById("hs").value;
	if(hs != null && hs != '' && typeof(hs) != 'undefined'){
		if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
			modifyFeature.deactivate();//取消编辑轮廓
		}
	}else{//取消绘制轮廓：画完后双击就取消绘制功能了
		if(drawGridlayer != null && drawGridlayer != '' && typeof(drawGridlayer) != 'undefined'){
			drawGridlayer.removeAllFeatures();
			map.removeLayer(drawGridlayer);//取消轮廓绘制
		}
		
	}
}

//根据网格id删除网格轮廓数据
function delGridData() {
	$("#tipMessage").html("");
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
	var data = "targetType=grid&wid="+$("#wid").val()+"&mapt=5";
	if(!confirm('确定要删除?')){
		return false;
	}
	var type = 1;
	var layerName="";
	layerName = "gridLayer";
	type = 1
	
	
	$.ajax({
	   type: "POST",
	   url: url,
	   data: data,
	   dataType:"json",
	   success: function(data){
		   	if(data.flag==true){
		   		
				DivShow('删除成功！');
				$('#delDrawData').hide();
				var selectRowNum = $("#selectRowNum").val();
				var markerImgId = "markerImg_"+selectRowNum;
				window.parent.document.getElementById(markerImgId).innerHTML = "";
				document.getElementById("hs").value = "";
				document.getElementById("x").value = "";
				document.getElementById("y").value = "";
				$("#drawPoint").show();
				$("#drawGrid").show();
				//清除绘制楼宇轮廓后留下的图层
				clearAllLayer();
				
		   	}if(data.flag==false){
				DivShow('删除失败！'); 
			}
		},
	   	error:function(msg){
			DivShow('删除失败！');
	  	}
	});
}


var nameColorObj = $("#nameColor").cxColor();//名称颜色
var areaColorObj = $("#areaColor").cxColor();//填充颜色
var lineColorObj = $("#lineColor").cxColor();//轮廓颜色
nameColorObj.color("#ff0000");
areaColorObj.color("#ff0000");
lineColorObj.color("#ff0000");

//设置按钮
function styleChangeClick(){
 	var mapStyleDiv = document.getElementById("changeStyleDiv");
 	var left = $("#settingDiv").offset().left;
 	$("#changeStyleDiv").css("left", left);
 	
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
}

//默认显示层级
function setDefaultMapLevel(){
	var mapCenterLevel = document.getElementById("mapCenterLevel").value;
	
	map.zoomTo(mapCenterLevel);
	
}

//辅助显示
function gridDisplayChange(){
	var left = $("#gridDisplaySettingDiv").offset().left;
 	$("#gridDisplayDiv").css("left", left);
	
 	if(gridDisplayDiv.style.display == 'none') {
 		displayGridLayer1 = new SPGIS.Layer.Vector("layer");
 		displayGridCenterLayer1 = new SPGIS.Layer.Vector("layer");
		displayGridLayer2 = new SPGIS.Layer.Vector("layer");
 		displayGridCenterLayer2 = new SPGIS.Layer.Vector("layer");
		displayGridLayer3 = new SPGIS.Layer.Vector("layer");
 		displayGridCenterLayer3 = new SPGIS.Layer.Vector("layer");
		
 		gridDisplayDiv.style.display = 'block';
 	}else {
 		gridDisplayDiv.style.display = 'none';
 	}
}

//根据层级获取网格地图轮廓
function getArcgisDataOfGridsByLevel(level){
	var wid = $("#wid").val();
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
		if(window.document.getElementById("displayLevel"+level).checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt=5&showType=' + level + '&gridId=' + wid;
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
						removeDisplayGridLayer(level);//先清除其他的网格轮廓
						for(var i=0;i<data.length;i++){
							getResourcePolygon(data[i], 'displayGridLayer', level);
						}
					}else{
						removeDisplayGridLayer(level);//先清除其他的网格轮廓
					}
				}
		 	});
		}else{
			removeDisplayGridLayer(level);
		}
	}else{
		window.document.getElementById("displayLevel"+level).checked = false;
		chooseGridTipMessage();
	}
}

/**
 * 在地图上画出多边形区域
 */
var displayGridLayer1; //上级网格轮廓图层
var displayGridCenterLayer1; //上级网格轮廓图层
var displayGridLayer2; //本级网格轮廓图层
var displayGridCenterLayer2; //本级网格轮廓图层
var displayGridLayer3; //下级网格轮廓图层
var displayGridCenterLayer3; //下级网格轮廓图层
function getResourcePolygon(data, showType, level){
	var pointList = [];
	var polygon_points;
	if(data != null && data.hs != null){
		polygon_points = data.hs.split(",");
	}
	
	var centerX = data.x;
	var centerY = data.y;
	var geojsonObject;
	if(polygon_points != null && polygon_points != "" && typeof(polygon_points) != 'undefined'){
		for(var i=0;i<polygon_points.length;i++){
			var pointX, pointY;
			if(i%2==0){
				var point = new SPGIS.Geometry.Point(polygon_points[i], polygon_points[i+1]);
				pointList.push(point);
			}
			i++;
		}
	}
	
	  
	var options={
        label : data.gridName,  
        fontColor:data.nameColor,  
        fontFamily:"sans-serif",  
        fontWeight:"bold",  
        fontSize:12  
    };
	var style = new SPGIS.Theme.Style(options);
	
	var displayGridLayerOption = {
        strokeOpacity:1,
        strokeColor: data.lineColor, //轮廓颜色
        strokeWidth: 2,
        strokeLinecap:"round",
        pointRadius: 4,
        pointerEvents: "visiblePainted" ,
        fillOpacity: 0.5,  //透明度
        fillColor: data.areaColor,  //填充颜色
        graphicOpacity:0.1
    };
	var displayGridLayerOptionStyle = new SPGIS.Theme.Style(displayGridLayerOption);
	
	  
	if(showType == "displayGridLayer"){
		var ring = new SPGIS.Geometry.LinearRing(pointList);
		var feature = new SPGIS.Feature(new SPGIS.Geometry.Polygon(ring));
		var centerFeature = new SPGIS.Feature(new SPGIS.Geometry.Point(centerX, centerY));
		
		if(level == 1){
			displayGridCenterLayer1.layer.styleMap = new SPGIS.Mapping.StyleMap(style);
			displayGridLayer1.layer.styleMap = new SPGIS.Mapping.StyleMap(displayGridLayerOptionStyle);
			
			displayGridCenterLayer1.addFeatures(centerFeature);
			displayGridLayer1.addFeatures(feature,SPGIS.MeasureStyle);
			map.addLayer(displayGridCenterLayer1);
			map.addLayer(displayGridLayer1);
		} else if(level == 2){
			displayGridCenterLayer2.layer.styleMap = new SPGIS.Mapping.StyleMap(style);
			displayGridLayer2.layer.styleMap = new SPGIS.Mapping.StyleMap(displayGridLayerOptionStyle);

			displayGridCenterLayer2.addFeatures(centerFeature);
			displayGridLayer2.addFeatures(feature,SPGIS.MeasureStyle);
			map.addLayer(displayGridCenterLayer2); 
			map.addLayer(displayGridLayer2); 
		} else if(level == 3){
			displayGridCenterLayer3.layer.styleMap = new SPGIS.Mapping.StyleMap(style);
			displayGridLayer3.layer.styleMap = new SPGIS.Mapping.StyleMap(displayGridLayerOptionStyle);

			displayGridCenterLayer3.addFeatures(centerFeature);
			displayGridLayer3.addFeatures(feature,SPGIS.MeasureStyle);
			map.addLayer(displayGridCenterLayer3); 
			map.addLayer(displayGridLayer3); 
		}
		
	}
}

/**
 * 清除辅助显示网格轮廓
 */
function removeDisplayGridLayer(level){
	if(level == 1 && displayGridLayer1 != null){
		displayGridLayer1.removeAllFeatures();
		map.removeLayer(displayGridLayer1); 
		if(displayGridCenterLayer1 != null){
			displayGridCenterLayer1.removeAllFeatures();
			map.removeLayer(displayGridCenterLayer1);
		} 
	} else if(level == 2 && displayGridLayer2 != null){
		displayGridLayer2.removeAllFeatures();
		map.removeLayer(displayGridLayer2); 
		if(displayGridCenterLayer2 != null){
			displayGridCenterLayer2.removeAllFeatures();
			map.removeLayer(displayGridCenterLayer2);
		} 
	} else if(level == 3 && displayGridLayer3 != null){
		displayGridLayer3.removeAllFeatures();
		map.removeLayer(displayGridLayer3); 
		if(displayGridCenterLayer3 != null){
			displayGridCenterLayer3.removeAllFeatures();
			map.removeLayer(displayGridCenterLayer3);
		} 
	}else{
		if(displayGridLayer1 != null){
			displayGridLayer1.removeAllFeatures();
			map.removeLayer(displayGridLayer1); 
			if(displayGridCenterLayer1 != null){
				displayGridCenterLayer1.removeAllFeatures();
				map.removeLayer(displayGridCenterLayer1);
			} 
		} 
		if(displayGridLayer2 != null){
			displayGridLayer2.removeAllFeatures();
			map.removeLayer(displayGridLayer2); 
			if(displayGridCenterLayer2 != null){
				displayGridCenterLayer2.removeAllFeatures();
				map.removeLayer(displayGridCenterLayer2);
			} 
		} 
		if(displayGridLayer3 != null){
			displayGridLayer3.removeAllFeatures();
			map.removeLayer(displayGridLayer3); 
			if(displayGridCenterLayer3 != null){
				displayGridCenterLayer3.removeAllFeatures();
				map.removeLayer(displayGridCenterLayer3);
			} 
		}
	}
	
}

function chooseGridTipMessage(){
	$.messager.alert('提示','请选择要编辑的网格!','warning');
}

//添加提示功能
$('body').initAnoleTip({
	Top : '1px',
	Right : '10px',
	RenderType : 0,
	Url : "${ZZGRID_DOMAIN!''}/zzgl/systemPrompts/getSystemPromptsInfoForJsonp.jhtml"
});


</script>
</html>
