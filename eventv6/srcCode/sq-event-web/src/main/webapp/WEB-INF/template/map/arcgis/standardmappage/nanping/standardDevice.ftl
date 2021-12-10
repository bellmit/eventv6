<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点列表</title>

 
<!-- 引入layUI样式 优化样式 -->
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/css/layui.css"/>
<!--本部样式-->
<!--引入 重置默认样式 statics/zhxc -->
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/monitor-air.css"/>
<!-- zTree 原生脚本 -->
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/layui.js" charset="utf-8"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
<style>
.rcontent {
    background: rgba(0, 69, 132, .3);
}
.NorPage {
    width: 100% ;   
    height: 40px;
}

.PreBtn  {
    float:right;
}
.PageInp{
	width: 54px;   
}
.PageInp input {
    width: 45px;
}
.cont-list{
background-color:transparent;
}
</style>

</head>
	<body>
		<div class="mr-container">
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="30" />
			<!--右边栏目-->
			<div class="rcontent rc-local-jc">
				<!--选项-->
				<div class="top-tabbar">
					<ul class="top-tabbar-con clearfix">
						<li id="gj">
							<i class="icon-tt-jg"></i>
							<h3>告警</h3>
							<span id="gj_val">0</span>
						</li>
						<li  class="tabbar-active" id="all">
							<i class="icon-tt-all"></i>
							<h3>全部</h3>
							<span id="all_val">0</span>
						</li>
					</ul>
				</div>
				<div class="right-content-first">
					<!--搜索-->
					<div class="cont-sch">
						<div class="sch-inp-box clearfix">
							<i></i>
							<input id="stationname" type="text" class="search-input" placeholder="请输入搜索关键词"/>
							<span class="btn-search-bo" onclick="loadMessage(1,30,'3')">搜索</span>
						</div>
					</div>
					<!--列表-->
					<div class="mr-cont-list-box">
						<div class="mr-cont-list">
							<ul class="cont-list">
							</ul>
							<!--分页-->
							<div class="cont-list-page">
							<div class="NorPage">
					        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
					        </div>
							</div>
						</div>
					</div>
				</div>
			</div>
 
		</div>
		<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	</body>
<script>
    $(function() {
        //图片居中
		var imgH, imgW, imgboxH, imgboxW;
		var rcontH, contschH, rlistboxH, rightcontentH, titleH ,toptebH;
		$(window).on('load resize', function () {
			imgH = $('.map-imgbox>img').height();
			imgW= $('.map-imgbox>img').width();
			imgboxW = $('.map-imgbox').width();
			imgboxH = $('.map-imgbox').height();
			$('.map-imgbox>img').css({'top': -(imgH - imgboxH)/2, 'left': -(imgW - imgboxW)/2});
		//右边栏目
            rightcontentH = $(window).height();
            titleH = $('.right-content-title').height();
            console.log(titleH);
            toptebH = $('.top-tabbar').height();
            $('.right-content-first').height(rightcontentH - titleH - toptebH);
            rcontH = $('.right-content-first').height(); //
            contschH = $('.cont-sch').height();
            $('.mr-cont-list-box').height(rcontH - contschH + 25); //高度：mr-cont-list-box

		});

		//选择top-tabbar
        $('.top-tabbar-con').on('click','li', function () {
          if($(this).attr("id")=='gj'){
         	// $(".gj").show();
         	// $(".zc").hide();
         	 showType='gj';
          }else{
         	// $(".zc").show();
         	 showType='';
          }
          loadMessage(1,30,'0');
            $(this).addClass('tabbar-active').siblings().removeClass('tabbar-active');
        });
    });
