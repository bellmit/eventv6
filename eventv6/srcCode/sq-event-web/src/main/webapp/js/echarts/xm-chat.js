// echats
// 本图表未标准样式图形~由规划运营部设计整理，请尽量按照本规范来使用
// ---------------------------------------------------------------------------------

// 基于准备好的dom，初始化echarts实例----柱状图


//demo1
var BarChart = echarts.init(document.getElementById('BarNormalA'));

var option1 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
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
        top: 5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['用户界面类'],
    },
    xAxis: {
        name: false,
        // nameTextStyle: {
        //     color: '#668aff',
        // },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['时间轴', '进度条', '查询选择', '资源操作', '表单及字段', '报表图表', '翻译组件', '弹窗组件', '其它类组件']
    },
    yAxis: [{
        type: 'value',
        name: false,
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
        name: '用户界面类',
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
                distance: 10,
            },
        },
        data: [100, 98, 78, 64, 53, 48, 46, 39, 3],

    }, ]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option1);
// ---------------------------------------------------------------------------------

//demo1



//demo2
// 基于准备好的dom，初始化echarts实例----柱状图
var BarChart = echarts.init(document.getElementById('BarNormalB'));

var option2 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
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
        bottom: '16%',
        left: '6%',
    },
    // toolbox: {
    //     right:'10%',
    //     top:0,
    //     feature: {
    //         dataView: {show: false, readOnly: false},
    //         magicType: {show: true, type: ['line', 'bar']},
    //         restore: {show: true},
    //         saveAsImage: {show: false}
    //     },
    // },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: 5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['用户界面类'],
    },
    xAxis: {
        name: false,
        // nameTextStyle: {
        //     color: '#668aff',
        // },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['时间轴', '进度条', '查询选择', '资源操作', '表单及字段', '报表图表', '翻译组件', '弹窗组件', '其它类组件']
    },
    yAxis: [{
        type: 'value',
        name: false,
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
    dataZoom: [{
        type: 'slider',
        height: 16,
        bottom: 2,
        start: 0,
        end: 60,
        backgroundColor: 'rgba(102,168,255,.2)',
        dataBackground: {
            areaStyle: {
                color: 'rgba(102,168,255,.2)',
            },
        },
        fillerColor: 'rgba(167,183,204,0.5)',
        borderColor: 'rgba(102,168,255,.05)',
        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
        handleSize: '110%',
        handleStyle: {
            color: 'rgba(102,168,255,.5)',

        },
        textStyle: {
            color: "rgba(167,183,204,0.9)"
        },
    }],
    series: [{
        name: '用户界面类',
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
                distance: 10,
            },
        },
        data: [100, 98, 78, 64, 53, 48, 46, 39, 3],

    }, ]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option2);
// ---------------------------------------------------------------------------------

//demo2






// 基于准备好的dom，初始化echarts实例-----柱状图
var BarChart = echarts.init(document.getElementById('demo1_3'));

var option1_3 = {
    title: false,
    tooltip: {
        trigger: 'axis',
        position: function (pt) {
            return [pt[0], '10%'];
        },
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
                color: 'rgba(76, 118, 255, 0.2)',
                opacity: 0.5,
            }
        },
        padding: [5, 10],
        backgroundColor: 'rgba(76, 118, 255, 0.3)',
        extraCssText: 'box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);',
        textStyle: {
            color: '#fff',
        }
    },
    legend: {
        bottom: 0,
        itemWidth: 12,
        itemHeight: 12,
        data: ['电信', '移动', '联通'],
        textStyle: {
            color: '#668aff'
        }
    },
    grid: {
        show: false,
        top: 26,
        right: '5%',
        bottom: '16%',
        left: '4%',
        containLabel: true,
    },
    xAxis: {
        name: '地区',
        nameTextStyle: {
            color: '#668aff',
        },
        nameLocation: 'end',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#66a8ff',
            }
        },
        axisTick: {
            show: false,
        },
        //坐标轴刻度标签
        axisLabel: {
            color: '#668aff',
        },
        data: ['发送总数', '成功量'],

    },
    yAxis: {
        nameTextStyle: {
            color: '#668aff',
        },
        nameGap: '14',
        splitNumber: 5,
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
                color: 'rgba(102, 143, 255, 0.5)',
                width: 1,
                type: 'dotted',
            },
        },
    },
    series: [{
            name: '电信',
            type: 'bar',
            symbolSize: 8,
            // label:{
            //     show: true,
            //     color: '#fff',
            //     position: 'top',
            //     distance: 6,
            // },
            //图形样式
            itemStyle: {
                color: '#ffca61',
            },
            barWidth: 20,
            data: [2205, 1159],
            stack: '发送总数',
        },
        {
            name: '移动',
            type: 'bar',
            symbolSize: 8,
            // label: {
            //     show: true,
            //     color: '#fff',
            //     position: 'top',
            //     distance: 6,
            // },
            //图形样式
            itemStyle: {
                color: '#668fff',
            },
            barWidth: 20,
            data: [10693, 7298],
            stack: '发送总数',
        },
        {
            name: '联通',
            type: 'bar',
            symbolSize: 8,
            // label: {
            //     show: true,
            //     color: '#fff',
            //     position: 'top',
            //     distance: 6,
            // },
            //图形样式
            itemStyle: {
                color: '#ff6161',
            },
            barWidth: 20,
            data: [2132, 918],
            stack: '发送总数',
        },
    ]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option1_3);





