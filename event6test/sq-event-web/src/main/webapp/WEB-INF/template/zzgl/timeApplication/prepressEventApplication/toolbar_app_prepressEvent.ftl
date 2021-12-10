<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.w190{width:190px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<form id="timeAppQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!}" />
		<input type="hidden" name="eventStatus" class="queryParam" value="00,01,02,03,04" />
		<input type="hidden" name="isCapApplicant" class="queryParam" value="true" />
		
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属网格：</li>
                    <li>
                        <input type="text" id="infoOrgCode" name="infoOrgCode" class="hide queryParam" />
                        <input id="gridName" type="text" class="inp1 InpDisable w150" />
                    </li>
                    <li>事件分类：</li>
	                <li>
	                	<input type="text" id="type" name="eventType" class="queryParam hide"/>
	                	<input type="text" id="typeName" class="inp1 InpDisable w150" />
	                </li>
	                <li>关键字：</li>
	                <li><input name="eventKeyRemarkWord" type="text" class="inp1 keyBlank w190 queryParam" id="eventKeyWord" value="事件描述/标题/事发详址/补充描述" defaultValue="事件描述/标题/事发详址/补充描述" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr <#if listType?? && listType=='4'>class="hide"</#if>>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>审核情况：</span></label>
	                                    			<input id="timeAppCheckStatus" name="timeAppCheckStatus" type="text" value="" class="queryParam hide"/>
	                                    			<input id="timeAppCheckStatusName" type="text" class="inp1 InpDisable w150" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 w150 queryParam" type="text" id="code" name="code" ></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>采集时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="eventCreateTimeStart" name="eventCreateTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="eventCreateTimeEnd" name="eventCreateTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
	                                    	</tr>
	                                    </table>
	                                </div>
	                                <div class="BottomShadow"></div>
	                            </div>
	                        </div>
	                    </div>
	            	</li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
	        <div class="clear"></div>‍
	        
		</div>
		<div class="h_10 clear"></div>
		<div class="ToolBar hide" id="toolBarDiv">
	    	<div class="blind"></div><!-- 文字提示 -->
	    	<script type="text/javascript">
				function DivHide() {
					$(".blind").slideUp();//窗帘效果展开
				}
				function DivShow(msg) {
					$(".blind").html(msg);
					$(".blind").slideDown();//窗帘效果展开
					setTimeout("this.DivHide()",800);
				}
			</script>
	        <div id="actionDiv" class="tool fr hide">
				<@actionCheck></@actionCheck>
			</div>
		        
	    </div>
	</form>
</div>

<script type="text/javascript">
	var timeAppCheckStatusComboBox = null;
	
	$(function(){
        AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
            if(items && items.length > 0){
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
        
        timeAppCheckStatusComboBox = AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, ["3"], {
        	DataSrc : [{"name":"待审核", "value":"3"},{"name":"已通过", "value":"1"},{"name":"未通过", "value":"2"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	}
        });
        
        AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, { 
        	ChooseType : "1" ,
        	EnabledSearch : true,
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        if($("#actionDiv").find("a").length) {
        	$("#actionDiv").show();
        } else {
        	$("#toolBarDiv").css({'height': '0', 'border-top': 'none'});
        }
        
        $("#toolBarDiv").show();
	});
    
    function prepress() {
    	var applicationId = "";

        $("input[name='applicationId']:checked").each(function() {
            applicationId = $(this).val();
        });
        
		if(isBlankString(applicationId)) {
			$.messager.alert('提示', '请选中要预处理的事件!', 'warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/timeApplication/toAudit.jhtml?applicationId=" + applicationId + "&listType=" + $('#listType').val();
			
		  	openJqueryWindowByParams({
		  		title: "预处理事件信息",
		  		targetUrl: url
		  	});
		}
	}
	
	function selectItem(index, rec) {
    	var timeAppCheckStatus = rec.timeAppCheckStatus;
    	
    	if(timeAppCheckStatus && timeAppCheckStatus == '3') {
    		$("#prepress").show();
    	} else {
    		$("#prepress").hide();
    	}
    }
    
    function detail(eventId) {
		if(eventId == "") {
			$.messager.alert('警告','请选中要查看的事件!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=" + eventId;
			
		  	openJqueryWindowByParams({
		  		title: "查看事件信息",
		  		targetUrl: url
		  	});
		}
    }
	
	function resetCondition() {//重置
		$('#timeAppQueryForm')[0].reset();
		$('#eventKeyWord').addClass('keyBlank');
		<!--为了重置后，字典名称能正常展示-->
		timeAppCheckStatusComboBox.setSelectedNodes(["3"]);
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#timeAppQueryForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		return searchArray;
	}
	
    function doSearch(queryParams, isCurrent){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
	}
	
	function reloadDataForSubPage(msg, isCurrent) {
		try{
			closeMaxJqueryWindow();
		} catch(e) {}
		
		if(msg) {
			DivShow(msg);
		}
		
		searchData(isCurrent);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
	function _onfocus(obj) {
		if($(obj).hasClass("keyBlank")){
			$(obj).val("");
			$(obj).removeClass('keyBlank')
		}
	}
	
	function _onblur(obj) {
		var eventKeyWord = $(obj).val();
		
		if(eventKeyWord == ''){
			$(obj).addClass('keyBlank');
			$(obj).val($(obj).attr("defaultValue"));
		}
	}

	//删除事件数据详情报表

    function detailData() {
        var url = "${rc.getContextPath()}/zhsq/timeApplicationReportFordelEvent/toReportForDelEvent.jhtml";

        openJqueryWindowByParams({
            title: "事件删除统计",
			padding_top:0,
			padding_left:0,
			padding_bottom:0,
			padding_right:0,
            targetUrl: url
        });
    }
</script>