</script>
    
	<script>
	var showType='0';
	$(document).ready(function(){
	         loadMessage(1,$("#pageSize").val(),'1');
	    	//详情的滚动条
            $('.cont-list').niceScroll({
                cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "4px", //像素光标的宽度
                cursorborder: "0", // 游标边框css定义
                cursorborderradius: "4px",//以像素为光标边界半径
                autohidemode: false //是否隐藏滚动条
            });
           
           
	    
	});
	function loadMessage(pageNo,pageSize,init){
		var results="";
		var stationname = $('#stationname').val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
		var postData = 'page='+pageNo+'&rows='+pageSize+'&sources=${sources!}&showType='+showType;
		if(init=='3'){//搜索
			postData+='&stationname='+stationname;
		}
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/listData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){ 
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
			 
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				var val1=0,val2=0,val3=0,val4=0,val5=0,val6=0,gj=0;
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  var aqi=parseInt(val.zgAQI.aqi);
					  var li="";
					  if(val.zgAQI.aqi>100){
					 	 gj+=1;
					  }
					  li+='<li class="cont-list-li " onClick="selected(\''+val.seqid+'\')">';
					  li+='<div class="cont-list-txt">';
						  li+='<h5 class="li-title" title="'+val.stationName+'">'+val.stationName+'</h5>';
							  li+='<div class="li-txt-data clearfix">';
								  li+='<i class=""></i>';
									  li+='<span class="txt-data">'+val.zgAQI.aqi+'</span>';
									  li+='<span ';
									
									li+='class="mrdata-bq bgcolor-tob-'+val.zgAQI.state+'">'+val.zgAQI.stateName;
									  if(aqi>=0 && aqi<=50){  
									    val1+=1;  
									  }
									  if(aqi>=51 && aqi<=100){
									    val2+=1;  
									  }
									   if(aqi>=101 && aqi<=150){
									    val3+=1;  
									  }
									   if(aqi>=151 && aqi<=200){
									     val4+=1;  
									  }
									   if(aqi>=201 && aqi<=300){
									    val5+=1;  
									  }
									   if(aqi>300){
									    val6+=1;  
									  }
									  
									  li+='</span>';
										  li+='</div>';
										  li+='<div class="data-mass-box clearfix">';
											  li+='<div class="data-mass">';
												  li+='<h6>PM2.5</h6>';
												  li+='<p class="data-mass-txt"><span class="dat">'+val.zgAQI.pm25+'</span><span class="unit">μm/m³</span></p>';
											  li+='</div>';
											  li+='<div class="data-mass">';
												  li+='<h6>PM10</h6>';
												  li+='<p class="data-mass-txt"><span class="dat">'+val.zgAQI.pm10+'</span><span class="unit">μm/m³</span></p>';
											  li+='</div>';
											  li+='<div class="data-mass">';
												  li+='<h6>O3</h6>';
												  li+='<p class="data-mass-txt"><span class="dat">'+val.zgAQI.o3+'</span><span class="unit">μm/m³</span></p>';
											  li+='</div>';
										  li+='</div>';
									  li+='</div>';
						  li+='</li>';
						tableBody+=li;
						results=results+","+val.seqid;  
						//selected(val.seqid);
					}
					
					// parent.setLevelData("id1",val1);
					// parent.setLevelData("id2",val2);
					// parent.setLevelData("id3",val3);
					// parent.setLevelData("id4",val4);
					 //parent.setLevelData("id5",val5);
					// parent.setLevelData("id6",val6);
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;">暂无数据</div>';
				}
				if(init=='1'){  
					$("#gj_val").html(gj);
					$("#all_val").html(data.total);   
				}else{
				
				  if(showType=='gj'){
				 	 $("#gj_val").html(data.total);
				  }else{
				 	 $("#all_val").html(data.total);
				  }
				}
				$(".cont-list").html('');
				
				$(".cont-list").html(tableBody);
				if($("#gj").hasClass("tabbar-active")){
					 $(".gj").show();
		         	 $(".zc").hide();
		          }else{
		         	 $(".zc").show();
		          }
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
	}
        $(function(){
			//手写 弹窗 start
			//页面加载完成后弹窗显示
//             $('.layer-con').show().removeClass('bounceInScale2').addClass('bounceInScale');
//				弹窗淡入
            $('.pon-w').on('click', function(){
                $('.layer-con').show().removeClass('bounceInScale2').addClass('bounceInScale');
                $(".mr-container").addClass('boxfilter');
                setTimeout(function () {
    			intChart.resize();}, 0);
            });
//				弹窗淡出
            $('.layer-con-close').on('click', function(){
                $('.layer-con').addClass('bounceInScale2').removeClass('bounceInScale');
                setTimeout(function(){$('.layer-con').hide();$('.mr-main-jainc1').show().siblings().hide();}, 300);
                $(".mr-container").removeClass('boxfilter');
            });
            //手写 弹窗 end
            //弹窗内容的切换
            var mrIndex;
            $('.mjtb-box').on('click', 'a', function(){
            	$(this).addClass('active').siblings().removeClass('active');
            	mrIndex = $(this).index();
            	if (mrIndex == 0) {
            		$('.mr-main-jainc2').show().siblings().hide();
            	} else if (mrIndex == 1) {
            		$('.mr-main-jainc3').show().siblings().hide();
            	}
            })
            $('.mr-cancel').click(function(){
            	$('.mr-main-jainc1').show().siblings().hide();
            })
        });
	var inputNum;
	function pageSubmit(){
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		if(parseInt(inputNum)>parseInt(pageCount)){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		change('4');
	}
var currentPageNum=1;
	 //分页
     function change(_index){
     $("#all").addClass("tabbar-active");
     $("#gj").removeClass("tabbar-active")
     
     
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
			case '1':		//上页
			    if(pagenum==1){
			      flag=1;
			      break;
			    }
				pagenum = parseInt(pagenum) - 1;
				pagenum = pagenum < firstnum ? firstnum : pagenum;
				break;
			case '2':		//下页
			    if(pagenum==lastnum){
			      flag=2;
			      break;
			    }
				pagenum = parseInt(pagenum) + 1;
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
		    case '3':
		        flag=3;
		        pagenum=1;
		        break;
		    case '4':
		        pagenum = inputNum;
		        if(pagenum==lastnum){
			      flag=4;
			      break;
			    }
				pagenum = parseInt(pagenum);
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
			default:
				break;
		}
		
		if(flag==1){
		  alert("当前已经是首页");
		  return;
		}else if(flag==2){
		  alert("当前已经是尾页");
		  return;
		}
		currentPageNum = pagenum;
	    loadMessage(pagenum,pageSize,'0');
	}
	

	function selected(id) {
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 490;
			opt.h = 270;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClickOnTitle($('#elementsCollectionStr').val(),490,270,id)
			}else {
				window.parent.localtionCasesPoint(id);
			}
		},1000);
	}

	
	//--定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 490;
			opt.h = 270;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/getArcgisLocateDataListOfInfos.jhtml?ids="+res+"&markerType=7788";
			return parent.MMApi.markerIcons(opt);
		}
		  
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			//window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res==""){
			return ;
		}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/getArcgisLocateDataListOfInfos.jhtml?ids="+res+"&markerType=${deviceType!}";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),490,270,30,30);
		}else {
			var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfYanpingStDeviceController/getArcgisLocateDataListOfDeviceInfos.jhtml?ids="+res+"&markerType=${deviceType!}";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfCases('"+gisDataUrl+"')";
			window.parent.getArcgisDataOfDevice(gisDataUrl);
		}
	}
	</script>
</html>