var BarChart = echarts.init(document.getElementById('demo1_4'));



var option1_4 = {

    title: false,
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} ',
                padding: [4, 3, 6, 2],
                backgroundColor: '#66a8ff',
                shadowColor: 'rgba(76, 118, 255, 0.3)',
                shadowBlur: 8,
                shadowOffsetY: 2,
            },
            shadowStyle: {
                color: 'rgba(0, 0, 0, 0.1)',
                opacity: 0.5,
            }
        },
        padding: [5, 10],
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        },
    },
    legend: {
        bottom: '0',
        itemWidth: 14,
        itemGap: 20,
        data: ['开发人数']
    },
    grid: {
        left: '5%',
        right: '10%',
        bottom: '4%',
        top: '0%',
        containLabel: true
    },
    xAxis: {
        axisTick: {
            show: false,
        },
        axisLine: {
            show: false,
        },
        splitLine: {
            lineStyle: {
                color: '#e5e5e5',
                type: 'dotted',
            }
        },
        type: 'value',
        boundaryGap: [0, 0]
    },
    yAxis: {
        axisTick: {
            show: false,
        },
        axisLine: {
            lineStyle: {
                color: '#ccc',
            }
        },
        axisLabel: {
            color: '#666',
        },
        type: 'category',
        data: ['60分以下', '60-70分', '70-80分', '80-90分', '90分以上'],
    },
    series: [{
        name: '开发人员人数',
        type: 'bar',
        //图形样式
        itemStyle: {
            color: 'rgba(102, 168, 255, 0.8)',
            borderColor: '#66a8ff',
        },
        barWidth: '20',
        label: {
            show: true,
            position: 'insideRight',
            color: '#fff',
        },
        data: [4, 6, 10, 14, 20]
    }, ]
};
// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option1_4);





var BarChart = echarts.init(document.getElementById('demo1_5'));
option1_5 = {
    grid: {
        x: 45,
        y: 15,
        x2: 55,
        y2: 20,
        borderWidth: 1
    },

    tooltip: {
        trigger: 'axis'
    },

    toolbox: {
        show: true,

    },
    calculable: true,
    xAxis: [{
        type: 'category',
        data: ['某某区', '某某区', '某某区', '某某区', '某某区'],
        axisLabel: {
            textStyle: {
                color: "#999"
            }
        },

        axisLine: {
            lineStyle: {
                type: 'solid',
                color: '#2b3e66', //左边线的颜色
                width: '1' //坐标线的宽度
            }
        },
    }],
    yAxis: [{
        splitLine: {
            show: false
        }, //去除网格线

        type: 'value',
        axisLabel: {
            textStyle: {
                color: "#999"
            }
        },
        axisLine: {
            lineStyle: {
                type: 'solid',
                color: '#2b3e66', //左边线的颜色
                width: '1' //坐标线的宽度
            }
        },
    }],
    series: [{
            name: '前五名',
            type: 'bar',
            barWidth: 30,

            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#abbed1'
                    }, {
                        offset: 1,
                        color: '#0e1636'
                    }]),
                }
            },

            data: [75, 55, 50, 35, 25],

        },

    ]
};

// 为echarts对象加载数据 
BarChart.setOption(option1_5);




var BarChart = echarts.init(document.getElementById('demo1_6'));

var dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
var data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
var yMax = 500;
var dataShadow = [];

for (var i = 0; i < data.length; i++) {
    dataShadow.push(yMax);
}

option1_6 = {
    title: {
        text: '特性示例：渐变色 阴影 滚动缩放',

    },
    xAxis: {
        data: dataAxis,
        axisLabel: {
            inside: true,
            textStyle: {
                color: '#fff'
            }
        },
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        z: 10
    },
    yAxis: {
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        },
        axisLabel: {
            textStyle: {
                color: '#999'
            }
        }
    },
    dataZoom: [{
        type: 'inside'
    }],
    series: [{ // For shadow
            type: 'bar',
            itemStyle: {
                normal: {
                    color: 'rgba(0,0,0,0.05)'
                }
            },
            barGap: '-100%',
            barCategoryGap: '40%',
            data: dataShadow,
            animation: false
        },
        {
            type: 'bar',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [{
                                offset: 0,
                                color: '#83bff6'
                            },
                            {
                                offset: 0.5,
                                color: '#188df0'
                            },
                            {
                                offset: 1,
                                color: '#188df0'
                            }
                        ]
                    )
                },
                emphasis: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [{
                                offset: 0,
                                color: '#2378f7'
                            },
                            {
                                offset: 0.7,
                                color: '#2378f7'
                            },
                            {
                                offset: 1,
                                color: '#83bff6'
                            }
                        ]
                    )
                }
            },
            data: data
        }
    ]
};

