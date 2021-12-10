<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>应急指挥的代办事件</title>
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/editMessageStyle.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/grid_pc.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/houseLayer.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/index.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/layer_fillet.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/nav_hover.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/pop.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/xm_map_right.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/xmwz.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/frame.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/function.js"></script>
</head>
<body style="border:block;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="bigType" value="${bigType}" />
	<input type="hidden" id="type" value="${type}" />
	<input type="hidden" id="statusName" value="${statusName}" />
	 
    <div class="SecMenu" style="display:block;">
			<div class="con">
            	<div class="SelectKind">
                	<p><input name="content"  id="content" type="text" class="inp1" value="==事件描述=="  onfocus="if (this.value=='==事件描述==') {this.value=''}" onblur="if(this.value==''){this.value='==事件描述=='}" /><input name="search" type="button" class="btn1" onclick="loadMessage(1,10);"/></p>			    
				</div>
                 <div class="list2"  id="eventContent" style="overflow:auto;">
				</div>
                <div class="yjzhpage">
                	<div class="fl"><select name="pageSize"  id="pageSize" onchange="change('3');"><option value="5">每页 5 条</option><option value="10" selected="selected">每页 10 条</option><option value="50">每页 50 条</option></select></div>
                    <div class="fr"><a href="javascript:change('1');">上一页</a> <a href="javascript:change('2');">下一页</a> 共<span id="pagination-num">1</span>/<span id="pageCount">4</span>页</div>
                </div>
            </div>
	 </div>			
	
<script type="text/javascript">

	
	$(document).ready(function(){
	    var winHeight=window.document.body.clientHeight;
        $("#eventContent").css("height",winHeight-66); 
		loadMessage(1,10);
	});
	
	
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var gridId = $('#gridId').val();
		var bigType=$('#bigType').val();
		var type=$('#type').val();
		var statusName=$('#statusName').val();
		var content=$('#content').val();
		if(content=="==事件描述==") content="";
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&content='+content+'&bigType='+bigType+'&type='+type+'&statusName='+statusName;
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/yjzhEventListData.json',
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				
				//设置页面页数
				$('#pagination-num').text(pageNo);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  var eventClass = val.eventClass;
						if(eventClass!=null){
							//eventClass = val.eventClass.substring(val.eventClass.indexOf("-")+1,val.eventClass.length);
						}
						var content = val.content;
						if(content==null || content==""){
							content = "";
						}else if(content.length > 12){
							content = content.substring(0,12)+"...";
						}
						if(val.handleDateStatus==1){
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限正常" src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/time_normal.png" />';
						}else if(val.handleDateStatus==2){
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限将到期" src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/time_to_expire.png" />';
						}else{
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限已到期" src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/time_expired.png" />';
						}
						
					    if((i+1)%2==0){//基数
					       tableBody+='<ul  class="back"><li class="ListIcon">';
					       
					       if(val.urgencyDegreeName=='一般'){
					          tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_01.gif"/></li>';
					       }else if(val.urgencyDegreeName=='紧急'){
					          tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_03.gif"/></li>';
						   }
					       tableBody+='<li class="ListInfo" onclick="selected(\''+(val.taskInfoId==null?'':val.taskInfoId)+'\',\''+val.eventId+'\')">';
					       tableBody+='<p>事件分类：'+(eventClass==null?'':eventClass)+'</p>';
					       tableBody+='<p>发生时间：'+(val.happenTimeStr==null?'':val.happenTimeStr)+'</p>';
					       tableBody+='<p>事件描述：'+(content==null?'':content)+'</p>';
					       tableBody+='</li></ul>';
					    }else{
					       tableBody+='<ul><li class="ListIcon">';
					       if(val.urgencyDegreeName=='一般'){
					          tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_01.gif"/></li>';
					       }else if(val.urgencyDegreeName=='紧急'){
					          tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_03.gif"/></li>';
						   }
					       tableBody+='<li class="ListInfo" onclick="selected(\''+(val.taskInfoId==null?'':val.taskInfoId)+'\',\''+val.eventId+'\')">';
					       tableBody+='<p>事件分类：'+(eventClass==null?'':eventClass)+'</p>';
					       tableBody+='<p>发生时间：'+(val.happenTimeStr==null?'':val.happenTimeStr)+'</p>';
					       tableBody+='<p>事件描述：'+(content==null?'':content)+'</p>';
					       tableBody+='</li></ul>';
					    }
					    results=results+","+val.eventId;
					     //results=results+","+val.eventId+"_"+(val.taskInfoId==null?'':val.taskInfoId);
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				$("#eventContent").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<ul>数据读取错误！！！</ul>';
				$("#eventContent").html(tableBody);
			}
		});
	}
	
	
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
	    loadMessage(pagenum,pageSize);
	}
	
		
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});

	
	function selected(tid,eventId){
		//window.parent.choiseEvent();
		//window.parent.sj_flag = 1;
		//window.parent.autoGoMap(window.parent.localMapt,window.parent.mapt);
		gisPosition(tid);
		
		setTimeout(function() {
			window.parent.localtionYjzhEventPoint(tid,eventId);
		},1000);
		
		//window.parent.localtionYjzhEventPoint(tid,eventId);
	}
	
	//地图定位
	function gisPosition(res){
	window.parent.clearMyLayer();
		if(res==""){
			return;
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfYjzhEvent.jhtml?ids="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfYjzhEvent('"+url+"')";
		window.parent.getArcgisDataOfYjzhEvent(url);
	}
</script>
</body>
</html>