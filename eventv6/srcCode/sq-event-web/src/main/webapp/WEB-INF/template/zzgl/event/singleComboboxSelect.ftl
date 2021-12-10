<script type="text/javascript">
function getSmallType(bigType,operateType){
	if(bigType != null && bigType!=''){
		$.ajax({
		    url:"${rc.getContextPath()}/zzgl/event/requiredEvent/getSmallType.json?bigType="+bigType+"&operateType="+operateType,
		    data:null,
		    type:'POST',
		    dataType:'json',
		    success:function(eventTypeJson){
		    	$("#type").empty();
		    	var eventTypeJson = eval(eventTypeJson);
		    	var option = $("<option>").val("").text("==请选择==");
			    $("#type").append(option);
	    		for(var i=1;i<eventTypeJson.length;i++){
	    			var option = $("<option>").val(eventTypeJson[i].value).text(eventTypeJson[i].text);
	    			$("#type").append(option);
	    		}
		 	},
			error: function(){
				var option = $("<option>").val("").text("==请选择==");
		    	$("#type").append(option);
			}
	    });
	}else{
	}
}
</script>