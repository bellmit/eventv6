<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>中型-事件列表弹窗</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/Wanli/css/1.3_tc_event-mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/JiangYinPlatform/css/mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-5.4.1/package/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/tianshui/css/public.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/tianshui/css/mask.css"/>
	<link rel="stylesheet" href="${uiDomain!''}/js/paging/paging.css">
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/zTreeStyle.css" />
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/anole_combobox.css" />
    <link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
    <script>
        // 页面缩放
        var winW, winH, whdef, rem;
        function fullPage() { //将页面等比缩放
            winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>,
            whdef = 100 / 1920,
            rem = winW * whdef, // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
            document.querySelector('html').style.fontSize = rem + 'px';
        }
        fullPage();
        window.onresize = fullPage;
    </script>
    <style>
.weishouli {
    color: #ff2e47 !important;
}

.chuli {
    color: #00ff9c!important;
}

    	.mlr30 {
		    margin-left: .1rem;
		}
    
	    .datagrid-mask {
			z-index:51;
			position: absolute;
			left: 0;
			top: 0;
			opacity: 0.3;
			background: #ccc;
		}
		
		.swiper-slide>img {
    		height: auto; 
		}
		
		.maed-c-left {
    		height: 4.6rem;
		}
		
		.maed-c-right {
    		height: 4.6rem;
		}
		
		.legal-mcccr-center {
    		height: 3.21rem;
		}
		
		.ts-rbip-table thead tr th:first-child {
    		text-align: center;
    	}
		.filter-item>input {
		    background-color: rgba(29, 32, 136, .1) ! important;
		    color: white ! important;
		}
		.mt-tbody {max-height: 4.5rem;}
		.mask-c-bottom {margin-top: .5rem;}
		.FromTo{margin-top:-0.05rem}
		.TimeControl{font-size:12px;}
		.ztree{background-color: rgba(31, 65, 103, .9) !important;}
		.ztree *{color:white!important;}
		.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
		.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
		.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
		.swiper-pagination {height: 0.12rem;bottom: 1rem !important;}
		.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
		.aj_items_t_yellow {text-align: left;}
		.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
		.mCSB_inside > .mCSB_container{margin-right:.1rem};
    </style>
