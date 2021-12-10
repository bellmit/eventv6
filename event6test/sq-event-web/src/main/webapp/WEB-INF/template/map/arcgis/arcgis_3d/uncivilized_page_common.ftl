<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>南昌-文明创建不下线</title>
        <link rel="icon" type="image/x-icon" href="${uiDomain!''}/web-assets/_big-screen/nanchang/images/favicon.ico"/>
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
        <link rel="stylesheet" href="${uiDomain!''}/web-assets/basic/homepage/nanchang-topic/css/nc-topic.css">
        <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
        <script type="text/javascript" src="${uiDomain!''}/js/paging/paging.js"></script>
		<link rel="stylesheet" href="${uiDomain!''}/js/paging/paging.css">
		<script type="text/javascript" src="${uiDomain}/web-assets/extend/picGallery/jquery-photo-gallery-master/jquery-photo-gallery/jquery.photo.gallery.js"></script>
    </head>
    <script type="text/javascript">
		var winW, scale,rem;
		$(window).on('load resize', function () {
			fullPage();
			function fullPage() {//将页面等比缩放
				winW = <#if pwidth??>${pwidth}<#else>window.innerWidth</#if>
				if (winW < 1000) {
					winW = 1000;
				}
				var whdef =  100 / 1920;
				rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
				$('html').css('font-size', rem + "px");
			}
		});
	</script>
	<style>
		.datagrid-mask {
			z-index:51;
			position: absolute;
    		left: 0;
    		top: 0;
    		opacity: 0.3;
    		background: ccc;
		}
		
		.zxfPagenum, .prebtn, .nextbtn, .disabled, .current, .zxfokbtn {
		    width: .24rem;
		    margin-left: 0;
		    box-shadow: none;
		    height: .28rem;
		    border: 1px solid #e5e5e5;
		    border-radius: .04rem;
		    text-align: center;
		    line-height: .24rem;
		    font-size: .12rem;
		    color: #807f7f;
		    margin-right: .08rem;
		    cursor: pointer;
		}
		
		.current {
		    background: #bae7fbca;
		    box-shadow: inset 0 0 0.24rem 0 #bae7fbca;
		    border: solid .01rem #bae7fbca;
		}
		
		.zxfokbtn {
		    color: white;
		    font-size: .12rem;
		    background: #3c7ef2;
		    display: inline-block;
		    width: .5rem;
		    text-align: center;
		    line-height: .24rem;
		    cursor: pointer;
		}
	</style>
    <body>
        <div class="container">
            <!-- 顶部背景 -->
            <i class="nct-top"></i>
            <!-- 内容 -->
            <div class="content">
                <!-- logo -->
                <div class="logo pic">
                    <img src="${uiDomain!''}/web-assets/basic/homepage/nanchang-topic/images/logo.png">
                </div>
                <!-- title -->
                <div class="title pic">
                    <img src="${uiDomain!''}/web-assets/basic/homepage/nanchang-topic/images/text-pic.png">
                </div>
                <!-- small-title -->
                <div class="small-title bs">
                    <i></i>
                    <p>不 文 明 现 象 播 报</p>
                    <i></i>
                </div>
                <!-- 中间内容 -->
                <div class="nct-box">
                    <div class="nct-b-nav clearfix">
                    <#if eventLabelDict??>
                    	<#list eventLabelDict as item>
                        	<a class="chooseLabel" href="javascript:void(0);" labelId="${item.labelId}">${item.labelName}</a>
		        		</#list>
		        	</#if>
                    </div>
                    <div class="nct-b-content bs" id="scrollDiv">
                        <div class="nct-bc-box bs gallerys" id="listShowDiv">
                           
                        </div>
                    </div>
                    
                </div>
                <div class="mask-c-bottom clearfix">
					<div class="fr mcb-pagination clearfix" id="pageDiv" style="visibility:hidden;margin-top:.1rem">
						<div id="eventTypePage">
						
						</div>
					</div>
				</div>
                <!-- 底部链接 -->
                <a style="margin-top:.1rem;width:7rem;margin-left:6rem;font-size:.2rem" href="javascript:void(0)" class="nct-btn">发现类似不文明现象请拨打热线电话：<em style="color:orange">12345</em></a>
            </div>
        </div>
        <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
        <script>
        
        	var picGalleryPath = "https://citybrain.yunshangnc.com/cbgw/ccno/zhsq_event/service/event/toImgGalleryPage.jhtml";
        
            $('.nct-bc-box').niceScroll({
                cursorcolor: "#000",
                cursoropacitymax: 0.15,
                cursorwidth: "6px",
                cursorborderradius: "3px",
                autohidemode: false,
            });
            $('.nct-b-nav').on('click','a',function(){
                $(this).toggleClass('active');
                getImgData(1);
            })
            
            $(function(){
            	getImgData();
            });
            
            var curPage=1;
            var pageSize=20;
            
            
            function getImgData(page){
            	modleEventLabelopen("scrollDiv");
            	if(page){
            		curPage=page;
            	}
            	
            	var searchParams={};
            	searchParams.eventType= "all";
	        	searchParams.createTimeStart= "${createTimeStart}";
	        	searchParams.createTimeEnd= "${createTimeEnd}";
	        	searchParams.searchEventLabel= "true";
	        	searchParams.labelModel= "002";
	        	searchParams.patrolType= "1,2";
	        	searchParams.page= curPage;
	        	searchParams.rows= pageSize;
	        	searchParams.eventSeq= "1";
	        	searchParams.isGitAttr= "true";
	        	searchParams.infoOrgCode= "${infoOrgCode}";
	        	
	        	var eventLabelIdsChoose="";
	        	var eventLabelIdsAll="";
	        	$('.chooseLabel').each(function(){
	        		eventLabelIdsAll+=','+$(this).attr('labelId');
	        		if($(this).hasClass("active")){
	        			eventLabelIdsChoose+=','+$(this).attr('labelId');
	        		}
	        	});
	        	
	        	if(eventLabelIdsChoose!=""){
	        		eventLabelIdsChoose=eventLabelIdsChoose.substr(1,eventLabelIdsChoose.length-1);
	        	}else{
	        		eventLabelIdsChoose=eventLabelIdsAll.substr(1,eventLabelIdsAll.length-1);
	        	}
	        	
	        	searchParams.eventLabelIds=eventLabelIdsChoose;
            	
            	$.ajax({
			        type: "POST",
    		        url : 'https://citybrain.yunshangnc.com/cbgw/ccno/zhsq_event/service/event/fetchEventData4Jsonp.json?listType=5&userId=99&orgId=99&orgCode=3601&jsonpcallback=?',
			        data: searchParams,
			        dataType:"jsonp",
			        success: function(data){
				    	var eventData=data.list;
				    	var str="";
				    	for(var i=0,j=eventData.length;i<j;i++){
				    	
					    	str+='<div class="nct-bcb-item">';
	                        str+='<div class="nct-bcbi-content">';
	                        str+='<div class="nct-bcbic-box">';
	                        str+='<i>'+eventData[i].eventLabelName+'</i>';
	                        str+='<div class="nct-bcbicb-pic">';
	                        str+='<img style="cursor: pointer;" class="gallery-pic" onclick="$.openPhotoGallery(this)" src="${IMG_URL}'+eventData[i].attachments[0].filePath+'">';
	                        str+='</div>';
	                        str+='</div>';
	                        str+='</div>';
	                    	str+='</div>';
				    	}
				    	
				    	$('#listShowDiv').html(str);
				    	
				    	$('#listShowDiv').getNiceScroll().resize();
				    	
				    	//设置分页
						if(data.total<pageSize){
			    			$("#pageDiv").css('visibility','hidden');
						}else{
			   				$("#pageDiv").css('visibility','visible');
							$("#eventTypePage").remove();
							$("#pageDiv").html('<div id="eventTypePage"></div>');
							$("#eventTypePage").createPage({
								pageNum: (Math.floor((data.total-1)/pageSize)+1),
								current: curPage,
								backfun: function(e) {
									curPage=e.current;
									getImgData();
								}
							});
						}
				    	
				    	modleEventLabelclose();
			    	}
    			});
            }
            
            
            function modleEventLabelopen(dom) {
				
				$('#listShowDiv').before("<div class='datagrid-mask'></div>");
				$('.datagrid-mask').css({
					display : "block",
					width : "100%",
					height : $('#'+dom).height()
				});
		
				document.body.scroll = "no";//除去滚动条
			}
		
			function modleEventLabelclose() {
				$(".datagrid-mask").css({
					display : "none"
				});
				$(".datagrid-mask-msg").css({
					display : "none"
				});
				$(".datagrid-mask").remove();
				$(".datagrid-mask-msg").remove();
				document.body.scroll = "auto";
			}
            
        </script>
        
        <!-- 埋点规范 -->
        <script src="https://citybrain.yunshangnc.com/tracker/citybrain-tracker.js"></script>
		<script>
		 	var citybrain_appid = 367561;
		</script>
    </body>
</html>