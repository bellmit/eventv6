<!DOCTYPE html>
<html>
<head>
	<title>水电煤</title>
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
	.aqi-chart1 {
	    float: left;
	    font-size: 12px;
	    line-height: 1.6;
	}
	.aqi-chart-nr {
	    width: 225px;
	    height: 104px;
	    padding: 5px 10px 0 10px;
	}
	.aqi-list {
	    border-collapse: collapse;
	    font-size: 12px;
	}
	.aqi-list tr th {
	 border: 1px solid #8694a9;
     padding: 3px;
     text-align: center;
	}
	.aqi-list tr td {
	    border: 1px solid #8694a9;
	    padding: 1px 3px;
	}
	 h5 {
	    font-weight: normal;
	    font-size: 14px;
	    padding: 0px 0 5px 0;
	}
	.fisright-chart2 {
	    margin-left: 0px;
	    border: 1px solid #6d8ca4;
	    margin-top: 10px;
	}
	.bombcon table{ border-collapse:collapse;}
    .bombcon table td{ border:1px solid #6d8ca4; padding:5px;}
    .buildcon{ margin-top:10px;}
	.buildleft{
	     margin-left:10px;
		 margin-top:10px; 
		 float:left; border:1px solid #81a1b9; 
		 width:188px;
	 	 min-height:440px;
	 }
	 
	 .buildright{
		 float:left;margin-left:10px;
		 height: 440px;width: 545px;
	 }
	 
	 
	.buildleft-tit{border-bottom:1px solid #81a1b9; height:37px; line-height:37px;}
	.buildleft-tit ul li{ padding:12px 0;width:49.5%;border-right:1px solid #81a1b9;}
	.buildleft-tit ul li.noline{ border:none;}
	.buildleft-list ul li{ 
	    float:none; text-align:left;padding: 8px 10px;
	    border: 1px solid #32d6c5;
	    border-image: -webkit-linear-gradient(45deg, #04c1cd, #715de7 50%, #d701ff) 10 10;
	    border-image: -moz-linear-gradient(45deg, #04c1cd, #715de7 50%, #d701ff) 10 10;
	    border-image: linear-gradient(45deg, #04c1cd, #715de7 50%, #d701ff) 10 10;
	    margin:7px 10px;
	    min-width: 140px;
    }
    .buildleft-tit li{
		float: left;
	    text-align: center;
	    line-height: 1;
	    width: 32.629% !important;
	}
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
	.buildleft-list ul li.buildleft-list-dq, .buildleft-list ul li:hover {
   		 background: rgba(7,141,210,0.85);
	}
	
	</style>
</head>
<body>

	   <div class="buildleft">
          	 <div class="buildleft-tit">
              <ul>
              	<li id="1" class="<#if type=='1'>build-dq<#else>noline</#if>" >水</li>
                <li id="2" class="<#if type=='2'>build-dq<#else>noline</#if>" >电</li>
                <li id="3" class="<#if type=='3'>build-dq<#else>noline</#if>" >煤</li>
              </ul>
              <div class="clearfloat"></div>
            </div>
            <div class="clearfloat"></div>
          	 <div class="bpscon-box-tit">
          	  <div style="margin-left:10px;margin-top:10px;margin-bottom:10px">日期</div> 
          	  <div style="margin-left:10px">
                  </select>
                  <input type="text" class="inp1 Wdate timeClass" id="syear"name="syear"  value="2017"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy',ychanged:rkYearChange})" >
                  
          	  </div>
                
                </div>
              <div class="buildleft-list">
              <div style="margin-left:10px;margin-top:10px;margin-bottom:10px">区域</div>
              <ul id="content-d" style="overflow-x:hidden;overflow-y:auto;height:260px;">
               <#list orgEntityInfos as obj >
               		<li id="${obj.orgCode}" <#if obj.orgCode==orgCode>build-dq<#else></#if> class="buildleft-list-dq"><i><img src="${rc.getContextPath()}/images/np_city_icon15.png" width="13" height="17" alt=""/></i>${obj.orgName}</li>
               </#list>
              </ul>
            </div>
          </div>
	   </div>	
	   
	   <div  class="buildright fisright-chart2">
	     	<div id="sdm-ggfw-max" style="width: 550px; height: 200px;">
	        </div> 
	        <div class="bombcon" style="overflow-x:hidden;overflow-y:auto;height:235px;">   
                <table  style="float:left;margin-left:10px;" width="47%" border="0" cellspacing="0" cellpadding="0">
                 	<tr class="coltit" >
                      <td align="center" style="width:40px">月份</td> 
                      <td align="center">使用量（<span id="unit1"></span>）</td>
                      <td align="center">同比（%）</td> 
                      <td align="center">环比（%）</td>
                    </tr>
                  <tbody id="data1"></tbody>
                </table>
                <table  style="float:left;margin-left:10px" width="47%" border="0" cellspacing="0" cellpadding="0">
                	<tr class="coltit" >
                      <td align="center" style="width:40px">月份</td> 
                      <td align="center">使用量（<span id="unit2"></span>）</td>
                      <td align="center">同比（%）</td> 
                      <td align="center">环比（%）</td>
                    </tr>
                  <tbody id="data2"></tbody>
                </table>
           </div>
	   
	   
	   </div>
	<script type="text/javascript">   
	var syear=${curYear};
	var type=${type};
	var orgCode=${orgCode};
		$(function(){
			//改变滚动条样式
			$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	    });  
		
	   	$(function(){
			$("#syear").val(${curYear});//默认当前年
			orgCode=$('.buildleft-list').find('li:first').attr("id");
			$(".buildleft-list li")[0].click();
	    });
	    
	    //选择年份事件
	     function rkYearChange(o,type){
	        if(type == undefined){
	            type = type;
	        }
	        syear=o.cal.date.y;
	        showEcharsReport(); 
	    }
		//选择类型事件
		$(".buildleft-tit li").click(function(){
		    type=$(this).attr('id');
			for (var i=1;i<4;i++)
			{
				$("#"+i).attr( "class","noline" );
			}
			$("#"+type).attr( "class","build-dq" );
			showEcharsReport();
		});
		
		$(".buildleft-list li").click(function(){
		    orgCode=$(this).attr('id');
			$(".buildleft-list li").removeAttr("class")
			$("#"+orgCode).attr( "class","buildleft-list-dq" );
			showEcharsReport();
		});
		
		
		function showEcharsReport(){
			//var url = '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/report.jhtml?orgCode='+orgCode+'&syear='+syear+'&type='+type+'&selType=sdm-2'; 
			//$("#contentFrame",document).attr("src", url)
			showTableData();
		} 
		function showTableData(){
			if(type=='2'){
				$("#unit1").html("度");
				$("#unit2").html("度");
			}else{
				$("#unit1").html("吨");
				$("#unit2").html("吨");
			}
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/jsonData.json?syear='+syear+"&type="+type+"&orgCode="+orgCode,
				dataType: 'json',
				success: function(data) {
				var data1='';
				var data2='';
				for (var i=0;i<data.curList.length;i++)
				{
				 	 var str='';
					  str+='<tr>';
	                  str+='<td align="center">'+(i+1)+'月</td>';
	                  str+='<td align="center">'+data.curList[i].usage+'</td>';
	                  str+='<td align="center">'+data.curList[i].yearBasis+'</td>';
	                  str+='<td align="center">'+data.curList[i].linkRelRatio+'</td>';
	                  str+='</tr>';
	                  if(i<6){
	                 	 data1+=str;
	                  }else{
	                  	data2+=str;
	                  }
				}
				$("#data1").html(data1);
				$("#data2").html(data2);
				showEcharsData(data);   
					//alert(data.curList);
	                // modleclose();
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					//modleclose(); //关闭遮罩层
				}
			});
		}
	  
		function showEcharsData(data){
		  var myChart1 = echarts.init(document.getElementById("sdm-ggfw-max"));
			 var typeName;
			 if(type=='1'){
			 	typeName="水";
			 }else if(type=='2'){
			 	typeName="电";
			 }else{
			 	typeName="煤";
			 }
			 var lastUsages=new Array();
			 var curUsages=new Array();
			 var yearBasisIncs=new Array();
			 var linkRelRatioIncs=new Array();
			 data.lastList.forEach(function(val,index,arr){
				  lastUsages[index]=data.lastList[index].usage;           
			 });
			 data.curList.forEach(function(val,index,arr){
				  curUsages[index]=data.curList[index].usage;  
				  yearBasisIncs[index]=data.curList[index].yearBasisInc;  
				  linkRelRatioIncs[index]=data.curList[index].linkRelRatioInc;            
			 });
		
	  		myChart1.setOption( {
					tooltip : {
			        	trigger: 'axis'
			    	},
			    	legend: {
		    			x : 'center',
			    		y : 'bottom' ,
		        		data:[data.lastYear+'年用'+typeName+'量',''+data.curYear+'年用'+typeName+'量','同比增长','环比增长'],
		       		 	textStyle: {      
		            	color: '#fff'
		       		 	}
		    		},
		    		calculable : true,
				    xAxis : [
				        {
				            type : 'category',
				            data : ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
				            axisLabel: {
					            show: true,
					            interval:0,
					            rotate: 0,
					            textStyle: {       
				                    color: '#fff'
				                }
					    	}
				        }
				    ],
				    yAxis : [
			        {
			            type : 'value',
			            name : typeName+'量',
			            axisLabel: {
				            show: true,
				             rotate: 50,
				             textStyle: {      
			                    color: '#fff'
			                }
				    	}
			        },
			        {
			            type : 'value',
			            name : '增长率',
			            axisLabel : {
			                formatter: '{value}%',
			                textStyle: {       
			                    color: '#fff'
			                }
			            }
			        }
			    ],
			    grid:{
		    	x:60, 
		    	y:20
			    },
			    series : [
			        {
			            name:data.lastYear+'年用'+typeName+'量',
			            type:'bar',
			            data:lastUsages
			            
			        },
			        {
			            name:data.curYear+'年用'+typeName+'量',
			            type:'bar',
			            data:curUsages
			        },
			        {
			            name:'同比增长',
			            type:'line',
			            yAxisIndex: 1,
			            data:yearBasisIncs
			        },
			        {
			            name:'环比增长',
			            type:'line',
			            yAxisIndex: 1,
			            data:linkRelRatioIncs
			        }
			    ]
		     });
	  }
	</script>

</body>

</html>
