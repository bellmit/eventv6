<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>打击重点统计分析</title>
		<!-- 引入layUI样式 -->
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/layui-v2.3.0/layui/css/layui.css"/>
		<!-- 引入 ECharts 文件 -->
		<script src="${uiDomain}/web-assets/plugins/echarts-3.2.3/echarts.min.js"></script>
		<!-- 本部样式 -->
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/basic/ch-analysis.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/layui-v2.3.0/layui/layuiExtend.css"/>
		<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
		<#include "/component/commonFiles-1.1.ftl" />
		<#include "/component/AnoleDate.ftl">
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>  
	</head>
	<body>
	<input type="hidden" id="startDate" name="startDate" />
	<input type="hidden" id="endDate" name="endDate" />	
		<div class="container-analiysis flex">
			<div class="analiysis-box flex-jc">
				<div class="analiy-top">
					<h2 class="analiy-title">打击重点统计分析</h2>
					<div class="target-boardx clearfix">
						<div class="tb-btn-box fl clearfix">
							<a href="javascript:void(0);" class="tb-bb-ech fl active"></a>
							<a href="javascript:void(0);" class="tb-bb-table fl"></a>
						</div>
						<div class="target-cpt-time fr">
							<div class="layui-inline"> <!-- 注意：这一层元素并不是必须的 -->
								<input type="text" class="layui-input"  id="date" name="date" style="width: 180px;">
							</div>
							<a href="javascript:void(0)" onclick = 'searchData()'
							style="display: inline-block;width:72px;height:26px;background-color:#4eaacc;border-radius: 2px;text-align: center;line-height: 26px;font-size: 14px;color: #fff;margin-left: 15px;font-family: 'Microsoft Yahei','simsun','arial','tahoma';">查询</a>
							<a href="javascript:void(0)" onclick = 'reset()'
							style="display: inline-block;width:72px;height:26px;background-color:#4eaacc;border-radius: 2px;text-align: center;line-height: 26px;font-size: 14px;color: #fff;margin-left: 15px;font-family: 'Microsoft Yahei','simsun','arial','tahoma';">重置</a>
						</div>
					</div>
				</div>
				<!--echats-->
				<div class="analiysis-echats">
					<div class="ae-content ae-content1" style="display: block;">
					<div id="container" class="xm-ect-tx"></div>
					</div>
					<div class="ae-content ae-content2" >
						
						<script>
							//这段js放在下面会引起页面样式出现问题，故放在这里
							$(window).on('load resize',function(){
							//计算高度
							var winH = $(window).height();
				            var analiytopH = $('.analiy-top').height();
				            $('.analiysis-echats').height( winH - analiytopH  - 60);
				            $('.ae-table-box .ae-table2').height($('.analiysis-echats').height() - 66);
				            $('.ae-table-box .ae-table2').niceScroll({
				                    cursorcolor:"rgba(0, 0, 0, 0.3)",
				                    cursoropacitymax:1,
				                    touchbehavior:false,
				                    cursorwidth:"4px",
				                    cursorborder:"0",
				                    cursorborderradius:"4px",
				                //  autohidemode: false //隐藏式滚动条
				                });
							})
							//图表跟表格的切换
							var index;
							$('.tb-btn-box').on('click','a',function(){
								$(this).addClass('active').siblings().removeClass('active');
								index = $(this).index();
								if (index == 0) {
									$('.ae-content1').show().siblings().hide();
									$('.ae-table-box .ae-table2').getNiceScroll().resize();
								} else{
									$('.ae-content2').show().siblings().hide();
									$('.ae-table-box .ae-table2').getNiceScroll().resize();
								}
							})
							
							$(function() {
								 // 加载时间插件
					 	        var dateApi = $("#date").anoleDateRender({
					 	        	BackfillType : "1",
					 	            ShowOptions : {
					 	                TabItems : [ "月","季","年" ]
					 	            },
					 	            BackEvents : {
					 	                OnSelected : function(api) {
					 	                    $("#startDate").val(api.getStartDate());
					 	                    $("#endDate").val(api.getEndDate());
					 	                }
					 	            }		            
					 	        }).anoleDateApi();
								
								searchData();
						    });
							
							
						
						    function getByColumn(resultMapList,sum,id) {
						    	
						    	var result = [];
						    	var diceName = [];
						    	if(sum >0){
							    	for (var i=0;i<resultMapList.length;i++){
							    		diceName.push(resultMapList[i].dictName);
							    		result.push({value:parseInt(resultMapList[i].count),percentage:(parseInt(resultMapList[i].count)/sum*100).toFixed(2)});
							    	}
						    	}else{
						    		for (var i=0;i<resultMapList.length;i++){
							    		diceName.push(resultMapList[i].dictName);
							    		result.push(parseInt(resultMapList[i].count));
							    	}
						    	}
						    	var imgDatUrl = 'image://data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iMTdweCIgaGVpZ2h0PSIxMDlweCIgdmlld0JveD0iMCAwIDE3IDEwOSIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4KICAgIDwhLS0gR2VuZXJhdG9yOiBTa2V0Y2ggNDYuMiAoNDQ0OTYpIC0gaHR0cDovL3d3dy5ib2hlbWlhbmNvZGluZy5jb20vc2tldGNoIC0tPgogICAgPHRpdGxlPjI8L3RpdGxlPgogICAgPGRlc2M+Q3JlYXRlZCB3aXRoIFNrZXRjaC48L2Rlc2M+CiAgICA8ZGVmcz4KICAgICAgICA8bGluZWFyR3JhZGllbnQgeDE9IjI1LjcwODEzMTglIiB5MT0iMzEuMTc4Njk2NyUiIHgyPSI4Ny4xMDI2NDY0JSIgeTI9IjUwJSIgaWQ9ImxpbmVhckdyYWRpZW50LTEiPgogICAgICAgICAgICA8c3RvcCBzdG9wLWNvbG9yPSIjNDQ1QjdDIiBzdG9wLW9wYWNpdHk9IjAiIG9mZnNldD0iMCUiPjwvc3RvcD4KICAgICAgICAgICAgPHN0b3Agc3RvcC1jb2xvcj0iIzQ0NUI3QyIgb2Zmc2V0PSIxMDAlIj48L3N0b3A+CiAgICAgICAgPC9saW5lYXJHcmFkaWVudD4KICAgIDwvZGVmcz4KICAgIDxnIGlkPSJQYWdlLTEiIHN0cm9rZT0ibm9uZSIgc3Ryb2tlLXdpZHRoPSIxIiBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPgogICAgICAgIDxnIGlkPSLkuqTpgJrov5DooYznu5/orqHmlLkiIHRyYW5zZm9ybT0idHJhbnNsYXRlKC00OTUuMDAwMDAwLCAtMTg4LjAwMDAwMCkiPgogICAgICAgICAgICA8ZyBpZD0iMi4iIHRyYW5zZm9ybT0idHJhbnNsYXRlKDQ1Ny4wMDAwMDAsIDU2LjAwMDAwMCkiPgogICAgICAgICAgICAgICAgPGcgaWQ9IjEu5Zyw6Z2i5Lqk6YCa6L+Q6JCl54+t5qyh5YiG5p6QIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgxLjAwMDAwMCwgNDQuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICAgICAgPGcgaWQ9Ikdyb3VwLTEyIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgzMi4wMDAwMDAsIDg1LjAwMDAwMCkiPgogICAgICAgICAgICAgICAgICAgICAgICA8ZyBpZD0iMiI+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cG9seWdvbiBpZD0iUmVjdGFuZ2xlLTMiIGZpbGw9IiMwNzFFM0IiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDExLjQwNzQwNywgNTkuMjAzNzA0KSBzY2FsZSgtMSwgMSkgdHJhbnNsYXRlKC0xMS40MDc0MDcsIC01OS4yMDM3MDQpICIgcG9pbnRzPSI4LjE0ODE0ODE1IDkuNzc3Nzc3NzggMTQuNjY2NjY2NyA2LjUxODUxODUyIDE0LjY2NjY2NjcgMTA4LjYyOTYzIDguMTQ4MTQ4MTUgMTExLjg4ODg4OSI+PC9wb2x5Z29uPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgPHBvbHlnb24gaWQ9IlJlY3RhbmdsZS02IiBmaWxsPSJ1cmwoI2xpbmVhckdyYWRpZW50LTEpIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgxMC45NTI2OTgsIDEwNi43MjU4ODEpIHJvdGF0ZSgyNS4wMDAwMDApIHRyYW5zbGF0ZSgtMTAuOTUyNjk4LCAtMTA2LjcyNTg4MSkgIiBwb2ludHM9IjYuMDkwMTk4NDEgMTAzLjg2MTU1IDEzLjI0NzYzNTcgMTAzLjczMDI3NSAyMS4wMDgxOTg4IDEwNC4yMTY2MzkgMTYuNTAxMjcxMSAxMDkuNzIxNDg3IDAuODk3MTk4MTA3IDEwOS4yOTU2MzciPjwvcG9seWdvbj4KICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwb2x5Z29uIGlkPSJSZWN0YW5nbGUtMyIgZmlsbD0iIzBEQ0U4NSIgcG9pbnRzPSIxNC42NjY2NjY3IDkuNzc3Nzc3NzggMjEuMTg1MTg1MiA2LjUxODUxODUyIDIxLjE4NTE4NTIgMTA4LjYyOTYzIDE0LjY2NjY2NjcgMTExLjg4ODg4OSI+PC9wb2x5Z29uPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgPHBvbHlnb24gaWQ9IlJlY3RhbmdsZS00IiBmaWxsPSIjNDQ1QjdDIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgxNC42NzUwMTUsIDYuNTEwMjY5KSByb3RhdGUoNDUuMDAwMDAwKSB0cmFuc2xhdGUoLTE0LjY3NTAxNSwgLTYuNTEwMjY5KSAiIHBvaW50cz0iMTIuODk5NTgwMiA0LjAwMjM5NDM5IDE5LjI3ODEwMDggMS45MDcxODMzNCAxNi45NjQ3MDE3IDguNzk5OTU1MDYgMTAuMDcxOTMgMTEuMTEzMzU0MSI+PC9wb2x5Z29uPgogICAgICAgICAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICA8L2c+CiAgICAgICAgPC9nPgogICAgPC9nPgo8L3N2Zz4='
				    			var option = {
				        	    color: ['#3398DB'],
				        	    tooltip : {
				        	        trigger: 'axis',
				        	        axisPointer : {           
				        	            type : 'shadow'       
				        	        },
				        	        formatter: function (params, ticket, callback) {
				        	          	return params[0].name +'<br/>'+params[0].seriesName+'/占比'+': '+params[0].value+'/'+params[0].data.percentage +'%';
				        	         }
				        	    },
				        	    grid: {
				        	        left: '3%',
				        	        right: '4%',
				        	        bottom: '16%',
				        	        containLabel: true
				        	    },
				        	    xAxis : [
				        	        {
				        	            type : 'category',
				        	            data : diceName,
				        	            axisTick: {
				        	                alignWithLabel: true
				        	            },
				        	            axisLabel: {
				        				    interval:0,//横轴信息全部显示
				        				    rotate:-10,//-30度角倾斜显示    
				        	            }

				        	        }
				        	    ],
				        	    yAxis : [
				        	        {
				        	            type : 'value',
				        	            name : '线索量'
			    	            		
				        	        }
				        	    ],
				        	    series : [
				        	        {
				        	            name:'线索量',
				        	            type:'pictorialBar',
				        	            barWidth: '60%',
				        	            symbol: imgDatUrl,
				        	            label: {
				        	                normal: {
				        	                    show: true,
				        	                    fontSize: 22,
				        	                    position: 'top',
				        	                    formatter: function(param){
						        	            if(param.value > 0){
						        	            	return param.data.num;
						        	            	}else{
						        	            	return "";
						        	            	}
						        	            }
				        	                }
				        	            },
				        	            data:result
				        	        }
				        	    ]
				        	}; 		
						    var myChart = echarts.init(document.getElementById(id));
					        myChart.setOption(option);
					        window.addEventListener("resize",function(){myChart.resize()});
						    	
						    	}
						    
						    /*查询*/			
							function searchData() {
								var startDate = $('#startDate').val();
				            	var endDate = $('#endDate').val();
								$.ajax( {
						            url: '${rc.getContextPath()}/zhsq/eventSBREClueReport/listChart.json',
						            type: 'POST',
						            data: { 
									startDate : $('#startDate').val(),
					            	endDate : $('#endDate').val()
						            },
						            dataType:"json",
						            error: function(data){
						            	$.messager.alert('提示','信息获取异常!','warning');
						            },
						            success: function(data){
						            	getByColumn(data.resultMapList,data.sum,'container');
						            	$("#myiframe").attr("src","${rc.getContextPath()}/zhsq/eventSBREClueReport/attackFocusReport.jhtml?type=2&startDate="+startDate+"&endDate="+endDate);
						            }
						    	});				
							}
							
							function reset() {//重置
								$('#startDate').val("");
								$('#endDate').val("");
								$('#date').val("");
								searchData();
							}
						    
							
						</script>
						<iframe name="myiframe" id="myiframe" src="${rc.getContextPath()}/zhsq/eventSBREClueReport/attackFocusReport.jhtml?type=2" frameborder="0"  width="100%" height="100%" scrolling="auto">
					</div>
				</div>
			</div>
		</div><!--container-fluid-->
	</body>
	
	
</html>
