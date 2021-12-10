<!DOCTYPE html>
 <html>
<head>
    <meta charset="UTF-8" />
    <title>选择器</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

	<script type="text/javascript">
	var base = "${base}";
	var oa = oa || {};
	oa.base = base;
	//document.domain = "${crossDomain}";
	//oa.crossDomain = "${crossDomain}";
	</script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/customBtn.css" />
   	<link rel="stylesheet" href="${rc.getContextPath()}/ace/20180118/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${rc.getContextPath()}/ace/20180118/font-awesome/4.5.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="${rc.getContextPath()}/ace/20180118/css/ace.min.css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/singlePerson.css"/>
	
	<style type="text/css">
	body{background-color:#fff;}
	.item-selected{font-weight:bold;}
	.btn-default,.font-cz{color:#6f80a1;}
	.btn-sure,.modal-footer .btn-primary{background-color: #29e !important;color:#fff !important;border-color: #357ebd !important;}
	.data-name{display:none;} 
	.data-code{display:none;} 
	.data-id{display:none;} 
	.data-leaf{display:none;} 
	.data-type{display:none;} 
	.tree-branch-header{width:70%!important;}
	.color_0{background-color: #66bbff;}
	.color_1{background-color: #aa99ff;}
	.color_2{background-color: #aadd55;}
	.color_3{background-color: #ccc;}
	
	/* Let's get this party started */
	::-webkit-scrollbar {
	    width: 12px;
	}
	 
	/* Track */
	::-webkit-scrollbar-track {
	    -webkit-box-shadow: inset 0 0 6px rgba(136,135,135,0.65); 
	    -webkit-border-radius: 10px;
	    border-radius: 10px;
	}
	 
	/* Handle */
	::-webkit-scrollbar-thumb {
	    -webkit-border-radius: 10px;
	    border-radius: 10px;
	    background: rgba(136,135,135,0.65); 
	    -webkit-box-shadow: inset 0 0 6px rgba(136,135,135,0.65); 
	}
	::-webkit-scrollbar-thumb:window-inactive {
		background: rgba(136,135,135,0.65); 
	}
	</style>
</head>
<body>
<div class="tips_box"></div>
<input type="hidden" id="selectedIds" value="${selectedIds!''}">
<div class="row" style="margin:10px 0;">
	  <div class="col-xs-7" style="width:50%;height:300px;overflow:auto;border:1px solid #e4e4e4;margin-left:20px;">
		<div class="widget-body" style="margin-left:-20px;">
			<div class="widget-main padding-8">
					<ul id="aceTreeId" class="tree tree-unselectable tree-folder-select" role="tree">
						<div style="margin-top:35%;margin-left:25%;" class="tree-loader" role="alert">
							<div class="tree-loading">
								<i class="ace-icon fa fa-refresh fa-spin blue"></i>数据初始化中...
							</div>
						</div>
					</ul>
				</div>
		</div>
	</div>
	<div class="col-xs-5">
		<div id="tree_party_clear" style="border-radius:5px 5px 0 0;color:#5F6E8D;vertical-align:middle; padding:1px;padding-right:10px; display:inline-block;text-align:right; background-color: #F5F5F5;width: 100%;height:25px;line-height: 25px;font-weight: bold;cursor:pointer;">
			清空<span class="glyphicon glyphicon-trash" style="vertical-align:middle;"></span>
		</div>
		<div class="singal-select-tree" style="height:275px;overflow:auto;">
				<ul class="dept-tree-children" id="tree_party_show">
					
				</ul>
			</div>
	</div>
</div>
<form class="form-horizontal" style="margin-top:10px;">
	<div class="form-group" style="display:block;margin:auto;text-align:center;width:200px;">
		<button type="button" class="zbtn-white" onclick="closeCurrentWindow();">关闭</button>
	    <button type="button" class="zbtn-white btn-sure" onclick="confirm();">确定 </button>
    </div>
</form>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ace.tree.extend.js?t=${ct}"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/commonTools.js"></script>
<script type="text/javascript" src=""></script>
<script>
var idsSeleted = '${selectedIds!''}';
var idSelectArray = [];
if(idsSeleted){
	idSelectArray = idsSeleted.split(',');
	//console.log(idSelectArray);
}
idsSeleted = idsSeleted + ',';

function closeCurrentWindow(){
	parent.closeMaxJqueryWindow();
}

function clickCallback(eleObj,node){
}

function beforeExpand(eleObj,node){
}

$(function(){
	
	$('#aceTreeId').aceTreeInit({
		initUrl : '${rc.getContextPath()}/web/aceTree/initTree.jhtml?t='+new Date().getTime(),
		synUrl : '${rc.getContextPath()}/web/aceTree/getChildrenData.jhtml?t='+new Date().getTime(),
		showTreeId : '#tree_party_show',
		'chooseType' : '${chooseType}',
		'showToLevel' : '${showToLevel}',
		//'openIcon' : 'icon-folder red ace-icon fa fa-sitemap',
		//'closeIcon' : 'icon-folder red ace-icon fa fa-sitemap',
		'itemSelect' : true,
		'folderSelect': '${folderSelect}',
		'multiSelect': 'false',
		'selectedIcon' : 'ace-icon fa fa-check',
		'unselectedIcon' : 'ace-icon fa fa-times',
		'folderOpenIcon' : 'icon-caret ace-icon tree-plus',
		'folderCloseIcon' : 'icon-caret ace-icon tree-minus',
		'initCallback' : function(ele){
			initData(ele);
		},
		'afterExpand' : function(ele){
			initData(ele);
		},
		clickCallback : clickCallback	//点击回调方法，必填
	}); 
	
});

var len = 0;
function initData(ele){
	if(idsSeleted){
		var ids = idsSeleted.split(',');
		for(var i = 0,len = ids.length; i< len ;i++ ){
			
			var id = ids[i];
			if(!id) continue;
			
			var $li = ele.find('li[data-id="'+id+'"]');
			var liNum = $('li[data-id="'+id+'"]').length;
			if(liNum > 1){
				idsSeleted = idsSeleted.replace( (id+',') ,'')
				$li.addClass('tree-selected');
				if($li.hasClass('tree-item')){
					$li.css('font-weight','bold');
				}
			}
		}
		//console.log(idsSeleted);
	}
}

function confirm(){
	var ids = [];
	var names = [];
	var info = "[";
	$('li[data-id]',$('#tree_party_show')).each(function(){
		ids.push($(this).find('.data-id').text());
		//names.push($(this).find('.data-name').text());
		//orgCode.push($(this).find('.data-code').text());
		info += "{\"ldName\":\"" + $(this).find('.data-name').text() + "\",\"orgCode\":\"" + $(this).find('.data-code').text() + "\",\"ldType\":\"${ldType!}\"},"
	});
	info = info.substring(0,info.length - 1);
	info += "]";
	if(!ids.length){
		alert('没有选中任何数据');
		return;
	}
	
	$.ajax({
		type: 'POST',
		url: '${rc.getContextPath()}/zhsq/ldOrg/addLdOrg.json',
		data: {
			info:info
		},
		cache: false,
		dataType: 'json',
		success: function(data) {
			if (data.result == 'fail') {
				alert('添加失败！');
			}  else {
				alert('添加成功！');
				parent.refreash();
			}
			parent.closeMaxJqueryWindow();
		},
		error: function(data) {
			alert('连接超时！');
			parent.closeMaxJqueryWindow();
		}
	});
	
	
}

</script>
</body>
</html>