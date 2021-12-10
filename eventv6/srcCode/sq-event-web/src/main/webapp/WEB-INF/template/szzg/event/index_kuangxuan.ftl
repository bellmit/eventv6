<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件框选统计</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<link href="${rc.getContextPath()}/css/kuangxuan.css" rel="stylesheet" type="text/css" />
</head>

<body onload="init()" style="background-color: rgba( 40, 76, 98, 0.9);">
<div class="pop-spot-box">
        <div class="pop-box">
            <ul class="pop-txt">
                <li class=""><p>框选面积：<strong>${area}</strong>平方公里</p></li>
                <li class=""><p>事件总量：<a href="javascript:showList(0);">${eventCount}</a><span style="cursor:pointer;" onclick="showList()">(点击弹出列表)<span></p></li>
                <li class=""><p>事件密集度：<strong id="midu"></strong>件/平方公里</p></li>
            </ul>
            <div class="zt-icon" onclick="showList()" style="margin:0px;margin-top:20px;">
                <div id="icon_1" class="zt-icon1"></div>
                <div id="icon_2" class="zt-icon2"></div>
                <div id="icon_3" class="zt-icon3"></div>
            </div>
        </div>
        <a class="pop-close"></a>
    </div>

</body>

<script type="text/javascript">
function showList(){
	var url = '/zhsq/szzg/eventController/kuangxuanListPage.jhtml?dateNo=${dateNo}&eventCount=${eventCount}';
	//if(parent.lastDateNo >0){
		//url += '&lastDateNo='+parent.lastDateNo;
	//}
	parent.getDetailOnMapOfListClick('nopan_,_true,_,top_,_0,_,menuSummaryWidth_,_1200,_,menuSummaryHeight_,_'+($(parent.document).height()-100)+',_,menuLayerName_,_EVENT_STATISTICS1,_,menuName_,_事件列表,_,menuSummaryUrl_,_'+url+'&wid=',320,110,1,undefined,undefined,undefined,true);
}
function init(){
	parent.lastDateNo = ${dateNo};
	var v = parseFloat((${eventCount}/${area}).toFixed(1));
	$("#midu").html(v);
	var scopeArr = [];
	<#list scope as s>
		scopeArr.push(parseFloat((${s.dictName}).toFixed(1)));
	</#list>
	if(scopeArr.length==0){
		alert('字典没配');
		return;
	}
	scopeArr = paixu(scopeArr);
	var icon=1;
	for(var i=0;i<scopeArr.length;i++){
		if(v <= scopeArr[i]){
			break;
		}
		icon ++;
	}
	$("#icon_"+icon).show();
}
function paixu(arr){
	var temp,l=arr.length;
	for(var i=0;i<l;i++){
		for(var j=i+1;j<l;j++){
			if(arr[i]>arr[j]){
				temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
	}
	return arr;
}
</script>
</html>