</head>
<body style="background-color: transparent;">
	<div class="event-mask" id="eventAttr" style="width: 10rem;height: 6rem;margin-top:-3rem;left:53%;z-index:55;display:none">
        <div class="mask-main-top" style="height:0rem;">
            <i class="close" id="eventAddClose"></i>
        </div>
        <div class="mask-main-bottom" style="height:100%;">
            <iframe id="mask-eventAttr" frameborder="0" width="100%" height="100%" style="overflow:hidden"></iframe>
        </div>
    </div>
    <!-- 有返回按钮时添加 ts-mask-title1-->
    <!-- 表格时查看 -->
    <div class="ts-mask-title clearfix" id="pageFlag">
       <p>详情</p>
    </div>
    <div class="sj-contnet bs">
	    <input id="bizPlatform" class="hide" name="bizPlatform" value="${bizPlatform!''}"/>
        <div class="sj-c-table bs hide" id="eventTablePart">
            <div class="sj-ct-top" id="eventTableDiv">
            	<div class="clearfix filter-box">
	                <div class="filter-item fl mlr30">
	                    <p>所属组织</p>
	                    <input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value="${infoOrgCode!''}"/>
                    	<input id="gridId" name="gridId" type="text" class="hide queryParam" >
	                    <input id="gridName" name="gridName" type="text" class="fl bs mlr10" style="width:120px;"/>
	                </div>
	                <div class="filter-item fl mlr30">
	                    <p>发生时间</p>
                        <input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
                		<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
                		<input type="text" id="_createTimeDateRender" class="fl bs mlr10" style="width:195px;" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
	                	<i></i>
	                </div>
	                <div class="filter-item fl">
	                    <p>关键字</p>
	                    <input type="text" class="fl bs mlr10" id="keyWord">
	                </div>
	                <div class="filter-btn fl mlr30 clearfix">
	                    <a href="javascript:void(0);" class="fl fb-confirm bs" onclick="searchEventList()">查询</a>
	                    <a href="javascript:void(0);" class="fl mlr20 fb-reset bs" onclick="resetCondition()">重置</a>
	                </div>
	            </div>
                <div class="ts-rbi3p-thead bs" style="margin-top: .1rem;">
                    <table class="ts-rbip-table">
                        <colgroup>
                            <col style="width: 1rem;">
                            <col style="width: auto;">
                            <col style="width: 1.5rem;">
                            <col style="width: 2.2rem;">
                            <col style="width: 2.2rem;">
                            <col style="width: 1rem;">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>事件名称</th>
                                <th>来源</th>
                                <th>类型</th>
                                <th>时间</th>
                                <th>状态</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div class="ts-rbi3p-tbody niceitem bs">
                    <table class="ts-rbip-table">
                        <colgroup>
                            <col style="width: 1rem;">
                            <col style="width: auto;">
                            <col style="width: 1.5rem;">
                            <col style="width: 2.2rem;">
                            <col style="width: 2.2rem;">
                            <col style="width: 1rem;">
                        </colgroup>
                        <tbody id="eventTableContent">
                           
                        </tbody>
                    </table>
                </div>
            </div>
            <div class=sj-ct-pagination" style="margin-top: -.5rem;float:right">
				<div class="sj-ctp-page clearfix mlr10" id="pageDiv" style="margin-left: 2rem;">
					<div id="eventTypePage"></div>
				</div>
			</div>
        </div>
        <div class="sj-c-details bs hide" id="eventInfoPart">
            <div class="maed-content" id="eventInfoDiv">
                <div class="clearfix">
                    <div class="maed-c-left niceitem fl">
                        <p class="me-cl-title" id="eventName" style="white-space: nowrap;text-overflow: ellipsis;"></p>
                        <p class="me-cl-small-title" id="eventType"></p>
                        <div class="maed-cl-top bs niceitem"  style="height: 1.5rem;">
                            <p class="maed-clt-title">于<span id="happenTimeStr"></span>在<span id="occurred"></span>发生：
                            </p>
                            <p class="maed-clt-text" id="content"></p>
                        </div>
                        <div class="maed-cl-bottom mtr15 bs">
                            <ul class="maed-clb-list bs">
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>所属网格：</p>
                                        <p id="gridPath"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>紧急程度：</p>
                                        <p id="urgencyDegreeName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                   <div class="maed-w50 fl clearfix">
                                        <p>事件编号：</p>
                                        <p id="eventCode"></p>
                                   </div>
                                   <div class="maed-w50 fr clearfix">
                                        <p>联系人员：</p>
                                        <p id="contactor"></p>
                                   </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>影响范围：</p>
                                        <p id="influenceDegreeName"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>信息来源：</p>
                                        <p id="sourceName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>涉及人员：</p>
                                        <p id="involvedNumName"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>当前状态：</p>
                                        <p id="statusName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>采集渠道：</p>
                                        <p id="collectWayName"></p>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="maed-c-right fr bs">
                        <div class="legal-mcccr-top bs">
                            <ul class="lmcccrt-list clearfix">
                                <li class="attrLabel" labelType="img" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="imgNum">图片</p>
                                    </a>
                                </li>
                                <li class="attrLabel" labelType="sound" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="soundNum">音频</p>
                                    </a>
                                </li>
                                <li class="attrLabel" labelType="video" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="videoNum">视频</p>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="img_swiper_div">
                            <div class="swiper-container" id="img_swiper">
                                <div class="swiper-wrapper" id="img_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/images/nopic.jpg">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="img_swiper_next"></div>
                                <div class="swiper-button-prev1" id="img_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="sound_swiper_div" style="display:none">
                            <div class="swiper-container" id="sound_swiper">
                                <div class="swiper-wrapper" id="sound_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/images/nopic.jpg">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="sound_swiper_next"></div>
                                <div class="swiper-button-prev1" id="sound_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="video_swiper_div" style="display:none">
                            <div class="swiper-container" id="video_swiper">
                                <div class="swiper-wrapper" id="video_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/images/nopic.jpg">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="video_swiper_next"></div>
                                <div class="swiper-button-prev1" id="video_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="img_release">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="img_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="img_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="img_num">0</span> ）</p>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="sound_release" style="display:none">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="sound_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="sound_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="sound_num">0</span> ）</p>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="video_release" style="display:none">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="video_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="video_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="video_num">0</span> ）</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="event-details-btn" <#if showButton?? && showButton=='1'><#else>style="display:none"</#if>>
                    <a style="cursor: pointer;" onclick="showZhoubianResource()">查看周边资源</a>
                </div>
            </div>
            <a href="javascript:void(0);" class="lmcc-mask-open">
                <i class="lmccm-open"></i>
                <p>处</p>
                <p>理</p>
                <p>记</p>
                <p>录</p>
            </a>
            <div class="lmcc-mask-box">
                <a href="javascript:void(0);" class="lmcc-mask-open">
                    <i class="lmccm-close"></i>
                    <p>处</p>
                    <p>理</p>
                    <p>记</p>
                    <p>录</p>
                </a>
                <div class="lmcc-mask bs">
                    <div class="lmcc-mask-content">
                        <div class="det-links-des clearfix">
                            <ul class="flex layer_aj_bt_n fr">
                                <li class="clearfix fr">
                                    <div class="aj_ks aj_ks_blue fl">
                                        <div class="aj_ks1"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>当前环节</p>
                                </li>
                                <li class="flex flex-ac fl" style="margin-right: .41rem;">
                                    <div class="aj_ks aj_ks_gray">
                                        <div class="aj_ks1"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>历史环节</p>
                                </li>
                                <li class="flex flex-ac fl">
                                    <div class="aj_ks aj_ks_gray">
                                        <div class="aj_ks1" style="background-color: #f0bd56"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>驳回环节</p>
                                </li>
                            </ul>
                        </div>
                        <div class="layer_aj_bt_b bs clearfix">
                            <p class="layer_aj_bt_b1">办理环节</p>
                            <p class="layer_aj_bt_b2">办理人/办理时间</p>
                            <p class="layer_aj_bt_b3">办理意见</p>
                        </div>
                        <div class="layer_aj_bt_s">
                            <div class="layer_aj_bt_line"></div>
                            <ul class="layer_aj_bt_items" id="flowDetail">
                                
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain!''}/web-assets/plugins/swiper-5.4.1/package/js/swiper.min.js"></script>
    <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
    <script src="${uiDomain!''}/web-assets/_big-screen/tianshui/js/tainshui.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/paging/paging.js"></script>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/jquery.anole.combobox.js"></script>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/anole.combobox.api.js"></script>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
    <script>
    
    	var curPage=1;
    	var pageSize=10;
    	var totalPage=1;
    	
    	var createTimeDateRender;
    	var eventTypeApi;
    
    	$(function(){
    		var curEventId="${eventId!''}";
    		if(curEventId!=null&&curEventId!=""){
    			initEventInfo(${eventId});
    		}else{
    			initEventTable();
    		}
    		
    		gridApi=AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
            }
        	}, {
            	OnCleared: function() {
                	$("#infoOrgCode").val('');
            	},
            	ShowOptions: {
                	EnableToolbar : true
            	}
        	});
			
			createTimeDateRender = $('#_createTimeDateRender').anoleDateRender({
				BackfillType : "1",
				ShowOptions : {
					TabItems : ["常用", "年", "季", "月", "清空"]
				},
				BackEvents : {
					OnSelected : function(api) {
						$("#happenTimeStart").val(api.getStartDate());
						$("#happenTimeEnd").val(api.getEndDate());
					},
					OnCleared : function() {
						$("#happenTimeStart").val('');
						$("#happenTimeEnd").val('');
					}
				}
			}).anoleDateApi();
			
    	});
    	
    	function modleEventLabelopen(dom) {
			$("<div class='datagrid-mask'></div>").css({
				display : "block",
				width : "100%",
				height : $(window).height()
			}).appendTo($('#'+dom));
	
			document.body.scroll = "no";//除去滚动条
		}

		function modleEventLabelclose() {
			$(".datagrid-mask").css({
				display : "none"
			});
			$(".datagrid-mask-msg").css({
				display : "none"
			});
			$(".datagrid-mask").remove();
			$(".datagrid-mask-msg").remove();
			document.body.scroll = "auto";
		}
    	
    	function formatStr(str){
			if(str == null ||str == 'null' || str == undefined ){
				return '';
			}else{
				return str;				
			}
		}
		
		function showImg(i){
			$('#eventAttr').show();
			$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?drag=true&background=none&overflow=none&fieldId=playImg&index='+i+'&titles='+encodeURI(imgTitleList.toString())+'&paths='+imgList.toString());
			//window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+imgList.toString());
		}
	
		function showVideo(id){//eventSeq
			$('#eventAttr').show();
			$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?isBigScreen=1&videoType=2&attachmentId='+id);
			//window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
		}
		
		function showZhoubianResource(){
			window.parent.postMessage('showEventZhouBian()','${messageUrl}');
		}
		
		var attrMap={
			'img':'图片',
			'sound':'音频',
			'video':'视频'
		}
		
		var imgTitleArr={
			"1":"处理前",
			"2":"处理中",
			"3":"处理后"
		}

		var statusMap={
	  			"00":"weishouli",
	  			"01":"weishouli",
	  			"02":"weishouli",
	  			"03":"chuli",
	  			"04":"chuli"
	  	}
	  	var statusNameMap={
	  			"00":"处理中",
	  			"01":"处理中",
	  			"02":"处理中",
	  			"03":"已处理",
	  			"04":"已处理"
	  	}
		
		function resetCondition(){
			createTimeDateRender.doClear();
			gridApi.doClearing();
			$('#keyWord').val('');
			searchEventList();
		}
		
		function searchEventList(page){
			modleEventLabelopen('eventTableDiv');
			if(page){
				curPage=page;
			}
			var queryParams={
				"infoOrgCode":$('#infoOrgCode').val()?$('#infoOrgCode').val():${infoOrgCode},
				"page":curPage,
				"rows":pageSize,
				"eventType":"all"
			};
			if($('#keyWord').val()){
				queryParams.keyWord=$('#keyWord').val();
			}
			if($('#happenTimeStart').val()){
				queryParams.happenTimeStart=$('#happenTimeStart').val();
			}
			if($('#happenTimeEnd').val()){
				queryParams.happenTimeEnd=$('#happenTimeEnd').val();
			}
			if($('#bizPlatform').val()){
				queryParams.bizPlatform=$('#bizPlatform').val();
			}
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
				data: queryParams,
				dataType:"json",
				success: function(data){
					console.log(data);
					
					var list=data.rows;
					totalPage=Math.ceil(data.total/pageSize);
					
					var str="";
					
					for(var i=0,j=list.length;i<j;i++){
					
						var d=list[i];
						
						str+='<tr style="cursor:pointer" onclick="initEventInfo('+d.eventId+')">';
                        str+='<td>'+parseInt(parseInt((curPage-1)*10)+parseInt(i+1))+'</td>';
                        str+='<td title="'+formatStr(d.eventName)+'">'+formatStr(d.eventName)+'</td>';
                        str+='<td title="'+formatStr(d.bizPlatformName)+'">'+formatStr(d.bizPlatformName)+'</td>';
                        str+='<td title="'+formatStr(d.eventClass)+'">'+formatStr(d.eventClass)+'</td>';
                        str+='<td>'+formatStr(d.happenTimeStr)+'</td>';
                        str+='<td class="'+statusMap[d.status]+'" title="'+formatStr(statusNameMap[d.status])+'">'+formatStr(statusNameMap[d.status])+'</td>';
                        str+='</tr>';
                        
					}
					
					$('#eventTableContent').html(str);
					
					//设置分页
					if(data.total<pageSize){
		    			$("#pageDiv").hide();
					}else{
		   				$("#pageDiv").show();
						$("#eventTypePage").remove();
						$("#pageDiv").html('<div id="eventTypePage"></div>');
						$("#eventTypePage").createPage({
							pageNum: (Math.floor((data.total-1)/pageSize)+1),
							current: curPage,
							backfun: function(e) {
								curPage=e.current;
								searchEventList(e.current);
							}
						});
					}
					
				},
				complete:function(){ 
					modleEventLabelclose();
				}
			});
		}
		
		function initEventTable(){
			$('#pageFlag').html('<p>列表</p>');
			$('#eventInfoPart').hide();
			$('#eventTablePart').show();
			searchEventList();
		}
    	
    	function initEventInfo(eventId){
    		$('#pageFlag').html('<div title="返回列表" style="cursor: pointer;" class="fl ts-mt-back mlr10"><a onclick="initEventTable()"></a></div><p>详情</p>');
    		$('#eventInfoPart').show();
			$('#eventTablePart').hide();
    		modleEventLabelopen('eventInfoDiv');
    		$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getEventInfo.json',
				data: {"eventId":eventId},
				dataType:"json",
				success: function(data){
				
					//设置事件基本信息
					var event=data.event;
					
					$('#urgencyDegreeName').html(formatStr(event.urgencyDegreeName));
					$('#influenceDegreeName').html(formatStr(event.influenceDegreeName));
					$('#involvedNumName').html(formatStr(event.involvedNumName));
					if(event.tel){
						$('#contactor').html(formatStr(event.contactUser)+'('+formatStr(event.tel)+')');
					}else{
						$('#contactor').html(formatStr(event.contactUser));
					}
					$('#sourceName').html(formatStr(event.sourceName));
					$('#collectWayName').html(formatStr(event.collectWayName));
					$('#gridPath').html(formatStr(event.gridPath));
					$('#eventCode').html(formatStr(event.code));
					$('#happenTimeStr').html(formatStr(event.happenTimeStr));
					$('#content').html(formatStr(event.content));
					$('#occurred').html(formatStr(event.occurred));
					$('#statusName').html(formatStr(event.statusName));
					$('#eventType').html('['+formatStr(event.eventClass)+']');
					$('#eventName').html(formatStr(event.eventName));
					$('#eventName').attr('title',formatStr(event.eventName));
					
					$("#content").niceScroll({
			            cursorcolor: "#185ab2",
			            cursoropacitymax: 1,
			            cursorwidth: "4px",
			            autohidemode: false,
			            cursorborder: "none",
			            cursorborderradius: "0",
			        })
					
					//填充环节信息
					var flow=data.process;
					var flowStr="";
					for(var i=0,j=flow.length;i<j;i++){
						var d=flow[i];
						
						//如果是当前环节#a9ff00
						if(d.IS_CURRENT_TASK){
							flowStr+='<li class="flex flex-ac">'+
							'<h5 class="aj_items_h aj_items_h_green" style="color:#a9ff00">'+d.TASK_NAME+'</h5>'+
							'<div class="aj_ks aj_ks_blue">'+
							'<div class="aj_ks1"></div>'+
							'<div class="aj_ks2"></div>'+
					        '</div>'+
					        '<div>'+
					        '<div class="flex flex-ac">'+
					        '<p class="aj_items_t aj_items_t_yellow">'+d.HANDLE_PERSON+'</p>'+
					        '</div>';
					        
					        if(d.subAndReceivedTaskList!=null && d.subAndReceivedTaskList.length>0){
					        
					        	for(var m=0,n=d.subAndReceivedTaskList.length;m<n;m++){
					        		var t=d.subAndReceivedTaskList[m];
					        		
					        		flowStr+='<div class="flex flex-ac mtr10">'+
					        		'<div class="aj_items_t2 flex">'+
					        		'<p class="aj_items_t aj_items_t_yellow" style="margin-left:0rem;text-align: left">'+t.TRANSACTOR_NAME+'('+t.ORG_NAME+')'+'<br>';
					        		if(m==0){
					        			flowStr+='<span>接受时间</span>:'+t.RECEIVE_TIME;
					        		}else{
					        			flowStr+='<span>处理时间</span>:'+t.END_TIME;
					        		}
					        		
					        		if(t.REMARKS){
					        			flowStr+='<p class="aj_items_t aj_items_t_result">'+t.REMARKS+'</p>'
					        		}
					        	
					        	}
					        
					        }
						
					        flowStr+='</div>'+
					        '</li>';
						}else{
						
							flowStr+='<li class="flex flex-ac">';
							
							if(d.OPERATE_TYPE==2){//驳回环节
								flowStr+='<h5 class="aj_items_h aj_items_h_green"  style="color:#f0bd56">'+d.TASK_NAME+'</h5>';
								flowStr+='<div class="aj_ks aj_ks_gray">'+
									'<div class="aj_ks1" style="background-color: #FFB90F"></div>'+
									'<div class="aj_ks2"></div>'+
									'</div>';
							}else{
							
								flowStr+='<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>';
								flowStr+='<div class="aj_ks aj_ks_gray">'+
								'<div class="aj_ks1"></div>'+
								'<div class="aj_ks2"></div>'+
								'</div>';
							
							}
							
							flowStr+='<div class="aj_items_t2 flex">'+
							'<p class="aj_items_t aj_items_t_green bs">'+formatStr(d.TRANSACTOR_NAME)+'('+d.ORG_NAME+')'+'<span> 耗时 '+d.INTER_TIME+'</span><br><span>办理时间</span>:'+d.START_TIME+'</p>'+
							'</div>';
							if(d.REMARKS){
								flowStr+='<p class="aj_items_t aj_items_t_result">'+d.REMARKS+'</p>';
							}
							flowStr+='</li>';
						
						}
					
					}
					
					$('#flowDetail').html(flowStr);
					
					//设置附件
					var attrStr="";
					imgList=[];
					imgTitleList=[];
					
					attrTitleList={};
					attrTimeList={};
					
					var imgs=data.imgs;
					var sounds=data.sounds;
					var videos=data.videos;
					var attrflag=0;
					
					if(imgs!=null&&imgs.length>0){
						$('#imgNum').html('图片('+imgs.length+')');
						$('#img_num').html(imgs.length);
						$('.attrLabel[labelType=img]').attr('total',imgs.length);
						var imgstr="";
						for(var i=0,j=imgs.length;i<j;i++){
							var d=imgs[i];
							imgList.push("${IMG_URL}"+d.filePath);
							imgTitleList.push(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前");
							
							attrTitleList['img_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['img_'+i]=d.createTimeStr;
							imgstr+='<div class="swiper-slide" >';
							imgstr+='<img style="cursor: pointer;" onclick="showImg('+i+')" src="${IMG_URL}'+d.filePath+'"/>';
							imgstr+='</div>';
						}
						$('#img_swiper_content').html(imgstr);
						
					}
					
					if(sounds!=null&&sounds.length>0){
						$('#soundNum').html('音频('+sounds.length+')');
						$('#sound_num').html(sounds.length);
						$('.attrLabel[labelType=sound]').attr('total',sounds.length);
						var soundstr="";
						for(var i=0,j=sounds.length;i<j;i++){
							var d=sounds[i];
							attrTitleList['sound_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['sound_'+i]=d.createTimeStr;
							soundstr+='<div class="swiper-slide" >';
							soundstr+='<img style="cursor: pointer;" onclick="showVideo('+d.attachmentId+')" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>';
							soundstr+='</div>';
						}
						$('#sound_swiper_content').html(soundstr);
					}
					
					if(videos!=null&&videos.length>0){
						$('#videoNum').html('视频('+videos.length+')');
						$('#video_num').html(videos.length);
						$('.attrLabel[labelType=video]').attr('total',videos.length);
						var videoStr="";
						for(var i=0,j=videos.length;i<j;i++){
							var d=videos[i];
							attrTitleList['video_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['video_'+i]=d.createTimeStr;
							videoStr+='<div class="swiper-slide" >';
							videoStr+='<img style="cursor: pointer;" onclick="showVideo('+d.attachmentId+')" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>';
							videoStr+='</div>';
						}
						$('#video_swiper_content').html(videoStr);
					}
					
					$('.attrLabel').eq(0).click();
					
					
				},
				complete:function(){
					modleEventLabelclose();
				}
			});	
    	}
    	
        //事件详情图片轮番
        
        //事件处理记录
        $(".layer_aj_bt_s").niceScroll({
            cursorcolor: "#185ab2",
            cursoropacitymax: 1,
            cursorwidth: "4px",
            autohidemode: false,
            cursorborder: "none",
            cursorborderradius: "0",
        })
        $('.sj-c-details>.lmcc-mask-open').click(function () {
            $(this).hide();
            $(".maed-cl-top").getNiceScroll().hide();
            $(".maed-clb-list").getNiceScroll().hide();
            $('.lmcc-mask-box').show();
            $('.lmcc-mask').animate({ "right": "0" }, 100, function () {
                $('.lmcc-mask-box').css({ "overflow": "unset" });
                $('.layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
                $(".layer_aj_bt_s").getNiceScroll().resize();
            });
        });
        $('.lmcc-mask-box>.lmcc-mask-open').click(function () {
            $('.lmcc-mask').animate({ "right": "-100%" }, 100, function () {
                $('.lmcc-mask-box').hide();
                $(".layer_aj_bt_s").getNiceScroll().resize();
                $(".maed-cl-top").getNiceScroll().show();
                $(".maed-clb-list").getNiceScroll().show();
                $('.sj-c-details>.lmcc-mask-open').show();
            });
        });
        
        $('.attrLabel').click(function(){
        	$(this).addClass('active').siblings().removeClass('active');
        	$('.attrSwiper').hide();
        	var labelType=$(this).attr('labelType');
        	$('#'+labelType+'_swiper_div').show();
        	$('#'+labelType+'_release').show();
        	if($(this).attr('isLoad')=='0'){
        		var swiper;
	            swiper = new Swiper('#'+labelType+'_swiper', {
	                allowTouchMove: false,
	                navigation: {
	                    nextEl: '#'+labelType+'_swiper_next',
	                    prevEl: '#'+labelType+'_swiper_prev',
	                },
	            });
	            //图片轮番的页数
	            $('#'+labelType+'_swiper_next, #'+labelType+'_swiper_prev').on('click', function () {
	                var swiI = swiper.activeIndex;
	                $('#'+labelType+'_title').html(attrTitleList[labelType+'_'+swiI]);
	                $('#'+labelType+'_time').html(attrTimeList[labelType+'_'+swiI]);
	                swiI += 1;
	                $('.lmcccrb-page p span:visible').eq(0).text(swiI);
	            });
		        if(parseInt($(this).attr('total'))<2){
		        	$('#'+labelType+'_swiper_next').hide();
		        	$('#'+labelType+'_swiper_prev').hide();
		        }
		        if(parseInt($(this).attr('total'))==0){
		        	$('#'+labelType+'_release').html('<div class="lmcccrb-page" style="margin-top: 0.30rem;"><p>暂无数据</p></div>');
		        }else{
		        	$('#'+labelType+'_title').html(attrTitleList[labelType+'_0']);
		        	$('#'+labelType+'_time').html(attrTimeList[labelType+'_0']);
		        }
		        $(this).attr('isLoad','1');
        	}
        });
        
        $('.close').click(function(){
			$('#mask-eventAttr').attr('src','');
			$('#eventAttr').hide();
		});
       
    </script>
</body>
</html>