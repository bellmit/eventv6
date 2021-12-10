<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>海康设备信息列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/map/utils/HashMapUtil.js"></script>

</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="eqpType" value="${eqpType!''}" />
	<input type="hidden" id="gdcId" value="${gdcId!''}" />
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="infoOrgCode" value="${infoOrgCode!''}" />
	<input type="hidden" id="keyWord" value="${keyWord!''}" />
	<input type="hidden" id="elementsCollectionStr" value="${elementsCollectionStr!''}" />
	<input type="hidden" id="pageSize" class="queryParam" value="20" />
	
    <div class="" style="display:block;margin:1px;">
        <div class="showRecords" style="width:298px;">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content" style="height:319px;width:298px;" id="content">
        </div>
        <div class="NorPage" style="position:initial;width:288px;">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
	var inputNum;

	var mapMenu = new HashMap();
	function pageSubmit(){
		var gdcId = $("#gdcId").val();
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
	
	$('#conditionDiv input[type="text"]').filter(".queryParam").keydown(function(e){ 
		if(e.keyCode==13){ 
			loadMessage(1,$("#pageSize").val());
		} 
	}); 
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	
	$(document).ready(function(){
		var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-156-96+43+56);

       	window.parent.IS_ACCUMULATION_LAYER = "1";//图层累加
		getMenuInfo(gdcId);
	});
	
	function ShowOrCloseSearchBtn(){
		var temp= $(".ListSearch").is(":hidden");//是否隐藏 
		if(temp == false) {
			$(".ListSearch").hide();
		}else {
			$(".ListSearch").show();
		}
	}
	
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	
	var results="";//获取定位对象集合
	var accessControl="";//获取定位对象集合
	var accessBrake="";//获取定位对象集合
	var bayonetEquipment="";//获取定位对象集合
	var accessAlarm="";//获取定位对象集合
	var visitorMachine="";//获取定位对象集合
	var videoCamera="";//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var pageSize = $("#pageSize").val();
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&eqpType='+$("#eqpType").val()+'&eqpName='+$("#keyWord").val()+'&infoOrgCode='+$("#infoOrgCode").val();
		var queryVal = "";
		
		$("#conditionDiv .queryParam").each(function() {
			queryVal = $(this).val();
			if(queryVal) {
				postData += '&' + $(this).attr('id') + '=' + queryVal;
			}
		});
		
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listEquipmentData.jhtml?t='+Math.random(),  
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
				var list= data.rows;
				var tableBody = '<div class="liebiao">';
				
				if(list && list.length > 0) {
					for(var i = 0, len = list.length;i < len; i++){
					  var val = list[i];
					  var eqpAddress = val.location;
					  var eqpName = val.eqpName;
					  var eqpType = val.eqpType;
					  
					  if(!eqpAddress) {
					  	eqpAddress = "&nbsp;";//为了排版整齐
					  } else if(eqpAddress.length>14){
					  	eqpAddress = eqpAddress.substring(0,14)+"";
					  }
					  
					  if(!eqpName) {
					  	eqpName = "";
					  } else if(eqpName.length>14){
					  	eqpName = eqpName.substring(0,14)+"";
					  }

					  var imgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/mapicon/"+(i+1)+"h.png";
					  var typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/menu_accessControl.png";
					tableBody+='<div style="padding-left:5px;line-height:80px;float:left;height:40px;width:33px;"><img style="width:18px;height:26px;" src="'+imgUrl+'" title="" /></div>';
					  
					  tableBody+='<dl onclick="selected(\''+val.eqpId+'\',\''+eqpName+'\',\''+eqpType+'\',\''+i+'\')">';
					  
					  var title = "";
					  var color = "";
					  if(eqpType=="001"){//门禁
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/menu_accessControl.png";
						  title = "门禁";
						  color = "#0FCF00";
						  accessControl += "," + val.eqpId;
						  gisPosition(val.eqpId,"accessControl",i);
					  }else if(eqpType=="002"){//访客机
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/visitorMachine.png";
// 						  tableBody+='<div style="float:right;margin-top:1px;height:38px;width:40px;background-color:#76A728;"><img style="width:30px;height:39px;" src="'+typeImgUrl+'" title="" /></div>';
						  title = "访客机";
						  color = "#458B74";
						  visitorMachine += "," + val.eqpId;
						  gisPosition(val.eqpId,"visitorMachine",i);
					  }else if(eqpType=="003"){//车闸
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/accessBrake.png";
// 						  tableBody+='<div style="float:right;margin-top:1px;height:38px;width:40px;background-color:#CD8500;"><img style="width:30px;height:39px;" src="'+typeImgUrl+'" title="" /></div>';
						  title = "车闸";
						  color = "#CD7054";
						  accessBrake += "," + val.eqpId;
						  gisPosition(val.eqpId,"accessBrake",i);
					  }else if(eqpType=="004"){//报警机
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/menu_accessAlarm.png";
// 						  tableBody+='<div style="float:right;margin-top:1px;height:38px;width:40px;background-color:#CD2626;"><img style="width:30px;height:39px;" src="'+typeImgUrl+'" title="" /></div>';
						  title = "报警机";
						  color = "#FF0000";
						  accessAlarm += "," + val.eqpId;
						  gisPosition(val.eqpId,"accessAlarm",i);
					  }else if(eqpType=="005"){//视频设备
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/menu_videoCamera.png";
// 						  tableBody+='<div style="float:right;margin-top:1px;height:38px;width:40px;background-color:#005BCF;"><img style="width:30px;height:39px;" src="'+typeImgUrl+'" title="" /></div>';
						  title = "视频设备";
						  color = "#6CA6CD";
						  videoCamera += "," + val.eqpId;
						  gisPosition(val.eqpId,"videoCamera",i);
					  }else if(eqpType=="006"){//卡口
						  typeImgUrl = "${uiDomain!''}/images/map/gisv0/special_config/images/menu_bayonetEquipment.png";
// 						  tableBody+='<div style="float:right;margin-top:1px;height:38px;width:40px;background-color:#722DA1;"><img style="width:30px;height:39px;" src="'+typeImgUrl+'" title="" /></div>';
						  title = "卡口";
						  color = "#8B0000";
						  bayonetEquipment += "," + val.eqpId;
						  gisPosition(val.eqpId,"bayonetEquipment",i);
					  }
					  tableBody+='<div style="margin-right:5px;height:38px;width:40px;float:right;margin-top:1px;background-color:'+color+';"><img style="width:22px;height:24px;padding-left:9px;padding-top:7px;" src="'+typeImgUrl+'" title="'+title+'" /></div>';
					  
					  	  tableBody+='<dt>';
						  		
						    tableBody+='<b style="font-weight:normal;" class="FontDarkBlue" title="'+ (val.eqpName==null?'':val.eqpName) +'">'+ eqpName +'</b>';
							  
							  tableBody+='<dd>';
							  
							  tableBody+='<span title="'+ (val.location==null?'':val.location) +'">'+ eqpAddress +'</span>';
							  
							  tableBody+='</dd>';
						  
						  tableBody+='</dt>';
					  tableBody+='</dl>';
					
					  
					  results += "," + val.eqpId;
					}
					
// 					accessControl = accessControl.substring(1);
// 					accessBrake = accessBrake.substring(1);
// 					bayonetEquipment = bayonetEquipment.substring(1);
// 					accessAlarm = accessAlarm.substring(1);
// 					visitorMachine = visitorMachine.substring(1);
// 					videoCamera = videoCamera.substring(1);
					
				} else {
					tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				
// 				gisPosition(accessControl,"accessControl");
// 				gisPosition(accessBrake,"accessBrake");
// 				gisPosition(bayonetEquipment,"bayonetEquipment");
// 				gisPosition(accessAlarm,"accessAlarm");
// 				gisPosition(visitorMachine,"visitorMachine");
// 				gisPosition(videoCamera,"videoCamera");
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
			alert('当前已经是首页！');
		  	return;
		}else if(flag==2){
			alert('当前已经是尾页！');
			return;
		}
		currentPageNum = pagenum;
	    loadMessage(pagenum,pageSize);
	}
	
	function selected(id, name,eqpType, index){
		var eqpTypeCode = "";
		if(eqpType=="001"){
			eqpTypeCode = "accessControl";
		}else if(eqpType=="003"){
			eqpTypeCode = "accessBrake";
		}else if(eqpType=="006"){
			eqpTypeCode = "bayonetEquipment";
		}else if(eqpType=="004"){
			eqpTypeCode = "accessAlarm";
		}else if(eqpType=="002"){
			eqpTypeCode = "visitorMachine";
		}else if(eqpType=="005"){
			eqpTypeCode = "videoCamera";
		}
// 		var elementsCollectionStr = mapMenu.get(eqpTypeCode);
		var elementsCollectionStr = format(mapMenu.get(eqpTypeCode), index);console.log(id+'='+elementsCollectionStr);
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 195;
			opt.ecs = elementsCollectionStr;
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		setTimeout(function() {
			if(elementsCollectionStr != "") {
				window.parent.getDetailOnMapOfListClick(elementsCollectionStr,340,195,id)
			}
		},1000);
	}
	
	function format(elementsCollectionStr, index){
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/goods/accessControl.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/region_videoCamera.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/visitorMachine_marker.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/region_accessAlarm.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/accessBrake.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		elementsCollectionStr = elementsCollectionStr.replace("/images/map/gisv0/map_config/unselected/goods/bayonetEquipment.png","/images/map/gisv0/special_config/images/mapicon/"+(parseInt(index)+1)+"h.png");
		
		elementsCollectionStr = elementsCollectionStr.replace("accessBrakeLayer","accessControlLayer");
		elementsCollectionStr = elementsCollectionStr.replace("visitorMachineLayer","accessControlLayer");
		elementsCollectionStr = elementsCollectionStr.replace("accessAlarmLayer","accessControlLayer");
		elementsCollectionStr = elementsCollectionStr.replace("bayonetEquipmentLayer","accessControlLayer");
		elementsCollectionStr = elementsCollectionStr.replace("videoCameraLayer","accessControlLayer");
		return elementsCollectionStr;
	}
	
	//地图定位
	function gisPosition(res,eqpType,index){
		var elementsCollectionStr = format(mapMenu.get(eqpType), index);
		///images/map/gisv0/map_config/unselected/goods/accessControl.png
		
		$('#elementsCollectionStr').val(mapMenu.get(eqpType));
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 195;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfEquipment.jhtml?ids="+res;
			return parent.MMApi.markerIcons(opt);
		}
		
		if(index == '0'){
			window.parent.clearSpecialLayer("accessControlLayer");
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
		if (res==""){
			return ;
		}
	  	console.log(elementsCollectionStr);
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfEquipment.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
			window.parent.getArcgisDataOfZhuanTi(url,elementsCollectionStr,340,195,18,26);
		}
	}

	function getMenuInfo(gdcId){
		var gdcId = $("#gdcId").val();
		var orgCode = $("#infoOrgCode").val();
		var homePageType = "IDSS";
		var displayStyle = '0'; // 平铺
		$.ajax({ 
			 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
			 type: 'POST',
			 timeout: 300000,
			 data: { orgCode:orgCode,homePageType:homePageType,gdcId:gdcId,isRootSearch:0},
			 dataType:"json",
			 async: true,
			 error: function(data){
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
			 },
			 success: function(data){
			    gisDataCfg=eval(data.gisDataCfg);
			    if(gisDataCfg != null){
			    	var htmlStr = "";
			    	var callBack = "";
			    	var elementsCollectionStr = "";
				    var ulFirstallList = gisDataCfg.childrenList;
				    if (ulFirstallList.length > 0) {
				    	for(var i=0; i<ulFirstallList.length; i++){
				    		if(ulFirstallList[i].menuCode == 'accessControl'){//门禁设备
				    			mapMenu.put("accessControl",ulFirstallList[i].callBack);
				    		}else if(ulFirstallList[i].menuCode == 'accessBrake'){//车闸设备
				    			mapMenu.put("accessBrake",ulFirstallList[i].callBack);
				    		}else if(ulFirstallList[i].menuCode == 'bayonetEquipment'){//卡口设备
				    			mapMenu.put("bayonetEquipment",ulFirstallList[i].callBack);
				    		}else if(ulFirstallList[i].menuCode == 'accessAlarm'){//报警机
				    			mapMenu.put("accessAlarm",ulFirstallList[i].callBack);
				    		}else if(ulFirstallList[i].menuCode == 'visitorMachine'){//访客机
				    			mapMenu.put("visitorMachine",ulFirstallList[i].callBack);
				    		}else if(ulFirstallList[i].menuCode == 'videoCamera'){//视频设备
				    			mapMenu.put("videoCamera",ulFirstallList[i].callBack);
				    		}
				    	}
// 				    	console.log("mapMenu---"+mapMenu.get("visitorMachine"));
					    loadMessage(1,$("#pageSize").val());
				    } else {
				    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
				    }
			    }
			 }
		 });

	}
</script>
</body>
</html>