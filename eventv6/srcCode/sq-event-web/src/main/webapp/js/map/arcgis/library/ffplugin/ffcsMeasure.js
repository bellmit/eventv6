//测距相关变量
 var clickNum = -1; //点的次数
 var pointArr = []; //存放一次测距时画的所有点
 var pointArrProj;
 var length = 0; //测距单段距离
 var lengthZ = 0; //累计长度
 var pointLength; //测距点
 var pointLengthProj;
 var lengthGraphicsArr = []; //二维数组，用来存储每次测距的各图形信息
 var lengthNum = 0; //总共测距的次数
 var delGraphicArr = []; //删除按钮的数组
 var allLineGrap = null; //线图形
 var textDivArr = []; //存放距离值的div
 var evLine = null; //线几何
 
 var pointAreaArr = []; //存放一次测面积时画的所有点
 
//面积相关变量
 var area = 0; //最近三点面积
 var areaZ = 0; //累计面积
 var pointAreaArr = []; //存放一次测面积时画的所有点
 var pointAreaArrProj; //坐标系转换过后的点
 var clickAreaNum = -1; //点的次数
 var pointArea; //测面积点
 var pointAreaProj;
 var areaGraphicsArr = []; //用于存储每次测面积时画的图形
 var areaNum = 0; //测面积的次数
 var delAreaGraphicsArr = []; //删除按钮的数组
 var areaUnit = "squareKiloMeters"; //hectare(公顷),squareMeter(平方米),squareKiloMeters(平方公里),acres(亩)；一亩=666.7平方米，一公顷=10000平方米，一平方公里=1000000平方米
 var CheckedRulerS = 1; //1页面中没有起点;2图中已经有了起点
 var textDivArr1 = []; //存放面积值的div
 var allPolygonGrap = null; //面图形
 var evPolygon = null; //面几何
 var allPolygonGrapLine = null; //活动线图形
 var evPolygonLine = null; //活动线几何
 var measureLabel = "";//测量数值
 var mmap;
	 
function Measure(map,toolbar){
	this.Mmap = map;
	mmap=map;
	this.Mtoolbar = toolbar;
}

Measure.prototype.caluDistance = function(){
	 if (this.Mmap.graphics == null) { return; }
    else {
       if (pointAreaArr.length > 0) {
           var json = "";
           for (var i = 0; i < pointAreaArr.length; i++) {
               json += "[" + pointAreaArr[i].x + "," + pointAreaArr[i].y + "],";

           }
           json += "[" + pointAreaArr[0].x + "," + pointAreaArr[0].y + "]";
           json = "[[" + json + "]]";
           var polylineJson = "{\"paths\":" + json + ",\"spatialReference\":{\"wkid\":" + map.spatialReference["wkid"] + "}}";

           polylineJson = JSON.stringify(polylineJson);
           var geometry = new esri.geometry.Polyline(polylineJson);

           checkArea(this, geometry);
       } else if (CheckedRulerS == 2 && pointArr.length > 0) {
           var json = "";
           for (var i = 0; i < pointArr.length; i++) {
               json += "[" + pointArr[i].x + "," + pointArr[i].y + "],";
           }
           json = json.substring(0, json.length - 1);
           json = "[[" + json + "]]";
           var polylineJson = "{\"paths\":" + json + ",\"spatialReference\":{\"wkid\":" + map.spatialReference["wkid"]  + "}}";

           polylineJson = JSON.stringify(polylineJson);
           var geometry = new esri.geometry.Polyline(polylineJson);
           var Tmg = this;
           checkOut(Tmg, geometry);
       } else {
           clickNum = -1;
           pointArr = [];
           length = 0;
           lengthZ = 0;
           pointLength = null;
           this.Mmap.drawMode = "measureLengthD";
           this.Mtoolbar.activate(esri.toolbars.Draw.POLYLINE);
           this.Mmap.disablePan();
       }
    }
};	
	 
