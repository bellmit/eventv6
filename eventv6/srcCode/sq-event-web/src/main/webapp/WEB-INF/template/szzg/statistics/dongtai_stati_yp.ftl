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
          <div class="buildleft" style="width:268px;min-height: 500px;border:none;background:none;" >
            <div class="buildleft-list" id="ul_org">
              <ul  style="margin-top:10px;">
                <li onclick="liClick('invest')">固定资产投资</li><!--zg_ecno_invest-->
                <li onclick="liClick('retail',4)">一般公共预算总收入</li>
                <li onclick="liClick('retail',5)">地方一般公共预算收入</li>
                <li onclick="liClick('retail',6)">社会消费品零售总额</li>
                <li onclick="liClick1('ldmj')">林地面积</li><!--zg_general_idx-->
              </ul>
            </div>
			<!-- 林地面积 -->
			<div style="display:none;" id="div_ldmj"> 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('ldmj')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_ldmj" onchange="yearChange(this,'ldmj')" style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
					<div class="buildleft-list gdp-list">
						<ul>
						<li onclick="gdpTab(this,1,'ldmj')" class="gdpTab">森林覆盖率</li>
						<li onclick="gdpTab(this,2,'ldmj')">林地面积</li>
						<li onclick="gdpTab(this,3,'ldmj')">森林蓄积</li>
					  </ul>
					</div>
				</div>
				<div id="ldmj_1" class="ldmj"><!-- GDP -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="ldmj_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_ldmj"></div>
						</div>
					</div>
					<div style="width:268px;height:30px;" >
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						<tbody>
							<tr class="header" style="height:30px">
								<td align="center" style="width:133px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:134px;border-bottom:none;">森林覆盖率(%)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_ldmj" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_ldmj" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="ldmj_2" style="display:none;"  class="ldmj"><!-- 林地面积 -->
					<div id="ldmj_chart_2"  style="width:268px;height:450px;"></div>
				</div>
				<div id="ldmj_3" style="display:none;"  class="ldmj"><!-- 森林蓄积 -->
					<div id="ldmj_chart_3"  style="width:268px;height:450px;"></div>
				</div>
			</div>
          
			<!-- 一般公共预算总收入,地方一般公共预算收入,社会消费品零售总额 -->
			<div style="display:none;" id="div_retail"> 
				<div style="width:247px;height:45px;">
					<div style="float:left;width:60px;">
						<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="back_('retail')" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
					</div>
					<div>
						<select id="year_retail" onchange="yearChange(this,'retail')" style="padding:0px;border:1px #a5c7fe solid;"></select>
						<select id="month_retail" onchange="RETAILChange('retail')"  style="padding:0px;border:1px #a5c7fe solid;"></select>
					</div>
					<div class="buildleft-list gdp-list">
						<ul>
						<li id="lj_retail" onclick="gdpTab(this,1,'retail')" class="gdpTab">累计</li>
						<li onclick="gdpTab(this,2,'retail')">增长</li>
					  </ul>
					</div>
				</div>
				<div id="retail_1" class="retail"><!-- gpbr -->
					<div style="position:relative;margin:10px 10px 10px 30px;">
						<div id="retail_chart_1"  style="width:200px;height:180px;position:relative;z-index:100;float:left;"></div>
						<div style="width:178px;height:178px;">
							<div class="circle" id="circle_retail"></div>
						</div>
					</div>
					<div style="width:268px;height:30px;" >
						<table  cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						<tbody>
							<tr class="header" style="height:30px;background:rgba(0,170,255,0.4);">
								<td align="center" style="width:133px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:134px;border-bottom:none;">累计(万元)</td>
							</tr>
						</tbody>
						</table>
					</div>
					<div id="li_retail" style="width:268px;height:240px;overflow-y:auto;" class="org_table">
						<table id="tb_retail" cellspacing="0" cellpadding="0" width="100%" class="c-tab1">
						</table>
					</div>
				</div>
				<div id="retail_2" style="display:none;"  class="retail"><!-- 产值 -->
					<div id="retail_chart_2"  style="width:268px;height:450px;"></div>
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
						<!--<li onclick="gdpTab(this,2,'invest')">产业投资额</li> -->
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
							<tr class="header" style="height:30px">
								<td align="center" style="width:133px;border-bottom:none;">区域名称</td>
								<td align="center" style="width:134px;border-bottom:none;">投资额(万元)</td>
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
				<div id="invest_3" style="display:none;"  class="invest"><!-- 增长 -->
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
	function liClick1(type){
		$("#ul_org").hide();
		$("#div_"+type).show();
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGeneralDate.json?t='+Math.random(),
	        data:{type:'ldmj'},type: 'POST', dataType:"json",
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
				}
					$("#year_"+type).html(yearHtml);
					yearChange({value:data.list[0].YEAR_},type);
	        }
	    });
	}
	function liClick(type,typenum){
		$("#ul_org").hide();
		$("#div_"+type).show();
		typeNum = typenum;
		if(type == 'retail'){$("#lj_retail").click();}
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
		if(yearObj[year].length>0){
			for(var i=0,l=yearObj[year].length;i<l;i++){
				html+="<option "+(i==0?"select=selected":"")+" value='"+yearObj[year][i]+"'>"+month[yearObj[year][i]]+"</option>";
			}
			$("#month_"+type).html(html);
		}
		switch(type){
			case 'invest' : INVESTChange(type);break;
			case 'gpbr' : GPBRChange(type);break;
			case 'retail' : RETAILChange(type);break;
			case 'ldmj' : LDMJChange(type);break;
		}
		
	}
	//林地面积
	function LDMJChange(type){
		closeHide(type);
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGeneralDate.json?t='+Math.random(),
	        type: 'POST',
	        data: {type:"'slfgl','slmj','ldmj'",year: document.getElementById('year_'+type).value},
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	            layer.close(index); 
	        },
	        success: function(data){ 
				var optionPie = {tooltip:{trigger:'item',formatter: "{b} <br/>{c}%"  },
						legend:{show:false,data:[]},toolbox:{show:false},calculable:false,
						series:[{name:'',type:'pie', radius : '100%',
						itemStyle:{normal:{borderColor:'rgb(103,23,23)',label:{show:false,position:'inner',textStyle:{color:'#fff'}},labelLine:{show:false}}},data:[]
				}]};
				var optionBarld ={  tooltip:{ trigger: 'axis'},  legend:{data:['林地面积'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],
				  yAxis:[{type:'category',data:[],axisLabel:{rotate:30,textStyle:{color:'#fff',fontSize:11}} }],
				series:[{name:'林地面积',type:'bar',/*barWidth:25,*/stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideLeft'}}},data:[]}]};
		
				var optionsl ={  tooltip:{ trigger: 'axis'},  legend:{data:['森林蓄积'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],
				  yAxis:[{type:'category',data:[],axisLabel:{rotate:30,textStyle:{color:'#fff',fontSize:11}} }],
				series:[{name:'森林蓄积',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideLeft'}}},data:[]}]};
		
				 
				var gridColor={},sum=[], liHtml = "";
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],val=isNum(d.IDX_VAL);
					d['x']=d.X,d['y']=d.Y+0.01;
					switch(d.IDX_PARAM){
						case 'ldmj'://林地面积
						optionBarld.yAxis[0].data.push(d.REGION_NAME);
						optionBarld.series[0].data.push(val);
						;break;
						case 'slmj'://森林蓄积
						optionsl.yAxis[0].data.push(d.REGION_NAME);
						optionsl.series[0].data.push(val);
						;break;
						case 'slfgl'://森林覆盖率
						d['color']= 'rgba(0,255,0,'+(val/100)+')';
						gridColor[d.INFO_ORG_CODE] = d.color;
						optionPie.legend.data.push(d.REGION_NAME);
						optionPie.series[0].data.push({value:val, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
						liHtml +="<tr class='header' style='width:100%;height:30px;'><td  align='center' width='134'>"+d.REGION_NAME+"</td><td align='center' width='134'>"+val+"%</td></tr>";
						sum.push(d);
						;break;
					}					
					
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				
				var myChart2 = echarts.init(document.getElementById(type+'_chart_2'));
				myChart2.setOption(optionBarld);
				
				var myChart3 = echarts.init(document.getElementById(type+'_chart_3'));
				myChart3.setOption(optionsl);
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$("#tb_"+type).html(liHtml);
				//layer.close(index); return;
				window.parent.renderJJStatistics('STATISTICS_LAYER',sum,type);
				setORG(gridColor);
	        	layer.close(index); 
	        }
	    });
		
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
				var optionBarzf ={  tooltip:{ trigger: 'axis'},  legend:{data:['增长'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],yAxis:[{type:'category',data:[],axisLabel:{rotate:30,textStyle:{color:'#fff'}} }],
				series:[{name:'增长',type:'bar',/*barWidth:25,*/stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideRight'}}},data:[]}]};
				
				var gridColor={},sum=[], liHtml = "",maxValue = 0;		
				for(var i=0,l=data.list.length;i<l;i++){
					if(isNum(data.list[i].VAL_)>maxValue){
						maxValue = isNum(data.list[i].VAL_);
					}
				}
				maxValue+=1;
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],VAL_=isNum(d.VAL_);
					d['x']=d.X-0.01,d['y']=d.Y+0.01;
					var per = Math.floor(VAL_/maxValue*100);
					if(per < 40){d['color']=  colorArr[3];}
					else if(per <70){d['color']=  colorArr[2];}
					else if(per <93){d['color']=  colorArr[1];}
					else{d['color']=  colorArr[0];}
					gridColor[d.INFO_ORG_CODE] = d.color;
					optionPie.legend.data.push("'"+d.REGION_NAME+"'");
					optionPie.series[0].data.push({value:VAL_, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
					
					optionBarzf.yAxis[0].data.push(d.REGION_NAME);
					optionBarzf.series[0].data.push(isNum(d.VAL_INC));
					
					liHtml +="<tr class='header' style='width:100%;height:30px;'><td  align='center' width='134'>"+d.REGION_NAME+"</td><td align='center' width='134'>"+VAL_+"</td></tr>";
					sum.push(d);
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				var myChart1 = echarts.init(document.getElementById(type+'_chart_2'));
				myChart1.setOption(optionBarzf);
				
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$("#tb_"+type).html(liHtml);
				window.parent.renderJJStatistics_YP('STATISTICS_LAYER',sum,type,typeNum);
				setORG(gridColor);
	        	layer.close(index);
			}
	    });
	}
	
	var colorArr = ['rgba(243,60,60,1)','rgba(243,120,100,0.94)', 'rgba(243,150,100,0.91)','rgba(243,190,100,0.57)'];
	
	//固定资产投资
	function INVESTChange(type){
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
				var optionBarzf ={  tooltip:{ trigger: 'axis'},  legend:{data:['增长'],textStyle:{color:'#fff'}},
							grid:{x:55,y:30,x2:10,y2:25},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'value',splitLine:{show:false},axisLabel:{textStyle:{color:'#fff'}}}],yAxis:[{type:'category',data:[],axisLabel:{rotate:30,textStyle:{color:'#fff'}} }],
				series:[{name:'增长',type:'bar',/*barWidth:25,*/stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideRight'}}},data:[]}]};
							 
				var gridColor={},sum=[], liHtml = "",maxValue = 0;
				for(var i=0,l=data.list.length;i<l;i++){
					if(isNum(data.list[i].GDP_SUM)>maxValue){
						maxValue = isNum(data.list[i].GDP_SUM);
					}
				}
				maxValue+=1;
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i],GDP_SUM=isNum(d.GDP_SUM);
					d['x']=d.X,d['y']=d.Y+0.01;
					var per = Math.floor(GDP_SUM/maxValue*100);
					if(per < 40){d['color']=  colorArr[3];}
					else if(per <70){d['color']=  colorArr[2];}
					else if(per <93){d['color']=  colorArr[1];}
					else{d['color']=  colorArr[0];}
					gridColor[d.INFO_ORG_CODE] = d.color;
					optionPie.legend.data.push("'"+d.REGION_NAME+"'");
					optionPie.series[0].data.push({value:GDP_SUM, name:d.REGION_NAME,itemStyle:{normal:{color:d.color}}});
					
					optionBarzf.yAxis[0].data.push(d.REGION_NAME);
					optionBarzf.series[0].data.push(isNum(d.GDP_INC));
					
					liHtml +="<tr class='header' style='width:100%;height:30px;'><td  align='center' width='134'>"+d.REGION_NAME+"</td><td align='center' width='134'>"+GDP_SUM+"</td></tr>";
					sum.push(d);
				}
				var myChart = echarts.init(document.getElementById(type+'_chart_1'));
				myChart.setOption(optionPie);
				
				var myChart3 = echarts.init(document.getElementById(type+'_chart_3'));
				myChart3.setOption(optionBarzf);
				setTimeout(function(){$("#circle_"+type).show();},1000);
				$("#tb_"+type).html(liHtml);
				window.parent.renderJJStatistics_YP('STATISTICS_LAYER',sum,type);
				setORG(gridColor);
	        	layer.close(index); 
	        }
	    });
		
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
