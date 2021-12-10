var currentType;
var myInterval = null;
$(function(){

    $('.sjmenu-an li').click(function(){
        var t = $(this).attr('id');
        var type = t.substring(0,t.length-3);
        $('.left_content1').hide();
        $('.left_content2').hide();
        $('.left_content3').hide();

        $('.sjmenu-an li').removeClass('sjmenu-current');
        $(this).addClass('sjmenu-current');

        if(type == currentType){
            currentType = '';
            $('.sjmenu-yjcd').hide();
            if(type=='gridadmin'||type=='eye'){
                removetype(type);
            }
        }else{
            currentType = type;
            $('.sjmenu-yjcd').show();
            if(t=='team_li'){
                $('.left_content1').show();
            }
            if(t=='gridadmin_li'){
                $('.left_content2').show();
                addlabtop(type);
            }
            if(t=='eye_li'){
                $('.left_content3').show();
                addlabtop(type);
            }
        }
        gisZhouBian();
    });

    $('.rangebox li').click(function(){
        var t = $(this).attr('id');
        var type = t.substring(0,t.length-3);
        //alert($(this).children('div').hasClass('sjdw-xz'));
        if($(this).children('div').hasClass('sjdw-xz')){
            removetype(type);
            $(this).children('div').removeClass('sjdw-xz');
        }else{
            addlabtop(type);
            $(this).children('div').addClass('sjdw-xz');
        }
        gisZhouBian();
    });

    //
    $('#distance').change(function(){
        var sel = $(this).children('option:selected').val();

        gisZhouBian();
        //var ck = $('.sjmeenus, .rangebox li').hasClass('sjmenu-current');
        //if(sel == "" || !ck) return;
        //$('.sjmenu-current').each(function(i){
        //    var type = $(this).attr('id');
        //    if(type != t)
        //        showZhouBian(type);
        //});
        //showZhouBian(currentType);
    });

    //
    $('.resourceCurrent').click(function(){
        if($('.sjmenu-an').css('display')=='none'){
            $('.resourceCurrent a').html('收起');
            $('.sjmenu-an').show();
            $('.sjmenu-yjcd').show();
        }else{
            $('.resourceCurrent a').html('展开');
            $('.sjmenu-an').hide();
            $('.sjmenu-yjcd').hide();
        }
    });
});

function gisZhouBian(){
    $('.labtop label').each(function(i){
        var t = $(this).attr('id');
        if(t!='labtop1'){
            var type = t.substring(0,t.length-2);
            //console.log(type);
            showZhouBian(type);
        }
    });
    showZhouBian(currentType);
}

function showZhouBian(type){
    msgIds = new Array();
    msgTels = new Array();
    cleanZhouBianLayer();
    var distance = $('#distance').val();
    if(distance==""||x==""||x==null||y==null||y=="") return;
    toHideZhouBianSketch();
    toShowZhouBianSketch(x,y,distance);
    if(type=='gridadmin'){
        getZhouBianGridAdmin();
    }
    if(type=='eye'){
        getZhouBianEye();
    }
    if(type=='orgteam'){
        getZhouBianTeam(1);
    }
    if(type=='prevtionteam'){
        getZhouBianTeam(2);
    }
    startMyInterval();
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
    //var type = $(obj).attr('id');
	//alert(type);
    //取消勾选去除对应图层
//	alert('去除图层'+$(obj).attr('id'));
    //取消勾选删除复选标签
    if(obj=='gridadmin'){
        $("#gridadmin, #gridadmin_l").remove();
        clearSpecialLayer('gridAdminLayer');
        clearInterval(myInterval);
    }
    if(obj=='eye'){
        $("#eye, #eye_l").remove();
        clearSpecialLayer('globalEyesLayer');
    }
    if(obj=='orgteam'){
        $("#orgteam, #orgteam_l").remove();
        clearSpecialLayer('controlsafetyRanks0Layer');
        //取消勾选去除左侧选中
        $('#'+obj+'_li').children('div').removeClass('sjdw-xz');
    }
    if(obj=='prevtionteam'){
        $("#prevtionteam, #prevtionteam_l").remove();
        clearSpecialLayer('controlsafetyRanks1Layer');
        //取消勾选去除左侧选中
        $('#'+obj+'_li').children('div').removeClass('sjdw-xz');
    }
    if( $(".labtop").html()=='<label id="labtop1">您已选择：</label>'){
        $("#labtop1").remove();
    }
}

function addlabtop(type){//alert('已勾选:'+type);
    if(type!=null&&type!=''&&$('#labtop1').length<1){
        $(".labtop").append('<label id="labtop1">您已选择：</label>');
    }
    if(type=='gridadmin'&&$('#gridadmin').length<1){
        $(".labtop").append('<input id="gridadmin" checked onclick="removelab(this)" type="checkbox"/><label id="gridadmin_l" for="gridadmin" style="">网格员</label>');
    }
    if(type=='eye'&&$('#eye').length<1){
        $(".labtop").append('<input id="eye" checked onclick="removelab(this)" type="checkbox"/><label id="eye_l" for="eye" style="">全球眼</label>');
    }
    if(type=='orgteam'&&$('#orgteam').length<1){
        $(".labtop").append('<input id="orgteam" checked onclick="removelab(this)" type="checkbox"/><label id="orgteam_l" for="orgteam" style="">综治机构组织</label>');
    }
    if(type=='prevtionteam'&&$('#prevtionteam').length<1){
        $(".labtop").append('<input id="prevtionteam" checked onclick="removelab(this)" type="checkbox"/><label id="prevtionteam_l" for="prevtionteam" style="">群防群治组织</label>');
    }
}

//清除周边资源
function cleanZhouBianLayer(){
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
    $('.left_content2').html('<iframe id="gridadminiframe" width="100%" height="400px" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>');
}
//周边全球眼
function getZhouBianEye(){
    var distance = $('#distance').val();
    if(distance==""||x==""||x==null||y==null||y=="") return;
//    alert('distance-'+distance+'x-'+x+'y-'+y);
    var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType=zhouBianStatOfGlobalEyesService&suffix=_list&mapType='+currentArcgisConfigInfo.mapType+'&distance='+distance+'&x='+x+'&y='+y+'&infoOrgCode='+$("#orgCode").val();
    $('.left_content3').html('<iframe id="eyeiframe" width="100%" height="400px" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>');
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