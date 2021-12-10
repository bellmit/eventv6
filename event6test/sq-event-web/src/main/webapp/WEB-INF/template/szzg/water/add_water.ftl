<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<style type="text/css">
		.inp1 {width:160px;}
		.LabName{
			width:120px;
		}
		.numberbox{
			height:28px !important;
		}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="id" name="id" value="<#if bo.seqId??>${bo.seqId?c}</#if>" />
		<input type="hidden" id="seqId" name="seqId" value="<#if bo.seqId??>${bo.seqId?c}</#if>" />
		<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if bo.seqId??>1<#else>0</#if>" />
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd"><label class="LabName"><span>所属区域：</span></label>
                            <input type="hidden" id="orgCode" name="orgCode"  value="<#if bo.orgCode??>${bo.orgCode!''}</#if>" >
                            <input  class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="orgName"
                                   data-options="required:true" name="orgName"  value="<#if bo.orgName??>${bo.orgName!''}</#if>" />
                        </td>
						<td>
							<label class="LabName"><span>监测站点名称：</span></label>
							<input type="text" id="name" name="name" value="${(bo.name)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[40]', tipPosition:'bottom',required:true" />
						</td>
					</tr>
				   <tr>
						<td>
							<label class="LabName"><span>经度：</span></label>
							<input readonly='readonly' type="text" id="longitude" name="longitude" value="${(bo.longitude)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'" />
						</td>
						<td>
							<label class="LabName"><span>纬度：</span></label>
							<input readonly='readonly' type="text" id="dimensions" name="dimensions" value="${(bo.dimensions)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'" />
						</td>
					</tr>
                    <tr>
	                    <td >
								<label class="LabName"><span>水质类别：</span></label>
								<input type="hidden" name="szlb" id="szlb" value='${(bo.szlb)!}' />
								<input type="text" id="szlbName" name="szlbName" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom',required:true" />
						</td>
					
						<td>
							<label class="LabName"><span>PH(无量纲)：</span></label>
							<input type="text" id="phvalue" name="phvalue" value="${(bo.phvalue)!}"  class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
					    <td>
							<label class="LabName"><span>溶解氧(mg/l)：</span></label>
							<input type="text" id="rjy" name="rjy" value="${(bo.rjy)!}"  class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'"  />
						</td>
						<td>
							<label class="LabName"><span>高锰酸盐指数(mg/l)：</span></label>
							<input type="text" id="mgzs" name="mgzs" value="${(bo.mgzs)!}"  class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'"  />
						</td>
						
						
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>氨氯(mg/l)：</span></label>
							<input type="text" id="al" name="al" value="${(bo.al)!}"  class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'"  />
						</td>
  						<td class="LeftTd"><label class="LabName"><span>检测时间：</span></label>
                        	<input id="endTime" name="endTime" type="text" data-options="tipPosition:'bottom',required:true" class="inp1 fl Wdate easyui-validatebox"  onclick="WdatePicker({isShowWeek:true,el:'endTime',dateFmt:'yyyy-MM-dd'})" value="<#if bo.endTime??>${bo.endTime?string('yyyy-MM-dd')}</#if>"/>
                        </td>
                    </tr>
                    <tr>
                         <td  class="LeftTd" colspan="2">
                            <label class="LabName"><span>地理标注：</span></label>
                        	<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                        </td>
                    </tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
    $(function(){
    	AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value=items[0].orgCode;
            }
        }, {
            rootName: "所属区域",
            ChooseType : '1',
            isShowPoorIcon: "1",
            ShowOptions : { GridShowToLevel : 4,EnableToolbar : true},
            OnCleared:function(){
                document.getElementById('orgCode').value='';
            }
        });
		
	   AnoleApi.initTreeComboBox("szlbName", null, '${ZG_WATER_TYPE!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('szlb').value = items[0].value;
            }
        }, ["${(bo.szlb)!}"], {ChooseType: '1', ShowOptions: {EnableToolbar: true}});
    });
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			
			var longitude=$("#longitude").val();
			var dimensions=$("#dimensions").val();
			if(longitude=='' || dimensions==''){
				$.messager.alert('提示', "经纬度不能为空，请标注地理位置！", 'info');
				return;
			}
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/water/save.jhtml',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (!data.success) {
						$.messager.alert('错误', data.tipMsg, 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
					}
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
		}
	}
	
	function showMap() {
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = $(window).width() - 15;
		var height = $(window).height() - 15;
		var gridId =  "";
		var markerOperation = $('#markerOperation').val();
		var mapType = $("#module").val();
		var isEdit = true;
		 var id = $('#id').val();
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	

	function getParameterJson() {
		var parameterJson = {
			"id": $("#id").val(),
			"name": $("#name").val()
		}
		return parameterJson;
	}
	
	//默认的回调函数，定位数据回调赋值可根据各个页面参数名称不同进行调整
	function mapMarkerSelectorCallback(mapt, x, y){//接收标注选项的回调函数（名称固定）
	    $("#mapt").val(mapt);//地图类型
	    $("#x").val(x);
	    $("#y").val(y);
	    $("#longitude").val(x);
	    $("#dimensions").val(y);
	    $("#mapTab2").html("修改地理位置");
	    closeMaxJqueryWindowForCross();
	    
	  
	}
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
