
<!DOCTYPE html>
<html style="background-color: transparent !important;">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>南安市详情页--大屏</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>

    <link href="http://app.just77.cn/natj/media/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/assets_admin/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/style-metro.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/style.css?2331" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/style-responsive.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/default.css?32" rel="stylesheet" type="text/css"/>
    <link href="http://app.just77.cn/natj/media/css/uniform.default.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/bootstrap-table.css" rel="stylesheet">
    <link href="http://app.just77.cn/natj/media/css/jquery.gritter.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/daterangepicker.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/fullcalendar.css" rel="stylesheet" type="text/css" />
    <link href="http://app.just77.cn/natj/media/css/jqvmap.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="http://app.just77.cn/natj/media/css/jquery.easy-pie-chart.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="http://app.just77.cn/natj/media/css/bootstrap-multiselect.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="http://app.just77.cn/natj/assets_admin/plugins/Swiper-3.1.0/css/swiper.min.css">
    <script src="http://app.just77.cn/natj/media/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="http://app.just77.cn/natj/assets_admin/js/common.js" type="text/javascript"></script>
    <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>

</head>
<!-- BEGIN BODY -->
<body class="page-header-fixed" style="background-color: transparent !important;color:white">
<style type="text/css">
    label {
        display: inline-block;
        margin: 5px 0px 0px;
    }
    html, body {
    height: 100%;
	}
	
	.uneditable-input, textarea.m-wrap, select.m-wrap, input[type="text"].m-wrap, input[type="password"].m-wrap, input[type="datetime"].m-wrap, input[type="datetime-local"].m-wrap, input[type="date"].m-wrap, input[type="month"].m-wrap, input[type="time"].m-wrap, input[type="week"].m-wrap, input[type="number"].m-wrap, input[type="email"].m-wrap, input[type="url"].m-wrap, input[type="search"].m-wrap, input[type="tel"].m-wrap, input[type="color"].m-wrap {
    	color: white;
    	background-color: rgba(6, 31, 52, .8);
	}
	
	.uecont * {
    	color: white !important;
	}
	
	.table-hover tbody tr:hover>td, .table-hover tbody tr:hover>th {
    	background-color: rgba(6, 31, 52, .8);
	}
	
	.h_gb_head h2 {
    	color: #4fa9c3;
	}
	
	.portlet.box.green {
    	border: none;
	}