// Enable data zoom when user click bar.
var zoomSize = 6;
BarChart.on('click', function (params) {
    console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
    BarChart.dispatchAction({
        type: 'dataZoom',
        startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
        endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
    });
});

BarChart.setOption(option1_6);




var BarChart = echarts.init(document.getElementById('demo1_7'));

var labelRight = {
    normal: {
        position: 'right'
    }
};
option1_7 = {
    title: {
        text: '交错正负轴标签',

    },
    tooltip: {
        trigger: 'axis',
        axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        top: 80,
        bottom: 30
    },
    xAxis: {
        type: 'value',
        position: 'top',
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
    },
    yAxis: {
        type: 'category',
        axisLine: {
            show: false
        },
        axisLabel: {
            show: false
        },
        axisTick: {
            show: false
        },
        splitLine: {
            show: false
        },
        data: ['ten', 'nine', 'eight', 'seven', 'six', 'five', 'four', 'three', 'two', 'one']
    },
    series: [{
        name: '生活费',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                formatter: '{b}'
            }
        },
        data: [{
                value: -0.07,
                label: labelRight
            },
            {
                value: -0.09,
                label: labelRight
            },
            0.2, 0.44,
            {
                value: -0.23,
                label: labelRight
            },
            0.08,
            {
                value: -0.17,
                label: labelRight
            },
            0.47,
            {
                value: -0.36,
                label: labelRight
            },
            0.18
        ]
    }]
};
BarChart.setOption(option1_7);





var BarChart = echarts.init(document.getElementById('demo1_8'));

var option1_8 = {

    tooltip: {
        trigger: 'axis',

        position: function (pt) {
            return [pt[0], '10%'];
        },
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} 月',
                padding: [4, 6, 6, 4],
                backgroundColor: '#66a8ff',
                shadowColor: 'rgba(102, 168, 255, 0.3)',
                shadowBlur: 10,
                shadowOffsetY: 4,
            },
            shadowStyle: {
                color: 'rgba(255, 180, 102, 0.2)',
                opacity: 0.5,
            }
        },
        formatter: '{b0}月<br>{c0}\n积分',
        padding: [5, 10],
        backgroundColor: 'rgba(50,50,50,0.6)',
        extraCssText: 'box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);',
        textStyle: {
            color: '#fff',
        }
    },
    title: false,
    grid: {
        show: false,
        top: 64,
        right: '6%',
        bottom: '18%',
        left: '9%',
    },
    xAxis: {
        name: '月份',
        nameTextStyle: {
            color: '#ccc',
        },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#66a8ff',
            }
        },
        axisTick: {
            show: false,
        },
        //坐标轴刻度标签
        axisLabel: {
            color: '#666',
        },
        data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12']
    },
    yAxis: {
        name: '积分',
        nameTextStyle: {
            color: '#ccc',
        },
        nameGap: '14',
        splitNumber: 3,
        axisLine: {
            show: true,
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
            color: '#666',
        },
    },
    series: [{
        type: 'bar',
        symbolSize: 8,
        label: {
            show: true,
            color: '#666',
            position: 'top',
            distance: 6,

        },
        //图形样式
        itemStyle: {
            color: 'rgba(102, 168, 255, 0.5)',
            borderColor: '#66a8ff',
        },
        barWidth: 20,

        data: [5, 4, 5, 6, 8, 7, 4, 6],
        markLine: {
            silent: true,
            precision: 0,
            label: {
                normal: {
                    position: 'end',
                    offset: [-30, 20],
                    formatter: '{c}'
                }
            },
            lineStyle: {
                normal: {
                    color: '#ff6666',
                }
            },
            data: [{
                type: 'average',
                name: '平均分',
                yAxis: '4',
            }],
        },
        emphasis: {
            itemStyle: {
                color: 'rgba(255, 102, 102, 0.9)',
                borderColor: '#ff6666',
                shadowColor: 'rgba(255, 102, 102, 0.3)',
                shadowBlur: 12,
                shadowOffsetY: 2,
            },
            label: {
                show: true,
                color: '#ff6666',
                fontSize: 14,
                fontWeight: 'bold',
            }
        }
    }]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option1_8);



