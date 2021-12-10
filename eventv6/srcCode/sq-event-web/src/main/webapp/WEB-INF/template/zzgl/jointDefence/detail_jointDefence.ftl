<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信息详情</title>
<#include "/component/commonFiles-1.1.ftl" />

    <link rel="stylesheet" type="text/css" href="${uiDomain}/css/addPicture.css" />

    <script type="text/javascript" src="${uiDomain}/js/viewPics/js/simple.slide.min.js"></script>
    <link rel="stylesheet" href="${uiDomain}/js/viewPics/css/animate.css">
    <link rel="stylesheet" href="${uiDomain}/js/viewPics/css/simple.slide.css">
    <script type="text/javascript" src="${uiDomain}/js/viewPics/js/jquery.SuperSlide2.js"></script>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/js/viewPics/css/leftSlip.css">
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body>
<div id="phtab" style="margin:10px 10px 0px 10px;" class="tabs-container">
    <div title="基础信息">
        <div class="NorForm" style="height:auto;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="LeftTd" style="width:38%;">
                        <label class="LabName" style="width:100px;"><span>姓名：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_name"></span>
                    </td>
                    <td class="LeftTd" style="width:38%;">
                        <label class="LabName" style="width:100px;"><span>曾用名：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_usedName"></span>
                    </td>
                    <td rowspan="4" class="LeftTd">
                        <div class="photoHouseHold">
                            <a href='javascript:;' i='${uiDomain}/images/sys1_10.png' class='Slide checkImg' id="checkImgId"><img src='${uiDomain}/images/sys1_10.png' alt='' id="photoimg"></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName" style="width:100px;"><span>证件号码：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_identityCard"></span>
                    </td>
                    <td class="LeftTd">
                        <label class="LabName" style="width:100px;"><span>出生年月：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_birthday"></span>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>性别：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_genderCN"></span>
                    </td>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>民族：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_ethnicCN"></span>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>籍贯：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_residentBirthplace"></span>
                    </td>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>婚姻状况：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_marriageCN"></span>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>政治面貌：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_residentPoliticsCN"></span>
                    </td>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>联系电话：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_residentMobile"></span>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>文化程度：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_educationCN"></span>
                    </td>
                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>宗教信仰：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_religionCN"></span>
                    </td>
                    <td class="LeftTd"></td>
                </tr>
<#--                <tr>-->
<#--                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>人户状态：</span></label>-->
<#--                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_houseResideCN"></span>-->
<#--                    </td>-->
<#--                    <td class="LeftTd"><label class="LabName" style="width:100px;"><span>人口类型：</span></label>-->
<#--                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_typeCN"></span>-->
<#--                    </td>-->
<#--                    <td></td>-->
<#--                </tr>-->
                <tr>
                    <td colspan="3"  class="LeftTd"><label class="LabName" style="width:100px;"><span>户籍地：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_residence"></span>
                    </td>
                </tr>
                <tr>
                    <td colspan="3"  class="LeftTd"><label class="LabName" style="width:100px;"><span>现居住地址：</span></label>
                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_residenceAddr"></span>
                    </td>
                </tr>
<#--                <tr>-->
<#--                    <td colspan="3"  class="LeftTd"><label class="LabName" style="width:100px;"><span>家庭地址：</span></label>-->
<#--                        <span class="Check_Radio FontDarkBlue" id="ciRsTop_familyAddress"></span>-->
<#--                    </td>-->
<#--                </tr>-->
            </table>
            <div class="title FontDarkBlue">上报事件</div>
            <table id="eventTable"></table>
        </div>
	</div>
    <div title="成员信息">
        <table id="memberTable"></table>
    </div>
</div>

</body>

<script type="text/javascript">
    $(function(){
        $("#phtab").height($(window).height()-17);
        $('#phtab').tabs({
            plain:true
        });

        $(".checkImg").simpleSlide({
            "opacity":0.5,                  //背景透明度
            "windowAction": "zoomIn",       //窗体进入动画
            "imageAction": "bounceIn",      //图片进入动画
            "loadingImage":"img/1.gif",      //加载图片
            "moreThanOnePic":false          //是否多于一张照片
        });

        $('#eventTable').datagrid({
            idField:'EVENT_ID',
            singleSelect:true,
            fitColumns:true,
            columns:[[
                {field:'CREATE_TIME',title:'时间',align:'center',width:'20%',
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                    }
                },
                {field:'CONTENT_',title:'事件描述',align:'center',width:'59.8%',
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        return '<a class="eName" href="###" title="'+value+'" onclick="eventDetail(' + rec.EVENT_ID + ')")>'+value+'</a>';
                    }
                },
                {field:'EVENT_STATUS',title:'状态',align:'center',width:'10%',
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        var f = '';
                        switch(value){
                        	case '00':f='受理';break;
                        	case '01':f='上报';break;
                        	case '02':f='分流';break;
                        	case '03':f='结案';break;
                        	case '04':f='归档';break;
                        }
                        return f;
                    }
                },
                {field:'CUR_USER',title:'处理人',align:'center',width:'12%',formatter:function(value,rec,rowIndex){
                        return value==null?"":('<p title="'+value+'">'+value+'</p>');                   
                    }}
            ]],
            url:"${rc.getContextPath()}/zhsq/jointDefence/event/listData.json?userId=${userId?c}&gridCode=${userRegionCode!''}",
            pagination:true
        });
        getRsDetailByIdCardAndOrgCode()
        loadDataList();

    });
