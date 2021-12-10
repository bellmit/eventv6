<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="elementsCollectionStr" value="gdcId_,_41380,_,orgCode_,_3502,_,homePageType_,_ARCGIS_STANDARD_HOME,_,smallIco_,_/images/map/gisv0/map_config/unselected/situation_gridAdmin.png,_,menuCode_,_gridAdmin,_,menuName_,_网格员,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/situation_gridAdmin.png,_,menuListUrl_,_/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdmin.jhtml,_,menuSummaryUrl_,_/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId=,_,menuLayerName_,_gridAdminLayer,_,menuDetailUrl_,_null,_,menuDetailWidth_,_null,_,menuDetailHeight_,_null,_,menuSummaryWidth_,_365,_,menuSummaryHeight_,_282,_,callBack_,_showObjectList,_," />
    <div class="" style="display:block;">
        <div class="ListShow content" style="width:200px;" id="content">
        	
        </div>
    </div>		
<script type="text/javascript">
	/*默认选中网格员*/
	(function($){
		
	})(jQuery);
	
	
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
	
	function ShowOrCloseSearchBtn(){
	var temp= $(".ListSearch").is(":hidden");//是否隐藏 
	if(temp == false) {
		$(".ListSearch").hide();
	}else {
		$(".ListSearch").show();
	}
	//var temp1= $(".ListSearch").is(":visible");//是否可见
		
	}
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	var isOnAnychat=false;//anychat是否在线
	$(document).ready(function(){ 
        var winHeight=400;
        $("#content").height(winHeight);
        loadMessage();
        if(parent.BRAC_GetFriendStatus && typeof(parent.BRAC_GetFriendStatus) == 'function'){
        	isOnAnychat=true;
        }
	});
	var results="";//获取定位对象集合
	var rowsObj = null;//获取定位对象集合
	var layerName="";
	function loadMessage(opts) {
		var settings = {
			pageNo : 0,
			pageSize : 10000,
            mapType : "${mapType}",
            distance : "${distance}",
            x : "${x}",
            y : "${y}",
            infoOrgCode : "${infoOrgCode}",
			zhoubianType : 'zhouBianStatOfGridAdminService',
			suffix : '_list'
		};
		var params = $.extend({}, settings, opts);
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		window.parent.clearSpecialLayer(layerName);
		window.parent.currentListNumStr = "";
		results="";rowsObj = null;
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
    	var dutys = "";
    	var postData;
    	
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t='+Math.random(), 
			data : params,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				var userIds = "";var tels = "";
				//设置页面页数
				var list=data.rows;
				rowsObj = data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					tableBody+='<ul>';
					
					for(var i=0;i<list.length;i++){
					  var val=list[i],
					  	  mobileTelephone = val.MOBILE_TELEPHONE || '',
					  	  partyName = val.PARTY_NAME || '',
					  	  userId = val.USER_ID || '';
					  
					  tableBody+='<dl onclick="selected(\''+val.GRID_ADMIN_ID+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">' + (val.MOBILE_TELEPHONE==null?'':('<img id="_sendMsgImg" src="${uiDomain!}/images/text_msg.png" title="发送信息" _gridAdminId="'+ val.GRID_ADMIN_ID +'" _userId="'+ userId +'" _mobileTelephone="'+ mobileTelephone +'" _partyName="'+ partyName +'" />&nbsp;'+mobileTelephone))+'</span>';
					  
					  tableBody += '<b class="FontDarkBlue" ';
					  if(partyName && partyName.length > 2) {  
					  	tableBody += ' title="'+ partyName +'" ';
					  	partyName = partyName.substring(0, 2) + "...";
					  }
					  tableBody += ' >'+ partyName +'</b>' + (val.DUTY_LABEL==null?'':'<label>[' + val.DUTY_LABEL + ']</label>');
					 try{
					  if(isOnAnychat && parent.BRAC_GetFriendStatus(userId)){
						tableBody += '<img width="12px" height="12px" id="_vedioImg" src="${uiDomain!}/js/anychat/webMeeting/images/advanceset/camera_false.png" title="发送视频" _gridAdminId="'+ val.GRID_ADMIN_ID +'" _userId="'+ userId +'" _mobileTelephone="'+ mobileTelephone +'" _partyName="'+ partyName +'" />'; 
					  }}catch(e){
						console.log(e);
					  }
					
					  tableBody+='</dt>';
					  tableBody+='<dd><span class="fr">距离：'+val.DISTANCE+'米</span>'+(val.GRID_NAME==null?'':val.GRID_NAME)+'</dd>';
					  tableBody+='</dl>';
					  			
					  results=results+","+val.GRID_ADMIN_ID;
					}
			        tableBody+='</ul>';
					results=results.substring(1, results.length);
                    if(val.USER_ID!=null&&val.USER_ID!="")
                        userIds=userIds+","+val.USER_ID;

                    if(val.MOBILE_TELEPHONE!=""&&val.MOBILE_TELEPHONE!=null)
                        tels=tels+","+val.MOBILE_TELEPHONE;
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
					//tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				//_vedioImg
				$("#content img[id='_vedioImg']").click(function(event) {
					var userId = $(this).attr("_userId"),partyName=$(this).attr("_partyName");
					parent.transBuffer_userId(userId,partyName,1)
					event.stopPropagation();//阻止将点击事件向上传递
				});
				$("#content img[id='_sendMsgImg']").click(function(event) {
					var userId = $(this).attr("_userId"),
						url = '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/toSendMsg.jhtml?gridAdminId='+ $(this).attr('_gridAdminId') +"&partyName="+$(this).attr("_partyName")+"&mobileTelephone="+$(this).attr("_mobileTelephone")+'&bizType=01';
						params = {};
					
					if(userId) {
						url += '&userId='+userId;
					}
					
					params = {
						title: "发送信息",
						targetUrl: url,
						width: 850,
						height: 400,
						resizable: true
					};
					
					parent.showMaxJqueryWindowByParams(params);
					
					event.stopPropagation();//阻止将点击事件向上传递
				});
				
				$(".AdvanceSearch").css("display","none");
				gisPosition(results, rowsObj);
                if(userIds!=""){
                    userIds=userIds.substring(1, userIds.length);
                    parent.msgIds.push(userIds.split(','));
                }
                if(tels!=""){
                    tels=tels.substring(1, tels.length);
                    parent.msgTels.push(tels.split(','));
                }
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

		var winHeight =380;// window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-200;
		
		var params = {
			title: "人员详情表",
			targetUrl: url,
			height: winHeight,
			width: 385,
			top: 32,
			left: 0,
			modal: false,
			collapsible: true,
			resizable: false
		}
		
		parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
		parent.showMaxJqueryWindowByParams(params);
	
	}
	
	//地图定位
	function gisPosition(res) {
		window.parent.clearSpecialLayer(layerName);
		if (res == "" && results == "") {
			return;
		} else if (typeof res == "undefined") {
			res = results;
		}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res;
			/*window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),390,300,undefined,undefined,{
				isShowMarkerName : 1
			});*/
			
			window.parent.markerArcgisDataOfZhuanTi($('#elementsCollectionStr').val(), rowsObj, null, {
				fieldId : 'GRID_ADMIN_ID',
				fieldName : 'PARTY_NAME',
				gisDataUrl : url,
				isShowMarkerName : 1,
				preMakeFunc : function(rowObj) {
					if (rowObj && rowObj.DUTY == '004') {
						var opt = {
							url : obj.iconUrl.replace('situation_gridAdmin.png', 'situation_car.png'),
							width : 30,
							height : 39
						};
						return opt;
					}
				}
			});
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
</script>
</body>
</html>