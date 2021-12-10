<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.w100{width:100px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="inspectionForm">
	        <div class="fl">
	        	<ul>
	                <li>所属网格：</li>
	                <li>
		                <input id="infoOrgCode" name="gridCode" value="${infoOrgCode!}" class="hide queryParam" />
		                <input type="text" id="gridName" name="gridName" value="${gridName!}" class="inp1 InpDisable w150"/>
		            </li>
		            <li>检查编号：</li>
	                <li>
		                <input type="text" id="inspectSeq" name="inspectSeq" value="" class="inp1 InpDisable w150 queryParam"/>
		            </li>
		            <li>检查时间：</li>
		            <li>
						<input type="text" style="cursor: pointer;" class="Wdate inp1 w100 fl queryParam" readonly name="startTime" id="startTimeStr" onclick="WdatePicker({readOnly:true})"/>
						<span class="Check_Radio" style="padding:0 5px;">至</span>
						<input  type="text" style="cursor: pointer;" class="Wdate inp1 w100 fl queryParam" readonly name="endStartTime" id="endStartTimeStr" onclick="WdatePicker({readOnly:true})"/>
				    </li>
		            
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:285px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>检查类型：</span></label>
	                                    			<input type="text" id="inspectType" name="inspectType" class="hide queryParam" />
	                								<input type="text" id="inspectTypeName" value="" class="inp1 w150" />
	                                    		</td>
	                                    	</tr>
	                                    </table>
	                                </div>
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
	        </div>‍
        </form>
        

	  	
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="toolBarDiv">
    	<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
		</script>
		<!--toolFrDiv代码格式不可换行，否则会影响布局-->
		<div class="tool fr" id="toolFrDiv"><@actionCheck></@actionCheck></div>
		
		<!--
       	<div class="tool fr">
       		<a href="#" class="NorToolBtn EditBtn"  onclick="report()">上报</a>
			<a href="#" class="NorToolBtn DelBtn"  onclick="del()">删除</a>
			<a href="#" class="NorToolBtn EditBtn" onclick="edit()" >编辑</a>
			<a href="#" class="NorToolBtn AddBtn" onclick="add()" >新增</a>
	  	</div> 
	  	-->

    </div>
	
</div>

<script type="text/javascript">
	var opt = {
		maxWidth: 735,
		maxHeight: 400
	};
	$(function(){
		if($('#toolFrDiv > a').length == 0){
			$("#toolBarDiv").remove();
		}
	
        AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
		
		AnoleApi.initTreeComboBox("inspectTypeName", "inspectType", "${FC_INSPECT_TYPE_PCODE!}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			},
			ChooseType : "1" 
		});

	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/fireControlCheck/toAdd.jhtml';
	   
	  	openJqueryWindowByParams($.extend({}, opt, {
	  		title: "新增消防检查",
	  		targetUrl: url
	  	}));
	}
	
	function edit() {
		var birId = "";
		$("input[name='birId']:checked").each(function() {
			birId = $(this).val();
		});
		if(birId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/fireControlCheck/toAdd.jhtml?birId=' + birId;
			
		  	openJqueryWindowByParams($.extend({}, opt, {
		  		title: "编辑消防检查",
		  		targetUrl: url
		  	}));
		}
	}

    //上报
    var reportBirId = 0;
    function report(){
        var rows = $('#list').datagrid('getSelections');
        var num=rows.length;
        if(num!=1){
            $.messager.alert('提示','请选择一条记录','info');
        }else{
            var isDel = rows[0].isDel;
            if(isDel!="2"){
                $.messager.alert('提示','该记录不能进行上报操作!','info');
                $('#list').datagrid('unselectAll');
            }else{
                var id=rows[0].birId;
                reportBirId = id;
                var eventName = "";
				if(rows[0].inspectObjName != null){
                    eventName = rows[0].inspectObjName;
				}
				if(rows[0].inspectTypeName != null){
                    eventName = eventName + rows[0].inspectTypeName;
                }
                var occurred=rows[0].inspectionAddr;
                var happenDateStr = rows[0].startTimeStr;
                var content = "被检查单位名称："+rows[0].inspectObjName+"。检查内容："+rows[0].content;
                var gridId = rows[0].gridId;
                var gridCode = rows[0].gridCode;
                var gridName = rows[0].gridName;
                var event = {
                    "eventName" : eventName==null?"":eventName,
                    "happenTimeStr" : happenDateStr==null?"":happenDateStr,
                    "occurred" : occurred==null?"":occurred,
                    "content" : content==null?"":content,
                    "gridId" : gridId==null?"":gridId,
                    "gridCode" : gridCode==null?"":gridCode,
                    "gridName" : gridName==null?"":gridName,
					"type" : rows[0].inspectType,
                    "typesForList" : rows[0].inspectType,//消防检查所属事件分类
                    "callBack" : 'parent.reportCallback', //上报完成后回调函数
                    "eventReportRecordInfo":{
						<#if INSPECT_INFO_BIZ_TYPE??>
							'bizType' : '${INSPECT_INFO_BIZ_TYPE}',
						</#if>
                        'bizId' : reportBirId
                    }
                };

                var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml';

                var reportWinId = openJqueryWindowByParams($.extend({}, opt, {
                    maxWidth: 800,
                    title: "上报消防检查信息"
                }));

                var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
                        '</form>';

                $("#jqueryToolbar").append($(reportForm));
                $("#_report4eventForm").append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));

                $("#_reportEventJson").val(JSON.stringify(event));
                $("#_report4eventForm").attr('action', url);

                $("#_report4eventForm").submit();

                $("#_report4eventForm").remove();

                <#--var event = JSON.stringify(event);-->
                <#--var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml?eventJson='+encodeURIComponent(event);-->
                <#--var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml?eventJson='+encodeURIComponent(JSON.stringify(event));-->
//                openJqueryWindowByParams($.extend({}, opt, {
//                    maxWidth: 800,
//                    title: "上报消防检查信息",
//                    targetUrl: url
//                }));
            }
        }
    }

    //上报回调函数
    function reportCallback(date){
        if(date.instanceId != null && date.instanceId != ""){
            $.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/fireControlCheck/report.jhtml',
                data: 'birId='+reportBirId+'&status=3',
                success: function(data){
                    closeMaxJqueryWindow();
                    $.messager.alert('提示','上报成功','info');
                    $("#list").datagrid('load');
                    $('#list').datagrid('unselectAll');
                },
                error:function(data){
                    $.messager.alert('错误','连接超时！','error');
                }
            });
        }else{
            $.messager.alert('提示','上报失败，请联系系统管理员！','error');
        }
    }

	
	function del(){
		var birId = "";
		$("input[name='birId']:checked").each(function() {
			birId = $(this).val();
		});
		if(birId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的消防检查信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/fireControlCheck/delFireCheck.jhtml',
						data: 'birId='+birId,
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.success) {
			  					reloadDataForSubPage(data.tipMsg);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '删除操作失败！', 'error');
			  					}
			  				}
						},
						error:function(data){
							modleclose();
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	function detail(birId) {
		var url = '${rc.getContextPath()}/zhsq/fireControlCheck/toDetail.jhtml?birId='+birId;
		
		opt.title = "查看消防检查";
	   	opt.targetUrl = url;
	   
	  	openJqueryWindowByParams(opt);
	}
	
	function authority(isDel) {
		var showBtn = ["AddBtn", "EditBtn", "ShangBaoBtn", "DelBtn"];
		
		switch(isDel) {
			case '1': {
			}
			case '3': {
				showBtn.splice(1, 3); break;
			}
			case '2': {
				break;
			}
		}
		
		$("#toolFrDiv > a").hide();
		
		for(var index in showBtn) {
			$("#toolFrDiv ." + showBtn[index]).show();
		}
	}
	
	function searchData() {
		var searchArray = new Array();
		
		$('.queryParam').each(function() {
			var queryObj = $(this),
				value = queryObj.val(),
				name = queryObj.attr("name");
			
			if(value) {
				searchArray[name] = value;
			}
			
		});
		
		doSearch(searchArray);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$('#inspectionForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>