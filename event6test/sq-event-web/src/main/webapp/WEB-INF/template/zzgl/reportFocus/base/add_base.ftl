<script type="text/javascript">
	function init4Location(locationId, option) {
		var locationOption = {
			_source : 'XIEJING',//必传参数，数据来源
			_select_scope : 0,
			_show_level : 6,//显示到哪个层级
			_context_show_level : 0,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
			_startAddress :"${reportFocus.occurred!}",
			_startDivisionCode : "${startDivisionCode!}", //默认选中网格，非必传参数
			_customAddressIsNull : false,
			_addressMap : {//编辑页面可以传这个参数，非必传参数
				_addressMapShow : true,//是否显示地图标注功能
				_addressMapIsEdit : true
			},
			BackEvents : {
				OnSelected : function(api) {
					var isLocated = api.addressData._addressMap._addressMapIsEdit || false,
						latitude = '', longitude = '', mapType = '5',
						showName = "标注地理位置";
					
					$("#occurred").val(api.getAddress());
					
					if(isLocated == true) {
						latitude = api.addressData._addressMap._addressMapX;
						longitude = api.addressData._addressMap._addressMapY;
						mapType = api.addressData._addressMap._addressMapType;
					}
					
					if(latitude && longitude) {
						showName = "修改地理位置";
					} else {
						latitude = '';
						longitude = '';
						mapType = '';
					}
					
					$('#x').val(latitude);
					$('#y').val(longitude);
					$('#mapt').val(mapType);
					$("#mapTab2").html(showName);
				},
				OnCleared : function(api) {
					//清空按钮触发的事件
					$("#occurred").val('');
					$('#x').val('');
					$('#y').val('');
					$('#mapt').val('');
					$("#mapTab2").html('标注地理位置');
				}
			}
		};
		
		<#if reportFocus.resMarker??>
			$.extend(locationOption._addressMap, {
				_addressMapX	: '${reportFocus.resMarker.x!}',
				_addressMapY	: '${reportFocus.resMarker.y!}',
				_addressMapType	: '${reportFocus.resMarker.mapType!}' 
			});
		</#if>
		
		option = option || {};
		
		for(var index in option) {
			if(typeof option[index] === 'object') {
				$.extend(locationOption[index], option[index]);
			} else {
				locationOption[index] = option[index];
			}
		}
		
		$("#" + locationId).anoleAddressRender(locationOption);
	}
	
	function autoRequiredBase(formId, isRequired) {
		isRequired = isRequired || false;
		
		$('#' + formId + ' .autoRequired').each(function() {
			var itemId = $(this).attr('id');
			
			$('#' + itemId).validatebox({
				required: isRequired
			});
		});
		
		if(isRequired) {
			$('#' + formId + ' .autoRequiredAsterik').show();
		} else {
			$('#' + formId + ' .autoRequiredAsterik').hide();
		}
	}
	
	/**
	* 表单属性是否变更，未变更返回true，否则返回false
	* formAttrArrayOriginal	原表单属性，为数组对象
	* formAttrArray			现表单属性，为数组对象
	* eliminateObj			需要移除的属性，为{}对象
	*/
	function formAttrCompare(formAttrArrayOriginal, formAttrArray, eliminateObj) {
		var flag = false, formAttrObjOriginal = formAttrArray2Obj(formAttrArrayOriginal), formAttrObj = formAttrArray2Obj(formAttrArray);
		eliminateObj = eliminateObj || {};
				
		for(var key in eliminateObj) {
			if(eliminateObj[key] === '') {
				delete formAttrObjOriginal[key];
				delete formAttrObj[key];
			} else {
				if(formAttrObjOriginal[key] == eliminateObj[key]) {
					delete formAttrObjOriginal[key];
				}
				
				if(formAttrObj[key] == eliminateObj[key]) {
					delete formAttrObj[key];
				}
			}
		}
		
		flag = formAttrCompare4Obj(formAttrObjOriginal, formAttrObj, formAttrObjOriginal);
		
		return flag;
	}
	
	/**
	* 指定的表单属性是否变更，未变更返回true，否则返回false
	* formAttrArrayOriginal	原表单属性，为数组对象
	* formAttrArray			现表单属性，为数组对象
	* specificObj			需要进行变更判断的属性，为{}对象
	*/
	function specificFormAttrCompare(formAttrArrayOriginal, formAttrArray, specificObj) {
		return formAttrCompare4Obj(formAttrArray2Obj(formAttrArrayOriginal), formAttrArray2Obj(formAttrArray), specificObj);
	}
	
	/**
	* 指定的表单属性是否变更，未变更返回true，否则返回false
	* formAttrObjOriginal	原表单属性，为json对象
	* formAttrObj			现表单属性，为json对象
	* specificObj			需要进行变更判断的属性，为{}对象
	*/
	function formAttrCompare4Obj(formAttrObjOriginal, formAttrObj, specificObj) {
		var flag = false;
		specificObj = specificObj || {};
		
		if(formAttrObjOriginal && formAttrObj && specificObj) {
			flag = true;
			
			for(var key in specificObj) {
				if(formAttrObjOriginal[key] || formAttrObj[key]) {
					if(typeof formAttrObjOriginal[key] === 'object'
						&& typeof formAttrObj[key] === 'object') {
						flag = specificFormAttrCompare(formAttrObjOriginal[key], formAttrObj[key], specificObj);
					} else if(formAttrObjOriginal[key] != formAttrObj[key]) {
						flag = false;
					}
					
					if(flag == false) {
						break;
					}
				}
			}
		}
		
		return flag;
	}
	
	/**
	* 数组对象转换为json对象
	* formAttrArray	数组对象，格式为[{'name': '', 'value': ''}, {'name': '', 'value': ''}]
	*/
	function formAttrArray2Obj(formAttrArray) {
		var formAttrObj = null;
		
		if(formAttrArray && typeof formAttrArray === 'object') {
			var objName = null, objValue = null;
			formAttrObj = {};
			
			for(var index in formAttrArray) {
				objName = formAttrArray[index].name;
				objValue = formAttrArray[index].value;
				
				formAttrObj[objName] = objValue;
			}
		}
		
		return formAttrObj;
	}
	
	/**
	* 获取短信模板中有涉及到表单属性id
	*/
	function _capAdviceNoteAssociatedAttr(adviceNoteInitial) {
		var attrIdObj = {};
		
		if(adviceNoteInitial) {
			if(adviceNoteInitial.indexOf("@reportDayZH@") >= 0
				|| adviceNoteInitial.indexOf("@reportTimeMinZH@") >= 0) {
				attrIdObj.reportTime = '';
			}
			
			if(adviceNoteInitial.indexOf("@feedbackDay@") >= 0
				|| adviceNoteInitial.indexOf("@feedbackDayZH@") >= 0
				|| adviceNoteInitial.indexOf("@feedbackHourZH@") >= 0
				|| adviceNoteInitial.indexOf("@feedbackMinZH@") >= 0) {
				attrIdObj.feedbackTime = '';
			}
			
			if(adviceNoteInitial.indexOf("@occurred@") >= 0) {
				attrIdObj.occurred = '';
			}
			
			if(adviceNoteInitial.indexOf("@gridRegionName@") >= 0
				|| adviceNoteInitial.indexOf("@communityRegionName@") >= 0
				|| adviceNoteInitial.indexOf("@streetRegionName@") >= 0) {
				attrIdObj.regionCode = '';
			}
		}
		
		return attrIdObj;
	}
</script>