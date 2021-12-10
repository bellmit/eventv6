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


</body>

<!-- 表格头部工具栏 -->
<script type="text/html" id="tbTollsBar">
    <div class="layui-col-xs12">
        <p class="text-nor font-size-15 cor-333 line-height-30 font-bold fl"><i class="text-icon"></i>数据列表</p>
        <div class="crudbox zhgl-btn-ext1 ca-btn-ext ca-btn-ext1 layui-fl ml30" id="selectType">
            <button type="button" class="layui-btn zhgl-btn-on" data-selectType="0">全部</button>
            <button type="button" class="layui-btn" data-selectType="1">已选择</button>
        </div>
        <div class="crudbox fr">
            <@actionCheck></@actionCheck>
<!--
                <button type="button" class="layui-btn layui-btn-ext2" onclick="exportData()">导出</button>
                <button type="button" class="layui-btn layui-btn-ext1"  onclick="del()">删除</button>
-->
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
                , url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/jurisdiction/listData.json'
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
                    regionCode : '${regionCode}'
                }
                , cols: [[
                    { type: 'numbers' }
                    //, { type: 'radio', hide:false }
                    ,{type:'checkbox',hide:false}
                    , { field: 'thoId', hide:true}
                    , { field: 'thoUuid', hide:true}
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
                            return deptName;
                        }}
                    , { field: 'bizStatus', title: '状态',templet:function(rec){
                            return bizStatusMap.get(rec.bizStatus)
                        }}
                ]]
                , page: true
                , done: function (res) {//表格的回调
/*                    console.log("res");
                    console.log(res);
                    console.log("res");*/
                    $('.ca-btn-ext1').find('.layui-btn').eq(indexBtn).addClass('zhgl-btn-on').siblings().removeClass('zhgl-btn-on');
                    $('#selectType').on('click','.layui-btn',function(){
                        $(this).addClass('zhgl-btn-on').siblings().removeClass('zhgl-btn-on');
                        indexBtn = $(this).index();
                        search();
                    })

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
/*                    var index = $(this).attr("data-index");
                    var tableBox = $(this).parents(".layui-table-box");
                    tableDiv = tableBox.find(".layui-table-body.layui-table-main");
                    var checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-checkbox div.layui-form-checkbox I");
                    if(checkCell.length>0){
                        checkCell.click();
                    }*/

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
                    var url = '${rc.getContextPath()}/zhsq/eliminateLetterTho/detail.jhtml?thoUuid='+obj.data.thoUuid+'&instanceId='+obj.data.instanceId;
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

/*            table.on('row(data1)',function(obj){
                obj.tr.find('input[type="radio"][lay-type]').first().nextAll().not('.layui-form-radioed').click();
            });*/

            //监听行单击事件（双击事件为：rowDouble）
/*            table.on('row(data1)', function(obj){
                var data = obj.data;
                console.log(data);
                var checkStatus = table.checkStatus('data1')
                console.log("选择的数量："+checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
                var index = $(this).attr("data-index");
                var tableBox = $(this).parents(".layui-table-box");
                tableDiv = tableBox.find(".layui-table-body.layui-table-main");
            });*/


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
                }else{
                    let intersection = Array.from(new Set(selectedThoUuidArr.filter(v=>bSet.has(v))));
                    selectedThoUuidArr = [];
                    selectedThoUuidArr.push.apply(selectedThoUuidArr,intersection);
                    console.log("交集");
                    console.log(intersection);
                    console.log(selectedThoUuidArr);
                    console.log("交集");

/*                    let difArr = Array.from(new Set(selectedThoUuidArr.concat(idArr).filter(v=>aSet.has(v) && !bSet.has(v))));
                    selectedThoUuidArr = [];
                    selectedThoUuidArr.push.apply(selectedThoUuidArr,difArr);
                    console.log("差集");
                    console.log(difArr);
                    console.log(selectedThoUuidArr);
                    console.log("差集");*/
                }
                console.log("aaaa");
                console.log(selectedThoUuidArr);
                console.log("bbbb");
                // var checkStatus = table.checkStatus('data1')
                // console.log("选择的数量："+checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
                // console.log(obj.data); //选中行的相关数据
                // console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
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

