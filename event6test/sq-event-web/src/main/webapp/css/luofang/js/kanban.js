
$(function(){
//	gdp
	var $count = 46.8;
    var $num = $count.toString();
    for(var i=0; i<$num.length; i++){
        $('#gdp_count').append('<i>'+$num[i]+'</i>');  //	将数字拆分，插入dom
    }
    
//			    tools
	$('.aside_month').on('click', function(e){
		$('.tools_items').fadeIn(200);
		e.stopPropagation();
		$('body').on('click', function(){
			$('.tools_items').fadeOut(200);
		});
	});
	$('.tools_items li').on('click', function(e){
		$('.tools_items').fadeOut(200);
		e.stopPropagation();
	});
	
	var $topTable = $('#j_table_top'), times = 0, $topTableH = $('#j_table_top>table').height(), $trH = $('#j_table_top>table>tbody>tr').height();
	if($('#j_table_top>table>tbody>tr').length > 6){
		$topTable.append($('#j_table_top>table').clone());
		if($topTable.find('table:first-child tr').length%2 == 1){
			$topTable.find('table').eq(1).find('tr:even').css({'background': 'transparent'})
			$topTable.find('table').eq(1).find('tr:odd').css({'background': 'rgba(128, 204, 255, .15)'})
		}
		var initT = setInterval(dynamicT, 2000);
		function dynamicT(){
			var $scrollH = parseInt($topTable.css('margin-top'));
			if($scrollH <= -$topTableH){
				$topTable.css({'margin-top': 0});
				times = 0;
			}
			++times;
			$topTable.animate({'margin-top': -$trH*times}, 200)
		}
		$topTable.hover(function(){
			clearInterval(initT);
		},function(){
			initT = setInterval(dynamicT, 2000);
		});	
	}
	
//	contain_tools_items
	
	var $navRk = $('.contain_tools_cont>li'), $navCRk = $('.contain_tools_sec>li'), $tIndex = 0, $tIndex = 0, $tList = ['产业罗坊', '宜居罗坊', '人口罗坊'];;
	var initRk = setInterval(autoRk, 10000);
	$('.contain_tools_sec>li:first-child').css({'opacity': '1', 'filter': 'Alpha(opacity=100)'});
	
	function autoRk(){
		$tIndex = $('.contain_tools_cont').find('.active').index();
		if($tIndex < $navRk.length-1){
			$tIndex++;
		}else{
			$tIndex = 0;
		}
		$('.wrap_contain>.aside_box_header>h5').html($tList[$tIndex]);
		$navRk.eq($tIndex).addClass('active').siblings().removeClass('active');
		$navCRk.eq($tIndex).addClass('active').animate({'opacity': '1', 'filter': 'Alpha(opacity=100)'}, 200).siblings().removeClass('active').animate({'opacity': '0', 'filter': 'Alpha(opacity=0)'}, 300);
	}
	$('.contain_tools_cont>li').on('click', function(){
		$tIndex = $(this).index();
		$('.wrap_contain>.aside_box_header>h5').html($tList[$tIndex]);
		$navCRk.eq($tIndex).addClass('active').animate({'opacity': '1', 'filter': 'Alpha(opacity=100)'}, 200).siblings().removeClass('active').animate({'opacity': '0', 'filter': 'Alpha(opacity=0)'}, 300);
		$(this).addClass('active').siblings().removeClass('active');
	});
	$('.contain_tools_sec').hover(function(){
		clearInterval(initRk);
	}, function(){
		initRk = setInterval(autoRk, 10000);
	});
	
});








