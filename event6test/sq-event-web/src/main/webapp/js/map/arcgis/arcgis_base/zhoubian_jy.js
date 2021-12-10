var currentType;
var myInterval = null;
$(function(){
    $('#distance').change(function(){
		var v = $(this).val();
		if(v ==0){
			xmin=0,ymin=0,xmax=0,ymax=0,rings=[];
			showZhouBian("");
		}else{
			showZhouBian();
		}
    });

    //
	$("#sjmenu_show").click(function(){
		$('.sjmenu').show();
		$("#sjmenu_hide").show(); 
		$(this).hide();
	});
    $('#sjmenu_hide').click(function(){
        $('.sjmenu').hide();
		$("#sjmenu_show").show(); 
		$(this).hide(); 
    });
});
	var resourceCircleLayerNum=0;
function showCircle(x,y,distance){
	if(!distance || distance == '0'){
		return;
	}
	$("#map"+currentN).ffcsMap.addScopeCircle({x : x, y : y ,radius : distance ,imgUrl:js_ctx +"/js/map/arcgis/library/style/images/GreenShinyPin.png"},function(d){
 		resourceCircleLayerNum = d.scopeCircleCount,circle = d.layer.graphics[0].geometry;
		xmin=circle.rings[0][44][0],xmax=circle.rings[0][15][0],ymin=circle.rings[0][31][1],ymax=circle.rings[0][0][1],rings=circle.rings;//圈 外 矩形
		sxmin=circle.rings[0][52][0],sxmax=circle.rings[0][22][0],symin=circle.rings[0][38][1],symax=circle.rings[0][8][1];//圈 内 矩形
 	});
}
function hideCircle(){
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "scopeCircleLayer"+resourceCircleLayerNum});
}
function gisZhouBian(){//点资源/点事件/范围框
	cleanZhouBianLayer();
	var types = "";
    $('.labtop label').each(function(i){
        var t = $(this).attr('title');
		if(t == 'gridadmin' || t == 'eye' || t== 'zzjg' || t == 'qfqz'){
			showResPoints(t);
		}else if(t!='labtop1'){
            types += t+',';
        }
    });
    showResPoints(types);
}
var xmin=0,ymin=0,xmax=0,ymax=0,rings=[];
 var typeArr = [],typeObj = {};
function showResPoints(typeCodes){
	
	if(typeCodes=='gridadmin'){
        getZhouBianGridAdmin();
    }else if(typeCodes=='eye'){
        getZhouBianEye();
    }else if(typeCodes=="zzjg"){
    	getZhouBianTeam(1);
    }else if(typeCodes=="qfqz"){
    	getZhouBianTeam(2);
    }else{
		 if(xmin == 0 || typeCodes.length <=1){
			 return;
		 }	   
		typeArr = [],typeObj = {};
		$.ajax({
			type: "POST",
			url: js_ctx+'/zhsq/szzg/zgResourceController/listData.json?t='+Math.random(),
			data : {
				xmin : xmin,ymin : ymin,xmax : xmax,ymax : ymax,typeCodes : typeCodes.substring(0,typeCodes.length-1),
				orgCode:document.getElementById('orgCode').value},
			dataType: "json",
			success: function (data) {//后台sql 查询圈外矩形内的点
				//var coordinates=[[xmax,ymin],[xmax,ymax],[xmin,ymax],[xmin,ymin],[xmax,ymin]];
				//$.fn.ffcsMap.renderShape({"type":"polygon","coordinates":[[[xmax,ymin],[xmax,ymax],[xmin,ymax],[xmin,ymin],[xmax,ymin]]]});
				//$.fn.ffcsMap.renderShape({"type":"polygon","coordinates":[[[sxmax,symin],[sxmax,symax],[sxmin,symax],[sxmin,symin],[sxmax,symin]]]});
				var urlObj = {};
				for(var j=0,lj=data.res.length;j<lj;j++){
					var d = data.res[j];
					urlObj[d.TYPE_CODE] = {url:d.URL,width:d.WIDTH,height:d.HEIGHT,icon:d.ICON,name:d.NAME};
					typeArr.push(d.TYPE_CODE);
				}
				for(var i=0,l=data.list.length;i<l;i++){
					var d = data.list[i];
					//判断在 圈内矩形 内的点													//判断 在圈内矩形外 && 圈内 的点
					if((d.lng <= sxmax && d.lng >= sxmin && d.lat <= symax && d.lat >= symin) || pointContains(rings,{x:d.lng,y:d.lat})){
						if(!typeObj[d.resTypeCode]){							
							typeObj[d.resTypeCode] = {layerName:'layer'+d.resTypeCode,imgUrl:uiDomain+"/images/map/gisv0/special_config/images/smartCity/resource/map/"+urlObj[d.resTypeCode].icon,data:[],coordinates:[[]]};
						}
						typeObj[d.resTypeCode].data.push({coordinates:[[]],x:d.lng,y:d.lat,gridName:d.resName,_oldData:{wid:d.resTableId,elementsCollectionStr:'menuSummaryUrl_,_'+urlObj[d.resTypeCode].url}});
					}
				}
				for(var j=0,lj=typeArr.length;j<lj;j++){
					var d = typeObj[typeArr[j]];
					if(d){
						$("#map"+currentN).ffcsMap.render(d.layerName,undefined,0,undefined,d.imgUrl,30,30,undefined,true,undefined,undefined,{},false,d.data);
						$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:urlObj[typeArr[j]].width,h:urlObj[typeArr[j]].height},d.layerName,urlObj[typeArr[j]].name,getInfoDetailOnMapNew);
					}
				}
			 },
			error: function (data) {
				$.messager.alert('错误', '连接超时！', 'error');
			}
		});
	}
}