var BarChart = echarts.init(document.getElementById('demo1_9'));

var option1_9 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} %',
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
        formatter: '{b0}<br>{a0}：{c0}%<br>{a1}：{c1}%',
        padding: [5, 10],
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        }
    },
    grid: {
        show: false,
        top: 40,
        right: '4%',
        bottom: '16%',
        left: '6%',
    },
    legend: {
        textStyle: {
            color: '#666'
        },
        top: -5,
        right: 0,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['已完成', '未完成'],
    },
    xAxis: {
        type: 'category',
        name: false,
        silent: false,
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
        axisTick: {
            alignWithLabel: true
        },

        splitLine: {
            show: false,
        },
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#ccc',
            }
        },
        axisLabel: {
            //fontWeight:10,
            //interval:2,
            align: 'center',
            color: '#999',
        },
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: [{
        type: 'value',

        splitLine: { //坐标轴在 grid 区域中的分隔线
            show: true,
            lineStyle: {
                color: '#ccc',
                width: 1,
                type: 'dotted',
            },
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            fontWeight: 10,
            fontsize: 5,
            color: '#999',
            formatter: '{value} ',
        }

    }],
    series: [{
        name: '已完成',
        type: 'bar',
        stack: '总量',
        barWidth: 20,
        itemStyle: {
            normal: {
                color: '#8db9ee',
            }
        },
        data: [1, 6, 8, 14, 3, 8, 1, 9]
    }, {
        name: '未完成',
        type: 'bar',
        stack: '总量',
        barWidth: 20,
        itemStyle: {
            normal: {
                color: '#bcc9da',
            }
        },
        data: [-3, -8, -1, -9, -1, -6, -8, -14]
    }]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option1_9);


//demo3
// 基于准备好的dom，初始化echarts实例----折线图
var BarChart = echarts.init(document.getElementById('demo2_1'));

var option2_1 = {
    tooltip: {
        trigger: 'axis',

        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} ',
                padding: [4, 6, 6, 4],
                backgroundColor: '#66a8ff',
                shadowColor: 'rgba(0, 0, 0, 0.3)',
                shadowBlur: 10,
                shadowOffsetY: 4,
            },
            shadowStyle: {
                color: 'rgba(0, 0, 0, 0.1)',
                opacity: 0.5,
            }
        },
        formatter: '{b0}<br>上报：{c0}\n个<br>答复：{c1}\n个',
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
        top: 48,
        right: '4%',
        bottom: '12%',
        left: '6%',
    },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: 5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['上报', '答复'],
    },
    xAxis: {
        name: '地区',
        nameTextStyle: {
            color: '#ccc',
        },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['大茂镇', '万城镇', '龙滚镇', '后安镇', '和乐镇', '长丰镇', '北大镇', '某某镇', '某某镇2', '某某镇3', '某某镇4', '某某镇5']
    },
    yAxis: [{
        type: 'value',
        name: '数量',
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
    series: [{
            name: '上报',
            type: 'bar',
            symbolSize: 8,
            label: {
                show: true,
                color: '#666',
                position: 'top',
                distance: 10,
            },
            //图形样式
            itemStyle: {
                color: 'rgba(255, 192, 102, 0.8)',
                borderColor: '#ffc066',
            },
            barWidth: 20,


            data: [887, 734, 680, 529, 395, 467, 444, 272, 95, 167, 444, 272]

        },
        {
            name: '答复',
            type: 'bar',
            label: {
                show: true,
                color: '#666',
                position: 'top',
                distance: 10,
            },
            //图形样式
            itemStyle: {
                color: 'rgba(96, 204, 82, 0.8)',
                borderColor: '#60cc52',
            },
            barWidth: 20,

            data: [676, 320, 353, 716, 513, 1097, 1025, 1316, 213, 397, 824, 416],

        },
    ]
};

// 使用刚指定的配置项和数据显示图表。
BarChart.setOption(option2_1);


//demo3



var BarChart = echarts.init(document.getElementById("demo2_2"));

option2_2 = {
    // title: {

    //     textStyle: {
    //         color: '#00FFFF',
    //         fontSize: 24
    //     }
    // },
    legend: {
        top: 5,
        textStyle: {
            color: '#999',
        },
        data: ['事件办结率', '办结及时率']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '2%',
        top: '18%',
        containLabel: true
    },

    tooltip: {
        show: "true",
        trigger: 'item',
        // backgroundColor: 'rgba(0,0,0,0.7)', // 背景
        padding: [8, 10], //内边距
        extraCssText: 'box-shadow: 0 0 3px rgba(255, 255, 255, 0.4);', //添加阴影
        formatter: function (params) {
            if (params.seriesIndex == "2" || params.seriesIndex == "3") {
                return params.name + '<br>' + params.seriesName + ' ：' + params.value + ' %';
            }
        }
    },
    yAxis: {
        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: true,
            lineStyle: {
                color: '#999',
            }
        },
        splitLine: {
            show: false,
            lineStyle: {
                color: '#363e83 ',
            }
        },
        axisLabel: {
            textStyle: {
                color: '#999',
                fontWeight: 'normal',
                fontSize: '12',
            },
        },
    },
    xAxis: [{
            type: 'category',
            axisTick: {
                show: false
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#999',
                }
            },
            axisLabel: {
                inside: false,
                textStyle: {
                    color: '#999',
                    fontWeight: 'normal',
                    fontSize: '12',
                },
                // formatter:function(val){
                //     return val.split("").join("\n")
                // },
            },
            data: ['矛盾纠纷', '民生服务', '治安安全', '环境卫生', '其他']
        }, {
            type: 'category',
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                show: false
            },
            splitArea: {
                show: false
            },
            splitLine: {
                show: false
            },
            data: ['矛盾纠纷', '民生服务', '治安安全', '环境卫生', '其他']
        },

    ],
    series: [{
            type: 'bar',
            xAxisIndex: 1,
            zlevel: 1,
            itemStyle: {
                normal: {
                    color: '#fff',
                    borderWidth: 0,
                    shadowBlur: {
                        shadowColor: 'rgba(255,255,255,0.31)',
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowOffsetY: 2,
                    },
                }
            },
            barWidth: '10%',
            data: [100, 100, 100, 100, 100]
        }, {
            type: 'bar',
            xAxisIndex: 1,
            barGap: '100%',
            data: [100, 100, 100, 100, 100],
            zlevel: 1,
            barWidth: '10%',
            itemStyle: {
                normal: {
                    color: '#fff',
                    borderWidth: 0,
                    shadowBlur: {
                        shadowColor: 'rgba(255,255,255,0.31)',
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowOffsetY: 2,
                    },
                }
            },
        }, {
            name: '事件办结率',
            type: 'bar',
            itemStyle: {
                normal: {
                    show: true,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#f7734e'
                    }, {
                        offset: 1,
                        color: '#e12945'
                    }]),
                    barBorderRadius: 50,
                    borderWidth: 0,
                }
            },
            zlevel: 2,
            barWidth: '10%',
            data: [96, 97, 98, 100, 97]
        }, {
            name: '办结及时率',
            type: 'bar',
            barWidth: '10%',
            itemStyle: {
                normal: {
                    show: true,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#96d668'
                    }, {
                        offset: 1,
                        color: '#01babc'
                    }]),
                    barBorderRadius: 50,
                    borderWidth: 0,
                }
            },
            zlevel: 2,
            barGap: '100%',
            data: [91, 85, 98, 99, 98]
        }

    ]
};


