<style type="text/css">
	.r_elist{height:30px;line-height:30px; font-size:12px;padding-top:10px;padding-left:100px;border-bottom:1px #ccc dashed;text-align:center;}
</style>

<script type="text/javascript">
	var myPageSize = 10;
	//列表分页设置
	function listPage(id) {
		if (typeof id == "undefined") id = "list";
		var p = $('#' + id).datagrid('getPager');
		$(p).pagination({
			pageList : [ 10 ], //可以设置每页记录条数的列表
			beforePageText : '第', //页数文本框前显示的汉字
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
	}
	
	//列表加载成功时
	function listSuccess(data, id) {
		if (typeof id == "undefined") id = "list";
		$('#' + id).datagrid('clearSelections');	//清除掉列表选中记录
		if (data.total == 0) {
		    var noDataImg=$('#' + id).parent().find("#noDataImg");
		    if(noDataImg.length==0){
		        $('#' + id).parent().eq(0).append('<div id="noDataImg" style="text-align: center;padding-top:40px;"><img src="${uiDomain}/images/nodata.png" title="暂无数据"/></div>');
		    }
		}else{
		     var noDataImg=$('#' + id).parent().find("#noDataImg");
		     if(noDataImg.length>0){
		        noDataImg.remove();
		     }
		}
	}
	
	//列表加载失败时
	function listError(id) {
		if (typeof id == "undefined") id = "list";
		var errorDiv = $('#' + id).parent().find(".r_elist");
	    if (errorDiv.length > 0) {
	        errorDiv.remove();
	    }
	    $('#' + id).parent().eq(0).append('<div class="r_elist">数据加载出错</div>');
	}
	
	//列标题展示
	function titleFormatter(value, rec) {
		var title = '';
		
		if(value) {
			title = '<span title="' + value + '">' + value + '</span>';
		}
		
		return title;
	}
</script>