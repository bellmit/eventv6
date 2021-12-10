window.onload = function(){
/*-------------------------------列表页签切换-------------------------------------*/
	var $NavDiv2 = $("#tab ul li");
	$NavDiv2.click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var NavIndex2 = $NavDiv2.index(this);
		$(".tabss").children().eq(NavIndex2).show().siblings().hide();
		$(".tabss2").children().eq(NavIndex2).show().siblings().hide();
	});

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
	$(".BtnList").each(function(){
		var x =0;
		$(this).find("a").each(function(){
			x+=$(this).outerWidth(true);
		});
		$(this).css("width",x);
	});
		
/*-------------------------------easyui 弹出窗内 MC_con 高度设置-------------------------------------*/
	var headDiv = 0;
	var footDiv = 0;
	if($('.MC_Top').length > 0){
		headDiv = $('.MC_Top').height();
	}
	if($('.BigTool').length > 0){
		footDiv = $('.BigTool').outerHeight();
	}
	var parentH = $("#MaxJqueryWindow",parent.document).height();
	if(typeof(parentH) != "undefined"){
		$('.MC_con').css('height', (parentH - headDiv - footDiv));
	}
}

/*-------------------------------easyui datagrid 百分比适应-------------------------------------*/
function fixWidth(percent){  
    return document.body.clientWidth * percent ; //这里你可以自己做调整  
}

function fixHeight(percent){  
    return document.body.clientHeight * percent ; //这里你可以自己做调整  
}