//处理测距结果
 Measure.prototype.checkOut = function(thisMap, geometry){
	 pointLength = pointArr[pointArr.length - 1];
     /*if (lengthNum > 0) {
         var delGraphic = new esri.Graphic(pointLength, new esri.symbol.PictureMarkerSymbol("images/del.png", 14, 13).setOffset(10, -10));
         thisMap.graphics.add(delGraphic);
         delGraphicArr.push(delGraphic);
         lengthGraphicsArr[lengthNum - 1].push(delGraphic);
     }*/
     clickNum = -1;
     pointArr = [];
     length = 0;
     lengthZ = 0;
     pointLength = null;
     evLine = null;

     this.Mtoolbar.deactivate();
     thisMap.drawMode = "pan";
     thisMap.enablePan();
     document.getElementById(thisMap.id + "_container").style.cursor = "auto";
     CheckedRulerS = 1;

     var div = document.getElementById("pointOutDiv");
     if (div) {
         div.parentNode.removeChild(div);
     }
 };
 
 
 Measure.prototype.pointOutDbClickEnd = function(x, y, label){
	 var tipDiv = document.getElementById("pointOutDiv");
	    if (!tipDiv) {
	        tipDiv = document.createElement("div");
	        tipDiv.id = "pointOutDiv";
	        tipDiv.style.position = "absolute";
	        tipDiv.style.height = "40px";
	        tipDiv.style.zIndex = 1100;
	        tipDiv.style.left = x + "px";
	        tipDiv.style.right = "auto";
	        tipDiv.style.top = y + "px";
	        tipDiv.style.bottom = "auto";
	        tipDiv.innerHTML = "<span style='text-decoration:none;font-size:12px;color:#393939;display:inline-block;float;left;border:1px solid #33A1C9;background-color: white;'>" + label + "</span>";
	        document.body.appendChild(tipDiv);
	    } else {
	        tipDiv.innerHTML = "<span style='text-decoration:none;font-size:12px;color:#393939;display:inline-block;float;left;border:1px solid #33A1C9;background-color: white;'>" + label + "</span>";
	        tipDiv.style.left = x + "px";
	        tipDiv.style.top = y + "px";
	        tipDiv.style.display = "block";
	    }
	measureLabel = label;
 };
 
 

 Measure.prototype.createTextDiv = function(thisMap, point, num, label) {
     var screen = thisMap.toScreen(point);
     var div = textDivArr[num].get(point);
     if (!div) {
         div = document.createElement("div");
         div.style.position = "absolute";
         div.style.left = (screen.x + 10) + "px";
         div.style.right = "auto";
         div.style.zIndex = 850;
         div.style.top = (screen.y - 15) + "px";
         div.style.bottom = "auto";
         div.innerHTML = "<span style='text-decoration:none;font-size:12px;color:#393939;display:inline-block;float;left;border:1px solid #33A1C9;background-color: white;'>" + label + "</span>";
         var mapdiv = document.getElementById(thisMap.id);
         mapdiv.appendChild(div);
         textDivArr[num].element(textDivArr[num].elements.length - 1).value = div;
     } else {
         div.style.left = (screen.x + 10) + "px";
         div.style.top = (screen.y - 15) + "px";
         div.style.display = "block";
     }
 }
 

 Measure.prototype.getDistanceInEarth = function (point1, point2) {
     var d = new Number(0);
     var radPerDegree = Math.PI / 180.0;
     var srsId = this.Mmap.spatialReference["wkid"] ;
	if (srsId == "4326" || srsId == "4490" ) {
         var latLength1 = Math.abs(this.translateLonLatToDistance({ x: point1.x, y: point2.y }).x - this.translateLonLatToDistance({ x: point2.x, y: point2.y }).x);
         var latLength2 = Math.abs(this.translateLonLatToDistance({ x: point1.x, y: point1.y }).x - this.translateLonLatToDistance({ x: point2.x, y: point1.y }).x);
         var lonLength = Math.abs(this.translateLonLatToDistance({ x: point1.x, y: point2.y }).y - this.translateLonLatToDistance({ x: point1.x, y: point1.y }).y);
         d = Math.sqrt(Math.pow(lonLength, 2) - Math.pow(Math.abs(latLength1 - latLength2) / 2, 2) + Math.pow(Math.abs(latLength1 - latLength2) / 2 + Math.min(latLength1, latLength2), 2));
     } else {
         var len_prj = Math.pow((point2.x - point1.x), 2) + Math.pow((point2.y - point1.y), 2);
         d = Math.sqrt(len_prj);
     }
     d = Math.ceil(d);
     return d;
 };
 
 Measure.prototype.translateLonLatToDistance = function (point) {
    var d = new Number(0);
    var radPerDegree = Math.PI / 180.0;
    var equatorialCircumference = Math.PI * 2 * 6378137;

    return {
        x: Math.cos(point.y * radPerDegree) * equatorialCircumference * Math.abs(point.x / 360),
        y: equatorialCircumference * Math.abs(point.y / 360)
    };
};