/*            let selectedId = tableDiv.find("tr[data-index=" + index + "]").find("td[data-field=thoId]").find("div.layui-table-cell").text();
            if(checkCell.parent().hasClass('layui-form-checked')){
                selectedThoUuidArr.push(selectedId);
                console.log(selectedThoUuidArr);
            }else{
                for(let i=0;i<selectedThoUuidArr.length;i++){
                    if(selectedId == selectedThoUuidArr[i]){
                        selectedThoUuidArr.splice(i, 1);
                        break;
                    }
                }
                console.log(selectedThoUuidArr);
            }*/
        });

        $(document).on("click","td div.laytable-cell-checkbox div.layui-form-checkbox",function(e){
            e.stopPropagation();
        });

    });

    function search(){
        //获取查询条件的值
        var regionCode = $("#regionCode").val(),
            letterType = $("#letterType").val(),
            fbDepartNameDet = $("#fbDepartNameDet").val(),
            fbDateStr = $('#fbDate').val().split(' - ');
        var startDateStr = fbDateStr[0],
            endDateStr = fbDateStr[1]?fbDateStr[1]:"";

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
                regionCode : regionCode,
                letterType : letterType,
                fbDepartNameDet : fbDepartNameDet,
                fbDateStart : startDateStr,
                fbDateEnd : endDateStr,
                selectedThoIdStr : selectedThoIdStr
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
            fbDateStr = $('#fbDate').val().split(' - ');
        var startDateStr = fbDateStr[0],
            endDateStr = fbDateStr[1]?fbDateStr[1]:"";
        console.log("查询条件s");
        console.log(selectedThoUuidArr.join());
        console.log("查询条件s");
        wait.reload({
            where:{
                regionCode : regionCode,
                letterType : letterType,
                fbDepartNameDet : fbDepartNameDet,
                fbDateStart : startDateStr,
                fbDateEnd : endDateStr,
                selectedThoIdStr : selectedThoUuidArr.join()
            },page:{
                curr:1
            }
        })
    }

    //导出
    function exportData(){
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length == 0){
            layer.msg("请选择一条归档数据",{skin:'layui-layer-hui'});
            return;
        }
        for(var i=0;i<datas.length;i++){
            let curflowStep = datas[i].flowStep;
            if(curflowStep !== 'end1'){
                layer.msg("请选择一条归档数据",{skin:'layui-layer-hui'});
                return;
            }
        }
        let exportIds = "";
        for(var i=0;i<datas.length;i++){
            exportIds += datas[i].thoUuid + ",";
        }
        exportIds = exportIds.substring(0,exportIds.length-1);
        var url = "${rc.getContextPath()}/zhsq/eliminateLetterThoExport/exportOut.jhtml?exportThoUuid="+exportIds;
        var submitLoad = layer.load(1);//打开遮罩
        $.ajax({
            type: 'POST',
            url: url,
            data: {

            },
            dataType: 'json',
            success: function (data) {
                console.log(data);
                window.open(data);
            },
            error: function (data) {
                layer.msg("连接超时", {skin: 'layui-layer-hui'});
            },
            complete: function () {
                if (submitLoad != null) {
                    layer.close(submitLoad);//关闭遮罩
                }
            }
        });

    }

    //删除
    function del(){
        var datas = layui.table.checkStatus('data1').data;
        if(datas.length < 1){
            layer.msg("请选择一条记录",{skin:'layui-layer-hui'});
        }else{

            var ids = "";
            var arr = [];
            for(var i=0;i<datas.length;i++){
                let obj = {};
                ids += datas[i].thoUuid + ",";
                obj.thoUuid = datas[i].thoUuid;
                arr.push(obj);
            }
            ids = ids.substring(0,ids.length-1);
            layer.confirm("确定要删除吗?",{
                btn:['确定','取消'],
                shade:[0.3]
            },function(index) {
                var submitLoad = layer.load(1);//打开遮罩
                $.ajax({
                    type: 'POST',
                    url: '${rc.getContextPath()}/zhsq/eliminateLetterTho/deleteBatch.jhtml',
                    contentType: "application/json;charset=UTF-8",
                    data: JSON.stringify(arr),
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