<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>南昌-待办事件</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>

	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/normal.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/jquery.mCustomScrollbar.css" />
    
    <link rel="stylesheet" href="${uiDomain}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/paging.js"></script>
	
	<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/jquery.easyui.patch.js"></script><!--用于修复easyui-1.4中easyui-numberbox失去焦点后不能输入小数点的问题-->
	<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

	<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/jquery.form.js" ></script>
	<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/function.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/global.js?t=${.now?datetime}"></script>

	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/workflow/easyui-utils.js"></script>
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
    <#include "/component/bigFileUpload.ftl" />

	
    <script type="text/javascript">
        var winW, scale;
        $(window).on('load resize', function () {
            fullPage();
            setTimeout(fullPage, 10);//二次执行页面缩放，解决全屏浏览时滚动条问题
            function fullPage() {//将页面等比缩放
                winW = $(window.parent).width();
                
                var whdef = 100 / 1920;
                var rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");


                if(winW<1200){
                    $("head").append("<link rel='stylesheet' href='${uiDomain!}/web-assets/_big-screen/nanchang-cc/css/small-screen.css'>");
                        
                 
                }else{
$("head").append("<link rel='stylesheet' href='${uiDomain!}/web-assets/_big-screen/nanchang-cc/css/index-mask.css'>");
                }
               
            }
        });
    </script>

    <style>
    ul, li {margin:0; padding:0; border: none; outline: 0; font: menu; }
    
    .zxfPagenum{
    color: #7fafff;
	    padding: 0 5px;
	height:20px;
	line-height:20px;
	margin:0 5px;
	border-radius:3px
}
.panel-body {
    font-size: 14px;
}
.nextpage{
	margin:0 5px
}

.nextbtn,.prebtn,span.disabled{
	margin: 0px 10px;
	height:42px;
	line-height:42px;
	border-radius:3px;
	color:#7fafff;
	    font-size: 14px;
}
.zxfinput{
	width:50px;
	height:29px;
	text-align:center;
	box-sizing:border-box;
	margin:0 12px;background-color: rgba(0,0,0,0);
	border-radius:3px;

border: solid 1px rgba(0, 176, 255, 0.5);
    color: #7aa9ff;
}.zxfokbtn{
	width:48px;
	height:32px;
	line-height:32px;
	margin-left:10px;
	cursor:pointer;
	border-radius:3px;
}
input::-webkit-outer-spin-button,input::-webkit-inner-spin-button{
	-webkit-appearance:none
}
input[type=number]{
	-moz-appearance:textfield
}
.current {
    background: rgba(40, 151, 255, 0.8);
    height: 20px;
    padding: 0 5px;
    line-height: 20px;
    border-radius: 3px;
}
.select-table .select-td {
    border-bottom: 1px solid #39BBF8;
    border-right: 1px solid #39BBF8;
}
.list_div{
	display: -moz-box; 
	display: -ms-flexbox;
	display: -webkit-flex;
	display: flex;
	-webkit-justify-content:center;
	-moz-justify-content:center;
	-ms-justify-content:center;
	justify-content: center;
	-webkit-align-items:center;
	-moz-align-items:center;
	-ms-align-items:center;
	align-items: center;
	color:#7fafff;
	    font-size: 14px;
}
		.mt45 {
    margin-top: 15px;
}
.mt40 {
    margin-top: 40px;
}
.mael-ctir-text {
    height: 1.9rem;
}
.mael-c-top {
    height: 5.25rem;
    margin-left:-8px;
	overflow-x: hidden;
}

.inp1 {
    background-color: #084bae !important;
    border-radius: 0.04rem ;
    border: solid 1px #2e7bec;
    color: #fff !important;
}
.TimeControl .nav{
background:none;
}
.TimeList ul li{
background:none;
color: #fff;
}
.TimeControl .nav ul li{
color: #fff;
}
.TimeSearch ul li{
color: #fff;
}
.ztree * {
    padding: 0;
    margin: 0;
    font-size: 12px;
    font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    color: #fff;
}


#leftPanel>.ztree * {
    padding: 0;
    margin: 0;
    font-size: 12px;
    font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    color: #000;
}

