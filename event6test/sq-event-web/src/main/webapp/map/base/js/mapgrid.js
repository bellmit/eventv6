//目录的变化
$('.level-content ul li').click(function(){
    var i = $(this).index();
    $(this).parent().parent().parent().hide();
    $(this).parent().parent().parent().siblings('.right-content-second').show();
    $('.second').show();
    $('.back').addClass('color-blue');
    $(".rc-content-box").getNiceScroll().show();
    $(".rc-content-box").getNiceScroll().resize();
//详情的滚动条
    $('.rc-content-box').niceScroll({
        cursorcolor:"rgba(0, 0, 0, 0.3)",
        cursoropacitymax:1,
        touchbehavior:false,
        cursorwidth:"4px",
        cursorborder:"0",
        cursorborderradius:"4px"
    });
});
function backLevel(){
    $('.right-content-first').show();
    $('.right-content-second').hide();
    $('.second').hide();
    $(".rc-content-box").getNiceScroll().hide();
    $('.back').removeClass('color-blue');
}
$('.back').click(backLevel);
//右边内容的弹出与隐藏
//右边显示的函数
var zhXc = js_ctx + '/map/base/images/'
function rightContentHide(){
    $('.right-content-switch').removeClass('right-content-switch1').addClass('right-content-switch2');
    $('.right-content-switch').find('img').prop('src',zhXc+'icon-to-left.png');
    $('.right-content').animate({'right': '60px'},function(){
        $('.right-tital').addClass('right-tital-boxshow2');
        $(".rc-content-box").getNiceScroll().show();
        $(".rc-content-box").getNiceScroll().resize();
        //详情的滚动条
        $('.rc-content-box').niceScroll({
            cursorcolor:"rgba(0, 0, 0, 0.3)",
            cursoropacitymax:1,
            touchbehavior:false,
            cursorwidth:"4px",
            cursorborder:"0",
            cursorborderradius:"4px"
        });
        //人员的滚动条
        $(".level-content").getNiceScroll().show();
        $(".level-content").getNiceScroll().resize();
        $('.level-content').niceScroll({
            cursorcolor:"rgba(0, 0, 0, 0.3)",
            cursoropacitymax:1,
            touchbehavior:false,
            cursorwidth:"4px",
            cursorborder:"0",
            cursorborderradius:"4px"
        });
    });
}
//右边显示的代码
var rTitle;
$('.right-tital-items').click(function(){
    $('.right-tital-items').find('.rt-items-icon').removeClass('rt-items-active');
    $(this).find('.rt-items-icon').addClass('rt-items-active');
    rTitle = $(this).prop('title');
    switch (rTitle){
        case '人':
            $('.right-content').hide();
            $('.rc-local-search-people').show();
            break;
        case '地':
            $('.right-content').hide();
            $('.rc-local-search-place').show();
            break;
        case '事':
            $('.right-content').hide();
            $('.rc-local-search-event').show();
            break;
        case '物':
            $('.right-content').hide();
            $('.rc-local-search-things').show();
            break;
        case '组织':
            $('.right-content').hide();
            $('.rc-local-search-organization').show();
            break;
        default:
            break;
    }
    rightContentHide();
});
$('.search-box').click(function(){
    $('.right-content').hide();
    $('.rc-local-search').show();
    rightContentHide();
})
//右边隐藏的代码
$('.right-content-switch').click(function(){
    if($(this).hasClass('right-content-switch2')){
        $('.right-content').animate({'right': '-230px'},function(){
            $('.right-tital').removeClass('right-tital-boxshow2');
            $(".rc-content-box").getNiceScroll().hide();
            $(".level-content").getNiceScroll().hide();
            $('.rt-items-icon').removeClass('rt-items-active');
        });
        $(this).removeClass('right-content-switch2').addClass('right-content-switch1');
        $(this).find('img').prop('src',zhXc+'icon-to-right.png');
    }else if($(this).hasClass('right-content-switch1')){
        rightContentHide();
        $(this).removeClass('right-content-switch1').addClass('right-content-switch2');
        $(this).find('img').prop('src',zhXc+'icon-to-left.png');
    }
})
//清空搜索内容
$('.close').click(function(){
    $(this).parent().siblings('.right-content-second').find('.rc-content').hide();
    $(this).parent().siblings('.right-content-second').addClass('no-content');
    $(".rc-content-box").getNiceScroll().hide();
})
//下拉选择框的控制代码
$('.dd-content').click(function(){
    $('.drop-down').toggle();
    $('.drop-down').css({'top':'36px'});
    $('.drop-down').animate({'top':'26px'});
    return false;
})
var optionText;
$('.dd-options').on('click','ul li a', function(){
    optionText = $(this).text();
    $('.dd-content').find('p').text(optionText);
});
//取消下拉选择框的代码
$(document).click(function(){
    $('.drop-down').hide();
    $(".dd-secondary-optios-list").getNiceScroll().hide();
})
//二级联动的列表
var that,optionsText;
$('.dd-options').on('mouseover','ul li a',function(){
    $('.dd-options').find('a').removeClass('active');
    $(this).addClass('active');
    $that = $(this);
    $('.dd-secondary-optios').on('mouseover',function(){
        $that.addClass('active');
    })
    $('.dd-secondary-optios-list').on('click','ul li a',function(){
        optionsText = $that.text();
        $('.dd-content').find('p').text(optionsText);
    })
    switch ($(this).text()){
        case '全部':
            $('.drop-down').css({'width': '60px'});
            $('.dd-secondary-optios').hide();
            break;
        case '人':
            secondaryLinkage($('.dd-secondary-optios-people'),'dds-optios-people');
            break;
        case '地':
            secondaryLinkage($('.dd-secondary-optios-place'),'dds-optios-place');
            break;
        case '事':
            secondaryLinkage($('.dd-secondary-optios-event'),'dds-optios-event');
            break;
        case '物':
            secondaryLinkage($('.dd-secondary-optios-things'),'dds-optios-things');
            break;
        case '组织':
            secondaryLinkage($('.dd-secondary-optios-organization'),'dds-optios-organization');
            break;
    }
})
function secondaryLinkage(item,item1){
    $(".dd-secondary-optios-list").hide();
    $(".dd-secondary-optios-list").getNiceScroll().hide();
    $('.drop-down').css({'width': '244px'});
    $('.dd-secondary-optios').show();
    item.show();
    $('.dd-secondary-optios').removeClass('dds-optios-organization dds-optios-things dds-optios-event dds-optios-place dds-optios-people').addClass(item1);
    item.getNiceScroll().show();
    item.getNiceScroll().resize();
    item.niceScroll({
        cursorcolor:"rgba(0, 0, 0, 0.3)",
        cursoropacitymax:1,
        touchbehavior:false,
        cursorwidth:"4px",
        cursorborder:"0",
        cursorborderradius:"4px",
        autohidemode: false
    });
}
//地区树的滚动条
$(function(){
    $('.left-ztree>.ztree').niceScroll({
        cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", // 游标边框css定义
        cursorborderradius: "4px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });
    $('.ztree li').on('click', function(){
        setTimeout(function(){$(".left-ztree>.ztree").getNiceScroll().resize();}, 280);
    });
});
//地区树的显示与隐藏
$('.address-sel').on('click',function(){
    $('.left-ztree').toggle();
    return false;
})
$('.left-ztree').click(function(){
    return false;
})
$(document).click(function(){
    $('.left-ztree').hide();
    $('.outline-list').hide();
})
//轮廓的显示与隐藏
var outlineWidth;
$('.outline').on('click',function(){
    outlineWidth = $(this).width();
    $('.outline-list').toggle();
    $('.outline-list').css({'width': outlineWidth})
    return false;
})
var outlineText;
// $('.outline-list').on('click','li a',function(){
//     outlineText = $(this).text();
//     $(this).parent().parent().siblings('.outline').find('p').text(outlineText);
// })

