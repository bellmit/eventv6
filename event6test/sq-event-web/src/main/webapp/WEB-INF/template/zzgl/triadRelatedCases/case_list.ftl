<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>${moduleName}列表</title>
    <#include "/component/layuiCommon/common_list_files_1.1.ftl" />
    <#include "/component/szcomponentsFiles-1.1.ftl" />
    <#include "/component/ComboBox.ftl" />

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

                </div>
            </div>
            <div class="layui-row layui-form layui-sch-g mt10">
                <form id="searchForm">
                    <div class="">
                        <div class="layui-col-xs3 bgt">
                            <div class="layui-form-item">
                                <label class="layui-form-label">所属地区</label>
                                <div class="layui-input-block">
                                    <input type="hidden" id="gridId" />
                                    <input id="regionCode" name="regionCode" type="hidden" value="${regionCode}">
                                    <input type="text" placeholder="所属地区" name="regionName" id="regionName" autocomplete="off" class="layui-input" value="${regionName}">
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs3">
                            <div class="layui-form-item">
                                <label class="layui-form-label">涉案性质</label>
                                <div class="layui-input-block">
                                    <select id="letterType" name="letterType">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs3">
                            <div class="layui-form-item">
                                <label class="layui-form-label">案件名称</label>
                                <div class="layui-input-block">
                                    <input type="text" id="fbDepartNameDet" name="fbDepartNameDet" placeholder="案件名称" autocomplete="off" class="layui-input">
                                </div>
                            </div>
                        </div>
                        <div class="layui-col-xs1 posi-rela mr30">
                            <div class="layui-form-item">
                                <label class="layui-form-label ml10 col-55 cursor j-click-more-search">高级查询</label>
                            </div>
                            <div class="more-search-ul hide" style="height: 12rem">
                                <div class="div-w100-h100 posi-rela">
                                    <ul class=" more-search-ul-com scroll">
                                        <li class="mb15">
                                            <div class="layui-form-item">
                                                <label class="layui-form-label ">状态</label>
                                                <div class="layui-input-block">
                                                    <select id="fillStatus" name="fillStatus">
                                                    </select>
                                                </div>
                                            </div>
                                        </li>
                                        <li class="mb15">
                                            <div class="layui-form-item">
                                                <label class="layui-form-label ">立案时间</label>
                                                <div class="layui-input-block">
                                                    <input style="cursor: pointer" type="text" placeholder="请选择时间" class="layui-input" id="fbDate" readonly>
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

</body>

<!-- 表格头部工具栏 -->
<script type="text/html" id="tbTollsBar">
    <div class="layui-col-xs12">
        <p class="text-nor font-size-15 cor-333 line-height-30 font-bold fl"><i class="text-icon"></i>数据列表</p>
        <div class="crudbox fr">
            <@actionCheck></@actionCheck>
<#--            <button type="button" class="layui-btn layui-btn-ext2" onclick="recovery()">回收</button>-->
<#--                <button type="button" class="layui-btn layui-btn-ext2" onclick="add()">新增</button>-->
<#--                 <button type="button" class="layui-btn layui-btn-ext2" onclick="edit()">编辑</button>-->
<#--                <button type="button" class="layui-btn layui-btn-ext1"  onclick="del()">删除</button>-->

        </div>
    </div>
</script>

