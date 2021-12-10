// feibo
//
// http://feibo.cn
//
// Copyright 2012, by jin
//
// 主容器页面
// 

var jin={
	numState:"20130410",
	getRandom:function(){
		var start=1000,end=9999;
		jin._getRandom=jin._getRandom+1 || start;
		if(jin._getRandom>end){
			jin._getRandom=start;
		}
		return "j"+new Date().getTime()+jin._getRandom;
	},
	wClientHeight:function(r){
		var d = document;
		if(!jin._clientHeight || r){
			jin._clientHeight=window.innerHeight || d.documentElement.clientHeight;
		}
		return jin._clientHeight;
	},
	slideAutoHeight:function(){
		var slide=$(".BoxAutoHeight");
		slide.height(jin.wClientHeight(true)-slide.offset().top);
		slide=null;
	},
	resize:function(){
		jin.slideAutoHeight();
		jin.NavBoxAutoHeight();
		jin.toggleSlideAutoHeight();
	}
};
//受控制css
//.full-con .nav-now .nav-list .nav-link .sub-nav .nav-top-now .nav-tab-now
(function(window, undefined){var document=window.document,location=document.location;
	//定义
	var fullLayout=$("#full-layout"),slide=$(".BoxAutoHeight");
	//导航自适应高度
	jin.slideAutoHeight();
	//resize
	$(window).on("resize",jin.resize);
	//隐藏显示侧边导航
	$(".toggle-slide").on("click",function(){
		if(fullLayout.hasClass("full-con")){
			fullLayout.removeClass("full-con");
		}else{
			fullLayout.addClass("full-con");
		}
		return false;
	});
	//导航伸缩
	var navbind=function(e){
		if($(this).closest(".nav-list").attr("id")==e.data.t){
			$(this).closest(".nav-list").find(".sub-nav").slideToggle("fast");
			if($("#"+e.data.t).hasClass("nav-now")){
				$("#"+e.data.t).removeClass("nav-now");
			}else{
				$("#"+e.data.t).addClass("nav-now");
			}
		}else{
			$("#"+e.data.t).find(".sub-nav").slideUp("fast");
			$("#"+e.data.t).removeClass("nav-now").removeAttr("id");
			$(this).closest(".nav-list").find(".sub-nav").slideDown("fast");
			$(this).closest(".nav-list").attr("id",e.data.t).addClass("nav-now");
		}
		return false;
	};
	$(".BoxAutoHeight [data-navbox]").each(function(){
		$(this).on("click",".nav-link",{t:jin.getRandom()},navbind);
	});
	//左侧导航切换
	var navTop=$("#top-box-max"),
		navLoading=$("#nav-loading");
	navTop.on("click","[data-navbox]",function(e){
		var self=$(this);
		navTop.find("[data-navbox]").removeClass("nav-top-now");
		self.addClass("nav-top-now");
		navLoading.stop().fadeIn(0);
		slide.find("[data-navbox]").filter("[data-navbox!="+self.data("navbox")+"]").hide("fast");
		slide.find("[data-navbox]").filter("[data-navbox="+self.data("navbox")+"]").show("fast");
		navLoading.fadeOut("slow");
		return false;
	});
	//导航跳转
	var conIframe=$("#con-iframe"),
		navTab=$("#nav-tab");
	fullLayout.on("click","[data-link]",function(){
		var self=$(this);
		//now
		$("#sub-nav-now").removeAttr("id");
		self.attr("id","sub-nav-now");
		//iframe
		var iframeid="iframe-box"+self.data("id");
		conIframe.find("[data-iframe]").hide();
		if($("#"+iframeid).size()<1){
			conIframe.append('<iframe data-iframe="true" id="'+iframeid+'" src="'+self.data("link")+'" style="width:100%; height:100%;" frameborder="0" allowtransparency="true"></iframe>');
		}else{
			$("#"+iframeid).show();
		}
		//tab
		var tabid="nav-tab"+self.data("id");
		navTab.find("[data-tab]").removeClass("nav-tab-now");
		if($("#"+tabid).size()<1){
			navTab.append
		('<li id="'+tabid+'" data-tab="true" data-dblclose="true" class="nav-tab-now"><span>'+self.text()+'<a data-close="true" href="#"><img src="images/oa_55.png"></a></span></li>');
		}else{
			$("#"+tabid).addClass("nav-tab-now");
		}
		//maxsize 6
		if(conIframe.find("[data-iframe]").size()>6){
			conIframe.find("[data-iframe]").eq(1).remove();
			navTab.find("[data-tab]").eq(1).remove();
		}
		//navTab click
		navTab.on("click","[data-tab]",function(){
			//navtab
			navTab.find("[data-tab]").removeClass("nav-tab-now");
			$(this).addClass("nav-tab-now");
			//iframe
			conIframe.find("[data-iframe]").hide();
			$("#"+$(this).attr("id").replace("nav-tab","iframe-box")).show();
		});
		return false;
	});
	//close tab
	var tabClose=function(self){
		if(self.hasClass("nav-tab-now")){
			if(self.next().size()>0){
				//navtab
				navTab.find("[data-tab]").removeClass("nav-tab-now");
				self.next().addClass("nav-tab-now");
				//iframe
				var thisiframe=$("#"+self.attr("id").replace("nav-tab","iframe-box"));
				conIframe.find("[data-iframe]").hide();
				thisiframe.next().show();
				//remove
				thisiframe.remove();
				self.remove();
			}else{
				//remove
				var thisiframe=$("#"+self.attr("id").replace("nav-tab","iframe-box"));
				thisiframe.remove();
				self.remove();
				//navtab
				navTab.find("[data-tab]").removeClass("nav-tab-now");
				navTab.find("[data-tab]").last().addClass("nav-tab-now");
				//iframe
				conIframe.find("[data-iframe]").hide();
				conIframe.find("[data-iframe]").last().show();
			}
		}else{
			//iframe
			var thisiframe=$("#"+self.attr("id").replace("nav-tab","iframe-box"));
			//remove
			thisiframe.remove();
			self.remove();
		}
	};
	navTab.on("click","[data-close]",function(){
		tabClose($(this).closest("[data-dblclose]"));
		return false;
	});
})(window);