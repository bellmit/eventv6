<!--
#PC公用页面
说明：
#1、该页面仅存放所有PC页面都需要的文件
#2、注意是所有页面都需要的。否则请考虑放其他文件，比如列表通用的放：src/main/webapp/WEB-INF/template/component/common_list_files.ftl
 -->

<!-- layui样式 -->
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/layui-v2.5.5/layui/css/layui.css" />
<!--本部样式-->
<!--引入 重置默认样式 statics/zhxc -->
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/zongzhi/pages/blue/css/global.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/zongzhi/pages/blue/css/layuiExtend.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_wangge/zongzhi/pages/blue/css/main.css" />
<#--<link rel="stylesheet" type="text/css" href="${re.getContextPath}/css/sz_custom.css">-->
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="${uiDomain}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${uiDomain}/web-assets/plugins/layui-v2.5.5/layui/layui.js"></script>
<script src="${rc.getContextPath()}/js/common.js"></script>
<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js"></script>
</script>