var defaultOptions = {
	'initUrl' : '',//初始化地址
	'synUrl' : '',//异步获取数据地址
	'showTreeId' : '',//显示树
	'openIcon' : 'icon-folder red ace-icon fa fa-folder-open',
	'closeIcon' : 'icon-folder red ace-icon fa fa-folder',
	'itemSelect' : true,//子项是否可选
	'folderSelect': false,//文档类是否可选
	'multiSelect': false,//是否多选
	'selectedIcon' : 'ace-icon fa fa-check',
	'unselectedIcon' : 'ace-icon fa fa-times',
	'chooseType' : '0',//数据类型0组织数据；1地域数据；2人员数据
	'showToLevel' : '6',//显示的最下层树层级
	'folderOpenIcon' : 'icon-caret ace-icon tree-plus',//文档栏打开样式
	'folderCloseIcon' : 'icon-caret ace-icon tree-minus',//文档栏关闭样式
	'initCallback' : null, //初始化完成后的回调事件
	'beforeExpand' : null,//点击展开的回调事件
	'afterExpand' : null,//展开后的回调事件
	'clickCallback' : null //选择点击回调方法
};

$.fn.extend({
	aceTreeInit : function(options){
		
	    $.extend(defaultOptions,options);
		  
		var self = $(this);
		var $selector = this.selector;
		
		var isInit = false;
		if(self.hasClass('init')){
			isInit = true;
		}
		
		var initUrl = defaultOptions.initUrl;
		if(!initUrl){
			alert('初始化请求地址不能为空');
			return;
		}
//		if(initUrl.indexOf('?')){
//			initUrl += '&chooseType='+defaultOptions.chooseType+'&showToLevel='+defaultOptions.showToLevel;
//		} else {
//			initUrl += '?chooseType='+defaultOptions.chooseType+'&showToLevel='+defaultOptions.showToLevel;
//		}
		
		var $showTreeObj = '';
		if(defaultOptions.showTreeId){
			$showTreeObj = $(defaultOptions.showTreeId);
		}
		
		var showTreeId = '';
		if(defaultOptions.showTreeId){
			showTreeId = defaultOptions.showTreeId;
		}
		
		var synUrl = defaultOptions.synUrl;
		if(!synUrl){
			alert('异步数据加载地址不能为空');
			return;
		}
//		if(synUrl.indexOf('?')){
//			synUrl += '&chooseType='+defaultOptions.chooseType+'&showToLevel='+defaultOptions.showToLevel;
//		} else {
//			synUrl += '?chooseType='+defaultOptions.chooseType+'&showToLevel='+defaultOptions.showToLevel;
//		}
		
		var clickCallback = defaultOptions.clickCallback;
		
		var id = "";
		if(defaultOptions.id && defaultOptions.id!='' && typeof(defaultOptions.id) != "undefined"){
			id = defaultOptions.id;
		}
		
		init = function(eleId,clickCallback,id,$showTreeObj){
			$('#tree_party_clear').on('click',function(){
				clearAllSelected($(this));
			});
			$.ajax({
				url : initUrl,
				data:{
					id : id,
					chooseType : defaultOptions.chooseType,
					showToLevel : defaultOptions.showToLevel
				},
				type:'post',
				beforeSend:function(){ },
				success:function(data){
					if(!data || !data.pData){
						alert('数据加载失败');
						return;
					}
					var html = '';
					html += '<li data-id="'+data.pData.id+'" class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
					html += '	<i onclick="openOrClose(this,\''+showTreeId+'\');" class="'+defaultOptions.folderCloseIcon+' init">';
					html += '		<span class="data-name">'+data.pData.name+'</span>';
					html += '		<span class="data-id">'+data.pData.id+'<span>';
					html += '		<span class="data-code">'+data.pData.code+'<span>';
					html += '	</i>&nbsp;';
					if(defaultOptions.folderSelect){
						html += '		<div onclick="optionSelect(this,\''+showTreeId+'\',1,\''+data.pData.type+'\');" class="tree-branch-header">';
					} else {
						html += '		<div class="tree-branch-header">';
					}
					
					html += '			<span class="data-id">'+data.pData.id+'</span>';
					html += '			<span class="data-name">'+data.pData.name+'</span>';
					html += '			<span class="data-code">'+data.pData.code+'</span>';
					html += '			<span class="tree-branch-name"> ';
					html += '				<i class="'+defaultOptions.openIcon+'"></i> ';
					html += '				<span class="tree-label">'+data.pData.name+'</span>';
				    html += '			</span>';
			    	html += '		</div>';
			    	for(var i = 0 ,len =  data.cData.length ; i < len ; i++){
			    		var tmp = data.cData[i];
			    		html += '	<ul class="tree-branch-children" role="group">';
			    		
			    		if(tmp.isLeaf){
			    			if(defaultOptions.itemSelect){
			    				html += '		<li data-id="'+tmp.id+'" onclick="optionSelect(this,\''+showTreeId+'\',2,\''+tmp.type+'\');" class="tree-item" role="treeitem">';
			    				html += '			<span class="data-id">'+tmp.id+'</span>';
								html += '			<span class="data-name">'+tmp.name+'</span>';
								html += '			<span class="data-code">'+tmp.code+'</span>';
			    			} else {
			    				html += '		<li class="tree-item" role="treeitem">';
			    			}
			    			
			    			html += '			<span class="tree-item-name"> ';
		    				html += '				<span class="tree-label">';
		    				if(tmp.type == '2'){//人员样式
		    					html += '				<i class="ace-icon fa fa-user red"></i> '+tmp.name;
		    				}else{
		    					html += '				<i class="ace-icon fa fa-file-text red"></i> '+tmp.name;
		    				}
							html += '				</span>';
							html += '			</span>';
							html += '		</li>';
			    		} else {
			    			html += '		<li data-id="'+tmp.id+'" class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
		    				html += '			<i onclick="openOrClose(this,\''+showTreeId+'\');" class="'+defaultOptions.folderOpenIcon+'">';
		    				html += '				<span class="data-name">'+tmp.name+'</span>';
							html += '				<span class="data-id">'+tmp.id+'<span>';
							html += '				<span class="data-code">'+tmp.code+'<span>';
							html += '			</i>&nbsp;';
							
							if(defaultOptions.folderSelect){
								html += '			<div onclick="optionSelect(this,\''+showTreeId+'\',1,\''+tmp.type+'\');" class="tree-branch-header">';
							} else {
								html += '			<div class="tree-branch-header">';
							}
							
							html += '				<span class="data-id">'+tmp.id+'</span>';
							html += '				<span class="data-name">'+tmp.name+'</span>';
							html += '				<span class="data-code">'+tmp.code+'</span>';
							html += '				<span class="tree-branch-name">'; 
							html += '					<i class="'+defaultOptions.closeIcon+'"></i> ';
							html += '					<span class="tree-label">'+tmp.name+'</span>';
							html += '				</span>';
							
							html += '			</div>';
							html += '		<ul class="tree-branch-children" role="group"></ul>';
							html += '		</li>';
			    		}
					
						html += '	</ul>';
			    	}
					html += '</li>';
					self.html(html);
					if(typeof(defaultOptions.initCallback) == 'function'){
						defaultOptions.initCallback(self);
					}
					self.addClass('init');
				},
				error:function(){
					alert("初始化数据失败");
				},
				complete:function(){ }
			});
		};
		
		ajaxData = function(eleObj,pId,$showTreeObj){
			$.ajax({
				url : synUrl,
				data:{
					pId : pId,
					chooseType : defaultOptions.chooseType,
					showToLevel : defaultOptions.showToLevel
				},
				type:'post',
				beforeSend:function(){ },
				success:function(data){
					if(data.length){
						var html = '';
				    	for(var i = 0 ,len =  data.length ; i < len ; i++){
				    		var tmp = data[i];
				    		
				    		if(tmp.isLeaf){
				    			
				    			if(defaultOptions.itemSelect){
				    				html += '		<li data-id="'+tmp.id+'" onclick="optionSelect(this,\''+showTreeId+'\',2,\''+tmp.type+'\');" class="tree-item" role="treeitem">';
				    				html += '			<span class="data-id">'+tmp.id+'</span>';
									html += '			<span class="data-name">'+tmp.name+'</span>';
									html += '			<span class="data-code">'+tmp.code+'</span>';
				    			} else {
				    				html += '		<li class="tree-item" role="treeitem">';
				    			}
				    			
				    			html += '			<span class="tree-item-name"> ';
			    				html += '				<span class="tree-label">';
			    				if(tmp.type == '2'){
			    					html += '					<i class="ace-icon fa fa-user red"></i> '+tmp.name;
			    				} else {
			    					html += '					<i class="ace-icon fa fa-file-text red"></i> '+tmp.name;
			    				}
		    					
								html += '				</span>';
								html += '			</span>';
								html += '		</li>';
				    		} else {
				    			html += '		<li data-id="'+tmp.id+'" class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
			    				html += '			<i onclick="openOrClose(this,\''+showTreeId+'\');" class="'+defaultOptions.folderOpenIcon+'">';
			    				html += '				<span class="data-name">'+tmp.name+'</span>';
			    				html += '				<span class="data-code">'+tmp.code+'</span>';
								html += '				<span class="data-id">'+tmp.id+'<span>';
								html += '			</i>&nbsp;';
								
								if(defaultOptions.folderSelect){
									html += '			<div onclick="optionSelect(this,\''+showTreeId+'\',1,\''+tmp.type+'\');" class="tree-branch-header">';
								} else {
									html += '			<div class="tree-branch-header">';
								}
								
								html += '				<span class="data-id">'+tmp.id+'</span>';
								html += '				<span class="data-code">'+tmp.code+'</span>';
								html += '				<span class="data-name">'+tmp.name+'</span>';
								html += '				<span class="tree-branch-name">'; 
								html += '					<i class="'+defaultOptions.closeIcon+'"></i> ';
								html += '					<span class="tree-label">'+tmp.name+'</span>';
								html += '				</span>';
								html += '			</div>';
								html += '		<ul class="tree-branch-children" role="group"></ul>';
								html += '		</li>';
				    		}
				    		
				    	}
				    	
				    	eleObj.nextAll('ul.tree-branch-children').first().html(html);
						
					}else{
						eleObj.nextAll('ul.tree-branch-children').first().html('');
					}
					if(typeof(defaultOptions.afterExpand) == 'function'){
						defaultOptions.afterExpand(eleObj.parent('li'));
					}
					
			    	eleObj.addClass('init');
			    	eleObj.removeClass('tree-plus');
			    	eleObj.addClass('tree-minus');
			    	eleObj.next().find('i.icon-folder').removeClass('fa-folder');
			    	eleObj.next().find('i.icon-folder').addClass('fa-folder-open');
				},
				error:function(){
					alert("请求失败");
					eleObj.nextAll('ul.tree-branch-children').first().html('');
				},
				complete:function(){ }
			});
		};
		
		if(!isInit){
			init($selector,clickCallback,id,$showTreeObj);
		}
	}
});

