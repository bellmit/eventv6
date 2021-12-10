$(function(){
	
	var picW, picCW, init, $spdO = 3000, $spdT = 3000; //$spdO为轮播时间间隔，$stdT为点击缩略图和分页后再次轮播的时间间隔
	$('.pic_cont>li').eq(0).css({'filter': 'Alpha(opacity=100)', 'opacity': '1'});
	
	$('.table-list>tbody>tr>td:first-child .s-pic-show').on('click', function () {
		$('.layer_wrap').css('visibility', 'visible');
		$('.pic_cont>li').eq(0).css({'filter': 'Alpha(opacity=100)', 'opacity': '1'}).addClass('active').siblings().removeClass('active');
		$('.pic_pag_cont>li').eq(0).addClass('active').siblings().removeClass('active');
		$('.pic_pag_cont').css({'left': 0});
		init = setInterval(autoP, $spdO);
		return $count = 0;
	});
	$('.layer_close').on('click', function(){
		$('.layer_wrap').css('visibility', 'hidden');
		clearInterval(init);
		clearTimeout(inits);
	});
	
	var $pagW, $pagPW, $pagN, $pagL, $picI, $picPI, $pagCW;
	$(window).on('load resize', function(){
		$pagW = $('.pic_pag_con').width(); //获取外层div宽度
		$pagN = $('.pic_pag_cont>li').length; //获取图片数量
		$pagPW = $pagW * .188; //计算图片宽度
		$pagL = $pagW * .015; //计算图片间距
		$pagCW = $pagN * $pagPW + $pagL * ($pagN-1); //计算整个缩略图框的宽度
		$('.pic_pag_cont>li').width($pagPW); 
		$('.pic_pag_cont').width($pagCW);
		$('.pic_pag_cont>li + li').css('margin-left', $pagL);
		if($pagN < 6){
			$('.pic_pag>.pic_btn').hide();
			$('.pic_pag_con').addClass('pag_flex');
			$('.pic_pag_cont').removeClass('pag_posi');
		}
	});
	
//	点击缩略图切换图片
	$('.pic_pag_cont>li').on('click', function(){
		$picPI = $('.pic_cont').find('.active').index(); //获取当前显示图片的索引
		$picI = $(this).index(); //设置要显示的图片的索引
		swiper($picPI, $picI);
	});
	function swiper(vul1, vul2){
		clearInterval(init);
		clearTimeout(inits);
		if(vul1 != vul2){
			$('.pic_pag_cont>li').eq(vul2).addClass('active').siblings().removeClass('active'); //点击缩略图添加边框
			$('.pic_cont>li').eq(vul2).addClass('active').animate({'filter': 'Alpha(opacity=100)', 'opacity': '1'}, 200); //显示的图片淡入
			$('.pic_cont>li').eq(vul1).animate({'filter': 'Alpha(opacity=0)', 'opacity': '0'}, 100, function(){$(this).removeClass('active')}); // 当前图片淡出
			picW = $('.pic_cont>li').eq(vul2).find('img').width(), picCW = $('.pic_cont').width();
			if(picW > picCW){ //ie兼容
				$('.pic_cont>li').eq(vul2).find('img').width(picW);
			}
		}
		inits = setTimeout(function(){init = setInterval(autoP, $spdO)}, $spdT);
	}
	
//	点击按钮换页
	var $pagCL,  $count = 0, inits;
	$('.pag_next').on('click', function(){
		clearInterval(init);
		clearTimeout(inits);
		$pagCL = $('.pic_pag_cont').position().left;
		if(($pagCW + $pagCL - $pagW) < $pagW){
			$('.pic_pag_cont').animate({'left': -($pagCW - $pagW)}, 500);
		}else{
			$count ++;
			$('.pic_pag_cont').animate({'left': -($pagW * $count + $pagL * $count)}, 500);
		}
		inits = setTimeout(function(){init = setInterval(autoP, $spdO)}, $spdT);
		return $count;
	});
	$('.pag_prev').on('click', function(){
		clearInterval(init);
		clearTimeout(inits);
		$pagCL = $('.pic_pag_cont').position().left;
		if(($pagCL) > -$pagW){
			$('.pic_pag_cont').animate({'left': 0}, 500);
		}else{
			$('.pic_pag_cont').animate({'left': $pagCL + $pagW + $pagL}, 500);
			$count --;
		}
		inits = setTimeout(function(){init = setInterval(autoP, $spdO)}, $spdT);
		return $count;
	});
	
//	自动轮播
	var $apI, auCount;
	function autoP(){
		$picPI = $('.pic_cont').find('.active').index(); //获取当前显示图片的索引
		if($picPI == $pagN - 1){
			$picPI = -1;
		}
		$apI = $picPI + 1;
		auCount = Math.floor($apI/5);
		autoPlay($picPI, $apI, auCount);
	}
	function autoPlay(vul1, vul2, vul3){
		if($pagN === 1){
			return false;
		}
		if($pagN > 5){
			$('.pic_pag_cont').animate({'left': -($pagW * vul3 + $pagL * vul3)}, 500);
		}
		$('.pic_pag_cont>li').eq(vul2).addClass('active').siblings().removeClass('active'); //点击缩略图添加边框
		$('.pic_cont>li').eq(vul2).addClass('active').animate({'filter': 'Alpha(opacity=100)', 'opacity': '1'}, 400); //显示的图片淡入
		$('.pic_cont>li').eq(vul1).animate({'filter': 'Alpha(opacity=0)', 'opacity': '0'}, 300, function(){$(this).removeClass('active')}); // 当前图片淡出
		picW = $('.pic_cont>li').eq(vul2).find('img').width(), picCW = $('.pic_cont').width();
		if(picW > picCW){ //ie兼容
			$('.pic_cont>li').eq(vul2).find('img').width(picW)
		}
		return $count = vul3;
	}
	
});






