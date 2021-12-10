<!DOCTYPE html>
<html>
<head>
	<title>户籍人口</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/echarts/echarts-all.js"></script>
	<style>
	body{
		color:#fff;
	}
	.fisright-chart1 {
		width:515px;
		min-height: 381px;
		background:none;
	}
	.fisright-chart2{
		margin-left:545px;
		min-height: 381px;
		background:none;
	}
	.npAlarminfo{
		background:none;
	}
	#combobox_INPUTgridName_div_0{
		background-color:rgba(30,64,89,0.75) !important;
		//color:#fff !important;
	}
	input{
		background-color:transparent !important;
		color:#fff !important;
	}
	span{
		color:#fff !important;
	}
	#combobox_INPUTgridName_ul_0{
		height:300px !important;
		max-height:300px !important;
	}
	h5 {
   	 	font-weight: normal;
    	font-size: 14px;
    	padding: 0px 0 5px 0;
	}	
	</style>
</head>
<body>
 <div class="npAlarminfo citybgbox bpsmain" style="width:960px;">
        <div>
            <div class="fisright-chart1">
            	
            	<div style="display:inline;">
            	<h5>按地区分布 <span  id="totalNum" style="color:#20c0d6 !important"> </span></h5>	
            		<input type="hidden" id="gridId" name="gridId" value=">
	        		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${KEY_DEFAULT_INFO_ORG_CODE}">
	        		<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="${KEY_DEFAULT_INFO_ORG_NAME}" 

style="width:175px;"/>
				</div>
				<div id="chartRegionDiv" class="chartlist" style="position:abolute;"></div>
               		
                </div>
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
            	<h5>按年龄段分布</h5>
              <div id="chartAgeDiv" class="chartlist" style="position:abolute; " ></div>
            </div><!--end .fisright-chart2-->
        </div><!--end .buildright-->
 </div>
<script type="text/javascript"> 
$(function(){
AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				showRegionEchart(grid.orgCode);
				showAgeEchart(grid.orgCode)
				//$("#infoOrgCode").val(grid.orgCode);
			}
		});

	showRegionEchart("${KEY_DEFAULT_INFO_ORG_CODE}");
	showAgeEchart("${KEY_DEFAULT_INFO_ORG_CODE}")
		//改变滚动条样式
		$("#combobox_INPUTgridName_div_0").mCustomScrollbar({theme:"minimal-dark"});
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
		
		
});
function showRegionEchart(orgCode){
		 $.ajax( {
    		url:"${rc.getContextPath()}/zhsq/szzg/classStat/regionEchart.jhtml?orgCode="+orgCode,  
    		type : 'POST',
    		dataType : "json",
    		error : function(data) {
    		},
    		success : function(retXml) {
    		 	var windowsHeight = $(window).height(); 
			 	$('#chartRegionDiv').height(windowsHeight-130);	
    		    if(retXml.regionList.length>0){
	    			showRegion(retXml);
	    			var totalDataList = retXml.regionList;
	    			var totalNum = 0;
	    			for(var i=0;i<totalDataList.length;i++){
	    				totalNum=Number(totalNum)+Number(totalDataList[i].value);
	    			}
	    			//$("#totalNum").html("按地区分布"+"  "+"户籍人口总数："+totalNum+"人") ;
	    			$("#totalNum").text(" 总人口："+totalNum+"人") ;
    			}else{
    				noData("chartRegionDiv");
    			}
    		}
    	});  
}
function showRegion(data){
		 //$('#chartRegionDiv').height(430);	
         var myChart = echarts.init(document.getElementById('chartRegionDiv')); 
         var option = {
			    tooltip : {
			        trigger: 'axis'
			    },
				color:['#87cefa','#ff7f50'],
			   // calculable : false,
				grid:{
					x:0,
					y:5,x2:55
				},
			    xAxis : [
			        {
			            type : 'category',
			            splitLine : {show : false},
			            axisLabel:{ 
			             interval:0,
                         rotate:-50,
                         margin:2,
							textStyle:{
                               color:'#FFFFFF'
                        	}
                    	},
			            data:data.orgNameList
			        }
			    ],
			    yAxis : [
			        {	
			       
			           max:data.max*2,
			            //max:data.max,
			            type : 'value',
			            position: 'right',
			            axisLabel:{ 
							textStyle:{
                               color:'#FFFFFF'
                        	}
                        }
			        }
			    ],
			    series : [

			        {
			        	name:'人数',
			            type:'bar',
			            itemStyle:{
		                normal:{
		               	 label:{show:false},
		               	 color:'#ffbc48'
		                },
		                emphasis:{label:{show:true}}
			            },
			            data:data.regionList
			        },
			
			
			        {
			            name:'性别',
			            type:'pie',
			            tooltip : {
			                trigger: 'item',
			                formatter: '{a} <br/>{b} : {c} ({d}%)'
			            },
			            center: [180,70],
			            radius : [0, 40],
			            itemStyle :　{
			                normal : {
			                    labelLine : {
			                        length : 20
			                    }
			                }
			            },
						data:data.sexList
			        }
			    ]
			};
	 	  myChart.setOption(option); 
	 	  
	 	  
 	      	 myChart.on("click", function(param){
        	  var	code = param.data.code;
        	  // if(code.length<=9){
        	  // 	 backCode = code;
        	 // 	 showEchart(code);
        	 // 	 showBackButton(code);
        	 //  }
		   if(code&&param.value > 0) {//为0的部分，点击后，不弹窗
		    //opt.targetUrl = '<%=APP_URL_RS%>/rspermanent/showPerList.jhtml?tarCode=' + code;
			//openJqueryWindowByParams(opt);
			}
        	});
	 	  
	}