function openOrClose(node,showTreeId){
	var $showTreeObj = $(showTreeId);
	var $currNode = $(node);
	$currNode.nextAll('ul').toggle();
	if($currNode.hasClass('tree-minus')){//待收起的样式
		$currNode.removeClass('tree-minus').addClass('tree-plus');
		$currNode.next().find('i.icon-folder').removeClass('fa-folder-open').addClass('fa-folder');
	} else {//带展开的样式
		if(!$currNode.hasClass('init')){
			var loadingText = '';
			loadingText += '<div class="tree-loader" role="alert">';
			loadingText += '	<div class="tree-loading">';
			loadingText += '		<i class="ace-icon fa fa-refresh fa-spin blue"></i>加载中...';
			loadingText += '	</div>';
			loadingText += '</div>';
			$currNode.nextAll('ul.tree-branch-children').show().first().html(loadingText);
			ajaxData($currNode,$('.data-id',$currNode).text(),$showTreeObj);
		} else {
			$currNode.removeClass('tree-plus').addClass('tree-minus');
			$currNode.next().find('i.icon-folder').removeClass('fa-folder').addClass('fa-folder-open');
		}
	}
}

function optionSelect(node,showTreeId,operType,objType){
	
	var $showTreeObj = $(showTreeId);
	var parentObj = operType=='1'?$(node).parent():$(node);
	
	var name = $('.data-name',$(node)).text();
	var id = $('.data-id',$(node)).text();
	var code = $('.data-code',$(node)).text();
	var isSelected = false;
	
	if(defaultOptions.multiSelect){//多选
		isSelected = mulitSelect(parentObj,$showTreeObj,id,code,name,objType);
	} else {
		isSelected = singleSelect(parentObj,$showTreeObj,id,code,name,objType);
	}
	
	if(typeof(defaultOptions.clickCallback) == 'function'){
		var nodeVo = {
			id : id,
			name : name,
			type : objType,
			selected : isSelected
		};
		defaultOptions.clickCallback($(node),nodeVo);
	}
}

