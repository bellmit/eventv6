<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>莆田中间首页页面视频调度</title>
<link rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/public.css">
<link rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/frame.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/gridListStyle.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/mapData.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/js/tablestyle.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>

<style type="text/css">
.backosp {
    background: #f3f6fd;
    height: 24px;
    padding: 6px 10px 0; font-size:14px;
}
.backosp a {
    background: url(${rc.getContextPath()}/theme/arcgis/standardmappage_putian/images/icon2_02.png) no-repeat;
    display: block;
    height: 16px;
    line-height: 16px;
    padding: 0 0 0 23px;
    text-decoration: none !important;
    width: 90px; float:left
}
.backosp span{float:right;}
.pageContent1{position: relative; display: block;width:100%; height:100%;}
	
#listtable{table-layout: fixed;}

#listtable td{
　　white-space: nowrap;
　　overflow: hidden;
　　text-overflow: ellipsis;
}
.kwgray{color:#878787;}
</style>
</head>
<body style="border:none;">
	
	<table style="background-color: #f7f7f7;height:35px;" class="pageContent">
		<tr>
			<td>名称<input type="text" id="platformName" size="15" value="==输入查询内容==" class="kwgray" onfocus="this.className='kw';if (this.value=='==输入查询内容==') {this.value=''}" onblur="if(this.value==''){this.className+='gray';this.value='==输入查询内容=='}" />
			<input type="button" value="查询" onclick="page(0);" /></td>
		</tr>
	</table>
	<DIV id="mainpanel" class="pageContent">
		<DIV class="grid" >				
			<DIV id="gridTbodyId" class="gridTbody"  >
				 <div id="box_4" style="display:block;">
                    <div class="NavBox VideoList" id="listtable">   
                        
                    </div>                   
                </div>
			</DIV>
		</DIV>
	</DIV>
				
	<#include "/map/arcgis/standardmappage/pagination.ftl"/>
	
<script type="text/javascript">
	$(document).ready(function(){
		page(0);
		var winHeight=window.parent.document.getElementById('map0').offsetHeight-32-38-35-28;
       	$("#gridTbodyId").height(winHeight);
	});
	
	var results="";//获取定位对象集合
	function page(i){
     	results="";
		var platformName = $('#platformName').val();
		if(platformName=="==输入查询内容==") platformName="";
		var pageSize = $("#pageSize").val();
		var pageNo;
		var totalPage=$('#totalPage').text();
		if(i!=-2){
			if(i==0){
				pageNo = 1;
			} else if(i==-3) {
				if(totalPage=="") return;
				if(parseInt(totalPage) == 0)
					return;
				pageNo = totalPage;
			} else {
				pageNo = $('#curPage').text();
				if(parseInt(totalPage) == 0)
					return;
				if(pageNo==totalPage && i>0)
					return;
				if(pageNo==1 && i<0)
					return;
				pageNo = parseInt(pageNo)+i;
			}
		} else {
			pageNo = $('#toPage').val();
			var reg = /^[1-9]+[0-9]*]*$/;
			if(!reg.test(pageNo))
				return;
			pageNo = parseInt(pageNo);
			if(pageNo>totalPage)
				return;
			if(pageNo<1)
				return;
		}
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/theme/arcgis/standardmappage_putian/images/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	var orgCode = ${orgCode};
		var postData = 'page='+pageNo+'&rows='+pageSize+'&orgCode='+orgCode+'&platformName='+platformName;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesListData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				$('#curPage').text(pageNo);
				var totalPage = Math.floor(data.totalCount/pageSize);
				if(data.totalCount%pageSize>0) totalPage+=1;
				$('#totalPage').text(totalPage);
				var list=data.list;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						var val=list[i];
						var imgurl="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/images/gisunselected/flag_qqy_001.png";
						var imgurlon=imgurl.replace("gisunselected","gisselected");
						var imgid = "qqy"+val.monitorId;
						tableBody+=' <ul onClick="selected(\''+val.monitorId+'\',\''+(val.platformName==null?'':val.platformName)+'\')">';
						tableBody+="<div onmouseover='styleonmouseoper(\""+imgid +"\",\""+imgurlon +"\")' onmouseout='styleonmouseoper(\""+imgid +"\",\""+imgurl +"\")'>";
                        tableBody+='    <li >'+(val.platformName==null?'':val.platformName)+'&nbsp;&nbsp;</li>';
                        tableBody+="</div>";
                        tableBody+=' </ul>';
						results=results+","+val.monitorId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<tr style="height: 100%"><td align="center" style="color:red;">未查到相关数据！</td></tr>';
				}
				$("#listtable").html(tableBody);
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<tr style="height: 100%"><td align="center" style="color:red;">数据读取错误！</td></tr>';
				$("#listtable").html(tableBody);
			}
		});
	}
	
	function selected(id, name){
		var arrangeId = id;
		var name =name;
		
		gisPosition(id);
		
		setTimeout(function() {
			window.parent.locationGlobalEyesPoint(id);
		},1000);
		
		//window.parent.locationGlobalEyesPoint(id);
	}
	//地图定位
	function gisPosition(res){
		if (res==""){
			return ;
		}
		
		var qqyurl= "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
		window.parent.getArcgisDataOfGlobalEyes(qqyurl);
	}
	function styleonmouseoper(id,imgurl) {
		styleonmouseoper(id,imgurl);
	}
	
	var flagimgid = "";
	
	function styleonmouseoper(id,imgurl) {
		var idObj = document.getElementById(id);
		if(idObj != null && flagimgid != id) {
			idObj.src = imgurl;
		}
	}
</script>
</body>
</html>