// @ZHANGZHIHUA
// 公共定位定位
// @ZHANGZHIHUA
// 公共定位定位
var showWinClass={
height:null,
url:null,
title:null,
width:1284,
showMax:true,//是否最大化
showToggle:true,//是否显示收缩按钮
showMin:false,//是否最小化
isResize:true,//是否调整大小
slide:false,//动作
isDrag:true,//拖动
isMax:false,
target:null,//目标对象，指定它将以appendTo()的方式载入
modal:true,//是否模态对话框
name:null,//创建iframe时 作为iframe的name和id
content:null,//内容
isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
buttons:null,//按钮
fixedType:null,//是否固定在右下角
isunmask:false,//是否取消遮罩层
name:12,//ifram 名称
scroll:true,//滚动条
buttons:null//按钮
};
function showGridWin(showWinClass){

	wingrid = $.ligerDialog.open({height:showWinClass.height, url: showWinClass.url,width: showWinClass.width, showMax: showWinClass.showMax, showToggle: showWinClass.showToggle, showMin: showWinClass.showMin, isResize: showWinClass.isResize, slide: showWinClass.slide ,modal: showWinClass.modal,title:showWinClass.title,isDrag:showWinClass.isDrag,title:showWinClass.title,isHidden:showWinClass.isHidden,buttons:showWinClass.buttons}); 
    if(showWinClass.isMax  ){
    wingrid.max();
    }
    if(showWinClass.isunmask){
    
   // wingrid.unmask(); 
    }
    if (scroll) {

		//$(".l-dialog-body").css("overflow", "hidden");
//		$(".l-dialog-content").css("overflow", "hidden");
		$(".l-dialog-content-noimage").css("overflow", "hidden");
//		$(".l-dialog-content-nopadding").css("overflow", "hidden");
//		$(".l-dialog-image").css("overflow", "hidden");
//		$(".l-dialog-buttons").css("text-align", "center");//
//		$(".l-dialog-buttons-inner").css("text-align", "center");
//		$(".l-dialog-buttons-inner").attr("align", "center");
//		$(".l-dialog-buttons-inner").css("width", "90%").css("margin","0,auto");
	} 
	return wingrid;
	
}

	// 设置事件
// $(".l-dialog-tc .l-dialog-close", g.dialog).click(function ()
//            {alert(11001);
//                if (p.isHidden)
//                    g.hide();
//                else
//                    g.close();
//            });
//	
	



  