function mulitSelect(parentObj,$showTreeObj,id,code,name,objType){
	var isSelected = true;
	if(parentObj.hasClass('tree-selected')){
		parentObj.removeClass('tree-selected');
		if(parentObj.hasClass('tree-item'))
			parentObj.css({'font-weight':'normal'});
		isSelected = false;
		var $showLiObj = $('li[data-id="'+id+'"]',$showTreeObj);
		resetSelected($showLiObj);
		$showLiObj.remove();
	} else {
		parentObj.addClass('tree-selected');
		if(parentObj.hasClass('tree-item'))
			parentObj.css({'font-weight':'bold'});
		isSelected = true;
		var numSelected = $showTreeObj.find('li').length;
		if(!numSelected){
			numSelected = 1;
		}else {
			numSelected = parseInt(numSelected)+1;
		}
		var selectedHtml = '';
		selectedHtml += '<li title="'+name+'" ondblclick="removeSelected(this,\''+id+'\')" data-id="'+id+'" data-order="'+numSelected+'" class="dept-tree-leaf dept-user zk-user">';
		selectedHtml += '	<span style="display:none;" class="data-id">'+id+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-name">'+name+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-code">'+code+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-type">'+objType+'</span>';
		selectedHtml += '	<div class="contact-photo">';
		
		var color = '#66bbff';
		switch(numSelected%4) {
			case 0:
				color = '#66bbff';
			  break;
			case 1:
				color = '#aa99ff';
			  break;
			case 2:
				color = '#aadd55';
			  break;
			case 3:
				color = '#ccc';
			  break;
		}
		selectedHtml += '		<div class="avatar" style="background-color: '+color+'">'+numSelected+'</div>';
		selectedHtml += '		<span class="contact-name"> '+name+'</span>';
		selectedHtml += '	</div>';
		selectedHtml += '</li>';
		
		if(numSelected > 1)
			$showTreeObj.find('li').last().after(selectedHtml);
		else
			$showTreeObj.html(selectedHtml);
		
	}
	return isSelected;
}