Measure.prototype.dealDelLeng = function (evt) {
    if (lengthNum > 0) {
        //var thisMap = this._map;
        var grap = evt.graphic;
        for (var i = 0; i < lengthNum; i++) {
            if (grap == delGraphicArr[i]) {
                var elements = textDivArr[i].elements;
                for (var k = 0; k < elements.length; k++) {
                    elements[k].value.parentNode.removeChild(elements[k].value);
                }
                //textDivArr.remove(textDivArr[i]);
                for (var j = lengthGraphicsArr[i].length - 1; j >= 0; j--) {
                    map.graphics.remove(lengthGraphicsArr[i][j]);
                }
                //lengthGraphicsArr.remove(lengthGraphicsArr[i]);
                //delGraphicArr.remove(delGraphicArr[i]);
                lengthNum--;
                break;
            }
        }
    }
};

//动态测面积
Measure.prototype.measureAreaD = function () {
    if (this.Mmap.graphics == null) { return; }
    else {
        if (pointArr.length > 0) {
            var json = "";
            for (var i = 0; i < pointArr.length; i++) {
                json += "[" + pointArr[i].x + "," + pointArr[i].y + "],"
            }
            json = json.substring(0, json.length - 1);
            json = "[[" + json + "]]";
            var polylineJson = "{\"paths\":" + json + ",\"spatialReference\":{\"wkid\":" + map.spatialReference["wkid"] + "}}";

            polylineJson = JSON.stringify(polylineJson);
            var geometry = new esri.geometry.Polyline(polylineJson);
            var Tmg = this;
            checkOut(Tmg, geometry);
        }
        else if (CheckedRulerS == 2 && pointAreaArr.length > 0) {
            var json = "";
            for (var i = 0; i < pointAreaArr.length; i++) {
                json += "[" + pointAreaArr[i].x + "," + pointAreaArr[i].y + "],";

            }
            json += "[" + pointAreaArr[0].x + "," + pointAreaArr[0].y + "]";
            json = "[[" + json + "]]";
            var polylineJson = "{\"paths\":" + json + ",\"spatialReference\":{\"wkid\":" + map.spatialReference["wkid"] + "}}";

            polylineJson = JSON.stringify(polylineJson);
            var geometry = new esri.geometry.Polyline(polylineJson);

            checkArea(this, geometry);
        }
        else {
            area = 0;
            areaZ = 0;
            pointAreaArr = [];
            clickAreaNum = -1;
            pointArea = null;

            this.Mmap.drawMode = "measureAreaD";
            this.Mtoolbar.activate(esri.toolbars.Draw.POLYLINE);
            this.Mmap.disablePan();
        }
    }
};


