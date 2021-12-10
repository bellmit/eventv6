$('.add').each(function (index) {

	$(this).click(function() {
	    $('.table-detail-wrap').eq(index).show().siblings('.table-detail-wrap').hide();
	    $('.table-detail-wrap').eq(index).siblings().find('.add').removeClass('disappear');
	    $('.table-detail-wrap').eq(index).siblings().find('.reduce').addClass('disappear');
		$('.add').eq(index).addClass('disappear');
		$('.reduce').eq(index).removeClass('disappear');
		$('.table-wrapper').scrollTop(index * 40 + 102);
		$('.table-list-tr').eq(index).addClass('tr-sec-bg');
		$('.table-list-tr').eq(index).siblings().removeClass('tr-sec-bg');
		$(".table-box").getNiceScroll().resize();
	})
})

$(function(){
	$('.table-box').niceScroll({
	    cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
	    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
	    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
	    cursorwidth: "5px", //像素光标的宽度
	    cursorborder: "0", // 游标边框css定义
	    cursorborderradius: "5px",//以像素为光标边界半径
	    autohidemode: false //是否隐藏滚动条
	});
});

$('.reduce').each(function(index) {
	$(this).click(function() {
	    $('.table-detail-wrap').eq(index).hide();
		$('.reduce').eq(index).addClass('disappear');
		$('.add').eq(index).removeClass('disappear');
		$('.table-list-tr').eq(index).removeClass('tr-sec-bg');
		$(".table-box").getNiceScroll().resize();
	})
})

$('.input-check').each(function(i) {
	$('.input-check').eq(i).attr('id', i);
	$('label').eq(i).attr('for', i);
})

$(function(){
	
});







