
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>南昌-视频可视化中间弹窗</title>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-new.css">
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8">
    </script>
    <script type="text/javascript">
        var winW, scale;
        $(window).on('load resize', function () {
            fullPage();
            setTimeout(fullPage, 10); //二次执行页面缩放，解决全屏浏览时滚动条问题
            function fullPage() { //将页面等比缩放
              winW = $(window.parent).width();
               // winW = 1366;
                if (winW < 1000) {
                    winW = 1000;
                }
                var whdef = 100 / 1920;
                var rem = winW * whdef; // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");
            }
        });
    </script>
	<style>
		.tabs-check-dw>li>b{width:6.2rem;}
		/* .dianwei-xianze-title-a2>a>p{width:100%;}
		#dianwei-title1{width:1.3rem;} */
	</style>
</head>

<body style="background: transparent;">
	<!-- 点位选择 -->
		<div class="dianwei-dengji dengji01">
			<p class="p0">点位等级</p>
			<p style="cursor:pointer;" class="fr"><a id="pointLevelB" class="p1"><span>B类</span></a></p>
			<p style="cursor:pointer;" class="fr"><a id="pointLevelA" class="p1 p2"><span>A类</span></a></p>
			<p class="fr p3" id="pointLevelText">已选择A类所有点位类型</p>
		</div>
		<div class="dianwei-dengji leixing01 leixing02">
			<p class="p0">点位类型</p>
			<!-- <p style="cursor:pointer;" class="fr"><a id="checkAll" class="p1 p2"><span>全选</span></a></p> -->
			<p class="fr pr" ><span>已选择：</span><span style="cursor:pointer;" ></span></p>
            <div class="dianwei-yixuanz dianwei-yixuanz01">
                <div class="dianwei-yixuanz-btn">
                    <ul class="dianwei-yixuanz-btnul"> 
                    </ul>
                </div>
            </div>
		</div>
		<div class="dianwei-content">
	
			<div class="dianwei-xianze-btn dianwei-xianze-btn-a1 dianwei-xianze-btn-a2">
				<div id="dianwei-title1" class="dianwei-xianze-title dianwei-xianze-title-a2 clearfix">
					
					
					<#list pointTypesByDict as pointType>
					
                       <#if professionCode=="" >
	                       <#if pointType.dictLevel==4 >
	                         <a href="javascript:void(0);" 
		                         <#if pointType_index==0 >
		                         	class="active"
		                         </#if>
	                         >
	                             <p >${pointType.dictName}</p>
	                         </a>
	                       </#if>
                       <#else>
                       		<#if pointType.dictLevel==4 && pointType.dictGeneralCode==professionCode >
	                         <a href="javascript:void(0);" class="active">
	                             <p >${pointType.dictName}</p>
	                         </a>
	                       </#if>
                       
                       </#if>
                       
                    </#list> 
				</div>
				<div id="dianwei-content1" class="clearfix dianwei-xianze-content dianwei-xianze-content-a2">
					<#list pointTypesByDict as pointType>
						<#if professionCode=="" >
	                       <#if pointType.dictLevel==4 >
	                         <div 
	                          <#if pointType_index==0 >
	                         	 class="dianwei-xianze-content1 fl dianwei-xianze-content1-on"> 
	                          <#else> 
	                         	 class="dianwei-xianze-content1 fl"> 
	                          </#if>
	                          <ul class="tabs-check-dw">
	                         	<li  style="cursor:pointer;" class="qb">
									<i></i>
									<b class="qb">全部</b>
								</li>
                                 <!-- 选中用类tabs-checked -->
                                  	<#list pointTypesByDict as spointType>
                                   		<#if spointType.dictPcode=pointType.dictCode >
                                   			<#if spointType.dictRemark="A" >
                                   				<li  style="cursor:pointer;" class="p-type tabs-check-dw-ed" >
                                   			<#else>
                                   				<li  style="cursor:pointer;" class="p-type" >
                                   			</#if>
                                   				<i></i>
		                                     	<b v="${spointType.dictGeneralCode}" class="${spointType.dictRemark}" title="${spointType.dictName}">${spointType.dictName}</b>
		                                 		</li>
	                                  </#if>
                                  	</#list> 
                              </ul>
	                         </div>
	                       </#if>
	                   <#else>
	                   		<#if pointType.dictLevel==4  && pointType.dictGeneralCode==professionCode>
	                         <div class="dianwei-xianze-content1 fl dianwei-xianze-content1-on"> 
	                         
	                          <ul class="tabs-check-dw">
	                         	<li  style="cursor:pointer;" class="qb">
									<i></i>
									<b class="qb">全部</b>
								</li>
                                 <!-- 选中用类tabs-checked -->
                                  <#list pointTypesByDict as spointType>
                                   	<#if spointType.dictPcode=pointType.dictCode >
		                                 	<#if spointType.dictRemark="A" >
                                   				<li  style="cursor:pointer;" class="p-type tabs-check-dw-ed" >
                                   			<#else>
                                   				<li  style="cursor:pointer;" class="p-type" >
                                   			</#if>
                                   				<i></i>
		                                     	<b v="${spointType.dictGeneralCode}" class="${spointType.dictRemark}" title="${spointType.dictName}">${spointType.dictName}</b>
		                                 		</li>
	                                  </#if>
                                  </#list> 
                              </ul>
	                         </div>
	                       </#if>
	                   </#if>
	               </#list>
					
			</div>
	
		</div>
		<div class="dianwei-queding">
			<p style="cursor:pointer;" >确定</p>
		</div>
		