//处理测面积结果
Measure.prototype.checkArea = function(thisSDMap, geometry) {
    if (pointAreaArr.length != 1) {
        pointArea = pointAreaArr[pointAreaArr.length - 1];
        /*if (areaNum > 0) { 
            var delGraphic = new esri.Graphic(pointArea, new esri.symbol.PictureMarkerSymbol("http://134.129.94.211:8081"+SDMAPConfig.serverPartUrl + "shanchu.png", 16, 16).setOffset(10, -10));
            thisSDMap.map.graphics.add(delGraphic);
            delAreaGraphicsArr.push(delGraphic);
            areaGraphicsArr[areaNum - 1].push(delGraphic);
        }*/
    } else {
        areaNum--;
        textDivArr1.remove(textDivArr1[areaNum]);

    }
    thisSDMap.graphics.remove(allPolygonGrapLine);
    CheckedRulerS = 1;
    area = 0;
    areaZ = 0;
    pointAreaArr = [];
    clickAreaNum = -1;
    pointArea = null;
    graphicsArea = null;
    evPolygon = null;
    evPolygonLine = null;

    this.Mtoolbar.deactivate();
    thisSDMap.drawMode = "pan";
    thisSDMap.enablePan();
    document.getElementById(thisSDMap.id + "_container").style.cursor = "auto";

    var div = document.getElementById("pointOutDiv");
    if (div) {
        div.parentNode.removeChild(div);
    }
	this.areaDCallBack(geometry.paths[0],measureLabel);
};

Measure.prototype.createTextDiv1 = function(thisMap, point, num, label) {
    var screen = this.Mmap.toScreen(point);
    var div = textDivArr1[num].element(0).value;
    if (!div) {
        div = document.createElement("div");
        div.style.position = "absolute";
        div.style.left = (screen.x + 10) + "px";
        div.style.right = "auto";
        div.style.zIndex = 850;
        div.style.top = (screen.y - 15) + "px";
        div.style.bottom = "auto";
        div.innerHTML = "<span style='text-decoration:none;font-size:12px;color:#393939;display:inline-block;float;left;border:1px solid #33A1C9;background-color: white;'>" + label + "</span>";
        var mapdiv = document.getElementById(thisMap.id);
        mapdiv.appendChild(div);
        textDivArr1[num].element(0).value = div;
    } else {
        if (arguments.length == 4)
            div.innerHTML = "<span style='text-decoration:none;font-size:12px;color:#393939;display:inline-block;float;left;border:1px solid #33A1C9;background-color: white;'>" + label + "</span>";
        div.style.left = (screen.x + 10) + "px";
        div.style.top = (screen.y - 15) + "px";
        div.style.display = "block";
	    }
	    textDivArr1[num].element(0).key = point;
	};
 
	
	Measure.prototype.getTriangleArea = function (point1, point2, point3) {
	    var area = 0;
	    if (!point1 || !point2 || !point3) {
	        return 0;
	    }
	    var srsId = this.Mmap.spatialReference["wkid"] ;
    if (srsId == "4326" || srsId == "4490" ) {
        point1 = this.translateLonLatToDistance(point1);
        point2 = this.translateLonLatToDistance(point2);
        point3 = this.translateLonLatToDistance(point3);
    }
    area = ((point1.x * point2.y - point2.x * point1.y) + (point2.x * point3.y - point3.x * point2.y) + (point3.x * point1.y - point1.x * point3.y)) / 2;
    return area;
};