function showAgeEchart(orgCode){

		 $.ajax( {
    		url:"${rc.getContextPath()}/zhsq/szzg/classStat/ageEchart.jhtml?orgCode="+orgCode,  
    		type : 'POST',
    		dataType : "json",
    		error : function(data) {
    		},
    		success : function(retXml) {
    			showAge(retXml);
    		}
    	});  
}
function showAge(data){
		 var windowsHeight = $(window).height(); 
		 $('#chartAgeDiv').height(windowsHeight-100);

		if(data.maleAgeList.length>0){
		// $('#chartAgeDiv').height(465);	
         var myChart = echarts.init(document.getElementById('chartAgeDiv')); 
         var option = {
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			      legend: {
				        data:['男', '女'],
				         y: 'top',
				        textStyle:{
                               color:'#FFFFFF'
                        	}
				    },
			  	grid:{
					x:58,
					y:20,x2:10
				},
			    color:['#87cefa','#ff7f50'],
			    calculable : false,
			    yAxis : [
			        {
			            type : 'category',
			            splitLine  : false,
			            axisLabel:{ 
							textStyle:{
                               color:'#FFFFFF'
                        	}
                    	},
			            data : ['0-12岁','13-19岁','20-29岁','30-39岁','40-49岁','50-59岁','60-69岁','70岁以上']
			        }
			    ],
			    xAxis : [
			        {
			            type : 'value',
			            
			            axisLabel:{ 
			             interval:0,
                         rotate:50,
							textStyle:{
                               color:'#FFFFFF'
                        	}
                    	}
			        }
			    ],
			    series : [
			
			        {
			            name:'男',
			            type:'bar',
			            stack: '性别',
			            itemStyle:{
		                emphasis:{label:{show:true}}
			            },
			            data:data.maleAgeList
			        },
			        {
			            name:'女',
			            type:'bar',
			            stack: '性别',
			            itemStyle:{
		                emphasis:{label:{show:true}}
			            },
			            data:data.femaleAgeList
			        }
			    
			    ]
			};
	 	  myChart.setOption(option); 
	 	 }else{
	 	 
	 	 	noData("chartAgeDiv");
	 	 }
	}	
</script>
<script type="text/javascript">
function noData(id){
	var div = document.getElementById(id);
	var w = $("#"+id).width(),h = $("#"+id).height();
	if(w.length == 0 || h.length == 0){
		w = "120",h = "120";
	}
	div.innerHTML = "<div style='width:"+w+"px;height:"+h+"px;vertical-align: middle;text-align:center;display: table-cell;'><img 

src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
}
</script>
</body>

</html>
