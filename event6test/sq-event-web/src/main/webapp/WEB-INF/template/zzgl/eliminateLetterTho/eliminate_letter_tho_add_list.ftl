<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>${moduleName}列表</title>
    <#include "/component/layuiCommon/common_list_files_1.1.ftl" />
    <#include "/component/szcomponentsFiles-1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
<#--    <#include "/zzgl/eliminateLetterTho/indus.tpl.ftl" />-->
    <style>
        .layui-form .layui-table-box{width: 100%;}
    </style>
</head>

<body class="bg-f5f5f5">


<div class="layui-row">
    <div class="layui-fluid">
        <div class="layui-col-xs12 mt15 lay-nor-hd layui-pbt0 br8">
            <div class="layui-row layui-form layui-sch-g mt10 pl35 pr35">
                <div class="pb5 bor-bottom layui-title-span-top j-click-span">
                    <span class="mr35 active" data-type="wait"${moduleName}</span>
                    <#--<span class="mr35" data-type="handing">已办事项</span>-->
                </div>
            </div>
            <div class="layui-row layui-form layui-sch-g mt10">
                <form id="searchForm">
                    <div class="">
                        <div class="layui-col-xs3 bgt">
                            <div class="layui-form-item">
                                <label class="layui-form-label">所属地域</label>
                                <div class="layui-input-block">
                                    <input id="regionCode" name="regionCode" type="hidden" value="${regionCode}">
                                    <input type="text" placeholder="行政区域" name="regionName" id="regionName" autocomplete="off" class="layui-input" value="${regionName}">
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs3">
                            <div class="layui-form-item">
                                <label class="layui-form-label">文书类型</label>
                                <div class="layui-input-block">
                                    <select id="letterType" name="letterType">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs3">
                            <div class="layui-form-item">
                                <label class="layui-form-label">制发单位</label>
                                <div class="layui-input-block">
                                    <input type="text" id="fbDepartNameDet" name="fbDepartNameDet" placeholder="制发单位" autocomplete="off" class="layui-input">
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs1 posi-rela mr30">
                            <div class="layui-form-item">
                                <label class="layui-form-label ml10 col-55 cursor j-click-more-search">高级查询</label>
                            </div>
                            <div class="more-search-ul hide" style="height: 75px">
                                <!-- <i class="mc-close j-click-mc-close hide"></i> -->
                                <div class="div-w100-h100 posi-rela">
                                    <ul class=" more-search-ul-com scroll">
                                        <li class="mb15">
                                            <div class="layui-form-item">
                                                <label class="layui-form-label ">发出时间</label>
                                                <div class="layui-input-block">
                                                    <input type="text" placeholder="请选择时间" class="layui-input" id="fbDate" readonly>
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="ml20 mr10">
                    <button type="button" class="layui-btn layui-btn-normal" id="searchBtn">查询</button>
                    <button type="button" class="layui-btn layui-btn-ext1" id="resetBtn">重置</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--我的待办列表-->
<div class="layui-row" id="waitDataTable">
    <div class="layui-fluid">
        <div class="layui-col-xs12 layui-form mt10 lay-nor-bd br8">
            <div class="layui-fluid">
                <div class="layui-col-xs12">
                    <div id="toolbarDemo"></div>
                    <table class="layui-hide" id="data1" lay-filter="data1"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 弹窗 start -->
<!-- 退回的弹窗 -->
<div class="layui-row layui-form lay-back" hidden>
    <div class="layui-fluid sch-ipt">
        <div class="layui-col-xs12">
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="required-red">*</span>退回说明</label>
                <div class="layui-input-block ml20">
                    <textarea placeholder="请输入退回说明" id="sendBackContent" lay-verify-custom="required:true,validType:['maxLength[200]']" class="layui-textarea"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 end -->
</body>
<!-- 表格头部工具栏 -->
<script type="text/html" id="tbTollsBar">
    <div class="layui-col-xs12">
        <p class="text-nor font-size-15 cor-333 line-height-30 font-bold fl"><i class="text-icon"></i>数据列表</p>
        <div class="crudbox fr">
            <@actionCheck></@actionCheck>
