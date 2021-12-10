/*
 * *****************************************************************
 * 1、contentPath为综治事件(zhsq_event)项目域名，
 * 本项目即${rc.getContextPath()}，其他项目为$EVENT_DOMAIN中的配置值
 * 2、openWin只能使用showCustomEasyWindow类似的方法，否则会导致语音盒不能正常使用
 * 3、bcall电话号码
 * 4、userName用户名称
 * 3、userImg中需要包含域名，否则图片不能正常显示
 * 5、softPhone区分是否软电话
********************************************************************/

function showVoiceCall(contentPath, openWin, bCall, userName, userImg,softPhone){
	 if(bCall==null || bCall==""){
		 $.messager.alert("提示", "没有电话号码可以呼叫！", "info");
		return;
	 }
	 
	 if(userName==undefined || userName==null || userName=="null"){
		 userName = "";
	 }
	 if (userImg==undefined || userImg==null || userImg=='') { 
	  	 userImg = contentPath+"/images/yy_31.png";//默认人员头像
	 }
	 
	 var url = contentPath+"/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall=" + bCall 
	 + "&userName=" + encodeURIComponent(encodeURIComponent(userName)) 
	 + "&userImg=" + encodeURI(userImg) + "&" + new Date().getTime();
	 var title = "语音盒呼叫";
	 if(softPhone){
		 url+="&page="+softPhone;
		 title ="电话呼叫";
	 }
	 
	 if(openWin!=undefined && openWin!=null && openWin!=""){
		 openWin(title, url, 314, 410);
	 }
}

/*
 * *****************************************************************
 * typeComboxCallBack,typesComboxCallBack是配合事件类型过滤使用的
 * itemId为存放下拉框generalCode的控件的Id
 * bigTypes为事件类型的大类，以英文逗号连接
 * smallTypes为事件类型的小类，以英文逗号连接
 * types为事件类型，以英文逗号连接
 * 
********************************************************************/

function typeComboxCallBack(itemId, bigTypes, smallTypes){
	if(itemId!=undefined && itemId!=null && itemId!=""){
		var type = $("#"+itemId).val();
		
		if(type.length==2 && (bigTypes.length>0 || smallTypes.length>0)){
			if(bigTypes.indexOf(type+',') == -1){
				var smallTypesAttr = smallTypes.split(',');
				var smallTypeStr = "";
				
				for(var index = 0, len = smallTypesAttr.length; index < len; index++){
					var smallType = smallTypesAttr[index];
					if(smallType!=null && smallType.length>2 && smallType.indexOf(type)==0){
						smallTypeStr += smallType + ",";
					}
				}
				
				if(smallTypeStr.length > 0){
					smallTypeStr = smallTypeStr.substr(0, smallTypeStr.length-1);
					$("#"+itemId).val(smallTypeStr);
				}
			}
		}
	}
}

function typesComboxCallBack(itemId, types){
	if(itemId!=undefined && itemId!=null && itemId!=""){
		if(types!=undefined && types!=null && types!=""){
			var typeAttr = types.split(",");
			var bigTypes = "";
			var smallTypes = "";
			
			for(var index = 0, len = typeAttr.length; index < len; index++){
				var typeTemp = typeAttr[index];
				if(typeTemp!=null && typeTemp.length>0){
					if(typeTemp.length == 2){
						bigTypes += typeTemp + ',';
					}else if(typeTemp.length > 2){
						smallTypes += typeTemp + ',';
					}
				}
			}
			
			typeComboxCallBack(itemId, bigTypes, smallTypes);
		}
	}
}
