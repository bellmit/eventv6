<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>应急预案</title>
<#include "/component/commonFiles-1.1.ftl" />

<link rel="stylesheet" href="${rc.getContextPath()}/js/leftMenu/dist/font-awesome.min.css">
<link rel="stylesheet" href="${rc.getContextPath()}/js/leftMenu/dist/sidebar-menu.css">
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/leftMenu/dist/sidebar-menu.js"></script>

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/bootstrap/bootstrap.min.css" />
<script type="text/javascript" src="${uiDomain!''}/js/jqPaginator.js" charset="GBK"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/layer/layer.js"></script>
<style>
body{ font-size:14px; font-family:"微软雅黑"; color:#666; }
h2,h3,h4,p,ul,li,body{padding:0; margin:0;}
ul,li{ list-style:none;}
a{ text-decoration:none; color:#666;}
a:hover{ color:#ff3333;}
.speleft{ float:left; width:18%; background:#f9f9f9; min-height:500px; padding:0 1%;}
.sperigt{margin-left:18%;}
.sperigt h2{ font-size:18px; font-weight:normal; height:42px;padding:0px 5px;border-bottom:1px solid #e5e5e5; position:relative;}
.spetit{ display:inline-block; border-bottom:2px solid #0066cc; position:absolute; left:0; top:0; height:41px; line-height:41px;}
.sperigt ul li{border-bottom:1px solid #e5e5e5;padding:10px 5px;}
.spedate{ float:right; color:#999;}
.w330{width: 330px;}

img{ border:none;}
.zcsearch{background:#fff; border:1px solid #d8d8d8;margin:10px auto; height:30px; border-radius:20px; width:45%; position:relative;}
.zc-txt{border:none; border-radius:20px; margin:0 15px; height:28px; line-height:28px; color:#999;width:94%;}
.zc-btn-search{background:url(${rc.getContextPath()}/cs/zzjc_icon1.png) no-repeat; width:15px; height:15px; display:inline-block; position:absolute; top:7px; right:12px;}
.zccon{ width:94%; margin:10px auto; border:1px solid #e5e5e5; padding:0px 10px 10px 10px;}
.zccon-tit{ border-bottom:2px solid #e5e5e5; height:50px; line-height:50px;}
.zccon-tit ul li{ float:left; color:#e5e5e5;}
.zccon-tit ul li a{ font-size:16px;padding:0 20px; display:inline-block; height:50px; line-height:50px;}
.zccon-tit ul li a.zcurrent{color:#2ba0f6; border-bottom:2px solid #2ba0f6;}
.zccon-list ul li{ border-bottom:1px solid #e5e5e5; padding:15px 0;}
.zc-date{ float:right; color:#bbb;}

.main-sidebar{
	position: absolute;
	top: 0;
	left: 0;
	height: 100%;
	min-height: 100%;
	width: 18%;
	z-index: 810;
	background-color: #1561ad;
}
</style>
</head>

<body>
<div class="speMainer">
	<input type="hidden" name="catalogCode" id="catalogCode" value=""  />
	<div id="leftList" class="speleft" style="height:300px;overflow:auto;">
	<aside class="main-sidebar">
		<section class="sidebar">
			<ul class="sidebar-menu"></ul>
		</section>
	</aside>
	</div>
	
	<div class="sperigt">
	    <h2>
		    <span id="title" class="spetit" style="margin-left:3%;"></span>
			<div class="zcsearch">
				<input id="keyWord" class="zc-txt" type="text" value="请输入您要查询文件名称" onfocus="if(this.value=='请输入您要查询文件名称'){this.value='';}" onblur="if(this.value==''){$(this).attr('style','color:gray;border:none;');this.value='请输入您要查询文件名称';}" onkeydown="_onkeydown();" />
				<a href="#" onclick="loadMessageA(1, '${subClassPcode}')" class="zc-btn-search"></a>
			</div>
		</h2>
		<div style="height:300px;overflow:auto;">
	        <ul id="rightList"></ul>
   		</div>
   		<div style="text-align:center; position: fixed; bottom:0px; width: 87%;background-color:#FBFBFB;border-top:1px dashed #e4e4e4;">
			<ul class="pagination" id="pagination1" style="margin:10px;"></ul>
  		</div>
	</div>
</div>
</body>

<script type="text/javascript">

var orgCode = "${orgCode!''}";
var pageSize = 6;
var isFirst = true;

var dictNameStr = "";
var dictNameArray = new Array();
var dictCodeStr = "";
var dictCodeArray = new Array();

$(function(){

	$("#catalogCode").val("${subClassPcode!''}");
	
	<#if dictNameList??>
		<#if (dictNameList?size>0)>
			<#list dictNameList as val>
				dictNameStr += "${val}"+",";
			</#list>
		</#if>
	</#if> 
	dictNameArray = dictNameStr.split(",");
	
	<#if dictCodeList??>
		<#if (dictCodeList?size>0)>
			<#list dictCodeList as val>
				dictCodeStr += "${val}"+",";
			</#list>
		</#if>
	</#if> 
	dictCodeArray = dictCodeStr.split(",");
	
	for(var i=0; i<dictNameArray.length-1; i++){
	
		var dictName = dictNameArray[i].split("/");
		var dictCode = dictCodeArray[i].split("/");
		if(dictName.length-1>1){
			var str = '';
			for(var j=0; j<dictName.length-1; j++){
				if(j==0){
					str='<li class="treeview"><a href="#" ><i class="fa fa-dashboard"></i><span onclick="loadMessageA(1,\''+dictCode[0]+'\')" >'+dictName[j]+'</span><i class="fa fa-angle-left pull-right"></i></a><ul class="treeview-menu">';	
					j++;
				}
				str+='<li><a href="#" onclick="loadMessageA(1,\''+dictCode[j]+'\')" ><i class="fa fa-circle-o"></i>'+dictName[j]+'</a></li>';
			}
			str+='</ul></li>';
			$(".sidebar-menu").append(str);
		}else{
			$(".sidebar-menu").append('<li class="treeview"><a href="#" onclick="loadMessageA(1,\''+dictCode[0]+'\')" ><i class="fa fa-dashboard"></i><span>'+dictName[0]+'</span></a></li>');
		}
	}
	
	$.sidebarMenu($('.sidebar-menu'))

	$.jqPaginator('#pagination1', {
        totalPages: 1,
        visiblePages: 6,
        currentPage: 1,
        onPageChange: function (num, type) {
        	if (!isFirst) loadMessageA(num, $("#catalogCode").val());
        	isFirst = false;
        }
    });

	loadMessageA(1, $("#catalogCode").val());

});

function _onkeydown() {
	var keyCode = event.keyCode;
	if(keyCode == 13) {
		loadMessageA(1, "${subClassPcode!''}");
	}
}

function loadMessageA(pageNo, catalogCode) {
	layer.load(0);
	$("#catalogCode").val(catalogCode);
	getList(pageNo, pageSize, catalogCode, function() {
		layer.closeAll('loading');
	});
}

function getList(pageNo, pageSize, catalogCode, completeFn){
	
	if(catalogCode!=""){		
		var postData = {};
		postData["catalogId"] = "${catalogId!''}";
		if (catalogCode != null && catalogCode != '') {
			postData["catalogCode"] = catalogCode;
		}
		postData["orgCode"] = orgCode;
		postData["subClassPcode"] = "${subClassPcode!''}";//应急预案字典
		postData["page"] = pageNo;
		postData["rows"] = pageSize;
		postData["status"] = "2";
		var keyWord = $("#keyWord").val();
		if (keyWord == "请输入您要查询文件名称") keyWord = "";
		if (keyWord != null && keyWord != "") postData["keyWord"] = keyWord;
		
		$.ajax({
	        type: "POST",
	        url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/releaseList.json",
	        data: postData,
	        dataType: "json",
	        success: function(data){
	        	if(data != null){
	        		$("#rightList").empty();
	        		
	        		var totalPage = Math.floor(data.total / pageSize);
					if (data.total % pageSize > 0) totalPage += 1;
					if (totalPage == 0) totalPage += 1;
					$('#pagination1').jqPaginator('option', {
					    totalPages: totalPage,
					    currentPage: pageNo
					});
	        		
	        		var num = data.rows.length;
	        		if(num>0){
	        			$("#title").text("应急预案");
		        		for(var i=0; i<num; i++){
		        			var obj = data.rows[i];
		        			var title = obj.title;
		        			var time = obj.pubDate;
		        			if(title && title.length > 32) {
					            title = title.substr(0, 32); 
					        }
		        			$("#rightList").append('<li><a style="margin-left:3%;" href="#" onclick="showRowDetail('+obj.infoOpenId+')"><span class="w330">'+title+'</span><span class="spedate">'+time+'</span></a></li>');
		        		}
	        		}else{
	        			$("#title").text("应急预案");
						$("#rightList").append('<div id="_nodatapic" style="margin: 30px auto 0; width: 288px;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
	        		}
	        	}
	        	if (typeof completeFn == "function") completeFn.call(this);
	        },
			error:function(xhr,status,error) {
				if (typeof completeFn == "function") completeFn.call(this);
			}
		});
	}else{
		$("#rightList").append('<div id="_nodatapic" style="margin: 30px auto 0; width: 288px;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
		if (typeof completeFn == "function") completeFn.call(this);
	}
}

function showRowDetail(infoOpenId) {
	var height = 400;
	var width = 850;
	var title = "应急预案详情";
	var url = "${SQ_ZZGRID_URL}/zzgl/grid/knowledgeLibrary/homeShow.jhtml?infoOpenId="+infoOpenId;
	window.parent.showMaxJqueryWindow(title,url,width,height); 
}

</script>
</html>