.DealProcess{background-color:#F9F9F9;}
	.DealProcess td{padding-bottom:10px;}
	.DealMan{width:630px; line-height:24px;}
	.MarskDiv{width:844px; height:52px; position:absolute; z-index:2; top:0; left:0;}
	/*办理人员全选样式*/
	.SelectAll{padding:2px 8px; *padding:0 8px 2px 5px; display:block; float:left; border-radius:3px; background:#2dcc70; color:#fff; font-weight:normal; font-size:12px;}
	.SelectAll:hover{background:#27ae61;}
	
	
.BigNorToolBtn{margin:0 8px;}

.nodata{width: 180px;height: 160px;}

.numberbox{background-color: rgba(0, 156, 255, .2);border: solid 0.01rem #009cff;}



.YearSelect ul li {background:none;color: #fff;}

.QuarterSelect ul li {background:none;color: #fff;}
.YearNav ul li { color: #fff }
    </style>
</head>

<body style="background: transparent;" >
    <!--隐藏form表单-->
    <div id="jqueryToolbar"></div>
    <!-- 待办事件列表 -->
    
    <div class="mae-container mae-event-list mae-container-on" id="mae-event-list">
        <div class="maet-btn clearfix" id="maet-btn">
            <a onclick="showTodo()"id="todoEvent"  href="javascript:void(0);" class="active fl">待办事件</a>
            <a onclick="showAll()" id="allEvent" href="javascript:void(0);" class="fl">辖区事件</a>
        </div>
        <div class="div-table">
        	<div id="searchForTodo" class="maet-top clearfix">
                <div class="maet-t-item fl clearfix">
                    <p>所属组织：</p>
                    <input id="infoOrgCode" name="eInfoOrgCode" type="text" class="hide queryParam" value="${infoOrgCodeForOut!''}"/>
                    <input id="infoOrgCodeForVerify" name="infoOrgCodeForVerify" type="text" class="hide queryParam" value="${infoOrgCodeForOut!''}"/>
                    <input id="gridId" name="gridId" type="text" class="hide queryParam" >
                    <input id="gridName" name="gridName" type="text" class="inp1 InpDisable maet-ti-select" style="width:4.1rem;" value="${gridNameForOut!''}" />
                </div>
                <div class="maet-t-item fl clearfix">
                    <p>事发时间：</p>
                    <input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
	                <input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
	                <input type="text" id="_happenTimeDateRender" class="inp1 InpDisable maet-ti-select" style="width:2.5rem" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
                </div>
                <div class="maet-t-item fl clearfix">
                    <p>事件类别：</p>
                    <input type="text" id="bizPlatform" name="bizPlatform" class="hide queryParam"/>
                    <input type="text" id="bizPlatformName" name="bizPlatformName" class="inp1 selectWidth maet-ti-select" style="width:5.4rem;" />
                </div>
                <a onclick="getList('todo',1,8);" href="javascript:void(0);" class="fr maet-t-search"></a>
            </div>
        	<div id="searchForAll" class="maet-top clearfix">
                <div class="maet-t-item fl clearfix">
                    <p>所属组织：</p>
                    <input id="infoOrgCodeForAll" name="eInfoOrgCode" type="text" class="hide queryParam" value="${eInfoOrgCodeForOut!''}"/>
                    <input id="gridIdForAll" name="gridId" type="text" class="hide queryParam" >
                    <input id="gridNameForAll" name="gridName" type="text" class="inp1 InpDisable maet-ti-select" style="width:4.1rem;" value="${gridNameForOut!''}" />
                </div>
                <div class="maet-t-item fl clearfix">
                    <p>事发时间：</p>
                    <input class="inp1 hide queryParam" type="text" id="happenTimeStartForAll" name="happenTimeStart" value="${happenTimeStartForAll!}"></input>
	                <input class="inp1 hide queryParam" type="text" id="happenTimeEndForAll" name="happenTimeEnd" value="${happenTimeEndForAll!}"></input>
	                <input type="text" id="_happenTimeDateRenderForAll" class="inp1 InpDisable maet-ti-select" style="width:2.5rem;" value="${happenTimeStartForAll!}<#if happenTimeStartForAll?? && happenTimeEndForAll??> ~ </#if>${happenTimeEndForAll!}"/>
                </div>
                <div class="maet-t-item fl clearfix">
                    <p>事件类别：</p>
                    <input type="text" id="bizPlatformForAll" name="bizPlatform" class="hide queryParam"/>
                    <input type="text" id="bizPlatformNameForAll" name="bizPlatformName" class="inp1 selectWidth maet-ti-select" style="width:5.4rem;" />
                </div>
                <a onclick="getListAll('all',1,8);" href="javascript:void(0);" class="fr maet-t-search"></a>
            </div>
            <table class="maet-content" style="margin-left: .22rem;">
                <colgroup>
                    <col style="width: 3.6rem">
                    <col style="width: 2.6rem">
                    <col style="width: 1.86rem">
                    <col style="width: 1.1rem">
                </colgroup>
                <thead>
                    <tr>
                        <th>事件名称</th>
                        <th>事发地点</th>
                        <th>事发时间</th>
                        <th>事件类型</th>
                    </tr>
                </thead>
                <tbody id="eventtodoList">
                
            	</tbody>
                <tbody id="eventallList">
                
            	</tbody>
            </table>
        </div>
        <div class="maet-pagination clearfix" id="pageDiv" style="width:9rem;margin-top:.6rem">
            <div id="eventTypePage" style="font-size:14px;margin-top:-50px;zoom:0.7" class="list_div"></div>
        </div>
        <div class="maet-pagination clearfix" id="pageDivAll" style="width:9rem;margin-top:.6rem">
            <div id="eventTypePageAll" style="font-size:14px;margin-top:-50px;zoom:0.7" class="list_div"></div>
        </div>
    </div>

    <!-- 待办事件详情 -->
    <div class="mae-container mae-event-detail" id="mae-event-detail">
            <div id="event_detail" style="height: 90%;overflow: hidden;z-index:0;position:relative"></div>
            <div id="event_detail_button" style="position: absolute;bottom:0.2rem;width: 10rem;z-index:1"></div>
    </div>
    
    <!-- 新增事件页面 -->
    <div class="mae-container mae-event-add" id="mae-event-add">
            <div id="event_add" style="height: 90%;overflow: hidden;"></div>
            <div id="event_add_button">
                <div class="maed-btn-box clearfix" style="margin-top:-5px">
                    <a onclick="submitEvent()" href="javascript:void(0);" class="fl handle">提交</a>
                    <a id="backEventVerify" href="javascript:void(0);" class="fr">返回</a>
                </div>
            </div>
    </div>

    <!-- 待办事件办理 -->
    <div class="mae-container mae-event-handle" id="mae-event-handle">
        <input type="hidden" name="formId" id="formId"/>
		<input type="hidden" name="instanceId" id="instanceId"/>
		<input type="hidden" name="nextNode" id="nextNode"/>
		<input type="hidden" name="nextNodeCode" id="nextNodeCode"/>
		<input type="hidden" name="nextNodeId" id="nextNodeId"/>
		<input type="hidden" name="curTaskId" id="curTaskId"/>
		<input type="hidden" name="deploymentId" id="deploymentId"/>
		<input type="hidden" name="curNodeName" id="curNodeName"/>
		
        <div class="mael-content">
            <div id="handlebox" class="mael-c-top">
                <div class="mael-ct-item clearfix">
                    <p><span>*</span>下一环节:</p>
                    <div class="mael-cti-right fr">
                        <div id="nextTaskNode" class="mael-crir-link clearfix" style="margin-top: 0.07rem;"></div>
                    </div>
                </div>
                
                <div id="evaLev" class="mael-ct-item clearfix">
                <input type="hidden" name="evaLevel" id="evaLevel"/>
                    <p><span>*</span>评价等级:</p>
                    <div class="mael-cti-right fr">
                        <div class="mael-crir-link clearfix">
                            <a onclick="evaClick('01')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: -0.02rem;"><i></i></div>
                                <p>不满意</p>
                            </a>
                            <a id="defaultSelect" onclick="evaClick('02')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: -0.02rem;"><i></i></div>
                                <p>满意</p>
                            </a>
                            <a onclick="evaClick('03')" href="javascript:void(0);" class="mael-crirl-item clearfix">
                                <div class="fl" style="margin-top: -0.02rem;"><i></i></div>
                                <p>非常满意</p>
                            </a>
                        </div>
                    </div>
                </div>

                <div id="handlePerson" class="mael-ct-item clearfix">
                    <p><span>*</span>办理人员:</p>
                    <div class="mael-cti-right fr">
                        <a style="display:none" id="selectOrgButton" onclick="_selectHandler()" href="javascript:void(0);" class="mael-ctir-personnel">
                            <p>选择办理人员</p>
                        </a>
                        <input type="hidden" name="nextStaffId" id="userIds"/>
				        <input type="hidden" name="curOrgIds" id="curOrgIds"/>
				        <input type="hidden" id="curOrgNames" />
				        <div class="FontDarkBlue fl DealMan Check_Radio" id="userName_div"><b name="userNames" id="userNames" style="color:white;font-size:12px"></b></div>
		    	        <div class="FontDarkBlue fl DealMan"><b name="htmlUserNames" id="htmlUserNames"></b></div>
				        <input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
                    </div>
                </div>

                <div id="intervalTr" class="mael-ct-item clearfix" style="display:none">
                    <p>办理时限:</p>
                    <div class="mael-cti-right fr">
                        <input type="text" id="eventHandleInterval" name="eventHandleInterval" class="from mael-ctir-text easyui-numberbox" data-options="tipPosition:'bottom',max:7,min:1" style="width: 210px; height: 28px;" value="${eventDefaultHandleInterval!'7'}" />
						<label class="LabName" style="float: none; display: inline-block; margin-left: 0; width: 25px;color:white">(天)</label>
                    </div>
                </div>
                
                <div class="mael-ct-item clearfix">
                    <p><span>*</span>办理意见:</p>
                    <div class="mael-cti-right fr">
                        <textarea id="adviceText" class="mael-ctir-text easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" placeholder="请输入您的意见"></textarea>
                    </div>
                </div>

                <div class="mael-ct-item clearfix" id="bigFileUploadDiv"></div>

            </div>
            <div id="workflowBtn" style="margin-top:-30px"><div>
            
        </div>
        
        
        
       <#include "/zzgl/event/workflow/select_user.ftl" />

        
    </div>

    <script src="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
    <script>
    var u = navigator.userAgent, app = navigator.appVersion;   
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    var g_EventNodeCode=null;
    
        $(function () {
        
        	//待办搜索条件栏
        	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.eInfoOrgCode);
                $("#infoOrgCodeForVerify").val(grid.orgCode);
            }
        	}, {
            	OnCleared: function() {
                	$("#infoOrgCode").val('');
            	},
            	ShowOptions: {
                	EnableToolbar : true
            	}
        	});
        	
        	
        	AnoleApi.initListComboBox("bizPlatformName", "bizPlatform", null, null, null, {
            	DataSrc: [{"name":"巡访事件", "value":'xunfan'},{"name":"AI预警事件", "value":'ai'},{"name":"督办事件", "value":'duban'}],
            	ShowOptions: {
                	EnableToolbar : true
            	}
        	});
        	
        	happenTimeDateRender = $('#_happenTimeDateRender').anoleDateRender({
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
			
			
			//辖区所有搜索条件栏
        	AnoleApi.initGridZtreeComboBox("gridNameForAll", "gridIdForAll", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCodeForAll").val(grid.eOrgCode);
            }
        	}, {
            	OnCleared: function() {
                	$("#infoOrgCodeForAll").val('');
            	},
            	ShowOptions: {
                	EnableToolbar : true
            	}
        	});
        	
        	
        	AnoleApi.initListComboBox("bizPlatformNameForAll", "bizPlatformForAll", null, null, null, {
            	DataSrc: [{"name":"巡访事件", "value":'xunfan'},{"name":"AI预警事件", "value":'ai'},{"name":"督办事件", "value":'duban'}],
            	ShowOptions: {
                	EnableToolbar : true
            	}
        	});
        	
        	happenTimeDateRender = $('#_happenTimeDateRenderForAll').anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#happenTimeStartForAll").val(api.getStartDate());
					$("#happenTimeEndForAll").val(api.getEndDate());
				},
				OnCleared : function() {
					$("#happenTimeStartForAll").val('');
					$("#happenTimeEndForAll").val('');
				}
			}
			}).anoleDateApi();
        
        	
        	//判断是否显示辖区所有页签（只有app显示）
        	if(isAndroid){
        	//if(navigator.userAgent.match("ua_ffcs")){
			    $("#listDiv").css("margin-left",".36rem");
				$("#_happenTimeDateRender").css("width","3.5rem");

				$("#_happenTimeDateRenderForAll").css("width","3.5rem");
				$("#event_detail_button").css("margin-left","1rem");
				//$(".mael-c-top").css("height","6.5rem");
				$("#handlebox").css("height","6.5rem");
				$(".ztree").css("zoom","1.2");	
 				$(".QuarterSelect  ul li ").css("font-size","0.25rem");
$(".TimeList ul li").css("font-size","0.25rem");
			}else{
				$('#allEvent').hide();
				$("#handlebox").css("height","4.6rem")
			}
            //获取列表数据
            getList('todo',1,8);
            // 事件类型的选择
            $('#maet-btn').on('click','a',function(){
                $(this).addClass('active').siblings().removeClass('active');
            })
         
            //事件处理记录
            $('#handlebox').niceScroll({
                cursorcolor: "rgba(0, 160, 233, .4)",
                cursoropacitymax: 1,
                touchbehavior: false,
                cursorwidth: "4px",
                cursorborder: "0",
                cursorborderradius: "4px",
                zindex: 25,
				 horizrailenabled: false, 
                autohidemode: false //隐藏式滚动条
            });
			 // 事件内容的滚动条优化
          /*  $('.maed-clb-list').niceScroll({
                cursorcolor: "rgba(0,160,233,.4)",//#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "6px", //像素光标的宽度
                cursorborder: "0", // 游标边框css定义
                cursorborderradius: "3px",//以像素为光标边界半径
                autohidemode: false, //是否隐藏滚动条
                background: "rgba(29,32,136,.4)",
            });
            $('.mae-container>.lmcc-mask-open').click(function () {
                $(this).hide();
                //$(".maed-cl-top").getNiceScroll().hide();
                //$(".maed-clb-list").getNiceScroll().hide();
                $('.lmcc-mask-box').show();
                $('.lmcc-mask').animate({ "right": "0" }, 100, function () {
                    $('.lmcc-mask-box').css({ "overflow": "unset" });
                    $('.layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
                    //$(".layer_aj_bt_s").getNiceScroll().resize();
                });
            });
            $('.lmcc-mask-box>.lmcc-mask-open').click(function () {
                $('.lmcc-mask-box').css({ "overflow": "hidden" });
                $('.lmcc-mask').animate({ "right": "-100%" }, 100, function () {
                    $('.lmcc-mask-box').hide();
                    //$(".layer_aj_bt_s").getNiceScroll().resize();
                  //  $(".maed-cl-top").getNiceScroll().show();
                   // $(".maed-clb-list").getNiceScroll().show();
                    $('.mae-container>.lmcc-mask-open').show();
                });
            });*/
           

            // 环节选择
            $('.mael-crir-link').on('click', '.mael-crirl-item', function () {
                $(this).addClass('active').siblings().removeClass('active');
            });

            // 弹窗间切换
            $('.evt-tt').on('click', function () {
                $('#mae-event-list').removeClass('mae-container-on');
                $('#mae-event-detail').addClass('mae-container-on');

                //事件详情图片轮番
                var swiper;
                function picSwiper() {
                    var swiI = 1;
                    swiper = new Swiper('.swiper-container', {
                        allowTouchMove: false,
                        navigation: {
                            nextEl: '.swiper-button-next1',
                            prevEl: '.swiper-button-prev1',
                        },
                    });
                    //图片轮番的页数
                    $('.swiper-button-next1, .swiper-button-prev1').on('click', function () {
                        swiI = swiper.activeIndex;
                        swiI += 1;
                        $('.lmcccrb-page p span').text(swiI);
                    });
                }
                picSwiper();
            });
            
            $(".validatebox-text").css("padding-top","0.05rem");

        });
        