Measure.prototype.measureClearAll = function(){
	lengthGraphicsArr = [];
    lengthNum = 0;
    delGraphicArr = [];

    areaGraphicsArr = [];
    areaNum = 0;
    delAreaGraphicsArr = [];
	if (textDivArr.length > 0) {
        for (var i = 0; i < textDivArr.length; i++) {
            var record = textDivArr[i];
            if (record.elements.length > 0) {
                for (var j = 0; j < record.elements.length; j++) {
                    record.element(j).value.parentNode.removeChild(record.element(j).value);
                }
            }
        }
    }
	if (textDivArr1.length > 0) {
        for (var i = 0; i < textDivArr1.length; i++) {
            var record = textDivArr1[i];
            if (record.elements.length > 0) {
                for (var j = 0; j < record.elements.length; j++) {
                    record.element(j).value.parentNode.removeChild(record.element(j).value);
                }
            }
        }
    }

    textDivArr = [];
    textDivArr1 = [];
    
    CheckedRulerS = 1;
    area = 0;
    areaZ = 0;
    pointAreaArr = [];
    clickAreaNum = -1;
    pointArea = null;
    graphicsArea = null;

    var div = document.getElementById("pointOutDiv");
    if (div) {
        div.parentNode.removeChild(div);
    }
};


Measure.prototype.onEvent = function(){
	
}

Measure.prototype.areaDCallBack = function(){
	
}

//------------------------------------------﻿//key-value集合类
function ZLKeyValueMapping() {
     this.elements = new Array();

     this.size = function () {
         return this.elements.length;
     }

     this.isEmpty = function () {
         return (this.elements.length < 1);
     }

     this.clear = function () {
         this.elements = new Array();
     }

     this.put = function (_key, _value) {
         this.elements.push({ key: _key, value: _value });
     }

     this.remove = function (_key) {
         var bln = false;

         try {
             for (i = 0; i < this.elements.length; i++) {
                 if (this.elements[i].key == _key) {
                     this.elements.splice(i, 1);
                     return true;
                 }
             }
         } catch (e) {
             bln = false;
         }
         return bln;
     }

     this.get = function (_key) {
         try {
             for (i = 0; i < this.elements.length; i++) {
                 if (this.elements[i].key == _key) {
                     return this.elements[i].value;
                 }
             }
         } catch (e) {
             return null;
         }
     }

     this.element = function (_index) {
         if (_index < 0 || _index >= this.elements.length) {
             return null;
         }
         return this.elements[_index];
     }

     this.containsKey = function (_key) {
         var bln = false;
         try {
             for (i = 0; i < this.elements.length; i++) {
                 if (this.elements[i].key == _key) {
                     bln = true;
                 }
             }
         } catch (e) {
             bln = false;
         }
         return bln;
     }

     this.containsValue = function (_value) {
         var bln = false;
         try {
             for (i = 0; i < this.elements.length; i++) {
                 if (this.elements[i].value == _value) {
                     bln = true;
                 }
             }
         } catch (e) {
             bln = false;
         }
         return bln;
     }

     this.values = function () {
         var arr = new Array();
         for (i = 0; i < this.elements.length; i++) {
             arr.push(this.elements[i].value);
         }
         return arr;
     }

     this.keys = function () {
         var arr = new Array();
         for (i = 0; i < this.elements.length; i++) {
             arr.push(this.elements[i].key);
         }
         return arr;
     }
     
     this.keyEquesNum =  function(_key){
    	 var ekeyNum = 0;
    	 for (var i = 0; i < this.elements.length; i++) {
    		 if (this.elements[i].key.wid != _key.wid) {
    			 var s=caculateLLRB(_key,this.elements[i].key);
    			 if(s){
    				 ekeyNum = ekeyNum + 1;
    			 }
             }
    	 }
    	 
    	 return ekeyNum;
     }
 }
function caculateLLRB(point1,point2) {
			var screen1 = mmap.toScreen(point1);
			var screen2 = mmap.toScreen(point2);
			var x=Math.abs(screen1.x-screen2.x);
			var y=Math.abs(screen1.y-screen2.y);
			var xy2=Math.pow(x,2)+Math.pow(y,2);
			var s=Math.sqrt(xy2);
			if(s<15){
				 return true;
			 }else{
				 return false;
			 }
		}

