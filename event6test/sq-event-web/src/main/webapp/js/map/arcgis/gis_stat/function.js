window.onload = function(){
/*-------------------------------专题图层显示隐藏-------------------------------------*/
	//统计数据显示
	$(".ssicon").click(function() {
		var lineDiv1 = $("#lineDiv1");
		var boxDiv1 = $("#boxDiv1");
		if(lineDiv1.css("display") == 'none') {
			boxDiv1.show();
			lineDiv1.show();
		}
	});
	//统计数据隐藏
	$("#sjiconDiv1").click(function() {
		var lineDiv1 = $("#lineDiv1");
		var boxDiv1 = $("#boxDiv1");
		if(lineDiv1.css("display") != 'none') {
			boxDiv1.hide();
			lineDiv1.hide();
		}
	});
/*--------------------------------------------------------------------*/
}
/**
*2015-09-06 liushi add
*统计大类的点击触发（显示或隐藏下面的具体统计项）
**/
var currentMenuCode='';
function classificationClick(elementsCollectionStr){
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	if(currentMenuCode != undefined && currentMenuCode != '' && currentMenuCode != menuCode) {
		var currentObj = $("#"+currentMenuCode);
		if(currentObj.css("display") != 'none') {
			currentObj.hide();
		}
	}
	currentMenuCode = menuCode;
	var nextObj = $("#"+menuCode);
	if(nextObj.css("display") != 'none') {
		nextObj.hide();
	}else {
		nextObj.show();
	}
}
function showObjectStat(elementsCollectionStr,gridId) {
	if(gridId == undefined){
		gridId = $("#gridId").val();
	}
	
	var url = analysisOfElementsCollection(elementsCollectionStr,"menuListUrl");
	if(url.indexOf('http://')<0){
		url = js_ctx + url;
	}
	if(url.indexOf("?")<=0){
		url += "?t="+Math.random();
	}
	url += "&gridId="+gridId;
	var html = '<form action="'+url+'" method="post" target="_self" id="postData_form">'+  
       '<input id="elementsCollectionStr" name="elementsCollectionStr" type="hidden" value="'+elementsCollectionStr+'"/>'+  
       '</form>';
    document.getElementById('stat_frme').contentWindow.document.write(html);  
	document.getElementById('stat_frme').contentWindow.document.getElementById('postData_form').submit();
	$(".ssicon").click();
}
//解析字符串
function analysisOfElementsCollection(elementsCollectionStr,elementsName){
	var ecs = elementsCollectionStr.split(",_,");
	var eclist = new Array();
	for(var i=0;i<ecs.length;i++){
		var e = ecs[i].split("_,_");
		if(elementsName == e[0]){
			return e[1];
		}
	}
	return "";
}
/******END LYJJ********/

