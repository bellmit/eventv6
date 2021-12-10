<@override name="gridTreeSearch">
	<li>所属网格：</li>
    <li>
	<input id="infoOrgCode" name="eInfoOrgCode" type="text" class="hide queryParam" value="${eInfoOrgCodeForOut!''}"/>
	<input id="gridId" name="gridId" type="text" class="hide queryParam" >
	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;" value="${gridNameForOut!''}" />
	</li>
</@override>


<@override name="gridTreeInit">
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
            <#if startGridId??>,startGridId : ${startGridId?c}</#if>
        });
</@override>

<@override name="treeStyleEvent"></@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />
