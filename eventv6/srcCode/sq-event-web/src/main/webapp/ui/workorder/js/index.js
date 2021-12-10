
$(function(){
//	计算container的高
	$(window).on('load resize', function(){
		var winH = $(window).height(), headerH = $('.header').height(), navH = $('.nav_con').height();
		$('.container').height(winH - (headerH + navH))
	});
	
//	nav
	var $nav = $('.nav_con li a');
	$nav.on('click', function(){
		$('.nav_con li').find('.active').removeClass('active');
		$(this).addClass('active');
	});
	
//	index.html nav
	$('.ace-nav li a').on('click', function(){
		$(this).not('.dropdown-toggle').addClass('active').parent().siblings().children('a').removeClass('active');
		$('.ace-nav>li').each(function(index, element){
			if($(this).children().hasClass('active')){
				return;
			}else{
				$(this).find('.nav_pic>img').attr('src', 'images/icon_nav_'+(index + 1)+'_1.png');
			}
		})
		$(this).find('.nav_pic>img').attr('src', 'images/icon_nav_'+($(this).parent().index()+1)+'_2.png');
	});
	
//	sidebar
	var $arrLi = $('.nav-list li .submenu li');
	$('.submenu li.on').parentsUntil('.open').siblings('.dropdown-toggle').css('background', '#f4f6f9').addClass('menu-select');
	$arrLi.on('click', function(e){
		$('.submenu li.on').removeClass('on');
		$('.menu-select').css('background', '#fff').removeClass('menu-select');
		$(this).addClass('on').parentsUntil('.open').siblings('.dropdown-toggle').css('background', '#f4f6f9').addClass('menu-select');
		e.stopPropagation();
	});
	
	$('.nav-list').height($(window).height() - 142);
	
	$('.nav-list>li>a, .nav-list>li>a+.submenu').on('mouseenter',function(){
		if($(this).parentsUntil('.main-container-inner').hasClass('menu-min')){
			if($(this).siblings().children().hasClass('on')){
				return;
			}else{
				$(this).parentsUntil('.nav-list').addClass('listHover');
			}
		}
	});
	$('.nav-list>li>a, .nav-list>li>a+.submenu').on('mouseleave',function(){
		if($(this).parentsUntil('.main-container-inner').hasClass('menu-min')){
			if($(this).siblings().children().hasClass('on')){
				return;
			}else{
				$(this).parentsUntil('.nav-list').removeClass('listHover');
			}
		}
	});
	
	$('.nav-list .submenu').on('click', 'li', function(){
		$('.nav-list .submenu').hide();
		$(this).parent().parent().addClass('open').siblings().removeClass('open');
	});
	$('.nav-list>li>a').on('click', function(){
		setTimeout(function(){$(".nav-list").getNiceScroll().resize();}, 280);
		if($(this).parentsUntil('.main-container-inner').hasClass('menu-min')){
			return false;
		}
		for(var i=0; i<$('.nav-list>li').length; i++){
			if($('.nav-list>li').eq(i).children('.submenu').children().hasClass('on')){
				$(this).find('.dropdown-toggle').css('background', '#f4f6f9');
			}else{
				$('.nav-list>li').eq(i).find('.dropdown-toggle').css('background', '#fff');
			}
		}
		if($(this).siblings().children().hasClass('on') || !$(this).parent().hasClass('open')){
			$(this).css('background', '#f4f6f9');
		}else{
			$(this).css('background', '#fff');
		}
	});
	
	$('.sidebar-collapse').on('click', function(){
		$(".nav-list").getNiceScroll().resize();
		if($(this).parent().hasClass('menu-min')){
			$(".nav-list").css('overflow', 'visible');
		}else{
			$(".nav-list").css('overflow', 'hidden');
		}
		for(var i=0; i<$('.nav-list>li').length; i++){
			if($('.nav-list>li').eq(i).children('.submenu').children().hasClass('on')){
				$('.nav-list>li').eq(i).addClass('open').find('.dropdown-toggle').css('background', '#f4f6f9').siblings('.submenu').css('display', 'block');
			}else{
				$('.nav-list>li').eq(i).removeClass('open').find('.dropdown-toggle').css('background', '#fff').siblings().css('display', 'none');
			}
		}
	});
	
	$('.nav-list>li>a').hover(function(){
		$(this).addClass('backOn');
	}, function(){
		$(this).removeClass('backOn');
	});

})