BarChart.setOption(option2_2);




var BarChart = echarts.init(document.getElementById("demo2_3"));
var option2_3 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
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
        formatter: '{b0}<br>累计剩余需求：{c0}\n个<br>发布需求：{c1}\n个<br>累计剩余工时：{c2}\n小时<br>发布工时：{c3}\n小时',
        padding: [5, 10],
        backgroundColor: 'rgba(102, 168, 255, 0.8)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        }
    },
    title: false,
    grid: {
        show: false,
        top: 48,
        right: '4%',
        bottom: '12%',
        left: '6%',
    },
    // toolbox: {
    //     right:'10%',
    //     top:0,
    //     feature: {
    //         dataView: {show: false, readOnly: false},
    //         magicType: {show: true, type: ['line', 'bar']},
    //         restore: {show: true},
    //         saveAsImage: {show: false}
    //     },
    // },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: 5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['累计剩余需求', '发布需求', '累计剩余工时', '发布工时'],
    },
    xAxis: {
        name: '地区',
        nameTextStyle: {
            color: '#668aff',
        },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['7月第一周', '7月第二周', '7月第三周', '7月第四周', '8月第一周', '8月第二周', '8月第三周', '8月第四周', '9月第一周', '9月第二周', '9月第三周', '9月第四周']
    },
    yAxis: [{
            type: 'value',
            name: '累计剩余需求/发布需求',
            nameTextStyle: {
                color: '#999',
            },
            nameGap: '14',
            max: '3000',
            splitNumber: 5,
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
        },
        {
            type: 'value',
            name: '工时',
            nameTextStyle: {
                color: '#999',
            },
            nameGap: '14',
            splitNumber: 5,
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#ffca61',
                },
            },
            //坐标轴刻度
            axisTick: {
                show: false
            },
            splitLine: { //坐标轴在 grid 区域中的分隔线
                show: false,
                lineStyle: {
                    color: 'rgba(102, 143, 255, 0.5)',
                    width: 1,
                    type: 'dotted',
                },
            },
            //坐标轴刻度标签
            axisLabel: {
                color: '#999',
                formatter: '{value} ',
            },
        },
    ],
    series: [{
            name: '累计剩余需求',
            type: 'bar',
            symbolSize: 8,
            label: {
                show: true,
                color: '#666',
                position: 'top',
                distance: 10,
            },
            //图形样式
            itemStyle: {
                color: 'rgba(255, 192, 102, 0.8)',
                borderColor: '#ffc066',
            },
            barWidth: 20,

            emphasis: {
                label: {
                    fontSize: 14,

                    backgroundColor: "#fff",
                    padding: [4, 6],
                    shadowColor: 'rgba(0, 0, 0, 0.2)',
                },

            },

            data: [887, 734, 680, 529, 395, 467, 444, 272, 95, 167, 444, 272]

        },
        {
            name: '发布需求',
            type: 'bar',
            label: {
                show: true,
                color: '#666',
                position: 'top',
                distance: 10,
            },
            //图形样式
            itemStyle: {
                color: 'rgba(96, 204, 82, 0.8)',
                borderColor: '#60cc52',
            },
            barWidth: 20,
            emphasis: {
                label: {
                    fontSize: 14,
                    backgroundColor: "#fff",
                    padding: [4, 6],
                    shadowColor: 'rgba(0, 0, 0, 0.2)',
                },

            },
            data: [676, 320, 353, 716, 513, 1097, 1025, 1316, 213, 397, 824, 416],

        },
        {
            name: '累计剩余工时',
            type: 'line',
            // label:{
            //     show: true,
            //     color: '#ffca61',
            //     position: 'top',
            //     distance: 6,
            // },
            yAxisIndex: 1,
            //图形样式
            itemStyle: {
                color: '#ff6161',
                borderColor: '#ff6161',
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#ff6161',
                },
            },
            data: [78, 185, 313, 268, 478, 322, 472, 185, 413, 68, 23, 52],

        },
        {
            name: '发布工时',
            type: 'line',
            // label:{
            //     show: true,
            //     color: '#ffca61',
            //     position: 'top',
            //     distance: 6,
            // },
            yAxisIndex: 1,
            //图形样式
            itemStyle: {
                color: '#66a8ff',
                borderColor: '#66a8ff',
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#66a8ff',
                },
            },
            data: [23, 52, 102, 472, 85, 413, 68, 78, 185, 313, 268, 378],

        }
    ]
};
BarChart.setOption(option2_3);




