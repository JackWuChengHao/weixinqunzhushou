/**
 * 
 */
$(document).ready(function(){
	initcahrts();
});


function initcahrts(){
	var myChart = echarts.init(document.getElementById('datacharts'));
	
	option = {
		    title: {
		        text: '趋势图'
		    },
		    xAxis: {
		        type: 'category',
		        data: ['02-22', '02-23', '02-24', '02-25', '02-26', '02-27', '02-28']
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [{
		        data: [10, 15, 19, 22, 22, 22, 22],
		        type: 'line'
		    }]
		};
		
	myChart.setOption(option);
	
	
}

