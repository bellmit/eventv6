
function easyuiGrid(options, param){
	if (typeof options == 'string'){
		$(options||"#list_data").datagrid('reload',param);
	} else {
		var config = {
			iconCls:'icon-edit',//图标
			nowrap:false,
			striped:true,
			fit: true,//自动大小
			method:'post',
			toolbar:'#tb',
			fitColumns:true,
			singleSelect:true,
			pagination:true,
			rownumbers:true
		};

		$(options.tableId||"#list_data").datagrid($.extend(true, {},config,options)).datagrid('getPager').pagination({  
	        pageSize: 10,//每页显示的记录条数，默认为10  
	        pageList: [10,20,50],//可以设置每页记录条数的列表  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    });
	}

	
}
(function($){
	$.fn.egrid = function(options, param){
		if (typeof options == 'string'){
			$(this).datagrid(options, param);
		} else {
			var config = {
				iconCls:'icon-edit',//图标
				nowrap:false,
				striped:true,
				fit: true,//自动大小
				method:'post',
				toolbar:'#tb',
				fitColumns:true,
				singleSelect:true,
				pagination:true,
				rownumbers:true
			};
	
			$(this).datagrid($.extend(true, {},config,options)).datagrid('getPager').pagination({  
		        pageSize: 10,//每页显示的记录条数，默认为10  
		        pageList: [10,20,50],//可以设置每页记录条数的列表  
		        beforePageText: '第',//页数文本框前显示的汉字  
		        afterPageText: '页    共 {pages} 页',  
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		    });
		}
	
		
	}
	
})(jQuery);


function openDialog(options){
	//默认配置
	var config = {
		width:600,
		height:330,
		collapsible:false,
		modal:true  
	}
	$(options.frameId||'#contentFrame').attr('src', options.url);
	$(options.winId||'#win').window($.extend(true, {},config,options));  
	
}


/**关闭弹出层**/
function closeDialog(options){
	$(options.tableId||"#list_data").datagrid('reload');
	$(options.winId||'#win').window('close'); 
}