var allLoadTime = 0; 
var todoPage=0;
var allPage=0;

function showTodo(){

	getList('todo',$("#eventTypePage").find(".current").html(),8);
}

function showAll(){

	if(allLoadTime==0){
		getListAll('all',1,8);
		allLoadTime=allLoadTime+1;
	}else{
		getListAll('all',$("#eventTypePageAll").find(".current").html(),8);
	}
}

function getList(type,pageNo,pageSize){
	$('#pageDiv').show();
	$('#eventtodoList').show();
	$('#searchForTodo').show();
	$('#pageDivAll').hide();
	$('#eventallList').hide();
	$('#searchForAll').hide();
	
	//获取来源平台查询条件（巡防类型）
	
	var bizPlatformStr;
	var patrolTypeStr;
	var bizPlatformForQueryStr;
	
	var sourceSearch=$('#bizPlatform').val();
	if(sourceSearch){
		if(sourceSearch=='xunfan'){
			patrolTypeStr='1,2';
			bizPlatformStr='0,3601001';
			bizPlatformForQueryStr='0';
		}else if(sourceSearch=='ai'){
			bizPlatformStr='3601002';
			bizPlatformForQueryStr='3601002';
		}else if(sourceSearch=='duban'){
			patrolTypeStr='0';
			bizPlatformStr='0';
			bizPlatformForQueryStr='0';
		}
	}
	
 modleopen();
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/nanChang3D/listData.json',
		dataType : "json",
		data: {'page':pageNo,'rows':pageSize,'eventType': type,'gridId':$('#gridId').val(),'patrolType':patrolTypeStr,'bizPlatform':bizPlatformStr,'infoOrgCodeForVerify':$('#infoOrgCodeForVerify').val(),
		'happenTimeStart':$('#happenTimeStart').val(),'happenTimeEnd':$('#happenTimeEnd').val(),'bizplatformForQuery':bizPlatformForQueryStr,'infoOrgCode':$('#infoOrgCode').val(),
		'type': '${type}','eventStatus':'${status}','isCapEventAdditionalColumn':true}, 
		success: function (data) {
		    if(!$('#happenTimeStart').val()&&!$('#happenTimeEnd').val()&&!$('#gridId').val()&&!$('#bizPlatform').val()){
		    	$("#todoTotal",window.parent.document).html(data.total);
		    }
		    
			if(data.total<pageSize){
			    $("#pageDiv").hide();
			}else{
			    $("#pageDiv").show();
			}
			if(todoPage==0||pageNo==1){
			$("#eventTypePage").remove();
			$("#pageDiv").html('<div id="eventTypePage" style="font-size:14px;margin-top:-50px;zoom:0.7" class="list_div"></div>');
			$("#eventTypePage").createPage({
				pageNum: (Math.floor((data.total-1)/pageSize)+1),
				current: pageNo,
				backfun: function(e) {
					getList(type,e.current,pageSize);
				}
			});
			todoPage=todoPage+1;
			}
			setEvent(data,type);
			
		},
		complete:function(){ modleclose();}
		//error: function (data) {layer.alert('连接超时！');}

	})
}


