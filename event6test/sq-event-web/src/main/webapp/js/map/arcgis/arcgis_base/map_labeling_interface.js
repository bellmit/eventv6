/**
	参数：resourcesId（资源id）  markerType（标识code）  gridId（网格id）
	数据保存将由接口里面的保存功能直接保存到数据库
*/

	function markResInMap(resourcesId,markerType,gridId) {
		var url = "";
		var mapEngineType = "";
		var GIS_MAP_GANSU_URL = "";
		$.ajax({
			type: "POST",
			url: jsBasePath+'/zzgl/map/gis/getMapEngineInfo.json',
			dataType:"json",
			async:false,
			success: function(data){
				mapEngineType = data.mapEngineType;
				GIS_MAP_GANSU_URL = data.GIS_MAP_GANSU_URL;
			},
			error:function(data){
				$.messager.alert('错误','无法获取地图引擎！','error');
			}
		});
		if('005' == mapEngineType) {
			url = GIS_MAP_GANSU_URL+'/zhsq/map/arcgis/arcgis/toMapArcgisOfAnchorPointForSave.jhtml?resourcesId='+resourcesId+'&markerType='+markerType+'&catalog=02';
		}else {
			url = jsBasePath+"/zzgl/res/marker/config.jhtml?gridId="+gridId+"&resourcesId="+resourcesId+"&markerType="+markerType;
		}
		showMaxJqueryWindowForCross("资源点位标注", url);
	}
/**

参数：gridId 网格Id,callBackUrl 跨域回传地图数据的接收页面,x，y标注点,mapt地图类型
如果是新增页面或者之前没有标注的不存在x，y，mapt ，可以不传递这三个参数
地图标注数据需要在业务中进行保存，接口只是提供经纬度获取以及对应的地图类型

*/
	function initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType){
		if(isEdit == undefined || isEdit == null) {
			isEdit = true;
		}
		if(mapType == undefined || mapType == null) {
			mapType = "";
		}
		var mapEngineType = "";
		var ZHSQ_EVENT_URL = "";
		var ZHSQ_ZZGRID_URL = "";
		$.ajax({
			type: "POST",
			url: jsBasePath+'/zhsq/map/arcgis/arcgis/getMapEngineInfo.json?mapType='+mapType+'&t='+Math.random(),
			dataType:"json",
			async:false,
			success: function(data){
				mapEngineType = data.mapEngineType;
				ZHSQ_EVENT_URL = data.ZHSQ_EVENT_URL;
				ZHSQ_ZZGRID_URL = data.ZHSQ_ZZGRID_URL;
				mapType = data.mapType;
			},
			error:function(data){
				$.messager.alert('错误','无法获取地图引擎！','error');
			}
		});
		
		var data;
		var url;
		if(x!=undefined && x!='' && y!=undefined && y!='' && mapt!=undefined && mapt!=''){
			data = 'x='+x+'&y='+y+'&mapt='+mapt+'&gridId='+gridId+'&isEdit='+isEdit+'&mapType='+mapType+'&callBackUrl='+callBackUrl;
		}else {
			data = 'gridId='+gridId+'&isEdit='+isEdit+'&mapType='+mapType+'&callBackUrl='+callBackUrl;
		}
		// 天地图
		if('005' == mapEngineType){//新地图链接
			url = ZHSQ_EVENT_URL+'/zhsq/map/arcgis/arcgis/outPlatCrossDomain.jhtml?' + data;
		}else{ 
			url = ZHSQ_ZZGRID_URL+'/zzgl/map/gis/popMapMarkerSelectorCrossDomainNew.jhtml?' + data;
		}
		showMaxJqueryWindowForCross("地理位置", url,width,height);
		
	}

/**
 * 新事件地图标注改造
 * @param gridId
 * @param callBackUrl
 * @param width
 * @param height
 * @param isEdit
 * @param mapType
 */
function showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,modularCode, isGetXIEJINGAddress){
    var x = $("#x").val();
    var y = $("#y").val();
    var mapt = $("#mapt").val();

    if(isEdit == undefined || isEdit == null) {
        isEdit = true;
    }

    if(mapType == undefined || mapType == null) {
        mapType = "";
    }

    var mapEngineType = "";
    var ZHSQ_EVENT_URL = "";
    var ZHSQ_ZZGRID_URL = "";
    var markerType = "";
    var FACTOR_STATION = "";
    var FACTOR_URL = "";
    var FACTOR_SERVICE = "";
    $.ajax({
        type: "POST",
        url: jsBasePath+'/zhsq/map/arcgis/arcgis/getMapEngineInfo.json?mapType='+mapType+'&modularCode='+modularCode+'&t='+Math.random(),
        dataType:"json",
        async:false,
        success: function(data){
            mapEngineType = data.mapEngineType;
            ZHSQ_EVENT_URL = data.ZHSQ_EVENT_URL;
            ZHSQ_ZZGRID_URL = data.ZHSQ_ZZGRID_URL;
            mapType = data.mapType;
            markerType = data.markerType
            FACTOR_STATION = data.FACTOR_STATION
            FACTOR_URL = data.FACTOR_URL
            FACTOR_SERVICE = data.FACTOR_SERVICE

        },
        error:function(data){
            $.messager.alert('错误','无法获取地图引擎！','error');
        }
    });

    var data;
    var url;
    if(typeof isGetXIEJINGAddress != 'undefined' && isGetXIEJINGAddress != null && isGetXIEJINGAddress != ''){
        data = "isGetXIEJINGAddress=" +isGetXIEJINGAddress;
    }
    if(x!=undefined && x!='' && y!=undefined && y!='' && mapt!=undefined && mapt!=''){
        data = 'x='+x+'&y='+y+'&mapt='+mapt+'&gridId='+gridId+'&isEdit='+isEdit+'&mapType='+mapType+'&callBackUrl='+callBackUrl;
    }else {
        data = 'gridId='+gridId+'&isEdit='+isEdit+'&mapType='+mapType+'&callBackUrl='+callBackUrl;
    }
    // 天地图
    if('005' == mapEngineType){//新地图链接
        if(FACTOR_STATION == "1") {
            var id = $("#id").val();
            if(id == undefined || id == "") {
                $.messager.alert('错误', '无法获取地图标注相关数据！', 'error');
                return;
            }else {
                var parameterJsonStr = JSON.stringify(parameterJson);
                data = "id=" + id + "&parameterJsonStr="+encodeURI(parameterJsonStr)+"&FACTOR_URL="+FACTOR_URL+"&FACTOR_SERVICE="+FACTOR_SERVICE+"&markerType="+markerType+"&"+data;
                url = ZHSQ_EVENT_URL + '/zhsq/map/arcgis/arcgisFactor/toFactorPointTagCommon.jhtml?' + data;
            }
        }else {
            url = ZHSQ_EVENT_URL+'/zhsq/map/arcgis/arcgis/outPlatCrossDomain.jhtml?' + data;
        }
    } else if ('006' == mapEngineType) { // 星云-高德引擎
        url = ZHSQ_EVENT_URL+'/zhsq/map/arcgis/arcgis/spgisCrossDomain.jhtml?' + data;
    } else {
        url = ZHSQ_ZZGRID_URL+'/zzgl/map/gis/popMapMarkerSelectorCrossDomainNew.jhtml?' + data;
    }
    showMaxJqueryWindowForCross("地理位置", url,width,height);
}


//默认的回调函数，定位数据回调赋值可根据各个页面参数名称不同进行调整
function mapMarkerSelectorCallback(mapt, x, y){//接收标注选项的回调函数（名称固定）
    $("#mapt").val(mapt);//地图类型
    $("#x").val(x);
    $("#y").val(y);
    $("#mapTab2").html("修改地理位置");
    closeMaxJqueryWindowForCross();
}
