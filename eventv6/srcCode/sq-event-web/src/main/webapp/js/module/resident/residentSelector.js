
(function ($) {
	var residentDefautOpts={
            width: 160,
            height: 28,
	    panelWidth: 440,
	    idField: 'CI_RS_ID',
	    textField: 'identityCard',
	    mode: 'remote',
	    url: '',
	    striped: true,
	    loadMsg: '正在处理，请稍后……', 
	    columns: [[
		      {field:'partyName',title:'姓名',width:100},
		      {field:'gender',title:'性别',align:'center',width:70,formatter:FmtGender},
		      {field:'identityCard',title:'身份证号',align:'center',width:150}

	    ]],
	    pagination: true,
	    layout:['first','prev','manual','next','last'],
	    fitColumns: true,
	    onChange:function(newValue,oldValue){
	    	$("#ciRsId").val("");
	    	$("#_hidGridId").val("");
	    	$("#rsId").val("");
	    	try{
		    	if(typeof(eval(residentChange)) == "function"){
		    		residentChange();
		    	}
	    	}
	    	catch(e){
	    	}
	    },
	    onLoadSuccess:function(data){
	    	if( typeof residentLoad === 'function' ){ 
	    		residentLoad();
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
	        match = scriptSrc.match(/js\/residentSelector\.js$/);
	        if (match) {
	            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
	            break;
	        }
	    }
	    return path;
		
	}
	
	function initResidentSelectors(){
		var path = getBasePath();
		$(".residentSelector").each(function(){
			
			var dataOpts = $(this).attr("resident-Option");
			if(dataOpts==null){
				dataOpts="";
			}
			else if(typeof(dataOpts)=="string"){
				dataOpts = new Function("return "+dataOpts)();
			}
			var opts =$.extend({}, residentDefautOpts, dataOpts, {
			    onSelect: function(index,data){
			    	var handler = dataOpts["callback"]?dataOpts["callback"]:residentDefautOpts["defaultSelect"];
			    	handler(data);
			    }
			});
			if(!opts["url"]){
				opts["url"] = path+'/resident/search/listData3.json?orgCode='+opts["orgCode"];
			}
			$(this).combogrid(opts);
			//$(this).combogrid("getPager").pagination({layout:['first','prev','manual','next','last']});
		});
	}
	 

	$.fn.initResidentSelector = function() {
		var me = $(this);
		//$(this).combogrid("getPager").pagination({layout:['first','prev','manual','next','last']});
		var path = getBasePath();
		var dataOpts = $(this).attr("resident-Option");
		if (dataOpts == null) {
			dataOpts = "";
		} else if (typeof (dataOpts) == "string") {
			dataOpts = new Function("return " + dataOpts)();
		}
		var opts = $.extend({}, residentDefautOpts, dataOpts, {
			onSelect : function(index, data) {
				var handler = dataOpts["callback"] ? dataOpts["callback"]: residentDefautOpts["defaultSelect"];
				handler(me, data);
			}
		});
		if (!opts["url"]) {
			opts["url"] = base + '/resident/search/listData3.json?orgCode='+ opts["orgCode"];
		}
		me.combogrid(opts);
	};
})(jQuery);


function FmtGender(v){
	if(v){
	  if(v=='1' || v=='M'){return '男';}
	  else {return '女';}
	}
    return '';
}

