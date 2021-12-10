<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>江阴市中控舱-预警事件</title>
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
        .module-bg {
    		visibility: visible;
		}
        </style>
    </head>
    <body>
        <div class="container_fluid">
            <div class="wrap_container">
                <div class="jy-csn-content flex flex-jb bs" style="margin-top:-.1rem">
                    <!-- 右边内容 -->
                    <div class="jy-csnc-right" style="margin-top:-.1rem">
                        <div class="zkc-r-bottom mtr10 module-bg" style="overflow: hidden;margin-left:-.1rem">
                            <div class="zkc-rb-content" style="right: -.8rem;opacity: 0;">
                                <div class="zkc-rb-top">
                                    <div id="keyWordPartTitle" class="zkc-rbt-keyword" >
                                        <div class="flex">
                                            <p>关键字:</p>
                                            <p class="flex1" id="keyword"></p>
                                        </div>
                                    </div>
                                    <div id="urgencePartTitle" style="display: block;">
                                        <div class="flex flex-jc">
                                            <div style="cursor:pointer" onclick="showEventList(3)" class="zkc-rbt-item urgent clearfix">
                                                <i></i>
                                                <p id="urgence_03">重大0件</p>
                                            </div>
                                            <div style="cursor:pointer" onclick="showEventList(2)" class="zkc-rbt-item normal clearfix">
                                                <i></i>
                                                <p id="urgence_02">紧急0件</p>
                                            </div>
                                            <div style="cursor:pointer" onclick="showEventList(1)" class="zkc-rbt-item general clearfix">
                                                <i></i>
                                                <p id="urgence_01">一般0件</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="zkc-rt-btn clearfix">
                                    <a id="showKeyWordPart" href="javascript:void(0);" class="fr">
                                        <div class="flex flex-ac flex-jc"><i></i></div>
                                        <p>关键字</p>
                                    </a>
                                    <a id="showUrgencePart" href="javascript:void(0);" class="fr active">
                                        <div class="flex flex-ac flex-jc"><i></i></div>
                                        <p>紧急程度</p>
                                    </a>
                                </div>
                                <div class="zkc-rb-bottom">
                                    <div id="keyWordContent">
                                        <div class="zkc-rbb-thead flex flex-jb">
                                            <p class="category">类型</p>
                                            <p class="title flex1">标题</p>
                                            <p class="time">时间</p>
                                        </div>
                                        <div class="zkc-rbb-tbody">
                                            <div class="swiper-container swiper-container3">
                                                <div class="swiper-wrapper" id="warnEventListContent">
                                                    
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="urgenceContent" style="display: block;">
                                        <div class="zkc-rbb-thead flex flex-jb">
                                            <p class="category">类型</p>
                                            <p class="title flex1">标题</p>
                                            <p class="time">时间</p>
                                        </div>
                                        <div class="zkc-rbb-tbody">
                                            <div class="swiper-container swiper-container2">
                                                <div class="swiper-wrapper" id="urgenceEventListContent">
                                                    
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
        </div>
        <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
        <script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"></script>
        <script src="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/js/countUp.js"></script>
        <script>
        	var curPage=1;
        	var pageSize=21;
        	var infoOrgCode=${infoOrgCode!'320281'};
        	var swiperSize=3;//列表轮播模块每一页显示的事件条数
        	
        	var now = new Date(); //当前日期   
			var nowMonth = now.getMonth()+1; //当前月   
			var nowYear = now.getFullYear(); //当前年   
        	var begintime;
        	var endtime;
        	var curMonthStart;
        	var curMonthEnd;
        	var searchUrgency='03';
        	
        	$(function(){
        		getListData(curPage);
        	});
        
        	function getListData(curPageNum){
        		initTime();
        		getMonthDays();
        			$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getWarningEventData.json',
					data: {"page":curPageNum,"rows":pageSize,"listType":"5","begintime":begintime,"endtime":endtime,"curMonthStart":curMonthStart,"curMonthEnd":curMonthEnd,
					"infoOrgCode":infoOrgCode,"status":"00,01,02,03","searchUrgency":searchUrgency},
					dataType:"json",
					success: function(data){
						console.log(data);
						setListData(data);
					}
				});
        	}
        	
        	function setListData(data){
        	
        		//先设置各种紧急程度的事件总数
        		//获取总数map
        		var urgencyDegreeCount=data.urgencyDegreeCount;
        		for(var key in urgencyDegreeCount){
        			$("#urgence_"+key).html(urgencyDegreeCount[key]);
        		}
        		
        		//设置关键词
        		$('#keyword').html(data.warnKeyWord);
        		
        		//设置紧急程度列表
        		var str = "",d="";
				for(var i=0,l=data.urgencyDegreeList.rows.length;i<l;i++){
			
				d = data.urgencyDegreeList.rows[i];
				if(i%swiperSize==0){
					str+='<div class="swiper-slide">'+
					'<ul class="zkc-cbrc-list1">';
				}
				
				str+='<li class="flex flex-jb">';
				if(d.urgencyDegree=='03'){
					str+='<p class="category urgent">';
				}else if(d.urgencyDegree=='02'){
					str+='<p class="category normal">';
				}else{
					str+='<p class="category general">';
				}
				str+=d.urgencyDegreeName+'事件</p>'+
				'<p style="cursor:pointer" title="'+d.eventName+'" onclick="showEventDetail('+d.eventId+')" class="title flex1">'+d.eventName+'</p>'+
				'<p class="time">'+new Date(d.createTime).format('yyyy-MM-dd')+'</p>'+
				'</li>';
				
				if(i%swiperSize==(swiperSize-1)){
					str+='</ul>'+
					'</div>';
				}
				
				if(i==data.urgencyDegreeList.rows.length&&(i%swiperSize<(swiperSize-1))){
					str+='</ul>'+
					'</div>';
				}
				
				}
			
				$("#urgenceEventListContent").html(str);
				
				//设置预警事件列表
				//清空变量
				str = "";
				d="";
				for(var i=0,l=data.warnList.rows.length;i<l;i++){
			
				d = data.warnList.rows[i];
				if(i%swiperSize==0){
					str+='<div class="swiper-slide">'+
					'<ul class="zkc-cbrc-list1">';
				}
				
				str+='<li class="flex flex-jb">';
				str+='<p  title="'+formatStr(d.typeName)+'" class="category normal">'+formatStr(d.typeName)+'</p>'+
				'<p style="cursor:pointer" title="'+d.eventName+'" class="title flex1" onclick="showEventDetail('+d.eventId+')">'+d.eventName+'</p>'+
				'<p class="time">'+new Date(d.createTime).format('yyyy-MM-dd')+'</p>'+
				'</li>';
				
				if(i%swiperSize==(swiperSize-1)){
					str+='</ul>'+
					'</div>';
				}
				
				if(i==data.warnList.rows.length&&(i%swiperSize<(swiperSize-1))){
					str+='</ul>'+
					'</div>';
				}
				
				}
				
				$("#warnEventListContent").html(str);
				
				initDom();
				
        	}
        	
			
            //swiper轮番
            var swiper1,swiper2;
        	function initDom(){
        	
                // 预警事件的轮番
                // 紧急程度的轮番
                swiper1 = new Swiper('.swiper-container2', {
                    direction: 'vertical',
                    loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
                }); 
                swiper1.autoplay.stop();
                $('.zkc-rb-bottom>div:last-child .zkc-rbb-tbody').hover(function(){
                    swiper1.autoplay.stop();
                },function(){
                    swiper1.autoplay.start();
                })
                // 关键字轮番
                swiper2 = new Swiper('.swiper-container3', {
                    init: false,
                    direction: 'vertical',
                    loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
                }); 
                swiper2.autoplay.stop();
                $('.zkc-rb-bottom>div:first-child .zkc-rbb-tbody').hover(function(){
                    swiper2.autoplay.stop();
                },function(){
                    swiper2.autoplay.start();
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
                        swiper1.autoplay.start();
                    });
                    $('.zkc-rt-box,.zkc-rb-content').animate({'right':'0','opacity':'1'},1500);
                })
            	})
                
            }
        
        	
           
            
            $('#showKeyWordPart').on('click',function(){
            	$('#showKeyWordPart').addClass('active');
            	$('#showUrgencePart').removeClass('active');
            	$('#keyWordPartTitle').show();
            	$('#urgencePartTitle').hide();
            	$('#keyWordContent').show();
            	$('#urgenceContent').hide();
            	
            	swiper2.init();
                swiper2.autoplay.start();
                swiper1.autoplay.stop();
            	
            })
            
            $('#showUrgencePart').on('click',function(){
            	$('#showKeyWordPart').removeClass('active');
            	$('#showUrgencePart').addClass('active');
            	$('#keyWordPartTitle').hide();
            	$('#urgencePartTitle').show();
            	$('#keyWordContent').hide();
            	$('#urgenceContent').show();
            	
            	swiper1.autoplay.start();
                swiper2.autoplay.stop();
            	
            })
            
            
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
			
			//获得当前月的天数   
			function getMonthDays() {   
    			var d=new Date(nowYear,nowMonth,0);
    			var days=d.getDate();
    			curMonthStart=nowYear+'-'+nowMonth+'-'+'01';   
    			curMonthEnd=nowYear+'-'+nowMonth+'-'+days;   
			}  
			
			function initTime(){
        		var begin=new Date();
        		var end=new Date();
        		new Date(begin.setYear((new Date().getFullYear()-1)));
        		new Date(begin.setMonth((new Date().getMonth()+1)));
        		begintime= begin.format("yyyy-MM")+'-01';
        		endtime=end.format("yyyy-MM-dd");
        	}
        	
        	window.addEventListener("message",function(e){  
				eval(e.data);
			},false);
			
			function formatStr(str){
				if(str == null ||str == 'null' || str == undefined ){
					return '';
				}else{
					return str;				
				}
			}
			
			//格式化字符串省略显示    
        	function strFn(str,rank){
				if(str == null ||str == 'null' || str == undefined ){
					return '';
				}
	
				if(str.length>rank){
					return str.substring(0,rank);
				}else{
					return str;
				}
			}
		
			//点击查看详情
			function showEventDetail(eventId){
		
				window.parent.postMessage('showEventDetail('+eventId+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
			
			}
			
			//点击查看相应的紧急程度的事件
			function showEventList(urgDegree){
				window.parent.postMessage('showEventList('+urgDegree+','+infoOrgCode+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
			}
        </script>
    </body>
</html>