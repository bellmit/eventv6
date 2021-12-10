<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>隐患</title>
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
<body style="border:none;">
	 
    <div class="SecMenu" style="display:block;">
			<div class="con">
            	<div class="SelectKind">
                	<p><input name="placeName"  id="placeName" type="text" class="inp1" value="==输入场所名称=="  onfocus="if (this.value=='==输入场所名称==') {this.value=''}" onblur="if(this.value==''){this.value='==输入场所名称=='}"/><input name="search" type="button" class="btn1" onclick="loadMessage(1,10);"/><input name="moreSearch" id="moreSearch" type="button" class="btn3" /></p>
				    <div class="AdvanceSearch" style="display:none;">
                    	<ul>
                        	<li>楼宇名称：
                        	    <input name="buildingName"  id="buildingName" type="text" class="inp1"/>
							</li>
                        </ul>
                    </div>
				</div>
                 <div class="list2"  id="content" style="overflow:auto;">
				</div>
                <div class="page">
                	<div class="fl"><select name="pageSize"  id="pageSize" onchange="change('3');"><option value="5">每页 5 条</option><option value="10" selected="selected">每页 10 条</option><option value="50">每页 50 条</option></select></div>
                    <div class="fr"><a href="javascript:change('1');">上一页</a> <a href="javascript:change('2');">下一页</a> 共<span id="pagination-num">1</span>/<span id="pageCount">4</span>页</div>
                </div>
            </div>
	 </div>			
	
<script type="text/javascript">
	$(document).ready(function(){
	    var winHeight=window.document.body.clientHeight;
        $("#content").css("height",winHeight-70); 
		loadMessage(1,10);
		 
	});
	
	
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var placeName=$('#placeName').val();
		if(placeName=="==输入场所名称==") placeName="";
		var buildingName=$('#buildingName').val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		//var postData = "pageNo="+pageNo+"&pageSize="+pageSize+"&placeName="+placeName+"&buildingName="+buildingName;
		var postData = 'page='+pageNo+'&rows='+pageSize+'&placeName='+placeName+"&buildingName="+buildingName+'&gridId='+${gridId!};
		$.ajax({
			type: "POST",
			//url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/hiddenTroubleListData.json?t='+Math.random(),
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/dangousListData.json?t='+Math.random(),
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
				
				//项目的列名：relaInputBean.projectLabel   企业：relaInputBean.enterpriseName
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					    if((i+1)%2==0){//基数
					       tableBody+='<ul class="back"><li class="ListIcon"><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/building_dormitory.png" /></li>';
					       tableBody+='<li class="ListInfo" onclick="selected(\''+val.safeCheckDetailId+'\',\''+(val.placeName==null?'':val.placeName)+'\')">';
					       tableBody+='<p>场所名称：'+(val.placeName==null?'':val.placeName)+'</p>';
					       tableBody+='<p>楼宇名称：'+(val.buildingName==null?'':val.buildingName)+'</p>';
					        tableBody+='<p>隐患描述：'+(val.checkDetail==null?'':val.checkDetail)+'</p>';
					       tableBody+='</li></ul>';
					    }else{
					       tableBody+='<ul ><li class="ListIcon"><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/building_dormitory.png" /></li>';
					       tableBody+='<li class="ListInfo" onclick="selected(\''+val.safeCheckDetailId+'\',\''+(val.placeName==null?'':val.placeName)+'\')">';
					       tableBody+='<p>场所名称：'+(val.placeName==null?'':val.placeName)+'</p>';
					       tableBody+='<p>楼宇名称：'+(val.buildingName==null?'':val.buildingName)+'</p>';
					        tableBody+='<p>隐患描述：'+(val.checkDetail==null?'':val.checkDetail)+'</p>';
					       tableBody+='</li></ul>';
					    }
						results=results+","+val.safeCheckDetailId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<ul>数据读取错误！！！</ul>';
				$("#content").html(tableBody);
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
	
	
	function selected(id, name){
		//gisPosition(id);
		
		setTimeout(function() {
			window.parent.localtionDangousPoint(id);
		},1000);
	
		//window.parent.localtionDangousPoint(id);
	}
	//地图定位
	function gisPosition(res){
	window.parent.clearMyLayer();
	    if (res==""){
			return ;
		}
		var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfDangous.jhtml?ids="+res;
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfDangous('"+gisDataUrl+"')";
		window.parent.getArcgisDataOfDangous(gisDataUrl);
	}
</script>
</body>
</html>