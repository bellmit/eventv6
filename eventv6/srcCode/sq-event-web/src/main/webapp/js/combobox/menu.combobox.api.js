/**
 * 多js公用一个Object对象
 */
if (typeof (MenuTreeApi) == "undefined") {
	var MenuTreeApi = {};
}

/**
 * 菜单树下拉框
 * 
 * @param tId1 : [必选]回填name值的文本框id
 * @param tId2 : [可选]回填value值的文本框id
 * @param callBackSelectedEvent : [可选]选中回调事件
 * @param options : [可选]用于修改默认参数值
 * @returns : 下拉框组件对象
 */
MenuTreeApi.initMenuZtreeComboBox = function(tId1, tId2, callBackSelectedEvent, options) {
	var settings = {
		ContextPath : $.anoleMenuContextPath(),
		RenderType : "10",
		TargetId : tId2,
		DataSrc : "/zhsq/map/menuconfigure/menuConfig/menuZTreeForJsonp.json?jsoncallback=?",
		Async : {
			enable : true,
			autoParam:["id=gdcPid"],
			dataFilter : _filter,
			otherParam : {
				"rootGdcPid" : $("#rootGdcPid").val()
			}
		},
		DataValue : "gdcPid",
		DataName : "name",
		OnSelected : callBackSelectedEvent,
		OnChanged : null,
		OnCleared : null,
		OnRenderCompleted : null,
		OnBusiVerify : null,
		IsTriggerDocument : true,
		DownDiv : {
			IsAutoStyle : false,
			Style : {
				"height" : "auto",
				"min-height" : "130px",
				"max-height" : "230px",
				"width" : "auto",
				"min-width" : "160px",
				"overflow-x" : "hidden",
				"border-color" : "#E2E3EA"
			}
		},
		ShowOptions : {}
	};
	$.extend(settings, options);
	if (typeof settings.DataSrc == "string") {
		settings.DataSrc = settings.ContextPath + settings.DataSrc;
	}
	return $("#" + tId1).anoleMenuRender(settings).menuTreeApi();
};

function _filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for ( var i = 0, l = childNodes.length; i < l; i++) {
		if (childNodes[i].name) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
	}
	return childNodes;
}