<script type="text/javascript">
function getSmallType(bigType,operateType){
	if(bigType != null && bigType!=''){
		$.ajax({
		    url:"${rc.getContextPath()}/zhsq/event/eventDisposalController/getSmallType.json?bigType="+bigType+"&operateType="+operateType,
		    data:null,
		    type:'POST',
		    dataType:'json',
		    success:function(eventTypeJson){
		    	$('#type').combobox('clear');
		    	var eventTypeJson = eval(eventTypeJson);
		    	$('#type').combobox("loadData",eventTypeJson);
		    	//设置默认值
		    	var data = $('#type').combobox('getData');
		    	<#if event??>
			    	<#if event.type??>
			    		for(var i in data){//根据具体的type来设置默认值
			    			if('${event.type}'==data[i].value){$('#type').combobox('select',data[i].value);}
			    			else{$('#type').combobox('select',data[0].value);}
			    		}
			    	<#else>
			    		$('#type').combobox('select',data[0].value);
			    	</#if>
		    	<#else>
		    		$('#type').combobox('select',data[0].value);
		    	</#if>
		 	},
			error: function(){
			}
	    });
	    if(bigType=='07'){
			$('#type').combobox({
				panelHeight:'243',
				width:'130'
			});
		}else if(bigType=='12'){
			$('#type').combobox({
				panelHeight:'auto',
				width:'160'
			});
		}else if(bigType=='16'){
			$('#type').combobox({
				panelHeight:'auto',
				width:'160'
			});
		}else{
			$('#type').combobox({
				panelHeight:'auto',
				width:'130'
			});
		}
	}else{
		$('#type').combobox('clear');
		var data = [{"value":"","text":"==请选择=="}];
		$('#type').combobox('loadData',data);
		var data = $('#type').combobox('getData');
	}
}
</script>