<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员</title>
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
	<input type="hidden" id="gridId" value="${gridId?c}" />
	
	   <div class="SecMenu" style="display:block;">
			<div class="con">
            	<div class="SelectKind">
                	<p><input name="name"  id="name" type="text" class="inp1" value="==输入姓名=="  onfocus="if (this.value=='==输入姓名==') {this.value=''}" onblur="if(this.value==''){this.value='==输入姓名=='}" /><input name="search" type="button" class="btn1" onclick="loadMessage(1,10);"/><input name="moreSearch" id="moreSearch" type="button" class="btn3" /></p>
					<div class="AdvanceSearch" style="display:none;">
                    	<ul>
                        	<li>所属网格：
                        			<input type="text" readonly="readonly" style="width:120;" onclick="showSingleMixedGridSelector();" name="gridName" id="gridName" value="<#if gridName??>${gridName}</#if>" />
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
<script type="text/javascript">
	$(document).ready(function(){
	    var winHeight=window.document.body.clientHeight;
        $("#content").css("height",winHeight-70); 
		loadMessage(1,10);
		$("#moreSearch").toggle(function(){
		    $(".AdvanceSearch").css("display","block");
		},function(){
		   $(".AdvanceSearch").css("display","none");
		});
	});
	
	
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var gridId = $('#gridId').val();
	    var name = $('#name').val();
	    if(name=="==输入姓名==") name="";
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    		background:'url(${uiDomain!''}/images/map/gisv0/map_taijiang/images/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminListData.json?t='+Math.random(), 
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
					    if((i+1)%2==0){//基数
					       tableBody+='<ul class="back"><li class="ListIcon"><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation_gridAdmin.png"/></li>';
					       tableBody+='<li class="ListInfo" onclick="selected(\''+val.gridAdminId+'\',\''+(val.partyName==null?'':val.partyName)+'\')">';
					       tableBody+='<p>姓名：'+(val.partyName==null?'':val.partyName)+'</p>';
					       tableBody+='<p>职务：'+(val.dutyLabel==null?'':val.dutyLabel)+'</p>';
					       tableBody+='<p>电话：'+(val.mobileTelephone==null?'':val.mobileTelephone)+'</p>';
					       tableBody+='<p title="'+val.gridPath+'">网络：'+(val.gridName==null?'':val.gridName)+'</p>';
					       tableBody+='</li></ul>';
					    }else{
					       tableBody+='<ul><li class="ListIcon"><img  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation_gridAdmin.png"/></li>';
					       tableBody+='<li class="ListInfo" onclick="selected(\''+val.gridAdminId+'\',\''+(val.partyName==null?'':val.partyName)+'\')">';
					       tableBody+='<p>姓名：'+(val.partyName==null?'':val.partyName)+'</p>';
					       tableBody+='<p>职务：'+(val.dutyLabel==null?'':val.dutyLabel)+'</p>';
					       tableBody+='<p>电话：'+(val.mobileTelephone==null?'':val.mobileTelephone)+'</p>';
					       tableBody+='<p title="'+val.gridPath+'">网络：'+(val.gridName==null?'':val.gridName)+'</p>';
					       tableBody+='</li></ul>';
					    }
						results=results+","+val.gridAdminId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				$("#content").html(tableBody);
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
	
	//查看单条记录详情
	function selected(id, name){
		//gisPosition(id);
		
		setTimeout(function() {
			window.parent.localtionGridAdminOrXldjPoint(id,'');
		},1000);
	
		//window.parent.localtionGridAdminOrXldjPoint(id,'');
	}
	
	//地图定位
	function gisPosition(res){
	window.parent.clearMyLayer();
		if (res==""){
			return ;
		}
		
		var gridAdminurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGridAdminOrXldj('"+gridAdminurl+"','')";
		window.parent.getArcgisDataOfGridAdminOrXldj(gridAdminurl,'');
	}
	
	function showSingleMixedGridSelector() {
		window.parent.showSingleMixedGridSelector();
	}
	
	function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto) {
		$("#gridId").val(gridId);
		$("#gridName").val(gridName);
	}
</script>
</body>
</html>