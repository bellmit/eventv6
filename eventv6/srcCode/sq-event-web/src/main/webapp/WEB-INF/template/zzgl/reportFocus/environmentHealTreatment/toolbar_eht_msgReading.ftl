<#include "/component/ComboBox.ftl" />
<style type="text/css">
    .width65px{width:75px;}
    .w150{width:150px;}
    .keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
    <form id="msgReadingForm">
        <input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
        <input type="hidden" id="reportType" name="reportType" value="${reportType!'4'}" class="queryParam" />
        <input type="hidden" name="msgModuleCode" class="queryParam" value="250101,250102,250103" />

        <div class="ConSearch">
            <div class="fl">
                <ul>
                    <li>所属区域：</li>
                    <li>
                        <input id="infoOrgCode" name="eRegionCode" type="text" class="hide queryParam"/>
                        <input id="gridId" type="text" class="hide"/>
                        <input id="gridName" type="text" class="inp1 InpDisable w150" />
                    </li>
                    <li>阅读状态：</li>
                    <li>
                        <input id="msgReceiveStatus" name="msgReceiveStatus" type="text" value="" class="queryParam hide"/>
                        <input id="msgReceiveStatusName" name="msgReceiveStatusName" type="text" class="inp1 InpDisable w150" />
                    </li>
                    <li>关键字：</li>
                    <li><input name="msgKeyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="报告人姓名/报告内容" defaultValue="报告人姓名/报告内容" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td><label class="LabName width65px"><span>报告编号：</span></label><input class="inp1 queryParam" type="text" id="reportCode" name="reportCode" style="width:248px;"></input></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>报告时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="msgSendDayStart" name="msgSendDayStart" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'msgSendDayEnd\')}',readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="msgSendDayEnd" name="msgSendDayEnd" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'msgSendDayStart\')}',readOnly:true})" readonly="readonly"></input>
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
            <div id="actionDiv" class="tool fr hide"><@actionCheck></@actionCheck></div>
        </div>
    </form>
</div>

<script type="text/javascript">
    var msgReceiveStatusComboBox = null;

    $(function() {
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

        msgReceiveStatusComboBox = AnoleApi.initListComboBox("msgReceiveStatusName", "msgReceiveStatus", null, null, ["0"], {
            DataSrc : [{"name":"未读", "value":"0"},{"name":"已读", "value":"1"}],
            IsTriggerDocument: false,
            ShowOptions:{
                EnableToolbar : true
            }
        });

        if($("#actionDiv").find("a").length) {
            $("#actionDiv").show();
        } else {
            $("#toolbarDiv").remove();
        }
    });

    function detail(reportUUID, instanceId, msgId, msgReceiveStatus) {
        if(msgId && msgReceiveStatus && msgReceiveStatus == '0') {
            $.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/reportEHT/readMsg.jhtml',
                data: {'msgId' : msgId},
                dataType:"json",
                success: function(data) {
                    if(data.success && data.success == true) {
                        searchData(true);
                    }
                },
                error:function(data) {
                    $.messager.alert('错误','消息阅读失败！','error');
                }
            });
        }
        if(reportUUID) {
            var listType = $("#listType").val();

            var url = "${rc.getContextPath()}/zhsq/reportEHT/toDetail.jhtml?reportUUID=" + reportUUID + "&listType=" + listType + "&reportType=" + $('#reportType').val();

            if(instanceId) {
                url += "&instanceId=" + instanceId;
            }

            openJqueryWindowByParams({
                maxWidth: 1000,
                title: "查看监测信息",
                targetUrl: url
            });
        } else {
            $.messager.alert('警告','请选择需要查看的记录!','warning');
        }
    }

    function resetCondition() {//重置
        $('#msgReadingForm')[0].reset();
        $('#keyWord').addClass('keyBlank');

        <!--为了重置后，字典名称能正常展示-->
        msgReceiveStatusComboBox.setSelectedNodes(["0"]);

        searchData();
    }

    function searchData(isCurrent){//查询
        doSearch(queryData(), isCurrent);
    }

    function queryData() {
        var searchArray = new Array();

        $("#msgReadingForm .queryParam").each(function() {
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
        var keyWord = $(obj).val();

        if(keyWord == ''){
            $(obj).addClass('keyBlank');
            $(obj).val($(obj).attr("defaultValue"));
        }
    }
</script>