function getListAll(type,pageNo,pageSize){
	$('#pageDiv').hide();
	$('#eventtodoList').hide();
	$('#searchForTodo').hide();
	$('#pageDivAll').show();
	$('#eventallList').show();
	$('#searchForAll').show();
	
	//获取来源平台查询条件（巡防类型）
	var bizPlatformStr;
	var patrolTypeStr;
	var bizPlatformForQueryStr;
	
	var sourceSearch=$('#bizPlatformForAll').val();
	if(sourceSearch){
		if(sourceSearch=='xunfan'){
			patrolTypeStr='1,2';
			bizPlatformStr='0,3601001';
			bizPlatformForQueryStr='0';
		}else if(sourceSearch=='ai'){
			bizPlatformStr='3601002';
			bizPlatformForQueryStr='3601002';
		}else if(sourceSearch=='duban'){
			patrolTypeStr='0';
			bizPlatformStr='0';
			bizPlatformForQueryStr='0';
		}
	}
	
 modleopen();
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/nanChang3D/listData.json',
		dataType : "json",
		data: {'page':pageNo,'rows':pageSize,'eventType': type,'gridId':$('#gridIdForAll').val(),'patrolType':patrolTypeStr,'bizPlatform':bizPlatformStr,
		'happenTimeStart':$('#happenTimeStartForAll').val(),'happenTimeEnd':$('#happenTimeEndForAll').val(),'bizplatformForQuery':bizPlatformForQueryStr,'eInfoOrgCode':$('#infoOrgCodeForAll').val(),
		'type': '${type}','eventStatus':'${status}','isCapEventAdditionalColumn':true}, 
		success: function (data) {
		    
			if(data.total<pageSize){
			    $("#pageDivAll").hide();
			}else{
			    $("#pageDivAll").show();
			}
			if(allPage==0||pageNo==1){
			$("#eventTypePageAll").remove();
			$("#pageDivAll").html('<div id="eventTypePageAll" style="font-size:14px;margin-top:-50px;zoom:0.7" class="list_div"></div>');
			$("#eventTypePageAll").createPage({
				pageNum: (Math.floor((data.total-1)/pageSize)+1),
				current: pageNo,
				backfun: function(e) {
					getListAll(type,e.current,pageSize);
				}
			});
			allPage=allPage+1;
			}
			setEventAll(data,type);
		},
		complete:function(){ modleclose();}
		//error: function (data) {layer.alert('连接超时！');}

	})
}


