window.oa = window.oa || {},

/**
 * 初始化地图map对象
 * @param {BMap.Map}
 */
oa.BMap = function(map){
	this.map = map;
};

oa.BMap.prototype.getMap = function(){
	return this.map;
};

/**
 * 根据经纬度获取地图Point对象
 * @param lng 经度
 * @param lat 纬度
 * @returns {BMap.Point}
 */
oa.BMap.prototype.getPoint = function(lng,lat){
	return new BMap.Point(lng,lat);
};

/**
 * 获取自定义图标
 * @param path 图标访问路径
 * @param width 图片宽度
 * @param height 图片高度
 * @returns {BMap.Icon}
 */
oa.BMap.prototype.getIcon = function(path,width,height){
	return new BMap.Icon(path,new BMap.Size(width,height),{
		offset: new BMap.Size(10, 25),
		imageOffset: new BMap.Size(0,0)
	});
};
/**
 *  根据经纬度获取地图Marker对象
 * @param {BMap.Point} 对象
 * @param {BMap.Icon} 对象
 * @returns {BMap.Marker}
 */
oa.BMap.prototype.getMarker = function (point,icon){
	if(!!icon){
		return new BMap.Marker(point,{icon:icon});
	}else{
		return new BMap.Marker(point);
	}
};
/**
 * marker添加点击事件,弹出信息窗口
 * @param {BMap.InfoWindow}
 * @param {BMap.Marker}
 * @returns 
 */
oa.BMap.prototype.addClickHander_marker_showInfoWindow = function(infoWindow,marker){
	marker.addEventListener("click",function(e){
		var p = e.target;
		var point = new BMap.Point(p.getPosition().lng,p.getPosition().lat);
		this.map.openInfoWindow(infoWindow,point);
	});
};

/**
 * 根据marker添加地图Marker对象
 * @param {BMap.Marker} 对象
 */
oa.BMap.prototype.addMarker = function(marker){
	this.map.addOverlay(marker);
};
/**
 * 根据经纬度点添加地图Marker对象
 * @param {BMap.Point} 对象
 * @param {BMap.Icon} 对象
 */
oa.BMap.prototype.addMarkerFromPoint = function(point,icon){
	var marker = this.getMarker(point,icon);
	this.map.addOverlay(marker);
};
/**
 * 普通信息窗口
 * @param title 标题
 * @param htmlContent 内容
 * @param width 窗口宽度
 * @param height 窗口高度
 * @returns {BMap.InfoWindow}
 */
oa.BMap.prototype.getInfoWindow = function(title,htmlContent,width,height){
	var defalutOpts = {
		width : 200,	// 信息窗口宽度
		height: 100,	// 信息窗口高度
		title : "<span style='font-weight:bold;margin-bottom:10px;'>信息窗口</span>"	// 信息窗口标题
	};
	if(title){
		title="<span style='font-weight:bold;margin-bottom:10px;'>"+title+"</span>";
	}
	var opts = {
	  width : width?width:defalutOpts.width,	// 信息窗口宽度
	  height: height?height:defalutOpts.height,	// 信息窗口高度
	  title : title?title:defalutOpts.title		// 信息窗口标题
	};
	return new BMap.InfoWindow(htmlContent,opts);
};

/**
 * 显示简单信息定位地址窗口
 * @param {BMap.Point}
 * @param address
 */
oa.BMap.prototype.showSimpleInfoWindow = function(point,address){
	var content = "<b>经度:</b>"+point.lng+"<br />";
	content += "<b>纬度:</b>"+point.lat+"<br />";
	content += "<b>地址:</b>"+address+"<br />";
	var infow = this.getInfoWindow("信息窗口",content);
	this.map.openInfoWindow(infow,point);
	
};
/**
 * 显示简单信息定位地址窗口
 * @param {BMap.Point}
 * @param address
 */
oa.BMap.prototype.showSimpleAddressInfoWindow = function(point,address,title){
	var infow = this.getInfoWindow(title,address);
	this.map.openInfoWindow(infow,point);
	
};

/**
 * 根据西南和东北方向的2个点,获取对应的矩形区域
 * @param {BMap.Point} 对象 
 * @param {BMap.Point} 对象
 * @return {BMap.Bounds} 对象
 */
