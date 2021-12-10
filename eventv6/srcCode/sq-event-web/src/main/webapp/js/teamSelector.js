
(function ($) {
	var teamDefautOpts={
            width: 160,
            height: 28,
	    panelWidth: 440,
	    multiple: true,
		rownumbers:true,
	    idField: 'name',
	    textField: 'name',
	    mode: 'remote',
	    url: '',
	    striped: true,
	    loadMsg: '正在处理，请稍后……', 
	    columns: [[
	      {field:'name',title:'组织姓名',width:100},
	      {field:'manager',title:'负责人',align:'center',width:70},
	      {field:'managerTel',title:'负责人电话',align:'center',width:150}
	    ]],
	    pagination: true,
	    fitColumns: true,
	    onChange:function(newValue,oldValue){
	    	try{
		    	if(typeof(eval(teamChange)) == "function"){
		    		teamChange();
		    	}
	    	}
	    	catch(e){
	    	}
	    },
	    onLoadSuccess:function(data){
	    	if( typeof teamLoad === 'function' ){ 
	    		teamLoad();
	    	}
	    },
	    defaultSelect:function(data){
	    	//......
	    }
	};
	
	function getBasePath(){
		var scripts = document.getElementsByTagName('script'), i, ln, path, scriptSrc, match;
		for (i = 0, ln = scripts.length; i < ln; i++) {
			scriptSrc = scripts[i].src;
	        match = scriptSrc.match(/js\/teamSelector\.js$/);
	        if (match) {
	            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
	            break;
	        }
	    }
	    return path;
		
	}
	
	var dataList = new Array();
	
	function initteamSelectors(){
		var path = getBasePath();
		$(".teamSelector").each(function(){
			var dataOpts = $(this).attr("team-Option");
			if(dataOpts==null){
				dataOpts="";
			}
			else if(typeof(dataOpts)=="string"){
				dataOpts = new Function("return "+dataOpts)();
			}
			var opts =$.extend({}, teamDefautOpts, dataOpts, {
			    onSelect: function(index,data){
			    	var handler = dataOpts["callback"]?dataOpts["callback"]:teamDefautOpts["defaultSelect"];
			    	dataList.push(data);
			    	handler(dataList);
			    },
			    onUnselect: function(index,data){
			    	var handler = dataOpts["callback"]?dataOpts["callback"]:teamDefautOpts["defaultSelect"];
			    	dataList.splice($.inArray(data,dataList),1);
			    	handler(dataList);
			    }
			});
			if(!opts["url"]){
				opts["url"] = path+'/team/search/listData.json?bizType='+opts["bizType"];
			}
			$(this).combogrid(opts);
		});
	}
	 $(function () {
		 initteamSelectors();
	 });
	 

})(jQuery);