<script>
    var selectedThoUuidArr = [];

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

    //涉案性质下拉框初始化
    LayuiComp.SingleSelect('letterType',{
        selectValue:"0",
        pCode:'${involvedNatureDict}',
        selectedCallback:function(originalData){
            // console.log(originalData);
        },
        context:'${ANOLE_COMPONENT_URL}'
    });

    //状态下拉框初始化
    LayuiComp.SingleSelect('fillStatus',{
        selectValue:"0",
        customData:[
            {name:'草稿',value:'00'},
            {name:'已提交',value:'01'}
        ],
        selectedCallback:function(originalData){
            // console.log(originalData);
        },
        context:'${ANOLE_COMPONENT_URL}'
    });

    //所属地域
    particleSize(${regionCode!''});
    AnoleApi.initGridZtreeComboBox("regionName", "gridId", function(gridId, items) {
        if(items != undefined && items != null && items.length > 0) {
            var grid = items[0];
            $("#regionCode").val(grid.orgCode);
            particleSize(grid.orgCode);
        }
    });
    function particleSize(infoOrgCode) {
        infoOrgCode = infoOrgCode + "";
        var l = infoOrgCode.length;
        var s = 5;
        // 2:省 4：市 6：区 9：街道 12：社区 15：网格
        switch(l) {
            case 2:
                s = 5;break;
            case 4:
                s = 5;break;
            case 6:
                s = 4;break;
            case 9:
                s = 3;break;
            case 12:
                s = 2;break;
            case 15:
                s = 1;break;
        }
        $(".Check_Radio").removeClass("FontDarkBlue").show().eq(0).addClass("FontDarkBlue").children().attr("checked", "checked");
        $(".Check_Radio:gt(" + (s - 1) + ")").hide();
    }

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
        var indexBtn = 0;
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
                , url: '${rc.getContextPath()}/zhsq/caseFilling/listData.json'
                , title: '用户数据表'
                , height: tbH
                , toolbar: '#tbTollsBar' //开启头部工具栏，并为其绑定左侧模板
                , defaultToolbar: ['filter']
                ,limit: 20
                , parseData: function(res){
                    return{
                        "code": 0,
                        "msg": "",
                        "count": res.total,
                        "data" : res.rows
                    }
                },where :{//默认的查询条件
                    gridCode : '${regionCode}'
                }
                , cols: [[
                    { type: 'numbers',title: '序号'}
                    ,{type:'checkbox'}
                    , { field: 'gridCodeStr', title: '所属地区' }
                    , { field: 'involvedNature', title: '涉案性质',templet: function (rec){
                            if(rec.involvedNature=="01"){
                                return "黑社会性质组织案件";
                            }
                            if(rec.involvedNature=="02"){
                                return "恶势力犯罪集团案件";
                            }
                            if(rec.involvedNature=="03"){
                                return "恶势力犯罪团伙案件";
                            }

                        } }
                    , { field: 'caseName', title: '案件名称',event:'viewDetails', style:'cursor: pointer;color: #2d67cc' }
                    ,{ field: 'filingTime', title: '立案时间' ,templet: function (rec){if(rec.filingTime != null){return util.toDateString(rec.filingTime,'yyyy-MM-dd')}else{return ''}}}
                    , { field: 'isFinishde', title: '案件是否办结',templet: function (rec){
                            if(rec.isFinishde==1){
                                return "是";
                            }
                            if (rec.isFinishde==0){
                                return "否";
                            }
                            if (rec.isFinishde==null){
                                return "--";
                            }

                        }}
                    , { field: 'investigationUnit', title: '侦查单位'}
                    , { field: 'fillStatus', title: '状态',templet: function (rec){
                            if(rec.fillStatus=="00"){
                                return "草稿";
                            }else {
                                return "提交";
                            }

                        }}
                ]]
                , page: true
                , done: function (res) {//表格的回调
                    $('.ca-btn-ext1').find('.layui-btn').eq(indexBtn).addClass('zhgl-btn-on').siblings().removeClass('zhgl-btn-on');
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

                    //判断是否需要勾选
                    var list = res.data;
                    console.log(list);
                    for(let i=0;i<list.length;i++){
                        let mainObj = list[i];
                        if(selectedThoUuidArr.join().indexOf(mainObj.thoId) != -1){
                            var index = i;
                            var tableBox = $('#data1').next('.layui-border-box');
                            tableDiv = tableBox.find(".layui-table-body.layui-table-main");
                            var checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-checkbox div.layui-form-checkbox I");
                            if(checkCell.length>0){
                                checkCell.click();
                            }
                        }

                    }


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
                    var url = '${rc.getContextPath()}/zhsq/caseFilling/detail.jhtml?undCaseUuid='+obj.data.undCaseUuid;
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
                format: 'yyyy-MM-dd',
                theme: 'rq1',
                btns: ['clear', 'confirm'],
                trigger: 'click',
            });



            table.on('checkbox(data1)', function(obj){
                //console.log(obj); //当前行的一些常用操作集合
                //console.log(obj.checked); //当前是否选中状态
                console.log(obj.data)
                let thoId = obj.data.thoId;
                var datas = layui.table.checkStatus('data1').data;
                console.log(datas);
                let idArr = [];
                for(let i=0;i<datas.length;i++){
                    idArr.push(datas[i].thoId);
                }
                let aSet = new Set(selectedThoUuidArr);
                let bSet = new Set(idArr);
                if(obj.checked){
                    //selectedThoUuidArr.push(thoId);
                    let union = Array.from(new Set(selectedThoUuidArr.concat(idArr)));
                    selectedThoUuidArr = [];
                    selectedThoUuidArr.push.apply(selectedThoUuidArr,union);
                    console.log("并集");
                    console.log(union);
                    console.log(selectedThoUuidArr);
                    console.log("并集");
                }else {
                    let intersection = Array.from(new Set(selectedThoUuidArr.filter(v => bSet.has(v))));
                    selectedThoUuidArr = [];
                    selectedThoUuidArr.push.apply(selectedThoUuidArr, intersection);
                    console.log("交集");
                    console.log(intersection);
                    console.log(selectedThoUuidArr);
                    console.log("交集");

                }
            });

        });

        //监听查询按钮
        $("#searchBtn").on('click', function () {
            search();
        });

        //监听重置按钮
        $("#resetBtn").on('click', function () {
            $("#searchForm")[0].reset();
            $("#regionCode").val('${regionCode}');
            selectedThoUuidArr = [];
            search();
        });

        //高级搜索
        $('.j-click-more-search').on('click', function (e) {
            e.stopPropagation()
            $('.more-search-ul').slideToggle(200)
        })

        $(document).on("click",".layui-table-body table.layui-table tbody tr",function(){
            var index = $(this).attr("data-index");
            var tableBox = $(this).parents(".layui-table-box");
            tableDiv = tableBox.find(".layui-table-body.layui-table-main");
            var checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-checkbox div.layui-form-checkbox I");
            if(checkCell.length>0){
                checkCell.click();
            }

        });

        $(document).on("click","td div.laytable-cell-checkbox div.layui-form-checkbox",function(e){
            e.stopPropagation();
        });

    });

    function search(){
        //获取查询条件的值
        var gridCode = $("#regionCode").val(),
            involvedNature = $("#letterType").val(),
            fbDepartNameDet = $("#fbDepartNameDet").val(),
            fbDateStr = $('#fbDate').val().split(' - ');
        var startDateStr = fbDateStr[0],
            fillStatus =  $('#fillStatus').val();

        let selectedThoIdStr = "";
        var selectType = $('#selectType').find('.zhgl-btn-on').data('selectType'.toLowerCase());
        if(selectType == '1'){
            selectedThoIdStr = selectedThoUuidArr.join();
            if(selectedThoUuidArr.length<1){
                selectedThoIdStr = -99;
            }
        }
        wait.reload({
            where:{
                gridCode : gridCode,
                involvedNature : involvedNature,
                caseName : fbDepartNameDet,
                filingTime : startDateStr,
                fillStatus:fillStatus
            },page:{
                curr:1
            }
        })
    }

    //查询所有
    function searchAll(){
        $("#searchBtn").click();
    }

    //查询已选择的列表
    function searchSelected(){
        //获取查询条件的值
        var regionCode = $("#regionCode").val(),
            letterType = $("#letterType").val(),
            fbDepartNameDet = $("#fbDepartNameDet").val(),
            fbDateStr = $('#fbDate').val().split(' - '),
            fillStatus = $('#fillStatus').val();
        console.log("查询条件s");
        console.log(selectedThoUuidArr.join());
        console.log("查询条件s");
        wait.reload({
            where:{
                gridCode : regionCode,
                involvedNature : letterType,
                fbDepartNameDet : fbDepartNameDet,
                filingTime : startDateStr,
                fillStatus : fillStatus
            },page:{
                curr:1
            }
        })
    }

    //新增
    function add() {
        var url = '${rc.getContextPath()}/zhsq/caseFilling/form.jhtml';
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
    }

    //回收
    function recovery() {
        var creatorId = '${creator}';
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length != 1){
            layer.msg("请选择一条提交记录",{skin:'layui-layer-hui'});
            return;
        }
        if(datas[0].fillStatus == "00"){
            layer.msg("请选择一条提交记录进行回收!",{skin:'layui-layer-hui'});
            return;
        }
        if(datas[0].creator != creatorId){
            layer.msg("只有提交者,才有权限回收!",{skin:'layui-layer-hui'});
            return;
        }
        var id = datas[0].undCaseId;
        var params = '?undCaseUuid='+datas[0].undCaseUuid;
        var url = '${rc.getContextPath()}/zhsq/caseFilling/form.jhtml'+params;
        layer.confirm("确定要回收吗?",{
            btn:['确定','取消'],
            shade:[0.3]
        },function(index) {
            var submitLoad = layer.load(1);//打开遮罩
            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/caseFilling/recovery.jhtml',
                data:{id:id},
                dataType: 'json',
                success: function(response) {
                    if(response && response.success) {
                        layer.confirm("是否进入数据填报页面?",{
                            btn:['是','否'],
                            shade:[0.3]
                        },function(index) {
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
                        },
                            function(){
                                // 按钮2的事件
                                $(".layui-laypage-btn").click();
                                layer.msg("回收成功",{skin:'layui-layer-hui'});
                            }
                        )
                    } else {
                        layer.msg("回收失败",{skin:'layui-layer-hui'});
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
    function edit(){
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length != 1){
            layer.msg("请选择一条记录",{skin:'layui-layer-hui'});
            return;
        }
        if(datas[0].fillStatus != "00"){
            layer.msg("只有草稿状态才可以编辑",{skin:'layui-layer-hui'});
            return;
        }
        var params = '?undCaseUuid='+datas[0].undCaseUuid;
        var url = '${rc.getContextPath()}/zhsq/caseFilling/form.jhtml'+params;
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
        if(datas.length < 1){
            layer.msg("请选择一条记录",{skin:'layui-layer-hui'});
            return;
        }

        for(var i=0;i<datas.length;i++){
            if(datas[i].fillStatus != "00"){
                layer.msg("只有草稿状态才可以删除",{skin:'layui-layer-hui'});
                return;
            }
        }
            var ids = "";
            var idList = new Array();　//创建一个数组
            for(var i=0;i<datas.length;i++){
                ids += datas[i].undCaseUuid + ",";
            }
            ids = ids.substring(0,ids.length-1);
            layer.confirm("确定要删除吗?",{
                btn:['确定','取消'],
                shade:[0.3]
            },function(index) {
                var submitLoad = layer.load(1);//打开遮罩
                $.ajax({
                    type: 'POST',
                    url: '${rc.getContextPath()}/zhsq/caseFilling/del.jhtml',
                    data:{ids:ids},
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

</script>



</html>