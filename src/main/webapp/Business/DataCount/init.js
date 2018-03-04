/**
 * 
 */
$(document).ready(function(){
	chatTimecount();
	radarChart();
	cakeChart();
	membersChange();
});




function chatTimecount(){
	var dom = document.getElementById("chatTime");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	app.title = '群消息活跃时段统计';

	option = {
		title: {
	        text: '群消息活跃时段统计'
	    },
	    angleAxis: {
	        type: 'category',
	        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
	        z: 10
	    },
	    radiusAxis: {
	    },
	    polar: {
	    },
	    series: [{
	        type: 'bar',
	        data: [1, 2, 3, 4, 3, 5, 1],
	        coordinateSystem: 'polar',
	        name: 'A',
	        stack: 'a'
	    }, {
	        type: 'bar',
	        data: [2, 4, 6, 1, 3, 2, 1],
	        coordinateSystem: 'polar',
	        name: 'B',
	        stack: 'a'
	    }, {
	        type: 'bar',
	        data: [1, 2, 3, 4, 1, 2, 5],
	        coordinateSystem: 'polar',
	        name: 'C',
	        stack: 'a'
	    }],
	    legend: {
	        show: true,
	        data: ['上午', '下午', '夜间']
	    }
	};
	;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}

function radarChart(){
	var dom = document.getElementById("radarChart");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
	    title: {
	        text: '热门话题分布'
	    },
	    tooltip: {},
	    legend: {
	        data: ['预算分配（Allocated Budget）', '实际开销（Actual Spending）']
	    },
	    radar: {
	        // shape: 'circle',
	        name: {
	            textStyle: {
	                color: '#fff',
	                backgroundColor: '#999',
	                borderRadius: 3,
	                padding: [3, 5]
	           }
	        },
	        indicator: [
	            { name: '财产', max: 6500},
	            { name: '民事纠纷', max: 16000},
	            { name: '劳务问题', max: 30000},
	            { name: '婚姻', max: 38000},
	            { name: '房产', max: 52000},
	            { name: '零售业', max: 25000}
	         ]
	     },
	     series: [{
	         name: '男 vs 女（Budget vs spending）',
	         type: 'radar',
	         // areaStyle: {normal: {}},
	         data : [
	             {
	                 value : [4300, 10000, 28000, 35000, 50000, 19000],
	                 name : '男（Allocated Budget）'
	             },
	              {
	                 value : [5000, 14000, 28000, 31000, 42000, 21000],
	                 name : '女（Actual Spending）'
	             }
	         ]
	     }]
	 };;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}

function cakeChart(){
	var dom = document.getElementById("popgroupChart");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
	    backgroundColor: '#2c343c',

	    title: {
	        text: '活跃群数量统计',
	        left: 'center',
	        top: 20,
	        textStyle: {
	            color: '#ccc'
	        }
	    },

	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },

	    visualMap: {
	        show: false,
	        min: 80,
	        max: 600,
	        inRange: {
	            colorLightness: [0, 1]
	        }
	    },
	    series : [
	        {
	            name:'每日信息数量',
	            type:'pie',
	            radius : '55%',
	            center: ['50%', '50%'],
	            data:[
	                {value:335, name:'1500~2000条'},
	                {value:310, name:'1000~1500条'},
	                {value:274, name:'500~1000条'},
	                {value:235, name:'<500条'},
	                {value:400, name:'>2000条'}
	            ].sort(function (a, b) { return a.value - b.value; }),
	            roseType: 'radius',
	            label: {
	                normal: {
	                    textStyle: {
	                        color: 'rgba(255, 255, 255, 0.3)'
	                    }
	                }
	            },
	            labelLine: {
	                normal: {
	                    lineStyle: {
	                        color: 'rgba(255, 255, 255, 0.3)'
	                    },
	                    smooth: 0.2,
	                    length: 10,
	                    length2: 20
	                }
	            },
	            itemStyle: {
	                normal: {
	                    color: '#c23531',
	                    shadowBlur: 200,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            },

	            animationType: 'scale',
	            animationEasing: 'elasticOut',
	            animationDelay: function (idx) {
	                return Math.random() * 200;
	            }
	        }
	    ]
	};;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}

function membersChange(){
	var dom = document.getElementById("datacounttable");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
	    title: {
	        text: '近一周群总人数变化'
	    },
	    xAxis: {
	        type: 'category',
	        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [{
	        data: [1820, 1932, 1901, 1934, 2290, 2330, 2320],
	        type: 'line'
	    }]
	};
	;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}