window.onload = function(){
/*-------------------------------列表页签切换-------------------------------------*/
	var $NavDiv2 = $("#tab ul li");
	$NavDiv2.click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var NavIndex2 = $NavDiv2.index(this);
		$(".tabss").children().eq(NavIndex2).show().siblings().hide();
	});
	
/*-------------------------------列表tass高度-------------------------------------*/
	var SearchDivHeight = 0;
	var TabsDivHeight = 0;
	var H10 = 0;//
	if($('.ConSearch').length > 0){
		SearchDivHeight = $('.ConSearch').height();
	}
	if($('#tab').length > 0){
		TabsDivHeight = $('#tab').height();
	}
	if($('.h_10').length > 0){
		H10 = $('.h_10').height();
	}
	$('.tabss').css('height',parent.document.body.clientHeight - SearchDivHeight - TabsDivHeight - H10);

/*-------------------------------列表工具栏更多-------------------------------------*/
	$(".ShowMoreTool").click(function(){
			MoreTool();
			return false;
		});
		function MoreTool(){
			$(".MoreTool").slideToggle("fast");
			$(".MoreTool").click(function(e){
				e.stopPropagation();
			});
		}
		
/*-------------------------------高级搜索-------------------------------------*/	
	$(".AdvanceSearchBtn").click(function(){
			AdvanceSearch();
			return false;
		});
		function AdvanceSearch(){
			$(".AdvanceSearch").slideToggle("fast");
			$(".AdvanceSearch").click(function(e){
				e.stopPropagation();
			});
		}
		
/*-------------------------------下拉框内部宽度设置-------------------------------------*/
	$('.DropDownList').each(function(index, element) {
        var DropDownWidth = $(this).width();
		$(this).find(".list").css('width', DropDownWidth - 12);
    });
	
/*-------------------------------点击其他地方隐藏-------------------------------------*/
	$(document).click(function(){ 
		$(".AdvanceSearch").fadeOut(); 
		$(".MoreTool").fadeOut(); 
	});
	
/*-------------------------------获取div下a标签的宽度-------------------------------------*/
	var x =0;
	var abc=$(".BtnList").find("a").each(function(){
		x+=$(this).outerWidth(true);
	});
	$(".BtnList").css("width",x);
		
/*-------------------------------easyui 弹出窗内 MC_con 高度设置-------------------------------------*/
	var headDiv = 0;
	var footDiv = 0;
	var easyuiHeight = 50;//
	if($('.MC_Top').length > 0){
		headDiv = $('.MC_Top').height();
	}
	if($('.BigTool').length > 0){
		footDiv = $('.BigTool').height();
	}
	$('.MC_con').css('height',parent.document.body.clientHeight - headDiv - footDiv - easyuiHeight);
	
}

/*-------------------------------easyui datagrid 百分比适应-------------------------------------*/
function fixWidth(percent){  
    return document.body.clientWidth * percent ; //这里你可以自己做调整  
}
