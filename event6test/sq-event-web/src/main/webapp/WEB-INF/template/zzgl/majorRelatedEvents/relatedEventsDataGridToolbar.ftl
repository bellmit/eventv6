<#include "/component/ComboBox.ftl" />
<#include "/component/AnoleDate.ftl" />
<style type="text/css">
.width65px{width:90px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <form id="searchForm">
        <input id="bizType" name="bizType" type="text" style="display:none;" value="<#if bizType??>${bizType}</#if>"/>
        <div class="fl">
        	<ul>
                <li>所属网格：</li>
                <li>
                	<input id="gridId" name="gridId" type="text" style="display:none;" value="<#if gridId??>${gridId?c}</#if>"/>
                	<input id="gridName" type="text" class="inp1 InpDisable" style="width:180px;"/>
                </li>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="案(事)件名称/案件情况" style="font-size:12px;color:gray;width:250px;" onfocus="if(this.value=='案(事)件名称/案件情况'){this.value='';}$(this).attr('style','width:250px;');" onblur="if(this.value==''){$(this).attr('style','font-size:12px;color:gray;width:250px;');this.value='案(事)件名称/案件情况';}" onkeydown="_onkeydown();"/></li>
                <li style="position:relative;">
            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
            		<div class="AdvanceSearch DropDownList hide" style="width:350px; top: 42px; left: -130px;">
                        <div class="LeftShadow">
                            <div class="RightShadow">
                                <div class="list NorForm">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td><label class="LabName width65px"><span>案件编号：</span></label>
                                        	<input class="inp1" type="text" id="reNo" name="reNo" style="width:150px;"></input>
                                        </td>
                                      </tr>
                                      <tr>
                                      	<td>
                                      		<label class="LabName width65px"><span>案件类型：</span></label>
                                      		<input type="text" id="eventType" name="eventType" style="display:none;"/>
            								<input type="text" class="inp1" style="width:150px;" id="eventTypeName" />
            							</td>
                                      </tr>
                                      <tr>
                                      	<td>
                                      		<label class="LabName width65px"><span>案件分级：</span></label>
                                      		<input type="text" id="eventLevel" name="eventLevel" style="display:none;"/>
            								<input type="text" class="inp1" style="width:150px;" id="eventLevelName" />
            							</td>
                                      </tr>
                                      <tr>
                                      	<td>
                                      		<label class="LabName width65px"><span>发生日期：</span></label>
                                      		<input id="occuDateStr" type="text" class="inp1" style="width:200px;" value="<#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.occuDateStartStr!''} ~ </#if><#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.occuDateEndStr!''}</#if>"/>
						        			<input id="occuDateStartStr" name="occuDateStartStr" type="text" style="display:none;" value="<#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.occuDateStartStr!''}</#if>"/>
						        			<input id="occuDateEndStr" name="occuDateEndStr" type="text" style="display:none;" value="<#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.occuDateEndStr!''}</#if>"/>
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
        </form>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBarDIV">
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
		<!--toolFrDIV代码格式不可换行，否则会影响布局-->
		<div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck></div>
    </div>
	
</div>

<script type="text/javascript">
	var queryValue = "";
	var lotUrl = "${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/listCareRoads.jhtml?lotName=";
	
	$(function(){
		if(document.getElementById('toolFrDIV').innerHTML==''){
			$("div").remove("#ToolBarDIV");
		}
		if (gridId > 0) {
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", null, {
				Async : {
					enable : true,
					autoParam : [ "id=gridId" ],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : gridId
					}
				}
			});
		} else {
			AnoleApi.initGridZtreeComboBox("gridName", "gridId");
		}
		
        AnoleApi.initListComboBox("eventTypeName", "eventType", "<#if eventTypeDC??>${eventTypeDC}</#if>", null, null, {
        	ShowOptions : {
        		EnableToolbar : true
        	}
        });
        AnoleApi.initListComboBox("eventLevelName", "eventLevel", "<#if eventTypeDC??>${eventLevelDC}</#if>", null, null, {
        	ShowOptions : {
        		EnableToolbar : true
        	}
        });
        $("#occuDateStr").anoleDateRender({
			BackfillType : "1",
			BackEvents : {
				OnSelected : function(api) {
					$("#occuDateStartStr").val(api.getStartDate("yyyy-MM-dd"));
					$("#occuDateEndStr").val(api.getEndDate("yyyy-MM-dd"));
				},
				OnCleared : function() {
					$("#occuDateStartStr").val("");
					$("#occuDateEndStr").val("");
				}
			}
		});

	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/toAddRelatedEvents.jhtml';
		var opt = $.extend({}, _win_opt, {
	    	title: "新增涉事案件",
	    	targetUrl: url,
	    	width: 650,
	    	maxHeight: 406
	    });
		openJqueryWindowByParams(opt);
	}
	
	function edit() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/toAddRelatedEvents.jhtml?reId='+idStr;
		var opt = $.extend({}, _win_opt, {
	    	title: "编辑涉事案件",
	    	targetUrl: url,
	    	width: 650,
	    	maxHeight: 406
	    });
		openJqueryWindowByParams(opt);
	}
	
	function del() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/delRelatedEvents.jhtml',
					data: 'idStr='+idStr,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '您成功删除'+data.result+'条记录！', 'info');
						$("#list").datagrid('load');
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	
	function getQueryData() {
		var a = getFormJson($("#searchForm"));
		if (a["keyWord"] == "案(事)件名称/案件情况") {
			a["keyWord"] = "";
		}
		return a;
	}
	
	function getFormJson(form) {
		var o = {};
		var a = $(form).serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	}
	
	function resetCondition() {
		$("#searchForm")[0].reset();
		$("#keyWord").attr('style','font-size:12px;color:gray;width:250px;');
		searchData(1);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData(1);
		}
	}
	
</script>