function singleSelect(parentObj,$showTreeObj,id,code,name,objType){
	var isSelected = true;
	if(parentObj.hasClass('tree-selected')){
		parentObj.removeClass('tree-selected').css({'font-weight':'normal'});
		isSelected = false;
		var $showLiObj = $('li[data-id="'+id+'"]',$showTreeObj);
		resetSelected($showLiObj);
		$showLiObj.remove();
	} else {
		$('li.tree-selected').removeClass('tree-selected').css({'font-weight':'normal'});
		parentObj.addClass('tree-selected').css({'font-weight':'bold'});
		var selectedHtml = '';
		selectedHtml += '<li title="'+name+'" ondblclick="removeSelected(this,\''+id+'\')" data-id="'+id+'" data-order="2" class="dept-tree-leaf dept-user zk-user">';
		selectedHtml += '	<span style="display:none;" class="data-id">'+id+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-code">'+code+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-name">'+name+'</span>';
		selectedHtml += '	<span style="display:none;" class="data-type">'+objType+'</span>';
		selectedHtml += '	<div class="contact-photo">';
		selectedHtml += '		<div class="avatar" style="background-color: #66bbff">1</div>';
		selectedHtml += '		<span class="contact-name"> '+name+'</span>';
		selectedHtml += '	</div>';
		selectedHtml += '</li>';
		$showTreeObj.html(selectedHtml);
		
	}
	return isSelected;
}

function removeSelected(node,id){
	if(typeof(defaultOptions.clickCallback) == 'function'){
		var nodeVo = {
			id : $(node).find('.data-id').text(),
			name : $(node).find('.data-name').text(),
			code : $(node).find('.data-code').text(),
			type : $(node).find('.data-type').text(),
			selected : false
		};
		defaultOptions.clickCallback($(node),nodeVo);
	}
	resetSelected($(node));
	$(node).remove();
	$('li[data-id="'+id+'"]').removeClass('tree-selected').css('font-weight','normal');
}

function clearAllSelected(){
	
	$($('li[data-order]'),$(defaultOptions.showTreeId)).each(function(){
		var id = $(this).attr('data-id');
		$(this).remove();
		$('li[data-id="'+id+'"]').removeClass('tree-selected').css('font-weight','normal');
	});
}

function resetSelected(eleObj){
	var currOrder = parseInt(eleObj.attr('data-order'));
	eleObj.nextAll('li').each(function(){
		var order_ = parseInt($(this).attr('data-order'));
		var nextOrder = parseInt(order_) - 1;
		if(order_ > currOrder){
			var color = '#66bbff';
			switch(nextOrder%4) {
				case 0:
					color = '#66bbff';
				  break;
				case 1:
					color = '#aa99ff';
				  break;
				case 2:
					color = '#aadd55';
				  break;
				case 3:
					color = '#ccc';
				  break;
			}
			$(this).attr('data-order',nextOrder);
			$(this).find('div.avatar').text(nextOrder).css('background-color',color);
		}
	});
}