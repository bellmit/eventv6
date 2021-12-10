 var LayuiComp = LayuiComp||{};
 LayuiComp.laydate = LayuiComp.laydate||{};
 //手动触发脏数据校验
 LayuiComp.isDirtyData = function(flag){
     LayuiComp.__form_change__ = !flag?true:!!flag;
 }
 LayuiComp.laydate.render = function(opt){
     var laydate = layui.laydate;
     if(opt.done){
         var oDone = opt.done;
         opt.done = function(){
             LayuiComp.__form_change__ = true;
             oDone.apply(this,arguments);
         }
     }else{
         opt.done = function(value, date, endDate){
             LayuiComp.__form_change__ = true;
         }
     }
     return laydate.render(opt);
 };
 LayuiComp.addWin=function(layIdx){
     LayuiComp._windowOpened = LayuiComp._windowOpened||[];
     LayuiComp._windowOpened.push(layIdx);
 }
 LayuiComp.removeWin = function(){
     if(!!parent.LayuiComp._windowOpened){
         return undefined;
     }
     return parent.LayuiComp._windowOpened.pop();
 }
 //打开全屏弹窗
 LayuiComp.openFullWin = function(url){
     var indexAdd = layer.open({
         type: 2,
         title: false,
         closeBtn: 0,
         area: ['100%', '100%'],
         shadeClose: true, //点击遮罩关闭
         content: url,
         success: function (layero) {
         }
     });
     LayuiComp.addWin(indexAdd);
 }
 Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

 //立即执行，非dom加载后执行
(function($){
        /**
         * 表单序列化成json对象
         *
         */
        $.fn.serializeJson = function(){ 
            var serializeObj = {};
            var array=this.serializeArray();
            var str=this.serialize();
            $(array).each(function(){
                if(serializeObj[this.name]){
                    if($.isArray(serializeObj[this.name])){
                        serializeObj[this.name].push(this.value);  
                    }else{  
                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
                    }
                }else{
                    serializeObj[this.name]=this.value;   
                }  
            });
            return serializeObj;  
        };

})(jQuery);

//$(function(){
layui.use(['jquery','form'],function(){
    var $ = layui.jquery,form=layui.form
    ;
    $(function(){
        //layer脏数据方法
        var __form_change__ = false;
        var setFunChange = function (data) {
            LayuiComp.__form_change__ = true;
        };
        form.on('select', setFunChange);
        form.on('checkbox', setFunChange);
        form.on('switch', setFunChange);
        form.on('radio', setFunChange);

        $("body").on("change", "input", setFunChange);
        $("body").on("change", "textarea", setFunChange);


        var _form_change_fun = function () {
            //多选框改变事件
            $("body select[multiple]").each(function () {
                if ($(this).data("data-dirty") == 'true') {
                    LayuiComp.__form_change__ = true;
                    return;
                }
            });
            return LayuiComp.__form_change__;
        }
        //关闭layer弹框脏数据验证
        $('button[lay-filter="dirty-close"]').on('click',function(){
            var isRefresh = $(this).hasClass("refresh");
            var $clos = function(){
                var layIdx = LayuiComp.removeWin();
                if(!!layIdx){
                    parent.layer.close(layIdx)
                }else{
                    parent.layer.closeAll();
                }
            }
            if(_form_change_fun()){
                layer.confirm("确定要关闭窗口（未保存数据将丢失）?",{
                    btn:['确定','取消'],
                    shade:[0.3]
                },function(index) {
                    if(isRefresh){
                        parent.window.location.reload();
                    }
                    $clos();
                })
                return;
            }
            $clos();
        });
        //正常关闭弹框
        $('button[lay-filter="close"]').on('click',function(){
            var layIdx = LayuiComp.removeWin();
            if(!!layIdx){
                parent.layer.close(layIdx)
            }else{
                parent.layer.closeAll();
            }
        })
    });
});


