<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<style type="text/css">
    .width65px{width:75px;}
    .w150{width:150px;}
    .keyBlank{color:gray;}
    .icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
    .dateRenderWidth{width: 248px;}
</style>
<div id="jqueryToolbar">
    <form id="ppmQueryForm">
        <input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
        <input type="hidden" id="reportType" name="reportType" value="${reportType!'4'}" class="queryParam iconParam" />
        <input type="hidden" name="routineInterval"  class="queryParam" value="${routineInterval!}" />

        <div class="ConSearch">
            <div class="fl">
                <ul>
                    <li>所属区域：</li>
                    <li>
                        <input id="infoOrgCode" name="eRegionCode" type="text" class="hide queryParam"/>
                        <input id="gridId" type="text" class="hide"/>
                        <input id="gridName" type="text" class="inp1 InpDisable w150" />
                    </li>
                    <#if listType?? && (listType == '2' || listType == '3' || listType == '4' || listType == '5')>
                        <li>报告状态：</li>
                        <li>
                            <input id="reportStatus" name="reportStatus" type="text" value="" class="queryParam hide"/>
                            <input id="reportStatusName" type="text" class="inp1 InpDisable w150" />
                        </li>
                    </#if>
                    <#if listType?? && listType == '2'>
                        <li>超时状态：</li>
                        <li>
                            <input id="isOvertime" name="isOvertime" type="text" value="" class="queryParam hide"/>
                            <input id="isOvertimeName" type="text" class="inp1 InpDisable w150" />
                        </li>
                    </#if>
                    <li>关键字：</li>
                    <li><input name="keyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="报告人姓名/发生地址" defaultValue="报告人姓名/发生地址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td><label class="LabName width65px"><span>报告编号：</span></label><input class="inp1 easyui-validatebox queryParam" type="text" id="reportCode" name="reportCode" style="width:248px;" data-options="validType:['autoTrim[\'reportCode\']']"></input></td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>姓名：</span></label><input class="inp1 queryParam" type="text" id="personName" name="personName" style="width:248px;"></input></td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>证件号码：</span></label><input class="inp1 queryParam" type="text" id="cardNumber" name="cardNumber" style="width:248px;"></input></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>报告时间：</span></label>
                                                    <input type="text" id="_reportDayDateRender" startId="reportDayStart" endId="reportDayEnd" class="inp1 InpDisable dateRenderWidth" value=""/>
                                                </td>
                                            </tr>
                                            <#if listType?? && (listType == '6')>
                                                <tr>
                                                    <td>
                                                        <label class="LabName width65px"><span>办结时间：</span></label>
                                                        <input type="text" id="_instanceEndDayDateRender" startId="instanceEndDayStart" endId="instanceEndDayEnd" class="inp1 InpDisable dateRenderWidth" value=""/>
                                                    </td>
                                                </tr>
                                            </#if>
                                            <#if listType?? && (listType == '2' || listType == '5' || listType == '6')>
                                                <tr>
                                                    <td>
                                                        <label class="LabName width65px"><span>发现来源：</span></label>
                                                        <input type="text" class="hide queryParam" id="dataSourceArray" name="dataSourceArray" />
                                                        <input class="inp1" type="text" id="dataSourceArrayName" style="width:248px;"></input>
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
                    <li><a href="#" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
                    <li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition('ppmQueryForm')">重置</a></li>
                </ul>
            </div>
            <div class="clear"></div>‍

        </div>
        <div class="h_10 clear"></div>
        <div style="position: relative;">
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
        </div>

        <div class="ToolBar" id="toolbarDiv">
            <div id="actionDiv" class="tool fr hide">
                <@actionCheck></@actionCheck>
                <#--<a id="add" href="javascript:void(0)" onclick="add({dataSource:'01'})" class="NorToolBtn AddBtn">新增(网格员)</a>
                <a id="edit" href="javascript:void(0)" onclick="edit()" class="NorToolBtn EditBtn">编辑</a>
                <a id="del" href="javascript:void(0)" onclick="del()" class="NorToolBtn DelBtn">删除</a>
                <a id="add" href="javascript:void(0)" onclick="add({dataSource:'02'})" class="NorToolBtn AddBtn">新增(单位科室排查)</a>
                <a id="add" href="javascript:void(0)" onclick="add({dataSource:'03'})" class="NorToolBtn AddBtn">新增(一键报贫/热线)</a>
                <a id="add" href="javascript:void(0)" onclick="add({dataSource:'04'})" class="NorToolBtn AddBtn">新增(上级扶贫部门发现反馈)</a>
                <a id="add" href="javascript:void(0)" onclick="add({dataSource:'05'})" class="NorToolBtn AddBtn">新增(扶责任人走访)</a>
                <a id="add" href="javascript:void(0)" onclick="add({dataSource:'06'})" class="NorToolBtn AddBtn">新增(泉州市级领导)</a>-->
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function() {
        var listType = $('#listType').val();

        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.eOrgCode);
            }
        }, {
            OnCleared: function() {
                $("#infoOrgCode").val('');
            },
            ShowOptions: {
                EnableToolbar : true
            }
        });

        if($('#reportStatus').length > 0) {
            AnoleApi.initTreeComboBox("reportStatusName", "reportStatus", "B210011001", null, null, {
                RenderType : "01",
                ShowOptions: {
                    EnableToolbar : true
                }
            });
        }

        if($('#dataSourceArrayName').length > 0) {
            AnoleApi.initTreeComboBox("dataSourceArrayName", "dataSourceArray", "B210011002", null, null, {
                RenderType : "01",
                ShowOptions: {
                    EnableToolbar : true
                }
            });
        }

        if($('#isOvertime').length > 0) {
            AnoleApi.initListComboBox("isOvertimeName", "isOvertime", null, null, [""], {
                DataSrc: [
                    {name:'超时', value:'1'}
                ],
                ShowOptions : {
                    EnableToolbar : true
                }
            });
        }

        init4DateRender('ppmQueryForm');
    });

    function queryData() {
        var searchArray = new Array();

        $("#ppmQueryForm .queryParam").each(function() {
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
</script>

<@block name="operateFunction">
    <#include "/zzgl/reportFocus/povertyPreMonitor/operate_ppm.ftl" />
</@block>

<#include "/zzgl/reportFocus/base/list_base.ftl" />