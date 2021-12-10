<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图首页</title>

    <!-- 公共css、js -->
	<#include "/component/commonFiles-1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
</head>
<body>
<div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
    <div class="NorForm NorForm2">
        <form id="tableForm" name="tableForm" method="POST" action="${rc.getContextPath()}/geo/testController/getXieJingAddrByFile.jhtml" ENCTYPE="multipart/form-data">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="6">
                        <label class="LabName">
                            <span>url：</span>
                        </label>
                        <input name="npFeatureUrl" id="npFeatureUrl" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="http://172.21.111.1:8085/geo/Nanping_MapCG_Com/ows"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>service：</span>
                        </label>
                        <input name="service" id="service" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="WFS"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>version：</span>
                        </label>
                        <input name="version" id="version" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="1.0.0"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>request：</span>
                        </label>
                        <input name="request" id="request" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="GetFeature"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>typeName：</span>
                        </label>
                        <input name="typeName" id="typeName" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="Nanping_MapCG_Com:comp_0101"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>maxFeatures：</span>
                        </label>
                        <input name="maxFeatures" id="maxFeatures" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="50"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>outputFormat：</span>
                        </label>
                        <input name="outputFormat" id="outputFormat" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="GML2"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>第三方照片公共路径：</span>
                        </label>
                        <input name="photoPath" id="photoPath" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox" value="http://172.21.111.1:8085/DigitalCityWeb/compImg/nanping/photo/"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName">
                            <span>对应模块类型：</span>
                        </label>
                        <input name="resTypeId" id="resTypeId" type="hidden"/>
                        <input name="resTypeCode" id="resTypeCode" type="hidden"/>
                        <input name="resTypeName" id="resTypeName" type="text" style="width:400px"
                               class="inp1 inp2 easyui-validatebox"/>
                    </td>
                </tr>
            </table>
        </form>

    </div>
</div>
<div class="BigTool">
    <div class="BtnList">
        <a href="###" id="submit" onclick="tableSubmit();" class="BigNorToolBtn BigJieAnBtn">获取数据</a>
    </div>
</div>
</body>
<script type="text/javascript">

    var resTypeArr = [];
    $(function(){
        resTypeArr = [
            <#if resTypeList??>
                <#list resTypeList as resType>
                    {"id": "${resType.resTypeId}","pid": "${resType.parentTypeId}","name":"${resType.name}", "value":"${resType.typeCode}"},
                </#list>
            </#if>
        ];
        AnoleApi.initTreeComboBox("resTypeName", "resTypeCode", null, null, [""], {
            DataSrc : resTypeArr,
            ShowOptions : {
                EnableToolbar : true
            }
        });

    });

	function tableSubmit() {
        getNpObjsData();
    }

    function getNpObjsData(){
        var data="";
        var npFeatureUrl = $("#npFeatureUrl").val();
        if(npFeatureUrl == ""){
            npFeatureUrl = "http://172.21.111.1:8085/geo/Nanping_MapCG_Com/ows";
        }
        data = data + "npFeatureUrl=" + npFeatureUrl;
        var service = $("#service").val();
        if(service == ""){
            service = "WFS";
        }
        data = data + "&service=" + service;
        var version = $("#version").val();
        if(version == ""){
            version = "1.0.0";
        }
        data = data + "&version=" + version;
        var request = $("#request").val();
        if(request == ""){
            request = "GetFeature";
        }
        data = data + "&request=" + request;
        var typeName = $("#typeName").val();
        if(typeName == ""){
            typeName = "Nanping_MapCG_Com:comp_0101";
        }
        data = data + "&typeName=" + typeName;
        var maxFeatures = $("#maxFeatures").val();
        if(maxFeatures != ""){
            data = data + "&maxFeatures=" + maxFeatures;
        }
        var outputFormat = $("#outputFormat").val();
        if(outputFormat == ""){
            outputFormat = "GML2";
        }
        data = data + "&outputFormat=" + outputFormat;

        var photoPath = $("#photoPath").val();
        if(photoPath == ""){
            photoPath = "http://172.21.111.1:8085/DigitalCityWeb/compImg/nanping/photo/";
        }
        data = data + "&photoPath=" + photoPath;

        var resTypeCode = $("#resTypeCode").val();
        data = data + "&resTypeCode=" + resTypeCode;


        modleopen()
        $.ajax({
            type: 'POST',
            url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getNpObjsData.jhtml",
            data: data,
            dataType: 'json',
            success: function (data) {
                modleclose();
                alert("获取数据成功！成功获取" + data + "条");
            },
            error:function(data){
                alert("error");
            }
        });
    }


</script>
</html>
