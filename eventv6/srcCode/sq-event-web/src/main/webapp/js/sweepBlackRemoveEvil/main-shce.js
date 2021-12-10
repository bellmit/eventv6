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