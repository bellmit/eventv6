<#include "/component/ComboBox.ftl" />
<style type="text/css">
    .width65px{width:100px;}
    .w150{width:150px;}
    .keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
    <form id="eventSBREClueQueryForm">
    
        <input type="hidden" id="exportName" name="exportName" class="queryParam" value="处置统计表" />
        <input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'0'}" />
        <input type="hidden" id="typeVal" name="typeVal" class="queryParam" value="${typeVal!''}" />

        <div class="ConSearch">
            <div class="fl">
                <ul>
                    <li><#if listType?? && listType==1>发生区域<#else>所属区域</#if>：</li>
                    <li>
                        <input id="gridId" type="text" class="hide"/>
                        <input id="infoOrgCode" name="<#if listType?? && listType==1>infoOrgCode<#else>regionCode</#if>" type="text" class="hide queryParam" value="${infoOrgCode!''}"/>
                        <input id="gridName" type="text" class="inp1 InpDisable w150" value="${gridName!''}"/>
                    </li>
                    <li>标题内容：</li>
                    <li>
                        <input id="clueTitle" name="clueTitle" type="text" class="inp1 queryParam InpDisable w150" onkeydown="_onkeydown();" />
                    </li>
                    <li>线索来源：</li>
                    <li>
                        <input id="clueSource" name="clueSource" type="text" value="" class="queryParam hide"/>
                        <input id="clueSourceName" type="text" class="inp1 InpDisable w150" />
                    </li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:388px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        	<tr>
                                                <td>
                                                    <label class="LabName width65px"><span>涉及人员：</span></label>
                                                    <input name="involvedPeopleName" type="text" class="inp1 InpDisable w150 queryParam" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>登记时间：</span></label>
                                                    <input class="inp1 Wdate fl queryParam" type="text" name="createdStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" name="createdEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>线索重要程度：</span></label>
                                                    <input id="importantDegree" name="importantDegree" type="text" class="queryParam hide"/>
                                                    <input id="importantDegreeName" type="text" class="inp1 InpDisable w150" />
                                                </td>
                                            </tr>
                                            <#if listType?? && listType != 1>
                                                <tr>
                                                    <td>
                                                        <label class="LabName width65px"><span>线索状态：</span></label>
                                                        <input id="clueStatus" name="clueStatus" type="text" class="queryParam hide"/>
                                                        <input id="clueStatusName" type="text" class="inp1 InpDisable w150" />
                                                    </td>
                                                </tr>
                                            </#if>

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
    
        <div class="ToolBar" id="toolBarDiv">
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
            
            <div id="actionDiv" class="tool fr">
            	<#if isCopyClue?? && isCopyClue>
            	<a id="copy" href="javascript:void(0)" onclick="copy()" class="NorToolBtn AddBtn">复制</a>
            	<#else>
            	<@actionCheck></@actionCheck>
            	</#if>
            </div>
            <div id="iconSelectDiv" class="fl" style="width: 500px;">
	        	<a href="###" class="icon_select" onclick="iconSelect(this);"><i class="toolbar-icon-all"></i>全部线索</a>
	        	<#if listType?? && listType != 1>
	        	<a href="###" onclick="iconSelect(this, 4);"><i class="ToolBarOverDue"></i>已过期</a>
	        	<a href="###" onclick="iconSelect(this, 5);"><i class="ToolBarDue"></i>将到期</a>
	        	</#if>
	        	<a href="###" onclick="iconSelect(this, 1);"><i class="toolbar-icon-assign"></i>上级转办</a>
	        	<a href="###" onclick="iconSelect(this, 2);"><i class="toolbar-icon-key"></i>重要线索</a>
	        	<a href="###" onclick="iconSelect(this, 3);"><i class="toolbar-icon-ordinary"></i>普通线索</a>
	        </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function() {
        //网格树
        AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items){
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
        //线索来源
        AnoleApi.initTreeComboBox("clueSourceName", "clueSource", "B591001", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        //线索紧急程度
        AnoleApi.initTreeComboBox("importantDegreeName", "importantDegree", "B591002", null, null, {
            ChooseType : "1",
            ShowOptions:{
                EnableToolbar : true
            }
        });

        <#if listType?? && listType != 1>
        //线索状态
        AnoleApi.initTreeComboBox("clueStatusName", "clueStatus", "B591005", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        </#if>
        
        <#if extraParam??>
			<#list extraParam?keys as mapKey>
				var inputObj = $('#eventSBREClueQueryForm input[name="${mapKey}"]');
				if(inputObj.length) {
					inputObj.val('${extraParam[mapKey]}');
				} else {
					$("#eventSBREClueQueryForm").prepend('<input type="hidden" class="queryParam" name="${mapKey}" value="${extraParam[mapKey]}" />');
				}
			</#list>
		</#if>
    });

    function add() {
        var url = "${rc.getContextPath()}/zhsq/eventSBREClue/toAdd.jhtml";

        openJqueryWindowByParams({
            title			: "新增线索信息",
            targetUrl		: url,
            padding_top		: 0,
            padding_bottom	: 0,
            padding_left	: 0,
            padding_right	: 0
        });
    }

    function edit() {
        var clueId = "";

        $("input[name='clueId']:checked").each(function() {
            clueId = $(this).val();
        });

        if(clueId == "") {
            $.messager.alert('警告','请选中要编辑的线索再执行此操作!','warning');
        } else {
            var url = "${rc.getContextPath()}/zhsq/eventSBREClue/toAdd.jhtml?clueId=" + clueId;

            openJqueryWindowByParams({
                title			: "编辑线索信息",
                targetUrl		: url,
                padding_top		: 0,
                padding_bottom	: 0,
                padding_left	: 0,
                padding_right	: 0
            });
        }
    }

    function del() {
        var clueId = "";

        $("input[name='clueId']:checked").each(function() {
            clueId = $(this).val();
        });

        if(clueId == "") {
            $.messager.alert('警告','请选中要删除的线索再执行此操作!','warning');
        } else {
            $.messager.confirm('提示', '您确定删除选中的线索吗？', function(r) {
                if(r){
                    modleopen();

                    $.ajax({
                        type: "POST",
                        url: '${rc.getContextPath()}/zhsq/eventSBREClue/delClue.jhtml',
                        data: 'clueId='+clueId,
                        dataType:"json",
                        success: function(data) {
                            if (data.success == true) {
                                modleclose();
                                reloadDataForSubPage(data.tipMsg, true);
                            } else {
                                $.messager.alert('错误',data.tipMsg,'error');
                                modleclose();
                            }
                        },
                        error:function(data){
                            $.messager.alert('错误','删除线索连接超时！','error');
                            modleclose();
                        }
                    });
                }
            });
        }
    }

    function detail(clueId, listType, isEncrypt) {
        if(clueId) {
        	//只有加密了的线索才需要进行查看权限验证
        	if(isEncrypt && isEncrypt == '1') {
	        	//加密信息查看权限验证
	        	$.ajax({
	        		type: "POST",
	        		url: '${rc.getContextPath()}/zhsq/eventSBREClue/checkAuthority.jhtml',
	        		data: {'clueId' : clueId},
	        		dataType:"json",
	        		success: function(data) {
	        			modleclose();
	        			
	        			if(data.result == true) {
	        				showDetail(clueId, listType);
	        			} else {
	        				$.messager.alert('错误', data.msgWrong, 'error');
	        			}
	        		},
	        		error:function(data){
	        			 $.messager.alert('错误','加密查看权限获取失败！','error');
	        			 modleclose();
	        		}
	        	});
        	} else {
        		showDetail(clueId, listType);
        	}
        } else {
            $.messager.alert('警告','请选中查看的线索再执行此操作!','warning');
        }
    }
    
    function showDetail(clueId, listType) {
    	if(clueId) {
    		listType = listType || $("#listType").val();
    		
    		var url = "${rc.getContextPath()}/zhsq/eventSBREClue/toDetail.jhtml?clueId=" + clueId + "&listType=" + listType;
    		
    		openJqueryWindowByParams({
    			title			: "查看线索信息",
    			targetUrl		: url,
    			padding_top		: 0,
    			padding_bottom	: 0,
    			padding_left	: 0,
    			padding_right	: 0
    		});
    	} else {
    		$.messager.alert('警告','请选中查看的线索再执行此操作!','warning');
    	}
    	
    }
    
    function copy() {
    	var clueId = "";

        $("input[name='clueId']:checked").each(function() {
            clueId = $(this).val();
        });

        if(clueId == "") {
            $.messager.alert('警告', '请选中要复制的线索信息!', 'warning');
        } else if(parent && typeof parent.copyClueInfo === 'function' ) {
        	parent.copyClueInfo(clueId);
        }
    }
    
    function iconSelect(obj, iconType, isCurrent) {//快速查询相应事件
    	var queryArray = queryData();
    	
		$("#iconSelectDiv > a").removeClass("icon_select");
		$(obj).addClass("icon_select");
				
		switch(iconType) {
			case 1: {//上级转办
				queryArray['clueSource'] = '02';
				break;
			}
			case 2: {//重要线索
				queryArray['importantDegree'] = '02';
				break;
			}
			case 3: {//普通线索
				queryArray['importantDegree'] = '01';
				break;
			}
			case 4: {//已过期
				queryArray['handleDateFlag'] = '3';
				break;
			}
			case 5: {//将到期
				queryArray['handleDateFlag'] = '2';
				break;
			}
		}
		
		doSearch(queryArray, isCurrent);
	}

    function resetCondition() {//重置
        $('#eventSBREClueQueryForm')[0].reset();
        
        if($("#iconSelectDiv > a").length) {
        	iconSelect($("#iconSelectDiv > a").eq(0));
        } else {
        	doSearch(queryData(), isCurrent);
        }
    }

    function searchData(isCurrent){//查询
    	var selectedIcon = $("#iconSelectDiv > a.icon_select");
    	
    	if(selectedIcon.length > 0) {
    		selectedIcon.eq(0).click();
    	} else {
        	doSearch(queryData(), isCurrent);
        }
    }

    function queryData() {
        var searchArray = new Array();

        $("#eventSBREClueQueryForm .queryParam").each(function() {
            var key = $(this).attr("name"),val = $(this).val() ;

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
    function exportClue(){
	    $.messager.confirm('提示', '导出可能需要等待一会，你确定导出吗？', function(r) {
	                if(r){
	   					 window.location.href="${rc.getContextPath()}/zhsq/eventSBREClue/toExport.jhtml?"+$("#eventSBREClueQueryForm").serialize();
	   				}
	    });

    }

    function _onkeydown(){
        var keyCode = event.keyCode;
        if(keyCode == 13){
            searchData();
        }
    }
</script>