oa.BMap.prototype.getBounds = function(swPoint,enEnd){
	return new BMap.Bounds(swPoint,enEnd);
};

/**
 * 删除所有覆盖物
 */
oa.BMap.prototype.removeAll = function(){
	this.map.clearOverlays();
};
/**
 * 地图移动到点
 * @param {BMap.Point} 对象 
 */
oa.BMap.prototype.moveTo = function(swPoint){
	this.map.panTo(swPoint);
};
/**
 * 添加覆盖物
 * @param {BMap.Overlay} 对象 
 */
oa.BMap.prototype.addOverlay = function(o){
	this.map.addOverlay(o);
};
/**
 * 地图移至到点
 * @param {BMap.Point} 对象 
 * @param scale缩放级别,默认值16
 */
oa.BMap.prototype.centerAndZoom = function(point,scale){
	if(scale){
		this.map.centerAndZoom(point, scale);
	}else{
		this.map.centerAndZoom(point, 16);
	}
};

/**
 * 获取地理 解析器 (将经纬度转换为地址)
 * @return {BMap.Geocoder} 对象
 */
oa.BMap.prototype.getGeocoder = function(){
	return new BMap.Geocoder();
};

/**
 * 逆地理编码 (将经纬度地址转换为地址)
 * @param {BMap.Point} 对象
 * @param callback 回调函数,函数参数为解析后地址
 */
oa.BMap.prototype.getAddress = function(point,callback){
	this.getGeocoder().getLocation(point,function(rs){
		var addComp = rs.addressComponents;
		var parseAddress = addComp.province+addComp.city+addComp.district+addComp.street+addComp.streetNumber;
		if(typeof(callback) == 'function'){
			callback(parseAddress);
		}
	});
};

/**
 * 地理编码 (将地址转换为经纬度)
 * @param address 待解析的地址
 * @param addressRange 待解析地址范围
 * @param callback 回调函数,函数参数为解析后地址
 */
oa.BMap.prototype.parseAddress = function(address,addressRange,callback){
	this.getGeocoder().getPoint(address,function(point){
		if(point){
			if(typeof(callback) == 'function'){
				callback(point);
			}
		}else{
			alert("您选择地址没有解析到结果");
		}
	},addressRange);
};
/**
 * 获取2点之前的距离
 * @param {BMap.Point} 待解析的地址
 * @param {BMap.Point} 待解析地址范围
 * @param precision 精度(默认值2)
 * @return 距离值(单位米) 
 */
oa.BMap.prototype.getDistance = function(pointA,pointB,precision){
	if(!precision)
		return this.map.getDistance(pointA,pointB).toFixed(2);
	else
		return this.map.getDistance(pointA,pointB).toFixed(precision);
		
};

/**
 * GPS坐标转百度坐标
 * @param {BMap.Point} 待转换的gps坐标
 * @param translateCallback 转换后的回调函数
 */
oa.BMap.prototype.GPS2BD = function(point,translateCallback){
	var convertor = new BMap.Convertor();
    var pointArr = [];
    pointArr.push(point);
    convertor.translate(pointArr, 1, 5, translateCallback);
};

/**
 * GPS坐标转百度坐标(批量转换)
 * @param id String 批量转换的id,线路id,可以随意
 * @param [{BMap.Point}] 批量的待转换的gps坐标
 * @param translateCallback 转换后的回调函数,参数为转换后点的数组
 */
