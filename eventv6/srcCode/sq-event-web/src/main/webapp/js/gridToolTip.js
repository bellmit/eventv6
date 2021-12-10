var zyupload_vesion=/\/js\/gridToolTip\.js$/;//当前版本，记得跟新版本时这个也要跟这替换

//获得域名
function getBasePath(){
	var scripts = document.getElementsByTagName('script'), i, ln, path, scriptSrc, match;
	for (i = 0, ln = scripts.length; i < ln; i++) {
		scriptSrc = scripts[i].src;
        match = scriptSrc.match(zyupload_vesion);
        if (match) {
            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
            break;
        }
    }
	if(path)
		return path;
	return "";
}
$(function(){
	 $(".gridTooltip").each(function(i,n){
	    	var _gridId = $(this).attr("gridId");
	    	var src =""; 
	    	if(_gridId){
	    		src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?gridId='+_gridId
	    	}else{
	    		var _infoOrgCode = $(this).attr("infoOrgCode");
	    		src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?infoOrgCode='+_infoOrgCode
	    	}
	    	
	    	$.ajax({
			    type: 'POST',
				url: src,
				dataType: "json",
				success: function(data){
					 $(n).tooltip({
						position: 'bottom',
						content: data,
						onShow: function(){
							$(this).tooltip('tip').css({
								borderColor: '#ff0000'
							});
						}
					});
				}
			});
	    });

	$(".gridTooltip2").each(function(i,n){
		var _gridId = $(this).attr("gridId");
		var src ="";
		if(_gridId){
			src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?gridId='+_gridId
		}else{
			var _infoOrgCode = $(this).attr("infoOrgCode");
			src = getBasePath().replace("/zyupload","")+'/zzgl/grid/mixedGrid/getGridPath.json?infoOrgCode='+_infoOrgCode
		}

		$.ajax({
			type: 'POST',
			url: src,
			dataType: "json",
			success: function(data){
				var index1=data.lastIndexOf(">");
				var gridName=data.substring(index1+1,data.length);//后缀名
				$(n).html(gridName);
				$(n).tooltip({
					position: 'bottom',
					content: data,
					onShow: function(){
						$(this).tooltip('tip').css({
							borderColor: '#ff0000'
						});
					}
				});
			}
		});
	});
	$(".gridTooltip3").each(function(i,n){
		var _gridId = $(this).attr("gridId");
		var src ="";
		if(_gridId){
			src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?gridId='+_gridId
		}else{
			var _infoOrgCode = $(this).attr("infoOrgCode");
			if(!_infoOrgCode) return;
			src = getBasePath().replace("/zyupload","")+'/zzgl/grid/mixedGrid/getGridPath.json?infoOrgCode='+_infoOrgCode
		}

		$.ajax({
			type: 'POST',
			url: src,
			dataType: "json",
			success: function(data){
				var index1=data.lastIndexOf(">");
				var gridName=data.substring(index1+1,data.length);//后缀名
				$(n).val(gridName);
			}
		});
	});
});

function getGridNameByGridId(value,rec,index){
	var _gridId = rec.gridId;
	var _span ="<span id=\"gridNameTXT_"+index+"\">"+value+"</span>";
	var src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?gridId='+_gridId;
	$.ajax({
		type: 'POST',
		url: src,
		dataType: "json",
		success: function(data){
			var txt ="";
			if(data){
				if(data.lastIndexOf(">")!=-1)
					txt = data.substring(data.lastIndexOf(">")+1);
				else{
					txt =data;
				}
			}
			var gid = "gridNameTXT_"+index;
			$("#"+gid).html(txt);
			$("#"+gid).tooltip({
				position: 'bottom',
				content: data,
				onShow: function(){
					$(this).tooltip('tip').css({
						borderColor: '#ff0000'
					});
				}
			});
		},
		error:function(data){
			var gid = "gridNameTXT_"+index;
			$("#"+gid).html("");
		}
	});
	return _span;
}

function getGridNameByInfoOrgCode(value,rec,index){
	var _infoOrgCode = rec.infoOrgCode;
	var _span ="<span id=\"gridNameTXT_"+index+"\">"+value+"</span>";
	var src = getBasePath()+'/zzgl/grid/mixedGrid/getGridPath.json?infoOrgCode='+_infoOrgCode;
	$.ajax({
		type: 'POST',
		url: src,
		dataType: "json",
		success: function(data){
			var txt =data;
			if(data){
				if(data.lastIndexOf(">")!=-1)
					txt = data.substring(data.lastIndexOf(">")+1);
			}
			var gid = "gridNameTXT_"+index;
			$("#"+gid).html(txt);
			$("#"+gid).tooltip({
				position: 'bottom',
				content: data,
				onShow: function(){
					$(this).tooltip('tip').css({
						borderColor: '#ff0000'
					});
				}
			});
		},
		error:function(data){
			var gid = "gridNameTXT_"+index;
			$("#"+gid).html("");
		}
	});
	return _span;
}