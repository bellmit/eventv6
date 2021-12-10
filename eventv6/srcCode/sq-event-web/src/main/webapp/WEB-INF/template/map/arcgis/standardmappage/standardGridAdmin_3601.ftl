<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>网格员列表</title>
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
   <#-- <script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>-->
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="MetterCondition">
            	<ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:36px;">姓名：</li>
                	<li class="LC2" style="float:left; line-height:26px;"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
                <ul id="trackHasRegion" class="type content light">
                    <p>是否巡查：</p>
                    <li onclick="hasTrack(this)" value="1">是</li>
                    <li onclick="hasTrack(this)" value="0">否</li>
                    <div class="clear"></div>
                </ul>
                <ul id="trackTimeRegion" class="content" style="display: none">
                    <li class="LC1" style="float:left; line-height:26px; width:60px;">开始时间：</li>
                    <li>
						<input type="text" id="trackStartDate" class="Wdate inp1" style="width:150px; height:26px; line-height:24px;">
					</li>
                    <li class="LC1" style="float:left; line-height:26px; width:60px;">结束时间：</li>
                    <li><input type="text" id="trackEndDate" class="Wdate inp1" style="width:150px; height:26px; line-height:24px;"></li>
                </ul>
            		<#--
            		<#if (dutyDC?? && dutyDC?size>0)>
                <ul id="content-md2" class="type content light" style="height: 150px;">
                	<p>选择职务：</p>
						<#list dutyDC as l>
							<li onclick="select(this)" id="${l.dictId}" value="${l.dictGeneralCode}">${l.dictName}</li>
						</#list>
                    <div class="clear"></div>
                </ul>
					</#if>
					-->
				<div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></div>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content" style="" id="content">
        	
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
/*默认选中网格员*/
	(function($){

	})(jQuery);


	var inputNum;

	function pageSubmit() {
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if (isNaN(inputNum)) {
			inputNum = 1;
		}
		if (parseInt(inputNum) > parseInt(pageCount)) {
			inputNum = pageCount;
		}
		if (inputNum <= 0 || inputNum == "") {
			inputNum = 1;
		}
		change('4');
	}

	function ShowOrCloseSearchBtn() {
		var temp = $(".ListSearch").is(":hidden");//是否隐藏
		if (temp == false) {
			$(".ListSearch").hide();
		} else {
			$(".ListSearch").show();
		}
	//var temp1= $(".ListSearch").is(":visible");//是否可见

	}

	$('#name').keydown(function (e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});

	function CloseSearchBtn() {
		$(".ListSearch").hide();
	}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());


        //$("#content-md2").mCustomScrollbar({theme:"minimal-dark"});
	});
	var results="";//获取定位对象集合
	var layerName="";
	
	//查询
	function loadMessage(pageNo,pageSize,searchType){
		if ($("#elementsCollectionStr").val() != "") {
			layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
			window.parent.currentLayerName = layerName;
		}
		
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var gridId = $('#gridId').val();
		var name = $('#name').val();
		var pageSize = $("#pageSize").val();

    	var  postData = {page:pageNo,rows:pageSize,gridId:gridId,name:name,regionCtrl:'nanchang'}
    	//网格管理员职务
        var dutys = "";
    	/*
    	$('#content-md2 li').each(function(index) {
			if($(this).attr('class')=='current'){
				dutys = $(this).attr('value') + "," + dutys;
			}
		});
		if (dutys!=null && dutys!="" && dutys.length > 0) {
			dutys = dutys.substring(0, dutys.length - 1);
			postData['duty']=dutys;
		}*/
		//是否巡查
		//巡查起止日期
        var trackEl = $("#trackHasRegion").children("li.current");
		if(trackEl && trackEl.attr('value')===1){
            var trackStartDate = $("#trackStartDate").val(),trackEndDate = $("#trackEndDate").val();
            if(trackStartDate=="" || trackEndDate==""){
                alert("清选择巡查起止日期");
                return;
			}
            if (trackStartDate > trackEndDate) {
                alert("开始时间需要小于结束时间");
                return;
            }
			postData['trackStartDate']=trackStartDate;
			postData['trackEndDate']=trackEndDate;
		}

		if($('#elementsCollectionStr').val() != "") {
            postData['elementsCollectionStr']=$('#elementsCollectionStr').val();
		}
        $.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
            background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminListData.json?t='+Math.random(), 
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
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  
					  tableBody+='<dl onclick="selected(\''+val.gridAdminId+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">' + (val.mobileTelephone==null?'':'<img src="${uiDomain!''}/images/map/gisv0/special_config/images/people_06.png">' + (val.mobileTelephone))+'</span>';
					  tableBody+='<b class="FontDarkBlue">'+(val.partyName==null?'':val.partyName)+'</b>'+
					  ((dutys==""||dutys.length>3)?(val.dutyLabel==null?'':'<label>【' + val.dutyLabel + '】</label>'):"");
					  tableBody+='</dt>';
					  tableBody+='<dd>'+(val.gridName==null?'':val.gridName)+'</dd>';
					  tableBody+='</dl>';
					  			
					  results=results+","+val.gridAdminId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
	var currentPageNum=1;
	 //分页
     function change(_index){
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
	    loadMessage(pagenum,pageSize);
	}
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	function selected(id){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 390;
			opt.h = 300;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		if(window.parent.qnviccub != undefined) {
	  		$("#qnviccub", window.parent.document).remove();
	  	}
	  	
	  	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+id;

		var winHeight = window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-200;
		
		var params = {
			title: "人员详情表",
			targetUrl: url,
			height: 320,
			width: 430,
			top: 32,
			left: 0,
			modal: false,
			collapsible: true,
			resizable: false
		}
		
		parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
		parent.showMaxJqueryWindowByParams(params);
	  	
	//	setTimeout(function() {
	//		if($('#elementsCollectionStr').val() != "") {
	//			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),390,300,id);
	//		}else {
	//			window.parent.localtionGridAdminOrXldjPoint(id,'');
	//		}
	//	},1000);
	
	}
	
	//地图定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 390;
			opt.h = 300;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res;
			return parent.MMApi.markerIcons(opt);
		}
		
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res==""){return ;}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),390,300);
		}else {
		  	var gridAdminurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res+"&showType=2";
		  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGridAdminOrXldj('"+gridAdminurl+"','')";
			window.parent.getArcgisDataOfGridAdminOrXldj(gridAdminurl,'');
		}
	}
	
	//--网格职务选择
	function select(obj){
		if($(obj).attr('class')!='current'){
			$(obj).addClass("current");
		}else{
			$(obj).removeClass("current");
		}
	}
	//是否巡查
	function hasTrack(el) {
	    var me = $(el),hasCurrent = me.hasClass('current');
		me.parent("ul").children("li").removeClass("current");
        me.addClass("current");
        if($(el).attr('value')===1){
            //昨天日期
            var day1 = new Date();
            day1.setDate(day1.getDate() - 1);
            var s1 = day1.format("yyyy-MM-dd hh:mm:ss");
            //设置时间事件
            $("#trackTimeRegion").find("input").val(s1);
            $("#trackTimeRegion").find("input").click(function () {
                var elId = $(this).attr("id");
                var cfg = {el:elId,trackStartDate:s1, dateFmt:'yyyy-MM-dd HH:mm:ss', readOnly:true};
                if(elId === 'trackStartDate') {
                    //cfg["maxDate"] = '#F{$dp.$D(\'trackEndDate\')}';
				} else {
                    cfg["minDate"] = '#F{$dp.$D(\'trackStartDate\')}';
				}
				WdatePicker(cfg);
            });
		   $("#trackTimeRegion").show();
        }else{
            $("#trackTimeRegion").hide();
        }
    }
</script>
</body>
</html>