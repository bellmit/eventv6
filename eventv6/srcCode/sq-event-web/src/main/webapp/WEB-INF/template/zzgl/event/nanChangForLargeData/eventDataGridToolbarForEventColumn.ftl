<@override name="extendCondition">
		<#if eventType?? && eventType != 'draft' && eventType != 'myhistory' && eventType != 'view' && eventType != 'donehistory'>
		    <tr>
		        <td>
		            <label class="LabName width65px"><span>巡访类型：</span></label>
		            <input type="text" id="patrolType" name="patrolType" class="hide queryParam"/>
		            <input type="text" id="patrolTypeName" class="inp1 selectWidth" />
		        </td>
		    </tr>
		    <tr>
		        <td>
		            <label class="LabName width65px"><span>平台来源：</span></label>
		            <input type="text" id="bizPlatform" name="bizPlatform" class="hide queryParam"/>
		            <input type="text" id="bizPlatformName" class="inp1 selectWidth" />
		        </td>
		    </tr>
		    <tr>
		        <td>
		            <label class="LabName width65px"><span>事件类别：</span></label>
		            <input type="text" id="eventClassification" name="eventClassification" class="hide queryParam"/>
		            <input type="text" id="eventClassificationName" class="inp1 selectWidth" />
		        </td>
		    </tr>
		    <tr>
		        <td>
		            <label class="LabName width65px"><span>发生类型：</span></label>
		            <input type="text" id="eventLabelIds" name="eventLabelIds" class="hide queryParam"/>
                    <input type="text" id="eventLabelStr" class="inp1 selectWidth" />
		        </td>
		    </tr>
		    <#if eventLabelDict??>
		    <tr>
		        <td>
		            <label class="LabName width65px"><span>发生类型：</span></label>
		            <input type="text" id="eventLabelIds" name="eventLabelIds" class="hide queryParam"/>
                    <input type="text" id="eventLabelStr" class="inp1 selectWidth" />
		        </td>
		    </tr>
		    </#if>
		</#if>
	</@override>
	<@override name="extendConditionInit">
		<#if eventType?? && eventType != 'draft' && eventType != 'myhistory' && eventType != 'view' && eventType != 'donehistory'>
	
	        var eventClassificationFlag=0;
			var eventClassificationClearFlag=0;
	
	        var patrolTypeComboBox= AnoleApi.initTreeComboBox("patrolTypeName", "patrolType", "A001093092", function(value){
	        	if(eventClassificationFlag>0){
	        		eventClassificationFlag=eventClassificationFlag-1;
	        	}else{
	        		eventClassificationComboBox.doClearing();
	        	}
	        	
	        }, null, {
	            RenderType : "01",
	            OnCleared : function(){
	            	if(eventClassificationClearFlag>0){
	        			eventClassificationClearFlag=eventClassificationClearFlag-1;;
	        		}else{
	        			eventClassificationComboBox.doClearing();
	        		}
	            },
	            ShowOptions:{
	                EnableToolbar : true
	            }
	        });
	        
	        var bizPlatformComboBox=AnoleApi.initTreeComboBox("bizPlatformName", "bizPlatform", "B6027",  function(value){
	        	
	        	if(eventClassificationFlag>0){
	        		eventClassificationFlag=eventClassificationFlag-1;
	        	}else{
	        		eventClassificationComboBox.doClearing();
	        	}
	        }, null,{
	            RenderType : "01",
	            OnCleared : function(){
	        		if(eventClassificationClearFlag>0){
	        			eventClassificationClearFlag=eventClassificationClearFlag-1;;
	        		}else{
	        			eventClassificationComboBox.doClearing();
	        		}
	            },
	            ShowOptions:{
	                EnableToolbar : true
	            }
	        });
	        
	        var eventClassificationComboBox= AnoleApi.initListComboBox("eventClassificationName", "eventClassification", "", function(value){
	        	if(value){
	        	
	        		var dataArr=value.split("|");
	        		if(dataArr[0]){
	        			eventClassificationClearFlag=eventClassificationClearFlag+1;
	        			patrolTypeComboBox.doClearing();
	        			if(dataArr[0]!="''"){
	        			
	        				eventClassificationFlag=eventClassificationFlag+1;
	        				var selectArr=dataArr[0].split(",");
	        				patrolTypeComboBox.setSelectedNodes(selectArr);
	        			}
	        		}
	        		
	        		if(dataArr[1]){
	        			eventClassificationClearFlag=eventClassificationClearFlag+1;
	        			bizPlatformComboBox.doClearing();
	        			if(dataArr[1]!="''"){
	        			
	        				eventClassificationFlag=eventClassificationFlag+1;
	        				var selectArr=dataArr[1].split(",");
	        				bizPlatformComboBox.setSelectedNodes(selectArr);
	        			
	        			}
	        		}
	        	}
	        },null, {
            	ChooseType : "1" ,
            	DataSrc: [{"name":"巡防事件", "value":"1,2|0,3601001"},
                      {"name":"AI预警事件", "value":"''|3601002"},
                      {"name":"督办事件", "value":"0|0"}
                     	],
            	ShowOptions:{
            		EnableToolbar : true
            	}
        	});
        	
        	
        <#if eventLabelDict??>	
	        AnoleApi.initListComboBox("eventLabelStr", "eventLabelIds", null, null, null, {
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
	        
	</#if>
	</@override>
	
	
<@extends name="/zzgl/event/eventDataGridToolbarForColumn.ftl" />