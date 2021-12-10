//tab-hk 的切换
$(function () {
   var $index;
$('.tab-hk li').on('click',function () {
	$(this).addClass('active').siblings().removeClass('active');
        $index = $(this).index();
        $('.barline').animate({'left': $index * $('.tab-hk li').width() }, 300);
    })
})
//多条tab（li）的js写法
// $(function () {
//     var $index, tabLi = $('.tab-hk li').length - 1;
//     $('.tab-hk li').width($('.tab-hk').width()/tabLi);
//     $('.tab-hk li').on('click',function () {
//         $(this).addClass('active').siblings().removeClass('active');
//         $index = $(this).index();
//         $('.barline').animate({'left': $index * $('.tab-hk li').width() }, 300);
//     })
// })


//	下拉框
$('.fw-xw-form .form').on('click', function(e){
    $(this).siblings('.tools-items').fadeIn(200).width($(this).siblings('.form').width());
    e.stopPropagation();
});
$('.tools-items>li').on('click', function(e){
    $(this).parent().fadeOut(200);
});
$('body').on('click', function(e){
    $('.tools-items').fadeOut(200);
});