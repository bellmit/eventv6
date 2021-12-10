//复选框的选中与未选中的切换
$('.enterprise-row ul li').click(function(){
	var i = $(this).index();
	if(!$(this).find('input').prop('checked')){
		$(this).find('input').prop('checked',true);
		$(this).find('.enterprise-box').addClass('enterprise-active');
		$(this).find('.checkbox>img').attr('src','style/images/icon-gou.png');
	}else{
		$(this).find('input').prop('checked',false);
		$(this).find('.enterprise-box').removeClass('enterprise-active');
		$(this).find('.checkbox>img').attr('src','style/images/icon-kuan.png');
	}
})
//关闭弹窗
$('.add').click(function(){
	$('.mask1').show();
})
$('.btn-bon').click(function(){
	$('.mask1').hide();
})
//动态生成li
var enterArr = $('.enterprise-row ul li');	
$('.sure').on('click', function(){
	for(var i=0; i<enterArr.length; i++){
		if(enterArr.eq(i).find('input').prop('checked')){
			$('.associated-list').prepend(
				'<li>'+
					'<a href="#" class="delete">'+
						'<img src="style/images/icon-close.png"/>'+
					'</a>'+
					'<div class="enterprise-box ">'+
						'<div class="enterprise-icon">'+
							'<img src='+enterArr.eq(i).find('.enterprise-icon>img').attr('src')+'>'+
						'</div>'+
						'<div class="enterprise-name flex flex_jc mt10">'+
							'<p>'+enterArr.eq(i).find('.enterprise-name>p').text()+'</p>'+
							'<span>'+enterArr.eq(i).find('.enterprise-name>span').text()+'</span>'+
						'</div>'+
					'</div>'+
				'</li>'
			)
		}
	}
	
});
//动态生成li的删除
$('.enterprise-box1').on('click','.delete',function(){
	$('.enterprise-box1 li>div.enterprise-box').parent().remove();
})
//	下拉框
$('.kh_select').on('click', function(e){
	$(this).find('img').css({'WebkitTransform': 'rotate(-180deg)', 'MozTransform': 'rotate(-180deg)', 'MsTransform': 'rotate(-180deg)', 'transform': 'rotate(-180deg)'})
	$(this).find('.tools_items').fadeIn(200);
	e.stopPropagation();
	$('body').on('click', function(){
		$('.kh_select').find('img').css({'WebkitTransform': 'rotate(0deg)', 'MozTransform': 'rotate(0deg)', 'MsTransform': 'rotate(0deg)', 'transform': 'rotate(0deg)'})
		$('.tools_items').fadeOut(200);
	});
});
$('.tools_items>li').on('click', function(e){
	$(this).parent().parent().find('img').css({'WebkitTransform': 'rotate(0deg)', 'MozTransform': 'rotate(0deg)', 'MsTransform': 'rotate(0deg)', 'transform': 'rotate(0deg)'})
	$(this).parent().fadeOut(200);
	e.stopPropagation();
});

//弹窗2的显示与隐藏
$('.enterprise-box1 ').on('click','li>div.enterprise-box',function(){
	$(this).addClass('enterprise-active');
	$('.delete').hide();
	$('.mask2').show();
})
$('.btn-bon').click(function(){
	$('.enterprise-box1 li>div.enterprise-box').removeClass('enterprise-active');
	$('.delete').show();
	$('.mask2').hide();
})
	