var BarChart = echarts.init(document.getElementById("demo3_1"));

var option3_1 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} %',
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
        formatter: '{b0}<br>{a0}：{c0}%<br>{a1}：{c1}%<br>{a2}：{c2}%<br>{a3}：{c3}%<br>{a4}：{c4}%',
        padding: [5, 10],
        backgroundColor: 'rgba(102, 168, 255, 0.8)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        }
    },
    title: false,
    grid: {
        show: false,
        top: 40,
        right: '0%',
        bottom: '18%',
        left: "8%",
    },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: -5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['累计剩余工时', '新增工时', '发布工时'],
    },
    xAxis: {
        name: false,
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: [{
        type: 'value',
        name: false,
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
    dataZoom: [{
        type: 'slider',
        height: 16,
        bottom: 2,
        start: 0,
        end: 50,
        backgroundColor: 'rgba(102,168,255,.2)',
        dataBackground: {
            areaStyle: {
                color: 'rgba(102,168,255,.2)',
            },
        },
        fillerColor: 'rgba(167,183,204,0.5)',
        borderColor: 'rgba(102,168,255,.05)',
        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
        handleSize: '110%',
        handleStyle: {
            color: 'rgba(102,168,255,.5)',

        },
        textStyle: {
            color: "rgba(167,183,204,0.9)"
        },
    }],
    series: [{
            name: '累计剩余工时',
            type: 'bar',
            symbolSize: 8,
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式

            itemStyle: {
                color: '#ff6161',
                borderColor: '#ff6161',
            },
            barWidth: 16,
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#ff6161',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [887, 734, 680, 529, 395, 467, 444, 272, 95, 167, 444, 272]

        },
        {
            name: '新增工时',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#ffc066',
                borderColor: '#ffc066',
            },
            barWidth: 16,
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#ffc066',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [676, 320, 353, 716, 513, 1097, 1025, 1316, 213, 397, 824, 416],

        },
        {
            name: '发布工时',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#66a8ff',
                borderColor: '#66a8ff',
            },
            barWidth: 16,
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#66a8ff',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [78, 185, 313, 268, 478, 322, 472, 185, 413, 68, 23, 52],

        },
    ]
};
BarChart.setOption(option3_1);



var BarChart = echarts.init(document.getElementById("demo3_2"));