<!--            <button type="button" class="layui-btn layui-btn-normal"  onclick="add()">新增</button>
            <button type="button" class="layui-btn layui-btn-normal"  onclick="edit()">编辑</button>
            <button type="button" class="layui-btn layui-btn layui-btn-ext1"  onclick="del()">删除</button>-->
        </div>
    </div>
</script>

<script>


    var bizStatusMap = new Map();
    bizStatusMap.set("99","草稿");
    bizStatusMap.set("01","待反馈");
    bizStatusMap.set("02","待审核");
    bizStatusMap.set("03","待审核");
    bizStatusMap.set("04","待审核");
    bizStatusMap.set("05","归档");
    bizStatusMap.set("06","待反馈");
    bizStatusMap.set("07","不通过完结");
    bizStatusMap.set("08","不通过完结");
    bizStatusMap.set("09","不通过完结");

    function getDictMap(){
        var indusCodeDictList = JSON.parse('${indusCodeDictList}');
        var DictMap = {};
        for(let i=0;i<indusCodeDictList.length;i++){
            let dictObj = indusCodeDictList[i];
            DictMap[dictObj.dictGeneralCode] = dictObj.dictName;
        }
        return DictMap;
    }
    var indusCodeDictMap = getDictMap();

    //文书下拉框初始化
    LayuiComp.SingleSelect('letterType',{
        selectValue:"0",
        pCode:'${letterTypeDict}',
        selectedCallback:function(originalData){
            // console.log(originalData);
        },
        context:'${ANOLE_COMPONENT_URL}'
    });

    //所属地域
    AnoleApi.initOrgZtreeComboBox("regionName", "regionCode", function (regionCode, items){
        if(items!=undefined && items!=null && items.length>0){
            var org = items[0];
            if(org.orgCode!='-1'){
                $("#regionCode").val(org.orgCode);
            }
        }
    },{
        ShowOptions : {
            rootName : "地域信息",
            //EnableToolbar : true
        }
    });



    function lableMouseOut(){
        layer.close(tipsIndex);
    }

    $(function () {
        // 计算页面高度
        var winW, winH, lhH, lbH, tbH;
        function calcLayBdH() {
            winW = $(window).width(),
                winH = $(window).height(),
                lhH = $('.lay-nor-hd').outerHeight(true),
                lbH;
            if (winW <= 1366) {
                lbH = winH - lhH - 20;
                tbH = lbH - 5;//table高度计算，可调整
            } else {
                lbH = winH - lhH - 30;
                tbH = lbH - 10;
            }
            $('.lay-nor-bd').css({ 'height': lbH + 'px' });
        }
        calcLayBdH();

        $(window).on('resize', function () {
            calcLayBdH();
        });

        layui.use(['form', 'table', 'laydate', 'laytpl', 'element','layer','util'], function () {
            var form = layui.form,
                table = layui.table,
                laydate = layui.laydate,
                laytpl = layui.laytpl,
                element = layui.element,
                layer = layui.layer,
                util = layui.util;

            wait = table.render({
                elem: '#data1'
                , url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/add/listData.json'
                , title: '新增文书列表'
                , parseData: function(res){
                    return{
                        "code": 0,
                        "msg": "",
                        "count": res.total,
                        "data" : res.rows
                    }
                }
                , height: tbH
                , toolbar: '#tbTollsBar' //开启头部工具栏，并为其绑定左侧模板
                , defaultToolbar: ['filter']
                ,limit: 20
                , cols: [[
                    { type: 'numbers' }
                    , { type: 'radio', hide:false }
                    , { field: 'thtNo', title: '“三书一函”编码' }
                    , { field: 'letterTypeCN', title: '文书类型' }
                    ,{ field: 'fbDate', title: '发出时间' ,templet: function (rec){if(rec.fbDate != null){return util.toDateString(rec.fbDate,'yyyy-MM-dd HH:mm:ss')}else{return ''}}}
                    , { field: 'caseName', title: '案件名称',event:'viewDetails', style:'cursor: pointer;color: #2d67cc' }
                    , { field: 'industrialCode', title: '行业领域名称',templet: function (rec){
                            var labelArray = eval('(' + rec.indusCodeArr + ')'),objStr;
                            if(!labelArray){
                                labelArray = [];
                            }else if(!labelArray.length){
                                labelArray = [labelArray];
                            }
                            let indusNameStr = "";
                            for(var i = 0; i< labelArray.length; i++){
                                objStr = JSON.stringify(labelArray[i]);
                                indusNameStr += indusCodeDictMap[labelArray[i].industrialCode] + ',';
                            }
                            indusNameStr = indusNameStr.substring(0,indusNameStr.length-1);
                            return indusNameStr;
                    }}
                    , { field: 'reDepartNameDet', title: '接收单位' ,templet: function (rec){
                        var deptName = rec.reChgs.reDepartNameDet;
                        console.log(rec.reChgs);
                        return deptName;
                    }}
                    , { field: 'bizStatus', title: '状态',templet:function(rec){
                        return bizStatusMap.get(rec.bizStatus)
                    }}
                ]]
                , page: true
                , done: function () {//表格的回调

                    element.render();
                    // 滚动条美化
                    $('.scroll').niceScroll({
                        cursorcolor: "#000", //滚动条的颜色
                        cursoropacitymax: 0.12, //滚动条的透明度，从0-1
                        cursorwidth: "4px", //滚动条的宽度  单位默认px
                        background: "transparent",
                        cursorborderradius: "2px", //滚动条两头的圆角
                        autohidemode: 'hidden', //是否隐藏滚动条  true的时候默认不显示滚动条，当鼠标经过的时候显示滚动条
                        zindex: 8, //给滚动条设置z-index值
                        touchbehavior: true,
                    })
                }
                ,text:{none: '<div class="layui-sup-nd">\n' +
                        '<div class="layui-nd-con">\n' +
                        '<img src="${uiDomain}/web-assets/_zhcs/shenzhen-fp/images/icon-timeline-no-data.png">\n' +
                        '<p>暂无数据</p>\n' +
                        '</div>\n' +
                        '</div>'
                }
            });

            //  查看详情的点击事件
            table.on('tool(data1)', function (obj) {
                if (obj.event === 'viewDetails') {
                    var url = '${rc.getContextPath()}/zhsq/eliminateLetterTho/detail.jhtml?thoUuid='+obj.data.thoUuid;
                    var detailsIndex = layer.open({
                        skin: 'layer-content-details',
                        type: 2,
                        title: false,
                        closeBtn: 0,
                        shadeClose: true, //点击遮罩关闭
                        area: ['100%', '100%'],
                        content: url,
                        success: function (layero) {
                        }
                    });
                    layer.full(detailsIndex);
                }
            });

            //日期范围
            laydate.render({
                elem: '#fbDate',
                range: true,
                format: 'yyyy-MM-dd',
                theme: 'rq1',
            });

            // 监听复选框事件
/*
            table.on('checkbox(data1)', function (obj) {
                if (obj.type == 'one') {
                    if (obj.checked) {
                        obj.tr.addClass("layui-table-click")
                    } else {
                        obj.tr.removeClass("layui-table-click")
                    }
                } else if (obj.type == 'all') {
                    if (obj.checked) {
                        $(obj.tr.prevObject[0]).find('tr').addClass("layui-table-click")
                    } else {
                        $(obj.tr.prevObject[0]).find('tr').removeClass("layui-table-click")
                    }
                }
            });
*/
            table.on('row(data1)',function(obj){
                obj.tr.find('input[type="radio"][lay-type]').first().nextAll().not('.layui-form-radioed').click();
            });

/*            $('#data1').siblings('.layui-form').find('.layui-table-main').on("click", " tr td", function () {
                //这里写你的响应事件
                if ($(this).data('field') == 'name') {
                    layer.config({
                        extend: '${uiDomain}/web-assets/plugins/layui-v2.5.5/layui/css/modules/layer/theme/shenzhen-fp/layer/style.css', //加载您的扩展样式
                        skin: 'layer-file-add lay-form-ml110'
                    });
                    var indexCheck = layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        shadeClose: true, //点击遮罩关闭
                        content: $('.lay-file-check'),
                        success: function (layero) {
                            calcLayBdH(layero)
                        }
                    });
                    layer.full(indexCheck);
                    $('.layui-back').on('click', function () {
                        layer.close(indexCheck);
                    });
                }
            });*/

        });



        //监听查询按钮
        $("#searchBtn").on('click', function () {
            search();
        });

        //监听重置按钮
        $("#resetBtn").on('click', function () {
            $("#searchForm")[0].reset();
            $("#regionCode").val('${regionCode}');
            search();
        });



        //高级搜索
        $('.j-click-more-search').on('click', function (e) {
            e.stopPropagation()
            $('.more-search-ul').slideToggle(200)
        })
    });

    function search(){
        //获取查询条件的值
        var regionCode = $("#regionCode").val(),
            letterType = $("#letterType").val(),
            fbDepartNameDet = $("#fbDepartNameDet").val(),
            fbDateStr = $('#fbDate').val().split(' - ');
        var startDateStr = fbDateStr[0],
            endDateStr = fbDateStr[1]?fbDateStr[1]:"";
        wait.reload({
            where:{
                regionCode : regionCode,
                letterType : letterType,
                fbDepartNameDet : fbDepartNameDet,
                fbDateStart : startDateStr,
                fbDateEnd : endDateStr
            },page:{
                curr:1
            }
        })
    }

    //新增
    function add(){
        var url = '${rc.getContextPath()}/zhsq/eliminateLetterTho/form.jhtml';
        var formIndex = layer.open({
            skin: 'layer-content-form',
            type: 2,
            title: false,
            closeBtn: 0,
            shadeClose: true, //点击遮罩关闭
            area: ['100%', '100%'],
            content: url,
            success: function () {
            }
        });
        layer.full(formIndex);
    }
    //新增
    function edit(){
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length != 1){
            layer.msg("请选择一条记录",{skin:'layui-layer-hui'});
            return;
        }
        if(datas[0].bizStatus != 99){
            layer.msg("只有草稿状态才可以编辑",{skin:'layui-layer-hui'});
            return;
        }
        var params = '?thoUuid='+datas[0].thoUuid;
        var url = '${rc.getContextPath()}/zhsq/eliminateLetterTho/edit.jhtml'+params;
        var formIndex = layer.open({
            skin: 'layer-content-form',
            type: 2,
            title: false,
            closeBtn: 0,
            shadeClose: true, //点击遮罩关闭
            area: ['100%', '100%'],
            content: url,
            success: function () {
            }
        });
        layer.full(formIndex);
    }

    //删除
    function del(){
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length != 1){
            layer.msg("请选择一条记录",{skin:'layui-layer-hui'});
        }else{
            if(datas[0].bizStatus != 99){
                layer.msg("只有草稿状态才可以删除",{skin:'layui-layer-hui'});
                return;
            }
/*            var ids = "";
            for(var i=0;i<datas.length;i++){
                ids += datas[i].thoUuid + ",";
            }
            ids = ids.substring(0,ids.length-1);*/
            layer.confirm("确定要删除吗?",{
                btn:['确定','取消'],
                shade:[0.3]
            },function(index) {
                var submitLoad = layer.load(1);//打开遮罩
                $.ajax({
                    type: 'POST',
                    url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/delete.jhtml',
                    data: {
                        thoUuid: datas[0].thoUuid
                    },
                    dataType: 'json',
                    success: function(response) {
                        if(response && response.success) {
                            $(".layui-laypage-btn").click();
                            layer.msg("删除成功",{skin:'layui-layer-hui'});
                        } else {
                            layer.msg("删除失败",{skin:'layui-layer-hui'});
                        }
                    },
                    error: function(data) {
                        layer.msg("连接超时",{skin:'layui-layer-hui'});
                    },
                    complete: function () {
                        if (submitLoad != null) {
                            layer.close(submitLoad);//关闭遮罩
                        }
                    }
                });
            })
        }
    }

</script>



</html>