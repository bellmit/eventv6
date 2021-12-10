<script type="text/javascript">
	function clickFormatter(value, rec, rowIndex) {
		var title = "";
		
		if(value) {
			var instanceId = rec.instanceId || '',
				showContent = '',
				superviseMark = rec.superviseMark || '0';
			
			if(superviseMark == '1') {
				showContent = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
			}
			
			title = showContent + '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.reportUUID + '\', \'' + instanceId + '\', \'' + $('#listType').val() + '\')">'+ value +'</a>';
		}
		
		return title;
	}
	
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		
		return value;
	}
	
	function curUserFormatter(value, rowData, rowIndex) {
		var title = '';
		
		if(value) {
			var curOrgName = rowData.curOrgName || '';
			
			if(curOrgName) {
				value += '(' + curOrgName + ')';
			}
			
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function hourFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 13) {
			value = value.substring(0, 13) + '时';
		}
		
		return value;
	}
</script>