var option3_2 = {
    title: false,
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} ',
                padding: [4, 3, 6, 2],
                backgroundColor: '#66a8ff',
                shadowColor: 'rgba(76, 118, 255, 0.3)',
                shadowBlur: 8,
                shadowOffsetY: 2,
            },
            shadowStyle: {
                color: 'rgba(0, 0, 0, 0.1)',
                opacity: 0.5,
            }
        },
        padding: [5, 10],
        backgroundColor: 'rgba(102, 168, 255, 0.8)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        },
    },
    legend: {
        bottom: '0',
        itemWidth: 14,
        itemGap: 20,
        data: ['发布工时', '新增工时', '累计剩余工时']
    },
    grid: {
        left: '0%',
        right: '10%',
        bottom: '12%',
        top: '8%',
        containLabel: true
    },
    xAxis: {
        axisTick: {
            show: false,
        },
        axisLine: {
            show: false,
        },
        splitLine: {
            lineStyle: {
                color: '#f5f5f5',
            }
        },
        type: 'value',
        boundaryGap: [0, 0.01]
    },
    yAxis: {
        axisTick: {
            show: false,
        },
        axisLine: {
            lineStyle: {
                color: '#666',
            }
        },
        type: 'category',
        data: ['推进部', '开发部', '项目部']
    },
    series: [{
            name: '发布工时',
            type: 'bar',
            //图形样式
            itemStyle: {
                color: 'rgba(102, 168, 255, 0.8)',
                borderColor: '#66a8ff',
            },
            barWidth: '16',
            label: {
                show: true,
                position: 'right',
                color: '#666',
            },
            data: [243, 1405, 3205.5]
        },
        {
            name: '新增工时',
            type: 'bar',
            //图形样式
            itemStyle: {
                color: 'rgba(96, 204, 82, 0.8)',
                borderColor: '#60cc52',
            },
            barWidth: '16',
            label: {
                show: true,
                position: 'right',
                color: '#666',
            },
            data: [283, 1005.5, 2205.5]
        },
        {
            name: '累计剩余工时',
            type: 'bar',
            //图形样式
            itemStyle: {
                color: 'rgba(255, 97, 97, 0.8)',
                borderColor: '#ff6161',
            },
            barWidth: '16',
            label: {
                show: true,
                position: 'right',
                color: '#666',
            },
            data: [324, 1605, 3605]
        }
    ]
};



BarChart.setOption(option3_2);








var BarChart = echarts.init(document.getElementById("demo5_1"));

