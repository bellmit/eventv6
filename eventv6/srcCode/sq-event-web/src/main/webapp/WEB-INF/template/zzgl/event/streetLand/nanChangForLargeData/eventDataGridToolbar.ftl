<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/orgtreeSet.js"></script>
<style type="text/css">
    .width65px{width:105px;}
    .selectWidth{width: 159px;}
    .dateRenderWidth{width: 195px;}
    /*图标选中凹陷效果只有在ie9及其以上才有效果*/
    .icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	<@block name="extraStyle"></@block>
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<div id="jqueryToolbar">
    <form id="eventQueryForm">

        <div class="ConSearch">
            <input type="hidden" id="handleDateFlag" name="handleDateFlag" value="${handleDateFlag!}" class="queryParam" />
            <input type="hidden" id="typesForList" name="typesForList" value="${typesForList!}" class="queryParam" />
            <input type="hidden" id="eventAttrTrigger" name="eventAttrTrigger" value="${eventAttrTrigger!}" class="queryParam" />
            <input type="hidden" id="isRemoveTypes" name="isRemoveTypes" value="<#if isRemoveTypes??>${isRemoveTypes?c}<#else>false</#if>" class="queryParam" />
            <input type="hidden" id="eventType" name="eventType" value ="${eventType!}" class="queryParam"/>
            <input type="hidden" id="listModel" value="${model!}" />

		<@block name="extraListParam"></@block>
		<@block name="generalParam">
            
            <div class="fl" id="topSearchUl">
                <ul>
                	<@block name="gridTreeSearch">
                		<input id="gridLevel" type="text" class="hide" value="${startGridLevel!}" />
                		<input id="infoOrgCode" name="eInfoOrgCode" type="text" class="hide queryParam" value="${eInfoOrgCodeForOut!''}"/>
                		<input id="gridId" name="gridId" type="text" class="hide queryParam" />
                	</@block>
                    <li class="eventCreateTimeLi">采集时间：</li>
	                <li class="eventCreateTimeLi">
	                	<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!}"></input>
	                	<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd" value="${createTimeEnd!}"></input>
	                	<input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:195px;" value="${createTimeStart!}<#if createTimeStart?? && createTimeEnd??> ~ </#if>${createTimeEnd!}"/>
	                </li>
                    <li>关键字：</li>
                    <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="<#if keyWord??>${keyWord}<#else>事件描述/标题/事发详址</#if>" style="<#if keyWord??><#else>color:gray;</#if> width:150px;" onfocus="if(this.value=='事件描述/标题/事发详址'){this.value='';}$(this).attr('style','width:150px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:150px;');this.value='事件描述/标题/事发详址';}" onkeydown="_onkeydown();" /></li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:400px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>排序字段：</span></label>
                                                    <input type="text" id="orderByField" name="orderByField" class="queryParam hide" value="${defaultOrderBy!}"/>
                                                    <input type="text" id="orderByFieldInput" class="hide" />
                                                    <input class="inp1 selectWidth" type="text" id="orderByFieldName" ></input>
                                                </td>
                                            </tr>
                                            <tr id="orderByFieldRadioTr" class="hide">
                                                <td>
                                                    <label class="LabName width65px"><span>排序方式：</span></label>
                                                    <div class="Check_Radio" style="margin-right: 40px;"><input type="radio" name="orderByRadio" id="orderByRadioAsc" value="ASC" checked onclick="radionCheck('ASC')"></input><label for="orderByRadioAsc" style="cursor: pointer;">升序</label></div>
                                                    <div class="Check_Radio"><input type="radio" name="orderByRadio" id="orderByRadioDesc" value="DESC" onclick="radionCheck('DESC')"></input><label for="orderByRadioDesc" style="cursor: pointer;">降序</label></div>
                                                </td>
                                            </tr>
                                            <tr class="query4todo">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>是否接收：</span></label>
	                                    			<input id="taskReceivedStaus" name="taskReceivedStaus" type="text" class="queryParam hide"/>
	                                    			<input id="taskReceivedStausName" type="text" class="inp1 selectWidth" />
	                                    		</td>
	                                    	</tr>
                                            <tr id="eventTypeTr">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>事件分类：</span></label>
	                                    			<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                                    			<input id="typeName" name="typeName" type="text" class="inp1 selectWidth"/>
	                                    		</td>
	                                    	</tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 queryParam" type="text" id="code" name="code" style="width:135px;"></input></td>
                                            </tr>
										<#if eventType=="all" || eventType=="attention">
                                            <tr>
                                                <td><label class="LabName width65px"><span>事件状态：</span></label>
                                                     <select name="statuss" id="statuss" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
														<#if statusDC??>
                                                            <option value="">不限</option>
															<#list statusDC as l>
																<#if l.dictGeneralCode != "06" && l.dictGeneralCode != "99" && l.dictGeneralCode != "05">
                                                                    <option value="${l.dictGeneralCode}">${l.dictName}</option>
																</#if>
															</#list>
														</#if>
                                                    </select>
                                                </td>
                                            </tr>
										</#if>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>事发时间：</span></label>
                                                    <input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
                                                    <input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
                                                    <input type="text" id="_happenTimeDateRender" class="inp1 InpDisable dateRenderWidth" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
                                                </td>
                                            </tr>
                                            <#if eventType??>
                                            	<#if eventType != 'draft'>
                                            		<tr>
                                            			<td>
                                            				<label class="LabName width65px"><span>办结期限：</span></label>
                                            				<input class="inp1 hide queryParam" type="text" id="handleDateDayStart" name="handleDateDayStart" value="${handleDateDayStart!}"></input>
                                            				<input class="inp1 hide queryParam" type="text" id="handleDateDayEnd" name="handleDateDayEnd" value="${handleDateDayEnd!}"></input>
                                            				<input type="text" id="_handleDateRender" class="inp1 InpDisable dateRenderWidth" value="${handleDateDayStart!}<#if handleDateDayStart?? && handleDateDayEnd??> ~ </#if>${handleDateDayEnd!}"/>
                                            			</td>
                                            		</tr>
                                            	</#if>
                                            	<#if eventType == "all">
                                            		<tr>
                                            			<td>
                                            				<label class="LabName width65px"><span>结案时间：</span></label>
                                            				<input class="inp1 hide queryParam" type="text" id="finTimeStart" name="finTimeStart" value=""></input>
                                            				<input class="inp1 hide queryParam" type="text" id="finTimeEnd" name="finTimeEnd" value=""></input>
                                            				<input type="text" id="_finTimeDateRender" class="inp1 InpDisable dateRenderWidth" value=""/>
                                            			</td>
                                            		</tr>
                                            	<#elseif eventType == 'history'>
                                            		<tr>
                                            			<td>
                                            				<label class="LabName width65px"><span>归档时间：</span></label>
                                            				<input class="inp1 hide queryParam" type="text" id="archiveTimeStart" name="archiveTimeStart" value=""></input>
                                            				<input class="inp1 hide queryParam" type="text" id="archiveTimeEnd" name="archiveTimeEnd" value=""></input>
                                            				<input type="text" id="_archiveTimeDateRender" class="inp1 InpDisable dateRenderWidth" value=""/>
                                            			</td>
                                            		</tr>
                                            	</#if>
                                            </#if>
                                            <tr>
                                                <td><label class="LabName width65px"><span>采集人：</span></label><input class="inp1 queryParam" type="text" id="contactUser" name="contactUser" style="width:135px;"></input></td>
                                            </tr>
                                            
                                        <#if eventType??&&(eventType=='all' || eventType=='history')>
                                        	<tr>
                                                <td>
                                                    <label class="LabName width65px"><span>采集人所属组织：</span></label>
                                                    <input id="creatorOrgCode" name="creatorOrgCode" type="text" class="queryParam hide"/>
                                                    <input id="userOrgId" type="text" class="hide"/>
                                                    <input id="userOrgName" type="text" class="inp1 selectWidth"/>
                                                </td>
                                            </tr>
                                        </#if>
                                        
                                        <@block name="extendCondition"></@block>
                                            
                                        <@block name="collectWaysCondition">
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>采集渠道：</span></label>
                                                    <input type="text" id="collectWays" name="collectWay" class="hide queryParam"/>
                                                    <input type="text" id="collectWaysName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                        </@block>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>信息来源：</span></label>
                                                    <input type="text" id="sources" name="source" class="hide queryParam"/>
                                                    <input type="text" id="sourcesName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                        <@block name="influenceDegreesCondition">
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>影响范围：</span></label>
                                                    <input type="text" id="influenceDegrees" name="influenceDegree" class="hide queryParam"/>
                                                    <input type="text" id="influenceDegreesName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                        </@block>
                                        <@block name="urgencyDegreesCondition">
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>紧急程度：</span></label>
                                                    <input type="text" id="urgencyDegrees" name="urgencyDegree" class="hide queryParam"/>
                                                    <input type="text" id="urgencyDegreesName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
                                        </@block>
                                            <tr <#if eventType=="history"><#else>style="display:none;"</#if>>
                                                <td>
                                                    <label class="LabName width65px"><span>评价等级：</span></label>
                                                    <input type="text" id="evaLevel" name="evaLevelList" class="queryParam hide" value="" />
                                                    <input type="text" id="evaLevelName" class="inp1 selectWidth" value="" />
                                                </td>
                                            </tr>
                                            <tr id="attrFlagTr" <#if eventType=="all" || eventType=="history"><#else>style="display:none;"</#if>>
                                                <td>
                                                    <label class="LabName width65px"><span>附件类型：</span></label>
                                                    <input type="text" id="attrFlags" name="attrFlag" class="hide queryParam"/>
                                                    <input type="text" id="attrFlagsName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
										<#if isShowPlatformSelect?? &&(isShowPlatformSelect=="true")>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>对接平台：</span></label>
                                                    <input type="text" id="bizPlatform" name="bizPlatform" class="hide queryParam"/>
                                                    <input type="text" id="bizPlatformName" name="bizPlatformName" class="inp1 selectWidth" />
                                                </td>
                                            </tr>
										</#if>
										<#if isShowOrgSelect?? &&(isShowOrgSelect=="true")>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>办理环节：</span></label>
                                                    <input type="text" id="curHandledTaskName" name="curHandledTaskName" class="hide queryParam"/>
                                                    <input type="text" id="curHandledTaskNameName" class="inp1 selectWidth" />
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
            </@block>    
            <div class="btns">
                <ul>
                    <li><a href="###" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
                    <li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
                </ul>
            </div>
            <div class="clear"></div>‍

        </div>
        <div class="h_10 clear"></div>
        <div class="ToolBar" id="ToolBar">
            <div class="blind"></div><!-- 文字提示 -->
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
            <@block name="changeEventList">
            <div class="tool fr" id="toolBarFrDiv">
            <#if model?? && model=='e'>
	           <a id="export" href="javascript:void(0)" onclick="doExportExcel()" class="NorToolBtn ExportBtn">确认导出</a>
	        	<#else>
		            <@actionCheck></@actionCheck>
	            	<#if false && model?? && eventType != "draft">
		                <a href="###" id="changeEventList" onclick="changeEventList()" style="margin-right:10px;">
							<#if model=='c'>
		                        <img src="${rc.getContextPath()}/images/ColumnView.png" />
							<#else>
		                        <img src="${rc.getContextPath()}/images/ListView.png" />
							</#if>
		                </a>
					</#if>
	        </#if> 
            
           
			</div>
			</@block>
        </div>
    </form>
    <@block name="excelForm">
	    <form id="excelForm" name="excelForm" action="toExport.jhtml" method="post">
			<input type="hidden" id="eventIdList" name="eventIdList" value="" />
			<input type="hidden" id="imageList" name="imageList" value="" />
			<input type="hidden" name="listType" value="9" />
			<input type="hidden" id="exportName" name="exportName" value="辖区事件导出" />
			<@block name="excelFormExtend">
			</@block>
		</form>
	</@block>
</div>

<script type="text/javascript">

    var startInfoOrgCode = '${infoOrgCode!}',
        taskReceivedStausComboBox = null,
        orderByFieldComboBox = null,
        createTimeDateRender = null,
        happenTimeDateRender = null,
        handleDateRender = null,
        finTimeDateRender = null,
        archiveTimeDateRender = null;
    
    var orgtreeset;//网格树

    //事件新增编辑详情窗口页面宽高可继承参数
    <@block name="openJqueryWindowByParams">
        var opt = {
            "height": $(window).height(),
            "width": $(window).width(),
            "maxHeight": $(window).height(),
            "maxWidth": $(window).width(),
            "isAutoHeight": false,
            "isAutoWidth": false
        };
    </@block>

    $(function(){
    
    	<@block name="gridTreeInit">
    	//初始化网格树
    	orgtreeset = new OrgTreeSet({
			treStyle:"${treStyle!'top'}",
			layoutId:'layoutArea',
			topSearchUl:'topSearchUl',
			startGridName:"${gridName!''}",
			dataDomian:'${COMPONENTS_URL}',
			defaultOrgCode:'${defaultInfoOrgCode}',
			onClickCB:function(rec){
				$('#infoOrgCode').val(rec.attributes.eOrgCode);
				$('#gridLevel').val(rec.attributes.gridLevel);
				$('#gridId').val(rec.id);
				
				if('left'=='${treStyle}'){
					conditionSearch();
				}
			},
			resetCB:function(){
				if('left'!='${treStyle}'){
			    	infoOrgCode = '${infoOrgCode}';
				}
			}
			<#if startGridId??>,startgridId : ${startGridId?c}</#if>
		});
		</@block>
    
    	<#if statuss??>
    		var statusArr = "${statuss}".split(',');
    		if($('#statuss').val()!=null) {
    			$('#statuss').combobox('setValues', statusArr);
    		}
    	</#if>
    	
        var typesDictCode = "${typesDictCode!}";
        if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
            AnoleApi.initTreeComboBox("typeName", "type", {
                "A001093199107" : [${typesDictCode!}]
            }, null, null, {//0 展示指定的字典；1 去除指定的字典；
                FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
                ChooseType : "1" ,
                EnabledSearch : true,
                ShowOptions: {
                    EnableToolbar : true
                }
            });
        } else {
        	if("${showFullTypeDict}"!=null&&"${showFullTypeDict}"!=""){
        	
	            AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, {
	                ChooseType : "1" ,
	                EnabledSearch : true,
	                ShowOptions: {
	                    EnableToolbar : true
	                }
	            });
        	}else{
        	
	            AnoleApi.initTreeComboBox("typeName", "type", "A001093199107", null, null, {
	                ChooseType : "1" ,
	                EnabledSearch : true,
	                ShowOptions: {
	                    EnableToolbar : true
	                }
	            });
        	}
        }

	<#if eventType?? && eventType=='history'>
        AnoleApi.initListComboBox("evaLevelName", "evaLevel", "D050001", null, null, {
            RenderType:'01',
            ShowOptions: {
                EnableToolbar: true
            }
        });
	</#if>
	<#if eventType??&&(eventType=='all' || eventType=='history')>
        AnoleApi.initFuncOrgZtreeComboBox("userOrgName", "userOrgId", function(orgId, items) {
            if (items && items.length > 0) {
                $("#creatorOrgCode").val(items[0].orgCode);
            }
        }, {
            OnCleared: function() {
                $("#creatorOrgCode").val('');
            },
            ShowOptions: {
                EnableToolbar : true
            }
        });
	</#if>

		<@block name="anoleApiInitData">
		
		if($('#_createTimeDateRender').length > 0) {
			createTimeDateRender = init4DateRender('_createTimeDateRender', 'createTimeStart', 'createTimeEnd');
		}
		if($('#_happenTimeDateRender').length > 0) {
			happenTimeDateRender = init4DateRender('_happenTimeDateRender', 'happenTimeStart', 'happenTimeEnd');
		}
		if($('#_handleDateRender').length > 0) {
			handleDateRender = init4DateRender('_handleDateRender', 'handleDateDayStart', 'handleDateDayEnd');
		}
		if($('#_finTimeDateRender').length > 0) {
			finTimeDateRender = init4DateRender('_finTimeDateRender', 'finTimeStart', 'finTimeEnd');
		}
		if($('#_archiveTimeDateRender').length > 0) {
			archiveTimeDateRender = init4DateRender('_archiveTimeDateRender', 'archiveTimeStart', 'archiveTimeEnd');
		}

        taskReceivedStausComboBox = AnoleApi.initListComboBox("taskReceivedStausName", "taskReceivedStaus", null, null, null, {
            DataSrc: [{"name":"已接收", "value":"1"},{"name":"未接收", "value":"0"}],
            ShowOptions:{
                EnableToolbar : true
            }
        });
        
        
        <@block name="collectWaysConditionInit">
        AnoleApi.initTreeComboBox("collectWaysName", "collectWays", "A001093096", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        </@block>

        AnoleApi.initTreeComboBox("sourcesName", "sources", "A001093222", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        
        <@block name="influenceDegreesConditionInit">
        AnoleApi.initTreeComboBox("influenceDegreesName", "influenceDegrees", "A001093094", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        </@block>
        
        <@block name="urgencyDegreesConditionInit">
        AnoleApi.initTreeComboBox("urgencyDegreesName", "urgencyDegrees", "A001093271", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        </@block>

        AnoleApi.initTreeComboBox("attrFlagsName", "attrFlags", "B063", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
        
        <@block name="extendConditionInit"></@block>

        orderByFieldComboBox = AnoleApi.initListComboBox("orderByFieldName", "orderByFieldInput", null, function(value) {
            $("#orderByFieldRadioTr").show();

            radionCheck();
        }, null, {
            DataSrc : [{"name":"采集时间", "value":"T1.CREATE_TIME"},{"name":"事发时间", "value":"T1.HAPPEN_TIME"}],
            IsTriggerDocument: false,
            ShowOptions:{
                EnableToolbar : true
            },
            OnCleared: function() {
                $("#orderByField").val("${defaultOrderBy!}");
                $("#orderByFieldRadioTr").hide();
            }
        });
		</@block>
	<#if eventType!="todo">
        $("#jqueryToolbar tr.query4todo").hide();
	</#if>

	<#if isShowPlatformSelect?? &&(isShowPlatformSelect=="true")>
        AnoleApi.initListComboBox("bizPlatformName", "bizPlatform", "B6027", null, null, {
            ChooseType : "1" ,
            ShowOptions: {
                EnableToolbar : true
            }
        });
	</#if>

	<#if isShowOrgSelect?? &&(isShowOrgSelect=="true")>
        AnoleApi.initListComboBox("curHandledTaskNameName", "curHandledTaskName", "B6028", null, null, {
            ChooseType : "1" ,
            ShowOptions: {
                EnableToolbar : true
            }
        });
	</#if>
    });

    function comboboxSelect(record, id, exceptValue){
        var exceptAttr = [];

        if(exceptValue==undefined || exceptValue==null){
            exceptValue = "";
            exceptAttr[0] = "";
        }else if(typeof exceptValue == 'string'){
            exceptAttr = exceptValue.split(',');
        }

        if(exceptValue.indexOf(record.value) != -1){
            $('#'+id).combobox("setValue", record.value);
        }else{
            for(var index = 0, len = exceptAttr.length; index < len; index++){
                $('#'+id).combobox("unselect", exceptAttr[index]);
            }
        }
    }

    function comboboxUnselect(record, id){
        var values = $('#'+id).combobox("getValues");
        if(values.length == 0){
            $('#'+id).combobox("setValue", "");
        }
    }

    function comboboxSetSelect(id, selectStr){//点击下拉列表对应图标
        if(id!=undefined && id!=null && id!="" && selectStr!=undefined && selectStr!=null && selectStr!=""){
            var comboboxItems = $('#'+id).combobox('getData');
            for(var index = 0, len = comboboxItems.length; index < len; index++){
                var itemValue = comboboxItems[index].value;
                if(itemValue!="" && selectStr.indexOf(itemValue)!=-1){
                    $('#'+id).combobox('select', itemValue);
                }
            }
        }
    }

    function radionCheck(orderType) {
        var orderField = $("#orderByFieldInput").val();

        if(isBlankStringTrim(orderType)) {
            orderType = $('#orderByFieldRadioTr input[name="orderByRadio"]:checked').eq(0).val();
        }

        $("#orderByField").val(orderField + " " + orderType);
    }
    
    //列表元素可见性、可编辑性设置
    function adjustListDisplayElement(option) {
    	if(isNotBlankParam(option) && typeof option === 'object') {
    		var exhibitParam = option.exhibitParam;
    		if(isNotBlankParam(exhibitParam) && typeof exhibitParam === 'object') {
    			
    			if(exhibitParam.isEditEventCreateTime == false) {
    				//如果后续日期范围组件可设置只读属性，此处再调整为只读，目前先隐藏处理
    				$('#eventQueryForm li.eventCreateTimeLi').hide();
    			}
    			
    			if(exhibitParam.isShowEventType == false) {
    				$('#eventTypeTr').hide();
    			}
    		}
    	}
    }
    
    function sendMsg(type,table) {
        var nameAndPhones = "";
        //判断是否显示了列表
        if($('#drugRecordContentDiv').length==0 || document.getElementById('drugRecordContentDiv').style.display != 'none'){
            var rows = $('#' + table).datagrid('getSelections');
            for ( var i = 0; i < rows.length; i++) {
                if (type == "TXT" || type == "M_VOX") {
                    if (rows[i].residentMobile != null && nameAndPhones.indexOf(rows[i].residentMobile)<0){
                        nameAndPhones += rows[i].name + "(" + rows[i].residentMobile + ");";
                        continue;
                    }
                }else{
                    if (rows[i].phone != null && nameAndPhones.indexOf(rows[i].phone)<0){
                        nameAndPhones += rows[i].name + "(" + rows[i].phone + ");";
                    }
                }
            }
            if("TXT"==type||"M_VOX"==type){
                $('#commonMsgForm').attr("commonMsgType","sms");
            }else{
                $('#commonMsgForm').attr("commonMsgType","vox");
            }
            $('#commonNameAndPhones').val(nameAndPhones);
            $('#commonMsgForm').submit();
        }
    }

    var defaultShowWindowHeight=560;
    function add() {
        var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/toAddEventByType.jhtml?typesForList=${typesForList!}&trigger=${trigger!}';
	<#if isRemoveTypes??>
        url += '&isRemoveTypes=${isRemoveTypes?c}';
        </#if>
        <@block name="addEventPageInfo">
            showMaxJqueryWindow("新增事件信息", url, fetchWinWidth(), defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
        </@block>
    }
    

    function edit() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选择一条编辑的数据再执行此操作!','warning');
            return;
        }
        var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/toAddEventByType.jhtml?eventId='+idStr+'&typesForList=${typesForList!}&trigger=${trigger!}';
        <#if isRemoveTypes??>
        	url += '&isRemoveTypes=${isRemoveTypes?c}';
        </#if>
        
        <@block name="eventEditUrlExtraParam"></@block>
	    <@block name="editEventPageInfo">
            showMaxJqueryWindow("编辑事件信息", url, fetchWinWidth(), defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
        </@block>
    }

    function print() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选中一条要打印的数据再执行此操作!','warning');
            return;
        }
        if(_bizPlatform == "001"){
            $.messager.alert('提示','旧平台事件暂不支持打印!','warning');
            return;
        }else{
            var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/print.jhtml?eventId='+idStr;
            winOpenFullScreen(url, "打印事件详情");
        }
    }

    function del() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','一次只能删除一条事件,不允许批量删除!','warning');
            return;
        }
        $.messager.confirm('提示', '您确定删除选中的事件吗？', function(r){
            if(_bizPlatform == "001"){
                $.ajax({
                    type: "POST",
                    url: '${rc.getContextPath()}/zhsq/event/streetLandEventController/delOldEvent.jhtml',
                    data: 'oldidStr='+idStr,
                    dataType:"json",
                    success: function(data){
                        modleclose();
                        $.messager.alert('提示', '事件删除成功！', 'info');
					<#if model?? && model == 'l'>
                        loadMessage((pageNum?pageNum:1),$('#pageSize').val());
					<#else>
                        flashData(true);
					</#if>

                    },
                    error:function(data){
                        $.messager.alert('错误','连接超时！','error');
                    }
                });
            }else{
                if (r){
                    modleopen();
                    $.ajax({
                        type: "POST",
                        url: '${rc.getContextPath()}/zhsq/event/streetLandEventController/delEvent.jhtml',
                        data: 'idStr='+idStr,
                        dataType:"json",
                        success: function(data){
                            modleclose();
                            $.messager.alert('提示', '事件删除成功！', 'info');
						<#if model?? && model == 'l'>
                            loadMessage((pageNum?pageNum:1),$('#pageSize').val());
						<#else>
                            flashData(true);
						</#if>
                        },
                        error:function(data){
                            $.messager.alert('错误','事件删除连接超时！','error');
                        }
                    });
                }
            }
        });
    }
    
    function invalid(operateName, invalidType) {
		operateName = operateName || '删除';
		invalidType = invalidType || 1;
		
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要' + operateName + '的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定' + operateName + '选中的事件吗？', function(r){
			if(r) {
				<@block name="invalidFunctionOpenMethod">
				var reportWinId = openJqueryWindowByParams({
							  		maxWidth: 480,
							  		maxHeight: 280,
							  		title: operateName + "事件"
								  });
				</@block>
				var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/toInvalidEvent.jhtml',
					reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post"></form>';
				
				$("#jqueryToolbar").append($(reportForm));
				$("#_report4eventForm").append($('<input type="hidden" id="_reportEventId" name="eventId" value="" />'));
				$("#_report4eventForm").append($('<input type="hidden" id="_defaultTipMsg" name="defaultTipMsg" value="" />'));
				$("#_report4eventForm").append($('<input type="hidden" id="_invalidType" name="invalidType" value="" />'));
				
				<@block name="invalidApplicationExtraAttribute"></@block>
				
				$("#_reportEventId").val(idStr);
				$("#_defaultTipMsg").val('事件' + operateName + '成功！');
				$("#_invalidType").val(invalidType);
				$("#_report4eventForm").attr('action', url);
				
				$("#_report4eventForm").submit();
				
				$("#_report4eventForm").remove();
			}
		});
	}
	
    function restart() {
        if(idStr) {
            var status = null;

            if($('#list').length) {
                var item = $('#list').datagrid('getSelected');
                if(item.length == 0) {
                    $.messager.alert('警告','请选择需要重启操作的事件!','warning');
                } else {
                    status = item.status;
                }
            } else if($("#"+ idStr +"_status").length) {
                status = $("#"+ idStr +"_status").val();
            }

            if(status && status != '04') {
                $.messager.alert('警告', '该事件尚未归档!', 'warning');
            } else {
                var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/toRestartEvent.jhtml?eventId='+idStr+'&typesForList=${typesForList!}&trigger=${trigger!}';
			<#if isRemoveTypes??>
                url += '&isRemoveTypes=${isRemoveTypes?c}';
			</#if>
                showMaxJqueryWindow("重启事件信息", url, fetchWinWidth(), defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
            }
        } else {
            $.messager.alert('警告','请选择需要重启操作的事件!','warning');
        }
    }

    function reportToDs(){
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选中要上报的数据再执行此操作!','warning');
            return;
        }else{
            $.messager.confirm('提示', '您确定上报选中的事件吗？', function(r){
                if(r){
                    modleopen();
                    $.ajax({
                        type: "POST",
                        url : '${rc.getContextPath()}/zhsq/event/eventDisposalDocking/toAddEventInter.jhtml?destPlatform=004&eventId='+idStr,
                        data: '',
                        dataType:"json",
                        success: function(data){
                            var result = data.result;
                            if(result > 0){
                                $.messager.alert('提示','事件上报成功！','info');
                            }else if(result == 0){
                                $.messager.alert('提示','事件已上报！','info');
                            }else{
                                $.messager.alert('提示','事件上报失败！','info');
                            }
                            searchData();
                            modleclose();
                        },
                        error:function(data){
                            $.messager.alert('错误','连接错误！','error');
                            modleclose();
                        }
                    });
                }
            });
        }

    }
    
    function recallWorkflow() {
    	if(idStr) {
    		$.messager.confirm('提示', '您确定撤回选中的事件吗？', function(r) {
    			if(r) {
	    			var status = null;
	    			
		            if($('#list').length) {
		                var item = $('#list').datagrid('getSelections');
		                if(item.length == 1) {
		                    status = item[0].status;
		                }
		            } else if($("#"+ idStr +"_status").length) {
		                status = $("#"+ idStr +"_status").val();
		            }
		
		            if(status) {
		            	if(status == '04') {
		            		$.messager.alert('警告','该事件已归档，无法进行撤回操作!','warning');
		            	} else if(_instanceId) {
		            		modleopen();
		            		
		            		$.ajax({
		            			type: "POST",
		            			url: '${rc.getContextPath()}/zhsq/workflow/workflowController/recallWorkflow.jhtml',
		            			data: {'instanceId': _instanceId},
		            			dataType:"json",
		            			success: function(data) {
		            				modleclose();
		            				
		            				if(data.result == true) {
		            					$.messager.alert('提示', '事件撤回操作成功！', 'info');
		            					
		            					<#if model?? && model == 'l'>
		            						loadMessage((pageNum?pageNum:1),$('#pageSize').val());
		            					<#else>
		            						flashData(true);
		            					</#if>
		            				} else if(isNotBlankStringTrim(data.msgWrong)) {
		            					$.messager.alert('错误', data.msgWrong, 'error');
		            				} else {
		            					$.messager.alert('错误','事件撤回操作失败！','error');
		            				}
		            			},
		            			error:function(data) {
		            				$.messager.alert('错误','事件撤回操作失败！','error');
		            			}
		            		});
		            	} else {
		            		 $.messager.alert('警告','该事件无法进行撤回操作!','warning');
		            	}
		            } else {
		                $.messager.alert('警告','该事件无法进行撤回操作!','warning');
		            }
	            }
    		});
        } else {
            $.messager.alert('警告','请选择一条需要撤回操作的事件!','warning');
        }
    }
    
    function btnAuthority() {//按钮展示权限设置
    	if(idStr) {
    		var status = null;
    		
    		if($('#list').length) {
    			var item = $('#list').datagrid('getSelections');
    			if(item.length == 1) {
    				status = item[0].status;
    			}
    		}  else if($("#"+ idStr +"_status").length) {
                status = $("#"+ idStr +"_status").val();
            }
            
            if(status == '00' || status == '01' || status == '02' || status == '03') {
            	$('#recallWorkflow').show();
            } else {
            	$('#recallWorkflow').hide();
            }
            
            <@block name="selfDefBtnAuthority"></@block>
    	}
    }
    
    function addTimeApplication() {
    	if(idStr==null || idStr=="") {
    		$.messager.alert('提示','请选中要申请延时的数据再执行此操作!','warning');
    		return;
    	}
    	
    	var url = "${rc.getContextPath()}/zhsq/eventExtend/toAddTimeApplication.jhtml?applicationType=3&eventId=" + idStr;
    	
    	<@block name="addTimeApplicationFunctionOpenMethod">
    	showMaxJqueryWindow("申请延时", url, 480, 280);
    	</@block>
    }
	
    function changeEventList() {
        var urlSearch = window.location.search;//获取当前连接携带的参数
        var extraParams = '${extraParams!}';
        var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=${eventType}&model=${model}&trigger=${trigger!}&eventAttrTrigger=${eventAttrTrigger!}';

        if(extraParams != ''){
            url += '&extraParams='+encodeURIComponent(extraParams);

        }

        if(isNotBlankStringTrim(urlSearch)) {
            var codeKey = "system_privilege_code",
                    actionKey = "system_privilege_action",
                    codeIndex = urlSearch.indexOf(codeKey),
                    actionIndex = urlSearch.indexOf(actionKey),
                    nameKey = "";

            if(codeIndex > 0 || actionIndex > 0) {
                urlSearch = urlSearch.substring(1);//去除起始的问号

                var paramsArray = urlSearch.split('&'),//分离各个参数
                        paramArray = null,		//[0]key [1]value
                        paramItem = null,		//paramsArray的组员
                        paramObj = {},			//转换后的参数对象
                        paramArrayKey = null,	//参数key
                        paramArrayVal = null;	//参数value

                for(var index = 0, len = paramsArray.length; index < len; index++) {//将url参数转换为对象
                    paramItem = paramsArray[index];

                    if(isNotBlankStringTrim(paramItem) && paramItem.indexOf("=") > 0) {
                        paramArray = paramItem.split("=");//分离参数的key和value，但是如果参数值中含有等号，则会导致使用有误

                        if(paramArray.length == 2) {
                            paramArrayKey = paramArray[0];
                            paramArrayVal = paramArray[1];

                            if(isNotBlankStringTrim(paramArrayKey)) {//参数Key有效时，才添加参数
                                paramArrayKey = $.trim(paramArrayKey);//去除收尾空格

                                if(isBlankStringTrim(paramArrayVal)) {//防止添加了undefined
                                    paramArrayVal = "";
                                }

                                paramObj[paramArrayKey] = paramArrayVal;
                            }
                        }
                    }
                }

                if(codeIndex > 0) {
                    nameKey = codeKey;
                } else if(actionIndex > 0) {
                    nameKey = actionKey;
                }

                if(isNotBlankStringTrim(nameKey)) {
                    url += '&' + nameKey + '=' + paramObj[nameKey];//增添权限参数
                }
            }
        }

        $("#changeEventList").attr("href", url);
    }
	
	<@block name="resetConditionFunction">
	    function resetCondition() {//重置
	    	clear4DateRender();
	    	
	   	    $('#eventQueryForm')[0].reset();
	        $("#keyWord").val("事件描述/标题/事发详址");
	        $("#keyWord").attr('style','font-size:12px;color:gray; width:150px;');
		<#if eventType=="all" || eventType=="attention">
	        $('#statuss').combobox('setValue', "");
			<#if statuss??>
	            var statusArr = "${statuss}".split(',');
	            if($('#statuss').val()!=null)
	                $('#statuss').combobox('setValues', statusArr);
			</#if>
		</#if>
		<@block name="treeStyleEvent">
			<#if 'left'!=treStyle>
				orgtreeset.reset();
				$("#infoOrgCode").val('');
				$("#gridId").val('');
			</#if>
		</@block>
        allSearchData($("#_allSearchAnchor"));
    }
	</@block>
    function conditionSearch(){//查询
        var iconDiv = $("#iconDiv > a[class='icon_select']");
        if(iconDiv.length > 0) {
            iconDiv.eq(0).click();
        } else {
            searchData();
        }

    }

    function _onkeydown(){
        var keyCode = event.keyCode;
        if(keyCode == 13){
            conditionSearch();
        }
    }

    function allSearchData(obj) {//点击所有图标
        iconSelect(obj);
        searchData();
    }

    function urgencySearchData(obj){//点击紧急图标
        var searchArray = {};
        iconSelect(obj);

        searchArray["urgencyDegree"] = "02,03,04";
        searchData(null, searchArray);
    }
    
    function superviseSearchData(obj) {//点击督办图标
    	var searchArray = {};
    	iconSelect(obj);
    	
    	searchArray["superviseMark"] = "1";
    	searchData(null, searchArray);
    }
    
    function handleSearchData(handleStatus, obj){//点击超时、即将超时图标
        var searchArray = {};
        iconSelect(obj);

        //searchArray["handleStatus"] = handleStatus;
        searchArray["handleDateFlag"] = handleStatus;
        searchData(null, searchArray);
    }

    function remindSearchData(remindStatus, obj){//点击催办图标
        var searchArray = {};
        iconSelect(obj);

        searchArray["remindStatus"] = remindStatus;
        searchData(null, searchArray);
    }
    
    <@block name="function_addIcons">
    function addIcons(){//id在column_event.ftl中使用
    	<@block name="function_addIcons_body">
        var iconDivLength = $("#iconDiv").length;
        if(iconDivLength == 0){
            var handleDateFlag = $("#handleDateFlag").val();
            var iconObj = {'handleDateFlag_1': 'ToolBarNormal', 'handleDateFlag_2': 'ToolBarDue', 'handleDateFlag_3': 'ToolBarOverDue'};
            var icons = '<div id="iconDiv" class="fl">'+
			<#if eventType??>
				<#if eventType != 'draft'>
					'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="allSearchData(this);"><i class="ToolBarAll"></i>所有</a>'+
				</#if>
				<#if eventType == 'todo'>
					'<a href="###" id="_remindSearchAnchor" onclick="remindSearchData(1, this);"><i class="ToolBarRemind"></i>催办</a>'+
				</#if>
				<#if eventType != 'draft'>
					'<a href="###" id="_urgencySearchAnchor" onclick="urgencySearchData(this);"><i class="ToolBarUrgency"></i>紧急</a>'+
					<#if eventType == 'all'>
						'<a href="###" id="_superviseSearchAnchor" onclick="superviseSearchData(this);"><i class="ToolBarSupervise"></i>已督办</a>'+
					</#if>
					'<a href="###" id="_dueSearchAnchor" onclick="handleSearchData(\'2\', this);"><i class="ToolBarDue"></i>将到期</a>'+
					'<a href="###" id="_overDueSearchAnchor" onclick="handleSearchData(\'3\', this);"><i class="ToolBarOverDue"></i>已过期</a>'+
					'<a href="###" id="_normalSearchAnchor" onclick="handleSearchData(\'1\', this);"><i class="ToolBarNormal"></i>正常</a>'+
				</#if>
			</#if>
			'</div>';
			
            $('#ToolBar').append(icons);
            var iconClass = iconObj["handleDateFlag_" + handleDateFlag];
            if(iconClass) {
                $("#iconDiv > a").hide();
                $("#iconDiv > a > i." + iconClass).parent().addClass("icon_select icon_select_pre").css("display", "inline-block");
                $("#iconDiv > a:hidden").remove();
            }
            
            //快捷图标有变更，需要重新获取
            iconLen = $('#iconDiv > a').length;
            
            if(iconLen > 5) {
            	$('#iconDiv').width($('#iconDiv > a').outerWidth() * iconLen);
            }
            
            var toolBarWidth = $('#toolBarFrDiv').outerWidth(true),
            	toolBarArchorWidth = 0;
            	
            $('#toolBarFrDiv > a').each(function() {
            	toolBarArchorWidth += $(this).outerWidth(true);
            });
            
            if(toolBarArchorWidth < toolBarWidth || toolBarArchorWidth == 0) {
            	$('#toolBarFrDiv').width(toolBarArchorWidth);
            }
        }
        </@block>
    }
    </@block>

    function iconSelect(obj){//为选择的图标增添凹陷效果
        if($("#iconDiv > a > i.icon_select_pre").length == 0) {
            if(isNotBlankParam(obj)){
                iconUnSelect();
                $(obj).addClass('icon_select');
            }
        }
    }

    function iconUnSelect(){//去除图片的凹陷效果
        $("#iconDiv > a[class='icon_select']").each(function(){
            $(this).removeClass('icon_select');
        });
    }

    function queryData(searchArray){
        var postData = {};
        if(searchArray!=undefined && searchArray!=null){
            postData = searchArray;
        }
        
        <@block name="extraQueryParams"></@block>

        $("#eventQueryForm .queryParam").each(function() {
            var name = $(this).attr("name"), val = $(this).val();

            if(name && val) {
                postData[name] = val;
            }
        });

        $("#eventQueryForm .queryComboboxId").each(function() {
            var name = $(this).val(), val = $('#' + name).combobox('getValues');

            if(name && val && isBlankParam(postData[name])) {
                postData[name] = val;
            }
        });

        var keyWord = $("#keyWord").val();
        if(keyWord!=null && keyWord!="" && keyWord!="事件描述/标题/事发详址") {
            postData["keyWord"]=keyWord;
        }
        
        <#if eventType=="view" || eventType=="all" || eventType=="toRemind" || eventType=="history">
        	var infoOrgCode = $("#infoOrgCode").val();
        	if(infoOrgCode==null || infoOrgCode=="") {
        		 postData['eInfoOrgCode'] = startInfoOrgCode;
        	}
        </#if>

        //高级查询
        <#if eventType=="all" || eventType=="attention">
        	var eventStatus = "";
	        var statuss = $('#statuss').combobox('getValues');
	
	        if(statuss.length > 0){
	            var status = "";
	
	            for(var i=0;i<statuss.length;i++){
	                status = statuss[i];
	
	                if(status == 'do'){
	                    eventStatus += ',00,01,02,03';
	                } else if(status == 'end'){
	                    eventStatus += ',04';
	                } else {
	                    eventStatus += "," + status;
	                }
	            }
	
	            if(eventStatus && eventStatus.length > 0) {
	                eventStatus = eventStatus.substring(1);
	            }
	        }
	        if(eventStatus!=null && eventStatus!="") {
	            postData["eventStatus"]=eventStatus;
	        }
        </#if>
        
        postData["typesForList"] = "${typesForList!}";
        postData["eventAttrTrigger"] = "${eventAttrTrigger!}";
        postData["isRemoveTypes"] = "<#if isRemoveTypes??>${isRemoveTypes?c}</#if>";
        
        var iconDiv = $("#iconDiv > a[class='icon_select']"),
        	  iconDivLen = iconDiv.length;
		
		if(iconDivLen > 0) {
			var iconId = "";
			
			iconDiv.each(function(){
				iconId = $(this).attr("id");
			});
			
			if(iconId == "_remindSearchAnchor") {//催办
				postData["remindStatus"] = "1";
			} else if(iconId == "_urgencySearchAnchor") {//紧急
				postData["urgencyDegree"] = "02,03,04";
			} else if(iconId == "_dueSearchAnchor") {//将到期
				postData["handleDateFlag"] = "2";
			} else if(iconId == "_overDueSearchAnchor") {//已过期
				postData["handleDateFlag"] = "3";
			} else if(iconId == "_normalSearchAnchor") {//正常
				postData["handleDateFlag"] = "1";
			}
		}
		
        return postData;
    }

    var isEditFlag;
    function locationTagging(gridId,x,y,mapt,isEdit){
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml'
        var width = 480;
        var height = 380;
        var mapType = 'EVENT_V1';
        isEditFlag = isEdit;
        initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType)
    }
    function mapMarkerSelectorCallback(mapt, x, y){//将标注信息传递到新增、编辑页面
        var childIframeContents = $("#"+maxJqueryWindowId).find("iframe").contents();
        childIframeContents.find("#mapt").val(mapt);
        childIframeContents.find("#x").val(x);
        childIframeContents.find("#y").val(y);
        var showName = "修改地理位置";
        if(isEditFlag == false) {
            showName = "查看地理位置";
        }
        childIframeContents.find("#mapTab").html(showName);
        childIframeContents.find(".mapTab").addClass("mapTab2");
        closeMaxJqueryWindowForCross();
    }
    
	<@block name="exclusiveFunction"></@block>
	
	//获取url
	function getUrlStr(isExport){
		  var model='${model}';
		  if(isExport){
		 	 model="e";
		  }
	   var urlSearch = window.location.search;//获取当前连接携带的参数
		var extraParams = '${extraParams!}';
		var url = '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=${eventType}&model='+model+'&trigger=${trigger!}&eventAttrTrigger=${eventAttrTrigger!}';
		
		if(extraParams != ''){
			url += '&extraParams='+encodeURIComponent(extraParams);
	
		}
		
		if(isNotBlankStringTrim(urlSearch)) {
			var codeKey = "system_privilege_code",
				actionKey = "system_privilege_action",
				codeIndex = urlSearch.indexOf(codeKey),
				actionIndex = urlSearch.indexOf(actionKey),
				nameKey = "";
			
			if(codeIndex > 0 || actionIndex > 0) {
				urlSearch = urlSearch.substring(1);//去除起始的问号
			
				var paramsArray = urlSearch.split('&'),//分离各个参数
					paramArray = null,		//[0]key [1]value
					paramItem = null,		//paramsArray的组员
					paramObj = {},			//转换后的参数对象
					paramArrayKey = null,	//参数key
					paramArrayVal = null;	//参数value
				
				for(var index = 0, len = paramsArray.length; index < len; index++) {//将url参数转换为对象
					paramItem = paramsArray[index];
					
					if(isNotBlankStringTrim(paramItem) && paramItem.indexOf("=") > 0) {
						paramArray = paramItem.split("=");//分离参数的key和value，但是如果参数值中含有等号，则会导致使用有误
						
						if(paramArray.length == 2) {
							paramArrayKey = paramArray[0];
							paramArrayVal = paramArray[1];
							
							if(isNotBlankStringTrim(paramArrayKey)) {//参数Key有效时，才添加参数
								paramArrayKey = $.trim(paramArrayKey);//去除收尾空格
								
								if(isBlankStringTrim(paramArrayVal)) {//防止添加了undefined
									paramArrayVal = "";
								}
								
								paramObj[paramArrayKey] = paramArrayVal;
							}
						}
					}
				}
				
				if(codeIndex > 0) {
					nameKey = codeKey;
				} else if(actionIndex > 0) {
					nameKey = actionKey;
				}
				
				if(isNotBlankStringTrim(nameKey)) {
					url += '&' + nameKey + '=' + paramObj[nameKey];//增添权限参数
				}
			}
			
			
		}
	   return url;
	}
  //弹出导出页面	
    function exportExcel(){
    		var url=getUrlStr(true);
    		showMaxJqueryWindow("导出事件", url);
    } 
  //导出
    function doExportExcel(){
    	$.messager.confirm('提示', '是否导出查询数据？', function(r){
    		if (r) {  
    			document.excelForm.submit();  
    		}
    	});

    }
    
    function init4DateRender(renderId, startTimeId, endTimeId) {
    	var dateRender = null;
    	
    	dateRender = $('#' + renderId).anoleDateRender({
    		BackfillType : "1",
    		ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
    		ShowOptions : {
    			TabItems : ["常用", "年", "季", "月", "清空"]
    		},
    		BackEvents : {
    			OnSelected : function(api) {
    				$("#" + startTimeId).val(api.getStartDate());
    				$("#" + endTimeId).val(api.getEndDate());
    			},
    			OnCleared : function() {
    				$("#" + startTimeId).val('');
    				$("#" + endTimeId).val('');
    			}
    		}
    	}).anoleDateApi();
    	
    	return dateRender;
    }
    
    function clear4DateRender() {
    	if(createTimeDateRender != null) {
    		createTimeDateRender.doClear();
    	}
    	if(happenTimeDateRender != null) {
    		happenTimeDateRender.doClear();
    	}
    	if(handleDateRender != null) {
    		handleDateRender.doClear();
    	}
    	if(finTimeDateRender != null) {
    		finTimeDateRender.doClear();
    	}
    	if(archiveTimeDateRender != null) {
    		archiveTimeDateRender.doClear();
    	}
    }
</script>