</style>
<!-- BEGIN PAGE CONTAINER-->
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="portlet box green">
                <div class="portlet-body" style="background-color: transparent !important;margin-top: -1rem;" id="scrollDiv">
                    <div class="tab-content">
                        <form name="example" method="post" action="" name="form1" id="form1" class="form-horizontal">
                            <input type="hidden" name="id" value="240"/>
                            <input type="hidden" name="current_step"
                                   value=""/>
                            <h4 class="form-section">调解信息</h4>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">案件编号：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.no}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">所属网格：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${gridname}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">调解员：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.mediator}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">结案时间：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.finishTime}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">案号类型：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.case_no_type}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">案号：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.case_no}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">当前环节：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="<#if bo.row.status??>${bo.row.status}<#else>已完结</#if>"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">当前状态：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.status}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">最新经办人：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.last_userName}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">当前处理人：</label>
                                        <div class="controls">
                                            <input name="" type="text"
                                                   value="${bo.row.current_userName}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span10">
                                    <div class="control-group">
                                        <label class="control-label">
                                            七对接类别：</label>
                                        <div class="controls" id="ckDiv">
                                            <label><input type="checkbox" value="访调对接" name="docking_type" disabled="disabled"/>访调对接</label>
                                            <label><input type="checkbox" value="公调对接" name="docking_type" disabled="disabled"/>公调对接</label>
                                            <label><input type="checkbox" value="检调对接" name="docking_type" disabled="disabled"/>检调对接</label>
                                            <label><input type="checkbox" value="诉调对接" name="docking_type" disabled="disabled"/>诉调对接</label>
                                            <label><input type="checkbox" value="援调对接" name="docking_type" disabled="disabled"/>援调对接</label>
                                            <label><input type="checkbox" value="行调对接" name="docking_type" disabled="disabled"/>行调对接</label>
                                            <label><input type="checkbox" value="市镇（镇村）对接" name="docking_type" checked disabled="disabled"/>市镇（镇村）对接</label>
                                            <label><input type="checkbox" value="其他" name="docking_type" disabled="disabled"/>其他</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">对接案号：</label>
                                        <div class="controls">
                                            <input name="" type="text" value="${bo.row.case_no}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">刑事赔偿保证金：</label>
                                        <div class="controls wauto">
                                            <input name="" type="text" value="${bo.row.criminal_bond}"
                                                   class="m-wrap" readonly>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">纠纷类别：</label>
                                        <div class="controls">
                                            <input name="" type="text" value="${bo.row.dispute_type}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">受理机构：</label>
                                        <div class="controls">
                                            <input type="text"
                                                   value="${bo.row.accept_institution}"
                                                   class="m-wrap media " readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">受理日期：</label>
                                        <div class="controls">
                                            <input name="" type="text" value="${bo.row.accept_date}"
                                                   class="m-wrap" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="span5">
                                    <div class="control-group">
                                        <label class="control-label">发生地：</label>
                                        <div class="controls">
                                            <input type="text"
                                                   value="${bo.row.locality}" class="m-wrap media "
                                                   readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span10">
                                    <div class="control-group">
                                        <label class="control-label">调解地点：</label>
                                        <div class="controls">
                                            <input type="text"
                                                   value="${bo.row.accept_place}" class="m-wrap media "
                                                   readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span10">
                                    <div class="control-group">
                                        <label class="control-label">案情简介：</label>
                                        <div class="controls">
                                            <div class="uecont">${bo.row.fullContent}</div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid" style="margin-left: 4rem;">
                                <div class="span10">
                                    <div class="control-group">
                                        <label class="control-label">案件陈述：</label>
                                        <div class="controls">
                                            <div class="uecont">${bo.row.statement}</div>
                                            <!--                                            <textarea name="fullContent" id="fullContent" type="text" value="" class="m-wrap media required" placeholder="" readonly></textarea>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid"  style="margin-left: 4rem;" id="photo" >
                                <div class="span8">
                                    <div class="control-group">
                                        <label class="control-label"  >图片：</label>
                                        <div class="controls">
                                            <ul>
                                                <#if attachment??>
                                                <#list attachment as item>
                                                <li class="img_item"
                                                    style="float: left;width:200px;height:auto;margin-right:5px;"
                                                    data="0">
                                                    <div class="item">
                                                        <div class="zoom">
                                                            <img class="img_cont"
                                                                 style="width:100%;"
                                                                src="http://app.just77.cn${item.url}">
                                                            <div class="zoom-icon"></div>
                                                        </div>
                                                    </div>
                                                </li>
                                                </#list>
                                                </#if>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <h4 class="form-section">当事人</h4>
                            <#if party??>
                            <#list party as partydate>
                            <div class="row-fluid">
                                <div class="span11">
                                    <div class="table-scrollable" style="margin-left:130px;">
                                        <div class="bg_white">
                                            <div>
                                                <div class="h_gb_head" onclick="opendown(this)">
                                                    <i class="h_gb_head_icon"></i>
                                                    <h2>${partydate.party_name}</h2>
                                                    <i class="ico_arrow folder_arrow_down"></i>
                                                </div>
                                                <div class="pb15 deviceSumbox">
                                                    <div class="row-fluid">
                                                        <div class="span10">
                                                            <label
                                                                    class="control-label">当事人类型：${partydate.party_type}</label>
                                                        </div>
                                                    </div>
                                                    <div class="tab_docking_type" style="display: block">
                                                        <table class="table table-bordered table-hover">
                                                            <tbody>
                                                            <tr>
                                                                <td>当事人信息：</td>
                                                                <td><input name="party_name" id="party_name"
                                                                           type="text"
                                                                           value="${partydate.party_name}"
                                                                           class="m-wrap media required"
                                                                           readonly></td>
                                                                <td><input name="party_name" id="party_name"
                                                                           type="text"
                                                                           value="${partydate.party_sex}"
                                                                           class="m-wrap media required"
                                                                           readonly>
                                                                </td>
                                                                <td><input name="party_idCard" id="party_idCard"
                                                                           type="text"
                                                                           value="${partydate.party_idCard}"
                                                                           class="m-wrap media required"
                                                                           readonly></td>
                                                                <td><input name="party_phone" id="party_phone"
                                                                           type="text"
                                                                           value="${partydate.party_phone}"
                                                                           class="m-wrap media required"
                                                                           readonly></td>
                                                                <td><input name="party_address"
                                                                           id="party_address"
                                                                           type="text"
                                                                           value="${partydate.party_address}"
                                                                           class="m-wrap media required"
                                                                           readonly></td>
                                                            </tr>

                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>


                                </div>
                            </div>
                            </#list>
                            </#if>

                            <h4 class="form-section">流程记录</h4>
                            <div class="row-fluid">
                                <div class="span10" style="padding-left:130px;">
                                    <div class="bootstrap-table"><div class="fixed-table-toolbar">
                                            <div class="bars pull-left"></div></div><div class="fixed-table-container" style="height: 406px; padding-bottom: 37px;">
                                            <div class="fixed-table-body">
                                                <table    data-page-list="[5, 10, 20, 50, 100, 200]" data-search="false" data-height="460"  class="table table-hover" >
                                                    <thead style="">
                                                    <tr style="">
                                                        <th style="">
                                                            <div class="th-inner ">环节</div>
                                                            <div class="fht-cell"></div>
                                                        </th>
                                                        <th style="">
                                                            <div class="th-inner ">经办人</div>
                                                            <div class="fht-cell"></div>
                                                        </th>
                                                        <th style="">
                                                            <div class="th-inner ">调解员</div>
                                                            <div class="fht-cell"></div>
                                                        </th>
                                                        <th style="">
                                                            <div class="th-inner ">操作</div>
                                                            <div class="fht-cell"></div>
                                                        </th>
                                                        <th style="">
                                                            <div class="th-inner ">时间</div>
                                                            <div class="fht-cell"></div>
                                                        </th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <#if log??>
                                                        <#list log as logdate>
                                                            <tr>
                                                                <td>${logdate.step_name}</td>
                                                                <td>${logdate.userName}</td>
                                                                <td><#if logdate.mediator??>${logdate.mediator}<#else>-</#if></td>
                                                                <td>${logdate.action}</td>
                                                                <td>${logdate.time}</td>
                                                            </tr>
                                                        </#list>
                                                    </#if>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="fixed-table-footer" style="display: none;">
                                                <table>
                                                    <tbody>
                                                    <tr>

                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                                <div class="pull-right pagination" style="display: none;">
                                                    <ul class="pagination">
                                                        <li class="page-first disabled">
                                                            <a href="javascript:void(0)">&lt;&lt;</a>
                                                        </li>
                                                        <li class="page-pre disabled">
                                                            <a href="javascript:void(0)">&lt;</a>
                                                        </li>
                                                        <li class="page-number active">
                                                            <a href="javascript:void(0)">1</a>
                                                        </li>
                                                        <li class="page-next disabled">
                                                            <a href="javascript:void(0)">&gt;</a>
                                                        </li>
                                                        <li class="page-last disabled">
                                                            <a href="javascript:void(0)">&gt;&gt;</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!-- END PAGE CONTENT-->

