<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>人员列表</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<script src="${rc.getContextPath()}/js/layer/3.1.1/layer.js"></script>
	<style type="text/css">
		.showRecords{width:260px; height:26px; text-align:left; color:#000; line-height:26px; border-bottom:1px solid #d8d8d8; background:#f2f2f2;}
		.showRecords ul li{padding:5px 0px !important;text-align:center;ine-height:26px;float: left;width: auto}
		.showRecords ul li {
			padding: 4px !important;
		}
	</style>
</head>
<body style="border:none;">
<input type="hidden" id="gridId" value="${gridId?c}" />
<input type="hidden" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
<input type="hidden" id="type" value="<#if type??>${type}</#if>" />
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
<input type="hidden" id="pageSize" value="20" />
<div class="" style="display:block;">
	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
	<div class="ListSearch">
		<div class="condition">
			<ul>
				<li class="LC1">排序方式：</li>
				<li class="LC2">
					<select name="order" id="order" class="sel1">
						<option value="">无</option>
						<option value="updateTime">更新时间</option>
						<option value="name">姓名</option>
					</select>
				</li>
			</ul>
			<ul>
				<li class="LC1">输入姓名：</li>
				<li class="LC2"><input id="name" name="name" type="text" class="inp1" /></li>
			</ul>
			<ul>
				<li class="LC1">证件号码：</li>
				<li class="LC2"><input id="idCard" name="idCard" type="text" class="inp1" /></li>
			</ul>
			<ul>
				<li class="LC1">人员类型：</li>
				<li class="LC2">
					<select id="_selMitype" name="mitype" class="sel1 easyui-validatebox" data-options="required:true" editable="false" style="width:161px;">
						<option value="">-请选择-</option>
						<#list typeDC as e>
							<option value="${e.dictGeneralCode}">${e.dictName}</option>
						</#list>
					</select>
				</li>
			</ul>
			<ul>
				<li class="LC1">&nbsp;</li>
				<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
			</ul>
			<div class="clear"></div>
		</div>
		<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
	</div>
	<div class="showRecords">
		<ul>
			<li><a href="#" onclick="javascrpit:loadMessage(1,$('#pageSize').val());"><span id="allVisit">全部</span></a></li>
		</ul>
		<ul>
			<li><a href="#" onclick="javascrpit:loadMessage(1,$('#pageSize').val(),'visit',);"><span id="isVisit">已走访</span></a></li>
		</ul>
		<ul>
			<li><a href="#" onclick="javascrpit:loadMessage(1,$('#pageSize').val(),'noVisit');"><span id="noVisit">未走访</span></a></li>
		</ul>
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
	}
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	$(document).ready(function(){
		var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
		$("#content").height(winHeight-56);
		loadMessage(1,$("#pageSize").val());
	});
	var results="";//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		pepLocalMap=[];
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var gridId = $('#gridId').val();
		var mitype = $('#_selMitype').val();
		var name = $("#name").val();
		if(name=="==输入姓名==") name="";
		var order = $("#order option:selected").val();
		var idCard = $("#idCard").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
				background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = '';
		postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&mitype='+mitype+'&name='+name+'&order='+order+'&idCard='+idCard+'&type='+$('#type').val()+'&elementsCollectionStr='+$('#elementsCollectionStr').val();
		if(searchType==null){
			$(".showRecords ul li a span").css("color","");
			$("#allVisit").css("color","red");
		}
		if('visit'==searchType){
			$(".showRecords ul li a span").css("color","");
			$("#isVisit").css("color","red");
			postData+='&isVisit=1';
		}
		if ('noVisit'==searchType){
			$(".showRecords ul li a span").css("color","");
			$("#noVisit").css("color","red");
			postData+='&isVisit=0';
		}
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeopleYouth.json',
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo > 1) {
					data.total = parseInt($('#records').text());
				}
				var total = (data==null?0:data.total);
				$('#records').text(total);
				var totalPage = Math.floor(total/pageSize);
				if(total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=(data==null?null:data.rows);
				function sortprice(a,b){
					return b.isVisit-a.isVisit;
				}
				if(list!=null){
					list.sort(sortprice);
				}
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						var val=list[i];
						var mitypeStr = val.mitypeStr;
						var mitype = val.mitype;
						var gender = val.gender;
						var isVisit = val.isVisit;

						var name = "";
						if(typeof val.partyName != 'undefined' && val.partyName != null && val.partyName != ""){
							name = val.partyName;
						}else if(typeof val.name != 'undefined' && val.name != null && val.name != ""){
							name = val.name;
						}
						var imageGenderSpan="";
						if(gender==null){
							gender = "";
						}else if (gender == 'M' || gender == '男'||gender == '男性'||gender == '1') {
							imageGenderSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/man.png">';
						}else {
							imageGenderSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/woman.png">';
						}
// 					  imageGenderSpan += '&nbsp;';
						if(mitype=='1'){
							imageGenderSpan += '<img title="'+mitypeStr+'" style="width:12px;height:12px;margin-left:3px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/youth_xiansan.png">';
						}else if(mitype=='2'){
							imageGenderSpan += '<img title="'+mitypeStr+'" style="width:12px;height:12px;margin-left:3px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/youth_buliang.png">';
						}else if(mitype=='3'){
							imageGenderSpan += '<img title="'+mitypeStr+'" style="width:12px;height:12px;margin-left:3px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/youth_liulang.png">';
						}else if(mitype=='4'){
							imageGenderSpan += '<img title="'+mitypeStr+'" style="width:12px;height:12px;margin-left:3px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/youth_fuxing.png">';
						}else if(mitype=='5'){
							imageGenderSpan += '<img title="'+mitypeStr+'" style="width:12px;height:12px;margin-left:3px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/youth_nongcun.png">';
						}

						var imageVisitSpan="";
						if(isVisit=="1"){
							imageVisitSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/isVisit.png">';
						}else{
							imageVisitSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/noVisit.png">';
						}

						tableBody+='<dl onclick="selected(\''+val.partyId+'\')">';
						tableBody+='<dt>';
						tableBody+='<span class="fr">'+(val.birthday==null ? '' : val.birthday)+'</span>';
						tableBody+='<b class="FontDarkBlue" id="pepList_'+val.partyId+'">'+imageVisitSpan+'&nbsp;'+(name==null?'':name)+'&nbsp;'+imageGenderSpan+'</b>';
						tableBody+='</dt>';
						tableBody+='<dd>'
								+(val.identityCard==null?'':'<img src="${uiDomain!''}/images/map/gisv0/special_config/images/people_03.png">'+val.identityCard)+
								'</dd>';
						tableBody+='</dl>';
						results=results+","+val.partyId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<ul>未查到相关数据！！</ul>';
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
				pagenum = inputNum
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

	/**
	 * 加载人员定位后的回调函数
	 * @param data
	 */
	var pepLocalMap = [];
	function loadPeopleLocateCallBack(data) {
		if(data != null && data.length >0){
			for (var i=0;i<data.length;i++){
				pepLocalMap.push({
					"ciRsId" : data[i].ciRsId,
					"partyId" : data[i].partyId,
					"name" : data[i].name,
					"address" : data[i].address,
					"wid" : data[i].wid
				});
			}
		}
	}

	var ss;

	function selected(id){
		var wid = id;
		if(pepLocalMap != null){
			var pepAddressArr = [];
			var pepAddrHTML = "<dd>请选择地址（现居住地址）：</dd>";
			for(var i=0; i< pepLocalMap.length;i++){
				if(id == pepLocalMap[i].partyId){
					pepAddressArr.push(pepLocalMap[i]);
					pepAddrHTML += "<dd><a href='#' onclick='showPepDetail(\""+pepLocalMap[i].wid+"\")'>"+ pepLocalMap[i].address+"</a></dd>"
				}
			}
			pepAddrHTML = "<div id='pepAddrList' style='max-height: 300px;overflow-y:auto'><dl>"+pepAddrHTML+"</dl></div>";
			if(pepAddressArr != null && pepAddressArr.length >1){
				layer.tips(pepAddrHTML, "#pepList_"+id+"", {
					tips: [1, 'rgba(120, 186, 50, 1)'], //还可配置颜色
					area: '240px',
					time: 0,
					shade: 0.1,
					shadeClose: true
				});
			}else{
				if(pepAddressArr != null && pepAddressArr.length == 1){
					wid = pepAddressArr[0].wid;
				}
				showPepDetail(wid);
			}
		}
	}


	function showPepDetail(wid){

		var gridId = $('#gridId').val();
		setTimeout(function() {
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400,236,wid,gridId)
		},1000);
	}
	//--定位
	function gisPosition(res){
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}

		if(res==""){
			return;
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?userIds="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
		window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400,236, null, null, null, null, null, undefined, loadPeopleLocateCallBack);
	}
</script>
</body>
</html>