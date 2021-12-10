<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl">
<style type="text/css">
.ConList .nav{position:relative;}
.ConList .nav .box{width:673px; height:36px; margin:0 25px; overflow:hidden; position:relative;}
.ConList .nav .box ul{position:absolute; top:0; z-index:2; height:36px; overflow:hidden;}
.ConList .nav .box li{position:relative;width:64px;}
.ConList .toLeft, .ConList .toRight{display: block;position: absolute;width: 21px;height: 36px;top: 0; z-index:3;}
.ConList .toLeft{left:0;background:url(${uiDomain}/images/toleft.png) no-repeat center;}
.ConList .toLeft:hover{background:url(${uiDomain}/images/tolefthover.png) no-repeat center;}
.ConList .toRight{right:0;background:url(${uiDomain}/images/toright.png) no-repeat center;}
.ConList .toRight:hover{background:url(${uiDomain}/images/torighthover.png) no-repeat center;}
.ConList .nav .box li div {
	position:absolute;top:0px;right:1px;height:15px;width:15px;
	background:url(${uiDomain}/images/dialogclose.png) no-repeat center;
}
</style>

</head>
<body>
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/saveOrUpdate.jhtml" method="post" >
		<input id="optType" name="optType" type="hidden" value="${optType!''}" />
		<div id="content-d" class="MC_con content light NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td><label class="LabName"><span>所属网格：</span></label>
	            	<#if !optType??>
	            		<div class="Check_Radio FontDarkBlue">${vo.regionName!''}</div>
	            	<#else>
		            	<input id="regionName" type="text" class="inp1 easyui-validatebox" 
		            	data-options="required:true"
		            	value="${vo.regionName!''}"/>
	            	</#if>
	            	<input id="regionCode" name="regionCode" type="hidden" value="${vo.regionCode!''}"/>
	            </td>
	            <td><label class="LabName"><span>事件类型：</span></label>
	            	<#if !optType??>
	            		<div class="Check_Radio FontDarkBlue">${vo.typeName!''}</div>
	            	<#else>
		            	<input id="typeName" type="text" class="inp1 easyui-validatebox" 
		            	data-options="required:true"
		            	value="${vo.typeName!''}" />
	            	</#if>
	            	<input id="type" name="type" type="hidden" value="${vo.type!''}" />
	            </td>
	            <td>
	            	<a href="###" class="NorToolBtn AddBtn" onclick="addProcCfg();" style="margin-top:3px;">添加配置</a>
	            </td>
	          </tr>
	        </table>
                <div class="NorForm ConList">
                	<div id="tabs" class="nav" style="margin-top:10px;">
	                	<a href="###" id="toLeft" class="link toLeft"></a>
	                    <div class="box">
	                        <ul style="left:0;">
	                            <#if !optType??>
	                    		<#list vo.etpcIds as etpcId>
	                    		<li>标准${etpcId_index + 1}</li>
	                    		</#list>
	                    		<#else>
	                    		<li class="current">标准1</li>
	                    		</#if>
	                        </ul>
	                    </div>
	                    <a href="###" id="toRight" class="link toRight"></a>
                	</div>
                	
                    <div class="tabss2">
                    <#if !optType??>
                    	<#list vo.etpcIds as etpcId>
                    		<div <#if etpcId_index != 0>clase="hide"</#if>>
	                            <input attrid="etpcIds" id="etpcIds${etpcId_index}" name="etpcIds[${etpcId_index}]" type="hidden" value="${etpcId?c}" />
	                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                              <tr>
	                                <td><label class="LabName"><span>事件采集标准：</span></label>
	                                	<input attrid="collectEventSpecs" id="collectEventSpecs${etpcId_index}" name="collectEventSpecs[${etpcId_index}]" type="text" class="inp1 easyui-validatebox" 
	                                	data-options="required:true,validType:['maxLength[500]','characterCheck']"
	                                	value="${vo.collectEventSpecs[etpcId_index]!''}"/>
	                                </td>
	                                <td><label class="LabName"><span>结案规范：</span></label>
	                                	<input attrid="eventCloseSpecs" id="eventCloseSpecs${etpcId_index}" name="eventCloseSpecs[${etpcId_index}]" type="text" class="inp1 easyui-validatebox" 
	                                	data-options="required:true,validType:['maxLength[500]','characterCheck']"
	                                	value="${vo.eventCloseSpecs[etpcId_index]!''}" />
	                                </td>
	                              </tr>
	                              <tr>
	                                <td><label class="LabName"><span>时限类型：</span></label>
	                                	<input attrid="timeLimitTypes" id="timeLimitTypes${etpcId_index}" name="timeLimitTypes[${etpcId_index}]" type="hidden" value="${vo.timeLimitTypes[etpcId_index]!''}"/>
	                                	<input attrid="timeLimitTypeNames" id="timeLimitTypeNames${etpcId_index}" type="text" class="inp1 easyui-validatebox" 
	                                	data-options="required:true"
	                                	value="" />
	                                </td>
	                                <td><label class="LabName"><span>时限值：</span></label>
	                                	<input attrid="timeLimitVals" id="timeLimitVals${etpcId_index}" name="timeLimitVals[${etpcId_index}]" type="text" class="inp1 easyui-validatebox" 
	                                	data-options="required:true,validType:['numLength[6]']"
	                                	value="${vo.timeLimitVals[etpcId_index]?c}" />
	                                </td>
	                              </tr>
	                            </table>
	                        </div>
                    	</#list>
                    <#else>
						<div>
                            <input attrid="etpcIds" id="etpcIds0" name="etpcIds[0]" type="hidden" value="" />
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td><label class="LabName"><span>事件采集标准：</span></label>
                                	<input attrid="collectEventSpecs" id="collectEventSpecs0" name="collectEventSpecs[0]" type="text" class="inp1 easyui-validatebox" 
                                	data-options="required:true,validType:['maxLength[500]','characterCheck']"
                                	value=""/>
                                </td>
                                <td><label class="LabName"><span>结案规范：</span></label>
                                	<input attrid="eventCloseSpecs" id="eventCloseSpecs0" name="eventCloseSpecs[0]" type="text" class="inp1 easyui-validatebox" 
                                	data-options="required:true,validType:['maxLength[500]','characterCheck']"
                                	value="" />
                                </td>
                              </tr>
                              <tr>
                                <td><label class="LabName"><span>时限类型：</span></label>
                                	<input attrid="timeLimitTypes" id="timeLimitTypes0" name="timeLimitTypes[0]" type="hidden" />
                                	<input attrid="timeLimitTypeNames" id="timeLimitTypeNames0" type="text" class="inp1 easyui-validatebox" 
                                	data-options="required:true"
                                	value="" />
                                </td>
                                <td><label class="LabName"><span>时限值：</span></label>
                                	<input attrid="timeLimitVals" id="timeLimitVals0" name="timeLimitVals[0]" type="text" class="inp1 easyui-validatebox" 
                                	data-options="required:true,validType:['numLength[6]']"
                                	value="" />
                                </td>
                              </tr>
                            </table>
                        </div>
                    </#if>
                	</div>
                </div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	            <a href="###" class="BigNorToolBtn SaveBtn" onclick="tableSubmit();">保存</a>
	            <a href="###" class="BigNorToolBtn CancelBtn" onclick="cancl();">取消</a>
	        </div>
	    </div>
	</form>
