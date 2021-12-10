<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>chart图表</title>
	<!--easyui所有相关样式需要放在样式前面-->
	<!-- 本部样式文件 -->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/echarts/reset.css" /> <!-- 重置默认样式 -->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/echarts/index-cpt.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>

<body>
	<!-- 组件开始 -->
	<div class="main clear">
		<!--增删改按钮栏-->
			<!-- 内容 -->
			<div class="dom-ifm">

				<!-- 一个柱子 -->
				<div class="dom-ifm-warp">
					<!-- <h5 class="dom-tit cor-blue">基本柱状1</h5> -->
					<!--组件内容-->
					<!--<iframe src="cpt-crud-1.html" width="100%" height="100px"></iframe>-->
					<!--DOM内容-->
					<div class="dom-warpbox h-300px">
						<!--请将下面整个DOM打包-->
						<div class="xm-chartbox">
							<div class="xm-chart">
								<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
								<div id="BarNormalA" style="width: 100%;height:100%;"></div>
							</div>
						</div>
					</div>
				</div>
		</div>


	</div>
	<!-- basic -->
	</script>
	<script src="${rc.getContextPath()}/js/echarts/jquery.nicescroll.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${rc.getContextPath()}/js/echarts/divscroll.js"></script>
	<!-- basic charts -->
    <script src="${rc.getContextPath()}/js/echarts/echarts.js"></script>
	<!-- <script src="${rc.getContextPath()}/js/echarts/xm-chat.js"></script> -->
	
	<script>
		var BarChart = echarts.init(document.getElementById('BarNormalA'));
		var infoOrgCode = '${infoOrgCode}';
		//固定滚动条
		$(function () {
			$('body').perfectScrollbar();

			//              组件展开
			var ifmH;
			$('.ifm-drawer a').on('click', function () {
				ifmH = $(this).parent().siblings().children('iframe').height();
				if ($(this).hasClass('ifm-on')) {
					$(this).parent().siblings('.ifm-wrap').animate({
						'height': 0
					}, 350);
					$(this).removeClass('ifm-on');
				} else {
					$(this).parent().siblings('.ifm-wrap').animate({
						'height': ifmH + 2
					}, 350);
					$(this).addClass('ifm-on');
				}
			});

			$('.choics-case').click(function () { //点击标签
				if ($('.pup-choics-case').is(':hidden')) { //如果当前隐藏
					$('.pup-choics-case').show(); //那么就显示div
				} else { //否则
					$('.pup-choics-case').hide(); //就隐藏div
				}
			})
			getDisputeData();
		})
		// 获取主要致贫原因柱状图数据并初始化图表
        function getDisputeData(){
			BarChart.showLoading('default', {maskColor: 'rgba(0,0,0,0)',textColor: '#fff',});
            $.ajax({
                type: 'post',
                url: '${rc.getContextPath()}/zhsq/map/gisstat/gisStat/getDisputeBarData.jhtml?infoOrgCode='+infoOrgCode,
                dataType: "json",
                success:function(data){
                	initDisputeChart(data);
                },
                error:function(){
                    //ajax请求失败
                },
                complete:function(){
                	BarChart.hideLoading();
                }
            });
        }
		
		// 初始化主要致贫原因柱状图
        function initDisputeChart(map){
			var data = map;
			var xData = [],
				zData = [],
			    yData = [];
			var bgData = [];
			
			for (var i = 0; i < map.length; i++) {
				xData.push(map[i].GRID_NAME);
				zData.push(map[i].ID_);
				yData.push(map[i].TOTAL_);
			}
			var option1 = {
					
			    tooltip: {
			        trigger: 'axis',
			
			        axisPointer: {
			            type: 'shadow',
			            label: {
			                show: true,
			                formatter: ' {value} ',
			                padding: [4, 6, 6, 4],
			                backgroundColor: '#66a8ff',
			                shadowColor: 'rgba(76, 118, 255, 0.3)',
			                shadowBlur: 10,
			                shadowOffsetY: 4,
			            },
			            shadowStyle: {
			                color: 'rgba(0, 0, 0, 0.1)',
			                opacity: 0.5,
			            }
			        },
			        formatter: '{a0}<br>{b0}：{c0}',
			        padding: [5, 10],
			        backgroundColor: 'rgba(0, 0, 0, 0.5)',
			        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
			        textStyle: {
			            color: '#fff',
			        }
			    },
			    title: false,
			    grid: {
			        show: false,
			        top: 40,
			        right: '4%',
			        bottom: '12%',
			        left: '6%',
			    },
			    legend: {
			        show: true,
			        textStyle: {
			            color: '#666'
			        },
			        top: 10,
			        x: 'left', 
			        itemGap: 40,
			        itemWidth: 10,
			        inactiveColor: 'rgba(0, 0, 0, 0.3)',
			        data: ['矛盾纠纷'],
			    },
			    xAxis: {
			        name: false,
			        nameLocation: 'end',
			        nameGap: '2',
			        gridIndex: 0,
			       // boundaryGap: ['10%', '10%'], //坐标轴两边留白
			        axisLine: {
			            onZero: true,
			            lineStyle: {
			                color: '#ccc',
			            }
			        },
			        axisTick: {
			            show: false,
			        },
			        //坐标轴刻度标签
			        axisLabel: {
			            color: '#666',
			            interval:0,
			            rotate:20
			        },
			        type: 'category',
			        data:xData
			    },
			    yAxis: [{
			        type: 'value',
			        name: false,
			        nameTextStyle: {
			            color: '#999',
			        },
			        nameGap: '14',
			        // max: '3000',
			        splitNumber: 4,
			        axisLine: {
			            show: false,
			            lineStyle: {
			                color: '#66a8ff',
			            },
			        },
			        //坐标轴刻度
			        axisTick: {
			            show: false
			        },
			        splitLine: { //坐标轴在 grid 区域中的分隔线
			            show: true,
			            lineStyle: {
			                color: '#ccc',
			                width: 1,
			                type: 'dotted',
			            },
			        },
			        //坐标轴刻度标签
			        axisLabel: {
			            color: '#999',
			            formatter: '{value} ',
			        },
			    }, ],
			    dataZoom: false,
			    series: [{
			        name: '矛盾纠纷',
			        type: 'bar',
			        label: {
			            show: true,
			            color: '#666',
			            position: 'top',
			            distance: 10,
			        },
			        //图形样式
			        itemStyle: {
			            color: 'rgba(102, 168, 255, 0.8)',
			            borderColor: '#66a8ff',
			        },
			        barWidth: 26, //柱图宽度
			        emphasis: {
			            label: {
			                show: true,
			                fontSize: 12,
			                color: '#66a8ff',
			                position: 'top',
			                distance: 5,
			            },
			        },
			        data: yData,
			    }, ]
			};
			
			 //根据窗口大小调整图表
            window.onresize = BarChart.resize; //解决问题4：如果窗口大小变化，图表可以自动进行调整；
			// 使用刚指定的配置项和数据显示图表。
			BarChart.setOption(option1);
            
			BarChart.on('click', function (params) { 
				var gridId = zData[params.dataIndex];
				var url = "${rc.getContextPath()}/zhsq/disputeMediation/9x/index.jhtml?gridId="+gridId+"&typeDispute="+1;
				//window.location.href=url; 
				parent.showMaxJqueryWindow('矛盾纠纷列表',url,null,null);
          });
		}

        function refresh(){            
            console.log("refresh");
            option.series.data = getDisputeData();
            myChart.setOption(option);
        };  
		
	</script>
	
	

</body>

</html>