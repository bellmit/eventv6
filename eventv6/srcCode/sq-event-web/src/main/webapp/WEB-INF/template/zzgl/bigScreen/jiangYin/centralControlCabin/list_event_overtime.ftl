<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>江阴市中控舱-超时问题</title>
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css">
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/css/public.css">
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/css/zkc.css">
        <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
        <script>
            var winW, winH, scale, whdef, rem;
            $(window).on('load resize', function () {
                fullPage();
                setTimeout(fullPage, 10);//二次执行页面缩放，解决全屏浏览时滚动条问题
            })
            function fullPage() {//将页面等比缩放
                winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>
                winH = <#if pheight??>${pheight}<#else>$(window.parent).height()</#if>
                if (winW < 1000) {
                    winW = 1000;
                }
                whdef = 100 / 1920;
                rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");
                scale = winW / 1920;
                $('.container_fluid').css({ 'width': 1920 * scale, 'height': winH, 'overflow': 'hidden' });
                if ((!!window.ActiveXObject || "ActiveXObject" in window)) {
                    $('.container_fluid').css({ 'width': 1920 * scale, 'height': winH - 1, 'overflow': 'hidden' });
                }
            }
        </script>
        <style>
        .zkc-cb-right {
    		visibility: visible;
		}
		.zkc-cbrc-list>li p {
			text-align: center;
		}
		.zkc-cbrcl-time {
    		width: 1.6rem;
    	}
    	.zkc-cbrcl-name {
    		width: 2rem;
    	}
	.mtr10 {
    	     margin-top: 0rem; 
	     margin-left:-.1rem;
	}
        </style>
    </head>
    <body>
        <div class="container_fluid">
        	<div class="wrap_container" style="margin-top:-.1rem">
				<div class="jy-csn-content flex flex-jb bs">
					<div class="jy-csnc-center">
						<div class="zkc-c-bottom mtr10 clearfix" style="overflow: hidden;">
							<div class="zkc-cb-right module-bg" style="margin-top:-.2rem;margin-left:-.2rem">
            					<div class="zkc-cbr-content-box" style="margin-left: .8rem;opacity: 1;">
                					<div class="zkc-cbr-content">
                    					<div class="swiper-container swiper-container1">
                         					<div class="swiper-wrapper" id="swiperContent">
                         	
                         					</div>
                    					</div>
                					</div>
            					</div>
							</div>
						</div>
					</div>
				</div>
			</div>
        </div>
        <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
        <script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"></script>
        <script src="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/js/countUp.js"></script>
        <script>
        

        var curPage=1;
        var pageSize=20;
        var infoOrgCode=${infoOrgCode!'320281'};
        var swiperSize=5;//列表轮播模块每一页显示的事件条数
        var begintime;
        var endtime;
        
        function initTime(){
        	var begin=new Date();
        	var end=new Date();
        	new Date(begin.setMonth((new Date().getMonth()-1)));
        	begintime= begin.format("yyyy-MM-dd");
        	endtime=end.format("yyyy-MM-dd");
        }
        
        $(function(){
        	getListData(curPage);
        });
        
        
        function getListData(curPageNum){
        	initTime();
        	$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
				data: {"handleDateFlag":3,"page":curPageNum,"rows":pageSize,"eventType":"all","createTimeStart":begintime,"createTimeEnd":endtime,
				"bizPlatform":"0","status":"00,01,02,03","isEntryMatter":true,"isEnableDefaultCreatTime":true,"infoOrgCode":infoOrgCode,"orderByField":"T1.CREATE_TIME"},
				dataType:"json",
				success: function(data){
					console.log(data);
					setEventList(data);
				}
			});
        }
        
        function setEventList(data){
        
			var str = "",d="";
			for(var i=0,l=data.rows.length;i<l;i++){
			
				d = data.rows[i];
				if(i%swiperSize==0){
					str+='<div class="swiper-slide">'+
					'<ul class="zkc-cbrc-list">';
				}
				
				str+='<li class="flex flex-jb flex-ac">'+
				'<p style="cursor:pointer" title="'+d.eventName+'" class="zkc-cbrcl-name" onclick="showEventDetail('+d.eventId+')">'+strFn(d.eventName,12)+'</p>'+
				'<p style="text-align:left" class="zkc-cbrcl-time">已超时'+formatHandleDate(d.handleDate)+'</p>'+
				'<a href="javascript:void(0);" style="width:0.5rem" onclick="showEventDetail('+d.eventId+')">'+formatSuperviseMark(d.superviseMark)+'</a>'+
				'</li>';
				
				if(i%swiperSize==(swiperSize-1)){
					str+='</ul>'+
					'</div>';
				}
				
				if(i==data.rows.length&&(i%swiperSize<(swiperSize-1))){
					str+='</ul>'+
					'</div>';
				}
				
			}
			
			$("#swiperContent").html(str);
			initDom();
			
		}
		
		window.addEventListener("message",function(e){  
				eval(e.data);
		},false);
		
		
		//督办点击事件
		function showEventDetail(eventId){
		
			window.parent.postMessage('showEventDetail('+eventId+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
			
		}
		
		
        
           
        
        //格式化字符串省略显示    
        function strFn(str,rank){
			if(str == null ||str == 'null' || str == undefined ){
				return '';
			}
	
			if(str.length>rank){
				return str.substring(0,rank)+"...";
			}else{
				return str;
			}
		}
		
		function formatSuperviseMark(superviseMark){
			if(superviseMark == null ||superviseMark == 'null' || superviseMark == undefined ){
				return '督办';
			}
			
			if(superviseMark=='1'){
				return '已督办';
			}else{
				return '督办';
			}
		}
		
		//计算与当前时间相差的天数
		function formatHandleDate(handleDate) {
   var timestamp = new Date().getTime() - handleDate;
   var xx = timestamp / 1000 / 60 / 60 / 24;
   xx=xx.toFixed(2);
   xx=formatHandleDateStr(xx.toString());
   return xx;
  }
  function formatHandleDateStr(xx){
  var str=xx;
  var strarr=str.split(".");
		if(!strarr[1]){
			strarr[1]='0'
		}
  var str2="0."+strarr[1];
  var t=strarr[0];
  if(t!=0){
   t=t+"天";
  }else{
   t="";
  }
  var str3=1440*str2;
  var fz="";
  var xs="";
  if(str3<60){
   str3=str3.toFixed(0);
   fz+=str3;
   fz+="分";
   t+=fz;
   return t;
  }else if(str3.toFixed(0)==60){
   xs+="1时";
   t+=xs;
   return t;
  }else if(str3>60){
  var s="";
  s+=str3/60;
  var arrxs=s.split(".");
  xs+=arrxs[0];
  xs+="时";
		if(!arrxs[1]){
			arrxs[1]='0'
		}
  var f="0."+arrxs[1];
  var f2=60*f;
  f2=f2.toFixed();
  fz+=f2;
  fz+="分";
  var xsfz=xs+fz;
  return t+=xsfz;
  }
  }

		function initDom(){
			var swiper;
				// 超时问题的轮番
                swiper = new Swiper('.swiper-container1', {
                    direction: 'vertical',
                    loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
                }); 
                swiper.autoplay.stop();
                $('.zkc-cbr-content').hover(function(){
                    swiper.autoplay.stop();
                },function(){
                    swiper.autoplay.start();
                })
                
                 // 页面的动效
            $('.jy-csn-top,.zkc-ct-title').children('div').animate({'width': '100%'}, 1500,'swing').promise().then(function(){
                $('.jy-csnc-title-box').css('visibility','visible');
                $('.jy-csnc-title-box').children('div').animate({'width':'100%'},1000,'swing').promise().then(function(){
                    $(this).parent().parent().css('visibility','visible');
                    var that = this;
                    $('.zkc-lt-top-box,.zkc-lt-bottom-box,.zkc-lb-content,.zkc-cbr-content-box,.zkc-echarts-box').animate({'marginLeft':'0','opacity':'1'},1500).promise().then(function(){
                        $('.zkc-lt-bottom').niceScroll({
                            cursorcolor: "#2458a7",
                            cursoropacitymax: 0.8,
                            cursorwidth: ".05rem",
                            cursorborderradius: ".05rem",
                            cursorborder: 'none',
                            autohidemode: true,
                        })
                        swiper.autoplay.start();
                        // 右边进度的动效
                        var divWidth;
                        $('.zkc-rt-content .zkc-rtc-item').each(function(){
                            divWidth = $(this).find('.zkc-pb-proportion>p').text()
                            $(this).find('.zkc-progress-bar>div').width(divWidth);
                        })
                    });
                    $('.zkc-rt-box,.zkc-rb-content').animate({'right':'0','opacity':'1'},1500);
                })
            })
		}
		
		Date.prototype.format = function(format) {
    	var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" : this.getMilliseconds()
    	// millisecond
    	}
    	if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
    	for ( var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
    	return format;
		}
		
		
        </script>
    </body>
</html>