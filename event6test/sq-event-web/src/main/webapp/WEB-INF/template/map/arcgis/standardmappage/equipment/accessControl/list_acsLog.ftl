<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>立体防控——门禁设备日志</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<link  rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "toolbar_acsLog.ftl" />
</div>

<div id="acsLogDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<div id="operateMask" class="MarskLayDiv hide" style="z-index: 11;" onclick="closeAcsLogImgDiv();"></div>
<div id="acsLogImgDiv" class="clear PopDiv hide" style="z-index:12; top: 48px; width: 400px;"></div>

<script type="text/javascript">
	var $imgArray = null;
	
	$(function(){
		loadDataList();
	})
	
	function loadDataList() {
		$('#list').datagrid({
			width:600,
			height:600,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,//让列宽自适应窗口宽度
			singleSelect: true,
			idField:'logId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listAcsLogData.json',
			columns:[[
				{field:'logId',checkbox:true,width:40,hidden:'true'},
				{field:'eqpId',checkbox:true,width:40,hidden:'true'},
				{field:'rsName',title:'住户名', align:'left',width:fixWidth(0.2),sortable:true, formatter:function(value, rec, index){
					var formatName = value;
					
					if(rec.imgUrl) {
						formatName = '<a href="#" onclick="openAcsLogImgDiv(\''+ rec.imgUrl +'\');" ><img title="查看门禁抓拍" src="${rc.getContextPath()}/images/attr_flag_pic.png" style="margin: 0 10px 3px 0; *margin: 0 10px 0 0;"/></a>'+ value;
					}
					
					return formatName;
				}},
				{field:'rsIdcard',title:'公民身份号码',align:'center',width:fixWidth(0.15),sortable:true},
				{field:'roomNo',title:'房号',align:'center',width:fixWidth(0.1),sortable:true},
                {field:'acqTime',title:'刷卡时间',align:'center',width:fixWidth(0.15),sortable:true, formatter:function(value, rec, index){
                	var formatDate = "";
                	
                	if(value) {
                		var time = new Date(value),
                			year = time.getFullYear(),
							month = time.getMonth()+1,
							day = time.getDate(),
							hour = time.getHours(),
							min = time.getMinutes(),
							second = time.getSeconds();
						
						if(month < 10) {
							month = "0"+month;
						}
						if(day < 10) {
							day = "0"+day;
						}
						if(hour < 10) {
							hour = "0"+hour;
						}
						if(min < 10) {
							min = "0"+min;
						}
						if(second < 10) {
							second = "0"+second;
						}
						
						formatDate = year+"-"+month+"-"+day+" "+hour+":"+min+":"+second;
                	}
                	
                	return formatDate;
                }},
                {field:'swipeMode',title:'开门方式',align:'center',width:fixWidth(0.1),sortable:true, formatter:function(value, rec, index){
                	var valName = "";
                	switch(value) {
                		case "0": {
                			valName = "无效"; break;
                		}
                		case "1": {
                			valName = "IC/ID卡"; break;
                		}
                		case "2": {
                			valName = "二代证"; break;
                		}
                		case "3": {
                			valName = "居民卡"; break;
                		}
                		case "7": {
                			valName = "室内机开锁"; break;
                		}
                		case "8": {
                			valName = "管理中心开锁"; break;
                		}
                	}
                	return valName;
                }},
                {field:'inoutMode',title:'出门方式',align:'center',width:fixWidth(0.08),sortable:true, formatter:function(value, rec, index){
                	var valName = "";
                	if(value == '1') {
                		valName = "进门";
                	} else if(value == '2') {
                		valName = "出门";
                	}
                	return valName;
                }},
                {field:'acsStatus',title:'刷卡状态',align:'center',width:fixWidth(0.1),sortable:true, formatter:function(value, rec, index){
                	return "正常";
                }}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{bizId: $("#bizId").val()},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			}
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		try{
			closeCustomJqueryWindow();
		}catch(e){}
		DivShow(result);
		searchData();
	}
	
	//初始化图片轮播
	function initNbspSlider(){
		$("#acsLogImg").nbspSlider({
			widths:         "400px",        // 幻灯片宽度
			heights:        "280px",
			effect:	         "vertical",
			numBtnSty:       "square",
			speeds:          300,
			autoplay:       1,
			delays:         4000,
			preNexBtnShow:   0,
			altOpa:         0.5,            // ALT区块透明度
			altBgColor:     '#ccc',         // ALT区块背景颜色
			altHeight:      '20px',         // ALT区块高度
			altShow:         1,             // ALT区块是否显示(1为是0为否)
			altFontColor:    '#000',        // ALT区块内的字体颜色
			prevId: 		'prevBtn',      // 上一张幻灯片按钮ID
			nextId: 		'nextBtn'		// 下一张幻灯片按钮ID
		});
	}
	
	function changeImgLi(imgUrl) {
		$("#acsLogImg").remove();
		$imgArray = new Array();
		var trHtmlImg = "";
		
		$imgArray.push("data:image/jpg;base64&#44;" + imgUrl);
		
		//针对IE的Hack
		//font-size:245px 约为高度的0.873 280*0.873约为245px
		//font-family:Arial; 防止非utf-8引起的hack失效问题，如gbk编码 
		for(var index in $imgArray) {
			trHtmlImg += '<li style="cursor: pointer; *font-family:Arial; *font-size: 245px;"><a onclick="showImg('+ index +'); "><img class="pic" style="vertical-align:middle;" alt="" onload="AutoResizeImage(380,280,this)" src="'+ $imgArray[index] +'" /></a></li>';
		}
		
		var trHtml = 
			'<div id="acsLogImg">'+
				'<ul>'+ trHtmlImg +'</ul>'+
			'</div>';
		
		var trHtmlObj = $(trHtml).appendTo($("#acsLogImgDiv"));
		$.parser.parse(trHtmlObj);//渲染新加入的部分
		
		initNbspSlider();
	}
	
	var $windowObj = null;
	function showImg(index) {
		var picHtml = 
					'<html>'+
						'<head>'+
							'<title>图片查看</title>'+
						'</head>'+
						'<body>'+
							'<div style="text-align: center;"><img src="'+ $imgArray[index] +'" /></div>'+
						'</body>'+
					'</html>';
					  
		if(!$windowObj || $windowObj.closed) {
			$windowObj = window.open('about:blank');
		} else {
			//chrome下每添加重载一次，title会多一次，但无明显的异常表现，可删除后，再次添加；
			//IE兼容模式下，删除title操作，会导致页面运行异常，直至页面关闭
			$($windowObj.document.getElementsByTagName("div")).remove();
		}
		
		$windowObj.document.write(picHtml);
		$windowObj.focus();//chrome有效；IE无效
	}
	
	function showImg_(index) {
		var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=acsLogImg&index="+index;
		
		openPostWindow(url, $imgArray, "图片查看");
	}

	function openPostWindow(url, data, name){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target=name;
		
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow(name);
		});
		
		document.body.appendChild(tempForm);
		
		if (tempForm.fireEvent) { 
            tempForm.fireEvent('onsubmit'); 
            tempForm.submit(); 
        } else if (document.createEvent) {
            //DOM2 fire event
            var ev = document.createEvent('HTMLEvents'); 
            ev.initEvent('submit', false, true); 
            tempForm.dispatchEvent(ev); 
        } 
        
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}
	
	//打开图片展示窗口
	function openAcsLogImgDiv(imgUrl) {
		changeImgLi(imgUrl);
		$("#operateMask").show();
		$("#acsLogImgDiv").show();
	}
	
	//关闭图片展示窗口
	function closeAcsLogImgDiv() {
		$("#acsLogImgDiv").hide();
		$("#operateMask").hide();
	}
</script>
</body>
</html>