function setEvent(data,t){
	var str = "",d="";
	for(var i=0,l=data.rows.length;i<l;i++){
		d = data.rows[i];
		str+="<tr>"+
		"<td><div onclick='showEventInfo("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")' class='evt-tt' title='"+strFn(d.eventName)+"'>"+strFn(d.eventName)+"</div></td>"+
		"<td style='cursor: pointer' onclick='showEventInfo("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")' title='"+d.occurred+"'>"+strFn(d.occurred)+"</td>"+
		"<td style='cursor: pointer' onclick='showEventInfo("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")'>"+d.happenTimeStr+"</td>"+
		"<td style='cursor: pointer' onclick='showEventInfo("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")'>"+fomPlatform(d.bizPlatform,d.patrolType)+"</td>"+
		"</tr>";
	}

	$("#event"+t+"List").html(str);
	if(isAndroid){
	//if(navigator.userAgent.match("ua_ffcs")){
		$('.evt-tt').css('margin-left','0.3rem');			
	}
	//$("#tableDiv").css({'height':'5.18rem','overflow':'hidden'});
}

function setEventAll(data,t){
	var str = "",d="";
	for(var i=0,l=data.rows.length;i<l;i++){
		d = data.rows[i];
		str+="<tr>"+
		"<td><div onclick='showEventInfoAll("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")' class='evt-tt' title='"+strFn(d.eventName)+"'>"+strFn(d.eventName)+"</div></td>"+
		"<td style='cursor: pointer' onclick='showEventInfoAll("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")' title='"+d.occurred+"'>"+strFn(d.occurred)+"</td>"+
		"<td style='cursor: pointer' onclick='showEventInfoAll("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")'>"+d.happenTimeStr+"</td>"+
		"<td style='cursor: pointer' onclick='showEventInfoAll("+d.eventId+","+d.bizPlatform+","+d.status+",0,"+d.instanceId+")'>"+fomPlatform(d.bizPlatform,d.patrolType)+"</td>"+
		"</tr>";
	}

	$("#event"+t+"List").html(str);
	if(isAndroid){
	//if(navigator.userAgent.match("ua_ffcs")){
		$('.evt-tt').css('margin-left','0.3rem');			
	}
	//$("#tableDiv").css({'height':'5.18rem','overflow':'hidden'});
}

function strFn(str){
	if(str == null ||str == 'null' || str == undefined ){
		return '';
	}
	
	if(str.length>13){
		return str.substring(0,13)+"...";
	}else{
		return str;
	}
}
function fomPlatform(str,patrolType){
    if(str == null ||str == 'null' || str == undefined){
        return '';
    }else if(str=='3601002'){
        return 'AI分析事件';
    }else if(null!=patrolType&&0!=patrolType){
        return '巡访事件';
    }else{
        return '平台事件';
    }
}

function showEventInfoAll(eventid,bizplatform,status,pointReFlag,instanceId){
	$('#mae-event-list').removeClass('mae-container-on');
    $('#mae-event-detail').addClass('mae-container-on');
    var str="";
    if(instanceId){
        str="<iframe class='mask-event' src='${rc.getContextPath()}/zhsq/nanChang3D/getEventInfo.jhtml?eventId="+eventid+"' frameborder='0' width='100%' height='100%'></iframe>"
    }else{//没有流程示例id则表示为审核事件跳转审核页面
        str="<iframe class='mask-event' src='${rc.getContextPath()}/zhsq/nanChang3D/toEventVerifyInfo.jhtml?eventVerifyId="+eventid+"' frameborder='0' width='100%' height='100%'></iframe>"
    }
    $("#event_detail").html(str);
    
    //设置按钮
    $("#event_detail_button").html('<div class="maed-btn-box clearfix" style="margin-top:-5px"><a id="backlist" style="margin-right: 60px" href="javascript:void(0);" class="fr">返回</a></div>');
    
    $('#backlist').on('click', function () {
        $('#mae-event-detail').removeClass('mae-container-on');
        $('#ascrail2000').css('display','none');
        $('#mae-event-handle').removeClass('mae-container-on');
        $('#mae-event-list').addClass('mae-container-on');
        getListAll('all',$("#eventTypePageAll").find(".current").html(),8);
        
    });
    
}

