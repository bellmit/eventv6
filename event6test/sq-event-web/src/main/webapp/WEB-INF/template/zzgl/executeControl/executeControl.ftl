<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>布控库列表</title>
	<#include "/component/szcommonFiles-1.1.ftl" />
	<#include "/component/szcomponentsFiles-1.1.ftl" />
    <style>
        .layui-edge {
            margin-right: 5px;
            border-width: 5px;
            border-top-color: #333;
            margin-top: -2px;
        }
        .layui-coop-btn-on, .layui-coop-btn-nor:not(.layui-coop-btn-dis):hover {
            background-image: -webkit-linear-gradient(-90deg, #0a7ef6 0%, #fad000 100%);
            background-image: -moz-linear-gradient(-90deg, #fab905 0%, #fad000 100%);
            background-image: -o-linear-gradient(-90deg, #fab905 0%, #fad000 100%);
            background-image: linear-gradient(-90deg, #148694 0%, #1049e9 100%);
            box-shadow: 0px 5px 10px rgba(247, 167, 8, 0.3);
            color: #fff;
        }
    </style>
</head>

<body class="bg-f5f5f5">

<!-- 管理布控库列表弹窗 -->
<div class="layui-row layui-form lay-cxkh-con1 manage1 layui-h-p100" hidden >
    <div class="layui-cxkh-tb layui-cxhk-h2 mlf20 mrf20 mtf20">
        <table class="layui-table" lay-skin="nob">
            <colgroup>
                <col width="10%">
                <#--<col width="20%">-->
                <col width="30%">
                <col width="18%">
                <col width="32%">
                <col>
            </colgroup>
            <thead>
            <tr>
                <th>序号</th>
                <#--<th>管理员</th>-->
                <th>布控库名称</th>
                <th>布控布类型</th>
                <th>描述</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
        <div class="layui-cxhk-h3 layui-niceS1">
            <form id="submitForm">
                <table class="layui-table" lay-skin="nob">
                    <colgroup>
                        <col width="10%">
                        <#--<col width="20%">-->
                        <col width="30%">
                        <col width="18%">
                        <col width="32%">
                        <col>
                    </colgroup>
                    <tbody id="titleTable">
                    </tbody>
                </table>
                <button id="saveBtn" type="button" class="layui-btn layui-hide" lay-submit lay-filter="submit">提交</button>
            </form>
        </div>
    </div>
    <div class="layui-cxkh-add mlf20 mrf20">
        <button class="layui-coop-btn-add mtf20" id="title-add"><i class="layui-icon layui-icon-addition mr10"></i>添加布控库信息</button>
    </div>
</div>


<div class="layui-row lay-file-add layui-show">
    <div class="layui-fluid">
        <div class="layui-col-xs12 bg-f5f5f5 lay-nor-hd layui-ptb5">
            <p class="text-lay-tt text-nor font-size-15 cor-333 font-bold fl">

            </p>
        </div>
        <div class="layui-col-xs12 lay-nor-bd">
            <div class="layui-col-xs12 layui-h-p100 bg-f5f5f5">
                <div class="layui-coop-lt bg-fff">
                    <div class="layui-fluid">
                        <div class="layui-col-xs12">
                            <button class="layui-coop-btn-add mt15" id="cxkhAdd" ><i class="layui-icon layui-icon-edit mrf5"></i>管理布控库</button>
                            <!-- 暂无数据 -->
                            <p id="noData" class="layui-text font-size-18 cor-blue2 font-bold text-align-c layui-hide mtf70">暂无布控库</p>

                            <!-- 布控库选择 -->
                            <div id="titleDiv" class="layui-coop-btn-w layui-btn-ali-lf layui-hide mt15">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-coop-rt bg-fff">

                    <!-- 暂无数据层 -->
                    <div id="conNoData" class="layui-col-xs12 layui-h-p100 layui-hide">
                        <div class="layui-sup-nd">
                            <div class="layui-nd-con">
                                <img src="${uiDomain}/web-assets/_zhcs/shenzhen-fp/images/icon-timeline-no-data.png">
                                <p>暂无数据可供查看</p>
                            </div>
                        </div>
                    </div>

                    <!-- 内容层 -->
                    <div class="layui-col-xs12 layui-h-p100 conHide layui-hide" >
                       <iframe id="monitorTask" name="monitorTask" src="${rc.getContextPath()}/zhsq/event/monitorTask/index.jhtml"  width="100%" height="100%" frameborder="0"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<script>
    var layerIndex;
    var ids = [];

    $(function () {
        layui.use(['layer', 'element', 'table','form'], function () {
            var element = layui.element,

                    layer = layui.layer,
                    form = layui.form,
                    table = layui.table;

            $('.layui-cxkh-det').on('click', '.layui-input', function () {
                //$('.layui-ipt-edit').removeClass('layui-ipt-edit');
                var x = $(this).parents("tr").attr("posx");
                var y = $(this).attr("posy");
                var titleStr = '序号'+x+' - ';
                if (y==1){
                    titleStr += '考核指标';
                }else if (y==2){
                    titleStr += '数据来源';
                }else if (y==3){
                    titleStr += '相关要求';
                }else{
                    titleStr = '';
                }

                $(this).addClass('layui-ipt-edit');
                layer.prompt({
                            skin: 'layer-ext-sch layer-type1-btn2 layui-cxkh-txa',
                            formType: 2,
                            title: titleStr,
                            closeBtn: 0,
                            btnAlign: 'c',
                            value: $(this).val(),
                            area: ['460px', '186px'], //自定义文本域宽高
                            shadeClose: true, //点击遮罩关闭
                            btn2: function() {//取消按钮的回调
                                $('.layui-ipt-edit').removeClass('layui-ipt-edit');
                            }
                        }
                        , function (value, index, elem) {
                            // alert(value); //得到value
                            $('.layui-ipt-edit').val(value);
                            layer.close(index);
                            $('.layui-ipt-edit').removeClass('layui-ipt-edit');
                        }
                );
            });

            //未提交
            var winH = window.winH || $(window).height(),
                    layH1 = winH * .7;
            $('.layui-niceS1').niceScroll({
                cursorcolor: "#ccc",
                cursoropacitymax: 0.8,
                cursorwidth: "4px",
                cursorborderradius: "4px",
                cursorborder: 'none',
                autohidemode: false,
                zindex: "99999999"
            }).hide();
            //添加布控库内容
            $('#cxkhAdd').on('click', function () {

                getTitle(2);
                layerIndex = layer.open({
                    skin: 'layer-ext-sch layer-type1-btn2',
                    type: 1,
                    title: '添加布控库内容',
                    btn: ['确 定', '取 消'],
                    btn1: function (index, layero) {
                        //按钮【确定】的回调
                        $("#saveBtn").click();
                        $(".nicescroll-cursors").height("0");
                    },
                    btn2: function (index, layero) {
                        //按钮【取消】的回调
                        $(".nicescroll-cursors").height("0");
                    },
                    btnAlign: 'c',
                    area: ['720px', '' + layH1 + 'px'],
                    shadeClose: true, //点击遮罩关闭
                    content: $('.manage1'),
                    success: function () {
                        setTimeout(function () {
                            $('.layui-niceS1').show().niceScroll().resize();
                        }, 300);
                    },
                    cancel : function(){
                        // 你点击右上角 X 取消后要做什么
                        $(".nicescroll-cursors").height("0");
                    }
                });
            });

            //监听提交
            form.on('submit(submit)', function(data){
                var flag = LayuiComp.valdationCom.verification($("#submitForm"));
                var data = $('#submitForm').serializeArray();
                data.push({"name":"delIds","value":ids});
                console.log(ids)
                if(flag){
                    var submitLoad = layer.load(1);//打开遮罩
                    $.ajax({
                        type: 'POST',
                        url: '${rc.getContextPath()}/zhsq/event/executeControl/save.json',
                        data: data,
                        dataType: 'json',
                        success: function(data) {
                            console.log(data);
                            if(data.result == "success") {
                                layer.msg(data.tipMsg == "add" ? "新增成功" : "修改成功", {skin: 'layui-layer-hui'});
                                layer.close(layerIndex);
                                window.location.reload();//页面刷新
                                getTitle(1);
                            } else {
                                layer.msg("操作失败", {skin: 'layui-layer-hui'});
                            }
                        },
                        error: function(data) {
                            layer.msg("连接超时",{skin:'layui-layer-hui'});
                        },
                        complete : function() {
                            layer.close(submitLoad);//关闭遮罩
                        }
                    });
                }
            });

            // tab
            $('.layui-coop-btn-w').on('click', '.layui-coop-btn-nor', function () {

                if ($(this).hasClass('layui-coop-btn-dis')) {
                    return false;
                } else {
                    $(this).addClass('layui-coop-btn-on').siblings().removeClass('layui-coop-btn-on');
                }
                var idName = $(this).parent().prop("id");
                var id = $(this).prop("id");
                var controlLibraryId = $(this).attr("controlLibraryId");
                var libType = $(this).attr("libType");
                if(idName == "titleDiv"){
                    toChildValue(id,controlLibraryId,libType);
                }else if(idName == "contentDiv"){
                    getTarget();
                }
            });
        });

        // 计算弹窗内容框高度
        function calcLayBdH() {
            var winW = $(window).width(),
                    winH = $(window).height(),
                    lhH = $('.lay-nor-hd').outerHeight(true),
                    lbH,
                    layCalcAdH = $('.lay-calc-ad').outerHeight(true);
            if (winW <= 1366) {
                lbH = winH - lhH - 10;
            } else {
                lbH = winH - lhH - 15;
            }
            $('.lay-nor-bd').height(lbH);
        }
        calcLayBdH();
        $('.layui-niceS2').niceScroll({
            cursorcolor: "#ccc",
            cursoropacitymax: 0.8,
            cursorwidth: "4px",
            cursorborderradius: "4px",
            cursorborder: 'none',
            autohidemode: false
        });

        $(window).on('resize', function () {
            calcLayBdH();
            $('.layui-niceS2').niceScroll().resize();
        });
        getTitle(1);
    });

    //删除布控库
    $("#titleTable").on('click','.exam-title', function () {
        var flag = true;
        var delId = $(this).attr("delId");

        var length = $(".exam-title").length;
        if (length <= 1){
            layer.msg("最少保留一个布控库",{skin:'layui-layer-hui'});
        }else{
            //先判断布控库中是否有布控任务,有就无法删除
            var controlLibraryId = $(this).attr("controlLibraryId");
            if (controlLibraryId == undefined || controlLibraryId == null || controlLibraryId == "null"){
                var $tr = $(this).parents('tr');
                var orderNum = $tr.children(':first').children('p').html();
                if(orderNum == length){
                    $tr.remove();
                }else{
                    $tr.remove();
                    var tmp;
                    var index;
                    for (var i=orderNum;i<length;i++ ){
                        tmp = parseInt(i)+1;
                        index = parseInt(i)-1;
                        $(".p"+tmp).html(i);
                        $(".p"+tmp).addClass("p"+i).removeClass("p"+tmp);

                        $(".controlLibraryId"+tmp).val(i);
                        $(".controlLibraryId"+tmp).attr("name","infos["+index+"].controlLibraryId");
                        $(".controlLibraryId"+tmp).addClass("controlLibraryId"+i).removeClass("controlLibraryId"+tmp);

                        $(".id"+tmp).attr("name","infos["+index+"].id");
                        $(".id"+tmp).addClass("id"+i).removeClass("id"+tmp);

                        $(".des"+tmp).attr("name","infos["+index+"].description");
                        $(".des"+tmp).addClass("des"+i).removeClass("des"+tmp);

                        $(".libType"+tmp).attr("name","infos["+index+"].libType");
                        $(".libType"+tmp).addClass("libType"+i).removeClass("libType"+tmp);


                        $(".inp"+tmp).attr("name","infos["+index+"].name");
                        $(".inp"+tmp).addClass("inp"+i).removeClass("inp"+tmp);
                        $(".button"+tmp).addClass("button"+i).removeClass("button"+tmp);
                    }
                }
                $('.layui-niceS1').niceScroll().resize();
                return;
            }
            $.ajax({
                type: 'POST',
                url: '${rc.getContextPath()}/zhsq/event/monitorTask/getCount.json',
                data: {libIds:controlLibraryId},
                dataType: 'json',
                async:false,//同步
                success: function(data) {
                    console.log(data);
                    if (data > 0){
                        flag = false;
                        layer.msg("布控库有正在运行的任务,无法删除!",{skin:'layui-layer-hui'});
                    }
                },
                error: function(data) {
                    layer.msg("连接超时",{skin:'layui-layer-hui'});
                }
            });
            if (flag){
                var $tr = $(this).parents('tr');
                console.log($tr)
                var orderNum = $tr.children(':first').children('p').html();
                if(orderNum == length){
                    $tr.remove();
                }else{
                    $tr.remove();
                    var tmp;
                    var index;
                    for (var i=orderNum;i<length;i++ ){
                        tmp = parseInt(i)+1;
                        index = parseInt(i)-1;
                        $(".p"+tmp).html(i);
                        $(".p"+tmp).addClass("p"+i).removeClass("p"+tmp);

                        $(".controlLibraryId"+tmp).val(i);
                        $(".controlLibraryId"+tmp).attr("name","infos["+index+"].controlLibraryId");
                        $(".controlLibraryId"+tmp).addClass("controlLibraryId"+i).removeClass("controlLibraryId"+tmp);

                        $(".id"+tmp).attr("name","infos["+index+"].id");
                        $(".id"+tmp).addClass("id"+i).removeClass("id"+tmp);

                        $(".des"+tmp).attr("name","infos["+index+"].description");
                        $(".des"+tmp).addClass("des"+i).removeClass("des"+tmp);

                        $(".libType"+tmp).attr("name","infos["+index+"].libType");
                        $(".libType"+tmp).addClass("libType"+i).removeClass("libType"+tmp);


                        $(".inp"+tmp).attr("name","infos["+index+"].name");
                        $(".inp"+tmp).addClass("inp"+i).removeClass("inp"+tmp);
                        $(".button"+tmp).addClass("button"+i).removeClass("button"+tmp);
                    }
                }
                $('.layui-niceS1').niceScroll().resize();
                ids.push(delId);
            }
        }
    });

    $("#title-add").on('click', function () {
        var length = $(".exam-title").length;
        var index = length+1;
        var html = '';
        html += '<tr>';
        html += '<td>';
        html += '<p class="text-align-c p'+index+'">'+index+'</p>';
        html += '</td>';
       /* html += '<td>';
        html += '<input type="hidden" name="infos['+length+'].userName" value="${userInfo.partyName}" readonly="readonly" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[32]\']" placeholder="管理员">';
        html += '</td>';*/
        html += '<td>';
        html += '<input type="text" name="infos['+length+'].name" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[25]\']" placeholder="布控库名称">';
        html += '</td>';
        html += '<td>';
        html += '<select id="libType_'+index+'" name="infos['+length+'].libType" lay-verify-custom="required:true" style="margin-right: 5px"></select>';
		html += '</td>';
        html += '<td>';
        html += '<input type="text" name="infos['+length+'].description" class="layui-input inp'+index+'" lay-verify-custom="validType:[\'maxLength[50]\']" placeholder="描述">';
        html += '</td>';
        html += '<td>';
        html += '<div class="text-align-c"><a href="javascript:;" class="button'+index+' exam-title layui-btn layui-icon layui-icon-subtraction"></a></div>';
        html += '</td>';
        html += '</tr>';

        $("#titleTable").append(html);
        $('.layui-niceS1').niceScroll().resize();
        LayuiComp.SingleSelect('libType_'+index+'',{
            selectValue:'',
            pCode:'A002004008001',
            selectedCallback:function(data,originalData){
                console.log(originalData);
            },
            successCallback:function(){
                restSelect();
            },
            context:'${sysDomain}'
        });
    });

    //获取布控库数据
    function getTitle(type) {
        //var submitLoad = layer.load(1);//打开遮罩
        $.ajax({
            type: 'POST',
            url: '${rc.getContextPath()}/zhsq/event/executeControl/getTitle.json',
            data: {},
            async: true,
            dataType: 'json',
            success: function(data) {
                console.log(data);
                if(type == '1'){
                    setTitle(data);
                }else if(type == '2'){
                    setTitleForm(data);
                }
            },
            error: function(data) {
                layer.msg("连接超时",{skin:'layui-layer-hui'});
            },
            complete : function() {
                //layer.close(submitLoad);//关闭遮罩
            }
        });
    }

    //渲染布控库数据
    function setTitle(data) {
        var length = data.length;
        if(length == null || length == undefined || length == 0){
            $("#titleDiv").addClass("layui-hide");
            $("#noData").removeClass("layui-hide");
            $(".conHide").addClass("layui-hide");
            $("#conNoData").removeClass("layui-hide");
        }else {
            $("#conNoData").addClass("layui-hide");
            $(".conHide").removeClass("layui-hide");
            var html = '';

            for (var i=0;i<length;i++){
                if(i == 0){
                    html += '<button controlLibraryId='+data[i].controlLibraryId+' libType='+data[i].libType+' id='+data[i].id+' class="layui-coop-btn-nor layui-coop-btn-on">';
                    html += '<p class="layui-inline">'+data[i].name+'</p>';
                    html += '</button>';
                }else {
                    html += '<button controlLibraryId='+data[i].controlLibraryId+' libType='+data[i].libType+' id='+data[i].id+' class="layui-coop-btn-nor">';
                    html += '<p class="layui-inline">'+data[i].name+'</p>';
                    html += '</button>';
                }
            }
            $("#titleDiv").html(html);
            $("#noData").addClass("layui-hide");
            $("#titleDiv").removeClass("layui-hide");
            window.onload = function () {
                toChildValue(data[0].id,data[0].controlLibraryId,data[0].libType);
            }
        }
    }

    //给子页面传入数据
    function toChildValue(id,controlLibraryId,libType) {
        var txt = id + "," + controlLibraryId + "," + libType;
        var frame = document.getElementById("monitorTask");
        frame.contentWindow.postMessage (txt, '*');
    }

    //渲染布控库表单数据
    function setTitleForm(data) {
        var length = data.length;
        var html = '';
        if(length == null || length == undefined || length == 0){
            html += '<tr>';
            html += '<td>';
            html += '<p class="text-align-c p'+1+'">'+1+'</p>';
            html += '</td>';
            /*html += '<td>';
            html += '<input type="hidden" name="infos['+length+'].userName" value="${userInfo.partyName}" readonly="readonly" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[32]\']" placeholder="管理员">';
            html += '</td>';*/
            html += '<td>';
            html += '<input type="text" name="infos['+length+'].name" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[25]\']" placeholder="布控库名称">';
            html += '</td>';
            html += '<td>';
            html += '<select id="libType_'+length+'" name="infos['+length+'].libType" lay-verify-custom="required:true"></select>';
            html += '</td>';
            html += '<td>';
            html += '<input type="text" name="infos['+length+'].description" class="layui-input des'+index+'" lay-verify-custom="validType:[\'maxLength[50]\']" placeholder="请输入描述">';
            html += '</td>';
            html += '<td>';
            html += '<div class="text-align-c"><a href="javascript:;" class="button'+index+' exam-title layui-btn layui-icon layui-icon-subtraction"></a></div>';
            html += '</td>';
            html += '</tr>';
            $("#id").val("");
            $("#titleTable").html(html);
            LayuiComp.SingleSelect('libType_'+length+'',{
                selectValue:'1',
                pCode:'A002004008001',
                selectedCallback:function(data,originalData){
                    console.log(originalData);
                },
                successCallback:function(){
                    restSelect();
                },
                context:'${sysDomain}'
            });
        }else {
            for (var i=0;i<length;i++){
                if (data[i].description == null) {
                    data[i].description = '';
                }
                var index = i+1;
                html += '<tr>';
                html += '<td>';
                html += '<input type="hidden" class="id'+index+'" name="infos['+i+'].id" value="'+data[i].id+'">';
                html += '<input type="hidden" class="controlLibraryId'+index+'" name="infos['+i+'].controlLibraryId" value="'+data[i].controlLibraryId+'">';
                html += '<input type="hidden" name="infos['+i+'].userIds" value="'+data[i].userIds+'">';
                html += '<p class="text-align-c p'+index+'">'+index+'</p>';
                html += '</td>';
                /*html += '<td>';
                html += '<input type="hidden" name="infos['+i+'].userName" readonly="readonly" value="'+data[i].userName+'" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[32]\']" placeholder="管理员">';
                html += '</td>';*/
                html += '<td>';
                html += '<input type="text" name="infos['+i+'].name" value="'+data[i].name+'" class="layui-input inp'+index+'" lay-verify-custom="required:true,validType:[\'maxLength[25]\']" placeholder="布控库名称">';
                html += '</td>';
                html += '<td>';
                html += '<select class="libType'+index+'" id="libType_'+index+'" name="infos['+i+'].libType" lay-verify-custom="required:true"></select>';
                html += '</td>';
                html += '<td>';
                html += '<input type="text" name="infos['+i+'].description" value="'+data[i].description+'" class="layui-input des'+index+'" lay-verify-custom="validType:[\'maxLength[50]\']" placeholder="请输入描述">';
                html += '</td>';
                html += '<td>';
                html += '<div class="text-align-c"><a delId="'+data[i].id+'" controlLibraryId="'+data[i].controlLibraryId+'" href="javascript:;" class="button'+index+' exam-title layui-btn layui-icon layui-icon-subtraction"></a></div>';
                html += '</td>';
                html += '</tr>';
            }
            $("#id").val(data[0].id);
            $("#controlLibraryId").val(data[0].controlLibraryId);
            $("#titleTable").html(html);
            for (var i=0;i<length;i++){
                var index = i+1;
                LayuiComp.SingleSelect('libType_'+index+'',{
                    selectValue:data[i].libType,
                    pCode:'A002004008001',
                    selectedCallback:function(data,originalData){
                        console.log(originalData);
                    },
                    successCallback:function(){
                        restSelect();
                    },
                    context:'${sysDomain}'
                });
            }
        }
    }
    function restSelect(){
        $('.layui-unselect').on('focus', function (ev) {
            var ev = ev || window.event
                    , selTop = $(this).offset().top
                    , selLeft = $(this).offset().left
                    , selWidth = $(this).parent().width();
            $(this).parent().siblings('.layui-anim').css({
                'top': selTop + 42,
                'left': selLeft,
                'width': selWidth,
                'min-width': 'unset',
                'position': 'fixed'
            });
        });
    }

</script>
</html>