</div>
<#--图片放大-->
<div class="enlarge">
    <a class="ico-close" onclick="narrow()"><i class="ion-ios-close-outline"></i></a>
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <#if attachment??>
            <#list attachment as item>
            <div class="swiper-slide">
                <div class="pinch-zoom">
                    <img src="http://app.just77.cn${item.url}">
                </div>
            </div>
            </#list>
            </#if>
        </div>
        <div class="swiper-pagination"></div>
    </div>
</div>

<#--对勾样式-->
<script src="http://app.just77.cn/natj/media/js/jquery.uniform.min.js" type="text/javascript"></script>
<script src="http://app.just77.cn/natj/media/js/app.js?4" type="text/javascript"></script>
<script>
    jQuery(document).ready(function () {
        // initiate layout and plugins
        App.init();
    });
</script>
<script type="text/javascript">
    <#if attachment?size ==0>$("#photo").hide() </#if>
</script>
<script type="text/javascript">
    $(document).ready(function () {
                var sheight = window.innerHeight;
                $(".swiper-container").height(sheight);
                $(".pinch-zoom").height(sheight);
                for (var i = 0; i < $(".swiper-slide").length; i++) {
                    var mtop = (sheight - $(".swiper-slide").eq(i).find("img").height()) / 2 - $(".title").height();
                    if (mtop >= 0) {
                        $(".swiper-slide").eq(i).find("img").css("margin-top", mtop);
                    }
                }
    });
</script>
<script type="text/javascript">
    //动态添加状态
    $(function () {
        var event = "${bo.row.docking_type !''}".split(",");
        var o ={};
        for (var i=0;i<event.length;i++){
            o[event[i]] = 1;

        }
        $("#ckDiv input").each(function (i,ck) {
            if (o[ck.value]){
                $(ck).attr("checked","checked");
                $(ck).parent().addClass("checked");
            }
        })
        
        $('html').niceScroll({
				cursorcolor: '#00a0e9',
				cursoropacitymax: '.4',
				cursorwidth: ".05rem",
				cursorborderradius: ".05rem",
				cursorborder: 'none',
				autohidemode: false,
		})
    });

</script>
</body>

</html>




