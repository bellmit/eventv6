<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地理位置标注组件测试页面</title>
<#include "/component/commonFiles-1.1.ftl">
<#include "/component/ComboBox.ftl" />
<script type="text/javascript" src="${ZHSQ_EVENT}/js/components/labelLocationPlugin/jquery.anole.labelLocation.js"></script>
</head>
<body>
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
	   <div class="NorForm NorForm2">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
          	<tr>
                <td>
                    <label class="LabName">
                    	<span>地理位置标注1：</span>
                    </label>
                    <!-- <input id="labelLocation1" type="text" style="width:365px" class="inp1 inp2 easyui-validatebox"/>

					-->
					<div id="labelLocation1" style="height:100%;float: left;"></div>
                </td>
          		<td>
                    <label class="LabName">
                    	<span>行政编码1：</span>
                    </label>
                    <input id="labelLocation2" type="text" style="width:365px" class="inp1 inp2 easyui-validatebox"/>
				</td>
          	</tr>
    		</table>
        </div>
	</div>

    <div class="BigTool">
        <div class="BtnList">
            <a href="###" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
            <a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
        </div>
    </div>
</body>

<script type="text/javascript">
var ztreeComboBoxObj = null;
$(function(){

	//模拟城市部件新增页面
	$("#labelLocation1").labelLocationRender({
        _showTypes : 'buildingLabelLocation,addressLabelLocation,mapLabelLocation',//新增编辑使用
		_isEdit : 'true',//新增编辑时位true,详情为false
        _moduleTypeCode : '020130',//城市部件
        _callBackUrl : '${ZHSQ_EVENT!""}/zhsq/map/labelLocation/labelLocationController/toLabelLocationCrossDomain.jhtml'
	});

    //模拟城市部件修改页面
    <#--$("#labelLocation1").labelLocationRender({-->
		<#--_bizId : "6",-->
        <#--_showTypes : 'buildingLabelLocation,addressLabelLocation,mapLabelLocation',//新增编辑使用-->
        <#--_isEdit : 'true',//新增编辑时位true,详情为false-->
        <#--_moduleTypeCode : '020130', //城市部件-->
        <#--_callBackUrl : '${ZHSQ_EVENT!""}/zhsq/map/labelLocation/labelLocationController/toLabelLocationCrossDomain.jhtml'-->
    <#--});-->

    <#--//模拟详情页面查看楼宇地理位置-->
    <#--$("#labelLocation1").labelLocationRender({-->
        <#--_isEdit : 'false',//新增编辑时为true,详情为false-->
        <#--_moduleTypeCode : 'CASE',-->
        <#--_labelLocationType : 'building',//标注类型，楼宇：building；部件：components。详情查看地理位置使用-->
        <#--_resId : "1046559",//楼宇id，查看详情时使用。-->
        <#--_callBackUrl : '${ZHSQ_EVENT!""}/zhsq/map/labelLocation/labelLocationController/toLabelLocationCrossDomain.jhtml'-->
    <#--});-->


    <#--//模拟详情页面查看部件地理位置-->
    <#--$("#labelLocation1").labelLocationRender({-->
        <#--_isEdit : 'false',//新增编辑时为true,详情为false-->
        <#--_moduleTypeCode : 'CASE',-->
        <#--_labelLocationType : 'components',//标注类型，楼宇：building；部件：components。详情查看地理位置使用-->
        <#--_resId : "6",//部件id，查看详情时使用。-->
        <#--_callBackUrl : '${ZHSQ_EVENT!""}/zhsq/map/labelLocation/labelLocationController/toLabelLocationCrossDomain.jhtml'-->
    <#--});-->

});

function tableSubmit(){
    var x = $("#x").val();
    var y = $("#y").val();
    var mapt = $("#mapt").val();
	var moduleTypeCode = $("#moduleTypeCode").val();
    var _resId = $("#_resId").val();
    var _labelLocationType = $("#_labelLocationType").val();

    alert("x:"+x+"\ny:"+y+"\nmapt:"+mapt+"\nmoduleTypeCode:"+moduleTypeCode+"\n_resId:"+_resId+"\n_labelLocationType:"+_labelLocationType);
}


</script>
</html>