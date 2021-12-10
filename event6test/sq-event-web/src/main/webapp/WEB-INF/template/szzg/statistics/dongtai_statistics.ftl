<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>经济运行</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
  .buildleft-list ul li{cursor:pointer;}
  .gdp-list{float:left;}
  .gdp-list ul li{padding:0px;margin:0px;min-width:80px;float:left;height:20px;text-align:center;padding-top:5px;}
  .header td{border:1px solid #6d8ca4}
  .circle {width:178px;height:178px;background:#fff;-moz-border-radius:89px;-webkit-border-radius:89px;
  border-radius:89px;position:relative;z-index:1;display:none;margin-left:10px;}
  .gdpTab{background-color:rgb(18,131,191)}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:268px;background:none;padding:0px;">
        <div class="buildcon" style="margin-top:0px;">
          <div class="buildleft" style="width:268px;min-height: 450px;border:none;background:none;" >
            <div class="buildleft-list" id="ul_org">
              <ul  style="margin-top:10px;">
                <li onclick="liClick('gdp')">地区生产总值</li>
                <li onclick="liClick('invest')">固定资产投资</li>
                <li onclick="liClick('gpbr')">公共财政预算收入</li>
                <li onclick="liClick('retail',1)">农民人均可支配收入</li>
                <li onclick="liClick('retail',2)">限额以上单位零售额</li>
                <li onclick="liClick('retail',3)">限额以上批发零售业销售额</li>
              </ul>
            </div>
			<!-- 居民人均收入,零售 -->
			<div style="display:none;" id="div_retail"> 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('retail')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_retail" onchange="yearChange(this,'retail')" style="padding:0px;border:1px #a5c7fe solid;"></select>
						<select id="month_retail" onchange="RETAILChange('retail')"  style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
				</div>
				<div id="retail_1" class="retail"><!-- retail -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="retail_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_retail" ></div>
						</div>
					</div>
					<div style="width:268px;height:30px;" >
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab123" id="tb_retail_1">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:40px;border-bottom:none;">排名</td>
								<td align="center" style="width:113px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:114px;border-bottom:none;">收入累计(万元)</td>
							</tr>
						</tbody>
						</table>
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab123" id="tb_retail_2">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:39px;border-bottom:none;">排名</td>
								<td align="center" style="width:113px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:114px;border-bottom:none;">收入累计(万元)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_retail" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_retail" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="retail_2" style="display:none;"  class="retail">
					<div id="retail_chart_2"  style="width:268px;height:450px;"></div>
				</div>
			</div>
			
			<!-- 公共财政预算收入 -->
			<div style="display:none;" id="div_gpbr"> 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('gpbr')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_gpbr" onchange="yearChange(this,'gpbr')" style="padding:0px;border:1px #a5c7fe solid;"></select>
						<select id="month_gpbr" onchange="GPBRChange('gpbr')"  style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
					<div class="buildleft-list gdp-list">
						<ul>
						<li onclick="gdpTab(this,1,'gpbr')" class="gdpTab">收入累计</li>
						<li onclick="gdpTab(this,2,'gpbr')">增幅</li>
					  </ul>
					</div>
				</div>
				<div id="gpbr_1" class="gpbr"><!-- gpbr -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="gpbr_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_gpbr"></div>
						</div>
					</div>
					<div style="width:268px;height:30px;" >
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:40px;border-bottom:none;">排名</td>
								<td align="center" style="width:113px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:114px;border-bottom:none;">收入累计(万元)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_gpbr" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_gpbr" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="gpbr_2" style="display:none;"  class="gpbr"><!-- 产值 -->
					<div id="gpbr_chart_2"  style="width:268px;height:450px;"></div>
				</div>
			</div>
          
			<!-- 地区生产总值 -->
			<div style="display:none;" id="div_gdp" > 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('gdp')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_gdp" onchange="yearChange(this,'gdp')" style="padding:0px;border:1px #a5c7fe solid;"></select>
						<select id="month_gdp" onchange="GDPChange('gdp')"  style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
					<div class="buildleft-list gdp-list">
						<ul>
						<li onclick="gdpTab(this,1,'gdp')" class="gdpTab">GDP</li>
						<li onclick="gdpTab(this,2,'gdp')">产值</li>
						<li onclick="gdpTab(this,3,'gdp')">增幅</li>
					  </ul>
					</div>
				</div>
				<div id="gdp_1" class="gdp"><!-- GDP -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="gdp_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_gdp"></div>
						</div>
					</div>
					<div style="width:268px;height:30px;">
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:40px;border-bottom:none;">排名</td>
								<td align="center" style="width:113px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:114px;border-bottom:none;">产值(万元)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_gdp" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_gdp" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="gdp_2" style="display:none;"  class="gdp"><!-- 产值 -->
					<div id="gdp_chart_2"  style="width:268px;height:450px;"></div>
				</div>
				<div id="gdp_3" style="display:none;"  class="gdp"><!-- 增幅 -->
					<div id="gdp_chart_3"  style="width:268px;height:450px;"></div>
				</div>
			</div>
			
			<!-- 固定资产投资 -->
			<div style="display:none;" id="div_invest"> 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('invest')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_invest" onchange="yearChange(this,'invest')" style="padding:0px;border:1px #a5c7fe solid;"></select>
						<select id="month_invest" onchange="GDPChange('invest')"  style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
					<div class="buildleft-list gdp-list">
						<ul>
						<li onclick="gdpTab(this,1,'invest')" class="gdpTab">投资额</li>
						<li onclick="gdpTab(this,2,'invest')">产业投资额</li>
						<li onclick="gdpTab(this,3,'invest')">涨幅</li>
					  </ul>
					</div>
				</div>
				<div id="invest_1" class="invest"><!-- GDP -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="invest_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_invest"></div>
						</div>
					</div>
					<div style="width:268px;height:30px;" >
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:40px;border-bottom:none;">排名</td>
								<td align="center" style="width:113px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:114px;border-bottom:none;">投资额(万元)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_invest" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_invest" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="invest_2" style="display:none;"  class="invest"><!-- 产值 -->
					<div id="invest_chart_2"  style="width:268px;height:450px;"></div>
				</div>
				<div id="invest_3" style="display:none;"  class="invest"><!-- 增幅 -->
					<div id="invest_chart_3"  style="width:268px;height:450px;"></div>
				</div>
			</div>
          
		  </div>
		
      </div><!--end .bpsmain-->
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
var parentMap,MAP,COLOR,SYMBOL,ESRI;
var gridLayer = null,yearObj={},textGraphic=[],areaGraphic=[];
	$(function(){
		$(".org_table").css({"height":(window.parent.document.documentElement.clientHeight -332)});
		$(".org_table").mCustomScrollbar({theme: "minimal-dark"});//return;
		parentMap = window.parent.$.fn.ffcsMap,COLOR = parentMap.getColor(),
		SYMBOL = parentMap.getSymbol(),MAP=parentMap.getMap(),ESRI = parentMap.getEsri();
		gridLayer = MAP.getLayer("gridLayer"),gridGraphics = gridLayer.graphics;
		
		parentMap.centerAt({
					x : parentMap.getCurrentMapCenterObj().centerPoint.x,          //中心点X坐标
					y : parentMap.getCurrentMapCenterObj().centerPoint.y,           //中心点y坐标
					wkid : window.parent.currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : 11
		    	});
		
		for(var i=0,l=gridGraphics.length;i<l;i++){//隐藏原有区域名称,新创建名称
			if(gridGraphics[i].attributes){
				areaGraphic.push(gridGraphics[i]);
			}else{
				textGraphic.push(gridGraphics[i]);
			}
		}
		var blackColor = COLOR.fromString("rgb(51,51,51)");
		for(var i=0,l=textGraphic.length;i<l;i++){
			gridLayer.remove(textGraphic[i]);
			textGraphic[i].symbol.setColor(blackColor);
			//textGraphic[i].setGeometry(textGraphic[i].geometry.offset(0,-0.01));
			gridLayer.add(textGraphic[i]);
		}
		 gridGraphics = gridLayer.graphics;
	})
	function backTextColor(){
		var redColor = COLOR.fromString("rgb(255,0,0)");
		for(var i=0,l=textGraphic.length;i<l;i++){//修改轮廓底色
			textGraphic[i].symbol.setColor(redColor);
			//textGraphic[i].setGeometry(textGraphic[i].geometry.offset(0,0.01));
		}
	}
	function back_(type){
		$('#div_'+type).hide();
		$('#ul_org').show();
		yearObj={};
		var redColor = COLOR.fromString("rgb(255,0,0)");
		for(var i=0,l=textGraphic.length;i<l;i++){//修改轮廓底色
			var d = gridGraphics[i].attributes.data;
			$(window.parent.document.getElementById("pie_"+d._oldData.infoOrgCode)).parent().remove();
			var lineC=[],areaC=[];
			for(var j=1;j<7;j+=2){
				lineC.push(parseInt("0x"+d.lineColor.slice(j,j+2)));        
				areaC.push(parseInt("0x"+d.areaColor.slice(j,j+2)));        
			}
			lineC = COLOR.fromString("rgba(" + lineC.join(",") + ",0.5)");
			areaC = COLOR.fromString("rgba(" + areaC.join(",") + ","+d.colorNum+")");
			gridGraphics[i].setSymbol(new SYMBOL.SimpleFillSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID,
						new SYMBOL.SimpleLineSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID, lineC, 2),areaC));
		}
		if(window.parent.currLayerEchartIndex){
			window.parent.layer.close(window.parent.currLayerEchartIndex);
		}
	}
	
	function gdpTab(o,n,cls){
		$("."+cls).hide();
		$("#"+cls+"_"+n).show();
		$(o).siblings().removeClass("gdpTab");
		$(o).addClass("gdpTab");
	}
	
	var typeNum ;
	function liClick(type,typenum){
		$("#ul_org").hide();
		$("#div_"+type).show();
		typeNum = typenum;
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findStatisticsDate.json?t='+Math.random(),
	        data:{table:type,type:typeNum},
			type: 'POST',
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	        },
	        success: function(data){ 
				var yearHtml="";
				if(data.list.length == 0){
					return ;
				}
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i];
					if(!yearObj[d.YEAR_]){
						yearObj[d.YEAR_] = [];
						yearHtml+="<option "+(i==0?"select=selected":"")+"  value='"+d.YEAR_+"'>"+d.YEAR_+"年</option>";
					}
					yearObj[d.YEAR_].push(d.MONTH_);
				}
					$("#year_"+type).html(yearHtml);
					yearChange({value:data.list[0].YEAR_},type);
	        }
	    });
	}
	function yearChange(o,type){
		var html="",year = o.value,month={1:'1月',2:'2月',3:'3月',4:'4月',5:'5月',6:'6月',7:'7月',8:'8月',9:'9月',10:'10月',11:'11月',12:'12月'};
		if(type=='gdp' || (typeNum && typeNum == 1)){
			month={3:'第一季度',6:'第二季度',9:'第三季度',12:'第四季度'};
		}
		for(var i=0,l=yearObj[year].length;i<l;i++){
			html+="<option "+(i==0?"select=selected":"")+" value='"+yearObj[year][i]+"'>"+month[yearObj[year][i]]+"</option>";
		}
		$("#month_"+type).html(html);
		switch(type){
			case 'gdp' : 
			case 'invest' : GDPChange(type);break;
			case 'gpbr' : GPBRChange(type);break;
			case 'retail' : RETAILChange(type);break;
		}
		
	}
	
	function RETAILChange(type){
		closeHide(type);
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPYearAndMonth.json?t='+Math.random(),
	        type: 'POST',
	        data: {table:type,type:typeNum, month :document.getElementById('month_'+type).value,year: document.getElementById('year_'+type).value},
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	            layer.close(index); 
	        },
	        success: function(data){ 
				var optionPie = {tooltip:{trigger:'item',formatter: "{b} <br/>{c} ({d}%)"  },
						legend:{show:false,data:[]},toolbox:{show:false},calculable:false,
						series:[{name:'',type:'pie', radius : '100%',
						itemStyle:{normal:{borderColor:'rgb(103,23,23)',label:{show:false,position:'inner',textStyle:{color:'#fff'}},labelLine:{show:false}}},data:[]
				}]};
				var gridColor={},sum=[], liHtml = "",maxValue = 0;		
				for(var i=0,l=data.list.length;i<l;i++){
					if(isNum(data.list[i].VAL_)>maxValue){
						maxValue = isNum(data.list[i].VAL_);
					}
				}
				maxValue+=1;
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],VAL_=isNum(d.VAL_);
					d['x']=d.X,d['y']=d.Y+0.0111,data.list[i].rank_ = VAL_;
					var per = Math.floor(VAL_/maxValue*100);
					if(per < 40){d['color']=  colorArr[3];}
					else if(per <70){d['color']=  colorArr[2];}
					else if(per <93){d['color']=  colorArr[1];}
					else{d['color']=  colorArr[0];}
					gridColor[d.INFO_ORG_CODE] = d.color;
					optionPie.legend.data.push("'"+d.REGION_NAME+"'");
					optionPie.series[0].data.push({value:VAL_, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
					
					//liHtml +="<tr class='header' style='width:100%;height:30px;'><td  align='center' width='134'>"+d.REGION_NAME+"</td><td align='center' width='134'>"+VAL_+"</td></tr>";
					sum.push(d);
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$(".c-tab123").hide();
				$("#tb_retail_"+(typeNum==1?1:2)).show();
				$("#tb_"+type).html(getRank(data.list));
				window.parent.renderJJStatistics('STATISTICS_LAYER',sum,type,typeNum);
				setORG(gridColor);
	        	layer.close(index);
			}
	    });
	}
	
	function GPBRChange(type){
		closeHide(type);
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPYearAndMonth.json?t='+Math.random(),
	        type: 'POST',
	        data: {table:type, month :document.getElementById('month_'+type).value,year: document.getElementById('year_'+type).value},
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	            layer.close(index); 
	        },
	        success: function(data){ 
				var optionPie = {tooltip:{trigger:'item',formatter: "{b} <br/>{c} ({d}%)"  },
						legend:{show:false,data:[]},toolbox:{show:false},calculable:false,
						series:[{name:'',type:'pie', radius : '100%',
						itemStyle:{normal:{borderColor:'rgb(103,23,23)',label:{show:false,position:'inner',textStyle:{color:'#fff'}},labelLine:{show:false}}},data:[]
				}]};
				var optionBarzf ={  tooltip:{ trigger: 'axis'},  legend:{data:['涨幅'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],yAxis:[{type:'category',data:[],axisLabel:{textStyle:{color:'#fff'}} }],
				series:[{name:'涨幅',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideRight'}}},data:[]}]};
				
				var gridColor={},sum=[], liHtml = "",maxValue = 0;		
				for(var i=0,l=data.list.length;i<l;i++){
					if(isNum(data.list[i].GPBR_SUM)>maxValue){
						maxValue = isNum(data.list[i].GPBR_SUM);
					}
				}
				maxValue+=1;
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],GPBR_SUM=isNum(d.GPBR_SUM);
					d['x']=d.X,d['y']=d.Y+0.0111,data.list[i].rank_ = GPBR_SUM;
					var per = Math.floor(GPBR_SUM/maxValue*100);
					if(per < 40){d['color']=  colorArr[3];}
					else if(per <70){d['color']=  colorArr[2];}
					else if(per <93){d['color']=  colorArr[1];}
					else{d['color']=  colorArr[0];}
					gridColor[d.INFO_ORG_CODE] = d.color;
					optionPie.legend.data.push("'"+d.REGION_NAME+"'");
					optionPie.series[0].data.push({value:GPBR_SUM, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
					
					optionBarzf.yAxis[0].data.push(d.REGION_NAME);
					optionBarzf.series[0].data.push(isNum(d.GPBR_INC));
					
					//liHtml +="<tr class='header' style='width:100%;height:30px;'><td  align='center' width='134'>"+d.REGION_NAME+"</td><td align='center' width='134'>"+GPBR_SUM+"</td></tr>";
					sum.push(d);
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				
				var myChart2 = echarts.init(document.getElementById(type+'_chart_2'));
				myChart2.setOption(optionBarzf);
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$("#tb_"+type).html(getRank(data.list));
				window.parent.renderJJStatistics('STATISTICS_LAYER',sum,type);
				setORG(gridColor);
	        	layer.close(index);
			}
	    });
	}
	var colorArr = ['rgba(243,60,60,1)','rgba(243,120,100,0.94)', 'rgba(243,150,100,0.91)','rgba(243,190,100,0.57)'];
	function GDPChange(type){
		closeHide(type);
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPYearAndMonth.json?t='+Math.random(),
	        type: 'POST',
	        data: {table:type, month :document.getElementById('month_'+type).value,year: document.getElementById('year_'+type).value},
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	            layer.close(index); 
	        },
	        success: function(data){ 
				var optionPie = {tooltip:{trigger:'item',formatter: "{b} <br/>{c} ({d}%)"  },
						legend:{show:false,data:[]},toolbox:{show:false},calculable:false,
						series:[{name:'',type:'pie', radius : '100%',
						itemStyle:{normal:{borderColor:'rgb(103,23,23)',label:{show:false,position:'inner',textStyle:{color:'#fff'}},labelLine:{show:false}}},data:[]
				}]};
				var optionBarzf ={  tooltip:{ trigger: 'axis'},  legend:{data:['第一产业', '第二产业','第三产业'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],yAxis:[{type:'category',data:[],axisLabel:{textStyle:{color:'#fff'}} }],
				series:[{name:'第一产业',type:'bar',/*barWidth:25,*/stack:'总量',itemStyle:{normal:{label:{show:false,position:'insideRight'}}},data:[]},
					{name:'第二产业',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:false,position:'insideRight'}}},data:[]},
						{name:'第三产业',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:false,position:'insideRight'}}},data:[]}]};
		
				var optionBar ={  tooltip:{ trigger: 'axis'},  legend:{data:['第一产业', '第二产业','第三产业'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:55},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{rotate:30,textStyle:{color:'#fff'}}}],yAxis:[{type:'category',data:[],axisLabel:{textStyle:{color:'#fff'}} }],
				series:[{name:'第一产业',type:'bar',stack:'总量',data:[]},
					{name:'第二产业',type:'bar',stack:'总量',data:[]},
						{name:'第三产业',type:'bar',stack:'总量',data:[]}]};
		
				 
				var gridColor={},sum=[], maxValue = 0;
				var rankData = [];
				for(var i=0,l=data.list.length;i<l;i++){
					if(isNum(data.list[i].GDP_SUM)>maxValue){
						maxValue = isNum(data.list[i].GDP_SUM);
					}
				}
				maxValue+=1;
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],GDP_SUM=isNum(d.GDP_SUM);
					d['x']=d.X,d['y']=d.Y+0.0111,data.list[i].rank_ = GDP_SUM;
					var per = Math.floor(GDP_SUM/maxValue*100);
					if(per < 40){d['color']=  colorArr[3];}
					else if(per <70){d['color']=  colorArr[2];}
					else if(per <93){d['color']=  colorArr[1];}
					else{d['color']=  colorArr[0];}
					gridColor[d.INFO_ORG_CODE] = d.color;
					optionPie.legend.data.push("'"+d.REGION_NAME+"'");
					optionPie.series[0].data.push({value:GDP_SUM, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
					
					optionBar.yAxis[0].data.push(d.REGION_NAME);
					optionBar.series[0].data.push(isNum(d.ONE_SUM));
					optionBar.series[1].data.push(isNum(d.TWO_SUM));
					optionBar.series[2].data.push(isNum(d.THREE_SUM));
					
					optionBarzf.yAxis[0].data.push(d.REGION_NAME);
					optionBarzf.series[0].data.push(isNum(d.ONE_INC));
					optionBarzf.series[1].data.push(isNum(d.TWO_INC));
					optionBarzf.series[2].data.push(isNum(d.THREE_INC));
					
					
					sum.push(d);
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				
				var myChart2 = echarts.init(document.getElementById(type+'_chart_2'));
				myChart2.setOption(optionBar);
				
				var myChart3 = echarts.init(document.getElementById(type+'_chart_3'));
				myChart3.setOption(optionBarzf);
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$("#tb_"+type).html(getRank(data.list));
				//layer.close(index); return;
				window.parent.renderJJStatistics('STATISTICS_LAYER',sum,type);
				setORG(gridColor);
	        	layer.close(index); 
	        }
	    });
		
	}
	function getRank(list,valCol){
		var l=list.length,liHtml='',temp ;
		for(var i=0;i<list.length;i++){
			for(var j=i+1;j<l;j++){
				if(list[i].rank_<list[j].rank_){
					temp = list[i];
					list[i] = list[j];
					list[j] = temp;
				}
			}
		}
		for(var i=0;i<list.length;i++){
			liHtml +="<tr class='header' style='width:100%;height:30px;'><td style='text-align:center;width:40px;border:1px solid #6d8ca4;'>"+(i+1)+"</td><td style='text-align:center;width:114px;border:1px solid #6d8ca4;'>"+list[i].REGION_NAME+"</td><td style='text-align:center;width:114px;border:1px solid #6d8ca4;'>"+list[i].rank_+"</td></tr>";
		}
		return liHtml;
	}
	function closeHide(type){
		if(window.parent.currLayerEchartIndex){
			window.parent.layer.close(window.parent.currLayerEchartIndex);
		}
		$("#circle_"+type).hide();
		index = layer.load(0, {time: 5000});
	}
	function setORG(gridColor){
		var polygons={};
		
		var fffColor = COLOR.fromString("#fff");
		for(var i=0,l=textGraphic.length;i<l;i++){//修改轮廓底色
			var orgcode = gridGraphics[i].attributes.data._oldData.infoOrgCode;
			if(gridColor[orgcode]){
				var orgColor = COLOR.fromString(gridColor[orgcode]);
				gridGraphics[i].setSymbol(new SYMBOL.SimpleFillSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID,
					new SYMBOL.SimpleLineSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID, fffColor, 2),orgColor));
				gridGraphics[i].symbol.setColor(orgColor);
			}
		}
		setTimeout(function(){
			for(var i=0,l=textGraphic.length;i<l;i++){//修改轮廓底色
				gridLayer.remove(textGraphic[i]);//先删后加 解决区域名称 被轮廓覆盖
				gridLayer.add(textGraphic[i]);
			}
		},500);
	}
	
	function isNum(n){return n?n:0;}
</script>
</html>
