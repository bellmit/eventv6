<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<script type="text/javascript" src="${uiDomain}/web-assets/extend/js/excelExportBaseOnPage.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/orgtreeSet.js"></script>
<style type="text/css">
	.width65px{width:105px;}
	.selectWidth{width: 159px;}
	.w150{width:130px;}
	.keyBlank{color:gray;}
	/*图标选中凹陷效果只有在ie9及其以上才有效果*/
	.icon_select{width:100px;background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	.icon_unselect{width:100px;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
</style>
<div id="jqueryToolbar">

	<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value="${infoOrgCode!''}"/>
	<form id="eventCaseQueryForm">
		<input id="listType" name="listType" type="text" class="hide queryParam" value="${listType!''}"/>
		
		<div class="ConSearch">
	        
	        <div class="fl" id="topSearchUl">
	        	<ul>
	            	<li class="eventCreateTimeLi">上报时间：</li>
	                <li class="eventCreateTimeLi">
	                	<input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
	                	<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
	                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:195px;" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
	                </li>
	                <li>关键字：</li>
                    <li><input name="keyWord" style="width:165px" type="text" class="inp1 queryParam" id="keyWord" placeholder="信息详情/标题/发生详址" value="<#if keyWord??>${keyWord}</#if>" /></li>
	        		<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div id="_advanceSearchDiv" class="AdvanceSearch DropDownList hide" style="width:320px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
                                                <td>
                                                    <label class="LabName width65px"><span>民生信息类型：</span></label>
                                                    <input type="text" id="infoType" name="infoType" class="hide queryParam" value="${infoType!}"/>
                                                    <input type="text" id="infoTypeName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>民生动态类型：</span></label>
                                                    <input type="text" id="infoTrendsType" name="infoTrendsType" class="hide queryParam" value="${infoTrendsType!}"/>
                                                    <input type="text" id="infoTrendsTypeName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>紧急程度：</span></label>
                                                    <input type="text" id="urgenceDegree" name="urgenceDegree" class="hide queryParam" value="${urgenceDegree!}"/>
                                                    <input type="text" id="urgenceDegreeName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>信息状态：</span></label>
                                                    <input type="text" id="status" name="status" class="hide queryParam" value="${status!}"/>
                                                    <input type="text" id="statusName" class="inp1 selectWidth" />
                                                </td>
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
		<div class="ToolBar" id="toolbarDiv">
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
	        <div id="toolFrDIV" class="tool fr"><@actionCheck></@actionCheck>
	          <!--<a href="#" id="delete" class="NorToolBtn DelBtn" onclick="del();">删除</a>
              <a href="#" id="edit" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
              <a href="#" id="add" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
	        </div>
	    </div>
	</form>
</div>

<script type="text/javascript">

	var startInfoOrgCode = '${infoOrgCode!}';
	var orgtreeset;
	var createTimeDateRender;
	var treStyle="${treStyle!'top'}";
	var listType="${listType!''}";
	$(function(){
		
		orgtreeset = new OrgTreeSet({
			treStyle:"${treStyle!'top'}",
			layoutId:'layoutArea',
			topSearchUl:'topSearchUl',
			startGridName:'${gridName}',
			dataDomian:'${COMPONENTS_URL}',
			defaultOrgCode:startInfoOrgCode,
			startgridId : '${startGridId}',
			onClickCB:function(rec){
				$('#infoOrgCode').val(rec.attributes.orgCode);
				if('left'=='${treStyle}'){
					searchData();
				}
			},
			resetCB:function(){
				if('left'!='${treStyle}'){
			    	orgCode = "${infoOrgCode}";
				}
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
		
		
		AnoleApi.initTreeComboBox("infoTypeName", "infoType", "A001135001", null, ['${infoType!}'], {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
		
		AnoleApi.initTreeComboBox("infoTrendsTypeName", "infoTrendsType", "A001135002", null, ['${infoTrendsType!}'], {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
		
		AnoleApi.initTreeComboBox("urgenceDegreeName", "urgenceDegree", "A001135003", null, ['${urgenceDegree!}'], {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
		
		AnoleApi.initTreeComboBox("statusName", "status", "A001135004", null, ['${status!}'], {
        	RenderType : "01",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        
	});
	

	function resetCondition() {//重置
		$('#eventCaseQueryForm')[0].reset();
		
		var queryParams=queryData();
		$("#list").datagrid('options').queryParams = queryParams;
		
		<#if 'left'!=treStyle>
			orgtreeset.reset();
			$("#infoOrgCode").val(startInfoOrgCode);
		</#if>
		
		<#if 'todo'==listType>
			$("#infoOrgCode").val('');
		</#if>

		searchData();
	}
	
	function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#eventCaseQueryForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		searchArray["infoOrgCode"]=$("#infoOrgCode").val();
		
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
		var keyWord = $(obj).val();
		
		if(keyWord == ''){
			$(obj).addClass('keyBlank');
			$(obj).val($(obj).attr("defaultValue"));
		}
	}
	
	//按钮控制
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = [];
			
			if(selectedRow.status=='99'){//表示是草稿状态的事件
			    showBtn.push("add","edit","delete");
			}else {//非草稿事件不能删除，编辑
			    showBtn.push("add")
			}
			
			$("#toolFrDIV > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDIV #" + showBtn[index]).show();
			}
		}
		
	}
	
	
	
	/*****************************************业务方法**********************************************/
	function addCallBack(infoId,instanceId){
		closeMaxJqueryWindow();
		searchData(true);
		var url = "${rc.getContextPath()}/zhsq/peopleLivelihood/toWorkflowDetailPage.jhtml?infoId="+infoId+"&instanceId="+instanceId+"&listType=2";//跳转待办列表
		showMaxJqueryWindow("民生信息详情", url, 900, document.body.clientHeight>530?530:undefined);
	}
	
	function submitCallBack(){
		closeMaxJqueryWindow();
		searchData(false);
	}
	
	function showDetail(infoId,instanceId){
		var url = "";
		if(instanceId&&instanceId!="undefined"){
			url = "${rc.getContextPath()}/zhsq/peopleLivelihood/toWorkflowDetailPage.jhtml?infoId="+infoId+"&instanceId="+instanceId+"&listType="+$('#listType').val();
		}else{
			url = "${rc.getContextPath()}/zhsq/peopleLivelihood/toDetailPage.jhtml?infoId="+infoId;
		}
        showMaxJqueryWindow("民生信息详情", url, 900, document.body.clientHeight>530?530:undefined);
	}
	
	//新增
	function add(){
		var url = "${rc.getContextPath()}/zhsq/peopleLivelihood/toAddPage.jhtml";
        showMaxJqueryWindow("民生信息新增", url, 900, document.body.clientHeight>510?510:undefined);
	}
	
	//编辑
	function edit(){
		var infoId="";
		var status="";
		$("input[name='infoId']:checked").each(function() {
			infoId = $(this).val();
		});
		
		$("input[name='status']:checked").each(function() {
			status = $(this).val();
		});
		
		if(infoId == "") {
			$.messager.alert('提示','请选中要编辑的记录再执行此操作!','info');
		}else if(status!='99'){
			$.messager.alert('提示','只有草稿状态的事件才能进行编辑操作!','info');
		}else {
            var url = "${rc.getContextPath()}/zhsq/peopleLivelihood/toAddPage.jhtml?infoId="+infoId;
            showMaxJqueryWindow("民生信息编辑", url, 900, document.body.clientHeight>510?510:undefined);
		}
	
	}
	
	
	
	//删除
	function del(){
	
		var infoId="";
		var status="";
		$("input[name='infoId']:checked").each(function() {
			infoId = $(this).val();
		});
		
		$("input[name='status']:checked").each(function() {
			status = $(this).val();
		});
		
		if(infoId == "") {
			$.messager.alert('提示','请选中要删除的民生信息再执行此操作!','info');
		}else if(status!='99'){
			$.messager.alert('提示','只有草稿状态的事件才能进行删除操作!','info');
		} else {
            $.messager.confirm('提示', '是否要删除该民生信息？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/peopleLivelihood/deleteInfo.json',
						data: {'infoId': infoId},
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.result==true){
								if(data.msg) {
			  						$.messager.alert('提示', data.msg, 'info');
			  					} else {
			  						$.messager.alert('提示', '删除成功！', 'info');
			  					}
			  					searchData();
			  				} else {
			  					if(data.msg) {
			  						$.messager.alert('提示', data.msg, 'info');
			  					} else {
			  						$.messager.alert('提示', '删除失败！', 'info');
			  					}
			  				}
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
							modleclose();
						}
					});
				}
			});
		}
		
	}
	
	
	
</script>

