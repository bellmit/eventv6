<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件列表</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="orgCode" value="${orgCode}" />
	<input type="hidden" id="pageSize" value="10" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">表单类型：</li>
                	<li class="LC2">
                	    <select name="formType" id="formType" class="sel1">
				             <option value="">==请选择==</option>
				             <#list FORM_TYPE_MAP?keys as key>
								<option value="${key}">${FORM_TYPE_MAP[key]}</option>
							 </#list>
						</select>
                	</li>
                </ul>
                <ul>
                	<li class="LC1">表单操作：</li>
                	<li class="LC2">
                		<select name="formOpt" id="formOpt" class="sel1">
				             <option value="">==请选择==</option>
				             <#list FORM_OPT_MAP?keys as key>
								<option value="${key}">${FORM_OPT_MAP[key]}</option>
							 </#list>
						</select>
                	</li>
                </ul>
                <ul>
                	<li class="LC1">事件标题：</li>
                	<li class="LC2">
                		<input type="text" name="title"  id="title" class="inp1"  />
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());"/></li>
                </ul>
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
        	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
        </div>
    </div>		
<script type="text/javascript">
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
$('#corName').keydown(function(e){ 
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
}); 
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
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map0').offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    
	});
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var orgCode = $('#orgCode').val();
		var formType = $("#formType").val();
		var formOpt = $("#formOpt").val();
		var title = $('#title').val();
		if(title=="==输入查询内容==") title="";
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	var postData = 'page='+pageNo+'&rows='+pageSize+'&formType='+formType+'&formOpt='+formOpt+'&title='+title;
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoflyjj/getArcgisLyjjEventDataList.jhtml?t='+Math.random(),  
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
					  
					  var title = val.TITLE;
					  if(title!=null && title!="" && title.length>12){
						title = title.substring(0,12)+"...";
					  }else{
						title = title;
					  }
					  
					  
					  tableBody+='<dl onclick="selected(\''+val.EVENT_ID+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b class="FontDarkBlue" title=\''+(val.TITLE==null?'':val.TITLE)+'\'>'+title+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title=\''+(val.HANDLED==null?'':val.HANDLED)+'\'>'+(val.HANDLED==null?'':val.HANDLED)+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.EVENT_ID;
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
			window.parent.localtionLyjjEventPoint(id);
		},1000);
	}
	//--定位
	function gisPosition(res){
		if (res==""){
			return ;
		}
		var corurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoflyjj/getArcgisLocateDataListOfEvent.jhtml?eventIds="+res;
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfLyjjEvent('"+corurl+"')";
		window.parent.getArcgisDataOfLyjjEvent(corurl);
	}
</script>
</body>
</html>