</body>

<script type="text/javascript">
	
	$(function() {
        <#if !optType??>
        	<#list vo.etpcIds as etpcId>
        	initByIndex(${etpcId_index}, ["${vo.timeLimitTypes[etpcId_index]!''}"]);
        	</#list>
        <#else>
        	AnoleApi.initGridZtreeComboBox("regionName", null, function(gridId, items) {
				if (items && items.length > 0) {
					$("#regionCode").val(items[0].orgCode);
				}
			});
	        
	        AnoleApi.initTreeComboBox("typeName", "type", "A001093199");
        	initByIndex(0);
        </#if>
        
        initTabs();
        
        $("#tabs li").eq(0).click();
        
        $('.link').bind('click', function() {
			doMove($(this).attr('id'));
		});
		
		wLi = $("#tabs li").eq(0).outerWidth(true);
        
        initCloseBtn();
	});
	
	function initTabs() {
		var tabsDiv = $("#tabs ul li");
		tabsDiv.unbind('click').bind('click', function() {
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = tabsDiv.index(this);
			$(".tabss").children().eq(NavIndex2).show().siblings().hide();
			$(".tabss2").children().eq(NavIndex2).show().siblings().hide();
		});
	}
	
	function initCloseBtn() {
		$("#tabs li div").remove();
		$("#tabs li").append('<div onclick="_close(this);"></div>');
		/*if ($("#tabs li").length > 1) {
			$("#tabs li:last").append('<div onclick="_close(this);"></div>');
		}*/
	}
	
	function initByIndex(index, selectNodes) {
		AnoleApi.initListComboBox("timeLimitTypeNames" + index, "timeLimitTypes" + index, null, null, selectNodes, {
			DataSrc: [{
				name : "小时", value : "H"
			}, {
				name : "日", value : "D"
			}, {
				name : "工作日", value : "WD"
			}]
		});
		
		initTabs();
		
		$("input[attrid]").each(function() {
			$(this).unbind('focus').bind('focus', function() {
				var id = $(this).attr('id');
				var attrid = $(this).attr('attrid');
				if (id != '' && attrid != '') {
					var i = parseInt(id.replace(attrid, ''));
					gotoTab(i);
				}
			});
		});
	}
	
	function addProcCfg() {
		var cfgDivs = $(".tabss2").children();
		var index = cfgDivs.length;
		/*if (index > 6) {
			$.messager.alert('提示', "最多只能增加7个选项卡", 'warning');
			return ;
		}*/
		var cfgDiv = cfgDivs.eq(0).clone(false).hide();
		cfgDiv.find("input").each(function() {
			$(this).val('');
			if ($(this).attr("name") != '') {
				$(this).removeAttr("name").attr("name", $(this).attr("attrid") + "["+index+"]");
			}
			if ($(this).attr("id") != '') {
				$(this).removeAttr("id").attr("id", $(this).attr("attrid") + index);
			}
		});
		cfgDiv.attr("id", "cfgDiv" + index);
		$(".tabss2").append(cfgDiv);
		$.parser.parse('#' + cfgDiv.attr('id'));
		var li = $("#tabs li").eq(0).clone(false).html('标准'+(index+1));
		$("#tabs ul").append(li);
		initByIndex(index);
		initCloseBtn();
		gotoTab(index);
	}
	
	function doMove(direction) {
		var min = 35;
		var liNum = $("#tabs li").length;
		var standard = wLi * 7;
		var curLeft = $("#tabs li").eq(0).offset().left;
		var lastLiIndex = (standard - curLeft + min) / wLi;
		var iLeft = parseInt($("#tabs ul").css('left'));
		if (direction == "toRight") {
			if (liNum > lastLiIndex) {
				$("#tabs ul").css('left', iLeft - wLi);
			}
		} else if(direction =="toLeft") {
			if (iLeft < 0) {
				$("#tabs ul").css('left', iLeft + wLi);
			}
		}
	}
	
	function gotoTab(index) {
		var min = 35;
		var liNum = $("#tabs li").length;
		var standard = wLi * 7;
		var curLeft = $("#tabs li").eq(0).offset().left;
		var lastLiIndex = (standard - curLeft + min) / wLi;
		var iLeft = parseInt($("#tabs ul").css('left'));
		if (index + 1 > lastLiIndex) {// 往右
			for (var i = 0; i < index + 1 - lastLiIndex; i++) {
				doMove("toRight");
			}
		} else if (index + 1 < lastLiIndex - 6) {// 往左
			for (var i = 0; i < lastLiIndex - 6 - (index + 1); i++) {
				doMove("toLeft");
			}
		}
		$("#tabs li").eq(index).click();
	}
	
	function _close(curObj) {
		var i = $("#tabs li").index($(curObj).parent());
		$(curObj).parent().remove();
		$(".tabss2").children().eq(i).remove();
		initCloseBtn();
		gotoTab(i - 1);
	}
	
	function tableSubmit() {
		var isValid =  $("#tableForm").form('validate');
		if(isValid && checkNum()) {
			layer.load(0);
			$("#tableForm").ajaxSubmit(function(data) {
				if (!data.success && data.type == 'OPERATE') {
					$.messager.alert('提示', data.tipMsg, 'warning');
				} else {
					if (data.type == 'ADD') {
						parent.reloadDataForSubPage(data.tipMsg);
					} else {
						parent.reloadDataForSubPage(data.tipMsg, true);
					}
				}
				layer.closeAll('loading');
			});
		}
	}
	
	function checkNum() {
		return true;
	}
	
	function cancl() {
		parent.closeMaxJqueryWindow();
	}

</script>
</html>
