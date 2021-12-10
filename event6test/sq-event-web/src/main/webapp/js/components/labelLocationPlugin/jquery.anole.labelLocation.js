/**
 * jQuery Anole Date
 *
 * Copyright 2014, YangCQ & sulch
 * No licensed under the licenses.
 * http://www.ximifan.com/
 *
 * Includes jquery.js
 * http://jquery.com/
 *
 * Date: 2014-11-13 17:36:03
 */
(function($) {
	var _LabelLocation = {
		sys : {						// 存放内部参数与函数
			version : "jQuery-Anole-LabelLocation-v1.0.0",
			type : "",
			name : "jquery.anole.labelLocation.js",
			path : "/js/components/labelLocationPlugin/",
			ContextPath : null,		// 上下文路径(必传)
			addressListDataSrc : "/zhsq/map/labelLocation/labelLocationController/addressListData.jhtml?jsoncallback=?",//部件列表查询URL
			buildingListDataSrc : "/zhsq/map/labelLocation/labelLocationController/buildingListData.jhtml?jsoncallback=?",//楼宇列表查询URL
			newMapCallBackUrl : '/zhsq/map/labelLocation/labelLocationController/toLabelLocationCrossDomain.jhtml',//新地图回调链接
			oldMapCallBackUrl : '/zzgl/map/labelLocationController/labelLocationCrossDomainCallBack.jhtml', //旧地图回调函数'
			html : '<div class="nav"><ul>[[item-head]]</ul></div><div class="con">[[item-body]]</div>',
			oldSearchStr : "",		//上次查询条件
			lastKeyupTime : ''     //keyup时间标识
		},
		renderObj : {},
		settings : {
			// ------ 常规参数控制 ------
			ContextPath : null,		// 上下文路径(必传)
			// ------ 显示参数控制 ------
			ShowOptions : {
				ErrorBgColor : "#fff3f3"
			},
			// ------ 回调事件 ------
			BackEvents : {
				OnSelected : null,
				OnCleared : null
			},
			// ------ 下拉按钮个性属性 ------
			DropButton : {
				BINOpenPath : "",//"img/Addressview.png",
				BINOpenHoverPath : "",//"img/Addressview.png",
				BIYOpenPath : "",//"img/Addressview.png",
				BIYOpenHoverPath :"" //"img/Addressview.png"
			},
			// ------ 下拉DIV个性属性 ------
			DownDiv : {
				Style : {}
			},
			_sys_domain : "",					//系统应用的域名
			_bizId : "",						//业务id
			_showTypes : "mapLabelLocation",    //配置组件显示功能：mapLabelLocation:地图标准,addressLabelLocation部件列表,buildingLabelLocation楼宇列表,addMapLabelLocation部件地图展示
			_moduleTypeCode : "",				//模块类型编码
			_labelLocationType : "map",			//地理位置标注类型
			_isEdit : "true",					//是否修改地图标注信息
			_callBackUrl : "",					//地图标注功能回调路径
			_startInfoOrgCode : "",  			//所属信息域code
			_resId : "",  		 				//选中后的部件或楼宇id
			_resLocationX : "",					//标注的经度
			_resLocationY : "",					//标注的纬度
			_resLocationMapType : "",			//标注的地图类型
			_targetId : "",           			//目标文本框ID,
			_defaultGridId : '',	  			//当前登录人的gridId
			_defaultGridName : '',	  			//当前登录人的GridName
			_defaultInfoOrgCode : '',  			//当前登录人的InfoOrgCode
			_lastKeyupTime : '',				//最后一次按键时间
			_isAddressFirstClick : 1,			//部件面板是否是第一次点击
			_isBuildingFirstClick : 1,			//部件楼宇是否是第一次点击
			_isMapFirstClick : 1,				//部件地图是否是第一次点击
			_isHaveAddressListDiv : 0,			//是否已经有部件选择面板
			_isHaveMapListDiv : 0,				//是否已经有地图选择面板
			_isHaveBuildingListDiv : 0,			//是否已经有楼宇选择面板
			_isHaveAddMapListDiv : 0			//是否已经有部件地图选择面板

		}
	};

	// 渲染地址控件入口
	_LabelLocation.sys.renderForLabelLocation = function(index, ctrlType, settings, element) {
		var me = this;
		me.data = {};
		_LabelLocation.sys.getMapLocationInfo(this, settings);
		new _LabelLocation.sys._renderForComm(this, index, ctrlType, settings, element);
		settings.targetId = $(element).attr("id");

		var showTypesArray = settings._showTypes.split(",");

		for (var i = 0; i < showTypesArray.length; i++) {
			if("addressLabelLocation" == showTypesArray[i] && settings._isEdit == 'true'){
				_LabelLocation.sys.initAddressList(me);
			}
			if("buildingLabelLocation" == showTypesArray[i] && settings._isEdit == 'true'){
				_LabelLocation.sys.initbuildingList(me);
			}
		}
		me.divHolder.addClass('AddressControl');

	};


	/**
	 * 按部件地理位置列表初始化
	 * @param target
	 */
	_LabelLocation.sys.initAddressList = function(target){
		$('#address_list_' + target.downDivId).datagrid({
			width : 425,
			height : 270,
			nowrap : false,
			striped : true,
			singleSelect: true,
			showHeader: false,
			idField : 'divisionCod',
			columns:[[
				{field:'id',title:'',hidden:true},
				{field:'componentsInfoStr',title:'部件信息', align:'left',width:420, formatter:function(value, rec, index){
					var componentsInfoStr = "";
					if(rec.componentsName!=null && rec.componentsName!=""){
						componentsInfoStr = componentsInfoStr + rec.componentsName;
					}
					if(componentsInfoStr != null && componentsInfoStr != ""){
						componentsInfoStr = '<div title="' + componentsInfoStr + '" style="line-height:20px;margin:10px 0px">'+componentsInfoStr+'</div>';
					}
					return componentsInfoStr;
				}}
			]],
			pageSize: 20,
			pageNumber : 1,
			pagination:true,
			onBeforeLoad:function(param){
				if(param.q){
					var n= param.q;
					if(n.lastIndexOf(" ")>=0)//输入法，输入时会有空格。避免没必要的空格查询
						return false;
					param.q= $.trim(n);
				}
			},
			onLoadSuccess:function(data){
				//隐藏datagrid的头部
				$('.datagrid-header').css({
					"visibility" : "hidden",
					"position" : "absolute"
				});

				$('#address_list_' + target.downDivId).datagrid('clearSelections');	//清除掉列表选中记录
				var _target_max_Height = parseInt(target.divHolder.css("max-height"));
				var _paginationHeight = parseInt($('.datagrid-pager .pagination-info').css('height'))+2;
				var _ConSearchHeight = parseInt($('#address_searchPane_'+ target.downDivId).css('height'));
				var _radioHeight = parseInt($('#address_radio_'+ target.downDivId).css('height'));
				_target_max_Height = _target_max_Height - _paginationHeight - _ConSearchHeight - _radioHeight;
				$('.datagrid-view').css("height", _target_max_Height+"px");
				$('.datagrid-view .datagrid-view2 .datagrid-body').css("height", _target_max_Height+"px");
				$('.datagrid-view .datagrid-view2 .datagrid-header').css("height","40px");

				if(data.total==0 || data.rows.length == 0 || data == 'undefined' ){
					var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src= "' + _LabelLocation.sys.anoleContextPath() + '/images/nodata.png" title="暂无数据"/></div>');
				}else{
					$('.datagrid-body-inner').eq(0).removeClass("l_elist");
				}
			},
			onLoadError:function(){
				//隐藏datagrid的头部
				$('.datagrid-header').css({
					"visibility" : "hidden",
					"position" : "absolute"
				});
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			},
			onClickRow : function(rowIndex, rowData) {
				target.settings._resLocationX = rowData.x;
				target.settings._resLocationY = rowData.y;
				target.settings._resLocationMapType = rowData.mapt;
				target.settings._resId = rowData.componentsId;
				$("#x").val(rowData.x);
				$("#y").val(rowData.y);
				$("#mapt").val(rowData.mapt);
				$("#_resId").val(rowData.componentsId);
				$("#_labelLocationType").val("components");


				target._showOrHideDiv();
				//console.log($("#x").val());
				//console.log($("#y").val());
				//console.log($("#mapt").val());
				//console.log("bizId=" + target.settings._bizId);
				$("mapTab_"+ target.downDivId).html("修改地理位置");

				var epk = _LabelLocation.sys.getEpk(target.element);
			}
		});


		//设置分页控件
		var p = $('#address_list_' + target.downDivId).datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			//displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录',
			displayMsg: '',
			onRefresh:function(pageNumber,pageSize){
				$('#address_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
				var searchStr = $('#address_searchCondition_' + target.downDivId).val();
				if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
					if(searchStr == '请输入部件编号查询'){
						searchStr = "";
					}
				}
				_LabelLocation.sys.getAddressListData(target, searchStr);
			},
			onChangePageSize:function(){
				$('#address_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
				var searchStr = $('#address_searchCondition_' + target.downDivId).val();
				if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
					if(searchStr == '请输入部件编号查询'){
						searchStr = "";
					}
				}
				_LabelLocation.sys.getAddressListData(target, searchStr);
			},
			onSelectPage:function(pageNumber,pageSize){
				$('#address_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
				var searchStr = $('#address_searchCondition_' + target.downDivId).val();
				if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
					if(searchStr == '请输入部件编号查询'){
						searchStr = "";
					}
				}
				_LabelLocation.sys.getAddressListData(target, searchStr);
			}
		});
	};

	/**
	 * 获取部件地理位置列表数据
	 * @param target
	 * @param searchStr
	 */
	_LabelLocation.sys.getAddressListData = function(target,searchStr){
		var page = $('#address_list_' + target.downDivId).datagrid('getPager').data("pagination").options.pageNumber || 1;
		var rows = $('#address_list_' + target.downDivId).datagrid('getPager').data("pagination").options.pageSize || 20;
		var mapt = $('#mapt').val();
		var url = _LabelLocation.sys.anoleContextPath();
		if(searchStr == null || typeof(searchStr) == 'undefined' || searchStr.indexOf("请输入") >= 0){
			searchStr = "";
		}
		var gridId = $("#address_gridId_"+ target.downDivId +"").val();
		if(typeof(_LabelLocation.sys.addressListDataSrc) != "undefined" && _LabelLocation.sys.addressListDataSrc != null){
			url = url + _LabelLocation.sys.addressListDataSrc;
		}

		$.ajax({
			type: "POST",
			url : url,
			dataType : 'json',
			data : {
				searchStr : encodeURIComponent(searchStr),
				gridId : gridId,
				mapt : mapt,
				page : page,
				rows : rows
			},
			success : function(data) {
				$('#address_list_' + target.downDivId).datagrid('loadData', data);
				//target._showOrHideDivEx(true);
				$('#address_list_' + target.downDivId).datagrid("loaded");
			},
			error : function() {
				$('#address_list_' + target.downDivId).datagrid("loaded");
				$.messager.alert('提示','部件列表查询异常！','error');
				$('#address_list_' + target.downDivId).datagrid("reloaded");
			}
		});
	};

	/**
	 * 初始化楼宇列表
	 * @param target
	 */
	_LabelLocation.sys.initbuildingList = function(target){
		$('#building_list_' + target.downDivId).datagrid({
			width : 425,
			height : 270,
			nowrap : false,
			striped : true,
			singleSelect: true,
			showHeader: false,
			idField : 'divisionCod',
			columns:[[
				{field:'buildingId',title:'ID',hidden:true},
				{field:'buildingInfoStr',title:'楼宇信息', align:'left',width:420,formatter:function(value, rec, index){
					var buildingInfoStr = "";
					if(rec.buildingName!=null && rec.buildingName!=""){
						buildingInfoStr = buildingInfoStr + rec.buildingName;
					}
					if(rec.buildingAddress!=null && rec.buildingAddress!=""){
						buildingInfoStr = buildingInfoStr + " | " + rec.buildingAddress;
					}
					if(buildingInfoStr != null && buildingInfoStr != ""){
						buildingInfoStr = '<div title="' + buildingInfoStr + '" style="line-height:20px;margin:10px 0px">'+buildingInfoStr+'</div>';
					}
					return buildingInfoStr;
				}}
			]],
			pageSize: 20,
			pageNumber : 1,
			pagination:true,
			onBeforeLoad:function(param){
	        	if(param.q){
	        		var n= param.q;
	        		if(n.lastIndexOf(" ")>=0)//输入法，输入时会有空格。避免没必要的空格查询
	        			return false;
	        		param.q= $.trim(n);
	        	}
	        },
			onLoadSuccess:function(data){
				//隐藏datagrid的头部
				$('.datagrid-header').css({
					"visibility" : "hidden",
					"position" : "absolute"
				});
				
			    $('#building_list_' + target.downDivId).datagrid('clearSelections');	//清除掉列表选中记录
				var _target_max_Height = parseInt(target.divHolder.css("max-height"));
				var _paginationHeight = parseInt($('.datagrid-pager .pagination-info').css('height'))+2;
				var _ConSearchHeight = parseInt($('#building_searchPane_'+ target.downDivId).css('height'));
				var _radioHeight = parseInt($('#building_radio_'+ target.downDivId).css('height'));
				_target_max_Height = _target_max_Height - _paginationHeight - _ConSearchHeight - _radioHeight;
				$('.datagrid-view').css("height", _target_max_Height+"px");
				$('.datagrid-view .datagrid-view2 .datagrid-body').css("height", _target_max_Height+"px");
				$('.datagrid-view .datagrid-view2 .datagrid-header').css("height","40px");

				if(data.total==0 || data.rows.length == 0 || data == 'undefined' ){
				  	var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src= "' + _LabelLocation.sys.anoleContextPath() + '/images/nodata.png" title="暂无数据"/></div>');
				}else{
		          	$('.datagrid-body-inner').eq(0).removeClass("l_elist");
		        }
			},
			onLoadError:function(){
				//隐藏datagrid的头部
				$('.datagrid-header').css({
					"visibility" : "hidden",
					"position" : "absolute"
				});
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			},
			onClickRow : function(rowIndex, rowData) {
				target.settings._resLocationX = rowData.x;
				target.settings._resLocationY = rowData.y;
				target.settings._resLocationMapType = rowData.mapt;
				target.settings._resId = rowData.buildingId;
				$("#x").val(rowData.x);
				$("#y").val(rowData.y);
				$("#mapt").val(rowData.mapt);
				$("#_resId").val(rowData.buildingId);
				$("#_labelLocationType").val("building");

				target._showOrHideDiv();

				//console.log($("#x").val());
				//console.log($("#y").val());
				//console.log($("#mapt").val());
				//console.log("bizId="+target.settings._bizId);
				$("mapTab_"+ target.downDivId).html("修改地理位置");
			}
		});
		

		//设置分页控件
	    var p = $('#building_list_' + target.downDivId).datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			//displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录',
			displayMsg: '',
	       	onRefresh:function(pageNumber,pageSize){
	       		$('#building_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
	       		var searchStr = $('#building_searchCondition_' + target.downDivId).val();
	       		if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
	       			if(searchStr == '请输入楼宇名称查询'){
	       				searchStr = "";
	       			}
				}
				_LabelLocation.sys.getBuildingListData(target, searchStr);
	        },
	       	onChangePageSize:function(){
	       		$('#building_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
	           	var searchStr = $('#building_searchCondition_' + target.downDivId).val();
	       		if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
	       			if(searchStr == '请输入楼宇名称查询'){
	       				searchStr = "";
	       			}
				}
				_LabelLocation.sys.getBuildingListData(target, searchStr);
	        },
	       	onSelectPage:function(pageNumber,pageSize){
	       		$('#building_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
	          	var searchStr = $('#building_searchCondition_' + target.downDivId).val();
	       		if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
	       			if(searchStr == '请输入楼宇名称查询'){
	       				searchStr = "";
	       			}
				}
				_LabelLocation.sys.getBuildingListData(target, searchStr);
	        }
		});
		
	};

	/**
	 * 获取楼宇列表数据
	 * @param target
	 * @param searchStr
	 */
	_LabelLocation.sys.getBuildingListData = function(target,searchStr){
		var page = $('#building_list_' + target.downDivId).datagrid('getPager').data("pagination").options.pageNumber || 1;
		var rows = $('#building_list_' + target.downDivId).datagrid('getPager').data("pagination").options.pageSize || 20;
		var url = _LabelLocation.sys.anoleContextPath();
		var mapt = target.settings._resLocationMapType;
		if(searchStr == null || typeof(searchStr) == 'undefined' || searchStr.indexOf("请输入") >= 0){
			searchStr = "";
		}
		var gridId = $("#building_gridId_"+ target.downDivId +"").val();
		if(typeof(_LabelLocation.sys.buildingListDataSrc) != "undefined" && _LabelLocation.sys.buildingListDataSrc != null){
			url = url + _LabelLocation.sys.buildingListDataSrc;
		}

		$.ajax({
			type: "POST",
			url : url,
			dataType : 'json',
			data : {
				searchStr : encodeURIComponent(searchStr),
				gridId : gridId,
				mapt : mapt,
				page : page,
				rows : rows
			},
			success : function(data) {
				$('#building_list_' + target.downDivId).datagrid('loadData', data);
				$('#building_list_' + target.downDivId).datagrid("loaded");
			},
			error : function() {
				$('#building_list_' + target.downDivId).datagrid("loaded");
				$.messager.alert('提示','楼宇列表查询异常！','error');
				$('#building_list_' + target.downDivId).datagrid("reloaded");
			}
		});
	};

	/**
	 * 触发查询
	 * @param target
	 * @param idStr
	 */
	_LabelLocation.sys.gotoSearch = function(target, idStr) {
		$('#'+ idStr +'_searchCondition_'+ target.downDivId).on('keyup', function(e) {
			var newAddress = $(this).val();
			if(newAddress != null && newAddress != '' && newAddress.indexOf("请输入查询")>=0){
				newAddress = "";
			}
			target.isBreak = false;
			target.settings._lastKeyupTime = new Date().getTime();//利用event的timeStamp来标记时间，这样每次的keyup事件都会修改last的值，注意last必需为全局变量
			setTimeout(function(){    //设时延迟0.5s执行
				$('#'+ idStr +'_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
				if(new Date().getTime()-target.settings._lastKeyupTime >= 500){//如果时间差为0（也就是你停止输入0.5s之内都没有其它的keyup事件发生）则做你想要做的事
					if(idStr != null && idStr != ""){
						if(idStr == "address"){
							_LabelLocation.sys.getAddressListData(target, newAddress);
						}else if(idStr == "building"){
							_LabelLocation.sys.getBuildingListData(target, newAddress);
						}
					}

					target.settings._oldAddress = newAddress;
				} else {
					$('#'+ idStr +'_list_' + target.downDivId).datagrid("loaded");
				}
			},500);
			$('#'+ idStr +'_list_' + target.downDivId).datagrid("loaded");
		});
	};

	/**
	 * 加载地图
	 * @param target
	 */
	_LabelLocation.sys.loadMapInfo = function(target) {
		var x = target.settings._resLocationX;
		var y = target.settings._resLocationY;
		var mapt = target.settings._resLocationMapType;
		var isEdit = target.settings._isEdit;
		var mapType = target.settings._moduleTypeCode;
		var gridId = target.settings._defaultGridId;
		//var callBackUrl = target.settings._callBackUrl;

		if (isEdit == undefined || isEdit == null) {
			isEdit = true;
		}

		if (mapType == undefined || mapType == null) {
			mapType = "";
		}

		var mapEngineType = "";
		var ZHSQ_EVENT_URL = "";
		var ZHSQ_ZZGRID_URL = "";
		var markerType = "";

		$.ajax({
			type : "POST",
			url : _LabelLocation.sys.anoleContextPath() + '/zhsq/map/labelLocation/labelLocationController/getMapEngineInfo.json?mapType=' + mapt +'&modularCode='+ mapType + '&t=' + Math.random(),
			dataType : "json",
			async : false,
			success : function(data) {
				mapEngineType = data.mapEngineType;
				ZHSQ_EVENT_URL = data.ZHSQ_EVENT_URL;
				ZHSQ_ZZGRID_URL = data.ZHSQ_ZZGRID_URL;
				mapType = data.mapType;
				markerType = data.markerType
			},
			error : function(data) {
				$.messager.alert('错误', '无法获取地图引擎信息！', 'error');
			}
		});

		var data;
		var url;
		var targetDownDivId = target.downDivId;
		if (x != undefined && x != '' && y != undefined && y != '' && mapt != undefined && mapt != '') {
			data = 'x=' + x + '&y=' + y + '&mapt=' + mapt + '&gridId=' + gridId + '&isEdit=' + isEdit + '&mapType='
			+ mapType;
		} else {
			data = 'gridId=' + gridId + '&isEdit=' + isEdit + '&mapType=' + mapType;
		}
		// 天地图
		if ('005' == mapEngineType) {// 新地图链接
			data = data + "&callBackUrl=" + ZHSQ_EVENT_URL + _LabelLocation.sys.newMapCallBackUrl  + "&targetDownDivId="+targetDownDivId ;
			url = ZHSQ_EVENT_URL + '/zhsq/map/labelLocation/labelLocationController/outPlatCrossDomain.jhtml?' + data;
		} else if ('006' == mapEngineType) { // 星云-高德引擎
			data = data + "&callBackUrl=" + ZHSQ_EVENT_URL + _LabelLocation.sys.newMapCallBackUrl  + "&targetDownDivId="+targetDownDivId ;
			url = ZHSQ_EVENT_URL+'/zhsq/map/arcgis/arcgis/spgisCrossDomain.jhtml?' + data;
		} else {
			data = data + "&callBackUrl=" + ZHSQ_ZZGRID_URL + _LabelLocation.sys.oldMapCallBackUrl  + "&targetDownDivId="+targetDownDivId ;
			url = ZHSQ_ZZGRID_URL + '/zzgl/map/labelLocationController/labelLocationCrossDomain.jhtml?' + data;
		}
		target.element.attr("isopen", "0");
		$("#map_iframe"+ target.downDivId).attr("src",url);
	};

	/**
	 * 加载地图
	 * @param target
	 */
	_LabelLocation.sys.getMapLocationInfo = function(target, settings) {
		target.settings = settings;
		var bizId = target.settings._bizId;
		var moduleTypeCode = target.settings._moduleTypeCode;
		var _labelLocationType = target.settings._labelLocationType;
		var resId = target.settings._resId;
		$.ajax({
			type : "POST",
			url : _LabelLocation.sys.anoleContextPath() + '/zhsq/map/labelLocation/labelLocationController/getMapLocationInfo.json?moduleTypeCode=' + moduleTypeCode +'&bizId='+ bizId +'&labelLocationType='+ _labelLocationType+'&resId='+ resId + '&t=' + Math.random(),
			dataType : "json",
			async : false,
			success : function(data) {
				if(data != null && typeof(data) != 'undefined'){
					if(data.x != null && data.x != ""){
						target.settings._resLocationX = data.x;
						$('#x').val(data.x);
					}
					if(data.y != null && data.y != ""){
						target.settings._resLocationY = data.y;
						$('#y').val(data.y);
					}
					if(data.mapt != null && data.mapt != ""){
						target.settings._resLocationMapType = data.mapt;
						$('#mapt').val(data.mapt);
					}

				}
			},
			error : function(data) {
				$.messager.alert('错误', '无法获取该业务的标注信息！', 'error');
			}
		});

	};


	/**
	 * 公共渲染方式
	 * @param target
	 * @param index
	 * @param ctrlType
	 * @param settings
	 * @param element
	 * @private
	 */
	_LabelLocation.sys._renderForComm = function(target, index, ctrlType, settings, element) {
		target.settings = settings;
		target.element = element;
		target._ctrl_name = ctrlType;
		target._ctrl_index = index;
		target._isIn = false;
		target.epk = _LabelLocation.sys.getEpk(element);
		target.downDivId = target._ctrl_name + "_" + target.epk + "_div_" + target._ctrl_index;
		var _bodyHtml = '<div id="' + target.downDivId + '" style="overflow:hidden">';
		var _hiddenHtml = '<input id="x" name="x" type="hidden"  value=""/>';
		_hiddenHtml = _hiddenHtml + '<input id="y" name="y" type="hidden"  value=""/>';
		_hiddenHtml = _hiddenHtml + '<input id="moduleTypeCode" name="moduleTypeCode" type="hidden"  value=""/>';
		_hiddenHtml = _hiddenHtml + '<input id="mapt" name="mapt" type="hidden"  value=""/>';
		_hiddenHtml = _hiddenHtml + '<input id="_resId" name="_resId" type="hidden"  value=""/>';
		_hiddenHtml = _hiddenHtml + '<input id="_labelLocationType" name="_labelLocationType" type="hidden"  value=""/>';
		var mapTabHtml = "";
		if(target.settings._resLocationX != null && target.settings._resLocationX != ""
			&& target.settings._resLocationY != null && target.settings._resLocationY != ""){
			if( typeof(target.settings._isEdit) != 'undefined' && target.settings._isEdit != null && target.settings._isEdit == "true"){
				mapTabHtml = "修改地理位置";
			}else{
				mapTabHtml = "查看地理位置";
			}

		}else{
			if( typeof(target.settings._isEdit) != 'undefined' && target.settings._isEdit != null && target.settings._isEdit == "true"){
				mapTabHtml = "标注地理位置";
			}else{
				mapTabHtml = "未标注地理位置";
			}
		}
		target.element.context.innerHTML = '<span class="Check_Radio mapTab2"><b id="mapTab_'+ target.downDivId +'">'+ mapTabHtml +'</b></span>';
		var _toolBarHtml = "";
		var _listHtml = "";
		var _operationBarHtml = "";
		if(target.settings._isEdit == 'true'){
			_operationBarHtml = '<div style="overflow:hidden" class="Check_Radio">';
		}

		var showTypesArray = target.settings._showTypes.split(",");

		for(var i=0;i<showTypesArray.length;i++){
			if(showTypesArray[i] == "addressLabelLocation" && target.settings._isEdit == 'true' && target.settings._isHaveAddressListDiv == 0){ //非详情页面才有部件选择面板
				_operationBarHtml = _operationBarHtml + '<input type="radio" style="margin-left:5px;" id="address_radio_'+ target.downDivId +'" name="labelLocationType_'+ target.downDivId +'" value="addressLabelLocation" /> 按部件地理位置';
				_toolBarHtml = _toolBarHtml + '<div id="address_searchPane_'+ target.downDivId +'" style="height:45px;width:390px;" class="MainContent datagrid-toolbar">' +
					'<div class="ConSearch">' +
					'<div class="fl">' +
					'<ul>' +
					'<li>所属区域：</li>' +
					'<li>' +
					'<input name="address_infoOrgCode_'+ target.downDivId +'" id="address_infoOrgCode_'+ target.downDivId +'" type="hidden" value="'+ target.settings._defaultInfoOrgCode +'"/>' +
					'<input name="address_gridId_'+ target.downDivId +'" id="address_gridId_'+ target.downDivId +'" type="hidden" value="'+ target.settings._defaultGridId +'"/>' +
					'<input name="address_gridName_'+ target.downDivId +'" id="address_gridName_'+ target.downDivId +'" type="text" class="inp1" style="width:80px;" value="'+ target.settings._defaultGridName +'"/>' +
					'</li>' +
					'<li><input name="address_searchCondition_'+ target.downDivId +'" id="address_searchCondition_'+ target.downDivId +'" type="text" class="inp1" style="color:gray; width:180px;" onfocus="if(this.value==\'请输入部件编号查询\'){this.value=\'\';}$(this).attr(\'style\',\'width:180px;\');" onblur="if(this.value==\'\'){$(this).attr(\'style\',\'color:gray;width:180px;\');this.value=\'请输入部件编号查询\';}" value="请输入部件编号查询"></input></li>' +
					'</ul>' +
					'</div>' +
					'</div>' +
					'</div>';
				_listHtml = _listHtml + '<div id="address_ListDiv_'+ target.downDivId +'" style="height:305px;width:365px"><table id="address_list_' + target.downDivId + '"></table></div></div>';
				target.settings._isHaveAddressListDiv = 1;
			}
			if((showTypesArray[i] == "mapLabelLocation" || target.settings._isEdit == 'false') && target.settings._isHaveMapListDiv == 0){//详情页面和有配置的新增编辑页面都有地图面板
				if(target.settings._isEdit == 'true'){
					_operationBarHtml = _operationBarHtml + '<input type="radio" style="margin-left:5px;" id="map_radio_'+ target.downDivId +'" name="labelLocationType_'+ target.downDivId +'" value="mapLabelLocation" /> 标注地理位置';
				}
				_toolBarHtml = _toolBarHtml + '<div id="map_labelLocationPane_'+ target.downDivId +'" style="height:300px;width:425px;overflow:hidden;padding:0px 0px 0px 0px"  region="center" border="false" class="MainContent datagrid-toolbar">' +
				'<iframe data-iframe="true"  src="" style="width:100%;height:100%;" id="map_iframe'+ target.downDivId +'" frameborder="0" allowtransparency="true"/>' +
				'</div>';
				target.settings._isHaveMapListDiv = 1;
			}
			if(showTypesArray[i] == "buildingLabelLocation" && target.settings._isEdit == 'true' && target.settings._isHaveBuildingListDiv == 0){//非详情页面才有楼宇选择面板
				_operationBarHtml = _operationBarHtml + '<input type="radio" style="margin-left:5px;" id="building_radio_'+ target.downDivId +'" name="labelLocationType_'+ target.downDivId +'" value="buildingLabelLocation" /> 按楼宇位置';
				_toolBarHtml = _toolBarHtml + '<div id="building_searchPane_'+ target.downDivId +'" style="height:45px;width:390px;" class="MainContent datagrid-toolbar">' +
					'<div class="ConSearch">' +
					'<div class="fl">' +
					'<ul>' +
					'<li>所属区域：</li>' +
					'<li>' +
					'<input name="building_infoOrgCode_'+ target.downDivId +'" id="building_infoOrgCode_'+ target.downDivId +'" type="hidden" value="'+ target.settings._defaultInfoOrgCode +'"/>' +
					'<input name="building_gridId_'+ target.downDivId +'" id="building_gridId_'+ target.downDivId +'" type="hidden" value="'+ target.settings._defaultGridId +'"/>' +
					'<input name="building_gridName_'+ target.downDivId +'" id="building_gridName_'+ target.downDivId +'" type="text" class="inp1" style="width:80px;" value="'+ target.settings._defaultGridName +'"/>' +
					'</li>' +
					'<li><input name="building_searchCondition_'+ target.downDivId +'" id="building_searchCondition_'+ target.downDivId +'" type="text" class="inp1" style="color:gray; width:180px;" onfocus="if(this.value==\'请输入楼宇名称查询\'){this.value=\'\';}$(this).attr(\'style\',\'width:180px;\');" onblur="if(this.value==\'\'){$(this).attr(\'style\',\'color:gray;width:180px;\');this.value=\'请输入楼宇名称查询\';}" value="请输入楼宇名称查询"/></li>' +
					'</ul>' +
					'</div>' +
					'</div>' +
					'</div>';
				_listHtml = _listHtml + '<div id="building_ListDiv_'+ target.downDivId +'" style="height:305px;width:365px"><table id="building_list_' + target.downDivId + '"></table></div></div>';
				target.settings._isHaveBuildingListDiv = 1;
			}
			if(showTypesArray[i] == "addMapLabelLocation" && target.settings._isEdit == 'true' && target.settings._isHaveAddMapListDiv == 0){//详情页面和有配置的新增编辑页面都有地图面板
				if(target.settings._isEdit == 'true'){
					_operationBarHtml = _operationBarHtml + '<input type="radio" style="margin-left:5px;" id="addMap_radio_'+ target.downDivId +'" name="labelLocationType_'+ target.downDivId +'" value="addMapLabelLocation" /> 标注部件位置';
				}
				_toolBarHtml = _toolBarHtml + '<div id="addMap_labelLocationPane_'+ target.downDivId +'" style="height:300px;width:425px;overflow:hidden;padding:0px 0px 0px 0px"  region="center" border="false" class="MainContent datagrid-toolbar">' +
				'<iframe data-iframe="true"  src="" style="width:100%;height:100%;" id="addMap_iframe'+ target.downDivId +'" frameborder="0" allowtransparency="true"/>' +
				'</div>';
				target.settings._isHaveAddMapListDiv = 1;
			}
		}
		if(target.settings._isEdit == 'true'){
			//_operationBarHtml = _operationBarHtml + "</div>"
		}

		_bodyHtml = _hiddenHtml + _bodyHtml + _operationBarHtml + _toolBarHtml + _listHtml;


		$('body').append(_bodyHtml);
		// div持有者
		target.divHolder = $("#" + target.downDivId).hide();
		$('#moduleTypeCode').val(target.settings._moduleTypeCode);

		$("input:radio[name='labelLocationType_"+ target.downDivId +"']").each(function(i,val){
			$(this).click(function() {
				if(val.value == "addressLabelLocation"){
					$('#address_list_'+target.downDivId).css("display","block");
					$('#address_searchPane_'+target.downDivId).css("display","block");
					$('#address_ListDiv_'+target.downDivId).css("display","block");
					$('#building_list_'+target.downDivId).css("display","none");
					$('#building_searchPane_'+target.downDivId).css("display","none");
					$('#building_ListDiv_'+target.downDivId).css("display","none");
					$('#map_labelLocationPane_'+target.downDivId).css("display","none");
					$('#addMap_labelLocationPane_'+target.downDivId).css("display","none");
					$("#address_radio_"+target.downDivId).attr("checked","checked");

					if(target.settings._isAddressFirstClick == 1){
						_LabelLocation.sys.gotoSearch(target, "address");
						var search = $('#address_searchCondition_'+target.downDivId).val();
						_LabelLocation.sys.getAddressListData(target, search);
						target.settings._isAddressFirstClick = target.settings._isAddressFirstClick + 1;
					}

				}else if(val.value == "mapLabelLocation"){
					$('#address_list_'+target.downDivId).css("display","none");
					$('#address_searchPane_'+target.downDivId).css("display","none");
					$('#address_ListDiv_'+target.downDivId).css("display","none");
					$('#building_list_'+target.downDivId).css("display","none");
					$('#building_searchPane_'+target.downDivId).css("display","none");
					$('#building_ListDiv_'+target.downDivId).css("display","none");
					$('#addMap_labelLocationPane_'+target.downDivId).css("display","none");
					$('#map_labelLocationPane_'+target.downDivId).css("display","block");
					$("#map_radio_"+target.downDivId).attr("checked","checked");

					if(target.settings._isMapFirstClick == 1){
						_LabelLocation.sys.loadMapInfo(target);
						target.settings._isMapFirstClick = target.settings._isMapFirstClick + 1;
					}
				}else if(val.value == "buildingLabelLocation"){
					$('#address_list_'+target.downDivId).css("display","none");
					$('#address_searchPane_'+target.downDivId).css("display","none");
					$('#address_ListDiv_'+target.downDivId).css("display","none");
					$('#building_list_'+target.downDivId).css("display","block");
					$('#building_searchPane_'+target.downDivId).css("display","block");
					$('#building_ListDiv_'+target.downDivId).css("display","block");
					$('#map_labelLocationPane_'+target.downDivId).css("display","none");
					$('#addMap_labelLocationPane_'+target.downDivId).css("display","none");
					$("#building_radio_"+target.downDivId).attr("checked","checked");

					if(target.settings._isBuildingFirstClick == 1){
						_LabelLocation.sys.gotoSearch(target, "building");
						var search = $('#building_searchCondition_'+target.downDivId).val();
						_LabelLocation.sys.getBuildingListData(target, search);
						target.settings._isBuildingFirstClick = target.settings._isBuildingFirstClick + 1;
					}
				}else if(val.value == "addMapLabelLocation"){
					$('#address_list_'+target.downDivId).css("display","none");
					$('#address_searchPane_'+target.downDivId).css("display","none");
					$('#address_ListDiv_'+target.downDivId).css("display","none");
					$('#building_list_'+target.downDivId).css("display","none");
					$('#building_searchPane_'+target.downDivId).css("display","none");
					$('#building_ListDiv_'+target.downDivId).css("display","none");
					$('#map_labelLocationPane_'+target.downDivId).css("display","none");
					$('#addMap_labelLocationPane_'+target.downDivId).css("display","block");
					$("#addMap_radio_"+target.downDivId).attr("checked","checked");

					if(target.settings._isMapFirstClick == 1){
						_LabelLocation.sys.loadMapInfo(target);
						target.settings._isAddMapFirstClick = target.settings._isMapFirstClick + 1;
					}
				}
			});
		});


		/* ========================================== *
		 * ------ 下拉按钮相关事件 ------					  *
		 * ========================================== */
		target.btnImgOverEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target._setBackGround(target.settings.DropButton.BINOpenHoverPath);
			} else {				// 打开
				target._setBackGround(target.settings.DropButton.BIYOpenHoverPath);
			}
		};
		target.btnImgOutEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target._setBackGround(target.settings.DropButton.BINOpenPath);
			} else {				// 打开
				target._setBackGround(target.settings.DropButton.BIYOpenPath);
			}
		};
		
		// TODO: 重复定位DIV位置
		target._repeatPosition = function(hDoc, hDiv, maxH, minH) {
			target._top = target.element.offset().top;
			var hElement = target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w;
			var _top = target._top + hElement;
			var hTop = target._top;
			var hBtm = hDoc - _top;
			var hHalf = (hTop + hBtm) / 2;
			var _divContentHeight = hDiv - 100;
			target.divHolder.find('.con').css({'height' : _divContentHeight});
			if (hHalf >= hDiv) {// 文档扣除元素高度的一半不小于DIV高度
				if (hBtm >= hDiv) {
					target.divHolder.css({ "top" : _top + "px" });
				} else {
					target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
				}
			} else {
				var _maxH = 0;
				if (hBtm + 30 >= hTop) {
					if (hBtm >= hDiv) {
						if (hBtm > maxH) {
							_maxH = maxH;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						} else {
							_maxH = hBtm - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						}
						target.divHolder.css({ "top" : _top + "px" });
					} else {
						if (!target.settings.DownDiv.IsAutoStyle) {
							_maxH = hBtm - 3;
							target.divHolder.css({ "top" : _top + "px", "max-height" : _maxH + "px" });
						}
					}
				} else {
					if (hTop >= hDiv) {
						if (hTop > maxH) {
							_maxH = maxH;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						} else {
							_maxH = hTop - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						}
						hDiv = target.divHolder.outerHeight(true);
						target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
					} else {
						if (!target.settings.DownDiv.IsAutoStyle) {
							_maxH = hTop - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
							hDiv = target.divHolder.outerHeight(true);
							target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
						}
					}
				}
				if (minH >= _maxH) {
					target.divHolder.css({ "min-height" : _maxH + "px" });
				} else {
					target.divHolder.css({ "min-height" : minH + "px" });
				}
			}
		};
		
		// 初始化文本框下拉按钮样式
		target.initTextField = function() {
			target.element.attr("isopen", "0");
			target.element.attr('readonly', 'readonly');
			target.element.css({ "cursor" : "pointer" });
			target._setBackGround(target.settings.DropButton.BINOpenPath);

			var showTypesArray = target.settings._showTypes.split(",");

			/*===========================
			 *  目标文本框点击事件
			 *===========================*/
			target.element.click(function(e) {
				target.isBreak = true;
				$(document).click();
				e.stopPropagation();
				target.isBreak = false;
				target._showOrHideDiv(e);
				
				if(showTypesArray != null && showTypesArray != "" && showTypesArray.length > 0){
					if(showTypesArray[0] == "addressLabelLocation" && target.settings._isEdit == 'true'){
						$('#address_list_'+target.downDivId).css("display","block");
						$('#address_searchPane_'+target.downDivId).css("display","block");
						$('#address_ListDiv_'+target.downDivId).css("display","block");
						$('#building_list_'+target.downDivId).css("display","none");
						$('#building_searchPane_'+target.downDivId).css("display","none");
						$('#building_ListDiv_'+target.downDivId).css("display","none");
						$('#map_labelLocationPane_'+target.downDivId).css("display","none");
						$("#address_radio_"+target.downDivId).attr("checked","checked");
						_LabelLocation.sys.gotoSearch(target, "address");
						var searchStr = $('#address_searchCondition_' + target.downDivId).val();
						_LabelLocation.sys.getAddressListData(target, searchStr);
					} else if(showTypesArray[0] == "buildingLabelLocation" && target.settings._isEdit == 'true'){
						$('#address_list_'+target.downDivId).css("display","none");
						$('#address_searchPane_'+target.downDivId).css("display","none");
						$('#address_ListDiv_'+target.downDivId).css("display","none");
						$('#building_list_'+target.downDivId).css("display","block");
						$('#building_searchPane_'+target.downDivId).css("display","block");
						$('#building_ListDiv_'+target.downDivId).css("display","block");
						$('#map_labelLocationPane_'+target.downDivId).css("display","none");
						$("#building_radio_"+target.downDivId).attr("checked","checked");
						_LabelLocation.sys.gotoSearch(target, "building");
						var searchStr = $('#building_searchCondition_' + target.downDivId).val();
						_LabelLocation.sys.getBuildingListData(target, searchStr);
					} else if(showTypesArray[0] == "mapLabelLocation" || target.settings._isEdit == 'false'){
						$('#address_list_'+target.downDivId).css("display","none");
						$('#address_searchPane_'+target.downDivId).css("display","none");
						$('#address_ListDiv_'+target.downDivId).css("display","none");
						$('#building_list_'+target.downDivId).css("display","none");
						$('#building_searchPane_'+target.downDivId).css("display","none");
						$('#building_ListDiv_'+target.downDivId).css("display","none");
						$('#map_labelLocationPane_'+target.downDivId).css("display","block");
						$("#map_radio_"+target.downDivId).attr("checked","checked");
						_LabelLocation.sys.loadMapInfo(target);
					}
				}
			});

			for(var i=0;i<showTypesArray.length;i++){
				if(showTypesArray[i] == "addressLabelLocation" && target.settings._isEdit == 'true'){
					//获取当前登录组织的网格信息
					_LabelLocation.sys._getDefaultGridInfoAndStartGrid(target, "address");
				}
				if(showTypesArray[i] == "buildingLabelLocation" && target.settings._isEdit == 'true'){
					//获取当前登录组织的网格信息
					_LabelLocation.sys._getDefaultGridInfoAndStartGrid(target, "building");
				}
			}


			target.element.keyup(function(e) {
				var showTypesArray = target.settings._showTypes.split(",");
				if(showTypesArray != null && showTypesArray != "" && showTypesArray.length > 0){
					if(showTypesArray[0] == "addressLabelLocation" && target.settings._isEdit == 'true'){
						$('#address_list_'+target.downDivId).css("display","block");
						$('#building_list_'+target.downDivId).css("display","none");
						_LabelLocation.sys.gotoSearch(target, "address");
						var searchStr = $('#address_searchCondition_' + target.downDivId).val();
						_LabelLocation.sys.getAddressListData(target, searchStr);
					} else if(showTypesArray[0] == "buildingLabelLocation" && target.settings._isEdit == 'true'){
						$('#address_list_'+target.downDivId).css("display","none");
						$('#building_list_'+target.downDivId).css("display","block");
						_LabelLocation.sys.gotoSearch(target, "building");
						var searchStr = $('#building_searchCondition_' + target.downDivId).val();
						_LabelLocation.sys.getBuildingListData(target, searchStr);
					}
				}
			});
			
			target.element.hover(function(e) {
				target._isIn = true;
			}, function(e) {
				target._isIn = false;
			});
			target.divHolder.hover(function(e) {
				target._isIn = true;
			}, function(e) {
				target._isIn = false;
			});
		};
		/* ==========================================
		 * ------ tree/list div ------
		 * =========================================*/
		target.divHolder.click(function(e) {
			//e.stopPropagation();
		});
		// 初始化下拉div样式
		target._initDownDiv = function(defWidth, defHeight) {
			target._border_top_w = 1;//parseInt(target.element.css('border-top-width')) || 0;
			target._border_left_w = 1;//parseInt(target.element.css('border-left-width')) || 0;
			target._border_right_w = 1;//parseInt(target.element.css('border-right-width')) || 0;
			target._border_bottom_w = 1;//parseInt(target.element.css('border-bottom-width')) || 0;
			var whiteHeight = target.element.outerHeight(true) - (target.element.innerHeight() + target._border_top_w + target._border_bottom_w);
			target._margin_top_w = whiteHeight / 2;
			target._margin_bottom_w = whiteHeight / 2;
			var whiteWidth = target.element.outerWidth(true) - (target.element.innerWidth() + target._border_left_w + target._border_right_w);
			target._margin_left_w = whiteWidth / 2;
			target._margin_right_w = whiteWidth / 2;
			target._top = target.element.offset().top - target._margin_top_w;
			target._left = target.element.offset().left - target._margin_left_w;
			right =  $(document).width() - target._left;
			if(right < 465){
				target._left = target._left - (465 - target.element.width());
			}else{
				target._left = target._left + target._margin_left_w;
			}
			target._background_color = 'rgb(255, 255, 255)';//target.element.css("background-color");
			var _css = {
				"position" : "absolute",
				"max-height" : "305px",
				"left" : target._left + "px",
				"top" : target._top + target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w + 1 + "px",
				"background-color" : target._background_color,
				"z-index" : 79002,
				"border-top-width" : target._border_top_w + "px",
				"border-top-color" : "#7ecef4",
				"border-top-style" : target.element.css("border-top-style") == "none" ? "solid" : target.element.css("border-top-style"),
				"border-left-width" : target._border_left_w + "px",
				"border-left-color" : "#7ecef4",
				"border-left-style" : target.element.css("border-left-style") == "none" ? "solid" : target.element.css("border-left-style"),
				"border-right-width" : target._border_right_w + "px",
				"border-right-color" : "#7ecef4",
				"border-right-style" : target.element.css("border-right-style") == "none" ? "solid" : target.element.css("border-right-style"),
				"border-bottom-width" : target._border_right_w + "px",
				"border-bottom-color" : "#7ecef4",
				"border-bottom-style" : target.element.css("border-right-style") == "none" ? "solid" : target.element.css("border-right-style")
			};
			target.divHolder.css(_css);
		};
		
		// 显示或隐藏DIV函数
		target._showOrHideDiv = function(event, type, iReset) {
			target._initDownDiv();
			if (target.isBreak == true) return;
			if (event != null && typeof type == "undefined") {
				var e = event.target || event.srcElement;
				if (target.element.attr("isopen") == "0") {// 当前为关闭状态
					// 关闭所有打开状态
					target.element.attr("isopen", "1");
					target._setBackGround(target.settings.DropButton.BIYOpenHoverPath);
					target._background_color = target.element.css("background-color");
					target.divHolder.css({ "background-color" : 'rgb(255, 255, 255)' });
					target._showOrHideDivEx(true);
				} else {
					target.element.attr("isopen", "0");
					target._setBackGround(target.settings.DropButton.BINOpenHoverPath);
					target._showOrHideDivEx(false);
				}
			} else {
				if (type == "show") {
					target.element.attr("isopen", "1");
					if (iReset) {
						target._setBackGround(target.settings.DropButton.BIYOpenPath);
					}
					target._showOrHideDivEx(true);
				} else {
					target.element.attr("isopen", "0");
					if (iReset) {
						target._setBackGround(target.settings.DropButton.BINOpenPath);
					}
					target._showOrHideDivEx(false);
				}
			}
		};
		
		target._setBackGround = function(imgPath) {
			if (imgPath != null && imgPath.length > 0) {
				target.element.css("background", "url(" + target.settings.ContextPath + _LabelLocation.sys.path + imgPath + ") no-repeat right");
				if (target.element.hasClass("validatebox-invalid")) {
					target.element.css("background-color", target.element.val() == '' ? target.settings.ShowOptions.ErrorBgColor : "#fff");
				} else {
					target.element.css("background-color", "#fff");
				}
			}
		};
		
		target._showOrHideDivEx = function(isShow) {
			if (isShow) {
				var hDoc = $(document).height();
				var maxH = parseInt(target.divHolder.css("max-height")) || 0;
				var minH = parseInt(target.divHolder.css("min-height")) || 0;
				target._repeatPosition(hDoc, target.divHolder.outerHeight(true), maxH, minH);
				target.divHolder.show();
				if (target.settings.ShowOptions.EnableToolbar) {
					target.divCleaner.show();
				}
				target.timerId = window.setInterval(function() {
					target._repeatPosition(hDoc, target.divHolder.outerHeight(true), maxH, minH);
				}, 500);
			} else {
				target.divHolder.hide();
				if (target.settings.ShowOptions.EnableToolbar) {
					target.divCleaner.hide();
				}
				window.clearInterval(target.timerId);
			}
		};

		/**
		 * 对象初始化函数
		 * @private
		 */
		target._init = function() {
			target.initTextField();
			/* ==============================================
			 * 显示div并且给document绑定一个click事件，监听鼠标移动事件。
			 * so页面事件尽量不要阻止事件冒泡。
			 * ==============================================*/
			$(document).bind("click", function(_event) {
				var _e = _event.target || _event.srcElement;
				if (!target._isIn) {
					target._showOrHideDiv(null, "hide", true);
				}
			});
			/* ==============================================
			 * 显示div并且给document绑定一个blur事件，监听事件。
			 * ==============================================*/
			$(document).bind("blur", function(_event) {
				var _e = _event.target || _event.srcElement;
				target._showOrHideDiv(null, "hide", true);
			});
			
		};
	};
	
	_LabelLocation.sys.getEpk = function(e) {
		return e.get(0).tagName + e.attr("id");
	};
	
	// epk : element 主键
	_LabelLocation.sys.hideAll = function(epk) {
		$("input[anole='" + _LabelLocation.sys.version + "']").each(function(index, element) {
			var api = $(element).anoleAddressApi(true);
			api._showOrHideDiv(null, "hide", epk != api.epk);
		});
	};
	

	_LabelLocation.sys._loadCss = function(cssName) {

		var jsPath = _LabelLocation.sys._getJsPath();
		document.write('<link rel="stylesheet" type="text/css" href="' + jsPath + cssName + '"/>');
	};

	/**
	 * 加载网格树所需文件
	 * @param _sys_domain
	 */
	_LabelLocation.sys.loadScript = function() {
		$('body').append('<script type="text/javascript">');
		$('body').append('	function mapMarkerSelectorCallback(x, y, mapt){');
		$('body').append('		console.log("1");');
		$('body').append('		$("#x").val(x);');
		$('body').append('		$("#y").val(y);');
		$('body').append('		$("#mapt").val(mapt);');
		$('body').append('	}');
		$('body').append('</script>');




		/**** 加载网格所需要的文件
		var arrScript = document.getElementsByTagName("script");
		var anoleComboboxIndex = -1;
		var anoleComboboxApiIndex = -1;
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			if(anoleComboboxIndex < 0){
				anoleComboboxIndex = src.indexOf('jquery.anole.combobox.js');
			}
			if (anoleComboboxApiIndex < 0) {
				anoleComboboxApiIndex = src.indexOf('anole.combobox.api.js');
			}
		}

		if(anoleComboboxIndex < 0){
			document.write('<script type="text/javascript" src="'+ _sys_domain +'/js/components/combobox/jquery.anole.combobox.js"></script>');
		}

		if (anoleComboboxApiIndex < 0) {
			document.write('<script type="text/javascript" src="'+ _sys_domain +'/js/components/combobox/anole.combobox.api.js"></script>');
		}

		var arrCss = document.getElementsByTagName("link");
		var zTreeStyleIndex = -1;
		var anole_comboboxIndex = -1;
		for ( var i = 0; i < arrCss.length; i++) {
			var href = arrCss[i].href;
			if(zTreeStyleIndex < 0){
				zTreeStyleIndex = href.indexOf('zTreeStyle.css');
			}
			if (anole_comboboxIndex < 0) {
				anole_comboboxIndex = href.indexOf('anole_combobox.css');
			}
		}

		if(zTreeStyleIndex < 0){
			document.write('<link rel="stylesheet" type="text/css" href="'+ _sys_domain +'/js/components/combobox/css/zTreeStyle.css" />');
		}

		if (anole_comboboxIndex < 0) {
			document.write('<link rel="stylesheet" type="text/css" href="'+ _sys_domain +'/js/components/combobox/css/anole_combobox.css" />');
		}
		******/
	};
	
	_LabelLocation.sys.anoleContextPath = function() {
		var jsPath = _LabelLocation.sys._getJsPath();
		jsPath = jsPath.replace(_LabelLocation.sys.path, '');
		return jsPath;
	};


	/**
	 * 初始化网格树
	 * @param target
	 * @param idStr
	 * @private
	 */
	_LabelLocation.sys._getDefaultGridInfoAndStartGrid = function(target, idStr) {
		var gridInfoObj = {};
		var src = "";
		var jsPath = target.settings.ContextPath;
		src = jsPath + "zhsq/map/labelLocation/labelLocationController/getDefaultGridInfoAndStartGrid.jhtml?jsoncallback=?";
		$.ajax({
			type : "POST",
			url : src,
			contentType : "application/json; charset=utf-8",
			dataType : "jsonp",
			async : false,
			success : function(data) {
				gridInfoObj = data;
				$("#" + idStr + "_infoOrgCode_"+ target.downDivId +"").val(gridInfoObj.defaultInfoOrgCode);
				$("#" + idStr + "_gridId_"+ target.downDivId +"").val(gridInfoObj.defaultGridId);
				$("#" + idStr + "_gridName_"+ target.downDivId +"").val(gridInfoObj.defaultGridName);
				target.settings._startGridId = gridInfoObj.defaultGridId;

				AnoleApi.initGridZtreeComboBox("" + idStr + "_gridName_"+ target.downDivId +"", "" + idStr + "_gridId_"+ target.downDivId +"", function (gridId, items){
					if(items!=undefined && items!=null && items.length>0){
						var grid = items[0];
						$('#'+ idStr +'_list_' + target.downDivId).datagrid("loading","正在查询，请稍候。。。");
						target.settings._defaultGridId = grid.gridId;
						target.settings._defaultGridName = grid.name;
						//target.settings._defaultInfoOrgCode = grid.orgCode;

						//$("#" + idStr + "_infoOrgCode_"+ target.downDivId +"").val(grid.orgCode);

						var searchStr = $('#'+ idStr +'_searchCondition_' + target.downDivId).val();
						if(searchStr != null && searchStr != '' && typeof(searchStr) != 'undefined'){
							if(searchStr.indexOf('请输入') >= 0){
								searchStr = "";
							}
						}
						if(idStr == "address"){
							_LabelLocation.sys.getAddressListData(target, searchStr);
						}
						if(idStr == "building"){
							_LabelLocation.sys.getBuildingListData(target, searchStr);
						}
					}
				}, {
					Async : {
						enable : true,
						autoParam : [ "id=gridId" ],
						dataFilter : _filter,
						otherParam : {
							"startGridId" : target.settings._startGridId
						}
					}
				});
			}
		});
	}
	
	//获取ContextPath上下文路径
	_LabelLocation.sys._getJsPath = function() {
		// 取得组件包的绝对路径(假设和jquery.anole.combobox.js部署在同一个目录)
		var jsPath = "";
		var arrScript = document.getElementsByTagName("script");
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			var index = src.indexOf('jquery.anole.labelLocation.js');
			if (index >= 0) {
				jsPath = src.substring(0, index);
				if (jsPath.charAt(0) == '/') {
					src = location.href;
					index = src.indexOf('//');
					if (index != -1) {
						index = src.indexOf('/', index + 2);
						if (index != -1)
							src = src.substring(0, index);
						jsPath = src + jsPath;
					}
				}
				break;
			}
		}
		return jsPath;
	}
	
	$.fn.labelLocationApi = function(isInner) {
		var epk = _LabelLocation.sys.getEpk($(this));
		if (isInner) {
			return _LabelLocation.renderObj[epk];
		} else {
			return {
				//getStartDate : _LabelLocation.renderObj[epk].getStartDate,
				//getEndDate : _LabelLocation.renderObj[epk].getEndDate,
			};
		}
	};
	
	$.fn.labelLocationRender = function(options) {
		var settings = $.extend(true, {}, _LabelLocation.settings);
		if (settings.ContextPath == null) {
			settings.ContextPath = _LabelLocation.sys.anoleContextPath();
		}
		if (typeof options.ShowOptions != "undefined") {
			$.extend(settings.ShowOptions, options.ShowOptions);
			delete options.ShowOptions;
		}
		if (typeof options.DropButton != "undefined") {
			$.extend(settings.DropButton, options.DropButton);
			delete options.DropButton;
		}
		if (typeof options.DownDiv != "undefined") {
			$.extend(settings.DownDiv, options.DownDiv);
			delete options.DownDiv;
		}
		$.extend(settings, options);
		if(options != null && typeof(options._sys_domain) != 'undefined' && options._sys_domain != null  && options._sys_domain != "" ){
			//_LabelLocation.sys.loadScript(options._sys_domain);
		}
		if(typeof(settings._showTypes) == "undefined" || settings._showTypes == null || settings._showTypes == ""){
			return ;
		}
		return this.each(function (index, element) {
			try {
				if (settings.ContextPath == null) {
					throw new Error("请参照文档正确配置【ContextPath】参数！");
				} else if (settings.ContextPath.length > 0) {
					settings.ContextPath += "/";
				}
				var e = $(element);
				var epk = _LabelLocation.sys.getEpk(e);
				e.attr("anole", _LabelLocation.sys.version);
				var obj = new _LabelLocation.sys.renderForLabelLocation(index, _LabelLocation.sys.type, settings, e);
				obj._init();
				if (obj != null) {
					_LabelLocation.renderObj[epk] = obj;
				}
			} catch (e) {
				alert(e.message);
				return false;
			}
		});
	};

	_LabelLocation.sys._loadCss("css/labelLocation.css");
	_LabelLocation.sys.loadScript();

})(jQuery);