var option5_1 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} %',
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
        formatter: '{b0}<br>{a0}：{c0}%<br>{a1}：{c1}%<br>{a2}：{c2}%<br>{a3}：{c3}%<br>{a4}：{c4}%',
        padding: [5, 10],
        backgroundColor: 'rgba(102, 168, 255, 0.8)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        }
    },
    title: false,
    grid: {
        show: false,
        top: 40,
        right: '0%',
        bottom: '18%',
        left: '6%',
    },
    // toolbox: {
    //     right:'10%',
    //     top:0,
    //     feature: {
    //         dataView: {show: false, readOnly: false},
    //         magicType: {show: true, type: ['line', 'bar']},
    //         restore: {show: true},
    //         saveAsImage: {show: false}
    //     },
    // },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: -5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['销售一区（游云飞）', '销售四区（程保针）', '项目部', '销售二区（陈金双）', '开发部'],
    },
    xAxis: {
        name: false,
        // nameTextStyle: {
        //     color: '#668aff',
        // },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: [{
        type: 'value',
        name: false,
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
    dataZoom: [{
        type: 'slider',
        height: 16,
        bottom: 2,
        start: 50,
        end: 100,
        backgroundColor: 'rgba(102,168,255,.2)',
        dataBackground: {
            areaStyle: {
                color: 'rgba(102,168,255,.2)',
            },
        },
        fillerColor: 'rgba(167,183,204,0.5)',
        borderColor: 'rgba(102,168,255,.05)',
        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
        handleSize: '110%',
        handleStyle: {
            color: 'rgba(102,168,255,.5)',

        },
        textStyle: {
            color: "rgba(167,183,204,0.9)"
        },
    }],
    series: [{
            name: '销售一区（游云飞）',
            type: 'bar',
            symbolSize: 8,
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式

            itemStyle: {
                color: '#ff6161',
                borderColor: '#ff6161',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#ff6161',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [887, 734, 680, 529, 395, 467, 444, 272, 95, 167, 444, 272]

        },
        {
            name: '销售四区（程保针）',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#ffc066',
                borderColor: '#ffc066',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#ffc066',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [676, 320, 353, 716, 513, 1097, 1025, 1316, 213, 397, 824, 416],

        },
        {
            name: '项目部',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#66a8ff',
                borderColor: '#66a8ff',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#66a8ff',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [78, 185, 313, 268, 478, 322, 472, 185, 413, 68, 23, 52],

        },
        {
            name: '销售二区（陈金双）',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#60cc52',
                borderColor: '#60cc52',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#60cc52',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [23, 52, 102, 472, 85, 413, 68, 78, 185, 313, 268, 378],

        },
        {
            name: '开发部',
            type: 'bar',

            //图形样式
            itemStyle: {
                color: '#9488eb',
                borderColor: '#9488eb',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#9488eb',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [23, 52, 102, 472, 85, 413, 68, 78, 185, 313, 268, 378],

        }
    ]
};

BarChart.setOption(option5_1);



var BarChart = echarts.init(document.getElementById("demo5_2"));
var option5_2 = {
    tooltip: {
        trigger: 'axis',

        // position: function (pt) {
        //     return [pt[0], '10%'];
        // },
        axisPointer: {
            type: 'shadow',
            label: {
                show: true,
                formatter: ' {value} %',
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
        formatter: '{b0}<br>{a0}：{c0}%<br>{a1}：{c1}%<br>{a2}：{c2}%<br>{a3}：{c3}%<br>{a4}：{c4}%',
        padding: [5, 10],
        backgroundColor: 'rgba(102, 168, 255, 0.8)',
        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
        textStyle: {
            color: '#fff',
        }
    },
    title: false,
    grid: {
        show: false,
        top: 40,
        right: '0%',
        bottom: '18%',
        left: '6%',
    },
    // toolbox: {
    //     right:'10%',
    //     top:0,
    //     feature: {
    //         dataView: {show: false, readOnly: false},
    //         magicType: {show: true, type: ['line', 'bar']},
    //         restore: {show: true},
    //         saveAsImage: {show: false}
    //     },
    // },
    legend: {
        show: true,
        textStyle: {
            color: '#666'
        },
        top: -5,
        itemGap: 40,
        itemWidth: 18,
        inactiveColor: 'rgba(0, 0, 0, 0.3)',
        data: ['项目二组（丁世瑜）',
            '项目三组（张智华）', '项目一组（黄志远）', '项目五组（潘刘永）', '项目四组（谢求生）'
        ],
    },
    xAxis: {
        name: false,
        // nameTextStyle: {
        //     color: '#668aff',
        // },
        nameLocation: 'end',
        nameGap: '2',
        boundaryGap: ['10%', '10%'], //坐标轴两边留白
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
        },
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: [{
        type: 'value',
        name: false,
        nameTextStyle: {
            color: '#999',
        },
        nameGap: '14',
        // max: '3000',
        splitNumber: 5,
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
    dataZoom: [{
        type: 'slider',
        height: 16,
        bottom: 2,
        start: 0,
        end: 60,
        backgroundColor: 'rgba(102,168,255,.2)',
        dataBackground: {
            areaStyle: {
                color: 'rgba(102,168,255,.2)',
            },
        },
        fillerColor: 'rgba(167,183,204,0.5)',
        borderColor: 'rgba(102,168,255,.05)',
        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
        handleSize: '110%',
        handleStyle: {
            color: 'rgba(102,168,255,.5)',

        },
        textStyle: {
            color: "rgba(167,183,204,0.9)"
        },
    }],
    series: [{
            name: '项目二组（丁世瑜）',
            type: 'bar',
            symbolSize: 8,
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式

            itemStyle: {
                color: '#c0f449',
                borderColor: '#c0f449',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#c0f449',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [887, 734, 680, 529, 395, 467, 444, 272, 95, 167, 444, 272]

        },

        {
            name: '项目三组（张智华）',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#9fc522',
                borderColor: '#9fc522',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#9fc522',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [676, 320, 353, 716, 513, 1097, 1025, 1316, 213, 397, 824, 416],

        },
        {
            name: '项目一组（黄志远）',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#679528',
                borderColor: '#679528',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#679528',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [78, 185, 313, 268, 478, 322, 472, 185, 413, 68, 23, 52],

        },
        {
            name: '项目五组（潘刘永）',
            type: 'bar',
            // label:{
            //     show: true,
            //     color: '#666',
            //     position: 'top',
            //     distance: 10,
            // },
            //图形样式
            itemStyle: {
                color: '#58852a',
                borderColor: '#58852a',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#58852a',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [23, 52, 102, 472, 85, 413, 68, 78, 185, 313, 268, 378],

        },
        {
            name: '项目四组（谢求生）',
            type: 'bar',

            //图形样式
            itemStyle: {
                color: '#4c7123',
                borderColor: '#4c7123',
            },

            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    color: '#4c7123',
                    position: 'top',
                    distance: 10,
                },
            },
            data: [23, 52, 102, 472, 85, 413, 68, 78, 185, 313, 268, 378],

        }
    ]
};

BarChart.setOption(option5_2);