oa.BMap.prototype.batchGPS2BD = function(id,pointArr,translateCallback,sort){
	var len = parseInt(pointArr.length/10)+1;
	var div = pointArr.length%10;
	var transArr = [];
	var transCount = 0;//已经转换的次数
	var innerCallbak = function(data){//异步回调
		transCount++;
		if(data.status === 0) {
			for (var i = 0; i < data.points.length; i++) {
				transArr.push(data.points[i]);
			}
		}else{
			console.log('转换失败'+data.message);
		}
		if(len == transCount && pointArr.length==transArr.length){
			translateCallback(transArr);
		}
	};
	if(len==1 && div==0){
		div = pointArr.length;
	}
	if(pointArr.length%10 == 0){
		len--;
	}
	var convertor = new BMap.Convertor();
	_inner_pointArr.put('id_'+id, []);//添加到map中
	//1与5表示从GPS转百度:参考:http://lbsyun.baidu.com/index.php?title=webapi/guide/changeposition
	for(var i=0;i<len;i++){
		if(i==len-1){
			if(sort){
				c(id,i,len,pointArr.slice(i*10),translateCallback);
			}else{
				convertor.translate(pointArr.slice(i*10), 1, 5,innerCallbak);//截取到尾部
			}
		}else{
			if(sort){
				c(id,i,len,pointArr.slice(i*10,(i+1)*10),translateCallback);
			}else{
				convertor.translate(pointArr.slice(i*10,(i+1)*10),1,5,innerCallbak);
			}
		}
	}
};

var _inner_pointArr = new Map();
function c(id,index,total,pointArr,callback){
	var convertor = new BMap.Convertor();
	convertor.translate(pointArr, 1, 5,function(data){
		if(data.status === 0){
			_inner_pointArr.get('id_'+id)[index] = data.points;
			var finish = true;
			for(var i=0;i<total;i++){
				if(!_inner_pointArr.get('id_'+id)[i]){
					finish = false;
				}
			}
			if(finish){
				var res = [];
				for(var i=0;i<_inner_pointArr.get('id_'+id).length;i++){
					for(var j=0;j<_inner_pointArr.get('id_'+id)[i].length;j++){
						res.push(_inner_pointArr.get('id_'+id)[i][j]);
					}
				}
				callback(res);
			}
		}else{
			console.log('转换失败....'+data.message);
		}
	});
}

/**
 * 根据点划线
 * @param [{BMap.Point}] 批量的待转换的gps坐标
 * @param opt 线参数
 */
oa.BMap.prototype.drawLine = function(pointArr,opt){
	var defaultLineOpt = {strokeColor:"blue", strokeWeight:6, strokeOpacity:0.5};
	var realOpt = $.extend({},defaultLineOpt,opt);
	var polyline = new BMap.Polyline(pointArr,realOpt);
	this.map.addOverlay(polyline);  
};

/**   
 *定义map方法     
 */
function Map() {
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	/** 存放数据 */
	this.data = new Object();

	/**   
	 * 放入一个键值对   
	 * @param {String} key   
	 * @param {Object} value   
	 */
	this.put = function(key, value) {
		if (this.data[key] == null) {
			this.keys.push(key);
		}
		this.data[key] = value;
	};

	/**   
	 * 获取某键对应的值   
	 * @param {String} key   
	 * @return {Object} value   
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/**   
	 * 删除一个键值对   
	 * @param {String} key   
	 */
	this.remove = function(key) {
		this.keys.remove(key);
		this.data[key] = null;
	};

	/**   
	 * 删除保存的根节点   
	 * @param {String} key    
	 */
	this.removeRootNode = function() {
		if (!this.keys.length)
			return false;
		for ( var i = 0; i < this.keys.length; i++) {
			if (this.data[i]) {
				if (this.data[i].rootType) {
					return false;
				}
				this.keys = new Array();
				this.data = new Object();
				return true;
			}
		}
		return false;
	};

	/**   
	 * 遍历Map,执行处理函数   
	 *    
	 * @param {Function} 回调函数 function(key,value,index){..}   
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len = this.keys.length;
		for ( var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};

	/**   
	 * 获取键值数组(类似Java的entrySet())   
	 * @return 键值对象{key,value}的数组   
	 */
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for ( var i = 0; i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};

	/**   
	 * 判断Map是否为空   
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/**   
	 * 获取键值对数量   
	 */
	this.size = function() {
		return this.keys.length;
	};

	//删除MAP所有元素
	this.clear = function() {
		this.keys = new Array();
		this.data = new Object();
	};

	/**   
	 * 重写toString    
	 */
	this.toString = function() {
		var s = "{";
		for ( var i = 0; i < this.keys.length; i++, s += ',') {
			var k = this.keys[i];
			s += k + "=" + this.data[k];
		}
		s += "}";
		return s;
	};
}