</body>

</html>
	<script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript"
	    charset="utf-8"></script>
<script>
    //美化滚动条
    $(window).on('load resize', function () {
    	
   	$('.dianwei-yixuanz-btnul').niceScroll({
           cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
           cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
           touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
           cursorwidth: "1px", //像素光标的宽度
           cursorborder: "0", // 游标边框css定义
           cursorborderradius: "1px", //以像素为光标边界半径
           autohidemode: 'leave' //是否隐藏滚动条
       });
    $('.dianwei-xianze-title').niceScroll({
        cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "2px", //像素光标的宽度
        cursorborder: "0", // 游标边框css定义
        cursorborderradius: "2px", //以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });
     $('.dianwei-xianze-content').niceScroll({
        cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "2px", //像素光标的宽度
        cursorborder: "0", // 游标边框css定义
        cursorborderradius: "2px", //以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
     });

     $('.dianwei-xianze-title').on('click', 'a', function () {
     setTimeout(function () {
          $('.dianwei-xianze-title').niceScroll().resize();
        $('.dianwei-xianze-content').niceScroll().resize();
        }, 100);
     })
    });
//美化滚动条

    /* $(function () {

        //添加移除样式切换
        $('.dianwei-add').click(function () {
        	$("#shaixuan1", parent.document).html("");
            //$(this).addClass('active').siblings().removeClass('active');
            $("#shaixuan1", parent.document).append($("#dianwei-content1 li.tabs-check-dw-ed").clone());
            $("#pointTypeCount", parent.document).html($("#dianwei-content1 li.tabs-check-dw-ed").length);
            parent.pointInfoListData();
            $(".mask-container", parent.document).hide()
        });
       
    }); */
</script>

<script>//新样式
        //美化滚动条
        $(window).on('load resize', function () {
            $('.dianwei-xianze-title, .dianwei-xianze-content').niceScroll({
                cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "2px", //像素光标的宽度
                cursorborder: "0", // 游标边框css定义
                cursorborderradius: "2px", //以像素为光标边界半径
                autohidemode: false //是否隐藏滚动条
            });

            $('.dianwei-xianze-title').on('click', 'a', function () {
                setTimeout(function () {
                    $('.dianwei-xianze-title').niceScroll().resize();
                    $('.dianwei-xianze-content').niceScroll().resize();
                }, 100);
            })
        });
        //美化滚动条

        $(function () {
			function showPointLevelText(){
				if($('#pointLevelB').hasClass('p2') && $('#pointLevelA').hasClass('p2')){
					$('#pointLevelText').text('已选择所有点位类型');
				}else if($('#pointLevelB').hasClass('p2') && !$('#pointLevelA').hasClass('p2')){
					$('#pointLevelText').text('已选择B类所有点位类型');
				}else if(!$('#pointLevelB').hasClass('p2') && $('#pointLevelA').hasClass('p2')){
					$('#pointLevelText').text('已选择A类所有点位类型');
				}else{
					$('#pointLevelText').text('');
				}
			}
			$('.dengji01>p>a').click(function () {
				$(this).toggleClass('p2');
				if($(this)[0]===$('#pointLevelA')[0]){
					if($('#pointLevelA').hasClass('p2')){
						$('.A').parent().addClass('tabs-check-dw-ed');
					}else{
						$('.A').parent().removeClass('tabs-check-dw-ed');
					}
				}else if($(this)[0]===$('#pointLevelB')[0]){
					if($('#pointLevelB').hasClass('p2')){
						$('.B').parent().addClass('tabs-check-dw-ed');
					}else{
						$('.B').parent().removeClass('tabs-check-dw-ed');
					}
				}
				
				
				showPointLevelText();
				/* if($('.dengji01>p').eq(1).find('a').hasClass('p2') && $('.dengji01>p').eq(2).find('a').hasClass('p2')){
					$('.dengji01>p').eq(3).text('已选择所有点位类型');
					$('.A').parent().addClass('tabs-check-dw-ed');
					$('.B').parent().addClass('tabs-check-dw-ed');
				}else if($('.dengji01>p').eq(1).find('a').hasClass('p2') && !$('.dengji01>p').eq(2).find('a').hasClass('p2')){
					$('.dengji01>p').eq(3).text('已选择B类所有点位类型');
					$('.B').parent().addClass('tabs-check-dw-ed');
					$('.A').parent().removeClass('tabs-check-dw-ed');
				}else if(!$('.dengji01>p').eq(1).find('a').hasClass('p2') && $('.dengji01>p').eq(2).find('a').hasClass('p2')){
					$('.dengji01>p').eq(3).text('已选择A类所有点位类型');
					$('.A').parent().addClass('tabs-check-dw-ed');
					$('.B').parent().removeClass('tabs-check-dw-ed');
				}else{
					$('.dengji01>p').eq(3).text('');
					$('.A').parent().removeClass('tabs-check-dw-ed');
					$('.B').parent().removeClass('tabs-check-dw-ed');
				} */
				initText();
			});
            //左边点位选择弹窗切换
            var ind;
            var inh;
            //右边边点位选择弹窗切换
            $('#dianwei-title1').on('click', 'a', function () {
                $(this).addClass('active').siblings('a').removeClass('active');
                ind = $(this).index();
                inh = $('#dianwei-title1').scrollTop();
                $('#dianwei-content1>div').eq(ind).addClass('dianwei-xianze-content1-on').siblings().removeClass('dianwei-xianze-content1-on');
            });
			
            var html='',html2='',html3='';
            $('.tabs-check-dw').on('click', 'li', function (e) {
            	$(this).toggleClass('tabs-check-dw-ed');
                if ($(this).find('b').text() == "全部") {
                	if($(this).hasClass('tabs-check-dw-ed')){
                		$(this).addClass('tabs-check-dw-ed');
                		$(this).siblings().addClass('tabs-check-dw-ed');
                        initText();
                	}else{
                		$(this).removeClass('tabs-check-dw-ed');
                		$(this).siblings().removeClass('tabs-check-dw-ed');
                        $('.leixing01>.pr>span:last-child').empty();
                        $('.dianwei-yixuanz01 ul').empty();
                        $('.dianwei-yixuanz01').slideUp();
                        initText();
                	}
                }else{//非全部

                    if ($(this).hasClass('tabs-check-dw-ed')) {
	                    $(this).find('b').css('color', '#fff');
	                    initText();
	                } else {
	                    $(this).find('b').css('color', '#f9f9f9');
	                    $(this).parent().children().eq(0).removeClass('tabs-check-dw-ed');//删除全选
                    	 initText();
                    	//全都不选
                         if(!$(this).parent().parent().parent().children().children().children().hasClass('tabs-check-dw-ed')){
                         	$('.leixing01>.pr>span:first-child').empty();
                     	}
	                }
                	if($(this).parent().find('.p-type').size()==$(this).parent().find('.p-type.tabs-check-dw-ed').size()){
                		$(this).parent().children().eq(0).addClass('tabs-check-dw-ed');
                	}
                }
            });
            var resizeNiceScroll=false;
			//点位选择小弹窗
            $('.leixing01>.pr>span:last-child').click(function(){
               	$('.dianwei-yixuanz01').slideToggle();
            	//$('.dianwei-yixuanz01').show();
            	if(resizeNiceScroll==false){
	            	$('.dianwei-yixuanz-btnul').getNiceScroll().resize();
	            	resizeNiceScroll=true;
            	}
            });
			//鼠标移开隐藏该窗口
            /* $('.leixing01>.pr>span:last-child').mouseleave(function(){
            	//$('.dianwei-yixuanz01').slideToggle();
            	$('.dianwei-yixuanz01').show();
           	}); */
            $('.dianwei-yixuanz01').mouseleave(function(){
            	//$('.dianwei-yixuanz01').slideToggle();
            	//$('.dianwei-yixuanz01').hide();
            	if($('.dianwei-yixuanz01').is(":hidden")){
            		//$('.dianwei-yixuanz01').show();
            	}else{
            		$('.dianwei-yixuanz01').hide();
            	} 
            	//$('.dianwei-yixuanz-btnul').niceScroll().hide();
           	});
            
            window.parent.pointLevels=getPointLevels();
            window.parent.pointTypes=getPointTypes();
            $(".dianwei-queding>p").click(function(){
            	//调用主页点位查询方法
            	window.parent.pointLevels=getPointLevels();
            	window.parent.pointTypes=getPointTypes();
            	window.parent.pointInfoListData();
            	//关闭
            	window.parent.$('.dianwei-main').removeClass('dianwei-main-on');
            	
            });
            initText();
            
            function initText(){
            	$('.leixing01>.pr>span:first-child').text('已选择：');
            	$("ul li.tabs-check-dw-ed>b").not('.qb').each(function(){
            		var text=$(this).text();
                    html+=text+';';
                    html2+='<li>'+text+'</li>';
        		});
            	if(html==''&&html2==''){
           	 		$('.leixing01>.pr>span:first-child').empty();
           	 	}
                $('.leixing01>.pr>span:last-child').empty();
                $('.leixing01>.pr>span:last-child').text(html);
                html='';
                $('.dianwei-yixuanz01 ul').empty();
                $('.dianwei-yixuanz01 ul').append(html2);
           	 	html2='';
           	 	$("#dianwei-content1>.dianwei-xianze-content1>ul").each(function(){
	           	 	if($(this).find('.p-type').size()==$(this).parent().find('.p-type.tabs-check-dw-ed').size()){
	           	 		$(this).children().eq(0).addClass('tabs-check-dw-ed');
	           	 	}else{
	           	 		$(this).children().eq(0).removeClass('tabs-check-dw-ed');
	           	 	}
           	 	});
           	 	checkPointLevel();
            }
            
            function checkPointLevel(){
            	if($("#dianwei-content1").find('.A').size()==$("#dianwei-content1").find('.tabs-check-dw-ed').find('.A').size()){
            		$('#pointLevelA').addClass('p2');
            	}else{
            		$('#pointLevelA').removeClass('p2');
            	}
            	if($("#dianwei-content1").find('.B').size()==$("#dianwei-content1").find('.tabs-check-dw-ed').find('.B').size()){
            		$('#pointLevelB').addClass('p2');
            	}else{
            		$('#pointLevelB').removeClass('p2');
            	}
            	showPointLevelText();
            }
            
            
            function getPointTypes(){
            	
            	var pointTypes="";//点位类型
            	$("ul li.tabs-check-dw-ed>b").each(function(){
        			if($(this)[0].attributes.length>1){
        				if(pointTypes!=''){
            				pointTypes+=',';
            			  }
	        			pointTypes+=$(this)[0].attributes.v.nodeValue;
        			}
        		});  
            	if(pointTypes=="")pointTypes+="N";
            	return pointTypes;
            }
            function getPointLevels(){
            	/* var pointLevels="";//点位级别 A,B,C
            	if($("#pointLevelA").hasClass('p1 p2')){
            		pointLevels+='A';
            	}
            	
            	if($("#pointLevelB").hasClass('p1 p2')){
            		if(pointLevels!=null){
                		pointLevels+=',';
                	}
            		pointLevels+='B';
            	}
            	if(pointLevels=="")pointLevels="N"; */
            	return "";
            }
          
            
        });

    </script>