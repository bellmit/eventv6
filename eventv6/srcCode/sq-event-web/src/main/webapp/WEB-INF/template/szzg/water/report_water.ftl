<!DOCTYPE html>
<html>
<head>
	<title>水质量</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
		
  	<script type="text/javascript" src="${rc.getContextPath()}/js/echarts/echarts-all.js"></script>
  	<style type="text/css">
	body {
	    font-family: "微软雅黑";
	    font-size: 0.875em;
	    color: #fff;
	   // background: rgba(40,71,82,0.5);
	}
	.clearfloat{ clear:both; height:0; font-size:1px; line-height:0;}
	h3 {
	    font-size: 18px;
	    padding: 8px 0 0 25px;
	}

	.bombcon table{ border-collapse:collapse;}
    .bombcon table td{ border:1px solid #6d8ca4; padding:5px;}
    .buildcon{ margin-top:10px;}
	.buildleft{
	 margin-top:10px; float:left; border:1px solid #81a1b9; width:188px;
	 min-height:400px;}
	.buildleft-tit{border-bottom:1px solid #81a1b9; height:37px; line-height:37px;}

	.noline,.build-dq{
		border-left: 1px solid #81a1b9 !important;
	}
	.build-dq{
	 background: rgba(7,141,210,0.85);
	}
	select {
	    border: solid 1px #fff;
	    background: none;
	    color: #fff;
	    padding: 3px 15px;
	    margin-right: 5px;
	}

	</style>
</head>
<body>
	<#if (list?size>0) >
	 <div style="text-align:right;margin-right: 10px">
	   检测时间：${list[0].endTime?string("yyyy年MM月dd日")}
	 </div>
    </#if>
    <div class="bombcon" id="content-d"   style="overflow-x:hidden;overflow-y:auto;height:250px;margin-top:10px" > 
                <table  width="100%" border="0" cellspacing="0" cellpadding="0">
             		<tr class="coltit">
                      <td align="center">监测站点名称</td> 
                      <td align="center">水质类别</td>
                      <td align="center">图标</td>
                      <td align="center">定位</td> 
                    </tr>
                     <tbody >
                    <#if (list?size>0) >
                      <#list list as obj >
	                     
	                  		 <tr>
		                      <td align="center">${obj.name!}</td>
		                      <td align="center">${obj.szlb!}</td>
		                      <td align="center"><#if obj.szlb??><img src="${rc.getContextPath()}/css/images/${obj.szlb!}.png" width="14" height="20"></#if></td>
		                      <td align="center" onclick="showXY('${obj.name!}','${obj.longitude!}','${obj.dimensions!}')"><img src="${rc.getContextPath()}/images/map_jiangyin/aqi-icon1.png" width="20" height="20" /></td>
		                     </tr>
	               	  </#list>
	               	  <#else>
	               	  <tr>
	               	 	 <td  colspan='4' align="center" style="color:#fd7f7f"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png"></td>
	               	  </tr>
					</#if>
                  </tbody>
                </table>
      </div>
      <div style="text-align:center;margin-top:10px">
	      I <img src="${rc.getContextPath()}/css/images/I.png" width="14" height="20"> &nbsp; &nbsp;
	      II <img src="${rc.getContextPath()}/css/images/II.png" width="14" height="20">&nbsp; &nbsp;
	      III <img src="${rc.getContextPath()}/css/images/III.png" width="14" height="20">&nbsp; &nbsp; 
	      IV <img src="${rc.getContextPath()}/css/images/IV.png" width="14" height="20">&nbsp; &nbsp; 
	               劣V <img src="${rc.getContextPath()}/css/images/V.png" width="14" height="20">&nbsp; &nbsp; 
       </div>
<script type="text/javascript"> 

	$(function(){
		//改变滚动条样式
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
			var arr=new Array()
	       <#if (list?size>0) >
              <#list list as obj >
                 	var aqi = new Object();   　　//创建对象  
					aqi.name='${obj.name!}';
					aqi.x='${obj.longitude!}';
					aqi.y='${obj.dimensions!}';
					if(aqi.x!='')
						arr.push(aqi);
           	  </#list>
			</#if>
			var mapLocationObject = {
      		 locationMaplist : arr
			};
			window.parent.locationPointsOnMap(mapLocationObject);
		
    });   
 	function showXY(name,x,y){
		var mapLocationObject = {
		       locationMaplist : [{
		           name : name,
		           x : x,
		           y : y
		       }]
		};
		if(x!=''){
			window.parent.locationPointsOnMap(mapLocationObject);
		}
	}
	function noData(id){
	var div = document.getElementById(id);
	var w = div.style.width,h = div.style.height;
	if(w.length == 0 || h.length == 0){
		w = "100px",h = "120px";
	}
	div.innerHTML = "<div style='width:"+w+";height:"+h+";vertical-align: middle;text-align:center;display: table-cell;'><img src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
	}
</script>
</body>

</html>