function showZhouBian(){
   // msgIds = new Array();
    //msgTels = new Array();
    cleanZhouBianLayer();
    hideCircle();
    if(distance==0||x==""||x==null||y==null||y=="") return;
    var distance = $('#distance').val();
    showCircle(x,y,distance);
	gisZhouBian();
}

function startMyInterval() {
    clearInterval(myInterval);
    var isGridAdminLi = false;
    $('.sjmenu-current').each(function(i) {
        var type = $(this).attr('id');
        if (type == 'gridadmin_li') {
            isGridAdminLi = true;
            return false;
        }
    });
    myInterval = setInterval(function() {
        if (isGridAdminLi) {
            $("#gridadminiframe")[0].contentWindow.gisPosition();
        }
    }, 5000);
}

function removelab(obj){
    var id = $(obj).attr('id');
    removetype(id);
}

function removetype(obj){
	$('#left_li_'+obj).children('div').removeClass('sjdw-xz');
	$("#top_input_"+obj).remove();
	$("#top_label_"+obj).remove();
    if( $(".labtop").html()=='<label id="labtop1">您已选择：</label>'){
        $("#labtop1").remove();
    }
	gisZhouBian();
}

function addlabtop(type){//alert('已勾选:'+type);
    if(type!=null&&type!=''&&$('#labtop1').length<1){
        $("#labtop").html('<label id="labtop1" title="labtop1">您已选择：</label>');
    }	
	 if(type!=null&&type!=''&&$("#top_input_"+type).length<1){
		$("#labtop").append("<input id='top_input_"+type+"' checked onclick=\"removetype('"+type+"')\" type='checkbox'/><label title='"+type+"' data='"+type+"' id='top_label_"+type+"'>"+top_label[type]+"</label>");
    }
}

//清除周边资源
function cleanZhouBianLayer(){
	for(var j=0,lj=typeArr.length;j<lj;j++){
		if(typeObj[typeArr[j]])
			clearSpecialLayer(typeObj[typeArr[j]].layerName);
	}
    clearSpecialLayer('controlsafetyRanks0Layer');
    clearSpecialLayer('controlsafetyRanks1Layer');
    clearSpecialLayer('gridAdminLayer');
    clearSpecialLayer('globalEyesLayer');
}
//周边网格员
function getZhouBianGridAdmin(){
    var distance = $('#distance').val();
    if(distance==""||x==""||x==null||y==null||y=="") return;
//    alert('distance-'+distance+'x-'+x+'y-'+y);
    var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType=zhouBianStatOfGridAdminService&suffix=_list&mapType='+currentArcgisConfigInfo.mapType+'&distance='+distance+'&x='+x+'&y='+y+'&infoOrgCode='+$("#orgCode").val();
    $('#left_content_gridadmin').html('<iframe id="gridadminiframe" width="100%" height="400px" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>');
}
//周边全球眼
function getZhouBianEye(){
    var distance = $('#distance').val();
    if(distance==""||x==""||x==null||y==null||y=="") return;
//    alert('distance-'+distance+'x-'+x+'y-'+y);
    var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType=zhouBianStatOfGlobalEyesService&suffix=_list&mapType='+currentArcgisConfigInfo.mapType+'&distance='+distance+'&x='+x+'&y='+y+'&infoOrgCode='+$("#orgCode").val();
    $('#left_content_eye').html('<iframe id="eyeiframe" width="100%" height="400px" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>');
}