function loadDataList() {	   
		$('#memberTable').datagrid({
			width:600,
			height:600,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,//让列宽自适应窗口宽度
			singleSelect: true,
			idField:'cRsId',
            loader:function(param,success,error){
                $.ajax({
                    dataType: "JSONP",
                    type: "post",
                    data: {
                        // 'identityCard': '331003197605275196',
                        <#--'identityCard': '${idCard!''}',-->
                        // 'orgCode': '621704001000'
                        'orgCode': '${userRegionCode!''}'
                    },
                    timeout: 2000,
                    url: '${RS_URL}/cirs/getListForJsonp.jhtml?jsoncallback=?',
                    success: function(data){
                        success(data.rows)
                    }
                })
            },
            <#--url:"${RS_URL}/individual/findHomeMemberListByIdCard.jhtml?orgCode=621704001000&identityCard=331003197605275196",-->
			columns:[[
				// {field:'householderRelationCN',title:'与户主关系',align:'center',width:'12%'},
                {field:'partyName',title:'姓名',align:'center',width:'12%',formatter:function(value,rec,rowIndex){
						var f= '<a class="eName" href="###"  onclick="detailRS(' + rec.partyId + ')")>'+value+'</a>';
						return f;
					 }	
                },
                {field:'usedName',title:'曾用名',align:'center',width:'12%'},
                {field:'genderCN',title:'性别',align:'center',width:'7%'},
                {field:'identityCard',title:'公民身份证号码',align:'center',width:'16%'},
                {field:'jtPoliticsCN',title:'政治面貌',align:'center',width:'10%'},
                {field:'nationCN',title:'民族',align:'center',width:'3%'},
                {field:'mobilePhone',title:'联系电话',align:'center',width:'16%'},
                // {field:'residence',title:'家庭地址',align:'center',width:'28%'}
			]],
			toolbar:'#jqueryToolbar',
			pagination:false,
			pageSize: 20,
			onDblClickRow:function(index,rec){
				
			},
			onLoadSuccess:function(data){
			    $("#tempstatus").val("1");
				if(data.total == 0){
					// $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			}
		});		
	}


	function getRsDetailByIdCardAndOrgCode() {
        $.ajax({
            dataType: "JSONP",
            type: "post",
            data:{
                'identityCard':'${idCard}',
                // 'identityCard':'350206198003318395',
                // 'orgCode':'621704001000'
                'orgCode':'${userOrgCode}'
            },
            timeout: 2000,
            url:  '${RS_URL}/individual/findByIdentityCardWithCiRs.jhtml',
            success: function (data) {
                console.log(data);
                if(data!=null){
                    $("#ciRsTop_name").text(data.partyName)  //姓名
                    $("#ciRsTop_usedName").text(data.usedName) //曾用名
                    $("#ciRsTop_identityCard").text(data.identityCard) //身份证
                    $("#ciRsTop_birthday").text(formatDate(data["birthday"].time, 'yyyy-MM-dd'))  //出生年月
                    $("#ciRsTop_genderCN").text(data.genderCN) //性别
                    $("#ciRsTop_ethnicCN").text(data.nationCN) //民族
                    $("#ciRsTop_residentBirthplace").text(data.residentBirthplace ) //籍贯
                    $("#ciRsTop_marriageCN").text(data.maritalStatusCN ) //婚姻状况
                    $("#ciRsTop_residentPoliticsCN").text(data.jtPoliticsCN ) //政治面貌
                    $("#ciRsTop_residentMobile").text(data.mobilePhone) //联系电话
                    $("#ciRsTop_educationCN").text(data.educationLevelCN) //文化程度
                    $("#ciRsTop_religionCN").text(data.religionCN) //宗教信仰
                    // $("#ciRsTop_houseResideCN").text(data.partyName) //人户状态
                    // $("#ciRsTop_typeCN").text(data.partyName) //人口类型
                    $("#ciRsTop_residence").text(data.residence) //户籍地
                    $("#ciRsTop_residenceAddr").text(data.residenceAddr) //现居住地址
                    // $("#ciRsTop_familyAddress").text(data.residence) //家庭地址
                }


            },
            error: function (e) {

            }
        });
    }


	function eventDetail(eventId){
		 var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&model=c&eventId='+eventId;
			openJqueryWindowByParams({
            title: "查看事件详情",
            targetUrl: url
           });
	}
	function detail(familyId){
	        var url = '${rc.getContextPath()}/zhsq/jointDefence/viewfamily.jhtml?familyId='+familyId;
			openJqueryWindowByParams({
            title: "查看信息",
            targetUrl: url
           });}
	function detailRS(rsId){
	        var url = '${RS_URL}/cirs/viewResidentSnippet.jhtml?partyId='+rsId;
			openJqueryWindowByParams({
            title: "查看成员信息",
            targetUrl: url,
            width:'1000',
            height:'450'
           });
	}
    function formatDate(dateStr, format) {
        var date = new Date(dateStr);
        var o = {
            "M+": date.getMonth() + 1, //month
            "d+": date.getDate(),    //day
            "h+": date.getHours(),   //hour
            "m+": date.getMinutes(), //minute
            "s+": date.getSeconds(), //second
            "q+": Math.floor((date.getMonth() + 3) / 3),  //quarter
            "S": date.getMilliseconds() //millisecond
        };
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1,
                    RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }
</script>
</html>
