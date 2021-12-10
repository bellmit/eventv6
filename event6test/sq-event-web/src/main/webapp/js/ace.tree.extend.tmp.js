$.fn.extend({
	  aceTreeInit : function(obj){
		
		var self = $(this);
		var $selector = this.selector;
		
		var isInit = false;
		if(self.hasClass('init')){
			isInit = true;
		}
		
		var initUrl = obj.initUrl;
		if(!initUrl){
			alert('初始化请求地址不能为空');
			return;
		}
		
		
		var $showTreeObj = '';
		if(obj.showTreeId){
			$showTreeObj = $(obj.showTreeId);
		}
		
		var synUrl = obj.synUrl;
		
		var clickCallback = obj.clickCallback;
		
		var orgId = "";
		if(obj.orgId && obj.orgId!='' && typeof(obj.orgId) != "undefined"){
			orgId = obj.orgId;
		}
		
		init = function(eleId,clickCallback,orgId){
			$.ajax({
				url : initUrl,
				data:{
					orgId : orgId
				},
				type:'post',
				beforeSend:function(){ },
				success:function(data){
					if(!data.pData){
						alert('数据加载失败');
						return;
					}
					var html = '';
					html += '<li class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
					html += '	<i class="icon-caret ace-icon tree-minus" data-orgname="'+data.pData.orgName+'" data-orgid="'+data.pData.orgId+'"></i>&nbsp;';
					html += '		<div class="tree-branch-header" data-orgname="'+data.pData.orgName+'" data-orgid="'+data.pData.orgId+'">';
					html += '			<span class="tree-branch-name"> ';
					html += '				<i class="icon-folder red ace-icon fa fa-folder-open"></i> ';
					html += '				<span class="tree-label">'+data.pData.orgName+'</span>';
				    html += '			</span>';
			    	html += '		</div>';
			    	for(var i = 0 ,len =  data.cData.length ; i < len ; i++){
			    		var tmp = data.cData[i];
			    		html += '	<ul class="tree-branch-children" role="group">';
		    			html += '		<li class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
	    				html += '			<i class="icon-caret ace-icon tree-plus" data-orgname="'+tmp.orgName+'" data-orgid="'+tmp.orgId+'"></i>&nbsp;';
						html += '			<div class="tree-branch-header" data-orgname="'+tmp.orgName+'" data-orgid="'+tmp.orgId+'">';
						html += '				<span class="tree-branch-name">'; 
						html += '					<i class="icon-folder red ace-icon fa fa-folder"></i> ';
						html += '					<span class="tree-label">'+tmp.orgName+'</span>';
						html += '				</span>';
						html += '			</div>';
						html += '		<ul class="tree-branch-children" role="group"></ul>';
						html += '		</li>';
						html += '	</ul>';
			    	}
					html += '</li>';
					self.html(html);
					
					bindEvents(self);
					
					self.addClass('init');
				},
				error:function(){
					alert("初始化数据失败");
				},
				complete:function(){ }
			});
		};
		
		ajaxData = function(eleObj,pOrgId){
			$.ajax({
				url : synUrl,
				data:{
					pOrgId : pOrgId
				},
				type:'post',
				beforeSend:function(){ },
				success:function(data){
					if(data.length){
						var html = '';
				    	for(var i = 0 ,len =  data.length ; i < len ; i++){
				    		var tmp = data[i];
			    			html += '		<li class="tree-branch tree-open" role="treeitem" aria-expanded="true">';
		    				html += '			<i class="icon-caret ace-icon tree-plus" data-orgname="'+tmp.orgName+'" data-orgid="'+tmp.orgId+'"></i>&nbsp;';
							html += '			<div class="tree-branch-header" data-orgname="'+tmp.orgName+'" data-orgid="'+tmp.orgId+'">';
							html += '				<span class="tree-branch-name">'; 
							html += '					<i class="icon-folder red ace-icon fa fa-folder"></i> ';
							html += '					<span class="tree-label">'+tmp.orgName+'</span>';
							html += '				</span>';
							html += '			</div>';
							html += '		<ul class="tree-branch-children" role="group"></ul>';
							html += '		</li>';
				    	}
				    	
				    	eleObj.nextAll('ul.tree-branch-children').first().html(html);
						
				    	bindEvents(eleObj.nextAll('ul.tree-branch-children').first());
					}else{
						eleObj.nextAll('ul.tree-branch-children').first().html('');
					}
			    	eleObj.addClass('init');
				},
				error:function(){
					alert("请求失败");
				},
				complete:function(){ }
			});
		};
		
		if(!isInit){
			init($selector,clickCallback,orgId);
		}
		
		function bindEvents(pEleObj){
			$('.icon-caret.tree-minus').on('click',pEleObj,function(){
				$(this).nextAll('ul').toggle();
				
				if($(this).hasClass('tree-minus')){
					$(this).removeClass('tree-minus');
					$(this).addClass('tree-plus');
					$(this).next().find('i.icon-folder').removeClass('fa-folder-open');
					$(this).next().find('i.icon-folder').addClass('fa-folder');
					
				} else {
					$(this).removeClass('tree-plus');
					$(this).addClass('tree-minus');
					$(this).next().find('i.icon-folder').removeClass('fa-folder');
					$(this).next().find('i.icon-folder').addClass('fa-folder-open');
				}
			});
			
			$('.icon-caret.tree-plus').on('click',pEleObj,function(){
				$(this).nextAll('ul').toggle();

				if($(this).hasClass('tree-plus')){
					
					if(!$(this).hasClass('init')){
						var loadingText = '';
						loadingText += '<div class="tree-loader" role="alert">';
						loadingText += '	<div class="tree-loading">';
						loadingText += '		<i class="ace-icon fa fa-refresh fa-spin blue"></i>';
						loadingText += '	</div>';
						loadingText += '</div>';
						$(this).nextAll('ul.tree-branch-children').show().first().html(loadingText);
						ajaxData($(this),$(this).attr('data-orgid'));
					}
					$(this).removeClass('tree-plus');
					$(this).addClass('tree-minus');
					$(this).next().find('i.icon-folder').removeClass('fa-folder');
					$(this).next().find('i.icon-folder').addClass('fa-folder-open');
				} else {
					$(this).removeClass('tree-minus');
					$(this).addClass('tree-plus');
					$(this).next().find('i.icon-folder').removeClass('fa-folder-open');
					$(this).next().find('i.icon-folder').addClass('fa-folder');
				}
			});
			
			$('.tree-branch-header').on('click',function(){
				var orgName = $(this).attr('data-orgname');
				var orgId = $(this).attr('data-orgid');
				var parentObj = $(this).parent();
				if(parentObj.hasClass('tree-selected')){
					$('li[data-orgid="'+orgId+'"]',$showTreeObj).remove();
					parentObj.removeClass('tree-selected');
				} else {
					parentObj.addClass('tree-selected');
					
					var numSelected = $showTreeObj.find('li').length;
					if(!numSelected){
						numSelected = 1;
					}else {
						numSelected = parseInt(numSelected)+1;
					}
					var selectedHtml = '';
					selectedHtml += '<li data-orgid="'+orgId+'" data-order="'+numSelected+'" class="dept-tree-leaf dept-user zk-user">';
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
					selectedHtml += '		<span class="contact-name"> '+orgName+'</span>';
					selectedHtml += '	</div>';
					selectedHtml += '</li>';
					
					if(numSelected > 1)
						$showTreeObj.find('li').last().after(selectedHtml);
					else
						$showTreeObj.html(selectedHtml);
				}
			});
			
			$('.tree-item').on('click',function(){
				if($(this).hasClass('tree-selected')){
					$(this).removeClass('tree-selected');
					$(this).removeClass('item-selected');
				} else {
					$(this).addClass('tree-selected item-selected');
				}
			});
		}
	}
});