function showEventInfo(eventid,bizplatform,status,pointReFlag,instanceId){


    $('#mae-event-list').removeClass('mae-container-on');
    $('#mae-event-detail').addClass('mae-container-on');
    var str="";
    if(instanceId){
        str="<iframe class='mask-event' src='${rc.getContextPath()}/zhsq/nanChang3D/getEventInfo.jhtml?eventId="+eventid+"' frameborder='0' width='100%' height='100%'></iframe>"
    }else{//没有流程示例id则表示为审核事件跳转审核页面
        str="<iframe class='mask-event' src='${rc.getContextPath()}/zhsq/nanChang3D/toEventVerifyInfo.jhtml?eventVerifyId="+eventid+"' frameborder='0' width='100%' height='100%'></iframe>"
    }
    $("#event_detail").html(str);
    
    //设置操作按钮
    
    //首先判断是不是审核事件（有无instanceId）
    if(instanceId){//
    
        if(status=='04'||status=='06'){
            if(pointReFlag=='0'){
            $("#event_detail_button").html('<div class="maed-btn-box clearfix" style="margin-top:-5px"><a id="backlist" style="margin-right: 60px" href="javascript:void(0);" class="fr">返回</a></div>');
            }else{
            $("#event_detail_button").html('');
            }
        }else{
            $.ajax({
   		        type: "POST",
   		        //async:false,
		        url: '${rc.getContextPath()}/zhsq/nanChang3D/getCurPro.json',
		        dataType : "json",
		        data: {eventId:eventid}, 
		        success: function (data) {
		        
		        var insid=data[data.length-1].INSTANCE_ID;//获取流程实例id
		        var curHandle=data[0].userInfoCurrentUser;//是否时当前办理人
                if(pointReFlag=='0'){
                    if(curHandle){
                    $("#event_detail_button").html('<div class="maed-btn-box clearfix" style="margin-top:-5px"><a id="eventhandle" onclick="showHandleDetail('+eventid+','+insid+')" href="javascript:void(0);" class="fl handle">办理</a><a id="backlist" href="javascript:void(0);" class="fr">返回</a></div>');
                    }else{
                    $("#event_detail_button").html('<div class="maed-btn-box clearfix" style="margin-top:-5px"><a id="backlist" style="margin-right: 60px" href="javascript:void(0);" class="fr">返回</a></div>');
                    }
                }else{
                    if(curHandle){
                    
                    $("#event_detail_button").html('<div class="maed-btn-box clearfix" style="margin-top:-5px"><a style="margin-left:50px" id="eventhandle" onclick="showHandleDetail('+eventid+','+insid+')" href="javascript:void(0);" class="fl handle">办理</a></div>');
                    }else{
                    $("#event_detail_button").html('');
                    
                    }
                }
                
                $('#backlist').on('click', function () {
                $('#mae-event-detail').removeClass('mae-container-on');
                $('#ascrail2000').css('display','none');
                $('#mae-event-handle').removeClass('mae-container-on');
                $('#mae-event-list').addClass('mae-container-on');
                getList('todo',$(".current").html(),8);
        
                });
                    
                $('#backdetail').on('click', function () {
                $('#mae-event-detail').addClass('mae-container-on');
                $('#ascrail2000').css('display','none');
                $('#mae-event-handle').removeClass('mae-container-on');
                showEventInfo(eventid,bizplatform,status,pointReFlag,instanceId);
                });
            
            }
	        });
            
        }
    
    }else{//审核事件操作按钮：提交，作废，返回
    
        $("#event_detail_button").html('<div class="mael-c-bottom clearfix" style="margin-top:-5px"><a id="eventhandle" onclick="submitVerify('+0+')" href="javascript:void(0);" class="fl handle"><p>提交</p></a><a onclick="setInvalid('+eventid+')" href="javascript:void(0);" class="fl mlr30"><p>作废</p></a><a id="backlist" href="javascript:void(0);" class="fr"><p>返回</p></a></div>');
    
    }
    
    
    $('#backlist').on('click', function () {
        $('#mae-event-detail').removeClass('mae-container-on');
        $('#ascrail2000').css('display','none');
        $('#mae-event-handle').removeClass('mae-container-on');
        $('#mae-event-list').addClass('mae-container-on');
        getList('todo',$("#eventTypePage").find(".current").html(),8);
        
    });
    
    $('#backEventVerify').on('click', function () {
        $('#ascrail2000').css('display','none');
        $('#mae-event-handle').removeClass('mae-container-on');
        $('#mae-event-add').removeClass('mae-container-on');
        $('#mae-event-list').removeClass('mae-container-on');
        $('#mae-event-detail').removeClass('mae-container-on');
        showEventInfo(eventid,bizplatform,status,pointReFlag,instanceId);
        
    });
    
    
    $('#backdetail').on('click', function () {
        $('#mae-event-detail').addClass('mae-container-on');
        $('#ascrail2000').css('display','none');
        $('#mae-event-handle').removeClass('mae-container-on');
        showEventInfo(eventid,bizplatform,status,pointReFlag,instanceId);
    });
    
     

}


