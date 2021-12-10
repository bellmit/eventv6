<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地址组件测试页面</title>
<#include "/component/commonFiles-1.1.ftl">
<#include "/component/ComboBox.ftl" />
<script type="text/javascript" src="${GEO_DOMAIN}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
</head>
<body>
<div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
    <div class="NorForm NorForm2">
        <form id="tableForm" name="tableForm" method="POST" action="${rc.getContextPath()}/geo/testController/getXieJingAddrByFile.jhtml" ENCTYPE="multipart/form-data">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="6">
                        <label class="LabName">
                            <span>协警地址：</span>
                        </label>
                        <input name="addressName" id="addressName" type="text" style="width:300px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>addressCode：</span>
                        </label>
                        <input name="addressCode" id="addressCode" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>infoOrgCode：</span>
                        </label>
                        <input name="infoOrgCode" id="infoOrgCode" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>infoOrgName：</span>
                        </label>
                        <input name="infoOrgName" id="infoOrgName" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>x：</span>
                        </label>
                        <input name="x" id="x" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>y：</span>
                        </label>
                        <input name="y" id="y" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>mapType：</span>
                        </label>
                        <input name="mapType" id="mapType" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        <label class="LabName">
                            <span>行政区划2：</span>
                        </label>
                        <input name="addressName2" id="addressName2" type="text" style="width:300px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>addressCode2：</span>
                        </label>
                        <input name="addressCode2" id="addressCode2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>infoOrgCode2：</span>
                        </label>
                        <input name="infoOrgCode2" id="infoOrgCode2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>infoOrgName2：</span>
                        </label>
                        <input name="infoOrgName2" id="infoOrgName2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>x2：</span>
                        </label>
                        <input name="x2" id="x2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>y2：</span>
                        </label>
                        <input name="y2" id="y2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>mapType2：</span>
                        </label>
                        <input name="mapType2" id="mapType2" type="text" style="width:150px"
                               class="inp1 inp2 easyui-validatebox" value=""/>
                    </td>
                </tr><tr>
                <td colspan="6">
                    <label class="LabName">
                        <span>户籍地：</span>
                    </label>
                    <input name="addressName3" id="addressName3" type="text" style="width:300px"
                           class="inp1 inp2 easyui-validatebox" value=""/>
                </td>
            </tr>
            </table>
        </form>
    </div>
</div>
</body>

<script type="text/javascript">
$(function(){

    $("#addressName").anoleAddressRender({
        _source : 'XIEJING',//必传参数，数据来源
        //_startDivisionCode : "350582", //默认选中网格，非必传参数
//        _start_level : 0, //从哪个层级开始显示，非必传参数，如果_startDivisionCode参数有传的话，_start_level这个参数设置的层级应该大于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置0-3
//        _show_level : 6,  //显示到哪个层级，7--网格，非必传参数，如果_startDivisionCode参数有传的话，_show_level这个参数设置的层级应该小于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置3-6
//        _addressMap : {//编辑页面可以传这个参数，非必传参数
//            _addressMapIsEdit : true,
//            _addressMapX : 118.54727185058594,
//            _addressMapY : 24.785271514892578,
//            _addressMapType : 5
//        },
        BackEvents : {
            OnSelected : function(api) {
                $("#addressName").val(api.getAddress());
                $("#addressCode").val(api.getAddressCode());
                $("#infoOrgCode").val(api.getInfoOrgCode());
                $("#infoOrgName").val(api.getInfoOrgName());
                $("#x").val(api.getAddressMapX());
                $("#y").val(api.getAddressMapY());
                $("#mapType").val(api.getAddressMapType());
            },
            OnCleared : function(api) {
                //清空按钮触发的事件
                $("#addressName").val('');
                $("#addressCode").val('');
                $("#infoOrgCode").val('');
            }
        }
    });

    //初始化地址组件
    $("#addressName2").anoleAddressRender({
        _start_level : 0, //从哪个层级开始显示，非必传参数，如果_startDivisionCode参数有传的话，_start_level这个参数设置的层级应该大于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置0-3
        _show_level : 6,  //显示到哪个层级，7--网格，非必传参数，如果_startDivisionCode参数有传的话，_show_level这个参数设置的层级应该小于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置3-6
        //_show_searchBar : "false",
        _addressFormate: "省-县",//户籍地可以用到
        BackEvents : {
            OnSelected : function(api) {
                //包含‘/’
                $("#addressName2").val(api.getAddress());
                $("#addressCode2").val(api.getAddressCode());
                $("#infoOrgCode2").val(api.getInfoOrgCode());
                $("#infoOrgName2").val(api.getInfoOrgName());
            },
            OnCleared : function(api) {
                //清空按钮触发的事件
                $("#addressName2").val('');
                $("#addressCode2").val('');
                $("#infoOrgCode2").val('');
            }
        }
    });

    //初始化地址组件
    $("#addressName3").anoleAddressRender({
        _start_level : 1, //从哪个层级开始显示，非必传参数，如果_startDivisionCode参数有传的话，_start_level这个参数设置的层级应该大于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置0-3
        _show_level : 3,  //显示到哪个层级，7--网格，非必传参数，如果_startDivisionCode参数有传的话，_show_level这个参数设置的层级应该小于_startDivisionCode参数的网格层级。如_startDivisionCode=350582，是区县层级，则_start_level只能设置3-6
        _show_searchBar : "false",
        _addressFormate: "省/县",//户籍地可以用到
        BackEvents : {
            OnSelected : function(api) {
                //包含‘/’
                $("#addressName3").val(api.getAddress());
            },
            OnCleared : function(api) {
                //清空按钮触发的事件
                $("#addressName3").val('');
            }
        }
    });

});


</script>
</html>