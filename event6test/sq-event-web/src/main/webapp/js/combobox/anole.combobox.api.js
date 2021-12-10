var $EventTypeObj = null;
var $GridZtreeObj = null;
/**
 * 树形类型初始化
 * 
 * @param contextPath
 * @param sourceFieldId
 * @param targetFieldId
 * @param pcode
 */
function initTreeComboBox(contextPath, sourceFieldId, targetFieldId, pcode) {
	$("#" + sourceFieldId)
			.anoleRender(
					{
						ContextPath : contextPath,
						RenderType : "10",
						TargetId : targetFieldId,
						DataSrc : contextPath
								+ "/zhsq/event/eventDisposalController/getDataDictTree.json?dictPcode="
								+ pcode,
						RootPCode : pcode,
						OnChanged : _OnChanged,
						OnSelected : _OnSelected,
						DownDiv : {
							IsAutoStyle : false,
							Style : {
								"height" : "auto",
								"min-height" : "130px",
								"max-height" : "230px",
								"width" : "auto",
								"overflow-x" : "hidden",
								"border-color" : "#E2E3EA"
							}
						},
						DropButton : {
							IsAutoStyle : false,
							Style : {
								"width" : "14px"
							}
						}
					});
}

function _OnChanged(val) {// val 选择后的值
	if (typeof onTreeComboBoxChanged != "undefined") {
		onTreeComboBoxChanged(val);
	}
}

function _OnSelected(val) {
	if (typeof onTreeComboBoxSelected != "undefined") {
		onTreeComboBoxSelected(val);
	}
}

// 网格树选中后，回调方法
function _OnGridTreeSelected(gridId, items) {
	if (typeof onGridTreeSelected != "undefined") {
		onGridTreeSelected(gridId, items);
	}
}

/**
 * 列表单选初始化
 * 
 * @param contextPath
 * @param sourceFieldId
 * @param targetFieldId
 * @param pcode
 * @param isSelectMore
 */
function initListComboBox(contextPath, sourceFieldId, targetFieldId, pcode,
		isSelectMore) {
	var defaultRenderType = "00";
	if (isSelectMore == true) {
		defaultRenderType = "01";
	}
	$("#" + sourceFieldId)
			.anoleRender(
					{
						ContextPath : contextPath,
						RenderType : defaultRenderType,
						TargetId : targetFieldId,
						DataSrc : contextPath
								+ "/zhsq/event/eventDisposalController/getDataDictTree.json?dictPcode="
								+ pcode,
						RootPCode : pcode,
						DownDiv : {
							IsAutoStyle : false,
							Style : {
								"height" : "auto",
								"min-height" : "130px",
								"max-height" : "230px",
								"width" : "auto",
								"overflow-x" : "hidden",
								"border-color" : "#E2E3EA"
							}
						},
						DropButton : {
							IsAutoStyle : false,
							Style : {
								"width" : "14px"
							}
						}
					});
}

/**
 * 树形事件类型初始化
 * 
 * @param contextPath
 * @param sourceFieldId
 * @param targetFieldId
 */
function initEventTypeComboBox(contextPath, sourceFieldId, targetFieldId) {
	$("#" + sourceFieldId)
			.anoleRender(
					{
						ContextPath : contextPath,
						RenderType : "10",
						TargetId : targetFieldId,
						DataSrc : contextPath
								+ "/zhsq/event/eventDisposalController/getDataDictTree.json?dictPcode=A001093199",
						RootPCode : "A001093199",
						OnRenderCompleted : initEventTypeCompleted,
						DownDiv : {
							IsAutoStyle : false,
							Style : {
								"height" : "280px",
								"width" : "auto",
								"overflow-x" : "hidden",
								"border-color" : "#E2E3EA"
							}
						},
						DropButton : {
							IsAutoStyle : false,
							Style : {
								"width" : "14px"
							}
						}
					});
}

/**
 * 树形事件类型初始化（tab切换风格）
 * 
 * @param contextPath
 * @param sourceFieldId
 * @param targetFieldId
 */
function initEventTypeComboBox2(contextPath, sourceFieldId, targetFieldId) {
	$("#" + sourceFieldId)
			.anoleRender(
					{
						ContextPath : contextPath,
						RenderType : "20",
						TargetId : targetFieldId,
						DataSrc : contextPath
								+ "/zhsq/event/eventDisposalController/getDataDictTree.json?dictPcode=A001093199",
						RootPCode : "A001093199",
						OnRenderCompleted : initEventTypeCompleted,
						DownDiv : {
							IsAutoStyle : true
						},
						DropButton : {
							IsAutoStyle : false,
							Style : {
								"width" : "14px"
							}
						}
					});
}

function initEventTypeCompleted(api) {
	$EventTypeObj = api;
}

/**
 * 网格树下拉框初始化
 * 
 * @param contextPath
 * @param sourceFieldId
 * @param targetFieldId
 */
function initGridZtreeComboBox(contextPath, sourceFieldId, targetFieldId) {
	$("#" + sourceFieldId).anoleRender({
		ContextPath : contextPath,
		RenderType : "10",
		TargetId : targetFieldId,
		DataSrc : contextPath + "/zhsq/grid/mixedGrid/gridZTree.json",
		Async : {
			enable : true,
			autoParam : [ "id=gridId" ],
			dataFilter : filter,
			otherParam : {
				"startGridId" : -1
			}
		},
		DataValue : "id",
		OnSelected : _OnGridTreeSelected,
		OnRenderCompleted : initGridZtreeCompleted,
		DownDiv : {
			IsAutoStyle : false,
			Style : {
				"height" : "auto",
				"min-height" : "130px",
				"max-height" : "230px",
				"width" : "auto",
				"overflow-x" : "hidden",
				"border-color" : "#E2E3EA"
			}
		},
		DropButton : {
			IsAutoStyle : false,
			Style : {
				"width" : "14px"
			}
		}
	});
}

function initGridZtreeCompleted(api) {
	$GridZtreeObj = api;
}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for ( var i = 0, l = childNodes.length; i < l; i++) {
		if (childNodes[i].name) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
	}
	return childNodes;
}