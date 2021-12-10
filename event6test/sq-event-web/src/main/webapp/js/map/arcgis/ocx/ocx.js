$(function () {

    //延迟初始化
    $(document).ready(function () {
        setTimeout(function () {
            init();
        }, 50); //这里设置延迟是为了正确加载OCX(取决于电脑性能,具体数值请根据实际情况设定,通常不需要修改 直接调用init()是可行的)
        setTimeout(function () {
            $('#PlayViewOCX').css({
                'width': '100%',
                'height': '100%'
            });
            $('.pop').hide();
        }, 500);//这里设置延迟(数值请根据实际情况来)是防止快速刷新页面导致进程残留  具体清楚进程方式请参考<关闭进程 云台控制>demo中的代码
    });

    //初始化
    function init() {
        var OCXobj = document.getElementById("PlayViewOCX");
        var txtInit = $("#config").val();
        OCXobj.ContainOCX_Init(txtInit);
    }

    
    ////OCX控件视频处理函数
    function play_ocx_do(param) {
        if ("null" == param || "" == param || null == param || "undefined" == typeof param) {
            return;
        } else {
            var OCXobj = document.getElementById("PlayViewOCX");
            OCXobj.ContainOCX_Do(param);
        }
    }
});

