<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼房名称详情</title>
<!--link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" /-->
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/ui/css/normal.css"/>
<link href="${rc.getContextPath()}/theme/standardfordetail/css/add_people.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jquery-easyui-1.4/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jquery-easyui-1.4/themes/icon.css">
<link href="${rc.getContextPath()}/ui/css/easyuiExtend.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

<link rel="stylesheet" href="${rc.getContextPath()}/ui/css/jquery.mCustomScrollbar.css">
<script src="${rc.getContextPath()}/ui/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/ui/js/function.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/base/getDictionaryListByConfig.jhtml?var=dictionaryData&bid=resident"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/global.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/module/resident/resident.js"></script>

</head>
<body>
<div class="OpenWindow Width900">
    <div class="MetterList">
    	<div class="box">
        	<div class="MetterContent">
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                            <li class="current">楼宇简介</li>
                            <li id="buildLi">楼宇结构</li>
                            <li id="placeLi">场所信息</li>
                            <li id="corLi">企业信息</li>
                            <li id="cirLi">人员信息</li>
                        </ul>
                    </div>
                    <div class="ListShow">
                    	<!-----------------------------------楼宇简介-------------------------------------->
                        <div class="tabs2">
                        	<div class="LyIntroduce">
                            	<div class="LyPhoto fl">
                                    <div class="img_list">
                                        <ul>
                                        	<li>
                                        		<a href="#">
													<img src="${rc.getContextPath()}/images/notbuilding.gif" alt=""/>
												</a>
											</li>
                                        </ul>
                                    </div>
                                    <!--
                                    <a href="#" id="toLeft" class="link toLeft"></a>
                                    <a href="#" id="toRight" class="link toRight"></a>-->
                                </div>
                                <div class="LyDetail fr">
                                	<div class="address"><span>${record.buildingName!''}</span></div>
                                	<#if record.buildingAddress??><p>${record.buildingAddress}&nbsp;</p></#if>
                                    <div class="fl">
                                    	<ul>
                                        	<li>管理单位：<span><#if record.managementCompany??>${record.managementCompany}</#if></span></li>
                                        	<li>使用性质：<span>${record.useNatureLabel!''}</span></li>
                                        	<li>建筑年份：<span><#if record.buildingYear??>${record.buildingYear?c}年</#if></span></li>
                                        	<li>地下楼层：<span><#if record.undergroundFloorNum??>${record.undergroundFloorNum?c}层</#if></span></li>
                                        	<li>占地面积：<span><#if record.area??>${record.area?c}平方米</#if></span></li>
                                        	<li>楼层总数：<span><#if record.buildingFloor??>${record.buildingFloor?c}层</#if></span></li>
                                        	<li>地面楼层：<span><#if record.groundFloorNum??>${record.groundFloorNum?c}层</#if></span></li>
                                        	<li>楼高：<span><#if record.hight??>${record.hight?c}米</#if></span></li>
                                        </ul>
                                    </div>
                                    <div class="fr">
                                        <dl>
                                            <dt><img src="${rc.getContextPath()}/theme/standardfordetail/images/earth_13.png" /></dt>
                                            <dd>现有人口总数</dd>
                                            <dd class="FontOrange"><b><#if cirCount??>${cirCount}</#if>人</b></dd>
                                        </dl>
                                        <dl>
                                            <dt><img src="${rc.getContextPath()}/theme/standardfordetail/images/earth_24.png" /></dt>
                                            <dd>场所</dd>
                                            <dd class="FontPurple"><b><#if placeCount??>${placeCount}</#if>个</b></dd>
                                        </dl>
                                        <dl>
                                            <dt><img src="${rc.getContextPath()}/theme/standardfordetail/images/earth_25.png" /></dt>
                                            <dd>企业</dd>
                                            <dd class="FontBlue"><b><#if corCount??>${corCount}</#if>个</b></dd>
                                        </dl>
                                        <dl>
                                            <dt><img src="${rc.getContextPath()}/theme/standardfordetail/images/earth_21.png" /></dt>
                                            <dd>出租屋</dd>
                                            <dd class="FontGreen"><b>${rentRoomCount}间</b></dd>
                                        </dl>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    	<!-----------------------------------楼宇结构-------------------------------------->
                        <div class="tabs2 hide">
                        	<div class="building fl">
                            	<iframe id="build" scrolling="no" frameborder='0'  src='' style='width:690px;height:390px;'></iframe>
                           	</div>
                           	
                            <div class="remarks fr">
                            	<ul class="CutLine">
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_09.png" />出租</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_07.png" />自用</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_11.png" />出售</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_13.png" />空置</li>
                                </ul>
                            	<ul class="CutLine">
                                	<!--<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_29.png" />党员</li>-->
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_36.png" />服务人员</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_38.png" />监管人员</li>
                                </ul>
                            	<ul>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_40.png" />放心户</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_42.png" />一般户</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_44.png" />不放心户</li>
                                	<li><img src="${rc.getContextPath()}/theme/standardfordetail/images/ly_46.png" />重点管控</li>
                                </ul>
                            </div>
                            <div class="clear"></div>
                        </div>
                    	<!-----------------------------------场所信息-------------------------------------->
                        <div class="tabs2 hide" style="margin:0;height:370px;width:100%;overflow:auto">
							<table id="placeInfoList" border="false"></table>
                        </div>
                    	<!-----------------------------------企业信息-------------------------------------->
                        <div class="tabs2 hide" style="margin:0;height:370px;width:100%;overflow:auto">
							<table id="corBaseInfoList" border="false"></table>
                        </div>
                    	<!-----------------------------------人员信息-------------------------------------->
                        <div class="tabs2 hide" style="margin:0;height:370px;width:100%;">
                        	<div class="LyPeople">
                            	<div class="search">
                                	<ul>
                                    	<li><input name="" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='请输入姓名进行查询';" onfocus="if(this.value=='请输入姓名进行查询')value='';" value="请输入姓名进行查询" style="width:380px; padding-left:5px; color:#868686;" /></li>
                                    	<li class="chaxun" onclick="searchAll();">查询</li>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                                <div class="type">
                                	<div class="TypeName fl">人员类型：</div>
                                    <ul class="xz">
                                    	<#if typeDC??>
                                    		<li id="" value="" class="current">全部</li>
											<#list typeDC as l>
												<li id="${l.COLUMN_VALUE}" value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</li>
											</#list>
									    </#if>
                                    </ul>
                                    <div class="clear h_10"></div>
                                	<div class="TypeName fl">人群分类：</div>
                                	<ul class="gl">
                                        <li id="" value="" class="current">全部</li>
                                        <li id="001" value="001">吸毒人员</li>
                                        <li id="002" value="002">矫正人员</li>
                                        <li id="003" value="003">上访人员</li>
                                        <li id="004" value="004">刑释解教人员</li>
                                        <li id="005" value="005">危险品从业员</li>
                                        <li id="006" value="006">重精神病人员</li>
                                        <li id="007" value="007">邪教人员</li>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                                <div class="h_10"></div>
                                <div style="margin:0;height:225px;width:100%">
                                	<table id="cirsInfoList" border="false"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

	<script>window.jQuery || document.write('<script src="${rc.getContextPath()}/theme/standardfordetail/js/minified/jquery-1.11.0.min.js"><\/script>')</script>
	
	<!-- custom scrollbar plugin -->
	<script src="${rc.getContextPath()}/theme/standardfordetail/js/jquery.mCustomScrollbar.concat.min.js"></script>
	
	<script type="text/javascript">
		// 楼层房间滚动
		var $slider_width = $('.con ul li').width(); // 每个房间宽度
		var floorWidth = $('.con').width(); // 层的宽度
		var floorLeft = $('.con').offset().left; // 层的最左侧
		var floorRight = $('.con').offset().left + floorWidth; // 层的最右侧
		
		var flag = 0;
		
		// 向右切换
		function ne(item) {
			var $slider = $('#' + item + ' ul'); // 当前层
			
			if ($slider.is(":animated")) {
				return;
			};
			
			var lastLi = $('#' + item + ' ul li').last(); // 最后一间房间
			var lastLiLeft = lastLi.offset().left;
			
			if (lastLiLeft <= 576) {
				$slider.animate({left: '0px'}, 'slow');
			} else {
				$slider.animate({left: '-=' + (floorWidth) + 'px'}, 'slow');
			}
		}
		
		// 向左切换
		function pre(item) {
			var $slider = $('#' + item + ' ul');
			
			if ($slider.is(":animated")) {
				return;
			};
			
			var liLength = $('#' + item + ' ul li').length; // 当前层总房间数
			var page_count = Math.ceil(liLength / 6); // 总共多少屏
			
			var firstLi = $('#' + item + ' ul li').first();
			var firstLiLeft = firstLi.offset().left;
			
			if (firstLiLeft >= 76) {
				$slider.animate({left: '-=' + floorWidth * (page_count - 1) + 'px'}, 'slow');
			} else {
				$slider.animate({left: '+=' + (floorWidth) + 'px'}, 'slow');
			}
		}
	</script>
	
	<script>
	//滚动条特效
	(function($){
		$(window).load(function(){
			
			$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
			$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
			
			
			$("#content-d").mCustomScrollbar({theme:"dark"});
			
			$("#content-md").mCustomScrollbar({theme:"minimal-dark"});
		});
	})(jQuery);
	
	//选项卡切换
	var $NavDiv2 = $(".ConList ul li");
		$NavDiv2.click(function(){
			  $(this).addClass("current").siblings().removeClass("current");
			  var NavIndex2 = $NavDiv2.index(this);
			  $(".ListShow .tabs2").eq(NavIndex2).show().siblings().hide();
   	});
   	
	//图片切换
    var index = 0;
	var timer = 0;
	var ulist = $('.img_list ul');
	var list = ulist.find('li');
	var llength = list.length;//li的个数，用来做边缘判断
	var lwidth = $(list[0]).width();//每个li的长度，ul每次移动的距离
	var uwidth = llength * lwidth;//ul的总宽度
	
	// 楼宇图片
	function init(){
		//生成按钮(可以隐藏)
		addBtn(llength);
		//显示隐藏左右点击开关
		$('.link').css('display', 'block');
		$('.link').bind('click', function(event) {
			var elm = $(event.target);
			doMove(elm.attr('id'));
			return false;
		});

		auto();
	}

	function auto(){
		//定时器
		timer = setInterval("doMove('toRight')",3000);

		$('.img_list li').hover(function() {
			clearInterval(timer);
		}, function() {
			timer = setInterval("doMove('toRight')",3000);
		});
	}
	

	function addBtn (length){
		for (var i = 0; i < length; i++) {
			var list = $('<li></li>').text(i+1);
		};
	}

	function doMove(direction){
		//向右按钮
		if (direction =="toRight") {
			index++;
			if ( index< llength) {
				uwidth = lwidth *index;
				ulist.css('left',-uwidth);
				//ulist.animate({left: -uwidth}, 1000);

			}else{
				ulist.css('left','0px');
				index = 0;
			}; 
		//向左按钮           
		}else if(direction =="toLeft"){
			index--;
			if ( index < 0) {
				index = llength - 1;                
			}
			uwidth = lwidth *index;
			ulist.css('left',-uwidth);
			//ulist.animate({left: -uwidth}, "slow");
		};
		//changeBtn(index);
	}
	
	init();
	</script>
	
	<script type="text/javascript">
		//添加遮罩和提示框
		function AddRunningDiv() {
	        $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(document).height() }).appendTo("body");
	        $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍待。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(document).height() - 45) / 2 });
		}
		
		//取消遮罩和提示框
		function MoveRunningDiv() {
            $("div[class='datagrid-mask']").remove();
			$("div[class='datagrid-mask-msg']").remove();
		}
	</script>
	
	<script type="text/javascript">
		var buildingId=${record.buildingId?c};
		var gridId=${record.gridId?c};
		
		//楼宇结构
		var isClcikBuildLi = false;
		
		$("#buildLi").click(function() {
			if (isClcikBuildLi) {
				return;
			}
		
			AddRunningDiv();
		
			var url = "${rc.getContextPath()}/zzgl/grid/areaBuildingInfo/standardDetailRoom.jhtml?buildingId=" + buildingId;
			$("#build").attr("src", url);
			
			isClcikBuildLi = true;
		});
		
		//场所列表
		$("#placeLi").click(function(){
			$('#placeInfoList').datagrid({
				width:800,
				height:400,
				nowrap: true,
				striped: true,
				fit: true,
				idField:'plaId',
				url:'${rc.getContextPath()}/zzgl/grid/placeInfo/listData.json',
				columns:[[
					{field:'ck',checkbox:true},
		            //{field:'plaId',title:'ID', align:'center',width:50},
		            {field:'plaName',title:'场所名称', align:'center',width:150, formatter:function(value, rec, index){
		            	var content;
		            	if (value.length > 11) {
		            		content = value.substring(0,9)+"...";
		            	} else {
		            		content = value;
		            	}
						var f = '<a href="###" onclick="showDetailRow('+ rec.plaId+ ')">'+'<span title="'+value+'" style="font-weight:normal">'+content+'</span>'+'</a>&nbsp;';
						return f;
					}},
		            {field:'gridName',title:'所属网格', align:'center',width:100},
				    {field:'roomAddress',title:'场所地址', align:'center',width:200,formatter:function(value){
				    	if (value != null && value != "" && value != undefined) {
					    	var content;
					    	if (value.length > 17) {
					    		content = value.substring(0,15)+"...";
					    	} else {
					    		content = value;
					    	}
					    	return '<span title="'+value+'" style="font-weight:normal">'+content+'</span>';
				    	}
					}},
				    {field:'plaTypeLabel',title:'场所分类', align:'center',width:80},
				    {field:'isFocus', title:'是否重点场所', width:80, align:'center', formatter:function(value, rec, index){
						if(value=='1'){
							return '是';
						}else{
						   return '否';
						}
					}},
				    {field:'principal',title:'负责人', align:'center',width:100},
				    {field:'principalPhone',title:'负责人联系电话', align:'center',width:100}
				   
				]],
				pagination:true,
				queryParams:{buildingId:buildingId},
				onLoadSuccess:function(data){
				    $('#placeInfoList').datagrid('clearSelections'); //清除掉列表选中记录 
					if(data.total==0){ 
						var body = $(this).data().datagrid.dc.body2; 
						body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/xingwang/images/nodata.png" title="暂无数据"/></div>'); 
					}
				  },
				onLoadError:function(){
			        var body = $(this).data().datagrid.dc.body2; 
					body.append('<div style="text-align: center;">数据加载出错！</div>'); 
				}
			});
		
			//设置分页控件
		    var p = $('#placeInfoList').datagrid('getPager');
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
		});
		
		//企业信息列表
		$('#corLi').click(function(){
			$('#corBaseInfoList').datagrid({
				width:600,
				height:400,
				nowrap: true,
				striped: true,
				fit: true,
				idField:'cbiId',
				url:'${rc.getContextPath()}/zzgl/grid/corBaseInfo/listData.json',
				columns:[[
					{field:'ck',checkbox:true},
		          //  {field:'cbiId',title:'ID', align:'center',width:50},
		            {field:'corName',title:'企业名称', align:'center',width:150,formatter:function(value, rec, index){
		            	var content;
		            	
		            	if (value.length > 11) {
		            		content = value.substring(0,9)+"...";
		            	} else {
		            		content = value;
		            	}
		            	
						var f = '<a href="###" onclick="showCorBaseInfoDetailRow('+ rec.cbiId+ ')">'+'<span title="'+value+'" style="font-weight:normal">'+content+'</span>'+'</a>&nbsp;';
						return f;
					}},
		            /*{field:'gridName',title:'所属网格', align:'center',width:100}*/
					{field:'roomAddress',title:'办公地址', align:'center',width:200,formatter:function(value){
						if (value != null && value != "" && value != undefined) {
							var content;
					    	
					    	if (value.length > 11) {
					    		content = value.substring(0,9)+"...";
					    	} else {
					    		content = value;
					    	}
					    	
				    		return '<span title="'+value+'" style="font-weight:normal">'+content+'</span>';
						}
					}},
				    /*{field:'corTypeLabel',title:'法人类型', align:'center',width:150},
				    {field:'corAddr',title:'法人地址', align:'center',width:100},*/
				    {field:'representativeName',title:'法人代表', align:'center',width:100},
				    {field:'establishDateStr',title:'成立日期', align:'center',width:100},
				    /*{field:'registeredCapital',title:'注册资金', align:'center',width:100},*/
				    {field:'categoryLabel',title:'行业分类', align:'center',width:150,formatter:function(value){
						if (value != null && value != "" && value != undefined) {
							var content;
					    	
					    	if (value.length > 11) {
					    		content = value.substring(0,9)+"...";
					    	} else {
					    		content = value;
					    	}
					    	
					    	return '<span title="'+value+'" style="font-weight:normal">'+content+'</span>';
				    	}
					}},
				    /*{field:'economicTypeLabel',title:'经济类型', align:'center',width:100},
				    {field:'administrativeDivision',title:'行政区划', align:'center',width:100},*/
				    {field:'telephone',title:'电话号码', align:'center',width:100}
				   
				]],
				pagination:true,
				queryParams:{buildingId:buildingId},
				onLoadSuccess:function(data){
					    $('#corBaseInfoList').datagrid('clearSelections'); //清除掉列表选中记录 
						if(data.total==0){ 
							var body = $(this).data().datagrid.dc.body2; 
							body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/xingwang/images/nodata.png" title="暂无数据"/></div>'); 
						}
				  },
				onLoadError:function(){
						var body = $(this).data().datagrid.dc.body2; 
						body.append('<div style="text-align: center;">数据加载出错！</div>'); 
				}
			});
			
			//设置分页控件
		    var p = $('#corBaseInfoList').datagrid('getPager');
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
		});
		
		//人员信息列表
		$('#cirLi').click(function(){
			$('#cirsInfoList').datagrid({
				//width:600,
				height:400,
				nowrap: true,
				striped: true,
				fit: true,
				idField:'ciRsId',
				url:'${rc.getContextPath()}/zzgl/grid/ciRsRoom/roomCiRsListData.json',
				columns:[[
					{field:'ck',checkbox:true},
	               //{field:'ciRsId',title:'ID', align:'center',width:100},
	                  /*{field:'orgName',title:'所属网格',align:'center',width:120},*/
	                {field:'name',title:'姓名', align:'center',width:100, formatter:function(value, rec, index){
						var f = '<a href="###" onclick="showPeople('+ rec.ciRsId+ ')">'+value+'</a>&nbsp;';
						return f;
					}},
				    {field:'identityCard',title:'身份证', align:'center',width:200},
	                {field:'gender',title:'性别', align:'center',width:100},
	                {field:'phone',title:'联系电话', align:'center',width:150,formatter:function(value, rec, index){
						if(rec.phone!=null ||rec.phone!=undefined || rec.phone!="")					
							return rec.phone;
						else if(rec.residentMobile!=null||rec.residentMobile!=undefined || rec.residentMobile!="")
							return rec.residentMobile;
						else
							return rec.ophoneNum;
					}},
	                {field:'holderRelation',title:'与户主关系', align:'center',width:100}
				    /*{field:'education',title:'学历', align:'center',width:90,formatter: formatEducation},
				    {field:'marriageLabel',title:'婚姻', align:'center',width:80,formatter: formatMarriage},
				    {field:'holderRelation',title:'与户主关系', align:'center',width:100},
				    {field:'typeLabel',title:'人口性质', align:'center',width:100},
				    {field:'phone',title:'联系电话', align:'center',width:100},
				    {field:'residenceAddr',title:'现居住地址', align:'center',width:180}*/
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,  
				queryParams:{buildingId:buildingId},
				onLoadSuccess:function(data){
				    $('#cirsInfoList').datagrid('clearSelections'); //清除掉列表选中记录 
					if(data.total==0){ 
						var body = $(this).data().datagrid.dc.body2; 
						body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/xingwang/images/nodata.png" title="暂无数据"/></div>'); 
					}
			  },
			onLoadError:function(){
					var body = $(this).data().datagrid.dc.body2; 
					body.append('<div style="text-align: center;">数据加载出错！</div>'); 
			},
			onDblClickRow: function(index,rowData){
				var row = $("#cirsInfoList").datagrid("getSelected");
				showPeople(row.ciRsId);
				}
			});
			
			//设置分页控件
		    var p = $('#cirsInfoList').datagrid('getPager');
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
		});
		
		var manageState;
		
		var $NavDiv3 = $(".gl li");
			$NavDiv3.click(function(){
			manageState = $(this).attr('id');
       	});
       	
       	var type;
       	
		var $NavDiv4 = $(".xz li");
			$NavDiv4.click(function(){
			type = $(this).attr('id');
       	});
		
		//查询
		var name, identityCard;
		
		function searchAll() {
			var a = new Array();
			a["buildingId"]=buildingId;
			
			// 管理
			if(manageState!=null && manageState!="") a["manageState"]=manageState;
			// 性质
			if(type!=null && type!="") a["type"]=type;
			
			// 姓名
			var inputName = $('.inp1').val()
			
			if ('请输入姓名进行查询' == inputName) {
				name = '';
			} else {
				name = inputName;
			}
			
			if(name!=null && name!="") a["name"]=name;			
			
			doSearch(a);
		}
		
		//执行查询
		function doSearch(queryParams){
			$('#cirsInfoList').datagrid('clearSelections');
			$("#cirsInfoList").datagrid('options').queryParams=queryParams;
			$("#cirsInfoList").datagrid('load');
		}
		
		function showDetailRow(id) {
			var url = '${rc.getContextPath()}/zzgl/grid/placeInfo/placeCorDetail.jhtml?plaId='+id;
			showMaxJqueryWindow("场所信息与企业基本信息",url);
	    }
		
	    function showCorBaseInfoDetailRow(id) {
		    var url = '${rc.getContextPath()}/zzgl/grid/corBaseInfo/detail.jhtml?cbiId='+id;
		    showMaxJqueryWindow("企业详细", url);
        }
        
        function showPeople(cirsId){
			//parent.showInfo(cirsId);
			if(typeof parent.showInfo=="undefined" ){
			    var url='${RESIDENT_DOMAIN}/cirs/viewResident.jhtml?menu=1&ciRsId='+cirsId;
			    showMaxJqueryWindow('人员基础信息',url);
			}else{
				parent.showInfo(cirsId);
			}
		}
	</script>
	
	<script type="text/javascript">
		var tabContentHtml = "";
		var gridId=${gridId?c};
		function roomCiRs(title,buildingId,roomId){
			openWindow(title,function() {
				var url = '${rc.getContextPath()}/zzgl/grid/ciRsRoom/standardRoomCiRsList.jhtml?buildingId='+buildingId+'&roomId='+roomId+'&gridId='+gridId;
		    	context = '<iframe width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		    	return context;
			});
		}
		
		var NorMapOpenDiv;
		
		function openWindow(title,callback) {
			var windowDiv = document.body;
			
			if (NorMapOpenDiv!= null) {
				NorMapOpenDiv.innerHTML = "";
				NorMapOpenDiv.style.overflow = "auto";
			} else {
				NorMapOpenDiv = document.createElement("div");
				NorMapOpenDiv.id = "NorMapOpenDiv";
				NorMapOpenDiv.style.overflow = "hidden";  
				windowDiv.appendChild(NorMapOpenDiv);
			}
			
			var content = "<div id='NorMapOpenDiv' class='NorMapOpenDiv' style='index:10'>";
			
			content += "<div class='box'>";
			content += "<div class='biaoti'>";
			
			content += "<span class='fr close' onclick=\"javascirpt:document.getElementById('NorMapOpenDiv').innerHTML='';\"></span>";
			
			content += title;
			content += '单元';
			
			content +="</div>";
			
			content += callback();
			
			content += "</div>";
			content += "</div>";
			
			NorMapOpenDiv.innerHTML = content;
		}
		
		$('.close').click(function(){
			document.getElementById('norMapOpenDiv').style.display = 'none';
		});
		
		function changeContent(flag) {
			if(flag==1) {
				tabContentHtml = $("#tabContent").html();
				$("#tabContent").empty();
			} else {
				$("#tabContent").empty();
				$("#tabContent").html(tabContentHtml);
			}
		}
	</script>
	
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>