//周边队伍
function getZhouBianTeam(bizType){
    var distance = $('#distance').val();
    if(distance==""||x==""||x==null||y==null||y=="") return;
//	alert('distance-'+distance+'x-'+x+'y-'+y);
    var zhoubianType = "";
    if(bizType == '1')
        zhoubianType = "zhouBianStatOfOrgTeamService";
    else if(bizType == '2')
        zhoubianType = "zhouBianStatOfPrvetionTeamService";
    $.ajax({
        type: "POST",
        url: js_ctx+'/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json',
        data : {
            pageNo : 1,
            pageSize : 100,
            mapType : currentArcgisConfigInfo.mapType,
            distance : distance,
            x : x,
            y : y,
            infoOrgCode : $("#orgCode").val(),
            zhoubianType : zhoubianType
        },
        dataType: "json",
        success: function (data) {
            //console.log(data);
            var list=data.rows;
            if(list && list.length>0) {
                var results = "";
                var userIds = "";
                var tels = "";
                for (var i = 0; i < list.length; i++) {
                    var val=list[i];
                    results=results+","+val.TEAM_ID;
                    if(val.MANAGER_ACCOUNT_ID!=null&&val.MANAGER_ACCOUNT_ID!="")
                        userIds=userIds+","+val.MANAGER_ACCOUNT_ID;
                    if(val.MANAGER_TEL!=""&&val.MANAGER_TEL!=null)
                        tels=tels+","+val.MANAGER_TEL;
                }
                results=results.substring(1, results.length);
                if(userIds!=""){
                    userIds=userIds.substring(1, userIds.length);
                    msgIds.push(userIds.split(','));
                }
                if(tels!=""){
                    tels=tels.substring(1, tels.length);
                    msgTels.push(tels.split(','));
                }
                if(bizType == '1')
                    gisPositionOrg(results);
                else if(bizType == '2')
                    gisPositionPrvetion(results);
            }
        },
        error: function (data) {
            $.messager.alert('错误', '连接超时！', 'error');
        }
    });
}
//地图定位群防群治
function gisPositionPrvetion(res) {
    if (res == "") {
        return;
    }
    //window.parent.clearMyLayer();
    var elementsCollectionStr = 'gdcId_,_44357,_,orgCode_,_35,_,homePageType_,_ARCGIS_STANDARD_HOME,_,smallIco_,_/images/map/gisv0/map_config/unselected/mark_controlsafetyRanks1.png,_,menuCode_,_controlsafetyRanks1,_,menuName_,_群防群治队伍,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/mark_controlsafetyRanks1.png,_,menuListUrl_,_/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfControlsafetyRanks.jhtml?bizType=1,_,menuSummaryUrl_,_/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?teamId=,_,menuLayerName_,_controlsafetyRanks1Layer,_,menuDetailUrl_,_'+gmisDomain+'/gmis/prvetionTeam/detail.jhtml?showClose=1&bizType=1&teamId=,_,menuDetailWidth_,_610,_,menuDetailHeight_,_410,_,menuSummaryWidth_,_330,_,menuSummaryHeight_,_170,_,callBack_,_showObjectList,_,';
    var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?ids="+res+"&showType=2";
    currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
    getArcgisDataOfZhuanTi(url,elementsCollectionStr,300,150);
}

// 地图定位综治机构
function gisPositionOrg(res) {
    if (res == "") {
        return;
    }
    //window.clearMyLayer();
    var elementsCollectionStr = 'gdcId_,_44358,_,orgCode_,_35,_,homePageType_,_ARCGIS_STANDARD_HOME,_,smallIco_,_/images/map/gisv0/map_config/unselected/mark_controlsafetyRanks0.png,_,menuCode_,_controlsafetyRanks0,_,menuName_,_机构队伍,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/mark_controlsafetyRanks0.png,_,menuListUrl_,_/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfControlsafetyRanks.jhtml?bizType=0,_,menuSummaryUrl_,_/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?teamId=,_,menuLayerName_,_controlsafetyRanks0Layer,_,menuDetailUrl_,_'+gmisDomain+'/gmis/prvetionTeam/detail.jhtml?showClose=1&bizType=0&teamId=,_,menuDetailWidth_,_610,_,menuDetailHeight_,_280,_,menuSummaryWidth_,_330,_,menuSummaryHeight_,_170,_,callBack_,_showObjectList,_,';
    var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?ids="+res+"&showType=2";
    currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
    getArcgisDataOfZhuanTi(url,elementsCollectionStr,300,150);
}

function pointContains(_19a,_199){//(geometry.rings,{x:d.x,y:d.y})
	var ring, _19b = false,pi, pj, _19c, j, i, pa, pal = _19a.length;
    for (pa = 0; pa < pal; pa++) {
        ring = _19a[pa];_19c = ring.length; j = 0;
        for (i = 0; i < _19c; i++) {
            j++;
            if (j === _19c) {j = 0;}
            pi = ring[i]; pj = ring[j];
            if ((pi[1] < _199.y && pj[1] >= _199.y || pj[1] < _199.y && pi[1] >= _199.y) && (pi[0] + (_199.y - pi[1]) / (pj[1] - pi[1]) * (pj[0] - pi[0]) < _199.x)) {
                _19b = !_19b;
            }
        }
    }
    return _19b;
}