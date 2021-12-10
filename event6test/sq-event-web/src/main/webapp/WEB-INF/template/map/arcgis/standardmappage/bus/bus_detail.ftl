<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>线路信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<style>
.ManagerInfo .left{
    font-weight: normal;
    padding: 0;
    font-size: 12px;
    width: 40%;
    display: inline-block;
}
.ManagerInfo .right{
    font-weight: normal;
    padding: 0;
    font-size: 12px;
    float: right;
    width: 40%;
    display: inline-block;
    margin-right: 20px;
}
.ManagerInfo ul li{width:100%;}
.ManagerSearch .c-title{
	color: #0075a9;
    font-size: 13px;
    font-weight: bold;
    padding-right: 10px;
    display:inline-block;
}
.ManagerSearch .c-data{
    font-size: 12px;
    color: #666;
    display:inline-block;
}
.ManagerSearch .sprator{
    color: #0075a9;
    display:inline-block;
}
</style>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color:rgba(238, 238, 238, .4);">
	<div style="margin:2px 3px 0px 3px;">
	<#if data?exists>
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                    <li>
                    	<p>
                    		<span class="left"><code>线路名称：</code><span>${data.buslineName!''}</span></span>
                        	<span class="right"><code>基本票价：</code>${data.priceBase!''} 元</span>
                    	</p>
                        <p>
                        	<span class="left"><code>上行时间：</code>${data.stimeUp!''}~${data.etimeUp!''}</span>
                        	<span class="right"><code>下行时间：</code>${data.stimeDown!''}~${data.etimeDown!''}</span>
                        </p>
                        <p>
                        	<span class="left"><code>上行里程：</code>${data.courseUp!''} 千米</span>
                        	<span class="right"><code>下行里程：</code>${data.courseDown!''} 千米</span>
                        </p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
            <!--
            <div class="ManagerContact">
            	<ul>
                	<li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                	<li id="callPhone" class="YellowBg" onclick="showCall('','','');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />语音盒呼叫</li>
                	<li id="sendMsg" class="CyanBg" onclick="sendMessage('','');"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_3.png" />发送短信</li>
            		<li id="mmp" class="BlueBg" onclick="mmp();"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_5.png" />视频呼叫</li>
                </ul>
                <div class="clear"></div>
            </div>
            --> 
            <div class="h_10"></div>
            <div class="ManagerSearch">
            	<div class="nav"> 
                    <ul>
                        <li class="current">车辆及站点信息</li>
                    </ul>
                    <div class="line"></div>
                </div>
                <div class="con" style="overflow: auto;max-height: 125px;border-bottom: 1px solid #eee;">
                	<div>
	                	<span class="c-title">途经车辆：</span>
	                	<#list carList as car>
	                	<span class="c-data">${car.carNo}</span>
	                	</#list>
                	</div>
                	<div style="margin-top: 5px;">
	                	<span class="c-title">上行站点：</span>
	                	<#list upStation as sta><span class="c-data">${sta.stationName}</span><#if sta_index+1!=upStation?size><span class="sprator">--</span></#if></#list>
                	</div>
                	<div style="margin-top: 5px;">
	                	<span class="c-title">下行站点：</span>
	                	<#list downStation as sta><span class="c-data">${sta.stationName}</span><#if sta_index+1!=downStation?size><span class="sprator">--</span></#if></#list>
                	</div>
                    <div class="clear"></div> 
                </div>
            </div>
        </div>
    <#else>
    <table cellpadding="0" cellspacing="0" border="0"  class="searchList-2">
    	<tr style="height: 185px"><td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty" >未查到相关数据！</td></tr>
    </table>
    </#if>
		
		
	</div>
	
</body>
<script type="text/javascript">
	function toFixNum(num){
		return new Number(num/1000000).toFixed(6);
	}
	var zhsq_url = "${SQ_ZHSQ_EVENT_URL}";
	var carList = {};
	var points = [];//二维数组线路
	var lineOpt = [{lineColor:'#ff0000',lineStyle:'solid'},{lineColor:'#0000ff',lineWidth:1}];//线路样式,一维数组
	var inPoints = [];
	<#list upStation as ups>
		inPoints.push({x:toFixNum(${ups.lat}),y:toFixNum(${ups.lng})});
	</#list>
	points.push(inPoints);
	inPoints = [];
	<#list downStation as downs>
		inPoints.push({x:toFixNum(${downs.lat}),y:toFixNum(${downs.lng})});
	</#list>
	points.push(inPoints);
	
	if(parent.notFirstLoad){
		window.parent.drawMultiLine('busLayer',points,lineOpt);
	}else{
		parent.notFirstLoad = true;
		window.parent.drawMultiLine('busLayer',points,lineOpt);
		setTimeout(function(){
			window.parent.drawMultiLine('busLayer',points,lineOpt);
		},500);//arcgis的bug,,不然2个的样式就会相同
	}
	
	
	var carList = [];
	<#list carList as car>
		carList.push({devId:'${car.devId}',carNo:'${car.carNo}'});
	</#list>
	parent.addSinglePointInfo('test',points[0][0].x,points[0][0].y,'images/icon_taxi.png','测试');
</script>
</html>
