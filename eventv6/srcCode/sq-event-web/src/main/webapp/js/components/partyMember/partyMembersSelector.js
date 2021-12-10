
(function($) {
    $.fn.partyMembersSelector = function(options, param) {// 定义扩展组件
        if(!param){
            param  = {};//当前搜索条件参数
        }

        /**获取值数组*/
        this.getDataValue = function(){
            return $(this).baseCombo('getValue');
        }
        /**设置值**/
        this.setData = function(obj){
            return $(this).baseCombo('setValue',obj);
        }

        // 当options为字符串时，说明执行的是该插件的方法。
        if (typeof options == "string") {
            return $.fn.baseCombo.apply(this, arguments);
        }
        options = options || {};
        options.dataUrl = '/resident/fetchFamilyMembersById.jhtml';
        options.idField = 'partyId';
        if(options.textField==null){
            options.textField = 'partyName';
        }
        if(options.columns==null){
            options.columns = [ [ {
                field : 'partyName',
                title : '姓名',
                width : 70,
                align : 'center'
            },{
                field : 'genderCN',
                align : 'center',
                title : '性别',
                width : 50
            },{
                field : 'identityCard',
                align : 'center',
                title : '身份证号',
                width : 120
            },{
                field : 'householderRelationCN',
                align : 'center',
                title : '家庭关系',
                width : 80
            } ] ];
        }
        var zuid = $(this)[0].id;
        // 当该组件在一个页面出现多次时，this是一个集合，故需要通过each遍历。
        return this
            .each(function() {
                var jq = $(this);
                // $.fn.baseCombo.parseOptions(this)作用是获取页面中的data-options中的配置
                var opts = $.extend({}, $.fn.baseCombo.parseOptions(this), options);
                // 把配置对象myopts放到$.fn.baseCombo这个函数中执行。
                var newopts = $.extend(true,
                    {
                        dataDomain : '',
                        panelWidth : 450,
                        height : 30,
                        fitColumns : true,
                        editable : false,
                        rownumbers : true,// 序号
                        pagination : true,// 是否分页
						pageSize : 10,// 每页显示的记录条数，默认为10
                        pageList : [ 10 ],// 可以设置每页记录条数的列表
                        queryParams:param,
                        loader : function(param, success) {
                            $.ajax({
                                url : options.dataDomain + options.dataUrl,
                                method : 'post',
                                data : param,
                                beforeSend: function () {
                                    $("<div class='selector-mask' style='position: absolute;left: 0; top: 0;width: 100%;  height: 100%;opacity: 0.3;filter: alpha(opacity=30); display: none;background: #ccc'></div>").css( {
                                        display : "block",
                                        width : "100%",
                                        height : $(window).height()
                                    }).appendTo(".panel.combo-p");
                                },
                                success : function(data) {
                                    success(data);
                                    var pager = jq.baseCombo('grid').datagrid('getPager');
                                    $(pager).pagination({
                                        layout:['info'],
                                        displayMsg: '共'+data.total+'条记录'
                                    });
                                },
                                error : function(data) {
                                    console.log(data);
                                },
                                complete: function () {
                                    $(".selector-mask").remove();
                                }
                            });
                        }
                    }, opts);
                $.fn.baseCombo.call(jq, newopts);
               /* var pager = jq.baseCombo('grid').datagrid('getPager');
                $(pager).pagination({
                    showPageList:false,
                    layout:['info'],
                    displayMsg: ''
                });*/
            });
    };

})(jQuery);