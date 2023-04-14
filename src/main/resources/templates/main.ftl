<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>首页--后台管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="icon" href="${base}/static/favicon.ico">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="//at.alicdn.com/t/font_tnyc012u2rlwstt9.css" media="all" />
    <link rel="stylesheet" href="${base}/static/css/main.css" media="all" />
</head>
<body class="childrenBody">
<#--<div class="panel_box row">-->
<#--    <#if (userMenu?size>0)>-->
<#--        <#list userMenu as items>-->
<#--            <div class="panel col <#if (!items_has_next)>max_panel</#if> ">-->
<#--                <a href="javascript:" data-url="${base}${items.href}">-->
<#--                    <div class="panel_icon" <#if (items.bgColor != "")>style="background-color: ${items.bgColor}"<#else>style="background-color: #54ade8"</#if>>-->
<#--                        <i class="layui-icon" data-icon="${items.icon}">${items.icon}</i>-->
<#--                    </div>-->
<#--                    <div class="panel_word newMessage">-->
<#--                        <span>${items.dataCount}</span>-->
<#--                        <cite>${items.name}</cite>-->
<#--                    </div>-->
<#--                </a>-->
<#--            </div>-->
<#--        </#list>-->
<#--    </#if>-->
<#--</div>-->
<div class="row">
    <div class="sysNotice col">
        <blockquote class="layui-elem-quote title">更新日志</blockquote>
        <div class="layui-elem-quote layui-quote-nm">
            ${site.remarks}
        </div>
    </div>
    <div class="sysNotice col">
        <blockquote class="layui-elem-quote title">系统基本参数</blockquote>
        <table class="layui-table">
            <colgroup>
                <col width="150">
                <col>
            </colgroup>
            <tbody>
            <tr>
                <td>网站名称</td>
                <td class="version">${site.name}</td>
            </tr>
            <tr>
                <td>当前版本</td>
                <td class="homePage">${site.version}</td>
            </tr>
            <tr>
                <td>服务器环境</td>
                <td class="server">${site.server}</td>
            </tr>
            <tr>
                <td>数据库版本</td>
                <td class="dataBase">${site.database}</td>
            </tr>
            <tr>
                <td>最大上传限制</td>
                <td class="maxUpload">${site.maxUpload}</td>
            </tr>
            <tr>
                <td>当前用户角色</td>
                <td class="userRights">
                    <#if (currentUser.roleLists?? && currentUser.roleLists?size>0)>
                        <#list currentUser.roleLists as items>
                            <span class="layui-badge layui-bg-green">${items.name}</span>
                        </#list>
                    </#if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script>
    layui.use(['layer','jquery','form'],function(){
        var layer = layui.layer,
                $ = layui.jquery,
                form = layui.form;

        $(".panel a").on("click",function(){
            window.parent.addTab($(this));
        });
    });
</script>
</body>
</html>