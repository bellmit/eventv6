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
	</style>
	<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/map/map.api.js"></script>
</head>
<body style="width:100%;height:100%;border:none;" >
	<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
	<input type="hidden" name="gridCode" id="gridCode" value="<#if gridCode??>${gridCode}</#if>" />
	<input type="hidden" name="orgCode" id="orgCode" value="<#if orgCode??>${orgCode}</#if>" />
	<input type="hidden" name="mapt" id="mapt" value="<#if mapt??>${mapt}</#if>" />
	<input type="hidden" readonly="true" name="x" id="x" value="<#if x??>${x?c}</#if>" />
	<input type="hidden" readonly="true" name="y" id="y" value="<#if y??>${y?c}</#if>" />
	<input type="hidden" readonly="true" name="wid" id="wid" value="" />
	
	<input type="hidden" readonly="true" name="ssqy" id="ssqy" value="" />
	<input type="hidden" readonly="true" name="qyfj" id="qyfj" value="" />
	<input type="hidden" readonly="true" name="qycj" id="qycj" value="" />
	<input type="hidden" readonly="true" name="qymc" id="qymc" value="" />
	
 	<div>
		<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
		</div>
	</div>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr style="float:left">
				    <td>
		            	<a id="savePointLocate" href="#" class="NorToolBtn SmallSaveBtn">确定</a> &nbsp;
		            </td>
		            <td>
		 				<div class="blind" style="color:yellow;font-size:13px;float:right;"></div>
		    	 	</td>
		         </tr>
		    </table>    	
		</div>
	</div>
	<div class="MapTools" style="top:261px;z-index:2;">
		<ul>
	    	<li class="ClearMap" onclick="clearMyLayer();"></li>
	    </ul>
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
 	modleopen();
 	_init();
	var eventDomain = "${rc.getContextPath()}";
	var zzgridDomain = "${SQ_ZZGRID_URL}";
	//验证用户标识，成功后回调
	//SPGIS.VerifyKey("2da73f2f-6485-479b-9e0c-da85b49d370c");
	$("#savePointLocate").bind("click", savePointLocate);
	MMApi.LoadMap(eventDomain, zzgridDomain, "${SPGIS_IP!''}", "SpGisMap", function(mapApi) {
		mapApi.Init("map0", $("#gridId").val(), $("#orgCode").val());
		map = MMGlobal.mapObj;
		
		markers = new SPGIS.Layer.Markers("markers");
		//定位到平潭
		//var bounds=new SPGIS.Geometry.Bounds(119.784790,25.499390 ,119.796021,25.509094);
		//map.showOfBounds(bounds);
		//vectorlayer = new SPGIS.Layer.Vector("vectorlayer");
		//map.addLayer(vectorlayer);
		
		//叠加wms服务
		//addWMSLayer(map);
		
		
		//添加标注功能
		var click = SPGIS.Control.ClickControl(function(e) {
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
		});
		map.addControl(click);
		click.activate();
		
		//连接数据源
		//创建数据源,fwlkbz_point:房屋轮廓标注点数据表
		conncallback=function(d){
 			// alert(d);
 		};
		ds=new SPGIS.Data.DataSource("gridbz_point",conncallback);
		//连接数据源
		ds.conn();
		initSyncDataToSPGisEvent();
		modleclose();
	});
 	
});

function initSyncDataToSPGisEvent() {
	gridDS = new SPGIS.Data.DataSource("wanggenew", function() {
		if (gridDS.isSucceed()) {
			var _click = SPGIS.Control.ClickControl(function(e) {
				modleopen();
				$("#savePointLocate").unbind("click");
				var lonlat = MMGlobal.mapObj.getLonLatFromPixel(e.xy).toShortString();
				var point = new SPGIS.Geometry.Point(lonlat.split(",")[0], lonlat.split(",")[1]);
				var p1 = new SPGIS.Geometry.Point((point.x - 0.00000000001), (point.y + 0.00000000001));
				var p2 = new SPGIS.Geometry.Point((point.x + 0.00000000001), (point.y + 0.00000000001));
				var p3 = new SPGIS.Geometry.Point((point.x + 0.00000000001), (point.y - 0.00000000001));
				var p4 = new SPGIS.Geometry.Point((point.x - 0.00000000001), (point.y - 0.00000000001));
				var ring = new SPGIS.Geometry.LinearRing([ p1, p2, p3, p4 ]);
				var polygon = new SPGIS.Geometry.Polygon(ring);
				var param = new SPGIS.Data.PolygonParameter(polygon);
				var query = new SPGIS.Data.QueryData(gridDS, param);
				query.submit(function(d) {
					modleclose();
					Lemon.MSG.MsgPanel.Debug(d);
					if (d.chains.length != 0) {
						var datas = d.chains;
						if (datas != null && datas.length > 0) {
							fid = datas[0].fid;
						} else {
							fid = "";
						}
					}
					$("#savePointLocate").bind("click", savePointLocate);
				});
			});
			MMGlobal.mapObj.addControl(_click);
			_click.activate();
		}
	});
	// 连接数据源
	gridDS.conn();
}

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

//叠加wms图层
function addWMSLayer(mapObject){
	//http://183.250.187.97:8080/spgis/jack/wms
	//wms图层参数
	var params = {
		layers: 'WANGGE',//楼宇：CQJZW，网格：WANGGE
        transparent: true
	};
	//wms选项
	var options = {
			buffer: 0,
		    displayOutsideMaxExtent: true,
		    isBaseLayer: false,
		    opacity: 1,
		    color: 'red'
	};
	//创建wms图层
	var WMSlayer = new SPGIS.Layer.WMS("wangge","http://${SPGIS_IP!''}:8080/spgis/jack/wms",params,options);
	map.addLayer(WMSlayer);
}
//保存并推送楼宇id和坐标信息
function savePointLocate() {
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	var wid = $('#wid').val();
	
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
		editCustomidByFid(gridDS, fid, wid, function() {
			//推送标注信息给星云
			saveDateToSP(x, y, mapt, wid);
			//本地保存标注信息
			saveDateToFFCS(x, y, mapt, wid);
		});
	}else{
		DivShow('请选择要标注的网格!');
	}
}

//本地保存
function saveDateToFFCS(x, y, mapt, wid){
	var data = 'wid='+ wid +'&x='+ x +'&y='+ y +'&mapt='+ mapt;
	var url = '${rc.getContextPath()}/zhsq/map/gridOL/gridOLController/updateMapDataOfGridOfBinding.json?'+data;
	if(wid != null && wid !=''){
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
				if(data.flag == true) {
					DivShow('操作成功!');
				}else {
					DivShow('操作失败!');
				}
			}
		});
	}else{
		alert("请选择要标注的网格!");
	}
}

//推送信息给星云
function saveDateToSP(x, y, mapt, wid){
	//editOrAddFlag = true;
	delSPGISDate(wid);//先删除后新增
	//console.log("删除完成！");
	//addSPGISDate();
	//querySPGISDate(wid);
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
	//Lemon.MSG.MsgPanel.Debug(d);
	//console.log(d.chains);
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
	//var mapApi = new MapApiImpl();
	if(isQuery != null && isQuery != '' && typeof(isQuery) != 'undefined' && isQuery == true){
		querySPGISDate(gridId);
	}
	if(gridId != null && gridId != '' && typeof(gridId) != 'undefined'){
		//MMApi.setCenter(gridId);
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


</script>
</html>