function submitEvent(){

$('#event_add_page')[0].contentWindow.showAdvice('saveEventAndStart', null, '0');

}

     function showHandleDetail(eventid,insid){
        modleopen(); 
     
        $('#mae-event-detail').removeClass('mae-container-on');
        $('#mae-event-handle').addClass('mae-container-on');
        
        // 清空人员选择器
		$('#userTable').datagrid('loadData', { total : 0, rows : [] });
		$('#selUserTable').datagrid('loadData', { total : 0, rows : [] });
		// $("#promoter").attr("checked",false);
		$("#fast_user_div").hide();
        
         //初始化附件上传
         var bigFileUploadOpt = {
             useType: 'edit',
             <@block name="fileTypes">
             fileExt: '.jpg,.gif,.png,.jpeg,.webp',
             </@block>
             maxSingleFileSize: 50,
             attachmentData: {'bizId':eventid,'attachmentType':'ZHSQ_EVENT','eventSeq':'1,2,3', 'isBindBizId': 'yes'},
             module: 'event',
             styleType: 'list'
         };

         //初始化附件上传
         $('#bigFileUploadDiv').html('');
         bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);

         $('#bigFileUploadDiv').delay(500).promise().done(defaultRadioCheck);//延时加载设置默认选中项(延时0.5秒)，为了能在图片上传控件加载完成后，才调用


         //获取工作流详情
        
        $.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/nanChang3D/getWorkflowHandleInfo.json',
		dataType : "json",
		data: {eventId:eventid,instanceId:insid}, 
		success: function (data) {
		    
		    $('#formId').val(eventid);
		    $('#instanceId').val(insid);
		    $('#curTaskId').val(data.taskId);
		    $('#deploymentId').val(data.deploymentId);
		    $('#curNodeName').val(data.curNode.nodeName);
		    
		    //设置下一环节
		    var strNextNodes='';
		    var nextNodes=data.taskNodes;
		    for(var i=0,j=nextNodes.length;i<j;i++){
		 
		    strNextNodes+='<a onclick="radioCheckOperate('+eventid+',\''+data.curNode.nodeName+'\',\''+nextNodes[i].nodeName+'\',\''+nextNodes[i].transitionCode+'\','+nextNodes[i].nodeId+','+insid+')" href="javascript:void(0);" class="mael-crirl-item clearfix">';
		    strNextNodes+='<div class="fl" style="margin-top: -0.02rem"><i></i></div>'+
		    '<p>'+nextNodes[i].nodeNameZH+'</p></a>';
		    
		    }
		    
		    
		    $('#nextTaskNode').html(strNextNodes);
		    
		    $(".mael-crirl-item")[0].click();//默认第一个选中
		    $('#ascrail2000').css('display','block');
		    
		    //设置按钮
		    var opList=data.operateLists;
		    if(opList.length==2){//有驳回按钮
		        var btnstr='<div class="mael-c-bottom clearfix">';
		        btnstr+='<a onclick="subTask()" href="javascript:void(0);" class="fl sumbit"><p>提交</p></a>'+
		        '<a id="rejectButton" onclick="rejectTask()" href="javascript:void(0);" class="fl mlr30"><p>驳回</p></a>'+
		        '<a id="backdetail" href="javascript:void(0);" class="fr"><p>取消</p></a></div>';
		        
		        $('#workflowBtn').html(btnstr);
		        
		    }else{
		        var btnstr='<div class="maed-btn-box clearfix clearfix">';
		        btnstr+='<a onclick="subTask()" href="javascript:void(0);" class="fl sumbit"><p>提交</p></a>'+
		        '<a id="backdetail" href="javascript:void(0);" class="fr"><p>取消</p></a></div>';
		        
		        $('#workflowBtn').html(btnstr);
		    }
		    
		    $('#backdetail').on('click', function () {
                $('#mae-event-detail').addClass('mae-container-on');
                $('#ascrail2000').css('display','none');
                $('#mae-event-handle').removeClass('mae-container-on');
                showEventInfo(eventid,bizplatform,status,pointReFlag);
            });
		    
		    modleclose(); 
		    
		    
		}

	    })
        
     } 
     
     function defaultRadioCheck(){}//附件上传回掉函数，不定义会报错
     
     
        //下一办理环节选中操作
		function radioCheckOperate(formId_,curnodeName_,nodeName_,nodeCode_,nodeId_,insid) {
		
		    $('#userIds').val('');
			$('#curOrgIds').val('');
			$('#curOrgNames').val('');
			$('#userNames').html('');
			$('#htmlUserNames').html('');
			$('#htmlUserIds').html('');
			
			$('#nextNode').val(nodeName_);
			$('#nextNodeCode').val(nodeCode_);
			$('#nextNodeId').val(nodeId_);
			
			if('null'==nodeCode_){
                nodeCode_='';
            }
			
			// 通过ajax请求判断是否隐藏人员选择器，如果是上报越级，隐藏人员选择器，并将值传入userIds和userNames
			$.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/keyelement/keyElementController/getNodeInfoForEvent.jhtml",
				data:{
					formId : formId_,
					formType : '300',
					curnodeName: curnodeName_,
					nodeName: nodeName_,
					nodeCode: nodeCode_,
					nodeId: nodeId_,
					instanceId: insid
				},
				dataType:"json",
				success:function(data) {
					
					g_EventNodeCode = data.eventNodeCode;
					
					if (data.msg && data.msg != "") {
						$.messager.alert("警告", data.msg, "warning");
					} else {
					
					    if(!data.isSelectOrg&&!data.isSelectUser){//不用选择人环节
					    
					        $('#handlePerson').hide();
					        $('#evaLev').hide();
					        if(curnodeName_=='task9'){//评价环节 
					        
					        $('#evaLev').show();
					        $('#defaultSelect').click();
					        
					        }
					    
					    }else{
					    	$('#evaLev').hide();
					    	$('#handlePerson').show();
							//选择人员的情况
							if(data.isSelectUser){
						    	$('#selectOrgButton').hide();
						    	if(isAndroid){
								//if(navigator.userAgent.match("ua_ffcs")){
									$('#userName_div').css("margin-top","-0.1rem");
									$('#nextTaskNode').css("margin-top","0.04rem");
								}else{
									$('#userName_div').css("margin-top","-0.01rem");
								}
						    	$('#userIds').val(data.userIds);
			                	$('#curOrgIds').val(data.orgIds);
			                	$('#userNames').html(data.userNames);
			                	$('#htmlUserIds').html(data.userIds);
	
							}else{
						    	$('#selectOrgButton').show();
								$('#userName_div').css("margin-top","3px");
							}
						
						
					    }
					    
					    
						if(data.isShowInterval) {
							$("#intervalTr").show();
							$(".validatebox-text").css({"background-color":"rgba(0, 156, 255, .2)","border": "solid .01rem #009cff","color":"white"});
						}else{
							$("#intervalTr").hide();
						}
							
					}
				}
			});
		}
		
		
		
	//打开人员选择窗口
	function _selectHandler() {
		$('#userSelectorBtnList').width(260);
		var obj = {
			nextNodeId				: $('#nextNodeId').val(),
			nextUserIdElId 			: 'userIds',
			nextUserNameElId 	: 'userNames',
			nextOrgIdElId 			: 'curOrgIds',
			nextOrgNameElId 	: 'curOrgNames',
			nextNodeType 			: 'task',
			formId					: $("#formId").val(),
			formType				: '300',
			instanceId				: $("#instanceId").val()
		};
		
		selectUserByObj(obj);
		if(isAndroid){
		//if(navigator.userAgent.match("ua_ffcs")){
			$('.window-shadow').css('display','none');			
			$('.window').css('zoom','0.76');			
			$('.window').css('margin-left','1.5rem');			
			$('.window').css('margin-top','0.5rem');			
		}else{
			$('.window-shadow').css('display','none');			
		}
		
	}
	
	
	function subTask(){
	    if($('#handlePerson').is(':hidden')){
            if(!$('#adviceText').val()){
                $.messager.alert("警告", "请填写办理意见", "info");
                return;
            }
      
        }else{
        
            if(!$('#userIds').val()){
                $.messager.alert("警告", "请选择办理人员", "info");
                return;
            }else{
                if(!$('#adviceText').val()){
                    $.messager.alert("警告", "请填写办理意见", "info");
                    return;
                }
            }
        
        }
	
	    modleopen();
	    
	    $.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml",
				data:{
					advice : $('#adviceText').val(),
					nextNode : $('#nextNode').val(),
					nextStaffId: $('#userIds').val(),
					instanceId:$('#instanceId').val(),
					deploymentId:$('#deploymentId').val(),
					curOrgIds:$('#curOrgIds').val(),
					formId:$('#formId').val(),
					taskId:$('#curTaskId').val(),
					evaLevel:$('#evaLevel').val(),
					curNodeName:$('#curNodeName').val(),
					eventHandleInterval:$('#eventHandleInterval').val()
				},
				dataType:"json",
				success:function(data) {
				    
				    modleclose();
				    $('#adviceText').val('');
                   // $('#mae-event-handle').removeClass('mae-container-on');
				    //$('#mae-event-detail').addClass('mae-container-on');
                   // $('#ascrail2000').css('display','none');
                    $('#mae-event-detail').removeClass('mae-container-on');
                    $('#ascrail2000').css('display','none');
                    $('#mae-event-handle').removeClass('mae-container-on');
                    $('#mae-event-list').addClass('mae-container-on');
                    getList('todo',$(".current").html(),8);
				}
				
		});
				
	}
	
	
	function rejectTask(){
	
	    
            if(!$('#adviceText').val()){
                $.messager.alert("警告", "请填写驳回意见", "info");
                return;
            }
      
        
	
	
	    $.messager.confirm('驳回提示', '确定要驳回吗？流程将返回上一操作步骤！', function(r) {
	    
			if (r) {
			    modleopen();
				$.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/rejectInstance.jhtml",
				data:{
					advice : $('#adviceText').val(),
					instanceId:$('#instanceId').val(),
					taskId:$('#curTaskId').val()
				},
				dataType:"json",
				success:function(data) {
				    
				    modleclose();
				    $('#adviceText').val('');
                   // $('#mae-event-handle').removeClass('mae-container-on');
				   // $('.mae-event-detail').addClass('mae-container-on');
                   // $('#ascrail2000').css('display','none');
                    $('#mae-event-detail').removeClass('mae-container-on');
                    $('#ascrail2000').css('display','none');
                    $('#mae-event-handle').removeClass('mae-container-on');
                    $('#mae-event-list').addClass('mae-container-on');
                    getList('todo',$(".current").html(),10);
				}
				
		        });
			}
		});
	
	
	}
	
	
	
	//事件作废
	function setInvalid(eventid) {
		    
		    $.messager.confirm('提示', '您确定作废该记录吗？', function(isCancel) {
	  			if(isCancel) {
	  			    modleopen();
	  			    $.ajax({
				        type: "POST",
	    		        url : '${rc.getContextPath()}/zhsq/eventWechat/invalidEventVerify.json',
				        data: {'eventVerifyId': eventid},
				        dataType:"json",
				        success: function(data){
					    	modleclose();
					
					    	if(data.success && data.success == true){
                    			//$('#mae-event-handle').removeClass('mae-container-on');
				    			//$('.mae-event-detail').addClass('mae-container-on');
               			       // $('#ascrail2000').css('display','none');
                    			$('#mae-event-detail').removeClass('mae-container-on');
                   			    $('#ascrail2000').css('display','none');
                    			$('#mae-event-handle').removeClass('mae-container-on');
                    			$('#mae-event-list').addClass('mae-container-on');
                    			getList('todo',$("#eventTypePage").find(".current"),8);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '作废失败！', 'error');
			  					}
			  					
                    			//$('#mae-event-handle').removeClass('mae-container-on');
				    			//$('.mae-event-detail').addClass('mae-container-on');
                    			//$('#ascrail2000').css('display','none');
                    			$('#mae-event-detail').removeClass('mae-container-on');
                    			$('#ascrail2000').css('display','none');
                    			$('#mae-event-handle').removeClass('mae-container-on');
                    			$('#mae-event-list').addClass('mae-container-on');
                    			getList('todo',$("#eventTypePage").find(".current"),8);
			  					
			  				}
				    	},
				    	error:function(data){
					    	modleclose();
					
					    	$.messager.alert('错误','事件作废失败 ！','error');
				    	}
	    			});
	  			
	  			}
	  		});
		    
	}
	
	function submitVerify(flag){
	    $('.mask-event')[0].contentWindow.tableSubmit(flag);
	}
	
	
	function evaClick(lev){
	    $('#evaLevel').val(lev);
	}
	
	
	
	//打开遮罩
function modleopen() {
	$("<div class='datagrid-mask'></div>").css( {
		display : "block",
		width : "100%",
		height : $(window).height()
	}).appendTo("body");
	
	document.body.scroll="no";//除去滚动条
}

	
           

    </script>
</body>

</html>