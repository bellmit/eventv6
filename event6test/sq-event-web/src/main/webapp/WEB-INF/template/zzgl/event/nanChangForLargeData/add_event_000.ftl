<@override name="eventAddPageTitle">
	江西省南昌市大数据平台新增页面
</@override>

<@override name="geographicalLabelingInput">
    <tr>
	  <td class="LeftTd"><label class="LabName"><span>地理位置：</span></label>
		  <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
	      <input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
	  </td>
	  <td>
		  <label class="LabName"><span>责任点位：</span></label>
		  <input type="hidden" id="pointSelection" name="pointSelection" value="<#if pointSelection??>${pointSelection}</#if>">
		  <input type="text" class="easyui-validatebox" style="height:30px;width:112px;" id="pointSelectionName" data-options="required:true,tipPosition:'bottom',prompt:'请选择'" editable="false"  value="<#if pointSelectionName??>${pointSelectionName}</#if>" />
	  </td>
    </tr>
</@override>

<@override name="happenTimeInput">
<tr>
    <td class="LeftTd">
		<label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
	</td>
	<td>
	<label class="LabName"><span>巡防类型：</span></label>
	<input type="hidden" id="patrolType" name="patrolType" <#if patrolType??>value="${patrolType}"</#if> />
	<input type="text" class="inp1 easyui-validatebox" style="width:123px" data-options="required:true,tipPosition:'bottom'" id="patrolTypeName" value="" />
	</td>
</tr>
</@override>

<@override name="singleLineExtraInfoTr">
<tr>
	<td>
		<label class="LabName"><span><label class="Asterik">*</label>发生类型：</span></label>
		<input type="hidden" id="eventLabelIds" name="eventLabelIds" value="${eventLabelIds!}" />
		<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="eventLabelName" value="${eventLabelName!}" />
	</td>
</tr>
</@override>

<@override name="contactUserTr">
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span id="contactUserLabelSpan"><label class="Asterik">*</label>联系人员：</span></label>
			<input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="<#if event.contactUser??>${event.contactUser}</#if>" />
		</td>
	</tr>
</@override>
<@override name="contactTelTr">
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span id="contactTelLabelSpan"><label class="Asterik">*</label>联系电话：</span></label>
			<input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="<#if event.tel??>${event.tel}</#if>" />
		</td>
	</tr>
</@override>

<@override name="closeButtonBlock"></@override>

<@override name="initExpandScript">
	   AnoleApi.initTreeComboBox("patrolTypeName", "patrolType", "A001093092", null, [<#if patrolType??>"${patrolType}"<#else>"0"</#if>]);
	   $('#content').css('height',"110px");
	   var searchParams={x:0.0,y:0.0,limits:500};
	   
	   <#if eventLabelDict??>
		   AnoleApi.initListComboBox("eventLabelName", "eventLabelIds", null, ["${eventLabelIds!}"], null, {
		        DataSrc: [
		        	<#list eventLabelDict as item>
		        		{"name":"${item.labelName}", "value":"${item.labelId}"},
		        	</#list>
		        ],
		        ShowOptions:{
		            EnableToolbar : true
		        }
		    });
	    </#if>
	   
	   $('#pointSelectionName').combogrid({    
           panelWidth:400,    
           //idField:'pointId', 
           value:'<#if pointSelectionName??>${pointSelectionName}</#if>',   
           textField:'pointName', 
           rownumbers:true, 
           loadMsg: '正在加载责任点位信息',  
           url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/findResposibilityPoints.json',
           queryParams:searchParams,    
           columns:[[
               {field:'pointId',title:'pointId', align:'center',hidden:true},    
               {field:'pointName',title:'点位名称',align:'center',width:345}    
               //{field:'pointManager',title:'联系人',align:'center',width:140},    
               //{field:'pmTel',title:'电话',align:'center',width:140}  
           ]],
           onLoadSuccess:function(){
           	   $(".combo").css("width","128px");
           },
           onShowPanel:function(){
               x_=$("#x").val();
               y_=$("#y").val();
               //console.log(x_);
               //console.log(y_);
               if(x_==""||y_==""){
                   //console.log($("#gridId").val());
                   var grid_=$("#gridId").val();
                   $.ajax({
					   type: "POST",
					   async: false,
		    	   	   url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/findGridCenter.json',
					   data: 'gridId='+grid_,
					   dataType:"json",
					   success: function(data){
						   if(data.length>0){
						       searchParams.x=data[0].x;
						       searchParams.y=data[0].y;
						       $("#x").val(data[0].x);
						       $("#y").val(data[0].y);
						   }
					   }
		    	   });
               }else{
                   searchParams.x=x_;
                   searchParams.y=y_;
               }

               $('#pointSelectionName').combogrid('grid').datagrid('load');
           },
           onSelect: function (rowIndex, rowData){
               x_=$("#x").val();
               y_=$("#y").val();
               $.ajax({
					type: "POST",
					async: false,
		    	   	url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/searchResposibilityPointInfo.json',
					data: {'x':x_,'y':y_,'pointId':rowData.pointId},
				    dataType:"json",
					success: function(data){
					    //console.log(data);   
					    if(data.msg){
                            $.messager.alert('提示',data.msg+'请重新选择地理位置或手动选择所属网格!','info');
                        }else{
                        	if(isNotBlankString(data.gridCode)){
				            	$('#gridCode').val(data.gridCode); 
                        	}
                        	if(isNotBlankString(data.gridName)){
                        		$('#gridName').val(data.gridName);
                        	}
                        	if(isNotBlankString(data.gridId)){
                        		$("#gridId").val(data.gridId);
                        	}
                        } 
                        $('#contactUser').val(data.pointManager);
                        $('#tel').val(data.pmTel);
                        if(data.eventType!=null&&data.eventType!=''){
                        
                            eventTreeApi.setSelectedNodes([data.eventType]);
		                
		                }
					}
		       });
               $('#pointSelection').val(rowData.pointId);
               $('#pointSelectionName').val(rowData.pointName);
           }
       });
</@override>

<@override name="attachmentCheck">
	var comboFlag=$('#pointSelectionName').validatebox('isValid');
	if(!comboFlag){
		$('#pointSelectionName').next().find("input").eq(0).mouseover();
	}
</@override>

<@extends name="/zzgl/event/add_event_000.ftl" />